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

import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectGroupsResponse;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * FindSubjectGroupsReponseJS
 *
 */
public class FindSubjectGroupsResponseJS extends JavaScriptObject implements
        FindSubjectGroupsResponse {
    public final static String NAME = "ns1.findSubjectGroupsResponse";
    
    protected FindSubjectGroupsResponseJS() {}
    
    
    public static final native FindSubjectGroupsResponse fromJSON(String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
        return null;
            }
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectGroupsResponse#getErrorMessage()
     */
    public final native String getErrorMessage() /*-{
        return this["ns1.findSubjectGroupsResponse"]["ms.errorMessage"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectGroupsResponse#getGroups()
     */
    public final native JsArray<SubjectGroupJS> getGroupsAsArray() /*-{
        return this["ns1.findSubjectGroupsResponse"]["ns1.subjectGroups"];
    }-*/;

    
    public final List<SubjectGroup> getGroups() {
        List<SubjectGroup> results = new ArrayList<SubjectGroup>();
        JsArray<SubjectGroupJS> groups = getGroupsAsArray();
        if (groups != null) {
            for (int i=0;i<groups.length();i++)
                results.add(groups.get(i));
        }
        return results;
    }
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectGroupsResponse#isErrored()
     */
    public final native boolean isErrored() /*-{
        if (this["ns1.findSubjectGroupsResponse"]["ms.ack"] === "Success")
            return false;
        else
            return true;
    }-*/;

}
