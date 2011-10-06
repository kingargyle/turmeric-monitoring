package org.ebayopensource.turmeric.monitoring.aggregation;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.aggregation.error.ErrorsByCategoryReader;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ErrorsByCategoryReaderTestITCase extends BaseErrorTestClass {
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

   @Before
   public void setUp() throws Exception {
      super.setup();
      keysToFind1MinAgo = createKeysToFind(serviceNameToStore1MinAgo, operationName1MinAgo, twoMinutesAgo);
      keysToFind2MinsAgos = createKeysToFind(serviceNameToStore2MinsAgo, operationName2MinsAgo, twoMinutesAgo);
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

      assertTrue(keys.isEmpty());
      assertTrue(!keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
      assertTrue(!keys.containsAll(Arrays.asList(keysToFind2MinsAgos)));
      assertTrue(!keys.containsAll(Arrays.asList(keysToFind3MinsAgos)));
   }
}
