/**
 * 
 */
package org.ebayopensource.turmeric.monitoring.client.model.policy;

/**
 * @author jose
 *
 */
public class PrimitiveValueImpl implements PrimitiveValue {
	private Long id;
	private  SupportedPrimitive type;
	private String value;
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setType(SupportedPrimitive type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}


	
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PrimitiveValue#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PrimitiveValue#getType()
	 */
	@Override
	public SupportedPrimitive getType() {

		return type;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.monitoring.client.model.policy.PrimitiveValue#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

}
