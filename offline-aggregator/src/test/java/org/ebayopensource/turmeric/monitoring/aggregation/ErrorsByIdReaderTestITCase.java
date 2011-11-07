package org.ebayopensource.turmeric.monitoring.aggregation;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.monitoring.aggregation.error.reader.ErrorsByIdReader;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ErrorsByIdReaderTestITCase extends BaseTest {

   protected Long[] keysToFind1MinAgo;
   protected Long[] keysToFind2MinsAgos;
   protected Long[] keysToFind3MinsAgos;

   private Long[] createKeysToFind(String serviceNameToStore, String operationName, long time) {
      return new Long[] { 0l, 1l, 2l };
   }

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      keysToFind1MinAgo = createKeysToFind(srvcAdminName, opName, twoMinutesAgo);
      keysToFind2MinsAgos = createKeysToFind(srvcAdminName, opName, twoMinutesAgo);
      keysToFind3MinsAgos = createKeysToFind(srvcAdminName, opName, threeMinutesAgo);
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

   @Override
   @After
   public void tearDown() {
      super.tearDown();
   }

}
