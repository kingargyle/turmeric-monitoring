package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

public abstract class ColumnFamilyReader<KeyType> extends CassandraObject<KeyType> {
   public ColumnFamilyReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
   }

   public abstract Map<KeyType, AggregationData<KeyType>> readData();

   public abstract List<KeyType> retrieveKeysInRange();

}
