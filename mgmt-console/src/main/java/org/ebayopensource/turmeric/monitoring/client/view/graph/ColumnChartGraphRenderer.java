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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.view.SummaryPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDrawOptions;
import com.google.gwt.visualization.client.CommonChartOptions;
import com.google.gwt.visualization.client.CommonOptions;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.ColumnChart;
import com.google.gwt.visualization.client.visualizations.Visualization;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;

/**
 * The Class ColumnChartGraphRenderer.
 */
public class ColumnChartGraphRenderer extends GraphRenderer {

    /** The data range. */
    Map<String, List<TimeSlotData>> dataRange;

    /**
     * Instantiates a new column chart graph renderer.
     * 
     * @param dataAgregator
     *            the data agregator
     * @param graphTitle
     *            the graph title
     * @param panel
     *            the panel
     * @param dataRange
     *            the data range
     * @param aggregationPeriod
     *            the aggregation period
     * @param hourSpan
     *            the hour span
     */
    public ColumnChartGraphRenderer(GraphDataAggregator dataAgregator, String graphTitle, SummaryPanel panel,
                    Map<String, List<TimeSlotData>> dataRange, long aggregationPeriod, int hourSpan) {
        super(dataAgregator, graphTitle, panel, aggregationPeriod, hourSpan);
        this.dataRange = dataRange;
    }

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.view.graph.GraphRenderer#createOptions()
     */
    @Override
    protected CommonChartOptions createOptions() {
        com.google.gwt.visualization.client.visualizations.ColumnChart.Options options = com.google.gwt.visualization.client.visualizations.ColumnChart.Options
                        .create();
        // options.setWidth(800);
        options.setHeight(230);
        options.setEnableTooltip(true);
        options.setShowCategories(true);
        options.set("fontSize", 10d);
        options.setTitle(graphTitle);
        options.setTitleFontSize(12d);
        options.setLogScale(false);
        return options;
    }

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.view.graph.GraphRenderer#createDataTable()
     */
    @Override
    protected AbstractDataTable createDataTable() {
        DataTable data = DataTable.create();
        Iterator<String> keys = dataRange.keySet().iterator();
        String consumerName = null;
        int rowSize = dataRange.keySet().size();
        int i = 0;
        boolean datesAlreadyAdded = false;
        data.addRows(rowSize);
        while (keys.hasNext()) {
            consumerName = keys.next();
            if (dataRange.get(consumerName) == null || dataRange.get(consumerName).size() == 0) {
                continue;
            }
            TimeSlotData firstDateRange = dataRange.get(consumerName).get(0);
            // GWT.log("firstDateRange for consumer = " + consumerName + ". "+
            // firstDateRange.getReturnData().get(0).getValue());
            TimeSlotData secondDateRange = dataRange.get(consumerName).get(1);
            if (firstDateRange.getReturnData() != null && secondDateRange.getReturnData() != null) {

                if (rowSize > 0) {
                    if (!datesAlreadyAdded) {
                        data.addColumn(ColumnType.STRING, "x");
                        data.addColumn(ColumnType.NUMBER,
                                        ConsoleUtil.shotTimeFormat.format(new Date(firstDateRange.getReturnData()
                                                        .get(0).getTimeSlot())));

                        data.addColumn(ColumnType.NUMBER,
                                        ConsoleUtil.shotTimeFormat.format(new Date(secondDateRange.getReturnData()
                                                        .get(0).getTimeSlot())));
                        datesAlreadyAdded = true;
                    }

                    data.setValue(i, 0, consumerName);
                    data.setValue(i, 1, this.dataAggregator.aggregateAll(firstDateRange.getReturnData()));
                    data.setValue(i, 2, this.dataAggregator.aggregateAll(secondDateRange.getReturnData()));
                    i++;
                }
                else {
                    data.addColumn(ColumnType.STRING, "x");
                    data.addColumn(ColumnType.NUMBER, "");
                    data.addColumn(ColumnType.NUMBER, "");

                }
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
        return new ColumnChart(createDataTable(),
                        (com.google.gwt.visualization.client.visualizations.ColumnChart.Options) createOptions());
    }

}
