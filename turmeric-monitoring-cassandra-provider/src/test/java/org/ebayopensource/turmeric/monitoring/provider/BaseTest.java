package org.ebayopensource.turmeric.monitoring.provider;

import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricServiceCallsByTimeDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricTimeSeriesDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValuesByIpAndDateDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValuesDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsServiceConsumerByIpDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsServiceOperationByIpDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricServiceCallsByTimeDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricTimeSeriesDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricValuesByIpAndDateDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricValuesDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsServiceConsumerByIpDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsServiceOperationByIpDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.manager.cassandra.server.CassandraTestManager;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.junit.After;
import org.junit.Before;

public abstract class BaseTest {

	/** The Constant TURMERIC_TEST_CLUSTER. */
	protected static final String TURMERIC_TEST_CLUSTER = "TestCluster";

	/** The Constant KEY_SPACE. */
	protected static final String KEY_SPACE = "TestKeyspace";

	/** The Constant HOST. */
	protected static final String HOST = "127.0.1.10:9160";

	protected final Map<String, String> options = loadProperties();

	/** The providers. */
	protected CassandraMetricsStorageProvider metricsStorageProvider;
	protected SOAMetricsQueryServiceProvider queryprovider;
	protected CassandraErrorLoggingHandler errorStorageProvider;

	protected MetricsServiceOperationByIpDAO<String, String> metricsServiceOperationByIpDAO;
	protected MetricsServiceConsumerByIpDAO<String, String> metricsServiceConsumerByIpDAO;
	protected MetricValuesByIpAndDateDAO<String, Long> metricValuesByIpAndDateDAO;
	protected MetricTimeSeriesDAO<String> metricTimeSeriesDAO;
	protected MetricValuesDAO<String> metricValuesDAO;
	protected MetricServiceCallsByTimeDAO<String, Long> serviceCallsByTimeDAO;

	@Before
	public void setUp() throws Exception {
		CassandraTestManager.initialize();

		metricsServiceOperationByIpDAO = new MetricsServiceOperationByIpDAOImpl<String, String>(TURMERIC_TEST_CLUSTER,
				HOST, KEY_SPACE, "ServiceOperationByIp", String.class, String.class);
		metricsServiceConsumerByIpDAO = new MetricsServiceConsumerByIpDAOImpl<String, String>(TURMERIC_TEST_CLUSTER,
				HOST, KEY_SPACE, "ServiceConsumerByIp", String.class, String.class);
		metricValuesByIpAndDateDAO = new MetricValuesByIpAndDateDAOImpl<String, Long>(TURMERIC_TEST_CLUSTER, HOST,
				KEY_SPACE, "MetricValuesByIpAndDate", String.class, Long.class);
		metricTimeSeriesDAO = new MetricTimeSeriesDAOImpl<String>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE,
				"MetricTimeSeries", String.class);
		metricValuesDAO = new MetricValuesDAOImpl<String>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE, "MetricValues",
				String.class);
		serviceCallsByTimeDAO = new MetricServiceCallsByTimeDAOImpl<String, Long>(TURMERIC_TEST_CLUSTER, HOST,
				KEY_SPACE, "ServiceCallsByTime", String.class, Long.class);
	}

	@After
	public void tearDown() {
		queryprovider = null;
		errorStorageProvider = null;
		metricsStorageProvider = null;
	}

	protected Map<String, String> loadProperties() {
		Map<String, String> options = new HashMap<String, String>();
		options.put("host-address", HOST);
		options.put("keyspace-name", KEY_SPACE);
		options.put("cluster-name", TURMERIC_TEST_CLUSTER);
		options.put("storeServiceMetrics", "false");
		options.put("embedded", "true");
		return options;
	}

}
