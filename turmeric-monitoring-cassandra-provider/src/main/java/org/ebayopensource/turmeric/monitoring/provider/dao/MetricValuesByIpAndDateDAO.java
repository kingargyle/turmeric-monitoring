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

import org.ebayopensource.turmeric.monitoring.provider.model.SuperModel;



// TODO: Auto-generated Javadoc
/**
 * The Interface MetricValuesByIpAndDateDAO.
 *
 * @param <SK> the generic type
 * @param <K> the key type
 * @author jose alvarez muguerza
 */
public interface MetricValuesByIpAndDateDAO<SK, K>  {
	 
 	/**
 	 * Find by range.
 	 *
 	 * @param fromSCNmame the from sc nmame
 	 * @param toSCNmame the to sc nmame
 	 * @return the list
 	 */
 	List<?> findByRange(K fromSCNmame, K toSCNmame);

}
