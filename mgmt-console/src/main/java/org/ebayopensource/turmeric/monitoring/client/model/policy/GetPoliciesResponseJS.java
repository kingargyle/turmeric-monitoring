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
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.model.policy.GetResourcesResponseJS.ResourceJS;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetPoliciesResponse;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.RuleEffectType;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * GetPoliciesResponseJS
 * 
 */
public class GetPoliciesResponseJS extends JavaScriptObject implements
		GetPoliciesResponse {

	public static final String NAME = "ns1.findPoliciesResponse";

	/**
	 * PolicyJS
	 * 
	 */
	public static class PolicyJS extends JavaScriptObject implements
			GenericPolicy {

		protected PolicyJS() {
		}

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy#getDescription()
		 */
		@Override
		public final native String getDescription() /*-{
			return this["ns2.Description"];
		}-*/;

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy#getId()
		 */
		public final native String getIdAsString() /*-{
			return this["@PolicyId"];
		}-*/;

		@Override
		public final Long getId() {
			return Long.valueOf(getIdAsString());
		}

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy#getName()
		 */
		@Override
		public final native String getName() /*-{
			return this["@PolicyName"];
		}-*/;

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy#getType()
		 */
		@Override
		public final native String getType() /*-{
			return this["@PolicyType"];
		}-*/;

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy#getRules()
		 */
		@Override
		public final List<Rule> getRules() {
			List<Rule> rules = new ArrayList<Rule>();
			JsArray<RuleJS> array = getRuleArray();
			if (array != null) {
				for (int i = 0; i < array.length(); i++)
					rules.add(array.get(i));
			}
			return rules;
		}

		public final native JsArray<RuleJS> getRuleArray() /*-{
			if (this["ns1.Rule"])
			return this["ns1.Rule"];
			else
			return null;
		}-*/;

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.GenericPolicy#getResources()
		 */
		@Override
		public final List<Resource> getResources() {
			List<Resource> resources = new ArrayList<Resource>();
			JsArray<ResourceJS> array = getResourceArray();
			if (array != null) {
				for (int i = 0; i < array.length(); i++)
					resources.add(array.get(i));
			}
			return resources;
		}

		public final native JsArray<ResourceJS> getResourceArray() /*-{
			if (this["ns1.Target"])
			if (this["ns1.Target"]["ns1.Resources"])
			return this["ns1.Target"]["ns1.Resources"]["ns1.Resource"];
			else
			return null;
			else
			return null;
		}-*/;

		/**
         * both Inclusion List & exclusion list are store at same list. the way to distinguish include subject as following example:
		 * assume a subject Id is 705033744 then at request schema:
		 * include subject looks like:
		 * <ns1:AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">(705033744)</ns1:AttributeValue>
		 * exclusion subject looks like:
		 * <ns1:AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">(?!705033744)</ns1:AttributeValue>
         */
		@Override
		public final List<SubjectGroup> getSubjectGroups() {
			List<SubjectGroup> subjects = new ArrayList<SubjectGroup>();
			JsArray<SubjectGroupJS> array = getSubjectGroupsArray();
			if (array != null) {
				for (int i = 0; i < array.length(); i++)
					if(! array.get(i).getName().startsWith("?!")){
						subjects.add(array.get(i));
					}
			}
			return subjects;
		}

		/**
         * both Inclusion List & exclusion list are store at same list. the way to distinguish include subject as following example:
		 * assume a subject Id is 705033744 then at request schema:
		 * include subject looks like:
		 * <ns1:AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">(705033744)</ns1:AttributeValue>
		 * exclusion subject looks like:
		 * <ns1:AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">(?!705033744)</ns1:AttributeValue>
         */
		@Override
		public final List<Subject> getSubjects() {
			List<Subject> subjects = new ArrayList<Subject>();
			JsArray<SubjectJS> array = getSubjectsArray();
			if (array != null) {
				for (int i = 0; i < array.length(); i++)
					if(! array.get(i).getName().startsWith("?!")){
						subjects.add(array.get(i));
					}
			}
			return subjects;
		}

		public final native JsArray<SubjectJS> getSubjectsArray() /*-{
			if (this["ns1.Target"])
			if (this["ns1.Target"]["ns1.Subjects"])
			return this["ns1.Target"]["ns1.Subjects"]["ns1.Subject"];
			else
			return null;
			else
			return null;
		}-*/;

		public final native JsArray<SubjectGroupJS> getSubjectGroupsArray() /*-{
			if (this["ns1.Target"])
			if (this["ns1.Target"]["ns1.Subjects"])
			return this["ns1.Target"]["ns1.Subjects"]["ns1.SubjectGroup"];
			else
			return null;
			else 
			return null;
		}-*/;

		public final Date getLastModified() {
			String tmp = getLastModifiedAsString();
			try {
				return ConsoleUtil.xsDateTimeFormat.parse(tmp);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}

		public final native String getLastModifiedAsString() /*-{
			return this["@LastModified"];
		}-*/;

		@Override
		public final native String getCreatedBy() /*-{
			return this["@CreatedBy"];
		}-*/;

		@Override
		public final native String getLastModifiedBy() /*-{
			return this["@LastModifiedBy"];
		}-*/;

		@Override
		public final Date getCreationDate() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public final boolean getEnabled() {
			return Boolean.valueOf(getEnabledAsString());
		}

		public native final String getEnabledAsString() /*-{
			return this["@Active"];
		}-*/;

		
		/**
         * both Inclusion List & exclusion list are store at same list. the way to distinguish include subject as following example:
		 * assume a subject Id is 705033744 then at request schema:
		 * include subject looks like:
		 * <ns1:AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">(705033744)</ns1:AttributeValue>
		 * exclusion subject looks like:
		 * <ns1:AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">(?!705033744)</ns1:AttributeValue>
         */
        @Override
		public final List<Subject> getExclusionSubjects() {
	        List<Subject> exclusionSubjects = new ArrayList<Subject>();
			JsArray<SubjectJS> array = getSubjectsArray();
			if (array != null) {
				for (int i = 0; i < array.length(); i++)
					if(array.get(i).getName().startsWith("?!")){
						exclusionSubjects.add(array.get(i));
					}
			}
			return exclusionSubjects;
		}
        
        /**
         * both Inclusion List & exclusion list are store at same list. the way to distinguish include subject as following example:
		 * assume a subject group Id is 705033744 then at request schema:
		 * include subject looks like:
		 * <ns1:AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">(705033744)</ns1:AttributeValue>
		 * exclusion subject looks like:
		 * <ns1:AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">(?!705033744)</ns1:AttributeValue>
         */
        	
		public final List<SubjectGroup> getExclusionSG() { 
	        List<SubjectGroup> exclusionSG = new ArrayList<SubjectGroup>();
			JsArray<SubjectGroupJS> array = getSubjectGroupsArray();
			if (array != null) {
				for (int i = 0; i < array.length(); i++)
					if(array.get(i).getName().startsWith("?!")){
						exclusionSG.add(array.get(i));
					}
			}
			return exclusionSG;
		}

	}

	/**
	 * RuleJS
	 * 
	 */
	public static class RuleJS extends JavaScriptObject implements Rule {

		protected RuleJS() {
		}

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.model.Rule#getId()
		 */
		@Override
		public final Long getId() {
			return Long.valueOf(getIdAsString());
		}

		private final native String getIdAsString() /*-{
			return this["@RuleId"];
		}-*/;

		@Override
		public native final String getRuleName() /*-{
			return this["@RuleName"];
		}-*/;

		@Override
		public final String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.model.Rule#getEffect()
		 */
		@Override
		public final RuleEffectType getEffect() {
			return RuleEffectType.valueOf(getEffectAsString());
		}

		private final native String getEffectAsString() /*-{
			return this["@Effect"];
		}-*/;

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.model.Rule#getPriority()
		 */
		@Override
		public final Integer getPriority() {
			return Integer.valueOf(getPriorityAsString());
		}

		private final native String getPriorityAsString() /*-{
			return this["@Priority"];
		}-*/;

		@Override
		public final Integer getVersion() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.model.Rule#getRolloverPeriod()
		 */
		@Override
		public final Long getRolloverPeriod() {
			return Long.valueOf(getRolloverPeriodAsString());
		}

		private final native String getRolloverPeriodAsString() /*-{
			return this["@RolloverPeriod"];
		}-*/;

		/**
		 * @see org.ebayopensource.turmeric.monitoring.client.model.Rule#getEffectDuration()
		 */
		@Override
		public final Long getEffectDuration() {
			return Long.valueOf(getEffectDurationAsString());
		}

		private final native String getEffectDurationAsString() /*-{
			return this["@EffectDuration"];
		}-*/;

		@Override
		public final Long getConditionDuration() {
			// TODO Auto-generated method stub
			return null;
		}


		public final Condition getCondition() {
			return getConditionsAsObject();
		}

		private final native ConditionJS getConditionsAsObject() /*-{
			return this["ns1.Condition"];
		}-*/;

		/**
		 * ConditionJS
		 * 
		 */
		public static class ConditionJS extends JavaScriptObject implements
				Condition {

			protected ConditionJS() {
			}

			@Override
			public final Expression getExpression() {
				return getExpressionAsObject();

			}

			public final native ExpressionJS getExpressionAsObject() /*-{
				return (this["ns1.Expression"]);
			}-*/;

			/**
			 * ExpressionJS
			 * 
			 */
			public static class ExpressionJS extends JavaScriptObject implements
					Expression {

				protected ExpressionJS() {
				}

				@Override
				public final Long getId() {
					return null;
				}

				public native final String getName() /*-{
					return this["@Name"];
				}-*/;

				@Override
				public final native String getComment() /*-{
					return this["ns1.Comment"];
				}-*/;

				@Override
				public final PrimitiveValue getPrimitiveValue() {
					return getPrimitiveValueAsObject();
				}

				public final native PrimitiveValueJS getPrimitiveValueAsObject() /*-{
					return this["ns1.PrimitiveValue"];
				}-*/;

				/**
				 * PrimitiveValueJS
				 * 
				 */
				public static class PrimitiveValueJS extends JavaScriptObject
						implements PrimitiveValue {

					protected PrimitiveValueJS() {
					}

					@Override
					public final Long getId() {
						return null;
					}

					@Override
					public final SupportedPrimitive getType() {
						return SupportedPrimitive.fromValue(getTypeAsString());
					}

					private final native String getTypeAsString() /*-{
						return this["@type"];
					}-*/;

					@Override
					public final native String getValue() /*-{
						return this["@value"];
					}-*/;

				}
			}
		}

		@Override
		public final List<RuleAttribute> getAttributeList() {
			
			List<RuleAttribute> attibutes = new ArrayList<RuleAttribute>();
			JsArray<RuleAttributeJS> jsAttributes= getRuleAttributeAsArray();
			if (jsAttributes != null) {
				for (int i = 0; i < jsAttributes.length(); i++)
					attibutes.add(jsAttributes.get(i));
			}
			return attibutes;
		}

		private final native JsArray<RuleAttributeJS> getRuleAttributeAsArray() /*-{
			return this["ns1.Attribute"];
		}-*/;

		/**
		 * RuleAttributeJS
		 * 
		 */
		public static class RuleAttributeJS extends JavaScriptObject
				implements RuleAttribute {

			protected RuleAttributeJS() {
			}

			
		    @Override
			public native final String getKey() /*-{
				return this["ns1.key"];
			}-*/;

			@Override
			public final native String getValue() /*-{
				return this["ns1.value"];
			}-*/;
		}

		
	}

	/**
	 * PolicySetJS
	 * 
	 */
	public static class PolicySetJS extends JavaScriptObject {
		protected PolicySetJS() {
		}

		public final native JsArray<PolicyJS> getPolicies() /*-{
			return this["ns1.policy"];
		}-*/;
	}

	protected GetPoliciesResponseJS() {
	}

	public static final native GetPoliciesResponseJS fromJSON(String json) /*-{
		try {
		return eval('(' + json + ')');
		} catch (err) {
		return null;
		}
	}-*/;

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetPoliciesResponse#getErrorMessage()
	 */
	public native final String getErrorMessage() /*-{
		return this["ns1.findPoliciesResponse"]["ms.errorMessage"];
	}-*/;

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetPoliciesResponse#getPolicies()
	 */
	public final Collection<GenericPolicy> getPolicies() {
		List<GenericPolicy> policies = new ArrayList<GenericPolicy>();
		JsArray<PolicyJS> jsPolicies = getPolicyArray();
		if (jsPolicies != null) {
			for (int i = 0; i < jsPolicies.length(); i++)
				policies.add(jsPolicies.get(i));
		}
		return policies;
	}

	public native final JsArray<PolicyJS> getPolicyArray() /*-{
															if (this["ns1.findPoliciesResponse"]["ns1.policySet"])
															return this["ns1.findPoliciesResponse"]["ns1.policySet"]["ns1.Policy"];
															else
															return null;
															}-*/;

	/**
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyQueryService.GetPoliciesResponse#isErrored()
	 */
	public native final boolean isErrored() /*-{
											if (this["ns1.findPoliciesResponse"]["ms.ack"] === "Success")
											return false;
											else
											return true;
											}-*/;
}
