package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.Map;

import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

public interface CassandraDataEraser<KeyType> {

   public void eraseData(Map<KeyType, AggregationData<KeyType>> data);

}
