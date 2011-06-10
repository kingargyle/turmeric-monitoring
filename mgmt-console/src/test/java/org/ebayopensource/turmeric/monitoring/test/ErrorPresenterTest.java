package org.ebayopensource.turmeric.monitoring.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.ebayopensource.turmeric.monitoring.client.model.DummyMetricsQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorCriteria;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorMetric;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorMetricData;
import org.ebayopensource.turmeric.monitoring.client.model.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorCategory;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorType;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Ordering;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Perspective;
import org.ebayopensource.turmeric.monitoring.client.model.ObjectType;
import org.ebayopensource.turmeric.monitoring.client.model.SelectionContext;
import org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter;
import org.ebayopensource.turmeric.monitoring.client.view.ErrorView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import com.google.gwt.event.shared.HandlerManager;

public class ErrorPresenterTest extends ConsoleGwtTestBase {

    ErrorPresenter errorPresenter = null;
    HandlerManager eventBus = null;
    DummyMetricsQueryServiceImpl service = null;
    ErrorView view = null;

    @Before
    public void setUp() {
        eventBus = new HandlerManager(null);
        service = new DummyMetricsQueryServiceImpl();
        view = mock(ErrorView.class);
        errorPresenter = new ErrorPresenter(this.eventBus, view, service);
    }

    @After
    public void tearDown() {
        service = null;
        eventBus = null;
        errorPresenter = null;
    }

    @Test
    public void testFetchConsumerErrorMetricsForServiceSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        ctx.select(ObjectType.ServiceName, serviceName);
        ErrorCriteria ec = ErrorCriteria.newErrorCriteria(ErrorType.Category, Arrays.asList(serviceName), null, null,
                        "", false, ErrorCategory.Application, ErrorSeverity.Error);
        MetricCriteria mc = MetricCriteria.newMetricCriteria("", now, oneHourLater, hoursInterval, Ordering.Ascending,
                        10, Perspective.Server, false);
        errorPresenter.fetchMetric(ErrorMetric.ConsumerError, ec, mc);
        verify(view).setErrorMetricData(Matchers.eq(ErrorMetric.ConsumerError), Matchers.any(ErrorMetricData.class));
    }

    @Test
    public void testFetchTopApplicationErrorsMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        String operationName = "MyOperationName";
        ctx.select(ObjectType.ServiceName, serviceName);
        ErrorCriteria ec = ErrorCriteria.newErrorCriteria(ErrorType.Category, Arrays.asList(serviceName),
                        Arrays.asList(operationName), null, "", false, ErrorCategory.Application, ErrorSeverity.Error);
        MetricCriteria mc = MetricCriteria.newMetricCriteria("", now, oneHourLater, hoursInterval, Ordering.Ascending,
                        10, Perspective.Server, false);
        errorPresenter.fetchMetric(ErrorMetric.TopApplicationErrors, ec, mc);
        verify(view).setErrorMetricData(Matchers.eq(ErrorMetric.TopApplicationErrors),
                        Matchers.any(ErrorMetricData.class));
    }

    @Test
    public void testFetchTopCategoryErrorsMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        String operationName = "MyOperationName";
        ctx.select(ObjectType.ServiceName, serviceName);
        ErrorCriteria ec = ErrorCriteria.newErrorCriteria(ErrorType.Category, Arrays.asList(serviceName),
                        Arrays.asList(operationName), null, "", false, ErrorCategory.Application, ErrorSeverity.Error);
        MetricCriteria mc = MetricCriteria.newMetricCriteria("", now, oneHourLater, hoursInterval, Ordering.Ascending,
                        10, Perspective.Server, false);
        errorPresenter.fetchMetric(ErrorMetric.TopCategoryErrors, ec, mc);
        verify(view).setErrorMetricData(Matchers.eq(ErrorMetric.TopCategoryErrors), Matchers.any(ErrorMetricData.class));
    }

    @Test
    public void testFetchTopCriticalsErrorsMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        String operationName = "MyOperationName";
        ctx.select(ObjectType.ServiceName, serviceName);
        ErrorCriteria ec = ErrorCriteria.newErrorCriteria(ErrorType.Category, Arrays.asList(serviceName),
                        Arrays.asList(operationName), null, "", false, ErrorCategory.Application, ErrorSeverity.Error);
        MetricCriteria mc = MetricCriteria.newMetricCriteria("", now, oneHourLater, hoursInterval, Ordering.Ascending,
                        10, Perspective.Server, false);
        errorPresenter.fetchMetric(ErrorMetric.TopCriticals, ec, mc);
        verify(view).setErrorMetricData(Matchers.eq(ErrorMetric.TopCriticals), Matchers.any(ErrorMetricData.class));
    }

    @Test
    public void testFetchTopErrorsErrorsMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        String operationName = "MyOperationName";
        ctx.select(ObjectType.ServiceName, serviceName);
        ErrorCriteria ec = ErrorCriteria.newErrorCriteria(ErrorType.Category, Arrays.asList(serviceName),
                        Arrays.asList(operationName), null, "", false, ErrorCategory.Application, ErrorSeverity.Error);
        MetricCriteria mc = MetricCriteria.newMetricCriteria("", now, oneHourLater, hoursInterval, Ordering.Ascending,
                        10, Perspective.Server, false);
        errorPresenter.fetchMetric(ErrorMetric.TopErrors, ec, mc);
        verify(view).setErrorMetricData(Matchers.eq(ErrorMetric.TopErrors), Matchers.any(ErrorMetricData.class));
    }

    @Test
    public void testFetchTopRequestErrorsErrorsMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        String operationName = "MyOperationName";
        ctx.select(ObjectType.ServiceName, serviceName);
        ErrorCriteria ec = ErrorCriteria.newErrorCriteria(ErrorType.Category, Arrays.asList(serviceName),
                        Arrays.asList(operationName), null, "", false, ErrorCategory.Application, ErrorSeverity.Error);
        MetricCriteria mc = MetricCriteria.newMetricCriteria("", now, oneHourLater, hoursInterval, Ordering.Ascending,
                        10, Perspective.Server, false);
        errorPresenter.fetchMetric(ErrorMetric.TopRequestErrors, ec, mc);
        verify(view).setErrorMetricData(Matchers.eq(ErrorMetric.TopRequestErrors), Matchers.any(ErrorMetricData.class));
    }

    @Test
    public void testFetchTopSeverityErrorsErrorsMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        String operationName = "MyOperationName";
        ctx.select(ObjectType.ServiceName, serviceName);
        ErrorCriteria ec = ErrorCriteria.newErrorCriteria(ErrorType.Category, Arrays.asList(serviceName),
                        Arrays.asList(operationName), null, "", false, ErrorCategory.Application, ErrorSeverity.Error);
        MetricCriteria mc = MetricCriteria.newMetricCriteria("", now, oneHourLater, hoursInterval, Ordering.Ascending,
                        10, Perspective.Server, false);
        errorPresenter.fetchMetric(ErrorMetric.TopSeverityErrors, ec, mc);
        verify(view).setErrorMetricData(Matchers.eq(ErrorMetric.TopSeverityErrors), Matchers.any(ErrorMetricData.class));
    }

    @Test
    public void testFetchTopSystemErrorsErrorsMetricsForOperationSelection() {
        long now = System.currentTimeMillis();
        long oneHourLater = now + 3600 * 1000;
        int hoursInterval = 1;
        SelectionContext ctx = new SelectionContext();
        String serviceName = "MyService";
        String operationName = "MyOperationName";
        ctx.select(ObjectType.ServiceName, serviceName);
        ErrorCriteria ec = ErrorCriteria.newErrorCriteria(ErrorType.Category, Arrays.asList(serviceName),
                        Arrays.asList(operationName), null, "", false, ErrorCategory.Application, ErrorSeverity.Error);
        MetricCriteria mc = MetricCriteria.newMetricCriteria("", now, oneHourLater, hoursInterval, Ordering.Ascending,
                        10, Perspective.Server, false);
        errorPresenter.fetchMetric(ErrorMetric.TopSystemErrors, ec, mc);
        verify(view).setErrorMetricData(Matchers.eq(ErrorMetric.TopSystemErrors), Matchers.any(ErrorMetricData.class));
    }

}
