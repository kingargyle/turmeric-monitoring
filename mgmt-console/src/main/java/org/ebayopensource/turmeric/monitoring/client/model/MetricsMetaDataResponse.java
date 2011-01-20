/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public class MetricsMetaDataResponse extends JavaScriptObject {

    protected MetricsMetaDataResponse() {
    }
    
    
    public static final native MetricsMetaDataResponse fromJSON (String json) /*-{
       try {
           return eval('(' + json + ')');
       } catch (err) {
           return null;
       }
    }-*/;

  
    public final native JsArrayString getResourceEntityResponseNames () /*-{
        var data = this.getMetricsMetadataResponse;
        if (data) {
            return data.resourceEntityResponseNames;
        } 
    }-*/;

    public final Set<String> getOrderedResourceEntityResponseNames () {
        JsArrayString array = getResourceEntityResponseNames();
        Set<String> set = new TreeSet<String>();
        if (array != null) {
            for (int i=0;i<array.length();i++) {
                set.add(array.get(i));
            }
        }
        return set;
   }
}
