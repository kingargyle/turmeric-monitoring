package org.ebayopensource.turmeric.monitoring.client.util;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfoImpl;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorCriteria;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorTimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.model.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.client.model.MetricValue;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorCategory;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorType;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Ordering;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Perspective;
import org.ebayopensource.turmeric.monitoring.client.model.ObjectType;
import org.ebayopensource.turmeric.monitoring.client.model.SelectionContext;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.view.SummaryPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;

/**
 * The Class GraphUtil.
 */
public class GraphUtil {
    
    /**
     * Gets the simple graph data.
     *
     * @param queryService the query service
     * @param metricName the metric name
     * @param roleType the role type
     * @param aggregationPeriod the aggregation period
     * @param selectionContext the selection context
     * @param date1 the date1
     * @param date2 the date2
     * @param hourSpan the hour span
     * @param callback the callback
     */
    public static void getSimpleGraphData(MetricsQueryService queryService, String metricName, String roleType, long aggregationPeriod,
                    final SelectionContext selectionContext, long date1, long date2, final int hourSpan,
                    AsyncCallback<List<TimeSlotData>> callback) {
        long hourToSecondsMultiplier = 3600;
        CriteriaInfoImpl criteriaInfo = new CriteriaInfoImpl();
        criteriaInfo.setMetricName(metricName);
        criteriaInfo.setServiceName(selectionContext.getSelection(ObjectType.ServiceName));
        if (selectionContext.getSelection(ObjectType.OperationName) != null) {
            criteriaInfo.setOperationName(selectionContext.getSelection(ObjectType.OperationName));
        }
        criteriaInfo.setRoleType(roleType);
        if(aggregationPeriod >= 3600){
            hourToSecondsMultiplier = aggregationPeriod;
        }
        queryService.getMetricValueTrend(new MetricValue(criteriaInfo, date1, hourToSecondsMultiplier * hourSpan,
                        (int) aggregationPeriod, ""), new MetricValue(criteriaInfo, date2,
                                        hourToSecondsMultiplier * hourSpan, (int) aggregationPeriod, ""), callback);
    }
    
    /**
     * Gets the simple error graph data.
     *
     * @param queryService the query service
     * @param errorType the error type
     * @param errorCategory the error category
     * @param severity the severity
     * @param roleType the role type
     * @param aggregationPeriod the aggregation period
     * @param selectionContext the selection context
     * @param date1 the date1
     * @param date2 the date2
     * @param hourSpan the hour span
     * @param callback the callback
     */
    public static void getSimpleErrorGraphData(MetricsQueryService queryService, ErrorType errorType, ErrorCategory errorCategory,  ErrorSeverity severity, String roleType, long aggregationPeriod,
                    final SelectionContext selectionContext, long date1, long date2, final int hourSpan,
                    AsyncCallback<List<ErrorTimeSlotData>> callback) {
        String serviceName = selectionContext.getSelection(ObjectType.ServiceName);
        String operationName = selectionContext.getSelection(ObjectType.OperationName);
        String consumerName = selectionContext.getSelection(ObjectType.ConsumerName);
        List<String> serviceNames = null;
        List<String> consumerNames = null;
        List<String> operationNames = null;
        if(serviceName != null){
            serviceNames = Arrays.asList(serviceName);
        }
        if(consumerName != null){
            consumerNames = Arrays.asList(consumerName);
        }
        
        if(operationName != null){
            operationNames = Arrays.asList(operationName);
        }
        
        ErrorCriteria ec = ErrorCriteria.newErrorCriteria(errorType , serviceNames, operationNames, consumerNames, null, false, errorCategory, severity);
        MetricCriteria firstDate = new MetricCriteria("", date1, hourSpan,(int)aggregationPeriod);
        MetricCriteria secondDate =  new MetricCriteria("", date2, hourSpan,(int)aggregationPeriod);
        queryService.getErrorTrend(ec, firstDate,secondDate, callback);
        
        
    }
    
    /**
     * Creates the line chart.
     *
     * @param panel the panel
     * @param timeData the time data
     * @param aggregationPeriod the aggregation period
     * @param hourSpan the hour span
     * @param graphTitle the graph title
     */
    public static void createLineChart(final SummaryPanel panel, final List<? extends TimeSlotData> timeData,
                    final long aggregationPeriod, final int hourSpan, final String graphTitle) {
        Runnable onLoadCallback = new Runnable() {
            public void run() {
                final LineChart lineChart = new LineChart(createChartDataTable(timeData, aggregationPeriod, hourSpan),
                                createOptions(graphTitle));
                panel.addChart(lineChart);
            }
        };

        // Load the visualization api, passing the onLoadCallback to be called when loading is done.
        // The gwt param "corechart" tells gwt to use the new charts

        VisualizationUtils.loadVisualizationApi(onLoadCallback, "corechart");
    }
    
    private static AbstractDataTable createChartDataTable(List<? extends TimeSlotData> timeDataRange, long aggregationPeriod,
                    int hourSpan) {
        int plotPointsPerHour = 1;
        if (aggregationPeriod < 3600) {// then, each point represents X minutes. e.g: minAggregationPeriod = 60, then we
                                       // would get 60 points to plot per hour
            plotPointsPerHour = (int) (3600 / aggregationPeriod);
        }
        DataTable data = DataTable.create();
        TimeSlotData firstDateRange = timeDataRange.get(0);
        TimeSlotData secondDateRange = timeDataRange.get(1);
        if (firstDateRange.getReturnData() != null && secondDateRange.getReturnData() != null) {
            int rowSize = hourSpan;
            double[] firstDateRangeArray = calculatePlotPointsPerTimeUnit(firstDateRange.getReturnData(),
                            plotPointsPerHour, hourSpan);
            double[] secondDateRangeArray = calculatePlotPointsPerTimeUnit(secondDateRange.getReturnData(),
                            plotPointsPerHour, hourSpan);
            String[] labelArray = calculateDateTimeLabelPerTimeUnit(firstDateRange.getReturnData(), plotPointsPerHour, hourSpan);
            if (rowSize > 0) {
                data.addColumn(ColumnType.STRING, "x");
                data.addColumn(ColumnType.NUMBER,
                                ConsoleUtil.shotTimeFormat.format(new Date(firstDateRange.getReturnData().get(0)
                                                .getTimeSlot())));

                data.addColumn(ColumnType.NUMBER,
                                ConsoleUtil.shotTimeFormat.format(new Date(secondDateRange.getReturnData().get(0)
                                                .getTimeSlot())));
                data.addRows(rowSize);
                for (int i = 0; i < rowSize; i++) {
                    // GWT.log("getValue = "+timeData.getReturnData().get(i).getValue());
                    data.setValue(i,
                                    0,
                                    labelArray[i]);
                    data.setValue(i, 1, firstDateRangeArray[i]);
                    data.setValue(i, 2, secondDateRangeArray[i]);
                }
            }
            else {
                data.addColumn(ColumnType.STRING, "x");
                data.addColumn(ColumnType.NUMBER, "");
                data.addColumn(ColumnType.NUMBER, "");
                data.addRows(rowSize);
            }
        }
        return data;
    }
    
    private static double[] calculatePlotPointsPerTimeUnit(List<TimeSlotValue> returnData, int plotPointsPerTimeUnit, int span) {
        double[] result = new double[span];
        int arrayIndex = 0;
        int counter = 0;
        for (int i = 0; i < returnData.size(); i++) {
            result[arrayIndex] += returnData.get(i).getValue();
            counter++;
            if (counter == plotPointsPerTimeUnit) {
                counter = 0;
                arrayIndex++;
            }
        }
        return result;
    }
    
    private static Options createOptions(String graphTitle) {
        Options options = Options.create();
        // options.setWidth(600);
        options.setHeight(230);
        options.setEnableTooltip(true);
        options.setShowCategories(true);
        options.set("fontSize", 10d);
        options.setSmoothLine(true);
        options.setPointSize(3);
        options.setLineSize(3);
        options.setTitle(graphTitle);
        options.setTitleFontSize(12d);
        return options;
    }
    
    private static String[] calculateDateTimeLabelPerTimeUnit(List<TimeSlotValue> returnData, int plotPointsPerTimeUnit,
                    int span) {
        String[] result = new String[span];
        int arrayIndex = 0;
        int counter = 0;
        for (int i = 0; i < returnData.size(); i++) {
            if (result[arrayIndex] == null) {
                result[arrayIndex] = ConsoleUtil.onlyTimeFormat.format(new Date(returnData.get(i).getTimeSlot()));
            }
            counter++;
            if (counter == plotPointsPerTimeUnit) {
                counter = 0;
                arrayIndex++;
            }
        }
        return result;
    }

    /**
     * Creates the error line chart.
     *
     * @param panel the panel
     * @param dataRanges the data ranges
     * @param aggregationPeriod the aggregation period
     * @param hourSpan the hour span
     * @param graphTitle the graph title
     */
    public static void createErrorLineChart(SummaryPanel panel, List<ErrorTimeSlotData> dataRanges,
                    long aggregationPeriod, int hourSpan, String graphTitle) {
        createLineChart(panel, dataRanges, aggregationPeriod, hourSpan, graphTitle);
    }
    
}
