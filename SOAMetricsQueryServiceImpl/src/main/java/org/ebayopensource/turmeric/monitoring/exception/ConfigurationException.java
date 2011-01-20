/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.exception;

public class ConfigurationException extends Exception {
		
	private static final long serialVersionUID = 666062620705792993L;
	
	String errorMessage;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	public ConfigurationException(String errorMessage, Throwable th) {
		super(errorMessage, th);
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
