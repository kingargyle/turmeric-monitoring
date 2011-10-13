package org.ebayopensource.turmeric.monitoring.cassandra.storage.dao.impl;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.dao.IpPerDayAndServiceNameDAO;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.BasicModel;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.SuperModel;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractSuperColumnFamilyDao;

public class IpPerDayAndServiceNameDAOImpl<SK, K> extends AbstractSuperColumnFamilyDao<SK, SuperModel,String, BasicModel>
         implements IpPerDayAndServiceNameDAO<SK, K> {

   public IpPerDayAndServiceNameDAOImpl(String clusterName, String host,
            String s_keyspace, String columnFamilyName, final Class<SK> sKTypeClass, final Class<K> kTypeClass) {
      super(clusterName, host, s_keyspace, sKTypeClass, SuperModel.class,
               String.class, BasicModel.class, columnFamilyName);
   }
}
