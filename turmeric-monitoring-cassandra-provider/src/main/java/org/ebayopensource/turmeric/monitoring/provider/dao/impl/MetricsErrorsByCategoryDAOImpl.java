/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao.impl;

import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorByIdDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorValuesDAO;

// TODO: Auto-generated Javadoc
/**
 * The Class MetricsErrorsByCategoryDAOImpl.
 *
 * @param <K> the key type
 * @author jamuguerza
 */
public class MetricsErrorsByCategoryDAOImpl<K> extends
		BaseMetricsErrorsByFiltersDAOImpl<K> {

	/**
	 * Instantiates a new metrics error dao impl.
	 *
	 * @param clusterName the cluster name
	 * @param host the host
	 * @param s_keyspace the s_keyspace
	 * @param columnFamilyName the column family name
	 * @param kTypeClass the k type class
	 * @param errorValuesDaoImpl the error values dao impl
	 * @param errorByIdDaoImpl the error by id dao impl
	 */
	public MetricsErrorsByCategoryDAOImpl(final String clusterName,
			final String host, final String s_keyspace,
			final String columnFamilyName, final Class<K> kTypeClass,
			final MetricsErrorValuesDAO<String> errorValuesDaoImpl, final MetricsErrorByIdDAO<Long> errorByIdDaoImpl) {

		super(clusterName, host, s_keyspace, columnFamilyName, kTypeClass, 
				errorValuesDaoImpl, errorByIdDaoImpl);
	}

}
