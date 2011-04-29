/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.List;

/**
 * ErrorMetricData.
 */
public class ErrorMetricData {
    
    /** The rest url. */
    String restUrl;
    
    /** The error criteria. */
    ErrorCriteria errorCriteria;
    
    /** The metric criteria. */
    MetricCriteria metricCriteria;
    
    /** The return data. */
    List<ErrorViewData> returnData;
    
    /**
     * Gets the return data.
     *
     * @return the return data
     */
    public List<ErrorViewData> getReturnData() {
        return returnData;
    }
    
    /**
     * Sets the return data.
     *
     * @param returnData the new return data
     */
    public void setReturnData(List<ErrorViewData> returnData) {
        this.returnData = returnData;
    }
    
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
    
    /**
     * Gets the rest url.
     *
     * @return the rest url
     */
    public String getRestUrl() {
        return restUrl;
    }
    
    /**
     * Sets the rest url.
     *
     * @param restUrl the new rest url
     */
    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }
}
