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
public class MetricComponentDef extends Persistent {
    private String name;
    private String type;
    @ManyToOne
    private MetricDef metricDef;

    protected MetricComponentDef() {
    }

    public MetricComponentDef(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public MetricDef getMetricDef() {
        return metricDef;
    }

    protected void setMetricDef(MetricDef metricDef) {
        this.metricDef = metricDef;
    }
}
