package org.ebayopensource.turmeric.monitoring.aggregation.writer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.serializers.LongSerializer;
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
import org.ebayopensource.turmeric.monitoring.aggregation.error.writer.ErrorsBySeverityWriter;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ErrorBySeverityWriterTest extends BaseTest {

   private ErrorsBySeverityWriter errorBySeverityWriter;

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      errorBySeverityWriter = new ErrorsBySeverityWriter(startTime, endTime, offlineConnectionInfo);
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
      errorBySeverityWriter = null;
   }

   @Test
   public void testWriteData() {
      Map<String, AggregationData<String>> data = new HashMap<String, AggregationData<String>>();
      String key = now + "|1";
      AggregationData<String> dataRow = new AggregationData<String>(key);
      dataRow.addColumn(now, "someerrorvaluekey");
      data.put(key, dataRow);
      errorBySeverityWriter.writeData(data);
      assertColumnFamilyEntry(key, now, "someerrorvaluekey");
   }

   private void assertColumnFamilyEntry(String key, Long columnName, String columnValue) {
      SliceQuery<Object, Long, String> q = HFactory.createSliceQuery(offlineConnectionInfo.getKeyspace(),
               SerializerTypeInferer.getSerializer(key), LongSerializer.get(), StringSerializer.get());
      q.setColumnFamily("ErrorCountsBySeverity");
      q.setKey(key);
      q.setRange(null, null, false, 100);
      QueryResult<ColumnSlice<Long, String>> r = q.execute();
      ColumnSlice<Long, String> columnSlice = r.get();
      for (HColumn<Long, String> hColumn : columnSlice.getColumns()) {
         assertEquals("The column name must be the same", columnName, hColumn.getName());
         assertEquals(columnValue, hColumn.getValue());
      }

   }
}
