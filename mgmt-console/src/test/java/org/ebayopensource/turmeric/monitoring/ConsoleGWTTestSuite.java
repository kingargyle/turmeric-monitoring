package org.ebayopensource.turmeric.monitoring;


import junit.framework.Test;
import junit.framework.TestSuite;

import org.ebayopensource.turmeric.monitoring.client.GraphUtilGWTTestCase;
import org.ebayopensource.turmeric.monitoring.client.UITest;

import com.google.gwt.junit.tools.GWTTestSuite;



public class ConsoleGWTTestSuite extends TestSuite {
    
    /**
     * Suite.
     *
     * @return the test
     */
    public static Test suite() {
        TestSuite gwtTestSuite = new GWTTestSuite("Monitoring Console GWTTestSuite");
        gwtTestSuite.addTestSuite(GraphUtilGWTTestCase.class);
        gwtTestSuite.addTestSuite(UITest.class);
        return gwtTestSuite;
    }
}
