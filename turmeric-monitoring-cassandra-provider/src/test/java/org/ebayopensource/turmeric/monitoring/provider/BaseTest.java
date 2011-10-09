package org.ebayopensource.turmeric.monitoring.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.OrderedSuperRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.SuperRow;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.RangeSuperSlicesQuery;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.monitoring.provider.dao.BaseMetricsErrorsByFilterDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricServiceCallsByTimeDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricTimeSeriesDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValueAggregatorTestImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValuesByIpAndDateDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValuesDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorByIdDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorValuesDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsServiceConsumerByIpDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsServiceOperationByIpDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricServiceCallsByTimeDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricTimeSeriesDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricValuesByIpAndDateDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricValuesDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorByIdDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorValuesDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorsByCategoryDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorsBySeverityDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsServiceConsumerByIpDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsServiceOperationByIpDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.manager.cassandra.server.CassandraTestManager;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricCategory;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricClassifier;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricId;
import org.ebayopensource.turmeric.runtime.common.monitoring.MonitoringLevel;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.AverageMetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.LongSumMetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorManager;
import org.junit.After;
import org.junit.Before;

public abstract class BaseTest {

   /** The Constant TURMERIC_TEST_CLUSTER. */
   protected static final String TURMERIC_TEST_CLUSTER = "TestCluster";

   /** The Constant KEY_SPACE. */
   protected static final String KEY_SPACE = "TestKeyspace";

   /** The Constant HOST. */
   protected static final String HOST = "127.0.1.10:9160";

   protected final Map<String, String> options = loadProperties();

   /** The providers. */
   protected CassandraMetricsStorageProvider metricsStorageProvider;
   protected SOAMetricsQueryServiceProvider queryprovider;
   protected CassandraErrorLoggingHandler errorStorageProvider;

   protected MetricsServiceConsumerByIpDAO<String, String> metricsServiceConsumerByIpDAO;
   protected MetricValuesByIpAndDateDAO<String, Long> metricValuesByIpAndDateDAO;
   protected MetricTimeSeriesDAO<String> metricTimeSeriesDAO;
   protected MetricValuesDAO<String> metricValuesDAO;
   protected MetricServiceCallsByTimeDAO<String, Long> serviceCallsByTimeDAO;
   protected MetricsErrorByIdDAO<Long> metricsErrorByIdDAOImpl;
   protected MetricsServiceOperationByIpDAO<String, String> metricsServiceOperationByIpDAO;
   protected BaseMetricsErrorsByFilterDAO<String> metricsErrorsByCategoryDAO;
   protected BaseMetricsErrorsByFilterDAO<String> metricsErrorsBySeverityDAO;
   protected MetricsErrorValuesDAO<String> metricsErrorValuesDAO;

   @Before
   public void setUp() throws Exception {
      CassandraTestManager.initialize();

      metricsServiceOperationByIpDAO = new MetricsServiceOperationByIpDAOImpl<String, String>(TURMERIC_TEST_CLUSTER,
               HOST, KEY_SPACE, "ServiceOperationByIp", String.class, String.class);
      metricsServiceConsumerByIpDAO = new MetricsServiceConsumerByIpDAOImpl<String, String>(TURMERIC_TEST_CLUSTER,
               HOST, KEY_SPACE, "ServiceConsumerByIp", String.class, String.class);
      metricValuesByIpAndDateDAO = new MetricValuesByIpAndDateDAOImpl<String, Long>(TURMERIC_TEST_CLUSTER, HOST,
               KEY_SPACE, "MetricValuesByIpAndDate", String.class, Long.class);
      metricTimeSeriesDAO = new MetricTimeSeriesDAOImpl<String>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE,
               "MetricTimeSeries", String.class);
      metricValuesDAO = new MetricValuesDAOImpl<String>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE, "MetricValues",
               String.class);
      serviceCallsByTimeDAO = new MetricServiceCallsByTimeDAOImpl<String, Long>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE,
               "ServiceCallsByTime", String.class, Long.class);
      metricsErrorByIdDAOImpl = new MetricsErrorByIdDAOImpl<Long>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE, "ErrorsById",
               Long.class);

      metricsErrorValuesDAO = new MetricsErrorValuesDAOImpl(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE, "ErrorValues",
               String.class);
      metricsErrorsByCategoryDAO = new MetricsErrorsByCategoryDAOImpl<String>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE,
               "ErrorCountsByCategory", String.class, metricsErrorValuesDAO, metricsErrorByIdDAOImpl);
      metricsErrorsBySeverityDAO = new MetricsErrorsBySeverityDAOImpl<String>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE,
               "ErrorCountsBySeverity", String.class, metricsErrorValuesDAO, metricsErrorByIdDAOImpl);
   }

   @After
   public void tearDown() {
      queryprovider = null;
      errorStorageProvider = null;
      metricsStorageProvider = null;
      cleanUpTestData();
   }

   protected void cleanUpTestData() {
      System.out.println("######### CLEANING DATA ##############");
      Keyspace kspace = new HectorManager().getKeyspace(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE, "ServiceOperationByIp",
               false, String.class, String.class);

      String[] columnFamilies = { "ErrorsById", "MetricTimeSeries", "MetricValues", "ErrorCountsByCategory",
               "ErrorCountsBySeverity" };
      String[] superColumnFamilies = { "MetricValuesByIpAndDate", "ServiceCallsByTime", "ServiceConsumerByIp",
               "ServiceOperationByIp" };

      for (String cf : columnFamilies) {
         RangeSlicesQuery<String, String, String> rq = HFactory.createRangeSlicesQuery(kspace, StringSerializer.get(),
                  StringSerializer.get(), StringSerializer.get());
         rq.setColumnFamily(cf);
         rq.setRange("", "", false, 1000);
         QueryResult<OrderedRows<String, String, String>> qr = rq.execute();
         OrderedRows<String, String, String> orderedRows = qr.get();
         Mutator<String> deleteMutator = HFactory.createMutator(kspace, StringSerializer.get());
         for (Row<String, String, String> row : orderedRows) {
            deleteMutator.delete(row.getKey(), cf, null, StringSerializer.get());
         }
      }

      for (String scf : superColumnFamilies) {
         RangeSuperSlicesQuery<String, String, String, String> rq = HFactory.createRangeSuperSlicesQuery(kspace,
                  StringSerializer.get(), StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
         rq.setColumnFamily(scf);
         rq.setRange("", "", false, 1000);
         QueryResult<OrderedSuperRows<String, String, String, String>> qr = rq.execute();
         OrderedSuperRows<String, String, String, String> orderedRows = qr.get();
         Mutator<String> deleteMutator = HFactory.createMutator(kspace, StringSerializer.get());

         for (SuperRow<String, String, String, String> row : orderedRows) {
            deleteMutator.delete(row.getKey(), scf, null, StringSerializer.get());
         }
      }

   }

   protected Map<String, String> loadProperties() {
      Map<String, String> options = new HashMap<String, String>();
      options.put("host-address", HOST);
      options.put("keyspace-name", KEY_SPACE);
      options.put("cluster-name", TURMERIC_TEST_CLUSTER);
      options.put("storeServiceMetrics", "false");
      options.put("embedded", "true");
      return options;
   }

   protected Collection<MetricValueAggregator> createMetricValueAggregatorsCollectionForOneConsumer(String serviceName,
            String operationName, String consumerName) {
      Collection<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
      MetricId metricId1 = new MetricId("test_count", serviceName, operationName);
      MetricValue metricValue1 = new LongSumMetricValue(metricId1, 123456);
      MetricId metricId2 = new MetricId("test_average", serviceName, operationName);
      MetricValue metricValue2 = new AverageMetricValue(metricId2, 17, 456854235.123);

      MetricClassifier metricClassifier1 = new MetricClassifier(consumerName, "sourcedc", "targetdc");
      MetricClassifier metricClassifier2 = new MetricClassifier(consumerName, "sourcedc", "targetdc");

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

}
