/**
 * 
 */
package org.ebayopensource.turmeric.monitoring.client.model.policy;

/**
 * @author jose
 * 
 */
public class ExpressionImpl implements Expression {

	private Long id;
	private String name;
	private String comment;
	private PrimitiveValue primitiveValue;
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setPrimitiveValue(PrimitiveValue primitiveValue) {
		this.primitiveValue = primitiveValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ebayopensource.turmeric.monitoring.client.model.policy.Expression
	 * #getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ebayopensource.turmeric.monitoring.client.model.policy.Expression
	 * #getName()
	 */
	@Override
	public String getName() {

		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ebayopensource.turmeric.monitoring.client.model.policy.Expression
	 * #getComment()
	 */
	@Override
	public String getComment() {
		return comment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ebayopensource.turmeric.monitoring.client.model.policy.Expression
	 * #getPrimitiveValue()
	 */
	@Override
	public PrimitiveValue getPrimitiveValue() {
		return primitiveValue;
	}

}
