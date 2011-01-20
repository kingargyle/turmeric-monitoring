/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.policy;

import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyImportPresenter.PolicyImportDisplay;
import org.ebayopensource.turmeric.monitoring.client.view.common.AbstractGenericView;
import org.ebayopensource.turmeric.monitoring.client.view.common.FooterWidget;
import org.ebayopensource.turmeric.monitoring.client.view.common.HeaderWidget;
import org.ebayopensource.turmeric.monitoring.client.view.common.PolicyMenuWidget;
import org.ebayopensource.turmeric.monitoring.client.view.common.PolicyTemplateDisplay.MenuDisplay;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class PolicyImportView extends AbstractGenericView implements PolicyImportDisplay {
	
	private final static UserAction SELECTED_ACTION = UserAction.POLICY_IMPORT;
	
	private DockLayoutPanel mainPanel;
	private MenuDisplay menuView;
	private Display contentView;
	private HasClickHandlers logoutComponent;
	
	public PolicyImportView() {
		mainPanel = new DockLayoutPanel(Unit.PX);
		initWidget(mainPanel);
		
		initialize();
	}
	
	@Override
	public void initialize() {
		mainPanel.clear();
		mainPanel.setWidth("100%");
	    mainPanel.add(initContentView());
	}
	

	
	protected Widget initContentView() {
		ScrollPanel actionPanel = new ScrollPanel();
	    contentView = new ContentView();
	    actionPanel.add(contentView.asWidget());
	    return actionPanel;
	}
	
	private class ContentView extends AbstractGenericView implements Display {
		private SimplePanel mainPanel;
		
		public ContentView() {
			mainPanel = new SimplePanel();
			initWidget(mainPanel);
			
			initialize();
		}
		
		public void activate() {
			// do nothing for now
		}

		@Override
		public void initialize() {
			mainPanel.clear();
			
			mainPanel.add(new Label("policy import here"));
		}
	}

	public void activate() {
		contentView.activate();
		this.setVisible(true);
	}

	public MenuDisplay getMenuView() {
		return menuView;
	}

	public Display getContentView() {
		return contentView;
	}
	
	public HasClickHandlers getLogoutComponent() {
		return logoutComponent;
	}

	public UserAction getActionSelected() {
		return UserAction.POLICY_IMPORT;
	}
}
