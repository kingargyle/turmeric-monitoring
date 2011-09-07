/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.cassandra.storage.model;

import static org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider.KEY_SEPARATOR;

/**
 * The Class MetricId.
 */
public class MetricIdentifier {

    /** The metric name. */
    private String metricName;

    /** The service admin name. */
    private String serviceAdminName;

    /** The operation name. */
    private String operationName;

    private boolean serverSide;

    /**
     * Instantiates a new metric id.
     */
    public MetricIdentifier() {

    }

    /**
     * Instantiates a new metric id.
     * 
     * @param metricName
     *            the metric name
     * @param serviceAdminName
     *            the service admin name
     * @param operationName
     *            the operation name
     */
    public MetricIdentifier(String metricName, String serviceAdminName, String operationName, boolean serverSide) {
        super();
        this.metricName = metricName;
        this.serviceAdminName = serviceAdminName;
        this.operationName = operationName;
        this.serverSide = serverSide;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((metricName == null) ? 0 : metricName.hashCode());
        result = prime * result + ((operationName == null) ? 0 : operationName.hashCode());
        result = prime * result + ((serviceAdminName == null) ? 0 : serviceAdminName.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MetricIdentifier other = (MetricIdentifier) obj;
        if (metricName == null) {
            if (other.metricName != null)
                return false;
        }
        else if (!metricName.equals(other.metricName))
            return false;
        if (operationName == null) {
            if (other.operationName != null)
                return false;
        }
        else if (!operationName.equals(other.operationName))
            return false;
        if (serviceAdminName == null) {
            if (other.serviceAdminName != null)
                return false;
        }
        else if (!serviceAdminName.equals(other.serviceAdminName))
            return false;
        return true;
    }

    /**
     * Gets the metric name.
     * 
     * @return the metric name
     */
    public String getMetricName() {
        return metricName;
    }

    /**
     * Gets the service admin name.
     * 
     * @return the service admin name
     */
    public String getServiceAdminName() {
        return serviceAdminName;
    }

    /**
     * Sets the metric name.
     * 
     * @param metricName
     *            the new metric name
     */
    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    /**
     * Sets the service admin name.
     * 
     * @param serviceAdminName
     *            the new service admin name
     */
    public void setServiceAdminName(String serviceAdminName) {
        this.serviceAdminName = serviceAdminName;
    }

    /**
     * Sets the operation name.
     * 
     * @param operationName
     *            the new operation name
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
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
     * Gets the key.
     * 
     * @return the key
     */
    public String getKey() {
        return this.metricName + KEY_SEPARATOR + this.serviceAdminName + KEY_SEPARATOR + this.operationName
                        + KEY_SEPARATOR + this.serverSide;
    }

    public boolean isServerSide() {
        return serverSide;
    }

}
