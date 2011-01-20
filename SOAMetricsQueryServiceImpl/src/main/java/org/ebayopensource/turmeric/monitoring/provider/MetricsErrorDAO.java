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

public interface MetricsErrorDAO extends ErrorDAO {
    List<Map<String, Object>> findErrorValuesByCategory(long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Long errorId, String category, String severity, Map<String,List<String>> filters);

    List<Map<String, Object>> findErrorValuesBySeverity(long beginTime, long endTime, boolean serverSide, int aggregationPeriod, Long errorId, String category, String severity, Map<String,List<String>> filters);
}
