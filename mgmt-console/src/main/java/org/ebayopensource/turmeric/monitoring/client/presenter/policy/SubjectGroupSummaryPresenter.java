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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.SupportedService;
import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy;
import org.ebayopensource.turmeric.monitoring.client.model.policy.OperationKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyEnforcementService;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyType;
import org.ebayopensource.turmeric.monitoring.client.model.policy.QueryCondition;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupImpl;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupQuery;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectType;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyEnforcementService.VerifyAccessResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeleteSubjectGroupResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectGroupsResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetPoliciesResponse;
import org.ebayopensource.turmeric.monitoring.client.presenter.AbstractGenericPresenter;
import org.ebayopensource.turmeric.monitoring.client.shared.AppUser;
import org.ebayopensource.turmeric.monitoring.client.util.PolicyKeysUtil;
import org.ebayopensource.turmeric.monitoring.client.view.common.PolicyTemplateDisplay.PolicyPageTemplateDisplay;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class SubjectGroupSummaryPresenter extends AbstractGenericPresenter {
	
	public final static String PRESENTER_ID = "SubjectGroupSummary";
	
	protected HandlerManager eventBus;
	protected SubjectGroupSummaryDisplay view;
	protected Map<SupportedService, ConsoleService> serviceMap;
	protected List<SubjectGroup> groups;
	protected Map<SubjectGroup, List<UserAction>> permissions;
	protected List<String> types;
	protected List<UserAction> permittedActions = new ArrayList<UserAction>();
	protected PolicyQueryService service;
	
	public interface SubjectGroupSummaryDisplay extends PolicyPageTemplateDisplay {
	    void setGroups(List<SubjectGroup> groups);
	    HasClickHandlers getSubjectCriteriaButton();
	    HasClickHandlers getPolicyCriteriaButton();
	    HasClickHandlers getSearchButton();
	    String getSearchTerm();
	    String getSelectedType();
	    boolean isSubjectCriteriaEnabled();
	    boolean isPolicyCriteriaEnabled();
	    void setAvailableTypes(List<String> types);
	    void setPermittedActions(SubjectGroup group, List<UserAction> permittedActions);
	    HasClickHandlers getActionButton();
	    Map<SubjectGroup, UserAction> getPendingActions();
	    void setSelectedType(String type);
	    void setSelectedSearchTerm(String name);
	    void setSearchCriteriaEnabled(boolean enabled);
	    void setPolicyCriteriaEnabled(boolean eanbled);
	    void error(String error);
	}
	
	
	
	public SubjectGroupSummaryPresenter(HandlerManager eventBus, SubjectGroupSummaryDisplay view, Map<SupportedService, ConsoleService> serviceMap) {
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
		//The user wants to search by subject group type + name
		this.view.getSubjectCriteriaButton().addClickHandler(new ClickHandler() {
		    public void onClick(ClickEvent event) {
		        view.setAvailableTypes(fetchSubjectTypes());
		        view.setSelectedSearchTerm("");
		    }
		});
		
		//the user wants to search by policy type and name
		this.view.getPolicyCriteriaButton().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                view.setAvailableTypes(fetchPolicyTypes());
                view.setSelectedSearchTerm("");
            }
        });
		
		this.view.getSearchButton().addClickHandler(new ClickHandler() {
		    public void onClick(ClickEvent event) {
		        fetchGroups(view.isSubjectCriteriaEnabled(), view.getSelectedType(), view.getSearchTerm());
		    }
		});

		this.view.getActionButton().addClickHandler(new ClickHandler() {
		    public void onClick(ClickEvent event) {
		        Map<SubjectGroup, UserAction> pending = view.getPendingActions();
		    
		        if (pending == null)
		            return;
		        if (pending.size() == 0)
		            return;
		        //all user actions have to be the same, so pick the first one
		        SubjectGroup group = pending.keySet().iterator().next();
		        UserAction action = pending.get(group);
		        
		        switch (action) 
		        {
		            case SUBJECT_GROUP_VIEW: {
		                HistoryToken token = makeToken(PolicyController.PRESENTER_ID, SubjectGroupViewPresenter.PRESENTER_ID, null);
                        token.addValue(HistoryToken.SELECTED_SUBJECT_GROUP_TOKEN, group.getName());
                        token.addValue(HistoryToken.SELECTED_SUBJECT_GROUP_TYPE_TOKEN, group.getType().toString());
                        History.newItem(token.toString(), true);
		                break;
		            }
		            case SUBJECT_GROUP_EDIT: {
		                HistoryToken token = makeToken(PolicyController.PRESENTER_ID,SubjectGroupEditPresenter.PRESENTER_ID, null);
                        token.addValue(HistoryToken.SELECTED_SUBJECT_GROUP_TOKEN, group.getName());
                        token.addValue(HistoryToken.SELECTED_SUBJECT_GROUP_TYPE_TOKEN, group.getType().toString());
                        History.newItem(token.toString(), true);
		                break;
		            }
		            case SUBJECT_GROUP_DELETE: {
		              
                        final List<SubjectGroupKey> keys= new ArrayList<SubjectGroupKey>();
                        Iterator<SubjectGroup> itor = pending.keySet().iterator();
                        SubjectGroup groupError = null;
                        while (itor.hasNext() && groupError == null) {
                            SubjectGroup sg = itor.next();

                            if (sg.getPolicies() != null && sg.getPolicies().size() > 0)
                                groupError = sg;
                            else {

                                SubjectGroupKey key = new SubjectGroupKey();
                                key.setType(sg.getType());
                                key.setName(sg.getName());
                                key.setId(sg.getId());
                                keys.add(key);
                            }
                        }

                        if (groupError != null)
                            view.error(ConsoleUtil.policyAdminConstants.deleteSubjetGroupInvalid());
                        else {

                            service.deleteSubjectGroups(keys, new AsyncCallback<DeleteSubjectGroupResponse>() {

                                @Override
                                public void onSuccess(DeleteSubjectGroupResponse result) {
                                    //                              fetchGroups(view.isSearchCriteriaEnabled(), view.getSelectedType(), view.getSearchTerm());
                                    removeGroups(groups, keys);
                                    view.setGroups(groups);
                                }

                                @Override
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
                            });
                        }
                        break;
		            }
		        }
		    }
		});
	}
	
	@Override
	public void go(HasWidgets container, final HistoryToken token) {
		container.clear();
		
		this.view.setGroups(null);
		
		service = (PolicyQueryService) serviceMap
		.get(SupportedService.POLICY_QUERY_SERVICE);
		
		//find out if any search terms have been prefilled
		String srchType = token.getValue(HistoryToken.SRCH_SUBJECT_GROUP_TYPE);
		String srchName = token.getValue(HistoryToken.SRCH_SUBJECT_GROUP_NAME);

		if (srchType != null && srchName != null) {
		    this.view.setSearchCriteriaEnabled(true);
		    this.view.setAvailableTypes(fetchSubjectTypes());
		    this.view.setSelectedType(srchType);
		    this.view.setSelectedSearchTerm(srchName);
		    fetchGroups(true, srchType, srchName);
		}

		this.view.activate();
		container.add(this.view.asWidget());
	}
	
	
	private void fetchGroups (boolean isSubjectType, String type, String searchTerm) {

	    if(isSubjectType){
	    	//Find by subject type 
	        SubjectGroupKey key = new SubjectGroupKey();
	        key.setType(type);
	        key.setName(searchTerm);
	        fetchGroups(Collections.singletonList(key));
	    } else {
	    	//Get the policies matching the search critieria and get its subject groups
	        PolicyKey pkey = new PolicyKey();
	        pkey.setType(type);
	        pkey.setName(searchTerm);
	        QueryCondition condition = new QueryCondition();
	        condition.addQuery(new QueryCondition.Query(QueryCondition.ActivePoliciesOnlyValue.FALSE));

	        service.findPolicies(null, Collections.singletonList(pkey), null, null, null, null, null, condition, new AsyncCallback<GetPoliciesResponse>() {
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

	            public void onSuccess(GetPoliciesResponse result) {
	                Collection<GenericPolicy>policies = result.getPolicies();  
	                if (policies != null && policies.size() >0) {    
	                    List<SubjectGroupKey> keys = new ArrayList<SubjectGroupKey>();
	                    for (GenericPolicy p:policies) {
	                        if (p.getSubjectGroups() != null) {

	                            //for each subject group returned, we fetch its full details
	                            for (SubjectGroup g:p.getSubjectGroups()) {
	                                SubjectGroupKey k = new SubjectGroupKey();
	                                k.setType(g.getType());
	                                k.setName(g.getName());
	                                keys.add(k);
	                            }
	                        }
	                    }
	                    fetchGroups(keys);
	                } else
	                    view.setGroups(null);
	            }
	        });   
	    }
	}
	
	private void fetchGroups (List<SubjectGroupKey> keys) {
	    if (keys == null || keys.isEmpty())
	        return;
	    SubjectGroupQuery query = new SubjectGroupQuery();
        query.setIncludeSubjects(true);
        query.setGroupKeys(keys);
        service.findSubjectGroups(query, new AsyncCallback<FindSubjectGroupsResponse>() {

            public void onSuccess(FindSubjectGroupsResponse response) {
                //Turn the SubjectGroups returned by the server into copies that are writeable
                //so that we can pump in the related policies
                List<SubjectGroup> results = response.getGroups();
                groups = new ArrayList<SubjectGroup>();
                permissions = new HashMap<SubjectGroup, List<UserAction>>();
                if (results != null) {
                    for (SubjectGroup r:results) {
                        SubjectGroupImpl sgi = new SubjectGroupImpl(r);
                        groups.add(sgi);
                        permissions.put(sgi, newPermissions());
                        //TODO do this lazily page by page
                        fetchAccess(sgi);
                    }
                }
                fetchPoliciesForGroups();
            }

            public void onFailure(Throwable arg) {
            	if (arg.getLocalizedMessage().contains("500")) {
					view.error(ConsoleUtil.messages
							.serverError(ConsoleUtil.policyAdminConstants
									.genericErrorMessage()));
				} else {
					view.error(ConsoleUtil.messages.serverError(arg
							.getLocalizedMessage()));
				}            }
        });
	}
	
	
	private void fetchPoliciesForGroups () {
	    if (groups == null || groups.size() ==0)
	        return;

	    //Make a request to the server to get the policies used by each group
	    List<SubjectGroupKey> groupKeys = new ArrayList<SubjectGroupKey>();
	    final Map<SubjectGroupKey, SubjectGroup> groupMap = new HashMap<SubjectGroupKey, SubjectGroup>();
	    for (final SubjectGroup g:groups) {
	        SubjectGroupKey key = new SubjectGroupKey();
	        key.setName(g.getName());
	        key.setType(g.getType());
	        groupKeys.add(key);
	        groupMap.put(key, g);
	    }
	    QueryCondition condition = new QueryCondition();
	    condition.addQuery(new QueryCondition.Query(QueryCondition.ActivePoliciesOnlyValue.FALSE));
	    service.findPolicies(null, PolicyKeysUtil.getAllPolicyKeyList(), null, null, null, groupKeys, null, condition, new AsyncCallback<GetPoliciesResponse>() {
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

	        public void onSuccess(GetPoliciesResponse result) {
	            Collection<GenericPolicy>policies = result.getPolicies();
	            if (policies != null) {           
	                for (GenericPolicy p:policies)
	                    updatePoliciesForSubjectGroups(groupMap, p);
	            }
                view.setGroups(groups);
	        }
	    });
	}

	private List<String> fetchSubjectTypes () {
	    return SubjectType.getValues();
	}
	
	private List<String> fetchPolicyTypes() {
	    return PolicyType.getValues();
	}

	private SubjectGroup getGroupByName (String name) {
	    if (name == null)
	        return null;
	    if (groups == null)
	        return null;
	    
	    SubjectGroup g = null;
	    int i=0;
	    while (g == null && i<groups.size()) {
	        if (name.equals(groups.get(i).getName()))
	            g = groups.get(i);
	        else
	            i++;
	    }
	    return g;
	}
	
	private void removeGroups (List<SubjectGroup> groups, List<SubjectGroupKey> keys) {
	    if (groups == null)
	        return;
	    if (keys == null)
	        return;
	    
	    ListIterator<SubjectGroup> itor = groups.listIterator();
	    while (itor.hasNext()) {
	        SubjectGroup g = itor.next();
	        for (SubjectGroupKey key:keys) {
	            if (g.getName().equals(key.getName()) && g.getType().equals(key.getType()))
	                itor.remove();
	        }
	    }
	}
	
	
	private void updatePoliciesForSubjectGroups (Map<SubjectGroupKey,SubjectGroup> groupMap, GenericPolicy p) {
	    if (p == null || p.getSubjectGroups() == null)
	        return;

	    for (SubjectGroup groupInPolicy:p.getSubjectGroups()) {
	        SubjectGroupKey key = new SubjectGroupKey();
	        key.setName(groupInPolicy.getName());
	        key.setType(groupInPolicy.getType());
	        SubjectGroup matchingGroup = groupMap.get(key);
	        if (matchingGroup != null) {
	            addPolicyToSubjectGroup(matchingGroup, p);
	        }
	    }
	}
	
	private void addPolicyToSubjectGroup (SubjectGroup group, GenericPolicy policy) {
	    if (group == null || policy == null)
	        return;

	    List<String> policyNames = group.getPolicies();
	    if (policyNames == null) {
	        policyNames = new ArrayList<String>();
	        ((SubjectGroupImpl)group).setPolicies(policyNames);
	    }
	    
	    policyNames.add(policy.getName());
	}
	
	
	private void fetchAccess (final SubjectGroup group) {
	    final List<UserAction> actions = newPermissions();
        permissions.put(group, actions);
	    fetchAccess(UserAction.SUBJECT_GROUP_DELETE, group, new AsyncCallback<Boolean>() {
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
	        public void onSuccess(Boolean allowed) {
	            if (allowed.booleanValue())
	                actions.add(UserAction.SUBJECT_GROUP_DELETE);
	            else
	                actions.remove(UserAction.SUBJECT_GROUP_DELETE);

                view.setPermittedActions(group, actions);
	        }
	    });
	    fetchAccess(UserAction.SUBJECT_GROUP_EDIT, group, new AsyncCallback<Boolean>() {
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
	        public void onSuccess(Boolean allowed) {
	            if (allowed.booleanValue())
	                actions.add(UserAction.SUBJECT_GROUP_EDIT);
	            else
	                actions.remove(UserAction.SUBJECT_GROUP_EDIT);

                view.setPermittedActions(group, actions);
            }
	    });
	}
	
	
	private void fetchAccess (final UserAction action, final SubjectGroup group, final AsyncCallback<Boolean> callback) {
	    /*
	    callback.onSuccess(Boolean.TRUE);
	    return;
	    //TODO TODO TODO commented out until PES starts returning reasonable JSON
	
	    */
	    
	    PolicyEnforcementService enforcementService = (PolicyEnforcementService)serviceMap.get(SupportedService.POLICY_ENFORCEMENT_SERVICE);
	    if (enforcementService == null)
	        return;
	    if (group == null)
	        return;
	    if (action == null)
	        return;
	    
	    String resName =  null;
	    String opName = null;
	    switch (action) {
	        case SUBJECT_GROUP_DELETE: {
	            resName = PolicyEnforcementService.SUBJECT_GROUP_DELETE_RESOURCE;
	            opName = group.getId().toString();
	            break;
	        }
	        case SUBJECT_GROUP_EDIT: {
	            resName = PolicyEnforcementService.SUBJECT_GROUP_EDIT_RESOURCE;
	            opName = group.getId().toString();
	            break;
	        }
	    }
	    
	    //TODO - are credentials necessary?
	    Map<String,String> credentials = new HashMap<String,String>();
	    credentials.put("X-TURMERIC-SECURITY-PASSWORD", AppUser.getUser().getPassword());
	    OperationKey opKey = new OperationKey();
        opKey.setResourceName(resName);
        opKey.setOperationName(opName);
        opKey.setResourceType("OBJECT");

        List<String> policyTypes = Collections.singletonList("AUTHZ");

        String[] subjectType = {"USER", AppUser.getUser().getUsername()};
        List<String[]> subjectTypes = Collections.singletonList(subjectType);

        enforcementService.verify(opKey, 
                                  policyTypes, 
                                  credentials, 
                                  subjectTypes, 
                                  null, null, null, 
                                  new AsyncCallback<VerifyAccessResponse>() {

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

                                    public void onSuccess(VerifyAccessResponse response) {
                                      callback.onSuccess(Boolean.valueOf(!response.isErrored()));
                                    }
                                  });
                              
	}
	
	private List<UserAction> newPermissions () {
	    List<UserAction> actions = new ArrayList<UserAction>();
	    actions.add(UserAction.SUBJECT_GROUP_VIEW); //view is always allowed
	    actions.add(UserAction.SUBJECT_GROUP_EXPORT); //allow export?
        return actions;
	}
}
