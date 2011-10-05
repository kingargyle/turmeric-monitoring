package org.ebayopensource.turmeric.monitoring.aggregation.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;
import me.prettyprint.hector.api.query.SuperColumnQuery;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.utils.cassandra.service.CassandraManager;

public class CassandraTestHelper {
	protected static final ObjectSerializer OBJ_SERIALIZER = ObjectSerializer.get();
    protected static final LongSerializer LONG_SERIALIZER = LongSerializer.get();
    protected static final StringSerializer STR_SERIALIZER = StringSerializer.get();
    protected static final String cluster_name = "TestCluster";
    protected static final String keyspace_name = "TurmericMonitoring";
    protected static final String cassandra_node_ip = "127.0.0.1";
    public Keyspace kspace = null;

    public void assertValues(ColumnSlice<Object, Object> columnSlice, Object... columnPairs) {

        // the asserts are done in this way: assert(columnPairs[0],
        // columnPairs[1]);, assert(columnPairs[2],
        // columnPairs[3]), ...;
        for (int i = 0; i < columnPairs.length / 2; i++) {
            HColumn<Object, Object> column = columnSlice.getColumnByName(columnPairs[2 * i]);
            assertNotNull("Null column name =" + columnPairs[2 * i], column);
            Object value = column.getValue();
            assertEquals("Expected = " + columnPairs[2 * i + 1] + ". Actual = " + value, columnPairs[2 * i + 1], value);
        }
    }

    public void assertValues(ColumnSlice<Object, Object> columnSlice, Object[] columnNames, Object[] columnValues) {

        // the asserts are done in this way: assert(columnPairs[0],
        // columnPairs[1]);, assert(columnPairs[2],
        // columnPairs[3]), ...;
        for (int i = 0; i < columnNames.length; i++) {
            HColumn<Object, Object> column = columnSlice.getColumnByName(columnNames[i]);
            assertNotNull("Null column name =" + columnNames[i], column);
            Object value = column.getValue();
            assertEquals("Expected = " + columnValues[i] + ". Actual = " + value, columnValues[i], value);
        }
    }

    public ColumnSlice<Object, Object> getColumnValues(Keyspace kspace, String cfName, Object key,
                    Serializer columnNameSerializer, Serializer valueSerializer, Object... columnNames) {

        SliceQuery<Object, Object, Object> q = HFactory.createSliceQuery(kspace,
                        SerializerTypeInferer.getSerializer(key), columnNameSerializer, valueSerializer);
        q.setColumnFamily(cfName);
        q.setKey(key);
        q.setColumnNames(columnNames);
        QueryResult<ColumnSlice<Object, Object>> r = q.execute();
        ColumnSlice<Object, Object> columnSlice = r.get();
        return columnSlice;
    }

    public List<CommonErrorData> createTestCommonErrorDataList(int errorQuantity) {
        List<CommonErrorData> commonErrorDataList = new ArrayList<CommonErrorData>();
        for (int i = 0; i < errorQuantity; i++) {
            CommonErrorData e = new CommonErrorData();
            e.setCategory(ErrorCategory.APPLICATION);
            e.setSeverity(ErrorSeverity.ERROR);
            e.setCause("TestCause");
            e.setDomain("TestDomain");
            e.setSubdomain("TestSubdomain");
            e.setErrorName("TestErrorName");
            e.setErrorId((long) i);
            e.setMessage("Error Message " + i);
            e.setOrganization("TestOrganization");
            commonErrorDataList.add(e);
        }
        return commonErrorDataList;

    }

    protected void assertColumnValueFromSuperColumn(HSuperColumn<Object, Object, Object> serviceOperationColumnSlice,
                    Object[] columnNames, Object[] columnValues) {
        if (serviceOperationColumnSlice != null && serviceOperationColumnSlice.getColumns() != null) {
            for (int i = 0; i < columnNames.length; i++) {
                for (HColumn<Object, Object> column : serviceOperationColumnSlice.getColumns()) {
                    if (column.getName().equals(columnNames[i])) {
                        assertEquals("Expected = " + columnValues[i] + ". Actual = " + column.getValue(),
                                        columnValues[i], column.getValue());
                        return;
                    }
                }
                fail("Column name " + columnNames[i] + " not found in super column");
            }
        }
        else {
            fail("Invalid supercolumn");
        }
    }

    protected HSuperColumn<Object, Object, Object> getSuperColumnValues(Keyspace kspace, String cfName, Object key,
                    Serializer superColumnNameSerializer, Serializer columnNameSerializer, Serializer valueSerializer,
                    Object superColumnName) {
        SuperColumnQuery<Object, Object, Object, Object> superColumnQuery = HFactory.createSuperColumnQuery(kspace,
                        SerializerTypeInferer.getSerializer(key), superColumnNameSerializer, columnNameSerializer,
                        valueSerializer);
        superColumnQuery.setColumnFamily(cfName).setKey(key).setSuperName(superColumnName);
        QueryResult<HSuperColumn<Object, Object, Object>> result = superColumnQuery.execute();
        return result.get();
    }

    protected void assertCassandraColumnValues(String columnFamily, Object key, Serializer columnNameSerializer,
                    Serializer valueSerializer, Object[] columnsToAssert, Object[] columnValues) {
        ColumnSlice<Object, Object> errorColumnSlice = getColumnValues(kspace, columnFamily, key, columnNameSerializer,
                        valueSerializer, columnsToAssert);
        assertValues(errorColumnSlice, columnsToAssert, columnValues);
    }

    protected void assertCassandraSuperColumnValues(String columnFamily, Object key, Object superColumnName,
                    Serializer superColumnNameSerializer, Serializer columnNameSerializer, Serializer valueSerializer,
                    Object[] columnsToAssert, Object[] columnValues) throws ServiceException {
        HSuperColumn<Object, Object, Object> serviceOperationColumnSlice = getSuperColumnValues(kspace, columnFamily,
                        key, superColumnNameSerializer, columnNameSerializer, valueSerializer, superColumnName);
        assertColumnValueFromSuperColumn(serviceOperationColumnSlice, columnsToAssert, columnValues);
    }

    public static void initialize() throws TTransportException, IOException, InterruptedException,
                    ConfigurationException {
        cleanUpCassandraDirs();
        loadConfig();
        CassandraManager.initialize();

    }

    private static void cleanUpCassandraDirs() {
        if (CassandraManager.getEmbeddedService() == null) {
            System.out.println("Cleaning cassandra dirs ? = " + deleteDir(new File("target/cassandra")));
        }
    }

    // Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }

        }
        // The directory is now empty so delete it
        return dir.delete();

    }

    /**
     * Load config.
     */
    private static void loadConfig() {
        // use particular test properties, maybe with copy method
        System.setProperty("log4j.configuration", "META-INF/config/cassandra/log4j.properties");
        System.setProperty("cassandra.config", "META-INF/config/cassandra/cassandra-test.yaml");
    }

}
