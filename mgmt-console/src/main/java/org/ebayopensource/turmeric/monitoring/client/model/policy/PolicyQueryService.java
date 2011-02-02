/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PolicyQueryService extends ConsoleService {

    public enum ResourceLevel {
        OPERATION, RESOURCE, GLOBAL
    };
    
	public enum PolicyOutputSelector {
	    ALL, RESOURCES, SUBJECTS, RULES, SUBJECTGROUPS
	}
	
	public enum UpdateMode {
	    REPLACE, UPDATE, DELETE
	}
	
	public enum RuleEffectType {
	    Allow, Flag, Challenge, Block, Softlimit
	}
	
	void createPolicy(GenericPolicy policy, AsyncCallback<CreatePolicyResponse> callback);
	
	void createSubjects(List<Subject> subject, AsyncCallback<CreateSubjectsResponse> callback);
	
	void createSubjectGroups (List<SubjectGroup> groups, AsyncCallback<CreateSubjectGroupsResponse> callback);

	void enablePolicy(PolicyKey key, AsyncCallback<EnablePolicyResponse> callback);

	void disablePolicy(PolicyKey key, AsyncCallback<DisablePolicyResponse> callback);

	void deletePolicy(PolicyKey key, AsyncCallback<DeletePolicyResponse> callback);

	void getResources(List<ResourceKey> keys, AsyncCallback<GetResourcesResponse> callback);

	void getEntityHistory(Long startDate, Long endDate,
            List<PolicyKey> polKeys, 
            List<ResourceKey> resKeys,
            List<OperationKey> opKeys,
            List<SubjectKey> subjectKeys,
            List<SubjectGroupKey> subjectGroupKeys, 
            AsyncCallback<GetEntityHistoryResponse> callback);
	
	void deleteSubjectGroups(List<SubjectGroupKey> keys, AsyncCallback<DeleteSubjectGroupResponse> callback);

	void findPolicies(Long sinceLastModifiedTime,
	                  List<PolicyKey> keys, 
	                  List<ResourceKey> resKeys,
	                  List<OperationKey> opKeys,
	                  List<SubjectKey> subjectKeys,
	                  List<SubjectGroupKey> subjectGroupKeys,
	                  PolicyOutputSelector outputSelector, 
	                  QueryCondition condition, 
	                  AsyncCallback<GetPoliciesResponse> callback);


	void deleteResources (List<ResourceKey> keys, AsyncCallback<DeleteResourceResponse> callback);
	
	void findSubjectGroups (SubjectGroupQuery query, AsyncCallback<FindSubjectGroupsResponse> callback);
	
	void findSubjects (SubjectQuery query, AsyncCallback<FindSubjectsResponse> callback);
	
	void findExternalSubjects(SubjectQuery query, AsyncCallback<FindExternalSubjectsResponse> callback);

	void getMetaData (QueryCondition condition, AsyncCallback<GetMetaDataResponse> callback);
	
	void updateSubjectGroups (List<SubjectGroup> groups, UpdateMode mode, AsyncCallback<UpdateSubjectGroupsResponse> callback);
	
	void updatePolicy (UpdateMode mode, GenericPolicy policy, AsyncCallback<UpdatePolicyResponse> callback);

	public interface GetResourcesResponse {
	    public Collection<Resource> getResources();
	    public boolean isErrored();
	    public String getErrorMessage();
	}

	public interface GetEntityHistoryResponse {
	    public Collection<EntityHistory> getEntities();
	    public boolean isErrored();
	    public String getErrorMessage();
	}

	public interface GetPoliciesResponse {
	    public Collection<GenericPolicy> getPolicies();
	    public boolean isErrored();
	    public String getErrorMessage();
	}

	public interface EnablePolicyResponse  {
	    public boolean isErrored();
	    public String getErrorMessage();
	}  

	public interface DisablePolicyResponse  {
	    public boolean isErrored();
	    public String getErrorMessage();
	}

	public interface DeletePolicyResponse {
		public Boolean isSuccess();       
		public boolean isErrored();
        public String getErrorMessage();
	}

	public interface DeleteSubjectGroupResponse {
	    public boolean isErrored();
	    public String getErrorMessage();
	}

	public interface DeleteResourceResponse {
	    public boolean isErrored();
	    public String getErrorMessage();
	}

	public interface CreateSubjectGroupsResponse {
	    public List<Long> getSubjectGroupIds();
	    public boolean isErrored();
        public String getErrorMessage();
	}

	public interface CreatePolicyResponse {
	    public Long getPolicyId();
	    public boolean isErrored();
	    public String getErrorMessage();
	}

	public interface CreateSubjectsResponse {
	    public List<Long> getSubjectIds();
	    public boolean isErrored();
	    public String getErrorMessage();
	}

	public interface FindSubjectGroupsResponse {
	    public List<SubjectGroup> getGroups();
	    public boolean isErrored();
	    public String getErrorMessage();
	}

	public interface FindSubjectsResponse {
	    public List<Subject> getSubjects();
	    public boolean isErrored();
	    public String getErrorMessage();
	}
	
	public interface FindExternalSubjectsResponse {
	    public List<Subject> getSubjects();
	    public boolean isErrored();
	    public String getErrorMessage();
	}

	public interface GetMetaDataResponse {
	    public Map<String,String> getValues();
	    public boolean isErrored();
	    public String getErrorMessage();
	}

	public interface UpdateSubjectGroupsResponse {
	    public boolean isErrored();
	    public String getErrorMessage();
	}

	public interface UpdatePolicyResponse { 
	    public boolean isErrored();
	    public String getErrorMessage();
	}
}
