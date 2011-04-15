/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client;

import java.util.Map;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Constants.DefaultStringValue;

public interface ConsoleConstants extends Constants {
	@DefaultStringValue("Turmeric")
	public String turmeric();

	@DefaultStringValue("Turmeric Management Console")
	public String title();

	@DefaultStringValue("Submit")
	public String submit();

	@DefaultStringValue("Apply")
	public String apply();

	@DefaultStringValue("Save")
	public String save();

	@DefaultStringValue("Cancel")
	public String cancel();

	@DefaultStringValue("Enter")
	public String enter();

	@DefaultStringValue("Clear")
	public String clear();

	@DefaultStringValue("Pools")
	public String pools();

	@DefaultStringValue("Top Volume")
	public String topVolume();

	@DefaultStringValue("Least Performance")
	public String leastPerformance();

	@DefaultStringValue("Call Trend")
	public String callTrend();

	@DefaultStringValue("Performance Trend")
	public String performanceTrend();

	@DefaultStringValue("Top Errors")
	public String topErrors();

	@DefaultStringValue("Consumer Traffic")
	public String consumerTraffic();

	@DefaultStringValue("Consumer Errors")
	public String consumerErrors();

	@DefaultStringValue("Call Volume")
	public String callVolume();

	@DefaultStringValue("Performance")
	public String performance();

	@DefaultStringValue("Top Service Errors")
	public String topServiceErrors();

	@DefaultStringValue("Top Consumer Errors")
	public String topConsumerErrors();

	@DefaultStringValue("Summary")
	public String summary();

	@DefaultStringValue("Services")
	public String services();

	@DefaultStringValue("Service")
	public String service();

	@DefaultStringValue("Operations")
	public String operations();

	@DefaultStringValue("Operation")
	public String operation();

	@DefaultStringValue("Errors")
	public String errors();

	@DefaultStringValue("Error")
	public String error();

	@DefaultStringValue("Consumers")
	public String consumers();

	@DefaultStringValue("Count")
	public String count();

	@DefaultStringValue("Change")
	public String change();

	@DefaultStringValue("Average")
	public String average();

	@DefaultStringValue("Total Services")
	public String totalServices();

	@DefaultStringValue("View metrics for")
	public String viewMetricsFor();

	@DefaultStringValue("First date")
	public String firstDate();

	@DefaultStringValue("Second date")
	public String secondDate();

	@DefaultStringValue("and")
	public String and();

	@DefaultStringValue("Go")
	public String go();

	@DefaultStringValue("at")
	public String at();

	@DefaultStringValue("Over")
	public String over();

	@DefaultStringValue("hr")
	public String hr();

	@DefaultStringValue("Ok")
	public String ok();

	@DefaultStringValue("Download")
	public String download();

	@DefaultStringValue("Results")
	public String results();

	@DefaultStringValue("A server error has occurred")
	public String serverError();

	@DefaultStringValue("Select Metrics")
	public String selectMetrics();

	@DefaultStringValue("View Errors By")
	public String viewErrorsBy();

	@DefaultStringValue("More")
	public String more();

	@DefaultStringValue("Italiano")
	public String italian();

	@DefaultStringValue("English")
	public String english();

	@DefaultStringValue("Deutsch")
	public String german();

	@DefaultStringValue("Fran\u00E7ais")
	public String french();

	@DefaultStringValue("Category")
	public String category();

	@DefaultStringValue("Severity")
	public String severity();

	@DefaultStringValue("Top Application Errors")
	public String topApplicationErrors();

	@DefaultStringValue("Top Request Errors")
	public String topRequestErrors();

	@DefaultStringValue("Top System Errors")
	public String topSystemErrors();

	@DefaultStringValue("Top Critical Errors")
	public String topCriticals();

	@DefaultStringValue("Top Warnings")
	public String topWarnings();

	@DefaultStringValue("Consumer Error")
	public String consumerError();

	@DefaultStringValue("Top Severity Errors")
	public String topSeverityErrors();

	@DefaultStringValue("Top Category Errors")
	public String topCategoryErrors();

	@DefaultStringValue("Errors to Calls")
	public String errorsToCalls();

	@DefaultStringValue("Name")
	public String name();

	@DefaultStringValue("Domain")
	public String domain();

	@DefaultStringValue("Subdomain")
	public String subdomain();

	@DefaultStringValue("Select Filter Criteria")
	public String selectFilterCriteria();

	@DefaultStringValue("Information")
	public String information();

	@DefaultStringValue("Create")
	public String create();

	@DefaultStringValue("Delete")
	public String delete();

	public Map metricNameMap();

	public Map errorViewTypeMap();

	@DefaultStringValue("Metrics Monitoring")
	public String metricsMonitoring();

	@DefaultStringValue("Unknown")
	public String unknown();
	
	@DefaultStringValue("Confirm")
	public String confirm();
	
}
