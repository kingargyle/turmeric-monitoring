/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.AbstractConsoleService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * PolicyEnforcementServiceImpl
 *
 */
public class PolicyEnforcementServiceImpl  extends AbstractConsoleService implements PolicyEnforcementService {

    private static final String BASE_POLICY_URL =  GWT.getModuleBaseURL()+"policyEnforcement";

    public PolicyEnforcementServiceImpl () {    
        namespaces.put("ns1", SECURITY_NAMESPACE);
        serviceNameHeaderValue += "PolicyEnforcementService";
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyEnforcementService#verify(org.ebayopensource.turmeric.monitoring.client.model.policy.OperationKey, java.util.List, java.util.Map, java.util.List, java.util.Map, java.util.List, java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void verify(OperationKey opKey, List<String> policyTypes,
                       Map<String, String> credentials,
                       List<String[]> subjectTypes,
                       Map<String, String> extendedInfo,
                       List<String> accessControlObjects, String resourceType,
                       final AsyncCallback<VerifyAccessResponse> callback) {

        String url = BASE_POLICY_URL + "?"+getPartialUrl("verifyAccess", namespaces, RequestFormat.NV);

        //encode body of the request
        if (opKey != null) {
            url += OperationKeyConverter.toNV(opKey);

            if (policyTypes != null) {
                int i=0;
                for (String pt: policyTypes) {
                    url += "&ns1:policyType("+i+")="+pt;
                    i++;
                }
            }

            if (credentials != null) {
                int i=0;
                for (Map.Entry<String, String> e:credentials.entrySet()) {
                    url += "&ns1:credential("+i+").ns1:key="+e.getKey();
                    url += "&ns1:credential("+i+").ns1:value="+e.getValue();
                    i++;
                }
            }

            if (subjectTypes != null) {
                int i=0;
                for (String[] st:subjectTypes) {
                    if (st != null) {
                        url += (st[0]==null?"":"&ns1:subject("+i+").ns1:domain="+st[0]);
                        url += (st[1]==null?"":"&ns1:subject("+i+").ns1:value="+st[1]);
                    }
                    i++;
                }
            }

            if (extendedInfo != null) {
                int i=0;
                for (Map.Entry<String, String> e:extendedInfo.entrySet()) {
                    url += "&ns1:extendedInfo("+i+").ns1:key="+e.getKey();
                    url += "&ns1:extendedInfo("+i+").ns1:value="+e.getValue();
                    i++;
                }
            }


            if (accessControlObjects != null) {
                int i=0;
                for (String s:accessControlObjects) {
                    url += "&ns1:accessControlObject("+i+")="+s;
                    i++;
                }
            }

            if (resourceType != null) {
                url += "&ns1:resourceType="+resourceType;
            }
            
          
            final RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
            
            setSecurityHeaders(builder);
            try {
                builder.sendRequest(null, new RequestCallback() {

                    public void onError(Request request, Throwable err) {
                        callback.onFailure(err);
                    }

                    public void onResponseReceived(Request request, Response response) {
          
                        System.err.println("PolicyEnforcementService request="+builder.getUrl());
                        System.err.println("PolicyEnforcementService response="+response.getText());
                        if (response.getStatusCode() != Response.SC_OK) {
                            callback.onFailure(new Throwable(ConsoleUtil.constants.error()+" "+response.getStatusCode()));
                        } else if (response.getHeader(ERROR_HEADER) != null) {
                            callback.onFailure(getErrorAsThrowable(VerifyAccessResponseJS.NAME, response));
                        } else {
                            //convert response.getText() to JSON
                            VerifyAccessResponse accessResponse = VerifyAccessResponseJS.fromJSON(response.getText());
                            callback.onSuccess(accessResponse);
                        } 
                    }
                });
            } catch (RequestException x) {
                callback.onFailure(x);
            }
        }
    }
}
