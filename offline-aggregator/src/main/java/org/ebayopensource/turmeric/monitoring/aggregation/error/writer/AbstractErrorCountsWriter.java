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

public class AbstractErrorCountsWriter<KeyType> extends CassandraObject implements CassandraDataWriter<KeyType> {

   public AbstractErrorCountsWriter(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
   }

   protected String columnFamilyName;

   /**
    * {@inheritDoc}
    */
   @Override
   public void writeData(Map<KeyType, AggregationData<KeyType>> data) {
      Mutator<String> mutator = HFactory.createMutator(connectionInfo.getKeyspace(), STR_SERIALIZER);
      for (Entry<KeyType, AggregationData<KeyType>> entry : data.entrySet()) {
         AggregationData<KeyType> dataRow = entry.getValue();
         Map<Object, Object> columns = dataRow.getColumns();
         for (Entry<Object, Object> column : columns.entrySet()) {
            HColumn<Long, String> hColumn = HFactory.createColumn((Long) column.getKey(), (String) column.getValue(),
                     LONG_SERIALIZER, STR_SERIALIZER);
            mutator.addInsertion((String) dataRow.getKey(), columnFamilyName, hColumn);
         }
      }
      mutator.execute();
   }

}
