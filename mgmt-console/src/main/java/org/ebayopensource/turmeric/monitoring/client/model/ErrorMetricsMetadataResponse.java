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

/**
 * ErrorMetricsMetadataResponse.
 */
public class ErrorMetricsMetadataResponse extends JavaScriptObject {
    
    /**
     * Instantiates a new error metrics metadata response.
     */
    protected ErrorMetricsMetadataResponse() {
    }
    
    /**
     * From json.
     *
     * @param json the json
     * @return the error metrics metadata response
     */
    public static final native ErrorMetricsMetadataResponse fromJSON (String json) /*-{
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
    public final native ErrorDetailJS getReturnData () /*-{
        if (!this.getErrorMetricsMetadataResponse)
            return null;
        return this.getErrorMetricsMetadataResponse.returnData;
    }-*/;
}