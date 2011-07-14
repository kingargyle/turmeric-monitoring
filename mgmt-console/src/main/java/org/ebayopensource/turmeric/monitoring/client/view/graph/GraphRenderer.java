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

public abstract class GraphRenderer {
    protected GraphDataAggregator dataAggregator;
    protected String graphTitle;
    protected SummaryPanel panel;
    protected long aggregationPeriod;
    protected int hourSpan;

    public GraphRenderer(GraphDataAggregator dataAgregator, String graphTitle, SummaryPanel panel,
                    long aggregationPeriod, int hourSpan) {
        super();
        this.dataAggregator = dataAgregator;
        this.graphTitle = graphTitle;
        this.panel = panel;
        this.aggregationPeriod = aggregationPeriod;
        this.hourSpan = hourSpan;
    }

    public GraphDataAggregator getDataAgregator() {
        return dataAggregator;
    }

    public String getGraphTitle() {
        return graphTitle;
    }

    public SummaryPanel getPanel() {
        return panel;
    }

    protected abstract CommonChartOptions createOptions();

    protected abstract AbstractDataTable createDataTable();

    public long getAggregationPeriod() {
        return aggregationPeriod;
    }

    public int getHourSpan() {
        return hourSpan;
    }

    public abstract Visualization<? extends AbstractDrawOptions> createVisualization();

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

    private void loadGraphAPI(Runnable onLoadCallback) {
        // Load the visualization api, passing the onLoadCallback to be called when loading is done.
        // The gwt param "corechart" tells gwt to use the new charts
        VisualizationUtils.loadVisualizationApi(onLoadCallback, "corechart");
    }

}
