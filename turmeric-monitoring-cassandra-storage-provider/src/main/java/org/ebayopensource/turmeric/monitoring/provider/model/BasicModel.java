/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.model;

import java.util.List;
import java.util.Map;

/**
 * The Class Model.
 * 
 * @author jamuguerza
 */
public class BasicModel<K> {
		
		public BasicModel(K keyType){
		}		
		
		/** The key. */
		private K key;
		
//		  /** The metric name. */
//	    private String metricName;
//
//	    /** The service admin name. */
//	    private String serviceAdminName;

	    /** The operation name List. */
	    private Map<String, Object> columns;

		
		/**
		 * Sets the key.
		 *
		 * @param key the new key
		 */
		public void setKey(K key) {
			this.key = key;
		}

		/**
		 * Gets the key.
		 *
		 * @return the key
		 */
		public K getKey() {
			return key;
		}

		public void setColumns(Map<String, Object> columns) {
			this.columns = columns;
		}

		public Map<String, Object> getColumns() {
			return columns;
		}


//		public String getMetricName() {
//			return metricName;
//		}
//
//		public void setMetricName(String metricName) {
//			this.metricName = metricName;
//		}
//
//		public String getServiceAdminName() {
//			return serviceAdminName;
//		}
//
//		public void setServiceAdminName(String serviceAdminName) {
//			this.serviceAdminName = serviceAdminName;
//		}


		
	}