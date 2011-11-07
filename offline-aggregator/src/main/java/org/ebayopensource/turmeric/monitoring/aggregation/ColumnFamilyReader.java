package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.Date;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;

public abstract class ColumnFamilyReader<KeyType> {
   protected static final int ROWS_NUMBER_MAX_VALUE = 20000000;
   protected Date startTime;
   protected Date endTime;
   protected CassandraConnectionInfo connectionInfo;

   protected static final LongSerializer LONG_SERIALIZER = LongSerializer.get();
   protected static final StringSerializer STR_SERIALIZER = StringSerializer.get();

   public ColumnFamilyReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super();
      this.startTime = startTime;
      this.endTime = endTime;
      this.connectionInfo = connectionInfo;
   }

   public abstract Map<KeyType, List<Map<Object, Object>>> readData();

   public abstract List<KeyType> retrieveKeysInRange();

}
