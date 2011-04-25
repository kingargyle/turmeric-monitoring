/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DummyMetricsQueryServiceImpl implements MetricsQueryService {
    
    Map<String, ErrorDetail> errorDetailsByName = new HashMap<String,ErrorDetail>();
    Map<String, ErrorDetail> errorDetailsById = new HashMap<String, ErrorDetail>();
    
    
    public static class ErrorDetailImpl implements ErrorDetail {
        protected String id;
        protected String name;
        protected String domain;
        protected String subDomain;
        protected String severity;
        protected String category;
        
        public String getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public String getDomain() {
            return domain;
        }
        public String getSubDomain() {
            return subDomain;
        }
        public String getSeverity() {
            return severity;
        }
        public String getCategory() {
            return category;
        }
    }
    
    public static class ErrorViewDataImpl implements ErrorViewData {
        private String consumer;
        private Double ratioDiff;
        private Double errorDiff;
        private Double errorCallRatio2;
        private Double errorCallRatio1;
        private Long errorCount1;
        private Long errorCount2;
        private String errorId;
        private String errorName;
        

        public void setConsumer(String consumer) {
            this.consumer = consumer;
        }

        public void setRatioDiff(double ratioDiff) {
            this.ratioDiff = Double.valueOf(ratioDiff);
        }

        public void setErrorDiff(double errorDiff) {
            this.errorDiff = Double.valueOf(errorDiff);
        }

        public void setErrorCallRatio2(double errorCallRatio2) {
            this.errorCallRatio2 = Double.valueOf(errorCallRatio2);
        }

        public void setErrorCallRatio1(double errorCallRatio1) {
            this.errorCallRatio1 = Double.valueOf(errorCallRatio1);
        }

        public void setErrorCount1(long errorCount1) {
            this.errorCount1 = Long.valueOf(errorCount1);
        }

        public void setErrorCount2(long errorCount2) {
            this.errorCount2 = Long.valueOf(errorCount2);
        }
        
        public void setErrorId(String id) {
            this.errorId = id;
        }

        public void setErrorName (String name) {
            this.errorName = name;
        }
        
        public String getConsumer() {
            return consumer;
        }

        public Double getRatioDiff() {
            return ratioDiff;
        }

        public Double getErrorDiff() {
            return errorDiff;
        }

        public Double getErrorCallRatio2() {
            return errorCallRatio2;
        }

        public Double getErrorCallRatio1() {
            return errorCallRatio1;
        }

        public Long getErrorCount1() {
            return errorCount1;
        }

        public Long getErrorCount2() {
            return errorCount2;
        }

        public String getErrorId() {
            return errorId;
        }

        public String getErrorName() {
            return errorName;
        }
    }
    
    /**
     * 
     * TimeSlotValue
     *
     */
    public static class TimeSlotValueImpl implements TimeSlotValue {
        String criteria;
        Long timeslot;
        Double value;
       
        public String getCriteria() {
           return criteria;
        }
        
        public Long getTimeSlot() {
           return timeslot;
        }
       
        public Double getValue() {
           return value;
        }

        public void setCriteria(String criteria) {
            this.criteria = criteria;
        }

        public void setTimeslot(Long timeslot) {
            this.timeslot = timeslot;
        }

        public void setValue(Double value) {
            this.value = value;
        }  
    }
    
    
    public DummyMetricsQueryServiceImpl () {
        ErrorDetailImpl ed0 = new ErrorDetailImpl();
        ed0.id="100";
        ed0.name="Err0";
        ed0.category = ErrorCategory.Application.toString();
        ed0.domain = "Foo";
        ed0.subDomain = "Bar";
        ed0.severity = "Warning";
        errorDetailsByName.put("Err0", ed0);
        errorDetailsById.put("100", ed0);
        
        ErrorDetailImpl ed1 = new ErrorDetailImpl();
        ed1.id="101";
        ed1.name="Err1";
        ed1.category = ErrorCategory.Request.toString();
        ed1.domain = "Foo";
        ed1.subDomain = "Bar";
        ed1.severity = "Critical";
        errorDetailsByName.put("Err1", ed1);
        errorDetailsById.put("101", ed1);
        
        ErrorDetailImpl ed2 = new ErrorDetailImpl();
        ed2.id="102";
        ed2.name="Err2";
        ed2.category = ErrorCategory.System.toString();
        ed2.domain = "Foo";
        ed2.subDomain = "Bar";
        ed2.severity = "Error";
        errorDetailsByName.put("Err2", ed2);
        errorDetailsById.put("102", ed2);
        
    }
    
    public String getMetricDataDownloadUrl(MetricCriteria mc, MetricResourceCriteria mrc) {

        return "";
    }

    public void getMetricData(MetricCriteria criteria,
                              MetricResourceCriteria resourceCriteria,
                              AsyncCallback<MetricData> callback) {

        MetricData result = new MetricData();
        result.setRestUrl(URL.encode(MetricsDataRequest.getRestURL(criteria, resourceCriteria)));
        result.setMetricCriteria(criteria);
        result.setMetricResourceCriteria(resourceCriteria);       
        
        if ("CallCount".equals(criteria.metricName)) {
            List<MetricGroupData> metrics = new ArrayList<MetricGroupData>();
            result.setReturnData(metrics);
            int max = (resourceCriteria.resourceEntityRequests != null && resourceCriteria.resourceEntityRequests.size() > 1? 1 : 10);
            for (int i=0;i<max;i++) {
                MetricGroupDataImpl rd = new MetricGroupDataImpl();
                CriteriaInfoImpl ci = new CriteriaInfoImpl();
                rd.setCriteriaInfo(ci);
                metrics.add(rd);
                if (resourceCriteria.resourceEntityResponseType.equals(Entity.Operation)) {
                    //Was a specific operation requested?
                    if (resourceCriteria.resourceEntityRequests != null) {
                        for (ResourceEntityRequest r:resourceCriteria.resourceEntityRequests) {
                            if (r.resourceEntityType.equals(Entity.Operation))
                                ci.setOperationName(r.resourceEntityNames.get(0));
                        }
                    }
                    //Make up some
                    if (ci.getOperationName() == null)
                        ci.setOperationName("Op"+i);
                } else if (resourceCriteria.resourceEntityResponseType.equals(Entity.Service)) {
                    if (resourceCriteria.resourceEntityRequests != null) {
                        for (ResourceEntityRequest r:resourceCriteria.resourceEntityRequests) {
                            if (r.resourceEntityType.equals(Entity.Service))
                                ci.setServiceName(r.resourceEntityNames.get(0));
                        }
                    }
                    if (ci.getServiceName() == null)
                        ci.setServiceName("Service"+i);
                } else if (resourceCriteria.resourceEntityResponseType.equals(Entity.Consumer)) {
                        ci.setConsumerName("Consumer"+i);
                }

                int s1 = Random.nextInt(1000);
                rd.setCount1(new Double(s1).toString());
                int s2 = Random.nextInt(1000);
                rd.setCount2(new Double(s2).toString());
                int s3 = Random.nextInt(100);
                rd.setDiff(new Double(s3).toString());            
               
            }
        } else if ("ResponseTime".equals(criteria.metricName)) {
            List<MetricGroupData> metrics = new ArrayList<MetricGroupData>();
            result.setReturnData(metrics);
            int max = (resourceCriteria.resourceEntityRequests != null && resourceCriteria.resourceEntityRequests.size() > 1? 1 : 10);
            
            for (int i=0;i<max;i++) {
               MetricGroupDataImpl rd = new MetricGroupDataImpl();
               CriteriaInfoImpl ci = new CriteriaInfoImpl();
               rd.setCriteriaInfo(ci);
               metrics.add(rd);
                if (resourceCriteria.resourceEntityResponseType.equals(Entity.Service)) {
                    //Was a specific service requested?
                    if (resourceCriteria.resourceEntityRequests != null) {
                        for (ResourceEntityRequest r:resourceCriteria.resourceEntityRequests) {
                            if (r.resourceEntityType.equals(Entity.Service))
                                ci.setServiceName(r.resourceEntityNames.get(0));
                        }
                    }
                    //otherwise make up some
                    if (ci.getServiceName() == null)
                        ci.setServiceName("Service "+i);
                } else if (resourceCriteria.resourceEntityResponseType.equals(Entity.Operation)) {
                    //Was a specific operation requested?
                    if (resourceCriteria.resourceEntityRequests != null) {
                        for (ResourceEntityRequest r:resourceCriteria.resourceEntityRequests) {
                            if (r.resourceEntityType.equals(Entity.Operation))
                                ci.setOperationName(r.resourceEntityNames.get(0));
                        }
                    }
                    //otherwise make up some
                    if (ci.getOperationName() == null)
                        ci.setOperationName("Op"+i);
                    
                } else if (resourceCriteria.resourceEntityResponseType.equals(Entity.Consumer)) {
                    //Was a specific consumer requested?
                    if (resourceCriteria.resourceEntityRequests != null) {
                        for (ResourceEntityRequest r:resourceCriteria.resourceEntityRequests) {
                            if (r.resourceEntityType.equals(Entity.Consumer))
                                ci.setConsumerName(r.resourceEntityNames.get(0));
                        }
                    }
                    //otherwise make up some
                    if (ci.getConsumerName() == null)
                        ci.setConsumerName("Consumer"+i);
                }

                  
                double s1 = Random.nextInt(5000000)/1.0;
                rd.setCount1(new Double(s1).toString());
                double s2 = Random.nextInt(5000000)/1.0;
                rd.setCount2(new Double(s2).toString());
                int s3 = Random.nextInt(100);
                rd.setDiff(new Double(s3).toString());            
                
            }
            
        } else if ("ErrorCount".equals(criteria.metricName)) {
            List<MetricGroupData> metrics = new ArrayList<MetricGroupData>();
            result.setReturnData(metrics);
            
            
            if (resourceCriteria.resourceEntityResponseType.equals(Entity.Consumer)) {
                //int max = (resourceCriteria.resourceEntityRequests != null && resourceCriteria.resourceEntityRequests.size() > 1? 2 : 10);
                int max = 2;
                for (int i=0;i<max;i++) {
                    MetricGroupDataImpl rd = new MetricGroupDataImpl();
                    CriteriaInfoImpl ci = new CriteriaInfoImpl();
                    rd.setCriteriaInfo(ci);
                    metrics.add(rd);
                    ci.setMetricName("Err"+i);
                    ci.setConsumerName("Consumer"+i);
                    
                    int s1 = Random.nextInt(1000);
                    rd.setCount1(new Double(s1).toString());
                    int s2 = Random.nextInt(1000);
                    rd.setCount2(new Double(s2).toString());
                    int s3 = Random.nextInt(100);
                    rd.setDiff(new Double(s3).toString());            
                }
            } else if (resourceCriteria.resourceEntityResponseType.equals(Entity.Service)) {
                //int max = (resourceCriteria.resourceEntityRequests != null && resourceCriteria.resourceEntityRequests.size() > 1? 2 : 10);
                String service =  null;
                if (resourceCriteria.resourceEntityRequests != null) {
                    for (ResourceEntityRequest r:resourceCriteria.resourceEntityRequests) {
                        if (r.resourceEntityType.equals(Entity.Service))
                            service = r.resourceEntityNames.get(0);
                    }
                }
                GWT.log("service="+service);

                int max = 2;
                for (int i=0;i<max;i++) {
                    MetricGroupDataImpl rd = new MetricGroupDataImpl();
                    CriteriaInfoImpl ci = new CriteriaInfoImpl();
                    rd.setCriteriaInfo(ci);
                    metrics.add(rd);
                    ci.setMetricName("Err"+i);
                    if (service == null)
                        ci.setServiceName("Service"+i);
                    else
                        ci.setServiceName(service);

                    int s1 = Random.nextInt(1000);
                    rd.setCount1(new Double(s1).toString());
                    int s2 = Random.nextInt(1000);
                    rd.setCount2(new Double(s2).toString());
                    int s3 = Random.nextInt(100);
                    rd.setDiff(new Double(s3).toString());            
                }
            }  else if (resourceCriteria.resourceEntityResponseType.equals(Entity.Operation)) {
                //int max = (resourceCriteria.resourceEntityRequests != null && resourceCriteria.resourceEntityRequests.size() > 1? 2 : 10);
                String operation =  null;
              
                if (resourceCriteria.resourceEntityRequests != null) {
                    for (ResourceEntityRequest r:resourceCriteria.resourceEntityRequests) {
                        if (r.resourceEntityType.equals(Entity.Operation))
                            operation = r.resourceEntityNames.get(0);
                    }
                }
                GWT.log("Op="+operation);
                int max = 2;
                for (int i=0;i<max;i++) {
                    MetricGroupDataImpl rd = new MetricGroupDataImpl();
                    CriteriaInfoImpl ci = new CriteriaInfoImpl();
                    rd.setCriteriaInfo(ci);
                    metrics.add(rd);
                    ci.setMetricName("Err"+i);
                    if (operation == null)
                        ci.setOperationName("Op"+i);
                    else
                        ci.setOperationName(operation);

                    int s1 = Random.nextInt(1000);
                    rd.setCount1(new Double(s1).toString());
                    int s2 = Random.nextInt(1000);
                    rd.setCount2(new Double(s2).toString());
                    int s3 = Random.nextInt(100);
                    rd.setDiff(new Double(s3).toString());            
                }
            } else if (resourceCriteria.resourceEntityResponseType.equals(Entity.Error)) {
                //int max = ( resourceCriteria.resourceEntityRequests!= null && resourceCriteria.resourceEntityRequests.size() > 1? 2 : 10);
                
                String service = null;
                String operation = null;
                if (resourceCriteria.resourceEntityRequests != null) {
                    for (ResourceEntityRequest r:resourceCriteria.resourceEntityRequests) {
                        if (r.resourceEntityType.equals(Entity.Operation))
                            operation = r.resourceEntityNames.get(0);
                        if (r.resourceEntityType.equals(Entity.Service))
                            service = r.resourceEntityNames.get(0);
                    }
                }
                int max = 2;
                for (int i=0;i<max;i++) {
                    MetricGroupDataImpl rd = new MetricGroupDataImpl();
                    CriteriaInfoImpl ci = new CriteriaInfoImpl();
                    rd.setCriteriaInfo(ci);
                    metrics.add(rd);
                    ci.setMetricName("Err"+i);
                    if (service == null)
                        ci.setServiceName("Service"+i);
                    else
                        ci.setServiceName(service);
                    if (operation == null)
                        ci.setOperationName("Op"+i); 
                    else
                        ci.setOperationName(operation);
                    int s1 = Random.nextInt(1000);
                    rd.setCount1(new Double(s1).toString());
                    int s2 = Random.nextInt(1000);
                    rd.setCount2(new Double(s2).toString());
                    int s3 = Random.nextInt(100);
                    rd.setDiff(new Double(s3).toString());            
                }
            }
        } 

        callback.onSuccess(result);
    }

	public void getServices(AsyncCallback<Map<String, Set<String>>> callback) {
		
		HashMap<String, Set<String>> testData = new HashMap<String, Set<String>>();
		testData.put("FindingService", new HashSet<String>(Arrays.asList(new String[]{"opA", "opB", "opC"})));
		testData.put("SomeOtherService", new HashSet<String>(Arrays.asList(new String[]{"opA", "opB"})));
		testData.put("YetAnotherService", new HashSet<String>(Arrays.asList(new String[]{"opA", "opB"})));;
		for (int i=0; i< 20; i++) {
			testData.put("Service"+i, new HashSet<String>(Arrays.asList(new String[]{"opA", "opB", "opC"})));
		}
		
		callback.onSuccess(testData);
	}

     public void getErrorData(ErrorCriteria ec, MetricCriteria mc,
                             AsyncCallback<ErrorMetricData> callback) {
        ErrorMetricData result = new ErrorMetricData();
        result.setRestUrl(URL.encode(ErrorMetricsDataRequest.getRestURL(ec, mc)));
        result.setErrorCriteria(ec);
        result.setMetricCriteria(mc);

        List<ErrorViewData> results = new ArrayList<ErrorViewData>();
        result.returnData = results;
        //specific error requested
        if (ec.e != null) {
            //return info about the consumers
            for (int i=0; i<10;i++) {
                ErrorViewDataImpl evd = new ErrorViewDataImpl();
                evd.setConsumer("Consumer"+i);
                evd.setErrorCallRatio1(Random.nextDouble()*10.0);
                evd.setErrorCallRatio2(Random.nextDouble()*10.0);
                evd.setErrorCount1(Random.nextInt(1000));
                evd.setErrorCount2(Random.nextInt(1000));
                results.add(evd);
            }
        } else {
            GWT.log("no error requested");
            //assume for now that errors are required rather than consumers
            for (Map.Entry<String, ErrorDetail> entry:errorDetailsByName.entrySet()) {
                if (ec.category != null) {
                    if (ec.category.toString().equalsIgnoreCase(entry.getValue().getCategory())) {
                        ErrorViewDataImpl evd = new ErrorViewDataImpl();
                        evd.setErrorId(entry.getValue().getId());
                        evd.setErrorName(entry.getValue().getName());
                        evd.setErrorCallRatio1(Random.nextDouble()*10.0);
                        evd.setErrorCallRatio2(Random.nextDouble()*10.0);
                        evd.setErrorCount1(Random.nextInt(1000));
                        evd.setErrorCount2(Random.nextInt(1000));
                        results.add(evd);
                    }
                } else if (ec.severity != null) {
                    if (ec.severity.toString().equalsIgnoreCase(entry.getValue().getSeverity())) {
                        ErrorViewDataImpl evd = new ErrorViewDataImpl();
                        evd.setErrorId(entry.getValue().getId());
                        evd.setErrorName(entry.getValue().getName());
                        evd.setErrorCallRatio1(Random.nextDouble()*10.0);
                        evd.setErrorCallRatio2(Random.nextDouble()*10.0);
                        evd.setErrorCount1(Random.nextInt(1000));
                        evd.setErrorCount2(Random.nextInt(1000));
                        results.add(evd);
                    }
                } else {
                    ErrorViewDataImpl evd = new ErrorViewDataImpl();
                    evd.setErrorName(entry.getValue().getName());
                    evd.setErrorId(entry.getValue().getId());
                    evd.setErrorCallRatio1(Random.nextDouble()*10.0);
                    evd.setErrorCallRatio2(Random.nextDouble()*10.0);
                    evd.setErrorCount1(Random.nextInt(1000));
                    evd.setErrorCount2(Random.nextInt(1000));
                    results.add(evd);
                }
                        
            }
        }
        
        callback.onSuccess(result);
    }

    
    
    
    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService#getErrorDataDownloadUrl(org.ebayopensource.turmeric.monitoring.client.model.ErrorCriteria, org.ebayopensource.turmeric.monitoring.client.model.MetricCriteria)
     */
    public String getErrorDataDownloadUrl(ErrorCriteria ec, MetricCriteria mc) {
        return "";
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService#getErrorDetail(java.lang.String, java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    public void getErrorDetail(String selectedErrId, String selectedErrName,
                               String service,
                               AsyncCallback<ErrorDetail> callback) {
        GWT.log("getErrorDetail, id="+selectedErrId+", name="+selectedErrName+" service="+service);
        ErrorDetail ed = errorDetailsByName.get(selectedErrName);
     
        callback.onSuccess(ed);
    }

    /**
     * @see org.ebayopensource.turmeric.monitoring.client.model.MetricsQueryService#getErrorTimeSlotData(org.ebayopensource.turmeric.monitoring.client.model.ErrorCriteria, org.ebayopensource.turmeric.monitoring.client.model.MetricCriteria, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    public void getErrorTimeSlotData(ErrorCriteria ec, MetricCriteria mc,
                                     AsyncCallback<ErrorTimeSlotData> callback) {
        // Auto-generated method stub
        
    }

    @Override
    public void getServiceMetricValueTrend(MetricValue firstDate, MetricValue secondDate,
                    AsyncCallback<List<TimeSlotData>> callback) {
        // TODO Auto-generated method stub
        
    }

}
