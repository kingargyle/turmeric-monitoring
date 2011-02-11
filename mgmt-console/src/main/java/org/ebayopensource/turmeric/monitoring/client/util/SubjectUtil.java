/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 *******************************************************************************/
 package org.ebayopensource.turmeric.monitoring.client.util;


import org.ebayopensource.turmeric.monitoring.client.model.policy.AttributeValue;
import org.ebayopensource.turmeric.monitoring.client.model.policy.Subject;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectAttributeDesignator;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.monitoring.client.model.policy.SubjectMatchType;

import com.google.gwt.core.client.GWT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubjectUtil {

		
//		 static void setSubjectGroupId(SubjectGroup sg, Long id)
//		{
//			SubjectMatchType subjectMatchType = null;
//			subjectMatchType= getSubjectMatchType(id);
//			sg.setSubjectMatch( subjectMatchType );
//		}
//		
//		 static void setExclusionSubjectGroupId(SubjectGroup sg, Long id)
//		{
//			SubjectMatchType subjectMatchType = null;
//			String idString = "(?!" + id + ")";
//			subjectMatchType = getSubjectMatchType(idString);
//
//			sg.setSubjectMatch( subjectMatchType );
//		}
//	
//		 static SubjectMatchType getSubjectMatchType(Long id)
//		{
//			SubjectMatchType subjectMatchType = new SubjectMatchType();
//			subjectMatchType.setMatchId( "urn:oasis:names:tc:xacml:1.0:function:integer-equal" );
//			AttributeValue attributeValueType = new AttributeValueType();
//			attributeValueType.setDataType( "http://www.w3.org/2001/XMLSchema#integer" );
//			attributeValueType.getContent().add( id.toString() );  // id is used as-is
//			subjectMatchType.setAttributeValue( attributeValueType );
//
//			SubjectAttributeDesignator subjectAttributeDesignatorType = new SubjectAttributeDesignatorImpl();
//			subjectAttributeDesignatorType.setDataType( "http://www.w3.org/2001/XMLSchema#integer" );
//			subjectAttributeDesignatorType.setAttributeId( "urn:oasis:names:tc:xacml:1.0:subject:subject-id" );
//			subjectMatchType.setSubjectAttributeDesignator( subjectAttributeDesignatorType );
//			return subjectMatchType;
//		}
//		
//		 static SubjectMatchType getSubjectMatchType(String idStr)
//		{
//			SubjectMatchType subjectMatchType = new SubjectMatchType();
//	        subjectMatchType.setMatchId( "urn:oasis:names:tc:xacml:1.0:function:string-regexp-match" );
//	        AttributeValue attributeValueType = new AttributeValueType();
//	        attributeValueType.setDataType( "http://www.w3.org/2001/XMLSchema#string" );
//	        attributeValueType.getContent().add( idStr);
//	        subjectMatchType.setAttributeValue( attributeValueType );
//
//	        SubjectAttributeDesignatorType subjectAttributeDesignatorType = new SubjectAttributeDesignatorType();
//	        subjectAttributeDesignatorType.setDataType( "http://www.w3.org/2001/XMLSchema#string" );
//	        subjectAttributeDesignatorType.setAttributeId( "urn:oasis:names:tc:xacml:1.0:subject:subject-id" );
//	        subjectMatchType.setSubjectAttributeDesignator( subjectAttributeDesignatorType );
//
//			return subjectMatchType;
//	
//		}
		
		

		public static Long getSubjectId(Subject subject)
		{
			Long id = null;
			Iterator<SubjectMatchType> iter = subject.getSubjectMatchTypes().iterator();
			while (iter.hasNext())
			{
				SubjectMatchType matchType = iter.next();
				id = getIdFromSubjectMatch(matchType);
				if (id != null)
					return id;
			}
			return id;
		}
		
		public  static Long getSubjectGroupId(SubjectGroup sg )
		{	
			 Long id = null;
				Iterator<SubjectMatchType> iter = sg.getSubjectMatchTypes().iterator();
				while (iter.hasNext())
				{
					SubjectMatchType matchType = iter.next();
					id = getIdFromSubjectMatch(matchType);
					if (id != null)
						return id;
				}
				return id;
			 
//			 List<SubjectMatchType> matchTypes = sg.getSubjectMatchTypes();
//			
//			return getIdFromSubjectMatchch(matchTypes);
		}
		
		 static Long getIdFromSubjectMatch(SubjectMatchType matchType)
		{
			Long subjectId = null;
			
			if (matchType != null && matchType.getSubjectAttributeDesignator().
						getAttributeId().equals("urn:oasis:names:tc:xacml:1.0:subject:subject-id"))
			{
				AttributeValue attributeValue = matchType.getAttributeValue();
				 
				String idString = attributeValue.getValue().toString();
	            if ("urn:oasis:names:tc:xacml:1.0:function:integer-equal".equals(matchType.getMatchId())) {
	                try {
	                    subjectId = Long.parseLong(idString);
	                } catch (Exception e) {
	                	GWT.log(e.getLocalizedMessage());
	                }
	            }

	            if ("urn:oasis:names:tc:xacml:1.0:function:string-regexp-match".equals(matchType.getMatchId())) {
	                try {
	                    subjectId = Long.parseLong(idString.substring(3, idString.length() -1));
	                } catch (Exception e) {
	                	GWT.log(e.getLocalizedMessage());
	                }
	            }
			}
				
			return subjectId;
		}
		
		 static boolean isExclusion(Subject subject)
		{
			List<SubjectMatchType> subjectMatchs = subject.getSubjectMatchTypes();
			return isExclusion(subjectMatchs);
		}
		
		 static boolean isSubjectType(Subject subject)
		{
			List<SubjectMatchType> subjectMatchs = subject.getSubjectMatchTypes();
			return isSubjectType(subjectMatchs);
		}
		
//		 static boolean isExclusion(SubjectGroup subjectGroup)
//		{
//			SubjectMatchType subjectMatchType = subjectGroup.getSubjectMatchType();
//			List<SubjectMatchType> subjectMatchs = new ArrayList<SubjectMatchType>();
//			subjectMatchs.add(subjectMatchType);
//			return isExclusion(subjectMatchs);
//		}
		
		 static boolean isExclusion(List<SubjectMatchType> matchTypes) {
			boolean isExclusion = false;
			for (SubjectMatchType matchType : matchTypes) {
				if (matchType != null && "urn:oasis:names:tc:xacml:1.0:function:string-regexp-match".equals(matchType.getMatchId())) {
					if (getIdFromSubjectMatch(matchType) != null)
						isExclusion = true;
					break;
				}
			}
			return isExclusion;
		}
		
		 static boolean isSubjectType(List<SubjectMatchType> matchTypes) {
			boolean isSubjectType = false;
			for (SubjectMatchType matchType : matchTypes) {
				if (matchType != null && "urn:oasis:names:tc:xacml:1.0:function:string-regexp-match".equals(matchType.getMatchId())) {
					if (getIdFromSubjectMatch(matchType) == null)
						isSubjectType = true;
					break;
				}
			}
			return isSubjectType;
		}
	

}
