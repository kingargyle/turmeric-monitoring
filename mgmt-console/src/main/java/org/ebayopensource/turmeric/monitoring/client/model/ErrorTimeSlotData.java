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
 * ErrorTimeSlotData.
 */
public class ErrorTimeSlotData extends TimeSlotData {

    /** The error criteria. */
    ErrorCriteria errorCriteria;
    
    /** The metric criteria. */
    MetricCriteria metricCriteria;
    
    /**
     * Gets the error criteria.
     *
     * @return the error criteria
     */
    public ErrorCriteria getErrorCriteria() {
        return errorCriteria;
    }
    
    /**
     * Sets the error criteria.
     *
     * @param errorCriteria the new error criteria
     */
    public void setErrorCriteria(ErrorCriteria errorCriteria) {
        this.errorCriteria = errorCriteria;
    }
    
    /**
     * Gets the metric criteria.
     *
     * @return the metric criteria
     */
    public MetricCriteria getMetricCriteria() {
        return metricCriteria;
    }
    
    /**
     * Sets the metric criteria.
     *
     * @param metricCriteria the new metric criteria
     */
    public void setMetricCriteria(MetricCriteria metricCriteria) {
        this.metricCriteria = metricCriteria;
    }
}
