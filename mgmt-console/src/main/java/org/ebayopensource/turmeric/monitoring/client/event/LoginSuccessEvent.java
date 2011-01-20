/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.event;

import org.ebayopensource.turmeric.monitoring.client.shared.AppUser;

import com.google.gwt.event.shared.GwtEvent;

public class LoginSuccessEvent extends GwtEvent<LoginSuccessEventHandler> {
	public static Type<LoginSuccessEventHandler> TYPE = new Type<LoginSuccessEventHandler>();
	
	private AppUser appUser;
	
	public LoginSuccessEvent(AppUser appUser) {
		this.appUser = appUser;
	}
	
	public static Type<LoginSuccessEventHandler> getTYPE() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginSuccessEventHandler handler) {
		handler.onSuccess(appUser);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LoginSuccessEventHandler> getAssociatedType() {
		return TYPE;
	}

}
