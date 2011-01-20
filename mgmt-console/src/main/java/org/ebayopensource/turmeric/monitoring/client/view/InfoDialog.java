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


/**
 * InfoDialog
 *
 */
public class InfoDialog extends AbstractDialog {


    public InfoDialog (boolean animationEnabled) {
        super(animationEnabled);
        dialog.setText(ConsoleUtil.constants.information());
        //TODO style
    }
}
