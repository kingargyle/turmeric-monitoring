package org.ebayopensource.turmeric.monitoring.provider.model;

import java.util.HashMap;

public class MetricValue<K> extends Model<K> {

   public MetricValue(K keyType) {
      super(keyType);
      setColumns(new HashMap<String, Object>());
   }

   @Override
   public boolean equals(Object obj) {
      return super.equals(obj);
   }

   public long getTimeMiliseconds() {
      String strKey = (String) getKey();// the format is ipaddress|metricname|timestamp i.e :
                                        // servername|SoaFwk.Op.Time.Total|1318259966434
      String timestampStr = strKey.substring(strKey.lastIndexOf('|') + 1);
      return Long.valueOf(timestampStr);
   }

   public Double getValueForMetric(String metricName) {
      Double result = 0.0D;
      if (getColumns() != null) {
         if ("CallCount".equals(metricName)) {
            result += (Long) getColumns().get("count");
         } else if ("ResponseTime".equals(metricName)) {
            result = (Double) getColumns().get("totalTime");
         } else if ("ErrorCount".equals(metricName)) {
            result += (Long) getColumns().get("value");
         }
      }
      return result;
   }

   @Override
   public int hashCode() {
      return super.hashCode();
   }

   @Override
   public String toString() {
      StringBuilder result = new StringBuilder();
      if (getColumns() != null) {
         result.append("[");
         for (String columnName : getColumns().keySet()) {
            result.append(columnName).append("={").append(getColumns().get(columnName)).append("}").append("\n");
         }
         result.append("]");
      }
      return result.toString();
   }

}
