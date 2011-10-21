/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.model;

import java.util.Map;

/**
 * The Class Model.
 * 
 * @author jamuguerza
 */
public class Model<K> {

   /** The operation name List. */
   protected Map<String, Object> columns;

   /** The key. */
   protected K key;

   /**
    * Instantiates a new model.
    * 
    * @param keyType
    *           the key type
    */
   public Model(K keyType) {
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      Model other = (Model) obj;
      if (columns == null) {
         if (other.columns != null) {
            return false;
         }
      } else if (!columns.equals(other.columns)) {
         return false;
      }
      if (key == null) {
         if (other.key != null) {
            return false;
         }
      } else if (!key.equals(other.key)) {
         return false;
      }
      return true;
   }

   public Map<String, Object> getColumns() {
      return columns;
   }

   /**
    * Gets the key.
    * 
    * @return the key
    */
   public K getKey() {
      return key;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((columns == null) ? 0 : columns.hashCode());
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      return result;
   }

   public void setColumns(Map<String, Object> columns) {
      this.columns = columns;
   }

   /**
    * Sets the key.
    * 
    * @param key
    *           the new key
    */
   public void setKey(K key) {
      this.key = key;
   }

   public void setKey(String key) {
      this.key = (K) key;
   }

}
