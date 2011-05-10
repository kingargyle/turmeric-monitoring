package org.ebayopensource.turmeric.monitoring.client.util;

import java.util.Arrays;
import java.util.List;

import org.ebayopensource.turmeric.monitoring.client.model.CriteriaInfoImpl;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorCriteria;
import org.ebayopensource.turmeric.monitoring.client.model.ErrorTimeSlotData;
import org.ebayopensource.turmeric.monitoring.client.model.MetricCriteria;
import org.ebayopensource.turmeric.monitoring.client.model.MetricValue;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorCategory;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorSeverity;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.ErrorType;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Ordering;
import org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService.Perspective;
import org.ebayopensource.turmeric.monitoring.client.model.ObjectType;
import org.ebayopensource.turmeric.monitoring.client.model.SelectionContext;
import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotData;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class GraphUtil {
    public static void getSimpleGraphData(MetricsQueryService queryService, String metricName, String roleType, long aggregationPeriod,
                    final SelectionContext selectionContext, long date1, long date2, final int hourSpan,
                    AsyncCallback<List<TimeSlotData>> callback) {
        CriteriaInfoImpl criteriaInfo = new CriteriaInfoImpl();
        criteriaInfo.setMetricName(metricName);
        criteriaInfo.setServiceName(selectionContext.getSelection(ObjectType.ServiceName));
        if (selectionContext.getSelection(ObjectType.OperationName) != null) {
            criteriaInfo.setOperationName(selectionContext.getSelection(ObjectType.OperationName));
        }
        criteriaInfo.setRoleType(roleType);

        queryService.getMetricValueTrend(new MetricValue(criteriaInfo, date1, aggregationPeriod * hourSpan,
                        (int) aggregationPeriod, ""), new MetricValue(criteriaInfo, date2,
                        aggregationPeriod * hourSpan, (int) aggregationPeriod, ""), callback);
    }
    
    public static void getSimpleErrorGraphData(MetricsQueryService queryService, ErrorType errorType, ErrorCategory errorCategory,  ErrorSeverity severity, String roleType, long aggregationPeriod,
                    final SelectionContext selectionContext, long date1, long date2, final int hourSpan,
                    AsyncCallback<List<ErrorTimeSlotData>> callback) {
        String serviceName = selectionContext.getSelection(ObjectType.ServiceName);
        String operationName = selectionContext.getSelection(ObjectType.OperationName);
        String consumerName = selectionContext.getSelection(ObjectType.ConsumerName);
        List<String> serviceNames = null;
        List<String> consumerNames = null;
        List<String> operationNames = null;
        if(serviceName != null){
            serviceNames = Arrays.asList(serviceName);
        }
        if(consumerName != null){
            consumerNames = Arrays.asList(consumerName);
        }
        
        if(operationName != null){
            operationNames = Arrays.asList(operationName);
        }
        
        ErrorCriteria ec = ErrorCriteria.newErrorCriteria(errorType , serviceNames, operationNames, consumerNames, null, false, errorCategory, severity);
        MetricCriteria firstDate = new MetricCriteria("", date1, hourSpan,(int)aggregationPeriod);
        MetricCriteria secondDate =  new MetricCriteria("", date2, hourSpan,(int)aggregationPeriod);
        queryService.getErrorTrend(ec, firstDate,secondDate, callback);
        
        
    }
}
