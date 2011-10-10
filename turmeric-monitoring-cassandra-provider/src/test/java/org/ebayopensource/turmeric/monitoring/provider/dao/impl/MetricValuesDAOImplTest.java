package org.ebayopensource.turmeric.monitoring.provider.dao.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.monitoring.provider.BaseTest;
import org.ebayopensource.turmeric.monitoring.provider.MockInitContext;
import org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceCassandraProviderImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValueAggregatorTestImpl;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.SystemMetricDefs;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricCategory;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricClassifier;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricId;
import org.ebayopensource.turmeric.runtime.common.monitoring.MonitoringLevel;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.AverageMetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

public class MetricValuesDAOImplTest extends BaseTest {

   private final String consumerName = "ConsumerName1";
   private List<CommonErrorData> errorsToStore = null;
   private final long fiftyNineSeconds = TimeUnit.SECONDS.toMillis(59);
   private final long now = System.currentTimeMillis();
   private final long oneMinuteAgo = now - TimeUnit.SECONDS.toMillis(60);
   private final long oneSecond = TimeUnit.SECONDS.toMillis(1);

   private final String opName = "Operation1";
   private final String serverName = "All";
   private final boolean serverSide = true;
   private final long sixMinuteAgo = now - TimeUnit.SECONDS.toMillis(60 * 6);
   private final String srvcAdminName = "ServiceAdminName1";
   private final long twoMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 2);
   private final long threeMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 3);

   private int accumCount = 0;
   private double accumResponse = 0.00;

   @Before
   public void setUp() throws Exception {
      super.setUp();
      metricsStorageProvider = new CassandraMetricsStorageProvider();
      metricsStorageProvider.init(options, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
      queryprovider = new SOAMetricsQueryServiceCassandraProviderImpl();
      cleanUpTestData();
   }

   @After
   public void tearDown() {
      super.tearDown();
      accumCount = 0;
      accumResponse = 0.00;
   }

   @Test
   public void testFindMetricValuesByOperation() throws ServiceException {
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);
      metricsStorageProvider.saveMetricSnapshot(twoMinutesAgo, snapshotCollection);
      
      Collection<MetricValueAggregator> snapshotCollection2 = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);
      
      metricsStorageProvider.saveMetricSnapshot(now, snapshotCollection2);
      Map<String, List<String>> filters = new HashMap<String, List<String>>();
      filters.put("Service", Arrays.asList(srvcAdminName));
      filters.put("Operation", Arrays.asList(opName));
      Map<String, List<org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?>>> result = metricValuesDAO
               .findMetricValuesByOperation(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), threeMinutesAgo,
                        oneMinuteAgo, true, 20, filters);
      assertNotNull(result);
      assertEquals(1, result.size());
      assertTrue(result.keySet().contains(opName));
      List<org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?>> metricValues = result.get(opName);
      assertNotNull(metricValues);
      assertEquals(1, metricValues.size());
      org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?> metricValue = metricValues.get(0);
      assertNotNull(metricValue);
      assertNotNull(metricValue.getColumns());
      assertEquals(2, metricValue.getColumns().size());
      Object count = metricValue.getColumns().get("count");
      assertNotNull(count);
      assertEquals(Long.class, count.getClass());
      Object totalTime = metricValue.getColumns().get("totalTime");
      assertNotNull(totalTime);
      assertEquals(Double.class, totalTime.getClass());

   }

   @Test
   public void testFindMetricValuesByOperationFromThreeMinutesToNow() throws ServiceException {
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);
      metricsStorageProvider.saveMetricSnapshot(twoMinutesAgo, snapshotCollection);

      Collection<MetricValueAggregator> snapshotCollection2 = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);
      metricsStorageProvider.saveMetricSnapshot(oneMinuteAgo, snapshotCollection2);
      Map<String, List<String>> filters = new HashMap<String, List<String>>();
      filters.put("Service", Arrays.asList(srvcAdminName));
      filters.put("Operation", Arrays.asList(opName));
      Map<String, List<org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?>>> result = metricValuesDAO
               .findMetricValuesByOperation(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), threeMinutesAgo, now, true,
                        20, filters);
      assertNotNull(result);
      assertEquals(1, result.size());
      List<org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?>> metricValues = result.get(opName);
      assertNotNull(metricValues);
      assertEquals(2, metricValues.size());
      org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?> metricValue = metricValues.get(0);
      assertNotNull(metricValue);
      assertNotNull(metricValue.getColumns());
      assertEquals(2, metricValue.getColumns().size());
      Object count = metricValue.getColumns().get("count");
      assertNotNull(count);
      assertEquals(Long.class, count.getClass());
      Object totalTime = metricValue.getColumns().get("totalTime");
      assertNotNull(totalTime);
      assertEquals(Double.class, totalTime.getClass());
      org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?> metricValue2 = metricValues.get(1);
      assertNotNull(metricValue2);
      assertNotNull(metricValue2.getColumns());
      assertEquals(2, metricValue2.getColumns().size());
      Object count2 = metricValue2.getColumns().get("count");
      assertNotNull(count2);
      assertEquals(Long.class, count2.getClass());
      Object totalTime2 = metricValue2.getColumns().get("totalTime");
      assertNotNull(totalTime2);
      assertEquals(Double.class, totalTime2.getClass());

   }
   
   @Test
   public void testFindMetricValuesByOperationFromThreeMinutesToOneHourLater() throws ServiceException {
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);
      metricsStorageProvider.saveMetricSnapshot(twoMinutesAgo, snapshotCollection);

      Collection<MetricValueAggregator> snapshotCollection2 = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);
      metricsStorageProvider.saveMetricSnapshot(oneMinuteAgo, snapshotCollection2);
      Map<String, List<String>> filters = new HashMap<String, List<String>>();
      filters.put("Service", Arrays.asList(srvcAdminName));
      filters.put("Operation", Arrays.asList(opName));
      long oneHourLater = now + (60*60*1000);
      Map<String, List<org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?>>> result = metricValuesDAO
               .findMetricValuesByOperation(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), threeMinutesAgo, oneHourLater , true,
                        20, filters);
      assertNotNull(result);
      assertEquals(1, result.size());
      List<org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?>> metricValues = result.get(opName);
      assertNotNull(metricValues);
      assertEquals(2, metricValues.size());
      org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?> metricValue = metricValues.get(0);
      assertNotNull(metricValue);
      assertNotNull(metricValue.getColumns());
      assertEquals(2, metricValue.getColumns().size());
      Object count = metricValue.getColumns().get("count");
      assertNotNull(count);
      assertEquals(Long.class, count.getClass());
      Object totalTime = metricValue.getColumns().get("totalTime");
      assertNotNull(totalTime);
      assertEquals(Double.class, totalTime.getClass());
      org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?> metricValue2 = metricValues.get(1);
      assertNotNull(metricValue2);
      assertNotNull(metricValue2.getColumns());
      assertEquals(2, metricValue2.getColumns().size());
      Object count2 = metricValue2.getColumns().get("count");
      assertNotNull(count2);
      assertEquals(Long.class, count2.getClass());
      Object totalTime2 = metricValue2.getColumns().get("totalTime");
      assertNotNull(totalTime2);
      assertEquals(Double.class, totalTime2.getClass());

   }

   @Test
   public void testFindMetricValuesByOperationFromOneMinuteToNow() throws ServiceException {
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);
      Collection<MetricValueAggregator> snapshotCollection2 = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);
      Collection<MetricValueAggregator> snapshotCollection3 = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);

      metricsStorageProvider.saveMetricSnapshot(twoMinutesAgo, snapshotCollection);
      metricsStorageProvider.saveMetricSnapshot(oneMinuteAgo, snapshotCollection2);
      metricsStorageProvider.saveMetricSnapshot(now, snapshotCollection3);

      Map<String, List<String>> filters = new HashMap<String, List<String>>();
      filters.put("Service", Arrays.asList(srvcAdminName));
      filters.put("Operation", Arrays.asList(opName));
      Map<String, List<org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?>>> result = metricValuesDAO
               .findMetricValuesByOperation(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), oneMinuteAgo, now, true,
                        20, filters);
      assertNotNull(result);
      assertEquals(1, result.size());
      List<org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?>> metricValues = result.get(opName);
      assertNotNull(metricValues);
      assertEquals(2, metricValues.size());
      org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?> metricValue = metricValues.get(0);
      assertNotNull(metricValue);
      assertNotNull(metricValue.getColumns());
      assertEquals(2, metricValue.getColumns().size());
      Object count = metricValue.getColumns().get("count");
      assertNotNull(count);
      assertEquals(Long.class, count.getClass());
      Object totalTime = metricValue.getColumns().get("totalTime");
      assertNotNull(totalTime);
      assertEquals(Double.class, totalTime.getClass());
      org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?> metricValue2 = metricValues.get(1);
      assertNotNull(metricValue2);
      assertNotNull(metricValue2.getColumns());
      assertEquals(2, metricValue2.getColumns().size());
      Object count2 = metricValue2.getColumns().get("count");
      assertNotNull(count2);
      assertEquals(Long.class, count.getClass());
      Object totalTime2 = metricValue2.getColumns().get("totalTime");
      assertNotNull(totalTime2);
      assertEquals(Double.class, totalTime.getClass());
   }

   protected Collection<MetricValueAggregator> createMetricValueAggregatorsForOneConsumerWithTotalMetric(
            String serviceName, String operationName, String consumerName) {
      accumCount += 1;
      accumResponse += 1234.00;
      Collection<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
      MetricId metricId1 = new MetricId(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), serviceName, operationName);
      MetricValue metricValue1 = new AverageMetricValue(metricId1, accumCount, accumResponse);
      MetricClassifier metricClassifier1 = new MetricClassifier(consumerName, "sourcedc", "targetdc");

      Map<MetricClassifier, MetricValue> valuesByClassifier1 = new HashMap<MetricClassifier, MetricValue>();
      valuesByClassifier1.put(metricClassifier1, metricValue1);

      MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1,
               MetricCategory.Timing, MonitoringLevel.NORMAL, valuesByClassifier1);

      result.add(aggregator1);

      return result;
   }

}