package org.ebayopensource.turmeric.monitoring.aggregation.error.writer;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraObject;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

public class ErrorValueWriter extends CassandraObject implements CassandraDataWriter<String> {

   public ErrorValueWriter(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
   }

   protected static final String columnFamilyName = "ErrorValues";

   /**
    * {@inheritDoc}
    */
   @Override
   public void writeData(Map<String, AggregationData<String>> data) {
      Mutator<String> mutator = HFactory.createMutator(connectionInfo.getKeyspace(), STR_SERIALIZER);
      for (Entry<String, AggregationData<String>> entry : data.entrySet()) {
         AggregationData<String> dataRow = entry.getValue();
         Map<Object, Object> columns = dataRow.getColumns();
         for (Entry<Object, Object> column : columns.entrySet()) {
            HColumn<String, String> hColumn = HFactory.createColumn((String) column.getKey(),
                     (String) column.getValue(), STR_SERIALIZER, STR_SERIALIZER);
            mutator.addInsertion(dataRow.getKey(), columnFamilyName, hColumn);
         }
      }
      mutator.execute();
   }

}
