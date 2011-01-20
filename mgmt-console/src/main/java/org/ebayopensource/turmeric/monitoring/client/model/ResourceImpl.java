/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.policy.Operation;

/**
 * ResourceImpl
 *
 */
public class ResourceImpl implements Resource {

    private Long id;
    private String resourceType;
    private String resourceName;
    private String description;
    private List<Operation> opList;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getResourceType() {
        return resourceType;
    }
    public void setResourceType(final String resourceType) {
        this.resourceType = resourceType;
    }
    public String getResourceName() {
        return resourceName;
    }
    public void setResourceName(final String resourceName) {
        this.resourceName = resourceName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(final String description) {
        this.description = description;
    }
    public void setOpList(final List<Operation> opList) {
        this.opList = opList;
    }
    public List<Operation> getOpList() {
        return opList;
    }
}
