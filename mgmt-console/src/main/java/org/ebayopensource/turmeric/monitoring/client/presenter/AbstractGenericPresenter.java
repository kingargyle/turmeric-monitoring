/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.presenter;

import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;

import com.google.gwt.user.client.ui.HasWidgets;

public abstract class AbstractGenericPresenter implements Presenter {

	public void go(HasWidgets container, HistoryToken token) {
		container.clear();
		getView().activate();
		container.add(getView().asWidget());
	}
	
	abstract protected Display getView();
	
	
	public HistoryToken makeToken (String presenterId, String subPresenterId, Map<String, String> values) {
	    if (presenterId == null)
	        return null;
	    HistoryToken tok = HistoryToken.newHistoryToken(presenterId, values);
	    if (subPresenterId != null)
	        tok.addValue(HistoryToken.SUB, subPresenterId);
	    return tok;
	}
}
