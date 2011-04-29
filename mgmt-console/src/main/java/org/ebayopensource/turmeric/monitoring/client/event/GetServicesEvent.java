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

/**
 * The Class GetServicesEvent.
 */
public class GetServicesEvent extends GwtEvent<GetServicesEventHandler> {
	
	/** The TYPE. */
	public static Type<GetServicesEventHandler> TYPE = new Type<GetServicesEventHandler>();
	
	/** The data. */
	Map<String, Set<String>> data;
	
	/**
	 * Instantiates a new gets the services event.
	 *
	 * @param data the data
	 */
	public GetServicesEvent (Map<String, Set<String>> data) {
		this.data = data;
	}
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Map<String, Set<String>> getData() {
		return this.data;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(GetServicesEventHandler handler) {
		handler.onData(this);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public Type<GetServicesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
