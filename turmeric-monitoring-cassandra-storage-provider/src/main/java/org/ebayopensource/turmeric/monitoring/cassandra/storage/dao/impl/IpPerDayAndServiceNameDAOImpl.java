package org.ebayopensource.turmeric.monitoring.cassandra.storage.dao.impl;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.BasicModel;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.SuperModel;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractSuperColumnFamilyDao;

// TODO: Auto-generated Javadoc
/**
 * The Class IpPerDayAndServiceNameDAOImpl.
 * 
 * @param <SK>
 *           the generic type
 * @param <K>
 *           the key type
 */
public class IpPerDayAndServiceNameDAOImpl<SK, K> extends
         AbstractSuperColumnFamilyDao<SK, SuperModel, String, BasicModel> {

   /**
    * Instantiates a new ip per day and service name dao impl.
    * 
    * @param clusterName
    *           the cluster name
    * @param host
    *           the host
    * @param s_keyspace
    *           the s_keyspace
    * @param columnFamilyName
    *           the column family name
    * @param sKTypeClass
    *           the s k type class
    * @param kTypeClass
    *           the k type class
    */
   public IpPerDayAndServiceNameDAOImpl(String clusterName, String host, String s_keyspace, String columnFamilyName,
            final Class<SK> sKTypeClass, final Class<K> kTypeClass) {
      super(clusterName, host, s_keyspace, sKTypeClass, SuperModel.class, String.class, BasicModel.class,
               columnFamilyName);
   }
}
