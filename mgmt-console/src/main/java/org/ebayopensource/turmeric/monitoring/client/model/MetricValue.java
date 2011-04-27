package org.ebayopensource.turmeric.monitoring.client.model;


/**
 * Class that holds the info required for the GetMetricValueRequest complexType in the SQMS wsdl
 * 
 * @author manuelchinea
 * 
 */
public class MetricValue {
    private CriteriaInfo criteriaInfo;
    private Long startTime;
    private Long duration;
    private Integer aggregationPeriod;
    private String autoDelay;

    public MetricValue(CriteriaInfo criteriaInfo, Long startTime, Long duration, Integer aggregationPeriod,
                    String autoDelay) {
        super();
        this.criteriaInfo = criteriaInfo;
        this.startTime = startTime;
        this.duration = duration;
        this.aggregationPeriod = aggregationPeriod;
        this.autoDelay = autoDelay;
    }

    public CriteriaInfo getCriteriaInfo() {
        return criteriaInfo;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getDuration() {
        return duration;
    }

    public Integer getAggregationPeriod() {
        return aggregationPeriod;
    }

    public String getAutoDelay() {
        return autoDelay;
    }
    
    public String asRestUrl () {
        StringBuilder url = new StringBuilder("");
        if(criteriaInfo != null)
        url.append(this.criteriaInfo.asRestUrl());
        if(startTime != null)
        url.append("&ns:startTime=").append(startTime);
        if(duration != null)
        url.append("&ns:duration=").append(duration);
        if(aggregationPeriod != null)
        url.append("&ns:aggregationPeriod=").append(aggregationPeriod);
        if(autoDelay != null)
        url.append("&ns:autoDelay=").append(autoDelay);
        return url.toString();
    }   

}
