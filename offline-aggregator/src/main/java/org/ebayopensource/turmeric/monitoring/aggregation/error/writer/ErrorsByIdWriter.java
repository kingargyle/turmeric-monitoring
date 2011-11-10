package org.ebayopensource.turmeric.monitoring.aggregation.error.writer;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraDataWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraObject;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

// TODO: Auto-generated Javadoc
/**
 * The Class ErrorsByIdWriter.
 */
public class ErrorsByIdWriter extends CassandraObject implements CassandraDataWriter<Long> {

   /** The Constant columnFamilyName. */
   private static final String columnFamilyName = "ErrorsById";

   /**
    * Instantiates a new errors by id writer.
    * 
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @param connectionInfo
    *           the connection info
    */
   public ErrorsByIdWriter(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void writeData(Map<Long, AggregationData<Long>> data) {
      Mutator<Long> mutator = HFactory.createMutator(connectionInfo.getKeyspace(), LONG_SERIALIZER);
      for (Entry<Long, AggregationData<Long>> entry : data.entrySet()) {
         AggregationData<Long> dataRow = entry.getValue();
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
