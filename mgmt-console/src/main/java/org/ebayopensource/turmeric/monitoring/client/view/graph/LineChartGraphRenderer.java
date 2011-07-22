/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.graph;

import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.view.SummaryPanel;

import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.AbstractDrawOptions;
import com.google.gwt.visualization.client.CommonChartOptions;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.Visualization;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;

/**
 * The Class LineChartGraphRenderer.
 */
public class LineChartGraphRenderer extends GraphRenderer {

    /** The graph data. */
    protected List<? extends TimeSlotData> graphData;

    /**
     * Instantiates a new line chart graph renderer.
     * 
     * @param dataAgregator
     *            the data agregator
     * @param graphTitle
     *            the graph title
     * @param panel
     *            the panel
     * @param graphData
     *            the graph data
     * @param aggregationPeriod
     *            the aggregation period
     * @param hourSpan
     *            the hour span
     */
    public LineChartGraphRenderer(GraphDataAggregator dataAgregator, String graphTitle, SummaryPanel panel,
                    List<? extends TimeSlotData> graphData, long aggregationPeriod, int hourSpan) {
        super(dataAgregator, graphTitle, panel, aggregationPeriod, hourSpan);
        this.graphData = graphData;
    }

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.view.graph.GraphRenderer#createOptions()
     */
    @Override
    protected CommonChartOptions createOptions() {
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

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.view.graph.GraphRenderer#createDataTable()
     */
    @Override
    protected AbstractDataTable createDataTable() {
        int plotPointsPerHour = 1;
        if (getAggregationPeriod() < 3600) {// then, each point represents X minutes. e.g:
                                            // minAggregationPeriod = 60, then we
            // would get 60 points to plot per hour
            plotPointsPerHour = (int) (3600 / getAggregationPeriod());
        }
        DataTable data = DataTable.create();
        TimeSlotData firstDateRange = this.graphData.get(0);
        TimeSlotData secondDateRange = this.graphData.get(1);
        if (firstDateRange.getReturnData() != null && secondDateRange.getReturnData() != null) {
            int rowSize = getHourSpan();
            double[] firstDateRangeArray = dataAggregator.aggregatePlotPointsPerTimeUnit(
                            firstDateRange.getReturnData(), plotPointsPerHour, getHourSpan());
            double[] secondDateRangeArray = dataAggregator.aggregatePlotPointsPerTimeUnit(
                            secondDateRange.getReturnData(), plotPointsPerHour, getHourSpan());
            String[] labelArray = dataAggregator.aggregateDateTimeLabelPerTimeUnit(firstDateRange.getReturnData(),
                            plotPointsPerHour, getHourSpan());
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
                    data.setValue(i, 0, labelArray[i]);
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

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.view.graph.GraphRenderer#createVisualization()
     */
    @Override
    public Visualization<? extends AbstractDrawOptions> createVisualization() {
        return new LineChart(createDataTable(), (Options) createOptions());
    }

}
