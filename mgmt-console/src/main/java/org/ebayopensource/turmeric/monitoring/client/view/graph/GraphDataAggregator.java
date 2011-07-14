package org.ebayopensource.turmeric.monitoring.client.view.graph;

import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue;

public abstract class GraphDataAggregator {
    protected double[] agrgegatedDataArray;

    public double[] getAgrgegatedDataArray() {
        return agrgegatedDataArray;
    }

    public GraphDataAggregator() {
        super();
    }

    public abstract double[] aggregatePlotPointsPerTimeUnit(List<TimeSlotValue> returnData, int plotPointsPerHour,
                    int hourSpan);

    public abstract double aggregateAll(List<TimeSlotValue> returnData);

    public String[] aggregateDateTimeLabelPerTimeUnit(List<TimeSlotValue> returnData, int plotPointsPerHour,
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
