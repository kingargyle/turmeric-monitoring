/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

public interface RuleAttribute {
	public static enum NotifyKeys {NotifyEmails,  NotifyActive};
	public static enum NotifyActiveValue {TRUE, FALSE};
	    
	public String getKey();

	public String getValue();

}
