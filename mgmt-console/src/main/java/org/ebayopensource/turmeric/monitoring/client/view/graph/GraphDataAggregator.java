/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.graph;

import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue;

/**
 * The Class GraphDataAggregator.
 */
public abstract class GraphDataAggregator {

    /** The agrgegated data array. */
    protected double[] agrgegatedDataArray;

    /**
     * Instantiates a new graph data aggregator.
     */
    public GraphDataAggregator() {
        super();
    }

    /**
     * Aggregate plot points per time unit.
     * 
     * @param returnData
     *            the return data
     * @param plotPointsPerHour
     *            the plot points per hour
     * @param hourSpan
     *            the hour span
     * @return the double[]
     */
    public abstract double[] aggregatePlotPointsPerTimeUnit(List<? extends TimeSlotValue> returnData,
                    int plotPointsPerHour, int hourSpan);

    /**
     * Aggregate all.
     * 
     * @param returnData
     *            the return data
     * @return the double
     */
    public abstract double aggregateAll(List<? extends TimeSlotValue> returnData);

    /**
     * Aggregate date time label per time unit.
     * 
     * @param returnData
     *            the return data
     * @param plotPointsPerHour
     *            the plot points per hour
     * @param hourSpan
     *            the hour span
     * @return the string[]
     */
    public String[] aggregateDateTimeLabelPerTimeUnit(List<? extends TimeSlotValue> returnData, int plotPointsPerHour,
                    int hourSpan) {
        String[] result = new String[hourSpan];
        int arrayIndex = 0;
        int counter = 0;
        for (int i = 0; i < returnData.size(); i++) {
            if (result[arrayIndex] == null) {
                result[arrayIndex] = ConsoleUtil.onlyTimeFormat.format(new Date(returnData.get(i).getTimeSlot()));
            }
            counter++;
            if (counter == plotPointsPerHour) {
                counter = 0;
                arrayIndex++;
            }
        }
        return result;
    }

}
