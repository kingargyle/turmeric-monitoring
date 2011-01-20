/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.policy.RuleAttribute.NotifyActiveValue;

/**
 * RuleAttributeImpl
 *
 */
public class RuleAttributeImpl implements RuleAttribute {   
    
	protected String key;
	protected String value;

	public RuleAttributeImpl(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public RuleAttributeImpl(String value) {
		this.key = RuleAttributeImpl.NotifyKeys.NotifyEmails.toString();
		this.value = value.toString();
	}

	public RuleAttributeImpl(NotifyActiveValue value) {
		this.key = RuleAttributeImpl.NotifyKeys.NotifyActive.toString();
		this.value = value.toString();
	}

	public String getKey() {
		return this.key;
	}

	public String getValue() {
		return value;
	}
}
