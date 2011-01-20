/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LoginEvent extends GwtEvent<LoginEventHandler> {
	public static Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();
	private String login;
	private String password;
	private String domain;
	
	
	
	public LoginEvent (String login, String password, String domain) {
		this.login = login;
		this.password = password;
		this.domain = domain;
	}
	
	
	public static Type<LoginEventHandler> getTYPE() {
		return TYPE;
	}


	public String getLogin() {
		return login;
	}



	public String getPassword() {
		return password;
	}
	
	public String getDomain() {
	    return domain;
	}


	@Override
	public Type<LoginEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginEventHandler handler) {
		handler.onLogin(this);
	}
}