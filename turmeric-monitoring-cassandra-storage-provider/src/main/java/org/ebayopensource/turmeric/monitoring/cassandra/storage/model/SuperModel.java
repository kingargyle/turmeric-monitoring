/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.cassandra.storage.model;

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class ModelSuper.
 * 
 * @param <SK>
 *           the generic type
 * @param <K>
 *           the key type
 * @author jamuguerza
 */
public class SuperModel<SK, K> {

   /** The key data. */
   private SK key;

   /** The columns. */
   private Map<K, BasicModel> columns;

   /**
    * Instantiates a new super model.
    * 
    * @param superKeyType
    *           the super key type
    * @param keyType
    *           the key type
    */
   public SuperModel(SK superKeyType, K keyType) {
   }

   /**
    * Gets the columns.
    * 
    * @return the columns
    */
   public Map<K, BasicModel> getColumns() {
      return columns;
   }

   /**
    * Gets the key.
    * 
    * @return the key
    */
   public SK getKey() {
      return key;
   }

   /**
    * Sets the columns.
    * 
    * @param columns
    *           the columns
    */
   public void setColumns(Map<K, BasicModel> columns) {
      this.columns = columns;
   }

   /**
    * Sets the key.
    * 
    * @param key
    *           the new key
    */
   public void setKey(SK key) {
      this.key = key;
   }

}
