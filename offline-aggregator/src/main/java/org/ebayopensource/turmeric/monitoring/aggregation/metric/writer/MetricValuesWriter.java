package org.ebayopensource.turmeric.monitoring.aggregation.metric.writer;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraDataWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraObject;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

public class MetricValuesWriter extends CassandraObject implements CassandraDataWriter<String> {

   private static final String columnFamilyName = "MetricValues";

   public MetricValuesWriter(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);

   }

   @Override
   public void writeData(Map<String, AggregationData<String>> data) {
      Mutator<String> mutator = HFactory.createMutator(connectionInfo.getKeyspace(), STR_SERIALIZER);
      for (Entry<String, AggregationData<String>> entry : data.entrySet()) {
         AggregationData<String> dataRow = entry.getValue();
         Map<Object, Object> columns = dataRow.getColumns();
         for (Entry<Object, Object> column : columns.entrySet()) {
            HColumn<String, Object> hColumn = HFactory.createColumn((String) column.getKey(), column.getValue(),
                     STR_SERIALIZER, ObjectSerializer.get());
            mutator.addInsertion(dataRow.getKey(), columnFamilyName, hColumn);
         }
      }
      mutator.execute();
   }

}
