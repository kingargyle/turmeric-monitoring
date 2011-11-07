package org.ebayopensource.turmeric.monitoring.aggregation.util;

/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

import java.io.File;
import java.io.IOException;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.service.EmbeddedCassandraService;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.utils.cassandra.service.CassandraManager;

/**
 * The Class CassandraTestManager.
 * 
 * @author jamuguerza
 */
public class CassandraTestManager {
   private static EmbeddedCassandraService cassandraService = null;

   /**
    * Set embedded cassandra.
    * 
    * @throws TTransportException
    *            the t transport exception
    * @throws IOException
    *            Signals that an I/O exception has occurred.
    * @throws InterruptedException
    *            the interrupted exception
    * @throws ConfigurationException
    *            the configuration exception
    */
   public static void initializeCluster() throws TTransportException, IOException, InterruptedException,
            ConfigurationException {

      // cleanUpCassandraDirs();
      if (cassandraService == null) {
         loadClusterConfig();
         cassandraService = new EmbeddedCassandraService();
         cassandraService.start();
      }
   }

   /**
    * Load config.
    */
   private static void loadClusterConfig() {
      // use particular test properties, maybe with copy method
      System.setProperty("log4j.configuration", "log4j.properties");

      System.setProperty("cassandra.config", "cassandra-test.yaml");
   }

   public static void cleanUpCassandraDirs() {
      try {
         cleanupData();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      if (CassandraManager.getEmbeddedService() == null) {
         System.out.println("Cleaning cassandra dirs ? = "
                  + deleteDir(new File("target/cassandra/data/TurmericMonitoring")));
      }
   }

   private static void cleanupData() throws IOException {
      String[] allDataFileLocations = DatabaseDescriptor.getAllDataFileLocations();
      for (String s : allDataFileLocations) {
         File dirFile = new File(s);
         if (dirFile.exists() && dirFile.isDirectory()) {
            FileUtils.delete(dirFile.listFiles());
         }
      }
   }

   // Deletes all files and subdirectories under dir.
   // Returns true if all deletions were successful.
   // If a deletion fails, the method stops attempting to delete and returns
   // false.
   private static boolean deleteDir(File dir) {
      if (dir.isDirectory()) {
         String[] children = dir.list();
         for (int i = 0; i < children.length; i++) {
            boolean success = deleteDir(new File(dir, children[i]));
            if (!success) {
               return false;
            }
         }

      }
      // The directory is now empty so delete it
      return dir.delete();

   }

}
