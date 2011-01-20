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
import org.ebayopensource.turmeric.monitoring.client.presenter.SplashPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.AuthzPolicyCreatePresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.BLPolicyCreatePresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyCreatePresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.HistoryChangeSummaryPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyController;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyImportPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicySummaryPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.RLPolicyCreatePresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupImportPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.WLPolicyCreatePresenter;

import com.google.gwt.user.client.History;

public class PresenterUtil {
	
	private final static Map<UserAction, String> ACTION_PRESENTERID_MAPPER = new HashMap<UserAction, String>();
	static {
		// MAIN MENU
		ACTION_PRESENTERID_MAPPER.put(UserAction.CONSOLE_MAIN, DashboardPresenter.DASH_ID);
		ACTION_PRESENTERID_MAPPER.put(UserAction.POLICY_MAIN, PolicyController.PRESENTER_ID);
		
		// POLICY MENU
		ACTION_PRESENTERID_MAPPER.put(UserAction.SUBJECT_GROUP_SUMMARY, SubjectGroupSummaryPresenter.PRESENTER_ID);
		ACTION_PRESENTERID_MAPPER.put(UserAction.SUBJECT_GROUP_CREATE, SubjectGroupCreatePresenter.PRESENTER_ID);
		ACTION_PRESENTERID_MAPPER.put(UserAction.SUBJECT_GROUP_IMPORT, SubjectGroupImportPresenter.PRESENTER_ID);
		ACTION_PRESENTERID_MAPPER.put(UserAction.POLICY_SUMMARY, PolicySummaryPresenter.PRESENTER_ID);
		ACTION_PRESENTERID_MAPPER.put(UserAction.POLICY_IMPORT, PolicyImportPresenter.PRESENTER_ID);
		ACTION_PRESENTERID_MAPPER.put(UserAction.AUTHZ_POLICY_CREATE, AuthzPolicyCreatePresenter.PRESENTER_ID);
		ACTION_PRESENTERID_MAPPER.put(UserAction.RL_POLICY_CREATE, RLPolicyCreatePresenter.PRESENTER_ID);
		ACTION_PRESENTERID_MAPPER.put(UserAction.BL_POLICY_CREATE, BLPolicyCreatePresenter.PRESENTER_ID);
		ACTION_PRESENTERID_MAPPER.put(UserAction.WL_POLICY_CREATE, WLPolicyCreatePresenter.PRESENTER_ID);
		ACTION_PRESENTERID_MAPPER.put(UserAction.CHANGE_HISTORY_SUMMARY, HistoryChangeSummaryPresenter.PRESENTER_ID);
		
	}
	
	public static void forceRedirectToPresenter(HistoryToken token, Presenter presenter) {
		if (token == null || !presenter.getId().equals(token.getPresenterId())) {
			History.newItem(HistoryToken.newHistoryToken(presenter.getId(), null).toString());
		}
		History.fireCurrentHistoryState();
	}
	
	public static void forceRedirectToSplashPresenter(HistoryToken token) {
		if (token == null || !SplashPresenter.SPLASH_ID.equals(token.getPresenterId())) {
			History.newItem(HistoryToken.newHistoryToken(SplashPresenter.SPLASH_ID, null).toString());
		}
		History.fireCurrentHistoryState();
	}
	
	public static String getPresenterId(UserAction action) {
		return ACTION_PRESENTERID_MAPPER.get(action);
	}
	
	private PresenterUtil() {
	}

}
