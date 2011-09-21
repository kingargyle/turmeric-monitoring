/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.model;

/**
 * The Class Model.
 * 
 * @author jamuguerza
 */
public class BasicModel {

	/** The key. */
	private String key;

	/** The string data. */
	private String stringData;

	/**
	 * Gets the string data.
	 * 
	 * @return the string data
	 */
	public String getStringData() {
		return stringData;
	}

	/**
	 * Sets the string data.
	 * 
	 * @param stringData
	 *            the new string data
	 */
	public void setStringData(String stringData) {
		this.stringData = stringData;
	}

	/**
	 * Sets the key.
	 * 
	 * @param key
	 *            the new key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
}
