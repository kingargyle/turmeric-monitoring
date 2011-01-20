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

public interface Presenter {
	
	public void go(final HasWidgets container, HistoryToken token);
	public String getId();
	
	public interface TabPresenter extends Presenter {
	    public HistoryToken getStateAsHistoryToken();
	}

}
