package org.ebayopensource.turmeric.services.monitoringservice.junit;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The Class TestGetMetricsData.
 *
 * @author Udayasankar Natarajan
 */

public class TestGetMetricsData extends AbstractSOAQueryMetricsTest {
	
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
	 * Testrequest1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest1() throws Exception {
		String requestXmlPath = basePath + "/request/request1.xml";
		String respXmlPath = basePath + "/response/response1.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest10.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest10() throws Exception {
		String requestXmlPath = basePath + "/request/request10.xml";
		String respXmlPath = basePath + "/response/response10.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest16.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest16() throws Exception {
		String requestXmlPath = basePath + "/request/request16.xml";
		String respXmlPath = basePath + "/response/response16.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest17.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest17() throws Exception {
		String requestXmlPath = basePath + "/request/request17.xml";
		String respXmlPath = basePath + "/response/response17.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest18.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest18() throws Exception {
		String requestXmlPath = basePath + "/request/request18.xml";
		String respXmlPath = basePath + "/response/response18.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest19.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest19() throws Exception {
		String requestXmlPath = basePath + "/request/request19.xml";
		String respXmlPath = basePath + "/response/response19.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
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
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest20.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest20() throws Exception {
		String requestXmlPath = basePath + "/request/request20.xml";
		String respXmlPath = basePath + "/response/response20.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest21.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest21() throws Exception {
		String requestXmlPath = basePath + "/request/request21.xml";
		String respXmlPath = basePath + "/response/response21.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest25.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest25() throws Exception {
		String requestXmlPath = basePath + "/request/request25.xml";
		String respXmlPath = basePath + "/response/response25.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest26.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest26() throws Exception {
		String requestXmlPath = basePath + "/request/request26.xml";
		String respXmlPath = basePath + "/response/response26.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest27.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest27() throws Exception {
		String requestXmlPath = basePath + "/request/request27.xml";
		String respXmlPath = basePath + "/response/response27.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest28.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest28() throws Exception {
		String requestXmlPath = basePath + "/request/request28.xml";
		String respXmlPath = basePath + "/response/response28.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest29.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest29() throws Exception {
		String requestXmlPath = basePath + "/request/request29.xml";
		String respXmlPath = basePath + "/response/response29.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
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
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest30.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest30() throws Exception {
		String requestXmlPath = basePath + "/request/request30.xml";
		String respXmlPath = basePath + "/response/response30.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest31.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest31() throws Exception {
		String requestXmlPath = basePath + "/request/request31.xml";
		String respXmlPath = basePath + "/response/response31.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest32.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest32() throws Exception {
		String requestXmlPath = basePath + "/request/request32.xml";
		String respXmlPath = basePath + "/response/response32.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest33.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest33() throws Exception {
		String requestXmlPath = basePath + "/request/request33.xml";
		String respXmlPath = basePath + "/response/response33.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest34.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest34() throws Exception {
		String requestXmlPath = basePath + "/request/request34.xml";
		String respXmlPath = basePath + "/response/response34.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest35.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest35() throws Exception {
		String requestXmlPath = basePath + "/request/request35.xml";
		String respXmlPath = basePath + "/response/response35.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest36.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest36() throws Exception {
		String requestXmlPath = basePath + "/request/request36.xml";
		String respXmlPath = basePath + "/response/response36.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
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
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest43.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest43() throws Exception {
		String requestXmlPath = basePath + "/request/request43.xml";
		String respXmlPath = basePath + "/response/response43.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest44.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest44() throws Exception {
		String requestXmlPath = basePath + "/request/request44.xml";
		String respXmlPath = basePath + "/response/response44.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest45.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest45() throws Exception {
		String requestXmlPath = basePath + "/request/request45.xml";
		String respXmlPath = basePath + "/response/response45.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest46.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest46() throws Exception {
		String requestXmlPath = basePath + "/request/request46.xml";
		String respXmlPath = basePath + "/response/response46.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest47.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest47() throws Exception {
		String requestXmlPath = basePath + "/request/request47.xml";
		String respXmlPath = basePath + "/response/response47.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest48.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest48() throws Exception {
		String requestXmlPath = basePath + "/request/request48.xml";
		String respXmlPath = basePath + "/response/response48.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
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
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest55.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest55() throws Exception {
		String requestXmlPath = basePath + "/request/request55.xml";
		String respXmlPath = basePath + "/response/response55.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest56.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest56() throws Exception {
		String requestXmlPath = basePath + "/request/request56.xml";
		String respXmlPath = basePath + "/response/response56.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest57.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest57() throws Exception {
		String requestXmlPath = basePath + "/request/request57.xml";
		String respXmlPath = basePath + "/response/response57.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest58.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest58() throws Exception {
		String requestXmlPath = basePath + "/request/request58.xml";
		String respXmlPath = basePath + "/response/response58.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest59.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest59() throws Exception {
		String requestXmlPath = basePath + "/request/request59.xml";
		String respXmlPath = basePath + "/response/response59.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
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
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest60.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest60() throws Exception {
		String requestXmlPath = basePath + "/request/request60.xml";
		String respXmlPath = basePath + "/response/response60.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest61.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest61() throws Exception {
		String requestXmlPath = basePath + "/request/request61.xml";
		String respXmlPath = basePath + "/response/response61.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest62.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest62() throws Exception {
		String requestXmlPath = basePath + "/request/request62.xml";
		String respXmlPath = basePath + "/response/response62.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest63.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest63() throws Exception {
		String requestXmlPath = basePath + "/request/request63.xml";
		String respXmlPath = basePath + "/response/response63.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest64.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest64() throws Exception {
		String requestXmlPath = basePath + "/request/request64.xml";
		String respXmlPath = basePath + "/response/response64.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest65.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest65() throws Exception {
		String requestXmlPath = basePath + "/request/request65.xml";
		String respXmlPath = basePath + "/response/response65.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest66.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest66() throws Exception {
		String requestXmlPath = basePath + "/request/request66.xml";
		String respXmlPath = basePath + "/response/response66.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest67.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest67() throws Exception {
		String requestXmlPath = basePath + "/request/request67.xml";
		String respXmlPath = basePath + "/response/response67.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest68.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest68() throws Exception {
		String requestXmlPath = basePath + "/request/request68.xml";
		String respXmlPath = basePath + "/response/response68.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest69.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest69() throws Exception {
		String requestXmlPath = basePath + "/request/request69.xml";
		String respXmlPath = basePath + "/response/response69.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest7.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest7() throws Exception {
		String requestXmlPath = basePath + "/request/request7.xml";
		String respXmlPath = basePath + "/response/response7.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest70.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest70() throws Exception {
		String requestXmlPath = basePath + "/request/request70.xml";
		String respXmlPath = basePath + "/response/response70.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest71.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest71() throws Exception {
		String requestXmlPath = basePath + "/request/request71.xml";
		String respXmlPath = basePath + "/response/response71.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest72.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest72() throws Exception {
		String requestXmlPath = basePath + "/request/request72.xml";
		String respXmlPath = basePath + "/response/response72.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest73.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest73() throws Exception {
		String requestXmlPath = basePath + "/request/request73.xml";
		String respXmlPath = basePath + "/response/response73.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest74.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest74() throws Exception {
		String requestXmlPath = basePath + "/request/request74.xml";
		String respXmlPath = basePath + "/response/response74.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest75.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest75() throws Exception {
		String requestXmlPath = basePath + "/request/request75.xml";
		String respXmlPath = basePath + "/response/response75.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest76.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest76() throws Exception {
		String requestXmlPath = basePath + "/request/request76.xml";
		String respXmlPath = basePath + "/response/response76.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest77.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest77() throws Exception {
		String requestXmlPath = basePath + "/request/request77.xml";
		String respXmlPath = basePath + "/response/response77.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest78.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest78() throws Exception {
		String requestXmlPath = basePath + "/request/request78.xml";
		String respXmlPath = basePath + "/response/response78.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest79.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest79() throws Exception {
		String requestXmlPath = basePath + "/request/request79.xml";
		String respXmlPath = basePath + "/response/response79.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest8.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest8() throws Exception {
		String requestXmlPath = basePath + "/request/request8.xml";
		String respXmlPath = basePath + "/response/response8.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest80.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest80() throws Exception {
		String requestXmlPath = basePath + "/request/request80.xml";
		String respXmlPath = basePath + "/response/response80.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest81.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest81() throws Exception {
		String requestXmlPath = basePath + "/request/request81.xml";
		String respXmlPath = basePath + "/response/response81.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest82.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest82() throws Exception {
		String requestXmlPath = basePath + "/request/request82.xml";
		String respXmlPath = basePath + "/response/response82.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

	/**
	 * Testrequest9.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testrequest9() throws Exception {
		String requestXmlPath = basePath + "/request/request9.xml";
		String respXmlPath = basePath + "/response/response9.xml";
		testGetMetricsData(requestXmlPath, respXmlPath);
	}

}
