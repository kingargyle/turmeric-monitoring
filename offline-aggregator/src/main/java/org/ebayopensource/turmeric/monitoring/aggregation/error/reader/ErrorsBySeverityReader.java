package org.ebayopensource.turmeric.monitoring.aggregation.error.reader;

import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class ErrorsBySeverityReader.
 */
public class ErrorsBySeverityReader extends AbstractErrorCountsReader {

   /**
    * Instantiates a new errors by severity reader.
    *
    * @param startTime the start time
    * @param endTime the end time
    * @param connectionInfo the connection info
    */
   public ErrorsBySeverityReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
      columnFamilyName = "ErrorCountsBySeverity";
   }

}
