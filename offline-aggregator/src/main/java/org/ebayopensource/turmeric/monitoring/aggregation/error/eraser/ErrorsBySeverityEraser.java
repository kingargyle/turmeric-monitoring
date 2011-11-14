package org.ebayopensource.turmeric.monitoring.aggregation.error.eraser;

import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;

public class ErrorsBySeverityEraser extends AbstractErrorCountsEraser {

   public ErrorsBySeverityEraser(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
      columnFamilyName = "ErrorCountsBySeverity";
   }

}
