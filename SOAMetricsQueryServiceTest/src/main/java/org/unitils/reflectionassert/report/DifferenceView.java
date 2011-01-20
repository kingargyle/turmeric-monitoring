/*******************************************************************************
 * Copyright (c) 2006, 2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.unitils.reflectionassert.report;

import org.unitils.reflectionassert.difference.Difference;


/**
 * An interface for classes that can create a string representation of a Difference.
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public interface DifferenceView {


    /**
     * Creates a string representation of the given difference tree.
     *
     * @param difference The root difference, not null
     * @return The string representation, not null
     */
    String createView(Difference difference);

}
