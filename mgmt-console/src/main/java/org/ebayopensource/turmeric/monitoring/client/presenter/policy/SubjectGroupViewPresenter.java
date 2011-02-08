/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.presenter.policy;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.SupportedService;
import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupQuery;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectGroupsResponse;
import org.ebayopensource.turmeric.monitoring.client.presenter.AbstractGenericPresenter;
import org.ebayopensource.turmeric.monitoring.client.view.common.PolicyTemplateDisplay.PolicyPageTemplateDisplay;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * SubjectGroupViewPresenter
 * 
 */
public class SubjectGroupViewPresenter extends AbstractGenericPresenter {
	public static final String PRESENTER_ID = "SubjectGroupView";

	protected HandlerManager eventBus;
	protected SubjectGroupViewDisplay view;
	protected SubjectGroup group;
	protected Map<SupportedService, ConsoleService> serviceMap;

	public interface SubjectGroupViewDisplay extends PolicyPageTemplateDisplay {
		public void setName(String name);

		public String getName();

		public void setDescription(String desc);

		public String getDescription();

		public void setType(String type);

		public String getType();

		public HasClickHandlers getCancelButton();

		public void setSubjects(List<String> subjects);

		public void error(String msg);

		public void clear();
	}

	public SubjectGroupViewPresenter(HandlerManager eventBus,
			SubjectGroupViewDisplay view,
			Map<SupportedService, ConsoleService> serviceMap) {
		this.eventBus = eventBus;
		this.view = view;
		this.view.setAssociatedId(getId());
		this.serviceMap = serviceMap;
		bind();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.monitoring.client.presenter.
	 * AbstractGenericPresenter#getView()
	 */
	@Override
	protected Display getView() {
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ebayopensource.turmeric.monitoring.client.presenter.Presenter#getId()
	 */
	@Override
	public String getId() {
		return PRESENTER_ID;
	}

	public void bind() {
		this.view.getCancelButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				view.clear();
				// Just go back to the summary
				HistoryToken token = makeToken(PolicyController.PRESENTER_ID,
						SubjectGroupSummaryPresenter.PRESENTER_ID, null);
				History.newItem(token.toString(), true);
			}
		});
	}

	public void go(final HasWidgets container, final HistoryToken token) {
		// Get the id from the token
		final String name = token
				.getValue(HistoryToken.SELECTED_SUBJECT_GROUP_TOKEN);
		final String type = token
				.getValue(HistoryToken.SELECTED_SUBJECT_GROUP_TYPE_TOKEN);
		if (name != null) {
			container.clear();
			view.activate();
			container.add(view.asWidget());

			// Get the SubjectGroup being viewed
			final PolicyQueryService service = (PolicyQueryService) serviceMap
					.get(SupportedService.POLICY_QUERY_SERVICE);
			SubjectGroupQuery query = new SubjectGroupQuery();
			query.setIncludeSubjects(true);
			SubjectGroupKey key = new SubjectGroupKey();
			key.setName(name);
			key.setType(type);
			query.setGroupKeys(Collections.singletonList(key));

			service.findSubjectGroups(query,
					new AsyncCallback<FindSubjectGroupsResponse>() {

						public void onFailure(Throwable arg) {
							if (arg.getLocalizedMessage().contains("500")) {
								view.error(ConsoleUtil.messages
										.serverError(ConsoleUtil.policyAdminConstants
												.genericErrorMessage()));
							} else {
								view.error(ConsoleUtil.messages.serverError(arg
										.getLocalizedMessage()));
							}
						}

						public void onSuccess(FindSubjectGroupsResponse response) {
							if (response.getGroups() != null
									&& response.getGroups().size() > 0) {
								group = response.getGroups().get(0);
								view.setName(group.getName());
								view.setType(group.getType());
								view.setDescription(group.getDescription());
								view.setSubjects(group.getSubjects());
							}
						}
					});
		}
	}
}
