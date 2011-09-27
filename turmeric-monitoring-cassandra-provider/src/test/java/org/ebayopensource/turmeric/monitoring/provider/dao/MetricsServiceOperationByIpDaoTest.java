package org.ebayopensource.turmeric.monitoring.provider.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricServiceCallsByTimeDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricTimeSeriesDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricValuesByIpAndDateDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricValuesDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsServiceConsumerByIpDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsServiceOperationByIpDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.manager.cassandra.server.CassandraTestManager;


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
import org.junit.BeforeClass;
import org.junit.Test;

public class MetricsServiceOperationByIpDaoTest extends BaseTest {

	
	private static MetricsServiceOperationByIpDAO<String, String> metricsServiceOperationByIpDAO ;
	private static MetricsServiceConsumerByIpDAO<String, String> metricsServiceConsumerByIpDAO ;
	private static MetricValuesByIpAndDateDAO<String, String> metricValuesByIpAndDateDAO ;
	private static MetricTimeSeriesDAO<String> metricTimeSeriesDAO ;
	private static MetricValuesDAO<String> metricValuesDAO ;
	private static MetricServiceCallsByTimeDAO<String, String> serviceCallsByTimeDAO;

	private static CassandraMetricsStorageProvider storageProvider;

	private static Map<String, String> createOptions() {
        Map<String, String> options = new HashMap<String, String>();
        options.put("host-address", HOST);
        options.put("keyspace-name", KEY_SPACE);
        options.put("cluster-name", TURMERIC_TEST_CLUSTER);
        options.put("storeServiceMetrics", "false");
        options.put("embedded", "false");
        return options;
    }
	@BeforeClass
	public static void beforeClass() throws Exception {
		CassandraTestManager.initialize();
		metricsServiceOperationByIpDAO = new MetricsServiceOperationByIpDAOImpl<String, String>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE, "ServiceOperationByIp", String.class, String.class);
		metricsServiceConsumerByIpDAO = new MetricsServiceConsumerByIpDAOImpl<String, String>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE, "ServiceConsumerByIp", String.class, String.class);
		metricValuesByIpAndDateDAO = new MetricValuesByIpAndDateDAOImpl<String, String>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE, "MetricValuesByIpAndDate", String.class, String.class);
		metricTimeSeriesDAO = new MetricTimeSeriesDAOImpl<String>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE, "MetricTimeSeries", String.class);
		metricValuesDAO = new MetricValuesDAOImpl<String>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE, "MetricValues", String.class);
		serviceCallsByTimeDAO = new MetricServiceCallsByTimeDAOImpl<String, String>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE, "ServiceCallsByTime", String.class, String.class);
		
		storageProvider = new CassandraMetricsStorageProvider();
		Map<String, String> options = createOptions();
		storageProvider.init(options,null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
		
	}

	@Test
	public void testFindMetricOperationNames() throws ServiceException {
		List<String> operationNames = new ArrayList<String>();
		operationNames.add("operationY2");
		
		
		saveServiceOperationByIp(operationNames);
		
	List<String> findMetricOperationNames = metricsServiceOperationByIpDAO.findMetricOperationNames(operationNames);
		assertNotNull(findMetricOperationNames);
		assertEquals(1, findMetricOperationNames.size());
		assertTrue("ServiceX2.operationY2".equals(findMetricOperationNames.get(0)));
	}

	
    
    private void saveServiceOperationByIp(List<String> operationNames) throws ServiceException {

        String serviceName = "ServiceX2";
        String operationName = operationNames.get(0);
        String consumerName = "consumerZ2";
        long timeSnapshot = System.currentTimeMillis();

        String ipAddress = storageProvider.getIPAddress();
        Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollectionForOneConsumer(
                        serviceName, operationName, consumerName);

        storageProvider.saveMetricSnapshot(timeSnapshot, snapshotCollection);

     }
    
	private Collection<MetricValueAggregator> createMetricValueAggregatorsCollectionForOneConsumer(
			String serviceName, String operationName, String consumerName) {
		Collection<MetricValueAggregator> result = new ArrayList<MetricValueAggregator>();
		MetricId metricId1 = new MetricId("test_count", serviceName,
				operationName);
		MetricValue metricValue1 = new LongSumMetricValue(metricId1, 123456);
		MetricId metricId2 = new MetricId("test_average", serviceName,
				operationName);
		MetricValue metricValue2 = new AverageMetricValue(metricId2, 17,
				456854235.123);

		MetricClassifier metricClassifier1 = new MetricClassifier(consumerName,
				"sourcedc", "targetdc");
		MetricClassifier metricClassifier2 = new MetricClassifier(consumerName,
				"sourcedc", "targetdc");

		Map<MetricClassifier, MetricValue> valuesByClassifier1 = new HashMap<MetricClassifier, MetricValue>();
		valuesByClassifier1.put(metricClassifier1, metricValue1);

		MetricValueAggregatorTestImpl aggregator1 = new MetricValueAggregatorTestImpl(
				metricValue1, MetricCategory.Timing, MonitoringLevel.NORMAL,
				valuesByClassifier1);

		result.add(aggregator1);

		Map<MetricClassifier, MetricValue> valuesByClassifier2 = new HashMap<MetricClassifier, MetricValue>();
		valuesByClassifier2.put(metricClassifier2, metricValue2);
		MetricValueAggregatorTestImpl aggregator2 = new MetricValueAggregatorTestImpl(
				metricValue2, MetricCategory.Timing, MonitoringLevel.NORMAL,
				valuesByClassifier2);

		result.add(aggregator2);

		return result;
	}
}
