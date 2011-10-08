package org.ebayopensource.turmeric.monitoring.provider.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.monitoring.provider.BaseTest;
import org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceCassandraProviderImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsServiceOperationByIpDAOImpl;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MetricsServiceOperationByIpDaoTest extends BaseTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		metricsStorageProvider = new CassandraMetricsStorageProvider();
		metricsStorageProvider.init(options, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
		cleanUpTestData();
	}

	@After
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testOneServiceOneOperation() throws ServiceException {
		List<String> operationNames = new ArrayList<String>();
		operationNames.add("operationY1");

		List<String> serviceNames = new ArrayList<String>();
		serviceNames.add("ServiceX1");

		saveServiceOperationByIp(serviceNames, operationNames);

		List<String> findMetricOperationNames = metricsServiceOperationByIpDAO.findMetricOperationNames(serviceNames);
		assertNotNull(findMetricOperationNames);
		assertEquals(1, findMetricOperationNames.size());
		assertTrue("ServiceX1.operationY1".equals(findMetricOperationNames.get(0)));
	}

	@Test
	public void testTwoServiceOneOperation() throws ServiceException {
		List<String> operationNames = new ArrayList<String>();
		operationNames.add("operationY1");

		List<String> serviceNames = new ArrayList<String>();
		serviceNames.add("ServiceX1");
		serviceNames.add("ServiceX2");
		saveServiceOperationByIp(serviceNames, operationNames);

		List<String> findMetricOperationNames = metricsServiceOperationByIpDAO.findMetricOperationNames(serviceNames);
		assertNotNull(findMetricOperationNames);
		assertEquals(2, findMetricOperationNames.size());
		assertTrue("ServiceX1.operationY1".equals(findMetricOperationNames.get(0)));
		assertTrue("ServiceX2.operationY1".equals(findMetricOperationNames.get(1)));
	}

	private void saveServiceOperationByIp(List<String> serviceNames, List<String> operationNames)
			throws ServiceException {
		long timeSnapshot = System.currentTimeMillis();

		for (String serviceName : serviceNames) {
			for (String operationName : operationNames) {
				String consumerName = "consumerZ2";

				// String ipAddress = metricsStorageProvider.getIPAddress();
				Collection<MetricValueAggregator> snapshotCollection = createMetricValueAggregatorsCollectionForOneConsumer(
						serviceName, operationName, consumerName);

				metricsStorageProvider.saveMetricSnapshot(timeSnapshot, snapshotCollection);
			}
		}
	}
}
