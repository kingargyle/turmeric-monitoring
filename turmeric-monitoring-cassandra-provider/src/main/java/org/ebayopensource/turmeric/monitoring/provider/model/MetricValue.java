package org.ebayopensource.turmeric.monitoring.provider.model;

import java.util.HashMap;

import org.apache.commons.collections.map.HashedMap;
import org.ebayopensource.turmeric.runtime.common.impl.internal.monitoring.SystemMetricDefs;

public class MetricValue<K> extends Model<K> {

   public MetricValue(K keyType) {
      super(keyType);
      this.setColumns(new HashMap<String, Object>());
   }

   @Override
   public String toString() {
      StringBuilder result = new StringBuilder();
      if (this.getColumns() != null) {
         result.append("[");
         for (String columnName : this.getColumns().keySet()) {
            result.append(columnName).append("={").append(this.getColumns().get(columnName)).append("}").append("\n");
         }
         result.append("]");
      }
      return result.toString();
   }

   public long getTimeMiliseconds() {
      String strKey = (String) this.getKey();// the format is ipaddress|metricname|timestamp i.e :
                                             // servername|SoaFwk.Op.Time.Total|1318259966434
      String timestampStr = strKey.substring(strKey.lastIndexOf('|') + 1);
      return Long.valueOf(timestampStr);
   }

   public Double getValueForMetric(String metricName) {
      Double result = 0.0D;
      if(this.getColumns()!=null)
      if ("CallCount".equals(metricName)) {
         result += (Long) this.getColumns().get("count");
      } else if ("ResponseTime".equals(metricName)) {
         result = (Double) this.getColumns().get("totalTime");
      } else if ("ErrorCount".equals(metricName)) {
         result += (Long) this.getColumns().get("value");
      }
      return result;
   }

}
