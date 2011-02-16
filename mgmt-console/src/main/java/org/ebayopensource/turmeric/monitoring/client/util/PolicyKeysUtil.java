/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.util;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.policy.OperationKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.PolicyType;
import org.ebayopensource.turmeric.monitoring.client.model.policy.ResourceKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.ResourceType;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroupKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectKey;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectType;

public class PolicyKeysUtil {

	private static List<PolicyKey> poKeys;
	private static List<ResourceKey> rsKeys;
	private static List<OperationKey> opKeys;
	private static List<SubjectKey> sKeys;
	private static List<SubjectGroupKey> sgKeys;

	public static List<PolicyKey> getAllPolicyKeyList() {
		if (poKeys == null) {
			poKeys = new ArrayList<PolicyKey>();
			PolicyKey poKey = null;
			for (String poType : PolicyType.getValues()) {
				poKey = new PolicyKey();
				poKey.setType(poType);
				poKeys.add(poKey);
			}
		}
		return poKeys;
	}

	public static List<ResourceKey> getAllResourceKeyList() {
		if (rsKeys == null) {
			rsKeys = new ArrayList<ResourceKey>();
			ResourceKey rsKey = null;
			for (String rsType : ResourceType.getValues()) {
				rsKey = new ResourceKey();
				rsKey.setType(rsType);
				rsKeys.add(rsKey);
			}
		}
		return rsKeys;
	}

	public static List<OperationKey> getAllOperationKeyList() {
		if (opKeys == null) {
			opKeys = new ArrayList<OperationKey>();
			OperationKey opKey = null;
			for (String rsType : ResourceType.getValues()) {
				opKey = new OperationKey();
				opKey.setResourceType(rsType);
				opKeys.add(opKey);
			}
		}
		return opKeys;
	}

	public static List<SubjectKey> getAllSubjectKeyList() {
		if (sKeys == null) {
			sKeys = new ArrayList<SubjectKey>();
			SubjectKey sKey = null;
			for (String sType : SubjectType.getValues()) {
				sKey = new SubjectKey();
				sKey.setType(sType);
				sKeys.add(sKey);
			}
		}
		return sKeys;
	}

	public static List<SubjectGroupKey> getAllSubjectGroupKeyList() {
		if (sgKeys == null) {
			sgKeys = new ArrayList<SubjectGroupKey>();
			SubjectGroupKey sgKey = null;
			for (String sType : SubjectType.getValues()) {
				sgKey = new SubjectGroupKey();
				sgKey.setType(sType);
				sgKeys.add(sgKey);
			}
		}
		return sgKeys;
	}

}
