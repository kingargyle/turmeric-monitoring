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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.Dashboard;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorDetail;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorMetric;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorMetricData;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorTimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorViewData;
import org.ebayopensource.turmeric.monitoring.client.model.Filterable;
import org.ebayopensource.turmeric.monitoring.client.model.ObjectType;
import org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter;
import org.ebayopensource.turmeric.monitoring.client.util.GraphUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;



/**
 * ErrorView
 * 
 * Tab showing information about SOA errors.
 *
 * There are 6 pages in total:
 * 
 * Navigate to the Error tab with no specific error in context:
 * Select view by Category:
 * Category top level page contains tables/graphs:
 *   Top Application Errors
 *   Top Request Errors
 *   Top System Errors
 *   
 * Category drill down when selecting an hour on the trend graphs:
 *   Error Trend Graph
 *   Top Errors: (Category selected)
 *   
 * Severity top level page contains tables/graphs:
 *   Top Critical Errors
 *   Top Errors
 *   Top Warnings
 *   
 * Severity drill down when selecting an hour on the trend graphs:
 * Error Trend Graph
 * Top Errors: (Severity selected)
 * 
 * When a specific error is selected (from any page):
 * Error details and consumers experiencing the error & error-to-call-ratio for both dates
 */
public class ErrorView extends Composite implements ErrorPresenter.Display {
    private String id;
    private Label summaryHeading;
    private FilterWidget filterWidget;
    private SplitLayoutPanel splitPanel;
    private ServiceListWidget serviceListWidget;
    private Dashboard dashboard;
    private DialogBox filterDialog;
    private Panel tablesPanel;
    
    private Button filterButton;
    private String dateFormat = "dd MMM yyyy";
    
    private FlexTable topApplicationErrorsTable;
    private SummaryPanel topApplicationErrorsPanel;
    private FlexTable topRequestErrorsTable;
    private SummaryPanel topRequestErrorsPanel;
    private FlexTable topSystemErrorsTable;
    private SummaryPanel topSystemErrorsPanel;
    private FlexTable topCriticalsTable;
    private SummaryPanel topCriticalsPanel;
    private FlexTable topErrorsTable;
    private SummaryPanel topErrorsPanel;
    private FlexTable topWarningsTable;
    private SummaryPanel topWarningsPanel;
    private FlexTable consumerErrorsTable;
    private SummaryPanel consumerErrorsPanel;
    private FlexTable topSeverityErrorsTable;
    private SummaryPanel topSeverityErrorsPanel;
    private FlexTable topCategoryErrorsTable;
    private SummaryPanel topCategoryErrorsPanel;
    private SummaryPanel errorDetailPanel;
    private FlexTable errorDetailTable;
    
    
    /**
     * Instantiates a new error view.
     *
     * @param dashboard the dashboard
     */
    public ErrorView (Dashboard dashboard) {
        //make the panel for the contents of the tab
        DockLayoutPanel contentPanel = new DockLayoutPanel(Unit.EM);

        //heading
        Panel topPanel = new FlowPanel();
        topPanel.addStyleName("summary-heading-panel");
        Grid g = new Grid (1,2);
        summaryHeading = new Label(ConsoleUtil.constants.summary());
        summaryHeading.setWidth("50em");
        g.setWidget(0,0, summaryHeading);
        g.setHeight("100%");
        g.setWidth("100%");
        g.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
  
        topPanel.add(g);

        //filters: dates, times and metrics
        filterWidget = new ErrorFilterWidget();
        filterWidget.setDateFormat(dateFormat);

        filterButton = new Button("Filter Criteria >>");
        filterButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                pickFilter();
            }
        });
        
        g.setWidget(0,1,filterButton);
        g.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
     
        filterDialog = new DialogBox(true);
        filterDialog.setText(ConsoleUtil.constants.selectFilterCriteria());
        FlowPanel contents = new FlowPanel();
        filterDialog.setWidget(contents);
        contents.add(filterWidget);
        
        filterWidget.getApplyButton().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                filterDialog.hide(true);
            }
        });
        
        filterWidget.getCancelButton().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                filterDialog.hide(true);
            }
        });



        splitPanel = new SplitLayoutPanel();
        contentPanel.addNorth(topPanel, 2.5);
        contentPanel.add(splitPanel);

        //scrolling panel for right hand side
        ScrollPanel rhs = new ScrollPanel();
        rhs.setAlwaysShowScrollBars(true);
        rhs.addStyleName("summary-panel");

        //panel to contain each table/graph
        tablesPanel = new FlowPanel();

        topApplicationErrorsTable = makeTable();
        topApplicationErrorsPanel = makePanel(ConsoleUtil.constants.topApplicationErrors(), topApplicationErrorsTable);
        topRequestErrorsTable = makeTable();
        topRequestErrorsPanel = makePanel(ConsoleUtil.constants.topRequestErrors(), topRequestErrorsTable); 
        topSystemErrorsTable = makeTable();
        topSystemErrorsPanel = makePanel(ConsoleUtil.constants.topSystemErrors(), topSystemErrorsTable);
        topCriticalsTable = makeTable();
        topCriticalsPanel = makePanel(ConsoleUtil.constants.topCriticals(), topCriticalsTable);
        topErrorsTable = makeTable();
        topErrorsPanel = makePanel(ConsoleUtil.constants.topErrors(), topErrorsTable);
        topWarningsTable = makeTable();
        topWarningsPanel = makePanel(ConsoleUtil.constants.topWarnings(), topWarningsTable);
        errorDetailTable = makeTable();
        errorDetailPanel = makePanel("", errorDetailTable);
        consumerErrorsTable = makeTable();
        consumerErrorsPanel = makePanel(ConsoleUtil.constants.consumerError(), consumerErrorsTable);
        topSeverityErrorsTable = makeTable();
        topSeverityErrorsPanel = makePanel(ConsoleUtil.constants.topSeverityErrors(), topSeverityErrorsTable);
        topCategoryErrorsTable = makeTable();
        topCategoryErrorsPanel = makePanel(ConsoleUtil.constants.topCategoryErrors(), topCategoryErrorsTable);
        tablesPanel.add(topApplicationErrorsPanel);
        tablesPanel.add(topRequestErrorsPanel);
        tablesPanel.add(topSystemErrorsPanel);
        tablesPanel.add(topCriticalsPanel);
        tablesPanel.add(topErrorsPanel);
        tablesPanel.add(topWarningsPanel);
        tablesPanel.add(topSeverityErrorsPanel);
        tablesPanel.add(topCategoryErrorsPanel);
        tablesPanel.add(errorDetailPanel);
        tablesPanel.add(consumerErrorsPanel);
        hide(topApplicationErrorsPanel);
        hide(topRequestErrorsPanel);
        hide(topSeverityErrorsPanel);
        hide(topCriticalsPanel);
        hide(topErrorsPanel);
        hide(topWarningsPanel);
        hide(topSeverityErrorsPanel);
        hide(topCategoryErrorsPanel);
        hide(consumerErrorsPanel);
        hide(errorDetailPanel);

        rhs.add(tablesPanel);

        serviceListWidget = new ServiceListWidget();

        splitPanel.addWest(serviceListWidget, 250);
        splitPanel.add(rhs);

        initWidget(contentPanel);

        this.dashboard = dashboard;
        this.dashboard.addView(this, ConsoleUtil.constants.errors());
    }

    /**
     * As widget.
     *
     * @return the widget
     * @see com.google.gwt.user.client.ui.Widget#asWidget()
     */
    public Widget asWidget() {
        return this;
    }

    /**
     * Hide.
     *
     * @param o the o
     */
    public void hide (UIObject o) {
        o.setVisible(false);
    }

    /**
     * Show.
     *
     * @param o the o
     */
    public void show (UIObject o) {
        o.setVisible(true);
    }

    /**
     * View about to be shown, do any clean up necessary.
     *
     * @see org.ebayopensource.turmeric.monitoring.client.Display#activate()
     */
    public void activate () {
        dashboard.activate(this);
    }

    /**
     * Sets the filter label.
     *
     * @param filterString the new filter label
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#setFilterLabel(java.lang.String)
     */
    public void setFilterLabel (String filterString) {
        filterButton.setText(filterString);
    }

    /**
     * Sets the associated id.
     *
     * @param id the new associated id
     * @see org.ebayopensource.turmeric.monitoring.client.Display#setAssociatedId(java.lang.String)
     */
    public void setAssociatedId (String id) {
        this.id = id;
    }


    /**
     * Gets the associated id.
     *
     * @return the associated id
     * @see org.ebayopensource.turmeric.monitoring.client.Display#getAssociatedId()
     */
    public String getAssociatedId() {
        return this.id;
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#getFilter()
     */
    public Filterable getFilter () {
        return this.filterWidget;
    }
   
    /**
     * Error.
     *
     * @param msg the msg
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#error(java.lang.String)
     */
    public void error (String msg) {
        ErrorDialog dialog = new ErrorDialog(true);
        dialog.setMessage(msg);
        dialog.getDialog().center();
        dialog.show();
     }
    
    /**
     * Gets the selector.
     *
     * @return the selector
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#getSelector()
     */
    public HasSelectionHandlers<TreeItem> getSelector() {
        return serviceListWidget.getServiceTree();
    }

    /**
     * Reset.
     *
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#reset()
     */
    public void reset() {
        Iterator<Widget> itor = tablesPanel.iterator();
        while (itor.hasNext())
            hide(itor.next());
    }

    /**
     * Sets the services map.
     *
     * @param map the map
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#setServicesMap(java.util.Map)
     */
    public void setServicesMap(Map<String, Set<String>> map) {
        serviceListWidget.setServicesMap(map);
    }


    /**
     * Sets the error metric data.
     *
     * @param m the m
     * @param errorData the error data
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#setErrorMetricData(org.ebayopensource.turmeric.monitoring.client.model.ErrorMetric, org.ebayopensource.turmeric.monitoring.client.model.ErrorMetricData)
     */
    public void setErrorMetricData (ErrorMetric m, ErrorMetricData errorData) {
        SummaryPanel panel = null;
        
        switch (m) {
            case TopApplicationErrors: {
                panel = topApplicationErrorsPanel;
                setErrorTableData (topApplicationErrorsTable, errorData);
                break;
            }
            case TopRequestErrors: {
                panel = topRequestErrorsPanel;
                setErrorTableData (topRequestErrorsTable, errorData);
                break;
            }
            case TopSystemErrors: {
                panel = topSystemErrorsPanel;
                setErrorTableData (topSystemErrorsTable, errorData);
                break;
            }
            case TopCriticals: {
                panel = topCriticalsPanel;
                setErrorTableData (topCriticalsTable, errorData);
                break;
            }
            case TopErrors: {
                panel = topErrorsPanel;
                setErrorTableData (topErrorsTable, errorData);
                break;
            }
            case TopWarnings: { 
                panel = topWarningsPanel;
                setErrorTableData (topWarningsTable, errorData);
                break;
            }
            case ConsumerError: {
                //This is the case when details for a single error has been chosen to be shown
                panel = consumerErrorsPanel;
                setErrorDetailTableData(consumerErrorsTable, errorData);
                break;
            }
            case TopSeverityErrors: {
                //Drill down from Severity landing page
                panel = topSeverityErrorsPanel;
                setErrorTableData (topSeverityErrorsTable, errorData);
                break;
            }
            case TopCategoryErrors: {
                //Drill down from Category landing page
                panel = topCategoryErrorsPanel;
                setErrorTableData (topCategoryErrorsTable, errorData);
                break;
            }
        }
           
        if (panel != null)
            panel.setInfo(errorData.getRestUrl());
        show(panel);
    }
    
    /**
     * @param errorData
     */
    private void setErrorTableData(FlexTable table, ErrorMetricData errorData) {
        /* 
         * Columns:
         * error Id,
         * error Name,
         * Error Count:  date1, date2
         * Error to Call Ratio: date1, date2
         * */
        if (errorData == null)
            return;
        
        String d1 = formatDateAndDuration(errorData.getMetricCriteria().date1, errorData.getMetricCriteria().durationSec);
        String d2 = formatDateAndDuration(errorData.getMetricCriteria().date2, errorData.getMetricCriteria().durationSec);
        
        table.clear();
        table.removeAllRows();
        table.setWidget(0, 2, new Label(ConsoleUtil.constants.count()));
        table.getFlexCellFormatter().setColSpan(0, 2, 2);
        table.getRowFormatter().addStyleName(0, "tbl-header1");
        table.setWidget(0, 3, new Label(ConsoleUtil.constants.errorsToCalls()));
        table.getFlexCellFormatter().setColSpan(0, 3, 2);
        table.setWidget(1, 0, new Label(ConsoleUtil.constants.error()));
        table.setWidget(1, 1, new Label(ConsoleUtil.constants.name()));
        table.setWidget(1, 2, new Label (d1));
        table.setWidget(1, 3, new Label (d2));
        table.setWidget(1, 4, new Label (d1));
        table.setWidget(1, 5, new Label (d2));
        table.getRowFormatter().addStyleName(1, "tbl-header1");
        
        if (errorData.getReturnData() == null)
            return;
        int i = 2;
        for (ErrorViewData evd:errorData.getReturnData()) {
            Label id = new Label(evd.getErrorId());
            id.addStyleName("clickable");
            table.setWidget(i, 0, id);
            Label name = new Label(evd.getErrorName());
            name.addStyleName("clickable");
            table.setWidget(i, 1, name);
            try {
                table.setWidget(i, 2, new Label(NumberFormat.getDecimalFormat().format(Long.valueOf(evd.getErrorCount1()))));
            } catch (NumberFormatException e) {
                table.setWidget(i, 2, new Label (ConsoleUtil.constants.error()));
            }
            try {
                table.setWidget(i, 3, new Label(NumberFormat.getDecimalFormat().format(Long.valueOf(evd.getErrorCount2()))));
            } catch (NumberFormatException e) {
                table.setWidget(i, 3, new Label(ConsoleUtil.constants.error()));
            }
            try {
                table.setWidget(i, 4, new Label(NumberFormat.getDecimalFormat().format(Double.valueOf(evd.getErrorCallRatio1()))));
            } catch (NumberFormatException e) {
                table.setWidget(i, 4, new Label(ConsoleUtil.constants.error()));
            }
            try {
                table.setWidget(i, 5, new Label(NumberFormat.getDecimalFormat().format(Double.valueOf(evd.getErrorCallRatio2()))));
            } catch (NumberFormatException e) {
                table.setWidget(i, 5, new Label(ConsoleUtil.constants.error()));
            }
            i++;
        }
    }
    
    private void setErrorDetailTableData (FlexTable table, ErrorMetricData errorData) {
        /* 
         * Columns:
         * Consumer Name,
         * Error Count:  date1, date2
         * Error to Call Ratio: date1, date2
         */
        if (errorData == null)
            return;
        
        String d1 = formatDateAndDuration(errorData.getMetricCriteria().date1, errorData.getMetricCriteria().durationSec);
        String d2 = formatDateAndDuration(errorData.getMetricCriteria().date2, errorData.getMetricCriteria().durationSec);
        table.clear();
        table.setWidget(0, 1, new Label(ConsoleUtil.constants.count()));
        table.getFlexCellFormatter().setColSpan(0, 1, 2);
        table.setWidget(0, 2, new Label(ConsoleUtil.constants.errorsToCalls()));
        table.getRowFormatter().addStyleName(0, "tbl-header1");
        table.getFlexCellFormatter().setColSpan(0, 2, 2);
        table.setWidget(1, 0, new Label(ConsoleUtil.constants.consumers()));
        table.setWidget(1, 1, new Label (d1));
        table.setWidget(1, 2, new Label (d2));
        table.setWidget(1, 3, new Label (d1));
        table.setWidget(1, 4, new Label (d2));
        table.getRowFormatter().addStyleName(1, "tbl-header1");
        if (errorData.getReturnData() == null)
            return;
        
        int i=2;
        for (ErrorViewData evd:errorData.getReturnData()) {
            Label str = new Label(evd.getConsumer());
            str.addStyleName("clickable");
            table.setWidget(i,0,str);

            try {
                table.setWidget(i, 1, new Label(NumberFormat.getDecimalFormat().format(evd.getErrorCount1())));
            } catch (NumberFormatException e) {
                table.setWidget(i, 1, new Label(ConsoleUtil.constants.error()));
            }

            try {
                table.setWidget(i, 2, new Label(NumberFormat.getDecimalFormat().format(evd.getErrorCount2())));
            } catch (NumberFormatException e) {
                table.setWidget(i, 2, new Label(ConsoleUtil.constants.error()));
            }

            try {
                table.setWidget(i, 3, new Label(NumberFormat.getDecimalFormat().format(evd.getErrorCallRatio1())));
            } catch (NumberFormatException e) {
                table.setWidget(i, 3, new Label(ConsoleUtil.constants.error()));
            }
            try {
                table.setWidget(i, 4, new Label(NumberFormat.getDecimalFormat().format(evd.getErrorCallRatio2())));
            } catch (NumberFormatException e) {
                table.setWidget(i, 4, new Label(ConsoleUtil.constants.error()));
            }
            i++;
        }
    }

    /**
     * Sets the error detail.
     *
     * @param ed the new error detail
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#setErrorDetail(org.ebayopensource.turmeric.monitoring.client.model.ErrorDetail)
     */
    public void setErrorDetail (ErrorDetail ed) {
        if (ed == null)
            return;
        /*
        errorDetailTable.clear();
        errorDetailTable.setWidget(0,0,new Label("Id: "+ed.getId()));
        errorDetailTable.setWidget(0,1,new Label(ConsoleUtil.constants.name()+": "+ed.getName()));
        errorDetailTable.setWidget(0,2, new Label(ConsoleUtil.constants.severity()+": "+ed.getSeverity()));
        errorDetailTable.setWidget(0,3, new Label(ConsoleUtil.constants.category()+": "+ed.getCategory()));
        errorDetailTable.setWidget(0,4, new Label(ConsoleUtil.constants.domain()+": "+ed.getDomain()));
        errorDetailTable.setWidget(0,4, new Label(ConsoleUtil.constants.subdomain()+": "+ed.getSubDomain()));
        show (errorDetailPanel);
        */
        String heading = summaryHeading.getText();
        heading += " [Id: "+ed.getId()+", ";
        heading += ConsoleUtil.constants.severity()+": "+ed.getSeverity()+", ";
        heading += ConsoleUtil.constants.category()+": "+ed.getCategory()+", ";
        heading += ConsoleUtil.constants.domain()+": "+ed.getDomain()+", ";
        heading += ConsoleUtil.constants.subdomain()+": "+ed.getSubDomain() +"]";
        summaryHeading.setText(heading);
    }
    
 

    /**
     * Sets the selection.
     *
     * @param selections the selections
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#setSelection(java.util.Map)
     */
    public void setSelection(Map<ObjectType,String> selections) {
        String s = "";
        if (selections != null) {
            String service = selections.get(ObjectType.ServiceName);
            String operation = selections.get(ObjectType.OperationName);
            String consumer = selections.get(ObjectType.ConsumerName);
            String errId = selections.get(ObjectType.ErrorId);
            String errName = selections.get(ObjectType.ErrorName);

            if (errId != null || errName != null) {
                s = s + (errId != null?errId:errName);
            } else {

                if (service != null)
                    s = s + " "+service;
                if (operation != null) 
                    s = s + " : "+operation;
                if (consumer != null)
                    s = s+ " : "+consumer;
            }
            serviceListWidget.setSelection(service, operation);
        } else
            serviceListWidget.setSelection(null, null);
        if ("".equals(s))
            s = ConsoleUtil.constants.summary();
        summaryHeading.setText(s); 
    }
    
    
    /**
     * Pick filter.
     */
    public void pickFilter () {
        int x = filterButton.getAbsoluteLeft();
        int y = filterButton.getAbsoluteTop() + filterButton.getOffsetHeight();
        //filterDialog.setAnimationEnabled(true);
        filterDialog.setGlassEnabled(true);
        filterDialog.setPopupPosition(x, y);
        filterDialog.show();
    }
  
 

    /**
     * Gets the table column.
     *
     * @param m the m
     * @param col the col
     * @return the table column
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#getTableColumn(org.ebayopensource.turmeric.monitoring.client.model.ErrorMetric, int)
     */
    public List<HasClickHandlers> getTableColumn(ErrorMetric m, int col) {
        List<HasClickHandlers> list = new ArrayList<HasClickHandlers>();
        FlexTable t = getTable(m);
        if (t == null)
            return list;
        for (int i=2;i<t.getRowCount();i++) {
            Widget w = t.getWidget(i, col);
            if (w instanceof HasClickHandlers)
                list.add((HasClickHandlers)w);
        }
        return list;
    }
    
    /**
     * Sets the download url.
     *
     * @param m the m
     * @param url the url
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#setDownloadUrl(org.ebayopensource.turmeric.monitoring.client.model.ErrorMetric, java.lang.String)
     */
    public void setDownloadUrl (ErrorMetric m, String url) {
        SummaryPanel p = getSummaryPanel(m);
        if (p != null)
            p.setDownloadUrl(url);
    }
        
    /**
     * Gets the table.
     *
     * @param m the m
     * @return the table
     */
    public FlexTable getTable (ErrorMetric m) {
        FlexTable table = null;
        switch (m) {
            case TopApplicationErrors: {
                table = topApplicationErrorsTable;
                break;
            }
            case TopRequestErrors: {
                table = topRequestErrorsTable;
                break;
            }
            case TopSystemErrors: {
                table = topSystemErrorsTable;
                break;
            }
            case TopCriticals: {
                table = topCriticalsTable;
                break;
            }
            case TopErrors: {
               table = topErrorsTable;
                break;
            }
            case TopWarnings: { 
                table = topWarningsTable;
                break;
            }
            case ConsumerError: {
                //This is the case when details for a single error has been chosen to be shown
                table = consumerErrorsTable;
                break;
            }
            case TopSeverityErrors: {
                //Drill down from Severity landing page
               table = topSeverityErrorsTable;
                break;
            }
            case TopCategoryErrors: {
                //Drill down from Category landing page
                table = topCategoryErrorsTable;
                break;
            }
        }
        return table;
    }
    
    
    
    /**
     * Gets the summary panel.
     *
     * @param metric the metric
     * @return the summary panel
     */
    public SummaryPanel getSummaryPanel(ErrorMetric metric) {
        SummaryPanel panel = null;
        
        switch (metric) {
            case TopApplicationErrors: {
                panel = topApplicationErrorsPanel;
                break;
            }
            case TopRequestErrors: {
                panel = topRequestErrorsPanel;
                break;
            }
            case TopSystemErrors: {
                panel = topSystemErrorsPanel;
                break;
            }
            case TopCriticals: {
                panel = topCriticalsPanel;
                break;
            }
            case TopErrors: {
               panel = topErrorsPanel;
                break;
            }
            case TopWarnings: { 
                panel = topWarningsPanel;
                break;
            }
            case ConsumerError: {
                //This is the case when details for a single error has been chosen to be shown
                panel = consumerErrorsPanel;
                break;
            }
            case TopSeverityErrors: {
                //Drill down from Severity landing page
               panel = topSeverityErrorsPanel;
                break;
            }
            case TopCategoryErrors: {
                //Drill down from Category landing page
                panel = topCategoryErrorsPanel;
                break;
            }
        }
        return panel;
    }

    /**
     * Make panel.
     *
     * @param heading the heading
     * @param contents the contents
     * @return the summary panel
     */
    protected SummaryPanel makePanel (String heading, Widget contents) {
        SummaryPanel panel = new SummaryPanel();
        panel.setHeading(heading);
        panel.setContents(contents);
        return panel;
    }
    
    /**
     * Make table.
     *
     * @return the flex table
     */
    protected FlexTable makeTable() {
        FlexTable table = new FlexTable();
        table.addStyleName("tbl");
        table.setWidth("100%");
        return table;
    }

    private String formatDateAndDuration (long ts, long durationSec) {
        String str = ConsoleUtil.timeFormat.format(new Date(ts));
        str += " + "+(durationSec/(60*60))+ConsoleUtil.constants.hr();
        return str;
    }

    /**
     * Sets the service system error trend data.
     *
     * @param dataRanges the data ranges
     * @param aggregationPeriod the aggregation period
     * @param hourSpan the hour span
     * @param graphTitle the graph title
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#setServiceSystemErrorTrendData(java.util.List, long, int, java.lang.String)
     */
    @Override
    public void setServiceSystemErrorTrendData(List<ErrorTimeSlotData> dataRanges, long aggregationPeriod, int hourSpan,  String graphTitle) {
        if (dataRanges.get(0).getReturnData() != null && dataRanges.get(1).getReturnData() != null) {
            GraphUtil.createErrorLineChart(this.topSystemErrorsPanel, dataRanges, aggregationPeriod,  hourSpan,  graphTitle);
        }
        else {
            GWT.log("empty graphData");
        }
    }

//    private void createLineChart(final SummaryPanel panel, final List<ErrorTimeSlotData> dataRanges, final String graphTitle) {
//        Runnable onLoadCallback = new Runnable() {
//            public void run() {
//                final LineChart lineChart = new LineChart(createChartDataTable(dataRanges), createOptions(graphTitle));
//                panel.addChart(lineChart);
//            }
//        };
//
//        //Load the visualization api, passing the onLoadCallback to be called when loading is done.
//        //The gwt param "corechart" tells gwt to use the new charts
//        
//        VisualizationUtils.loadVisualizationApi(onLoadCallback, "corechart");
//    }

//    protected Options createOptions(String graphTitle) {
//        Options options = Options.create();
//        //options.setWidth(600);
//        options.setHeight(230);
//        options.setEnableTooltip(true);
//        options.setShowCategories(true);
//        options.set("fontSize", 10d);
//        options.setSmoothLine(true);
//        options.setPointSize(3);
//        options.setLineSize(3);
//        options.setTitle(graphTitle);
//        options.setTitleFontSize(12d);
//        return options;
//    }

    /**
 * Creates the chart data table.
 *
 * @param dataRanges the data ranges
 * @return the abstract data table
 */
protected AbstractDataTable createChartDataTable(List<ErrorTimeSlotData> dataRanges) {
        DataTable data = DataTable.create();
        ErrorTimeSlotData firstDateRange = dataRanges.get(0);
        ErrorTimeSlotData secondDateRange = dataRanges.get(1);
        if (firstDateRange.getReturnData() != null && secondDateRange.getReturnData() != null) {
            int rowSize = firstDateRange.getReturnData() != null ? firstDateRange.getReturnData().size() : 0;

            if (rowSize > 0) {
                data.addColumn(ColumnType.STRING, "x");
                data.addColumn(ColumnType.NUMBER,
                                ConsoleUtil.shotTimeFormat.format(new Date(firstDateRange.getReturnData().get(0)
                                                .getTimeSlot())));

                data.addColumn(ColumnType.NUMBER,
                                ConsoleUtil.shotTimeFormat.format(new Date(secondDateRange.getReturnData().get(0)
                                                .getTimeSlot())));
                data.addRows(rowSize);
                for (int i = 0; i < rowSize; i++) {
                    // GWT.log("getValue = "+timeData.getReturnData().get(i).getValue());
                    data.setValue(i,
                                    0,
                                    ConsoleUtil.onlyTimeFormat.format(new Date(firstDateRange.getReturnData().get(i)
                                                    .getTimeSlot())));
                    data.setValue(i, 1, firstDateRange.getReturnData().get(i).getValue());
                    data.setValue(i, 2, secondDateRange.getReturnData().get(i).getValue());
                }
            }
            else {
                data.addColumn(ColumnType.STRING, "x");
                data.addColumn(ColumnType.NUMBER, "");
                data.addColumn(ColumnType.NUMBER, "");
                data.addRows(rowSize);
            }
        }
        return data;
    }

    /**
     * Sets the service application error trend data.
     *
     * @param dataRanges the data ranges
     * @param aggregationPeriod the aggregation period
     * @param hourSpan the hour span
     * @param graphTitle the graph title
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#setServiceApplicationErrorTrendData(java.util.List, long, int, java.lang.String)
     */
    @Override
    public void setServiceApplicationErrorTrendData(List<ErrorTimeSlotData> dataRanges, long aggregationPeriod, int hourSpan,  String graphTitle) {
        if (dataRanges.get(0).getReturnData() != null && dataRanges.get(1).getReturnData() != null) {
            GraphUtil.createErrorLineChart(this.topApplicationErrorsPanel, dataRanges,aggregationPeriod, hourSpan,   graphTitle);
        }
        else {
            GWT.log("empty graphData");
        }
    }

    /**
     * Sets the service request error trend data.
     *
     * @param dataRanges the data ranges
     * @param aggregationPeriod the aggregation period
     * @param hourSpan the hour span
     * @param graphTitle the graph title
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter.Display#setServiceRequestErrorTrendData(java.util.List, long, int, java.lang.String)
     */
    @Override
    public void setServiceRequestErrorTrendData(List<ErrorTimeSlotData> dataRanges, long aggregationPeriod, int hourSpan,   String graphTitle) {
        if (dataRanges.get(0).getReturnData() != null && dataRanges.get(1).getReturnData() != null) {
            GraphUtil.createErrorLineChart(this.topRequestErrorsPanel, dataRanges,  aggregationPeriod,  hourSpan,  graphTitle);
        }
        else {
            GWT.log("empty graphData");
        }
    }

    @Override
    public void addValueChangeHandlerForDate1(ValueChangeHandler<Date> valueChangeHandler) {
        getFilter().getDate1().addValueChangeHandler(valueChangeHandler);
    }

    @Override
    public void addValueChangeHandlerForDate2(ValueChangeHandler<Date> valueChangeHandler) {
        getFilter().getDate2().addValueChangeHandler(valueChangeHandler);
    }

    @Override
    public void addFilterOptionsApplyClickHandler(ClickHandler handler) {
        getFilter().getApplyButton().addClickHandler(handler) ;
    }

    @Override
    public void addTreeElementSelectionHandler(SelectionHandler<TreeItem> selectionHandler) {
        getSelector().addSelectionHandler(selectionHandler);
    }

}
