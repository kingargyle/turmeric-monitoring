/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

/**
 * The Class CriteriaInfoImpl.
 */
public class CriteriaInfoImpl implements CriteriaInfo {
    
    /** The service consumer type. */
    protected String serviceConsumerType;
    
    /** The service name. */
    protected String serviceName;
    
    /** The operation name. */
    protected String operationName;
    
    /** The consumer name. */
    protected String consumerName;
    
    /** The machine name. */
    protected String machineName;
    
    /** The pool name. */
    protected String poolName;
    
    /** The metric name. */
    protected String metricName;
    
    /** The role type. */
    protected String roleType;

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo#getServiceConsumerType()
     */
    public String getServiceConsumerType() {
        return serviceConsumerType;
    }

    /**
     * Sets the service consumer type.
     *
     * @param serviceConsumerType the new service consumer type
     */
    public void setServiceConsumerType(String serviceConsumerType) {
        this.serviceConsumerType = serviceConsumerType;
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo#getServiceName()
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Sets the service name.
     *
     * @param serviceName the new service name
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo#getOperationName()
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

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo#getConsumerName()
     */
    public String getConsumerName() {
        return consumerName;
    }

    /**
     * Sets the consumer name.
     *
     * @param consumerName the new consumer name
     */
    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo#getMachineName()
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * Sets the machine name.
     *
     * @param machineName the new machine name
     */
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo#getPoolName()
     */
    public String getPoolName() {
        return poolName;
    }

    /**
     * Sets the pool name.
     *
     * @param poolName the new pool name
     */
    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo#getMetricName()
     */
    public String getMetricName() {
        return metricName;
    }

    /**
     * Sets the metric name.
     *
     * @param metricName the new metric name
     */
    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo#getRoleType()
     */
    public String getRoleType() {
        return roleType;
    }

    /**
     * Sets the role type.
     *
     * @param roleType the new role type
     */
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo#asRestUrl()
     */
    @Override
    public String asRestUrl() {
        StringBuilder url = new StringBuilder("");
        if(serviceConsumerType != null)
        url.append("&ns:criteriaInfo.ns:serviceConsumerType=").append(serviceConsumerType);
        if(metricName != null)
        url.append("&ns:criteriaInfo.ns:metricName=").append(metricName);
        if(serviceName != null)
        url.append("&ns:criteriaInfo.ns:serviceName=").append(serviceName);
        if(operationName != null)
        url.append("&ns:criteriaInfo.ns:operationName=").append(operationName);
        if(consumerName != null)
        url.append("&ns:criteriaInfo.ns:consumerName=").append(consumerName);
        if(poolName != null)
        url.append("&ns:criteriaInfo.ns:poolName=").append(poolName);
        if(machineName != null)
        url.append("&ns:criteriaInfo.ns:machineName=").append(machineName);
        if(roleType != null)
        url.append("&ns:criteriaInfo.ns:roleType=").append(roleType);

        return url.toString();

    }

}