/*******************************************************************************
 *   Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"); 
 *   you may not use this file except in compliance with the License. 
 *   You may obtain a copy of the License at 
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client;

import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.event.ObjectSelectionEvent;
import org.ebayopensource.turmeric.monitoring.client.model.DummyMetricsQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.ObjectType;
import org.ebayopensource.turmeric.monitoring.client.presenter.MenuController;
import org.ebayopensource.turmeric.monitoring.client.presenter.Presenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.ServicePresenter;
import org.ebayopensource.turmeric.monitoring.client.view.DashboardContainer;
import org.ebayopensource.turmeric.monitoring.client.view.ServiceView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;



/**
 * The Class UITest.
 */
public class UITest extends GWTTestCase{

    /**
     * Gets the module name.
     *
     * @return the module name
     * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
     */
    @Override
    public String getModuleName() {
        // TODO Auto-generated method stub
        return "org.ebayopensource.turmeric.monitoring.ConsoleJunit";
    }

    /**
     * Test list services.
     */
    public void testListServices(){
        final HistoryToken token = HistoryToken.newHistoryToken(MenuController.PRESENTER_ID);
        final HandlerManager eventBus = new HandlerManager(null);
        DummyMetricsQueryServiceImpl service = new DummyMetricsQueryServiceImpl();
        Dashboard dshbrd = new DashboardContainer();
        ServiceView view = new ServiceView(dshbrd);
        Presenter presenter = new ServicePresenter(eventBus, view, service);
        
        Map<ObjectType,String> selection = new HashMap<ObjectType, String>();
        selection.put(ObjectType.ServiceName, "Service1");
        //now, I select a service...
        eventBus.fireEvent(new ObjectSelectionEvent(selection));
        
        presenter.go(dshbrd,token);
        Tree serviceTree = (Tree)view.getSelector();
        TreeItem serviceName = null;
        int serviceCount = service.getServiceData().keySet().size();
        String[] serviceArray = service.getServiceData().keySet().toArray(new String[0]);
        String treeParentLabel = "Services("+serviceCount+")";
        assertEquals(treeParentLabel, serviceTree.getItem(0).getText());
        assertEquals(serviceCount, serviceTree.getItem(0).getChildCount());
        int returnedServiceListCount = serviceTree.getItem(0).getChildCount();
        for(int i = 0; i < returnedServiceListCount; i++){
            serviceName = serviceTree.getItem(0).getChild(i);
            assertEquals(serviceArray[i], serviceName.getText());
        }
      
    }
}
