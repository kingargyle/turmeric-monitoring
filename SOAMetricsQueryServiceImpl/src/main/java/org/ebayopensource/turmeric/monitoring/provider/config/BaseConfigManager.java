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

/**
 * The Class BaseConfigManager.
 */
public abstract class BaseConfigManager {
	
	/** The Constant BASE_PATH. */
	protected static final String BASE_PATH = "META-INF/metrics/";
	
	/** The Constant BASE_SCHEMA_PATH. */
	protected static final String BASE_SCHEMA_PATH = "META-INF/metrics/schema/";
	
	/** The Constant CONFIG_RELATIVE_PATH. */
	protected static final String CONFIG_RELATIVE_PATH = "config";

	/** The m_root data. */
	protected Element m_rootData = null;
	
	/** The m_config loaded. */
	protected boolean m_configLoaded = false;
	
	/** The m_config relative path. */
	protected String m_configRelativePath = CONFIG_RELATIVE_PATH;
	
	/** The m_config full path. */
	protected String m_configFullPath = BASE_PATH + CONFIG_RELATIVE_PATH + "/";
	
	/** The m_schema path. */
	protected String m_schemaPath = BASE_SCHEMA_PATH;

	/**
	 * Inits the.
	 *
	 * @throws ConfigurationException the configuration exception
	 */
	protected synchronized void init() throws ConfigurationException {
		loadConfig();
	}

	/**
	 * Load config.
	 *
	 * @throws ConfigurationException the configuration exception
	 */
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

	/**
	 * Gets the config path.
	 *
	 * @return the config path
	 */
	public String getConfigPath() {
		return m_configFullPath;
	}

	private synchronized void setConfigPath(String path) {
		m_configFullPath = path;
	}

	/**
	 * Sets the config test case.
	 *
	 * @param relativePath the relative path
	 * @param force the force
	 */
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

	/**
	 * Sets the config test case.
	 *
	 * @param relativePath the new config test case
	 */
	public void setConfigTestCase(String relativePath) {
		setConfigTestCase(relativePath, false);
	}

	/**
	 * Gets the config test case.
	 *
	 * @return the config test case
	 */
	public String getConfigTestCase() {
		return m_configRelativePath;
	}

	/**
	 * Perform the mapping from DOM element to user-friendly config object.
	 *
	 * @param rootData the root data
	 * @throws ConfigurationException the configuration exception
	 */
	public abstract void map(Element rootData) throws ConfigurationException;

	/**
	 * Specify the config file name.
	 *
	 * @return the config file name
	 */
	public abstract String getConfigFileName();

	/**
	 * Specify the schema file name.
	 *
	 * @return the schema file name
	 */
	public abstract String getSchemaFileName();

	/**
	 * Specify the root element name for the xml config instance. Needed for XML
	 * instance validation purpose
	 *
	 * @return the root element name
	 */
	public abstract String getRootElementName();

}
