/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.ConsoleService;
import org.ebayopensource.turmeric.monitoring.client.model.DummyMetricsQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.client.model.pes.PolicyEnforcementServiceImpl;
import org.ebayopensource.turmeric.monitoring.client.model.policy.DummyPolicyQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyServiceImplTest;
import org.ebayopensource.turmeric.monitoring.client.shared.AppUser;
import org.ebayopensource.turmeric.monitoring.client.util.AppKeyUtil;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;


/**
 * Entry point for Turmeric Management Console app.
 */
public class Console implements EntryPoint
{
	/**
     * This is the entry point method.
     */
    public void onModuleLoad()
    { 
        Window.setTitle(ConsoleUtil.constants.title());
        Window.addCloseHandler(new CloseHandler<Window> () {

            public void onClose(CloseEvent<Window> event) {
                //briefly save the login/password combo
                //NOTE when using gwt hosted mode, which is sloooow, the
                //cookie will already have expired before the reload is
                //completed, so don't expect this to work in hosted mode
                Date expiry = new Date(System.currentTimeMillis()+1000);

                //TODO change to using secure cookies only!
                String cookie = AppUser.toCookie();
                if (cookie != null) {
                    Cookies.setCookie(AppKeyUtil.COOKIE_SESSID_KEY, cookie, expiry);
                                      //expiry, null, null, false);
                } 
            }
        });

        final HandlerManager eventBus = new HandlerManager(null);

        Map<SupportedService, ConsoleService> serviceMap = createServiceMap();
        AppController pageController = new AppController(eventBus, RootLayoutPanel.get(), serviceMap);
        pageController.start();
    }
	
    // wrap the services. not sure how many services will be used later.
	private Map<SupportedService, ConsoleService> createServiceMap() {
		Map<SupportedService, ConsoleService> serviceMap = new HashMap<SupportedService, ConsoleService>();
		serviceMap.put(SupportedService.METRICS_QUERY_SERVICE, new MetricsQueryServiceImpl());
		//serviceMap.put(SupportedService.POLICY_QUERY_SERVICE, new PolicyQueryServiceImpl());
		serviceMap.put(SupportedService.POLICY_ENFORCEMENT_SERVICE, new PolicyEnforcementServiceImpl());
		return serviceMap;
	}
}
