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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.model.policy.ExtraField;
import org.ebayopensource.turmeric.monitoring.client.model.policy.Operation;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicySubjectAssignment;
import org.ebayopensource.turmeric.monitoring.client.model.policy.Resource;
import org.ebayopensource.turmeric.monitoring.client.model.policy.Subject;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyCreatePresenter.PolicyCreateDisplay;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyCreatePresenter.ResourcesContentDisplay;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyCreatePresenter.SubjectContentDisplay;
import org.ebayopensource.turmeric.monitoring.client.view.ErrorDialog;
import org.ebayopensource.turmeric.monitoring.client.view.common.AbstractGenericView;
import org.ebayopensource.turmeric.monitoring.client.view.common.SelectBoxesWidget;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;

public abstract class PolicyCreateView extends ResizeComposite implements
		PolicyCreateDisplay {

	private DockLayoutPanel mainPanel;

	protected Display contentView;
	private ResourcesContentDisplay resourceContentView;
	private SubjectContentDisplay subjectContentView;

	protected TextBox policyName;
	protected TextBox policyDesc;
	protected Label policyType;
	protected Label policyStatus;
	protected boolean policyEnabled;

	private Button saveButton;
	private Button cancelButton;

	protected Grid extraFieldsGrid = new Grid(1, 1);

	protected abstract String getTitleForm();

	/**
	 * PolicyConditionWidget
	 * 
	 */
	private class PolicyConditionWidget extends Composite {
		private FlowPanel mainPanel;
		private Button addConditionButton;
		
		TextBox quantityBox;
		
		private Label rsLabel;
		private Label opLabel;
		
		private ListBox rsListBox;
		private ListBox opListBox;
		private ListBox conditionListBox;
		private ListBox aritmSignListBox;
		private ListBox logicOpListBox;
		
	
		public PolicyConditionWidget() {
			mainPanel = new FlowPanel();

			final Grid grid = new Grid(3, 7);

			rsLabel = new Label(
					ConsoleUtil.policyAdminConstants.service());
			opLabel = new Label(
					ConsoleUtil.policyAdminConstants.operationName());
			
			rsListBox = new ListBox(false);
			opListBox= new ListBox(false);
			conditionListBox=new ListBox(false);
			aritmSignListBox= new ListBox(false);
			setAritmSignListBoxValues();
			logicOpListBox= new ListBox(false);
			setLogicOpListBoxValues();
			
			quantityBox = new TextBox();
			
			addConditionButton = new Button(ConsoleUtil.policyAdminConstants.add());
		
			grid.setWidget(0, 0, rsLabel);
			grid.setWidget(0, 2, opLabel);
			
			grid.setWidget(1, 0, rsListBox);
			grid.setWidget(1, 1, new Label(":"));
			grid.setWidget(1, 2, opListBox);
			grid.setWidget(1, 3, conditionListBox);
			grid.setWidget(1, 4, aritmSignListBox);
			grid.setWidget(1, 5, quantityBox);
			grid.setWidget(1, 6, logicOpListBox);
			
			grid.setWidget(2, 4, addConditionButton);

		
			mainPanel.add(grid);
			initWidget(mainPanel);
		}

		private void setAritmSignListBoxValues(){
			aritmSignListBox.addItem("<");
			aritmSignListBox.addItem(">");
			aritmSignListBox.addItem("<=");
			aritmSignListBox.addItem(">=");
			aritmSignListBox.addItem("=");
		}
		
		private void setLogicOpListBoxValues(){
			logicOpListBox.addItem("");
			logicOpListBox.addItem(" AND ");
			logicOpListBox.addItem(" OR ");
		}
		
		
		public void clearConditionBuilder() {
			// select nothing
			rsListBox.setSelectedIndex(-1);
			opListBox.setSelectedIndex(-1);
			conditionListBox.setSelectedIndex(-1);
			aritmSignListBox.setSelectedIndex(-1);
			logicOpListBox.setSelectedIndex(-1);
			quantityBox.setText("");
		}

		public boolean validAllFields(){
			boolean valid = true;

			if(this.rsListBox.getSelectedIndex() == -1){
				return false;
			}
			if(this.opListBox.getSelectedIndex() == -1){
				return false;
			}
			if(this.aritmSignListBox.getSelectedIndex() == -1){
				return false;
			}
			if("".equals(this.quantityBox.getText())){
				return false;
			}
			
			return valid;
		}
		
		public HasClickHandlers getAddConditionButton() {
			return addConditionButton;
		}

		
		public HasChangeHandlers getRsListBox() {
			return rsListBox;
		}

		
		public String getQuantityBox() {
			return quantityBox.getValue();
		}

		public String getRsNameSelected() {
			if (rsListBox.getSelectedIndex() >= 0)
				return rsListBox.getItemText(rsListBox.getSelectedIndex());
			else
				return null;
		}

		public String getOpNameSelected() {
			if (opListBox.getSelectedIndex() >= 0)
				return opListBox.getItemText(opListBox.getSelectedIndex());
			else
				return null;
		}
		
		public String getConditionSelected() {
			if (conditionListBox.getSelectedIndex() >= 0)
				return conditionListBox.getItemText(conditionListBox.getSelectedIndex());
			else
				return null;
		}
		
		public String getAritmSignSelected() {
			if (aritmSignListBox.getSelectedIndex() >= 0)
				return aritmSignListBox.getItemText(aritmSignListBox.getSelectedIndex());
			else
				return null;
		}
		
		public String getLogicOpSelected() {
			if (logicOpListBox.getSelectedIndex() >= 0)
				return logicOpListBox.getItemText(logicOpListBox.getSelectedIndex());
			else
				return null;
		}
		
		public void setRsNames(List<String>  names) {
			rsListBox.clear();
			for (String s : names){
				rsListBox.addItem(s);
			}
		}
		
		
		
		public void initializeRsListBox(){
			rsListBox.setSelectedIndex(-1);
		}
		
		public void setOpNames(List<String>  names) {
			opListBox.clear();
			for (String s : names){
				opListBox.addItem(s);
			}
		}
	
		public void setConditionNames(List<String>  conditions) {
			conditionListBox.clear();
			for (String s : conditions){
				conditionListBox.addItem(s);
			}
		}

	}
	
	public PolicyCreateView() {
		mainPanel = new DockLayoutPanel(Unit.EM);
		initWidget(mainPanel);
		mainPanel.setWidth("100%");
		initialize();

	}

	public abstract UserAction getSelectedAction();
	
	public void initialize() {

		policyName = new TextBox();
		policyDesc = new TextBox();
		policyType = new Label();
		policyStatus = new Label();

		initializeExtraFields();

		// CONTENT
		StackLayoutPanel policyContentPanel = new StackLayoutPanel(Unit.EM);
		policyContentPanel.add(initContentView(), getTitleForm(), 2);
		policyContentPanel.add(initResourceContentView(),
				ConsoleUtil.policyAdminConstants.resources(), 2);
		policyContentPanel.add(initSubjectContentView(),
				ConsoleUtil.policyAdminConstants.subjectsAndSubjectGroups(), 2);
		policyContentPanel.setHeight("90%");

		saveButton = new Button(ConsoleUtil.constants.save());
		cancelButton = new Button(ConsoleUtil.constants.cancel());
		HorizontalPanel buttonsPannel = new HorizontalPanel();
		buttonsPannel.add(saveButton);
		buttonsPannel.add(cancelButton);

		mainPanel.addSouth(buttonsPannel, 2);
		mainPanel.add(policyContentPanel);
	}

	protected abstract void initializeExtraFields();

	protected Widget initContentView() {
		ScrollPanel actionPanel = new ScrollPanel();
		contentView = new ContentView();
		actionPanel.add(contentView.asWidget());
		return actionPanel;
	}

	protected Widget initResourceContentView() {
		ScrollPanel actionPanel = new ScrollPanel();
		resourceContentView = new ResourceContentView();
		actionPanel.add(resourceContentView.asWidget());

		return actionPanel;
	}

	protected Widget initSubjectContentView() {
		ScrollPanel actionPanel = new ScrollPanel();
		subjectContentView = new SubjectContentView();
		actionPanel.add(subjectContentView.asWidget());

		return actionPanel;
	}

	protected class ContentView extends AbstractGenericView implements Display {
		private VerticalPanel mainPanel;
		private Grid policyInfoGrid;
		private PolicyConditionWidget conditionWidget;
		protected DisclosurePanel conditionPanel;

		
		public ContentView() {
			mainPanel = new VerticalPanel();
			initWidget(mainPanel);
			initialize();
		}

		public void activate() {
			// do nothing for now
		}

		@Override
		public void initialize() {
			mainPanel.clear();

			policyName.setWidth("300px");
			policyDesc.setWidth("250px");
			policyStatus.setWidth("300px");
			policyType.setWidth("300px");
			
			policyInfoGrid = new Grid(4, 2);

			policyInfoGrid.setWidget(0, 0, new Label(
					ConsoleUtil.policyAdminConstants.policyName() + ":"));
			policyInfoGrid.setWidget(0, 1, policyName);
			policyInfoGrid
					.setWidget(
							1,
							0,
							new Label(ConsoleUtil.policyAdminConstants
									.policyDescription() + ":"));
			policyInfoGrid.setWidget(1, 1, policyDesc);

			policyInfoGrid.setWidget(2, 0, new Label(
					ConsoleUtil.policyAdminConstants.policyType() + ":"));
			policyInfoGrid.setWidget(2, 1, policyType);
			
			policyInfoGrid.setWidget(3, 0, new Label(
					ConsoleUtil.policyAdminConstants.status() + ":"));
			policyInfoGrid.setWidget(3, 1, policyStatus);
			
			mainPanel.add(policyInfoGrid);
			mainPanel.add(extraFieldsGrid);
			
			
			conditionWidget = new PolicyConditionWidget();
			conditionPanel = new DisclosurePanel(
					ConsoleUtil.policyAdminConstants.conditionBuilder());
			conditionPanel.setContent(conditionWidget);
			conditionPanel.setStyleName("policy-content");
			
			mainPanel.add(conditionPanel);

		}
	}

	private class ResourceContentView extends AbstractGenericView implements
			ResourcesContentDisplay {
		private FlowPanel mainPanel;

		private CellTable<Resource> cellTable;
		MultiSelectionModel<Resource> selectionModel;
		ProvidesKey<Resource> keyProvider;
		private PolicyResourceAssignmentWidget resourceAssignmentWidget;
		private DisclosurePanel resourceAssignmentPanel;
		private Button addResourceButton;
		private Button cancelResourceButton;
		private FlowPanel resourceAssignmentButtonPanel;
		private PushButton delButton;
		private PushButton editButton;

		private List<Resource> selections = new ArrayList<Resource>();
		private ListDataProvider<Resource> dataProvider;
		private List<UserAction> permissions;

		public ResourceContentView() {
			mainPanel = new FlowPanel();
			initWidget(mainPanel);
			initialize();
		}

		public void setUserActions(List<UserAction> actions) {
			permissions = actions;

			enableActions();
		}

		public void activate() {
			// do nothing for now
		}

		public void clearAssignmentWidget() {
			resourceAssignmentWidget.clear();
		}

		public void show() {
			resourceAssignmentPanel.setOpen(true);
		}

		public void hide() {
			resourceAssignmentPanel.setOpen(false);
		}

		@Override
		public void initialize() {
			mainPanel.clear();
			createResourceAssignmentFields();
			mainPanel.add(resourceAssignmentPanel);

			// bottom part of panel is a table with search results
			Grid summaryGrid = new Grid(3, 1);
			summaryGrid.setStyleName("sggrid");

			Grid actionGrid = new Grid(1, 2);

			editButton = new PushButton("[ "
					+ ConsoleUtil.policyAdminConstants.edit() + " ]");
			editButton.setEnabled(false);

			delButton = new PushButton("[ "
					+ ConsoleUtil.policyAdminConstants.delete() + " ]");
			delButton.setEnabled(false);

			actionGrid.setWidget(0, 0, editButton);
			actionGrid.setWidget(0, 1, delButton);

			summaryGrid.setWidget(0, 0, actionGrid);

			keyProvider = new ProvidesKey<Resource>() {
				public Object getKey(Resource assignment) {
					return assignment == null ? null : assignment
							.getResourceType() + assignment.getResourceName();
				}
			};

			cellTable = new CellTable<Resource>(keyProvider);
			selectionModel = new MultiSelectionModel<Resource>(keyProvider);
			cellTable.setSelectionModel(selectionModel);
			selectionModel
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						public void onSelectionChange(SelectionChangeEvent event) {
							Set<Resource> x = selectionModel.getSelectedSet();
							selections.clear();
							if (x != null)
								selections.addAll(x);
							enableActions();
						}
					});
			dataProvider = new ListDataProvider<Resource>();
			dataProvider.addDataDisplay(cellTable);
			SimplePager pager = new SimplePager();
			pager.setDisplay(cellTable);

			// checkbox column for selection
			Column<Resource, Boolean> checkColumn = new Column<Resource, Boolean>(
					new CheckboxCell(true)) {
				public Boolean getValue(Resource rs) {
					// Get the value from the selection model.
					return selectionModel.isSelected(rs);
				}
			};
			checkColumn.setFieldUpdater(new FieldUpdater<Resource, Boolean>() {
				public void update(int index, Resource rs, Boolean value) {
					// Called when the user clicks on a checkbox.
					selectionModel.setSelected(rs, value);
				}
			});
			cellTable.addColumn(checkColumn, "");

			// resource type
			TextColumn<Resource> resourceTypeCol = new TextColumn<Resource>() {
				public String getValue(Resource assignment) {
					if (assignment == null
							|| assignment.getResourceType() == null) {
						return null;
					}
					return assignment.getResourceType();
				}
			};
			cellTable.addColumn(resourceTypeCol,
					ConsoleUtil.policyAdminConstants.resourceType());

			// resource Name
			TextColumn<Resource> resourceNameCol = new TextColumn<Resource>() {
				public String getValue(Resource assignment) {
					if (assignment == null
							|| assignment.getResourceName() == null) {
						return null;
					}
					return assignment.getResourceName();
				}
			};
			cellTable.addColumn(resourceNameCol,
					ConsoleUtil.policyAdminConstants.resourceName());

			// operations
			// TODO add operations name into table
			Column<Resource, List<String>> resourceOpsCol = new Column<Resource, List<String>>(
					new CustomListCell(MIN_SCROLLBAR_SIZE)) {
				public List<String> getValue(Resource resource) {

					if (resource == null || resource.getOpList() == null) {
						return null;
					}
					ArrayList<String> opsNamesList = new ArrayList<String>();
					for (Operation op : resource.getOpList()) {
						opsNamesList.add(op.getOperationName());
					}
					return opsNamesList;
				}
			};

			cellTable.addColumn(resourceOpsCol,
					ConsoleUtil.policyAdminConstants.operations());

			summaryGrid.setWidget(1, 0, cellTable);
			summaryGrid.setWidget(2, 0, pager);

			mainPanel.addStyleName("resource-summary");
			// mainPanel.add(searchPanel);
			// searchPanel.addStyleName("policy-content");
			summaryGrid.addStyleName("resource-content");
			mainPanel.add(summaryGrid);
		}

		protected void createResourceAssignmentFields() {

			resourceAssignmentWidget = new PolicyResourceAssignmentWidget();
			resourceAssignmentPanel = new DisclosurePanel(
					ConsoleUtil.policyAdminConstants.assignResources());
			resourceAssignmentPanel
					.addCloseHandler(new CloseHandler<DisclosurePanel>() {
						public void onClose(CloseEvent<DisclosurePanel> arg0) {
							resourceAssignmentWidget.clear();
						}
					});

			addResourceButton = new Button(
					ConsoleUtil.policyAdminConstants.save());
			cancelResourceButton = new Button(
					ConsoleUtil.policyAdminConstants.cancel());

			resourceAssignmentButtonPanel = new FlowPanel();
			resourceAssignmentButtonPanel.add(addResourceButton);
			resourceAssignmentButtonPanel.add(cancelResourceButton);

			Grid resourceAssignmentGrid = new Grid(2, 1);
			resourceAssignmentGrid.setWidget(0, 0, resourceAssignmentWidget);
			resourceAssignmentGrid.setWidget(1, 0,
					resourceAssignmentButtonPanel);
			resourceAssignmentPanel.setContent(resourceAssignmentGrid);
		}

		public List<Resource> getSelections() {
			return selections;
		}

		private void enableActions() {
			boolean permitted = permissions != null
					&& permissions.contains(UserAction.POLICY_EDIT);
			delButton.setEnabled(permitted && selections.size() >= 1);
			editButton.setEnabled(permitted && selections.size() == 1);
		}

		@Override
		public void setAvailableOperations(List<String> operations) {
			resourceAssignmentWidget.setAvailableOperations(operations);
		}

		@Override
		public void setSelectedOperations(List<String> operations) {
			resourceAssignmentWidget.setSelectedOperations(operations);
		}

		@Override
		public void setResourceTypes(List<String> resourceTypes) {
			resourceAssignmentWidget.setResourceTypes(resourceTypes);
		}

		@Override
		public void setResourceNames(List<String> resourceNames) {
			resourceAssignmentWidget.setResourceNames(resourceNames);

		}

		@Override
		public void setResourceLevel(List<String> resourceLevels) {
			resourceAssignmentWidget.setResourceLevels(resourceLevels);
		}

		@Override
		public HasClickHandlers getAddResourceButton() {
			return addResourceButton;
		}

		@Override
		public HasClickHandlers getCancelResourceButton() {
			return cancelResourceButton;
		}

		@Override
		public String getResourceName() {
			return resourceAssignmentWidget.getResourceName();
		}

		@Override
		public String getResourceLevel() {
			return resourceAssignmentWidget.getResourceLevel();
		}

		public HasClickHandlers getDelButton() {
			return delButton;
		}

		
		@Override
		public ListBox getResourceTypeBox() {
			return resourceAssignmentWidget.getResourceTypeBox();
		}

		@Override
		public ListBox getResourceLevelBox() {
			return resourceAssignmentWidget.getResourceLevelBox();
		}

		@Override
		public String getResourceType() {
			return resourceAssignmentWidget.getResourceType();
		}

		@Override
		public void error(String msg) {
			ErrorDialog dialog = new ErrorDialog(true);
			dialog.setMessage(msg);
			dialog.getDialog().center();
			dialog.show();

		}

		@Override
		public ListBox getResourceNameBox() {
			return resourceAssignmentWidget.getResourceNameBox();
		}

		@Override
		public List<String> getSelectedOperations() {
			return resourceAssignmentWidget.getSelectedOperations();
		}

		@Override
		public Label getResourceTypeLabel() {
			return resourceAssignmentWidget.getResourceTypeLabel();
		}

		@Override
		public Label getResourceLevelLabel() {
			return resourceAssignmentWidget.getResourceLevelLabel();
		}
		
		@Override
		public Label getResourceNameLabel() {
			return resourceAssignmentWidget.getResourceNameLabel();
		}

		@Override
		public SelectBoxesWidget getSelectBoxesWidget() {
			return resourceAssignmentWidget.getSelectBoxesWidget();
		}

		public void setAssignments(List<Resource> assignments) {
			selections.clear();
			List<Resource> data = dataProvider.getList();
			data.clear();
			if (assignments != null)
				data.addAll(assignments);
			cellTable.redraw();
		}

		@Override
		public HasClickHandlers getEditButton() {
			return editButton;
		}

		@Override
		public List<String> getAvailableOperations() {
			return resourceAssignmentWidget.getAvailableOperations();
		}

	}

	private class SubjectContentView extends AbstractGenericView implements
			SubjectContentDisplay {
		private SimplePanel mainPanel;
		private Grid mainGrid;
		private PolicySubjectAssignmentWidget subjectAssignmentWidget;
		private DisclosurePanel subjectAssignmentPanel;
		private FlowPanel subjectAssignmentButtonPanel;
		private Button addSubjectButton;
		private Button cancelSubjectButton;
		private CellTable<PolicySubjectAssignment> cellTable;
		private ProvidesKey<PolicySubjectAssignment> keyProvider;
		private MultiSelectionModel<PolicySubjectAssignment> selectionModel;
		private List<PolicySubjectAssignment> selections = new ArrayList<PolicySubjectAssignment>();
		private ListDataProvider<PolicySubjectAssignment> dataProvider;
		private Grid subjectGrid;
		private Grid actionGrid;
		private SimplePager pager;
		private PushButton editButton;
		private PushButton delButton;
		private List<UserAction> permissions;

		public SubjectContentView() {
			mainPanel = new SimplePanel();
			mainGrid = new Grid(2, 1);
			mainPanel.add(mainGrid);
			initWidget(mainPanel);

			initialize();
		}

		public void activate() {
			// do nothing for now
		}

		@Override
		public void initialize() {
			subjectGrid = new Grid(3, 1);
			createButtonMenu();
			subjectGrid.setWidget(0, 0, actionGrid);
			createSubjectTableFields();
			subjectGrid.setWidget(1, 0, cellTable);
			subjectGrid.setWidget(2, 0, pager);

			createSubjectAssignmentFields();
			mainGrid.setWidget(0, 0, subjectAssignmentPanel);
			mainGrid.setWidget(1, 0, subjectGrid);
			mainGrid.setWidth("60%");
		}

		protected void createButtonMenu() {
			// Edit (subject assignment) button - depends on POLICY_EDIT
			// permission
			// Delete (subject assignment) button - depends on POLICY_EDIT
			// permission
			// DeleteAll (subject assignment) button - depends on POLICY_EDIT
			// permission
			actionGrid = new Grid(1, 2);
			editButton = new PushButton("[ "
					+ ConsoleUtil.policyAdminConstants.edit() + " ]");
			editButton.setEnabled(false);
			delButton = new PushButton("[ "
					+ ConsoleUtil.policyAdminConstants.delete() + " ]");
			delButton.setEnabled(false);

			actionGrid.setWidget(0, 0, editButton);
			actionGrid.setWidget(0, 1, delButton);
		}

		protected void createSubjectTableFields() {

			// SubjectAssignment
			// - SubjectType
			// - List of Subject
			// - List of SubjectGroup

			keyProvider = new ProvidesKey<PolicySubjectAssignment>() {
				public Object getKey(PolicySubjectAssignment assignment) {
					return assignment == null ? null : assignment
							.getSubjectType();
				}
			};

			cellTable = new CellTable<PolicySubjectAssignment>(keyProvider);
			selectionModel = new MultiSelectionModel<PolicySubjectAssignment>(
					keyProvider);
			cellTable.setSelectionModel(selectionModel);
			selectionModel
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						public void onSelectionChange(SelectionChangeEvent event) {
							Set<PolicySubjectAssignment> x = selectionModel
									.getSelectedSet();
							selections.clear();
							if (x != null)
								selections.addAll(x);
							enableActions();
						}
					});
			dataProvider = new ListDataProvider<PolicySubjectAssignment>();
			dataProvider.addDataDisplay(cellTable);

			pager = new SimplePager();
			pager.setDisplay(cellTable);

			// checkbox column for selection
			Column<PolicySubjectAssignment, Boolean> checkColumn = new Column<PolicySubjectAssignment, Boolean>(
					new CheckboxCell(true)) {
				public Boolean getValue(PolicySubjectAssignment assignment) {
					// Get the value from the selection model.
					return selectionModel.isSelected(assignment);
				}
			};

			checkColumn
					.setFieldUpdater(new FieldUpdater<PolicySubjectAssignment, Boolean>() {
						public void update(int index,
								PolicySubjectAssignment assignment,
								Boolean value) {
							// Called when the user clicks on a checkbox.
							selectionModel.setSelected(assignment, value);
						}
					});
			cellTable.addColumn(checkColumn, "");

			// text column for type
			TextColumn<PolicySubjectAssignment> typeCol = new TextColumn<PolicySubjectAssignment>() {
				public String getValue(PolicySubjectAssignment assignment) {
					if (assignment == null || assignment.getSubjects() == null)
						return null;
					return assignment.getSubjectType();
				}
			};
			cellTable.addColumn(typeCol,
					ConsoleUtil.policyAdminConstants.subjectType());

			// text column for Subject names
			Column<PolicySubjectAssignment, List<String>> subjectNamesCol = new Column<PolicySubjectAssignment, List<String>>(
					new CustomListCell(MIN_SCROLLBAR_SIZE)) {
				public List<String> getValue(PolicySubjectAssignment assignment) {

					if (assignment == null || assignment.getSubjects() == null) {
						return null;
					}
					ArrayList<String> namesList = new ArrayList<String>();
					for (Subject subject : assignment.getSubjects()) {
						namesList.add(subject.getName());
					}

					return namesList;
				}
			};
			
			cellTable.addColumn(subjectNamesCol,
					ConsoleUtil.policyAdminConstants.subjects());
			
			// text column for Exclusion Subject names
			Column<PolicySubjectAssignment, List<String>> exclusionSubjectNamesCol = new Column<PolicySubjectAssignment, List<String>>(
					new CustomListCell(MIN_SCROLLBAR_SIZE)) {
				public List<String> getValue(PolicySubjectAssignment assignment) {

					if (assignment == null || assignment.getExclusionSubjects() == null) {
						return null;
					}
					ArrayList<String> namesList = new ArrayList<String>();
					for (Subject subject : assignment.getExclusionSubjects()) {
						namesList.add(subject.getName());
					}

					return namesList;
				}
			};
			
			cellTable.addColumn(exclusionSubjectNamesCol,
					ConsoleUtil.policyAdminConstants.exclusionSubjects());

			// text column for SubjectGroup names
			Column<PolicySubjectAssignment, List<String>> subjectGroupNamesCol = new Column<PolicySubjectAssignment, List<String>>(
					new CustomListCell(MIN_SCROLLBAR_SIZE)) {
				public List<String> getValue(PolicySubjectAssignment assignment) {

					if (assignment == null || assignment.getSubjectGroups()  == null) {
						return null;
					}
					ArrayList<String> namesList = new ArrayList<String>();
					for (SubjectGroup subjectGroup : assignment.getSubjectGroups()) {
						namesList.add(subjectGroup.getName());
					}

					return namesList;
				}
			};
			
			cellTable.addColumn(subjectGroupNamesCol,
					ConsoleUtil.policyAdminConstants.subjectGroups());

		
			// text column for Exclusion Subject Group names
			Column<PolicySubjectAssignment, List<String>> exclusionSGNamesCol = new Column<PolicySubjectAssignment, List<String>>(
					new CustomListCell(MIN_SCROLLBAR_SIZE)) {
				public List<String> getValue(PolicySubjectAssignment assignment) {

					if (assignment == null || assignment.getExclusionSubjectGroups() == null) {
						return null;
					}
					ArrayList<String> namesList = new ArrayList<String>();
					for (SubjectGroup subjectGroup : assignment.getExclusionSubjectGroups()) {
						namesList.add(subjectGroup.getName());
					}

					return namesList;
				}
			};
			cellTable.addColumn(exclusionSGNamesCol,
					ConsoleUtil.policyAdminConstants.exclusionSubjectGroups());


		}

		protected void createSubjectAssignmentFields() {

			subjectAssignmentWidget = new PolicySubjectAssignmentWidget();

			subjectAssignmentPanel = new DisclosurePanel(
					ConsoleUtil.policyAdminConstants
							.assignSubjectsAndSubjectGroups());
			subjectAssignmentPanel
					.addCloseHandler(new CloseHandler<DisclosurePanel>() {
						public void onClose(CloseEvent<DisclosurePanel> arg0) {
							subjectAssignmentWidget.clear();
						}
					});

			addSubjectButton = new Button(ConsoleUtil.constants.apply());
			cancelSubjectButton = new Button(ConsoleUtil.constants.cancel());

			subjectAssignmentButtonPanel = new FlowPanel();
			subjectAssignmentButtonPanel.add(addSubjectButton);
			subjectAssignmentButtonPanel.add(cancelSubjectButton);

			Grid subjectAssignmentGrid = new Grid(2, 1);
			subjectAssignmentGrid.setWidget(0, 0, subjectAssignmentWidget);
			subjectAssignmentGrid.setWidget(1, 0, subjectAssignmentButtonPanel);
			subjectAssignmentPanel.setContent(subjectAssignmentGrid);

		}

		public void show() {
			subjectAssignmentPanel.setOpen(true);
		}

		public void hide() {
			subjectAssignmentPanel.setOpen(false);
		}

		public List<PolicySubjectAssignment> getSelectedSubjectAssignments() {
			return selections;
		}

		public HasClickHandlers getEditButton() {
			return editButton;
		}

		public HasClickHandlers getDelButton() {
			return delButton;
		}

		public HasCloseHandlers getCloseHandler() {
			return subjectAssignmentPanel;
		}

		public HasOpenHandlers getOpenHandler() {
			return subjectAssignmentPanel;
		}

		public void setUserActions(List<UserAction> actions) {
			permissions = actions;
			enableActions();
		}

	    public void setExclusionListVisible(boolean visible){
	    	subjectAssignmentWidget.setExclusionListVisible(visible);
	    }
	    
		public void setAssignments(List<PolicySubjectAssignment> assignments) {
			HashSet<PolicySubjectAssignment> tmp = new HashSet(
					selectionModel.getSelectedSet());
			for (PolicySubjectAssignment a : tmp) {
				selectionModel.setSelected(a, false);
			}
			selections.clear();

			List<PolicySubjectAssignment> list = (assignments == null ? new ArrayList<PolicySubjectAssignment>()
					: new ArrayList<PolicySubjectAssignment>(assignments));
			dataProvider.setList(list);
			dataProvider.refresh();

			cellTable.setRowCount(list.size());
			cellTable.redraw();
		}

		public List<PolicySubjectAssignment> getAssignments() {
			return dataProvider.getList();
		}

		public HasClickHandlers getAddButton() {
			return addSubjectButton;
		}

		public HasClickHandlers getCancelButton() {
			return cancelSubjectButton;
		}

		public void setAvailableSubjectTypes(List<String> availableSubjectTypes) {
			subjectAssignmentWidget
					.setAvailableSubjectTypes(availableSubjectTypes);
		}

		public void setAvailableSubjectGroups(List<String> list) {
			subjectAssignmentWidget.setAvailableGroups(list);
		}

		public HasClickHandlers getGroupSearchButton() {
			return subjectAssignmentWidget.getGroupSearchButton();
		}

		public String getGroupSearchTerm() {
			return subjectAssignmentWidget.getGroupSearchTerm();
		}

		public String getSubjectSearchTerm() {
			return subjectAssignmentWidget.getSearchTerm();
		}

		public String getSubjectType() {
			return subjectAssignmentWidget.getSubjectType();
		}

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyCreatePresenter.SubjectContentDisplay#getSelectedSubjectGroups()
		 */
		@Override
		public List<String> getSelectedSubjectGroups() {
			return subjectAssignmentWidget.getSelectedGroups();
		}

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyCreatePresenter.SubjectContentDisplay#getSelectedSubjects()
		 */
		@Override
		public List<String> getSelectedSubjects() {
			return subjectAssignmentWidget.getSelectedSubjects();
		}

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyCreatePresenter.SubjectContentDisplay#getSubjectSearchButton()
		 */
		@Override
		public HasClickHandlers getSubjectSearchButton() {
			return subjectAssignmentWidget.getSearchButton();
		}

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyCreatePresenter.SubjectContentDisplay#setAvailableSubjects(java.util.List)
		 */
		@Override
		public void setAvailableSubjects(List<String> list) {
			subjectAssignmentWidget.setAvailableSubjects(list);
		}

		@Override
		public void clearAssignmentWidget() {
			subjectAssignmentWidget.clear();
		}

		private void enableActions() {
			boolean permitted = permissions != null
					&& permissions.contains(UserAction.POLICY_EDIT);
			delButton.setEnabled(permitted && selections.size() == 1);
			editButton.setEnabled(permitted && selections.size() == 1);
		}

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyCreatePresenter.SubjectContentDisplay#setSelectedSubjectGroups(java.util.List)
		 */
		@Override
		public void setSelectedSubjectGroups(List<String> list) {
			subjectAssignmentWidget.setSelectedGroups(list);
		}

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyCreatePresenter.SubjectContentDisplay#setSelectedSubjects(java.util.List)
		 */
		@Override
		public void setSelectedSubjects(List<String> list) {
			subjectAssignmentWidget.setSelectedSubjects(list);
		}

		@Override
		public List<String> getSelectedExclusionSubjects() {
			return subjectAssignmentWidget.getSelectedExclusionSubjects();
		}

		@Override
		public List<String> getSelectedExclusionSG() {
			return subjectAssignmentWidget.getSelectedExclusionSG();
		}
		
		@Override
		public void setAvailableExclusionSubjects(List<String> list) {
			subjectAssignmentWidget.setAvailableExclusionSubjects(list);
		}

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyCreatePresenter.SubjectContentDisplay#setSelectedExclusionSubjects(java.util.List)
		 */
		@Override
		public void setSelectedExclusionSubjects(final List<String> list) {
			subjectAssignmentWidget.setSelectedExclusionSubjects(list);
		}

		@Override
		public void setAvailableExclusionSG(List<String> list) {
			subjectAssignmentWidget.setAvailableExclusionSG(list);
		}

		@Override
		public void setSelectedExclusionSG(final List<String> list) {
			subjectAssignmentWidget.setSelectedExclusionSG(list);
		}


	}

	public void activate() {
		contentView.activate();
		this.setVisible(true);
	}

	public TextBox getPolicyName() {
		return policyName;
	}

	public boolean getPolicyEnabled() {
		return policyEnabled;
	}

	public TextBox getPolicyDesc() {
		return policyDesc;
	}

	public Display getContentView() {
		return contentView;
	}

	public ResourcesContentDisplay getResourceContentView() {
		return resourceContentView;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	@Override
	public SubjectContentDisplay getSubjectContentView() {
		return subjectContentView;
	}

	@Override
	public void setUserActions(List<UserAction> permittedActions) {
		resourceContentView.setUserActions(permittedActions);
		subjectContentView.setUserActions(permittedActions);
	}

	public void clear() {
		policyName.setText("");
		policyDesc.setText("");
		policyType.setText("");
		policyStatus.setText("");
		this.resourceContentView.clearAssignmentWidget();
		this.subjectContentView.clearAssignmentWidget();
		this.resourceContentView.setAssignments(Collections.EMPTY_LIST);
		this.subjectContentView.setAssignments(Collections.EMPTY_LIST);
		((ContentView) contentView).conditionWidget.clearConditionBuilder();
		
	}

	public void error(String msg) {
		ErrorDialog dialog = new ErrorDialog(true);
		dialog.setMessage(msg);
		dialog.getDialog().center();
		dialog.show();
	}

	@Override
	public String getExtraFieldValue(int order) {
		String value = null;
		if (extraFieldsGrid.getWidget(order, 1) instanceof TextBox) {
			value = ((TextBox) extraFieldsGrid.getWidget(order, 1)).getText();

		} else if (extraFieldsGrid.getWidget(order, 1) instanceof TextArea) {
			value = ((TextArea) extraFieldsGrid.getWidget(order, 1)).getText();

		} else if (extraFieldsGrid.getWidget(order, 1) instanceof CheckBox) {
			Boolean booleanValue = ((CheckBox) extraFieldsGrid.getWidget(order,
					1)).getValue();
			value = (String.valueOf(booleanValue));

		} else if (extraFieldsGrid.getWidget(order, 1) instanceof ListBox) {
			ListBox list = (ListBox) extraFieldsGrid.getWidget(order, 1);
			int index = list.getSelectedIndex();
			if (index >= 0) {
				value = list.getItemText(index);
			}
		}
		return value;
	}
	
	@Override
	public void setExtraFieldValue(int order, String value, boolean append) {
		// textArea widet type
		if (extraFieldsGrid.getWidget(order, 1) instanceof TextArea) {
			TextArea text = (TextArea) extraFieldsGrid.getWidget(order, 1);
			if(append){
				value = text.getText() + value ;
			}
			text.setText(value);
			
		}else if (extraFieldsGrid.getWidget(order, 1) instanceof TextBox) {
			TextBox text = (TextBox) extraFieldsGrid.getWidget(order, 1);
			text.setText(value);
			
		}else  if (extraFieldsGrid.getWidget(order, 1) instanceof ListBox) {
			ListBox list = (ListBox) extraFieldsGrid.getWidget(order, 1);
			for (int i = 0; i < list.getItemCount(); i++) {
				if(value.equalsIgnoreCase(list.getItemText(i))){
					list.setSelectedIndex(i);
					break;
				}
			}
		}
	}


	
	
	@Override
	public HasClickHandlers getAddConditionButton() {
		return ((ContentView) contentView).conditionWidget.getAddConditionButton();
	}

	@Override
	public HasChangeHandlers getRsListBox() {
		return ((ContentView) contentView).conditionWidget.getRsListBox();
	}

	
	@Override
	public String getQuantityBox() {
		return ((ContentView) contentView).conditionWidget.getQuantityBox();
	}
	
	@Override
	public String getRsNameSelected() {
		return ((ContentView) contentView).conditionWidget.getRsNameSelected();
	}
	
	@Override
	public String getOpNameSelected() {
		return ((ContentView) contentView).conditionWidget.getOpNameSelected();
	}
	
	@Override
	public String getConditionSelected() {
		return ((ContentView) contentView).conditionWidget.getConditionSelected();
	}
	
	@Override
	public String getAritmSignSelected() {
		return ((ContentView) contentView).conditionWidget.getAritmSignSelected();	
	}
	
	@Override
	public String getLogicOpSelected() {
		return ((ContentView) contentView).conditionWidget.getLogicOpSelected();		
	}
	
	@Override
	public void setRsNames(List<String>  names) {
		((ContentView) contentView).conditionWidget.setRsNames( names);	
		((ContentView) contentView).conditionWidget.initializeRsListBox();
	}
	
	@Override
	public void setOpNames(List<String>  names) {
		((ContentView) contentView).conditionWidget. setOpNames(names);
	}
	
	@Override
	public void setConditionNames(List<String>  conditions) {
		((ContentView) contentView).conditionWidget.setConditionNames(conditions);
	}
	
	@Override
	public void setPolicyStatus(final boolean enabled) {
		this.policyStatus.setText(ConsoleUtil.policyAdminConstants.disable());
	}
	
	@Override
	public void setPolicyType(final String policyType) {
		this.policyType.setText(policyType);
	}
	

	
	@Override
	public void clearConditionBuilder() {
		((ContentView) contentView).conditionWidget.clearConditionBuilder();		
	}

	
	@Override
	public void setConditionBuilderVisible(boolean visible) {
		((ContentView) contentView).conditionPanel.setVisible(visible);
	}

	@Override
	public void setExclusionListsVisible(boolean visible){;
		((SubjectContentView) subjectContentView).setExclusionListVisible(visible);
	}


	public boolean validAllConditionFields(){
		return ((ContentView) contentView).conditionWidget.validAllFields();
	}
	
	
}
