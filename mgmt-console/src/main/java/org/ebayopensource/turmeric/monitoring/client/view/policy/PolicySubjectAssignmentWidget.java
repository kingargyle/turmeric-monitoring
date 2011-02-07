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

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;


/**
 * PolicySubjectAssignmentWidget
 *
 */
public class PolicySubjectAssignmentWidget extends SubjectGroupAssignmentWidget {

    protected SelectBoxesWidget selectGroups;
    protected TextBox searchGroupBox;
    protected Button searchGroupButton;
    protected SelectBoxesWidget selectExclusionSubjects;
    protected SelectBoxesWidget selectExclusionSG;
    
    public PolicySubjectAssignmentWidget() {
        super();
    }
    
    public List<String> getSelectedGroups () {
        return selectGroups.getSelections();
    }
    
    public HasClickHandlers getGroupSearchButton () {
        return searchGroupButton;
    }
    
    public String getGroupSearchTerm () {
        return searchGroupBox.getText();
    }
    
    public void setAvailableSubjectTypes (List<String> availableSubjectTypes) {
        super.setAvailableSubjectTypes(availableSubjectTypes);
        List<String> emptyList = Collections.emptyList();
        setSelectedGroups(emptyList);
        setAvailableGroups(emptyList);
    }
    
    public void setAvailableGroups(List<String> availableGroups) {
        selectGroups.setAvailables(availableGroups);
    }
    
    public void setSelectedGroups(List<String> selectedGroups) {
        selectGroups.setSelections(selectedGroups);
    }
    
    public void clear () {
        super.clear();
        List<String> emptyList = Collections.emptyList();
        setAvailableGroups(emptyList);
        setSelectedGroups(emptyList);
        
        setAvailableExclusionSubjects(emptyList);
        setSelectedExclusionSubjects(emptyList);
        searchGroupBox.setText("");
    }
    
    protected void createFields() {
        super.createFields();
        createExclusionSubjectFields();
        
        createGroupSearchFields();
        createGroupsFields();
        createExclusionSGFields();

    }
    
    protected void positionFields () {
        super.positionFields();
        positionExclusionSubjectFields();
        
        positionGroupSearchFields();
        positionGroupsFields();
        positionExclusionSGFields();
    }
    
    protected void positionGroupSearchFields() {
        int newRow = table.getRowCount();
        table.setWidget(newRow,0, searchGroupButton);
        table.getCellFormatter().setHorizontalAlignment(newRow, 0, HasHorizontalAlignment.ALIGN_LEFT);
        table.setWidget(newRow,1,searchGroupBox);
    }
    

    protected void createGroupSearchFields () {
        searchGroupBox = new TextBox();
        searchGroupButton = new Button(ConsoleUtil.policyAdminConstants.searchGroups());
    }
    
    
    protected void positionGroupsFields() {
        int newRow = table.getRowCount();
        table.setWidget(newRow, 0, selectGroups);
        table.getFlexCellFormatter().setColSpan(newRow, 0, 2);                
    }
    
    protected void positionExclusionSubjectFields() {
    	 int newRow = table.getRowCount();
    	 table.setWidget(newRow, 0, selectExclusionSubjects);
         table.getFlexCellFormatter().setColSpan(newRow, 0, 2);         
    }
   
    protected void positionExclusionSGFields() {
   	 int newRow = table.getRowCount();
   	 table.setWidget(newRow, 0, selectExclusionSG);
        table.getFlexCellFormatter().setColSpan(newRow, 0, 2);         
   }
    
    /**
     * 
     */
    protected void createGroupsFields() {
        selectGroups = new SelectBoxesWidget(ConsoleUtil.policyAdminConstants.availableSubjectGroups(), true, ConsoleUtil.policyAdminConstants.selectedSubjectGroups(), true);
    }

    protected void createExclusionSubjectFields() {
        selectExclusionSubjects = new SelectBoxesWidget(ConsoleUtil.policyAdminConstants.availableSubjects(), true, ConsoleUtil.policyAdminConstants.selectedExclusionSubject(), true);
    }

    protected void createExclusionSGFields() {
        selectExclusionSG = new SelectBoxesWidget(ConsoleUtil.policyAdminConstants.availableSubjectGroups(), true, ConsoleUtil.policyAdminConstants.selectedExclusionSG(), true);
    }

    
    public List<String> getSelectedExclusionSubjects () {
        return selectExclusionSubjects.getSelections();
    }
    
   
    public List<String> getSelectedExclusionSG() {
        return selectExclusionSG.getSelections();
    }
    
    public void setAvailableExclusionSubjects(final List<String> availableExclusionSubjects) {
    	selectExclusionSubjects.setAvailables(availableExclusionSubjects);
    }
 
    public void setAvailableExclusionSG(final List<String> availableExclusionSG) {
    	selectExclusionSG.setAvailables(availableExclusionSG);
    }
    
    public void setSelectedExclusionSubjects(final List<String> availableExclusionSubjects) {
    	selectExclusionSubjects.setSelections(availableExclusionSubjects);
    }
    
    public void setSelectedExclusionSG(final List<String> availableExclusionSG) {
    	selectExclusionSG.setSelections(availableExclusionSG);
    }
}
