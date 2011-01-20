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
import org.ebayopensource.turmeric.monitoring.client.model.ErrorViewData;
import org.ebayopensource.turmeric.monitoring.client.model.Filterable;
import org.ebayopensource.turmeric.monitoring.client.model.ObjectType;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorMetricData;
import org.ebayopensource.turmeric.monitoring.client.presenter.ErrorPresenter;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;



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

    public Widget asWidget() {
        return this;
    }

    public void hide (UIObject o) {
        o.setVisible(false);
    }

    public void show (UIObject o) {
        o.setVisible(true);
    }

    /**
     * View about to be shown, do any clean up necessary
     * @see org.ebayopensource.turmeric.monitoring.client.Display#activate()
     */
    public void activate () {
        dashboard.activate(this);
    }

    public void setFilterLabel (String filterString) {
        filterButton.setText(filterString);
    }

    public void setAssociatedId (String id) {
        this.id = id;
    }


    public String getAssociatedId() {
        return this.id;
    }

    public Filterable getFilter () {
        return this.filterWidget;
    }
   
    public void error (String msg) {
        ErrorDialog dialog = new ErrorDialog(true);
        dialog.setMessage(msg);
        dialog.getDialog().center();
        dialog.show();
     }
    
    public HasSelectionHandlers<TreeItem> getSelector() {
        return serviceListWidget.getServiceTree();
    }

    public void reset() {
        Iterator<Widget> itor = tablesPanel.iterator();
        while (itor.hasNext())
            hide(itor.next());
    }

    public void setServicesMap(Map<String, Set<String>> map) {
        serviceListWidget.setServicesMap(map);
    }


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
    
    
    public void pickFilter () {
        int x = filterButton.getAbsoluteLeft();
        int y = filterButton.getAbsoluteTop() + filterButton.getOffsetHeight();
        //filterDialog.setAnimationEnabled(true);
        filterDialog.setGlassEnabled(true);
        filterDialog.setPopupPosition(x, y);
        filterDialog.show();
    }
  
 

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
    
    public void setDownloadUrl (ErrorMetric m, String url) {
        SummaryPanel p = getSummaryPanel(m);
        if (p != null)
            p.setDownloadUrl(url);
    }
        
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

    protected SummaryPanel makePanel (String heading, Widget contents) {
        SummaryPanel panel = new SummaryPanel();
        panel.setHeading(heading);
        panel.setContents(contents);
        return panel;
    }
    
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

}
