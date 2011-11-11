package org.ebayopensource.turmeric.monitoring.aggregation.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class DateRange.
 */
public class DateRange {

   /** The start date. */
   private final Date startDate;

   /** The end date. */
   private final Date endDate;

   /** The Constant ONE_DAY_IN_MILIS. */
   public static final long ONE_DAY_IN_MILIS = 86400000;

   /**
    * Instantiates a new date range.
    * 
    * @param startDate
    *           the start date
    * @param endDate
    *           the end date
    */
   public DateRange(Date startDate, Date endDate) {
      this.startDate = startDate;
      this.endDate = endDate;
   }

   /**
    * Gets the fomated string range.
    * 
    * @param formatter
    *           the formatter
    * @return the fomated string range
    */
   public String[] getFomatedStringRange(SimpleDateFormat formatter) {
      Date[] dateRange = getDateRange();
      String result[] = new String[dateRange.length];
      for (int i = 0; i < dateRange.length; i++) {
         result[i] = formatter.format(dateRange[i]);
      }
      return result;
   }

   /**
    * Gets the date range between 2 dates.
    * 
    * @return the date range
    */
   public Date[] getDateRange() {
      Date[] result = null;

      // round them to the midnight date
      Calendar startCalendar = Calendar.getInstance();
      startCalendar.setTime(startDate);
      startCalendar.set(Calendar.HOUR_OF_DAY, 0);
      startCalendar.set(Calendar.MINUTE, 0);
      startCalendar.set(Calendar.SECOND, 0);
      startCalendar.set(Calendar.MILLISECOND, 0);
      long startDateResetInMilis = startCalendar.getTimeInMillis();

      Calendar endCalendar = Calendar.getInstance();
      endCalendar.setTime(endDate);
      endCalendar.set(Calendar.HOUR_OF_DAY, 0);
      endCalendar.set(Calendar.MINUTE, 0);
      endCalendar.set(Calendar.SECOND, 0);
      endCalendar.set(Calendar.MILLISECOND, 0);
      long endDateResetInMilis = endCalendar.getTimeInMillis();
      long diff = endDateResetInMilis - startDateResetInMilis;
      int dateRangeQuantity = (int) (diff / ONE_DAY_IN_MILIS) + 1;
      result = new Date[dateRangeQuantity];
      for (int i = 0; i < dateRangeQuantity; i++) {
         result[i] = new Date(startDateResetInMilis + (ONE_DAY_IN_MILIS * i));
      }
      return result;
   }
}
