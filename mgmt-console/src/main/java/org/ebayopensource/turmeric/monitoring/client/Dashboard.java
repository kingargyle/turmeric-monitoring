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



/**
 * The Interface Dashboard.
 */
public interface Dashboard extends Container {
	
	/**
	 * Adds the view.
	 *
	 * @param view the view
	 * @param name the name
	 * @return the int
	 */
	public int addView (Display view, String name);
	
	/**
	 * Gets the view.
	 *
	 * @param index the index
	 * @return the view
	 */
	public Display getView(Integer index);
	
	/**
	 * Gets the index.
	 *
	 * @param view the view
	 * @return the index
	 */
	public int getIndex (Display view);
	
	/**
	 * Activate.
	 *
	 * @param view the view
	 */
	public void activate (Display view);
    
    /**
     * Gets the tab selector.
     *
     * @return the tab selector
     */
    public HasSelectionHandlers<Integer> getTabSelector();
}
