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
 * TimeSlotData
 *
 */

public class TimeSlotData {
    String restUrl;
    List<TimeSlotValue> returnData;
    
    
    public String getRestUrl() {
        return restUrl;
    }

    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }
    
    public List<TimeSlotValue> getReturnData() {
        return returnData;
    }

    public void setReturnData(List<TimeSlotValue> tsvs) {
        this.returnData = new ArrayList<TimeSlotValue>(tsvs);
    }
}
