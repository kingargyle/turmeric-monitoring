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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.SupportedService;
import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreateSubjectsResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindExternalSubjectsResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.Subject;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupImpl;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectImpl;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectQuery;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreateSubjectGroupsResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectsResponse;
import org.ebayopensource.turmeric.monitoring.client.presenter.AbstractGenericPresenter;
import org.ebayopensource.turmeric.monitoring.client.view.common.PolicyTemplateDisplay.PolicyPageTemplateDisplay;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;

public class SubjectGroupCreatePresenter extends AbstractGenericPresenter {

	public final static String PRESENTER_ID = "SubjectGroupCreate";

	protected HandlerManager eventBus;
	protected SubjectGroupCreateDisplay view;
	protected Map<SupportedService, ConsoleService> serviceMap;
	protected List<String> subjectTypes;
	private List<Long> createdSubjectIds;

	protected PolicyQueryService service;

	public interface SubjectGroupCreateDisplay extends
			PolicyPageTemplateDisplay {

		public Button getCreateButton();

		public HasClickHandlers getCancelButton();

		public HasClickHandlers getSearchButton();

		public List<String> getSelectedSubjects();

		public String getSubjectType();

		public String getSearchTerm();

		public String getDescription();

		public String getName();

		public void setAvailableSubjects(List<String> subjects);

		public void setSubjectTypes(List<String> subjectTypes);

		public void error(String msg);

	}

	public SubjectGroupCreatePresenter(HandlerManager eventBus,
			SubjectGroupCreateDisplay view,
			Map<SupportedService, ConsoleService> serviceMap) {
		this.eventBus = eventBus;
		this.view = view;
		this.view.setAssociatedId(getId());
		this.serviceMap = serviceMap;

		bind();
	}

	public String getId() {
		return PRESENTER_ID;
	}

	@Override
	protected Display getView() {
		return view;
	}

	public void bind() {
		this.view.getSearchButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// a search term has been entered
				SubjectGroupCreatePresenter.this.view.getSearchTerm();
				// get the Subject Type
				SubjectGroupCreatePresenter.this.view.getSubjectType();

				// do a lookup of all the matching Subjects
				service = (PolicyQueryService) serviceMap
						.get(SupportedService.POLICY_QUERY_SERVICE);
				SubjectKey key = new SubjectKey();
				String name = SubjectGroupCreatePresenter.this.view
						.getSearchTerm();
				if (name != null && !name.trim().equals(""))
					key.setName(name);
				key.setType(SubjectGroupCreatePresenter.this.view
						.getSubjectType());

				SubjectQuery query = new SubjectQuery();
				query.setSubjectKeys(Collections.singletonList(key));

				if ("USER".equals(key.getType())) {
					service.findExternalSubjects(query,
							new AsyncCallback<FindExternalSubjectsResponse>() {

								public void onFailure(Throwable arg0) {
									view.error(arg0.getLocalizedMessage());
								}

								public void onSuccess(
										FindExternalSubjectsResponse response) {
									List<Subject> subjects = response
											.getSubjects();
									List<String> names = new ArrayList<String>();
									if (subjects != null) {
										for (Subject s : subjects)
											names.add(s.getName());
									}
									view.setAvailableSubjects(names);

								}

							});

				} else {
					service.findSubjects(query,
							new AsyncCallback<FindSubjectsResponse>() {

								@Override
								public void onFailure(Throwable arg0) {
									SubjectGroupCreatePresenter.this.view
											.error(arg0.getMessage());
								}

								@Override
								public void onSuccess(
										FindSubjectsResponse response) {
									List<Subject> subjects = response
											.getSubjects();
									List<String> names = new ArrayList<String>();
									if (subjects != null) {
										for (Subject s : subjects)
											names.add(s.getName());
									}
									view.setAvailableSubjects(names);
								}

							});
				}
			}
		});

		this.view.getCreateButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Verify that the group has a name, and at least one subject
				String name = SubjectGroupCreatePresenter.this.view.getName();
				if (name == null || name.length() == 0) {
					SubjectGroupCreatePresenter.this.view
							.error(ConsoleUtil.policyAdminMessages
									.nameFieldMessage());
					return;
				}

				String description = SubjectGroupCreatePresenter.this.view
						.getDescription();

				List<String> subjectNames = SubjectGroupCreatePresenter.this.view
						.getSelectedSubjects();
				if (subjectNames == null || subjectNames.isEmpty()) {
					SubjectGroupCreatePresenter.this.view
							.error(ConsoleUtil.policyAdminMessages
									.minimumSubjectsMessage());
					return;
				}

				if("USER".equals(view.getSubjectType())){				
					 //external subjects todays are only USER types
	    		     List<Subject> subjects = new ArrayList<Subject>();
					for (String sbName : subjectNames) {
						SubjectImpl subject = new SubjectImpl();
						subject.setType("USER");
						subject.setName(sbName);
						subjects.add(subject);
					}
					createInternalSubject(subjects);
				}

				// user wants to create the Subject Group
				// 1. send the new Subject Group to the server
				// 2. when server acknowledges creation, use the history
				// mechanism to move back to the Subject Group Summary
				service = (PolicyQueryService) serviceMap
						.get(SupportedService.POLICY_QUERY_SERVICE);
				final SubjectGroupImpl group = new SubjectGroupImpl();
				group.setName(name);
				group.setDescription(description);

				group.setType(SubjectGroupCreatePresenter.this.view
						.getSubjectType());
				group.setSubjects(subjectNames);

				/**
				 * This timer is needed due to GWT has only one thread, so
				 * Thread.sleep is not a valid option The purpose of sleeping
				 * time is wait until new external subject been created into
				 * turmeric db, in order to assign them as internal subjects
				 */
				Timer timer = new Timer() {
					public void run() {
						

						service.createSubjectGroups(
								Collections.singletonList((SubjectGroup) group),
								new AsyncCallback<CreateSubjectGroupsResponse>() {

									@Override
									public void onFailure(Throwable arg0) {
										SubjectGroupCreatePresenter.this.view
												.error(arg0.getMessage());
									}

									@Override
									public void onSuccess(
											CreateSubjectGroupsResponse arg0) {
										Map<String, String> prefill = new HashMap<String, String>();
										prefill.put(
												HistoryToken.SRCH_SUBJECT_GROUP_TYPE,
												SubjectGroupCreatePresenter.this.view
														.getSubjectType());
										prefill.put(
												HistoryToken.SRCH_SUBJECT_GROUP_NAME,
												SubjectGroupCreatePresenter.this.view
														.getName());
										HistoryToken token = makeToken(
												PolicyController.PRESENTER_ID,
												SubjectGroupSummaryPresenter.PRESENTER_ID,
												prefill);

										History.newItem(token.toString(), true);
									}

								});
						
						view.getCreateButton().setEnabled(true);

					}

				};
				if("USER".equals(view.getSubjectType())){
					view.getCreateButton().setEnabled(false);
					timer.schedule(3000);
				}else{
		        	timer.schedule(1);
		        }
			}
		});

		this.view.getCancelButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Just go back to the summary
				HistoryToken token = makeToken(PolicyController.PRESENTER_ID,
						SubjectGroupSummaryPresenter.PRESENTER_ID, null);
				History.newItem(token.toString(), true);
			}
		});
	}

	private void createInternalSubject(final List<Subject> subjects) {

		List<SubjectKey> keys = new ArrayList<SubjectKey>();
		for (Subject subj : subjects) {
			SubjectKey key = new SubjectKey();
			key.setName(subj.getName());
			// today external subject supported are USER types
			key.setType("USER");
			keys.add(key);
		}

		final SubjectQuery query = new SubjectQuery();
		query.setSubjectKeys(keys);
		service.findSubjects(query,
				new AsyncCallback<PolicyQueryService.FindSubjectsResponse>() {

					public void onSuccess(FindSubjectsResponse result) {
						subjects.removeAll(result.getSubjects());
						if(subjects.size()>0){
							service.createSubjects(
									subjects,
									new AsyncCallback<PolicyQueryService.CreateSubjectsResponse>() {
	
										public void onSuccess(
												final CreateSubjectsResponse result) {
											// do nothing, subjects has been stored,
											// we can continue...
										}
	
										public void onFailure(final Throwable caught) {
											view.error(caught.getLocalizedMessage());
										}
									});
						}
					}

					public void onFailure(Throwable caught) {
						view.error(caught.getLocalizedMessage());
					}

				});

	}

	@Override
	public void go(HasWidgets container, final HistoryToken token) {
		fetchSubjectTypes();
		this.view.setSubjectTypes(subjectTypes);
		container.clear();
		this.view.activate();
		container.add(this.view.asWidget());
	}

	private void fetchSubjectTypes() {
		subjectTypes = org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectType
				.getValues();
	}
}
