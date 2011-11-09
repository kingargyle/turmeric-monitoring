package org.ebayopensource.turmeric.monitoring.aggregation.error.writer;

import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;

public class ErrorsBySeverityWriter extends AbstractErrorCountsWriter<String> {

   public ErrorsBySeverityWriter(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
      columnFamilyName = "ErrorCountsBySeverity";
   }

}
