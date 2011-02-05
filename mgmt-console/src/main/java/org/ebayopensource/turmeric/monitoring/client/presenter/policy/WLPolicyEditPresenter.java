/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.presenter.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.SupportedService;
import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.ResourceLevel;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdateMode;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdatePolicyResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class WLPolicyEditPresenter extends PolicyEditPresenter {

	public WLPolicyEditPresenter(HandlerManager eventBus,
			PolicyEditDisplay view,
			Map<SupportedService, ConsoleService> serviceMap) {
		super(eventBus, view, serviceMap);
		view.setConditionBuilderVisible(false);
	}

	public final static String PRESENTER_ID = "WLPolicyEdit";

	@Override
	public String getId() {
		return PRESENTER_ID;
	}

	@Override
	public List<String> getResourceLevels() {
		List<String> rsLevels = new ArrayList<String>();

		for (ResourceLevel rsLevel : ResourceLevel.values()) {
			rsLevels.add(rsLevel.name());
		}

		return rsLevels;
	}

	@Override
	protected void bindSaveButton() {
		{
			// fired on saved policy
			this.view.getSaveButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					GWT.log("EDITION MODE:");

					final GenericPolicy p = getPolicy(view.getPolicyName()
							.getValue(), originalPolicyType, view
							.getPolicyDesc().getValue(), resourceAssignments,
							subjectAssignments, view.getPolicyEnabled(), Long
									.valueOf(originalPolicyId), null);

					GWT.log("Updating policy: " + p.getId() + "-" + p.getName());
					/**
					 * This timer is needed due to GWT has only one thread, so
					 * Thread.sleep is not a valid option The purpose of
					 * sleeping time is wait until new external subject been
					 * created into turmeric db, in order to assign them as
					 * internal subjects
					 */
					Timer timer = new Timer() {
						public void run() {
							service.updatePolicy(UpdateMode.REPLACE, p,
									new AsyncCallback<UpdatePolicyResponse>() {

										public void onFailure(Throwable err) {
											view.getResourceContentView()
													.error(ConsoleUtil.messages.serverError(err
															.getLocalizedMessage()));
										}

										public void onSuccess(
												UpdatePolicyResponse response) {
											GWT.log("Updated policy");
											WLPolicyEditPresenter.this.view
													.clear();
											clearLists();
											HistoryToken token = makeToken(
													PolicyController.PRESENTER_ID,
													PolicySummaryPresenter.PRESENTER_ID,
													null);

											// Prefill the summary search with
											// the policy we just modified
											token.addValue(
													HistoryToken.SRCH_POLICY_TYPE,
													originalPolicyType);
											token.addValue(
													HistoryToken.SRCH_POLICY_NAME,
													p.getName());
											History.newItem(token.toString(),
													true);
										}
									});

							view.getSaveButton().setEnabled(true);
						}

					};
					if (view.getSubjectContentView().getAssignments().size() > 0
							&& "USER".equals(view.getSubjectContentView()
									.getAssignments().get(0).getSubjectType())) {
						view.getSaveButton().setEnabled(false);
						timer.schedule(3000);
					} else {
						timer.schedule(1);
					}
				}
			});
		}
	}

}
