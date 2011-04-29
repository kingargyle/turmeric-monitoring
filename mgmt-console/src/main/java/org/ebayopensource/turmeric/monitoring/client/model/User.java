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
 * The Class User.
 */
public class User {
	
	private static final User user = new User();
	private AuthenticationState authState;

	private User () {
		this.authState = AuthenticationState.Unknown;
	}
	
	/**
	 * Gets the single instance of User.
	 *
	 * @return single instance of User
	 */
	public static User getInstance() {
		return user;
	}
	
	/**
	 * Checks if is authenticated.
	 *
	 * @return true, if is authenticated
	 */
	public boolean isAuthenticated () {
		return this.authState.equals(AuthenticationState.Authenticated);
	}
	
	/**
	 * Sets the authentication state.
	 *
	 * @param authState the new authentication state
	 */
	public void setAuthenticationState (AuthenticationState authState) {
		this.authState = authState;
	}

}
