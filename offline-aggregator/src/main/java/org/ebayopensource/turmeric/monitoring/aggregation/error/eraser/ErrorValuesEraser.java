package org.ebayopensource.turmeric.monitoring.aggregation.error.eraser;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraDataEraser;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraObject;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

// TODO: Auto-generated Javadoc
/**
 * The Class ErrorValuesEraser.
 */
public class ErrorValuesEraser extends CassandraObject implements CassandraDataEraser<String> {

   /** The column family name. */
   private final String columnFamilyName = "ErrorValues";

   /**
    * Instantiates a new error values eraser.
    * 
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @param connectionInfo
    *           the connection info
    */
   public ErrorValuesEraser(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void eraseData(Map<String, AggregationData<String>> data) {
      Mutator<String> mutator = HFactory.createMutator(connectionInfo.getKeyspace(), STR_SERIALIZER);
      for (Entry<String, AggregationData<String>> entry : data.entrySet()) {
         AggregationData<String> dataRow = entry.getValue();
         Map<Object, Object> columns = dataRow.getColumns();
         for (Entry<Object, Object> column : columns.entrySet()) {
            // HColumn<String, Object> hColumn = HFactory.createColumn((String) column.getKey(), column.getValue(),
            // STR_SERIALIZER, SerializerTypeInferer.getSerializer(column.getValue()));
            // mutator.addInsertion(dataRow.getKey(), columnFamilyName, hColumn);
            mutator.addDeletion(dataRow.getKey(), columnFamilyName, (String) column.getKey(), STR_SERIALIZER);
         }
      }
      mutator.execute();
   }
}