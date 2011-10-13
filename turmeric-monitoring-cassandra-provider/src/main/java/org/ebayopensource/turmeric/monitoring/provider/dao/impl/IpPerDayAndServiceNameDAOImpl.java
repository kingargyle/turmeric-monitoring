package org.ebayopensource.turmeric.monitoring.provider.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SuperColumnQuery;

import org.ebayopensource.turmeric.monitoring.provider.dao.IpPerDayAndServiceNameDAO;
import org.ebayopensource.turmeric.monitoring.provider.model.Model;
import org.ebayopensource.turmeric.monitoring.provider.model.SuperModel;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractSuperColumnFamilyDao;

public class IpPerDayAndServiceNameDAOImpl<SK, K> extends AbstractSuperColumnFamilyDao<SK, SuperModel, String, Model>
         implements IpPerDayAndServiceNameDAO<SK, K> {

   public IpPerDayAndServiceNameDAOImpl(String clusterName, String host, String s_keyspace, String columnFamilyName,
            final Class<SK> sKTypeClass, final Class<K> kTypeClass) {
      super(clusterName, host, s_keyspace, sKTypeClass, SuperModel.class, String.class, Model.class, columnFamilyName);
   }

   @Override
   public List<String> findByDateAndServiceName(long currentTimeMillis, String serviceName) {
      List<String> result = new ArrayList<String>();
      String superKey = new SimpleDateFormat("ddMMyyyy").format(new Date(currentTimeMillis));

      SuperColumnQuery<String, String, String, String> superColumnQuery = HFactory.createSuperColumnQuery(
               this.keySpace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get(),
               StringSerializer.get());
      superColumnQuery.setColumnFamily("IpPerDayAndServiceName").setKey(superKey).setSuperName(serviceName);
      QueryResult<HSuperColumn<String, String, String>> queryResult = superColumnQuery.execute();
      HSuperColumn<String, String, String> superColumn = queryResult.get();
      for (HColumn<String, String> ipColumn : superColumn.getColumns()) {
         result.add(ipColumn.getName());
      }

      return result;
   }
}
