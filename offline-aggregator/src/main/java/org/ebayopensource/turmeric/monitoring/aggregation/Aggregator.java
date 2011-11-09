package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

// TODO: Auto-generated Javadoc
/**
 * The Class Aggregator.
 */
public class Aggregator {

   /** The start time. */
   private final Date startTime;
   
   /** The end time. */
   private final Date endTime;
   
   /** The realtime cluster. */
   private final CassandraConnectionInfo realtimeCluster;
   
   /** The offline cluster. */
   private final CassandraConnectionInfo offlineCluster;

   /**
    * Instantiates a new aggregator.
    *
    * @param startTime the start time
    * @param endTime the end time
    * @param realtimeCluster the realtime cluster
    * @param offlineCluster the offline cluster
    */
   public Aggregator(Date startTime, Date endTime, CassandraConnectionInfo realtimeCluster,
            CassandraConnectionInfo offlineCluster) {
      super();
      this.startTime = startTime;
      this.endTime = endTime;
      this.realtimeCluster = realtimeCluster;
      this.offlineCluster = offlineCluster;
   }

   /**
    * Do aggregation.
    *
    * @param startTime the start time
    * @param realtimeCluster the realtime cluster
    * @param offlineCluster the offline cluster
    */
   public void doAggregation(Date startTime, CassandraConnectionInfo realtimeCluster,
            CassandraConnectionInfo offlineCluster) {
      // AggregationData data = extractData();
      insertDataIntoOfflineCluster();
      // deleteExtractedDataFromRealTimeCluster(data);
   }

   /**
    * Insert data into offline cluster.
    */
   private void insertDataIntoOfflineCluster() {
      // TODO Auto-generated method stub

   }

   /**
    * Delete extracted data from real time cluster.
    *
    * @param data the data
    */
   private void deleteExtractedDataFromRealTimeCluster(AggregationData data) {
      // TODO Auto-generated method stub

   }

}
