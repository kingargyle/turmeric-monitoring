package org.ebayopensource.turmeric.monitoring.aggregation;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

public class CassandraConnectionInfo {
   private Keyspace keyspace = null;
   private Cluster cluster = null;

   public CassandraConnectionInfo(String clusterName, String hostAdress, String port, String keySpaceName) {
      cluster = HFactory.getOrCreateCluster(clusterName, hostAdress);
      keyspace = HFactory.createKeyspace(keySpaceName, cluster);
   }

   public Keyspace getKeyspace() {
      return keyspace;
   }

   public Cluster getCluster() {
      return cluster;
   }

}
