package org.ebayopensource.turmeric.monitoring.aggregation.error.reader;

import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class ErrorsByCategoryReader.
 */
public class ErrorsByCategoryReader extends AbstractErrorCountsReader {

   /**
    * Instantiates a new errors by category reader.
    *
    * @param startTime the start time
    * @param endTime the end time
    * @param realtimeCluster the realtime cluster
    */
   public ErrorsByCategoryReader(Date startTime, Date endTime, CassandraConnectionInfo realtimeCluster) {
      super(startTime, endTime, realtimeCluster);
      columnFamilyName = "ErrorCountsByCategory";
   }
}
