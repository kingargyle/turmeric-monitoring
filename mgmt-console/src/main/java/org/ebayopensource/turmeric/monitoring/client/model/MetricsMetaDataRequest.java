/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.Iterator;
import java.util.Set;

import com.google.gwt.core.client.GWT;

/**
 * The Class MetricsMetaDataRequest.
 */
public class MetricsMetaDataRequest {

    /**
     * Gets the rest url.
     *
     * @param resourceEntityType the resource entity type
     * @param resourceEntityName the resource entity name
     * @param resourceEntityResponseType the resource entity response type
     * @return the rest url
     */
    public static String getRestURL (String resourceEntityType, Set<String> resourceEntityName, String resourceEntityResponseType) {
        String baseURL = GWT.getModuleBaseURL()+"smqs";
        String url = baseURL;
        url += "?"+MetricsQueryService.SERVICE_NAME_HEADER_VALUE;
        url += "&"+MetricsQueryService.OPERATION_NAME_HEADER+"=getMetricsMetadata";
        url += "&"+MetricsQueryService.USECASE_HEADER_VALUE;
        url += "&"+MetricsQueryService.NV_DATA_FORMAT_HEADER_VALUE;
        url += "&"+MetricsQueryService.JSON_RESPONSE_FORMAT_HEADER_VALUE;
        url += "&"+MetricsQueryService.NV_NAMESPACE;
        
        url +="&ns:resourceEntityType="+resourceEntityType;

        if (resourceEntityName != null && resourceEntityName.size() > 0) {
            if (resourceEntityName.size() == 1)
                url+="&ns:resourceEntityName="+resourceEntityName.iterator().next();
            else {
                int i=0;
                for (String s:resourceEntityName) {
                    url+="&ns:resourceEntityName("+(i++)+")="+s;
                }
            }
        }
        url += "&ns:resourceEntityResponseType="+resourceEntityResponseType;
        
        return url;
    }
    
    /**
     * Gets the jSON url.
     *
     * @return the jSON url
     */
    public static String getJSONUrl () {
        String baseURL = GWT.getModuleBaseURL()+"smqs";
        String url = baseURL;
        url += "?"+MetricsQueryService.SERVICE_NAME_HEADER_VALUE;
        url += "&"+MetricsQueryService.OPERATION_NAME_HEADER+"=getMetricsMetadata";
        url += "&"+MetricsQueryService.USECASE_HEADER_VALUE;
        url += "&"+MetricsQueryService.JSON_DATA_FORMAT_HEADER_VALUE;
        url += "&"+MetricsQueryService.JSON_RESPONSE_FORMAT_HEADER_VALUE;
        url += "&"+MetricsQueryService.NV_NAMESPACE;
        return url;
    }
    
    /**
     * Gets the jSON.
     *
     * @param resourceEntityType the resource entity type
     * @param resourceEntityName the resource entity name
     * @param resourceEntityResponseType the resource entity response type
     * @return the jSON
     */
    public static String getJSON (String resourceEntityType, Set<String> resourceEntityName, String resourceEntityResponseType) {
        String json = ""+
        "{" +
        "   \"jsonns.ns\":\""+MetricsQueryService.NAMESPACE+"\"," +
        "   \"ns.getMetricsMetadataRequest\":{" +
        "       \"ns.resourceEntityType\":\""+resourceEntityType+"\","+
        "       \"ns.resourceEntityName\":[";
        Iterator<String> itor = resourceEntityName.iterator();
        while (itor.hasNext()) {
            json += "\""+itor.next()+"\"";
            if (itor.hasNext())
                json += ",";
        }
        json += "],"+
        "       \"ns.resourceEntityResponseType\":\"" + resourceEntityResponseType + "\"" +
        "   }" +
        "}";
        
        return json;
    }
}
