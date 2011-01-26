/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup;

public class GenericPolicyImpl implements GenericPolicy {
	private Long id;
	private String name;
	private String description;
	private String type;
	private Date creationDate;
	private String createdBy;
	private Date lastModified;
	private String lastModifiedBy;
	
	private boolean enabled;
	private List<SubjectGroup> subjectGroups;
	private List<Subject> subjects;
	private List<Resource> resources;
	private List<Rule> rules;

	
	@SuppressWarnings("unchecked")
	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public List<SubjectGroup> getSubjectGroups() {
		return subjectGroups;
	}
	public void setSubjectGroups(List<SubjectGroup> subjectGroups) {
		this.subjectGroups = subjectGroups;
	}
	public List<Resource> getResources() {
		return resources;
	}
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
    public List<Subject> getSubjects() {
        return subjects;
    }
	@Override
	public List<Rule> getRules() {
		return rules;
	}
    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
    
    public boolean equals (Object o) {
        if (!(o instanceof GenericPolicy))
            return false;
        
        GenericPolicy p = (GenericPolicy)o;
        
        if (this.id != null && p.getId() != null && this.id.equals(p.getId()))
            return true;
        if (this.type != null && this.type.equals(p.getType())) {
            if (this.name != null && this.name.equals(p.getName()))
                return true;
        }
        
        return false;
    }

}
