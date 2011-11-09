package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.Date;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;

// TODO: Auto-generated Javadoc
/**
 * The Class CassandraObject.
 * 
 * @param <KeyType>
 *           the generic type
 */
public class CassandraObject<KeyType> {

   /**
    * Instantiates a new cassandra object.
    * 
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @param connectionInfo
    *           the connection info
    */
   public CassandraObject(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super();
      this.startTime = startTime;
      this.endTime = endTime;
      this.connectionInfo = connectionInfo;
   }

   /** The Constant ROWS_NUMBER_MAX_VALUE. */
   protected static final int ROWS_NUMBER_MAX_VALUE = 20000000;

   /** The start time. */
   protected Date startTime;

   /** The end time. */
   protected Date endTime;

   /** The connection info. */
   protected CassandraConnectionInfo connectionInfo;

   /** The Constant LONG_SERIALIZER. */
   protected static final LongSerializer LONG_SERIALIZER = LongSerializer.get();

   /** The Constant STR_SERIALIZER. */
   protected static final StringSerializer STR_SERIALIZER = StringSerializer.get();

}