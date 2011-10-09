/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ebayopensource.turmeric.monitoring.provider.dao.BaseMetricsErrorsByFilterDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorByIdDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorValuesDAO;
import org.ebayopensource.turmeric.monitoring.provider.model.Model;
import org.ebayopensource.turmeric.monitoring.provider.util.KeyGeneratorUtil;
import org.ebayopensource.turmeric.monitoring.v1.services.ResourceEntity;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

/**
 * The abstract class  BaseMetricsErrorsByFiltersDAO.
 * @author jose alvarez muguerza
 */
public  class BaseMetricsErrorsByFiltersDAOImpl<K>  extends AbstractColumnFamilyDao<K, Model>
	implements	BaseMetricsErrorsByFilterDAO<K> {

    private final MetricsErrorValuesDAO<String> errorValuesDaoImpl ;
    private final  MetricsErrorByIdDAO<Long> errorByIdDaoImpl; 

	public BaseMetricsErrorsByFiltersDAOImpl(final String clusterName, final String host,
			final String s_keyspace,  final String columnFamilyName,final Class<K> kTypeClass,  final MetricsErrorValuesDAO<String> errorValuesDaoImpl, final MetricsErrorByIdDAO<Long> errorByIdDaoImpl) {
		super(clusterName, host, s_keyspace, kTypeClass, Model.class, columnFamilyName);
		this.errorValuesDaoImpl = errorValuesDaoImpl;
		this.errorByIdDaoImpl = errorByIdDaoImpl;
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
		// IP or ServerName for improvement, for now we just use All-

		final List<String> errorKeys =  KeyGeneratorUtil.generateErrorValuesKeys( serverSide,
				filters, filter);
		//KEY format: ServerName|ServiceAdminName1|ConsumerName|Operation1|APPLICATION|true
		//looks into ErrorCountsByCategory cf
		Map<K, Map<Long, String>> findItems = findItemsWithStringColumnValues((List<K>) errorKeys, beginTime, endTime);
		//Map<K, Map<Long, Object>> findItems = this.findItemsWithObjectColumnValues((List<K>) errorKeys, beginTime, endTime);

		List<Map<K, Object>> result = new ArrayList<Map<K, Object>>();
		Set<Entry<K, Map<Long, String>>> entrySet = findItems.entrySet();
		for (Entry<K, Map<Long, String>> findItemSet : entrySet) {
			Map<Long, String> value = findItemSet.getValue();
			Set<Entry<Long, String>> entrySet2 = value.entrySet();
			for (Entry<Long, String> findItemSet2 : entrySet2) {
				Map<K, Object> row = new HashMap<K, Object>();

				//TODO fix it for getExtendedErrorMetricsData
				String errorValueKey = findItemSet2.getValue();
				//the error value must be in format: timestamp|randomnumber
				ErrorValue errorValue = errorValuesDaoImpl.find(errorValueKey );
				
				row.put((K)"errorCount", errorByIdDaoImpl.findCountByTimeRange(errorValue.getErrorId(), beginTime, endTime)); //TODO read from column family
				row.put((K) "errorId", errorValue.getErrorId());
				row.put((K)"errorName", errorValue.getName());
				if (filters.get(ResourceEntity.CONSUMER.value()) != null){
					row.put((K)"consumerName", errorValue.getConsumerName());
				}
				row.put((K)"serverSide", errorValue.isServerSide());
			
				row.put((K)"timeStamp", findItemSet2.getKey());
				result.add(row);
			}
		}
	
		return result;	
		
	}
	
	
}
