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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.OrderedSuperRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.SuperRow;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.RangeSuperSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
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

public class CassandraMetricsStorageProviderTest extends CassandraTestHelper {
   public boolean isEmbedded() {
      return embedded;
   }

   public void setEmbedded(boolean embedded) {
      this.embedded = embedded;
   }

   CassandraMetricsStorageProvider provider = null;
   protected boolean embedded = true;

   private void cleanUpTestData() {
      String[] columnFamilies = { "MetricIdentifier", "MetricValues" };
      String[] superColumnFamilies = { "MetricTimeSeries", "ServiceCallsByTime", "ServiceConsumerByIp",
               "ServiceOperationByIp" };

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

   private Map<String, String> createOptions() {
      Map<String, String> options = new HashMap<String, String>();
      options.put("host-address", cassandra_node_ip);
      options.put("keyspace-name", keyspace_name);
      options.put("cluster-name", cluster_name);
      options.put("storeServiceMetrics", "false");
      options.put("embedded", Boolean.valueOf(embedded).toString());
      return options;
   }

   private List<MetricValueAggregator> deepCopyAggregators(MetricValueAggregator... aggregators) {
      // The aggregator list passed to the storage provider is always a deep copy of the aggregators
      List<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
      for (MetricValueAggregator aggregator : aggregators) {
         result.add((MetricValueAggregator) aggregator.deepCopy(false));
      }
      return result;
   }

   private boolean existsMetricValuesSuperRowKey(String columnFamilyName, String key) {
      SliceQuery<String, String, Object> query = HFactory.createSliceQuery(kspace, StringSerializer.get(),
               StringSerializer.get(), ObjectSerializer.get());

      QueryResult<ColumnSlice<String, Object>> result = query.setColumnFamily(columnFamilyName).setKey(key)
      // .setColumnNames(allColumnNames).execute();

               .setRange("", "", false, 10).execute();
      return !result.get().getColumns().isEmpty();
   }

   @Before
   public void setUp() throws TTransportException, IOException, InterruptedException, ConfigurationException {
      if (embedded) {
         initialize();
      }
      provider = new CassandraMetricsStorageProvider();
      kspace = new HectorManager().getKeyspace(cluster_name, cassandra_node_ip, keyspace_name, "MetricIdentifier",
               false, String.class, String.class);
      Map<String, String> options = createOptions();
      provider.init(options, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
   }

   @After
   public void tearDown() {
      cleanUpTestData();
      provider = null;
      kspace = null;
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
               + metricId.getAdminName() + CassandraMetricsStorageProvider.KEY_SEPARATOR + metricId.getOperationName()
               + "|true";
      String actualKey = provider.getKeyfromMetricId(metricId, true);
      assertEquals(expectedKey, actualKey);
   }

   @Test
   public void testInit() {
      assertFalse(provider.isStoreServiceMetrics());
      assertEquals(20, provider.getSnapshotInterval());
   }

   @Test
   public void testSaveIpByDateAndOperationName() throws ServiceException {
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection();
      long timeSnapshot = System.currentTimeMillis();
      String ipByDateAndOperationNameKey = new SimpleDateFormat("ddMMyyyy").format(System.currentTimeMillis());

      provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

      assertCassandraSuperColumnValues("IpPerDayAndServiceName", ipByDateAndOperationNameKey, "service1",
               STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, new String[] { provider.getIPAddress() },
               new String[] { "" });

   }

   @Test
   public void testSaveMetricIdentifier() throws ServiceException {
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection();
      long timeSnapshot = System.currentTimeMillis();
      String[] metricIdentifierColumns = new String[] { "metricName", "serviceAdminName", "operationName" };

      provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

      assertCassandraColumnValues("MetricIdentifier", "test_count|service1|operation1|true", STR_SERIALIZER,
               STR_SERIALIZER, metricIdentifierColumns, new String[] { "test_count", "service1", "operation1" });

      assertCassandraColumnValues("MetricIdentifier", "test_average|service2|operation2|true", STR_SERIALIZER,
               STR_SERIALIZER, metricIdentifierColumns, new String[] { "test_average", "service2", "operation2" });

   }

   @Test
   public void testSaveMetricTimeSeries() throws ServiceException {
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection();
      long timeSnapshot = System.currentTimeMillis();
      String ipAddress = provider.getIPAddress();
      String metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "service1"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + "operation1"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + "missing"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_count"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
      String metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "service2"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + "operation2"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + "anotherusecase"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_average"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
      String metricSeriesKeyForTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR
               + "service1|operation1|test_count|20|true";
      String metricSeriesKeyForTestCountByConsumer = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR
               + "service1|operation1|missing|test_count|20|true";
      String metricSeriesKeyForTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR
               + "service2|operation2|test_average|20|true";
      String metricSeriesKeyForTestAvgByConsumer = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR
               + "service2|operation2|anotherusecase|test_average|20|true";

      provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

      assertCassandraSuperColumnValues("MetricTimeSeries", metricSeriesKeyForTestCount, timeSnapshot, LONG_SERIALIZER,
               STR_SERIALIZER, STR_SERIALIZER, new String[] { metricValueKeyTestCount }, new String[] { "" });

      assertCassandraSuperColumnValues("MetricTimeSeries", metricSeriesKeyForTestCountByConsumer, timeSnapshot,
               LONG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, new String[] { metricValueKeyTestCount },
               new String[] { "" });

      assertCassandraSuperColumnValues("MetricTimeSeries", metricSeriesKeyForTestAvg, timeSnapshot, LONG_SERIALIZER,
               STR_SERIALIZER, STR_SERIALIZER, new String[] { metricValueKeyTestAvg }, new String[] { "" });

      assertCassandraSuperColumnValues("MetricTimeSeries", metricSeriesKeyForTestAvgByConsumer, timeSnapshot,
               LONG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, new String[] { metricValueKeyTestAvg },
               new String[] { "" });

   }

   @Test
   public void testSaveMetricValues() throws ServiceException {
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection();
      long timeSnapshot = System.currentTimeMillis();
      String ipAddress = provider.getIPAddress();
      String metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "service1"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + "operation1"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + "missing"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_count"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
      String metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "service2"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + "operation2"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + "anotherusecase"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_average"
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;

      provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

      assertCassandraColumnValues("MetricValues", metricValueKeyTestCount, STR_SERIALIZER, OBJ_SERIALIZER,
               new String[] { "value" }, new Long[] { 20L });

      assertCassandraColumnValues("MetricValues", metricValueKeyTestAvg, STR_SERIALIZER, OBJ_SERIALIZER, new String[] {
               "count", "totalTime" }, new Object[] { 2L, 345d });

   }

   @Test
   public void testSaveServiceConsumerByIp() throws ServiceException {
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection();
      long timeSnapshot = System.currentTimeMillis();
      String ipAddress = provider.getIPAddress();
      provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

      assertCassandraSuperColumnValues("ServiceConsumerByIp", ipAddress, "service1", STR_SERIALIZER, STR_SERIALIZER,
               STR_SERIALIZER, new String[] { "missing" }, new String[] { "" });

      assertCassandraSuperColumnValues("ServiceConsumerByIp", ipAddress, "service2", STR_SERIALIZER, STR_SERIALIZER,
               STR_SERIALIZER, new String[] { "anotherusecase" }, new String[] { "" });

   }

   @Test
   public void testSaveServiceOperationByIp() throws ServiceException {
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection();
      long timeSnapshot = System.currentTimeMillis();
      String ipAddress = provider.getIPAddress();

      provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

      assertCassandraSuperColumnValues("ServiceOperationByIp", ipAddress, "service1", STR_SERIALIZER, STR_SERIALIZER,
               STR_SERIALIZER, new String[] { "operation1" }, new String[] { "" });

      assertCassandraSuperColumnValues("ServiceOperationByIp", ipAddress, "service2", STR_SERIALIZER, STR_SERIALIZER,
               STR_SERIALIZER, new String[] { "operation2" }, new String[] { "" });

   }

   @Test
   public void testServiceCallsByTime() throws ServiceException {
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection();
      long timeSnapshot = System.currentTimeMillis();
      String ipAddress = provider.getIPAddress();

      String serviceCallsByTimeKey = ipAddress + "|service1|true";
      String serviceCallsByTimeKeySrv2 = ipAddress + "|service2|true";

      provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

      assertCassandraSuperColumnValues("ServiceCallsByTime", serviceCallsByTimeKey, Long.valueOf(timeSnapshot),
               LONG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, new String[] { "operation1" }, new String[] { "" });

      assertCassandraSuperColumnValues("ServiceCallsByTime", serviceCallsByTimeKeySrv2, Long.valueOf(timeSnapshot),
               LONG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, new String[] { "operation2" }, new String[] { "" });
   }

   @Test
   public void testSnapshotsAreSubtracted() throws Exception {
      long timeStamp = System.currentTimeMillis();
      String metricName1 = "test_count";
      String serviceName1 = "service1";
      String operationName1 = "operation1";

      String metricName2 = "test_average";
      String serviceName2 = "service2";
      String operationName2 = "operation2";

      String consumerName = "consumer1";
      String ipAddress = provider.getIPAddress();

      MetricId metricId1 = new MetricId(metricName1, serviceName1, operationName1);
      MetricValue metricValue1 = new LongSumMetricValue(metricId1);
      MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      MetricId metricId2 = new MetricId(metricName2, serviceName2, operationName2);
      MetricValue metricValue2 = new AverageMetricValue(metricId2);
      MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      // Simulate first call
      MetricClassifier metricClassifier1 = new MetricClassifier(consumerName, "sourceDC1", "targetDC1");
      aggregator1.update(metricClassifier1, 2L);
      aggregator2.update(metricClassifier1, 4L);

      List<MetricValueAggregator> aggregators = deepCopyAggregators(aggregator1, aggregator2);

      // First save
      provider.saveMetricSnapshot(timeStamp, aggregators);

      String metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + serviceName1
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName1
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + consumerName
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + metricName1
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeStamp;
      String metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + serviceName2
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName2
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + consumerName
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + metricName2
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeStamp;

      assertCassandraColumnValues("MetricValues", metricValueKeyTestCount, STR_SERIALIZER, OBJ_SERIALIZER,
               new String[] { "value" }, new Object[] { 2l });

      assertCassandraColumnValues("MetricValues", metricValueKeyTestAvg, STR_SERIALIZER, OBJ_SERIALIZER, new String[] {
               "count", "totalTime" }, new Object[] { 1l, 4d });

      // Simulate second call
      long countDelta = 1L;
      long timeDelta = 5L;
      aggregator1.update(metricClassifier1, countDelta);
      aggregator2.update(metricClassifier1, timeDelta);

      // Second save
      timeStamp += TimeUnit.SECONDS.toMillis(60);
      aggregators = deepCopyAggregators(aggregator1, aggregator2);
      provider.saveMetricSnapshot(timeStamp, aggregators);

      metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + serviceName1
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName1
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + consumerName
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + metricName1
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeStamp;
      metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + serviceName2
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName2
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + consumerName
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + metricName2
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeStamp;

      assertCassandraColumnValues("MetricValues", metricValueKeyTestCount, STR_SERIALIZER, OBJ_SERIALIZER,
               new String[] { "value" }, new Object[] { countDelta });

      assertCassandraColumnValues("MetricValues", metricValueKeyTestAvg, STR_SERIALIZER, OBJ_SERIALIZER, new String[] {
               "count", "totalTime" }, new Object[] { countDelta, (double) timeDelta });

   }

   @Test
   public void testZeroValuesAreNotSaved() throws Exception {
      long timeStamp = System.currentTimeMillis();
      String metricName1 = "test_count";
      String serviceName1 = "service1";
      String operationName1 = "operation1";

      String metricName2 = "test_average";
      String serviceName2 = "service2";
      String operationName2 = "operation2";

      String consumerName = "consumer1";
      String ipAddress = provider.getIPAddress();

      MetricId metricId1 = new MetricId(metricName1, serviceName1, operationName1);
      MetricValue metricValue1 = new LongSumMetricValue(metricId1);
      MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      MetricId metricId2 = new MetricId(metricName2, serviceName2, operationName2);
      MetricValue metricValue2 = new AverageMetricValue(metricId2);
      MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      // Simulate first call
      MetricClassifier metricClassifier1 = new MetricClassifier(consumerName, "sourceDC1", "targetDC1");
      aggregator1.update(metricClassifier1, 2L);
      aggregator2.update(metricClassifier1, 4L);

      List<MetricValueAggregator> aggregators = deepCopyAggregators(aggregator1, aggregator2);

      // First save
      provider.saveMetricSnapshot(timeStamp, aggregators);

      String metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + serviceName1
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName1
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + consumerName
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + metricName1
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeStamp;
      String metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + serviceName2
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName2
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + consumerName
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + metricName2
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeStamp;

      boolean existsTestCountRowFirstSave = existsMetricValuesSuperRowKey("MetricValues", metricValueKeyTestCount);

      assertTrue("There should be a row stored.", existsTestCountRowFirstSave);

      boolean existsTestAvgRowFirstSave = existsMetricValuesSuperRowKey("MetricValues", metricValueKeyTestCount);

      assertTrue("There should be a row stored.", existsTestAvgRowFirstSave);

      assertCassandraColumnValues("MetricValues", metricValueKeyTestCount, STR_SERIALIZER, OBJ_SERIALIZER,
               new String[] { "value" }, new Object[] { 2l });

      assertCassandraColumnValues("MetricValues", metricValueKeyTestAvg, STR_SERIALIZER, OBJ_SERIALIZER, new String[] {
               "count", "totalTime" }, new Object[] { 1l, 4d });

      // nothing happens but the timer asks to save the metrics again
      // Second save
      timeStamp += TimeUnit.SECONDS.toMillis(60);
      aggregators = deepCopyAggregators(aggregator1, aggregator2);
      provider.saveMetricSnapshot(timeStamp, aggregators);

      metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + serviceName1
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName1
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + consumerName
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + metricName1
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeStamp;
      metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + serviceName2
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName2
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + consumerName
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + metricName2
               + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeStamp;

      boolean existsTestCountRow = existsMetricValuesSuperRowKey("MetricValues", metricValueKeyTestCount);

      assertFalse("The should be no row stored.", existsTestCountRow);

      boolean existsTestAvgRow = existsMetricValuesSuperRowKey("MetricValues", metricValueKeyTestAvg);

      assertFalse("The should be no row stored.", existsTestAvgRow);
   }
}
