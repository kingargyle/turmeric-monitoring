/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdatePolicyResponse;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * UpdatePolicyResponseJS
 *
 */
public class UpdatePolicyResponseJS extends JavaScriptObject implements
        UpdatePolicyResponse {
    public static final String NAME = "ns1.updatePolicyResponse";
    
    public static final native UpdatePolicyResponseJS fromJSON(String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
        return null;
        }
    }-*/;
    
    protected UpdatePolicyResponseJS() {}
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdatePolicyResponse#getErrorMessage()
     */
    @Override
    public final native String getErrorMessage() /*-{
      return this["ns1.updatePolicyResponse"]["ms.errorMessage"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdatePolicyResponse#isErrored()
     */
    @Override
    public final native boolean isErrored() /*-{
         if (this["ns1.updatePolicyResponse"]["ms.ack"] === "Success")
            return false;
        else
            return true;
    }-*/;
}
