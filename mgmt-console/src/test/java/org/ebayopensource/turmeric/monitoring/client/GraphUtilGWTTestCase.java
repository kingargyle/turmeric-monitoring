package org.ebayopensource.turmeric.monitoring.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue;
import org.ebayopensource.turmeric.monitoring.client.util.GraphUtil;
import org.ebayopensource.turmeric.monitoring.client.view.SummaryPanel;
import org.junit.Test;

import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.LineChart;

public class GraphUtilGWTTestCase extends ConsoleGWTTestCase{
    /** The Constant ASYNC_DELAY_MS. */
    public static final int ASYNC_DELAY_MS = 45 * 1000;

    SummaryPanel panel = null;

    private boolean loaded = false;

    @Test
    public void testCreateLineChart() {
        panel = new SummaryPanel();
        final List<TimeSlotData> graphData = createGraphData(60, 4);
        
        Runnable onLoadCallback = new Runnable() {
            public void run() {
                final LineChart lineChart = new LineChart(GraphUtil.createChartDataTable(graphData, 60l, 4),
                                GraphUtil.createOptions("GrahpTitle"));
                panel.addChart(lineChart);
            }
        };
        loadApiAsync(onLoadCallback);
    }
    
    
    
    private List<TimeSlotData> createGraphData(int dataPerHour, int hours) {
        long now = new Date().getTime();
        long oneMinute = 1000;// in milisecs
        List<TimeSlotData> graphData = new ArrayList<TimeSlotData>();
        TimeSlotData dataItem = null;
        TimeSlotValue plotValue = null;
        long timeToAdd = dataPerHour == 60 ? 1000 : 60000;
        for (int i = 0; i < 2; i++) {
            dataItem = new TimeSlotData();
            dataItem.setReturnData(new ArrayList<TimeSlotValue>());
            for (int j = 0; j < dataPerHour * hours; j++) {
                plotValue = new DummyTimeSlotValue("", (double) j, now + (timeToAdd * j));
                dataItem.getReturnData().add(plotValue);
            }
            graphData.add(dataItem);
        }
        return graphData;

    }

    protected String getVisualizationPackage() {
        return "corechart";
    }

    protected void loadApiAsync(final Runnable testRunnable) {
        if (loaded) {
            testRunnable.run();
        }
        else {
            VisualizationUtils.loadVisualizationApi(new Runnable() {
                public void run() {
                    loaded = true;
                    try {
                        testRunnable.run();
                        finishTest();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        fail();
                    }
                }
            }, getVisualizationPackage());
            delayTestFinish(ASYNC_DELAY_MS);
        }
    }
}
