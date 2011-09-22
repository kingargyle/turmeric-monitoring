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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.MetricIdentifier;

import org.ebayopensource.turmeric.monitoring.provider.dao.BaseMetricsErrorsByFilterDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorByIdDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorValuesDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsServiceOperationByIpDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorByIdDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorValuesDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorsByCategoryDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorsBySeverityDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsServiceOperationByIpDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.model.ExtendedErrorViewData;
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
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.SystemMetricDefs;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById;
import org.ebayopensource.turmeric.utils.ContextUtils;
import org.ebayopensource.turmeric.utils.cassandra.service.CassandraManager;

/**
 * The Class SOAMetricsQueryServiceCassandraProviderImpl.
 * 
 * @author jamuguerza
 */
public class SOAMetricsQueryServiceCassandraProviderImpl implements
		SOAMetricsQueryServiceProvider {

	private final MetricsErrorByIdDAO metricsErrorByIdDAO;

	private final MetricsErrorValuesDAO metricsErrorValuesDAO;

	private final BaseMetricsErrorsByFilterDAO metricsErrorsByCategoryDAO;

	private final BaseMetricsErrorsByFilterDAO metricsErrorsBySeverityDAO;

	private final MetricsServiceOperationByIpDAO metricsServiceOperationByIpDAO;

	/** The Constant cassandraPropFilePath. */
	private static final String cassandraPropFilePath = "META-INF/config/cassandra/cassandra.properties";

	/** The Constant c_hostIp. */
	private static final String c_clusterName = "cassandra-rl-cluster-name";

	/** The Constant c_hostIp. */
	private static final String c_hostIp = "cassandra-host-ip";

	/** The Constant c_rpcPort. */
	private static final String c_rpcPort = "cassandra-rpc-port";

	/** The Constant c_keyspace. */
	private static final String c_keyspace = "cassandra-monitoring-keyspace";

	/** The Constant c_error_by_id_cf. */
	private static final String c_error_by_id_cf = "cassandra-error-by-id-cf";

	/** The Constant c_error_values_cf. */
	private static final String c_error_values_cf = "cassandra-error-values-cf";

	/** The Constant c_errors_by_category_cf. */
	private static final String c_errors_by_category_cf = "cassandra-errors-by-category-cf";

	/** The Constant c_errors_by_severity_cf. */
	private static final String c_errors_by_severity_cf = "cassandra-errors-by-severity-cf";

	/** The Constant c_metrics_cf. */
	private static final String c_metrics_cf = "cassandra-metrics-cf";

	/** The host. */
	private static String host;

	/** The keyspace. */
	private static String keyspace;

	private static String clusterName;

	private static String errorByIdCF;

	private static String errorValuesCF;

	private static String errorsByCategoryCF;

	private static String errorsBySeverityCF;

	private static String metricsCF;

	{
		CassandraManager.initialize();
		getCassandraConfig();
	}

	/**
	 * Instantiates a new Metrics Query Service cassandra provider impl.
	 */
	public SOAMetricsQueryServiceCassandraProviderImpl() {

		metricsErrorByIdDAO = new MetricsErrorByIdDAOImpl(clusterName,
				host, keyspace, errorByIdCF);

		metricsErrorValuesDAO = new MetricsErrorValuesDAOImpl(clusterName,
				host, keyspace, errorValuesCF);

		metricsErrorsByCategoryDAO = new MetricsErrorsByCategoryDAOImpl(
				clusterName, host, keyspace, errorsByCategoryCF,
				metricsErrorValuesDAO);

		metricsErrorsBySeverityDAO = new MetricsErrorsBySeverityDAOImpl(
				clusterName, host, keyspace, errorsBySeverityCF,
				metricsErrorValuesDAO);

		metricsServiceOperationByIpDAO = new MetricsServiceOperationByIpDAOImpl(clusterName, host, keyspace, metricsCF);
	}

	/**
	 * Gets the cassandra config.
	 * 
	 * @return the cassandra config
	 */
	private static void getCassandraConfig() {
		ClassLoader classLoader = ContextUtils.getClassLoader();
		InputStream inStream = classLoader
				.getResourceAsStream(cassandraPropFilePath);

		if (inStream != null) {
			Properties properties = new Properties();
			try {
				properties.load(inStream);
				clusterName = properties.getProperty(c_clusterName);
				host = (String) properties.get(c_hostIp) + ":"
						+ (String) properties.get(c_rpcPort);

				keyspace = (String) properties.get(c_keyspace);

				errorByIdCF = (String) properties.get(c_error_by_id_cf);
				errorValuesCF = (String) properties.get(c_error_values_cf);
				errorsByCategoryCF = (String) properties
						.get(c_errors_by_category_cf);
				errorsBySeverityCF = (String) properties
						.get(c_errors_by_severity_cf);

				metricsCF = (String) properties.get(c_metrics_cf);

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
	public List<MetricData> getCustomReportData(ReportCriteria reportCriteria,
			MetricCriteria metricCriteria) {
		return null;
	}

	@Override
	public List<MetricData> getDetailData(String dc,
			MetricCriteria metricCriteria,
			MetricResourceCriteria metricResourceCriteria) {
		return null;
	}

	@Override
	public List<MetricGraphData> getErrorGraph(final String serviceName,
			final String operationName, final String consumerName,
			final String errorId, final String errorCategory,
			final String errorSeverity, final MetricCriteria metricCriteria) {

		List<MetricGraphData> result = new ArrayList<MetricGraphData>();

		final String filter = getFilter(errorCategory, errorSeverity);

		int aggregationPeriod = metricCriteria.getAggregationPeriod();
		long beginTime = metricCriteria.getFirstStartTime();
		long duration = metricCriteria.getDuration();
		long endTime = beginTime + TimeUnit.SECONDS.toMillis(duration);
		boolean serverSide = MonitoringSystem.COLLECTION_LOCATION_SERVER
				.equals(metricCriteria.getRoleType());

		Map<String, List<String>> filters = new HashMap<String, List<String>>();
		if (serviceName != null && !"".equals(serviceName)) {
			filters.put(ResourceEntity.SERVICE.value(),
					Collections.singletonList(serviceName));
		}
		if (operationName != null && !"".equals(operationName)) {
			filters.put(ResourceEntity.OPERATION.value(),
					Collections.singletonList(operationName));
		}
		if (consumerName != null && !"".equals(consumerName)) {
			filters.put(ResourceEntity.CONSUMER.value(),
					Collections.singletonList(consumerName));
		}

		List<Map<String, Object>> queryResult = metricsErrorsByCategoryDAO
				.findErrorValuesByFilter(beginTime, endTime, serverSide,
						aggregationPeriod, null, filter, filters);

		for (int i = 0; i < duration / aggregationPeriod; ++i) {
			long startTime = beginTime
					+ TimeUnit.SECONDS.toMillis(i * aggregationPeriod);
			long stopTime = startTime
					+ TimeUnit.SECONDS.toMillis(aggregationPeriod);
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

	private String getFilter(final String errorCategory,
			final String errorSeverity) {
		String filter = null;

		if (errorCategory != null) {
			filter = ErrorCategory.fromValue(errorCategory).value();
		} else if (errorSeverity != null) {
			filter = ErrorSeverity.fromValue(errorSeverity).value();
		}

		return filter;
	}

	@Override
	@Deprecated
	public List<ErrorViewData> getErrorMetricsData(final String errorType,
			final List<String> serviceNames, final List<String> operationNames,
			final List<String> consumerNames, final String errorIdString,
			final String errorCategory, final String errorSeverity,
			final String isErrorId, final MetricCriteria metricCriteria) {
		long firstStartTime = metricCriteria.getFirstStartTime();
		long secondStartTime = metricCriteria.getSecondStartTime();
		long duration = TimeUnit.SECONDS.toMillis(metricCriteria.getDuration());
		int aggregationPeriod = metricCriteria.getAggregationPeriod();
		boolean serverSide = !MonitoringSystem.COLLECTION_LOCATION_CLIENT
				.equals(metricCriteria.getRoleType());

		// Validate input parameters
		Long errorId = errorIdString == null ? null : Long
				.parseLong(errorIdString);

		final String filter = getFilter(errorCategory, errorSeverity);

		Map<String, List<String>> filters = populateFilters(serviceNames,
				operationNames, consumerNames);

		List<Map<String, Object>> errors1;
		List<Map<String, Object>> errors2;
		if ("Category".equals(errorType)) {
			errors1 = metricsErrorsByCategoryDAO.findErrorValuesByFilter(
					firstStartTime, firstStartTime + duration, serverSide,
					aggregationPeriod, errorId, filter, filters);
			errors2 = metricsErrorsByCategoryDAO.findErrorValuesByFilter(
					secondStartTime, secondStartTime + duration, serverSide,
					aggregationPeriod, errorId, filter, filters);
		} else if ("Severity".equals(errorType)) {
			errors1 = metricsErrorsBySeverityDAO.findErrorValuesByFilter(
					firstStartTime, firstStartTime + duration, serverSide,
					aggregationPeriod, errorId, filter, filters);
			errors2 = metricsErrorsBySeverityDAO.findErrorValuesByFilter(
					secondStartTime, secondStartTime + duration, serverSide,
					aggregationPeriod, errorId, filter, filters);
		} else {
			throw new IllegalArgumentException("Invalid error type "
					+ errorType);
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
			errorViewData.setErrorCount2(count2);

			errorViewData.setErrorCount1(0);
			result.add(errorViewData);
		}

		// Sort the results
		final boolean sortAsc = metricCriteria.getSortOrder() == SortOrderType.ASCENDING;
		Collections.sort(result, new Comparator<ErrorViewData>() {
			@Override
			public int compare(ErrorViewData evd1, ErrorViewData evd2) {
				double v1 = Math.max(evd1.getErrorCount1(),
						evd1.getErrorCount2());
				double v2 = Math.max(evd2.getErrorCount1(),
						evd2.getErrorCount2());
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
		int rows = metricCriteria.getNumRows() == null ? 0 : Integer
				.parseInt(metricCriteria.getNumRows());
		trimResultList(result, rows);

		return result;
	}

	@Override
	public List<ExtendedErrorViewData> getExtendedErrorMetricsData(
			String errorType, List<String> serviceNames,
			List<String> operationNames, List<String> consumerNames,
			String errorIdString, String errorCategory, String errorSeverity,
			String errorNameParam, MetricCriteria metricCriteria) {
		long firstStartTime = metricCriteria.getFirstStartTime();
		long secondStartTime = metricCriteria.getSecondStartTime();
		long duration = TimeUnit.SECONDS.toMillis(metricCriteria.getDuration());
		int aggregationPeriod = metricCriteria.getAggregationPeriod();
		boolean serverSide = !MonitoringSystem.COLLECTION_LOCATION_CLIENT
				.equals(metricCriteria.getRoleType());

		// Validate input parameters
		Long errorId = errorIdString == null ? null : Long
				.parseLong(errorIdString);

		final String filter = getFilter(errorCategory, errorSeverity);

		Map<String, List<String>> filters = populateFilters(serviceNames,
				operationNames, consumerNames);

		List<Map<String, Object>> errors1;
		double calls1;
		List<Map<String, Object>> errors2;
		double calls2;

		if ("Category".equals(errorType)) {
			errors1 = metricsErrorsByCategoryDAO.findErrorValuesByFilter(
					firstStartTime, firstStartTime + duration, serverSide,
					aggregationPeriod, errorId, filter, filters);
			calls1 = 0L;
//			 calls1 = metricsDAO.findCallsCount(firstStartTime, firstStartTime
//			 + duration, serverSide,
//			 aggregationPeriod);
			errors2 = metricsErrorsByCategoryDAO.findErrorValuesByFilter(
					secondStartTime, secondStartTime + duration, serverSide,
					aggregationPeriod, errorId, filter, filters);
			calls2 = 0L;
			// calls2 = metricsDAO.findCallsCount(secondStartTime,
			// secondStartTime + duration, serverSide,
			// aggregationPeriod);
		} else if ("Severity".equals(errorType)) {
			errors1 = metricsErrorsBySeverityDAO.findErrorValuesByFilter(
					firstStartTime, firstStartTime + duration, serverSide,
					aggregationPeriod, errorId, filter, filters);
			calls1 = 0L;
			// calls1 = metricsDAO.findCallsCount(firstStartTime, firstStartTime
			// + duration, serverSide,
			// aggregationPeriod);
			errors2 = metricsErrorsBySeverityDAO.findErrorValuesByFilter(
					secondStartTime, secondStartTime + duration, serverSide,
					aggregationPeriod, errorId, filter, filters);
			calls2 = 0L;
			// calls2 = metricsDAO.findCallsCount(secondStartTime,
			// secondStartTime + duration, serverSide,
			// aggregationPeriod);
		} else {
			throw new IllegalArgumentException("Invalid error type "
					+ errorType);
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
				double v1 = Math.max(evd1.getErrorCount1(),
						evd1.getErrorCount2());
				double v2 = Math.max(evd2.getErrorCount1(),
						evd2.getErrorCount2());
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
		int rows = metricCriteria.getNumRows() == null ? 0 : Integer
				.parseInt(metricCriteria.getNumRows());
		trimResultList(result, rows);

		return result;
	}

	private Map<String, List<String>> populateFilters(
			final List<String> serviceNames, final List<String> operationNames,
			final List<String> consumerNames) {

		Map<String, List<String>> filters = new HashMap<String, List<String>>();

		if (!serviceNames.isEmpty()) {
			filters.put(ResourceEntity.SERVICE.value(), serviceNames);
		}

		if (!operationNames.isEmpty()) {
			filters.put(ResourceEntity.OPERATION.value(), operationNames);
		}

		if (!consumerNames.isEmpty()) {
			filters.put(ResourceEntity.CONSUMER.value(), consumerNames);
		}

		return filters;
	}

	@Override
	public ErrorInfos getErrorMetricsMetadata(String errorId, String errorName,
			String serviceName) {
		ErrorById error = metricsErrorByIdDAO.find(errorId);
		if (error != null) {
			ErrorInfos result = new ErrorInfos();
			result.setId(String.valueOf(error.getErrorId()));
			result.setName(error.getName());
			result.setCategory(error.getCategory());
			result.setSeverity(error.getSeverity());
			result.setDomain(error.getDomain());
			result.setSubDomain(error.getSubDomain());
			return result;
		}
		return null;
	}

	@Override
	public List<MetricGroupData> getMetricsData(MetricCriteria metricCriteria,
			MetricResourceCriteria metricResourceCriteria) {

//		// STEP 1. Decode the input parameters
//		String encodedMetricName = metricCriteria.getMetricName();
//		String metricName = decodeMetricName(encodedMetricName);
//
//		Map<String, List<String>> filters = new HashMap<String, List<String>>();
//		for (ResourceEntity resourceEntityType : ResourceEntity.values()) {
//			List<String> resourceEntityNames = null;
//			for (ResourceEntityRequest resourceEntityRequest : metricResourceCriteria
//					.getResourceRequestEntities()) {
//				if (resourceEntityRequest.getResourceEntityType() == resourceEntityType) {
//					resourceEntityNames = resourceEntityRequest
//							.getResourceEntityName();
//					break;
//				}
//			}
//			if (resourceEntityNames == null)
//				resourceEntityNames = Collections.emptyList();
//			if (!resourceEntityNames.isEmpty())
//				filters.put(resourceEntityType.value(), resourceEntityNames);
//		}
//
//		String groupBy = metricResourceCriteria.getResourceEntityResponseType();
//
//		long firstStartTime = metricCriteria.getFirstStartTime();
//		long secondStartTime = metricCriteria.getSecondStartTime();
//		long duration = TimeUnit.SECONDS.toMillis(metricCriteria.getDuration());
//		int aggregationPeriod = metricCriteria.getAggregationPeriod();
//		boolean serverSide = !MonitoringSystem.COLLECTION_LOCATION_CLIENT
//				.equals(metricCriteria.getRoleType());
//
//		// STEP 2. Query the data
//		List<Map<String, MetricIdentifier>> data1;
//		List<Map<String, MetricIdentifier>> data2;
//		if (ResourceEntity.SERVICE.value().equals(groupBy)) {
//			data1 = metricsDAO.findMetricComponentValuesByService(metricName,
//					firstStartTime, firstStartTime + duration, serverSide,
//					aggregationPeriod, filters);
//			data2 = metricsDAO.findMetricComponentValuesByService(metricName,
//					secondStartTime, secondStartTime + duration, serverSide,
//					aggregationPeriod, filters);
//		} else if (ResourceEntity.OPERATION.value().equals(groupBy)) {
//			data1 = metricsDAO.findMetricComponentValuesByOperation(metricName,
//					firstStartTime, firstStartTime + duration, serverSide,
//					aggregationPeriod, filters);
//			data2 = metricsDAO.findMetricComponentValuesByOperation(metricName,
//					secondStartTime, secondStartTime + duration, serverSide,
//					aggregationPeriod, filters);
//		} else if (ResourceEntity.CONSUMER.value().equals(groupBy)) {
//			data1 = metricsDAO.findMetricComponentValuesByConsumer(metricName,
//					firstStartTime, firstStartTime + duration, serverSide,
//					aggregationPeriod, filters);
//			data2 = metricsDAO.findMetricComponentValuesByConsumer(metricName,
//					secondStartTime, secondStartTime + duration, serverSide,
//					aggregationPeriod, filters);
//		} else if ("Error".equals(groupBy)) {
//			metricName = "SoaFwk.Op.Err.%";
//			data1 = metricsDAO.findMetricComponentValuesByMetric(metricName,
//					firstStartTime, firstStartTime + duration, serverSide,
//					aggregationPeriod, filters);
//			data2 = metricsDAO.findMetricComponentValuesByMetric(metricName,
//					secondStartTime, secondStartTime + duration, serverSide,
//					aggregationPeriod, filters);
//		} else {
//			throw new UnsupportedOperationException();
//		}
//
//		// Move data to a better data structure
//		Map<String, Map<String, Object>> map1 = transformAggregatedMetricComponentValues(
//				encodedMetricName, data1);
//		Map<String, Map<String, Object>> map2 = transformAggregatedMetricComponentValues(
//				encodedMetricName, data2);
//
//		// STEP 3. Create and populate the return values
//		List<MetricGroupData> result = new ArrayList<MetricGroupData>();
//		for (Map.Entry<String, Map<String, Object>> entry : map1.entrySet()) {
//			MetricGroupData metricGroupData = new MetricGroupData();
//			Map<String, Object> row1 = entry.getValue();
//			Double count1 = (Double) row1.get("value");
//			metricGroupData.setCount1(count1);
//			Map<String, Object> row2 = map2.remove(entry.getKey());
//			if (row2 != null) {
//				Double count2 = (Double) row2.get("value");
//				metricGroupData.setCount2(count2);
//			} else {
//				metricGroupData.setCount2(0);
//			}
//
//			CriteriaInfo criteriaInfo = populateCriteriaInfo(groupBy, row1);
//			metricGroupData.setCriteriaInfo(criteriaInfo);
//
//			result.add(metricGroupData);
//		}
//		for (Map.Entry<String, Map<String, Object>> entry : map2.entrySet()) {
//			MetricGroupData metricGroupData = new MetricGroupData();
//			Map<String, Object> row2 = entry.getValue();
//			Double count2 = (Double) row2.get("value");
//			metricGroupData.setCount2(count2);
//			metricGroupData.setCount1(0);
//
//			CriteriaInfo criteriaInfo = populateCriteriaInfo(groupBy, row2);
//			metricGroupData.setCriteriaInfo(criteriaInfo);
//
//			result.add(metricGroupData);
//		}
//
//		// Sort the results
//		final boolean sortAsc = metricCriteria.getSortOrder() == SortOrderType.ASCENDING;
//		Collections.sort(result, new Comparator<MetricGroupData>() {
//			@Override
//			public int compare(MetricGroupData mgd1, MetricGroupData mgd2) {
//				double v1 = Math.max(mgd1.getCount1(), mgd1.getCount2());
//				double v2 = Math.max(mgd2.getCount1(), mgd2.getCount2());
//				if (v1 == v2) {
//					v1 = mgd1.getCount1() + mgd1.getCount2();
//					v2 = mgd2.getCount1() + mgd2.getCount2();
//				}
//				if (v1 == v2) {
//					v1 = mgd1.getCount2();
//					v2 = mgd2.getCount2();
//				}
//				int result = v1 > v2 ? 1 : v1 < v2 ? -1 : 0;
//				return sortAsc ? result : -result;
//			}
//		});
//
//		// Trim to the number of requested rows
//		int rows = metricCriteria.getNumRows() == null ? 0 : Integer
//				.parseInt(metricCriteria.getNumRows());
//		trimResultList(result, rows);
//
//		return result;
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
        if (consumerName != null)
            criteriaInfo.setConsumerName(consumerName);
        String serviceAdminName = (String) row.get("serviceAdminName");
        if (serviceAdminName != null)
            criteriaInfo.setServiceName(serviceAdminName);
        String operationName = (String) row.get("operationName");
        if (operationName != null)
            criteriaInfo.setOperationName(operationName);
        String machineName = (String) row.get("machineName");
        if (machineName != null)
            criteriaInfo.setMachineName(machineName);
        String machineGroupName = (String) row.get("machineGroupName");
        if (machineGroupName != null)
            criteriaInfo.setPoolName(machineGroupName);
        return criteriaInfo;
    }
    
	private Map<String, Map<String, Object>> transformAggregatedMetricComponentValues(
			String encodedMetricName, List<Map<String, MetricIdentifier>> rows) {
//		Map<String, Map<String, MetricIdentifier>> result = new HashMap<String, Map<String, MetricIdentifier>>();
//		Double totalCount = 0.0D;
//		for (Map<String, MetricIdentifier> row : rows) {
//			// Need to create a key that is the same for the N
//			// metricComponentDefs
//			Map<String, MetricIdentifier> keyMap = new TreeMap<String, MetricIdentifier>(row);
//			keyMap.remove("value");
//			keyMap.remove("metricComponentDef");
//			String key = keyMap.toString();
//
//			Map<String, MetricIdentifier> existingRow = result.get(key);
//			if (existingRow != null) {
//				MetricComponentDef metricComponentDef = (MetricComponentDef) row
//						.get("metricComponentDef");
//				if ("CallCount".equals(encodedMetricName)) {
//					if ("count".equals(metricComponentDef.getName())) {
//						// Overwrite the value from the existing row, we are
//						// interested in the call count
//						result.put(key, row);
//					}
//				} else if ("ResponseTime".equals(encodedMetricName)) {
//					if ("count".equals(metricComponentDef.getName())) {
//						Double count = (Double) row.get("value");
//						existingRow.put("value", count == 0 ? 0.0D
//								: (Double) existingRow.get("value"));
//						totalCount += count;
//					} else {
//						Double count = (Double) existingRow.get("value");
//						existingRow.put("value", count == 0 ? 0.0D
//								: (Double) row.get("value"));
//						totalCount += count;
//					}
//				} else {
//					throw new IllegalArgumentException("Unknown metric name "
//							+ encodedMetricName);
//				}
//			} else {
//				result.put(key, row);
//			}
//		}
//		if (("ResponseTime".equals(encodedMetricName) && totalCount > 0.0d)) {
//			// need to avg each element by the total Count, for the
//			// getMetricValue case
//			for (Map<String, Object> mapToAvg : result.values()) {
//				Double valueToAvg = (Double) mapToAvg.get("value");
//				if (valueToAvg != null) {
//					valueToAvg = valueToAvg / totalCount;
//					mapToAvg.put("value", valueToAvg);
//				}
//			}
//		}
//		return result;
		return null;
	}

	private String decodeMetricName(String encodedMetricName) {
		String metricName;
		if ("CallCount".equals(encodedMetricName))
			metricName = SystemMetricDefs.OP_TIME_TOTAL.getMetricName();
		else if ("ResponseTime".equals(encodedMetricName))
			metricName = SystemMetricDefs.OP_TIME_TOTAL.getMetricName();
		else if ("ErrorCount".equals(encodedMetricName))
			metricName = SystemMetricDefs.OP_ERR_TOTAL.getMetricName();
		else
			throw new IllegalArgumentException("Unknown metric name "
					+ encodedMetricName);
		return metricName;
	}

	@Override
	public MetricData getMetricSummaryData(String dc,
			MetricCriteria metricCriteria,
			MetricResourceCriteria metricResourceCriteria) {
		return null;
	}

	@Override
	public List<MetricGraphData> getMetricValue(CriteriaInfo criteriaInfo,
			long startTime, long duration, int aggregationPeriod,
			String autoDelay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PolicyMetricData> getPolicyMetricData(long startTime,
			long endTime, String policyType, String policyName,
			String serviceName, String operationName, String subjectTypeName,
			String subjectValue, String effect) {
		return null;
	}

	@Override
	public List<PolicyMetricGraphData> getPolicyMetricDetailData(
			String policyName, String serviceName, String operationName,
			String subjectTypeName, String subjectValue, String listType,
			long startTime, long endTime) {
		return null;
	}

	@Override
	public List<MetricData> getStandardReportData(String reportType,
			MetricCriteria metricCriteria) {
		return null;
	}

	@Override
	public List<String> getMetricsMetadata(String resourceEntityType,
			List<String> resourceEntityName, String resourceEntityResponseType) {
		ResourceEntity resourceEntity = ResourceEntity
				.fromValue(resourceEntityType);
		if (resourceEntity != ResourceEntity.SERVICE) {
			throw new IllegalArgumentException(
					"Unsupported input ResourceEntity " + resourceEntityType);
		}

		ResourceEntity responseEntity = ResourceEntity
				.fromValue(resourceEntityResponseType);
		switch (responseEntity) {
		case SERVICE:
			 return metricsServiceOperationByIpDAO.findMetricServiceAdminNames(resourceEntityName);
		case OPERATION:
			return metricsServiceOperationByIpDAO.findMetricOperationNames(resourceEntityName);
		case CONSUMER: {
//			 return metricsDAO.findMetricConsumerNames(resourceEntityName);
		}
		default:
			throw new IllegalArgumentException(
					"Unsupported output ResourceEntity "
							+ resourceEntityResponseType);
		}

	}

	private Map<String, Map<String, Object>> transformAggregatedErrorValues(
			List<Map<String, Object>> rows) {
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
		for (Map<String, Object> row : rows) {
			long errorId = (Long) row.get("errorId");
			result.put(String.valueOf(errorId), row);
		}
		return result;
	}

	private void trimResultList(List<?> list, int maxRows) {
		if (maxRows > 0 && list.size() > maxRows)
			list.subList(0, maxRows);
	}

}