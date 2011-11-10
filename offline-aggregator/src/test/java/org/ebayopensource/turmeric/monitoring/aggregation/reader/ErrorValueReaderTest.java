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
import org.apache.commons.lang.ArrayUtils;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.monitoring.aggregation.BaseTest;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;
import org.ebayopensource.turmeric.monitoring.aggregation.error.reader.ErrorValueReader;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ErrorValueReaderTest extends BaseTest {
   protected String[] keysToFind;

   private String[] createKeysToFind(long timestamp) {
      return new String[] { timestamp + "|1" };
   }

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      keysToFind3MinsAgo = createKeysToFind(threeMinutesAgo);
      keysToFind2MinsAgo = createKeysToFind(twoMinutesAgo);
      keysToFind1MinAgo = createKeysToFind(oneMinuteAgo);
      keysToFind = (String[]) ArrayUtils.addAll(ArrayUtils.addAll(keysToFind3MinsAgo, keysToFind2MinsAgo),
               keysToFind1MinAgo);
      errorStorageProvider.persistErrors(createTestCommonErrorDataList(3), serverName, srvcAdminName, opName,
               serverSide, consumerName, threeMinutesAgo);
      errorStorageProvider.persistErrors(createTestCommonErrorDataList(3), serverName, srvcAdminName, opName,
               serverSide, consumerName, twoMinutesAgo);
      errorStorageProvider.persistErrors(createTestCommonErrorDataList(3), serverName, srvcAdminName, opName,
               serverSide, consumerName, oneMinuteAgo);
   }

   @Test
   public void testRetrieveKeysInRange2MinsToNow() throws ServiceException {
      startTime = new Date(threeMinutesAgo);
      endTime = new Date(now);
      reader = new ErrorValueReader(startTime, endTime, connectionInfo);
      List<String> keys = reader.retrieveKeysInRange();
      assertNotNull("the key list must not be null", keys);
      assertTrue(keys.containsAll(Arrays.asList(keysToFind3MinsAgo)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind2MinsAgo)));
      assertTrue(keys.containsAll(Arrays.asList(keysToFind1MinAgo)));
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
   }

   @Test
   public void testReadDataInRange3MinsToNow() throws ServiceException {
      startTime = new Date(threeMinutesAgo);
      endTime = new Date(now);
      reader = new ErrorValueReader(startTime, endTime, connectionInfo);
      Map<String, AggregationData<Long>> readData = reader.readData();
      assertNotNull("readData should not be null", readData);
      for (int i = 0; i < keysToFind.length; i++) {
         AggregationData<Long> rowData = readData.get(keysToFind[i]);
         assertNotNull(rowData);
         int columnSize = rowData.size();
         assertEquals("There must be 17 columns", 17, columnSize);
         assertEquals("TestOrganization", rowData.getValue("organization"));
         assertEquals("APPLICATION", rowData.getValue("category"));
         assertEquals("TestSubdomain", rowData.getValue("subDomain"));
         assertEquals("TestErrorName", rowData.getValue("name"));
         assertEquals("TestDomain", rowData.getValue("domain"));
         assertEquals("ERROR", rowData.getValue("severity"));
         assertEquals(0l, rowData.getValue("aggregationPeriod"));
         assertEquals(consumerName, rowData.getValue("consumerName"));
         assertNotNull(rowData.getValue("errorId"));
         Long errorId = (Long) rowData.getValue("errorId");
         assertEquals("Error Message " + errorId, rowData.getValue("errorMessage"));
         assertEquals(keysToFind[i], rowData.getValue("key"));
         assertEquals(opName, rowData.getValue("operationName"));
         assertEquals(1, rowData.getValue("randomNumber"));
         assertEquals(serverName, rowData.getValue("serverName"));
         assertEquals("true", rowData.getValue("serverSide"));
         assertEquals(srvcAdminName, rowData.getValue("serviceAdminName"));
         assertNotNull(rowData.getValue("tstamp"));
      }
   }
}
