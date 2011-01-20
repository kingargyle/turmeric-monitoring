/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.event;

import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.GwtEvent;

public class GetServicesEvent extends GwtEvent<GetServicesEventHandler> {
	
	public static Type<GetServicesEventHandler> TYPE = new Type<GetServicesEventHandler>();
	Map<String, Set<String>> data;
	
	public GetServicesEvent (Map<String, Set<String>> data) {
		this.data = data;
	}
	
	public Map<String, Set<String>> getData() {
		return this.data;
	}
	
	@Override
	protected void dispatch(GetServicesEventHandler handler) {
		handler.onData(this);
	}

	@Override
	public Type<GetServicesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
