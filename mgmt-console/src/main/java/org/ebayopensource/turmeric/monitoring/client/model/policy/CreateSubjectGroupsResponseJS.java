/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreateSubjectGroupsResponse;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * CreateSubjectGroupsResponseJS
 *
 */
public class CreateSubjectGroupsResponseJS  extends JavaScriptObject implements
        CreateSubjectGroupsResponse {

    public static final String NAME = "ns1.createSubjectGroupsResponse";
    
    
    protected CreateSubjectGroupsResponseJS () {
    }

    public static final native CreateSubjectGroupsResponse fromJSON (String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
            return null;
        }
    }-*/;
    

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreateSubjectGroupsResponse#getSubjectGroupIds()
     */
    public final List<Long> getSubjectGroupIds() {
        JsArrayString idsAsStrings = getSubjectGroupIdsAsStrings();
        List<Long> results = new ArrayList<Long>();
        if (idsAsStrings != null) {
            for (int i=0;i<idsAsStrings.length();i++) {
                String s = idsAsStrings.get(i);
                results.add(Long.valueOf(s));
            }
        }
        return results;
    };

    
    
    public final native JsArrayString getSubjectGroupIdsAsStrings () /*-{
        return this["ns1.createSubjectGroupsResponse"]["ns1.subjectGroupIds"];
    }-*/;
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.BaseResponse#getErrorMessage()
     */
    public final native String getErrorMessage() /*-{
        return this["ns1.createSubjectGroupsResponse"]["ms.errorMessage"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.BaseResponse#isErrored()
     */
    public final native boolean isErrored() /*-{
        if (this["ns1.createSubjectGroupsResponse"]["ms.ack"] === "Success")
            return false;
        else
            return true;
    }-*/;

    
    
}
