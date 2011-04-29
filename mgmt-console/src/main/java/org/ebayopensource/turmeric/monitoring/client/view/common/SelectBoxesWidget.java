/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * SelectBoxesWidget.
 */
public class SelectBoxesWidget extends Composite {
    private Panel panel;
    private ListBox availableBox;
    private ListBox selectedBox;
    
    /** The grid. */
    protected Grid grid;
    
    /** The add button. */
    protected Button addButton;
    
    /** The del button. */
    protected Button delButton;
    
    /** The available label. */
    protected Label availableLabel;
    
    /** The selected label. */
    protected Label selectedLabel;
    
    
    /**
     * Instantiates a new select boxes widget.
     *
     * @param availableName the available name
     * @param isAvailableMulti the is available multi
     * @param selectedName the selected name
     * @param isSelectedMulti the is selected multi
     */
    public SelectBoxesWidget (String availableName, boolean isAvailableMulti, String selectedName, boolean isSelectedMulti) {
        panel = new SimplePanel();
        availableLabel = new Label(availableName);
        selectedLabel = new Label(selectedName);
        
        availableBox = new ListBox(isAvailableMulti);
        selectedBox = new ListBox(isSelectedMulti);
       
 
        //arrows
        addButton = new Button(">>");
        delButton = new Button("<<");
        
        addButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                move(availableBox, selectedBox);
            }  
        });
        
        delButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
               move(selectedBox, availableBox);
            }  
        });
        
        grid = new Grid(2,3);
        grid.setWidget(0, 0, availableLabel);
        grid.setWidget(0, 2, selectedLabel);
        grid.setWidget(1, 0, availableBox);
        grid.getCellFormatter().setHorizontalAlignment(1,0, HasHorizontalAlignment.ALIGN_CENTER);
        Grid arrowGrid = new Grid(2,1);
        arrowGrid.setWidget(0,0,addButton);
        arrowGrid.setWidget(1,0,delButton);
        grid.setWidget(1, 1, arrowGrid);
        grid.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_MIDDLE);
        grid.getCellFormatter().setHorizontalAlignment(1,1, HasHorizontalAlignment.ALIGN_CENTER);
        grid.setWidget(1, 2, selectedBox);
        grid.getCellFormatter().setHorizontalAlignment(1,2, HasHorizontalAlignment.ALIGN_CENTER);
        panel.add(grid);
        initWidget(panel);
    }
    

    /**
     * Gets the selections.
     *
     * @return the selections
     */
    public List<String> getSelections () {
        List<String> selectedSubjects = new ArrayList<String>();
        for (int i=0; i<selectedBox.getItemCount();i++)
            selectedSubjects.add(selectedBox.getItemText(i));

        return selectedSubjects;
    }
    
    /**
     * Gets the availables.
     *
     * @return the availables
     */
    public List<String> getAvailables () {
        List<String> availables = new ArrayList<String>();
        for (int i=0; i<availableBox.getItemCount();i++)
            availables.add(availableBox.getItemText(i));

        return availables;
    }
    
    
    /**
     * Sets the availables.
     *
     * @param availables the new availables
     */
    public void setAvailables (List<String> availables) {
        availableBox.clear();
        if (availables != null && availables.size() >0) {
            for (String s:availables) {
                if (! contains(selectedBox, s))
                    availableBox.addItem(s);
            }
        }
    }
    
    /**
     * Sets the selections.
     *
     * @param selects the new selections
     */
    public void setSelections (List<String> selects) {
        selectedBox.clear();
        if (selects != null && selects.size() > 0)
            for (String s:selects) {
                if (! contains(availableBox, s))
                    selectedBox.addItem(s);
            }
    }
    
    /**
     * Contains.
     *
     * @param box the box
     * @param string the string
     * @return true, if successful
     */
    protected boolean contains (ListBox box, String string) {
        if (box == null)
            return false;
        if (string == null)
            return false;
        
        boolean result = false;
        for (int i=0; (i< box.getItemCount()) && !result; i++) {
            result = string.equals(box.getItemText(i));
        }
        return result;
    }
    
    /**
     * Move.
     *
     * @param from the from
     * @param to the to
     */
    protected void move (ListBox from, ListBox to) {
        int count = from.getItemCount();
        Map<Integer, String> selections = new HashMap<Integer, String>();
        
        for (int i=0;i<count;i++)
            if (from.isItemSelected(i))
                selections.put(new Integer(i), from.getItemText(i));
        
        for (Map.Entry<Integer, String> entry:selections.entrySet()) {
            to.addItem(entry.getValue());
            boolean removed = false;
            for (int i=0;i<count && !removed;i++) {
                if (entry.getValue().equals(from.getItemText(i))) {
                    from.removeItem(i);
                    removed = true;
                }
            }
        } 
    }
}
