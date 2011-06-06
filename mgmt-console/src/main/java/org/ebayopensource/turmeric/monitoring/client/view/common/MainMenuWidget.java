/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.common;

import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.view.common.TemplateDisplay.MenuDisplay;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class MainMenuWidget.
 */
public class MainMenuWidget extends AbstractGenericView implements MenuDisplay {

	private final static int COLUMN_FOR_MENU_HEADER = 0;
	/**
	 * This attribute holds the specific row a component needs to be inserted.
	 */
	private final static Map<UserAction, Integer> MENU_ROW_MAPPER = new HashMap<UserAction, Integer>();
	static {
		MENU_ROW_MAPPER.put(UserAction.CONSOLE_MAIN, 1);
		MENU_ROW_MAPPER.put(UserAction.POLICY_MAIN, 2);
	}
	
	private SimplePanel mainPanel;
	private FlexTable menuTable;
	
	/**
	 * Instantiates a new main menu widget.
	 *
	 * @param selected the selected
	 */
	public MainMenuWidget(UserAction selected) {
		mainPanel = new SimplePanel();
		initWidget(mainPanel);

		initialize();
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.monitoring.client.Display#activate()
	 */
	@Override
    public void activate() {
		this.asWidget().setVisible(true);
	}
	
	private void setToMenuTable(int row, int column, Widget widget) {
		menuTable.setWidget(row, column, widget);
	}
	
	private void initHeaders() {
		Label menuHeader = new Label("Console Applications");
		setToMenuTable(0, COLUMN_FOR_MENU_HEADER, menuHeader);
		menuTable.getFlexCellFormatter().setColSpan(0, 0, 2);
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.monitoring.client.view.common.AbstractGenericView#initialize()
	 */
	@Override
	public void initialize() {
		mainPanel.clear();
		menuTable = new FlexTable();
		mainPanel.add(menuTable);
		menuTable.getColumnFormatter().setWidth(0, "10px");
		
		initHeaders();
		//initMenuLinks();
	}

	/**
	 * Change selection.
	 *
	 * @param action the action
	 */
	public void changeSelection(UserAction action) {
		// do nothing
	}

	/**
	 * Show component for access.
	 *
	 * @param action the action
	 */
	public void showComponentForAccess(UserAction action) {
		Integer row = MENU_ROW_MAPPER.get(action);
		if (row != null) {
			menuTable.getRowFormatter().setVisible(row, true);
		}
	}

	/**
	 * Hide component for access.
	 *
	 * @param action the action
	 */
	public void hideComponentForAccess(UserAction action) {
		Integer row = MENU_ROW_MAPPER.get(action);
		if (row != null) {
			menuTable.getRowFormatter().setVisible(row, false);
		}
	}
}
