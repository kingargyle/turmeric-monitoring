
package org.ebayopensource.turmeric.errorlibrary.turmericmonitoring;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;

public class ErrorDataCollection {

    private final static String ORGANIZATION = "Turmeric";
    public final static CommonErrorData SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR = createCommonErrorData(50002L, (ErrorSeverity.ERROR), (ErrorCategory.SYSTEM), "SVC_SOAMETRICSQUERYSERVICE_INTERNAL_ERROR", "TurmericMonitoring", "", "");

    private static CommonErrorData createCommonErrorData(long errorId, ErrorSeverity severity, ErrorCategory category, String errorName, String domain, String subDomain, String errorGroup) {
        CommonErrorData errorData = new CommonErrorData();
        errorData.setErrorId(errorId);
        errorData.setSeverity(severity);
        errorData.setCategory(category);
        errorData.setSubdomain(subDomain);
        errorData.setDomain(domain);
        errorData.setErrorGroups(errorGroup);
        errorData.setErrorName(errorName);
        errorData.setOrganization(ORGANIZATION);
        return errorData;
    }

}
