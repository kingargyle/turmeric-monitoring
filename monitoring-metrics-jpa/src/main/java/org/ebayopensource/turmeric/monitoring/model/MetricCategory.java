/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.model;

/**
 * The Enum MetricCategory.
 */
public enum MetricCategory {
    
    /** The TIMING. */
    TIMING("Timing"), 
 /** The ERROR. */
 ERROR("Error"), 
 /** The OTHER. */
 OTHER("Other");

    private final String value;

    private MetricCategory(String value) {
        this.value = value;
    }

    /**
     * From.
     *
     * @param value the value
     * @return the metric category
     */
    public static MetricCategory from(String value) {
        for (MetricCategory category : values()) {
            if (category.value.equals(value))
                return category;
        }
        throw new IllegalArgumentException("Unknown MetricCategory " + value);
    }
}
