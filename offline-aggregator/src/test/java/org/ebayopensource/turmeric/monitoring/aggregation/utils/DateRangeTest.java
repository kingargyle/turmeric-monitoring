package org.ebayopensource.turmeric.monitoring.aggregation.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class DateRangeTest {

   DateRangeHelper dateRange = null;

   @Test
   public void testGetDateRangeOneDayDifference() {
      Date startDate = createDateTime(new Date(), 2, 0, 0, 0);
      Date endDate = new Date(startDate.getTime() + DateRangeHelper.ONE_DAY_IN_MILIS);
      dateRange = new DateRangeHelper(startDate, endDate);
      Date[] result = dateRange.getMidnightDates();
      assertNotNull(result);
      assertEquals(2, result.length);
      assertDateEquals(createDateTime(startDate, 0, 0, 0, 0), result[0]);
      assertDateEquals(createDateTime(endDate, 0, 0, 0, 0), result[1]);
   }

   private void assertDateEquals(Date date1, Date date2) {
      Calendar cal1 = Calendar.getInstance();
      cal1.setTime(date1);

      Calendar cal2 = Calendar.getInstance();
      cal2.setTime(date2);

      assertEquals("Year must be the same", cal1.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
      assertEquals("Month must be the same", cal1.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
      assertEquals("Day of year must be the same", cal1.get(Calendar.DAY_OF_YEAR), cal2.get(Calendar.DAY_OF_YEAR));
      assertEquals("Hour of day must be the same", cal1.get(Calendar.HOUR_OF_DAY), cal2.get(Calendar.HOUR_OF_DAY));
      assertEquals("Minute must be the same", cal1.get(Calendar.MINUTE), cal2.get(Calendar.MINUTE));
      assertEquals("Second must be the same", cal1.get(Calendar.SECOND), cal2.get(Calendar.SECOND));
      assertEquals("Milisecond must be the same", cal1.get(Calendar.MILLISECOND), cal2.get(Calendar.MILLISECOND));
   }

   private Date createDateTime(Date initialDate, int hour, int minute, int seconds, int milisecs) {
      Calendar startCalendar = Calendar.getInstance();
      startCalendar.setTimeInMillis(initialDate.getTime());
      startCalendar.set(Calendar.HOUR_OF_DAY, hour);
      startCalendar.set(Calendar.MINUTE, minute);
      startCalendar.set(Calendar.SECOND, seconds);
      startCalendar.set(Calendar.MILLISECOND, milisecs);
      return startCalendar.getTime();
   }

   @Test
   public void testGetMidnightDates3DaysDifference() {
      Date startDate = createDateTime(new Date(), 2, 0, 0, 0);// 2 am
      Date dayInbetween = new Date(startDate.getTime() + (DateRangeHelper.ONE_DAY_IN_MILIS * 1));
      Date endDate = new Date(startDate.getTime() + (DateRangeHelper.ONE_DAY_IN_MILIS * 2));
      dateRange = new DateRangeHelper(startDate, endDate);
      Date[] result = dateRange.getMidnightDates();
      assertNotNull(result);
      assertEquals(3, result.length);
      assertDateEquals(createDateTime(startDate, 0, 0, 0, 0), result[0]);
      assertDateEquals(createDateTime(dayInbetween, 0, 0, 0, 0), result[1]);
      assertDateEquals(createDateTime(endDate, 0, 0, 0, 0), result[2]);
   }

   @Test
   public void testGetMidnightDatesLessThanOneDayDifference() {
      Date startDate = createDateTime(new Date(), 2, 0, 0, 0);
      Date endDate = new Date(startDate.getTime() + (1000));
      dateRange = new DateRangeHelper(startDate, endDate);
      Date[] result = dateRange.getMidnightDates();
      assertNotNull(result);
      assertEquals(1, result.length);
      assertDateEquals(createDateTime(startDate, 0, 0, 0, 0), result[0]);
      assertDateEquals(createDateTime(endDate, 0, 0, 0, 0), result[0]);
   }

   @Test
   public void testGetFomatedStringRangeOneDayDifference() {
      Date startDate = createDateTime(new Date(), 2, 0, 0, 0);
      Date endDate = new Date(startDate.getTime() + DateRangeHelper.ONE_DAY_IN_MILIS);
      dateRange = new DateRangeHelper(startDate, endDate);
      SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
      String[] result = dateRange.getFomatedStringRange(formatter);
      assertNotNull(result);
      assertEquals(2, result.length);
      assertEquals(formatter.format(startDate), result[0]);
      assertEquals(formatter.format(endDate), result[1]);
   }

   @Test
   public void testHourlyDateRange3HoursDifference() {
      Date startDate = createDateTime(new Date(), 6, 37, 10, 0);// 6:37 am
      Date endDate = new Date(startDate.getTime() + (DateRangeHelper.ONE_HOUR_IN_MILIS * 2));// 8:37 am
      dateRange = new DateRangeHelper(startDate, endDate);
      Date[] result = dateRange.getHourlyDateRange();
      assertNotNull(result);
      assertEquals(3, result.length);
      assertDateEquals(createDateTime(startDate, 6, 0, 0, 0), result[0]);
      assertDateEquals(createDateTime(startDate, 7, 0, 0, 0), result[1]);
      assertDateEquals(createDateTime(endDate, 8, 0, 0, 0), result[2]);
   }

   @Test
   public void testHourlyDateRangeLessThan1Hour() {
      Date startDate = createDateTime(new Date(), 6, 59, 59, 0);// 6:59:59:0 am
      Date endDate = new Date(startDate.getTime() + 900);// 6:59:59:0 am + 900 milisecs
      dateRange = new DateRangeHelper(startDate, endDate);
      Date[] result = dateRange.getHourlyDateRange();
      assertNotNull(result);
      assertEquals(1, result.length);
      assertDateEquals(createDateTime(startDate, 6, 0, 0, 0), result[0]);
   }

   @Test
   public void testDateRange() {
      Date startDate = createDateTime(new Date(), 6, 59, 59, 0);// 6:59 am
      Date endDate = new Date(startDate.getTime() + (DateRangeHelper.ONE_HOUR_IN_MILIS * 5));// 11:59 am
      dateRange = new DateRangeHelper(startDate, endDate);
      Date[] result = dateRange.getDateRange(DateRangeHelper.ONE_HOUR_IN_MILIS * 3);
      assertNotNull(result);
      assertEquals(2, result.length);
      assertDateEquals(createDateTime(startDate, 6, 0, 0, 0), result[0]);
      assertDateEquals(createDateTime(startDate, 9, 0, 0, 0), result[1]);
   }
}
