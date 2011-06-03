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
 * The Enum MonitoringLevel.
 */
public enum MonitoringLevel {
    
    /** The NORMAL. */
    NORMAL("normal"), 
 /** The FINE. */
 FINE("fine"), 
 /** The FINEST. */
 FINEST("finest");

    private final String value;

    private MonitoringLevel(String value) {
        this.value = value;
    }

    /**
     * From.
     *
     * @param value the value
     * @return the monitoring level
     */
    public static MonitoringLevel from(String value) {
        for (MonitoringLevel level : values()) {
            if (level.value.equals(value))
                return level;
        }
        throw new IllegalArgumentException("Unknown MonitoringLevel " + value);
    }
}
