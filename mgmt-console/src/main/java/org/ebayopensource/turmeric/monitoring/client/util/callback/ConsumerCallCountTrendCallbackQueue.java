package org.ebayopensource.turmeric.monitoring.client.util.callback;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo;
import org.ebayopensource.turmeric.monitoring.client.model.ObjectType;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.presenter.ConsumerPresenter.Display;

public class ConsumerCallCountTrendCallbackQueue extends ConsumerTabCallbackQueue {

    public ConsumerCallCountTrendCallbackQueue(String serviceName, String operationName, long aggregationPeriod, int hourSpan, Display view) {
        super(serviceName, operationName, hourSpan, aggregationPeriod, view);
    }

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
