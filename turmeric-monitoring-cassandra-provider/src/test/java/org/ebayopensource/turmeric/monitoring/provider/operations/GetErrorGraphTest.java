package org.ebayopensource.turmeric.monitoring.provider.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.monitoring.provider.BaseTest;
import org.ebayopensource.turmeric.monitoring.provider.MockInitContext;
import org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceCassandraProviderImpl;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGraphData;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetErrorGraphTest extends BaseTest {

   private final String consumerName = "ConsumerName1";
   private final long now = System.currentTimeMillis();
   private final long oneMinuteAgo = now - TimeUnit.SECONDS.toMillis(60);
   private final String opName = "Operation1";
   private final long sixMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 6);
   private final String srvcAdminName = "ServiceAdminName1";
   private final long twoMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 2);
   private final long threeMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 3);

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
   }

   public void createTestData() throws ServiceException {
      CommonErrorData error1 = new CommonErrorData();
      error1.setCategory(ErrorCategory.APPLICATION);
      error1.setSeverity(ErrorSeverity.ERROR);
      error1.setCause("TestCause");
      error1.setDomain("TestDomain");
      error1.setSubdomain("TestSubdomain");
      error1.setErrorName("TestErrorName1");
      error1.setErrorId(1l);
      error1.setMessage("Error Message 1");
      error1.setOrganization("TestOrganization");

      CommonErrorData error2 = new CommonErrorData();
      error2.setCategory(ErrorCategory.APPLICATION);
      error2.setSeverity(ErrorSeverity.ERROR);
      error2.setCause("TestCause");
      error2.setDomain("TestDomain");
      error2.setSubdomain("TestSubdomain");
      error2.setErrorName("TestErrorName2");
      error2.setErrorId(2l);
      error2.setMessage("Error Message 2");
      error2.setOrganization("TestOrganization");

      errorStorageProvider.persistErrors(Collections.singletonList(error1), getInetAddress(), srvcAdminName, opName,
               true, consumerName, sixMinutesAgo);

      errorStorageProvider.persistErrors(Collections.singletonList(error2), getInetAddress(), srvcAdminName,
               "anotherOperation", true, "anotherConsumer", threeMinutesAgo);

      errorStorageProvider.persistErrors(Collections.singletonList(error1), getInetAddress(), srvcAdminName, opName,
               true, consumerName, oneMinuteAgo);

   }

   @Test
   public void testOneServiceOneOperationOneConsumerByCategory() {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 2;
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      List<MetricGraphData> result = queryprovider.getErrorGraph(srvcAdminName, opName, consumerName, null,
               ErrorCategory.APPLICATION.value(), null, metricCriteria);
      assertNotNull(result);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, result.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : result) {
         sum += metricGraphData.getCount();
      }

      assertEquals("The sum of the MetricGraphData collection must be " + expectedSum, expectedSum, sum);
   }

   @Test
   public void testOneServiceOneOperationOneConsumerBySeverity() {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 2;
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      List<MetricGraphData> result = queryprovider.getErrorGraph(srvcAdminName, opName, consumerName, null, null,
               ErrorSeverity.ERROR.value(), metricCriteria);
      assertNotNull(result);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, result.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : result) {
         sum += metricGraphData.getCount();
      }

      assertEquals("The sum of the MetricGraphData collection must be " + expectedSum, expectedSum, sum);
   }

   @Test
   public void testOneServiceByCategory() {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 3;
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      List<MetricGraphData> result = queryprovider.getErrorGraph(srvcAdminName, null, null, null,
               ErrorCategory.APPLICATION.value(), null, metricCriteria);
      assertNotNull(result);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, result.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : result) {
         sum += metricGraphData.getCount();
      }

      assertEquals("The sum of the MetricGraphData collection must be " + expectedSum, expectedSum, sum);
   }

   @Test
   public void testOneServiceBySeverity() {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 3;
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      List<MetricGraphData> result = queryprovider.getErrorGraph(srvcAdminName, null, null, null, null,
               ErrorSeverity.ERROR.value(), metricCriteria);
      assertNotNull(result);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, result.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : result) {
         sum += metricGraphData.getCount();
      }

      assertEquals("The sum of the MetricGraphData collection must be " + expectedSum, expectedSum, sum);
   }

   @Test
   public void testOneServiceOneOperationByCategory() {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 1;
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      List<MetricGraphData> result = queryprovider.getErrorGraph(srvcAdminName, "anotherOperation", null, null,
               ErrorCategory.APPLICATION.value(), null, metricCriteria);
      assertNotNull(result);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, result.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : result) {
         sum += metricGraphData.getCount();
      }

      assertEquals("The sum of the MetricGraphData collection must be " + expectedSum, expectedSum, sum);
   }

   @Test
   public void testOneServiceOneOperationBySeverity() {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 1;
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      List<MetricGraphData> result = queryprovider.getErrorGraph(srvcAdminName, "anotherOperation", null, null, null,
               ErrorSeverity.ERROR.value(), metricCriteria);
      assertNotNull(result);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, result.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : result) {
         sum += metricGraphData.getCount();
      }

      assertEquals("The sum of the MetricGraphData collection must be " + expectedSum, expectedSum, sum);
   }

   @Test
   public void testOneServiceOneConsumerByCategory() {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 2;
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      List<MetricGraphData> result = queryprovider.getErrorGraph(srvcAdminName, null, consumerName, null,
               ErrorCategory.APPLICATION.value(), null, metricCriteria);
      assertNotNull(result);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, result.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : result) {
         sum += metricGraphData.getCount();
      }

      assertEquals("The sum of the MetricGraphData collection must be " + expectedSum, expectedSum, sum);
   }

   @Test
   public void testOneServiceOneConsumerBySeverity() {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      int expectedSum = 2;
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      List<MetricGraphData> result = queryprovider.getErrorGraph(srvcAdminName, null, consumerName, null, null,
               ErrorSeverity.ERROR.value(), metricCriteria);
      assertNotNull(result);
      assertEquals("The response must contain always duration/aggregationPeriod elements",
               duration / aggregationPeriod, result.size());
      int sum = 0;
      for (MetricGraphData metricGraphData : result) {
         sum += metricGraphData.getCount();
      }

      assertEquals("The sum of the MetricGraphData collection must be " + expectedSum, expectedSum, sum);
   }
}
