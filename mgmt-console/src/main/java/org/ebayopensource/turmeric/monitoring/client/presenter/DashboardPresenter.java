/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.presenter;

import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.Controller;
import org.ebayopensource.turmeric.monitoring.client.Dashboard;
import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService;
import org.ebayopensource.turmeric.monitoring.client.view.ConsumerView;
import org.ebayopensource.turmeric.monitoring.client.view.ErrorView;
import org.ebayopensource.turmeric.monitoring.client.view.ServiceView;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * The Class DashboardPresenter.
 */
public class DashboardPresenter implements Presenter, Controller {
    
    /** The Constant DASH_ID. */
    public static final String DASH_ID = "dash";
    
    /** The Constant TAB. */
    public static final String TAB = "tab";
    
    /** The event bus. */
    protected HandlerManager eventBus;
    
    /** The view. */
    protected Dashboard view;
    
    /** The query service. */
    protected MetricsQueryService queryService;
    
    /** The default presenter. */
    protected TabPresenter defaultPresenter;
    
    /** The presenters. */
    protected Map<String,TabPresenter> presenters = new HashMap<String,TabPresenter>();
    
    /** The added. */
    protected boolean added;
    
    /** The container. */
    protected HasWidgets container;
  
    
    /**
     * Instantiates a new dashboard presenter.
     *
     * @param eventBus the event bus
     * @param view the view
     * @param queryService the query service
     */
    public DashboardPresenter (HandlerManager eventBus, Dashboard view, MetricsQueryService queryService) {
        this.eventBus = eventBus;
        this.view = view;
        this.queryService = queryService;
        initTabPresenters();
        bind();
    }
    
    /**
     * Gets the id.
     *
     * @return the id
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.Presenter#getId()
     */
    public String getId() {
        return DASH_ID;
    }

    /**
     * Bind.
     */
    public void bind () {
        //listen for tabs to be selected so we can update the central History
        //mechanism
        this.view.getTabSelector().addSelectionHandler(new SelectionHandler<Integer> () {

            public void onSelection(SelectionEvent<Integer> event) {
                Integer tab = event.getSelectedItem();
                if (tab == null) 
                    return;
         
                //Work out which tab was selected and move to it via
                //the history mechanism, so that the back/forward buttons
                //recall the tab selections.
                Display display = DashboardPresenter.this.view.getView(tab);
                
                //in creating a new history item every time we click a tab,
                //this means that we are not going back to show the last state associated
                //with the tab. Instead we are creating new state! Eg:
                // click Services tab
                //   click an operation
                // click Pools tab
                //
                // *** if you click Back now:
                // you will get the operation 
                //***** if you click Services now:
                // you will get the default services contents
                //History.newItem(newTabHistoryToken(display.getAssociatedId()).toString());
                TabPresenter p = presenters.get(display.getAssociatedId());
                if (p!=null) {
                    String history = p.getStateAsHistoryToken().toString();
                    History.newItem(history);
                }
                else
                    History.newItem(newTabHistoryToken(display.getAssociatedId()).toString());
            }
        });
        
    }

    /**
     * Go.
     *
     * @param container the container
     * @param token the token
     * @see org.ebayopensource.turmeric.monitoring.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasWidgets, org.ebayopensource.turmeric.monitoring.client.model.HistoryToken)
     */
    public void go(HasWidgets container, HistoryToken token) {
        this.container = container;
        //if (!added) {
            container.add(this.view.asWidget());
            added = true;
        //}
        selectPresenter(token);
    }

    /**
     * Adds the presenter.
     *
     * @param id the id
     * @param p the p
     * @see org.ebayopensource.turmeric.monitoring.client.Controller#addPresenter(java.lang.String, org.ebayopensource.turmeric.monitoring.client.presenter.Presenter)
     */
    public void addPresenter(String id, Presenter p) {
        presenters.put(id, (TabPresenter)p);
    }

    /**
     * Gets the presenter.
     *
     * @param id the id
     * @return the presenter
     * @see org.ebayopensource.turmeric.monitoring.client.Controller#getPresenter(java.lang.String)
     */
    public Presenter getPresenter(String id) {
        return presenters.get(id);
    }
    
    /**
     * Gets the tab presenter.
     *
     * @param id the id
     * @return the tab presenter
     */
    public TabPresenter getTabPresenter(String id) {
        return presenters.get(id);
    }

    /**
     * Select presenter.
     *
     * @param token the token
     * @see org.ebayopensource.turmeric.monitoring.client.Controller#selectPresenter(org.ebayopensource.turmeric.monitoring.client.model.HistoryToken)
     */
    public void selectPresenter(HistoryToken token) {
        //we want to select the current tab presenter based on the history token
        if (token != null) {
            String id = HistoryToken.getValue(token, TAB);
           
            if (id != null) {
                Presenter p = getPresenter(id);         
                p.go(this.view, token);
            }
            else {
                History.newItem(newTabHistoryToken(this.defaultPresenter.getId()).toString());
            }
        } 
    }

    private void initTabPresenters () {
        ServicePresenter servicePresenter = new ServicePresenter(this.eventBus, new ServiceView(this.view), queryService);
        this.defaultPresenter = servicePresenter;
        this.presenters.put(servicePresenter.getId(), servicePresenter);
        
        ConsumerPresenter consumerPresenter = new ConsumerPresenter(this.eventBus, new ConsumerView(this.view), queryService);
        this.presenters.put(consumerPresenter.getId(), consumerPresenter);
        
        ErrorPresenter errorPresenter = new ErrorPresenter(this.eventBus, new ErrorView(this.view), queryService);
        this.presenters.put(errorPresenter.getId(), errorPresenter);
    }
    
    private HistoryToken newTabHistoryToken (String tabPresenterId) {
        HistoryToken t = HistoryToken.newHistoryToken(DASH_ID);
        t.addValue(TAB, tabPresenterId);
        return t;
    }
}
