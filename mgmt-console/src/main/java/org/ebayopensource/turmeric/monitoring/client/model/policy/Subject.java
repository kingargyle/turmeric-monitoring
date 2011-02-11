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
 * Subject
 *
 */
public interface Subject {
    public String getType();
    public String getName();
    public long getExternalSubjectId();
    public List<SubjectMatchType> getSubjectMatchTypes();
    public String getCreatedBy();
    public long getLastModifiedTime();
    public String getLastModifiedBy();
}
