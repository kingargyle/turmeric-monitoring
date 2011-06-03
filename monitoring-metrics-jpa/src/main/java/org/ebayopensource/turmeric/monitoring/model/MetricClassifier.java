/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.model;

import javax.persistence.Entity;

import org.ebayopensource.turmeric.utils.jpa.model.Persistent;

/**
 * The Class MetricClassifier.
 */
@Entity
public class MetricClassifier extends Persistent {
    private String consumerName;
    private String sourceDataCenter;
    private String targetDataCenter;

    /**
     * Instantiates a new metric classifier.
     */
    protected MetricClassifier() {
    }

    /**
     * Instantiates a new metric classifier.
     *
     * @param consumerName the consumer name
     * @param sourceDataCenter the source data center
     * @param targetDataCenter the target data center
     */
    public MetricClassifier(String consumerName, String sourceDataCenter, String targetDataCenter) {
        this.consumerName = consumerName;
        this.sourceDataCenter = sourceDataCenter;
        this.targetDataCenter = targetDataCenter;
    }

    /**
     * Gets the consumer name.
     *
     * @return the consumer name
     */
    public String getConsumerName() {
        return consumerName;
    }

    /**
     * Gets the source data center.
     *
     * @return the source data center
     */
    public String getSourceDataCenter() {
        return sourceDataCenter;
    }

    /**
     * Gets the target data center.
     *
     * @return the target data center
     */
    public String getTargetDataCenter() {
        return targetDataCenter;
    }
}
