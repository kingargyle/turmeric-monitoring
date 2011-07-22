/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.graph;

import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue;

/**
 * The Class SumGraphDataAggregator.
 */
public class SumGraphDataAggregator extends GraphDataAggregator {

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.client.view.graph.GraphDataAggregator#aggregatePlotPointsPerTimeUnit(java
     * .util.List, int, int)
     */
    @Override
    public double[] aggregatePlotPointsPerTimeUnit(List<? extends TimeSlotValue> returnData, int plotPointsPerHour,
                    int hourSpan) {
        double[] result = new double[hourSpan];
        int arrayIndex = 0;
        int counter = 0;
        for (int i = 0; i < returnData.size(); i++) {
            result[arrayIndex] += returnData.get(i).getValue();
            counter++;
            if (counter == plotPointsPerHour) {
                counter = 0;
                arrayIndex++;
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.view.graph.GraphDataAggregator#aggregateAll(java.util.List)
     */
    @Override
    public double aggregateAll(List<? extends TimeSlotValue> returnData) {
        double result = 0.0;
        for (TimeSlotValue timeSlotValue : returnData) {
            result += timeSlotValue.getValue();
        }
        return result;
    }

}
