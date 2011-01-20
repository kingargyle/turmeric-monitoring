/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.common;

import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.view.common.PolicyTemplateDisplay.MenuDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class PolicyMenuWidget extends AbstractGenericView implements MenuDisplay {	

    private SimplePanel mainPanel;
    private Tree tree;
    TreeItem sgParent;
    TreeItem polParent;
    TreeItem polCreateItem;
    TreeItem editHistoryItem;
    TreeItem authPolCreateItem;
    
    public PolicyMenuWidget() {		
        mainPanel = new SimplePanel();
        initWidget(mainPanel);
        initialize();
    }
	
	public void activate() {
		this.asWidget().setVisible(true);
	}
	
	

	@Override
	public void initialize() {
		mainPanel.clear();		
		tree = new Tree();
		initSubjectGroup(tree);
		initPolicy(tree);
		initEntityHistory(tree);
		mainPanel.add(tree);	
	}
	
	public void initSubjectGroup(Tree tree) {
	    Label sgHeader = new Label(ConsoleUtil.policyAdminConstants.subjectGroup());
	    sgParent = tree.addItem(sgHeader);
	    sgParent.setState(true, false);
	    TreeItem sgSummaryItem = sgParent.addItem(new Label(ConsoleUtil.policyAdminConstants.summary()));
	    sgSummaryItem.setUserObject(UserAction.SUBJECT_GROUP_SUMMARY);
	    TreeItem sgCreateItem = sgParent.addItem(new Label(ConsoleUtil.policyAdminConstants.create()));
	    sgCreateItem.setUserObject(UserAction.SUBJECT_GROUP_CREATE);
	    TreeItem sgImportItem = sgParent.addItem(new Label(ConsoleUtil.policyAdminConstants.importAction()));
	    sgImportItem.setUserObject(UserAction.SUBJECT_GROUP_IMPORT);
	}
	
	public void initPolicy(Tree tree) {
	    Label policyHeader = new Label(ConsoleUtil.policyAdminConstants.policies());
	    polParent = tree.addItem(policyHeader);
	    polParent.setState(true, false);
	    TreeItem polSummaryItem = polParent.addItem(new Label(ConsoleUtil.policyAdminConstants.summary()));
	    polSummaryItem.setUserObject(UserAction.POLICY_SUMMARY);
	    TreeItem polImportItem = polParent.addItem(new Label(ConsoleUtil.policyAdminConstants.importAction()));
	    polImportItem.setUserObject(UserAction.POLICY_IMPORT);
	    polCreateItem = polParent.addItem(new Label(ConsoleUtil.policyAdminConstants.create()));
	    
	    authPolCreateItem = polCreateItem.addItem(new Label(ConsoleUtil.policyAdminConstants.authzPolicy()));
	    authPolCreateItem.setUserObject(UserAction.AUTHZ_POLICY_CREATE);
	    TreeItem blPolCreateItem = polCreateItem.addItem(new Label(ConsoleUtil.policyAdminConstants.blPolicy()));
	    blPolCreateItem.setUserObject(UserAction.BL_POLICY_CREATE);
	    TreeItem wlPolCreateItem = polCreateItem.addItem(new Label(ConsoleUtil.policyAdminConstants.wlPolicy()));
	    wlPolCreateItem.setUserObject(UserAction.WL_POLICY_CREATE);
	    TreeItem rlPolCreateItem = polCreateItem.addItem(new Label(ConsoleUtil.policyAdminConstants.rateLimitingPolicy()));
	    rlPolCreateItem.setUserObject(UserAction.RL_POLICY_CREATE);
	}

	public void initEntityHistory(Tree tree) {
	    editHistoryItem = tree.addItem(new Label(ConsoleUtil.policyAdminConstants.changeHistory()));
	    editHistoryItem.setState(true, false);
	    TreeItem editHistoryViewItem = editHistoryItem.addItem(new Label(ConsoleUtil.policyAdminConstants.view()));
	    editHistoryViewItem.setUserObject(UserAction.CHANGE_HISTORY_SUMMARY);
	}
	
	public HasSelectionHandlers<TreeItem> getSelector() {
        return tree;
	}

	public void setActions(List<UserAction> actions) {
	    if (tree != null) {
	        for (int i=0;i<tree.getItemCount();i++) {
	            TreeItem item = tree.getItem(i);
	            setActions(item, actions);
	        }
	        tree.ensureSelectedItemVisible();
	    } 
	}

	public void setSelected (UserAction action) {
	    if (tree != null) {
	        for (int i=0;i<tree.getItemCount();i++) {
	            TreeItem item = tree.getItem(i);
	            setSelected(item, action);
	        }
	        tree.ensureSelectedItemVisible();
	    }
	}
	
	private void setSelected (TreeItem item, UserAction action) {
	    if (item == null || action == null)
	        return;
	    
	    if (item.getChildCount() > 0) {
	        for (int i=0;i<item.getChildCount(); i++) {
	            setSelected(item.getChild(i), action);
	        }
	    } else {
	        if (item.getUserObject() != null && action.equals(item.getUserObject()))
	            item.setSelected(true);
	        else
	            item.setSelected(false); 
	    }	    
	}
	
	
	private void setActions (TreeItem item, List<UserAction> actions) {
	    if (item == null || actions == null)
	        return;
	    if (item.getChildCount() > 0) {
	        for (int i=0; i<item.getChildCount(); i++)
	            setActions(item.getChild(i), actions);
	    } else {
	        UserAction action = (UserAction)item.getUserObject();
	        if (action != null && actions.contains(action))
	            item.setVisible(true);
	        else
	            item.setVisible(false);
	    }
	}
}
