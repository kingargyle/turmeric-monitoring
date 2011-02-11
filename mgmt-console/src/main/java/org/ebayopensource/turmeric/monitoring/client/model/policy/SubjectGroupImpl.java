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
import java.util.Date;
import java.util.List;



/**
 * SubjectGroupImpl
 *
 */
public class SubjectGroupImpl implements SubjectGroup {
	private String name;
    private String type;
    private String groupCalculator;
    private String description;
    private String lastModifiedBy;
    private Date lastModifiedTime;
    private String createdBy;
    private List<String> subjects;
    private List<String> policies;
//    private SubjectMatchType subjectMatch;
    private List<SubjectMatchType> subjectMatchTypes;
    private Long id;

    
    public SubjectGroupImpl () {}
    
    public SubjectGroupImpl (SubjectGroup g) {
        if (g == null)
            return;
        this.id = g.getId();
        this.name = g.getName();
        this.type = g.getType();
        this.description = g.getDescription();
        this.lastModifiedBy = g.getLastModifiedBy();
        this.lastModifiedTime = g.getLastModifiedTime();
        this.createdBy = g.getCreatedBy();
        this.subjects = (g.getSubjects()==null?null:new ArrayList(g.getSubjects()));
        this.policies = (g.getPolicies()==null?null:new ArrayList(g.getPolicies()));
        this.subjectMatchTypes = g.getSubjectMatchTypes();
    }
    
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }
    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public List<String> getSubjects() {
        return subjects;
    } 
    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }
    public List<String> getPolicies() {
        return policies;
    }
    public void setPolicies(List<String> policies) {
        this.policies = policies;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getGroupCalculator() {
        return groupCalculator;
    }
    public void setGroupCalculator(String groupCalculator) {
        this.groupCalculator = groupCalculator;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getId() {
        return id;
    }
  
    public boolean equals (Object o) {
        if (!(o instanceof SubjectGroup))
            return false;
        
      
        
        if (name == null || type==null)
            return false;
       
        return (name.equals(((SubjectGroup)o).getName()) && type.equals(((SubjectGroup)o).getType()));
    }

	

//	public void setSubjectMatchType(SubjectMatchType subjectMatch) {
//		this.subjectMatch = subjectMatch;
//	} 
//
//	@Override
//	public SubjectMatchType getSubjectMatchType() {
//		return subjectMatch;
//	}

	public void setSubjectMatch(List<SubjectMatchType> subjectMatchType) {
		this.subjectMatchTypes = subjectMatchType;

	}
	public List<SubjectMatchType> getSubjectMatchTypes() {
		return subjectMatchTypes;
	}
}
