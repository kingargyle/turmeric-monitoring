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
 * SubjectJS
 *
 */
public class SubjectJS extends JavaScriptObject implements Subject {

    protected SubjectJS() {}
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.Subject#getCreatedBy()
     */
    public native final String getLastModifiedBy() /*-{
        return this["@LastModifiedBy"];
    }-*/;
    
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.Subject#getCreatedBy()
     */
    public native final String getCreatedBy() /*-{
        return this["@CreatedBy"];
    }-*/;

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.Subject#getLastModifiedTime()
     */
    @Override
    public final long getLastModifiedTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.Subject#getName()
     */
    public native final String getName() /*-{
        return this["@SubjectName"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.Subject#getType()
     */
    public native final  String getType() /*-{
        return this["@SubjectType"];
    }-*/;

}
