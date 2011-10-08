package org.ebayopensource.turmeric.monitoring.provider.model;

import java.util.HashMap;

import org.apache.commons.collections.map.HashedMap;

public class MetricValue<K> extends Model<K> {

   public MetricValue(K keyType) {
      super(keyType);
     this.setColumns(new HashMap<String, Object>());
   }

}
