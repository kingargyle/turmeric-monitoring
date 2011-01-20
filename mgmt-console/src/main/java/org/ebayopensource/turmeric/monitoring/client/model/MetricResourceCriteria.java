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

import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Entity;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.EntityName;



/**
 * MetricResourceCriteria
 *
 */
public class MetricResourceCriteria {
    final public List<ResourceEntityRequest> resourceEntityRequests;
    final public Entity resourceEntityResponseType;
  
    public static MetricResourceCriteria newMetricResourceCriteria(final List<EntityName> requestEntityNames,
                                                                   final Entity returnType) {
        MetricResourceCriteria rc = new MetricResourceCriteria(requestEntityNames, returnType);
        return rc;
    }
    
    
    private MetricResourceCriteria (final List<EntityName> requestEntityNames,
                                    final Entity returnType) {
        if (requestEntityNames != null) {
            this.resourceEntityRequests = new ArrayList<ResourceEntityRequest>();
            // the entities being queried, eg Service, Operation etc
            for (EntityName requestEntity : requestEntityNames) {
                ResourceEntityRequest r = new ResourceEntityRequest();
                r.resourceEntityType = requestEntity.type;
                // the names relevant to the type of entity
                if (requestEntity.names != null) {
                    for (String n : requestEntity.names)
                        r.addResourceEntityName(n);
                }
                this.resourceEntityRequests.add(r);
            }
        } else
            this.resourceEntityRequests = null;
        this.resourceEntityResponseType = returnType;
    }
    
    public String asRestUrl () {
        String url="";
        url +="&ns:metricResourceCriteria.ns:resourceEntityResponseType="+resourceEntityResponseType.toString();
        
        if (resourceEntityRequests != null) {
            int i=0;
            for (ResourceEntityRequest r:resourceEntityRequests) {
                if (r.resourceEntityType != null)
                    url +="&ns:metricResourceCriteria.ns:resourceRequestEntities("+(i)+").ns:resourceEntityType="+r.resourceEntityType.toString();
                
                if (r.resourceEntityNames != null) {
                    int j=0;
                    for (String n:r.resourceEntityNames) 
                        url +="&ns:metricResourceCriteria.ns:resourceRequestEntities("+(i)+").ns:resourceEntityName("+(j++)+")="+n;
                }
                i++;
            }
            
        }
        
        return url;
    }
}