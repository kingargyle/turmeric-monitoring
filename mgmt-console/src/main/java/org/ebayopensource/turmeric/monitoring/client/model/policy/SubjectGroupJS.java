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
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;


import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;


/**
 * SubjectGroupJS
 *
 */
public class SubjectGroupJS extends JavaScriptObject implements SubjectGroup {

    protected SubjectGroupJS () {}
    
   
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup#getCreatedBy()
     */
    public native final String getCreatedBy() /*-{
        return this["@CreatedBy"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup#getDescription()
     */
    public native final String getDescription() /*-{
        return this["@Description"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup#getLastModifiedBy()
     */
    public native final String getLastModifiedBy() /*-{
        return this["@LastModifiedBy"];
    }-*/;
    

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup#getLastModifiedTime()
     */
    @Override
    public final Date getLastModifiedTime() {

        String tmp = getLastModifiedAsString();
        if (tmp == null)
            return null;
        try {
            return ConsoleUtil.xsDateTimeFormat.parse(tmp);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    
    public native final String getLastModifiedAsString() /*-{
        return this["@LastUpdatedDate"];
    }-*/;
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup#getName()
     */
    public native final String getName() /*-{
        return this["@SubjectGroupName"];
    }-*/;

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup#getPolicies()
     */
    @Override
    public final List<String> getPolicies() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup#getSubjects()
     */
    public final List<String> getSubjects() {
        List<String> strings = new ArrayList<String>();
        JsArray<SubjectJS> subjects = getSubjectsAsObjects();
        if (subjects != null) {
            for (int i=0; i< subjects.length(); i++)
                strings.add(subjects.get(i).getName());
        }
        return strings;
    }
    
    private native final JsArray<SubjectJS> getSubjectsAsObjects () /*-{
        return this["ns1.Subject"];
    }-*/;

    @Override
    public final List<SubjectMatchType> getSubjectMatchTypes() {
        List<SubjectMatchType> results = new ArrayList<SubjectMatchType>();
        JsArray<SubjectMatchTypeJS> subjectMatchs = getSubjectMatchAsArray();
        if (subjectMatchs != null) {
            for (int i=0;i<subjectMatchs.length();i++)
                results.add(subjectMatchs.get(i));
        }
        return results;
    }
    
    public final native JsArray<SubjectMatchTypeJS> getSubjectMatchAsArray() /*-{
    	return this["ns2.SubjectMatch"]
	}-*/;


    public native final String getType() /*-{
        return this["@SubjectType"];
    }-*/;

    
    public final String getIdFromSubjectMatchAsString () {
    
        JsArray<SubjectMatchTypeJS> array =  getSubjectMatchAsArray();
        if (array == null)
            return null;
        
        if (array.length() == 0)
            return null;
        
        SubjectMatchTypeJS element = array.get(0);
        SubjectAttributeDesignatorJS des = element.getSubjectAttributeDesignatorAsObject();
        String attId = des.getAttributeId();
        if ("urn:oasis:names:tc:xacml:1.0:subject:subject-id".equals(attId)) {
            AttributeValueJS attVal = element.getAttributeValueAsObject();
            return attVal.getValue();
        } else
            return null;
    }
    
    @Override
    public final Long getId() {
        String s = getIdFromSubjectMatchAsString();
       
        if (s == null)
            return null;
        
        return Long.valueOf(s);
    }

    
}
