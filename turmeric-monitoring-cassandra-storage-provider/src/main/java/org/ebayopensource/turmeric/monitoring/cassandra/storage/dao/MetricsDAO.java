/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.cassandra.storage.dao;

import static org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider.KEY_SEPARATOR;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
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

/**
 * The Class MetricsDAO.
 */
public class MetricsDAO {

    private static final StringSerializer STR_SERIALIZER = StringSerializer.get();

    private static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("ddMMyyyy");

    /** The cluster name. */
    private String clusterName;

    /** The host. */
    private String host;

    /** The key space. */
    private Keyspace keySpace;

    ObjectSerializer OBJ_SERIALIZER = ObjectSerializer.get();
    LongSerializer LNG_SERIALIZER = LongSerializer.get();

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

        Mutator<String> mutator = HFactory.createMutator(keySpace, STR_SERIALIZER);

        List<HColumn<String, String>> columnList = Arrays.asList(HFactory.createStringColumn(
                        cmetricIdentifier.getOperationName(), ""));

        mutator.addInsertion(ipAddress, "ServiceOperationByIp", HFactory.createSuperColumn(superColumnName, columnList,
                        STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER));

        // now I store a row with "All" as key, for when the searches requires to list all the services in all the nodes
        mutator.addInsertion("All", "ServiceOperationByIp", HFactory.createSuperColumn(superColumnName, columnList,
                        STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER));

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

        Mutator<String> mutator = HFactory.createMutator(keySpace, STR_SERIALIZER);

        List<HColumn<String, String>> columnList = Arrays.asList(HFactory.createStringColumn(consumerName, ""));

        mutator.addInsertion(ipAddress, "ServiceConsumerByIp", HFactory.createSuperColumn(superColumnName, columnList,
                        STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER));

        // now I store a row with "All" as key, for when the searches requires to list all the services in all the nodes
        mutator.addInsertion("All", "ServiceConsumerByIp", HFactory.createSuperColumn(superColumnName, columnList,
                        STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER));

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
                    boolean serverSide, List<MetricValue> metricValuesToSave) {
        String timeSeriesKey = createKeyForTimeSeries(ipAddress, cmetricIdentifier, snapshotInterval);
        String metricValueKey = null;
        String metricValuesByIpDate = null;

        Mutator<String> metricTimeSeriesMutator = HFactory.createMutator(keySpace, STR_SERIALIZER);

        for (MetricValue metricValue : metricValuesToSave) {
            metricValueKey = createKeyForMetricValue(ipAddress, cmetricIdentifier, now);
            metricValuesByIpDate = createKeyForMetricValuesByIpAndDate(ipAddress);
            MetricComponentValue[] metricComponentValues = metricValue.getValues();
            for (MetricComponentValue metricComponentValue : metricComponentValues) {
                metricTimeSeriesMutator.addInsertion(metricValueKey, "MetricValues", HFactory.createColumn(
                                metricComponentValue.getName(), metricComponentValue.getValue(), STR_SERIALIZER,
                                OBJ_SERIALIZER));

                List<HColumn<String, String>> metricValuesColumns = Arrays.asList(HFactory.createColumn(metricValueKey,
                                "", STR_SERIALIZER, STR_SERIALIZER));

                HSuperColumn<Long, String, String> serviceOperationCallsByTimeColumn = HFactory.createSuperColumn(now,
                                metricValuesColumns, LNG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER);

                metricTimeSeriesMutator.addInsertion(metricValuesByIpDate, "MetricValuesByIpAndDate",
                                serviceOperationCallsByTimeColumn);

            }

            metricTimeSeriesMutator.addInsertion(timeSeriesKey, "MetricTimeSeries",
                            HFactory.createColumn(now, metricValueKey, LNG_SERIALIZER, STR_SERIALIZER));
        }
        String serviceOperationCallsByTimeKey = createServiceOperationCallsInTime(ipAddress, cmetricIdentifier);
        List<HColumn<String, String>> operationListColumns = Arrays.asList(HFactory.createColumn(
                        cmetricIdentifier.getOperationName(), "", STR_SERIALIZER, STR_SERIALIZER));
        HSuperColumn<Long, String, String> serviceOperationCallsByTimeColumn = HFactory.createSuperColumn(now,
                        operationListColumns, LNG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER);
        metricTimeSeriesMutator.addInsertion(serviceOperationCallsByTimeKey, "ServiceCallsByTime",
                        serviceOperationCallsByTimeColumn);

        MutationResult result = metricTimeSeriesMutator.execute();
    }

    public String createKeyForMetricValuesByIpAndDate(String ipAddress) {
        String date = DATE_FORMATER.format(new Date());
        String result = ipAddress + KEY_SEPARATOR + date;
        return result;
    }

    public String createServiceOperationCallsInTime(String ipAddress, MetricIdentifier cmetricIdentifier) {
        return ipAddress + KEY_SEPARATOR + cmetricIdentifier.getServiceAdminName() + KEY_SEPARATOR
                        + cmetricIdentifier.isServerSide();
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
    private String createKeyForMetricValue(String ipAddress, MetricIdentifier cmetricIdentifier, long now) {
        return ipAddress + KEY_SEPARATOR + cmetricIdentifier.getMetricName() + KEY_SEPARATOR + now;
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
        return ipAddress + KEY_SEPARATOR + cmetricIdentifier.getServiceAdminName() + KEY_SEPARATOR
                        + cmetricIdentifier.getOperationName() + KEY_SEPARATOR + cmetricIdentifier.getMetricName()
                        + KEY_SEPARATOR + snapshotInterval;
    }
}
