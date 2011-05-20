package org.ebayopensource.turmeric.monitoring.client.util.callback;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.presenter.ConsumerPresenter.Display;

import com.google.gwt.core.client.GWT;

public class ConsumerErrorCountCallbackQueue extends ConsumerTabCallbackQueue {

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

    public ConsumerErrorCountCallbackQueue(String serviceName, String operationName, long aggregationPeriod, int hourSpan, Display view) {
        super(serviceName, operationName, hourSpan, aggregationPeriod, view);
    }

}
