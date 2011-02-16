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

/**
 * GenericPolicyConverter
 * 
 */
public class GenericPolicyConverter {

	public static String toNV(GenericPolicy policy) {
		String url = "";
		if (policy == null)
			return url;

		url += (policy.getId() == null ? "" : "&ns1:policy.@PolicyId="
				+ policy.getId().toString());

		// type, name are mandatory

		url += (policy.getType() == null || policy.getType().equals("") ? ""
				: "&ns1:policy.@PolicyType=" + policy.getType().toString());
		String tmp = policy.getName();
		if (tmp != null && !"".equals(tmp.trim()))
			url += "&ns1:policy.@PolicyName=" + tmp;

		// description & status are optional
		// url += "&ns1:policy.@Active="+policy.getEnabled();
		url += (policy.getDescription() == null
				|| "".equals(policy.getDescription().trim()) ? ""
				: "&ns1:policy.ns2:Description=" + policy.getDescription());

		// rule is optional for RateLimiting policies
		if (policy.getRules() != null) {
			int i = 0;
			for (Rule rule : policy.getRules()) {
				url += (rule.getEffect() == null ? "" : "&ns1:policy.ns1:Rule("
						+ i + ").@Effect=" + rule.getEffect());
				url += (rule.getRuleName() == null ? ""
						: "&ns1:policy.ns1:Rule(" + i + ").@RuleName="
								+ rule.getRuleName()
								+ String.valueOf(Math.random()).substring(2, 5));
				url += (rule.getPriority() == null ? ""
						: "&ns1:policy.ns1:Rule(" + i + ").@Priority="
								+ rule.getPriority());
				url += (rule.getRolloverPeriod() == null ? ""
						: "&ns1:policy.ns1:Rule(" + i + ").@RolloverPeriod="
								+ rule.getRolloverPeriod());
				url += (rule.getEffectDuration() == null ? ""
						: "&ns1:policy.ns1:Rule(" + i + ").@EffectDuration="
								+ rule.getEffectDuration());
				url += (rule.getConditionDuration() == null ? ""
						: "&ns1:policy.ns1:Rule(" + i + ").@ConditionDuration="
								+ rule.getConditionDuration());
				url += (rule.getDescription() == null ? ""
						: "&ns1:policy.ns1:Rules(" + i + ").@Description="
								+ rule.getDescription());

				if (rule.getCondition() != null) {
					Condition condition = rule.getCondition();
					if (condition.getExpression() != null) {
						Expression expression = condition.getExpression();
						url += (expression.getName() == null ? ""
								: ("&ns1:policy.ns1:Rule.ns1:Condition.ns1:Expression.@name=" + rule
										.getConditionDuration()));
						if (expression.getPrimitiveValue() != null) {
							PrimitiveValue primitiveValue = expression
									.getPrimitiveValue();
							url += "&ns1:policy.ns1:Rule.ns1:Condition.ns1:Expression.ns1:PrimitiveValue.@type="
									+ primitiveValue.getType();
							url += "&ns1:policy.ns1:Rule.ns1:Condition.ns1:Expression.ns1:PrimitiveValue.@value="
									+ primitiveValue.getValue();
						}

					}

				}

				if (rule.getAttributeList() != null) {
					List<RuleAttribute> attributeList = rule.getAttributeList();
					if (attributeList.size() > 0) {
						int j = 0;
						for (RuleAttribute attribute : attributeList) {
							url += "&ns1:policy.ns1:Rule.ns1:Attribute(" + j
									+ ").ns1:key="
									+ attribute.getKey().toString();
							url += "&ns1:policy.ns1:Rule.ns1:Attribute(" + j
									+ ").ns1:value="
									+ attribute.getValue().toString();
							j++;
						}
					}

				}

				i++;
			}
		}

		// resources
		if (policy.getResources() != null) {
			int i = 0;
			for (Resource r : policy.getResources()) {
				url += (r.getId() == null ? ""
						: "&ns1:policy.ns1:Target.ns1:Resources.ns1:Resources("
								+ i + ").@ResourceId=" + r.getId());
				url += (r.getResourceName() == null ? ""
						: "&ns1:policy.ns1:Target.ns1:Resources.ns1:Resource("
								+ i + ").@ResourceName=" + r.getResourceName());
				url += (r.getResourceType() == null ? ""
						: "&ns1:policy.ns1:Target.ns1:Resources.ns1:Resource("
								+ i + ").@ResourceType=" + r.getResourceType());
				url += (r.getDescription() == null ? ""
						: "&ns1:policy.ns1:Target.ns1:Resources.ns1:Resource("
								+ i + ").@Description=" + r.getDescription());

				if (r.getOpList() != null) {
					int j = 0;
					for (Operation op : r.getOpList()) {
						url += (op.getOperationName() == null ? ""
								: "&ns1:policy.ns1:Target.ns1:Resources.ns1:Resource("
										+ i
										+ ").ns1:Operation("
										+ j
										+ ").@OperationName="
										+ op.getOperationName());
						j++;
					}
				}
				i++;
			}
		}

		url += createSubjectsTarget(policy.getSubjects(),
				policy.getExclusionSubjects());

		url += createSubjectGroupsTarget(policy.getSubjectGroups(),
				policy.getExclusionSG());

		return url;

	}

	private static String createSubjectGroupsTarget(
			List<SubjectGroup> inclusionSubjectGroups,
			List<SubjectGroup> exclusionSubjectGroups) {
		String url = "";
		int i = 0;
		// inclusion subjecs groups
		if (inclusionSubjectGroups != null) {
			for (SubjectGroup sg : inclusionSubjectGroups) {
				url += (sg.getName() == null ? ""
						: "&ns1:policy.ns1:Target.ns1:Subjects.ns1:SubjectGroup("
								+ i + ").@SubjectGroupName=" + sg.getName());
				url += (sg.getType() == null ? ""
						: "&ns1:policy.ns1:Target.ns1:Subjects.ns1:SubjectGroup("
								+ i
								+ ").@SubjectType="
								+ sg.getType().toString());
				if (sg.getSubjectMatchTypes() != null
						&& sg.getSubjectMatchTypes().size() > 0) {
					int j = 0;
					for (SubjectMatchType smt : sg.getSubjectMatchTypes()) {
						url += "&ns1:policy.ns1:Target.ns1:Subjects.ns1:SubjectGroup("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").@MatchId="
								+ smt.getMatchId();
						url += "&ns1:policy.ns1:Target.ns1:Subjects.ns1:SubjectGroup("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").ns2:AttributeValue="
								+ smt.getAttributeValue().getValue();

						url += "&ns1:policy.ns1:Target.ns1:Subjects.ns1:SubjectGroup("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").ns2:SubjectAttributeDesignator.@AttributeId="
								+ smt.getSubjectAttributeDesignator()
										.getAttributeId();
					}
				}
				i++;
			}
		}
		// exclusion subjects groups
		if (exclusionSubjectGroups != null) {

			for (SubjectGroup sg : exclusionSubjectGroups) {
				url += (sg.getName() == null ? ""
						: "&ns1:policy.ns1:Target.ns1:Subjects.ns1:SubjectGroup("
								+ i + ").@SubjectGroupName=" + sg.getName());
				url += (sg.getType() == null ? ""
						: "&ns1:policy.ns1:Target.ns1:Subjects.ns1:SubjectGroup("
								+ i
								+ ").@SubjectType="
								+ sg.getType().toString());
				if (sg.getSubjectMatchTypes() != null
						&& sg.getSubjectMatchTypes().size() > 0) {
					int j = 0;
					for (SubjectMatchType smt : sg.getSubjectMatchTypes()) {
						url += "&ns1:policy.ns1:Target.ns1:Subjects.ns1:SubjectGroup("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").@MatchId="
								+ smt.getMatchId();
						url += "&ns1:policy.ns1:Target.ns1:Subjects.ns1:SubjectGroup("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").ns2:AttributeValue="
								+ smt.getAttributeValue().getValue();

						url += "&ns1:policy.ns1:Target.ns1:Subjects.ns1:SubjectGroup("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").ns2:SubjectAttributeDesignator.@AttributeId="
								+ smt.getSubjectAttributeDesignator()
										.getAttributeId();
					}
				}

				i++;
			}
		}
		return url;
	}

	private static String createSubjectsTarget(List<Subject> inclusionSubjects,
			List<Subject> exclusionSubjects) {
		String url = "";
		int i = 0;
		// inclusion Subjects
		if (inclusionSubjects != null) {
			for (Subject s : inclusionSubjects) {
				url += (s.getName() == null ? ""
						: "&ns1:policy.ns1:Target.ns1:Subjects.ns1:Subject("
								+ i + ").@SubjectName=" + s.getName());
				url += (s.getType() == null ? ""
						: "&ns1:policy.ns1:Target.ns1:Subjects.ns1:Subject("
								+ i + ").@SubjectType="
								+ s.getType().toString());
				if (s.getSubjectMatchTypes() != null
						&& s.getSubjectMatchTypes().size() > 0) {
					int j = 0;
					for (SubjectMatchType smt : s.getSubjectMatchTypes()) {
						url += "&ns1:policy.ns1:Target.ns1:Subjects.ns1:Subject("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").@MatchId="
								+ smt.getMatchId();
						url += "&ns1:policy.ns1:Target.ns1:Subjects.ns1:Subject("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").ns2:AttributeValue="
								+ smt.getAttributeValue().getValue();
						url += "&ns1:policy.ns1:Target.ns1:Subjects.ns1:Subject("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").ns2:SubjectAttributeDesignator.@AttributeId="
								+ smt.getSubjectAttributeDesignator()
										.getAttributeId();
					}
				}
				i++;
			}
		}

		// exclusion Subjects
		if (exclusionSubjects != null) {

			for (Subject s : exclusionSubjects) {
				url += (s.getName() == null ? ""
						: "&ns1:policy.ns1:Target.ns1:Subjects.ns1:Subject("
								+ i + ").@SubjectName=" + s.getName());
				url += (s.getType() == null ? ""
						: "&ns1:policy.ns1:Target.ns1:Subjects.ns1:Subject("
								+ i + ").@SubjectType="
								+ s.getType().toString());
				if (s.getSubjectMatchTypes() != null
						&& s.getSubjectMatchTypes().size() > 0) {
					int j = 0;
					for (SubjectMatchType smt : s.getSubjectMatchTypes()) {
						url += "&ns1:policy.ns1:Target.ns1:Subjects.ns1:Subject("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").@MatchId="
								+ smt.getMatchId();
						url += "&ns1:policy.ns1:Target.ns1:Subjects.ns1:Subject("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").ns2:AttributeValue="
								+ smt.getAttributeValue().getValue();
						url += "&ns1:policy.ns1:Target.ns1:Subjects.ns1:Subject("
								+ i
								+ ").ns2:SubjectMatch("
								+ j
								+ ").ns2:SubjectAttributeDesignator.@AttributeId="
								+ smt.getSubjectAttributeDesignator()
										.getAttributeId();
					}
				}
				i++;
			}
		}
		return url;
	}

	public static String toJSON(GenericPolicy policy) {
		String json = "";
		if (policy == null) {
			return json;
		}

		json += "           \"ns1.policy\": {";
		json += (policy.getId() == null ? "" : "\"@PolicyId\":\""
				+ policy.getId().toString() + "\",");

		// type, name are mandatory
		json += "\"@PolicyType\":\""
						+ policy.getType().toString().toUpperCase() + "\"";
		json += ",\"@PolicyName\":\"" + policy.getName() + "\"";
		// description & status are optional
		json += ",\"ns2.Description\":\"" + policy.getDescription() + "\"";

		json += createRuleJSON(policy);
		
		if (policy.getResources() != null) {
			json += ",\"ns1.Target\": { ";
			json += "\"ns1.Resources\": {";
			

			for (int i = 0; i < policy.getResources().size(); i++) {
				json += "\"ns1.Resource\": [";
				Resource rs = policy.getResources().get(i);
				json += "{\"@ResourceName\": \"" + rs.getResourceName() + "\"";
				json += ",\"@ResourceType\": \""
						+ rs.getResourceType().toString() + "\"";
				json += ",\"@Description\": \"" + rs.getDescription() + "\",";

				if (rs.getOpList() != null) {
					json += "\"ns1.Operation\": [";

					for (int j = 0; j < rs.getOpList().size(); j++) {
						Operation op = rs.getOpList().get(j);
						json += "{\"@OperationName\": \""
								+ op.getOperationName() + "\"}";

						if (j < rs.getOpList().size() - 1) {
							json += ",";
						}
					}
					json += "]";
				}

				json += "}";

				if (i < policy.getResources().size() - 1)
					json += ",";
				
				json += "]";
			}
			
		}
		json += "}";

		json += ",\"ns1.Subjects\": {";

		json += createSubjectsTargetJSON(policy);

		json += "}";
		json += "}";
		json += "}";

		return json;
	}

	private static String createRuleJSON(GenericPolicy policy) {
		String json = "";
		// rule is optional for RateLimiting policies
		if (policy.getRules() != null) {
			json += ",\"ns1.Rule\": [";
			for (int i = 0; i < policy.getRules().size(); i++) {
				Rule rule = policy.getRules().get(i);

				json += "{\"@Effect\": \"" + rule.getEffect() + "\",";
				json += "\"@RuleName\": \""	+ rule.getRuleName()
						+ String.valueOf(Math.random()).substring(2, 5) + "\",";
				json += "\"@Priority\": \"" + rule.getPriority() + "\",";
				json += "\"@RolloverPeriod\": \"" + rule.getRolloverPeriod() + "\",";
				json += "\"@EffectDuration\": \"" + rule.getEffectDuration() + "\",";
				json +=  "\"@ConditionDuration\": \"" + rule.getConditionDuration() + "\",";
				
				if (rule.getCondition() != null) {
					Condition condition = rule.getCondition();
					if (condition.getExpression() != null) {
						Expression expression = condition.getExpression();
						json += "\"ns1.Condition\": { ";
						json += "\"ns1.Expression\": { ";
						json +=  (expression.getName() == null || expression.getName().equals("")) ? ""
								:"\"@name\": \"" + expression.getName()
										+ "\",";
						if (expression.getPrimitiveValue() != null) {
							PrimitiveValue primitiveValue = expression
									.getPrimitiveValue();
							json += "\"ns1.PrimitiveValue\": { ";
							json += "\"@type\":\"" + primitiveValue.getType()
											+ "\",";
							json += "\"@value\":\"" + primitiveValue.getValue()
											+ "\"";
							json += "}";
						}
					}
					json += "}";
					json += "}";

				}

				if (rule.getAttributeList() != null
						&& !rule.getAttributeList().isEmpty()) {
					List<RuleAttribute> attributeList = rule.getAttributeList();
					if (attributeList.size() > 0) {
						json += ",\"ns1.Attribute\": [";
						for (int j = 0; j < attributeList.size(); j++) {
							json += attributeList.get(j).getKey() == null ? ""
									: "{\"ns1.key\": \""
											+ attributeList.get(j).getKey()
											+ "\",";
							json += attributeList.get(j).getValue() == null ? ""
									: "\"ns1.value\": \""
											+ attributeList.get(j).getValue()
											+ "\"}";

							if (j < attributeList.size() - 1) {
								json += ",";
							}
						}
						json += "]";
					}

				}
				json += "}";
				if (i < policy.getRules().size() - 1) {
					json += ",";
				}

			}
			json += "]";

		}

		return json;
	}

	private static String createSubjectsTargetJSON(GenericPolicy policy) {
		List<Subject> inclusionSubjects = policy.getSubjects();
		List<Subject> exclusionSubjects = policy.getExclusionSubjects();
		List<SubjectGroup> inclusionSubjectGroup = policy.getSubjectGroups();
		List<SubjectGroup> exclusionSubjectGroup = policy.getExclusionSG();

		String json = "";
		boolean needComma = false;
		// inclusion Subjects
		if (inclusionSubjects != null && inclusionSubjects.size() > 0) {

			for (int i = 0; i < inclusionSubjects.size(); i++) {
				json += "\"ns1.Subject\": [";

				Subject s = inclusionSubjects.get(i);
				json += "{\"@SubjectName\": \"" + s.getName() + "\"";
				json += ",\"@SubjectType\": \"" + s.getType().toString()
						+ "\",";

				if (s.getSubjectMatchTypes() != null
						&& s.getSubjectMatchTypes().size() > 0) {
					json += "\"ns2.SubjectMatch\": [";

					for (int j = 0; j < s.getSubjectMatchTypes().size(); j++) {
						SubjectMatchType smt = s.getSubjectMatchTypes().get(j);
						json += "{\"@MatchId\": \"" + smt.getMatchId() + "\",";
						json += "\"ns2.AttributeValue\":";
						json += "{\"__value__\": \""
								+ smt.getAttributeValue().getValue() + "\"},";
						json += "\"ns2.SubjectAttributeDesignator\": ";
						json += "{\"@AttributeId\": \""
								+ smt.getSubjectAttributeDesignator()
										.getAttributeId() + "\"}";

						json += "}";

						if (j < s.getSubjectMatchTypes().size() - 1) {
							json += ",";
						}
					}

					json += "]";

				}

				json += "}";

				if (i < inclusionSubjects.size() - 1) {
					json += ",";
				}
				json += "]";
			}

			
			needComma = true;
		}

		// exclusion Subjects
		if (exclusionSubjects != null && exclusionSubjects.size() > 0) {
			if (needComma) {
				json += ",";
			}

			json += "\"ns1.Subject\": [";

			for (int i = 0; i < exclusionSubjects.size(); i++) {
				Subject s = exclusionSubjects.get(i);
				json += "{\"@SubjectName\": \"" + s.getName() + "\"";
				json += ",\"@SubjectType\": \"" + s.getType().toString() + "\"";

				if (s.getSubjectMatchTypes() != null
						&& s.getSubjectMatchTypes().size() > 0) {
					json += "\"ns2.SubjectMatch\": [";

					for (int j = 0; j < s.getSubjectMatchTypes().size(); j++) {
						SubjectMatchType smt = s.getSubjectMatchTypes().get(j);
						json += "{\"@MatchId\": \"" + smt.getMatchId() + "\",";
						json += "\"ns2.AttributeValue\":";
						json += "{\"__value__\": \""
								+ smt.getAttributeValue().getValue() + "\"},";
						json += "\"ns2.SubjectAttributeDesignator\": ";
						json += "{\"@AttributeId\": \""
								+ smt.getSubjectAttributeDesignator()
										.getAttributeId() + "\"}";
						json += "}";

						if (j < s.getSubjectMatchTypes().size() - 1) {
							json += ",";
						}

					}
					json += "]";
				}

				json += "}";

				if (i < exclusionSubjects.size() - 1) {
					json += ",";
				}
			}

			json += "]";
			needComma = true;

		}

		// inclusionSubjectGroup
		if (inclusionSubjectGroup != null && inclusionSubjectGroup.size() > 0) {
			if (needComma) {
				json += ",";
			}

			json += "\"ns1.SubjectGroup\": [";

			for (int i = 0; i < inclusionSubjectGroup.size(); i++) {
				SubjectGroup sg = inclusionSubjectGroup.get(i);
				json += "{\"@SubjectGroupName\": \"" + sg.getName() + "\"";
				json += ",\"@SubjectType\": \"" + sg.getType().toString()
						+ "\",";

				if (sg.getSubjectMatchTypes() != null
						&& sg.getSubjectMatchTypes().size() > 0) {
					json += "\"ns2.SubjectMatch\": [";

					for (int j = 0; j < sg.getSubjectMatchTypes().size(); j++) {
						SubjectMatchType smt = sg.getSubjectMatchTypes().get(j);
						json += "{\"@MatchId\": \"" + smt.getMatchId() + "\",";
						json += "\"ns2.AttributeValue\":";
						json += "{\"__value__\": \""
								+ smt.getAttributeValue().getValue() + "\"},";
						json += "\"ns2.SubjectAttributeDesignator\": ";
						json += "{\"@AttributeId\": \""
								+ smt.getSubjectAttributeDesignator()
										.getAttributeId() + "\"}";
						json += "}";

						if (j < sg.getSubjectMatchTypes().size() - 1) {
							json += ",";
						}
					}
					json += "]";

				}

				json += "}";

				if (i < inclusionSubjectGroup.size() - 1) {
					json += ",";
				}
			}

			json += "]";
			needComma = true;
		}

		// exclusion Subjects
		if (exclusionSubjectGroup != null && exclusionSubjectGroup.size() > 0) {
			if (needComma) {
				json += ",";
			}
			json += "\"ns1.SubjectGroup\": [";

			for (int i = 0; i < exclusionSubjectGroup.size(); i++) {
				SubjectGroup sg = exclusionSubjectGroup.get(i);
				json += "{\"@SubjectGroupName\": \"" + sg.getName() + "\"";
				json += ",\"@SubjectType\": \"" + sg.getType().toString()
						+ "\",";

				if (sg.getSubjectMatchTypes() != null
						&& sg.getSubjectMatchTypes().size() > 0) {
					json += "\"ns2.SubjectMatch\": [";

					for (int j = 0; j < sg.getSubjectMatchTypes().size(); j++) {
						SubjectMatchType smt = sg.getSubjectMatchTypes().get(j);
						json += "{\"@MatchId\": \"" + smt.getMatchId() + "\",";
						json += "\"ns2.AttributeValue\":";
						json += "{\"__value__\": \""
								+ smt.getAttributeValue().getValue() + "\"},";
						json += "\"ns2.SubjectAttributeDesignator\": ";
						json += "{\"@AttributeId\": \""
								+ smt.getSubjectAttributeDesignator()
										.getAttributeId() + "\"}";
						json += "}";

						if (j < sg.getSubjectMatchTypes().size() - 1) {
							json += ",";
						}
					}
					json += "]";
				}

				json += "}";

				if (i < exclusionSubjectGroup.size() - 1) {
					json += ",";
				}
			}

			json += "]";
		}

		return json;

	}

}
