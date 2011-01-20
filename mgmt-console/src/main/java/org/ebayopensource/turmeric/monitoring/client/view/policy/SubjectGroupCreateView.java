/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.policy;

import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay;
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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SubjectGroupCreateView extends AbstractGenericView implements SubjectGroupCreateDisplay {
	
	private final static UserAction SELECTED_ACTION = UserAction.SUBJECT_GROUP_CREATE;
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
	    private Button createButton;
	    private Button cancelButton;

	    

	    public ContentView() {
	        mainPanel = new FlowPanel();
	        table = new FlexTable();
	        nameBox = new TextBox();
	        descBox = new TextArea();
	        createButton = new Button(ConsoleUtil.constants.create());
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
	        mainPanel.add(createButton);
	        mainPanel.add(cancelButton);
	    }
	    
	    public HasClickHandlers getCreateButton() {
	        return createButton;
	    }
	    
	    public HasClickHandlers getCancelButton() {
	        return cancelButton;
	    }
	    
	    public HasClickHandlers getSearchButton() {
	        return assignmentWidget.getSearchButton();
	    }

        /**
         * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getDescription()
         */
        public String getDescription() {
           return descBox.getText();
        }

        /**
         * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getName()
         */
        public String getName() {
            return nameBox.getText();
        }

        /**
         * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getSearchTerm()
         */
        public String getSearchTerm() {
            return assignmentWidget.getSearchTerm();
        }

        /**
         * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getSelectedSubjects()
         */
        public List<String> getSelectedSubjects() {
            return assignmentWidget.getSelectedSubjects();
        }

        /**
         * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getSubjectType()
         */
        public String getSubjectType() {
            return assignmentWidget.getSubjectType();
        }

        public void setAvailableSubjects (List<String> subjects) {
            assignmentWidget.setAvailableSubjects(subjects);
        }
	    
        public void setSubjectTypes (List<String> subjectTypes) {
            assignmentWidget.setAvailableSubjectTypes(subjectTypes);
        }
	}

	
	public SubjectGroupCreateView() {
	    scrollPanel = new ScrollPanel();
		mainPanel = new FlowPanel();
		scrollPanel.add(mainPanel);
		initWidget(scrollPanel);
		
		initialize();
	}
	
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
	


	public void activate() {
		contentView.activate();
		this.setVisible(true);
	}

	public Display getContentView() {
		return contentView;
	}


    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getCancelButton()
     */
    @Override
    public HasClickHandlers getCancelButton() {
       return ((ContentView)contentView).getCancelButton();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getCreateButton()
     */
    @Override
    public HasClickHandlers getCreateButton() {
        return ((ContentView)contentView).getCreateButton();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getDescription()
     */
    @Override
    public String getDescription() {
        return ((ContentView)contentView).getDescription();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getName()
     */
    @Override
    public String getName() {
        return ((ContentView)contentView).getName();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getSearchButton()
     */
    @Override
    public HasClickHandlers getSearchButton() {
        return ((ContentView)contentView).getSearchButton();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getSearchTerm()
     */
    @Override
    public String getSearchTerm() {
       return ((ContentView)contentView).getSearchTerm();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getSelectedSubjects()
     */
    @Override
    public List<String> getSelectedSubjects() {
       return ((ContentView)contentView).getSelectedSubjects();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#getSubjectType()
     */
    @Override
    public String getSubjectType() {
       return ((ContentView)contentView).getSubjectType();
    }

    @Override
    public void setAvailableSubjects(List<String> subjects) {
        ((ContentView)contentView).setAvailableSubjects(subjects);
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupCreatePresenter.SubjectGroupCreateDisplay#setSubjectTypes(java.util.List)
     */
    @Override
    public void setSubjectTypes(List<String> subjectTypes) {
        ((ContentView)contentView).setSubjectTypes(subjectTypes);
    }
    
    public void error (String msg) {
        ErrorDialog dialog = new ErrorDialog(true);
        dialog.setMessage(msg);
        dialog.getDialog().center();
        dialog.show();
    }
}
