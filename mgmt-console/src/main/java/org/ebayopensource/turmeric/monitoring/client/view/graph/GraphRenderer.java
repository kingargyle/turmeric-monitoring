/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.graph;

import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.view.SummaryPanel;

import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDrawOptions;
import com.google.gwt.visualization.client.CommonChartOptions;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;
import com.google.gwt.visualization.client.visualizations.Visualization;

/**
 * The Class GraphRenderer.
 */
public abstract class GraphRenderer {

    /** The data aggregator. */
    protected GraphDataAggregator dataAggregator;

    /** The graph title. */
    protected String graphTitle;

    /** The panel. */
    protected SummaryPanel panel;

    /** The aggregation period. */
    protected long aggregationPeriod;

    /** The hour span. */
    protected int hourSpan;

    /**
     * Instantiates a new graph renderer.
     * 
     * @param dataAgregator
     *            the data agregator
     * @param graphTitle
     *            the graph title
     * @param panel
     *            the panel
     * @param aggregationPeriod
     *            the aggregation period
     * @param hourSpan
     *            the hour span
     */
    public GraphRenderer(GraphDataAggregator dataAgregator, String graphTitle, SummaryPanel panel,
                    long aggregationPeriod, int hourSpan) {
        super();
        this.dataAggregator = dataAgregator;
        this.graphTitle = graphTitle;
        this.panel = panel;
        this.aggregationPeriod = aggregationPeriod;
        this.hourSpan = hourSpan;
    }

    /**
     * Gets the data agregator.
     * 
     * @return the data agregator
     */
    public GraphDataAggregator getDataAgregator() {
        return dataAggregator;
    }

    /**
     * Gets the graph title.
     * 
     * @return the graph title
     */
    public String getGraphTitle() {
        return graphTitle;
    }

    /**
     * Gets the panel.
     * 
     * @return the panel
     */
    public SummaryPanel getPanel() {
        return panel;
    }

    /**
     * Creates the options.
     * 
     * @return the common chart options
     */
    protected abstract CommonChartOptions createOptions();

    /**
     * Creates the data table.
     * 
     * @return the abstract data table
     */
    protected abstract AbstractDataTable createDataTable();

    /**
     * Gets the aggregation period.
     * 
     * @return the aggregation period
     */
    public long getAggregationPeriod() {
        return aggregationPeriod;
    }

    /**
     * Gets the hour span.
     * 
     * @return the hour span
     */
    public int getHourSpan() {
        return hourSpan;
    }

    /**
     * Creates the visualization.
     * 
     * @return the visualization<? extends abstract draw options>
     */
    public abstract Visualization<? extends AbstractDrawOptions> createVisualization();

    /**
     * This template method depends on {@link #createVisualization} to create the appropriate GWT Chart.
     */
    public void render() {
        {
            Runnable onLoadCallback = new Runnable() {
                public void run() {
                    final Visualization<? extends AbstractDrawOptions> chart = createVisualization();
                    panel.addChart(chart);
                }
            };

            loadGraphAPI(onLoadCallback);
        }
    }

    /**
     * Load graph api.
     * 
     * @param onLoadCallback
     *            the on load callback
     */
    private void loadGraphAPI(Runnable onLoadCallback) {
        // Load the visualization api, passing the onLoadCallback to be called when loading is done.
        // The gwt param "corechart" tells gwt to use the new charts
        VisualizationUtils.loadVisualizationApi(onLoadCallback, "corechart");
    }

}
