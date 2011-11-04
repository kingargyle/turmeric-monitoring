/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao.impl;




import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.OrderedSuperRows;
import me.prettyprint.hector.api.beans.SuperRow;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSuperSlicesQuery;

import org.ebayopensource.turmeric.monitoring.provider.dao.MetricValuesByIpAndDateDAO;
import org.ebayopensource.turmeric.monitoring.provider.model.Model;
import org.ebayopensource.turmeric.monitoring.provider.model.SuperModel;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractSuperColumnFamilyDao;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorHelper;



// TODO: Auto-generated Javadoc
/**
 * The Class MetricValuesByIpAndDateDAOImpl.
 *
 * @param <SK> the generic type
 * @param <K> the key type
 * @author jamuguerza
 */
public class MetricValuesByIpAndDateDAOImpl<SK, K> extends
		AbstractSuperColumnFamilyDao<SK, SuperModel, K, Model>
		implements MetricValuesByIpAndDateDAO<SK, K> {

	/**
	 * Instantiates a new metrics error values dao impl.
	 *
	 * @param clusterName the cluster name
	 * @param host the host
	 * @param s_keyspace the s_keyspace
	 * @param columnFamilyName the column family name
	 * @param sKTypeClass the s k type class
	 * @param kTypeClass the k type class
	 */
	public MetricValuesByIpAndDateDAOImpl(final String clusterName, final String host,
			final String s_keyspace, final String columnFamilyName, final Class<SK> sKTypeClass, final Class<K> kTypeClass) {
		super(clusterName, host, s_keyspace, sKTypeClass, SuperModel.class,
				kTypeClass, Model.class, columnFamilyName);
	}

	
}
