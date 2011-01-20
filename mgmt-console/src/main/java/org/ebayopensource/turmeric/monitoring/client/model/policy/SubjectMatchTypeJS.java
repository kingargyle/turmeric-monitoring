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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * SubjectMatchTypeJS
 *
 */
public class SubjectMatchTypeJS extends JavaScriptObject implements SubjectMatchType {

    protected SubjectMatchTypeJS() {}
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectMatchType#getMatchId()
     */
    @Override
    public native final String getMatchId() /*-{
        return this["@MatchId"];
    }-*/;

	
	@Override
    public final String getAttributeValue() {
        String result = new String();
        AttributeValueJS attributeValue = getAttributeValueAsObject();
        if (attributeValue != null) {
            result = attributeValue.getValue() ;
        }
        return result;
    }
    
    public final native AttributeValueJS getAttributeValueAsObject() /*-{
        return this["ns2.AttributeValue"];
	}-*/;
  
    @Override
    public final String getSubjectAttributeDesignator() {
        String result = new String();
        SubjectAttributeDesignatorJS subjectAttributeDesignator = getSubjectAttributeDesignatorAsObject();
        if (subjectAttributeDesignator != null) {
            result = subjectAttributeDesignator.getAttributeId() ;
        }
        return result;
    }
    
    public final native SubjectAttributeDesignatorJS getSubjectAttributeDesignatorAsObject() /*-{
        return this["ns2.SubjectAttributeDesignator"];
	}-*/;

}
