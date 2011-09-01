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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
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
import me.prettyprint.hector.api.query.SuperColumnQuery;

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
    private static final String cluster_name = "Test Cluster";
    private static final String keyspace_name = "TurmericMonitoring";
    private static final String cassandra_node_ip = "127.0.0.1";
    CassandraMetricsStorageProvider provider = null;
    Keyspace kspace = null;
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
                                StringSerializer.get(), LongSerializer.get(), StringSerializer.get());
                multigetSliceQuery.setColumnFamily("MetricTimeSeries");
                multigetSliceQuery.setKeys(keysToRemove.get("MetricTimeSeries"));
                multigetSliceQuery.setRange(0l, Long.MAX_VALUE, false, 3);

                QueryResult<Rows<String, Long, String>> result = multigetSliceQuery.execute();
                Mutator<String> metricTimeSeries = HFactory.createMutator(kspace, StringSerializer.get());
                for (Row<String, Long, String> row : result.get()) {
                    System.out.println("MetricTimeSeries:key to delete=" + row.getKey());
                    metricTimeSeries.delete(row.getKey(), "MetricTimeSeries", null, StringSerializer.get());
                }

            }
            if (keysToRemove.containsKey("MetricIdentifier")) {
                MultigetSliceQuery<String, String, String> multigetSliceQuery = HFactory.createMultigetSliceQuery(
                                kspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
                multigetSliceQuery.setColumnFamily("MetricIdentifier");
                multigetSliceQuery.setKeys(keysToRemove.get("MetricIdentifier"));
                multigetSliceQuery.setRange("", "", false, 3);
                QueryResult<Rows<String, String, String>> result = multigetSliceQuery.execute();
                Mutator<String> metricIdentifier = HFactory.createMutator(kspace, StringSerializer.get());
                for (Row<String, String, String> row : result.get()) {
                    System.out.println("MetricIdentifier:key to delete=" + row.getKey());
                    metricIdentifier.delete(row.getKey(), "MetricIdentifier", null, StringSerializer.get());
                }
            }
            if (keysToRemove.containsKey("ServiceOperationByIp")) {
                MultigetSuperSliceQuery<String, String, String, String> multigetSliceQuery = HFactory
                                .createMultigetSuperSliceQuery(kspace, StringSerializer.get(), StringSerializer.get(),
                                                StringSerializer.get(), StringSerializer.get());
                multigetSliceQuery.setColumnFamily("ServiceOperationByIp");
                multigetSliceQuery.setKeys(keysToRemove.get("ServiceOperationByIp"));
                multigetSliceQuery.setRange("", "", false, 3);
                QueryResult<SuperRows<String, String, String, String>> result = multigetSliceQuery.execute();
                Mutator<String> serviceOperationByIp = HFactory.createMutator(kspace, StringSerializer.get());
                for (SuperRow<String, String, String, String> row : result.get()) {
                    System.out.println("ServiceOperationByIp:key to delete=" + row.getKey());
                    serviceOperationByIp.delete(row.getKey(), "ServiceOperationByIp", null, StringSerializer.get());
                }
            }
            if (keysToRemove.containsKey("ServiceConsumerByIp")) {
                MultigetSuperSliceQuery<String, String, String, String> multigetSliceQuery = HFactory
                                .createMultigetSuperSliceQuery(kspace, StringSerializer.get(), StringSerializer.get(),
                                                StringSerializer.get(), StringSerializer.get());
                multigetSliceQuery.setColumnFamily("ServiceConsumerByIp");
                multigetSliceQuery.setKeys(keysToRemove.get("ServiceConsumerByIp"));
                multigetSliceQuery.setRange("", "", false, 3);
                QueryResult<SuperRows<String, String, String, String>> result = multigetSliceQuery.execute();
                Mutator<String> serviceConsumerByIp = HFactory.createMutator(kspace, StringSerializer.get());
                for (SuperRow<String, String, String, String> row : result.get()) {
                    System.out.println("ServiceConsumerByIp:key to delete=" + row.getKey());
                    serviceConsumerByIp.delete(row.getKey(), "ServiceConsumerByIp", null, StringSerializer.get());
                }
            }
            if (keysToRemove.containsKey("MetricValues")) {
                MultigetSliceQuery<String, String, String> multigetSliceQuery = HFactory.createMultigetSliceQuery(
                                kspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
                multigetSliceQuery.setColumnFamily("MetricValues");
                multigetSliceQuery.setKeys(keysToRemove.get("MetricValues"));
                multigetSliceQuery.setRange("", "", false, 3);
                QueryResult<Rows<String, String, String>> result = multigetSliceQuery.execute();
                Mutator<String> metricIdentifier = HFactory.createMutator(kspace, StringSerializer.get());
                for (Row<String, String, String> row : result.get()) {
                    System.out.println("MetricValues:key to delete=" + row.getKey());
                    metricIdentifier.delete(row.getKey(), "MetricValues", null, StringSerializer.get());
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
        String metricValueKeyTestCount = provider.getIPAddress() + "-" + "test_count" + "-" + timeSnapshot;
        String metricValueKeyTestAvg = provider.getIPAddress() + "-" + "test_average" + "-" + timeSnapshot;
        addToKeyCleanupList("MetricTimeSeries", provider.getIPAddress() + "-" + "service1-operation1-test_count-20");
        addToKeyCleanupList("MetricTimeSeries", provider.getIPAddress() + "-" + "service2-operation2-test_average-20");
        addToKeyCleanupList("MetricIdentifier", "test_count-service1-operation1");
        addToKeyCleanupList("MetricIdentifier", "test_average-service2-operation2");
        addToKeyCleanupList("ServiceOperationByIp", provider.getIPAddress());
        addToKeyCleanupList("ServiceOperationByIp", "All");
        addToKeyCleanupList("ServiceConsumerByIp", "All");
        addToKeyCleanupList("ServiceConsumerByIp", provider.getIPAddress());
        addToKeyCleanupList("MetricValues", metricValueKeyTestCount);
        addToKeyCleanupList("MetricValues", metricValueKeyTestAvg);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);
        System.out.println("Time in ms =" + (System.currentTimeMillis() - timeSnapshot));
        // now I need to retrieve the values. I use Hector for this.
        ColumnSlice<Object, Object> errorColumnSlice = getColumnValues(kspace, "MetricIdentifier",
                        "test_count-service1-operation1", StringSerializer.get(), StringSerializer.get(), "metricName",
                        "serviceAdminName", "operationName");
        assertValues(errorColumnSlice, "metricName", "test_count", "serviceAdminName", "service1", "operationName",
                        "operation1");

        ColumnSlice<Object, Object> errorColumnSlice2 = getColumnValues(kspace, "MetricIdentifier",
                        "test_average-service2-operation2", StringSerializer.get(), StringSerializer.get(),
                        "metricName", "serviceAdminName", "operationName");
        assertValues(errorColumnSlice2, "metricName", "test_average", "serviceAdminName", "service2", "operationName",
                        "operation2");

        HSuperColumn<Object, Object, Object> serviceOperationColumnSlice = getSuperColumnValues(kspace,
                        "ServiceOperationByIp", provider.getIPAddress(), StringSerializer.get(),
                        StringSerializer.get(), StringSerializer.get(), "service1");
        assertColumnValueFromSuperColumn(serviceOperationColumnSlice, "operation1", "");

        HSuperColumn<Object, Object, Object> serviceOperationColumnSlice2 = getSuperColumnValues(kspace,
                        "ServiceOperationByIp", provider.getIPAddress(), StringSerializer.get(),
                        StringSerializer.get(), StringSerializer.get(), "service2");
        assertColumnValueFromSuperColumn(serviceOperationColumnSlice2, "operation2", "");

        // now the consumer cf
        HSuperColumn<Object, Object, Object> serviceConsumerColumnSlice = getSuperColumnValues(kspace,
                        "ServiceConsumerByIp", provider.getIPAddress(), StringSerializer.get(), StringSerializer.get(),
                        StringSerializer.get(), "service1");
        assertColumnValueFromSuperColumn(serviceConsumerColumnSlice, "missing", "");

        // now the consumer cf
        HSuperColumn<Object, Object, Object> serviceConsumerColumnSlice2 = getSuperColumnValues(kspace,
                        "ServiceConsumerByIp", provider.getIPAddress(), StringSerializer.get(), StringSerializer.get(),
                        StringSerializer.get(), "service2");
        assertColumnValueFromSuperColumn(serviceConsumerColumnSlice2, "anotherusecase", "");

        // now I check the MetricTimeSeries

        ColumnSlice<Object, Object> metricTimeSeriesColumnSlice = getColumnValues(kspace, "MetricTimeSeries",
                        provider.getIPAddress() + "-" + "service1-operation1-test_count-20", LongSerializer.get(),
                        StringSerializer.get(), timeSnapshot);
        assertValues(metricTimeSeriesColumnSlice, timeSnapshot, metricValueKeyTestCount);

        ColumnSlice<Object, Object> metricTimeSeriesColumnSlice2 = getColumnValues(kspace, "MetricTimeSeries",
                        provider.getIPAddress() + "-" + "service2-operation2-test_average-20", LongSerializer.get(),
                        StringSerializer.get(), timeSnapshot);
        assertValues(metricTimeSeriesColumnSlice2, timeSnapshot, metricValueKeyTestAvg);

        // now I check the MetricValues
        ColumnSlice<Object, Object> metricValuesColumnSlice = getColumnValues(kspace, "MetricValues",
                        metricValueKeyTestCount, StringSerializer.get(), ObjectSerializer.get(), "value");
        assertValues(metricValuesColumnSlice, "value", 20L);

        // now I check the MetricValues
        ColumnSlice<Object, Object> metricValuesColumnSlice2 = getColumnValues(kspace, "MetricValues",
                        metricValueKeyTestAvg, StringSerializer.get(), ObjectSerializer.get(), "count", "totalTime");
        assertValues(metricValuesColumnSlice2, "count", 2l, "totalTime", 345d);

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
                        + serviceName + "-" + operationName, StringSerializer.get(), StringSerializer.get(),
                        "metricName", "serviceAdminName", "operationName");
        assertValues(errorColumnSlice, "metricName", "test_count", "serviceAdminName", serviceName, "operationName",
                        operationName);

        ColumnSlice<Object, Object> errorColumnSlice2 = getColumnValues(kspace, "MetricIdentifier", "test_average-"
                        + serviceName + "-" + operationName, StringSerializer.get(), StringSerializer.get(),
                        "metricName", "serviceAdminName", "operationName");
        assertValues(errorColumnSlice2, "metricName", "test_average", "serviceAdminName", serviceName, "operationName",
                        operationName);

        HSuperColumn<Object, Object, Object> serviceOperationColumnSlice = getSuperColumnValues(kspace,
                        "ServiceOperationByIp", provider.getIPAddress(), StringSerializer.get(),
                        StringSerializer.get(), StringSerializer.get(), serviceName);
        // assertValues(serviceOperationColumnSlice, "ServiceX", "operationY", "");
        assertColumnValueFromSuperColumn(serviceOperationColumnSlice, operationName, "");

        // now the consumer cf
        HSuperColumn<Object, Object, Object> serviceConsumerColumnSlice = getSuperColumnValues(kspace,
                        "ServiceConsumerByIp", provider.getIPAddress(), StringSerializer.get(), StringSerializer.get(),
                        StringSerializer.get(), serviceName);
        assertColumnValueFromSuperColumn(serviceConsumerColumnSlice, "missing", "");

        // now the consumer cf
        HSuperColumn<Object, Object, Object> serviceConsumerColumnSlice2 = getSuperColumnValues(kspace,
                        "ServiceConsumerByIp", provider.getIPAddress(), StringSerializer.get(), StringSerializer.get(),
                        StringSerializer.get(), serviceName);
        assertColumnValueFromSuperColumn(serviceConsumerColumnSlice2, "anotherusecase", "");

        // now I check the MetricTimeSeries

        ColumnSlice<Object, Object> metricTimeSeriesColumnSlice = getColumnValues(kspace, "MetricTimeSeries",
                        provider.getIPAddress() + "-" + serviceName + "-" + operationName + "-test_count-20",
                        LongSerializer.get(), StringSerializer.get(), timeSnapshot);
        assertValues(metricTimeSeriesColumnSlice, timeSnapshot, metricValueKeyTestCount);

        ColumnSlice<Object, Object> metricTimeSeriesColumnSlice2 = getColumnValues(kspace, "MetricTimeSeries",
                        provider.getIPAddress() + "-" + serviceName + "-" + operationName + "-test_average-20",
                        LongSerializer.get(), StringSerializer.get(), timeSnapshot);
        assertValues(metricTimeSeriesColumnSlice2, timeSnapshot, metricValueKeyTestAvg);

        // now I check the MetricValues
        ColumnSlice<Object, Object> metricValuesColumnSlice = getColumnValues(kspace, "MetricValues",
                        metricValueKeyTestCount, StringSerializer.get(), ObjectSerializer.get(), "value");
        assertValues(metricValuesColumnSlice, "value", 123456l);

        // now I check the MetricValues
        ColumnSlice<Object, Object> metricValuesColumnSlice2 = getColumnValues(kspace, "MetricValues",
                        metricValueKeyTestAvg, StringSerializer.get(), ObjectSerializer.get(), "count", "totalTime");
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
                        + serviceName + "-" + operationName, StringSerializer.get(), StringSerializer.get(),
                        "metricName", "serviceAdminName", "operationName");
        assertValues(errorColumnSlice, "metricName", "test_count", "serviceAdminName", serviceName, "operationName",
                        operationName);

        ColumnSlice<Object, Object> errorColumnSlice2 = getColumnValues(kspace, "MetricIdentifier", "test_average-"
                        + serviceName + "-" + operationName, StringSerializer.get(), StringSerializer.get(),
                        "metricName", "serviceAdminName", "operationName");
        assertValues(errorColumnSlice2, "metricName", "test_average", "serviceAdminName", serviceName, "operationName",
                        operationName);

        HSuperColumn<Object, Object, Object> serviceOperationColumnSlice = getSuperColumnValues(kspace,
                        "ServiceOperationByIp", provider.getIPAddress(), StringSerializer.get(),
                        StringSerializer.get(), StringSerializer.get(), serviceName);
        // assertValues(serviceOperationColumnSlice, "ServiceX", "operationY", "");
        assertColumnValueFromSuperColumn(serviceOperationColumnSlice, operationName, "");

        // now the consumer cf
        HSuperColumn<Object, Object, Object> serviceConsumerColumnSlice = getSuperColumnValues(kspace,
                        "ServiceConsumerByIp", provider.getIPAddress(), StringSerializer.get(), StringSerializer.get(),
                        StringSerializer.get(), serviceName);
        assertColumnValueFromSuperColumn(serviceConsumerColumnSlice, consumerName, "");

        // now I check the MetricTimeSeries

        ColumnSlice<Object, Object> metricTimeSeriesColumnSlice = getColumnValues(kspace, "MetricTimeSeries",
                        provider.getIPAddress() + "-" + serviceName + "-" + operationName + "-test_count-20",
                        LongSerializer.get(), StringSerializer.get(), timeSnapshot);
        assertValues(metricTimeSeriesColumnSlice, timeSnapshot, metricValueKeyTestCount);

        ColumnSlice<Object, Object> metricTimeSeriesColumnSlice2 = getColumnValues(kspace, "MetricTimeSeries",
                        provider.getIPAddress() + "-" + serviceName + "-" + operationName + "-test_average-20",
                        LongSerializer.get(), StringSerializer.get(), timeSnapshot);
        assertValues(metricTimeSeriesColumnSlice2, timeSnapshot, metricValueKeyTestAvg);

        // now I check the MetricValues
        ColumnSlice<Object, Object> metricValuesColumnSlice = getColumnValues(kspace, "MetricValues",
                        metricValueKeyTestCount, StringSerializer.get(), ObjectSerializer.get(), "value");
        assertValues(metricValuesColumnSlice, "value", 123456l);

        // now I check the MetricValues
        ColumnSlice<Object, Object> metricValuesColumnSlice2 = getColumnValues(kspace, "MetricValues",
                        metricValueKeyTestAvg, StringSerializer.get(), ObjectSerializer.get(), "count", "totalTime");
        assertValues(metricValuesColumnSlice2, "count", 17l, "totalTime", 456854235.123d);

    }

    private void assertColumnValueFromSuperColumn(HSuperColumn<Object, Object, Object> serviceOperationColumnSlice,
                    Object columnName, Object columnValue) {
        int i = 0;
        for (HColumn<Object, Object> column : serviceOperationColumnSlice.getColumns()) {
            if (column.getName().equals(columnName)) {
                assertEquals("Expected = " + columnValue + ". Actual = " + column.getValue(), columnValue,
                                column.getValue());
                return;
            }
        }
        fail("No column found with this name = " + columnName);
    }

    private HSuperColumn<Object, Object, Object> getSuperColumnValues(Keyspace kspace, String cfName, Object key,
                    Serializer superColumnNameSerializer, Serializer<?> columnNameSerializer,
                    Serializer valueSerializer, String superColumnName) {
        SuperColumnQuery<Object, Object, Object, Object> superColumnQuery = HFactory.createSuperColumnQuery(kspace,
                        SerializerTypeInferer.getSerializer(key), superColumnNameSerializer, columnNameSerializer,
                        valueSerializer);
        superColumnQuery.setColumnFamily(cfName).setKey(key).setSuperName(superColumnName);
        QueryResult<HSuperColumn<Object, Object, Object>> result = superColumnQuery.execute();
        return result.get();
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
