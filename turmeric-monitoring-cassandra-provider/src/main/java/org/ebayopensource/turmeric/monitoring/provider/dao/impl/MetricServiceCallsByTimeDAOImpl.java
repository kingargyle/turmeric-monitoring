/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao.impl;





import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsServiceCallsByTimeDAO;
import org.ebayopensource.turmeric.monitoring.provider.model.BasicModel;
import org.ebayopensource.turmeric.monitoring.provider.model.SuperModel;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractSuperColumnFamilyDao;



/**
 * The Class MetricServiceCallsByTimeDAOImpl.
 * 
 * @author jamuguerza
 */
public class MetricServiceCallsByTimeDAOImpl extends
		AbstractSuperColumnFamilyDao<String, SuperModel, String, BasicModel>
		implements MetricsServiceCallsByTimeDAO {

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
	public MetricServiceCallsByTimeDAOImpl(String clusterName, String host,
			String s_keyspace, String columnFamilyName) {
		super(clusterName, host, s_keyspace, String.class, SuperModel.class,
				String.class, BasicModel.class, columnFamilyName);
	}

}
