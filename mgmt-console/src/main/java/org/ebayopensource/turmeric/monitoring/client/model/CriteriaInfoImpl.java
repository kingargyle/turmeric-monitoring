/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

public class CriteriaInfoImpl implements CriteriaInfo {
    protected String serviceConsumerType;
    protected String serviceName;
    protected String operationName;
    protected String consumerName;
    protected String machineName;
    protected String poolName;
    protected String metricName;
    protected String roleType;

    public String getServiceConsumerType() {
        return serviceConsumerType;
    }

    public void setServiceConsumerType(String serviceConsumerType) {
        this.serviceConsumerType = serviceConsumerType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

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