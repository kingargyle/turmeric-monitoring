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

    protected MetricValue() {
    }

    public MetricValue(Metric metric, MetricClassifier metricClassifier, Machine machine) {
        this.metric = metric;
        this.metricClassifier = metricClassifier;
        this.machine = machine;
    }

    public Metric getMetric() {
        return metric;
    }

    public MetricClassifier getMetricClassifier() {
        return metricClassifier;
    }

    public Machine getMachine() {
        return machine;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isServerSide() {
        return serverSide;
    }

    public void setServerSide(boolean serverSide) {
        this.serverSide = serverSide;
    }

    public int getAggregationPeriod() {
        return aggregationPeriod;
    }

    public void setAggregationPeriod(int aggregationPeriod) {
        this.aggregationPeriod = aggregationPeriod;
    }

    public Set<MetricComponentValue> getMetricComponentValues() {
        return Collections.unmodifiableSet(componentValues);
    }

    public void addMetricComponentValue(MetricComponentValue componentValue) {
        componentValue.setMetricValue(this);
        componentValues.add(componentValue);
    }

    public MetricComponentValue findMetricComponentValue(String componentName) {
        for (MetricComponentValue componentValue : componentValues) {
            if (componentValue.getMetricComponentDef().getName().equals(componentName)) {
                return componentValue;
            }
        }
        return null;
    }

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
