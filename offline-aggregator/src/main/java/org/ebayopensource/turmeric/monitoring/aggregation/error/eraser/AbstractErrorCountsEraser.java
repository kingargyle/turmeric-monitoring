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

public class AbstractErrorCountsEraser extends CassandraObject implements CassandraDataEraser<String> {

   protected String columnFamilyName;

   public AbstractErrorCountsEraser(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);

   }

   @Override
   public void eraseData(Map<String, AggregationData<String>> data) {
      Mutator<String> mutator = HFactory.createMutator(connectionInfo.getKeyspace(), STR_SERIALIZER);
      for (Entry<String, AggregationData<String>> entry : data.entrySet()) {
         AggregationData<String> dataRow = entry.getValue();
         Map<Object, Object> columns = dataRow.getColumns();
         for (Entry<Object, Object> column : columns.entrySet()) {
            mutator.addDeletion(dataRow.getKey(), columnFamilyName, (Long) column.getKey(), LONG_SERIALIZER);
         }
      }
      mutator.execute();
   }

}
