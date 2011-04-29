/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.ArrayList;
import java.util.List;

/**
 * TimeSlotData.
 */

public class TimeSlotData {
    
    /** The rest url. */
    String restUrl;
    
    /** The return data. */
    List<TimeSlotValue> returnData;
    
    
    /**
     * Gets the rest url.
     *
     * @return the rest url
     */
    public String getRestUrl() {
        return restUrl;
    }

    /**
     * Sets the rest url.
     *
     * @param restUrl the new rest url
     */
    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }
    
    /**
     * Gets the return data.
     *
     * @return the return data
     */
    public List<TimeSlotValue> getReturnData() {
        return returnData;
    }

    /**
     * Sets the return data.
     *
     * @param tsvs the new return data
     */
    public void setReturnData(List<TimeSlotValue> tsvs) {
        this.returnData = new ArrayList<TimeSlotValue>(tsvs);
    }
}
