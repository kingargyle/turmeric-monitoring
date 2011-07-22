package org.ebayopensource.turmeric.monitoring.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.DummyTimeSlotValue;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue;
import org.ebayopensource.turmeric.monitoring.client.view.graph.GraphDataAggregator;
import org.ebayopensource.turmeric.monitoring.client.view.graph.SumGraphDataAggregator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataAggregatorTest extends ConsoleGwtTestBase {
    GraphDataAggregator aggregator;

    @Before
    public void setup() {
        aggregator = new SumGraphDataAggregator();
    }

    @After
    public void tearDown() {
        aggregator = null;
    }

    @Test
    public void testSumGraphDataAggregatorAggregatePlotPointsPerTimeUnitEmptyList() {
        List<? extends TimeSlotValue> list = new ArrayList<DummyTimeSlotValue>();

        int plotPointsPerHour = 1;
        int hourSpan = 2;

        double[] result = aggregator.aggregatePlotPointsPerTimeUnit(list, plotPointsPerHour, hourSpan);
        assertEquals(0d, result[0], 0);
    }

    @Test
    public void testSumGraphDataAggregatorAggregatePlotPointsPerTimeUnit() {
        List<DummyTimeSlotValue> list = new ArrayList<DummyTimeSlotValue>();
        for (int i = 0; i < 10; i++) {
            DummyTimeSlotValue newItem = new DummyTimeSlotValue("", new Double(i), new Long(i));
            list.add(newItem);
        }
        int plotPointsPerHour = 5;
        int hourSpan = 2;

        double[] result = aggregator.aggregatePlotPointsPerTimeUnit(list, plotPointsPerHour, hourSpan);
        assertEquals(10d, result[0], 0);// value for the first plot point
        assertEquals(35d, result[1], 0);// value for the second plot point
    }

    @Test
    public void testSumGraphDataAggregatorAggregateAll() {
        List<DummyTimeSlotValue> list = new ArrayList<DummyTimeSlotValue>();
        for (int i = 0; i < 10; i++) {
            DummyTimeSlotValue newItem = new DummyTimeSlotValue("", new Double(i), new Long(i));
            list.add(newItem);
        }
        int plotPointsPerHour = 10;
        int hourSpan = 2;

        double[] result = aggregator.aggregatePlotPointsPerTimeUnit(list, plotPointsPerHour, hourSpan);
        assertEquals(45d, result[0], 0);
    }

    @Test
    public void testSumGraphDataAggregatorAggregateDateTimeLabelPerTimeUnit() {
        long now = System.currentTimeMillis();

        List<DummyTimeSlotValue> list = new ArrayList<DummyTimeSlotValue>();
        for (int i = 0; i < 10; i++) {
            DummyTimeSlotValue newItem = new DummyTimeSlotValue("", new Double(i), now + (i * 1000));
            list.add(newItem);
        }
        int plotPointsPerHour = 5;
        int hourSpan = 2;

        String[] result = aggregator.aggregateDateTimeLabelPerTimeUnit(list, plotPointsPerHour, hourSpan);

        assertEquals(ConsoleUtil.onlyTimeFormat.format(new Date(list.get(0).getTimeSlot())), result[0]);
        assertEquals(ConsoleUtil.onlyTimeFormat.format(new Date(list.get(5).getTimeSlot())), result[1]);

    }
}
