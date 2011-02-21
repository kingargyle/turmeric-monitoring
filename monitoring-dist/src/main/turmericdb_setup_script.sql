-- phpMyAdmin SQL Dump
-- version 3.3.2deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 18, 2011 at 04:41 PM
-- Server version: 5.1.41
-- PHP Version: 5.3.2-1ubuntu4.7

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `turmericdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `BasicAuth`
--

CREATE TABLE IF NOT EXISTS `BasicAuth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `subjectName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `BasicAuth`
--

INSERT INTO `BasicAuth` (`id`, `createdBy`, `createdOn`, `updatedBy`, `updatedOn`, `password`, `subjectName`) VALUES
(1, 'jose', '2010-12-22 14:13:54', NULL, NULL, 'admin', 'admin'),
(2, 'jose', '2010-12-22 14:13:54', NULL, NULL, 'guest', 'guest');

-- --------------------------------------------------------

--
-- Table structure for table `ConditionTbl`
--

CREATE TABLE IF NOT EXISTS `ConditionTbl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `expression_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC21C54C3643700EB` (`expression_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `ConditionTbl`
--

INSERT INTO `ConditionTbl` (`id`, `createdBy`, `createdOn`, `updatedBy`, `updatedOn`, `expression_id`) VALUES
(1, NULL, NULL, NULL, NULL, 1),
(2, NULL, NULL, NULL, NULL, 2);

-- --------------------------------------------------------

--
-- Table structure for table `Expression`
--

CREATE TABLE IF NOT EXISTS `Expression` (
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
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `Expression`
--

INSERT INTO `Expression` (`id`, `createdBy`, `createdOn`, `updatedBy`, `updatedOn`, `comment`, `name`, `primitiveValue_id`) VALUES
(1, NULL, NULL, NULL, NULL, 'Hits count', 'Hits', 1),
(2, NULL, NULL, NULL, NULL, 'service count', 'serverCount', 2);

-- --------------------------------------------------------

--
-- Table structure for table `Operation`
--

CREATE TABLE IF NOT EXISTS `Operation` (
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
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=977 ;

--
-- Dumping data for table `Operation`
--

INSERT INTO `Operation` (`id`, `createdBy`, `createdOn`, `updatedBy`, `updatedOn`, `description`, `operationName`, `resource_id`) VALUES
(1, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'checkout', 1),
(2, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'commit', 2),
(3, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'merge', 1),
(4, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'createPolicy', 3),
(5, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'getMetaData', 3),
(6, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'findPolicies', 3),
(95, 'admin', '2011-01-03 13:51:33', NULL, NULL, NULL, '69', 4),
(96, 'admin', '2011-01-03 13:51:33', NULL, NULL, NULL, 'policydelete', 4),
(97, 'admin', '2011-01-03 13:51:33', NULL, NULL, NULL, '69', 5),
(98, 'admin', '2011-01-03 13:51:33', NULL, NULL, NULL, 'policydelete', 5),
(99, 'admin', '2011-01-03 13:51:33', NULL, NULL, NULL, '69', 6),
(100, 'admin', '2011-01-03 13:51:33', NULL, NULL, NULL, 'policydelete', 6),
(101, 'admin', '2011-01-03 13:51:33', NULL, NULL, NULL, '69', 7),
(102, 'admin', '2011-01-03 13:51:33', NULL, NULL, NULL, 'policydelete', 7),
(103, 'admin', '2011-01-03 13:51:33', NULL, NULL, NULL, 'Admin_Policy_policydelete', 8),
(104, 'admin', '2011-01-03 13:51:33', NULL, NULL, NULL, '28', 8),
(7, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'findSubjectGroups', 3),
(8, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'findSubjects', 3),
(9, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'findResources', 3),
(10, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'createSubjectGroups', 3),
(13, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'getResources', 3),
(14, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'getEntityHistory', 3),
(11, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'updateSubjectGroups', 3),
(12, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'deletePolicy', 3),
(20, NULL, '2010-01-31 10:49:55', NULL, NULL, NULL, 'enablePolicy', 3),
(21, NULL, '2010-01-31 10:49:55', NULL, NULL, NULL, 'disablePolicy', 3),
(22, NULL, '2010-01-31 10:49:55', NULL, NULL, NULL, 'updatePolicy', 3),
(23, NULL, '2010-01-31 10:49:55', NULL, NULL, NULL, 'deleteSubjectGroups', 3),
(24, NULL, '2010-01-31 10:49:55', NULL, NULL, NULL, 'findExternalSubjects', 3),
(25, NULL, '2010-01-31 10:49:55', NULL, NULL, NULL, 'createSubjects', 3),
(106, 'admin', '2011-02-11 19:01:29', NULL, NULL, NULL, '71', 4),
(108, 'admin', '2011-02-11 19:01:29', NULL, NULL, NULL, '71', 5),
(110, 'admin', '2011-02-11 19:01:29', NULL, NULL, NULL, '71', 6),
(112, 'admin', '2011-02-11 19:01:29', NULL, NULL, NULL, '71', 7),
(113, 'admin', '2011-02-11 19:01:29', NULL, NULL, NULL, 'Admin_Policy_dd', 8),
(114, 'admin', '2011-02-11 19:01:29', NULL, NULL, NULL, '29', 8),
(901, NULL, '2011-01-17 00:00:00', NULL, NULL, NULL, 'All', 4),
(912, NULL, '2011-01-17 00:00:00', NULL, NULL, NULL, 'All', 5),
(913, NULL, '2011-01-17 00:00:00', NULL, NULL, NULL, 'All', 6),
(914, NULL, '2011-01-17 00:00:00', NULL, NULL, NULL, 'All', 7),
(915, NULL, '2011-01-17 00:00:00', NULL, NULL, NULL, 'All', 8),
(916, NULL, '2011-01-17 00:00:00', NULL, NULL, NULL, 'All', 9),
(917, 'admin', '2011-02-11 19:27:09', NULL, NULL, NULL, 'SuperPolicy', 4),
(918, 'admin', '2011-02-11 19:27:09', NULL, NULL, NULL, '73', 4),
(919, 'admin', '2011-02-11 19:27:09', NULL, NULL, NULL, 'SuperPolicy', 5),
(920, 'admin', '2011-02-11 19:27:09', NULL, NULL, NULL, '73', 5),
(921, 'admin', '2011-02-11 19:27:09', NULL, NULL, NULL, 'SuperPolicy', 6),
(922, 'admin', '2011-02-11 19:27:09', NULL, NULL, NULL, '73', 6),
(923, 'admin', '2011-02-11 19:27:09', NULL, NULL, NULL, 'SuperPolicy', 7),
(924, 'admin', '2011-02-11 19:27:09', NULL, NULL, NULL, '73', 7),
(925, 'admin', '2011-02-11 19:27:09', NULL, NULL, NULL, 'Admin_Policy_SuperPolicy', 8),
(926, 'admin', '2011-02-11 19:27:09', NULL, NULL, NULL, '30', 8);

-- --------------------------------------------------------

--
-- Table structure for table `Policy`
--

CREATE TABLE IF NOT EXISTS `Policy` (
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
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=86 ;

--
-- Dumping data for table `Policy`
--

INSERT INTO `Policy` (`id`, `createdBy`, `createdOn`, `updatedBy`, `updatedOn`, `active`, `description`, `policyName`, `policyType`) VALUES
(1, 'admin', '2010-12-14 10:12:58', NULL, '2010-12-20 15:02:32', b'1', 'description of BLPolicy1', 'BLPolicy1', 'BLACKLIST'),
(3, 'admin', '2010-12-14 16:39:04', NULL, '2010-12-23 16:35:55', b'1', 'description of BLPolicy2', 'BLPolicy2', 'BLACKLIST'),
(4, 'admin', '2010-12-14 16:39:04', NULL, NULL, b'1', 'description of RL1', 'RL1', 'RL'),
(5, 'admin', '2010-12-14 16:39:04', NULL, NULL, b'1', 'description of RL2', 'RL2', 'RL'),
(6, 'admin', '2010-12-14 16:39:04', NULL, NULL, b'1', 'description of whitelist1', 'whitelist1', 'WHITELIST'),
(8, 'admin', '2010-12-23 15:25:07', NULL, NULL, b'1', 'an auth policy', 'PolicyService', 'AUTHZ'),
(71, 'admin', '2011-02-11 19:01:29', NULL, '2011-02-11 19:02:10', b'1', 'description of BLPolicy3', 'BLPolicy3', 'BLACKLIST'),
(73, 'admin', '2011-02-11 19:27:09', NULL, '2011-02-17 17:09:52', b'1', 'The base admin policy', 'SuperPolicy', 'AUTHZ'),
(74, 'admin', '2011-02-11 19:27:09', NULL, '2011-02-11 19:27:10', b'1', 'Managing SubjectGroup SuperPolicy', 'Admin_Policy_SuperPolicy', 'AUTHZ');

-- --------------------------------------------------------

--
-- Table structure for table `Policy_ExclusionSubjectGroups`
--

CREATE TABLE IF NOT EXISTS `Policy_ExclusionSubjectGroups` (
  `Policy_id` bigint(20) NOT NULL,
  `exclusionSubjectGroups_id` bigint(20) NOT NULL,
  KEY `FK45E0F89F741F702C` (`exclusionSubjectGroups_id`),
  KEY `FK45E0F89FD5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Policy_ExclusionSubjectGroups`
--


-- --------------------------------------------------------

--
-- Table structure for table `Policy_ExclusionSubjects`
--

CREATE TABLE IF NOT EXISTS `Policy_ExclusionSubjects` (
  `Policy_id` bigint(20) NOT NULL,
  `exclusionSubjects_id` bigint(20) NOT NULL,
  KEY `FKC8318EE8B3D781A0` (`exclusionSubjects_id`),
  KEY `FKC8318EE8D5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Policy_ExclusionSubjects`
--


-- --------------------------------------------------------

--
-- Table structure for table `Policy_Operation`
--

CREATE TABLE IF NOT EXISTS `Policy_Operation` (
  `Policy_id` bigint(20) NOT NULL,
  `operations_id` bigint(20) NOT NULL,
  KEY `FK6CA0829A4BFA9764` (`operations_id`),
  KEY `FK6CA0829AD5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Policy_Operation`
--

INSERT INTO `Policy_Operation` (`Policy_id`, `operations_id`) VALUES
(1, 1),
(5, 2),
(8, 12),
(8, 11),
(8, 10),
(8, 14),
(8, 20),
(8, 21),
(8, 22),
(8, 23),
(8, 9),
(8, 8),
(8, 7),
(8, 6),
(8, 13),
(8, 5),
(8, 4),
(8, 24),
(8, 25),
(73, 916),
(73, 901),
(73, 914),
(73, 915),
(73, 912),
(73, 913),
(74, 926),
(74, 924),
(74, 925),
(74, 922),
(74, 923),
(74, 920),
(74, 921),
(74, 918),
(74, 919),
(74, 917);

-- --------------------------------------------------------

--
-- Table structure for table `Policy_Resource`
--

CREATE TABLE IF NOT EXISTS `Policy_Resource` (
  `Policy_id` bigint(20) NOT NULL,
  `resources_id` bigint(20) NOT NULL,
  KEY `FKCAF2247BF4EC17F4` (`resources_id`),
  KEY `FKCAF2247BD5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Policy_Resource`
--

INSERT INTO `Policy_Resource` (`Policy_id`, `resources_id`) VALUES
(1, 1),
(5, 2),
(8, 3);

-- --------------------------------------------------------

--
-- Table structure for table `Policy_Rule`
--

CREATE TABLE IF NOT EXISTS `Policy_Rule` (
  `Policy_id` bigint(20) NOT NULL,
  `rules_id` bigint(20) NOT NULL,
  KEY `FKE46F35E941837750` (`rules_id`),
  KEY `FKE46F35E9D5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Policy_Rule`
--

INSERT INTO `Policy_Rule` (`Policy_id`, `rules_id`) VALUES
(1, 1),
(4, 1),
(5, 1),
(5, 2),
(6, 1);

-- --------------------------------------------------------

--
-- Table structure for table `Policy_Subject`
--

CREATE TABLE IF NOT EXISTS `Policy_Subject` (
  `Policy_id` bigint(20) NOT NULL,
  `subjects_id` bigint(20) NOT NULL,
  KEY `FK5E0FB31FAB44C2E` (`subjects_id`),
  KEY `FK5E0FB31FD5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Policy_Subject`
--

INSERT INTO `Policy_Subject` (`Policy_id`, `subjects_id`) VALUES
(1, 1),
(6, 2),
(4, 3),
(5, 5),
(6, 5),
(6, 4),
(3, 3),
(8, 1),
(71, 7),
(8, 7),
(73, 1);

-- --------------------------------------------------------

--
-- Table structure for table `Policy_SubjectGroup`
--

CREATE TABLE IF NOT EXISTS `Policy_SubjectGroup` (
  `Policy_id` bigint(20) NOT NULL,
  `subjectGroups_id` bigint(20) NOT NULL,
  KEY `FKBFCC6EA0AC010E5E` (`subjectGroups_id`),
  KEY `FKBFCC6EA0D5D8832B` (`Policy_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Policy_SubjectGroup`
--

INSERT INTO `Policy_SubjectGroup` (`Policy_id`, `subjectGroups_id`) VALUES
(74, 30);

-- --------------------------------------------------------

--
-- Table structure for table `PrimitiveValue`
--

CREATE TABLE IF NOT EXISTS `PrimitiveValue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `PrimitiveValue`
--

INSERT INTO `PrimitiveValue` (`id`, `createdBy`, `createdOn`, `updatedBy`, `updatedOn`, `type`, `value`) VALUES
(1, NULL, NULL, NULL, NULL, 0, 'HITS>5'),
(2, NULL, NULL, NULL, NULL, 0, 'PaymentService.commit:count>3');

-- --------------------------------------------------------

--
-- Table structure for table `Resource`
--

CREATE TABLE IF NOT EXISTS `Resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `resourceName` varchar(255) DEFAULT NULL,
  `resourceType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `Resource`
--

INSERT INTO `Resource` (`id`, `createdBy`, `createdOn`, `updatedBy`, `updatedOn`, `description`, `resourceName`, `resourceType`) VALUES
(1, NULL, '2010-12-14 10:49:55', NULL, NULL, 'test', 'testService', 'SERVICE'),
(2, NULL, '2010-12-14 11:08:27', NULL, NULL, 'desc Payment', 'PaymentService', 'SERVICE'),
(3, NULL, '2010-12-14 10:49:55', NULL, NULL, NULL, 'PolicyService', 'SERVICE'),
(4, 'admin', '2010-12-29 17:38:46', NULL, NULL, NULL, 'SERVICE.PolicyService.disablePolicy', 'OBJECT'),
(5, 'admin', '2010-12-29 17:38:46', NULL, NULL, NULL, 'SERVICE.PolicyService.deletePolicy', 'OBJECT'),
(6, 'admin', '2010-12-29 17:38:46', NULL, NULL, NULL, 'SERVICE.PolicyService.enablePolicy', 'OBJECT'),
(7, 'admin', '2010-12-29 17:38:46', NULL, NULL, NULL, 'SERVICE.PolicyService.updatePolicy', 'OBJECT'),
(8, 'admin', '2010-12-29 17:38:46', NULL, NULL, NULL, 'SERVICE.PolicyService.updateSubjectGroups', 'OBJECT'),
(9, 'admin', '2011-01-02 18:03:17', NULL, NULL, NULL, 'SERVICE.PolicyService.deleteSubjectGroups', 'OBJECT');

-- --------------------------------------------------------

--
-- Table structure for table `Rule`
--

CREATE TABLE IF NOT EXISTS `Rule` (
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
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `Rule`
--

INSERT INTO `Rule` (`id`, `createdBy`, `createdOn`, `updatedBy`, `updatedOn`, `description`, `effect`, `effectDuration`, `priority`, `rolloverPeriod`, `ruleName`, `condition_id`, `notifyEmails`, `notifyActive`) VALUES
(1, NULL, NULL, NULL, NULL, 'HITS TEST', 1, 10000, 3, 30000, 'rul1', 1, NULL, b'0'),
(2, NULL, NULL, NULL, NULL, 'ServiceXXX.OperationXX.count test', 3, 10000, NULL, 30000, 'server', 2, NULL, b'0');

-- --------------------------------------------------------

--
-- Table structure for table `Subject`
--

CREATE TABLE IF NOT EXISTS `Subject` (
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
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `Subject`
--

INSERT INTO `Subject` (`id`, `createdBy`, `createdOn`, `updatedBy`, `updatedOn`, `description`, `emailContact`, `externalSubjectId`, `ipMask`, `subjectName`, `subjectType`) VALUES
(1, 'admin', '2011-02-17 17:08:33', 'admin', NULL, 'Admin User', NULL, 0, NULL, 'admin', 'USER'),
(2, NULL, '2010-12-14 10:37:42', NULL, NULL, 'IPDESK', NULL, 0, NULL, '1.102.96.107', 'IP'),
(3, NULL, '2010-12-14 10:37:42', NULL, NULL, 'IPDESK', NULL, 0, NULL, '1.102.96.103', 'IP'),
(4, NULL, '2010-12-14 10:37:42', NULL, NULL, 'IPDESK', NULL, 0, NULL, '1.102.96.104', 'IP'),
(5, NULL, '2010-12-14 10:37:42', NULL, NULL, 'IPDESK', NULL, 0, NULL, '1.102.96.105', 'IP'),
(6, NULL, '2010-12-14 10:37:42', NULL, NULL, 'IPDESK', NULL, 0, NULL, '1.102.96.106', 'IP'),
(7, 'admin', '2011-02-17 19:01:55', NULL, NULL, 'Guest user', NULL, 0, NULL, 'guest', 'USER');

-- --------------------------------------------------------

--
-- Table structure for table `SubjectGroup`
--

CREATE TABLE IF NOT EXISTS `SubjectGroup` (
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
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=37 ;

--
-- Dumping data for table `SubjectGroup`
--

INSERT INTO `SubjectGroup` (`id`, `createdBy`, `createdOn`, `updatedBy`, `updatedOn`, `applyToAll`, `applyToEach`, `description`, `subjectGroupCalculator`, `subjectGroupName`, `subjectType`) VALUES
(23, 'admin', '2011-01-02 18:03:17', NULL, NULL, b'0', b'0', 'first subjectgroup', NULL, 'SG_1', 'IP'),
(30, 'admin', '2011-02-11 19:27:09', NULL, '2011-02-18 13:55:47', b'0', b'0', 'Managing Admin_Policy_SuperPolicy', NULL, 'Admin_Policy_SuperPolicy', 'USER');

-- --------------------------------------------------------

--
-- Table structure for table `SubjectGroup_Subject`
--

CREATE TABLE IF NOT EXISTS `SubjectGroup_Subject` (
  `SubjectGroup_id` bigint(20) NOT NULL,
  `subjects_id` bigint(20) NOT NULL,
  KEY `FKFBFB71A0AB44C2E` (`subjects_id`),
  KEY `FKFBFB71A0B009DAAB` (`SubjectGroup_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `SubjectGroup_Subject`
--

INSERT INTO `SubjectGroup_Subject` (`SubjectGroup_id`, `subjects_id`) VALUES
(12, 2),
(12, 3),
(13, 3),
(12, 1),
(11, 5),
(12, 4),
(12, 5),
(12, 6),
(14, 5),
(15, 1),
(16, 1),
(17, 1),
(18, 1),
(19, 1),
(20, 1),
(21, 1),
(22, 1),
(23, 3),
(30, 1);

-- --------------------------------------------------------

--
-- Table structure for table `SubjectType`
--

CREATE TABLE IF NOT EXISTS `SubjectType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdOn` datetime DEFAULT NULL,
  `updatedBy` varchar(255) DEFAULT NULL,
  `updatedOn` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `external` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `SubjectType`
--
