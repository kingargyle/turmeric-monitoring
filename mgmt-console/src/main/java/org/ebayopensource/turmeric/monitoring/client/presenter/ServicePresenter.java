/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.Dashboard;
import org.ebayopensource.turmeric.monitoring.client.Util;
import org.ebayopensource.turmeric.monitoring.client.event.DateFilterSelectionEvent;
import org.ebayopensource.turmeric.monitoring.client.event.GetServicesEvent;
import org.ebayopensource.turmeric.monitoring.client.event.ObjectSelectionEvent;
import org.ebayopensource.turmeric.monitoring.client.event.ObjectSelectionEventHandler;
import org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfoImpl;
import org.ebayopensource.turmeric.monitoring.client.model.FilterContext;
import org.ebayopensource.turmeric.monitoring.client.model.Filterable;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.client.model.MetricData;
import org.ebayopensource.turmeric.monitoring.client.model.MetricResourceCriteria;
import org.ebayopensource.turmeric.monitoring.client.model.MetricValue;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Entity;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.EntityName;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Ordering;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Perspective;
import org.ebayopensource.turmeric.monitoring.client.model.ObjectType;
import org.ebayopensource.turmeric.monitoring.client.model.SelectionContext;
import org.ebayopensource.turmeric.monitoring.client.model.ServiceMetric;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.util.GraphUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.logging.client.HasWidgetsLogHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * The Class ServicePresenter.
 */
public class ServicePresenter implements Presenter.TabPresenter {
    private static Logger errorLogger = Logger.getLogger("errorLogger");

    
    /** The Constant DEFAULT_SERVICE. */
    public final static String DEFAULT_SERVICE = "FindingService";

    /** The Constant SERVICE_ID. */
    public final static String SERVICE_ID = "Service";

    /** The view. */
    protected Display view;

    /** The event bus. */
    protected HandlerManager eventBus;

    /** The query service. */
    protected MetricsQueryService queryService;

    /** The selection context. */
    protected SelectionContext selectionContext;

    /** The selected date1. */
    protected long selectedDate1;

    /** The selected date2. */
    protected long selectedDate2;

    /** The selected duration hrs. */
    protected int selectedDurationHrs;

    /** The services list. */
    protected Map<String, Set<String>> servicesList;

    /** The selected metrics. */
    protected List<ServiceMetric> selectedMetrics = new ArrayList<ServiceMetric>(Arrays.asList(ServiceMetric.values()));

    /** The min aggregation period. */
    protected long minAggregationPeriod = MetricCriteria.minAggregationPeriod;

    /**
     * The Interface Display.
     */
    public interface Display extends org.ebayopensource.turmeric.monitoring.client.Display {

        /**
         * Error.
         * 
         * @param error
         *            the error
         */
        public void error(String error);

        /**
         * Sets the services map.
         * 
         * @param map
         *            the map
         */
        public void setServicesMap(Map<String, Set<String>> map);

        /**
         * Sets the metric.
         * 
         * @param metric
         *            the metric
         * @param m
         *            the m
         */
        public void setMetric(ServiceMetric metric, MetricData m);

        /**
         * Gets the selector.
         * 
         * @return the selector
         */
        public HasSelectionHandlers<TreeItem> getSelector();

        /**
         * Gets the table row.
         * 
         * @param metric
         *            the metric
         * @param row
         *            the row
         * @param startCol
         *            the start col
         * @return the table row
         */
        public List<HasClickHandlers> getTableRow(ServiceMetric metric, int row, int startCol);

        /**
         * Gets the table column.
         * 
         * @param metric
         *            the metric
         * @param startRow
         *            the start row
         * @param col
         *            the col
         * @return the table column
         */
        public List<HasClickHandlers> getTableColumn(ServiceMetric metric, int startRow, int col);

        /**
         * Sets the selection.
         * 
         * @param selection
         *            the selection
         */
        public void setSelection(Map<ObjectType, String> selection);

        /**
         * Reset.
         */
        public void reset();

        /**
         * Sets the download url.
         * 
         * @param m
         *            the m
         * @param url
         *            the url
         */
        public void setDownloadUrl(ServiceMetric m, String url);

        /**
         * Gets the filter.
         * 
         * @return the filter
         */
        public Filterable getFilter();

        /**
         * Sets the filter label.
         * 
         * @param str
         *            the new filter label
         */
        public void setFilterLabel(String str);

        /**
         * Sets the service call trend data.
         * 
         * @param dataRanges
         *            the new service call trend data
         */
        public void setServiceCallTrendData(List<TimeSlotData> dataRanges, long aggregationPeriod, int hourSpan, String graphTitle);

        /**
         * Sets the service performance trend data.
         * 
         * @param dataRanges
         *            the new service performance trend data
         */
        public void setServicePerformanceTrendData(List<TimeSlotData> dataRanges, long aggregationPeriod, int hourSpan, String graphTitle);

        /**
         * Sets the service error trend data.
         * 
         * @param dataRanges
         *            the new service error trend data
         */
        public void setServiceErrorTrendData(List<TimeSlotData> dataRanges, long aggregationPeriod, int hourSpan, String graphTitle);

        public HasWidgets getErrorWidget();
    }

    /**
     * Instantiates a new service presenter.
     * 
     * @param eventBus
     *            the event bus
     * @param view
     *            the view
     * @param queryService
     *            the query service
     */
    public ServicePresenter(HandlerManager eventBus, Display view, MetricsQueryService queryService) {
        this.view = view;
        this.view.setAssociatedId(SERVICE_ID);
        this.eventBus = eventBus;
        this.queryService = queryService;
        bind();
    }

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.Presenter#getId()
     */
    public String getId() {
        return SERVICE_ID;
    }

    /**
     * Handle a navigational change, either via the history forward/back buttons or via a user selection (for simplicity
     * also generated as a history change).
     * 
     * @param container
     *            the container
     * @param token
     *            the token
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasWidgets,
     *      org.ebayopensource.turmeric.monitoring.client.model.HistoryToken)
     */
    public void go(HasWidgets container, HistoryToken token) {
        SelectionContext oldContext = selectionContext;
        selectionContext = SelectionContext.fromHistoryToken(token);
        view.setSelection(selectionContext.getSelections());
        conditionalFireServiceFilterSelectionEvent(oldContext, selectionContext);

        if (servicesList == null)
            fetchServices();

        // if no dates have been selected, then by default compare the last fully complete
        // hour of today with yesterday
        FilterContext filter = FilterContext.fromHistoryToken(token);
        Date now = new Date();
        long fullTimeLastHour = Util.getLastHour(now);
        long sameTimeYesterday = Util.get24HrsPrevious(fullTimeLastHour);

        long oldDate1 = selectedDate1;
        long oldDate2 = selectedDate2;

        selectedDate1 = (filter.getDate1() == 0 ? new Date(sameTimeYesterday).getTime() : filter.getDate1());
        selectedDate2 = (filter.getDate2() == 0 ? new Date(fullTimeLastHour).getTime() : filter.getDate2());

        Date asDate1 = new Date(selectedDate1);
        Date asDate2 = new Date(selectedDate2);

        ServicePresenter.this.view.getFilter().setHours1(Util.getAvailableHours(selectedDate1));
        ServicePresenter.this.view.getFilter().setHour1(asDate1.getHours());
        ServicePresenter.this.view.getFilter().setDate1(asDate1);

        ServicePresenter.this.view.getFilter().setDate2(asDate2);

        int oldInterval = selectedDurationHrs;
        selectedDurationHrs = (filter.getDurationHrs() == 0 ? MetricsQueryService.DEFAULT_DURATION_HRS : filter
                        .getDurationHrs());

        view.setFilterLabel(makeFilterLabel(selectedDate1, selectedDate2, selectedDurationHrs));
        conditionalFireDateSelectionEvent(oldDate1, selectedDate1, oldDate2, selectedDate2, oldInterval,
                        selectedDurationHrs);

        int[] intervals = new int[24];
        for (int i = 0; i < 24; i++)
            intervals[i] = i + 1;
        ServicePresenter.this.view.getFilter().setDurations(intervals);
        ServicePresenter.this.view.getFilter().setDuration(selectedDurationHrs);
        ServicePresenter.this.view.getFilter().setMetricNames(
                        Util.convertFromEnumToCamelCase(Arrays.asList(ServiceMetric.values())));

        if (filter.getMetricNames() == null)
            selectedMetrics = new ArrayList<ServiceMetric>(Arrays.asList(ServiceMetric.values())); // do them all by
                                                                                                   // default
        else
            selectedMetrics = Util.convertToEnumFromCamelCase(filter.getMetricNames(), ServiceMetric.class);

        view.getFilter().setSelectedMetricNames(Util.convertFromEnumToCamelCase(selectedMetrics));

        view.reset();

        if (selectionContext.getSelection(ObjectType.ServiceName) != null) {
            fetchMetrics(selectedMetrics, selectionContext, selectedDate1, selectedDate2, selectedDurationHrs);
        }

        ((Dashboard) container).activate(this.view);
    }

    /**
     * Bind.
     */
    public void bind() {
        // listen for any changes from other tabs to the currently selected service or operation
        this.eventBus.addHandler(ObjectSelectionEvent.TYPE, new ObjectSelectionEventHandler() {

            public void onSelection(ObjectSelectionEvent event) {
                selectionContext = new SelectionContext();
                if (event.getSelection(ObjectType.ServiceName) != null)
                    selectionContext.select(ObjectType.ServiceName, event.getSelection(ObjectType.ServiceName));
                if (event.getSelection(ObjectType.OperationName) != null)
                    selectionContext.select(ObjectType.OperationName, event.getSelection(ObjectType.OperationName));
            }
        });
        // listen for changes to date1 from other tabs
        this.view.getFilter().getDate1().addValueChangeHandler(new ValueChangeHandler<Date>() {

            public void onValueChange(ValueChangeEvent<Date> event) {
                Date date = event.getValue();
                int[] hrs = Util.getAvailableHours(date);
                ServicePresenter.this.view.getFilter().setHours1(hrs);
            }
        });

        // handle user selection of some new dates and intervals to see metrics for
        this.view.getFilter().getApplyButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                if (selectionContext.getSelection(ObjectType.ServiceName) == null) {
                    view.error(ConsoleUtil.messages.selectServer());
                }
                else {
                    // Get the date component
                    selectedDate1 = ServicePresenter.this.view.getFilter().getDate1().getValue().getTime();
                    selectedDate2 = ServicePresenter.this.view.getFilter().getDate2().getValue().getTime();

                    // Get the hour component
                    int hour1 = ServicePresenter.this.view.getFilter().getHour1();
                    int hour2 = ServicePresenter.this.view.getFilter().getHour1();
                    selectedDate1 += (Util.HRS_1_MS * hour1);
                    selectedDate2 += (Util.HRS_1_MS * hour2);

                    // Get the selected interval
                    selectedDurationHrs = ServicePresenter.this.view.getFilter().getDuration();

                    view.setFilterLabel(makeFilterLabel(selectedDate1, selectedDate2, selectedDurationHrs));

                    // Inform of changes to dates and durations
                    eventBus.fireEvent(new DateFilterSelectionEvent(selectedDate1, selectedDate2, selectedDurationHrs));

                    // Get which metrics are required
                    selectedMetrics = Util.convertToEnumFromCamelCase(ServicePresenter.this.view.getFilter()
                                    .getSelectedMetricNames(), ServiceMetric.class);

                    view.reset();
                    // Window.alert("getApplyButton().addClickHandler. Before fetchMetrics");
                    // Make a history event so the back/forward buttons work but don't fire it as we don't
                    // want to change pages
                    fetchMetrics(selectedMetrics, selectionContext, selectedDate1, selectedDate2, selectedDurationHrs);
                    insertHistory(selectionContext, selectedDate1, selectedDate2, selectedDurationHrs, selectedMetrics,
                                    false);
                }
            }
        });

        // handle selection of service or operation from list
        this.view.getSelector().addSelectionHandler(new SelectionHandler<TreeItem>() {

            public void onSelection(SelectionEvent<TreeItem> event) {
                TreeItem selection = event.getSelectedItem();
                // get service and or operation name corresponding to this selection
                selectionContext = new SelectionContext();

                // If its the root, then no service is selected
                if (selection.getParentItem() == null) {
                    selectionContext.unselect(ObjectType.ServiceName);
                    selectionContext.unselect(ObjectType.OperationName);
                }
                else {

                    // If its a leaf, its an operation
                    if (selection.getChildCount() == 0) {
                        selectionContext.select(ObjectType.ServiceName, selection.getParentItem().getText());
                        selectionContext.select(ObjectType.OperationName, selection.getText());
                    }
                    else {
                        // Its a service
                        selectionContext.select(ObjectType.ServiceName, selection.getText());
                    }
                }

                view.setSelection(selectionContext.getSelections());
                // tell any interested parties the user has selected a service or operation
                fireObjectSelectionEvent(selectionContext);

                // Get the date component
                selectedDate1 = ServicePresenter.this.view.getFilter().getDate1().getValue().getTime();
                selectedDate2 = ServicePresenter.this.view.getFilter().getDate2().getValue().getTime();

                // Get the hour component
                int hour1 = ServicePresenter.this.view.getFilter().getHour1();
                int hour2 = ServicePresenter.this.view.getFilter().getHour1();
                selectedDate1 += (Util.HRS_1_MS * hour1);
                selectedDate2 += (Util.HRS_1_MS * hour2);

                // Get the interval
                selectedDurationHrs = ServicePresenter.this.view.getFilter().getDuration();

                // Inform of the selection of dates and duration
                eventBus.fireEvent(new DateFilterSelectionEvent(selectedDate1, selectedDate2, selectedDurationHrs));

                // Get the metrics requested
                selectedMetrics = Util.convertToEnumFromCamelCase(ServicePresenter.this.view.getFilter()
                                .getSelectedMetricNames(), ServiceMetric.class);

                view.reset();

                // If at least a service was selected, get the metrics for the currently selected date
                if (selectionContext.isSelected(ObjectType.ServiceName))
                    fetchMetrics(selectedMetrics, selectionContext, selectedDate1, selectedDate2, selectedDurationHrs);

                // Make a history event so the back/forward buttons work but don't fire it as we don't
                // want to change pages
                insertHistory(selectionContext, selectedDate1, selectedDate2, selectedDurationHrs, selectedMetrics,
                                false);
            }
        });
    }

    /**
     * Get a number of metrics from the server.
     * 
     * @param metrics
     *            the metrics
     * @param selectionContext
     *            the selection context
     * @param date1
     *            the date1
     * @param date2
     *            the date2
     * @param intervalHrs
     *            the interval hrs
     */
    protected void fetchMetrics(List<ServiceMetric> metrics, SelectionContext selectionContext, long date1, long date2,
                    int intervalHrs) {
        boolean callTopVolumeGraph = false;
        boolean callPerformanceGraph = false;
        boolean callTopErrorsGraph = false;

        Entity returnType = null;
        for (ServiceMetric m : metrics) {
            switch (m) {
                case TopVolume: {
                    // callcount grouped by operation
                    returnType = Entity.Operation;
                    callTopVolumeGraph = true;
                    break;
                }
                case ConsumerTraffic: {
                    // callcount, grouped by consumer
                    returnType = Entity.Consumer;
                    break;
                }
                case LeastPerformance: {
                    // response time, grouped by operation
                    returnType = Entity.Operation;
                    callPerformanceGraph = true;
                    break;
                }
                case TopErrors: {
                    // error count - grouped by service, or operation
                    returnType = Entity.Error;
                    callTopErrorsGraph = true;
                    break;
                }
                case ConsumerErrors: {
                    // error count - grouped by consumers
                    returnType = Entity.Consumer;
                    break;
                }
            }

            fetchMetric(m, selectionContext, returnType, date1, date2, intervalHrs);
        }

        if (callTopVolumeGraph)
            getServiceCallTrend(selectionContext, date1, date2, intervalHrs);
        if (callPerformanceGraph)
            getServicePerformanceTrend(selectionContext, date1, date2, intervalHrs);
        if (callTopErrorsGraph)
            getServiceErrorTrend(selectionContext, date1, date2, intervalHrs);

    }

    private void getSimpleGraphData(String metricName, String roleType, long aggregationPeriod,
                    final SelectionContext selectionContext, long date1, long date2, final int hourSpan,
                    AsyncCallback<List<TimeSlotData>> callback) {
        GraphUtil.getSimpleGraphData(queryService, metricName, roleType, aggregationPeriod, selectionContext, date1, date2, hourSpan, callback);
    }

    private void getServiceCallTrend(final SelectionContext selectionContext, long date1, long date2, final int hourSpan) {
        AsyncCallback<List<TimeSlotData>> callback = new AsyncCallback<List<TimeSlotData>>() {

            @Override
            public void onSuccess(List<TimeSlotData> dataRanges) {
                String serviceOpName = selectionContext.getSelection(ObjectType.ServiceName);
                if (selectionContext.getSelection(ObjectType.OperationName) != null) {
                    serviceOpName += "." + selectionContext.getSelection(ObjectType.OperationName);
                }
                String graphTitle = ConsoleUtil.messages.graphTitle("Call Count", serviceOpName, hourSpan);
                ServicePresenter.this.view.activate();
                ServicePresenter.this.view.setServiceCallTrendData(dataRanges, minAggregationPeriod, hourSpan, graphTitle);
            }

            @Override
            public void onFailure(Throwable exception) {
                errorLogger.log(Level.SEVERE, "error in getServiceCallTrend", exception);
            }
        };
        this.getSimpleGraphData("CallCount", "server", minAggregationPeriod, selectionContext, date1, date2, hourSpan,
                        callback);
    }

    private void getServicePerformanceTrend(final SelectionContext selectionContext, long date1, long date2,
                    final int hourSpan) {
        AsyncCallback<List<TimeSlotData>> callback = new AsyncCallback<List<TimeSlotData>>() {

            @Override
            public void onSuccess(List<TimeSlotData> dataRanges) {
                String serviceOpName = selectionContext.getSelection(ObjectType.ServiceName);
                if (selectionContext.getSelection(ObjectType.OperationName) != null) {
                    serviceOpName += "." + selectionContext.getSelection(ObjectType.OperationName);
                }
                String graphTitle = ConsoleUtil.messages.graphTitle("Response Time", serviceOpName, hourSpan);
                ServicePresenter.this.view.activate();
                ServicePresenter.this.view.setServicePerformanceTrendData(dataRanges, minAggregationPeriod, hourSpan, graphTitle);
            }

            @Override
            public void onFailure(Throwable exception) {
                errorLogger.log(Level.SEVERE, "error in getServicePerformanceTrend", exception);
                
            }
        };
        
        this.getSimpleGraphData("ResponseTime", "server", minAggregationPeriod, selectionContext, date1, date2, hourSpan,
                        callback);
        
    }

    private void getServiceErrorTrend(final SelectionContext selectionContext, long date1, long date2,
                    final int hourSpan) {

        AsyncCallback<List<TimeSlotData>> callback = new AsyncCallback<List<TimeSlotData>>() {

            @Override
            public void onSuccess(List<TimeSlotData> dataRanges) {
                String serviceOpName = selectionContext.getSelection(ObjectType.ServiceName);
                if (selectionContext.getSelection(ObjectType.OperationName) != null) {
                    serviceOpName += "." + selectionContext.getSelection(ObjectType.OperationName);
                }
                String graphTitle = ConsoleUtil.messages.graphTitle("Error Count", serviceOpName, hourSpan);
                ServicePresenter.this.view.activate();
                ServicePresenter.this.view.setServiceErrorTrendData(dataRanges, minAggregationPeriod, hourSpan, graphTitle);
            }

            @Override
            public void onFailure(Throwable exception) {
                GWT.log(exception.getMessage());
            }
        };
        
        this.getSimpleGraphData("ErrorCount", "server", minAggregationPeriod, selectionContext, date1, date2, hourSpan,
                        callback);
        
    }

    /**
     * Get some metrics numbers from the server.
     * 
     * @param m
     *            the metric to fetch
     * @param selectionContext
     *            the selection context
     * @param returnType
     *            the return type
     * @param date1
     *            the date1
     * @param date2
     *            the date2
     * @param intervalHrs
     *            the interval hrs
     */
    protected void fetchMetric(final ServiceMetric m, final SelectionContext selectionContext, Entity returnType,
                    final long date1, final long date2, final int intervalHrs) {
        List<EntityName> subject = new ArrayList<EntityName>();
        if (selectionContext.getSelection(ObjectType.ServiceName) != null) {
            EntityName serviceName = new EntityName();
            serviceName.type = Entity.Service;
            serviceName.add(selectionContext.getSelection(ObjectType.ServiceName));
            subject.add(serviceName);
        }

        if (selectionContext.getSelection(ObjectType.OperationName) != null) {
            EntityName opName = new EntityName();
            opName.type = Entity.Operation;
            opName.add(selectionContext.getSelection(ObjectType.OperationName));
            subject.add(opName);
        }

        MetricCriteria mc = MetricCriteria.newMetricCriteria(m.toMetricName(), date2, date1, intervalHrs,
                        Ordering.Descending, 10, Perspective.Server, false);
        MetricResourceCriteria rmc = MetricResourceCriteria.newMetricResourceCriteria(subject, returnType);
        String url = queryService.getMetricDataDownloadUrl(mc, rmc);
        ServicePresenter.this.view.setDownloadUrl(m, url);

        queryService.getMetricData(mc, rmc, new AsyncCallback<MetricData>() {

            public void onFailure(Throwable error) {
                if (!ConsoleUtil.messages.badOrMissingResponseData().equals(error.getMessage()))
                    ServicePresenter.this.view.error(ConsoleUtil.messages.serverError(error.getLocalizedMessage()));
                else
                    ServicePresenter.this.view.setMetric(m, null);
            }

            public void onSuccess(MetricData metric) {
                ServicePresenter.this.view.activate();
                ServicePresenter.this.view.setMetric(m, metric);
                switch (m) {
                    case ConsumerErrors:
                    case ConsumerTraffic: {
                        // add a handler so a click navigates to the consumer page
                        List<HasClickHandlers> clickHandlers = view.getTableColumn(m, 1, 0);
                        if (clickHandlers != null) {
                            for (HasClickHandlers h : clickHandlers) {
                                h.addClickHandler(new ClickHandler() {
                                    public void onClick(ClickEvent event) {
                                        Object o = event.getSource();
                                        if (o instanceof HasText) {
                                            String consumer = ((HasText) o).getText();
                                            SelectionContext tmpSelection = new SelectionContext();
                                            tmpSelection.selectAll(selectionContext);
                                            tmpSelection.select(ObjectType.ConsumerName, consumer);
                                            insertHistory(ConsumerPresenter.CONSUMER_ID, tmpSelection, date1, date2,
                                                            intervalHrs, true);
                                        }
                                    }
                                });
                            }
                        }
                        break;
                    }
                        /*
                         * Commented out. At the time of first release, the errors listed in the Top Errors table are in
                         * fact the names of the error metrics (eg SoaFwk.Op.Error.Total, SoaFwk.Op.Err.Category.System
                         * etc) instead of a meaningful name of the errors themselves.
                         */
                        /*
                         * case TopErrors: { //add a handler so a click navigates to the error page
                         * List<HasClickHandlers> clickHandlers = view.getTableColumn(m,1,0); if (clickHandlers != null)
                         * { for (HasClickHandlers h:clickHandlers) { h.addClickHandler(new ClickHandler() { public void
                         * onClick(ClickEvent event) { Object o = event.getSource(); if (o instanceof HasText) { String
                         * errorName = ((HasText)o).getText(); SelectionContext tmpSelection = new SelectionContext();
                         * tmpSelection.selectAll(selectionContext); tmpSelection.select(ObjectType.ErrorName,
                         * errorName); insertHistory(ErrorPresenter.ERROR_ID, tmpSelection, date1, date2, intervalHrs,
                         * true); } } }); } } break; }
                         */
                }
            }
        });

    }

    /**
     * Upload the list of Services/operations.
     */
    protected void fetchServices() {
        queryService.getServices(new AsyncCallback<Map<String, Set<String>>>() {

            public void onFailure(Throwable error) {
                //here
                ServicePresenter.this.view.error(ConsoleUtil.messages.serverError(error.getLocalizedMessage()));
            }

            public void onSuccess(Map<String, Set<String>> services) {
                ServicePresenter.this.servicesList = services;
                ServicePresenter.this.view.setServicesMap(services);
                // tell any interested parties about the service/op list
                ServicePresenter.this.eventBus.fireEvent(new GetServicesEvent(services));
            }
        });
    }

    /**
     * Make an entry in the history so we can return to it, but do not necessarily cause the history mechanism to fire,
     * as we may want to stay on the same page.
     * 
     * @param selectionContext
     *            the selection context
     * @param d1
     *            start date1
     * @param d2
     *            start date2
     * @param interval
     *            how many hrs to add to starting dates
     * @param metrics
     *            which metrics to view
     * @param fire
     *            whether or not to fire the history event
     */
    protected void insertHistory(SelectionContext selectionContext, long d1, long d2, int interval,
                    Collection<ServiceMetric> metrics, boolean fire) {
        HistoryToken token = HistoryToken.newHistoryToken(DashboardPresenter.DASH_ID, null);
        token.addValue(DashboardPresenter.TAB, SERVICE_ID);
        selectionContext.appendToHistoryToken(token);
        token.addValue(HistoryToken.SELECTED_DATE1_TOKEN, String.valueOf(d1));
        token.addValue(HistoryToken.SELECTED_DATE2_TOKEN, String.valueOf(d2));
        token.addValue(HistoryToken.SELECTED_DURATION_TOKEN, String.valueOf(interval));
        token.addValue(HistoryToken.SELECTED_METRICS_TOKEN, metrics);
        History.newItem(token.toString(), fire);
    }

    /**
     * Insert history.
     * 
     * @param presenterId
     *            the presenter id
     * @param selectionContext
     *            the selection context
     * @param d1
     *            the d1
     * @param d2
     *            the d2
     * @param interval
     *            the interval
     * @param fire
     *            the fire
     */
    protected void insertHistory(String presenterId, SelectionContext selectionContext, long d1, long d2, int interval,
                    boolean fire) {
        HistoryToken token = HistoryToken.newHistoryToken(DashboardPresenter.DASH_ID, null);
        token.addValue(DashboardPresenter.TAB, presenterId);
        selectionContext.appendToHistoryToken(token);
        token.addValue(HistoryToken.SELECTED_DATE1_TOKEN, String.valueOf(d1));
        token.addValue(HistoryToken.SELECTED_DATE2_TOKEN, String.valueOf(d2));
        token.addValue(HistoryToken.SELECTED_DURATION_TOKEN, String.valueOf(interval));
        History.newItem(token.toString(), fire);
    }

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.Presenter.TabPresenter#getStateAsHistoryToken()
     */
    public HistoryToken getStateAsHistoryToken() {
        HistoryToken token = HistoryToken.newHistoryToken(DashboardPresenter.DASH_ID);
        token.addValue(DashboardPresenter.TAB, SERVICE_ID);
        if (selectionContext != null)
            selectionContext.appendToHistoryToken(token);
        token.addValue(HistoryToken.SELECTED_DATE1_TOKEN, String.valueOf(selectedDate1));
        token.addValue(HistoryToken.SELECTED_DATE2_TOKEN, String.valueOf(selectedDate2));
        token.addValue(HistoryToken.SELECTED_DURATION_TOKEN, String.valueOf(selectedDurationHrs));
        token.addValue(HistoryToken.SELECTED_METRICS_TOKEN, selectedMetrics);
        return token;
    }

    /**
     * Conditionally fire the service selection event. Only fire if the selection is different to previous.
     * 
     * @param oldSelections
     * @param newSelections
     */
    private void conditionalFireServiceFilterSelectionEvent(SelectionContext oldContext, SelectionContext newContext) {
        // Inform interested parties if either the service or operation changed
        if ((oldContext == null && newContext != null) || (oldContext != null && newContext != null)
                        || !oldContext.equals(newContext)) {
            Map<ObjectType, String> selection = new HashMap<ObjectType, String>(2);
            selection.put(ObjectType.ServiceName, newContext.getSelection(ObjectType.ServiceName));
            selection.put(ObjectType.OperationName, newContext.getSelection(ObjectType.OperationName));

            eventBus.fireEvent(new ObjectSelectionEvent(selection));
        }
    }

    private void fireObjectSelectionEvent(SelectionContext sc) {
        eventBus.fireEvent(new ObjectSelectionEvent(sc.getSelections()));
    }

    /**
     * Conditionally fire the date and interval selection event. Only fire if the selection is different to previous.
     * 
     * @param oldD1
     * @param newD1
     * @param oldD2
     * @param newD2
     * @param oldDurationHrs
     * @param newDurationHrs
     */
    private void conditionalFireDateSelectionEvent(long oldD1, long newD1, long oldD2, long newD2, long oldDurationHrs,
                    int newDurationHrs) {
        // Inform interested parties if the dates or duration changed
        if ((oldD1 != newD1) || (oldD2 != newD2) || (oldDurationHrs != newDurationHrs)) {
            eventBus.fireEvent(new DateFilterSelectionEvent(newD1, newD2, newDurationHrs));
        }
    }

    private String makeFilterLabel(long d1, long d2, int durationHrs) {
        String d1s = ConsoleUtil.timeFormat.format(new Date(d1));
        String d2s = ConsoleUtil.timeFormat.format(new Date(d2));

        String filterString = d1s + " + " + (durationHrs) + " - ";
        filterString += d2s + " + " + (durationHrs) + " >>";
        return filterString;
    }

}
