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

public class FilterContext {
    long date1;
    long date2;
    int durationHrs;
    List<String> metricNames;
    
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

    public long getDate1() {
        return date1;
    }
    public long getDate2() {
        return date2;
    }
    public int getDurationHrs() {
        return durationHrs;
    }
    public List<String> getMetricNames() {
        return metricNames;
    }
    
    public void setDate1(long date1) {
        this.date1 = date1;
    }
    public void setDate2(long date2) {
        this.date2 = date2;
    }
    public void setDurationHrs(int durationHrs) {
        this.durationHrs = durationHrs;
    }
    public void setMetricNames(List<String> metricNames) {
        this.metricNames = metricNames;
    }
    
    public HistoryToken toHistoryToken (String presenterId, Map<String,String> values) {
        HistoryToken token = HistoryToken.newHistoryToken(presenterId, values);
        token.addValue(HistoryToken.SELECTED_DATE1_TOKEN, String.valueOf(date1));
        token.addValue(HistoryToken.SELECTED_DATE2_TOKEN, String.valueOf(date2));
        token.addValue(HistoryToken.SELECTED_DURATION_TOKEN, String.valueOf(durationHrs));
        token.addValue(HistoryToken.SELECTED_METRICS_TOKEN, metricNames);
        return token;
    }
}
