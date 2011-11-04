package org.ebayopensource.turmeric.monitoring.provider.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.ebayopensource.turmeric.runtime.common.monitoring.value.LongSumMetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetMetricsMetadataTest extends BaseTest {

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
      MetricId metricId1 = new MetricId(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), srvcAdminName, opName);
      MetricValue metricValue1 = new AverageMetricValue(metricId1);
      MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      MetricId metricId2 = new MetricId(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), srvcAdminName,
               "anotherOperation");
      MetricValue metricValue2 = new AverageMetricValue(metricId2);
      MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      MetricId metricId3 = new MetricId(SystemMetricDefs.OP_ERR_TOTAL.getMetricName(), srvcAdminName,
               "anotherOperation");
      MetricValue metricValue3 = new LongSumMetricValue(metricId3);
      MetricValueAggregatorTestImpl aggregator3 = new MetricValueAggregatorTestImpl(metricValue3,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      MetricClassifier metricClassifier1 = new MetricClassifier(consumerName, "sourceDC1", "targetDC1");
      MetricClassifier metricClassifier2 = new MetricClassifier("anotherConsumer", "sourceDC1", "targetDC1");
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier2, 1L);
      aggregator2.update(metricClassifier2, 1L);
      aggregator3.update(metricClassifier2, 1L);
      List<MetricValueAggregator> aggregators = deepCopyAggregators(aggregator1, aggregator2, aggregator3);
      metricsStorageProvider.saveMetricSnapshot(sixMinutesAgo, aggregators);

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

   @Test
   public void testServiceResponseEntityWithNullResourceEntityListAndServiceResponseType() {
      List<String> result = queryprovider.getMetricsMetadata("Service", null, "Service");
      assertNotNull(result);
      assertEquals(1, result.size());
      assertEquals(srvcAdminName, result.get(0));
   }

   @Test
   public void testServiceResponseEntityWithOperationResponseType() {
      List<String> result = queryprovider.getMetricsMetadata("Service", Arrays.asList(srvcAdminName), "Operation");
      assertNotNull(result);
      assertEquals(2, result.size());
      assertEquals(srvcAdminName + "." + opName, result.get(0));
      assertEquals(srvcAdminName + "." + "anotherOperation", result.get(1));
   }

   @Test
   public void testServiceResponseEntityWithConsumerResponseType() {
      List<String> result = queryprovider.getMetricsMetadata("Service", Arrays.asList(srvcAdminName), "Consumer");
      assertNotNull(result);
      assertEquals(2, result.size());
      assertEquals(consumerName, result.get(0));
      assertEquals("anotherConsumer", result.get(1));
   }

}
