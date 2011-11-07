package org.ebayopensource.turmeric.monitoring.aggregation.error.reader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public abstract class AbstractErrorCountsReader extends ColumnFamilyReader<String> {

   @Override
   public Map<String, List<Map<Object, Object>>> readData() {
      Map<String, List<Map<Object, Object>>> result = new HashMap<String, List<Map<Object, Object>>>();
      try {

         List<String> keysToRead = retrieveKeysInRange();
         MultigetSliceQuery<String, Long, String> multigetSliceQuery = HFactory.createMultigetSliceQuery(
                  connectionInfo.getKeyspace(), STR_SERIALIZER, LONG_SERIALIZER, STR_SERIALIZER);
         multigetSliceQuery.setColumnFamily(columnFamilyName);
         multigetSliceQuery.setKeys(keysToRead);
         multigetSliceQuery.setRange(startTime.getTime(), endTime.getTime(), false, ROWS_NUMBER_MAX_VALUE);
         QueryResult<Rows<String, Long, String>> queryResult = multigetSliceQuery.execute();
         if (queryResult != null) {
            for (Row<String, Long, String> row : queryResult.get()) {
               for (HColumn<Long, String> column : row.getColumnSlice().getColumns()) {
                  Map<Object, Object> rowMap = new HashMap<Object, Object>();
                  rowMap.put(column.getName(), column.getValue());
                  if (result.get(row.getKey()) == null) {
                     result.put(row.getKey(), new ArrayList<Map<Object, Object>>());
                  }
                  result.get(row.getKey()).add(rowMap);
               }
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

   protected String columnFamilyName;

   public AbstractErrorCountsReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);

   }

   @Override
   public List<String> retrieveKeysInRange() {
      List<String> rowKeys = new ArrayList<String>();

      try {
         RangeSlicesQuery<String, Long, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(
                  connectionInfo.getKeyspace(), STR_SERIALIZER, LONG_SERIALIZER, STR_SERIALIZER);
         rangeSlicesQuery.setColumnFamily(columnFamilyName);
         rangeSlicesQuery.setKeys(null, null);
         rangeSlicesQuery.setReturnKeysOnly();
         rangeSlicesQuery.setRange(startTime.getTime(), endTime.getTime(), false, ROWS_NUMBER_MAX_VALUE);
         rangeSlicesQuery.setRowCount(ROWS_NUMBER_MAX_VALUE);
         QueryResult<OrderedRows<String, Long, String>> result = rangeSlicesQuery.execute();
         OrderedRows<String, Long, String> orderedRows = result.get();

         for (Row<String, Long, String> row : orderedRows) {
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

}
