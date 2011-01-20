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
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectsResponse;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * FindSubjectsResponseJS
 *
 */
public class FindSubjectsResponseJS extends JavaScriptObject implements
        FindSubjectsResponse {
    
    public static final String NAME = "ns1.findSubjectsResponse";

    protected FindSubjectsResponseJS() {}
    
    public static final native FindSubjectsResponse fromJSON(String json) /*-{
    try {
        return eval('(' + json + ')');
    } catch (err) {
    return null;
        }
    }-*/;

    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectsResponse#getSubjects()
     */
    public final List<Subject> getSubjects() {
        List<Subject> results = new ArrayList<Subject>();
       JsArray<SubjectJS> subjects = getSubjectsArray();
       if (subjects != null) {
           for (int i=0;i<subjects.length();i++)
           results.add(subjects.get(i));
       }
       return results;
    }
    
    
    public final native JsArray<SubjectJS> getSubjectsArray() /*-{
        return this["ns1.findSubjectsResponse"]["ns1.subjects"];
    }-*/;
    

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectGroupsResponse#getErrorMessage()
     */
    public final native String getErrorMessage() /*-{
        return this["ns1.findSubjectsResponse"]["ms.errorMessage"];
    }-*/;


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectGroupsResponse#isErrored()
     */
    public final native boolean isErrored() /*-{
    if (this["ns1.findSubjectsResponse"]["ms.ack"] === "Success")
        return false;
    else
        return true;
}-*/;
}

