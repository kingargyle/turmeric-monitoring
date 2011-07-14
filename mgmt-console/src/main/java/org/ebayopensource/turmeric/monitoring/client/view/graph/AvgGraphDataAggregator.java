package org.ebayopensource.turmeric.monitoring.client.view.graph;

import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue;

public class AvgGraphDataAggregator extends GraphDataAggregator {

    @Override
    public double[] aggregatePlotPointsPerTimeUnit(List<TimeSlotValue> returnData, int plotPointsPerHour, int hourSpan) {
        double[] result = new double[hourSpan];
        int arrayIndex = 0;
        int counter = 0;
        int avgFactor = 0;
        for (int i = 0; i < returnData.size(); i++) {
            if (returnData.get(i).getValue() > 0.0d) {
                result[arrayIndex] += returnData.get(i).getValue();
                avgFactor++;
            }
            counter++;
            if (counter == plotPointsPerHour) {
                result[arrayIndex] = result[arrayIndex] / (avgFactor > 0 ? avgFactor : 1);
                avgFactor = 0;
                counter = 0;
                arrayIndex++;
            }
        }
        return result;
    }

    @Override
    public double aggregateAll(List<TimeSlotValue> returnData) {
        double result = 0.0;
        int aggregatedValue = 0;
        for (TimeSlotValue timeSlotValue : returnData) {
            if (timeSlotValue.getValue() > 0.0d) {
                result += timeSlotValue.getValue();
                aggregatedValue++;
            }
        }
        return result / (aggregatedValue > 0 ? aggregatedValue : 1);
    }

}
