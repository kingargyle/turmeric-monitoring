/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.persistence.EntityManagerFactory;

import org.ebayopensource.turmeric.monitoring.MetricsDAO;
import org.ebayopensource.turmeric.monitoring.MetricsDAOImpl;
import org.ebayopensource.turmeric.monitoring.model.Machine;
import org.ebayopensource.turmeric.monitoring.model.Metric;
import org.ebayopensource.turmeric.monitoring.model.MetricCategory;
import org.ebayopensource.turmeric.monitoring.model.MetricClassifier;
import org.ebayopensource.turmeric.monitoring.model.MetricComponentDef;
import org.ebayopensource.turmeric.monitoring.model.MetricComponentValue;
import org.ebayopensource.turmeric.monitoring.model.MetricDef;
import org.ebayopensource.turmeric.monitoring.model.MetricValue;
import org.ebayopensource.turmeric.monitoring.model.MonitoringLevel;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.SystemMetricDefs;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricId;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricComponentType;
import org.ebayopensource.turmeric.utils.jpa.EntityManagerContext;
import org.ebayopensource.turmeric.utils.jpa.PersistenceContext;

/**
 * The Class CSVImporter.
 */
public class CSVImporter {
    
    /**
     * The main method.
     *
     * @param args the arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        String resourceName = "META-INF/metrics/data/dbexport/OpenSourceCSVData.zip";
        if (args.length == 1)
            resourceName = args[0];

        EntityManagerFactory factory = PersistenceContext.createEntityManagerFactory("metrics");
        EntityManagerContext.open(factory);

        MetricsDAO metricsDAO = new MetricsDAOImpl();

        Machine localMachine = Machine.newMachine();
        Machine machine = metricsDAO.findMachine(localMachine.getHostAddress());
        if (machine == null)
        {
            machine = localMachine;
            metricsDAO.persistMachine(machine);
        }

        Map<Long, MetricClassifier> classifiers = saveConsumers(metricsDAO, loadConsumers(resourceName));
        Map<Long, Metric> metrics = saveMetrics(metricsDAO, loadMetrics(resourceName));

        saveMetricsData(resourceName, metricsDAO, classifiers, metrics, machine);

        EntityManagerContext.close();
    }

    private static Map<Long, String> loadConsumers(String resourceName) throws IOException {
        ZipInputStream zipStream = newZipStream(resourceName);
        try {
            Pattern pattern = Pattern.compile("\\d{4}/\\d{2}/\\d{2}/\\d{2},(\\d+),(.+)");

            Map<Long, String> result = new HashMap<Long, String>();

            ZipEntry zipEntry;
            while ((zipEntry = zipStream.getNextEntry()) != null) {
                if (zipEntry.getName().endsWith("SOA_USE_CASE.csv")) {
                    BufferedReader lineReader = new BufferedReader(new InputStreamReader(zipStream, "UTF-8"));
                    String line;
                    while ((line = lineReader.readLine()) != null) {
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.matches()) {
                            long id = Long.parseLong(matcher.group(1));
                            String name = matcher.group(2).trim();
                            result.put(id, name);
                        }
                    }
                    break;
                }
            }

            return result;
        } finally {
            zipStream.close();
        }
    }

    private static Map<Long, Metric> loadMetrics(String resourceName) throws IOException {
        ZipInputStream zipStream = newZipStream(resourceName);
        try {
            Pattern pattern = Pattern.compile("\\d{4}/\\d{2}/\\d{2}/\\d{2},(\\d+),([^,]+),([^,]+),(.+)");

            Map<Long, Metric> result = new HashMap<Long, Metric>();

            ZipEntry zipEntry;
            while ((zipEntry = zipStream.getNextEntry()) != null) {
                if (zipEntry.getName().endsWith("SOA_METRIC.csv")) {
                    BufferedReader lineReader = new BufferedReader(new InputStreamReader(zipStream, "UTF-8"));
                    String line;
                    while ((line = lineReader.readLine()) != null) {
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.matches()) {
                            long id = Long.parseLong(matcher.group(1));
                            String metricName = matcher.group(2).trim();
                            String serviceName = matcher.group(3).trim();
                            String operationName = matcher.group(4).trim();

                            // Only save operation metrics, since service metrics are not used
                            // by the current implementation of SOAMetricsQueryService
                            if (!"null".equals(operationName)) {
                                // EBay's implementation was removing the "Op." from the metric
                                // Here we restore it if the metric has an operation name associated
                                if (!metricName.startsWith("SoaFwk.Op."))
                                    metricName = metricName.replace("SoaFwk.", "SoaFwk.Op.");

                                MetricDef metricDef = null;
                                for (SystemMetricDefs.OpLevelMetricDef sysMetricDef : SystemMetricDefs.getAllOperationMetrics()) {
                                    if (metricName.equals(sysMetricDef.getMetricName())) {
                                        metricDef = new MetricDef(metricName, MetricCategory.from(sysMetricDef.getCategory().value()), MonitoringLevel.from(sysMetricDef.getLevel().getValue()));
                                        List<MetricComponentType> metricComponentTypes = sysMetricDef.getValueFactory().create(new MetricId(metricName, serviceName, operationName)).getAllComponentsTypes();
                                        for (MetricComponentType metricComponentType : metricComponentTypes) {
                                            MetricComponentDef metricComponentDef = new MetricComponentDef(metricComponentType.getName(), metricComponentType.getType().getName());
                                            metricDef.addMetricComponentDef(metricComponentDef);
                                        }
                                        break;
                                    }
                                }
                                if (metricDef != null) {
                                    Metric metric = new Metric(serviceName, operationName, metricDef);
                                    result.put(id, metric);
                                }
                            }
                        }
                    }
                    break;
                }
            }

            return result;
        } finally {
            zipStream.close();
        }
    }

    private static Map<Long, MetricClassifier> saveConsumers(MetricsDAO metricsDAO, Map<Long, String> consumers) {
        Map<Long, MetricClassifier> result = new HashMap<Long, MetricClassifier>();
        for (Map.Entry<Long, String> entry : consumers.entrySet()) {
            String consumer = entry.getValue();
            String unknown = "unknown";
            MetricClassifier dbMetricClassifier = metricsDAO.findMetricClassifier(consumer, unknown, unknown);
            if (dbMetricClassifier == null) {
                dbMetricClassifier = new MetricClassifier(consumer, unknown, unknown);
                metricsDAO.persistMetricClassifier(dbMetricClassifier);
            }
            result.put(entry.getKey(), dbMetricClassifier);
        }
        return result;
    }

    private static Map<Long, Metric> saveMetrics(MetricsDAO metricsDAO, Map<Long, Metric> metrics) {
        Map<Long, Metric> result = new HashMap<Long, Metric>();
        for (Map.Entry<Long, Metric> entry : metrics.entrySet()) {
            Metric metric = entry.getValue();
            MetricDef metricDef = metric.getMetricDef();
            MetricDef dbMetricDef = metricsDAO.findMetricDef(metricDef.getName());
            if (dbMetricDef == null) {
                metricsDAO.persistMetricDef(metricDef);
                dbMetricDef = metricDef;
            }

            Metric dbMetric = metricsDAO.findMetric(metric.getMetricDef().getName(), metric.getServiceAdminName(), metric.getOperationName());
            if (dbMetric == null) {
                dbMetric = new Metric(metric.getServiceAdminName(), metric.getOperationName(), dbMetricDef);
                metricsDAO.persistMetric(dbMetric);
            }

            result.put(entry.getKey(), dbMetric);
        }

        return result;
    }

    private static void saveMetricsData(String resourceName, MetricsDAO metricsDAO, Map<Long, MetricClassifier> metricClassifiers, Map<Long, Metric> metrics, Machine machine) throws IOException {
        ZipInputStream zipStream = newZipStream(resourceName);
        try {
            Pattern pattern = Pattern.compile("\\d{4}/\\d{2}/\\d{2}/\\d{2},(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+)\\s*");

            List<MetricValue> result = new ArrayList<MetricValue>();

            ZipEntry zipEntry;
            while ((zipEntry = zipStream.getNextEntry()) != null) {
                if (zipEntry.getName().contains("SOA_1H_POOL_METRIC_")) {
                    BufferedReader lineReader = new BufferedReader(new InputStreamReader(zipStream, "UTF-8"));
                    String line;
                    while ((line = lineReader.readLine()) != null) {
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.matches()) {
                            long timeStamp = Long.parseLong(matcher.group(2)) * 1000L;
                            long metricId = Long.parseLong(matcher.group(5));
                            long consumerId = Long.parseLong(matcher.group(6));
                            boolean serverSide = Long.parseLong(matcher.group(8)) == 0;
                            double value1 = Double.parseDouble(matcher.group(9));
                            double value2 = Double.parseDouble(matcher.group(10));

                            MetricClassifier metricClassifier = metricClassifiers.get(consumerId);
                            if (metricClassifier == null)
                                continue;

                            Metric metric = metrics.get(metricId);
                            if (metric == null)
                                continue;

                            MetricValue metricValue = new MetricValue(metric, metricClassifier, machine);
                            metricValue.setServerSide(serverSide);
                            metricValue.setTimeStamp(timeStamp);
                            metricValue.setAggregationPeriod(3600);
                            for (MetricComponentDef metricComponentDef : metric.getMetricDef().getMetricComponentDefs()) {
                                // These are the possible names of metric components
                                // This information is lost in EBay's CSV data, and here we restore it
                                if ("value".equals(metricComponentDef.getName()) ||
                                        "count".equals(metricComponentDef.getName())) {
                                    MetricComponentValue metricComponentValue = new MetricComponentValue(metricComponentDef, value1);
                                    metricValue.addMetricComponentValue(metricComponentValue);
                                } else if ("totalTime".equals(metricComponentDef.getName())) {
                                    MetricComponentValue metricComponentValue = new MetricComponentValue(metricComponentDef, value2);
                                    metricValue.addMetricComponentValue(metricComponentValue);
                                } else {
                                    throw new IllegalArgumentException(metricComponentDef.getName());
                                }
                            }

                            result.add(metricValue);
                        }
                    }
                    break;
                }
            }

            metricsDAO.persistMetricValues(result);

        } finally {
            zipStream.close();
        }
    }

    private static ZipInputStream newZipStream(String resourceName) throws FileNotFoundException {
        InputStream resourceStream = CSVImporter.class.getClassLoader().getResourceAsStream(resourceName);
        if (resourceStream == null)
            throw new FileNotFoundException(resourceName);
        return new ZipInputStream(resourceStream);
    }
}
