/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.policy;

import java.util.Collections;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.view.common.SelectBoxesWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

/**
 * PolicyResourceAssignmentWidget
 *
 * Assign resources to a Policy.
 */
public class PolicyResourceAssignmentWidget extends Composite {
    
	protected Label resourceLevelLabel;
    protected ListBox resourceLevelBox;
    
    protected Label resourceTypeLabel;
    protected ListBox resourceTypeBox;
    
    protected Label resourceNameLabel;
    protected ListBox resourceNameBox;
    
    protected FlowPanel panel;
    protected FlexTable table;
    
    protected Button addOperationButton;
    protected Button delOperationButton;
    
    protected SelectBoxesWidget selectBoxes;


    public PolicyResourceAssignmentWidget()
    {
        panel = new FlowPanel();
        panel.addStyleName("resource-assignment-panel");
        table = new FlexTable();  
        table.setWidth("100%");
        panel.add(table);   
        createFields();
        positionFields();
        initWidget(panel);
    }

 

    public List<String> getSelectedOperations() {
        return selectBoxes.getSelections();
    }
  
    public List<String> getAvailableOperations() {
        return selectBoxes.getAvailables();
    }
    
    public Label getResourceNameLabel() {
        return resourceNameLabel;
    }
    
    public Label getResourceTypeLabel() {
        return resourceTypeLabel;
    }
    
    public Label getResourceLevelLabel() {
        return resourceLevelLabel;
    }
    
    public SelectBoxesWidget getSelectBoxesWidget(){
    	return selectBoxes;
    }
    
    public void clear () {
        List<String> emptyList = Collections.emptyList();
        setAvailableOperations(emptyList);
        setSelectedOperations(emptyList);
        resourceLevelBox.setSelectedIndex(-1);
        resourceTypeBox.setSelectedIndex(-1);
        resourceTypeBox.setVisible(false);
        resourceNameBox.setSelectedIndex(-1);
        resourceNameBox.setVisible(false);
        resourceNameLabel.setVisible(false);
        resourceTypeLabel.setVisible(false);
        selectBoxes.setVisible(false);
    }
    
    
    public ListBox getResourceTypeBox(){
    	return resourceTypeBox;	
    }
    

    public ListBox getResourceLevelBox(){
    	return resourceLevelBox;	
    }
    
    public ListBox getResourceNameBox(){
    	return resourceNameBox;	
    }
    
    
    public String getResourceType() {
        int index = resourceTypeBox.getSelectedIndex();
        if (index < 0)
            return null;
        
        return resourceTypeBox.getItemText(index);
    }
    
    public String getResourceName() {
        int index = resourceNameBox.getSelectedIndex();
        if (index < 0)
            return null;
        
        return resourceNameBox.getItemText(index);
    }
    
    public String getResourceLevel() {
        int index = resourceLevelBox.getSelectedIndex();
        if (index < 0)
            return null;
        
        return resourceLevelBox.getItemText(index);
    }
    
    public void setAvailableOperations (List<String> availableOperations) {
        selectBoxes.setAvailables(availableOperations);
    }
    
    public void setSelectedOperations (List<String> selectedOperations) {
        selectBoxes.setSelections(selectedOperations);
    }
    
    public void setResourceLevels (List<String> resourceLevels) {
        //enable the selection of a resource level
        resourceLevelBox.clear();
        resourceLevelBox.setVisible(true);
        resourceLevelLabel.setVisible(true);
        for (String s:resourceLevels)
        	resourceLevelBox.addItem(s);
        List<String> emptyList = Collections.emptyList();
        setResourceTypes(emptyList);
        setResourceNames(emptyList);
        setSelectedOperations(emptyList);
        setAvailableOperations(emptyList);
    }
    
    public void setResourceTypes (List<String> availableResourceTypes) {
        //enable the selection of a resource type
        resourceTypeBox.clear();
//        resourceTypeBox.setVisible(true);
//        resourceTypeLabel.setVisible(true);
        for (String s:availableResourceTypes)
        	resourceTypeBox.addItem(s);
        List<String> emptyList = Collections.emptyList();
        setResourceNames(emptyList);
        setSelectedOperations(emptyList);
        setAvailableOperations(emptyList);
    }
    
    public void setResourceNames (List<String> availableResourceNames) {
        //enable the selection of a resource type
        resourceNameBox.clear();
//        resourceNameBox.setVisible(true);
//        resourceNameLabel.setVisible(true);
        for (String s:availableResourceNames)
        	resourceNameBox.addItem(s);
        List<String> emptyList = Collections.emptyList();
        setSelectedOperations(emptyList);
        setAvailableOperations(emptyList);
    }
    
    protected void createFields () {
        createResourceFields();
        createOperationFields();
    }
    
    protected void positionFields () {
    	 // resourceLevelBox add it first
        positionResourceLevelFields();
    	
    	// resourceTypeBox 
        positionResourceTypeFields();
        
        // resourceNameBox
        positionResourceNameFields();
        
        //position the operation boxes
        positionOperationFields();
    }
   
    
  
    
    private void createResourceFields () {
       	resourceLevelBox = new ListBox(false);
        resourceLevelLabel = new Label(ConsoleUtil.policyAdminConstants.resourceLevel()+":");
        resourceLevelBox.setVisible(false);
        resourceLevelLabel.setVisible(false);
    	
    	resourceTypeBox = new ListBox(false);
        resourceTypeLabel = new Label(ConsoleUtil.policyAdminConstants.resourceType()+":");
        resourceTypeBox.setVisible(false);
        resourceTypeLabel.setVisible(false);
        
        resourceNameBox = new ListBox(false);
        resourceNameLabel = new Label(ConsoleUtil.policyAdminConstants.resourceName()+":");
        resourceNameBox.setVisible(false);
        resourceNameLabel.setVisible(false);
    }
    
    private void createOperationFields () {
        selectBoxes = new SelectBoxesWidget(ConsoleUtil.policyAdminConstants.availableOperations(), true, ConsoleUtil.policyAdminConstants.selectedOperations(), true);
        selectBoxes.setVisible(false);
    }
  
    private void positionResourceLevelFields () {      
        table.setWidget(0, 0, resourceLevelLabel);
        table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
        table.getCellFormatter().setWordWrap(0, 0, false);
        table.setWidget(0, 1, resourceLevelBox);
    }
    
    private void positionResourceTypeFields () {      
        table.setWidget(1, 0, resourceTypeLabel);
        table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
        table.getCellFormatter().setWordWrap(0, 0, false);
        table.setWidget(1, 1, resourceTypeBox);
    }
    
    private void positionResourceNameFields () {      
        table.setWidget(2, 0, resourceNameLabel);
        table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
        table.getCellFormatter().setWordWrap(0, 0, false);
        table.setWidget(2, 1, resourceNameBox);
    }
    
    private void positionOperationFields () {
        table.setWidget(4, 0, selectBoxes);
        table.getFlexCellFormatter().setColSpan(4, 0, 4);
    }
    
   
}
