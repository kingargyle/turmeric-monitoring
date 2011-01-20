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
import org.ebayopensource.turmeric.monitoring.client.event.LogoutEvent;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyController;
import org.ebayopensource.turmeric.monitoring.client.view.ConsumerView;
import org.ebayopensource.turmeric.monitoring.client.view.ErrorView;
import org.ebayopensource.turmeric.monitoring.client.view.ServiceView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class DashboardPresenter implements Presenter, Controller {
    public static final String DASH_ID = "dash";
    public static final String TAB = "tab";
    protected HandlerManager eventBus;
    protected Dashboard view;
    protected MetricsQueryService queryService;
    protected TabPresenter defaultPresenter;
    protected Map<String,TabPresenter> presenters = new HashMap<String,TabPresenter>();
    protected boolean added;
    protected HasWidgets container;
  
    
    public DashboardPresenter (HandlerManager eventBus, Dashboard view, MetricsQueryService queryService) {
        this.eventBus = eventBus;
        this.view = view;
        this.queryService = queryService;
        initTabPresenters();
        bind();
    }
    
    public String getId() {
        return DASH_ID;
    }

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
                
                //TODO in creating a new history item every time we click a tab,
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
        
        /*
        //listen for logout
        this.view.getLogout().addClickHandler(new ClickHandler() {   
            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new LogoutEvent());
            }
        });
        
        //listen for change to policy app
        this.view.getPolicyApp().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                //navigating away
                added = false;
                if (container != null)
                    container.remove(view.asWidget());
                HistoryToken tok = HistoryToken.newHistoryToken(PolicyController.PRESENTER_ID, null);
                History.newItem(tok.toString());
            }
        });
 */
    }

    public void go(HasWidgets container, HistoryToken token) {
        this.container = container;
        //if (!added) {
            container.add(this.view.asWidget());
            added = true;
        //}

        selectPresenter(token);
    }

    public void addPresenter(String id, Presenter p) {
        presenters.put(id, (TabPresenter)p);
    }

    public Presenter getPresenter(String id) {
        return presenters.get(id);
    }
    
    public TabPresenter getTabPresenter(String id) {
        return presenters.get(id);
    }

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
