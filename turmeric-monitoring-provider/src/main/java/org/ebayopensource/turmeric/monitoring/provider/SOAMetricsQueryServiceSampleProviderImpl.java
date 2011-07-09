/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXB;

import org.ebayopensource.turmeric.monitoring.provider.model.ExtendedErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.CriteriaInfo;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorInfos;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricValueRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricValueResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsMetadataRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsMetadataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGraphData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGroupData;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricResourceCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.PolicyMetricData;
import org.ebayopensource.turmeric.monitoring.v1.services.PolicyMetricGraphData;
import org.ebayopensource.turmeric.monitoring.v1.services.ReportCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.ResourceEntityRequest;

/**
 * The Class SOAMetricsQueryServiceSampleProviderImpl.
 */
public class SOAMetricsQueryServiceSampleProviderImpl implements SOAMetricsQueryServiceProvider {

    /** The Constant basePath. */
    private static final String basePath = "META-INF/metrics/data/";

    /** The get metrics metadata req list. */
    private static List<GetMetricsMetadataRequest> getMetricsMetadataReqList = new ArrayList<GetMetricsMetadataRequest>();

    /** The get metric value req list. */
    private static List<GetMetricValueRequest> getMetricValueReqList = new ArrayList<GetMetricValueRequest>();

    /** The get metrics req list. */
    private static List<GetMetricsRequest> getMetricsReqList = new ArrayList<GetMetricsRequest>();

    /**
     * Instantiates a new sOA metrics query service sample provider impl.
     */
    public SOAMetricsQueryServiceSampleProviderImpl() {
        loadRequestData();
    }

    /**
     * Load request data.
     */
    private static void loadRequestData() {
        String propertiesPath = basePath + "operation.properties";
        ClassLoader cl = SOAMetricsQueryServiceSampleProviderImpl.class.getClassLoader();
        Properties prop = new Properties();
        InputStream is = cl.getResourceAsStream(propertiesPath);
        try {
            prop.load(is);
        }
        catch (IOException e) {

            return;
        }
        String methodPath = basePath + "getMetricsMetadata/";
        populateList(methodPath, getMetricsMetadataReqList, GetMetricsMetadataRequest.class,
                        Integer.parseInt(prop.getProperty("getMetricsMetadata")));
        methodPath = basePath + "getMetricValue/";
        populateList(methodPath, getMetricValueReqList, GetMetricValueRequest.class,
                        Integer.parseInt(prop.getProperty("getMetricValue")));
        methodPath = basePath + "getMetricsData/";
        populateList(methodPath, getMetricsReqList, GetMetricsRequest.class,
                        Integer.parseInt(prop.getProperty("getMetricsData")));

    }

    /**
     * Populate list.
     * 
     * @param <T>
     *            the generic type
     * @param methodPath
     *            the method path
     * @param list
     *            the list
     * @param ref
     *            the ref
     * @param numFiles
     *            the num files
     */
    private static <T> void populateList(String methodPath, List<T> list, Class<T> ref, int numFiles) {
        // String methodPath = basePath + "getMetricsMetada/";
        T request;
        ClassLoader cl = SOAMetricsQueryServiceSampleProviderImpl.class.getClassLoader();

        /*
         * BufferedReader br = new BufferedReader(new InputStreamReader(is)); String str; try { while ((str =
         * br.readLine()) != null) { file_name = str; if (file_name.startsWith("request") && file_name.endsWith(".xml"))
         * { num_files++; } } } catch (Exception e) { return; }
         */
        // Arrays.sort(files);
        for (int i = 0; i < numFiles; i++) {

            request = JAXB.unmarshal(cl.getResourceAsStream(methodPath + "request/" + "request" + (i + 1) + ".xml"),
                            ref);
            list.add(request);
        }
    }

    /**
     * Gets the response data.
     * 
     * @param <T>
     *            the generic type
     * @param responseFileIndex
     *            the response file index
     * @param methodPath
     *            the method path
     * @param ref
     *            the ref
     * @return the response data
     */
    private static <T> T getResponseData(int responseFileIndex, String methodPath, Class<T> ref) {

        ClassLoader cl = SOAMetricsQueryServiceSampleProviderImpl.class.getClassLoader();
        InputStream is = cl.getResourceAsStream(methodPath + "response/response" + responseFileIndex + ".xml");
        T obj;
        obj = JAXB.unmarshal(is, ref);
        return obj;

    }

    /**
     * Find request file index.
     * 
     * @param resourceEntityType
     *            the resource entity type
     * @param resourceEntityName
     *            the resource entity name
     * @param resourceEntityResponseType
     *            the resource entity response type
     * @return the int
     */
    private static int findRequestFileIndex(String resourceEntityType, List<String> resourceEntityName,
                    String resourceEntityResponseType) {
        int result = 0;
        String entityType, entityResponseType;
        List<String> entityNames;
        GetMetricsMetadataRequest request;
        if (resourceEntityName == null) {
            resourceEntityName = new ArrayList<String>();
        }

        for (int i = 0; i < getMetricsMetadataReqList.size(); i++) {
            entityNames = new ArrayList<String>();
            request = getMetricsMetadataReqList.get(i);
            entityType = (request.getResourceEntityType() != null) ? request.getResourceEntityType().value() : null;
            entityResponseType = (request.getResourceEntityResponseType() != null) ? request
                            .getResourceEntityResponseType().value() : null;
            entityNames.addAll(request.getResourceEntityName());
            if (!resourceEntityType.equalsIgnoreCase(entityType)
                            || !stringEquals(resourceEntityResponseType, entityResponseType)) {
                continue;
            }

            if (entityNames.containsAll(resourceEntityName)) {
                entityNames.removeAll(resourceEntityName);
                if (entityNames.isEmpty()) {
                    result = i;
                    break;
                }
            }

        }
        return result + 1;
    }

    /**
     * Find request file index.
     * 
     * @param criteriaInfo
     *            the criteria info
     * @param startTime
     *            the start time
     * @param duration
     *            the duration
     * @param aggregationPeriod
     *            the aggregation period
     * @param autoDelay
     *            the auto delay
     * @return the int
     */
    private static int findRequestFileIndex(CriteriaInfo criteriaInfo, long startTime, long duration,
                    int aggregationPeriod, String autoDelay) {
        int result = 0;
        CriteriaInfo criteriaInfoRef;
        long startTimeRef, durationRef;
        int aggregationPeriodRef;
        String autoDelayRef;

        GetMetricValueRequest request;

        for (int i = 0; i < getMetricValueReqList.size(); i++) {
            request = getMetricValueReqList.get(i);
            criteriaInfoRef = request.getCriteriaInfo();
            startTimeRef = request.getStartTime();
            durationRef = request.getDuration();
            aggregationPeriodRef = request.getAggregationPeriod();
            autoDelayRef = request.getAutoDelay();

            /*
             * if (!(criteriaInfoRef.getServiceConsumerType().equals(criteriaInfo.getServiceConsumerType()) &&
             * criteriaInfoRef.getMetricName().equals(criteriaInfo.getMetricName()) &&
             * criteriaInfoRef.getConsumerName().equals(criteriaInfo.getConsumerName()) &&
             * criteriaInfoRef.getServiceName().equals(criteriaInfo.getServiceName()) &&
             * criteriaInfoRef.getOperationName().equals(criteriaInfo.getOperationName()) &&
             * criteriaInfoRef.getPoolName().equals(criteriaInfo.getPoolName()) &&
             * criteriaInfoRef.getMachineName().equals(criteriaInfo.getMachineName()) &&
             * criteriaInfoRef.getRoleType().equals(criteriaInfo.getRoleType()))){ continue; }
             */
            if (!(stringEquals(criteriaInfoRef.getServiceConsumerType(), criteriaInfo.getServiceConsumerType())
                            && stringEquals(criteriaInfoRef.getMetricName(), criteriaInfo.getMetricName())
                            && stringEquals(criteriaInfoRef.getConsumerName(), criteriaInfo.getConsumerName())
                            && stringEquals(criteriaInfoRef.getServiceName(), criteriaInfo.getServiceName())
                            && stringEquals(criteriaInfoRef.getOperationName(), criteriaInfo.getOperationName())
                            && stringEquals(criteriaInfoRef.getPoolName(), criteriaInfo.getPoolName())
                            && stringEquals(criteriaInfoRef.getMachineName(), criteriaInfo.getMachineName()) && stringEquals(
                            criteriaInfoRef.getRoleType(), criteriaInfo.getRoleType()))) {
                continue;
            }
            if (startTimeRef == startTime && durationRef == duration && aggregationPeriodRef == aggregationPeriod &&
            // autoDelayRef.equals(autoDelay)){
                            stringEquals(autoDelayRef, autoDelay)) {
                result = i;
                break;
            }

        }
        return result + 1;
    }

    /**
     * Find request file index.
     * 
     * @param metricCriteria
     *            the metric criteria
     * @param metricResourceCriteria
     *            the metric resource criteria
     * @return the int
     */
    private static int findRequestFileIndex(MetricCriteria metricCriteria, MetricResourceCriteria metricResourceCriteria) {
        int result = 0;
        MetricCriteria metricCriteriaRef;
        MetricResourceCriteria metricResourceCriteriaRef;
        List<ResourceEntityRequest> resourceEntityRequestList1, resourceEntityRequestList2;
        List<String> resourceEntityResponseName1, resourceEntityResponseName2;

        resourceEntityResponseName1 = metricResourceCriteria.getResourceEntityResponseName();

        resourceEntityRequestList1 = metricResourceCriteria.getResourceRequestEntities();

        GetMetricsRequest request;

        for (int i = 0; i < getMetricsReqList.size(); i++) {
            request = getMetricsReqList.get(i);
            metricCriteriaRef = request.getMetricCriteria();
            metricResourceCriteriaRef = request.getMetricResourceCriteria();
            resourceEntityResponseName2 = metricResourceCriteriaRef.getResourceEntityResponseName();

            if (!(metricCriteriaRef.getFirstStartTime() == metricCriteria.getFirstStartTime()
                            && metricCriteriaRef.getSecondStartTime() == metricCriteria.getSecondStartTime()
                            && metricCriteriaRef.getDuration() == metricCriteria.getDuration()
                            && metricCriteriaRef.getAggregationPeriod() == metricCriteria.getAggregationPeriod()
                            && metricCriteriaRef.getSortOrder() == metricCriteria.getSortOrder()
                            && stringEquals(metricCriteriaRef.getNumRows(), metricCriteria.getNumRows())
                            && stringEquals(metricCriteriaRef.getMetricName(), metricCriteria.getMetricName())
                            && stringEquals(metricCriteriaRef.getRoleType(), metricCriteria.getRoleType()) && stringEquals(
                            metricCriteriaRef.getAutoDelay(), metricCriteria.getAutoDelay()))) {
                continue;
            }

            if (!stringEquals(metricResourceCriteria.getResourceEntityResponseType(),
                            metricResourceCriteriaRef.getResourceEntityResponseType())) {
                continue;
            }

            if (resourceEntityResponseName1.size() != resourceEntityResponseName2.size()) {
                continue;
            }
            if (resourceEntityResponseName2.containsAll(resourceEntityResponseName1)) {
                resourceEntityResponseName2.removeAll(resourceEntityResponseName1);
                if (!resourceEntityResponseName2.isEmpty()) {
                    continue;
                }
            }
            else {
                continue;
            }

            resourceEntityRequestList2 = metricResourceCriteriaRef.getResourceRequestEntities();
            if (resourceEntityRequestList1.size() != resourceEntityRequestList2.size()) {
                continue;
            }
            int size = resourceEntityRequestList1.size();

            boolean isEquals = true;

            for (int j = 0; j < size; j++) {
                if (!resourceEntityRequestEquals(resourceEntityRequestList1.get(j), resourceEntityRequestList2.get(j))) {
                    isEquals = false;
                    break;

                }
            }
            if (!isEquals) {
                continue;
            }
            else {
                result = i;
                break;
            }

        }
        return result + 1;
    }

    /**
     * String equals.
     * 
     * @param str1
     *            the str1
     * @param str2
     *            the str2
     * @return true, if successful
     */
    private static boolean stringEquals(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 != null && str1.equalsIgnoreCase(str2)) {
            return true;
        }
        return false;
    }

    /**
     * Resource entity request equals.
     * 
     * @param req1
     *            the req1
     * @param req2
     *            the req2
     * @return true, if successful
     */
    private static boolean resourceEntityRequestEquals(ResourceEntityRequest req1, ResourceEntityRequest req2) {
        boolean result = false;
        List<String> list1, list2;

        if (req1 == null && req2 == null) {
            result = true;
        }
        if (req1 != null && req2 != null) {
            if (!stringEquals(req1.getResourceEntityType().value(), req2.getResourceEntityType().value())) {
                result = false;
            }
            else {
                list1 = req1.getResourceEntityName();
                list2 = req2.getResourceEntityName();
                if (list1.containsAll(list2)) {
                    list1.removeAll(list2);
                    if (list1.isEmpty()) {
                        result = true;
                    }
                    else {
                        result = false;
                    }
                }
                else {
                    result = false;
                }
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getCustomReportData(org.ebayopensource
     * .turmeric.monitoring.v1.services.ReportCriteria,
     * org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria)
     */
    @Override
    public List<MetricData> getCustomReportData(ReportCriteria reportCriteria, MetricCriteria metricCriteria) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getDetailData(java.lang.String,
     * org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria,
     * org.ebayopensource.turmeric.monitoring.v1.services.MetricResourceCriteria)
     */
    @Override
    public List<MetricData> getDetailData(String dc, MetricCriteria metricCriteria,
                    MetricResourceCriteria metricResourceCriteria) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getErrorGraph(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria)
     */
    @Override
    public List<MetricGraphData> getErrorGraph(String serviceName, String operationName, String consumerName,
                    String errorId, String errorCategory, String errorSeverity, MetricCriteria metricCriteria) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getErrorMetricsData(java.lang.
     * String, java.util.List, java.util.List, java.util.List, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria)
     */
    @Override
    public List<ErrorViewData> getErrorMetricsData(String errorType, List<String> serviceName,
                    List<String> operationName, List<String> consumerName, String errorId, String errorCategory,
                    String errorSeverity, String errorName, MetricCriteria metricCriteria) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getErrorMetricsMetadata(java.lang
     * .String, java.lang.String, java.lang.String)
     */
    @Override
    public ErrorInfos getErrorMetricsMetadata(String errorId, String errorName, String serviceName) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getMetricsData(org.ebayopensource
     * .turmeric.monitoring.v1.services.MetricCriteria,
     * org.ebayopensource.turmeric.monitoring.v1.services.MetricResourceCriteria)
     */
    @Override
    public List<MetricGroupData> getMetricsData(MetricCriteria metricCriteria,
                    MetricResourceCriteria metricResourceCriteria) {
        int responseFileIndex = findRequestFileIndex(metricCriteria, metricResourceCriteria);
        GetMetricsResponse response = null;
        if (responseFileIndex > 0) {
            response = getResponseData(responseFileIndex, basePath + "getMetricsData/", GetMetricsResponse.class);
        }
        return response.getReturnData();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getMetricSummaryData(java.lang
     * .String, org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria,
     * org.ebayopensource.turmeric.monitoring.v1.services.MetricResourceCriteria)
     */
    @Override
    public MetricData getMetricSummaryData(String dc, MetricCriteria metricCriteria,
                    MetricResourceCriteria metricResourceCriteria) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getMetricValue(org.ebayopensource
     * .turmeric.monitoring.v1.services.CriteriaInfo, long, long, int, java.lang.String)
     */
    @Override
    public List<MetricGraphData> getMetricValue(CriteriaInfo criteriaInfo, long startTime, long duration,
                    int aggregationPeriod, String autoDelay) {
        int responseFileIndex = findRequestFileIndex(criteriaInfo, startTime, duration, aggregationPeriod, autoDelay);
        GetMetricValueResponse response = null;
        if (responseFileIndex > 0) {
            response = getResponseData(responseFileIndex, basePath + "getMetricValue/", GetMetricValueResponse.class);
        }
        return response.getReturnData();
    }

    /*
     * (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getPolicyMetricData(long,
     * long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<PolicyMetricData> getPolicyMetricData(long startTime, long endTime, String policyType,
                    String policyName, String serviceName, String operationName, String subjectTypeName,
                    String subjectValue, String effect) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getPolicyMetricDetailData(java
     * .lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long,
     * long)
     */
    @Override
    public List<PolicyMetricGraphData> getPolicyMetricDetailData(String policyName, String serviceName,
                    String operationName, String subjectTypeName, String subjectValue, String listType, long startTime,
                    long endTime) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getStandardReportData(java.lang
     * .String, org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria)
     */
    @Override
    public List<MetricData> getStandardReportData(String reportType, MetricCriteria metricCriteria) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getMetricsMetadata(java.lang.String
     * , java.util.List, java.lang.String)
     */
    @Override
    public List<String> getMetricsMetadata(String resourceEntityType, List<String> resourceEntityName,
                    String resourceEntityResponseType) {
        int responseFileIndex = findRequestFileIndex(resourceEntityType, resourceEntityName, resourceEntityResponseType);
        GetMetricsMetadataResponse response = null;
        if (responseFileIndex > 0) {
            response = getResponseData(responseFileIndex, basePath + "getMetricsMetadata/",
                            GetMetricsMetadataResponse.class);
        }
        return response.getResourceEntityResponseNames();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.ebayopensource.turmeric.monitoring.provider.SOAMetricsQueryServiceProvider#getExtendedErrorMetricsData(java
     * .lang.String, java.util.List, java.util.List, java.util.List, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria)
     */
    @Override
    public List<ExtendedErrorViewData> getExtendedErrorMetricsData(String errorType, List<String> serviceName,
                    List<String> operationName, List<String> consumerName, String errorId, String errorCategory,
                    String errorSeverity, String errorName, MetricCriteria metricCriteria) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * MetricData metricData1, metricData2; MetricGraphData metricGraphData1, metricGraphData2; ErrorViewData
     * errorViewData1, errorViewData2; ErrorInfos errorInfos; MetricGroupData metricGroupData1, metricGroupData2;
     * PolicyMetricData policyMetricData1, policyMetricData2;
     */
    /*
     * List<CriteriaInfo> criteriaInfoList = new ArrayList<CriteriaInfo>(); List<PolicyMetricData> policyMetricDataList
     * = new ArrayList<PolicyMetricData>(); List<MetricGroupData> metricGroupDataList = new
     * ArrayList<MetricGroupData>(); List<MetricGraphData> metricGraphDataList = new ArrayList<MetricGraphData>();
     * List<MetricData> metricDataList = new ArrayList<MetricData>(); List<ErrorViewData> errorViewDataList = new
     * ArrayList<ErrorViewData>(); List<ErrorInfos> errorInfosList = new ArrayList<ErrorInfos>(); List<String>
     * metricsMetadataList = new ArrayList<String>(); private static final String csvPath = "/META-INF/metrics/data/";
     * private static final String criteriaInfoFilePath = csvPath+"CriteriaInfo.csv"; private static final String
     * metricDataFilePath = csvPath+"MetricData.csv"; private static final String metricGraphDataFilePath =
     * csvPath+"MetricGraphData.csv"; private static final String metricGroupDataFilePath =
     * csvPath+"MetricGroupData.csv"; private static final String policyMetricDataFilePath =
     * csvPath+"PolicyMetricData.csv"; private static final String errorViewDataFilePath = csvPath+"ErrorViewData.csv";
     * private static final String errorInfosFilePath = csvPath+"ErrorInfos.csv"; private static final String
     * metricsMetadataFilePath = csvPath+"MetricsMetadata.csv"; public SOAMetricsQueryServiceSampleProviderImpl(){
     * initDummyValues(); }
     * @Override public List<MetricData> getCustomReportData(ReportCriteria reportCriteria, MetricCriteria
     * metricCriteria) { return metricDataList; }
     * @Override public List<MetricData> getDetailData(String dc, MetricCriteria metricCriteria, MetricResourceCriteria
     * metricResourceCriteria) { return metricDataList; }
     * @Override public List<MetricGraphData> getErrorGraph(String serviceName, String operationName, String
     * consumerName, String errorId, String errorCategory, String errorSeverity, MetricCriteria metricCriteria) { return
     * metricGraphDataList; }
     * @Override public List<ErrorViewData> getErrorMetricsData(String errorType, List<String> serviceName, List<String>
     * operationName, List<String> consumerName, String errorId, String errorCategory, String errorSeverity, String
     * isErrorId, MetricCriteria metricCriteria) { return errorViewDataList; }
     * @Override public ErrorInfos getErrorMetricsMetadata(String errorId, String serviceName) { return
     * errorInfosList.get(0); }
     * @Override public MetricData getMetricSummaryData(String dc, MetricCriteria metricCriteria, MetricResourceCriteria
     * metricResourceCriteria) { return metricDataList.get(0); }
     * @Override public List<MetricGraphData> getMetricValue(CriteriaInfo criteriaInfo, long startTime, long duration,
     * int aggregationPeriod, String autoDelay) { return metricGraphDataList; }
     * @Override public List<MetricGroupData> getMetricsData(MetricCriteria metricCriteria, MetricResourceCriteria
     * metricResourceCriteria) { return metricGroupDataList; }
     * @Override public List<PolicyMetricData> getPolicyMetricData(long startTime, long endTime, String policyType,
     * String policyName, String serviceName, String operationName, String subjectTypeName, String subjectValue, String
     * effect) { return policyMetricDataList; }
     * @Override public List<PolicyMetricData> getPolicyMetricDetailData(String policyName, String serviceName, String
     * operationName, String subjectTypeName, String subjectValue, String listType, long startTime, long endTime) {
     * return policyMetricDataList; }
     * @Override public List<MetricData> getStandardReportData(String reportType, MetricCriteria metricCriteria){ return
     * metricDataList; }
     * @Override public List<String> getMetricsMetadata(String resourceEntityType, List<String> resourceEntityName,
     * String resourceEntityResponseType) { return metricsMetadataList; } private void initDummyValues(){ String
     * filePath = null; try{ filePath = criteriaInfoFilePath; populateCriteriaInfo(filePath); filePath =
     * policyMetricDataFilePath; populatePolicyMetricData(filePath); filePath = metricGroupDataFilePath;
     * populateMetricGroupData(filePath); filePath = metricGraphDataFilePath; populateMetricGraphData(filePath);
     * filePath = metricDataFilePath; populateMetricData(filePath); filePath = errorViewDataFilePath;
     * populateErrorViewData(filePath); filePath = errorInfosFilePath; populateErrorInfosData(filePath); filePath =
     * metricsMetadataFilePath; populateMetricsMetadata(filePath); }catch (Exception e) {
     * System.out.println("Exception while loading data from CSV : " + filePath); e.printStackTrace(); } } private void
     * populateCriteriaInfo(String fileName)throws Exception{ InputStream is =
     * SOAMetricsQueryServiceSampleProviderImpl.class.getResourceAsStream(fileName); BufferedReader br = new
     * BufferedReader(new InputStreamReader(is)); String str; String[] values; int index; CriteriaInfo criteriaInfo;
     * boolean header = true; while ((str = br.readLine()) != null && str.length() > 0){ if(header){ header = false;
     * continue; } values = str.split(","); criteriaInfo = new CriteriaInfo(); index = 0;
     * criteriaInfo.setServiceConsumerType(values[index]); criteriaInfo.setMetricName(values[++index]);
     * criteriaInfo.setServiceName(values[++index]); criteriaInfo.setOperationName(values[++index]);
     * criteriaInfo.setConsumerName(values[++index]); criteriaInfo.setPoolName(values[++index]);
     * criteriaInfo.setMachineName(values[++index]); criteriaInfo.setRoleType(values[++index]);
     * criteriaInfoList.add(criteriaInfo); } try { br.close(); is.close(); } catch (Exception e) { throw new
     * Exception("Exception while closing file streams",e); } } private void populatePolicyMetricData(String
     * fileName)throws Exception{ InputStream is =
     * SOAMetricsQueryServiceSampleProviderImpl.class.getResourceAsStream(fileName); BufferedReader br = new
     * BufferedReader(new InputStreamReader(is)); String str; String[] values; int index; PolicyMetricData
     * policyMetricData; boolean header = true; while ((str = br.readLine()) != null && str.length() > 0){ if (header){
     * header = false; continue; } values = str.split(","); policyMetricData = new PolicyMetricData(); index = 0;
     * policyMetricData.setHitCount(values[index]); policyMetricData.setListType(values[++index]);
     * policyMetricData.setSubjectType(values[++index]); policyMetricData.setSubjectValue(values[++index]);
     * policyMetricData.setEffectDuration(Long.parseLong(values[++index]));
     * policyMetricData.setPolicyName(values[++index]); policyMetricData.setOperationName(values[++index]);
     * policyMetricData.setServiceName(values[++index]); policyMetricData.setEffect(values[++index]);
     * policyMetricDataList.add(policyMetricData); } try { br.close(); is.close(); } catch (Exception e) { throw new
     * Exception("Exception while closing file streams",e); } } private void populateMetricGroupData(String
     * fileName)throws Exception{ InputStream is =
     * SOAMetricsQueryServiceSampleProviderImpl.class.getResourceAsStream(fileName); BufferedReader br = new
     * BufferedReader(new InputStreamReader(is)); String str; String[] values; int index; MetricGroupData
     * metricGroupData; boolean header = true; while ((str = br.readLine()) != null && str.length() > 0){ if (header){
     * header = false; continue; } values = str.split(","); metricGroupData = new MetricGroupData(); index = 0;
     * metricGroupData.setDiff(Double.parseDouble(values[index]));
     * metricGroupData.setCount2(Double.parseDouble(values[++index]));
     * metricGroupData.setCount1(Double.parseDouble(values[++index])); metricGroupDataList.add(metricGroupData); }
     * CriteriaInfo criteriaInfo; for(int i=0; i<metricGroupDataList.size(); i++){ criteriaInfo =
     * criteriaInfoList.get(i); metricGroupDataList.get(i).setCriteriaInfo(criteriaInfo); } try { br.close();
     * is.close(); } catch (Exception e) { throw new Exception("Exception while closing file streams",e); } } private
     * void populateMetricGraphData(String fileName)throws Exception{ InputStream is =
     * SOAMetricsQueryServiceSampleProviderImpl.class.getResourceAsStream(fileName); BufferedReader br = new
     * BufferedReader(new InputStreamReader(is)); String str; String[] values; int index; MetricGraphData
     * metricGraphData; boolean header = true; while ((str = br.readLine()) != null && str.length() > 0){ if (header){
     * header = false; continue; } values = str.split(","); metricGraphData = new MetricGraphData(); index = 0;
     * metricGraphData.setTimeSlot(Long.parseLong(values[index])); metricGraphData.setCriteria(values[++index]);
     * metricGraphData.setCount(Double.parseDouble(values[++index])); metricGraphDataList.add(metricGraphData); } try {
     * br.close(); is.close(); } catch (Exception e) { throw new Exception("Exception while closing file streams",e); }
     * } private void populateMetricData(String fileName)throws Exception{ InputStream is =
     * SOAMetricsQueryServiceSampleProviderImpl.class.getResourceAsStream(fileName); BufferedReader br = new
     * BufferedReader(new InputStreamReader(is)); String str; String[] values; int index; MetricData metricData; boolean
     * header = true; while ((str = br.readLine()) != null && str.length() > 0){ if (header){ header = false; continue;
     * } values = str.split(","); metricData = new MetricData(); index = 0;
     * metricData.setErrorCountDiff(Double.parseDouble(values[index]));
     * metricData.setCallCountDiff(Double.parseDouble(values[++index]));
     * metricData.setResponseTimeDiff(Double.parseDouble(values[++index]));
     * metricData.setRatioDiff(Double.parseDouble(values[++index]));
     * metricData.setErrorCountRatio2(Double.parseDouble(values[++index]));
     * metricData.setErrorCountRatio1(Double.parseDouble(values[++index]));
     * metricData.setErrorCount2(Long.parseLong(values[++index]));
     * metricData.setErrorCount1(Long.parseLong(values[++index]));
     * metricData.setCallCount2(Long.parseLong(values[++index]));
     * metricData.setCallCount1(Long.parseLong(values[++index]));
     * metricData.setResponseTime2(Double.parseDouble(values[++index]));
     * metricData.setResponseTime1(Double.parseDouble(values[++index])); metricDataList.add(metricData); } CriteriaInfo
     * criteriaInfo; for(int i=0; i<metricDataList.size(); i++){ criteriaInfo = criteriaInfoList.get(i);
     * metricDataList.get(i).setCriteriaInfo(criteriaInfo); } try { br.close(); is.close(); } catch (Exception e) {
     * throw new Exception("Exception while closing file streams",e); } } private void populateErrorViewData(String
     * fileName)throws Exception{ InputStream is =
     * SOAMetricsQueryServiceSampleProviderImpl.class.getResourceAsStream(fileName); BufferedReader br = new
     * BufferedReader(new InputStreamReader(is)); String str; String[] values; int index; ErrorViewData errorViewData;
     * boolean header = true; while ((str = br.readLine()) != null && str.length() > 0){ if (header){ header = false;
     * continue; } values = str.split(","); errorViewData = new ErrorViewData(); index = 0;
     * errorViewData.setCriteria(values[index]); errorViewData.setRatioDiff(Double.parseDouble(values[++index]));
     * errorViewData.setErrorDiff(Double.parseDouble(values[++index]));
     * errorViewData.setErrorCallRatio2(Double.parseDouble(values[++index]));
     * errorViewData.setErrorCallRatio1(Double.parseDouble(values[++index]));
     * errorViewData.setErrorCount2(Long.parseLong(values[++index]));
     * errorViewData.setErrorCount1(Long.parseLong(values[++index])); errorViewDataList.add(errorViewData); } try {
     * br.close(); is.close(); } catch (Exception e) { throw new Exception("Exception while closing file streams",e); }
     * } private void populateErrorInfosData(String fileName)throws Exception{ InputStream is =
     * SOAMetricsQueryServiceSampleProviderImpl.class.getResourceAsStream(fileName); BufferedReader br = new
     * BufferedReader(new InputStreamReader(is)); String str; String[] values; int index; ErrorInfos errorInfos; boolean
     * header = true; while ((str = br.readLine()) != null && str.length() > 0){ if(header){ header = false; continue; }
     * values = str.split(","); errorInfos = new ErrorInfos(); index = 0; errorInfos.setId(values[index]);
     * errorInfos.setName(values[++index]); errorInfos.setCategory(values[++index]);
     * errorInfos.setSeverity(values[++index]); errorInfos.setDomain(values[++index]);
     * errorInfos.setSubDomain(values[++index]); errorInfosList.add(errorInfos); } try { br.close(); is.close(); } catch
     * (Exception e) { throw new Exception("Exception while closing file streams",e); } } private void
     * populateMetricsMetadata(String fileName)throws Exception{ InputStream is =
     * SOAMetricsQueryServiceSampleProviderImpl.class.getResourceAsStream(fileName); BufferedReader br = new
     * BufferedReader(new InputStreamReader(is)); String str; while ((str = br.readLine()) != null && str.length() > 0){
     * metricsMetadataList.add(str); } try { br.close(); is.close(); } catch (Exception e) { throw new
     * Exception("Exception while closing file streams",e); } }
     */

    /*
     * public static void main(String args[]){ SOAMetricsQueryServiceSampleProviderImpl obj = new
     * SOAMetricsQueryServiceSampleProviderImpl(); List<String> names = new ArrayList<String>();
     * names.add("BulkDataExchangeService"); names.add("MetadataDependencyService"); names.add("PolicyService");
     * names.add("ShippingCalculatorService"); List<String> list = obj.getMetricsMetadata("Service", names,
     * "Operation"); for(String str :list){ System.out.println(str); } GetMetricsMetadataRequest request = new
     * GetMetricsMetadataRequest(); request = JAXB.unmarshal(new
     * File("meta-src/META-INF/metrics/data/getMetricsMetadata/request/request3.xml"), GetMetricsMetadataRequest.class);
     * List<String> result = obj.getMetricsMetadata(request.getResourceEntityType().value(),
     * request.getResourceEntityName(), request.getResourceEntityResponseType().value()); for(String str : result){
     * System.out.println(str); } GetMetricValueRequest request = new GetMetricValueRequest(); request =
     * JAXB.unmarshal(new File("meta-src/META-INF/metrics/data/getMetricValue/request/request6.xml"),
     * GetMetricValueRequest.class); List<MetricGraphData> result = obj.getMetricValue(request.getCriteriaInfo(),
     * request.getStartTime(), request.getDuration(), request.getAggregationPeriod(), request.getAutoDelay());
     * for(MetricGraphData data : result){ System.out.println(data.getTimeSlot()+","+data.getCount()); }
     * GetMetricsRequest request = new GetMetricsRequest(); request = JAXB.unmarshal(new
     * File("meta-src/META-INF/metrics/data/getMetricsData/request/request82.xml"), GetMetricsRequest.class);
     * List<MetricGroupData> result = obj.getMetricsData(request.getMetricCriteria(),
     * request.getMetricResourceCriteria()); for(MetricGroupData data : result){
     * System.out.println(data.getCriteriaInfo(
     * ).getConsumerName()+","+data.getDiff()+","+data.getCount1()+","+data.getCount2
     * ()+","+data.getCriteriaInfo().getMetricName()); } }
     */
}
