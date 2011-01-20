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
        searchGroupBox.setText("");
    }
    
    protected void createFields() {
        super.createFields();
        createGroupSearchFields();
        createGroupsFields();
    }
    
    protected void positionFields () {
        super.positionFields();
        positionGroupSearchFields();
        positionGroupsFields();
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
    
    /**
     * 
     */
    protected void createGroupsFields() {
        selectGroups = new SelectBoxesWidget(ConsoleUtil.policyAdminConstants.availableSubjectGroups(), true, ConsoleUtil.policyAdminConstants.selectedSubjectGroups(), true);
    }

}
