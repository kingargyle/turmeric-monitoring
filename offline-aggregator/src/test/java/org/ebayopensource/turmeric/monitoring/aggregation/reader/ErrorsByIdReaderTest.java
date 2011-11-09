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
import org.ebayopensource.turmeric.monitoring.aggregation.error.reader.ErrorsByIdReader;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ErrorsByIdReaderTest extends BaseTest {

   protected Long[] keysToFind;

   private Long[] createKeysToFind() {
      return new Long[] { 0l, 1l, 2l };
   }

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      keysToFind = createKeysToFind();

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

      assertTrue(keys.containsAll(Arrays.asList(keysToFind)));
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
      reader = new ErrorsByIdReader(startTime, endTime, connectionInfo);
      Map<Long, AggregationData<Long>> readData = reader.readData();
      assertNotNull("readData should not be null", readData);
      for (int i = 0; i < keysToFind.length; i++) {
         AggregationData<Long> rowData = readData.get(keysToFind[i]);
         assertNotNull(rowData);
         int columnSize = rowData.size();
         assertEquals("There must be 10 columns", 10, columnSize);
         assertEquals("TestOrganization", rowData.getValue("organization"));
         assertEquals("APPLICATION", rowData.getValue("category"));
         assertEquals("TestSubdomain", rowData.getValue("subDomain"));
         assertEquals("TestErrorName", rowData.getValue("name"));
         assertEquals("TestDomain", rowData.getValue("domain"));
         assertEquals("ERROR", rowData.getValue("severity"));
         assertEquals(threeMinutesAgo + "|1", rowData.getValue("" + threeMinutesAgo));
         assertEquals(twoMinutesAgo + "|1", rowData.getValue("" + twoMinutesAgo));
         assertEquals(oneMinuteAgo + "|1", rowData.getValue("" + oneMinuteAgo));
      }
   }

}
