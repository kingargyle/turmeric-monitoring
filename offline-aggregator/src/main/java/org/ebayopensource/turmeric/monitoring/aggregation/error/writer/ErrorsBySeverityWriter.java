package org.ebayopensource.turmeric.monitoring.aggregation.error.writer;

import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class ErrorsBySeverityWriter.
 */
public class ErrorsBySeverityWriter extends AbstractErrorCountsWriter<String> {

   /**
    * Instantiates a new errors by severity writer.
    * 
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @param connectionInfo
    *           the connection info
    */
   public ErrorsBySeverityWriter(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
      columnFamilyName = "ErrorCountsBySeverity";
   }

}
