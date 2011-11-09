package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.Date;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;

public class CassandraObject<KeyType> {

   public CassandraObject(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super();
      this.startTime = startTime;
      this.endTime = endTime;
      this.connectionInfo = connectionInfo;
   }

   protected static final int ROWS_NUMBER_MAX_VALUE = 20000000;
   protected Date startTime;
   protected Date endTime;
   protected CassandraConnectionInfo connectionInfo;
   protected static final LongSerializer LONG_SERIALIZER = LongSerializer.get();
   protected static final StringSerializer STR_SERIALIZER = StringSerializer.get();

}