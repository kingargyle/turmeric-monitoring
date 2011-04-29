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
    
    /**
     * Gets the apply button.
     *
     * @return the apply button
     */
    public HasClickHandlers getApplyButton();    
    
    /**
     * Gets the cancel button.
     *
     * @return the cancel button
     */
    public HasClickHandlers getCancelButton();
    
    /**
     * Gets the hour1.
     *
     * @return the hour1
     */
    public int getHour1 ();
    
    /**
     * Gets the hour2.
     *
     * @return the hour2
     */
    public int getHour2 ();
    
    /**
     * Gets the duration.
     *
     * @return the duration
     */
    public int getDuration ();
    
    /**
     * Gets the date1.
     *
     * @return the date1
     */
    public HasValue<Date> getDate1();
    
    /**
     * Gets the date2.
     *
     * @return the date2
     */
    public HasValue<Date> getDate2();
    
    /**
     * Gets the selected metric names.
     *
     * @return the selected metric names
     */
    public List<String> getSelectedMetricNames();
    
    /**
     * Sets the duration.
     *
     * @param duration the new duration
     */
    public void setDuration (int duration);
    
    /**
     * Sets the durations.
     *
     * @param durations the new durations
     */
    public void setDurations (int[] durations);     
    
    /**
     * Sets the date1.
     *
     * @param d the new date1
     */
    public void setDate1(Date d);
    
    /**
     * Sets the date2.
     *
     * @param d the new date2
     */
    public void setDate2(Date d);
    
    /**
     * Sets the hour1.
     *
     * @param hour the new hour1
     */
    public void setHour1 (int hour);
    
    /**
     * Sets the hour2.
     *
     * @param hour the new hour2
     */
    public void setHour2 (int hour);
    
    /**
     * Sets the hours1.
     *
     * @param hours the new hours1
     */
    public void setHours1 (int[] hours);
    
    /**
     * Sets the hours2.
     *
     * @param hours the new hours2
     */
    public void setHours2 (int[] hours);
    
    /**
     * Sets the metric names.
     *
     * @param names the new metric names
     */
    public void setMetricNames (List<String> names);
    
    /**
     * Sets the selected metric names.
     *
     * @param names the new selected metric names
     */
    public void setSelectedMetricNames (List<String> names);
    
    
    /**
     * The Interface ErrorFilterable.
     */
    public interface ErrorFilterable extends Filterable {
        
        /**
         * Sets the category view names.
         *
         * @param names the new category view names
         */
        public void setCategoryViewNames (List<String> names);
        
        /**
         * Sets the selected category view names.
         *
         * @param names the new selected category view names
         */
        public void setSelectedCategoryViewNames (List<String> names);
        
        /**
         * Gets the selected category view names.
         *
         * @return the selected category view names
         */
        public List<String> getSelectedCategoryViewNames ();
        
        /**
         * Sets the severity view names.
         *
         * @param names the new severity view names
         */
        public void setSeverityViewNames (List<String> names);
        
        /**
         * Sets the selected severity view names.
         *
         * @param names the new selected severity view names
         */
        public void setSelectedSeverityViewNames(List<String> names);
        
        /**
         * Gets the selected severity view names.
         *
         * @return the selected severity view names
         */
        public List<String> getSelectedSeverityViewNames();
    }
}
