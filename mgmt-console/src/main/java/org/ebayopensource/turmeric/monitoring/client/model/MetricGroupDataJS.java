/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import com.google.gwt.core.client.JavaScriptObject;


/**
 * The Class MetricGroupDataJS.
 */
public class MetricGroupDataJS extends JavaScriptObject implements MetricGroupData {
   
    /**
     * Instantiates a new metric group data js.
     */
    protected MetricGroupDataJS () {
    }
     
    /**
     * Gets the diff.
     *
     * @return the diff
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricGroupData#getDiff()
     */
    public final native String getDiff ()   /*-{
        return this.diff;
    }-*/;

    /**
     * Gets the count1.
     *
     * @return the count1
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricGroupData#getCount1()
     */
    public final native String getCount1 () /*-{
        return this.count1;
    }-*/; 
    
    /**
     * Gets the count2.
     *
     * @return the count2
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricGroupData#getCount2()
     */
    public final native String getCount2 () /*-{
        return this.count2;
    }-*/;
    
    /**
     * Gets the criteria info js.
     *
     * @return the criteria info js
     */
    public final native CriteriaInfoJS getCriteriaInfoJS() /*-{
        return this.criteriaInfo 
    }-*/;
    
 
    /**
     * Wierd workaround for GWT hosted mode problem. If
     * CriteriaInfoJS implements CriteriaInfo then an
     * java.lang.AbstractMethodError is thrown when this
     * method is called in hosted mode. Oddly, this works
     * just find in production mode. As hosted mode is
     * too productive to lose, the work around is to make only
     * a java object that implements CriteriaInfo interface
     * and then copy from one to the other in this method.
     *
     * @return the criteria info
     */
    public final CriteriaInfo getCriteriaInfo () {
        CriteriaInfoJS js = getCriteriaInfoJS();
        CriteriaInfoImpl impl = new CriteriaInfoImpl();
        impl.consumerName = js.getConsumerName();
        impl.machineName = js.getMachineName();
        impl.metricName = js.getMetricName();
        impl.operationName = js.getOperationName();
        impl.poolName = js.getPoolName();
        impl.roleType = js.getRoleType();
        impl.serviceConsumerType = js.getServiceConsumerType();
        impl.serviceName = js.getServiceName();
        return impl;
    }
}
