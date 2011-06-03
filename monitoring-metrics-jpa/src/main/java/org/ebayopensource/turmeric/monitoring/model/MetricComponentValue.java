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
 * The Class MetricComponentValue.
 */
@Entity
public class MetricComponentValue extends Persistent {
    @ManyToOne
    private MetricComponentDef metricComponentDef;
    @ManyToOne
    private MetricValue metricValue;
    private Double value;

    /**
     * Instantiates a new metric component value.
     */
    protected MetricComponentValue() {
    }

    /**
     * Instantiates a new metric component value.
     *
     * @param component the component
     * @param value the value
     */
    public MetricComponentValue(MetricComponentDef component, double value) {
        this.metricComponentDef = component;
        this.value = value;
    }

    /**
     * Gets the metric component def.
     *
     * @return the metric component def
     */
    public MetricComponentDef getMetricComponentDef() {
        return metricComponentDef;
    }

    /**
     * Gets the metric value.
     *
     * @return the metric value
     */
    public MetricValue getMetricValue() {
        return metricValue;
    }

    /**
     * Sets the metric value.
     *
     * @param metricValue the new metric value
     */
    protected void setMetricValue(MetricValue metricValue) {
        this.metricValue = metricValue;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * Aggregate.
     *
     * @param that the that
     */
    public void aggregate(MetricComponentValue that) {
        if (!metricComponentDef.getName().equals(that.metricComponentDef.getName()) ||
                !metricComponentDef.getType().equals(that.metricComponentDef.getType()))
            throw new IllegalArgumentException();
        value += that.value;
    }
}
