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

public class MetricsDataRequest {
    
    public static String getRestURL (MetricCriteria criteria, MetricResourceCriteria resourceCriteria) {
        String url = GWT.getModuleBaseURL()+"smqs";
        url += "?"+getUrl(criteria, resourceCriteria);

        return url;
    }
    
    public static String getRestDownloadUrl (MetricCriteria criteria, MetricResourceCriteria resourceCriteria) {
        String url = GWT.getModuleBaseURL()+"dwnld";
        url += "?"+getUrl(criteria, resourceCriteria);
        return url;
    }
 
    
    private static String getUrl (MetricCriteria criteria, MetricResourceCriteria resourceCriteria) {
        String url = "";
        url += MetricsQueryService.SERVICE_NAME_HEADER_VALUE;
        url += "&"+MetricsQueryService.OPERATION_NAME_HEADER+"=getMetricsData";
        url += "&"+ConsoleService.USECASE_HEADER_VALUE;
        url += "&"+ConsoleService.NV_DATA_FORMAT_HEADER_VALUE;
        url += "&"+ConsoleService.JSON_RESPONSE_FORMAT_HEADER_VALUE;
        url += "&"+MetricsQueryService.NV_NAMESPACE;
        url+=criteria.asRestUrl();
        url+=resourceCriteria.asRestUrl();

        return url;
        
    }
}
