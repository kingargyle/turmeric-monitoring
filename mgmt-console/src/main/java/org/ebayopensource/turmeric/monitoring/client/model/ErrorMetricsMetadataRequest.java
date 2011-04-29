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
 * ErrorMetricsMetadataRequest.
 */
public class ErrorMetricsMetadataRequest {
    
    /**
     * Gets the rest url.
     *
     * @param errorId the error id
     * @param errorName the error name
     * @param service the service
     * @return the rest url
     */
    public static String getRestURL (String errorId, String errorName, String service) {
        String url = GWT.getModuleBaseURL()+"smqs";
        url += "?"+getUrl(errorId, errorName, service);
        return url;
    }
    
    /**
     * Gets the rest download url.
     *
     * @param errorId the error id
     * @param errorName the error name
     * @param service the service
     * @return the rest download url
     */
    public static String getRestDownloadUrl (String errorId, String errorName, String service) {
        String url = GWT.getModuleBaseURL()+"dwnld";
        url += "?"+getUrl(errorId, errorName, service);
        return url;
    }
    
    /**
     * Gets the url.
     *
     * @param errorId the error id
     * @param errorName the error name
     * @param service the service
     * @return the url
     */
    public static String getUrl (String errorId, String errorName, String service) {
        String url = "";
        url += MetricsQueryService.SERVICE_NAME_HEADER_VALUE;
        url += "&"+MetricsQueryService.OPERATION_NAME_HEADER+"=getErrorMetricsMetadata";
        url += "&"+MetricsQueryService.USECASE_HEADER_VALUE;
        url += "&"+MetricsQueryService.NV_DATA_FORMAT_HEADER_VALUE;
        url += "&"+MetricsQueryService.JSON_RESPONSE_FORMAT_HEADER_VALUE;
        url += "&"+MetricsQueryService.NV_NAMESPACE;
        if (errorId != null)
            url += "&ns:errorId="+errorId;
        if (errorName != null)
            url += "&ns:errorName="+errorName;
        url += "&ns:serviceName="+service;
        return url;
    }
}
