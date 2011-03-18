/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.event.LoginEvent;
import org.ebayopensource.turmeric.monitoring.client.event.LoginEventHandler;
import org.ebayopensource.turmeric.monitoring.client.event.LoginFailureEvent;
import org.ebayopensource.turmeric.monitoring.client.event.LoginFailureEventHandler;
import org.ebayopensource.turmeric.monitoring.client.event.LoginSuccessEvent;
import org.ebayopensource.turmeric.monitoring.client.event.LoginSuccessEventHandler;
import org.ebayopensource.turmeric.monitoring.client.event.LogoutEvent;
import org.ebayopensource.turmeric.monitoring.client.event.LogoutEventHandler;
import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.model.pes.PolicyEnforcementService;
import org.ebayopensource.turmeric.monitoring.client.model.pes.PolicyEnforcementService.VerifyAccessResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.OperationKey;
import org.ebayopensource.turmeric.monitoring.client.presenter.MenuController;
import org.ebayopensource.turmeric.monitoring.client.presenter.Presenter;
import org.ebayopensource.turmeric.monitoring.client.presenter.SplashPresenter;
import org.ebayopensource.turmeric.monitoring.client.shared.AppUser;
import org.ebayopensource.turmeric.monitoring.client.util.AppKeyUtil;
import org.ebayopensource.turmeric.monitoring.client.util.PresenterUtil;
import org.ebayopensource.turmeric.monitoring.client.view.SplashView;
import org.ebayopensource.turmeric.monitoring.client.view.common.ApplicationMenuView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
		//See if a security cookie has been stored to identify the user
		 AppUser user = AppUser.fromCookie(Cookies.getCookie(AppKeyUtil.COOKIE_SESSID_KEY));
		if ( user!= null) {
		    //the user has been identified take them to the last page they were on
		    //unless that was not saved
			if (token == null || token.getPresenterId() == null) {
				//no history, default landing page is dashboard
				History.newItem(HistoryToken.newHistoryToken(MenuController.PRESENTER_ID, null).toString());
			}
			
			History.fireCurrentHistoryState();
		} else {
		    //user not identified, go to the "login" page
			PresenterUtil.forceRedirectToPresenter(token, presenters.get(SplashPresenter.SPLASH_ID));
		}
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		
		final HistoryToken token = HistoryToken.newHistoryToken(event.getValue());

		if (AppUser.getUser() != null) {
			// identified
			if (SplashPresenter.SPLASH_ID.equals(token.getPresenterId())) {
				return;
			}
		} else {
		    //not identified, force to the "login" page
			if (!SplashPresenter.SPLASH_ID.equals(token.getPresenterId())) {
		        return;
		    }
		}

		selectPresenter(token);
	}

	private void bind() {
		//if login succeeded
		this.eventBus.addHandler(LoginSuccessEvent.TYPE,
				new LoginSuccessEventHandler() {
			public void onSuccess (AppUser arg) {
				
				AppController.this.rootContainer.clear();
			    History.newItem(HistoryToken.newHistoryToken(MenuController.PRESENTER_ID, null).toString());
			}
		});
		
		//if login failed
		this.eventBus.addHandler(LoginFailureEvent.TYPE,
			new LoginFailureEventHandler() {
				public void onFailure() {
					SplashPresenter concreteSplashPresenter = (SplashPresenter) presenters.get(SplashPresenter.SPLASH_ID);
					concreteSplashPresenter.handleLoginErrorView();
				}
		});
		
		
		//logout
		this.eventBus.addHandler(LogoutEvent.TYPE,
				new LogoutEventHandler() {
			public void onLogout (LogoutEvent event) {
			 				
				Cookies.removeCookie(AppKeyUtil.COOKIE_SESSID_KEY);
				AppUser.logout();

				//Clean up 
				cleanup();
				
				initPresenters();
				
				//Go back to the splash screen
				HistoryToken token = HistoryToken.newHistoryToken();
				PresenterUtil.forceRedirectToPresenter(token, presenters.get(SplashPresenter.SPLASH_ID));
				
				SplashPresenter concreteSplashPresenter = (SplashPresenter) presenters.get(SplashPresenter.SPLASH_ID);
				concreteSplashPresenter.handleLogoutSuccessView();
			}
		});
		
		// login event handler
		this.eventBus.addHandler(LoginEvent.TYPE,
				new LoginEventHandler() {
			public void onLogin(LoginEvent event) {
				// TODO there is no service to authenticate the username/password
			    //This info has to be presented on every request, so the only
			    //time we find out if login/password is accepted is when we supply
			    //it on a request.
			    AppUser user = AppUser.newAppUser(event.getLogin(), event.getPassword(), event.getDomain());
			    Cookies.removeCookie(AppKeyUtil.COOKIE_SESSID_KEY);

			    
				Map<String, String> credentials = new HashMap<String, String>();
				credentials.put("X-TURMERIC-SECURITY-PASSWORD", AppUser.getUser()
						.getPassword());
				OperationKey opKey = new OperationKey();
				
				opKey.setResourceName(PolicyEnforcementService.POLICY_SERVICE_NAME);
				opKey.setOperationName("");
				opKey.setResourceType("OBJECT");

				List<String> policyTypes = Collections.singletonList("AUTHZ");

				String[] subjectType = { "USER", AppUser.getUser().getUsername() };
				List<String[]> subjectTypes = Collections.singletonList(subjectType);

				PolicyEnforcementService enforcementService = (PolicyEnforcementService) serviceMap
				.get(SupportedService.POLICY_ENFORCEMENT_SERVICE);
				
				enforcementService.verify(opKey, policyTypes, credentials,
						subjectTypes, null, null, null,
						new AsyncCallback<VerifyAccessResponse>() {

							public void onFailure(Throwable arg) {
							    AppController.this.eventBus.fireEvent(new LoginFailureEvent());
							}

							public void onSuccess(VerifyAccessResponse response) {
							    AppController.this.eventBus.fireEvent(new LoginSuccessEvent(AppUser.getUser()));
							
							}
						});
			    
			}
		});
	}
	
	private void initPresenters () {
		Presenter presenter = null;
		//splash page
		presenter = new SplashPresenter(this.eventBus, new SplashView());
		addPresenter(presenter.getId(), presenter);
		//main page
		presenter = new MenuController(this.eventBus, rootContainer, new ApplicationMenuView(), serviceMap);
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
