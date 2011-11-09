package org.ebayopensource.turmeric.monitoring.aggregation.error.writer;

import java.util.Map;

import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

public interface CassandraDataWriter<KeyType> {

   public abstract void writeData(Map<KeyType, AggregationData<KeyType>> data);

}