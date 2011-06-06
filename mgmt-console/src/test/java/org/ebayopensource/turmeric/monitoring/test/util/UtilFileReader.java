package org.ebayopensource.turmeric.monitoring.test.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import junit.framework.Assert;


/**
 * Utility class to handle the reading of files stored under src/test/resources/ .
 * @author manuelchinea
 *
 */
public class UtilFileReader implements Serializable{

    /**
     * Reads the response file stored in src/test/resources/metricName/response<index>.txt.
     * @param metricName The name of the metric to search the response for
     * @param index The index of the response file for the metric
     * @return The contents of the response file as a String
     */
    public String getResponseString(String metricName, int index) {
        return this.getFileStringContent(metricName, "response" + index + ".txt");
    }

    /**
     * Reads the request file stored in src/test/resources/metricName/request<index>.txt.
     * @param metricName The name of the metric to search the request for
     * @param index The index of the request file for the metric
     * @return The contents of the request file as a String
     */
    public String getRequestString(String metricName, int index) {
        return this.getFileStringContent(metricName, "request" + index + ".txt");
    }

    

    /**
     * Reads the response file stored in src/test/resources/metricName/response<index>.json.
     * @param metricName The name of the metric to search the response for
     * @param responseIndex The index of the response file for the metric
     * @return The contents of the response file as a String
     */
    public String getJsonResponseString(String metricName, int responseIndex) {
        return this.getFileStringContent(metricName, "response" + responseIndex + ".json");
    }
    
    /**
     * Gets the file string content.
     *
     * @param metricName the metric name
     * @param filename the filename
     * @return the file string content
     */
    private String getFileStringContent(String metricName, String filename) {
        StringBuilder strBuilder = new StringBuilder();

        try {
            BufferedReader in = new BufferedReader(new FileReader("src/test/resources/" + metricName + "/" + filename));
            String str = null;
            while ((str = in.readLine()) != null) {
                strBuilder.append(str).append("\n");
            }
            in.close();
        }
        catch (IOException e) {
            Assert.fail("Could not read the request parameters");
        }
        return strBuilder.toString().trim();
    }
}
