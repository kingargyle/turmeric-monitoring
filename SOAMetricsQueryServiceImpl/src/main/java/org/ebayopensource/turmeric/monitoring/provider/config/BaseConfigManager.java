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
import org.ebayopensource.turmeric.monitoring.util.XMLParseUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class BaseConfigManager {
	protected static final String BASE_PATH = "META-INF/metrics/";
	protected static final String BASE_SCHEMA_PATH = "META-INF/metrics/schema/";
	protected static final String CONFIG_RELATIVE_PATH = "config";

	protected Element m_rootData = null;
	protected boolean m_configLoaded = false;
	protected String m_configRelativePath = CONFIG_RELATIVE_PATH;
	protected String m_configFullPath = BASE_PATH + CONFIG_RELATIVE_PATH + "/";
	protected String m_schemaPath = BASE_SCHEMA_PATH;

	protected synchronized void init() throws ConfigurationException {
		loadConfig();
	}

	protected synchronized void loadConfig() throws ConfigurationException {
		if (m_configLoaded) {
			return;
		}
		String fullConfigFileName = m_configFullPath + getConfigFileName();
		String fullSchemaFileName = m_schemaPath + getSchemaFileName();
		loadConfigFromFile(fullConfigFileName, fullSchemaFileName,
				getRootElementName());
		m_configLoaded = true;
	}

	private synchronized void loadConfigFromFile(String fileName,
			String schemaName, String rootElementName)
			throws ConfigurationException {
		if (m_rootData != null) {
			return;
		}
		// get the root element by parsing the specified filename
		m_rootData = getRootData(fileName, schemaName, rootElementName);

		// map the root element to corresponding user-friendly object
		map(m_rootData);
	}

	// get root element
	private Element getRootData(String fileName, String schemaName,
			String rootElementName) throws ConfigurationException {
		Document doc = null;
		try {
			doc = XMLParseUtil.parseXML(fileName, schemaName, true,
					rootElementName);
		} catch (Exception e) {
			throw new ConfigurationException(e.getMessage(), e);
		}
		if (doc != null) {
			return doc.getDocumentElement();
		} else {
			return null;
		}
	}

	public String getConfigPath() {
		return m_configFullPath;
	}

	private synchronized void setConfigPath(String path) {
		m_configFullPath = path;
	}

	public synchronized void setConfigTestCase(String relativePath,
			boolean force) {
		m_configRelativePath = relativePath;
		String newPath = BASE_PATH + relativePath + "/";
		if (!force && m_configFullPath != null
				&& m_configFullPath.equals(newPath)) {
			return;
		}

		m_configLoaded = false;
		m_rootData = null;
		setConfigPath(newPath);

	}

	public void setConfigTestCase(String relativePath) {
		setConfigTestCase(relativePath, false);
	}

	public String getConfigTestCase() {
		return m_configRelativePath;
	}

	/**
	 * Perform the mapping from DOM element to user-friendly config object
	 * 
	 * @param rootData
	 */
	public abstract void map(Element rootData) throws ConfigurationException;

	/**
	 * Specify the config file name
	 * 
	 * @return
	 */
	public abstract String getConfigFileName();

	/**
	 * Specify the schema file name
	 * 
	 * @return
	 */
	public abstract String getSchemaFileName();

	/**
	 * Specify the root element name for the xml config instance. Needed for XML
	 * instance validation purpose
	 * 
	 * @return
	 */
	public abstract String getRootElementName();

}
