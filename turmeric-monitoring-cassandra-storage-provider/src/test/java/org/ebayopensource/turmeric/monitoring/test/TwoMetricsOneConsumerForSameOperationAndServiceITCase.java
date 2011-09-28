package org.ebayopensource.turmeric.monitoring.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.OrderedSuperRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.SuperRow;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.RangeSuperSlicesQuery;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.monitoring.utils.CassandraTestHelper;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricCategory;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricClassifier;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricId;
import org.ebayopensource.turmeric.runtime.common.monitoring.MonitoringLevel;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.AverageMetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.LongSumMetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TwoMetricsOneConsumerForSameOperationAndServiceITCase extends CassandraTestHelper {
    CassandraMetricsStorageProvider provider = null;

    @Before
    public void setUp() {
        provider = new CassandraMetricsStorageProvider();
        kspace = new HectorManager().getKeyspace(cluster_name, cassandra_node_ip, keyspace_name, "MetricIdentifier",
                        false, null, String.class);
        Map<String, String> options = createOptions();
        provider.init(options, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
    }

    private Map<String, String> createOptions() {
        Map<String, String> options = new HashMap<String, String>();
        options.put("hostName", cassandra_node_ip);
        options.put("keyspaceName", keyspace_name);
        options.put("clusterName", cluster_name);
        options.put("storeServiceMetrics", "false");
        return options;
    }

    @After
    public void tearDown() {
        cleanUpTestData();
        provider = null;
        kspace = null;
    }

    private void cleanUpTestData() {
        String[] columnFamilies = { "MetricTimeSeries", "MetricIdentifier", "MetricValues" };
        String[] superColumnFamilies = { "ServiceCallsByTime", "ServiceConsumerByIp", "ServiceOperationByIp" };

        for (String cf : columnFamilies) {
            RangeSlicesQuery<String, String, String> rq = HFactory.createRangeSlicesQuery(kspace, STR_SERIALIZER,
                            STR_SERIALIZER, STR_SERIALIZER);
            rq.setColumnFamily(cf);
            rq.setRange("", "", false, 1000);
            QueryResult<OrderedRows<String, String, String>> qr = rq.execute();
            OrderedRows<String, String, String> orderedRows = qr.get();
            Mutator<String> deleteMutator = HFactory.createMutator(kspace, STR_SERIALIZER);
            for (Row<String, String, String> row : orderedRows) {
                deleteMutator.delete(row.getKey(), cf, null, STR_SERIALIZER);
            }
        }

        for (String scf : superColumnFamilies) {
            RangeSuperSlicesQuery<String, String, String, String> rq = HFactory.createRangeSuperSlicesQuery(kspace,
                            STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER);
            rq.setColumnFamily(scf);
            rq.setRange("", "", false, 1000);
            QueryResult<OrderedSuperRows<String, String, String, String>> qr = rq.execute();
            OrderedSuperRows<String, String, String, String> orderedRows = qr.get();
            Mutator<String> deleteMutator = HFactory.createMutator(kspace, STR_SERIALIZER);

            for (SuperRow<String, String, String, String> row : orderedRows) {
                deleteMutator.delete(row.getKey(), scf, null, STR_SERIALIZER);
            }
        }

    }

    @Test
    public void testSaveMetricsIdentifier() throws ServiceException {

        String serviceName = "ServiceX1";
        String operationName = "operationY1";
        String consumerName = "consumerZ1";
        long timeSnapshot = System.currentTimeMillis();

        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollectionForOneConsumer(
                        serviceName, operationName, consumerName);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraColumnValues("MetricIdentifier", "test_count|" + serviceName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + "true", STR_SERIALIZER, STR_SERIALIZER,
                        new String[] { "metricName", "serviceAdminName", "operationName" }, new String[] {
                                "test_count", serviceName, operationName });

        assertCassandraColumnValues("MetricIdentifier", "test_average|" + serviceName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + "true", STR_SERIALIZER, STR_SERIALIZER,
                        new String[] { "metricName", "serviceAdminName", "operationName" }, new String[] {
                                "test_average", serviceName, operationName });

    }

    @Test
    public void testSaveServiceOperationByIp() throws ServiceException {

        String serviceName = "ServiceX2";
        String operationName = "operationY2";
        String consumerName = "consumerZ2";
        long timeSnapshot = System.currentTimeMillis();

        String ipAddress = provider.getIPAddress();
        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollectionForOneConsumer(
                        serviceName, operationName, consumerName);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraSuperColumnValues("ServiceOperationByIp", ipAddress, serviceName, STR_SERIALIZER,
                        STR_SERIALIZER, STR_SERIALIZER, new String[] { operationName }, new String[] { "" });

    }

    @Test
    public void testSaveServiceConsumerByIp() throws ServiceException {

        String serviceName = "ServiceX3";
        String operationName = "operationY3";
        String consumerName = "consumerZ3";
        long timeSnapshot = System.currentTimeMillis();

        String ipAddress = provider.getIPAddress();
        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollectionForOneConsumer(
                        serviceName, operationName, consumerName);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraSuperColumnValues("ServiceConsumerByIp", ipAddress, serviceName, STR_SERIALIZER, STR_SERIALIZER,
                        STR_SERIALIZER, new String[] { consumerName }, new String[] { "" });

    }

    @Test
    public void testSaveMetricTimeSeries() throws ServiceException {

        String serviceName = "ServiceX4";
        String operationName = "operationY4";
        String consumerName = "consumerZ4";

        String ipAddress = provider.getIPAddress();
        long timeSnapshot = System.currentTimeMillis();

        String metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_count"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
        String metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_average"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollectionForOneConsumer(
                        serviceName, operationName, consumerName);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraColumnValues("MetricTimeSeries", ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR
                        + serviceName + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName
                        + "|test_count|20|true", LONG_SERIALIZER, STR_SERIALIZER, new Long[] { timeSnapshot },
                        new String[] { metricValueKeyTestCount });

        assertCassandraColumnValues("MetricTimeSeries", ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR
                        + serviceName + CassandraMetricsStorageProvider.KEY_SEPARATOR + operationName
                        + "|test_average|20|true", LONG_SERIALIZER, STR_SERIALIZER, new Long[] { timeSnapshot },
                        new String[] { metricValueKeyTestAvg });
    }

    @Test
    public void testSaveMetricValues() throws ServiceException {

        String serviceName = "ServiceX5";
        String operationName = "operationY5";
        String consumerName = "consumerZ5";

        String ipAddress = provider.getIPAddress();
        long timeSnapshot = System.currentTimeMillis();

        String metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_count"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
        String metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_average"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollectionForOneConsumer(
                        serviceName, operationName, consumerName);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraColumnValues("MetricValues", metricValueKeyTestCount, STR_SERIALIZER, OBJ_SERIALIZER,
                        new String[] { "value" }, new Object[] { 123456l });

        assertCassandraColumnValues("MetricValues", metricValueKeyTestAvg, STR_SERIALIZER, OBJ_SERIALIZER,
                        new String[] { "count", "totalTime" }, new Object[] { 17l, 456854235.123d });

    }

    @Test
    public void testSaveServiceCallsByTime() throws ServiceException {

        String serviceName = "ServiceX6";
        String operationName = "operationY6";
        String consumerName = "consumerZ6";

        String ipAddress = provider.getIPAddress();
        Object serviceCallsByTimeKey = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + serviceName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + true;
        long timeSnapshot = System.currentTimeMillis();

        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollectionForOneConsumer(
                        serviceName, operationName, consumerName);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraSuperColumnValues("ServiceCallsByTime", serviceCallsByTimeKey, Long.valueOf(timeSnapshot),
                        LONG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, new String[] { operationName },
                        new String[] { "" });

    }

    private Collection<MetricValueAggregator> createMetricValueAggregatorsCollectionForOneConsumer(String serviceName,
                    String operationName, String consumerName) {
        Collection<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
        MetricId metricId1 = new MetricId("test_count", serviceName, operationName);
        MetricValue metricValue1 = new LongSumMetricValue(metricId1, 123456);
        MetricId metricId2 = new MetricId("test_average", serviceName, operationName);
        MetricValue metricValue2 = new AverageMetricValue(metricId2, 17, 456854235.123);

        MetricClassifier metricClassifier1 = new MetricClassifier(consumerName, "sourcedc", "targetdc");
        MetricClassifier metricClassifier2 = new MetricClassifier(consumerName, "sourcedc", "targetdc");

        Map<MetricClassifier, MetricValue> valuesByClassifier1 = new HashMap<MetricClassifier, MetricValue>();
        valuesByClassifier1.put(metricClassifier1, metricValue1);

        MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(metricValue1,
                        MetricCategory.Timing, MonitoringLevel.NORMAL, valuesByClassifier1);

        result.add(aggregator1);

        Map<MetricClassifier, MetricValue> valuesByClassifier2 = new HashMap<MetricClassifier, MetricValue>();
        valuesByClassifier2.put(metricClassifier2, metricValue2);
        MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(metricValue2,
                        MetricCategory.Timing, MonitoringLevel.NORMAL, valuesByClassifier2);

        result.add(aggregator2);

        return result;
    }
}
