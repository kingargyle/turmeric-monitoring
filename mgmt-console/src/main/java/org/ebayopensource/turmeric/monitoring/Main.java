/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.monitoring;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Main {
    
    public static final String GET_METRICS_DATA_JSON = "{"+
    "\"getMetricsDataResponse\":{"+
    "\"returnData\":[{"+
    "\"criteriaInfo\":{"+
    "\"serviceConsumerType\":\"Operation\","+
    "\"metricName\":\"CallCount\","+
    "\"serviceName\":\"SOAMetricsQueryService\","+
    "\"operationName\":\"getMetricsData\","+
    "\"roleType\":\"server\""+
    "},"+
    "\"diff\":\"0.0\","+
    "\"count2\":\"1.0\","+
    "\"count1\":\"1.0\""+
    "}]"+
    "}"+
    "}";
    
    public static class SimpleHandler extends AbstractHandler {

        public void handle(String target,
                Request baseRequest,
                HttpServletRequest request,
                HttpServletResponse response) throws IOException,
                ServletException {
            
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            
            String tmp = request.getParameter("X-TURMERIC-SERVICE-NAME");
            if ("SOAMetricsQueryService".equals(tmp)) {
                tmp = request.getParameter("X-TURMERIC-OPERATION-NAME");
                
                if ("getMetricsData".equals(tmp)) {
                    response.getOutputStream().write(GET_METRICS_DATA_JSON.getBytes());
                }
            }
        }
    }
    
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(9090);
        server.setHandler(new SimpleHandler());

        server.start();
        server.join();
    }

    

}
