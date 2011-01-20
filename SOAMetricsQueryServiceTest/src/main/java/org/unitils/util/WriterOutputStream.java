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
import java.io.OutputStream;
import java.io.Writer;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class WriterOutputStream extends OutputStream {

	private Writer writer;
	
	
	public WriterOutputStream(Writer writer) {
		this.writer = writer;
	}

	
	@Override
	public void write(int b) throws IOException {
		writer.write(b);
	}

}
