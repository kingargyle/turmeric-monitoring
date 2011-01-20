/*******************************************************************************
 * Copyright (c) 2006, 2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.unitils.reflectionassert.comparator.impl;

import org.unitils.reflectionassert.ReflectionComparator;
import org.unitils.reflectionassert.comparator.Comparator;
import org.unitils.reflectionassert.difference.Difference;

/**
 * A comparator that filters out java-defaults.
 * If the left object is null, false or 0, both objects are considered equal.
 * This implements the IGNORE_DEFAULTS comparison mode.
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public class IgnoreDefaultsComparator implements Comparator {


    /**
     * Returns true if the left object is a java default
     *
     * @param left  The left object
     * @param right The right object
     * @return True if left is null, false or 0
     */
    public boolean canCompare(Object left, Object right) {
        // object types
        if (left == null) {
            return true;
        }
        // primitive boolean types
        if (left instanceof Boolean && !(Boolean) left) {
            return true;
        }
        // primitive character types
        if (left instanceof Character && (Character) left == 0) {
            return true;
        }
        // primitive int/long/double/float types
        if (left instanceof Number && ((Number) left).doubleValue() == 0) {
            return true;
        }
        return false;
    }


    /**
     * Always returns null: both objects are equal.
     *
     * @param left                 The left object
     * @param right                The right object
     * @param onlyFirstDifference  True if only the first difference should be returned
     * @param reflectionComparator The root comparator for inner comparisons, not null
     * @return null
     */
    public Difference compare(Object left, Object right, boolean onlyFirstDifference, ReflectionComparator reflectionComparator) {
        // ignore
        return null;
    }
}
