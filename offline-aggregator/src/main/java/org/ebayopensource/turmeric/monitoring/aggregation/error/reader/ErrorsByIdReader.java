package org.ebayopensource.turmeric.monitoring.aggregation.error.reader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.ColumnFamilyReader;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

public class ErrorsByIdReader extends ColumnFamilyReader<Long> {

   private static final String columnFamilyName = "ErrorsById";

   public ErrorsByIdReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);

   }

   @Override
   public List<Long> retrieveKeysInRange() {
      List<Long> rowKeys = new ArrayList<Long>();

      try {
         RangeSlicesQuery<Long, String, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(
                  connectionInfo.getKeyspace(), LongSerializer.get(), StringSerializer.get(), StringSerializer.get());
         rangeSlicesQuery.setColumnFamily(columnFamilyName);
         rangeSlicesQuery.setKeys(null, null);
         rangeSlicesQuery.setReturnKeysOnly();
         rangeSlicesQuery.setRange("", "", false, ROWS_NUMBER_MAX_VALUE);
         rangeSlicesQuery.setRowCount(ROWS_NUMBER_MAX_VALUE);
         QueryResult<OrderedRows<Long, String, String>> result = rangeSlicesQuery.execute();
         OrderedRows<Long, String, String> orderedRows = result.get();

         for (Row<Long, String, String> row : orderedRows) {
            if (!row.getColumnSlice().getColumns().isEmpty()) {
               rowKeys.add(row.getKey());
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         if (e.getCause() != null) {
            e.getCause().printStackTrace();
         }
      }

      return rowKeys;
   }

   @Override
   public Map<Long, AggregationData<Long>> readData() {
      Map<Long, AggregationData<Long>> result = new HashMap<Long, AggregationData<Long>>();
      try {

         List<Long> keysToRead = retrieveKeysInRange();
         MultigetSliceQuery<Long, String, String> multigetSliceQuery = HFactory.createMultigetSliceQuery(
                  connectionInfo.getKeyspace(), LONG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER);
         multigetSliceQuery.setColumnFamily(columnFamilyName);
         multigetSliceQuery.setKeys(keysToRead);
         multigetSliceQuery.setRange("", "", false, ROWS_NUMBER_MAX_VALUE);
         QueryResult<Rows<Long, String, String>> queryResult = multigetSliceQuery.execute();
         if (queryResult != null) {
            for (Row<Long, String, String> row : queryResult.get()) {
               AggregationData<Long> rowData = new AggregationData<Long>(row.getKey());
               for (HColumn<String, String> column : row.getColumnSlice().getColumns()) {
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

}
