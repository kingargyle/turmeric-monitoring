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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.SuperSlice;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;
import me.prettyprint.hector.api.query.SuperSliceQuery;

import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValuesDAO;
import org.ebayopensource.turmeric.monitoring.provider.model.MetricValue;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.SystemMetricDefs;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

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

   public static final List<String> errorListNames = new ArrayList<String>();

   static {
      errorListNames.add(SystemMetricDefs.OP_ERR_TOTAL.getMetricName());
      errorListNames.add(SystemMetricDefs.OP_ERR_CAT_APPLICATION.getMetricName());
      errorListNames.add(SystemMetricDefs.OP_ERR_CAT_SYSTEM.getMetricName());
      errorListNames.add(SystemMetricDefs.OP_ERR_CAT_REQUEST.getMetricName());
      errorListNames.add(SystemMetricDefs.OP_ERR_UNEXPECTED.getMetricName());
      errorListNames.add(SystemMetricDefs.OP_ERR_SEVERITY_ERROR.getMetricName());
      errorListNames.add(SystemMetricDefs.OP_ERR_SEVERITY_WARNING.getMetricName());
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
    * Creates the metric time series key.
    * 
    * @param ipAddress
    *           the ip address
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
   private String createMetricTimeSeriesKey(String ipAddress, String metricName, String serviceName,
            String operationName, int snapshotInterval, boolean isServerSide) throws ServiceException {

      return ipAddress + KEY_SEPARATOR + serviceName + KEY_SEPARATOR + operationName + KEY_SEPARATOR + metricName
               + KEY_SEPARATOR + snapshotInterval + KEY_SEPARATOR + isServerSide;

   }

   /**
    * Creates the metric time series key by consumer.
    * 
    * @param ipAddress
    *           the ip address
    * @param metricName
    *           the metric name
    * @param serviceName
    *           the service name
    * @param operationName
    *           the operation name
    * @param consumerName
    *           the consumer name
    * @param aggregationPeriod
    *           the aggregation period
    * @param serverSide
    *           the server side
    * @return the string
    */
   private String createMetricTimeSeriesKeyByConsumer(String ipAddress, String metricName, String serviceName,
            String operationName, String consumerName, int aggregationPeriod, boolean serverSide) {
      return ipAddress + KEY_SEPARATOR + serviceName + KEY_SEPARATOR + operationName + KEY_SEPARATOR + consumerName
               + KEY_SEPARATOR + metricName + KEY_SEPARATOR + aggregationPeriod + KEY_SEPARATOR + serverSide;
   }

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
         metricValue.setKey(key);
         System.out.println("findByKeys: metricValue found." + key + "={" + metricValue + "}");
         result.add(metricValue);
      }
      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, List<MetricValue<?>>> findMetricValuesByConsumer(List<String> ipAddressList, String metricName,
            long begin, long end, boolean serverSide, int aggregationPeriod, String serviceName,
            List<String> operationNames, List<String> consumerNames) throws ServiceException {
      Map<String, List<MetricValue<?>>> result = new HashMap<String, List<MetricValue<?>>>();
      Map<Long, MetricValue<?>> metricValuesByTime = new TreeMap<Long, MetricValue<?>>();
      for (String operationName : operationNames) {
         for (String consumerName : consumerNames) {
            for (String ipAddress : ipAddressList) {
               Set<String> metricValuesToGet = new HashSet<String>();
               SuperSliceQuery<String, Long, String, String> q = HFactory.createSuperSliceQuery(keySpace,
                        StringSerializer.get(), LongSerializer.get(), StringSerializer.get(), StringSerializer.get());
               q.setColumnFamily("MetricTimeSeries");
               String metricTimeSeriesKey = createMetricTimeSeriesKeyByConsumer(ipAddress, metricName, serviceName,
                        operationName, consumerName, aggregationPeriod, serverSide);
               q.setKey(metricTimeSeriesKey);
               q.setRange(begin, end, false, 100000000);
               QueryResult<SuperSlice<Long, String, String>> r = q.execute();
               SuperSlice<Long, String, String> columnSlice = r.get();
               for (HSuperColumn<Long, String, String> column : columnSlice.getSuperColumns()) {
                  for (HColumn<String, String> metricValueCol : column.getColumns()) {
                     if (metricValueCol.getName() != null && metricValueCol.getName().contains(metricName)
                              && metricValueCol.getName().contains(consumerName)) {
                        metricValuesToGet.add(metricValueCol.getName());
                     }
                  }

               }
               System.out.printf("findMetricValuesByConsumer(%s,%d,%d,%s,%d). Columns found for key=[%s]={"
                        + metricValuesToGet + "}\n", metricName, begin, end, serverSide, aggregationPeriod,
                        metricTimeSeriesKey);
               List<MetricValue<?>> metricValues = this.findByKeys(metricValuesToGet);
               // now, I add them ordered by timestamp
               for (MetricValue<?> metricValue : metricValues) {
                  if (metricValuesByTime.containsKey(metricValue.getTimeMiliseconds())) {
                     MetricValue<?> oldMetricValue = metricValuesByTime.get(metricValue.getTimeMiliseconds());
                     if (SystemMetricDefs.OP_TIME_TOTAL.getMetricName().equals(metricName)) {
                        Long count = (Long) oldMetricValue.getColumns().get("count");
                        Double total = (Double) oldMetricValue.getColumns().get("totalTime");
                        count += (Long) metricValue.getColumns().get("count");
                        total += (Double) metricValue.getColumns().get("totalTime");
                        oldMetricValue.getColumns().put("count", count);
                        oldMetricValue.getColumns().put("totalTime", total);
                     }
                  } else {
                     metricValuesByTime.put(metricValue.getTimeMiliseconds(), metricValue);
                  }

               }

               if (result.get(consumerName) != null) {
                  result.get(consumerName).addAll(metricValuesByTime.values());
               } else {
                  List<MetricValue<?>> theList = new ArrayList<MetricValue<?>>();
                  theList.addAll(metricValuesByTime.values());
                  result.put(consumerName, theList);
               }

               metricValuesByTime.clear();
            }
         }
      }

      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, List<MetricValue<?>>> findMetricValuesByOperation(List<String> ipAddressList, String metricName,
            long begin, long end, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters)
            throws ServiceException {
      Map<String, List<MetricValue<?>>> result = new HashMap<String, List<MetricValue<?>>>();

      List<String> serviceAdminNames = filters.get("Service");
      List<String> operationNames = filters.get("Operation");
      List<String> consumerNames = filters.get("Consumer");
      String serviceName = serviceAdminNames.get(0);
      String resultKey = null;
      Map<Long, MetricValue<?>> metricValuesByTime = new TreeMap<Long, MetricValue<?>>();
      for (String operation : operationNames) {
         resultKey = operation;
         for (String ipAddress : ipAddressList) {
            if (consumerNames == null || consumerNames.isEmpty()) {
               Set<String> metricValuesToGet = new HashSet<String>();
               SuperSliceQuery<String, Long, String, String> q = HFactory.createSuperSliceQuery(keySpace,
                        StringSerializer.get(), LongSerializer.get(), StringSerializer.get(), StringSerializer.get());
               q.setColumnFamily("MetricTimeSeries");
               String metricTimeSeriesKey = createMetricTimeSeriesKey(ipAddress, metricName, serviceName, operation,
                        aggregationPeriod, serverSide);
               q.setKey(metricTimeSeriesKey);
               q.setRange(begin, end, false, 100000000);
               QueryResult<SuperSlice<Long, String, String>> r = q.execute();
               SuperSlice<Long, String, String> columnSlice = r.get();
               for (HSuperColumn<Long, String, String> column : columnSlice.getSuperColumns()) {
                  for (HColumn<String, String> metricValuesKey : column.getColumns()) {
                     if (metricValuesKey.getName() != null && metricValuesKey.getName().contains(metricName)) {
                        metricValuesToGet.add(metricValuesKey.getName());
                     }
                  }

               }
               System.out.printf("findMetricValuesByOperation(%s,%d,%d,%s,%d). Columns found for key=[%s]={"
                        + metricValuesToGet + "}\n", metricName, begin, end, serverSide, aggregationPeriod,
                        metricTimeSeriesKey);
               List<MetricValue<?>> metricValues = this.findByKeys(metricValuesToGet);
               // now, I add them ordered by timestamp
               for (MetricValue<?> metricValue : metricValues) {
                  if (metricValuesByTime.containsKey(metricValue.getTimeMiliseconds())) {
                     MetricValue<?> oldMetricValue = metricValuesByTime.get(metricValue.getTimeMiliseconds());
                     if (SystemMetricDefs.OP_TIME_TOTAL.getMetricName().equals(metricName)) {
                        Long count = (Long) oldMetricValue.getColumns().get("count");
                        Double total = (Double) oldMetricValue.getColumns().get("totalTime");
                        count += (Long) metricValue.getColumns().get("count");
                        total += (Double) metricValue.getColumns().get("totalTime");
                        oldMetricValue.getColumns().put("count", count);
                        oldMetricValue.getColumns().put("totalTime", total);
                     }
                  } else {
                     metricValuesByTime.put(metricValue.getTimeMiliseconds(), metricValue);
                  }
               }
            } else {
               resultKey = serviceName;
               for (String consumerName : consumerNames) {// group by service name
                  Set<String> metricValuesToGet = new HashSet<String>();
                  SuperSliceQuery<String, Long, String, String> q = HFactory
                           .createSuperSliceQuery(keySpace, StringSerializer.get(), LongSerializer.get(),
                                    StringSerializer.get(), StringSerializer.get());
                  q.setColumnFamily("MetricTimeSeries");
                  String metricTimeSeriesKey = createMetricTimeSeriesKeyByConsumer(ipAddress, metricName, serviceName,
                           operation, consumerName, aggregationPeriod, serverSide);
                  q.setKey(metricTimeSeriesKey);
                  q.setRange(begin, end, false, 100000000);
                  QueryResult<SuperSlice<Long, String, String>> r = q.execute();
                  SuperSlice<Long, String, String> columnSlice = r.get();
                  for (HSuperColumn<Long, String, String> column : columnSlice.getSuperColumns()) {
                     for (HColumn<String, String> metricValuesKey : column.getColumns()) {
                        if (metricValuesKey.getName() != null && metricValuesKey.getName().contains(metricName)) {
                           metricValuesToGet.add(metricValuesKey.getName());
                        }
                     }

                  }
                  System.out.printf("findMetricValuesByOperation(%s,%d,%d,%s,%d). Columns found for key=[%s]={"
                           + metricValuesToGet + "}\n", metricName, begin, end, serverSide, aggregationPeriod,
                           metricTimeSeriesKey);
                  List<MetricValue<?>> metricValues = this.findByKeys(metricValuesToGet);
                  // now, I add them ordered by timestamp
                  for (MetricValue<?> metricValue : metricValues) {
                     if (metricValuesByTime.containsKey(metricValue.getTimeMiliseconds())) {
                        MetricValue<?> oldMetricValue = metricValuesByTime.get(metricValue.getTimeMiliseconds());
                        if (SystemMetricDefs.OP_TIME_TOTAL.getMetricName().equals(metricName)) {
                           Long count = (Long) oldMetricValue.getColumns().get("count");
                           Double total = (Double) oldMetricValue.getColumns().get("totalTime");
                           count += (Long) metricValue.getColumns().get("count");
                           total += (Double) metricValue.getColumns().get("totalTime");
                           oldMetricValue.getColumns().put("count", count);
                           oldMetricValue.getColumns().put("totalTime", total);
                        }
                     } else {
                        metricValuesByTime.put(metricValue.getTimeMiliseconds(), metricValue);
                     }
                  }
               }

            }

         }
         if (result.get(resultKey) != null) {
            result.get(resultKey).addAll(metricValuesByTime.values());
         } else {
            List<MetricValue<?>> theList = new ArrayList<MetricValue<?>>();
            theList.addAll(metricValuesByTime.values());
            result.put(resultKey, theList);
         }

         metricValuesByTime.clear();
      }

      return result;
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

   @Override
   public Map<String, List<MetricValue<?>>> findMetricErrorValuesByOperation(List<String> ipAddressList, long begin,
            long end, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters)
            throws ServiceException {
      Map<String, List<MetricValue<?>>> result = new HashMap<String, List<MetricValue<?>>>();

      List<String> serviceAdminNames = filters.get("Service");
      List<String> operationNames = filters.get("Operation");
      List<String> consumerNames = filters.get("Consumer");
      String serviceName = serviceAdminNames.get(0);
      String resultKey = null;
      Map<Long, MetricValue<?>> metricValuesByTime = new TreeMap<Long, MetricValue<?>>();
      for (String operation : operationNames) {
         for (String errorMetricName : errorListNames) {
            resultKey = operation;
            for (String ipAddress : ipAddressList) {
               if (consumerNames == null || consumerNames.isEmpty()) {
                  Set<String> metricValuesToGet = new HashSet<String>();
                  SuperSliceQuery<String, Long, String, String> q = HFactory
                           .createSuperSliceQuery(keySpace, StringSerializer.get(), LongSerializer.get(),
                                    StringSerializer.get(), StringSerializer.get());
                  q.setColumnFamily("MetricTimeSeries");
                  String metricTimeSeriesKey = createMetricTimeSeriesKey(ipAddress, errorMetricName, serviceName,
                           operation, aggregationPeriod, serverSide);
                  q.setKey(metricTimeSeriesKey);
                  q.setRange(begin, end, false, 100000000);
                  QueryResult<SuperSlice<Long, String, String>> r = q.execute();
                  SuperSlice<Long, String, String> columnSlice = r.get();
                  for (HSuperColumn<Long, String, String> column : columnSlice.getSuperColumns()) {
                     for (HColumn<String, String> metricValuesKey : column.getColumns()) {
                        if (metricValuesKey.getName() != null && metricValuesKey.getName().contains(errorMetricName)) {
                           metricValuesToGet.add(metricValuesKey.getName());
                        }
                     }

                  }
                  System.out.printf("findMetricErrorValuesByOperation(%s,%d,%d,%s,%d). Columns found for key=[%s]={"
                           + metricValuesToGet + "}\n", errorMetricName, begin, end, serverSide, aggregationPeriod,
                           metricTimeSeriesKey);
                  List<MetricValue<?>> metricValues = this.findByKeys(metricValuesToGet);
                  // now, I add them ordered by timestamp
                  for (MetricValue<?> metricValue : metricValues) {
                     if (metricValuesByTime.containsKey(metricValue.getTimeMiliseconds())) {
                        MetricValue<?> oldMetricValue = metricValuesByTime.get(metricValue.getTimeMiliseconds());
                        if (SystemMetricDefs.OP_TIME_TOTAL.getMetricName().equals(errorMetricName)) {
                           Long count = (Long) oldMetricValue.getColumns().get("count");
                           Double total = (Double) oldMetricValue.getColumns().get("totalTime");
                           count += (Long) metricValue.getColumns().get("count");
                           total += (Double) metricValue.getColumns().get("totalTime");
                           oldMetricValue.getColumns().put("count", count);
                           oldMetricValue.getColumns().put("totalTime", total);
                        } else if (SystemMetricDefs.OP_ERR_TOTAL.getMetricName().equals(errorMetricName)) {
                           Double value = (Double) oldMetricValue.getColumns().get("value");
                           value += (Double) metricValue.getColumns().get("value");
                           oldMetricValue.getColumns().put("value", value);
                        }
                     } else {
                        metricValuesByTime.put(metricValue.getTimeMiliseconds(), metricValue);
                     }
                  }
               } else {
                  resultKey = errorMetricName;
                  for (String consumerName : consumerNames) {// group by service name
                     Set<String> metricValuesToGet = new HashSet<String>();
                     SuperSliceQuery<String, Long, String, String> q = HFactory.createSuperSliceQuery(keySpace,
                              StringSerializer.get(), LongSerializer.get(), StringSerializer.get(),
                              StringSerializer.get());
                     q.setColumnFamily("MetricTimeSeries");
                     String metricTimeSeriesKey = createMetricTimeSeriesKeyByConsumer(ipAddress, errorMetricName,
                              serviceName, operation, consumerName, aggregationPeriod, serverSide);
                     q.setKey(metricTimeSeriesKey);
                     q.setRange(begin, end, false, 100000000);
                     QueryResult<SuperSlice<Long, String, String>> r = q.execute();
                     SuperSlice<Long, String, String> columnSlice = r.get();
                     for (HSuperColumn<Long, String, String> column : columnSlice.getSuperColumns()) {
                        for (HColumn<String, String> metricValuesKey : column.getColumns()) {
                           if (metricValuesKey.getName() != null && metricValuesKey.getName().contains(errorMetricName)) {
                              metricValuesToGet.add(metricValuesKey.getName());
                           }
                        }

                     }
                     System.out.printf("findMetricErrorValuesByOperation(%s,%d,%d,%s,%d). Columns found for key=[%s]={"
                              + metricValuesToGet + "}\n", errorMetricName, begin, end, serverSide, aggregationPeriod,
                              metricTimeSeriesKey);
                     List<MetricValue<?>> metricValues = this.findByKeys(metricValuesToGet);
                     // now, I add them ordered by timestamp
                     for (MetricValue<?> metricValue : metricValues) {
                        if (metricValuesByTime.containsKey(metricValue.getTimeMiliseconds())) {
                           MetricValue<?> oldMetricValue = metricValuesByTime.get(metricValue.getTimeMiliseconds());
                           if (SystemMetricDefs.OP_TIME_TOTAL.getMetricName().equals(errorMetricName)) {
                              Long count = (Long) oldMetricValue.getColumns().get("count");
                              Double total = (Double) oldMetricValue.getColumns().get("totalTime");
                              count += (Long) metricValue.getColumns().get("count");
                              total += (Double) metricValue.getColumns().get("totalTime");
                              oldMetricValue.getColumns().put("count", count);
                              oldMetricValue.getColumns().put("totalTime", total);
                           } else if (SystemMetricDefs.OP_ERR_TOTAL.getMetricName().equals(errorMetricName)) {
                              Double value = (Double) oldMetricValue.getColumns().get("value");
                              value += (Double) metricValue.getColumns().get("value");
                              oldMetricValue.getColumns().put("value", value);
                           }
                        } else {
                           metricValuesByTime.put(metricValue.getTimeMiliseconds(), metricValue);
                        }
                     }
                  }

               }

            }
            if (result.get(resultKey) != null) {
               result.get(resultKey).addAll(metricValuesByTime.values());
            } else {
               List<MetricValue<?>> theList = new ArrayList<MetricValue<?>>();
               theList.addAll(metricValuesByTime.values());
               result.put(resultKey, theList);
            }

            metricValuesByTime.clear();
         }
      }
      return result;
   }

   @Override
   public List<String> getErrorMetricNameList() {
      return errorListNames;
   }

}
