/*******************************************************************************
 * Copyright (c) 2006, 2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.unitils.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Wrapper that makes an Reader available as an InputStream 
 */
public class ReaderInputStream extends InputStream {
	
	private Reader reader;
	
	public ReaderInputStream(Reader reader) {
		this.reader = reader;
	}
	
	public int read() throws IOException {
		return reader.read();
	}
	
}
