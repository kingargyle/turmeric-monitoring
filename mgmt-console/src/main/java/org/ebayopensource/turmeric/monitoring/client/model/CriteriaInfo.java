/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

/**
 * The Interface CriteriaInfo.
 */
public interface CriteriaInfo {

    /**
     * Gets the service consumer type.
     *
     * @return the service consumer type
     */
    public String getServiceConsumerType();
    
    /**
     * Gets the service name.
     *
     * @return the service name
     */
    public String getServiceName();
    
    /**
     * Gets the operation name.
     *
     * @return the operation name
     */
    public String getOperationName();
  
    /**
     * Gets the consumer name.
     *
     * @return the consumer name
     */
    public String getConsumerName();
   
    /**
     * Gets the machine name.
     *
     * @return the machine name
     */
    public String getMachineName();
   
    /**
     * Gets the pool name.
     *
     * @return the pool name
     */
    public String getPoolName();
   
    /**
     * Gets the metric name.
     *
     * @return the metric name
     */
    public String getMetricName();
    
    /**
     * Gets the role type.
     *
     * @return the role type
     */
    public String getRoleType();
    
    /**
     * As rest url.
     *
     * @return the string
     */
    public String asRestUrl();
}
