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


// TODO: Auto-generated Javadoc
/**
 * The Interface MetricsServiceConsumerByIpDAO.
 *
 * @param <SK> the generic type
 * @param <K> the key type
 * @author jose alvarez muguerza
 */
public interface MetricsServiceConsumerByIpDAO<SK, K>  {

	/**
	 * Find metric consumer names.
	 *
	 * @param serviceNames the service names
	 * @return the list
	 */
	List<String> findMetricConsumerNames(List<String> serviceNames);
    

}
