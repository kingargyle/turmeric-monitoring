/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.cassandra.storage.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.TypeInferringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.MetricIdentifier;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricComponentValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorManager;

import com.google.common.collect.Lists;

/**
 * The Class MetricsDAO.
 */
public class MetricsDAO {

    /** The cluster name. */
    private String clusterName;

    /** The host. */
    private String host;

    /** The key space. */
    private Keyspace keySpace;

    /**
     * Instantiates a new metrics dao.
     * 
     * @param clusterName
     *            the cluster name
     * @param host
     *            the host
     * @param keyspaceName
     *            the keyspace name
     */
    public MetricsDAO(String clusterName, String host, String keyspaceName) {
        this.clusterName = clusterName;
        this.host = host;
        this.keySpace = new HectorManager().getKeyspace(clusterName, host, keyspaceName, "ServiceOperationByIp");
    }

    /**
     * Save service operation by ip cf.
     * 
     * @param ipAddress
     *            the ip address
     * @param cmetricIdentifier
     *            the cmetric identifier
     */
    public void saveServiceOperationByIpCF(String ipAddress, MetricIdentifier cmetricIdentifier) {
        String superColumnName = cmetricIdentifier.getServiceAdminName();

        Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());

        List<HColumn<String, String>> columnList = Arrays.asList(HFactory.createStringColumn(
                        cmetricIdentifier.getOperationName(), ""));

        mutator.addInsertion(ipAddress, "ServiceOperationByIp", HFactory.createSuperColumn(superColumnName, columnList,
                        StringSerializer.get(), StringSerializer.get(), StringSerializer.get()));

        // now I store a row with "All" as key, for when the searches requires to list all the services in all the nodes
        mutator.addInsertion("All", "ServiceOperationByIp", HFactory.createSuperColumn(superColumnName, columnList,
                        StringSerializer.get(), StringSerializer.get(), StringSerializer.get()));

        MutationResult result = mutator.execute();
    }

    /**
     * Save service consumer by ip cf.
     * 
     * @param ipAddress
     *            the ip address
     * @param cmetricIdentifier
     *            the cmetric identifier
     * @param consumerName
     *            the consumer name
     */
    public void saveServiceConsumerByIpCF(String ipAddress, MetricIdentifier cmetricIdentifier, String consumerName) {
        String superColumnName = cmetricIdentifier.getServiceAdminName();

        Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());

        List<HColumn<String, String>> columnList = Arrays.asList(HFactory.createStringColumn(consumerName, ""));

        mutator.addInsertion(ipAddress, "ServiceConsumerByIp", HFactory.createSuperColumn(superColumnName, columnList,
                        StringSerializer.get(), StringSerializer.get(), StringSerializer.get()));

        // now I store a row with "All" as key, for when the searches requires to list all the services in all the nodes
        mutator.addInsertion("All", "ServiceConsumerByIp", HFactory.createSuperColumn(superColumnName, columnList,
                        StringSerializer.get(), StringSerializer.get(), StringSerializer.get()));

        MutationResult result = mutator.execute();
    }

    /**
     * Save metric values.
     * 
     * @param ipAddress
     *            the ip address
     * @param cmetricIdentifier
     *            the cmetric identifier
     * @param now
     *            the now
     * @param snapshotInterval
     *            the snapshot interval
     * @param metricValuesToSave
     *            the metric values to save
     */
    @SuppressWarnings("unchecked")
    public void saveMetricValues(String ipAddress, MetricIdentifier cmetricIdentifier, long now, int snapshotInterval,
                    List<MetricValue> metricValuesToSave) {
        String timeSeriesKey = createKeyForTimeSeries(ipAddress, cmetricIdentifier, snapshotInterval);
        String metricValueKey = null;
        StringSerializer stringSerializer = StringSerializer.get();
        Mutator<String> metricTimeSeriesMutator = HFactory.createMutator(keySpace, stringSerializer);
        ObjectSerializer objectSerializer = ObjectSerializer.get();
        LongSerializer longSerializer = LongSerializer.get();
        for (MetricValue metricValue : metricValuesToSave) {
            metricValueKey = createKyeForMetricValue(ipAddress, cmetricIdentifier, now);
            MetricComponentValue[] metricComponentValues = metricValue.getValues();
            for (MetricComponentValue metricComponentValue : metricComponentValues) {
                metricTimeSeriesMutator.addInsertion(metricValueKey, "MetricValues", HFactory.createColumn(
                                metricComponentValue.getName(), metricComponentValue.getValue(), stringSerializer,
                                objectSerializer));
            }

            metricTimeSeriesMutator.addInsertion(timeSeriesKey, "MetricTimeSeries",
                            HFactory.createColumn(now, metricValueKey, longSerializer, stringSerializer));
        }
        String serviceOperationCallsByTimeKey = createServiceOperationCallsInTime(ipAddress, cmetricIdentifier);
        List<HColumn<String, String>> operationListColumns = Arrays.asList(HFactory.createColumn(
                        cmetricIdentifier.getOperationName(), "", stringSerializer, stringSerializer));
        HSuperColumn<Long, String, String> serviceOperationCallsByTimeColumn = HFactory.createSuperColumn(now,
                        operationListColumns, longSerializer, stringSerializer, stringSerializer);
        metricTimeSeriesMutator.addInsertion(serviceOperationCallsByTimeKey, "ServiceCallsByTime",
                        serviceOperationCallsByTimeColumn);

        MutationResult result = metricTimeSeriesMutator.execute();
    }

    private String createServiceOperationCallsInTime(String ipAddress, MetricIdentifier cmetricIdentifier) {
        return ipAddress + "-" + cmetricIdentifier.getServiceAdminName();
    }

    /**
     * Creates the kye for metric value.
     * 
     * @param ipAddress
     *            the ip address
     * @param cmetricIdentifier
     *            the cmetric identifier
     * @param now
     *            the now
     * @return the string
     */
    private String createKyeForMetricValue(String ipAddress, MetricIdentifier cmetricIdentifier, long now) {
        return ipAddress + "-" + cmetricIdentifier.getMetricName() + "-" + now;
    }

    /**
     * Creates the key for time series.
     * 
     * @param ipAddress
     *            the ip address
     * @param cmetricIdentifier
     *            the cmetric identifier
     * @param snapshotInterval
     *            the snapshot interval
     * @return the string
     */
    private String createKeyForTimeSeries(String ipAddress, MetricIdentifier cmetricIdentifier, int snapshotInterval) {
        return ipAddress + "-" + cmetricIdentifier.getServiceAdminName() + "-" + cmetricIdentifier.getOperationName()
                        + "-" + cmetricIdentifier.getMetricName() + "-" + snapshotInterval;
    }
}
