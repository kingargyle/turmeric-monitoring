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
 * ErrorDetailJS
 *
 */
public class ErrorDetailJS extends JavaScriptObject implements ErrorDetail {
    
    protected ErrorDetailJS () {
    }
    
    public final native String getId() /*-{
        return this.id;
    }-*/;
    
    public final native String getName() /*-{
        return this.name;
    }-*/;
    
    public final native String getCategory() /*-{
        return this.category;
    }-*/;

    public final native String getSeverity() /*-{
        return this.severity;
    }-*/;

    public final native String getDomain() /*-{
        return this.domain;
    }-*/;
    
    public final native String getSubDomain() /*-{
        return this.subDomain;
    }-*/;
  
}
