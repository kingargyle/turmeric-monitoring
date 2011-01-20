/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.common;

import org.ebayopensource.turmeric.monitoring.client.Display;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractGenericView extends Composite implements Display {

	private String associatedId;

	public Widget asWidget() {
		return this;
	}
	
	public String getAssociatedId() {
		return associatedId;
	}

	public void setAssociatedId(String id) {
		this.associatedId = id;
	}
	
	public abstract void initialize();
}
