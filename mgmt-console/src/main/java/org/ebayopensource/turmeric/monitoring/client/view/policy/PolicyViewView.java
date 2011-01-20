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
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.model.Resource;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.model.policy.ExtraField;
import org.ebayopensource.turmeric.monitoring.client.model.policy.Operation;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicySubjectAssignment;
import org.ebayopensource.turmeric.monitoring.client.model.policy.Subject;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyViewPresenter.PolicyViewDisplay;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyViewPresenter.ResourcesContentDisplay;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyViewPresenter.SubjectContentDisplay;
import org.ebayopensource.turmeric.monitoring.client.util.PolicyExtraFieldsUtil;
import org.ebayopensource.turmeric.monitoring.client.view.ErrorDialog;
import org.ebayopensource.turmeric.monitoring.client.view.common.AbstractGenericView;
import org.ebayopensource.turmeric.monitoring.client.view.policy.PolicyCreateView.ContentView;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;

public class PolicyViewView extends ResizeComposite implements
		PolicyViewDisplay {

	private DockLayoutPanel mainPanel;
	private static final String TITLE_FORM = ConsoleUtil.policyAdminConstants
			.policyInformationView();
	protected static UserAction SELECTED_ACTION = UserAction.POLICY_VIEW;

	private Display contentView;
	private ResourcesContentDisplay resourceContentView;
	private SubjectContentDisplay subjectContentView;

	protected Label policyName;
	protected Label policyDesc;
	protected boolean extraGridAvailable;
	
	protected boolean policyEnabled;

	private Button cancelButton;

	protected Grid extraFieldsGrid = new Grid(1, 1);
	protected List<ExtraField> extraFieldList;


		
	public PolicyViewView() {
		mainPanel = new DockLayoutPanel(Unit.EM);
		initWidget(mainPanel);
		mainPanel.setWidth("100%");
		initialize();

	}

	@Override
	public UserAction getSelectedAction() {
		return SELECTED_ACTION;
	}

	public void initialize() {

		policyName = new Label();
		policyDesc = new Label();
		extraFieldList = new ArrayList<ExtraField>();

		// CONTENT
		StackLayoutPanel policyContentPanel = new StackLayoutPanel(Unit.EM);
		policyContentPanel.add(initContentView(), TITLE_FORM, 2);
		policyContentPanel.add(initResourceContentView(),
				ConsoleUtil.policyAdminConstants.resources(), 2);
		policyContentPanel.add(initSubjectContentView(),
				ConsoleUtil.policyAdminConstants.subjectsAndSubjectGroups(), 2);
		policyContentPanel.setHeight("90%");

		cancelButton = new Button(ConsoleUtil.constants.cancel());
		HorizontalPanel buttonsPannel = new HorizontalPanel();
		buttonsPannel.add(cancelButton);

		mainPanel.addSouth(buttonsPannel, 1);
		mainPanel.add(policyContentPanel);
	
	
	}

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

	private class ContentView extends AbstractGenericView implements Display {
		private VerticalPanel mainPanel;
		private Grid policyInfoGrid;

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

			policyInfoGrid = new Grid(2, 2);

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

			mainPanel.add(policyInfoGrid);
			setExtraFields();
			mainPanel.add(extraFieldsGrid);
			extraFieldsGrid.setVisible(extraGridAvailable);
			
		}

		protected void setExtraFields() {
			// TODO move this setting to a particular RL class
			extraFieldList = PolicyExtraFieldsUtil.getRLExtraFields();
			extraFieldsGrid = new Grid(extraFieldList.size() + 1, 3);
			for (ExtraField extraField : extraFieldList) {

				extraFieldsGrid.setWidget(extraField.getOrder()-1, 0, new Label(
						extraField.getLabelName()));

				if (extraField.getFieldType() != null
						&& "CheckBox".equalsIgnoreCase(extraField
								.getFieldType())) {
					CheckBox chBox = new CheckBox();
					// TODO set value
					chBox.setEnabled(false);
					extraField.setCheckBox(chBox);
					extraFieldsGrid.setWidget(extraField.getOrder()-1, 1,
							extraField.getCheckBox());
				} else if (extraField.getFieldType() != null
						&& 
						("TextBox".equalsIgnoreCase(extraField
								.getFieldType())
						||"TextArea".equalsIgnoreCase(extraField
								.getFieldType())
						|| "ListBox".equalsIgnoreCase(extraField
								.getFieldType()))) {
					Label label = new Label(" ");
					extraFieldsGrid.setWidget(extraField.getOrder()-1, 1, label);
				}
			}
			
		}
	}

	private class ResourceContentView extends AbstractGenericView implements
			ResourcesContentDisplay {
		private FlowPanel mainPanel;

		private CellTable<Resource> cellTable;
		ProvidesKey<Resource> keyProvider;
		private ListDataProvider<Resource> dataProvider;

		public ResourceContentView() {
			mainPanel = new FlowPanel();
			initWidget(mainPanel);
			initialize();
		}

		public void activate() {
			// do nothing for now
		}

		@Override
		public void initialize() {
			mainPanel.clear();

			// bottom part of panel is a table with search results
			Grid summaryGrid = new Grid(2, 1);
			summaryGrid.setStyleName("sggrid");

			keyProvider = new ProvidesKey<Resource>() {
				public Object getKey(Resource assignment) {
					return assignment == null ? null : assignment
							.getResourceType() + assignment.getResourceName();
				}
			};

			cellTable = new CellTable<Resource>(keyProvider);

			dataProvider = new ListDataProvider<Resource>();
			dataProvider.addDataDisplay(cellTable);
			SimplePager pager = new SimplePager();
			pager.setDisplay(cellTable);
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
			TextColumn<Resource> resourceOpsCol = new TextColumn<Resource>() {
				public String getValue(Resource assignment) {
					if (assignment == null || assignment.getOpList() == null)
						return null;

					StringBuilder strbuilder = new StringBuilder();
					for (Operation op : assignment.getOpList()) {
						strbuilder.append(op.getOperationName() + "\n");
					}
					return strbuilder.toString();
				}
			};

			cellTable.addColumn(resourceOpsCol,
					ConsoleUtil.policyAdminConstants.operations());

			summaryGrid.setWidget(0, 0, cellTable);
			summaryGrid.setWidget(1, 0, pager);

			mainPanel.addStyleName("resource-summary");
			summaryGrid.addStyleName("resource-content");
			mainPanel.add(summaryGrid);
		}

		@Override
		public void error(String msg) {
			ErrorDialog dialog = new ErrorDialog(true);
			dialog.setMessage(msg);
			dialog.getDialog().center();
			dialog.show();

		}

		public void setAssignments(List<Resource> assignments) {
			List<Resource> data = dataProvider.getList();
			data.clear();
			if (assignments != null)
				data.addAll(assignments);
			cellTable.redraw();
		}

	}

	private class SubjectContentView extends AbstractGenericView implements
			SubjectContentDisplay {
		private SimplePanel mainPanel;
		private Grid mainGrid;
		private CellTable<PolicySubjectAssignment> cellTable;
		private ProvidesKey<PolicySubjectAssignment> keyProvider;
		private MultiSelectionModel<PolicySubjectAssignment> selectionModel;
		private ListDataProvider<PolicySubjectAssignment> dataProvider;
		private Grid subjectGrid;
		private SimplePager pager;

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
			createSubjectTableFields();
			subjectGrid.setWidget(0, 0, cellTable);
			subjectGrid.setWidget(1, 0, pager);
			mainGrid.setWidget(1, 0, subjectGrid);
			mainGrid.setWidth("60%");
		}

		protected void createSubjectTableFields() {

			keyProvider = new ProvidesKey<PolicySubjectAssignment>() {
				public Object getKey(PolicySubjectAssignment assignment) {
					return assignment == null ? null : assignment
							.getSubjectType();
				}
			};

			cellTable = new CellTable<PolicySubjectAssignment>(keyProvider);

			dataProvider = new ListDataProvider<PolicySubjectAssignment>();
			dataProvider.addDataDisplay(cellTable);

			pager = new SimplePager();
			pager.setDisplay(cellTable);
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
			TextColumn<PolicySubjectAssignment> subjectNamesCol = new TextColumn<PolicySubjectAssignment>() {
				public String getValue(PolicySubjectAssignment assignment) {
					if (assignment == null || assignment.getSubjects() == null)
						return null;

					StringBuilder strbuilder = new StringBuilder();
					for (Subject s : assignment.getSubjects()) {
						strbuilder.append(s.getName() + " ");
					}
					return strbuilder.toString();
				}
			};
			cellTable.addColumn(subjectNamesCol,
					ConsoleUtil.policyAdminConstants.subjects());

			// text column for SubjectGroup names
			TextColumn<PolicySubjectAssignment> subjectGroupNamesCol = new TextColumn<PolicySubjectAssignment>() {
				public String getValue(PolicySubjectAssignment assignment) {
					if (assignment == null
							|| assignment.getSubjectGroups() == null)
						return null;

					StringBuilder strbuilder = new StringBuilder();
					for (SubjectGroup s : assignment.getSubjectGroups()) {
						strbuilder.append(s.getName() + " ");
					}
					return strbuilder.toString();
				}
			};
			cellTable.addColumn(subjectGroupNamesCol,
					ConsoleUtil.policyAdminConstants.subjectGroups());
		}

		public void setAssignments(List<PolicySubjectAssignment> assignments) {
			List<PolicySubjectAssignment> data = dataProvider.getList();
			data.clear();

			if (assignments != null)
				data.addAll(assignments);
			cellTable.redraw();
		}

	}

	public void activate() {
		contentView.activate();
		this.setVisible(true);
	}

	public UserAction getActionSelected() {
		return UserAction.BL_POLICY_CREATE;
	}

	public Display getContentView() {
		return contentView;
	}

	public ResourcesContentDisplay getResourceContentView() {
		return resourceContentView;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	@Override
	public SubjectContentDisplay getSubjectContentView() {
		return subjectContentView;
	}

	@Override
	public void setPolicyDesc(final String policyDesc) {
		this.policyDesc.setText(policyDesc);
	}

	@Override
	public void setPolicyName(final String policyName) {
		this.policyName.setText(policyName);
	}

	@Override
	public void setExtraFieldAvailable(final boolean available) {
		this.extraGridAvailable = available;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		this.policyEnabled = enabled;
	}

	@Override
	public void setExtraFieldList(List<ExtraField> extraFieldList) {

		for (ExtraField extraField : extraFieldList) {

				if (extraField.getFieldType() != null
						&& "CheckBox".equalsIgnoreCase(extraField
								.getFieldType())) {
					CheckBox ch = (CheckBox) extraFieldsGrid.getWidget(
							extraField.getOrder()-1, 1);
					ch.setValue(Boolean.parseBoolean(extraField.getValue()));

				} else if (extraField.getFieldType() != null
						&& ("Label".equalsIgnoreCase(extraField.getFieldType()))) {
					Label lbl = (Label) extraFieldsGrid.getWidget(
							extraField.getOrder()-1, 1);
					lbl.setText(extraField.getValue());
				
				}
			}
			extraFieldsGrid.setVisible(extraGridAvailable);
	}

	public void clear() {
		policyName.setText("");
		policyDesc.setText("");
		extraGridAvailable = false;
		
		
		if (this.extraFieldList != null && this.extraFieldList.size() > 0) {
			
			for (int i = 0; i < extraFieldList.size(); i++) {
				extraFieldList.get(i).setValue("");
			}
				
			setExtraFieldList(extraFieldList);
		}
		
		
	}

	public void error(String msg) {
		ErrorDialog dialog = new ErrorDialog(true);
		dialog.setMessage(msg);
		dialog.getDialog().center();
		dialog.show();
	}

	@Override
	public void setAssociatedId(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAssociatedId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
