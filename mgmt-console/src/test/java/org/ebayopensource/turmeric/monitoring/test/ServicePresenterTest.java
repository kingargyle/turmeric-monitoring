package org.ebayopensource.turmeric.monitoring.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.ebayopensource.turmeric.monitoring.client.model.DummyMetricsQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.client.model.MetricData;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Entity;
import org.ebayopensource.turmeric.monitoring.client.model.ObjectType;
import org.ebayopensource.turmeric.monitoring.client.model.SelectionContext;
import org.ebayopensource.turmeric.monitoring.client.model.ServiceMetric;
import org.ebayopensource.turmeric.monitoring.client.presenter.ServicePresenter;
import org.ebayopensource.turmeric.monitoring.client.view.ServiceView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import com.google.gwt.event.shared.HandlerManager;

public class ServicePresenterTest extends ConsoleGwtTestBase {

    ServicePresenter servicePresenter = null;
    HandlerManager eventBus = null;
    DummyMetricsQueryServiceImpl service = null;
    ServiceView view = null;

    @Before
    public void setUp() {
        eventBus = new HandlerManager(null);
        service = new DummyMetricsQueryServiceImpl();
        view = mock(ServiceView.class);
        view.addValueChangeHandlerForDate1(null);
        view.addFilterOptionsApplyClickHandler(null);
        view.addTreeElementSelectionHandler(null);
        servicePresenter = new ServicePresenter(this.eventBus, view, service);
        
    }

    @After
    public void tearDown() {
        service = null;
        eventBus = null;
        servicePresenter = null;
    }

    @Test
    public void testFetchTopVolumeMetricsForServiceSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        ctx.select(ObjectType.ServiceName, serviceName);
        servicePresenter.fetchMetric(ServiceMetric.TopVolume, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).activate();
        verify(view).setMetric(Matchers.eq(ServiceMetric.TopVolume), Matchers.any(MetricData.class));
    }

    @Test
    public void testFetchTopVolumeMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String operationName = "MyServiceOperation";
        ctx.select(ObjectType.OperationName, operationName);
        servicePresenter.fetchMetric(ServiceMetric.TopVolume, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).activate();
        verify(view).setMetric(Matchers.eq(ServiceMetric.TopVolume), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchLeastPerformanceMetricsForServiceSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        ctx.select(ObjectType.ServiceName, serviceName);
        servicePresenter.fetchMetric(ServiceMetric.LeastPerformance, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).activate();
        verify(view).setMetric(Matchers.eq(ServiceMetric.LeastPerformance), Matchers.any(MetricData.class));
    }

    @Test
    public void testFetchLeastPerformanceMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String operationName = "MyServiceOperation";
        ctx.select(ObjectType.OperationName, operationName);
        servicePresenter.fetchMetric(ServiceMetric.LeastPerformance, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).activate();
        verify(view).setMetric(Matchers.eq(ServiceMetric.LeastPerformance), Matchers.any(MetricData.class));
    }
    
    @Test
    public void testFetchConsumerErrorsMetricsForServiceSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        ctx.select(ObjectType.ServiceName, serviceName);
        servicePresenter.fetchMetric(ServiceMetric.ConsumerErrors, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).activate();
        verify(view).setMetric(Matchers.eq(ServiceMetric.ConsumerErrors), Matchers.any(MetricData.class));
    }

    @Test
    public void testFetchConsumerErrorsMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String operationName = "MyServiceOperation";
        ctx.select(ObjectType.OperationName, operationName);
        servicePresenter.fetchMetric(ServiceMetric.ConsumerErrors, ctx, Entity.Operation, now, oneHourLater, hoursInterval);
        verify(view).activate();
        verify(view).setMetric(Matchers.eq(ServiceMetric.ConsumerErrors), Matchers.any(MetricData.class));
    }
    

}
