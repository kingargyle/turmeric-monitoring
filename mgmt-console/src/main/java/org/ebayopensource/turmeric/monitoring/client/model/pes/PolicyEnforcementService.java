/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.pes;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.policy.OperationKey;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * PolicyEnforcementService
 *
 */
public interface PolicyEnforcementService {
    
    public static final String SUBJECT_GROUP_DELETE_RESOURCE = "SERVICE.PolicyService.deleteSubjectGroups";
    public static final String SUBJECT_GROUP_EDIT_RESOURCE = "SERVICE.PolicyService.updateSubjectGroups";
    public static final String SUBJECT_GROUP_DELETE_OPERATION_NAME = "deleteSubjectGroups";
    public static final String SUBJECT_GROUP_OPERATION_NAME = "updateSubjectGroups";
    public static final String POLICY_DELETE_RESOURCE = "SERVICE.PolicyService.deletePolicy";
    public static final String POLICY_EDIT_RESOURCE = "SERVICE.PolicyService.updatePolicy";
    public static final String POLICY_DELETE_OPERATION_NAME = "deletePolicy";
    public static final String POLICY_EDIT_OPERATION_NAME = "updatePolicy";
    public static final String POLICY_SERVICE_NAME = "PolicyService";
    
    public void verify (OperationKey opKey,
                        List<String> policyTypes,
                        Map<String,String> credentials,
                        List<String[]> subjectTypes,
                        Map<String,String> extendedInfo,
                        List<String> accessControlObjects,
                        String resourceType,
                        AsyncCallback<VerifyAccessResponse> callback);
    
    
    public interface VerifyAccessResponse {
        //TODO add other data access methods if necessary
        public boolean isErrored();
    }

}
