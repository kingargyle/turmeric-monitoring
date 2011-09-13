/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * The Class KeyGeneratorUtilTest.
 * @author jamuguerza
 */
public class KeyGeneratorUtilTest {
	 /*
	  * key format is ip|serviceName|consumerName|operationName|Category/Severity|<true/false>
	  */
	Map<String, List<String>> filters;
	boolean serverSide = false;
	String param1 = "APPLICATION"; //any category
	
	@Before
    public void setup() throws Exception {
		filters = filterArrayGenerator();
    }
    
	@Test
	public void testNotNull() {
		List<String> keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertNotNull(keys);
	}

	@Test
	public void testNotEmpty() {
		List<String> keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertFalse(keys.isEmpty());
	}
	
	@Test
	public void testKeyValuesAll() {
		List<String> keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertEquals(1, keys.size());
		assertEquals("ALL|ALL|ALL|ALL|APPLICATION|false", keys.get(0));
	}
	
	@Test
	public void testKeyValuesServerSide() {
		serverSide = true;
		List<String> keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertEquals(1, keys.size());
		assertEquals("ALL|ALL|ALL|ALL|APPLICATION|true", keys.get(0));
	}
	
	@Test
	public void testKeyValuesParam1() {
		param1 = "SYSTEM";
		List<String> keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertEquals(1, keys.size());
		assertEquals("ALL|ALL|ALL|ALL|SYSTEM|false", keys.get(0));
	}
	
	@Test
	public void testKeyValuesServer() {
		filters.get("Server").add("Server1");
		List<String> keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertEquals("Server1|ALL|ALL|ALL|APPLICATION|false", keys.get(0));
	
		filters.get("Server").add("Server2");
		keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertEquals(2, keys.size());
		assertEquals("Server1|ALL|ALL|ALL|APPLICATION|false", keys.get(0));
		assertEquals("Server2|ALL|ALL|ALL|APPLICATION|false", keys.get(1));
	}
	
	@Test
	public void testKeyValuesService() {
		filters.get("Service").add("Service1");
		List<String> keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertEquals("ALL|Service1|ALL|ALL|APPLICATION|false", keys.get(0));
	
		filters.get("Service").add("Service2");
		keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertEquals(2, keys.size());
		assertEquals("ALL|Service1|ALL|ALL|APPLICATION|false", keys.get(0));
		assertEquals("ALL|Service2|ALL|ALL|APPLICATION|false", keys.get(1));
	}
	
	@Test
	public void testKeyValuesConsumer() {
		filters.get("Consumer").add("Consumer1");
		List<String> keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertEquals("ALL|ALL|Consumer1|ALL|APPLICATION|false", keys.get(0));
	
		filters.get("Consumer").add("Consumer2");
		keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertEquals(2, keys.size());
		assertEquals("ALL|ALL|Consumer1|ALL|APPLICATION|false", keys.get(0));
		assertEquals("ALL|ALL|Consumer2|ALL|APPLICATION|false", keys.get(1));
	}
	
	
	
	@Test
	public void testKeyValuesOperation() {
		filters.get("Operation").add("Operation1");
		List<String> keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertEquals("ALL|ALL|ALL|Operation1|APPLICATION|false", keys.get(0));
	
		filters.get("Operation").add("Operation2");
		keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertEquals(2, keys.size());
		assertEquals("ALL|ALL|ALL|Operation1|APPLICATION|false", keys.get(0));
		assertEquals("ALL|ALL|ALL|Operation2|APPLICATION|false", keys.get(1));
	}
	
	@Test
	public void testKeyValuesMix1() {
		filters.get("Service").add("Service1");
		filters.get("Service").add("Service2");
		
		filters.get("Consumer").add("Consumer1");
		filters.get("Consumer").add("Consumer2");
		
		filters.get("Operation").add("Operation1");
		filters.get("Operation").add("Operation2");
		filters.get("Operation").add("Operation3");
		filters.get("Operation").add("Operation4");
		
		List<String> keys = KeyGeneratorUtil.generate(serverSide,filters, param1);		
		assertEquals(16, keys.size()); // 2 exponential 8
		assertEquals("ALL|Service1|Consumer1|Operation1|APPLICATION|false", keys.get(0));
		assertEquals("ALL|Service1|Consumer1|Operation2|APPLICATION|false", keys.get(1));
		assertEquals("ALL|Service1|Consumer1|Operation3|APPLICATION|false", keys.get(2));
		assertEquals("ALL|Service1|Consumer1|Operation4|APPLICATION|false", keys.get(3));
		
		assertEquals("ALL|Service1|Consumer2|Operation1|APPLICATION|false", keys.get(4));
		assertEquals("ALL|Service1|Consumer2|Operation2|APPLICATION|false", keys.get(5));
		assertEquals("ALL|Service1|Consumer2|Operation3|APPLICATION|false", keys.get(6));
		assertEquals("ALL|Service1|Consumer2|Operation4|APPLICATION|false", keys.get(7));
		
		assertEquals("ALL|Service2|Consumer1|Operation1|APPLICATION|false", keys.get(8));
		assertEquals("ALL|Service2|Consumer1|Operation2|APPLICATION|false", keys.get(9));
		assertEquals("ALL|Service2|Consumer1|Operation3|APPLICATION|false", keys.get(10));
		assertEquals("ALL|Service2|Consumer1|Operation4|APPLICATION|false", keys.get(11));
		
		assertEquals("ALL|Service2|Consumer2|Operation1|APPLICATION|false", keys.get(12));
		assertEquals("ALL|Service2|Consumer2|Operation2|APPLICATION|false", keys.get(13));
		assertEquals("ALL|Service2|Consumer2|Operation3|APPLICATION|false", keys.get(14));
		assertEquals("ALL|Service2|Consumer2|Operation4|APPLICATION|false", keys.get(15));
		
			
	
	}
	
	
	private Map<String, List<String>> filterArrayGenerator() {
		final Map<String, List<String>> filters = new HashMap<String, List<String>>();
		filters.put("Server", new ArrayList<String>( ));
		filters.put("Service", new ArrayList<String>( ));
		filters.put("Operation", new ArrayList<String>( ));
		filters.put("Consumer", new ArrayList<String>( ));
		
		return filters;
	}
}
