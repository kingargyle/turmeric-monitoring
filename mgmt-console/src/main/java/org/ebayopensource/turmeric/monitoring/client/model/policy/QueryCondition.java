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
import java.util.List;

/**
 * QueryCondition
 *
 */
public class QueryCondition {
    public static enum ResolutionType {AND, OR};
    public static enum PolicyQueryType {Effect, ActivePoliciesOnly, SubjectSearchScope, ExpandResourceLevelPolicies};
    public static enum MetaDataQueryType {RESOURCE_TYPE, POLICY_TYPE, SUBJECT_TYPE};
    public static enum MetaDataQueryValue {Type};
    public static enum EffectValue {BLOCK, FLAG, CHALLENGE, ALLOW};
    public static enum ActivePoliciesOnlyValue {TRUE, FALSE};
    public static enum SubjectSearchScopeValue {TARGET, EXCLUDED, BOTH};
    public static enum ExpandResourceLevelPoliciesValue {TRUE, FALSE};
 
    
    private ResolutionType resolution;
    private List<Query> queries = new ArrayList<Query>();
    
    
    
    public static class Query { 
        protected String type;
        protected String value;
 
        public Query (String key, String value) {
            this.type = key;
            this.value = value;
        }
    
        public Query (MetaDataQueryType type, MetaDataQueryValue value) {
            this.type = type.toString();
            this.value = value.toString();
        }
        
        public Query (EffectValue value)
        {
            this.type=PolicyQueryType.Effect.toString();
            this.value=value.toString();
        }
        
        public Query (ActivePoliciesOnlyValue value)
        {
            this.type=PolicyQueryType.ActivePoliciesOnly.toString();
            this.value=value.toString();
        }
        public Query (SubjectSearchScopeValue value) {
            this.type = PolicyQueryType.SubjectSearchScope.toString();
            this.value = value.toString();
        }
        
        public Query (ExpandResourceLevelPoliciesValue value) {
            this.type = PolicyQueryType.ExpandResourceLevelPolicies.toString();
            this.value = value.toString();
        }
        
        public String getType () {
            return this.type;
        }
        
        public String  getValue()
        {
            return value;
        }
    };
    

    public ResolutionType getResolution() {
        return resolution;
    }

    public void setResolution(ResolutionType resolution) {
        this.resolution = resolution;
    }
    
    public void addQuery (Query q) {
        queries.add(q);
    }
 
    public List<Query> getQueries () {
        return queries;
    }
}
