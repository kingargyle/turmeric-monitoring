/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

/**
 * The Enum AuthenticationState.
 */
public enum AuthenticationState {
	
	/** The Unknown. */
	Unknown,
/** The Authenticated. */
Authenticated,
/** The Unauthenticated. */
Unauthenticated;
	
	/**
	 * Checks if is authenticated.
	 *
	 * @return true, if is authenticated
	 */
	public boolean isAuthenticated() {
		return this.equals(Authenticated);
	}
}
