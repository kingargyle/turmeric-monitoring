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
import org.eclipse.jetty.io.ByteArrayBuffer;
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
public class SOAMetricsQueryServiceJSONTest {
    private Server server;
    private HttpClient client;
    private String uri;

    /**
     * This setup method is not static since we don't want data in the DB to survive each test.
     * This way, each test starts with an empty DB and does not depend on other tests
     * @throws Exception if the setup fails
     */
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
    public void join() throws Exception {
        // Simulate the call of a service that generates metrics data, using the SOAMetricsQueryService itself.
        getMetricsData();
//        forcePersistMetrics();

        String result = getErrorMetricsData();
        System.err.println("result = " + result);

        server.join();
    }

//    @Test
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
        assertEquals("getMetricsData", criteriaInfo.get("operationName"));
    }

//    @Test
    public void testGetMetricsMetadata() throws Exception {
        // Simulate the call of a service that generates metrics data, using the SOAMetricsQueryService itself.
        getMetricsData();
        forcePersistMetrics();

        String responseString = getMetricsMetadata();
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

    /**
     * Forces the MetricsStorageProvider to persist the metrics in memory to the storage
     */
    private void forcePersistMetrics() {
        MonitoringSystem.persistMetricsSnapSnapshot(null, true);
    }

    private String getMetricsData() throws Exception {
        ContentExchange getMetricsData = new ContentExchange(true);

        // The method MUST be POST so the SOA framework uses the HTTP body
        // and deserializes it as service operation's arguments
        getMetricsData.setMethod(HttpMethods.POST);

        getMetricsData.addRequestHeader("Content-Type", "application/json;charset=UTF-8");

        StringBuilder serviceURI = new StringBuilder();
        serviceURI.append(uri);
        serviceURI.append("?").append(SOAHeaders.SERVICE_NAME).append("=SOAMetricsQueryService");
//        serviceURI.append("&").append(SOAHeaders.SERVICE_OPERATION_NAME).append("=getMetricsData");
        serviceURI.append("&").append(SOAHeaders.SERVICE_OPERATION_NAME).append("=invalid");
        serviceURI.append("&").append(SOAHeaders.REQUEST_DATA_FORMAT).append("=JSON");
        serviceURI.append("&").append(SOAHeaders.RESPONSE_DATA_FORMAT).append("=JSON");
        getMetricsData.setURL(serviceURI.toString());

        long startTime = System.currentTimeMillis();
        // Move the start time a bit in the past so that we have some data available
        startTime -= TimeUnit.SECONDS.toMillis(30);

        // It is very important to specify the namespace, otherwise the unmarshalling of
        // the service operation's arguments will not work properly.
        // Also, the namespace cannot be any namespace, but must be the proper one.
        String body = "" +
                "{" +
                "   \"jsonns.ns\":\"http://www.ebay.com/marketplace/services\"," +
                "   \"ns.getMetricsDataRequest\":{" +
                "       \"ns.metricCriteria\":{" +
                "           \"ns.firstStartTime\":" + startTime + "," +
                "           \"ns.secondStartTime\":" + startTime + "," +
                "           \"ns.duration\":3600," +
                "           \"ns.aggregationPeriod\":60," +
                "           \"ns.numRows\":\"5\"," +
                "           \"ns.metricName\":\"CallCount\"," +
                "           \"ns.roleType\":\"server\"" +
                "       }," +
                "       \"ns.metricResourceCriteria\":{" +
                "           \"ns.resourceEntityResponseType\":\"Operation\"," +
                "           \"ns.resourceRequestEntities\":{"+
                "                \"ns.resourceEntityType\":\"Service\"" +
                "           }"+
                "       }" +
                "   }" +
                "}";
        getMetricsData.setRequestContent(new ByteArrayBuffer(body, "UTF-8"));

        client.send(getMetricsData);
        int status = getMetricsData.waitForDone();
        assertEquals(HttpExchange.STATUS_COMPLETED, status);
//        assertEquals(HttpServletResponse.SC_OK, getMetricsData.getResponseStatus());

        return getMetricsData.getResponseContent();
    }

    private String getMetricsMetadata() throws Exception {
        ContentExchange getMetricsMetadata = new ContentExchange(true);
        getMetricsMetadata.setMethod(HttpMethods.POST);
        getMetricsMetadata.addRequestHeader("Content-Type", "application/json;charset=UTF-8");

        StringBuilder serviceURI = new StringBuilder();
        serviceURI.append(uri);
        serviceURI.append("?").append(SOAHeaders.SERVICE_NAME).append("=SOAMetricsQueryService");
        serviceURI.append("&").append(SOAHeaders.SERVICE_OPERATION_NAME).append("=getMetricsMetadata");
        serviceURI.append("&").append(SOAHeaders.REQUEST_DATA_FORMAT).append("=JSON");
        serviceURI.append("&").append(SOAHeaders.RESPONSE_DATA_FORMAT).append("=JSON");
        getMetricsMetadata.setURL(serviceURI.toString());

        long startTime = System.currentTimeMillis();
        startTime -= TimeUnit.SECONDS.toMillis(30);

        String body = "" +
                "{" +
                "   \"jsonns.ns\":\"http://www.ebay.com/marketplace/services\"," +
                "   \"ns.getMetricsMetadataRequest\":{" +
                "       \"ns.resourceEntityType\":\"Service\"," +
                "       \"ns.resourceEntityName\":[\"SOAMetricsQueryService\"]," +
                "       \"ns.resourceEntityResponseType\":\"Operation\"" +
                "   }" +
                "}";
        getMetricsMetadata.setRequestContent(new ByteArrayBuffer(body, "UTF-8"));

        client.send(getMetricsMetadata);
        int status = getMetricsMetadata.waitForDone();
        assertEquals(HttpExchange.STATUS_COMPLETED, status);
        assertEquals(HttpServletResponse.SC_OK, getMetricsMetadata.getResponseStatus());

        return getMetricsMetadata.getResponseContent();
    }

    private String getErrorMetricsData() throws Exception {
        ContentExchange getErrorMetricsData = new ContentExchange(true);
        getErrorMetricsData.setMethod(HttpMethods.POST);
        getErrorMetricsData.addRequestHeader("Content-Type", "application/json;charset=UTF-8");

        StringBuilder serviceURI = new StringBuilder();
        serviceURI.append(uri);
        serviceURI.append("?").append(SOAHeaders.SERVICE_NAME).append("=SOAMetricsQueryService");
        serviceURI.append("&").append(SOAHeaders.SERVICE_OPERATION_NAME).append("=getErrorMetricsData");
        serviceURI.append("&").append(SOAHeaders.REQUEST_DATA_FORMAT).append("=JSON");
        serviceURI.append("&").append(SOAHeaders.RESPONSE_DATA_FORMAT).append("=JSON");
        getErrorMetricsData.setURL(serviceURI.toString());

        long startTime = System.currentTimeMillis();
        startTime -= TimeUnit.SECONDS.toMillis(30);

        String body = "" +
                "{" +
                "   \"jsonns.ns\":\"http://www.ebayopensource.org/turmeric/monitoring/v1/services\"," +
                "   \"ns.getErrorMetricsDataRequest\":{" +
                "       \"ns.errorType\":\"Category\"," +
                "       \"ns.metricCriteria\":{" +
                "           \"ns.firstStartTime\":" + startTime + "," +
                "           \"ns.secondStartTime\":" + startTime + "," +
                "           \"ns.duration\":3600," +
                "           \"ns.aggregationPeriod\":60," +
                "           \"ns.numRows\":\"5\"," +
                "           \"ns.roleType\":\"server\"" +
                "       }," +
                "   }" +
                "}";
        getErrorMetricsData.setRequestContent(new ByteArrayBuffer(body, "UTF-8"));

        client.send(getErrorMetricsData);
        int status = getErrorMetricsData.waitForDone();
        assertEquals(HttpExchange.STATUS_COMPLETED, status);
        assertEquals(HttpServletResponse.SC_OK, getErrorMetricsData.getResponseStatus());

        return getErrorMetricsData.getResponseContent();
    }
}
