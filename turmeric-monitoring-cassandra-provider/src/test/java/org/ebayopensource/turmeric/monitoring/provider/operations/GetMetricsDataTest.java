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
import org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGroupData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricResourceCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.ResourceEntity;
import org.ebayopensource.turmeric.monitoring.v1.services.ResourceEntityRequest;
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

public class GetMetricsDataTest extends BaseTest {

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

   private List<MetricValueAggregator> deepCopyAggregators(MetricValueAggregator... aggregators) {
      // The aggregator list passed to the storage provider is always a deep copy of the aggregators
      List<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
      for (MetricValueAggregator aggregator : aggregators) {
         result.add((MetricValueAggregator) aggregator.deepCopy(false));
      }
      return result;
   }

   @Test
   public void testCallCountOneOperationNoConsumer() throws ServiceException {

      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(now);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      metricCriteria.setMetricName("CallCount");

      MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
      metricResourceCriteria.setResourceEntityResponseType("Operation");
      ResourceEntityRequest rer1 = new ResourceEntityRequest();
      rer1.setResourceEntityType(ResourceEntity.OPERATION);
      rer1.getResourceEntityName().add(opName);
      metricResourceCriteria.getResourceRequestEntities().add(rer1);
      ResourceEntityRequest rer2 = new ResourceEntityRequest();
      rer2.setResourceEntityType(ResourceEntity.SERVICE);
      rer2.getResourceEntityName().add(srvcAdminName);
      metricResourceCriteria.getResourceRequestEntities().add(rer2);
      List<MetricGroupData> response = queryprovider.getMetricsData(metricCriteria, metricResourceCriteria);
      assertNotNull(response);
      assertEquals(1, response.size());
      MetricGroupData data = response.get(0);
      assertNotNull(data);
      assertEquals(13, data.getCount1(), 0);
      assertEquals(0, data.getCount2(), 0);

   }

   @Test
   public void testCallCountOneServiceNoOperationOneConsumer() throws ServiceException {

      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(now);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      metricCriteria.setMetricName("CallCount");

      MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
      metricResourceCriteria.setResourceEntityResponseType("Service");
      ResourceEntityRequest rer1 = new ResourceEntityRequest();
      rer1.setResourceEntityType(ResourceEntity.SERVICE);
      rer1.getResourceEntityName().add(srvcAdminName);
      metricResourceCriteria.getResourceRequestEntities().add(rer1);
      ResourceEntityRequest rer2 = new ResourceEntityRequest();
      rer2.setResourceEntityType(ResourceEntity.CONSUMER);
      rer2.getResourceEntityName().add(consumerName);
      metricResourceCriteria.getResourceRequestEntities().add(rer2);
      List<MetricGroupData> response = queryprovider.getMetricsData(metricCriteria, metricResourceCriteria);
      assertNotNull(response);
      assertEquals(1, response.size());
      MetricGroupData data = response.get(0);
      assertNotNull(data);
      assertEquals(9, data.getCount1(), 0);
      assertEquals(0, data.getCount2(), 0);
      assertEquals("The response should have the data grouped by service.", srvcAdminName, data.getCriteriaInfo()
               .getServiceName());
   }

   @Test
   public void testCallCountNoOperationNoConsumer() throws ServiceException {

      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(threeMinutesAgo);
      metricCriteria.setSecondStartTime(now);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      metricCriteria.setMetricName("CallCount");

      MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
      metricResourceCriteria.setResourceEntityResponseType("Operation");
      ResourceEntityRequest rer1 = new ResourceEntityRequest();
      rer1.setResourceEntityType(ResourceEntity.OPERATION);
      metricResourceCriteria.getResourceRequestEntities().add(rer1);
      ResourceEntityRequest rer2 = new ResourceEntityRequest();
      rer2.setResourceEntityType(ResourceEntity.SERVICE);
      rer2.getResourceEntityName().add(srvcAdminName);
      metricResourceCriteria.getResourceRequestEntities().add(rer2);
      List<MetricGroupData> response = queryprovider.getMetricsData(metricCriteria, metricResourceCriteria);
      assertNotNull(response);
      assertEquals(1, response.size());
      MetricGroupData data = response.get(0);
      assertNotNull(data);
      assertEquals(11, data.getCount1(), 0);
      assertEquals(0, data.getCount2(), 0);
      assertEquals("The response should have the data grouped by operation.", opName, data.getCriteriaInfo()
               .getOperationName());

   }

   @Test
   public void testResponseTimeOneOperationNoConsumerOperationResponse() throws ServiceException {
      long duration = 60;// in secs
      int aggregationPeriod = 20;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(twoMinutesAgo);
      metricCriteria.setSecondStartTime(now);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      metricCriteria.setMetricName("ResponseTime");

      MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
      metricResourceCriteria.setResourceEntityResponseType("Operation");
      ResourceEntityRequest rer1 = new ResourceEntityRequest();
      rer1.setResourceEntityType(ResourceEntity.OPERATION);
      rer1.getResourceEntityName().add(opName);
      metricResourceCriteria.getResourceRequestEntities().add(rer1);
      ResourceEntityRequest rer2 = new ResourceEntityRequest();
      rer2.setResourceEntityType(ResourceEntity.SERVICE);
      rer2.getResourceEntityName().add(srvcAdminName);
      metricResourceCriteria.getResourceRequestEntities().add(rer2);
      List<MetricGroupData> response = queryprovider.getMetricsData(metricCriteria, metricResourceCriteria);
      assertNotNull(response);
      assertEquals(1, response.size());
      MetricGroupData data = response.get(0);
      assertNotNull(data);
      assertEquals("Unexpected value for Count1.", 9, data.getCount1(), 0);
      assertEquals("Unexpected value for Count2.", 0, data.getCount2(), 0);
      assertEquals("The response should have the data grouped by operation.", opName, data.getCriteriaInfo()
               .getOperationName());

   }

   @Test
   public void testErrorCountNoOperationNoConsumerOperationResponse() throws ServiceException {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(now);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      metricCriteria.setMetricName("ErrorCount");

      MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
      metricResourceCriteria.setResourceEntityResponseType("Error");
      ResourceEntityRequest rer1 = new ResourceEntityRequest();
      metricResourceCriteria.getResourceRequestEntities().add(rer1);
      ResourceEntityRequest rer2 = new ResourceEntityRequest();
      rer2.setResourceEntityType(ResourceEntity.SERVICE);
      rer2.getResourceEntityName().add(srvcAdminName);
      metricResourceCriteria.getResourceRequestEntities().add(rer2);
      List<MetricGroupData> response = queryprovider.getMetricsData(metricCriteria, metricResourceCriteria);
      assertNotNull(response);
      assertEquals(1, response.size());
      MetricGroupData data = response.get(0);
      assertNotNull(data);
      assertEquals("Unexpected value for Count1.", 1, data.getCount1(), 0);
      assertEquals("Unexpected value for Count2.", 0, data.getCount2(), 0);
      assertEquals("The response should have the data grouped by error metric name.",
               SystemMetricDefs.OP_ERR_TOTAL.getMetricName(), data.getCriteriaInfo().getMetricName());
      assertEquals("The response should contain the service name", srvcAdminName, data.getCriteriaInfo()
               .getServiceName());

   }

   @Test
   public void testResponseTimeNoOperationOneConsumerConsumerResponse() throws ServiceException {
      long duration = 60 * 6;// in secs
      int aggregationPeriod = 20;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(sixMinutesAgo);
      metricCriteria.setSecondStartTime(now);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      metricCriteria.setMetricName("ResponseTime");

      MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
      metricResourceCriteria.setResourceEntityResponseType("Consumer");
      ResourceEntityRequest rer1 = new ResourceEntityRequest();
      rer1.setResourceEntityType(ResourceEntity.CONSUMER);
      rer1.getResourceEntityName().add(consumerName);
      metricResourceCriteria.getResourceRequestEntities().add(rer1);
      ResourceEntityRequest rer2 = new ResourceEntityRequest();
      rer2.setResourceEntityType(ResourceEntity.SERVICE);
      rer2.getResourceEntityName().add(srvcAdminName);
      metricResourceCriteria.getResourceRequestEntities().add(rer2);
      List<MetricGroupData> response = queryprovider.getMetricsData(metricCriteria, metricResourceCriteria);
      assertNotNull(response);
      assertEquals(1, response.size());
      MetricGroupData data = response.get(0);
      assertNotNull(data);
      assertEquals("Unexpected value for Count1.", 9, data.getCount1(), 0);
      assertEquals("Unexpected value for Count2.", 0, data.getCount2(), 0);
      assertEquals("The response should have the data grouped by consumerName.", consumerName, data.getCriteriaInfo()
               .getConsumerName());

   }

   @Test
   public void testResponseTimeNoOperationNoConsumerOperationResponse() throws ServiceException {
      long duration = 60;// in secs
      int aggregationPeriod = 20;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(twoMinutesAgo);
      metricCriteria.setSecondStartTime(now);
      metricCriteria.setDuration(duration);
      metricCriteria.setAggregationPeriod(aggregationPeriod);
      metricCriteria.setRoleType("server");
      metricCriteria.setMetricName("ResponseTime");

      MetricResourceCriteria metricResourceCriteria = new MetricResourceCriteria();
      metricResourceCriteria.setResourceEntityResponseType("Operation");
      ResourceEntityRequest rer1 = new ResourceEntityRequest();
      rer1.setResourceEntityType(ResourceEntity.OPERATION);
      metricResourceCriteria.getResourceRequestEntities().add(rer1);
      ResourceEntityRequest rer2 = new ResourceEntityRequest();
      rer2.setResourceEntityType(ResourceEntity.SERVICE);
      rer2.getResourceEntityName().add(srvcAdminName);
      metricResourceCriteria.getResourceRequestEntities().add(rer2);
      List<MetricGroupData> response = queryprovider.getMetricsData(metricCriteria, metricResourceCriteria);
      assertNotNull(response);
      assertEquals(1, response.size());
      MetricGroupData data = response.get(0);
      assertNotNull(data);
      assertEquals("Unexpected value for Count1.", 9, data.getCount1(), 0);
      assertEquals("Unexpected value for Count2.", 0, data.getCount2(), 0);
      assertEquals("The response should have the data grouped by operation.", opName, data.getCriteriaInfo()
               .getOperationName());

   }
}