/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.storage;

import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManagerFactory;

import org.ebayopensource.turmeric.monitoring.MetricsDAO;
import org.ebayopensource.turmeric.monitoring.model.Machine;
import org.ebayopensource.turmeric.monitoring.model.Metric;
import org.ebayopensource.turmeric.monitoring.model.MetricCategory;
import org.ebayopensource.turmeric.monitoring.model.MetricClassifier;
import org.ebayopensource.turmeric.monitoring.model.MetricComponentDef;
import org.ebayopensource.turmeric.monitoring.model.MetricComponentValue;
import org.ebayopensource.turmeric.monitoring.model.MetricDef;
import org.ebayopensource.turmeric.monitoring.model.MetricValue;
import org.ebayopensource.turmeric.monitoring.model.MonitoringLevel;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceRuntimeException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.impl.utils.ReflectionUtils;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricId;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricsStorageProvider;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;
import org.ebayopensource.turmeric.utils.jpa.JPAAroundAdvice;
import org.ebayopensource.turmeric.utils.jpa.PersistenceContext;

/**
 * The Class DAOMetricsStorageProvider.
 */
public class DAOMetricsStorageProvider implements MetricsStorageProvider
{
    private volatile MetricsStorageProvider delegate;
    private EntityManagerFactory entityManagerFactory;
    private MetricsDAO metricsDAO;

    /**
     * Instantiates a new dAO metrics storage provider.
     */
    public DAOMetricsStorageProvider() {
    }

    /**
     * Instantiates a new dAO metrics storage provider.
     *
     * @param entityManagerFactory the entity manager factory
     * @param metricsDAO the metrics dao
     */
    public DAOMetricsStorageProvider(EntityManagerFactory entityManagerFactory, MetricsDAO metricsDAO) {
        this.entityManagerFactory = entityManagerFactory;
        this.metricsDAO = metricsDAO;
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.runtime.common.monitoring.MetricsStorageProvider#init(java.util.Map, java.lang.String, java.lang.String, java.lang.Integer)
     */
    @Override
    public void init(Map<String, String> options, String name, String collectionLocation, Integer snapshotInterval) {
        try {
            int midPeriod = 3600; // 1 hour
            int longPeriod = 86400; // 24 hours
            boolean serverSide = MonitoringSystem.COLLECTION_LOCATION_SERVER.equals(collectionLocation);
            boolean storeServiceMetrics = Boolean.parseBoolean(options.get("storeServiceMetrics"));
            EntityManagerFactory entityManagerFactory = this.entityManagerFactory;
            if (entityManagerFactory == null) {
                String persistenceUnitName = options.get("persistenceUnitName");
                entityManagerFactory = PersistenceContext.createEntityManagerFactory(persistenceUnitName);
            }
            MetricsDAO metricsDAO = this.metricsDAO;
            if (metricsDAO == null) {
                String metricsDAOClassName = options.get("metricsDAOClassName");
                metricsDAO = ReflectionUtils.createInstance(metricsDAOClassName, MetricsDAO.class, Thread.currentThread().getContextClassLoader());
            }
            ClassLoader classLoader = MetricsStorageProvider.class.getClassLoader();
            Class[] interfaces = {MetricsStorageProvider.class};
            Target target = new Target(snapshotInterval, midPeriod, longPeriod, serverSide, storeServiceMetrics, metricsDAO);
            JPAAroundAdvice handler = new JPAAroundAdvice(entityManagerFactory, target);
            delegate = (MetricsStorageProvider) Proxy.newProxyInstance(classLoader, interfaces, handler);
            delegate.init(options, name, collectionLocation, snapshotInterval);
        } catch (ServiceException x) {
            throw ServiceRuntimeException.wrap(x);
        }
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.runtime.common.monitoring.MetricsStorageProvider#saveMetricSnapshot(long, java.util.Collection)
     */
    @Override
    public void saveMetricSnapshot(long timeSnapshot, Collection<MetricValueAggregator> snapshotCollection) throws ServiceException {
        delegate.saveMetricSnapshot(timeSnapshot, snapshotCollection);
    }

    private static class Target implements MetricsStorageProvider {
        private final int shortPeriod;
        private final int midPeriod;
        private final int longPeriod;
        private final boolean serverSide;
        private final boolean storeServiceMetrics;
        private final MetricsDAO metricsDAO;
        private Collection<MetricValueAggregator> previousSnapshot;
        private long lastMidAggregation;
        private long lastLongAggregation;

        private Target(int shortPeriod, int midPeriod, int longPeriod, boolean serverSide, boolean storeServiceMetrics, MetricsDAO metricsDAO) {
            this.shortPeriod = shortPeriod;
            this.midPeriod = midPeriod;
            this.longPeriod = longPeriod;
            this.serverSide = serverSide;
            this.storeServiceMetrics = storeServiceMetrics;
            this.metricsDAO = metricsDAO;
        }

        @Override
        public void init(Map<String, String> options, String name, String collectionLocation, Integer snapshotInterval) {
        }

        @Override
        public void saveMetricSnapshot(long timeSnapshot, Collection<MetricValueAggregator> snapshot) throws ServiceException {
            if (snapshot == null || snapshot.isEmpty()) {
                return;
            }

            if (lastMidAggregation == 0L || timeSnapshot - lastMidAggregation >= TimeUnit.SECONDS.toMillis(midPeriod)) {
                aggregateByMidPeriod(timeSnapshot);
                lastMidAggregation = timeSnapshot;
            }

            if (lastLongAggregation == 0L || timeSnapshot - lastLongAggregation >= TimeUnit.SECONDS.toMillis(longPeriod)) {
                aggregateByLongPeriod(timeSnapshot);
                lastLongAggregation = timeSnapshot;
            }

            try {
                Machine jpaMachine = findMachine(getInetAddress().getHostAddress());
                if (jpaMachine == null)
                    jpaMachine = createMachine();

                List<MetricValue> jpaMetricValues = new ArrayList<MetricValue>();
                for (MetricValueAggregator aggregator : snapshot) {
                    MetricId metricId = aggregator.getMetricId();
                    if (metricId.getOperationName() == null) {
                        // Service-level metric, should we skip it ?
                        if (!storeServiceMetrics)
                            continue;
                    }

                    aggregator = resolve(aggregator);

                    Metric jpaMetric = null;
                    for (org.ebayopensource.turmeric.runtime.common.monitoring.MetricClassifier classifier : aggregator.getClassifiers()) {
                        org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue metricValue = aggregator.getValue(classifier);
                        org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricComponentValue[] metricComponentValues = metricValue.getValues();
                        if (valuesAreNonZero(metricComponentValues)) {
                            if (jpaMetric == null) {
                                jpaMetric = findMetric(metricId);
                                if (jpaMetric == null)
                                    jpaMetric = createMetric(metricId, aggregator);
                            }

                            MetricClassifier jpaMetricClassifier = findMetricClassifier(classifier);
                            if (jpaMetricClassifier == null)
                                jpaMetricClassifier = createMetricClassifier(classifier);

                            MetricValue jpaMetricValue = new MetricValue(jpaMetric, jpaMetricClassifier, jpaMachine);
                            jpaMetricValue.setTimeStamp(timeSnapshot);
                            jpaMetricValue.setServerSide(serverSide);
                            jpaMetricValue.setAggregationPeriod(shortPeriod);

                            for (org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricComponentValue metricComponentValue : metricComponentValues) {
                                MetricComponentDef metricComponentDef = jpaMetric.getMetricDef().findMetricComponentDef(metricComponentValue.getName());
                                if (metricComponentDef != null) {
                                    Number value = (Number) metricComponentValue.getValue();
                                    MetricComponentValue jpaMetricComponentValue = new MetricComponentValue(metricComponentDef, value.doubleValue());
                                    jpaMetricValue.addMetricComponentValue(jpaMetricComponentValue);
                                }
                            }
                            jpaMetricValues.add(jpaMetricValue);
                        }
                    }
                }
                metricsDAO.persistMetricValues(jpaMetricValues);
            } finally {
                previousSnapshot = snapshot;
            }
        }

        private InetAddress getInetAddress() throws ServiceException {
            try {
                return InetAddress.getLocalHost();
            } catch (UnknownHostException x) {
                throw new ServiceException("", x);
            }
        }

        protected boolean valuesAreNonZero(org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricComponentValue[] metricComponentValues) {
            for (org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricComponentValue metricComponentValue : metricComponentValues) {
                Number value = (Number) metricComponentValue.getValue();
                if (value.longValue() != 0L)
                    return true;
            }
            return false;
        }

        protected MetricValueAggregator resolve(MetricValueAggregator aggregator) {
            if (previousSnapshot != null) {
                MetricId metricId = aggregator.getMetricId();
                for (MetricValueAggregator previousAggregator : previousSnapshot) {
                    if (metricId.equals(previousAggregator.getMetricId())) {
                        return (MetricValueAggregator) aggregator.diff(previousAggregator, true);
                    }
                }
            }
            return aggregator;
        }

        protected Metric findMetric(MetricId metricId) {
            return metricsDAO.findMetric(metricId.getMetricName(), metricId.getAdminName(), metricId.getOperationName());
        }

        protected Metric createMetric(MetricId metricId, MetricValueAggregator aggregator) {
            MetricDef metricDef = metricsDAO.findMetricDef(metricId.getMetricName());
            if (metricDef == null) {
                MetricCategory category = MetricCategory.from(aggregator.getCategory().value());
                MonitoringLevel level = MonitoringLevel.from(aggregator.getLevel().getValue());
                metricDef = new MetricDef(metricId.getMetricName(), category, level);
                for (org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricComponentType type : aggregator.getAllComponentsTypes()) {
                    MetricComponentDef metricComponentDef = new MetricComponentDef(type.getName(), type.getType().getName());
                    metricDef.addMetricComponentDef(metricComponentDef);
                }
                metricsDAO.persistMetricDef(metricDef);
            }
            Metric metric = new Metric(metricId.getAdminName(), metricId.getOperationName(), metricDef);
            metricsDAO.persistMetric(metric);
            return metric;
        }

        protected MetricClassifier findMetricClassifier(org.ebayopensource.turmeric.runtime.common.monitoring.MetricClassifier classifier) {
            return metricsDAO.findMetricClassifier(classifier.getUseCase(), classifier.getSourceDC(), classifier.getTargetDC());
        }

        protected MetricClassifier createMetricClassifier(org.ebayopensource.turmeric.runtime.common.monitoring.MetricClassifier classifier) {
            MetricClassifier metricClassifier = new MetricClassifier(classifier.getUseCase(), classifier.getSourceDC(), classifier.getTargetDC());
            metricsDAO.persistMetricClassifier(metricClassifier);
            return metricClassifier;
        }

        private Machine findMachine(String hostAddress) {
            return metricsDAO.findMachine(hostAddress);
        }

        protected void aggregateByMidPeriod(long timeStamp) {
            aggregateByPeriod(midPeriod, timeStamp, shortPeriod);
        }

        protected Machine createMachine() {
            Machine machine = Machine.newMachine();
            metricsDAO.persistMachine(machine);
            return machine;
        }

        protected void aggregateByLongPeriod(long timeStamp) {
            aggregateByPeriod(longPeriod, timeStamp, midPeriod);
        }

        private void aggregateByPeriod(int period, long timeStamp, int shorterPeriod)
        {
            // Find all the metric values to aggregate
            long startTime = timeStamp - TimeUnit.SECONDS.toMillis(period);
            List<MetricValue> metricValues = metricsDAO.findMetricValues(startTime, timeStamp, shorterPeriod);

            List<MetricValue> result = new ArrayList<MetricValue>();
            while (!metricValues.isEmpty()) {
                // Find all metric values that can be aggregated:
                // must have same metric, serverSide and classifier
                List<MetricValue> similar = new ArrayList<MetricValue>();
                MetricValue prototype = metricValues.remove(0);
                similar.add(prototype);
                for (Iterator<MetricValue> iterator = metricValues.iterator(); iterator.hasNext();) {
                    MetricValue candidate = iterator.next();
                    if (prototype.getMetric().getId().equals(candidate.getMetric().getId()) &&
                            prototype.isServerSide() == candidate.isServerSide() &&
                            prototype.getMetricClassifier().getId().equals(candidate.getMetricClassifier().getId())) {
                        similar.add(candidate);
                        iterator.remove();
                    }
                }

                // Create the aggregated metric value
                MetricValue aggregatedMetricValue = new MetricValue(prototype.getMetric(), prototype.getMetricClassifier(), prototype.getMachine());
                aggregatedMetricValue.setTimeStamp(timeStamp);
                aggregatedMetricValue.setServerSide(prototype.isServerSide());
                aggregatedMetricValue.setAggregationPeriod(period);
                for (MetricValue value : similar)
                    aggregatedMetricValue.aggregate(value);
                result.add(aggregatedMetricValue);
            }

            metricsDAO.persistMetricValues(result);
        }
    }
}
