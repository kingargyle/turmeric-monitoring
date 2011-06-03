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
 * The Class MetricComponentDef.
 */
@Entity
public class MetricComponentDef extends Persistent {
    private String name;
    private String type;
    @ManyToOne
    private MetricDef metricDef;

    /**
     * Instantiates a new metric component def.
     */
    protected MetricComponentDef() {
    }

    /**
     * Instantiates a new metric component def.
     *
     * @param name the name
     * @param type the type
     */
    public MetricComponentDef(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the metric def.
     *
     * @return the metric def
     */
    public MetricDef getMetricDef() {
        return metricDef;
    }

    /**
     * Sets the metric def.
     *
     * @param metricDef the new metric def
     */
    protected void setMetricDef(MetricDef metricDef) {
        this.metricDef = metricDef;
    }
}
