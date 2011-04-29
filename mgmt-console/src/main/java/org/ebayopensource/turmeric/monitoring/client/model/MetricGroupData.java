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
 * The Interface MetricGroupData.
 */
public interface MetricGroupData {
  
    /**
     * Gets the diff.
     *
     * @return the diff
     */
    public String getDiff();
    
    /**
     * Gets the count1.
     *
     * @return the count1
     */
    public String getCount1();
    
    /**
     * Gets the count2.
     *
     * @return the count2
     */
    public String getCount2();
    
    /**
     * Gets the criteria info.
     *
     * @return the criteria info
     */
    public CriteriaInfo getCriteriaInfo();
    
}
