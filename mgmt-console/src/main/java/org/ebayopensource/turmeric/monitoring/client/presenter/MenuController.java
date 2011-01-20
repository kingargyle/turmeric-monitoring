/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.presenter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.Container;
import org.ebayopensource.turmeric.monitoring.client.Controller;
import org.ebayopensource.turmeric.monitoring.client.SupportedService;
import org.ebayopensource.turmeric.monitoring.client.event.LogoutEvent;
import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;
import org.ebayopensource.turmeric.monitoring.client.presenter.policy.PolicyController;
import org.ebayopensource.turmeric.monitoring.client.shared.AppUser;
import org.ebayopensource.turmeric.monitoring.client.view.DashboardContainer;
import org.ebayopensource.turmeric.monitoring.client.view.PolicyContainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * This presenter contains top level menu of the application.
 * (1) Monitoring Console
 * (2) Policy Admin
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
	    HasChangeHandlers getAppSelectionChange();
	    void setAvailableApps(Map<String, String> apps);
	    String getSelectedApp();
	    void setSelectedApp(String useraction);
	    void setUserName(String name);
	}
	
	public MenuController(HandlerManager eventBus, HasWidgets rootContainer, MenuControllerDisplay view, Map<SupportedService, ConsoleService> serviceMap) {
		this.eventBus = eventBus;
		this.view = view;
		this.serviceMap = serviceMap;
		this.rootContainer = rootContainer;
		
		initPresenters();
		bind();
	}

	public void go(HasWidgets container, HistoryToken token) {
	    //only add ourselves to the root window the first time we are activated
	    if (!added) {
	        added =true;
	        rootContainer.add(this.view.asWidget());
	        this.view.setAvailableApps(getAvailableApps());
	        this.view.setUserName((AppUser.getUser()==null?"":AppUser.getUser().getUsername()));
	    }
	    

	    //try my sub presenters
	    String id = token.getPresenterId();
	    if (id != null && !PRESENTER_ID.equals(id))
	        selectPresenter(token);

	}

	public String getId() {
		return PRESENTER_ID;
	}

	public void bind() {
	    //listen for change to selected app
	    this.view.getAppSelectionChange().addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                String selectedApp = view.getSelectedApp();
                if (selectedApp == null)
                    return;
                UserAction ua = UserAction.valueOf(selectedApp);
                Presenter p = actionMap.get(ua);
                if (p != null) {
                    HistoryToken tok = HistoryToken.newHistoryToken(p.getId(), null);
                    History.newItem(tok.toString());
                }
            }  
	    });
	    
		
		//listen for logout
		this.view.getLogoutComponent().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {    
                eventBus.fireEvent(new LogoutEvent());
            }
		});
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
           
            UserAction ua = getActionForPresenter(presenter);
            if (ua != null)
                view.setSelectedApp(ua.toString());
                
           //Pass in this view so that all presenter/view pairs are children
           //of the MenuController's view (get constant header/footer wrapping)
            presenter.go(view, token);
        }
	}
	
	public void initPresenters() {
		Presenter dp = new DashboardPresenter (eventBus, new DashboardContainer(), (MetricsQueryService) this.serviceMap.get(SupportedService.METRICS_QUERY_SERVICE));
		addPresenter(dp.getId(), dp);
		actionMap.put(UserAction.METRICS_MAIN, dp);
		Presenter pp = new PolicyController(eventBus, new PolicyContainer(), this.serviceMap);
		addPresenter(pp.getId(), pp);
		actionMap.put(UserAction.POLICY_MAIN, pp);
	}

	private Map<String, String> getAvailableApps () {
	    Map<String, String> apps = new HashMap<String, String>();
	    apps.put(ConsoleUtil.policyAdminConstants.policyAdministration(), UserAction.POLICY_MAIN.toString());
	    apps.put(ConsoleUtil.constants.metricsMonitoring(), UserAction.METRICS_MAIN.toString());
	    return apps;
	}
	
	
	private UserAction getActionForPresenter (Presenter presenter) {
	    UserAction ua = null;
	    for (Map.Entry<UserAction, Presenter> e: actionMap.entrySet()) {
	        if (e.getValue().getId().equals(presenter.getId()))
	            ua = e.getKey();
	    }
	    return ua;
	}
}
