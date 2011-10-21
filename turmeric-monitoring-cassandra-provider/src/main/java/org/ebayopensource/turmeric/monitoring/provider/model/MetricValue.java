package org.ebayopensource.turmeric.monitoring.provider.model;

import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class MetricValue.
 * 
 * @param <K>
 *           the key type
 */
public class MetricValue<K> extends Model<K> {

   /**
    * Instantiates a new metric value.
    * 
    * @param keyType
    *           the key type
    */
   public MetricValue(K keyType) {
      super(keyType);
      setColumns(new HashMap<String, Object>());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object obj) {
      return super.equals(obj);
   }

   /**
    * Gets the time miliseconds.
    * 
    * @return the time miliseconds
    */
   public long getTimeMiliseconds() {
      String strKey = (String) getKey();// the format is ipaddress|metricname|timestamp i.e :
                                        // servername|SoaFwk.Op.Time.Total|1318259966434
      String timestampStr = strKey.substring(strKey.lastIndexOf('|') + 1);
      return Long.valueOf(timestampStr);
   }

   /**
    * Gets the value for metric.
    * 
    * @param metricName
    *           the metric name
    * @return the value for metric
    */
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

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      return super.hashCode();
   }

   /**
    * {@inheritDoc}
    */
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
