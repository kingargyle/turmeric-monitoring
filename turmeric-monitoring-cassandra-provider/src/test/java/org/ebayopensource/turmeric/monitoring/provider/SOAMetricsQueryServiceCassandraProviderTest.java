/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.v1.services.MetricGraphData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class SOAMetricsQueryServiceCassandraProviderTest.
 * @author jamuguerza
 */
public class SOAMetricsQueryServiceCassandraProviderTest {

    /** The now. */
    private long now = 0l;
    
    /** The sixty mins ago. */
    private long sixtyMinsAgo = 0l;
    
    /** The sixty one mins ago. */
    private long sixtyOneMinsAgo = 0l;
    
    /** The sixty two mins ago. */
    private long sixtyTwoMinsAgo = 0l;
    
    /** The one minute ago. */
    private long oneMinuteAgo = 0l;
    
    /** The two minutes ago. */
    private long twoMinutesAgo = 0l;
    
    /** The provider. */
    private SOAMetricsQueryServiceProvider provider;

    
    /**
     * Setup.
     *
     * @throws Exception the exception
     */
    @Before
    public void setup() throws Exception {
    	  now = System.currentTimeMillis();
          sixtyMinsAgo = now - TimeUnit.SECONDS.toMillis(3600);
          sixtyOneMinsAgo = sixtyMinsAgo - TimeUnit.SECONDS.toMillis(60);
          sixtyTwoMinsAgo = sixtyOneMinsAgo - TimeUnit.SECONDS.toMillis(60);
          oneMinuteAgo = now - TimeUnit.SECONDS.toMillis(60);
          twoMinutesAgo = oneMinuteAgo - TimeUnit.SECONDS.toMillis(60);
          createData();
//         provider = new SOAMetricsQueryServiceCassandraProviderImpl();
     
    }

    @After
	public void tearDown() {
	    provider = null;
	}


	/**
	 * Creates the data.
	 */
	private void createData() {

//		org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById error1 = new org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById();
//		error1.setErrorId(1L);
//		error1.setCategory( ErrorCategory.SYSTEM.value());
//		error1.setDomain("domain_1");
//		error1.setSubDomain("sub_domain_1");
//		error1.setOrganization("organization");
//		error1.setSeverity(ErrorSeverity.ERROR.value());
//		error1.setName("error_1");
//		error1.setTimestamp();
//		error1.setConsumerName();
//        entityManager.persist(error1);
//        ErrorValue errorValue1 = new ErrorValue(error1, "message1", "service_3", "operation_3_1", "consumer_3",
//                        oneMinuteAgo, true, 0);
//        entityManager.persist(errorValue1);
/////////////
//        org.ebayopensource.turmeric.runtime.error.cassandra.model.Error error2 = new org.ebayopensource.turmeric.runtime.error.cassandra.model.Error();
//		error2.setErrorId(2L);
//		error2.setCategory( ErrorCategory.SYSTEM.value());
//		error2.setDomain("domain_1");
//		error2.setSubDomain("sub_domain_1");
//		error2.setOrganization("organization");
//		error2.setSeverity(ErrorSeverity.ERROR.value());
//		error2.setName("error_2");
//        entityManager.persist(error2);
//        ErrorValue errorValue2 = new ErrorValue(error2, "message1", "service_3", "operation_3_1", "consumer_3",
//                        oneMinuteAgo, true, 0);
//        entityManager.persist(errorValue2);
///////////////
//        org.ebayopensource.turmeric.runtime.error.cassandra.model.Error error3 = new org.ebayopensource.turmeric.runtime.error.cassandra.model.Error();
//		error3.setErrorId(3L);
//		error3.setCategory( ErrorCategory.SYSTEM.value());
//		error3.setDomain("domain_1");
//		error3.setSubDomain("sub_domain_1");
//		error3.setOrganization("organization");
//		error3.setSeverity(ErrorSeverity.ERROR.value());
//		error3.setName("error_2");
//		
//        ErrorValue errorValue3 = new ErrorValue(error2, "message2", "service_3", "operation_3_1", "consumer_3",
//                        twoMinutesAgo, true, 0);
//        entityManager.persist(errorValue3);
//
///////////////
//        org.ebayopensource.turmeric.runtime.error.cassandra.model.Error error4 = new org.ebayopensource.turmeric.runtime.error.cassandra.model.Error();
//		error4.setErrorId(4L);
//		error4.setCategory( ErrorCategory.SYSTEM.value());
//		error4.setDomain("domain_1");
//		error4.setSubDomain("sub_domain_1");
//		error4.setOrganization("organization");
//		error4.setSeverity(ErrorSeverity.ERROR.value());
//		error4.setName("error_2");
//		
///////////////
//		  org.ebayopensource.turmeric.runtime.error.cassandra.model.Error error5 = new org.ebayopensource.turmeric.runtime.error.cassandra.model.Error();
//			error5.setErrorId(5L);
//			error5.setCategory( ErrorCategory.APPLICATION.value());
//			error5.setDomain("domain_1");
//			error5.setSubDomain("sub_domain_1");
//			error5.setOrganization("organization");
//			error5.setSeverity(ErrorSeverity.ERROR.value());
//			error5.setName("error_3");
//        
//         ErrorValue errorValue5 = new ErrorValue(error3, "message4", "service_2", "operation_3_1", "consumer_3",
//                        oneMinuteAgo, true, 0);
//        entityManager.persist(errorValue5);
//
//        /////////////
//		  org.ebayopensource.turmeric.runtime.error.cassandra.model.Error error6 = new org.ebayopensource.turmeric.runtime.error.cassandra.model.Error();
//			error6.setErrorId(6L);
//			error6.setCategory( ErrorCategory.APPLICATION.value());
//			error6.setDomain("domain_1");
//			error6.setSubDomain("sub_domain_1");
//			error6.setOrganization("organization");
//			error6.setSeverity(ErrorSeverity.ERROR.value());
//			error6.setName("error_3");
//            
//        ErrorValue errorValue6 = new ErrorValue(error3, "message5", "service_2", "operation_3_1", "consumer_3",
//                        twoMinutesAgo, true, 0);
//
//        cassandra.persist and commit();
    
	    
	}
    
	/**
	 * Test nothing.
	 */
	@Test
	public void testNothing() {
		assertNull(null);
		
	}

	   /**
     * Test get error graph for empty category.
     */
//    @Test
    public void testGetErrorGraphForEmptyCategory() {
        long duration = 3600;// in secs
        int aggregationPeriod = 60;// in secs
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(twoMinutesAgo);
        metricCriteria.setDuration(duration);
        metricCriteria.setAggregationPeriod(aggregationPeriod);
        metricCriteria.setRoleType("server");

        List<MetricGraphData> result = provider.getErrorGraph("service_3", null, null, null,
                        ErrorCategory.REQUEST.value(), null, metricCriteria);

        assertNotNull(result);
        assertEquals(duration / aggregationPeriod, result.size());
        // there must be duration/aggregationPeriod elements. The first
        assertEquals(duration / aggregationPeriod, result.size());
        // must not be any value > 0.0
        for (int i = 0; i < duration / aggregationPeriod; i++) {
            assertEquals(0.0d, result.get(0).getCount(), 0.0d);
        }

    }
    
    /**
     * Test get application error graph for service one minute ago.
     */
//    @Test
    public void testGetApplicationErrorGraphForServiceOneMinuteAgo() {
        long duration = 120;// in secs
        int aggregationPeriod = 60;// in secs
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(oneMinuteAgo);
        metricCriteria.setDuration(duration);
        metricCriteria.setAggregationPeriod(aggregationPeriod);
        metricCriteria.setRoleType("server");

        List<MetricGraphData> result = provider.getErrorGraph("service_2", null, null, null,
                        ErrorCategory.APPLICATION.value(), null, metricCriteria);

        assertNotNull(result);
        assertEquals(duration / aggregationPeriod, result.size());
        // there must be duration/aggregationPeriod elements. The first
        assertEquals(duration / aggregationPeriod, result.size());
        assertEquals(1.0d, result.get(0).getCount(), 0.0d);// first element must be 1
        assertEquals(0.0d, result.get(1).getCount(), 0.0d);// second element must be 0
    }
    
    /**
     * Test get application error graph for service two minutes ago.
     */
//    @Test
    public void testGetApplicationErrorGraphForServiceTwoMinutesAgo() {
        long duration = 180;// in secs
        int aggregationPeriod = 60;// in secs
        MetricCriteria metricCriteria = new MetricCriteria();
        metricCriteria.setFirstStartTime(twoMinutesAgo);
        metricCriteria.setDuration(duration);
        metricCriteria.setAggregationPeriod(aggregationPeriod);
        metricCriteria.setRoleType("server");

        List<MetricGraphData> result = provider.getErrorGraph("service_2", null, null, null,
                        ErrorCategory.APPLICATION.value(), null, metricCriteria);

        assertNotNull(result);
        assertEquals(duration / aggregationPeriod, result.size());
        // there must be duration/aggregationPeriod elements. The first
        assertEquals(duration / aggregationPeriod, result.size());
        assertEquals(1.0d, result.get(0).getCount(), 0.0d);// first element must be 1
        assertEquals(1.0d, result.get(1).getCount(), 0.0d);// second element must be 1
        assertEquals(0.0d, result.get(2).getCount(), 0.0d);// third element must be 0

    }
}
