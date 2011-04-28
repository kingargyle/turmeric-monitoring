/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * MetricsQueryServiceImpl
 * 
 * Talks to a remote server that can supply SOAMetricsQueryService wsdl.
 * 
 * At the time of writing, turmeric does not support JSONP, so the remote server is in fact a proxy that talks tothe
 * SOAMetricsQueryService and returns the results.
 * 
 * Calls are made using a REST url (ie call is encoded in query params - referred to in documentation as "NV" style
 * invocation).
 * 
 * Data is returned as JSON.
 * 
 */
public class MetricsQueryServiceImpl extends AbstractConsoleService implements MetricsQueryService {

    public MetricsQueryServiceImpl() {
        Map<String, String> config = ConsoleUtil.getConfig();
        if (config == null)
            return;

        String tmp = config.get("maxAggregationPeriod");
        if (tmp != null) {
            try {
                MetricCriteria.maxAggregationPeriod = Integer.valueOf(tmp).intValue();
            }
            catch (NumberFormatException e) {
                // log?
            }
        }

        tmp = config.get("medAggregationPeriod");
        if (tmp != null) {
            try {
                MetricCriteria.medAggregationPeriod = Integer.valueOf(tmp).intValue();
            }
            catch (NumberFormatException e) {
                //log?
            }
        }
        tmp = config.get("minAggregationPeriod");
        if (tmp != null) {
            try {
                MetricCriteria.minAggregationPeriod = Integer.valueOf(tmp).intValue();
            }
            catch (NumberFormatException e) {
               //log?
            }
        }
    }

    /**
     * Call the remote server to obtain metrics measurements.
     * 
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService#getMetricData(org.ebayopensource.turmeric.monitoring.client.model.MetricCriteria,
     *      org.ebayopensource.turmeric.monitoring.client.model.MetricResourceCriteria,
     *      com.google.gwt.user.client.rpc.AsyncCallback)
     */
    public void getMetricData(final MetricCriteria criteria, final MetricResourceCriteria resourceCriteria,
                    final AsyncCallback<MetricData> callback) {

        final String url = URL.encode(MetricsDataRequest.getRestURL(criteria, resourceCriteria));
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        final MetricData data = new MetricData();
        data.setRestUrl(url);
        data.setMetricCriteria(criteria);
        data.setMetricResourceCriteria(resourceCriteria);

        try {
            builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable err) {
                    callback.onFailure(err);
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() != Response.SC_OK) {
                        callback.onFailure(getErrorAsThrowable(response));
                    }
                    else if (response.getHeader(ERROR_HEADER) != null) {
                        callback.onFailure(getErrorAsThrowable(response));
                    }
                    else {
                        MetricsDataResponse metricsResponse = MetricsDataResponse.fromJSON(response.getText());
                        if (metricsResponse == null) {
                            GWT.log("bad response: " + response.getText());
                            callback.onFailure(new Throwable(ConsoleUtil.messages.badOrMissingResponseData()));
                        }
                        else {
                            JsArray<MetricGroupDataJS> rows = metricsResponse.getReturnData();
                            List<MetricGroupData> results = new ArrayList<MetricGroupData>();
                            if (rows != null) {
                                for (int i = 0; i < rows.length(); i++) {
                                    MetricGroupDataJS js = rows.get(i);
                                    results.add(js);
                                }
                            }
                            data.setReturnData(results);
                            callback.onSuccess(data);
                        }
                    }
                }
            });
        }
        catch (RequestException x) {
            callback.onFailure(x);
        }
    }

    public String getMetricDataDownloadUrl(MetricCriteria criteria, MetricResourceCriteria resourceCriteria) {
        return URL.encode(MetricsDataRequest.getRestDownloadUrl(criteria, resourceCriteria));
    }

    /**
     * Call the remote server to obtain the list of services and their operations.
     * 
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService#getServices(com.google.gwt.user.client.rpc.AsyncCallback)
     */
    public void getServices(final AsyncCallback<Map<String, Set<String>>> callback) {

        final String url = MetricsMetaDataRequest.getRestURL("Service", null, "Service");

        // the final results
        final Map<String, Set<String>> serviceMap = new TreeMap<String, Set<String>>();

        // Ask for the services
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
        try {
            builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable err) {
                    callback.onFailure(err);
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() != Response.SC_OK) {
                        GWT.log("Errored request: " + url + " response code=" + response.getStatusCode() + " response="
                                        + response.getText());
                        callback.onFailure(getErrorAsThrowable(response));
                    }
                    else if (response.getHeader(ERROR_HEADER) != null) {
                        GWT.log("Errored request: " + url + " response code=" + response.getStatusCode() + " response="
                                        + response.getText());
                        callback.onFailure(getErrorAsThrowable(response));
                    }
                    else {
                        // convert JSON to list of service names
                        GWT.log(response.getText());
                        MetricsMetaDataResponse metaDataResponse = MetricsMetaDataResponse.fromJSON(response.getText());
                        if (metaDataResponse == null)
                            callback.onFailure(new Throwable(ConsoleUtil.messages.badRequestData()));
                        else {
                            Set<String> services = metaDataResponse.getOrderedResourceEntityResponseNames();
                            for (String s : services) {
                                GWT.log("service " + s);
                                serviceMap.put(s, new TreeSet<String>());
                            }

                            // Now ask for the operations of all the services
                            getServiceOperationsJSON(serviceMap, callback);
                        }
                    }
                }
            });
        }
        catch (RequestException x) {
            callback.onFailure(x);
        }
    }

    /**
     * Talk to the remote server to obtain a list of all operations for the given services.
     * 
     * @param serviceMap
     *            keys are the list of services for which to obtain the operations
     * @param callback
     */
    public void getServiceOperations(final Map<String, Set<String>> serviceMap,
                    final AsyncCallback<Map<String, Set<String>>> callback) {

        final String url = MetricsMetaDataRequest.getRestURL("Service", serviceMap.keySet(), "Operation");
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
        try {
            builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable err) {
                    callback.onFailure(err);
                }

                public void onResponseReceived(final Request request, final Response response) {
                    if (response.getStatusCode() != Response.SC_OK) {
                        GWT.log("Errored request: " + url + " response code=" + response.getStatusCode() + " response="
                                        + response.getText());
                        callback.onFailure(getErrorAsThrowable(response));
                    }
                    else if (response.getHeader(ERROR_HEADER) != null) {
                        GWT.log("Errored request: " + url + " response code=" + response.getStatusCode() + " response="
                                        + response.getText());
                        callback.onFailure(getErrorAsThrowable(response));
                    }
                    else {
                        MetricsMetaDataResponse metaDataResponse = MetricsMetaDataResponse.fromJSON(response.getText());
                        if (metaDataResponse == null)
                            callback.onFailure(new Throwable(ConsoleUtil.messages.badOrMissingResponseData()));
                        else {
                            Set<String> operationNames = metaDataResponse.getOrderedResourceEntityResponseNames();
                            String error = null;
                            Iterator<String> itor = operationNames.iterator();
                            while (itor.hasNext() && error == null) {
                                String s = itor.next();

                                int dot = s.indexOf(".");
                                if (dot < 0) {
                                    error = s;
                                }
                                else {
                                    Set<String> operations = serviceMap.get(s.substring(0, dot));
                                    if (operations != null)
                                        operations.add(s.substring(dot + 1));
                                }
                            }
                            if (error != null)
                                callback.onFailure(new Throwable(error));
                            else
                                callback.onSuccess(serviceMap);
                        }
                    }
                }
            });
        }
        catch (RequestException x) {
            callback.onFailure(x);
        }
    }

    public void getServiceOperationsJSON(final Map<String, Set<String>> serviceMap,
                    final AsyncCallback<Map<String, Set<String>>> callback) {

        final String url = MetricsMetaDataRequest.getJSONUrl();
        final String json = MetricsMetaDataRequest.getJSON("Service", serviceMap.keySet(), "Operation");
        GWT.log(json);
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
        try {
            builder.sendRequest(json, new RequestCallback() {

                public void onError(Request request, Throwable err) {
                    callback.onFailure(err);
                }

                public void onResponseReceived(final Request request, final Response response) {
                    if (response.getStatusCode() != Response.SC_OK) {
                        GWT.log("Errored request: " + url + " response code=" + response.getStatusCode() + " response="
                                        + response.getText());
                        callback.onFailure(getErrorAsThrowable(response));
                    }
                    else if (response.getHeader(ERROR_HEADER) != null) {
                        GWT.log("Errored request: " + url + " Response=" + response.getText());
                        callback.onFailure(getErrorAsThrowable(response));
                    }
                    else {
                        MetricsMetaDataResponse metaDataResponse = MetricsMetaDataResponse.fromJSON(response.getText());
                        if (metaDataResponse == null)
                            callback.onFailure(new Throwable(ConsoleUtil.messages.badOrMissingResponseData()));
                        else {
                            Set<String> operationNames = metaDataResponse.getOrderedResourceEntityResponseNames();
                            String error = null;
                            Iterator<String> itor = operationNames.iterator();
                            while (itor.hasNext() && error == null) {
                                String s = itor.next();

                                int dot = s.indexOf(".");
                                if (dot < 0) {
                                    error = s;
                                }
                                else {
                                    Set<String> operations = serviceMap.get(s.substring(0, dot));
                                    if (operations != null)
                                        operations.add(s.substring(dot + 1));
                                }
                            }
                            if (error != null)
                                callback.onFailure(new Throwable(error));
                            else
                                callback.onSuccess(serviceMap);
                        }
                    }
                }
            });
        }
        catch (RequestException x) {
            callback.onFailure(x);
        }
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService#getErrorData(org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorType,
     *      java.util.List, java.util.List, java.util.List, java.lang.String, boolean,
     *      org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorCategory,
     *      org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorSeverity,
     *      org.ebayopensource.turmeric.monitoring.client.model.MetricCriteria)
     */
    public void getErrorData(final ErrorCriteria errorCriteria, final MetricCriteria metricCriteria,
                    final AsyncCallback<ErrorMetricData> callback) {
        final String url = URL.encode(ErrorMetricsDataRequest.getRestURL(errorCriteria, metricCriteria));
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        final ErrorMetricData data = new ErrorMetricData();
        data.setRestUrl(url);
        data.setErrorCriteria(errorCriteria);
        data.setMetricCriteria(metricCriteria);

        try {
            builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable err) {
                    callback.onFailure(err);
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() != Response.SC_OK) {
                        GWT.log("Errored request: " + url + " response code=" + response.getStatusCode() + " response="
                                        + response.getText());
                        callback.onFailure(getErrorAsThrowable(response));
                    }
                    else if (response.getHeader(ERROR_HEADER) != null) {
                        GWT.log("Errored request: " + url + " Response=" + response.getText());
                        callback.onFailure(getErrorAsThrowable(response));
                    }
                    else {
                        GWT.log("Response for ErrorMetrics: " + response.getText());
                        ErrorMetricsDataResponse metricsResponse = ErrorMetricsDataResponse.fromJSON(response.getText());
                        if (metricsResponse == null) {
                            GWT.log("Errored request: " + url + " Response=" + response.getText());
                            callback.onFailure(new Throwable(ConsoleUtil.messages.badOrMissingResponseData()));
                        }
                        else {
                            JsArray<ErrorViewDataJS> rows = metricsResponse.getReturnData();
                            List<ErrorViewData> results = new ArrayList<ErrorViewData>();
                            if (rows != null) {
                                for (int i = 0; i < rows.length(); i++) {
                                    ErrorViewDataJS js = rows.get(i);
                                    results.add(js);
                                }
                            }
                            data.setReturnData(results);
                            callback.onSuccess(data);
                        }
                    }
                }
            });
        }
        catch (RequestException x) {
            callback.onFailure(x);
            GWT.log("Exception in server call: " + x.toString());
        }
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService#getErrorDataDownloadUrl(org.ebayopensource.turmeric.monitoring.client.model.ErrorCriteria,
     *      org.ebayopensource.turmeric.monitoring.client.model.MetricCriteria)
     */
    public String getErrorDataDownloadUrl(ErrorCriteria ec, MetricCriteria mc) {
        return URL.encode(ErrorMetricsDataRequest.getRestDownloadUrl(ec, mc));
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService#getErrorDetail(java.lang.String,
     *      java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    public void getErrorDetail(final String errorId, final String errorName, final String service,
                    final AsyncCallback<ErrorDetail> callback) {
        final String url = URL.encode(ErrorMetricsMetadataRequest.getRestURL(errorId, errorName, service));
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

        try {
            builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable err) {
                    callback.onFailure(err);
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() != Response.SC_OK) {
                        callback.onFailure(new Throwable("Error " + response.getStatusCode()));
                    }
                    else if (response.getHeader(ERROR_HEADER) != null) {
                        callback.onFailure(new Throwable(ConsoleUtil.messages.badRequestData()));
                    }
                    else {
                        ErrorMetricsMetadataResponse metricsResponse = ErrorMetricsMetadataResponse.fromJSON(response
                                        .getText());
                        if (metricsResponse == null) {
                            callback.onFailure(new Throwable(ConsoleUtil.messages.badOrMissingResponseData()));
                        }
                        else {

                            ErrorDetailJS js = metricsResponse.getReturnData();
                            if (js == null)
                                callback.onFailure(new Throwable(ConsoleUtil.messages.badOrMissingResponseData()));
                            else
                                callback.onSuccess(js);
                        }
                    }
                }
            });
        }
        catch (RequestException x) {
            callback.onFailure(x);
        }
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService#getErrorTimeSlotData(org.ebayopensource.turmeric.monitoring.client.model.ErrorCriteria,
     *      org.ebayopensource.turmeric.monitoring.client.model.MetricCriteria,
     *      com.google.gwt.user.client.rpc.AsyncCallback)
     */
    public void getErrorTimeSlotData(final ErrorCriteria ec, final MetricCriteria mc,
                    final AsyncCallback<ErrorTimeSlotData> callback) {

        final String url = URL.encode(ErrorMetricsGraphRequest.getRestURL(ec, mc));
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        final ErrorTimeSlotData data = new ErrorTimeSlotData();
        data.setRestUrl(url);
        data.setErrorCriteria(ec);
        data.setMetricCriteria(mc);
        try {
            builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable err) {
                    callback.onFailure(err);
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() != Response.SC_OK) {
                        callback.onFailure(new Throwable("Error " + response.getStatusCode()));
                    }
                    else if (response.getHeader(ERROR_HEADER) != null) {
                        callback.onFailure(new Throwable(ConsoleUtil.messages.badRequestData()));
                    }
                    else {
                        ErrorMetricsGraphResponse graphResponse = ErrorMetricsGraphResponse.fromJSON(response.getText());
                        if (graphResponse == null) {
                            callback.onFailure(new Throwable(ConsoleUtil.messages.badOrMissingResponseData()));
                        }
                        else {
                            JsArray<MetricGraphDataJS> rows = graphResponse.getReturnData();
                            List<TimeSlotValue> results = new ArrayList<TimeSlotValue>();
                            if (rows != null) {
                                for (int i = 0; i < rows.length(); i++) {
                                    MetricGraphDataJS js = rows.get(i);
                                    results.add(js);
                                }
                            }
                            data.setReturnData(results);
                            callback.onSuccess(data);

                        }
                    }
                }
            });
        }
        catch (RequestException x) {
            callback.onFailure(x);
        }
    }

    private void getMetricValueForDate(MetricValue mv, final AsyncCallback<TimeSlotData> callback) throws RequestException {

            GWT.log("timestamp:"+mv.getStartTime());
            final TimeSlotData data = new TimeSlotData();
            data.setReturnData(new ArrayList<TimeSlotValue>(0));
            
            final String url = URL.encode(MetricValueRequest.getRestURL(mv));
            RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
            builder.setTimeoutMillis(60000);
            //GWT.log("getServiceCallTrend url ->" + url);
            builder.sendRequest(null, new RequestCallback() {
                public void onError(Request request, Throwable err) {
                    GWT.log("Error calling the SQMS",err);
                    callback.onFailure(err);
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() != Response.SC_OK) {
                        GWT.log("Response status code: "+response.getStatusCode());
                        callback.onFailure(new RequestException("Response status code: "+response.getStatusCode()));
                    }
                    else if (response.getHeader(ERROR_HEADER) != null) {
                        GWT.log("Error. Response headers : "+response.getHeadersAsString());
                        callback.onFailure(new RequestException("Error. Response headers : "+response.getHeadersAsString()));
                    }
                    else {
                        //GWT.log(response.getText());
                        MetricValueResponse graphResponse = MetricValueResponse.fromJSON(response.getText());
                        if (graphResponse == null) {
                            GWT.log("Null response");
                        }else {
                            JsArray<MetricGraphDataJS> rows = graphResponse.getReturnData();
                            //GWT.log("rows.size = "+rows.length());
                            List<TimeSlotValue> results = new ArrayList<TimeSlotValue>();
                            if (rows != null) {
                                for (int i = 0; i < rows.length(); i++) {
                                    MetricGraphDataJS js = rows.get(i);
                                    results.add(js);
                                }
                            }
                            //GWT.log("results  = "+results.size());
                            data.getReturnData().addAll(results);
                            //GWT.log("adding all results: "+data.getReturnData().size());
                            callback.onSuccess(data);
                        }
                    }
                }
            });
        
    }

    @Override
    public void getServiceMetricValueTrend(MetricValue firstDate, final MetricValue secondDate,
                     final AsyncCallback<List<TimeSlotData>> callback) {
        try {
            final TimeSlotData firstDateRange = new TimeSlotData();
            final TimeSlotData secondDateRange = new TimeSlotData();
            this.getMetricValueForDate(firstDate , new AsyncCallback<TimeSlotData>(){

                @Override
                public void onFailure(Throwable arg0) {
                    Window.alert("Error: "+arg0.getMessage());
                }

                @Override
                public void onSuccess(TimeSlotData arg0) {
                    firstDateRange.setReturnData(arg0.getReturnData());
                    try {
                        getMetricValueForDate(secondDate , new AsyncCallback<TimeSlotData>(){

                            @Override
                            public void onFailure(Throwable arg0) {
                                Window.alert("Error: "+arg0.getMessage());
                            }

                            @Override
                            public void onSuccess(TimeSlotData arg0) {
                                secondDateRange.setReturnData(arg0.getReturnData());
                                List<TimeSlotData> results =  new ArrayList<TimeSlotData>();
                                results.add(firstDateRange);
                                results.add(secondDateRange);
                                if(firstDateRange != null && secondDateRange != null){
                                    callback.onSuccess(results);
                                }else{
                                    callback.onFailure(new Exception("Error getting the graphs"));
                                }
                            }
                            
                        });
                    }
                    catch (RequestException e) {
                        e.printStackTrace();
                    }
                }
                
            });
            
        }
        catch (RequestException x) {
            callback.onFailure(x);
        }

    }

}
