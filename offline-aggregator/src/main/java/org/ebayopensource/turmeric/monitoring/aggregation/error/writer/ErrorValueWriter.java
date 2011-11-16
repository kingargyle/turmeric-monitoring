package org.ebayopensource.turmeric.monitoring.aggregation.error.writer;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraDataWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraObject;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

// TODO: Auto-generated Javadoc
/**
 * The Class ErrorValueWriter.
 */
public class ErrorValueWriter extends CassandraObject implements CassandraDataWriter<String> {

   /**
    * Instantiates a new error value writer.
    * 
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @param connectionInfo
    *           the connection info
    */
   public ErrorValueWriter(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
   }

   /** The Constant columnFamilyName. */
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
            if (column != null && column.getKey() != null && column.getValue() != null) {
               HColumn<String, Object> hColumn = HFactory.createColumn((String) column.getKey(), column.getValue(),
                        STR_SERIALIZER, SerializerTypeInferer.getSerializer(column.getValue()));
               mutator.addInsertion(dataRow.getKey(), columnFamilyName, hColumn);
            }
         }
      }
      mutator.execute();
   }

}
