package org.ebayopensource.turmeric.monitoring.aggregation.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class DateRangeTest {

   DateRange dateRange = null;

   @Test
   public void testGetDateRangeOneDayDifference() {
      Date startDate = createStartDate();
      Date endDate = new Date(startDate.getTime() + DateRange.ONE_DAY_IN_MILIS);
      dateRange = new DateRange(startDate, endDate);
      Date[] result = dateRange.getDateRange();
      assertNotNull(result);
      assertEquals(2, result.length);
      assertDateEquals(startDate, result[0]);
      assertDateEquals(endDate, result[1]);
   }

   private void assertDateEquals(Date date1, Date date2) {
      Calendar cal1 = Calendar.getInstance();
      cal1.setTime(date1);

      Calendar cal2 = Calendar.getInstance();
      cal2.setTime(date2);

      assertEquals(cal1.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
      assertEquals(cal1.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
      assertEquals(cal1.get(Calendar.DAY_OF_YEAR), cal2.get(Calendar.DAY_OF_YEAR));
   }

   private Date createStartDate() {
      // round them to the midnight date
      Calendar startCalendar = Calendar.getInstance();
      startCalendar.setTimeInMillis(System.currentTimeMillis());
      startCalendar.set(Calendar.HOUR_OF_DAY, 2);
      startCalendar.set(Calendar.MINUTE, 0);
      startCalendar.set(Calendar.SECOND, 0);
      startCalendar.set(Calendar.MILLISECOND, 0);
      return startCalendar.getTime();
   }

   @Test
   public void testGetDateRange3DaysDifference() {
      Date startDate = createStartDate();
      Date endDate = new Date(startDate.getTime() + (DateRange.ONE_DAY_IN_MILIS * 2));
      dateRange = new DateRange(startDate, endDate);
      Date[] result = dateRange.getDateRange();
      assertNotNull(result);
      assertEquals(3, result.length);
      assertDateEquals(startDate, result[0]);
      assertDateEquals(endDate, result[2]);
   }

   @Test
   public void testGetDateRangeLessThanOneDayDifference() {
      Date startDate = createStartDate();
      Date endDate = new Date(startDate.getTime() + (1000));
      dateRange = new DateRange(startDate, endDate);
      Date[] result = dateRange.getDateRange();
      assertNotNull(result);
      assertEquals(1, result.length);
      assertDateEquals(startDate, result[0]);
      assertDateEquals(endDate, result[0]);
   }

   @Test
   public void testGetFomatedStringRangeOneDayDifference() {
      Date startDate = createStartDate();
      Date endDate = new Date(startDate.getTime() + DateRange.ONE_DAY_IN_MILIS);
      dateRange = new DateRange(startDate, endDate);
      SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
      String[] result = dateRange.getFomatedStringRange(formatter);
      assertNotNull(result);
      assertEquals(2, result.length);
      assertEquals(formatter.format(startDate), result[0]);
      assertEquals(formatter.format(endDate), result[1]);
   }

}
