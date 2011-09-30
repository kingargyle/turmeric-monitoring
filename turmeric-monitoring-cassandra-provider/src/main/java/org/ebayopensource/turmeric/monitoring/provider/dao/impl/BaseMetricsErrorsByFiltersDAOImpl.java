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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ebayopensource.turmeric.monitoring.provider.dao.BaseMetricsErrorsByFilterDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorValuesDAO;
import org.ebayopensource.turmeric.monitoring.provider.util.KeyGeneratorUtil;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

/**
 * The abstract class  BaseMetricsErrorsByFiltersDAO.
 * @author jose alvarez muguerza
 */
public  class BaseMetricsErrorsByFiltersDAOImpl<K>  extends AbstractColumnFamilyDao<K, String>
	implements	BaseMetricsErrorsByFilterDAO<K> {

    private final MetricsErrorValuesDAO errorValuesDaoImpl ;

	public BaseMetricsErrorsByFiltersDAOImpl(final String clusterName, final String host,
			final String s_keyspace,  final String columnFamilyName,final Class<K> kTypeClass,  final MetricsErrorValuesDAO errorValuesDaoImpl) {
		super(clusterName, host, s_keyspace, kTypeClass, String.class, columnFamilyName);
		this.errorValuesDaoImpl = errorValuesDaoImpl;
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.monitoring.provider.dao.BaseMetricsErrorsByFilterDAO#findErrorValuesByFilter(long, long, boolean, int, java.lang.Long, java.lang.String, java.util.Map)
	 */
	public List<Map<K, Object>> findErrorValuesByFilter(long beginTime,
			long endTime, boolean serverSide, int aggregationPeriod,
			Long errorId, String filter,
			Map<String, List<String>> filters) {
		// errorId comes null
		// agregationpPeriod not used
		// IP or ServerName for improvement, for now we just use ALL-

		final List<K> errorKeys =  (List<K>) KeyGeneratorUtil.generateErrorValuesKeys( serverSide,
				filters, filter);
		Set<K> errorValuesIds = (Set<K>) findItems(errorKeys, String.valueOf(beginTime), String.valueOf(endTime));
		Set<ErrorValue> errorValues = errorValuesDaoImpl.findItems(
				new ArrayList<K>(errorValuesIds), "", "");

		List<Map<K, Object>> result = new ArrayList<Map<K, Object>>();
		for (ErrorValue errorValue : errorValues) {
			Map<K, Object> row = new HashMap<K, Object>();
			row.put((K)"timeStamp", errorValue.getTimeStamp());
			result.add(row);
		}

		return result;	
		
	}
}
