/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
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

import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.beans.SuperRow;
import me.prettyprint.hector.api.beans.SuperRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.query.MultigetSuperSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.monitoring.utils.CassandraTestHelper;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
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
        provider.init(options, null, null, 20);
    }

    @After
    public void tearDown() {
        cleanUpTestData();
        provider = null;
        kspace = null;
    }

    private void cleanUpTestData() {
        if (!keysToRemove.isEmpty()) {
            if (keysToRemove.containsKey("MetricTimeSeries")) {
                // first, cleanup of "MetricTimeSeries"
                MultigetSliceQuery<String, Long, String> multigetSliceQuery = HFactory.createMultigetSliceQuery(kspace,
                                STR_SERIALIZER, LONG_SERIALIZER, STR_SERIALIZER);
                multigetSliceQuery.setColumnFamily("MetricTimeSeries");
                multigetSliceQuery.setKeys(keysToRemove.get("MetricTimeSeries"));
                multigetSliceQuery.setRange(0l, Long.MAX_VALUE, false, 3);

                QueryResult<Rows<String, Long, String>> result = multigetSliceQuery.execute();
                Mutator<String> metricTimeSeries = HFactory.createMutator(kspace, STR_SERIALIZER);
                for (Row<String, Long, String> row : result.get()) {
                    System.out.println("MetricTimeSeries:key to delete=" + row.getKey());
                    metricTimeSeries.delete(row.getKey(), "MetricTimeSeries", null, STR_SERIALIZER);
                }

            }
            if (keysToRemove.containsKey("MetricIdentifier")) {
                MultigetSliceQuery<String, String, String> multigetSliceQuery = HFactory.createMultigetSliceQuery(
                                kspace, STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER);
                multigetSliceQuery.setColumnFamily("MetricIdentifier");
                multigetSliceQuery.setKeys(keysToRemove.get("MetricIdentifier"));
                multigetSliceQuery.setRange("", "", false, 3);
                QueryResult<Rows<String, String, String>> result = multigetSliceQuery.execute();
                Mutator<String> metricIdentifier = HFactory.createMutator(kspace, STR_SERIALIZER);
                for (Row<String, String, String> row : result.get()) {
                    System.out.println("MetricIdentifier:key to delete=" + row.getKey());
                    metricIdentifier.delete(row.getKey(), "MetricIdentifier", null, STR_SERIALIZER);
                }
            }
            if (keysToRemove.containsKey("ServiceOperationByIp")) {
                MultigetSuperSliceQuery<String, String, String, String> multigetSliceQuery = HFactory
                                .createMultigetSuperSliceQuery(kspace, STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER,
                                                STR_SERIALIZER);
                multigetSliceQuery.setColumnFamily("ServiceOperationByIp");
                multigetSliceQuery.setKeys(keysToRemove.get("ServiceOperationByIp"));
                multigetSliceQuery.setRange("", "", false, 3);
                QueryResult<SuperRows<String, String, String, String>> result = multigetSliceQuery.execute();
                Mutator<String> serviceOperationByIp = HFactory.createMutator(kspace, STR_SERIALIZER);
                for (SuperRow<String, String, String, String> row : result.get()) {
                    System.out.println("ServiceOperationByIp:key to delete=" + row.getKey());
                    serviceOperationByIp.delete(row.getKey(), "ServiceOperationByIp", null, STR_SERIALIZER);
                }
            }
            if (keysToRemove.containsKey("ServiceConsumerByIp")) {
                MultigetSuperSliceQuery<String, String, String, String> multigetSliceQuery = HFactory
                                .createMultigetSuperSliceQuery(kspace, STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER,
                                                STR_SERIALIZER);
                multigetSliceQuery.setColumnFamily("ServiceConsumerByIp");
                multigetSliceQuery.setKeys(keysToRemove.get("ServiceConsumerByIp"));
                multigetSliceQuery.setRange("", "", false, 3);
                QueryResult<SuperRows<String, String, String, String>> result = multigetSliceQuery.execute();
                Mutator<String> serviceConsumerByIp = HFactory.createMutator(kspace, STR_SERIALIZER);
                for (SuperRow<String, String, String, String> row : result.get()) {
                    System.out.println("ServiceConsumerByIp:key to delete=" + row.getKey());
                    serviceConsumerByIp.delete(row.getKey(), "ServiceConsumerByIp", null, STR_SERIALIZER);
                }
            }
            if (keysToRemove.containsKey("MetricValues")) {
                MultigetSliceQuery<String, String, String> multigetSliceQuery = HFactory.createMultigetSliceQuery(
                                kspace, STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER);
                multigetSliceQuery.setColumnFamily("MetricValues");
                multigetSliceQuery.setKeys(keysToRemove.get("MetricValues"));
                multigetSliceQuery.setRange("", "", false, 3);
                QueryResult<Rows<String, String, String>> result = multigetSliceQuery.execute();
                Mutator<String> metricIdentifier = HFactory.createMutator(kspace, STR_SERIALIZER);
                for (Row<String, String, String> row : result.get()) {
                    System.out.println("MetricValues:key to delete=" + row.getKey());
                    metricIdentifier.delete(row.getKey(), "MetricValues", null, STR_SERIALIZER);
                }
            }
            keysToRemove.clear();
        }
    }

    @Test
    public void testInit() {
        assertFalse(provider.isStoreServiceMetrics());
        assertEquals(3600, provider.getMidPeriod());
        assertEquals(86400, provider.getLongPeriod());
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
        String expectedKey = metricId.getMetricName() + "-" + metricId.getAdminName() + "-"
                        + metricId.getOperationName();
        String actualKey = provider.getKeyfromMetricId(metricId);
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
        String metricValueKeyTestCount = ipAddress + "-" + "test_count" + "-" + timeSnapshot;
        String metricValueKeyTestAvg = ipAddress + "-" + "test_average" + "-" + timeSnapshot;
        String metricSeriesKeyForTestCount = ipAddress + "-" + "service1-operation1-test_count-20";
        String metricSeriesKeyForTestAvg = ipAddress + "-" + "service2-operation2-test_average-20";
        String serviceCallsByTimeKey = ipAddress + "-service1";
        String serviceCallsByTimeKeySrv2 = ipAddress + "-service2";
        String[] metricIdentifierColumns = new String[] { "metricName", "serviceAdminName", "operationName" };
        Long[] metricSeriesColumn = new Long[] { timeSnapshot };

        addToKeyCleanupList("MetricTimeSeries", metricSeriesKeyForTestCount);
        addToKeyCleanupList("MetricTimeSeries", metricSeriesKeyForTestAvg);
        addToKeyCleanupList("MetricIdentifier", "test_count-service1-operation1");
        addToKeyCleanupList("MetricIdentifier", "test_average-service2-operation2");
        addToKeyCleanupList("ServiceOperationByIp", ipAddress);
        addToKeyCleanupList("ServiceOperationByIp", "All");
        addToKeyCleanupList("ServiceConsumerByIp", "All");
        addToKeyCleanupList("ServiceConsumerByIp", ipAddress);
        addToKeyCleanupList("MetricValues", metricValueKeyTestCount);
        addToKeyCleanupList("MetricValues", metricValueKeyTestAvg);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);
        System.out.println("Time in ms =" + (System.currentTimeMillis() - timeSnapshot));

        assertCassandraColumnValues("MetricIdentifier", "test_count-service1-operation1", STR_SERIALIZER,
                        STR_SERIALIZER, metricIdentifierColumns,
                        new String[] { "test_count", "service1", "operation1" });

        assertCassandraColumnValues("MetricIdentifier", "test_average-service2-operation2", STR_SERIALIZER,
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

    private void addToKeyCleanupList(String cfName, String key) {
        List<String> keys = this.keysToRemove.get(cfName);
        if (keys == null) {
            keys = new ArrayList<String>();
            this.keysToRemove.put(cfName, keys);
        }
        keys.add(key);
    }

    @Test
    public void testSaveMetricSnapshotTwoMetricsTwoConsumersSameTimestampForSameOperationAndService()
                    throws ServiceException {
        String serviceName = "ServiceX";
        String operationName = "operationY";

        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection(serviceName,
                        operationName);
        long timeSnapshot = System.currentTimeMillis();
        String metricValueKeyTestCount = provider.getIPAddress() + "-" + "test_count" + "-" + timeSnapshot;
        String metricValueKeyTestAvg = provider.getIPAddress() + "-" + "test_average" + "-" + timeSnapshot;

        addToKeyCleanupList("MetricTimeSeries", provider.getIPAddress() + "-" + serviceName + "-" + operationName
                        + "-test_count-20");
        addToKeyCleanupList("MetricTimeSeries", provider.getIPAddress() + "-" + serviceName + "-" + operationName
                        + "-test_average-20");
        addToKeyCleanupList("MetricIdentifier", "test_count-" + serviceName + "-" + operationName);
        addToKeyCleanupList("MetricIdentifier", "test_average-" + serviceName + "-" + operationName);
        addToKeyCleanupList("ServiceOperationByIp", provider.getIPAddress());
        addToKeyCleanupList("ServiceOperationByIp", "All");
        addToKeyCleanupList("ServiceConsumerByIp", "All");
        addToKeyCleanupList("ServiceConsumerByIp", provider.getIPAddress());
        addToKeyCleanupList("MetricValues", metricValueKeyTestCount);
        addToKeyCleanupList("MetricValues", metricValueKeyTestAvg);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);
        System.out.println("Time in ms =" + (System.currentTimeMillis() - timeSnapshot));
        // now I need to retrieve the values. I use Hector for this.
        ColumnSlice<Object, Object> errorColumnSlice = getColumnValues(kspace, "MetricIdentifier", "test_count-"
                        + serviceName + "-" + operationName, STR_SERIALIZER, STR_SERIALIZER, "metricName",
                        "serviceAdminName", "operationName");
        assertValues(errorColumnSlice, "metricName", "test_count", "serviceAdminName", serviceName, "operationName",
                        operationName);

        ColumnSlice<Object, Object> errorColumnSlice2 = getColumnValues(kspace, "MetricIdentifier", "test_average-"
                        + serviceName + "-" + operationName, STR_SERIALIZER, STR_SERIALIZER, "metricName",
                        "serviceAdminName", "operationName");
        assertValues(errorColumnSlice2, "metricName", "test_average", "serviceAdminName", serviceName, "operationName",
                        operationName);

        HSuperColumn<Object, Object, Object> serviceOperationColumnSlice = getSuperColumnValues(kspace,
                        "ServiceOperationByIp", provider.getIPAddress(), STR_SERIALIZER, STR_SERIALIZER,
                        STR_SERIALIZER, serviceName);
        // assertValues(serviceOperationColumnSlice, "ServiceX", "operationY", "");
        assertColumnValueFromSuperColumn(serviceOperationColumnSlice, new String[] { operationName },
                        new String[] { "" });

        // now the consumer cf
        HSuperColumn<Object, Object, Object> serviceConsumerColumnSlice = getSuperColumnValues(kspace,
                        "ServiceConsumerByIp", provider.getIPAddress(), STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER,
                        serviceName);
        assertColumnValueFromSuperColumn(serviceConsumerColumnSlice, new String[] { "missing" }, new String[] { "" });

        // now the consumer cf
        HSuperColumn<Object, Object, Object> serviceConsumerColumnSlice2 = getSuperColumnValues(kspace,
                        "ServiceConsumerByIp", provider.getIPAddress(), STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER,
                        serviceName);
        assertColumnValueFromSuperColumn(serviceConsumerColumnSlice2, new String[] { "anotherusecase" },
                        new String[] { "" });

        // now I check the MetricTimeSeries

        ColumnSlice<Object, Object> metricTimeSeriesColumnSlice = getColumnValues(kspace, "MetricTimeSeries",
                        provider.getIPAddress() + "-" + serviceName + "-" + operationName + "-test_count-20",
                        LONG_SERIALIZER, STR_SERIALIZER, timeSnapshot);
        assertValues(metricTimeSeriesColumnSlice, timeSnapshot, metricValueKeyTestCount);

        ColumnSlice<Object, Object> metricTimeSeriesColumnSlice2 = getColumnValues(kspace, "MetricTimeSeries",
                        provider.getIPAddress() + "-" + serviceName + "-" + operationName + "-test_average-20",
                        LONG_SERIALIZER, STR_SERIALIZER, timeSnapshot);
        assertValues(metricTimeSeriesColumnSlice2, timeSnapshot, metricValueKeyTestAvg);

        // now I check the MetricValues
        ColumnSlice<Object, Object> metricValuesColumnSlice = getColumnValues(kspace, "MetricValues",
                        metricValueKeyTestCount, STR_SERIALIZER, OBJ_SERIALIZER, "value");
        assertValues(metricValuesColumnSlice, "value", 123456l);

        // now I check the MetricValues
        ColumnSlice<Object, Object> metricValuesColumnSlice2 = getColumnValues(kspace, "MetricValues",
                        metricValueKeyTestAvg, STR_SERIALIZER, OBJ_SERIALIZER, "count", "totalTime");
        assertValues(metricValuesColumnSlice2, "count", 17l, "totalTime", 456854235.123d);

    }

    @Test
    public void testSaveMetricSnapshotTwoMetricsOneConsumerSameTimestampForSameOperationAndService()
                    throws ServiceException {

        String serviceName = "ServiceX1";
        String operationName = "operationY1";
        String consumerName = "consumerZ1";

        long timeSnapshot = System.currentTimeMillis();
        String metricValueKeyTestCount = provider.getIPAddress() + "-" + "test_count" + "-" + timeSnapshot;
        String metricValueKeyTestAvg = provider.getIPAddress() + "-" + "test_average" + "-" + timeSnapshot;
        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollectionForOneConsumer(
                        serviceName, operationName, consumerName);

        addToKeyCleanupList("MetricTimeSeries", provider.getIPAddress() + "-" + serviceName + "-" + operationName
                        + "-test_count-20");
        addToKeyCleanupList("MetricTimeSeries", provider.getIPAddress() + "-" + serviceName + "-" + operationName
                        + "-test_average-20");
        addToKeyCleanupList("MetricIdentifier", "test_count-" + serviceName + "-" + operationName);
        addToKeyCleanupList("MetricIdentifier", "test_average-" + serviceName + "-" + operationName);
        addToKeyCleanupList("ServiceOperationByIp", provider.getIPAddress());
        addToKeyCleanupList("ServiceOperationByIp", "All");
        addToKeyCleanupList("ServiceConsumerByIp", provider.getIPAddress());
        addToKeyCleanupList("ServiceConsumerByIp", "All");
        addToKeyCleanupList("MetricValues", metricValueKeyTestCount);
        addToKeyCleanupList("MetricValues", metricValueKeyTestAvg);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);
        System.out.println("Time in ms =" + (System.currentTimeMillis() - timeSnapshot));
        // now I need to retrieve the values. I use Hector for this.
        ColumnSlice<Object, Object> errorColumnSlice = getColumnValues(kspace, "MetricIdentifier", "test_count-"
                        + serviceName + "-" + operationName, STR_SERIALIZER, STR_SERIALIZER, "metricName",
                        "serviceAdminName", "operationName");
        assertValues(errorColumnSlice, "metricName", "test_count", "serviceAdminName", serviceName, "operationName",
                        operationName);

        ColumnSlice<Object, Object> errorColumnSlice2 = getColumnValues(kspace, "MetricIdentifier", "test_average-"
                        + serviceName + "-" + operationName, STR_SERIALIZER, STR_SERIALIZER, "metricName",
                        "serviceAdminName", "operationName");
        assertValues(errorColumnSlice2, "metricName", "test_average", "serviceAdminName", serviceName, "operationName",
                        operationName);

        HSuperColumn<Object, Object, Object> serviceOperationColumnSlice = getSuperColumnValues(kspace,
                        "ServiceOperationByIp", provider.getIPAddress(), STR_SERIALIZER, STR_SERIALIZER,
                        STR_SERIALIZER, serviceName);
        // assertValues(serviceOperationColumnSlice, "ServiceX", "operationY", "");
        assertColumnValueFromSuperColumn(serviceOperationColumnSlice, new String[] { operationName },
                        new String[] { "" });

        // now the consumer cf
        HSuperColumn<Object, Object, Object> serviceConsumerColumnSlice = getSuperColumnValues(kspace,
                        "ServiceConsumerByIp", provider.getIPAddress(), STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER,
                        serviceName);
        assertColumnValueFromSuperColumn(serviceConsumerColumnSlice, new String[] { consumerName }, new String[] { "" });

        // now I check the MetricTimeSeries

        ColumnSlice<Object, Object> metricTimeSeriesColumnSlice = getColumnValues(kspace, "MetricTimeSeries",
                        provider.getIPAddress() + "-" + serviceName + "-" + operationName + "-test_count-20",
                        LONG_SERIALIZER, STR_SERIALIZER, timeSnapshot);
        assertValues(metricTimeSeriesColumnSlice, timeSnapshot, metricValueKeyTestCount);

        ColumnSlice<Object, Object> metricTimeSeriesColumnSlice2 = getColumnValues(kspace, "MetricTimeSeries",
                        provider.getIPAddress() + "-" + serviceName + "-" + operationName + "-test_average-20",
                        LONG_SERIALIZER, STR_SERIALIZER, timeSnapshot);
        assertValues(metricTimeSeriesColumnSlice2, timeSnapshot, metricValueKeyTestAvg);

        // now I check the MetricValues
        ColumnSlice<Object, Object> metricValuesColumnSlice = getColumnValues(kspace, "MetricValues",
                        metricValueKeyTestCount, STR_SERIALIZER, OBJ_SERIALIZER, "value");
        assertValues(metricValuesColumnSlice, "value", 123456l);

        // now I check the MetricValues
        ColumnSlice<Object, Object> metricValuesColumnSlice2 = getColumnValues(kspace, "MetricValues",
                        metricValueKeyTestAvg, STR_SERIALIZER, OBJ_SERIALIZER, "count", "totalTime");
        assertValues(metricValuesColumnSlice2, "count", 17l, "totalTime", 456854235.123d);

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
