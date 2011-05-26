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
 * ErrorViewDataJS.
 */
public class ErrorViewDataJS extends JavaScriptObject implements ErrorViewData{
    
    /**
     * Instantiates a new error view data js.
     */
    protected ErrorViewDataJS () {
    }
    

    /**
     * Gets the ratio diff.
     *
     * @return the ratio diff
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorViewData#getRatioDiff()
     */
    public final Double getRatioDiff () {
        try {
            String s = getRatioDiffAsString();
            return new Double(s);
        } catch (JavaScriptException e) {
            throw new NumberFormatException ();
        }
    }
    
    /**
     * Gets the error diff.
     *
     * @return the error diff
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorViewData#getErrorDiff()
     */
    public final Double getErrorDiff () {
        try {
            String s = getErrorDiffAsString();
            return new Double(s);
        } catch (JavaScriptException e) {
            throw new NumberFormatException ();
        }
    }

    /**
     * Gets the error call ratio1.
     *
     * @return the error call ratio1
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorViewData#getErrorCallRatio1()
     */
    public final Double getErrorCallRatio1 () {
        try {
            String s = getErrorCallRatio1AsString();
            return new Double(s);
        } catch (JavaScriptException e) {
            throw new NumberFormatException ();
        }
    }

    /**
     * Gets the error call ratio2.
     *
     * @return the error call ratio2
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorViewData#getErrorCallRatio2()
     */
    public final Double getErrorCallRatio2 (){
        try {
            String s = getErrorCallRatio2AsString();
            return new Double(s);
        } catch (JavaScriptException e) {
           throw new NumberFormatException();
        }
    }

    /**
     * Gets the error count1.
     *
     * @return the error count1
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorViewData#getErrorCount1()
     */
    public final Long getErrorCount1() {
        try {
            String s = getErrorCount1AsString();
            return new Long(s);
        } catch (JavaScriptException e) {
            throw new NumberFormatException();
        }
    }

    /**
     * Gets the error count2.
     *
     * @return the error count2
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorViewData#getErrorCount2()
     */
    public final Long getErrorCount2(){
        try {
            String s = getErrorCount2AsString();
            return new Long(s);
        } catch (JavaScriptException e) {
            throw new NumberFormatException();
        }
    }
    
    /**
     * Gets the consumer.
     *
     * @return the consumer
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorViewData#getConsumer()
     */
    public final native String getConsumer()   /*-{
        return this.consumer;
    }-*/;
    
    /**
     * Gets the error id.
     *
     * @return the error id
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorViewData#getErrorId()
     */
    public final native String getErrorId() /*-{
        if (this.error)
            return this.error.errorId;
        return null;
    }-*/;
    
    /**
     * Gets the error name.
     *
     * @return the error name
     * @see org.ebayopensource.turmeric.monitoring.client.model.ErrorViewData#getErrorName()
     */
    public final native String getErrorName() /*-{
        if (this.error)
            return this.error.errorName;
        return null;
    }-*/;
    
    /**
     * Gets the ratio diff as string.
     *
     * @return the ratio diff as string
     */
    public final native String getRatioDiffAsString ()   /*-{
        return this.ratioDiff;
    }-*/;
    
    /**
     * Gets the error diff as string.
     *
     * @return the error diff as string
     */
    public final native String getErrorDiffAsString ()   /*-{
        return this.errorDiff;
    }-*/;
    
    /**
     * Gets the error call ratio1 as string.
     *
     * @return the error call ratio1 as string
     */
    public final native String getErrorCallRatio1AsString () /*-{
        return this.errorCallRatio1;
    }-*/; 
    
    /**
     * Gets the error call ratio2 as string.
     *
     * @return the error call ratio2 as string
     */
    public final native String getErrorCallRatio2AsString () /*-{
        return this.errorCallRatio2;
    }-*/;
    
    /**
     * Gets the error count1 as string.
     *
     * @return the error count1 as string
     */
    public final native String getErrorCount1AsString() /*-{
        return this.errorCount1; 
    }-*/;    
    
    /**
     * Gets the error count2 as string.
     *
     * @return the error count2 as string
     */
    public final native String getErrorCount2AsString() /*-{
        return this.errorCount2; 
    }-*/;
    
    
}
