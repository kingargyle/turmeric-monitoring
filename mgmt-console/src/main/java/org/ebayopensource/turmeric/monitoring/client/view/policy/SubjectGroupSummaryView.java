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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay;
import org.ebayopensource.turmeric.monitoring.client.view.ErrorDialog;
import org.ebayopensource.turmeric.monitoring.client.view.common.AbstractGenericView;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.RangeChangeEvent;

public class SubjectGroupSummaryView extends AbstractGenericView implements SubjectGroupSummaryDisplay {
	private ScrollPanel scrollPanel;
	private FlowPanel mainPanel;
	private Display contentView;
	
	
	/**
	 * SubjectSearchWidget
	 *
	 */
	private class SubjectSearchWidget extends Composite {
	    private FlowPanel mainPanel;
	    private FlowPanel radioPanel;
	    private RadioButton subjectCriteriaButton;
	    private RadioButton policyCriteriaButton;
	    private Button searchButton;
	    private TextBox searchBox;
	    private Label typeLabel;
	    private ListBox typeBox;
	    
	    public SubjectSearchWidget() {
	        mainPanel = new FlowPanel();
	        
	        Grid grid = new Grid(3, 2);
	        //Search for a SubjectGroup by: SubjectType + Name or PolicyType + Name
	        radioPanel = new FlowPanel();
	        subjectCriteriaButton = new RadioButton("Criteria", ConsoleUtil.policyAdminConstants.subjectCriteria());
	        subjectCriteriaButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    typeLabel.setText(ConsoleUtil.policyAdminConstants.subjectType());
                    searchButton.setText(ConsoleUtil.policyAdminConstants.search());
                }
	        });
	        policyCriteriaButton = new RadioButton("Criteria", ConsoleUtil.policyAdminConstants.policyCriteria());
	        policyCriteriaButton.addClickHandler(new ClickHandler() {
	            public void onClick(ClickEvent event) {
	                typeLabel.setText(ConsoleUtil.policyAdminConstants.policyType());
	                searchButton.setText(ConsoleUtil.policyAdminConstants.search());
	            }
	        });
	        radioPanel.add(subjectCriteriaButton);
	        radioPanel.add(policyCriteriaButton);
	        
	        typeLabel = new Label(ConsoleUtil.policyAdminConstants.subjectType()+":");
	        typeBox = new ListBox(false);
	        searchButton = new Button(ConsoleUtil.policyAdminConstants.search());
	        searchBox = new TextBox();
	        grid.setWidget(0, 0, radioPanel);
	        grid.setWidget(1, 0, typeLabel);
	        grid.setWidget(1, 1, typeBox);
	        grid.setWidget(2, 0, searchButton);
	        grid.setWidget(2, 1, searchBox);
	        
	        
	        mainPanel.add(grid);
	        initWidget(mainPanel);
	    }
	    
	    public void clear () {
	        //select nothing
	        subjectCriteriaButton.setValue(false);
	        policyCriteriaButton.setValue(false);
	        searchBox.setText("");
	    }
	    
	    
	    public HasClickHandlers getSubjectCriteriaButton() {
	        return subjectCriteriaButton;
	    }
	    
	    public HasClickHandlers getPolicyCriteriaButton() {
	        return policyCriteriaButton;
	    }
	    
	    public HasClickHandlers getSearchButton() {
	        return searchButton;
	    }
	    
	    public boolean isSubjectCriteriaEnabled() {
	        return subjectCriteriaButton.getValue()==true;
	    }
	    
	    public void setSubjectCriteriaEnabled(boolean enabled) {
	        subjectCriteriaButton.setValue(enabled);
	    }
	    
	    public boolean isPolicyCriteriaEnabled() {
	        return policyCriteriaButton.getValue()==true;
	    }
	    
	    public void setPolicyCriteriaEnabled(boolean enabled) {
	        policyCriteriaButton.setValue(enabled);
	    }
	    
	    public String getSearchTerm () {
	        return searchBox.getValue();
	    }

	    public String getSelectedType() {
	        if (typeBox.getSelectedIndex() >= 0)
	            return typeBox.getItemText(typeBox.getSelectedIndex());
	        else
	            return null;
	    }

	    public void setAvailableTypes (List<String> types) {
	        //enable the selection of a subject type
	        typeBox.clear();
	        if (types != null) {
	            for (String s:types)
	                typeBox.addItem(s);
	        }
	    }

	    public void setSelectedType (String type) {
	        if (type == null)
	            return;
	        
	        int idx = -1;
	        for (int i=0;i<typeBox.getItemCount() && idx <0; i++) {
	            if (typeBox.getItemText(i).equals(type))
	                idx = i;
	        }
	        if (idx >= 0)
	            typeBox.setSelectedIndex(idx);
	    }
	    
	    public void setSelectedSearchTerm (String term) {
	        searchBox.setValue((term==null?"":term));
	    }
	    
	}
	
	
	

	private class ContentView extends AbstractGenericView implements Display {
	    private FlowPanel mainPanel;
	    private CellTable<SubjectGroup> cellTable;
	    ProvidesKey<SubjectGroup> keyProvider;
	    DisclosurePanel searchPanel;
	    SubjectSearchWidget searchWidget;
	    ListDataProvider<SubjectGroup> dataProvider;
	    PushButton actionButton = new PushButton(ConsoleUtil.constants.apply());
	    final Map<SubjectGroup, UserAction> pendingActions = new HashMap<SubjectGroup, UserAction>();
	    Map<SubjectGroup, List<UserAction>> permittedActions = new HashMap<SubjectGroup, List<UserAction>>();
	    
	  
	    
	    public ContentView() {
	        mainPanel = new FlowPanel();
	        initWidget(mainPanel);
	        initialize();
	    }


	    public void setGroups(List<SubjectGroup> groups) {
	        cellTable.setRowCount(0);
	        List<SubjectGroup> list;
	        if (groups == null)
	            list = Collections.emptyList();
	        else
	            list = groups;
	        dataProvider.setList(list);
	        dataProvider.refresh();
	    }
	    
	  
	    HasClickHandlers getActionButton () {
	        return actionButton;
	    }
	    
	    public void setUserActions(SubjectGroup group, List<UserAction> enabledActions) {
	        if (group == null)
	            return;
	        if (enabledActions == null)
	            permittedActions.remove(group);
	        else
	            permittedActions.put(group, new ArrayList<UserAction>(enabledActions));
	       if (cellTable != null)
	           cellTable.redraw();
	    }

	    public void activate() {
	        updateButton(null);
	        actionButton.setEnabled(false);
	    }

	    @Override
	    public void initialize() {
	        mainPanel.clear();
	        
	        //top part of contentPanel is a disclosure panel with a search feature
	        searchWidget = new SubjectSearchWidget();
	        searchPanel = new DisclosurePanel(ConsoleUtil.policyAdminConstants.search());
	        searchPanel.setContent(searchWidget);
	       
	        
	        
	        //bottom part of panel is a table with search results
	        Grid summaryGrid = new Grid (3, 1);
	        summaryGrid.setStyleName("sggrid");
	      
	        Grid buttonGrid = new Grid(1,1);
	        buttonGrid.setWidget(0,0,actionButton);
	        summaryGrid.setWidget(0,0, buttonGrid);
	        keyProvider = new ProvidesKey<SubjectGroup>() {
	            public Object getKey(SubjectGroup group) {
	                return group == null ? null : group.getName();
	            }
	        };

	        cellTable = new CellTable<SubjectGroup>(keyProvider);
	        dataProvider = new ListDataProvider<SubjectGroup>();
	        dataProvider.addDataDisplay(cellTable);
            SimplePager pager = new SimplePager();
            pager.setDisplay(cellTable);

           
	        //checkbox column for view selection
	        Column<SubjectGroup, SubjectGroup> viewColumn = new Column<SubjectGroup, SubjectGroup>(
	                new CustomPermissionCheckboxCell<SubjectGroup, Boolean>(UserAction.SUBJECT_GROUP_VIEW, pendingActions, permittedActions)) {
	            public SubjectGroup getValue(SubjectGroup group) {
	                return group;
	            }
	        };
	        viewColumn.setFieldUpdater(new FieldUpdater<SubjectGroup, SubjectGroup>() {

                @Override
                public void update(int arg0, SubjectGroup arg1,SubjectGroup arg2) {
                    //if it was in there, remove it
                    if (pendingActions.keySet().contains(arg1)) {
                        pendingActions.remove(arg1);
                        updateButton(null);
                    } else {
                        pendingActions.clear();
                        pendingActions.put(arg1, UserAction.SUBJECT_GROUP_VIEW);
                        updateButton(UserAction.SUBJECT_GROUP_VIEW);
                    }

                    cellTable.redraw();
                }
	        });
	        cellTable.addColumn(viewColumn, ConsoleUtil.policyAdminConstants.view());

         
            //checkbox column for edit
            Column<SubjectGroup, SubjectGroup> editColumn = new Column<SubjectGroup, SubjectGroup>(
                    new CustomPermissionCheckboxCell<SubjectGroup, Boolean>(UserAction.SUBJECT_GROUP_EDIT, pendingActions, permittedActions)) {
                public SubjectGroup getValue(SubjectGroup group) {
                  return group;
                }
            };
            editColumn.setFieldUpdater(new FieldUpdater<SubjectGroup, SubjectGroup>() {
                public void update(int arg0, SubjectGroup arg1,SubjectGroup arg2) {
                    if (pendingActions.keySet().contains(arg1)) {
                        pendingActions.remove(arg1);
                        updateButton(null);
                    }else {
                        // Called when the user clicks on a checkbox.
                        // Only 1 edit can be active at a time
                        pendingActions.clear();
                        pendingActions.put(arg1, UserAction.SUBJECT_GROUP_EDIT);
                        updateButton(UserAction.SUBJECT_GROUP_EDIT);
                    }
                  
                    cellTable.redraw();
                }
            });
            cellTable.addColumn(editColumn, ConsoleUtil.policyAdminConstants.edit());


            //checkbox column for delete
            Column<SubjectGroup, SubjectGroup> deleteColumn = new Column<SubjectGroup, SubjectGroup>(
                    new CustomPermissionCheckboxCell<SubjectGroup, Boolean>(UserAction.SUBJECT_GROUP_DELETE, pendingActions, permittedActions)) {
                public SubjectGroup getValue(SubjectGroup group) {
                   return group;
                }
            };
            deleteColumn.setFieldUpdater(new FieldUpdater<SubjectGroup, SubjectGroup>() {
                public void update(int index, SubjectGroup group,SubjectGroup arg2) {

                    if (pendingActions.keySet().contains(group)) {
                        pendingActions.remove(group);
                        if (pendingActions.size() == 0)
                            updateButton(null);

                    } else {
                        // Called when the user clicks on a checkbox.
                        // Many groups can be selected for delete at the same time,
                        // but any other pending ops must be cancelled
                        Iterator<Map.Entry<SubjectGroup,UserAction>> itor = pendingActions.entrySet().iterator();
                        while (itor.hasNext()) {
                            Map.Entry<SubjectGroup, UserAction> entry = itor.next();
                            if (!entry.getValue().equals(UserAction.SUBJECT_GROUP_DELETE))
                                itor.remove();
                        }

                        updateButton(UserAction.SUBJECT_GROUP_DELETE);
                        pendingActions.put(group, UserAction.SUBJECT_GROUP_DELETE);
                    }
                    cellTable.redraw();
                }
            });
            cellTable.addColumn(deleteColumn, ConsoleUtil.policyAdminConstants.delete());
            

	        //text column for name
	        TextColumn<SubjectGroup> groupNameCol = new TextColumn<SubjectGroup> () {
	            public String getValue(SubjectGroup group) {
	                if (group == null)
	                    return null;
	                return group.getName();
	            }
	        };
	        cellTable.addColumn(groupNameCol, ConsoleUtil.policyAdminConstants.subjectGroupName());
	          
	        //subject type
	        TextColumn<SubjectGroup> groupTypeCol = new TextColumn<SubjectGroup> () {
	            public String getValue(SubjectGroup group) {
	                return (group==null?null:group.getType().toString());
	            }
	        };
	        cellTable.addColumn(groupTypeCol, ConsoleUtil.policyAdminConstants.subjectType());
	        
	        //subject description
	        Column<SubjectGroup, String> descCol = new Column<SubjectGroup, String>(new DescriptionCell()) {
	            
	            public  String getValue(SubjectGroup sg) {
                    return sg.getDescription();
                }            
	        };
	        cellTable.addColumn(descCol, ConsoleUtil.policyAdminConstants.description());

	            
	        //assigned subjects
	        //TODO improve this
	        TextColumn<SubjectGroup> groupSubjectsCol = new TextColumn<SubjectGroup>() {
	            public String getValue(SubjectGroup group) {
	                if (group == null || group.getSubjects() == null)
	                    return null;

	                StringBuilder strbuilder = new StringBuilder();
	                for (String s:group.getSubjects()) {
	                    strbuilder.append(s+" ");
	                }
	                return strbuilder.toString();
	            }
	        };
	        cellTable.addColumn(groupSubjectsCol, ConsoleUtil.policyAdminConstants.subjectsAssigned());
	       
	        //policies assigned
	        //TODO improve this
            TextColumn<SubjectGroup> groupPoliciesCol = new TextColumn<SubjectGroup>() {
                public String getValue(SubjectGroup group) {
                    if (group == null || group.getPolicies() == null)
                        return null;

                    StringBuilder strbuilder = new StringBuilder();
                    for (String s:group.getPolicies()) {
                        strbuilder.append(s+" ");
                    }
                    return strbuilder.toString();
                }
            };
            cellTable.addColumn(groupPoliciesCol, ConsoleUtil.policyAdminConstants.policiesAssigned());
            
            //created by
            TextColumn<SubjectGroup> groupCreatedByCol = new TextColumn<SubjectGroup> () {
                public String getValue(SubjectGroup group) {
                    return (group==null?null:group.getCreatedBy());
                }
            };
            cellTable.addColumn(groupCreatedByCol, ConsoleUtil.policyAdminConstants.createdBy());

	         
            //Last modified by
            TextColumn<SubjectGroup> groupModifiedByCol = new TextColumn<SubjectGroup> () {
                public String getValue(SubjectGroup group) {
                    return (group==null?null:group.getLastModifiedBy());
                }
            };
            cellTable.addColumn(groupModifiedByCol, ConsoleUtil.policyAdminConstants.lastModifiedBy());
            
	       //Last modified date
            Column<SubjectGroup, Date> groupModifiedDateCol = new Column<SubjectGroup, Date>(
                    new DateCell(ConsoleUtil.tzTimeFormat)) {
                public Date getValue(SubjectGroup group) {
                    return (group==null?null:group.getLastModifiedTime());
                }
            };
            cellTable.addColumn(groupModifiedDateCol, ConsoleUtil.policyAdminConstants.lastModifiedTime());
	   
            cellTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
                public void onRangeChange(RangeChangeEvent event) {
                    //TODO ask for permissions for this user for the next lot of subjectgroups selected
                }
            });
            
          
	        summaryGrid.setWidget(1, 0, cellTable);
	        summaryGrid.setWidget(2,0,pager);
	        	   
	        mainPanel.addStyleName("sg-summary");
	        mainPanel.add(searchPanel);
	        searchPanel.addStyleName("sg-content");
	        summaryGrid.addStyleName("sg-content");
	        mainPanel.add(summaryGrid);
	    }

	    private void updateButton (UserAction action) {
	        String str = ConsoleUtil.constants.apply();
	        if (action == null) 
	            actionButton.setEnabled(false);
	        else {
	            switch (action) {
	                case SUBJECT_GROUP_VIEW: {
	                    str = ConsoleUtil.policyAdminConstants.view();
	                    actionButton.setEnabled(true);
	                    break;
	                }
	                case SUBJECT_GROUP_EDIT: {
	                    str = ConsoleUtil.policyAdminConstants.edit();
	                    actionButton.setEnabled(true);
	                    break;
	                }
	                case SUBJECT_GROUP_DELETE: {
	                    str = ConsoleUtil.policyAdminConstants.delete();
	                    actionButton.setEnabled(true);
	                    break;
	                }
	                default: {
	                    actionButton.setEnabled(false);
	                }
	            }
	        }
	        actionButton.setText(str);
	    }
	   
	    public Map<SubjectGroup, UserAction> getPendingActions () {
	        return new HashMap<SubjectGroup, UserAction>(pendingActions);
	    }
	}



	public SubjectGroupSummaryView() {
	    scrollPanel = new ScrollPanel();
		mainPanel = new FlowPanel();
		scrollPanel.add(mainPanel);
		initWidget(scrollPanel);
		
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
	

	public void activate() {
		contentView.activate();
		this.setVisible(true);
	}


	public Display getContentView() {
		return contentView;
	}
	
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay#setGroups(java.util.List)
     */
    @Override
    public void setGroups(List<SubjectGroup> groups) {
       ((ContentView)contentView).setGroups(groups);
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay#getSearchButton()
     */
    @Override
    public HasClickHandlers getSearchButton() {
        return ((ContentView)contentView).searchWidget.getSearchButton();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay#getSearchTerm()
     */
    @Override
    public String getSearchTerm() {
        return ((ContentView)contentView).searchWidget.getSearchTerm();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay#getType()
     */
    @Override
    public String getSelectedType() {
        return ((ContentView)contentView).searchWidget.getSelectedType();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay#isPolicyCriteriaEnabled()
     */
    @Override
    public boolean isPolicyCriteriaEnabled() {
        return ((ContentView)contentView).searchWidget.isPolicyCriteriaEnabled();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay#isSearchCriteriaEnabled()
     */
    @Override
    public boolean isSubjectCriteriaEnabled() {
       return ((ContentView)contentView).searchWidget.isSubjectCriteriaEnabled();
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay#setAvailableTypes(java.util.List)
     */
    @Override
    public void setAvailableTypes(List<String> types) {
        ((ContentView)contentView).searchWidget.setAvailableTypes(types);
    }
    
    public HasClickHandlers getSubjectCriteriaButton() {
        return ((ContentView)contentView).searchWidget.getSubjectCriteriaButton();
    }
    
    public HasClickHandlers getPolicyCriteriaButton() {
        return ((ContentView)contentView).searchWidget.getPolicyCriteriaButton();
    }
    
    public void setPermittedActions (SubjectGroup group, List<UserAction> permittedActions) {
        ((ContentView)contentView).setUserActions(group, permittedActions);
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay#getActionButton()
     */
    @Override
    public HasClickHandlers getActionButton() {
        return ((ContentView)contentView).getActionButton();
    }

    
 
    public Map<SubjectGroup, UserAction> getPendingActions() {
        return ((ContentView)contentView).getPendingActions();
    }
  
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay#setSelectedSearchTerm(java.lang.String)
     */
    @Override
    public void setSelectedSearchTerm(String name) {
        ((ContentView)contentView).searchPanel.setOpen(true);
        ((ContentView)contentView).searchWidget.setSelectedSearchTerm(name);
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay#setSelectedType(java.lang.String)
     */
    @Override
    public void setSelectedType(String type) {
        ((ContentView)contentView).searchPanel.setOpen(true);
        ((ContentView)contentView).searchWidget.setSelectedType(type);
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay#setPolicyCriteriaEnabled(boolean)
     */
    @Override
    public void setPolicyCriteriaEnabled(boolean enabled) {
        ((ContentView)contentView).searchPanel.setOpen(true);
        ((ContentView)contentView).searchWidget.setPolicyCriteriaEnabled(enabled);
        
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.SubjectGroupSummaryPresenter.SubjectGroupSummaryDisplay#setSearchCriteriaEnabled(boolean)
     */
    @Override
    public void setSearchCriteriaEnabled(boolean enabled) {
        ((ContentView)contentView).searchPanel.setOpen(true);
        ((ContentView)contentView).searchWidget.setSubjectCriteriaEnabled(enabled);
    }
    
    public void error(String msg) {
		ErrorDialog dialog = new ErrorDialog(true);
		dialog.setMessage(msg);
		dialog.getDialog().center();
		dialog.show();
	}

   
   
    
}
