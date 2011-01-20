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

public class EntityHistoryImpl implements EntityHistory {

	private String comments;
	private String loginSubject;
	private String auditType;
	private Date lastModifiedTime;



	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setLoginSubject(String loginSubject) {
		this.loginSubject = loginSubject;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	@Override
	public String getComments() {
		return comments;
	}

	@Override
	public String getLoginSubject() {
		return loginSubject;
	}

	@Override
	public String getAuditType() {
		return auditType;
	}

	
	@Override
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}


	

}
