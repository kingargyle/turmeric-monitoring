/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * The Interface GetServicesEventHandler.
 */
public interface GetServicesEventHandler extends EventHandler {
	
	/**
	 * On data.
	 *
	 * @param event the event
	 */
	public void onData (GetServicesEvent event);
}
