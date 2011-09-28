package org.ebayopensource.turmeric.monitoring.test;

import static org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider.KEY_SEPARATOR;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.dao.MetricsDAO;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.MetricIdentifier;
import org.ebayopensource.turmeric.monitoring.utils.CassandraTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MetricDAOTest extends CassandraTestHelper {
    private MetricsDAO metricDAO;

    @Before
    public void init() throws TTransportException, IOException, InterruptedException, ConfigurationException {
    	initialize();
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
        String actual = metricDAO.createKeyForServiceOperationCallsInTime("testip", cmetricIdentifier);
        String expected = "testip|servName1|false";
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateKeyForTimeSeries() {
        MetricIdentifier cmetricIdentifier = new MetricIdentifier("metricName2", "servName2", "ops4", true);
        String actual = metricDAO.createKeyForTimeSeries("theip", cmetricIdentifier, 15);
        String expected = "theip|servName2|ops4|metricName2|15|true";
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateKeyForServiceOperationCallsInTime() {
        MetricIdentifier cmetricIdentifier = new MetricIdentifier("metricName3", "servName3", "ops7", false);
        String actual = metricDAO.createKeyForServiceOperationCallsInTime("theip", cmetricIdentifier);
        String expected = "theip|servName3|false";
        assertEquals(expected, actual);
    }
}
