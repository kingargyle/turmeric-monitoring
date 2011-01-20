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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import org.ebayopensource.turmeric.utils.jpa.model.Persistent;

@Entity
public class MetricDef extends Persistent {
    private String name;
    @Enumerated(EnumType.STRING)
    private MetricCategory category;
    @Enumerated(EnumType.STRING)
    private MonitoringLevel level;
    @OneToMany(mappedBy = "metricDef", cascade = CascadeType.ALL)
    private Set<MetricComponentDef> components = new HashSet<MetricComponentDef>();

    protected MetricDef() {
    }

    public MetricDef(String name, MetricCategory category, MonitoringLevel level) {
        this.name = name;
        this.category = category;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public MetricCategory getMetricCategory() {
        return category;
    }

    public MonitoringLevel getMonitoringLevel() {
        return level;
    }

    public void addMetricComponentDef(MetricComponentDef component) {
        component.setMetricDef(this);
        components.add(component);
    }

    public Set<MetricComponentDef> getMetricComponentDefs() {
        return Collections.unmodifiableSet(components);
    }

    public MetricComponentDef findMetricComponentDef(String componentName) {
        for (MetricComponentDef component : components) {
            if (component.getName().equals(componentName)) {
                return component;
            }
        }
        return null;
    }
}
