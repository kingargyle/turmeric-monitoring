package org.ebayopensource.turmeric.gwttestutils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.Dashboard;
import org.ebayopensource.turmeric.monitoring.client.DummyTimeSlotValue;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue;
import org.ebayopensource.turmeric.monitoring.client.view.DashboardContainer;
import org.ebayopensource.turmeric.monitoring.client.view.ServiceView;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gwt.visualization.client.VisualizationUtils;
import com.octo.gwt.test.GwtTest;


@Ignore
public class MyTest extends GwtTest{

    private Dashboard dshbrd = null;
    private ServiceView view = null;
    private boolean loaded = false;
    
    
    @Override
    public String getModuleName() {
        return "org.ebayopensource.turmeric.monitoring.Console";
    }

    protected void loadApi(final Runnable testRunnable) {
        if (loaded) {
            testRunnable.run();
        }
        else {
            VisualizationUtils.loadVisualizationApi(new Runnable() {
                public void run() {
                    loaded = true;
                    try {
                        testRunnable.run();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Assert.fail();
                    }
                    
                }
            }, "corechart");
        }
    }
    
    @Test
    public void testSetServiceCallTrendDataInHourlyData() {
        dshbrd = new DashboardContainer();
        view = new ServiceView(dshbrd);
        final List<TimeSlotData> graphData = createGraphData(1);

        loadApi(new Runnable() {
            public void run() {
                view.setServiceCallTrendData(graphData, 3600l, 1, "Test Service Call Graph Title");
            }
        });
    }
    
    private List<TimeSlotData> createGraphData(int dataPerHour) {
        long now = new Date().getTime();
        long oneMinute = 1000;// in milisecs
        List<TimeSlotData> graphData = new ArrayList<TimeSlotData>();
        TimeSlotData dataItem = null;
        TimeSlotValue plotValue = null;

        for (int i = 0; i < 2; i++) {
            dataItem = new TimeSlotData();
            dataItem.setReturnData(new ArrayList<TimeSlotValue>());
            for (int j = 0; j < dataPerHour; j++) {
                plotValue = new DummyTimeSlotValue("", (double) j, now + (oneMinute * j));
                dataItem.getReturnData().add(plotValue);
            }
            graphData.add(dataItem);
        }
        return graphData;

    }
}
