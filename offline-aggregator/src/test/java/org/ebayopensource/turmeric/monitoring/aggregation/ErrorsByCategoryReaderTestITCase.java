package org.ebayopensource.turmeric.monitoring.aggregation;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.aggregation.error.AbstractErrorCountsReader;
import org.ebayopensource.turmeric.monitoring.aggregation.error.ErrorsByCategoryReader;
import org.ebayopensource.turmeric.monitoring.aggregation.util.MockInitContext;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ErrorsByCategoryReaderTestITCase {
   /** The Constant HOST. */
   protected static final String HOST = "127.0.1.10:9160";
   /** The Constant KEY_SPACE. */
   protected static final String KEY_SPACE = "TurmericMonitoring";
   /** The Constant TURMERIC_TEST_CLUSTER. */
   protected static final String TURMERIC_TEST_CLUSTER = "Test Cluster";
   CassandraConnectionInfo connectionInfo;
   private final String consumerName = "ConsumerName1";

   private Date endTime;
   protected CassandraErrorLoggingHandler errorStorageProvider;
   private long fiveMinutesAgo;
   private long fourMinutesAgo;
   private final String[] keyArray = null;
   private String[] keysToFind1MinAgo;
   private String[] keysToFind2MinsAgos;
   private String[] keysToFind3MinsAgos;
   protected CassandraMetricsStorageProvider metricsStorageProvider;
   private long now;
   private CassandraConnectionInfo offlineConnectionInfo;
   private long oneMinuteAgo;
   private String operationName1MinAgo;
   private String operationName2MinsAgo;
   private String operationName3MinsAgo;
   private final String opName = "Operation1";
   private AbstractErrorCountsReader reader;
   private final String serverName = "localhost";
   private final boolean serverSide = true;
   private String serviceNameToStore1MinAgo;
   private String serviceNameToStore2MinsAgo;
   private String serviceNameToStore3MinsAgo;
   private final String srvcAdminName = "ServiceAdminName1";
   private Date startTime;
   private long threeMinutesAgo;
   private long twoMinutesAgo;

   private String[] createKeysToFind(String serviceNameToStore, String operationName, long time) {
      return new String[] { "localhost|" + serviceNameToStore + "|All|All|APPLICATION|true",
               "localhost|" + serviceNameToStore + "|ConsumerName1|" + operationName + "|APPLICATION|true",
               "localhost|" + serviceNameToStore + "|All|" + operationName + "|APPLICATION|true",
               "All|" + serviceNameToStore + "|All|All|APPLICATION|true",
               "All|" + serviceNameToStore + "|All|" + operationName + "|APPLICATION|true",
               "All|" + serviceNameToStore + "|ConsumerName1|" + operationName + "|APPLICATION|true",
               "localhost|" + serviceNameToStore + "|ConsumerName1|All|APPLICATION|true",
               "All|" + serviceNameToStore + "|ConsumerName1|All|APPLICATION|true" };
   }

   protected Map<String, String> createOptions() {
      Map<String, String> options = new HashMap<String, String>();
      options.put("host-address", HOST);
      options.put("keyspace-name", KEY_SPACE);
      options.put("cluster-name", TURMERIC_TEST_CLUSTER);
      options.put("storeServiceMetrics", "false");
      options.put("embedded", "false");
      return options;
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

   @Before
   public void setUp() throws Exception {
      Map<String, String> options = createOptions();
      now = System.currentTimeMillis();
      oneMinuteAgo = now - (1000 * 60 * 1);
      twoMinutesAgo = now - (1000 * 60 * 2);
      threeMinutesAgo = now - (1000 * 60 * 3);
      fourMinutesAgo = now - (1000 * 60 * 4);
      fiveMinutesAgo = now - (1000 * 60 * 5);

      InitContext ctx = new MockInitContext(options);
      errorStorageProvider = new CassandraErrorLoggingHandler();
      metricsStorageProvider = new CassandraMetricsStorageProvider();
      errorStorageProvider.init(ctx);
      metricsStorageProvider.init(options, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
      connectionInfo = new CassandraConnectionInfo("Test Cluster", "127.0.0.1", "9160", "TurmericMonitoring");
      offlineConnectionInfo = new CassandraConnectionInfo("Test Cluster", "127.0.0.1", "9160",
               "TurmericMonitoringOffline");

      serviceNameToStore1MinAgo = srvcAdminName + "-" + oneMinuteAgo;
      operationName1MinAgo = opName + "-" + oneMinuteAgo;
      keysToFind1MinAgo = createKeysToFind(serviceNameToStore1MinAgo, operationName1MinAgo, twoMinutesAgo);
      serviceNameToStore2MinsAgo = srvcAdminName + "-" + twoMinutesAgo;
      operationName2MinsAgo = opName + "-" + twoMinutesAgo;
      keysToFind2MinsAgos = createKeysToFind(serviceNameToStore2MinsAgo, operationName2MinsAgo, twoMinutesAgo);
      serviceNameToStore3MinsAgo = srvcAdminName + "-" + threeMinutesAgo;
      operationName3MinsAgo = opName + "-" + threeMinutesAgo;
      keysToFind3MinsAgos = createKeysToFind(serviceNameToStore3MinsAgo, operationName3MinsAgo, threeMinutesAgo);
      errorStorageProvider.persistErrors(createTestCommonErrorDataList(3), serverName, serviceNameToStore3MinsAgo,
               operationName3MinsAgo, serverSide, consumerName, threeMinutesAgo);
      errorStorageProvider.persistErrors(createTestCommonErrorDataList(3), serverName, serviceNameToStore2MinsAgo,
               operationName2MinsAgo, serverSide, consumerName, twoMinutesAgo);
      errorStorageProvider.persistErrors(createTestCommonErrorDataList(3), serverName, serviceNameToStore1MinAgo,
               operationName1MinAgo, serverSide, consumerName, oneMinuteAgo);
   }

   @After
   public void tearDown() throws Exception {

   }

   @Test
   public void testRetrieveKeysInRange1MinToNow() throws ServiceException {
      startTime = new Date(oneMinuteAgo);
      endTime = new Date(now);
      reader = new ErrorsByCategoryReader(startTime, endTime, connectionInfo);

      List<String> keys = reader.retrieveKeysInRange();
      for (String string : keys) {
         System.out.println("key = " + string);
      }
      assertTrue(keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
      assertTrue(!keys.containsAll(Arrays.asList(keysToFind2MinsAgos)));
      assertTrue(!keys.containsAll(Arrays.asList(keysToFind3MinsAgos)));
   }

   @Test
   public void testRetrieveKeysInRange2MinsToNow() throws ServiceException {
      startTime = new Date(twoMinutesAgo);
      endTime = new Date(now);
      reader = new ErrorsByCategoryReader(startTime, endTime, connectionInfo);

      List<String> keys = reader.retrieveKeysInRange();
      for (String string : keys) {
         System.out.println("key = " + string);
      }
      assertTrue(keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind2MinsAgos)));
      assertTrue(!keys.containsAll(Arrays.asList(keysToFind3MinsAgos)));
   }

   @Test
   public void testRetrieveKeysInRange3MinsToNow() throws ServiceException {
      startTime = new Date(threeMinutesAgo);
      endTime = new Date(now);
      reader = new ErrorsByCategoryReader(startTime, endTime, connectionInfo);

      List<String> keys = reader.retrieveKeysInRange();
      for (String string : keys) {
         System.out.println("key = " + string);
      }
      assertTrue(keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind2MinsAgos)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind3MinsAgos)));
   }

   @Test
   public void testRetrieveKeysInRange5MinsTo4MinsAgo() throws ServiceException {
      startTime = new Date(fiveMinutesAgo);
      endTime = new Date(fourMinutesAgo);
      reader = new ErrorsByCategoryReader(startTime, endTime, connectionInfo);

      List<String> keys = reader.retrieveKeysInRange();
      for (String string : keys) {
         System.out.println("key = " + string);
      }
      assertTrue(keys.isEmpty());
      assertTrue(!keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
      assertTrue(!keys.containsAll(Arrays.asList(keysToFind2MinsAgos)));
      assertTrue(!keys.containsAll(Arrays.asList(keysToFind3MinsAgos)));
   }
}
