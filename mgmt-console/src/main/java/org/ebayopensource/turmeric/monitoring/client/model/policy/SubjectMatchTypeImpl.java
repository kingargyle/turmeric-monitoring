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
    private String attributeValue;
    private String subjectAttributeDesignator;
    
	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	@Override
	public String getMatchId() {
		return matchId;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	@Override
	public String getAttributeValue() {
		return attributeValue;
	}
	
	public void setSubjectAttributeDesignator(String subjectAttributeDesignator) {
		this.subjectAttributeDesignator = subjectAttributeDesignator;
	}
	
	@Override
	public String getSubjectAttributeDesignator() {
		return subjectAttributeDesignator;
	}
}
