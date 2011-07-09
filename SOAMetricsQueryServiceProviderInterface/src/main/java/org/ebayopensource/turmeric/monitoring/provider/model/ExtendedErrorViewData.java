/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring.provider.model;

import org.ebayopensource.turmeric.monitoring.v1.services.ErrorViewData;

/**
 * Model class to pass error call numbers from the provider to the SQMSImpl code.
 */
public class ExtendedErrorViewData extends ErrorViewData {

    /** The error call1. */
    protected double errorCall1;

    /** The error call2. */
    protected double errorCall2;

    /**
     * Gets the error call1.
     * 
     * @return the error call1
     */
    public double getErrorCall1() {
        return errorCall1;
    }

    /**
     * Sets the error call1.
     * 
     * @param errorCall1
     *            the new error call1
     */
    public void setErrorCall1(double errorCall1) {
        this.errorCall1 = errorCall1;
    }

    /**
     * Gets the error call2.
     * 
     * @return the error call2
     */
    public double getErrorCall2() {
        return errorCall2;
    }

    /**
     * Sets the error call2.
     * 
     * @param errorCall2
     *            the new error call2
     */
    public void setErrorCall2(double errorCall2) {
        this.errorCall2 = errorCall2;
    }
}
