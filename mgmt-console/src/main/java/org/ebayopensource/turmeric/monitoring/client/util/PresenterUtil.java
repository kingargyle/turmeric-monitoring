/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.util;

import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.presenter.DashboardPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.Presenter;

import com.google.gwt.user.client.History;

/**
 * The Class PresenterUtil.
 */
public class PresenterUtil {
	
	private final static Map<UserAction, String> ACTION_PRESENTERID_MAPPER = new HashMap<UserAction, String>();
	static {
		// MAIN MENU
		ACTION_PRESENTERID_MAPPER.put(UserAction.CONSOLE_MAIN, DashboardPresenter.DASH_ID);
	}
	
	/**
	 * Force redirect to servicePresenter.
	 *
	 * @param token the token
	 * @param presenter the presenter
	 */
	public static void forceRedirectToPresenter(HistoryToken token, Presenter presenter) {
		if (token == null || !presenter.getId().equals(token.getPresenterId())) {
			History.newItem(HistoryToken.newHistoryToken(presenter.getId(), null).toString());
		}
		History.fireCurrentHistoryState();
	}
	
	/**
	 * Gets the servicePresenter id.
	 *
	 * @param action the action
	 * @return the servicePresenter id
	 */
	public static String getPresenterId(UserAction action) {
		return ACTION_PRESENTERID_MAPPER.get(action);
	}
	
	private PresenterUtil() {
	}

}
