/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.common;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.presenter.MenuController.MenuControllerDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationMenuView extends ResizeComposite implements HasWidgets, MenuControllerDisplay {

	private DockLayoutPanel mainPanel;
	private HeaderWidget headerWidget;
	private FooterWidget footerWidget;
	private Widget contentWidget;
	
	public ApplicationMenuView() {
	    initialize();
	    initWidget(mainPanel);
	}

	public void initialize() {
	    
	    mainPanel = new DockLayoutPanel(Unit.PX);
        mainPanel.setWidth("100%");

        headerWidget = new HeaderWidget("88");
        footerWidget = new FooterWidget();

        mainPanel.addNorth(headerWidget,88);
        mainPanel.addSouth(footerWidget,40);
	}

	
	

    /**
     * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public void add(Widget arg0) {
        if (contentWidget != null)
            mainPanel.remove(contentWidget);
        contentWidget = arg0;
        mainPanel.add(contentWidget);
    }

    /**
     * @see com.google.gwt.user.client.ui.HasWidgets#clear()
     */
    @Override
    public void clear() {
        if (contentWidget != null)
            mainPanel.remove(contentWidget);
        contentWidget = null;
    }

    /**
     * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
     */
    @Override
    public Iterator<Widget> iterator() {
        return mainPanel.iterator();
    }

    /**
     * @see com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public boolean remove(Widget arg0) {
        boolean result = false;
        if (contentWidget != null) {
            result = mainPanel.remove(contentWidget);
        }
        contentWidget = null;
        return result;
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.Container#asWidget()
     */
    @Override
    public Widget asWidget() {
        return this;
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.MenuController.MenuControllerDisplay#getLogoutComponent()
     */
    @Override
    public HasClickHandlers getLogoutComponent() {
       return headerWidget.getLogoutComponent();
    }

    @Override
    public void setUserName(String name) {
        // TODO Auto-generated method stub
        
    }

}
    