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
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.Filterable;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class FilterWidget extends Composite implements Filterable {
    
    protected Panel panel;
    protected Label date1Label;
    protected Label date2Label;
    protected DateBox date1Input;
    protected DateBox date2Input;
    protected Grid date1Grid;
    protected ListBox hourInput1;
    protected ListBox hourInput2;
    protected Grid date2Grid;
    protected Label intervalLabel;
    protected ListBox durationInput;
    protected DateBox.DefaultFormat dateFormat;
    protected Label metricsLabel;
    protected Grid metricsGrid;
    protected Button applyButton;
    protected Button cancelButton;
    protected FlexTable filterTable;
    protected DisclosurePanel advancedPanel;
    protected Panel advancedPanelContent;
    
    public FilterWidget() {
        panel = new FlowPanel();
        panel.addStyleName("date-select-panel");
        
        filterTable = new FlexTable();

        createDate1Widgets();
        createDate2Widgets();    
        createDurationWidgets();
        createMetricsWidgets();       
        createAdvancedPanel();
        createOthers();
        position();
        
        applyButton = new Button(ConsoleUtil.constants.apply());
        cancelButton = new Button (ConsoleUtil.constants.cancel());

        panel.add(filterTable);
        panel.add(cancelButton);
        panel.add(applyButton);
        initWidget(panel);
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
        
        filterTable.setWidget(3, 0, metricsLabel);
        filterTable.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_LEFT);
        filterTable.getCellFormatter().setWordWrap(3, 0, false);
        filterTable.setWidget(3, 1, metricsGrid);
        filterTable.getCellFormatter().setVerticalAlignment(3, 0, HasVerticalAlignment.ALIGN_TOP);
        
        filterTable.setWidget(4, 0, advancedPanel);
        filterTable.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_LEFT);
        filterTable.getFlexCellFormatter().setColSpan(4, 0, 2);
    }
    
    public void createOthers() {
    }
    
    public void createAdvancedPanel () {
        advancedPanel = new DisclosurePanel(ConsoleUtil.constants.more());
        advancedPanelContent = new FlowPanel();
        advancedPanel.setContent(advancedPanelContent);
    }
    
    public void createMetricsWidgets() {
        metricsLabel = new Label(ConsoleUtil.constants.selectMetrics()+": ");
        metricsGrid = new Grid(2,1);
    }
    /**
     * 
     */
    public void createDurationWidgets() {
        intervalLabel = new Label(ConsoleUtil.constants.over()+": ");
        intervalLabel.addStyleName("date-select-panel-item");
        durationInput = new ListBox(false);
        durationInput.addStyleName("date-select-panel-item");
    }

    public void createDate1Widgets () {
        
        date1Label = new Label(ConsoleUtil.constants.firstDate()+": ");
        date1Label.addStyleName("date-select-panel-item");
        
        date1Input = new DateBox();
        date1Input.addStyleName("date-select-panel-item");
        date1Input.getDatePicker().addStyleName("turmeric");
        hourInput1 = new ListBox(false);
        hourInput1.addStyleName("date-select-panel-item");
         date1Grid = new Grid(1,2);
        date1Grid.setWidget(0,0,date1Input);
        date1Grid.setWidget(0,1,hourInput1);
    }

    public void createDate2Widgets () {
        date2Label = new Label(ConsoleUtil.constants.secondDate()+": ");
        date2Label.addStyleName("date-select-panel-item");
        date2Input = new DateBox();
        date2Input.addStyleName("date-select-panel-item");
        date2Input.getDatePicker().addStyleName("turmeric");
        hourInput2 = new ListBox(false);
        hourInput2.addStyleName("date-select-panel-item"); 
         date2Grid = new Grid(1,2);
        date2Grid.setWidget(0,0,date2Input);
        date2Grid.setWidget(0, 1, hourInput2);
    }
    
    public void disable () {
       
        date1Input.setEnabled(false);
        date2Input.setEnabled(false);
        hourInput1.setEnabled(false);
        hourInput2.setEnabled(false);
    }
    
    public void enable () {
       
        date1Input.setEnabled(true);
        date2Input.setEnabled(true);
        hourInput1.setEnabled(true);
        hourInput2.setEnabled(true);
    }
    
    public void setDateFormat (String format) {
        dateFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat(format));
        date1Input.setFormat(dateFormat);
        date2Input.setFormat(dateFormat);
    }
    
    public void setDate1Label (String text) {
        date1Label.setText(text);
    }
    
    public void setDate2Label (String text) {
        date2Label.setText(text);
    }

    public HasClickHandlers getApplyButton () {
        return applyButton;
    }
    
    public HasClickHandlers getCancelButton () {
        return cancelButton;
    }
    
    public HasValue<Date> getDate1 () {
        return date1Input;
    }
    
    public HasValue<Date> getDate2 () {
        return date2Input;
    }
    
    public void setDate1(Date d) {
        date1Input.setValue(d);
    }

    public void setDate2(Date d) {
        date2Input.setValue(d);
    }

    public void setHour1 (int hour) {
        if (hour < hourInput1.getItemCount())
            hourInput1.setItemSelected(hour, true);
    }

    public void setHour2 (int hour) {
        if (hour < hourInput2.getItemCount())
            hourInput2.setItemSelected(hour, true);
    }
    public int getHour1 () {
        return hourInput1.getSelectedIndex();
    }

    public int getHour2 () {
        return hourInput2.getSelectedIndex();
    }
    
    public int getDuration () {
        return durationInput.getSelectedIndex()+1;
    }
    
    public void setDuration (int duration) {
        if (duration >1 && duration < durationInput.getItemCount())
        durationInput.setItemSelected(duration-1, true);
    }
    
    public void setHours1 (int[] hours) {
        hourInput1.clear();
        for (int i=0;i<hours.length;i++) {
            hourInput1.addItem(String.valueOf(hours[i])+":00");
        }
    }
    
    public void setHours2(int[] hours) {
        hourInput2.clear();
        for (int i=0;i<hours.length;i++) {
            hourInput2.addItem(String.valueOf(hours[i])+":00");
        }
    }
    
    public void setDurations(int[] durations) {
        durationInput.clear();
        for (int i=0;i<durations.length;i++) {
            durationInput.addItem(String.valueOf(durations[i])+ConsoleUtil.constants.hr());
        }
    }
    
    public void setMetricNames (List<String> names) {
        metricsGrid.clear();
        metricsGrid.resize(names.size(), 1);
        int i=0;
        for (String name:names) {
            String localizedName = (String)ConsoleUtil.constants.metricNameMap().get(name);
            CheckBox checkbox = new CheckBox(localizedName);
            checkbox.setFormValue(name);
            metricsGrid.setWidget(i++, 0, checkbox);
        }
    }
    
    
    public List<String> getSelectedMetricNames() {
        List<String> names = new ArrayList<String> ();
        for (int i=0; i<metricsGrid.getRowCount(); i++) {
            Widget w = metricsGrid.getWidget(i, 0);
            if (w instanceof CheckBox) {
                if (((CheckBox)w).getValue())
                    names.add(((CheckBox)w).getFormValue());
            }
        }
        return names;
    }
    
    public void setSelectedMetricNames (List<String> names) {
        for (int i=0; i<metricsGrid.getRowCount(); i++) {
            Widget w = metricsGrid.getWidget(i,0);
            if (w instanceof CheckBox) {
                if (names.contains(((CheckBox)w).getFormValue()))
                    ((CheckBox)w).setValue(true);
                else
                    ((CheckBox)w).setValue(false);
            }
        }
    }
}
