/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.common;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.Entity;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class GenericConfirmationWidget.
 */
public class GenericConfirmationWidget extends PopupPanel {
	
	/** The Constant CONFIRM_EVENTHANDLER_TYPE. */
	public final static Type<ConfirmEventHandler> CONFIRM_EVENTHANDLER_TYPE = new Type<ConfirmEventHandler>();

	private UserAction action;
	private Entity entity;
	
	private VerticalPanel mainPanel;
	private Label message;
	private Button confirmButton;
	private Button ignoreButton;
	
	/**
	 * Instantiates a new generic confirmation widget.
	 *
	 * @param action the action
	 * @param textMessage the text message
	 * @param entity the entity
	 */
	public GenericConfirmationWidget(UserAction action, String textMessage, Entity entity) {
		super();
		hide();		
		setGlassEnabled(true);
		
		this.action = action;
		this.entity = entity;
		
		mainPanel = new VerticalPanel();
		add(mainPanel);
		mainPanel.setSpacing(10);
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		message = new Label(textMessage);
		mainPanel.add(message);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		mainPanel.add(buttonPanel);
		confirmButton = new Button(ConsoleUtil.constants.confirm());
		ignoreButton = new Button(ConsoleUtil.constants.cancel());
		buttonPanel.add(confirmButton);
		buttonPanel.add(ignoreButton);
		
		bind();
	}
	
	/**
	 * Gets the action.
	 *
	 * @return the action
	 */
	public UserAction getAction() {
		return action;
	}
	
	/**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * Gets the confirm button.
	 *
	 * @return the confirm button
	 */
	public Button getConfirmButton() {
		return confirmButton;
	}
	
	private void bind() {
		confirmButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		ignoreButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});
	}

	/**
	 * The Interface ConfirmEventHandler.
	 */
	public interface ConfirmEventHandler extends EventHandler {
	    
    	/**
    	 * On selection.
    	 *
    	 * @param event the event
    	 */
    	public void onSelection(ConfirmEvent event);
	}

	/**
	 * The Class ConfirmEvent.
	 */
	public static class ConfirmEvent extends GwtEvent<ConfirmEventHandler> {
		
		private UserAction action;
		private Long entityId;
		
	    /**
    	 * Instantiates a new confirm event.
    	 *
    	 * @param action the action
    	 * @param entityId the entity id
    	 */
    	public ConfirmEvent(UserAction action, Long entityId) {
	    	this.action = action;
	    	this.entityId = entityId;
	    }
	    
    	/* (non-Javadoc)
    	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
    	 */
    	@Override
	    protected void dispatch(ConfirmEventHandler handler) {
	        handler.onSelection (this);
	    }
	    
    	/* (non-Javadoc)
    	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
    	 */
    	@Override
	    public Type<ConfirmEventHandler> getAssociatedType() {
	        return CONFIRM_EVENTHANDLER_TYPE;
	    }
	    
    	/**
    	 * Gets the action.
    	 *
    	 * @return the action
    	 */
    	public UserAction getAction() {
			return action;
		}
	    
    	/**
    	 * Gets the entity id.
    	 *
    	 * @return the entity id
    	 */
    	public Long getEntityId() {
			return entityId;
		}
	}
}
