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
 * ErrorDialog
 * 
 * A popup to show an error message
 *
 */
public class ErrorDialog extends AbstractDialog {
    
    public ErrorDialog (boolean animationEnabled) {
        super(animationEnabled);
       
        dialog.addStyleName("error");
        dialog.setText(ConsoleUtil.constants.error());
        label.addStyleName("error");
        ok.addStyleName("error");
    }
}
