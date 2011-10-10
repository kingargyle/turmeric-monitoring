/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.MetricIdentifier;
import org.ebayopensource.turmeric.monitoring.provider.model.MetricValue;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;

/**
 * The Interface MetricValuesDAO.
 * 
 * @author jose alvarez muguerza
 */
public interface MetricValuesDAO<K> {
   public MetricValue<?> find(K key);

   public Map<String, List<MetricValue<?>>> findMetricValuesByOperation(String metricName, long firstStartTime,
            long l, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters) throws ServiceException;

}
