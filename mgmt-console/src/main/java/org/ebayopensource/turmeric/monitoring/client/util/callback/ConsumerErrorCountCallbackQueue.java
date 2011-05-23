/*******************************************************************************
 *   Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"); 
 *   you may not use this file except in compliance with the License. 
 *   You may obtain a copy of the License at 
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.util.callback;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.presenter.ConsumerPresenter.Display;

/**
 * The Class ConsumerErrorCountCallbackQueue.
 */
public class ConsumerErrorCountCallbackQueue extends ConsumerTabCallbackQueue {

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.util.callback.ConsumerTabCallbackQueue#setGraphData(java.util.Map)
     */
    @Override
    protected void setGraphData(Map<String, List<TimeSlotData>> graphData) {
        String graphTitle = "";
        graphTitle = "Error Count for " + serviceName;
        if (getOperationName() != null) {
            graphTitle += "." + getOperationName();
        }
        graphTitle += " over a " + hourSpan + " hr period";
        view.setConsumerErrorCountTrendData(graphData, this.getAggregationPeriod(), this.getHourSpan(), graphTitle);
    }

    /**
     * Instantiates a new consumer error count callback queue.
     *
     * @param serviceName the service name
     * @param operationName the operation name
     * @param aggregationPeriod the aggregation period
     * @param hourSpan the hour span
     * @param view the view
     */
    public ConsumerErrorCountCallbackQueue(String serviceName, String operationName, long aggregationPeriod, int hourSpan, Display view) {
        super(serviceName, operationName, hourSpan, aggregationPeriod, view);
    }

}
