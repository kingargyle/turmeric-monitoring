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
 * SubjectAttributeDesignatorImpl
 *
 */
public class SubjectAttributeDesignatorImpl implements SubjectAttributeDesignator {
 
	private String attributeId;
    private String dataType;

	public String getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}

	@Override
	public String getDataType() {
		return dataType;
	}
		   
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
