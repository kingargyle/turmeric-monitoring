/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Query;

import org.ebayopensource.turmeric.monitoring.model.Machine;
import org.ebayopensource.turmeric.monitoring.model.Metric;
import org.ebayopensource.turmeric.monitoring.model.MetricCategory;
import org.ebayopensource.turmeric.monitoring.model.MetricClassifier;
import org.ebayopensource.turmeric.monitoring.model.MetricComponentDef;
import org.ebayopensource.turmeric.monitoring.model.MetricComponentValue;
import org.ebayopensource.turmeric.monitoring.model.MetricDef;
import org.ebayopensource.turmeric.monitoring.model.MetricValue;
import org.ebayopensource.turmeric.monitoring.model.MonitoringLevel;
import org.ebayopensource.turmeric.utils.jpa.EntityManagerContext;
import org.ebayopensource.turmeric.utils.jpa.JPAAroundAdvice;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MetricsDAOTest extends AbstractJPATest {
    private MetricsDAO metricsDAO;

    @Before
    public void initDAO() {
        ClassLoader classLoader = MetricsDAO.class.getClassLoader();
        Class[] interfaces = {MetricsDAO.class};
        MetricsDAO target = new MetricsDAOImpl();
        metricsDAO = (MetricsDAO) Proxy.newProxyInstance(classLoader, interfaces, new JPAAroundAdvice(factory, target));
    }

    @Test
    public void testPersistMetricDef() {
        MetricDef metricDef = new MetricDef("metric1", MetricCategory.TIMING, MonitoringLevel.NORMAL);
        MetricComponentDef component1 = new MetricComponentDef("count", "long");
        metricDef.addMetricComponentDef(component1);
        MetricComponentDef component2 = new MetricComponentDef("total", "long");
        metricDef.addMetricComponentDef(component2);
        metricsDAO.persistMetricDef(metricDef);

        assertNotNull(metricDef.getId());
        for (MetricComponentDef metricComponentDef : metricDef.getMetricComponentDefs())
            assertNotNull(metricComponentDef.getId());

        EntityManagerContext.open(factory);
        try {
            MetricDef savedMetricDef = EntityManagerContext.get().find(MetricDef.class, metricDef.getId());
            assertNotNull(savedMetricDef);
            assertEquals(2, savedMetricDef.getMetricComponentDefs().size());
        } finally {
            EntityManagerContext.close();
        }
    }

    @Test
    public void testFindMetricDef() {
        MetricDef metricDef = new MetricDef("metric1", MetricCategory.TIMING, MonitoringLevel.NORMAL);
        MetricComponentDef component1 = new MetricComponentDef("count", "long");
        metricDef.addMetricComponentDef(component1);
        MetricComponentDef component2 = new MetricComponentDef("total", "long");
        metricDef.addMetricComponentDef(component2);
        metricsDAO.persistMetricDef(metricDef);

        MetricDef savedMetricDef = metricsDAO.findMetricDef(metricDef.getName());
        assertNotNull(savedMetricDef);
        assertEquals(2, savedMetricDef.getMetricComponentDefs().size());
    }

    @Test
    public void testPersistMetric() {
        MetricDef metricDef = new MetricDef("metric1", MetricCategory.TIMING, MonitoringLevel.NORMAL);
        metricDef.addMetricComponentDef(new MetricComponentDef("count", "long"));
        metricsDAO.persistMetricDef(metricDef);

        Metric metric = new Metric("service1", "operation1", metricDef);
        metricsDAO.persistMetric(metric);

        assertNotNull(metric.getId());

        EntityManagerContext.open(factory);
        try {
            Metric savedMetric = EntityManagerContext.get().find(Metric.class, metric.getId());
            assertNotNull(savedMetric);
        } finally {
            EntityManagerContext.close();
        }
    }

    @Test
    public void testFindMetric() {
        MetricDef metricDef = new MetricDef("metric1", MetricCategory.TIMING, MonitoringLevel.NORMAL);
        metricDef.addMetricComponentDef(new MetricComponentDef("count", "long"));
        metricsDAO.persistMetricDef(metricDef);

        Metric metric = new Metric("service1", "operation1", metricDef);
        metricsDAO.persistMetric(metric);

        Metric savedMetric = metricsDAO.findMetric(metricDef.getName(), metric.getServiceAdminName(), metric.getOperationName());
        assertNotNull(savedMetric);
    }

    @Test
    public void testPersistMetricValues() throws Exception {
        Machine machine = Machine.newMachine();
        metricsDAO.persistMachine(machine);

        MetricDef metricDef = new MetricDef("metric1", MetricCategory.TIMING, MonitoringLevel.NORMAL);
        MetricComponentDef component1 = new MetricComponentDef("count", "long");
        metricDef.addMetricComponentDef(component1);
        MetricComponentDef component2 = new MetricComponentDef("total", "long");
        metricDef.addMetricComponentDef(component2);
        metricsDAO.persistMetricDef(metricDef);

        Metric metric = new Metric("service1", "operation1", metricDef);
        metricsDAO.persistMetric(metric);

        MetricClassifier metricClassifier1 = new MetricClassifier("useCase1", "sourceDataCenter1", "targetDataCenter1");
        metricsDAO.persistMetricClassifier(metricClassifier1);

        MetricValue metricValue1 = new MetricValue(metric, metricClassifier1, machine);
        MetricComponentValue value1_1 = new MetricComponentValue(component1, 12345);
        metricValue1.addMetricComponentValue(value1_1);
        MetricComponentValue value1_2 = new MetricComponentValue(component2, 54321);
        metricValue1.addMetricComponentValue(value1_2);

        MetricClassifier metricClassifier2 = new MetricClassifier("useCase2", "sourceDataCenter2", "targetDataCenter2");
        metricsDAO.persistMetricClassifier(metricClassifier2);

        MetricValue metricValue2 = new MetricValue(metric, metricClassifier2, machine);
        MetricComponentValue value2_1 = new MetricComponentValue(component1, 67890);
        metricValue2.addMetricComponentValue(value2_1);
        MetricComponentValue value2_2 = new MetricComponentValue(component2, 98760);
        metricValue2.addMetricComponentValue(value2_2);

        metricsDAO.persistMetricValues(Arrays.asList(metricValue1, metricValue2));

        EntityManagerContext.open(factory);
        try {
            StringBuilder hql = new StringBuilder();
            hql.append("from ").append(MetricValue.class.getName()).append(" as mv");
            hql.append(" where mv.metric.serviceAdminName = :serviceAdminName");
            Query query = EntityManagerContext.get().createQuery(hql.toString());
            query.setParameter("serviceAdminName", metric.getServiceAdminName());
            @SuppressWarnings("unchecked")
            List<MetricValue> resultList = query.getResultList();
            assertNotNull(resultList);
            assertEquals(2, resultList.size());
        } finally {
            EntityManagerContext.close();
        }
    }
}
