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
import java.util.List;
import java.util.Properties;

import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorValuesDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorsByCategoryDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorsBySeverityDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorValuesDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorsByCategoryDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorsBySeverityDAOImpl;
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
import org.ebayopensource.turmeric.utils.ContextUtils;
import org.ebayopensource.turmeric.utils.cassandra.service.CassandraManager;

/**
 * The Class SOAMetricsQueryServiceCassandraProviderImpl.
 * 
 * @author jamuguerza
 */
public class SOAMetricsQueryServiceCassandraProviderImpl implements
		SOAMetricsQueryServiceProvider {

	private final MetricsErrorDAO metricsErrorDAO;

	private final MetricsErrorValuesDAO metricsErrorValuesDAO;

	private final MetricsErrorsByCategoryDAO metricsErrorsByCategoryDAO;

	private final MetricsErrorsBySeverityDAO metricsErrorsBySeverityDAO;

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

	/** The Constant c_error_cf. */
	private static final String c_error_cf = "cassandra-error-cf";

	/** The Constant c_error_values_cf. */
	private static final String c_error_values_cf = "cassandra-error-values-cf";

	/** The Constant c_errors_by_category_cf. */
	private static final String c_errors_by_category_cf = "cassandra-errors-by-category-cf";

	/** The Constant c_errors_by_severity_cf. */
	private static final String c_errors_by_severity_cf = "cassandra-errors-by-severity-cf";

	/** The host. */
	private static String host;

	/** The keyspace. */
	private static String keyspace;

	private static String clusterName;

	private static String errorCF;

	private static String errorValuesCF;

	private static String errorsByCategoryCF;

	private static String errorsBySeverityCF;

	{
		CassandraManager.initialize();
		getCassandraConfig();
	}

	/**
	 * Instantiates a new Metrics Query Service cassandra provider impl.
	 */
	public SOAMetricsQueryServiceCassandraProviderImpl() {
		metricsErrorDAO = new MetricsErrorDAOImpl(clusterName, host, keyspace, errorCF);
		metricsErrorValuesDAO = new MetricsErrorValuesDAOImpl(clusterName, host, keyspace, errorValuesCF);
		metricsErrorsByCategoryDAO = new MetricsErrorsByCategoryDAOImpl(clusterName, host, keyspace, errorsByCategoryCF);
		metricsErrorsBySeverityDAO = new MetricsErrorsBySeverityDAOImpl(clusterName, host, keyspace, errorsBySeverityCF);
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

				errorCF = (String) properties.get(c_error_cf);
				errorValuesCF = (String) properties.get(c_error_values_cf);
				errorsByCategoryCF = (String) properties
						.get(c_errors_by_category_cf);
				errorsBySeverityCF = (String) properties
						.get(c_errors_by_severity_cf);

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetricData> getDetailData(String dc,
			MetricCriteria metricCriteria,
			MetricResourceCriteria metricResourceCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetricGraphData> getErrorGraph(String serviceName,
			String operationName, String consumerName, String errorId,
			String errorCategory, String errorSeverity,
			MetricCriteria metricCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ErrorViewData> getErrorMetricsData(String errorType,
			List<String> serviceName, List<String> operationName,
			List<String> consumerName, String errorId, String errorCategory,
			String errorSeverity, String errorName,
			MetricCriteria metricCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExtendedErrorViewData> getExtendedErrorMetricsData(
			String errorType, List<String> serviceName,
			List<String> operationName, List<String> consumerName,
			String errorId, String errorCategory, String errorSeverity,
			String errorName, MetricCriteria metricCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErrorInfos getErrorMetricsMetadata(String errorId, String errorName,
			String serviceName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetricGroupData> getMetricsData(MetricCriteria metricCriteria,
			MetricResourceCriteria metricResourceCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetricData getMetricSummaryData(String dc,
			MetricCriteria metricCriteria,
			MetricResourceCriteria metricResourceCriteria) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PolicyMetricGraphData> getPolicyMetricDetailData(
			String policyName, String serviceName, String operationName,
			String subjectTypeName, String subjectValue, String listType,
			long startTime, long endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetricData> getStandardReportData(String reportType,
			MetricCriteria metricCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMetricsMetadata(String resourceEntityType,
			List<String> resourceEntityName, String resourceEntityResponseType) {
		// TODO Auto-generated method stub
		return null;
	}

}