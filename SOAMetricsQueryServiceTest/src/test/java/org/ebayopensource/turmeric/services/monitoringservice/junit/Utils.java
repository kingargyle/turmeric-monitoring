/*******************************************************************************
 * Copyright (c) 2006, 2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.services.monitoringservice.junit;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXB;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.custommonkey.xmlunit.XMLAssert;
import org.ebayopensource.turmeric.monitoring.impl.SOAMetricsQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricValueRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricValueResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsMetadataRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsMetadataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsResponse;
import org.unitils.reflectionassert.ReflectionAssert;

public class Utils {
	public static SOAMetricsQueryServiceImpl consumer = null;
	
	public static void init() {
		try {
			consumer = new SOAMetricsQueryServiceImpl();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public static void testGetMetricsMetadata(String requestXmlPath, String respXmlPath) {
		init();
		/*File reqFile = new File (requestXmlPath);
		File respFile = new File(respXmlPath);
		
		GetMetricsMetadataRequest req = JAXB.unmarshal(reqFile, GetMetricsMetadataRequest.class);
		GetMetricsMetadataResponse xmlResp = JAXB.unmarshal(respFile, GetMetricsMetadataResponse.class);
		
		GetMetricsMetadataResponse resp = consumer.getMetricsMetadata(req);
		
		ReflectionAssert.assertReflectionEquals(xmlResp, resp);*/
		ClassLoader cl = Utils.class.getClassLoader();
		InputStream requestis = cl.getResourceAsStream(requestXmlPath);
		InputStream responseis = cl.getResourceAsStream(respXmlPath);
		
		GetMetricsMetadataRequest req = JAXB.unmarshal(requestis, GetMetricsMetadataRequest.class);
		GetMetricsMetadataResponse xmlResp = JAXB.unmarshal(responseis, GetMetricsMetadataResponse.class);
		
		GetMetricsMetadataResponse resp = consumer.getMetricsMetadata(req);
		
		ReflectionAssert.assertReflectionEquals(xmlResp, resp);
	}
	
	public static void testGetMetricValue(String requestXmlPath, String respXmlPath) {
		init();
		/*File reqFile = new File (requestXmlPath);
		File respFile = new File(respXmlPath);
		
		GetMetricValueRequest req = JAXB.unmarshal(reqFile, GetMetricValueRequest.class);
		GetMetricValueResponse xmlResp = JAXB.unmarshal(respFile, GetMetricValueResponse.class);
		
		GetMetricValueResponse resp = consumer.getMetricValue(req);
		
		ReflectionAssert.assertReflectionEquals(xmlResp, resp);*/
		
		ClassLoader cl = Utils.class.getClassLoader();
		InputStream requestis = cl.getResourceAsStream(requestXmlPath);
		InputStream responseis = cl.getResourceAsStream(respXmlPath);
		
		GetMetricValueRequest req = JAXB.unmarshal(requestis, GetMetricValueRequest.class);
		GetMetricValueResponse xmlResp = JAXB.unmarshal(responseis, GetMetricValueResponse.class);
		
		GetMetricValueResponse resp = consumer.getMetricValue(req);
		
		ReflectionAssert.assertReflectionEquals(xmlResp, resp);
	}

	public static void testGetMetricsData(String requestXmlPath, String respXmlPath) throws Exception {
		init();
		/*File reqFile = new File (requestXmlPath);
		File respFile = new File(respXmlPath);
		
		GetMetricsRequest req = JAXB.unmarshal(reqFile, GetMetricsRequest.class);
		GetMetricsResponse xmlResp = JAXB.unmarshal(respFile, GetMetricsResponse.class);
		
		GetMetricsResponse resp = consumer.getMetricsData(req);
		
		ReflectionAssert.assertReflectionEquals(xmlResp, resp);*/
		
		ClassLoader cl = Utils.class.getClassLoader();
		InputStreamReader requestis = new InputStreamReader(cl.getResourceAsStream(requestXmlPath));
		InputStreamReader responseis = new InputStreamReader(cl.getResourceAsStream(respXmlPath));		
		
		GetMetricsRequest req = JAXB.unmarshal(requestis, GetMetricsRequest.class);
//		GetMetricsResponse xmlResp = JAXB.unmarshal(responseis, GetMetricsResponse.class);
		
		
		GetMetricsResponse resp = consumer.getMetricsData(req);
		ByteArrayOutputStream xmlResp = new ByteArrayOutputStream(); 
		JAXB.marshal(resp, xmlResp);
		StringReader str = new StringReader(xmlResp.toString());
		System.out.println(xmlResp.toString());
		XMLAssert.assertXMLEqual("Responses do not match. ", responseis, str);
		
//		ReflectionAssert.assertReflectionEquals(xmlResp, resp);
	}
}
