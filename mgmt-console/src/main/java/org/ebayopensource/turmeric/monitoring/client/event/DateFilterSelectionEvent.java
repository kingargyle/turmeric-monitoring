/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class DateFilterSelectionEvent.
 */
public class DateFilterSelectionEvent extends GwtEvent<DateFilterSelectionHandler> {
    
    /** The date1. */
    long date1;
    
    /** The date2. */
    long date2;
    
    /** The duration. */
    int duration;

    /** The TYPE. */
    public static Type<DateFilterSelectionHandler> TYPE = new Type<DateFilterSelectionHandler>();
    
    /**
     * Instantiates a new date filter selection event.
     *
     * @param date1 the date1
     * @param date2 the date2
     * @param duration the duration
     */
    public DateFilterSelectionEvent (long date1, long date2, int duration) {
        this.date1 = date1;
        this.date2 = date2;
        this.duration = duration;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
     */
    @Override
    protected void dispatch(DateFilterSelectionHandler handler) {
        handler.onSelection(this);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DateFilterSelectionHandler> getAssociatedType() {
      return TYPE;
    }

    /**
     * Gets the date1.
     *
     * @return the date1
     */
    public long getDate1() {
        return date1;
    }
    
    /**
     * Gets the date2.
     *
     * @return the date2
     */
    public long getDate2() {
        return date2;
    }
    
    /**
     * Gets the duration.
     *
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

}
