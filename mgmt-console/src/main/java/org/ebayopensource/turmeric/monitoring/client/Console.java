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
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryServiceImpl;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
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
        final HandlerManager eventBus = new HandlerManager(null);

        Map<SupportedService, ConsoleService> serviceMap = createServiceMap();
        AppController pageController = new AppController(eventBus, RootLayoutPanel.get(), serviceMap);
        pageController.start();
    }
	
    // wrap the services. not sure how many services will be used later.
	private Map<SupportedService, ConsoleService> createServiceMap() {
		Map<SupportedService, ConsoleService> serviceMap = new HashMap<SupportedService, ConsoleService>();
		serviceMap.put(SupportedService.METRICS_QUERY_SERVICE, new MetricsQueryServiceImpl());
		return serviceMap;
	}
}
