/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;


import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsServiceConsumerByIpDAO;
import org.ebayopensource.turmeric.monitoring.provider.model.BasicModel;
import org.ebayopensource.turmeric.monitoring.provider.model.SuperModel;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricDef;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractSuperColumnFamilyDao;



/**
 * The Class MetricsServiceOperationByIpDAOImpl.
 * 
 * @author jamuguerza
 */
public class MetricsServiceConsumerByIpDAOImpl extends
		AbstractSuperColumnFamilyDao<String, SuperModel, String, BasicModel>
		implements MetricsServiceConsumerByIpDAO {

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
	public MetricsServiceConsumerByIpDAOImpl(String clusterName, String host,
			String s_keyspace, String columnFamilyName) {
		super(clusterName, host, s_keyspace, String.class, SuperModel.class,
				String.class, BasicModel.class, columnFamilyName);
	}

}
