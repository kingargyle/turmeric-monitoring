/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * AbstractDialog
 *
 */
public abstract class AbstractDialog extends Composite {
    protected DialogBox dialog;
    protected Label label;
    protected Button ok;
    protected FlowPanel contents;
    
    public AbstractDialog (boolean animationEnabled) {
        dialog = new DialogBox();
        
        // Create a table to layout the content
        FlowPanel dialogContents = new FlowPanel();
        dialog.setWidget(dialogContents);
        dialog.setAnimationEnabled(animationEnabled);

        // Add some text to the dialog
        label = new Label();
        dialogContents.add(label);

        // Add a close button at the bottom of the dialog
        ok = new Button(ConsoleUtil.constants.ok());
        ok.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialog.hide();
            }
        });
        dialogContents.add(ok);
    }
    
    public void setMessage (String message) {
        label.setText(message);
    }
    
    
    public void show () {
        dialog.show();
    }

    public void hide () {
        dialog.hide();
    }
    
    public DialogBox getDialog () {
        return dialog;
    }
}
