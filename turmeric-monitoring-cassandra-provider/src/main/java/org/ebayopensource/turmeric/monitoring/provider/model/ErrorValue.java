package org.ebayopensource.turmeric.monitoring.provider.model;

public class ErrorValue<K> extends Model<K> {

	/** The aggregation period. */
	protected Long aggregationPeriod;

	/** The category. */
	protected String category;

	/** The consumer name. */
	protected String consumerName;

	/** The domain. */
	protected String domain;

	/** The error id. */
	protected Long errorId;

	/** The error message. */
	protected String errorMessage;

	/** The key. */
	protected K key;

	/** The name. */
	protected String name;

	/** The operation name. */
	protected String operationName;

	/** The organization. */
	protected String organization;

	/** The random number. */
	protected int randomNumber;

	/** The server name. */
	protected String serverName;

	/** The server side. */
	protected String serverSide;

	/** The service admin name. */
	protected String serviceAdminName;

	/** The severity. */
	protected String severity;

	/** The sub domain. */
	protected String subDomain;

	/** The time stamp. */
	protected Long tstamp;

	public Long getAggregationPeriod() {
		return aggregationPeriod;
	}

	public void setAggregationPeriod(Long aggregationPeriod) {
		this.aggregationPeriod = aggregationPeriod;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Long getErrorId() {
		return errorId;
	}

	public void setErrorId(Long errorId) {
		this.errorId = errorId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public int getRandomNumber() {
		return randomNumber;
	}

	public void setRandomNumber(int randomNumber) {
		this.randomNumber = randomNumber;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerSide() {
		return serverSide;
	}

	public void setServerSide(String serverSide) {
		this.serverSide = serverSide;
	}

	public String getServiceAdminName() {
		return serviceAdminName;
	}

	public void setServiceAdminName(String serviceAdminName) {
		this.serviceAdminName = serviceAdminName;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getSubDomain() {
		return subDomain;
	}

	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}

	public Long getTstamp() {
		return tstamp;
	}

	public void setTstamp(Long tstamp) {
		this.tstamp = tstamp;
	}

	public ErrorValue(K keyType) {
		super(keyType);
	}

}
