/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring.provider;

import java.util.List;

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

public interface SOAMetricsQueryServiceProvider {

	public List<MetricData> getCustomReportData(ReportCriteria reportCriteria,
			MetricCriteria metricCriteria);

	public List<MetricData> getDetailData(String dc,
					MetricCriteria metricCriteria,
					MetricResourceCriteria metricResourceCriteria);

	public List<MetricGraphData> getErrorGraph(String serviceName,
			String operationName,
			String consumerName,
			String errorId,
			String errorCategory,
			String errorSeverity,
			MetricCriteria metricCriteria);

	public List<ErrorViewData> getErrorMetricsData(String errorType,
			List<String> serviceName,
			List<String> operationName,
			List<String> consumerName,
			String errorId,
			String errorCategory,
			String errorSeverity,
			String errorName,
			MetricCriteria metricCriteria);

	public ErrorInfos getErrorMetricsMetadata(String errorId, String errorName, String serviceName);

	public List<MetricGroupData> getMetricsData(MetricCriteria metricCriteria, MetricResourceCriteria metricResourceCriteria);

	public MetricData getMetricSummaryData(String dc,
			MetricCriteria metricCriteria,
			MetricResourceCriteria metricResourceCriteria);

	public List<MetricGraphData> getMetricValue(CriteriaInfo criteriaInfo,
			long startTime,
			long duration,
			int aggregationPeriod,
			String autoDelay);

	public List<PolicyMetricData> getPolicyMetricData(long startTime,
			long endTime,
			String policyType,
			String policyName,
			String serviceName,
			String operationName,
			String subjectTypeName,
			String subjectValue,
			String effect);

	public List<PolicyMetricGraphData> getPolicyMetricDetailData(String policyName,
			String serviceName,
			String operationName,
			String subjectTypeName,
			String subjectValue,
			String listType,
			long startTime,
			long endTime);

	public List<MetricData> getStandardReportData(String reportType, MetricCriteria metricCriteria);

	public List<String> getMetricsMetadata(String resourceEntityType,
			List<String> resourceEntityName,
			String resourceEntityResponseType);


}
