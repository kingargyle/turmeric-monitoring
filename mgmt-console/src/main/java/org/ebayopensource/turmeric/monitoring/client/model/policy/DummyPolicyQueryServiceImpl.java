/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreateSubjectsResponse;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class DummyPolicyQueryServiceImpl implements PolicyQueryService {
    private static long subjectGroupIdCounter = 0;
    
	private static Collection<GenericPolicyImpl> tmpPolicies;
	static {
		tmpPolicies = new ArrayList<GenericPolicyImpl>();
		GenericPolicyImpl policy = null;
		for (int i = 0; i < 12; i++) {
			policy = new GenericPolicyImpl();
			policy.setId(new Long(i));
			policy.setName("Policy_" + i);
			policy.setDescription("A description");
			policy.setCreatedBy("jose");
			policy.setType("BlackList");
			policy.setLastModifiedBy("jose");
			policy.setCreationDate(new Date(System.currentTimeMillis()));
			policy.setLastModified(new Date(System.currentTimeMillis()));
			policy.setEnabled(true);

			if (i == 0 || i == 3 || i == 4 || i == 10 || i == 11) {
				List<SubjectGroup> subjectGroups = new ArrayList<SubjectGroup>();
				SubjectGroupImpl sg = null;

				for (int j = 0; j < 2; j++) {
					sg = new SubjectGroupImpl();
					sg.setName("SG_" + i);

					subjectGroups.add(sg);
				}
				policy.setSubjectGroups(subjectGroups);
			}

			if (i == 2 || i == 5 || i == 6 || i == 8 || i == 10) {
				policy.setEnabled(false);
			}

			tmpPolicies.add(policy);
		}

	}

	
	public class CreateSubjectGroupsResponseImpl implements CreateSubjectGroupsResponse {

	    private List<Long> ids;
	    
	    public void setSubjectGroupIds(List<Long> ids) {
	        this.ids = ids;
	    }
        /**
         * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreateSubjectGroupsResponse#getSubjectGroupIds()
         */
        @Override
        public List<Long> getSubjectGroupIds() {
            return this.ids;
        }

        /**
         * @see org.ebayopensource.turmeric.monitoring.client.model.policy.BaseResponse#getErrorMessage()
         */
        @Override
        public String getErrorMessage() {
            return null;
        }

        /**
         * @see org.ebayopensource.turmeric.monitoring.client.model.policy.BaseResponse#isErrored()
         */
        @Override
        public boolean isErrored() {
            return false;
        }
	    
	}


	private GenericPolicyImpl createDummyPolicy(long id, String type, boolean status) {
		GenericPolicyImpl policy = new GenericPolicyImpl();
		policy.setId((long) id);
		policy.setName(type + "-" + id);
		policy.setDescription(type + "-DESC-" + id);
		policy.setType(type);
		long newDate = new Date().getTime() - 400000000 * id;
		policy.setCreationDate(new Date(newDate));
		policy.setCreatedBy(type + "-CREATOR-" + id);
		long updatedDate = new Date().getTime() - 100000 * id;
		policy.setLastModified(new Date(updatedDate));
		policy.setLastModifiedBy(type + "-MOD-" + id);
		policy.setEnabled(status);
		return policy;
	}
	/*

	public void getPolicy(GetPolicyRequest request,
			AsyncCallback<GetPolicyResponse> callback) {
		GetPolicyResponse response = new GetPolicyResponse();
		long id = new Date().getTime();
		GenericPolicy policy = new GenericPolicy();
		policy.setId(id);
		policy.setName("Policy" + id);
		policy.setDescription("Description" + id);
		policy.setType("AUTHZ");
		policy.setCreatedBy("Creator" + id);
		long newDate = new Date().getTime() - 100000 * 5;
		policy.setLastModified(new Date(newDate));
		policy.setLastModifiedBy("Modifier" + id);
		policy.setEnabled(true);

		// resources
		List<Resource> resources = new ArrayList<Resource>();
		for (int iter = 1; iter < 15; iter++) {
			Resource resource = new Resource();
			resource.setId((long) iter);
			resource.setResourceName("Resource" + iter);
			resource.setDescription("Description" + iter);
			resource.setResourceType("Resource Type" + iter);

			List<String> opNames = new ArrayList<String>();
			opNames.add("Operation_1");
			opNames.add("Operation_2");
			opNames.add("Operation_3");
			resource.setOpList(opNames);

			resources.add(resource);
		}
		policy.setResources(resources);

		response.setPolicy(policy);
		callback.onSuccess(response);
	}
*/
	
	public void getResources(List<ResourceKey> keys,
	                         AsyncCallback<GetResourcesResponse> callback) {

	    final Collection<Resource> result = new ArrayList<Resource>();
	    if (keys == null) {
	        for (int iter = 1; iter < 15; iter++) {
	            ResourceImpl resource = new ResourceImpl();
	            resource.setId((long) iter);
	            resource.setResourceName("Resource" + iter);
	            resource.setDescription("Description" + iter);
	            resource.setResourceType("Resource Type" + iter);

	            List<Operation> opList = new ArrayList<Operation>();
	            OperationImpl op = new OperationImpl();
	            op.setOperationName("Operation_1");
	            opList.add(op);
	            OperationImpl op2 = new OperationImpl();
	            op2.setOperationName("Operation_2");
	            opList.add(op2);
	            OperationImpl op3 = new OperationImpl();
	            op3.setOperationName("Operation_3");
	            opList.add(op3);
	            
	            resource.setOpList(opList);

	            result.add(resource);
	        }
	    } else {
	        for (ResourceKey key:keys) {
	            if ("GENERIC".equals(key.getType())) {
	                for (int iter = 1; iter < 4; iter++) {
	                    ResourceImpl resource = new ResourceImpl();
	                    resource.setId((long) iter);
	                    resource.setResourceName("Resource"
	                                             + "GENERIC"
	                                             + iter);
	                    resource.setDescription("Description" + iter);
	                    resource.setResourceType("GENERIC");

	                    List<Operation> opList = new ArrayList<Operation>();
	    	            OperationImpl op = new OperationImpl();
	    	            op.setOperationName("Operation_1");
	    	            opList.add(op);
	    	            OperationImpl op2 = new OperationImpl();
	    	            op2.setOperationName("Operation_2");
	    	            opList.add(op2);
	    	            OperationImpl op3 = new OperationImpl();
	    	            op3.setOperationName("Operation_3");
	    	            opList.add(op3);
	    	            
	    	            resource.setOpList(opList);

	                    result.add(resource);
	                }

	            } else if ("SERVICE".equals(key.getType())) {
	                for (int iter = 4; iter < 6; iter++) {
	                    ResourceImpl resource = new ResourceImpl();
	                    resource.setId((long) iter);
	                    resource.setResourceName("Resource"
	                                             + "SERVICE"
	                                             + iter);
	                    resource.setDescription("Description" + iter);
	                    resource.setResourceType("SERVICE");

	                    List<Operation> opList = new ArrayList<Operation>();
	    	            OperationImpl op = new OperationImpl();
	    	            op.setOperationName("Operation_1");
	    	            opList.add(op);
	    	            OperationImpl op2 = new OperationImpl();
	    	            op2.setOperationName("Operation_2");
	    	            opList.add(op2);
	    	            OperationImpl op3 = new OperationImpl();
	    	            op3.setOperationName("Operation_3");
	    	            opList.add(op3);
	    	            
	    	            resource.setOpList(opList);

	                    result.add(resource);
	                }

	            } else if ("URL".equals(key.getType())) {
	                for (int iter = 6; iter < 11; iter++) {
	                    ResourceImpl resource = new ResourceImpl();
	                    resource.setId((long) iter);
	                    resource.setResourceName("Resource"
	                                             + "URL" + iter);
	                    resource.setDescription("Description" + iter);
	                    resource.setResourceType("URL");

	                    List<Operation> opList = new ArrayList<Operation>();
	    	            OperationImpl op = new OperationImpl();
	    	            op.setOperationName("Operation_1");
	    	            opList.add(op);
	    	            OperationImpl op2 = new OperationImpl();
	    	            op2.setOperationName("Operation_2");
	    	            opList.add(op2);
	    	            OperationImpl op3 = new OperationImpl();
	    	            op3.setOperationName("Operation_3");
	    	            opList.add(op3);
	    	            
	    	            resource.setOpList(opList);

	                    result.add(resource);
	                }
	            } else if ("OBJECT".equals(key.getType())) {
	                for (int iter = 11; iter < 16; iter++) {
	                    ResourceImpl resource = new ResourceImpl();
	                    resource.setId((long) iter);
	                    resource.setResourceName("Resource"
	                                             + "OBJECT"
	                                             + iter);
	                    resource.setDescription("Description" + iter);
	                    resource.setResourceType("OBJECT");

	                    List<Operation> opList = new ArrayList<Operation>();
	    	            OperationImpl op = new OperationImpl();
	    	            op.setOperationName("Operation_1");
	    	            opList.add(op);
	    	            OperationImpl op2 = new OperationImpl();
	    	            op2.setOperationName("Operation_2");
	    	            opList.add(op2);
	    	            OperationImpl op3 = new OperationImpl();
	    	            op3.setOperationName("Operation_3");
	    	            opList.add(op3);
	    	            
	    	            resource.setOpList(opList);


	                    result.add(resource);
	                }
	            }
	        }
	    }
		
		
		GetResourcesResponse response = new GetResourcesResponse() {
            public Collection<Resource> getResources() {
                return result;
            }

            public String getErrorMessage() {
                return null;
            }

            public boolean isErrored() {
                return false;
            }
		};
		
		callback.onSuccess(response);
	}

	public void deleteResources(List<ResourceKey> keys,
	                           AsyncCallback<DeleteResourceResponse> callback) {
	    DeleteResourceResponse response = new DeleteResourceResponse() {
            @Override
            public String getErrorMessage() {
                return null;
            }

            @Override
            public boolean isErrored() {
                return false;
            }  
	    };
	    callback.onSuccess(response);
	}

	public void enablePolicy(PolicyKey key, AsyncCallback<EnablePolicyResponse> callback) {
	    EnablePolicyResponse response = new EnablePolicyResponse() {
	        @Override
	        public String getErrorMessage() {
	            return null;
	        }

	        @Override
	        public boolean isErrored() {
	            return false;
	        }
	    };
	    callback.onSuccess(response);
	}

	public void disablePolicy(PolicyKey key, AsyncCallback<DisablePolicyResponse> callback) {
	    DisablePolicyResponse response = new DisablePolicyResponse() {
	        @Override
	        public String getErrorMessage() {
	            return null;
	        }

	        @Override
	        public boolean isErrored() {
	            return false;
	        }
	    };
	    callback.onSuccess(response);
	}

	/*
	public void submitTicketTraceForPolicy(
			SubmitTicketTraceForPolicyRequest request,
			AsyncCallback<SubmitTicketTraceForPolicyResponse> callback) {
		SubmitTicketTraceForPolicyResponse response = new SubmitTicketTraceForPolicyResponse();
		response.setResult(Boolean.TRUE);
		callback.onSuccess(response);
	}
	*/

	public void deletePolicy(PolicyKey key, AsyncCallback<DeletePolicyResponse> callback) {
		DeletePolicyResponse response = new DeletePolicyResponse() 
		{
            @Override
            public Boolean isSuccess() {
                return Boolean.TRUE;
            }

            @Override
            public String getErrorMessage() {
                return null;
            }

            @Override
            public boolean isErrored() {
                return false;
            }
		    
		};
		callback.onSuccess(response);
	}

	/*
	public void deployPolicy(SubmitTicketTraceForPolicyRequest request,
			AsyncCallback<SubmitTicketTraceForPolicyResponse> callback) {
		SubmitTicketTraceForPolicyResponse response = new SubmitTicketTraceForPolicyResponse();
		response.setResult(Boolean.TRUE);
		callback.onSuccess(response);
	}
	 */
	@Override
	public void findPolicies(Long sinceLastModifiedTime,
	                  List<PolicyKey> keys, 
	                  List<ResourceKey> resKeys,
	                  List<OperationKey> opKeys,
	                  List<SubjectKey> subjectKeys,
	                  List<SubjectGroupKey> subjectGroupKeys,
	                  PolicyOutputSelector outputSelector, 
	                  QueryCondition condition, 
	                  AsyncCallback<GetPoliciesResponse> callback) {
	    
	    if (keys == null)
	        return;

	    //Only honour a single key for each list

	    //get by matching subjectgroup
	    if (subjectGroupKeys != null) {
	        final Collection<GenericPolicy> policies = new ArrayList<GenericPolicy>();

	        //search by name & type
	        if (subjectGroupKeys.get(0).getName() != null) {
                //crappy pattern match - assume wildcards at head and tail
                String name = subjectGroupKeys.get(0).getName().replace('%', ' ').trim();
                for (GenericPolicyImpl genericPolicy : tmpPolicies) {
                    List<SubjectGroup> subjectGroups = genericPolicy.getSubjectGroups();
                    if (subjectGroups != null) {

                        for (SubjectGroup subjectGroup : subjectGroups) {
                            if (subjectGroup.getName().toLowerCase().contains(name.toLowerCase())
                                    && subjectGroup.getType()==subjectGroupKeys.get(0).getType()) {
                                policies.add(genericPolicy);
                                break;
                            }
                        }
                    }
                }
            } else {
                //search by exact id
                for (GenericPolicyImpl genericPolicy : tmpPolicies) {
                    if (genericPolicy.getId().longValue()==keys.get(0).getId())
                        policies.add(genericPolicy);
                }
            }

            GetPoliciesResponse response = new GetPoliciesResponse() {
                @Override
                public String getErrorMessage() {
                    return null;
                }

                @Override
                public boolean isErrored() {
                    return false;
                }

                @Override
                public Collection<GenericPolicy> getPolicies() {
                    return policies;
                }
            };

            callback.onSuccess(response);
            return;
        }

        //get by matching policy info
        final Collection<GenericPolicy> policies = new ArrayList<GenericPolicy>();
        if (keys.get(0).getName() != null) {
            //really rubbish pattern match - just get rid of leading/trailing wildcard
            String name = keys.get(0).getName().replace('%', ' ').trim();

            for (GenericPolicyImpl genericPolicy : tmpPolicies) {
                if (genericPolicy.getName().toLowerCase().contains(name))
                    policies.add(genericPolicy);
            }
        }   else if (keys.get(0).getId() !=null ) {
            for (GenericPolicyImpl genericPolicy : tmpPolicies) {
                if (genericPolicy.getId().longValue() == keys.get(0).getId())
                    policies.add(genericPolicy);
            }
        }

        GetPoliciesResponse response = new GetPoliciesResponse() {   
            @Override
            public String getErrorMessage() {
                return null;
            }

            @Override
            public boolean isErrored() {
                return false;
            }

            @Override
            public Collection<GenericPolicy> getPolicies() {
                return policies;
            }
        };

        callback.onSuccess(response);
        return;
	}

	


	@Override
	public void deleteSubjectGroups(List<SubjectGroupKey> keys,
			AsyncCallback<DeleteSubjectGroupResponse> callback) {
		
		//TODO needs to verify if no policy uses them
		DeleteSubjectGroupResponse response = new DeleteSubjectGroupResponse() {
            @Override
            public String getErrorMessage() {
                return null;
            }
            @Override
            public boolean isErrored() {
                return false;
            }
		};
		
		callback.onSuccess(response);
	}

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#createSubjectGroups(java.util.List, com.google.gwt.user.client.rpc.AsyncCallback)
     */
	@Override
	public void createSubjectGroups(List<SubjectGroup> groups,
	                                AsyncCallback<CreateSubjectGroupsResponse> callback) {
	    List<Long> ids = new ArrayList<Long>();
	    CreateSubjectGroupsResponseImpl impl = new CreateSubjectGroupsResponseImpl();
	    if (groups != null) {
	        for (SubjectGroup g:groups) {
	            ids.add(new Long(subjectGroupIdCounter++));
	        }
	    }
	    impl.setSubjectGroupIds(ids);
	    callback.onSuccess(impl);
	}

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#createPolicy(org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void createPolicy(GenericPolicy policy,
                             AsyncCallback<CreatePolicyResponse> callback) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#findSubjectGroups(org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupQuery, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void findSubjectGroups(
                                  SubjectGroupQuery query,
                                  AsyncCallback<FindSubjectGroupsResponse> callback) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#findSubjects(org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectQuery, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void findSubjects(SubjectQuery query,
                             AsyncCallback<FindSubjectsResponse> callback) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#getMetaData(org.ebayopensource.turmeric.monitoring.client.model.policy.QueryCondition, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void getMetaData(QueryCondition condition,
                            AsyncCallback<GetMetaDataResponse> callback) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#updateSubjectGroups(java.util.List, org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdateMode, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void updateSubjectGroups(
                                    List<SubjectGroup> groups,
                                    UpdateMode mode,
                                    AsyncCallback<UpdateSubjectGroupsResponse> callback) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#updatePolicy(org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdateMode, org.ebayopensource.turmeric.monitoring.client.model.GenericPolicy, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void updatePolicy(UpdateMode mode, GenericPolicy policy,
                             AsyncCallback<UpdatePolicyResponse> callback) {
        // TODO Auto-generated method stub
        
    }

	@Override
	public void getEntityHistory(Long startDate, Long endDate,
			List<PolicyKey> polKeys, List<ResourceKey> resKeys,
			List<OperationKey> opKeys, List<SubjectKey> subjectKeys,
			List<SubjectGroupKey> subjectGroupKeys,
			AsyncCallback<GetEntityHistoryResponse> callback) {
		// TODO Auto-generated method stub
		
	}

	public void findExternalSubjects(SubjectQuery query,
			AsyncCallback<FindExternalSubjectsResponse> callback) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Creates internal subjects based on external ones
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#createSubjects(List<org.ebayopensource.turmeric.monitoring.client.model.policy.Subjects>, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void createSubjects(List<Subject> subject,
			AsyncCallback<CreateSubjectsResponse> callback) {
		// TODO Auto-generated method stub
		
	}
}
