/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.cassandra.storage.dao.impl;





import org.ebayopensource.turmeric.monitoring.cassandra.storage.dao.MetricValuesByIpAndDateDAO;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.BasicModel;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.SuperModel;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractSuperColumnFamilyDao;



/**
 * The Class MetricValuesByIpAndDateDAOImpl.
 * 
 * @author jamuguerza
 */
public class MetricValuesByIpAndDateDAOImpl<SK, K> extends
		AbstractSuperColumnFamilyDao<SK, SuperModel, K, BasicModel>
		implements MetricValuesByIpAndDateDAO<SK, K> {

	/**
	 * Instantiates a new metrics error values dao impl.
	 * 
	 * @param clusterName
	 *            the cluster name
	 * @param host
	 *            the host
	 * @param s_keyspace
	 *            the s_keyspace
	 * @param columnFamilyName
	 *            the column family name
	 */
	public MetricValuesByIpAndDateDAOImpl(final String clusterName, final String host,
			final String s_keyspace, final String columnFamilyName, final Class<SK> sKTypeClass, final Class<K> kTypeClass) {
		super(clusterName, host, s_keyspace, sKTypeClass, SuperModel.class,
				kTypeClass, BasicModel.class, columnFamilyName);
	}

}
