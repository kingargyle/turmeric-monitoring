/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client;

import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.presenter.MenuController;
import org.ebayopensource.turmeric.monitoring.client.presenter.Presenter;
import org.ebayopensource.turmeric.monitoring.client.view.common.ApplicationMenuView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController implements Controller, ValueChangeHandler<String>{
	
	protected HandlerManager eventBus;
	protected HasWidgets rootContainer;
	protected Map<String, Presenter> presenters = new HashMap<String, Presenter>();
	protected Map<SupportedService, ConsoleService> serviceMap;
	
	public AppController(HandlerManager eventBus, HasWidgets rootContainer, Map<SupportedService, ConsoleService> serviceMap) {
		this.eventBus = eventBus;
		this.rootContainer = rootContainer;
		this.serviceMap = serviceMap;
		
		initPresenters();
	}
	
	/**
	 *  Start the application.
	 */
	public void start () {
		bind();
		History.addValueChangeHandler(this);
		final HistoryToken token = HistoryToken.newHistoryToken();
		selectPresenter(token);
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		Window.alert("AppController.onValueChange. event.getValue = "+event.getValue());
		final HistoryToken token = HistoryToken.newHistoryToken(event.getValue());

//		if (AppUser.getUser() != null) {
//			// identified
//			if (SplashPresenter.SPLASH_ID.equals(token.getPresenterId())) {
//				return;
//			}
//		} else {
//		    //not identified, force to the "login" page
//			if (!SplashPresenter.SPLASH_ID.equals(token.getPresenterId())) {
//		        return;
//		    }
//		}

		selectPresenter(token);
	}

	private void bind() {
	}
	
	private void initPresenters () {
		Presenter presenter = new MenuController(this.eventBus, rootContainer, new ApplicationMenuView(), serviceMap);
		addPresenter(presenter.getId(), presenter);
	}
	
	private void cleanup () {
	    this.rootContainer.clear(); //get rid of whatever is being displayed
	    this.presenters.clear();
	}

    public void addPresenter(String id, Presenter p) {
        this.presenters.put(id, p);
    }

    public Presenter getPresenter(String id) {
        return this.presenters.get(id);
    }

    public void selectPresenter(HistoryToken token) {
    	String presenterId = token != null ? token.getPresenterId() : null;
        Presenter presenter = null;
        presenter = this.presenters.get(presenterId);
        if (presenter != null) {
            presenter.go(this.rootContainer, token);
        }
        else {
            presenter = presenters.get(MenuController.PRESENTER_ID);
            if (presenter != null)
                presenter.go(this.rootContainer, token);
        }
    }
}
