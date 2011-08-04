package org.ebayopensource.turmeric.monitoring.test;

import org.ebayopensource.turmeric.monitoring.client.view.SummaryPanel;
import org.ebayopensource.turmeric.monitoring.client.view.graph.ColumnChartGraphRenderer;
import org.ebayopensource.turmeric.monitoring.client.view.graph.GraphRenderer;
import org.ebayopensource.turmeric.monitoring.client.view.graph.LineChartGraphRenderer;
import org.ebayopensource.turmeric.monitoring.client.view.graph.SumGraphDataAggregator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GraphRendererTest extends ConsoleGwtTestBase {
    GraphRenderer renderer = null;
    SummaryPanel panel = null;

    @Before
    public void setup() {
        panel = new SummaryPanel();
    }

    @After
    public void cleanup() {
        panel = null;
    }

    @Test
    public void testColumnChartGraphRendererConstructorWithNullDataRange() {
        GraphRenderer renderer = new ColumnChartGraphRenderer(new SumGraphDataAggregator(), "Test Title", panel, null,
                        3600, 1);
        renderer.render();
    }

    @Test
    public void LineChartGraphRendererWithNullDataRange() {
        GraphRenderer renderer = new LineChartGraphRenderer(new SumGraphDataAggregator(), "Test Title", panel, null,
                        3600, 1);
        renderer.render();
    }

}
