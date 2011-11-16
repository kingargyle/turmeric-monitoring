package org.ebayopensource.turmeric.monitoring.aggregation.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class DateRange.
 */
public class DateRangeHelper {

   /** The start date. */
   private final Date startDate;

   /** The end date. */
   private final Date endDate;

   /** The Constant ONE_DAY_IN_MILIS. */
   public static final int ONE_DAY_IN_MILIS = 86400000;

   /** The Constant ONE_DAY_IN_MILIS. */
   public static final int ONE_HOUR_IN_MILIS = 3600000;

   /**
    * Instantiates a new date range.
    * 
    * @param startDate
    *           the start date
    * @param endDate
    *           the end date
    */
   public DateRangeHelper(Date startDate, Date endDate) {
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
      Date[] dateRange = getMidnightDates();
      String result[] = new String[dateRange.length];
      for (int i = 0; i < dateRange.length; i++) {
         result[i] = formatter.format(dateRange[i]);
      }
      return result;
   }

   /**
    * Gets the midnight dates for the 2 dates (startDate, endDate).
    * 
    * @return the date range
    */
   public Date[] getMidnightDates() {
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

   /**
    * Gets the hourly date range for the 2 dates, offset to 0 minutes and 0 seconds. If the first time is 6:37pm and the
    * second time is 7:20pm, it returns and array of 2 dates: 6pm and 7 pm.
    * 
    * @return the hourly date range.
    */
   public Date[] getHourlyDateRange() {
      return getDateRange(ONE_HOUR_IN_MILIS);
   }

   /**
    * Gets the date range for the 2 dates, offset to 0 minutes and 0 seconds. If the first time is 6:37pm and the second
    * time is 7:20pm, it returns and array of (endDateResetInMilis - startDateResetInMilis/diffInMilis) date elements.
    * If the diffInMilis is 3600000 then the first element in the array will be 6pm and the second would be 7pm.
    * 
    * @param diffInMilis
    *           the diff in milis
    * @return the date range
    */
   public Date[] getDateRange(int diffInMilis) {
      Date[] result = null;

      // round them to the midnight date
      Calendar startCalendar = Calendar.getInstance();
      startCalendar.setTime(startDate);
      startCalendar.set(Calendar.MINUTE, 0);
      startCalendar.set(Calendar.SECOND, 0);
      startCalendar.set(Calendar.MILLISECOND, 0);
      long startDateResetInMilis = startCalendar.getTimeInMillis();

      Calendar endCalendar = Calendar.getInstance();
      endCalendar.setTime(endDate);
      endCalendar.set(Calendar.MINUTE, 0);
      endCalendar.set(Calendar.SECOND, 0);
      endCalendar.set(Calendar.MILLISECOND, 0);
      long endDateResetInMilis = endCalendar.getTimeInMillis();
      long diff = endDateResetInMilis - startDateResetInMilis;
      int dateRangeQuantity = (int) (diff / diffInMilis) + 1;
      result = new Date[dateRangeQuantity];
      for (int i = 0; i < dateRangeQuantity; i++) {
         result[i] = new Date(startDateResetInMilis + (diffInMilis * i));
      }
      return result;
   }
}
