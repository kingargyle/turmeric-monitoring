/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;

import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.MonitoringSystem;
import org.ebayopensource.turmeric.runtime.common.types.SOAHeaders;
import org.ebayopensource.turmeric.runtime.spf.pipeline.SPFServlet;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ajax.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// TODO: this test is skipped because of http://ph-0146.eva.ebay.com/jira/browse/TURMERIC-350
@Ignore
public class SOAMetricsQueryServiceNVTest {
    private Server server;
    private HttpClient client;
    private String uri;

    @Before
    public void startServer() throws Exception {
        server = new Server();
        Connector connector = new SelectChannelConnector();
        server.addConnector(connector);

        String contextPath = "";
        ServletContextHandler context = new ServletContextHandler(server, contextPath, ServletContextHandler.SESSIONS);
        String servletPath = "/ws/spf";
        ServletHolder servletHolder = new ServletHolder(SPFServlet.class);
        servletHolder.setInitOrder(1);
        context.addServlet(servletHolder, servletPath + "/*");

        server.start();
        uri = "http://localhost:" + connector.getLocalPort() + contextPath + servletPath;

        client = new HttpClient();
        client.start();
    }

    @After
    public void stopServer() throws Exception {
        if (client != null) {
            client.stop();
        }

        if (server != null) {
            server.stop();
            server.join();
        }
    }

    @Test
    public void testGetMetricsData() throws Exception {
        // Simulate the call of a service that generates metrics data, using the SOAMetricsQueryService itself.
        getMetricsData();
        forcePersistMetrics();

        // Read the metrics from the storage
        String responseString = getMetricsData();

        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) JSON.parse(responseString);
        assertEquals(1, response.size());
        @SuppressWarnings("unchecked")
        Map<String, Object> getMetricsDataResponse = (Map<String, Object>) response.get("getMetricsDataResponse");
        assertNotNull(getMetricsDataResponse);
        Object[] returnData = (Object[]) getMetricsDataResponse.get("returnData");
        assertNotNull(returnData);
        assertEquals(1, returnData.length);
        @SuppressWarnings("unchecked")
        Map<String, Object> metricGroupData = (Map<String, Object>) returnData[0];
        assertEquals(1, Double.valueOf((String) metricGroupData.get("count1")).intValue());
        assertEquals(1, Double.valueOf((String) metricGroupData.get("count2")).intValue());
        assertEquals(0, Double.valueOf((String) metricGroupData.get("diff")).intValue());
        @SuppressWarnings("unchecked")
        Map<String, Object> criteriaInfo = (Map<String, Object>) metricGroupData.get("criteriaInfo");
        assertNotNull(criteriaInfo);
        assertEquals("SOAMetricsQueryService", criteriaInfo.get("serviceName"));
    }

   @Test
    public void testGetMetricsMetaData() throws Exception {
        // Simulate the call of a service that generates metrics data, using the SOAMetricsQueryService itself.
        getMetricsData();
        forcePersistMetrics();

        String responseString = getMetricsMetaDataForServiceOperations();

        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) JSON.parse(responseString);
        assertEquals(1, response.size());
        @SuppressWarnings("unchecked")
        Map<String, Object> getMetricsDataResponse = (Map<String, Object>) response.get("getMetricsMetadataResponse");
        assertNotNull(getMetricsDataResponse);
        Object[] resourceEntityResponseNames = (Object[]) getMetricsDataResponse.get("resourceEntityResponseNames");
        assertNotNull(resourceEntityResponseNames);
        assertEquals(1, resourceEntityResponseNames.length);
        assertEquals("SOAMetricsQueryService.getMetricsData", resourceEntityResponseNames[0]);
    }

    private String getMetricsData() throws Exception {
        ContentExchange getMetricsData = new ContentExchange(true);

        getMetricsData.setMethod(HttpMethods.GET);
        long startTime = System.currentTimeMillis();
        // Move the start time a bit in the past so that we have some data available
        startTime -= TimeUnit.SECONDS.toMillis(30);

        StringBuilder serviceURI = new StringBuilder();
        serviceURI.append(uri);

        serviceURI.append("?").append(SOAHeaders.SERVICE_NAME).append("=SOAMetricsQueryService");
        serviceURI.append("&").append(SOAHeaders.SERVICE_OPERATION_NAME).append("=getMetricsData");
        serviceURI.append("&").append(SOAHeaders.RESPONSE_DATA_FORMAT).append("=JSON");
        serviceURI.append("&").append(SOAHeaders.REQUEST_DATA_FORMAT).append("=NV");
        serviceURI.append("&").append("nvns:ns=http://www.ebay.com/marketplace/services");
        serviceURI.append("&").append("ns:metricCriteria.ns:firstStartTime").append("=").append(String.valueOf(startTime));
        serviceURI.append("&").append("ns:metricCriteria.ns:secondStartTime").append("=").append(String.valueOf(startTime));
        serviceURI.append("&").append("ns:metricCriteria.ns:duration").append("=3600");
        serviceURI.append("&").append("ns:metricCriteria.ns:aggregationPeriod").append("=60");
        serviceURI.append("&").append("ns:metricCriteria.ns:sortOrder=ascending");
        serviceURI.append("&").append("ns:metricCriteria.ns:numRows").append("=5");
        serviceURI.append("&").append("ns:metricCriteria.ns:metricName=CallCount");
        serviceURI.append("&").append("ns:metricResourceCriteria.ns:resourceEntityResponseType=Operation");
        serviceURI.append("&").append("ns:metricResourceCriteria.ns:resourceRequestEntities.ns:resourceEntityType=Service");
        serviceURI.append("&").append("ns:metricResourceCriteria.ns:resourceRequestEntities.ns:resourceEntityName=SOAMetricsQueryService");

        getMetricsData.setURL(serviceURI.toString());

        client.send(getMetricsData);
        int status = getMetricsData.waitForDone();

        assertEquals(HttpExchange.STATUS_COMPLETED, status);
        assertEquals(HttpServletResponse.SC_OK, getMetricsData.getResponseStatus());

        return getMetricsData.getResponseContent();
    }

    private String getMetricsMetaDataForServiceOperations() throws Exception {
        ContentExchange getMetricsMetaData = new ContentExchange(true);

        getMetricsMetaData.setMethod(HttpMethods.GET);

        StringBuilder serviceURI = new StringBuilder();
        serviceURI.append(uri);

        //Get the operations of Service SOAMetricsQueryService (could be any service, but there
        //aren't any others right now
        serviceURI.append("?").append(SOAHeaders.SERVICE_NAME).append("=SOAMetricsQueryService");
        serviceURI.append("&").append(SOAHeaders.SERVICE_OPERATION_NAME).append("=getMetricsMetadata");
        serviceURI.append("&").append(SOAHeaders.RESPONSE_DATA_FORMAT).append("=json");
        serviceURI.append("&").append(SOAHeaders.REQUEST_DATA_FORMAT).append("=NV");
        serviceURI.append("&").append("nvns:ns=http://www.ebay.com/marketplace/services");
        serviceURI.append("&").append("ns:resourceEntityType=Service");
        serviceURI.append("&").append("ns:resourceEntityName=SOAMetricsQueryService");
        serviceURI.append("&").append("ns:resourceEntityResponseType=Operation");

        getMetricsMetaData.setURL(serviceURI.toString());

        client.send(getMetricsMetaData);
        int status = getMetricsMetaData.waitForDone();

        assertEquals(HttpExchange.STATUS_COMPLETED, status);
        assertEquals(HttpServletResponse.SC_OK, getMetricsMetaData.getResponseStatus());

        return getMetricsMetaData.getResponseContent();
    }

    /**
     * Forces the MetricsStorageProvider to persist the metrics in memory to the storage
     */
    private void forcePersistMetrics() {
        MonitoringSystem.persistMetricsSnapSnapshot(null, true);
    }
}
