/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.model;

public class Error<K> extends Model<K> {
	private String category;
	private String name;
	private String domain;
	private String subDomain;
	private String severity;
	private Long errorId;
	private String organization;
	
	
	public String getCategory() {
		return category;
	}


	public String getName() {
		return name;
	}


	public String getDomain() {
		return domain;
	}


	public String getSubDomain() {
		return subDomain;
	}


	public String getSeverity() {
		return severity;
	}


	public Long getErrorId() {
		return errorId;
	}


	public String getOrganization() {
		return organization;
	}


	public Error(K keyType){ 
		super(keyType);
	}

}
