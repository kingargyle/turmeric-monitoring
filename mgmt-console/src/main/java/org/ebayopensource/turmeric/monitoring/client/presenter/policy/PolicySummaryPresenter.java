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
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.SupportedService;
import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy;
import org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicyImpl;
import org.ebayopensource.turmeric.monitoring.client.model.policy.Operation;
import org.ebayopensource.turmeric.monitoring.client.model.policy.OperationKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyEnforcementService;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService;
import org.ebayopensource.turmeric.monitoring.client.model.policy.Resource;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyEnforcementService.VerifyAccessResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeletePolicyResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DisablePolicyResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.EnablePolicyResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetPoliciesResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetResourcesResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyType;
import org.ebayopensource.turmeric.monitoring.client.model.policy.QueryCondition;
import org.ebayopensource.turmeric.monitoring.client.model.policy.QueryCondition.ActivePoliciesOnlyValue;
import org.ebayopensource.turmeric.monitoring.client.model.policy.QueryCondition.EffectValue;
import org.ebayopensource.turmeric.monitoring.client.model.policy.ResourceKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.ResourceType;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectType;
import org.ebayopensource.turmeric.monitoring.client.presenter.AbstractGenericPresenter;
import org.ebayopensource.turmeric.monitoring.client.shared.AppUser;
import org.ebayopensource.turmeric.monitoring.client.util.PolicyKeysUtil;
import org.ebayopensource.turmeric.monitoring.client.view.common.PolicyTemplateDisplay.PolicyPageTemplateDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class PolicySummaryPresenter extends AbstractGenericPresenter {

	public final static String PRESENTER_ID = "PolicySummary";

	protected HandlerManager eventBus;
	protected PolicySummaryDisplay view;
	protected Map<SupportedService, ConsoleService> serviceMap;
	protected List<GenericPolicy> policies;
	protected List<Resource> resources;
    protected Map<GenericPolicy, List<UserAction>> permissions = new HashMap<GenericPolicy, List<UserAction>>();
	protected List<String> types;
	protected PolicyQueryService service;

	public interface PolicySummaryDisplay extends PolicyPageTemplateDisplay {
		void setPolicies(List<GenericPolicy> policies);

		HasClickHandlers getSubjectCriteriaButton();

		HasClickHandlers getPolicyCriteriaButton();

		HasClickHandlers getResourceCriteriaButton();

		HasClickHandlers getSubjectGroupCriteriaButton();

		HasClickHandlers getSearchButton();

		String getSearchTerm();

		String getSelectedType();

		String getSelectedResource();

		String getSelectedOperation();
		
	    Map<GenericPolicy, UserAction> getPendingActions();

		boolean isSearchCriteriaEnabled();

		boolean isPolicyCriteriaEnabled();

		boolean isResourceCriteriaEnabled();

		boolean isSubjectCriteriaEnabled();

		boolean isSubjectGroupCriteriaEnabled();

		void setAvailableTypes(List<String> types);

		void setResourceNames();

		void setOperationNames();

		void setPermittedActions(GenericPolicy policy, List<UserAction> permittedActions);

		HasClickHandlers getActionButton();

		void setSelectedType(String type);

		void setSelectedSearchTerm(String name);

		void setSearchCriteriaEnabled(boolean enabled);

		void setPolicyCriteriaEnabled(boolean eanbled);

		void setResourceCriteriaEnabled(boolean eanbled);

		void setSubjectCriteriaEnabled(boolean eanbled);

		void setSubjectGroupCriteriaEnabled(boolean eanbled);

		void error(String msg);

		List<String> getPolicyTypes();

		void setPolicyTypes(List<String> types);

		HasChangeHandlers getResourceNameBox();

		HasChangeHandlers getAvailableTypesBox();

		void setRsNames(List<String> names);

		void setOpNames(List<String> names);

		void setRLEffectBoxVisible(boolean b);

		void setRLEffectLabelVisible(boolean b);

		void setEffect(List<String> types);

		String getSelectedEffect();
	}

	public PolicySummaryPresenter(HandlerManager eventBus,
			PolicySummaryDisplay view,
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
	protected PolicyPageTemplateDisplay getView() {
		return view;
	}

	public void bind() {

		// The user wants to search by subject group type + name
		this.view.getSubjectCriteriaButton().addClickHandler(
				new ClickHandler() {
					public void onClick(ClickEvent event) {
						view.setAvailableTypes(fetchSubjectTypes());
					}
				});

		// the user wants to search by policy type and name
		this.view.getPolicyCriteriaButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				view.setAvailableTypes(fetchPolicyTypes());
			}
		});

		// the user wants to search by resource type
		this.view.getResourceCriteriaButton().addClickHandler(
				new ClickHandler() {
					public void onClick(ClickEvent event) {
						view.setAvailableTypes(fetchResourceTypes());
					}
				});

		// the user wants to search by resource type
		this.view.getAvailableTypesBox().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (view.isResourceCriteriaEnabled()) {
					if (view.getSelectedType() != null
							&& !"".equals(view.getSelectedResource())) {
						fetchResourcesByType(view.getSelectedType());
					}
				}
				if (view.isPolicyCriteriaEnabled()) {
					// TODO improve RL identification
					if (view.getSelectedType() != null
							&& "RL".equals(view.getSelectedType())) {
						view.setRLEffectBoxVisible(true);
						view.setRLEffectLabelVisible(true);

					} else {
						view.setRLEffectBoxVisible(false);
						view.setRLEffectLabelVisible(false);
					}
				}
			}
		});

		// the user wants to search by rs name
		this.view.getResourceNameBox().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (view.getSelectedType() != null
						&& !"".equals(view.getSelectedResource())) {

					getOperationNamesByRs(view.getSelectedResource());
					PolicySummaryPresenter.this.view.setOperationNames();
				}
			}
		});

		// the user wants to search by SubjectGroup type and name
		this.view.getSubjectGroupCriteriaButton().addClickHandler(
				new ClickHandler() {
					public void onClick(ClickEvent event) {
						view.setAvailableTypes(fetchSubjectTypes());
					}
				});

		this.view.getSearchButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {	    
				if (PolicySummaryPresenter.this.view.isPolicyCriteriaEnabled()) {
					fetchPolicyTypes();
					fetchPoliciesByName(PolicySummaryPresenter.this.view
							.getSearchTerm(), PolicySummaryPresenter.this.view
							.getSelectedType(),
							PolicySummaryPresenter.this.view
									.getSelectedEffect());
				} else if (PolicySummaryPresenter.this.view
						.isResourceCriteriaEnabled()) {
					fetchPoliciesByResource(PolicySummaryPresenter.this.view
							.getSelectedType(),
							PolicySummaryPresenter.this.view
									.getSelectedResource(),
							PolicySummaryPresenter.this.view
									.getSelectedOperation());

				} else if (PolicySummaryPresenter.this.view
						.isSubjectCriteriaEnabled()) {
					fetchPoliciesBySubject(
							PolicySummaryPresenter.this.view.getSearchTerm(),
							PolicySummaryPresenter.this.view.getSelectedType());

				} else if (PolicySummaryPresenter.this.view
						.isSubjectGroupCriteriaEnabled()) {
					fetchPoliciesBySubjectGroupName(
							PolicySummaryPresenter.this.view.getSearchTerm(),
							PolicySummaryPresenter.this.view.getSelectedType());
				} else {
					PolicySummaryPresenter.this.view
							.error(ConsoleUtil.policyAdminConstants
									.searchCriteriaInvalid());
				}

				// fetchPolicies(view.isSearchCriteriaEnabled(),
				// view.getSelectedType(), view.getSearchTerm());
				// setPolicies();
			}
		});
		
		this.view.getActionButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                Map<GenericPolicy, UserAction> pending = view.getPendingActions();
                
                if (pending == null)
                    return;
                if (pending.size() == 0)
                    return;
                
                //Things that can be pending:
                //1. editing/viewing a SINGLE policy
                // or
                //2. deleting multiple policies
                // or
                //3. enabling/disabling multiple policies
                
                for (Map.Entry<GenericPolicy, UserAction> entry:pending.entrySet()) {
                    final GenericPolicy p = entry.getKey();
                    switch(entry.getValue()) {
                        case POLICY_VIEW: {
                                    HistoryToken token = makeToken(
                                            PolicyController.PRESENTER_ID,
                                            PolicyViewPresenter.PRESENTER_ID, null);
                                    token.addValue(
                                            HistoryToken.SELECTED_POLICY_TOKEN_ID,
                                            String.valueOf(entry.getKey().getId()));
                                    token.addValue(
                                        HistoryToken.SELECTED_POLICY_TOKEN_TYPE,
                                        String.valueOf(entry.getKey().getType()));
                                    History.newItem(token.toString(), true);
                            break;
                        }
                        case POLICY_EDIT: {
                                    GWT.log("EDIT POLICY:");
                                    // TODO improve this, do not HRDCODE
                                    String policyType = String.valueOf(entry.getKey().getType());
                                    String subPresenter = null;

                                    if ("BLACKLIST".equals(policyType)) {
                                        subPresenter = BLPolicyEditPresenter.PRESENTER_ID;
                                    } else if ("WHITELIST".equals(policyType)) {
                                        subPresenter = WLPolicyEditPresenter.PRESENTER_ID;
                                    } else if ("AUTHZ".equals(policyType)) {
                                        subPresenter = AUTHZPolicyEditPresenter.PRESENTER_ID;
                                    } else if ("RL".equals(policyType)) {
                                        subPresenter = RLPolicyEditPresenter.PRESENTER_ID;
                                    }
                                    HistoryToken token = makeToken(
                                            PolicyController.PRESENTER_ID,
                                            subPresenter, null);
                                    token.addValue(
                                            HistoryToken.SELECTED_POLICY_TOKEN_ID,
                                            String.valueOf(entry.getKey().getId()));
                                    token.addValue(
                                            HistoryToken.SELECTED_POLICY_TOKEN_TYPE,
                                            String.valueOf(entry.getKey().getType()));

                                    History.newItem(token.toString(), true);
                            break;
                        }
                        case POLICY_ENABLE: {
                                    final PolicyKey key = new PolicyKey();
                                    key.setId(entry.getKey().getId());
                                    key.setName(entry.getKey().getName());
                                    key.setType(entry.getKey().getType());
                                    GWT.log("Updating status for :" + entry.getKey().getType()
                                            + " - " + entry.getKey().getName());
                                    service.enablePolicy(key,
                                            new AsyncCallback<EnablePolicyResponse>() {
                                                public void onFailure(Throwable arg) {
                                                    PolicySummaryPresenter.this.view.error(ConsoleUtil.messages.serverError(arg
                                                            .getLocalizedMessage()));
                                                    GWT.log("ERROR - Enabling fails");
                                                }

                                                public void onSuccess(EnablePolicyResponse result) {
                                                    ((GenericPolicyImpl)p).setEnabled(true);
                                                    view.setPolicies(policies);
                                                }
                                            });
                            break;
                        }
                        case POLICY_DISABLE: {
                                    final PolicyKey key = new PolicyKey();
                                    key.setId(entry.getKey().getId());
                                    key.setName(entry.getKey().getName());
                                    key.setType(entry.getKey().getType());
                                    GWT.log("Updating status for :" + entry.getKey().getType()
                                            + " - " + entry.getKey().getName());
                                    service.disablePolicy(key,
                                            new AsyncCallback<DisablePolicyResponse>() {
                                                public void onFailure(Throwable arg) {
                                                    PolicySummaryPresenter.this.view.error(ConsoleUtil.messages.serverError(arg
                                                                                                                           .getLocalizedMessage()));
                                                }

                                                public void onSuccess(
                                                                      DisablePolicyResponse result) {
                                                    ((GenericPolicyImpl)p).setEnabled(false);
                                                    view.setPolicies(policies);
                                                }
                                    });
                                    break;
                        }
                        case POLICY_DELETE: {   
                                        final PolicyKey key = new PolicyKey();
                                        key.setType(entry.getKey().getType());
                                        key.setName(entry.getKey().getName());
                                        key.setId(entry.getKey().getId());

                                        service.deletePolicy(
                                                key,
                                                new AsyncCallback<DeletePolicyResponse>() {

                                                    @Override
                                                    public void onSuccess(
                                                            DeletePolicyResponse result) {
                                                        removePolicy(policies, key);
                                                        view.setPolicies(policies);
                                                    }

                                                    @Override
                                                    public void onFailure(
                                                            Throwable caught) {
                                                        view.error(ConsoleUtil.messages.serverError(caught
                                                                .getLocalizedMessage()));
                                                    }
                                                });
                            break;
                        }
                        case POLICY_EXPORT: {
                            // TODO
                            break;
                        }
                    }
                }
            }
		});
	}

	protected List<Resource> getResources() {
		return resources;
	}

	private void removePolicy(List<GenericPolicy> policies, PolicyKey key) {
		if (policies == null)
			return;
		if (key == null)
			return;
		ListIterator<GenericPolicy> itor = policies.listIterator();
		while (itor.hasNext()) {
			GenericPolicy pol = itor.next();
			if (pol.getName().equals(key.getName())
					&& pol.getType().equals(key.getType()))
				itor.remove();
		}
	}
	
	private List<GenericPolicy> copyToWriteable(Collection<GenericPolicy> policies) {
	    List<GenericPolicy> list = new ArrayList<GenericPolicy>();
	    if (policies == null)
	        return list;
	    for (GenericPolicy p:policies) {
	        GenericPolicyImpl writeable = new GenericPolicyImpl();
            writeable.setId(p.getId());
            writeable.setType(p.getType());
            writeable.setName(p.getName());
            writeable.setDescription(p.getDescription());
            writeable.setCreatedBy(p.getCreatedBy());
            writeable.setCreationDate(p.getCreationDate());
            writeable.setLastModified(p.getLastModified());
            writeable.setLastModifiedBy(p.getLastModifiedBy());
            writeable.setResources(p.getResources());
            writeable.setSubjectGroups(p.getSubjectGroups());
            writeable.setSubjects(p.getSubjects());
            writeable.setEnabled(p.getEnabled());
            list.add(writeable);
	    }
	    return list;
	}


	@Override
	public void go(HasWidgets container, final HistoryToken token) {
		container.clear();
		this.view.setPolicies(null);
		service = (PolicyQueryService) serviceMap
				.get(SupportedService.POLICY_QUERY_SERVICE);

		// find out if any search terms have been prefilled
		String srchType = token.getValue(HistoryToken.SRCH_POLICY_TYPE);
		String srchName = token.getValue(HistoryToken.SRCH_POLICY_NAME);

		if (srchType != null && srchName != null) {
		    this.view.setSearchCriteriaEnabled(true);
		    this.view.setPolicyCriteriaEnabled(true);
		    this.view.setSubjectCriteriaEnabled(false);
		    this.view.setSubjectGroupCriteriaEnabled(false);
		    this.view.setResourceCriteriaEnabled(false);
		    this.view.setAvailableTypes(fetchPolicyTypes());
		    this.view.setSelectedType(srchType);
		    this.view.setSelectedSearchTerm(srchName);
		    fetchPoliciesByName(srchName, srchType, null);
		}

		setRLEffect();

		this.view.activate();
		container.add(this.view.asWidget());
	}

	protected void setRLEffect() {
		this.view.setEffect(Collections.EMPTY_LIST);
		List<String> rlEffect = new ArrayList<String>();
		rlEffect.add("BLOCK");
		rlEffect.add("CHALLENGE");
		rlEffect.add("FLAG");
		this.view.setEffect(rlEffect);
	}

	private void setPolicies() {
		this.view.setPolicies(policies);
	}

	private List<String> fetchSubjectTypes() {
		return SubjectType.getValues();
	}

	private List<String> fetchPolicyTypes() {
		return PolicyType.getValues();
	}

	private List<String> fetchResourceTypes() {
		return ResourceType.getValues();
	}

	private void fetchPoliciesByName(String name, String type, String effect) {
		PolicyKey key = new PolicyKey();
		key.setName(name);
		key.setType(type);

		QueryCondition condition = new QueryCondition();
		// TODO improve this hardcoded RL
		if ("RL".equals(type) && effect != null) {
			condition.addQuery(new QueryCondition.Query(
					QueryCondition.EffectValue.valueOf(effect)));
		}

		condition.addQuery(new QueryCondition.Query(
				QueryCondition.ActivePoliciesOnlyValue.FALSE));

				service.findPolicies(null, Collections.singletonList(key), null, null,
				null, null, null, condition,
				new AsyncCallback<GetPoliciesResponse>() {

					public void onFailure(Throwable arg) {
						PolicySummaryPresenter.this.view
								.error(ConsoleUtil.messages.serverError(arg
										.getLocalizedMessage()));
					}

					@Override
					public void onSuccess(GetPoliciesResponse result) {
						PolicySummaryPresenter.this.policies = copyToWriteable(result.getPolicies());
						PolicySummaryPresenter.this.view.setPolicies(policies);
					      for (GenericPolicy p:policies)
	                            fetchAccess(p);
					}
				});

	}

	private void fetchResourcesByType(String type) {
		ResourceKey key = new ResourceKey();
		key.setType(type);

		service.getResources(Collections.singletonList(key),
				new AsyncCallback<GetResourcesResponse>() {

					public void onFailure(Throwable arg) {
						PolicySummaryPresenter.this.view
								.error(ConsoleUtil.messages.serverError(arg
										.getLocalizedMessage()));
					}

					@Override
					public void onSuccess(GetResourcesResponse result) {
						PolicySummaryPresenter.this.resources = new ArrayList<Resource>(
								result.getResources());

						List<String> rsNames = new ArrayList<String>();
						for (Resource rs : resources) {
							rsNames.add(rs.getResourceName());
						}
						PolicySummaryPresenter.this.view.setRsNames(rsNames);
						PolicySummaryPresenter.this.view.setResourceNames();

					}
				});

	}

	private void getOperationNamesByRs(String rsName) {
		List<String> opNames = new ArrayList<String>();

		if (resources != null && resources.size() > 0) {
			List<Operation> operations = new ArrayList<Operation>();
			for (Resource rs : resources) {
				if (rsName.equals(rs.getResourceName())) {
					operations = rs.getOpList();
					for (Operation op : operations) {
						opNames.add(op.getOperationName());
					}

					break;
				}

			}
		}

		PolicySummaryPresenter.this.view.setOpNames(opNames);
		PolicySummaryPresenter.this.view.setOperationNames();

	}

	private void fetchPoliciesBySubjectGroupName(String name, String type) {
		SubjectGroupKey key = new SubjectGroupKey();
		key.setName(name);
		key.setType(type);

		List<PolicyKey> polKeys = PolicyKeysUtil.getAllPolicyKeyList();

		QueryCondition condition = new QueryCondition();
		condition.addQuery(new QueryCondition.Query(
				ActivePoliciesOnlyValue.FALSE));

		service.findPolicies(null, polKeys, null, null, null,
				Collections.singletonList(key), null, condition,
				new AsyncCallback<GetPoliciesResponse>() {

					public void onFailure(Throwable arg) {
						PolicySummaryPresenter.this.view
								.error(ConsoleUtil.messages.serverError(arg
										.getLocalizedMessage()));
					}

					@Override
					public void onSuccess(GetPoliciesResponse result) {
						PolicySummaryPresenter.this.policies = copyToWriteable(result.getPolicies());
						PolicySummaryPresenter.this.view.setPolicies(policies);
					      for (GenericPolicy p:policies)
	                            fetchAccess(p);
					}
				});

	}

	private void fetchPoliciesBySubject(String subjectName, String subjectType) {
		SubjectKey subjectKey = new SubjectKey();
		subjectKey.setType(subjectType);
		// if (subjectName != null && !"".equals(subjectName.trim()))
		subjectKey.setName(subjectName);
		List<PolicyKey> polKeys = PolicyKeysUtil.getAllPolicyKeyList();

		QueryCondition condition = new QueryCondition();
		condition.addQuery(new QueryCondition.Query(
				ActivePoliciesOnlyValue.FALSE));
		service.findPolicies(null, polKeys, null, null,
				Collections.singletonList(subjectKey), null, null, condition,
				new AsyncCallback<GetPoliciesResponse>() {

					public void onFailure(Throwable arg) {
						PolicySummaryPresenter.this.view
								.error(ConsoleUtil.messages.serverError(arg
										.getLocalizedMessage()));
					}

					@Override
					public void onSuccess(GetPoliciesResponse result) {
						PolicySummaryPresenter.this.policies = copyToWriteable(result.getPolicies());
						PolicySummaryPresenter.this.view.setPolicies(policies);
					      for (GenericPolicy p:policies)
	                            fetchAccess(p);
					}
				});
	}

	private void fetchPoliciesByResource(final String resourceType,
			final String resourceName, final String operationName) {
		ResourceKey resKey = new ResourceKey();

		List<OperationKey> opKeys = null;
		resKey.setType(resourceType);

		if (resourceName != null && !"".equals(resourceName.trim())) {
			resKey.setName(resourceName);
		}

		if (operationName != null && !"".equals(operationName.trim())) {
			OperationKey opKey = new OperationKey();
			opKey.setOperationName(operationName);
			opKey.setResourceType(resourceType);
			opKey.setResourceName(resourceName);

			opKeys = new ArrayList<OperationKey>();
			opKeys.add(opKey);
		} else {
			// get by all operations
			OperationKey opKey = new OperationKey();
			List<Operation> opList = null;
			for (Resource resource : resources) {
				if (resourceName.equals(resource.getResourceName())) {
					opList = resource.getOpList();
					break;
				}

			}
			if (opList != null && !opList.isEmpty()) {
				opKeys = new ArrayList<OperationKey>();

				for (Operation operation : opList) {
					opKey.setOperationName(operation.getOperationName());
					opKey.setResourceName(resourceName);
					opKey.setResourceType(resourceType);
					opKeys.add(opKey);
				}
			}

		}

		List<PolicyKey> polKeys = PolicyKeysUtil.getAllPolicyKeyList();

		QueryCondition condition = new QueryCondition();
		condition.addQuery(new QueryCondition.Query(
				ActivePoliciesOnlyValue.FALSE));
		service.findPolicies(null, polKeys, Collections.singletonList(resKey),
				opKeys, null, null, null, condition,
				new AsyncCallback<GetPoliciesResponse>() {

					public void onFailure(Throwable arg) {
						PolicySummaryPresenter.this.view
								.error(ConsoleUtil.messages.serverError(arg
										.getLocalizedMessage()));
					}

					@Override
					public void onSuccess(GetPoliciesResponse result) {
						PolicySummaryPresenter.this.policies = copyToWriteable(result.getPolicies());
						PolicySummaryPresenter.this.view.setPolicies(policies);
						for (GenericPolicy p:policies)
						    fetchAccess(p);
					}
				});
	}
	
	private void fetchAccess (final GenericPolicy policy) {
	    List<UserAction> actions = newPermissions();
	    permissions.put(policy, actions);
	    
	    fetchAccess(UserAction.POLICY_DELETE, policy,
	                new AsyncCallback<Boolean>() {
	        public void onFailure(Throwable arg0) {
                view.error(arg0.getLocalizedMessage());
            }

	        public void onSuccess(Boolean allowed) {
	            List<UserAction> permits = permissions.get(policy);
	            if (allowed.booleanValue())
	                permits.add(UserAction.POLICY_DELETE);
	            else
	                permits.remove(UserAction.POLICY_DELETE);
	            view.setPermittedActions(policy, permits);     
            }
	    });
	    
	    
	    fetchAccess(UserAction.POLICY_EDIT, policy, 
	                new AsyncCallback<Boolean> () {
	        public void onFailure(Throwable arg0) {
                view.error(arg0.getLocalizedMessage());
            }

            public void onSuccess(Boolean allowed) {
                List<UserAction> permits = permissions.get(policy); 
                if (allowed.booleanValue()) {
                    permits.add(UserAction.POLICY_EDIT);
                    permits.add(UserAction.POLICY_DISABLE);
                    permits.add(UserAction.POLICY_ENABLE);
                } else {
                    permits.remove(UserAction.POLICY_EDIT);
                    permits.remove(UserAction.POLICY_DISABLE);
                    permits.remove(UserAction.POLICY_ENABLE);
                }
                view.setPermittedActions(policy, permits);
            }
	    });
	}

	private void fetchAccess (final UserAction action, final GenericPolicy policy, final AsyncCallback<Boolean> callback) {
	    
	    //TODO TODO TODO
	    //Remove after PES starts working properly with valid JSON
	    /*
        
	    callback.onSuccess(Boolean.TRUE);
	    return;
	       */

	    PolicyEnforcementService enforcementService = (PolicyEnforcementService)serviceMap.get(SupportedService.POLICY_ENFORCEMENT_SERVICE);
	    if (enforcementService == null)
	        return;
	    if (policy == null)
	        return;
	    if (action == null)
	        return;

	    String resName =  null;
	    String opName = null;
	    switch (action) {
	        case POLICY_DELETE: {
	            resName = PolicyEnforcementService.POLICY_DELETE_RESOURCE;
	            opName = policy.getId().toString();
	            break;
	        }
	        case POLICY_EDIT: {
	            resName = PolicyEnforcementService.POLICY_EDIT_RESOURCE;
	            opName = policy.getId().toString();
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

	    enforcementService.verify(opKey, policyTypes, credentials, subjectTypes, null, null, null, 
	                              new AsyncCallback<VerifyAccessResponse>() {

	        public void onFailure(Throwable arg0) {
	           callback.onFailure(arg0);
	        }

	        public void onSuccess(VerifyAccessResponse response) {
	            System.err.println("Response = "+(!response.isErrored())+" for PES for action="+action+" on  policy "+policy.getName());
	            callback.onSuccess(Boolean.valueOf(!response.isErrored()));
	        }
	    });

	}
	
    private List<UserAction> newPermissions () {
        List<UserAction> actions = new ArrayList<UserAction>();
        actions.add(UserAction.POLICY_VIEW); //view is always allowed
        actions.add(UserAction.POLICY_EXPORT); //allow export too
        return actions;
    }
}