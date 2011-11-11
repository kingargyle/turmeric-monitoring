package org.ebayopensource.turmeric.monitoring.aggregation.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.serializers.ObjectSerializer;
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
import org.ebayopensource.turmeric.monitoring.aggregation.metric.writer.MetricValuesWriter;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MetricValuesWriterTest extends BaseTest {

   private MetricValuesWriter metricValuesWriter;

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      metricValuesWriter = new MetricValuesWriter(startTime, endTime, offlineConnectionInfo);
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
      metricValuesWriter = null;
   }

   @Test
   public void testWriteData() {
      Map<String, AggregationData<String>> data = new HashMap<String, AggregationData<String>>();
      String key = "somemetrictimeserieskey";
      AggregationData<String> dataRow = new AggregationData<String>(key);
      dataRow.addColumn("column1", new Long(11));
      dataRow.addColumn("column2", new Double(123.45678));
      dataRow.addColumn("column3", new Float(17.234));
      data.put(key, dataRow);
      metricValuesWriter.writeData(data);

      assertColumnFamilyEntry(key, "column1", new Long(11));
      assertColumnFamilyEntry(key, "column2", new Double(123.45678));
      assertColumnFamilyEntry(key, "column3", new Float(17.234));
   }

   private void assertColumnFamilyEntry(String key, String columnName, Object columnValue) {
      SliceQuery<Object, String, Object> q = HFactory.createSliceQuery(offlineConnectionInfo.getKeyspace(),
               SerializerTypeInferer.getSerializer(key), StringSerializer.get(), ObjectSerializer.get());
      q.setColumnFamily("MetricValues");
      q.setKey(key);
      q.setRange(null, null, false, 100);
      QueryResult<ColumnSlice<String, Object>> r = q.execute();
      ColumnSlice<String, Object> columnSlice = r.get();
      for (HColumn<String, Object> hColumn : columnSlice.getColumns()) {
         if (columnName.equals(hColumn.getName())) {
            assertEquals(columnValue, hColumn.getValue());
            return;
         }
      }
      fail("No column with name " + columnName + " was found for the key " + key);

   }
}
