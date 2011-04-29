/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring.client.model;

/**
 * The Enum ErrorMetric.
 */
public enum ErrorMetric {
    
    /** The Top application errors. */
    TopApplicationErrors("?"),
    
    /** The Top request errors. */
    TopRequestErrors("?"),
    
    /** The Top system errors. */
    TopSystemErrors("?"),
    
    /** The Top criticals. */
    TopCriticals("?"),
    
    /** The Top errors. */
    TopErrors("?"),
    
    /** The Top warnings. */
    TopWarnings("?"),
    
    /** The Consumer error. */
    ConsumerError("?"),
    
    /** The Top category errors. */
    TopCategoryErrors("?"),
    
    /** The Top severity errors. */
    TopSeverityErrors("?");
    
    
    /** The CATEGOR y_ metrics. */
    public static ErrorMetric[] CATEGORY_METRICS = new ErrorMetric[] {TopApplicationErrors, TopRequestErrors, TopSystemErrors};
    
    /** The SEVERIT y_ metrics. */
    public static ErrorMetric[] SEVERITY_METRICS = new ErrorMetric[] {TopCriticals, TopErrors, TopWarnings};
    
    private String metricName;
    
    private ErrorMetric (String metricName) {
        this.metricName = metricName;
    }
    
    /**
     * To metric name.
     *
     * @return the string
     */
    public String toMetricName() {
        return this.metricName;
    }
}