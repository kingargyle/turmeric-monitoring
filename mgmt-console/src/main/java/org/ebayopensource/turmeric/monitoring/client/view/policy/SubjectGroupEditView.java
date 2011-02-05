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
import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupEditPresenter.SubjectGroupEditDisplay;
import org.ebayopensource.turmeric.monitoring.client.view.ErrorDialog;
import org.ebayopensource.turmeric.monitoring.client.view.common.AbstractGenericView;
import org.ebayopensource.turmeric.monitoring.client.view.common.FooterWidget;
import org.ebayopensource.turmeric.monitoring.client.view.common.HeaderWidget;
import org.ebayopensource.turmeric.monitoring.client.view.common.PolicyMenuWidget;
import org.ebayopensource.turmeric.monitoring.client.view.common.PolicyTemplateDisplay.MenuDisplay;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * SubjectGroupEditView
 *
 */
public class SubjectGroupEditView extends AbstractGenericView implements SubjectGroupEditDisplay {
    private ScrollPanel scrollPanel;
    private FlowPanel mainPanel;
    private Display contentView;
    
    
    
    /**
     * ContentView
     *
     */
    private class ContentView extends AbstractGenericView implements  Display {
        private FlowPanel mainPanel;
        private FlexTable table;
        private TextBox nameBox;
        private TextArea descBox;
        private SubjectGroupAssignmentWidget assignmentWidget;
        private Button saveButton;
        private Button cancelButton;

        
        public ContentView() {
            mainPanel = new FlowPanel();
            table = new FlexTable();
            nameBox = new TextBox();
            descBox = new TextArea();
            saveButton = new Button(ConsoleUtil.constants.apply());
            cancelButton = new Button(ConsoleUtil.constants.cancel());
            initWidget(mainPanel);

            initialize();
        }

        public void activate() {  
            
        }
        
        @Override
        public void initialize() {
            mainPanel.clear();
            table.setWidget(0, 0, new Label(ConsoleUtil.policyAdminConstants.subjectGroupName()+":"));
            table.setWidget(0, 1, nameBox);
            table.setWidget(1, 0, new Label(ConsoleUtil.policyAdminConstants.subjectGroupDescription()+":"));
            table.setWidget(1, 1, descBox);
            assignmentWidget = new SubjectGroupAssignmentWidget();
            table.setWidget(2, 0, new Label(ConsoleUtil.policyAdminConstants.subjects()+":"));
            table.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_TOP);
            table.setWidget(2, 1, assignmentWidget);
            
            
            mainPanel.add(table);
            mainPanel.add(saveButton);
            mainPanel.add(cancelButton);
        }
        
        public void setDescription (String desc) {
            descBox.setText(desc);
        }
        
        public String getDescription () {
            return descBox.getText();
        }
        
        public void setName (String name) {
            nameBox.setText(name);
        }
        
        public String getName () {
            return nameBox.getText();
        }
        
        public Button getSaveButton() {
            return saveButton;
        }
        
        public HasClickHandlers getCancelButton() {
            return cancelButton;
        }
        
        public HasClickHandlers getSearchButton() {
            return assignmentWidget.getSearchButton();
        }
        
        public String getSearchTerm() {
            return assignmentWidget.getSearchTerm();
        }

        public List<String> getSelectedSubjects() {
            return assignmentWidget.getSelectedSubjects();
        }
        
        public void setSelectedSubjects(List<String> selectedSubjects) {
            assignmentWidget.setSelectedSubjects(selectedSubjects);
        } 
        
        public void setAvailableSubjects(List<String> selectedSubjects) {
            assignmentWidget.setAvailableSubjects(selectedSubjects);
        }
    }

    
    public SubjectGroupEditView() {
        scrollPanel = new ScrollPanel();
        mainPanel = new FlowPanel();
        scrollPanel.add(mainPanel);
        initWidget(scrollPanel);
        
        initialize();
    }

    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.view.common.AbstractGenericView#initialize()
     */
    @Override
    public void initialize() {
        mainPanel.clear();
        mainPanel.add(initContentView());
    }
 
    
    protected Widget initContentView() {
        ScrollPanel actionPanel = new ScrollPanel();
        contentView = new ContentView();
        actionPanel.add(contentView.asWidget());
        return actionPanel;
    }
    

    public Display getContentView() {
        return contentView;
    }
   



    public void activate() {
        contentView.activate();
        this.setVisible(true);
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupEditPresenter.SubjectGroupEditDisplay#getDescription()
     */
    @Override
    public String getDescription() {
        return ((ContentView)contentView).getDescription();
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupEditPresenter.SubjectGroupEditDisplay#getName()
     */
    @Override
    public String getName() {
        return ((ContentView)contentView).getName();
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupEditPresenter.SubjectGroupEditDisplay#getSelectedSubjects()
     */
    @Override
    public List<String> getSelectedSubjects() {
        return ((ContentView)contentView).getSelectedSubjects();
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupEditPresenter.SubjectGroupEditDisplay#setAvailableSubjects(java.util.List)
     */
    @Override
    public void setAvailableSubjects(List<String> subjects) {
         ((ContentView)contentView).setAvailableSubjects(subjects);
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupEditPresenter.SubjectGroupEditDisplay#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String desc) {
        ((ContentView)contentView).setDescription(desc);
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupEditPresenter.SubjectGroupEditDisplay#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        ((ContentView)contentView).setName(name);
    }
    
    
    public void error (String msg) {
        ErrorDialog dialog = new ErrorDialog(true);
        dialog.setMessage(msg);
        dialog.getDialog().center();
        dialog.show();
    }

    public void clear () {
        setName("");
        setDescription("");
        List<String> empty = Collections.emptyList();
        setSelectedSubjects(empty);
        setAvailableSubjects(empty); 
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupEditPresenter.SubjectGroupEditDisplay#setSelectedSubjects()
     */
    @Override
    public void setSelectedSubjects(List<String> subjects) {
        ((ContentView)contentView).setSelectedSubjects(subjects);
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupEditPresenter.SubjectGroupEditDisplay#getSearchTerm()
     */
    @Override
    public String getSearchTerm() {
       return ((ContentView)contentView).getSearchTerm();
    }
    
    public HasClickHandlers getSearchButton() {
        return ((ContentView)contentView).getSearchButton();
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupEditPresenter.SubjectGroupEditDisplay#getApplyButton()
     */
    @Override
    public Button getApplyButton() {
        return ((ContentView)contentView).getSaveButton();
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupEditPresenter.SubjectGroupEditDisplay#getCancelButton()
     */
    @Override
    public HasClickHandlers getCancelButton() {
        return ((ContentView)contentView).getCancelButton();
    }
    
}
