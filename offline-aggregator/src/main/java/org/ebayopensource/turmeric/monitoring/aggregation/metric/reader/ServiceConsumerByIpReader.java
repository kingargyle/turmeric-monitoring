package org.ebayopensource.turmeric.monitoring.aggregation.metric.reader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

// TODO: Auto-generated Javadoc
/**
 * The Class MetricTimeSeriesReader.
 */
public class ServiceConsumerByIpReader extends ColumnFamilyReader<String> {

   /** The Constant columnFamilyName. */
   private static final String columnFamilyName = "ServiceConsumerByIp";
   private final IpPerDayAndServiceNameReader ipPerDayReader;

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
   public ServiceConsumerByIpReader(Date startTime, Date endTime, CassandraConnectionInfo connectionInfo) {
      super(startTime, endTime, connectionInfo);
      ipPerDayReader = new IpPerDayAndServiceNameReader(startTime, endTime, connectionInfo);
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
               Set<String> operations = new HashSet<String>();
               for (HSuperColumn<String, String, String> hSuperColumn : superColumns) {
                  for (HColumn<String, String> hColumn : hSuperColumn.getColumns()) {
                     operations.add(hColumn.getName());
                  }
                  rowData.addColumn(hSuperColumn.getName(), operations);
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
      result.add("All");
      try {
         Map<String, AggregationData<String>> ipPerDayData = ipPerDayReader.readData();
         for (Entry<String, AggregationData<String>> ipPerDayDataEntry : ipPerDayData.entrySet()) {
            AggregationData<String> ipPerdayRow = ipPerDayDataEntry.getValue();
            for (Entry<Object, Object> ipPerDayColumn : ipPerdayRow.getColumns().entrySet()) {
               Set ipAdresses = (Set) ipPerDayColumn.getValue();
               for (Object ipAdress : ipAdresses) {
                  if (!result.contains(ipAdress)) {
                     result.add(ipAdress.toString());
                  }
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
