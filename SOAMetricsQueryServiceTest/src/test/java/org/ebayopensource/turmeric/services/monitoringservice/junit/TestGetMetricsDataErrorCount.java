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

public class TestGetMetricsDataErrorCount  {
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
	public void testrequest11() throws Exception {
		String requestXmlPath = basePath + "/request/request11.xml";
		String respXmlPath = basePath + "/response/response11.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest12() throws Exception {
		String requestXmlPath = basePath + "/request/request12.xml";
		String respXmlPath = basePath + "/response/response12.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest13() throws Exception {
		String requestXmlPath = basePath + "/request/request13.xml";
		String respXmlPath = basePath + "/response/response13.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest14() throws Exception {
		String requestXmlPath = basePath + "/request/request14.xml";
		String respXmlPath = basePath + "/response/response14.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest15() throws Exception {
		String requestXmlPath = basePath + "/request/request15.xml";
		String respXmlPath = basePath + "/response/response15.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}



	@Test
	public void testrequest22() throws Exception {
		String requestXmlPath = basePath + "/request/request22.xml";
		String respXmlPath = basePath + "/response/response22.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest23() throws Exception {
		String requestXmlPath = basePath + "/request/request23.xml";
		String respXmlPath = basePath + "/response/response23.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest24() throws Exception {
		String requestXmlPath = basePath + "/request/request24.xml";
		String respXmlPath = basePath + "/response/response24.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}


	@Test
	public void testrequest37() throws Exception {
		String requestXmlPath = basePath + "/request/request37.xml";
		String respXmlPath = basePath + "/response/response37.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest38() throws Exception {
		String requestXmlPath = basePath + "/request/request38.xml";
		String respXmlPath = basePath + "/response/response38.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest39() throws Exception {
		String requestXmlPath = basePath + "/request/request39.xml";
		String respXmlPath = basePath + "/response/response39.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest40() throws Exception {
		String requestXmlPath = basePath + "/request/request40.xml";
		String respXmlPath = basePath + "/response/response40.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest41() throws Exception {
		String requestXmlPath = basePath + "/request/request41.xml";
		String respXmlPath = basePath + "/response/response41.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest42() throws Exception {
		String requestXmlPath = basePath + "/request/request42.xml";
		String respXmlPath = basePath + "/response/response42.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}


	@Test
	public void testrequest49() throws Exception {
		String requestXmlPath = basePath + "/request/request49.xml";
		String respXmlPath = basePath + "/response/response49.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest50() throws Exception {
		String requestXmlPath = basePath + "/request/request50.xml";
		String respXmlPath = basePath + "/response/response50.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest51() throws Exception {
		String requestXmlPath = basePath + "/request/request51.xml";
		String respXmlPath = basePath + "/response/response51.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest52() throws Exception {
		String requestXmlPath = basePath + "/request/request52.xml";
		String respXmlPath = basePath + "/response/response52.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest53() throws Exception {
		String requestXmlPath = basePath + "/request/request53.xml";
		String respXmlPath = basePath + "/response/response53.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

	@Test
	public void testrequest54() throws Exception {
		String requestXmlPath = basePath + "/request/request54.xml";
		String respXmlPath = basePath + "/response/response54.xml";
		Utils.testGetMetricsData(requestXmlPath, respXmlPath);
	}

}
