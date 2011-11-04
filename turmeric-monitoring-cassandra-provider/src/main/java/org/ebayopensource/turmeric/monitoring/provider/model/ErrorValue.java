package org.ebayopensource.turmeric.monitoring.provider.model;

/**
 * The Class ErrorValue.
 * 
 * @param <K>
 *           the key type
 */
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

   /**
    * Gets the aggregation period.
    * 
    * @return the aggregation period
    */
   public Long getAggregationPeriod() {
      return aggregationPeriod;
   }

   /**
    * Sets the aggregation period.
    * 
    * @param aggregationPeriod
    *           the new aggregation period
    */
   public void setAggregationPeriod(Long aggregationPeriod) {
      this.aggregationPeriod = aggregationPeriod;
   }

   /**
    * Gets the category.
    * 
    * @return the category
    */
   public String getCategory() {
      return category;
   }

   /**
    * Sets the category.
    * 
    * @param category
    *           the new category
    */
   public void setCategory(String category) {
      this.category = category;
   }

   /**
    * Gets the consumer name.
    * 
    * @return the consumer name
    */
   public String getConsumerName() {
      return consumerName;
   }

   /**
    * Sets the consumer name.
    * 
    * @param consumerName
    *           the new consumer name
    */
   public void setConsumerName(String consumerName) {
      this.consumerName = consumerName;
   }

   /**
    * Gets the domain.
    * 
    * @return the domain
    */
   public String getDomain() {
      return domain;
   }

   /**
    * Sets the domain.
    * 
    * @param domain
    *           the new domain
    */
   public void setDomain(String domain) {
      this.domain = domain;
   }

   /**
    * Gets the error id.
    * 
    * @return the error id
    */
   public Long getErrorId() {
      return errorId;
   }

   /**
    * Sets the error id.
    * 
    * @param errorId
    *           the new error id
    */
   public void setErrorId(Long errorId) {
      this.errorId = errorId;
   }

   /**
    * Gets the error message.
    * 
    * @return the error message
    */
   public String getErrorMessage() {
      return errorMessage;
   }

   /**
    * Sets the error message.
    * 
    * @param errorMessage
    *           the new error message
    */
   public void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public K getKey() {
      return key;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setKey(K key) {
      this.key = key;
   }

   /**
    * Gets the name.
    * 
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * Sets the name.
    * 
    * @param name
    *           the new name
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * Gets the operation name.
    * 
    * @return the operation name
    */
   public String getOperationName() {
      return operationName;
   }

   /**
    * Sets the operation name.
    * 
    * @param operationName
    *           the new operation name
    */
   public void setOperationName(String operationName) {
      this.operationName = operationName;
   }

   /**
    * Gets the organization.
    * 
    * @return the organization
    */
   public String getOrganization() {
      return organization;
   }

   /**
    * Sets the organization.
    * 
    * @param organization
    *           the new organization
    */
   public void setOrganization(String organization) {
      this.organization = organization;
   }

   /**
    * Gets the random number.
    * 
    * @return the random number
    */
   public int getRandomNumber() {
      return randomNumber;
   }

   /**
    * Sets the random number.
    * 
    * @param randomNumber
    *           the new random number
    */
   public void setRandomNumber(int randomNumber) {
      this.randomNumber = randomNumber;
   }

   /**
    * Gets the server name.
    * 
    * @return the server name
    */
   public String getServerName() {
      return serverName;
   }

   /**
    * Sets the server name.
    * 
    * @param serverName
    *           the new server name
    */
   public void setServerName(String serverName) {
      this.serverName = serverName;
   }

   /**
    * Gets the server side.
    * 
    * @return the server side
    */
   public String getServerSide() {
      return serverSide;
   }

   /**
    * Sets the server side.
    * 
    * @param serverSide
    *           the new server side
    */
   public void setServerSide(String serverSide) {
      this.serverSide = serverSide;
   }

   /**
    * Gets the service admin name.
    * 
    * @return the service admin name
    */
   public String getServiceAdminName() {
      return serviceAdminName;
   }

   /**
    * Sets the service admin name.
    * 
    * @param serviceAdminName
    *           the new service admin name
    */
   public void setServiceAdminName(String serviceAdminName) {
      this.serviceAdminName = serviceAdminName;
   }

   /**
    * Gets the severity.
    * 
    * @return the severity
    */
   public String getSeverity() {
      return severity;
   }

   /**
    * Sets the severity.
    * 
    * @param severity
    *           the new severity
    */
   public void setSeverity(String severity) {
      this.severity = severity;
   }

   /**
    * Gets the sub domain.
    * 
    * @return the sub domain
    */
   public String getSubDomain() {
      return subDomain;
   }

   /**
    * Sets the sub domain.
    * 
    * @param subDomain
    *           the new sub domain
    */
   public void setSubDomain(String subDomain) {
      this.subDomain = subDomain;
   }

   /**
    * Gets the tstamp.
    * 
    * @return the tstamp
    */
   public Long getTstamp() {
      return tstamp;
   }

   /**
    * Sets the tstamp.
    * 
    * @param tstamp
    *           the new tstamp
    */
   public void setTstamp(Long tstamp) {
      this.tstamp = tstamp;
   }

   /**
    * Instantiates a new error value.
    * 
    * @param keyType
    *           the key type
    */
   public ErrorValue(K keyType) {
      super(keyType);
   }

}
