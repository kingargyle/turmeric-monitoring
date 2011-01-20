/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.presenter.policy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.Controller;
import org.ebayopensource.turmeric.monitoring.client.PolicyDashboard;
import org.ebayopensource.turmeric.monitoring.client.SupportedService;
import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyType;
import org.ebayopensource.turmeric.monitoring.client.model.policy.ResourceType;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectType;
import org.ebayopensource.turmeric.monitoring.client.presenter.Presenter;
import org.ebayopensource.turmeric.monitoring.client.shared.AppUserRole;
import org.ebayopensource.turmeric.monitoring.client.util.PolicyMenuUtil;
import org.ebayopensource.turmeric.monitoring.client.util.PresenterUtil;
import org.ebayopensource.turmeric.monitoring.client.view.policy.AUTHZPolicyEditView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.AuthzPolicyCreateView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.BLPolicyCreateView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.BLPolicyEditView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.HistoryChangeSummaryView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.PolicyImportView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.PolicySummaryView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.PolicyViewView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.RLPolicyCreateView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.RLPolicyEditView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.SubjectGroupCreateView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.SubjectGroupEditView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.SubjectGroupImportView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.SubjectGroupSummaryView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.SubjectGroupViewView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.WLPolicyCreateView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.WLPolicyEditView;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TreeItem;


public class PolicyController implements Presenter, Controller {

	public final static String PRESENTER_ID = "PolicyMain";

	
	protected HandlerManager eventBus;
	protected PolicyDashboard view;
	protected Map<String, Presenter> presenters = new HashMap<String, Presenter>();
	protected Map<UserAction, Presenter> actionsMap = new HashMap<UserAction, Presenter>();
	protected Map<Presenter, UserAction> presentersMap = new HashMap<Presenter, UserAction>();
	protected Map<SupportedService, ConsoleService> serviceMap;
	protected boolean added;
	protected boolean metaInit = false;
	protected HasWidgets container;
	
	
	public PolicyController (HandlerManager eventBus,  PolicyDashboard view, Map<SupportedService, ConsoleService> serviceMap) {
		this.eventBus = eventBus;
		this.view = view;
		this.serviceMap = serviceMap;
		
		initPresenters();
		bind();
	}
	
	private void initMetaData() {
	    PolicyQueryService policyService = (PolicyQueryService)serviceMap.get(SupportedService.POLICY_QUERY_SERVICE);
	    SubjectType.init(policyService, new AsyncCallback<List<String>>() {

            public void onFailure(Throwable arg0) {
                view.error(arg0.getLocalizedMessage());
            }

            public void onSuccess(List<String> arg0) {
                //nothing to do, the SubjectTypes have been loaded
            }
	    });
	    
	    ResourceType.init(policyService, new AsyncCallback<List<String>>() {
            public void onFailure(Throwable arg0) {
                view.error(arg0.getLocalizedMessage());
            }

            public void onSuccess(List<String> arg0) {
                //nothing to do, the ResourceTypes have been loaded
            }
	    });
	    
	    PolicyType.init(policyService, new AsyncCallback<List<String>>() {
            public void onFailure(Throwable arg0) {
                view.error(arg0.getLocalizedMessage());
            }

            public void onSuccess(List<String> arg0) {
                //nothing to do, the PolicyTypes have been loaded
            }
	    });
	}
	private void initPresenters() {
		Presenter presenter = null;
		
		presenter = new SubjectGroupSummaryPresenter(eventBus, new SubjectGroupSummaryView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
		addUserAction(UserAction.SUBJECT_GROUP_SUMMARY, presenter);
		
		presenter = new SubjectGroupCreatePresenter(eventBus, new SubjectGroupCreateView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
		addUserAction(UserAction.SUBJECT_GROUP_CREATE, presenter);
		
		presenter = new SubjectGroupImportPresenter(eventBus, new SubjectGroupImportView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
		addUserAction(UserAction.SUBJECT_GROUP_IMPORT, presenter);
		
		presenter = new SubjectGroupEditPresenter(eventBus, new SubjectGroupEditView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
		addUserAction(UserAction.SUBJECT_GROUP_EDIT,  presenter);
		
		presenter = new SubjectGroupViewPresenter(eventBus, new SubjectGroupViewView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
		addUserAction(UserAction.SUBJECT_GROUP_VIEW,  presenter);
		
		presenter = new PolicySummaryPresenter(eventBus, new PolicySummaryView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
		addUserAction(UserAction.POLICY_SUMMARY,  presenter);

		presenter = new PolicyImportPresenter(eventBus, new PolicyImportView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
		addUserAction(UserAction.POLICY_IMPORT, presenter);

		presenter = new AuthzPolicyCreatePresenter(eventBus, new AuthzPolicyCreateView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
		addUserAction(UserAction.AUTHZ_POLICY_CREATE, presenter);

		presenter = new RLPolicyCreatePresenter(eventBus, new RLPolicyCreateView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
		addUserAction(UserAction.RL_POLICY_CREATE, presenter);

		presenter = new BLPolicyCreatePresenter(eventBus, new BLPolicyCreateView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
		addUserAction(UserAction.BL_POLICY_CREATE, presenter);

		presenter = new WLPolicyCreatePresenter(eventBus, new WLPolicyCreateView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
		addUserAction(UserAction.WL_POLICY_CREATE, presenter);
		
		presenter = new HistoryChangeSummaryPresenter(eventBus, new HistoryChangeSummaryView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
		addUserAction(UserAction.CHANGE_HISTORY_SUMMARY, presenter);
		
		presenter = new BLPolicyEditPresenter(eventBus, new BLPolicyEditView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
                addUserAction(UserAction.BL_POLICY_EDIT, presenter);
		
		presenter = new WLPolicyEditPresenter(eventBus, new WLPolicyEditView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
                addUserAction(UserAction.WL_POLICY_EDIT, presenter);
		
		presenter = new AUTHZPolicyEditPresenter(eventBus, new AUTHZPolicyEditView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
                addUserAction(UserAction.AUTHZ_POLICY_EDIT, presenter);
		
		presenter = new RLPolicyEditPresenter(eventBus, new RLPolicyEditView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
                addUserAction(UserAction.RL_POLICY_EDIT, presenter);
		
        presenter = new PolicyViewPresenter(eventBus, new PolicyViewView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
        addUserAction(UserAction.POLICY_VIEW, presenter);
	}
	
	public void bind() {
	    
	    this.view.getSelector().addSelectionHandler(new SelectionHandler<TreeItem> () {
            public void onSelection(SelectionEvent<TreeItem> selection) {
                //a menu item has been selected - work out which one it is
                if (selection.getSelectedItem().getUserObject() != null) {
                    UserAction action = (UserAction)selection.getSelectedItem().getUserObject();
                    String presenterId = PresenterUtil.getPresenterId(action);
                    if (presenterId != null) {
                        Presenter presenter = presenters.get(presenterId);
                        HistoryToken tok = HistoryToken.newHistoryToken(PRESENTER_ID, null);
                        tok.addValue(HistoryToken.SUB, presenter.getId());
                        History.newItem(tok.toString());
                    }
                }
            }
	    });
	}

	public void go(HasWidgets container, final HistoryToken token) {
        this.container = container;
	    //if (!added) {
	        container.add(this.view.asWidget());
	        added = true;
	    //}
	    if (!metaInit) {
	        initMetaData();
	        metaInit = true;
	    }
	    List<UserAction> actions = PolicyMenuUtil.getPermittedUserActions(AppUserRole.ADMIN);
	    this.view.setActions(actions);
	    
	    selectPresenter(token);
	}

	public String getId() {
		return PRESENTER_ID;
	}

	public void addUserAction (UserAction action, Presenter p) {
	    actionsMap.put(action,p);
	    presentersMap.put(p, action);
	}
	
	public Presenter getPresenter(UserAction action) {
	    return actionsMap.get(action);
	}
	
	public UserAction getUserAction (Presenter p) {
	    return presentersMap.get(p);
	}
	
	public void addPresenter(String id, Presenter p) {
		presenters.put(id, p);
	}

	public Presenter getPresenter(String id) {
		return presenters.get(id);
	}

	public void selectPresenter(HistoryToken token) {
	    
	    if (token != null) {
            String id = HistoryToken.getValue(token, HistoryToken.SUB);
           
            if (id != null) {
                Presenter p = getPresenter(id);     
                if (p != null) {
                    UserAction ua = presentersMap.get(p);
                    this.view.setSelected(ua);
                    p.go(this.view, token);
                }
            }
        }        
	}
}
