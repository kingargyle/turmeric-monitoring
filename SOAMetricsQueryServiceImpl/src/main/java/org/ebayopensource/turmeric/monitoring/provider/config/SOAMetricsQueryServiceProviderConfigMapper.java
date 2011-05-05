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
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.exception.ConfigurationException;
import org.ebayopensource.turmeric.monitoring.util.DomParseUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The Class SOAMetricsQueryServiceProviderConfigMapper.
 */
public class SOAMetricsQueryServiceProviderConfigMapper {
	
	/**
	 * Map subject group service configuration.
	 *
	 * @param filename the filename
	 * @param topLevel the top level
	 * @param dst the dst
	 * @throws ConfigurationException the configuration exception
	 */
	public static void map(String filename, Element topLevel, SOAMetricsQueryServiceProviderConfig dst) throws ConfigurationException
    {

		if (topLevel == null) {
			return;
		}
		mapMetricsQueryServiceProviderConfig(filename, topLevel, dst);
	}

	/**
	 * Map metrics query service provider config.
	 *
	 * @param filename the filename
	 * @param metricsQueryServiceConfigProvider the metrics query service config provider
	 * @param dst the dst
	 * @throws ConfigurationException the configuration exception
	 */
	public static void mapMetricsQueryServiceProviderConfig(String filename, Element metricsQueryServiceConfigProvider, SOAMetricsQueryServiceProviderConfig dst) throws ConfigurationException {
		try {
			String defProviderKey = DomParseUtil.getElementText(filename, metricsQueryServiceConfigProvider, "default");
			dst.setDefaultProvider(defProviderKey);

			Element providerConfigList = DomParseUtil.getSingleElement(filename, metricsQueryServiceConfigProvider, "provider-config-list");
			if (providerConfigList != null) {
				NodeList nodeList = DomParseUtil.getImmediateChildrenByTagName(providerConfigList, "provider-config");
				dst.setProviderMap(getProviderMap(filename, nodeList));
			}
		} catch(Exception e) {
			throw new ConfigurationException(
					/*SecuritySystemErrorTypes.SYS_CONFIG_MAP_DATA_ERROR,
					"Error in mapping metricsqueryservice config: " + e.getMessage(),
					e*/
					e.getMessage(), e);
		}
	}

	private static Map<String, String> getProviderMap(String filename, NodeList nodeList) throws Exception {
		Map<String, String> policyTypeMap = new HashMap<String, String>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element item = (Element)nodeList.item(i);
			String type = DomParseUtil.getElementText(filename, item, "name");
			String className = DomParseUtil.getElementText(filename, item, "provider-impl-class-name");
			policyTypeMap.put(type, className);
		}
		return policyTypeMap;
	}
}
