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

public class CriteriaInfoJS extends JavaScriptObject {
    protected CriteriaInfoJS() {
    }

    public final native String getServiceConsumerType () /*-{
        return this.serviceConsumerType;
    }-*/;

    public final native String getMetricName () /*-{
        return this.metricName;
    }-*/;

    public final native String getServiceName () /*-{
        return this.serviceName;
    }-*/;

    public final native String getOperationName () /*-{
        return this.operationName;
    }-*/;

    public final native String getConsumerName () /*-{
        return this.consumerName;
    }-*/;

    public final native String getPoolName () /*-{
        return this.poolName;
    }-*/;

    public final native String getMachineName () /*-{
        return this.machineName;
     }-*/;

    public final native String getRoleType () /*-{
        return this.roleType;
     }-*/;
}
