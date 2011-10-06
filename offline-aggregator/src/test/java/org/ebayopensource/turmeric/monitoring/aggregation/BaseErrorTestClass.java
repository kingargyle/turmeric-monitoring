package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.aggregation.util.MockInitContext;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;

public class BaseErrorTestClass {

   /** The Constant HOST. */
   protected static final String HOST = "127.0.1.10:9160";
   /** The Constant KEY_SPACE. */
   protected static final String KEY_SPACE = "TurmericMonitoring";
   /** The Constant TURMERIC_TEST_CLUSTER. */
   protected static final String TURMERIC_TEST_CLUSTER = "Test Cluster";
   protected CassandraConnectionInfo connectionInfo;
   protected final String consumerName = "ConsumerName1";
   protected Date endTime;
   protected CassandraErrorLoggingHandler errorStorageProvider;
   protected long fiveMinutesAgo;
   protected long fourMinutesAgo;
   private final String[] keyArray = null;
   protected String[] keysToFind1MinAgo;
   protected String[] keysToFind2MinsAgos;
   protected String[] keysToFind3MinsAgos;
   protected CassandraMetricsStorageProvider metricsStorageProvider;
   protected long now;
   protected CassandraConnectionInfo offlineConnectionInfo;
   protected long oneMinuteAgo;
   protected String operationName1MinAgo;
   protected String operationName2MinsAgo;
   protected String operationName3MinsAgo;
   protected final String opName = "Operation1";
   protected ColumnFamilyReader reader;
   protected final String serverName = "localhost";
   protected final boolean serverSide = true;
   protected String serviceNameToStore1MinAgo;
   protected String serviceNameToStore2MinsAgo;
   protected String serviceNameToStore3MinsAgo;
   protected final String srvcAdminName = "ServiceAdminName1";
   protected Date startTime;
   protected long threeMinutesAgo;
   protected long twoMinutesAgo;

   protected Map<String, String> createOptions() {
      Map<String, String> options = new HashMap<String, String>();
      options.put("host-address", HOST);
      options.put("keyspace-name", KEY_SPACE);
      options.put("cluster-name", TURMERIC_TEST_CLUSTER);
      options.put("storeServiceMetrics", "false");
      options.put("embedded", "false");
      return options;
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

   public void setup() throws ServiceException {
      Map<String, String> options = createOptions();
      now = System.currentTimeMillis();
      oneMinuteAgo = now - (1000 * 60 * 1);
      twoMinutesAgo = now - (1000 * 60 * 2);
      threeMinutesAgo = now - (1000 * 60 * 3);
      fourMinutesAgo = now - (1000 * 60 * 4);
      fiveMinutesAgo = now - (1000 * 60 * 5);

      serviceNameToStore1MinAgo = srvcAdminName + "-" + oneMinuteAgo;
      serviceNameToStore2MinsAgo = srvcAdminName + "-" + twoMinutesAgo;
      serviceNameToStore3MinsAgo = srvcAdminName + "-" + threeMinutesAgo;
      operationName1MinAgo = opName + "-" + oneMinuteAgo;
      operationName2MinsAgo = opName + "-" + twoMinutesAgo;
      operationName3MinsAgo = opName + "-" + threeMinutesAgo;

      InitContext ctx = new MockInitContext(options);
      errorStorageProvider = new CassandraErrorLoggingHandler();
      metricsStorageProvider = new CassandraMetricsStorageProvider();
      errorStorageProvider.init(ctx);
      metricsStorageProvider.init(options, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
      connectionInfo = new CassandraConnectionInfo("Test Cluster", "127.0.0.1", "9160", "TurmericMonitoring");
      offlineConnectionInfo = new CassandraConnectionInfo("Test Cluster", "127.0.0.1", "9160",
               "TurmericMonitoringOffline");

   }

}
