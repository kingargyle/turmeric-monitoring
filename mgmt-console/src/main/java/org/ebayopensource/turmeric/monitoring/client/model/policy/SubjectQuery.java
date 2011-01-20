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

/**
 * SubjectQuery
 *
 */
public class SubjectQuery {

    private List<SubjectTypeKey> typeKeys;
    private List<SubjectKey> subjectKeys;
    private String query;
    
    public static class SubjectTypeKey {
        private Long typeId;
        private String typeName;
        
        public Long getTypeId() {
            return typeId;
        }
        public void setTypeId(Long typeId) {
            this.typeId = typeId;
        }
        public String getTypeName() {
            return typeName;
        }
        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

    public List<SubjectTypeKey> getTypeKeys() {
        return typeKeys;
    }

    public void setTypeKeys(List<SubjectTypeKey> typeKeys) {
        this.typeKeys = typeKeys;
    }

    public List<SubjectKey> getSubjectKeys() {
        return subjectKeys;
    }

    public void setSubjectKeys(List<SubjectKey> subjectKeys) {
        this.subjectKeys = subjectKeys;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
