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

import org.ebayopensource.turmeric.monitoring.client.model.policy.GetPoliciesResponseJS.RuleJS.ConditionJS;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * SubjectJS
 *
 */
public class SubjectJS extends JavaScriptObject implements Subject {

    protected SubjectJS() {}
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.Subject#getCreatedBy()
     */
    public native final String getLastModifiedBy() /*-{
        return this["@LastModifiedBy"];
    }-*/;
    
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.Subject#getCreatedBy()
     */
    public native final String getCreatedBy() /*-{
        return this["@CreatedBy"];
    }-*/;

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.Subject#getLastModifiedTime()
     */
    @Override
    public final long getLastModifiedTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.Subject#getName()
     */
    public native final String getName() /*-{
        return this["@SubjectName"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.Subject#getType()
     */
    public native final  String getType() /*-{
        return this["@SubjectType"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.Subject#getExternalSubjectId()
     */
	private native final String getExternalSubjectIdAsString() /*-{
		return this["@ExternalSubjectId"];
	 }-*/;

	/**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.Subject#getExternalSubjectId()
     */
	public final long getExternalSubjectId() {
		return Long.parseLong(getExternalSubjectIdAsString());
	}

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
	
	private final native JsArray<SubjectMatchTypeJS> getSubjectMatchAsArray() /*-{
		return this["ns2.SubjectMatch"]
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
	
}
