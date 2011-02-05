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
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupQuery;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectImpl;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectQuery;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectGroupsResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectsResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdateMode;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdateSubjectGroupsResponse;
import org.ebayopensource.turmeric.monitoring.client.presenter.AbstractGenericPresenter;
import org.ebayopensource.turmeric.monitoring.client.view.common.PolicyTemplateDisplay.PolicyPageTemplateDisplay;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * SubjectGroupEditPresenter
 *
 */
public class SubjectGroupEditPresenter extends AbstractGenericPresenter {
    public final static String PRESENTER_ID = "SubjectGroupEdit";
    
    protected HandlerManager eventBus;
    protected SubjectGroupEditDisplay view;
    protected SubjectGroup originalGroup;
    protected Map<SupportedService, ConsoleService> serviceMap;
    private PolicyQueryService service ;
    
    public interface SubjectGroupEditDisplay extends PolicyPageTemplateDisplay {
        public void setName (String name);
        public String getName ();
        public void setDescription (String desc);
        public String getDescription();
        public HasClickHandlers getSearchButton();
        public HasClickHandlers getCancelButton();
        public Button getApplyButton();
        public List<String> getSelectedSubjects();
        public void setSelectedSubjects(List<String> subjects);
        public void setAvailableSubjects(List<String> subjects);
        public String getSearchTerm();
        public void error(String msg);
        public void clear();
    }
    
    
    
    public SubjectGroupEditPresenter (HandlerManager eventBus, SubjectGroupEditDisplay view, Map<SupportedService, ConsoleService> serviceMap) {
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
                    //do a lookup of all the matching Subjects  
                    //Get the available subjects of that type
                    SubjectQuery subjectQuery = new SubjectQuery();
                    SubjectKey key = new SubjectKey();
                    String subName = view.getSearchTerm();
                    if (subName != null && !subName.trim().equals(""))
                        key.setName(subName);
                    key.setType(originalGroup.getType());
                    subjectQuery.setSubjectKeys(Collections.singletonList(key));
   				 
                    if ("USER".equals(key.getType())) {
    					service.findExternalSubjects(subjectQuery,
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
    									
    									if (originalGroup.getSubjects() != null){
    			                                names.removeAll(originalGroup.getSubjects());
    									 }
    			                        view.setAvailableSubjects(names);

    								}

    							});

    				} else {
	                    service.findSubjects(subjectQuery, new AsyncCallback<FindSubjectsResponse> () {
	
	                        public void onFailure(Throwable arg0) {
	                            view.error(arg0.getMessage());
	                        }
	
	                        public void onSuccess(FindSubjectsResponse response) {
	                            List<Subject> subjects = response.getSubjects();
	                            List<String> names = new ArrayList<String>();
	                            if (subjects != null) {
	                                for (Subject s:subjects)
	                                    names.add(s.getName());
	                            }
	                            if (originalGroup.getSubjects() != null){
	                                names.removeAll(originalGroup.getSubjects());
	                            }
	                            view.setAvailableSubjects(names);
	                        }
	                    });
	    				}
                }
            });
            
            this.view.getApplyButton().addClickHandler(new ClickHandler() {
                public  void onClick(ClickEvent event) {
                	List<String> subjects = SubjectGroupEditPresenter.this.view.getSelectedSubjects();
    		        if (subjects == null || subjects.isEmpty()) {
    		            SubjectGroupEditPresenter.this.view.error(ConsoleUtil.policyAdminMessages.minimumSubjectsMessage());
    		            return;
    		        }
			        
			        
			        //TODO Send the changes to the server side. When acknowledged go back to Summary
                    final SubjectGroupImpl editedGroup = new SubjectGroupImpl(originalGroup);
                    editedGroup.setName(view.getName());
                    editedGroup.setDescription(view.getDescription());
                    editedGroup.setSubjects(view.getSelectedSubjects());
                    
    				if("USER".equals(originalGroup.getType())){				
    					//external subjects todays are only USER types
	    		        List<Subject> externalSubjects= new ArrayList<Subject>();
	            		for (String sb :  view.getSelectedSubjects()) {
	            			SubjectImpl subject = new SubjectImpl();
	            			subject.setType("USER");
	            			subject.setName(sb);
	            			externalSubjects.add(subject);
	            		}
	            		
	            	    createInternalSubject(externalSubjects);
    				}                    
            	    /**
            	     * This timer is needed due to GWT has only one thread, so
            	     * Thread.sleep is not a valid option
            	     * The purpose of sleeping time is wait until new external subject 
            	     * been created into turmeric db, in order to assign them as 
            	     * internal subjects 
            	     */
			        Timer timer = new Timer() {
			            public void run() {
			            	   			        
		                    service.updateSubjectGroups(Collections.singletonList((SubjectGroup)editedGroup),UpdateMode.REPLACE, new AsyncCallback<UpdateSubjectGroupsResponse>() {
		
		                        public void onFailure(Throwable arg0) {
		                            view.error(arg0.getLocalizedMessage());
		                        }
		
		                        public void onSuccess(UpdateSubjectGroupsResponse response) {
		                            //copy changes from the editedGroup back to the group
		                            ((SubjectGroupImpl)originalGroup).setName(view.getName());
		                            ((SubjectGroupImpl)originalGroup).setDescription(view.getDescription());
		                            ((SubjectGroupImpl)originalGroup).setSubjects(view.getSelectedSubjects());
		                            view.clear();
		                            HistoryToken token = makeToken(PolicyController.PRESENTER_ID,
		                                                           SubjectGroupSummaryPresenter.PRESENTER_ID, 
		                                                           null);
		                            token.addValue(HistoryToken.SRCH_SUBJECT_GROUP_TYPE, originalGroup.getType());
		                            token.addValue(HistoryToken.SRCH_SUBJECT_GROUP_NAME, originalGroup.getName());
		                            History.newItem(token.toString(), true);
		                        }
		                    });
		            		
			            }
			        };
			        if("USER".equals(originalGroup.getType())){
						view.getApplyButton().setEnabled(false);
			        	timer.schedule(3000);
			        }else{
			        	timer.schedule(1);
			        }
                }
            });

            this.view.getCancelButton().addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    view.clear();
                    //Just go back to the summary
                    HistoryToken token = makeToken(PolicyController.PRESENTER_ID, SubjectGroupSummaryPresenter.PRESENTER_ID, null);
                    History.newItem(token.toString(), true);
                }
            });
    }
    
  	private void createInternalSubject(final List<Subject> subjects) {
		  
  		List<SubjectKey> keys = new ArrayList<SubjectKey>();
			for (Subject subj : subjects) {
				SubjectKey key = new SubjectKey();
				key.setName(subj.getName());
				//today external subject supported are USER types 
				key.setType("USER");
				keys.add(key);
			}
  			
  			final SubjectQuery query = new SubjectQuery();
			query.setSubjectKeys(keys);
  			service.findSubjects(query, new AsyncCallback<PolicyQueryService.FindSubjectsResponse>() {
				
				public void onSuccess(FindSubjectsResponse result) {
					subjects.removeAll(result.getSubjects());
					if(subjects.size()>0){

						service.createSubjects(subjects,
								new AsyncCallback<PolicyQueryService.CreateSubjectsResponse>() {
	
									public void onSuccess(final CreateSubjectsResponse result) {
										//do nothing, subjects has been stored, we can continue...
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
    
    public void go(final HasWidgets container, final HistoryToken token) {
        //Get the id from the token
        final String name = token.getValue(HistoryToken.SELECTED_SUBJECT_GROUP_TOKEN);
        final String type = token.getValue(HistoryToken.SELECTED_SUBJECT_GROUP_TYPE_TOKEN);
        
        // do a lookup of all the matching Subjects
		 service = (PolicyQueryService)serviceMap.get(SupportedService.POLICY_QUERY_SERVICE);

        
        if (name != null) {
            container.clear();
            view.activate();
            container.add(view.asWidget());

            //Get the SubjectGroup being edited
            final PolicyQueryService service = (PolicyQueryService)serviceMap.get(SupportedService.POLICY_QUERY_SERVICE);
            SubjectGroupQuery query = new SubjectGroupQuery();
            query.setIncludeSubjects(true);
            SubjectGroupKey key = new SubjectGroupKey();
            key.setName(name);
            key.setType(type);
            query.setGroupKeys(Collections.singletonList(key));
            service.findSubjectGroups(query, new AsyncCallback<FindSubjectGroupsResponse>() {

                public void onFailure(Throwable arg0) {
                    view.error(arg0.getLocalizedMessage());
                }

                public void onSuccess(FindSubjectGroupsResponse response) {
                    if (response.getGroups() != null && response.getGroups().size() > 0) {
                        //copy the SubjectGroup to make it editable
                        originalGroup = new SubjectGroupImpl(response.getGroups().get(0));
                        view.setName(originalGroup.getName());
                        view.setDescription(originalGroup.getDescription());
                        view.setSelectedSubjects(originalGroup.getSubjects());                       
                        //Get the available subjects of that type

                        SubjectKey key = new SubjectKey();
                        String subName = view.getSearchTerm();
                        if (subName != null && !subName.trim().equals("")){
                            key.setName(subName);
                        }
                        key.setType(type);
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
        								    //remove all the subjects that are already selected
        	                                if (originalGroup.getSubjects() != null){
        	                                    names.removeAll(originalGroup.getSubjects());
        	                                }
        			                        view.setAvailableSubjects(names);

        								}

        							});

        				} else {
                        
	                        service.findSubjects(query, new AsyncCallback<FindSubjectsResponse> () {
	
	                            public void onFailure(Throwable arg0) {
	                                view.error(arg0.getMessage());
	                            }
	
	                            public void onSuccess(FindSubjectsResponse response) {
	                                List<Subject> subjects = response.getSubjects();
	                                List<String> names = new ArrayList<String>();
	                                if (subjects != null) {
	                                    for (Subject s:subjects)
	                                        names.add(s.getName());
	                                }
	                                //remove all the subjects that are already selected
	                                if (originalGroup.getSubjects() != null){
	                                    names.removeAll(originalGroup.getSubjects());
	                                }
	                                view.setAvailableSubjects(names);
	                            }
	                        });
        				}
                    }
                }
            });  
        }
    }
}
