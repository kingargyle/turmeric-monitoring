package org.ebayopensource.turmeric.monitoring.aggregation.error.writer;

import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;

public class ErrorsByCategoryWriter extends AbstractErrorCountsWriter<String> {

   public ErrorsByCategoryWriter(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
      columnFamilyName = "ErrorCountsByCategory";
   }

}
