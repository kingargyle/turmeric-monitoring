/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.common;

import org.ebayopensource.turmeric.monitoring.client.Display;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Interface TemplateDisplay.
 */
public interface TemplateDisplay {
	
	/**
	 * This interface manages the menu of the template display.
	 * @author nuy
	 *
	 */
	public interface MenuDisplay extends Display {

		
		/**
		 * The Interface MenuSelectionEventHandler.
		 */
		public interface MenuSelectionEventHandler extends EventHandler {
			
			/**
			 * On selection.
			 *
			 * @param action the action
			 */
			void onSelection(UserAction action);
		}
		
		/**
		 * The Class MenuSelectionEvent.
		 */
		public class MenuSelectionEvent extends GwtEvent<MenuSelectionEventHandler> {
			
			/** The Constant TYPE. */
			public static final Type<MenuSelectionEventHandler> TYPE = new Type<MenuSelectionEventHandler>();

			private UserAction action;
			
			/**
			 * Instantiates a new menu selection event.
			 *
			 * @param action the action
			 */
			public MenuSelectionEvent(UserAction action) {
				this.action = action;
			}
			
			/* (non-Javadoc)
			 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
			 */
			@Override
			protected void dispatch(MenuSelectionEventHandler handler) {
				handler.onSelection(action);
			}

			/* (non-Javadoc)
			 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
			 */
			@Override
			public Type<MenuSelectionEventHandler> getAssociatedType() {
				return TYPE;
			}
		}
	}
	
}
