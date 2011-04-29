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


public interface MetricsQueryService extends ConsoleService {
    

    public static final String SERVICE_NAME_HEADER_VALUE=SERVICE_NAME_HEADER+"=SOAMetricsQueryService";
    public static final String NAMESPACE = "http://www.ebayopensource.org/turmeric/monitoring/v1/services";
    public static final String NV_NAMESPACE="nvns:ns="+NAMESPACE;
    public final static int DEFAULT_DURATION_HRS = 1;
	public enum Ordering {Ascending, Descending};
	public enum Entity {Service, Operation, Consumer, Machine, Pool, Error};
	public enum Perspective {Client,Server};
	public enum ErrorType {Category,Severity};
	public enum ErrorCategory {Request,Application,System};
	public enum ErrorSeverity{Critical,Error,Warning};
	
	public static class EntityName {
	    public Entity type;
	    public List<String> names;
	    
	    public void add(String name) {
	        if (names == null)
	            names = new ArrayList<String>();
	        names.add(name);
	    }
	}
	
	
	
	/**
	 * Get the list of services
	 * @param callback when the data is ready the callback is invoked
	 */
	public void getServices(AsyncCallback<Map<String,Set<String>>> callback);
	

	
	/**
	 * Get the values for a metric for a service or an operation of a service
	 * 
     * @param mc the metric criteria to use eg start times, duration etc
     * @param mrc the resource metric criteria to use eg selected entities and return entity type
	 * @param callback
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
	
	
	
    public void getErrorData(ErrorCriteria ec,
                             MetricCriteria mc,
                             AsyncCallback<ErrorMetricData> callback);
    
    public void getErrorTimeSlotData (ErrorCriteria ec,
                                      MetricCriteria mc,
                                      AsyncCallback<ErrorTimeSlotData> callback);
    
    
    public String getErrorDataDownloadUrl(ErrorCriteria ec,
                                          MetricCriteria mc);

    
    public void getErrorDetail(String errorId, String errorName, String service, AsyncCallback<ErrorDetail>callback);
    
    /**
     * Get the data required to plot the service call trend in a graph, corresponding to one date. 
     * @param firstDate
     * @param secondDate 
     * @param callback
     */
    public void getServiceMetricValueTrend(MetricValue firstDate, MetricValue secondDate, AsyncCallback<List<TimeSlotData>> callback);
    
}
