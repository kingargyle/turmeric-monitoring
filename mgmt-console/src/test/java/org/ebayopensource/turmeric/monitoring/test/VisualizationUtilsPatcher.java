package org.ebayopensource.turmeric.monitoring.test;

import com.google.gwt.visualization.client.VisualizationUtils;
import com.octo.gwt.test.patchers.AutomaticPatcher;
import com.octo.gwt.test.patchers.PatchClass;
import com.octo.gwt.test.patchers.PatchMethod;

@PatchClass(VisualizationUtils.class)
public class VisualizationUtilsPatcher {

    @PatchMethod
    public static void loadVisualizationApi(Runnable onLoad, String... packages) {
        // do nothing instead of the original code
    }

}
