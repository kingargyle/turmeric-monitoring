/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import com.google.gwt.core.client.JavaScriptObject;

public class OperationJS extends JavaScriptObject implements Operation {

	  protected OperationJS() {}
	   
	@Override
	public  native final String getDescription() /*-{
		return this["@Description"];
	}-*/;

	@Override
	public native final String getOperationName() /*-{
		return this["@OperationName"];
	}-*/;
	

	@Override
	public  native final String getOperationId() /*-{
		return this["@OperationId"];
	}-*/;

	@Override
	public  final long getCreationDate() {
		return 0;
	};

	@Override
	public native final  String getCreationBy() /*-{
		return this["@CreationBy"];
	}-*/;


	@Override
	public final  long getLastModifiedTime()  {
		return 0;
	};


	@Override
	public native final  String getLastModifiedBy()  /*-{
		return this["@LastModifiedBy"];
	}-*/;


}
