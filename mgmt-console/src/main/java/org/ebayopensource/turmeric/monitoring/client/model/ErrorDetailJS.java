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
 * ErrorDetailJS.
 */
public class ErrorDetailJS extends JavaScriptObject implements ErrorDetail {
    
    /**
     * Instantiates a new error detail js.
     */
    protected ErrorDetailJS () {
    }
    
    /**
     * Gets the id.
     *
     * @return the id
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorDetail#getId()
     */
    public final native String getId() /*-{
        return this.id;
    }-*/;
    
    /**
     * Gets the name.
     *
     * @return the name
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorDetail#getName()
     */
    public final native String getName() /*-{
        return this.name;
    }-*/;
    
    /**
     * Gets the category.
     *
     * @return the category
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorDetail#getCategory()
     */
    public final native String getCategory() /*-{
        return this.category;
    }-*/;

    /**
     * Gets the severity.
     *
     * @return the severity
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorDetail#getSeverity()
     */
    public final native String getSeverity() /*-{
        return this.severity;
    }-*/;

    /**
     * Gets the domain.
     *
     * @return the domain
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorDetail#getDomain()
     */
    public final native String getDomain() /*-{
        return this.domain;
    }-*/;
    
    /**
     * Gets the sub domain.
     *
     * @return the sub domain
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorDetail#getSubDomain()
     */
    public final native String getSubDomain() /*-{
        return this.subDomain;
    }-*/;
  
}
