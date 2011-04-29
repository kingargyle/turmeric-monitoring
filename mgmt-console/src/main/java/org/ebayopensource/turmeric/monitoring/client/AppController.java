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

/**
 * Base Controller class in the application.
 */
public class AppController implements Controller, ValueChangeHandler<String>{
	
    /** Base event bus for comunicatting to listener the multiple events in the system. */
	protected HandlerManager eventBus;
	
	/** The root container. */
	protected HasWidgets rootContainer;
	
	/** Map of registered presenters. */
	protected Map<String, Presenter> presenters = new HashMap<String, Presenter>();
	
	/** The service map. */
	protected Map<SupportedService, ConsoleService> serviceMap;
	
	/**
	 * Instantiates a new app controller.
	 *
	 * @param eventBus the event bus
	 * @param rootContainer the root container
	 * @param serviceMap the service map
	 */
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

	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
	 */
	public void onValueChange(ValueChangeEvent<String> event) {
		final HistoryToken token = HistoryToken.newHistoryToken(event.getValue());
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

	/**
	 * Adds the presenter.
	 *
	 * @param id The Presenter id
	 * @param p the Presenter object to be added to {@link #presenters}
	 */
    public void addPresenter(String id, Presenter p) {
        this.presenters.put(id, p);
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.Controller#getPresenter(java.lang.String)
     */
    public Presenter getPresenter(String id) {
        return this.presenters.get(id);
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.Controller#selectPresenter(org.ebayopensource.turmeric.monitoring.client.model.HistoryToken)
     */
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
