package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

// TODO: Auto-generated Javadoc
/**
 * The Class ColumnFamilyReader.
 * 
 * @param <KeyType>
 *           the generic type
 */
public abstract class ColumnFamilyReader<KeyType> extends CassandraObject<KeyType> {

   /**
    * Instantiates a new column family reader.
    * 
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @param connectionInfo
    *           the connection info
    */
   public ColumnFamilyReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
   }

   /**
    * Read data.
    * 
    * @return the map
    */
   public abstract Map<KeyType, AggregationData<KeyType>> readData();

   /**
    * Retrieve keys in range.
    * 
    * @return the list
    */
   public abstract List<KeyType> retrieveKeysInRange();

}
