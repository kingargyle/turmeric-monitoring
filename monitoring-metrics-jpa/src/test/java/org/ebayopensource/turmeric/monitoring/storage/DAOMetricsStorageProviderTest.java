/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.ebayopensource.turmeric.monitoring.AbstractJPATest;
import org.ebayopensource.turmeric.monitoring.MetricsDAOImpl;
import org.ebayopensource.turmeric.monitoring.model.MetricComponentValue;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricCategory;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricClassifier;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricId;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricsStorageProvider;
import org.ebayopensource.turmeric.runtime.common.monitoring.MonitoringLevel;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.AverageMetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.LongSumMetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;
import org.ebayopensource.turmeric.utils.jpa.EntityManagerContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DAOMetricsStorageProviderTest extends AbstractJPATest {
    private MetricsStorageProvider metricsStorageProvider;

    @Before
    public void init() {
        metricsStorageProvider = new DAOMetricsStorageProvider(factory, new MetricsDAOImpl());
        metricsStorageProvider.init(Collections.<String, String>emptyMap(), null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 60);
    }

    private List<MetricValueAggregator> deepCopyAggregators(MetricValueAggregator... aggregators) {
        // The aggregator list passed to the storage provider is always a deep copy of the aggregators
        List<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
        for (MetricValueAggregator aggregator : aggregators) {
            result.add((MetricValueAggregator) aggregator.deepCopy(false));
        }
        return result;
    }

    @Test
    public void testSaveFirstSnapshot() throws Exception {
        long timeStamp = System.currentTimeMillis();

        MetricId metricId1 = new MetricId("test_count", "service1", "operation1");
        MetricValue metricValue1 = new LongSumMetricValue(metricId1);
        MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1, MetricCategory.Timing, MonitoringLevel.NORMAL);

        MetricId metricId2 = new MetricId("test_average", "service2", "operation2");
        MetricValue metricValue2 = new AverageMetricValue(metricId2);
        MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2, MetricCategory.Timing, MonitoringLevel.NORMAL);

        // Simulate one call to the service from consumer1
        MetricClassifier metricClassifier1 = new MetricClassifier("consumer1", "sourceDC1", "targetDC1");
        aggregator1.update(metricClassifier1, 1L);
        aggregator2.update(metricClassifier1, 2L);
        // Simulate another call to the service from consumer2
        MetricClassifier metricClassifier2 = new MetricClassifier("consumer2", "sourceDC1", "targetDC1");
        aggregator1.update(metricClassifier2, 1L);
        aggregator2.update(metricClassifier2, 3L);

        List<MetricValueAggregator> aggregators = deepCopyAggregators(aggregator1, aggregator2);

        metricsStorageProvider.saveMetricSnapshot(timeStamp, aggregators);

        EntityManagerContext.open(factory);
        try {
            StringBuilder jpql = new StringBuilder();
            jpql.append("from ").append(org.ebayopensource.turmeric.monitoring.model.MetricValue.class.getName()).append(" as mv");
            EntityManager entityManager = EntityManagerContext.get();
            @SuppressWarnings("unchecked")
            List<org.ebayopensource.turmeric.monitoring.model.MetricValue> metricValues = entityManager.createQuery(jpql.toString()).getResultList();
            assertEquals(4, metricValues.size());
        } finally {
            EntityManagerContext.close();
        }
    }

    @Test
    public void testSnapshotsAreSubtracted() throws Exception {
        long timeStamp = System.currentTimeMillis();

        MetricId metricId1 = new MetricId("test_count", "service1", "operation1");
        MetricValue metricValue1 = new LongSumMetricValue(metricId1);
        MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1, MetricCategory.Timing, MonitoringLevel.NORMAL);

        MetricId metricId2 = new MetricId("test_average", "service2", "operation2");
        MetricValue metricValue2 = new AverageMetricValue(metricId2);
        MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2, MetricCategory.Timing, MonitoringLevel.NORMAL);

        // Simulate first call
        MetricClassifier metricClassifier1 = new MetricClassifier("consumer1", "sourceDC1", "targetDC1");
        aggregator1.update(metricClassifier1, 1L);
        aggregator2.update(metricClassifier1, 2L);

        List<MetricValueAggregator> aggregators = deepCopyAggregators(aggregator1, aggregator2);

        // First save
        metricsStorageProvider.saveMetricSnapshot(timeStamp, aggregators);

        // Simulate second call
        long countDelta = 1L;
        long timeDelta = 3L;
        aggregator1.update(metricClassifier1, countDelta);
        aggregator2.update(metricClassifier1, timeDelta);

        // Second save
        timeStamp += TimeUnit.SECONDS.toMillis(60);
        aggregators = deepCopyAggregators(aggregator1, aggregator2);
        metricsStorageProvider.saveMetricSnapshot(timeStamp, aggregators);

        EntityManagerContext.open(factory);
        try {
            StringBuilder jpql = new StringBuilder();
            jpql.append("from ").append(org.ebayopensource.turmeric.monitoring.model.MetricValue.class.getName()).append(" as mv");
            jpql.append(" where mv.timeStamp = :timeStamp");
            EntityManager entityManager = EntityManagerContext.get();
            Query query = entityManager.createQuery(jpql.toString());
            query.setParameter("timeStamp", timeStamp);
            @SuppressWarnings("unchecked")
            List<org.ebayopensource.turmeric.monitoring.model.MetricValue> metricValues = query.getResultList();
            assertEquals(2, metricValues.size());
            org.ebayopensource.turmeric.monitoring.model.MetricValue countMetricValue;
            org.ebayopensource.turmeric.monitoring.model.MetricValue averageMetricValue;
            if (metricValues.get(0).getMetricComponentValues().size() == 1) {
                countMetricValue = metricValues.get(0);
                averageMetricValue = metricValues.get(1);
            } else {
                countMetricValue = metricValues.get(1);
                averageMetricValue = metricValues.get(0);
            }
            MetricComponentValue countComponent = countMetricValue.getMetricComponentValues().iterator().next();
            assertEquals(countDelta, countComponent.getValue().longValue());
            org.ebayopensource.turmeric.monitoring.model.MetricComponentValue averageCountComponent = averageMetricValue.findMetricComponentValue("count");
            org.ebayopensource.turmeric.monitoring.model.MetricComponentValue averageTimeComponent = averageMetricValue.findMetricComponentValue("totalTime");
            assertEquals(countDelta, averageCountComponent.getValue().longValue());
            assertEquals(timeDelta, averageTimeComponent.getValue().longValue());
        } finally {
            EntityManagerContext.close();
        }
    }

    @Test
    public void testSnapshotsAreAggregated() throws Exception {
        long timeStamp = System.currentTimeMillis();

        MetricId metricId1 = new MetricId("test_count", "service1", "operation1");
        MetricValue metricValue1 = new LongSumMetricValue(metricId1);
        MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1, MetricCategory.Timing, MonitoringLevel.NORMAL);

        MetricId metricId2 = new MetricId("test_average", "service2", "operation2");
        MetricValue metricValue2 = new AverageMetricValue(metricId2);
        MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2, MetricCategory.Timing, MonitoringLevel.NORMAL);

        // Simulate first call
        MetricClassifier metricClassifier1 = new MetricClassifier("consumer1", "sourceDC1", "targetDC1");
        long count1 = 1L;
        aggregator1.update(metricClassifier1, count1);
        long time1 = 2L;
        aggregator2.update(metricClassifier1, time1);

        List<MetricValueAggregator> aggregators = deepCopyAggregators(aggregator1, aggregator2);

        // First save
        metricsStorageProvider.saveMetricSnapshot(timeStamp, aggregators);

        // Simulate second call
        long count2 = 1L;
        aggregator1.update(metricClassifier1, count2);
        long time2 = 3L;
        aggregator2.update(metricClassifier1, time2);

        // Second save
        timeStamp += TimeUnit.SECONDS.toMillis(60);
        aggregators = deepCopyAggregators(aggregator1, aggregator2);
        metricsStorageProvider.saveMetricSnapshot(timeStamp, aggregators);

        // Simulate third call
        long count3 = 1L;
        aggregator1.update(metricClassifier1, count3);
        long time3 = 5L;
        aggregator2.update(metricClassifier1, time3);

        // Let time pass up to 1 hour, the storage provider must have saved aggregated values
        timeStamp += TimeUnit.SECONDS.toMillis(3540);
        aggregators = deepCopyAggregators(aggregator1, aggregator2);
        metricsStorageProvider.saveMetricSnapshot(timeStamp, aggregators);

        EntityManagerContext.open(factory);
        try {
            StringBuilder jpql = new StringBuilder();
            jpql.append("select mv from ").append(org.ebayopensource.turmeric.monitoring.model.MetricValue.class.getName()).append(" as mv");
            jpql.append(" where mv.aggregationPeriod = 3600");
            EntityManager entityManager = EntityManagerContext.get();
            Query query = entityManager.createQuery(jpql.toString());
            @SuppressWarnings("unchecked")
            List<org.ebayopensource.turmeric.monitoring.model.MetricValue> metricValues = query.getResultList();
            assertEquals(2, metricValues.size());
            org.ebayopensource.turmeric.monitoring.model.MetricValue countMetricValue;
            org.ebayopensource.turmeric.monitoring.model.MetricValue averageMetricValue;
            if (metricValues.get(0).getMetricComponentValues().size() == 1) {
                countMetricValue = metricValues.get(0);
                averageMetricValue = metricValues.get(1);
            } else {
                countMetricValue = metricValues.get(1);
                averageMetricValue = metricValues.get(0);
            }
            MetricComponentValue countComponent = countMetricValue.getMetricComponentValues().iterator().next();
            assertEquals(count1 + count2, countComponent.getValue().longValue());
            org.ebayopensource.turmeric.monitoring.model.MetricComponentValue averageCountComponent = averageMetricValue.findMetricComponentValue("count");
            org.ebayopensource.turmeric.monitoring.model.MetricComponentValue averageTimeComponent = averageMetricValue.findMetricComponentValue("totalTime");
            assertEquals(count1 + count2, averageCountComponent.getValue().longValue());
            assertEquals(time1 + time2, averageTimeComponent.getValue().longValue());

            jpql.setLength(0);
            jpql.append("from ").append(org.ebayopensource.turmeric.monitoring.model.MetricValue.class.getName()).append(" as mv");
            jpql.append(" where mv.aggregationPeriod = 60");
            jpql.append(" and mv.timeStamp = :timeStamp");
            query = entityManager.createQuery(jpql.toString());
            query.setParameter("timeStamp", timeStamp);
            metricValues = query.getResultList();
            assertEquals(2, metricValues.size());
        } finally {
            EntityManagerContext.close();
        }
    }

    @Test
    public void testZeroValuesAreNotSaved() throws Exception {
        long timeStamp1 = System.currentTimeMillis();

        MetricId metricId1 = new MetricId("test_count", "service1", "operation1");
        MetricValue metricValue1 = new LongSumMetricValue(metricId1);
        MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1, MetricCategory.Timing, MonitoringLevel.NORMAL);

        MetricId metricId2 = new MetricId("test_average", "service2", "operation2");
        MetricValue metricValue2 = new AverageMetricValue(metricId2);
        MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2, MetricCategory.Timing, MonitoringLevel.NORMAL);

        // Simulate first call
        MetricClassifier metricClassifier1 = new MetricClassifier("consumer1", "sourceDC1", "targetDC1");
        long count1 = 1L;
        aggregator1.update(metricClassifier1, count1);
        long time1 = 2L;
        aggregator2.update(metricClassifier1, time1);

        List<MetricValueAggregator> aggregators = deepCopyAggregators(aggregator1, aggregator2);

        // First save
        metricsStorageProvider.saveMetricSnapshot(timeStamp1, aggregators);

        // Time passes, no more calls, but the timer asks again to save the metrics
        aggregators = deepCopyAggregators(aggregator1, aggregator2);
        long timeStamp2 = System.currentTimeMillis();
        metricsStorageProvider.saveMetricSnapshot(timeStamp2, aggregators);

        // Be sure that no new metrics are saved, since nothing happened in between
        EntityManagerContext.open(factory);
        try {
            StringBuilder jpql = new StringBuilder();
            jpql.append("from ").append(org.ebayopensource.turmeric.monitoring.model.MetricValue.class.getName()).append(" as mv");
            jpql.append(" where mv.timeStamp = :timeStamp");
            EntityManager entityManager = EntityManagerContext.get();
            Query query = entityManager.createQuery(jpql.toString());
            query.setParameter("timeStamp", timeStamp2);
            @SuppressWarnings("unchecked")
            List<org.ebayopensource.turmeric.monitoring.model.MetricValue> metricValues = query.getResultList();
            assertTrue(metricValues.isEmpty());
        } finally {
            EntityManagerContext.close();
        }
    }
}
