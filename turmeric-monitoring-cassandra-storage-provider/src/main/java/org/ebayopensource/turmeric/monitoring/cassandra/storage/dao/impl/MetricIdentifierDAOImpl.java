/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.cassandra.storage.dao.impl;


import org.ebayopensource.turmeric.monitoring.cassandra.storage.dao.MetricIndentifierDAO;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.MetricIdentifier;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

/**
 * @author jamuguerza
 *
 */
public class MetricIdentifierDAOImpl<K> extends AbstractColumnFamilyDao<K, MetricIdentifier> implements
	MetricIndentifierDAO<K> {

	public MetricIdentifierDAOImpl(String clusterName, String host,
			String s_keyspace,  String columnFamilyName, final Class<K> kTypeClass) {
		super(clusterName, host, s_keyspace, kTypeClass, MetricIdentifier.class,
				columnFamilyName);
		
	}

}
