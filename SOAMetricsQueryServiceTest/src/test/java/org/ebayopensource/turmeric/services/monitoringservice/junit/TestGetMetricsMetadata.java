package org.ebayopensource.turmeric.services.monitoringservice.junit;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Udayasankar Natarajan
 */

public class TestGetMetricsMetadata extends TestCase {
	private static String basePath = "META-INF/data/testcases/GetMetricsMetadata";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testrequest1() {
		String requestXmlPath = basePath + "/request/request1.xml";
		String respXmlPath = basePath + "/response/response1.xml";
		Utils.testGetMetricsMetadata(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest2() {
		String requestXmlPath = basePath + "/request/request2.xml";
		String respXmlPath = basePath + "/response/response2.xml";
		Utils.testGetMetricsMetadata(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest3() {
		String requestXmlPath = basePath + "/request/request3.xml";
		String respXmlPath = basePath + "/response/response3.xml";
		Utils.testGetMetricsMetadata(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest4() {
		String requestXmlPath = basePath + "/request/request4.xml";
		String respXmlPath = basePath + "/response/response4.xml";
		Utils.testGetMetricsMetadata(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest5() {
		String requestXmlPath = basePath + "/request/request5.xml";
		String respXmlPath = basePath + "/response/response5.xml";
		Utils.testGetMetricsMetadata(requestXmlPath, respXmlPath);
	}

}
