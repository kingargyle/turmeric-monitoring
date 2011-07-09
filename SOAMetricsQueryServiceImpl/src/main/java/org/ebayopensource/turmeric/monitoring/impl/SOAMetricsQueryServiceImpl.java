/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorMessage;
import org.ebayopensource.turmeric.errorlibrary.turmericmonitoring.ErrorConstants;
import org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider;
import org.ebayopensource.turmeric.monitoring.provider.config.SOAMetricsQueryServiceProviderFactory;
import org.ebayopensource.turmeric.monitoring.provider.model.ExtendedErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorInfos;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.GetCustomReportDataRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetCustomReportDataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetDetailDataRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetDetailDataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetErrorGraphRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetErrorGraphResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetErrorMetricsDataRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetErrorMetricsDataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetErrorMetricsMetadataRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetErrorMetricsMetadataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricSummaryDataRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricSummaryDataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricValueRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricValueResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsMetadataRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsMetadataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetPolicyMetricDataRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetPolicyMetricDataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetPolicyMetricDetailDataRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetPolicyMetricDetailDataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetStandardReportRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetStandardReportResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetVersionRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetVersionResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGraphData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGroupData;
import org.ebayopensource.turmeric.monitoring.v1.services.PolicyMetricData;
import org.ebayopensource.turmeric.monitoring.v1.services.PolicyMetricGraphData;
import org.ebayopensource.turmeric.runtime.common.exceptions.ErrorDataFactory;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceRuntimeException;
import org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService;

/**
 * The Class SOAMetricsQueryServiceImpl.
 */
public class SOAMetricsQueryServiceImpl implements SOAMetricsQueryService {

    private static volatile SOAMetricsQueryServiceProvider s_provider;
    private static List<CommonErrorData> s_errorData = null;
    private static final String s_providerPropFilePath = "META-INF/soa/services/config/SOAMetricsQueryService/service_provider.properties";
    private static final String s_providerPropKey = "preferred-provider";

    private static void initialize() {
        String preferredProvider = null;
        if (s_errorData != null) {
            throw new ServiceRuntimeException(s_errorData);
        }
        try {
            if (s_provider == null) {
                synchronized (SOAMetricsQueryServiceImpl.class) {
                    if (s_provider == null) {
                        preferredProvider = getPreferredProvider();
                        if (preferredProvider == null) {
                            s_provider = SOAMetricsQueryServiceProviderFactory.create();
                        }
                        else {
                            s_provider = SOAMetricsQueryServiceProviderFactory.create(preferredProvider);
                        }
                    }
                }
            }
        }
        catch (ServiceException se) {
            ErrorMessage errMsg = se.getErrorMessage();
            s_errorData = errMsg.getError();
            throw new ServiceRuntimeException(s_errorData);
        }
    }

    private static String getPreferredProvider() {
        ClassLoader classLoader = SOAMetricsQueryServiceImpl.class.getClassLoader();
        InputStream inStream = classLoader.getResourceAsStream(s_providerPropFilePath);
        String provider = null;
        if (inStream != null) {
            Properties properties = new Properties();
            try {
                properties.load(inStream);
                provider = (String) properties.get(s_providerPropKey);
            }
            catch (IOException e) {
                // ignore
            }
            finally {
                try {
                    inStream.close();
                }
                catch (IOException e) {
                    // ignore
                }
            }
        }
        return provider;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getCustomReportData(org.ebayopensource
     * .turmeric.monitoring.v1.services.GetCustomReportDataRequest)
     */
    @Override
    public GetCustomReportDataResponse getCustomReportData(GetCustomReportDataRequest getCustomReportDataRequest) {

        GetCustomReportDataResponse response = null;
        try {
            initialize();
            response = new GetCustomReportDataResponse();
            List<MetricData> result = s_provider.getCustomReportData(getCustomReportDataRequest.getReportCriteria(),
                            getCustomReportDataRequest.getMetricCriteria());
            response.getReturnData().addAll(result);
        }
        catch (Exception e) {
            response.setErrorMessage(new ErrorMessage());
            response.getErrorMessage()
                            .getError()
                            .add(ErrorDataFactory.createErrorData(
                                            ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR,
                                            ErrorConstants.ERRORDOMAIN));
        }
        return response;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getDetailData(org.ebayopensource.
     * turmeric.monitoring.v1.services.GetDetailDataRequest)
     */
    @Override
    public GetDetailDataResponse getDetailData(GetDetailDataRequest getDetailDataRequest) {
        GetDetailDataResponse response = null;
        try {
            initialize();
            response = new GetDetailDataResponse();
            List<MetricData> result = s_provider.getDetailData(getDetailDataRequest.getDc(),
                            getDetailDataRequest.getMetricCriteria(), getDetailDataRequest.getMetricResourceCriteria());
            response.getReturnData().addAll(result);
        }
        catch (Exception e) {
            response.setErrorMessage(new ErrorMessage());
            response.getErrorMessage()
                            .getError()
                            .add(ErrorDataFactory.createErrorData(
                                            ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR,
                                            ErrorConstants.ERRORDOMAIN));
        }
        return response;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getErrorGraph(org.ebayopensource.
     * turmeric.monitoring.v1.services.GetErrorGraphRequest)
     */
    @Override
    public GetErrorGraphResponse getErrorGraph(GetErrorGraphRequest getErrorGraphRequest) {
        GetErrorGraphResponse response = null;
        initialize();
        try {
            response = new GetErrorGraphResponse();
            List<MetricGraphData> result = s_provider.getErrorGraph(getErrorGraphRequest.getServiceName(),
                            getErrorGraphRequest.getOperationName(), getErrorGraphRequest.getConsumerName(),
                            getErrorGraphRequest.getErrorId(), getErrorGraphRequest.getErrorCategory(),
                            getErrorGraphRequest.getErrorSeverity(), getErrorGraphRequest.getMetricCriteria());
            response.getReturnData().addAll(result);
        }
        catch (Exception e) {
            response.setErrorMessage(new ErrorMessage());
            response.getErrorMessage()
                            .getError()
                            .add(ErrorDataFactory.createErrorData(
                                            ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR,
                                            ErrorConstants.ERRORDOMAIN));
        }
        return response;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getErrorMetricsData(org.ebayopensource
     * .turmeric.monitoring.v1.services.GetErrorMetricsDataRequest)
     */
    @Override
    public GetErrorMetricsDataResponse getErrorMetricsData(GetErrorMetricsDataRequest getErrorMetricsDataRequest) {
        GetErrorMetricsDataResponse response = null;
        initialize();
        try {
            response = new GetErrorMetricsDataResponse();
            List<ExtendedErrorViewData> result = s_provider.getExtendedErrorMetricsData(
                            getErrorMetricsDataRequest.getErrorType(), getErrorMetricsDataRequest.getServiceName(),
                            getErrorMetricsDataRequest.getOperationName(),
                            getErrorMetricsDataRequest.getConsumerName(), getErrorMetricsDataRequest.getErrorId(),
                            getErrorMetricsDataRequest.getErrorCategory(),
                            getErrorMetricsDataRequest.getErrorSeverity(), getErrorMetricsDataRequest.getErrorName(),
                            getErrorMetricsDataRequest.getMetricCriteria());
            updateErrorDiffValues(result);
            response.getReturnData().addAll(result);
        }
        catch (Exception e) {
            response.setErrorMessage(new ErrorMessage());
            response.getErrorMessage()
                            .getError()
                            .add(ErrorDataFactory.createErrorData(
                                            ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR,
                                            ErrorConstants.ERRORDOMAIN));
        }
        return response;
    }

    public void updateErrorDiffValues(List<ExtendedErrorViewData> result) {
        for (ExtendedErrorViewData errorViewData : result) {
            calcErrorDiffValues(errorViewData);
        }
    }

    public void calcErrorDiffValues(ExtendedErrorViewData errorViewData) {
        double errorCount1 = errorViewData.getErrorCount1();
        double errorCount2 = errorViewData.getErrorCount2();
        if (errorCount2 != 0d) {
            errorViewData.setErrorDiff(calcDiff(errorCount1, errorCount2));
        }
        else {
            errorViewData.setErrorDiff(errorViewData.getErrorCount1() == 0 ? 0 : -100);
        }
        double errorCallRatio1 = errorViewData.getErrorCallRatio1();
        double errorCallRatio2 = errorViewData.getErrorCallRatio2();
        if (errorCallRatio2 != 0d) {
            errorViewData.setRatioDiff(calcDiff(errorCallRatio1, errorCallRatio2));
        }
        else {
            errorViewData.setRatioDiff(errorViewData.getErrorCallRatio1() == 0 ? 0 : -100);
        }
        double errorCall1 = errorViewData.getErrorCall1();
        double errorCall2 = errorViewData.getErrorCall2();
        errorViewData.setErrorCallRatio1(errorCall1 == 0 ? 0 : errorViewData.getErrorCount1() / errorCall1);
        errorViewData.setErrorCallRatio2(errorCall2 == 0 ? 0 : errorViewData.getErrorCount2() / errorCall2);
    }

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getErrorMetricsMetadata(org.
     * ebayopensource.turmeric.monitoring.v1.services.GetErrorMetricsMetadataRequest)
     */
    @Override
    public GetErrorMetricsMetadataResponse getErrorMetricsMetadata(
                    GetErrorMetricsMetadataRequest getErrorMetricsMetadataRequest) {
        GetErrorMetricsMetadataResponse response = null;
        try {
            initialize();
            response = new GetErrorMetricsMetadataResponse();
            ErrorInfos result = s_provider.getErrorMetricsMetadata(getErrorMetricsMetadataRequest.getErrorId(),
                            getErrorMetricsMetadataRequest.getErrorName(),
                            getErrorMetricsMetadataRequest.getServiceName());
            response.setReturnData(result);
        }
        catch (Exception e) {
            response.setErrorMessage(new ErrorMessage());
            response.getErrorMessage()
                            .getError()
                            .add(ErrorDataFactory.createErrorData(
                                            ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR,
                                            ErrorConstants.ERRORDOMAIN));
        }
        return response;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getMetricsData(org.ebayopensource
     * .turmeric.monitoring.v1.services.GetMetricsRequest)
     */
    @Override
    public GetMetricsResponse getMetricsData(GetMetricsRequest getMetricsDataRequest) {
        GetMetricsResponse response = null;
        try {
            initialize();
            response = new GetMetricsResponse();
            List<MetricGroupData> result = s_provider.getMetricsData(getMetricsDataRequest.getMetricCriteria(),
                            getMetricsDataRequest.getMetricResourceCriteria());
            updateDiffValues(result);
            response.getReturnData().addAll(result);
        }
        catch (Exception e) {
            response.setErrorMessage(new ErrorMessage());
            response.getErrorMessage()
                            .getError()
                            .add(ErrorDataFactory.createErrorData(
                                            ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR,
                                            ErrorConstants.ERRORDOMAIN));
        }
        return response;
    }

    public void updateDiffValues(List<MetricGroupData> result) {
        for (MetricGroupData metricGroupData : result) {
            calcDiffValue(metricGroupData);
        }
    }

    public void calcDiffValue(MetricGroupData metricGroupData) {
        double count1 = metricGroupData.getCount1();
        double count2 = metricGroupData.getCount2();
        Double diff = calcDiff(count1, count2);
        metricGroupData.setDiff(diff);
    }

    public Double calcDiff(double from, double to) {
        Double diff = Double.valueOf(0);
        if (from == 0d) {
            if (to != 0d) {
                diff = 100d;
            }
        }
        else {
            if (to != 0d) {
                diff = Double.valueOf(((to - from) * 100) / from);
            }
            else {
                diff = -100d;
            }
        }
        diff = (Math.round(diff * 100)) / 100.0;

        return diff;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getMetricsMetadata(org.ebayopensource
     * .turmeric.monitoring.v1.services.GetMetricsMetadataRequest)
     */
    @Override
    public GetMetricsMetadataResponse getMetricsMetadata(GetMetricsMetadataRequest getMetricsMetadataRequest) {
        GetMetricsMetadataResponse response = null;
        try {
            initialize();
            response = new GetMetricsMetadataResponse();
            List<String> result = s_provider.getMetricsMetadata(getMetricsMetadataRequest.getResourceEntityType()
                            .value(), getMetricsMetadataRequest.getResourceEntityName(), (getMetricsMetadataRequest
                            .getResourceEntityResponseType() == null) ? null : getMetricsMetadataRequest
                            .getResourceEntityResponseType().value());
            response.getResourceEntityResponseNames().addAll(result);
        }
        catch (Exception e) {
            response.setErrorMessage(new ErrorMessage());
            response.getErrorMessage()
                            .getError()
                            .add(ErrorDataFactory.createErrorData(
                                            ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR,
                                            ErrorConstants.ERRORDOMAIN));
        }
        return response;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getMetricSummaryData(org.ebayopensource
     * .turmeric.monitoring.v1.services.GetMetricSummaryDataRequest)
     */
    @Override
    public GetMetricSummaryDataResponse getMetricSummaryData(GetMetricSummaryDataRequest getMetricSummaryDataRequest) {
        GetMetricSummaryDataResponse response = null;
        try {
            initialize();
            response = new GetMetricSummaryDataResponse();
            MetricData result = s_provider.getMetricSummaryData(getMetricSummaryDataRequest.getDc(),
                            getMetricSummaryDataRequest.getMetricCriteria(),
                            getMetricSummaryDataRequest.getMetricResourceCriteria());
            response.setReturnData(result);
        }
        catch (Exception e) {
            response.setErrorMessage(new ErrorMessage());
            response.getErrorMessage()
                            .getError()
                            .add(ErrorDataFactory.createErrorData(
                                            ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR,
                                            ErrorConstants.ERRORDOMAIN));
        }
        return response;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getMetricValue(org.ebayopensource
     * .turmeric.monitoring.v1.services.GetMetricValueRequest)
     */
    @Override
    public GetMetricValueResponse getMetricValue(GetMetricValueRequest getMetricValueRequest) {
        GetMetricValueResponse response = null;
        try {
            initialize();
            response = new GetMetricValueResponse();
            List<MetricGraphData> result = s_provider.getMetricValue(getMetricValueRequest.getCriteriaInfo(),
                            getMetricValueRequest.getStartTime(), getMetricValueRequest.getDuration(),
                            getMetricValueRequest.getAggregationPeriod(), getMetricValueRequest.getAutoDelay());
            response.getReturnData().addAll(result);
        }
        catch (Exception e) {
            response.setErrorMessage(new ErrorMessage());
            response.getErrorMessage()
                            .getError()
                            .add(ErrorDataFactory.createErrorData(
                                            ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR,
                                            ErrorConstants.ERRORDOMAIN));
        }
        return response;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getPolicyMetricData(org.ebayopensource
     * .turmeric.monitoring.v1.services.GetPolicyMetricDataRequest)
     */
    @Override
    public GetPolicyMetricDataResponse getPolicyMetricData(GetPolicyMetricDataRequest getPolicyMetricDataRequest) {
        GetPolicyMetricDataResponse response = null;
        try {
            initialize();
            response = new GetPolicyMetricDataResponse();
            List<PolicyMetricData> result = s_provider.getPolicyMetricData(getPolicyMetricDataRequest.getStartTime(),
                            getPolicyMetricDataRequest.getEndTime(), getPolicyMetricDataRequest.getPolicyType(),
                            getPolicyMetricDataRequest.getPolicyName(), getPolicyMetricDataRequest.getServiceName(),
                            getPolicyMetricDataRequest.getOperationName(),
                            getPolicyMetricDataRequest.getSubjectTypeName(),
                            getPolicyMetricDataRequest.getSubjectValue(), getPolicyMetricDataRequest.getEffect());
            response.getReturnData().addAll(result);
        }
        catch (Exception e) {
            response.setErrorMessage(new ErrorMessage());
            response.getErrorMessage()
                            .getError()
                            .add(ErrorDataFactory.createErrorData(
                                            ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR,
                                            ErrorConstants.ERRORDOMAIN));
        }
        return response;
    }

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getPolicyMetricDetailData(org.
     * ebayopensource.turmeric.monitoring.v1.services.GetPolicyMetricDetailDataRequest)
     */
    @Override
    public GetPolicyMetricDetailDataResponse getPolicyMetricDetailData(
                    GetPolicyMetricDetailDataRequest getPolicyMetricDetailDataRequest) {
        GetPolicyMetricDetailDataResponse response = null;
        try {
            initialize();
            response = new GetPolicyMetricDetailDataResponse();
            List<PolicyMetricGraphData> result = s_provider.getPolicyMetricDetailData(
                            getPolicyMetricDetailDataRequest.getPolicyName(),
                            getPolicyMetricDetailDataRequest.getServiceName(),
                            getPolicyMetricDetailDataRequest.getOperationName(),
                            getPolicyMetricDetailDataRequest.getSubjectTypeName(),
                            getPolicyMetricDetailDataRequest.getSubjectValue(),
                            getPolicyMetricDetailDataRequest.getListType(),
                            getPolicyMetricDetailDataRequest.getStartTime(),
                            getPolicyMetricDetailDataRequest.getEndTime());
            response.getReturnData().addAll(result);
        }
        catch (Exception e) {
            response.setErrorMessage(new ErrorMessage());
            response.getErrorMessage()
                            .getError()
                            .add(ErrorDataFactory.createErrorData(
                                            ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR,
                                            ErrorConstants.ERRORDOMAIN));
        }
        return response;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getStandardReportData(org.ebayopensource
     * .turmeric.monitoring.v1.services.GetStandardReportRequest)
     */
    @Override
    public GetStandardReportResponse getStandardReportData(GetStandardReportRequest getStandardReportDataRequest) {
        GetStandardReportResponse response = null;
        try {
            initialize();
            response = new GetStandardReportResponse();
            List<MetricData> result = s_provider.getStandardReportData(getStandardReportDataRequest.getReportType(),
                            getStandardReportDataRequest.getMetricCriteria());
            response.getReturnData().addAll(result);
        }
        catch (Exception e) {
            response.setErrorMessage(new ErrorMessage());
            response.getErrorMessage()
                            .getError()
                            .add(ErrorDataFactory.createErrorData(
                                            ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR,
                                            ErrorConstants.ERRORDOMAIN));
        }
        return response;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService#getVersion(org.ebayopensource.turmeric
     * .monitoring.v1.services.GetVersionRequest)
     */
    @Override
    public GetVersionResponse getVersion(GetVersionRequest param0) {
        initialize();
        GetVersionResponse response = new GetVersionResponse();
        response.setVersion("1.0.0");

        return response;
    }

}
