package org.ebayopensource.turmeric.monitoring.aggregation.metric.reader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.OrderedSuperRows;
import me.prettyprint.hector.api.beans.SuperRow;
import me.prettyprint.hector.api.beans.SuperRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.MultigetSuperSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSuperSlicesQuery;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.ColumnFamilyReader;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

// TODO: Auto-generated Javadoc
/**
 * The Class MetricTimeSeriesReader.
 */
public class MetricTimeSeriesReader extends ColumnFamilyReader<String> {

   /** The Constant columnFamilyName. */
   private static final String columnFamilyName = "MetricTimeSeries";

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
   public MetricTimeSeriesReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, AggregationData<String>> readData() {
      Map<String, AggregationData<String>> result = new HashMap<String, AggregationData<String>>();
      try {
         MultigetSuperSliceQuery<String, Long, String, String> multigetSuperSliceQuery = HFactory
                  .createMultigetSuperSliceQuery(connectionInfo.getKeyspace(), STR_SERIALIZER, LONG_SERIALIZER,
                           STR_SERIALIZER, STR_SERIALIZER);
         multigetSuperSliceQuery.setColumnFamily(columnFamilyName);
         List<String> keysToRead = retrieveKeysInRange();
         multigetSuperSliceQuery.setKeys(keysToRead);
         multigetSuperSliceQuery.setRange(startTime.getTime(), endTime.getTime(), false, ROWS_NUMBER_MAX_VALUE);
         QueryResult<SuperRows<String, Long, String, String>> queryResult = multigetSuperSliceQuery.execute();
         SuperRows<String, Long, String, String> orderedRows = queryResult.get();

         for (SuperRow<String, Long, String, String> row : orderedRows) {
            AggregationData<String> rowData = new AggregationData<String>(row.getKey());
            List<HSuperColumn<Long, String, String>> superColumns = row.getSuperSlice().getSuperColumns();
            if (superColumns != null && !superColumns.isEmpty()) {
               List<String> metricKeys = new ArrayList<String>();
               for (HSuperColumn<Long, String, String> hSuperColumn : superColumns) {
                  for (HColumn<String, String> hColumn : hSuperColumn.getColumns()) {
                     metricKeys.add(hColumn.getName());
                  }
                  rowData.addColumn(hSuperColumn.getName(), metricKeys);
               }

            }
            result.put(rowData.getKey(), rowData);
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
      List<String> result = new ArrayList<String>();
      try {
         RangeSuperSlicesQuery<String, Long, String, String> rangeSuperSlicesQuery = HFactory
                  .createRangeSuperSlicesQuery(connectionInfo.getKeyspace(), STR_SERIALIZER, LONG_SERIALIZER,
                           STR_SERIALIZER, STR_SERIALIZER);
         rangeSuperSlicesQuery.setColumnFamily(columnFamilyName);
         rangeSuperSlicesQuery.setKeys(null, null);
         rangeSuperSlicesQuery.setRange(startTime.getTime(), endTime.getTime(), false, ROWS_NUMBER_MAX_VALUE);
         rangeSuperSlicesQuery.setRowCount(ROWS_NUMBER_MAX_VALUE);
         QueryResult<OrderedSuperRows<String, Long, String, String>> queryResult = rangeSuperSlicesQuery.execute();
         OrderedSuperRows<String, Long, String, String> orderedRows = queryResult.get();

         for (SuperRow<String, Long, String, String> row : orderedRows) {
            List<HSuperColumn<Long, String, String>> superColumns = row.getSuperSlice().getSuperColumns();
            if (superColumns != null && !superColumns.isEmpty()) {
               result.add(row.getKey());
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         if (e.getCause() != null) {
            e.getCause().printStackTrace();
         }
      }
      return result;
   }

}
