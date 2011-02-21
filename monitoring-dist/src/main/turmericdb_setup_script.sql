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
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AuditHistory`
--

LOCK TABLES `AuditHistory` WRITE;
/*!40000 ALTER TABLE `AuditHistory` DISABLE KEYS */;
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
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BasicAuth`
--

LOCK TABLES `BasicAuth` WRITE;
/*!40000 ALTER TABLE `BasicAuth` DISABLE KEYS */;
INSERT INTO `BasicAuth` VALUES (1,'jose','2010-12-22 14:13:54',NULL,NULL,'admin','admin'),(2,'jose','2010-12-22 14:13:54',NULL,NULL,'guest','guest');
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
-- Table structure for table `Error`
--

DROP TABLE IF EXISTS `Error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Error` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `errorId` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `organization` varchar(255) DEFAULT NULL,
  `severity` varchar(255) DEFAULT NULL,
  `subDomain` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `errorId` (`errorId`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Error`
--

LOCK TABLES `Error` WRITE;
/*!40000 ALTER TABLE `Error` DISABLE KEYS */;
INSERT INTO `Error` VALUES (1,'SYSTEM','TurmericSecurity',30211,'svc_security_policyenforcement_authz_failed','ebayopensource','ERROR','POLICYENFORCEMENT'),(2,'SYSTEM','TurmericSecurity',30002,'svc_security_unexpected_authn_error','ebayopensource','ERROR','AUTHN'),(3,'SYSTEM','TurmericSecurity',30001,'svc_security_authn_failed','ebayopensource','ERROR','AUTHN');
/*!40000 ALTER TABLE `Error` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ErrorValue`
--

DROP TABLE IF EXISTS `ErrorValue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ErrorValue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `aggregationPeriod` int(11) NOT NULL,
  `consumerName` varchar(255) DEFAULT NULL,
  `errorMessage` varchar(255) DEFAULT NULL,
  `operationName` varchar(255) DEFAULT NULL,
  `serverSide` bit(1) NOT NULL,
  `serviceAdminName` varchar(255) DEFAULT NULL,
  `timeStamp` bigint(20) NOT NULL,
  `error_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD7D0B0693BC9FC98` (`error_id`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ErrorValue`
--

LOCK TABLES `ErrorValue` WRITE;
/*!40000 ALTER TABLE `ErrorValue` DISABLE KEYS */;
INSERT INTO `ErrorValue` VALUES (1,0,'TMC','\"Authorization failed for SERVICE.PolicyService.updatePolicy.8 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293476031,1),(2,0,'TMC','\"Authorization failed for SERVICE.PolicyService.updatePolicy.74 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293476054,1),(3,0,'TMC','\"Authorization failed for SERVICE.PolicyService.deletePolicy.74 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293476046,1),(4,0,'TMC','\"Authorization failed for SERVICE.PolicyService.deletePolicy.8 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293476048,1),(5,0,'TMC','\"Authorization failed for SERVICE.PolicyService.deleteSubjectGroups.30 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293550721,1),(6,0,'Missing','Unexpected authentication error: unexpected null response from authenticator','authenticate','','AuthenticationService',1298293654292,2),(7,0,'TMC','Authentication failed : Unexpected authentication error: unexpected null response from authenticator','findPolicies','','PolicyService',1298293654298,3),(8,0,'Missing','Unexpected authentication error: unexpected null response from authenticator','authenticate','','AuthenticationService',1298293660556,2),(9,0,'TMC','Authentication failed : Unexpected authentication error: unexpected null response from authenticator','findPolicies','','PolicyService',1298293660565,3),(10,0,'TMC','\"Authorization failed for SERVICE.PolicyService.deletePolicy.8 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293686196,1),(11,0,'TMC','\"Authorization failed for SERVICE.PolicyService.updatePolicy.8 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293686208,1),(12,0,'TMC','\"Authorization failed for SERVICE.PolicyService.updatePolicy.74 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293686231,1),(13,0,'TMC','\"Authorization failed for SERVICE.PolicyService.deletePolicy.73 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293686232,1),(14,0,'TMC','\"Authorization failed for SERVICE.PolicyService.deletePolicy.74 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293686270,1),(15,0,'TMC','\"Authorization failed for SERVICE.PolicyService.updatePolicy.73 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293686279,1),(16,0,'TMC','\"Authorization failed for SERVICE.PolicyService.deletePolicy.? with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293686370,1),(17,0,'TMC','\"Authorization failed for SERVICE.PolicyService.deletePolicy.? with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293686580,1),(18,0,'TMC','\"Authorization failed for SERVICE.PolicyService.updatePolicy.? with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293686586,1),(19,0,'TMC','\"Authorization failed for SERVICE.PolicyService.updatePolicy.? with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293686595,1),(20,0,'TMC','\"Authorization failed for SERVICE.PolicyService.updatePolicy.? with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293686606,1),(21,0,'TMC','\"Authorization failed for SERVICE.PolicyService.deletePolicy.? with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298293686611,1),(22,0,'TMC','\"Authorization failed for SERVICE.PolicyService.deleteSubjectGroups.30 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298298963698,1),(23,0,'TMC','\"Authorization failed for SERVICE.PolicyService.deleteSubjectGroups.30 with error: User is not authorized.\"','verifyAccess','','PolicyEnforcementService',1298300941267,1);
/*!40000 ALTER TABLE `ErrorValue` ENABLE KEYS */;
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
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
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
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
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
) ENGINE=MyISAM AUTO_INCREMENT=148 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Metric`
--

LOCK TABLES `Metric` WRITE;
/*!40000 ALTER TABLE `Metric` DISABLE KEYS */;
INSERT INTO `Metric` VALUES (1,'getAuthenticationPolicy','PolicyService',1),(2,'getAuthenticationPolicy','PolicyService',2),(3,'authenticate','AuthenticationService',3),(4,'authenticate','AuthenticationService',2),(5,'getAuthenticationPolicy','PolicyService',3),(6,'getAuthenticationPolicy','PolicyService',4),(7,'getAuthenticationPolicy','PolicyService',5),(8,'getAuthenticationPolicy','PolicyService',6),(9,'getAuthenticationPolicy','PolicyService',7),(10,'getAuthenticationPolicy','PolicyService',8),(11,'getAuthenticationPolicy','PolicyService',9),(12,'getAuthenticationPolicy','PolicyService',10),(13,'getAuthenticationPolicy','PolicyService',11),(14,'getAuthenticationPolicy','PolicyService',12),(15,'getAuthenticationPolicy','PolicyService',13),(16,'getAuthenticationPolicy','PolicyService',14),(17,'getMetaData','PolicyService',2),(18,'findPolicies','PolicyService',7),(19,'getMetaData','PolicyService',1),(20,'findPolicies','PolicyService',5),(21,'findPolicies','PolicyService',11),(22,'findPolicies','PolicyService',9),(23,'findPolicies','PolicyService',10),(24,'authenticate','AuthenticationService',1),(25,'findPolicies','PolicyService',13),(26,'authenticate','AuthenticationService',14),(27,'getMetaData','PolicyService',6),(28,'getMetaData','PolicyService',8),(29,'authenticate','AuthenticationService',12),(30,'findPolicies','PolicyService',4),(31,'getMetaData','PolicyService',14),(32,'findPolicies','PolicyService',3),(33,'authenticate','AuthenticationService',6),(34,'authorize','AuthorizationService',3),(35,'authenticate','AuthenticationService',8),(36,'getMetaData','PolicyService',12),(37,'authorize','AuthorizationService',4),(38,'findPolicies','PolicyService',6),(39,'findPolicies','PolicyService',8),(40,'authenticate','AuthenticationService',4),(41,'authorize','AuthorizationService',6),(42,'authorize','AuthorizationService',8),(43,'findPolicies','PolicyService',12),(44,'authorize','AuthorizationService',14),(45,'getMetaData','PolicyService',4),(46,'findPolicies','PolicyService',14),(47,'getMetaData','PolicyService',3),(48,'authorize','AuthorizationService',12),(49,'authorize','AuthorizationService',1),(50,'authorize','AuthorizationService',2),(51,'findPolicies','PolicyService',2),(52,'getMetaData','PolicyService',7),(53,'findPolicies','PolicyService',1),(54,'getMetaData','PolicyService',5),(55,'getMetaData','PolicyService',13),(56,'getMetaData','PolicyService',11),(57,'getMetaData','PolicyService',9),(58,'getMetaData','PolicyService',10),(59,'verifyAccess','PolicyEnforcementService',15),(60,'getGroupMembers','GroupMembershipService',6),(61,'getGroupMembers','GroupMembershipService',8),(62,'getGroupMembers','GroupMembershipService',14),(63,'verifyAccess','PolicyEnforcementService',16),(64,'verifyAccess','PolicyEnforcementService',4),(65,'getGroupMembers','GroupMembershipService',12),(66,'verifyAccess','PolicyEnforcementService',3),(67,'getGroupMembers','GroupMembershipService',2),(68,'getGroupMembers','GroupMembershipService',1),(69,'verifyAccess','PolicyEnforcementService',7),(70,'verifyAccess','PolicyEnforcementService',5),(71,'verifyAccess','PolicyEnforcementService',13),(72,'verifyAccess','PolicyEnforcementService',11),(73,'verifyAccess','PolicyEnforcementService',9),(74,'verifyAccess','PolicyEnforcementService',17),(75,'verifyAccess','PolicyEnforcementService',2),(76,'verifyAccess','PolicyEnforcementService',1),(77,'verifyAccess','PolicyEnforcementService',8),(78,'verifyAccess','PolicyEnforcementService',6),(79,'verifyAccess','PolicyEnforcementService',18),(80,'verifyAccess','PolicyEnforcementService',14),(81,'verifyAccess','PolicyEnforcementService',12),(82,'getGroupMembers','GroupMembershipService',4),(83,'getGroupMembers','GroupMembershipService',3),(84,'findSubjectGroups','PolicyService',10),(85,'findSubjectGroups','PolicyService',11),(86,'findSubjectGroups','PolicyService',13),(87,'findSubjectGroups','PolicyService',4),(88,'findSubjectGroups','PolicyService',14),(89,'findSubjectGroups','PolicyService',6),(90,'findSubjectGroups','PolicyService',8),(91,'findSubjectGroups','PolicyService',1),(92,'findSubjectGroups','PolicyService',9),(93,'findSubjectGroups','PolicyService',5),(94,'findSubjectGroups','PolicyService',7),(95,'findSubjectGroups','PolicyService',3),(96,'findSubjectGroups','PolicyService',12),(97,'findSubjectGroups','PolicyService',2),(98,'findSubjects','PolicyService',1),(99,'findSubjects','PolicyService',8),(100,'findSubjects','PolicyService',6),(101,'findSubjects','PolicyService',14),(102,'findSubjects','PolicyService',4),(103,'findSubjects','PolicyService',13),(104,'findSubjects','PolicyService',10),(105,'findSubjects','PolicyService',11),(106,'findSubjects','PolicyService',2),(107,'findSubjects','PolicyService',12),(108,'findSubjects','PolicyService',3),(109,'findSubjects','PolicyService',5),(110,'findSubjects','PolicyService',7),(111,'findSubjects','PolicyService',9),(112,'getEntityHistory','PolicyService',13),(113,'getEntityHistory','PolicyService',11),(114,'getEntityHistory','PolicyService',10),(115,'getEntityHistory','PolicyService',4),(116,'getEntityHistory','PolicyService',6),(117,'getEntityHistory','PolicyService',8),(118,'getEntityHistory','PolicyService',14),(119,'getEntityHistory','PolicyService',1),(120,'getEntityHistory','PolicyService',7),(121,'getEntityHistory','PolicyService',5),(122,'getEntityHistory','PolicyService',9),(123,'getEntityHistory','PolicyService',3),(124,'getEntityHistory','PolicyService',12),(125,'getEntityHistory','PolicyService',2),(126,'getResources','PolicyService',3),(127,'getResources','PolicyService',5),(128,'getResources','PolicyService',7),(129,'getResources','PolicyService',9),(130,'getResources','PolicyService',2),(131,'getResources','PolicyService',12),(132,'getResources','PolicyService',4),(133,'getResources','PolicyService',10),(134,'getResources','PolicyService',11),(135,'getResources','PolicyService',13),(136,'getResources','PolicyService',1),(137,'getResources','PolicyService',6),(138,'getResources','PolicyService',8),(139,'getResources','PolicyService',14),(140,'authenticate','AuthenticationService',18),(141,'findPolicies','PolicyService',15),(142,'findPolicies','PolicyService',16),(143,'findPolicies','PolicyService',17),(144,'authenticate','AuthenticationService',17),(145,'authenticate','AuthenticationService',16),(146,'authenticate','AuthenticationService',15),(147,'findPolicies','PolicyService',18);
/*!40000 ALTER TABLE `Metric` ENABLE KEYS */;
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
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MetricClassifier`
--

LOCK TABLES `MetricClassifier` WRITE;
/*!40000 ALTER TABLE `MetricClassifier` DISABLE KEYS */;
INSERT INTO `MetricClassifier` VALUES (1,'Missing','Unknown','Unknown'),(2,'TMC','Unknown','Unknown');
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
) ENGINE=MyISAM AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MetricComponentDef`
--

LOCK TABLES `MetricComponentDef` WRITE;
/*!40000 ALTER TABLE `MetricComponentDef` DISABLE KEYS */;
INSERT INTO `MetricComponentDef` VALUES (1,'totalTime','java.lang.Double',1),(2,'count','java.lang.Long',1),(3,'count','java.lang.Long',2),(4,'totalTime','java.lang.Double',2),(5,'totalTime','java.lang.Double',3),(6,'count','java.lang.Long',3),(7,'totalTime','java.lang.Double',4),(8,'count','java.lang.Long',4),(9,'totalTime','java.lang.Double',5),(10,'count','java.lang.Long',5),(11,'totalTime','java.lang.Double',6),(12,'count','java.lang.Long',6),(13,'totalTime','java.lang.Double',7),(14,'count','java.lang.Long',7),(15,'totalTime','java.lang.Double',8),(16,'count','java.lang.Long',8),(17,'totalTime','java.lang.Double',9),(18,'count','java.lang.Long',9),(19,'totalTime','java.lang.Double',10),(20,'count','java.lang.Long',10),(21,'totalTime','java.lang.Double',11),(22,'count','java.lang.Long',11),(23,'totalTime','java.lang.Double',12),(24,'count','java.lang.Long',12),(25,'totalTime','java.lang.Double',13),(26,'count','java.lang.Long',13),(27,'totalTime','java.lang.Double',14),(28,'count','java.lang.Long',14),(29,'value','java.lang.Long',15),(30,'value','java.lang.Long',16),(31,'value','java.lang.Long',17),(32,'value','java.lang.Long',18);
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
) ENGINE=MyISAM AUTO_INCREMENT=2587 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MetricComponentValue`
--

LOCK TABLES `MetricComponentValue` WRITE;
/*!40000 ALTER TABLE `MetricComponentValue` DISABLE KEYS */;
INSERT INTO `MetricComponentValue` VALUES (1,1771480496,1,1),(2,3,2,1),(3,3,3,2),(4,3182359,4,2),(5,3,6,3),(6,25933134,5,3),(7,16231435,4,4),(8,3,3,4),(9,11487817,5,5),(10,3,6,5),(11,3,8,6),(12,3228666006,7,6),(13,27300646,9,7),(14,3,10,7),(15,3,12,8),(16,1803779256,11,8),(17,1737407722,13,9),(18,3,14,9),(19,26777437,15,10),(20,3,16,10),(21,3,18,11),(22,1474330,17,11),(23,2055282,19,12),(24,6,20,12),(25,3,22,13),(26,1823884384,21,13),(27,3,24,14),(28,1402948951,23,14),(29,1401032989,25,15),(30,3,26,15),(31,3,28,16),(32,18071004,27,16),(33,13976216,4,17),(34,3,3,17),(35,4,14,18),(36,3291363,13,18),(37,3,2,19),(38,14014326145,1,19),(39,4,10,20),(40,16385397,9,20),(41,6984067,1,21),(42,1,2,21),(43,1319595,4,22),(44,1,3,22),(45,1,3,23),(46,1017503,4,23),(47,323717532,21,24),(48,3,22,24),(49,1,18,25),(50,30814755,17,25),(51,4,18,26),(52,1563581,17,26),(53,2549510,19,27),(54,8,20,27),(55,4,2,28),(56,68947198,1,28),(57,4601821,25,29),(58,3,26,29),(59,6026678,27,30),(60,4,28,30),(61,8289671,11,31),(62,1,12,31),(63,112436,15,32),(64,1,16,32),(65,14025023367,11,33),(66,3,12,33),(67,3,16,34),(68,9070108,15,34),(69,1048836,23,35),(70,4,24,35),(71,330192212,7,36),(72,3,8,36),(73,1677013,27,37),(74,3,28,37),(75,9715905,5,38),(76,4,6,38),(77,998285,23,39),(78,1,24,39),(79,4,12,40),(80,9631632416,11,40),(81,2292548,5,41),(82,4,6,41),(83,4,16,42),(84,9558562839,15,42),(85,1242934435,23,43),(86,3,24,43),(87,3,8,44),(88,528126290,7,44),(89,1,28,45),(90,1136811,27,45),(91,321336634,11,46),(92,3,12,46),(93,301180656,15,47),(94,3,16,47),(95,9641841203,7,48),(96,4,8,48),(97,518167980,11,49),(98,3,12,49),(99,1,6,50),(100,1132366,5,50),(101,503872180,15,51),(102,3,16,51),(103,1,6,52),(104,2946220,5,52),(105,3,24,53),(106,5172657,23,53),(107,11644051,7,54),(108,1,8,54),(109,1980609,27,55),(110,3,28,55),(111,3,8,56),(112,15272318668,7,56),(113,1840747,27,57),(114,3,28,57),(115,3,6,58),(116,13109898032,5,58),(117,288489,23,59),(118,3,24,59),(119,5283087,9,60),(120,1,10,60),(121,11939704,1,61),(122,3,2,61),(123,4,3,62),(124,3983279,4,62),(125,511004,13,63),(126,1,14,63),(127,2772109,4,64),(128,4,3,64),(129,865312102,13,65),(130,3,14,65),(131,4,2,66),(132,22770303,1,66),(133,13146682640,9,67),(134,3,10,67),(135,533668,17,68),(136,1,18,68),(137,2,20,69),(138,844753,19,69),(139,1,22,70),(140,9762413,21,70),(141,1222462686,25,71),(142,3,26,71),(143,3,22,72),(144,14039858522,21,72),(145,1,26,73),(146,658678,25,73),(147,3,18,74),(148,11611544341,17,74),(149,6,20,75),(150,1492879840,19,75),(151,1,14,76),(152,402337,13,76),(153,8126917,13,77),(154,12,14,77),(155,4,29,78),(156,76249670,9,79),(157,1,10,79),(158,12,10,80),(159,63631318,9,80),(160,61414460,11,81),(161,2,12,81),(162,89887961,1,82),(163,10,2,82),(164,50698627,15,83),(165,2,16,83),(166,8782507,4,84),(167,10,3,84),(168,11573225,4,85),(169,10,3,85),(170,1,22,86),(171,256863376,21,86),(172,635009526,21,87),(173,13,22,87),(174,2,28,88),(175,1361949,27,88),(176,6680823,17,89),(177,12,18,89),(178,2,20,90),(179,42833457,19,90),(180,5155894,19,91),(181,24,20,91),(182,10,2,92),(183,45581777,1,92),(184,4,30,93),(185,10,8,94),(186,4356756092,7,94),(187,2,24,95),(188,270794,23,95),(189,536401223,5,96),(190,10,6,96),(191,8680458,25,97),(192,1,26,97),(193,16776555,25,98),(194,13,26,98),(195,4168730,4,99),(196,2,3,99),(197,16532713,27,100),(198,10,28,100),(199,98437784,11,101),(200,10,12,101),(201,9222942,1,102),(202,2,2,102),(203,636099,15,103),(204,10,16,103),(205,521382998,13,104),(206,10,14,104),(207,1005131,23,105),(208,10,24,105),(209,10,10,106),(210,619302948,9,106),(211,272136503,7,107),(212,1,8,107),(213,13,8,108),(214,662230402,7,108),(215,274430038,25,109),(216,10,26,109),(217,74486331,5,110),(218,1,6,110),(219,35513468,5,111),(220,12,6,111),(221,42484891,23,112),(222,10,24,112),(223,422489440,11,113),(224,10,12,113),(225,22164646,5,114),(226,14,6,114),(227,10,16,115),(228,365156964,15,115),(229,4041756832,21,116),(230,10,22,116),(231,1964521710,7,117),(232,15,8,117),(233,10,18,118),(234,499343455,17,118),(235,23616782,27,119),(236,10,28,119),(237,4,31,120),(238,255854435,11,121),(239,1,12,121),(240,591253715,11,122),(241,13,12,122),(242,178001855,15,123),(243,1,16,123),(244,13,16,124),(245,467651471,15,124),(246,10,3,125),(247,44894983,4,125),(248,1196321476,1,126),(249,10,2,126),(250,10,8,127),(251,466610071,7,127),(252,15,12,128),(253,1930568352,11,128),(254,19291327,5,129),(255,10,6,129),(256,15,16,130),(257,1806187699,15,130),(258,34418075,5,131),(259,10,6,131),(260,14587525,23,132),(261,1,24,132),(262,13,24,133),(263,20018747,23,133),(264,194919805,7,134),(265,10,8,134),(266,10179543,27,135),(267,15,28,135),(268,1,28,136),(269,601944,27,136),(270,39707562,27,137),(271,13,28,137),(272,15,24,138),(273,1624113,23,138),(274,10,10,139),(275,63039622,9,139),(276,15,2,140),(277,87662207,1,140),(278,14,3,141),(279,43804853,4,141),(280,3077423,13,142),(281,10,14,142),(282,1211951,4,143),(283,1,3,143),(284,19404077,4,144),(285,12,3,144),(286,2705370784,15,145),(287,10,16,145),(288,10,12,146),(289,3922160460,11,146),(290,77189124,1,147),(291,1,2,147),(292,12,2,148),(293,99128900,1,148),(294,4,32,149),(295,10,28,150),(296,115894272,27,150),(297,10,18,151),(298,9752740,17,151),(299,6171433,19,152),(300,20,20,152),(301,136390780,21,153),(302,10,22,153),(303,302165587,23,154),(304,10,24,154),(305,2,8,155),(306,64547971,7,155),(307,10,26,156),(308,28042442,25,156),(309,2,6,157),(310,1228873,5,157),(311,1,29,158),(312,33954647,19,159),(313,2,20,159),(314,63620346,21,160),(315,1,22,160),(316,23219131,1,161),(317,5,2,161),(318,1,26,162),(319,961974,25,162),(320,3207072,4,163),(321,5,3,163),(322,276275816,21,164),(323,1,22,164),(324,172604389,21,165),(325,5,22,165),(326,13256747,19,166),(327,2,20,166),(328,2051326,19,167),(329,10,20,167),(330,1,30,168),(331,485079382,7,169),(332,3,8,169),(333,113245,23,170),(334,1,24,170),(335,2881794,25,171),(336,1,26,171),(337,5,26,172),(338,3570912,25,172),(339,1,3,173),(340,12423083,4,173),(341,5,12,174),(342,26729861,11,174),(343,1,8,175),(344,66126956,7,175),(345,5,16,176),(346,360653,15,176),(347,5,24,177),(348,504389,23,177),(349,284660504,7,178),(350,1,8,178),(351,5,8,179),(352,180214296,7,179),(353,3,26,180),(354,1679180,25,180),(355,3,22,181),(356,474489139,21,181),(357,300078444,7,182),(358,6,8,182),(359,3050475,27,183),(360,5,28,183),(361,1,31,184),(362,1,12,185),(363,275239776,11,185),(364,167149174,11,186),(365,5,12,186),(366,212757712,15,187),(367,1,16,187),(368,120733587,15,188),(369,5,16,188),(370,3,2,189),(371,196394452,1,189),(372,6,12,190),(373,281987518,11,190),(374,20798083,5,191),(375,5,6,191),(376,242725651,15,192),(377,6,16,192),(378,501759,27,193),(379,1,28,193),(380,5,8,194),(381,36768913,7,194),(382,62853758,11,195),(383,1,12,195),(384,6773351,27,196),(385,6,28,196),(386,1,16,197),(387,8556016,15,197),(388,1,28,198),(389,668594,27,198),(390,4209348,27,199),(391,5,28,199),(392,29486191,1,200),(393,6,2,200),(394,3,16,201),(395,262631393,15,201),(396,460982307,11,202),(397,3,12,202),(398,61910585,1,203),(399,1,2,203),(400,5,2,204),(401,39596061,1,204),(402,12360055,27,205),(403,3,28,205),(404,10,20,206),(405,1757775,19,206),(406,30972638,21,207),(407,5,22,207),(408,53764434,1,208),(409,1,2,208),(410,5,26,209),(411,2173678,25,209),(412,670562,5,210),(413,1,6,210),(414,16068641,17,211),(415,1,18,211),(416,1512724,13,212),(417,1,14,212),(418,5282124,13,213),(419,5,14,213),(420,49106556,9,214),(421,1,10,214),(422,5,10,215),(423,24992512,9,215),(424,1,12,216),(425,23064962,11,216),(426,1,16,217),(427,8608707,15,217),(428,5,3,218),(429,3432370,4,218),(430,1,10,219),(431,52927040,9,219),(432,1,28,220),(433,751640,27,220),(434,32792184,17,221),(435,1,18,221),(436,5,18,222),(437,1628277,17,222),(438,5,2,223),(439,27674451,1,223),(440,1,14,224),(441,352443,13,224),(442,3,6,225),(443,170379762,5,225),(444,3324134,27,226),(445,5,28,226),(446,1,6,227),(447,51715548,5,227),(448,13728443,1,228),(449,1,2,228),(450,2050213,13,229),(451,3,14,229),(452,3,10,230),(453,191830866,9,230),(454,46887533,5,231),(455,1,6,231),(456,5,6,232),(457,8074460,5,232),(458,5,24,233),(459,3383401,23,233),(460,5,12,234),(461,189957922,11,234),(462,6,6,235),(463,3345496,5,235),(464,157532268,15,236),(465,5,16,236),(466,3,18,237),(467,160120890,17,237),(468,3,3,238),(469,17845782,4,238),(470,1,24,239),(471,2061501,23,239),(472,5,8,240),(473,197316443,7,240),(474,5,6,241),(475,11813596,5,241),(476,1,24,242),(477,7963271,23,242),(478,4832612,23,243),(479,5,24,243),(480,6,24,244),(481,581805,23,244),(482,17551071,9,245),(483,5,10,245),(484,6,3,246),(485,14417965,4,246),(486,5,14,247),(487,1991474,13,247),(488,894087,4,248),(489,1,3,248),(490,13041758,4,249),(491,5,3,249),(492,1,32,250),(493,1635249,17,251),(494,5,18,251),(495,3,24,252),(496,7065905,23,252),(497,24823083,7,253),(498,1,8,253),(499,1,3,254),(500,786802,4,254),(501,89275684,1,255),(502,1,2,255),(503,2,20,256),(504,5216988,19,256),(505,1,22,257),(506,34464922,21,257),(507,2,2,258),(508,9774258,1,258),(509,993627,25,259),(510,1,26,259),(511,1479599,4,260),(512,2,3,260),(513,1,22,261),(514,33042510,21,261),(515,263839,19,262),(516,2,20,262),(517,1,26,263),(518,1169256,25,263),(519,11017586,11,264),(520,2,12,264),(521,37216311,7,265),(522,1,8,265),(523,2,16,266),(524,156678,15,266),(525,2,24,267),(526,216342,23,267),(527,1,16,268),(528,8255095,15,268),(529,98338706,11,269),(530,1,12,269),(531,35194893,7,270),(532,1,8,270),(533,649224,27,271),(534,1,28,271),(535,49805060,7,272),(536,2,8,272),(537,1291124,27,273),(538,2,28,273),(539,32150238,11,274),(540,1,12,274),(541,27493798,15,275),(542,1,16,275),(543,2,12,276),(544,47177169,11,276),(545,2,6,277),(546,1123253,5,277),(547,41771546,15,278),(548,2,16,278),(549,1,28,279),(550,475936,27,279),(551,2,8,280),(552,15554895,7,280),(553,1,12,281),(554,33700343,11,281),(555,2,28,282),(556,1136876,27,282),(557,1,16,283),(558,5836550,15,283),(559,651266,27,284),(560,1,28,284),(561,103244546,7,285),(562,1,8,285),(563,2,2,286),(564,4034181,1,286),(565,1,2,287),(566,4061539,1,287),(567,1,26,288),(568,1396707,25,288),(569,661195,19,289),(570,4,20,289),(571,2,22,290),(572,12838675,21,290),(573,47814467,19,291),(574,2,20,291),(575,27329096,1,292),(576,1,2,292),(577,1022600,25,293),(578,2,26,293),(579,1,22,294),(580,99441380,21,294),(581,18813559,17,295),(582,1,18,295),(583,1,14,296),(584,920764,13,296),(585,1,3,297),(586,868423,4,297),(587,1,10,298),(588,2564199,9,298),(589,2,3,299),(590,3522061,4,299),(591,26347727,9,300),(592,1,10,300),(593,1,18,301),(594,301758,17,301),(595,3968507,1,302),(596,2,2,302),(597,1,14,303),(598,374731,13,303),(599,1515955,27,304),(600,2,28,304),(601,24683478,5,305),(602,1,6,305),(603,1,6,306),(604,1240886,5,306),(605,1554851,23,307),(606,2,24,307),(607,48497919,11,308),(608,2,12,308),(609,2,6,309),(610,1254502,5,309),(611,2,16,310),(612,43211927,15,310),(613,2836805,23,311),(614,1,24,311),(615,1,24,312),(616,2086593,23,312),(617,51715841,7,313),(618,2,8,313),(619,3528722,5,314),(620,2,6,314),(621,1424901,23,315),(622,1,24,315),(623,86502468,5,316),(624,1,6,316),(625,2,24,317),(626,187259,23,317),(627,7946626,9,318),(628,2,10,318),(629,1610043,4,319),(630,2,3,319),(631,2,14,320),(632,533001,13,320),(633,736926,4,321),(634,1,3,321),(635,87951877,9,322),(636,1,10,322),(637,508950,13,323),(638,1,14,323),(639,712878,17,324),(640,2,18,324),(641,36349432,17,325),(642,1,18,325),(643,1,3,326),(644,1098144,4,326),(645,1,2,327),(646,3761738,1,327),(647,1,3,328),(648,565946,4,328),(649,1,22,329),(650,23973214,21,329),(651,355650,19,330),(652,2,20,330),(653,275949,25,331),(654,1,26,331),(655,1,22,332),(656,65977605,21,332),(657,1,26,333),(658,1782236,25,333),(659,37128543,19,334),(660,2,20,334),(661,4527513,11,335),(662,1,12,335),(663,77892,15,336),(664,1,16,336),(665,65066,23,337),(666,1,24,337),(667,1,8,338),(668,27116958,7,338),(669,67320488,7,339),(670,1,8,339),(671,36101416,7,340),(672,1,8,340),(673,1,28,341),(674,621265,27,341),(675,1,12,342),(676,22268263,11,342),(677,1,16,343),(678,18327921,15,343),(679,65184569,11,344),(680,1,12,344),(681,6474632,15,345),(682,1,16,345),(683,34749541,11,346),(684,1,12,346),(685,1,6,347),(686,621001,5,347),(687,32881929,15,348),(688,1,16,348),(689,1,28,349),(690,470451,27,349),(691,1,8,350),(692,6611460,7,350),(693,714908,27,351),(694,1,28,351),(695,1359539,27,352),(696,1,28,352),(697,1339567,1,353),(698,1,2,353),(699,58171415,1,354),(700,1,2,354),(701,1,2,355),(702,2899219,1,355),(703,2,20,356),(704,386390,19,356),(705,1,22,357),(706,5414857,21,357),(707,1,26,358),(708,443362,25,358),(709,1,14,359),(710,411312,13,359),(711,1,10,360),(712,2002965,9,360),(713,532859,13,361),(714,1,14,361),(715,1,3,362),(716,696316,4,362),(717,57147328,9,363),(718,1,10,363),(719,199143,17,364),(720,1,18,364),(721,1,2,365),(722,1884227,1,365),(723,17060593,17,366),(724,1,18,366),(725,1,28,367),(726,457076,27,367),(727,1,6,368),(728,1079999,5,368),(729,1,24,369),(730,698231,23,369),(731,14774452,11,370),(732,1,12,370),(733,417486,5,371),(734,1,6,371),(735,12434722,15,372),(736,1,16,372),(737,55642369,5,373),(738,1,6,373),(739,15759356,7,374),(740,1,8,374),(741,1550664,5,375),(742,1,6,375),(743,1,24,376),(744,2108302,23,376),(745,1,24,377),(746,1073659,23,377),(747,1,24,378),(748,56830,23,378),(749,1,3,379),(750,818852,4,379),(751,2720223,9,380),(752,1,10,380),(753,1,3,381),(754,457347,4,381),(755,337190,13,382),(756,1,14,382),(757,424653,4,383),(758,1,3,383),(759,356105,17,384),(760,1,18,384),(761,97950224,1,385),(762,2,2,385),(763,17028682,19,386),(764,4,20,386),(765,119712915,21,387),(766,2,22,387),(767,4,2,388),(768,20316846,1,388),(769,2,26,389),(770,3019439,25,389),(771,3632455,4,390),(772,4,3,390),(773,4,12,391),(774,24183548,11,391),(775,134164930,7,392),(776,2,8,392),(777,4,16,393),(778,370773,15,393),(779,4,24,394),(780,467762,23,394),(781,2,16,395),(782,29549781,15,395),(783,2,12,396),(784,128778609,11,396),(785,1555119,27,397),(786,2,28,397),(787,4,8,398),(788,23949965,7,398),(789,3421954,27,399),(790,4,28,399),(791,15994279,11,400),(792,4,12,400),(793,3188113,5,401),(794,4,6,401),(795,1965029,15,402),(796,4,16,402),(797,1066565,27,403),(798,2,28,403),(799,4,8,404),(800,34747451,7,404),(801,118155634,11,405),(802,2,12,405),(803,3510421,27,406),(804,4,28,406),(805,2,16,407),(806,26779005,15,407),(807,155921532,7,408),(808,2,8,408),(809,10551966,1,409),(810,4,2,409),(811,2,26,410),(812,20157686,25,410),(813,2695760,19,411),(814,8,20,411),(815,4,22,412),(816,28653409,21,412),(817,4,20,413),(818,34958363,19,413),(819,2,2,414),(820,90334768,1,414),(821,4,26,415),(822,2314399,25,415),(823,131019649,21,416),(824,2,22,416),(825,2,18,417),(826,62474097,17,417),(827,2845385,4,418),(828,2,3,418),(829,3493290,4,419),(830,4,3,419),(831,86704497,9,420),(832,2,10,420),(833,4,2,421),(834,10661361,1,421),(835,1759208,13,422),(836,2,14,422),(837,4,28,423),(838,3512113,27,423),(839,2,6,424),(840,81694134,5,424),(841,4,24,425),(842,3395039,23,425),(843,4,12,426),(844,97366829,11,426),(845,4,6,427),(846,3267185,5,427),(847,4,16,428),(848,83337178,15,428),(849,2,24,429),(850,23863419,23,429),(851,5511303,23,430),(852,2,24,430),(853,4,8,431),(854,105067000,7,431),(855,4,6,432),(856,8962867,5,432),(857,88450552,5,433),(858,2,6,433),(859,4,24,434),(860,545273,23,434),(861,15019932,9,435),(862,4,10,435),(863,3461895,4,436),(864,4,3,436),(865,1622257,13,437),(866,4,14,437),(867,94305132,9,438),(868,2,10,438),(869,2,14,439),(870,1731885,13,439),(871,4,18,440),(872,1783586,17,440),(873,51292878,17,441),(874,2,18,441),(875,2,3,442),(876,1532084,4,442),(877,73939391,1,443),(878,1,2,443),(879,6413956,19,444),(880,2,20,444),(881,1,22,445),(882,55725075,21,445),(883,23084479,1,446),(884,3,2,446),(885,1375829,25,447),(886,1,26,447),(887,3,3,448),(888,2844930,4,448),(889,22646175,21,449),(890,1,22,449),(891,67972382,5,450),(892,1,6,450),(893,251042,19,451),(894,2,20,451),(895,848826,25,452),(896,1,26,452),(897,1,10,453),(898,69041066,9,453),(899,25519629,11,454),(900,3,12,454),(901,59628215,7,455),(902,1,8,455),(903,274122,13,456),(904,1,14,456),(905,256500,15,457),(906,3,16,457),(907,318791,23,458),(908,3,24,458),(909,10686626,15,459),(910,1,16,459),(911,1,12,460),(912,85169621,11,460),(913,1,8,461),(914,24034565,7,461),(915,31747627,17,462),(916,1,18,462),(917,610584,27,463),(918,1,28,463),(919,41843566,7,464),(920,3,8,464),(921,3,28,465),(922,2409792,27,465),(923,1,12,466),(924,21987457,11,466),(925,18321564,15,467),(926,1,16,467),(927,3,12,468),(928,37738625,11,468),(929,1,3,469),(930,658501,4,469),(931,2708943,5,470),(932,3,6,470),(933,29450857,15,471),(934,3,16,471),(935,1,28,472),(936,750503,27,472),(937,37904607,7,473),(938,3,8,473),(939,1,12,474),(940,54569807,11,474),(941,3,28,475),(942,1821507,27,475),(943,8779969,15,476),(944,1,16,476),(945,1,28,477),(946,478012,27,477),(947,92487864,7,478),(948,1,8,478),(949,6444019,1,479),(950,3,2,479),(951,3204146,1,480),(952,1,2,480),(953,2742096,25,481),(954,1,26,481),(955,1671131,19,482),(956,6,20,482),(957,3,22,483),(958,30201476,21,483),(959,2475060,23,484),(960,1,24,484),(961,6088405,19,485),(962,2,20,485),(963,44995010,1,486),(964,1,2,486),(965,2921712,25,487),(966,3,26,487),(967,86098157,21,488),(968,1,22,488),(969,33732542,17,489),(970,1,18,489),(971,1,14,490),(972,416283,13,490),(973,1290358,4,491),(974,1,3,491),(975,1,10,492),(976,2216129,9,492),(977,3,3,493),(978,2628240,4,493),(979,43354485,9,494),(980,1,10,494),(981,258716,17,495),(982,1,18,495),(983,1,8,496),(984,90256556,7,496),(985,8449542,1,497),(986,3,2,497),(987,704692,13,498),(988,1,14,498),(989,3,28,499),(990,26405431,27,499),(991,41060652,5,500),(992,1,6,500),(993,34278010,19,501),(994,2,20,501),(995,87266647,21,502),(996,1,22,502),(997,1,6,503),(998,1111212,5,503),(999,3,24,504),(1000,5228768,23,504),(1001,86468634,11,505),(1002,3,12,505),(1003,3,6,506),(1004,1762174,5,506),(1005,3,16,507),(1006,75786720,15,507),(1007,1,26,508),(1008,1304772,25,508),(1009,1,24,509),(1010,5827251,23,509),(1011,1,24,510),(1012,3587543,23,510),(1013,123007280,7,511),(1014,3,8,511),(1015,69783536,1,512),(1016,1,2,512),(1017,5843829,5,513),(1018,3,6,513),(1019,1030947,23,514),(1020,1,24,514),(1021,70351792,5,515),(1022,1,6,515),(1023,3,24,516),(1024,274030,23,516),(1025,10028630,9,517),(1026,3,10,517),(1027,2185508,4,518),(1028,3,3,518),(1029,86543799,11,519),(1030,1,12,519),(1031,915715,13,520),(1032,3,14,520),(1033,16300969,15,521),(1034,1,16,521),(1035,1,3,522),(1036,574584,4,522),(1037,72690018,9,523),(1038,1,10,523),(1039,630283,13,524),(1040,1,14,524),(1041,1568458,17,525),(1042,3,18,525),(1043,1,18,526),(1044,63644346,17,526),(1045,1120348,4,527),(1046,1,3,527),(1047,463843,27,528),(1048,1,28,528),(1049,2965360,1,529),(1050,1,2,529),(1051,570592,4,530),(1052,1,3,530),(1053,29410178,21,531),(1054,1,22,531),(1055,481925,25,532),(1056,1,26,532),(1057,1,32,533),(1058,3700642,11,534),(1059,1,12,534),(1060,81582,15,535),(1061,1,16,535),(1062,1,29,536),(1063,1,24,537),(1064,67619,23,537),(1065,31081288,7,538),(1066,1,8,538),(1067,1,30,539),(1068,1,28,540),(1069,2033476,27,540),(1070,28496960,11,541),(1071,1,12,541),(1072,389425,5,542),(1073,1,6,542),(1074,1,8,543),(1075,7624499,7,543),(1076,1,28,544),(1077,600847,27,544),(1078,1,31,545),(1079,442917,19,546),(1080,2,20,546),(1081,1,22,547),(1082,6154661,21,547),(1083,1,26,548),(1084,466055,25,548),(1085,1,3,549),(1086,460971,4,549),(1087,1,31,550),(1088,23477840,17,551),(1089,1,18,551),(1090,1391598,1,552),(1091,1,2,552),(1092,1,28,553),(1093,3139742,27,553),(1094,1,6,554),(1095,27858338,5,554),(1096,1,24,555),(1097,706827,23,555),(1098,1,12,556),(1099,17474310,11,556),(1100,15528170,15,557),(1101,1,16,557),(1102,21127329,7,558),(1103,1,8,558),(1104,1,30,559),(1105,1,29,560),(1106,1262669,5,561),(1107,1,6,561),(1108,1388269,23,562),(1109,1,24,562),(1110,1,32,563),(1111,1,10,564),(1112,2180180,9,564),(1113,288942,13,565),(1114,1,14,565),(1115,599331,4,566),(1116,1,3,566),(1117,1,18,567),(1118,246929,17,567),(1119,1,2,568),(1120,4947687,1,568),(1121,1092616,4,569),(1122,1,3,569),(1123,1,22,570),(1124,40223281,21,570),(1125,458854,25,571),(1126,1,26,571),(1127,1,32,572),(1128,5829883,11,573),(1129,1,12,573),(1130,88907,15,574),(1131,1,16,574),(1132,1,29,575),(1133,124578,23,576),(1134,1,24,576),(1135,1,8,577),(1136,41681187,7,577),(1137,1,30,578),(1138,895893,27,579),(1139,1,28,579),(1140,1,12,580),(1141,39544305,11,580),(1142,1,6,581),(1143,1034573,5,581),(1144,1,8,582),(1145,8614069,7,582),(1146,413659,27,583),(1147,1,28,583),(1148,1,31,584),(1149,2,20,585),(1150,592587,19,585),(1151,7003646,21,586),(1152,1,22,586),(1153,583778,25,587),(1154,1,26,587),(1155,951603,4,588),(1156,1,3,588),(1157,1,31,589),(1158,1,18,590),(1159,34157169,17,590),(1160,3299329,1,591),(1161,1,2,591),(1162,5345702,27,592),(1163,1,28,592),(1164,38460637,5,593),(1165,1,6,593),(1166,870655,23,594),(1167,1,24,594),(1168,24453851,11,595),(1169,1,12,595),(1170,20103069,15,596),(1171,1,16,596),(1172,1,8,597),(1173,30880523,7,597),(1174,1,30,598),(1175,1,29,599),(1176,1,6,600),(1177,2040670,5,600),(1178,1195319,23,601),(1179,1,24,601),(1180,1,32,602),(1181,3653146,9,603),(1182,1,10,603),(1183,412130,13,604),(1184,1,14,604),(1185,1,3,605),(1186,1053422,4,605),(1187,1,18,606),(1188,427431,17,606),(1189,12,29,607),(1190,13,2,608),(1191,132625703,1,608),(1192,14896789,4,609),(1193,13,3,609),(1194,1,22,610),(1195,118963767,21,610),(1196,2,20,611),(1197,4558999,19,611),(1198,12,30,612),(1199,995671876,7,613),(1200,12,8,613),(1201,2,24,614),(1202,217231,23,614),(1203,1,26,615),(1204,6750457,25,615),(1205,2187856,4,616),(1206,2,3,616),(1207,141443423,11,617),(1208,13,12,617),(1209,939875,15,618),(1210,13,16,618),(1211,1112861,23,619),(1212,13,24,619),(1213,128957646,7,620),(1214,1,8,620),(1215,12,26,621),(1216,12311606,25,621),(1217,953788578,21,622),(1218,12,22,622),(1219,152749106,7,623),(1220,19,8,623),(1221,13,28,624),(1222,7694847,27,624),(1223,12,31,625),(1224,118023132,11,626),(1225,1,12,626),(1226,1,16,627),(1227,92385689,15,627),(1228,12,2,628),(1229,645984854,1,628),(1230,118536026,11,629),(1231,19,12,629),(1232,19765325,5,630),(1233,13,6,630),(1234,26734710,15,631),(1235,19,16,631),(1236,212771724,7,632),(1237,13,8,632),(1238,19,28,633),(1239,17521505,27,633),(1240,584979,27,634),(1241,1,28,634),(1242,19,2,635),(1243,73876929,1,635),(1244,218441870,15,636),(1245,12,16,636),(1246,891102112,11,637),(1247,12,12,637),(1248,24926799,1,638),(1249,1,2,638),(1250,59146036,27,639),(1251,12,28,639),(1252,41305691,19,640),(1253,26,20,640),(1254,13,22,641),(1255,151833088,21,641),(1256,5972728,25,642),(1257,13,26,642),(1258,1082608,5,643),(1259,2,6,643),(1260,361132,13,644),(1261,1,14,644),(1262,1,10,645),(1263,24154147,9,645),(1264,10785528,11,646),(1265,2,12,646),(1266,5339663,15,647),(1267,2,16,647),(1268,13,3,648),(1269,17409856,4,648),(1270,2,28,649),(1271,1081062,27,649),(1272,1,18,650),(1273,17981251,17,650),(1274,13,2,651),(1275,43217153,1,651),(1276,548484852,5,652),(1277,12,6,652),(1278,13,28,653),(1279,7344824,27,653),(1280,4384987,1,654),(1281,2,2,654),(1282,43597860,13,655),(1283,12,14,655),(1284,592281122,9,656),(1285,12,10,656),(1286,23077185,5,657),(1287,1,6,657),(1288,8802636,23,658),(1289,13,24,658),(1290,501230880,11,659),(1291,13,12,659),(1292,15110106,5,660),(1293,19,6,660),(1294,446767904,15,661),(1295,13,16,661),(1296,12,18,662),(1297,540780172,17,662),(1298,12,3,663),(1299,34761246,4,663),(1300,528263397,7,664),(1301,13,8,664),(1302,56967892,5,665),(1303,13,6,665),(1304,9347339,23,666),(1305,1,24,666),(1306,19,24,667),(1307,2832955,23,667),(1308,96896994,9,668),(1309,13,10,668),(1310,19,3,669),(1311,37071887,4,669),(1312,13,14,670),(1313,3752279,13,670),(1314,1,3,671),(1315,705282,4,671),(1316,12,32,672),(1317,6185630,17,673),(1318,13,18,673),(1319,31231302,23,674),(1320,12,24,674),(1321,14470672,7,675),(1322,2,8,675),(1323,3,3,676),(1324,13976216,4,676),(1325,24,14,677),(1326,18448763,13,677),(1327,14014326145,1,678),(1328,3,2,678),(1329,24,10,679),(1330,111792520,9,679),(1331,41,2,680),(1332,317567230,1,680),(1333,41,3,681),(1334,42696809,4,681),(1335,41,3,682),(1336,40880727,4,682),(1337,24,22,683),(1338,1210993346,21,683),(1339,5,18,684),(1340,139223199,17,684),(1341,24,18,685),(1342,10632298,17,685),(1343,10627261,19,686),(1344,48,20,686),(1345,215075143,1,687),(1346,44,2,687),(1347,28749606,25,688),(1348,24,26,688),(1349,44,28,689),(1350,73604368,27,689),(1351,349679540,11,690),(1352,41,12,690),(1353,41,16,691),(1354,3081395,15,691),(1355,14025023367,11,692),(1356,3,12,692),(1357,9070108,15,693),(1358,3,16,693),(1359,44,24,694),(1360,4931375,23,694),(1361,1258983326,7,695),(1362,24,8,695),(1363,1677013,27,696),(1364,3,28,696),(1365,24,6,697),(1366,56735930,5,697),(1367,68123584,23,698),(1368,41,24,698),(1369,44,12,699),(1370,11034346653,11,699),(1371,49614143,5,700),(1372,53,6,700),(1373,44,16,701),(1374,10778421761,15,701),(1375,3,24,702),(1376,1242934435,23,702),(1377,53,8,703),(1378,3097175557,7,703),(1379,46172419,27,704),(1380,41,28,704),(1381,24,12,705),(1382,1156145481,11,705),(1383,953708997,15,706),(1384,24,16,706),(1385,11181588443,7,707),(1386,44,8,707),(1387,2984919490,11,708),(1388,53,12,708),(1389,70052409,5,709),(1390,41,6,709),(1391,53,16,710),(1392,2685589601,15,710),(1393,41,6,711),(1394,129335204,5,711),(1395,34588166,23,712),(1396,24,24,712),(1397,41,8,713),(1398,567161474,7,713),(1399,43638720,27,714),(1400,53,28,714),(1401,3,8,715),(1402,15272318668,7,715),(1403,48246474,27,716),(1404,24,28,716),(1405,13109898032,5,717),(1406,3,6,717),(1407,6390754,23,718),(1408,53,24,718),(1409,224319511,9,719),(1410,41,10,719),(1411,53,2,720),(1412,225334764,1,720),(1413,106992777,4,721),(1414,53,3,721),(1415,13441415,13,722),(1416,41,14,722),(1417,36954107,4,723),(1418,24,3,723),(1419,865312102,13,724),(1420,3,14,724),(1421,171660168,1,725),(1422,24,2,725),(1423,3,10,726),(1424,13146682640,9,726),(1425,41,18,727),(1426,23202674,17,727),(1427,82,20,728),(1428,56529632,19,728),(1429,419225643,21,729),(1430,41,22,729),(1431,1222462686,25,730),(1432,3,26,730),(1433,14039858522,21,731),(1434,3,22,731),(1435,41,26,732),(1436,44599432,25,732),(1437,3,18,733),(1438,11611544341,17,733),(1439,1492879840,19,734),(1440,6,20,734),(1441,3,14,735),(1442,2276193,13,735),(1443,17,29,736),(1444,149510373,9,737),(1445,3,10,737),(1446,95264950,11,738),(1447,5,12,738),(1448,5,16,739),(1449,64646997,15,739),(1450,721736418,21,740),(1451,5,22,740),(1452,5,28,741),(1453,3194651,27,741),(1454,6,20,742),(1455,60649203,19,742),(1456,17,30,743),(1457,25,8,744),(1458,5837507350,7,744),(1459,601270,23,745),(1460,5,24,745),(1461,25,6,746),(1462,1255265837,5,746),(1463,19253488,25,747),(1464,5,26,747),(1465,5,3,748),(1466,18779669,4,748),(1467,5,2,749),(1468,27336372,1,749),(1469,25,14,750),(1470,567031071,13,750),(1471,1403414936,9,751),(1472,25,10,751),(1473,758517128,7,752),(1474,5,8,752),(1475,25,26,753),(1476,288420824,25,753),(1477,5,6,754),(1478,210770024,5,754),(1479,25,22,755),(1480,5470034549,21,755),(1481,1200244517,17,756),(1482,25,18,756),(1483,17,31,757),(1484,717158608,11,758),(1485,5,12,758),(1486,483145256,15,759),(1487,3,16,759),(1488,97502011,4,760),(1489,25,3,760),(1490,2038700782,1,761),(1491,25,2,761),(1492,5,24,762),(1493,34481723,23,762),(1494,5,28,763),(1495,2870023,27,763),(1496,4464073,4,764),(1497,5,3,764),(1498,3186444047,15,765),(1499,25,16,765),(1500,25,12,766),(1501,5274244879,11,766),(1502,164026508,1,767),(1503,3,2,767),(1504,17,32,768),(1505,187400363,27,769),(1506,25,28,769),(1507,25,24,770),(1508,340462794,23,770),(1509,103841726,7,771),(1510,5,8,771),(1511,5,6,772),(1512,2982043,5,772),(1513,10,20,773),(1514,62614273,19,773),(1515,5,22,774),(1516,273523258,21,774),(1517,5,26,775),(1518,6350869,25,775),(1519,297136412,7,776),(1520,5,8,776),(1521,2794763,27,777),(1522,5,28,777),(1523,5,12,778),(1524,269279542,11,778),(1525,5,16,779),(1526,49951540,15,779),(1527,216423308,1,780),(1528,5,2,780),(1529,5,18,781),(1530,131088839,17,781),(1531,209333749,9,782),(1532,5,10,782),(1533,5,14,783),(1534,3191074,13,783),(1535,199153812,5,784),(1536,5,6,784),(1537,13246940,23,785),(1538,5,24,785),(1539,4537378,4,786),(1540,5,3,786),(1541,261165299,1,787),(1542,4,2,787),(1543,4,16,788),(1544,48491502,15,788),(1545,4,12,789),(1546,312286936,11,789),(1547,2814927,27,790),(1548,4,28,790),(1549,351653942,7,791),(1550,4,8,791),(1551,24296489,25,792),(1552,4,26,792),(1553,88861235,19,793),(1554,8,20,793),(1555,4,22,794),(1556,316559186,21,794),(1557,5004166,4,795),(1558,4,3,795),(1559,4,24,796),(1560,32527475,23,796),(1561,4,6,797),(1562,245304812,5,797),(1563,4,10,798),(1564,254947027,9,798),(1565,4,14,799),(1566,2871118,13,799),(1567,4,18,800),(1568,151286656,17,800),(1569,1,26,801),(1570,275949,25,801),(1571,65977605,21,802),(1572,1,22,802),(1573,37128543,19,803),(1574,2,20,803),(1575,67320488,7,804),(1576,1,8,804),(1577,65184569,11,805),(1578,1,12,805),(1579,6474632,15,806),(1580,1,16,806),(1581,1,28,807),(1582,470451,27,807),(1583,58171415,1,808),(1584,1,2,808),(1585,532859,13,809),(1586,1,14,809),(1587,57147328,9,810),(1588,1,10,810),(1589,17060593,17,811),(1590,1,18,811),(1591,1,6,812),(1592,55642369,5,812),(1593,1073659,23,813),(1594,1,24,813),(1595,1,3,814),(1596,818852,4,814),(1597,67972382,5,815),(1598,1,6,815),(1599,1,10,816),(1600,69041066,9,816),(1601,1,14,817),(1602,274122,13,817),(1603,31747627,17,818),(1604,1,18,818),(1605,658501,4,819),(1606,1,3,819),(1607,1,24,820),(1608,2475060,23,820),(1609,1,8,821),(1610,90256556,7,821),(1611,2,20,822),(1612,34278010,19,822),(1613,87266647,21,823),(1614,1,22,823),(1615,1304772,25,824),(1616,1,26,824),(1617,1,2,825),(1618,69783536,1,825),(1619,1,12,826),(1620,86543799,11,826),(1621,16300969,15,827),(1622,1,16,827),(1623,1,28,828),(1624,463843,27,828),(1625,2,32,829),(1626,2,29,830),(1627,2,30,831),(1628,2,31,832),(1629,2,31,833),(1630,2,30,834),(1631,2,29,835),(1632,2,32,836),(1633,273170037,1,837),(1634,3,2,837),(1635,211104063,1,838),(1636,3,2,838),(1637,3,3,839),(1638,1600540,4,839),(1639,3,12,840),(1640,212792757,11,840),(1641,3,16,841),(1642,164390,15,841),(1643,278654425,11,842),(1644,3,12,842),(1645,4334080,15,843),(1646,3,16,843),(1647,305051,23,844),(1648,3,24,844),(1649,1291365,27,845),(1650,3,28,845),(1651,9291148,7,846),(1652,3,8,846),(1653,1443383,27,847),(1654,3,28,847),(1655,6256210,11,848),(1656,3,12,848),(1657,3,6,849),(1658,1254475,5,849),(1659,3,16,850),(1660,816012,15,850),(1661,3,8,851),(1662,217707967,7,851),(1663,1315001,27,852),(1664,3,28,852),(1665,286767565,7,853),(1666,3,8,853),(1667,3,2,854),(1668,4183158,1,854),(1669,1691743,19,855),(1670,6,20,855),(1671,3,22,856),(1672,214834365,21,856),(1673,3,26,857),(1674,1355528,25,857),(1675,3,22,858),(1676,280726092,21,858),(1677,1159703,25,859),(1678,3,26,859),(1679,13537985,19,860),(1680,6,20,860),(1681,2250699,4,861),(1682,3,3,861),(1683,1636240,4,862),(1684,3,3,862),(1685,4482270,1,863),(1686,3,2,863),(1687,1326765,27,864),(1688,3,28,864),(1689,1761410,23,865),(1690,3,24,865),(1691,242973997,11,866),(1692,3,12,866),(1693,1222343,5,867),(1694,3,6,867),(1695,236964167,15,868),(1696,3,16,868),(1697,3,24,869),(1698,3487453,23,869),(1699,3,8,870),(1700,246074390,7,870),(1701,3,6,871),(1702,206132008,5,871),(1703,267474032,5,872),(1704,3,6,872),(1705,242214,23,873),(1706,3,24,873),(1707,208992228,9,874),(1708,3,10,874),(1709,1530648,4,875),(1710,3,3,875),(1711,739280,13,876),(1712,3,14,876),(1713,3,14,877),(1714,952724,13,877),(1715,270954489,9,878),(1716,3,10,878),(1717,202358966,17,879),(1718,3,18,879),(1719,3,18,880),(1720,252318269,17,880),(1721,4557273,1,881),(1722,1,2,881),(1723,1,3,882),(1724,779110,4,882),(1725,1,22,883),(1726,260060650,21,883),(1727,6803292,19,884),(1728,2,20,884),(1729,1,26,885),(1730,5846773,25,885),(1731,5358520,11,886),(1732,1,12,886),(1733,1,16,887),(1734,94072,15,887),(1735,1,24,888),(1736,130894,23,888),(1737,1,8,889),(1738,268973993,7,889),(1739,1,8,890),(1740,4747616,7,890),(1741,796304,27,891),(1742,1,28,891),(1743,258916799,11,892),(1744,1,12,892),(1745,1,16,893),(1746,223437210,15,893),(1747,1,12,894),(1748,3135524,11,894),(1749,808942,5,895),(1750,1,6,895),(1751,1,16,896),(1752,411018,15,896),(1753,7918288,7,897),(1754,1,8,897),(1755,1,28,898),(1756,719461,27,898),(1757,1,28,899),(1758,723905,27,899),(1759,2023476,1,900),(1760,1,2,900),(1761,34757203,1,901),(1762,1,2,901),(1763,2,20,902),(1764,572270,19,902),(1765,1,22,903),(1766,6443178,21,903),(1767,1,26,904),(1768,603688,25,904),(1769,579734,13,905),(1770,1,14,905),(1771,33508769,9,906),(1772,1,10,906),(1773,805100,4,907),(1774,1,3,907),(1775,1,18,908),(1776,23913098,17,908),(1777,2538538,1,909),(1778,1,2,909),(1779,813104,27,910),(1780,1,28,910),(1781,31644644,5,911),(1782,1,6,911),(1783,1,24,912),(1784,903477,23,912),(1785,20121994,11,913),(1786,1,12,913),(1787,1,6,914),(1788,621002,5,914),(1789,1,16,915),(1790,16843136,15,915),(1791,1,8,916),(1792,21766846,7,916),(1793,1961501,5,917),(1794,1,6,917),(1795,1,24,918),(1796,8122713,23,918),(1797,104760,23,919),(1798,1,24,919),(1799,3433416,9,920),(1800,1,10,920),(1801,1,3,921),(1802,689847,4,921),(1803,1,14,922),(1804,405918,13,922),(1805,1,3,923),(1806,1006007,4,923),(1807,469538,17,924),(1808,1,18,924),(1809,1,2,925),(1810,4486140,1,925),(1811,1,3,926),(1812,626893,4,926),(1813,103243431,21,927),(1814,1,22,927),(1815,7035096,19,928),(1816,2,20,928),(1817,1,26,929),(1818,3699264,25,929),(1819,5241235,11,930),(1820,1,12,930),(1821,1,16,931),(1822,86545,15,931),(1823,120318,23,932),(1824,1,24,932),(1825,108531210,7,933),(1826,1,8,933),(1827,1,8,934),(1828,4996835,7,934),(1829,727387,27,935),(1830,1,28,935),(1831,1,12,936),(1832,102403208,11,936),(1833,69010376,15,937),(1834,1,16,937),(1835,1,12,938),(1836,3434297,11,938),(1837,571272,5,939),(1838,1,6,939),(1839,403241,15,940),(1840,1,16,940),(1841,1,8,941),(1842,7575183,7,941),(1843,1,28,942),(1844,691289,27,942),(1845,1,28,943),(1846,501754,27,943),(1847,2311873,1,944),(1848,1,2,944),(1849,1,2,945),(1850,32905175,1,945),(1851,2,20,946),(1852,573642,19,946),(1853,6234657,21,947),(1854,1,22,947),(1855,564760,25,948),(1856,1,26,948),(1857,601597,13,949),(1858,1,14,949),(1859,1,10,950),(1860,31473474,9,950),(1861,838855,4,951),(1862,1,3,951),(1863,1,18,952),(1864,21864566,17,952),(1865,1,2,953),(1866,1857647,1,953),(1867,731392,27,954),(1868,1,28,954),(1869,29732699,5,955),(1870,1,6,955),(1871,1,24,956),(1872,837814,23,956),(1873,1,12,957),(1874,18298982,11,957),(1875,672223,5,958),(1876,1,6,958),(1877,15743539,15,959),(1878,1,16,959),(1879,1,8,960),(1880,19943366,7,960),(1881,1875541,5,961),(1882,1,6,961),(1883,1,24,962),(1884,4880924,23,962),(1885,1,24,963),(1886,104292,23,963),(1887,3376185,9,964),(1888,1,10,964),(1889,687416,4,965),(1890,1,3,965),(1891,393238,13,966),(1892,1,14,966),(1893,1,3,967),(1894,929366,4,967),(1895,1,18,968),(1896,433768,17,968),(1897,4585046,1,969),(1898,1,2,969),(1899,747039,4,970),(1900,1,3,970),(1901,1,22,971),(1902,121983814,21,971),(1903,2,20,972),(1904,6976602,19,972),(1905,1,26,973),(1906,3214126,25,973),(1907,1,12,974),(1908,5378610,11,974),(1909,95674,15,975),(1910,1,16,975),(1911,126839,23,976),(1912,1,24,976),(1913,127233232,7,977),(1914,1,8,977),(1915,1,8,978),(1916,4905715,7,978),(1917,1,28,979),(1918,789959,27,979),(1919,1,12,980),(1920,121343203,11,980),(1921,1,16,981),(1922,86620421,15,981),(1923,3221539,11,982),(1924,1,12,982),(1925,1,6,983),(1926,684271,5,983),(1927,410108,15,984),(1928,1,16,984),(1929,1,8,985),(1930,7810303,7,985),(1931,1,28,986),(1932,722100,27,986),(1933,387060,27,987),(1934,1,28,987),(1935,2122431,1,988),(1936,1,2,988),(1937,1,2,989),(1938,34340214,1,989),(1939,2,20,990),(1940,672415,19,990),(1941,6445258,21,991),(1942,1,22,991),(1943,554662,25,992),(1944,1,26,992),(1945,690324,13,993),(1946,1,14,993),(1947,1,10,994),(1948,32869194,9,994),(1949,1,3,995),(1950,795172,4,995),(1951,1,18,996),(1952,23346004,17,996),(1953,2230983,1,997),(1954,1,2,997),(1955,1,28,998),(1956,748494,27,998),(1957,31240540,5,999),(1958,1,6,999),(1959,1,24,1000),(1960,833897,23,1000),(1961,1,12,1001),(1962,19554862,11,1001),(1963,660388,5,1002),(1964,1,6,1002),(1965,16575735,15,1003),(1966,1,16,1003),(1967,1,8,1004),(1968,21199675,7,1004),(1969,2024330,5,1005),(1970,1,6,1005),(1971,4866979,23,1006),(1972,1,24,1006),(1973,127089,23,1007),(1974,1,24,1007),(1975,3479362,9,1008),(1976,1,10,1008),(1977,669889,4,1009),(1978,1,3,1009),(1979,1,14,1010),(1980,394201,13,1010),(1981,895044,4,1011),(1982,1,3,1011),(1983,1,18,1012),(1984,433750,17,1012),(1985,1,29,1013),(1986,2,20,1014),(1987,6756131,19,1014),(1988,1,22,1015),(1989,50199295,21,1015),(1990,5,2,1016),(1991,14760519,1,1016),(1992,624288,25,1017),(1993,1,26,1017),(1994,5,3,1018),(1995,2675908,4,1018),(1996,70117110,21,1019),(1997,1,22,1019),(1998,4179961,19,1020),(1999,2,20,1020),(2000,1,30,1021),(2001,99857990,7,1022),(2002,3,8,1022),(2003,98756,23,1023),(2004,1,24,1023),(2005,1283691,25,1024),(2006,1,26,1024),(2007,1,3,1025),(2008,440156,4,1025),(2009,18668522,11,1026),(2010,5,12,1026),(2011,51716734,7,1027),(2012,1,8,1027),(2013,5,16,1028),(2014,488062,15,1028),(2015,438714,23,1029),(2016,5,24,1029),(2017,74649375,7,1030),(2018,1,8,1030),(2019,1175288,25,1031),(2020,3,26,1031),(2021,94510941,21,1032),(2022,3,22,1032),(2023,24549432,7,1033),(2024,6,8,1033),(2025,2496422,27,1034),(2026,5,28,1034),(2027,1,31,1035),(2028,69492414,11,1036),(2029,1,12,1036),(2030,1,16,1037),(2031,42939197,15,1037),(2032,3,2,1038),(2033,44838653,1,1038),(2034,18321653,11,1039),(2035,6,12,1039),(2036,5,6,1040),(2037,2464447,5,1040),(2038,7572397,15,1041),(2039,6,16,1041),(2040,342653,27,1042),(2041,1,28,1042),(2042,5,8,1043),(2043,27759557,7,1043),(2044,49617223,11,1044),(2045,1,12,1044),(2046,6,28,1045),(2047,2727066,27,1045),(2048,3303900,15,1046),(2049,1,16,1046),(2050,1,28,1047),(2051,370712,27,1047),(2052,8120068,1,1048),(2053,6,2,1048),(2054,3,16,1049),(2055,21690632,15,1049),(2056,67840782,11,1050),(2057,3,12,1050),(2058,26197112,1,1051),(2059,1,2,1051),(2060,25792419,27,1052),(2061,3,28,1052),(2062,1894784,19,1053),(2063,10,20,1053),(2064,5,22,1054),(2065,22295631,21,1054),(2066,1,2,1055),(2067,45977588,1,1055),(2068,2118155,25,1056),(2069,5,26,1056),(2070,401747,5,1057),(2071,1,6,1057),(2072,23628489,17,1058),(2073,1,18,1058),(2074,1,14,1059),(2075,730686,13,1059),(2076,25079697,9,1060),(2077,1,10,1060),(2078,3207720,11,1061),(2079,1,12,1061),(2080,1500516,15,1062),(2081,1,16,1062),(2082,5,3,1063),(2083,2528152,4,1063),(2084,32655447,9,1064),(2085,1,10,1064),(2086,1,28,1065),(2087,440220,27,1065),(2088,1,18,1066),(2089,19076119,17,1066),(2090,5,2,1067),(2091,7756186,1,1067),(2092,1,14,1068),(2093,12788233,13,1068),(2094,38877467,5,1069),(2095,3,6,1069),(2096,2244221,27,1070),(2097,5,28,1070),(2098,1,6,1071),(2099,31214029,5,1071),(2100,1353452,1,1072),(2101,1,2,1072),(2102,3,14,1073),(2103,1433769,13,1073),(2104,42182316,9,1074),(2105,3,10,1074),(2106,1,6,1075),(2107,23758084,5,1075),(2108,3256106,23,1076),(2109,5,24,1076),(2110,68214534,11,1077),(2111,5,12,1077),(2112,2469907,5,1078),(2113,6,6,1078),(2114,58358511,15,1079),(2115,5,16,1079),(2116,3,18,1080),(2117,37640609,17,1080),(2118,1255412,4,1081),(2119,3,3,1081),(2120,1,24,1082),(2121,1360127,23,1082),(2122,5,8,1083),(2123,73235928,7,1083),(2124,5,6,1084),(2125,6449773,5,1084),(2126,1,24,1085),(2127,4112865,23,1085),(2128,6,24,1086),(2129,458135,23,1086),(2130,11143518,9,1087),(2131,5,10,1087),(2132,6,3,1088),(2133,2696061,4,1088),(2134,5,14,1089),(2135,1334829,13,1089),(2136,1,3,1090),(2137,539808,4,1090),(2138,1,32,1091),(2139,1531978,17,1092),(2140,5,18,1092),(2141,3586730,23,1093),(2142,3,24,1093),(2143,1,8,1094),(2144,4241157,7,1094),(2145,1,3,1095),(2146,829073,4,1095),(2147,4290757,1,1096),(2148,1,2,1096),(2149,1,3,1097),(2150,650394,4,1097),(2151,6526458,19,1098),(2152,2,20,1098),(2153,1,12,1099),(2154,5028503,11,1099),(2155,1,16,1100),(2156,84779,15,1100),(2157,120000,23,1101),(2158,1,24,1101),(2159,4551939,7,1102),(2160,1,8,1102),(2161,1,28,1103),(2162,768617,27,1103),(2163,1,12,1104),(2164,3087796,11,1104),(2165,634261,5,1105),(2166,1,6,1105),(2167,402567,15,1106),(2168,1,16,1106),(2169,1,8,1107),(2170,7367963,7,1107),(2171,646828,27,1108),(2172,1,28,1108),(2173,2052880,1,1109),(2174,1,2,1109),(2175,590670,19,1110),(2176,2,20,1110),(2177,6069959,21,1111),(2178,1,22,1111),(2179,1,26,1112),(2180,530037,25,1112),(2181,603845,13,1113),(2182,1,14,1113),(2183,30698362,9,1114),(2184,1,10,1114),(2185,1,3,1115),(2186,641467,4,1115),(2187,1,18,1116),(2188,21818212,17,1116),(2189,1,2,1117),(2190,2094004,1,1117),(2191,713875,27,1118),(2192,1,28,1118),(2193,1,6,1119),(2194,29122767,5,1119),(2195,1,24,1120),(2196,806946,23,1120),(2197,18342724,11,1121),(2198,1,12,1121),(2199,1,6,1122),(2200,631653,5,1122),(2201,1,16,1123),(2202,15449803,15,1123),(2203,1,8,1124),(2204,19905014,7,1124),(2205,1,6,1125),(2206,1939435,5,1125),(2207,1,24,1126),(2208,104483,23,1126),(2209,1,10,1127),(2210,3228233,9,1127),(2211,1,3,1128),(2212,611616,4,1128),(2213,1,14,1129),(2214,387302,13,1129),(2215,1,3,1130),(2216,828007,4,1130),(2217,479196,17,1131),(2218,1,18,1131),(2219,94057850980,21,1132),(2220,1,22,1132),(2221,1,26,1133),(2222,3098300,25,1133),(2223,94063021163,7,1134),(2224,1,8,1134),(2225,94057232155,11,1135),(2226,1,12,1135),(2227,1,16,1136),(2228,94024928624,15,1136),(2229,349807,27,1137),(2230,1,28,1137),(2231,31955896,1,1138),(2232,1,2,1138),(2233,4895295,23,1139),(2234,1,24,1139),(2235,273170037,1,1140),(2236,3,2,1140),(2237,12,2,1141),(2238,243783798,1,1141),(2239,12,3,1142),(2240,7079884,4,1142),(2241,252468147,11,1143),(2242,12,12,1143),(2243,12,16,1144),(2244,1013522,15,1144),(2245,278654425,11,1145),(2246,3,12,1145),(2247,4334080,15,1146),(2248,3,16,1146),(2249,12,24,1147),(2250,1241816,23,1147),(2251,3,28,1148),(2252,1291365,27,1148),(2253,13,8,1149),(2254,53042685,7,1149),(2255,7022072,27,1150),(2256,12,28,1150),(2257,13,12,1151),(2258,37457019,11,1151),(2259,6417668,5,1152),(2260,12,6,1152),(2261,13,16,1153),(2262,10015343,15,1153),(2263,276139261,7,1154),(2264,12,8,1154),(2265,6821745,27,1155),(2266,13,28,1155),(2267,3,8,1156),(2268,286767565,7,1156),(2269,13,2,1157),(2270,20813886,1,1157),(2271,24,20,1158),(2272,5995524,19,1158),(2273,12,22,1159),(2274,262323048,21,1159),(2275,3,26,1160),(2276,1355528,25,1160),(2277,3,22,1161),(2278,280726092,21,1161),(2279,12,26,1162),(2280,5531005,25,1162),(2281,13537985,19,1163),(2282,6,20,1163),(2283,3,3,1164),(2284,2250699,4,1164),(2285,7244986,4,1165),(2286,12,3,1165),(2287,20959628,1,1166),(2288,12,2,1166),(2289,12,28,1167),(2290,6577851,27,1167),(2291,8399650,23,1168),(2292,12,24,1168),(2293,387507093,11,1169),(2294,12,12,1169),(2295,13,6,1170),(2296,6277516,5,1170),(2297,359934891,15,1171),(2298,12,16,1171),(2299,3,24,1172),(2300,3487453,23,1172),(2301,402125219,7,1173),(2302,12,8,1173),(2303,220382588,5,1174),(2304,12,6,1174),(2305,267474032,5,1175),(2306,3,6,1175),(2307,1140973,23,1176),(2308,13,24,1176),(2309,12,10,1177),(2310,233652942,9,1177),(2311,13,3,1178),(2312,6885477,4,1178),(2313,3654768,13,1179),(2314,12,14,1179),(2315,3,14,1180),(2316,952724,13,1180),(2317,3,10,1181),(2318,270954489,9,1181),(2319,12,18,1182),(2320,205707196,17,1182),(2321,252318269,17,1183),(2322,3,18,1183),(2323,5,22,1184),(2324,94613255985,21,1184),(2325,31521409,19,1185),(2326,10,20,1185),(2327,17142154,25,1186),(2328,5,26,1186),(2329,5,8,1187),(2330,94642408973,7,1187),(2331,94609387779,11,1188),(2332,5,12,1188),(2333,94446935828,15,1189),(2334,5,16,1189),(2335,2333238,27,1190),(2336,5,28,1190),(2337,160155600,1,1191),(2338,5,2,1191),(2339,5,14,1192),(2340,3206186,13,1192),(2341,153629496,9,1193),(2342,5,10,1193),(2343,5,18,1194),(2344,110017999,17,1194),(2345,5,6,1195),(2346,145498734,5,1195),(2347,5,24,1196),(2348,26878776,23,1196),(2349,5,3,1197),(2350,4198232,4,1197),(2351,1,29,1198),(2352,2,20,1199),(2353,6756131,19,1199),(2354,50199295,21,1200),(2355,1,22,1200),(2356,624288,25,1201),(2357,1,26,1201),(2358,1,30,1202),(2359,99857990,7,1203),(2360,3,8,1203),(2361,1,24,1204),(2362,98756,23,1204),(2363,440156,4,1205),(2364,1,3,1205),(2365,51716734,7,1206),(2366,1,8,1206),(2367,3,26,1207),(2368,1175288,25,1207),(2369,94510941,21,1208),(2370,3,22,1208),(2371,1,31,1209),(2372,44838653,1,1210),(2373,3,2,1210),(2374,1,28,1211),(2375,342653,27,1211),(2376,49617223,11,1212),(2377,1,12,1212),(2378,1,16,1213),(2379,3303900,15,1213),(2380,21690632,15,1214),(2381,3,16,1214),(2382,67840782,11,1215),(2383,3,12,1215),(2384,25792419,27,1216),(2385,3,28,1216),(2386,1,2,1217),(2387,45977588,1,1217),(2388,401747,5,1218),(2389,1,6,1218),(2390,23628489,17,1219),(2391,1,18,1219),(2392,1,12,1220),(2393,3207720,11,1220),(2394,1,16,1221),(2395,1500516,15,1221),(2396,32655447,9,1222),(2397,1,10,1222),(2398,440220,27,1223),(2399,1,28,1223),(2400,1,14,1224),(2401,12788233,13,1224),(2402,3,6,1225),(2403,38877467,5,1225),(2404,31214029,5,1226),(2405,1,6,1226),(2406,1,2,1227),(2407,1353452,1,1227),(2408,3,14,1228),(2409,1433769,13,1228),(2410,42182316,9,1229),(2411,3,10,1229),(2412,37640609,17,1230),(2413,3,18,1230),(2414,1255412,4,1231),(2415,3,3,1231),(2416,1,24,1232),(2417,1360127,23,1232),(2418,1,32,1233),(2419,3586730,23,1234),(2420,3,24,1234),(2421,1,8,1235),(2422,4241157,7,1235),(2423,829073,4,1236),(2424,1,3,1236),(2425,1,29,1237),(2426,2,20,1238),(2427,3408731,19,1238),(2428,1,22,1239),(2429,23574565,21,1239),(2430,5,2,1240),(2431,18355531,1,1240),(2432,866001,25,1241),(2433,1,26,1241),(2434,3075205,4,1242),(2435,5,3,1242),(2436,4786546,19,1243),(2437,2,20,1243),(2438,1,30,1244),(2439,3,8,1245),(2440,125858096,7,1245),(2441,1,24,1246),(2442,99890,23,1246),(2443,508239,4,1247),(2444,1,3,1247),(2445,22358874,11,1248),(2446,5,12,1248),(2447,26486187,7,1249),(2448,1,8,1249),(2449,376181,15,1250),(2450,5,16,1250),(2451,5,24,1251),(2452,489994,23,1251),(2453,3,26,1252),(2454,1492902,25,1252),(2455,115425640,21,1253),(2456,3,22,1253),(2457,28174566,7,1254),(2458,6,8,1254),(2459,8315512,27,1255),(2460,5,28,1255),(2461,1,31,1256),(2462,79888363,1,1257),(2463,3,2,1257),(2464,6,12,1258),(2465,21345364,11,1258),(2466,2013799,5,1259),(2467,5,6,1259),(2468,6,16,1260),(2469,9347898,15,1260),(2470,329369,27,1261),(2471,1,28,1261),(2472,5,8,1262),(2473,37473576,7,1262),(2474,1,12,1263),(2475,23007544,11,1263),(2476,3034326,27,1264),(2477,6,28,1264),(2478,1,16,1265),(2479,3264494,15,1265),(2480,6,2,1266),(2481,9169456,1,1266),(2482,28684182,15,1267),(2483,3,16,1267),(2484,110014930,11,1268),(2485,3,12,1268),(2486,4477353,27,1269),(2487,3,28,1269),(2488,1824401,19,1270),(2489,10,20,1270),(2490,5,22,1271),(2491,32061474,21,1271),(2492,19382315,1,1272),(2493,1,2,1272),(2494,2208744,25,1273),(2495,5,26,1273),(2496,1,6,1274),(2497,445885,5,1274),(2498,1,18,1275),(2499,14032454,17,1275),(2500,721890,13,1276),(2501,1,14,1276),(2502,38336294,9,1277),(2503,1,10,1277),(2504,1,12,1278),(2505,4916465,11,1278),(2506,1,16,1279),(2507,2772727,15,1279),(2508,2506031,4,1280),(2509,5,3,1280),(2510,1,10,1281),(2511,18774097,9,1281),(2512,1,28,1282),(2513,446588,27,1282),(2514,31055261,17,1283),(2515,1,18,1283),(2516,5,2,1284),(2517,11514047,1,1284),(2518,265940,13,1285),(2519,1,14,1285),(2520,3,6,1286),(2521,72767824,5,1286),(2522,2505953,27,1287),(2523,5,28,1287),(2524,1,6,1288),(2525,17877364,5,1288),(2526,1,2,1289),(2527,1720612,1,1289),(2528,2002967,13,1290),(2529,3,14,1290),(2530,3,10,1291),(2531,76388143,9,1291),(2532,1,6,1292),(2533,36388830,5,1292),(2534,3400779,23,1293),(2535,5,24,1293),(2536,92531890,11,1294),(2537,5,12,1294),(2538,2593213,5,1295),(2539,6,6,1295),(2540,78447653,15,1296),(2541,5,16,1296),(2542,62868858,17,1297),(2543,3,18,1297),(2544,3,3,1298),(2545,1530872,4,1298),(2546,2233012,23,1299),(2547,1,24,1299),(2548,98109817,7,1300),(2549,5,8,1300),(2550,9414768,5,1301),(2551,5,6,1301),(2552,6,24,1302),(2553,607382,23,1302),(2554,14214678,9,1303),(2555,5,10,1303),(2556,6,3,1304),(2557,3050882,4,1304),(2558,5,14,1305),(2559,1457240,13,1305),(2560,1,3,1306),(2561,1084710,4,1306),(2562,1,32,1307),(2563,1728459,17,1308),(2564,5,18,1308),(2565,3,24,1309),(2566,7307890,23,1309),(2567,1,8,1310),(2568,5980454,7,1310),(2569,590150,4,1311),(2570,1,3,1311),(2571,13135947619,21,1312),(2572,1,22,1312),(2573,1,26,1313),(2574,1309050,25,1313),(2575,1,8,1314),(2576,13138591334,7,1314),(2577,1,12,1315),(2578,13135316458,11,1315),(2579,13095205277,15,1316),(2580,1,16,1316),(2581,359985,27,1317),(2582,1,28,1317),(2583,39715287,1,1318),(2584,1,2,1318),(2585,1,24,1319),(2586,2287969,23,1319);
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
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MetricDef`
--

LOCK TABLES `MetricDef` WRITE;
/*!40000 ALTER TABLE `MetricDef` DISABLE KEYS */;
INSERT INTO `MetricDef` VALUES (1,'TIMING','FINEST','SoaFwk.Op.Time.StartCall'),(2,'TIMING','FINEST','SoaFwk.Op.Time.StartReqPipeline'),(3,'TIMING','NORMAL','SoaFwk.Op.Time.Pipeline_Request'),(4,'TIMING','NORMAL','SoaFwk.Op.Time.Total'),(5,'TIMING','FINEST','SoaFwk.Op.Time.StartDeserialization'),(6,'TIMING','FINEST','SoaFwk.Op.Time.StartRespPipeline'),(7,'TIMING','NORMAL','SoaFwk.Op.Time.Deserialization'),(8,'TIMING','NORMAL','SoaFwk.Op.Time.Call'),(9,'TIMING','NORMAL','SoaFwk.Op.Time.Authentication'),(10,'TIMING','NORMAL','SoaFwk.Op.Time.Authorization'),(11,'TIMING','FINEST','SoaFwk.Op.Time.StartSerialization'),(12,'TIMING','NORMAL','SoaFwk.Op.Time.RespDispatch'),(13,'TIMING','NORMAL','SoaFwk.Op.Time.Serialization'),(14,'TIMING','NORMAL','SoaFwk.Op.Time.Pipeline_Response'),(15,'ERROR','NORMAL','SoaFwk.Op.Err.Category.System'),(16,'ERROR','NORMAL','SoaFwk.Op.Err.Total'),(17,'ERROR','NORMAL','SoaFwk.Op.FailedCalls'),(18,'ERROR','NORMAL','SoaFwk.Op.Err.Severity.Error');
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
) ENGINE=MyISAM AUTO_INCREMENT=1320 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MetricValue`
--

LOCK TABLES `MetricValue` WRITE;
/*!40000 ALTER TABLE `MetricValue` DISABLE KEYS */;
INSERT INTO `MetricValue` VALUES (1,5,'',1298293469952,1,1,1),(2,5,'',1298293469952,1,2,1),(3,5,'',1298293469952,1,3,1),(4,5,'',1298293469952,1,4,1),(5,5,'',1298293469952,1,5,1),(6,5,'',1298293469952,1,6,1),(7,5,'',1298293469952,1,7,1),(8,5,'',1298293469952,1,8,1),(9,5,'',1298293469952,1,9,1),(10,5,'',1298293469952,1,10,1),(11,5,'',1298293469952,1,11,1),(12,5,'',1298293469952,1,12,1),(13,5,'',1298293469952,1,13,1),(14,5,'',1298293469952,1,14,1),(15,5,'',1298293469952,1,15,1),(16,5,'',1298293469952,1,16,1),(17,5,'',1298293474952,1,17,2),(18,5,'',1298293474952,1,18,1),(19,5,'',1298293474952,1,19,2),(20,5,'',1298293474952,1,20,1),(21,5,'',1298293474952,1,1,1),(22,5,'',1298293474952,1,2,1),(23,5,'',1298293474952,1,4,1),(24,5,'',1298293474952,1,21,1),(25,5,'',1298293474952,1,22,2),(26,5,'',1298293474952,1,22,1),(27,5,'',1298293474952,1,23,1),(28,5,'',1298293474952,1,24,1),(29,5,'',1298293474952,1,25,1),(30,5,'',1298293474952,1,26,1),(31,5,'',1298293474952,1,8,1),(32,5,'',1298293474952,1,10,1),(33,5,'',1298293474952,1,27,2),(34,5,'',1298293474952,1,28,2),(35,5,'',1298293474952,1,29,1),(36,5,'',1298293474952,1,30,1),(37,5,'',1298293474952,1,31,2),(38,5,'',1298293474952,1,32,1),(39,5,'',1298293474952,1,14,1),(40,5,'',1298293474952,1,33,1),(41,5,'',1298293474952,1,34,1),(42,5,'',1298293474952,1,35,1),(43,5,'',1298293474952,1,36,2),(44,5,'',1298293474952,1,37,1),(45,5,'',1298293474952,1,16,1),(46,5,'',1298293474952,1,38,1),(47,5,'',1298293474952,1,39,1),(48,5,'',1298293474952,1,40,1),(49,5,'',1298293474952,1,41,1),(50,5,'',1298293474952,1,3,1),(51,5,'',1298293474952,1,42,1),(52,5,'',1298293474952,1,5,1),(53,5,'',1298293474952,1,43,1),(54,5,'',1298293474952,1,6,1),(55,5,'',1298293474952,1,44,1),(56,5,'',1298293474952,1,45,2),(57,5,'',1298293474952,1,46,1),(58,5,'',1298293474952,1,47,2),(59,5,'',1298293474952,1,48,1),(60,5,'',1298293474952,1,7,1),(61,5,'',1298293474952,1,49,1),(62,5,'',1298293474952,1,50,1),(63,5,'',1298293474952,1,9,1),(64,5,'',1298293474952,1,51,1),(65,5,'',1298293474952,1,52,2),(66,5,'',1298293474952,1,53,1),(67,5,'',1298293474952,1,54,2),(68,5,'',1298293474952,1,11,1),(69,5,'',1298293474952,1,12,1),(70,5,'',1298293474952,1,13,1),(71,5,'',1298293474952,1,55,2),(72,5,'',1298293474952,1,56,2),(73,5,'',1298293474952,1,15,1),(74,5,'',1298293474952,1,57,2),(75,5,'',1298293474952,1,58,2),(76,5,'',1298293479952,1,18,2),(77,5,'',1298293479952,1,18,1),(78,5,'',1298293479952,1,59,2),(79,5,'',1298293479952,1,20,2),(80,5,'',1298293479952,1,20,1),(81,5,'',1298293479952,1,60,1),(82,5,'',1298293479952,1,1,1),(83,5,'',1298293479952,1,61,1),(84,5,'',1298293479952,1,2,1),(85,5,'',1298293479952,1,4,1),(86,5,'',1298293479952,1,21,2),(87,5,'',1298293479952,1,21,1),(88,5,'',1298293479952,1,62,1),(89,5,'',1298293479952,1,22,1),(90,5,'',1298293479952,1,23,2),(91,5,'',1298293479952,1,23,1),(92,5,'',1298293479952,1,24,1),(93,5,'',1298293479952,1,63,2),(94,5,'',1298293479952,1,64,2),(95,5,'',1298293479952,1,65,1),(96,5,'',1298293479952,1,66,2),(97,5,'',1298293479952,1,25,2),(98,5,'',1298293479952,1,25,1),(99,5,'',1298293479952,1,67,1),(100,5,'',1298293479952,1,26,1),(101,5,'',1298293479952,1,8,1),(102,5,'',1298293479952,1,68,1),(103,5,'',1298293479952,1,10,1),(104,5,'',1298293479952,1,69,2),(105,5,'',1298293479952,1,29,1),(106,5,'',1298293479952,1,70,2),(107,5,'',1298293479952,1,30,2),(108,5,'',1298293479952,1,30,1),(109,5,'',1298293479952,1,71,2),(110,5,'',1298293479952,1,32,2),(111,5,'',1298293479952,1,32,1),(112,5,'',1298293479952,1,14,1),(113,5,'',1298293479952,1,33,1),(114,5,'',1298293479952,1,34,1),(115,5,'',1298293479952,1,35,1),(116,5,'',1298293479952,1,72,2),(117,5,'',1298293479952,1,37,1),(118,5,'',1298293479952,1,73,2),(119,5,'',1298293479952,1,16,1),(120,5,'',1298293479952,1,74,2),(121,5,'',1298293479952,1,38,2),(122,5,'',1298293479952,1,38,1),(123,5,'',1298293479952,1,39,2),(124,5,'',1298293479952,1,39,1),(125,5,'',1298293479952,1,75,2),(126,5,'',1298293479952,1,76,2),(127,5,'',1298293479952,1,40,1),(128,5,'',1298293479952,1,41,1),(129,5,'',1298293479952,1,3,1),(130,5,'',1298293479952,1,42,1),(131,5,'',1298293479952,1,5,1),(132,5,'',1298293479952,1,43,2),(133,5,'',1298293479952,1,43,1),(134,5,'',1298293479952,1,6,1),(135,5,'',1298293479952,1,44,1),(136,5,'',1298293479952,1,46,2),(137,5,'',1298293479952,1,46,1),(138,5,'',1298293479952,1,48,1),(139,5,'',1298293479952,1,7,1),(140,5,'',1298293479952,1,49,1),(141,5,'',1298293479952,1,50,1),(142,5,'',1298293479952,1,9,1),(143,5,'',1298293479952,1,51,2),(144,5,'',1298293479952,1,51,1),(145,5,'',1298293479952,1,77,2),(146,5,'',1298293479952,1,78,2),(147,5,'',1298293479952,1,53,2),(148,5,'',1298293479952,1,53,1),(149,5,'',1298293479952,1,79,2),(150,5,'',1298293479952,1,80,2),(151,5,'',1298293479952,1,11,1),(152,5,'',1298293479952,1,12,1),(153,5,'',1298293479952,1,13,1),(154,5,'',1298293479952,1,81,2),(155,5,'',1298293479952,1,82,1),(156,5,'',1298293479952,1,15,1),(157,5,'',1298293479952,1,83,1),(158,5,'',1298293554954,1,59,2),(159,5,'',1298293554954,1,84,2),(160,5,'',1298293554954,1,85,2),(161,5,'',1298293554954,1,1,1),(162,5,'',1298293554954,1,86,2),(163,5,'',1298293554954,1,4,1),(164,5,'',1298293554954,1,21,2),(165,5,'',1298293554954,1,21,1),(166,5,'',1298293554954,1,23,2),(167,5,'',1298293554954,1,23,1),(168,5,'',1298293554954,1,63,2),(169,5,'',1298293554954,1,64,2),(170,5,'',1298293554954,1,65,1),(171,5,'',1298293554954,1,25,2),(172,5,'',1298293554954,1,25,1),(173,5,'',1298293554954,1,67,1),(174,5,'',1298293554954,1,8,1),(175,5,'',1298293554954,1,87,2),(176,5,'',1298293554954,1,10,1),(177,5,'',1298293554954,1,29,1),(178,5,'',1298293554954,1,30,2),(179,5,'',1298293554954,1,30,1),(180,5,'',1298293554954,1,71,2),(181,5,'',1298293554954,1,72,2),(182,5,'',1298293554954,1,37,1),(183,5,'',1298293554954,1,16,1),(184,5,'',1298293554954,1,74,2),(185,5,'',1298293554954,1,38,2),(186,5,'',1298293554954,1,38,1),(187,5,'',1298293554954,1,39,2),(188,5,'',1298293554954,1,39,1),(189,5,'',1298293554954,1,76,2),(190,5,'',1298293554954,1,41,1),(191,5,'',1298293554954,1,3,1),(192,5,'',1298293554954,1,42,1),(193,5,'',1298293554954,1,88,2),(194,5,'',1298293554954,1,6,1),(195,5,'',1298293554954,1,89,2),(196,5,'',1298293554954,1,44,1),(197,5,'',1298293554954,1,90,2),(198,5,'',1298293554954,1,46,2),(199,5,'',1298293554954,1,46,1),(200,5,'',1298293554954,1,49,1),(201,5,'',1298293554954,1,77,2),(202,5,'',1298293554954,1,78,2),(203,5,'',1298293554954,1,53,2),(204,5,'',1298293554954,1,53,1),(205,5,'',1298293554954,1,80,2),(206,5,'',1298293554954,1,12,1),(207,5,'',1298293554954,1,13,1),(208,5,'',1298293554954,1,91,2),(209,5,'',1298293554954,1,15,1),(210,5,'',1298293554954,1,83,1),(211,5,'',1298293554954,1,92,2),(212,5,'',1298293554954,1,18,2),(213,5,'',1298293554954,1,18,1),(214,5,'',1298293554954,1,20,2),(215,5,'',1298293554954,1,20,1),(216,5,'',1298293554954,1,60,1),(217,5,'',1298293554954,1,61,1),(218,5,'',1298293554954,1,2,1),(219,5,'',1298293554954,1,93,2),(220,5,'',1298293554954,1,62,1),(221,5,'',1298293554954,1,22,2),(222,5,'',1298293554954,1,22,1),(223,5,'',1298293554954,1,24,1),(224,5,'',1298293554954,1,94,2),(225,5,'',1298293554954,1,66,2),(226,5,'',1298293554954,1,26,1),(227,5,'',1298293554954,1,95,2),(228,5,'',1298293554954,1,68,1),(229,5,'',1298293554954,1,69,2),(230,5,'',1298293554954,1,70,2),(231,5,'',1298293554954,1,32,2),(232,5,'',1298293554954,1,32,1),(233,5,'',1298293554954,1,14,1),(234,5,'',1298293554954,1,33,1),(235,5,'',1298293554954,1,34,1),(236,5,'',1298293554954,1,35,1),(237,5,'',1298293554954,1,73,2),(238,5,'',1298293554954,1,75,2),(239,5,'',1298293554954,1,96,2),(240,5,'',1298293554954,1,40,1),(241,5,'',1298293554954,1,5,1),(242,5,'',1298293554954,1,43,2),(243,5,'',1298293554954,1,43,1),(244,5,'',1298293554954,1,48,1),(245,5,'',1298293554954,1,7,1),(246,5,'',1298293554954,1,50,1),(247,5,'',1298293554954,1,9,1),(248,5,'',1298293554954,1,51,2),(249,5,'',1298293554954,1,51,1),(250,5,'',1298293554954,1,79,2),(251,5,'',1298293554954,1,11,1),(252,5,'',1298293554954,1,81,2),(253,5,'',1298293554954,1,82,1),(254,5,'',1298293554954,1,97,2),(255,5,'',1298293559954,1,98,2),(256,5,'',1298293559954,1,84,2),(257,5,'',1298293559954,1,85,2),(258,5,'',1298293559954,1,1,1),(259,5,'',1298293559954,1,86,2),(260,5,'',1298293559954,1,4,1),(261,5,'',1298293559954,1,21,1),(262,5,'',1298293559954,1,23,1),(263,5,'',1298293559954,1,25,1),(264,5,'',1298293559954,1,8,1),(265,5,'',1298293559954,1,87,2),(266,5,'',1298293559954,1,10,1),(267,5,'',1298293559954,1,29,1),(268,5,'',1298293559954,1,99,2),(269,5,'',1298293559954,1,100,2),(270,5,'',1298293559954,1,30,1),(271,5,'',1298293559954,1,101,2),(272,5,'',1298293559954,1,37,1),(273,5,'',1298293559954,1,16,1),(274,5,'',1298293559954,1,38,1),(275,5,'',1298293559954,1,39,1),(276,5,'',1298293559954,1,41,1),(277,5,'',1298293559954,1,3,1),(278,5,'',1298293559954,1,42,1),(279,5,'',1298293559954,1,88,2),(280,5,'',1298293559954,1,6,1),(281,5,'',1298293559954,1,89,2),(282,5,'',1298293559954,1,44,1),(283,5,'',1298293559954,1,90,2),(284,5,'',1298293559954,1,46,1),(285,5,'',1298293559954,1,102,2),(286,5,'',1298293559954,1,49,1),(287,5,'',1298293559954,1,53,1),(288,5,'',1298293559954,1,103,2),(289,5,'',1298293559954,1,12,1),(290,5,'',1298293559954,1,13,1),(291,5,'',1298293559954,1,104,2),(292,5,'',1298293559954,1,91,2),(293,5,'',1298293559954,1,15,1),(294,5,'',1298293559954,1,105,2),(295,5,'',1298293559954,1,92,2),(296,5,'',1298293559954,1,18,1),(297,5,'',1298293559954,1,106,2),(298,5,'',1298293559954,1,20,1),(299,5,'',1298293559954,1,2,1),(300,5,'',1298293559954,1,93,2),(301,5,'',1298293559954,1,22,1),(302,5,'',1298293559954,1,24,1),(303,5,'',1298293559954,1,94,2),(304,5,'',1298293559954,1,26,1),(305,5,'',1298293559954,1,95,2),(306,5,'',1298293559954,1,32,1),(307,5,'',1298293559954,1,14,1),(308,5,'',1298293559954,1,33,1),(309,5,'',1298293559954,1,34,1),(310,5,'',1298293559954,1,35,1),(311,5,'',1298293559954,1,107,2),(312,5,'',1298293559954,1,96,2),(313,5,'',1298293559954,1,40,1),(314,5,'',1298293559954,1,5,1),(315,5,'',1298293559954,1,43,1),(316,5,'',1298293559954,1,108,2),(317,5,'',1298293559954,1,48,1),(318,5,'',1298293559954,1,7,1),(319,5,'',1298293559954,1,50,1),(320,5,'',1298293559954,1,9,1),(321,5,'',1298293559954,1,51,1),(322,5,'',1298293559954,1,109,2),(323,5,'',1298293559954,1,110,2),(324,5,'',1298293559954,1,11,1),(325,5,'',1298293559954,1,111,2),(326,5,'',1298293559954,1,97,2),(327,5,'',1298293579954,1,1,1),(328,5,'',1298293579954,1,4,1),(329,5,'',1298293579954,1,21,1),(330,5,'',1298293579954,1,23,1),(331,5,'',1298293579954,1,112,2),(332,5,'',1298293579954,1,113,2),(333,5,'',1298293579954,1,25,1),(334,5,'',1298293579954,1,114,2),(335,5,'',1298293579954,1,8,1),(336,5,'',1298293579954,1,10,1),(337,5,'',1298293579954,1,29,1),(338,5,'',1298293579954,1,30,1),(339,5,'',1298293579954,1,115,2),(340,5,'',1298293579954,1,37,1),(341,5,'',1298293579954,1,16,1),(342,5,'',1298293579954,1,38,1),(343,5,'',1298293579954,1,39,1),(344,5,'',1298293579954,1,116,2),(345,5,'',1298293579954,1,117,2),(346,5,'',1298293579954,1,41,1),(347,5,'',1298293579954,1,3,1),(348,5,'',1298293579954,1,42,1),(349,5,'',1298293579954,1,118,2),(350,5,'',1298293579954,1,6,1),(351,5,'',1298293579954,1,44,1),(352,5,'',1298293579954,1,46,1),(353,5,'',1298293579954,1,49,1),(354,5,'',1298293579954,1,119,2),(355,5,'',1298293579954,1,53,1),(356,5,'',1298293579954,1,12,1),(357,5,'',1298293579954,1,13,1),(358,5,'',1298293579954,1,15,1),(359,5,'',1298293579954,1,18,1),(360,5,'',1298293579954,1,20,1),(361,5,'',1298293579954,1,120,2),(362,5,'',1298293579954,1,2,1),(363,5,'',1298293579954,1,121,2),(364,5,'',1298293579954,1,22,1),(365,5,'',1298293579954,1,24,1),(366,5,'',1298293579954,1,122,2),(367,5,'',1298293579954,1,26,1),(368,5,'',1298293579954,1,32,1),(369,5,'',1298293579954,1,14,1),(370,5,'',1298293579954,1,33,1),(371,5,'',1298293579954,1,34,1),(372,5,'',1298293579954,1,35,1),(373,5,'',1298293579954,1,123,2),(374,5,'',1298293579954,1,40,1),(375,5,'',1298293579954,1,5,1),(376,5,'',1298293579954,1,43,1),(377,5,'',1298293579954,1,124,2),(378,5,'',1298293579954,1,48,1),(379,5,'',1298293579954,1,125,2),(380,5,'',1298293579954,1,7,1),(381,5,'',1298293579954,1,50,1),(382,5,'',1298293579954,1,9,1),(383,5,'',1298293579954,1,51,1),(384,5,'',1298293579954,1,11,1),(385,5,'',1298293629956,1,98,2),(386,5,'',1298293629956,1,84,2),(387,5,'',1298293629956,1,85,2),(388,5,'',1298293629956,1,1,1),(389,5,'',1298293629956,1,86,2),(390,5,'',1298293629956,1,4,1),(391,5,'',1298293629956,1,8,1),(392,5,'',1298293629956,1,87,2),(393,5,'',1298293629956,1,10,1),(394,5,'',1298293629956,1,29,1),(395,5,'',1298293629956,1,99,2),(396,5,'',1298293629956,1,100,2),(397,5,'',1298293629956,1,101,2),(398,5,'',1298293629956,1,37,1),(399,5,'',1298293629956,1,16,1),(400,5,'',1298293629956,1,41,1),(401,5,'',1298293629956,1,3,1),(402,5,'',1298293629956,1,42,1),(403,5,'',1298293629956,1,88,2),(404,5,'',1298293629956,1,6,1),(405,5,'',1298293629956,1,89,2),(406,5,'',1298293629956,1,44,1),(407,5,'',1298293629956,1,90,2),(408,5,'',1298293629956,1,102,2),(409,5,'',1298293629956,1,49,1),(410,5,'',1298293629956,1,103,2),(411,5,'',1298293629956,1,12,1),(412,5,'',1298293629956,1,13,1),(413,5,'',1298293629956,1,104,2),(414,5,'',1298293629956,1,91,2),(415,5,'',1298293629956,1,15,1),(416,5,'',1298293629956,1,105,2),(417,5,'',1298293629956,1,92,2),(418,5,'',1298293629956,1,106,2),(419,5,'',1298293629956,1,2,1),(420,5,'',1298293629956,1,93,2),(421,5,'',1298293629956,1,24,1),(422,5,'',1298293629956,1,94,2),(423,5,'',1298293629956,1,26,1),(424,5,'',1298293629956,1,95,2),(425,5,'',1298293629956,1,14,1),(426,5,'',1298293629956,1,33,1),(427,5,'',1298293629956,1,34,1),(428,5,'',1298293629956,1,35,1),(429,5,'',1298293629956,1,107,2),(430,5,'',1298293629956,1,96,2),(431,5,'',1298293629956,1,40,1),(432,5,'',1298293629956,1,5,1),(433,5,'',1298293629956,1,108,2),(434,5,'',1298293629956,1,48,1),(435,5,'',1298293629956,1,7,1),(436,5,'',1298293629956,1,50,1),(437,5,'',1298293629956,1,9,1),(438,5,'',1298293629956,1,109,2),(439,5,'',1298293629956,1,110,2),(440,5,'',1298293629956,1,11,1),(441,5,'',1298293629956,1,111,2),(442,5,'',1298293629956,1,97,2),(443,5,'',1298293634956,1,98,2),(444,5,'',1298293634956,1,84,2),(445,5,'',1298293634956,1,85,2),(446,5,'',1298293634956,1,1,1),(447,5,'',1298293634956,1,86,2),(448,5,'',1298293634956,1,4,1),(449,5,'',1298293634956,1,21,1),(450,5,'',1298293634956,1,126,2),(451,5,'',1298293634956,1,23,1),(452,5,'',1298293634956,1,25,1),(453,5,'',1298293634956,1,127,2),(454,5,'',1298293634956,1,8,1),(455,5,'',1298293634956,1,87,2),(456,5,'',1298293634956,1,128,2),(457,5,'',1298293634956,1,10,1),(458,5,'',1298293634956,1,29,1),(459,5,'',1298293634956,1,99,2),(460,5,'',1298293634956,1,100,2),(461,5,'',1298293634956,1,30,1),(462,5,'',1298293634956,1,129,2),(463,5,'',1298293634956,1,101,2),(464,5,'',1298293634956,1,37,1),(465,5,'',1298293634956,1,16,1),(466,5,'',1298293634956,1,38,1),(467,5,'',1298293634956,1,39,1),(468,5,'',1298293634956,1,41,1),(469,5,'',1298293634956,1,130,2),(470,5,'',1298293634956,1,3,1),(471,5,'',1298293634956,1,42,1),(472,5,'',1298293634956,1,88,2),(473,5,'',1298293634956,1,6,1),(474,5,'',1298293634956,1,89,2),(475,5,'',1298293634956,1,44,1),(476,5,'',1298293634956,1,90,2),(477,5,'',1298293634956,1,46,1),(478,5,'',1298293634956,1,102,2),(479,5,'',1298293634956,1,49,1),(480,5,'',1298293634956,1,53,1),(481,5,'',1298293634956,1,103,2),(482,5,'',1298293634956,1,12,1),(483,5,'',1298293634956,1,13,1),(484,5,'',1298293634956,1,131,2),(485,5,'',1298293634956,1,104,2),(486,5,'',1298293634956,1,91,2),(487,5,'',1298293634956,1,15,1),(488,5,'',1298293634956,1,105,2),(489,5,'',1298293634956,1,92,2),(490,5,'',1298293634956,1,18,1),(491,5,'',1298293634956,1,106,2),(492,5,'',1298293634956,1,20,1),(493,5,'',1298293634956,1,2,1),(494,5,'',1298293634956,1,93,2),(495,5,'',1298293634956,1,22,1),(496,5,'',1298293634956,1,132,2),(497,5,'',1298293634956,1,24,1),(498,5,'',1298293634956,1,94,2),(499,5,'',1298293634956,1,26,1),(500,5,'',1298293634956,1,95,2),(501,5,'',1298293634956,1,133,2),(502,5,'',1298293634956,1,134,2),(503,5,'',1298293634956,1,32,1),(504,5,'',1298293634956,1,14,1),(505,5,'',1298293634956,1,33,1),(506,5,'',1298293634956,1,34,1),(507,5,'',1298293634956,1,35,1),(508,5,'',1298293634956,1,135,2),(509,5,'',1298293634956,1,107,2),(510,5,'',1298293634956,1,96,2),(511,5,'',1298293634956,1,40,1),(512,5,'',1298293634956,1,136,2),(513,5,'',1298293634956,1,5,1),(514,5,'',1298293634956,1,43,1),(515,5,'',1298293634956,1,108,2),(516,5,'',1298293634956,1,48,1),(517,5,'',1298293634956,1,7,1),(518,5,'',1298293634956,1,50,1),(519,5,'',1298293634956,1,137,2),(520,5,'',1298293634956,1,9,1),(521,5,'',1298293634956,1,138,2),(522,5,'',1298293634956,1,51,1),(523,5,'',1298293634956,1,109,2),(524,5,'',1298293634956,1,110,2),(525,5,'',1298293634956,1,11,1),(526,5,'',1298293634956,1,111,2),(527,5,'',1298293634956,1,97,2),(528,5,'',1298293634956,1,139,2),(529,5,'',1298293654957,1,1,1),(530,5,'',1298293654957,1,4,1),(531,5,'',1298293654957,1,21,2),(532,5,'',1298293654957,1,25,2),(533,5,'',1298293654957,1,140,1),(534,5,'',1298293654957,1,8,1),(535,5,'',1298293654957,1,10,1),(536,5,'',1298293654957,1,141,2),(537,5,'',1298293654957,1,29,1),(538,5,'',1298293654957,1,30,2),(539,5,'',1298293654957,1,142,2),(540,5,'',1298293654957,1,16,1),(541,5,'',1298293654957,1,38,2),(542,5,'',1298293654957,1,3,1),(543,5,'',1298293654957,1,6,1),(544,5,'',1298293654957,1,46,2),(545,5,'',1298293654957,1,143,2),(546,5,'',1298293654957,1,12,1),(547,5,'',1298293654957,1,13,1),(548,5,'',1298293654957,1,15,1),(549,5,'',1298293654957,1,2,1),(550,5,'',1298293654957,1,144,1),(551,5,'',1298293654957,1,22,2),(552,5,'',1298293654957,1,24,1),(553,5,'',1298293654957,1,26,1),(554,5,'',1298293654957,1,32,2),(555,5,'',1298293654957,1,14,1),(556,5,'',1298293654957,1,33,1),(557,5,'',1298293654957,1,35,1),(558,5,'',1298293654957,1,40,1),(559,5,'',1298293654957,1,145,1),(560,5,'',1298293654957,1,146,1),(561,5,'',1298293654957,1,5,1),(562,5,'',1298293654957,1,43,2),(563,5,'',1298293654957,1,147,2),(564,5,'',1298293654957,1,7,1),(565,5,'',1298293654957,1,9,1),(566,5,'',1298293654957,1,51,2),(567,5,'',1298293654957,1,11,1),(568,5,'',1298293664957,1,1,1),(569,5,'',1298293664957,1,4,1),(570,5,'',1298293664957,1,21,2),(571,5,'',1298293664957,1,25,2),(572,5,'',1298293664957,1,140,1),(573,5,'',1298293664957,1,8,1),(574,5,'',1298293664957,1,10,1),(575,5,'',1298293664957,1,141,2),(576,5,'',1298293664957,1,29,1),(577,5,'',1298293664957,1,30,2),(578,5,'',1298293664957,1,142,2),(579,5,'',1298293664957,1,16,1),(580,5,'',1298293664957,1,38,2),(581,5,'',1298293664957,1,3,1),(582,5,'',1298293664957,1,6,1),(583,5,'',1298293664957,1,46,2),(584,5,'',1298293664957,1,143,2),(585,5,'',1298293664957,1,12,1),(586,5,'',1298293664957,1,13,1),(587,5,'',1298293664957,1,15,1),(588,5,'',1298293664957,1,2,1),(589,5,'',1298293664957,1,144,1),(590,5,'',1298293664957,1,22,2),(591,5,'',1298293664957,1,24,1),(592,5,'',1298293664957,1,26,1),(593,5,'',1298293664957,1,32,2),(594,5,'',1298293664957,1,14,1),(595,5,'',1298293664957,1,33,1),(596,5,'',1298293664957,1,35,1),(597,5,'',1298293664957,1,40,1),(598,5,'',1298293664957,1,145,1),(599,5,'',1298293664957,1,146,1),(600,5,'',1298293664957,1,5,1),(601,5,'',1298293664957,1,43,2),(602,5,'',1298293664957,1,147,2),(603,5,'',1298293664957,1,7,1),(604,5,'',1298293664957,1,9,1),(605,5,'',1298293664957,1,51,2),(606,5,'',1298293664957,1,11,1),(607,5,'',1298293689959,1,59,2),(608,5,'',1298293689959,1,1,1),(609,5,'',1298293689959,1,4,1),(610,5,'',1298293689959,1,21,2),(611,5,'',1298293689959,1,23,2),(612,5,'',1298293689959,1,63,2),(613,5,'',1298293689959,1,64,2),(614,5,'',1298293689959,1,65,1),(615,5,'',1298293689959,1,25,2),(616,5,'',1298293689959,1,67,1),(617,5,'',1298293689959,1,8,1),(618,5,'',1298293689959,1,10,1),(619,5,'',1298293689959,1,29,1),(620,5,'',1298293689959,1,30,2),(621,5,'',1298293689959,1,71,2),(622,5,'',1298293689959,1,72,2),(623,5,'',1298293689959,1,37,1),(624,5,'',1298293689959,1,16,1),(625,5,'',1298293689959,1,74,2),(626,5,'',1298293689959,1,38,2),(627,5,'',1298293689959,1,39,2),(628,5,'',1298293689959,1,76,2),(629,5,'',1298293689959,1,41,1),(630,5,'',1298293689959,1,3,1),(631,5,'',1298293689959,1,42,1),(632,5,'',1298293689959,1,6,1),(633,5,'',1298293689959,1,44,1),(634,5,'',1298293689959,1,46,2),(635,5,'',1298293689959,1,49,1),(636,5,'',1298293689959,1,77,2),(637,5,'',1298293689959,1,78,2),(638,5,'',1298293689959,1,53,2),(639,5,'',1298293689959,1,80,2),(640,5,'',1298293689959,1,12,1),(641,5,'',1298293689959,1,13,1),(642,5,'',1298293689959,1,15,1),(643,5,'',1298293689959,1,83,1),(644,5,'',1298293689959,1,18,2),(645,5,'',1298293689959,1,20,2),(646,5,'',1298293689959,1,60,1),(647,5,'',1298293689959,1,61,1),(648,5,'',1298293689959,1,2,1),(649,5,'',1298293689959,1,62,1),(650,5,'',1298293689959,1,22,2),(651,5,'',1298293689959,1,24,1),(652,5,'',1298293689959,1,66,2),(653,5,'',1298293689959,1,26,1),(654,5,'',1298293689959,1,68,1),(655,5,'',1298293689959,1,69,2),(656,5,'',1298293689959,1,70,2),(657,5,'',1298293689959,1,32,2),(658,5,'',1298293689959,1,14,1),(659,5,'',1298293689959,1,33,1),(660,5,'',1298293689959,1,34,1),(661,5,'',1298293689959,1,35,1),(662,5,'',1298293689959,1,73,2),(663,5,'',1298293689959,1,75,2),(664,5,'',1298293689959,1,40,1),(665,5,'',1298293689959,1,5,1),(666,5,'',1298293689959,1,43,2),(667,5,'',1298293689959,1,48,1),(668,5,'',1298293689959,1,7,1),(669,5,'',1298293689959,1,50,1),(670,5,'',1298293689959,1,9,1),(671,5,'',1298293689959,1,51,2),(672,5,'',1298293689959,1,79,2),(673,5,'',1298293689959,1,11,1),(674,5,'',1298293689959,1,81,2),(675,5,'',1298293689959,1,82,1),(676,3600,'',1298297070060,1,17,2),(677,3600,'',1298297070060,1,18,1),(678,3600,'',1298297070060,1,19,2),(679,3600,'',1298297070060,1,20,1),(680,3600,'',1298297070060,1,1,1),(681,3600,'',1298297070060,1,2,1),(682,3600,'',1298297070060,1,4,1),(683,3600,'',1298297070060,1,21,1),(684,3600,'',1298297070060,1,22,2),(685,3600,'',1298297070060,1,22,1),(686,3600,'',1298297070060,1,23,1),(687,3600,'',1298297070060,1,24,1),(688,3600,'',1298297070060,1,25,1),(689,3600,'',1298297070060,1,26,1),(690,3600,'',1298297070060,1,8,1),(691,3600,'',1298297070060,1,10,1),(692,3600,'',1298297070060,1,27,2),(693,3600,'',1298297070060,1,28,2),(694,3600,'',1298297070060,1,29,1),(695,3600,'',1298297070060,1,30,1),(696,3600,'',1298297070060,1,31,2),(697,3600,'',1298297070060,1,32,1),(698,3600,'',1298297070060,1,14,1),(699,3600,'',1298297070060,1,33,1),(700,3600,'',1298297070060,1,34,1),(701,3600,'',1298297070060,1,35,1),(702,3600,'',1298297070060,1,36,2),(703,3600,'',1298297070060,1,37,1),(704,3600,'',1298297070060,1,16,1),(705,3600,'',1298297070060,1,38,1),(706,3600,'',1298297070060,1,39,1),(707,3600,'',1298297070060,1,40,1),(708,3600,'',1298297070060,1,41,1),(709,3600,'',1298297070060,1,3,1),(710,3600,'',1298297070060,1,42,1),(711,3600,'',1298297070060,1,5,1),(712,3600,'',1298297070060,1,43,1),(713,3600,'',1298297070060,1,6,1),(714,3600,'',1298297070060,1,44,1),(715,3600,'',1298297070060,1,45,2),(716,3600,'',1298297070060,1,46,1),(717,3600,'',1298297070060,1,47,2),(718,3600,'',1298297070060,1,48,1),(719,3600,'',1298297070060,1,7,1),(720,3600,'',1298297070060,1,49,1),(721,3600,'',1298297070060,1,50,1),(722,3600,'',1298297070060,1,9,1),(723,3600,'',1298297070060,1,51,1),(724,3600,'',1298297070060,1,52,2),(725,3600,'',1298297070060,1,53,1),(726,3600,'',1298297070060,1,54,2),(727,3600,'',1298297070060,1,11,1),(728,3600,'',1298297070060,1,12,1),(729,3600,'',1298297070060,1,13,1),(730,3600,'',1298297070060,1,55,2),(731,3600,'',1298297070060,1,56,2),(732,3600,'',1298297070060,1,15,1),(733,3600,'',1298297070060,1,57,2),(734,3600,'',1298297070060,1,58,2),(735,3600,'',1298297070060,1,18,2),(736,3600,'',1298297070060,1,59,2),(737,3600,'',1298297070060,1,20,2),(738,3600,'',1298297070060,1,60,1),(739,3600,'',1298297070060,1,61,1),(740,3600,'',1298297070060,1,21,2),(741,3600,'',1298297070060,1,62,1),(742,3600,'',1298297070060,1,23,2),(743,3600,'',1298297070060,1,63,2),(744,3600,'',1298297070060,1,64,2),(745,3600,'',1298297070060,1,65,1),(746,3600,'',1298297070060,1,66,2),(747,3600,'',1298297070060,1,25,2),(748,3600,'',1298297070060,1,67,1),(749,3600,'',1298297070060,1,68,1),(750,3600,'',1298297070060,1,69,2),(751,3600,'',1298297070060,1,70,2),(752,3600,'',1298297070060,1,30,2),(753,3600,'',1298297070060,1,71,2),(754,3600,'',1298297070060,1,32,2),(755,3600,'',1298297070060,1,72,2),(756,3600,'',1298297070060,1,73,2),(757,3600,'',1298297070060,1,74,2),(758,3600,'',1298297070060,1,38,2),(759,3600,'',1298297070060,1,39,2),(760,3600,'',1298297070060,1,75,2),(761,3600,'',1298297070060,1,76,2),(762,3600,'',1298297070060,1,43,2),(763,3600,'',1298297070060,1,46,2),(764,3600,'',1298297070060,1,51,2),(765,3600,'',1298297070060,1,77,2),(766,3600,'',1298297070060,1,78,2),(767,3600,'',1298297070060,1,53,2),(768,3600,'',1298297070060,1,79,2),(769,3600,'',1298297070060,1,80,2),(770,3600,'',1298297070060,1,81,2),(771,3600,'',1298297070060,1,82,1),(772,3600,'',1298297070060,1,83,1),(773,3600,'',1298297070060,1,84,2),(774,3600,'',1298297070060,1,85,2),(775,3600,'',1298297070060,1,86,2),(776,3600,'',1298297070060,1,87,2),(777,3600,'',1298297070060,1,88,2),(778,3600,'',1298297070060,1,89,2),(779,3600,'',1298297070060,1,90,2),(780,3600,'',1298297070060,1,91,2),(781,3600,'',1298297070060,1,92,2),(782,3600,'',1298297070060,1,93,2),(783,3600,'',1298297070060,1,94,2),(784,3600,'',1298297070060,1,95,2),(785,3600,'',1298297070060,1,96,2),(786,3600,'',1298297070060,1,97,2),(787,3600,'',1298297070060,1,98,2),(788,3600,'',1298297070060,1,99,2),(789,3600,'',1298297070060,1,100,2),(790,3600,'',1298297070060,1,101,2),(791,3600,'',1298297070060,1,102,2),(792,3600,'',1298297070060,1,103,2),(793,3600,'',1298297070060,1,104,2),(794,3600,'',1298297070060,1,105,2),(795,3600,'',1298297070060,1,106,2),(796,3600,'',1298297070060,1,107,2),(797,3600,'',1298297070060,1,108,2),(798,3600,'',1298297070060,1,109,2),(799,3600,'',1298297070060,1,110,2),(800,3600,'',1298297070060,1,111,2),(801,3600,'',1298297070060,1,112,2),(802,3600,'',1298297070060,1,113,2),(803,3600,'',1298297070060,1,114,2),(804,3600,'',1298297070060,1,115,2),(805,3600,'',1298297070060,1,116,2),(806,3600,'',1298297070060,1,117,2),(807,3600,'',1298297070060,1,118,2),(808,3600,'',1298297070060,1,119,2),(809,3600,'',1298297070060,1,120,2),(810,3600,'',1298297070060,1,121,2),(811,3600,'',1298297070060,1,122,2),(812,3600,'',1298297070060,1,123,2),(813,3600,'',1298297070060,1,124,2),(814,3600,'',1298297070060,1,125,2),(815,3600,'',1298297070060,1,126,2),(816,3600,'',1298297070060,1,127,2),(817,3600,'',1298297070060,1,128,2),(818,3600,'',1298297070060,1,129,2),(819,3600,'',1298297070060,1,130,2),(820,3600,'',1298297070060,1,131,2),(821,3600,'',1298297070060,1,132,2),(822,3600,'',1298297070060,1,133,2),(823,3600,'',1298297070060,1,134,2),(824,3600,'',1298297070060,1,135,2),(825,3600,'',1298297070060,1,136,2),(826,3600,'',1298297070060,1,137,2),(827,3600,'',1298297070060,1,138,2),(828,3600,'',1298297070060,1,139,2),(829,3600,'',1298297070060,1,140,1),(830,3600,'',1298297070060,1,141,2),(831,3600,'',1298297070060,1,142,2),(832,3600,'',1298297070060,1,143,2),(833,3600,'',1298297070060,1,144,1),(834,3600,'',1298297070060,1,145,1),(835,3600,'',1298297070060,1,146,1),(836,3600,'',1298297070060,1,147,2),(837,5,'',1298298710110,1,19,2),(838,5,'',1298298710110,1,1,1),(839,5,'',1298298710110,1,4,1),(840,5,'',1298298710110,1,8,1),(841,5,'',1298298710110,1,10,1),(842,5,'',1298298710110,1,27,2),(843,5,'',1298298710110,1,28,2),(844,5,'',1298298710110,1,29,1),(845,5,'',1298298710110,1,31,2),(846,5,'',1298298710110,1,37,1),(847,5,'',1298298710110,1,16,1),(848,5,'',1298298710110,1,41,1),(849,5,'',1298298710110,1,3,1),(850,5,'',1298298710110,1,42,1),(851,5,'',1298298710110,1,6,1),(852,5,'',1298298710110,1,44,1),(853,5,'',1298298710110,1,45,2),(854,5,'',1298298710110,1,49,1),(855,5,'',1298298710110,1,12,1),(856,5,'',1298298710110,1,13,1),(857,5,'',1298298710110,1,55,2),(858,5,'',1298298710110,1,56,2),(859,5,'',1298298710110,1,15,1),(860,5,'',1298298710110,1,58,2),(861,5,'',1298298710110,1,17,2),(862,5,'',1298298710110,1,2,1),(863,5,'',1298298710110,1,24,1),(864,5,'',1298298710110,1,26,1),(865,5,'',1298298710110,1,14,1),(866,5,'',1298298710110,1,33,1),(867,5,'',1298298710110,1,34,1),(868,5,'',1298298710110,1,35,1),(869,5,'',1298298710110,1,36,2),(870,5,'',1298298710110,1,40,1),(871,5,'',1298298710110,1,5,1),(872,5,'',1298298710110,1,47,2),(873,5,'',1298298710110,1,48,1),(874,5,'',1298298710110,1,7,1),(875,5,'',1298298710110,1,50,1),(876,5,'',1298298710110,1,9,1),(877,5,'',1298298710110,1,52,2),(878,5,'',1298298710110,1,54,2),(879,5,'',1298298710110,1,11,1),(880,5,'',1298298710110,1,57,2),(881,5,'',1298298715110,1,1,1),(882,5,'',1298298715110,1,4,1),(883,5,'',1298298715110,1,21,2),(884,5,'',1298298715110,1,23,2),(885,5,'',1298298715110,1,25,2),(886,5,'',1298298715110,1,8,1),(887,5,'',1298298715110,1,10,1),(888,5,'',1298298715110,1,29,1),(889,5,'',1298298715110,1,30,2),(890,5,'',1298298715110,1,37,1),(891,5,'',1298298715110,1,16,1),(892,5,'',1298298715110,1,38,2),(893,5,'',1298298715110,1,39,2),(894,5,'',1298298715110,1,41,1),(895,5,'',1298298715110,1,3,1),(896,5,'',1298298715110,1,42,1),(897,5,'',1298298715110,1,6,1),(898,5,'',1298298715110,1,44,1),(899,5,'',1298298715110,1,46,2),(900,5,'',1298298715110,1,49,1),(901,5,'',1298298715110,1,53,2),(902,5,'',1298298715110,1,12,1),(903,5,'',1298298715110,1,13,1),(904,5,'',1298298715110,1,15,1),(905,5,'',1298298715110,1,18,2),(906,5,'',1298298715110,1,20,2),(907,5,'',1298298715110,1,2,1),(908,5,'',1298298715110,1,22,2),(909,5,'',1298298715110,1,24,1),(910,5,'',1298298715110,1,26,1),(911,5,'',1298298715110,1,32,2),(912,5,'',1298298715110,1,14,1),(913,5,'',1298298715110,1,33,1),(914,5,'',1298298715110,1,34,1),(915,5,'',1298298715110,1,35,1),(916,5,'',1298298715110,1,40,1),(917,5,'',1298298715110,1,5,1),(918,5,'',1298298715110,1,43,2),(919,5,'',1298298715110,1,48,1),(920,5,'',1298298715110,1,7,1),(921,5,'',1298298715110,1,50,1),(922,5,'',1298298715110,1,9,1),(923,5,'',1298298715110,1,51,2),(924,5,'',1298298715110,1,11,1),(925,5,'',1298298770113,1,1,1),(926,5,'',1298298770113,1,4,1),(927,5,'',1298298770113,1,21,2),(928,5,'',1298298770113,1,23,2),(929,5,'',1298298770113,1,25,2),(930,5,'',1298298770113,1,8,1),(931,5,'',1298298770113,1,10,1),(932,5,'',1298298770113,1,29,1),(933,5,'',1298298770113,1,30,2),(934,5,'',1298298770113,1,37,1),(935,5,'',1298298770113,1,16,1),(936,5,'',1298298770113,1,38,2),(937,5,'',1298298770113,1,39,2),(938,5,'',1298298770113,1,41,1),(939,5,'',1298298770113,1,3,1),(940,5,'',1298298770113,1,42,1),(941,5,'',1298298770113,1,6,1),(942,5,'',1298298770113,1,44,1),(943,5,'',1298298770113,1,46,2),(944,5,'',1298298770113,1,49,1),(945,5,'',1298298770113,1,53,2),(946,5,'',1298298770113,1,12,1),(947,5,'',1298298770113,1,13,1),(948,5,'',1298298770113,1,15,1),(949,5,'',1298298770113,1,18,2),(950,5,'',1298298770113,1,20,2),(951,5,'',1298298770113,1,2,1),(952,5,'',1298298770113,1,22,2),(953,5,'',1298298770113,1,24,1),(954,5,'',1298298770113,1,26,1),(955,5,'',1298298770113,1,32,2),(956,5,'',1298298770113,1,14,1),(957,5,'',1298298770113,1,33,1),(958,5,'',1298298770113,1,34,1),(959,5,'',1298298770113,1,35,1),(960,5,'',1298298770113,1,40,1),(961,5,'',1298298770113,1,5,1),(962,5,'',1298298770113,1,43,2),(963,5,'',1298298770113,1,48,1),(964,5,'',1298298770113,1,7,1),(965,5,'',1298298770113,1,50,1),(966,5,'',1298298770113,1,9,1),(967,5,'',1298298770113,1,51,2),(968,5,'',1298298770113,1,11,1),(969,5,'',1298298775113,1,1,1),(970,5,'',1298298775113,1,4,1),(971,5,'',1298298775113,1,21,2),(972,5,'',1298298775113,1,23,2),(973,5,'',1298298775113,1,25,2),(974,5,'',1298298775113,1,8,1),(975,5,'',1298298775113,1,10,1),(976,5,'',1298298775113,1,29,1),(977,5,'',1298298775113,1,30,2),(978,5,'',1298298775113,1,37,1),(979,5,'',1298298775113,1,16,1),(980,5,'',1298298775113,1,38,2),(981,5,'',1298298775113,1,39,2),(982,5,'',1298298775113,1,41,1),(983,5,'',1298298775113,1,3,1),(984,5,'',1298298775113,1,42,1),(985,5,'',1298298775113,1,6,1),(986,5,'',1298298775113,1,44,1),(987,5,'',1298298775113,1,46,2),(988,5,'',1298298775113,1,49,1),(989,5,'',1298298775113,1,53,2),(990,5,'',1298298775113,1,12,1),(991,5,'',1298298775113,1,13,1),(992,5,'',1298298775113,1,15,1),(993,5,'',1298298775113,1,18,2),(994,5,'',1298298775113,1,20,2),(995,5,'',1298298775113,1,2,1),(996,5,'',1298298775113,1,22,2),(997,5,'',1298298775113,1,24,1),(998,5,'',1298298775113,1,26,1),(999,5,'',1298298775113,1,32,2),(1000,5,'',1298298775113,1,14,1),(1001,5,'',1298298775113,1,33,1),(1002,5,'',1298298775113,1,34,1),(1003,5,'',1298298775113,1,35,1),(1004,5,'',1298298775113,1,40,1),(1005,5,'',1298298775113,1,5,1),(1006,5,'',1298298775113,1,43,2),(1007,5,'',1298298775113,1,48,1),(1008,5,'',1298298775113,1,7,1),(1009,5,'',1298298775113,1,50,1),(1010,5,'',1298298775113,1,9,1),(1011,5,'',1298298775113,1,51,2),(1012,5,'',1298298775113,1,11,1),(1013,5,'',1298298965117,1,59,2),(1014,5,'',1298298965117,1,84,2),(1015,5,'',1298298965117,1,85,2),(1016,5,'',1298298965117,1,1,1),(1017,5,'',1298298965117,1,86,2),(1018,5,'',1298298965117,1,4,1),(1019,5,'',1298298965117,1,21,2),(1020,5,'',1298298965117,1,23,2),(1021,5,'',1298298965117,1,63,2),(1022,5,'',1298298965117,1,64,2),(1023,5,'',1298298965117,1,65,1),(1024,5,'',1298298965117,1,25,2),(1025,5,'',1298298965117,1,67,1),(1026,5,'',1298298965117,1,8,1),(1027,5,'',1298298965117,1,87,2),(1028,5,'',1298298965117,1,10,1),(1029,5,'',1298298965117,1,29,1),(1030,5,'',1298298965117,1,30,2),(1031,5,'',1298298965117,1,71,2),(1032,5,'',1298298965117,1,72,2),(1033,5,'',1298298965117,1,37,1),(1034,5,'',1298298965117,1,16,1),(1035,5,'',1298298965117,1,74,2),(1036,5,'',1298298965117,1,38,2),(1037,5,'',1298298965117,1,39,2),(1038,5,'',1298298965117,1,76,2),(1039,5,'',1298298965117,1,41,1),(1040,5,'',1298298965117,1,3,1),(1041,5,'',1298298965117,1,42,1),(1042,5,'',1298298965117,1,88,2),(1043,5,'',1298298965117,1,6,1),(1044,5,'',1298298965117,1,89,2),(1045,5,'',1298298965117,1,44,1),(1046,5,'',1298298965117,1,90,2),(1047,5,'',1298298965117,1,46,2),(1048,5,'',1298298965117,1,49,1),(1049,5,'',1298298965117,1,77,2),(1050,5,'',1298298965117,1,78,2),(1051,5,'',1298298965117,1,53,2),(1052,5,'',1298298965117,1,80,2),(1053,5,'',1298298965117,1,12,1),(1054,5,'',1298298965117,1,13,1),(1055,5,'',1298298965117,1,91,2),(1056,5,'',1298298965117,1,15,1),(1057,5,'',1298298965117,1,83,1),(1058,5,'',1298298965117,1,92,2),(1059,5,'',1298298965117,1,18,2),(1060,5,'',1298298965117,1,20,2),(1061,5,'',1298298965117,1,60,1),(1062,5,'',1298298965117,1,61,1),(1063,5,'',1298298965117,1,2,1),(1064,5,'',1298298965117,1,93,2),(1065,5,'',1298298965117,1,62,1),(1066,5,'',1298298965117,1,22,2),(1067,5,'',1298298965117,1,24,1),(1068,5,'',1298298965117,1,94,2),(1069,5,'',1298298965117,1,66,2),(1070,5,'',1298298965117,1,26,1),(1071,5,'',1298298965117,1,95,2),(1072,5,'',1298298965117,1,68,1),(1073,5,'',1298298965117,1,69,2),(1074,5,'',1298298965117,1,70,2),(1075,5,'',1298298965117,1,32,2),(1076,5,'',1298298965117,1,14,1),(1077,5,'',1298298965117,1,33,1),(1078,5,'',1298298965117,1,34,1),(1079,5,'',1298298965117,1,35,1),(1080,5,'',1298298965117,1,73,2),(1081,5,'',1298298965117,1,75,2),(1082,5,'',1298298965117,1,96,2),(1083,5,'',1298298965117,1,40,1),(1084,5,'',1298298965117,1,5,1),(1085,5,'',1298298965117,1,43,2),(1086,5,'',1298298965117,1,48,1),(1087,5,'',1298298965117,1,7,1),(1088,5,'',1298298965117,1,50,1),(1089,5,'',1298298965117,1,9,1),(1090,5,'',1298298965117,1,51,2),(1091,5,'',1298298965117,1,79,2),(1092,5,'',1298298965117,1,11,1),(1093,5,'',1298298965117,1,81,2),(1094,5,'',1298298965117,1,82,1),(1095,5,'',1298298965117,1,97,2),(1096,5,'',1298299060132,1,1,1),(1097,5,'',1298299060132,1,4,1),(1098,5,'',1298299060132,1,23,2),(1099,5,'',1298299060132,1,8,1),(1100,5,'',1298299060132,1,10,1),(1101,5,'',1298299060132,1,29,1),(1102,5,'',1298299060132,1,37,1),(1103,5,'',1298299060132,1,16,1),(1104,5,'',1298299060132,1,41,1),(1105,5,'',1298299060132,1,3,1),(1106,5,'',1298299060132,1,42,1),(1107,5,'',1298299060132,1,6,1),(1108,5,'',1298299060132,1,44,1),(1109,5,'',1298299060132,1,49,1),(1110,5,'',1298299060132,1,12,1),(1111,5,'',1298299060132,1,13,1),(1112,5,'',1298299060132,1,15,1),(1113,5,'',1298299060132,1,18,2),(1114,5,'',1298299060132,1,20,2),(1115,5,'',1298299060132,1,2,1),(1116,5,'',1298299060132,1,22,2),(1117,5,'',1298299060132,1,24,1),(1118,5,'',1298299060132,1,26,1),(1119,5,'',1298299060132,1,32,2),(1120,5,'',1298299060132,1,14,1),(1121,5,'',1298299060132,1,33,1),(1122,5,'',1298299060132,1,34,1),(1123,5,'',1298299060132,1,35,1),(1124,5,'',1298299060132,1,40,1),(1125,5,'',1298299060132,1,5,1),(1126,5,'',1298299060132,1,48,1),(1127,5,'',1298299060132,1,7,1),(1128,5,'',1298299060132,1,50,1),(1129,5,'',1298299060132,1,9,1),(1130,5,'',1298299060132,1,51,2),(1131,5,'',1298299060132,1,11,1),(1132,5,'',1298299155137,1,21,2),(1133,5,'',1298299155137,1,25,2),(1134,5,'',1298299155137,1,30,2),(1135,5,'',1298299155137,1,38,2),(1136,5,'',1298299155137,1,39,2),(1137,5,'',1298299155137,1,46,2),(1138,5,'',1298299155137,1,53,2),(1139,5,'',1298299155137,1,43,2),(1140,3600,'',1298300670183,1,19,2),(1141,3600,'',1298300670183,1,1,1),(1142,3600,'',1298300670183,1,4,1),(1143,3600,'',1298300670183,1,8,1),(1144,3600,'',1298300670183,1,10,1),(1145,3600,'',1298300670183,1,27,2),(1146,3600,'',1298300670183,1,28,2),(1147,3600,'',1298300670183,1,29,1),(1148,3600,'',1298300670183,1,31,2),(1149,3600,'',1298300670183,1,37,1),(1150,3600,'',1298300670183,1,16,1),(1151,3600,'',1298300670183,1,41,1),(1152,3600,'',1298300670183,1,3,1),(1153,3600,'',1298300670183,1,42,1),(1154,3600,'',1298300670183,1,6,1),(1155,3600,'',1298300670183,1,44,1),(1156,3600,'',1298300670183,1,45,2),(1157,3600,'',1298300670183,1,49,1),(1158,3600,'',1298300670183,1,12,1),(1159,3600,'',1298300670183,1,13,1),(1160,3600,'',1298300670183,1,55,2),(1161,3600,'',1298300670183,1,56,2),(1162,3600,'',1298300670183,1,15,1),(1163,3600,'',1298300670183,1,58,2),(1164,3600,'',1298300670183,1,17,2),(1165,3600,'',1298300670183,1,2,1),(1166,3600,'',1298300670183,1,24,1),(1167,3600,'',1298300670183,1,26,1),(1168,3600,'',1298300670183,1,14,1),(1169,3600,'',1298300670183,1,33,1),(1170,3600,'',1298300670183,1,34,1),(1171,3600,'',1298300670183,1,35,1),(1172,3600,'',1298300670183,1,36,2),(1173,3600,'',1298300670183,1,40,1),(1174,3600,'',1298300670183,1,5,1),(1175,3600,'',1298300670183,1,47,2),(1176,3600,'',1298300670183,1,48,1),(1177,3600,'',1298300670183,1,7,1),(1178,3600,'',1298300670183,1,50,1),(1179,3600,'',1298300670183,1,9,1),(1180,3600,'',1298300670183,1,52,2),(1181,3600,'',1298300670183,1,54,2),(1182,3600,'',1298300670183,1,11,1),(1183,3600,'',1298300670183,1,57,2),(1184,3600,'',1298300670183,1,21,2),(1185,3600,'',1298300670183,1,23,2),(1186,3600,'',1298300670183,1,25,2),(1187,3600,'',1298300670183,1,30,2),(1188,3600,'',1298300670183,1,38,2),(1189,3600,'',1298300670183,1,39,2),(1190,3600,'',1298300670183,1,46,2),(1191,3600,'',1298300670183,1,53,2),(1192,3600,'',1298300670183,1,18,2),(1193,3600,'',1298300670183,1,20,2),(1194,3600,'',1298300670183,1,22,2),(1195,3600,'',1298300670183,1,32,2),(1196,3600,'',1298300670183,1,43,2),(1197,3600,'',1298300670183,1,51,2),(1198,3600,'',1298300670183,1,59,2),(1199,3600,'',1298300670183,1,84,2),(1200,3600,'',1298300670183,1,85,2),(1201,3600,'',1298300670183,1,86,2),(1202,3600,'',1298300670183,1,63,2),(1203,3600,'',1298300670183,1,64,2),(1204,3600,'',1298300670183,1,65,1),(1205,3600,'',1298300670183,1,67,1),(1206,3600,'',1298300670183,1,87,2),(1207,3600,'',1298300670183,1,71,2),(1208,3600,'',1298300670183,1,72,2),(1209,3600,'',1298300670183,1,74,2),(1210,3600,'',1298300670183,1,76,2),(1211,3600,'',1298300670183,1,88,2),(1212,3600,'',1298300670183,1,89,2),(1213,3600,'',1298300670183,1,90,2),(1214,3600,'',1298300670183,1,77,2),(1215,3600,'',1298300670183,1,78,2),(1216,3600,'',1298300670183,1,80,2),(1217,3600,'',1298300670183,1,91,2),(1218,3600,'',1298300670183,1,83,1),(1219,3600,'',1298300670183,1,92,2),(1220,3600,'',1298300670183,1,60,1),(1221,3600,'',1298300670183,1,61,1),(1222,3600,'',1298300670183,1,93,2),(1223,3600,'',1298300670183,1,62,1),(1224,3600,'',1298300670183,1,94,2),(1225,3600,'',1298300670183,1,66,2),(1226,3600,'',1298300670183,1,95,2),(1227,3600,'',1298300670183,1,68,1),(1228,3600,'',1298300670183,1,69,2),(1229,3600,'',1298300670183,1,70,2),(1230,3600,'',1298300670183,1,73,2),(1231,3600,'',1298300670183,1,75,2),(1232,3600,'',1298300670183,1,96,2),(1233,3600,'',1298300670183,1,79,2),(1234,3600,'',1298300670183,1,81,2),(1235,3600,'',1298300670183,1,82,1),(1236,3600,'',1298300670183,1,97,2),(1237,5,'',1298300945193,1,59,2),(1238,5,'',1298300945193,1,84,2),(1239,5,'',1298300945193,1,85,2),(1240,5,'',1298300945193,1,1,1),(1241,5,'',1298300945193,1,86,2),(1242,5,'',1298300945193,1,4,1),(1243,5,'',1298300945193,1,23,2),(1244,5,'',1298300945193,1,63,2),(1245,5,'',1298300945193,1,64,2),(1246,5,'',1298300945193,1,65,1),(1247,5,'',1298300945193,1,67,1),(1248,5,'',1298300945193,1,8,1),(1249,5,'',1298300945193,1,87,2),(1250,5,'',1298300945193,1,10,1),(1251,5,'',1298300945193,1,29,1),(1252,5,'',1298300945193,1,71,2),(1253,5,'',1298300945193,1,72,2),(1254,5,'',1298300945193,1,37,1),(1255,5,'',1298300945193,1,16,1),(1256,5,'',1298300945193,1,74,2),(1257,5,'',1298300945193,1,76,2),(1258,5,'',1298300945193,1,41,1),(1259,5,'',1298300945193,1,3,1),(1260,5,'',1298300945193,1,42,1),(1261,5,'',1298300945193,1,88,2),(1262,5,'',1298300945193,1,6,1),(1263,5,'',1298300945193,1,89,2),(1264,5,'',1298300945193,1,44,1),(1265,5,'',1298300945193,1,90,2),(1266,5,'',1298300945193,1,49,1),(1267,5,'',1298300945193,1,77,2),(1268,5,'',1298300945193,1,78,2),(1269,5,'',1298300945193,1,80,2),(1270,5,'',1298300945193,1,12,1),(1271,5,'',1298300945193,1,13,1),(1272,5,'',1298300945193,1,91,2),(1273,5,'',1298300945193,1,15,1),(1274,5,'',1298300945193,1,83,1),(1275,5,'',1298300945193,1,92,2),(1276,5,'',1298300945193,1,18,2),(1277,5,'',1298300945193,1,20,2),(1278,5,'',1298300945193,1,60,1),(1279,5,'',1298300945193,1,61,1),(1280,5,'',1298300945193,1,2,1),(1281,5,'',1298300945193,1,93,2),(1282,5,'',1298300945193,1,62,1),(1283,5,'',1298300945193,1,22,2),(1284,5,'',1298300945193,1,24,1),(1285,5,'',1298300945193,1,94,2),(1286,5,'',1298300945193,1,66,2),(1287,5,'',1298300945193,1,26,1),(1288,5,'',1298300945193,1,95,2),(1289,5,'',1298300945193,1,68,1),(1290,5,'',1298300945193,1,69,2),(1291,5,'',1298300945193,1,70,2),(1292,5,'',1298300945193,1,32,2),(1293,5,'',1298300945193,1,14,1),(1294,5,'',1298300945193,1,33,1),(1295,5,'',1298300945193,1,34,1),(1296,5,'',1298300945193,1,35,1),(1297,5,'',1298300945193,1,73,2),(1298,5,'',1298300945193,1,75,2),(1299,5,'',1298300945193,1,96,2),(1300,5,'',1298300945193,1,40,1),(1301,5,'',1298300945193,1,5,1),(1302,5,'',1298300945193,1,48,1),(1303,5,'',1298300945193,1,7,1),(1304,5,'',1298300945193,1,50,1),(1305,5,'',1298300945193,1,9,1),(1306,5,'',1298300945193,1,51,2),(1307,5,'',1298300945193,1,79,2),(1308,5,'',1298300945193,1,11,1),(1309,5,'',1298300945193,1,81,2),(1310,5,'',1298300945193,1,82,1),(1311,5,'',1298300945193,1,97,2),(1312,5,'',1298300955193,1,21,2),(1313,5,'',1298300955193,1,25,2),(1314,5,'',1298300955193,1,30,2),(1315,5,'',1298300955193,1,38,2),(1316,5,'',1298300955193,1,39,2),(1317,5,'',1298300955193,1,46,2),(1318,5,'',1298300955193,1,53,2),(1319,5,'',1298300955193,1,43,2);
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
) ENGINE=MyISAM AUTO_INCREMENT=977 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Operation`
--

LOCK TABLES `Operation` WRITE;
/*!40000 ALTER TABLE `Operation` DISABLE KEYS */;
INSERT INTO `Operation` VALUES (1,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'checkout',1),(2,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'commit',2),(3,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'merge',1),(4,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'createPolicy',3),(5,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'getMetaData',3),(6,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'findPolicies',3),(95,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'69',4),(96,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'policydelete',4),(97,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'69',5),(98,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'policydelete',5),(99,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'69',6),(100,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'policydelete',6),(101,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'69',7),(102,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'policydelete',7),(103,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'Admin_Policy_policydelete',8),(104,'admin','2011-01-03 13:51:33',NULL,NULL,NULL,'28',8),(7,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'findSubjectGroups',3),(8,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'findSubjects',3),(9,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'findResources',3),(10,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'createSubjectGroups',3),(13,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'getResources',3),(14,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'getEntityHistory',3),(11,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'updateSubjectGroups',3),(12,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'deletePolicy',3),(20,'admin','2010-01-31 10:49:55',NULL,NULL,NULL,'enablePolicy',3),(21,'admin','2010-01-31 10:49:55',NULL,NULL,NULL,'disablePolicy',3),(22,'admin','2010-01-31 10:49:55',NULL,NULL,NULL,'updatePolicy',3),(23,'admin','2010-01-31 10:49:55',NULL,NULL,NULL,'deleteSubjectGroups',3),(24,'admin','2010-01-31 10:49:55',NULL,NULL,NULL,'findExternalSubjects',3),(25,'admin','2010-01-31 10:49:55',NULL,NULL,NULL,'createSubjects',3),(106,'admin','2011-02-11 19:01:29',NULL,NULL,NULL,'71',4),(108,'admin','2011-02-11 19:01:29',NULL,NULL,NULL,'71',5),(110,'admin','2011-02-11 19:01:29',NULL,NULL,NULL,'71',6),(112,'admin','2011-02-11 19:01:29',NULL,NULL,NULL,'71',7),(113,'admin','2011-02-11 19:01:29',NULL,NULL,NULL,'Admin_Policy_dd',8),(114,'admin','2011-02-11 19:01:29',NULL,NULL,NULL,'29',8),(901,'admin','2011-01-17 00:00:00',NULL,NULL,NULL,'All',4),(912,'admin','2011-01-17 00:00:00',NULL,NULL,NULL,'All',5),(913,'admin','2011-01-17 00:00:00',NULL,NULL,NULL,'All',6),(914,'admin','2011-01-17 00:00:00',NULL,NULL,NULL,'All',7),(915,'admin','2011-01-17 00:00:00',NULL,NULL,NULL,'All',8),(916,'admin','2011-01-17 00:00:00',NULL,NULL,NULL,'All',9),(917,'admin','2011-02-11 19:27:09',NULL,NULL,NULL,'SuperPolicy',4),(918,'admin','2011-02-11 19:27:09',NULL,NULL,NULL,'73',4),(919,'admin','2011-02-11 19:27:09',NULL,NULL,NULL,'SuperPolicy',5),(920,'admin','2011-02-11 19:27:09',NULL,NULL,NULL,'73',5),(921,'admin','2011-02-11 19:27:09',NULL,NULL,NULL,'SuperPolicy',6),(922,'admin','2011-02-11 19:27:09',NULL,NULL,NULL,'73',6),(923,'admin','2011-02-11 19:27:09',NULL,NULL,NULL,'SuperPolicy',7),(924,'admin','2011-02-11 19:27:09',NULL,NULL,NULL,'73',7),(925,'admin','2011-02-11 19:27:09',NULL,NULL,NULL,'Admin_Policy_SuperPolicy',8),(926,'admin','2011-02-11 19:27:09',NULL,NULL,NULL,'30',8);
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
) ENGINE=MyISAM AUTO_INCREMENT=86 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Policy`
--

LOCK TABLES `Policy` WRITE;
/*!40000 ALTER TABLE `Policy` DISABLE KEYS */;
INSERT INTO `Policy` VALUES (1,'admin','2010-12-14 10:12:58','admin','2010-12-20 15:02:32','','description of BLPolicy1','BLPolicy1','BLACKLIST'),(3,'admin','2010-12-14 16:39:04','admin','2010-12-23 16:35:55','','description of BLPolicy2','BLPolicy2','BLACKLIST'),(4,'admin','2010-12-14 16:39:04',NULL,NULL,'','description of RL1','RL1','RL'),(5,'admin','2010-12-14 16:39:04',NULL,NULL,'','description of RL2','RL2','RL'),(6,'admin','2010-12-14 16:39:04',NULL,NULL,'','description of whitelist1','whitelist1','WHITELIST'),(8,'admin','2010-12-23 15:25:07',NULL,NULL,'','an auth policy','PolicyService','AUTHZ'),(71,'admin','2011-02-11 19:01:29','admin','2011-02-11 19:02:10','','description of BLPolicy3','BLPolicy3','BLACKLIST'),(73,'admin','2011-02-11 19:27:09','admin','2011-02-17 17:09:52','','The base admin policy','SuperPolicy','AUTHZ'),(74,'admin','2011-02-11 19:27:09','admin','2011-02-11 19:27:10','','Managing SubjectGroup SuperPolicy','Admin_Policy_SuperPolicy','AUTHZ');
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
INSERT INTO `Policy_Operation` VALUES (1,1),(5,2),(8,12),(8,11),(8,10),(8,14),(8,20),(8,21),(8,22),(8,23),(8,9),(8,8),(8,7),(8,6),(8,13),(8,5),(8,4),(8,24),(8,25),(73,916),(73,901),(73,914),(73,915),(73,912),(73,913),(74,926),(74,924),(74,925),(74,922),(74,923),(74,920),(74,921),(74,918),(74,919),(74,917);
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
INSERT INTO `Policy_Subject` VALUES (1,1),(6,2),(4,3),(5,5),(6,5),(6,4),(3,3),(8,1),(71,7),(8,7),(73,1);
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
INSERT INTO `Policy_SubjectGroup` VALUES (74,30);
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
INSERT INTO `Resource` VALUES (1,'admin','2010-12-14 10:49:55',NULL,NULL,'test','testService','SERVICE'),(2,'admin','2010-12-14 11:08:27',NULL,NULL,'desc Payment','PaymentService','SERVICE'),(3,'admin','2010-12-14 10:49:55',NULL,NULL,NULL,'PolicyService','SERVICE'),(4,'admin','2010-12-29 17:38:46',NULL,NULL,NULL,'SERVICE.PolicyService.disablePolicy','OBJECT'),(5,'admin','2010-12-29 17:38:46',NULL,NULL,NULL,'SERVICE.PolicyService.deletePolicy','OBJECT'),(6,'admin','2010-12-29 17:38:46',NULL,NULL,NULL,'SERVICE.PolicyService.enablePolicy','OBJECT'),(7,'admin','2010-12-29 17:38:46',NULL,NULL,NULL,'SERVICE.PolicyService.updatePolicy','OBJECT'),(8,'admin','2010-12-29 17:38:46',NULL,NULL,NULL,'SERVICE.PolicyService.updateSubjectGroups','OBJECT'),(9,'admin','2011-01-02 18:03:17',NULL,NULL,NULL,'SERVICE.PolicyService.deleteSubjectGroups','OBJECT');
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
INSERT INTO `Rule` VALUES (1,'admin','2011-02-17 19:02:55',NULL,NULL,'HITS TEST',1,10000,3,30000,'rul1',1,NULL,'\0'),(2,'admin','2011-02-17 19:01:55',NULL,NULL,'ServiceXXX.OperationXX.count test',3,10000,NULL,30000,'server',2,NULL,'\0');
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
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Subject`
--

LOCK TABLES `Subject` WRITE;
/*!40000 ALTER TABLE `Subject` DISABLE KEYS */;
INSERT INTO `Subject` VALUES (1,'admin','2011-02-17 17:08:33','',NULL,'Admin User',NULL,0,NULL,'admin','USER'),(2,'admin','2010-12-14 10:37:42',NULL,NULL,'IPDESK',NULL,0,NULL,'1.102.96.107','IP'),(3,'admin','2010-12-14 10:37:42',NULL,NULL,'IPDESK',NULL,0,NULL,'1.102.96.103','IP'),(4,'admin','2010-12-14 10:37:42',NULL,NULL,'IPDESK',NULL,0,NULL,'1.102.96.104','IP'),(5,'admin','2010-12-14 10:37:42',NULL,NULL,'IPDESK',NULL,0,NULL,'1.102.96.105','IP'),(6,'admin','2010-12-14 10:37:42',NULL,NULL,'IPDESK',NULL,0,NULL,'1.102.96.106','IP'),(7,'admin','2011-02-17 19:01:55',NULL,NULL,'Guest user',NULL,0,NULL,'guest','USER');
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
) ENGINE=MyISAM AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SubjectGroup`
--

LOCK TABLES `SubjectGroup` WRITE;
/*!40000 ALTER TABLE `SubjectGroup` DISABLE KEYS */;
INSERT INTO `SubjectGroup` VALUES (23,'admin','2011-01-02 18:03:17',NULL,NULL,'\0','\0','first subjectgroup',NULL,'SG_1','IP'),(30,'admin','2011-02-11 19:27:09','admin','2011-02-18 13:55:47','\0','\0','Managing Admin_Policy_SuperPolicy',NULL,'Admin_Policy_SuperPolicy','USER');
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
INSERT INTO `SubjectGroup_Subject` VALUES (12,2),(12,3),(13,3),(12,1),(11,5),(12,4),(12,5),(12,6),(14,5),(15,1),(16,1),(17,1),(18,1),(19,1),(20,1),(21,1),(22,1),(23,3),(30,1);
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

-- Dump completed on 2011-02-21 12:48:31
