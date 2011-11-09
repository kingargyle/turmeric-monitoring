package org.ebayopensource.turmeric.monitoring.aggregation.error.writer;

import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class ErrorsByCategoryWriter.
 */
public class ErrorsByCategoryWriter extends AbstractErrorCountsWriter<String> {

   /**
    * Instantiates a new errors by category writer.
    * 
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @param connectionInfo
    *           the connection info
    */
   public ErrorsByCategoryWriter(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
      columnFamilyName = "ErrorCountsByCategory";
   }

}
