/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.Resource;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetResourcesResponse;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * GetResourcesResponseJS
 *
 */
public class GetResourcesResponseJS extends JavaScriptObject implements
        GetResourcesResponse {

    public static final String NAME = "ns1.getResourcesResponse";
    /**
     * ResourceJS
     *
     */
    public static class ResourceJS extends JavaScriptObject implements Resource {

        protected ResourceJS() {}
        /**
         * @see org.ebayopensource.turmeric.monitoring.client.model.Resource#getDescription()
         */
        @Override
        public final native String getDescription() /*-{
            return this["@Description"];
        }-*/;

        /**
         * @see org.ebayopensource.turmeric.monitoring.client.model.Resource#getId()
         */
       
        public final native String getIdAsString() /*-{
            return this["@ResourceId"];
        }-*/;
        
        @Override
        public final Long getId() {
            return Long.valueOf(getIdAsString());
        }
        
    
        /**
         * @see org.ebayopensource.turmeric.monitoring.client.model.Resource#getOpList()
         */
        @Override
		public final List<Operation> getOpList() {
			List<Operation> operations = new ArrayList<Operation>();
			JsArray<OperationJS> jsOperations = getOperationsArray();
			if (jsOperations != null) {
			    for (int i=0;i<jsOperations.length();i++)
			        operations.add(jsOperations.get(i));
			}
			return operations;
		};
		
		public final native JsArray<OperationJS> getOperationsArray () /*-{
	    	return this["ns1.Operation"];
	    }-*/;
	    
		/**
         * @see org.ebayopensource.turmeric.monitoring.client.model.Resource#getResourceName()
         */
        @Override
        public final native String getResourceName() /*-{
            return this["@ResourceName"];
        }-*/;

        /**
         * @see org.ebayopensource.turmeric.monitoring.client.model.Resource#getResourceType()
         */
        @Override
        public final native String getResourceType() /*-{
            return this["@ResourceType"];
        }-*/;
    }
    
    
    protected GetResourcesResponseJS () {}
    
    public static final native GetResourcesResponse fromJSON (String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
        return null;
            }
    }-*/;
    
    
    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetResourcesResponse#getErrorMessage()
     */
    public native final String getErrorMessage() /*-{
        return this["ns1.getResourcesResponse"]["ms.errorMessage"];
    }-*/;

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetResourcesResponse#getResources()
     */
    public final Collection<Resource> getResources() {
        List<Resource> resources = new ArrayList<Resource>();
        JsArray<ResourceJS> jsResources = getResourcesArray();
        if (jsResources != null) {
            for (int i=0; i<jsResources.length();i++)
                resources.add(jsResources.get(i));
        }
        return resources;
    }
    
    
    public native final JsArray<ResourceJS> getResourcesArray () /*-{
        return this["ns1.getResourcesResponse"]["ns1.resources"];
    }-*/; 

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetResourcesResponse#isErrored()
     */
    public native final boolean isErrored() /*-{
        if (this["ns1.getResourcesResponse"]["ms.ack"] === "Success")
            return false;
        else
            return true;
    }-*/;

}
