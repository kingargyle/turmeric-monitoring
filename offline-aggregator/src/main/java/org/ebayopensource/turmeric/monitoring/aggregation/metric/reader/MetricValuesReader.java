package org.ebayopensource.turmeric.monitoring.aggregation.metric.reader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.ColumnFamilyReader;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

// TODO: Auto-generated Javadoc
/**
 * The Class MetricTimeSeriesReader.
 */
public class MetricValuesReader extends ColumnFamilyReader<String> {

   /** The Constant columnFamilyName. */
   private static final String columnFamilyName = "MetricValues";
   private MetricTimeSeriesReader timeSeriesReader = null;

   /**
    * Instantiates a new metric time series reader.
    * 
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @param connectionInfo
    *           the connection info
    */
   public MetricValuesReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
      timeSeriesReader = new MetricTimeSeriesReader(startTime, endTime, connectionInfo);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, AggregationData<String>> readData() {
      Map<String, AggregationData<String>> result = new HashMap<String, AggregationData<String>>();
      try {

         List<String> keysToRead = retrieveKeysInRange();
         MultigetSliceQuery<String, String, Object> multigetSliceQuery = HFactory.createMultigetSliceQuery(
                  connectionInfo.getKeyspace(), STR_SERIALIZER, STR_SERIALIZER, ObjectSerializer.get());
         multigetSliceQuery.setColumnFamily(columnFamilyName);
         multigetSliceQuery.setKeys(keysToRead);
         multigetSliceQuery.setRange("", "", false, ROWS_NUMBER_MAX_VALUE);
         QueryResult<Rows<String, String, Object>> queryResult = multigetSliceQuery.execute();
         if (queryResult != null) {
            for (Row<String, String, Object> row : queryResult.get()) {
               AggregationData<String> rowData = new AggregationData<String>(row.getKey());
               for (HColumn<String, Object> column : row.getColumnSlice().getColumns()) {
                  rowData.addColumn(column.getName(), column.getValue());
               }
               result.put(row.getKey(), rowData);
            }
         } else {

         }
      } catch (Exception e) {
         e.printStackTrace();
         if (e.getCause() != null) {
            e.getCause().printStackTrace();
         }
      }
      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> retrieveKeysInRange() {
      List<String> rowKeys = new ArrayList<String>();
      Map<String, AggregationData<String>> metricSeriesData = timeSeriesReader.readData();
      for (Entry<String, AggregationData<String>> metricTimeSeriesRow : metricSeriesData.entrySet()) {
         AggregationData<String> rowData = metricTimeSeriesRow.getValue();
         for (Entry<Object, Object> columnDataEntry : rowData.getColumns().entrySet()) {
            Set metricValueKeys = (Set) columnDataEntry.getValue();
            for (Object metricValueKey : metricValueKeys) {
               if (!rowKeys.contains(metricValueKey)) {
                  rowKeys.add(metricValueKey.toString());
               }
            }

         }
      }
      return rowKeys;
   }

}
