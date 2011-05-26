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

/**
 * The Class DashboardContainer.
 */
public class DashboardContainer extends ResizeComposite implements HasWidgets, Dashboard {
	
	/** The content panel. */
	protected TabLayoutPanel contentPanel;


	/**
	 * Instantiates a new dashboard container.
	 */
	public DashboardContainer() {
	    configureContent();
	    initWidget(contentPanel);
	}
	
	/**
	 * Gets the tab selector.
	 *
	 * @return the tab selector
	 * @see org.ebayopensource.turmeric.monitoring.client.Dashboard#getTabSelector()
	 */
	public HasSelectionHandlers<Integer> getTabSelector () {
	    return contentPanel;
	}

	/**
	 * Configure content.
	 *
	 * @return the tab layout panel
	 */
	public TabLayoutPanel configureContent() {
		contentPanel = new TabLayoutPanel(2, Unit.EM);
		return contentPanel;
	}
	
	
	/**
	 * Adds the view.
	 *
	 * @param view the view
	 * @param name the name
	 * @return the int
	 * @see org.ebayopensource.turmeric.monitoring.client.Dashboard#addView(org.ebayopensource.turmeric.monitoring.client.Display, java.lang.String)
	 */
	public int addView (Display view, String name) {
		this.contentPanel.add(view.asWidget(), name);
		return this.contentPanel.getWidgetIndex(view.asWidget());
	}
	
	
	/**
	 * Activate.
	 *
	 * @param view the view
	 * @see org.ebayopensource.turmeric.monitoring.client.Dashboard#activate(org.ebayopensource.turmeric.monitoring.client.Display)
	 */
	public void activate(Display view) {
		this.contentPanel.selectTab(view.asWidget(), false);	
	}

	/**
	 * Gets the index.
	 *
	 * @param view the view
	 * @return the index
	 * @see org.ebayopensource.turmeric.monitoring.client.Dashboard#getIndex(org.ebayopensource.turmeric.monitoring.client.Display)
	 */
	public int getIndex(Display view) {
		return this.contentPanel.getWidgetIndex(view.asWidget());
	}
	
	/**
	 * Gets the view.
	 *
	 * @param index the index
	 * @return the view
	 * @see org.ebayopensource.turmeric.monitoring.client.Dashboard#getView(java.lang.Integer)
	 */
	public Display getView (Integer index) {
	    return (Display)this.contentPanel.getWidget(index.intValue());
	}
	
	/**
	 * As widget.
	 *
	 * @return the widget
	 * @see com.google.gwt.user.client.ui.Widget#asWidget()
	 */
	public Widget asWidget() {
		return this;
	}
	
	
	/**
	 * Gets the content panel.
	 *
	 * @return the content panel
	 */
	public TabLayoutPanel getContentPanel () {
		return this.contentPanel;
	}

	/**
	 * Adds the.
	 *
	 * @param arg0 the arg0
	 * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
	 */
	public void add(Widget arg0) {
	  //this.mainPanel.add(arg0);
	}

	/**
	 * Clear.
	 *
	 * @see com.google.gwt.user.client.ui.HasWidgets#clear()
	 */
	public void clear() {
		//this.mainPanel.clear();
	}

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
	 */
	public Iterator<Widget> iterator() {
		//return this.mainPanel.iterator();
	    return null;
	}

	/**
	 * Removes the widget.
	 *
	 * @param arg0 the arg0
	 * @return true, if successful
	 * @see com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client.ui.Widget)
	 */
	public boolean remove(Widget arg0) {
		//return this.mainPanel.remove(arg0);
	    return false;
	}
}
