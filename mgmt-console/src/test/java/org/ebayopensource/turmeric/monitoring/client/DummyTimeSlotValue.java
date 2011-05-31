package org.ebayopensource.turmeric.monitoring.client;

import org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue;

/**
 * The Class DummyTimeSlotValue.
 */
public class DummyTimeSlotValue implements TimeSlotValue {

    /**
     * Instantiates a new test time slot value.
     *
     * @param criteria the criteria
     * @param value the value
     * @param timeSlot the time slot
     */
    public DummyTimeSlotValue(String criteria, Double value, Long timeSlot) {
        super();
        this.criteria = criteria;
        this.value = value;
        this.timeSlot = timeSlot;
    }

    private String criteria;
    private Double value;
    private Long timeSlot;
    
    /**
     * Gets the criteria.
     *
     * @return the criteria
     * @see org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue#getCriteria()
     */
    @Override
    public String getCriteria() {
        return criteria;
    }

    /**
     * Gets the value.
     *
     * @return the value
     * @see org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue#getValue()
     */
    @Override
    public Double getValue() {
        return value;
    }

    /**
     * Gets the time slot.
     *
     * @return the time slot
     * @see org.ebayopensource.turmeric.monitoring.client.model.TimeSlotValue#getTimeSlot()
     */
    @Override
    public Long getTimeSlot() {
        return timeSlot;
    }

}
