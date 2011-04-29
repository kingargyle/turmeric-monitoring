package org.ebayopensource.turmeric.monitoring.test;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.ebayopensource.turmeric.monitoring.test.util.UtilFileReader;
import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Mock class of the SQMService. This servlet expects to receive 2 init params: responseIndex and metricName. With these 2 params the servlet will write in the response associated with these params 
 * @author manuelchinea
 *
 */
public class MockSQMSServlet extends HttpServlet {
    
    private UtilFileReader utilReader = new UtilFileReader();
    private static final long serialVersionUID = 5392552486039200492L;

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        String responseIndexStr = this.getInitParameter("responseIndex");
        int responseIndex = Integer.valueOf(responseIndexStr);
        System.err.println("responseIndexStr ="+responseIndex);
        
        String metricName = this.getInitParameter("metricName");
        System.err.println("metricName ="+metricName);
        
        Enumeration paramNames = request.getParameterNames();
        String paramName = null;
        while(paramNames.hasMoreElements()){
            paramName = (String) paramNames.nextElement();
            System.err.println("request["+paramName+"] =>{"+request.getParameter(paramName)+"}");
        }
        String responseContent = utilReader.getJsonResponseString(metricName, responseIndex);
        response.getWriter().write(responseContent);
    }
    
}
