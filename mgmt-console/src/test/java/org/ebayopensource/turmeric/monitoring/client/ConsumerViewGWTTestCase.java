package org.ebayopensource.turmeric.monitoring.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue;
import org.ebayopensource.turmeric.monitoring.client.view.ConsumerView;
import org.ebayopensource.turmeric.monitoring.client.view.DashboardContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gwt.visualization.client.VisualizationUtils;

/**
 * The Class ServiceViewGWTTestCase.
 */
public class ConsumerViewGWTTestCase extends ConsoleGWTTestCase {

    /** The Constant ASYNC_DELAY_MS. */
    public static final int ASYNC_DELAY_MS = 15 * 1000;

    private Dashboard dshbrd = null;
    private ConsumerView view = null;
    private boolean loaded = false;

    /**
     * Test set service call trend data.
     */
    
    @Before
    public void gwtSetUp(){
        dshbrd = new DashboardContainer();
        view = new ConsumerView(dshbrd);
    }
    
    @After
    public void gwtTearDown(){
        dshbrd = null;
        view = null;
    }
    
    
    @Test
    public void testSetConsumerCallTrendData() {
        final List<TimeSlotData> graphData = createGraphData(60);

        loadApi(new Runnable() {
            public void run() {
                view.setConsumerCallTrendData(graphData, 60l, 1, "Test Consumer Call Graph Title");
            }
        });
    }
    
//    @Test(expected=java.lang.NullPointerException.class)
//    public void testErrorInSetServiceCallTrendData() {
//        final List<TimeSlotData> graphData = null;
//
//        loadApi(new Runnable() {
//            public void run() {
//                view.setConsumerCallTrendData(graphData, 60l, 1, "Test Service Call Graph Title");
//            }
//        });
//    }
//
//    /**
//     * Test set service call trend data with hourly data.
//     */
//    public void testSetServiceCallTrendDataInHourlyData() {
//        final List<TimeSlotData> graphData = createGraphData(1);
//
//        loadApi(new Runnable() {
//            public void run() {
//                view.setServiceCallTrendData(graphData, 3600l, 1, "Test Service Call Graph Title");
//            }
//        });
//    }
//
//    /**
//     * Test set service performance trend data.
//     */
//    public void testSetServicePerformanceTrendData() {
//        final List<TimeSlotData> graphData = createGraphData(60);
//
//        loadApi(new Runnable() {
//            public void run() {
//                view.setServicePerformanceTrendData(graphData, 60l, 1, "Test Service Call Graph Title");
//            }
//        });
//    }
//
//    /**
//     * Test set service error trend data.
//     */
//    public void testSetServiceErrorTrendData() {
//        final List<TimeSlotData> graphData = createGraphData(60);
//
//        loadApi(new Runnable() {
//            public void run() {
//                view.setServiceErrorTrendData(graphData, 60l, 1, "Test Service Call Graph Title");
//            }
//        });
//    }

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
     * @param testRunnable
     *            the test runnable
     */
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
                        fail();
                    }
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
