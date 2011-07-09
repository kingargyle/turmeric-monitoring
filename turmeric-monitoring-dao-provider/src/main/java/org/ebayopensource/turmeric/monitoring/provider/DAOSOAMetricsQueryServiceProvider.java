/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;

import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.MetricsDAO;
import org.ebayopensource.turmeric.monitoring.MetricsDAOImpl;
import org.ebayopensource.turmeric.monitoring.model.MetricComponentDef;
import org.ebayopensource.turmeric.monitoring.provider.model.ExtendedErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.CriteriaInfo;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorInfos;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGraphData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGroupData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricResourceCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.PolicyMetricData;
import org.ebayopensource.turmeric.monitoring.v1.services.PolicyMetricGraphData;
import org.ebayopensource.turmeric.monitoring.v1.services.ReportCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.ResourceEntity;
import org.ebayopensource.turmeric.monitoring.v1.services.ResourceEntityRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.SortOrderType;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.SystemMetricDefs;
import org.ebayopensource.turmeric.utils.jpa.JPAAroundAdvice;
import org.ebayopensource.turmeric.utils.jpa.PersistenceContext;

/**
 * A Hibernate/JPA provider implementation for the SOAMetricsQueryService.
 * 
 * @see org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider
 */
public class DAOSOAMetricsQueryServiceProvider implements SOAMetricsQueryServiceProvider {
    private final SOAMetricsQueryServiceProvider delegate;

    /**
     * Constructor used by the XML configuration, it must hard-code all dependencies.
     */
    public DAOSOAMetricsQueryServiceProvider() {
        this(PersistenceContext.createEntityManagerFactory("metrics"));
    }

    /**
     * Constructor that allows dependency injection, used for tests.
     * 
     * @param entityManagerFactory
     *            the JPA EntityManagerFactory to inject
     */
    public DAOSOAMetricsQueryServiceProvider(EntityManagerFactory entityManagerFactory) {
        ClassLoader classLoader = SOAMetricsQueryServiceProvider.class.getClassLoader();
        Class[] interfaces = { SOAMetricsQueryServiceProvider.class };
        JPAAroundAdvice handler = new JPAAroundAdvice(entityManagerFactory, new Target());
        this.delegate = (SOAMetricsQueryServiceProvider) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MetricData> getCustomReportData(ReportCriteria reportCriteria, MetricCriteria metricCriteria) {
        return delegate.getCustomReportData(reportCriteria, metricCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MetricData> getDetailData(String dc, MetricCriteria metricCriteria,
                    MetricResourceCriteria metricResourceCriteria) {
        return delegate.getDetailData(dc, metricCriteria, metricResourceCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MetricGraphData> getErrorGraph(String serviceName, String operationName, String consumerName,
                    String errorId, String errorCategory, String errorSeverity, MetricCriteria metricCriteria) {
        return delegate.getErrorGraph(serviceName, operationName, consumerName, errorId, errorCategory, errorSeverity,
                        metricCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ErrorViewData> getErrorMetricsData(String errorType, List<String> serviceName,
                    List<String> operationName, List<String> consumerName, String errorId, String errorCategory,
                    String errorSeverity, String errorName, MetricCriteria metricCriteria) {
        return delegate.getErrorMetricsData(errorType, serviceName, operationName, consumerName, errorId,
                        errorCategory, errorSeverity, errorName, metricCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ErrorInfos getErrorMetricsMetadata(String errorId, String errorName, String serviceName) {
        return delegate.getErrorMetricsMetadata(errorId, errorName, serviceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MetricGroupData> getMetricsData(MetricCriteria metricCriteria,
                    MetricResourceCriteria metricResourceCriteria) {
        return delegate.getMetricsData(metricCriteria, metricResourceCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetricData getMetricSummaryData(String dc, MetricCriteria metricCriteria,
                    MetricResourceCriteria metricResourceCriteria) {
        return delegate.getMetricSummaryData(dc, metricCriteria, metricResourceCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MetricGraphData> getMetricValue(CriteriaInfo criteriaInfo, long startTime, long duration, int step,
                    String autoDelay) {
        return delegate.getMetricValue(criteriaInfo, startTime, duration, step, autoDelay);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PolicyMetricData> getPolicyMetricData(long startTime, long endTime, String policyType,
                    String policyName, String serviceName, String operationName, String subjectTypeName,
                    String subjectValue, String effect) {
        return delegate.getPolicyMetricData(startTime, endTime, policyType, policyName, serviceName, operationName,
                        subjectTypeName, subjectValue, effect);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PolicyMetricGraphData> getPolicyMetricDetailData(String policyName, String serviceName,
                    String operationName, String subjectTypeName, String subjectValue, String listType, long startTime,
                    long endTime) {
        return delegate.getPolicyMetricDetailData(policyName, serviceName, operationName, subjectTypeName,
                        subjectValue, listType, startTime, endTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MetricData> getStandardReportData(String reportType, MetricCriteria metricCriteria) {
        return delegate.getStandardReportData(reportType, metricCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getMetricsMetadata(String resourceEntityType, List<String> resourceEntityName,
                    String resourceEntityResponseType) {
        return delegate.getMetricsMetadata(resourceEntityType, resourceEntityName, resourceEntityResponseType);
    }

    @Override
    public List<ExtendedErrorViewData> getExtendedErrorMetricsData(String errorType, List<String> serviceName,
                    List<String> operationName, List<String> consumerName, String errorId, String errorCategory,
                    String errorSeverity, String errorName, MetricCriteria metricCriteria) {

        return delegate.getExtendedErrorMetricsData(errorType, serviceName, operationName, consumerName, errorId,
                        errorCategory, errorSeverity, errorName, metricCriteria);
    }

    private static class Target implements SOAMetricsQueryServiceProvider {
        private final MetricsDAO metricsDAO = new MetricsDAOImpl();
        private final MetricsErrorDAO errorDAO = new MetricsErrorDAOImpl();

        @Override
        public List<MetricData> getCustomReportData(ReportCriteria reportCriteria, MetricCriteria metricCriteria) {
            return null;
        }

        @Override
        public List<MetricData> getDetailData(String dc, MetricCriteria metricCriteria,
                        MetricResourceCriteria metricResourceCriteria) {
            return null;
        }

        @Override
        public List<MetricGraphData> getErrorGraph(String serviceName, String operationName, String consumerName,
                        String errorId, String errorCategoryStr, String errorSeverityStr, MetricCriteria metricCriteria) {
            List<MetricGraphData> result = new ArrayList<MetricGraphData>();

            String errorCategory = errorCategoryStr == null ? null : ErrorCategory.fromValue(errorCategoryStr).value();
            String errorSeverity = errorSeverityStr == null ? null : ErrorSeverity.fromValue(errorSeverityStr).value();
            int aggregationPeriod = metricCriteria.getAggregationPeriod();
            long beginTime = metricCriteria.getFirstStartTime();
            long duration = metricCriteria.getDuration();
            long endTime = beginTime + TimeUnit.SECONDS.toMillis(duration);
            boolean serverSide = MonitoringSystem.COLLECTION_LOCATION_SERVER.equals(metricCriteria.getRoleType());

            Map<String, List<String>> filters = new HashMap<String, List<String>>();
            if (serviceName != null && !"".equals(serviceName))
                filters.put(ResourceEntity.SERVICE.value(), Collections.singletonList(serviceName));
            if (operationName != null && !"".equals(operationName))
                filters.put(ResourceEntity.OPERATION.value(), Collections.singletonList(operationName));
            if (consumerName != null && !"".equals(consumerName))
                filters.put(ResourceEntity.CONSUMER.value(), Collections.singletonList(consumerName));

            List<Map<String, Object>> queryResult = errorDAO.findAllErrorValuesByCategory(beginTime, endTime,
                            serverSide, aggregationPeriod, null, errorCategory, errorSeverity, filters);

            for (int i = 0; i < duration / aggregationPeriod; ++i) {
                long startTime = beginTime + TimeUnit.SECONDS.toMillis(i * aggregationPeriod);
                long stopTime = startTime + TimeUnit.SECONDS.toMillis(aggregationPeriod);
                double value = 0;
                for (Map<String, Object> row : queryResult) {
                    long time = (Long) row.get("timeStamp");
                    if (startTime <= time && time < stopTime) {
                        value += 1;
                    }
                }
                MetricGraphData metricGraphData = new MetricGraphData();
                metricGraphData.setCount(value);
                metricGraphData.setTimeSlot(startTime);
                metricGraphData.setCriteria(null); // Not supported for now
                result.add(metricGraphData);
            }

            return result;
        }

        @Override
        public List<ErrorViewData> getErrorMetricsData(String errorType, List<String> serviceNames,
                        List<String> operationNames, List<String> consumerNames, String errorIdString,
                        String errorCategoryString, String errorSeverityString, String isErrorId,
                        MetricCriteria metricCriteria) {
            long firstStartTime = metricCriteria.getFirstStartTime();
            long secondStartTime = metricCriteria.getSecondStartTime();
            long duration = TimeUnit.SECONDS.toMillis(metricCriteria.getDuration());
            int aggregationPeriod = metricCriteria.getAggregationPeriod();
            boolean serverSide = !MonitoringSystem.COLLECTION_LOCATION_CLIENT.equals(metricCriteria.getRoleType());

            // Validate input parameters
            Long errorId = errorIdString == null ? null : Long.parseLong(errorIdString);
            String errorCategory = errorCategoryString == null ? null : ErrorCategory.fromValue(errorCategoryString)
                            .value();
            String errorSeverity = errorSeverityString == null ? null : ErrorSeverity.fromValue(errorSeverityString)
                            .value();

            Map<String, List<String>> filters = new HashMap<String, List<String>>();
            if (!serviceNames.isEmpty())
                filters.put(ResourceEntity.SERVICE.value(), serviceNames);
            if (!operationNames.isEmpty())
                filters.put(ResourceEntity.OPERATION.value(), operationNames);
            if (!consumerNames.isEmpty())
                filters.put(ResourceEntity.CONSUMER.value(), consumerNames);

            List<Map<String, Object>> errors1;
            double calls1;
            List<Map<String, Object>> errors2;
            double calls2;
            if ("Category".equals(errorType)) {
                errors1 = errorDAO.findErrorValuesByCategory(firstStartTime, firstStartTime + duration, serverSide,
                                aggregationPeriod, errorId, errorCategory, errorSeverity, filters);
                calls1 = metricsDAO.findCallsCount(firstStartTime, firstStartTime + duration, serverSide,
                                aggregationPeriod);
                errors2 = errorDAO.findErrorValuesByCategory(secondStartTime, secondStartTime + duration, serverSide,
                                aggregationPeriod, errorId, errorCategory, errorSeverity, filters);
                calls2 = metricsDAO.findCallsCount(secondStartTime, secondStartTime + duration, serverSide,
                                aggregationPeriod);
            }
            else if ("Severity".equals(errorType)) {
                errors1 = errorDAO.findErrorValuesBySeverity(firstStartTime, firstStartTime + duration, serverSide,
                                aggregationPeriod, errorId, errorCategory, errorSeverity, filters);
                calls1 = metricsDAO.findCallsCount(firstStartTime, firstStartTime + duration, serverSide,
                                aggregationPeriod);
                errors2 = errorDAO.findErrorValuesBySeverity(secondStartTime, secondStartTime + duration, serverSide,
                                aggregationPeriod, errorId, errorCategory, errorSeverity, filters);
                calls2 = metricsDAO.findCallsCount(secondStartTime, secondStartTime + duration, serverSide,
                                aggregationPeriod);
            }
            else {
                throw new IllegalArgumentException("Invalid error type " + errorType);
            }

            Map<String, Map<String, Object>> map1 = transformAggregatedErrorValues(errors1);
            Map<String, Map<String, Object>> map2 = transformAggregatedErrorValues(errors2);

            List<ErrorViewData> result = new ArrayList<ErrorViewData>();
            for (Map.Entry<String, Map<String, Object>> entry : map1.entrySet()) {
                ErrorViewData errorViewData = new ErrorViewData();
                Map<String, Object> row1 = entry.getValue();

                long errorId1 = (Long) row1.get("errorId");
                String errorName = (String) row1.get("errorName");
                org.ebayopensource.turmeric.monitoring.v1.services.Error error1 = new org.ebayopensource.turmeric.monitoring.v1.services.Error();
                error1.setErrorId(String.valueOf(errorId1));
                error1.setErrorName(errorName);
                errorViewData.setError(error1);

                long count1 = (Long) row1.get("errorCount");
                errorViewData.setErrorCount1(count1);

                errorViewData.setErrorCallRatio1(calls1 == 0 ? 0 : count1 / calls1);

                Map<String, Object> row2 = map2.remove(entry.getKey());
                if (row2 != null) {
                    long count2 = (Long) row2.get("errorCount");
                    errorViewData.setErrorCount2(count2);
                    errorViewData.setErrorCallRatio2(calls2 == 0 ? 0 : count2 / calls2);
                }
                else {
                    errorViewData.setErrorCount2(0);
                    errorViewData.setErrorCallRatio2(0);
                }

                result.add(errorViewData);
            }
            for (Map.Entry<String, Map<String, Object>> entry : map2.entrySet()) {
                ErrorViewData errorViewData = new ErrorViewData();
                Map<String, Object> row2 = entry.getValue();

                long errorId2 = (Long) row2.get("errorId");
                String errorName = (String) row2.get("errorName");
                org.ebayopensource.turmeric.monitoring.v1.services.Error error2 = new org.ebayopensource.turmeric.monitoring.v1.services.Error();
                error2.setErrorId(String.valueOf(errorId2));
                error2.setErrorName(errorName);
                errorViewData.setError(error2);

                long count2 = (Long) row2.get("errorCount");
                errorViewData.setErrorCount2(count2);

                errorViewData.setErrorCallRatio2(calls2 == 0 ? 0 : count2 / calls2);

                errorViewData.setErrorCount1(0);
                errorViewData.setErrorCallRatio1(0);
                errorViewData.setErrorDiff(100);
                errorViewData.setRatioDiff(100);

                result.add(errorViewData);
            }

            // Sort the results
            final boolean sortAsc = metricCriteria.getSortOrder() == SortOrderType.ASCENDING;
            Collections.sort(result, new Comparator<ErrorViewData>() {
                @Override
                public int compare(ErrorViewData evd1, ErrorViewData evd2) {
                    double v1 = Math.max(evd1.getErrorCount1(), evd1.getErrorCount2());
                    double v2 = Math.max(evd2.getErrorCount1(), evd2.getErrorCount2());
                    if (v1 == v2) {
                        v1 = evd1.getErrorCount1() + evd1.getErrorCount2();
                        v2 = evd2.getErrorCount1() + evd2.getErrorCount2();
                    }
                    if (v1 == v2) {
                        v1 = evd1.getErrorCallRatio1();
                        v2 = evd2.getErrorCallRatio2();
                    }
                    if (v1 == v2) {
                        v1 = evd1.getErrorCallRatio1() + evd1.getErrorCallRatio2();
                        v2 = evd2.getErrorCallRatio2() + evd2.getErrorCallRatio2();
                    }
                    int result = v1 > v2 ? 1 : v1 < v2 ? -1 : 0;
                    return sortAsc ? result : -result;
                }
            });

            // Trim to the number of requested rows
            int rows = metricCriteria.getNumRows() == null ? 0 : Integer.parseInt(metricCriteria.getNumRows());
            trimResultList(result, rows);

            return result;
        }

        @Override
        public ErrorInfos getErrorMetricsMetadata(String errorId, String errorName, String serviceName) {
            long id = Long.parseLong(errorId);
            org.ebayopensource.turmeric.runtime.error.model.Error error = errorDAO.findErrorByErrorId(id);
            if (error != null) {
                ErrorInfos result = new ErrorInfos();
                result.setId(String.valueOf(error.getId()));
                result.setName(error.getName());
                result.setCategory(error.getCategory().value());
                result.setSeverity(error.getSeverity().value());
                result.setDomain(error.getDomain());
                result.setSubDomain(error.getSubDomain());
                return result;
            }
            return null;
        }

        @Override
        public List<MetricGroupData> getMetricsData(MetricCriteria metricCriteria,
                        MetricResourceCriteria metricResourceCriteria) {
            // STEP 1. Decode the input parameters
            String encodedMetricName = metricCriteria.getMetricName();
            String metricName = decodeMetricName(encodedMetricName);

            Map<String, List<String>> filters = new HashMap<String, List<String>>();
            for (ResourceEntity resourceEntityType : ResourceEntity.values()) {
                List<String> resourceEntityNames = null;
                for (ResourceEntityRequest resourceEntityRequest : metricResourceCriteria.getResourceRequestEntities()) {
                    if (resourceEntityRequest.getResourceEntityType() == resourceEntityType) {
                        resourceEntityNames = resourceEntityRequest.getResourceEntityName();
                        break;
                    }
                }
                if (resourceEntityNames == null)
                    resourceEntityNames = Collections.emptyList();
                if (!resourceEntityNames.isEmpty())
                    filters.put(resourceEntityType.value(), resourceEntityNames);
            }

            String groupBy = metricResourceCriteria.getResourceEntityResponseType();

            long firstStartTime = metricCriteria.getFirstStartTime();
            long secondStartTime = metricCriteria.getSecondStartTime();
            long duration = TimeUnit.SECONDS.toMillis(metricCriteria.getDuration());
            int aggregationPeriod = metricCriteria.getAggregationPeriod();
            boolean serverSide = !MonitoringSystem.COLLECTION_LOCATION_CLIENT.equals(metricCriteria.getRoleType());

            // STEP 2. Query the data
            List<Map<String, Object>> data1;
            List<Map<String, Object>> data2;
            if (ResourceEntity.SERVICE.value().equals(groupBy)) {
                data1 = metricsDAO.findMetricComponentValuesByService(metricName, firstStartTime, firstStartTime
                                + duration, serverSide, aggregationPeriod, filters);
                data2 = metricsDAO.findMetricComponentValuesByService(metricName, secondStartTime, secondStartTime
                                + duration, serverSide, aggregationPeriod, filters);
            }
            else if (ResourceEntity.OPERATION.value().equals(groupBy)) {
                data1 = metricsDAO.findMetricComponentValuesByOperation(metricName, firstStartTime, firstStartTime
                                + duration, serverSide, aggregationPeriod, filters);
                data2 = metricsDAO.findMetricComponentValuesByOperation(metricName, secondStartTime, secondStartTime
                                + duration, serverSide, aggregationPeriod, filters);
            }
            else if (ResourceEntity.CONSUMER.value().equals(groupBy)) {
                data1 = metricsDAO.findMetricComponentValuesByConsumer(metricName, firstStartTime, firstStartTime
                                + duration, serverSide, aggregationPeriod, filters);
                data2 = metricsDAO.findMetricComponentValuesByConsumer(metricName, secondStartTime, secondStartTime
                                + duration, serverSide, aggregationPeriod, filters);
            }
            else if ("Error".equals(groupBy)) {
                metricName = "SoaFwk.Op.Err.%";
                data1 = metricsDAO.findMetricComponentValuesByMetric(metricName, firstStartTime, firstStartTime
                                + duration, serverSide, aggregationPeriod, filters);
                data2 = metricsDAO.findMetricComponentValuesByMetric(metricName, secondStartTime, secondStartTime
                                + duration, serverSide, aggregationPeriod, filters);
            }
            else {
                throw new UnsupportedOperationException();
            }

            // Move data to a better data structure
            Map<String, Map<String, Object>> map1 = transformAggregatedMetricComponentValues(encodedMetricName, data1);
            Map<String, Map<String, Object>> map2 = transformAggregatedMetricComponentValues(encodedMetricName, data2);

            // STEP 3. Create and populate the return values
            List<MetricGroupData> result = new ArrayList<MetricGroupData>();
            for (Map.Entry<String, Map<String, Object>> entry : map1.entrySet()) {
                MetricGroupData metricGroupData = new MetricGroupData();
                Map<String, Object> row1 = entry.getValue();
                Double count1 = (Double) row1.get("value");
                metricGroupData.setCount1(count1);
                Map<String, Object> row2 = map2.remove(entry.getKey());
                if (row2 != null) {
                    Double count2 = (Double) row2.get("value");
                    metricGroupData.setCount2(count2);
                }
                else {
                    metricGroupData.setCount2(0);
                    // metricGroupData.setDiff(-100);
                }

                CriteriaInfo criteriaInfo = populateCriteriaInfo(groupBy, row1);
                metricGroupData.setCriteriaInfo(criteriaInfo);

                result.add(metricGroupData);
            }
            for (Map.Entry<String, Map<String, Object>> entry : map2.entrySet()) {
                MetricGroupData metricGroupData = new MetricGroupData();
                Map<String, Object> row2 = entry.getValue();
                Double count2 = (Double) row2.get("value");
                metricGroupData.setCount2(count2);
                metricGroupData.setCount1(0);
                // metricGroupData.setDiff(100);

                CriteriaInfo criteriaInfo = populateCriteriaInfo(groupBy, row2);
                metricGroupData.setCriteriaInfo(criteriaInfo);

                result.add(metricGroupData);
            }

            // Sort the results
            final boolean sortAsc = metricCriteria.getSortOrder() == SortOrderType.ASCENDING;
            Collections.sort(result, new Comparator<MetricGroupData>() {
                @Override
                public int compare(MetricGroupData mgd1, MetricGroupData mgd2) {
                    double v1 = Math.max(mgd1.getCount1(), mgd1.getCount2());
                    double v2 = Math.max(mgd2.getCount1(), mgd2.getCount2());
                    if (v1 == v2) {
                        v1 = mgd1.getCount1() + mgd1.getCount2();
                        v2 = mgd2.getCount1() + mgd2.getCount2();
                    }
                    if (v1 == v2) {
                        v1 = mgd1.getCount2();
                        v2 = mgd2.getCount2();
                    }
                    int result = v1 > v2 ? 1 : v1 < v2 ? -1 : 0;
                    return sortAsc ? result : -result;
                }
            });

            // Trim to the number of requested rows
            int rows = metricCriteria.getNumRows() == null ? 0 : Integer.parseInt(metricCriteria.getNumRows());
            trimResultList(result, rows);

            return result;
        }

        private void trimResultList(List<?> list, int maxRows) {
            if (maxRows > 0 && list.size() > maxRows)
                list.subList(0, maxRows);
        }

        private CriteriaInfo populateCriteriaInfo(String groupBy, Map<String, Object> row) {
            CriteriaInfo criteriaInfo = new CriteriaInfo();
            criteriaInfo.setServiceConsumerType(groupBy);
            String metricName = (String) row.get("metricName");
            criteriaInfo.setMetricName(metricName);
            boolean serverSide = (Boolean) row.get("serverSide");
            criteriaInfo.setRoleType(serverSide ? MonitoringSystem.COLLECTION_LOCATION_SERVER
                            : MonitoringSystem.COLLECTION_LOCATION_CLIENT);
            String consumerName = (String) row.get("consumerName");
            if (consumerName != null)
                criteriaInfo.setConsumerName(consumerName);
            String serviceAdminName = (String) row.get("serviceAdminName");
            if (serviceAdminName != null)
                criteriaInfo.setServiceName(serviceAdminName);
            String operationName = (String) row.get("operationName");
            if (operationName != null)
                criteriaInfo.setOperationName(operationName);
            String machineName = (String) row.get("machineName");
            if (machineName != null)
                criteriaInfo.setMachineName(machineName);
            String machineGroupName = (String) row.get("machineGroupName");
            if (machineGroupName != null)
                criteriaInfo.setPoolName(machineGroupName);
            return criteriaInfo;
        }

        private String decodeMetricName(String encodedMetricName) {
            String metricName;
            if ("CallCount".equals(encodedMetricName))
                metricName = SystemMetricDefs.OP_TIME_TOTAL.getMetricName();
            else if ("ResponseTime".equals(encodedMetricName))
                metricName = SystemMetricDefs.OP_TIME_TOTAL.getMetricName();
            else if ("ErrorCount".equals(encodedMetricName))
                metricName = SystemMetricDefs.OP_ERR_TOTAL.getMetricName();
            else
                throw new IllegalArgumentException("Unknown metric name " + encodedMetricName);
            return metricName;
        }

        @Override
        public MetricData getMetricSummaryData(String dc, MetricCriteria metricCriteria,
                        MetricResourceCriteria metricResourceCriteria) {
            return null;
        }

        @Override
        public List<MetricGraphData> getMetricValue(CriteriaInfo criteriaInfo, long beginTime, long duration,
                        int aggregationPeriod, String autoDelay) {
            String encodedMetricName = criteriaInfo.getMetricName();
            String metricName = decodeMetricName(encodedMetricName);

            long endTime = beginTime + TimeUnit.SECONDS.toMillis(duration);
            boolean serverSide = MonitoringSystem.COLLECTION_LOCATION_SERVER.equals(criteriaInfo.getRoleType());

            List<Map<String, Object>> data = metricsDAO.findMetricComponentValuesByTimeStamp(metricName, beginTime,
                            endTime, serverSide, aggregationPeriod, criteriaInfo.getServiceName(),
                            criteriaInfo.getOperationName(), criteriaInfo.getConsumerName());

            Map<String, Map<String, Object>> map = transformAggregatedMetricComponentValues(encodedMetricName, data);

            List<MetricGraphData> result = new ArrayList<MetricGraphData>();
            for (int i = 0; i < duration / aggregationPeriod; ++i) {
                long startTime = beginTime + TimeUnit.SECONDS.toMillis(i * aggregationPeriod);
                long stopTime = startTime + TimeUnit.SECONDS.toMillis(aggregationPeriod);
                double value = 0;
                for (Map<String, Object> row : map.values()) {
                    long time = (Long) row.get("timeStamp");
                    if (startTime <= time && time < stopTime) {
                        value = (Double) row.get("value");
                        break;
                    }
                }
                MetricGraphData metricGraphData = new MetricGraphData();
                metricGraphData.setCount(value);
                metricGraphData.setTimeSlot(startTime);
                metricGraphData.setCriteria(null); // Not supported for now
                result.add(metricGraphData);
            }

            return result;
        }

        @Override
        public List<PolicyMetricData> getPolicyMetricData(long startTime, long endTime, String policyType,
                        String policyName, String serviceName, String operationName, String subjectTypeName,
                        String subjectValue, String effect) {
            return null;
        }

        @Override
        public List<PolicyMetricGraphData> getPolicyMetricDetailData(String policyName, String serviceName,
                        String operationName, String subjectTypeName, String subjectValue, String listType,
                        long startTime, long endTime) {
            return null;
        }

        @Override
        public List<MetricData> getStandardReportData(String reportType, MetricCriteria metricCriteria) {
            return null;
        }

        @Override
        public List<String> getMetricsMetadata(String resourceEntityType, List<String> resourceEntityName,
                        String resourceEntityResponseType) {
            ResourceEntity resourceEntity = ResourceEntity.fromValue(resourceEntityType);
            if (resourceEntity != ResourceEntity.SERVICE)
                throw new IllegalArgumentException("Unsupported input ResourceEntity " + resourceEntityType);

            ResourceEntity responseEntity = ResourceEntity.fromValue(resourceEntityResponseType);
            switch (responseEntity) {
                case SERVICE:
                    return metricsDAO.findMetricServiceAdminNames(resourceEntityName);
                case OPERATION:
                    return metricsDAO.findMetricOperationNames(resourceEntityName);
                case CONSUMER: {
                    return metricsDAO.findMetricConsumerNames(resourceEntityName);
                }
                default:
                    throw new IllegalArgumentException("Unsupported output ResourceEntity "
                                    + resourceEntityResponseType);
            }
        }

        private Map<String, Map<String, Object>> transformAggregatedMetricComponentValues(String encodedMetricName,
                        List<Map<String, Object>> rows) {
            Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
            for (Map<String, Object> row : rows) {
                // Need to create a key that is the same for the N
                // metricComponentDefs
                Map<String, Object> keyMap = new TreeMap<String, Object>(row);
                keyMap.remove("value");
                keyMap.remove("metricComponentDef");
                String key = keyMap.toString();

                Map<String, Object> existingRow = result.get(key);
                if (existingRow != null) {
                    MetricComponentDef metricComponentDef = (MetricComponentDef) row.get("metricComponentDef");
                    if ("CallCount".equals(encodedMetricName)) {
                        if ("count".equals(metricComponentDef.getName())) {
                            // Overwrite the value from the existing row, we are
                            // interested in the call count
                            result.put(key, row);
                        }
                    }
                    else if ("ResponseTime".equals(encodedMetricName)) {
                        if ("count".equals(metricComponentDef.getName())) {
                            Double count = (Double) row.get("value");
                            existingRow.put("value", count == 0 ? 0.0D : (Double) existingRow.get("value") / count);
                        }
                        else {
                            Double count = (Double) existingRow.get("value");
                            existingRow.put("value", count == 0 ? 0.0D : (Double) row.get("value") / count);
                        }
                    }
                    else {
                        throw new IllegalArgumentException("Unknown metric name " + encodedMetricName);
                    }
                }
                else {
                    result.put(key, row);
                }
            }
            return result;
        }

        private Map<String, Map<String, Object>> transformAggregatedErrorValues(List<Map<String, Object>> rows) {
            Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
            for (Map<String, Object> row : rows) {
                long errorId = (Long) row.get("errorId");
                result.put(String.valueOf(errorId), row);
            }
            return result;
        }

        @Override
        public List<ExtendedErrorViewData> getExtendedErrorMetricsData(String errorType, List<String> serviceNames,
                        List<String> operationNames, List<String> consumerNames, String errorIdString,
                        String errorCategoryString, String errorSeverityString, String errorNameParam,
                        MetricCriteria metricCriteria) {
            long firstStartTime = metricCriteria.getFirstStartTime();
            long secondStartTime = metricCriteria.getSecondStartTime();
            long duration = TimeUnit.SECONDS.toMillis(metricCriteria.getDuration());
            int aggregationPeriod = metricCriteria.getAggregationPeriod();
            boolean serverSide = !MonitoringSystem.COLLECTION_LOCATION_CLIENT.equals(metricCriteria.getRoleType());

            // Validate input parameters
            Long errorId = errorIdString == null ? null : Long.parseLong(errorIdString);
            String errorCategory = errorCategoryString == null ? null : ErrorCategory.fromValue(errorCategoryString)
                            .value();
            String errorSeverity = errorSeverityString == null ? null : ErrorSeverity.fromValue(errorSeverityString)
                            .value();

            Map<String, List<String>> filters = new HashMap<String, List<String>>();
            if (!serviceNames.isEmpty())
                filters.put(ResourceEntity.SERVICE.value(), serviceNames);
            if (!operationNames.isEmpty())
                filters.put(ResourceEntity.OPERATION.value(), operationNames);
            if (!consumerNames.isEmpty())
                filters.put(ResourceEntity.CONSUMER.value(), consumerNames);

            List<Map<String, Object>> errors1;
            double calls1;
            List<Map<String, Object>> errors2;
            double calls2;
            if ("Category".equals(errorType)) {
                errors1 = errorDAO.findErrorValuesByCategory(firstStartTime, firstStartTime + duration, serverSide,
                                aggregationPeriod, errorId, errorCategory, errorSeverity, filters);
                calls1 = metricsDAO.findCallsCount(firstStartTime, firstStartTime + duration, serverSide,
                                aggregationPeriod);
                errors2 = errorDAO.findErrorValuesByCategory(secondStartTime, secondStartTime + duration, serverSide,
                                aggregationPeriod, errorId, errorCategory, errorSeverity, filters);
                calls2 = metricsDAO.findCallsCount(secondStartTime, secondStartTime + duration, serverSide,
                                aggregationPeriod);
            }
            else if ("Severity".equals(errorType)) {
                errors1 = errorDAO.findErrorValuesBySeverity(firstStartTime, firstStartTime + duration, serverSide,
                                aggregationPeriod, errorId, errorCategory, errorSeverity, filters);
                calls1 = metricsDAO.findCallsCount(firstStartTime, firstStartTime + duration, serverSide,
                                aggregationPeriod);
                errors2 = errorDAO.findErrorValuesBySeverity(secondStartTime, secondStartTime + duration, serverSide,
                                aggregationPeriod, errorId, errorCategory, errorSeverity, filters);
                calls2 = metricsDAO.findCallsCount(secondStartTime, secondStartTime + duration, serverSide,
                                aggregationPeriod);
            }
            else {
                throw new IllegalArgumentException("Invalid error type " + errorType);
            }

            Map<String, Map<String, Object>> map1 = transformAggregatedErrorValues(errors1);
            Map<String, Map<String, Object>> map2 = transformAggregatedErrorValues(errors2);

            List<ExtendedErrorViewData> result = new ArrayList<ExtendedErrorViewData>();
            for (Map.Entry<String, Map<String, Object>> entry : map1.entrySet()) {
                ExtendedErrorViewData errorViewData = new ExtendedErrorViewData();
                Map<String, Object> row1 = entry.getValue();

                long errorId1 = (Long) row1.get("errorId");
                String errorName = (String) row1.get("errorName");
                org.ebayopensource.turmeric.monitoring.v1.services.Error error1 = new org.ebayopensource.turmeric.monitoring.v1.services.Error();
                error1.setErrorId(String.valueOf(errorId1));
                error1.setErrorName(errorName);
                errorViewData.setError(error1);

                long count1 = (Long) row1.get("errorCount");
                errorViewData.setErrorCount1(count1);
                errorViewData.setErrorCall1(calls1);
                // errorViewData.setErrorCallRatio1(calls1 == 0 ? 0 : count1 / calls1);

                Map<String, Object> row2 = map2.remove(entry.getKey());
                if (row2 != null) {
                    long count2 = (Long) row2.get("errorCount");
                    errorViewData.setErrorCount2(count2);
                    errorViewData.setErrorCall2(calls2);
                    // errorViewData.setErrorCallRatio2(calls2 == 0 ? 0 : count2 / calls2);
                }
                else {
                    errorViewData.setErrorCount2(0);
                    errorViewData.setErrorCall2(0);
                    // errorViewData.setErrorCallRatio2(0);
                }

                result.add(errorViewData);
            }
            for (Map.Entry<String, Map<String, Object>> entry : map2.entrySet()) {
                ExtendedErrorViewData errorViewData = new ExtendedErrorViewData();
                Map<String, Object> row2 = entry.getValue();

                long errorId2 = (Long) row2.get("errorId");
                String errorName = (String) row2.get("errorName");
                org.ebayopensource.turmeric.monitoring.v1.services.Error error2 = new org.ebayopensource.turmeric.monitoring.v1.services.Error();
                error2.setErrorId(String.valueOf(errorId2));
                error2.setErrorName(errorName);
                errorViewData.setError(error2);

                long count2 = (Long) row2.get("errorCount");
                errorViewData.setErrorCount2(count2);
                errorViewData.setErrorCall2(calls2);
                // errorViewData.setErrorCallRatio2(calls2 == 0 ? 0 : count2 / calls2);

                errorViewData.setErrorCount1(0);
                errorViewData.setErrorCall1(0);
                // errorViewData.setErrorCallRatio1(0);
                // errorViewData.setErrorDiff(100);
                // errorViewData.setRatioDiff(100);

                result.add(errorViewData);
            }

            // Sort the results
            final boolean sortAsc = metricCriteria.getSortOrder() == SortOrderType.ASCENDING;
            Collections.sort(result, new Comparator<ErrorViewData>() {
                @Override
                public int compare(ErrorViewData evd1, ErrorViewData evd2) {
                    double v1 = Math.max(evd1.getErrorCount1(), evd1.getErrorCount2());
                    double v2 = Math.max(evd2.getErrorCount1(), evd2.getErrorCount2());
                    if (v1 == v2) {
                        v1 = evd1.getErrorCount1() + evd1.getErrorCount2();
                        v2 = evd2.getErrorCount1() + evd2.getErrorCount2();
                    }
                    if (v1 == v2) {
                        v1 = evd1.getErrorCallRatio1();
                        v2 = evd2.getErrorCallRatio2();
                    }
                    if (v1 == v2) {
                        v1 = evd1.getErrorCallRatio1() + evd1.getErrorCallRatio2();
                        v2 = evd2.getErrorCallRatio2() + evd2.getErrorCallRatio2();
                    }
                    int result = v1 > v2 ? 1 : v1 < v2 ? -1 : 0;
                    return sortAsc ? result : -result;
                }
            });

            // Trim to the number of requested rows
            int rows = metricCriteria.getNumRows() == null ? 0 : Integer.parseInt(metricCriteria.getNumRows());
            trimResultList(result, rows);

            return result;
        }
    }
}
