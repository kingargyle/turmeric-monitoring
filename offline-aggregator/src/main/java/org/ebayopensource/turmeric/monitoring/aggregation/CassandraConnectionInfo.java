package org.ebayopensource.turmeric.monitoring.aggregation;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class CassandraConnectionInfo.
 */
public class CassandraConnectionInfo {

   /** The keyspace. */
   private Keyspace keyspace = null;

   /** The cluster. */
   private Cluster cluster = null;

   /**
    * Instantiates a new cassandra connection info.
    * 
    * @param clusterName
    *           the cluster name
    * @param hostAdress
    *           the host adress
    * @param port
    *           the port
    * @param keySpaceName
    *           the key space name
    */
   public CassandraConnectionInfo(String clusterName, String hostAdress, String port, String keySpaceName) {
      cluster = HFactory.getOrCreateCluster(clusterName, hostAdress);
      keyspace = HFactory.createKeyspace(keySpaceName, cluster);
   }

   /**
    * Gets the keyspace.
    * 
    * @return the keyspace
    */
   public Keyspace getKeyspace() {
      return keyspace;
   }

   /**
    * Gets the cluster.
    * 
    * @return the cluster
    */
   public Cluster getCluster() {
      return cluster;
   }

}
