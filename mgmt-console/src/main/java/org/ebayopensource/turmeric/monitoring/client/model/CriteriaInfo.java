/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

public interface CriteriaInfo {

    public String getServiceConsumerType();
    
    public String getServiceName();
    
    public String getOperationName();
  
    public String getConsumerName();
   
    public String getMachineName();
   
    public String getPoolName();
   
    public String getMetricName();
    
    public String getRoleType();
    
    public String asRestUrl();
}
