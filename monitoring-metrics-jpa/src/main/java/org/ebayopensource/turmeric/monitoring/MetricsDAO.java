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

public interface MetricsDAO {
    MetricDef findMetricDef(String metricName);

    void persistMetricDef(MetricDef metricDef);

    Metric findMetric(String metricName, String serviceName, String operationName);

    void persistMetric(Metric metric);

    void persistMetricValues(List<MetricValue> metricValues);

    MetricClassifier findMetricClassifier(String consumerName, String sourceDC, String targetDC);

    void persistMetricClassifier(MetricClassifier metricClassifier);

    List<MetricValue> findMetricValues(long startTimeStamp, long endTimeStamp, int aggregation);

    List<Map<String, Object>> findMetricComponentValuesByService(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters);

    List<Map<String, Object>> findMetricComponentValuesByOperation(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters);

    List<Map<String, Object>> findMetricComponentValuesByConsumer(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters);

    List<Map<String, Object>> findMetricComponentValuesByMetric(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters);

    List<Map<String, Object>> findMetricComponentValuesByTimeStamp(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, String serviceName, String operationName, String consumerName);

    List<String> findMetricServiceAdminNames(List<String> serviceNames);

    List<String> findMetricOperationNames(List<String> serviceNames);

    Machine findMachine(String hostAddress);

    void persistMachine(Machine machine);

    long findCallsCount(long beginTime, long endTime, boolean serverSide, int aggregationPeriod);
}
