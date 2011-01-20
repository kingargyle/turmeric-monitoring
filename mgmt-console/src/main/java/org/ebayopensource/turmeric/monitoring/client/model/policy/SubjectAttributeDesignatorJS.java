/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * SubjectAttributeDesignatorJS
 *
 */
public class SubjectAttributeDesignatorJS extends JavaScriptObject implements SubjectAttributeDesignator {

    protected SubjectAttributeDesignatorJS() {}

    @Override
    public native final String getAttributeId() /*-{
        return this["@AttributeId"];
    }-*/;
        
  
}
