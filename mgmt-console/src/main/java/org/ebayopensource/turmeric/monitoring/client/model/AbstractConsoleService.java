/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.Map;
import java.util.TreeMap;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

/**
 * AbstractConsoleService
 *
 */
public class AbstractConsoleService implements ConsoleService {
    public static final String SECURITY_NAMESPACE = "http://www.ebayopensource.org/turmeric/security/v1/services";
    public static final String OASIS_NAMESPACE = "urn:oasis:names:tc:xacml:2.0:policy:schema:os";
    public static final String COMMON_NAMESPACE = "http://www.ebayopensource.org/turmeric/common/v1/types";
    public static enum RequestFormat  {JSON, NV};
    protected final Map<String,String> namespaces = new TreeMap<String, String>();
    protected String serviceNameHeaderValue=SERVICE_NAME_HEADER+"=";
    
    
    public String getPartialUrl(String operation, Map<String,String> namespaces, RequestFormat format) {
        String url = "";

        url += serviceNameHeaderValue;
        url += "&"+OPERATION_NAME_HEADER+"="+operation;
        url += "&"+USECASE_HEADER_VALUE;
        switch (format) {
            case JSON:
                url += "&"+JSON_DATA_FORMAT_HEADER_VALUE;
                break;
            case NV:
                url += "&"+NV_DATA_FORMAT_HEADER_VALUE;
                break;
        }
       
        url += "&"+JSON_RESPONSE_FORMAT_HEADER_VALUE;

        if (namespaces != null && RequestFormat.NV == format) {
            for (String s:namespaces.keySet()) {
                url += "&nvns:"+s+"="+namespaces.get(s);
            }
        }
        return url;
    }
    

    public void setSecurityHeaders (RequestBuilder requestBuilder) {
       
    }
    
    
    
    public Throwable getErrorAsThrowable (String responseName, Response response) {
        if (response == null)
            return null;
        
        //try parsing the json as an errorMessage
        ErrorResponse errorResponse = ErrorResponse.fromJSON(response.getText());
        JsArray<RemoteError> errors = errorResponse.getErrors();
        if (errors == null || errors.length()==0)
            errors = errorResponse.getServiceErrors(responseName);

        if (errors == null)
            return new Throwable(ConsoleUtil.constants.error()+" "+response.getStatusCode());
        String s = "";
        for (int i=0;i<errors.length();i++) {
            RemoteError re = errors.get(i);
            s += re.getCategory()+":"+re.getSeverity()+":"+re.getMessage()+" ";
        }

        if (!"".equals(s))
            return new Throwable(s);
        return new Throwable(ConsoleUtil.messages.badRequestData());
    }
    
    public Throwable getErrorAsThrowable (Response response) {
        if (response == null)
            return null;
        
        //try parsing the json as an errorMessage
        ErrorResponse errorResponse = ErrorResponse.fromJSON(response.getText());
        JsArray<RemoteError> errors = errorResponse.getErrors();
        if (errors == null) {
            return new Throwable(ConsoleUtil.constants.error()+" "+response.getStatusCode());
        }
        String s = "";
        for (int i=0;i<errors.length();i++) {
            RemoteError re = errors.get(i);
            s += re.getCategory()+":"+re.getSeverity()+":"+re.getMessage()+" ";
        }

        if (!"".equals(s))
            return new Throwable(s);
        return new Throwable(ConsoleUtil.messages.badRequestData());
    }
    
}
