package org.ebayopensource.turmeric.monitoring.aggregation.error;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.ColumnFamilyReader;

public abstract class AbstractErrorCountsReader extends ColumnFamilyReader {

   protected String columnFamilyName;

   public AbstractErrorCountsReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);

   }

   @Override
   public List<String> retrieveKeysInRange() {
      List<String> rowKeys = new ArrayList<String>();

      try {
         RangeSlicesQuery<String, Long, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(
                  connectionInfo.getKeyspace(), StringSerializer.get(), LongSerializer.get(), StringSerializer.get());
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
