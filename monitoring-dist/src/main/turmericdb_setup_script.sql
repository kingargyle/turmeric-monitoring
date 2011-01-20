-- MySQL dump 10.13  Distrib 5.1.49, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: turmericdb
-- ------------------------------------------------------
-- Server version	5.1.49-1ubuntu8.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `AuditHistory`
--

DROP TABLE IF EXISTS `AuditHistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AuditHistory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `entityId` bigint(20) NOT NULL,
  `entityName` varchar(255) DEFAULT NULL,
  `entityType` varchar(255) DEFAULT NULL,
  `operationType` varchar(255) DEFAULT NULL,
  `subjectId` bigint(20) NOT NULL,
  `subjectName` varchar(255) DEFAULT NULL,
  `subjectType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=115 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AuditHistory`
--

LOCK TABLES `AuditHistory` WRITE;
/*!40000 ALTER TABLE `AuditHistory` DISABLE KEYS */;
INSERT INTO `AuditHistory` VALUES (111,NULL,'2011-01-03 13:50:44',NULL,NULL,'SUBJECTGROUP','SUBJECTGROUP[USER:sg2:25] create @[USER:admin:1]',25,'sg2','USER','create',1,'admin','USER'),(112,NULL,'2011-01-03 13:51:33',NULL,NULL,'POLICY','POLICY[BLACKLIST:policydelete:69] create @[USER:admin:1]',69,'policydelete','BLACKLIST','create',1,'admin','USER'),(113,NULL,'2011-01-03 13:53:09',NULL,NULL,'POLICY','POLICY[BLACKLIST:policydelete:69] delete @[USER:admin:1]',69,'policydelete','BLACKLIST','delete',1,'admin','USER'),(114,NULL,'2011-01-03 13:53:16',NULL,NULL,'POLICY','POLICY[BLACKLIST:dadadad:7] delete @[USER:admin:1]',7,'dadadad','BLACKLIST','delete',1,'admin','USER');
/*!40000 ALTER TABLE `AuditHistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BasicAuth`
--

DROP TABLE IF EXISTS `BasicAuth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BasicAuth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `subjectName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BasicAuth`
--

LOCK TABLES `BasicAuth` WRITE;
/*!40000 ALTER TABLE `BasicAuth` DISABLE KEYS */;
INSERT INTO `BasicAuth` VALUES (1,'jose','2010-12-22 14:13:54',NULL,NULL,'admin','admin');
/*!40000 ALTER TABLE `BasicAuth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ConditionTbl`
--

DROP TABLE IF EXISTS `ConditionTbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ConditionTbl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `expression_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC21C54C3643700EB` (`expression_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ConditionTbl`
--

LOCK TABLES `ConditionTbl` WRITE;
/*!40000 ALTER TABLE `ConditionTbl` DISABLE KEYS */;
INSERT INTO `ConditionTbl` VALUES (1,NULL,NULL,NULL,NULL,1),(2,NULL,NULL,NULL,NULL,2);
/*!40000 ALTER TABLE `ConditionTbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Expression`
--

DROP TABLE IF EXISTS `Expression`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Expression` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `primitiveValue_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKBCD6EB8FD60970B` (`primitiveValue_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Expression`
--

LOCK TABLES `Expression` WRITE;
/*!40000 ALTER TABLE `Expression` DISABLE KEYS */;
INSERT INTO `Expression` VALUES (1,NULL,NULL,NULL,NULL,'Hits count','Hits',1),(2,NULL,NULL,NULL,NULL,'service count','serverCount',2);
/*!40000 ALTER TABLE `Expression` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Machine`
--

DROP TABLE IF EXISTS `Machine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Machine` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `canonicalHostName` varchar(255) DEFAULT NULL,
  `hostAddress` varchar(255) DEFAULT NULL,
  `machineGroup_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9469DC272D9CF3C4` (`machineGroup_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Machine`
--

LOCK TABLES `Machine` WRITE;
/*!40000 ALTER TABLE `Machine` DISABLE KEYS */;
INSERT INTO `Machine` VALUES (1,'jose-Vostro','127.0.1.1',NULL);
/*!40000 ALTER TABLE `Machine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MachineGroup`
--

DROP TABLE IF EXISTS `MachineGroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MachineGroup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MachineGroup`
--

LOCK TABLES `MachineGroup` WRITE;
/*!40000 ALTER TABLE `MachineGroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `MachineGroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Metric`
--

DROP TABLE IF EXISTS `Metric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Metric` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operationName` varchar(255) DEFAULT NULL,
  `serviceAdminName` varchar(255) DEFAULT NULL,
  `metricDef_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK892AE1D05E732490` (`metricDef_id`)
) ENGINE=MyISAM AUTO_INCREMENT=259 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Metric`
--

LOCK TABLES `Metric` WRITE;
/*!40000 ALTER TABLE `Metric` DISABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MetricClassifier`
--

DROP TABLE IF EXISTS `MetricClassifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MetricClassifier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `consumerName` varchar(255) DEFAULT NULL,
  `sourceDataCenter` varchar(255) DEFAULT NULL,
  `targetDataCenter` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MetricClassifier`
--

LOCK TABLES `MetricClassifier` WRITE;
/*!40000 ALTER TABLE `MetricClassifier` DISABLE KEYS */;
INSERT INTO `MetricClassifier` VALUES (1,'TMC','Unknown','Unknown'),(2,'Missing','Unknown','Unknown');
/*!40000 ALTER TABLE `MetricClassifier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MetricComponentDef`
--

DROP TABLE IF EXISTS `MetricComponentDef`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MetricComponentDef` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `metricDef_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF9616FB85E732490` (`metricDef_id`)
) ENGINE=MyISAM AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MetricComponentDef`
--

LOCK TABLES `MetricComponentDef` WRITE;
/*!40000 ALTER TABLE `MetricComponentDef` DISABLE KEYS */;
INSERT INTO `MetricComponentDef` VALUES (1,'count','java.lang.Long',1),(2,'totalTime','java.lang.Double',1),(3,'totalTime','java.lang.Double',2),(4,'count','java.lang.Long',2),(5,'count','java.lang.Long',3),(6,'totalTime','java.lang.Double',3),(7,'totalTime','java.lang.Double',4),(8,'count','java.lang.Long',4),(9,'count','java.lang.Long',5),(10,'totalTime','java.lang.Double',5),(11,'totalTime','java.lang.Double',6),(12,'count','java.lang.Long',6),(13,'count','java.lang.Long',7),(14,'totalTime','java.lang.Double',7),(15,'totalTime','java.lang.Double',8),(16,'count','java.lang.Long',8),(17,'count','java.lang.Long',9),(18,'totalTime','java.lang.Double',9),(19,'totalTime','java.lang.Double',10),(20,'count','java.lang.Long',10),(21,'count','java.lang.Long',11),(22,'totalTime','java.lang.Double',11),(23,'totalTime','java.lang.Double',12),(24,'count','java.lang.Long',12),(25,'value','java.lang.Long',13),(26,'value','java.lang.Long',14),(27,'value','java.lang.Long',15),(28,'value','java.lang.Long',16),(29,'totalTime','java.lang.Double',17),(30,'count','java.lang.Long',17),(31,'count','java.lang.Long',18),(32,'totalTime','java.lang.Double',18);
/*!40000 ALTER TABLE `MetricComponentDef` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MetricComponentValue`
--

DROP TABLE IF EXISTS `MetricComponentValue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MetricComponentValue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value` double DEFAULT NULL,
  `metricComponentDef_id` bigint(20) DEFAULT NULL,
  `metricValue_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK27C05C6411641864` (`metricComponentDef_id`),
  KEY `FK27C05C6414E5B790` (`metricValue_id`)
) ENGINE=MyISAM AUTO_INCREMENT=147193 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MetricComponentValue`
--

LOCK TABLES `MetricComponentValue` WRITE;
/*!40000 ALTER TABLE `MetricComponentValue` DISABLE KEYS */;
/*!40000 ALTER TABLE `MetricComponentValue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MetricDef`
--

DROP TABLE IF EXISTS `MetricDef`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MetricDef` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MetricDef`
--

LOCK TABLES `MetricDef` WRITE;
/*!40000 ALTER TABLE `MetricDef` DISABLE KEYS */;
INSERT INTO `MetricDef` VALUES (1,'TIMING','FINEST','SoaFwk.Op.Time.StartRespPipeline'),(2,'TIMING','NORMAL','SoaFwk.Op.Time.Call'),(3,'TIMING','NORMAL','SoaFwk.Op.Time.Deserialization'),(4,'TIMING','FINEST','SoaFwk.Op.Time.StartDeserialization'),(5,'TIMING','FINEST','SoaFwk.Op.Time.StartReqPipeline'),(6,'TIMING','FINEST','SoaFwk.Op.Time.StartCall'),(7,'TIMING','NORMAL','SoaFwk.Op.Time.Total'),(8,'TIMING','FINEST','SoaFwk.Op.Time.StartSerialization'),(9,'TIMING','NORMAL','SoaFwk.Op.Time.RespDispatch'),(10,'TIMING','NORMAL','SoaFwk.Op.Time.Pipeline_Request'),(11,'TIMING','NORMAL','SoaFwk.Op.Time.Pipeline_Response'),(12,'TIMING','NORMAL','SoaFwk.Op.Time.Serialization'),(13,'ERROR','NORMAL','SoaFwk.Op.FailedCalls'),(14,'ERROR','NORMAL','SoaFwk.Op.Err.Category.System'),(15,'ERROR','NORMAL','SoaFwk.Op.Err.Total'),(16,'ERROR','NORMAL','SoaFwk.Op.Err.Severity.Error'),(17,'TIMING','NORMAL','SoaFwk.Op.Time.Authentication'),(18,'TIMING','NORMAL','SoaFwk.Op.Time.Authorization');
/*!40000 ALTER TABLE `MetricDef` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MetricValue`
--

DROP TABLE IF EXISTS `MetricValue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MetricValue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `aggregationPeriod` int(11) NOT NULL,
  `serverSide` bit(1) NOT NULL,
  `timeStamp` bigint(20) NOT NULL,
  `machine_id` bigint(20) DEFAULT NULL,
  `metric_id` bigint(20) DEFAULT NULL,
  `metricClassifier_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKFB536181D653E464` (`metricClassifier_id`),
  KEY `FKFB53618159051464` (`metric_id`),
  KEY `FKFB536181786F8CF0` (`machine_id`)
) ENGINE=MyISAM AUTO_INCREMENT=74414 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MetricValue`
--

LOCK TABLES `MetricValue` WRITE;
/*!40000 ALTER TABLE `MetricValue` DISABLE KEYS */;
/*!40000 ALTER TABLE `MetricValue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Operation`
--

DROP TABLE IF EXISTS `Operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `operationName` varchar(255) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKDA8CF547555CDD6B` (`resource_id`)
) ENGINE=MyISAM AUTO_INCREMENT=105 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Operation`
--

LOCK TABLES `Operation` WRITE;
/*!40000 ALTER TABLE `Operation` DISABLE KEYS */;
INSERT INTO `Operation` VALUES (1,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'checkout',1),(2,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'commit',2),(3,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'merge',1),(4,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'createPolicy',3),(5,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'getMetaData',3),(6,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'findPolicies',3),(90,'admin','2011-01-03 13:50:44',NULL,NULL,NULL,'25',9),(91,'admin','2011-01-03 13:50:44',NULL,NULL,NULL,'Admin_SubjectGroup_sg2',8),(92,'admin','2011-01-03 13:50:44',NULL,NULL,NULL,'sg2',8),(93,'admin','2011-01-03 13:50:44',NULL,NULL,NULL,'25',8),(94,'admin','2011-01-03 13:50:44',NULL,NULL,NULL,'26',8),(95,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'69',4),(96,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'policydelete',4),(97,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'69',5),(98,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'policydelete',5),(99,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'69',6),(100,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'policydelete',6),(101,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'69',7),(102,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'policydelete',7),(103,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'Admin_Policy_policydelete',8),(104,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'28',8),(7,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'findSubjectGroups',3),(8,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'findSubjects',3),(9,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'findResources',3),(10,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'createSubjectGroups',3),(89,'admin','2011-01-03 13:50:44',NULL,NULL,NULL,'sg2',9),(13,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'getResources',3),(14,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'getEntityHistory',3),(11,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'updateSubjectGroups',3),(12,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'deletePolicy',3);
/*!40000 ALTER TABLE `Operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Policy`
--

DROP TABLE IF EXISTS `Policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Policy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `policyName` varchar(255) DEFAULT NULL,
  `policyType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=71 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Policy`
--

LOCK TABLES `Policy` WRITE;
/*!40000 ALTER TABLE `Policy` DISABLE KEYS */;
INSERT INTO `Policy` VALUES (1,NULL,'2010-12-14 10:12:58',NULL,'2010-12-20 15:02:32','','der','FooBar9','BLACKLIST'),(2,NULL,'2010-12-14 10:29:01',NULL,'2010-12-22 10:31:29','\0',NULL,'BL','BLACKLIST'),(3,NULL,'2010-12-14 16:39:04',NULL,'2010-12-23 16:35:55','','','BL1','BLACKLIST'),(4,NULL,'2010-12-14 16:39:04',NULL,NULL,'',NULL,'RL1','RL'),(5,NULL,'2010-12-14 16:39:04',NULL,NULL,'',NULL,'RL2','RL'),(6,NULL,'2010-12-14 16:39:04',NULL,NULL,'',NULL,'whitelist1','WHITELIST'),(8,NULL,'2010-12-23 15:25:07',NULL,NULL,'','an auth policy','PolicyService','AUTHZ'),(68,'admin','2011-01-03 13:51:28',NULL,NULL,'\0',NULL,'policy to delete','BLACKLIST'),(67,'admin','2011-01-03 13:50:44',NULL,'2011-01-03 13:50:44','','Managing SubjectGroup sg2','Admin_SubjectGroup_sg2','AUTHZ');
/*!40000 ALTER TABLE `Policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Policy_ExclusionSubjectGroups`
--

DROP TABLE IF EXISTS `Policy_ExclusionSubjectGroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Policy_ExclusionSubjectGroups` (
  `Policy_id` bigint(20) NOT NULL,
  `exclusionSubjectGroups_id` bigint(20) NOT NULL,
  KEY `FK45E0F89F741F702C` (`exclusionSubjectGroups_id`),
  KEY `FK45E0F89FD5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Policy_ExclusionSubjectGroups`
--

LOCK TABLES `Policy_ExclusionSubjectGroups` WRITE;
/*!40000 ALTER TABLE `Policy_ExclusionSubjectGroups` DISABLE KEYS */;
/*!40000 ALTER TABLE `Policy_ExclusionSubjectGroups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Policy_ExclusionSubjects`
--

DROP TABLE IF EXISTS `Policy_ExclusionSubjects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Policy_ExclusionSubjects` (
  `Policy_id` bigint(20) NOT NULL,
  `exclusionSubjects_id` bigint(20) NOT NULL,
  KEY `FKC8318EE8B3D781A0` (`exclusionSubjects_id`),
  KEY `FKC8318EE8D5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Policy_ExclusionSubjects`
--

LOCK TABLES `Policy_ExclusionSubjects` WRITE;
/*!40000 ALTER TABLE `Policy_ExclusionSubjects` DISABLE KEYS */;
/*!40000 ALTER TABLE `Policy_ExclusionSubjects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Policy_Operation`
--

DROP TABLE IF EXISTS `Policy_Operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Policy_Operation` (
  `Policy_id` bigint(20) NOT NULL,
  `operations_id` bigint(20) NOT NULL,
  KEY `FK6CA0829A4BFA9764` (`operations_id`),
  KEY `FK6CA0829AD5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Policy_Operation`
--

LOCK TABLES `Policy_Operation` WRITE;
/*!40000 ALTER TABLE `Policy_Operation` DISABLE KEYS */;
INSERT INTO `Policy_Operation` VALUES (1,1),(5,2),(8,12),(8,11),(8,10),(8,14),(68,12),(67,90),(8,9),(8,8),(8,7),(8,6),(67,91),(67,89),(67,94),(67,92),(67,93),(8,13),(8,5),(8,4);
/*!40000 ALTER TABLE `Policy_Operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Policy_Resource`
--

DROP TABLE IF EXISTS `Policy_Resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Policy_Resource` (
  `Policy_id` bigint(20) NOT NULL,
  `resources_id` bigint(20) NOT NULL,
  KEY `FKCAF2247BF4EC17F4` (`resources_id`),
  KEY `FKCAF2247BD5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Policy_Resource`
--

LOCK TABLES `Policy_Resource` WRITE;
/*!40000 ALTER TABLE `Policy_Resource` DISABLE KEYS */;
INSERT INTO `Policy_Resource` VALUES (1,1),(5,2),(8,3);
/*!40000 ALTER TABLE `Policy_Resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Policy_Rule`
--

DROP TABLE IF EXISTS `Policy_Rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Policy_Rule` (
  `Policy_id` bigint(20) NOT NULL,
  `rules_id` bigint(20) NOT NULL,
  KEY `FKE46F35E941837750` (`rules_id`),
  KEY `FKE46F35E9D5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Policy_Rule`
--

LOCK TABLES `Policy_Rule` WRITE;
/*!40000 ALTER TABLE `Policy_Rule` DISABLE KEYS */;
INSERT INTO `Policy_Rule` VALUES (1,1),(4,1),(5,1),(5,2),(6,1);
/*!40000 ALTER TABLE `Policy_Rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Policy_Subject`
--

DROP TABLE IF EXISTS `Policy_Subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Policy_Subject` (
  `Policy_id` bigint(20) NOT NULL,
  `subjects_id` bigint(20) NOT NULL,
  KEY `FK5E0FB31FAB44C2E` (`subjects_id`),
  KEY `FK5E0FB31FD5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Policy_Subject`
--

LOCK TABLES `Policy_Subject` WRITE;
/*!40000 ALTER TABLE `Policy_Subject` DISABLE KEYS */;
INSERT INTO `Policy_Subject` VALUES (1,1),(6,2),(4,3),(5,5),(6,5),(6,4),(3,3),(8,1);
/*!40000 ALTER TABLE `Policy_Subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Policy_SubjectGroup`
--

DROP TABLE IF EXISTS `Policy_SubjectGroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Policy_SubjectGroup` (
  `Policy_id` bigint(20) NOT NULL,
  `subjectGroups_id` bigint(20) NOT NULL,
  KEY `FKBFCC6EA0AC010E5E` (`subjectGroups_id`),
  KEY `FKBFCC6EA0D5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Policy_SubjectGroup`
--

LOCK TABLES `Policy_SubjectGroup` WRITE;
/*!40000 ALTER TABLE `Policy_SubjectGroup` DISABLE KEYS */;
INSERT INTO `Policy_SubjectGroup` VALUES (67,26);
/*!40000 ALTER TABLE `Policy_SubjectGroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PrimitiveValue`
--

DROP TABLE IF EXISTS `PrimitiveValue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PrimitiveValue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PrimitiveValue`
--

LOCK TABLES `PrimitiveValue` WRITE;
/*!40000 ALTER TABLE `PrimitiveValue` DISABLE KEYS */;
INSERT INTO `PrimitiveValue` VALUES (1,NULL,NULL,NULL,NULL,0,'HITS>5'),(2,NULL,NULL,NULL,NULL,0,'PaymentService.commit:count>3');
/*!40000 ALTER TABLE `PrimitiveValue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Resource`
--

DROP TABLE IF EXISTS `Resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `resourceName` varchar(255) DEFAULT NULL,
  `resourceType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Resource`
--

LOCK TABLES `Resource` WRITE;
/*!40000 ALTER TABLE `Resource` DISABLE KEYS */;
INSERT INTO `Resource` VALUES (1,NULL,'2010-12-14 10:49:55',NULL,NULL,'test','testService','SERVICE'),(2,NULL,'2010-12-14 11:08:27',NULL,NULL,'desc Payment','PaymentService','SERVICE'),(3,NULL,'2010-12-14 10:49:55',NULL,NULL,NULL,'PolicyService','SERVICE'),(4,'admin','2010-12-29 17:38:46',NULL,NULL,NULL,'SERVICE.PolicyService.disablePolicy','OBJECT'),(5,'admin','2010-12-29 17:38:46',NULL,NULL,NULL,'SERVICE.PolicyService.deletePolicy','OBJECT'),(6,'admin','2010-12-29 17:38:46',NULL,NULL,NULL,'SERVICE.PolicyService.enablePolicy','OBJECT'),(7,'admin','2010-12-29 17:38:46',NULL,NULL,NULL,'SERVICE.PolicyService.updatePolicy','OBJECT'),(8,'admin','2010-12-29 17:38:46',NULL,NULL,NULL,'SERVICE.PolicyService.updateSubjectGroups','OBJECT'),(9,'admin','2011-01-02 18:03:17',NULL,NULL,NULL,'SERVICE.PolicyService.deleteSubjectGroups','OBJECT');
/*!40000 ALTER TABLE `Resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Rule`
--

DROP TABLE IF EXISTS `Rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `effect` int(11) DEFAULT NULL,
  `effectDuration` bigint(20) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `rolloverPeriod` bigint(20) DEFAULT NULL,
  `ruleName` varchar(255) DEFAULT NULL,
  `condition_id` bigint(20) DEFAULT NULL,
  `notifyEmails` varchar(255) DEFAULT NULL,
  `notifyActive` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK270B1C6850D789` (`condition_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rule`
--

LOCK TABLES `Rule` WRITE;
/*!40000 ALTER TABLE `Rule` DISABLE KEYS */;
INSERT INTO `Rule` VALUES (1,NULL,NULL,NULL,NULL,'HITS TEST',1,10000,3,30000,'rul1',1, NULL, 0),(2,NULL,NULL,NULL,NULL,'ServiceXXX.OperationXX.count test',3,10000,NULL,30000,'server',2, NULL, 0);
/*!40000 ALTER TABLE `Rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Subject`
--

DROP TABLE IF EXISTS `Subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Subject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `emailContact` varchar(255) DEFAULT NULL,
  `externalSubjectId` bigint(20) NOT NULL,
  `ipMask` varchar(255) DEFAULT NULL,
  `subjectName` varchar(255) DEFAULT NULL,
  `subjectType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Subject`
--

LOCK TABLES `Subject` WRITE;
/*!40000 ALTER TABLE `Subject` DISABLE KEYS */;
INSERT INTO `Subject` VALUES (1,NULL,'2010-12-14 10:37:42',NULL,NULL,'IPDESK',NULL,0,NULL,'admin','USER'),(2,NULL,'2010-12-14 10:37:42',NULL,NULL,'IPDESK',NULL,0,NULL,'1.102.96.107','IP'),(3,NULL,'2010-12-14 10:37:42',NULL,NULL,'IPDESK',NULL,0,NULL,'1.102.96.103','IP'),(4,NULL,'2010-12-14 10:37:42',NULL,NULL,'IPDESK',NULL,0,NULL,'1.102.96.104','IP'),(5,NULL,'2010-12-14 10:37:42',NULL,NULL,'IPDESK',NULL,0,NULL,'1.102.96.105','IP'),(6,NULL,'2010-12-14 10:37:42',NULL,NULL,'IPDESK',NULL,0,NULL,'1.102.96.106','IP');
/*!40000 ALTER TABLE `Subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SubjectGroup`
--

DROP TABLE IF EXISTS `SubjectGroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SubjectGroup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `applyToAll` bit(1) NOT NULL,
  `applyToEach` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `subjectGroupCalculator` varchar(255) DEFAULT NULL,
  `subjectGroupName` varchar(255) DEFAULT NULL,
  `subjectType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SubjectGroup`
--

LOCK TABLES `SubjectGroup` WRITE;
/*!40000 ALTER TABLE `SubjectGroup` DISABLE KEYS */;
INSERT INTO `SubjectGroup` VALUES (23,'admin','2011-01-02 18:03:17',NULL,NULL,'\0','\0','first sg',NULL,'SG_1','IP'),(24,'admin','2011-01-02 18:03:17',NULL,NULL,'\0','\0','Managing Admin_SubjectGroup_SG_1',NULL,'Admin_SubjectGroup_SG_1','USER'),(25,'admin','2011-01-03 13:50:44',NULL,NULL,'\0','\0','subj group 2',NULL,'sg2','USER'),(26,'admin','2011-01-03 13:50:44',NULL,NULL,'\0','\0','Managing Admin_SubjectGroup_sg2',NULL,'Admin_SubjectGroup_sg2','USER'),(27,'admin','2011-01-03 13:51:28',NULL,NULL,'\0','\0','Managing Admin_Policy_policy to delete',NULL,'Admin_Policy_policy to delete','USER');
/*!40000 ALTER TABLE `SubjectGroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SubjectGroup_Subject`
--

DROP TABLE IF EXISTS `SubjectGroup_Subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SubjectGroup_Subject` (
  `SubjectGroup_id` bigint(20) NOT NULL,
  `subjects_id` bigint(20) NOT NULL,
  KEY `FKFBFB71A0AB44C2E` (`subjects_id`),
  KEY `FKFBFB71A0B009DAAB` (`SubjectGroup_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SubjectGroup_Subject`
--

LOCK TABLES `SubjectGroup_Subject` WRITE;
/*!40000 ALTER TABLE `SubjectGroup_Subject` DISABLE KEYS */;
INSERT INTO `SubjectGroup_Subject` VALUES (12,2),(12,3),(13,3),(12,1),(11,5),(12,4),(12,5),(12,6),(14,5),(15,1),(16,1),(17,1),(18,1),(19,1),(20,1),(21,1),(22,1),(23,3),(24,1),(25,1),(26,1),(27,1);
/*!40000 ALTER TABLE `SubjectGroup_Subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SubjectType`
--

DROP TABLE IF EXISTS `SubjectType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SubjectType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `external` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SubjectType`
--

LOCK TABLES `SubjectType` WRITE;
/*!40000 ALTER TABLE `SubjectType` DISABLE KEYS */;
/*!40000 ALTER TABLE `SubjectType` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-01-03 13:58:55
