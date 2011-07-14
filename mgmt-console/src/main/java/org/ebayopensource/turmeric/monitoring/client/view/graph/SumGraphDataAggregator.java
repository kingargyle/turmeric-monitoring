package org.ebayopensource.turmeric.monitoring.client.view.graph;

import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue;

public class SumGraphDataAggregator extends GraphDataAggregator {

    @Override
    public double[] aggregatePlotPointsPerTimeUnit(List<TimeSlotValue> returnData, int plotPointsPerHour, int hourSpan) {
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

    @Override
    public double aggregateAll(List<TimeSlotValue> returnData) {
        double result = 0.0;
        for (TimeSlotValue timeSlotValue : returnData) {
            result += timeSlotValue.getValue();
        }
        return result;
    }

}
