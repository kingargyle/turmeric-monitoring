package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;
import org.ebayopensource.turmeric.monitoring.aggregation.utils.DateRangeHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class DataAggregator.
 */
public class DataAggregator extends DateRangedObject {

   /** The old metric time series data. */
   private final Map<String, AggregationData<String>> oldMetricTimeSeriesData;

   /** The old metric values data. */
   private final Map<String, AggregationData<String>> oldMetricValuesData;

   /** The aggregated metric time series data. */
   private final Map<String, AggregationData<String>> aggregatedMetricTimeSeriesData;

   /** The aggregated metric values data. */
   private final Map<String, AggregationData<String>> aggregatedMetricValuesData;

   /** The date range helper. */
   private final DateRangeHelper dateRangeHelper;

   /** The new aggregation period. */
   private final int oldAggregationPeriod, newAggregationPeriod;

   /**
    * Instantiates a new data aggregator.
    * 
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @param metricTimeSeriesData
    *           the metric time series data
    * @param metricValuesData
    *           the metric values data
    * @param oldAggregationPeriodInSecs
    *           the old aggregation period
    * @param newAggregationPeriodInSecs
    *           the new aggregation period
    */
   public DataAggregator(Date startTime, Date endTime, Map<String, AggregationData<String>> metricTimeSeriesData,
            Map<String, AggregationData<String>> metricValuesData, int oldAggregationPeriodInSecs,
            int newAggregationPeriodInSecs) {
      super(startTime, endTime);
      if (oldAggregationPeriodInSecs > newAggregationPeriodInSecs) {
         throw new IllegalArgumentException("startAggregationPeriod must be greater than endAggregationPeriod");
      }
      oldAggregationPeriod = oldAggregationPeriodInSecs * 1000;// to milisecs
      newAggregationPeriod = newAggregationPeriodInSecs * 1000;
      dateRangeHelper = new DateRangeHelper(startTime, endTime);
      oldMetricTimeSeriesData = metricTimeSeriesData;
      oldMetricValuesData = metricValuesData;
      aggregatedMetricTimeSeriesData = new HashMap<String, AggregationData<String>>();
      aggregatedMetricValuesData = new HashMap<String, AggregationData<String>>();
   }

   /**
    * Aggregate.
    */
   public void aggregate() {
      Date[] dateRange = dateRangeHelper.getDateRange(getAggregationDiff());

      for (int i = 0; i < dateRange.length - 1; i++) {
         for (Entry<String, AggregationData<String>> oldMetricTimeSeriesEntry : oldMetricTimeSeriesData.entrySet()) {
            Set<AggregationData<String>> oldMetricValuesInRange = new HashSet();
            AggregationData<String> oldMetricTimeSeriesRow = oldMetricTimeSeriesEntry.getValue();
            String oldMetricTimeSeriesRowKey = oldMetricTimeSeriesRow.getKey();
            Map oldTimeSeriesColumns = oldMetricTimeSeriesRow.getColumns();
            for (Object timeStampObj : oldTimeSeriesColumns.keySet()) {
               Long oldTimeSeriesTsmp = (Long) timeStampObj;
               if (oldTimeSeriesTsmp >= dateRange[i].getTime() && oldTimeSeriesTsmp < dateRange[i + 1].getTime()) {
                  Set<String> oldMetricValuesKeys = (Set<String>) oldTimeSeriesColumns.get(timeStampObj);
                  oldMetricValuesInRange.addAll(getAllMetricValues(oldMetricValuesKeys));
               }
            }
            if (!oldMetricValuesInRange.isEmpty()) {
               String newMetricValueKey = createMetricValueKey(oldMetricTimeSeriesRowKey, dateRange[i]);
               String newMetricTimeSeriesKey = createMetricTimeSeriesKey(oldMetricTimeSeriesRowKey);
               AggregationData<String> newMetricValue = addAllMetricValuesInrange(oldMetricValuesInRange,
                        newMetricValueKey);
               addNewMetricTimeSeriesColumn(newMetricTimeSeriesKey, dateRange[i].getTime(), newMetricValueKey);
               aggregatedMetricValuesData.put(newMetricValueKey, newMetricValue);
            }
         }
      }

   }

   public int getAggregationDiff() {
      return newAggregationPeriod / oldAggregationPeriod;
   }

   /**
    * Adds the new metric time series column.
    * 
    * @param newMetricTimeSeriesKey
    *           the new metric time series key
    * @param time
    *           the time
    * @param newMetricValueKey
    *           the new metric value key
    */
   private void addNewMetricTimeSeriesColumn(String newMetricTimeSeriesKey, long time, String newMetricValueKey) {
      AggregationData<String> newTimeSeriesRow = aggregatedMetricTimeSeriesData.get(newMetricTimeSeriesKey);
      if (newTimeSeriesRow == null) {
         newTimeSeriesRow = new AggregationData<String>(newMetricTimeSeriesKey);
         newTimeSeriesRow.addColumn(time, new HashSet(Arrays.asList(newMetricValueKey)));
         aggregatedMetricTimeSeriesData.put(newMetricTimeSeriesKey, newTimeSeriesRow);
      } else {
         Set<String> metricValueKeysSet = (Set<String>) newTimeSeriesRow.getValue(time);
         if (metricValueKeysSet == null) {
            metricValueKeysSet = new HashSet<String>();
            newTimeSeriesRow.addColumn(time, metricValueKeysSet);
         }
         metricValueKeysSet.add(newMetricValueKey);
      }
   }

   /**
    * Creates the metric time series key.
    * 
    * @param oldMetricTimeSeriesRowKey
    *           the old metric time series row key
    * @return the string
    */
   public String createMetricTimeSeriesKey(String oldMetricTimeSeriesRowKey) {
      String oldAggregationPeriodStr = String.valueOf(oldAggregationPeriod);
      int index = oldMetricTimeSeriesRowKey.indexOf(oldAggregationPeriodStr + "|");
      if (index == -1) {
         throw new IllegalArgumentException(oldAggregationPeriod + " not found in " + oldMetricTimeSeriesRowKey);
      }
      String result = oldMetricTimeSeriesRowKey.substring(0, index - 1)
               + "|"
               + newAggregationPeriod
               + "|"
               + oldMetricTimeSeriesRowKey.substring(index + 1 + oldAggregationPeriodStr.length(),
                        oldMetricTimeSeriesRowKey.length());
      return result;
   }

   /**
    * Creates the metric value key.
    * 
    * @param oldMetricTimeSeriesKey
    *           the old metric value key
    * @param date
    *           the date
    * @return the string
    */
   public String createMetricValueKey(String oldMetricTimeSeriesKey, Date date) {
      int index = oldMetricTimeSeriesKey.lastIndexOf("|");
      if (index == -1) {
         throw new IllegalArgumentException("| not found in " + oldMetricTimeSeriesKey);
      }
      String result = oldMetricTimeSeriesKey.substring(0, index);
      result = result.substring(0, result.lastIndexOf("|"));
      result = result + "|" + date.getTime();
      return result;
   }

   /**
    * Gets the aggregated metric time series data.
    * 
    * @return the aggregated metric time series data
    */
   public Map<String, AggregationData<String>> getAggregatedMetricTimeSeriesData() {
      return aggregatedMetricTimeSeriesData;
   }

   /**
    * Gets the aggregated metric values data.
    * 
    * @return the aggregated metric values data
    */
   public Map<String, AggregationData<String>> getAggregatedMetricValuesData() {
      return aggregatedMetricValuesData;
   }

   /**
    * Adds the all metric values inrange.
    * 
    * @param oldMetricValuesInRange
    *           the old metric values in range
    * @param newMetricValueKey
    *           the new metric value key
    * @return the aggregation data
    */
   private AggregationData<String> addAllMetricValuesInrange(Set<AggregationData<String>> oldMetricValuesInRange,
            String newMetricValueKey) {
      Map<Object, Object> aggregatedColumns = new HashMap();
      for (AggregationData<String> oldMetricValue : oldMetricValuesInRange) {
         Map<Object, Object> oldColumns = oldMetricValue.getColumns();
         for (Object oldColumnKey : oldColumns.keySet()) {
            if (!aggregatedColumns.containsKey(oldColumnKey)) {
               aggregatedColumns.put(oldColumnKey, oldColumns.get(oldColumnKey));
            } else {
               Object aggregatedValue = aggregatedColumns.get(oldColumnKey);
               Object oldColumnValue = oldColumns.get(oldColumnKey);
               Object newMetricValue = aggregateColumnValues(aggregatedValue, oldColumnValue);
               aggregatedColumns.put(oldColumnKey, newMetricValue);
            }
         }
      }
      AggregationData<String> result = new AggregationData<String>(newMetricValueKey);
      result.addAllColumns(aggregatedColumns);
      return result;
   }

   /**
    * Aggregate column values.
    * 
    * @param aggregatedValue
    *           the aggregated value
    * @param oldColumnValue
    *           the old column value
    * @return the object
    */
   public Object aggregateColumnValues(Object aggregatedValue, Object oldColumnValue) {
      if (oldColumnValue instanceof Long) {
         return (Long) aggregatedValue + (Long) oldColumnValue;
      } else if (oldColumnValue instanceof Double) {
         return (Double) aggregatedValue + (Double) oldColumnValue;
      } else {
         throw new IllegalArgumentException("oldColumnValue is instanceof " + oldColumnValue.getClass().getName());
      }
   }

   /**
    * Gets the all metric values.
    * 
    * @param oldMetricValuesKeys
    *           the old metric values keys
    * @return the all metric values
    */
   public Set<AggregationData<String>> getAllMetricValues(Set<String> oldMetricValuesKeys) {
      Set<AggregationData<String>> result = new HashSet();
      for (String metricValueKey : oldMetricValuesKeys) {
         result.add(oldMetricValuesData.get(metricValueKey));
      }
      return result;
   }
}
