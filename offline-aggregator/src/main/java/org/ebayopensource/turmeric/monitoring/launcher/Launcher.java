package org.ebayopensource.turmeric.monitoring.launcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ebayopensource.turmeric.monitoring.aggregation.CassandraConnectionInfo;
import org.ebayopensource.turmeric.monitoring.aggregation.Exporter;

// TODO: Auto-generated Javadoc
/**
 * The Class Launcher.
 */
public class Launcher {

   static String onlineClusterName = null;
   static String onlineHostAddress = null;
   static String onlineCLusterPort = null;
   static String offlineClusterName = null;
   static String offlineHostAddress = null;
   static String offlineCLusterPort = null;
   static String keyspaceName = null;
   static String startTimeStr = null;
   static String endTimeStr = null;

   /**
    * The main method.
    * 
    * @param args
    *           the arguments
    * @throws ParseException
    *            the parse exception
    */

   public static void main(String[] args) throws ParseException {
      // String onlineClusterName = "OnlineCluster";
      // String offlineClusterName = "OfflineCluster";
      // String onlineHostAddress = "127.0.0.1";
      // String onlineCLusterPort = "9160";
      // String offlineHostAddress = "127.0.0.2";
      // String offlineCLusterPort = "9160";
      // String keyspaceName = "TurmericMonitoring";
      // Date startTime = formatter.parse("11/15/2011-18:00");
      // Date endTime = formatter.parse("11/15/2011-22:00");
      if (args == null || args.length == 0 || args[0] == null || args[0].isEmpty() || args[0].equals("help")) {
         printHelp();
         System.exit(0);
      }
      onlineClusterName = args[0];
      onlineHostAddress = args[1];
      onlineCLusterPort = args[2];
      offlineClusterName = args[3];
      offlineHostAddress = args[4];
      offlineCLusterPort = args[5];
      keyspaceName = args[6];
      startTimeStr = args[7];
      endTimeStr = args[8];
      printLauncherValues();
      CassandraConnectionInfo realtimeCluster = new CassandraConnectionInfo(onlineClusterName, onlineHostAddress,
               onlineCLusterPort, keyspaceName);
      CassandraConnectionInfo offlineCluster = new CassandraConnectionInfo(offlineClusterName, offlineHostAddress,
               offlineCLusterPort, keyspaceName);
      SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy-HH:mm");
      Date startTime = formatter.parse(startTimeStr);
      Date endTime = formatter.parse(endTimeStr);
      Exporter aggregator = new Exporter(startTime, endTime, realtimeCluster, offlineCluster);
      aggregator.export();
      System.exit(0);
   }

   private static void printHelp() {
      String exampleParams = "onlineClusterName onlineHostAddress onlineCLusterPort offlineClusterName offlineHostAddress offlineCLusterPort keyspaceName startTime (MM/dd/yyyy-HH:mm) endTime (MM/dd/yyyy-HH:mm)";
      String exampleRun = "OnlineCluster 127.0.0.1 9160 OfflineCluster 127.0.0.2 9160 TurmericMonitoring 11/16/2011-09:00 11/16/2011-11:00";
      System.out.println("Run the program with the following params:" + exampleParams);
      System.out.println("\nExample:" + exampleRun);
   }

   private static void printLauncherValues() {
      System.out.println("onlineClusterName = " + onlineClusterName);
      System.out.println("onlineHostAddress = " + onlineHostAddress);
      System.out.println("onlineCLusterPort = " + onlineCLusterPort);
      System.out.println("offlineClusterName = " + offlineClusterName);
      System.out.println("offlineHostAddress = " + offlineHostAddress);
      System.out.println("offlineCLusterPort = " + offlineCLusterPort);
      System.out.println("keyspaceName = " + keyspaceName);
      System.out.println("startTimeStr = " + startTimeStr);
      System.out.println("endTimeStr = " + endTimeStr);

   }
}
