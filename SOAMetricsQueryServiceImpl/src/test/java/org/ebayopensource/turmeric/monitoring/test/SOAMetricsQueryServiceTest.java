/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.test;

import org.ebayopensource.turmeric.services.monitoring.intf.SOAMetricsQueryService;
import org.ebayopensource.turmeric.monitoring.v1.services.GetCustomReportDataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetDetailDataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetErrorGraphResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetErrorMetricsDataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetErrorMetricsMetadataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricSummaryDataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricValueResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsMetadataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetPolicyMetricDataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetPolicyMetricDetailDataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetStandardReportResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetVersionResponse;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.sif.service.Service;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceFactory;
import org.junit.Before;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

// TODO: re-enable this test
@Ignore
public class SOAMetricsQueryServiceTest {

    private SOAMetricsQueryService m_proxy;

    @Before
    public void initProxy() throws ServiceException
    {
        String svcAdminName = "SOAMetricsQueryService";
        String clientName = "SOAMetricsQueryService_Test";
        Service service = ServiceFactory.create(svcAdminName, clientName, null);
        m_proxy = service.getProxy();
    }

    public void testGetErrorMetricsMetadata() throws Exception {
        GetErrorMetricsMetadataResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getErrorMetricsMetadata(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

    public void testGetErrorMetricsData() throws Exception {
        GetErrorMetricsDataResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getErrorMetricsData(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

    public void testGetDetailData() throws Exception {
        GetDetailDataResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getDetailData(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

    public void testGetErrorGraph() throws Exception {
        GetErrorGraphResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getErrorGraph(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

    public void testGetPolicyMetricDetailData() throws Exception {
        GetPolicyMetricDetailDataResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getPolicyMetricDetailData(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

    public void testGetStandardReportData() throws Exception {
        GetStandardReportResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getStandardReportData(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

    public void testGetVersion() throws Exception {
        GetVersionResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getVersion(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

    public void testGetMetricsMetadata() throws Exception {
        GetMetricsMetadataResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getMetricsMetadata(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

    public void testGetMetricSummaryData() throws Exception {
        GetMetricSummaryDataResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getMetricSummaryData(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

    public void testGetCustomReportData() throws Exception {
        GetCustomReportDataResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getCustomReportData(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

    public void testGetMetricsData() throws Exception {
        GetMetricsResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getMetricsData(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

    public void testGetPolicyMetricData() throws Exception {
        GetPolicyMetricDataResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getPolicyMetricData(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

    public void testGetMetricValue() throws Exception {
        GetMetricValueResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = m_proxy.getMetricValue(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }
}
