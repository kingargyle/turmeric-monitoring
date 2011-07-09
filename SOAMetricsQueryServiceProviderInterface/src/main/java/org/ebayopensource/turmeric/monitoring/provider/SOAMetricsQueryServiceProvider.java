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

/**
 * The Interface SOAMetricsQueryServiceProvider.
 */
public interface SOAMetricsQueryServiceProvider {

    /**
     * Gets the custom report data.
     * 
     * @param reportCriteria
     *            the report criteria
     * @param metricCriteria
     *            the metric criteria
     * @return the custom report data
     */
    public List<MetricData> getCustomReportData(ReportCriteria reportCriteria, MetricCriteria metricCriteria);

    /**
     * Gets the detail data.
     * 
     * @param dc
     *            the dc
     * @param metricCriteria
     *            the metric criteria
     * @param metricResourceCriteria
     *            the metric resource criteria
     * @return the detail data
     */
    public List<MetricData> getDetailData(String dc, MetricCriteria metricCriteria,
                    MetricResourceCriteria metricResourceCriteria);

    /**
     * Gets the error graph.
     * 
     * @param serviceName
     *            the service name
     * @param operationName
     *            the operation name
     * @param consumerName
     *            the consumer name
     * @param errorId
     *            the error id
     * @param errorCategory
     *            the error category
     * @param errorSeverity
     *            the error severity
     * @param metricCriteria
     *            the metric criteria
     * @return the error graph
     */
    public List<MetricGraphData> getErrorGraph(String serviceName, String operationName, String consumerName,
                    String errorId, String errorCategory, String errorSeverity, MetricCriteria metricCriteria);

    /**
     * Gets the error metrics data.
     * 
     * @param errorType
     *            the error type
     * @param serviceName
     *            the service name
     * @param operationName
     *            the operation name
     * @param consumerName
     *            the consumer name
     * @param errorId
     *            the error id
     * @param errorCategory
     *            the error category
     * @param errorSeverity
     *            the error severity
     * @param errorName
     *            the error name
     * @param metricCriteria
     *            the metric criteria
     * @return the error metrics data
     * @deprecated Instead use
     *             {@link #getExtendedErrorMetricsData(String, List, List, List, String, String, String, String, MetricCriteria)}
     */
    public List<ErrorViewData> getErrorMetricsData(String errorType, List<String> serviceName,
                    List<String> operationName, List<String> consumerName, String errorId, String errorCategory,
                    String errorSeverity, String errorName, MetricCriteria metricCriteria);

    /**
     * Gets the error metrics data.
     * 
     * @param errorType
     *            the error type
     * @param serviceName
     *            the service name
     * @param operationName
     *            the operation name
     * @param consumerName
     *            the consumer name
     * @param errorId
     *            the error id
     * @param errorCategory
     *            the error category
     * @param errorSeverity
     *            the error severity
     * @param errorName
     *            the error name
     * @param metricCriteria
     *            the metric criteria
     * @return the error metrics data
     */
    public List<ExtendedErrorViewData> getExtendedErrorMetricsData(String errorType, List<String> serviceName,
                    List<String> operationName, List<String> consumerName, String errorId, String errorCategory,
                    String errorSeverity, String errorName, MetricCriteria metricCriteria);

    /**
     * Gets the error metrics metadata.
     * 
     * @param errorId
     *            the error id
     * @param errorName
     *            the error name
     * @param serviceName
     *            the service name
     * @return the error metrics metadata
     */
    public ErrorInfos getErrorMetricsMetadata(String errorId, String errorName, String serviceName);

    /**
     * Gets the metrics data.
     * 
     * @param metricCriteria
     *            the metric criteria
     * @param metricResourceCriteria
     *            the metric resource criteria
     * @return the metrics data
     */
    public List<MetricGroupData> getMetricsData(MetricCriteria metricCriteria,
                    MetricResourceCriteria metricResourceCriteria);

    /**
     * Gets the metric summary data.
     * 
     * @param dc
     *            the dc
     * @param metricCriteria
     *            the metric criteria
     * @param metricResourceCriteria
     *            the metric resource criteria
     * @return the metric summary data
     */
    public MetricData getMetricSummaryData(String dc, MetricCriteria metricCriteria,
                    MetricResourceCriteria metricResourceCriteria);

    /**
     * Gets the metric value.
     * 
     * @param criteriaInfo
     *            the criteria info
     * @param startTime
     *            the start time
     * @param duration
     *            the duration
     * @param aggregationPeriod
     *            the aggregation period
     * @param autoDelay
     *            the auto delay
     * @return the metric value
     */
    public List<MetricGraphData> getMetricValue(CriteriaInfo criteriaInfo, long startTime, long duration,
                    int aggregationPeriod, String autoDelay);

    /**
     * Gets the policy metric data.
     * 
     * @param startTime
     *            the start time
     * @param endTime
     *            the end time
     * @param policyType
     *            the policy type
     * @param policyName
     *            the policy name
     * @param serviceName
     *            the service name
     * @param operationName
     *            the operation name
     * @param subjectTypeName
     *            the subject type name
     * @param subjectValue
     *            the subject value
     * @param effect
     *            the effect
     * @return the policy metric data
     */
    public List<PolicyMetricData> getPolicyMetricData(long startTime, long endTime, String policyType,
                    String policyName, String serviceName, String operationName, String subjectTypeName,
                    String subjectValue, String effect);

    /**
     * Gets the policy metric detail data.
     * 
     * @param policyName
     *            the policy name
     * @param serviceName
     *            the service name
     * @param operationName
     *            the operation name
     * @param subjectTypeName
     *            the subject type name
     * @param subjectValue
     *            the subject value
     * @param listType
     *            the list type
     * @param startTime
     *            the start time
     * @param endTime
     *            the end time
     * @return the policy metric detail data
     */
    public List<PolicyMetricGraphData> getPolicyMetricDetailData(String policyName, String serviceName,
                    String operationName, String subjectTypeName, String subjectValue, String listType, long startTime,
                    long endTime);

    /**
     * Gets the standard report data.
     * 
     * @param reportType
     *            the report type
     * @param metricCriteria
     *            the metric criteria
     * @return the standard report data
     */
    public List<MetricData> getStandardReportData(String reportType, MetricCriteria metricCriteria);

    /**
     * Gets the metrics metadata.
     * 
     * @param resourceEntityType
     *            the resource entity type
     * @param resourceEntityName
     *            the resource entity name
     * @param resourceEntityResponseType
     *            the resource entity response type
     * @return the metrics metadata
     */
    public List<String> getMetricsMetadata(String resourceEntityType, List<String> resourceEntityName,
                    String resourceEntityResponseType);

}
