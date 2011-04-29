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
 * BaseResponse.
 */
public abstract class BaseResponse extends JavaScriptObject {
    
    /**
     * Instantiates a new base response.
     */
    protected BaseResponse() {
    }
    
    
    /**
     * Checks if is errored.
     *
     * @param response the response
     * @return true, if is errored
     */
    public final native boolean isErrored (String response) /*-{   
        var ack = this[response].ack;
        if (!ack)
            return false;
        if (ack.toLowerCase() == 'success')
            return false;
        return true;
    }-*/;
    
    /**
     * Gets the error message.
     *
     * @param response the response
     * @return the error message
     */
    public final native String getErrorMessage (String response) /*-{
         if (!this[response])
          return null;
          
          var em = this[response].errorMessage;
    }-*/;
    
    /**
     * Gets the version.
     *
     * @param response the response
     * @return the version
     */
    public final native String getVersion (String response) /*-{
        return null;
 
    }-*/;

}
