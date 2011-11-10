package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.Map;

import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

// TODO: Auto-generated Javadoc
/**
 * The Interface CassandraDataWriter.
 * 
 * @param <KeyType>
 *           the generic type
 */
public interface CassandraDataWriter<KeyType> {

   /**
    * Write data.
    * 
    * @param data
    *           the data
    */
   public abstract void writeData(Map<KeyType, AggregationData<KeyType>> data);

}