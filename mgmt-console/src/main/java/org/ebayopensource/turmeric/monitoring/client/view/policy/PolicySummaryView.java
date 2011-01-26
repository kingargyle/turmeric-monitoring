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
import org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicySummaryPresenter.PolicySummaryDisplay;
import org.ebayopensource.turmeric.monitoring.client.view.ErrorDialog;
import org.ebayopensource.turmeric.monitoring.client.view.common.AbstractGenericView;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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

public class PolicySummaryView extends AbstractGenericView implements
		PolicySummaryDisplay {

	private final static UserAction SELECTED_ACTION = UserAction.POLICY_SUMMARY;

	private FlowPanel mainPanel;
	private ScrollPanel scrollPanel;
	private Display contentView;

	/**
	 * PolicySearchWidget
	 * 
	 */
	private class PolicySearchWidget extends Composite {
		private FlowPanel mainPanel;
		private FlowPanel radioPanel;
		private RadioButton policyCriteriaButton;
		private RadioButton resourceCriteriaButton;
		private RadioButton subjectCriteriaButton;
		private RadioButton subjectGroupCriteriaButton;
		private Button searchButton;
		TextBox searchBox;
		private Label typeLabel;
		private Label nameLabel;
		private Label opLabel;
		private Label rlEffectLabel;

		private ListBox typeBox;
		private ListBox rsNameBox;
		private ListBox rlEffectBox;

		private ListBox opBox;
		private List<String> policyTypes=new ArrayList<String>();
		private List<String> rsNames=new ArrayList<String>();
		private List<String> opNames=new ArrayList<String>();
		

		public PolicySearchWidget() {
			mainPanel = new FlowPanel();

			final Grid grid = new Grid(5, 2);
			// Search for a Policy by: SubjectType + Name or PolicyType + Name
			// TODO complete filter options
			radioPanel = new FlowPanel();

			policyCriteriaButton = new RadioButton("Criteria",
					ConsoleUtil.policyAdminConstants.policyCriteria());
			policyCriteriaButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					typeLabel.setText(ConsoleUtil.policyAdminConstants
							.policyType());
					nameLabel.setText(ConsoleUtil.policyAdminConstants
							.policyName());
					searchButton.setText(ConsoleUtil.policyAdminConstants
							.search());

					rlEffectLabel.setVisible(false);
					rlEffectBox.setVisible(false);
					grid.clearCell(2, 1);
					grid.setWidget(2, 1, searchBox);
					grid.setWidget(3,1, rlEffectBox);
					grid.setWidget(3, 0, rlEffectLabel);
					
					
										
				}
			});

			resourceCriteriaButton = new RadioButton("Criteria",
					ConsoleUtil.policyAdminConstants.resourceCriteria());
			resourceCriteriaButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					typeLabel.setText(ConsoleUtil.policyAdminConstants
							.resourceType());
					nameLabel.setText(ConsoleUtil.policyAdminConstants
							.resourceName());
					
					searchButton.setText(ConsoleUtil.policyAdminConstants
							.search());
					grid.clearCell(2, 1);
					grid.setWidget(2, 1, rsNameBox);
					grid.setWidget(3, 0, opLabel);
					grid.setWidget(3, 1, opBox);
				}
			});

			subjectCriteriaButton = new RadioButton("Criteria",
					ConsoleUtil.policyAdminConstants.subjectCriteria());
			subjectCriteriaButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					typeLabel.setText(ConsoleUtil.policyAdminConstants
							.subjectType());
					nameLabel.setText(ConsoleUtil.policyAdminConstants
							.subjectName());
					searchButton.setText(ConsoleUtil.policyAdminConstants
							.search());
					rlEffectLabel.setVisible(false);
					rlEffectBox.setVisible(false);
					grid.clearCell(2, 1);
					grid.setWidget(2, 1, searchBox);
					grid.clearCell(3, 0);
					grid.clearCell(3, 1);
				}
			});

			subjectGroupCriteriaButton = new RadioButton("Criteria",
					ConsoleUtil.policyAdminConstants.subjectGroupCriteria());
			subjectGroupCriteriaButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					typeLabel.setText(ConsoleUtil.policyAdminConstants
							.subjectType());
					nameLabel.setText(ConsoleUtil.policyAdminConstants
							.subjectName());
					searchButton.setText(ConsoleUtil.policyAdminConstants
							.search());
					rlEffectLabel.setVisible(false);
					rlEffectBox.setVisible(false);
					grid.clearCell(2, 1);
					grid.setWidget(2, 1, searchBox);
					grid.clearCell(3, 0);
					grid.clearCell(3, 1);
				}
			});

			radioPanel.add(policyCriteriaButton);
			radioPanel.add(resourceCriteriaButton);
			radioPanel.add(subjectCriteriaButton);
			radioPanel.add(subjectGroupCriteriaButton);

			typeLabel = new Label(
					ConsoleUtil.policyAdminConstants.policyType() );
			nameLabel = new Label(
					ConsoleUtil.policyAdminConstants.policyName() );
			
			opLabel = new Label(
					ConsoleUtil.policyAdminConstants.operationName());
			
			rlEffectLabel = new Label(
					ConsoleUtil.policyAdminConstants.effect());
			
			typeBox = new ListBox(false);
			rsNameBox = new ListBox(false);
			rlEffectBox = new ListBox(false);
			opBox = new ListBox(false);
			
			searchButton = new Button(ConsoleUtil.policyAdminConstants.search());
			searchBox = new TextBox();
			
			grid.setWidget(0, 1, radioPanel);
			grid.setWidget(1, 0, typeLabel);
			grid.setWidget(1, 1, typeBox);
			grid.setWidget(2, 0, nameLabel);
			grid.setWidget(2, 1, searchBox);
			grid.setWidget(4, 1, searchButton);
		
			mainPanel.add(grid);
			initWidget(mainPanel);
		}

		public void clear() {
			// select nothing
			policyCriteriaButton.setValue(false);
			resourceCriteriaButton.setValue(false);
			subjectCriteriaButton.setValue(false);
			subjectGroupCriteriaButton.setValue(false);
			opBox.clear();
			opBox.setEnabled(false);
			searchBox.setText("");
		}

		public HasClickHandlers getSubjectCriteriaButton() {
			return subjectCriteriaButton;
		}

		public HasClickHandlers getPolicyCriteriaButton() {
			return policyCriteriaButton;
		}

		public HasClickHandlers getResourceCriteriaButton() {
			return resourceCriteriaButton;
		}

		public HasClickHandlers getSubjectGroupCriteriaButton() {
			return subjectGroupCriteriaButton;
		}

		public HasClickHandlers getSearchButton() {
			return searchButton;
		}

		public void setSubjectCriteriaEnabled(boolean enabled) {
			subjectCriteriaButton.setValue(enabled);
		}

		public boolean isPolicyCriteriaEnabled() {
			return policyCriteriaButton.getValue() == true;
		}

		public boolean isSubjectCriteriaEnabled() {
			return subjectCriteriaButton.getValue() == true;
		}

		public boolean isSubjectGroupCriteriaEnabled() {
			return subjectGroupCriteriaButton.getValue() == true;
		}

		public boolean isResourceCriteriaEnabled() {
			return resourceCriteriaButton.getValue() == true;
		}

		public void setPolicyCriteriaEnabled(boolean enabled) {
			policyCriteriaButton.setValue(enabled);
		}

		public void setSubjectGroupCriteriaEnabled(boolean enabled) {
			subjectGroupCriteriaButton.setValue(enabled);
		}

		public void setResourceCriteriaEnabled(boolean enabled) {
			resourceCriteriaButton.setValue(enabled);
		}

		public void setRsNames(List<String> names) {
			rsNames = names;
		}
		
		public void setRLEffect(List<String> effects) {
			rlEffectBox.clear();
			for (String effect: effects) {
				rlEffectBox.addItem(effect);
			}
		}
		
		public void setOpNames(List<String> names) {
			opNames = names;
		}
		
		public String getSearchTerm() {
			return searchBox.getValue();
		}

		public List<String> getPolicyTypes(){
			return policyTypes;
		}
		
		public void  setPolicyTypes(List<String> types){
			policyTypes = types;
		}
		
		public String getSelectedType() {
			if (typeBox.getSelectedIndex() >= 0)
				return typeBox.getItemText(typeBox.getSelectedIndex());
			else
				return null;
		}

		
		public String getSelectedEffect() {
			if (rlEffectBox.getSelectedIndex() >= 0)
				return rlEffectBox.getItemText(rlEffectBox.getSelectedIndex());
			else
				return null;
		}
		
		public void setAvailableTypes(List<String> types) {
			// enable the selection of a subject type
			typeBox.clear();
			for (String s : types){
				typeBox.addItem(s);
			}
			searchBox.setText("");
		}

		public void setSelectedType(String type) {
			if (type == null)
				return;

			int idx = -1;
			for (int i = 0; i < typeBox.getItemCount() && idx < 0; i++) {
				if (typeBox.getItemText(i).equals(type))
					idx = i;
			}
			if (idx >= 0)
				typeBox.setSelectedIndex(idx);
		}

	
		public String getSelectedRsName() {
			if (rsNameBox.getSelectedIndex() >= 0)
				return rsNameBox.getItemText(rsNameBox.getSelectedIndex());
			else
				return null;
		}

		public void setRLEffectBoxVisible(boolean b) {
			rlEffectBox.setVisible(b);
		}
		
		public void setRLEffectLabelVisible(boolean b) {
			rlEffectLabel.setVisible(b);
		}
		
		public ListBox getRsNameBox() {
			return rsNameBox;
		}
		
		public ListBox getAvailableTypeBox() {
			return typeBox;
		}
		
		public ListBox getOperationNameBox() {
			return opBox;
		}
		
		
		public void setAvailableRsNames() {
			rsNameBox.clear();
			for (String name : rsNames) {
				rsNameBox.addItem(name);	
			}	
			
		}

		public void setSelectedRsName(String name) {
			if (name == null)
				return;

			int idx = -1;
			for (int i = 0; i < rsNameBox.getItemCount() && idx < 0; i++) {
				if (rsNameBox.getItemText(i).equals(name))
					idx = i;
			}
			if (idx >= 0)
				rsNameBox.setSelectedIndex(idx);
		}

		public String getSelectedOp() {
			if (opBox.getSelectedIndex() >= 0)
				return opBox.getItemText(opBox.getSelectedIndex());
			else
				return null;
		}

		public void setAvailableOps() {
			opBox.clear();
			for (String name : opNames) {
				opBox.addItem(name);	
			}	
		}

		public void setSelectedOp(String op) {
			if (op == null)
				return;

			int idx = -1;
			for (int i = 0; i < opBox.getItemCount() && idx < 0; i++) {
				if (opBox.getItemText(i).equals(op))
					idx = i;
			}
			if (idx >= 0)
				opBox.setSelectedIndex(idx);
		}

		public void setSelectedSearchTerm(String term) {
			searchBox.setValue((term == null ? "" : term));
		}

	}

	private class ContentView extends AbstractGenericView implements Display {
		private FlowPanel mainPanel;

		private CellTable<GenericPolicy> cellTable;
		ProvidesKey<GenericPolicy> keyProvider;
		DisclosurePanel searchPanel;
		PolicySearchWidget searchWidget;
		PushButton actionButton = new PushButton(ConsoleUtil.constants.apply());
		final Map<GenericPolicy, UserAction> pendingActions = new HashMap<GenericPolicy, UserAction>();
		final Map<GenericPolicy, List<UserAction>> permittedActions = new HashMap<GenericPolicy, List<UserAction>>();
		ListDataProvider<GenericPolicy> dataProvider;

		
		/**
		 * EnablePermissionCheckboxCell
		 *
		 */
		public class EnablePermissionCheckboxCell extends CustomPermissionCheckboxCell<GenericPolicy, Boolean> {

            /**
             * @param action
             * @param pendingActions
             * @param permittedActions
             */
            public EnablePermissionCheckboxCell(UserAction action,
                    Map<GenericPolicy, UserAction> pendingActions,
                    Map<GenericPolicy, List<UserAction>> permittedActions) {
                super(action, pendingActions, permittedActions);
            }
            
            public void render(GenericPolicy value, Object key, SafeHtmlBuilder sb) {
                if (value == null)
                    return;    

                 //if the user has permission for the action, then render according to
                //the boolean value else render as disabled
                List<UserAction> permitted = permittedActions.get(value);
                UserAction pending = pendingActions.get(value);
                
                boolean isChecked = false;
                boolean isDisabled = false;
           
                if (permitted != null && permitted.contains(this.action)) {
                    //The user is permitted to perform the enable/disable action
                    //Check what the enable/disable state of the policy is and render appropriately
                    boolean isEnabled = value.getEnabled();
                    switch (action) {
                        case POLICY_ENABLE: {
                            //this is the enable checkbox, and the policy is already enabled, render as disabled
                            if (isEnabled) {
                                isChecked = false;
                                isDisabled = true;
                    
                            } else {
                                //policy is not already enabled, decide whether to render as checked or not
                                //based on if there is a pending action to enable it
                                isDisabled = false;
                                isChecked = (pending != null && pending.equals(action) ? true : false);
                            }
                            break;
                        }
                        case POLICY_DISABLE: {
                            //if this is the disable checkbox, and the policy is already disabled, render as disabled
                            if (!isEnabled) {
                                isChecked = false;
                                isDisabled = true;
                            }  else {
                                //the policy is not already disabled, decide whether to render it as checked or not
                                //based on if there is a pending action to disable it
                                isDisabled = false;
                                isChecked = (pending != null && pending.equals(action) ? true : false);
                            }
                            break;
                        }
                    }
                }
                else {
                    //the user is not permitted to perform the enable or disable action, so render it always as 
                    //disabled. 
                    isDisabled = true;
                    isChecked = false;
                }
                
                sb.appendHtmlConstant("<input type='checkbox' " + (isDisabled? "disabled=disabled" :"")+ (isChecked? " checked ": "")+"></input>");
              }
		}
		
		public ContentView() {
			mainPanel = new FlowPanel();
			initWidget(mainPanel);
			initialize();
		}

		public void setPolicies(List<GenericPolicy> policies) {			
		    cellTable.setRowCount(0);
		    List<GenericPolicy> list;
		    if (policies == null)
		        list = Collections.emptyList();
		    else
		        list = policies;
		    dataProvider.setList(list);
		    dataProvider.refresh();
		}

		public void setUserActions(GenericPolicy policy, List<UserAction> enabledActions) {
		    if (policy == null)
                return;
            if (enabledActions == null)
                permittedActions.remove(policy);
            else
                permittedActions.put(policy, new ArrayList<UserAction>(enabledActions));
           if (cellTable != null)
               cellTable.redraw();
		}
		

		public Map<GenericPolicy, UserAction> getPendingActions () {
		    return pendingActions;
		}

		public void activate() {
			// do nothing for now
		    updateButton(ConsoleUtil.constants.apply());
		    actionButton.setEnabled(false);
		}

		@Override
		public void initialize() {
			mainPanel.clear();

			// top part of contentPanel is a disclosure panel with a search
			// feature
			searchWidget = new PolicySearchWidget();
			searchPanel = new DisclosurePanel(
					ConsoleUtil.policyAdminConstants.search());
			searchPanel.setContent(searchWidget);

			// bottom part of panel is a table with search results
			Grid summaryGrid = new Grid(3, 1);
			summaryGrid.setStyleName("sggrid");

			Grid actionGrid = new Grid(1, 1);
			actionGrid.setWidget(0,0,actionButton);
			summaryGrid.setWidget(0,0, actionGrid);
			
			keyProvider = new ProvidesKey<GenericPolicy>() {
				public Object getKey(GenericPolicy policy) {
					return policy == null ? null : policy.getId();
				}
			};

			cellTable = new CellTable<GenericPolicy>(keyProvider);
			dataProvider = new ListDataProvider<GenericPolicy>();
			dataProvider.addDataDisplay(cellTable);
			SimplePager pager = new SimplePager();
			pager.setDisplay(cellTable);

			
			 //checkbox column for view selection
            Column<GenericPolicy, GenericPolicy> viewColumn = new Column<GenericPolicy, GenericPolicy>(
                    new CustomPermissionCheckboxCell<GenericPolicy, Boolean>(UserAction.POLICY_VIEW, pendingActions, permittedActions)) {
                public GenericPolicy getValue(GenericPolicy policy) {
                    return policy;
                }
            };
            viewColumn.setFieldUpdater(new FieldUpdater<GenericPolicy, GenericPolicy>() {

                @Override
                public void update(int arg0, GenericPolicy arg1,GenericPolicy arg2) {
                    //if it was in there, remove it
                    if (pendingActions.keySet().contains(arg1)) {
                        pendingActions.remove(arg1);
                        updateButton(null);
                    } else {
                        pendingActions.clear();
                        pendingActions.put(arg1, UserAction.POLICY_VIEW);
                        updateButton(ConsoleUtil.policyAdminConstants.view());
                    }

                    cellTable.redraw();
                }
            });
            cellTable.addColumn(viewColumn, ConsoleUtil.policyAdminConstants.view());

         
            //checkbox column for edit
            Column<GenericPolicy, GenericPolicy> editColumn = new Column<GenericPolicy, GenericPolicy>(
                    new CustomPermissionCheckboxCell<GenericPolicy,Boolean>(UserAction.POLICY_EDIT, pendingActions, permittedActions)) {
                public GenericPolicy getValue(GenericPolicy group) {
                  return group;
                }
            };
            editColumn.setFieldUpdater(new FieldUpdater<GenericPolicy, GenericPolicy>() {
                public void update(int arg0, GenericPolicy arg1,GenericPolicy arg2) {
                    if (pendingActions.keySet().contains(arg1)) {
                        pendingActions.remove(arg1);
                        updateButton(null);
                    }else {
                        // Called when the user clicks on a checkbox.
                        // Only 1 edit can be active at a time
                        pendingActions.clear();
                        pendingActions.put(arg1, UserAction.POLICY_EDIT);
                        updateButton(ConsoleUtil.policyAdminConstants.edit());
                    }
                  
                    cellTable.redraw();
                }
            });
            cellTable.addColumn(editColumn, ConsoleUtil.policyAdminConstants.edit());


            //checkbox column for delete
            Column<GenericPolicy, GenericPolicy> deleteColumn = new Column<GenericPolicy, GenericPolicy>(
                    new CustomPermissionCheckboxCell<GenericPolicy, Boolean>(UserAction.POLICY_DELETE, pendingActions, permittedActions)) {
                public GenericPolicy getValue(GenericPolicy group) {
                   return group;
                }
            };
            deleteColumn.setFieldUpdater(new FieldUpdater<GenericPolicy, GenericPolicy>() {
                public void update(int index, GenericPolicy group, GenericPolicy arg2) {

                    if (pendingActions.keySet().contains(group)) {
                        pendingActions.remove(group);
                        if (pendingActions.size() == 0)
                            updateButton(null);

                    } else {
                        // Called when the user clicks on a checkbox.
                        // Many groups can be selected for delete at the same time,
                        // but any other pending ops must be cancelled
                        Iterator<Map.Entry<GenericPolicy,UserAction>> itor = pendingActions.entrySet().iterator();
                        while (itor.hasNext()) {
                            Map.Entry<GenericPolicy, UserAction> entry = itor.next();
                            if (!entry.getValue().equals(UserAction.POLICY_DELETE))
                                itor.remove();
                        }

                        updateButton(ConsoleUtil.policyAdminConstants.delete());
                        pendingActions.put(group, UserAction.POLICY_DELETE);
                    }
                    cellTable.redraw();
                }
            });
            cellTable.addColumn(deleteColumn, ConsoleUtil.policyAdminConstants.delete());
            
			
            
            //checkbox column for enable
            Column<GenericPolicy, GenericPolicy> enableColumn = new Column<GenericPolicy, GenericPolicy>(
                    new EnablePermissionCheckboxCell(UserAction.POLICY_ENABLE, pendingActions, permittedActions)) {
                public GenericPolicy getValue(GenericPolicy group) {
                   return group;
                }
            };
            enableColumn.setFieldUpdater(new FieldUpdater<GenericPolicy, GenericPolicy>() {
                public void update(int index, GenericPolicy policy, GenericPolicy arg2) {

                    if (pendingActions.keySet().contains(policy)) {
                        pendingActions.remove(policy);
                        if (pendingActions.size() == 0)
                            updateButton(null);

                    } else {
                        // Called when the user clicks on a checkbox.
                        // Many groups can be selected for enable/disable at the same time,
                        // but any other pending ops must be cancelled
                        Iterator<Map.Entry<GenericPolicy,UserAction>> itor = pendingActions.entrySet().iterator();
                        while (itor.hasNext()) {
                            Map.Entry<GenericPolicy, UserAction> entry = itor.next();
                            if (!entry.getValue().equals(UserAction.POLICY_ENABLE) && !entry.getValue().equals(UserAction.POLICY_DISABLE))
                                itor.remove();
                        }

                        updateButton(ConsoleUtil.policyAdminConstants.enable()+"/"+ConsoleUtil.policyAdminConstants.disable());
                        pendingActions.put(policy, UserAction.POLICY_ENABLE);
                    }
                    cellTable.redraw();
                }
            });
            cellTable.addColumn(enableColumn, ConsoleUtil.policyAdminConstants.enable());
            
            //checkbox column for disable
            Column<GenericPolicy, GenericPolicy> disableColumn = new Column<GenericPolicy, GenericPolicy>(
                    new EnablePermissionCheckboxCell(UserAction.POLICY_DISABLE, pendingActions, permittedActions)) {
                public GenericPolicy getValue(GenericPolicy group) {
                   return group;
                }
            };
            disableColumn.setFieldUpdater(new FieldUpdater<GenericPolicy, GenericPolicy>() {
                public void update(int index, GenericPolicy policy, GenericPolicy arg2) {
                    if (pendingActions.keySet().contains(policy)) {
                        pendingActions.remove(policy);
                        if (pendingActions.size() == 0)
                            updateButton(null);

                    } else {
                        // Called when the user clicks on a checkbox.
                        // Many groups can be selected for enable at the same time,
                        // but any other pending ops must be cancelled
                        Iterator<Map.Entry<GenericPolicy,UserAction>> itor = pendingActions.entrySet().iterator();
                        while (itor.hasNext()) {
                            Map.Entry<GenericPolicy, UserAction> entry = itor.next();
                            if (!entry.getValue().equals(UserAction.POLICY_DISABLE) && !entry.getValue().equals(UserAction.POLICY_ENABLE))
                                itor.remove();
                        }

                        updateButton(ConsoleUtil.policyAdminConstants.enable()+"/"+ConsoleUtil.policyAdminConstants.disable());
                        pendingActions.put(policy, UserAction.POLICY_DISABLE);
                    }
                    cellTable.redraw();
                }
            });
            cellTable.addColumn(disableColumn, ConsoleUtil.policyAdminConstants.disable());
            

			// policy name
			TextColumn<GenericPolicy> policyNameCol = new TextColumn<GenericPolicy>() {
				public String getValue(GenericPolicy policy) {
					if (policy == null)
						return null;
					return policy.getName();
				}
			};
			cellTable.addColumn(policyNameCol,
					ConsoleUtil.policyAdminConstants.policyName());

	         //policy description
            Column<GenericPolicy, String> descCol = new Column<GenericPolicy, String>(new DescriptionCell()) {
                
                public  String getValue(GenericPolicy p) {
                    return p.getDescription();
                }            
            };
			cellTable.addColumn(descCol,
					ConsoleUtil.policyAdminConstants.policyDescription());

			// policy type
			TextColumn<GenericPolicy> policyTypeCol = new TextColumn<GenericPolicy>() {
				public String getValue(GenericPolicy policy) {
					return (policy == null ? null : policy.getType());
				}
			};
			cellTable.addColumn(policyTypeCol,
					ConsoleUtil.policyAdminConstants.policyType());

	
			// created by
			TextColumn<GenericPolicy> policyCreatedByCol = new TextColumn<GenericPolicy>() {
				public String getValue(GenericPolicy policy) {
					return (policy == null ? null : policy.getCreatedBy());
				}
			};
			cellTable.addColumn(policyCreatedByCol,
					ConsoleUtil.policyAdminConstants.createdBy());

			// Create date
			Column<GenericPolicy, Date> policyCreatedDateCol = new Column<GenericPolicy, Date>(
					new DateCell(ConsoleUtil.tzTimeFormat)) {
				public Date getValue(GenericPolicy policy) {
					return (policy == null ? null : policy.getCreationDate());
				}
			};
			cellTable.addColumn(policyCreatedDateCol,
					ConsoleUtil.policyAdminConstants.createTime());

			// Last modified by
			TextColumn<GenericPolicy> policyModifiedByCol = new TextColumn<GenericPolicy>() {
				public String getValue(GenericPolicy policy) {
					return (policy == null ? null : policy.getLastModifiedBy());
				}
			};
			cellTable.addColumn(policyModifiedByCol,
					ConsoleUtil.policyAdminConstants.lastModifiedBy());

			// Last modified date
			Column<GenericPolicy, Date> policyModifiedDateCol = new Column<GenericPolicy, Date>(
					new DateCell(ConsoleUtil.tzTimeFormat)) {
				public Date getValue(GenericPolicy policy) {
					return (policy == null ? null : policy.getLastModified());
				}
			};
			cellTable.addColumn(policyModifiedDateCol,
					ConsoleUtil.policyAdminConstants.lastModifiedTime());

			// policy status
			TextColumn<GenericPolicy> policyStatusCol = new TextColumn<GenericPolicy>() {
				public String getValue(GenericPolicy policy) {
					return (policy == null ? null
							: policy.getEnabled() ? "ENABLED" : "DISABLED");
				}
			};
			cellTable.addColumn(policyStatusCol,
					ConsoleUtil.policyAdminConstants.status());

			summaryGrid.setWidget(1, 0, cellTable);
			summaryGrid.setWidget(2, 0, pager);

			mainPanel.addStyleName("policy-summary");
			mainPanel.add(searchPanel);
			searchPanel.addStyleName("policy-content");
			summaryGrid.addStyleName("policy-content");
			mainPanel.add(summaryGrid);
		}
		
		public HasClickHandlers getActionButton () {
		    return actionButton;
		}

		private void updateButton (String label) {
		    if (label == null){ 
		        label = ConsoleUtil.constants.apply(); 
		    } else{
		        actionButton.setEnabled(true);
		    }
		    
		    
		    actionButton.setText(label);
		}

	}

	public PolicySummaryView() {
	    scrollPanel = new ScrollPanel();
		mainPanel = new FlowPanel();
		scrollPanel.add(mainPanel);
		initWidget(scrollPanel);

		initialize();
	}

	public void error(String msg) {
		ErrorDialog dialog = new ErrorDialog(true);
		dialog.setMessage(msg);
		dialog.getDialog().center();
		dialog.show();
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

    public void setPermittedActions (GenericPolicy policy, List<UserAction> permittedActions) {
        ((ContentView)contentView).setUserActions(policy, permittedActions);
    }
	@Override
	public void setPolicies(List<GenericPolicy> policies) {
		((ContentView) contentView).setPolicies(policies);
	}

	@Override
	public HasClickHandlers getSearchButton() {
		return ((ContentView) contentView).searchWidget.getSearchButton();
	}

	@Override
	public String getSearchTerm() {
		return ((ContentView) contentView).searchWidget.getSearchTerm();
	}

	@Override
	public String getSelectedType() {
		return ((ContentView) contentView).searchWidget.getSelectedType();
	}
	
	@Override
	public String getSelectedEffect() {
		return ((ContentView) contentView).searchWidget.getSelectedEffect();
	}

	@Override
	public boolean isPolicyCriteriaEnabled() {
		return ((ContentView) contentView).searchWidget
				.isPolicyCriteriaEnabled();
	}

	@Override
	public boolean isResourceCriteriaEnabled() {
		return ((ContentView) contentView).searchWidget
				.isResourceCriteriaEnabled();
	}

	@Override
	public boolean isSubjectCriteriaEnabled() {
		return ((ContentView) contentView).searchWidget
				.isSubjectCriteriaEnabled();
	}

	@Override
	public boolean isSubjectGroupCriteriaEnabled() {
		return ((ContentView) contentView).searchWidget
				.isSubjectGroupCriteriaEnabled();
	}

	@Override
	public boolean isSearchCriteriaEnabled() {
		return ((ContentView) contentView).searchWidget
				.isSubjectCriteriaEnabled();
	}

	@Override
	public void setAvailableTypes(List<String> types) {
		((ContentView) contentView).searchWidget.setAvailableTypes(types);
		((ContentView) contentView).searchWidget.getAvailableTypeBox().setSelectedIndex(-1);
		if(! ((ContentView) contentView).searchWidget.isResourceCriteriaEnabled()){
			((ContentView) contentView).searchWidget.getRsNameBox().setSelectedIndex(-1);
			((ContentView) contentView).searchWidget.getOperationNameBox().setSelectedIndex(-1);
		}
	}

	@Override
	public void setResourceNames() {
		((ContentView) contentView).searchWidget.setAvailableRsNames();
		((ContentView) contentView).searchWidget.getRsNameBox().setSelectedIndex(-1);
		((ContentView) contentView).searchWidget.getOperationNameBox().clear();
		((ContentView) contentView).searchWidget.getOperationNameBox().setSelectedIndex(-1);
		((ContentView) contentView).searchWidget.getOperationNameBox().setEnabled(true);
	}
	
	@Override
	public void setOperationNames() {
		((ContentView) contentView).searchWidget.setAvailableOps();
		((ContentView) contentView).searchWidget.getOperationNameBox().setSelectedIndex(-1);
	}
	
	public HasClickHandlers getSubjectCriteriaButton() {
		return ((ContentView) contentView).searchWidget
				.getSubjectCriteriaButton();
	}

	public HasClickHandlers getPolicyCriteriaButton() {
		return ((ContentView) contentView).searchWidget
				.getPolicyCriteriaButton();
	}

	public HasClickHandlers getResourceCriteriaButton() {
		return ((ContentView) contentView).searchWidget
				.getResourceCriteriaButton();
	}

	public HasClickHandlers getSubjectGroupCriteriaButton() {
		return ((ContentView) contentView).searchWidget
				.getSubjectGroupCriteriaButton();
	}

	@Override
	public void setSelectedSearchTerm(String name) {
		((ContentView) contentView).searchPanel.setOpen(true);
		((ContentView) contentView).searchWidget.setSelectedSearchTerm(name);
	}

	@Override
	public void setSelectedType(String type) {
		((ContentView) contentView).searchPanel.setOpen(true);
		((ContentView) contentView).searchWidget.setSelectedType(type);
	}

	@Override
	public void setPolicyCriteriaEnabled(boolean enabled) {
		((ContentView) contentView).searchPanel.setOpen(true);
		((ContentView) contentView).searchWidget
				.setPolicyCriteriaEnabled(enabled);

	}

	@Override
	public void setResourceCriteriaEnabled(boolean enabled) {
		((ContentView) contentView).searchPanel.setOpen(true);
		((ContentView) contentView).searchWidget
				.setResourceCriteriaEnabled(enabled);

	}

	@Override
	public void setSubjectCriteriaEnabled(boolean enabled) {
		((ContentView) contentView).searchPanel.setOpen(true);
		((ContentView) contentView).searchWidget
				.setSubjectCriteriaEnabled(enabled);

	}

	@Override
	public void setSubjectGroupCriteriaEnabled(boolean enabled) {
		((ContentView) contentView).searchPanel.setOpen(true);
		((ContentView) contentView).searchWidget
				.setSubjectGroupCriteriaEnabled(enabled);

	}

	@Override
	public void setSearchCriteriaEnabled(boolean enabled) {
		((ContentView) contentView).searchPanel.setOpen(true);
		((ContentView) contentView).searchWidget
				.setSubjectCriteriaEnabled(enabled);
	}
	
	@Override
	public List<String> getPolicyTypes(){
		return ((ContentView) contentView).searchWidget.getPolicyTypes();
	}
	
	@Override
	public void  setPolicyTypes(List<String> types){
		((ContentView) contentView).searchWidget.setPolicyTypes(types);
	}

	@Override
	public String getSelectedResource() {
		return ((ContentView) contentView).searchWidget.getSelectedRsName();
	}

	@Override
	public String getSelectedOperation() {
		return ((ContentView) contentView).searchWidget.getSelectedOp();
	}
	
	@Override
	public HasChangeHandlers getResourceNameBox() {
		return ((ContentView) contentView).searchWidget
				.getRsNameBox();
	}
	
	@Override
	public HasChangeHandlers getAvailableTypesBox() {
		return ((ContentView) contentView).searchWidget
				.getAvailableTypeBox();
	}
	
	
	@Override
	public void setRLEffectBoxVisible(boolean b) {
		((ContentView) contentView).searchWidget
				.setRLEffectBoxVisible(b);
	}

	@Override
	public void setRLEffectLabelVisible(boolean b) {
		((ContentView) contentView).searchWidget
			.setRLEffectLabelVisible(b);
	}

	@Override
	public void setRsNames(List<String> names) {
		((ContentView) contentView).searchWidget.setRsNames(names);		
	}
	
	
	@Override
	public void setOpNames(List<String> names) {
		((ContentView) contentView).searchWidget.setOpNames(names);
	}

	@Override
	public void setEffect(List<String> effects) {
		((ContentView) contentView).searchWidget.setRLEffect(effects);
				
	}


    @Override
    public HasClickHandlers getActionButton() {
        return ((ContentView) contentView).getActionButton();
    }

    @Override
    public Map<GenericPolicy, UserAction> getPendingActions() {
       return ((ContentView) contentView).getPendingActions();
    }
}
