/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.runtime.error.ErrorDAO;

/**
 * Extends the ErrorDAO class with additional methods for retrieval of error values.
 * 
 * @author sbordet
 *
 * @see org.ebayopensource.turmeric.runtime.error.ErrorDAO
 */
public interface MetricsErrorDAO extends ErrorDAO {
	/**
	 * Given a <code>beginTime</code> and a <code>endTime</code> and a <code>aggregationPeriod</code>
	 * retrieve the Error values by Category.
	 * 
	 * @param beginTime The beginning time since the epoch
	 * @param endTime  The end time since the epoch
	 * @param serverSide true or false
	 * @param aggregationPeriod  The aggregation period to retrieve for.
	 * @param errorId The errorId to retrieve
	 * @param category The category to retrieve
	 * @param severity The severity
	 * @param filters Any filters
	 * @return A List of Maps with the resulting values.
	 */
    List<Map<String, Object>> findErrorValuesByCategory(long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Long errorId, String category, String severity, Map<String,List<String>> filters);
    
    
    
    /**
     * Find all error values by category.
     *
     * @param beginTime the begin time
     * @param endTime the end time
     * @param serverSide the server side
     * @param aggregationPeriod the aggregation period
     * @param errorId the error id
     * @param category the category
     * @param severity the severity
     * @param filters the filters
     * @return the list
     */
    List<Map<String, Object>>  findAllErrorValuesByCategory(long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Long errorId, String category, String severity, Map<String,List<String>> filters);
    
    
    /**
	 * Given a <code>beginTime</code> and a <code>endTime</code> and a <code>aggregationPeriod</code>
	 * retrieve the Error values by Severity.
     * 
     * @param beginTime The beginning time since the epoch
     * @param endTime The end time since the epoch
     * @param serverSide true or false
     * @param aggregationPeriod The aggregation period to retrieve for.
     * @param errorId The errorId
     * @param category The category
     * @param severity The severity
     * @param filters Filter values.
     * @return A List of Maps with resulting values.
     */
    List<Map<String, Object>> findErrorValuesBySeverity(long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Long errorId, String category, String severity, Map<String,List<String>> filters);
}
