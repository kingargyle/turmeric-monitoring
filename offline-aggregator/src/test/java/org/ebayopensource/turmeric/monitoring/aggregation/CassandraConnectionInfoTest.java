package org.ebayopensource.turmeric.monitoring.aggregation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CassandraConnectionInfoTest extends BaseTest {
   CassandraConnectionInfo connectionInfo;

   @Before
   public void setup() throws TTransportException, ServiceException, IOException, InterruptedException,
            ConfigurationException {
      super.setUp();
      connectionInfo = new CassandraConnectionInfo("TestCluster", "127.0.0.1", "9160", "TurmericMonitoring");
   }

   @Override
   @After
   public void tearDown() {
      super.tearDown();
      connectionInfo = null;
   }

   @Test
   public void testGetCluster() {
      assertNotNull(connectionInfo.getCluster());
      assertEquals("TestCluster", connectionInfo.getCluster().describeClusterName());
   }

   @Test
   public void testGetKeyspace() {
      assertNotNull(connectionInfo.getKeyspace());
      assertEquals("TurmericMonitoring", connectionInfo.getKeyspace().getKeyspaceName());
   }

}
