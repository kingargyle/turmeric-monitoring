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


/**
 * The Class AbstractGenericView.
 */
public abstract class AbstractGenericView extends Composite implements Display {

	private String associatedId;

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.Widget#asWidget()
	 */
	public Widget asWidget() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.monitoring.client.Display#getAssociatedId()
	 */
	public String getAssociatedId() {
		return associatedId;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.monitoring.client.Display#setAssociatedId(java.lang.String)
	 */
	public void setAssociatedId(String id) {
		this.associatedId = id;
	}
	
	/**
	 * Initialize.
	 */
	public abstract void initialize();
}
