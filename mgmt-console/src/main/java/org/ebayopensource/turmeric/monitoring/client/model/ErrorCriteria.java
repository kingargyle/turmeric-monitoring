/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorCategory;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorType;

/**
 * ErrorCriteria
 *
 */
public class ErrorCriteria {
    final public ErrorType errorType;
    final public List<String> serviceNames;
    final public List<String> operationNames; 
    final public List<String> consumerNames;
    final public String e;
    final public boolean isId;
    final public ErrorCategory category;
    final public ErrorSeverity severity;
    
    public static ErrorCriteria newErrorCriteria (ErrorType errorType,
                                                  List<String> serviceNames,
                                                  List<String> operationNames,
                                                  List<String> consumerNames,
                                                  String e,
                                                  boolean isId,
                                                  ErrorCategory category,
                                                  ErrorSeverity severity) {
        return new ErrorCriteria(errorType, serviceNames, operationNames, consumerNames, e, isId, category, severity);
        
    }
    
    private ErrorCriteria (ErrorType errorType,
                           List<String> serviceNames,
                           List<String> operationNames,
                           List<String> consumerNames,
                           String e,
                           boolean isId,
                           ErrorCategory category,
                           ErrorSeverity severity) {
        this.errorType = errorType;
        this.serviceNames = (serviceNames==null?null:new ArrayList<String>(serviceNames));
        this.operationNames = (operationNames==null?null:new ArrayList<String>(operationNames));
        this.consumerNames = (consumerNames==null?null:new ArrayList<String>(consumerNames));
        this.e = e;
        this.isId = isId;
        this.category = category;
        this.severity = severity;
    }
    
    

    public String asRestUrl () {
        String url = "";
        if (errorType == null)
            url +="&ns:errorType="+ErrorType.Category.toString();
        else
            url +="&ns:errorType="+errorType.toString();

        if (serviceNames != null) {
            int i=0;
            for (String s:serviceNames) {
                url += "&ns:serviceName("+(i++)+")="+s;
            }
        }
        
        if (operationNames != null) {
            int i=0;
            for (String s:operationNames) {
                url += "&ns:operationName("+(i++)+")="+s;
            }
        }
        
        if (consumerNames != null ) {
            int i=0;
            for (String s:consumerNames) {
                url += "&ns:consumerName("+(i++)+")="+s;
            }
        }

        if (e != null) {
            if (isId) 
                url +="&ns:errorId="+e;
            else 
                url +="&ns:errorName="+e;
        }

        if (category != null)
            url += "&ns:errorCategory="+category.toString();
        if (severity != null)
            url += "&ns:errorSeverity="+severity.toString();
        return url;
    }   
}
