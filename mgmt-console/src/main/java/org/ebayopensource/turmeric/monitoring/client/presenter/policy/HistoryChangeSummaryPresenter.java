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
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.SupportedService;
import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.policy.EntityHistory;
import org.ebayopensource.turmeric.monitoring.client.model.policy.OperationKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetEntityHistoryResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.QueryCondition;
import org.ebayopensource.turmeric.monitoring.client.model.policy.ResourceKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.ResourceType;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectType;
import org.ebayopensource.turmeric.monitoring.client.presenter.AbstractGenericPresenter;
import org.ebayopensource.turmeric.monitoring.client.util.PolicyKeysUtil;
import org.ebayopensource.turmeric.monitoring.client.view.common.PolicyTemplateDisplay.PolicyPageTemplateDisplay;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class HistoryChangeSummaryPresenter extends AbstractGenericPresenter {
	
	public final static String PRESENTER_ID = "HistoryChangeSummary";
	
	protected HandlerManager eventBus;
	protected HistoryChangeSummaryDisplay view;
	protected Map<SupportedService, ConsoleService> serviceMap;
	//TODO temporary
	protected List<EntityHistory> entities;
	protected PolicyQueryService service;
	
	public interface HistoryChangeSummaryDisplay extends PolicyPageTemplateDisplay {
	    void setEntities(List<EntityHistory> entities);
	    void setEntityTypes(List<String> entityTypes) ;
	    HasClickHandlers getSearchButton();
	    String getEntity();
	    long getFrom();
	    long getTo();
	    void error(String error);
	}
	
	
	
	public HistoryChangeSummaryPresenter(HandlerManager eventBus, HistoryChangeSummaryDisplay view, Map<SupportedService, ConsoleService> serviceMap) {
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
	protected PolicyPageTemplateDisplay getView() {
		return view;
	}
	
	public void bind() {		
		
		this.view.getSearchButton().addClickHandler(new ClickHandler() {
		    public void onClick(ClickEvent event) {
		    	fetchEntities(view.getEntity(), view.getFrom(), view.getTo());
		    }
		});

	}
	
	@Override
	public void go(HasWidgets container, final HistoryToken token) {
		container.clear();
		//TODO fetch from server
		this.view.setEntities(null);
		
		setEntityTypes();
		
		service = (PolicyQueryService) serviceMap
		.get(SupportedService.POLICY_QUERY_SERVICE);

		this.view.activate();
		container.add(this.view.asWidget());
	}
	
	private void setEntityTypes() {
		//TODO Improve this
		List<String> entitiesList = new ArrayList<String>();
		entitiesList.add("All");
		entitiesList.add("Resource");
		entitiesList.add("Operation");
		entitiesList.add("Subject");
		entitiesList.add("Subject Group");
		entitiesList.add("Authorization Policy");
		entitiesList.add("Blacklist Policy");
		entitiesList.add("Rate Limiting Policy");
		entitiesList.add("Whitelist Policy");
		
	    this.view.setEntityTypes(entitiesList);
	}
	
	
	private void fetchEntities(String entityType, long from, long to) {

	    if(from == 0){
	        from = System.currentTimeMillis() - 24*60*60*1000;
	    }
	    if(to == 0){
	        to = System.currentTimeMillis();
	    }
	    
	    List<PolicyKey> poKeys = null;
	    List<SubjectKey> sKeys = null;
	    List<SubjectGroupKey> sgKeys = null;
	    List<ResourceKey> rsKeys = null;
	    List<OperationKey> opKeys = null;

	    PolicyKey pkey=null;
	    SubjectKey sKey=null;
	    SubjectGroupKey sgKey=null;
	    ResourceKey rsKey=null;
	    
	    //TODO IMProve this
	    if("Authorization Policy".equals(entityType)){
	    	pkey = new PolicyKey();
	        pkey.setType("AUTHZ");
		    poKeys =  new ArrayList<PolicyKey>(Collections.singletonList(pkey));
	        QueryCondition condition = new QueryCondition();
	        condition.addQuery(new QueryCondition.Query(QueryCondition.ActivePoliciesOnlyValue.FALSE));
	    }else if("Blacklist Policy".equals(entityType)){
	    	pkey = new PolicyKey();
	        pkey.setType("BLACKLIST");
		    poKeys =  new ArrayList<PolicyKey>(Collections.singletonList(pkey));
	        QueryCondition condition = new QueryCondition();
	        condition.addQuery(new QueryCondition.Query(QueryCondition.ActivePoliciesOnlyValue.FALSE));
	    }else if("Whitelist Policy".equals(entityType)){
	    	pkey = new PolicyKey();
	        pkey.setType("WHITELIST");
	        poKeys =  new ArrayList<PolicyKey>(Collections.singletonList(pkey));
	        QueryCondition condition = new QueryCondition();
	        condition.addQuery(new QueryCondition.Query(QueryCondition.ActivePoliciesOnlyValue.FALSE));
	    }else if("Rate Limiting Policy".equals(entityType)){
	    	pkey = new PolicyKey();
	        pkey.setType("RL");
	        poKeys =  new ArrayList<PolicyKey>(Collections.singletonList(pkey));
	        QueryCondition condition = new QueryCondition();
	        condition.addQuery(new QueryCondition.Query(QueryCondition.ActivePoliciesOnlyValue.FALSE));
	    }else if("Subject".equals(entityType)){
	    	sKeys = new ArrayList<SubjectKey>(PolicyKeysUtil.getAllSubjectKeyList());
	    }else if("Subject Group".equals(entityType)){
	    	sgKeys = new ArrayList<SubjectGroupKey>(PolicyKeysUtil.getAllSubjectGroupKeyList());
	    }else if("Resource".equals(entityType)){
	    	rsKeys = new ArrayList<ResourceKey>(PolicyKeysUtil.getAllResourceKeyList());
	    }else if("Operation".equals(entityType)){
	    	opKeys = new ArrayList<OperationKey>(PolicyKeysUtil.getAllOperationKeyList());
	    }else if ("All".equals(entityType)){
	    	poKeys = new ArrayList<PolicyKey>(PolicyKeysUtil.getAllPolicyKeyList());
	    	rsKeys = new ArrayList<ResourceKey>(PolicyKeysUtil.getAllResourceKeyList());
	    	opKeys = new ArrayList<OperationKey>(PolicyKeysUtil.getAllOperationKeyList());
	    	sKeys = new ArrayList<SubjectKey>(PolicyKeysUtil.getAllSubjectKeyList());
	    	sgKeys = new ArrayList<SubjectGroupKey>(PolicyKeysUtil.getAllSubjectGroupKeyList());
	    }
	    
	    
	        service.getEntityHistory(from, to , poKeys, rsKeys, opKeys,sKeys, sgKeys, new AsyncCallback<GetEntityHistoryResponse>() {
	            public void onFailure(Throwable arg) {
	            	if(arg.getLocalizedMessage().contains("500")){
						view
						.error(ConsoleUtil.messages.serverError(
								ConsoleUtil.policyAdminConstants.genericErrorMessage()));
					}else{
						view
						.error(ConsoleUtil.messages.serverError(arg
								.getLocalizedMessage()));
					}
	                
	            }

	            public void onSuccess(GetEntityHistoryResponse result) {
	                List<EntityHistory> entities= new ArrayList<EntityHistory>(result.getEntities());  
	                if (entities != null && entities.size() >0) {    
	                        view.setEntities(entities);
	                } else
	                    view.setEntities(null);
	            }
	        });   
	    
	}
	
}
