package org.ebayopensource.turmeric.monitoring.aggregation.reader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.monitoring.aggregation.BaseTest;
import org.ebayopensource.turmeric.monitoring.aggregation.error.reader.ErrorsBySeverityReader;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ErrorsBySeverityReaderTest extends BaseTest {
   private String[] createKeysToFind(String serviceNameToStore, String operationName, long time) {
      return new String[] { "localhost|" + serviceNameToStore + "|All|All|ERROR|true",
               "localhost|" + serviceNameToStore + "|ConsumerName1|" + operationName + "|ERROR|true",
               "localhost|" + serviceNameToStore + "|All|" + operationName + "|ERROR|true",
               "All|" + serviceNameToStore + "|All|All|ERROR|true",
               "All|" + serviceNameToStore + "|All|" + operationName + "|ERROR|true",
               "All|" + serviceNameToStore + "|ConsumerName1|" + operationName + "|ERROR|true",
               "localhost|" + serviceNameToStore + "|ConsumerName1|All|ERROR|true",
               "All|" + serviceNameToStore + "|ConsumerName1|All|ERROR|true" };
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
   }

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      keysToFind1MinAgo = createKeysToFind(srvcAdminName, opName, twoMinutesAgo);
      keysToFind2MinsAgo = createKeysToFind(srvcAdminName, opName, twoMinutesAgo);
      keysToFind3MinsAgo = createKeysToFind(srvcAdminName, opName, threeMinutesAgo);
      errorStorageProvider.persistErrors(createTestCommonErrorDataList(3), serverName, srvcAdminName, opName,
               serverSide, consumerName, threeMinutesAgo);
      errorStorageProvider.persistErrors(createTestCommonErrorDataList(3), serverName, srvcAdminName, opName,
               serverSide, consumerName, twoMinutesAgo);
      errorStorageProvider.persistErrors(createTestCommonErrorDataList(3), serverName, srvcAdminName, opName,
               serverSide, consumerName, oneMinuteAgo);
   }

   @Test
   public void testRetrieveKeysInRange1MinToNow() throws ServiceException {
      startTime = new Date(oneMinuteAgo);
      endTime = new Date(now);
      reader = new ErrorsBySeverityReader(startTime, endTime, connectionInfo);

      List<String> keys = reader.retrieveKeysInRange();
      assertNotNull(keys);
      assertTrue(keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
   }

   @Test
   public void testRetrieveKeysInRange2MinsToNow() throws ServiceException {
      startTime = new Date(twoMinutesAgo);
      endTime = new Date(now);
      reader = new ErrorsBySeverityReader(startTime, endTime, connectionInfo);

      List<String> keys = reader.retrieveKeysInRange();
      assertNotNull(keys);
      assertTrue(keys.containsAll(Arrays.asList(keysToFind2MinsAgo)));
   }

   @Test
   public void testRetrieveKeysInRange3MinsToNow() throws ServiceException {
      startTime = new Date(threeMinutesAgo);
      endTime = new Date(now);
      reader = new ErrorsBySeverityReader(startTime, endTime, connectionInfo);

      List<String> keys = reader.retrieveKeysInRange();
      assertNotNull(keys);
      assertTrue("The response must contain " + Arrays.toString(keysToFind3MinsAgo),
               keys.containsAll(Arrays.asList(keysToFind3MinsAgo)));
   }

   @Test
   public void testRetrieveKeysInRange5MinsTo4MinsAgo() throws ServiceException {
      startTime = new Date(fiveMinutesAgo);
      endTime = new Date(fourMinutesAgo);
      reader = new ErrorsBySeverityReader(startTime, endTime, connectionInfo);

      List<String> keys = reader.retrieveKeysInRange();
      assertNotNull(keys);
      assertTrue(keys.isEmpty());
      assertTrue(!keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
      assertTrue(!keys.containsAll(Arrays.asList(keysToFind2MinsAgo)));
      assertTrue(!keys.containsAll(Arrays.asList(keysToFind3MinsAgo)));
   }
}
