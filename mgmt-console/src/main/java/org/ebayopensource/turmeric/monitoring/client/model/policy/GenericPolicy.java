/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import java.util.Date;
import java.util.List;



public interface GenericPolicy {
    public Long getId();
    public String getType();
    public String getName();
    public String getDescription();
    public List<SubjectGroup> getSubjectGroups();
    public List<Subject> getSubjects();
	public List<Resource> getResources();
	public Date getLastModified();
	public String getCreatedBy();
	public String getLastModifiedBy() ;
	public Date getCreationDate();
	public boolean getEnabled();
	public List<Rule> getRules();

	//TODO
	//public Version getVersion();
    
}