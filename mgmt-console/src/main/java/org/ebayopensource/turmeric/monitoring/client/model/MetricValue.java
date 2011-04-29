package org.ebayopensource.turmeric.monitoring.client.model;


/**
 * Class that holds the info required for the GetMetricValueRequest complexType in the SQMS wsdl.
 *
 * @author manuelchinea
 */
public class MetricValue {
    private CriteriaInfo criteriaInfo;
    private Long startTime;
    private Long duration;
    private Integer aggregationPeriod;
    private String autoDelay;

    /**
     * Instantiates a new metric value.
     *
     * @param criteriaInfo the criteria info
     * @param startTime the start time
     * @param duration the duration
     * @param aggregationPeriod the aggregation period
     * @param autoDelay the auto delay
     */
    public MetricValue(CriteriaInfo criteriaInfo, Long startTime, Long duration, Integer aggregationPeriod,
                    String autoDelay) {
        super();
        this.criteriaInfo = criteriaInfo;
        this.startTime = startTime;
        this.duration = duration;
        this.aggregationPeriod = aggregationPeriod;
        this.autoDelay = autoDelay;
    }

    /**
     * Gets the criteria info.
     *
     * @return the criteria info
     */
    public CriteriaInfo getCriteriaInfo() {
        return criteriaInfo;
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public Long getStartTime() {
        return startTime;
    }

    /**
     * Gets the duration.
     *
     * @return the duration
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * Gets the aggregation period.
     *
     * @return the aggregation period
     */
    public Integer getAggregationPeriod() {
        return aggregationPeriod;
    }

    /**
     * Gets the auto delay.
     *
     * @return the auto delay
     */
    public String getAutoDelay() {
        return autoDelay;
    }
    
    /**
     * As rest url.
     *
     * @return the string
     */
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
