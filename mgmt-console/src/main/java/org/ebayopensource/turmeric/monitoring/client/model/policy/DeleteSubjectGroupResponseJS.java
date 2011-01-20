/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * DeleteSubjectGroupResponse
 *
 */
public class DeleteSubjectGroupResponseJS extends JavaScriptObject
        implements
        org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeleteSubjectGroupResponse {
    
    public static final String NAME = "ns1.deleteSubjectGroupsResponse";

    protected DeleteSubjectGroupResponseJS() {}
    
    public static final native DeleteSubjectGroupResponseJS fromJSON (String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
        return null;
            }
    }-*/;
    
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeleteSubjectGroupResponse#getErrorMessage()
     */
    public final native String getErrorMessage() /*-{
        return this["ns1.deleteSubjectGroupsResponse"]["ms.errorMessage"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeleteSubjectGroupResponse#isErrored()
     */
    public final native boolean isErrored() /*-{
        if (this["ns1.deleteSubjectGroupsResponse"]["ms.ack"] === "Success")
            return false;
        else
            return true;
        return false;
    }-*/;

}
