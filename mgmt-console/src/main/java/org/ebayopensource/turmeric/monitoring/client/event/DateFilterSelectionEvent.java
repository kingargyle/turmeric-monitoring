/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.event;

import java.util.Date;

import com.google.gwt.event.shared.GwtEvent;

public class DateFilterSelectionEvent extends GwtEvent<DateFilterSelectionHandler> {
    String consumer;
    long date1;
    long date2;
    int duration;

    public static Type<DateFilterSelectionHandler> TYPE = new Type<DateFilterSelectionHandler>();
    
    public DateFilterSelectionEvent (long date1, long date2, int duration) {
        this.date1 = date1;
        this.date2 = date2;
        this.duration = duration;
    }

    @Override
    protected void dispatch(DateFilterSelectionHandler handler) {
        handler.onSelection(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DateFilterSelectionHandler> getAssociatedType() {
      return TYPE;
    }

    public long getDate1() {
        return date1;
    }
    public long getDate2() {
        return date2;
    }
    public int getDuration() {
        return duration;
    }

}
