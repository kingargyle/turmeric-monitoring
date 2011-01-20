/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring.client.model;

public enum ErrorMetric {
    
    TopApplicationErrors("?"),
    TopRequestErrors("?"),
    TopSystemErrors("?"),
    TopCriticals("?"),
    TopErrors("?"),
    TopWarnings("?"),
    ConsumerError("?"),
    TopCategoryErrors("?"),
    TopSeverityErrors("?");
    
    
    public static ErrorMetric[] CATEGORY_METRICS = new ErrorMetric[] {TopApplicationErrors, TopRequestErrors, TopSystemErrors};
    public static ErrorMetric[] SEVERITY_METRICS = new ErrorMetric[] {TopCriticals, TopErrors, TopWarnings};
    
    private String metricName;
    
    private ErrorMetric (String metricName) {
        this.metricName = metricName;
    }
    
    public String toMetricName() {
        return this.metricName;
    }
}