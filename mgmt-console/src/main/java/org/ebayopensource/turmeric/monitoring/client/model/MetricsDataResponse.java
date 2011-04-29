/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * The Class MetricsDataResponse.
 */
public class MetricsDataResponse extends JavaScriptObject {
    
    /**
     * Instantiates a new metrics data response.
     */
    protected MetricsDataResponse() {
    }
    
    /**
     * From json.
     *
     * @param json the json
     * @return the metrics data response
     */
    public static final native MetricsDataResponse fromJSON (String json) /*-{
       try {
           return eval('(' + json + ')');
       } catch (err) {
           return null;
       }
    }-*/;

    /**
     * Gets the return data.
     *
     * @return the return data
     */
    public final native JsArray<MetricGroupDataJS> getReturnData () /*-{
        if (!this.getMetricsDataResponse)
            return null;
        return this.getMetricsDataResponse.returnData;
    }-*/;
}
