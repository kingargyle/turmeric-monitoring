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

public class LoginFailureEvent extends GwtEvent<LoginFailureEventHandler> {

	public static Type<LoginFailureEventHandler> TYPE = new Type<LoginFailureEventHandler>();
	
	@Override
	protected void dispatch(LoginFailureEventHandler handler) {
		handler.onFailure();
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LoginFailureEventHandler> getAssociatedType() {
		return TYPE;
	}

}
