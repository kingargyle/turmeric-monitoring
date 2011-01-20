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

public class ObjectSelectionEvent extends GwtEvent<ObjectSelectionEventHandler> {
    public static Type<ObjectSelectionEventHandler> TYPE = new Type<ObjectSelectionEventHandler>();

    Map<ObjectType,String> selectedObjects;

    public ObjectSelectionEvent(Map<ObjectType,String> selection) {
        selectedObjects = new HashMap<ObjectType,String>(selection);
    }

    @Override
    protected void dispatch(ObjectSelectionEventHandler handler) {
        handler.onSelection (this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ObjectSelectionEventHandler> getAssociatedType() {
        return TYPE;
    }

    public Map<ObjectType,String> getSelections() {
        return selectedObjects;
    }

    public String getSelection (ObjectType t) {
        return selectedObjects.get(t);
    }
}
