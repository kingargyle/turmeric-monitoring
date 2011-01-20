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

public interface ConsoleMessageConstants extends Messages
{
    @DefaultMessage("A server error occurred: {0}")
    public String serverError (String err);
    
    @DefaultMessage("Select a server.")
    public String selectServer ();
    
    @DefaultMessage("Bad or missing response data.")
    public String badOrMissingResponseData();
    
    @DefaultMessage("Bad request data.")
    public String badRequestData();
    
    @DefaultMessage("Invalid credentials")
    public String loginFailed();
    
    @DefaultMessage("You have been logged out successfully")
    public String logoutSuccessful();
    
}
