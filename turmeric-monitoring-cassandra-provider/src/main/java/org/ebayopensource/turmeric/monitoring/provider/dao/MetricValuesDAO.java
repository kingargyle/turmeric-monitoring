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

import org.ebayopensource.turmeric.monitoring.provider.model.MetricValue;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;

// TODO: Auto-generated Javadoc
/**
 * The Interface MetricValuesDAO.
 *
 * @param <K> the key type
 * @author jose alvarez muguerza
 */
public interface MetricValuesDAO<K> {
   
   /**
    * Find.
    *
    * @param key the key
    * @return the metric value
    */
   public MetricValue<?> find(K key);

   /**
    * Find metric values by consumer.
    *
    * @param ipAddressList the ip address list
    * @param metricName the metric name
    * @param begin the begin
    * @param end the end
    * @param serverSide the server side
    * @param aggregationPeriod the aggregation period
    * @param serviceName the service name
    * @param operationNames the operation names
    * @param consumerNames the consumer names
    * @return the map
    * @throws ServiceException the service exception
    */
   Map<String, List<MetricValue<?>>> findMetricValuesByConsumer(List<String> ipAddressList, String metricName,
            long begin, long end, boolean serverSide, int aggregationPeriod, String serviceName,
            List<String> operationNames, List<String> consumerNames) throws ServiceException;

   /**
    * Find metric values by operation.
    *
    * @param ipaddressList the ipaddress list
    * @param metricName the metric name
    * @param firstStartTime the first start time
    * @param l the l
    * @param serverSide the server side
    * @param aggregationPeriod the aggregation period
    * @param filters the filters
    * @return the map
    * @throws ServiceException the service exception
    */
   public Map<String, List<MetricValue<?>>> findMetricValuesByOperation(List<String> ipaddressList, String metricName,
            long firstStartTime, long l, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters)
            throws ServiceException;

}
