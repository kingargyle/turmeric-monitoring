/*******************************************************************************
 * Copyright (c) 2006, 2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package com.ebay.marketplace.services.soametricsqueryservice.soametricsqueryservice.gen;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import com.ebay.marketplace.services.GetCustomReportDataRequest;
import com.ebay.marketplace.services.GetCustomReportDataResponse;
import com.ebay.marketplace.services.GetDetailDataRequest;
import com.ebay.marketplace.services.GetDetailDataResponse;
import com.ebay.marketplace.services.GetErrorGraphRequest;
import com.ebay.marketplace.services.GetErrorGraphResponse;
import com.ebay.marketplace.services.GetErrorMetricsDataRequest;
import com.ebay.marketplace.services.GetErrorMetricsDataResponse;
import com.ebay.marketplace.services.GetErrorMetricsMetadataRequest;
import com.ebay.marketplace.services.GetErrorMetricsMetadataResponse;
import com.ebay.marketplace.services.GetMetricSummaryDataRequest;
import com.ebay.marketplace.services.GetMetricSummaryDataResponse;
import com.ebay.marketplace.services.GetMetricValueRequest;
import com.ebay.marketplace.services.GetMetricValueResponse;
import com.ebay.marketplace.services.GetMetricsMetadataRequest;
import com.ebay.marketplace.services.GetMetricsMetadataResponse;
import com.ebay.marketplace.services.GetMetricsRequest;
import com.ebay.marketplace.services.GetMetricsResponse;
import com.ebay.marketplace.services.GetPolicyMetricDataRequest;
import com.ebay.marketplace.services.GetPolicyMetricDataResponse;
import com.ebay.marketplace.services.GetPolicyMetricDetailDataRequest;
import com.ebay.marketplace.services.GetStandardReportRequest;
import com.ebay.marketplace.services.GetStandardReportResponse;
import com.ebay.marketplace.services.GetVersionRequest;
import com.ebay.marketplace.services.GetVersionResponse;
import com.ebay.marketplace.services.soametricsqueryservice.AsyncSOAMetricsQueryService;
import com.ebay.soaframework.common.exceptions.ServiceException;
import com.ebay.soaframework.common.exceptions.ServiceRuntimeException;
import com.ebay.soaframework.common.types.Cookie;
import com.ebay.soaframework.common.types.SOAHeaders;
import com.ebay.soaframework.sif.service.Service;
import com.ebay.soaframework.sif.service.ServiceFactory;
import com.ebay.soaframework.sif.service.ServiceInvokerOptions;


/**
 * Note : Generated file, any changes will be lost upon regeneration.
 * This class is not thread safe
 * 
 */
public class SharedSOAMetricsQueryServiceConsumer {

    private URL m_serviceLocation = null;
    private final static String SVC_ADMIN_NAME = "SOAMetricsQueryService";
    private String m_clientName;
    private String m_environment = "production";
    private AsyncSOAMetricsQueryService m_proxy = null;
    private String m_authToken = null;
    private Cookie[] m_cookies;
    private Service m_service = null;

    public SharedSOAMetricsQueryServiceConsumer(String clientName)
        throws ServiceException
    {
        if (clientName == null) {
            throw new ServiceException("clientName can not be null");
        }
        m_clientName = clientName;
    }

    public SharedSOAMetricsQueryServiceConsumer(String clientName, String environment)
        throws ServiceException
    {
        if (environment == null) {
            throw new ServiceException("environment can not be null");
        }
        if (clientName == null) {
            throw new ServiceException("clientName can not be null");
        }
        m_clientName = clientName;
        m_environment = environment;
    }

    /**
     * Use this method to initialize ConsumerApp after creating a Consumer instance
     * 
     */
    public void init()
        throws ServiceException
    {
        getService();
    }

    protected void setServiceLocation(String serviceLocation)
        throws MalformedURLException
    {
        m_serviceLocation = new URL(serviceLocation);
        if (m_service!= null) {
            m_service.setServiceLocation(m_serviceLocation);
        }
    }

    private void setUserProvidedSecurityCredentials(Service service) {
        if (m_authToken!= null) {
            service.setSessionTransportHeader(SOAHeaders.AUTH_TOKEN, m_authToken);
        }
        if (m_cookies!= null) {
            for (int i = 0; (i<m_cookies.length); i ++) {
                service.setCookie(m_cookies[i]);
            }
        }
    }

    /**
     * Use this method to set User Credentials (Token) 
     * 
     */
    protected void setAuthToken(String authToken) {
        m_authToken = authToken;
    }

    /**
     * Use this method to set User Credentials (Cookie)
     * 
     */
    protected void setCookies(Cookie[] cookies) {
        m_cookies = cookies;
    }

    /**
     * Use this method to get the Invoker Options on the Service and set them to user-preferences
     * 
     */
    public ServiceInvokerOptions getServiceInvokerOptions()
        throws ServiceException
    {
        m_service = getService();
        return m_service.getInvokerOptions();
    }

    protected AsyncSOAMetricsQueryService getProxy()
        throws ServiceException
    {
        m_service = getService();
        m_proxy = m_service.getProxy();
        return m_proxy;
    }

    /**
     * Method returns an instance of Service which has been initilized for this Consumer
     * 
     */
    public Service getService()
        throws ServiceException
    {
        if (m_service == null) {
            m_service = ServiceFactory.create(SVC_ADMIN_NAME, m_environment, m_clientName, m_serviceLocation);
        }
        setUserProvidedSecurityCredentials(m_service);
        return m_service;
    }

    public Future<?> getErrorMetricsMetadataAsync(GetErrorMetricsMetadataRequest param0, AsyncHandler<GetErrorMetricsMetadataResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getErrorMetricsMetadataAsync(param0, param1);
        return result;
    }

    public Response<GetErrorMetricsMetadataResponse> getErrorMetricsMetadataAsync(GetErrorMetricsMetadataRequest param0) {
        Response<GetErrorMetricsMetadataResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getErrorMetricsMetadataAsync(param0);
        return result;
    }

    public Future<?> getErrorMetricsDataAsync(GetErrorMetricsDataRequest param0, AsyncHandler<GetErrorMetricsDataResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getErrorMetricsDataAsync(param0, param1);
        return result;
    }

    public Response<GetErrorMetricsDataResponse> getErrorMetricsDataAsync(GetErrorMetricsDataRequest param0) {
        Response<GetErrorMetricsDataResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getErrorMetricsDataAsync(param0);
        return result;
    }

    public Future<?> getDetailDataAsync(GetDetailDataRequest param0, AsyncHandler<GetDetailDataResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getDetailDataAsync(param0, param1);
        return result;
    }

    public Response<GetDetailDataResponse> getDetailDataAsync(GetDetailDataRequest param0) {
        Response<GetDetailDataResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getDetailDataAsync(param0);
        return result;
    }

    public Future<?> getErrorGraphAsync(GetErrorGraphRequest param0, AsyncHandler<GetErrorGraphResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getErrorGraphAsync(param0, param1);
        return result;
    }

    public Response<GetErrorGraphResponse> getErrorGraphAsync(GetErrorGraphRequest param0) {
        Response<GetErrorGraphResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getErrorGraphAsync(param0);
        return result;
    }

    public Future<?> getPolicyMetricDetailDataAsync(GetPolicyMetricDetailDataRequest param0, AsyncHandler<GetPolicyMetricDataResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getPolicyMetricDetailDataAsync(param0, param1);
        return result;
    }

    public Response<GetPolicyMetricDataResponse> getPolicyMetricDetailDataAsync(GetPolicyMetricDetailDataRequest param0) {
        Response<GetPolicyMetricDataResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getPolicyMetricDetailDataAsync(param0);
        return result;
    }

    public Future<?> getStandardReportDataAsync(GetStandardReportRequest param0, AsyncHandler<GetStandardReportResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getStandardReportDataAsync(param0, param1);
        return result;
    }

    public Response<GetStandardReportResponse> getStandardReportDataAsync(GetStandardReportRequest param0) {
        Response<GetStandardReportResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getStandardReportDataAsync(param0);
        return result;
    }

    public Future<?> getVersionAsync(GetVersionRequest param0, AsyncHandler<GetVersionResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getVersionAsync(param0, param1);
        return result;
    }

    public Response<GetVersionResponse> getVersionAsync(GetVersionRequest param0) {
        Response<GetVersionResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getVersionAsync(param0);
        return result;
    }

    public Future<?> getMetricsMetadataAsync(GetMetricsMetadataRequest param0, AsyncHandler<GetMetricsMetadataResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getMetricsMetadataAsync(param0, param1);
        return result;
    }

    public Response<GetMetricsMetadataResponse> getMetricsMetadataAsync(GetMetricsMetadataRequest param0) {
        Response<GetMetricsMetadataResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getMetricsMetadataAsync(param0);
        return result;
    }

    public Future<?> getMetricSummaryDataAsync(GetMetricSummaryDataRequest param0, AsyncHandler<GetMetricSummaryDataResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getMetricSummaryDataAsync(param0, param1);
        return result;
    }

    public Response<GetMetricSummaryDataResponse> getMetricSummaryDataAsync(GetMetricSummaryDataRequest param0) {
        Response<GetMetricSummaryDataResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getMetricSummaryDataAsync(param0);
        return result;
    }

    public Future<?> getCustomReportDataAsync(GetCustomReportDataRequest param0, AsyncHandler<GetCustomReportDataResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getCustomReportDataAsync(param0, param1);
        return result;
    }

    public Response<GetCustomReportDataResponse> getCustomReportDataAsync(GetCustomReportDataRequest param0) {
        Response<GetCustomReportDataResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getCustomReportDataAsync(param0);
        return result;
    }

    public Future<?> getMetricsDataAsync(GetMetricsRequest param0, AsyncHandler<GetMetricsResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getMetricsDataAsync(param0, param1);
        return result;
    }

    public Response<GetMetricsResponse> getMetricsDataAsync(GetMetricsRequest param0) {
        Response<GetMetricsResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getMetricsDataAsync(param0);
        return result;
    }

    public Future<?> getPolicyMetricDataAsync(GetPolicyMetricDataRequest param0, AsyncHandler<GetPolicyMetricDataResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getPolicyMetricDataAsync(param0, param1);
        return result;
    }

    public Response<GetPolicyMetricDataResponse> getPolicyMetricDataAsync(GetPolicyMetricDataRequest param0) {
        Response<GetPolicyMetricDataResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getPolicyMetricDataAsync(param0);
        return result;
    }

    public Future<?> getMetricValueAsync(GetMetricValueRequest param0, AsyncHandler<GetMetricValueResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getMetricValueAsync(param0, param1);
        return result;
    }

    public Response<GetMetricValueResponse> getMetricValueAsync(GetMetricValueRequest param0) {
        Response<GetMetricValueResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getMetricValueAsync(param0);
        return result;
    }

    public List<Response<?>> poll(boolean param0, boolean param1)
        throws InterruptedException
    {
        List<Response<?>> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.poll(param0, param1);
        return result;
    }

    public GetErrorMetricsMetadataResponse getErrorMetricsMetadata(GetErrorMetricsMetadataRequest param0) {
        GetErrorMetricsMetadataResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getErrorMetricsMetadata(param0);
        return result;
    }

    public GetErrorMetricsDataResponse getErrorMetricsData(GetErrorMetricsDataRequest param0) {
        GetErrorMetricsDataResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getErrorMetricsData(param0);
        return result;
    }

    public GetDetailDataResponse getDetailData(GetDetailDataRequest param0) {
        GetDetailDataResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getDetailData(param0);
        return result;
    }

    public GetErrorGraphResponse getErrorGraph(GetErrorGraphRequest param0) {
        GetErrorGraphResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getErrorGraph(param0);
        return result;
    }

    public GetPolicyMetricDataResponse getPolicyMetricDetailData(GetPolicyMetricDetailDataRequest param0) {
        GetPolicyMetricDataResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getPolicyMetricDetailData(param0);
        return result;
    }

    public GetStandardReportResponse getStandardReportData(GetStandardReportRequest param0) {
        GetStandardReportResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getStandardReportData(param0);
        return result;
    }

    public GetVersionResponse getVersion(GetVersionRequest param0) {
        GetVersionResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getVersion(param0);
        return result;
    }

    public GetMetricsMetadataResponse getMetricsMetadata(GetMetricsMetadataRequest param0) {
        GetMetricsMetadataResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getMetricsMetadata(param0);
        return result;
    }

    public GetMetricSummaryDataResponse getMetricSummaryData(GetMetricSummaryDataRequest param0) {
        GetMetricSummaryDataResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getMetricSummaryData(param0);
        return result;
    }

    public GetCustomReportDataResponse getCustomReportData(GetCustomReportDataRequest param0) {
        GetCustomReportDataResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getCustomReportData(param0);
        return result;
    }

    public GetMetricsResponse getMetricsData(GetMetricsRequest param0) {
        GetMetricsResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getMetricsData(param0);
        return result;
    }

    public GetPolicyMetricDataResponse getPolicyMetricData(GetPolicyMetricDataRequest param0) {
        GetPolicyMetricDataResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getPolicyMetricData(param0);
        return result;
    }

    public GetMetricValueResponse getMetricValue(GetMetricValueRequest param0) {
        GetMetricValueResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getMetricValue(param0);
        return result;
    }

}
