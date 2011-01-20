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

import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.RuleEffectType;

/**
 * Rule
 *
 */
public interface Rule {

    public Long getId();
    public String getRuleName();
    public String getDescription();
    public RuleEffectType getEffect();
    public Integer getPriority();
    public Integer getVersion();
    public Long getRolloverPeriod();
    public Long getEffectDuration();
    public Long getConditionDuration();
    public Condition getCondition();
    public List<RuleAttribute> getAttributeList();

}
