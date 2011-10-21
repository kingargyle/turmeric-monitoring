/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao.impl;

import java.util.Map;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorByIdDAO;
import org.ebayopensource.turmeric.monitoring.provider.model.Error;
import org.ebayopensource.turmeric.monitoring.provider.model.MetricValue;
import org.ebayopensource.turmeric.monitoring.provider.model.Model;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

// TODO: Auto-generated Javadoc
/**
 * The Class MetricsErrorByIdDAOImpl.
 *
 * @param <K> the key type
 * @author jamuguerza
 */
public class MetricsErrorByIdDAOImpl<K> extends AbstractColumnFamilyDao<K, Error> implements MetricsErrorByIdDAO<K> {

   /**
    * Instantiates a new metrics error dao impl.
    *
    * @param clusterName the cluster name
    * @param host the host
    * @param s_keyspace the s_keyspace
    * @param columnFamilyName the column family name
    * @param kTypeClass the k type class
    */
   public MetricsErrorByIdDAOImpl(String clusterName, String host, String s_keyspace, String columnFamilyName,
            final Class<K> kTypeClass) {
      super(clusterName, host, s_keyspace, kTypeClass, Error.class, columnFamilyName);
   }

   /**
    * {@inheritDoc}
    */
   public Long findCountByTimeRange(K key, Long startTime, Long endTime) {
      Long result = 0l;

      try {
         SliceQuery<Long, String, String> q = HFactory.createSliceQuery(this.keySpace, LongSerializer.get(),
                  StringSerializer.get(), StringSerializer.get());
         q.setColumnFamily(this.columnFamilyName);
         q.setKey((Long) key);
         q.setRange(String.valueOf(startTime), String.valueOf(endTime), false, 100000000);
         QueryResult<ColumnSlice<String, String>> r = q.execute();
         ColumnSlice<String, String> columnSlice = r.get();
         result = (long) columnSlice.getColumns().size();
      } catch (Exception e) {
         throw new RuntimeException("Error in findCountByTimeRange with key ="+key, e);
      }

      return result;
   }
}
