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
 * ErrorCriteria.
 */
public class ErrorCriteria {
    
    /** The error type. */
    final public ErrorType errorType;
    
    /** The service names. */
    final public List<String> serviceNames;
    
    /** The operation names. */
    final public List<String> operationNames; 
    
    /** The consumer names. */
    final public List<String> consumerNames;
    
    /** The e. */
    final public String e;
    
    /** The is id. */
    final public boolean isId;
    
    /** The category. */
    final public ErrorCategory category;
    
    /** The severity. */
    final public ErrorSeverity severity;
    
    /**
     * New error criteria.
     *
     * @param errorType the error type
     * @param serviceNames the service names
     * @param operationNames the operation names
     * @param consumerNames the consumer names
     * @param e the e
     * @param isId the is id
     * @param category the category
     * @param severity the severity
     * @return the error criteria
     */
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
    
    

    /**
     * As rest url.
     *
     * @return the string
     */
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
        
        if (operationNames != null && !operationNames.isEmpty()) {
            int i=0;
            for (String s:operationNames) {
                url += "&ns:operationName("+(i++)+")="+s;
            }
        }
        
        if (consumerNames != null && !consumerNames.isEmpty()) {
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
