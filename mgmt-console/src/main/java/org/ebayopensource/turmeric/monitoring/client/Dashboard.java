/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;



public interface Dashboard extends Container {
	
	public int addView (Display view, String name);
	public Display getView(Integer index);
	public int getIndex (Display view);
	public void activate (Display view);
    public HasSelectionHandlers<Integer> getTabSelector();
}
