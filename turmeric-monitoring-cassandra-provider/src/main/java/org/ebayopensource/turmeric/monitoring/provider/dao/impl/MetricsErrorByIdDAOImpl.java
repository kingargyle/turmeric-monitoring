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
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

/**
 * @author jamuguerza
 *
 */
public class MetricsErrorByIdDAOImpl extends AbstractColumnFamilyDao<String, org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById > implements
		MetricsErrorByIdDAO {
		
	/**
	 * Instantiates a new metrics error dao impl.
	 *
	 * @param clusterName the cluster name
	 * @param host the host
	 * @param s_keyspace the s_keyspace
	 * @param columnFamilyName the column family name
	 */
	public MetricsErrorByIdDAOImpl(String clusterName, String host,
			String s_keyspace,  String columnFamilyName) {
		super(clusterName, host, s_keyspace, String.class, ErrorById.class,
				columnFamilyName);
	}


}
