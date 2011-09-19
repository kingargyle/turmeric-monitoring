/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.model;

import java.util.Map;

/**
 *  column family XXXXXX (SupercolumnFamily){
 *	key : String
 *	columns {
 *		supercolumnName = String
 *		superColumnValue = list of Strings
 *	}
 *
 *       ex: key1: [service1: op1, op2][service2: op2, op3]
 *  
 * The Class RateLimiterPolicyModel.
 * @author jamuguerza
 */
public class SuperModel  {

	/** The key data. */
	private String key;
	
	private Map<String, BasicModel> columns ;

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setColumns(Map<String, BasicModel> columns) {
		this.columns = columns;
	}

	public Map<String, BasicModel> getColumns() {
		return columns;
	}
		
}
