package org.ebayopensource.turmeric.monitoring.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ebayopensource.turmeric.monitoring.client.AppController;
import org.ebayopensource.turmeric.monitoring.client.SupportedService;
import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.DummyMetricsQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.client.model.ServiceMetric;
import org.ebayopensource.turmeric.monitoring.client.presenter.DashboardPresenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.MenuController;
import org.ebayopensource.turmeric.monitoring.client.presenter.ServicePresenter;
import org.ebayopensource.turmeric.monitoring.client.view.ServiceView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.octo.gwt.test.GwtTest;

public class ServicePresenterTest extends GwtTest {

    ServicePresenter servicePresenter = null;
    HandlerManager eventBus = null;
    DummyMetricsQueryServiceImpl service = null;
    AppController appController = null;
    Map<SupportedService, ConsoleService> serviceMap = null;
    
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
        menuController.getPresenter(ServicePresenter.SERVICE_ID);
        DashboardPresenter dshbrdPresenter = (DashboardPresenter) menuController.getPresenter(DashboardPresenter.DASH_ID);
        assertNotNull(dshbrdPresenter);
        servicePresenter = (ServicePresenter) dshbrdPresenter.getPresenter(ServicePresenter.SERVICE_ID);
    }

    @After
    public void tearDown() {
        service = null;
        eventBus = null;
        servicePresenter = null;
    }

    @Test
    public void testServicePresenterInitialization() {
        assertNotNull(servicePresenter);
        ServiceView view = (ServiceView)servicePresenter.getView();
        assertNotNull(view);
    }
    
    @Test
    public void testServiceSelectionFromServiceList() {
        assertNotNull(servicePresenter);
        ServiceView view = (ServiceView)servicePresenter.getView();
        assertNotNull(view);
        Tree serviceTree = (Tree)view.getSelector();
        assertNotNull(serviceTree);
        assertNotNull(serviceTree.getItem(0));//first level of the services tree
        assertNotNull(serviceTree.getItem(0).getChild(0));//first service in the list
        TreeItem serviceToSelect = serviceTree.getItem(0).asTreeItem().getChild(0);
        String html = serviceToSelect.asTreeItem().getHTML();
        assertNotNull(html);
        Map<String, Set<String>> serviceData = service.getServiceData();
        String firstServiceName = serviceData.keySet().iterator().next();
        
        assertTrue(html.contains(firstServiceName));
        //now, I select the first service in the tree
        serviceTree.setSelectedItem(serviceToSelect);
        //and now I get the data table in the view
        FlexTable table = view.getTable(ServiceMetric.TopVolume);
        assertNotNull(table);
        Widget cellContent = table.getWidget(1, 0);
        assertNotNull(cellContent);
//        //I need to get the first operation of firstServiceName
//        String firstOperationName = serviceData.get(firstServiceName);
//        assertEquals(firstOperationName, cellContent.getElement().getChild(0).getNodeValue());
    }

    @Override
    public String getModuleName() {
        return "org.ebayopensource.turmeric.monitoring.Console";
    }
}
