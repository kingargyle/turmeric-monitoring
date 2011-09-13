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



/**
 * The Interface BaseMetricsErrorsByFilterDAO.
 * @author jose alvarez muguerza
 */
public interface BaseMetricsErrorsByFilterDAO  {

    /**
	 * Given a <code>beginTime</code> and a <code>endTime</code> and a <code>aggregationPeriod</code>
	 * retrieve the Error values by Severity.
     * 
     * @param beginTime The beginning time since the epoch
     * @param endTime The end time since the epoch
     * @param serverSide true or false
     * @param aggregationPeriod The aggregation period to retrieve for.
     * @param errorId The errorId
     * @param filter The category/The severity
     * @param filters Filter values.
     * @return A List of Maps with resulting values.
     */
    List<Map<String, Object>> findErrorValuesByFilter(long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Long errorId, String filter, Map<String,List<String>> filters);

}
