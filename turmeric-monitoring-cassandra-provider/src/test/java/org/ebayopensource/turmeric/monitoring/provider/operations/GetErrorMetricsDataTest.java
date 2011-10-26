package org.ebayopensource.turmeric.monitoring.provider.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.ebayopensource.turmeric.monitoring.provider.model.ExtendedErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetErrorMetricsDataTest extends BaseTest {

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
               "anotherOperation", true, consumerName, threeMinutesAgo);

      // errorStorageProvider.persistErrors(Collections.singletonList(error1), getInetAddress(), srvcAdminName, opName,
      // true, consumerName, twoMinutesAgo);

      errorStorageProvider.persistErrors(Collections.singletonList(error1), getInetAddress(), srvcAdminName, opName,
               true, consumerName, oneMinuteAgo);

   }

   @Test
   public void testGetApplicationErrorsByCategoryOneServiceOneOperationOneConsumer() throws ServiceException {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 0;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");

      List<ExtendedErrorViewData> response = queryprovider.getExtendedErrorMetricsData("Category",
               Arrays.asList(srvcAdminName), Arrays.asList(opName), Arrays.asList(consumerName), null,
               ErrorCategory.APPLICATION.value(), ErrorSeverity.ERROR.value(), null, metricCriteria);
      assertNotNull(response);
      assertEquals("There must be 1 data row in the response", 1, response.size());
      int expectedErrorCount1 = 2;
      assertEquals("there must be " + expectedErrorCount1 + " errors for the errorCount1 field.", expectedErrorCount1,
               response.get(0).getErrorCount1());
      int expectedErrorCount2 = 1;
      assertEquals("there must be " + expectedErrorCount2 + " errors for the errorCount2 field.", expectedErrorCount2,
               response.get(0).getErrorCount2());

   }

   @Test
   public void testGetApplicationErrorsByCategoryOneServiceOneOperationOneConsumerOneMinDiff() throws ServiceException {
      long duration = 60 * 1;// in secs
      int aggregationPeriod = 0;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(threeMinutesAgo);
      metricCriteria.setSecondStartTime(twoMinutesAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");

      List<ExtendedErrorViewData> response = queryprovider.getExtendedErrorMetricsData("Category",
               Arrays.asList(srvcAdminName), Arrays.asList(opName, "anotherOperation"), Arrays.asList(consumerName),
               null, ErrorCategory.APPLICATION.value(), ErrorSeverity.ERROR.value(), null, metricCriteria);
      assertNotNull(response);
      assertEquals("There must be 2 data rows in the response", 2, response.size());
      int expectedErrorCount1 = 1;
      assertEquals("there must be " + expectedErrorCount1 + " errors for the errorCount1 field.", expectedErrorCount1,
               response.get(0).getErrorCount1());
      int expectedErrorCount2 = 0;
      assertEquals("there must be " + expectedErrorCount2 + " errors for the errorCount2 field.", expectedErrorCount2,
               response.get(0).getErrorCount2());

      expectedErrorCount1 = 0;
      assertEquals("there must be " + expectedErrorCount1 + " errors for the errorCount1 field.", expectedErrorCount1,
               response.get(1).getErrorCount1());
      expectedErrorCount2 = 1;
      assertEquals("there must be " + expectedErrorCount2 + " errors for the errorCount2 field.", expectedErrorCount2,
               response.get(1).getErrorCount2());

   }

   @Test
   public void testGetApplicationErrorsBySeverityOneServiceOneOperationOneConsumer() throws ServiceException {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 0;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");

      List<ExtendedErrorViewData> response = queryprovider.getExtendedErrorMetricsData("Severity",
               Arrays.asList(srvcAdminName), Arrays.asList(opName), Arrays.asList(consumerName), null,
               ErrorCategory.APPLICATION.value(), ErrorSeverity.ERROR.value(), null, metricCriteria);
      assertNotNull(response);
      assertEquals("There must be 1 data row in the response", 1, response.size());
      int expectedErrorCount1 = 2;
      assertEquals("there must be " + expectedErrorCount1 + " errors for the errorCount1 field.", expectedErrorCount1,
               response.get(0).getErrorCount1());
      int expectedErrorCount2 = 1;
      assertEquals("there must be " + expectedErrorCount2 + " errors for the errorCount2 field.", expectedErrorCount2,
               response.get(0).getErrorCount2());

   }

   @Test
   public void testGetApplicationErrorsByCategoryOneServiceNoOperationOneConsumer() throws ServiceException {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 0;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");

      List<ExtendedErrorViewData> response = queryprovider.getExtendedErrorMetricsData("Category",
               Arrays.asList(srvcAdminName), new ArrayList<String>(), Arrays.asList(consumerName), null,
               ErrorCategory.APPLICATION.value(), ErrorSeverity.ERROR.value(), null, metricCriteria);
      assertNotNull(response);
      assertEquals("There must be 2 data rows in the response", 2, response.size());
      int expectedErrorCount1 = 2;
      assertEquals("there must be " + expectedErrorCount1 + " errors for the errorCount1 field.", expectedErrorCount1,
               response.get(0).getErrorCount1());
      int expectedErrorCount2 = 1;
      assertEquals("there must be " + expectedErrorCount2 + " errors for the errorCount2 field.", expectedErrorCount2,
               response.get(0).getErrorCount2());

      expectedErrorCount1 = 1;
      assertEquals("there must be " + expectedErrorCount1 + " errors for the errorCount1 field.", expectedErrorCount1,
               response.get(1).getErrorCount1());
      expectedErrorCount2 = 0;
      assertEquals("there must be " + expectedErrorCount2 + " errors for the errorCount2 field.", expectedErrorCount2,
               response.get(1).getErrorCount2());

   }

   @Test
   public void testGetApplicationErrorsBySeverityOneServiceNoOperationOneConsumer() throws ServiceException {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 0;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");

      List<ExtendedErrorViewData> response = queryprovider.getExtendedErrorMetricsData("Severity",
               Arrays.asList(srvcAdminName), new ArrayList<String>(), Arrays.asList(consumerName), null,
               ErrorCategory.APPLICATION.value(), ErrorSeverity.ERROR.value(), null, metricCriteria);
      assertNotNull(response);
      assertEquals("There must be 2 data rows in the response", 2, response.size());
      int expectedErrorCount1 = 2;
      assertEquals("there must be " + expectedErrorCount1 + " errors for the errorCount1 field.", expectedErrorCount1,
               response.get(0).getErrorCount1());
      int expectedErrorCount2 = 1;
      assertEquals("there must be " + expectedErrorCount2 + " errors for the errorCount2 field.", expectedErrorCount2,
               response.get(0).getErrorCount2());

      expectedErrorCount1 = 1;
      assertEquals("there must be " + expectedErrorCount1 + " errors for the errorCount1 field.", expectedErrorCount1,
               response.get(1).getErrorCount1());
      expectedErrorCount2 = 0;
      assertEquals("there must be " + expectedErrorCount2 + " errors for the errorCount2 field.", expectedErrorCount2,
               response.get(1).getErrorCount2());

   }

   @Test
   public void testGetApplicationErrorsByCategoryOneServiceNoOperationNoConsumer() throws ServiceException {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 0;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");

      List<ExtendedErrorViewData> response = queryprovider.getExtendedErrorMetricsData("Category",
               Arrays.asList(srvcAdminName), new ArrayList<String>(), new ArrayList<String>(), null,
               ErrorCategory.APPLICATION.value(), ErrorSeverity.ERROR.value(), null, metricCriteria);
      assertNotNull(response);
      assertEquals("There must be 2 data rows in the response", 2, response.size());
      int expectedErrorCount1 = 2;
      assertEquals("there must be " + expectedErrorCount1 + " errors for the errorCount1 field.", expectedErrorCount1,
               response.get(0).getErrorCount1());
      int expectedErrorCount2 = 1;
      assertEquals("there must be " + expectedErrorCount2 + " errors for the errorCount2 field.", expectedErrorCount2,
               response.get(0).getErrorCount2());

      expectedErrorCount1 = 1;
      assertEquals("there must be " + expectedErrorCount1 + " errors for the errorCount1 field.", expectedErrorCount1,
               response.get(1).getErrorCount1());
      expectedErrorCount2 = 0;
      assertEquals("there must be " + expectedErrorCount2 + " errors for the errorCount2 field.", expectedErrorCount2,
               response.get(1).getErrorCount2());

   }

   @Test
   public void testGetApplicationErrorsBySeverityOneServiceNoOperationNoConsumer() throws ServiceException {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 0;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(oneMinuteAgo);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");

      List<ExtendedErrorViewData> response = queryprovider.getExtendedErrorMetricsData("Severity",
               Arrays.asList(srvcAdminName), new ArrayList<String>(), new ArrayList<String>(), null,
               ErrorCategory.APPLICATION.value(), ErrorSeverity.ERROR.value(), null, metricCriteria);
      assertNotNull(response);
      assertEquals("There must be 2 data rows in the response", 2, response.size());
      int expectedErrorCount1 = 2;
      assertEquals("there must be " + expectedErrorCount1 + " errors for the errorCount1 field.", expectedErrorCount1,
               response.get(0).getErrorCount1());
      int expectedErrorCount2 = 1;
      assertEquals("there must be " + expectedErrorCount2 + " errors for the errorCount2 field.", expectedErrorCount2,
               response.get(0).getErrorCount2());

      expectedErrorCount1 = 1;
      assertEquals("there must be " + expectedErrorCount1 + " errors for the errorCount1 field.", expectedErrorCount1,
               response.get(1).getErrorCount1());
      expectedErrorCount2 = 0;
      assertEquals("there must be " + expectedErrorCount2 + " errors for the errorCount2 field.", expectedErrorCount2,
               response.get(1).getErrorCount2());

   }

   @Test(expected = IllegalArgumentException.class)
   public void testGetApplicationErrorsByUnsupportedErrorType() throws ServiceException {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 0;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(now);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");

      List<ExtendedErrorViewData> response = queryprovider.getExtendedErrorMetricsData("UnsupportedErrorType",
               Arrays.asList(srvcAdminName), new ArrayList<String>(), new ArrayList<String>(), null,
               ErrorCategory.APPLICATION.value(), ErrorSeverity.ERROR.value(), null, metricCriteria);

   }

}
