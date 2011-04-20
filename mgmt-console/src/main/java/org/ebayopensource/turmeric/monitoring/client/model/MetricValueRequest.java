package org.ebayopensource.turmeric.monitoring.client.model;

import com.google.gwt.core.client.GWT;

public class MetricValueRequest {
    public static String getRestURL(MetricValue mv) {
        StringBuilder url = new StringBuilder(GWT.getModuleBaseURL()+"smqs?");
        url.append(MetricsQueryService.SERVICE_NAME_HEADER_VALUE);
        url.append("&").append(MetricsQueryService.OPERATION_NAME_HEADER).append("=getMetricValue");
        url.append("&").append(MetricsQueryService.USECASE_HEADER_VALUE);
        url.append("&").append(MetricsQueryService.NV_DATA_FORMAT_HEADER_VALUE);
        url.append("&").append(MetricsQueryService.JSON_RESPONSE_FORMAT_HEADER_VALUE);
        url.append("&").append(MetricsQueryService.NV_NAMESPACE);
        url.append(mv.asRestUrl());
        return url.toString();
    }
}
