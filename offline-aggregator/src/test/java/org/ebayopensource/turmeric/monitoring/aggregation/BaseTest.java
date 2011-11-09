package org.ebayopensource.turmeric.monitoring.aggregation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.OrderedSuperRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.SuperRow;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.RangeSuperSlicesQuery;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.aggregation.util.CassandraTestManager;
import org.ebayopensource.turmeric.monitoring.aggregation.util.MockInitContext;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorManager;
import org.junit.After;

public class BaseTest {

   /** The Constant HOST. */
   protected static final String HOST = "127.0.0.1";
   /** The Constant KEY_SPACE. */
   protected static final String ONLINE_KEYSPACE_NAME = "TurmericMonitoring";
   protected static final String OFFLINE_KEYSPACE_NAME = "TurmericOfflineMonitoring";
   /** The Constant TURMERIC_TEST_CLUSTER. */
   protected static final String TURMERIC_CLUSTER = "TestCluster";
   private static final String PORT = "9160";
   protected CassandraConnectionInfo connectionInfo;
   protected final String consumerName = "ConsumerName1";
   protected Date endTime;
   protected CassandraErrorLoggingHandler errorStorageProvider;
   protected long fiveMinutesAgo;
   protected long fourMinutesAgo;
   protected String[] keysToFind1MinAgo;
   protected String[] keysToFind2MinsAgo;
   protected String[] keysToFind3MinsAgo;
   protected CassandraMetricsStorageProvider metricsStorageProvider;
   protected long now;
   protected CassandraConnectionInfo offlineConnectionInfo;
   protected long oneMinuteAgo;
   protected final String opName = "Operation1";
   protected ColumnFamilyReader reader;
   protected final String serverName = "localhost";
   protected final boolean serverSide = true;
   protected final String srvcAdminName = "ServiceAdminName1";
   protected Date startTime;
   protected long threeMinutesAgo;
   protected long twoMinutesAgo;

   protected Map<String, String> createOptionsForKeyspace(String keyspaceName) {
      Map<String, String> options = new HashMap<String, String>();
      options.put("host-address", HOST + ":" + PORT);
      options.put("keyspace-name", keyspaceName);
      options.put("cluster-name", TURMERIC_CLUSTER);
      options.put("storeServiceMetrics", "false");
      options.put("embedded", "false");
      options.put("random-generator-class-name", "org.ebayopensource.turmeric.monitoring.aggregation.util.MockRandom");
      return options;
   }

   protected Map<String, String> createOptionsForOnlineKeyspace() {
      return createOptionsForKeyspace(ONLINE_KEYSPACE_NAME);
   }

   protected Map<String, String> createOptionsForOfflineKeyspace() {
      return createOptionsForKeyspace(OFFLINE_KEYSPACE_NAME);
   }

   protected List<CommonErrorData> createTestCommonErrorDataList(int errorQuantity) {
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

   public void setUp() throws ServiceException, TTransportException, IOException, InterruptedException,
            ConfigurationException {
      Map<String, String> options = createOptionsForOnlineKeyspace();
      now = System.currentTimeMillis();
      oneMinuteAgo = now - (1000 * 60 * 1);
      twoMinutesAgo = now - (1000 * 60 * 2);
      threeMinutesAgo = now - (1000 * 60 * 3);
      fourMinutesAgo = now - (1000 * 60 * 4);
      fiveMinutesAgo = now - (1000 * 60 * 5);

      CassandraTestManager.initializeCluster();
      InitContext ctx = new MockInitContext(options);
      errorStorageProvider = new CassandraErrorLoggingHandler();
      metricsStorageProvider = new CassandraMetricsStorageProvider();
      errorStorageProvider.init(ctx);
      metricsStorageProvider.init(options, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
      connectionInfo = new CassandraConnectionInfo(TURMERIC_CLUSTER, HOST, PORT, ONLINE_KEYSPACE_NAME);
      offlineConnectionInfo = new CassandraConnectionInfo(TURMERIC_CLUSTER, HOST, PORT, OFFLINE_KEYSPACE_NAME);
      // now I need to create the keyspace for the offline data
      createOfflineKeyspace();
   }

   private void createOfflineKeyspace() throws ServiceException {
      try {
         Cluster cluster = HFactory.getOrCreateCluster(TURMERIC_CLUSTER, HOST + ":" + PORT);
         KeyspaceDefinition ksDefinition = new ThriftKsDef(OFFLINE_KEYSPACE_NAME);
         Keyspace keyspace = HFactory.createKeyspace(OFFLINE_KEYSPACE_NAME, cluster);
         // createKeyspace(kspace, cluster);
         cluster.addKeyspace(ksDefinition);
      } catch (HectorException e) {
         // ugly I know
      }
      // I create a error Logging provider to get the cf created for us
      Map<String, String> offlineOptions = createOptionsForOfflineKeyspace();
      new CassandraErrorLoggingHandler().init(new MockInitContext(offlineOptions));
      new CassandraMetricsStorageProvider().init(offlineOptions, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
   }

   @After
   public void tearDown() {
      errorStorageProvider = null;
      metricsStorageProvider = null;
      keysToFind1MinAgo = null;
      keysToFind2MinsAgo = null;
      keysToFind3MinsAgo = null;
      now = -1;
      fiveMinutesAgo = -1;
      fourMinutesAgo = -1;
      threeMinutesAgo = -1;
      twoMinutesAgo = -1;
      oneMinuteAgo = -1;
      startTime = null;
      endTime = null;
      reader = null;
      cleanUpTestData();
      // CassandraTestManager.cleanUpCassandraDirs();
   }

   protected void cleanUpTestData() {
      System.out.println("######### CLEANING DATA ##############");
      Keyspace kspace = new HectorManager().getKeyspace(TURMERIC_CLUSTER, HOST, ONLINE_KEYSPACE_NAME,
               "ServiceOperationByIp", false, String.class, String.class);

      String[] columnFamilies = { "ErrorsById", "MetricValues", "ErrorCountsByCategory", "ErrorCountsBySeverity" };
      String[] superColumnFamilies = { "MetricTimeSeries", "MetricValuesByIpAndDate", "ServiceCallsByTime",
               "ServiceConsumerByIp", "ServiceOperationByIp" };

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

}
