/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.view.common.SelectBoxesWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * SubjectGroupAssigmentWidget
 *
 * Assign Subjects to a SubjectGroup.
 * If the Subject Group already exists, the Subject Type will be set, and this
 * widget will allow Subjects of that type to be added or subtracted from
 * the Group. If the Subject Type is not set, this widget allows it to be selected,
 * and then Subjects of that Type to be be added or subtracted from the Group.
 */
public class SubjectGroupAssignmentWidget extends Composite {
    
    protected String subjectType;
    protected ListBox subjectTypeBox;
    protected Label subjectTypeLabel;
    protected TextBox searchBox;
    protected Button searchButton;
    protected FlowPanel panel;
    protected FlexTable table;
    protected Button addSubjectButton;
    protected Button delSubjectButton;
    protected SelectBoxesWidget selectBoxes;


    /**
     * Constructor.
     * A SubjectType has not been selected at the time of the creation of the Widget.
     * The Widget will allow one to be selected, along with the Subjects of that SubjectType.
     * 
     */
    public SubjectGroupAssignmentWidget ()
    {
        panel = new FlowPanel();
        panel.addStyleName("subject-assignment-panel");
        table = new FlexTable();  
        table.setWidth("100%");
        panel.add(table);   
        createFields();
        positionFields();
        initWidget(panel);
    }

 

    public List<String> getSelectedSubjects () {
        return selectBoxes.getSelections();
    }
    
    public void clear () {
        List<String> emptyList = Collections.emptyList();
        setAvailableSubjects(emptyList);
        setSelectedSubjects(emptyList);
        searchBox.setText("");
    }
    
    public String getSubjectType () {
        int index = subjectTypeBox.getSelectedIndex();
        if (index < 0)
            return null;
        
        return subjectTypeBox.getItemText(index);
    }
    
    public String getSearchTerm () {
        return searchBox.getText();
    }
    public HasValue<String> getSearchBox () {
        return searchBox;
    }
    
    public HasClickHandlers getSearchButton() {
        return searchButton;
    }
    
    public void setAvailableSubjects (List<String> availableSubjects) {
        selectBoxes.setAvailables(availableSubjects);
    }
    
    public void setSelectedSubjects (List<String> selectedSubjects) {
        selectBoxes.setSelections(selectedSubjects);
    }
    
    
    public void setAvailableSubjectTypes (List<String> availableSubjectTypes) {
        //enable the selection of a subject type
        subjectTypeBox.clear();
        subjectTypeBox.setVisible(true);
        subjectTypeLabel.setVisible(true);
        if(availableSubjectTypes != null){
        	for (String s:availableSubjectTypes)
            subjectTypeBox.addItem(s);
        }
        List<String> emptyList = Collections.emptyList();
        setSelectedSubjects(emptyList);
        setAvailableSubjects(emptyList);
    }
    
    protected void createFields () {
        createSubjectTypeFields();
        createSearchFields();
        createSubjectsFields();
    }
    
    protected void positionFields () {
        // subjectTypeBox add it first
        positionSubjectTypeFields();
        
        //position the search box
        positionSearchFields();
        
        //position the subject boxes
        positionSubjectsFields();
    }
   
    
    private void positionSubjectTypeFields () {      
        table.setWidget(0, 0, subjectTypeLabel);
        table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
        table.getCellFormatter().setWordWrap(0, 0, false);
        table.setWidget(0, 1, subjectTypeBox);
    }
    
    private void createSubjectTypeFields () {
        subjectTypeBox = new ListBox(false);
        subjectTypeLabel = new Label(ConsoleUtil.policyAdminConstants.subjectType()+":");
        subjectTypeBox.setVisible(false);
        subjectTypeLabel.setVisible(false);
    }
    
    private void createSubjectsFields () {
        selectBoxes = new SelectBoxesWidget(ConsoleUtil.policyAdminConstants.availableSubjects(), true, ConsoleUtil.policyAdminConstants.selectedSubjects(), true);
    }
    
    
    private void positionSubjectsFields () {
        table.setWidget(2, 0, selectBoxes);
        table.getFlexCellFormatter().setColSpan(2, 0, 2);
    }
    
    private void createSearchFields (){
        searchBox = new TextBox();
        searchButton = new Button(ConsoleUtil.policyAdminConstants.searchSubjects());
    }
    
    private void positionSearchFields() {
        table.setWidget(1,0, searchButton);
        table.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);
        table.setWidget(1,1,searchBox);
    }
}
