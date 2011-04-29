/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client;

import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;

import com.google.gwt.user.client.ui.Widget;

/**
 * The Interface Display.
 */
public interface Display {

	/**
	 * Activate.
	 */
	void activate();
	
	/**
	 * As widget.
	 *
	 * @return the widget
	 */
	Widget asWidget();
	
	/**
	 * Sets the associated id.
	 *
	 * @param id the new associated id
	 */
	void setAssociatedId (String id);
	
	/**
	 * Gets the associated id.
	 *
	 * @return the associated id
	 */
	String getAssociatedId ();
}
