/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.runtime.error.ErrorDAOImpl;
import org.ebayopensource.turmeric.runtime.error.model.ErrorValue;

public class MetricsErrorDAOImpl extends ErrorDAOImpl implements MetricsErrorDAO
{
    @Override
    public List<Map<String, Object>> findErrorValuesByCategory(long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Long errorId, String category, String severity, Map<String, List<String>> filters) {
        return findErrorValues("Category", beginTime, endTime, serverSide, aggregationPeriod, errorId, category, severity, filters);
    }

    @Override
    public List<Map<String, Object>> findErrorValuesBySeverity(long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Long errorId, String category, String severity, Map<String, List<String>> filters) {
        return findErrorValues("Severity", beginTime, endTime, serverSide, aggregationPeriod, errorId, category, severity, filters);
    }

    private List<Map<String, Object>> findErrorValues(String groupBy, long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Long errorId, String category, String severity, Map<String, List<String>> filters) {
        List<String> serviceAdminNames = filters.get("Service");
        List<String> operationNames = filters.get("Operation");
        List<String> consumerNames = filters.get("Consumer");

        StringBuilder jpql = new StringBuilder();
        jpql.append("select");
        jpql.append(" count(ev)");
        jpql.append(", e.errorId");
        jpql.append(", e.name");
        if (consumerNames != null)
            jpql.append(", ev.consumerName");
        jpql.append(" from ").append(ErrorValue.class.getName()).append(" ev");
        jpql.append(" join ev.error e");
        jpql.append(" where ev.timeStamp >= :beginTime and ev.timeStamp < :endTime");
        jpql.append(" and ev.serverSide = :serverSide");
        if (errorId != null)
            jpql.append(" and e.errorId = :errorId");
        if (category != null)
            jpql.append(" and e.category = :category");
        if (severity != null)
            jpql.append(" and e.severity = :severity");
        if (serviceAdminNames != null)
            jpql.append(" and ev.serviceAdminName in (:serviceAdminNames)");
        if (operationNames != null)
            jpql.append(" and ev.operationName in (:operationNames)");
        if (consumerNames != null)
            jpql.append(" and ev.consumerName in (:consumerNames)");
        if ("Category".equals(groupBy))
            jpql.append(" group by e.category");
        else if ("Severity".equals(groupBy))
            jpql.append(" group by e.severity");
        jpql.append(", e.errorId");
        jpql.append(", e.name");
        if (consumerNames != null)
            jpql.append(", ev.consumerName");

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("beginTime", beginTime);
        query.setParameter("endTime", endTime);
        query.setParameter("serverSide", serverSide);
        if (errorId != null)
            query.setParameter("errorId", errorId);
        if (category != null)
            query.setParameter("category", ErrorCategory.fromValue(category));
        if (severity != null)
            query.setParameter("severity", ErrorSeverity.fromValue(severity));
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
            row.put("errorCount", items[index]);
            row.put("errorId", items[++index]);
            row.put("errorName", items[++index]);
            if (consumerNames != null)
                row.put("consumerName", items[++index]);
            row.put("serverSide", serverSide);
        }

        return result;
    }
}
