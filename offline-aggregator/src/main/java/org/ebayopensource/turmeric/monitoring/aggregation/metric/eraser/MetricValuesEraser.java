package org.ebayopensource.turmeric.monitoring.aggregation.metric.eraser;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraDataEraser;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraObject;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

public class MetricValuesEraser extends CassandraObject implements CassandraDataEraser<String> {

   private final String columnFamilyName = "MetricValues";

   public MetricValuesEraser(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);

   }

   @Override
   public void eraseData(Map<String, AggregationData<String>> data) {
      Mutator<String> mutator = HFactory.createMutator(connectionInfo.getKeyspace(), STR_SERIALIZER);
      for (Entry<String, AggregationData<String>> entry : data.entrySet()) {
         AggregationData<String> dataRow = entry.getValue();
         mutator.addDeletion(dataRow.getKey(), columnFamilyName, null, ObjectSerializer.get());
      }
      mutator.execute();
   }

}
