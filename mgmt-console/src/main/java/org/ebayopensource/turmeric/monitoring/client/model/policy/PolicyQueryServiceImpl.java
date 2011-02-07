/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.AbstractConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreateSubjectGroupsResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectQuery.SubjectTypeKey;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * PolicyQueryServiceImpl
 * 
 */
public class PolicyQueryServiceImpl extends AbstractConsoleService implements
		PolicyQueryService {
	private static final String BASE_POLICY_URL = GWT.getModuleBaseURL()
			+ "policy";

	public PolicyQueryServiceImpl() {
		namespaces.put("ns1", SECURITY_NAMESPACE);
		namespaces.put("ns2", OASIS_NAMESPACE);
		serviceNameHeaderValue += "PolicyService";
	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#createSubjectGroups(java.util.List,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void createSubjectGroups(final List<SubjectGroup> groups,
			final AsyncCallback<CreateSubjectGroupsResponse> callback) {
		if (groups == null)
			return;

		String url = BASE_POLICY_URL
				+ "?"
				+ getPartialUrl("createSubjectGroups", namespaces,
						RequestFormat.NV);
		if (groups != null) {
			int i = 0;
			for (SubjectGroup g : groups) {
				url += (g.getName() == null ? "" : "&ns1:subjectGroups(" + i
						+ ").@SubjectGroupName=" + g.getName().trim());
				url += (g.getType() == null ? "" : "&ns1:subjectGroups(" + i
						+ ").@SubjectType=" + g.getType().toString());
				url += (g.getDescription() == null ? "" : "&ns1:subjectGroups("
						+ i + ").@Description=" + g.getDescription());

				// Subjects must have name && type?
				if (g.getSubjects() != null) {
					int j = 0;
					for (String s : g.getSubjects()) {
						url += "&ns1:subjectGroups(" + i + ").ns1:Subject(" + j
								+ ").@SubjectName=" + s;
						url += "&ns1:subjectGroups(" + i + ").ns1:Subject(" + j
								+ ").@SubjectType=" + g.getType().toString();
						j++;
					}
				}
				i++;
			}
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
					URL.encode(url));
			setSecurityHeaders(builder);
			try {
				builder.sendRequest(null, new RequestCallback() {

					public void onError(Request request, Throwable err) {
						callback.onFailure(err);
					}

					public void onResponseReceived(Request request,
							Response response) {
						if (response.getStatusCode() != Response.SC_OK) {
							callback.onFailure(new Throwable(
									ConsoleUtil.constants.error() + " "
											+ response.getStatusCode()));
						} else if (response.getHeader(ERROR_HEADER) != null) {
							callback.onFailure(getErrorAsThrowable(
									CreateSubjectGroupsResponseJS.NAME,
									response));
						} else {
							// convert response.getText() to JSON
							CreateSubjectGroupsResponse createResponse = CreateSubjectGroupsResponseJS
									.fromJSON(response.getText());
							if (createResponse.isErrored())
								callback.onFailure(getErrorAsThrowable(
										CreateSubjectGroupsResponseJS.NAME,
										response));
							else
								callback.onSuccess(createResponse);
						}
					}
				});
			} catch (RequestException x) {
				callback.onFailure(x);
			}
		}
	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#deletePolicy(org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeletePolicyRequest,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void deletePolicy(final PolicyKey key,
			final AsyncCallback<DeletePolicyResponse> callback) {
		if (key == null) {
			callback.onFailure(null);
		}

		String url = BASE_POLICY_URL + "?"
				+ getPartialUrl("deletePolicy", namespaces, RequestFormat.NV);
		url += (key.getId() == null ? "" : "&ns1:policyKey.ns1:policyId="
				+ key.getId().toString());
		url += (key.getName() == null ? "" : "&ns1:policyKey.ns1:policyName="
				+ key.getName().trim());
		url += (key.getType() == null ? "" : "&ns1:policyKey.ns1:policyType="
				+ key.getType().toString().toUpperCase());

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								DeletePolicyResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						DeletePolicyResponse deleteResponse = DeletePolicyResponseJS
								.fromJSON(response.getText());
						if (deleteResponse.isErrored())
							callback.onFailure(getErrorAsThrowable(
									DeletePolicyResponseJS.NAME, response));
						else
							callback.onSuccess(deleteResponse);
					}
				}
			});
		} catch (RequestException x) {
			callback.onFailure(x);
		}
	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#deleteResource(org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeleteResourceRequest,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void deleteResources(List<ResourceKey> keys,
			AsyncCallback<DeleteResourceResponse> callback) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#deleteSubjectGroup(org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DeleteSubjectGroupRequest,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void deleteSubjectGroups(final List<SubjectGroupKey> keys,
			final AsyncCallback<DeleteSubjectGroupResponse> callback) {
		if (keys == null) {
			return;
		}

		String url = BASE_POLICY_URL
				+ "?"
				+ getPartialUrl("deleteSubjectGroups", namespaces,
						RequestFormat.NV);
		int i = 0;
		for (SubjectGroupKey key : keys) {
			url += (key.getId() == null ? "" : "&ns1:subjectGroupKey(" + i
					+ ").ns1:subjectGroupId=" + key.getId().toString());
			url += (key.getName() == null ? "" : "&ns1:subjectGroupKey(" + i
					+ ").ns1:subjectGroupName=" + key.getName().trim());
			url += (key.getType() == null ? "" : "&ns1:subjectGroupKey(" + i
					+ ").ns1:subjectType=" + key.getType().toString());
			i++;
		}
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								DeleteSubjectGroupResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						DeleteSubjectGroupResponse delResponse = DeleteSubjectGroupResponseJS
								.fromJSON(response.getText());
						if (delResponse.isErrored())
							callback.onFailure(getErrorAsThrowable(
									DeleteSubjectGroupResponseJS.NAME, response));
						else
							callback.onSuccess(delResponse);
					}
				}
			});
		} catch (RequestException x) {
			callback.onFailure(x);
		}
	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#disablePolicy(org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.DisablePolicyRequest,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void disablePolicy(final PolicyKey key,
			final AsyncCallback<DisablePolicyResponse> callback) {
		if (key == null) {
			callback.onFailure(null);
		}

		String url = BASE_POLICY_URL + "?"
				+ getPartialUrl("disablePolicy", namespaces, RequestFormat.NV);
		url += (key.getId() == null ? "" : "&ns1:policyKey.ns1:policyId="
				+ key.getId().toString());
		url += (key.getName() == null ? "" : "&ns1:policyKey.ns1:policyName="
				+ key.getName().trim());
		url += (key.getType() == null ? "" : "&ns1:policyKey.ns1:policyType="
				+ key.getType().toString().toUpperCase());

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					// TODO
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								DisablePolicyResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						DisablePolicyResponse enableResponse = DisablePolicyResponseJS
								.fromJSON(response.getText());
						if (enableResponse.isErrored())
							callback.onFailure(getErrorAsThrowable(
									DisablePolicyResponseJS.NAME, response));
						else
							callback.onSuccess(enableResponse);
					}
				}
			});
		} catch (RequestException x) {
			callback.onFailure(x);
		}
	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#enablePolicy(org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.EnablePolicyRequest,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void enablePolicy(final PolicyKey key,
			final AsyncCallback<EnablePolicyResponse> callback) {
		if (key == null) {
			callback.onFailure(null);
		}

		String url = BASE_POLICY_URL + "?"
				+ getPartialUrl("enablePolicy", namespaces, RequestFormat.NV);
		url += (key.getId() == null ? "" : "&ns1:policyKey.ns1:policyId="
				+ key.getId().toString());
		url += (key.getName() == null ? "" : "&ns1:policyKey.ns1:policyName="
				+ key.getName().trim());
		url += (key.getType() == null ? "" : "&ns1:policyKey.ns1:policyType="
				+ key.getType().toString().toUpperCase());
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								EnablePolicyResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						EnablePolicyResponse enableResponse = EnablePolicyResponseJS
								.fromJSON(response.getText());
						if (enableResponse.isErrored())
							callback.onFailure(getErrorAsThrowable(
									EnablePolicyResponseJS.NAME, response));
						else
							callback.onSuccess(enableResponse);
					}
				}
			});
		} catch (RequestException x) {
			callback.onFailure(x);
		}
	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#getResources(org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetResourcesRequest,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void getResources(final List<ResourceKey> keys,
			final AsyncCallback<GetResourcesResponse> callback) {

		if (keys == null)
			return;

		String url = BASE_POLICY_URL + "?"
				+ getPartialUrl("getResources", namespaces, RequestFormat.NV);
		int i = 0;
		for (ResourceKey key : keys) {
			url += (key.getId() == null ? "" : "&ns1:resourceKey(" + i
					+ ").ns1:resourceId=" + key.getId().toString());
			url += (key.getName() == null || key.getName().trim().isEmpty() ? ""
					: "&ns1:resourceKey(" + i + ").ns1:resourceName="
							+ key.getName().trim());
			url += (key.getType() == null || key.getType().trim().isEmpty() ? ""
					: "&ns1:resourceKey(" + i + ").ns1:resourceType="
							+ key.getType().toString().toUpperCase());
			i++;
		}

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								GetResourcesResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						GetResourcesResponse resourcesResponse = GetResourcesResponseJS
								.fromJSON(response.getText());
						if (resourcesResponse.isErrored())
							callback.onFailure(getErrorAsThrowable(
									GetResourcesResponseJS.NAME, response));
						else
							callback.onSuccess(resourcesResponse);
					}
				}
			});

		} catch (RequestException x) {
			callback.onFailure(x);
		}
	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#getPoliciesByKey(org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyKey,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void findPolicies(Long sinceLastModifiedTime, List<PolicyKey> keys,
			List<ResourceKey> resKeys, List<OperationKey> opKeys,
			List<SubjectKey> subjectKeys,
			List<SubjectGroupKey> subjectGroupKeys,
			PolicyOutputSelector outputSelector, QueryCondition condition,
			final AsyncCallback<GetPoliciesResponse> callback) {

		String url = BASE_POLICY_URL + "?"
				+ getPartialUrl("findPolicies", namespaces, RequestFormat.NV);

		// sinceLastModifiedTime is measured as the milliseconds since the
		// epoch(GMT)
		// "YYYY-MM-DDThh:mm:ssZ" eg 2002-05-30T09:30:10Z
		if (sinceLastModifiedTime != null) {
			String time = ConsoleUtil.xsDateTimeFormat.format(new Date(
					sinceLastModifiedTime.longValue()));
			url += "&ns1:lastUpdatedDate=" + URL.encodeQueryString(time);
		}

		// find by a particular policy type
		// if(keys.size()==1){
		url = createPolicyKeyRequest(keys, url);
		// }else{
		// //find with all policy types
		// url = createPolicyKeyForAllRequest(keys, subjectGroupKeys, url);
		// }

		url = createResourceKeyRequest(resKeys, url);
		url = createOperationKeyRequest(opKeys, url);
		url = createSubjectKeyRequest(subjectKeys, url);
		url = createSubjectGroupKeyRequest(subjectGroupKeys, url);

		// is there an outputselector?
		if (outputSelector != null) {
			url += "&ns1:outputSelector=" + outputSelector.toString();
		}
		// is there a query condition?
		if (condition != null) {
			url += (condition.getResolution() == null ? ""
					: "&ns1:queryCondition.ns1:resolution="
							+ condition.getResolution().toString());
			if (condition.getQueries() != null) {
				int i = 0;
				for (QueryCondition.Query q : condition.getQueries()) {
					url += "&ns1:queryCondition.ns1:query(" + i
							+ ").ns1:QueryType=" + q.getType().toString();
					url += "&ns1:queryCondition.ns1:query(" + i
							+ ").ns1:QueryValue=" + q.getValue().toString();
				}
			}
		}
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								GetPoliciesResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						GetPoliciesResponse findResponse = GetPoliciesResponseJS
								.fromJSON(response.getText());
						if (findResponse.isErrored()) {
							callback.onFailure(getErrorAsThrowable(
									GetPoliciesResponseJS.NAME, response));
						} else
							callback.onSuccess(findResponse);
					}
				}
			});

		} catch (RequestException x) {
			callback.onFailure(x);
		}

	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#getEntityHistory(org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetEntityHistoryRequest,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void getEntityHistory(Long startDate, Long endDate,
			List<PolicyKey> polKeys, List<ResourceKey> resKeys,
			List<OperationKey> opKeys, List<SubjectKey> subjectKeys,
			List<SubjectGroupKey> subjectGroupKeys,
			final AsyncCallback<GetEntityHistoryResponse> callback) {

		if (startDate == null || endDate == null || startDate > endDate)
			return;

		String url = BASE_POLICY_URL
				+ "?"
				+ getPartialUrl("getEntityHistory", namespaces,
						RequestFormat.NV);

		// startDate is measured as the milliseconds since the epoch(GMT)
		// "YYYY-MM-DDThh:mm:ssZ" eg 2002-05-30T09:30:10Z
		String start = ConsoleUtil.xsDateTimeFormat.format(
				new Date(startDate.longValue())).trim();
		url += "&ns1:startDate=" + URL.encodeQueryString(start);

		String end = ConsoleUtil.xsDateTimeFormat.format(
				new Date(endDate.longValue())).trim();
		url += "&ns1:endDate=" + URL.encodeQueryString(end);
		url = createPolicyKeyRequest(polKeys, url);
		url = createResourceKeyRequest(resKeys, url);
		url = createOperationKeyRequest(opKeys, url);
		url = createSubjectKeyRequest(subjectKeys, url);
		url = createSubjectGroupKeyRequest(subjectGroupKeys, url);

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		setSecurityHeaders(builder);

		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								GetEntityHistoryResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						GetEntityHistoryResponse createResponse = GetEntityHistoryResponseJS
								.fromJSON(response.getText());
						if (createResponse.isErrored()) {
							callback.onFailure(getErrorAsThrowable(
									GetEntityHistoryResponseJS.NAME, response));
						} else
							callback.onSuccess(createResponse);
					}
				}
			});

		} catch (RequestException x) {
			callback.onFailure(x);
		}

	}

	/**
	 * Creates internal subjects based on external ones
	 * 
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.
	 *      PolicyQueryService
	 *      #createSubjects(List<org.ebayopensource.turmeric.monitoring
	 *      .client.model.policy.Subjects>,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void createSubjects(final List<Subject> subjects,
			final AsyncCallback<CreateSubjectsResponse> callback) {
		if (subjects == null) {
			return;
		}

		String url = BASE_POLICY_URL + "?"
				+ getPartialUrl("createSubjects", namespaces, RequestFormat.NV);

		url += SubjectsConverter.toNV(subjects);

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								CreateSubjectsResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						CreateSubjectsResponse createResponse = CreateSubjectsResponseJS
								.fromJSON(response.getText());
						if (createResponse.isErrored())
							callback.onFailure(getErrorAsThrowable(
									CreateSubjectsResponseJS.NAME, response));
						else
							callback.onSuccess(createResponse);
					}
				}
			});
		} catch (RequestException x) {
			callback.onFailure(x);
		}
	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#createPolicy(org.org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void createPolicy(final GenericPolicy policy,
			final AsyncCallback<CreatePolicyResponse> callback) {
		if (policy == null) {
			return;
		}

		String url = BASE_POLICY_URL + "?"
				+ getPartialUrl("createPolicy", namespaces, RequestFormat.NV);

		url += GenericPolicyConverter.toNV(policy);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								CreatePolicyResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						CreatePolicyResponse createResponse = CreatePolicyResponseJS
								.fromJSON(response.getText());
						if (createResponse.isErrored()) {
							callback.onFailure(getErrorAsThrowable(
									CreatePolicyResponseJS.NAME, response));
						} else
							callback.onSuccess(createResponse);
					}
				}
			});

		} catch (RequestException x) {
			callback.onFailure(x);
		}
	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#findSubjectGroups(org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupQuery,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void findSubjectGroups(final SubjectGroupQuery query,
			final AsyncCallback<FindSubjectGroupsResponse> callback) {
		if (query == null)
			return;

		String url = BASE_POLICY_URL
				+ "?"
				+ getPartialUrl("findSubjectGroups", namespaces,
						RequestFormat.NV);

		if (query.getGroupKeys() != null) {
			int i = 0;
			for (SubjectGroupKey key : query.getGroupKeys()) {
				url += (key.getType() == null
						|| "".equals(key.getType().trim()) ? ""
						: "&ns1:subjectGroupQuery.ns1:subjectGroupKey(" + i
								+ ").ns1:subjectType=" + key.getType());
				url += ((key.getName() == null || key.getName().trim()
						.isEmpty()) ? ""
						: "&ns1:subjectGroupQuery.ns1:subjectGroupKey(" + i
								+ ").ns1:subjectGroupName="
								+ key.getName().trim());
				url += (key.getId() == null ? ""
						: "&ns1:subjectGroupQuery.ns1:subjectGroupKey(" + i
								+ ").ns1:subjectGroupId="
								+ key.getId().toString());
				i++;
			}
		}

		url += (query.getQuery() == null ? ""
				: "&ns1:subjectGroupQuery.ns1:queryString=" + query.getQuery());
		url += "&ns1:subjectGroupQuery.ns1:includeSubjects="
				+ query.isIncludeSubjects();
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));

		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								FindSubjectGroupsResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						FindSubjectGroupsResponse findResponse = FindSubjectGroupsResponseJS
								.fromJSON(response.getText());
						if (findResponse.isErrored()) {
							callback.onFailure(getErrorAsThrowable(
									FindSubjectGroupsResponseJS.NAME, response));
						} else
							callback.onSuccess(findResponse);
					}
				}
			});

		} catch (RequestException x) {
			callback.onFailure(x);
		}

	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#findSubjects(org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectQuery,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void findSubjects(final SubjectQuery query,
			final AsyncCallback<FindSubjectsResponse> callback) {
		if (query == null)
			return;

		String url = BASE_POLICY_URL + "?"
				+ getPartialUrl("findSubjects", namespaces, RequestFormat.NV);
		if (query.getTypeKeys() != null) {
			int i = 0;
			for (SubjectTypeKey key : query.getTypeKeys()) {
				url += (key.getTypeId() == null ? ""
						: "&ns1:subjectQuery.ns1:subjectTypeKey(" + i
								+ ").ns1:subjectTypeId="
								+ key.getTypeId().toString());
				url += (key.getTypeName() == null ? ""
						: "&ns1:subjectQuery.ns1:subjectTypeKey(" + i
								+ ").ns1:subjectType=" + key.getTypeName());
				i++;
			}
		}

		if (query.getSubjectKeys() != null) {
			int i = 0;
			for (SubjectKey key : query.getSubjectKeys()) {
				url += (key.getType() == null ? ""
						: "&ns1:subjectQuery.ns1:subjectKey(" + i
								+ ").ns1:subjectType=" + key.getType());
				url += (key.getName() == null || key.getName().trim().isEmpty() ? ""
						: "&ns1:subjectQuery.ns1:subjectKey(" + i
								+ ").ns1:subjectName=" + key.getName().trim());
				url += (key.getId() == null ? ""
						: "&ns1:subjectQuery.ns1:subjectKey(" + i
								+ ").ns1:subjectId=" + key.getId().toString());
				i++;
			}
		}

		url += (query.getQuery() == null ? ""
				: "&ns1:subjectQuery.ns1:queryString=" + query.getQuery());

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								FindSubjectsResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						FindSubjectsResponse findResponse = FindSubjectsResponseJS
								.fromJSON(response.getText());
						if (findResponse.isErrored()) {
							callback.onFailure(getErrorAsThrowable(
									FindSubjectsResponseJS.NAME, response));
						} else
							callback.onSuccess(findResponse);
					}
				}
			});

		} catch (RequestException x) {
			callback.onFailure(x);
		}

	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#findExternalSubjects(org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectQuery,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void findExternalSubjects(SubjectQuery query,
			final AsyncCallback<FindExternalSubjectsResponse> callback) {
		if (query == null)
			return;

		String url = BASE_POLICY_URL
				+ "?"
				+ getPartialUrl("findExternalSubjects", namespaces,
						RequestFormat.NV);

		if (query.getTypeKeys() != null) {
			int i = 0;
			for (SubjectTypeKey key : query.getTypeKeys()) {
				url += (key.getTypeId() == null ? ""
						: "&ns1:subjectQuery.ns1:subjectTypeKey(" + i
								+ ").ns1:subjectTypeId="
								+ key.getTypeId().toString());
				url += (key.getTypeName() == null ? ""
						: "&ns1:subjectQuery.ns1:subjectTypeKey(" + i
								+ ").ns1:subjectType=" + key.getTypeName());
				i++;
			}
		}

		if (query.getSubjectKeys() != null) {
			int i = 0;
			for (SubjectKey key : query.getSubjectKeys()) {
				url += (key.getType() == null ? ""
						: "&ns1:subjectQuery.ns1:subjectKey(" + i
								+ ").ns1:subjectType=" + key.getType());
				url += (key.getName() == null || key.getName().trim().isEmpty() ? ""
						: "&ns1:subjectQuery.ns1:subjectKey(" + i
								+ ").ns1:subjectName=" + key.getName().trim());
				url += (key.getId() == null ? ""
						: "&ns1:subjectQuery.ns1:subjectKey(" + i
								+ ").ns1:subjectId=" + key.getId().toString());
				i++;
			}
		}

		url += (query.getQuery() == null ? ""
				: "&ns1:subjectQuery.ns1:queryString=" + query.getQuery());

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								FindExternalSubjectsResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						FindExternalSubjectsResponse findResponse = FindExternalSubjectsResponseJS
								.fromJSON(response.getText());
						if (findResponse.isErrored()) {
							callback.onFailure(getErrorAsThrowable(
									FindExternalSubjectsResponseJS.NAME,
									response));
						} else
							callback.onSuccess(findResponse);
					}
				}
			});

		} catch (RequestException x) {
			callback.onFailure(x);
		}
	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#getMetaData(org.ebayopensource.turmeric.monitoring.client.model.policy.QueryCondition,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void getMetaData(QueryCondition condition,
			final AsyncCallback<GetMetaDataResponse> callback) {
		if (condition == null)
			return;

		String url = BASE_POLICY_URL + "?"
				+ getPartialUrl("getMetaData", namespaces, RequestFormat.NV);
		url += (condition.getResolution() == null ? ""
				: "&ns1:queryCondition.ns1:resolution="
						+ condition.getResolution().toString());
		if (condition.getQueries() != null) {
			int i = 0;
			for (QueryCondition.Query q : condition.getQueries()) {
				url += "&ns1:queryCondition.ns1:query(" + i
						+ ").ns1:QueryType=" + q.getType().toString();
				url += "&ns1:queryCondition.ns1:query(" + i
						+ ").ns1:QueryValue=" + q.getValue().toString();
			}
		}
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								CreatePolicyResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						GetMetaDataResponse metaDataResponse = GetMetaDataResponseJS
								.fromJSON(response.getText());
						if (metaDataResponse.isErrored()) {
							callback.onFailure(getErrorAsThrowable(
									GetMetaDataResponseJS.NAME, response));
						} else
							callback.onSuccess(metaDataResponse);
					}
				}
			});

		} catch (RequestException x) {
			callback.onFailure(x);
		}
	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#updateSubjectGroups(java.util.List,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void updateSubjectGroups(List<SubjectGroup> groups, UpdateMode mode,
			final AsyncCallback<UpdateSubjectGroupsResponse> callback) {
		if (groups == null)
			return;
		String url = BASE_POLICY_URL
				+ "?"
				+ getPartialUrl("updateSubjectGroups", namespaces,
						RequestFormat.NV);
		if (groups != null) {
			int i = 0;
			for (SubjectGroup g : groups) {
				url += (g.getName() == null ? "" : "&ns1:subjectGroups(" + i
						+ ").@SubjectGroupName=" + g.getName().trim());
				url += (g.getType() == null ? "" : "&ns1:subjectGroups(" + i
						+ ").@SubjectType=" + g.getType().toString());
				url += (g.getDescription() == null ? "" : "&ns1:subjectGroups("
						+ i + ").@Description=" + g.getDescription());

				// TODO - what about all the other stuff
				// ApplyToEach
				// ApplyToAll

				// Subjects must have name && type?
				if (g.getSubjects() != null && g.getSubjects().size() > 0) {
					int j = 0;
					for (String s : g.getSubjects()) {
						url += "&ns1:subjectGroups(" + i + ").ns1:Subject(" + j
								+ ").@SubjectName=" + s;
						url += "&ns1:subjectGroups(" + i + ").ns1:Subject(" + j
								+ ").@SubjectType=" + g.getType().toString();
						j++;
					}
				}

				// SubjectMatch for the id. Optional: 0 or 1 element
				if (g.getSubjectMatchTypes() != null
						&& g.getSubjectMatchTypes().size() > 0) {
					int j = 0;
					for (SubjectMatchType sm : g.getSubjectMatchTypes()) {
						url += "&ns1:subjectGroups(" + i
								+ ").ns2:SubjectMatch(" + j + ").@MatchId="
								+ sm.getMatchId();
						url += "&ns1:subjectGroups(" + i
								+ ").ns2:SubjectMatch(" + j
								+ ").ns2:AttributeValue="
								+ sm.getAttributeValue();
						url += "&ns1:subjectGroups("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").ns2:SubjectAttributeDesignator.@AttributeId="
								+ sm.getSubjectAttributeDesignator();
						j++;
					}
				}

				i++;
			}

			// update mode
			url += (mode == null ? "" : "&ns1:updateMode=" + mode.toString());
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
					URL.encode(url));
			setSecurityHeaders(builder);
			try {
				builder.sendRequest(null, new RequestCallback() {

					public void onError(Request request, Throwable err) {
						callback.onFailure(err);
					}

					public void onResponseReceived(Request request,
							Response response) {
						if (response.getStatusCode() != Response.SC_OK) {
							callback.onFailure(new Throwable(
									ConsoleUtil.constants.error() + " "
											+ response.getStatusCode()));
						} else if (response.getHeader(ERROR_HEADER) != null) {
							callback.onFailure(getErrorAsThrowable(
									CreateSubjectGroupsResponseJS.NAME,
									response));
						} else {
							// convert response.getText() to JSON
							UpdateSubjectGroupsResponse updateResponse = UpdateSubjectGroupsResponseJS
									.fromJSON(response.getText());
							if (updateResponse.isErrored())
								callback.onFailure(getErrorAsThrowable(
										UpdateSubjectGroupsResponseJS.NAME,
										response));
							else
								callback.onSuccess(updateResponse);
						}
					}
				});
			} catch (RequestException x) {
				callback.onFailure(x);
			}
		}
	}

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService#updatePolicy(org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.UpdateMode,
	 *      org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy,
	 *      com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void updatePolicy(UpdateMode mode, GenericPolicy policy,
			final AsyncCallback<UpdatePolicyResponse> callback) {
		if (policy == null)
			return;

		String url = BASE_POLICY_URL + "?"
				+ getPartialUrl("updatePolicy", namespaces, RequestFormat.NV);

		// update mode
		url += (mode == null ? "" : "&ns1:updateMode=" + mode.toString());

		url += GenericPolicyConverter.toNV(policy);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		setSecurityHeaders(builder);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable err) {
					callback.onFailure(err);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						callback.onFailure(new Throwable(ConsoleUtil.constants
								.error() + " " + response.getStatusCode()));
					} else if (response.getHeader(ERROR_HEADER) != null) {
						callback.onFailure(getErrorAsThrowable(
								UpdatePolicyResponseJS.NAME, response));
					} else {
						// convert response.getText() to JSON
						UpdatePolicyResponse updateResponse = UpdatePolicyResponseJS
								.fromJSON(response.getText());
						if (updateResponse.isErrored())
							callback.onFailure(getErrorAsThrowable(
									UpdatePolicyResponseJS.NAME, response));
						else
							callback.onSuccess(updateResponse);
					}
				}
			});
		} catch (RequestException x) {
			callback.onFailure(x);
		}

	}

	private String createSubjectGroupKeyRequest(
			List<SubjectGroupKey> subjectGroupKeys, String url) {
		// subject group key is optional
		if (subjectGroupKeys != null && subjectGroupKeys.size() > 0) {
			int i = 0;
			for (SubjectGroupKey subjectGroupKey : subjectGroupKeys) {
				url += (subjectGroupKey.getType() == null
						|| "".equals(subjectGroupKey.getType().trim()) ? ""
						: "&ns1:subjectGroupKey(" + i + ").ns1:subjectType="
								+ subjectGroupKey.getType());
				url += (subjectGroupKey.getId() == null ? ""
						: "&ns1:subjectGroupKey(" + i + ").ns1:subjectGroupId="
								+ subjectGroupKey.getId());
				url += ((subjectGroupKey.getName() == null || subjectGroupKey
						.getName().trim().isEmpty()) ? ""
						: "&ns1:subjectGroupKey(" + i
								+ ").ns1:subjectGroupName="
								+ subjectGroupKey.getName().trim());
				i++;
			}
		}
		return url;
	}

	private String createSubjectKeyRequest(List<SubjectKey> subjectKeys,
			String url) {
		// subject key is optional
		if (subjectKeys != null && subjectKeys.size() > 0) {
			int i = 0;
			for (SubjectKey subjectKey : subjectKeys) {
				url += (subjectKey.getType() == null
						|| "".equals(subjectKey.getType().trim()) ? ""
						: "&ns1:subjectKey(" + i + ").ns1:subjectType="
								+ subjectKey.getType());
				url += (subjectKey.getId() == null ? "" : "&ns1:subjectKey("
						+ i + ").ns1:subjectId=" + subjectKey.getId());
				url += ((subjectKey.getName() == null || subjectKey.getName()
						.trim().isEmpty()) ? "" : "&ns1:subjectKey(" + i
						+ ").ns1:subjectName=" + subjectKey.getName().trim());

				i++;
			}
		}
		return url;
	}

	private String createOperationKeyRequest(List<OperationKey> opKeys,
			String url) {
		// operation key is optional
		if (opKeys != null && opKeys.size() > 0) {
			int i = 0;
			for (OperationKey opKey : opKeys) {
				url += (opKey.getOperationId() == null ? ""
						: "&ns1:operationKey(" + i + ").ns1:operationId="
								+ opKey.getOperationId());
				url += (opKey.getOperationName() == null || opKey
						.getOperationName().trim().isEmpty()) ? ""
						: "&ns1:operationKey(" + i + ").ns1:operationName="
								+ opKey.getOperationName().trim();
				url += (opKey.getResourceName() == null || opKey
						.getResourceName().trim().isEmpty()) ? ""
						: "&ns1:operationKey(" + i + ").ns1:resourceName="
								+ opKey.getResourceName().trim();
				url += (opKey.getResourceType() == null || opKey
						.getResourceType().trim().isEmpty()) ? ""
						: "&ns1:operationKey(" + i + ").ns1:resourceType="
								+ opKey.getResourceType();
				i++;
			}
		}
		return url;
	}

	private String createResourceKeyRequest(List<ResourceKey> resKeys,
			String url) {
		// resource key is optional
		if (resKeys != null && resKeys.size() > 0) {
			int i = 0;
			for (ResourceKey resKey : resKeys) {
				url += (resKey.getId() == null ? "" : "&ns1:resourceKey(" + i
						+ ").ns1:resourceId=" + resKey.getId());
				url += (resKey.getType() == null || resKey.getType().trim()
						.isEmpty()) ? "" : "&ns1:resourceKey(" + i
						+ ").ns1:resourceType=" + resKey.getType();
				url += (resKey.getName() == null || resKey.getName().trim()
						.isEmpty()) ? "" : "&ns1:resourceKey(" + i
						+ ").ns1:resourceName=" + resKey.getName().trim();
				i++;
			}
		}
		return url;
	}

	private String createPolicyKeyRequest(List<PolicyKey> keys, String url) {
		// at least one policy key must be present
		if (keys != null && keys.size() > 0) {
			int i = 0;
			for (PolicyKey key : keys) {
				url += (key.getType() == null
						|| "".equals(key.getType().trim()) ? ""
						: "&ns1:policyKey(" + i + ").ns1:policyType="
								+ key.getType());
				url += ((key.getId() == null) ? "" : "&ns1:policyKey(" + i
						+ ").ns1:policyId=" + key.getId());
				url += ((key.getName() == null || key.getName().trim()
						.isEmpty()) ? "" : "&ns1:policyKey(" + i
						+ ").ns1:policyName=" + key.getName().trim());

				i++;

			}
		}
		return url;
	}

}
