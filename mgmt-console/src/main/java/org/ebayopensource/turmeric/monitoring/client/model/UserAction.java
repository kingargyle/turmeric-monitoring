/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

public enum UserAction {
	UNKNOWN,
	
	// specific to main menu
	CONSOLE_MAIN,
	POLICY_MAIN,
	METRICS_MAIN,
	
	// specific to policy admin ui
	SUBJECT_GROUP_SUMMARY,
	SUBJECT_GROUP_CREATE,
	SUBJECT_GROUP_IMPORT,
	POLICY_SUMMARY,
	POLICY_IMPORT,
	AUTHZ_POLICY_CREATE,
	RL_POLICY_CREATE,
	BL_POLICY_CREATE,
	WL_POLICY_CREATE,
	CHANGE_HISTORY_SUMMARY,
	BL_POLICY_EDIT,
	AUTHZ_POLICY_EDIT,
	RL_POLICY_EDIT,
	WL_POLICY_EDIT,
	
	// other permissions to policy admin ui
	POLICY_VIEW,
	POLICY_ENABLE,
	POLICY_DISABLE,
	POLICY_SUBMIT_TICKET_TRACE,
	POLICY_EDIT,
	POLICY_DELETE,
	POLICY_EXPORT, 
	
	//permissions for Subject Groups
	SUBJECT_GROUP_VIEW,
	SUBJECT_GROUP_EDIT,
	SUBJECT_GROUP_DELETE,
	SUBJECT_GROUP_EXPORT,
	
	RESOURCE_SUMMARY,
	RESOURCE_VIEW,
	RESOURCE_EDIT, 
	RESOURCE_DELETE
	
}