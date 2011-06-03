/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.model.Machine;
import org.ebayopensource.turmeric.monitoring.model.Metric;
import org.ebayopensource.turmeric.monitoring.model.MetricClassifier;
import org.ebayopensource.turmeric.monitoring.model.MetricDef;
import org.ebayopensource.turmeric.monitoring.model.MetricValue;

/**
 * The Interface MetricsDAO.
 */
public interface MetricsDAO {
    
    /**
     * Find metric def.
     *
     * @param metricName the metric name
     * @return the metric def
     */
    MetricDef findMetricDef(String metricName);

    /**
     * Persist metric def.
     *
     * @param metricDef the metric def
     */
    void persistMetricDef(MetricDef metricDef);

    /**
     * Find metric.
     *
     * @param metricName the metric name
     * @param serviceName the service name
     * @param operationName the operation name
     * @return the metric
     */
    Metric findMetric(String metricName, String serviceName, String operationName);

    /**
     * Persist metric.
     *
     * @param metric the metric
     */
    void persistMetric(Metric metric);

    /**
     * Persist metric values.
     *
     * @param metricValues the metric values
     */
    void persistMetricValues(List<MetricValue> metricValues);

    /**
     * Find metric classifier.
     *
     * @param consumerName the consumer name
     * @param sourceDC the source dc
     * @param targetDC the target dc
     * @return the metric classifier
     */
    MetricClassifier findMetricClassifier(String consumerName, String sourceDC, String targetDC);

    /**
     * Persist metric classifier.
     *
     * @param metricClassifier the metric classifier
     */
    void persistMetricClassifier(MetricClassifier metricClassifier);

    /**
     * Find metric values.
     *
     * @param startTimeStamp the start time stamp
     * @param endTimeStamp the end time stamp
     * @param aggregation the aggregation
     * @return the list
     */
    List<MetricValue> findMetricValues(long startTimeStamp, long endTimeStamp, int aggregation);

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
    List<Map<String, Object>> findMetricComponentValuesByService(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters);

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
    List<Map<String, Object>> findMetricComponentValuesByOperation(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters);

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
    List<Map<String, Object>> findMetricComponentValuesByConsumer(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters);

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
    List<Map<String, Object>> findMetricComponentValuesByMetric(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters);

    /**
     * Find metric component values by time stamp.
     *
     * @param metricName the metric name
     * @param beginTime the begin time
     * @param endTime the end time
     * @param serverSide the server side
     * @param aggregationPeriod the aggregation period
     * @param serviceName the service name
     * @param operationName the operation name
     * @param consumerName the consumer name
     * @return the list
     */
    List<Map<String, Object>> findMetricComponentValuesByTimeStamp(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, String serviceName, String operationName, String consumerName);

    /**
     * Find metric service admin names.
     *
     * @param serviceNames the service names
     * @return the list
     */
    List<String> findMetricServiceAdminNames(List<String> serviceNames);

    /**
     * Find metric operation names.
     *
     * @param serviceNames the service names
     * @return the list
     */
    List<String> findMetricOperationNames(List<String> serviceNames);
    
    /**
     * Find metric consumer names.
     *
     * @param serviceNames the service names
     * @return the list
     */
    List<String> findMetricConsumerNames(List<String> serviceNames);

    /**
     * Find machine.
     *
     * @param hostAddress the host address
     * @return the machine
     */
    Machine findMachine(String hostAddress);

    /**
     * Persist machine.
     *
     * @param machine the machine
     */
    void persistMachine(Machine machine);

    /**
     * Find calls count.
     *
     * @param beginTime the begin time
     * @param endTime the end time
     * @param serverSide the server side
     * @param aggregationPeriod the aggregation period
     * @return the long
     */
    long findCallsCount(long beginTime, long endTime, boolean serverSide, int aggregationPeriod);
}
