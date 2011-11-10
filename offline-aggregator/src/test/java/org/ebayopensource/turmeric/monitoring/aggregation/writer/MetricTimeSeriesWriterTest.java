package org.ebayopensource.turmeric.monitoring.aggregation.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.serializers.LongSerializer;
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
import org.ebayopensource.turmeric.monitoring.aggregation.metric.writer.MetricTimeSeriesWriter;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MetricTimeSeriesWriterTest extends BaseTest {

   private MetricTimeSeriesWriter metricTimeSeriesWriter;

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      metricTimeSeriesWriter = new MetricTimeSeriesWriter(startTime, endTime, offlineConnectionInfo);
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
      metricTimeSeriesWriter = null;
   }

   @Test
   public void testWriteData() {
      Map<String, AggregationData<String>> data = new HashMap<String, AggregationData<String>>();
      String key = "somemetrictimeserieskey";
      AggregationData<String> dataRow = new AggregationData<String>(key);
      dataRow.addColumn(threeMinutesAgo, Arrays.asList("metricvaluekey1", "metricvaluekey2"));
      dataRow.addColumn(twoMinutesAgo, Arrays.asList("metricvaluekey3", "metricvaluekey4"));
      dataRow.addColumn(oneMinuteAgo, Arrays.asList("metricvaluekey5", "metricvaluekey6"));

      data.put(key, dataRow);
      metricTimeSeriesWriter.writeData(data);
      assertColumnFamilyEntry(key, oneMinuteAgo, new String[] { "metricvaluekey5", "metricvaluekey6" });
      assertColumnFamilyEntry(key, twoMinutesAgo, new String[] { "metricvaluekey3", "metricvaluekey4" });
      assertColumnFamilyEntry(key, threeMinutesAgo, new String[] { "metricvaluekey1", "metricvaluekey2" });
   }

   private void assertColumnFamilyEntry(String key, Long superColumnName, String[] columnValues) {
      SuperSliceQuery<Object, Long, String, String> q = HFactory.createSuperSliceQuery(
               offlineConnectionInfo.getKeyspace(), SerializerTypeInferer.getSerializer(key), LongSerializer.get(),
               StringSerializer.get(), StringSerializer.get());
      q.setColumnFamily("MetricTimeSeries");
      q.setKey(key);
      q.setRange(null, null, false, 100);
      QueryResult<SuperSlice<Long, String, String>> r = q.execute();
      SuperSlice<Long, String, String> columnSlice = r.get();
      for (HSuperColumn<Long, String, String> hSuperColumn : columnSlice.getSuperColumns()) {
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
