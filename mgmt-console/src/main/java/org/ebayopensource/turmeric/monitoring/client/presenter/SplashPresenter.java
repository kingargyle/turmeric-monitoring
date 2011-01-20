/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.presenter;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.event.LoginEvent;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;

public class SplashPresenter implements Presenter {
	
	public final static String SPLASH_ID = "Splash";
	protected Display view;
	protected HandlerManager eventBus;
	

	public interface Display extends org.ebayopensource.turmeric.monitoring.client.Display{
		HasClickHandlers getSubmitButton();
		HasValue<String> getLogin();
		HasValue<String> getPassword();
		HasValue<String> getDomain();
		void promptMessage(String message);
	}

	public SplashPresenter (HandlerManager eventBus, Display view) {
		this.view = view;
		this.eventBus = eventBus;
	    bind();
	}

	public void go(final HasWidgets container, HistoryToken token) {
		container.clear();
		this.view.activate();
		container.add(this.view.asWidget());
	}
	
	public String getId()
	{
		return SPLASH_ID;
	}
	
	public void bind() {
		this.view.getSubmitButton().addClickHandler(new ClickHandler() {   
			public void onClick(ClickEvent event) {
				SplashPresenter.this.eventBus.fireEvent(new LoginEvent(view.getLogin().getValue(), 
				                                                       view.getPassword().getValue(),
				                                                       view.getDomain().getValue()));
			}
		});
	}
	
	// TODO - need to handle this properly
	public void handleLoginErrorView() {
		view.getPassword().setValue("");
		view.promptMessage(ConsoleUtil.messages.loginFailed());
	}
	
	public void handleLogoutSuccessView() {
		view.promptMessage(ConsoleUtil.messages.logoutSuccessful());
	}
}
