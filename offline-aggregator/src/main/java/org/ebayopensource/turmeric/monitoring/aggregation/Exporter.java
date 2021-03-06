package org.ebayopensource.turmeric.monitoring.aggregation;

import java.util.Date;
import java.util.Map;

import org.ebayopensource.turmeric.monitoring.aggregation.data.AggregationData;
import org.ebayopensource.turmeric.monitoring.aggregation.error.eraser.ErrorValuesEraser;
import org.ebayopensource.turmeric.monitoring.aggregation.error.eraser.ErrorsByCategoryEraser;
import org.ebayopensource.turmeric.monitoring.aggregation.error.eraser.ErrorsBySeverityEraser;
import org.ebayopensource.turmeric.monitoring.aggregation.error.reader.ErrorValueReader;
import org.ebayopensource.turmeric.monitoring.aggregation.error.reader.ErrorsByCategoryReader;
import org.ebayopensource.turmeric.monitoring.aggregation.error.reader.ErrorsByIdReader;
import org.ebayopensource.turmeric.monitoring.aggregation.error.reader.ErrorsBySeverityReader;
import org.ebayopensource.turmeric.monitoring.aggregation.error.writer.ErrorValueWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.error.writer.ErrorsByCategoryWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.error.writer.ErrorsByIdWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.error.writer.ErrorsBySeverityWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.eraser.MetricTimeSeriesEraser;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.eraser.MetricValuesEraser;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.reader.IpPerDayAndServiceNameReader;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.reader.MetricTimeSeriesReader;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.reader.MetricValuesByIpAndDateReader;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.reader.MetricValuesReader;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.reader.ServiceConsumerByIpReader;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.reader.ServiceOperationByIpReader;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.writer.IpPerDayAndServiceNameWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.writer.MetricTimeSeriesWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.writer.MetricValuesByIpAndDateWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.writer.MetricValuesWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.writer.ServiceConsumerByIpWriter;
import org.ebayopensource.turmeric.monitoring.aggregation.metric.writer.ServiceOperationByIpWriter;

// TODO: Auto-generated Javadoc
/**
 * The Class Aggregator.
 */
public class Exporter {

   /** The start time. */
   private final Date startTime;

   /** The end time. */
   private final Date endTime;

   /** The realtime cluster. */
   private final CassandraConnectionInfo realtimeCluster;

   /** The offline cluster. */
   private final CassandraConnectionInfo offlineCluster;

   /** The errors by category reader. */
   private ErrorsByCategoryReader errorsByCategoryReader;

   /** The errors by severity reader. */
   private ErrorsBySeverityReader errorsBySeverityReader;

   /** The metric time series reader. */
   private MetricTimeSeriesReader metricTimeSeriesReader;

   private MetricValuesReader metricValuesReader;

   private MetricValuesByIpAndDateReader metricValuesByIpAndDateReader;

   private IpPerDayAndServiceNameReader ipPerDayAndServiceNameReader;

   private ServiceOperationByIpReader serviceOperationByIpReader;

   private ServiceConsumerByIpReader serviceConsumerByIpReader;

   private Map<String, AggregationData<String>> errorsByCategoryData;

   private Map<String, AggregationData<String>> errorsBySeverityData;

   private Map<String, AggregationData<String>> metricTimeSeriesData;

   private Map<String, AggregationData<String>> metricValuesData;

   private Map<String, AggregationData<String>> metricValuesByIpAndDateData;

   private Map<String, AggregationData<String>> ipPerDayAndServiceNameData;

   private Map<String, AggregationData<String>> serviceOperationByIpData;

   private Map<String, AggregationData<String>> serviceConsumerByIpData;

   private ErrorsByCategoryWriter errorByCategoryWriter;

   private ErrorsBySeverityWriter errorBySeverityWriter;

   private MetricTimeSeriesWriter metricTimeSeriesWriter;

   private MetricValuesWriter metricValuesWriter;

   private MetricValuesByIpAndDateWriter metricValuesByIpAndDateWriter;

   private ServiceOperationByIpWriter serviceOperationByIpWriter;

   private IpPerDayAndServiceNameWriter ipPerDayAndServiceNameWriter;

   private ServiceConsumerByIpWriter serviceConsumerByIpWriter;

   private ErrorsByIdReader errorsByIdReader;

   private ErrorValueReader errorValuesReader;

   private Map<Long, AggregationData<Long>> errorsByIdData;

   private Map<String, AggregationData<String>> errorValuesData;

   private ErrorsByIdWriter errorByIdWriter;

   private ErrorValueWriter errorValueWriter;

   private ErrorsByCategoryEraser errorByCategoryEraser;

   private ErrorsBySeverityEraser errorBySeverityEraser;

   private ErrorValuesEraser errorValueEraser;

   private MetricTimeSeriesEraser metricTimeSeriesEraser;

   private MetricValuesEraser metricValuesEraser;

   /**
    * Instantiates a new aggregator.
    * 
    * @param startTime
    *           the start time
    * @param endTime
    *           the end time
    * @param realtimeCluster
    *           the realtime cluster
    * @param offlineCluster
    *           the offline cluster
    */
   public Exporter(Date startTime, Date endTime, CassandraConnectionInfo realtimeCluster,
            CassandraConnectionInfo offlineCluster) {
      super();
      this.startTime = startTime;
      this.endTime = endTime;
      this.realtimeCluster = realtimeCluster;
      this.offlineCluster = offlineCluster;
      initReaders();
      initWriters();
      initErasers();
   }

   private void initErasers() {
      errorByCategoryEraser = new ErrorsByCategoryEraser(startTime, endTime, realtimeCluster);
      errorBySeverityEraser = new ErrorsBySeverityEraser(startTime, endTime, realtimeCluster);
      errorValueEraser = new ErrorValuesEraser(startTime, endTime, realtimeCluster);
      metricTimeSeriesEraser = new MetricTimeSeriesEraser(startTime, endTime, realtimeCluster);
      metricValuesEraser = new MetricValuesEraser(startTime, endTime, realtimeCluster);
   }

   /**
    * Inits the writers.
    */
   private void initWriters() {
      errorByCategoryWriter = new ErrorsByCategoryWriter(startTime, endTime, offlineCluster);
      errorBySeverityWriter = new ErrorsBySeverityWriter(startTime, endTime, offlineCluster);
      errorByIdWriter = new ErrorsByIdWriter(startTime, endTime, offlineCluster);
      errorValueWriter = new ErrorValueWriter(startTime, endTime, offlineCluster);
      metricTimeSeriesWriter = new MetricTimeSeriesWriter(startTime, endTime, offlineCluster);
      metricValuesWriter = new MetricValuesWriter(startTime, endTime, offlineCluster);
      metricValuesByIpAndDateWriter = new MetricValuesByIpAndDateWriter(startTime, endTime, offlineCluster);
      ipPerDayAndServiceNameWriter = new IpPerDayAndServiceNameWriter(startTime, endTime, offlineCluster);
      serviceOperationByIpWriter = new ServiceOperationByIpWriter(startTime, endTime, offlineCluster);
      serviceConsumerByIpWriter = new ServiceConsumerByIpWriter(startTime, endTime, offlineCluster);
   }

   /**
    * Inits the readers.
    */
   private void initReaders() {
      errorsByCategoryReader = new ErrorsByCategoryReader(startTime, endTime, realtimeCluster);
      errorsBySeverityReader = new ErrorsBySeverityReader(startTime, endTime, realtimeCluster);
      errorsByIdReader = new ErrorsByIdReader(startTime, endTime, realtimeCluster);
      errorValuesReader = new ErrorValueReader(startTime, endTime, realtimeCluster);
      metricTimeSeriesReader = new MetricTimeSeriesReader(startTime, endTime, realtimeCluster);
      metricValuesReader = new MetricValuesReader(startTime, endTime, realtimeCluster);
      metricValuesByIpAndDateReader = new MetricValuesByIpAndDateReader(startTime, endTime, realtimeCluster);
      ipPerDayAndServiceNameReader = new IpPerDayAndServiceNameReader(startTime, endTime, realtimeCluster);
      serviceOperationByIpReader = new ServiceOperationByIpReader(startTime, endTime, realtimeCluster);
      serviceConsumerByIpReader = new ServiceConsumerByIpReader(startTime, endTime, realtimeCluster);
   }

   /**
    * Do aggregation.
    * 
    * @param startTime
    *           the start time
    * @param realtimeCluster
    *           the realtime cluster
    * @param offlineCluster
    *           the offline cluster
    */
   public void export() {
      extractDataFromOnlineCluster();
      insertDataIntoOfflineCluster();
      deleteExtractedDataFromRealTimeCluster();
   }

   /**
    * Extract data from online cluster.
    */
   private void extractDataFromOnlineCluster() {
      errorsByCategoryData = errorsByCategoryReader.readData();
      errorsBySeverityData = errorsBySeverityReader.readData();
      errorsByIdData = errorsByIdReader.readData();
      errorValuesData = errorValuesReader.readData();
      metricTimeSeriesData = metricTimeSeriesReader.readData();
      metricValuesData = metricValuesReader.readData();
      metricValuesByIpAndDateData = metricValuesByIpAndDateReader.readData();
      ipPerDayAndServiceNameData = ipPerDayAndServiceNameReader.readData();
      serviceOperationByIpData = serviceOperationByIpReader.readData();
      serviceConsumerByIpData = serviceConsumerByIpReader.readData();
   }

   /**
    * Insert data into offline cluster.
    */
   private void insertDataIntoOfflineCluster() {
      errorByCategoryWriter.writeData(errorsByCategoryData);
      errorBySeverityWriter.writeData(errorsBySeverityData);
      errorByIdWriter.writeData(errorsByIdData);
      errorValueWriter.writeData(errorValuesData);
      metricValuesWriter.writeData(metricValuesData);
      metricValuesByIpAndDateWriter.writeData(metricValuesByIpAndDateData);
      metricTimeSeriesWriter.writeData(metricTimeSeriesData);
      ipPerDayAndServiceNameWriter.writeData(ipPerDayAndServiceNameData);
      serviceOperationByIpWriter.writeData(serviceOperationByIpData);
      serviceConsumerByIpWriter.writeData(serviceConsumerByIpData);
   }

   /**
    * Delete extracted data from real time cluster.
    */
   private void deleteExtractedDataFromRealTimeCluster() {
      errorByCategoryEraser.eraseData(errorsByCategoryData);
      errorBySeverityEraser.eraseData(errorsBySeverityData);
      errorValueEraser.eraseData(errorValuesData);
      metricTimeSeriesEraser.eraseData(metricTimeSeriesData);
      metricValuesEraser.eraseData(metricValuesData);
   }

}
