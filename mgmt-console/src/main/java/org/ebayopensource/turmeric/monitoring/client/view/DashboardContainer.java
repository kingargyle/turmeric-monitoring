/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view;

import java.util.Iterator;

import org.ebayopensource.turmeric.monitoring.client.Dashboard;
import org.ebayopensource.turmeric.monitoring.client.Display;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class DashboardContainer extends ResizeComposite implements HasWidgets, Dashboard {
	protected TabLayoutPanel contentPanel;


	public DashboardContainer() {
	    configureContent();
	    initWidget(contentPanel);
	}
	
	public HasSelectionHandlers<Integer> getTabSelector () {
	    return contentPanel;
	}

	public TabLayoutPanel configureContent() {
		contentPanel = new TabLayoutPanel(2.5, Unit.EM);
		return contentPanel;
	}
	
	
	public int addView (Display view, String name) {
		this.contentPanel.add(view.asWidget(), name);
		return this.contentPanel.getWidgetIndex(view.asWidget());
	}
	
	
	public void activate(Display view) {
		this.contentPanel.selectTab(view.asWidget(), false);	
	}

	public int getIndex(Display view) {
		return this.contentPanel.getWidgetIndex(view.asWidget());
	}
	
	public Display getView (Integer index) {
	    return (Display)this.contentPanel.getWidget(index.intValue());
	}
	
	public Widget asWidget() {
		return this;
	}
	
	
	public TabLayoutPanel getContentPanel () {
		return this.contentPanel;
	}

	public void add(Widget arg0) {
	  //this.mainPanel.add(arg0);
	}

	public void clear() {
		//this.mainPanel.clear();
	}

	public Iterator<Widget> iterator() {
		//return this.mainPanel.iterator();
	    return null;
	}

	public boolean remove(Widget arg0) {
		//return this.mainPanel.remove(arg0);
	    return false;
	}
}
