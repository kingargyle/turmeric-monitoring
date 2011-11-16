package org.ebayopensource.turmeric.monitoring.aggregation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;
import org.ebayopensource.turmeric.monitoring.aggregation.utils.DateRangeHelper;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DataAggregatorTest extends BaseTest {
   DataAggregator aggregator = null;

   private Map<String, AggregationData<String>> oldMetricTimeSeriesData;

   private Map<String, AggregationData<String>> oldMetricValuesData;

   private int oldAggregationPeriod, newAggregationPeriod;

   private Date startDate, endDate;

   private DateRangeHelper dateRangeHelper;

   private Date[] dateRange;

   private int aggregationDiff;

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      startDate = new Date();
      endDate = new Date(startDate.getTime() + (DateRangeHelper.ONE_HOUR_IN_MILIS * 1));// 1 hour later
      dateRangeHelper = new DateRangeHelper(startDate, endDate);

      oldAggregationPeriod = 60;// each 60 secs
      newAggregationPeriod = 3600;// each hour
      aggregationDiff = newAggregationPeriod / oldAggregationPeriod;
      dateRange = dateRangeHelper.getDateRange(aggregationDiff);
      createData();
      aggregator = new DataAggregator(startDate, endDate, oldMetricTimeSeriesData, oldMetricValuesData,
               oldAggregationPeriod, newAggregationPeriod);
   }

   private void createData() {
      long startDateInMilis = startDate.getTime();
      long endDateInMilis = endDate.getTime();
      oldMetricValuesData = new HashMap<String, AggregationData<String>>();
      oldMetricTimeSeriesData = new HashMap<String, AggregationData<String>>();
      String metricTimeSeriesKey = serverName + "|" + srvcAdminName + "|" + opName + "|" + consumerName + "|"
               + oldAggregationPeriod + "|true";
      AggregationData<String> metricTimeSeriesData = new AggregationData<String>(metricTimeSeriesKey);
      while (startDateInMilis <= endDateInMilis) {
         Set<String> metricValueKeys = new HashSet<String>();
         for (int i = 0; i < 7; i++) {
            String metricValueKey = serverName + "|" + srvcAdminName + "|" + opName + "|" + consumerName + "|"
                     + "someMetricName" + i + "|" + startDateInMilis;
            AggregationData<String> metricValueData = createMetricValueData(metricValueKey);
            metricValueKeys.add(metricValueKey);
            oldMetricValuesData.put(metricValueData.getKey(), metricValueData);
         }
         metricTimeSeriesData.addColumn(startDateInMilis, metricValueKeys);
         startDateInMilis += oldAggregationPeriod * 1000;
      }
      oldMetricTimeSeriesData.put(metricTimeSeriesData.getKey(), metricTimeSeriesData);
      return;
   }

   private AggregationData<String> createMetricValueData(String metricValueKey) {
      AggregationData<String> metricValueData = new AggregationData<String>(metricValueKey);
      metricValueData.addColumn("metricDefValue1Long", new Long(3));
      metricValueData.addColumn("metricDefValue2Double", new Double(1.5));
      metricValueData.addColumn("metricDefValue3Long", new Long(11));
      metricValueData.addColumn("metricDefValue4Double", new Double(31));
      return metricValueData;
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
      aggregator = null;
   }

   @Test
   public void testAggregate() {
      aggregator.aggregate();
      Map<String, AggregationData<String>> aggregatedMetricTimeSeriesData = aggregator
               .getAggregatedMetricTimeSeriesData();
      assertNotNull(aggregatedMetricTimeSeriesData);

      Map<String, AggregationData<String>> aggregatedMetricValuesData = aggregator.getAggregatedMetricValuesData();
      assertNotNull(aggregatedMetricValuesData);
   }

   @Test
   public void testGetAggregatedMetricTimeSeriesData() {
      fail("Not yet implemented");
   }

   @Test
   public void testGetAggregatedMetricValuesData() {
      fail("Not yet implemented");
   }

   @Test
   public void testAggregateColumnValues() {
      fail("Not yet implemented");
   }

   @Test
   public void testGetAllMetricValues() {
      fail("Not yet implemented");
   }

   @Test
   public void testCreateMetricTimeSeriesKey() {
      String oldMetricTimeSeriesKey = serverName + "|" + srvcAdminName + "|" + opName + "|" + consumerName + "|"
               + oldAggregationPeriod + "|true";
      String expectedMetricTimeSeriesKey = serverName + "|" + srvcAdminName + "|" + opName + "|" + consumerName + "|"
               + newAggregationPeriod + "|true";
      String theResult = aggregator.createMetricTimeSeriesKey(oldMetricTimeSeriesKey);
      assertNotNull(theResult);
      assertEquals(expectedMetricTimeSeriesKey, theResult);
   }

   @Test
   public void testCreateMetricValueKey() {
      Date someDate = new Date();
      Date someOtherDate = new Date(someDate.getTime() + 1000);
      String metricValueKey = serverName + "|" + srvcAdminName + "|" + opName + "|" + consumerName + "|"
               + "someMetricName|" + someDate.getTime();
      String expectedMetricValueKey = serverName + "|" + srvcAdminName + "|" + opName + "|" + consumerName + "|"
               + "someMetricName|" + someOtherDate.getTime();
      String theResult = aggregator.createMetricValueKey(metricValueKey, someOtherDate);
      assertNotNull(theResult);
      assertEquals(expectedMetricValueKey, theResult);
   }

   @Test
   public void testaAgregateLongColumnValues() {
      Object oldColumnValue = new Long(1);
      Object aggregatedValue = new Long(2);
      Object theResult = aggregator.aggregateColumnValues(aggregatedValue, oldColumnValue);
      assertNotNull(theResult);
      assertEquals(new Long(3), theResult);
   }

   @Test
   public void testaAgregateDoubleColumnValues() {
      Object oldColumnValue = new Double(11.4);
      Object aggregatedValue = new Double(22.78);
      Object theResult = aggregator.aggregateColumnValues(aggregatedValue, oldColumnValue);
      assertNotNull(theResult);
      assertEquals(new Double(34.18), theResult);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testaAgregateInvalidObjectColumnValues() {
      Object oldColumnValue = new Integer(1);
      Object aggregatedValue = new Integer(2);
      Object theResult = aggregator.aggregateColumnValues(aggregatedValue, oldColumnValue);
      fail("The test should fail because integer is not a valid column value object");
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

}
