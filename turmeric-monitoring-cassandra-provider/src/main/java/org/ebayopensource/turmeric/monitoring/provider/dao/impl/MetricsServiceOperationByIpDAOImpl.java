/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SuperColumnQuery;

import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsServiceOperationByIpDAO;
import org.ebayopensource.turmeric.monitoring.provider.model.Model;
import org.ebayopensource.turmeric.monitoring.provider.model.SuperModel;

import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractSuperColumnFamilyDao;

import edu.emory.mathcs.backport.java.util.Collections;

// TODO: Auto-generated Javadoc
/**
 * The Class MetricsServiceOperationByIpDAOImpl.
 *
 * @param <SK> the generic type
 * @param <K> the key type
 * @author jamuguerza
 */
public class MetricsServiceOperationByIpDAOImpl<SK, K> extends AbstractSuperColumnFamilyDao<SK, SuperModel, K, Model>
         implements MetricsServiceOperationByIpDAO<SK, K> {

   /**
    * Instantiates a new metrics error values dao impl.
    *
    * @param clusterName the cluster name
    * @param host the host
    * @param s_keyspace the s_keyspace
    * @param columnFamilyName the column family name
    * @param sKTypeClass the s k type class
    * @param kTypeClass the k type class
    */
   public MetricsServiceOperationByIpDAOImpl(String clusterName, String host, String s_keyspace,
            String columnFamilyName, final Class<SK> sKTypeClass, final Class<K> kTypeClass) {
      super(clusterName, host, s_keyspace, sKTypeClass, SuperModel.class, kTypeClass, Model.class, columnFamilyName);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> findMetricOperationNames(final List<String> serviceAdminNames) {
      Set<String> resultSet = new TreeSet<String>();

      for (String serviceName : serviceAdminNames) {
         SuperColumnQuery<String, String, String, String> superColumnQuery = HFactory.createSuperColumnQuery(
                  this.keySpace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get(),
                  StringSerializer.get());
         superColumnQuery.setColumnFamily(this.columnFamilyName).setKey("All").setSuperName(serviceName);
         QueryResult<HSuperColumn<String, String, String>> queryResult = superColumnQuery.execute();
         HSuperColumn<String, String, String> operationColumns = queryResult.get();
         for (HColumn<String, String> operationNamesColum : operationColumns.getColumns()) {
            resultSet.add(serviceName + "." + operationNamesColum.getName());
         }

      }
      List<String> resultList = new ArrayList<String>(resultSet);
      Collections.sort(resultList);
      return resultList;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> findMetricServiceAdminNames(final List<String> serviceAdminNames) {
      Set<String> resultSet = new TreeSet<String>();

      List<SK> keys = new ArrayList<SK>();
      keys.add((SK) "All");
      String[] serviceNames = null;
      if (serviceAdminNames != null && !serviceAdminNames.isEmpty()) {
         serviceNames = new String[serviceAdminNames.size()];
         serviceNames = serviceAdminNames.toArray(serviceNames);
      }
      Map<SK, SuperModel> findItems = findItems(keys, (K[]) serviceNames);

      for (Map.Entry<SK, SuperModel> findItem : findItems.entrySet()) {
         SK key = findItem.getKey(); // IP, in this case ALL

         SuperModel superModel = findItem.getValue();
         if (superModel != null) {
            Map<String, Model> columns = superModel.getColumns();

            for (String serviceName : columns.keySet()) {
               resultSet.add(serviceName);
            }
         }
      }

      List<String> resultList = new ArrayList<String>(resultSet);
      Collections.sort(resultList);

      return resultList;
   }

}
