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
 * SubjectsConverter
 * @author jose
 */
public class SubjectsConverter {
    
	public static String toNV(final List<Subject> subjects) {
        String url = "";
        if (subjects== null || subjects.size() == 0){
            return url;
        }

        int i = 0;
        for (Subject sub : subjects) {
			url += (sub.getName() == null ? "" : "&ns1:subjects(" + i
					+ ").@SubjectName=" + sub.getName().trim());
			url += (sub.getType() == null ? "" : "&ns1:subjects(" + i
					+ ").@SubjectType=" + sub.getType().toString());
			url += (sub.getExternalSubjectId() == 0 ? "" : "&ns1:subjects("
					+ i + ").@ExternalSubjectId=" + sub.getExternalSubjectId());
			i++;
        }
		
		

        return url;
	 }
        
	
	
	
	
    
    public String toJSON (final List<Subject> subjects) {
        String json = "";
        if (subjects == null)
            return json;
        
        //TODO
        
        return json;
    }

}
