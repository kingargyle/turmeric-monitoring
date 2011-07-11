package org.ebayopensource.turmeric.monitoring;

import static org.junit.Assert.assertEquals;

import org.ebayopensource.turmeric.monitoring.impl.SOAMetricsQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.provider.model.ExtendedErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGroupData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SOAMetricsQueryServiceImplTest {
    private SOAMetricsQueryServiceImpl service;

    @Before
    public void setUp() {
        service = new SOAMetricsQueryServiceImpl();
    }

    @After
    public void tearDown() {
        service = null;
    }

    @Test
    public void testCalcDiff100Positive() {
        Double oneHundredPercent = Double.valueOf(100d);
        assertEquals(oneHundredPercent, service.calcDiff(10d, 20d));// 100%
        assertEquals(oneHundredPercent, service.calcDiff(11d, 22d));// 100%
    }

    @Test
    public void testCalcDiff100Negative() {
        Double oneHundredPercentNeg = Double.valueOf(-100d);
        assertEquals(oneHundredPercentNeg, service.calcDiff(20d, 0d));// -100%
        assertEquals(oneHundredPercentNeg, service.calcDiff(30d, 0d));// -100%
    }

    @Test
    public void testCalcDiff50PercentNegative() {
        Double fiftyPercentNeg = Double.valueOf(-50d);
        assertEquals(fiftyPercentNeg, service.calcDiff(20d, 10d));
    }

    @Test
    public void testCalcDiff50PercentPositive() {
        Double fiftyPercent = Double.valueOf(50d);
        assertEquals(fiftyPercent, service.calcDiff(10, 15));

    }

    @Test
    public void testCalcDiffValue() {
        MetricGroupData metricGroupData = new MetricGroupData();
        metricGroupData.setCount1(1d);
        metricGroupData.setCount2(2d);
        service.calcDiffValue(metricGroupData);
        Double expectedValue = Double.valueOf(100d);
        assertEquals(expectedValue, Double.valueOf(metricGroupData.getDiff()));
    }

    @Test
    public void testCalcDiffValueWithZeroedCount2() {
        MetricGroupData metricGroupData = new MetricGroupData();
        metricGroupData.setCount1(1d);
        metricGroupData.setCount2(0d);
        service.calcDiffValue(metricGroupData);
        Double expectedValue = Double.valueOf(-100d);
        assertEquals(expectedValue, Double.valueOf(metricGroupData.getDiff()));
    }

    @Test
    public void testCalcDiffValueWithZeroes() {
        MetricGroupData metricGroupData = new MetricGroupData();
        metricGroupData.setCount1(0d);
        metricGroupData.setCount2(0d);
        service.calcDiffValue(metricGroupData);
        Double expectedValue = Double.valueOf(0d);
        assertEquals(expectedValue, Double.valueOf(metricGroupData.getDiff()));
    }

    @Test
    public void testCalcDiffValueFromZeroToNonZero() {
        MetricGroupData metricGroupData = new MetricGroupData();
        metricGroupData.setCount1(0d);
        metricGroupData.setCount2(12d);
        service.calcDiffValue(metricGroupData);
        Double expectedValue = Double.valueOf(100d);
        assertEquals(expectedValue, Double.valueOf(metricGroupData.getDiff()));
    }

    @Test
    public void testCalcDiffValueWithFloatingPoint() {
        MetricGroupData metricGroupData = new MetricGroupData();
        metricGroupData.setCount1(14d);
        metricGroupData.setCount2(174d);
        service.calcDiffValue(metricGroupData);
        Double expectedValue = Double.valueOf(1142.86d);
        assertEquals(expectedValue, Double.valueOf(metricGroupData.getDiff()));
    }

    @Test
    public void testCalcErrorDiffValues() {
        ExtendedErrorViewData errorViewData = new ExtendedErrorViewData();
        errorViewData.setErrorCount1(1l);
        errorViewData.setErrorCount2(2l);
        errorViewData.setErrorCall1(1l);
        errorViewData.setErrorCall2(2l);
        errorViewData.setErrorCallRatio1(1l);
        errorViewData.setErrorCallRatio2(2l);
        service.calcErrorDiffValues(errorViewData);
        Double expectedValue = Double.valueOf(100d);
        assertEquals(expectedValue, Double.valueOf(errorViewData.getErrorDiff()));
    }

    @Test
    public void testCalcErrorDiffValuesErrorCount2InZero() {
        ExtendedErrorViewData errorViewData = new ExtendedErrorViewData();
        errorViewData.setErrorCount1(1l);
        errorViewData.setErrorCount2(0l);
        errorViewData.setErrorCall1(1l);
        errorViewData.setErrorCall2(2l);
        errorViewData.setErrorCallRatio1(1l);
        errorViewData.setErrorCallRatio2(2l);
        service.calcErrorDiffValues(errorViewData);
        Double expectedValue = Double.valueOf(-100d);
        assertEquals(expectedValue, Double.valueOf(errorViewData.getErrorDiff()));
    }

    @Test
    public void testCalcErrorDiffValuesErrorCallRadio2InZero() {
        ExtendedErrorViewData errorViewData = new ExtendedErrorViewData();
        errorViewData.setErrorCount1(1l);
        errorViewData.setErrorCount2(1l);
        errorViewData.setErrorCall1(1l);
        errorViewData.setErrorCall2(1l);
        errorViewData.setErrorCallRatio1(1l);
        errorViewData.setErrorCallRatio2(0l);
        service.calcErrorDiffValues(errorViewData);
        Double expectedValue = Double.valueOf(-100d);
        assertEquals(expectedValue, Double.valueOf(errorViewData.getRatioDiff()));
    }

}
