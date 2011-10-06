package org.ebayopensource.turmeric.monitoring.aggregation;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.aggregation.error.ErrorsByIdReader;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.Before;
import org.junit.Test;

public class ErrorsByIdReaderTestITCase extends BaseErrorTestClass {

   protected Long[] keysToFind1MinAgo;
   protected Long[] keysToFind2MinsAgos;
   protected Long[] keysToFind3MinsAgos;

   private Long[] createKeysToFind(String serviceNameToStore, String operationName, long time) {
      return new Long[] { 0l, 1l, 2l };
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

   @Test
   public void testRetrieveKeysInRange1MinToNow() throws ServiceException {
      startTime = new Date(oneMinuteAgo);
      endTime = new Date(now);
      reader = new ErrorsByIdReader(startTime, endTime, connectionInfo);

      List<String> keys = reader.retrieveKeysInRange();

      assertTrue(keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind2MinsAgos)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind3MinsAgos)));
   }

   @Test
   public void testRetrieveKeysInRange2MinsToNow() throws ServiceException {
      startTime = new Date(twoMinutesAgo);
      endTime = new Date(now);
      reader = new ErrorsByIdReader(startTime, endTime, connectionInfo);

      List<Long> keys = reader.retrieveKeysInRange();
      for (Long key : keys) {
         System.out.println(key);
      }

      assertTrue(!keys.isEmpty());
      assertTrue(keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind2MinsAgos)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind3MinsAgos)));
   }

   @Test
   public void testRetrieveKeysInRange3MinsToNow() throws ServiceException {
      startTime = new Date(threeMinutesAgo);
      endTime = new Date(now);
      reader = new ErrorsByIdReader(startTime, endTime, connectionInfo);

      List<Long> keys = reader.retrieveKeysInRange();

      assertTrue(!keys.isEmpty());
      assertTrue(keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind2MinsAgos)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind3MinsAgos)));
   }

   @Test
   public void testRetrieveKeysInRange5MinsTo4MinsAgo() throws ServiceException {
      startTime = new Date(fiveMinutesAgo);
      endTime = new Date(fourMinutesAgo);
      reader = new ErrorsByIdReader(startTime, endTime, connectionInfo);

      List<Long> keys = reader.retrieveKeysInRange();

      assertTrue(!keys.isEmpty());
      assertTrue(keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind2MinsAgos)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind3MinsAgos)));
   }

}
