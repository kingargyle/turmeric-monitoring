/*******************************************************************************
 * Copyright (c) 2006, 2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.unitils.core.util;

import java.util.Properties;

/**
 * Base class for implementations that need access to the unitils configuration 
 * 
 * @author Filip Neven
 * @author Tim Ducheyne
 *
 */
public class BaseConfigurable implements Configurable {

	protected Properties configuration;
	
	/* 
	 * @see org.unitils.core.util.Configurable#init(java.util.Properties)
	 */
	public void init(Properties configuration) {
		this.configuration = configuration;
	}

}
