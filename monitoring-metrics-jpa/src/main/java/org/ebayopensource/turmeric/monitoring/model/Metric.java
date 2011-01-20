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

@Entity
public class Metric extends Persistent {
    private String serviceAdminName;
    private String operationName;
    @ManyToOne
    private MetricDef metricDef;

    protected Metric() {
    }

    public Metric(String serviceAdminName, String operationName, MetricDef metricDef) {
        this.serviceAdminName = serviceAdminName;
        this.operationName = operationName;
        this.metricDef = metricDef;
    }

    public String getServiceAdminName() {
        return serviceAdminName;
    }

    public String getOperationName() {
        return operationName;
    }

    public MetricDef getMetricDef() {
        return metricDef;
    }
}
