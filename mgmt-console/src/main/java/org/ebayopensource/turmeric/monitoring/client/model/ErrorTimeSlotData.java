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
 * ErrorTimeSlotData
 *
 */
public class ErrorTimeSlotData extends TimeSlotData {

    ErrorCriteria errorCriteria;
    MetricCriteria metricCriteria;
    
    public ErrorCriteria getErrorCriteria() {
        return errorCriteria;
    }
    public void setErrorCriteria(ErrorCriteria errorCriteria) {
        this.errorCriteria = errorCriteria;
    }
    public MetricCriteria getMetricCriteria() {
        return metricCriteria;
    }
    public void setMetricCriteria(MetricCriteria metricCriteria) {
        this.metricCriteria = metricCriteria;
    }
}
