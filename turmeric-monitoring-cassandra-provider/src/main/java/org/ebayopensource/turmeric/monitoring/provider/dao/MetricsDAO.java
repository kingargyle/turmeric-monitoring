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


/**
 * The Interface MetricsDAO.
 * @author jose alvarez muguerza
 */
public interface MetricsDAO  {

    /**
     * Find metric component values by service.
     *
     * @param metricName the metric name
     * @param beginTime the begin time
     * @param endTime the end time
     * @param serverSide the server side
     * @param aggregationPeriod the aggregation period
     * @param filters the filters
     * @return the list
     */
    List<Map<String, MetricIdentifier>> findMetricComponentValuesByService(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters);

    /**
     * Find metric component values by operation.
     *
     * @param metricName the metric name
     * @param beginTime the begin time
     * @param endTime the end time
     * @param serverSide the server side
     * @param aggregationPeriod the aggregation period
     * @param filters the filters
     * @return the list
     */
    List<Map<String, MetricIdentifier>> findMetricComponentValuesByOperation(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters);
 
    /**
     * Find metric component values by consumer.
     *
     * @param metricName the metric name
     * @param beginTime the begin time
     * @param endTime the end time
     * @param serverSide the server side
     * @param aggregationPeriod the aggregation period
     * @param filters the filters
     * @return the list
     */
    List<Map<String, MetricIdentifier>> findMetricComponentValuesByConsumer(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters);
    /**
     * Find metric component values by metric.
     *
     * @param metricName the metric name
     * @param beginTime the begin time
     * @param endTime the end time
     * @param serverSide the server side
     * @param aggregationPeriod the aggregation period
     * @param filters the filters
     * @return the list
     */
    List<Map<String, MetricIdentifier>> findMetricComponentValuesByMetric(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters);

}
