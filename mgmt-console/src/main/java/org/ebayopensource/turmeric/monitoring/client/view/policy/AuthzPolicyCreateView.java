/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.policy;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.model.policy.ExtraField;

public class AuthzPolicyCreateView extends PolicyCreateView {

	protected static  UserAction SELECTED_ACTION = UserAction.AUTHZ_POLICY_CREATE;
	private static final String TITLE_FORM= ConsoleUtil.policyAdminConstants.policyInformationAuthzCreate();
	

	@Override
	public String getTitleForm(){
		return TITLE_FORM;
	}

	@Override
	public UserAction getSelectedAction(){
		return SELECTED_ACTION;
	}

	



    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.Display#getAssociatedId()
     */
    @Override
    public String getAssociatedId() {
        // TODO Auto-generated method stub
        return null;
    }


    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.Display#setAssociatedId(java.lang.String)
     */
    @Override
    public void setAssociatedId(String id) {
        // TODO Auto-generated method stub
        
    }

	@Override
	protected void initializeExtraFields() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolicyDesc(String policyDesc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolicyName(String policyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExtraFieldList(List<ExtraField> extraFieldList) {
		// TODO Auto-generated method stub
		
	}



}

