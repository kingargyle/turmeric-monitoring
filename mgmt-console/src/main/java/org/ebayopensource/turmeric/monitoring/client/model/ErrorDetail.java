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
 * ErrorDetail.
 */
public interface ErrorDetail {
    
    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId();

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName();

    /**
     * Gets the domain.
     *
     * @return the domain
     */
    public String getDomain();

    /**
     * Gets the sub domain.
     *
     * @return the sub domain
     */
    public String getSubDomain();

    /**
     * Gets the severity.
     *
     * @return the severity
     */
    public String getSeverity();

    /**
     * Gets the category.
     *
     * @return the category
     */
    public String getCategory();
}
