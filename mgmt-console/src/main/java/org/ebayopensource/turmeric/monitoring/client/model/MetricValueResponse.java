package org.ebayopensource.turmeric.monitoring.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * The Class MetricValueResponse.
 */
public class MetricValueResponse  extends JavaScriptObject {

    /**
     * Instantiates a new metric value response.
     */
    protected MetricValueResponse(){
        
    }
    
    /**
     * From json.
     *
     * @param json the json
     * @return the metric value response
     */
    public static final native MetricValueResponse fromJSON (String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
            return null;
        }
    }-*/;
    
    /**
     * Gets the return data.
     *
     * @return the return data
     */
    public final native JsArray<MetricGraphDataJS> getReturnData () /*-{
        if (!this.getMetricValueResponse)
            return null;
        return this.getMetricValueResponse.returnData;
    }-*/;
}
