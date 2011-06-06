package org.ebayopensource.turmeric.monitoring.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.ebayopensource.turmeric.monitoring.client.model.ConsumerMetric;
import org.ebayopensource.turmeric.monitoring.client.model.DummyMetricsQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.client.model.MetricData;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Entity;
import org.ebayopensource.turmeric.monitoring.client.model.ObjectType;
import org.ebayopensource.turmeric.monitoring.client.model.SelectionContext;
import org.ebayopensource.turmeric.monitoring.client.presenter.ConsumerPresenter;
import org.ebayopensource.turmeric.monitoring.client.view.ConsumerView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import com.google.gwt.event.shared.HandlerManager;


public class ConsumerPresenterTest extends ConsoleGwtTestBase {

    ConsumerPresenter consumerPresenter = null;
    HandlerManager eventBus = null;
    DummyMetricsQueryServiceImpl service = null;
    ConsumerView view = null;

    @Before
    public void setUp() {
        eventBus = new HandlerManager(null);
        service = new DummyMetricsQueryServiceImpl();
        view = mock(ConsumerView.class);
        consumerPresenter = new ConsumerPresenter(this.eventBus, view, service);
    }

    @After
    public void tearDown() {
        service = null;
        eventBus = null;
        consumerPresenter = null;
    }
    
    @Test
    public void testFetchTopVolumeMetricsForServiceSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        ctx.select(ObjectType.ServiceName, serviceName);
        consumerPresenter.fetchMetric(ConsumerMetric.TopVolume, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.TopVolume), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchTopVolumeMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyServiceOperation";
        ctx.select(ObjectType.OperationName, serviceName);
        consumerPresenter.fetchMetric(ConsumerMetric.TopVolume, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.TopVolume), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchTopVolumeMetricsForConsumerSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String consumerName = "MyServiceConsumer";
        ctx.select(ObjectType.ConsumerName, consumerName);
        consumerPresenter.fetchMetric(ConsumerMetric.TopVolume, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.TopVolume), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchCallVolumeMetricsForServiceSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        ctx.select(ObjectType.ServiceName, serviceName);
        consumerPresenter.fetchMetric(ConsumerMetric.CallVolume, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.CallVolume), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchCallVolumeMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyServiceOperation";
        ctx.select(ObjectType.OperationName, serviceName);
        consumerPresenter.fetchMetric(ConsumerMetric.CallVolume, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.CallVolume), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchPerformanceMetricsForServiceSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        ctx.select(ObjectType.ServiceName, serviceName);
        consumerPresenter.fetchMetric(ConsumerMetric.Performance, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.Performance), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchPerformanceMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyServiceOperation";
        ctx.select(ObjectType.OperationName, serviceName);
        consumerPresenter.fetchMetric(ConsumerMetric.Performance, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.Performance), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchLeastPerformanceMetricsForServiceSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        ctx.select(ObjectType.ServiceName, serviceName);
        consumerPresenter.fetchMetric(ConsumerMetric.LeastPerformance, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.LeastPerformance), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchLeastPerformanceMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyServiceOperation";
        ctx.select(ObjectType.OperationName, serviceName);
        consumerPresenter.fetchMetric(ConsumerMetric.LeastPerformance, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.LeastPerformance), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchTopServiceErrorsMetricsForServiceSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        ctx.select(ObjectType.ServiceName, serviceName);
        consumerPresenter.fetchMetric(ConsumerMetric.TopServiceErrors, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.TopServiceErrors), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchTopServiceErrorsMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyServiceOperation";
        ctx.select(ObjectType.OperationName, serviceName);
        consumerPresenter.fetchMetric(ConsumerMetric.TopServiceErrors, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.TopServiceErrors), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchErrorsMetricsForServiceSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        ctx.select(ObjectType.ServiceName, serviceName);
        consumerPresenter.fetchMetric(ConsumerMetric.Errors, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.Errors), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchErrorsMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyServiceOperation";
        ctx.select(ObjectType.OperationName, serviceName);
        consumerPresenter.fetchMetric(ConsumerMetric.Errors, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).setMetric(Matchers.eq(ConsumerMetric.Errors), Matchers.any(MetricData.class));
    }
}
