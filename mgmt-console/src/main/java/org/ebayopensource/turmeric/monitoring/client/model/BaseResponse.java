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
 * BaseResponse
 *
 */
public abstract class BaseResponse extends JavaScriptObject {
    protected BaseResponse() {
    }
    
    
    public final native boolean isErrored (String response) /*-{   
        var ack = this[response].ack;
        if (!ack)
            return false;
        if (ack.toLowerCase() == 'success')
            return false;
        return true;
    }-*/;
    
    public final native String getErrorMessage (String response) /*-{
         if (!this[response])
          return null;
          
          var em = this[response].errorMessage;
    }-*/;
    
    public final native String getVersion (String response) /*-{
        return null;
 
    }-*/;

}
