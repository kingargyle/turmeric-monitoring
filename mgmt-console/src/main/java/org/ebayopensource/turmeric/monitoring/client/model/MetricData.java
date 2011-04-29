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
 * MetricData
 * 
 * Results for metrics gathered.
 *
 */
public class MetricData {
    
    /** The rest url. */
    String restUrl;
   

    /** The metric criteria. */
    MetricCriteria metricCriteria;
    
    /** The metric resource criteria. */
    MetricResourceCriteria metricResourceCriteria;
    
    /** The return data. */
    List<MetricGroupData> returnData;
    

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
     * Gets the metric resource criteria.
     *
     * @return the metric resource criteria
     */
    public MetricResourceCriteria getMetricResourceCriteria() {
        return metricResourceCriteria;
    }

    /**
     * Sets the metric resource criteria.
     *
     * @param metricResourceCriteria the new metric resource criteria
     */
    public void setMetricResourceCriteria(MetricResourceCriteria metricResourceCriteria) {
        this.metricResourceCriteria = metricResourceCriteria;
    }
	
	/**
	 * Gets the return data.
	 *
	 * @return the return data
	 */
	public List<MetricGroupData> getReturnData() {
        return returnData;
    }

    /**
     * Sets the return data.
     *
     * @param returnData the new return data
     */
    public void setReturnData(List<MetricGroupData> returnData) {
        this.returnData = returnData;
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
