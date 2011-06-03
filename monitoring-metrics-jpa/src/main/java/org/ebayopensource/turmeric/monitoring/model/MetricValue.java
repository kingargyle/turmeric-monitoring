/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.ebayopensource.turmeric.utils.jpa.model.Persistent;

/**
 * The Class MetricValue.
 */
@Entity
public class MetricValue extends Persistent {
    @ManyToOne
    private Metric metric;
    @ManyToOne
    private MetricClassifier metricClassifier;
    @ManyToOne
    private Machine machine;
    private long timeStamp;
    private boolean serverSide;
    private int aggregationPeriod;
    @OneToMany(mappedBy = "metricValue", cascade = CascadeType.ALL)
    private Set<MetricComponentValue> componentValues = new HashSet<MetricComponentValue>();

    /**
     * Instantiates a new metric value.
     */
    protected MetricValue() {
    }

    /**
     * Instantiates a new metric value.
     *
     * @param metric the metric
     * @param metricClassifier the metric classifier
     * @param machine the machine
     */
    public MetricValue(Metric metric, MetricClassifier metricClassifier, Machine machine) {
        this.metric = metric;
        this.metricClassifier = metricClassifier;
        this.machine = machine;
    }

    /**
     * Gets the metric.
     *
     * @return the metric
     */
    public Metric getMetric() {
        return metric;
    }

    /**
     * Gets the metric classifier.
     *
     * @return the metric classifier
     */
    public MetricClassifier getMetricClassifier() {
        return metricClassifier;
    }

    /**
     * Gets the machine.
     *
     * @return the machine
     */
    public Machine getMachine() {
        return machine;
    }

    /**
     * Gets the time stamp.
     *
     * @return the time stamp
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the time stamp.
     *
     * @param timeStamp the new time stamp
     */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Checks if is server side.
     *
     * @return true, if is server side
     */
    public boolean isServerSide() {
        return serverSide;
    }

    /**
     * Sets the server side.
     *
     * @param serverSide the new server side
     */
    public void setServerSide(boolean serverSide) {
        this.serverSide = serverSide;
    }

    /**
     * Gets the aggregation period.
     *
     * @return the aggregation period
     */
    public int getAggregationPeriod() {
        return aggregationPeriod;
    }

    /**
     * Sets the aggregation period.
     *
     * @param aggregationPeriod the new aggregation period
     */
    public void setAggregationPeriod(int aggregationPeriod) {
        this.aggregationPeriod = aggregationPeriod;
    }

    /**
     * Gets the metric component values.
     *
     * @return the metric component values
     */
    public Set<MetricComponentValue> getMetricComponentValues() {
        return Collections.unmodifiableSet(componentValues);
    }

    /**
     * Adds the metric component value.
     *
     * @param componentValue the component value
     */
    public void addMetricComponentValue(MetricComponentValue componentValue) {
        componentValue.setMetricValue(this);
        componentValues.add(componentValue);
    }

    /**
     * Find metric component value.
     *
     * @param componentName the component name
     * @return the metric component value
     */
    public MetricComponentValue findMetricComponentValue(String componentName) {
        for (MetricComponentValue componentValue : componentValues) {
            if (componentValue.getMetricComponentDef().getName().equals(componentName)) {
                return componentValue;
            }
        }
        return null;
    }

    /**
     * Aggregate.
     *
     * @param that the that
     */
    public void aggregate(MetricValue that) {
        for (MetricComponentDef metricComponentDef : metric.getMetricDef().getMetricComponentDefs()) {
            MetricComponentValue thisComponentValue = findMetricComponentValue(metricComponentDef.getName());
            if (thisComponentValue == null) {
                thisComponentValue = new MetricComponentValue(metricComponentDef, 0);
                addMetricComponentValue(thisComponentValue);
            }
            MetricComponentValue thatComponentValue = that.findMetricComponentValue(metricComponentDef.getName());
            thisComponentValue.aggregate(thatComponentValue);
        }
    }
}
