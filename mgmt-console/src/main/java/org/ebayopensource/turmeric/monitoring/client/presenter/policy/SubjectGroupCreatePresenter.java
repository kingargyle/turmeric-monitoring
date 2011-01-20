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
import org.ebayopensource.turmeric.monitoring.client.model.policy.Subject;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupImpl;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class SubjectGroupCreatePresenter extends AbstractGenericPresenter {
	
	public final static String PRESENTER_ID = "SubjectGroupCreate";
	
	protected HandlerManager eventBus;
	protected SubjectGroupCreateDisplay view;
	protected Map<SupportedService, ConsoleService> serviceMap;
	protected List<String> subjectTypes;

    protected PolicyQueryService service;
	
	public interface SubjectGroupCreateDisplay extends PolicyPageTemplateDisplay {
	    
	    public HasClickHandlers getCreateButton();
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
	
	public SubjectGroupCreatePresenter(HandlerManager eventBus, SubjectGroupCreateDisplay view, Map<SupportedService, ConsoleService> serviceMap) {
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
		        //a search term has been entered
		        SubjectGroupCreatePresenter.this.view.getSearchTerm();
		        //get the Subject Type
		        SubjectGroupCreatePresenter.this.view.getSubjectType();
		        
		        //do a lookup of all the matching Subjects
		        service = (PolicyQueryService) serviceMap.get(SupportedService.POLICY_QUERY_SERVICE);
		        SubjectKey key = new SubjectKey();
		        String name = SubjectGroupCreatePresenter.this.view.getSearchTerm();
		        if (name != null && !name.trim().equals(""))
		            key.setName(name);
		        key.setType(SubjectGroupCreatePresenter.this.view.getSubjectType());

		        SubjectQuery query = new SubjectQuery();
		        query.setSubjectKeys(Collections.singletonList(key));
		        service.findSubjects(query, new AsyncCallback<FindSubjectsResponse> () {

		            @Override
		            public void onFailure(Throwable arg0) {
		                SubjectGroupCreatePresenter.this.view.error(arg0.getMessage());
		            }

		            @Override
		            public void onSuccess(FindSubjectsResponse response) {
		                List<Subject> subjects = response.getSubjects();
		                List<String> names = new ArrayList<String>();
		                if (subjects != null) {
		                    for (Subject s:subjects)
		                        names.add(s.getName());
		                }
		                SubjectGroupCreatePresenter.this.view.setAvailableSubjects(names);
		            }

		        });
		    }
		});

		this.view.getCreateButton().addClickHandler(new ClickHandler() {
		    public void onClick(ClickEvent event) {
		        //Verify that the group has a name, and at least one subject
		        String name = SubjectGroupCreatePresenter.this.view.getName();
		        if (name == null || name.length()==0) {
		            SubjectGroupCreatePresenter.this.view.error(ConsoleUtil.policyAdminMessages.nameFieldMessage());
		            return;
		        }
		        
		        String description = SubjectGroupCreatePresenter.this.view.getDescription();
		        
		        List<String> subjects = SubjectGroupCreatePresenter.this.view.getSelectedSubjects();
		        if (subjects == null || subjects.isEmpty()) {
		            SubjectGroupCreatePresenter.this.view.error(ConsoleUtil.policyAdminMessages.minimumSubjectsMessage());
		            return;
		        }
		       

		        //user wants to create the Subject Group
		        //1. send the new Subject Group to the server
		        //2. when server acknowledges creation, use the history mechanism to move back to the Subject Group Summary
		        service = (PolicyQueryService) serviceMap.get(SupportedService.POLICY_QUERY_SERVICE);
		        SubjectGroupImpl group = new SubjectGroupImpl();
		        group.setName(name);
		        group.setDescription(description);

		        group.setType(SubjectGroupCreatePresenter.this.view.getSubjectType());  
		        group.setSubjects(subjects);
		        service.createSubjectGroups(Collections.singletonList((SubjectGroup)group), new AsyncCallback<CreateSubjectGroupsResponse>() {

                    @Override
                    public void onFailure(Throwable arg0) {
                        SubjectGroupCreatePresenter.this.view.error(arg0.getMessage());
                    }

                    @Override
                    public void onSuccess(CreateSubjectGroupsResponse arg0) {
                        Map<String,String> prefill = new HashMap<String,String>();
                        prefill.put(HistoryToken.SRCH_SUBJECT_GROUP_TYPE, SubjectGroupCreatePresenter.this.view.getSubjectType());
                        prefill.put(HistoryToken.SRCH_SUBJECT_GROUP_NAME, SubjectGroupCreatePresenter.this.view.getName());
                        HistoryToken token = makeToken(PolicyController.PRESENTER_ID,
                                                       SubjectGroupSummaryPresenter.PRESENTER_ID, 
                                                       prefill);
                        
                        History.newItem(token.toString(), true);
                    }
		            
		        });
		        

		    }
		});

		this.view.getCancelButton().addClickHandler(new ClickHandler() {
		    public void onClick(ClickEvent event) {
		        //Just go back to the summary
		        HistoryToken token = makeToken(PolicyController.PRESENTER_ID,SubjectGroupSummaryPresenter.PRESENTER_ID, null);
                History.newItem(token.toString(), true);
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
	
	
	private void fetchSubjectTypes () {
	        subjectTypes = org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectType.getValues();
	}
}
