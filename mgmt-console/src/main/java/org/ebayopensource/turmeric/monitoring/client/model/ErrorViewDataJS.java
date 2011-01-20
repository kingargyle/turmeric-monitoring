/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * ErrorViewDataJS
 *
 */
public class ErrorViewDataJS extends JavaScriptObject implements ErrorViewData{
    
    protected ErrorViewDataJS () {
    }
    

    public final Double getRatioDiff () {
        try {
            String s = getRatioDiffAsString();
            return new Double(s);
        } catch (JavaScriptException e) {
            throw new NumberFormatException ();
        }
    }
    public final Double getErrorDiff () {
        try {
            String s = getErrorDiffAsString();
            return new Double(s);
        } catch (JavaScriptException e) {
            throw new NumberFormatException ();
        }
    }

    public final Double getErrorCallRatio1 () {
        try {
            String s = getErrorCallRatio1AsString();
            return new Double(s);
        } catch (JavaScriptException e) {
            throw new NumberFormatException ();
        }
    }

    public final Double getErrorCallRatio2 (){
        try {
            String s = getErrorCallRatio2AsString();
            return new Double(s);
        } catch (JavaScriptException e) {
           throw new NumberFormatException();
        }
    }

    public final Long getErrorCount1() {
        try {
            String s = getErrorCount1AsString();
            return new Long(s);
        } catch (JavaScriptException e) {
            throw new NumberFormatException();
        }
    }

    public final Long getErrorCount2(){
        try {
            String s = getErrorCount2AsString();
            return new Long(s);
        } catch (JavaScriptException e) {
            throw new NumberFormatException();
        }
    }
    
    public final native String getConsumer()   /*-{
        return this.consumer;
    }-*/;
    
    public final native String getErrorId() /*-{
        if (this.error)
            return this.error.errorId;
        return null;
    }-*/;
    
    public final native String getErrorName() /*-{
        if (this.error)
            return this.error.errorName;
        return null;
    }-*/;
    
    public final native String getRatioDiffAsString ()   /*-{
        return this.ratioDiff;
    }-*/;
    
    public final native String getErrorDiffAsString ()   /*-{
        return this.errorDiff;
    }-*/;
    
    public final native String getErrorCallRatio1AsString () /*-{
        return this.errorCallRatio1;
    }-*/; 
    
    public final native String getErrorCallRatio2AsString () /*-{
        return this.errorCallRatio2;
    }-*/;
    
    public final native String getErrorCount1AsString() /*-{
        return this.errorCount1; 
    }-*/;    
    
    public final native String getErrorCount2AsString() /*-{
        return this.errorCount2; 
    }-*/;
    
    
}
