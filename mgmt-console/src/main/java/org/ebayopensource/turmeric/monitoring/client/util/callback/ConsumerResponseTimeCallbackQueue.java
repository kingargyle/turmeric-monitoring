package org.ebayopensource.turmeric.monitoring.client.util.callback;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.presenter.ConsumerPresenter.Display;

public class ConsumerResponseTimeCallbackQueue extends ConsumerTabCallbackQueue {

    public ConsumerResponseTimeCallbackQueue(String serviceName, String operationName, int hourSpan, Display view) {
        super(serviceName, operationName, hourSpan, view);
    }

    @Override
    protected void setGraphData(Map<String, List<TimeSlotData>> graphData) {
        String graphTitle = "";
        graphTitle = "Response Time for " + serviceName;
        if (getOperationName() != null) {
            graphTitle += "." + operationName;
        }
        graphTitle += " over a " + hourSpan + " hr period";
        view.setConsumerServicePerformanceTrendData(graphData, graphTitle);
    }

}
