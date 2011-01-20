/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

public class MetricGroupDataImpl implements MetricGroupData {
    protected String diff;
    protected String count1;
    protected String count2;
    protected CriteriaInfoImpl criteriaInfo;
    
    public String getDiff() {
        return diff;
    }
    public void setDiff(String diff) {
        this.diff = diff;
    }
    public String getCount1() {
        return count1;
    }
    public void setCount1(String count1) {
        this.count1 = count1;
    }
    public String getCount2() {
        return count2;
    }
    public void setCount2(String count2) {
        this.count2 = count2;
    }
    public CriteriaInfo getCriteriaInfo() {
        return criteriaInfo;
    }
    public void setCriteriaInfo(CriteriaInfoImpl criteriaInfo) {
        this.criteriaInfo = criteriaInfo;
    }
}