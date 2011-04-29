/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring.test;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.ebayopensource.turmeric.monitoring.server.DownloadServlet;
import org.ebayopensource.turmeric.monitoring.test.util.UtilFileReader;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for the download servlet. This Test class basically deploy in a jetty instance the {@link  DownloadServlet}
 * and a mock servlet of the SQMS {@link MockSQMSServlet}. The tests methods depend on a series of resource text files
 * that determine for each MetricName being tested which responses (json and csv) should be retrieved
 * 
 * @author manuelchinea
 *
 */

public class DownloadServletTest {
    private Server server;
    private HttpClient client;
    private String uri;
    private ServletHolder mockSQMSHolder =null;
    private UtilFileReader utilReader = new UtilFileReader();

    /**
     * Basic init of the download servlet and the MockSQMS.
     * @throws Exception
     *             if the setup fails
     */
    @Before
    public void startServer() throws Exception {
        server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(8976);
        server.addConnector(connector);

        String contextPath = "";
        ServletContextHandler context = new ServletContextHandler(server, contextPath, ServletContextHandler.SESSIONS);
        ServletHolder downloadServltHolder = new ServletHolder(DownloadServlet.class);
        downloadServltHolder.setInitOrder(1);
        String sqmsURL = "http://localhost:" + connector.getPort() + "/console/sqms";
        System.err.println(sqmsURL);
        
        downloadServltHolder.setInitParameter("SOAMetricsQueryServiceURL", sqmsURL);
        downloadServltHolder.setInitParameter("myPrefix", "/console/dwnld");
        context.addServlet(downloadServltHolder, "/console/dwnld/*");

        mockSQMSHolder = new ServletHolder(MockSQMSServlet.class);
        mockSQMSHolder.setInitOrder(1);
        context.addServlet(mockSQMSHolder, "/console/sqms/*");

        server.start();
        uri = "http://localhost:" + connector.getPort() + "/console/dwnld";

        client = new HttpClient();
        client.start();
    }

    
    /**
     * Stops the jetty instance.
     * @throws Exception if there's a problem stopping the jetty instance
     */
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

    
    /**
     * Test for the CallCount metric data download.
     * @throws Exception 
     */
    @Test
    public void testGetCallCountDownloadData() throws Exception{
        String resultContent = getMetricsDataOperationDownload("CallCount", 1);
        System.err.println(resultContent);

        assertEquals(utilReader.getResponseString("CallCount", 1), resultContent.trim());
    }
    
    
    
    
    /**
     * Test for the ResponseTime metric data download.
     * @throws Exception If the request/response of the data fails
     */
    @Test
    public void testGetResponseTimeDownloadData() throws Exception {
        String resultContent = getMetricsDataOperationDownload("ResponseTime", 1);
        System.err.println(resultContent);

        assertEquals(utilReader.getResponseString("ResponseTime", 1), resultContent.trim());
    }

    

    private String getMetricsDataOperationDownload(String metricName, int responseIndex) throws Exception {
        mockSQMSHolder.setInitParameter("responseIndex", String.valueOf(responseIndex));
        mockSQMSHolder.setInitParameter("metricName", metricName);
        ContentExchange getMetricsData = new ContentExchange(true);

        // and deserializes it as service operation's arguments
        getMetricsData.setMethod(HttpMethods.GET);

        getMetricsData.addRequestHeader("Content-Type", "application/json;charset=UTF-8");

        StringBuilder serviceURI = new StringBuilder(uri);
        serviceURI.append(utilReader.getRequestString(metricName, responseIndex));

        getMetricsData.setURL(serviceURI.toString());

        client.send(getMetricsData);
        int status = getMetricsData.waitForDone();
        assertEquals(HttpExchange.STATUS_COMPLETED, status);
        assertEquals(HttpServletResponse.SC_OK, getMetricsData.getResponseStatus());
        HttpFields responseFields = getMetricsData.getResponseFields();
        String contentDisposition = responseFields.getStringField("Content-disposition");
        computeFileNameSuffix(metricName, responseIndex);
        Assert.assertTrue (contentDisposition.endsWith(computeFileNameSuffix(metricName, responseIndex)));
        
        return getMetricsData.getResponseContent();
    }

    private String computeFileNameSuffix(String operationName, int index) {
        // duration+secondStarttime+reoletype+firstStartTime+metricName
        String requestContent = utilReader.getRequestString(operationName, index);
        String duration = getElementValueFromString("duration", requestContent);
        String secondStartTime = getElementValueFromString("secondStartTime", requestContent);
        String roleType = getElementValueFromString("roleType", requestContent);
        String firstStartTime = getElementValueFromString("firstStartTime", requestContent);
        String metricName = getElementValueFromString("metricName", requestContent);
        String result = duration + "_" + secondStartTime + "_" + roleType + "_" + firstStartTime + "_" + metricName
                        + ".csv";

        return result;
    }

    private String getElementValueFromString(String key, String requestContent) {
        String result = null;
        int indexOfKey = requestContent.indexOf(key);
        if (indexOfKey > 0) {
            int indexOfTrailingAmp = requestContent.indexOf("&", indexOfKey);
            if (indexOfTrailingAmp > 0) {
                // the current format is key=value
                result = requestContent.substring(indexOfKey + key.length() + 1, indexOfTrailingAmp);
            }
            else {
                result = requestContent.substring(indexOfKey + key.length() + 1);
            }

        }
        else {

        }
        return result;
    }

}
