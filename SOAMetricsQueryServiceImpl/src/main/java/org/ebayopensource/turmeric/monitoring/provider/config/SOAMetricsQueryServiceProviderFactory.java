/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ebayopensource.turmeric.monitoring.exception.ConfigurationException;
import org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider;
import org.ebayopensource.turmeric.monitoring.util.ReflectionUtil;
import org.ebayopensource.turmeric.runtime.common.exceptions.ErrorUtils;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.utils.LogManager;
import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;

public class SOAMetricsQueryServiceProviderFactory {
	private static Map<String, SOAMetricsQueryServiceProvider>  s_serviceDMProviderMap = new HashMap<String, SOAMetricsQueryServiceProvider>();
	private static Set<String> s_failedProviders = new HashSet<String>();
	private static String s_defaultProviderKey;
	private static volatile CommonErrorData s_errorData;
	private static Logger s_Logger = LogManager.getInstance(SOAMetricsQueryServiceProviderFactory.class);

	static {
		// static initialization
		SOAMetricsQueryServiceProviderConfigManager configMngr = SOAMetricsQueryServiceProviderConfigManager.getInstance();
		try {
			s_defaultProviderKey = configMngr.getConfig().getDefaultProvider();
		} catch (ConfigurationException e) {
			s_errorData = getConfigError(configMngr);
		}
	}

	// disable creating instances
	private SOAMetricsQueryServiceProviderFactory() {

	}

	public static SOAMetricsQueryServiceProvider create() throws ServiceException
    {
		return create(s_defaultProviderKey);
	}

	public static SOAMetricsQueryServiceProvider create(String providerKey) throws ServiceException {

		if (s_errorData != null)
			throw new ServiceException(s_errorData);

		SOAMetricsQueryServiceProvider providerImpl = s_serviceDMProviderMap.get(providerKey);
		SOAMetricsQueryServiceProviderConfigManager configMngr = SOAMetricsQueryServiceProviderConfigManager.getInstance();

		if (providerImpl == null) {
			// check the failed set
			if (s_failedProviders.contains(providerKey)) {
				new ServiceException(getConfigError(configMngr));
			}
			synchronized (SOAMetricsQueryServiceProviderFactory.class) {
				providerImpl = s_serviceDMProviderMap.get(providerKey);
				if (providerImpl == null) {
					try {
						String providerImplClassName = configMngr.getConfig().getProviderImplClassName(providerKey);
						if (providerImplClassName != null) {
							providerImpl = getServiceDataModelProviderInstance(providerImplClassName);
							s_serviceDMProviderMap.put(providerKey, providerImpl);
						}
					} catch (ConfigurationException ce) {
						s_Logger.log(Level.SEVERE, "invalid configuration" , ce);
					}
				}
				if (providerImpl == null) {
					s_failedProviders.add(providerKey);
				}
			}

			if (providerImpl == null) {
				throw new ServiceException(getConfigError(configMngr));
			}
		}

		return providerImpl;
	}

	private static SOAMetricsQueryServiceProvider getServiceDataModelProviderInstance(String metricsQueryServiceProviderClassName) {

		SOAMetricsQueryServiceProvider serviceDMProviderImpl = null;
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			serviceDMProviderImpl = ReflectionUtil.createInstance(metricsQueryServiceProviderClassName, SOAMetricsQueryServiceProvider.class, cl);

		} catch (Exception e) {
			s_Logger.log(Level.SEVERE,
					"The SOAMetricsQueryServiceDMProvider class name: "
						+ metricsQueryServiceProviderClassName + " is invalid",
					e);

		}
		return serviceDMProviderImpl;
	}

	private static CommonErrorData getConfigError(
			SOAMetricsQueryServiceProviderConfigManager configMngr) {
		return ErrorUtils.createErrorData(
                "MetricsQueryServiceError",
                "TurmericMonitoring",
                new Object[]{new String("SOAMetricsQueryService"),
                        configMngr.getConfigPath() +
                                configMngr.getConfigFileName()});
	}

	/*public static void main(String args[]){
		SOAMetricsQueryServiceProvider provider;
		try {
			provider = SOAMetricsQueryServiceProviderFactory.create("intalio");
			System.out.println(provider);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/





}
