package org.ebayopensource.turmeric.monitoring.aggregation.metric.writer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraDataWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraObject;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

// TODO: Auto-generated Javadoc
/**
 * The Class MetricTimeSeriesWriter.
 */
public class ServiceConsumerByIpWriter extends CassandraObject implements CassandraDataWriter<String> {

   private static final String columnFamilyName = "ServiceConsumerByIp";

   /**
    * Instantiates a new metric time series writer.
    * 
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @param connectionInfo
    *           the connection info
    */
   public ServiceConsumerByIpWriter(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void writeData(Map<String, AggregationData<String>> data) {
      Mutator<String> mutator = HFactory.createMutator(connectionInfo.getKeyspace(), STR_SERIALIZER);
      for (Entry<String, AggregationData<String>> rowEntry : data.entrySet()) {
         AggregationData<String> row = rowEntry.getValue();
         for (Entry<Object, Object> columnEntry : row.getColumns().entrySet()) {
            String serviceName = (String) columnEntry.getKey();
            List<HColumn<String, String>> metricTimeSeriesColumn = new ArrayList<HColumn<String, String>>();
            Set ipAdresses = (Set) columnEntry.getValue();
            for (Object ipAdress : ipAdresses) {
               metricTimeSeriesColumn.add(HFactory.createColumn((String) ipAdress, "", STR_SERIALIZER, STR_SERIALIZER));
            }
            HSuperColumn<String, String, String> metricTimeSeriesSuperColumn = HFactory.createSuperColumn(serviceName,
                     metricTimeSeriesColumn, STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER);
            mutator.addInsertion(row.getKey(), columnFamilyName, metricTimeSeriesSuperColumn);
         }

      }
      mutator.execute();
   }
}
