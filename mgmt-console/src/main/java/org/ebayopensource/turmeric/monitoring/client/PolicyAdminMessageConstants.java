/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client;

import com.google.gwt.i18n.client.Messages;

public interface PolicyAdminMessageConstants extends Messages {
    
    @DefaultMessage("The operation was not successful")
    public String operationUnsuccessful();
    
	@DefaultMessage("Request failed. Please try again later.")
	public String failedOperationMessage();
	
	@DefaultMessage("Request has been successfully sent.")
	public String successfulOperationMessage();
	
	@DefaultMessage("Name field is required")
	public String nameFieldMessage();
	
	@DefaultMessage("At least one Subject is required")
	public String minimumSubjectsMessage();
	
	@DefaultMessage("Do you wish to enable this policy : \"{0}\" ?")
	public String enablePolicyConfirmMessage(String policyName);
	
	@DefaultMessage("Do you wish to enable all selected policies ?")
	public String enablePoliciesConfirmMessage();
	
	@DefaultMessage("Do you wish to disable all selected policies ?")
	public String disablePoliciesConfirmMessage();
	
	@DefaultMessage("Do you wish to delete all selected policies ?")
	public String deletePoliciesConfirmMessage();
	
	@DefaultMessage("Do you wish to export all selected policies ?")
	public String exportPoliciesConfirmMessage();
	@DefaultMessage("Do you wish to disable this policy : \"{0}\" ?")
	public String disablePolicyConfirmMessage(String policyName);
		
	@DefaultMessage("Do you wish to delete this policy : \"{0}\" ?")
	public String deletePolicyConfirmMessage(String policyName);
	
	@DefaultMessage("Select an application")
	public String selectAnApplication ();
}
