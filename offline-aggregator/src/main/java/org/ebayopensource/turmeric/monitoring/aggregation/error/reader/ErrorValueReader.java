package org.ebayopensource.turmeric.monitoring.aggregation.error.reader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
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

public class ErrorValueReader extends ColumnFamilyReader<String> {

   public ErrorValueReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
   }

   private static final String columnFamilyName = "ErrorValues";
   private static final String countsColumnFamilyName = "ErrorCountsByCategory";
   private static final List<String> longColumnNames = java.util.Arrays
            .asList("aggregationPeriod", "errorId", "tstamp");
   private static final List<String> intColumnNames = java.util.Arrays.asList("randomNumber");

   @Override
   public Map<String, AggregationData<String>> readData() {
      Map<String, AggregationData<String>> result = new HashMap<String, AggregationData<String>>();
      try {
         List<String> keysToRead = retrieveKeysInRange();
         MultigetSliceQuery<String, String, byte[]> multigetSliceQuery = HFactory.createMultigetSliceQuery(
                  connectionInfo.getKeyspace(), STR_SERIALIZER, STR_SERIALIZER, BytesArraySerializer.get());
         multigetSliceQuery.setColumnFamily(columnFamilyName);
         multigetSliceQuery.setKeys(keysToRead);
         multigetSliceQuery.setRange("", "", false, ROWS_NUMBER_MAX_VALUE);

         QueryResult<Rows<String, String, byte[]>> queryResult = multigetSliceQuery.execute();
         if (queryResult != null) {
            for (Row<String, String, byte[]> row : queryResult.get()) {
               AggregationData<String> rowData = new AggregationData<String>(row.getKey());
               for (HColumn<String, byte[]> column : row.getColumnSlice().getColumns()) {
                  byte[] byteValue = column.getValue();
                  if (byteValue != null && byteValue.length > 0) {
                     if (longColumnNames.contains(column.getName())) {
                        Long lngValue = (Long) SerializerTypeInferer.getSerializer(Long.class).fromBytes(byteValue);
                        rowData.addColumn(column.getName(), lngValue);
                     } else if (intColumnNames.contains(column.getName())) {
                        Integer intValue = (Integer) SerializerTypeInferer.getSerializer(Integer.class).fromBytes(
                                 byteValue);
                        rowData.addColumn(column.getName(), intValue);
                     } else {
                        String strValue = (String) SerializerTypeInferer.getSerializer(String.class).fromBytes(
                                 byteValue);
                        rowData.addColumn(column.getName(), strValue);
                     }
                  } else {
                     rowData.addColumn(column.getName(), null);
                  }
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

   @Override
   public List<String> retrieveKeysInRange() {
      List<String> result = new ArrayList<String>();
      try {
         RangeSlicesQuery<String, Long, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(
                  connectionInfo.getKeyspace(), STR_SERIALIZER, LONG_SERIALIZER, STR_SERIALIZER);
         rangeSlicesQuery.setColumnFamily(countsColumnFamilyName);
         rangeSlicesQuery.setKeys(null, null);
         // rangeSlicesQuery.setReturnKeysOnly();
         rangeSlicesQuery.setRange(startTime.getTime(), endTime.getTime(), false, ROWS_NUMBER_MAX_VALUE);
         rangeSlicesQuery.setRowCount(ROWS_NUMBER_MAX_VALUE);
         QueryResult<OrderedRows<String, Long, String>> queryResult = rangeSlicesQuery.execute();
         OrderedRows<String, Long, String> orderedRows = queryResult.get();

         for (Row<String, Long, String> row : orderedRows) {
            List<HColumn<Long, String>> columns = row.getColumnSlice().getColumns();
            if (columns != null && !columns.isEmpty()) {
               for (HColumn<Long, String> hColumn : columns) {
                  result.add(hColumn.getValue());
               }
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
