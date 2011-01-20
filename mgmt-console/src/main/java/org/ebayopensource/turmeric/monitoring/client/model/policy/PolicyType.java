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
import java.util.Collections;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetMetaDataResponse;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * PolicyType
 *
 */
public class PolicyType {
    private static List<String> values;
    private static boolean init;
    
    public static List<String> getValues () {
        return Collections.unmodifiableList(values);
    }

    public static void init(PolicyQueryService service, final AsyncCallback<List<String>> callback) {
        if (init)
            return;
        
        QueryCondition query = new QueryCondition ();
        query.addQuery(new QueryCondition.Query(QueryCondition.MetaDataQueryType.POLICY_TYPE, QueryCondition.MetaDataQueryValue.Type));
        
        service.getMetaData(query, new AsyncCallback<GetMetaDataResponse> () {

            public void onFailure(Throwable arg0) {
                init = false;
                callback.onFailure(arg0);
            }

            public void onSuccess(GetMetaDataResponse response) {
                values = new ArrayList<String>(response.getValues().keySet());
                init = true;
                callback.onSuccess(values);
            }              
        });
    }
}
