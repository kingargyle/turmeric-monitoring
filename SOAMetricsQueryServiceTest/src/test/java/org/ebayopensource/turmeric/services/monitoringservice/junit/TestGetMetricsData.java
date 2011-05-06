package org.ebayopensource.turmeric.services.monitoringservice.junit;

import org.custommonkey.xmlunit.XMLUnit;
import org.ebayopensource.turmeric.monitoring.util.CSVImporter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Udayasankar Natarajan
 */

public class TestGetMetricsData  {
	private static String basePath = "META-INF/data/testcases/GetMetricsData";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		CSVImporter.importCSV();
		XMLUnit.setIgnoreWhitespace(true);
	}

	@Test
	public void testrequest1() throws Exception {
		String requestXmlPath = basePath + "/request/request1.xml";
		String respXmlPath = basePath + "/response/response1.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest10() throws Exception {
		String requestXmlPath = basePath + "/request/request10.xml";
		String respXmlPath = basePath + "/response/response10.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest16() throws Exception {
		String requestXmlPath = basePath + "/request/request16.xml";
		String respXmlPath = basePath + "/response/response16.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest17() throws Exception {
		String requestXmlPath = basePath + "/request/request17.xml";
		String respXmlPath = basePath + "/response/response17.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest18() throws Exception {
		String requestXmlPath = basePath + "/request/request18.xml";
		String respXmlPath = basePath + "/response/response18.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest19() throws Exception {
		String requestXmlPath = basePath + "/request/request19.xml";
		String respXmlPath = basePath + "/response/response19.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest2() throws Exception {
		String requestXmlPath = basePath + "/request/request2.xml";
		String respXmlPath = basePath + "/response/response2.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest20() throws Exception {
		String requestXmlPath = basePath + "/request/request20.xml";
		String respXmlPath = basePath + "/response/response20.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest21() throws Exception {
		String requestXmlPath = basePath + "/request/request21.xml";
		String respXmlPath = basePath + "/response/response21.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest25() throws Exception {
		String requestXmlPath = basePath + "/request/request25.xml";
		String respXmlPath = basePath + "/response/response25.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest26() throws Exception {
		String requestXmlPath = basePath + "/request/request26.xml";
		String respXmlPath = basePath + "/response/response26.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest27() throws Exception {
		String requestXmlPath = basePath + "/request/request27.xml";
		String respXmlPath = basePath + "/response/response27.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest28() throws Exception {
		String requestXmlPath = basePath + "/request/request28.xml";
		String respXmlPath = basePath + "/response/response28.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest29() throws Exception {
		String requestXmlPath = basePath + "/request/request29.xml";
		String respXmlPath = basePath + "/response/response29.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest3() throws Exception {
		String requestXmlPath = basePath + "/request/request3.xml";
		String respXmlPath = basePath + "/response/response3.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest30() throws Exception {
		String requestXmlPath = basePath + "/request/request30.xml";
		String respXmlPath = basePath + "/response/response30.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest31() throws Exception {
		String requestXmlPath = basePath + "/request/request31.xml";
		String respXmlPath = basePath + "/response/response31.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest32() throws Exception {
		String requestXmlPath = basePath + "/request/request32.xml";
		String respXmlPath = basePath + "/response/response32.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest33() throws Exception {
		String requestXmlPath = basePath + "/request/request33.xml";
		String respXmlPath = basePath + "/response/response33.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest34() throws Exception {
		String requestXmlPath = basePath + "/request/request34.xml";
		String respXmlPath = basePath + "/response/response34.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest35() throws Exception {
		String requestXmlPath = basePath + "/request/request35.xml";
		String respXmlPath = basePath + "/response/response35.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest36() throws Exception {
		String requestXmlPath = basePath + "/request/request36.xml";
		String respXmlPath = basePath + "/response/response36.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}


	@Test
	public void testrequest4() throws Exception {
		String requestXmlPath = basePath + "/request/request4.xml";
		String respXmlPath = basePath + "/response/response4.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest43() throws Exception {
		String requestXmlPath = basePath + "/request/request43.xml";
		String respXmlPath = basePath + "/response/response43.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest44() throws Exception {
		String requestXmlPath = basePath + "/request/request44.xml";
		String respXmlPath = basePath + "/response/response44.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest45() throws Exception {
		String requestXmlPath = basePath + "/request/request45.xml";
		String respXmlPath = basePath + "/response/response45.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest46() throws Exception {
		String requestXmlPath = basePath + "/request/request46.xml";
		String respXmlPath = basePath + "/response/response46.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest47() throws Exception {
		String requestXmlPath = basePath + "/request/request47.xml";
		String respXmlPath = basePath + "/response/response47.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest48() throws Exception {
		String requestXmlPath = basePath + "/request/request48.xml";
		String respXmlPath = basePath + "/response/response48.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}


	@Test
	public void testrequest5() throws Exception {
		String requestXmlPath = basePath + "/request/request5.xml";
		String respXmlPath = basePath + "/response/response5.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest55() throws Exception {
		String requestXmlPath = basePath + "/request/request55.xml";
		String respXmlPath = basePath + "/response/response55.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest56() throws Exception {
		String requestXmlPath = basePath + "/request/request56.xml";
		String respXmlPath = basePath + "/response/response56.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest57() throws Exception {
		String requestXmlPath = basePath + "/request/request57.xml";
		String respXmlPath = basePath + "/response/response57.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest58() throws Exception {
		String requestXmlPath = basePath + "/request/request58.xml";
		String respXmlPath = basePath + "/response/response58.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest59() throws Exception {
		String requestXmlPath = basePath + "/request/request59.xml";
		String respXmlPath = basePath + "/response/response59.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest6() throws Exception {
		String requestXmlPath = basePath + "/request/request6.xml";
		String respXmlPath = basePath + "/response/response6.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest60() throws Exception {
		String requestXmlPath = basePath + "/request/request60.xml";
		String respXmlPath = basePath + "/response/response60.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest61() throws Exception {
		String requestXmlPath = basePath + "/request/request61.xml";
		String respXmlPath = basePath + "/response/response61.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest62() throws Exception {
		String requestXmlPath = basePath + "/request/request62.xml";
		String respXmlPath = basePath + "/response/response62.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest63() throws Exception {
		String requestXmlPath = basePath + "/request/request63.xml";
		String respXmlPath = basePath + "/response/response63.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest64() throws Exception {
		String requestXmlPath = basePath + "/request/request64.xml";
		String respXmlPath = basePath + "/response/response64.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest65() throws Exception {
		String requestXmlPath = basePath + "/request/request65.xml";
		String respXmlPath = basePath + "/response/response65.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest66() throws Exception {
		String requestXmlPath = basePath + "/request/request66.xml";
		String respXmlPath = basePath + "/response/response66.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest67() throws Exception {
		String requestXmlPath = basePath + "/request/request67.xml";
		String respXmlPath = basePath + "/response/response67.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest68() throws Exception {
		String requestXmlPath = basePath + "/request/request68.xml";
		String respXmlPath = basePath + "/response/response68.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest69() throws Exception {
		String requestXmlPath = basePath + "/request/request69.xml";
		String respXmlPath = basePath + "/response/response69.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest7() throws Exception {
		String requestXmlPath = basePath + "/request/request7.xml";
		String respXmlPath = basePath + "/response/response7.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest70() throws Exception {
		String requestXmlPath = basePath + "/request/request70.xml";
		String respXmlPath = basePath + "/response/response70.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest71() throws Exception {
		String requestXmlPath = basePath + "/request/request71.xml";
		String respXmlPath = basePath + "/response/response71.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest72() throws Exception {
		String requestXmlPath = basePath + "/request/request72.xml";
		String respXmlPath = basePath + "/response/response72.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest73() throws Exception {
		String requestXmlPath = basePath + "/request/request73.xml";
		String respXmlPath = basePath + "/response/response73.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest74() throws Exception {
		String requestXmlPath = basePath + "/request/request74.xml";
		String respXmlPath = basePath + "/response/response74.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest75() throws Exception {
		String requestXmlPath = basePath + "/request/request75.xml";
		String respXmlPath = basePath + "/response/response75.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest76() throws Exception {
		String requestXmlPath = basePath + "/request/request76.xml";
		String respXmlPath = basePath + "/response/response76.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest77() throws Exception {
		String requestXmlPath = basePath + "/request/request77.xml";
		String respXmlPath = basePath + "/response/response77.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest78() throws Exception {
		String requestXmlPath = basePath + "/request/request78.xml";
		String respXmlPath = basePath + "/response/response78.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest79() throws Exception {
		String requestXmlPath = basePath + "/request/request79.xml";
		String respXmlPath = basePath + "/response/response79.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest8() throws Exception {
		String requestXmlPath = basePath + "/request/request8.xml";
		String respXmlPath = basePath + "/response/response8.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest80() throws Exception {
		String requestXmlPath = basePath + "/request/request80.xml";
		String respXmlPath = basePath + "/response/response80.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest81() throws Exception {
		String requestXmlPath = basePath + "/request/request81.xml";
		String respXmlPath = basePath + "/response/response81.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest82() throws Exception {
		String requestXmlPath = basePath + "/request/request82.xml";
		String respXmlPath = basePath + "/response/response82.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest9() throws Exception {
		String requestXmlPath = basePath + "/request/request9.xml";
		String respXmlPath = basePath + "/response/response9.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

}
