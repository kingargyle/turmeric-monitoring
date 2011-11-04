/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao.impl;

import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorByIdDAO;
import org.ebayopensource.turmeric.monitoring.provider.model.Error;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

// TODO: Auto-generated Javadoc
/**
 * The Class MetricsErrorByIdDAOImpl.
 * 
 * @param <K>
 *           the key type
 * @author jamuguerza
 */
public class MetricsErrorByIdDAOImpl<K> extends AbstractColumnFamilyDao<K, Error> implements MetricsErrorByIdDAO<K> {

   /**
    * Instantiates a new metrics error dao impl.
    * 
    * @param clusterName
    *           the cluster name
    * @param host
    *           the host
    * @param s_keyspace
    *           the s_keyspace
    * @param columnFamilyName
    *           the column family name
    * @param kTypeClass
    *           the k type class
    */
   public MetricsErrorByIdDAOImpl(String clusterName, String host, String s_keyspace, String columnFamilyName,
            final Class<K> kTypeClass) {
      super(clusterName, host, s_keyspace, kTypeClass, Error.class, columnFamilyName);
   }

   // /**
   // * {@inheritDoc}
   // */
   // @Override
   // public Long findCountByTimeRange(K key, Long startTime, Long endTime) {
   // Long result = 0l;
   //
   // try {
   // SliceQuery<Long, String, String> q = HFactory.createSliceQuery(keySpace, LongSerializer.get(),
   // StringSerializer.get(), StringSerializer.get());
   // q.setColumnFamily(columnFamilyName);
   // q.setKey((Long) key);
   // q.setRange(String.valueOf(startTime), String.valueOf(endTime), false, 100000000);
   // QueryResult<ColumnSlice<String, String>> r = q.execute();
   // ColumnSlice<String, String> columnSlice = r.get();
   //
   // result = (long) columnSlice.getColumns().size();
   // } catch (Exception e) {
   // throw new RuntimeException("Error in findCountByTimeRange with key =" + key, e);
   // }
   //
   // return result;
   // }

   // @Override
   // public Long findCountByFilter(K key, Long startTime, Long endTime, String serviceName, String operationName,
   // String consumerName) {
   // Long result = 0l;
   //
   // try {
   // SliceQuery<Long, String, String> q = HFactory.createSliceQuery(keySpace, LongSerializer.get(),
   // StringSerializer.get(), StringSerializer.get());
   // q.setColumnFamily(columnFamilyName);
   // q.setKey((Long) key);
   // q.setRange(String.valueOf(startTime), String.valueOf(endTime), false, 100000000);
   // QueryResult<ColumnSlice<String, String>> r = q.execute();
   // ColumnSlice<String, String> columnSlice = r.get();
   // for (HColumn<String, String> column : columnSlice.getColumns()) {
   // System.out.println(column.getName());
   // System.out.println(column.getValue());
   // }
   // result = (long) columnSlice.getColumns().size();
   // } catch (Exception e) {
   // throw new RuntimeException("Error in findCountByTimeRange with key =" + key, e);
   // }
   //
   // return result;
   // }
}
