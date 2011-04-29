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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.Dashboard;
import org.ebayopensource.turmeric.monitoring.client.Util;
import org.ebayopensource.turmeric.monitoring.client.event.DateFilterSelectionEvent;
import org.ebayopensource.turmeric.monitoring.client.event.DateFilterSelectionHandler;
import org.ebayopensource.turmeric.monitoring.client.event.GetServicesEvent;
import org.ebayopensource.turmeric.monitoring.client.event.GetServicesEventHandler;
import org.ebayopensource.turmeric.monitoring.client.event.ObjectSelectionEvent;
import org.ebayopensource.turmeric.monitoring.client.event.ObjectSelectionEventHandler;
import org.ebayopensource.turmeric.monitoring.client.model.ConsumerMetric;
import org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfo;
import org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfoImpl;
import org.ebayopensource.turmeric.monitoring.client.model.FilterContext;
import org.ebayopensource.turmeric.monitoring.client.model.Filterable;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.client.model.MetricData;
import org.ebayopensource.turmeric.monitoring.client.model.MetricGroupData;
import org.ebayopensource.turmeric.monitoring.client.model.MetricResourceCriteria;
import org.ebayopensource.turmeric.monitoring.client.model.MetricValue;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Entity;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.EntityName;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Ordering;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Perspective;
import org.ebayopensource.turmeric.monitoring.client.model.ObjectType;
import org.ebayopensource.turmeric.monitoring.client.model.SelectionContext;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;

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
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * ConsumerPresenter.
 */
public class ConsumerPresenter implements Presenter.TabPresenter {

    /** The Constant CONSUMER_ID. */
    protected final static String CONSUMER_ID = "Consumer";
    
    /** The Constant ONE_CONSUMER_METRICS. */
    protected final static List<ConsumerMetric> ONE_CONSUMER_METRICS = Arrays.asList(new ConsumerMetric[] {
            ConsumerMetric.TopVolume, ConsumerMetric.LeastPerformance, ConsumerMetric.TopServiceErrors,
            ConsumerMetric.TopConsumerErrors });
    
    /** The Constant ANY_CONSUMER_METRICS. */
    protected final static List<ConsumerMetric> ANY_CONSUMER_METRICS = Arrays.asList(new ConsumerMetric[] {
            ConsumerMetric.CallVolume, ConsumerMetric.Performance, ConsumerMetric.Errors });
    
    /** The view. */
    protected Display view;
    
    /** The event bus. */
    protected HandlerManager eventBus;
    
    /** The query service. */
    protected MetricsQueryService queryService;
    
    /** The services list. */
    protected Map<String, Set<String>> servicesList;
    
    /** The selection context. */
    protected SelectionContext selectionContext;
    
    /** The selected duration. */
    protected int selectedDuration;
    
    /** The selected date1. */
    protected long selectedDate1;
    
    /** The selected date2. */
    protected long selectedDate2;
    
    /** The selected metrics. */
    protected List<ConsumerMetric> selectedMetrics;

    /**
     * The Interface Display.
     */
    public interface Display extends org.ebayopensource.turmeric.monitoring.client.Display {
        
        /**
         * Gets the table column.
         *
         * @param metric the metric
         * @param startRow the start row
         * @param col the col
         * @return the table column
         */
        public List<HasClickHandlers> getTableColumn(ConsumerMetric metric, int startRow, int col);

        /**
         * Error.
         *
         * @param error the error
         */
        public void error(String error);

        /**
         * Sets the services map.
         *
         * @param map the map
         */
        public void setServicesMap(Map<String, Set<String>> map);

        /**
         * Sets the selection.
         *
         * @param selections the selections
         */
        public void setSelection(Map<ObjectType, String> selections);

        /**
         * Gets the selector.
         *
         * @return the selector
         */
        public HasSelectionHandlers<TreeItem> getSelector();

        /**
         * Sets the metric.
         *
         * @param m the m
         * @param result the result
         */
        public void setMetric(ConsumerMetric m, MetricData result);

        /**
         * Sets the download url.
         *
         * @param m the m
         * @param url the url
         */
        public void setDownloadUrl(ConsumerMetric m, String url);

        /**
         * Reset.
         */
        public void reset();

        /**
         * Gets the filter.
         *
         * @return the filter
         */
        public Filterable getFilter();

        /**
         * Sets the filter label.
         *
         * @param str the new filter label
         */
        public void setFilterLabel(String str);

        /**
         * Sets the consumer call trend data.
         *
         * @param graphData the new consumer call trend data
         */
        void setConsumerCallTrendData(List<TimeSlotData> graphData);
    }

    /**
     * Instantiates a new consumer presenter.
     *
     * @param eventBus the event bus
     * @param view the view
     * @param queryService the query service
     */
    public ConsumerPresenter(HandlerManager eventBus, Display view, MetricsQueryService queryService) {
        this.eventBus = eventBus;
        this.view = view;
        this.view.setAssociatedId(CONSUMER_ID);
        this.queryService = queryService;
        bind();
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.Presenter#getId()
     */
    public String getId() {
        return CONSUMER_ID;
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasWidgets, org.ebayopensource.turmeric.monitoring.client.model.HistoryToken)
     */
    public void go(HasWidgets container, HistoryToken token) {
        // find out which entities have been selected in context
        selectionContext = SelectionContext.fromHistoryToken(token);
        view.setSelection(selectionContext.getSelections());

        if (servicesList == null)
            fetchServices();

        // find out the filter criteria
        FilterContext filter = FilterContext.fromHistoryToken(token);
        // if no dates have been selected, then by default compare the last fully complete
        // hour of today with yesterday
        Date now = new Date();
        long fullTimeLastHour = Util.getLastHour(now);
        long sameTimeYesterday = Util.get24HrsPrevious(fullTimeLastHour);

        selectedDate1 = (filter.getDate1() == 0 ? new Date(sameTimeYesterday).getTime() : filter.getDate1());
        selectedDate2 = (filter.getDate2() == 0 ? new Date(fullTimeLastHour).getTime() : filter.getDate2());
        view.getFilter().setHours1(Util.getAvailableHours(selectedDate1));

        view.getFilter().setHour1(new Date(selectedDate1).getHours());
        view.getFilter().setDate1(new Date(selectedDate1));

        view.getFilter().setHours2(Util.getAvailableHours(selectedDate2));
        view.getFilter().setHour2(new Date(selectedDate2).getHours());
        view.getFilter().setDate2(new Date(selectedDate2));

        // duration
        String tmp = HistoryToken.getValue(token, HistoryToken.SELECTED_DURATION_TOKEN);
        selectedDuration = (filter.getDurationHrs() == 0 ? MetricsQueryService.DEFAULT_DURATION_HRS : filter
                        .getDurationHrs());
        int[] intervals = new int[24];
        for (int i = 0; i < 24; i++)
            intervals[i] = i + 1;
        view.getFilter().setDurations(intervals);
        view.getFilter().setDuration(selectedDuration);
        view.setFilterLabel(makeFilterLabel(selectedDate1, selectedDate2, selectedDuration));

        // which metrics are available
        if (selectionContext.getSelection(ObjectType.ConsumerName) == null) {
            view.getFilter().setMetricNames(Util.convertFromEnumToCamelCase(ANY_CONSUMER_METRICS));
            if (filter.getMetricNames() == null) {
                selectedMetrics = ANY_CONSUMER_METRICS;
            }
            else {
                selectedMetrics = Util.convertToEnumFromCamelCase(filter.getMetricNames(), ConsumerMetric.class);
                if (selectedMetrics == null || selectedMetrics.isEmpty())
                    selectedMetrics = ANY_CONSUMER_METRICS;
            }
        }
        else {
            view.getFilter().setMetricNames(Util.convertFromEnumToCamelCase(ONE_CONSUMER_METRICS));
            if (filter.getMetricNames() == null)
                selectedMetrics = ONE_CONSUMER_METRICS;
            else
                selectedMetrics = Util.convertToEnumFromCamelCase(filter.getMetricNames(), ConsumerMetric.class);
        }

        view.getFilter().setSelectedMetricNames(Util.convertFromEnumToCamelCase(selectedMetrics));
        view.reset();

        fetchMetrics(selectedMetrics, selectionContext, selectedDate1, selectedDate2, selectedDuration);

        // this.view.activate();
        ((Dashboard) container).activate(this.view);
    }

    /**
     * Bind.
     */
    public void bind() {
        // listen for any uploads of the services/operations list by other tabs
        this.eventBus.addHandler(GetServicesEvent.TYPE, new GetServicesEventHandler() {

            public void onData(GetServicesEvent event) {
                // only fetch once?
                if (ConsumerPresenter.this.servicesList == null) {
                    ConsumerPresenter.this.servicesList = event.getData();
                    ConsumerPresenter.this.view.setServicesMap(event.getData());
                }
            }

        });

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

        // listen for any changes from other tabs to the currently selected dates
        this.eventBus.addHandler(DateFilterSelectionEvent.TYPE, new DateFilterSelectionHandler() {

            public void onSelection(DateFilterSelectionEvent event) {
                selectedDate1 = event.getDate1();
                selectedDate2 = event.getDate2();
                selectedDuration = event.getDuration();
                view.getFilter().setHours1(Util.getAvailableHours(selectedDate1));
                view.getFilter().setHour1(new Date(selectedDate1).getHours());
                view.getFilter().setDate1(new Date(selectedDate1));

                view.getFilter().setHours2(Util.getAvailableHours(selectedDate2));
                view.getFilter().setHour2(new Date(selectedDate2).getHours());
                view.getFilter().setDate2(new Date(selectedDate2));
                view.getFilter().setDuration(selectedDuration);
            }
        });

        // listen for user selection of date1
        this.view.getFilter().getDate1().addValueChangeHandler(new ValueChangeHandler<Date>() {

            public void onValueChange(ValueChangeEvent<Date> event) {
                Date date = event.getValue();
                int[] hrs = Util.getAvailableHours(date);
                ConsumerPresenter.this.view.getFilter().setHours1(hrs);
            }
        });

        // listen for user selection of date2
        this.view.getFilter().getDate2().addValueChangeHandler(new ValueChangeHandler<Date>() {

            public void onValueChange(ValueChangeEvent<Date> event) {
                Date date = event.getValue();
                int[] hrs = Util.getAvailableHours(date);
                ConsumerPresenter.this.view.getFilter().setHours2(hrs);
            }
        });

        // handle user selection of some new dates and intervals to see metrics for

        this.view.getFilter().getApplyButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                // Get the date component
                long oldDate1 = selectedDate1;
                long oldDate2 = selectedDate2;
                selectedDate1 = ConsumerPresenter.this.view.getFilter().getDate1().getValue().getTime();
                selectedDate2 = ConsumerPresenter.this.view.getFilter().getDate2().getValue().getTime();

                // Get the hour component
                int hour1 = ConsumerPresenter.this.view.getFilter().getHour1();
                int hour2 = ConsumerPresenter.this.view.getFilter().getHour2();
                selectedDate1 += (Util.HRS_1_MS * hour1);
                selectedDate2 += (Util.HRS_1_MS * hour2);

                // Get the selected interval
                int oldDuration = selectedDuration;
                selectedDuration = ConsumerPresenter.this.view.getFilter().getDuration();

                view.setFilterLabel(makeFilterLabel(selectedDate1, selectedDate2, selectedDuration));

                // tell other interested tabs that the selected dates have changed
                if ((oldDate1 != selectedDate1) || (oldDate2 != selectedDate2) || (oldDuration != selectedDuration)) {
                    eventBus.fireEvent(new DateFilterSelectionEvent(selectedDate1, selectedDate2, selectedDuration));
                }

                // Get which metrics are required
                selectedMetrics = Util.convertToEnumFromCamelCase(ConsumerPresenter.this.view.getFilter()
                                .getSelectedMetricNames(), ConsumerMetric.class);

                // Clean up from previous selections
                ConsumerPresenter.this.view.reset();

                // Make a history event so the back/forward buttons work but don't fire it as we don't
                // want to change pages
                fetchMetrics(selectedMetrics, selectionContext, selectedDate1, selectedDate2, selectedDuration);
                insertHistory(selectionContext, selectedDate1, selectedDate2, selectedDuration, selectedMetrics, false);
            }
        });

        // handle selection of service or operation from list
        this.view.getSelector().addSelectionHandler(new SelectionHandler<TreeItem>() {

            public void onSelection(SelectionEvent<TreeItem> event) {
                TreeItem selection = event.getSelectedItem();
                // get service and or operation name corresponding to this selection
                selectionContext.unselect(ObjectType.ServiceName);
                selectionContext.unselect(ObjectType.OperationName);
                // If its the root, ignore it
                if (selection.getParentItem() != null) {
                    // If its a leaf, its an operation
                    if (selection.getChildCount() == 0) {
                        selectionContext.select(ObjectType.OperationName, selection.getText());
                        selectionContext.select(ObjectType.ServiceName, selection.getParentItem().getText());

                    }
                    else {
                        // Its a service
                        selectionContext.select(ObjectType.ServiceName, selection.getText());
                    }
                }
                view.setSelection(selectionContext.getSelections());

                eventBus.fireEvent(new ObjectSelectionEvent(selectionContext.getSelections()));

                // Get the date component
                selectedDate1 = ConsumerPresenter.this.view.getFilter().getDate1().getValue().getTime();
                selectedDate2 = ConsumerPresenter.this.view.getFilter().getDate2().getValue().getTime();

                // Get the hour component
                int hour1 = ConsumerPresenter.this.view.getFilter().getHour1();
                int hour2 = ConsumerPresenter.this.view.getFilter().getHour2();
                selectedDate1 += (Util.HRS_1_MS * hour1);
                selectedDate2 += (Util.HRS_1_MS * hour2);

                // Get the interval
                selectedDuration = ConsumerPresenter.this.view.getFilter().getDuration();

                // Get the metrics requested
                selectedMetrics = Util.convertToEnumFromCamelCase(ConsumerPresenter.this.view.getFilter()
                                .getSelectedMetricNames(), ConsumerMetric.class);
                view.reset();
                // Fetch set of metrics for the selected service/operation for the currently selected dates
                fetchMetrics(selectedMetrics, selectionContext, selectedDate1, selectedDate2, selectedDuration);

                // Make a history event so the back/forward buttons work but don't fire it as we don't
                // want to change pages
                insertHistory(selectionContext, selectedDate1, selectedDate2, selectedDuration, selectedMetrics, false);
            }
        });
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.Presenter.TabPresenter#getStateAsHistoryToken()
     */
    public HistoryToken getStateAsHistoryToken() {
        HistoryToken token = HistoryToken.newHistoryToken(DashboardPresenter.DASH_ID);
        token.addValue(DashboardPresenter.TAB, CONSUMER_ID);
        if (selectionContext != null)
            selectionContext.appendToHistoryToken(token);

        token.addValue(HistoryToken.SELECTED_DATE1_TOKEN, String.valueOf(selectedDate1));
        token.addValue(HistoryToken.SELECTED_DATE2_TOKEN, String.valueOf(selectedDate2));
        if (selectedDuration > 0)
            token.addValue(HistoryToken.SELECTED_DURATION_TOKEN, String.valueOf(selectedDuration));

        return token;
    }

    /**
     * Fetch services.
     */
    protected void fetchServices() {
        queryService.getServices(new AsyncCallback<Map<String, Set<String>>>() {

            public void onFailure(Throwable error) {
                ConsumerPresenter.this.view.error(ConsoleUtil.messages.serverError(error.getLocalizedMessage()));
            }

            public void onSuccess(Map<String, Set<String>> services) {
                ConsumerPresenter.this.servicesList = services;
                ConsumerPresenter.this.view.setServicesMap(services);
            }
        });
    }

    /**
     * Fetch metrics.
     *
     * @param metrics the metrics
     * @param sc the sc
     * @param date1 the date1
     * @param date2 the date2
     * @param intervalHrs the interval hrs
     */
    protected void fetchMetrics(List<ConsumerMetric> metrics, SelectionContext sc, long date1, long date2,
                    int intervalHrs) {
        for (ConsumerMetric m : metrics) {
            Entity returnType = null;
            switch (m) {
                case CallVolume: {
                    returnType = Entity.Consumer;
                    break;
                }
                case Performance: {
                    returnType = Entity.Consumer;
                    break;
                }
                case Errors: {
                    returnType = Entity.Consumer;
                    break;
                }
                case TopVolume: {
                    if (sc.getSelection(ObjectType.OperationName) != null)
                        returnType = Entity.Operation;
                    else
                        returnType = Entity.Service;
                    break;
                }
                case LeastPerformance: {
                    if (sc.getSelection(ObjectType.OperationName) != null)
                        returnType = Entity.Operation;
                    else
                        returnType = Entity.Service;
                    break;
                }
                case TopServiceErrors: {
                    returnType = Entity.Error;
                    break;
                }
                case TopConsumerErrors: {
                    returnType = Entity.Error;
                    break;
                }
            }
            fetchMetric(m, sc, returnType, date1, date2, intervalHrs);
        }
    }

    /**
     * Fetch metric.
     *
     * @param m the m
     * @param sc the sc
     * @param returnType the return type
     * @param date1 the date1
     * @param date2 the date2
     * @param durationHrs the duration hrs
     */
    protected void fetchMetric(final ConsumerMetric m, final SelectionContext sc, final Entity returnType,
                    final long date1, final long date2, final int durationHrs) {

        List<EntityName> subject = new ArrayList<EntityName>();
        EntityName serviceName = new EntityName();
        EntityName opName = new EntityName();
        EntityName consumerName = new EntityName();

        if (sc.getSelection(ObjectType.ServiceName) != null) {
            serviceName.type = Entity.Service;
            serviceName.add(sc.getSelection(ObjectType.ServiceName));
            subject.add(serviceName);
        }

        if (sc.getSelection(ObjectType.OperationName) != null) {
            opName.type = Entity.Operation;
            opName.add(sc.getSelection(ObjectType.OperationName));
            subject.add(opName);
        }

        if (sc.getSelection(ObjectType.ConsumerName) != null) {
            consumerName.type = Entity.Consumer;
            consumerName.add(ConsoleUtil.convertConsumerToMissing(sc.getSelection(ObjectType.ConsumerName)));
            subject.add(consumerName);
        }

        MetricCriteria mc = MetricCriteria.newMetricCriteria(m.toMetricName(), date1, date2, durationHrs,
                        Ordering.Descending, 10, Perspective.Server, false);
        MetricResourceCriteria rmc = MetricResourceCriteria.newMetricResourceCriteria(subject, returnType);
        String url = queryService.getMetricDataDownloadUrl(mc, rmc);
        ConsumerPresenter.this.view.setDownloadUrl(m, url);
        queryService.getMetricData(mc, rmc, new AsyncCallback<MetricData>() {

            public void onFailure(Throwable error) {
                if (!ConsoleUtil.messages.badOrMissingResponseData().equals(error.getMessage()))
                    ConsumerPresenter.this.view.error(ConsoleUtil.messages.serverError(error.getLocalizedMessage()));
                else
                    ConsumerPresenter.this.view.setMetric(m, null);
            }

            public void onSuccess(MetricData result) {
                ConsumerPresenter.this.view.setMetric(m, result);

                switch (m) {
                    case CallVolume:
                        // here I need to call the getMetricsValue for each consumer name I get. Also I need the 2 dates
                        //getConsumerServiceTrends(sc.getSelection(ObjectType.ServiceName), date1, date2,
                        //                result.getReturnData(), 0);
                    case Performance:
                    case Errors: {
                        List<HasClickHandlers> clickHandlers = ConsumerPresenter.this.view.getTableColumn(m, 1, 0);
                        if (clickHandlers != null) {
                            for (HasClickHandlers h : clickHandlers) {
                                h.addClickHandler(new ClickHandler() {
                                    public void onClick(ClickEvent event) {
                                        Object o = event.getSource();
                                        if (o instanceof HasText) {
                                            String consumer = ((HasText) o).getText();
                                            SelectionContext tmpCtx = new SelectionContext();
                                            tmpCtx.selectAll(selectionContext);
                                            tmpCtx.select(ObjectType.ConsumerName, consumer);

                                            view.reset();
                                            fetchMetrics(ONE_CONSUMER_METRICS, tmpCtx, date1, date2, durationHrs);
                                            insertHistory(ConsumerPresenter.CONSUMER_ID, tmpCtx, date1, date2,
                                                            durationHrs, false);
                                        }
                                    }
                                });
                            }
                        }
                        break;
                    }
                    case TopVolume:
                    case TopServiceErrors:
                    case LeastPerformance: {
                        List<HasClickHandlers> clickHandlers = ConsumerPresenter.this.view.getTableColumn(m, 1, 0);
                        if (clickHandlers != null) {
                            for (HasClickHandlers h : clickHandlers) {
                                h.addClickHandler(new ClickHandler() {
                                    public void onClick(ClickEvent event) {
                                        Object o = event.getSource();
                                        if (o instanceof HasText) {
                                            String name = ((HasText) o).getText();
                                            SelectionContext ctx = new SelectionContext();
                                            if (returnType == returnType.Service)
                                                ctx.select(ObjectType.ServiceName, name);
                                            else if (returnType == returnType.Operation) {
                                                ctx.select(ObjectType.ServiceName,
                                                                sc.getSelection(ObjectType.ServiceName));
                                                ctx.select(ObjectType.OperationName, name);
                                            }
                                            insertHistory(ServicePresenter.SERVICE_ID, ctx, date1, date2, durationHrs,
                                                            true);
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
                         * case TopConsumerErrors: { List<HasClickHandlers> clickHandlers =
                         * ConsumerPresenter.this.view.getTableColumn(m, 1, 0); if (clickHandlers != null) { for
                         * (HasClickHandlers h:clickHandlers) { h.addClickHandler(new ClickHandler() { public void
                         * onClick(ClickEvent event) { Object o = event.getSource(); if (o instanceof HasText) { String
                         * err = ((HasText)o).getText(); SelectionContext ctx = new SelectionContext();
                         * ctx.selectAll(sc); ctx.select(ObjectType.ErrorName, err);
                         * insertHistory(ErrorPresenter.ERROR_ID, ctx, date1, date2, durationHrs, true); } } }); } }
                         * break; }
                         */
                }
            }
        });
    }

    /**
     * Gets the consumer service trends.
     *
     * @param serviceName the service name
     * @param date1 the date1
     * @param date2 the date2
     * @param returnData the return data
     * @param initialIndex the initial index
     * @return the consumer service trends
     */
    protected void getConsumerServiceTrends(final String serviceName, final long date1, final long date2,
                    final List<MetricGroupData> returnData, final int initialIndex) {
        if (returnData != null) {
            // first I need to iterate through the list of consumers
            final int returnDataSize = returnData.size();
            GWT.log("returnDataSize=" + returnDataSize);
            if (returnDataSize == 0) {
                return;
            }
            MetricGroupData metricGroupData = returnData.get(initialIndex);

            // now I call the SQMS with the data for this consumer
            CriteriaInfoImpl criteriaInfo = new CriteriaInfoImpl();
            criteriaInfo.setMetricName("CallCount");
            criteriaInfo.setConsumerName(metricGroupData.getCriteriaInfo().getConsumerName());
            criteriaInfo.setServiceName(serviceName);
            criteriaInfo.setRoleType("server");
            MetricValue firstDate = new MetricValue(criteriaInfo, Util.resetTo12am(date1).getTime(), 86400l,
                            returnDataSize, "");
            MetricValue secondDate = new MetricValue(criteriaInfo, Util.resetTo12am(date2).getTime(), 86400l,
                            returnDataSize, "");

            queryService.getServiceMetricValueTrend(firstDate, secondDate, new AsyncCallback<List<TimeSlotData>>() {

                @Override
                public void onSuccess(List<TimeSlotData> dataRanges) {
                    if (initialIndex < returnDataSize - 1) {
                        ConsumerPresenter.this.getConsumerServiceTrends(serviceName, date1, date2,
                                        returnData.subList(initialIndex, returnDataSize), initialIndex + 1);
                    }
                    else {
                        ConsumerPresenter.this.view.activate();
                        ConsumerPresenter.this.view.setConsumerCallTrendData(dataRanges);
                    }

                }

                @Override
                public void onFailure(Throwable exception) {
                    GWT.log(exception.getMessage());
                }
            });

        }
    }

    /**
     * Insert history.
     *
     * @param sc the sc
     * @param d1 the d1
     * @param d2 the d2
     * @param interval the interval
     * @param metrics the metrics
     * @param fire the fire
     */
    protected void insertHistory(SelectionContext sc, long d1, long d2, int interval,
                    Collection<ConsumerMetric> metrics, boolean fire) {
        HistoryToken token = HistoryToken.newHistoryToken(DashboardPresenter.DASH_ID, null);
        token.addValue(DashboardPresenter.TAB, CONSUMER_ID);

        if (sc != null)
            sc.appendToHistoryToken(token);

        token.addValue(HistoryToken.SELECTED_DATE1_TOKEN, String.valueOf(d1));
        token.addValue(HistoryToken.SELECTED_DATE2_TOKEN, String.valueOf(d2));
        token.addValue(HistoryToken.SELECTED_DURATION_TOKEN, String.valueOf(interval));
        token.addValue(HistoryToken.SELECTED_METRICS_TOKEN, metrics);
        History.newItem(token.toString(), fire);
    }

    /**
     * Insert history.
     *
     * @param tabId the tab id
     * @param sc the sc
     * @param d1 the d1
     * @param d2 the d2
     * @param interval the interval
     * @param fire the fire
     */
    protected void insertHistory(String tabId, SelectionContext sc, long d1, long d2, int interval, boolean fire) {
        HistoryToken token = HistoryToken.newHistoryToken(DashboardPresenter.DASH_ID, null);
        token.addValue(DashboardPresenter.TAB, tabId);

        if (sc != null)
            sc.appendToHistoryToken(token);

        token.addValue(HistoryToken.SELECTED_DATE1_TOKEN, String.valueOf(d1));
        token.addValue(HistoryToken.SELECTED_DATE2_TOKEN, String.valueOf(d2));
        token.addValue(HistoryToken.SELECTED_DURATION_TOKEN, String.valueOf(interval));
        History.newItem(token.toString(), fire);
    }

    private String makeFilterLabel(long d1, long d2, int durationHrs) {
        String d1s = ConsoleUtil.timeFormat.format(new Date(d1));
        String d2s = ConsoleUtil.timeFormat.format(new Date(d2));

        String filterString = d1s + " + " + (durationHrs) + " - ";
        filterString += d2s + " + " + (durationHrs) + " >>";
        return filterString;
    }

}
