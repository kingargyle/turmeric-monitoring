package org.ebayopensource.turmeric.monitoring.aggregation.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.monitoring.aggregation.BaseTest;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;
import org.ebayopensource.turmeric.monitoring.aggregation.error.writer.ErrorsByIdWriter;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ErrorsByIdWriterTest extends BaseTest {

   private ErrorsByIdWriter errorByIdWriter;

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      errorByIdWriter = new ErrorsByIdWriter(startTime, endTime, offlineConnectionInfo);
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
      errorByIdWriter = null;
   }

   @Test
   public void testWriteData() {
      Map<Long, AggregationData<Long>> data = new HashMap<Long, AggregationData<Long>>();
      Long key = 1l;
      AggregationData<Long> dataRow = new AggregationData<Long>(key);
      dataRow.addColumn("organization", "TestOrganization");
      dataRow.addColumn("category", "APPLICATION");
      dataRow.addColumn("subDomain", "TestSubdomain");

      data.put(key, dataRow);
      errorByIdWriter.writeData(data);
      assertColumnFamilyEntry(key, "organization", "TestOrganization");
      assertColumnFamilyEntry(key, "category", "APPLICATION");
      assertColumnFamilyEntry(key, "subDomain", "TestSubdomain");
   }

   private void assertColumnFamilyEntry(Long key, String columnName, String columnValue) {
      SliceQuery<Object, String, String> q = HFactory.createSliceQuery(offlineConnectionInfo.getKeyspace(),
               SerializerTypeInferer.getSerializer(key), StringSerializer.get(), StringSerializer.get());
      q.setColumnFamily("ErrorsById");
      q.setKey(key);
      q.setRange(null, null, false, 100);
      QueryResult<ColumnSlice<String, String>> r = q.execute();
      ColumnSlice<String, String> columnSlice = r.get();
      for (HColumn<String, String> hColumn : columnSlice.getColumns()) {
         if (columnName.equals(hColumn.getName())) {
            assertEquals("The column value should be the same for the column name " + columnName, columnValue,
                     hColumn.getValue());
            return;
         }
      }
      fail("No column with name " + columnName + " was found for the key " + key);

   }
}
