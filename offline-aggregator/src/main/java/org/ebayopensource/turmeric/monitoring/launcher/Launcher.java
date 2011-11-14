package org.ebayopensource.turmeric.monitoring.launcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.Aggregator;
import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class Launcher.
 */
public class Launcher {

   /**
    * The main method.
    * 
    * @param args
    *           the arguments
    * @throws ParseException
    *            the parse exception
    */
   public static void main(String[] args) throws ParseException {
      String onlineClusterName = "OnlineCluster";
      String offlineClusterName = "OfflineCluster";
      String onlineHostAddress = "127.0.0.1";
      String onlineCLusterPort = "9160";
      String offlineHostAddress = "127.0.0.2";
      String offlineCLusterPort = "9160";
      String keyspaceName = "TurmericMonitoring";
      CassandraConnectionInfo realtimeCluster = new CassandraConnectionInfo(onlineClusterName, onlineHostAddress,
               onlineCLusterPort, keyspaceName);
      CassandraConnectionInfo offlineCluster = new CassandraConnectionInfo(offlineClusterName, offlineHostAddress,
               offlineCLusterPort, keyspaceName);
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
      Date startTime = formatter.parse("13/11/2011-18:00");
      Date endTime = formatter.parse("13/11/2011-22:00");
      Aggregator aggregator = new Aggregator(startTime, endTime, realtimeCluster, offlineCluster);
      aggregator.export();
      System.exit(0);
   }
}
