/*******************************************************************************
 *   Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"); 
 *   you may not use this file except in compliance with the License. 
 *   You may obtain a copy of the License at 
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.util.callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.presenter.ConsumerPresenter.Display;

import com.google.gwt.core.client.GWT;

/**
 * The Class ConsumerTabCallbackQueue.
 */
public abstract class ConsumerTabCallbackQueue extends ParallelCallbackQueue<List<TimeSlotData>> {
    
    /** The service name. */
    protected String serviceName;
    
    /** The operation name. */
    protected String operationName;
    
    /** The hour span. */
    protected int hourSpan;
    
    /** The aggregation period. */
    protected long aggregationPeriod;

    /**
     * Instantiates a new consumer tab callback queue.
     *
     * @param serviceName the service name
     * @param operationName the operation name
     * @param hourSpan the hour span
     * @param aggregationPeriod the aggregation period
     * @param view the view
     */
    public ConsumerTabCallbackQueue(String serviceName, String operationName, int hourSpan, long aggregationPeriod,
                    Display view) {
        super();
        this.serviceName = serviceName;
        this.operationName = operationName;
        this.hourSpan = hourSpan;
        this.aggregationPeriod = aggregationPeriod;
        this.view = view;
    }

    /**
     * Gets the aggregation period.
     *
     * @return the aggregation period
     */
    public long getAggregationPeriod() {
        return aggregationPeriod;
    }

    /**
     * Sets the aggregation period.
     *
     * @param aggregationPeriod the new aggregation period
     */
    public void setAggregationPeriod(long aggregationPeriod) {
        this.aggregationPeriod = aggregationPeriod;
    }

    /**
     * Gets the service name.
     *
     * @return the service name
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Gets the operation name.
     *
     * @return the operation name
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * Gets the hour span.
     *
     * @return the hour span
     */
    public int getHourSpan() {
        return hourSpan;
    }

    /** The view. */
    protected Display view;

    /**
     * Instantiates a new consumer tab callback queue.
     */
    public ConsumerTabCallbackQueue() {
        super();
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.util.callback.ParallelCallbackQueue#success()
     */
    @Override
    protected void success() {
        Map<String, List<TimeSlotData>> graphData = new HashMap<String, List<TimeSlotData>>();
        GWT.log("ConsumerTabCallbackQueue.success()");
        //GWT.log("ConsumerCallCountTrendCallbackQueue success!!");
        for (ParallelCallback<List<TimeSlotData>>  cllbck : this.callbacks) {
            List<TimeSlotData> data = cllbck.getData();
            //GWT.log("data.size() = "+data.size());
            graphData.put(cllbck.getId(), data);
        }
        setGraphData(graphData);
    }

    /**
     * Sets the graph data.
     *
     * @param graphData the graph data
     */
    protected abstract void setGraphData(Map<String, List<TimeSlotData>> graphData);

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.util.callback.ParallelCallbackQueue#stopOnError(java.lang.Throwable, org.ebayopensource.turmeric.monitoring.client.util.callback.ParallelCallback)
     */
    @Override
    protected void stopOnError(Throwable excp, ParallelCallback<List<TimeSlotData>> callbackInQueue) {
        GWT.log("queue error!. Callback = "+callbackInQueue, excp);
    }

    /**
     * Sets the view.
     *
     * @param view the new view
     */
    public void setView(Display view) {
        this.view = view;
    }

}