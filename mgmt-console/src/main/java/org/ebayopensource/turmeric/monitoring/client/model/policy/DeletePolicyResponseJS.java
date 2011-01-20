/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeletePolicyResponse;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * DeletePolicyResponseJS
 *
 */
public class DeletePolicyResponseJS extends JavaScriptObject implements
        DeletePolicyResponse {

    public static final String NAME = "ns1.deletePolicyResponse";
    
    protected DeletePolicyResponseJS () {}
    
    public static final native DeletePolicyResponse fromJSON (String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
        return null;
            }
    }-*/;
    
    public final Boolean isSuccess () {
        return Boolean.valueOf(getResultAsString());
    }
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeletePolicyResponse#getErrorMessage()
     */
    @Override
    public native final String getErrorMessage() /*-{
       return this["ns1.deletePolicyResponse"]["ms.errorMessage"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeletePolicyResponse#isSuccess()
     */
    public native final String getResultAsString() /*-{
        return this["ns1.deletePolicyResponse"]["ns1.success"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeletePolicyResponse#isErrored()
     */
    @Override
    public native final boolean isErrored() /*-{
        if (this["ns1.deletePolicyResponse"]["ms.ack"] === "Success")
            return false;
        else
            return true;
    }-*/;

}
