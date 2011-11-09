package org.ebayopensource.turmeric.monitoring.aggregation.data;

import java.util.HashMap;
import java.util.Map;

public class AggregationData<KeyType> {
   protected KeyType key;
   protected Map<Object, Object> columns;

   private AggregationData(KeyType key, Map<Object, Object> columns) {
      super();
      this.key = key;
      this.columns = columns;
   }

   public AggregationData(KeyType key) {
      this(key, new HashMap<Object, Object>());
   }

   public KeyType getKey() {
      return key;
   }

   public Map<Object, Object> getColumns() {
      return columns;
   }

   public void addColumn(Object columnKey, Object columnValue) {
      this.columns.put(columnKey, columnValue);
   }

   public boolean contains(Object columnKey) {
      return this.columns.containsKey(columnKey);
   }

   public Object getValue(Object columnKey) {
      return this.columns.get(columnKey);
   }

   public int size() {
      return this.columns.size();
   }

}
