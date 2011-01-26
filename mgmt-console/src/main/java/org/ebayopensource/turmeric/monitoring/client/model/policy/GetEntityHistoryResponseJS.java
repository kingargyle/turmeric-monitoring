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
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.policy.GetPoliciesResponseJS.PolicyJS;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.CreatePolicyResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetEntityHistoryResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetPoliciesResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * GetEntityHistoryResponseJS
 *
 */
public class GetEntityHistoryResponseJS extends JavaScriptObject implements
        GetEntityHistoryResponse{

	protected static String NAME= "ns1.getEntityHistoryResponse";
 
	protected GetEntityHistoryResponseJS() {}

    public static final native GetEntityHistoryResponseJS fromJSON (String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
        return null;
        }    
    }-*/;
    
    @Override
	public final Collection<EntityHistory> getEntities() {
        List<EntityHistory> entities= new ArrayList<EntityHistory>();
        JsArray<EntityHistoryJS> jsEntities= getEntityArray();
        if (jsEntities != null) {
            for (int i=0; i<jsEntities.length();i++)
                entities.add(jsEntities.get(i));
        }
        return entities;
    }
    
    public native final JsArray<EntityHistoryJS> getEntityArray () /*-{
        if (this["ns1.getEntityHistoryResponse"]["ns1.entityHistories"])
            return this["ns1.getEntityHistoryResponse"]["ns1.entityHistories"];
        else
            return null;
    }-*/; 

    
	
    public static class EntityHistorySetJS  extends JavaScriptObject {
        protected EntityHistorySetJS () {}
        
        public final native JsArray<EntityHistoryJS> getEntities() /*-{
           	return this["ns1.entityHistories"]; 
        }-*/;
    }

	@Override
	 public native final boolean isErrored() /*-{
    if (this["ns1.getEntityHistoryResponse"]["ms.ack"] === "Success")
        return false;
    else
        return true;
	}-*/;

	@Override
	public native final String getErrorMessage() /*-{
    return this["ns1.getEntityHistoryResponse"]["ms.errorMessage"];
	}-*/;


	/**
     * EntityHistoryJS
     *
     */
    public static class EntityHistoryJS extends JavaScriptObject implements EntityHistory {

        protected EntityHistoryJS() {}
       
        @Override
        public final native String getComments() /*-{
            return this["ns1.comments"];
        }-*/;
        
        @Override
        public final native String getLoginSubject() /*-{
            return this["ns1.loginSubject"];
        }-*/;

        public final native String getLastModifiedTimeAsString() /*-{
        return this["ns1.auditDate"];
	    }-*/;
	    
	    @Override
	    public final Date getLastModifiedTime() {
		    String tmp = getLastModifiedTimeAsString();
		        try {
			        return ConsoleUtil.xsDateTimeFormat.parse(tmp);
			    } catch (Exception e) {
			        return null;
			}
		    
		}

		
	    
		@Override
		public final native String getAuditType() /*-{
        	return this["ns1.auditType"];
    	}-*/;
		
    
    }

	
	
}
