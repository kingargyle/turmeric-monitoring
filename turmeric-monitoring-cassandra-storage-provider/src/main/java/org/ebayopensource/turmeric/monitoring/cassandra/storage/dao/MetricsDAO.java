package org.ebayopensource.turmeric.monitoring.cassandra.storage.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.ebayopensource.turmeric.monitoring.cassandra.storage.model.MetricIdentifier;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorManager;

public class MetricsDAO {

    private String clusterName;
    private String host;
    private Keyspace keySpace;

    public MetricsDAO(String clusterName, String host, String keyspaceName) {
        this.clusterName = clusterName;
        this.host = host;
        this.keySpace = new HectorManager().getKeyspace(clusterName, host, keyspaceName, "ServiceOperationByIp");
    }

    public void saveServiceOperationByIpCF(String ipAddress, MetricIdentifier cmetricIdentifier) {
        String superColumnName = cmetricIdentifier.getServiceAdminName();

        Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());

        List<HColumn<String, String>> columnList = Arrays.asList(HFactory.createStringColumn(
                        cmetricIdentifier.getOperationName(), ""));

        mutator.insert(ipAddress, "ServiceOperationByIp", HFactory.createSuperColumn(superColumnName, columnList,
                        StringSerializer.get(), StringSerializer.get(), StringSerializer.get()));

        // now I store a row with "All" as key, for when the searches requires to list all the services in all the nodes
        mutator.insert("All", "ServiceOperationByIp", HFactory.createSuperColumn(superColumnName, columnList,
                        StringSerializer.get(), StringSerializer.get(), StringSerializer.get()));

        mutator.execute();
    }

    public void saveServiceConsumerByIpCF(String ipAddress, MetricIdentifier cmetricIdentifier, String consumerName) {
        String superColumnName = cmetricIdentifier.getServiceAdminName();

        Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());

        List<HColumn<String, String>> columnList = Arrays.asList(HFactory.createStringColumn(consumerName, ""));

        mutator.insert(ipAddress, "ServiceConsumerByIp", HFactory.createSuperColumn(superColumnName, columnList,
                        StringSerializer.get(), StringSerializer.get(), StringSerializer.get()));

        // now I store a row with "All" as key, for when the searches requires to list all the services in all the nodes
        mutator.insert("All", "ServiceConsumerByIp", HFactory.createSuperColumn(superColumnName, columnList,
                        StringSerializer.get(), StringSerializer.get(), StringSerializer.get()));

        mutator.execute();
    }
}
