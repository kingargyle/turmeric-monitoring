/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view;

import java.util.Iterator;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.PolicyDashboard;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.view.common.FooterWidget;
import org.ebayopensource.turmeric.monitoring.client.view.common.HeaderWidget;
import org.ebayopensource.turmeric.monitoring.client.view.common.PolicyMenuWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * PolicyContainer
 *
 */
public class PolicyContainer extends ResizeComposite implements HasWidgets, PolicyDashboard {

    protected SplitLayoutPanel contentPanel;
    Widget policyContent;
    PolicyMenuWidget menuWidget;

    public PolicyContainer() {
        configureContent();
        initWidget(contentPanel);
    }
    
    protected SplitLayoutPanel configureContent() {
        contentPanel = new SplitLayoutPanel();
        contentPanel.addWest(initMenuView(), 150);
        return contentPanel;
    }

    protected Widget initMenuView() {
        ScrollPanel scroller = new ScrollPanel();
        menuWidget = new PolicyMenuWidget();
        scroller.add(menuWidget);
        return scroller;
    }


    public void add(Widget arg0) {
        if (policyContent != null) {
            contentPanel.remove(policyContent);
        }

        GWT.log(arg0.getClass().getName() + "as policy content");
        policyContent = arg0;
        contentPanel.add(policyContent);
    }
    
    public void clear() {
        if (policyContent != null) {
            contentPanel.remove(policyContent);
            policyContent = null;
        }
        this.onResize();
    }

    public Iterator<Widget> iterator() {
        return this.contentPanel.iterator();
    }

    public boolean remove(Widget arg0) {
        boolean result = contentPanel.remove(arg0);
        if (arg0 == policyContent)
            policyContent = null;
            
        return result;
        
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.view.PolicyDashboard#activate(org.ebayopensource.turmeric.monitoring.client.Display)
     */
    @Override
    public void activate(Display view) {
        // TODO Auto-generated method stub
        
    }

    public void setActions(List<UserAction> actions) {
        this.menuWidget.setActions(actions);
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.view.PolicyDashboard#getSelector()
     */
    @Override
    public HasSelectionHandlers<TreeItem> getSelector() {
        return menuWidget.getSelector();
    }
    
    public void setSelected (UserAction action) {
        GWT.log("UserAction selected:"+action);
        this.menuWidget.setSelected(action);
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.PolicyDashboard#error(java.lang.String)
     */
    @Override
    public void error(String err) {
        ErrorDialog dialog = new ErrorDialog(true);
        dialog.setMessage(err);
        dialog.getDialog().center();
        dialog.show(); 
    }

}
