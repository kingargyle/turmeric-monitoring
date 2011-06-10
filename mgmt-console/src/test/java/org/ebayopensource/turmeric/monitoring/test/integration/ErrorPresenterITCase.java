package org.ebayopensource.turmeric.monitoring.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.ebayopensource.turmeric.monitoring.client.model.ErrorMetric;
import org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter;
import org.ebayopensource.turmeric.monitoring.client.view.ErrorView;
import org.junit.Test;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class ErrorPresenterITCase extends ConsoleIntegrationTestBase {

    @Test
    public void testServicePresenterInitialization() {
        assertNotNull(errorPresenter);
        ErrorView view = (ErrorView) errorPresenter.getView();
        assertNotNull(view);
    }

    @Test
    public void testServiceSelectionFromServiceList() {
        assertNotNull(errorPresenter);
        ErrorView view = (ErrorView) errorPresenter.getView();
        assertNotNull(view);
        Tree serviceTree = (Tree) view.getSelector();
        assertNotNull(serviceTree);
        assertNotNull(serviceTree.getItem(0));// first level of the services
                                              // tree
        assertNotNull(serviceTree.getItem(0).getChild(0));// first service in
                                                          // the list
        TreeItem serviceToSelect = serviceTree.getItem(0).asTreeItem().getChild(0);
        String html = serviceToSelect.asTreeItem().getHTML();
        assertNotNull(html);
        Map<String, Set<String>> serviceData = service.getServiceData();
        String firstServiceName = serviceData.keySet().iterator().next();

        assertTrue(html.contains(firstServiceName));
        // now, I select the first service in the tree
        selectServiceForTab(ErrorPresenter.ERROR_ID, firstServiceName);
        // and now I get the data table in the view
        FlexTable table = view.getTable(ErrorMetric.TopApplicationErrors);
        assertNotNull(table);
        Widget cellContent = table.getWidget(1, 0);
        assertNotNull(cellContent);
        // //I need to get the first operation of firstServiceName
        NodeList<Node> childNodes = cellContent.getElement().getChildNodes();
        int childNodesLength = childNodes.getLength();
        for (int i = 0; i < childNodesLength; i++) {
            assertEquals("Error", childNodes.getItem(i).getNodeValue());
        }

    }

    @Test
    public void testDateSelectionInFilter() {
        assertNotNull(errorPresenter);
        ErrorView view = (ErrorView) errorPresenter.getView();
        assertNotNull(view);
        Tree serviceTree = (Tree) view.getSelector();
        assertNotNull(serviceTree);
        assertNotNull(serviceTree.getItem(0));// first level of the services
                                              // tree
        assertNotNull(serviceTree.getItem(0).getChild(0));// first service in
                                                          // the list
        TreeItem serviceToSelect = serviceTree.getItem(0).asTreeItem().getChild(0);
        String html = serviceToSelect.asTreeItem().getHTML();
        assertNotNull(html);
        Map<String, Set<String>> serviceData = service.getServiceData();
        String firstServiceName = serviceData.keySet().iterator().next();

        assertTrue(html.contains(firstServiceName));
    }

}
