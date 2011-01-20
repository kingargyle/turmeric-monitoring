/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client;


import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.UserAction;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;

/**
 * PolicyDashboard
 *
 */
public interface PolicyDashboard  extends Container {
    public void activate (Display view);
    public HasSelectionHandlers<TreeItem> getSelector();
    public void setActions(List<UserAction> actions);
    public void setSelected(UserAction action);
    public void error(String err);
}
