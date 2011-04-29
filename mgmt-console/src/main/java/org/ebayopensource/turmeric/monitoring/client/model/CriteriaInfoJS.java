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
 * The Class CriteriaInfoJS.
 */
public class CriteriaInfoJS extends JavaScriptObject {
    
    /**
     * Instantiates a new criteria info js.
     */
    protected CriteriaInfoJS() {
    }

    /**
     * Gets the service consumer type.
     *
     * @return the service consumer type
     */
    public final native String getServiceConsumerType () /*-{
        return this.serviceConsumerType;
    }-*/;

    /**
     * Gets the metric name.
     *
     * @return the metric name
     */
    public final native String getMetricName () /*-{
        return this.metricName;
    }-*/;

    /**
     * Gets the service name.
     *
     * @return the service name
     */
    public final native String getServiceName () /*-{
        return this.serviceName;
    }-*/;

    /**
     * Gets the operation name.
     *
     * @return the operation name
     */
    public final native String getOperationName () /*-{
        return this.operationName;
    }-*/;

    /**
     * Gets the consumer name.
     *
     * @return the consumer name
     */
    public final native String getConsumerName () /*-{
        return this.consumerName;
    }-*/;

    /**
     * Gets the pool name.
     *
     * @return the pool name
     */
    public final native String getPoolName () /*-{
        return this.poolName;
    }-*/;

    /**
     * Gets the machine name.
     *
     * @return the machine name
     */
    public final native String getMachineName () /*-{
        return this.machineName;
     }-*/;

    /**
     * Gets the role type.
     *
     * @return the role type
     */
    public final native String getRoleType () /*-{
        return this.roleType;
     }-*/;
}
