/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreatePolicyResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreateSubjectGroupsResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeletePolicyResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeleteSubjectGroupResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DisablePolicyResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.EnablePolicyResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectGroupsResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.FindSubjectsResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetPoliciesResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetResourcesResponse;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * PolicyServiceImplTest
 *
 */
public class PolicyServiceImplTest {
    PolicyQueryService service;
    
    public PolicyServiceImplTest (PolicyQueryService service) {
        this.service = service;
    }

    public void test () {
        
        testResources();
        //testGetPoliciesByPolicyKey();
        //testFindSubjects();
        //testFindSubjectGroups();
        //testCreatePolicy();
        //testCreateSubjectGroups();
        //testDeletePolicy();
        //testEnablePolicy();
        //testDisablePolicy();
        //testDeleteSubjectGroups();
    }
    
    /**
     * 
     */
    public  void testGetPoliciesByPolicyKey() {
        PolicyKey key = new PolicyKey();
        key.setType("BLACKLIST");
        key.setName("Goofy");
        key.setId(new Long(9));
        QueryCondition condition = new QueryCondition();
        condition.addQuery(new QueryCondition.Query(QueryCondition.ActivePoliciesOnlyValue.FALSE));
        service.findPolicies(null, Collections.singletonList(key),null,null, null, null, null, condition, new AsyncCallback<GetPoliciesResponse>() {
            public void onFailure(Throwable arg0) {
                GWT.log("getPolicies failed "+arg0);
            }
   
            public void onSuccess(GetPoliciesResponse arg0) {
               
                GWT.log("getPolicies succeeded: "+arg0.getPolicies().size());
            }
            
        });
    }

    public void testResources () {
        ResourceKey key = new ResourceKey();
        key.setType("SERVICE");
        
        service.getResources(Collections.singletonList(key), new AsyncCallback<GetResourcesResponse>() {

            @Override
            public void onFailure(Throwable arg0) {
                GWT.log("getResources failed "+arg0);
            }

            @Override
            public void onSuccess(GetResourcesResponse arg0) {
               
                GWT.log("getResources succeeded: "+arg0.getResources().size());
            }
            
        });
    }
    
    
    public void testFindSubjects () {
        
        SubjectQuery query = new SubjectQuery();
        SubjectKey key = new SubjectKey();
        key.setType("USER");
        key.setName("%foo%");
        query.setSubjectKeys(Collections.singletonList(key));
        
        service.findSubjects(query, new AsyncCallback<FindSubjectsResponse>() {
            @Override
            public void onFailure(Throwable arg0) {
                GWT.log("findSubjects failed "+arg0);
            }

            @Override
            public void onSuccess(FindSubjectsResponse arg0) {
               
                GWT.log("findSubjects succeeded: "+arg0.getSubjects().size());
            }
        });
    }
    
    public void testFindSubjectGroups() {
        SubjectGroupQuery query = new SubjectGroupQuery();
        SubjectGroupKey key = new SubjectGroupKey();
        key.setName("%foo%");
        key.setType("APP");
        query.setGroupKeys(Collections.singletonList(key));
        
        service.findSubjectGroups(query, new AsyncCallback<FindSubjectGroupsResponse>() {
            @Override
            public void onFailure(Throwable arg0) {
                GWT.log("findSubjectGroups failed "+arg0);
            }

            @Override
            public void onSuccess(FindSubjectGroupsResponse arg0) {
               
                GWT.log("findSubjectGroups succeeded: "+arg0.getGroups().size());
            }
        });
    }
    
    public void testCreatePolicy () {
        GenericPolicyImpl policy = new GenericPolicyImpl();
        policy.setType("BLACKLIST");
        policy.setName("Honda");
        policy.setDescription("FeeFiFoFum");
        
        service.createPolicy(policy, new AsyncCallback<CreatePolicyResponse>() {
            @Override
            public void onFailure(Throwable arg0) {
                GWT.log("createPolicy failed "+arg0);
            }

            @Override
            public void onSuccess(CreatePolicyResponse arg0) {
               
                GWT.log("createPolicy succeeded: "+arg0.getPolicyId());
            }
        });
    }
    
    public void testCreateSubjectGroups() {
        
        List<SubjectGroup> groups = new ArrayList<SubjectGroup>();
        SubjectGroupImpl g = new SubjectGroupImpl();
        g.setType("USER");
        g.setName("FooGroupABCDEF");
        g.setDescription("Babblebabblebabble");
       // g.setSubjects(Arrays.asList(new String[] {"Sub1", "Sub2"}));
        groups.add(g);
        
        service.createSubjectGroups(groups, new AsyncCallback<CreateSubjectGroupsResponse>() {
            @Override
            public void onFailure(Throwable arg0) {
                GWT.log("createsubjectgroup failed "+arg0);
            }

            @Override
            public void onSuccess(CreateSubjectGroupsResponse arg0) {
               
                GWT.log("createsubjectgroup succeeded: "+arg0.getSubjectGroupIds().size());
            }
        });
    }
    
    
    public void testDeletePolicy() {
        PolicyKey key = new PolicyKey();
        key.setId(new Long(100));
        key.setName("FooPolicy");
        key.setType("BLACKLIST");
        
        service.deletePolicy(key, new AsyncCallback<DeletePolicyResponse>() {
            @Override
            public void onFailure(Throwable arg0) {
                GWT.log("deletePolicy failed "+arg0);
            }

            @Override
            public void onSuccess(DeletePolicyResponse arg0) {
               
                GWT.log("deletePolicy succeeded: "+arg0.isSuccess());
            }
        });
    }
    public void testDisablePolicy() {
        PolicyKey key = new PolicyKey();
        key.setId(new Long(100));
        key.setName("FooPolicy");
        key.setType("BLACKLIST");
        
        service.disablePolicy(key, new AsyncCallback<DisablePolicyResponse>() {
            @Override
            public void onFailure(Throwable arg0) {
                GWT.log("disablePolicy failed "+arg0);
            }

            @Override
            public void onSuccess(DisablePolicyResponse arg0) {
               
                GWT.log("disablePolicy succeeded: ");
            }
        });
    }
    
    public void testEnablePolicy() {
        PolicyKey key = new PolicyKey();
        key.setId(new Long(100));
        key.setName("FooPolicy");
        key.setType("BLACKLIST");
        
        service.enablePolicy(key, new AsyncCallback<EnablePolicyResponse>() {
            @Override
            public void onFailure(Throwable arg0) {
                GWT.log("enablePolicy failed "+arg0);
            }

            @Override
            public void onSuccess(EnablePolicyResponse arg0) {
               
                GWT.log("enablePolicy succeeded: ");
            }
        });
    }
    
    
    public void testDeleteSubjectGroups () {
        
        List<SubjectGroupKey> keys = new ArrayList<SubjectGroupKey>();
        SubjectGroupKey key = new SubjectGroupKey();
        key.setId(new Long(100));
        key.setName("FooGroup");
        key.setType("USER");
        keys.add(key);
        service.deleteSubjectGroups(keys, new AsyncCallback<DeleteSubjectGroupResponse>() {
            @Override
            public void onFailure(Throwable arg0) {
                GWT.log("deletesubjectgroups failed "+arg0);
            }

            @Override
            public void onSuccess(DeleteSubjectGroupResponse arg0) {
               
                GWT.log("deletesubjectgroups succeeded: ");
            }
        });
    }
    
}
