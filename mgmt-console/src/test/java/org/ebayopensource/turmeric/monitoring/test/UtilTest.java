/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.Util;
import org.ebayopensource.turmeric.monitoring.client.model.ServiceMetric;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test class for the methods in the {@link Util} class.
 */
public class UtilTest {
    
    
    /**
     * Test method for the {@link Util#convertToEnum} method.
     * @throws Exception if the method {@link Util#convertToEnum} fails
     */
    @Test
    public void testConvertToEnum () throws Exception {
        List<ServiceMetric> list = Util.convertToEnum("TopVolume, ConsumerErrors", ServiceMetric.class);
        assertNotNull(list);
        assertEquals(2, list.size());
        assertTrue(list.contains(ServiceMetric.ConsumerErrors));
        assertTrue(list.contains(ServiceMetric.TopVolume));
    }
    
    /**
     * Test method for the {@link Util#convertToEnumFromCamelCase} method.
     * @throws Exception if the method {@link Util#convertToEnumFromCamelCase} fails
     */
    @Test
    public void testConvertToEnumFromCamelCase () throws Exception {
        List<ServiceMetric> list = Util.convertToEnumFromCamelCase(Arrays.asList(new String[]{"topVolume", "consumerErrors"}), ServiceMetric.class);
        assertNotNull(list);
        assertEquals(2, list.size());
        assertTrue(list.contains(ServiceMetric.ConsumerErrors));
        assertTrue(list.contains(ServiceMetric.TopVolume));
    }
    
    /**
     * Test method for the {@link Util#isToday} method.
     * @throws Exception if the method {@link Util#isToday} fails
     */
    @Test
    public void testIsToday () throws Exception {
        Date now = new Date();
        assertTrue (Util.isToday(now));
        
        long time = now.getTime() - (28*60*60*1000);
        assertFalse (Util.isToday(new Date(time)));
    }

}
