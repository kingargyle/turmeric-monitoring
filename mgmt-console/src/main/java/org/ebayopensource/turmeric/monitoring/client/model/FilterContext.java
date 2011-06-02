/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The Class FilterContext.
 */
public class FilterContext {
    
    /** The date1. */
    long date1;
    
    /** The date2. */
    long date2;
    
    /** The duration hrs. */
    int durationHrs;
    
    /** The metric names. */
    List<String> metricNames;
    
    /**
     * From history token.
     *
     * @param token the token
     * @return the filter context
     */
    public static FilterContext fromHistoryToken (HistoryToken token) {
        FilterContext f = new FilterContext();
        String tmp = HistoryToken.getValue(token, HistoryToken.SELECTED_DATE1_TOKEN);
        if (tmp != null)
            f.setDate1(Long.parseLong(tmp));
        tmp = HistoryToken.getValue(token, HistoryToken.SELECTED_DATE2_TOKEN);
        if (tmp != null)
            f.setDate2(Long.parseLong(tmp));
        tmp = HistoryToken.getValue(token, HistoryToken.SELECTED_DURATION_TOKEN);
        if (tmp != null)
            f.setDurationHrs(Integer.valueOf(tmp).intValue());

        tmp = HistoryToken.getValue(token, HistoryToken.SELECTED_METRICS_TOKEN);
        if (tmp != null) {
            String[] metrics = tmp.split(",");
            f.setMetricNames(Arrays.asList(metrics));
        }
        return f;
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
     * Gets the duration hrs.
     *
     * @return the duration hrs
     */
    public int getDurationHrs() {
        return durationHrs;
    }
    
    /**
     * Gets the metric names.
     *
     * @return the metric names
     */
    public List<String> getMetricNames() {
        return metricNames;
    }
    
    /**
     * Sets the date1.
     *
     * @param date1 the new date1
     */
    public void setDate1(long date1) {
        this.date1 = date1;
    }
    
    /**
     * Sets the date2.
     *
     * @param date2 the new date2
     */
    public void setDate2(long date2) {
        this.date2 = date2;
    }
    
    /**
     * Sets the duration hrs.
     *
     * @param durationHrs the new duration hrs
     */
    public void setDurationHrs(int durationHrs) {
        this.durationHrs = durationHrs;
    }
    
    /**
     * Sets the metric names.
     *
     * @param metricNames the new metric names
     */
    public void setMetricNames(List<String> metricNames) {
        this.metricNames = metricNames;
    }
    
    /**
     * To history token.
     *
     * @param presenterId the servicePresenter id
     * @param values the values
     * @return the history token
     */
    public HistoryToken toHistoryToken (String presenterId, Map<String,String> values) {
        HistoryToken token = HistoryToken.newHistoryToken(presenterId, values);
        token.addValue(HistoryToken.SELECTED_DATE1_TOKEN, String.valueOf(date1));
        token.addValue(HistoryToken.SELECTED_DATE2_TOKEN, String.valueOf(date2));
        token.addValue(HistoryToken.SELECTED_DURATION_TOKEN, String.valueOf(durationHrs));
        token.addValue(HistoryToken.SELECTED_METRICS_TOKEN, metricNames);
        return token;
    }
}
