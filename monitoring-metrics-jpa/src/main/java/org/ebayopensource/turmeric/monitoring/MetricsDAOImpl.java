/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.ebayopensource.turmeric.monitoring.model.Machine;
import org.ebayopensource.turmeric.monitoring.model.Metric;
import org.ebayopensource.turmeric.monitoring.model.MetricClassifier;
import org.ebayopensource.turmeric.monitoring.model.MetricComponentDef;
import org.ebayopensource.turmeric.monitoring.model.MetricComponentValue;
import org.ebayopensource.turmeric.monitoring.model.MetricDef;
import org.ebayopensource.turmeric.monitoring.model.MetricValue;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.SystemMetricDefs;
import org.ebayopensource.turmeric.utils.jpa.AbstractDAO;
import org.ebayopensource.turmeric.utils.jpa.EntityManagerContext;

public class MetricsDAOImpl extends AbstractDAO implements MetricsDAO {
    @Override
    public MetricDef findMetricDef(String metricName) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("from ").append(MetricDef.class.getName()).append(" as md");
        jpql.append(" where md.name = :name");

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("name", metricName);
        return getSingleResultOrNull(query);
    }

    @Override
    public void persistMetricDef(MetricDef metricDef) {
        persistEntity(metricDef);
    }

    @Override
    public Metric findMetric(String metricName, String serviceAdminName, String operationName) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("from ").append(Metric.class.getName()).append(" as m");
        jpql.append(" where m.metricDef.name = :name and m.serviceAdminName = :serviceAdminName and m.operationName = :operationName");

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("name", metricName);
        query.setParameter("serviceAdminName", serviceAdminName);
        query.setParameter("operationName", operationName);
        return getSingleResultOrNull(query);
    }

    @Override
    public void persistMetric(Metric metric) {
        persistEntity(metric);
    }

    @Override
    public void persistMetricValues(List<MetricValue> metricValues) {
        persistEntities(metricValues);
    }

    @Override
    public MetricClassifier findMetricClassifier(String consumerName, String sourceDC, String targetDC) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("from ").append(MetricClassifier.class.getName()).append(" as mc");
        jpql.append(" where mc.consumerName = :consumerName and mc.sourceDataCenter = :sourceDC and mc.targetDataCenter = :targetDC");

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("consumerName", consumerName);
        query.setParameter("sourceDC", sourceDC);
        query.setParameter("targetDC", targetDC);
        return getSingleResultOrNull(query);
    }

    @Override
    public void persistMetricClassifier(MetricClassifier metricClassifier) {
        persistEntity(metricClassifier);
    }

    @Override
    public List<MetricValue> findMetricValues(long startTimeStamp, long endTimeStamp, int aggregation) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("from ").append(MetricValue.class.getName()).append(" as mv");
        jpql.append(" where mv.timeStamp >= :start and mv.timeStamp < :end");
        jpql.append(" and mv.aggregationPeriod = :aggregation");

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("start", startTimeStamp);
        query.setParameter("end", endTimeStamp);
        query.setParameter("aggregation", aggregation);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> findMetricComponentValuesByService(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters) {
        List<String> serviceAdminNames = filters.get("Service");
        List<String> operationNames = filters.get("Operation");
        List<String> consumerNames = filters.get("Consumer");

        StringBuilder jpql = new StringBuilder();
        jpql.append("select");
        jpql.append(" sum(mcv.value)");
        jpql.append(", mcv.metricComponentDef.id");
        jpql.append(", mv.serverSide");
        jpql.append(", b.canonicalHostName");
        jpql.append(", bg.name");
        jpql.append(", m.serviceAdminName");
        if (operationNames != null)
            jpql.append(", m.operationName");
        if (consumerNames != null)
            jpql.append(", mc.consumerName");
        jpql.append(" from ").append(MetricComponentValue.class.getName()).append(" as mcv");
        jpql.append(" join mcv.metricValue mv");
        jpql.append(" join mv.metricClassifier mc");
        jpql.append(" join mv.metric m");
        jpql.append(" join mv.machine b");
        jpql.append(" left join b.machineGroup bg");
        jpql.append(" join m.metricDef md");
        jpql.append(" where md.name = :metricName");
        jpql.append(" and mv.timeStamp >= :beginTime and mv.timeStamp < :endTime");
        jpql.append(" and mv.serverSide = :serverSide");
        jpql.append(" and mv.aggregationPeriod = :aggregationPeriod");
        if (serviceAdminNames != null)
            jpql.append(" and m.serviceAdminName in (:serviceAdminNames)");
        if (operationNames != null)
            jpql.append(" and m.operationName in (:operationNames)");
        if (consumerNames != null)
            jpql.append(" and mc.consumerName in (:consumerNames)");
        jpql.append(" group by mcv.metricComponentDef.id");
        jpql.append(", mv.serverSide");
        jpql.append(", b.canonicalHostName");
        jpql.append(", bg.name");
        jpql.append(", m.serviceAdminName");
        if (operationNames != null)
            jpql.append(", m.operationName");
        if (consumerNames != null)
            jpql.append(", mc.consumerName");

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("metricName", metricName);
        query.setParameter("beginTime", beginTime);
        query.setParameter("endTime", endTime);
        query.setParameter("serverSide", serverSide);
        query.setParameter("aggregationPeriod", aggregationPeriod);
        if (serviceAdminNames != null)
            query.setParameter("serviceAdminNames", serviceAdminNames);
        if (operationNames != null)
            query.setParameter("operationNames", operationNames);
        if (consumerNames != null)
            query.setParameter("consumerNames", consumerNames);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Object[] items : rows) {
            int index = 0;
            Map<String, Object> row = new HashMap<String, Object>();
            result.add(row);
            row.put("value", items[index]);
            row.put("metricComponentDef", entityManager.find(MetricComponentDef.class, items[++index]));
            row.put("serverSide", items[++index]);
            row.put("machineName", items[++index]);
            row.put("machineGroupName", items[++index]);
            row.put("metricName", metricName);
            row.put("serviceAdminName", items[++index]);
            if (operationNames != null)
                row.put("operationName", items[++index]);
            if (consumerNames != null)
                row.put("consumerName", items[++index]);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> findMetricComponentValuesByOperation(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters) {
        List<String> serviceAdminNames = filters.get("Service");
        List<String> operationNames = filters.get("Operation");
        List<String> consumerNames = filters.get("Consumer");

        StringBuilder jpql = new StringBuilder();
        jpql.append("select");
        jpql.append(" sum(mcv.value)");
        jpql.append(", mcv.metricComponentDef.id");
        jpql.append(", mv.serverSide");
        jpql.append(", b.canonicalHostName");
        jpql.append(", bg.name");
        jpql.append(", m.serviceAdminName");
        jpql.append(", m.operationName");
        if (consumerNames != null)
            jpql.append(", mc.consumerName");
        jpql.append(" from ").append(MetricComponentValue.class.getName()).append(" as mcv");
        jpql.append(" join mcv.metricValue mv");
        jpql.append(" join mv.metricClassifier mc");
        jpql.append(" join mv.metric m");
        jpql.append(" join mv.machine b");
        jpql.append(" left join b.machineGroup bg");
        jpql.append(" join m.metricDef md");
        jpql.append(" where md.name = :metricName");
        jpql.append(" and mv.timeStamp >= :beginTime and mv.timeStamp < :endTime");
        jpql.append(" and mv.serverSide = :serverSide");
        jpql.append(" and mv.aggregationPeriod = :aggregationPeriod");
        if (serviceAdminNames != null)
            jpql.append(" and m.serviceAdminName in (:serviceAdminNames)");
        if (operationNames != null)
            jpql.append(" and m.operationName in (:operationNames)");
        if (consumerNames != null)
            jpql.append(" and mc.consumerName in (:consumerNames)");
        jpql.append(" group by mcv.metricComponentDef.id");
        jpql.append(", mv.serverSide");
        jpql.append(", b.canonicalHostName");
        jpql.append(", bg.name");
        jpql.append(", m.serviceAdminName");
        jpql.append(", m.operationName");
        if (consumerNames != null)
            jpql.append(", mc.consumerName");

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("metricName", metricName);
        query.setParameter("beginTime", beginTime);
        query.setParameter("endTime", endTime);
        query.setParameter("serverSide", serverSide);
        query.setParameter("aggregationPeriod", aggregationPeriod);
        if (serviceAdminNames != null)
            query.setParameter("serviceAdminNames", serviceAdminNames);
        if (operationNames != null)
            query.setParameter("operationNames", operationNames);
        if (consumerNames != null)
            query.setParameter("consumerNames", consumerNames);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Object[] items : rows) {
            int index = 0;
            Map<String, Object> row = new HashMap<String, Object>();
            result.add(row);
            row.put("value", items[index]);
            row.put("metricComponentDef", entityManager.find(MetricComponentDef.class, items[++index]));
            row.put("serverSide", items[++index]);
            row.put("machineName", items[++index]);
            row.put("machineGroupName", items[++index]);
            row.put("metricName", metricName);
            row.put("serviceAdminName", items[++index]);
            row.put("operationName", items[++index]);
            if (consumerNames != null)
                row.put("consumerName", items[++index]);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> findMetricComponentValuesByConsumer(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters) {
        List<String> serviceAdminNames = filters.get("Service");
        List<String> operationNames = filters.get("Operation");
        List<String> consumerNames = filters.get("Consumer");

        StringBuilder jpql = new StringBuilder();
        jpql.append("select");
        jpql.append(" sum(mcv.value)");
        jpql.append(", mcv.metricComponentDef.id");
        jpql.append(", mv.serverSide");
        jpql.append(", b.canonicalHostName");
        jpql.append(", bg.name");
        jpql.append(", mc.consumerName");
        if (serviceAdminNames != null)
            jpql.append(", m.serviceAdminName");
        if (operationNames != null)
            jpql.append(", m.operationName");
        jpql.append(" from ").append(MetricComponentValue.class.getName()).append(" as mcv");
        jpql.append(" join mcv.metricValue mv");
        jpql.append(" join mv.metricClassifier mc");
        jpql.append(" join mv.metric m");
        jpql.append(" join mv.machine b");
        jpql.append(" left join b.machineGroup bg");
        jpql.append(" join m.metricDef md");
        jpql.append(" where md.name = :metricName");
        jpql.append(" and mv.timeStamp >= :beginTime and mv.timeStamp < :endTime");
        jpql.append(" and mv.serverSide = :serverSide");
        jpql.append(" and mv.aggregationPeriod = :aggregationPeriod");
        if (consumerNames != null)
            jpql.append(" and mc.consumerName in (:consumerNames)");
        if (serviceAdminNames != null)
            jpql.append(" and m.serviceAdminName in (:serviceAdminNames)");
        if (operationNames != null)
            jpql.append(" and m.operationName in (:operationNames)");
        jpql.append(" group by mcv.metricComponentDef.id");
        jpql.append(", mv.serverSide");
        jpql.append(", b.canonicalHostName");
        jpql.append(", bg.name");
        jpql.append(", mc.consumerName");
        if (serviceAdminNames != null)
            jpql.append(", m.serviceAdminName");
        if (operationNames != null)
            jpql.append(", m.operationName");

        EntityManager entityManager = EntityManagerContext.get();
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("metricName", metricName);
        query.setParameter("beginTime", beginTime);
        query.setParameter("endTime", endTime);
        query.setParameter("serverSide", serverSide);
        query.setParameter("aggregationPeriod", aggregationPeriod);
        if (consumerNames != null)
            query.setParameter("consumerNames", consumerNames);
        if (serviceAdminNames != null)
            query.setParameter("serviceAdminNames", serviceAdminNames);
        if (operationNames != null)
            query.setParameter("operationNames", operationNames);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Object[] items : rows) {
            int index = 0;
            Map<String, Object> row = new HashMap<String, Object>();
            result.add(row);
            row.put("value", items[index]);
            row.put("metricComponentDef", entityManager.find(MetricComponentDef.class, items[++index]));
            row.put("serverSide", items[++index]);
            row.put("machineName", items[++index]);
            row.put("machineGroupName", items[++index]);
            row.put("metricName", metricName);
            row.put("consumerName", items[++index]);
            if (serviceAdminNames != null)
                row.put("serviceAdminName", items[++index]);
            if (operationNames != null)
                row.put("operationName", items[++index]);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> findMetricComponentValuesByMetric(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Map<String, List<String>> filters) {
        List<String> serviceAdminNames = filters.get("Service");
        List<String> operationNames = filters.get("Operation");
        List<String> consumerNames = filters.get("Consumer");

        StringBuilder jpql = new StringBuilder();
        jpql.append("select");
        jpql.append(" sum(mcv.value)");
        jpql.append(", mcv.metricComponentDef.id");
        jpql.append(", mv.serverSide");
        jpql.append(", b.canonicalHostName");
        jpql.append(", bg.name");
        jpql.append(", md.name");
        if (serviceAdminNames != null)
            jpql.append(", m.serviceAdminName");
        if (operationNames != null)
            jpql.append(", m.operationName");
        if (consumerNames != null)
            jpql.append(", mc.consumerName");
        jpql.append(" from ").append(MetricComponentValue.class.getName()).append(" as mcv");
        jpql.append(" join mcv.metricValue mv");
        jpql.append(" join mv.metricClassifier mc");
        jpql.append(" join mv.metric m");
        jpql.append(" join mv.machine b");
        jpql.append(" left join b.machineGroup bg");
        jpql.append(" join m.metricDef md");
        jpql.append(" where md.name like :metricName");
        jpql.append(" and mv.timeStamp >= :beginTime and mv.timeStamp < :endTime");
        jpql.append(" and mv.serverSide = :serverSide");
        jpql.append(" and mv.aggregationPeriod = :aggregationPeriod");
        if (serviceAdminNames != null)
            jpql.append(" and m.serviceAdminName in (:serviceAdminNames)");
        if (operationNames != null)
            jpql.append(" and m.operationName in (:operationNames)");
        if (consumerNames != null)
            jpql.append(" and mc.consumerName in (:consumerNames)");
        jpql.append(" group by mcv.metricComponentDef.id");
        jpql.append(", mv.serverSide");
        jpql.append(", b.canonicalHostName");
        jpql.append(", bg.name");
        jpql.append(", md.name");
        if (serviceAdminNames != null)
            jpql.append(", m.serviceAdminName");
        if (operationNames != null)
            jpql.append(", m.operationName");
        if (consumerNames != null)
            jpql.append(", mc.consumerName");

        EntityManager entityManager = EntityManagerContext.get();
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("metricName", metricName);
        query.setParameter("beginTime", beginTime);
        query.setParameter("endTime", endTime);
        query.setParameter("serverSide", serverSide);
        query.setParameter("aggregationPeriod", aggregationPeriod);
        if (serviceAdminNames != null)
            query.setParameter("serviceAdminNames", serviceAdminNames);
        if (operationNames != null)
            query.setParameter("operationNames", operationNames);
        if (consumerNames != null)
            query.setParameter("consumerNames", consumerNames);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Object[] items : rows) {
            int index = 0;
            Map<String, Object> row = new HashMap<String, Object>();
            result.add(row);
            row.put("value", items[index]);
            row.put("metricComponentDef", entityManager.find(MetricComponentDef.class, items[++index]));
            row.put("serverSide", items[++index]);
            row.put("machineName", items[++index]);
            row.put("machineGroupName", items[++index]);
            row.put("metricName", items[++index]);
            if (serviceAdminNames != null)
                row.put("serviceAdminName", items[++index]);
            if (operationNames != null)
                row.put("operationName", items[++index]);
            if (consumerNames != null)
                row.put("consumerName", items[++index]);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> findMetricComponentValuesByTimeStamp(String metricName, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, String serviceAdminName, String operationName, String consumerName) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select");
        jpql.append(" sum(mcv.value)");
        jpql.append(", mcv.metricComponentDef.id");
        jpql.append(", mv.timeStamp");
        if (serviceAdminName != null)
            jpql.append(", m.serviceAdminName");
        if (operationName != null)
            jpql.append(", m.operationName");
        if (consumerName != null)
            jpql.append(", mc.consumerName");
        jpql.append(" from ").append(MetricComponentValue.class.getName()).append(" as mcv");
        jpql.append(" join mcv.metricValue mv");
        jpql.append(" join mv.metricClassifier mc");
        jpql.append(" join mv.metric m");
        jpql.append(" join m.metricDef md");
        jpql.append(" where md.name = :metricName");
        jpql.append(" and mv.timeStamp >= :beginTime and mv.timeStamp < :endTime");
        jpql.append(" and mv.serverSide = :serverSide");
        jpql.append(" and mv.aggregationPeriod = :aggregationPeriod");
        if (serviceAdminName != null)
            jpql.append(" and m.serviceAdminName = :serviceAdminName");
        if (operationName != null)
            jpql.append(" and m.operationName = :operationName");
        if (consumerName != null)
            jpql.append(" and mc.consumerName = :consumerName");
        jpql.append(" group by mcv.metricComponentDef.id");
        jpql.append(", mv.timeStamp");
        if (serviceAdminName != null)
            jpql.append(", m.serviceAdminName");
        if (operationName != null)
            jpql.append(", m.operationName");
        if (consumerName != null)
            jpql.append(", mc.consumerName");

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("metricName", metricName);
        query.setParameter("beginTime", beginTime);
        query.setParameter("endTime", endTime);
        query.setParameter("serverSide", serverSide);
        query.setParameter("aggregationPeriod", aggregationPeriod);
        if (serviceAdminName != null)
            query.setParameter("serviceAdminName", serviceAdminName);
        if (operationName != null)
            query.setParameter("operationName", operationName);
        if (consumerName != null)
            query.setParameter("consumerName", consumerName);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Object[] items : rows) {
            int index = 0;
            Map<String, Object> row = new HashMap<String, Object>();
            result.add(row);
            row.put("value", items[index]);
            row.put("metricComponentDef", entityManager.find(MetricComponentDef.class, items[++index]));
            row.put("timeStamp", items[++index]);
            if (serviceAdminName != null)
                row.put("serviceAdminName", items[++index]);
            if (operationName != null)
                row.put("operationName", items[++index]);
            if (consumerName != null)
                row.put("consumerName", items[++index]);
        }

        return result;
    }

    @Override
    public List<String> findMetricServiceAdminNames(List<String> serviceAdminNames) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select m.serviceAdminName");
        jpql.append(" from ").append(Metric.class.getName()).append(" as m");
        if (!serviceAdminNames.isEmpty())
            jpql.append(" where m.serviceAdminName in (:serviceAdminNames)");
        jpql.append(" group by m.serviceAdminName");

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        if (!serviceAdminNames.isEmpty())
            query.setParameter("serviceAdminNames", serviceAdminNames);
        return query.getResultList();
    }

    @Override
    public List<String> findMetricOperationNames(List<String> serviceAdminNames) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select m.serviceAdminName, m.operationName");
        jpql.append(" from ").append(Metric.class.getName()).append(" as m");
        jpql.append(" where m.operationName is not null");
        if (!serviceAdminNames.isEmpty())
            jpql.append(" and m.serviceAdminName in (:serviceAdminNames)");
        jpql.append(" group by m.serviceAdminName, m.operationName");

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        if (!serviceAdminNames.isEmpty())
            query.setParameter("serviceAdminNames", serviceAdminNames);

        @SuppressWarnings("unchecked")
        List<Object[]> servicesAndOperations = query.getResultList();

        List<String> result = new ArrayList<String>();
        for (Object[] serviceAndOperation : servicesAndOperations)
            result.add(serviceAndOperation[0] + "." + serviceAndOperation[1]);

        return result;
    }

    @Override
    public Machine findMachine(String hostAddress) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("from ").append(Machine.class.getName()).append(" as m");
        jpql.append(" where m.hostAddress = :address");

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("address", hostAddress);
        return getSingleResultOrNull(query);
    }

    @Override
    public void persistMachine(Machine machine) {
        persistEntity(machine);
    }

    @Override
    public long findCallsCount(long beginTime, long endTime, boolean serverSide, int aggregationPeriod) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select sum(mcv.value)");
        jpql.append("from ").append(MetricComponentValue.class.getName()).append(" mcv");
        jpql.append(" join mcv.metricValue mv");
        jpql.append(" join mcv.metricComponentDef mc");
        jpql.append(" join mc.metricDef m");
        jpql.append(" where m.name = :metricName");
        jpql.append(" and mc.name = :metricComponentName");
        jpql.append(" and mv.serverSide = :serverSide");
        jpql.append(" and mv.aggregationPeriod = :aggregationPeriod");

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("metricName", SystemMetricDefs.OP_TIME_TOTAL.getMetricName());
        query.setParameter("metricComponentName", "count");
        query.setParameter("serverSide", serverSide);
        query.setParameter("aggregationPeriod", aggregationPeriod);
        Double result = getSingleResultOrNull(query);
        return result == null ? 0 : result.longValue();
    }

    @Override
    public List<String> findMetricConsumerNames(List<String> serviceAdminNames) {
        //select distinct mv.metricClassifier.consumerName from MetricValue where
        // mv.metric in (select m from metric where m.serviceAdminName in (:serviceAdminNames))
        StringBuilder jpql = new StringBuilder();
        jpql.append("select distinct mv.metricClassifier.consumerName");
        jpql.append(" from ").append(MetricValue.class.getName()).append(" mv");
        jpql.append(" where mv.metric in");
        jpql.append(" (select m from ").append(Metric.class.getName()).append(" m");
        jpql.append(" where m.serviceAdminName in (:serviceAdminNames))");
       
        
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        if (!serviceAdminNames.isEmpty())
            query.setParameter("serviceAdminNames", serviceAdminNames);

        @SuppressWarnings("unchecked")
        List<Object> consumerNames = query.getResultList();

        List<String> result = new ArrayList<String>();
        for (Object consumername : consumerNames)
            result.add(consumername.toString());

        return result;
    }
}
