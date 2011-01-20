/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.Filterable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * ErrorFilterWidget
 *
 */
public class ErrorFilterWidget extends FilterWidget implements Filterable.ErrorFilterable{
    
    protected Label viewLabel;
    protected Grid viewGrid;
    protected Grid categoryGrid;
    protected Grid categoriesGrid;
    protected Grid severityGrid;
    protected Grid severitiesGrid;
    protected Label categoryLabel;
    protected Label severityLabel;
    
    public ErrorFilterWidget() {
        super();
        
    
        
    }
    
    public void createOthers () {
        
        viewLabel = new Label(ConsoleUtil.constants.viewErrorsBy()+": ");
        viewGrid = new Grid(1,2);

        categoryGrid = new Grid(2,1);
        categoryLabel = new Label(ConsoleUtil.constants.category()+": ");
        categoriesGrid = new Grid();
        
        categoryGrid.setWidget(0,0,categoryLabel);
        categoryGrid.setWidget(1,0,categoriesGrid);

        severityGrid = new Grid(2,1);
        severityLabel = new Label(ConsoleUtil.constants.severity()+": ");
        severitiesGrid = new Grid();
        severityGrid.setWidget (0,0,severityLabel);
        severityGrid.setWidget(1,0,severitiesGrid);

        viewGrid.setWidget(0,0, categoryGrid);
        viewGrid.setWidget(0,1, severityGrid);
    }
    
    public void position () {
        filterTable.clear();
       
        filterTable.setWidget(0, 0, date1Label);
        filterTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
        filterTable.getCellFormatter().setWordWrap(0, 0, false);
        filterTable.setWidget(0, 1, date1Grid);
        
        
        filterTable.setWidget(1, 0,date2Label);
        filterTable.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);
        filterTable.getCellFormatter().setWordWrap(1, 0, false);
        filterTable.setWidget(1, 1, date2Grid);

        filterTable.setWidget(2, 0, intervalLabel);
        filterTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);
        filterTable.getCellFormatter().setWordWrap(2, 0, false);
        filterTable.setWidget(2, 1, durationInput);
     
       
        filterTable.setWidget(3, 0, viewLabel);
        filterTable.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_LEFT);
        filterTable.getCellFormatter().setWordWrap(3, 0, false);
        filterTable.setWidget(3, 1, viewGrid);

        
        filterTable.setWidget(4, 0, advancedPanel);
        filterTable.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_LEFT);
        filterTable.getFlexCellFormatter().setColSpan(4, 0, 2);
    }
    
    public void createAdvancedPanel () {
        super.createAdvancedPanel();
        
       
    }

    public void setCategoryViewNames (List<String> names) {
        setGridValues(categoriesGrid, names, ConsoleUtil.constants.metricNameMap(), severitiesGrid);
    }

    public void setSelectedCategoryViewNames (List<String> names) {
        setSelectedGridValues(categoriesGrid, names, severitiesGrid);
    }

    public List<String> getSelectedCategoryViewNames() {
        return getSelectedGridValues(categoriesGrid);
    }

    
    public void setSeverityViewNames (List<String> names) {
        setGridValues(severitiesGrid, names, ConsoleUtil.constants.metricNameMap(), categoriesGrid);
    }
    
    public void setSelectedSeverityViewNames (List<String> names) {
        setSelectedGridValues(severitiesGrid, names, categoriesGrid);
    }
    
    public List<String> getSelectedSeverityViewNames () {

        return getSelectedGridValues(severitiesGrid);
    }

    public void disable (Grid g) {
        Iterator<Widget> itor = g.iterator();
        while (itor.hasNext()) {
            Widget w = itor.next();
            if (w instanceof CheckBox)
                ((CheckBox)w).setEnabled(false);
        }
    }
    
    public void enable (Grid g) {
        Iterator<Widget> itor = g.iterator();
        while (itor.hasNext()) {
            Widget w = itor.next();
            if (w instanceof CheckBox)
                ((CheckBox)w).setEnabled(true);
        }
    }
    
    protected void setGridValues (final Grid grid, final List<String> names, final Map localizedNameMap, final Grid counterpartGrid) {
        grid.clear();
        grid.resize(names.size(), 1);
        int i=0;
        for (String name:names) {
            CheckBox cb = new CheckBox((String)localizedNameMap.get(name));
            cb.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    //if a checkbox is selected, the counterpart Grid entries are all disabled
                    if (((CheckBox)event.getSource()).getValue())
                        disable(counterpartGrid);
                    else {
                        //if no checkboxes are selected, then the counterpart Grid is enabled
                        int checkCount = 0;
                        Iterator<Widget> itor = grid.iterator();
                        while (itor.hasNext()) {
                            Widget w = itor.next();
                            if (w instanceof CheckBox) {
                                if (((CheckBox)w).getValue())
                                    checkCount++;
                            }
                        }
                        if (checkCount == 0)
                            enable(counterpartGrid);
                    }
                }
            });
            cb.setFormValue(name);
            grid.setWidget(i, 0, cb);
            grid.getCellFormatter().setWordWrap(i, 0, false);
            i++;
        }
    }
    
    protected void setSelectedGridValues (Grid grid, List<String> names, final Grid counterpartGrid) {
        boolean checked = false;
        for (int i=0; i<grid.getRowCount(); i++) {
            Widget w = grid.getWidget(i,0);
            if (w instanceof CheckBox) {
                if (names.contains(((CheckBox)w).getFormValue())) {
                    ((CheckBox)w).setValue(true);
                    checked = true;
                }
                else
                    ((CheckBox)w).setValue(false);
            }
        }
        if (checked)
            disable(counterpartGrid);
    }
    
    protected List<String> getSelectedGridValues (Grid grid) {
        List<String> names = new ArrayList<String> ();
        for (int i=0; i<grid.getRowCount(); i++) {
            Widget w = grid.getWidget(i, 0);
            if (w instanceof CheckBox) {
                if (((CheckBox)w).getValue() && ((CheckBox)w).isEnabled())
                    names.add(((CheckBox)w).getFormValue());
            }
        }
        return names;
    }

   
}
