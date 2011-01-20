/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.event.dom.client.HasClickHandlers;


/**
 * Filterable
 * 
 * Interface for selection of filter criteria that is common to
 * many tabs.
 *
 */
public interface Filterable {
    public HasClickHandlers getApplyButton();    
    public HasClickHandlers getCancelButton();
    public int getHour1 ();
    public int getHour2 ();
    public int getDuration ();
    public HasValue<Date> getDate1();
    public HasValue<Date> getDate2();
    public List<String> getSelectedMetricNames();
    public void setDuration (int duration);
    public void setDurations (int[] durations);     
    public void setDate1(Date d);
    public void setDate2(Date d);
    public void setHour1 (int hour);
    public void setHour2 (int hour);
    public void setHours1 (int[] hours);
    public void setHours2 (int[] hours);
    public void setMetricNames (List<String> names);
    public void setSelectedMetricNames (List<String> names);
    
    
    public interface ErrorFilterable extends Filterable {
        public void setCategoryViewNames (List<String> names);
        public void setSelectedCategoryViewNames (List<String> names);
        public List<String> getSelectedCategoryViewNames ();
        public void setSeverityViewNames (List<String> names);
        public void setSelectedSeverityViewNames(List<String> names);
        public List<String> getSelectedSeverityViewNames();
    }
}
