/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Constants.DefaultStringValue;

public interface PolicyAdminConstants extends Constants {
    
    @DefaultStringValue("Subject")
    public String subjectCriteria();

    @DefaultStringValue("Subject Group")
    public String subjectGroupCriteria();
    
    @DefaultStringValue("Policy")
    public String policyCriteria();

    @DefaultStringValue("Resource")
    public String resourceCriteria();
    
    @DefaultStringValue("Subjects and Subject Groups")
    public String subjectsAndSubjectGroups();
    
    @DefaultStringValue("Assign Subjects and Subject Groups")
    public String assignSubjectsAndSubjectGroups();
    
    @DefaultStringValue("Assign Resources")
    public String assignResources();
    
    @DefaultStringValue("Condition Builder")
    public String conditionBuilder();
     
    @DefaultStringValue("Search")
    public String search();
    
    @DefaultStringValue("Search Groups")
    public String searchGroups();
    
    @DefaultStringValue("Search Subjects")
    public String searchSubjects();

    @DefaultStringValue("Available Operations")
    public String availableOperations();

    @DefaultStringValue("Selected Operations")
    public String selectedOperations();
    
    
    @DefaultStringValue("Available Subjects")
    public String availableSubjects();
    
    @DefaultStringValue("Available Subject Groups")
    public String availableSubjectGroups();
    
	@DefaultStringValue("Subject Group")
	public String subjectGroup();
	
	@DefaultStringValue("Subject Groups")
	public String subjectGroups();
	
	@DefaultStringValue("Selected Subject Groups")
	public String selectedSubjectGroups();
	
	@DefaultStringValue("Selected Exclusion Subject")
	public String selectedExclusionSubject();

	@DefaultStringValue("Selected Exclusion Subject Groups")
	public String selectedExclusionSG();
	
	@DefaultStringValue("Subject Type")
	public String subjectType();

	@DefaultStringValue("Subject Name")
	public String subjectName();
	
	@DefaultStringValue ("Subject Group Description")
	public String subjectGroupDescription();
	
	@DefaultStringValue ("Subject Group Name")
	public String subjectGroupName();
	
	@DefaultStringValue ("Selected Subjects")
	public String selectedSubjects();
	
	@DefaultStringValue ("Subjects")
	public String subjects();
	
	@DefaultStringValue ("Exclusion Subjects")
	public String exclusionSubjects();
	
	@DefaultStringValue ("Exclusion Subject Groups")
	public String exclusionSubjectGroups();
	
	@DefaultStringValue ("Subjects Assigned")
	public String subjectsAssigned();
	
	@DefaultStringValue ("Policies Assigned")
	public String policiesAssigned();
	
	@DefaultStringValue ("Created By")
	public String createdBy();

	@DefaultStringValue ("Last Modified By")
	public String lastModifiedBy();

	@DefaultStringValue ("Last Modified Time")
	public String lastModifiedTime();
	
	@DefaultStringValue("Create Time")
	public String createTime();

	@DefaultStringValue ("Actions")
	public String actions();
	
	@DefaultStringValue("Create")
	public String create();
	
	@DefaultStringValue("Import")
	public String importAction();
	
	@DefaultStringValue("All Policies")
	public String allPolicies();
	
	@DefaultStringValue("Summary")
	public String summary();
	
	@DefaultStringValue("Authorization Policy")
	public String authzPolicy();
	
	@DefaultStringValue("Rate Limiting Policy")
	public String rateLimitingPolicy();
	
	@DefaultStringValue("Blacklist Policy")
	public String blPolicy();
	
	@DefaultStringValue("Whitelist Policy")
	public String wlPolicy();
	
	@DefaultStringValue("Change History")
	public String changeHistory();

	@DefaultStringValue("Policy Name")
	public String policyName();
	
	@DefaultStringValue("Policy Description")
	public String policyDescription();
	
	@DefaultStringValue("Policy Type")
	public String policyType();

	@DefaultStringValue("Policy Based Email Address")
	public String policyBasedEmailAddress();

	@DefaultStringValue("Subject Based Email Address")
	public String subjectBasedEmailAddress();

	@DefaultStringValue("Effect Duration")
	public String effectDuration();
	
	@DefaultStringValue("Rollover Period")
	public String rollovelPeriod();

	@DefaultStringValue("Priority")
	public String priority();
	
	@DefaultStringValue("Effect")
	public String effect();
	
	@DefaultStringValue("Condition")
	public String condition();
	
	@DefaultStringValue("(secs)")
	public String fieldUnitSecs();
	
	@DefaultStringValue("Status")
	public String status();
	
	@DefaultStringValue("View")
	public String view();
	
	@DefaultStringValue("Enable")
	public String enable();
	
	@DefaultStringValue("Disable")
	public String disable();
	
	@DefaultStringValue("Submit a Trace Ticket")
	public String submitTraceTicket();
	
	@DefaultStringValue("Delete")
	public String delete();
	
	@DefaultStringValue("Export")
	public String export();
	
	@DefaultStringValue("Confirm")
	public String confirm();
	
	@DefaultStringValue("Cancel")
	public String cancel();
	
	@DefaultStringValue("Close")
	public String close();

	@DefaultStringValue("Save")
	public String save();

    @DefaultStringValue("Edit")
    public String edit();
    
    @DefaultStringValue("Add")
    public String add();
    
    @DefaultStringValue("Editing Authorization Policy")
    public String policyInformationAuthzEdit();

    @DefaultStringValue("New Authorization Policy")
    public String policyInformationAuthzCreate();

    @DefaultStringValue("Editing BlackList Policy")
    public String policyInformationBLEdit();

    @DefaultStringValue("New Blacklist Policy")
    public String policyInformationBLCreate();
    
    @DefaultStringValue("New Whitelist Policy")
    public String policyInformationWLCreate();

    @DefaultStringValue("Editing WhiteList Policy")
    public String policyInformationWLEdit();

    @DefaultStringValue("New Rate Limiting Policy")
    public String policyInformationRLCreate();

    @DefaultStringValue("Editing Rate Limiting Policy")
    public String policyInformationRLEdit();
    
    @DefaultStringValue("View Policy")
    public String policyInformationView();
    
	@DefaultStringValue("Resources")
    public String resources();
 
	@DefaultStringValue("Service")
    public String service();

	@DefaultStringValue("Resource Name")
    public String resourceName();
    
    @DefaultStringValue("Operation Name")
    public String operationName();
    
    @DefaultStringValue("Resource Type")
    public String resourceType();
    
    @DefaultStringValue("Level")
    public String resourceLevel();
    
    @DefaultStringValue("Assign Another Resource")
    public String resourceAssignAnotherResources();
    
    @DefaultStringValue("Operations")
    public String operations();
 
    @DefaultStringValue("Must select a searching criteria")
    public String searchCriteriaInvalid();
    
    @DefaultStringValue("Cannot delete a subject group that is assigned to a policy")
    public String deleteSubjetGroupInvalid();
 
    @DefaultStringValue("All fields must be populated")
    public String populateAllFiedls();
    
    @DefaultStringValue("Description")
    public String description();
    
    @DefaultStringValue("Policies")
    public String policies();

    @DefaultStringValue("Policy Administration")
    public String policyAdministration();
 
    /*
     * Entity History View
     */
    @DefaultStringValue("When")
    public String ehWhenColumn();

    @DefaultStringValue("Who")
    public String ehWhoColumn();

    @DefaultStringValue("Source IP")
    public String ehIPColumn();

    @DefaultStringValue("Type of Change")
    public String ehChangeTypeColumn();

    @DefaultStringValue("Comments")
    public String ehCommentsColumn();

    /*
     * Generic Error message. This message should never be shown  unless 
     * that the error message received from server be improperly set 
     */
    @DefaultStringValue("Operation not valid. Please contact your site administrator.")
    public String genericErrorMessage();
  
}
