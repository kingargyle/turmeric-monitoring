/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreatePolicyResponse;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * CreatePolicyResponseJS
 *
 */
public class CreatePolicyResponseJS extends JavaScriptObject implements
        CreatePolicyResponse {
    
    public static final String NAME = "ns1.createPolicyResponse";

    protected CreatePolicyResponseJS () {}

    
    public static final native CreatePolicyResponse fromJSON (String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
            return null;
        }
    }-*/;
    
    @Override
    public final Long getPolicyId () {
        return Long.valueOf(getPolicyIdAsString());
    }
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreatePolicyResponse#getErrorMessage()
     */
    @Override
    public native final String getErrorMessage() /*-{
       return this["ns1.createPolicyResponse"]["ms.errorMessage"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreatePolicyResponse#getPolicyId()
     */
    public native final String getPolicyIdAsString() /*-{
        return this["ns1.createPolicyResponse"]["ns1.policyId"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreatePolicyResponse#isErrored()
     */
    @Override
    public native final boolean isErrored() /*-{
        if (this["ns1.createPolicyResponse"]["ms.ack"] === "Success")
            return false;
        else
            return true;
    }-*/;

}
