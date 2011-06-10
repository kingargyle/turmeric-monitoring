package org.ebayopensource.turmeric.monitoring.test.integration;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.AppController;
import org.ebayopensource.turmeric.monitoring.client.SupportedService;
import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.DummyMetricsQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.presenter.ConsumerPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.DashboardPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.MenuController;
import org.ebayopensource.turmeric.monitoring.client.presenter.Presenter.TabPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.ServicePresenter;
import org.ebayopensource.turmeric.monitoring.test.ConsoleGwtTestBase;
import org.junit.After;
import org.junit.Before;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class ConsoleIntegrationTestBase extends ConsoleGwtTestBase {

    protected TabPresenter consumerPresenter = null;
    protected TabPresenter servicePresenter = null;
    protected TabPresenter errorPresenter = null;
    HandlerManager eventBus = null;
    protected DummyMetricsQueryServiceImpl service = null;
    AppController appController = null;

    public ConsoleIntegrationTestBase() {
        super();
    }

    @Before
    public void setUp() {
        service = new DummyMetricsQueryServiceImpl();
        Map<SupportedService, ConsoleService> serviceMap = new HashMap<SupportedService, ConsoleService>();
        serviceMap.put(SupportedService.METRICS_QUERY_SERVICE, service);
        eventBus = new HandlerManager(null);
        appController = new AppController(eventBus, RootLayoutPanel.get(), serviceMap);
        appController.start();
        MenuController menuController = (MenuController) appController.getPresenter(MenuController.PRESENTER_ID);
        assertNotNull(menuController);

        DashboardPresenter dshbrdPresenter = (DashboardPresenter) menuController
                        .getPresenter(DashboardPresenter.DASH_ID);
        assertNotNull(dshbrdPresenter);
        servicePresenter = (TabPresenter) dshbrdPresenter.getPresenter(ServicePresenter.SERVICE_ID);
        consumerPresenter = (TabPresenter) dshbrdPresenter.getPresenter(ConsumerPresenter.CONSUMER_ID);
        errorPresenter = (TabPresenter) dshbrdPresenter.getPresenter(ErrorPresenter.ERROR_ID);
    }

    @After
    public void tearDown() {
        service = null;
        eventBus = null;
        servicePresenter = null;
        appController = null;
        consumerPresenter = null;
        errorPresenter = null;
    }

    protected void selectServiceForTab(String tabName, String serviceName) {
        appController.selectPresenter(HistoryToken.newHistoryToken("dash~tab=" + tabName + "~svc=" + serviceName));
    }

}