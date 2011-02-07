/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import java.util.List;

/**
 * PolicySubjectAssignment
 *
 */
public class PolicySubjectAssignment {
    
    private String subjectType;
    private List<Subject> subjects;
    private List<Subject> exclusionSubjects;
    private List<SubjectGroup> subjectGroups;
    private List<SubjectGroup> exclusionSubjectGroups;

    

	public List<Subject> getExclusionSubjects() {
		return exclusionSubjects;
	}
	public void setExclusionSubjects(List<Subject> exclusionSubjects) {
		this.exclusionSubjects = exclusionSubjects;
	}
	public String getSubjectType() {
        return subjectType;
    }
    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }
    public List<Subject> getSubjects() {
        return subjects;
    }
    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
    public List<SubjectGroup> getSubjectGroups() {
        return subjectGroups;
    }
    public void setSubjectGroups(List<SubjectGroup> subjectGroups) {
        this.subjectGroups = subjectGroups;
    }
    public List<SubjectGroup> getExclusionSubjectGroups() {
		return exclusionSubjectGroups;
	}
	public void setExclusionSubjectGroups(List<SubjectGroup> exclusionSubjectGroups) {
		this.exclusionSubjectGroups = exclusionSubjectGroups;
	}
}
