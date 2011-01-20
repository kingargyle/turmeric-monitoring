/*******************************************************************************
 * Copyright (c) 2006, 2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.unitils.core;

/**
 * Exception type, used for all unrecoverable exceptions that occur in unitils.
 */
public class UnitilsException extends RuntimeException {

    public UnitilsException() {
    }

    public UnitilsException(String message) {
        super(message);
    }

    public UnitilsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnitilsException(Throwable cause) {
        super(cause);
    }

}
