package org.ebayopensource.turmeric.monitoring.aggregation.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.SuperSlice;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SuperSliceQuery;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.monitoring.aggregation.BaseTest;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.writer.IpPerDayAndServiceNameWriter;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IpPerDayAndServiceNameWriterTest extends BaseTest {

   private IpPerDayAndServiceNameWriter ipPerDayAndServiceNameWriter;

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      ipPerDayAndServiceNameWriter = new IpPerDayAndServiceNameWriter(startTime, endTime, offlineConnectionInfo);
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
      ipPerDayAndServiceNameWriter = null;
   }

   @Test
   public void testWriteData() {
      Map<String, AggregationData<String>> data = new HashMap<String, AggregationData<String>>();
      String key = new SimpleDateFormat("ddMMyyyy").format(new Date());
      AggregationData<String> dataRow = new AggregationData<String>(key);
      Set ipAdresses = new HashSet();
      ipAdresses.addAll(Arrays.asList("ip1", "ip2", "ip3"));
      dataRow.addColumn(srvcAdminName, ipAdresses);

      data.put(key, dataRow);
      ipPerDayAndServiceNameWriter.writeData(data);
      assertColumnFamilyEntry(key, srvcAdminName, new String[] { "ip1", "ip2", "ip3" });
   }

   private void assertColumnFamilyEntry(String key, String superColumnName, String[] columnValues) {
      SuperSliceQuery<Object, String, String, String> q = HFactory.createSuperSliceQuery(
               offlineConnectionInfo.getKeyspace(), SerializerTypeInferer.getSerializer(key), StringSerializer.get(),
               StringSerializer.get(), StringSerializer.get());
      q.setColumnFamily("IpPerDayAndServiceName");
      q.setKey(key);
      q.setRange(null, null, false, 100);
      QueryResult<SuperSlice<String, String, String>> r = q.execute();
      SuperSlice<String, String, String> columnSlice = r.get();
      for (HSuperColumn<String, String, String> hSuperColumn : columnSlice.getSuperColumns()) {
         if (superColumnName.equals(hSuperColumn.getName())) {
            assertEquals("The super column must contain " + columnValues.length + " columns", columnValues.length,
                     hSuperColumn.getColumns().size());
            for (int i = 0; i < columnValues.length; i++) {
               boolean columnFound = false;
               for (HColumn<String, String> column : hSuperColumn.getColumns()) {
                  if (columnValues[i].equals(column.getName())) {
                     columnFound = true;
                     break;
                  }
               }
               assertTrue("the column name " + columnValues[i] + " wasn't found", columnFound);
            }
            return;
         }
      }
      fail("No super column with name " + superColumnName + " was found for the key " + key);

   }
}
