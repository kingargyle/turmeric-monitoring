/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.model.Machine;
import org.ebayopensource.turmeric.monitoring.model.Metric;
import org.ebayopensource.turmeric.monitoring.model.MetricCategory;
import org.ebayopensource.turmeric.monitoring.model.MetricClassifier;
import org.ebayopensource.turmeric.monitoring.model.MetricComponentDef;
import org.ebayopensource.turmeric.monitoring.model.MetricComponentValue;
import org.ebayopensource.turmeric.monitoring.model.MetricDef;
import org.ebayopensource.turmeric.monitoring.model.MetricValue;
import org.ebayopensource.turmeric.monitoring.model.MonitoringLevel;
import org.ebayopensource.turmeric.monitoring.v1.services.CriteriaInfo;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorInfos;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGraphData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGroupData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricResourceCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.ResourceEntity;
import org.ebayopensource.turmeric.monitoring.v1.services.ResourceEntityRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.SortOrderType;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.SystemMetricDefs;
import org.ebayopensource.turmeric.runtime.error.model.ErrorValue;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * The Class DAOSOAMetricsQueryServiceProviderTest.
 */
public class DAOSOAMetricsQueryServiceProviderTest {
    private static EntityManagerFactory entityManagerFactory;
    private SOAMetricsQueryServiceProvider provider;
    private static long now = System.currentTimeMillis();
    private static long oneMinuteAgo = now - TimeUnit.SECONDS.toMillis(60);
    private static long twoMinutesAgo = oneMinuteAgo - TimeUnit.SECONDS.toMillis(60);

    /**
     * Creates the entity manager factory.
     *
     * @throws Exception the exception
     */
    @BeforeClass
    public static void createEntityManagerFactory() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("metrics");
        createData();
    }

    /**
     * Creates the data.
     *
     * @throws Exception the exception
     */
    public static void createData() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            try {
                // Create the metricDefs
                MetricDef opTimeTotal = new MetricDef(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(),
                                MetricCategory.TIMING, MonitoringLevel.NORMAL);
                MetricComponentDef opTimeTotal_1 = new MetricComponentDef("count", Long.class.getName());
                opTimeTotal.addMetricComponentDef(opTimeTotal_1);
                MetricComponentDef opTimeTotal_2 = new MetricComponentDef("totalTime", Double.class.getName());
                opTimeTotal.addMetricComponentDef(opTimeTotal_2);
                entityManager.persist(opTimeTotal);
                MetricDef opErrTotal = new MetricDef(SystemMetricDefs.OP_ERR_TOTAL.getMetricName(),
                                MetricCategory.ERROR, MonitoringLevel.NORMAL);
                MetricComponentDef opErrTotal_1 = new MetricComponentDef("value", Long.class.getName());
                opErrTotal.addMetricComponentDef(opErrTotal_1);
                entityManager.persist(opErrTotal);
                MetricDef opErrRequest = new MetricDef(SystemMetricDefs.OP_ERR_CAT_REQUEST.getMetricName(),
                                MetricCategory.ERROR, MonitoringLevel.NORMAL);
                MetricComponentDef opErrRequest_1 = new MetricComponentDef("value", Long.class.getName());
                opErrRequest.addMetricComponentDef(opErrRequest_1);
                entityManager.persist(opErrRequest);

                // Create the metrics
                Metric metric_1 = new Metric("service_1", "operation_1_1", opTimeTotal);
                entityManager.persist(metric_1);
                Metric metric_2 = new Metric("service_1", "operation_1_2", opTimeTotal);
                entityManager.persist(metric_2);
                Metric metric_3 = new Metric("service_2", "operation_2_1", opTimeTotal);
                entityManager.persist(metric_3);
                Metric metric_4 = new Metric("service_2", "operation_2_2", opTimeTotal);
                entityManager.persist(metric_4);
                Metric metric_5 = new Metric("service_3", "operation_3_1", opErrTotal);
                entityManager.persist(metric_5);
                Metric metric_6 = new Metric("service_3", "operation_3_1", opErrRequest);
                entityManager.persist(metric_6);

                // Create the metric classifiers
                MetricClassifier metricClassifier1 = new MetricClassifier("consumer_1", "sourceDC", "targetDC");
                entityManager.persist(metricClassifier1);
                MetricClassifier metricClassifier2 = new MetricClassifier("consumer_2", "sourceDC", "targetDC");
                entityManager.persist(metricClassifier2);
                MetricClassifier metricClassifier3 = new MetricClassifier("consumer_3", "sourceDC", "targetDC");
                entityManager.persist(metricClassifier3);

                // Create the machine
                Machine machine = Machine.newMachine();
                entityManager.persist(machine);

                long sixtyMinsAgo = now - TimeUnit.SECONDS.toMillis(3600);
                long sixtyOneMinsAgo = sixtyMinsAgo - TimeUnit.SECONDS.toMillis(60);
                long sixtyTwoMinsAgo = sixtyOneMinsAgo - TimeUnit.SECONDS.toMillis(60);

                // Consumer1 calls operation_1_1 62 mins ago
                MetricValue metricValue = new MetricValue(metric_1, metricClassifier1, machine);
                metricValue.setTimeStamp(sixtyTwoMinsAgo);
                metricValue.setServerSide(true);
                metricValue.setAggregationPeriod(60);
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_1, 1));
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_2, 11));
                entityManager.persist(metricValue);
                // Then calls operation_2_2
                metricValue = new MetricValue(metric_4, metricClassifier1, machine);
                metricValue.setTimeStamp(sixtyTwoMinsAgo);
                metricValue.setServerSide(true);
                metricValue.setAggregationPeriod(60);
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_1, 1));
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_2, 41));
                entityManager.persist(metricValue);
                // Consumer2 calls operation_1_1 61 mins ago
                metricValue = new MetricValue(metric_1, metricClassifier2, machine);
                metricValue.setTimeStamp(sixtyOneMinsAgo);
                metricValue.setServerSide(true);
                metricValue.setAggregationPeriod(60);
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_1, 1));
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_2, 11));
                entityManager.persist(metricValue);
                // Then calls operation_2_2
                metricValue = new MetricValue(metric_4, metricClassifier2, machine);
                metricValue.setTimeStamp(sixtyOneMinsAgo);
                metricValue.setServerSide(true);
                metricValue.setAggregationPeriod(60);
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_1, 1));
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_2, 41));
                entityManager.persist(metricValue);
                // Then calls operation_1_2
                metricValue = new MetricValue(metric_2, metricClassifier2, machine);
                metricValue.setTimeStamp(sixtyOneMinsAgo);
                metricValue.setServerSide(true);
                metricValue.setAggregationPeriod(60);
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_1, 1));
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_2, 23));
                entityManager.persist(metricValue);

                // Consumer 1 calls operation 1_2 3 times
                metricValue = new MetricValue(metric_2, metricClassifier1, machine);
                metricValue.setTimeStamp(twoMinutesAgo);
                metricValue.setServerSide(true);
                metricValue.setAggregationPeriod(60);
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_1, 1));
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_2, 23));
                entityManager.persist(metricValue);
                metricValue = new MetricValue(metric_2, metricClassifier1, machine);
                metricValue.setTimeStamp(oneMinuteAgo);
                metricValue.setServerSide(true);
                metricValue.setAggregationPeriod(60);
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_1, 1));
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_2, 23));
                entityManager.persist(metricValue);
                metricValue = new MetricValue(metric_2, metricClassifier1, machine);
                metricValue.setTimeStamp(oneMinuteAgo);
                metricValue.setServerSide(true);
                metricValue.setAggregationPeriod(60);
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_1, 1));
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_2, 23));
                entityManager.persist(metricValue);
                // Consumer 2 calls operation_2_2
                metricValue = new MetricValue(metric_4, metricClassifier2, machine);
                metricValue.setTimeStamp(twoMinutesAgo);
                metricValue.setServerSide(true);
                metricValue.setAggregationPeriod(60);
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_1, 1));
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_2, 41));
                entityManager.persist(metricValue);
                // Then calls operation_2_1
                metricValue = new MetricValue(metric_3, metricClassifier2, machine);
                metricValue.setTimeStamp(oneMinuteAgo);
                metricValue.setServerSide(true);
                metricValue.setAggregationPeriod(60);
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_1, 1));
                metricValue.addMetricComponentValue(new MetricComponentValue(opTimeTotal_2, 31));
                entityManager.persist(metricValue);

                // Error data
                metricValue = new MetricValue(metric_5, metricClassifier3, machine);
                metricValue.setTimeStamp(twoMinutesAgo);
                metricValue.setServerSide(true);
                metricValue.setAggregationPeriod(60);
                metricValue.addMetricComponentValue(new MetricComponentValue(opErrTotal_1, 1));
                entityManager.persist(metricValue);
                metricValue = new MetricValue(metric_6, metricClassifier3, machine);
                metricValue.setTimeStamp(twoMinutesAgo);
                metricValue.setServerSide(true);
                metricValue.setAggregationPeriod(60);
                metricValue.addMetricComponentValue(new MetricComponentValue(opErrRequest_1, 1));
                entityManager.persist(metricValue);
                
                org.ebayopensource.turmeric.runtime.error.model.Error error1 =
                    new org.ebayopensource.turmeric.runtime.error.model.Error(1, "error_1", ErrorCategory.SYSTEM, ErrorSeverity.ERROR, "domain_1", "sub_domain_1", "organization");
            entityManager.persist(error1);
            ErrorValue errorValue1 = new ErrorValue(error1, "message1", "service_3", "operation_3_1", "consumer_3", oneMinuteAgo, true, 0);
            entityManager.persist(errorValue1);

                org.ebayopensource.turmeric.runtime.error.model.Error error2 = new org.ebayopensource.turmeric.runtime.error.model.Error(
                                2, "error_2", ErrorCategory.SYSTEM, ErrorSeverity.ERROR, "domain_1", "sub_domain_1",
                                "organization");
                entityManager.persist(error2);
                ErrorValue errorValue2 = new ErrorValue(error2, "message1", "service_3", "operation_3_1", "consumer_3",
                                oneMinuteAgo, true, 0);
                entityManager.persist(errorValue2);

                ErrorValue errorValue3 = new ErrorValue(error2, "message2", "service_3", "operation_3_1", "consumer_3",
                                twoMinutesAgo, true, 0);
                entityManager.persist(errorValue3);

                ErrorValue errorValue4 = new ErrorValue(error2, "message3", "service_3", "operation_3_1", "consumer_3",
                                twoMinutesAgo, true, 0);
                entityManager.persist(errorValue4);

                org.ebayopensource.turmeric.runtime.error.model.Error error3 = new org.ebayopensource.turmeric.runtime.error.model.Error(
                                3, "error_3", ErrorCategory.APPLICATION, ErrorSeverity.ERROR, "domain_1",
                                "sub_domain_1", "organization");
                entityManager.persist(error3);
                ErrorValue errorValue5 = new ErrorValue(error3, "message4", "service_2", "operation_3_1", "consumer_3",
                                oneMinuteAgo, true, 0);
                entityManager.persist(errorValue5);

                ErrorValue errorValue6 = new ErrorValue(error3, "message5", "service_2", "operation_3_1", "consumer_3",
                                twoMinutesAgo, true, 0);
                entityManager.persist(errorValue6);

                transaction.commit();
            }
            catch (Exception x) {
                transaction.rollback();
                throw x;
            }
        }
        finally {
            entityManager.close();
        }
    }

    /**
     * Destroy entity manager factory.
     */
    @AfterClass
    public static void destroyEntityManagerFactory() {
        entityManagerFactory.close();
    }

    /**
     * Inits the provider.
     */
    @Before
    public void initProvider() {
        provider = new DAOSOAMetricsQueryServiceProvider(entityManagerFactory);
    }

    /**
     * Test get metrics data by operation all services all operations all consumers.
     */
    @Test
    public void testGetMetricsDataByOperationAllServicesAllOperationsAllConsumers() {
        long now = System.currentTimeMillis();
        long oneHourAgo = now - TimeUnit.SECONDS.toMillis(3600);
        long twoHoursAgo = oneHourAgo - TimeUnit.SECONDS.toMillis(3600);

        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setSecondStartTime(oneHourAgo);
        metricCriteria.setFirstStartTime(twoHoursAgo);
        metricCriteria.setDuration(3600);
        metricCriteria.setAggregationPeriod(60);
        metricCriteria.setMetricName("CallCount");

        MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
        ResourceEntityRequest entityRequest = new ResourceEntityRequest();
        entityRequest.setResourceEntityType(ResourceEntity.SERVICE);
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest);
        metricResourceCriteria.setResourceEntityResponseType(ResourceEntity.OPERATION.value());

        List<MetricGroupData> result = provider.getMetricsData(metricCriteria, metricResourceCriteria);
        // All 4 operations of the services have been called
        assertEquals(4, result.size());
        // First result must be the operation that got called 3 times, then the one that got called 2 times
        assertEquals("operation_1_2", result.get(0).getCriteriaInfo().getOperationName());
        assertEquals(3, Double.valueOf(result.get(0).getCount2()).intValue());
        assertEquals("operation_2_2", result.get(1).getCriteriaInfo().getOperationName());
        assertEquals(2, Double.valueOf(result.get(1).getCount1()).intValue());
    }

    /**
     * Test get metrics data by operation one service all operations all consumers.
     */
    @Test
    public void testGetMetricsDataByOperationOneServiceAllOperationsAllConsumers() {
        long now = System.currentTimeMillis();
        long oneHourAgo = now - TimeUnit.SECONDS.toMillis(3600);
        long twoHoursAgo = oneHourAgo - TimeUnit.SECONDS.toMillis(3600);

        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setSecondStartTime(oneHourAgo);
        metricCriteria.setFirstStartTime(twoHoursAgo);
        metricCriteria.setDuration(3600);
        metricCriteria.setAggregationPeriod(60);
        metricCriteria.setMetricName("CallCount");

        MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
        ResourceEntityRequest entityRequest = new ResourceEntityRequest();
        entityRequest.setResourceEntityType(ResourceEntity.SERVICE);
        entityRequest.getResourceEntityName().add("service_1");
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest);
        metricResourceCriteria.setResourceEntityResponseType(ResourceEntity.OPERATION.value());

        List<MetricGroupData> result = provider.getMetricsData(metricCriteria, metricResourceCriteria);
        // Both operations of service_1 have been called
        assertEquals(2, result.size());
        // First result must be the operation that got called 3 times
        assertEquals("operation_1_2", result.get(0).getCriteriaInfo().getOperationName());
        assertEquals(3, Double.valueOf(result.get(0).getCount2()).intValue());
        assertEquals("operation_1_1", result.get(1).getCriteriaInfo().getOperationName());
        assertEquals(2, Double.valueOf(result.get(1).getCount1()).intValue());
    }

    /**
     * Test get metrics data by operation one service one operation all consumers.
     */
    @Test
    public void testGetMetricsDataByOperationOneServiceOneOperationAllConsumers() {
        long now = System.currentTimeMillis();
        long oneHourAgo = now - TimeUnit.SECONDS.toMillis(3600);
        long twoHoursAgo = oneHourAgo - TimeUnit.SECONDS.toMillis(3600);

        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setSecondStartTime(oneHourAgo);
        metricCriteria.setFirstStartTime(twoHoursAgo);
        metricCriteria.setDuration(3600);
        metricCriteria.setAggregationPeriod(60);
        metricCriteria.setMetricName("CallCount");

        MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
        ResourceEntityRequest entityRequest1 = new ResourceEntityRequest();
        entityRequest1.setResourceEntityType(ResourceEntity.SERVICE);
        entityRequest1.getResourceEntityName().add("service_1");
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest1);
        ResourceEntityRequest entityRequest2 = new ResourceEntityRequest();
        entityRequest2.setResourceEntityType(ResourceEntity.OPERATION);
        entityRequest2.getResourceEntityName().add("operation_1_2");
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest2);
        metricResourceCriteria.setResourceEntityResponseType(ResourceEntity.OPERATION.value());

        List<MetricGroupData> result = provider.getMetricsData(metricCriteria, metricResourceCriteria);
        // Only one operation requested
        assertEquals(1, result.size());
        assertEquals("operation_1_2", result.get(0).getCriteriaInfo().getOperationName());
        assertEquals(1, Double.valueOf(result.get(0).getCount1()).intValue());
        assertEquals(3, Double.valueOf(result.get(0).getCount2()).intValue());
    }

    /**
     * Test get metrics data by operation one service one operation one consumer.
     */
    @Test
    public void testGetMetricsDataByOperationOneServiceOneOperationOneConsumer() {
        long now = System.currentTimeMillis();
        long twoHoursAgo = now - TimeUnit.SECONDS.toMillis(2 * 3600);
        long twoMinuteAgo = now - TimeUnit.SECONDS.toMillis(2 * 60);

        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(twoHoursAgo);
        metricCriteria.setSecondStartTime(twoMinuteAgo);
        metricCriteria.setDuration(3600);
        metricCriteria.setAggregationPeriod(60);
        metricCriteria.setMetricName("CallCount");

        MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
        ResourceEntityRequest entityRequest1 = new ResourceEntityRequest();
        entityRequest1.setResourceEntityType(ResourceEntity.SERVICE);
        entityRequest1.getResourceEntityName().add("service_2");
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest1);
        ResourceEntityRequest entityRequest2 = new ResourceEntityRequest();
        entityRequest2.setResourceEntityType(ResourceEntity.OPERATION);
        entityRequest2.getResourceEntityName().add("operation_2_1");
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest2);
        ResourceEntityRequest entityRequest3 = new ResourceEntityRequest();
        entityRequest3.setResourceEntityType(ResourceEntity.CONSUMER);
        entityRequest3.getResourceEntityName().add("consumer_2");
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest3);
        metricResourceCriteria.setResourceEntityResponseType(ResourceEntity.OPERATION.value());

        List<MetricGroupData> result = provider.getMetricsData(metricCriteria, metricResourceCriteria);
        // Only one operation requested
        assertEquals(1, result.size());
        assertEquals("SoaFwk.Op.Time.Total", result.get(0).getCriteriaInfo().getMetricName());
        assertEquals("service_2", result.get(0).getCriteriaInfo().getServiceName());
        assertEquals("operation_2_1", result.get(0).getCriteriaInfo().getOperationName());
        assertEquals("consumer_2", result.get(0).getCriteriaInfo().getConsumerName());
        assertEquals(MonitoringSystem.COLLECTION_LOCATION_SERVER, result.get(0).getCriteriaInfo().getRoleType());
        assertEquals(0, Double.valueOf(result.get(0).getCount1()).intValue());
        assertEquals(1, Double.valueOf(result.get(0).getCount2()).intValue());
    }

    /**
     * Test get metrics data by consumer all services all operations all consumers.
     */
    @Test
    public void testGetMetricsDataByConsumerAllServicesAllOperationsAllConsumers() {
        long now = System.currentTimeMillis();
        long oneHourAgo = now - TimeUnit.SECONDS.toMillis(3600);
        long twoHoursAgo = oneHourAgo - TimeUnit.SECONDS.toMillis(3600);

        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setSecondStartTime(oneHourAgo);
        metricCriteria.setFirstStartTime(twoHoursAgo);
        metricCriteria.setDuration(3600);
        metricCriteria.setAggregationPeriod(60);
        metricCriteria.setMetricName("CallCount");

        MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
        ResourceEntityRequest entityRequest = new ResourceEntityRequest();
        entityRequest.setResourceEntityType(ResourceEntity.SERVICE);
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest);
        metricResourceCriteria.setResourceEntityResponseType(ResourceEntity.CONSUMER.value());

        List<MetricGroupData> result = provider.getMetricsData(metricCriteria, metricResourceCriteria);
        // All consumers have called
        assertEquals(2, result.size());
        // Consumer_1 made 2 calls in first hour and 3 calls in second hour
        assertEquals("consumer_1", result.get(0).getCriteriaInfo().getConsumerName());
        assertEquals(2, Double.valueOf(result.get(0).getCount1()).intValue());
        assertEquals(3, Double.valueOf(result.get(0).getCount2()).intValue());
        // Consumer_2 made 3 calls then 2 calls
        assertEquals("consumer_2", result.get(1).getCriteriaInfo().getConsumerName());
        assertEquals(3, Double.valueOf(result.get(1).getCount1()).intValue());
        assertEquals(2, Double.valueOf(result.get(1).getCount2()).intValue());
    }

    /**
     * Test get metrics data by consumer one service all operations all consumers.
     */
    @Test
    public void testGetMetricsDataByConsumerOneServiceAllOperationsAllConsumers() {
        long now = System.currentTimeMillis();
        long oneHourAgo = now - TimeUnit.SECONDS.toMillis(3600);
        long twoHoursAgo = oneHourAgo - TimeUnit.SECONDS.toMillis(3600);

        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setSecondStartTime(oneHourAgo);
        metricCriteria.setFirstStartTime(twoHoursAgo);
        metricCriteria.setDuration(3600);
        metricCriteria.setAggregationPeriod(60);
        metricCriteria.setMetricName("CallCount");

        MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
        ResourceEntityRequest entityRequest = new ResourceEntityRequest();
        entityRequest.setResourceEntityType(ResourceEntity.SERVICE);
        entityRequest.getResourceEntityName().add("service_2");
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest);
        metricResourceCriteria.setResourceEntityResponseType(ResourceEntity.CONSUMER.value());

        List<MetricGroupData> result = provider.getMetricsData(metricCriteria, metricResourceCriteria);
        // Both consumers have called service_2
        assertEquals(2, result.size());
        // Consumer_2 has called service_2 1 time in first hour and 2 times in second hour
        assertEquals("consumer_2", result.get(0).getCriteriaInfo().getConsumerName());
        assertEquals(1, Double.valueOf(result.get(0).getCount1()).intValue());
        assertEquals(2, Double.valueOf(result.get(0).getCount2()).intValue());
        // Consumer_1 has called service_2 only in the first hour
        assertEquals("consumer_1", result.get(1).getCriteriaInfo().getConsumerName());
        assertEquals(1, Double.valueOf(result.get(1).getCount1()).intValue());
        assertEquals(0, Double.valueOf(result.get(1).getCount2()).intValue());
    }

    /**
     * Test get metrics data by consumer one service all operations one consumer.
     */
    @Test
    public void testGetMetricsDataByConsumerOneServiceAllOperationsOneConsumer() {
        long now = System.currentTimeMillis();
        long oneHourAgo = now - TimeUnit.SECONDS.toMillis(3600);
        long twoHoursAgo = oneHourAgo - TimeUnit.SECONDS.toMillis(3600);

        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setSecondStartTime(oneHourAgo);
        metricCriteria.setFirstStartTime(twoHoursAgo);
        metricCriteria.setDuration(3600);
        metricCriteria.setAggregationPeriod(60);
        metricCriteria.setMetricName("ResponseTime");

        MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
        ResourceEntityRequest entityRequest1 = new ResourceEntityRequest();
        entityRequest1.setResourceEntityType(ResourceEntity.SERVICE);
        entityRequest1.getResourceEntityName().add("service_2");
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest1);
        ResourceEntityRequest entityRequest2 = new ResourceEntityRequest();
        entityRequest2.setResourceEntityType(ResourceEntity.CONSUMER);
        entityRequest2.getResourceEntityName().add("consumer_1");
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest2);
        metricResourceCriteria.setResourceEntityResponseType(ResourceEntity.CONSUMER.value());

        List<MetricGroupData> result = provider.getMetricsData(metricCriteria, metricResourceCriteria);
        // Only one consumer requested
        assertEquals(1, result.size());
        assertEquals("consumer_1", result.get(0).getCriteriaInfo().getConsumerName());
        assertEquals(41, Double.valueOf(result.get(0).getCount1()).intValue());
        assertEquals(0, Double.valueOf(result.get(0).getCount2()).intValue());
    }

    /**
     * Test get metrics data by metric one service one operation one consumer.
     */
    @Test
    public void testGetMetricsDataByMetricOneServiceOneOperationOneConsumer() {
        long now = System.currentTimeMillis();
        long oneHourAgo = now - TimeUnit.SECONDS.toMillis(3600);
        long twoHoursAgo = oneHourAgo - TimeUnit.SECONDS.toMillis(3600);

        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(twoHoursAgo);
        metricCriteria.setSecondStartTime(oneHourAgo);
        metricCriteria.setDuration(3600);
        metricCriteria.setAggregationPeriod(60);
        metricCriteria.setMetricName("ErrorCount");

        MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
        ResourceEntityRequest entityRequest1 = new ResourceEntityRequest();
        entityRequest1.setResourceEntityType(ResourceEntity.SERVICE);
        entityRequest1.getResourceEntityName().add("service_3");
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest1);
        ResourceEntityRequest entityRequest2 = new ResourceEntityRequest();
        entityRequest2.setResourceEntityType(ResourceEntity.OPERATION);
        entityRequest2.getResourceEntityName().add("operation_3_1");
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest2);
        ResourceEntityRequest entityRequest3 = new ResourceEntityRequest();
        entityRequest3.setResourceEntityType(ResourceEntity.CONSUMER);
        entityRequest3.getResourceEntityName().add("consumer_3");
        metricResourceCriteria.getResourceRequestEntities().add(entityRequest3);
        metricResourceCriteria.setResourceEntityResponseType("Error");

        List<MetricGroupData> result = provider.getMetricsData(metricCriteria, metricResourceCriteria);

        Machine machine = Machine.newMachine();
        assertEquals(2, result.size());
        for (MetricGroupData metricGroupData : result) {
            assertEquals("service_3", metricGroupData.getCriteriaInfo().getServiceName());
            assertEquals("operation_3_1", metricGroupData.getCriteriaInfo().getOperationName());
            assertEquals("consumer_3", metricGroupData.getCriteriaInfo().getConsumerName());
            assertEquals(MonitoringSystem.COLLECTION_LOCATION_SERVER, metricGroupData.getCriteriaInfo().getRoleType());
            assertEquals(machine.getCanonicalHostName(), metricGroupData.getCriteriaInfo().getMachineName());
            assertEquals(0, Double.valueOf(metricGroupData.getCount1()).intValue());
            assertEquals(1, Double.valueOf(metricGroupData.getCount2()).intValue());
        }
        String metricName1 = result.get(0).getCriteriaInfo().getMetricName();
        String metricName2 = result.get(1).getCriteriaInfo().getMetricName();
        assertFalse(metricName1.equals(metricName2));
        if (SystemMetricDefs.OP_ERR_TOTAL.getMetricName().equals(metricName1)) {
            assertEquals(SystemMetricDefs.OP_ERR_CAT_REQUEST.getMetricName(), metricName2);
        }
        else if (SystemMetricDefs.OP_ERR_CAT_REQUEST.getMetricName().equals(metricName1)) {
            assertEquals(SystemMetricDefs.OP_ERR_TOTAL.getMetricName(), metricName2);
        }
        else {
            fail();
        }
    }

    /**
     * Test get metric value by operation all services all operations all consumers.
     */
    @Test
    public void testGetMetricValueByOperationAllServicesAllOperationsAllConsumers() {
        long now = System.currentTimeMillis();
        long oneHourAgo = now - TimeUnit.SECONDS.toMillis(3600);
        long twoHoursAgo = oneHourAgo - TimeUnit.SECONDS.toMillis(3600);

        CriteriaInfo criteriaInfo = new CriteriaInfo();
        criteriaInfo.setMetricName("CallCount");
        criteriaInfo.setRoleType(MonitoringSystem.COLLECTION_LOCATION_SERVER);

        long duration = 3600;
        int aggregationPeriod = 60;
        List<MetricGraphData> result = provider.getMetricValue(criteriaInfo, twoHoursAgo, duration, aggregationPeriod,
                        null);
        assertEquals(duration / aggregationPeriod, result.size());
        // One minute there were 2 calls, the other minute 3 calls
        assertEquals(2, Double.valueOf(result.get(57).getCount()).intValue());
        assertEquals(3, Double.valueOf(result.get(58).getCount()).intValue());
    }

    /**
     * Test get metric value by operation one service all operations all consumers.
     */
    @Test
    public void testGetMetricValueByOperationOneServiceAllOperationsAllConsumers() {
        long now = System.currentTimeMillis();
        long oneHourAgo = now - TimeUnit.SECONDS.toMillis(3600);
        long twoHoursAgo = oneHourAgo - TimeUnit.SECONDS.toMillis(3600);

        CriteriaInfo criteriaInfo = new CriteriaInfo();
        criteriaInfo.setMetricName("CallCount");
        criteriaInfo.setRoleType(MonitoringSystem.COLLECTION_LOCATION_SERVER);
        criteriaInfo.setServiceName("service_2");

        long duration = 3600;
        int aggregationPeriod = 60;
        List<MetricGraphData> result = provider.getMetricValue(criteriaInfo, twoHoursAgo, duration, aggregationPeriod,
                        null);
        assertEquals(duration / aggregationPeriod, result.size());
        // One call each minute for service_2
        assertEquals(1, Double.valueOf(result.get(57).getCount()).intValue());
        assertEquals(1, Double.valueOf(result.get(58).getCount()).intValue());
    }

    /**
     * Test get metric value by operation one service one operation all consumers.
     */
    @Test
    public void testGetMetricValueByOperationOneServiceOneOperationAllConsumers() {
        long now = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1);
        long oneHourAgo = now - TimeUnit.SECONDS.toMillis(3600);

        CriteriaInfo criteriaInfo = new CriteriaInfo();
        criteriaInfo.setMetricName("ResponseTime");
        criteriaInfo.setRoleType(MonitoringSystem.COLLECTION_LOCATION_SERVER);
        criteriaInfo.setServiceName("service_2");
        criteriaInfo.setOperationName("operation_2_2");

        long duration = 3600;
        int aggregationPeriod = 60;
        List<MetricGraphData> result = provider.getMetricValue(criteriaInfo, oneHourAgo, duration, aggregationPeriod,
                        null);
        assertEquals(duration / aggregationPeriod, result.size());
        // One call for operation_2_2
        assertEquals(41, Double.valueOf(result.get(57).getCount()).intValue());
    }

    /**
     * Test get metric value by operation all services all operations one consumer.
     */
    @Test
    public void testGetMetricValueByOperationAllServicesAllOperationsOneConsumer() {
        long now = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1);
        long oneHourAgo = now - TimeUnit.SECONDS.toMillis(3600);

        CriteriaInfo criteriaInfo = new CriteriaInfo();
        criteriaInfo.setMetricName("CallCount");
        criteriaInfo.setRoleType(MonitoringSystem.COLLECTION_LOCATION_SERVER);
        criteriaInfo.setConsumerName("consumer_1");

        long duration = 3600;
        int aggregationPeriod = 60;
        List<MetricGraphData> result = provider.getMetricValue(criteriaInfo, oneHourAgo, duration, aggregationPeriod,
                        null);
        assertEquals(duration / aggregationPeriod, result.size());
        // One call each minute for consumer_1
        assertEquals(1, Double.valueOf(result.get(57).getCount()).intValue());
        assertEquals(2, Double.valueOf(result.get(58).getCount()).intValue());
    }

    /**
     * Test get metrics metadata by service.
     */
    @Test
    public void testGetMetricsMetadataByService() {
        List<String> serviceNames = provider.getMetricsMetadata(ResourceEntity.SERVICE.value(),
                        Collections.<String> emptyList(), ResourceEntity.SERVICE.value());
        assertEquals(3, serviceNames.size());
        Collections.sort(serviceNames);
        assertEquals("service_1", serviceNames.get(0));
        assertEquals("service_2", serviceNames.get(1));
        assertEquals("service_3", serviceNames.get(2));

        serviceNames = provider.getMetricsMetadata(ResourceEntity.SERVICE.value(), Arrays.asList("service_2"),
                        ResourceEntity.SERVICE.value());
        assertEquals(1, serviceNames.size());
        assertEquals("service_2", serviceNames.get(0));
    }

    /**
     * Test get metrics metadata by operation.
     */
    @Test
    public void testGetMetricsMetadataByOperation() {
        List<String> operationNames = provider.getMetricsMetadata(ResourceEntity.SERVICE.value(),
                        Arrays.asList("service_1", "service_2"), ResourceEntity.OPERATION.value());
        assertEquals(4, operationNames.size());
        Collections.sort(operationNames);
        assertEquals("service_1.operation_1_1", operationNames.get(0));
        assertEquals("service_1.operation_1_2", operationNames.get(1));
        assertEquals("service_2.operation_2_1", operationNames.get(2));
        assertEquals("service_2.operation_2_2", operationNames.get(3));

        operationNames = provider.getMetricsMetadata(ResourceEntity.SERVICE.value(), Arrays.asList("service_2"),
                        ResourceEntity.OPERATION.value());
        assertEquals(2, operationNames.size());
        Collections.sort(operationNames);
        assertEquals("service_2.operation_2_1", operationNames.get(0));
        assertEquals("service_2.operation_2_2", operationNames.get(1));
    }

    /**
     * Test get error metrics metadata.
     */
    @Test
    public void testGetErrorMetricsMetadata() {
        String errorId = String.valueOf(1L);
        ErrorInfos error = provider.getErrorMetricsMetadata(errorId, null, null);
        assertEquals(errorId, error.getId());
        assertEquals("error_1", error.getName());
        assertEquals(ErrorCategory.SYSTEM.value(), error.getCategory());
        assertEquals(ErrorSeverity.ERROR.value(), error.getSeverity());
        assertEquals("domain_1", error.getDomain());
        assertEquals("sub_domain_1", error.getSubDomain());
    }

    /**
     * Test get error metrics data by category.
     */
    @Test
    public void testGetErrorMetricsDataByCategory() {
        long oneMinuteAgo = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(60);
        long twoMinutesAgo = oneMinuteAgo - TimeUnit.SECONDS.toMillis(60);
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(twoMinutesAgo);
        metricCriteria.setSecondStartTime(oneMinuteAgo);
        metricCriteria.setDuration(3600);

        List<ErrorViewData> errorData = provider.getErrorMetricsData("Category", Collections.<String>emptyList(), Collections.<String>emptyList(), Collections.<String>emptyList(), null, null, null, null, metricCriteria);
        assertEquals(3, errorData.size());
        ErrorViewData errorDatum = errorData.get(0);
        String errorId = "3";
        assertEquals(errorId, errorDatum.getError().getErrorId());
        String errorName = "error_3";
        assertEquals(errorName, errorDatum.getError().getErrorName());
        assertEquals(1, errorDatum.getErrorCount1());
        assertEquals(0, errorDatum.getErrorCount2());
        assertEquals(-100, errorDatum.getErrorDiff(), 0);

        String errorId2 = "2";
        String errorName2 = "error_2";
        errorData = provider.getErrorMetricsData("Category", Arrays.asList("service_3"), Arrays.asList("operation_3_1"), Arrays.asList("consumer_3"), errorId2, ErrorCategory.SYSTEM.value(), ErrorSeverity.ERROR.value(), null, metricCriteria);
        assertEquals(1, errorData.size());
        errorDatum = errorData.get(0);
        assertEquals(errorId2, errorDatum.getError().getErrorId());
        assertEquals(errorName2, errorDatum.getError().getErrorName());
        assertEquals(1, errorDatum.getErrorCount1());
        assertEquals(0, errorDatum.getErrorCount2());
        assertEquals(-100, errorDatum.getErrorDiff(), 0);
    }

    /**
     * Test get error metrics data by severity.
     */
    @Test
    public void testGetErrorMetricsDataBySeverity() {
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(twoMinutesAgo);
        metricCriteria.setSecondStartTime(oneMinuteAgo);
        metricCriteria.setDuration(3600);

        List<ErrorViewData> errorData = provider.getErrorMetricsData("Severity", Collections.<String> emptyList(),
                        Collections.<String> emptyList(), Collections.<String> emptyList(), null, null, null, null,
                        metricCriteria);
        assertEquals(3, errorData.size());
        ErrorViewData errorDatum = errorData.get(0);
        String errorId = "2";
        assertEquals(errorId, errorDatum.getError().getErrorId());
        String errorName = "error_2";
        assertEquals(errorName, errorDatum.getError().getErrorName());
        assertEquals(3, errorDatum.getErrorCount1());
        assertEquals(1, errorDatum.getErrorCount2());
        assertEquals(200, errorDatum.getErrorDiff(), 1);

        String errorId2 = "2";
        String errorName2 = "error_2";
        errorData = provider.getErrorMetricsData("Severity", Arrays.asList("service_3"),
                        Arrays.asList("operation_3_1"), Arrays.asList("consumer_3"), errorId2,
                        ErrorCategory.SYSTEM.value(), ErrorSeverity.ERROR.value(), null, metricCriteria);
        assertEquals(1, errorData.size());
        errorDatum = errorData.get(0);
        assertEquals(errorId2, errorDatum.getError().getErrorId());
        assertEquals(errorName2, errorDatum.getError().getErrorName());
        assertEquals(3, errorDatum.getErrorCount1());
        assertEquals(1, errorDatum.getErrorCount2());
        assertEquals(200, errorDatum.getErrorDiff(), 1);
    }

    
    /**
     * Test get system error graph for service one minute ago.
     */
    @Test
    public void testGetSystemErrorGraphForServiceOneMinuteAgo() {
        long duration = 120;// in secs
        int aggregationPeriod = 60;// in secs
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(oneMinuteAgo);
        metricCriteria.setDuration(duration);
        metricCriteria.setAggregationPeriod(aggregationPeriod);
        metricCriteria.setRoleType("server");

        List<MetricGraphData> result = provider.getErrorGraph("service_3", null, null, null,
                        ErrorCategory.SYSTEM.value(), null, metricCriteria);

        assertNotNull(result);
        assertEquals(duration / aggregationPeriod, result.size());
        // there must be duration/aggregationPeriod elements. The first
        assertEquals(duration / aggregationPeriod, result.size());
        assertEquals(2.0d, result.get(0).getCount(), 0.0d);// first element must be 1
        assertEquals(0.0d, result.get(1).getCount(), 0.0d);// second element must be 0

    }

    
    /**
     * Test get system error graph for service two minutes ago.
     */
    @Test
    public void testGetSystemErrorGraphForServiceTwoMinutesAgo() {
        long duration = 180;// in secs
        int aggregationPeriod = 60;// in secs
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(twoMinutesAgo);
        metricCriteria.setDuration(duration);
        metricCriteria.setAggregationPeriod(aggregationPeriod);
        metricCriteria.setRoleType("server");

        List<MetricGraphData> result = provider.getErrorGraph("service_3", null, null, null,
                        ErrorCategory.SYSTEM.value(), null, metricCriteria);

        assertNotNull(result);
        assertEquals(duration / aggregationPeriod, result.size());
        // there must be duration/aggregationPeriod elements. The first
        assertEquals(duration / aggregationPeriod, result.size());
        assertEquals(2.0d, result.get(0).getCount(), 0.0d);// first element must be 2
        assertEquals(2.0d, result.get(1).getCount(), 0.0d);// second element must be 1
        assertEquals(0.0d, result.get(2).getCount(), 0.0d);// third element must be 0

    }

    
    /**
     * Test get application error graph for service one minute ago.
     */
    @Test
    public void testGetApplicationErrorGraphForServiceOneMinuteAgo() {
        long duration = 120;// in secs
        int aggregationPeriod = 60;// in secs
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(oneMinuteAgo);
        metricCriteria.setDuration(duration);
        metricCriteria.setAggregationPeriod(aggregationPeriod);
        metricCriteria.setRoleType("server");

        List<MetricGraphData> result = provider.getErrorGraph("service_2", null, null, null,
                        ErrorCategory.APPLICATION.value(), null, metricCriteria);

        assertNotNull(result);
        assertEquals(duration / aggregationPeriod, result.size());
        // there must be duration/aggregationPeriod elements. The first
        assertEquals(duration / aggregationPeriod, result.size());
        assertEquals(1.0d, result.get(0).getCount(), 0.0d);// first element must be 1
        assertEquals(0.0d, result.get(1).getCount(), 0.0d);// second element must be 0

    }

    
    /**
     * Test get application error graph for service two minutes ago.
     */
    @Test
    public void testGetApplicationErrorGraphForServiceTwoMinutesAgo() {
        long duration = 180;// in secs
        int aggregationPeriod = 60;// in secs
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(twoMinutesAgo);
        metricCriteria.setDuration(duration);
        metricCriteria.setAggregationPeriod(aggregationPeriod);
        metricCriteria.setRoleType("server");

        List<MetricGraphData> result = provider.getErrorGraph("service_2", null, null, null,
                        ErrorCategory.APPLICATION.value(), null, metricCriteria);

        assertNotNull(result);
        assertEquals(duration / aggregationPeriod, result.size());
        // there must be duration/aggregationPeriod elements. The first
        assertEquals(duration / aggregationPeriod, result.size());
        assertEquals(1.0d, result.get(0).getCount(), 0.0d);// first element must be 1
        assertEquals(1.0d, result.get(1).getCount(), 0.0d);// second element must be 1
        assertEquals(0.0d, result.get(2).getCount(), 0.0d);// third element must be 0

    }

    
    /**
     * Test get error graph for non existant service.
     */
    @Test
    public void testGetErrorGraphForNonExistantService() {
        long duration = 3600;// in secs
        int aggregationPeriod = 60;// in secs
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(twoMinutesAgo);
        metricCriteria.setDuration(duration);
        metricCriteria.setAggregationPeriod(aggregationPeriod);
        metricCriteria.setRoleType("server");

        List<MetricGraphData> result = provider.getErrorGraph("nonexistant_service", null, null, null,
                        ErrorCategory.SYSTEM.value(), null, metricCriteria);

        assertNotNull(result);
        assertEquals(duration / aggregationPeriod, result.size());
        // there must be duration/aggregationPeriod elements. The first
        assertEquals(duration / aggregationPeriod, result.size());
        // must not be any value > 0.0
        for (int i = 0; i < duration / aggregationPeriod; i++) {
            assertEquals(0.0d, result.get(0).getCount(), 0.0d);
        }

    }

    
    /**
     * Test get error graph for empty category.
     */
    @Test
    public void testGetErrorGraphForEmptyCategory() {
        long duration = 3600;// in secs
        int aggregationPeriod = 60;// in secs
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(twoMinutesAgo);
        metricCriteria.setDuration(duration);
        metricCriteria.setAggregationPeriod(aggregationPeriod);
        metricCriteria.setRoleType("server");

        List<MetricGraphData> result = provider.getErrorGraph("service_3", null, null, null,
                        ErrorCategory.REQUEST.value(), null, metricCriteria);

        assertNotNull(result);
        assertEquals(duration / aggregationPeriod, result.size());
        // there must be duration/aggregationPeriod elements. The first
        assertEquals(duration / aggregationPeriod, result.size());
        // must not be any value > 0.0
        for (int i = 0; i < duration / aggregationPeriod; i++) {
            assertEquals(0.0d, result.get(0).getCount(), 0.0d);
        }

    }
    
    
    /**
     * Test get error graph for non existant category.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetErrorGraphForNonExistantCategory() {
        long duration = 3600;// in secs
        int aggregationPeriod = 60;// in secs
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(twoMinutesAgo);
        metricCriteria.setDuration(duration);
        metricCriteria.setAggregationPeriod(aggregationPeriod);
        metricCriteria.setRoleType("server");

        List<MetricGraphData> result = provider.getErrorGraph("service_3", null, null, null,
                        "NonExistantCategory", null, metricCriteria);

    }
    
    
    /**
     * Test get error graph for non existant severity.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetErrorGraphForNonExistantSeverity() {
        long duration = 3600;// in secs
        int aggregationPeriod = 60;// in secs
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(twoMinutesAgo);
        metricCriteria.setDuration(duration);
        metricCriteria.setAggregationPeriod(aggregationPeriod);
        metricCriteria.setRoleType("server");

        List<MetricGraphData> result = provider.getErrorGraph("service_3", null, null, null,
                        null, "NonExistantSeverity", metricCriteria);

    }

}
