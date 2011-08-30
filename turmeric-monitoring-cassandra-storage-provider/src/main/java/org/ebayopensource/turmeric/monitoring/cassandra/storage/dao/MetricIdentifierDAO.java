package org.ebayopensource.turmeric.monitoring.cassandra.storage.dao;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.MetricIdentifier;
import org.ebayopensource.turmeric.utils.cassandra.dao.AbstractColumnFamilyDao;

public class MetricIdentifierDAO extends AbstractColumnFamilyDao<String, MetricIdentifier> {

    public MetricIdentifierDAO(String clusterName, String host, String s_keyspace, Class<String> keyTypeClass,
                    Class<MetricIdentifier> persistentClass, String columnFamilyName) {
        super(clusterName, host, s_keyspace, keyTypeClass, persistentClass, columnFamilyName);
    }

}
