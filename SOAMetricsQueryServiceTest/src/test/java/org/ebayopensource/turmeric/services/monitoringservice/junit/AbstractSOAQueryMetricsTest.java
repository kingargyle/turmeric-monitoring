/*******************************************************************************
 * Copyright (c) 2006, 2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.services.monitoringservice.junit;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXB;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.custommonkey.xmlunit.Diff;
import org.ebayopensource.turmeric.monitoring.impl.SOAMetricsQueryServiceImpl;
import org.ebayopensource.turmeric.monitoring.util.IgnoreMachineName;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricValueRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricValueResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsMetadataRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsMetadataResponse;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsRequest;
import org.ebayopensource.turmeric.monitoring.v1.services.GetMetricsResponse;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * The Class AbstractSOAQueryMetricsTest.
 * 
 * @author dcarver
 * @since 1.0.0
 * 
 */
public abstract class AbstractSOAQueryMetricsTest {

	/** The consumer. */
	private SOAMetricsQueryServiceImpl consumer = null;

	/**
	 * Inits the.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void init() throws Exception {
		consumer = new SOAMetricsQueryServiceImpl();
	}

	/**
	 * Tear down.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@After
	public void tearDown() throws Exception {
		consumer = null;
	}

	/**
	 * Test get metrics metadata.
	 * 
	 * @param requestXmlPath
	 *            the request xml path
	 * @param respXmlPath
	 *            the resp xml path
	 * @throws Exception
	 *             the exception
	 */
	public void testGetMetricsMetadata(String requestXmlPath, String respXmlPath)
			throws Exception {
		init();
		ClassLoader cl = AbstractSOAQueryMetricsTest.class.getClassLoader();
		InputStreamReader requestis = new InputStreamReader(
				cl.getResourceAsStream(requestXmlPath));
		InputStreamReader responseis = new InputStreamReader(
				cl.getResourceAsStream(respXmlPath));

		GetMetricsMetadataRequest req = JAXB.unmarshal(requestis,
				GetMetricsMetadataRequest.class);

		GetMetricsMetadataResponse resp = consumer.getMetricsMetadata(req);

		ByteArrayOutputStream xmlResp = new ByteArrayOutputStream();
		JAXB.marshal(resp, xmlResp);
		StringReader str = new StringReader(xmlResp.toString());
		System.out.println(xmlResp.toString());
		assertXMLEqual(responseis, str);
	}

	private void assertXMLEqual(Reader expected, Reader actual ) throws Exception {
		Diff diff = new Diff(expected, actual);
		diff.overrideDifferenceListener(new IgnoreMachineName());
		assertTrue(diff.toString(), diff.similar());
	}

	/**
	 * Test get metric value.
	 * 
	 * @param requestXmlPath
	 *            the request xml path
	 * @param respXmlPath
	 *            the resp xml path
	 * @throws Exception
	 *             the exception
	 */
	public void testGetMetricValue(String requestXmlPath, String respXmlPath)
			throws Exception {
		ClassLoader cl = AbstractSOAQueryMetricsTest.class.getClassLoader();
		InputStreamReader requestis = new InputStreamReader(
				cl.getResourceAsStream(requestXmlPath));
		InputStreamReader responseis = new InputStreamReader(
				cl.getResourceAsStream(respXmlPath));

		GetMetricValueRequest req = JAXB.unmarshal(requestis,
				GetMetricValueRequest.class);

		GetMetricValueResponse resp = consumer.getMetricValue(req);
		ByteArrayOutputStream xmlResp = new ByteArrayOutputStream();
		JAXB.marshal(resp, xmlResp);
		StringReader str = new StringReader(xmlResp.toString());
		System.out.println(xmlResp.toString());
		assertXMLEqual(responseis, str);
	}

	/**
	 * Test get metrics data.
	 * 
	 * @param requestXmlPath
	 *            the request xml path
	 * @param respXmlPath
	 *            the resp xml path
	 * @throws Exception
	 *             the exception
	 */
	public void testGetMetricsData(String requestXmlPath, String respXmlPath)
			throws Exception {
		ClassLoader cl = AbstractSOAQueryMetricsTest.class.getClassLoader();
		InputStreamReader requestis = new InputStreamReader(
				cl.getResourceAsStream(requestXmlPath));
		InputStreamReader responseis = new InputStreamReader(
				cl.getResourceAsStream(respXmlPath));

		GetMetricsRequest req = JAXB.unmarshal(requestis,
				GetMetricsRequest.class);

		GetMetricsResponse resp = consumer.getMetricsData(req);
		ByteArrayOutputStream xmlResp = new ByteArrayOutputStream();
		JAXB.marshal(resp, xmlResp);
		StringReader str = new StringReader(xmlResp.toString());
		System.out.println(xmlResp.toString());
		assertXMLEqual(responseis, str);
	}
}
