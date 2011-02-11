/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

/**
 * SubjectMatchTypeImpl
 *
 */
public class SubjectMatchTypeImpl implements SubjectMatchType {
    private String matchId;
    private AttributeValue attributeValue;
    private SubjectAttributeDesignator subjectAttributeDesignator;
    
	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	@Override
	public String getMatchId() {
		return matchId;
	}

	public void setAttributeValue(AttributeValue attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	@Override
	public AttributeValue getAttributeValue() {
		return attributeValue;
	}
	
	public void setSubjectAttributeDesignator(SubjectAttributeDesignator subjectAttributeDesignator) {
		this.subjectAttributeDesignator = subjectAttributeDesignator;
	}
	
	@Override
	public SubjectAttributeDesignator getSubjectAttributeDesignator() {
		return subjectAttributeDesignator;
	}
}
