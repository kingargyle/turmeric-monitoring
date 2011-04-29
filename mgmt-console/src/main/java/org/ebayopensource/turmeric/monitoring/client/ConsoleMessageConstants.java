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
    @DefaultMessage("A server error occurred: {0}")
    public String serverError (String err);
    
    /**
     * Select server.
     *
     * @return the string
     */
    @DefaultMessage("Select a server.")
    public String selectServer ();
    
    /**
     * Bad or missing response data.
     *
     * @return the string
     */
    @DefaultMessage("Bad or missing response data.")
    public String badOrMissingResponseData();
    
    /**
     * Bad request data.
     *
     * @return the string
     */
    @DefaultMessage("Bad request data.")
    public String badRequestData();
    
    /**
     * Login failed.
     *
     * @return the string
     */
    @DefaultMessage("Invalid credentials")
    public String loginFailed();
    
    /**
     * Logout successful.
     *
     * @return the string
     */
    @DefaultMessage("You have been logged out successfully")
    public String logoutSuccessful();
    
}
