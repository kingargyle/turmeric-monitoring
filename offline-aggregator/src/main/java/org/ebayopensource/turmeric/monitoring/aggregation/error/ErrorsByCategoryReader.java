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

public class ErrorsByCategoryReader extends ColumnFamilyReader {

   private static final String columnFamilyName = "ErrorCountsByCategory";

   public ErrorsByCategoryReader(Date startTime, Date endTime, CassandraConnectionInfo realtimeCluster) {
      super(startTime, endTime, realtimeCluster);
   }

   @Override
   public List<String> retrieveKeysInRange() {
      List<String> rowKeys = new ArrayList<String>();

      RangeSlicesQuery<String, Long, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(
               this.connectionInfo.getKeyspace(), StringSerializer.get(), LongSerializer.get(), StringSerializer.get());
      rangeSlicesQuery.setColumnFamily(columnFamilyName);
      rangeSlicesQuery.setKeys(null, null);
      rangeSlicesQuery.setReturnKeysOnly();
      rangeSlicesQuery.setRange(null, null, false, Integer.MAX_VALUE);
      rangeSlicesQuery.setRowCount(Integer.MAX_VALUE);
      QueryResult<OrderedRows<String, Long, String>> result = rangeSlicesQuery.execute();
      OrderedRows<String, Long, String> orderedRows = result.get();
      
      for (Row<String, Long, String> row : orderedRows) {
         if (!row.getColumnSlice().getColumns().isEmpty()) {
            rowKeys.add(row.getKey());
         }
      }

      return rowKeys;
   }
}
