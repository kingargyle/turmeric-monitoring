/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view.policy;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class DescriptionCell extends AbstractCell<String> {
    PopupPanel popup;

    public DescriptionCell() {
        // Our cell responds to change events.
        super("click", "mouseout");
    }

    public void onBrowserEvent(Element parent, String value, Object key,
                               NativeEvent event, ValueUpdater<String> valueUpdater) {
        // Check that the value is not null.
        if (value == null) {
            return;
        }

        // Call the super handler, which handlers the enter key.
        super.onBrowserEvent(parent, value, key, event, valueUpdater);

        // Handle click events.
        if ("click".equals(event.getType())) {
            showPopup(parent, value);
        } else if ("mouseout".equals(event.getType()))
            hidePopup();
    }
    
    private void hidePopup() {
        if (popup != null)
            popup.hide(true);
    }

    private void showPopup (Element parent, String sg) {         
        popup = new PopupPanel(true);
        popup.setWidget(new Label ((sg==null?"":sg)));
        popup.setPopupPosition(parent.getAbsoluteLeft()+parent.getOffsetWidth(), parent.getAbsoluteTop());
        popup.show();
    }

    /**
     * @see com.google.gwt.cell.client.AbstractCell#render(java.lang.Object, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
     */
    @Override
    public void render(String sg, Object arg1, SafeHtmlBuilder safeHtml) {
        safeHtml.appendHtmlConstant ("<div><p class=\"clickable\">");
        if (sg != null) {
            int length = 10;
            if (sg.length()<length)
                length=sg.length();
            safeHtml.appendEscaped(sg.substring(0, length).trim()+"...");
        }
        safeHtml.appendHtmlConstant("</p></div>");
    }
}