/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.provider.dao.BaseMetricsErrorsByFilterDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.IpPerDayAndServiceNameDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricServiceCallsByTimeDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValuesByIpAndDateDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValuesDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorByIdDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorValuesDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsServiceConsumerByIpDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsServiceOperationByIpDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricIdentifierDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricServiceCallsByTimeDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricTimeSeriesDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricValuesByIpAndDateDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricValuesDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorByIdDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorValuesDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorsByCategoryDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorsBySeverityDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsServiceConsumerByIpDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsServiceOperationByIpDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.model.Error;
import org.ebayopensource.turmeric.monitoring.provider.model.ExtendedErrorViewData;
import org.ebayopensource.turmeric.monitoring.provider.model.MetricValue;
import org.ebayopensource.turmeric.monitoring.provider.model.Model;
import org.ebayopensource.turmeric.monitoring.provider.model.SuperModel;
import org.ebayopensource.turmeric.monitoring.v1.services.CriteriaInfo;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorInfos;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGraphData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGroupData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricResourceCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.PolicyMetricData;
import org.ebayopensource.turmeric.monitoring.v1.services.PolicyMetricGraphData;
import org.ebayopensource.turmeric.monitoring.v1.services.ReportCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.ResourceEntity;
import org.ebayopensource.turmeric.monitoring.v1.services.ResourceEntityRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.SortOrderType;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.SystemMetricDefs;
import org.ebayopensource.turmeric.utils.ContextUtils;
import org.ebayopensource.turmeric.utils.cassandra.service.CassandraManager;

/**
 * The Class SOAMetricsQueryServiceCassandraProviderImpl.
 * 
 * @author jamuguerza
 */
public class SOAMetricsQueryServiceCassandraProviderImpl implements SOAMetricsQueryServiceProvider {

   /** The Constant c_hostIp. */
   private static final String c_clusterName = "cassandra-cluster-name";
   /** The Constant c_hostIp. */
   private static final String c_embeed = "cassandra-embeed";
   /** The Constant c_error_by_id_cf. */
   private static final String c_error_by_id_cf = "cassandra-error-by-id-cf";
   /** The Constant c_error_values_cf. */
   private static final String c_error_values_cf = "cassandra-error-values-cf";
   /** The Constant c_errors_by_category_cf. */
   private static final String c_errors_by_category_cf = "cassandra-errors-by-category-cf";

   /** The Constant c_errors_by_severity_cf. */
   private static final String c_errors_by_severity_cf = "cassandra-errors-by-severity-cf";
   /** The Constant c_hostIp. */
   private static final String c_hostIp = "cassandra-host-ip";
   /** The Constant c_keyspace. */
   private static final String c_keyspace = "cassandra-monitoring-keyspace";
   private static final String c_metric_consumer_by_ip_cf = "cassandra-metric-consumer-by-ip-cf";
   private static final String c_metric_service_calls_by_time_cf = "cassandra-metric-service-calls-by-time-cf";
   private static final String c_metric_service_operation_by_ip_cf = "cassandra-metric-service-operation-by-ip-cf";
   private static final String c_metric_timeseries_cf = "cassandra-metric-timeseries-cf";

   private static final String c_metric_values_by_ip_and_date_cf = "cassandra-metric-values-by-ip-and-date-cf";

   private static final String c_metric_values_cf = "cassandra-metric-values-cf";

   /** The Constant c_metrics_cf. */
   private static final String c_metrics_cf = "cassandra-metrics-cf";

   /** The Constant c_rpcPort. */
   private static final String c_rpcPort = "cassandra-rpc-port";

   /** The Constant cassandraPropFilePath. */
   private static final String cassandraPropFilePath = "META-INF/config/cassandra/cassandra.properties";

   private static String clusterName;

   /** The host. */
   private static String embeed;

   private static String errorByIdCF;

   private static String errorsByCategoryCF;

   private static String errorsBySeverityCF;

   private static String errorValuesCF;

   /** The host. */
   private static String host;

   /** The keyspace. */
   private static String keyspace;

   private static String metricByTimeSeriesCF;

   private static String metricConsumerByIpCF;

   private static String metricsCF;

   private static String metricServiceCallsByTimeCF;

   private static String metricServiceOperationByIpCF;

   private static String metricValuesByIpAndDateCF;

   private static String metricValuesCF;

   private final IpPerDayAndServiceNameDAO<String, String> ipPerDayAndServiceNameDAO;

   private final MetricIdentifierDAOImpl<String> metricIdentifierDAO;

   private final MetricsErrorByIdDAO<Long> metricsErrorByIdDAO;
   private final BaseMetricsErrorsByFilterDAO<String> metricsErrorsByCategoryDAO;
   private final BaseMetricsErrorsByFilterDAO<String> metricsErrorsBySeverityDAO;
   private final MetricsErrorValuesDAO<String> metricsErrorValuesDAO;
   private final MetricServiceCallsByTimeDAO<String, Long> metricServiceCallsByTimeDAO;
   private final MetricsServiceConsumerByIpDAO<String, String> metricsServiceConsumerByIpDAO;
   private final MetricsServiceOperationByIpDAO<String, String> metricsServiceOperationByIpDAO;
   private final MetricTimeSeriesDAOImpl<String, Long> metricTimeSeriesDAO;
   private final MetricValuesByIpAndDateDAO<String, Long> metricValuesByIpAndDateDAO;
   private final MetricValuesDAO<String> metricValuesDAO;

   /**
    * Instantiates a new Metrics Query Service cassandra provider impl.
    */
   public SOAMetricsQueryServiceCassandraProviderImpl() {

      getCassandraConfig();
      if (Boolean.valueOf(embeed)) {
         CassandraManager.initialize();
      }

      metricsErrorByIdDAO = new MetricsErrorByIdDAOImpl<Long>(clusterName, host, keyspace, errorByIdCF, Long.class);
      metricsErrorValuesDAO = new MetricsErrorValuesDAOImpl(clusterName, host, keyspace, errorValuesCF, String.class);
      metricsErrorsByCategoryDAO = new MetricsErrorsByCategoryDAOImpl<String>(clusterName, host, keyspace,
               errorsByCategoryCF, String.class, metricsErrorValuesDAO, metricsErrorByIdDAO);
      metricsErrorsBySeverityDAO = new MetricsErrorsBySeverityDAOImpl<String>(clusterName, host, keyspace,
               errorsBySeverityCF, String.class, metricsErrorValuesDAO, metricsErrorByIdDAO);
      metricIdentifierDAO = new MetricIdentifierDAOImpl<String>(clusterName, host, keyspace, metricsCF, String.class);
      metricTimeSeriesDAO = new MetricTimeSeriesDAOImpl<String, Long>(clusterName, host, keyspace,
               metricByTimeSeriesCF, String.class, Long.class);
      metricsServiceConsumerByIpDAO = new MetricsServiceConsumerByIpDAOImpl<String, String>(clusterName, host,
               keyspace, metricConsumerByIpCF, String.class, String.class);
      metricValuesDAO = new MetricValuesDAOImpl<String>(clusterName, host, keyspace, metricValuesCF, String.class);
      metricsServiceOperationByIpDAO = new MetricsServiceOperationByIpDAOImpl<String, String>(clusterName, host,
               keyspace, metricServiceOperationByIpCF, String.class, String.class);
      metricServiceCallsByTimeDAO = new MetricServiceCallsByTimeDAOImpl<String, Long>(clusterName, host, keyspace,
               metricServiceCallsByTimeCF, String.class, Long.class);
      metricValuesByIpAndDateDAO = new MetricValuesByIpAndDateDAOImpl<String, Long>(clusterName, host, keyspace,
               metricValuesByIpAndDateCF, String.class, Long.class);
      ipPerDayAndServiceNameDAO = new org.ebayopensource.turmeric.monitoring.provider.dao.impl.IpPerDayAndServiceNameDAOImpl<String, String>(
               clusterName, host, keyspace, "IpPerDayAndServiceName", String.class, String.class);

   }

   private String decodeMetricName(String encodedMetricName) {
      String metricName;
      if ("CallCount".equals(encodedMetricName)) {
         metricName = SystemMetricDefs.OP_TIME_TOTAL.getMetricName();
      } else if ("ResponseTime".equals(encodedMetricName)) {
         metricName = SystemMetricDefs.OP_TIME_TOTAL.getMetricName();
      } else if ("ErrorCount".equals(encodedMetricName)) {
         metricName = SystemMetricDefs.OP_ERR_TOTAL.getMetricName();
      } else {
         throw new IllegalArgumentException("Unknown metric name " + encodedMetricName);
      }
      return metricName;
   }

   /**
    * Gets the cassandra config.
    * 
    * @return the cassandra config
    */
   private void getCassandraConfig() {
      ClassLoader classLoader = ContextUtils.getClassLoader();
      InputStream inStream = classLoader.getResourceAsStream(cassandraPropFilePath);

      if (inStream != null) {
         Properties properties = new Properties();
         try {
            properties.load(inStream);
            clusterName = properties.getProperty(c_clusterName);
            host = (String) properties.get(c_hostIp) + ":" + (String) properties.get(c_rpcPort);
            embeed = (String) properties.get(c_hostIp) + ":" + (String) properties.get(c_embeed);

            keyspace = (String) properties.get(c_keyspace);

            errorByIdCF = (String) properties.get(c_error_by_id_cf);
            errorValuesCF = (String) properties.get(c_error_values_cf);
            errorsByCategoryCF = (String) properties.get(c_errors_by_category_cf);
            errorsBySeverityCF = (String) properties.get(c_errors_by_severity_cf);

            metricsCF = (String) properties.get(c_metrics_cf);
            metricByTimeSeriesCF = (String) properties.get(c_metric_timeseries_cf);
            metricConsumerByIpCF = (String) properties.get(c_metric_consumer_by_ip_cf);
            metricValuesCF = (String) properties.get(c_metric_values_cf);
            metricServiceOperationByIpCF = (String) properties.get(c_metric_service_operation_by_ip_cf);
            metricServiceCallsByTimeCF = (String) properties.get(c_metric_service_calls_by_time_cf);
            metricValuesByIpAndDateCF = (String) properties.get(c_metric_values_by_ip_and_date_cf);

         } catch (IOException e) {
            // ignore
         } finally {
            try {
               inStream.close();
            } catch (IOException e) {
               // ignore
            }
         }
      }
   }

   @Override
   public List<MetricData> getCustomReportData(ReportCriteria reportCriteria, MetricCriteria metricCriteria) {
      return null;
   }

   @Override
   public List<MetricData> getDetailData(String dc, MetricCriteria metricCriteria,
            MetricResourceCriteria metricResourceCriteria) {
      return null;
   }

   @Override
   public List<MetricGraphData> getErrorGraph(final String serviceName, final String operationName,
            final String consumerName, final String errorId, final String errorCategory, final String errorSeverity,
            final MetricCriteria metricCriteria) {

      List<MetricGraphData> result = new ArrayList<MetricGraphData>();

      int aggregationPeriod = metricCriteria.getAggregationPeriod();
      long beginTime = metricCriteria.getFirstStartTime();
      long duration = metricCriteria.getDuration();
      long endTime = beginTime + TimeUnit.SECONDS.toMillis(duration);
      boolean serverSide = MonitoringSystem.COLLECTION_LOCATION_SERVER.equals(metricCriteria.getRoleType());

      Map<String, List<String>> filters = new HashMap<String, List<String>>();
      if (serviceName != null && !"".equals(serviceName)) {
         filters.put(ResourceEntity.SERVICE.value(), Collections.singletonList(serviceName));
      }
      if (operationName != null && !"".equals(operationName)) {
         filters.put(ResourceEntity.OPERATION.value(), Collections.singletonList(operationName));
      }
      if (consumerName != null && !"".equals(consumerName)) {
         filters.put(ResourceEntity.CONSUMER.value(), Collections.singletonList(consumerName));
      }

      List<Map<String, Object>> queryResult = new ArrayList<Map<String, Object>>();

      if (errorCategory != null) {
         final String filter = (errorCategory == null ? null : ErrorCategory.fromValue(errorCategory).name());
         queryResult = metricsErrorsByCategoryDAO.findErrorValuesByFilter(beginTime, endTime, serverSide,
                  aggregationPeriod, null, filter, filters);
      } else if (errorSeverity != null) {
         final String filter = (errorSeverity == null ? null : ErrorSeverity.fromValue(errorSeverity).name());
         queryResult = metricsErrorsBySeverityDAO.findErrorValuesByFilter(beginTime, endTime, serverSide,
                  aggregationPeriod, null, filter, filters);
      }

      for (int i = 0; i < duration / aggregationPeriod; ++i) {
         long startTime = beginTime + TimeUnit.SECONDS.toMillis(i * aggregationPeriod);
         long stopTime = startTime + TimeUnit.SECONDS.toMillis(aggregationPeriod);
         double value = 0;
         for (Map<String, Object> row : queryResult) {
            long time = (Long) row.get("timeStamp");
            if (startTime <= time && time < stopTime) {
               value += 1;
            }
         }
         MetricGraphData metricGraphData = new MetricGraphData();
         metricGraphData.setCount(value);
         metricGraphData.setTimeSlot(startTime);
         metricGraphData.setCriteria(null); // Not supported for now
         result.add(metricGraphData);
      }

      return result;

   }

   @Override
   public List<ErrorViewData> getErrorMetricsData(final String errorType, final List<String> serviceNames,
            final List<String> operationNames, final List<String> consumerNames, final String errorIdString,
            final String errorCategory, final String errorSeverity, final String isErrorId,
            final MetricCriteria metricCriteria) {

      long firstStartTime = metricCriteria.getFirstStartTime();
      long secondStartTime = metricCriteria.getSecondStartTime();
      long duration = TimeUnit.SECONDS.toMillis(metricCriteria.getDuration());
      int aggregationPeriod = metricCriteria.getAggregationPeriod();
      boolean serverSide = !MonitoringSystem.COLLECTION_LOCATION_CLIENT.equals(metricCriteria.getRoleType());

      // Validate input parameters
      Long errorId = errorIdString == null ? null : Long.parseLong(errorIdString);

      Map<String, List<String>> filters = populateFilters(serviceNames, operationNames, consumerNames);

      List<Map<String, Object>> errors1;
      List<Map<String, Object>> errors2;
      if ("Category".equals(errorType)) {
         final String filter = (errorCategory == null ? null : ErrorCategory.fromValue(errorCategory).name());

         errors1 = metricsErrorsByCategoryDAO.findErrorValuesByFilter(firstStartTime, firstStartTime + duration,
                  serverSide, aggregationPeriod, errorId, filter, filters);
         errors2 = metricsErrorsByCategoryDAO.findErrorValuesByFilter(secondStartTime, secondStartTime + duration,
                  serverSide, aggregationPeriod, errorId, filter, filters);
      } else if ("Severity".equals(errorType)) {
         final String filter = (errorSeverity == null ? null : ErrorSeverity.fromValue(errorSeverity).name());

         errors1 = metricsErrorsBySeverityDAO.findErrorValuesByFilter(firstStartTime, firstStartTime + duration,
                  serverSide, aggregationPeriod, errorId, filter, filters);
         errors2 = metricsErrorsBySeverityDAO.findErrorValuesByFilter(secondStartTime, secondStartTime + duration,
                  serverSide, aggregationPeriod, errorId, filter, filters);
      } else {
         throw new IllegalArgumentException("Invalid error type " + errorType);
      }

      Map<String, Map<String, Object>> map1 = transformAggregatedErrorValues(errors1);
      Map<String, Map<String, Object>> map2 = transformAggregatedErrorValues(errors2);

      List<ErrorViewData> result = new ArrayList<ErrorViewData>();
      for (Map.Entry<String, Map<String, Object>> entry : map1.entrySet()) {
         ErrorViewData errorViewData = new ErrorViewData();
         Map<String, Object> row1 = entry.getValue();

         long errorId1 = (Long) row1.get("errorId");
         String errorName = (String) row1.get("errorName");
         org.ebayopensource.turmeric.monitoring.v1.services.Error error1 = new org.ebayopensource.turmeric.monitoring.v1.services.Error();
         error1.setErrorId(String.valueOf(errorId1));
         error1.setErrorName(errorName);
         errorViewData.setError(error1);

         long count1 = (Long) row1.get("errorCount");
         // long count1 = row1.size();
         errorViewData.setErrorCount1(count1);

         Map<String, Object> row2 = map2.remove(entry.getKey());
         if (row2 != null) {
            long count2 = (Long) row2.get("errorCount");
            errorViewData.setErrorCount2(count2);
         } else {
            errorViewData.setErrorCount2(0);
         }

         result.add(errorViewData);
      }
      for (Map.Entry<String, Map<String, Object>> entry : map2.entrySet()) {
         ErrorViewData errorViewData = new ErrorViewData();
         Map<String, Object> row2 = entry.getValue();

         long errorId2 = (Long) row2.get("errorId");
         String errorName = (String) row2.get("errorName");
         org.ebayopensource.turmeric.monitoring.v1.services.Error error2 = new org.ebayopensource.turmeric.monitoring.v1.services.Error();
         error2.setErrorId(String.valueOf(errorId2));
         error2.setErrorName(errorName);
         errorViewData.setError(error2);

         long count2 = (Long) row2.get("errorCount");
         // long count2 = row2.size();
         errorViewData.setErrorCount2(count2);

         errorViewData.setErrorCount1(0);
         result.add(errorViewData);
      }

      // Sort the results
      final boolean sortAsc = metricCriteria.getSortOrder() == SortOrderType.ASCENDING;
      Collections.sort(result, new Comparator<ErrorViewData>() {
         @Override
         public int compare(ErrorViewData evd1, ErrorViewData evd2) {
            double v1 = Math.max(evd1.getErrorCount1(), evd1.getErrorCount2());
            double v2 = Math.max(evd2.getErrorCount1(), evd2.getErrorCount2());
            if (v1 == v2) {
               v1 = evd1.getErrorCount1() + evd1.getErrorCount2();
               v2 = evd2.getErrorCount1() + evd2.getErrorCount2();
            }
            if (v1 == v2) {
               v1 = evd1.getErrorCallRatio1();
               v2 = evd2.getErrorCallRatio2();
            }
            if (v1 == v2) {
               v1 = evd1.getErrorCallRatio1() + evd1.getErrorCallRatio2();
               v2 = evd2.getErrorCallRatio2() + evd2.getErrorCallRatio2();
            }
            int result = v1 > v2 ? 1 : v1 < v2 ? -1 : 0;
            return sortAsc ? result : -result;
         }
      });

      // Trim to the number of requested rows
      int rows = metricCriteria.getNumRows() == null ? 0 : Integer.parseInt(metricCriteria.getNumRows());
      trimResultList(result, rows);

      return result;
   }

   @Override
   public ErrorInfos getErrorMetricsMetadata(String errorId, String errorName, String serviceName) {
      long id = Long.parseLong(errorId);
      Error<?> found = metricsErrorByIdDAO.find(id);
      ErrorInfos result = null;

      if (found != null) {
         result = new ErrorInfos();
         result.setCategory(found.getCategory());
         result.setSeverity(found.getSeverity());
         result.setDomain(found.getDomain());
         result.setSubDomain(found.getSubDomain());
         result.setId(String.valueOf(found.getErrorId()));
         result.setName(found.getName());

      }
      return result;
   }

   @Override
   public List<ExtendedErrorViewData> getExtendedErrorMetricsData(String errorType, List<String> serviceNames,
            List<String> operationNames, List<String> consumerNames, String errorIdString, String errorCategory,
            String errorSeverity, String errorNameParam, MetricCriteria metricCriteria) {
      long firstStartTime = metricCriteria.getFirstStartTime();
      long secondStartTime = metricCriteria.getSecondStartTime();
      long duration = TimeUnit.SECONDS.toMillis(metricCriteria.getDuration());
      int aggregationPeriod = metricCriteria.getAggregationPeriod();
      boolean serverSide = !MonitoringSystem.COLLECTION_LOCATION_CLIENT.equals(metricCriteria.getRoleType());

      // Validate input parameters
      Long errorId = errorIdString == null ? null : Long.parseLong(errorIdString);

      Map<String, List<String>> filters = populateFilters(serviceNames, operationNames, consumerNames);

      List<Map<String, Object>> errors1;
      double calls1 = 0L;
      List<Map<String, Object>> errors2;
      double calls2 = 0L;

      if ("Category".equals(errorType)) {
         final String filter = (errorCategory == null ? null : ErrorCategory.fromValue(errorCategory).name());

         errors1 = metricsErrorsByCategoryDAO.findErrorValuesByFilter(firstStartTime, firstStartTime + duration,
                  serverSide, aggregationPeriod, errorId, filter, filters);
         calls1 = retrieveCallCounts(firstStartTime, duration, calls1);

         errors2 = metricsErrorsByCategoryDAO.findErrorValuesByFilter(secondStartTime, secondStartTime + duration,
                  serverSide, aggregationPeriod, errorId, filter, filters);
         calls2 = retrieveCallCounts(secondStartTime, duration, calls2);

      } else if ("Severity".equals(errorType)) {
         final String filter = (errorSeverity == null ? null : ErrorSeverity.fromValue(errorSeverity).name());

         errors1 = metricsErrorsBySeverityDAO.findErrorValuesByFilter(firstStartTime, firstStartTime + duration,
                  serverSide, aggregationPeriod, errorId, filter, filters);
         calls1 = retrieveCallCounts(firstStartTime, duration, calls1);
         errors2 = metricsErrorsBySeverityDAO.findErrorValuesByFilter(secondStartTime, secondStartTime + duration,
                  serverSide, aggregationPeriod, errorId, filter, filters);
         calls2 = retrieveCallCounts(secondStartTime, duration, calls2);

      } else {
         throw new IllegalArgumentException("Invalid error type " + errorType);
      }

      Map<String, Map<String, Object>> map1 = transformAggregatedErrorValues(errors1);
      Map<String, Map<String, Object>> map2 = transformAggregatedErrorValues(errors2);

      List<ExtendedErrorViewData> result = new ArrayList<ExtendedErrorViewData>();
      for (Map.Entry<String, Map<String, Object>> entry : map1.entrySet()) {
         ExtendedErrorViewData errorViewData = new ExtendedErrorViewData();
         Map<String, Object> row1 = entry.getValue();

         long errorId1 = (Long) row1.get("errorId");
         String errorName = (String) row1.get("errorName");
         org.ebayopensource.turmeric.monitoring.v1.services.Error error1 = new org.ebayopensource.turmeric.monitoring.v1.services.Error();
         error1.setErrorId(String.valueOf(errorId1));
         error1.setErrorName(errorName);
         errorViewData.setError(error1);

         long count1 = (Long) row1.get("errorCount");
         errorViewData.setErrorCount1(count1);
         errorViewData.setErrorCall1(calls1);
         Map<String, Object> row2 = map2.remove(entry.getKey());
         if (row2 != null) {
            long count2 = (Long) row2.get("errorCount");
            errorViewData.setErrorCount2(count2);
            errorViewData.setErrorCall2(calls2);
         } else {
            errorViewData.setErrorCount2(0);
            errorViewData.setErrorCall2(0);
         }

         result.add(errorViewData);
      }
      for (Map.Entry<String, Map<String, Object>> entry : map2.entrySet()) {
         ExtendedErrorViewData errorViewData = new ExtendedErrorViewData();
         Map<String, Object> row2 = entry.getValue();

         long errorId2 = (Long) row2.get("errorId");
         String errorName = (String) row2.get("errorName");
         org.ebayopensource.turmeric.monitoring.v1.services.Error error2 = new org.ebayopensource.turmeric.monitoring.v1.services.Error();
         error2.setErrorId(String.valueOf(errorId2));
         error2.setErrorName(errorName);
         errorViewData.setError(error2);

         long count2 = (Long) row2.get("errorCount");
         errorViewData.setErrorCount2(count2);
         errorViewData.setErrorCall2(calls2);
         errorViewData.setErrorCount1(0);
         errorViewData.setErrorCall1(0);
         result.add(errorViewData);
      }

      // Sort the results
      final boolean sortAsc = metricCriteria.getSortOrder() == SortOrderType.ASCENDING;
      Collections.sort(result, new Comparator<ErrorViewData>() {
         @Override
         public int compare(ErrorViewData evd1, ErrorViewData evd2) {
            double v1 = Math.max(evd1.getErrorCount1(), evd1.getErrorCount2());
            double v2 = Math.max(evd2.getErrorCount1(), evd2.getErrorCount2());
            if (v1 == v2) {
               v1 = evd1.getErrorCount1() + evd1.getErrorCount2();
               v2 = evd2.getErrorCount1() + evd2.getErrorCount2();
            }
            if (v1 == v2) {
               v1 = evd1.getErrorCallRatio1();
               v2 = evd2.getErrorCallRatio2();
            }
            if (v1 == v2) {
               v1 = evd1.getErrorCallRatio1() + evd1.getErrorCallRatio2();
               v2 = evd2.getErrorCallRatio2() + evd2.getErrorCallRatio2();
            }
            int result = v1 > v2 ? 1 : v1 < v2 ? -1 : 0;
            return sortAsc ? result : -result;
         }
      });

      // Trim to the number of requested rows
      int rows = metricCriteria.getNumRows() == null ? 0 : Integer.parseInt(metricCriteria.getNumRows());
      trimResultList(result, rows);

      return result;
   }

   @Override
   public List<MetricGroupData> getMetricsData(MetricCriteria metricCriteria,
            MetricResourceCriteria metricResourceCriteria) {
      List<MetricGroupData> result = new ArrayList<MetricGroupData>();
      Map<Long, MetricGraphData> metricGraphDataByTime = new TreeMap<Long, MetricGraphData>();
      try {
         // STEP 1. Decode the input parameters
         String encodedMetricName = metricCriteria.getMetricName();
         String metricName = decodeMetricName(encodedMetricName);

         Map<String, List<String>> filters = new HashMap<String, List<String>>();
         for (ResourceEntity resourceEntityType : ResourceEntity.values()) {
            List<String> resourceEntityNames = null;
            for (ResourceEntityRequest resourceEntityRequest : metricResourceCriteria.getResourceRequestEntities()) {
               if (resourceEntityRequest.getResourceEntityType() == resourceEntityType) {
                  resourceEntityNames = resourceEntityRequest.getResourceEntityName();
                  break;
               }
            }
            if (resourceEntityNames == null) {
               resourceEntityNames = Collections.emptyList();
            }
            if (!resourceEntityNames.isEmpty()) {
               filters.put(resourceEntityType.value(), resourceEntityNames);
            }
         }

         String groupBy = metricResourceCriteria.getResourceEntityResponseType();

         long firstStartTime = metricCriteria.getFirstStartTime();
         long secondStartTime = metricCriteria.getSecondStartTime();
         long duration = TimeUnit.SECONDS.toMillis(metricCriteria.getDuration());
         int aggregationPeriod = metricCriteria.getAggregationPeriod();
         boolean serverSide = !MonitoringSystem.COLLECTION_LOCATION_CLIENT.equals(metricCriteria.getRoleType());

         // STEP 2. Query the data
         Map<String, List<MetricValue<?>>> data1 = null;
         Map<String, List<MetricValue<?>>> data2 = null;
         if (ResourceEntity.SERVICE.value().equals(groupBy)) {
            // get the operation names for the service
            List<String> serviceNames = filters.get("Service");
            for (String serviceName : serviceNames) {
               // get the ipaddress for the service name
               List<String> ipAddressList = ipPerDayAndServiceNameDAO.findByDateAndServiceName(
                        System.currentTimeMillis(), serviceName);
               List<String> operationNames = metricsServiceOperationByIpDAO.findMetricOperationNames(serviceNames);
               operationNames = removeServiceNamePrefix(operationNames);
               filters.put("Operation", operationNames);
               data1 = metricValuesDAO.findMetricValuesByOperation(ipAddressList, metricName, firstStartTime,
                        firstStartTime + duration, serverSide, aggregationPeriod, filters);
               data2 = metricValuesDAO.findMetricValuesByOperation(ipAddressList, metricName, secondStartTime,
                        secondStartTime + duration, serverSide, aggregationPeriod, filters);
               // Move data to a better data structure
               Map<String, Object> map1 = transformAggregatedMetricComponentValues(encodedMetricName, data1);
               Map<String, Object> map2 = transformAggregatedMetricComponentValues(encodedMetricName, data2);
               for (String operation : operationNames) {
                  // STEP 3. Create and populate the return values
                  MetricGroupData metricGroupData = new MetricGroupData();
                  metricGroupData.setCount1((Double) map1.get(operation));
                  metricGroupData.setCount2((Double) map2.get(operation));
                  CriteriaInfo criteriaInfo = new CriteriaInfo();
                  criteriaInfo.setOperationName(operation);
                  metricGroupData.setCriteriaInfo(criteriaInfo);
                  result.add(metricGroupData);
               }
            }

         } else if (ResourceEntity.OPERATION.value().equals(groupBy)) {
            List<String> operationNames = filters.get("Operation");
            List<String> serviceNames = filters.get("Service");
            // get the operation names for the service
            if (operationNames == null || operationNames.isEmpty()) {
               operationNames = metricsServiceOperationByIpDAO.findMetricOperationNames(serviceNames);
               operationNames = removeServiceNamePrefix(operationNames);
               filters.put("Operation", operationNames);
            }
            for (String serviceName : serviceNames) {
               List<String> ipAddressList = ipPerDayAndServiceNameDAO.findByDateAndServiceName(
                        System.currentTimeMillis(), serviceName);
               data1 = metricValuesDAO.findMetricValuesByOperation(ipAddressList, metricName, firstStartTime,
                        firstStartTime + duration, serverSide, aggregationPeriod, filters);
               data2 = metricValuesDAO.findMetricValuesByOperation(ipAddressList, metricName, secondStartTime,
                        secondStartTime + duration, serverSide, aggregationPeriod, filters);
               // Move data to a better data structure
               Map<String, Object> map1 = transformAggregatedMetricComponentValues(encodedMetricName, data1);
               Map<String, Object> map2 = transformAggregatedMetricComponentValues(encodedMetricName, data2);
               operationNames = filters.get("Operation");
               for (String operation : operationNames) {
                  // STEP 3. Create and populate the return values
                  MetricGroupData metricGroupData = new MetricGroupData();
                  metricGroupData.setCount1((Double) map1.get(operation));
                  metricGroupData.setCount2((Double) map2.get(operation));
                  CriteriaInfo criteriaInfo = new CriteriaInfo();
                  criteriaInfo.setOperationName(operation);
                  metricGroupData.setCriteriaInfo(criteriaInfo);
                  result.add(metricGroupData);
               }

            }

         } else if (ResourceEntity.CONSUMER.value().equals(groupBy)) {
            List<String> operationNames = filters.get("Operation");
            List<String> serviceNames = filters.get("Service");
            // get the operation names for the service
            if (operationNames == null || operationNames.isEmpty()) {
               operationNames = metricsServiceOperationByIpDAO.findMetricOperationNames(serviceNames);
               operationNames = removeServiceNamePrefix(operationNames);
            }
            List<String> consumerNames = metricsServiceConsumerByIpDAO.findMetricConsumerNames(serviceNames);
            filters.put("Consumer", consumerNames);
            for (String serviceName : serviceNames) {
               List<String> ipAddressList = ipPerDayAndServiceNameDAO.findByDateAndServiceName(
                        System.currentTimeMillis(), serviceName);
               data1 = metricValuesDAO.findMetricValuesByConsumer(ipAddressList, metricName, firstStartTime,
                        firstStartTime + duration, serverSide, aggregationPeriod, serviceName, operationNames,
                        consumerNames);
               data2 = metricValuesDAO.findMetricValuesByConsumer(ipAddressList, metricName, secondStartTime,
                        secondStartTime + duration, serverSide, aggregationPeriod, serviceName, operationNames,
                        consumerNames);
               // Move data to a better data structure
               Map<String, Object> map1 = transformAggregatedMetricComponentValues(encodedMetricName, data1);
               Map<String, Object> map2 = transformAggregatedMetricComponentValues(encodedMetricName, data2);
               for (String consumerName : consumerNames) {
                  // STEP 3. Create and populate the return values
                  MetricGroupData metricGroupData = new MetricGroupData();
                  metricGroupData.setCount1((Double) map1.get(consumerName));
                  metricGroupData.setCount2((Double) map2.get(consumerName));
                  CriteriaInfo criteriaInfo = new CriteriaInfo();
                  criteriaInfo.setConsumerName(consumerName);
                  metricGroupData.setCriteriaInfo(criteriaInfo);
                  result.add(metricGroupData);
               }

            }

         } else if ("Error".equals(groupBy)) {
            // metricName = "SoaFwk.Op.Err.%";
            // data1 = metricsDAO.findMetricComponentValuesByMetric(metricName, firstStartTime, firstStartTime +
            // duration,
            // serverSide, aggregationPeriod, filters);
            // data2 = metricsDAO.findMetricComponentValuesByMetric(metricName, secondStartTime, secondStartTime +
            // duration,
            // serverSide, aggregationPeriod, filters);
         } else {
            throw new UnsupportedOperationException();
         }

         // Sort the results
         final boolean sortAsc = metricCriteria.getSortOrder() == SortOrderType.ASCENDING;
         Collections.sort(result, new Comparator<MetricGroupData>() {
            @Override
            public int compare(MetricGroupData mgd1, MetricGroupData mgd2) {
               double v1 = Math.max(mgd1.getCount1(), mgd1.getCount2());
               double v2 = Math.max(mgd2.getCount1(), mgd2.getCount2());
               if (v1 == v2) {
                  v1 = mgd1.getCount1() + mgd1.getCount2();
                  v2 = mgd2.getCount1() + mgd2.getCount2();
               }
               if (v1 == v2) {
                  v1 = mgd1.getCount2();
                  v2 = mgd2.getCount2();
               }
               int result = v1 > v2 ? 1 : v1 < v2 ? -1 : 0;
               return sortAsc ? result : -result;
            }
         });

         // Trim to the number of requested rows
         int rows = metricCriteria.getNumRows() == null ? 0 : Integer.parseInt(metricCriteria.getNumRows());
         trimResultList(result, rows);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return result;
   }

   @Override
   public List<String> getMetricsMetadata(String resourceEntityType, List<String> resourceEntityName,
            String resourceEntityResponseType) {
      ResourceEntity resourceEntity = ResourceEntity.fromValue(resourceEntityType);
      if (resourceEntity != ResourceEntity.SERVICE) {
         throw new IllegalArgumentException("Unsupported input ResourceEntity " + resourceEntityType);
      }

      ResourceEntity responseEntity = ResourceEntity.fromValue(resourceEntityResponseType);
      switch (responseEntity) {
         case SERVICE:
            return metricsServiceOperationByIpDAO.findMetricServiceAdminNames(resourceEntityName);
         case OPERATION:
            return metricsServiceOperationByIpDAO.findMetricOperationNames(resourceEntityName);
         case CONSUMER: {
            return metricsServiceConsumerByIpDAO.findMetricConsumerNames(resourceEntityName);
         }
         default:
            throw new IllegalArgumentException("Unsupported output ResourceEntity " + resourceEntityResponseType);
      }

   }

   @Override
   public MetricData getMetricSummaryData(String dc, MetricCriteria metricCriteria,
            MetricResourceCriteria metricResourceCriteria) {
      return null;
   }

   @Override
   public List<MetricGraphData> getMetricValue(CriteriaInfo criteriaInfo, long beginTime, long duration,
            int aggregationPeriod, String autoDelay) {
      List<MetricGraphData> result = new ArrayList<MetricGraphData>();
      Map<Long, MetricGraphData> metricGraphDataByTime = new TreeMap<Long, MetricGraphData>();
      String encodedMetricName = criteriaInfo.getMetricName();
      String metricName = decodeMetricName(encodedMetricName);

      long endTime = beginTime + TimeUnit.SECONDS.toMillis(duration);
      boolean serverSide = MonitoringSystem.COLLECTION_LOCATION_SERVER.equals(criteriaInfo.getRoleType());
      boolean totalizeResultsPerService = false;
      Map<String, List<String>> filters = new HashMap<String, List<String>>();
      if (criteriaInfo.getServiceName() != null) {
         filters.put("Service", Arrays.asList(criteriaInfo.getServiceName().trim()));
      }
      if (criteriaInfo.getOperationName() != null) {
         filters.put("Operation", Arrays.asList(criteriaInfo.getOperationName().trim()));
      }
      if (criteriaInfo.getConsumerName() != null) {
         filters.put("Consumer", Arrays.asList(criteriaInfo.getConsumerName().trim()));
      }

      Map<String, List<MetricValue<?>>> metricValuesMap;
      try {
         List<String> serviceNames = filters.get("Service");
         List<String> operationNames = filters.get("Operation");
         List<String> consumerNames = filters.get("Consumer");
         if (operationNames == null || operationNames.isEmpty()) {
            // get the operation names for the service
            operationNames = metricsServiceOperationByIpDAO.findMetricOperationNames(serviceNames);
            operationNames = removeServiceNamePrefix(operationNames);
            filters.put("Operation", operationNames);
            totalizeResultsPerService = true;
         }
         if (consumerNames != null && !consumerNames.isEmpty()) {// iterate by consumer
            for (String serviceName : serviceNames) {
               // get the ipaddress for the service name
               List<String> ipAddressList = ipPerDayAndServiceNameDAO.findByDateAndServiceName(
                        System.currentTimeMillis(), serviceName);
               metricValuesMap = metricValuesDAO.findMetricValuesByConsumer(ipAddressList, metricName, beginTime,
                        endTime, serverSide, aggregationPeriod, serviceName, operationNames, consumerNames);
               for (String consumerName : consumerNames) {
                  List<MetricValue<?>> metricValues = metricValuesMap.get(consumerName);

                  for (int i = 0; i < duration / aggregationPeriod; ++i) {
                     long startTime = beginTime + TimeUnit.SECONDS.toMillis(i * aggregationPeriod);
                     long stopTime = startTime + TimeUnit.SECONDS.toMillis(aggregationPeriod);
                     double value = 0;
                     for (MetricValue<?> metricValue : metricValues) {
                        long time = metricValue.getTimeMiliseconds();
                        if (startTime <= time && time < stopTime) {
                           value += metricValue.getValueForMetric(encodedMetricName);
                           break;
                        }
                     }
                     MetricGraphData metricGraphData = new MetricGraphData();
                     metricGraphData.setCount(value);
                     metricGraphData.setTimeSlot(startTime);
                     metricGraphData.setCriteria(null); // Not supported for now
                     if (totalizeResultsPerService) {
                        MetricGraphData alreadyStoredMetricGraph = null;
                        if (metricGraphDataByTime.containsKey(startTime)) {
                           alreadyStoredMetricGraph = metricGraphDataByTime.get(startTime);
                           alreadyStoredMetricGraph.setCount(metricGraphData.getCount()
                                    + alreadyStoredMetricGraph.getCount());
                        } else {
                           result.add(metricGraphData);
                           metricGraphDataByTime.put(startTime, metricGraphData);
                        }
                     } else {
                        result.add(metricGraphData);
                     }
                  }
               }
            }
         } else {// go by operations instead
            for (String serviceName : serviceNames) {
               // get the ipaddress for the service name
               List<String> ipAddressList = ipPerDayAndServiceNameDAO.findByDateAndServiceName(
                        System.currentTimeMillis(), serviceName);
               metricValuesMap = metricValuesDAO.findMetricValuesByOperation(ipAddressList, metricName, beginTime,
                        endTime, serverSide, aggregationPeriod, filters);
               operationNames = filters.get("Operation");
               for (String opName : operationNames) {
                  List<MetricValue<?>> metricValues = metricValuesMap.get(opName);

                  for (int i = 0; i < duration / aggregationPeriod; ++i) {
                     long startTime = beginTime + TimeUnit.SECONDS.toMillis(i * aggregationPeriod);
                     long stopTime = startTime + TimeUnit.SECONDS.toMillis(aggregationPeriod);
                     double value = 0;
                     for (MetricValue<?> metricValue : metricValues) {
                        long time = metricValue.getTimeMiliseconds();
                        if (startTime <= time && time < stopTime) {
                           value += metricValue.getValueForMetric(encodedMetricName);
                           break;
                        }
                     }
                     MetricGraphData metricGraphData = new MetricGraphData();
                     metricGraphData.setCount(value);
                     metricGraphData.setTimeSlot(startTime);
                     metricGraphData.setCriteria(null); // Not supported for now
                     if (totalizeResultsPerService) {
                        MetricGraphData alreadyStoredMetricGraph = null;
                        if (metricGraphDataByTime.containsKey(startTime)) {
                           alreadyStoredMetricGraph = metricGraphDataByTime.get(startTime);
                           alreadyStoredMetricGraph.setCount(metricGraphData.getCount()
                                    + alreadyStoredMetricGraph.getCount());
                        } else {
                           result.add(metricGraphData);
                           metricGraphDataByTime.put(startTime, metricGraphData);
                        }
                     } else {
                        result.add(metricGraphData);
                     }

                  }
               }
            }
         }

      } catch (ServiceException e) {
         e.printStackTrace();
      }

      return result;
   }

   @Override
   public List<PolicyMetricData> getPolicyMetricData(long startTime, long endTime, String policyType,
            String policyName, String serviceName, String operationName, String subjectTypeName, String subjectValue,
            String effect) {
      return null;
   }

   @Override
   public List<PolicyMetricGraphData> getPolicyMetricDetailData(String policyName, String serviceName,
            String operationName, String subjectTypeName, String subjectValue, String listType, long startTime,
            long endTime) {
      return null;
   }

   @Override
   public List<MetricData> getStandardReportData(String reportType, MetricCriteria metricCriteria) {
      return null;
   }

   private CriteriaInfo populateCriteriaInfo(String groupBy, Map<String, Object> row) {
      CriteriaInfo criteriaInfo = new CriteriaInfo();
      criteriaInfo.setServiceConsumerType(groupBy);
      String metricName = (String) row.get("metricName");
      criteriaInfo.setMetricName(metricName);
      boolean serverSide = (Boolean) row.get("serverSide");
      criteriaInfo.setRoleType(serverSide ? MonitoringSystem.COLLECTION_LOCATION_SERVER
               : MonitoringSystem.COLLECTION_LOCATION_CLIENT);
      String consumerName = (String) row.get("consumerName");
      if (consumerName != null) {
         criteriaInfo.setConsumerName(consumerName);
      }
      String serviceAdminName = (String) row.get("serviceAdminName");
      if (serviceAdminName != null) {
         criteriaInfo.setServiceName(serviceAdminName);
      }
      String operationName = (String) row.get("operationName");
      if (operationName != null) {
         criteriaInfo.setOperationName(operationName);
      }
      String machineName = (String) row.get("machineName");
      if (machineName != null) {
         criteriaInfo.setMachineName(machineName);
      }
      String machineGroupName = (String) row.get("machineGroupName");
      if (machineGroupName != null) {
         criteriaInfo.setPoolName(machineGroupName);
      }
      return criteriaInfo;
   }

   private Map<String, List<String>> populateFilters(final List<String> serviceNames,
            final List<String> operationNames, final List<String> consumerNames) {

      Map<String, List<String>> filters = new HashMap<String, List<String>>();

      if (serviceNames != null && !serviceNames.isEmpty()) {
         filters.put(ResourceEntity.SERVICE.value(), serviceNames);
      }

      if (operationNames != null && !operationNames.isEmpty()) {
         filters.put(ResourceEntity.OPERATION.value(), operationNames);
      }

      if (consumerNames != null && !consumerNames.isEmpty()) {
         filters.put(ResourceEntity.CONSUMER.value(), consumerNames);
      }

      return filters;
   }

   private List<String> removeServiceNamePrefix(List<String> operationNames) {
      List<String> result = new ArrayList<String>();
      for (String operationName : operationNames) {
         result.add(operationName.substring(operationName.indexOf(".") + 1));
      }

      return result;
   }

   private double retrieveCallCounts(long startTime, long duration, double calls1) {
      List<String> metricValuesList = new ArrayList<String>();
      List<?> valuesByIpAndDateList = metricValuesByIpAndDateDAO.findByRange(Long.valueOf(startTime),
               Long.valueOf(startTime + duration));

      for (Object superModel : valuesByIpAndDateList) {
         Map<Long, Model> superColumns = ((SuperModel<String, Long>) superModel).getColumns();

         for (Entry<Long, Model> superColumn : superColumns.entrySet()) {
            Model<String> model = superColumn.getValue();
            Map<String, Object> columns = model.getColumns();
            for (Entry<String, Object> column : columns.entrySet()) {
               if (column.getKey().contains(SystemMetricDefs.OP_TIME_TOTAL.getMetricName())) {
                  metricValuesList.add(column.getKey());
               }
            }
         }
      }
      for (String metricValueKey : metricValuesList) {
         org.ebayopensource.turmeric.monitoring.provider.model.MetricValue<?> metricValue = metricValuesDAO
                  .find(metricValueKey);
         Map<String, Object> columns = metricValue.getColumns();
         if (columns != null && !columns.isEmpty()) {
            if (columns.containsKey("count")) {
               Object value = columns.get("count");
               if (value != null) {
                  // calls1 = calls1 + (Long) value;
                  calls1 += 1;
               }
            }
         }
      }
      return calls1;
   }

   private Map<String, Map<String, Object>> transformAggregatedErrorValues(List<Map<String, Object>> rows) {
      Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
      for (Map<String, Object> row : rows) {
         long errorId = (Long) row.get("errorId");
         result.put(String.valueOf(errorId), row);
      }
      return result;
   }

   private Map<String, Object> transformAggregatedMetricComponentValues(String encodedMetricName,
            Map<String, List<MetricValue<?>>> rows) {
      Map<String, Object> result = new TreeMap<String, Object>();
      if ("CallCount".equals(encodedMetricName)) {
         for (String key : rows.keySet()) {
            double total = 0.0D;
            for (MetricValue<?> metricValue : rows.get(key)) {
               total += (Long) metricValue.getColumns().get("count");
               // if(metricValue.getColumns().get("count")!=null){
               // total += 1;
               // }
            }
            result.put(key, total);
         }
      } else if ("ResponseTime".equals(encodedMetricName)) {
         for (String key : rows.keySet()) {
            double total = 0.0D;
            for (MetricValue<?> metricValue : rows.get(key)) {
               total += (Double) metricValue.getColumns().get("totalTime");
            }
            result.put(key, total);
         }
      } else {
         for (String key : rows.keySet()) {
            double total = 0.0D;
            for (MetricValue<?> metricValue : rows.get(key)) {
               total += (Long) metricValue.getColumns().get("value");
            }
            result.put(key, total);
         }
      }

      return result;

   }

   private void trimResultList(List<?> list, int maxRows) {
      if (maxRows > 0 && list.size() > maxRows) {
         list.subList(0, maxRows);
      }
   }

}