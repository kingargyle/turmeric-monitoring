package org.ebayopensource.turmeric.monitoring.test;

import java.io.IOException;
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

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
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

public class TwoMetricsTwoConsumersForSameOperationAndServiceTest extends CassandraTestHelper {
    CassandraMetricsStorageProvider provider = null;

    @Before
    public void setUp() throws TTransportException, IOException, InterruptedException, ConfigurationException {
    	initialize();
        provider = new CassandraMetricsStorageProvider();
        kspace = new HectorManager().getKeyspace(cluster_name, cassandra_node_ip, keyspace_name, "MetricIdentifier",
                        false, String.class, String.class);
        Map<String, String> options = createOptions();
        provider.init(options, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
    }

    private Map<String, String> createOptions() {
        Map<String, String> options = new HashMap<String, String>();
        options.put("host-address", cassandra_node_ip);
        options.put("keyspace-name", keyspace_name);
        options.put("cluster-name", cluster_name);
        options.put("storeServiceMetrics", "false");
        options.put("embedded", "true");
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
    public void testSaveMetricIdentifier() throws ServiceException {
        String serviceName = "ServiceA1";
        String operationName = "operationA1";
        long timeSnapshot = System.currentTimeMillis();

        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection(serviceName,
                        operationName);

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
        String serviceName = "ServiceA2";
        String operationName = "operationA2";
        String ipAddress = provider.getIPAddress();
        long timeSnapshot = System.currentTimeMillis();
        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection(serviceName,
                        operationName);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraSuperColumnValues("ServiceOperationByIp", ipAddress, serviceName, STR_SERIALIZER,
                        STR_SERIALIZER, STR_SERIALIZER, new String[] { operationName }, new String[] { "" });

    }

    @Test
    public void testSaveServiceConsumerByIp() throws ServiceException {
        String serviceName = "ServiceA3";
        String operationName = "operationA3";
        String ipAddress = provider.getIPAddress();
        long timeSnapshot = System.currentTimeMillis();

        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection(serviceName,
                        operationName);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraSuperColumnValues("ServiceConsumerByIp", ipAddress, serviceName, STR_SERIALIZER, STR_SERIALIZER,
                        STR_SERIALIZER, new String[] { "missing" }, new String[] { "" });

        assertCassandraSuperColumnValues("ServiceConsumerByIp", ipAddress, serviceName, STR_SERIALIZER, STR_SERIALIZER,
                        STR_SERIALIZER, new String[] { "anotherusecase" }, new String[] { "" });

    }

    @Test
    public void testSaveMetricTimeSeries() throws ServiceException {
        String serviceName = "ServiceA4";
        String operationName = "operationA4";
        String ipAddress = provider.getIPAddress();
        long timeSnapshot = System.currentTimeMillis();

        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection(serviceName,
                        operationName);

        String metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_count"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
        String metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_average"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;

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
        String serviceName = "ServiceA5";
        String operationName = "operationA5";
        String ipAddress = provider.getIPAddress();
        long timeSnapshot = System.currentTimeMillis();
        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection(serviceName,
                        operationName);

        String metricValueKeyTestCount = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_count"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;
        String metricValueKeyTestAvg = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + "test_average"
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + timeSnapshot;

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraColumnValues("MetricValues", metricValueKeyTestCount, STR_SERIALIZER, OBJ_SERIALIZER,
                        new String[] { "value" }, new Object[] { 123456l });

        assertCassandraColumnValues("MetricValues", metricValueKeyTestAvg, STR_SERIALIZER, OBJ_SERIALIZER,
                        new String[] { "count", "totalTime" }, new Object[] { 17l, 456854235.123d });

    }

    @Test
    public void testSaveServiceCallsByTime() throws ServiceException {
        String serviceName = "ServiceA6";
        String operationName = "operationA6";
        String ipAddress = provider.getIPAddress();
        long timeSnapshot = System.currentTimeMillis();
        Object serviceCallsByTimeKey = ipAddress + CassandraMetricsStorageProvider.KEY_SEPARATOR + serviceName
                        + CassandraMetricsStorageProvider.KEY_SEPARATOR + true;

        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection(serviceName,
                        operationName);

        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

        assertCassandraSuperColumnValues("ServiceCallsByTime", serviceCallsByTimeKey, Long.valueOf(timeSnapshot),
                        LONG_SERIALIZER, STR_SERIALIZER, STR_SERIALIZER, new String[] { operationName },
                        new String[] { "" });

    }

    private Collection<MetricValueAggregator> createMetricValueAggregatorsCollection(String serviceName,
                    String operationName) {
        Collection<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
        MetricId metricId1 = new MetricId("test_count", serviceName, operationName);
        MetricValue metricValue1 = new LongSumMetricValue(metricId1, 123456);
        MetricId metricId2 = new MetricId("test_average", serviceName, operationName);
        MetricValue metricValue2 = new AverageMetricValue(metricId2, 17, 456854235.123);

        MetricClassifier metricClassifier1 = new MetricClassifier("missing", "sourcedc", "targetdc");
        MetricClassifier metricClassifier2 = new MetricClassifier("anotherusecase", "sourcedc", "targetdc");

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
