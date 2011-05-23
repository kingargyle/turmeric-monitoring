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
 * The Class ConsumerCallCountTrendCallbackQueue.
 */
public class ConsumerCallCountTrendCallbackQueue extends ConsumerTabCallbackQueue {

    /**
     * Instantiates a new consumer call count trend callback queue.
     *
     * @param serviceName the service name
     * @param operationName the operation name
     * @param aggregationPeriod the aggregation period
     * @param hourSpan the hour span
     * @param view the view
     */
    public ConsumerCallCountTrendCallbackQueue(String serviceName, String operationName, long aggregationPeriod, int hourSpan, Display view) {
        super(serviceName, operationName, hourSpan, aggregationPeriod, view);
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.util.callback.ConsumerTabCallbackQueue#setGraphData(java.util.Map)
     */
    @Override
    protected void setGraphData(Map<String, List<TimeSlotData>> graphData) {
        String graphTitle = "";
        graphTitle = "Call Count for " + serviceName;
        if (getOperationName() != null) {
            graphTitle += "." + operationName;
        }
        graphTitle += " over a " + hourSpan + " hr period";

        view.setConsumerServiceCallTrendData(graphData, this.getAggregationPeriod(), this.getHourSpan(), graphTitle);
    }
}
