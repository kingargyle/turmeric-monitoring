package org.ebayopensource.turmeric.monitoring.client.util.callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.presenter.ConsumerPresenter.Display;

import com.google.gwt.core.client.GWT;

public abstract class ConsumerTabCallbackQueue extends ParallelCallbackQueue<List<TimeSlotData>> {
    
    protected String serviceName;
    protected String operationName;
    protected int hourSpan;
    protected long aggregationPeriod;

    public ConsumerTabCallbackQueue(String serviceName, String operationName, int hourSpan, long aggregationPeriod,
                    Display view) {
        super();
        this.serviceName = serviceName;
        this.operationName = operationName;
        this.hourSpan = hourSpan;
        this.aggregationPeriod = aggregationPeriod;
        this.view = view;
    }

    public long getAggregationPeriod() {
        return aggregationPeriod;
    }

    public void setAggregationPeriod(long aggregationPeriod) {
        this.aggregationPeriod = aggregationPeriod;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getOperationName() {
        return operationName;
    }

    public int getHourSpan() {
        return hourSpan;
    }

    protected Display view;

    public ConsumerTabCallbackQueue() {
        super();
    }

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

    protected abstract void setGraphData(Map<String, List<TimeSlotData>> graphData);

    @Override
    protected void stopOnError(Throwable excp, ParallelCallback<List<TimeSlotData>> callbackInQueue) {
        GWT.log("queue error!. Callback = "+callbackInQueue, excp);
    }

    public void setView(Display view) {
        this.view = view;
    }

}