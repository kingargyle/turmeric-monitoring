/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectQuery.SubjectTypeKey;

/**
 * SubjectGroupQuery
 *
 */
public class SubjectGroupQuery {
    private List<SubjectGroupKey> subjectKeys;
    private String query;
    private boolean includeSubjects;
    
    public List<SubjectGroupKey> getGroupKeys() {
        return subjectKeys;
    }
    public void setGroupKeys(List<SubjectGroupKey> subjectKeys) {
        this.subjectKeys = subjectKeys;
    }
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
    public void setIncludeSubjects(boolean includeSubjects) {
        this.includeSubjects = includeSubjects;
    }
    public boolean isIncludeSubjects() {
        return includeSubjects;
    }
}
