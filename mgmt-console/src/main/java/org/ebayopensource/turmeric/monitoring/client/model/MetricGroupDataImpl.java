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
 * The Class MetricGroupDataImpl.
 */
public class MetricGroupDataImpl implements MetricGroupData {
    
    /** The diff. */
    protected String diff;
    
    /** The count1. */
    protected String count1;
    
    /** The count2. */
    protected String count2;
    
    /** The criteria info. */
    protected CriteriaInfoImpl criteriaInfo;
    
    /**
     * Gets the diff.
     *
     * @return the diff
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricGroupData#getDiff()
     */
    public String getDiff() {
        return diff;
    }
    
    /**
     * Sets the diff.
     *
     * @param diff the new diff
     */
    public void setDiff(String diff) {
        this.diff = diff;
    }
    
    /**
     * Gets the count1.
     *
     * @return the count1
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricGroupData#getCount1()
     */
    public String getCount1() {
        return count1;
    }
    
    /**
     * Sets the count1.
     *
     * @param count1 the new count1
     */
    public void setCount1(String count1) {
        this.count1 = count1;
    }
    
    /**
     * Gets the count2.
     *
     * @return the count2
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricGroupData#getCount2()
     */
    public String getCount2() {
        return count2;
    }
    
    /**
     * Sets the count2.
     *
     * @param count2 the new count2
     */
    public void setCount2(String count2) {
        this.count2 = count2;
    }
    
    /**
     * Gets the criteria info.
     *
     * @return the criteria info
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricGroupData#getCriteriaInfo()
     */
    public CriteriaInfo getCriteriaInfo() {
        return criteriaInfo;
    }
    
    /**
     * Sets the criteria info.
     *
     * @param criteriaInfo the new criteria info
     */
    public void setCriteriaInfo(CriteriaInfoImpl criteriaInfo) {
        this.criteriaInfo = criteriaInfo;
    }
}