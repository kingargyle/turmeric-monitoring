/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.MetricIdentifier;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsDAO;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricDef;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

/**
 * The Class MetricsDAOImpl.
 *
 * @author jamuguerza
 */
public class MetricsDAOImpl extends AbstractColumnFamilyDao<String, MetricDef> implements
		MetricsDAO {
		

	/**
	 * Instantiates a new metrics error values dao impl.
	 *
	 * @param clusterName the cluster name
	 * @param host the host
	 * @param s_keyspace the s_keyspace
	 * @param columnFamilyName the column family name
	 */
	public MetricsDAOImpl(String clusterName, String host,
			String s_keyspace, String columnFamilyName) {
		super(clusterName, host, s_keyspace, String.class, MetricDef.class,
				columnFamilyName);
	}

	
	@Override
	public List<Map<String, MetricIdentifier>> findMetricComponentValuesByService(
			String metricName, long beginTime, long endTime,
			boolean serverSide, int aggregationPeriod,
			Map<String, List<String>> filters) {
		
		List<String> keys= null;
		Set<MetricDef> findItems = findItems(keys,String.valueOf(beginTime), String.valueOf(endTime));
		
		return null;
	}

	@Override
	public List<Map<String, MetricIdentifier>> findMetricComponentValuesByOperation(
			String metricName, long beginTime, long endTime,
			boolean serverSide, int aggregationPeriod,
			Map<String, List<String>> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, MetricIdentifier>> findMetricComponentValuesByConsumer(
			String metricName, long beginTime, long endTime,
			boolean serverSide, int aggregationPeriod,
			Map<String, List<String>> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, MetricIdentifier>> findMetricComponentValuesByMetric(
			String metricName, long beginTime, long endTime,
			boolean serverSide, int aggregationPeriod,
			Map<String, List<String>> filters) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
