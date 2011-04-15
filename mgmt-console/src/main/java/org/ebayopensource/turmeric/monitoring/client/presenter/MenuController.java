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

import org.ebayopensource.turmeric.monitoring.client.Container;
import org.ebayopensource.turmeric.monitoring.client.Controller;
import org.ebayopensource.turmeric.monitoring.client.SupportedService;
import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.view.DashboardContainer;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * This presenter contains top level menu of the application. 
 * 
 * @author nuy
 * 
 */
public class MenuController implements Presenter, Controller {

    public final static String PRESENTER_ID = "Menu";

    protected HandlerManager eventBus;
    protected MenuControllerDisplay view;
    protected HasWidgets rootContainer;
    protected boolean added;

    protected Map<String, Presenter> presenters = new HashMap<String, Presenter>();
    protected Map<UserAction, Presenter> actionMap = new HashMap<UserAction, Presenter>();
    protected Map<SupportedService, ConsoleService> serviceMap;

    public interface MenuControllerDisplay extends Container {
        HasClickHandlers getLogoutComponent();

        void setUserName(String name);
    }

    public MenuController(HandlerManager eventBus, HasWidgets rootContainer, MenuControllerDisplay view,
                    Map<SupportedService, ConsoleService> serviceMap) {
        this.eventBus = eventBus;
        this.view = view;
        this.serviceMap = serviceMap;
        this.rootContainer = rootContainer;

        initPresenters();
        bind();
    }

    public void go(HasWidgets container, HistoryToken token) {
        String id = null;

        // only add ourselves to the root window the first time we are activated
        if (!added) {
            added = true;
            rootContainer.add(this.view.asWidget());
        }

        if (token != null) {
            // try my sub presenters
            id = token.getPresenterId();
        }
        // Window.alert("MenuController: presenter id = "+id);
        if (id != null && !PRESENTER_ID.equals(id)) {
            selectPresenter(token);
        }
        else {
            HistoryToken tok = HistoryToken.newHistoryToken(DashboardPresenter.DASH_ID, null);
            History.newItem(tok.toString());
            selectPresenter(tok);
        }

    }

    public String getId() {
        return PRESENTER_ID;
    }

    public void bind() {
    }

    public void addPresenter(String id, Presenter p) {
        presenters.put(id, p);
    }

    public Presenter getPresenter(String id) {
        return presenters.get(id);
    }

    public void selectPresenter(HistoryToken token) {
        String presenterId = token != null ? token.getPresenterId() : null;

        Presenter presenter = presenters.get(presenterId);
        if (presenter != null) {
            // Pass in this view so that all presenter/view pairs are children
            // of the MenuController's view (get constant header/footer wrapping)
            presenter.go(view, token);
        }
    }

    public void initPresenters() {
        Presenter dp = new DashboardPresenter(eventBus, new DashboardContainer(),
                        (MetricsQueryService) this.serviceMap.get(SupportedService.METRICS_QUERY_SERVICE));
        addPresenter(dp.getId(), dp);
        actionMap.put(UserAction.METRICS_MAIN, dp);
    }

}
