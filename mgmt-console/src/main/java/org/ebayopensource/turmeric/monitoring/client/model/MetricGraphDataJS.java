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
 * MetricGraphDataJS.
 */
public class MetricGraphDataJS extends JavaScriptObject implements TimeSlotValue {
    
    /**
     * Instantiates a new metric graph data js.
     */
    protected MetricGraphDataJS () {
    }
    
    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue#getCriteria()
     */
    public final native String getCriteria() /*-{
        return this.criteria;
    }-*/;
    
    /**
     * Gets the time slot as string.
     *
     * @return the time slot as string
     */
    public final native String getTimeSlotAsString() /*-{
        return this.timeSlot;
    }-*/;
    
    /**
     * Gets the value as string.
     *
     * @return the value as string
     */
    public final native String getValueAsString() /*-{
        return this.count;
    }-*/;

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue#getValue()
     */
    public final Double getValue() {
        try {
            String s = getValueAsString();
            return new Double(s);
        } catch (JavaScriptException e) {
            throw new NumberFormatException();
        }
    }
    
    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue#getTimeSlot()
     */
    public final Long getTimeSlot() {
        try {
            String s = getTimeSlotAsString();
            return new Long(s);
        } catch (JavaScriptException e) {
            throw new NumberFormatException();
        }
    }
}
