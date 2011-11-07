package org.ebayopensource.turmeric.monitoring.aggregation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.monitoring.aggregation.error.reader.ErrorsByCategoryReader;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ErrorsByCategoryReaderTestITCase extends BaseTest {
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

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      keysToFind1MinAgo = createKeysToFind(srvcAdminName, opName, oneMinuteAgo);
      keysToFind2MinsAgos = createKeysToFind(srvcAdminName, opName, twoMinutesAgo);
      keysToFind3MinsAgos = createKeysToFind(srvcAdminName, opName, threeMinutesAgo);
      errorStorageProvider.persistErrors(createTestCommonErrorDataList(3), serverName, srvcAdminName, opName,
               serverSide, consumerName, threeMinutesAgo);
      errorStorageProvider.persistErrors(createTestCommonErrorDataList(3), serverName, srvcAdminName, opName,
               serverSide, consumerName, twoMinutesAgo);
      errorStorageProvider.persistErrors(createTestCommonErrorDataList(3), serverName, srvcAdminName, opName,
               serverSide, consumerName, oneMinuteAgo);
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
   }

   @Test
   public void testRetrieveKeysInRange1MinToNow() throws ServiceException {
      startTime = new Date(oneMinuteAgo);
      endTime = new Date(now);
      reader = new ErrorsByCategoryReader(startTime, endTime, connectionInfo);

      List<String> keys = reader.retrieveKeysInRange();

      assertTrue(keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
   }

   @Test
   public void testRetrieveKeysInRange2MinsToNow() throws ServiceException {
      startTime = new Date(twoMinutesAgo);
      endTime = new Date(now);
      reader = new ErrorsByCategoryReader(startTime, endTime, connectionInfo);
      List<String> keys = reader.retrieveKeysInRange();
      assertTrue(keys.containsAll(Arrays.asList(keysToFind2MinsAgos)));
   }

   @Test
   public void testRetrieveKeysNokeysInTimeRange() throws ServiceException {
      startTime = new Date(now - (1000 * 60 * 60 * 24));// one day ago
      endTime = new Date(now - (1000 * 60 * 60 * 23));// one day ago an hour later
      reader = new ErrorsByCategoryReader(startTime, endTime, connectionInfo);

      List<String> keys = reader.retrieveKeysInRange();

      assertTrue(keys.isEmpty());
   }

   @Test
   public void testReadDataInRange3MinsToNow() throws ServiceException {
      startTime = new Date(threeMinutesAgo);
      endTime = new Date(now);
      reader = new ErrorsByCategoryReader(startTime, endTime, connectionInfo);
      Map<String, List<Map<Object, Object>>> readData = reader.readData();
      assertNotNull("readData should not be null", readData);
      for (int i = 0; i < keysToFind1MinAgo.length; i++) {
         List<Map<Object, Object>> columns = readData.get(keysToFind1MinAgo[i]);
         assertNotNull(columns);
         int columnSize = columns.size();
         assertEquals("There must be 3 column timestamps", 3, columnSize);
         for (Map<Object, Object> map : columns) {
            System.out.println("keysToFind1MinAgo[" + i + "]->" + keysToFind1MinAgo[i] + "=" + map);
            // each map should have 1 key with the
         }
         Map<Object, Object> map = columns.get(0);
         assertTrue("Each map must have as key the timestamp", map.containsKey(threeMinutesAgo));
         assertNotNull(map.get(threeMinutesAgo));
         assertEquals("Each map must contain the corresponding errorValueKey.", threeMinutesAgo + "|1",
                  map.get(threeMinutesAgo));
         map = columns.get(1);
         assertTrue("Each map must have as key the timestamp", map.containsKey(twoMinutesAgo));
         assertNotNull(map.get(twoMinutesAgo));
         assertEquals("Each map must contain the corresponding errorValueKey.", twoMinutesAgo + "|1",
                  map.get(twoMinutesAgo));
         map = columns.get(2);
         assertTrue("Each map must have as key the timestamp", map.containsKey(oneMinuteAgo));
         assertNotNull(map.get(oneMinuteAgo));
         assertEquals("Each map must contain the corresponding errorValueKey.", oneMinuteAgo + "|1",
                  map.get(oneMinuteAgo));
      }
   }
}
