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
 * RuleImpl
 *
 */
public class RuleImpl implements Rule {

	private Long id;
	private String ruleName;
	private String description;
	private RuleEffectType effect;
	private Integer priority;
	private Integer version;
	

	private Long rolloverPeriod;
	private Long effectDuration;
	private Long conditionDuration;
	private Condition condition;
    public List<RuleAttribute> attributeList;
	
	
	public RuleImpl(String ruleName, String description, RuleEffectType effect, Integer priority,
			Long rolloverPeriod, Long effectDuration, Long conditionDuration, Condition condition, 
			List<RuleAttribute> attributeList){
		this.ruleName=ruleName;
		this.description=description;
		this.effect = effect;
		this.priority = priority;
		this.rolloverPeriod =rolloverPeriod;
		this.effectDuration =effectDuration;
		this.conditionDuration = conditionDuration;
		this.condition = condition;
		this.attributeList = attributeList;
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public RuleEffectType getEffect() {
		return effect;
	}
	public void setEffect(RuleEffectType effect) {
		this.effect = effect;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getVersion() {
		return version;
	}
//	public void setVersion(Integer version) {
//		this.version = version;
//	}
	public Long getRolloverPeriod() {
		return rolloverPeriod;
	}
	public void setRolloverPeriod(Long rolloverPeriod) {
		this.rolloverPeriod = rolloverPeriod;
	}
	public Long getEffectDuration() {
		return effectDuration;
	}
	public void setEffectDuration(Long effectDuration) {
		this.effectDuration = effectDuration;
	}
	public Long getConditionDuration() {
		return conditionDuration;
	}
	public void setConditionDuration(Long conditionDuration) {
		this.conditionDuration = conditionDuration;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Condition getCondition() {
		return condition;
	}
	
	public List<RuleAttribute> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<RuleAttribute> attributeList) {
		this.attributeList = attributeList;
	}


	
}
