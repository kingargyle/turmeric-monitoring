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
 * The Enum ConsumerMetric.
 */
public enum ConsumerMetric {
    
    /** The Call volume. */
    CallVolume ("CallCount"), 
    
    /** The Performance. */
    Performance ("ResponseTime"),
    
    /** The Errors. */
    Errors ("ErrorCount"), 
    
    /** The Top volume. */
    TopVolume ("CallCount"), 
    
    /** The Least performance. */
    LeastPerformance ("ResponseTime"), 
    
    /** The Top service errors. */
    TopServiceErrors ("ErrorCount"), 
    
    /** The Top consumer errors. */
    TopConsumerErrors ("ErrorCount");

    private String metricName;

    private ConsumerMetric (String metricName) {
        this.metricName = metricName;
    }

    /**
     * To metric name.
     *
     * @return the string
     */
    public String toMetricName () {
        return this.metricName;
    }
}