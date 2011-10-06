package org.ebayopensource.turmeric.monitoring.aggregation.error;

import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;

public class ErrorsByCategoryReader extends AbstractErrorCountsReader {

   public ErrorsByCategoryReader(Date startTime, Date endTime, CassandraConnectionInfo realtimeCluster) {
      super(startTime, endTime, realtimeCluster);
      columnFamilyName = "ErrorCountsByCategory";
   }
}
