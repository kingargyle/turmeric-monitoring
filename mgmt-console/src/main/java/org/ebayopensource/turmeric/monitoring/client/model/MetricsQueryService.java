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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The Interface MetricsQueryService.
 */
public interface MetricsQueryService extends ConsoleService {
    

    /** The Constant SERVICE_NAME_HEADER_VALUE. */
    public static final String SERVICE_NAME_HEADER_VALUE=SERVICE_NAME_HEADER+"=SOAMetricsQueryService";
    
    /** The Constant NAMESPACE. */
    public static final String NAMESPACE = "http://www.ebayopensource.org/turmeric/monitoring/v1/services";
    
    /** The Constant NV_NAMESPACE. */
    public static final String NV_NAMESPACE="nvns:ns="+NAMESPACE;
    
    /** The Constant DEFAULT_DURATION_HRS. */
    public final static int DEFAULT_DURATION_HRS = 1;
	
	/**
	 * The Enum Ordering.
	 */
	public enum Ordering {
/** The Ascending. */
Ascending, 
 /** The Descending. */
 Descending};
	
	/**
	 * The Enum Entity.
	 */
	public enum Entity {
/** The Service. */
Service, 
 /** The Operation. */
 Operation, 
 /** The Consumer. */
 Consumer, 
 /** The Machine. */
 Machine, 
 /** The Pool. */
 Pool, 
 /** The Error. */
 Error};
	
	/**
	 * The Enum Perspective.
	 */
	public enum Perspective {
/** The Client. */
Client,
/** The Server. */
Server};
	
	/**
	 * The Enum ErrorType.
	 */
	public enum ErrorType {
/** The Category. */
Category,
/** The Severity. */
Severity};
	
	/**
	 * The Enum ErrorCategory.
	 */
	public enum ErrorCategory {
/** The Request. */
Request,
/** The Application. */
Application,
/** The System. */
System};
	
	/**
	 * The Enum ErrorSeverity.
	 */
	public enum ErrorSeverity{
/** The Critical. */
Critical,
/** The Error. */
Error,
/** The Warning. */
Warning};
	
	/**
	 * The Class EntityName.
	 */
	public static class EntityName {
	    
    	/** The type. */
    	public Entity type;
	    
    	/** The names. */
    	public List<String> names;
	    
	    /**
    	 * Adds the.
    	 *
    	 * @param name the name
    	 */
    	public void add(String name) {
	        if (names == null)
	            names = new ArrayList<String>();
	        names.add(name);
	    }
	}
	
	
	
	/**
	 * Get the list of services.
	 *
	 * @param callback when the data is ready the callback is invoked
	 * @return the services
	 */
	public void getServices(AsyncCallback<Map<String,Set<String>>> callback);
	

	
	/**
	 * Get the values for a metric for a service or an operation of a service.
	 *
	 * @param mc the metric criteria to use eg start times, duration etc
	 * @param mrc the resource metric criteria to use eg selected entities and return entity type
	 * @param callback the callback
	 * @return the metric data
	 */
	public void getMetricData(MetricCriteria mc,
	                          MetricResourceCriteria mrc,
	                          AsyncCallback<MetricData> callback);
	
	/* 
	 * public void getMetricTimeSlotData (MetricCriteria mc, MetricResourceCriteria mrc (?), AsyncCallback<MetricTimeSlotData>);
	 */
	
	/**
	 * Get a REST url to request metrics.
	 * 
	 * @param mc the metric criteria to use eg start times, duration etc
	 * @param mrc the resource metric criteria to use eg selected entities and return entity type
	 * @return a REST url
	 */
	public String getMetricDataDownloadUrl (MetricCriteria mc,
	                                        MetricResourceCriteria mrc);
	
	
	
    /**
     * Gets the error data.
     *
     * @param ec the ec
     * @param mc the mc
     * @param callback the callback
     * @return the error data
     */
    public void getErrorData(ErrorCriteria ec,
                             MetricCriteria mc,
                             AsyncCallback<ErrorMetricData> callback);
    
    /**
     * Gets the error time slot data.
     *
     * @param ec the ec
     * @param mc the mc
     * @param callback the callback
     * @return the error time slot data
     */
    public void getErrorTimeSlotData (ErrorCriteria ec,
                                      MetricCriteria mc,
                                      AsyncCallback<ErrorTimeSlotData> callback);
    
    
    /**
     * Gets the error data download url.
     *
     * @param ec the ec
     * @param mc the mc
     * @return the error data download url
     */
    public String getErrorDataDownloadUrl(ErrorCriteria ec,
                                          MetricCriteria mc);

    
    /**
     * Gets the error detail.
     *
     * @param errorId the error id
     * @param errorName the error name
     * @param service the service
     * @param callback the callback
     * @return the error detail
     */
    public void getErrorDetail(String errorId, String errorName, String service, AsyncCallback<ErrorDetail>callback);
    
    /**
     * Get the data required to plot the service call trend in a graph, corresponding to one date.
     *
     * @param firstDate the first date
     * @param secondDate the second date
     * @param callback the callback
     * @return the service metric value trend
     */
    public void getMetricValueTrend(MetricValue firstDate, MetricValue secondDate, AsyncCallback<List<TimeSlotData>> callback);
    

    /**
     * Talk to the remote server to obtain a list of all consumers for the given service.
     *
     * @param serviceName The serviceadminname
     * @param callback the callback
     * @return the service consumers
     */
    public void getServiceConsumers(final String serviceName, final AsyncCallback<Set<String>> callback);
    
}
