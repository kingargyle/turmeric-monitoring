/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DisablePolicyResponse;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * DisablePolicyResponseJS
 *
 */
public class DisablePolicyResponseJS extends JavaScriptObject implements
        DisablePolicyResponse {

    public static final String NAME = "ns1.disablePolicyResponse";
    
    protected DisablePolicyResponseJS () {}


    public static final native DisablePolicyResponse fromJSON (String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
        return null;
            }
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.BaseResponse#getErrorMessage()
     */
    public native final String getErrorMessage() /*-{
       return this["ns1.disablePolicyResponse"]["ms.errorMessage"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.BaseResponse#isErrored()
     */
    public native final boolean isErrored() /*-{
        if (this["ns1.disablePolicyResponse"]["ms.ack"] === "Success")
            return false;
        else
            return true;
    }-*/;

}
