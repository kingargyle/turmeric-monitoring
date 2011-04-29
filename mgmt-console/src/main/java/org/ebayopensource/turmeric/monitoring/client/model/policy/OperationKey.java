/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

/**
 * OperationKey.
 */
public class OperationKey {
    
    private Long operationId;
    private String operationName;
    private String resourceName;
    private String resourceType;
    
    /**
     * Gets the operation id.
     *
     * @return the operation id
     */
    public Long getOperationId() {
        return operationId;
    }
    
    /**
     * Sets the operation id.
     *
     * @param operationId the new operation id
     */
    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }
    
    /**
     * Gets the operation name.
     *
     * @return the operation name
     */
    public String getOperationName() {
        return operationName;
    }
    
    /**
     * Sets the operation name.
     *
     * @param operationName the new operation name
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
    
    /**
     * Gets the resource name.
     *
     * @return the resource name
     */
    public String getResourceName() {
        return resourceName;
    }
    
    /**
     * Sets the resource name.
     *
     * @param resourceName the new resource name
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    /**
     * Gets the resource type.
     *
     * @return the resource type
     */
    public String getResourceType() {
        return resourceType;
    }
    
    /**
     * Sets the resource type.
     *
     * @param resourceType the new resource type
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

}
