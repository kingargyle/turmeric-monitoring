/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.presenter;

import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * The Interface Presenter.
 */
public interface Presenter {
	
	/**
	 * Go.
	 *
	 * @param container the container
	 * @param token the token
	 */
	public void go(final HasWidgets container, HistoryToken token);
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId();
	
	/**
	 * The Interface TabPresenter.
	 */
	public interface TabPresenter extends Presenter {
	    
    	/**
    	 * Gets the state as history token.
    	 *
    	 * @return the state as history token
    	 */
    	public HistoryToken getStateAsHistoryToken();
	}

}
