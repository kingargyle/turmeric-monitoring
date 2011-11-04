/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.model;

// TODO: Auto-generated Javadoc
/**
 * The Class Error.
 * 
 * @param <K>
 *           the key type
 */
public class Error<K> extends Model<K> {

   /** The category. */
   private String category;

   /** The name. */
   private String name;

   /** The domain. */
   private String domain;

   /** The sub domain. */
   private String subDomain;

   /** The severity. */
   private String severity;

   /** The error id. */
   private Long errorId;

   /** The organization. */
   private String organization;

   /**
    * Gets the category.
    * 
    * @return the category
    */
   public String getCategory() {
      return category;
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
    * Gets the domain.
    * 
    * @return the domain
    */
   public String getDomain() {
      return domain;
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
    * Gets the severity.
    * 
    * @return the severity
    */
   public String getSeverity() {
      return severity;
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
    * Gets the organization.
    * 
    * @return the organization
    */
   public String getOrganization() {
      return organization;
   }

   /**
    * Instantiates a new error.
    * 
    * @param keyType
    *           the key type
    */
   public Error(K keyType) {
      super(keyType);
   }

}
