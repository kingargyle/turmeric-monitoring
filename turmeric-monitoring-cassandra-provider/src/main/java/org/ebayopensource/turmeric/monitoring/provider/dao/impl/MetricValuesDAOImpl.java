/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao.impl;

import static org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider.KEY_SEPARATOR;

import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.DoubleSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.SuperSlice;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;
import me.prettyprint.hector.api.query.SuperSliceQuery;

import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValuesDAO;
import org.ebayopensource.turmeric.monitoring.provider.model.MetricValue;
import org.ebayopensource.turmeric.monitoring.provider.model.Model;
import org.ebayopensource.turmeric.monitoring.provider.model.SuperModel;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractSuperColumnFamilyDao;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class MetricValuesDAOImpl.
 * 
 * @param <K>
 *           the key type
 * @author jamuguerza
 */
public class MetricValuesDAOImpl<K> extends
         AbstractColumnFamilyDao<K, org.ebayopensource.turmeric.monitoring.provider.model.MetricValue> implements
         MetricValuesDAO<K> {

   /**
    * {@inheritDoc}
    */
   @Override
   public MetricValue find(K key) {
      SliceQuery<String, String, Object> query = HFactory.createSliceQuery(keySpace, StringSerializer.get(),
               StringSerializer.get(), ObjectSerializer.get());

      QueryResult<ColumnSlice<String, Object>> result = query.setColumnFamily(columnFamilyName).setKey((String) key)
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
         metricValue.setKey(key);
         return metricValue;
      } catch (Exception e) {
         throw new RuntimeException("Error finding MetricValue with key =" + key, e);
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
    * @param kTypeClass
    *           the k type class
    */
   public MetricValuesDAOImpl(final String clusterName, final String host, final String s_keyspace,
            final String columnFamilyName, final Class<K> kTypeClass) {
      super(clusterName, host, s_keyspace, kTypeClass,
               org.ebayopensource.turmeric.monitoring.provider.model.MetricValue.class, columnFamilyName);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, List<MetricValue<?>>> findMetricValuesByOperation(String metricName, long begin, long end,
            boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters) throws ServiceException {
      Map<String, List<MetricValue<?>>> result = new HashMap<String, List<MetricValue<?>>>();

      List<String> serviceAdminNames = filters.get("Service");
      List<String> operationNames = filters.get("Operation");
      List<String> consumerNames = filters.get("Consumer");
      String serviceName = serviceAdminNames.get(0);
      for (String operation : operationNames) {
         Set<String> metricValuesToGet = new HashSet<String>();
         SliceQuery<String, Long, String> q = HFactory.createSliceQuery(keySpace, StringSerializer.get(),
                  LongSerializer.get(), StringSerializer.get());
         q.setColumnFamily("MetricTimeSeries");
         String metricTimeSeriesKey = createMetricTimeSeriesKey(metricName, serviceName, operation, aggregationPeriod,
                  serverSide);
         q.setKey(metricTimeSeriesKey);
         q.setRange(begin, end, false, 100000000);
         QueryResult<ColumnSlice<Long, String>> r = q.execute();
         ColumnSlice<Long, String> columnSlice = r.get();
         for (HColumn<Long, String> column : columnSlice.getColumns()) {
            if (column.getValue() != null && column.getValue().contains(metricName)) {
               metricValuesToGet.add(column.getValue());
            }
         }
         System.out.printf("findMetricValuesByOperation(%s,%d,%d,%s,%d). Columns found for key=[%s]={"+metricValuesToGet+"}\n", metricName, begin,end, serverSide,aggregationPeriod,metricTimeSeriesKey);
         List<MetricValue<?>> metricValues = this.findByKeys(metricValuesToGet);
         
         result.put(operation, metricValues);
      }
      return result;
   }

   /**
    * Find by keys.
    * 
    * @param metricValuesToGet
    *           the metric values to get
    * @return the list
    */
   private List<MetricValue<?>> findByKeys(Set<String> metricValuesToGet) {
      List<MetricValue<?>> result = new ArrayList<MetricValue<?>>();
      for (String key : metricValuesToGet) {
         MetricValue<?> metricValue = this.find((K) key);
         System.out.println("findByKeys: metricValue found."+key+"={"+metricValue+"}");
         result.add(metricValue);
      }
      return result;
   }

   /**
    * Creates the metric time series key.
    * 
    * @param metricName
    *           the metric name
    * @param serviceName
    *           the service name
    * @param operationName
    *           the operation name
    * @param snapshotInterval
    *           the snapshot interval
    * @param isServerSide
    *           the is server side
    * @return the string
    * @throws ServiceException
    *            the service exception
    */
   private String createMetricTimeSeriesKey(String metricName, String serviceName, String operationName,
            int snapshotInterval, boolean isServerSide) throws ServiceException {
      String ipAddress = getIPAddress();
      return ipAddress + KEY_SEPARATOR + serviceName + KEY_SEPARATOR + operationName + KEY_SEPARATOR + metricName
               + KEY_SEPARATOR + snapshotInterval + KEY_SEPARATOR + isServerSide;

   }

   /**
    * Gets the inet address.
    * 
    * @return the inet address
    * @throws ServiceException
    *            the service exception
    */
   public String getIPAddress() throws ServiceException {
      try {
         return InetAddress.getLocalHost().getCanonicalHostName();
      } catch (UnknownHostException x) {
         throw new ServiceException("Unkonwn host name", x);
      }
   }

}
