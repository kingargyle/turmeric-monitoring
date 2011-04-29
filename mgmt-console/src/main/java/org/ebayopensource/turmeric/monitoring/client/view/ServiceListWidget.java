/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * The Class ServiceListWidget.
 */
public class ServiceListWidget extends Composite {
    
    /** The service list. */
    protected ScrollPanel serviceList;
    
    /** The service tree. */
    protected Tree serviceTree;
    
    /** The root. */
    protected TreeItem root;
    
    /** The selected service. */
    protected String selectedService;
    
    /** The selected operation. */
    protected String selectedOperation;

    /**
     * Instantiates a new service list widget.
     */
    public ServiceListWidget () {
        
        serviceList = new ScrollPanel();
        serviceTree = new Tree();
        serviceList.add(serviceTree);
        initWidget(serviceList);
    }
    
    /**
     * Gets the service tree.
     *
     * @return the service tree
     */
    public Tree getServiceTree () {
        return serviceTree;
    }
    
    /**
     * Gets the service list.
     *
     * @return the service list
     */
    public ScrollPanel getServiceList () {
        return serviceList;
    }
    
    /**
     * Sets the services map.
     *
     * @param map the map
     */
    public void setServicesMap(Map<String, Set<String>> map) {
        serviceTree.removeItems();
        if (map == null) 
            return;   
            
        Label rootLabel = new Label(ConsoleUtil.constants.services()+"("+map.size()+")");
        rootLabel.getElement().setId("service-tree-root");
        root = new TreeItem (rootLabel);
        root.addStyleName("turmeric-selectable");
        
        TreeItem selectedItem = null;
        
        for (Map.Entry<String, Set<String>> e:map.entrySet()) {
            Label label = new Label(e.getKey());
            TreeItem serviceItem = new TreeItem(label);
            if (selectedService != null && selectedService.equals(e.getKey()))
                selectedItem = serviceItem;
            
            serviceItem.addStyleName("turmeric-selectable");
            for (String s:e.getValue()) {
                Label opLabel = new Label(s);
                TreeItem opItem = new TreeItem(opLabel);
                opItem.addStyleName("turmeric-op");
                if (selectedOperation != null && selectedOperation.equals(s) && selectedService != null && selectedService.equals(e.getKey()))
                    selectedItem = opItem;
                
                opLabel.addMouseOverHandler(new MouseOverHandler() {

                    public void onMouseOver(MouseOverEvent event) {
                        ((Label)event.getSource()).getElement().setId("op-highlight");
                    }
                });
                opLabel.addMouseOutHandler(new MouseOutHandler() {
                    
                    public void onMouseOut(MouseOutEvent event) {
                        ((Label)event.getSource()).getElement().setId("");
                    }
                    
                });
                serviceItem.addItem(opItem);  
                
            }
            root.addItem(serviceItem);
        }
        serviceTree.addItem(root);
        if (selectedItem != null) {
            serviceTree.setSelectedItem(selectedItem, false);
            selectedItem.setState(true, false);
        }
        
        serviceTree.ensureSelectedItemVisible();
        root.setState(true, false);
    }
    
    /**
     * Sets the selection.
     *
     * @param service the service
     * @param operation the operation
     */
    public void setSelection (String service, String operation) {
        selectedService = service;
        selectedOperation = operation;

        if (serviceTree != null) {
            TreeItem selectedItem = null;
            if (service == null)
                selectedItem = root;
            else {
                Iterator<TreeItem> itor = serviceTree.treeItemIterator();
                while (selectedItem == null && itor.hasNext()) {
                    TreeItem item = itor.next();
                    if (item.getChildCount() > 0) {
                        //we're looking at a service, check if it matches
                        if (selectedService != null && selectedService.equals(item.getText()) && selectedOperation==null){
                            selectedItem = item;
                        }
                    } else {
                        //we're looking at an operation, check if service and operation match
                        if (selectedService != null && selectedService.equals(item.getParentItem().getText()) && selectedOperation != null && selectedOperation.equals(item.getText())) {
                            selectedItem = item;
                        }
                    }
                }
            }
            if (selectedItem != null) {
                serviceTree.setSelectedItem(selectedItem, false);
            }
            serviceTree.ensureSelectedItemVisible();
        }  
    }
}
