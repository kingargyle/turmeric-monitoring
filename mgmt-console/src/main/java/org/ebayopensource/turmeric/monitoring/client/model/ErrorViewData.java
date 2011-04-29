/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

/**
 * ErrorViewData.
 */
public interface ErrorViewData {
    
    /**
     * Gets the consumer.
     *
     * @return the consumer
     */
    public String getConsumer();
    
    /**
     * Gets the error id.
     *
     * @return the error id
     */
    public String getErrorId();
    
    /**
     * Gets the error name.
     *
     * @return the error name
     */
    public String getErrorName();

    /**
     * Gets the ratio diff.
     *
     * @return the ratio diff
     */
    public Double getRatioDiff();

    /**
     * Gets the error diff.
     *
     * @return the error diff
     */
    public Double getErrorDiff();

    /**
     * Gets the error call ratio2.
     *
     * @return the error call ratio2
     */
    public Double getErrorCallRatio2();

    /**
     * Gets the error call ratio1.
     *
     * @return the error call ratio1
     */
    public Double getErrorCallRatio1();

    /**
     * Gets the error count1.
     *
     * @return the error count1
     */
    public Long getErrorCount1();

    /**
     * Gets the error count2.
     *
     * @return the error count2
     */
    public Long getErrorCount2();

}
