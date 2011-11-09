package org.ebayopensource.turmeric.monitoring.aggregation.data;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class AggregationData.
 *
 * @param <KeyType> the generic type
 */
public class AggregationData<KeyType> {
   
   /** The key. */
   protected KeyType key;
   
   /** The columns. */
   protected Map<Object, Object> columns;

   /**
    * Instantiates a new aggregation data.
    *
    * @param key the key
    * @param columns the columns
    */
   private AggregationData(KeyType key, Map<Object, Object> columns) {
      super();
      this.key = key;
      this.columns = columns;
   }

   /**
    * Instantiates a new aggregation data.
    *
    * @param key the key
    */
   public AggregationData(KeyType key) {
      this(key, new HashMap<Object, Object>());
   }

   /**
    * Gets the key.
    *
    * @return the key
    */
   public KeyType getKey() {
      return key;
   }

   /**
    * Gets the columns.
    *
    * @return the columns
    */
   public Map<Object, Object> getColumns() {
      return columns;
   }

   /**
    * Adds the column.
    *
    * @param columnKey the column key
    * @param columnValue the column value
    */
   public void addColumn(Object columnKey, Object columnValue) {
      this.columns.put(columnKey, columnValue);
   }

   /**
    * Contains.
    *
    * @param columnKey the column key
    * @return true, if successful
    */
   public boolean contains(Object columnKey) {
      return this.columns.containsKey(columnKey);
   }

   /**
    * Gets the value.
    *
    * @param columnKey the column key
    * @return the value
    */
   public Object getValue(Object columnKey) {
      return this.columns.get(columnKey);
   }

   /**
    * Size.
    *
    * @return the int
    */
   public int size() {
      return this.columns.size();
   }

}
