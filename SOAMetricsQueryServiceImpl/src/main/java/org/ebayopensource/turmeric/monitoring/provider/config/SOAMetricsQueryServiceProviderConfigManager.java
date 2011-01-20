/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.config;

import org.ebayopensource.turmeric.monitoring.exception.ConfigurationException;
import org.w3c.dom.Element;

public class SOAMetricsQueryServiceProviderConfigManager extends BaseConfigManager
{
	private static final String CONFIG_FILENAME = "SOAMetricsQueryServiceProviderConfig.xml";
	private static final String SCHEMA_FILENAME = "SOAMetricsQueryServiceProviderConfig.xsd";
	private static final String ROOT_ELEMENT = "metrics-query-service-provider-config";
    private static SOAMetricsQueryServiceProviderConfigManager s_instance = null;

	private SOAMetricsQueryServiceProviderConfig m_config;



	public static SOAMetricsQueryServiceProviderConfigManager getInstance() {
    	if (s_instance == null) {
    		s_instance = new SOAMetricsQueryServiceProviderConfigManager();
    	}
    	return s_instance;
    }


	public synchronized SOAMetricsQueryServiceProviderConfig getConfig() throws ConfigurationException
    {
		loadConfig();
		return m_config;
	}

	public synchronized SOAMetricsQueryServiceProviderConfig getConfigForUpdate() throws ConfigurationException {
		loadConfig();
		if (m_config == null) {
			return null;
		}
		return m_config.copy();
	}


	public void map(Element rootData) throws ConfigurationException {
		// if no rootData, reset config to null (clean up previous instance)
		if (rootData == null) {
			m_config = null;
		} else {
			m_config = new SOAMetricsQueryServiceProviderConfig();
			SOAMetricsQueryServiceProviderConfigMapper.map(getConfigFileName(), rootData, m_config);
		}
	}

	public String getConfigFileName() {
		return CONFIG_FILENAME;
	}

	public String getSchemaFileName() {
		return SCHEMA_FILENAME;

	}

	public String getRootElementName() {
		return ROOT_ELEMENT;
	}

}
