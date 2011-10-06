package org.ebayopensource.turmeric.monitoring.aggregation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class CassandraConnectionInfoTestITCase {
   CassandraConnectionInfo connectionInfo;

   @Before
   public void setup() {
      connectionInfo = new CassandraConnectionInfo("Test Cluster", "127.0.0.1", "9160", "TurmericMonitoring");
   }

   @Test
   public void testGetCluster() {
      assertNotNull(connectionInfo.getCluster());
      assertEquals("Test Cluster", connectionInfo.getCluster().describeClusterName());
   }

   @Test
   public void testGetKeyspace() {
      assertNotNull(connectionInfo.getKeyspace());
      assertEquals("TurmericMonitoring", connectionInfo.getKeyspace().getKeyspaceName());
   }

}
