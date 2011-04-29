/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import com.google.gwt.core.client.GWT;

/**
 * ErrorMetricsDataRequest.
 */
public class ErrorMetricsDataRequest {

    /**
     * Gets the rest url.
     *
     * @param errorCriteria the error criteria
     * @param criteria the criteria
     * @return the rest url
     */
    public static String getRestURL (ErrorCriteria errorCriteria, MetricCriteria criteria) {
        String url = GWT.getModuleBaseURL()+"smqs";
        url += "?"+getUrl(errorCriteria, criteria);
        return url;
    }
    
    /**
     * Gets the rest download url.
     *
     * @param errorCriteria the error criteria
     * @param criteria the criteria
     * @return the rest download url
     */
    public static String getRestDownloadUrl (ErrorCriteria errorCriteria, MetricCriteria criteria) {
        String url = GWT.getModuleBaseURL()+"dwnld";
        url += "?"+getUrl(errorCriteria, criteria);
        return url;
    }
    
    private static String getUrl (ErrorCriteria errorCriteria, MetricCriteria criteria) {
        String url = "";
        url += MetricsQueryService.SERVICE_NAME_HEADER_VALUE;
        url += "&"+MetricsQueryService.OPERATION_NAME_HEADER+"=getErrorMetricsData";
        url += "&"+MetricsQueryService.USECASE_HEADER_VALUE;
        url += "&"+MetricsQueryService.NV_DATA_FORMAT_HEADER_VALUE;
        url += "&"+MetricsQueryService.JSON_RESPONSE_FORMAT_HEADER_VALUE;
        url += "&"+MetricsQueryService.NV_NAMESPACE;
        
        url += errorCriteria.asRestUrl();
        url += criteria.asRestUrl();
        return url;
    }
}
