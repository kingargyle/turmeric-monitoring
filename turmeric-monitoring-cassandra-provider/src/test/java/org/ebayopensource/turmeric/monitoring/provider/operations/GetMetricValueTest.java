package org.ebayopensource.turmeric.monitoring.provider.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.monitoring.provider.BaseTest;
import org.ebayopensource.turmeric.monitoring.provider.MockInitContext;
import org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceCassandraProviderImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValueAggregatorTestImpl;
import org.ebayopensource.turmeric.monitoring.v1.services.CriteriaInfo;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGraphData;
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
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetMetricValueTest extends BaseTest {

   private int accumCount = 0;
   private double accumResponse = 0;
   private final long now = System.currentTimeMillis();
   private final String consumerName = "ConsumerName1";
   private final List<CommonErrorData> errorsToStore = null;

   private final long oneMinuteAgo = now - TimeUnit.SECONDS.toMillis(60);
   private final String opName = "Operation1";
   private final long sixMinuteAgo = now - TimeUnit.SECONDS.toMillis(60 * 6);
   private final String srvcAdminName = "ServiceAdminName1";
   private final long threeMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 3);
   private final long twoMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 2);

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

      return deepCopyAggregators(aggregator1);

   }

   protected Collection<MetricValueAggregator> createMetricValueAggregatorsWithTotalMetric(String serviceName,
            String operationName, String consumerName) {

      Collection<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
      MetricId metricId1 = new MetricId(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), serviceName, operationName);
      MetricValue metricValue1 = new AverageMetricValue(metricId1, 1, 1234.0d);
      MetricClassifier metricClassifier1 = new MetricClassifier(consumerName, "sourcedc", "targetdc");

      Map<MetricClassifier, MetricValue> valuesByClassifier1 = new HashMap<MetricClassifier, MetricValue>();
      valuesByClassifier1.put(metricClassifier1, metricValue1);

      MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1,
               MetricCategory.Timing, MonitoringLevel.NORMAL, valuesByClassifier1);

      result.add(aggregator1);

      return result;
   }

   private List<CommonErrorData> createTestCommonErrorDataList(int errorQuantity) {
      List<CommonErrorData> commonErrorDataList = new ArrayList<CommonErrorData>();
      for (int i = 0; i < errorQuantity; i++) {
         CommonErrorData e = new CommonErrorData();
         e.setCategory(ErrorCategory.APPLICATION);
         e.setSeverity(ErrorSeverity.ERROR);
         e.setCause("TestCause");
         e.setDomain("TestDomain");
         e.setSubdomain("TestSubdomain");
         e.setErrorName("TestErrorName");
         e.setErrorId(Long.valueOf(i));
         e.setMessage("Error Message " + i);
         e.setOrganization("TestOrganization");
         commonErrorDataList.add(e);
      }
      return commonErrorDataList;

   }

   public void createTestData() throws ServiceException {
      MetricId metricId1 = new MetricId(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), srvcAdminName, opName);
      MetricValue metricValue1 = new AverageMetricValue(metricId1);
      MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      MetricClassifier metricClassifier1 = new MetricClassifier(consumerName, "sourceDC1", "targetDC1");
      MetricClassifier metricClassifier2 = new MetricClassifier("anotherConsumer", "sourceDC1", "targetDC1");
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier2, 1L);

      List<MetricValueAggregator> aggregators = deepCopyAggregators(aggregator1);
      metricsStorageProvider.saveMetricSnapshot(sixMinuteAgo, aggregators);

      // // now, the second call
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier2, 1L);
      aggregators = deepCopyAggregators(aggregator1);
      metricsStorageProvider.saveMetricSnapshot(threeMinutesAgo, aggregators);
      //
      // // now, at the third minute I do 5 calls for consumerName, 1 from "anotherConsumer"
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier2, 1L);
      // aggregator2.update(metricClassifier2, 17L);
      aggregators = deepCopyAggregators(aggregator1);
      metricsStorageProvider.saveMetricSnapshot(twoMinutesAgo, aggregators);
      //
      // // now, 4th call, 2 calls are made from consumerName, 1 from "anotherConsumer"
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier2, 1L);
      // aggregator2.update(metricClassifier2, 1L);
      aggregators = deepCopyAggregators(aggregator1);
      metricsStorageProvider.saveMetricSnapshot(oneMinuteAgo, aggregators);
   }

   private List<MetricValueAggregator> deepCopyAggregators(MetricValueAggregator... aggregators) {
      // The aggregator list passed to the storage provider is always a deep copy of the aggregators
      List<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
      for (MetricValueAggregator aggregator : aggregators) {
         result.add((MetricValueAggregator) aggregator.deepCopy(false));
      }
      return result;
   }

   @Override
   @Before
   public void setUp() throws Exception {
      super.setUp();
      errorStorageProvider = new CassandraErrorLoggingHandler();
      metricsStorageProvider = new CassandraMetricsStorageProvider();
      InitContext ctx = new MockInitContext(options);
      errorStorageProvider.init(ctx);
      metricsStorageProvider.init(options, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
      queryprovider = new SOAMetricsQueryServiceCassandraProviderImpl();
      createTestData();
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
      accumCount = 0;
      accumResponse = 0;
   }

   @Test
   public void testGetMetricValueCallCountMetricForOneServiceNoOperationOneConsumer() throws ServiceException {// validated
      long duration = 60 * 7;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 9;
      CriteriaInfo criteriaInfo = new CriteriaInfo();
      criteriaInfo.setMetricName("CallCount");
      criteriaInfo.setServiceName(srvcAdminName);
      criteriaInfo.setRoleType("server");
      criteriaInfo.setConsumerName(consumerName);
      List<MetricGraphData> response = queryprovider.getMetricValue(criteriaInfo, sixMinuteAgo, duration,
               aggregationPeriod, "false");
      assertNotNull(response);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, response.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : response) {
         sum += metricGraphData.getCount();
      }
      assertEquals("The sum of the MetricGraphData collection must be " + expectedSum, expectedSum, sum);
   }

   @Test
   public void testGetMetricValueCallCountMetricForOneServiceNoOperationNoConsumer() throws ServiceException {// validated

      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 13;
      CriteriaInfo criteriaInfo = new CriteriaInfo();
      criteriaInfo.setMetricName("CallCount");
      criteriaInfo.setServiceName(srvcAdminName);
      criteriaInfo.setRoleType("server");
      List<MetricGraphData> response = queryprovider.getMetricValue(criteriaInfo, sixMinuteAgo, duration,
               aggregationPeriod, "false");
      assertNotNull(response);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, response.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : response) {
         sum += metricGraphData.getCount();
      }

      assertEquals("The sum of the MetricGraphData collection must be " + expectedSum, expectedSum, sum);
   }

   @Test
   public void testGetMetricValueCallCountMetricForOneServiceNoOperationAnotherConsumer() throws ServiceException {// validated

      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 4;
      CriteriaInfo criteriaInfo = new CriteriaInfo();
      criteriaInfo.setMetricName("CallCount");
      criteriaInfo.setServiceName(srvcAdminName);
      criteriaInfo.setRoleType("server");
      criteriaInfo.setConsumerName("anotherConsumer");
      List<MetricGraphData> response = queryprovider.getMetricValue(criteriaInfo, sixMinuteAgo, duration,
               aggregationPeriod, "false");
      assertNotNull(response);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, response.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : response) {
         sum += metricGraphData.getCount();
      }

      assertEquals("The sum of the MetricGraphData collection must be " + expectedSum, expectedSum, sum);
   }

   @Test
   public void testGetMetricValueCallCountMetricOneServiceOneOperationOneConsumer() throws ServiceException {// validated

      long duration = 60 * 3;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 8;
      CriteriaInfo criteriaInfo = new CriteriaInfo();
      criteriaInfo.setMetricName("CallCount");
      criteriaInfo.setServiceName(srvcAdminName);
      criteriaInfo.setRoleType("server");
      criteriaInfo.setConsumerName(consumerName);
      criteriaInfo.setOperationName(opName);
      List<MetricGraphData> response = queryprovider.getMetricValue(criteriaInfo, threeMinutesAgo, duration,
               aggregationPeriod, "false");
      assertNotNull(response);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, response.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : response) {
         sum += metricGraphData.getCount();
      }

      assertEquals("The sum of the MetricGraphData collection must be " + expectedSum, expectedSum, sum);
   }

}
