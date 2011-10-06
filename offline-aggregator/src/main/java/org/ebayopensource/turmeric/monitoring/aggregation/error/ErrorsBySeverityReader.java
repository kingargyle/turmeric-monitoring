package org.ebayopensource.turmeric.monitoring.aggregation.error;

import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;

public class ErrorsBySeverityReader extends AbstractErrorCountsReader {

   public ErrorsBySeverityReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
      columnFamilyName = "ErrorCountsBySeverity";
   }

}
