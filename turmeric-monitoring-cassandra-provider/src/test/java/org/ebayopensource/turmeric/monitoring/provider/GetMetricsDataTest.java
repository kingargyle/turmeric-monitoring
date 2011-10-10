package org.ebayopensource.turmeric.monitoring.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValueAggregatorTestImpl;
import org.ebayopensource.turmeric.monitoring.provider.model.ExtendedErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorInfos;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGraphData;
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
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class GetMetricsDataTest extends BaseTest {

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
   private int accumCount = 0;
   private double accumResponse = 0;

   /**
    * Creates the data.
    * 
    * @param time
    */
   private void createData() {
      errorsToStore = createTestCommonErrorDataList(1);
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
   public void testGetMetricsDataCallCountMetricForOneOperationNoConsumers() throws ServiceException {
      createData();
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);
      Collection<MetricValueAggregator> snapshotCollection2 = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
              srvcAdminName, opName, consumerName);  

      metricsStorageProvider.saveMetricSnapshot(twoMinutesAgo, snapshotCollection);
      metricsStorageProvider.saveMetricSnapshot(now, snapshotCollection2);
      
      


      long duration = 60;// in secs
      // according DAOErrorLoggingHandler.persistErrors aggregation period
      // should always be 0
      int aggregationPeriod = 20;// in secs
      MetricCriteria metricCriteria = new MetricCriteria();
      metricCriteria.setFirstStartTime(twoMinutesAgo);
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
      metricResourceCriteria.getResourceRequestEntities().add(rer1 );
      ResourceEntityRequest rer2 = new ResourceEntityRequest();
      rer2.setResourceEntityType(ResourceEntity.SERVICE);
      rer2.getResourceEntityName().add(srvcAdminName);
      metricResourceCriteria.getResourceRequestEntities().add(rer2 );
      List<MetricGroupData> response = queryprovider.getMetricsData(metricCriteria, metricResourceCriteria );
      assertNotNull(response);
      assertEquals(1, response.size());
      MetricGroupData data = response.get(0);
      assertNotNull(data);
      assertEquals(1, data.getCount1(), 0);
      assertEquals(1, data.getCount2(), 0);

   }
   
   @Test
   public void testGetMetricsDataResponseTimeMetricForOneOperationNoConsumers() throws ServiceException {
      createData();
      
      Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
               srvcAdminName, opName, consumerName);
      Collection<MetricValueAggregator> snapshotCollection2 = createMetricValueAggregatorsForOneConsumerWithTotalMetric(
              srvcAdminName, opName, consumerName);  

      metricsStorageProvider.saveMetricSnapshot(twoMinutesAgo, snapshotCollection);
      metricsStorageProvider.saveMetricSnapshot(now, snapshotCollection2);


      long duration = 60;// in secs
      // according DAOErrorLoggingHandler.persistErrors aggregation period
      // should always be 0
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
      metricResourceCriteria.getResourceRequestEntities().add(rer1 );
      ResourceEntityRequest rer2 = new ResourceEntityRequest();
      rer2.setResourceEntityType(ResourceEntity.SERVICE);
      rer2.getResourceEntityName().add(srvcAdminName);
      metricResourceCriteria.getResourceRequestEntities().add(rer2 );
      List<MetricGroupData> response = queryprovider.getMetricsData(metricCriteria, metricResourceCriteria );
      assertNotNull(response);
      assertEquals(1, response.size());
      MetricGroupData data = response.get(0);
      assertNotNull(data);
      assertEquals("Unexpected value for Count1.", 1234, data.getCount1(), 0);
      assertEquals("Unexpected value for Count2.", 1234, data.getCount2(), 0);

   }
   
   protected Collection<MetricValueAggregator> createMetricValueAggregatorsForOneConsumerWithTotalMetric(
            String serviceName, String operationName, String consumerName) {
      accumCount = 1;
	  accumResponse = 1234.00; 
	   
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
