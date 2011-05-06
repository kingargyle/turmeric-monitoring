package org.ebayopensource.turmeric.services.monitoringservice.junit;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The Class TestGetMetricsDataErrorCount.
 *
 * @author Udayasankar Natarajan
 */

public class TestGetMetricsDataErrorCount extends AbstractSOAQueryMetricsTest  {
	
	/** The base path. */
	private static String basePath = "META-INF/data/testcases/GetMetricsData";

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
	 * Testrequest11.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest11() throws Exception {
		String requestXmlPath = basePath + "/request/request11.xml";
		String respXmlPath = basePath + "/response/response11.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest12.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest12() throws Exception {
		String requestXmlPath = basePath + "/request/request12.xml";
		String respXmlPath = basePath + "/response/response12.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest13.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest13() throws Exception {
		String requestXmlPath = basePath + "/request/request13.xml";
		String respXmlPath = basePath + "/response/response13.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest14.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest14() throws Exception {
		String requestXmlPath = basePath + "/request/request14.xml";
		String respXmlPath = basePath + "/response/response14.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest15.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest15() throws Exception {
		String requestXmlPath = basePath + "/request/request15.xml";
		String respXmlPath = basePath + "/response/response15.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}



	/**
	 * Testrequest22.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest22() throws Exception {
		String requestXmlPath = basePath + "/request/request22.xml";
		String respXmlPath = basePath + "/response/response22.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest23.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest23() throws Exception {
		String requestXmlPath = basePath + "/request/request23.xml";
		String respXmlPath = basePath + "/response/response23.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest24.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest24() throws Exception {
		String requestXmlPath = basePath + "/request/request24.xml";
		String respXmlPath = basePath + "/response/response24.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}


	/**
	 * Testrequest37.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest37() throws Exception {
		String requestXmlPath = basePath + "/request/request37.xml";
		String respXmlPath = basePath + "/response/response37.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest38.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest38() throws Exception {
		String requestXmlPath = basePath + "/request/request38.xml";
		String respXmlPath = basePath + "/response/response38.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest39.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest39() throws Exception {
		String requestXmlPath = basePath + "/request/request39.xml";
		String respXmlPath = basePath + "/response/response39.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest40.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest40() throws Exception {
		String requestXmlPath = basePath + "/request/request40.xml";
		String respXmlPath = basePath + "/response/response40.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest41.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest41() throws Exception {
		String requestXmlPath = basePath + "/request/request41.xml";
		String respXmlPath = basePath + "/response/response41.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest42.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest42() throws Exception {
		String requestXmlPath = basePath + "/request/request42.xml";
		String respXmlPath = basePath + "/response/response42.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}


	/**
	 * Testrequest49.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest49() throws Exception {
		String requestXmlPath = basePath + "/request/request49.xml";
		String respXmlPath = basePath + "/response/response49.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest50.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest50() throws Exception {
		String requestXmlPath = basePath + "/request/request50.xml";
		String respXmlPath = basePath + "/response/response50.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest51.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest51() throws Exception {
		String requestXmlPath = basePath + "/request/request51.xml";
		String respXmlPath = basePath + "/response/response51.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest52.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest52() throws Exception {
		String requestXmlPath = basePath + "/request/request52.xml";
		String respXmlPath = basePath + "/response/response52.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest53.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest53() throws Exception {
		String requestXmlPath = basePath + "/request/request53.xml";
		String respXmlPath = basePath + "/response/response53.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest54.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest54() throws Exception {
		String requestXmlPath = basePath + "/request/request54.xml";
		String respXmlPath = basePath + "/response/response54.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

}
