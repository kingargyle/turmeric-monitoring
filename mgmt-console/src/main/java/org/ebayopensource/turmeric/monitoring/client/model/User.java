/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

public class User {
	
	private static final User user = new User();
	private AuthenticationState authState;

	private User () {
		this.authState = AuthenticationState.Unknown;
	}
	
	public static User getInstance() {
		return user;
	}
	
	public boolean isAuthenticated () {
		return this.authState.equals(AuthenticationState.Authenticated);
	}
	
	public void setAuthenticationState (AuthenticationState authState) {
		this.authState = authState;
	}

}
