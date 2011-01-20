package org.ebayopensource.turmeric.monitoring.client.model.policy;


public class ConditionImpl  implements Condition{

	private Expression expression;
	
	public ConditionImpl(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public Expression getExpression() {
		return expression;
	}

	

	public void setExpressionList(Expression expression) {
		this.expression = expression;
	}

}
