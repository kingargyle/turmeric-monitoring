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

import org.ebayopensource.turmeric.common.v1.types.AckValue;
import org.ebayopensource.turmeric.common.v1.types.BaseResponse;
import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorMessage;
import org.ebayopensource.turmeric.errorlibrary.turmericmonitoring.ErrorConstants;
import org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService;
import org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider;
import org.ebayopensource.turmeric.monitoring.provider.config.SOAMetricsQueryServiceProviderFactory;
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

public class SOAMetricsQueryServiceImpl
    implements SOAMetricsQueryService
{

	private static volatile SOAMetricsQueryServiceProvider s_provider;
	private static List<CommonErrorData> s_errorData = null;
	private static final String s_providerPropFilePath =
		"META-INF/soa/services/config/SOAMetricsQueryService/service_provider.properties";
	private static final String s_providerPropKey = "preferred-provider";


	private static void initialize(){
		String preferredProvider = null;
		if (s_errorData != null){
			throw new ServiceRuntimeException(s_errorData);
		}
		try{
			if (s_provider == null){
				synchronized (SOAMetricsQueryServiceImpl.class) {
					if (s_provider == null){
						preferredProvider = getPreferredProvider();
						if (preferredProvider == null){
							s_provider = SOAMetricsQueryServiceProviderFactory.create();
						}else{
							s_provider = SOAMetricsQueryServiceProviderFactory.create(preferredProvider);
						}
					}
				}
			}
		} catch(ServiceException se){
			ErrorMessage errMsg = se.getErrorMessage();
			s_errorData = errMsg.getError();
			throw new ServiceRuntimeException(s_errorData);
		}
	}

	private static String getPreferredProvider() {
		ClassLoader classLoader = SOAMetricsQueryServiceImpl.class.getClassLoader();
		InputStream	inStream = classLoader.getResourceAsStream(s_providerPropFilePath);
		String provider = null;
		if (inStream != null) {
			Properties properties = new Properties();
			try {
				properties.load(inStream);
				provider = (String)properties.get(s_providerPropKey);
			} catch (IOException e) {
				// ignore
			}
			finally {
				try {
					inStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return provider;
	}




    @Override
	public GetCustomReportDataResponse getCustomReportData(
			GetCustomReportDataRequest getCustomReportDataRequest) {

    	GetCustomReportDataResponse response = null;
    	try {
			initialize();
			response = new GetCustomReportDataResponse();
			List<MetricData> result = s_provider.getCustomReportData(
					getCustomReportDataRequest.getReportCriteria(),
					getCustomReportDataRequest.getMetricCriteria());
			response.getReturnData().addAll(result);
			response.setAck(AckValue.SUCCESS);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			response.setErrorMessage(new ErrorMessage());
			response.getErrorMessage().getError().add(ErrorDataFactory.createErrorData(ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR, ErrorConstants.ERRORDOMAIN));
		}
		return response;
	}

	@Override
	public GetDetailDataResponse getDetailData(
			GetDetailDataRequest getDetailDataRequest) {
		GetDetailDataResponse response = null;
		try {
			initialize();
			response = new GetDetailDataResponse();
			List<MetricData> result = s_provider.getDetailData(
					getDetailDataRequest.getDc(), getDetailDataRequest
							.getMetricCriteria(), getDetailDataRequest
							.getMetricResourceCriteria());
			response.getReturnData().addAll(result);
			response.setAck(AckValue.SUCCESS);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			response.setErrorMessage(new ErrorMessage());
			response.getErrorMessage().getError().add(ErrorDataFactory.createErrorData(ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR, ErrorConstants.ERRORDOMAIN));
		}
		return response;
	}

	@Override
	public GetErrorGraphResponse getErrorGraph(
			GetErrorGraphRequest getErrorGraphRequest) {
		GetErrorGraphResponse response = null;
		initialize();
		try {
			response = new GetErrorGraphResponse();
			List<MetricGraphData> result = s_provider.getErrorGraph(
					getErrorGraphRequest.getServiceName(), getErrorGraphRequest
							.getOperationName(), getErrorGraphRequest
							.getConsumerName(), getErrorGraphRequest
							.getErrorId(), getErrorGraphRequest
							.getErrorCategory(), getErrorGraphRequest
							.getErrorSeverity(), getErrorGraphRequest
							.getMetricCriteria());
			response.getReturnData().addAll(result);
			response.setAck(AckValue.SUCCESS);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			response.setErrorMessage(new ErrorMessage());
			response.getErrorMessage().getError().add(ErrorDataFactory.createErrorData(ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR, ErrorConstants.ERRORDOMAIN));
		}
		return response;
	}

	@Override
	public GetErrorMetricsDataResponse getErrorMetricsData(
			GetErrorMetricsDataRequest getErrorMetricsDataRequest) {
		GetErrorMetricsDataResponse response = null;
		initialize();
		try {
			response = new GetErrorMetricsDataResponse();
			List<ErrorViewData> result = s_provider.getErrorMetricsData(
					getErrorMetricsDataRequest.getErrorType(),
					getErrorMetricsDataRequest.getServiceName(),
					getErrorMetricsDataRequest.getOperationName(),
					getErrorMetricsDataRequest.getConsumerName(),
					getErrorMetricsDataRequest.getErrorId(),
					getErrorMetricsDataRequest.getErrorCategory(),
					getErrorMetricsDataRequest.getErrorSeverity(),
					getErrorMetricsDataRequest.getErrorName(),
					getErrorMetricsDataRequest.getMetricCriteria());
			response.getReturnData().addAll(result);
			response.setAck(AckValue.SUCCESS);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			response.setErrorMessage(new ErrorMessage());
			response.getErrorMessage().getError().add(ErrorDataFactory.createErrorData(ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR, ErrorConstants.ERRORDOMAIN));
		}
		return response;
	}

	@Override
	public GetErrorMetricsMetadataResponse getErrorMetricsMetadata(
			GetErrorMetricsMetadataRequest getErrorMetricsMetadataRequest) {
		GetErrorMetricsMetadataResponse response = null;
		try {
			initialize();
			response = new GetErrorMetricsMetadataResponse();
			ErrorInfos result = s_provider.getErrorMetricsMetadata(
					getErrorMetricsMetadataRequest.getErrorId(),
					getErrorMetricsMetadataRequest.getErrorName(),
					getErrorMetricsMetadataRequest.getServiceName());
			response.setReturnData(result);
			response.setAck(AckValue.SUCCESS);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			response.setErrorMessage(new ErrorMessage());
			response.getErrorMessage().getError().add(ErrorDataFactory.createErrorData(ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR, ErrorConstants.ERRORDOMAIN));
		}
		return response;
	}

	@Override
	public GetMetricsResponse getMetricsData(
			GetMetricsRequest getMetricsDataRequest) {
		GetMetricsResponse response = null;
		try {
			initialize();
			response = new GetMetricsResponse();
			List<MetricGroupData> result = s_provider.getMetricsData(
					getMetricsDataRequest.getMetricCriteria(),
					getMetricsDataRequest.getMetricResourceCriteria());
			response.getReturnData().addAll(result);
			response.setAck(AckValue.SUCCESS);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			response.setErrorMessage(new ErrorMessage());
			response.getErrorMessage().getError().add(ErrorDataFactory.createErrorData(ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR, ErrorConstants.ERRORDOMAIN));
		}
		return response;
	}

	@Override
	public GetMetricsMetadataResponse getMetricsMetadata(
			GetMetricsMetadataRequest getMetricsMetadataRequest) {
		GetMetricsMetadataResponse response = null;
		try {
			initialize();
			response = new GetMetricsMetadataResponse();
			List<String> result = s_provider
					.getMetricsMetadata(
							getMetricsMetadataRequest.getResourceEntityType()
									.value(),
							getMetricsMetadataRequest.getResourceEntityName(),
							(getMetricsMetadataRequest
									.getResourceEntityResponseType() == null) ? null
									: getMetricsMetadataRequest
											.getResourceEntityResponseType()
											.value());
			response.getResourceEntityResponseNames().addAll(result);
			response.setAck(AckValue.SUCCESS);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			response.setErrorMessage(new ErrorMessage());
			response.getErrorMessage().getError().add(ErrorDataFactory.createErrorData(ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR, ErrorConstants.ERRORDOMAIN));
		}
		return response;
	}

	@Override
	public GetMetricSummaryDataResponse getMetricSummaryData(
			GetMetricSummaryDataRequest getMetricSummaryDataRequest) {
		GetMetricSummaryDataResponse response = null;
		try {
			initialize();
			response = new GetMetricSummaryDataResponse();
			MetricData result = s_provider.getMetricSummaryData(
					getMetricSummaryDataRequest.getDc(),
					getMetricSummaryDataRequest.getMetricCriteria(),
					getMetricSummaryDataRequest.getMetricResourceCriteria());
			response.setReturnData(result);
			response.setAck(AckValue.SUCCESS);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			response.setErrorMessage(new ErrorMessage());
			response.getErrorMessage().getError().add(ErrorDataFactory.createErrorData(ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR, ErrorConstants.ERRORDOMAIN));
		}
		return response;
	}

	@Override
	public GetMetricValueResponse getMetricValue(
			GetMetricValueRequest getMetricValueRequest) {
		GetMetricValueResponse response = null;
		try {
			initialize();
			response = new GetMetricValueResponse();
			List<MetricGraphData> result = s_provider.getMetricValue(
					getMetricValueRequest.getCriteriaInfo(),
					getMetricValueRequest.getStartTime(), getMetricValueRequest
							.getDuration(), getMetricValueRequest
							.getAggregationPeriod(), getMetricValueRequest
							.getAutoDelay());
			response.getReturnData().addAll(result);
			response.setAck(AckValue.SUCCESS);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			response.setErrorMessage(new ErrorMessage());
			response.getErrorMessage().getError().add(ErrorDataFactory.createErrorData(ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR, ErrorConstants.ERRORDOMAIN));
		}
		return response;
	}

	@Override
	public GetPolicyMetricDataResponse getPolicyMetricData(
			GetPolicyMetricDataRequest getPolicyMetricDataRequest) {
		GetPolicyMetricDataResponse response = null;
		try {
			initialize();
			response = new GetPolicyMetricDataResponse();
			List<PolicyMetricData> result = s_provider.getPolicyMetricData(
					getPolicyMetricDataRequest.getStartTime(),
					getPolicyMetricDataRequest.getEndTime(),
					getPolicyMetricDataRequest.getPolicyType(),
					getPolicyMetricDataRequest.getPolicyName(),
					getPolicyMetricDataRequest.getServiceName(),
					getPolicyMetricDataRequest.getOperationName(),
					getPolicyMetricDataRequest.getSubjectTypeName(),
					getPolicyMetricDataRequest.getSubjectValue(),
					getPolicyMetricDataRequest.getEffect());
			response.getReturnData().addAll(result);
			response.setAck(AckValue.SUCCESS);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			response.setErrorMessage(new ErrorMessage());
			response.getErrorMessage().getError().add(ErrorDataFactory.createErrorData(ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR, ErrorConstants.ERRORDOMAIN));
		}
		return response;
	}

	@Override
	public GetPolicyMetricDetailDataResponse getPolicyMetricDetailData(
			GetPolicyMetricDetailDataRequest getPolicyMetricDetailDataRequest) {
		GetPolicyMetricDetailDataResponse response = null;
		try {
			initialize();
			response = new GetPolicyMetricDetailDataResponse();
			List<PolicyMetricGraphData> result = s_provider
					.getPolicyMetricDetailData(getPolicyMetricDetailDataRequest
							.getPolicyName(), getPolicyMetricDetailDataRequest
							.getServiceName(), getPolicyMetricDetailDataRequest
							.getOperationName(),
							getPolicyMetricDetailDataRequest
									.getSubjectTypeName(),
							getPolicyMetricDetailDataRequest.getSubjectValue(),
							getPolicyMetricDetailDataRequest.getListType(),
							getPolicyMetricDetailDataRequest.getStartTime(),
							getPolicyMetricDetailDataRequest.getEndTime());
			response.getReturnData().addAll(result);
			response.setAck(AckValue.SUCCESS);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			response.setErrorMessage(new ErrorMessage());
			response.getErrorMessage().getError().add(ErrorDataFactory.createErrorData(ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR, ErrorConstants.ERRORDOMAIN));
		}
		return response;
	}

	@Override
	public GetStandardReportResponse getStandardReportData(
			GetStandardReportRequest getStandardReportDataRequest) {
		GetStandardReportResponse response = null;
		try {
			initialize();
			response = new GetStandardReportResponse();
			List<MetricData> result = s_provider.getStandardReportData(
					getStandardReportDataRequest.getReportType(),
					getStandardReportDataRequest.getMetricCriteria());
			response.getReturnData().addAll(result);
			response.setAck(AckValue.SUCCESS);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			response.setErrorMessage(new ErrorMessage());
			response.getErrorMessage().getError().add(ErrorDataFactory.createErrorData(ErrorConstants.SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR, ErrorConstants.ERRORDOMAIN));
		}
		return response;
	}

	public GetVersionResponse getVersion(GetVersionRequest param0) {
		initialize();
        GetVersionResponse response = new GetVersionResponse();
        response.setVersion("1.0.0");

		return response;
    }
	
	






}
