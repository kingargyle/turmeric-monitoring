package org.ebayopensource.turmeric.monitoring.test;

import com.google.gwt.visualization.client.DataTable;
import com.octo.gwt.test.patchers.PatchClass;
import com.octo.gwt.test.patchers.PatchMethod;

@PatchClass(DataTable.class)
public class DataTablePatcher {

    @PatchMethod
    public static DataTable create() {
        return null;
    }

}
