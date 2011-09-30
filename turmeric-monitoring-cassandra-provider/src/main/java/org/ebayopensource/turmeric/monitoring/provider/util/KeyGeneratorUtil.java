/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jamuguerza
 * 
 */
public class KeyGeneratorUtil {

	private static final String KEY_TOKEN = "|"; 

	private static final String KEY_ALL = "All"; 


	/**
	 * Generate.
	 *
	 * @param serverSide the server side
	 * @param filters the filters
	 * @param param1 Category or Severity filter, One of them, never both
	 * @return the list
	 */
	public static List<String> generateErrorValuesKeys(final boolean serverSide,
			final Map<String, List<String>> filters, final String param1) {
		final List<String> errorKeys = new ArrayList<String>();

		final List<String> serverNames = new ArrayList<String>(
				filters.get("Server"));
		final List<String> serviceAdminNames = new ArrayList<String>(
				filters.get("Service"));
		final List<String> operationNames = new ArrayList<String>(
				filters.get("Operation"));
		final List<String> consumerNames = new ArrayList<String>(
				filters.get("Consumer"));

		if (serverNames == null || serverNames.isEmpty()) {
			serverNames.add(KEY_ALL );
		}
		if (serviceAdminNames == null || serviceAdminNames.isEmpty()) {
			serviceAdminNames.add(KEY_ALL );
		}
		if (consumerNames == null || consumerNames.isEmpty()) {
			consumerNames.add(KEY_ALL );
		}
		if (operationNames == null || operationNames.isEmpty()) {
			operationNames.add(KEY_ALL );
		}

		for (String serverName : serverNames) {
			for (String serviceName : serviceAdminNames) {
				for (String consumerName : consumerNames) {
					for (String operationName : operationNames) {
						errorKeys.add(serverName + KEY_TOKEN + serviceName + KEY_TOKEN +  consumerName
								+ KEY_TOKEN +  operationName + KEY_TOKEN + param1 + KEY_TOKEN +  serverSide);
					}
				}
			}
		}

		return errorKeys;
	}
}
