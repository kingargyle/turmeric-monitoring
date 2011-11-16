package org.ebayopensource.turmeric.monitoring.aggregation.metric.reader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.SuperRow;
import me.prettyprint.hector.api.beans.SuperRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.MultigetSuperSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.ColumnFamilyReader;
import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;
import org.ebayopensource.turmeric.monitoring.aggregation.utils.DateRangeHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class MetricTimeSeriesReader.
 */
public class IpPerDayAndServiceNameReader extends ColumnFamilyReader<String> {

   /** The Constant columnFamilyName. */
   private static final String columnFamilyName = "IpPerDayAndServiceName";
   private static final SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
   private final DateRangeHelper dateRangeCalculator;

   /**
    * Instantiates a new metric time series reader.
    * 
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @param connectionInfo
    *           the connection info
    */
   public IpPerDayAndServiceNameReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
      dateRangeCalculator = new DateRangeHelper(startTime, endTime);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, AggregationData<String>> readData() {
      Map<String, AggregationData<String>> result = new HashMap<String, AggregationData<String>>();
      try {
         MultigetSuperSliceQuery<String, String, String, String> multigetSuperSliceQuery = HFactory
                  .createMultigetSuperSliceQuery(connectionInfo.getKeyspace(), STR_SERIALIZER, STR_SERIALIZER,
                           STR_SERIALIZER, STR_SERIALIZER);
         multigetSuperSliceQuery.setColumnFamily(columnFamilyName);
         List<String> keysToRead = retrieveKeysInRange();
         multigetSuperSliceQuery.setKeys(keysToRead);
         multigetSuperSliceQuery.setRange("", "", false, ROWS_NUMBER_MAX_VALUE);
         QueryResult<SuperRows<String, String, String, String>> queryResult = multigetSuperSliceQuery.execute();
         SuperRows<String, String, String, String> orderedRows = queryResult.get();

         for (SuperRow<String, String, String, String> row : orderedRows) {
            AggregationData<String> rowData = new AggregationData<String>(row.getKey());
            List<HSuperColumn<String, String, String>> superColumns = row.getSuperSlice().getSuperColumns();
            if (superColumns != null && !superColumns.isEmpty()) {
               Set<String> ipAddresses = new HashSet<String>();
               for (HSuperColumn<String, String, String> hSuperColumn : superColumns) {
                  for (HColumn<String, String> hColumn : hSuperColumn.getColumns()) {
                     ipAddresses.add(hColumn.getName());
                  }
                  rowData.addColumn(hSuperColumn.getName(), ipAddresses);
               }

            }
            result.put(rowData.getKey(), rowData);
         }
      } catch (Exception e) {
         e.printStackTrace();
         if (e.getCause() != null) {
            e.getCause().printStackTrace();
         }
      }
      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> retrieveKeysInRange() {
      List<String> result = new ArrayList<String>();
      try {
         String[] dateRange = dateRangeCalculator.getFomatedStringRange(formatter);
         MultigetSuperSliceQuery<String, String, String, String> rangeSuperSlicesQuery = HFactory
                  .createMultigetSuperSliceQuery(connectionInfo.getKeyspace(), STR_SERIALIZER, STR_SERIALIZER,
                           STR_SERIALIZER, STR_SERIALIZER);
         rangeSuperSlicesQuery.setColumnFamily(columnFamilyName);
         rangeSuperSlicesQuery.setKeys(dateRange);
         rangeSuperSlicesQuery.setRange("", "", false, ROWS_NUMBER_MAX_VALUE);
         QueryResult<SuperRows<String, String, String, String>> queryResult = rangeSuperSlicesQuery.execute();
         SuperRows<String, String, String, String> orderedRows = queryResult.get();

         for (SuperRow<String, String, String, String> row : orderedRows) {
            List<HSuperColumn<String, String, String>> superColumns = row.getSuperSlice().getSuperColumns();
            if (superColumns != null && !superColumns.isEmpty()) {
               if (!result.contains(row.getKey())) {
                  result.add(row.getKey());
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         if (e.getCause() != null) {
            e.getCause().printStackTrace();
         }
      }
      return result;
   }
}
