package org.ebayopensource.turmeric.monitoring.provider;

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
import org.junit.Ignore;
import org.junit.Test;

public class GetMetricValueTest extends BaseTest {

   private int accumCount = 0;
   private double accumResponse = 0;
   private final long now = System.currentTimeMillis();
   private final String consumerName = "ConsumerName1";
   private List<CommonErrorData> errorsToStore = null;
   private final long fiftyNineSeconds = TimeUnit.SECONDS.toMillis(59);
   private final long fourMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 4);

   private final long oneMinuteAgo = now - TimeUnit.SECONDS.toMillis(60);
   private final long oneSecond = TimeUnit.SECONDS.toMillis(1);
   private final String opName = "Operation1";
   private final String serverName = "All";
   private final boolean serverSide = true;
   private final long sixMinuteAgo = now - TimeUnit.SECONDS.toMillis(60 * 6);
   private final String srvcAdminName = "ServiceAdminName1";
   private final long threeMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 3);
   private final long twoMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 2);

   /**
    * Creates the data.
    * 
    * @param time
    */
   private void createData() {
      errorsToStore = createTestCommonErrorDataList(1);
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

      cleanUpTestData();
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
      accumCount = 0;
      accumResponse = 0;

   }

   @Test
   public void testGetMetricValueCallCountMetricForOneService() throws ServiceException {
      createData();
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);
      Collection<MetricValueAggregator> snapshotCollection2 = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);

      metricsStorageProvider.saveMetricSnapshot(twoMinutesAgo, snapshotCollection);
      metricsStorageProvider.saveMetricSnapshot(oneMinuteAgo, snapshotCollection2);

      long duration = 120;// in secs
      int aggregationPeriod = 20;// in secs
      CriteriaInfo criteriaInfo = new CriteriaInfo();
      criteriaInfo.setMetricName("CallCount");
      criteriaInfo.setServiceName(srvcAdminName);
      criteriaInfo.setRoleType("server");
      List<MetricGraphData> response = queryprovider.getMetricValue(criteriaInfo, twoMinutesAgo, duration,
               aggregationPeriod, "false");
      assertNotNull(response);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, response.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : response) {
         sum += metricGraphData.getCount();
      }
      assertEquals("The sum of the MetricGraphData collection must be 2", 2, sum);
   }

   @Test
   public void testGetMetricValueCallCountMetricForOneServiceOneConsumer() throws ServiceException {
      createData();
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);
      Collection<MetricValueAggregator> snapshotCollection2 = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);

      metricsStorageProvider.saveMetricSnapshot(twoMinutesAgo, snapshotCollection);
      metricsStorageProvider.saveMetricSnapshot(oneMinuteAgo, snapshotCollection2);

      long duration = 120;// in secs
      int aggregationPeriod = 20;// in secs
      CriteriaInfo criteriaInfo = new CriteriaInfo();
      criteriaInfo.setMetricName("CallCount");
      criteriaInfo.setServiceName(srvcAdminName);
      criteriaInfo.setRoleType("server");
      criteriaInfo.setConsumerName(consumerName);
      List<MetricGraphData> response = queryprovider.getMetricValue(criteriaInfo, twoMinutesAgo, duration,
               aggregationPeriod, "false");
      assertNotNull(response);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, response.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : response) {
         sum += metricGraphData.getCount();
      }
      assertEquals("The sum of the MetricGraphData collection must be 2", 2, sum);
   }

   @Ignore
   @Test
   public void testGetMetricValueCallCountMetricForOneServiceThreeOperationsTwoConsumers() throws ServiceException {
      createData();
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsWithTotalMetric(srvcAdminName,
               opName, consumerName);
      Collection<MetricValueAggregator> snapshotCollection2 = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, "anotherOperation", "anotherConsumer");
      Collection<MetricValueAggregator> snapshotCollection3 = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, "anotherOperation2", "anotherConsumer");
      Collection<MetricValueAggregator> snapshotCollection4 = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, "anotherOperation2", "anotherConsumer");

      metricsStorageProvider.saveMetricSnapshot(sixMinuteAgo, snapshotCollection);
      metricsStorageProvider.saveMetricSnapshot(threeMinutesAgo, snapshotCollection2);
      metricsStorageProvider.saveMetricSnapshot(twoMinutesAgo, snapshotCollection3);
      metricsStorageProvider.saveMetricSnapshot(oneMinuteAgo, snapshotCollection4);

      long duration = 120;// in secs
      int aggregationPeriod = 20;// in secs
      CriteriaInfo criteriaInfo = new CriteriaInfo();
      criteriaInfo.setMetricName("CallCount");
      criteriaInfo.setServiceName(srvcAdminName);
      criteriaInfo.setRoleType("server");
      criteriaInfo.setConsumerName("anotherConsumer");
      List<MetricGraphData> response = queryprovider.getMetricValue(criteriaInfo, twoMinutesAgo, duration,
               aggregationPeriod, "false");
      assertNotNull(response);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, response.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : response) {
         sum += metricGraphData.getCount();
      }
      assertEquals("The sum of the MetricGraphData collection must be 2", 2, sum);
   }

   @Test
   public void testGetMetricValueCallCountMetricForOneServiceTwoConsumers() throws ServiceException {
      createData();
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsWithTotalMetric(srvcAdminName,
               opName, consumerName);
      Collection<MetricValueAggregator> snapshotCollection2 = createMetricValueAggregatorsWithTotalMetric(
               srvcAdminName, "anotherOperation", "anotherConsumer");

      metricsStorageProvider.saveMetricSnapshot(oneMinuteAgo, snapshotCollection);
      metricsStorageProvider.saveMetricSnapshot(oneMinuteAgo, snapshotCollection2);

      long duration = 120;// in secs
      int aggregationPeriod = 20;// in secs
      CriteriaInfo criteriaInfo = new CriteriaInfo();
      criteriaInfo.setMetricName("CallCount");
      criteriaInfo.setServiceName(srvcAdminName);
      criteriaInfo.setRoleType("server");
      criteriaInfo.setConsumerName("anotherConsumer");
      List<MetricGraphData> response = queryprovider.getMetricValue(criteriaInfo, twoMinutesAgo, duration,
               aggregationPeriod, "false");
      assertNotNull(response);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, response.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : response) {
         sum += metricGraphData.getCount();
      }
      assertEquals("The sum of the MetricGraphData collection must be 1", 1, sum);
   }

}
