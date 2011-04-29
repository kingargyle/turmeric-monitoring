/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

/**
 * The Enum UserAction.
 */
public enum UserAction {
	
	/** The UNKNOWN. */
	UNKNOWN,
	
	// specific to main menu
	/** The CONSOL e_ main. */
	CONSOLE_MAIN,
	
	/** The POLIC y_ main. */
	POLICY_MAIN,
	
	/** The METRIC s_ main. */
	METRICS_MAIN,
	
	// specific to policy admin ui
	/** The SUBJEC t_ grou p_ summary. */
	SUBJECT_GROUP_SUMMARY,
	
	/** The SUBJEC t_ grou p_ create. */
	SUBJECT_GROUP_CREATE,
	
	/** The SUBJEC t_ grou p_ import. */
	SUBJECT_GROUP_IMPORT,
	
	/** The POLIC y_ summary. */
	POLICY_SUMMARY,
	
	/** The POLIC y_ import. */
	POLICY_IMPORT,
	
	/** The AUTH z_ polic y_ create. */
	AUTHZ_POLICY_CREATE,
	
	/** The R l_ polic y_ create. */
	RL_POLICY_CREATE,
	
	/** The B l_ polic y_ create. */
	BL_POLICY_CREATE,
	
	/** The W l_ polic y_ create. */
	WL_POLICY_CREATE,
	
	/** The CHANG e_ histor y_ summary. */
	CHANGE_HISTORY_SUMMARY,
	
	/** The B l_ polic y_ edit. */
	BL_POLICY_EDIT,
	
	/** The AUTH z_ polic y_ edit. */
	AUTHZ_POLICY_EDIT,
	
	/** The R l_ polic y_ edit. */
	RL_POLICY_EDIT,
	
	/** The W l_ polic y_ edit. */
	WL_POLICY_EDIT,
	
	// other permissions to policy admin ui
	/** The POLIC y_ view. */
	POLICY_VIEW,
	
	/** The POLIC y_ enable. */
	POLICY_ENABLE,
	
	/** The POLIC y_ disable. */
	POLICY_DISABLE,
	
	/** The POLIC y_ submi t_ ticke t_ trace. */
	POLICY_SUBMIT_TICKET_TRACE,
	
	/** The POLIC y_ edit. */
	POLICY_EDIT,
	
	/** The POLIC y_ delete. */
	POLICY_DELETE,
	
	/** The POLIC y_ export. */
	POLICY_EXPORT, 
	
	//permissions for Subject Groups
	/** The SUBJEC t_ grou p_ view. */
	SUBJECT_GROUP_VIEW,
	
	/** The SUBJEC t_ grou p_ edit. */
	SUBJECT_GROUP_EDIT,
	
	/** The SUBJEC t_ grou p_ delete. */
	SUBJECT_GROUP_DELETE,
	
	/** The SUBJEC t_ grou p_ export. */
	SUBJECT_GROUP_EXPORT,
	
	/** The RESOURC e_ summary. */
	RESOURCE_SUMMARY,
	
	/** The RESOURC e_ view. */
	RESOURCE_VIEW,
	
	/** The RESOURC e_ edit. */
	RESOURCE_EDIT, 
	
	/** The RESOURC e_ delete. */
	RESOURCE_DELETE
	
}