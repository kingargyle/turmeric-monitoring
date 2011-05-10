/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring.client;

import com.google.gwt.i18n.client.Messages;

/**
 * The Interface ConsoleMessageConstants.
 */
public interface ConsoleMessageConstants extends Messages
{
    
    /**
     * Server error.
     *
     * @param err the err
     * @return the string
     */
    public String serverError (String err);
    
    /**
     * Select server.
     *
     * @return the string
     */
    public String selectServer ();
    
    /**
     * Bad or missing response data.
     *
     * @return the string
     */
    public String badOrMissingResponseData();
    
    /**
     * Bad request data.
     *
     * @return the string
     */
    public String badRequestData();
    
    /**
     * Login failed.
     *
     * @return the string
     */
    public String loginFailed();
    
    /**
     * Logout successful.
     *
     * @return the string
     */
    public String logoutSuccessful();
    
    
    /**
     * Graph title.
     *
     * @param metricName the metric name
     * @param serviceName the service name
     * @param hourSpan the hour span
     * @return the string
     */
    public String graphTitle(String metricName, String serviceName, int hourSpan);
    
}
