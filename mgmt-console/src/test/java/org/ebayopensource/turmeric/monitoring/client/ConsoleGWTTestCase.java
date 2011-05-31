package org.ebayopensource.turmeric.monitoring.client;

import com.google.gwt.junit.client.GWTTestCase;


/**
 * The Class ConsoleGWTTestCase.
 */
public abstract class ConsoleGWTTestCase extends GWTTestCase  {

    /**
     * Instantiates a new console gwt test case.
     */
    public ConsoleGWTTestCase() {
        super();
    }

    /**
     * Gets the module name.
     *
     * @return the module name
     * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
     */
    @Override
    public String getModuleName() {
        return "org.ebayopensource.turmeric.monitoring.ConsoleJunit";
    }

}