/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.event;

import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.ObjectType;



import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class ObjectSelectionEvent.
 */
public class ObjectSelectionEvent extends GwtEvent<ObjectSelectionEventHandler> {
    
    /** The TYPE. */
    public static Type<ObjectSelectionEventHandler> TYPE = new Type<ObjectSelectionEventHandler>();

    /** The selected objects. */
    Map<ObjectType,String> selectedObjects;

    /**
     * Instantiates a new object selection event.
     *
     * @param selection the selection
     */
    public ObjectSelectionEvent(Map<ObjectType,String> selection) {
        selectedObjects = new HashMap<ObjectType,String>(selection);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
     */
    @Override
    protected void dispatch(ObjectSelectionEventHandler handler) {
        handler.onSelection (this);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ObjectSelectionEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Gets the selections.
     *
     * @return the selections
     */
    public Map<ObjectType,String> getSelections() {
        return selectedObjects;
    }

    /**
     * Gets the selection.
     *
     * @param t the t
     * @return the selection
     */
    public String getSelection (ObjectType t) {
        return selectedObjects.get(t);
    }
}
