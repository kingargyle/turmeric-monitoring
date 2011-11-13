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
import org.ebayopensource.turmeric.monitoring.aggregation.metric.reader.MetricValuesByIpAndDateReader;
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

public class MetricValuesByIpAndDateReaderTest extends BaseTest {
   protected String[] keysToFind;

   private String[] createKeysToFind(String ipAdress, long timestamp) {
      // manuelchinea-desktop|ServiceAdminName1|Operation1|ConsumerName1|SoaFwk.Op.Time.Total|20|true,
      // manuelchinea-desktop|ServiceAdminName1|Operation1|anotherConsumer|SoaFwk.Op.Time.Total|20|true,
      // manuelchinea-desktop|ServiceAdminName1|Operation1|SoaFwk.Op.Time.Total|20|true
      return new String[] { ipAdress + "|" + new SimpleDateFormat("ddMMyyyy").format(new Date(timestamp)) };
   }

   @Override
   @Before
   public void setUp() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      keysToFind = createKeysToFind(getIPAddress(), threeMinutesAgo);

      createTestData();

      startTime = new Date(threeMinutesAgo);
      endTime = new Date(now);
      reader = new MetricValuesByIpAndDateReader(startTime, endTime, connectionInfo);
   }

   private Object[] createKeysToFindNoConsumer(String serverName, String srvcAdminName, String opName,
            String metricName, int aggregationPeriod, boolean isServerSide) {
      return new String[] { serverName + "|" + srvcAdminName + "|" + opName + "|" + metricName + "|"
               + aggregationPeriod + "|" + isServerSide };
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
      Map<String, AggregationData<Long>> readData = reader.readData();
      assertNotNull("readData should not be null", readData);
      for (int i = 0; i < keysToFind.length; i++) {
         AggregationData<Long> rowData = readData.get(keysToFind[i]);
         assertNotNull("the readData must not contain null data elements.", rowData);
         for (Entry<Object, Object> column : rowData.getColumns().entrySet()) {
            assertNotNull("the rowData must not contain null columns", column);
            Long timestamp = (Long) column.getKey();
            boolean isValidTimestamp = ((timestamp.equals(threeMinutesAgo) || timestamp.equals(twoMinutesAgo) || timestamp
                     .equals(oneMinuteAgo)) && !timestamp.equals(now));
            assertTrue("the timestamp must be one value beetwen " + threeMinutesAgo + " and " + oneMinuteAgo,
                     isValidTimestamp);
            Set metricValueKeys = (Set) column.getValue();
            assertNotNull(metricValueKeys);
            for (Object metricValueKey : metricValueKeys) {
               assertNotNull("There should not be null metric value keys", metricValueKey);
            }
         }
      }
   }
}
