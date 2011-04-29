/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.Dictionary;

/**
 * The Class ConsoleUtil.
 */
public class ConsoleUtil {

    /** The Constant shotTimeFormat. */
    public static final DateTimeFormat shotTimeFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
    
    /** The Constant timeFormat. */
    public static final DateTimeFormat timeFormat = DateTimeFormat.getFormat("dd MMM yyyy HH:mm");
    
    /** The Constant onlyTimeFormat. */
    public static final DateTimeFormat onlyTimeFormat = DateTimeFormat.getFormat("HH:mm:ss");
    
    /** The Constant tzDateFormat. */
    public static final DateTimeFormat tzDateFormat = DateTimeFormat.getFormat("dd MM yyyy ZZZZ");
    
    /** The Constant tzTimeFormat. */
    public static final DateTimeFormat tzTimeFormat = DateTimeFormat.getFormat("dd MM yyyy HH:mm");
    
    /** The Constant xsDateTimeFormat. */
    public static final DateTimeFormat xsDateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SZZZ");
	
	/** The Constant constants. */
	public static final ConsoleConstants constants = GWT.create(ConsoleConstants.class);
	
	/** The Constant messages. */
	public static final ConsoleMessageConstants messages = GWT.create(ConsoleMessageConstants.class);
	private static final Map<String,String> configMap = new HashMap<String,String>();
	
	/**
	 * Gets the config.
	 *
	 * @return the config
	 */
	public static Map<String,String> getConfig () {
	    if (configMap.isEmpty()) {
	        
	        try {
	            Dictionary configDictionary = Dictionary.getDictionary("turmericConfig");
	            Set<String> keys = configDictionary.keySet();
	            for (String key:keys)
	                configMap.put(key, configDictionary.get(key));
	        } catch (MissingResourceException e) {
	            //log it?
	        }
	        
	    }
	        
	    return Collections.unmodifiableMap(configMap);
	}
	  
    /**
     * Convert consumer from missing.
     *
     * @param name the name
     * @return the string
     */
    public static String convertConsumerFromMissing (String name) {
        if (name == null)
            return null;
        if ("Missing".equalsIgnoreCase(name))
            return constants.unknown();
        return name;
            
    }
    
    /**
     * Convert consumer to missing.
     *
     * @param name the name
     * @return the string
     */
    public static String convertConsumerToMissing (String name) {
        if (name == null)
            return null;
        if (constants.unknown().equalsIgnoreCase(name))
            return "Missing";
        return name;
    }
}
