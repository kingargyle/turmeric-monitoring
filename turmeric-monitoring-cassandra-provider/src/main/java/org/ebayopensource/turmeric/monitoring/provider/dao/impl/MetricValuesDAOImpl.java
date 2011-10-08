/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao.impl;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.DoubleSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValuesDAO;
import org.ebayopensource.turmeric.monitoring.provider.model.MetricValue;
import org.ebayopensource.turmeric.monitoring.provider.model.Model;
import org.ebayopensource.turmeric.monitoring.provider.model.SuperModel;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractSuperColumnFamilyDao;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorHelper;

/**
 * The Class MetricValuesDAOImpl.
 * 
 * @author jamuguerza
 */
public class MetricValuesDAOImpl<K> extends
         AbstractColumnFamilyDao<K, org.ebayopensource.turmeric.monitoring.provider.model.MetricValue> implements
         MetricValuesDAO<K> {

   @Override
   public MetricValue find(K key) {
      SliceQuery<String, String,Object> query = HFactory.createSliceQuery(keySpace,
               StringSerializer.get(), StringSerializer.get(), ObjectSerializer.get());

      QueryResult<ColumnSlice<String, Object>> result = query.setColumnFamily(columnFamilyName).setKey((String)key)
      // .setColumnNames(allColumnNames).execute();

               .setRange("", "", false, 10).execute();

      try {
         if (result.get().getColumns().isEmpty()) {
            return null;
         }
      } catch (Exception e) {
         return null;
      }

      try {
         MetricValue metricValue = new MetricValue<K>(key);
         Map<String, Object> columns = metricValue.getColumns();
         ColumnSlice<String, Object> queryResult = result.get();
         for (HColumn<String, Object> column : queryResult.getColumns()) {
            Object val = column.getValue();
            columns.put(column.getName(), val);
         }
         return metricValue;
      } catch (Exception e) {
         throw new RuntimeException("Error creating persistent class", e);
      }
   }

   /**
    * Instantiates a new metric values dao impl.
    * 
    * @param clusterName
    *           the cluster name
    * @param host
    *           the host
    * @param s_keyspace
    *           the s_keyspace
    * @param columnFamilyName
    *           the column family name
    */
   public MetricValuesDAOImpl(final String clusterName, final String host, final String s_keyspace,
            final String columnFamilyName, final Class<K> kTypeClass) {
      super(clusterName, host, s_keyspace, kTypeClass,
               org.ebayopensource.turmeric.monitoring.provider.model.MetricValue.class, columnFamilyName);
   }

}
