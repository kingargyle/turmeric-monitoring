/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.ebayopensource.turmeric.utils.jpa.model.Persistent;

/**
 * The Class Metric.
 */
@Entity
public class Metric extends Persistent {
    private String serviceAdminName;
    private String operationName;
    @ManyToOne
    private MetricDef metricDef;

    /**
     * Instantiates a new metric.
     */
    protected Metric() {
    }

    /**
     * Instantiates a new metric.
     *
     * @param serviceAdminName the service admin name
     * @param operationName the operation name
     * @param metricDef the metric def
     */
    public Metric(String serviceAdminName, String operationName, MetricDef metricDef) {
        this.serviceAdminName = serviceAdminName;
        this.operationName = operationName;
        this.metricDef = metricDef;
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
     * Gets the operation name.
     *
     * @return the operation name
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * Gets the metric def.
     *
     * @return the metric def
     */
    public MetricDef getMetricDef() {
        return metricDef;
    }
}
