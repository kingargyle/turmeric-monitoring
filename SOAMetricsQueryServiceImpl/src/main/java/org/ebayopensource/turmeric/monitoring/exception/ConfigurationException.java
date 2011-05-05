/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.exception;

/**
 * The Class ConfigurationException.
 */
public class ConfigurationException extends Exception {
		
	private static final long serialVersionUID = 666062620705792993L;
	
	/** The error message. */
	String errorMessage;

	/**
	 * Instantiates a new configuration exception.
	 */
	public ConfigurationException() {
		super();
	}

	/**
	 * Instantiates a new configuration exception.
	 *
	 * @param errorMessage the error message
	 */
	public ConfigurationException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	/**
	 * Instantiates a new configuration exception.
	 *
	 * @param errorMessage the error message
	 * @param th the th
	 */
	public ConfigurationException(String errorMessage, Throwable th) {
		super(errorMessage, th);
		this.errorMessage = errorMessage;
	}

	/**
	 * Gets the error message.
	 *
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets the error message.
	 *
	 * @param errorMessage the new error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
