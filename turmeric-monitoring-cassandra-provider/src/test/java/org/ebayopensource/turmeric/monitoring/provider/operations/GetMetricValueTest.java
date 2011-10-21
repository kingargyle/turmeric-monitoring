package org.ebayopensource.turmeric.monitoring.provider.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

   private long now = 0;
   private String consumerName = null;

   private long oneMinuteAgo = 0;
   private String opName = null;
   private long sixMinutesAgo = 0;
   private String srvcAdminName = null;
   private long threeMinutesAgo = 0;
   private long twoMinutesAgo = 0;

   public void createTestData() throws ServiceException {
      MetricId metricId1 = new MetricId(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), srvcAdminName, opName);
      MetricValue metricValue1 = new AverageMetricValue(metricId1);

      MetricId metricId2 = new MetricId(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), srvcAdminName,
               "anotherOperation");
      MetricValue metricValue2 = new AverageMetricValue(metricId2);
      MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      MetricClassifier metricClassifier1 = new MetricClassifier(consumerName, "sourceDC1", "targetDC1");
      MetricClassifier metricClassifier2 = new MetricClassifier("anotherConsumer", "sourceDC1", "targetDC1");
      aggregator1.update(metricClassifier1, 1L);
      aggregator2.update(metricClassifier1, 1l);
      aggregator2.update(metricClassifier1, 1l);
      aggregator1.update(metricClassifier2, 1L);

      List<MetricValueAggregator> aggregators = deepCopyAggregators(aggregator1, aggregator2);
      metricsStorageProvider.saveMetricSnapshot(sixMinutesAgo, aggregators);

      // // now, the second call
      aggregator1.update(metricClassifier1, 1L);
      aggregator2.update(metricClassifier1, 1l);
      aggregator1.update(metricClassifier2, 1L);
      aggregators = deepCopyAggregators(aggregator1, aggregator2);
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

      now = System.currentTimeMillis();
      consumerName = "ConsumerName1";

      oneMinuteAgo = now - TimeUnit.SECONDS.toMillis(60);
      opName = "Operation1";
      sixMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 6);
      srvcAdminName = "ServiceAdminName1";
      threeMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 3);
      twoMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 2);

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
   }

   @Test
   public void testCallCountOneServiceNoOperationOneConsumer() throws ServiceException {// validated
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 12;
      CriteriaInfo criteriaInfo = new CriteriaInfo();
      criteriaInfo.setMetricName("CallCount");
      criteriaInfo.setServiceName(srvcAdminName);
      criteriaInfo.setRoleType("server");
      criteriaInfo.setConsumerName(consumerName);
      List<MetricGraphData> response = queryprovider.getMetricValue(criteriaInfo, sixMinutesAgo, duration,
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
   public void testCallCountOneServiceNoOperationNoConsumer() throws ServiceException {// validated

      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 16;
      CriteriaInfo criteriaInfo = new CriteriaInfo();
      criteriaInfo.setMetricName("CallCount");
      criteriaInfo.setServiceName(srvcAdminName);
      criteriaInfo.setRoleType("server");
      List<MetricGraphData> response = queryprovider.getMetricValue(criteriaInfo, sixMinutesAgo, duration,
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
   public void testCallCountOneServiceNoOperationAnotherConsumer() throws ServiceException {// validated

      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 4;
      CriteriaInfo criteriaInfo = new CriteriaInfo();
      criteriaInfo.setMetricName("CallCount");
      criteriaInfo.setServiceName(srvcAdminName);
      criteriaInfo.setRoleType("server");
      criteriaInfo.setConsumerName("anotherConsumer");
      List<MetricGraphData> response = queryprovider.getMetricValue(criteriaInfo, sixMinutesAgo, duration,
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
   public void testCallCountOneServiceOneOperationOneConsumer() throws ServiceException {// validated

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
