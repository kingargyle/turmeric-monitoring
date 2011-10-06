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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.cassandra.storage.provider.CassandraMetricsStorageProvider;

import org.ebayopensource.turmeric.monitoring.provider.dao.MetricsErrorByIdDAO;
import org.ebayopensource.turmeric.monitoring.provider.dao.impl.MetricsErrorByIdDAOImpl;
import org.ebayopensource.turmeric.monitoring.provider.manager.cassandra.server.CassandraTestManager;
import org.ebayopensource.turmeric.monitoring.provider.model.ExtendedErrorViewData;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorInfos;
import org.ebayopensource.turmeric.monitoring.v1.services.ErrorViewData;
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

    private List<CommonErrorData> errorsToStore = null;
    private final String serverName = "All";
    private final boolean serverSide = true;
    private final String srvcAdminName = "ServiceAdminName1";
    private final String opName = "Operation1";
    private final String consumerName = "ConsumerName1";
    
    private long now = System.currentTimeMillis();
    private long oneSecond = TimeUnit.SECONDS.toMillis(1);
    private long fiftyNineSeconds = TimeUnit.SECONDS.toMillis(59);
    private long oneMinuteAgo = now -  TimeUnit.SECONDS.toMillis(60);
    private long twoMinutesAgo = now - TimeUnit.SECONDS.toMillis(60 * 2);
    private long sixMinuteAgo = now -  TimeUnit.SECONDS.toMillis(60 * 6);


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();


        errorStorageProvider = new CassandraErrorLoggingHandler();
        metricsStorageProvider = new CassandraMetricsStorageProvider();

        InitContext ctx = new MockInitContext(options);
        errorStorageProvider.init(ctx);
        metricsStorageProvider.init(options, null, MonitoringSystem.COLLECTION_LOCATION_SERVER, 20);
        queryprovider = new SOAMetricsQueryServiceCassandraProviderImpl();

        cleanUpTestData();
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
    private void createData() {
        errorsToStore = createTestCommonErrorDataList(3);
    }   
    
    
    @Test
    public void testErrorMetricsMetadata() throws ServiceException {
    	createData();
    	
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
    
//    @Test
//    public void testExtendedErrorMetricsData() throws ServiceException {
//    	createData();
//    	errorStorageProvider.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName,
//    			oneMinuteAgo + fiftyNineSeconds);
//    	
//    	long duration = 120;// in secs
//		//according DAOErrorLoggingHandler.persistErrors aggregation period should always be 0
//    	int aggregationPeriod = 0;// in secs
//        MetricCriteria metricCriteria = new MetricCriteria();
//        metricCriteria.setFirstStartTime(oneMinuteAgo);
//        metricCriteria.setDuration(duration);
//        metricCriteria.setAggregationPeriod(aggregationPeriod);
//        metricCriteria.setRoleType("server");
//   	
//		List<ExtendedErrorViewData> extendedErrorMetricsData = queryprovider.getExtendedErrorMetricsData("Category", Arrays.asList( srvcAdminName), Arrays.asList( opName), Arrays.asList( consumerName), null, ErrorCategory.APPLICATION.value(), null, null, metricCriteria);
//		assertNotNull(extendedErrorMetricsData);
//    }
    
    @Test
    public void testGetErrorMetricsDataByCategory() throws ServiceException {
		createData();
    	errorStorageProvider.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName,
    			oneMinuteAgo + fiftyNineSeconds);
    	
		MetricCriteria metricCriteria = new MetricCriteria();
		metricCriteria.setFirstStartTime(twoMinutesAgo);
		metricCriteria.setSecondStartTime(oneMinuteAgo);
		metricCriteria.setDuration(3600);

		List<ErrorViewData> errorData = queryprovider.getErrorMetricsData("Category", Collections.<String> emptyList(),
				Collections.<String> emptyList(), Collections.<String> emptyList(), null, null, null, null,
				metricCriteria);
		assertEquals(0, errorData.size());

    }
    
    
    
    @Test
    public void testGetErrorGraphOneMinuteAgoOtherCategory() throws ServiceException {
    	createData();
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
				ErrorCategory.REQUEST.value(), null, metricCriteria);

		assertNotNull(result);
		 assertEquals(duration / aggregationPeriod, result.size());
        assertEquals(0L, result.get(0).getCount(), 0.0d);
        assertEquals(0L, result.get(1).getCount(), 0.0d);		
    }
    
    @Test
    public void testGetErrorGraphOneMinuteAgoCategory() throws ServiceException {
    	createData();
    	errorStorageProvider.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName,
    			oneMinuteAgo + oneSecond);
        
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
        assertEquals(1L, result.get(0).getCount(), 0.0d);
        assertEquals(0L, result.get(1).getCount(), 0.0d);		
    }
    
    @Test
    public void testGetErrorGraphOneMinuteAgoSeverity() throws ServiceException {
    	createData();
    	errorStorageProvider.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName,
    			now - fiftyNineSeconds);
        
    	 long duration = 120;// in secs
         int aggregationPeriod = 60;// in secs
         MetricCriteria metricCriteria = new MetricCriteria();
         metricCriteria.setFirstStartTime(oneMinuteAgo);
         metricCriteria.setDuration(duration);
         metricCriteria.setAggregationPeriod(aggregationPeriod);
         metricCriteria.setRoleType("server");
    	
		List<MetricGraphData> result = queryprovider.getErrorGraph(srvcAdminName, opName, consumerName, null,
				null, ErrorSeverity.ERROR.value(), metricCriteria);

		assertNotNull(result);
		 assertEquals(duration / aggregationPeriod, result.size());
        assertEquals(1L, result.get(0).getCount(), 0.0d);
        assertEquals(0L, result.get(1).getCount(), 0.0d);		
    }
    
    @Test
    public void testGetErrorGraphSixMinuteAgoSeverity() throws ServiceException {
    	createData();
    	errorStorageProvider.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName,
    			sixMinuteAgo + oneSecond);
        
    	 long duration = 120;// in secs
         int aggregationPeriod = 60;// in secs
         MetricCriteria metricCriteria = new MetricCriteria();
         metricCriteria.setFirstStartTime(oneMinuteAgo);
         metricCriteria.setDuration(duration);
         metricCriteria.setAggregationPeriod(aggregationPeriod);
         metricCriteria.setRoleType("server");
    	
		List<MetricGraphData> result = queryprovider.getErrorGraph(srvcAdminName, opName, consumerName, null,
				null, ErrorSeverity.ERROR.value(), metricCriteria);

		assertNotNull(result);
		 assertEquals(duration / aggregationPeriod, result.size());
        assertEquals(0L, result.get(0).getCount(), 0.0d);
        assertEquals(0L, result.get(1).getCount(), 0.0d);		
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
