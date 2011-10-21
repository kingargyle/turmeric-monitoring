/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao;

import org.ebayopensource.turmeric.monitoring.provider.model.Error;

/**
 * The Interface MetricsErrorDAO.
 * 
 * @param <K>
 *           the key type
 * @author jose alvarez muguerza
 */
public interface MetricsErrorByIdDAO<K> {

   /**
    * Find.
    * 
    * @param key
    *           the key
    * @return the error
    */
   public Error<?> find(K key);

   /**
    * Find count by time range.
    * 
    * @param key
    *           the key
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @return the long
    */
   public Long findCountByTimeRange(K key, Long startTime, Long endTime);
}
