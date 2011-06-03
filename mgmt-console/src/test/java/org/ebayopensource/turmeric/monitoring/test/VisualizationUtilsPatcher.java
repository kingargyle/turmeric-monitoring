package org.ebayopensource.turmeric.monitoring.test;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.octo.gwt.test.patchers.AutomaticPatcher;
import com.octo.gwt.test.patchers.PatchClass;
import com.octo.gwt.test.patchers.PatchMethod;

@PatchClass(VisualizationUtils.class)
public class VisualizationUtilsPatcher extends AutomaticPatcher{

    @PatchMethod
    public static void loadVisualizationApi(String version, Runnable onLoad, JsArrayString packages) {
        // do nothing instead of the orginial code
    }

}
