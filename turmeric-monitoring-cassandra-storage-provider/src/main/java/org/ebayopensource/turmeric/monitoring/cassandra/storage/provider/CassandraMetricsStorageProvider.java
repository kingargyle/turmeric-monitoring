/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.cassandra.storage.provider;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.dao.MetricIdentifierDAO;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.dao.MetricsDAO;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.MetricIdentifier;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricClassifier;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricsStorageProvider;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;

/**
 * The Class CassandraMetricsStorageProvider.
 */
public class CassandraMetricsStorageProvider implements MetricsStorageProvider {

    /** The mid period. */
    private int midPeriod;

    /** The long period. */
    private int longPeriod;

    /** The snapshot interval. */
    private int snapshotInterval;

    /** The server side. */
    private boolean serverSide;

    /** The store service metrics. */
    private boolean storeServiceMetrics;

    /** The metric id dao. */
    private MetricIdentifierDAO metricIdDAO;

    /** The metrics dao. */
    private MetricsDAO metricsDAO;

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.runtime.common.monitoring.MetricsStorageProvider#init(java.util.Map,
     * java.lang.String, java.lang.String, java.lang.Integer)
     */
    @Override
    public void init(Map<String, String> options, String name, String collectionLocation, Integer snapshotInterval) {
        this.midPeriod = 3600;
        this.longPeriod = 86400;
        this.serverSide = MonitoringSystem.COLLECTION_LOCATION_SERVER.equals(collectionLocation);
        this.storeServiceMetrics = Boolean.parseBoolean(options.get("storeServiceMetrics"));
        this.snapshotInterval = snapshotInterval;
        String host = options.get("hostName");
        String s_keyspace = options.get("keyspaceName");
        String columnFamilyName = "MetricIdentifier";
        String clusterName = options.get("clusterName");
        metricIdDAO = new MetricIdentifierDAO(clusterName, host, s_keyspace, String.class, MetricIdentifier.class,
                        columnFamilyName);
        metricsDAO = new MetricsDAO(clusterName, host, s_keyspace);
    }

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.runtime.common.monitoring.MetricsStorageProvider#saveMetricSnapshot(long,
     * java.util.Collection)
     */
    @Override
    public void saveMetricSnapshot(long timeSnapshot, Collection<MetricValueAggregator> snapshotCollection)
                    throws ServiceException {
        List<MetricValue> metricValuesToSave = new ArrayList<MetricValue>();
        for (MetricValueAggregator metricValueAggregator : snapshotCollection) {
            org.ebayopensource.turmeric.runtime.common.monitoring.MetricId metricId = metricValueAggregator
                            .getMetricId();
            if (metricId.getOperationName() == null) {
                // Service-level metric, should we skip it ?
                if (!storeServiceMetrics)
                    continue;
            }
            MetricIdentifier cmetricIdentifier = null;
            Collection<MetricClassifier> classifiers = metricValueAggregator.getClassifiers();

            for (MetricClassifier metricClassifier : classifiers) {
                org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue metricValue = metricValueAggregator
                                .getValue(metricClassifier);
                org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricComponentValue[] metricComponentValues = metricValue
                                .getValues();
                if (valuesAreNonZero(metricComponentValues)) {
                    if (cmetricIdentifier == null) {
                        cmetricIdentifier = findMetricId(getKeyfromMetricId(metricId));
                        if (cmetricIdentifier == null) {
                            createMetricId(metricId, metricValueAggregator);
                            cmetricIdentifier = findMetricId(getKeyfromMetricId(metricId));
                        }
                    }
                    // now, store the service stats for the getMetricsMetadata calls
                    metricsDAO.saveServiceOperationByIpCF(getIPAddress(), cmetricIdentifier);
                    // now, store the service stats for the getMetricsMetadata calls for consumers
                    metricsDAO.saveServiceConsumerByIpCF(getIPAddress(), cmetricIdentifier,
                                    metricClassifier.getUseCase());
                    metricValuesToSave.add(metricValue);
                }

            }
            metricsDAO.saveMetricValues(getIPAddress(), cmetricIdentifier, timeSnapshot, snapshotInterval,
                            metricValuesToSave);
            metricValuesToSave.clear();
            cmetricIdentifier = null;
        }

    }

    /**
     * Creates the metric id.
     * 
     * @param metricId
     *            the metric id
     * @param metricValueAggregator
     *            the metric value aggregator
     */
    private void createMetricId(org.ebayopensource.turmeric.runtime.common.monitoring.MetricId metricId,
                    MetricValueAggregator metricValueAggregator) {
        MetricIdentifier model = new MetricIdentifier(metricId.getMetricName(), metricId.getAdminName(),
                        metricId.getOperationName());
        String key = model.getKey();
        this.metricIdDAO.save(key, model);
    }

    /**
     * Find metric id.
     * 
     * @param keyfromMetricId
     *            the keyfrom metric id
     * @return the metric identifier
     */
    private MetricIdentifier findMetricId(String keyfromMetricId) {
        return this.metricIdDAO.find(keyfromMetricId);
    }

    /**
     * Gets the keyfrom metric id.
     * 
     * @param metricId
     *            the metric id
     * @return the keyfrom metric id
     */
    public String getKeyfromMetricId(org.ebayopensource.turmeric.runtime.common.monitoring.MetricId metricId) {
        return metricId.getMetricName() + "-" + metricId.getAdminName() + "-" + metricId.getOperationName();
    }

    /**
     * Gets the mid period.
     * 
     * @return the mid period
     */
    public int getMidPeriod() {
        return midPeriod;
    }

    /**
     * Gets the long period.
     * 
     * @return the long period
     */
    public int getLongPeriod() {
        return longPeriod;
    }

    /**
     * Checks if is server side.
     * 
     * @return true, if is server side
     */
    public boolean isServerSide() {
        return serverSide;
    }

    /**
     * Checks if is store service metrics.
     * 
     * @return true, if is store service metrics
     */
    public boolean isStoreServiceMetrics() {
        return storeServiceMetrics;
    }

    /**
     * Gets the snapshot interval.
     * 
     * @return the snapshot interval
     */
    public int getSnapshotInterval() {
        return snapshotInterval;
    }

    /**
     * Values are non zero.
     * 
     * @param metricComponentValues
     *            the metric component values
     * @return true, if successful
     */
    protected boolean valuesAreNonZero(
                    org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricComponentValue[] metricComponentValues) {
        for (org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricComponentValue metricComponentValue : metricComponentValues) {
            Number value = (Number) metricComponentValue.getValue();
            if (value.longValue() != 0L)
                return true;
        }
        return false;
    }

    /**
     * Gets the inet address.
     * 
     * @return the inet address
     * @throws ServiceException
     *             the service exception
     */
    public String getIPAddress() throws ServiceException {
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        }
        catch (UnknownHostException x) {
            throw new ServiceException("Unkonwn host name", x);
        }
    }

}
