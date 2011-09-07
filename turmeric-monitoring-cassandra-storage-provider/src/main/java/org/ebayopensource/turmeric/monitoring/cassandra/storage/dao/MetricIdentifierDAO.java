/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.cassandra.storage.dao;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.MetricIdentifier;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

public class MetricIdentifierDAO extends AbstractColumnFamilyDao<String, MetricIdentifier> {

    public MetricIdentifierDAO(String clusterName, String host, String s_keyspace, Class<String> keyTypeClass,
                    Class<MetricIdentifier> persistentClass, String columnFamilyName) {
        super(clusterName, host, s_keyspace, keyTypeClass, persistentClass, columnFamilyName);
    }

}
