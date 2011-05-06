package org.ebayopensource.turmeric.services.monitoringservice.junit;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The Class TestGetMetricsMetadata.
 *
 * @author Udayasankar Natarajan
 */

public class TestGetMetricsMetadata extends AbstractSOAQueryMetricsTest {
	
	/** The base path. */
	private static String basePath = "META-INF/data/testcases/GetMetricsMetadata";

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		CSVImporter.importCSV();
		XMLUnit.setIgnoreWhitespace(true);
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception the exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Testrequest1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest1() throws Exception {
		String requestXmlPath = basePath + "/request/request1.xml";
		String respXmlPath = basePath + "/response/response1.xml";
		testGetMetricsMetadata(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest2.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest2() throws Exception {
		String requestXmlPath = basePath + "/request/request2.xml";
		String respXmlPath = basePath + "/response/response2.xml";
		testGetMetricsMetadata(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest3.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest3() throws Exception {
		String requestXmlPath = basePath + "/request/request3.xml";
		String respXmlPath = basePath + "/response/response3.xml";
		testGetMetricsMetadata(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest4.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest4() throws Exception {
		String requestXmlPath = basePath + "/request/request4.xml";
		String respXmlPath = basePath + "/response/response4.xml";
		testGetMetricsMetadata(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest5.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest5() throws Exception {
		String requestXmlPath = basePath + "/request/request5.xml";
		String respXmlPath = basePath + "/response/response5.xml";
		testGetMetricsMetadata(requestXmlPath, respXmlPath);
	}

}
