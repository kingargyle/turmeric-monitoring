/*******************************************************************************
 * Copyright (c) 2006|2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE|2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.OrderedSuperRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.beans.SuperRow;
import me.prettyprint.hector.api.beans.SuperRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.query.MultigetSuperSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.RangeSuperSlicesQuery;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.monitoring.utils.CassandraTestHelper;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricCategory;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricClassifier;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricId;
import org.ebayopensource.turmeric.runtime.common.monitoring.MonitoringLevel;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.AverageMetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.LongSumMetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CassandraMetricsStorageProviderITCase extends CassandraTestHelper {
    CassandraMetricsStorageProvider provider = null;
    Map<String, List<String>> keysToRemove = new HashMap<String, List<String>>();

    @Before
    public void setUp() {
        provider = new CassandraMetricsStorageProvider();
        kspace = new HectorManager().getKeyspace(cluster_name, cassandra_node_ip, keyspace_name, "MetricIdentifier");
        Map<String, String> options = createOptions();
        provider.init(options, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
    }

    @After
    public void tearDown() {
        cleanUpTestData();
        provider = null;
        kspace = null;
    }

    private void cleanUpTestData() {
        String[] columnFamilies = { "MetricTimeSeries", "MetricIdentifier", "MetricValues" };
        String[] superColumnFamilies = { "ServiceCallsByTime", "ServiceConsumerByIp", "ServiceOperationByIp" };

        for (String cf : columnFamilies) {
            RangeSlicesQuery<String, String, String> rq = HFactory.createRangeSlicesQuery(kspace, STR_SERIALIZER,
                            STR_SERIALIZER, STR_SERIALIZER);
            rq.setColumnFamily(cf);
            rq.setRange("", "", false, 1000);
            QueryResult<OrderedRows<String, String, String>> qr = rq.execute();
            OrderedRows<String, String, String> orderedRows = qr.get();
            Mutator<String> deleteMutator = HFactory.createMutator(kspace, STR_SERIALIZER);
            for (Row<String, String, String> row : orderedRows) {
                deleteMutator.delete(row.getKey(), cf, null, STR_SERIALIZER);
            }
        }

        for (String scf : superColumnFamilies) {
            RangeSuperSlicesQuery<String, String, String, String> rq = HFactory.createRangeSuperSlicesQuery(kspace,
                            STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER);
            rq.setColumnFamily(scf);
            rq.setRange("", "", false, 1000);
            QueryResult<OrderedSuperRows<String, String, String, String>> qr = rq.execute();
            OrderedSuperRows<String, String, String, String> orderedRows = qr.get();
            Mutator<String> deleteMutator = HFactory.createMutator(kspace, STR_SERIALIZER);

            for (SuperRow<String, String, String, String> row : orderedRows) {
                deleteMutator.delete(row.getKey(), scf, null, STR_SERIALIZER);
            }
        }

    }

    @Test
    public void testInit() {
        assertFalse(provider.isStoreServiceMetrics());
        assertEquals(20, provider.getSnapshotInterval());
    }

    @Test
    public void testGetIpAddress() throws ServiceException, UnknownHostException {
        String ipAddress = provider.getIPAddress();
        String expectedIpAddress = InetAddress.getLocalHost().getCanonicalHostName();
        assertEquals(expectedIpAddress, ipAddress);
    }

    @Test
    public void testGetKeyfromMetricId() {
        org.ebayopensource.turmeric.runtime.common.monitoring.MetricId metricId = new MetricId("metricName",
                        "ServiceAdminName", "operationName");
        String expectedKey = metricId.getMetricName() + CassandraMetricsStorageProvider.KEY_SEPARATOR
                        + metricId.getAdminName() + CassandraMetricsStorageProvider.KEY_SEPARATOR
                        + metricId.getOperationName() + "|true";
        String actualKey = provider.getKeyfromMetricId(metricId, true);
        assertEquals(expectedKey, actualKey);
    }

    private Map<String, String> createOptions() {
        Map<String, String> options = new HashMap<String, String>();
        options.put("hostName", cassandra_node_ip);
        options.put("keyspaceName", keyspace_name);
        options.put("clusterName", cluster_name);
        options.put("storeServiceMetrics", "false");
        return options;
    }

    @Test
    public void testSaveMetricSnapshot() throws ServiceException {
        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection();
        long timeSnapshot = System.currentTimeMillis();
        String ipAddress = provider.getIPAddress();
        String metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_count"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
        String metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_average"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
        String metricSeriesKeyForTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR
                        + "service1|operation1|test_count|20";
        String metricSeriesKeyForTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR
                        + "service2|operation2|test_average|20";
        String serviceCallsByTimeKey = ipAddress + "|service1|true";
        String serviceCallsByTimeKeySrv2 = ipAddress + "|service2|true";
        String[] metricIdentifierColumns = new String[] { "metricName", "serviceAdminName", "operationName" };
        Long[] metricSeriesColumn = new Long[] { timeSnapshot };

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraColumnValues("MetricIdentifier", "test_count|service1|operation1|true", STR_SERIALIZER,
                        STR_SERIALIZER, metricIdentifierColumns,
                        new String[] { "test_count", "service1", "operation1" });

        assertCassandraColumnValues("MetricIdentifier", "test_average|service2|operation2|true", STR_SERIALIZER,
                        STR_SERIALIZER, metricIdentifierColumns, new String[] { "test_average", "service2",
                                "operation2" });

        assertCassandraSuperColumnValues("ServiceOperationByIp", ipAddress, "service1", STR_SERIALIZER, STR_SERIALIZER,
                        STR_SERIALIZER, new String[] { "operation1" }, new String[] { "" });

        assertCassandraSuperColumnValues("ServiceOperationByIp", ipAddress, "service2", STR_SERIALIZER, STR_SERIALIZER,
                        STR_SERIALIZER, new String[] { "operation2" }, new String[] { "" });

        assertCassandraSuperColumnValues("ServiceConsumerByIp", ipAddress, "service1", STR_SERIALIZER, STR_SERIALIZER,
                        STR_SERIALIZER, new String[] { "missing" }, new String[] { "" });

        assertCassandraSuperColumnValues("ServiceConsumerByIp", ipAddress, "service2", STR_SERIALIZER, STR_SERIALIZER,
                        STR_SERIALIZER, new String[] { "anotherusecase" }, new String[] { "" });

        assertCassandraColumnValues("MetricTimeSeries", metricSeriesKeyForTestCount, LONG_SERIALIZER, STR_SERIALIZER,
                        metricSeriesColumn, new String[] { metricValueKeyTestCount });

        assertCassandraColumnValues("MetricTimeSeries", metricSeriesKeyForTestAvg, LONG_SERIALIZER, STR_SERIALIZER,
                        metricSeriesColumn, new String[] { metricValueKeyTestAvg });

        assertCassandraColumnValues("MetricValues", metricValueKeyTestCount, STR_SERIALIZER, OBJ_SERIALIZER,
                        new String[] { "value" }, new Long[] { 20L });

        assertCassandraColumnValues("MetricValues", metricValueKeyTestAvg, STR_SERIALIZER, OBJ_SERIALIZER,
                        new String[] { "count", "totalTime" }, new Object[] { 2L, 345d });

        assertCassandraSuperColumnValues("ServiceCallsByTime", serviceCallsByTimeKey, Long.valueOf(timeSnapshot),
                        LONG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, new String[] { "operation1" },
                        new String[] { "" });

        assertCassandraSuperColumnValues("ServiceCallsByTime", serviceCallsByTimeKeySrv2, Long.valueOf(timeSnapshot),
                        LONG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, new String[] { "operation2" },
                        new String[] { "" });
    }

    @Test
    public void testSaveMetricSnapshotTwoMetricsTwoConsumersSameTimestampForSameOperationAndService()
                    throws ServiceException {
        String serviceName = "ServiceX";
        String operationName = "operationY";
        String ipAddress = provider.getIPAddress();
        Object serviceCallsByTimeKey = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + serviceName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + true;

        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection(serviceName,
                        operationName);
        long timeSnapshot = System.currentTimeMillis();
        String metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_count"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
        String metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_average"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraColumnValues("MetricIdentifier", "test_count|" + serviceName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + "true", STR_SERIALIZER, STR_SERIALIZER,
                        new String[] { "metricName", "serviceAdminName", "operationName" }, new String[] {
                                "test_count", serviceName, operationName });

        assertCassandraColumnValues("MetricIdentifier", "test_average|" + serviceName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + "true", STR_SERIALIZER, STR_SERIALIZER,
                        new String[] { "metricName", "serviceAdminName", "operationName" }, new String[] {
                                "test_average", serviceName, operationName });

        assertCassandraSuperColumnValues("ServiceOperationByIp", ipAddress, serviceName, STR_SERIALIZER,
                        STR_SERIALIZER, STR_SERIALIZER, new String[] { operationName }, new String[] { "" });

        assertCassandraSuperColumnValues("ServiceConsumerByIp", ipAddress, serviceName, STR_SERIALIZER, STR_SERIALIZER,
                        STR_SERIALIZER, new String[] { "missing" }, new String[] { "" });

        assertCassandraSuperColumnValues("ServiceConsumerByIp", ipAddress, serviceName, STR_SERIALIZER, STR_SERIALIZER,
                        STR_SERIALIZER, new String[] { "anotherusecase" }, new String[] { "" });

        assertCassandraColumnValues("MetricTimeSeries", ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR
                        + serviceName + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName
                        + "|test_count|20", LONG_SERIALIZER, STR_SERIALIZER, new Long[] { timeSnapshot },
                        new String[] { metricValueKeyTestCount });

        assertCassandraColumnValues("MetricTimeSeries", ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR
                        + serviceName + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName
                        + "|test_average|20", LONG_SERIALIZER, STR_SERIALIZER, new Long[] { timeSnapshot },
                        new String[] { metricValueKeyTestAvg });

        assertCassandraColumnValues("MetricValues", metricValueKeyTestCount, STR_SERIALIZER, OBJ_SERIALIZER,
                        new String[] { "value" }, new Object[] { 123456l });

        assertCassandraColumnValues("MetricValues", metricValueKeyTestAvg, STR_SERIALIZER, OBJ_SERIALIZER,
                        new String[] { "count", "totalTime" }, new Object[] { 17l, 456854235.123d });

        assertCassandraSuperColumnValues("ServiceCallsByTime", serviceCallsByTimeKey, Long.valueOf(timeSnapshot),
                        LONG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, new String[] { operationName },
                        new String[] { "" });

    }

    @Test
    public void testSaveMetricSnapshotTwoMetricsOneConsumerSameTimestampForSameOperationAndService()
                    throws ServiceException {

        String serviceName = "ServiceX1";
        String operationName = "operationY1";
        String consumerName = "consumerZ1";

        String ipAddress = provider.getIPAddress();
        Object serviceCallsByTimeKey = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + serviceName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + true;
        long timeSnapshot = System.currentTimeMillis();

        String metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_count"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
        String metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_average"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollectionForOneConsumer(
                        serviceName, operationName, consumerName);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraColumnValues("MetricIdentifier", "test_count|" + serviceName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + "true", STR_SERIALIZER, STR_SERIALIZER,
                        new String[] { "metricName", "serviceAdminName", "operationName" }, new String[] {
                                "test_count", serviceName, operationName });

        assertCassandraColumnValues("MetricIdentifier", "test_average|" + serviceName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + "true", STR_SERIALIZER, STR_SERIALIZER,
                        new String[] { "metricName", "serviceAdminName", "operationName" }, new String[] {
                                "test_average", serviceName, operationName });

        assertCassandraSuperColumnValues("ServiceOperationByIp", ipAddress, serviceName, STR_SERIALIZER,
                        STR_SERIALIZER, STR_SERIALIZER, new String[] { operationName }, new String[] { "" });

        assertCassandraSuperColumnValues("ServiceConsumerByIp", ipAddress, serviceName, STR_SERIALIZER, STR_SERIALIZER,
                        STR_SERIALIZER, new String[] { consumerName }, new String[] { "" });

        assertCassandraColumnValues("MetricTimeSeries", ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR
                        + serviceName + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName
                        + "|test_count|20", LONG_SERIALIZER, STR_SERIALIZER, new Long[] { timeSnapshot },
                        new String[] { metricValueKeyTestCount });

        assertCassandraColumnValues("MetricTimeSeries", ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR
                        + serviceName + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName
                        + "|test_average|20", LONG_SERIALIZER, STR_SERIALIZER, new Long[] { timeSnapshot },
                        new String[] { metricValueKeyTestAvg });

        assertCassandraColumnValues("MetricValues", metricValueKeyTestCount, STR_SERIALIZER, OBJ_SERIALIZER,
                        new String[] { "value" }, new Object[] { 123456l });

        assertCassandraColumnValues("MetricValues", metricValueKeyTestAvg, STR_SERIALIZER, OBJ_SERIALIZER,
                        new String[] { "count", "totalTime" }, new Object[] { 17l, 456854235.123d });

        assertCassandraSuperColumnValues("ServiceCallsByTime", serviceCallsByTimeKey, Long.valueOf(timeSnapshot),
                        LONG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, new String[] { operationName },
                        new String[] { "" });

    }

    private Collection<MetricValueAggregator> createMetricValueAggregatorsCollection() {
        Collection<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
        MetricId metricId1 = new MetricId("test_count", "service1", "operation1");
        MetricValue metricValue1 = new LongSumMetricValue(metricId1, 20);
        MetricId metricId2 = new MetricId("test_average", "service2", "operation2");
        MetricValue metricValue2 = new AverageMetricValue(metricId2, 2, 345);

        MetricClassifier metricClassifier1 = new MetricClassifier("missing", "sourcedc", "targetdc");
        MetricClassifier metricClassifier2 = new MetricClassifier("anotherusecase", "sourcedc", "targetdc");

        Map<MetricClassifier, MetricValue> valuesByClassifier1 = new HashMap<MetricClassifier, MetricValue>();
        valuesByClassifier1.put(metricClassifier1, metricValue1);

        MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1,
                        MetricCategory.Timing, MonitoringLevel.NORMAL, valuesByClassifier1);

        result.add(aggregator1);

        Map<MetricClassifier, MetricValue> valuesByClassifier2 = new HashMap<MetricClassifier, MetricValue>();
        valuesByClassifier2.put(metricClassifier2, metricValue2);
        MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2,
                        MetricCategory.Timing, MonitoringLevel.NORMAL, valuesByClassifier2);

        result.add(aggregator2);

        return result;
    }

    private Collection<MetricValueAggregator> createMetricValueAggregatorsCollection(String serviceName,
                    String operationName) {
        Collection<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
        MetricId metricId1 = new MetricId("test_count", serviceName, operationName);
        MetricValue metricValue1 = new LongSumMetricValue(metricId1, 123456);
        MetricId metricId2 = new MetricId("test_average", serviceName, operationName);
        MetricValue metricValue2 = new AverageMetricValue(metricId2, 17, 456854235.123);

        MetricClassifier metricClassifier1 = new MetricClassifier("missing", "sourcedc", "targetdc");
        MetricClassifier metricClassifier2 = new MetricClassifier("anotherusecase", "sourcedc", "targetdc");

        Map<MetricClassifier, MetricValue> valuesByClassifier1 = new HashMap<MetricClassifier, MetricValue>();
        valuesByClassifier1.put(metricClassifier1, metricValue1);

        MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1,
                        MetricCategory.Timing, MonitoringLevel.NORMAL, valuesByClassifier1);

        result.add(aggregator1);

        Map<MetricClassifier, MetricValue> valuesByClassifier2 = new HashMap<MetricClassifier, MetricValue>();
        valuesByClassifier2.put(metricClassifier2, metricValue2);
        MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2,
                        MetricCategory.Timing, MonitoringLevel.NORMAL, valuesByClassifier2);

        result.add(aggregator2);

        return result;
    }

    private Collection<MetricValueAggregator> createMetricValueAggregatorsCollectionForOneConsumer(String serviceName,
                    String operationName, String consumerName) {
        Collection<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
        MetricId metricId1 = new MetricId("test_count", serviceName, operationName);
        MetricValue metricValue1 = new LongSumMetricValue(metricId1, 123456);
        MetricId metricId2 = new MetricId("test_average", serviceName, operationName);
        MetricValue metricValue2 = new AverageMetricValue(metricId2, 17, 456854235.123);

        MetricClassifier metricClassifier1 = new MetricClassifier(consumerName, "sourcedc", "targetdc");
        MetricClassifier metricClassifier2 = new MetricClassifier(consumerName, "sourcedc", "targetdc");

        Map<MetricClassifier, MetricValue> valuesByClassifier1 = new HashMap<MetricClassifier, MetricValue>();
        valuesByClassifier1.put(metricClassifier1, metricValue1);

        MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1,
                        MetricCategory.Timing, MonitoringLevel.NORMAL, valuesByClassifier1);

        result.add(aggregator1);

        Map<MetricClassifier, MetricValue> valuesByClassifier2 = new HashMap<MetricClassifier, MetricValue>();
        valuesByClassifier2.put(metricClassifier2, metricValue2);
        MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2,
                        MetricCategory.Timing, MonitoringLevel.NORMAL, valuesByClassifier2);

        result.add(aggregator2);

        return result;
    }
}
