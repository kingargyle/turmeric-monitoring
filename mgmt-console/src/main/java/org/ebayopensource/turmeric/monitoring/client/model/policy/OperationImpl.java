/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

public class OperationImpl implements Operation {

	private String description;
	private String operationName;
	private String operationId;
	private long creationDate;
	private String creationBy;
	private long lastModifiedTime;
	private String lastModifiedBy;

	public void setCeationBy(String creationBy) {
		this.creationBy = creationBy;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getOperationName() {
		return operationName;
	}

	@Override
	public String getOperationId() {
		return operationId;
	}

	@Override
	public long getCreationDate() {

		return creationDate;
	}

	@Override
	public String getCreationBy() {
		return creationBy;
	}

	@Override
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	@Override
	public String getLastModifiedBy() {

		return lastModifiedBy;
	}

}
