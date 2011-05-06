/*******************************************************************************
 * Copyright (c) 2006, 2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


/**
 * The Class Generator.
 */
public class Generator {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		final String basePath = "data/testcases/";
		//String srcPath = TestCaseGenerator.class.getResource(".").getPath();
		String packagePath = "org.ebayopensource.turmeric.services.monitoringservice.junit";
		String srcPath = "src/main/test" + File.separator + packagePath.replace(".", File.separator);
		
		File rootDir = new File(basePath);
		
		if (rootDir.isDirectory()) {
			String[] children = rootDir.list();
			for (int i = 0; i < children.length; i++) {
				if(children[i].equalsIgnoreCase(".svn"))
					continue;
				// Create a new test class for each directory
				File dir = new File(rootDir.getAbsolutePath() + File.separator + children[i]);
				if (dir.isDirectory()) {
					File testClassFile = new File(srcPath + File.separator + "Test" + children[i] + ".java");
					try {
						testClassFile.createNewFile();
						BufferedWriter bw = new BufferedWriter(new FileWriter(testClassFile));
						bw.write("package org.ebayopensource.turmeric.services.monitoringservice.junit;\n\n");
						//bw.write("import java.io.File;\n\n");
						bw.write("import junit.framework.TestCase;\n\n");
						bw.write("import org.junit.After;\n");
						bw.write("import org.junit.AfterClass;\n");
						bw.write("import org.junit.Before;\n");
						bw.write("import org.junit.BeforeClass;\n");
						bw.write("import org.junit.Test;\n\n");
						/*bw.write("import org.unitils.reflectionassert.ReflectionAssert;\n\n");
						bw.write("import javax.xml.bind.JAXB;\n\n");
						bw.write("import com.ebay.marketplace.services.GetMetricsMetadataRequest;\n");
						bw.write("import com.ebay.marketplace.services.GetMetricsMetadataResponse;\n");
						bw.write("import com.ebay.marketplace.services.soametricsqueryservice.SOAMetricsQueryService;\n\n");*/
						bw.write("/**\n * @author Udayasankar Natarajan\n */\n\n");
						bw.write("public class Test" + children[i] + " extends TestCase {\n");
						bw.write("\tprivate static String basePath = \"data/testcases/" +dir.getName() + "\";\n\n");
						bw.write("\t/**\n\t * @throws java.lang.Exception\n\t */\n");
						bw.write("\t@BeforeClass\n");
						bw.write("\tpublic static void setUpBeforeClass() throws Exception {\n");
						bw.write("\t}\n\n");
						bw.write("\t/**\n\t * @throws java.lang.Exception\n\t */\n");
						bw.write("\t@AfterClass\n");
						bw.write("\tpublic static void tearDownAfterClass() throws Exception {\n");
						bw.write("\t}\n\n");
						bw.write("\t/**\n\t * @throws java.lang.Exception\n\t */\n");
						bw.write("\t@Before\n");
						bw.write("\tpublic void setUp() throws Exception {\n");
						bw.write("\t}\n\n");
						bw.write("\t/**\n\t * @throws java.lang.Exception\n\t */\n");
						bw.write("\t@After\n");
						bw.write("\tpublic void tearDown() throws Exception {\n");
						bw.write("\t}\n\n");
	
						/*bw.write("\tprotected void testGetMeticsMetadata(SOAMetricsQueryService service, String reqXmlPath, String respXmlPath) {\n");
						bw.write("\t\tFile reqFile = new File (reqXmlPath);\n");
						bw.write("\t\tFile respFile = new File(respXmlPath);\n");
						bw.write("\t\tGetMetricsMetadataRequest req = JAXB.unmarshal(reqFile, GetMetricsMetadataRequest.class);\n");
						bw.write("\t\tGetMetricsMetadataResponse xmlResp = JAXB.unmarshal(respFile, GetMetricsMetadataResponse.class);\n");
						bw.write("\t\tGetMetricsMetadataResponse resp = service.getMetricsMetadata(req);\n");
						bw.write("\t\tReflectionAssert.assertReflectionEquals(xmlResp, resp);\n\t}\n");*/
						File reqDir = new File(dir.getAbsolutePath() + File.separator + "request");
						String[] reqs = reqDir.list();
						for (int j = 0; j < reqs.length; j++) {
							if(reqs[j].equalsIgnoreCase(".svn"))
								continue;
							bw.write("\t@Test\n");
							String reqName = reqs[j].substring(0, reqs[j].indexOf(".xml", 0));
							String reqNum = reqName.substring(7);
							/*String reqXmlPath = dir.getAbsolutePath() + File.separator + "request" + File.separator + "request" + reqNum + ".xml";
							reqXmlPath = reqXmlPath.replace("\\", "\\\\");
							String respXmlPath = dir.getAbsolutePath() + File.separator + "response" + File.separator + "response" + reqNum + ".xml";
							respXmlPath = respXmlPath.replace("\\", "\\\\");*/
							String reqXmlPath = "request/request" + reqNum + ".xml";
							String respXmlPath = "response/response" + reqNum + ".xml";
							bw.write("\tpublic void test" + reqName +"() {\n");
							//bw.write("\t\tSOAMetricsQueryService service = null;\n");
							bw.write("\t\tString requestXmlPath = basePath + \"/" + reqXmlPath + "\";\n");
							bw.write("\t\tString respXmlPath = basePath + \"/" + respXmlPath + "\";\n");
							//bw.write("\t\tString respXmlPath = \"" + respXmlPath + "\";\n"); 
							bw.write("\t\tUtils.test" + children[i] + "(requestXmlPath, respXmlPath);\n");
							bw.write("\t}\n\n");
						}
						bw.write("}\n");
						bw.close();
					} catch (FileNotFoundException e) {
						System.out.println(e);
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println(e);
						e.printStackTrace();
					}
					
				}
			}
		}
	}
}
