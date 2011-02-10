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

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public interface PolicyTemplateDisplay {
	
	/**
	 * This interface manages the menu of the template display.
	 * @author nuy
	 *
	 */
	public interface MenuDisplay extends Display {

		
		public interface MenuSelectionEventHandler extends EventHandler {
			void onSelection(UserAction action);
		}
		
		public class MenuSelectionEvent extends GwtEvent<MenuSelectionEventHandler> {
			public static final Type<MenuSelectionEventHandler> TYPE = new Type<MenuSelectionEventHandler>();

			private UserAction action;
			
			public MenuSelectionEvent(UserAction action) {
				this.action = action;
			}
			
			@Override
			protected void dispatch(MenuSelectionEventHandler handler) {
				handler.onSelection(action);
			}

			@Override
			public Type<MenuSelectionEventHandler> getAssociatedType() {
				return TYPE;
			}
		}
	}
	
	
	/**
	 * This interface describes the template display. It has a menu, content, and logout components.
	 * @author nuy
	 *
	 */
	public interface PolicyPageTemplateDisplay extends Display {
		//Minimun size required in a elements list to be rendered with scrollbar
		public static int MIN_SCROLLBAR_SIZE = 5;
		Display getContentView();
	
	}
}
