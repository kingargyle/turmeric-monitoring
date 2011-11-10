package org.ebayopensource.turmeric.monitoring.aggregation.reader;

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
import org.ebayopensource.turmeric.monitoring.aggregation.BaseTest;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;
import org.ebayopensource.turmeric.monitoring.aggregation.error.reader.ErrorsByCategoryReader;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ErrorsByCategoryReaderTest extends BaseTest {
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
      keysToFind2MinsAgo = createKeysToFind(srvcAdminName, opName, twoMinutesAgo);
      keysToFind3MinsAgo = createKeysToFind(srvcAdminName, opName, threeMinutesAgo);
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
      assertNotNull(keys);
      assertEquals(keysToFind1MinAgo.length, keys.size());
      assertTrue(keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
   }

   @Test
   public void testRetrieveKeysInRange2MinsToNow() throws ServiceException {
      startTime = new Date(twoMinutesAgo);
      endTime = new Date(now);
      reader = new ErrorsByCategoryReader(startTime, endTime, connectionInfo);
      List<String> keys = reader.retrieveKeysInRange();
      assertNotNull(keys);
      assertEquals(keysToFind2MinsAgo.length, keys.size());
      assertTrue(keys.containsAll(Arrays.asList(keysToFind2MinsAgo)));
   }

   @Test
   public void testRetrieveKeysNokeysInTimeRange() throws ServiceException {
      startTime = new Date(now - (1000 * 60 * 60 * 24));// one day ago
      endTime = new Date(now - (1000 * 60 * 60 * 23));// one day ago an hour later
      reader = new ErrorsByCategoryReader(startTime, endTime, connectionInfo);

      List<String> keys = reader.retrieveKeysInRange();
      assertNotNull(keys);
      assertTrue(keys.isEmpty());
   }

   @Test
   public void testReadDataInRange3MinsToNow() throws ServiceException {
      startTime = new Date(threeMinutesAgo);
      endTime = new Date(now);
      reader = new ErrorsByCategoryReader(startTime, endTime, connectionInfo);
      Map<String, AggregationData<String>> readData = reader.readData();
      assertNotNull("readData should not be null", readData);
      for (int i = 0; i < keysToFind1MinAgo.length; i++) {
         AggregationData<String> rowData = readData.get(keysToFind1MinAgo[i]);
         assertNotNull(rowData);
         int columnSize = rowData.size();
         assertEquals("There must be 3 column timestamps", 3, columnSize);

         assertTrue("Each map must have as key the timestamp", rowData.contains(threeMinutesAgo));
         assertNotNull(rowData.getValue(threeMinutesAgo));
         assertEquals("Each map must contain the corresponding errorValueKey.", threeMinutesAgo + "|1",
                  rowData.getValue(threeMinutesAgo));
         assertTrue("Each map must have as key the timestamp", rowData.contains(twoMinutesAgo));
         assertNotNull(rowData.getValue(twoMinutesAgo));
         assertEquals("Each map must contain the corresponding errorValueKey.", twoMinutesAgo + "|1",
                  rowData.getValue(twoMinutesAgo));
         assertTrue("Each map must have as key the timestamp", rowData.contains(oneMinuteAgo));
         assertNotNull(rowData.getValue(oneMinuteAgo));
         assertEquals("Each map must contain the corresponding errorValueKey.", oneMinuteAgo + "|1",
                  rowData.getValue(oneMinuteAgo));
      }
   }
}
