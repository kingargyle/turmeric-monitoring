/********************************************************************
 * Copyright (c) 2011 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.policy;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.client.model.UserAction;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * CustomPermissionCheckboxCell
 *
 */
public class CustomPermissionCheckboxCell<C, V> extends AbstractEditableCell<C, V> {

    UserAction action;
    Map<C, UserAction> pendingActions;
    Map<C, List<UserAction>> permittedActions;
    
    public CustomPermissionCheckboxCell (UserAction action, Map<C, UserAction> pendingActions, Map<C, List<UserAction>> permittedActions) {
        super("change", "keydown");
        this.action = action;
        this.pendingActions = pendingActions;
        this.permittedActions = permittedActions;
    }
    
    public void render(C value, Object key, SafeHtmlBuilder sb) {
      if (value == null)
          return;    
      
       //if the user has permission for the action, then render according to
      //the boolean value else render as disabled
      List<UserAction> permitted = permittedActions.get(value);
      UserAction pending = pendingActions.get(value);
 
      if (permitted != null && permitted.contains(this.action)) {
          if (pending != null && pending.equals(this.action))
              sb.appendHtmlConstant("<input type='checkbox' checked></input>");
          else                     
              sb.appendHtmlConstant("<input type='checkbox'></input>"); 
      } else  {
          //render as disabled
          if (pending != null && pending.equals(this.action))
              sb.appendHtmlConstant("<input type='checkbox' disabled=disabled checked></input>"); 
          else
              sb.appendHtmlConstant("<input type='checkbox' disabled=disabled></input>"); 
      }      
    }
    


    /**
     * @see com.google.gwt.cell.client.AbstractEditableCell#isEditing(com.google.gwt.dom.client.Element, java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean isEditing(Element arg0, C arg1, Object arg2) {
     
        return false;
    }

    
    @Override
    public void onBrowserEvent(Element parent, C value,
                               Object key, NativeEvent event,
                               ValueUpdater<C> valueUpdater) {
        String type = event.getType();

        boolean enterPressed = "keydown".equals(type) && event.getKeyCode() == KeyCodes.KEY_ENTER;
        if ("change".equals(type) || enterPressed) {
            InputElement input = parent.getFirstChild().cast();
        
            if (valueUpdater != null) {
                valueUpdater.update(value);
            }
        }
    }

}
