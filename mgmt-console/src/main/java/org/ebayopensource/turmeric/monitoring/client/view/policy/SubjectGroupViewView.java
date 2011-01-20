/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.policy;

import java.util.Collections;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupViewPresenter.SubjectGroupViewDisplay;
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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * SubjectGroupViewView
 *
 */
public class SubjectGroupViewView extends AbstractGenericView implements
        SubjectGroupViewDisplay {
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
        private Label nameBox;
        private Label descBox;
        private Label typeBox;
        private Grid grid;
        private Button cancelButton;

        
        public ContentView() {
            mainPanel = new FlowPanel();
            table = new FlexTable();
            typeBox = new Label();
            typeBox.addStyleName("nonselectable");
            nameBox = new Label();
            nameBox.addStyleName("nonselectable");
            descBox = new Label();
            descBox.addStyleName("nonselectable");
            cancelButton = new Button(ConsoleUtil.constants.cancel());
            grid = new Grid();
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
            table.setWidget(2, 0, new Label(ConsoleUtil.policyAdminConstants.subjectType()+":"));
            table.setWidget(2, 1, typeBox);
            table.setWidget(3, 0, new Label(ConsoleUtil.policyAdminConstants.subjects()+":"));
            table.getCellFormatter().setVerticalAlignment(3, 0, HasVerticalAlignment.ALIGN_TOP);
            //put in the subjects
            table.setWidget(3, 1, grid);    
            mainPanel.add(table);
            mainPanel.add(cancelButton);
        }
        
        public void setType (String type) {
            typeBox.setText(type);
        }
        
        public String getType () {
            return typeBox.getText();
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
        
        public HasClickHandlers getCancelButton() {
            return cancelButton;
        }

        public List<String> getSubjects() {
           //TODO
            return null;
        }
        
        public void setSubjects(List<String> subjects) {
            if (subjects == null) {
                grid.clear(true);
                grid.resize(0, 0);
            } else {
                grid.resize(subjects.size(), 1);
                int i = 0;
                for (String s:subjects) {
                    Label l = new Label(s);
                    l.addStyleName("nonselectable");
                    grid.setWidget(i++, 0, l);
                }
            }
        } 
    }
    
    
    public SubjectGroupViewView() {
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
  
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupViewPresenter.SubjectGroupViewDisplay#clear()
     */
    @Override
    public void clear() {
       ((ContentView)contentView).setName("");
       ((ContentView)contentView).setType("");
       ((ContentView)contentView).setDescription("");
       List<String> emptyList = Collections.emptyList();
       ((ContentView)contentView).setSubjects(emptyList);
    }

    
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupViewPresenter.SubjectGroupViewDisplay#error(java.lang.String)
     */
    @Override
    public void error(String msg) {
        ErrorDialog dialog = new ErrorDialog(true);
        dialog.setMessage(msg);
        dialog.getDialog().center();
        dialog.show();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupViewPresenter.SubjectGroupViewDisplay#getCancelButton()
     */
    @Override
    public HasClickHandlers getCancelButton() {
        return ((ContentView)contentView).getCancelButton();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupViewPresenter.SubjectGroupViewDisplay#getDescription()
     */
    @Override
    public String getDescription() {
       return ((ContentView)contentView).getDescription();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupViewPresenter.SubjectGroupViewDisplay#getName()
     */
    @Override
    public String getName() {
        return ((ContentView)contentView).getName();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupViewPresenter.SubjectGroupViewDisplay#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String desc) {  
        ((ContentView)contentView).setDescription(desc);
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupViewPresenter.SubjectGroupViewDisplay#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        ((ContentView)contentView).setName(name);
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupViewPresenter.SubjectGroupViewDisplay#setSubjects(java.util.List)
     */
    @Override
    public void setSubjects(List<String> subjects) {
        ((ContentView)contentView).setSubjects(subjects);
    }



    /**
     * @see org.ebayopensource.turmeric.monitoring.client.Display#activate()
     */
    @Override
    public void activate() {
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupViewPresenter.SubjectGroupViewDisplay#getType()
     */
    @Override
    public String getType() {
      return ((ContentView)contentView).getType();
    }


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupViewPresenter.SubjectGroupViewDisplay#setType(java.lang.String)
     */
    @Override
    public void setType(String type) {
        ((ContentView)contentView).setType(type);
    }

}
