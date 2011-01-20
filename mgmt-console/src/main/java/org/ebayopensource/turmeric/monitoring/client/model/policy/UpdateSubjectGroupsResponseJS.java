/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdateSubjectGroupsResponse;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * UpdateSubjectGroupsResponseJS
 *
 */
public class UpdateSubjectGroupsResponseJS extends JavaScriptObject implements
        UpdateSubjectGroupsResponse {
    public static final String NAME = "ns1.updateSubjectGroupsResponse";

    public static final native UpdateSubjectGroupsResponseJS fromJSON (String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
            return null;
        }
    }-*/;

    
    
    protected UpdateSubjectGroupsResponseJS() {}
    
    
    public final Boolean isSuccess () {
        return Boolean.valueOf(getResultAsString());
    }
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdateSubjectGroupsResponse#getErrorMessage()
     */
    public final native String getErrorMessage() /*-{
        return this["ns1.updateSubjectGroupsResponse"]["ms.errorMessage"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdateSubjectGroupsResponse#isSuccess()
     */
    public final native String getResultAsString() /*-{
        return this["ns1.updateSubjectGroupsResponse"]["ns1.success"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdateSubjectGroupsResponse#isErrored()
     */
    public final native boolean isErrored() /*-{
        if (this["ns1.updateSubjectGroupsResponse"]["ms.ack"] === "Success")
            return false;
        else
            return true;
    }-*/;

}
