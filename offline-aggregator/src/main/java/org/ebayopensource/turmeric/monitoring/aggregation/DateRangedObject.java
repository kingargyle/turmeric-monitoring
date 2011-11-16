package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.Date;

public class DateRangedObject {

   /** The start time. */
   protected Date startTime;
   /** The end time. */
   protected Date endTime;

   public DateRangedObject(Date startTime, Date endTime) {
      this.startTime = startTime;
      this.endTime = endTime;
   }

}