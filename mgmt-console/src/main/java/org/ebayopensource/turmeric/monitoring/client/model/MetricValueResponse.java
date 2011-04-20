package org.ebayopensource.turmeric.monitoring.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class MetricValueResponse  extends JavaScriptObject {

    protected MetricValueResponse(){
        
    }
    
    public static final native MetricValueResponse fromJSON (String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
            return null;
        }
    }-*/;
    
    public final native JsArray<MetricGraphDataJS> getReturnData () /*-{
        if (!this.getMetricValueResponse)
            return null;
        return this.getMetricValueResponse.returnData;
    }-*/;
}
