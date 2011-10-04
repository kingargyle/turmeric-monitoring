/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;

import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorByIdDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorByIdDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.manager.cassandra.server.CassandraTestManager;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorInfos;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGraphData;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class SOAMetricsQueryServiceCassandraProviderTest.
 * 
 * @author jamuguerza
 */
public class SOAMetricsQueryServiceCassandraProviderTest extends BaseTest {

   
    private ErrorById errorToSave = null;
    private ErrorValue errorValue = null;
    private List<CommonErrorData> errorsToStore = null;
    private final String serverName = "localhost";
    private final boolean serverSide = true;
    private final String srvcAdminName = "ServiceAdminName1";
    private final String opName = "Operation1";
    private final String consumerName = "ConsumerName1";
    
    private long now = System.currentTimeMillis();
    private long oneMinuteAgo = now -  TimeUnit.SECONDS.toMillis(60);

    private MetricsErrorByIdDAO<Long> metricsErrorByIdDAOImpl;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        metricsErrorByIdDAOImpl = new MetricsErrorByIdDAOImpl<Long>(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE,
                        "ErrorsById", Long.class);

        errorStorageProvider = new CassandraErrorLoggingHandler();
        metricsStorageProvider = new CassandraMetricsStorageProvider();

        InitContext ctx = new MockInitContext(options);
        errorStorageProvider.init(ctx);
        metricsStorageProvider.init(options, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
        queryprovider = new SOAMetricsQueryServiceCassandraProviderImpl();
        

    }

    @Override
    @After
    public void tearDown() {
        super.tearDown();
    }

    /**
     * Creates the data.
     * @param time 
     */
    private void createData(long time) {
        
        errorToSave = new ErrorById();
        errorToSave.setCategory(ErrorCategory.REQUEST.toString());
        errorToSave.setSeverity(ErrorSeverity.ERROR.toString());
        errorToSave.setDomain("TestDomain");
        errorToSave.setErrorId(Long.valueOf(123));
        errorToSave.setName("TestError1");
        errorToSave.setOrganization("TestOrg1");
        errorToSave.setSubDomain("TestSubDomain");

        errorValue = new ErrorValue();
        errorValue.setErrorId(Long.valueOf(123));
        errorValue.setConsumerName("theTestConsumer");
        errorValue.setErrorMessage("The actual message");
        errorValue.setOperationName("Op1");
        errorValue.setServerName("TheServerName");
        errorValue.setServerSide(true);
        errorValue.setServiceAdminName("TheServiceAdminName");
        errorValue.setTimeStamp(time);

        errorsToStore = createTestCommonErrorDataList(3);
        }

 
   
    
    
    @Test
    public void testErrorMetricsMetadata() throws ServiceException {
    	createData(now);
    	
        errorStorageProvider.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName,
                        now);
        
		ErrorInfos errorMetricsMetadata = queryprovider.getErrorMetricsMetadata("1", "TestErrorName",
                        "ServiceAdminName1");
        
         assertNotNull(errorMetricsMetadata);
         assertEquals(ErrorCategory.APPLICATION.name(),
         errorMetricsMetadata.getCategory());
         assertEquals(ErrorSeverity.ERROR.name(),
         errorMetricsMetadata.getSeverity());
         assertEquals("TestDomain", errorMetricsMetadata.getDomain());
         assertEquals("1", errorMetricsMetadata.getId());
         assertEquals("TestErrorName", errorMetricsMetadata.getName());
         assertEquals("TestSubdomain", errorMetricsMetadata.getSubDomain());

    }
    
    //@Test
    public void testGetErrorGraphOneMinuteAgo() throws ServiceException {
    	createData(oneMinuteAgo);
    	errorStorageProvider.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName,
                now);
        
    	 long duration = 120;// in secs
         int aggregationPeriod = 60;// in secs
         MetricCriteria metricCriteria = new MetricCriteria();
         metricCriteria.setFirstStartTime(oneMinuteAgo);
         metricCriteria.setDuration(duration);
         metricCriteria.setAggregationPeriod(aggregationPeriod);
         metricCriteria.setRoleType("server");
    	
		List<MetricGraphData> result = queryprovider.getErrorGraph(srvcAdminName, opName, consumerName, null,
				ErrorCategory.APPLICATION.value(), null, metricCriteria);

		assertNotNull(result);
		 assertEquals(duration / aggregationPeriod, result.size());
        // there must be duration/aggregationPeriod elements. The first
        assertEquals(duration / aggregationPeriod, result.size());
        assertEquals(1L, result.get(0).getCount(), 0.0d);
        assertEquals(1L, result.get(1).getCount(), 0.0d);
		
    }
    
    private List<CommonErrorData> createTestCommonErrorDataList(int errorQuantity) {
        List<CommonErrorData> commonErrorDataList = new ArrayList<CommonErrorData>();
        for (int i = 0; i < errorQuantity; i++) {
            CommonErrorData e = new CommonErrorData();
            e.setCategory(ErrorCategory.APPLICATION);
            e.setSeverity(ErrorSeverity.ERROR);
            e.setCause("TestCause");
            e.setDomain("TestDomain");
            e.setSubdomain("TestSubdomain");
            e.setErrorName("TestErrorName");
            e.setErrorId(Long.valueOf(i));
            e.setMessage("Error Message " + i);
            e.setOrganization("TestOrganization");
            commonErrorDataList.add(e);
        }
        return commonErrorDataList;

    }
    
  

}
