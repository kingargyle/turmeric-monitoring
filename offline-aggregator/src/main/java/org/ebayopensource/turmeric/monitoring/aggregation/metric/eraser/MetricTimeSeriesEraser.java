package org.ebayopensource.turmeric.monitoring.aggregation.metric.eraser;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraDataEraser;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraObject;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

public class MetricTimeSeriesEraser extends CassandraObject implements CassandraDataEraser<String> {

   private final String columnFamilyName = "MetricTimeSeries";

   public MetricTimeSeriesEraser(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);

   }

   @Override
   public void eraseData(Map<String, AggregationData<String>> data) {
      Mutator<String> metricTimeSeriesMutator = HFactory.createMutator(connectionInfo.getKeyspace(), STR_SERIALIZER);
      for (Entry<String, AggregationData<String>> rowEntry : data.entrySet()) {
         AggregationData<String> row = rowEntry.getValue();
         for (Entry<Object, Object> columnEntry : row.getColumns().entrySet()) {
            Long timestamp = (Long) columnEntry.getKey();
            metricTimeSeriesMutator.addSuperDelete(rowEntry.getKey(), columnFamilyName, timestamp, LONG_SERIALIZER);
         }

      }
      metricTimeSeriesMutator.execute();
   }
}
