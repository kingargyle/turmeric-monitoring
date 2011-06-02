/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.user.client.History;


/**
 * HistoryToken
 * 
 * servicePresenter-id:[name=value]*n.
 */
public class HistoryToken {
    
    /** The Constant DELIM. */
    public static final String DELIM = "~";
    
    /** The Constant EQUALS. */
    public static final String EQUALS = "=";
    
    /** The Constant SUB. */
    public final static String SUB = "sub";
    
    /** The Constant SELECTED_SERVICE_TOKEN. */
    public final static String SELECTED_SERVICE_TOKEN = "svc";
    
    /** The Constant SELECTED_OPERATION_TOKEN. */
    public final static String SELECTED_OPERATION_TOKEN = "op";
    
    /** The Constant SELECTED_CONSUMER_TOKEN. */
    public final static String SELECTED_CONSUMER_TOKEN = "cnsmr";
    
    /** The Constant SELECTED_DATE1_TOKEN. */
    public final static String SELECTED_DATE1_TOKEN = "d1";
    
    /** The Constant SELECTED_DATE2_TOKEN. */
    public final static String SELECTED_DATE2_TOKEN = "d2";
    
    /** The Constant SELECTED_DURATION_TOKEN. */
    public final static String SELECTED_DURATION_TOKEN = "drn";
    
    /** The Constant SELECTED_METRICS_TOKEN. */
    public final static String SELECTED_METRICS_TOKEN = "mtrcs";
    
    /** The Constant SELECTED_ERROR_NAME_TOKEN. */
    public final static String SELECTED_ERROR_NAME_TOKEN = "err";
    
    /** The Constant SELECTED_ERROR_ID_TOKEN. */
    public final static String SELECTED_ERROR_ID_TOKEN = "errid";
    
    /** The Constant SELECTED_ERROR_TYPE_TOKEN. */
    public final static String SELECTED_ERROR_TYPE_TOKEN = "errtyp";
    
    /** The Constant SELECTED_SUBJECT_GROUP_TOKEN. */
    public final static String SELECTED_SUBJECT_GROUP_TOKEN = "sgrp";
    
    /** The Constant SELECTED_SUBJECT_GROUP_TYPE_TOKEN. */
    public final static String SELECTED_SUBJECT_GROUP_TYPE_TOKEN = "sgtyp";
    
    /** The Constant SELECTED_POLICY_TOKEN_ID. */
    public final static String SELECTED_POLICY_TOKEN_ID = "plcy_id";
    
    /** The Constant SELECTED_POLICY_TOKEN_TYPE. */
    public final static String SELECTED_POLICY_TOKEN_TYPE = "plcy_type";

    /** The Constant SRCH_SUBJECT_GROUP_TYPE. */
    public final static String SRCH_SUBJECT_GROUP_TYPE = "sgrptyp";
    
    /** The Constant SRCH_SUBJECT_GROUP_NAME. */
    public final static String SRCH_SUBJECT_GROUP_NAME = "sgrpnm";
    
    /** The Constant SRCH_SUBJECT_GROUP_ID. */
    public final static String SRCH_SUBJECT_GROUP_ID = "sgrpid";

    /** The Constant SRCH_POLICY_TYPE. */
    public final static String SRCH_POLICY_TYPE = "plcytyp";
    
    /** The Constant SRCH_POLICY_NAME. */
    public final static String SRCH_POLICY_NAME = "plcynm";
    

    
	private String presenterId;
	private Map<String,String> map;
	
	private HistoryToken (String token) {
		parse (token);
	}

	private HistoryToken (String presenterId, Map<String,String> map) {
		this.presenterId = presenterId;
		if (map != null)
			this.map = new HashMap<String,String>(map);
	}
	
	private void parse (String token) {
		if (token == null) return;
		String[] elements = token.split(DELIM);
		if (elements != null) {
			for (int i=0;i<elements.length;i++) {
				String element = elements[i];
				if (i == 0)
					this.presenterId = element;
				else {
					if (this.map == null)
						this.map = new HashMap<String, String>();

					int idx = element.indexOf(EQUALS);
					if (idx > 0) {
						String[] pairs = element.split(EQUALS);
						if (pairs != null && pairs.length==2) {
							this.map.put(pairs[0], pairs[1]);
						}
					}
				}
			}
		}
	}

	/**
	 * Gets the servicePresenter id.
	 *
	 * @return the servicePresenter id
	 */
	public String getPresenterId () {
		return this.presenterId;
	}
	
	/**
	 * Gets the value.
	 *
	 * @param key the key
	 * @return the value
	 */
	public String getValue (String key) {
		if (this.map == null) return null;
		return (this.map.get(key));
	}
	
	/**
	 * Adds the value.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void addValue (String key, String value) {
		if (this.map == null)
			this.map = new HashMap<String,String>();
		this.map.put(key, value);
	}
	
	/**
	 * Adds the value.
	 *
	 * @param key the key
	 * @param list the list
	 */
	public void addValue (String key, Collection<?> list) {
	    StringBuffer strbuff = new StringBuffer();
	    for (Object o:list) {
	        strbuff.append(o.toString());
	        strbuff.append(",");
	    }
	    if (strbuff.length() > 0)
	        strbuff.deleteCharAt(strbuff.length()-1);
	    
	    addValue(key, strbuff.toString());
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	public String toString () {
		if (this.presenterId == null) return "";
		StringBuffer strbuff = new StringBuffer();
		strbuff.append(this.presenterId);
		strbuff.append(DELIM);
		if (this.map != null) {
			Iterator<String> itor = this.map.keySet().iterator();
			while (itor.hasNext()) {
				String key = itor.next();
				strbuff.append(key);
				strbuff.append(EQUALS);
				strbuff.append(this.map.get(key));
				if (itor.hasNext())
					strbuff.append(DELIM);
			}
		}
		return strbuff.toString();
	}
	
	/**
	 * New history token.
	 *
	 * @param token the token
	 * @return the history token
	 */
	public static HistoryToken newHistoryToken (String token) {
		return new HistoryToken(token);
	}
	
	/**
	 * New history token.
	 *
	 * @return the history token
	 */
	public static HistoryToken newHistoryToken () {
	    String t = History.getToken();
	    if (t == null || t.isEmpty())
	        return null;
	    
		return new HistoryToken(History.getToken());
	}
	
	/**
	 * New history token.
	 *
	 * @param presenterId the servicePresenter id
	 * @param map the map
	 * @return the history token
	 */
	public static HistoryToken newHistoryToken (String presenterId, Map<String,String> map) {
		return new HistoryToken(presenterId, map);
	}
	
	/**
	 * Gets the value.
	 *
	 * @param token the token
	 * @param key the key
	 * @return the value
	 */
	public static String getValue(HistoryToken token, String key) {
		if (token == null) return null;
		return (token.getValue(key));
	}
	
	/**
	 * Gets the value.
	 *
	 * @param token the token
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the value
	 */
	public static String getValue(HistoryToken token, String key, String defaultValue) {
		if (token == null) return defaultValue;
		String val = token.getValue(key);
		if (val == null) return defaultValue;
		return val;
	}
}
