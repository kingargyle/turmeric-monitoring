package org.ebayopensource.turmeric.monitoring.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SuperColumnQuery;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.monitoring.utils.CassandraTestHelper;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
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
import org.junit.Ignore;
import org.junit.Test;

public class CassandraMetricsStorageProviderITCase extends CassandraTestHelper {
    private static final String cluster_name = "Test Cluster";
    private static final String keyspace_name = "TurmericMonitoring";
    private static final String cassandra_node_ip = "192.168.2.41";
    CassandraMetricsStorageProvider provider = null;
    Keyspace kspace = null;

    @Before
    public void setUp() {
        provider = new CassandraMetricsStorageProvider();
        kspace = new HectorManager().getKeyspace(cluster_name, cassandra_node_ip, keyspace_name, "MetricIdentifier");
        Map<String, String> options = createOptions();
        provider.init(options, null, null, 20);
    }

    @After
    public void tearDown() {
        provider = null;
        kspace = null;
    }

    @Test
    public void testInit() {

        assertFalse(provider.isStoreServiceMetrics());
        assertEquals(3600, provider.getMidPeriod());
        assertEquals(86400, provider.getLongPeriod());
        assertEquals(20, provider.getSnapshotInterval());
    }

    @Test
    public void testGetIpAddress() throws ServiceException, UnknownHostException {
        String ipAddress = provider.getIPAddress();
        String expectedIpAddress = InetAddress.getLocalHost().getCanonicalHostName();
        assertEquals(expectedIpAddress, ipAddress);
    }

    @Test
    public void testGetKeyfromMetricId() {
        org.ebayopensource.turmeric.runtime.common.monitoring.MetricId metricId = new MetricId("metricName",
                        "ServiceAdminName", "operationName");
        String expectedKey = metricId.getMetricName() + "-" + metricId.getAdminName() + "-"
                        + metricId.getOperationName();
        String actualKey = provider.getKeyfromMetricId(metricId);
        assertEquals(expectedKey, actualKey);
    }

    private Map<String, String> createOptions() {
        Map<String, String> options = new HashMap<String, String>();
        options.put("hostName", cassandra_node_ip);
        options.put("keyspaceName", keyspace_name);
        options.put("clusterName", cluster_name);
        options.put("storeServiceMetrics", "false");
        return options;
    }

    @Test
    public void testSaveMetricSnapshot() throws ServiceException {

        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollection();
        long timeSnapshot = System.currentTimeMillis();
        provider.saveMetricSnapshot(timeSnapshot, snapshotCollection);
        System.out.println("Time in ms =" + (System.currentTimeMillis() - timeSnapshot));
        // now I need to retrieve the values. I use Hector for this.
        ColumnSlice<Object, Object> errorColumnSlice = getColumnValues(kspace, "MetricIdentifier",
                        "test_count-service1-operation1", StringSerializer.get(), StringSerializer.get(), "metricName",
                        "serviceAdminName", "operationName");
        assertValues(errorColumnSlice, "metricName", "test_count", "serviceAdminName", "service1", "operationName",
                        "operation1");

        ColumnSlice<Object, Object> errorColumnSlice2 = getColumnValues(kspace, "MetricIdentifier",
                        "test_average-service2-operation2", StringSerializer.get(), StringSerializer.get(),
                        "metricName", "serviceAdminName", "operationName");
        assertValues(errorColumnSlice2, "metricName", "test_average", "serviceAdminName", "service2", "operationName",
                        "operation2");

        HSuperColumn<Object, Object, Object> serviceOperationColumnSlice = getSuperColumnValues(kspace,
                        "ServiceOperationByIp", provider.getIPAddress(), StringSerializer.get(),
                        StringSerializer.get(), StringSerializer.get(), "service1");
        assertValues(serviceOperationColumnSlice, "service1", "operation1", "");

        HSuperColumn<Object, Object, Object> serviceOperationColumnSlice2 = getSuperColumnValues(kspace,
                        "ServiceOperationByIp", provider.getIPAddress(), StringSerializer.get(),
                        StringSerializer.get(), StringSerializer.get(), "service2");
        assertValues(serviceOperationColumnSlice2, "service2", "operation2", "");

        // now the consumer cf
        HSuperColumn<Object, Object, Object> serviceConsumerColumnSlice = getSuperColumnValues(kspace,
                        "ServiceConsumerByIp", provider.getIPAddress(), StringSerializer.get(), StringSerializer.get(),
                        StringSerializer.get(), "service1");
        assertValues(serviceConsumerColumnSlice, "service1", "missing", "");

        // now the consumer cf
        HSuperColumn<Object, Object, Object> serviceConsumerColumnSlice2 = getSuperColumnValues(kspace,
                        "ServiceConsumerByIp", provider.getIPAddress(), StringSerializer.get(), StringSerializer.get(),
                        StringSerializer.get(), "service2");
        assertValues(serviceConsumerColumnSlice2, "service2", "anotherusecase", "");

    }

    private void assertValues(HSuperColumn<Object, Object, Object> serviceOperationColumnSlice, String superColumnName,
                    Object... columnPairs) {
        int i = 0;
        for (HColumn<Object, Object> column : serviceOperationColumnSlice.getColumns()) {
            assertEquals(columnPairs[2 * i], column.getName());
            assertEquals("Expected = " + columnPairs[2 * i + 1] + ". Actual = " + column.getValue(),
                            columnPairs[2 * i + 1], column.getValue());
        }
    }

    private HSuperColumn<Object, Object, Object> getSuperColumnValues(Keyspace kspace, String cfName, Object key,
                    Serializer superColumnNameSerializer, Serializer<?> columnNameSerializer,
                    Serializer valueSerializer, String superColumnName) {
        SuperColumnQuery<Object, Object, Object, Object> superColumnQuery = HFactory.createSuperColumnQuery(kspace,
                        SerializerTypeInferer.getSerializer(key), superColumnNameSerializer, columnNameSerializer,
                        valueSerializer);
        superColumnQuery.setColumnFamily(cfName).setKey(key).setSuperName(superColumnName);
        QueryResult<HSuperColumn<Object, Object, Object>> result = superColumnQuery.execute();
        return result.get();
    }

    private Collection<MetricValueAggregator> createMetricValueAggregatorsCollection() {
        Collection<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
        MetricId metricId1 = new MetricId("test_count", "service1", "operation1");
        MetricValue metricValue1 = new LongSumMetricValue(metricId1, 20);
        MetricId metricId2 = new MetricId("test_average", "service2", "operation2");
        MetricValue metricValue2 = new AverageMetricValue(metricId2, 2, 345);

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
