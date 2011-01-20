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
 * ErrorMetricData
 *
 */
public class ErrorMetricData {
    String restUrl;
    ErrorCriteria errorCriteria;
    MetricCriteria metricCriteria;
    List<ErrorViewData> returnData;
    
    public List<ErrorViewData> getReturnData() {
        return returnData;
    }
    public void setReturnData(List<ErrorViewData> returnData) {
        this.returnData = returnData;
    }
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
    public String getRestUrl() {
        return restUrl;
    }
    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }
}
