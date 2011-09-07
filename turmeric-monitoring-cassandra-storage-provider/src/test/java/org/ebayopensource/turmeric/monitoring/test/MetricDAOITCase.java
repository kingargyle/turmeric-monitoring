package org.ebayopensource.turmeric.monitoring.test;

import static org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider.KEY_SEPARATOR;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.dao.MetricsDAO;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.MetricIdentifier;
import org.ebayopensource.turmeric.monitoring.utils.CassandraTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MetricDAOITCase extends CassandraTestHelper {
    private MetricsDAO metricDAO;

    @Before
    public void init() {
        metricDAO = new MetricsDAO(this.cluster_name, this.cassandra_node_ip, this.keyspace_name);
    }

    @After
    public void tearDown() {
        metricDAO = null;
    }

    @Test
    public void testCreateKeyForMetricValuesByIpAndDate() {
        String actual = metricDAO.createKeyForMetricValuesByIpAndDate("someip");
        String date = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String expected = "someip" + KEY_SEPARATOR + date;
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateServiceOperationCallsInTime() {
        MetricIdentifier cmetricIdentifier = new MetricIdentifier("metricName1", "servName1", "ops1", false);
        String actual = metricDAO.createServiceOperationCallsInTime("testip", cmetricIdentifier);
        String expected = "testip|servName1|false";
        assertEquals(expected, actual);
    }
}
