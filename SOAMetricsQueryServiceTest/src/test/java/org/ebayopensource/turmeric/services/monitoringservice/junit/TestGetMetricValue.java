package org.ebayopensource.turmeric.services.monitoringservice.junit;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The Class TestGetMetricValue.
 *
 * @author Udayasankar Natarajan
 */

public class TestGetMetricValue extends AbstractSOAQueryMetricsTest {
	
	/** The base path. */
	private static String basePath = "META-INF/data/testcases/GetMetricValue";

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
	 * Testrequest1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest1() throws Exception {
		String requestXmlPath = basePath + "/request/request1.xml";
		String respXmlPath = basePath + "/response/response1.xml";
		testGetMetricValue(requestXmlPath, respXmlPath);
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
		testGetMetricValue(requestXmlPath, respXmlPath);
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
		testGetMetricValue(requestXmlPath, respXmlPath);
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
		testGetMetricValue(requestXmlPath, respXmlPath);
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
		testGetMetricValue(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest6.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest6() throws Exception {
		String requestXmlPath = basePath + "/request/request6.xml";
		String respXmlPath = basePath + "/response/response6.xml";
		testGetMetricValue(requestXmlPath, respXmlPath);
	}

}
