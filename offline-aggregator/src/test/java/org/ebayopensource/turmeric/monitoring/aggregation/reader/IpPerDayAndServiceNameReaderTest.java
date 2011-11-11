package org.ebayopensource.turmeric.monitoring.aggregation.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.monitoring.aggregation.BaseTest;
import org.ebayopensource.turmeric.monitoring.aggregation.MetricValueAggregatorTestImpl;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.reader.IpPerDayAndServiceNameReader;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.SystemMetricDefs;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricCategory;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricClassifier;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricId;
import org.ebayopensource.turmeric.runtime.common.monitoring.MonitoringLevel;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.AverageMetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.LongSumMetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IpPerDayAndServiceNameReaderTest extends BaseTest {
   protected String[] keysToFind;

   private String[] createKeysToFind(Date date) {
      // manuelchinea-desktop|ServiceAdminName1|Operation1|ConsumerName1|SoaFwk.Op.Time.Total|20|true,
      // manuelchinea-desktop|ServiceAdminName1|Operation1|anotherConsumer|SoaFwk.Op.Time.Total|20|true,
      // manuelchinea-desktop|ServiceAdminName1|Operation1|SoaFwk.Op.Time.Total|20|true
      return new String[] { new SimpleDateFormat("ddMMyyyy").format(date) };
   }

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      keysToFind = createKeysToFind(new Date(threeMinutesAgo));
      createTestData();

      startTime = new Date(threeMinutesAgo);
      endTime = new Date(now);
      reader = new IpPerDayAndServiceNameReader(startTime, endTime, connectionInfo);
   }

   public void createTestData() throws ServiceException {
      MetricId metricId1 = new MetricId(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), srvcAdminName, opName);
      MetricValue metricValue1 = new AverageMetricValue(metricId1);
      MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      MetricId metricId2 = new MetricId(SystemMetricDefs.OP_TIME_TOTAL.getMetricName(), srvcAdminName,
               "anotherOperation");
      MetricValue metricValue2 = new AverageMetricValue(metricId2);
      MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      MetricId metricId3 = new MetricId(SystemMetricDefs.OP_ERR_TOTAL.getMetricName(), srvcAdminName,
               "anotherOperation");
      MetricValue metricValue3 = new LongSumMetricValue(metricId3);
      MetricValueAggregatorTestImpl aggregator3 = new MetricValueAggregatorTestImpl(metricValue3,
               MetricCategory.Timing, MonitoringLevel.NORMAL);

      MetricClassifier metricClassifier1 = new MetricClassifier(consumerName, "sourceDC1", "targetDC1");
      MetricClassifier metricClassifier2 = new MetricClassifier("anotherConsumer", "sourceDC1", "targetDC1");
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier2, 1L);
      aggregator2.update(metricClassifier2, 1L);
      aggregator3.update(metricClassifier2, 1L);
      List<MetricValueAggregator> aggregators = deepCopyAggregators(aggregator1, aggregator2, aggregator3);
      metricsStorageProvider.saveMetricSnapshot(fiveMinutesAgo, aggregators);

      // // now, the second call
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier2, 1L);
      aggregators = deepCopyAggregators(aggregator1);
      metricsStorageProvider.saveMetricSnapshot(threeMinutesAgo, aggregators);
      //
      // // now, at the third minute I do 5 calls for consumerName, 1 from "anotherConsumer"
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier2, 1L);
      // aggregator2.update(metricClassifier2, 17L);
      aggregators = deepCopyAggregators(aggregator1);
      metricsStorageProvider.saveMetricSnapshot(twoMinutesAgo, aggregators);
      //
      // // now, 4th call, 2 calls are made from consumerName, 1 from "anotherConsumer"
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier1, 1L);
      aggregator1.update(metricClassifier2, 1L);
      // aggregator2.update(metricClassifier2, 1L);
      aggregators = deepCopyAggregators(aggregator1);
      metricsStorageProvider.saveMetricSnapshot(oneMinuteAgo, aggregators);
   }

   @Test
   public void testRetrieveKeysInRange() throws ServiceException {

      List<String> keys = reader.retrieveKeysInRange();
      assertNotNull("keys must not be null", keys);
      assertEquals(keysToFind.length, keys.size());
      assertTrue("the key list must contain the values: " + Arrays.toString(keysToFind),
               keys.containsAll(Arrays.asList(keysToFind)));
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
   }

   public String getIPAddress() throws ServiceException {
      try {
         return InetAddress.getLocalHost().getCanonicalHostName();
      } catch (UnknownHostException x) {
         throw new ServiceException("Unkonwn host name", x);
      }
   }

   @Test
   public void testReadDataInRange() throws ServiceException {
      String ipAdress = getIPAddress();
      Map<String, AggregationData<Long>> readData = reader.readData();
      assertNotNull("readData should not be null", readData);
      for (int i = 0; i < keysToFind.length; i++) {
         AggregationData<Long> rowData = readData.get(keysToFind[i]);
         assertNotNull("the readData must not contain null data elements.", rowData);
         for (Entry<Object, Object> column : rowData.getColumns().entrySet()) {
            assertNotNull("the rowData must not contain null columns", column);
            String theServiceName = (String) column.getKey();
            assertEquals("The column name must be " + srvcAdminName, srvcAdminName, theServiceName);
            Set<String> ipAddresses = (Set) column.getValue();
            assertNotNull("The ip set must not be null", ipAddresses);
            assertEquals("The ip address list must contain 1 ip address", 1, ipAddresses.size());
            assertTrue("The ip address list must contain the ip address: " + ipAdress, ipAddresses.contains(ipAdress));
         }
      }
   }
}
