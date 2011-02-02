/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

/**
 * SubjectImpl
 *
 */
public class SubjectImpl implements Subject {
    private String type;
    private String name;
    private String createdBy;
    private long externalSubjectId;
    private long lastModifiedTime;
    private String lastModifiedBy;
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
    public long getLastModifiedTime() {
        return lastModifiedTime;
    }
    
    public boolean equals (Object o) {
        if (!(o instanceof Subject))
            return false;
        
        if (name == null || type==null)
            return false;
       
        return (name.equals(((Subject)o).getName()) && type.equals(((Subject)o).getType()));
    }
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
    
	@Override
	public long getExternalSubjectId() {
		return externalSubjectId;
	}
	
	public void setExternalSubjectId(long externalSubjectId) {
		this.externalSubjectId = externalSubjectId;
	}
}
