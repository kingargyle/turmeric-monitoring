package org.ebayopensource.turmeric.monitoring.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue;
import org.ebayopensource.turmeric.monitoring.client.view.DashboardContainer;
import org.ebayopensource.turmeric.monitoring.client.view.ServiceView;
import org.junit.Test;

import com.google.gwt.visualization.client.VisualizationUtils;



/**
 * The Class ServiceViewTest.
 */
public class ServiceViewTest extends ConsoleGWTTestCase {

    /** The Constant ASYNC_DELAY_MS. */
    public static final int ASYNC_DELAY_MS = 15 * 1000;

    private Dashboard dshbrd;
    private ServiceView view;
    private boolean loaded = false;

    /**
     * Test set service call trend data.
     */
    @Test
    public void testSetServiceCallTrendData() {
        dshbrd = new DashboardContainer();
        view = new ServiceView(dshbrd);
        final List<TimeSlotData> graphData = createGraphData(60);

        loadApi(new Runnable() {
            public void run() {
                view.setServiceCallTrendData(graphData, 60l, 1, "Test Service Call Graph Title");
            }
        });
    }
    
    /**
     * Test set service call trend data with hourly data.
     */
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
    
    /**
     * Test set service performance trend data.
     */
    public void testSetServicePerformanceTrendData() {
        dshbrd = new DashboardContainer();
        view = new ServiceView(dshbrd);
        final List<TimeSlotData> graphData = createGraphData(60);

        loadApi(new Runnable() {
            public void run() {
                view.setServicePerformanceTrendData(graphData, 60l, 1, "Test Service Call Graph Title");
            }
        });
    }
    
    /**
     * Test set service error trend data.
     */
    public void testSetServiceErrorTrendData() {
        dshbrd = new DashboardContainer();
        view = new ServiceView(dshbrd);
        final List<TimeSlotData> graphData = createGraphData(60);

        loadApi(new Runnable() {
            public void run() {
                view.setServiceErrorTrendData(graphData, 60l, 1, "Test Service Call Graph Title");
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

    /**
     * Load api.
     *
     * @param testRunnable the test runnable
     */
    protected void loadApi(final Runnable testRunnable) {
        if (loaded) {
            testRunnable.run();
        }
        else {
            VisualizationUtils.loadVisualizationApi(new Runnable() {
                public void run() {
                    loaded = true;
                    testRunnable.run();
                   finishTest();
                }
            }, getVisualizationPackage());
            delayTestFinish(ASYNC_DELAY_MS);
        }
    }

    /**
     * Gets the visualization package.
     *
     * @return the visualization package
     */
    protected String getVisualizationPackage() {
        return "corechart";
    }
}
