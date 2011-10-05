package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;

public class Aggregator {
	
	private Date startTime; 
	private Date endTime;
	private CassandraConnectionInfo realtimeCluster;
	private CassandraConnectionInfo offlineCluster;
	
	
	public Aggregator(Date startTime, Date endTime, CassandraConnectionInfo realtimeCluster,
			CassandraConnectionInfo offlineCluster) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.realtimeCluster = realtimeCluster;
		this.offlineCluster = offlineCluster;
	}

	public AggregationData extractData(){
		AggregationData data = new AggregationData();
		return data;
		
	}
	
	public void doAggregation(Date startTime, CassandraConnectionInfo realtimeCluster, CassandraConnectionInfo offlineCluster){
		AggregationData data = extractData();
		insertDataIntoOfflineCluster();
		deleteExtractedDataFromRealTimeCluster(data);
	}

	private void insertDataIntoOfflineCluster() {
		// TODO Auto-generated method stub
		
	}

	private void deleteExtractedDataFromRealTimeCluster(AggregationData data) {
		// TODO Auto-generated method stub
		
	}

}
