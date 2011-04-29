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
 * OperationKeyConverter.
 */
public class OperationKeyConverter {

    /**
     * To nv.
     *
     * @param opKeys the op keys
     * @return the string
     */
    public static String toNV (List<OperationKey> opKeys) {
        String url = "";
        if (opKeys != null && opKeys.size() > 0) {
            int i=0;
            for (OperationKey opKey:opKeys) {
                url += toNV(opKey, i);
                i++;
            }
        }
        return url;
    }
    
    /**
     * To nv.
     *
     * @param opKey the op key
     * @return the string
     */
    public static String toNV (OperationKey opKey) {
        return toNV(opKey, 0);
    }

    private static String toNV (OperationKey opKey, int i) {
        String url = "";
        if (opKey != null) {
            url += (opKey.getOperationId() == null? "":"&ns1:operationKey("+i+").ns1:operationId="+opKey.getOperationId());
            url += (opKey.getOperationName() == null || "".equals(opKey.getOperationName().trim())? "":"&ns1:operationKey("+i+").ns1:operationName="+opKey.getOperationName());
            url += (opKey.getResourceName() == null || "".equals(opKey.getResourceName().trim())? "":"&ns1:operationKey("+i+").ns1:resourceName="+opKey.getResourceName());
            url += (opKey.getResourceType() == null || "".equals(opKey.getResourceType().trim())? "":"&ns1:operationKey("+i+").ns1:resourceType="+opKey.getResourceType());
        }
        return url;
    } 
}
