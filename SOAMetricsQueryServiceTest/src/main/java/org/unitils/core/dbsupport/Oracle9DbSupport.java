/*******************************************************************************
 * Copyright (c) 2006, 2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.unitils.core.dbsupport;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class Oracle9DbSupport extends OracleDbSupport {

	@Override
	protected Integer getOracleMajorVersionNumber() {
		return 9;
	}

	
}
