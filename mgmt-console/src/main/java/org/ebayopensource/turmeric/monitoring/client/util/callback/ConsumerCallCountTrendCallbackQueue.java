package org.ebayopensource.turmeric.monitoring.client.util.callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.presenter.ConsumerPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.ConsumerPresenter.Display;

import com.google.gwt.core.client.GWT;

public class ConsumerCallCountTrendCallbackQueue extends ParallelCallbackQueue<List<TimeSlotData>>  {

    private Display view;

    @Override
    protected void success() {
        Map<String, List<TimeSlotData>> graphData = new HashMap<String, List<TimeSlotData>>();
        
        GWT.log("ConsumerCallCountTrendCallbackQueue success!!");
        for (ParallelCallback<List<TimeSlotData>>  cllbck : this.callbacks) {
            List<TimeSlotData> data = cllbck.getData();
            GWT.log("data.size() = "+data.size());
            graphData.put(cllbck.getId(), data);
        }
        view.setConsumerServiceCallTrendData(graphData);
    }

    @Override
    protected void stopOnError(Throwable excp, ParallelCallback<List<TimeSlotData>> callbackInQueue) {
        GWT.log("queue error!. Callback = "+callbackInQueue, excp);
    }

    public void setView(Display view) {
        this.view = view;
    }

}
