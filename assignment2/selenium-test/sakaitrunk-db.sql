-- MySQL dump 10.13  Distrib 5.1.49, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: sakaitrunk
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
-- Table structure for table `A2_ASSIGNMENT_T`
--

DROP TABLE IF EXISTS `A2_ASSIGNMENT_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `A2_ASSIGNMENT_T` (
  `ASSIGNMENT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `GRADEBOOK_ITEM_ID` bigint(20) DEFAULT NULL,
  `CONTEXT` varchar(99) NOT NULL,
  `TITLE` varchar(255) NOT NULL,
  `DRAFT` bit(1) DEFAULT NULL,
  `SORT_INDEX` int(11) NOT NULL,
  `OPEN_DATE` datetime NOT NULL,
  `ACCEPT_UNTIL_DATE` datetime DEFAULT NULL,
  `GRADED` bit(1) DEFAULT NULL,
  `DUE_DATE` datetime DEFAULT NULL,
  `HONOR_PLEDGE` bit(1) DEFAULT NULL,
  `INSTRUCTIONS` text,
  `REQUIRES_SUBMISSION` bit(1) DEFAULT NULL,
  `SUBMISSION_TYPE` int(11) NOT NULL,
  `SEND_SUBMISSION_NOTIF` bit(1) DEFAULT NULL,
  `HAS_ANNOUNCEMENT` bit(1) DEFAULT NULL,
  `ANNOUNCEMENT_ID` varchar(99) DEFAULT NULL,
  `ADDED_TO_SCHEDULE` bit(1) DEFAULT NULL,
  `EVENT_ID` varchar(99) DEFAULT NULL,
  `NUM_SUB_ALLOWED` int(11) NOT NULL,
  `CONTENT_REVIEW_ENABLED` bit(1) DEFAULT NULL,
  `CONTENT_REVIEW_REF` varchar(255) DEFAULT NULL,
  `MODEL_ANSWER_ENABLED` bit(1) DEFAULT NULL,
  `MODEL_ANSWER_TEXT` text,
  `MODEL_ANSWER_DISPLAY_RULE` int(11) DEFAULT NULL,
  `CREATOR` varchar(99) NOT NULL,
  `CREATE_DATE` datetime NOT NULL,
  `MODIFIED_BY` varchar(99) DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  `REMOVED` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ASSIGNMENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `A2_ASSIGNMENT_T`
--

LOCK TABLES `A2_ASSIGNMENT_T` WRITE;
/*!40000 ALTER TABLE `A2_ASSIGNMENT_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `A2_ASSIGNMENT_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `A2_ASSIGN_ATTACH_T`
--

DROP TABLE IF EXISTS `A2_ASSIGN_ATTACH_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `A2_ASSIGN_ATTACH_T` (
  `ASSIGN_ATTACH_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ASSIGN_ATTACH_TYPE` varchar(1) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `ASSIGNMENT_ID` bigint(20) NOT NULL,
  `ATTACHMENT_REFERENCE` varchar(255) NOT NULL,
  PRIMARY KEY (`ASSIGN_ATTACH_ID`),
  UNIQUE KEY `ASSIGNMENT_ID` (`ASSIGNMENT_ID`,`ATTACHMENT_REFERENCE`),
  KEY `FKFF1065FC175E3454` (`ASSIGNMENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `A2_ASSIGN_ATTACH_T`
--

LOCK TABLES `A2_ASSIGN_ATTACH_T` WRITE;
/*!40000 ALTER TABLE `A2_ASSIGN_ATTACH_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `A2_ASSIGN_ATTACH_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `A2_ASSIGN_GROUP_T`
--

DROP TABLE IF EXISTS `A2_ASSIGN_GROUP_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `A2_ASSIGN_GROUP_T` (
  `ASSIGNMENT_GROUP_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `ASSIGNMENT_ID` bigint(20) NOT NULL,
  `GROUP_REF` varchar(255) NOT NULL,
  PRIMARY KEY (`ASSIGNMENT_GROUP_ID`),
  UNIQUE KEY `ASSIGNMENT_ID` (`ASSIGNMENT_ID`,`GROUP_REF`),
  KEY `FK39B6CD12175E3454` (`ASSIGNMENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `A2_ASSIGN_GROUP_T`
--

LOCK TABLES `A2_ASSIGN_GROUP_T` WRITE;
/*!40000 ALTER TABLE `A2_ASSIGN_GROUP_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `A2_ASSIGN_GROUP_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `A2_SUBMISSION_ATTACH_T`
--

DROP TABLE IF EXISTS `A2_SUBMISSION_ATTACH_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `A2_SUBMISSION_ATTACH_T` (
  `SUBMISSION_ATTACH_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SUB_ATTACH_TYPE` varchar(1) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `SUBMISSION_VERSION_ID` bigint(20) NOT NULL,
  `ATTACHMENT_REFERENCE` varchar(255) NOT NULL,
  PRIMARY KEY (`SUBMISSION_ATTACH_ID`),
  UNIQUE KEY `SUBMISSION_VERSION_ID` (`SUBMISSION_VERSION_ID`,`ATTACHMENT_REFERENCE`),
  KEY `FK3FF3D33F49CF92D6` (`SUBMISSION_VERSION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `A2_SUBMISSION_ATTACH_T`
--

LOCK TABLES `A2_SUBMISSION_ATTACH_T` WRITE;
/*!40000 ALTER TABLE `A2_SUBMISSION_ATTACH_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `A2_SUBMISSION_ATTACH_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `A2_SUBMISSION_T`
--

DROP TABLE IF EXISTS `A2_SUBMISSION_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `A2_SUBMISSION_T` (
  `SUBMISSION_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `ASSIGNMENT_ID` bigint(20) NOT NULL,
  `USER_ID` varchar(99) NOT NULL,
  `RESUBMIT_CLOSE_DATE` datetime DEFAULT NULL,
  `NUM_SUB_ALLOWED` int(11) DEFAULT NULL,
  `COMPLETED` bit(1) DEFAULT NULL,
  `CREATED_BY` varchar(99) NOT NULL,
  `CREATED_DATE` datetime NOT NULL,
  `MODIFIED_BY` varchar(99) DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`SUBMISSION_ID`),
  UNIQUE KEY `ASSIGNMENT_ID` (`ASSIGNMENT_ID`,`USER_ID`),
  KEY `FK4A5A558F175E3454` (`ASSIGNMENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `A2_SUBMISSION_T`
--

LOCK TABLES `A2_SUBMISSION_T` WRITE;
/*!40000 ALTER TABLE `A2_SUBMISSION_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `A2_SUBMISSION_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `A2_SUBMISSION_VERSION_T`
--

DROP TABLE IF EXISTS `A2_SUBMISSION_VERSION_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `A2_SUBMISSION_VERSION_T` (
  `SUBMISSION_VERSION_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `SUBMISSION_ID` bigint(20) NOT NULL,
  `SUBMITTED_DATE` datetime DEFAULT NULL,
  `SUBMITTED_VERSION_NUMBER` int(11) NOT NULL,
  `FEEDBACK_RELEASED_DATE` datetime DEFAULT NULL,
  `SUBMITTED_TEXT` text,
  `HONOR_PLEDGE` bit(1) DEFAULT NULL,
  `ANNOTATED_TEXT` text,
  `FEEDBACK_NOTES` text,
  `DRAFT` bit(1) DEFAULT NULL,
  `CREATED_BY` varchar(99) NOT NULL,
  `CREATED_DATE` datetime NOT NULL,
  `MODIFIED_BY` varchar(99) DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  `LAST_FEEDBACK_BY` varchar(99) DEFAULT NULL,
  `LAST_FEEDBACK_DATE` datetime DEFAULT NULL,
  `STUDENT_SAVE_DATE` datetime DEFAULT NULL,
  `FEEDBACK_LAST_VIEWED` datetime DEFAULT NULL,
  PRIMARY KEY (`SUBMISSION_VERSION_ID`),
  UNIQUE KEY `SUBMISSION_ID` (`SUBMISSION_ID`,`SUBMITTED_VERSION_NUMBER`),
  KEY `FK873450C88ABCB1A5` (`SUBMISSION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `A2_SUBMISSION_VERSION_T`
--

LOCK TABLES `A2_SUBMISSION_VERSION_T` WRITE;
/*!40000 ALTER TABLE `A2_SUBMISSION_VERSION_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `A2_SUBMISSION_VERSION_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ANNOUNCEMENT_CHANNEL`
--

DROP TABLE IF EXISTS `ANNOUNCEMENT_CHANNEL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ANNOUNCEMENT_CHANNEL` (
  `CHANNEL_ID` varchar(255) NOT NULL,
  `NEXT_ID` int(11) DEFAULT NULL,
  `XML` longtext,
  UNIQUE KEY `ANNOUNCEMENT_CHANNEL_INDEX` (`CHANNEL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ANNOUNCEMENT_CHANNEL`
--

LOCK TABLES `ANNOUNCEMENT_CHANNEL` WRITE;
/*!40000 ALTER TABLE `ANNOUNCEMENT_CHANNEL` DISABLE KEYS */;
INSERT INTO `ANNOUNCEMENT_CHANNEL` VALUES ('/announcement/channel/~d1430d8d-af0b-49e6-8c39-2d253673319a/main',0,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<channel context=\"~d1430d8d-af0b-49e6-8c39-2d253673319a\" id=\"main\"><properties/></channel>'),('/announcement/channel/92baf195-be33-4a5b-b378-6d96e9665ffc/main',0,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<channel context=\"92baf195-be33-4a5b-b378-6d96e9665ffc\" id=\"main\"><properties/></channel>');
/*!40000 ALTER TABLE `ANNOUNCEMENT_CHANNEL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ANNOUNCEMENT_MESSAGE`
--

DROP TABLE IF EXISTS `ANNOUNCEMENT_MESSAGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ANNOUNCEMENT_MESSAGE` (
  `CHANNEL_ID` varchar(255) NOT NULL,
  `MESSAGE_ID` varchar(36) NOT NULL,
  `DRAFT` char(1) DEFAULT NULL,
  `PUBVIEW` char(1) DEFAULT NULL,
  `OWNER` varchar(99) DEFAULT NULL,
  `MESSAGE_DATE` datetime NOT NULL,
  `XML` longtext,
  `MESSAGE_ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`CHANNEL_ID`,`MESSAGE_ID`),
  KEY `IE_ANNC_MSG_CHANNEL` (`CHANNEL_ID`),
  KEY `IE_ANNC_MSG_ATTRIB` (`DRAFT`,`PUBVIEW`,`OWNER`,`MESSAGE_ORDER`),
  KEY `IE_ANNC_MSG_DATE` (`MESSAGE_DATE`),
  KEY `IE_ANNC_MSG_DATE_DESC` (`MESSAGE_DATE`),
  KEY `ANNOUNCEMENT_MESSAGE_CDD` (`CHANNEL_ID`,`MESSAGE_DATE`,`MESSAGE_ORDER`,`DRAFT`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ANNOUNCEMENT_MESSAGE`
--

LOCK TABLES `ANNOUNCEMENT_MESSAGE` WRITE;
/*!40000 ALTER TABLE `ANNOUNCEMENT_MESSAGE` DISABLE KEYS */;
/*!40000 ALTER TABLE `ANNOUNCEMENT_MESSAGE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASN_AP_ITEM_ACCESS_T`
--

DROP TABLE IF EXISTS `ASN_AP_ITEM_ACCESS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ASN_AP_ITEM_ACCESS_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEM_ACCESS` varchar(255) DEFAULT NULL,
  `ASN_AP_ITEM_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ITEM_ACCESS` (`ITEM_ACCESS`,`ASN_AP_ITEM_ID`),
  KEY `FK573733586E844C61` (`ASN_AP_ITEM_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASN_AP_ITEM_ACCESS_T`
--

LOCK TABLES `ASN_AP_ITEM_ACCESS_T` WRITE;
/*!40000 ALTER TABLE `ASN_AP_ITEM_ACCESS_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `ASN_AP_ITEM_ACCESS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASN_AP_ITEM_T`
--

DROP TABLE IF EXISTS `ASN_AP_ITEM_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ASN_AP_ITEM_T` (
  `ID` bigint(20) NOT NULL,
  `ASSIGNMENT_ID` varchar(255) DEFAULT NULL,
  `TITLE` varchar(255) DEFAULT NULL,
  `TEXT` text,
  `RELEASE_DATE` datetime DEFAULT NULL,
  `RETRACT_DATE` datetime DEFAULT NULL,
  `HIDE` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK514CEE15935EEE07` (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASN_AP_ITEM_T`
--

LOCK TABLES `ASN_AP_ITEM_T` WRITE;
/*!40000 ALTER TABLE `ASN_AP_ITEM_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `ASN_AP_ITEM_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASN_MA_ITEM_T`
--

DROP TABLE IF EXISTS `ASN_MA_ITEM_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ASN_MA_ITEM_T` (
  `ID` bigint(20) NOT NULL,
  `ASSIGNMENT_ID` varchar(255) DEFAULT NULL,
  `TEXT` text,
  `SHOW_TO` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK2E508110935EEE07` (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASN_MA_ITEM_T`
--

LOCK TABLES `ASN_MA_ITEM_T` WRITE;
/*!40000 ALTER TABLE `ASN_MA_ITEM_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `ASN_MA_ITEM_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASN_NOTE_ITEM_T`
--

DROP TABLE IF EXISTS `ASN_NOTE_ITEM_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ASN_NOTE_ITEM_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ASSIGNMENT_ID` varchar(255) DEFAULT NULL,
  `NOTE` text,
  `CREATOR_ID` varchar(255) DEFAULT NULL,
  `SHARE_WITH` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASN_NOTE_ITEM_T`
--

LOCK TABLES `ASN_NOTE_ITEM_T` WRITE;
/*!40000 ALTER TABLE `ASN_NOTE_ITEM_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `ASN_NOTE_ITEM_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASN_SUP_ATTACH_T`
--

DROP TABLE IF EXISTS `ASN_SUP_ATTACH_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ASN_SUP_ATTACH_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATTACHMENT_ID` varchar(255) DEFAULT NULL,
  `ASN_SUP_ITEM_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ATTACHMENT_ID` (`ATTACHMENT_ID`,`ASN_SUP_ITEM_ID`),
  KEY `FK560294CEDE4CD07F` (`ASN_SUP_ITEM_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='This table is for assignment supplement item attachment.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASN_SUP_ATTACH_T`
--

LOCK TABLES `ASN_SUP_ATTACH_T` WRITE;
/*!40000 ALTER TABLE `ASN_SUP_ATTACH_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `ASN_SUP_ATTACH_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASN_SUP_ITEM_T`
--

DROP TABLE IF EXISTS `ASN_SUP_ITEM_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ASN_SUP_ITEM_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASN_SUP_ITEM_T`
--

LOCK TABLES `ASN_SUP_ITEM_T` WRITE;
/*!40000 ALTER TABLE `ASN_SUP_ITEM_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `ASN_SUP_ITEM_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASSIGNMENT_ASSIGNMENT`
--

DROP TABLE IF EXISTS `ASSIGNMENT_ASSIGNMENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ASSIGNMENT_ASSIGNMENT` (
  `ASSIGNMENT_ID` varchar(99) NOT NULL,
  `CONTEXT` varchar(99) DEFAULT NULL,
  `XML` longtext,
  UNIQUE KEY `ASSIGNMENT_ASSIGNMENT_INDEX` (`ASSIGNMENT_ID`),
  KEY `ASSIGNMENT_ASSIGNMENT_CONTEXT` (`CONTEXT`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASSIGNMENT_ASSIGNMENT`
--

LOCK TABLES `ASSIGNMENT_ASSIGNMENT` WRITE;
/*!40000 ALTER TABLE `ASSIGNMENT_ASSIGNMENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `ASSIGNMENT_ASSIGNMENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASSIGNMENT_CONTENT`
--

DROP TABLE IF EXISTS `ASSIGNMENT_CONTENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ASSIGNMENT_CONTENT` (
  `CONTENT_ID` varchar(99) NOT NULL,
  `CONTEXT` varchar(99) DEFAULT NULL,
  `XML` longtext,
  UNIQUE KEY `ASSIGNMENT_CONTENT_INDEX` (`CONTENT_ID`),
  KEY `ASSIGNMENT_CONTENT_CONTEXT` (`CONTEXT`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASSIGNMENT_CONTENT`
--

LOCK TABLES `ASSIGNMENT_CONTENT` WRITE;
/*!40000 ALTER TABLE `ASSIGNMENT_CONTENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `ASSIGNMENT_CONTENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASSIGNMENT_SUBMISSION`
--

DROP TABLE IF EXISTS `ASSIGNMENT_SUBMISSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ASSIGNMENT_SUBMISSION` (
  `SUBMISSION_ID` varchar(99) NOT NULL,
  `CONTEXT` varchar(99) NOT NULL,
  `SUBMITTER_ID` varchar(99) NOT NULL,
  `SUBMIT_TIME` varchar(99) DEFAULT NULL,
  `SUBMITTED` varchar(6) DEFAULT NULL,
  `GRADED` varchar(6) DEFAULT NULL,
  `XML` longtext,
  UNIQUE KEY `ASSIGNMENT_SUBMISSION_INDEX` (`SUBMISSION_ID`),
  UNIQUE KEY `ASSIGNMENT_SUBMISSION_SUBMITTER_INDEX` (`CONTEXT`,`SUBMITTER_ID`),
  KEY `ASSIGNMENT_SUBMISSION_CONTEXT` (`CONTEXT`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASSIGNMENT_SUBMISSION`
--

LOCK TABLES `ASSIGNMENT_SUBMISSION` WRITE;
/*!40000 ALTER TABLE `ASSIGNMENT_SUBMISSION` DISABLE KEYS */;
/*!40000 ALTER TABLE `ASSIGNMENT_SUBMISSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CALENDAR_CALENDAR`
--

DROP TABLE IF EXISTS `CALENDAR_CALENDAR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CALENDAR_CALENDAR` (
  `CALENDAR_ID` varchar(99) NOT NULL,
  `NEXT_ID` int(11) DEFAULT NULL,
  `XML` longtext,
  UNIQUE KEY `CALENDAR_CALENDAR_INDEX` (`CALENDAR_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CALENDAR_CALENDAR`
--

LOCK TABLES `CALENDAR_CALENDAR` WRITE;
/*!40000 ALTER TABLE `CALENDAR_CALENDAR` DISABLE KEYS */;
INSERT INTO `CALENDAR_CALENDAR` VALUES ('/calendar/calendar/~d1430d8d-af0b-49e6-8c39-2d253673319a/main',0,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<calendar context=\"~d1430d8d-af0b-49e6-8c39-2d253673319a\" id=\"main\"><properties/></calendar>');
/*!40000 ALTER TABLE `CALENDAR_CALENDAR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CALENDAR_EVENT`
--

DROP TABLE IF EXISTS `CALENDAR_EVENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CALENDAR_EVENT` (
  `CALENDAR_ID` varchar(99) NOT NULL,
  `EVENT_ID` varchar(36) NOT NULL,
  `EVENT_START` datetime NOT NULL,
  `EVENT_END` datetime NOT NULL,
  `RANGE_START` int(11) NOT NULL,
  `RANGE_END` int(11) NOT NULL,
  `XML` longtext,
  UNIQUE KEY `EVENT_INDEX` (`EVENT_ID`),
  KEY `CALENDAR_EVENT_INDEX` (`CALENDAR_ID`),
  KEY `CALENDAR_EVENT_RSTART` (`RANGE_START`),
  KEY `CALENDAR_EVENT_REND` (`RANGE_END`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CALENDAR_EVENT`
--

LOCK TABLES `CALENDAR_EVENT` WRITE;
/*!40000 ALTER TABLE `CALENDAR_EVENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `CALENDAR_EVENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CHAT2_CHANNEL`
--

DROP TABLE IF EXISTS `CHAT2_CHANNEL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CHAT2_CHANNEL` (
  `CHANNEL_ID` varchar(99) NOT NULL,
  `PLACEMENT_ID` varchar(99) DEFAULT NULL,
  `CONTEXT` varchar(99) NOT NULL,
  `CREATION_DATE` datetime DEFAULT NULL,
  `title` varchar(64) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `filterType` varchar(25) DEFAULT NULL,
  `filterParam` int(11) DEFAULT NULL,
  `timeParam` int(11) NOT NULL,
  `numberParam` int(11) NOT NULL,
  `placementDefaultChannel` bit(1) DEFAULT NULL,
  `ENABLE_USER_OVERRIDE` bit(1) DEFAULT NULL,
  `migratedChannelId` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`CHANNEL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='This table stores chat channels';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CHAT2_CHANNEL`
--

LOCK TABLES `CHAT2_CHANNEL` WRITE;
/*!40000 ALTER TABLE `CHAT2_CHANNEL` DISABLE KEYS */;
/*!40000 ALTER TABLE `CHAT2_CHANNEL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CHAT2_MESSAGE`
--

DROP TABLE IF EXISTS `CHAT2_MESSAGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CHAT2_MESSAGE` (
  `MESSAGE_ID` varchar(99) NOT NULL,
  `CHANNEL_ID` varchar(99) DEFAULT NULL,
  `OWNER` varchar(96) NOT NULL,
  `MESSAGE_DATE` datetime DEFAULT NULL,
  `BODY` text NOT NULL,
  `migratedMessageId` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`MESSAGE_ID`),
  KEY `FK720F9882555E0B79` (`CHANNEL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='This table stores chat messages';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CHAT2_MESSAGE`
--

LOCK TABLES `CHAT2_MESSAGE` WRITE;
/*!40000 ALTER TABLE `CHAT2_MESSAGE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CHAT2_MESSAGE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CITATION_CITATION`
--

DROP TABLE IF EXISTS `CITATION_CITATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CITATION_CITATION` (
  `CITATION_ID` varchar(36) NOT NULL,
  `PROPERTY_NAME` varchar(255) DEFAULT NULL,
  `PROPERTY_VALUE` longtext,
  KEY `CITATION_CITATION_INDEX` (`CITATION_ID`),
  KEY `CITATION_CITATION_INDEX2` (`CITATION_ID`,`PROPERTY_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CITATION_CITATION`
--

LOCK TABLES `CITATION_CITATION` WRITE;
/*!40000 ALTER TABLE `CITATION_CITATION` DISABLE KEYS */;
/*!40000 ALTER TABLE `CITATION_CITATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CITATION_COLLECTION`
--

DROP TABLE IF EXISTS `CITATION_COLLECTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CITATION_COLLECTION` (
  `COLLECTION_ID` varchar(36) NOT NULL,
  `PROPERTY_NAME` varchar(255) DEFAULT NULL,
  `PROPERTY_VALUE` longtext,
  KEY `CITATION_COLLECTION_INDEX` (`COLLECTION_ID`),
  KEY `CITATION_COLLECTION_INDEX2` (`COLLECTION_ID`,`PROPERTY_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CITATION_COLLECTION`
--

LOCK TABLES `CITATION_COLLECTION` WRITE;
/*!40000 ALTER TABLE `CITATION_COLLECTION` DISABLE KEYS */;
/*!40000 ALTER TABLE `CITATION_COLLECTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CITATION_SCHEMA`
--

DROP TABLE IF EXISTS `CITATION_SCHEMA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CITATION_SCHEMA` (
  `SCHEMA_ID` varchar(36) NOT NULL,
  `PROPERTY_NAME` varchar(255) DEFAULT NULL,
  `PROPERTY_VALUE` longtext,
  KEY `CITATION_SCHEMA_INDEX` (`SCHEMA_ID`),
  KEY `CITATION_SCHEMA_INDEX2` (`SCHEMA_ID`,`PROPERTY_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CITATION_SCHEMA`
--

LOCK TABLES `CITATION_SCHEMA` WRITE;
/*!40000 ALTER TABLE `CITATION_SCHEMA` DISABLE KEYS */;
INSERT INTO `CITATION_SCHEMA` VALUES ('unknown','sakai:hasField','creator'),('unknown','sakai:hasField','title'),('unknown','sakai:hasField','year'),('unknown','sakai:hasField','date'),('unknown','sakai:hasField','publisher'),('unknown','sakai:hasField','publicationLocation'),('unknown','sakai:hasField','volume'),('unknown','sakai:hasField','issue'),('unknown','sakai:hasField','pages'),('unknown','sakai:hasField','startPage'),('unknown','sakai:hasField','endPage'),('unknown','sakai:hasField','edition'),('unknown','sakai:hasField','editor'),('unknown','sakai:hasField','sourceTitle'),('unknown','sakai:hasField','Language'),('unknown','sakai:hasField','abstract'),('unknown','sakai:hasField','note'),('unknown','sakai:hasField','isnIdentifier'),('unknown','sakai:hasField','subject'),('unknown','sakai:hasField','locIdentifier'),('unknown','sakai:hasField','dateRetrieved'),('unknown','sakai:hasField','openURL'),('unknown','sakai:hasField','doi'),('unknown','sakai:hasField','rights'),('article','sakai:hasField','creator'),('article','sakai:hasField','title'),('article','sakai:hasField','sourceTitle'),('article','sakai:hasField','year'),('article','sakai:hasField','date'),('article','sakai:hasField','volume'),('article','sakai:hasField','issue'),('article','sakai:hasField','pages'),('article','sakai:hasField','startPage'),('article','sakai:hasField','endPage'),('article','sakai:hasField','abstract'),('article','sakai:hasField','note'),('article','sakai:hasField','isnIdentifier'),('article','sakai:hasField','subject'),('article','sakai:hasField','Language'),('article','sakai:hasField','locIdentifier'),('article','sakai:hasField','dateRetrieved'),('article','sakai:hasField','openURL'),('article','sakai:hasField','doi'),('article','sakai:hasField','rights'),('book','sakai:hasField','creator'),('book','sakai:hasField','title'),('book','sakai:hasField','year'),('book','sakai:hasField','date'),('book','sakai:hasField','publisher'),('book','sakai:hasField','publicationLocation'),('book','sakai:hasField','edition'),('book','sakai:hasField','editor'),('book','sakai:hasField','sourceTitle'),('book','sakai:hasField','abstract'),('book','sakai:hasField','note'),('book','sakai:hasField','isnIdentifier'),('book','sakai:hasField','subject'),('book','sakai:hasField','Language'),('book','sakai:hasField','locIdentifier'),('book','sakai:hasField','dateRetrieved'),('book','sakai:hasField','openURL'),('book','sakai:hasField','doi'),('book','sakai:hasField','rights'),('chapter','sakai:hasField','creator'),('chapter','sakai:hasField','title'),('chapter','sakai:hasField','year'),('chapter','sakai:hasField','date'),('chapter','sakai:hasField','publisher'),('chapter','sakai:hasField','publicationLocation'),('chapter','sakai:hasField','edition'),('chapter','sakai:hasField','editor'),('chapter','sakai:hasField','sourceTitle'),('chapter','sakai:hasField','pages'),('chapter','sakai:hasField','startPage'),('chapter','sakai:hasField','endPage'),('chapter','sakai:hasField','abstract'),('chapter','sakai:hasField','note'),('chapter','sakai:hasField','isnIdentifier'),('chapter','sakai:hasField','subject'),('chapter','sakai:hasField','Language'),('chapter','sakai:hasField','locIdentifier'),('chapter','sakai:hasField','dateRetrieved'),('chapter','sakai:hasField','openURL'),('chapter','sakai:hasField','doi'),('chapter','sakai:hasField','rights'),('report','sakai:hasField','creator'),('report','sakai:hasField','title'),('report','sakai:hasField','year'),('report','sakai:hasField','date'),('report','sakai:hasField','publisher'),('report','sakai:hasField','publicationLocation'),('report','sakai:hasField','editor'),('report','sakai:hasField','edition'),('report','sakai:hasField','sourceTitle'),('report','sakai:hasField','pages'),('report','sakai:hasField','abstract'),('report','sakai:hasField','isnIdentifier'),('report','sakai:hasField','note'),('report','sakai:hasField','subject'),('report','sakai:hasField','Language'),('report','sakai:hasField','locIdentifier'),('report','sakai:hasField','dateRetrieved'),('report','sakai:hasField','openURL'),('report','sakai:hasField','doi'),('report','sakai:hasField','rights'),('proceed','sakai:hasField','creator'),('proceed','sakai:hasField','title'),('proceed','sakai:hasField','year'),('proceed','sakai:hasField','volume'),('proceed','sakai:hasField','pages'),('proceed','sakai:hasField','sourceTitle'),('proceed','sakai:hasField','note'),('electronic','sakai:hasField','title'),('electronic','sakai:hasField','year'),('electronic','sakai:hasField','sourceTitle'),('electronic','sakai:hasField','abstract'),('electronic','sakai:hasField','subject'),('thesis','sakai:hasField','creator'),('thesis','sakai:hasField','title'),('thesis','sakai:hasField','year'),('thesis','sakai:hasField','publisher'),('thesis','sakai:hasField','pages'),('thesis','sakai:hasField','note'),('thesis','sakai:hasField','subject');
/*!40000 ALTER TABLE `CITATION_SCHEMA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CITATION_SCHEMA_FIELD`
--

DROP TABLE IF EXISTS `CITATION_SCHEMA_FIELD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CITATION_SCHEMA_FIELD` (
  `SCHEMA_ID` varchar(36) NOT NULL,
  `FIELD_ID` varchar(36) NOT NULL,
  `PROPERTY_NAME` varchar(255) DEFAULT NULL,
  `PROPERTY_VALUE` longtext,
  KEY `CITATION_SCHEMA_FIELD_INDEX` (`SCHEMA_ID`,`FIELD_ID`),
  KEY `CITATION_SCHEMA_FIELD_INDEX2` (`SCHEMA_ID`,`FIELD_ID`,`PROPERTY_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CITATION_SCHEMA_FIELD`
--

LOCK TABLES `CITATION_SCHEMA_FIELD` WRITE;
/*!40000 ALTER TABLE `CITATION_SCHEMA_FIELD` DISABLE KEYS */;
INSERT INTO `CITATION_SCHEMA_FIELD` VALUES ('unknown','creator','sakai:hasOrder','0'),('unknown','creator','sakai:required','false'),('unknown','creator','sakai:minCardinality','0'),('unknown','creator','sakai:maxCardinality','2147483647'),('unknown','creator','sakai:valueType','shorttext'),('unknown','creator','sakai:ris_identifier','A1,AU'),('unknown','title','sakai:hasOrder','1'),('unknown','title','sakai:required','true'),('unknown','title','sakai:minCardinality','1'),('unknown','title','sakai:maxCardinality','1'),('unknown','title','sakai:valueType','shorttext'),('unknown','title','sakai:ris_identifier','T1,TI,CT,BT'),('unknown','year','sakai:hasOrder','2'),('unknown','year','sakai:required','false'),('unknown','year','sakai:minCardinality','0'),('unknown','year','sakai:maxCardinality','1'),('unknown','year','sakai:valueType','number'),('unknown','date','sakai:hasOrder','3'),('unknown','date','sakai:required','false'),('unknown','date','sakai:minCardinality','0'),('unknown','date','sakai:maxCardinality','1'),('unknown','date','sakai:valueType','number'),('unknown','date','sakai:ris_identifier','Y1,PY'),('unknown','publisher','sakai:hasOrder','4'),('unknown','publisher','sakai:required','false'),('unknown','publisher','sakai:minCardinality','0'),('unknown','publisher','sakai:maxCardinality','1'),('unknown','publisher','sakai:valueType','shorttext'),('unknown','publisher','sakai:ris_identifier','PB'),('unknown','publicationLocation','sakai:hasOrder','5'),('unknown','publicationLocation','sakai:required','false'),('unknown','publicationLocation','sakai:minCardinality','0'),('unknown','publicationLocation','sakai:maxCardinality','1'),('unknown','publicationLocation','sakai:valueType','shorttext'),('unknown','publicationLocation','sakai:ris_identifier','CY'),('unknown','volume','sakai:hasOrder','6'),('unknown','volume','sakai:required','false'),('unknown','volume','sakai:minCardinality','0'),('unknown','volume','sakai:maxCardinality','1'),('unknown','volume','sakai:valueType','number'),('unknown','volume','sakai:ris_identifier','VL'),('unknown','issue','sakai:hasOrder','7'),('unknown','issue','sakai:required','false'),('unknown','issue','sakai:minCardinality','0'),('unknown','issue','sakai:maxCardinality','1'),('unknown','issue','sakai:valueType','number'),('unknown','issue','sakai:ris_identifier','IS'),('unknown','pages','sakai:hasOrder','8'),('unknown','pages','sakai:required','false'),('unknown','pages','sakai:minCardinality','0'),('unknown','pages','sakai:maxCardinality','1'),('unknown','pages','sakai:valueType','number'),('unknown','pages','sakai:ris_identifier','SP'),('unknown','startPage','sakai:hasOrder','9'),('unknown','startPage','sakai:required','false'),('unknown','startPage','sakai:minCardinality','0'),('unknown','startPage','sakai:maxCardinality','1'),('unknown','startPage','sakai:valueType','number'),('unknown','startPage','sakai:ris_identifier','SP'),('unknown','endPage','sakai:hasOrder','10'),('unknown','endPage','sakai:required','false'),('unknown','endPage','sakai:minCardinality','0'),('unknown','endPage','sakai:maxCardinality','1'),('unknown','endPage','sakai:valueType','number'),('unknown','endPage','sakai:ris_identifier','EP'),('unknown','edition','sakai:hasOrder','11'),('unknown','edition','sakai:required','false'),('unknown','edition','sakai:minCardinality','0'),('unknown','edition','sakai:maxCardinality','1'),('unknown','edition','sakai:valueType','number'),('unknown','edition','sakai:ris_identifier','VL'),('unknown','editor','sakai:hasOrder','12'),('unknown','editor','sakai:required','false'),('unknown','editor','sakai:minCardinality','0'),('unknown','editor','sakai:maxCardinality','2147483647'),('unknown','editor','sakai:valueType','shorttext'),('unknown','editor','sakai:ris_identifier','ED,A2,A3'),('unknown','sourceTitle','sakai:hasOrder','13'),('unknown','sourceTitle','sakai:required','false'),('unknown','sourceTitle','sakai:minCardinality','0'),('unknown','sourceTitle','sakai:maxCardinality','1'),('unknown','sourceTitle','sakai:valueType','shorttext'),('unknown','sourceTitle','sakai:ris_identifier','T3'),('unknown','Language','sakai:hasOrder','14'),('unknown','Language','sakai:required','false'),('unknown','Language','sakai:minCardinality','0'),('unknown','Language','sakai:maxCardinality','1'),('unknown','Language','sakai:valueType','number'),('unknown','abstract','sakai:hasOrder','15'),('unknown','abstract','sakai:required','false'),('unknown','abstract','sakai:minCardinality','0'),('unknown','abstract','sakai:maxCardinality','1'),('unknown','abstract','sakai:valueType','longtext'),('unknown','abstract','sakai:ris_identifier','N2,AB'),('unknown','note','sakai:hasOrder','16'),('unknown','note','sakai:required','false'),('unknown','note','sakai:minCardinality','0'),('unknown','note','sakai:maxCardinality','2147483647'),('unknown','note','sakai:valueType','shorttext'),('unknown','note','sakai:ris_identifier','N1,AB'),('unknown','isnIdentifier','sakai:hasOrder','17'),('unknown','isnIdentifier','sakai:required','false'),('unknown','isnIdentifier','sakai:minCardinality','0'),('unknown','isnIdentifier','sakai:maxCardinality','1'),('unknown','isnIdentifier','sakai:valueType','shorttext'),('unknown','isnIdentifier','sakai:ris_identifier','SN'),('unknown','subject','sakai:hasOrder','18'),('unknown','subject','sakai:required','false'),('unknown','subject','sakai:minCardinality','0'),('unknown','subject','sakai:maxCardinality','2147483647'),('unknown','subject','sakai:valueType','shorttext'),('unknown','subject','sakai:ris_identifier','KW'),('unknown','locIdentifier','sakai:hasOrder','19'),('unknown','locIdentifier','sakai:required','false'),('unknown','locIdentifier','sakai:minCardinality','0'),('unknown','locIdentifier','sakai:maxCardinality','1'),('unknown','locIdentifier','sakai:valueType','shorttext'),('unknown','locIdentifier','sakai:ris_identifier','M1'),('unknown','dateRetrieved','sakai:hasOrder','20'),('unknown','dateRetrieved','sakai:required','false'),('unknown','dateRetrieved','sakai:minCardinality','0'),('unknown','dateRetrieved','sakai:maxCardinality','1'),('unknown','dateRetrieved','sakai:valueType','date'),('unknown','openURL','sakai:hasOrder','21'),('unknown','openURL','sakai:required','false'),('unknown','openURL','sakai:minCardinality','0'),('unknown','openURL','sakai:maxCardinality','1'),('unknown','openURL','sakai:valueType','shorttext'),('unknown','doi','sakai:hasOrder','22'),('unknown','doi','sakai:required','false'),('unknown','doi','sakai:minCardinality','0'),('unknown','doi','sakai:maxCardinality','1'),('unknown','doi','sakai:valueType','number'),('unknown','rights','sakai:hasOrder','23'),('unknown','rights','sakai:required','false'),('unknown','rights','sakai:minCardinality','0'),('unknown','rights','sakai:maxCardinality','2147483647'),('unknown','rights','sakai:valueType','shorttext'),('article','creator','sakai:hasOrder','0'),('article','creator','sakai:required','false'),('article','creator','sakai:minCardinality','0'),('article','creator','sakai:maxCardinality','2147483647'),('article','creator','sakai:valueType','shorttext'),('article','creator','sakai:ris_identifier','A1,AU'),('article','title','sakai:hasOrder','1'),('article','title','sakai:required','true'),('article','title','sakai:minCardinality','1'),('article','title','sakai:maxCardinality','1'),('article','title','sakai:valueType','shorttext'),('article','title','sakai:ris_identifier','T1,TI,CT'),('article','sourceTitle','sakai:hasOrder','2'),('article','sourceTitle','sakai:required','false'),('article','sourceTitle','sakai:minCardinality','0'),('article','sourceTitle','sakai:maxCardinality','1'),('article','sourceTitle','sakai:valueType','shorttext'),('article','sourceTitle','sakai:ris_identifier','JF,JO,JA,J1,J2,BT'),('article','year','sakai:hasOrder','3'),('article','year','sakai:required','false'),('article','year','sakai:minCardinality','0'),('article','year','sakai:maxCardinality','1'),('article','year','sakai:valueType','number'),('article','date','sakai:hasOrder','4'),('article','date','sakai:required','false'),('article','date','sakai:minCardinality','0'),('article','date','sakai:maxCardinality','1'),('article','date','sakai:valueType','number'),('article','date','sakai:ris_identifier','Y1,PY'),('article','volume','sakai:hasOrder','5'),('article','volume','sakai:required','false'),('article','volume','sakai:minCardinality','0'),('article','volume','sakai:maxCardinality','1'),('article','volume','sakai:valueType','number'),('article','volume','sakai:ris_identifier','VL'),('article','issue','sakai:hasOrder','6'),('article','issue','sakai:required','false'),('article','issue','sakai:minCardinality','0'),('article','issue','sakai:maxCardinality','1'),('article','issue','sakai:valueType','number'),('article','issue','sakai:ris_identifier','IS'),('article','pages','sakai:hasOrder','7'),('article','pages','sakai:required','false'),('article','pages','sakai:minCardinality','0'),('article','pages','sakai:maxCardinality','1'),('article','pages','sakai:valueType','number'),('article','startPage','sakai:hasOrder','8'),('article','startPage','sakai:required','false'),('article','startPage','sakai:minCardinality','0'),('article','startPage','sakai:maxCardinality','1'),('article','startPage','sakai:valueType','number'),('article','startPage','sakai:ris_identifier','SP'),('article','endPage','sakai:hasOrder','9'),('article','endPage','sakai:required','false'),('article','endPage','sakai:minCardinality','0'),('article','endPage','sakai:maxCardinality','1'),('article','endPage','sakai:valueType','number'),('article','endPage','sakai:ris_identifier','EP'),('article','abstract','sakai:hasOrder','10'),('article','abstract','sakai:required','false'),('article','abstract','sakai:minCardinality','0'),('article','abstract','sakai:maxCardinality','1'),('article','abstract','sakai:valueType','longtext'),('article','abstract','sakai:ris_identifier','N2,AB'),('article','note','sakai:hasOrder','11'),('article','note','sakai:required','false'),('article','note','sakai:minCardinality','0'),('article','note','sakai:maxCardinality','2147483647'),('article','note','sakai:valueType','shorttext'),('article','note','sakai:ris_identifier','N1,AB'),('article','isnIdentifier','sakai:hasOrder','12'),('article','isnIdentifier','sakai:required','false'),('article','isnIdentifier','sakai:minCardinality','0'),('article','isnIdentifier','sakai:maxCardinality','1'),('article','isnIdentifier','sakai:valueType','shorttext'),('article','isnIdentifier','sakai:ris_identifier','SN'),('article','subject','sakai:hasOrder','13'),('article','subject','sakai:required','false'),('article','subject','sakai:minCardinality','0'),('article','subject','sakai:maxCardinality','2147483647'),('article','subject','sakai:valueType','shorttext'),('article','subject','sakai:ris_identifier','KW'),('article','Language','sakai:hasOrder','14'),('article','Language','sakai:required','false'),('article','Language','sakai:minCardinality','0'),('article','Language','sakai:maxCardinality','1'),('article','Language','sakai:valueType','number'),('article','locIdentifier','sakai:hasOrder','15'),('article','locIdentifier','sakai:required','false'),('article','locIdentifier','sakai:minCardinality','0'),('article','locIdentifier','sakai:maxCardinality','1'),('article','locIdentifier','sakai:valueType','shorttext'),('article','locIdentifier','sakai:ris_identifier','M1'),('article','dateRetrieved','sakai:hasOrder','16'),('article','dateRetrieved','sakai:required','false'),('article','dateRetrieved','sakai:minCardinality','0'),('article','dateRetrieved','sakai:maxCardinality','1'),('article','dateRetrieved','sakai:valueType','date'),('article','openURL','sakai:hasOrder','17'),('article','openURL','sakai:required','false'),('article','openURL','sakai:minCardinality','0'),('article','openURL','sakai:maxCardinality','1'),('article','openURL','sakai:valueType','shorttext'),('article','doi','sakai:hasOrder','18'),('article','doi','sakai:required','false'),('article','doi','sakai:minCardinality','0'),('article','doi','sakai:maxCardinality','1'),('article','doi','sakai:valueType','number'),('article','rights','sakai:hasOrder','19'),('article','rights','sakai:required','false'),('article','rights','sakai:minCardinality','0'),('article','rights','sakai:maxCardinality','2147483647'),('article','rights','sakai:valueType','shorttext'),('book','creator','sakai:hasOrder','0'),('book','creator','sakai:required','true'),('book','creator','sakai:minCardinality','1'),('book','creator','sakai:maxCardinality','2147483647'),('book','creator','sakai:valueType','shorttext'),('book','creator','sakai:ris_identifier','A1,AU'),('book','title','sakai:hasOrder','1'),('book','title','sakai:required','true'),('book','title','sakai:minCardinality','1'),('book','title','sakai:maxCardinality','1'),('book','title','sakai:valueType','shorttext'),('book','title','sakai:ris_identifier','BT,T1,TI'),('book','year','sakai:hasOrder','2'),('book','year','sakai:required','false'),('book','year','sakai:minCardinality','0'),('book','year','sakai:maxCardinality','1'),('book','year','sakai:valueType','number'),('book','date','sakai:hasOrder','3'),('book','date','sakai:required','false'),('book','date','sakai:minCardinality','0'),('book','date','sakai:maxCardinality','1'),('book','date','sakai:valueType','number'),('book','date','sakai:ris_identifier','Y1,PY'),('book','publisher','sakai:hasOrder','4'),('book','publisher','sakai:required','false'),('book','publisher','sakai:minCardinality','0'),('book','publisher','sakai:maxCardinality','1'),('book','publisher','sakai:valueType','shorttext'),('book','publisher','sakai:ris_identifier','PB'),('book','publicationLocation','sakai:hasOrder','5'),('book','publicationLocation','sakai:required','false'),('book','publicationLocation','sakai:minCardinality','0'),('book','publicationLocation','sakai:maxCardinality','1'),('book','publicationLocation','sakai:valueType','shorttext'),('book','publicationLocation','sakai:ris_identifier','CY'),('book','edition','sakai:hasOrder','6'),('book','edition','sakai:required','false'),('book','edition','sakai:minCardinality','0'),('book','edition','sakai:maxCardinality','1'),('book','edition','sakai:valueType','number'),('book','edition','sakai:ris_identifier','VL'),('book','editor','sakai:hasOrder','7'),('book','editor','sakai:required','false'),('book','editor','sakai:minCardinality','0'),('book','editor','sakai:maxCardinality','2147483647'),('book','editor','sakai:valueType','shorttext'),('book','editor','sakai:ris_identifier','ED,A2,A3'),('book','sourceTitle','sakai:hasOrder','8'),('book','sourceTitle','sakai:required','false'),('book','sourceTitle','sakai:minCardinality','0'),('book','sourceTitle','sakai:maxCardinality','1'),('book','sourceTitle','sakai:valueType','shorttext'),('book','sourceTitle','sakai:ris_identifier','T3'),('book','abstract','sakai:hasOrder','9'),('book','abstract','sakai:required','false'),('book','abstract','sakai:minCardinality','0'),('book','abstract','sakai:maxCardinality','1'),('book','abstract','sakai:valueType','longtext'),('book','abstract','sakai:ris_identifier','N2,AB'),('book','note','sakai:hasOrder','10'),('book','note','sakai:required','false'),('book','note','sakai:minCardinality','0'),('book','note','sakai:maxCardinality','2147483647'),('book','note','sakai:valueType','shorttext'),('book','note','sakai:ris_identifier','N1,AB'),('book','isnIdentifier','sakai:hasOrder','11'),('book','isnIdentifier','sakai:required','false'),('book','isnIdentifier','sakai:minCardinality','0'),('book','isnIdentifier','sakai:maxCardinality','1'),('book','isnIdentifier','sakai:valueType','shorttext'),('book','isnIdentifier','sakai:ris_identifier','SN'),('book','subject','sakai:hasOrder','12'),('book','subject','sakai:required','false'),('book','subject','sakai:minCardinality','0'),('book','subject','sakai:maxCardinality','2147483647'),('book','subject','sakai:valueType','shorttext'),('book','subject','sakai:ris_identifier','KW'),('book','Language','sakai:hasOrder','13'),('book','Language','sakai:required','false'),('book','Language','sakai:minCardinality','0'),('book','Language','sakai:maxCardinality','1'),('book','Language','sakai:valueType','number'),('book','locIdentifier','sakai:hasOrder','14'),('book','locIdentifier','sakai:required','false'),('book','locIdentifier','sakai:minCardinality','0'),('book','locIdentifier','sakai:maxCardinality','1'),('book','locIdentifier','sakai:valueType','shorttext'),('book','locIdentifier','sakai:ris_identifier','M1'),('book','dateRetrieved','sakai:hasOrder','15'),('book','dateRetrieved','sakai:required','false'),('book','dateRetrieved','sakai:minCardinality','0'),('book','dateRetrieved','sakai:maxCardinality','1'),('book','dateRetrieved','sakai:valueType','date'),('book','openURL','sakai:hasOrder','16'),('book','openURL','sakai:required','false'),('book','openURL','sakai:minCardinality','0'),('book','openURL','sakai:maxCardinality','1'),('book','openURL','sakai:valueType','shorttext'),('book','doi','sakai:hasOrder','17'),('book','doi','sakai:required','false'),('book','doi','sakai:minCardinality','0'),('book','doi','sakai:maxCardinality','1'),('book','doi','sakai:valueType','number'),('book','rights','sakai:hasOrder','18'),('book','rights','sakai:required','false'),('book','rights','sakai:minCardinality','0'),('book','rights','sakai:maxCardinality','2147483647'),('book','rights','sakai:valueType','shorttext'),('chapter','creator','sakai:hasOrder','0'),('chapter','creator','sakai:required','true'),('chapter','creator','sakai:minCardinality','1'),('chapter','creator','sakai:maxCardinality','2147483647'),('chapter','creator','sakai:valueType','shorttext'),('chapter','creator','sakai:ris_identifier','A1,AU'),('chapter','title','sakai:hasOrder','1'),('chapter','title','sakai:required','true'),('chapter','title','sakai:minCardinality','1'),('chapter','title','sakai:maxCardinality','1'),('chapter','title','sakai:valueType','shorttext'),('chapter','title','sakai:ris_identifier','CT,T1,TI'),('chapter','year','sakai:hasOrder','2'),('chapter','year','sakai:required','false'),('chapter','year','sakai:minCardinality','0'),('chapter','year','sakai:maxCardinality','1'),('chapter','year','sakai:valueType','number'),('chapter','date','sakai:hasOrder','3'),('chapter','date','sakai:required','false'),('chapter','date','sakai:minCardinality','0'),('chapter','date','sakai:maxCardinality','1'),('chapter','date','sakai:valueType','number'),('chapter','date','sakai:ris_identifier','Y1,PY'),('chapter','publisher','sakai:hasOrder','4'),('chapter','publisher','sakai:required','false'),('chapter','publisher','sakai:minCardinality','0'),('chapter','publisher','sakai:maxCardinality','1'),('chapter','publisher','sakai:valueType','shorttext'),('chapter','publisher','sakai:ris_identifier','PB'),('chapter','publicationLocation','sakai:hasOrder','5'),('chapter','publicationLocation','sakai:required','false'),('chapter','publicationLocation','sakai:minCardinality','0'),('chapter','publicationLocation','sakai:maxCardinality','1'),('chapter','publicationLocation','sakai:valueType','shorttext'),('chapter','publicationLocation','sakai:ris_identifier','CY'),('chapter','edition','sakai:hasOrder','6'),('chapter','edition','sakai:required','false'),('chapter','edition','sakai:minCardinality','0'),('chapter','edition','sakai:maxCardinality','1'),('chapter','edition','sakai:valueType','number'),('chapter','edition','sakai:ris_identifier','VL'),('chapter','editor','sakai:hasOrder','7'),('chapter','editor','sakai:required','false'),('chapter','editor','sakai:minCardinality','0'),('chapter','editor','sakai:maxCardinality','2147483647'),('chapter','editor','sakai:valueType','shorttext'),('chapter','editor','sakai:ris_identifier','ED,A2,A3'),('chapter','sourceTitle','sakai:hasOrder','8'),('chapter','sourceTitle','sakai:required','false'),('chapter','sourceTitle','sakai:minCardinality','0'),('chapter','sourceTitle','sakai:maxCardinality','1'),('chapter','sourceTitle','sakai:valueType','shorttext'),('chapter','sourceTitle','sakai:ris_identifier','BT'),('chapter','pages','sakai:hasOrder','9'),('chapter','pages','sakai:required','false'),('chapter','pages','sakai:minCardinality','0'),('chapter','pages','sakai:maxCardinality','1'),('chapter','pages','sakai:valueType','number'),('chapter','pages','sakai:ris_identifier','SP'),('chapter','startPage','sakai:hasOrder','10'),('chapter','startPage','sakai:required','false'),('chapter','startPage','sakai:minCardinality','0'),('chapter','startPage','sakai:maxCardinality','1'),('chapter','startPage','sakai:valueType','number'),('chapter','startPage','sakai:ris_identifier','SP'),('chapter','endPage','sakai:hasOrder','11'),('chapter','endPage','sakai:required','false'),('chapter','endPage','sakai:minCardinality','0'),('chapter','endPage','sakai:maxCardinality','1'),('chapter','endPage','sakai:valueType','number'),('chapter','endPage','sakai:ris_identifier','EP'),('chapter','abstract','sakai:hasOrder','12'),('chapter','abstract','sakai:required','false'),('chapter','abstract','sakai:minCardinality','0'),('chapter','abstract','sakai:maxCardinality','1'),('chapter','abstract','sakai:valueType','longtext'),('chapter','abstract','sakai:ris_identifier','N2,AB'),('chapter','note','sakai:hasOrder','13'),('chapter','note','sakai:required','false'),('chapter','note','sakai:minCardinality','0'),('chapter','note','sakai:maxCardinality','2147483647'),('chapter','note','sakai:valueType','shorttext'),('chapter','note','sakai:ris_identifier','N1,AB'),('chapter','isnIdentifier','sakai:hasOrder','14'),('chapter','isnIdentifier','sakai:required','false'),('chapter','isnIdentifier','sakai:minCardinality','0'),('chapter','isnIdentifier','sakai:maxCardinality','1'),('chapter','isnIdentifier','sakai:valueType','shorttext'),('chapter','isnIdentifier','sakai:ris_identifier','SN'),('chapter','subject','sakai:hasOrder','15'),('chapter','subject','sakai:required','false'),('chapter','subject','sakai:minCardinality','0'),('chapter','subject','sakai:maxCardinality','2147483647'),('chapter','subject','sakai:valueType','shorttext'),('chapter','subject','sakai:ris_identifier','KW'),('chapter','Language','sakai:hasOrder','16'),('chapter','Language','sakai:required','false'),('chapter','Language','sakai:minCardinality','0'),('chapter','Language','sakai:maxCardinality','1'),('chapter','Language','sakai:valueType','number'),('chapter','locIdentifier','sakai:hasOrder','17'),('chapter','locIdentifier','sakai:required','false'),('chapter','locIdentifier','sakai:minCardinality','0'),('chapter','locIdentifier','sakai:maxCardinality','1'),('chapter','locIdentifier','sakai:valueType','shorttext'),('chapter','locIdentifier','sakai:ris_identifier','M1'),('chapter','dateRetrieved','sakai:hasOrder','18'),('chapter','dateRetrieved','sakai:required','false'),('chapter','dateRetrieved','sakai:minCardinality','0'),('chapter','dateRetrieved','sakai:maxCardinality','1'),('chapter','dateRetrieved','sakai:valueType','date'),('chapter','openURL','sakai:hasOrder','19'),('chapter','openURL','sakai:required','false'),('chapter','openURL','sakai:minCardinality','0'),('chapter','openURL','sakai:maxCardinality','1'),('chapter','openURL','sakai:valueType','shorttext'),('chapter','doi','sakai:hasOrder','20'),('chapter','doi','sakai:required','false'),('chapter','doi','sakai:minCardinality','0'),('chapter','doi','sakai:maxCardinality','1'),('chapter','doi','sakai:valueType','number'),('chapter','rights','sakai:hasOrder','21'),('chapter','rights','sakai:required','false'),('chapter','rights','sakai:minCardinality','0'),('chapter','rights','sakai:maxCardinality','2147483647'),('chapter','rights','sakai:valueType','shorttext'),('report','creator','sakai:hasOrder','0'),('report','creator','sakai:required','true'),('report','creator','sakai:minCardinality','1'),('report','creator','sakai:maxCardinality','2147483647'),('report','creator','sakai:valueType','shorttext'),('report','creator','sakai:ris_identifier','A1,AU'),('report','title','sakai:hasOrder','1'),('report','title','sakai:required','true'),('report','title','sakai:minCardinality','1'),('report','title','sakai:maxCardinality','1'),('report','title','sakai:valueType','shorttext'),('report','title','sakai:ris_identifier','T1,TI'),('report','year','sakai:hasOrder','2'),('report','year','sakai:required','false'),('report','year','sakai:minCardinality','0'),('report','year','sakai:maxCardinality','1'),('report','year','sakai:valueType','number'),('report','date','sakai:hasOrder','3'),('report','date','sakai:required','false'),('report','date','sakai:minCardinality','0'),('report','date','sakai:maxCardinality','1'),('report','date','sakai:valueType','number'),('report','date','sakai:ris_identifier','Y1,PY'),('report','publisher','sakai:hasOrder','4'),('report','publisher','sakai:required','false'),('report','publisher','sakai:minCardinality','0'),('report','publisher','sakai:maxCardinality','1'),('report','publisher','sakai:valueType','shorttext'),('report','publisher','sakai:ris_identifier','PB'),('report','publicationLocation','sakai:hasOrder','5'),('report','publicationLocation','sakai:required','false'),('report','publicationLocation','sakai:minCardinality','0'),('report','publicationLocation','sakai:maxCardinality','1'),('report','publicationLocation','sakai:valueType','shorttext'),('report','publicationLocation','sakai:ris_identifier','CY'),('report','editor','sakai:hasOrder','6'),('report','editor','sakai:required','false'),('report','editor','sakai:minCardinality','0'),('report','editor','sakai:maxCardinality','2147483647'),('report','editor','sakai:valueType','shorttext'),('report','editor','sakai:ris_identifier','ED,A2,A3'),('report','edition','sakai:hasOrder','7'),('report','edition','sakai:required','false'),('report','edition','sakai:minCardinality','0'),('report','edition','sakai:maxCardinality','1'),('report','edition','sakai:valueType','number'),('report','edition','sakai:ris_identifier','VL'),('report','sourceTitle','sakai:hasOrder','8'),('report','sourceTitle','sakai:required','false'),('report','sourceTitle','sakai:minCardinality','0'),('report','sourceTitle','sakai:maxCardinality','1'),('report','sourceTitle','sakai:valueType','shorttext'),('report','sourceTitle','sakai:ris_identifier','T3'),('report','pages','sakai:hasOrder','9'),('report','pages','sakai:required','false'),('report','pages','sakai:minCardinality','0'),('report','pages','sakai:maxCardinality','1'),('report','pages','sakai:valueType','number'),('report','pages','sakai:ris_identifier','SP'),('report','abstract','sakai:hasOrder','10'),('report','abstract','sakai:required','false'),('report','abstract','sakai:minCardinality','0'),('report','abstract','sakai:maxCardinality','1'),('report','abstract','sakai:valueType','longtext'),('report','abstract','sakai:ris_identifier','N2,AB'),('report','isnIdentifier','sakai:hasOrder','11'),('report','isnIdentifier','sakai:required','false'),('report','isnIdentifier','sakai:minCardinality','0'),('report','isnIdentifier','sakai:maxCardinality','1'),('report','isnIdentifier','sakai:valueType','shorttext'),('report','isnIdentifier','sakai:ris_identifier','SN'),('report','note','sakai:hasOrder','12'),('report','note','sakai:required','false'),('report','note','sakai:minCardinality','0'),('report','note','sakai:maxCardinality','2147483647'),('report','note','sakai:valueType','shorttext'),('report','note','sakai:ris_identifier','N1,AB'),('report','subject','sakai:hasOrder','13'),('report','subject','sakai:required','false'),('report','subject','sakai:minCardinality','0'),('report','subject','sakai:maxCardinality','2147483647'),('report','subject','sakai:valueType','shorttext'),('report','subject','sakai:ris_identifier','KW'),('report','Language','sakai:hasOrder','14'),('report','Language','sakai:required','false'),('report','Language','sakai:minCardinality','0'),('report','Language','sakai:maxCardinality','1'),('report','Language','sakai:valueType','number'),('report','locIdentifier','sakai:hasOrder','15'),('report','locIdentifier','sakai:required','false'),('report','locIdentifier','sakai:minCardinality','0'),('report','locIdentifier','sakai:maxCardinality','1'),('report','locIdentifier','sakai:valueType','shorttext'),('report','locIdentifier','sakai:ris_identifier','M1'),('report','dateRetrieved','sakai:hasOrder','16'),('report','dateRetrieved','sakai:required','false'),('report','dateRetrieved','sakai:minCardinality','0'),('report','dateRetrieved','sakai:maxCardinality','1'),('report','dateRetrieved','sakai:valueType','date'),('report','openURL','sakai:hasOrder','17'),('report','openURL','sakai:required','false'),('report','openURL','sakai:minCardinality','0'),('report','openURL','sakai:maxCardinality','1'),('report','openURL','sakai:valueType','shorttext'),('report','doi','sakai:hasOrder','18'),('report','doi','sakai:required','false'),('report','doi','sakai:minCardinality','0'),('report','doi','sakai:maxCardinality','1'),('report','doi','sakai:valueType','number'),('report','rights','sakai:hasOrder','19'),('report','rights','sakai:required','false'),('report','rights','sakai:minCardinality','0'),('report','rights','sakai:maxCardinality','2147483647'),('report','rights','sakai:valueType','shorttext'),('proceed','creator','sakai:hasOrder','0'),('proceed','creator','sakai:required','false'),('proceed','creator','sakai:minCardinality','0'),('proceed','creator','sakai:maxCardinality','2147483647'),('proceed','creator','sakai:valueType','shorttext'),('proceed','creator','sakai:ris_identifier','AU'),('proceed','title','sakai:hasOrder','1'),('proceed','title','sakai:required','true'),('proceed','title','sakai:minCardinality','1'),('proceed','title','sakai:maxCardinality','1'),('proceed','title','sakai:valueType','shorttext'),('proceed','title','sakai:ris_identifier','CT'),('proceed','year','sakai:hasOrder','2'),('proceed','year','sakai:required','false'),('proceed','year','sakai:minCardinality','0'),('proceed','year','sakai:maxCardinality','1'),('proceed','year','sakai:valueType','number'),('proceed','year','sakai:ris_identifier','PY'),('proceed','volume','sakai:hasOrder','3'),('proceed','volume','sakai:required','false'),('proceed','volume','sakai:minCardinality','0'),('proceed','volume','sakai:maxCardinality','1'),('proceed','volume','sakai:valueType','number'),('proceed','volume','sakai:ris_identifier','VL'),('proceed','pages','sakai:hasOrder','4'),('proceed','pages','sakai:required','false'),('proceed','pages','sakai:minCardinality','0'),('proceed','pages','sakai:maxCardinality','1'),('proceed','pages','sakai:valueType','number'),('proceed','pages','sakai:ris_identifier','SP'),('proceed','sourceTitle','sakai:hasOrder','5'),('proceed','sourceTitle','sakai:required','false'),('proceed','sourceTitle','sakai:minCardinality','0'),('proceed','sourceTitle','sakai:maxCardinality','1'),('proceed','sourceTitle','sakai:valueType','shorttext'),('proceed','sourceTitle','sakai:ris_identifier','BT'),('proceed','note','sakai:hasOrder','6'),('proceed','note','sakai:required','false'),('proceed','note','sakai:minCardinality','0'),('proceed','note','sakai:maxCardinality','2147483647'),('proceed','note','sakai:valueType','shorttext'),('proceed','note','sakai:ris_identifier','N1,AB'),('electronic','title','sakai:hasOrder','0'),('electronic','title','sakai:required','true'),('electronic','title','sakai:minCardinality','1'),('electronic','title','sakai:maxCardinality','1'),('electronic','title','sakai:valueType','shorttext'),('electronic','title','sakai:ris_identifier','CT'),('electronic','year','sakai:hasOrder','1'),('electronic','year','sakai:required','false'),('electronic','year','sakai:minCardinality','0'),('electronic','year','sakai:maxCardinality','1'),('electronic','year','sakai:valueType','number'),('electronic','year','sakai:ris_identifier','PY'),('electronic','sourceTitle','sakai:hasOrder','2'),('electronic','sourceTitle','sakai:required','false'),('electronic','sourceTitle','sakai:minCardinality','0'),('electronic','sourceTitle','sakai:maxCardinality','1'),('electronic','sourceTitle','sakai:valueType','shorttext'),('electronic','sourceTitle','sakai:ris_identifier','T3'),('electronic','abstract','sakai:hasOrder','3'),('electronic','abstract','sakai:required','false'),('electronic','abstract','sakai:minCardinality','0'),('electronic','abstract','sakai:maxCardinality','1'),('electronic','abstract','sakai:valueType','longtext'),('electronic','abstract','sakai:ris_identifier','N2,AB'),('electronic','subject','sakai:hasOrder','4'),('electronic','subject','sakai:required','false'),('electronic','subject','sakai:minCardinality','0'),('electronic','subject','sakai:maxCardinality','2147483647'),('electronic','subject','sakai:valueType','shorttext'),('electronic','subject','sakai:ris_identifier','KW'),('thesis','creator','sakai:hasOrder','0'),('thesis','creator','sakai:required','false'),('thesis','creator','sakai:minCardinality','0'),('thesis','creator','sakai:maxCardinality','2147483647'),('thesis','creator','sakai:valueType','shorttext'),('thesis','creator','sakai:ris_identifier','AU'),('thesis','title','sakai:hasOrder','1'),('thesis','title','sakai:required','true'),('thesis','title','sakai:minCardinality','1'),('thesis','title','sakai:maxCardinality','1'),('thesis','title','sakai:valueType','shorttext'),('thesis','title','sakai:ris_identifier','CT'),('thesis','year','sakai:hasOrder','2'),('thesis','year','sakai:required','false'),('thesis','year','sakai:minCardinality','0'),('thesis','year','sakai:maxCardinality','1'),('thesis','year','sakai:valueType','number'),('thesis','year','sakai:ris_identifier','PY'),('thesis','publisher','sakai:hasOrder','3'),('thesis','publisher','sakai:required','false'),('thesis','publisher','sakai:minCardinality','0'),('thesis','publisher','sakai:maxCardinality','1'),('thesis','publisher','sakai:valueType','shorttext'),('thesis','publisher','sakai:ris_identifier','PB'),('thesis','pages','sakai:hasOrder','4'),('thesis','pages','sakai:required','false'),('thesis','pages','sakai:minCardinality','0'),('thesis','pages','sakai:maxCardinality','1'),('thesis','pages','sakai:valueType','number'),('thesis','pages','sakai:ris_identifier','SP'),('thesis','note','sakai:hasOrder','5'),('thesis','note','sakai:required','false'),('thesis','note','sakai:minCardinality','0'),('thesis','note','sakai:maxCardinality','2147483647'),('thesis','note','sakai:valueType','shorttext'),('thesis','note','sakai:ris_identifier','N1,AB'),('thesis','subject','sakai:hasOrder','6'),('thesis','subject','sakai:required','false'),('thesis','subject','sakai:minCardinality','0'),('thesis','subject','sakai:maxCardinality','2147483647'),('thesis','subject','sakai:valueType','shorttext'),('thesis','subject','sakai:ris_identifier','KW');
/*!40000 ALTER TABLE `CITATION_SCHEMA_FIELD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CMN_TYPE_T`
--

DROP TABLE IF EXISTS `CMN_TYPE_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CMN_TYPE_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `UUID` varchar(36) NOT NULL,
  `LAST_MODIFIED_BY` varchar(36) NOT NULL,
  `LAST_MODIFIED_DATE` datetime NOT NULL,
  `CREATED_BY` varchar(36) NOT NULL,
  `CREATED_DATE` datetime NOT NULL,
  `AUTHORITY` varchar(100) NOT NULL,
  `DOMAIN` varchar(100) NOT NULL,
  `KEYWORD` varchar(100) NOT NULL,
  `DISPLAY_NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UUID` (`UUID`),
  UNIQUE KEY `AUTHORITY` (`AUTHORITY`,`DOMAIN`,`KEYWORD`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CMN_TYPE_T`
--

LOCK TABLES `CMN_TYPE_T` WRITE;
/*!40000 ALTER TABLE `CMN_TYPE_T` DISABLE KEYS */;
INSERT INTO `CMN_TYPE_T` VALUES (1,0,'6542f08e-68d3-429a-9dc8-1d32666f265b','SYSTEM','2011-04-13 14:59:27','SYSTEM','2011-04-13 14:59:27','org.sakaiproject.component.app.messageforums','sakai_messageforums','Owner Permission Level','Owner Permission Level','Owner Permission Level'),(2,0,'73201531-6fbc-446a-abc0-9f0bdf83c01f','SYSTEM','2011-04-13 14:59:27','SYSTEM','2011-04-13 14:59:27','org.sakaiproject.component.app.messageforums','sakai_messageforums','Author Permission Level','Author Permission Level','Author Permission Level'),(3,0,'22e7796d-f9e1-43bd-9f41-b9fcebaf6894','SYSTEM','2011-04-13 14:59:27','SYSTEM','2011-04-13 14:59:27','org.sakaiproject.component.app.messageforums','sakai_messageforums','Nonediting Author Permission Level','Nonediting Author Permission Level','Nonediting Author Permission Level'),(4,0,'b9865f7f-c1c8-467f-94f5-9e5f80ed9913','SYSTEM','2011-04-13 14:59:27','SYSTEM','2011-04-13 14:59:27','org.sakaiproject.component.app.messageforums','sakai_messageforums','Reviewer Permission Level','Reviewer Permission Level','Reviewer Permission Level'),(5,0,'1ca2d020-d95a-469d-bb50-40531fb6364b','SYSTEM','2011-04-13 14:59:27','SYSTEM','2011-04-13 14:59:27','org.sakaiproject.component.app.messageforums','sakai_messageforums','Contributor Permission Level','Contributor Permission Level','Contributor Permission Level'),(6,0,'8b9402a8-0538-486e-bb2f-7e3f9ba051aa','SYSTEM','2011-04-13 14:59:27','SYSTEM','2011-04-13 14:59:27','org.sakaiproject.component.app.messageforums','sakai_messageforums','None Permission Level','None Permission Level','None Permission Level'),(7,0,'3c017b67-362d-44cc-b0bc-5c0d5d61489f','SYSTEM','2011-04-13 14:59:27','SYSTEM','2011-04-13 14:59:27','org.sakaiproject.component.app.messageforums','sakai_messageforums','Custom Permission Level','Custom Permission Level','Custom Permission Level'),(8,0,'2ecd65c8-500b-4b12-96ab-3aa1c60bdc61','SYSTEM','2011-04-13 14:59:48','SYSTEM','2011-04-13 14:59:48','org.sakaiproject','api.common.edu.person','SakaiPerson.recordType.systemMutable','System Mutable SakaiPerson','System Mutable SakaiPerson'),(9,0,'6bec791d-f277-4d56-bdf9-694cf7bcc280','SYSTEM','2011-04-13 14:59:48','SYSTEM','2011-04-13 14:59:48','org.sakaiproject','api.common.edu.person','SakaiPerson.recordType.userMutable','User Mutable SakaiPerson','User Mutable SakaiPerson');
/*!40000 ALTER TABLE `CMN_TYPE_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CM_ACADEMIC_SESSION_T`
--

DROP TABLE IF EXISTS `CM_ACADEMIC_SESSION_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CM_ACADEMIC_SESSION_T` (
  `ACADEMIC_SESSION_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `LAST_MODIFIED_BY` varchar(255) DEFAULT NULL,
  `LAST_MODIFIED_DATE` date DEFAULT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` date DEFAULT NULL,
  `ENTERPRISE_ID` varchar(255) NOT NULL,
  `TITLE` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `START_DATE` date DEFAULT NULL,
  `END_DATE` date DEFAULT NULL,
  `IS_CURRENT` bit(1) NOT NULL,
  PRIMARY KEY (`ACADEMIC_SESSION_ID`),
  UNIQUE KEY `ENTERPRISE_ID` (`ENTERPRISE_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CM_ACADEMIC_SESSION_T`
--

LOCK TABLES `CM_ACADEMIC_SESSION_T` WRITE;
/*!40000 ALTER TABLE `CM_ACADEMIC_SESSION_T` DISABLE KEYS */;
INSERT INTO `CM_ACADEMIC_SESSION_T` VALUES (1,0,NULL,NULL,'admin','2011-04-13','Winter 2011','Winter 2011','Winter 2011','2011-01-01','2011-04-01','\0'),(2,1,NULL,NULL,'admin','2011-04-13','Spring 2011','Spring 2011','Spring 2011','2011-04-01','2011-06-01',''),(3,1,NULL,NULL,'admin','2011-04-13','Summer 2011','Summer 2011','Summer 2011','2011-06-01','2011-09-01',''),(4,0,NULL,NULL,'admin','2011-04-13','Fall 2011','Fall 2011','Fall 2011','2011-09-01','2012-01-01','\0');
/*!40000 ALTER TABLE `CM_ACADEMIC_SESSION_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CM_COURSE_SET_CANON_ASSOC_T`
--

DROP TABLE IF EXISTS `CM_COURSE_SET_CANON_ASSOC_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CM_COURSE_SET_CANON_ASSOC_T` (
  `CANON_COURSE` bigint(20) NOT NULL,
  `COURSE_SET` bigint(20) NOT NULL,
  PRIMARY KEY (`COURSE_SET`,`CANON_COURSE`),
  KEY `FKBFCBD9AE2D306E01` (`COURSE_SET`),
  KEY `FKBFCBD9AE7F976CD6` (`CANON_COURSE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CM_COURSE_SET_CANON_ASSOC_T`
--

LOCK TABLES `CM_COURSE_SET_CANON_ASSOC_T` WRITE;
/*!40000 ALTER TABLE `CM_COURSE_SET_CANON_ASSOC_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `CM_COURSE_SET_CANON_ASSOC_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CM_COURSE_SET_OFFERING_ASSOC_T`
--

DROP TABLE IF EXISTS `CM_COURSE_SET_OFFERING_ASSOC_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CM_COURSE_SET_OFFERING_ASSOC_T` (
  `COURSE_OFFERING` bigint(20) NOT NULL,
  `COURSE_SET` bigint(20) NOT NULL,
  PRIMARY KEY (`COURSE_SET`,`COURSE_OFFERING`),
  KEY `FK5B9A5CFD26827043` (`COURSE_OFFERING`),
  KEY `FK5B9A5CFD2D306E01` (`COURSE_SET`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CM_COURSE_SET_OFFERING_ASSOC_T`
--

LOCK TABLES `CM_COURSE_SET_OFFERING_ASSOC_T` WRITE;
/*!40000 ALTER TABLE `CM_COURSE_SET_OFFERING_ASSOC_T` DISABLE KEYS */;
INSERT INTO `CM_COURSE_SET_OFFERING_ASSOC_T` VALUES (4,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1);
/*!40000 ALTER TABLE `CM_COURSE_SET_OFFERING_ASSOC_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CM_CROSS_LISTING_T`
--

DROP TABLE IF EXISTS `CM_CROSS_LISTING_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CM_CROSS_LISTING_T` (
  `CROSS_LISTING_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `LAST_MODIFIED_BY` varchar(255) DEFAULT NULL,
  `LAST_MODIFIED_DATE` date DEFAULT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` date DEFAULT NULL,
  PRIMARY KEY (`CROSS_LISTING_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CM_CROSS_LISTING_T`
--

LOCK TABLES `CM_CROSS_LISTING_T` WRITE;
/*!40000 ALTER TABLE `CM_CROSS_LISTING_T` DISABLE KEYS */;
INSERT INTO `CM_CROSS_LISTING_T` VALUES (1,0,NULL,NULL,'admin','2011-04-13'),(2,0,NULL,NULL,'admin','2011-04-13'),(3,0,NULL,NULL,'admin','2011-04-13'),(4,0,NULL,NULL,'admin','2011-04-13'),(5,0,NULL,NULL,'admin','2011-04-13');
/*!40000 ALTER TABLE `CM_CROSS_LISTING_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CM_ENROLLMENT_SET_T`
--

DROP TABLE IF EXISTS `CM_ENROLLMENT_SET_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CM_ENROLLMENT_SET_T` (
  `ENROLLMENT_SET_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `LAST_MODIFIED_BY` varchar(255) DEFAULT NULL,
  `LAST_MODIFIED_DATE` date DEFAULT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` date DEFAULT NULL,
  `ENTERPRISE_ID` varchar(255) NOT NULL,
  `TITLE` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `CATEGORY` varchar(255) NOT NULL,
  `DEFAULT_CREDITS` varchar(255) NOT NULL,
  `COURSE_OFFERING` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ENROLLMENT_SET_ID`),
  UNIQUE KEY `ENTERPRISE_ID` (`ENTERPRISE_ID`),
  KEY `FK99479DD126827043` (`COURSE_OFFERING`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CM_ENROLLMENT_SET_T`
--

LOCK TABLES `CM_ENROLLMENT_SET_T` WRITE;
/*!40000 ALTER TABLE `CM_ENROLLMENT_SET_T` DISABLE KEYS */;
INSERT INTO `CM_ENROLLMENT_SET_T` VALUES (1,0,NULL,NULL,'admin','2011-04-13','SMPL101 Winter 2011es','SMPL101 Enrollment Set','Sample course offering #1, Winter 2011 Enrollment Set','lecture','3',4),(2,0,NULL,NULL,'admin','2011-04-13','SMPL202 Winter 2011es','SMPL202 Enrollment Set','Sample course offering #2, Winter 2011 Enrollment Set','lecture','3',5),(3,0,NULL,NULL,'admin','2011-04-13','SMPL101 Spring 2011es','SMPL101 Enrollment Set','Sample course offering #1, Spring 2011 Enrollment Set','lecture','3',6),(4,0,NULL,NULL,'admin','2011-04-13','SMPL202 Spring 2011es','SMPL202 Enrollment Set','Sample course offering #2, Spring 2011 Enrollment Set','lecture','3',7),(5,0,NULL,NULL,'admin','2011-04-13','SMPL101 Summer 2011es','SMPL101 Enrollment Set','Sample course offering #1, Summer 2011 Enrollment Set','lecture','3',8),(6,0,NULL,NULL,'admin','2011-04-13','SMPL202 Summer 2011es','SMPL202 Enrollment Set','Sample course offering #2, Summer 2011 Enrollment Set','lecture','3',9),(7,0,NULL,NULL,'admin','2011-04-13','SMPL101 Fall 2011es','SMPL101 Enrollment Set','Sample course offering #1, Fall 2011 Enrollment Set','lecture','3',10),(8,0,NULL,NULL,'admin','2011-04-13','SMPL202 Fall 2011es','SMPL202 Enrollment Set','Sample course offering #2, Fall 2011 Enrollment Set','lecture','3',11);
/*!40000 ALTER TABLE `CM_ENROLLMENT_SET_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CM_ENROLLMENT_T`
--

DROP TABLE IF EXISTS `CM_ENROLLMENT_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CM_ENROLLMENT_T` (
  `ENROLLMENT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `LAST_MODIFIED_BY` varchar(255) DEFAULT NULL,
  `LAST_MODIFIED_DATE` date DEFAULT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` date DEFAULT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `STATUS` varchar(255) NOT NULL,
  `CREDITS` varchar(255) NOT NULL,
  `GRADING_SCHEME` varchar(255) NOT NULL,
  `DROPPED` bit(1) DEFAULT NULL,
  `ENROLLMENT_SET` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ENROLLMENT_ID`),
  UNIQUE KEY `USER_ID` (`USER_ID`,`ENROLLMENT_SET`),
  KEY `FK7A7F878E456D3EA1` (`ENROLLMENT_SET`)
) ENGINE=MyISAM AUTO_INCREMENT=1441 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CM_ENROLLMENT_T`
--

LOCK TABLES `CM_ENROLLMENT_T` WRITE;
/*!40000 ALTER TABLE `CM_ENROLLMENT_T` DISABLE KEYS */;
INSERT INTO `CM_ENROLLMENT_T` VALUES (1,0,NULL,NULL,'admin','2011-04-13','student0001','wait','3','pnp','\0',1),(2,0,NULL,NULL,'admin','2011-04-13','student0002','enrolled','3','standard','\0',1),(3,0,NULL,NULL,'admin','2011-04-13','student0003','wait','3','pnp','\0',1),(4,0,NULL,NULL,'admin','2011-04-13','student0004','enrolled','3','standard','\0',1),(5,0,NULL,NULL,'admin','2011-04-13','student0005','wait','3','pnp','\0',1),(6,0,NULL,NULL,'admin','2011-04-13','student0006','enrolled','3','standard','\0',1),(7,0,NULL,NULL,'admin','2011-04-13','student0007','wait','3','pnp','\0',1),(8,0,NULL,NULL,'admin','2011-04-13','student0008','enrolled','3','standard','\0',1),(9,0,NULL,NULL,'admin','2011-04-13','student0009','wait','3','pnp','\0',1),(10,0,NULL,NULL,'admin','2011-04-13','student0010','enrolled','3','standard','\0',1),(11,0,NULL,NULL,'admin','2011-04-13','student0011','wait','3','pnp','\0',1),(12,0,NULL,NULL,'admin','2011-04-13','student0012','enrolled','3','standard','\0',1),(13,0,NULL,NULL,'admin','2011-04-13','student0013','wait','3','pnp','\0',1),(14,0,NULL,NULL,'admin','2011-04-13','student0014','enrolled','3','standard','\0',1),(15,0,NULL,NULL,'admin','2011-04-13','student0015','wait','3','pnp','\0',1),(16,0,NULL,NULL,'admin','2011-04-13','student0016','enrolled','3','standard','\0',1),(17,0,NULL,NULL,'admin','2011-04-13','student0017','wait','3','pnp','\0',1),(18,0,NULL,NULL,'admin','2011-04-13','student0018','enrolled','3','standard','\0',1),(19,0,NULL,NULL,'admin','2011-04-13','student0019','wait','3','pnp','\0',1),(20,0,NULL,NULL,'admin','2011-04-13','student0020','enrolled','3','standard','\0',1),(21,0,NULL,NULL,'admin','2011-04-13','student0021','wait','3','pnp','\0',1),(22,0,NULL,NULL,'admin','2011-04-13','student0022','enrolled','3','standard','\0',1),(23,0,NULL,NULL,'admin','2011-04-13','student0023','wait','3','pnp','\0',1),(24,0,NULL,NULL,'admin','2011-04-13','student0024','enrolled','3','standard','\0',1),(25,0,NULL,NULL,'admin','2011-04-13','student0025','wait','3','pnp','\0',1),(26,0,NULL,NULL,'admin','2011-04-13','student0026','enrolled','3','standard','\0',1),(27,0,NULL,NULL,'admin','2011-04-13','student0027','wait','3','pnp','\0',1),(28,0,NULL,NULL,'admin','2011-04-13','student0028','enrolled','3','standard','\0',1),(29,0,NULL,NULL,'admin','2011-04-13','student0029','wait','3','pnp','\0',1),(30,0,NULL,NULL,'admin','2011-04-13','student0030','enrolled','3','standard','\0',1),(31,0,NULL,NULL,'admin','2011-04-13','student0031','wait','3','pnp','\0',1),(32,0,NULL,NULL,'admin','2011-04-13','student0032','enrolled','3','standard','\0',1),(33,0,NULL,NULL,'admin','2011-04-13','student0033','wait','3','pnp','\0',1),(34,0,NULL,NULL,'admin','2011-04-13','student0034','enrolled','3','standard','\0',1),(35,0,NULL,NULL,'admin','2011-04-13','student0035','wait','3','pnp','\0',1),(36,0,NULL,NULL,'admin','2011-04-13','student0036','enrolled','3','standard','\0',1),(37,0,NULL,NULL,'admin','2011-04-13','student0037','wait','3','pnp','\0',1),(38,0,NULL,NULL,'admin','2011-04-13','student0038','enrolled','3','standard','\0',1),(39,0,NULL,NULL,'admin','2011-04-13','student0039','wait','3','pnp','\0',1),(40,0,NULL,NULL,'admin','2011-04-13','student0040','enrolled','3','standard','\0',1),(41,0,NULL,NULL,'admin','2011-04-13','student0041','wait','3','pnp','\0',1),(42,0,NULL,NULL,'admin','2011-04-13','student0042','enrolled','3','standard','\0',1),(43,0,NULL,NULL,'admin','2011-04-13','student0043','wait','3','pnp','\0',1),(44,0,NULL,NULL,'admin','2011-04-13','student0044','enrolled','3','standard','\0',1),(45,0,NULL,NULL,'admin','2011-04-13','student0045','wait','3','pnp','\0',1),(46,0,NULL,NULL,'admin','2011-04-13','student0046','enrolled','3','standard','\0',1),(47,0,NULL,NULL,'admin','2011-04-13','student0047','wait','3','pnp','\0',1),(48,0,NULL,NULL,'admin','2011-04-13','student0048','enrolled','3','standard','\0',1),(49,0,NULL,NULL,'admin','2011-04-13','student0049','wait','3','pnp','\0',1),(50,0,NULL,NULL,'admin','2011-04-13','student0050','enrolled','3','standard','\0',1),(51,0,NULL,NULL,'admin','2011-04-13','student0051','wait','3','pnp','\0',1),(52,0,NULL,NULL,'admin','2011-04-13','student0052','enrolled','3','standard','\0',1),(53,0,NULL,NULL,'admin','2011-04-13','student0053','wait','3','pnp','\0',1),(54,0,NULL,NULL,'admin','2011-04-13','student0054','enrolled','3','standard','\0',1),(55,0,NULL,NULL,'admin','2011-04-13','student0055','wait','3','pnp','\0',1),(56,0,NULL,NULL,'admin','2011-04-13','student0056','enrolled','3','standard','\0',1),(57,0,NULL,NULL,'admin','2011-04-13','student0057','wait','3','pnp','\0',1),(58,0,NULL,NULL,'admin','2011-04-13','student0058','enrolled','3','standard','\0',1),(59,0,NULL,NULL,'admin','2011-04-13','student0059','wait','3','pnp','\0',1),(60,0,NULL,NULL,'admin','2011-04-13','student0060','enrolled','3','standard','\0',1),(61,0,NULL,NULL,'admin','2011-04-13','student0061','wait','3','pnp','\0',1),(62,0,NULL,NULL,'admin','2011-04-13','student0062','enrolled','3','standard','\0',1),(63,0,NULL,NULL,'admin','2011-04-13','student0063','wait','3','pnp','\0',1),(64,0,NULL,NULL,'admin','2011-04-13','student0064','enrolled','3','standard','\0',1),(65,0,NULL,NULL,'admin','2011-04-13','student0065','wait','3','pnp','\0',1),(66,0,NULL,NULL,'admin','2011-04-13','student0066','enrolled','3','standard','\0',1),(67,0,NULL,NULL,'admin','2011-04-13','student0067','wait','3','pnp','\0',1),(68,0,NULL,NULL,'admin','2011-04-13','student0068','enrolled','3','standard','\0',1),(69,0,NULL,NULL,'admin','2011-04-13','student0069','wait','3','pnp','\0',1),(70,0,NULL,NULL,'admin','2011-04-13','student0070','enrolled','3','standard','\0',1),(71,0,NULL,NULL,'admin','2011-04-13','student0071','wait','3','pnp','\0',1),(72,0,NULL,NULL,'admin','2011-04-13','student0072','enrolled','3','standard','\0',1),(73,0,NULL,NULL,'admin','2011-04-13','student0073','wait','3','pnp','\0',1),(74,0,NULL,NULL,'admin','2011-04-13','student0074','enrolled','3','standard','\0',1),(75,0,NULL,NULL,'admin','2011-04-13','student0075','wait','3','pnp','\0',1),(76,0,NULL,NULL,'admin','2011-04-13','student0076','enrolled','3','standard','\0',1),(77,0,NULL,NULL,'admin','2011-04-13','student0077','wait','3','pnp','\0',1),(78,0,NULL,NULL,'admin','2011-04-13','student0078','enrolled','3','standard','\0',1),(79,0,NULL,NULL,'admin','2011-04-13','student0079','wait','3','pnp','\0',1),(80,0,NULL,NULL,'admin','2011-04-13','student0080','enrolled','3','standard','\0',1),(81,0,NULL,NULL,'admin','2011-04-13','student0081','wait','3','pnp','\0',1),(82,0,NULL,NULL,'admin','2011-04-13','student0082','enrolled','3','standard','\0',1),(83,0,NULL,NULL,'admin','2011-04-13','student0083','wait','3','pnp','\0',1),(84,0,NULL,NULL,'admin','2011-04-13','student0084','enrolled','3','standard','\0',1),(85,0,NULL,NULL,'admin','2011-04-13','student0085','wait','3','pnp','\0',1),(86,0,NULL,NULL,'admin','2011-04-13','student0086','enrolled','3','standard','\0',1),(87,0,NULL,NULL,'admin','2011-04-13','student0087','wait','3','pnp','\0',1),(88,0,NULL,NULL,'admin','2011-04-13','student0088','enrolled','3','standard','\0',1),(89,0,NULL,NULL,'admin','2011-04-13','student0089','wait','3','pnp','\0',1),(90,0,NULL,NULL,'admin','2011-04-13','student0090','enrolled','3','standard','\0',1),(91,0,NULL,NULL,'admin','2011-04-13','student0091','wait','3','pnp','\0',1),(92,0,NULL,NULL,'admin','2011-04-13','student0092','enrolled','3','standard','\0',1),(93,0,NULL,NULL,'admin','2011-04-13','student0093','wait','3','pnp','\0',1),(94,0,NULL,NULL,'admin','2011-04-13','student0094','enrolled','3','standard','\0',1),(95,0,NULL,NULL,'admin','2011-04-13','student0095','wait','3','pnp','\0',1),(96,0,NULL,NULL,'admin','2011-04-13','student0096','enrolled','3','standard','\0',1),(97,0,NULL,NULL,'admin','2011-04-13','student0097','wait','3','pnp','\0',1),(98,0,NULL,NULL,'admin','2011-04-13','student0098','enrolled','3','standard','\0',1),(99,0,NULL,NULL,'admin','2011-04-13','student0099','wait','3','pnp','\0',1),(100,0,NULL,NULL,'admin','2011-04-13','student0100','enrolled','3','standard','\0',1),(101,0,NULL,NULL,'admin','2011-04-13','student0101','wait','3','pnp','\0',1),(102,0,NULL,NULL,'admin','2011-04-13','student0102','enrolled','3','standard','\0',1),(103,0,NULL,NULL,'admin','2011-04-13','student0103','wait','3','pnp','\0',1),(104,0,NULL,NULL,'admin','2011-04-13','student0104','enrolled','3','standard','\0',1),(105,0,NULL,NULL,'admin','2011-04-13','student0105','wait','3','pnp','\0',1),(106,0,NULL,NULL,'admin','2011-04-13','student0106','enrolled','3','standard','\0',1),(107,0,NULL,NULL,'admin','2011-04-13','student0107','wait','3','pnp','\0',1),(108,0,NULL,NULL,'admin','2011-04-13','student0108','enrolled','3','standard','\0',1),(109,0,NULL,NULL,'admin','2011-04-13','student0109','wait','3','pnp','\0',1),(110,0,NULL,NULL,'admin','2011-04-13','student0110','enrolled','3','standard','\0',1),(111,0,NULL,NULL,'admin','2011-04-13','student0111','wait','3','pnp','\0',1),(112,0,NULL,NULL,'admin','2011-04-13','student0112','enrolled','3','standard','\0',1),(113,0,NULL,NULL,'admin','2011-04-13','student0113','wait','3','pnp','\0',1),(114,0,NULL,NULL,'admin','2011-04-13','student0114','enrolled','3','standard','\0',1),(115,0,NULL,NULL,'admin','2011-04-13','student0115','wait','3','pnp','\0',1),(116,0,NULL,NULL,'admin','2011-04-13','student0116','enrolled','3','standard','\0',1),(117,0,NULL,NULL,'admin','2011-04-13','student0117','wait','3','pnp','\0',1),(118,0,NULL,NULL,'admin','2011-04-13','student0118','enrolled','3','standard','\0',1),(119,0,NULL,NULL,'admin','2011-04-13','student0119','wait','3','pnp','\0',1),(120,0,NULL,NULL,'admin','2011-04-13','student0120','enrolled','3','standard','\0',1),(121,0,NULL,NULL,'admin','2011-04-13','student0121','wait','3','pnp','\0',1),(122,0,NULL,NULL,'admin','2011-04-13','student0122','enrolled','3','standard','\0',1),(123,0,NULL,NULL,'admin','2011-04-13','student0123','wait','3','pnp','\0',1),(124,0,NULL,NULL,'admin','2011-04-13','student0124','enrolled','3','standard','\0',1),(125,0,NULL,NULL,'admin','2011-04-13','student0125','wait','3','pnp','\0',1),(126,0,NULL,NULL,'admin','2011-04-13','student0126','enrolled','3','standard','\0',1),(127,0,NULL,NULL,'admin','2011-04-13','student0127','wait','3','pnp','\0',1),(128,0,NULL,NULL,'admin','2011-04-13','student0128','enrolled','3','standard','\0',1),(129,0,NULL,NULL,'admin','2011-04-13','student0129','wait','3','pnp','\0',1),(130,0,NULL,NULL,'admin','2011-04-13','student0130','enrolled','3','standard','\0',1),(131,0,NULL,NULL,'admin','2011-04-13','student0131','wait','3','pnp','\0',1),(132,0,NULL,NULL,'admin','2011-04-13','student0132','enrolled','3','standard','\0',1),(133,0,NULL,NULL,'admin','2011-04-13','student0133','wait','3','pnp','\0',1),(134,0,NULL,NULL,'admin','2011-04-13','student0134','enrolled','3','standard','\0',1),(135,0,NULL,NULL,'admin','2011-04-13','student0135','wait','3','pnp','\0',1),(136,0,NULL,NULL,'admin','2011-04-13','student0136','enrolled','3','standard','\0',1),(137,0,NULL,NULL,'admin','2011-04-13','student0137','wait','3','pnp','\0',1),(138,0,NULL,NULL,'admin','2011-04-13','student0138','enrolled','3','standard','\0',1),(139,0,NULL,NULL,'admin','2011-04-13','student0139','wait','3','pnp','\0',1),(140,0,NULL,NULL,'admin','2011-04-13','student0140','enrolled','3','standard','\0',1),(141,0,NULL,NULL,'admin','2011-04-13','student0141','wait','3','pnp','\0',1),(142,0,NULL,NULL,'admin','2011-04-13','student0142','enrolled','3','standard','\0',1),(143,0,NULL,NULL,'admin','2011-04-13','student0143','wait','3','pnp','\0',1),(144,0,NULL,NULL,'admin','2011-04-13','student0144','enrolled','3','standard','\0',1),(145,0,NULL,NULL,'admin','2011-04-13','student0145','wait','3','pnp','\0',1),(146,0,NULL,NULL,'admin','2011-04-13','student0146','enrolled','3','standard','\0',1),(147,0,NULL,NULL,'admin','2011-04-13','student0147','wait','3','pnp','\0',1),(148,0,NULL,NULL,'admin','2011-04-13','student0148','enrolled','3','standard','\0',1),(149,0,NULL,NULL,'admin','2011-04-13','student0149','wait','3','pnp','\0',1),(150,0,NULL,NULL,'admin','2011-04-13','student0150','enrolled','3','standard','\0',1),(151,0,NULL,NULL,'admin','2011-04-13','student0151','wait','3','pnp','\0',1),(152,0,NULL,NULL,'admin','2011-04-13','student0152','enrolled','3','standard','\0',1),(153,0,NULL,NULL,'admin','2011-04-13','student0153','wait','3','pnp','\0',1),(154,0,NULL,NULL,'admin','2011-04-13','student0154','enrolled','3','standard','\0',1),(155,0,NULL,NULL,'admin','2011-04-13','student0155','wait','3','pnp','\0',1),(156,0,NULL,NULL,'admin','2011-04-13','student0156','enrolled','3','standard','\0',1),(157,0,NULL,NULL,'admin','2011-04-13','student0157','wait','3','pnp','\0',1),(158,0,NULL,NULL,'admin','2011-04-13','student0158','enrolled','3','standard','\0',1),(159,0,NULL,NULL,'admin','2011-04-13','student0159','wait','3','pnp','\0',1),(160,0,NULL,NULL,'admin','2011-04-13','student0160','enrolled','3','standard','\0',1),(161,0,NULL,NULL,'admin','2011-04-13','student0161','wait','3','pnp','\0',1),(162,0,NULL,NULL,'admin','2011-04-13','student0162','enrolled','3','standard','\0',1),(163,0,NULL,NULL,'admin','2011-04-13','student0163','wait','3','pnp','\0',1),(164,0,NULL,NULL,'admin','2011-04-13','student0164','enrolled','3','standard','\0',1),(165,0,NULL,NULL,'admin','2011-04-13','student0165','wait','3','pnp','\0',1),(166,0,NULL,NULL,'admin','2011-04-13','student0166','enrolled','3','standard','\0',1),(167,0,NULL,NULL,'admin','2011-04-13','student0167','wait','3','pnp','\0',1),(168,0,NULL,NULL,'admin','2011-04-13','student0168','enrolled','3','standard','\0',1),(169,0,NULL,NULL,'admin','2011-04-13','student0169','wait','3','pnp','\0',1),(170,0,NULL,NULL,'admin','2011-04-13','student0170','enrolled','3','standard','\0',1),(171,0,NULL,NULL,'admin','2011-04-13','student0171','wait','3','pnp','\0',1),(172,0,NULL,NULL,'admin','2011-04-13','student0172','enrolled','3','standard','\0',1),(173,0,NULL,NULL,'admin','2011-04-13','student0173','wait','3','pnp','\0',1),(174,0,NULL,NULL,'admin','2011-04-13','student0174','enrolled','3','standard','\0',1),(175,0,NULL,NULL,'admin','2011-04-13','student0175','wait','3','pnp','\0',1),(176,0,NULL,NULL,'admin','2011-04-13','student0176','enrolled','3','standard','\0',1),(177,0,NULL,NULL,'admin','2011-04-13','student0177','wait','3','pnp','\0',1),(178,0,NULL,NULL,'admin','2011-04-13','student0178','enrolled','3','standard','\0',1),(179,0,NULL,NULL,'admin','2011-04-13','student0179','wait','3','pnp','\0',1),(180,0,NULL,NULL,'admin','2011-04-13','student0180','enrolled','3','standard','\0',1),(181,0,NULL,NULL,'admin','2011-04-13','student0181','wait','3','pnp','\0',2),(182,0,NULL,NULL,'admin','2011-04-13','student0182','enrolled','3','standard','\0',2),(183,0,NULL,NULL,'admin','2011-04-13','student0183','wait','3','pnp','\0',2),(184,0,NULL,NULL,'admin','2011-04-13','student0184','enrolled','3','standard','\0',2),(185,0,NULL,NULL,'admin','2011-04-13','student0185','wait','3','pnp','\0',2),(186,0,NULL,NULL,'admin','2011-04-13','student0186','enrolled','3','standard','\0',2),(187,0,NULL,NULL,'admin','2011-04-13','student0187','wait','3','pnp','\0',2),(188,0,NULL,NULL,'admin','2011-04-13','student0188','enrolled','3','standard','\0',2),(189,0,NULL,NULL,'admin','2011-04-13','student0189','wait','3','pnp','\0',2),(190,0,NULL,NULL,'admin','2011-04-13','student0190','enrolled','3','standard','\0',2),(191,0,NULL,NULL,'admin','2011-04-13','student0191','wait','3','pnp','\0',2),(192,0,NULL,NULL,'admin','2011-04-13','student0192','enrolled','3','standard','\0',2),(193,0,NULL,NULL,'admin','2011-04-13','student0193','wait','3','pnp','\0',2),(194,0,NULL,NULL,'admin','2011-04-13','student0194','enrolled','3','standard','\0',2),(195,0,NULL,NULL,'admin','2011-04-13','student0195','wait','3','pnp','\0',2),(196,0,NULL,NULL,'admin','2011-04-13','student0196','enrolled','3','standard','\0',2),(197,0,NULL,NULL,'admin','2011-04-13','student0197','wait','3','pnp','\0',2),(198,0,NULL,NULL,'admin','2011-04-13','student0198','enrolled','3','standard','\0',2),(199,0,NULL,NULL,'admin','2011-04-13','student0199','wait','3','pnp','\0',2),(200,0,NULL,NULL,'admin','2011-04-13','student0200','enrolled','3','standard','\0',2),(201,0,NULL,NULL,'admin','2011-04-13','student0201','wait','3','pnp','\0',2),(202,0,NULL,NULL,'admin','2011-04-13','student0202','enrolled','3','standard','\0',2),(203,0,NULL,NULL,'admin','2011-04-13','student0203','wait','3','pnp','\0',2),(204,0,NULL,NULL,'admin','2011-04-13','student0204','enrolled','3','standard','\0',2),(205,0,NULL,NULL,'admin','2011-04-13','student0205','wait','3','pnp','\0',2),(206,0,NULL,NULL,'admin','2011-04-13','student0206','enrolled','3','standard','\0',2),(207,0,NULL,NULL,'admin','2011-04-13','student0207','wait','3','pnp','\0',2),(208,0,NULL,NULL,'admin','2011-04-13','student0208','enrolled','3','standard','\0',2),(209,0,NULL,NULL,'admin','2011-04-13','student0209','wait','3','pnp','\0',2),(210,0,NULL,NULL,'admin','2011-04-13','student0210','enrolled','3','standard','\0',2),(211,0,NULL,NULL,'admin','2011-04-13','student0211','wait','3','pnp','\0',2),(212,0,NULL,NULL,'admin','2011-04-13','student0212','enrolled','3','standard','\0',2),(213,0,NULL,NULL,'admin','2011-04-13','student0213','wait','3','pnp','\0',2),(214,0,NULL,NULL,'admin','2011-04-13','student0214','enrolled','3','standard','\0',2),(215,0,NULL,NULL,'admin','2011-04-13','student0215','wait','3','pnp','\0',2),(216,0,NULL,NULL,'admin','2011-04-13','student0216','enrolled','3','standard','\0',2),(217,0,NULL,NULL,'admin','2011-04-13','student0217','wait','3','pnp','\0',2),(218,0,NULL,NULL,'admin','2011-04-13','student0218','enrolled','3','standard','\0',2),(219,0,NULL,NULL,'admin','2011-04-13','student0219','wait','3','pnp','\0',2),(220,0,NULL,NULL,'admin','2011-04-13','student0220','enrolled','3','standard','\0',2),(221,0,NULL,NULL,'admin','2011-04-13','student0221','wait','3','pnp','\0',2),(222,0,NULL,NULL,'admin','2011-04-13','student0222','enrolled','3','standard','\0',2),(223,0,NULL,NULL,'admin','2011-04-13','student0223','wait','3','pnp','\0',2),(224,0,NULL,NULL,'admin','2011-04-13','student0224','enrolled','3','standard','\0',2),(225,0,NULL,NULL,'admin','2011-04-13','student0225','wait','3','pnp','\0',2),(226,0,NULL,NULL,'admin','2011-04-13','student0226','enrolled','3','standard','\0',2),(227,0,NULL,NULL,'admin','2011-04-13','student0227','wait','3','pnp','\0',2),(228,0,NULL,NULL,'admin','2011-04-13','student0228','enrolled','3','standard','\0',2),(229,0,NULL,NULL,'admin','2011-04-13','student0229','wait','3','pnp','\0',2),(230,0,NULL,NULL,'admin','2011-04-13','student0230','enrolled','3','standard','\0',2),(231,0,NULL,NULL,'admin','2011-04-13','student0231','wait','3','pnp','\0',2),(232,0,NULL,NULL,'admin','2011-04-13','student0232','enrolled','3','standard','\0',2),(233,0,NULL,NULL,'admin','2011-04-13','student0233','wait','3','pnp','\0',2),(234,0,NULL,NULL,'admin','2011-04-13','student0234','enrolled','3','standard','\0',2),(235,0,NULL,NULL,'admin','2011-04-13','student0235','wait','3','pnp','\0',2),(236,0,NULL,NULL,'admin','2011-04-13','student0236','enrolled','3','standard','\0',2),(237,0,NULL,NULL,'admin','2011-04-13','student0237','wait','3','pnp','\0',2),(238,0,NULL,NULL,'admin','2011-04-13','student0238','enrolled','3','standard','\0',2),(239,0,NULL,NULL,'admin','2011-04-13','student0239','wait','3','pnp','\0',2),(240,0,NULL,NULL,'admin','2011-04-13','student0240','enrolled','3','standard','\0',2),(241,0,NULL,NULL,'admin','2011-04-13','student0241','wait','3','pnp','\0',2),(242,0,NULL,NULL,'admin','2011-04-13','student0242','enrolled','3','standard','\0',2),(243,0,NULL,NULL,'admin','2011-04-13','student0243','wait','3','pnp','\0',2),(244,0,NULL,NULL,'admin','2011-04-13','student0244','enrolled','3','standard','\0',2),(245,0,NULL,NULL,'admin','2011-04-13','student0245','wait','3','pnp','\0',2),(246,0,NULL,NULL,'admin','2011-04-13','student0246','enrolled','3','standard','\0',2),(247,0,NULL,NULL,'admin','2011-04-13','student0247','wait','3','pnp','\0',2),(248,0,NULL,NULL,'admin','2011-04-13','student0248','enrolled','3','standard','\0',2),(249,0,NULL,NULL,'admin','2011-04-13','student0249','wait','3','pnp','\0',2),(250,0,NULL,NULL,'admin','2011-04-13','student0250','enrolled','3','standard','\0',2),(251,0,NULL,NULL,'admin','2011-04-13','student0251','wait','3','pnp','\0',2),(252,0,NULL,NULL,'admin','2011-04-13','student0252','enrolled','3','standard','\0',2),(253,0,NULL,NULL,'admin','2011-04-13','student0253','wait','3','pnp','\0',2),(254,0,NULL,NULL,'admin','2011-04-13','student0254','enrolled','3','standard','\0',2),(255,0,NULL,NULL,'admin','2011-04-13','student0255','wait','3','pnp','\0',2),(256,0,NULL,NULL,'admin','2011-04-13','student0256','enrolled','3','standard','\0',2),(257,0,NULL,NULL,'admin','2011-04-13','student0257','wait','3','pnp','\0',2),(258,0,NULL,NULL,'admin','2011-04-13','student0258','enrolled','3','standard','\0',2),(259,0,NULL,NULL,'admin','2011-04-13','student0259','wait','3','pnp','\0',2),(260,0,NULL,NULL,'admin','2011-04-13','student0260','enrolled','3','standard','\0',2),(261,0,NULL,NULL,'admin','2011-04-13','student0261','wait','3','pnp','\0',2),(262,0,NULL,NULL,'admin','2011-04-13','student0262','enrolled','3','standard','\0',2),(263,0,NULL,NULL,'admin','2011-04-13','student0263','wait','3','pnp','\0',2),(264,0,NULL,NULL,'admin','2011-04-13','student0264','enrolled','3','standard','\0',2),(265,0,NULL,NULL,'admin','2011-04-13','student0265','wait','3','pnp','\0',2),(266,0,NULL,NULL,'admin','2011-04-13','student0266','enrolled','3','standard','\0',2),(267,0,NULL,NULL,'admin','2011-04-13','student0267','wait','3','pnp','\0',2),(268,0,NULL,NULL,'admin','2011-04-13','student0268','enrolled','3','standard','\0',2),(269,0,NULL,NULL,'admin','2011-04-13','student0269','wait','3','pnp','\0',2),(270,0,NULL,NULL,'admin','2011-04-13','student0270','enrolled','3','standard','\0',2),(271,0,NULL,NULL,'admin','2011-04-13','student0271','wait','3','pnp','\0',2),(272,0,NULL,NULL,'admin','2011-04-13','student0272','enrolled','3','standard','\0',2),(273,0,NULL,NULL,'admin','2011-04-13','student0273','wait','3','pnp','\0',2),(274,0,NULL,NULL,'admin','2011-04-13','student0274','enrolled','3','standard','\0',2),(275,0,NULL,NULL,'admin','2011-04-13','student0275','wait','3','pnp','\0',2),(276,0,NULL,NULL,'admin','2011-04-13','student0276','enrolled','3','standard','\0',2),(277,0,NULL,NULL,'admin','2011-04-13','student0277','wait','3','pnp','\0',2),(278,0,NULL,NULL,'admin','2011-04-13','student0278','enrolled','3','standard','\0',2),(279,0,NULL,NULL,'admin','2011-04-13','student0279','wait','3','pnp','\0',2),(280,0,NULL,NULL,'admin','2011-04-13','student0280','enrolled','3','standard','\0',2),(281,0,NULL,NULL,'admin','2011-04-13','student0281','wait','3','pnp','\0',2),(282,0,NULL,NULL,'admin','2011-04-13','student0282','enrolled','3','standard','\0',2),(283,0,NULL,NULL,'admin','2011-04-13','student0283','wait','3','pnp','\0',2),(284,0,NULL,NULL,'admin','2011-04-13','student0284','enrolled','3','standard','\0',2),(285,0,NULL,NULL,'admin','2011-04-13','student0285','wait','3','pnp','\0',2),(286,0,NULL,NULL,'admin','2011-04-13','student0286','enrolled','3','standard','\0',2),(287,0,NULL,NULL,'admin','2011-04-13','student0287','wait','3','pnp','\0',2),(288,0,NULL,NULL,'admin','2011-04-13','student0288','enrolled','3','standard','\0',2),(289,0,NULL,NULL,'admin','2011-04-13','student0289','wait','3','pnp','\0',2),(290,0,NULL,NULL,'admin','2011-04-13','student0290','enrolled','3','standard','\0',2),(291,0,NULL,NULL,'admin','2011-04-13','student0291','wait','3','pnp','\0',2),(292,0,NULL,NULL,'admin','2011-04-13','student0292','enrolled','3','standard','\0',2),(293,0,NULL,NULL,'admin','2011-04-13','student0293','wait','3','pnp','\0',2),(294,0,NULL,NULL,'admin','2011-04-13','student0294','enrolled','3','standard','\0',2),(295,0,NULL,NULL,'admin','2011-04-13','student0295','wait','3','pnp','\0',2),(296,0,NULL,NULL,'admin','2011-04-13','student0296','enrolled','3','standard','\0',2),(297,0,NULL,NULL,'admin','2011-04-13','student0297','wait','3','pnp','\0',2),(298,0,NULL,NULL,'admin','2011-04-13','student0298','enrolled','3','standard','\0',2),(299,0,NULL,NULL,'admin','2011-04-13','student0299','wait','3','pnp','\0',2),(300,0,NULL,NULL,'admin','2011-04-13','student0300','enrolled','3','standard','\0',2),(301,0,NULL,NULL,'admin','2011-04-13','student0301','wait','3','pnp','\0',2),(302,0,NULL,NULL,'admin','2011-04-13','student0302','enrolled','3','standard','\0',2),(303,0,NULL,NULL,'admin','2011-04-13','student0303','wait','3','pnp','\0',2),(304,0,NULL,NULL,'admin','2011-04-13','student0304','enrolled','3','standard','\0',2),(305,0,NULL,NULL,'admin','2011-04-13','student0305','wait','3','pnp','\0',2),(306,0,NULL,NULL,'admin','2011-04-13','student0306','enrolled','3','standard','\0',2),(307,0,NULL,NULL,'admin','2011-04-13','student0307','wait','3','pnp','\0',2),(308,0,NULL,NULL,'admin','2011-04-13','student0308','enrolled','3','standard','\0',2),(309,0,NULL,NULL,'admin','2011-04-13','student0309','wait','3','pnp','\0',2),(310,0,NULL,NULL,'admin','2011-04-13','student0310','enrolled','3','standard','\0',2),(311,0,NULL,NULL,'admin','2011-04-13','student0311','wait','3','pnp','\0',2),(312,0,NULL,NULL,'admin','2011-04-13','student0312','enrolled','3','standard','\0',2),(313,0,NULL,NULL,'admin','2011-04-13','student0313','wait','3','pnp','\0',2),(314,0,NULL,NULL,'admin','2011-04-13','student0314','enrolled','3','standard','\0',2),(315,0,NULL,NULL,'admin','2011-04-13','student0315','wait','3','pnp','\0',2),(316,0,NULL,NULL,'admin','2011-04-13','student0316','enrolled','3','standard','\0',2),(317,0,NULL,NULL,'admin','2011-04-13','student0317','wait','3','pnp','\0',2),(318,0,NULL,NULL,'admin','2011-04-13','student0318','enrolled','3','standard','\0',2),(319,0,NULL,NULL,'admin','2011-04-13','student0319','wait','3','pnp','\0',2),(320,0,NULL,NULL,'admin','2011-04-13','student0320','enrolled','3','standard','\0',2),(321,0,NULL,NULL,'admin','2011-04-13','student0321','wait','3','pnp','\0',2),(322,0,NULL,NULL,'admin','2011-04-13','student0322','enrolled','3','standard','\0',2),(323,0,NULL,NULL,'admin','2011-04-13','student0323','wait','3','pnp','\0',2),(324,0,NULL,NULL,'admin','2011-04-13','student0324','enrolled','3','standard','\0',2),(325,0,NULL,NULL,'admin','2011-04-13','student0325','wait','3','pnp','\0',2),(326,0,NULL,NULL,'admin','2011-04-13','student0326','enrolled','3','standard','\0',2),(327,0,NULL,NULL,'admin','2011-04-13','student0327','wait','3','pnp','\0',2),(328,0,NULL,NULL,'admin','2011-04-13','student0328','enrolled','3','standard','\0',2),(329,0,NULL,NULL,'admin','2011-04-13','student0329','wait','3','pnp','\0',2),(330,0,NULL,NULL,'admin','2011-04-13','student0330','enrolled','3','standard','\0',2),(331,0,NULL,NULL,'admin','2011-04-13','student0331','wait','3','pnp','\0',2),(332,0,NULL,NULL,'admin','2011-04-13','student0332','enrolled','3','standard','\0',2),(333,0,NULL,NULL,'admin','2011-04-13','student0333','wait','3','pnp','\0',2),(334,0,NULL,NULL,'admin','2011-04-13','student0334','enrolled','3','standard','\0',2),(335,0,NULL,NULL,'admin','2011-04-13','student0335','wait','3','pnp','\0',2),(336,0,NULL,NULL,'admin','2011-04-13','student0336','enrolled','3','standard','\0',2),(337,0,NULL,NULL,'admin','2011-04-13','student0337','wait','3','pnp','\0',2),(338,0,NULL,NULL,'admin','2011-04-13','student0338','enrolled','3','standard','\0',2),(339,0,NULL,NULL,'admin','2011-04-13','student0339','wait','3','pnp','\0',2),(340,0,NULL,NULL,'admin','2011-04-13','student0340','enrolled','3','standard','\0',2),(341,0,NULL,NULL,'admin','2011-04-13','student0341','wait','3','pnp','\0',2),(342,0,NULL,NULL,'admin','2011-04-13','student0342','enrolled','3','standard','\0',2),(343,0,NULL,NULL,'admin','2011-04-13','student0343','wait','3','pnp','\0',2),(344,0,NULL,NULL,'admin','2011-04-13','student0344','enrolled','3','standard','\0',2),(345,0,NULL,NULL,'admin','2011-04-13','student0345','wait','3','pnp','\0',2),(346,0,NULL,NULL,'admin','2011-04-13','student0346','enrolled','3','standard','\0',2),(347,0,NULL,NULL,'admin','2011-04-13','student0347','wait','3','pnp','\0',2),(348,0,NULL,NULL,'admin','2011-04-13','student0348','enrolled','3','standard','\0',2),(349,0,NULL,NULL,'admin','2011-04-13','student0349','wait','3','pnp','\0',2),(350,0,NULL,NULL,'admin','2011-04-13','student0350','enrolled','3','standard','\0',2),(351,0,NULL,NULL,'admin','2011-04-13','student0351','wait','3','pnp','\0',2),(352,0,NULL,NULL,'admin','2011-04-13','student0352','enrolled','3','standard','\0',2),(353,0,NULL,NULL,'admin','2011-04-13','student0353','wait','3','pnp','\0',2),(354,0,NULL,NULL,'admin','2011-04-13','student0354','enrolled','3','standard','\0',2),(355,0,NULL,NULL,'admin','2011-04-13','student0355','wait','3','pnp','\0',2),(356,0,NULL,NULL,'admin','2011-04-13','student0356','enrolled','3','standard','\0',2),(357,0,NULL,NULL,'admin','2011-04-13','student0357','wait','3','pnp','\0',2),(358,0,NULL,NULL,'admin','2011-04-13','student0358','enrolled','3','standard','\0',2),(359,0,NULL,NULL,'admin','2011-04-13','student0359','wait','3','pnp','\0',2),(360,0,NULL,NULL,'admin','2011-04-13','student0360','enrolled','3','standard','\0',2),(361,0,NULL,NULL,'admin','2011-04-13','student0001','wait','3','pnp','\0',3),(362,0,NULL,NULL,'admin','2011-04-13','student0002','enrolled','3','standard','\0',3),(363,0,NULL,NULL,'admin','2011-04-13','student0003','wait','3','pnp','\0',3),(364,0,NULL,NULL,'admin','2011-04-13','student0004','enrolled','3','standard','\0',3),(365,0,NULL,NULL,'admin','2011-04-13','student0005','wait','3','pnp','\0',3),(366,0,NULL,NULL,'admin','2011-04-13','student0006','enrolled','3','standard','\0',3),(367,0,NULL,NULL,'admin','2011-04-13','student0007','wait','3','pnp','\0',3),(368,0,NULL,NULL,'admin','2011-04-13','student0008','enrolled','3','standard','\0',3),(369,0,NULL,NULL,'admin','2011-04-13','student0009','wait','3','pnp','\0',3),(370,0,NULL,NULL,'admin','2011-04-13','student0010','enrolled','3','standard','\0',3),(371,0,NULL,NULL,'admin','2011-04-13','student0011','wait','3','pnp','\0',3),(372,0,NULL,NULL,'admin','2011-04-13','student0012','enrolled','3','standard','\0',3),(373,0,NULL,NULL,'admin','2011-04-13','student0013','wait','3','pnp','\0',3),(374,0,NULL,NULL,'admin','2011-04-13','student0014','enrolled','3','standard','\0',3),(375,0,NULL,NULL,'admin','2011-04-13','student0015','wait','3','pnp','\0',3),(376,0,NULL,NULL,'admin','2011-04-13','student0016','enrolled','3','standard','\0',3),(377,0,NULL,NULL,'admin','2011-04-13','student0017','wait','3','pnp','\0',3),(378,0,NULL,NULL,'admin','2011-04-13','student0018','enrolled','3','standard','\0',3),(379,0,NULL,NULL,'admin','2011-04-13','student0019','wait','3','pnp','\0',3),(380,0,NULL,NULL,'admin','2011-04-13','student0020','enrolled','3','standard','\0',3),(381,0,NULL,NULL,'admin','2011-04-13','student0021','wait','3','pnp','\0',3),(382,0,NULL,NULL,'admin','2011-04-13','student0022','enrolled','3','standard','\0',3),(383,0,NULL,NULL,'admin','2011-04-13','student0023','wait','3','pnp','\0',3),(384,0,NULL,NULL,'admin','2011-04-13','student0024','enrolled','3','standard','\0',3),(385,0,NULL,NULL,'admin','2011-04-13','student0025','wait','3','pnp','\0',3),(386,0,NULL,NULL,'admin','2011-04-13','student0026','enrolled','3','standard','\0',3),(387,0,NULL,NULL,'admin','2011-04-13','student0027','wait','3','pnp','\0',3),(388,0,NULL,NULL,'admin','2011-04-13','student0028','enrolled','3','standard','\0',3),(389,0,NULL,NULL,'admin','2011-04-13','student0029','wait','3','pnp','\0',3),(390,0,NULL,NULL,'admin','2011-04-13','student0030','enrolled','3','standard','\0',3),(391,0,NULL,NULL,'admin','2011-04-13','student0031','wait','3','pnp','\0',3),(392,0,NULL,NULL,'admin','2011-04-13','student0032','enrolled','3','standard','\0',3),(393,0,NULL,NULL,'admin','2011-04-13','student0033','wait','3','pnp','\0',3),(394,0,NULL,NULL,'admin','2011-04-13','student0034','enrolled','3','standard','\0',3),(395,0,NULL,NULL,'admin','2011-04-13','student0035','wait','3','pnp','\0',3),(396,0,NULL,NULL,'admin','2011-04-13','student0036','enrolled','3','standard','\0',3),(397,0,NULL,NULL,'admin','2011-04-13','student0037','wait','3','pnp','\0',3),(398,0,NULL,NULL,'admin','2011-04-13','student0038','enrolled','3','standard','\0',3),(399,0,NULL,NULL,'admin','2011-04-13','student0039','wait','3','pnp','\0',3),(400,0,NULL,NULL,'admin','2011-04-13','student0040','enrolled','3','standard','\0',3),(401,0,NULL,NULL,'admin','2011-04-13','student0041','wait','3','pnp','\0',3),(402,0,NULL,NULL,'admin','2011-04-13','student0042','enrolled','3','standard','\0',3),(403,0,NULL,NULL,'admin','2011-04-13','student0043','wait','3','pnp','\0',3),(404,0,NULL,NULL,'admin','2011-04-13','student0044','enrolled','3','standard','\0',3),(405,0,NULL,NULL,'admin','2011-04-13','student0045','wait','3','pnp','\0',3),(406,0,NULL,NULL,'admin','2011-04-13','student0046','enrolled','3','standard','\0',3),(407,0,NULL,NULL,'admin','2011-04-13','student0047','wait','3','pnp','\0',3),(408,0,NULL,NULL,'admin','2011-04-13','student0048','enrolled','3','standard','\0',3),(409,0,NULL,NULL,'admin','2011-04-13','student0049','wait','3','pnp','\0',3),(410,0,NULL,NULL,'admin','2011-04-13','student0050','enrolled','3','standard','\0',3),(411,0,NULL,NULL,'admin','2011-04-13','student0051','wait','3','pnp','\0',3),(412,0,NULL,NULL,'admin','2011-04-13','student0052','enrolled','3','standard','\0',3),(413,0,NULL,NULL,'admin','2011-04-13','student0053','wait','3','pnp','\0',3),(414,0,NULL,NULL,'admin','2011-04-13','student0054','enrolled','3','standard','\0',3),(415,0,NULL,NULL,'admin','2011-04-13','student0055','wait','3','pnp','\0',3),(416,0,NULL,NULL,'admin','2011-04-13','student0056','enrolled','3','standard','\0',3),(417,0,NULL,NULL,'admin','2011-04-13','student0057','wait','3','pnp','\0',3),(418,0,NULL,NULL,'admin','2011-04-13','student0058','enrolled','3','standard','\0',3),(419,0,NULL,NULL,'admin','2011-04-13','student0059','wait','3','pnp','\0',3),(420,0,NULL,NULL,'admin','2011-04-13','student0060','enrolled','3','standard','\0',3),(421,0,NULL,NULL,'admin','2011-04-13','student0061','wait','3','pnp','\0',3),(422,0,NULL,NULL,'admin','2011-04-13','student0062','enrolled','3','standard','\0',3),(423,0,NULL,NULL,'admin','2011-04-13','student0063','wait','3','pnp','\0',3),(424,0,NULL,NULL,'admin','2011-04-13','student0064','enrolled','3','standard','\0',3),(425,0,NULL,NULL,'admin','2011-04-13','student0065','wait','3','pnp','\0',3),(426,0,NULL,NULL,'admin','2011-04-13','student0066','enrolled','3','standard','\0',3),(427,0,NULL,NULL,'admin','2011-04-13','student0067','wait','3','pnp','\0',3),(428,0,NULL,NULL,'admin','2011-04-13','student0068','enrolled','3','standard','\0',3),(429,0,NULL,NULL,'admin','2011-04-13','student0069','wait','3','pnp','\0',3),(430,0,NULL,NULL,'admin','2011-04-13','student0070','enrolled','3','standard','\0',3),(431,0,NULL,NULL,'admin','2011-04-13','student0071','wait','3','pnp','\0',3),(432,0,NULL,NULL,'admin','2011-04-13','student0072','enrolled','3','standard','\0',3),(433,0,NULL,NULL,'admin','2011-04-13','student0073','wait','3','pnp','\0',3),(434,0,NULL,NULL,'admin','2011-04-13','student0074','enrolled','3','standard','\0',3),(435,0,NULL,NULL,'admin','2011-04-13','student0075','wait','3','pnp','\0',3),(436,0,NULL,NULL,'admin','2011-04-13','student0076','enrolled','3','standard','\0',3),(437,0,NULL,NULL,'admin','2011-04-13','student0077','wait','3','pnp','\0',3),(438,0,NULL,NULL,'admin','2011-04-13','student0078','enrolled','3','standard','\0',3),(439,0,NULL,NULL,'admin','2011-04-13','student0079','wait','3','pnp','\0',3),(440,0,NULL,NULL,'admin','2011-04-13','student0080','enrolled','3','standard','\0',3),(441,0,NULL,NULL,'admin','2011-04-13','student0081','wait','3','pnp','\0',3),(442,0,NULL,NULL,'admin','2011-04-13','student0082','enrolled','3','standard','\0',3),(443,0,NULL,NULL,'admin','2011-04-13','student0083','wait','3','pnp','\0',3),(444,0,NULL,NULL,'admin','2011-04-13','student0084','enrolled','3','standard','\0',3),(445,0,NULL,NULL,'admin','2011-04-13','student0085','wait','3','pnp','\0',3),(446,0,NULL,NULL,'admin','2011-04-13','student0086','enrolled','3','standard','\0',3),(447,0,NULL,NULL,'admin','2011-04-13','student0087','wait','3','pnp','\0',3),(448,0,NULL,NULL,'admin','2011-04-13','student0088','enrolled','3','standard','\0',3),(449,0,NULL,NULL,'admin','2011-04-13','student0089','wait','3','pnp','\0',3),(450,0,NULL,NULL,'admin','2011-04-13','student0090','enrolled','3','standard','\0',3),(451,0,NULL,NULL,'admin','2011-04-13','student0091','wait','3','pnp','\0',3),(452,0,NULL,NULL,'admin','2011-04-13','student0092','enrolled','3','standard','\0',3),(453,0,NULL,NULL,'admin','2011-04-13','student0093','wait','3','pnp','\0',3),(454,0,NULL,NULL,'admin','2011-04-13','student0094','enrolled','3','standard','\0',3),(455,0,NULL,NULL,'admin','2011-04-13','student0095','wait','3','pnp','\0',3),(456,0,NULL,NULL,'admin','2011-04-13','student0096','enrolled','3','standard','\0',3),(457,0,NULL,NULL,'admin','2011-04-13','student0097','wait','3','pnp','\0',3),(458,0,NULL,NULL,'admin','2011-04-13','student0098','enrolled','3','standard','\0',3),(459,0,NULL,NULL,'admin','2011-04-13','student0099','wait','3','pnp','\0',3),(460,0,NULL,NULL,'admin','2011-04-13','student0100','enrolled','3','standard','\0',3),(461,0,NULL,NULL,'admin','2011-04-13','student0101','wait','3','pnp','\0',3),(462,0,NULL,NULL,'admin','2011-04-13','student0102','enrolled','3','standard','\0',3),(463,0,NULL,NULL,'admin','2011-04-13','student0103','wait','3','pnp','\0',3),(464,0,NULL,NULL,'admin','2011-04-13','student0104','enrolled','3','standard','\0',3),(465,0,NULL,NULL,'admin','2011-04-13','student0105','wait','3','pnp','\0',3),(466,0,NULL,NULL,'admin','2011-04-13','student0106','enrolled','3','standard','\0',3),(467,0,NULL,NULL,'admin','2011-04-13','student0107','wait','3','pnp','\0',3),(468,0,NULL,NULL,'admin','2011-04-13','student0108','enrolled','3','standard','\0',3),(469,0,NULL,NULL,'admin','2011-04-13','student0109','wait','3','pnp','\0',3),(470,0,NULL,NULL,'admin','2011-04-13','student0110','enrolled','3','standard','\0',3),(471,0,NULL,NULL,'admin','2011-04-13','student0111','wait','3','pnp','\0',3),(472,0,NULL,NULL,'admin','2011-04-13','student0112','enrolled','3','standard','\0',3),(473,0,NULL,NULL,'admin','2011-04-13','student0113','wait','3','pnp','\0',3),(474,0,NULL,NULL,'admin','2011-04-13','student0114','enrolled','3','standard','\0',3),(475,0,NULL,NULL,'admin','2011-04-13','student0115','wait','3','pnp','\0',3),(476,0,NULL,NULL,'admin','2011-04-13','student0116','enrolled','3','standard','\0',3),(477,0,NULL,NULL,'admin','2011-04-13','student0117','wait','3','pnp','\0',3),(478,0,NULL,NULL,'admin','2011-04-13','student0118','enrolled','3','standard','\0',3),(479,0,NULL,NULL,'admin','2011-04-13','student0119','wait','3','pnp','\0',3),(480,0,NULL,NULL,'admin','2011-04-13','student0120','enrolled','3','standard','\0',3),(481,0,NULL,NULL,'admin','2011-04-13','student0121','wait','3','pnp','\0',3),(482,0,NULL,NULL,'admin','2011-04-13','student0122','enrolled','3','standard','\0',3),(483,0,NULL,NULL,'admin','2011-04-13','student0123','wait','3','pnp','\0',3),(484,0,NULL,NULL,'admin','2011-04-13','student0124','enrolled','3','standard','\0',3),(485,0,NULL,NULL,'admin','2011-04-13','student0125','wait','3','pnp','\0',3),(486,0,NULL,NULL,'admin','2011-04-13','student0126','enrolled','3','standard','\0',3),(487,0,NULL,NULL,'admin','2011-04-13','student0127','wait','3','pnp','\0',3),(488,0,NULL,NULL,'admin','2011-04-13','student0128','enrolled','3','standard','\0',3),(489,0,NULL,NULL,'admin','2011-04-13','student0129','wait','3','pnp','\0',3),(490,0,NULL,NULL,'admin','2011-04-13','student0130','enrolled','3','standard','\0',3),(491,0,NULL,NULL,'admin','2011-04-13','student0131','wait','3','pnp','\0',3),(492,0,NULL,NULL,'admin','2011-04-13','student0132','enrolled','3','standard','\0',3),(493,0,NULL,NULL,'admin','2011-04-13','student0133','wait','3','pnp','\0',3),(494,0,NULL,NULL,'admin','2011-04-13','student0134','enrolled','3','standard','\0',3),(495,0,NULL,NULL,'admin','2011-04-13','student0135','wait','3','pnp','\0',3),(496,0,NULL,NULL,'admin','2011-04-13','student0136','enrolled','3','standard','\0',3),(497,0,NULL,NULL,'admin','2011-04-13','student0137','wait','3','pnp','\0',3),(498,0,NULL,NULL,'admin','2011-04-13','student0138','enrolled','3','standard','\0',3),(499,0,NULL,NULL,'admin','2011-04-13','student0139','wait','3','pnp','\0',3),(500,0,NULL,NULL,'admin','2011-04-13','student0140','enrolled','3','standard','\0',3),(501,0,NULL,NULL,'admin','2011-04-13','student0141','wait','3','pnp','\0',3),(502,0,NULL,NULL,'admin','2011-04-13','student0142','enrolled','3','standard','\0',3),(503,0,NULL,NULL,'admin','2011-04-13','student0143','wait','3','pnp','\0',3),(504,0,NULL,NULL,'admin','2011-04-13','student0144','enrolled','3','standard','\0',3),(505,0,NULL,NULL,'admin','2011-04-13','student0145','wait','3','pnp','\0',3),(506,0,NULL,NULL,'admin','2011-04-13','student0146','enrolled','3','standard','\0',3),(507,0,NULL,NULL,'admin','2011-04-13','student0147','wait','3','pnp','\0',3),(508,0,NULL,NULL,'admin','2011-04-13','student0148','enrolled','3','standard','\0',3),(509,0,NULL,NULL,'admin','2011-04-13','student0149','wait','3','pnp','\0',3),(510,0,NULL,NULL,'admin','2011-04-13','student0150','enrolled','3','standard','\0',3),(511,0,NULL,NULL,'admin','2011-04-13','student0151','wait','3','pnp','\0',3),(512,0,NULL,NULL,'admin','2011-04-13','student0152','enrolled','3','standard','\0',3),(513,0,NULL,NULL,'admin','2011-04-13','student0153','wait','3','pnp','\0',3),(514,0,NULL,NULL,'admin','2011-04-13','student0154','enrolled','3','standard','\0',3),(515,0,NULL,NULL,'admin','2011-04-13','student0155','wait','3','pnp','\0',3),(516,0,NULL,NULL,'admin','2011-04-13','student0156','enrolled','3','standard','\0',3),(517,0,NULL,NULL,'admin','2011-04-13','student0157','wait','3','pnp','\0',3),(518,0,NULL,NULL,'admin','2011-04-13','student0158','enrolled','3','standard','\0',3),(519,0,NULL,NULL,'admin','2011-04-13','student0159','wait','3','pnp','\0',3),(520,0,NULL,NULL,'admin','2011-04-13','student0160','enrolled','3','standard','\0',3),(521,0,NULL,NULL,'admin','2011-04-13','student0161','wait','3','pnp','\0',3),(522,0,NULL,NULL,'admin','2011-04-13','student0162','enrolled','3','standard','\0',3),(523,0,NULL,NULL,'admin','2011-04-13','student0163','wait','3','pnp','\0',3),(524,0,NULL,NULL,'admin','2011-04-13','student0164','enrolled','3','standard','\0',3),(525,0,NULL,NULL,'admin','2011-04-13','student0165','wait','3','pnp','\0',3),(526,0,NULL,NULL,'admin','2011-04-13','student0166','enrolled','3','standard','\0',3),(527,0,NULL,NULL,'admin','2011-04-13','student0167','wait','3','pnp','\0',3),(528,0,NULL,NULL,'admin','2011-04-13','student0168','enrolled','3','standard','\0',3),(529,0,NULL,NULL,'admin','2011-04-13','student0169','wait','3','pnp','\0',3),(530,0,NULL,NULL,'admin','2011-04-13','student0170','enrolled','3','standard','\0',3),(531,0,NULL,NULL,'admin','2011-04-13','student0171','wait','3','pnp','\0',3),(532,0,NULL,NULL,'admin','2011-04-13','student0172','enrolled','3','standard','\0',3),(533,0,NULL,NULL,'admin','2011-04-13','student0173','wait','3','pnp','\0',3),(534,0,NULL,NULL,'admin','2011-04-13','student0174','enrolled','3','standard','\0',3),(535,0,NULL,NULL,'admin','2011-04-13','student0175','wait','3','pnp','\0',3),(536,0,NULL,NULL,'admin','2011-04-13','student0176','enrolled','3','standard','\0',3),(537,0,NULL,NULL,'admin','2011-04-13','student0177','wait','3','pnp','\0',3),(538,0,NULL,NULL,'admin','2011-04-13','student0178','enrolled','3','standard','\0',3),(539,0,NULL,NULL,'admin','2011-04-13','student0179','wait','3','pnp','\0',3),(540,0,NULL,NULL,'admin','2011-04-13','student0180','enrolled','3','standard','\0',3),(541,0,NULL,NULL,'admin','2011-04-13','student0181','wait','3','pnp','\0',4),(542,0,NULL,NULL,'admin','2011-04-13','student0182','enrolled','3','standard','\0',4),(543,0,NULL,NULL,'admin','2011-04-13','student0183','wait','3','pnp','\0',4),(544,0,NULL,NULL,'admin','2011-04-13','student0184','enrolled','3','standard','\0',4),(545,0,NULL,NULL,'admin','2011-04-13','student0185','wait','3','pnp','\0',4),(546,0,NULL,NULL,'admin','2011-04-13','student0186','enrolled','3','standard','\0',4),(547,0,NULL,NULL,'admin','2011-04-13','student0187','wait','3','pnp','\0',4),(548,0,NULL,NULL,'admin','2011-04-13','student0188','enrolled','3','standard','\0',4),(549,0,NULL,NULL,'admin','2011-04-13','student0189','wait','3','pnp','\0',4),(550,0,NULL,NULL,'admin','2011-04-13','student0190','enrolled','3','standard','\0',4),(551,0,NULL,NULL,'admin','2011-04-13','student0191','wait','3','pnp','\0',4),(552,0,NULL,NULL,'admin','2011-04-13','student0192','enrolled','3','standard','\0',4),(553,0,NULL,NULL,'admin','2011-04-13','student0193','wait','3','pnp','\0',4),(554,0,NULL,NULL,'admin','2011-04-13','student0194','enrolled','3','standard','\0',4),(555,0,NULL,NULL,'admin','2011-04-13','student0195','wait','3','pnp','\0',4),(556,0,NULL,NULL,'admin','2011-04-13','student0196','enrolled','3','standard','\0',4),(557,0,NULL,NULL,'admin','2011-04-13','student0197','wait','3','pnp','\0',4),(558,0,NULL,NULL,'admin','2011-04-13','student0198','enrolled','3','standard','\0',4),(559,0,NULL,NULL,'admin','2011-04-13','student0199','wait','3','pnp','\0',4),(560,0,NULL,NULL,'admin','2011-04-13','student0200','enrolled','3','standard','\0',4),(561,0,NULL,NULL,'admin','2011-04-13','student0201','wait','3','pnp','\0',4),(562,0,NULL,NULL,'admin','2011-04-13','student0202','enrolled','3','standard','\0',4),(563,0,NULL,NULL,'admin','2011-04-13','student0203','wait','3','pnp','\0',4),(564,0,NULL,NULL,'admin','2011-04-13','student0204','enrolled','3','standard','\0',4),(565,0,NULL,NULL,'admin','2011-04-13','student0205','wait','3','pnp','\0',4),(566,0,NULL,NULL,'admin','2011-04-13','student0206','enrolled','3','standard','\0',4),(567,0,NULL,NULL,'admin','2011-04-13','student0207','wait','3','pnp','\0',4),(568,0,NULL,NULL,'admin','2011-04-13','student0208','enrolled','3','standard','\0',4),(569,0,NULL,NULL,'admin','2011-04-13','student0209','wait','3','pnp','\0',4),(570,0,NULL,NULL,'admin','2011-04-13','student0210','enrolled','3','standard','\0',4),(571,0,NULL,NULL,'admin','2011-04-13','student0211','wait','3','pnp','\0',4),(572,0,NULL,NULL,'admin','2011-04-13','student0212','enrolled','3','standard','\0',4),(573,0,NULL,NULL,'admin','2011-04-13','student0213','wait','3','pnp','\0',4),(574,0,NULL,NULL,'admin','2011-04-13','student0214','enrolled','3','standard','\0',4),(575,0,NULL,NULL,'admin','2011-04-13','student0215','wait','3','pnp','\0',4),(576,0,NULL,NULL,'admin','2011-04-13','student0216','enrolled','3','standard','\0',4),(577,0,NULL,NULL,'admin','2011-04-13','student0217','wait','3','pnp','\0',4),(578,0,NULL,NULL,'admin','2011-04-13','student0218','enrolled','3','standard','\0',4),(579,0,NULL,NULL,'admin','2011-04-13','student0219','wait','3','pnp','\0',4),(580,0,NULL,NULL,'admin','2011-04-13','student0220','enrolled','3','standard','\0',4),(581,0,NULL,NULL,'admin','2011-04-13','student0221','wait','3','pnp','\0',4),(582,0,NULL,NULL,'admin','2011-04-13','student0222','enrolled','3','standard','\0',4),(583,0,NULL,NULL,'admin','2011-04-13','student0223','wait','3','pnp','\0',4),(584,0,NULL,NULL,'admin','2011-04-13','student0224','enrolled','3','standard','\0',4),(585,0,NULL,NULL,'admin','2011-04-13','student0225','wait','3','pnp','\0',4),(586,0,NULL,NULL,'admin','2011-04-13','student0226','enrolled','3','standard','\0',4),(587,0,NULL,NULL,'admin','2011-04-13','student0227','wait','3','pnp','\0',4),(588,0,NULL,NULL,'admin','2011-04-13','student0228','enrolled','3','standard','\0',4),(589,0,NULL,NULL,'admin','2011-04-13','student0229','wait','3','pnp','\0',4),(590,0,NULL,NULL,'admin','2011-04-13','student0230','enrolled','3','standard','\0',4),(591,0,NULL,NULL,'admin','2011-04-13','student0231','wait','3','pnp','\0',4),(592,0,NULL,NULL,'admin','2011-04-13','student0232','enrolled','3','standard','\0',4),(593,0,NULL,NULL,'admin','2011-04-13','student0233','wait','3','pnp','\0',4),(594,0,NULL,NULL,'admin','2011-04-13','student0234','enrolled','3','standard','\0',4),(595,0,NULL,NULL,'admin','2011-04-13','student0235','wait','3','pnp','\0',4),(596,0,NULL,NULL,'admin','2011-04-13','student0236','enrolled','3','standard','\0',4),(597,0,NULL,NULL,'admin','2011-04-13','student0237','wait','3','pnp','\0',4),(598,0,NULL,NULL,'admin','2011-04-13','student0238','enrolled','3','standard','\0',4),(599,0,NULL,NULL,'admin','2011-04-13','student0239','wait','3','pnp','\0',4),(600,0,NULL,NULL,'admin','2011-04-13','student0240','enrolled','3','standard','\0',4),(601,0,NULL,NULL,'admin','2011-04-13','student0241','wait','3','pnp','\0',4),(602,0,NULL,NULL,'admin','2011-04-13','student0242','enrolled','3','standard','\0',4),(603,0,NULL,NULL,'admin','2011-04-13','student0243','wait','3','pnp','\0',4),(604,0,NULL,NULL,'admin','2011-04-13','student0244','enrolled','3','standard','\0',4),(605,0,NULL,NULL,'admin','2011-04-13','student0245','wait','3','pnp','\0',4),(606,0,NULL,NULL,'admin','2011-04-13','student0246','enrolled','3','standard','\0',4),(607,0,NULL,NULL,'admin','2011-04-13','student0247','wait','3','pnp','\0',4),(608,0,NULL,NULL,'admin','2011-04-13','student0248','enrolled','3','standard','\0',4),(609,0,NULL,NULL,'admin','2011-04-13','student0249','wait','3','pnp','\0',4),(610,0,NULL,NULL,'admin','2011-04-13','student0250','enrolled','3','standard','\0',4),(611,0,NULL,NULL,'admin','2011-04-13','student0251','wait','3','pnp','\0',4),(612,0,NULL,NULL,'admin','2011-04-13','student0252','enrolled','3','standard','\0',4),(613,0,NULL,NULL,'admin','2011-04-13','student0253','wait','3','pnp','\0',4),(614,0,NULL,NULL,'admin','2011-04-13','student0254','enrolled','3','standard','\0',4),(615,0,NULL,NULL,'admin','2011-04-13','student0255','wait','3','pnp','\0',4),(616,0,NULL,NULL,'admin','2011-04-13','student0256','enrolled','3','standard','\0',4),(617,0,NULL,NULL,'admin','2011-04-13','student0257','wait','3','pnp','\0',4),(618,0,NULL,NULL,'admin','2011-04-13','student0258','enrolled','3','standard','\0',4),(619,0,NULL,NULL,'admin','2011-04-13','student0259','wait','3','pnp','\0',4),(620,0,NULL,NULL,'admin','2011-04-13','student0260','enrolled','3','standard','\0',4),(621,0,NULL,NULL,'admin','2011-04-13','student0261','wait','3','pnp','\0',4),(622,0,NULL,NULL,'admin','2011-04-13','student0262','enrolled','3','standard','\0',4),(623,0,NULL,NULL,'admin','2011-04-13','student0263','wait','3','pnp','\0',4),(624,0,NULL,NULL,'admin','2011-04-13','student0264','enrolled','3','standard','\0',4),(625,0,NULL,NULL,'admin','2011-04-13','student0265','wait','3','pnp','\0',4),(626,0,NULL,NULL,'admin','2011-04-13','student0266','enrolled','3','standard','\0',4),(627,0,NULL,NULL,'admin','2011-04-13','student0267','wait','3','pnp','\0',4),(628,0,NULL,NULL,'admin','2011-04-13','student0268','enrolled','3','standard','\0',4),(629,0,NULL,NULL,'admin','2011-04-13','student0269','wait','3','pnp','\0',4),(630,0,NULL,NULL,'admin','2011-04-13','student0270','enrolled','3','standard','\0',4),(631,0,NULL,NULL,'admin','2011-04-13','student0271','wait','3','pnp','\0',4),(632,0,NULL,NULL,'admin','2011-04-13','student0272','enrolled','3','standard','\0',4),(633,0,NULL,NULL,'admin','2011-04-13','student0273','wait','3','pnp','\0',4),(634,0,NULL,NULL,'admin','2011-04-13','student0274','enrolled','3','standard','\0',4),(635,0,NULL,NULL,'admin','2011-04-13','student0275','wait','3','pnp','\0',4),(636,0,NULL,NULL,'admin','2011-04-13','student0276','enrolled','3','standard','\0',4),(637,0,NULL,NULL,'admin','2011-04-13','student0277','wait','3','pnp','\0',4),(638,0,NULL,NULL,'admin','2011-04-13','student0278','enrolled','3','standard','\0',4),(639,0,NULL,NULL,'admin','2011-04-13','student0279','wait','3','pnp','\0',4),(640,0,NULL,NULL,'admin','2011-04-13','student0280','enrolled','3','standard','\0',4),(641,0,NULL,NULL,'admin','2011-04-13','student0281','wait','3','pnp','\0',4),(642,0,NULL,NULL,'admin','2011-04-13','student0282','enrolled','3','standard','\0',4),(643,0,NULL,NULL,'admin','2011-04-13','student0283','wait','3','pnp','\0',4),(644,0,NULL,NULL,'admin','2011-04-13','student0284','enrolled','3','standard','\0',4),(645,0,NULL,NULL,'admin','2011-04-13','student0285','wait','3','pnp','\0',4),(646,0,NULL,NULL,'admin','2011-04-13','student0286','enrolled','3','standard','\0',4),(647,0,NULL,NULL,'admin','2011-04-13','student0287','wait','3','pnp','\0',4),(648,0,NULL,NULL,'admin','2011-04-13','student0288','enrolled','3','standard','\0',4),(649,0,NULL,NULL,'admin','2011-04-13','student0289','wait','3','pnp','\0',4),(650,0,NULL,NULL,'admin','2011-04-13','student0290','enrolled','3','standard','\0',4),(651,0,NULL,NULL,'admin','2011-04-13','student0291','wait','3','pnp','\0',4),(652,0,NULL,NULL,'admin','2011-04-13','student0292','enrolled','3','standard','\0',4),(653,0,NULL,NULL,'admin','2011-04-13','student0293','wait','3','pnp','\0',4),(654,0,NULL,NULL,'admin','2011-04-13','student0294','enrolled','3','standard','\0',4),(655,0,NULL,NULL,'admin','2011-04-13','student0295','wait','3','pnp','\0',4),(656,0,NULL,NULL,'admin','2011-04-13','student0296','enrolled','3','standard','\0',4),(657,0,NULL,NULL,'admin','2011-04-13','student0297','wait','3','pnp','\0',4),(658,0,NULL,NULL,'admin','2011-04-13','student0298','enrolled','3','standard','\0',4),(659,0,NULL,NULL,'admin','2011-04-13','student0299','wait','3','pnp','\0',4),(660,0,NULL,NULL,'admin','2011-04-13','student0300','enrolled','3','standard','\0',4),(661,0,NULL,NULL,'admin','2011-04-13','student0301','wait','3','pnp','\0',4),(662,0,NULL,NULL,'admin','2011-04-13','student0302','enrolled','3','standard','\0',4),(663,0,NULL,NULL,'admin','2011-04-13','student0303','wait','3','pnp','\0',4),(664,0,NULL,NULL,'admin','2011-04-13','student0304','enrolled','3','standard','\0',4),(665,0,NULL,NULL,'admin','2011-04-13','student0305','wait','3','pnp','\0',4),(666,0,NULL,NULL,'admin','2011-04-13','student0306','enrolled','3','standard','\0',4),(667,0,NULL,NULL,'admin','2011-04-13','student0307','wait','3','pnp','\0',4),(668,0,NULL,NULL,'admin','2011-04-13','student0308','enrolled','3','standard','\0',4),(669,0,NULL,NULL,'admin','2011-04-13','student0309','wait','3','pnp','\0',4),(670,0,NULL,NULL,'admin','2011-04-13','student0310','enrolled','3','standard','\0',4),(671,0,NULL,NULL,'admin','2011-04-13','student0311','wait','3','pnp','\0',4),(672,0,NULL,NULL,'admin','2011-04-13','student0312','enrolled','3','standard','\0',4),(673,0,NULL,NULL,'admin','2011-04-13','student0313','wait','3','pnp','\0',4),(674,0,NULL,NULL,'admin','2011-04-13','student0314','enrolled','3','standard','\0',4),(675,0,NULL,NULL,'admin','2011-04-13','student0315','wait','3','pnp','\0',4),(676,0,NULL,NULL,'admin','2011-04-13','student0316','enrolled','3','standard','\0',4),(677,0,NULL,NULL,'admin','2011-04-13','student0317','wait','3','pnp','\0',4),(678,0,NULL,NULL,'admin','2011-04-13','student0318','enrolled','3','standard','\0',4),(679,0,NULL,NULL,'admin','2011-04-13','student0319','wait','3','pnp','\0',4),(680,0,NULL,NULL,'admin','2011-04-13','student0320','enrolled','3','standard','\0',4),(681,0,NULL,NULL,'admin','2011-04-13','student0321','wait','3','pnp','\0',4),(682,0,NULL,NULL,'admin','2011-04-13','student0322','enrolled','3','standard','\0',4),(683,0,NULL,NULL,'admin','2011-04-13','student0323','wait','3','pnp','\0',4),(684,0,NULL,NULL,'admin','2011-04-13','student0324','enrolled','3','standard','\0',4),(685,0,NULL,NULL,'admin','2011-04-13','student0325','wait','3','pnp','\0',4),(686,0,NULL,NULL,'admin','2011-04-13','student0326','enrolled','3','standard','\0',4),(687,0,NULL,NULL,'admin','2011-04-13','student0327','wait','3','pnp','\0',4),(688,0,NULL,NULL,'admin','2011-04-13','student0328','enrolled','3','standard','\0',4),(689,0,NULL,NULL,'admin','2011-04-13','student0329','wait','3','pnp','\0',4),(690,0,NULL,NULL,'admin','2011-04-13','student0330','enrolled','3','standard','\0',4),(691,0,NULL,NULL,'admin','2011-04-13','student0331','wait','3','pnp','\0',4),(692,0,NULL,NULL,'admin','2011-04-13','student0332','enrolled','3','standard','\0',4),(693,0,NULL,NULL,'admin','2011-04-13','student0333','wait','3','pnp','\0',4),(694,0,NULL,NULL,'admin','2011-04-13','student0334','enrolled','3','standard','\0',4),(695,0,NULL,NULL,'admin','2011-04-13','student0335','wait','3','pnp','\0',4),(696,0,NULL,NULL,'admin','2011-04-13','student0336','enrolled','3','standard','\0',4),(697,0,NULL,NULL,'admin','2011-04-13','student0337','wait','3','pnp','\0',4),(698,0,NULL,NULL,'admin','2011-04-13','student0338','enrolled','3','standard','\0',4),(699,0,NULL,NULL,'admin','2011-04-13','student0339','wait','3','pnp','\0',4),(700,0,NULL,NULL,'admin','2011-04-13','student0340','enrolled','3','standard','\0',4),(701,0,NULL,NULL,'admin','2011-04-13','student0341','wait','3','pnp','\0',4),(702,0,NULL,NULL,'admin','2011-04-13','student0342','enrolled','3','standard','\0',4),(703,0,NULL,NULL,'admin','2011-04-13','student0343','wait','3','pnp','\0',4),(704,0,NULL,NULL,'admin','2011-04-13','student0344','enrolled','3','standard','\0',4),(705,0,NULL,NULL,'admin','2011-04-13','student0345','wait','3','pnp','\0',4),(706,0,NULL,NULL,'admin','2011-04-13','student0346','enrolled','3','standard','\0',4),(707,0,NULL,NULL,'admin','2011-04-13','student0347','wait','3','pnp','\0',4),(708,0,NULL,NULL,'admin','2011-04-13','student0348','enrolled','3','standard','\0',4),(709,0,NULL,NULL,'admin','2011-04-13','student0349','wait','3','pnp','\0',4),(710,0,NULL,NULL,'admin','2011-04-13','student0350','enrolled','3','standard','\0',4),(711,0,NULL,NULL,'admin','2011-04-13','student0351','wait','3','pnp','\0',4),(712,0,NULL,NULL,'admin','2011-04-13','student0352','enrolled','3','standard','\0',4),(713,0,NULL,NULL,'admin','2011-04-13','student0353','wait','3','pnp','\0',4),(714,0,NULL,NULL,'admin','2011-04-13','student0354','enrolled','3','standard','\0',4),(715,0,NULL,NULL,'admin','2011-04-13','student0355','wait','3','pnp','\0',4),(716,0,NULL,NULL,'admin','2011-04-13','student0356','enrolled','3','standard','\0',4),(717,0,NULL,NULL,'admin','2011-04-13','student0357','wait','3','pnp','\0',4),(718,0,NULL,NULL,'admin','2011-04-13','student0358','enrolled','3','standard','\0',4),(719,0,NULL,NULL,'admin','2011-04-13','student0359','wait','3','pnp','\0',4),(720,0,NULL,NULL,'admin','2011-04-13','student0360','enrolled','3','standard','\0',4),(721,0,NULL,NULL,'admin','2011-04-13','student0001','wait','3','pnp','\0',5),(722,0,NULL,NULL,'admin','2011-04-13','student0002','enrolled','3','standard','\0',5),(723,0,NULL,NULL,'admin','2011-04-13','student0003','wait','3','pnp','\0',5),(724,0,NULL,NULL,'admin','2011-04-13','student0004','enrolled','3','standard','\0',5),(725,0,NULL,NULL,'admin','2011-04-13','student0005','wait','3','pnp','\0',5),(726,0,NULL,NULL,'admin','2011-04-13','student0006','enrolled','3','standard','\0',5),(727,0,NULL,NULL,'admin','2011-04-13','student0007','wait','3','pnp','\0',5),(728,0,NULL,NULL,'admin','2011-04-13','student0008','enrolled','3','standard','\0',5),(729,0,NULL,NULL,'admin','2011-04-13','student0009','wait','3','pnp','\0',5),(730,0,NULL,NULL,'admin','2011-04-13','student0010','enrolled','3','standard','\0',5),(731,0,NULL,NULL,'admin','2011-04-13','student0011','wait','3','pnp','\0',5),(732,0,NULL,NULL,'admin','2011-04-13','student0012','enrolled','3','standard','\0',5),(733,0,NULL,NULL,'admin','2011-04-13','student0013','wait','3','pnp','\0',5),(734,0,NULL,NULL,'admin','2011-04-13','student0014','enrolled','3','standard','\0',5),(735,0,NULL,NULL,'admin','2011-04-13','student0015','wait','3','pnp','\0',5),(736,0,NULL,NULL,'admin','2011-04-13','student0016','enrolled','3','standard','\0',5),(737,0,NULL,NULL,'admin','2011-04-13','student0017','wait','3','pnp','\0',5),(738,0,NULL,NULL,'admin','2011-04-13','student0018','enrolled','3','standard','\0',5),(739,0,NULL,NULL,'admin','2011-04-13','student0019','wait','3','pnp','\0',5),(740,0,NULL,NULL,'admin','2011-04-13','student0020','enrolled','3','standard','\0',5),(741,0,NULL,NULL,'admin','2011-04-13','student0021','wait','3','pnp','\0',5),(742,0,NULL,NULL,'admin','2011-04-13','student0022','enrolled','3','standard','\0',5),(743,0,NULL,NULL,'admin','2011-04-13','student0023','wait','3','pnp','\0',5),(744,0,NULL,NULL,'admin','2011-04-13','student0024','enrolled','3','standard','\0',5),(745,0,NULL,NULL,'admin','2011-04-13','student0025','wait','3','pnp','\0',5),(746,0,NULL,NULL,'admin','2011-04-13','student0026','enrolled','3','standard','\0',5),(747,0,NULL,NULL,'admin','2011-04-13','student0027','wait','3','pnp','\0',5),(748,0,NULL,NULL,'admin','2011-04-13','student0028','enrolled','3','standard','\0',5),(749,0,NULL,NULL,'admin','2011-04-13','student0029','wait','3','pnp','\0',5),(750,0,NULL,NULL,'admin','2011-04-13','student0030','enrolled','3','standard','\0',5),(751,0,NULL,NULL,'admin','2011-04-13','student0031','wait','3','pnp','\0',5),(752,0,NULL,NULL,'admin','2011-04-13','student0032','enrolled','3','standard','\0',5),(753,0,NULL,NULL,'admin','2011-04-13','student0033','wait','3','pnp','\0',5),(754,0,NULL,NULL,'admin','2011-04-13','student0034','enrolled','3','standard','\0',5),(755,0,NULL,NULL,'admin','2011-04-13','student0035','wait','3','pnp','\0',5),(756,0,NULL,NULL,'admin','2011-04-13','student0036','enrolled','3','standard','\0',5),(757,0,NULL,NULL,'admin','2011-04-13','student0037','wait','3','pnp','\0',5),(758,0,NULL,NULL,'admin','2011-04-13','student0038','enrolled','3','standard','\0',5),(759,0,NULL,NULL,'admin','2011-04-13','student0039','wait','3','pnp','\0',5),(760,0,NULL,NULL,'admin','2011-04-13','student0040','enrolled','3','standard','\0',5),(761,0,NULL,NULL,'admin','2011-04-13','student0041','wait','3','pnp','\0',5),(762,0,NULL,NULL,'admin','2011-04-13','student0042','enrolled','3','standard','\0',5),(763,0,NULL,NULL,'admin','2011-04-13','student0043','wait','3','pnp','\0',5),(764,0,NULL,NULL,'admin','2011-04-13','student0044','enrolled','3','standard','\0',5),(765,0,NULL,NULL,'admin','2011-04-13','student0045','wait','3','pnp','\0',5),(766,0,NULL,NULL,'admin','2011-04-13','student0046','enrolled','3','standard','\0',5),(767,0,NULL,NULL,'admin','2011-04-13','student0047','wait','3','pnp','\0',5),(768,0,NULL,NULL,'admin','2011-04-13','student0048','enrolled','3','standard','\0',5),(769,0,NULL,NULL,'admin','2011-04-13','student0049','wait','3','pnp','\0',5),(770,0,NULL,NULL,'admin','2011-04-13','student0050','enrolled','3','standard','\0',5),(771,0,NULL,NULL,'admin','2011-04-13','student0051','wait','3','pnp','\0',5),(772,0,NULL,NULL,'admin','2011-04-13','student0052','enrolled','3','standard','\0',5),(773,0,NULL,NULL,'admin','2011-04-13','student0053','wait','3','pnp','\0',5),(774,0,NULL,NULL,'admin','2011-04-13','student0054','enrolled','3','standard','\0',5),(775,0,NULL,NULL,'admin','2011-04-13','student0055','wait','3','pnp','\0',5),(776,0,NULL,NULL,'admin','2011-04-13','student0056','enrolled','3','standard','\0',5),(777,0,NULL,NULL,'admin','2011-04-13','student0057','wait','3','pnp','\0',5),(778,0,NULL,NULL,'admin','2011-04-13','student0058','enrolled','3','standard','\0',5),(779,0,NULL,NULL,'admin','2011-04-13','student0059','wait','3','pnp','\0',5),(780,0,NULL,NULL,'admin','2011-04-13','student0060','enrolled','3','standard','\0',5),(781,0,NULL,NULL,'admin','2011-04-13','student0061','wait','3','pnp','\0',5),(782,0,NULL,NULL,'admin','2011-04-13','student0062','enrolled','3','standard','\0',5),(783,0,NULL,NULL,'admin','2011-04-13','student0063','wait','3','pnp','\0',5),(784,0,NULL,NULL,'admin','2011-04-13','student0064','enrolled','3','standard','\0',5),(785,0,NULL,NULL,'admin','2011-04-13','student0065','wait','3','pnp','\0',5),(786,0,NULL,NULL,'admin','2011-04-13','student0066','enrolled','3','standard','\0',5),(787,0,NULL,NULL,'admin','2011-04-13','student0067','wait','3','pnp','\0',5),(788,0,NULL,NULL,'admin','2011-04-13','student0068','enrolled','3','standard','\0',5),(789,0,NULL,NULL,'admin','2011-04-13','student0069','wait','3','pnp','\0',5),(790,0,NULL,NULL,'admin','2011-04-13','student0070','enrolled','3','standard','\0',5),(791,0,NULL,NULL,'admin','2011-04-13','student0071','wait','3','pnp','\0',5),(792,0,NULL,NULL,'admin','2011-04-13','student0072','enrolled','3','standard','\0',5),(793,0,NULL,NULL,'admin','2011-04-13','student0073','wait','3','pnp','\0',5),(794,0,NULL,NULL,'admin','2011-04-13','student0074','enrolled','3','standard','\0',5),(795,0,NULL,NULL,'admin','2011-04-13','student0075','wait','3','pnp','\0',5),(796,0,NULL,NULL,'admin','2011-04-13','student0076','enrolled','3','standard','\0',5),(797,0,NULL,NULL,'admin','2011-04-13','student0077','wait','3','pnp','\0',5),(798,0,NULL,NULL,'admin','2011-04-13','student0078','enrolled','3','standard','\0',5),(799,0,NULL,NULL,'admin','2011-04-13','student0079','wait','3','pnp','\0',5),(800,0,NULL,NULL,'admin','2011-04-13','student0080','enrolled','3','standard','\0',5),(801,0,NULL,NULL,'admin','2011-04-13','student0081','wait','3','pnp','\0',5),(802,0,NULL,NULL,'admin','2011-04-13','student0082','enrolled','3','standard','\0',5),(803,0,NULL,NULL,'admin','2011-04-13','student0083','wait','3','pnp','\0',5),(804,0,NULL,NULL,'admin','2011-04-13','student0084','enrolled','3','standard','\0',5),(805,0,NULL,NULL,'admin','2011-04-13','student0085','wait','3','pnp','\0',5),(806,0,NULL,NULL,'admin','2011-04-13','student0086','enrolled','3','standard','\0',5),(807,0,NULL,NULL,'admin','2011-04-13','student0087','wait','3','pnp','\0',5),(808,0,NULL,NULL,'admin','2011-04-13','student0088','enrolled','3','standard','\0',5),(809,0,NULL,NULL,'admin','2011-04-13','student0089','wait','3','pnp','\0',5),(810,0,NULL,NULL,'admin','2011-04-13','student0090','enrolled','3','standard','\0',5),(811,0,NULL,NULL,'admin','2011-04-13','student0091','wait','3','pnp','\0',5),(812,0,NULL,NULL,'admin','2011-04-13','student0092','enrolled','3','standard','\0',5),(813,0,NULL,NULL,'admin','2011-04-13','student0093','wait','3','pnp','\0',5),(814,0,NULL,NULL,'admin','2011-04-13','student0094','enrolled','3','standard','\0',5),(815,0,NULL,NULL,'admin','2011-04-13','student0095','wait','3','pnp','\0',5),(816,0,NULL,NULL,'admin','2011-04-13','student0096','enrolled','3','standard','\0',5),(817,0,NULL,NULL,'admin','2011-04-13','student0097','wait','3','pnp','\0',5),(818,0,NULL,NULL,'admin','2011-04-13','student0098','enrolled','3','standard','\0',5),(819,0,NULL,NULL,'admin','2011-04-13','student0099','wait','3','pnp','\0',5),(820,0,NULL,NULL,'admin','2011-04-13','student0100','enrolled','3','standard','\0',5),(821,0,NULL,NULL,'admin','2011-04-13','student0101','wait','3','pnp','\0',5),(822,0,NULL,NULL,'admin','2011-04-13','student0102','enrolled','3','standard','\0',5),(823,0,NULL,NULL,'admin','2011-04-13','student0103','wait','3','pnp','\0',5),(824,0,NULL,NULL,'admin','2011-04-13','student0104','enrolled','3','standard','\0',5),(825,0,NULL,NULL,'admin','2011-04-13','student0105','wait','3','pnp','\0',5),(826,0,NULL,NULL,'admin','2011-04-13','student0106','enrolled','3','standard','\0',5),(827,0,NULL,NULL,'admin','2011-04-13','student0107','wait','3','pnp','\0',5),(828,0,NULL,NULL,'admin','2011-04-13','student0108','enrolled','3','standard','\0',5),(829,0,NULL,NULL,'admin','2011-04-13','student0109','wait','3','pnp','\0',5),(830,0,NULL,NULL,'admin','2011-04-13','student0110','enrolled','3','standard','\0',5),(831,0,NULL,NULL,'admin','2011-04-13','student0111','wait','3','pnp','\0',5),(832,0,NULL,NULL,'admin','2011-04-13','student0112','enrolled','3','standard','\0',5),(833,0,NULL,NULL,'admin','2011-04-13','student0113','wait','3','pnp','\0',5),(834,0,NULL,NULL,'admin','2011-04-13','student0114','enrolled','3','standard','\0',5),(835,0,NULL,NULL,'admin','2011-04-13','student0115','wait','3','pnp','\0',5),(836,0,NULL,NULL,'admin','2011-04-13','student0116','enrolled','3','standard','\0',5),(837,0,NULL,NULL,'admin','2011-04-13','student0117','wait','3','pnp','\0',5),(838,0,NULL,NULL,'admin','2011-04-13','student0118','enrolled','3','standard','\0',5),(839,0,NULL,NULL,'admin','2011-04-13','student0119','wait','3','pnp','\0',5),(840,0,NULL,NULL,'admin','2011-04-13','student0120','enrolled','3','standard','\0',5),(841,0,NULL,NULL,'admin','2011-04-13','student0121','wait','3','pnp','\0',5),(842,0,NULL,NULL,'admin','2011-04-13','student0122','enrolled','3','standard','\0',5),(843,0,NULL,NULL,'admin','2011-04-13','student0123','wait','3','pnp','\0',5),(844,0,NULL,NULL,'admin','2011-04-13','student0124','enrolled','3','standard','\0',5),(845,0,NULL,NULL,'admin','2011-04-13','student0125','wait','3','pnp','\0',5),(846,0,NULL,NULL,'admin','2011-04-13','student0126','enrolled','3','standard','\0',5),(847,0,NULL,NULL,'admin','2011-04-13','student0127','wait','3','pnp','\0',5),(848,0,NULL,NULL,'admin','2011-04-13','student0128','enrolled','3','standard','\0',5),(849,0,NULL,NULL,'admin','2011-04-13','student0129','wait','3','pnp','\0',5),(850,0,NULL,NULL,'admin','2011-04-13','student0130','enrolled','3','standard','\0',5),(851,0,NULL,NULL,'admin','2011-04-13','student0131','wait','3','pnp','\0',5),(852,0,NULL,NULL,'admin','2011-04-13','student0132','enrolled','3','standard','\0',5),(853,0,NULL,NULL,'admin','2011-04-13','student0133','wait','3','pnp','\0',5),(854,0,NULL,NULL,'admin','2011-04-13','student0134','enrolled','3','standard','\0',5),(855,0,NULL,NULL,'admin','2011-04-13','student0135','wait','3','pnp','\0',5),(856,0,NULL,NULL,'admin','2011-04-13','student0136','enrolled','3','standard','\0',5),(857,0,NULL,NULL,'admin','2011-04-13','student0137','wait','3','pnp','\0',5),(858,0,NULL,NULL,'admin','2011-04-13','student0138','enrolled','3','standard','\0',5),(859,0,NULL,NULL,'admin','2011-04-13','student0139','wait','3','pnp','\0',5),(860,0,NULL,NULL,'admin','2011-04-13','student0140','enrolled','3','standard','\0',5),(861,0,NULL,NULL,'admin','2011-04-13','student0141','wait','3','pnp','\0',5),(862,0,NULL,NULL,'admin','2011-04-13','student0142','enrolled','3','standard','\0',5),(863,0,NULL,NULL,'admin','2011-04-13','student0143','wait','3','pnp','\0',5),(864,0,NULL,NULL,'admin','2011-04-13','student0144','enrolled','3','standard','\0',5),(865,0,NULL,NULL,'admin','2011-04-13','student0145','wait','3','pnp','\0',5),(866,0,NULL,NULL,'admin','2011-04-13','student0146','enrolled','3','standard','\0',5),(867,0,NULL,NULL,'admin','2011-04-13','student0147','wait','3','pnp','\0',5),(868,0,NULL,NULL,'admin','2011-04-13','student0148','enrolled','3','standard','\0',5),(869,0,NULL,NULL,'admin','2011-04-13','student0149','wait','3','pnp','\0',5),(870,0,NULL,NULL,'admin','2011-04-13','student0150','enrolled','3','standard','\0',5),(871,0,NULL,NULL,'admin','2011-04-13','student0151','wait','3','pnp','\0',5),(872,0,NULL,NULL,'admin','2011-04-13','student0152','enrolled','3','standard','\0',5),(873,0,NULL,NULL,'admin','2011-04-13','student0153','wait','3','pnp','\0',5),(874,0,NULL,NULL,'admin','2011-04-13','student0154','enrolled','3','standard','\0',5),(875,0,NULL,NULL,'admin','2011-04-13','student0155','wait','3','pnp','\0',5),(876,0,NULL,NULL,'admin','2011-04-13','student0156','enrolled','3','standard','\0',5),(877,0,NULL,NULL,'admin','2011-04-13','student0157','wait','3','pnp','\0',5),(878,0,NULL,NULL,'admin','2011-04-13','student0158','enrolled','3','standard','\0',5),(879,0,NULL,NULL,'admin','2011-04-13','student0159','wait','3','pnp','\0',5),(880,0,NULL,NULL,'admin','2011-04-13','student0160','enrolled','3','standard','\0',5),(881,0,NULL,NULL,'admin','2011-04-13','student0161','wait','3','pnp','\0',5),(882,0,NULL,NULL,'admin','2011-04-13','student0162','enrolled','3','standard','\0',5),(883,0,NULL,NULL,'admin','2011-04-13','student0163','wait','3','pnp','\0',5),(884,0,NULL,NULL,'admin','2011-04-13','student0164','enrolled','3','standard','\0',5),(885,0,NULL,NULL,'admin','2011-04-13','student0165','wait','3','pnp','\0',5),(886,0,NULL,NULL,'admin','2011-04-13','student0166','enrolled','3','standard','\0',5),(887,0,NULL,NULL,'admin','2011-04-13','student0167','wait','3','pnp','\0',5),(888,0,NULL,NULL,'admin','2011-04-13','student0168','enrolled','3','standard','\0',5),(889,0,NULL,NULL,'admin','2011-04-13','student0169','wait','3','pnp','\0',5),(890,0,NULL,NULL,'admin','2011-04-13','student0170','enrolled','3','standard','\0',5),(891,0,NULL,NULL,'admin','2011-04-13','student0171','wait','3','pnp','\0',5),(892,0,NULL,NULL,'admin','2011-04-13','student0172','enrolled','3','standard','\0',5),(893,0,NULL,NULL,'admin','2011-04-13','student0173','wait','3','pnp','\0',5),(894,0,NULL,NULL,'admin','2011-04-13','student0174','enrolled','3','standard','\0',5),(895,0,NULL,NULL,'admin','2011-04-13','student0175','wait','3','pnp','\0',5),(896,0,NULL,NULL,'admin','2011-04-13','student0176','enrolled','3','standard','\0',5),(897,0,NULL,NULL,'admin','2011-04-13','student0177','wait','3','pnp','\0',5),(898,0,NULL,NULL,'admin','2011-04-13','student0178','enrolled','3','standard','\0',5),(899,0,NULL,NULL,'admin','2011-04-13','student0179','wait','3','pnp','\0',5),(900,0,NULL,NULL,'admin','2011-04-13','student0180','enrolled','3','standard','\0',5),(901,0,NULL,NULL,'admin','2011-04-13','student0181','wait','3','pnp','\0',6),(902,0,NULL,NULL,'admin','2011-04-13','student0182','enrolled','3','standard','\0',6),(903,0,NULL,NULL,'admin','2011-04-13','student0183','wait','3','pnp','\0',6),(904,0,NULL,NULL,'admin','2011-04-13','student0184','enrolled','3','standard','\0',6),(905,0,NULL,NULL,'admin','2011-04-13','student0185','wait','3','pnp','\0',6),(906,0,NULL,NULL,'admin','2011-04-13','student0186','enrolled','3','standard','\0',6),(907,0,NULL,NULL,'admin','2011-04-13','student0187','wait','3','pnp','\0',6),(908,0,NULL,NULL,'admin','2011-04-13','student0188','enrolled','3','standard','\0',6),(909,0,NULL,NULL,'admin','2011-04-13','student0189','wait','3','pnp','\0',6),(910,0,NULL,NULL,'admin','2011-04-13','student0190','enrolled','3','standard','\0',6),(911,0,NULL,NULL,'admin','2011-04-13','student0191','wait','3','pnp','\0',6),(912,0,NULL,NULL,'admin','2011-04-13','student0192','enrolled','3','standard','\0',6),(913,0,NULL,NULL,'admin','2011-04-13','student0193','wait','3','pnp','\0',6),(914,0,NULL,NULL,'admin','2011-04-13','student0194','enrolled','3','standard','\0',6),(915,0,NULL,NULL,'admin','2011-04-13','student0195','wait','3','pnp','\0',6),(916,0,NULL,NULL,'admin','2011-04-13','student0196','enrolled','3','standard','\0',6),(917,0,NULL,NULL,'admin','2011-04-13','student0197','wait','3','pnp','\0',6),(918,0,NULL,NULL,'admin','2011-04-13','student0198','enrolled','3','standard','\0',6),(919,0,NULL,NULL,'admin','2011-04-13','student0199','wait','3','pnp','\0',6),(920,0,NULL,NULL,'admin','2011-04-13','student0200','enrolled','3','standard','\0',6),(921,0,NULL,NULL,'admin','2011-04-13','student0201','wait','3','pnp','\0',6),(922,0,NULL,NULL,'admin','2011-04-13','student0202','enrolled','3','standard','\0',6),(923,0,NULL,NULL,'admin','2011-04-13','student0203','wait','3','pnp','\0',6),(924,0,NULL,NULL,'admin','2011-04-13','student0204','enrolled','3','standard','\0',6),(925,0,NULL,NULL,'admin','2011-04-13','student0205','wait','3','pnp','\0',6),(926,0,NULL,NULL,'admin','2011-04-13','student0206','enrolled','3','standard','\0',6),(927,0,NULL,NULL,'admin','2011-04-13','student0207','wait','3','pnp','\0',6),(928,0,NULL,NULL,'admin','2011-04-13','student0208','enrolled','3','standard','\0',6),(929,0,NULL,NULL,'admin','2011-04-13','student0209','wait','3','pnp','\0',6),(930,0,NULL,NULL,'admin','2011-04-13','student0210','enrolled','3','standard','\0',6),(931,0,NULL,NULL,'admin','2011-04-13','student0211','wait','3','pnp','\0',6),(932,0,NULL,NULL,'admin','2011-04-13','student0212','enrolled','3','standard','\0',6),(933,0,NULL,NULL,'admin','2011-04-13','student0213','wait','3','pnp','\0',6),(934,0,NULL,NULL,'admin','2011-04-13','student0214','enrolled','3','standard','\0',6),(935,0,NULL,NULL,'admin','2011-04-13','student0215','wait','3','pnp','\0',6),(936,0,NULL,NULL,'admin','2011-04-13','student0216','enrolled','3','standard','\0',6),(937,0,NULL,NULL,'admin','2011-04-13','student0217','wait','3','pnp','\0',6),(938,0,NULL,NULL,'admin','2011-04-13','student0218','enrolled','3','standard','\0',6),(939,0,NULL,NULL,'admin','2011-04-13','student0219','wait','3','pnp','\0',6),(940,0,NULL,NULL,'admin','2011-04-13','student0220','enrolled','3','standard','\0',6),(941,0,NULL,NULL,'admin','2011-04-13','student0221','wait','3','pnp','\0',6),(942,0,NULL,NULL,'admin','2011-04-13','student0222','enrolled','3','standard','\0',6),(943,0,NULL,NULL,'admin','2011-04-13','student0223','wait','3','pnp','\0',6),(944,0,NULL,NULL,'admin','2011-04-13','student0224','enrolled','3','standard','\0',6),(945,0,NULL,NULL,'admin','2011-04-13','student0225','wait','3','pnp','\0',6),(946,0,NULL,NULL,'admin','2011-04-13','student0226','enrolled','3','standard','\0',6),(947,0,NULL,NULL,'admin','2011-04-13','student0227','wait','3','pnp','\0',6),(948,0,NULL,NULL,'admin','2011-04-13','student0228','enrolled','3','standard','\0',6),(949,0,NULL,NULL,'admin','2011-04-13','student0229','wait','3','pnp','\0',6),(950,0,NULL,NULL,'admin','2011-04-13','student0230','enrolled','3','standard','\0',6),(951,0,NULL,NULL,'admin','2011-04-13','student0231','wait','3','pnp','\0',6),(952,0,NULL,NULL,'admin','2011-04-13','student0232','enrolled','3','standard','\0',6),(953,0,NULL,NULL,'admin','2011-04-13','student0233','wait','3','pnp','\0',6),(954,0,NULL,NULL,'admin','2011-04-13','student0234','enrolled','3','standard','\0',6),(955,0,NULL,NULL,'admin','2011-04-13','student0235','wait','3','pnp','\0',6),(956,0,NULL,NULL,'admin','2011-04-13','student0236','enrolled','3','standard','\0',6),(957,0,NULL,NULL,'admin','2011-04-13','student0237','wait','3','pnp','\0',6),(958,0,NULL,NULL,'admin','2011-04-13','student0238','enrolled','3','standard','\0',6),(959,0,NULL,NULL,'admin','2011-04-13','student0239','wait','3','pnp','\0',6),(960,0,NULL,NULL,'admin','2011-04-13','student0240','enrolled','3','standard','\0',6),(961,0,NULL,NULL,'admin','2011-04-13','student0241','wait','3','pnp','\0',6),(962,0,NULL,NULL,'admin','2011-04-13','student0242','enrolled','3','standard','\0',6),(963,0,NULL,NULL,'admin','2011-04-13','student0243','wait','3','pnp','\0',6),(964,0,NULL,NULL,'admin','2011-04-13','student0244','enrolled','3','standard','\0',6),(965,0,NULL,NULL,'admin','2011-04-13','student0245','wait','3','pnp','\0',6),(966,0,NULL,NULL,'admin','2011-04-13','student0246','enrolled','3','standard','\0',6),(967,0,NULL,NULL,'admin','2011-04-13','student0247','wait','3','pnp','\0',6),(968,0,NULL,NULL,'admin','2011-04-13','student0248','enrolled','3','standard','\0',6),(969,0,NULL,NULL,'admin','2011-04-13','student0249','wait','3','pnp','\0',6),(970,0,NULL,NULL,'admin','2011-04-13','student0250','enrolled','3','standard','\0',6),(971,0,NULL,NULL,'admin','2011-04-13','student0251','wait','3','pnp','\0',6),(972,0,NULL,NULL,'admin','2011-04-13','student0252','enrolled','3','standard','\0',6),(973,0,NULL,NULL,'admin','2011-04-13','student0253','wait','3','pnp','\0',6),(974,0,NULL,NULL,'admin','2011-04-13','student0254','enrolled','3','standard','\0',6),(975,0,NULL,NULL,'admin','2011-04-13','student0255','wait','3','pnp','\0',6),(976,0,NULL,NULL,'admin','2011-04-13','student0256','enrolled','3','standard','\0',6),(977,0,NULL,NULL,'admin','2011-04-13','student0257','wait','3','pnp','\0',6),(978,0,NULL,NULL,'admin','2011-04-13','student0258','enrolled','3','standard','\0',6),(979,0,NULL,NULL,'admin','2011-04-13','student0259','wait','3','pnp','\0',6),(980,0,NULL,NULL,'admin','2011-04-13','student0260','enrolled','3','standard','\0',6),(981,0,NULL,NULL,'admin','2011-04-13','student0261','wait','3','pnp','\0',6),(982,0,NULL,NULL,'admin','2011-04-13','student0262','enrolled','3','standard','\0',6),(983,0,NULL,NULL,'admin','2011-04-13','student0263','wait','3','pnp','\0',6),(984,0,NULL,NULL,'admin','2011-04-13','student0264','enrolled','3','standard','\0',6),(985,0,NULL,NULL,'admin','2011-04-13','student0265','wait','3','pnp','\0',6),(986,0,NULL,NULL,'admin','2011-04-13','student0266','enrolled','3','standard','\0',6),(987,0,NULL,NULL,'admin','2011-04-13','student0267','wait','3','pnp','\0',6),(988,0,NULL,NULL,'admin','2011-04-13','student0268','enrolled','3','standard','\0',6),(989,0,NULL,NULL,'admin','2011-04-13','student0269','wait','3','pnp','\0',6),(990,0,NULL,NULL,'admin','2011-04-13','student0270','enrolled','3','standard','\0',6),(991,0,NULL,NULL,'admin','2011-04-13','student0271','wait','3','pnp','\0',6),(992,0,NULL,NULL,'admin','2011-04-13','student0272','enrolled','3','standard','\0',6),(993,0,NULL,NULL,'admin','2011-04-13','student0273','wait','3','pnp','\0',6),(994,0,NULL,NULL,'admin','2011-04-13','student0274','enrolled','3','standard','\0',6),(995,0,NULL,NULL,'admin','2011-04-13','student0275','wait','3','pnp','\0',6),(996,0,NULL,NULL,'admin','2011-04-13','student0276','enrolled','3','standard','\0',6),(997,0,NULL,NULL,'admin','2011-04-13','student0277','wait','3','pnp','\0',6),(998,0,NULL,NULL,'admin','2011-04-13','student0278','enrolled','3','standard','\0',6),(999,0,NULL,NULL,'admin','2011-04-13','student0279','wait','3','pnp','\0',6),(1000,0,NULL,NULL,'admin','2011-04-13','student0280','enrolled','3','standard','\0',6),(1001,0,NULL,NULL,'admin','2011-04-13','student0281','wait','3','pnp','\0',6),(1002,0,NULL,NULL,'admin','2011-04-13','student0282','enrolled','3','standard','\0',6),(1003,0,NULL,NULL,'admin','2011-04-13','student0283','wait','3','pnp','\0',6),(1004,0,NULL,NULL,'admin','2011-04-13','student0284','enrolled','3','standard','\0',6),(1005,0,NULL,NULL,'admin','2011-04-13','student0285','wait','3','pnp','\0',6),(1006,0,NULL,NULL,'admin','2011-04-13','student0286','enrolled','3','standard','\0',6),(1007,0,NULL,NULL,'admin','2011-04-13','student0287','wait','3','pnp','\0',6),(1008,0,NULL,NULL,'admin','2011-04-13','student0288','enrolled','3','standard','\0',6),(1009,0,NULL,NULL,'admin','2011-04-13','student0289','wait','3','pnp','\0',6),(1010,0,NULL,NULL,'admin','2011-04-13','student0290','enrolled','3','standard','\0',6),(1011,0,NULL,NULL,'admin','2011-04-13','student0291','wait','3','pnp','\0',6),(1012,0,NULL,NULL,'admin','2011-04-13','student0292','enrolled','3','standard','\0',6),(1013,0,NULL,NULL,'admin','2011-04-13','student0293','wait','3','pnp','\0',6),(1014,0,NULL,NULL,'admin','2011-04-13','student0294','enrolled','3','standard','\0',6),(1015,0,NULL,NULL,'admin','2011-04-13','student0295','wait','3','pnp','\0',6),(1016,0,NULL,NULL,'admin','2011-04-13','student0296','enrolled','3','standard','\0',6),(1017,0,NULL,NULL,'admin','2011-04-13','student0297','wait','3','pnp','\0',6),(1018,0,NULL,NULL,'admin','2011-04-13','student0298','enrolled','3','standard','\0',6),(1019,0,NULL,NULL,'admin','2011-04-13','student0299','wait','3','pnp','\0',6),(1020,0,NULL,NULL,'admin','2011-04-13','student0300','enrolled','3','standard','\0',6),(1021,0,NULL,NULL,'admin','2011-04-13','student0301','wait','3','pnp','\0',6),(1022,0,NULL,NULL,'admin','2011-04-13','student0302','enrolled','3','standard','\0',6),(1023,0,NULL,NULL,'admin','2011-04-13','student0303','wait','3','pnp','\0',6),(1024,0,NULL,NULL,'admin','2011-04-13','student0304','enrolled','3','standard','\0',6),(1025,0,NULL,NULL,'admin','2011-04-13','student0305','wait','3','pnp','\0',6),(1026,0,NULL,NULL,'admin','2011-04-13','student0306','enrolled','3','standard','\0',6),(1027,0,NULL,NULL,'admin','2011-04-13','student0307','wait','3','pnp','\0',6),(1028,0,NULL,NULL,'admin','2011-04-13','student0308','enrolled','3','standard','\0',6),(1029,0,NULL,NULL,'admin','2011-04-13','student0309','wait','3','pnp','\0',6),(1030,0,NULL,NULL,'admin','2011-04-13','student0310','enrolled','3','standard','\0',6),(1031,0,NULL,NULL,'admin','2011-04-13','student0311','wait','3','pnp','\0',6),(1032,0,NULL,NULL,'admin','2011-04-13','student0312','enrolled','3','standard','\0',6),(1033,0,NULL,NULL,'admin','2011-04-13','student0313','wait','3','pnp','\0',6),(1034,0,NULL,NULL,'admin','2011-04-13','student0314','enrolled','3','standard','\0',6),(1035,0,NULL,NULL,'admin','2011-04-13','student0315','wait','3','pnp','\0',6),(1036,0,NULL,NULL,'admin','2011-04-13','student0316','enrolled','3','standard','\0',6),(1037,0,NULL,NULL,'admin','2011-04-13','student0317','wait','3','pnp','\0',6),(1038,0,NULL,NULL,'admin','2011-04-13','student0318','enrolled','3','standard','\0',6),(1039,0,NULL,NULL,'admin','2011-04-13','student0319','wait','3','pnp','\0',6),(1040,0,NULL,NULL,'admin','2011-04-13','student0320','enrolled','3','standard','\0',6),(1041,0,NULL,NULL,'admin','2011-04-13','student0321','wait','3','pnp','\0',6),(1042,0,NULL,NULL,'admin','2011-04-13','student0322','enrolled','3','standard','\0',6),(1043,0,NULL,NULL,'admin','2011-04-13','student0323','wait','3','pnp','\0',6),(1044,0,NULL,NULL,'admin','2011-04-13','student0324','enrolled','3','standard','\0',6),(1045,0,NULL,NULL,'admin','2011-04-13','student0325','wait','3','pnp','\0',6),(1046,0,NULL,NULL,'admin','2011-04-13','student0326','enrolled','3','standard','\0',6),(1047,0,NULL,NULL,'admin','2011-04-13','student0327','wait','3','pnp','\0',6),(1048,0,NULL,NULL,'admin','2011-04-13','student0328','enrolled','3','standard','\0',6),(1049,0,NULL,NULL,'admin','2011-04-13','student0329','wait','3','pnp','\0',6),(1050,0,NULL,NULL,'admin','2011-04-13','student0330','enrolled','3','standard','\0',6),(1051,0,NULL,NULL,'admin','2011-04-13','student0331','wait','3','pnp','\0',6),(1052,0,NULL,NULL,'admin','2011-04-13','student0332','enrolled','3','standard','\0',6),(1053,0,NULL,NULL,'admin','2011-04-13','student0333','wait','3','pnp','\0',6),(1054,0,NULL,NULL,'admin','2011-04-13','student0334','enrolled','3','standard','\0',6),(1055,0,NULL,NULL,'admin','2011-04-13','student0335','wait','3','pnp','\0',6),(1056,0,NULL,NULL,'admin','2011-04-13','student0336','enrolled','3','standard','\0',6),(1057,0,NULL,NULL,'admin','2011-04-13','student0337','wait','3','pnp','\0',6),(1058,0,NULL,NULL,'admin','2011-04-13','student0338','enrolled','3','standard','\0',6),(1059,0,NULL,NULL,'admin','2011-04-13','student0339','wait','3','pnp','\0',6),(1060,0,NULL,NULL,'admin','2011-04-13','student0340','enrolled','3','standard','\0',6),(1061,0,NULL,NULL,'admin','2011-04-13','student0341','wait','3','pnp','\0',6),(1062,0,NULL,NULL,'admin','2011-04-13','student0342','enrolled','3','standard','\0',6),(1063,0,NULL,NULL,'admin','2011-04-13','student0343','wait','3','pnp','\0',6),(1064,0,NULL,NULL,'admin','2011-04-13','student0344','enrolled','3','standard','\0',6),(1065,0,NULL,NULL,'admin','2011-04-13','student0345','wait','3','pnp','\0',6),(1066,0,NULL,NULL,'admin','2011-04-13','student0346','enrolled','3','standard','\0',6),(1067,0,NULL,NULL,'admin','2011-04-13','student0347','wait','3','pnp','\0',6),(1068,0,NULL,NULL,'admin','2011-04-13','student0348','enrolled','3','standard','\0',6),(1069,0,NULL,NULL,'admin','2011-04-13','student0349','wait','3','pnp','\0',6),(1070,0,NULL,NULL,'admin','2011-04-13','student0350','enrolled','3','standard','\0',6),(1071,0,NULL,NULL,'admin','2011-04-13','student0351','wait','3','pnp','\0',6),(1072,0,NULL,NULL,'admin','2011-04-13','student0352','enrolled','3','standard','\0',6),(1073,0,NULL,NULL,'admin','2011-04-13','student0353','wait','3','pnp','\0',6),(1074,0,NULL,NULL,'admin','2011-04-13','student0354','enrolled','3','standard','\0',6),(1075,0,NULL,NULL,'admin','2011-04-13','student0355','wait','3','pnp','\0',6),(1076,0,NULL,NULL,'admin','2011-04-13','student0356','enrolled','3','standard','\0',6),(1077,0,NULL,NULL,'admin','2011-04-13','student0357','wait','3','pnp','\0',6),(1078,0,NULL,NULL,'admin','2011-04-13','student0358','enrolled','3','standard','\0',6),(1079,0,NULL,NULL,'admin','2011-04-13','student0359','wait','3','pnp','\0',6),(1080,0,NULL,NULL,'admin','2011-04-13','student0360','enrolled','3','standard','\0',6),(1081,0,NULL,NULL,'admin','2011-04-13','student0001','wait','3','pnp','\0',7),(1082,0,NULL,NULL,'admin','2011-04-13','student0002','enrolled','3','standard','\0',7),(1083,0,NULL,NULL,'admin','2011-04-13','student0003','wait','3','pnp','\0',7),(1084,0,NULL,NULL,'admin','2011-04-13','student0004','enrolled','3','standard','\0',7),(1085,0,NULL,NULL,'admin','2011-04-13','student0005','wait','3','pnp','\0',7),(1086,0,NULL,NULL,'admin','2011-04-13','student0006','enrolled','3','standard','\0',7),(1087,0,NULL,NULL,'admin','2011-04-13','student0007','wait','3','pnp','\0',7),(1088,0,NULL,NULL,'admin','2011-04-13','student0008','enrolled','3','standard','\0',7),(1089,0,NULL,NULL,'admin','2011-04-13','student0009','wait','3','pnp','\0',7),(1090,0,NULL,NULL,'admin','2011-04-13','student0010','enrolled','3','standard','\0',7),(1091,0,NULL,NULL,'admin','2011-04-13','student0011','wait','3','pnp','\0',7),(1092,0,NULL,NULL,'admin','2011-04-13','student0012','enrolled','3','standard','\0',7),(1093,0,NULL,NULL,'admin','2011-04-13','student0013','wait','3','pnp','\0',7),(1094,0,NULL,NULL,'admin','2011-04-13','student0014','enrolled','3','standard','\0',7),(1095,0,NULL,NULL,'admin','2011-04-13','student0015','wait','3','pnp','\0',7),(1096,0,NULL,NULL,'admin','2011-04-13','student0016','enrolled','3','standard','\0',7),(1097,0,NULL,NULL,'admin','2011-04-13','student0017','wait','3','pnp','\0',7),(1098,0,NULL,NULL,'admin','2011-04-13','student0018','enrolled','3','standard','\0',7),(1099,0,NULL,NULL,'admin','2011-04-13','student0019','wait','3','pnp','\0',7),(1100,0,NULL,NULL,'admin','2011-04-13','student0020','enrolled','3','standard','\0',7),(1101,0,NULL,NULL,'admin','2011-04-13','student0021','wait','3','pnp','\0',7),(1102,0,NULL,NULL,'admin','2011-04-13','student0022','enrolled','3','standard','\0',7),(1103,0,NULL,NULL,'admin','2011-04-13','student0023','wait','3','pnp','\0',7),(1104,0,NULL,NULL,'admin','2011-04-13','student0024','enrolled','3','standard','\0',7),(1105,0,NULL,NULL,'admin','2011-04-13','student0025','wait','3','pnp','\0',7),(1106,0,NULL,NULL,'admin','2011-04-13','student0026','enrolled','3','standard','\0',7),(1107,0,NULL,NULL,'admin','2011-04-13','student0027','wait','3','pnp','\0',7),(1108,0,NULL,NULL,'admin','2011-04-13','student0028','enrolled','3','standard','\0',7),(1109,0,NULL,NULL,'admin','2011-04-13','student0029','wait','3','pnp','\0',7),(1110,0,NULL,NULL,'admin','2011-04-13','student0030','enrolled','3','standard','\0',7),(1111,0,NULL,NULL,'admin','2011-04-13','student0031','wait','3','pnp','\0',7),(1112,0,NULL,NULL,'admin','2011-04-13','student0032','enrolled','3','standard','\0',7),(1113,0,NULL,NULL,'admin','2011-04-13','student0033','wait','3','pnp','\0',7),(1114,0,NULL,NULL,'admin','2011-04-13','student0034','enrolled','3','standard','\0',7),(1115,0,NULL,NULL,'admin','2011-04-13','student0035','wait','3','pnp','\0',7),(1116,0,NULL,NULL,'admin','2011-04-13','student0036','enrolled','3','standard','\0',7),(1117,0,NULL,NULL,'admin','2011-04-13','student0037','wait','3','pnp','\0',7),(1118,0,NULL,NULL,'admin','2011-04-13','student0038','enrolled','3','standard','\0',7),(1119,0,NULL,NULL,'admin','2011-04-13','student0039','wait','3','pnp','\0',7),(1120,0,NULL,NULL,'admin','2011-04-13','student0040','enrolled','3','standard','\0',7),(1121,0,NULL,NULL,'admin','2011-04-13','student0041','wait','3','pnp','\0',7),(1122,0,NULL,NULL,'admin','2011-04-13','student0042','enrolled','3','standard','\0',7),(1123,0,NULL,NULL,'admin','2011-04-13','student0043','wait','3','pnp','\0',7),(1124,0,NULL,NULL,'admin','2011-04-13','student0044','enrolled','3','standard','\0',7),(1125,0,NULL,NULL,'admin','2011-04-13','student0045','wait','3','pnp','\0',7),(1126,0,NULL,NULL,'admin','2011-04-13','student0046','enrolled','3','standard','\0',7),(1127,0,NULL,NULL,'admin','2011-04-13','student0047','wait','3','pnp','\0',7),(1128,0,NULL,NULL,'admin','2011-04-13','student0048','enrolled','3','standard','\0',7),(1129,0,NULL,NULL,'admin','2011-04-13','student0049','wait','3','pnp','\0',7),(1130,0,NULL,NULL,'admin','2011-04-13','student0050','enrolled','3','standard','\0',7),(1131,0,NULL,NULL,'admin','2011-04-13','student0051','wait','3','pnp','\0',7),(1132,0,NULL,NULL,'admin','2011-04-13','student0052','enrolled','3','standard','\0',7),(1133,0,NULL,NULL,'admin','2011-04-13','student0053','wait','3','pnp','\0',7),(1134,0,NULL,NULL,'admin','2011-04-13','student0054','enrolled','3','standard','\0',7),(1135,0,NULL,NULL,'admin','2011-04-13','student0055','wait','3','pnp','\0',7),(1136,0,NULL,NULL,'admin','2011-04-13','student0056','enrolled','3','standard','\0',7),(1137,0,NULL,NULL,'admin','2011-04-13','student0057','wait','3','pnp','\0',7),(1138,0,NULL,NULL,'admin','2011-04-13','student0058','enrolled','3','standard','\0',7),(1139,0,NULL,NULL,'admin','2011-04-13','student0059','wait','3','pnp','\0',7),(1140,0,NULL,NULL,'admin','2011-04-13','student0060','enrolled','3','standard','\0',7),(1141,0,NULL,NULL,'admin','2011-04-13','student0061','wait','3','pnp','\0',7),(1142,0,NULL,NULL,'admin','2011-04-13','student0062','enrolled','3','standard','\0',7),(1143,0,NULL,NULL,'admin','2011-04-13','student0063','wait','3','pnp','\0',7),(1144,0,NULL,NULL,'admin','2011-04-13','student0064','enrolled','3','standard','\0',7),(1145,0,NULL,NULL,'admin','2011-04-13','student0065','wait','3','pnp','\0',7),(1146,0,NULL,NULL,'admin','2011-04-13','student0066','enrolled','3','standard','\0',7),(1147,0,NULL,NULL,'admin','2011-04-13','student0067','wait','3','pnp','\0',7),(1148,0,NULL,NULL,'admin','2011-04-13','student0068','enrolled','3','standard','\0',7),(1149,0,NULL,NULL,'admin','2011-04-13','student0069','wait','3','pnp','\0',7),(1150,0,NULL,NULL,'admin','2011-04-13','student0070','enrolled','3','standard','\0',7),(1151,0,NULL,NULL,'admin','2011-04-13','student0071','wait','3','pnp','\0',7),(1152,0,NULL,NULL,'admin','2011-04-13','student0072','enrolled','3','standard','\0',7),(1153,0,NULL,NULL,'admin','2011-04-13','student0073','wait','3','pnp','\0',7),(1154,0,NULL,NULL,'admin','2011-04-13','student0074','enrolled','3','standard','\0',7),(1155,0,NULL,NULL,'admin','2011-04-13','student0075','wait','3','pnp','\0',7),(1156,0,NULL,NULL,'admin','2011-04-13','student0076','enrolled','3','standard','\0',7),(1157,0,NULL,NULL,'admin','2011-04-13','student0077','wait','3','pnp','\0',7),(1158,0,NULL,NULL,'admin','2011-04-13','student0078','enrolled','3','standard','\0',7),(1159,0,NULL,NULL,'admin','2011-04-13','student0079','wait','3','pnp','\0',7),(1160,0,NULL,NULL,'admin','2011-04-13','student0080','enrolled','3','standard','\0',7),(1161,0,NULL,NULL,'admin','2011-04-13','student0081','wait','3','pnp','\0',7),(1162,0,NULL,NULL,'admin','2011-04-13','student0082','enrolled','3','standard','\0',7),(1163,0,NULL,NULL,'admin','2011-04-13','student0083','wait','3','pnp','\0',7),(1164,0,NULL,NULL,'admin','2011-04-13','student0084','enrolled','3','standard','\0',7),(1165,0,NULL,NULL,'admin','2011-04-13','student0085','wait','3','pnp','\0',7),(1166,0,NULL,NULL,'admin','2011-04-13','student0086','enrolled','3','standard','\0',7),(1167,0,NULL,NULL,'admin','2011-04-13','student0087','wait','3','pnp','\0',7),(1168,0,NULL,NULL,'admin','2011-04-13','student0088','enrolled','3','standard','\0',7),(1169,0,NULL,NULL,'admin','2011-04-13','student0089','wait','3','pnp','\0',7),(1170,0,NULL,NULL,'admin','2011-04-13','student0090','enrolled','3','standard','\0',7),(1171,0,NULL,NULL,'admin','2011-04-13','student0091','wait','3','pnp','\0',7),(1172,0,NULL,NULL,'admin','2011-04-13','student0092','enrolled','3','standard','\0',7),(1173,0,NULL,NULL,'admin','2011-04-13','student0093','wait','3','pnp','\0',7),(1174,0,NULL,NULL,'admin','2011-04-13','student0094','enrolled','3','standard','\0',7),(1175,0,NULL,NULL,'admin','2011-04-13','student0095','wait','3','pnp','\0',7),(1176,0,NULL,NULL,'admin','2011-04-13','student0096','enrolled','3','standard','\0',7),(1177,0,NULL,NULL,'admin','2011-04-13','student0097','wait','3','pnp','\0',7),(1178,0,NULL,NULL,'admin','2011-04-13','student0098','enrolled','3','standard','\0',7),(1179,0,NULL,NULL,'admin','2011-04-13','student0099','wait','3','pnp','\0',7),(1180,0,NULL,NULL,'admin','2011-04-13','student0100','enrolled','3','standard','\0',7),(1181,0,NULL,NULL,'admin','2011-04-13','student0101','wait','3','pnp','\0',7),(1182,0,NULL,NULL,'admin','2011-04-13','student0102','enrolled','3','standard','\0',7),(1183,0,NULL,NULL,'admin','2011-04-13','student0103','wait','3','pnp','\0',7),(1184,0,NULL,NULL,'admin','2011-04-13','student0104','enrolled','3','standard','\0',7),(1185,0,NULL,NULL,'admin','2011-04-13','student0105','wait','3','pnp','\0',7),(1186,0,NULL,NULL,'admin','2011-04-13','student0106','enrolled','3','standard','\0',7),(1187,0,NULL,NULL,'admin','2011-04-13','student0107','wait','3','pnp','\0',7),(1188,0,NULL,NULL,'admin','2011-04-13','student0108','enrolled','3','standard','\0',7),(1189,0,NULL,NULL,'admin','2011-04-13','student0109','wait','3','pnp','\0',7),(1190,0,NULL,NULL,'admin','2011-04-13','student0110','enrolled','3','standard','\0',7),(1191,0,NULL,NULL,'admin','2011-04-13','student0111','wait','3','pnp','\0',7),(1192,0,NULL,NULL,'admin','2011-04-13','student0112','enrolled','3','standard','\0',7),(1193,0,NULL,NULL,'admin','2011-04-13','student0113','wait','3','pnp','\0',7),(1194,0,NULL,NULL,'admin','2011-04-13','student0114','enrolled','3','standard','\0',7),(1195,0,NULL,NULL,'admin','2011-04-13','student0115','wait','3','pnp','\0',7),(1196,0,NULL,NULL,'admin','2011-04-13','student0116','enrolled','3','standard','\0',7),(1197,0,NULL,NULL,'admin','2011-04-13','student0117','wait','3','pnp','\0',7),(1198,0,NULL,NULL,'admin','2011-04-13','student0118','enrolled','3','standard','\0',7),(1199,0,NULL,NULL,'admin','2011-04-13','student0119','wait','3','pnp','\0',7),(1200,0,NULL,NULL,'admin','2011-04-13','student0120','enrolled','3','standard','\0',7),(1201,0,NULL,NULL,'admin','2011-04-13','student0121','wait','3','pnp','\0',7),(1202,0,NULL,NULL,'admin','2011-04-13','student0122','enrolled','3','standard','\0',7),(1203,0,NULL,NULL,'admin','2011-04-13','student0123','wait','3','pnp','\0',7),(1204,0,NULL,NULL,'admin','2011-04-13','student0124','enrolled','3','standard','\0',7),(1205,0,NULL,NULL,'admin','2011-04-13','student0125','wait','3','pnp','\0',7),(1206,0,NULL,NULL,'admin','2011-04-13','student0126','enrolled','3','standard','\0',7),(1207,0,NULL,NULL,'admin','2011-04-13','student0127','wait','3','pnp','\0',7),(1208,0,NULL,NULL,'admin','2011-04-13','student0128','enrolled','3','standard','\0',7),(1209,0,NULL,NULL,'admin','2011-04-13','student0129','wait','3','pnp','\0',7),(1210,0,NULL,NULL,'admin','2011-04-13','student0130','enrolled','3','standard','\0',7),(1211,0,NULL,NULL,'admin','2011-04-13','student0131','wait','3','pnp','\0',7),(1212,0,NULL,NULL,'admin','2011-04-13','student0132','enrolled','3','standard','\0',7),(1213,0,NULL,NULL,'admin','2011-04-13','student0133','wait','3','pnp','\0',7),(1214,0,NULL,NULL,'admin','2011-04-13','student0134','enrolled','3','standard','\0',7),(1215,0,NULL,NULL,'admin','2011-04-13','student0135','wait','3','pnp','\0',7),(1216,0,NULL,NULL,'admin','2011-04-13','student0136','enrolled','3','standard','\0',7),(1217,0,NULL,NULL,'admin','2011-04-13','student0137','wait','3','pnp','\0',7),(1218,0,NULL,NULL,'admin','2011-04-13','student0138','enrolled','3','standard','\0',7),(1219,0,NULL,NULL,'admin','2011-04-13','student0139','wait','3','pnp','\0',7),(1220,0,NULL,NULL,'admin','2011-04-13','student0140','enrolled','3','standard','\0',7),(1221,0,NULL,NULL,'admin','2011-04-13','student0141','wait','3','pnp','\0',7),(1222,0,NULL,NULL,'admin','2011-04-13','student0142','enrolled','3','standard','\0',7),(1223,0,NULL,NULL,'admin','2011-04-13','student0143','wait','3','pnp','\0',7),(1224,0,NULL,NULL,'admin','2011-04-13','student0144','enrolled','3','standard','\0',7),(1225,0,NULL,NULL,'admin','2011-04-13','student0145','wait','3','pnp','\0',7),(1226,0,NULL,NULL,'admin','2011-04-13','student0146','enrolled','3','standard','\0',7),(1227,0,NULL,NULL,'admin','2011-04-13','student0147','wait','3','pnp','\0',7),(1228,0,NULL,NULL,'admin','2011-04-13','student0148','enrolled','3','standard','\0',7),(1229,0,NULL,NULL,'admin','2011-04-13','student0149','wait','3','pnp','\0',7),(1230,0,NULL,NULL,'admin','2011-04-13','student0150','enrolled','3','standard','\0',7),(1231,0,NULL,NULL,'admin','2011-04-13','student0151','wait','3','pnp','\0',7),(1232,0,NULL,NULL,'admin','2011-04-13','student0152','enrolled','3','standard','\0',7),(1233,0,NULL,NULL,'admin','2011-04-13','student0153','wait','3','pnp','\0',7),(1234,0,NULL,NULL,'admin','2011-04-13','student0154','enrolled','3','standard','\0',7),(1235,0,NULL,NULL,'admin','2011-04-13','student0155','wait','3','pnp','\0',7),(1236,0,NULL,NULL,'admin','2011-04-13','student0156','enrolled','3','standard','\0',7),(1237,0,NULL,NULL,'admin','2011-04-13','student0157','wait','3','pnp','\0',7),(1238,0,NULL,NULL,'admin','2011-04-13','student0158','enrolled','3','standard','\0',7),(1239,0,NULL,NULL,'admin','2011-04-13','student0159','wait','3','pnp','\0',7),(1240,0,NULL,NULL,'admin','2011-04-13','student0160','enrolled','3','standard','\0',7),(1241,0,NULL,NULL,'admin','2011-04-13','student0161','wait','3','pnp','\0',7),(1242,0,NULL,NULL,'admin','2011-04-13','student0162','enrolled','3','standard','\0',7),(1243,0,NULL,NULL,'admin','2011-04-13','student0163','wait','3','pnp','\0',7),(1244,0,NULL,NULL,'admin','2011-04-13','student0164','enrolled','3','standard','\0',7),(1245,0,NULL,NULL,'admin','2011-04-13','student0165','wait','3','pnp','\0',7),(1246,0,NULL,NULL,'admin','2011-04-13','student0166','enrolled','3','standard','\0',7),(1247,0,NULL,NULL,'admin','2011-04-13','student0167','wait','3','pnp','\0',7),(1248,0,NULL,NULL,'admin','2011-04-13','student0168','enrolled','3','standard','\0',7),(1249,0,NULL,NULL,'admin','2011-04-13','student0169','wait','3','pnp','\0',7),(1250,0,NULL,NULL,'admin','2011-04-13','student0170','enrolled','3','standard','\0',7),(1251,0,NULL,NULL,'admin','2011-04-13','student0171','wait','3','pnp','\0',7),(1252,0,NULL,NULL,'admin','2011-04-13','student0172','enrolled','3','standard','\0',7),(1253,0,NULL,NULL,'admin','2011-04-13','student0173','wait','3','pnp','\0',7),(1254,0,NULL,NULL,'admin','2011-04-13','student0174','enrolled','3','standard','\0',7),(1255,0,NULL,NULL,'admin','2011-04-13','student0175','wait','3','pnp','\0',7),(1256,0,NULL,NULL,'admin','2011-04-13','student0176','enrolled','3','standard','\0',7),(1257,0,NULL,NULL,'admin','2011-04-13','student0177','wait','3','pnp','\0',7),(1258,0,NULL,NULL,'admin','2011-04-13','student0178','enrolled','3','standard','\0',7),(1259,0,NULL,NULL,'admin','2011-04-13','student0179','wait','3','pnp','\0',7),(1260,0,NULL,NULL,'admin','2011-04-13','student0180','enrolled','3','standard','\0',7),(1261,0,NULL,NULL,'admin','2011-04-13','student0181','wait','3','pnp','\0',8),(1262,0,NULL,NULL,'admin','2011-04-13','student0182','enrolled','3','standard','\0',8),(1263,0,NULL,NULL,'admin','2011-04-13','student0183','wait','3','pnp','\0',8),(1264,0,NULL,NULL,'admin','2011-04-13','student0184','enrolled','3','standard','\0',8),(1265,0,NULL,NULL,'admin','2011-04-13','student0185','wait','3','pnp','\0',8),(1266,0,NULL,NULL,'admin','2011-04-13','student0186','enrolled','3','standard','\0',8),(1267,0,NULL,NULL,'admin','2011-04-13','student0187','wait','3','pnp','\0',8),(1268,0,NULL,NULL,'admin','2011-04-13','student0188','enrolled','3','standard','\0',8),(1269,0,NULL,NULL,'admin','2011-04-13','student0189','wait','3','pnp','\0',8),(1270,0,NULL,NULL,'admin','2011-04-13','student0190','enrolled','3','standard','\0',8),(1271,0,NULL,NULL,'admin','2011-04-13','student0191','wait','3','pnp','\0',8),(1272,0,NULL,NULL,'admin','2011-04-13','student0192','enrolled','3','standard','\0',8),(1273,0,NULL,NULL,'admin','2011-04-13','student0193','wait','3','pnp','\0',8),(1274,0,NULL,NULL,'admin','2011-04-13','student0194','enrolled','3','standard','\0',8),(1275,0,NULL,NULL,'admin','2011-04-13','student0195','wait','3','pnp','\0',8),(1276,0,NULL,NULL,'admin','2011-04-13','student0196','enrolled','3','standard','\0',8),(1277,0,NULL,NULL,'admin','2011-04-13','student0197','wait','3','pnp','\0',8),(1278,0,NULL,NULL,'admin','2011-04-13','student0198','enrolled','3','standard','\0',8),(1279,0,NULL,NULL,'admin','2011-04-13','student0199','wait','3','pnp','\0',8),(1280,0,NULL,NULL,'admin','2011-04-13','student0200','enrolled','3','standard','\0',8),(1281,0,NULL,NULL,'admin','2011-04-13','student0201','wait','3','pnp','\0',8),(1282,0,NULL,NULL,'admin','2011-04-13','student0202','enrolled','3','standard','\0',8),(1283,0,NULL,NULL,'admin','2011-04-13','student0203','wait','3','pnp','\0',8),(1284,0,NULL,NULL,'admin','2011-04-13','student0204','enrolled','3','standard','\0',8),(1285,0,NULL,NULL,'admin','2011-04-13','student0205','wait','3','pnp','\0',8),(1286,0,NULL,NULL,'admin','2011-04-13','student0206','enrolled','3','standard','\0',8),(1287,0,NULL,NULL,'admin','2011-04-13','student0207','wait','3','pnp','\0',8),(1288,0,NULL,NULL,'admin','2011-04-13','student0208','enrolled','3','standard','\0',8),(1289,0,NULL,NULL,'admin','2011-04-13','student0209','wait','3','pnp','\0',8),(1290,0,NULL,NULL,'admin','2011-04-13','student0210','enrolled','3','standard','\0',8),(1291,0,NULL,NULL,'admin','2011-04-13','student0211','wait','3','pnp','\0',8),(1292,0,NULL,NULL,'admin','2011-04-13','student0212','enrolled','3','standard','\0',8),(1293,0,NULL,NULL,'admin','2011-04-13','student0213','wait','3','pnp','\0',8),(1294,0,NULL,NULL,'admin','2011-04-13','student0214','enrolled','3','standard','\0',8),(1295,0,NULL,NULL,'admin','2011-04-13','student0215','wait','3','pnp','\0',8),(1296,0,NULL,NULL,'admin','2011-04-13','student0216','enrolled','3','standard','\0',8),(1297,0,NULL,NULL,'admin','2011-04-13','student0217','wait','3','pnp','\0',8),(1298,0,NULL,NULL,'admin','2011-04-13','student0218','enrolled','3','standard','\0',8),(1299,0,NULL,NULL,'admin','2011-04-13','student0219','wait','3','pnp','\0',8),(1300,0,NULL,NULL,'admin','2011-04-13','student0220','enrolled','3','standard','\0',8),(1301,0,NULL,NULL,'admin','2011-04-13','student0221','wait','3','pnp','\0',8),(1302,0,NULL,NULL,'admin','2011-04-13','student0222','enrolled','3','standard','\0',8),(1303,0,NULL,NULL,'admin','2011-04-13','student0223','wait','3','pnp','\0',8),(1304,0,NULL,NULL,'admin','2011-04-13','student0224','enrolled','3','standard','\0',8),(1305,0,NULL,NULL,'admin','2011-04-13','student0225','wait','3','pnp','\0',8),(1306,0,NULL,NULL,'admin','2011-04-13','student0226','enrolled','3','standard','\0',8),(1307,0,NULL,NULL,'admin','2011-04-13','student0227','wait','3','pnp','\0',8),(1308,0,NULL,NULL,'admin','2011-04-13','student0228','enrolled','3','standard','\0',8),(1309,0,NULL,NULL,'admin','2011-04-13','student0229','wait','3','pnp','\0',8),(1310,0,NULL,NULL,'admin','2011-04-13','student0230','enrolled','3','standard','\0',8),(1311,0,NULL,NULL,'admin','2011-04-13','student0231','wait','3','pnp','\0',8),(1312,0,NULL,NULL,'admin','2011-04-13','student0232','enrolled','3','standard','\0',8),(1313,0,NULL,NULL,'admin','2011-04-13','student0233','wait','3','pnp','\0',8),(1314,0,NULL,NULL,'admin','2011-04-13','student0234','enrolled','3','standard','\0',8),(1315,0,NULL,NULL,'admin','2011-04-13','student0235','wait','3','pnp','\0',8),(1316,0,NULL,NULL,'admin','2011-04-13','student0236','enrolled','3','standard','\0',8),(1317,0,NULL,NULL,'admin','2011-04-13','student0237','wait','3','pnp','\0',8),(1318,0,NULL,NULL,'admin','2011-04-13','student0238','enrolled','3','standard','\0',8),(1319,0,NULL,NULL,'admin','2011-04-13','student0239','wait','3','pnp','\0',8),(1320,0,NULL,NULL,'admin','2011-04-13','student0240','enrolled','3','standard','\0',8),(1321,0,NULL,NULL,'admin','2011-04-13','student0241','wait','3','pnp','\0',8),(1322,0,NULL,NULL,'admin','2011-04-13','student0242','enrolled','3','standard','\0',8),(1323,0,NULL,NULL,'admin','2011-04-13','student0243','wait','3','pnp','\0',8),(1324,0,NULL,NULL,'admin','2011-04-13','student0244','enrolled','3','standard','\0',8),(1325,0,NULL,NULL,'admin','2011-04-13','student0245','wait','3','pnp','\0',8),(1326,0,NULL,NULL,'admin','2011-04-13','student0246','enrolled','3','standard','\0',8),(1327,0,NULL,NULL,'admin','2011-04-13','student0247','wait','3','pnp','\0',8),(1328,0,NULL,NULL,'admin','2011-04-13','student0248','enrolled','3','standard','\0',8),(1329,0,NULL,NULL,'admin','2011-04-13','student0249','wait','3','pnp','\0',8),(1330,0,NULL,NULL,'admin','2011-04-13','student0250','enrolled','3','standard','\0',8),(1331,0,NULL,NULL,'admin','2011-04-13','student0251','wait','3','pnp','\0',8),(1332,0,NULL,NULL,'admin','2011-04-13','student0252','enrolled','3','standard','\0',8),(1333,0,NULL,NULL,'admin','2011-04-13','student0253','wait','3','pnp','\0',8),(1334,0,NULL,NULL,'admin','2011-04-13','student0254','enrolled','3','standard','\0',8),(1335,0,NULL,NULL,'admin','2011-04-13','student0255','wait','3','pnp','\0',8),(1336,0,NULL,NULL,'admin','2011-04-13','student0256','enrolled','3','standard','\0',8),(1337,0,NULL,NULL,'admin','2011-04-13','student0257','wait','3','pnp','\0',8),(1338,0,NULL,NULL,'admin','2011-04-13','student0258','enrolled','3','standard','\0',8),(1339,0,NULL,NULL,'admin','2011-04-13','student0259','wait','3','pnp','\0',8),(1340,0,NULL,NULL,'admin','2011-04-13','student0260','enrolled','3','standard','\0',8),(1341,0,NULL,NULL,'admin','2011-04-13','student0261','wait','3','pnp','\0',8),(1342,0,NULL,NULL,'admin','2011-04-13','student0262','enrolled','3','standard','\0',8),(1343,0,NULL,NULL,'admin','2011-04-13','student0263','wait','3','pnp','\0',8),(1344,0,NULL,NULL,'admin','2011-04-13','student0264','enrolled','3','standard','\0',8),(1345,0,NULL,NULL,'admin','2011-04-13','student0265','wait','3','pnp','\0',8),(1346,0,NULL,NULL,'admin','2011-04-13','student0266','enrolled','3','standard','\0',8),(1347,0,NULL,NULL,'admin','2011-04-13','student0267','wait','3','pnp','\0',8),(1348,0,NULL,NULL,'admin','2011-04-13','student0268','enrolled','3','standard','\0',8),(1349,0,NULL,NULL,'admin','2011-04-13','student0269','wait','3','pnp','\0',8),(1350,0,NULL,NULL,'admin','2011-04-13','student0270','enrolled','3','standard','\0',8),(1351,0,NULL,NULL,'admin','2011-04-13','student0271','wait','3','pnp','\0',8),(1352,0,NULL,NULL,'admin','2011-04-13','student0272','enrolled','3','standard','\0',8),(1353,0,NULL,NULL,'admin','2011-04-13','student0273','wait','3','pnp','\0',8),(1354,0,NULL,NULL,'admin','2011-04-13','student0274','enrolled','3','standard','\0',8),(1355,0,NULL,NULL,'admin','2011-04-13','student0275','wait','3','pnp','\0',8),(1356,0,NULL,NULL,'admin','2011-04-13','student0276','enrolled','3','standard','\0',8),(1357,0,NULL,NULL,'admin','2011-04-13','student0277','wait','3','pnp','\0',8),(1358,0,NULL,NULL,'admin','2011-04-13','student0278','enrolled','3','standard','\0',8),(1359,0,NULL,NULL,'admin','2011-04-13','student0279','wait','3','pnp','\0',8),(1360,0,NULL,NULL,'admin','2011-04-13','student0280','enrolled','3','standard','\0',8),(1361,0,NULL,NULL,'admin','2011-04-13','student0281','wait','3','pnp','\0',8),(1362,0,NULL,NULL,'admin','2011-04-13','student0282','enrolled','3','standard','\0',8),(1363,0,NULL,NULL,'admin','2011-04-13','student0283','wait','3','pnp','\0',8),(1364,0,NULL,NULL,'admin','2011-04-13','student0284','enrolled','3','standard','\0',8),(1365,0,NULL,NULL,'admin','2011-04-13','student0285','wait','3','pnp','\0',8),(1366,0,NULL,NULL,'admin','2011-04-13','student0286','enrolled','3','standard','\0',8),(1367,0,NULL,NULL,'admin','2011-04-13','student0287','wait','3','pnp','\0',8),(1368,0,NULL,NULL,'admin','2011-04-13','student0288','enrolled','3','standard','\0',8),(1369,0,NULL,NULL,'admin','2011-04-13','student0289','wait','3','pnp','\0',8),(1370,0,NULL,NULL,'admin','2011-04-13','student0290','enrolled','3','standard','\0',8),(1371,0,NULL,NULL,'admin','2011-04-13','student0291','wait','3','pnp','\0',8),(1372,0,NULL,NULL,'admin','2011-04-13','student0292','enrolled','3','standard','\0',8),(1373,0,NULL,NULL,'admin','2011-04-13','student0293','wait','3','pnp','\0',8),(1374,0,NULL,NULL,'admin','2011-04-13','student0294','enrolled','3','standard','\0',8),(1375,0,NULL,NULL,'admin','2011-04-13','student0295','wait','3','pnp','\0',8),(1376,0,NULL,NULL,'admin','2011-04-13','student0296','enrolled','3','standard','\0',8),(1377,0,NULL,NULL,'admin','2011-04-13','student0297','wait','3','pnp','\0',8),(1378,0,NULL,NULL,'admin','2011-04-13','student0298','enrolled','3','standard','\0',8),(1379,0,NULL,NULL,'admin','2011-04-13','student0299','wait','3','pnp','\0',8),(1380,0,NULL,NULL,'admin','2011-04-13','student0300','enrolled','3','standard','\0',8),(1381,0,NULL,NULL,'admin','2011-04-13','student0301','wait','3','pnp','\0',8),(1382,0,NULL,NULL,'admin','2011-04-13','student0302','enrolled','3','standard','\0',8),(1383,0,NULL,NULL,'admin','2011-04-13','student0303','wait','3','pnp','\0',8),(1384,0,NULL,NULL,'admin','2011-04-13','student0304','enrolled','3','standard','\0',8),(1385,0,NULL,NULL,'admin','2011-04-13','student0305','wait','3','pnp','\0',8),(1386,0,NULL,NULL,'admin','2011-04-13','student0306','enrolled','3','standard','\0',8),(1387,0,NULL,NULL,'admin','2011-04-13','student0307','wait','3','pnp','\0',8),(1388,0,NULL,NULL,'admin','2011-04-13','student0308','enrolled','3','standard','\0',8),(1389,0,NULL,NULL,'admin','2011-04-13','student0309','wait','3','pnp','\0',8),(1390,0,NULL,NULL,'admin','2011-04-13','student0310','enrolled','3','standard','\0',8),(1391,0,NULL,NULL,'admin','2011-04-13','student0311','wait','3','pnp','\0',8),(1392,0,NULL,NULL,'admin','2011-04-13','student0312','enrolled','3','standard','\0',8),(1393,0,NULL,NULL,'admin','2011-04-13','student0313','wait','3','pnp','\0',8),(1394,0,NULL,NULL,'admin','2011-04-13','student0314','enrolled','3','standard','\0',8),(1395,0,NULL,NULL,'admin','2011-04-13','student0315','wait','3','pnp','\0',8),(1396,0,NULL,NULL,'admin','2011-04-13','student0316','enrolled','3','standard','\0',8),(1397,0,NULL,NULL,'admin','2011-04-13','student0317','wait','3','pnp','\0',8),(1398,0,NULL,NULL,'admin','2011-04-13','student0318','enrolled','3','standard','\0',8),(1399,0,NULL,NULL,'admin','2011-04-13','student0319','wait','3','pnp','\0',8),(1400,0,NULL,NULL,'admin','2011-04-13','student0320','enrolled','3','standard','\0',8),(1401,0,NULL,NULL,'admin','2011-04-13','student0321','wait','3','pnp','\0',8),(1402,0,NULL,NULL,'admin','2011-04-13','student0322','enrolled','3','standard','\0',8),(1403,0,NULL,NULL,'admin','2011-04-13','student0323','wait','3','pnp','\0',8),(1404,0,NULL,NULL,'admin','2011-04-13','student0324','enrolled','3','standard','\0',8),(1405,0,NULL,NULL,'admin','2011-04-13','student0325','wait','3','pnp','\0',8),(1406,0,NULL,NULL,'admin','2011-04-13','student0326','enrolled','3','standard','\0',8),(1407,0,NULL,NULL,'admin','2011-04-13','student0327','wait','3','pnp','\0',8),(1408,0,NULL,NULL,'admin','2011-04-13','student0328','enrolled','3','standard','\0',8),(1409,0,NULL,NULL,'admin','2011-04-13','student0329','wait','3','pnp','\0',8),(1410,0,NULL,NULL,'admin','2011-04-13','student0330','enrolled','3','standard','\0',8),(1411,0,NULL,NULL,'admin','2011-04-13','student0331','wait','3','pnp','\0',8),(1412,0,NULL,NULL,'admin','2011-04-13','student0332','enrolled','3','standard','\0',8),(1413,0,NULL,NULL,'admin','2011-04-13','student0333','wait','3','pnp','\0',8),(1414,0,NULL,NULL,'admin','2011-04-13','student0334','enrolled','3','standard','\0',8),(1415,0,NULL,NULL,'admin','2011-04-13','student0335','wait','3','pnp','\0',8),(1416,0,NULL,NULL,'admin','2011-04-13','student0336','enrolled','3','standard','\0',8),(1417,0,NULL,NULL,'admin','2011-04-13','student0337','wait','3','pnp','\0',8),(1418,0,NULL,NULL,'admin','2011-04-13','student0338','enrolled','3','standard','\0',8),(1419,0,NULL,NULL,'admin','2011-04-13','student0339','wait','3','pnp','\0',8),(1420,0,NULL,NULL,'admin','2011-04-13','student0340','enrolled','3','standard','\0',8),(1421,0,NULL,NULL,'admin','2011-04-13','student0341','wait','3','pnp','\0',8),(1422,0,NULL,NULL,'admin','2011-04-13','student0342','enrolled','3','standard','\0',8),(1423,0,NULL,NULL,'admin','2011-04-13','student0343','wait','3','pnp','\0',8),(1424,0,NULL,NULL,'admin','2011-04-13','student0344','enrolled','3','standard','\0',8),(1425,0,NULL,NULL,'admin','2011-04-13','student0345','wait','3','pnp','\0',8),(1426,0,NULL,NULL,'admin','2011-04-13','student0346','enrolled','3','standard','\0',8),(1427,0,NULL,NULL,'admin','2011-04-13','student0347','wait','3','pnp','\0',8),(1428,0,NULL,NULL,'admin','2011-04-13','student0348','enrolled','3','standard','\0',8),(1429,0,NULL,NULL,'admin','2011-04-13','student0349','wait','3','pnp','\0',8),(1430,0,NULL,NULL,'admin','2011-04-13','student0350','enrolled','3','standard','\0',8),(1431,0,NULL,NULL,'admin','2011-04-13','student0351','wait','3','pnp','\0',8),(1432,0,NULL,NULL,'admin','2011-04-13','student0352','enrolled','3','standard','\0',8),(1433,0,NULL,NULL,'admin','2011-04-13','student0353','wait','3','pnp','\0',8),(1434,0,NULL,NULL,'admin','2011-04-13','student0354','enrolled','3','standard','\0',8),(1435,0,NULL,NULL,'admin','2011-04-13','student0355','wait','3','pnp','\0',8),(1436,0,NULL,NULL,'admin','2011-04-13','student0356','enrolled','3','standard','\0',8),(1437,0,NULL,NULL,'admin','2011-04-13','student0357','wait','3','pnp','\0',8),(1438,0,NULL,NULL,'admin','2011-04-13','student0358','enrolled','3','standard','\0',8),(1439,0,NULL,NULL,'admin','2011-04-13','student0359','wait','3','pnp','\0',8),(1440,0,NULL,NULL,'admin','2011-04-13','student0360','enrolled','3','standard','\0',8);
/*!40000 ALTER TABLE `CM_ENROLLMENT_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CM_MEETING_T`
--

DROP TABLE IF EXISTS `CM_MEETING_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CM_MEETING_T` (
  `MEETING_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LOCATION` varchar(255) DEFAULT NULL,
  `START_TIME` time DEFAULT NULL,
  `FINISH_TIME` time DEFAULT NULL,
  `NOTES` varchar(255) DEFAULT NULL,
  `MONDAY` bit(1) DEFAULT NULL,
  `TUESDAY` bit(1) DEFAULT NULL,
  `WEDNESDAY` bit(1) DEFAULT NULL,
  `THURSDAY` bit(1) DEFAULT NULL,
  `FRIDAY` bit(1) DEFAULT NULL,
  `SATURDAY` bit(1) DEFAULT NULL,
  `SUNDAY` bit(1) DEFAULT NULL,
  `SECTION_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`MEETING_ID`),
  KEY `FKE15DCD9BD0506F16` (`SECTION_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CM_MEETING_T`
--

LOCK TABLES `CM_MEETING_T` WRITE;
/*!40000 ALTER TABLE `CM_MEETING_T` DISABLE KEYS */;
INSERT INTO `CM_MEETING_T` VALUES (1,'A Building 11','10:30:00','11:00:00',NULL,'','\0','','\0','','\0','\0',12),(2,'A Building 11','10:30:00','11:00:00',NULL,'','\0','','\0','','\0','\0',13),(3,NULL,NULL,NULL,NULL,'\0','\0','\0','\0','\0','\0','\0',14),(4,'B Building 202','10:00:00','11:30:00',NULL,'\0','','\0','','\0','\0','\0',15),(5,'B Hall 11','09:00:00','10:30:00',NULL,'\0','','\0','','\0','\0','\0',16),(6,'C Building 100','13:30:00','15:00:00',NULL,'\0','','\0','','\0','\0','\0',17),(7,'Building 10','09:00:00','10:00:00',NULL,'','\0','','\0','','\0','\0',18),(8,'Hall 200','16:00:00','17:00:00',NULL,'','\0','','\0','','\0','\0',19),(9,NULL,NULL,NULL,NULL,'\0','\0','\0','\0','\0','\0','\0',20),(10,'2 Building A','11:30:00','13:00:00',NULL,'\0','','\0','','\0','\0','\0',21),(11,'101 Hall A','10:00:00','11:00:00',NULL,'','\0','','\0','','\0','\0',22),(12,'202 Building','08:00:00','09:00:00',NULL,'','\0','','\0','','\0','\0',23),(13,'11 Hall B','14:00:00','15:30:00',NULL,'\0','','\0','','\0','\0','\0',24),(14,'100 Building C','15:00:00','16:00:00',NULL,'','\0','','\0','','\0','\0',25),(15,'A Building 11','10:30:00','11:00:00',NULL,'','\0','','\0','','\0','\0',26),(16,'A Building 11','10:30:00','11:00:00',NULL,'','\0','','\0','','\0','\0',27),(17,NULL,NULL,NULL,NULL,'\0','\0','\0','\0','\0','\0','\0',28),(18,'B Building 202','10:00:00','11:30:00',NULL,'\0','','\0','','\0','\0','\0',29),(19,'B Hall 11','09:00:00','10:30:00',NULL,'\0','','\0','','\0','\0','\0',30),(20,'C Building 100','13:30:00','15:00:00',NULL,'\0','','\0','','\0','\0','\0',31),(21,'Building 10','09:00:00','10:00:00',NULL,'','\0','','\0','','\0','\0',32),(22,'Hall 200','16:00:00','17:00:00',NULL,'','\0','','\0','','\0','\0',33),(23,NULL,NULL,NULL,NULL,'\0','\0','\0','\0','\0','\0','\0',34),(24,'2 Building A','11:30:00','13:00:00',NULL,'\0','','\0','','\0','\0','\0',35),(25,'101 Hall A','10:00:00','11:00:00',NULL,'','\0','','\0','','\0','\0',36),(26,'202 Building','08:00:00','09:00:00',NULL,'','\0','','\0','','\0','\0',37),(27,'11 Hall B','14:00:00','15:30:00',NULL,'\0','','\0','','\0','\0','\0',38),(28,'100 Building C','15:00:00','16:00:00',NULL,'','\0','','\0','','\0','\0',39),(29,'A Building 11','10:30:00','11:00:00',NULL,'','\0','','\0','','\0','\0',40),(30,'A Building 11','10:30:00','11:00:00',NULL,'','\0','','\0','','\0','\0',41),(31,NULL,NULL,NULL,NULL,'\0','\0','\0','\0','\0','\0','\0',42),(32,'B Building 202','10:00:00','11:30:00',NULL,'\0','','\0','','\0','\0','\0',43),(33,'B Hall 11','09:00:00','10:30:00',NULL,'\0','','\0','','\0','\0','\0',44),(34,'C Building 100','13:30:00','15:00:00',NULL,'\0','','\0','','\0','\0','\0',45),(35,'Building 10','09:00:00','10:00:00',NULL,'','\0','','\0','','\0','\0',46),(36,'Hall 200','16:00:00','17:00:00',NULL,'','\0','','\0','','\0','\0',47),(37,NULL,NULL,NULL,NULL,'\0','\0','\0','\0','\0','\0','\0',48),(38,'2 Building A','11:30:00','13:00:00',NULL,'\0','','\0','','\0','\0','\0',49),(39,'101 Hall A','10:00:00','11:00:00',NULL,'','\0','','\0','','\0','\0',50),(40,'202 Building','08:00:00','09:00:00',NULL,'','\0','','\0','','\0','\0',51),(41,'11 Hall B','14:00:00','15:30:00',NULL,'\0','','\0','','\0','\0','\0',52),(42,'100 Building C','15:00:00','16:00:00',NULL,'','\0','','\0','','\0','\0',53),(43,'A Building 11','10:30:00','11:00:00',NULL,'','\0','','\0','','\0','\0',54),(44,'A Building 11','10:30:00','11:00:00',NULL,'','\0','','\0','','\0','\0',55),(45,NULL,NULL,NULL,NULL,'\0','\0','\0','\0','\0','\0','\0',56),(46,'B Building 202','10:00:00','11:30:00',NULL,'\0','','\0','','\0','\0','\0',57),(47,'B Hall 11','09:00:00','10:30:00',NULL,'\0','','\0','','\0','\0','\0',58),(48,'C Building 100','13:30:00','15:00:00',NULL,'\0','','\0','','\0','\0','\0',59),(49,'Building 10','09:00:00','10:00:00',NULL,'','\0','','\0','','\0','\0',60),(50,'Hall 200','16:00:00','17:00:00',NULL,'','\0','','\0','','\0','\0',61),(51,NULL,NULL,NULL,NULL,'\0','\0','\0','\0','\0','\0','\0',62),(52,'2 Building A','11:30:00','13:00:00',NULL,'\0','','\0','','\0','\0','\0',63),(53,'101 Hall A','10:00:00','11:00:00',NULL,'','\0','','\0','','\0','\0',64),(54,'202 Building','08:00:00','09:00:00',NULL,'','\0','','\0','','\0','\0',65),(55,'11 Hall B','14:00:00','15:30:00',NULL,'\0','','\0','','\0','\0','\0',66),(56,'100 Building C','15:00:00','16:00:00',NULL,'','\0','','\0','','\0','\0',67);
/*!40000 ALTER TABLE `CM_MEETING_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CM_MEMBERSHIP_T`
--

DROP TABLE IF EXISTS `CM_MEMBERSHIP_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CM_MEMBERSHIP_T` (
  `MEMBER_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `ROLE` varchar(255) NOT NULL,
  `MEMBER_CONTAINER_ID` bigint(20) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`MEMBER_ID`),
  UNIQUE KEY `USER_ID` (`USER_ID`,`MEMBER_CONTAINER_ID`),
  KEY `FK9FBBBFE067131463` (`MEMBER_CONTAINER_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=1546 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CM_MEMBERSHIP_T`
--

LOCK TABLES `CM_MEMBERSHIP_T` WRITE;
/*!40000 ALTER TABLE `CM_MEMBERSHIP_T` DISABLE KEYS */;
INSERT INTO `CM_MEMBERSHIP_T` VALUES (1,0,'da1','DeptAdmin',1,'active'),(2,0,'instructor1','I',4,NULL),(3,0,'instructor2','I',5,NULL),(4,0,'instructor1','I',6,NULL),(5,0,'instructor2','I',7,NULL),(6,0,'instructor1','I',8,NULL),(7,0,'instructor2','I',9,NULL),(8,0,'instructor1','I',10,NULL),(9,0,'instructor2','I',11,NULL),(10,0,'student0001','S',14,'member'),(11,0,'student0002','S',14,'member'),(12,0,'student0003','S',14,'member'),(13,0,'student0004','S',14,'member'),(14,0,'student0005','S',14,'member'),(15,0,'student0006','S',14,'member'),(16,0,'student0007','S',14,'member'),(17,0,'student0008','S',14,'member'),(18,0,'student0009','S',14,'member'),(19,0,'student0010','S',14,'member'),(20,0,'student0011','S',14,'member'),(21,0,'student0012','S',14,'member'),(22,0,'student0013','S',14,'member'),(23,0,'student0014','S',14,'member'),(24,0,'student0015','S',14,'member'),(25,0,'student0016','S',14,'member'),(26,0,'student0017','S',14,'member'),(27,0,'student0018','S',14,'member'),(28,0,'student0019','S',14,'member'),(29,0,'student0020','S',14,'member'),(30,0,'student0021','S',14,'member'),(31,0,'student0022','S',14,'member'),(32,0,'student0023','S',14,'member'),(33,0,'student0024','S',14,'member'),(34,0,'student0025','S',14,'member'),(35,0,'student0026','S',14,'member'),(36,0,'student0027','S',14,'member'),(37,0,'student0028','S',14,'member'),(38,0,'student0029','S',14,'member'),(39,0,'student0030','S',14,'member'),(40,0,'instructor','I',14,'section_leader'),(41,0,'admin','I',14,'section_leader'),(42,0,'student0031','S',15,'member'),(43,0,'student0032','S',15,'member'),(44,0,'student0033','S',15,'member'),(45,0,'student0034','S',15,'member'),(46,0,'student0035','S',15,'member'),(47,0,'student0036','S',15,'member'),(48,0,'student0037','S',15,'member'),(49,0,'student0038','S',15,'member'),(50,0,'student0039','S',15,'member'),(51,0,'student0040','S',15,'member'),(52,0,'student0041','S',15,'member'),(53,0,'student0042','S',15,'member'),(54,0,'student0043','S',15,'member'),(55,0,'student0044','S',15,'member'),(56,0,'student0045','S',15,'member'),(57,0,'student0046','S',15,'member'),(58,0,'student0047','S',15,'member'),(59,0,'student0048','S',15,'member'),(60,0,'student0049','S',15,'member'),(61,0,'student0050','S',15,'member'),(62,0,'student0051','S',15,'member'),(63,0,'student0052','S',15,'member'),(64,0,'student0053','S',15,'member'),(65,0,'student0054','S',15,'member'),(66,0,'student0055','S',15,'member'),(67,0,'student0056','S',15,'member'),(68,0,'student0057','S',15,'member'),(69,0,'student0058','S',15,'member'),(70,0,'student0059','S',15,'member'),(71,0,'student0060','S',15,'member'),(72,0,'instructor','I',15,'section_leader'),(73,0,'admin','I',15,'section_leader'),(74,0,'student0061','S',16,'member'),(75,0,'student0062','S',16,'member'),(76,0,'student0063','S',16,'member'),(77,0,'student0064','S',16,'member'),(78,0,'student0065','S',16,'member'),(79,0,'student0066','S',16,'member'),(80,0,'student0067','S',16,'member'),(81,0,'student0068','S',16,'member'),(82,0,'student0069','S',16,'member'),(83,0,'student0070','S',16,'member'),(84,0,'student0071','S',16,'member'),(85,0,'student0072','S',16,'member'),(86,0,'student0073','S',16,'member'),(87,0,'student0074','S',16,'member'),(88,0,'student0075','S',16,'member'),(89,0,'student0076','S',16,'member'),(90,0,'student0077','S',16,'member'),(91,0,'student0078','S',16,'member'),(92,0,'student0079','S',16,'member'),(93,0,'student0080','S',16,'member'),(94,0,'student0081','S',16,'member'),(95,0,'student0082','S',16,'member'),(96,0,'student0083','S',16,'member'),(97,0,'student0084','S',16,'member'),(98,0,'student0085','S',16,'member'),(99,0,'student0086','S',16,'member'),(100,0,'student0087','S',16,'member'),(101,0,'student0088','S',16,'member'),(102,0,'student0089','S',16,'member'),(103,0,'student0090','S',16,'member'),(104,0,'instructor','I',16,'section_leader'),(105,0,'admin','I',16,'section_leader'),(106,0,'student0091','S',17,'member'),(107,0,'student0092','S',17,'member'),(108,0,'student0093','S',17,'member'),(109,0,'student0094','S',17,'member'),(110,0,'student0095','S',17,'member'),(111,0,'student0096','S',17,'member'),(112,0,'student0097','S',17,'member'),(113,0,'student0098','S',17,'member'),(114,0,'student0099','S',17,'member'),(115,0,'student0100','S',17,'member'),(116,0,'student0101','S',17,'member'),(117,0,'student0102','S',17,'member'),(118,0,'student0103','S',17,'member'),(119,0,'student0104','S',17,'member'),(120,0,'student0105','S',17,'member'),(121,0,'student0106','S',17,'member'),(122,0,'student0107','S',17,'member'),(123,0,'student0108','S',17,'member'),(124,0,'student0109','S',17,'member'),(125,0,'student0110','S',17,'member'),(126,0,'student0111','S',17,'member'),(127,0,'student0112','S',17,'member'),(128,0,'student0113','S',17,'member'),(129,0,'student0114','S',17,'member'),(130,0,'student0115','S',17,'member'),(131,0,'student0116','S',17,'member'),(132,0,'student0117','S',17,'member'),(133,0,'student0118','S',17,'member'),(134,0,'student0119','S',17,'member'),(135,0,'student0120','S',17,'member'),(136,0,'instructor','I',17,'section_leader'),(137,0,'admin','I',17,'section_leader'),(138,0,'student0121','S',18,'member'),(139,0,'student0122','S',18,'member'),(140,0,'student0123','S',18,'member'),(141,0,'student0124','S',18,'member'),(142,0,'student0125','S',18,'member'),(143,0,'student0126','S',18,'member'),(144,0,'student0127','S',18,'member'),(145,0,'student0128','S',18,'member'),(146,0,'student0129','S',18,'member'),(147,0,'student0130','S',18,'member'),(148,0,'student0131','S',18,'member'),(149,0,'student0132','S',18,'member'),(150,0,'student0133','S',18,'member'),(151,0,'student0134','S',18,'member'),(152,0,'student0135','S',18,'member'),(153,0,'student0136','S',18,'member'),(154,0,'student0137','S',18,'member'),(155,0,'student0138','S',18,'member'),(156,0,'student0139','S',18,'member'),(157,0,'student0140','S',18,'member'),(158,0,'student0141','S',18,'member'),(159,0,'student0142','S',18,'member'),(160,0,'student0143','S',18,'member'),(161,0,'student0144','S',18,'member'),(162,0,'student0145','S',18,'member'),(163,0,'student0146','S',18,'member'),(164,0,'student0147','S',18,'member'),(165,0,'student0148','S',18,'member'),(166,0,'student0149','S',18,'member'),(167,0,'student0150','S',18,'member'),(168,0,'instructor','I',18,'section_leader'),(169,0,'admin','I',18,'section_leader'),(170,0,'student0151','S',19,'member'),(171,0,'student0152','S',19,'member'),(172,0,'student0153','S',19,'member'),(173,0,'student0154','S',19,'member'),(174,0,'student0155','S',19,'member'),(175,0,'student0156','S',19,'member'),(176,0,'student0157','S',19,'member'),(177,0,'student0158','S',19,'member'),(178,0,'student0159','S',19,'member'),(179,0,'student0160','S',19,'member'),(180,0,'student0161','S',19,'member'),(181,0,'student0162','S',19,'member'),(182,0,'student0163','S',19,'member'),(183,0,'student0164','S',19,'member'),(184,0,'student0165','S',19,'member'),(185,0,'student0166','S',19,'member'),(186,0,'student0167','S',19,'member'),(187,0,'student0168','S',19,'member'),(188,0,'student0169','S',19,'member'),(189,0,'student0170','S',19,'member'),(190,0,'student0171','S',19,'member'),(191,0,'student0172','S',19,'member'),(192,0,'student0173','S',19,'member'),(193,0,'student0174','S',19,'member'),(194,0,'student0175','S',19,'member'),(195,0,'student0176','S',19,'member'),(196,0,'student0177','S',19,'member'),(197,0,'student0178','S',19,'member'),(198,0,'student0179','S',19,'member'),(199,0,'student0180','S',19,'member'),(200,0,'instructor','I',19,'section_leader'),(201,0,'admin','I',19,'section_leader'),(202,0,'student0181','S',20,'member'),(203,0,'student0182','S',20,'member'),(204,0,'student0183','S',20,'member'),(205,0,'student0184','S',20,'member'),(206,0,'student0185','S',20,'member'),(207,0,'student0186','S',20,'member'),(208,0,'student0187','S',20,'member'),(209,0,'student0188','S',20,'member'),(210,0,'student0189','S',20,'member'),(211,0,'student0190','S',20,'member'),(212,0,'student0191','S',20,'member'),(213,0,'student0192','S',20,'member'),(214,0,'student0193','S',20,'member'),(215,0,'student0194','S',20,'member'),(216,0,'student0195','S',20,'member'),(217,0,'student0196','S',20,'member'),(218,0,'student0197','S',20,'member'),(219,0,'student0198','S',20,'member'),(220,0,'student0199','S',20,'member'),(221,0,'student0200','S',20,'member'),(222,0,'student0201','S',20,'member'),(223,0,'student0202','S',20,'member'),(224,0,'student0203','S',20,'member'),(225,0,'student0204','S',20,'member'),(226,0,'student0205','S',20,'member'),(227,0,'student0206','S',20,'member'),(228,0,'student0207','S',20,'member'),(229,0,'student0208','S',20,'member'),(230,0,'student0209','S',20,'member'),(231,0,'student0210','S',20,'member'),(232,0,'instructor','I',20,'section_leader'),(233,0,'admin','I',20,'section_leader'),(234,0,'student0211','S',21,'member'),(235,0,'student0212','S',21,'member'),(236,0,'student0213','S',21,'member'),(237,0,'student0214','S',21,'member'),(238,0,'student0215','S',21,'member'),(239,0,'student0216','S',21,'member'),(240,0,'student0217','S',21,'member'),(241,0,'student0218','S',21,'member'),(242,0,'student0219','S',21,'member'),(243,0,'student0220','S',21,'member'),(244,0,'student0221','S',21,'member'),(245,0,'student0222','S',21,'member'),(246,0,'student0223','S',21,'member'),(247,0,'student0224','S',21,'member'),(248,0,'student0225','S',21,'member'),(249,0,'student0226','S',21,'member'),(250,0,'student0227','S',21,'member'),(251,0,'student0228','S',21,'member'),(252,0,'student0229','S',21,'member'),(253,0,'student0230','S',21,'member'),(254,0,'student0231','S',21,'member'),(255,0,'student0232','S',21,'member'),(256,0,'student0233','S',21,'member'),(257,0,'student0234','S',21,'member'),(258,0,'student0235','S',21,'member'),(259,0,'student0236','S',21,'member'),(260,0,'student0237','S',21,'member'),(261,0,'student0238','S',21,'member'),(262,0,'student0239','S',21,'member'),(263,0,'student0240','S',21,'member'),(264,0,'instructor','I',21,'section_leader'),(265,0,'admin','I',21,'section_leader'),(266,0,'student0241','S',22,'member'),(267,0,'student0242','S',22,'member'),(268,0,'student0243','S',22,'member'),(269,0,'student0244','S',22,'member'),(270,0,'student0245','S',22,'member'),(271,0,'student0246','S',22,'member'),(272,0,'student0247','S',22,'member'),(273,0,'student0248','S',22,'member'),(274,0,'student0249','S',22,'member'),(275,0,'student0250','S',22,'member'),(276,0,'student0251','S',22,'member'),(277,0,'student0252','S',22,'member'),(278,0,'student0253','S',22,'member'),(279,0,'student0254','S',22,'member'),(280,0,'student0255','S',22,'member'),(281,0,'student0256','S',22,'member'),(282,0,'student0257','S',22,'member'),(283,0,'student0258','S',22,'member'),(284,0,'student0259','S',22,'member'),(285,0,'student0260','S',22,'member'),(286,0,'student0261','S',22,'member'),(287,0,'student0262','S',22,'member'),(288,0,'student0263','S',22,'member'),(289,0,'student0264','S',22,'member'),(290,0,'student0265','S',22,'member'),(291,0,'student0266','S',22,'member'),(292,0,'student0267','S',22,'member'),(293,0,'student0268','S',22,'member'),(294,0,'student0269','S',22,'member'),(295,0,'student0270','S',22,'member'),(296,0,'instructor','I',22,'section_leader'),(297,0,'admin','I',22,'section_leader'),(298,0,'student0271','S',23,'member'),(299,0,'student0272','S',23,'member'),(300,0,'student0273','S',23,'member'),(301,0,'student0274','S',23,'member'),(302,0,'student0275','S',23,'member'),(303,0,'student0276','S',23,'member'),(304,0,'student0277','S',23,'member'),(305,0,'student0278','S',23,'member'),(306,0,'student0279','S',23,'member'),(307,0,'student0280','S',23,'member'),(308,0,'student0281','S',23,'member'),(309,0,'student0282','S',23,'member'),(310,0,'student0283','S',23,'member'),(311,0,'student0284','S',23,'member'),(312,0,'student0285','S',23,'member'),(313,0,'student0286','S',23,'member'),(314,0,'student0287','S',23,'member'),(315,0,'student0288','S',23,'member'),(316,0,'student0289','S',23,'member'),(317,0,'student0290','S',23,'member'),(318,0,'student0291','S',23,'member'),(319,0,'student0292','S',23,'member'),(320,0,'student0293','S',23,'member'),(321,0,'student0294','S',23,'member'),(322,0,'student0295','S',23,'member'),(323,0,'student0296','S',23,'member'),(324,0,'student0297','S',23,'member'),(325,0,'student0298','S',23,'member'),(326,0,'student0299','S',23,'member'),(327,0,'student0300','S',23,'member'),(328,0,'instructor','I',23,'section_leader'),(329,0,'admin','I',23,'section_leader'),(330,0,'student0301','S',24,'member'),(331,0,'student0302','S',24,'member'),(332,0,'student0303','S',24,'member'),(333,0,'student0304','S',24,'member'),(334,0,'student0305','S',24,'member'),(335,0,'student0306','S',24,'member'),(336,0,'student0307','S',24,'member'),(337,0,'student0308','S',24,'member'),(338,0,'student0309','S',24,'member'),(339,0,'student0310','S',24,'member'),(340,0,'student0311','S',24,'member'),(341,0,'student0312','S',24,'member'),(342,0,'student0313','S',24,'member'),(343,0,'student0314','S',24,'member'),(344,0,'student0315','S',24,'member'),(345,0,'student0316','S',24,'member'),(346,0,'student0317','S',24,'member'),(347,0,'student0318','S',24,'member'),(348,0,'student0319','S',24,'member'),(349,0,'student0320','S',24,'member'),(350,0,'student0321','S',24,'member'),(351,0,'student0322','S',24,'member'),(352,0,'student0323','S',24,'member'),(353,0,'student0324','S',24,'member'),(354,0,'student0325','S',24,'member'),(355,0,'student0326','S',24,'member'),(356,0,'student0327','S',24,'member'),(357,0,'student0328','S',24,'member'),(358,0,'student0329','S',24,'member'),(359,0,'student0330','S',24,'member'),(360,0,'instructor','I',24,'section_leader'),(361,0,'admin','I',24,'section_leader'),(362,0,'student0331','S',25,'member'),(363,0,'student0332','S',25,'member'),(364,0,'student0333','S',25,'member'),(365,0,'student0334','S',25,'member'),(366,0,'student0335','S',25,'member'),(367,0,'student0336','S',25,'member'),(368,0,'student0337','S',25,'member'),(369,0,'student0338','S',25,'member'),(370,0,'student0339','S',25,'member'),(371,0,'student0340','S',25,'member'),(372,0,'student0341','S',25,'member'),(373,0,'student0342','S',25,'member'),(374,0,'student0343','S',25,'member'),(375,0,'student0344','S',25,'member'),(376,0,'student0345','S',25,'member'),(377,0,'student0346','S',25,'member'),(378,0,'student0347','S',25,'member'),(379,0,'student0348','S',25,'member'),(380,0,'student0349','S',25,'member'),(381,0,'student0350','S',25,'member'),(382,0,'student0351','S',25,'member'),(383,0,'student0352','S',25,'member'),(384,0,'student0353','S',25,'member'),(385,0,'student0354','S',25,'member'),(386,0,'student0355','S',25,'member'),(387,0,'student0356','S',25,'member'),(388,0,'student0357','S',25,'member'),(389,0,'student0358','S',25,'member'),(390,0,'student0359','S',25,'member'),(391,0,'student0360','S',25,'member'),(392,0,'instructor','I',25,'section_leader'),(393,0,'admin','I',25,'section_leader'),(394,0,'student0001','S',28,'member'),(395,0,'student0002','S',28,'member'),(396,0,'student0003','S',28,'member'),(397,0,'student0004','S',28,'member'),(398,0,'student0005','S',28,'member'),(399,0,'student0006','S',28,'member'),(400,0,'student0007','S',28,'member'),(401,0,'student0008','S',28,'member'),(402,0,'student0009','S',28,'member'),(403,0,'student0010','S',28,'member'),(404,0,'student0011','S',28,'member'),(405,0,'student0012','S',28,'member'),(406,0,'student0013','S',28,'member'),(407,0,'student0014','S',28,'member'),(408,0,'student0015','S',28,'member'),(409,0,'student0016','S',28,'member'),(410,0,'student0017','S',28,'member'),(411,0,'student0018','S',28,'member'),(412,0,'student0019','S',28,'member'),(413,0,'student0020','S',28,'member'),(414,0,'student0021','S',28,'member'),(415,0,'student0022','S',28,'member'),(416,0,'student0023','S',28,'member'),(417,0,'student0024','S',28,'member'),(418,0,'student0025','S',28,'member'),(419,0,'student0026','S',28,'member'),(420,0,'student0027','S',28,'member'),(421,0,'student0028','S',28,'member'),(422,0,'student0029','S',28,'member'),(423,0,'student0030','S',28,'member'),(424,0,'instructor','I',28,'section_leader'),(425,0,'admin','I',28,'section_leader'),(426,0,'student0031','S',29,'member'),(427,0,'student0032','S',29,'member'),(428,0,'student0033','S',29,'member'),(429,0,'student0034','S',29,'member'),(430,0,'student0035','S',29,'member'),(431,0,'student0036','S',29,'member'),(432,0,'student0037','S',29,'member'),(433,0,'student0038','S',29,'member'),(434,0,'student0039','S',29,'member'),(435,0,'student0040','S',29,'member'),(436,0,'student0041','S',29,'member'),(437,0,'student0042','S',29,'member'),(438,0,'student0043','S',29,'member'),(439,0,'student0044','S',29,'member'),(440,0,'student0045','S',29,'member'),(441,0,'student0046','S',29,'member'),(442,0,'student0047','S',29,'member'),(443,0,'student0048','S',29,'member'),(444,0,'student0049','S',29,'member'),(445,0,'student0050','S',29,'member'),(446,0,'student0051','S',29,'member'),(447,0,'student0052','S',29,'member'),(448,0,'student0053','S',29,'member'),(449,0,'student0054','S',29,'member'),(450,0,'student0055','S',29,'member'),(451,0,'student0056','S',29,'member'),(452,0,'student0057','S',29,'member'),(453,0,'student0058','S',29,'member'),(454,0,'student0059','S',29,'member'),(455,0,'student0060','S',29,'member'),(456,0,'instructor','I',29,'section_leader'),(457,0,'admin','I',29,'section_leader'),(458,0,'student0061','S',30,'member'),(459,0,'student0062','S',30,'member'),(460,0,'student0063','S',30,'member'),(461,0,'student0064','S',30,'member'),(462,0,'student0065','S',30,'member'),(463,0,'student0066','S',30,'member'),(464,0,'student0067','S',30,'member'),(465,0,'student0068','S',30,'member'),(466,0,'student0069','S',30,'member'),(467,0,'student0070','S',30,'member'),(468,0,'student0071','S',30,'member'),(469,0,'student0072','S',30,'member'),(470,0,'student0073','S',30,'member'),(471,0,'student0074','S',30,'member'),(472,0,'student0075','S',30,'member'),(473,0,'student0076','S',30,'member'),(474,0,'student0077','S',30,'member'),(475,0,'student0078','S',30,'member'),(476,0,'student0079','S',30,'member'),(477,0,'student0080','S',30,'member'),(478,0,'student0081','S',30,'member'),(479,0,'student0082','S',30,'member'),(480,0,'student0083','S',30,'member'),(481,0,'student0084','S',30,'member'),(482,0,'student0085','S',30,'member'),(483,0,'student0086','S',30,'member'),(484,0,'student0087','S',30,'member'),(485,0,'student0088','S',30,'member'),(486,0,'student0089','S',30,'member'),(487,0,'student0090','S',30,'member'),(488,0,'instructor','I',30,'section_leader'),(489,0,'admin','I',30,'section_leader'),(490,0,'student0091','S',31,'member'),(491,0,'student0092','S',31,'member'),(492,0,'student0093','S',31,'member'),(493,0,'student0094','S',31,'member'),(494,0,'student0095','S',31,'member'),(495,0,'student0096','S',31,'member'),(496,0,'student0097','S',31,'member'),(497,0,'student0098','S',31,'member'),(498,0,'student0099','S',31,'member'),(499,0,'student0100','S',31,'member'),(500,0,'student0101','S',31,'member'),(501,0,'student0102','S',31,'member'),(502,0,'student0103','S',31,'member'),(503,0,'student0104','S',31,'member'),(504,0,'student0105','S',31,'member'),(505,0,'student0106','S',31,'member'),(506,0,'student0107','S',31,'member'),(507,0,'student0108','S',31,'member'),(508,0,'student0109','S',31,'member'),(509,0,'student0110','S',31,'member'),(510,0,'student0111','S',31,'member'),(511,0,'student0112','S',31,'member'),(512,0,'student0113','S',31,'member'),(513,0,'student0114','S',31,'member'),(514,0,'student0115','S',31,'member'),(515,0,'student0116','S',31,'member'),(516,0,'student0117','S',31,'member'),(517,0,'student0118','S',31,'member'),(518,0,'student0119','S',31,'member'),(519,0,'student0120','S',31,'member'),(520,0,'instructor','I',31,'section_leader'),(521,0,'admin','I',31,'section_leader'),(522,0,'student0121','S',32,'member'),(523,0,'student0122','S',32,'member'),(524,0,'student0123','S',32,'member'),(525,0,'student0124','S',32,'member'),(526,0,'student0125','S',32,'member'),(527,0,'student0126','S',32,'member'),(528,0,'student0127','S',32,'member'),(529,0,'student0128','S',32,'member'),(530,0,'student0129','S',32,'member'),(531,0,'student0130','S',32,'member'),(532,0,'student0131','S',32,'member'),(533,0,'student0132','S',32,'member'),(534,0,'student0133','S',32,'member'),(535,0,'student0134','S',32,'member'),(536,0,'student0135','S',32,'member'),(537,0,'student0136','S',32,'member'),(538,0,'student0137','S',32,'member'),(539,0,'student0138','S',32,'member'),(540,0,'student0139','S',32,'member'),(541,0,'student0140','S',32,'member'),(542,0,'student0141','S',32,'member'),(543,0,'student0142','S',32,'member'),(544,0,'student0143','S',32,'member'),(545,0,'student0144','S',32,'member'),(546,0,'student0145','S',32,'member'),(547,0,'student0146','S',32,'member'),(548,0,'student0147','S',32,'member'),(549,0,'student0148','S',32,'member'),(550,0,'student0149','S',32,'member'),(551,0,'student0150','S',32,'member'),(552,0,'instructor','I',32,'section_leader'),(553,0,'admin','I',32,'section_leader'),(554,0,'student0151','S',33,'member'),(555,0,'student0152','S',33,'member'),(556,0,'student0153','S',33,'member'),(557,0,'student0154','S',33,'member'),(558,0,'student0155','S',33,'member'),(559,0,'student0156','S',33,'member'),(560,0,'student0157','S',33,'member'),(561,0,'student0158','S',33,'member'),(562,0,'student0159','S',33,'member'),(563,0,'student0160','S',33,'member'),(564,0,'student0161','S',33,'member'),(565,0,'student0162','S',33,'member'),(566,0,'student0163','S',33,'member'),(567,0,'student0164','S',33,'member'),(568,0,'student0165','S',33,'member'),(569,0,'student0166','S',33,'member'),(570,0,'student0167','S',33,'member'),(571,0,'student0168','S',33,'member'),(572,0,'student0169','S',33,'member'),(573,0,'student0170','S',33,'member'),(574,0,'student0171','S',33,'member'),(575,0,'student0172','S',33,'member'),(576,0,'student0173','S',33,'member'),(577,0,'student0174','S',33,'member'),(578,0,'student0175','S',33,'member'),(579,0,'student0176','S',33,'member'),(580,0,'student0177','S',33,'member'),(581,0,'student0178','S',33,'member'),(582,0,'student0179','S',33,'member'),(583,0,'student0180','S',33,'member'),(584,0,'instructor','I',33,'section_leader'),(585,0,'admin','I',33,'section_leader'),(586,0,'student0181','S',34,'member'),(587,0,'student0182','S',34,'member'),(588,0,'student0183','S',34,'member'),(589,0,'student0184','S',34,'member'),(590,0,'student0185','S',34,'member'),(591,0,'student0186','S',34,'member'),(592,0,'student0187','S',34,'member'),(593,0,'student0188','S',34,'member'),(594,0,'student0189','S',34,'member'),(595,0,'student0190','S',34,'member'),(596,0,'student0191','S',34,'member'),(597,0,'student0192','S',34,'member'),(598,0,'student0193','S',34,'member'),(599,0,'student0194','S',34,'member'),(600,0,'student0195','S',34,'member'),(601,0,'student0196','S',34,'member'),(602,0,'student0197','S',34,'member'),(603,0,'student0198','S',34,'member'),(604,0,'student0199','S',34,'member'),(605,0,'student0200','S',34,'member'),(606,0,'student0201','S',34,'member'),(607,0,'student0202','S',34,'member'),(608,0,'student0203','S',34,'member'),(609,0,'student0204','S',34,'member'),(610,0,'student0205','S',34,'member'),(611,0,'student0206','S',34,'member'),(612,0,'student0207','S',34,'member'),(613,0,'student0208','S',34,'member'),(614,0,'student0209','S',34,'member'),(615,0,'student0210','S',34,'member'),(616,0,'instructor','I',34,'section_leader'),(617,0,'admin','I',34,'section_leader'),(618,0,'student0211','S',35,'member'),(619,0,'student0212','S',35,'member'),(620,0,'student0213','S',35,'member'),(621,0,'student0214','S',35,'member'),(622,0,'student0215','S',35,'member'),(623,0,'student0216','S',35,'member'),(624,0,'student0217','S',35,'member'),(625,0,'student0218','S',35,'member'),(626,0,'student0219','S',35,'member'),(627,0,'student0220','S',35,'member'),(628,0,'student0221','S',35,'member'),(629,0,'student0222','S',35,'member'),(630,0,'student0223','S',35,'member'),(631,0,'student0224','S',35,'member'),(632,0,'student0225','S',35,'member'),(633,0,'student0226','S',35,'member'),(634,0,'student0227','S',35,'member'),(635,0,'student0228','S',35,'member'),(636,0,'student0229','S',35,'member'),(637,0,'student0230','S',35,'member'),(638,0,'student0231','S',35,'member'),(639,0,'student0232','S',35,'member'),(640,0,'student0233','S',35,'member'),(641,0,'student0234','S',35,'member'),(642,0,'student0235','S',35,'member'),(643,0,'student0236','S',35,'member'),(644,0,'student0237','S',35,'member'),(645,0,'student0238','S',35,'member'),(646,0,'student0239','S',35,'member'),(647,0,'student0240','S',35,'member'),(648,0,'instructor','I',35,'section_leader'),(649,0,'admin','I',35,'section_leader'),(650,0,'student0241','S',36,'member'),(651,0,'student0242','S',36,'member'),(652,0,'student0243','S',36,'member'),(653,0,'student0244','S',36,'member'),(654,0,'student0245','S',36,'member'),(655,0,'student0246','S',36,'member'),(656,0,'student0247','S',36,'member'),(657,0,'student0248','S',36,'member'),(658,0,'student0249','S',36,'member'),(659,0,'student0250','S',36,'member'),(660,0,'student0251','S',36,'member'),(661,0,'student0252','S',36,'member'),(662,0,'student0253','S',36,'member'),(663,0,'student0254','S',36,'member'),(664,0,'student0255','S',36,'member'),(665,0,'student0256','S',36,'member'),(666,0,'student0257','S',36,'member'),(667,0,'student0258','S',36,'member'),(668,0,'student0259','S',36,'member'),(669,0,'student0260','S',36,'member'),(670,0,'student0261','S',36,'member'),(671,0,'student0262','S',36,'member'),(672,0,'student0263','S',36,'member'),(673,0,'student0264','S',36,'member'),(674,0,'student0265','S',36,'member'),(675,0,'student0266','S',36,'member'),(676,0,'student0267','S',36,'member'),(677,0,'student0268','S',36,'member'),(678,0,'student0269','S',36,'member'),(679,0,'student0270','S',36,'member'),(680,0,'instructor','I',36,'section_leader'),(681,0,'admin','I',36,'section_leader'),(682,0,'student0271','S',37,'member'),(683,0,'student0272','S',37,'member'),(684,0,'student0273','S',37,'member'),(685,0,'student0274','S',37,'member'),(686,0,'student0275','S',37,'member'),(687,0,'student0276','S',37,'member'),(688,0,'student0277','S',37,'member'),(689,0,'student0278','S',37,'member'),(690,0,'student0279','S',37,'member'),(691,0,'student0280','S',37,'member'),(692,0,'student0281','S',37,'member'),(693,0,'student0282','S',37,'member'),(694,0,'student0283','S',37,'member'),(695,0,'student0284','S',37,'member'),(696,0,'student0285','S',37,'member'),(697,0,'student0286','S',37,'member'),(698,0,'student0287','S',37,'member'),(699,0,'student0288','S',37,'member'),(700,0,'student0289','S',37,'member'),(701,0,'student0290','S',37,'member'),(702,0,'student0291','S',37,'member'),(703,0,'student0292','S',37,'member'),(704,0,'student0293','S',37,'member'),(705,0,'student0294','S',37,'member'),(706,0,'student0295','S',37,'member'),(707,0,'student0296','S',37,'member'),(708,0,'student0297','S',37,'member'),(709,0,'student0298','S',37,'member'),(710,0,'student0299','S',37,'member'),(711,0,'student0300','S',37,'member'),(712,0,'instructor','I',37,'section_leader'),(713,0,'admin','I',37,'section_leader'),(714,0,'student0301','S',38,'member'),(715,0,'student0302','S',38,'member'),(716,0,'student0303','S',38,'member'),(717,0,'student0304','S',38,'member'),(718,0,'student0305','S',38,'member'),(719,0,'student0306','S',38,'member'),(720,0,'student0307','S',38,'member'),(721,0,'student0308','S',38,'member'),(722,0,'student0309','S',38,'member'),(723,0,'student0310','S',38,'member'),(724,0,'student0311','S',38,'member'),(725,0,'student0312','S',38,'member'),(726,0,'student0313','S',38,'member'),(727,0,'student0314','S',38,'member'),(728,0,'student0315','S',38,'member'),(729,0,'student0316','S',38,'member'),(730,0,'student0317','S',38,'member'),(731,0,'student0318','S',38,'member'),(732,0,'student0319','S',38,'member'),(733,0,'student0320','S',38,'member'),(734,0,'student0321','S',38,'member'),(735,0,'student0322','S',38,'member'),(736,0,'student0323','S',38,'member'),(737,0,'student0324','S',38,'member'),(738,0,'student0325','S',38,'member'),(739,0,'student0326','S',38,'member'),(740,0,'student0327','S',38,'member'),(741,0,'student0328','S',38,'member'),(742,0,'student0329','S',38,'member'),(743,0,'student0330','S',38,'member'),(744,0,'instructor','I',38,'section_leader'),(745,0,'admin','I',38,'section_leader'),(746,0,'student0331','S',39,'member'),(747,0,'student0332','S',39,'member'),(748,0,'student0333','S',39,'member'),(749,0,'student0334','S',39,'member'),(750,0,'student0335','S',39,'member'),(751,0,'student0336','S',39,'member'),(752,0,'student0337','S',39,'member'),(753,0,'student0338','S',39,'member'),(754,0,'student0339','S',39,'member'),(755,0,'student0340','S',39,'member'),(756,0,'student0341','S',39,'member'),(757,0,'student0342','S',39,'member'),(758,0,'student0343','S',39,'member'),(759,0,'student0344','S',39,'member'),(760,0,'student0345','S',39,'member'),(761,0,'student0346','S',39,'member'),(762,0,'student0347','S',39,'member'),(763,0,'student0348','S',39,'member'),(764,0,'student0349','S',39,'member'),(765,0,'student0350','S',39,'member'),(766,0,'student0351','S',39,'member'),(767,0,'student0352','S',39,'member'),(768,0,'student0353','S',39,'member'),(769,0,'student0354','S',39,'member'),(770,0,'student0355','S',39,'member'),(771,0,'student0356','S',39,'member'),(772,0,'student0357','S',39,'member'),(773,0,'student0358','S',39,'member'),(774,0,'student0359','S',39,'member'),(775,0,'student0360','S',39,'member'),(776,0,'instructor','I',39,'section_leader'),(777,0,'admin','I',39,'section_leader'),(778,0,'student0001','S',42,'member'),(779,0,'student0002','S',42,'member'),(780,0,'student0003','S',42,'member'),(781,0,'student0004','S',42,'member'),(782,0,'student0005','S',42,'member'),(783,0,'student0006','S',42,'member'),(784,0,'student0007','S',42,'member'),(785,0,'student0008','S',42,'member'),(786,0,'student0009','S',42,'member'),(787,0,'student0010','S',42,'member'),(788,0,'student0011','S',42,'member'),(789,0,'student0012','S',42,'member'),(790,0,'student0013','S',42,'member'),(791,0,'student0014','S',42,'member'),(792,0,'student0015','S',42,'member'),(793,0,'student0016','S',42,'member'),(794,0,'student0017','S',42,'member'),(795,0,'student0018','S',42,'member'),(796,0,'student0019','S',42,'member'),(797,0,'student0020','S',42,'member'),(798,0,'student0021','S',42,'member'),(799,0,'student0022','S',42,'member'),(800,0,'student0023','S',42,'member'),(801,0,'student0024','S',42,'member'),(802,0,'student0025','S',42,'member'),(803,0,'student0026','S',42,'member'),(804,0,'student0027','S',42,'member'),(805,0,'student0028','S',42,'member'),(806,0,'student0029','S',42,'member'),(807,0,'student0030','S',42,'member'),(808,0,'instructor','I',42,'section_leader'),(809,0,'admin','I',42,'section_leader'),(810,0,'student0031','S',43,'member'),(811,0,'student0032','S',43,'member'),(812,0,'student0033','S',43,'member'),(813,0,'student0034','S',43,'member'),(814,0,'student0035','S',43,'member'),(815,0,'student0036','S',43,'member'),(816,0,'student0037','S',43,'member'),(817,0,'student0038','S',43,'member'),(818,0,'student0039','S',43,'member'),(819,0,'student0040','S',43,'member'),(820,0,'student0041','S',43,'member'),(821,0,'student0042','S',43,'member'),(822,0,'student0043','S',43,'member'),(823,0,'student0044','S',43,'member'),(824,0,'student0045','S',43,'member'),(825,0,'student0046','S',43,'member'),(826,0,'student0047','S',43,'member'),(827,0,'student0048','S',43,'member'),(828,0,'student0049','S',43,'member'),(829,0,'student0050','S',43,'member'),(830,0,'student0051','S',43,'member'),(831,0,'student0052','S',43,'member'),(832,0,'student0053','S',43,'member'),(833,0,'student0054','S',43,'member'),(834,0,'student0055','S',43,'member'),(835,0,'student0056','S',43,'member'),(836,0,'student0057','S',43,'member'),(837,0,'student0058','S',43,'member'),(838,0,'student0059','S',43,'member'),(839,0,'student0060','S',43,'member'),(840,0,'instructor','I',43,'section_leader'),(841,0,'admin','I',43,'section_leader'),(842,0,'student0061','S',44,'member'),(843,0,'student0062','S',44,'member'),(844,0,'student0063','S',44,'member'),(845,0,'student0064','S',44,'member'),(846,0,'student0065','S',44,'member'),(847,0,'student0066','S',44,'member'),(848,0,'student0067','S',44,'member'),(849,0,'student0068','S',44,'member'),(850,0,'student0069','S',44,'member'),(851,0,'student0070','S',44,'member'),(852,0,'student0071','S',44,'member'),(853,0,'student0072','S',44,'member'),(854,0,'student0073','S',44,'member'),(855,0,'student0074','S',44,'member'),(856,0,'student0075','S',44,'member'),(857,0,'student0076','S',44,'member'),(858,0,'student0077','S',44,'member'),(859,0,'student0078','S',44,'member'),(860,0,'student0079','S',44,'member'),(861,0,'student0080','S',44,'member'),(862,0,'student0081','S',44,'member'),(863,0,'student0082','S',44,'member'),(864,0,'student0083','S',44,'member'),(865,0,'student0084','S',44,'member'),(866,0,'student0085','S',44,'member'),(867,0,'student0086','S',44,'member'),(868,0,'student0087','S',44,'member'),(869,0,'student0088','S',44,'member'),(870,0,'student0089','S',44,'member'),(871,0,'student0090','S',44,'member'),(872,0,'instructor','I',44,'section_leader'),(873,0,'admin','I',44,'section_leader'),(874,0,'student0091','S',45,'member'),(875,0,'student0092','S',45,'member'),(876,0,'student0093','S',45,'member'),(877,0,'student0094','S',45,'member'),(878,0,'student0095','S',45,'member'),(879,0,'student0096','S',45,'member'),(880,0,'student0097','S',45,'member'),(881,0,'student0098','S',45,'member'),(882,0,'student0099','S',45,'member'),(883,0,'student0100','S',45,'member'),(884,0,'student0101','S',45,'member'),(885,0,'student0102','S',45,'member'),(886,0,'student0103','S',45,'member'),(887,0,'student0104','S',45,'member'),(888,0,'student0105','S',45,'member'),(889,0,'student0106','S',45,'member'),(890,0,'student0107','S',45,'member'),(891,0,'student0108','S',45,'member'),(892,0,'student0109','S',45,'member'),(893,0,'student0110','S',45,'member'),(894,0,'student0111','S',45,'member'),(895,0,'student0112','S',45,'member'),(896,0,'student0113','S',45,'member'),(897,0,'student0114','S',45,'member'),(898,0,'student0115','S',45,'member'),(899,0,'student0116','S',45,'member'),(900,0,'student0117','S',45,'member'),(901,0,'student0118','S',45,'member'),(902,0,'student0119','S',45,'member'),(903,0,'student0120','S',45,'member'),(904,0,'instructor','I',45,'section_leader'),(905,0,'admin','I',45,'section_leader'),(906,0,'student0121','S',46,'member'),(907,0,'student0122','S',46,'member'),(908,0,'student0123','S',46,'member'),(909,0,'student0124','S',46,'member'),(910,0,'student0125','S',46,'member'),(911,0,'student0126','S',46,'member'),(912,0,'student0127','S',46,'member'),(913,0,'student0128','S',46,'member'),(914,0,'student0129','S',46,'member'),(915,0,'student0130','S',46,'member'),(916,0,'student0131','S',46,'member'),(917,0,'student0132','S',46,'member'),(918,0,'student0133','S',46,'member'),(919,0,'student0134','S',46,'member'),(920,0,'student0135','S',46,'member'),(921,0,'student0136','S',46,'member'),(922,0,'student0137','S',46,'member'),(923,0,'student0138','S',46,'member'),(924,0,'student0139','S',46,'member'),(925,0,'student0140','S',46,'member'),(926,0,'student0141','S',46,'member'),(927,0,'student0142','S',46,'member'),(928,0,'student0143','S',46,'member'),(929,0,'student0144','S',46,'member'),(930,0,'student0145','S',46,'member'),(931,0,'student0146','S',46,'member'),(932,0,'student0147','S',46,'member'),(933,0,'student0148','S',46,'member'),(934,0,'student0149','S',46,'member'),(935,0,'student0150','S',46,'member'),(936,0,'instructor','I',46,'section_leader'),(937,0,'admin','I',46,'section_leader'),(938,0,'student0151','S',47,'member'),(939,0,'student0152','S',47,'member'),(940,0,'student0153','S',47,'member'),(941,0,'student0154','S',47,'member'),(942,0,'student0155','S',47,'member'),(943,0,'student0156','S',47,'member'),(944,0,'student0157','S',47,'member'),(945,0,'student0158','S',47,'member'),(946,0,'student0159','S',47,'member'),(947,0,'student0160','S',47,'member'),(948,0,'student0161','S',47,'member'),(949,0,'student0162','S',47,'member'),(950,0,'student0163','S',47,'member'),(951,0,'student0164','S',47,'member'),(952,0,'student0165','S',47,'member'),(953,0,'student0166','S',47,'member'),(954,0,'student0167','S',47,'member'),(955,0,'student0168','S',47,'member'),(956,0,'student0169','S',47,'member'),(957,0,'student0170','S',47,'member'),(958,0,'student0171','S',47,'member'),(959,0,'student0172','S',47,'member'),(960,0,'student0173','S',47,'member'),(961,0,'student0174','S',47,'member'),(962,0,'student0175','S',47,'member'),(963,0,'student0176','S',47,'member'),(964,0,'student0177','S',47,'member'),(965,0,'student0178','S',47,'member'),(966,0,'student0179','S',47,'member'),(967,0,'student0180','S',47,'member'),(968,0,'instructor','I',47,'section_leader'),(969,0,'admin','I',47,'section_leader'),(970,0,'student0181','S',48,'member'),(971,0,'student0182','S',48,'member'),(972,0,'student0183','S',48,'member'),(973,0,'student0184','S',48,'member'),(974,0,'student0185','S',48,'member'),(975,0,'student0186','S',48,'member'),(976,0,'student0187','S',48,'member'),(977,0,'student0188','S',48,'member'),(978,0,'student0189','S',48,'member'),(979,0,'student0190','S',48,'member'),(980,0,'student0191','S',48,'member'),(981,0,'student0192','S',48,'member'),(982,0,'student0193','S',48,'member'),(983,0,'student0194','S',48,'member'),(984,0,'student0195','S',48,'member'),(985,0,'student0196','S',48,'member'),(986,0,'student0197','S',48,'member'),(987,0,'student0198','S',48,'member'),(988,0,'student0199','S',48,'member'),(989,0,'student0200','S',48,'member'),(990,0,'student0201','S',48,'member'),(991,0,'student0202','S',48,'member'),(992,0,'student0203','S',48,'member'),(993,0,'student0204','S',48,'member'),(994,0,'student0205','S',48,'member'),(995,0,'student0206','S',48,'member'),(996,0,'student0207','S',48,'member'),(997,0,'student0208','S',48,'member'),(998,0,'student0209','S',48,'member'),(999,0,'student0210','S',48,'member'),(1000,0,'instructor','I',48,'section_leader'),(1001,0,'admin','I',48,'section_leader'),(1002,0,'student0211','S',49,'member'),(1003,0,'student0212','S',49,'member'),(1004,0,'student0213','S',49,'member'),(1005,0,'student0214','S',49,'member'),(1006,0,'student0215','S',49,'member'),(1007,0,'student0216','S',49,'member'),(1008,0,'student0217','S',49,'member'),(1009,0,'student0218','S',49,'member'),(1010,0,'student0219','S',49,'member'),(1011,0,'student0220','S',49,'member'),(1012,0,'student0221','S',49,'member'),(1013,0,'student0222','S',49,'member'),(1014,0,'student0223','S',49,'member'),(1015,0,'student0224','S',49,'member'),(1016,0,'student0225','S',49,'member'),(1017,0,'student0226','S',49,'member'),(1018,0,'student0227','S',49,'member'),(1019,0,'student0228','S',49,'member'),(1020,0,'student0229','S',49,'member'),(1021,0,'student0230','S',49,'member'),(1022,0,'student0231','S',49,'member'),(1023,0,'student0232','S',49,'member'),(1024,0,'student0233','S',49,'member'),(1025,0,'student0234','S',49,'member'),(1026,0,'student0235','S',49,'member'),(1027,0,'student0236','S',49,'member'),(1028,0,'student0237','S',49,'member'),(1029,0,'student0238','S',49,'member'),(1030,0,'student0239','S',49,'member'),(1031,0,'student0240','S',49,'member'),(1032,0,'instructor','I',49,'section_leader'),(1033,0,'admin','I',49,'section_leader'),(1034,0,'student0241','S',50,'member'),(1035,0,'student0242','S',50,'member'),(1036,0,'student0243','S',50,'member'),(1037,0,'student0244','S',50,'member'),(1038,0,'student0245','S',50,'member'),(1039,0,'student0246','S',50,'member'),(1040,0,'student0247','S',50,'member'),(1041,0,'student0248','S',50,'member'),(1042,0,'student0249','S',50,'member'),(1043,0,'student0250','S',50,'member'),(1044,0,'student0251','S',50,'member'),(1045,0,'student0252','S',50,'member'),(1046,0,'student0253','S',50,'member'),(1047,0,'student0254','S',50,'member'),(1048,0,'student0255','S',50,'member'),(1049,0,'student0256','S',50,'member'),(1050,0,'student0257','S',50,'member'),(1051,0,'student0258','S',50,'member'),(1052,0,'student0259','S',50,'member'),(1053,0,'student0260','S',50,'member'),(1054,0,'student0261','S',50,'member'),(1055,0,'student0262','S',50,'member'),(1056,0,'student0263','S',50,'member'),(1057,0,'student0264','S',50,'member'),(1058,0,'student0265','S',50,'member'),(1059,0,'student0266','S',50,'member'),(1060,0,'student0267','S',50,'member'),(1061,0,'student0268','S',50,'member'),(1062,0,'student0269','S',50,'member'),(1063,0,'student0270','S',50,'member'),(1064,0,'instructor','I',50,'section_leader'),(1065,0,'admin','I',50,'section_leader'),(1066,0,'student0271','S',51,'member'),(1067,0,'student0272','S',51,'member'),(1068,0,'student0273','S',51,'member'),(1069,0,'student0274','S',51,'member'),(1070,0,'student0275','S',51,'member'),(1071,0,'student0276','S',51,'member'),(1072,0,'student0277','S',51,'member'),(1073,0,'student0278','S',51,'member'),(1074,0,'student0279','S',51,'member'),(1075,0,'student0280','S',51,'member'),(1076,0,'student0281','S',51,'member'),(1077,0,'student0282','S',51,'member'),(1078,0,'student0283','S',51,'member'),(1079,0,'student0284','S',51,'member'),(1080,0,'student0285','S',51,'member'),(1081,0,'student0286','S',51,'member'),(1082,0,'student0287','S',51,'member'),(1083,0,'student0288','S',51,'member'),(1084,0,'student0289','S',51,'member'),(1085,0,'student0290','S',51,'member'),(1086,0,'student0291','S',51,'member'),(1087,0,'student0292','S',51,'member'),(1088,0,'student0293','S',51,'member'),(1089,0,'student0294','S',51,'member'),(1090,0,'student0295','S',51,'member'),(1091,0,'student0296','S',51,'member'),(1092,0,'student0297','S',51,'member'),(1093,0,'student0298','S',51,'member'),(1094,0,'student0299','S',51,'member'),(1095,0,'student0300','S',51,'member'),(1096,0,'instructor','I',51,'section_leader'),(1097,0,'admin','I',51,'section_leader'),(1098,0,'student0301','S',52,'member'),(1099,0,'student0302','S',52,'member'),(1100,0,'student0303','S',52,'member'),(1101,0,'student0304','S',52,'member'),(1102,0,'student0305','S',52,'member'),(1103,0,'student0306','S',52,'member'),(1104,0,'student0307','S',52,'member'),(1105,0,'student0308','S',52,'member'),(1106,0,'student0309','S',52,'member'),(1107,0,'student0310','S',52,'member'),(1108,0,'student0311','S',52,'member'),(1109,0,'student0312','S',52,'member'),(1110,0,'student0313','S',52,'member'),(1111,0,'student0314','S',52,'member'),(1112,0,'student0315','S',52,'member'),(1113,0,'student0316','S',52,'member'),(1114,0,'student0317','S',52,'member'),(1115,0,'student0318','S',52,'member'),(1116,0,'student0319','S',52,'member'),(1117,0,'student0320','S',52,'member'),(1118,0,'student0321','S',52,'member'),(1119,0,'student0322','S',52,'member'),(1120,0,'student0323','S',52,'member'),(1121,0,'student0324','S',52,'member'),(1122,0,'student0325','S',52,'member'),(1123,0,'student0326','S',52,'member'),(1124,0,'student0327','S',52,'member'),(1125,0,'student0328','S',52,'member'),(1126,0,'student0329','S',52,'member'),(1127,0,'student0330','S',52,'member'),(1128,0,'instructor','I',52,'section_leader'),(1129,0,'admin','I',52,'section_leader'),(1130,0,'student0331','S',53,'member'),(1131,0,'student0332','S',53,'member'),(1132,0,'student0333','S',53,'member'),(1133,0,'student0334','S',53,'member'),(1134,0,'student0335','S',53,'member'),(1135,0,'student0336','S',53,'member'),(1136,0,'student0337','S',53,'member'),(1137,0,'student0338','S',53,'member'),(1138,0,'student0339','S',53,'member'),(1139,0,'student0340','S',53,'member'),(1140,0,'student0341','S',53,'member'),(1141,0,'student0342','S',53,'member'),(1142,0,'student0343','S',53,'member'),(1143,0,'student0344','S',53,'member'),(1144,0,'student0345','S',53,'member'),(1145,0,'student0346','S',53,'member'),(1146,0,'student0347','S',53,'member'),(1147,0,'student0348','S',53,'member'),(1148,0,'student0349','S',53,'member'),(1149,0,'student0350','S',53,'member'),(1150,0,'student0351','S',53,'member'),(1151,0,'student0352','S',53,'member'),(1152,0,'student0353','S',53,'member'),(1153,0,'student0354','S',53,'member'),(1154,0,'student0355','S',53,'member'),(1155,0,'student0356','S',53,'member'),(1156,0,'student0357','S',53,'member'),(1157,0,'student0358','S',53,'member'),(1158,0,'student0359','S',53,'member'),(1159,0,'student0360','S',53,'member'),(1160,0,'instructor','I',53,'section_leader'),(1161,0,'admin','I',53,'section_leader'),(1162,0,'student0001','S',56,'member'),(1163,0,'student0002','S',56,'member'),(1164,0,'student0003','S',56,'member'),(1165,0,'student0004','S',56,'member'),(1166,0,'student0005','S',56,'member'),(1167,0,'student0006','S',56,'member'),(1168,0,'student0007','S',56,'member'),(1169,0,'student0008','S',56,'member'),(1170,0,'student0009','S',56,'member'),(1171,0,'student0010','S',56,'member'),(1172,0,'student0011','S',56,'member'),(1173,0,'student0012','S',56,'member'),(1174,0,'student0013','S',56,'member'),(1175,0,'student0014','S',56,'member'),(1176,0,'student0015','S',56,'member'),(1177,0,'student0016','S',56,'member'),(1178,0,'student0017','S',56,'member'),(1179,0,'student0018','S',56,'member'),(1180,0,'student0019','S',56,'member'),(1181,0,'student0020','S',56,'member'),(1182,0,'student0021','S',56,'member'),(1183,0,'student0022','S',56,'member'),(1184,0,'student0023','S',56,'member'),(1185,0,'student0024','S',56,'member'),(1186,0,'student0025','S',56,'member'),(1187,0,'student0026','S',56,'member'),(1188,0,'student0027','S',56,'member'),(1189,0,'student0028','S',56,'member'),(1190,0,'student0029','S',56,'member'),(1191,0,'student0030','S',56,'member'),(1192,0,'instructor','I',56,'section_leader'),(1193,0,'admin','I',56,'section_leader'),(1194,0,'student0031','S',57,'member'),(1195,0,'student0032','S',57,'member'),(1196,0,'student0033','S',57,'member'),(1197,0,'student0034','S',57,'member'),(1198,0,'student0035','S',57,'member'),(1199,0,'student0036','S',57,'member'),(1200,0,'student0037','S',57,'member'),(1201,0,'student0038','S',57,'member'),(1202,0,'student0039','S',57,'member'),(1203,0,'student0040','S',57,'member'),(1204,0,'student0041','S',57,'member'),(1205,0,'student0042','S',57,'member'),(1206,0,'student0043','S',57,'member'),(1207,0,'student0044','S',57,'member'),(1208,0,'student0045','S',57,'member'),(1209,0,'student0046','S',57,'member'),(1210,0,'student0047','S',57,'member'),(1211,0,'student0048','S',57,'member'),(1212,0,'student0049','S',57,'member'),(1213,0,'student0050','S',57,'member'),(1214,0,'student0051','S',57,'member'),(1215,0,'student0052','S',57,'member'),(1216,0,'student0053','S',57,'member'),(1217,0,'student0054','S',57,'member'),(1218,0,'student0055','S',57,'member'),(1219,0,'student0056','S',57,'member'),(1220,0,'student0057','S',57,'member'),(1221,0,'student0058','S',57,'member'),(1222,0,'student0059','S',57,'member'),(1223,0,'student0060','S',57,'member'),(1224,0,'instructor','I',57,'section_leader'),(1225,0,'admin','I',57,'section_leader'),(1226,0,'student0061','S',58,'member'),(1227,0,'student0062','S',58,'member'),(1228,0,'student0063','S',58,'member'),(1229,0,'student0064','S',58,'member'),(1230,0,'student0065','S',58,'member'),(1231,0,'student0066','S',58,'member'),(1232,0,'student0067','S',58,'member'),(1233,0,'student0068','S',58,'member'),(1234,0,'student0069','S',58,'member'),(1235,0,'student0070','S',58,'member'),(1236,0,'student0071','S',58,'member'),(1237,0,'student0072','S',58,'member'),(1238,0,'student0073','S',58,'member'),(1239,0,'student0074','S',58,'member'),(1240,0,'student0075','S',58,'member'),(1241,0,'student0076','S',58,'member'),(1242,0,'student0077','S',58,'member'),(1243,0,'student0078','S',58,'member'),(1244,0,'student0079','S',58,'member'),(1245,0,'student0080','S',58,'member'),(1246,0,'student0081','S',58,'member'),(1247,0,'student0082','S',58,'member'),(1248,0,'student0083','S',58,'member'),(1249,0,'student0084','S',58,'member'),(1250,0,'student0085','S',58,'member'),(1251,0,'student0086','S',58,'member'),(1252,0,'student0087','S',58,'member'),(1253,0,'student0088','S',58,'member'),(1254,0,'student0089','S',58,'member'),(1255,0,'student0090','S',58,'member'),(1256,0,'instructor','I',58,'section_leader'),(1257,0,'admin','I',58,'section_leader'),(1258,0,'student0091','S',59,'member'),(1259,0,'student0092','S',59,'member'),(1260,0,'student0093','S',59,'member'),(1261,0,'student0094','S',59,'member'),(1262,0,'student0095','S',59,'member'),(1263,0,'student0096','S',59,'member'),(1264,0,'student0097','S',59,'member'),(1265,0,'student0098','S',59,'member'),(1266,0,'student0099','S',59,'member'),(1267,0,'student0100','S',59,'member'),(1268,0,'student0101','S',59,'member'),(1269,0,'student0102','S',59,'member'),(1270,0,'student0103','S',59,'member'),(1271,0,'student0104','S',59,'member'),(1272,0,'student0105','S',59,'member'),(1273,0,'student0106','S',59,'member'),(1274,0,'student0107','S',59,'member'),(1275,0,'student0108','S',59,'member'),(1276,0,'student0109','S',59,'member'),(1277,0,'student0110','S',59,'member'),(1278,0,'student0111','S',59,'member'),(1279,0,'student0112','S',59,'member'),(1280,0,'student0113','S',59,'member'),(1281,0,'student0114','S',59,'member'),(1282,0,'student0115','S',59,'member'),(1283,0,'student0116','S',59,'member'),(1284,0,'student0117','S',59,'member'),(1285,0,'student0118','S',59,'member'),(1286,0,'student0119','S',59,'member'),(1287,0,'student0120','S',59,'member'),(1288,0,'instructor','I',59,'section_leader'),(1289,0,'admin','I',59,'section_leader'),(1290,0,'student0121','S',60,'member'),(1291,0,'student0122','S',60,'member'),(1292,0,'student0123','S',60,'member'),(1293,0,'student0124','S',60,'member'),(1294,0,'student0125','S',60,'member'),(1295,0,'student0126','S',60,'member'),(1296,0,'student0127','S',60,'member'),(1297,0,'student0128','S',60,'member'),(1298,0,'student0129','S',60,'member'),(1299,0,'student0130','S',60,'member'),(1300,0,'student0131','S',60,'member'),(1301,0,'student0132','S',60,'member'),(1302,0,'student0133','S',60,'member'),(1303,0,'student0134','S',60,'member'),(1304,0,'student0135','S',60,'member'),(1305,0,'student0136','S',60,'member'),(1306,0,'student0137','S',60,'member'),(1307,0,'student0138','S',60,'member'),(1308,0,'student0139','S',60,'member'),(1309,0,'student0140','S',60,'member'),(1310,0,'student0141','S',60,'member'),(1311,0,'student0142','S',60,'member'),(1312,0,'student0143','S',60,'member'),(1313,0,'student0144','S',60,'member'),(1314,0,'student0145','S',60,'member'),(1315,0,'student0146','S',60,'member'),(1316,0,'student0147','S',60,'member'),(1317,0,'student0148','S',60,'member'),(1318,0,'student0149','S',60,'member'),(1319,0,'student0150','S',60,'member'),(1320,0,'instructor','I',60,'section_leader'),(1321,0,'admin','I',60,'section_leader'),(1322,0,'student0151','S',61,'member'),(1323,0,'student0152','S',61,'member'),(1324,0,'student0153','S',61,'member'),(1325,0,'student0154','S',61,'member'),(1326,0,'student0155','S',61,'member'),(1327,0,'student0156','S',61,'member'),(1328,0,'student0157','S',61,'member'),(1329,0,'student0158','S',61,'member'),(1330,0,'student0159','S',61,'member'),(1331,0,'student0160','S',61,'member'),(1332,0,'student0161','S',61,'member'),(1333,0,'student0162','S',61,'member'),(1334,0,'student0163','S',61,'member'),(1335,0,'student0164','S',61,'member'),(1336,0,'student0165','S',61,'member'),(1337,0,'student0166','S',61,'member'),(1338,0,'student0167','S',61,'member'),(1339,0,'student0168','S',61,'member'),(1340,0,'student0169','S',61,'member'),(1341,0,'student0170','S',61,'member'),(1342,0,'student0171','S',61,'member'),(1343,0,'student0172','S',61,'member'),(1344,0,'student0173','S',61,'member'),(1345,0,'student0174','S',61,'member'),(1346,0,'student0175','S',61,'member'),(1347,0,'student0176','S',61,'member'),(1348,0,'student0177','S',61,'member'),(1349,0,'student0178','S',61,'member'),(1350,0,'student0179','S',61,'member'),(1351,0,'student0180','S',61,'member'),(1352,0,'instructor','I',61,'section_leader'),(1353,0,'admin','I',61,'section_leader'),(1354,0,'student0181','S',62,'member'),(1355,0,'student0182','S',62,'member'),(1356,0,'student0183','S',62,'member'),(1357,0,'student0184','S',62,'member'),(1358,0,'student0185','S',62,'member'),(1359,0,'student0186','S',62,'member'),(1360,0,'student0187','S',62,'member'),(1361,0,'student0188','S',62,'member'),(1362,0,'student0189','S',62,'member'),(1363,0,'student0190','S',62,'member'),(1364,0,'student0191','S',62,'member'),(1365,0,'student0192','S',62,'member'),(1366,0,'student0193','S',62,'member'),(1367,0,'student0194','S',62,'member'),(1368,0,'student0195','S',62,'member'),(1369,0,'student0196','S',62,'member'),(1370,0,'student0197','S',62,'member'),(1371,0,'student0198','S',62,'member'),(1372,0,'student0199','S',62,'member'),(1373,0,'student0200','S',62,'member'),(1374,0,'student0201','S',62,'member'),(1375,0,'student0202','S',62,'member'),(1376,0,'student0203','S',62,'member'),(1377,0,'student0204','S',62,'member'),(1378,0,'student0205','S',62,'member'),(1379,0,'student0206','S',62,'member'),(1380,0,'student0207','S',62,'member'),(1381,0,'student0208','S',62,'member'),(1382,0,'student0209','S',62,'member'),(1383,0,'student0210','S',62,'member'),(1384,0,'instructor','I',62,'section_leader'),(1385,0,'admin','I',62,'section_leader'),(1386,0,'student0211','S',63,'member'),(1387,0,'student0212','S',63,'member'),(1388,0,'student0213','S',63,'member'),(1389,0,'student0214','S',63,'member'),(1390,0,'student0215','S',63,'member'),(1391,0,'student0216','S',63,'member'),(1392,0,'student0217','S',63,'member'),(1393,0,'student0218','S',63,'member'),(1394,0,'student0219','S',63,'member'),(1395,0,'student0220','S',63,'member'),(1396,0,'student0221','S',63,'member'),(1397,0,'student0222','S',63,'member'),(1398,0,'student0223','S',63,'member'),(1399,0,'student0224','S',63,'member'),(1400,0,'student0225','S',63,'member'),(1401,0,'student0226','S',63,'member'),(1402,0,'student0227','S',63,'member'),(1403,0,'student0228','S',63,'member'),(1404,0,'student0229','S',63,'member'),(1405,0,'student0230','S',63,'member'),(1406,0,'student0231','S',63,'member'),(1407,0,'student0232','S',63,'member'),(1408,0,'student0233','S',63,'member'),(1409,0,'student0234','S',63,'member'),(1410,0,'student0235','S',63,'member'),(1411,0,'student0236','S',63,'member'),(1412,0,'student0237','S',63,'member'),(1413,0,'student0238','S',63,'member'),(1414,0,'student0239','S',63,'member'),(1415,0,'student0240','S',63,'member'),(1416,0,'instructor','I',63,'section_leader'),(1417,0,'admin','I',63,'section_leader'),(1418,0,'student0241','S',64,'member'),(1419,0,'student0242','S',64,'member'),(1420,0,'student0243','S',64,'member'),(1421,0,'student0244','S',64,'member'),(1422,0,'student0245','S',64,'member'),(1423,0,'student0246','S',64,'member'),(1424,0,'student0247','S',64,'member'),(1425,0,'student0248','S',64,'member'),(1426,0,'student0249','S',64,'member'),(1427,0,'student0250','S',64,'member'),(1428,0,'student0251','S',64,'member'),(1429,0,'student0252','S',64,'member'),(1430,0,'student0253','S',64,'member'),(1431,0,'student0254','S',64,'member'),(1432,0,'student0255','S',64,'member'),(1433,0,'student0256','S',64,'member'),(1434,0,'student0257','S',64,'member'),(1435,0,'student0258','S',64,'member'),(1436,0,'student0259','S',64,'member'),(1437,0,'student0260','S',64,'member'),(1438,0,'student0261','S',64,'member'),(1439,0,'student0262','S',64,'member'),(1440,0,'student0263','S',64,'member'),(1441,0,'student0264','S',64,'member'),(1442,0,'student0265','S',64,'member'),(1443,0,'student0266','S',64,'member'),(1444,0,'student0267','S',64,'member'),(1445,0,'student0268','S',64,'member'),(1446,0,'student0269','S',64,'member'),(1447,0,'student0270','S',64,'member'),(1448,0,'instructor','I',64,'section_leader'),(1449,0,'admin','I',64,'section_leader'),(1450,0,'student0271','S',65,'member'),(1451,0,'student0272','S',65,'member'),(1452,0,'student0273','S',65,'member'),(1453,0,'student0274','S',65,'member'),(1454,0,'student0275','S',65,'member'),(1455,0,'student0276','S',65,'member'),(1456,0,'student0277','S',65,'member'),(1457,0,'student0278','S',65,'member'),(1458,0,'student0279','S',65,'member'),(1459,0,'student0280','S',65,'member'),(1460,0,'student0281','S',65,'member'),(1461,0,'student0282','S',65,'member'),(1462,0,'student0283','S',65,'member'),(1463,0,'student0284','S',65,'member'),(1464,0,'student0285','S',65,'member'),(1465,0,'student0286','S',65,'member'),(1466,0,'student0287','S',65,'member'),(1467,0,'student0288','S',65,'member'),(1468,0,'student0289','S',65,'member'),(1469,0,'student0290','S',65,'member'),(1470,0,'student0291','S',65,'member'),(1471,0,'student0292','S',65,'member'),(1472,0,'student0293','S',65,'member'),(1473,0,'student0294','S',65,'member'),(1474,0,'student0295','S',65,'member'),(1475,0,'student0296','S',65,'member'),(1476,0,'student0297','S',65,'member'),(1477,0,'student0298','S',65,'member'),(1478,0,'student0299','S',65,'member'),(1479,0,'student0300','S',65,'member'),(1480,0,'instructor','I',65,'section_leader'),(1481,0,'admin','I',65,'section_leader'),(1482,0,'student0301','S',66,'member'),(1483,0,'student0302','S',66,'member'),(1484,0,'student0303','S',66,'member'),(1485,0,'student0304','S',66,'member'),(1486,0,'student0305','S',66,'member'),(1487,0,'student0306','S',66,'member'),(1488,0,'student0307','S',66,'member'),(1489,0,'student0308','S',66,'member'),(1490,0,'student0309','S',66,'member'),(1491,0,'student0310','S',66,'member'),(1492,0,'student0311','S',66,'member'),(1493,0,'student0312','S',66,'member'),(1494,0,'student0313','S',66,'member'),(1495,0,'student0314','S',66,'member'),(1496,0,'student0315','S',66,'member'),(1497,0,'student0316','S',66,'member'),(1498,0,'student0317','S',66,'member'),(1499,0,'student0318','S',66,'member'),(1500,0,'student0319','S',66,'member'),(1501,0,'student0320','S',66,'member'),(1502,0,'student0321','S',66,'member'),(1503,0,'student0322','S',66,'member'),(1504,0,'student0323','S',66,'member'),(1505,0,'student0324','S',66,'member'),(1506,0,'student0325','S',66,'member'),(1507,0,'student0326','S',66,'member'),(1508,0,'student0327','S',66,'member'),(1509,0,'student0328','S',66,'member'),(1510,0,'student0329','S',66,'member'),(1511,0,'student0330','S',66,'member'),(1512,0,'instructor','I',66,'section_leader'),(1513,0,'admin','I',66,'section_leader'),(1514,0,'student0331','S',67,'member'),(1515,0,'student0332','S',67,'member'),(1516,0,'student0333','S',67,'member'),(1517,0,'student0334','S',67,'member'),(1518,0,'student0335','S',67,'member'),(1519,0,'student0336','S',67,'member'),(1520,0,'student0337','S',67,'member'),(1521,0,'student0338','S',67,'member'),(1522,0,'student0339','S',67,'member'),(1523,0,'student0340','S',67,'member'),(1524,0,'student0341','S',67,'member'),(1525,0,'student0342','S',67,'member'),(1526,0,'student0343','S',67,'member'),(1527,0,'student0344','S',67,'member'),(1528,0,'student0345','S',67,'member'),(1529,0,'student0346','S',67,'member'),(1530,0,'student0347','S',67,'member'),(1531,0,'student0348','S',67,'member'),(1532,0,'student0349','S',67,'member'),(1533,0,'student0350','S',67,'member'),(1534,0,'student0351','S',67,'member'),(1535,0,'student0352','S',67,'member'),(1536,0,'student0353','S',67,'member'),(1537,0,'student0354','S',67,'member'),(1538,0,'student0355','S',67,'member'),(1539,0,'student0356','S',67,'member'),(1540,0,'student0357','S',67,'member'),(1541,0,'student0358','S',67,'member'),(1542,0,'student0359','S',67,'member'),(1543,0,'student0360','S',67,'member'),(1544,0,'instructor','I',67,'section_leader'),(1545,0,'admin','I',67,'section_leader');
/*!40000 ALTER TABLE `CM_MEMBERSHIP_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CM_MEMBER_CONTAINER_T`
--

DROP TABLE IF EXISTS `CM_MEMBER_CONTAINER_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CM_MEMBER_CONTAINER_T` (
  `MEMBER_CONTAINER_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CLASS_DISCR` varchar(100) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `LAST_MODIFIED_BY` varchar(255) DEFAULT NULL,
  `LAST_MODIFIED_DATE` date DEFAULT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` date DEFAULT NULL,
  `ENTERPRISE_ID` varchar(100) NOT NULL,
  `TITLE` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `CROSS_LISTING` bigint(20) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `START_DATE` date DEFAULT NULL,
  `END_DATE` date DEFAULT NULL,
  `CANONICAL_COURSE` bigint(20) DEFAULT NULL,
  `ACADEMIC_SESSION` bigint(20) DEFAULT NULL,
  `CATEGORY` varchar(255) DEFAULT NULL,
  `PARENT_COURSE_SET` bigint(20) DEFAULT NULL,
  `COURSE_OFFERING` bigint(20) DEFAULT NULL,
  `ENROLLMENT_SET` bigint(20) DEFAULT NULL,
  `PARENT_SECTION` bigint(20) DEFAULT NULL,
  `MAXSIZE` int(11) DEFAULT NULL,
  PRIMARY KEY (`MEMBER_CONTAINER_ID`),
  UNIQUE KEY `CLASS_DISCR` (`CLASS_DISCR`,`ENTERPRISE_ID`),
  KEY `FKD96A9BC626827043` (`COURSE_OFFERING`),
  KEY `FKD96A9BC6D05F59F1` (`CANONICAL_COURSE`),
  KEY `FKD96A9BC6456D3EA1` (`ENROLLMENT_SET`),
  KEY `FKD96A9BC6661E50E9` (`ACADEMIC_SESSION`),
  KEY `FKD96A9BC649A68CB6` (`PARENT_COURSE_SET`),
  KEY `FKD96A9BC63B0306B1` (`PARENT_SECTION`),
  KEY `FKD96A9BC64F7C8841` (`CROSS_LISTING`)
) ENGINE=MyISAM AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CM_MEMBER_CONTAINER_T`
--

LOCK TABLES `CM_MEMBER_CONTAINER_T` WRITE;
/*!40000 ALTER TABLE `CM_MEMBER_CONTAINER_T` DISABLE KEYS */;
INSERT INTO `CM_MEMBER_CONTAINER_T` VALUES (1,'org.sakaiproject.coursemanagement.impl.CourseSetCmImpl',8,'admin','2011-04-13','admin','2011-04-13','SMPL','Sample Department','We study wet things in the Sample Dept',NULL,NULL,NULL,NULL,NULL,NULL,'DEPT',NULL,NULL,NULL,NULL,NULL),(2,'org.sakaiproject.coursemanagement.impl.CanonicalCourseCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL101','Sample 101','A survey of samples',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'org.sakaiproject.coursemanagement.impl.CanonicalCourseCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL202','Sample 202','An in depth study of samples',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'org.sakaiproject.coursemanagement.impl.CourseOfferingCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL101 Winter 2011','SMPL101','Sample course offering #1, Winter 2011',2,'open','2011-01-01','2011-04-01',2,1,NULL,NULL,NULL,NULL,NULL,NULL),(5,'org.sakaiproject.coursemanagement.impl.CourseOfferingCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL202 Winter 2011','SMPL202','Sample course offering #2, Winter 2011',2,'open','2011-01-01','2011-04-01',3,1,NULL,NULL,NULL,NULL,NULL,NULL),(6,'org.sakaiproject.coursemanagement.impl.CourseOfferingCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL101 Spring 2011','SMPL101','Sample course offering #1, Spring 2011',3,'open','2011-04-01','2011-06-01',2,2,NULL,NULL,NULL,NULL,NULL,NULL),(7,'org.sakaiproject.coursemanagement.impl.CourseOfferingCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL202 Spring 2011','SMPL202','Sample course offering #2, Spring 2011',3,'open','2011-04-01','2011-06-01',3,2,NULL,NULL,NULL,NULL,NULL,NULL),(8,'org.sakaiproject.coursemanagement.impl.CourseOfferingCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL101 Summer 2011','SMPL101','Sample course offering #1, Summer 2011',4,'open','2011-06-01','2011-09-01',2,3,NULL,NULL,NULL,NULL,NULL,NULL),(9,'org.sakaiproject.coursemanagement.impl.CourseOfferingCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL202 Summer 2011','SMPL202','Sample course offering #2, Summer 2011',4,'open','2011-06-01','2011-09-01',3,3,NULL,NULL,NULL,NULL,NULL,NULL),(10,'org.sakaiproject.coursemanagement.impl.CourseOfferingCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL101 Fall 2011','SMPL101','Sample course offering #1, Fall 2011',5,'open','2011-09-01','2012-01-01',2,4,NULL,NULL,NULL,NULL,NULL,NULL),(11,'org.sakaiproject.coursemanagement.impl.CourseOfferingCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL202 Fall 2011','SMPL202','Sample course offering #2, Fall 2011',5,'open','2011-09-01','2012-01-01',3,4,NULL,NULL,NULL,NULL,NULL,NULL),(12,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL101 Winter 2011','SMPL101 Winter 2011','SMPL101 Winter 2011 Lecture',NULL,NULL,NULL,NULL,NULL,NULL,'01.lct',NULL,4,1,NULL,NULL),(13,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL202 Winter 2011','SMPL202 Winter 2011','SMPL202 Winter 2011 Lecture',NULL,NULL,NULL,NULL,NULL,NULL,'01.lct',NULL,5,2,NULL,NULL),(14,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 1 SMPL101 Winter 2011','Discussion 1 SMPL101','Discussion 1 SMPL101 Winter 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,4,NULL,NULL,NULL),(15,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 2 SMPL101 Winter 2011','Discussion 2 SMPL101','Discussion 2 SMPL101 Winter 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,4,NULL,NULL,NULL),(16,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 3 SMPL101 Winter 2011','Discussion 3 SMPL101','Discussion 3 SMPL101 Winter 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,4,NULL,NULL,NULL),(17,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 4 SMPL101 Winter 2011','Discussion 4 SMPL101','Discussion 4 SMPL101 Winter 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,4,NULL,NULL,NULL),(18,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 5 SMPL101 Winter 2011','Discussion 5 SMPL101','Discussion 5 SMPL101 Winter 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,4,NULL,NULL,NULL),(19,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 6 SMPL101 Winter 2011','Discussion 6 SMPL101','Discussion 6 SMPL101 Winter 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,4,NULL,NULL,NULL),(20,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 1 SMPL202 Winter 2011','Discussion 1 SMPL202','Discussion 1 SMPL202 Winter 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,5,NULL,NULL,NULL),(21,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 2 SMPL202 Winter 2011','Discussion 2 SMPL202','Discussion 2 SMPL202 Winter 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,5,NULL,NULL,NULL),(22,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 3 SMPL202 Winter 2011','Discussion 3 SMPL202','Discussion 3 SMPL202 Winter 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,5,NULL,NULL,NULL),(23,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 4 SMPL202 Winter 2011','Discussion 4 SMPL202','Discussion 4 SMPL202 Winter 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,5,NULL,NULL,NULL),(24,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 5 SMPL202 Winter 2011','Discussion 5 SMPL202','Discussion 5 SMPL202 Winter 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,5,NULL,NULL,NULL),(25,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 6 SMPL202 Winter 2011','Discussion 6 SMPL202','Discussion 6 SMPL202 Winter 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,5,NULL,NULL,NULL),(26,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL101 Spring 2011','SMPL101 Spring 2011','SMPL101 Spring 2011 Lecture',NULL,NULL,NULL,NULL,NULL,NULL,'01.lct',NULL,6,3,NULL,NULL),(27,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL202 Spring 2011','SMPL202 Spring 2011','SMPL202 Spring 2011 Lecture',NULL,NULL,NULL,NULL,NULL,NULL,'01.lct',NULL,7,4,NULL,NULL),(28,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 1 SMPL101 Spring 2011','Discussion 1 SMPL101','Discussion 1 SMPL101 Spring 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,6,NULL,NULL,NULL),(29,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 2 SMPL101 Spring 2011','Discussion 2 SMPL101','Discussion 2 SMPL101 Spring 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,6,NULL,NULL,NULL),(30,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 3 SMPL101 Spring 2011','Discussion 3 SMPL101','Discussion 3 SMPL101 Spring 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,6,NULL,NULL,NULL),(31,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 4 SMPL101 Spring 2011','Discussion 4 SMPL101','Discussion 4 SMPL101 Spring 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,6,NULL,NULL,NULL),(32,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 5 SMPL101 Spring 2011','Discussion 5 SMPL101','Discussion 5 SMPL101 Spring 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,6,NULL,NULL,NULL),(33,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 6 SMPL101 Spring 2011','Discussion 6 SMPL101','Discussion 6 SMPL101 Spring 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,6,NULL,NULL,NULL),(34,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 1 SMPL202 Spring 2011','Discussion 1 SMPL202','Discussion 1 SMPL202 Spring 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,7,NULL,NULL,NULL),(35,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 2 SMPL202 Spring 2011','Discussion 2 SMPL202','Discussion 2 SMPL202 Spring 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,7,NULL,NULL,NULL),(36,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 3 SMPL202 Spring 2011','Discussion 3 SMPL202','Discussion 3 SMPL202 Spring 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,7,NULL,NULL,NULL),(37,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 4 SMPL202 Spring 2011','Discussion 4 SMPL202','Discussion 4 SMPL202 Spring 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,7,NULL,NULL,NULL),(38,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 5 SMPL202 Spring 2011','Discussion 5 SMPL202','Discussion 5 SMPL202 Spring 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,7,NULL,NULL,NULL),(39,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 6 SMPL202 Spring 2011','Discussion 6 SMPL202','Discussion 6 SMPL202 Spring 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,7,NULL,NULL,NULL),(40,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL101 Summer 2011','SMPL101 Summer 2011','SMPL101 Summer 2011 Lecture',NULL,NULL,NULL,NULL,NULL,NULL,'01.lct',NULL,8,5,NULL,NULL),(41,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL202 Summer 2011','SMPL202 Summer 2011','SMPL202 Summer 2011 Lecture',NULL,NULL,NULL,NULL,NULL,NULL,'01.lct',NULL,9,6,NULL,NULL),(42,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 1 SMPL101 Summer 2011','Discussion 1 SMPL101','Discussion 1 SMPL101 Summer 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,8,NULL,NULL,NULL),(43,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 2 SMPL101 Summer 2011','Discussion 2 SMPL101','Discussion 2 SMPL101 Summer 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,8,NULL,NULL,NULL),(44,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 3 SMPL101 Summer 2011','Discussion 3 SMPL101','Discussion 3 SMPL101 Summer 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,8,NULL,NULL,NULL),(45,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 4 SMPL101 Summer 2011','Discussion 4 SMPL101','Discussion 4 SMPL101 Summer 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,8,NULL,NULL,NULL),(46,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 5 SMPL101 Summer 2011','Discussion 5 SMPL101','Discussion 5 SMPL101 Summer 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,8,NULL,NULL,NULL),(47,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 6 SMPL101 Summer 2011','Discussion 6 SMPL101','Discussion 6 SMPL101 Summer 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,8,NULL,NULL,NULL),(48,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 1 SMPL202 Summer 2011','Discussion 1 SMPL202','Discussion 1 SMPL202 Summer 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,9,NULL,NULL,NULL),(49,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 2 SMPL202 Summer 2011','Discussion 2 SMPL202','Discussion 2 SMPL202 Summer 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,9,NULL,NULL,NULL),(50,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 3 SMPL202 Summer 2011','Discussion 3 SMPL202','Discussion 3 SMPL202 Summer 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,9,NULL,NULL,NULL),(51,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 4 SMPL202 Summer 2011','Discussion 4 SMPL202','Discussion 4 SMPL202 Summer 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,9,NULL,NULL,NULL),(52,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 5 SMPL202 Summer 2011','Discussion 5 SMPL202','Discussion 5 SMPL202 Summer 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,9,NULL,NULL,NULL),(53,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 6 SMPL202 Summer 2011','Discussion 6 SMPL202','Discussion 6 SMPL202 Summer 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,9,NULL,NULL,NULL),(54,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL101 Fall 2011','SMPL101 Fall 2011','SMPL101 Fall 2011 Lecture',NULL,NULL,NULL,NULL,NULL,NULL,'01.lct',NULL,10,7,NULL,NULL),(55,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','SMPL202 Fall 2011','SMPL202 Fall 2011','SMPL202 Fall 2011 Lecture',NULL,NULL,NULL,NULL,NULL,NULL,'01.lct',NULL,11,8,NULL,NULL),(56,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 1 SMPL101 Fall 2011','Discussion 1 SMPL101','Discussion 1 SMPL101 Fall 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,10,NULL,NULL,NULL),(57,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 2 SMPL101 Fall 2011','Discussion 2 SMPL101','Discussion 2 SMPL101 Fall 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,10,NULL,NULL,NULL),(58,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 3 SMPL101 Fall 2011','Discussion 3 SMPL101','Discussion 3 SMPL101 Fall 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,10,NULL,NULL,NULL),(59,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 4 SMPL101 Fall 2011','Discussion 4 SMPL101','Discussion 4 SMPL101 Fall 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,10,NULL,NULL,NULL),(60,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 5 SMPL101 Fall 2011','Discussion 5 SMPL101','Discussion 5 SMPL101 Fall 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,10,NULL,NULL,NULL),(61,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 6 SMPL101 Fall 2011','Discussion 6 SMPL101','Discussion 6 SMPL101 Fall 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,10,NULL,NULL,NULL),(62,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 1 SMPL202 Fall 2011','Discussion 1 SMPL202','Discussion 1 SMPL202 Fall 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,11,NULL,NULL,NULL),(63,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 2 SMPL202 Fall 2011','Discussion 2 SMPL202','Discussion 2 SMPL202 Fall 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,11,NULL,NULL,NULL),(64,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 3 SMPL202 Fall 2011','Discussion 3 SMPL202','Discussion 3 SMPL202 Fall 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,11,NULL,NULL,NULL),(65,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 4 SMPL202 Fall 2011','Discussion 4 SMPL202','Discussion 4 SMPL202 Fall 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,11,NULL,NULL,NULL),(66,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 5 SMPL202 Fall 2011','Discussion 5 SMPL202','Discussion 5 SMPL202 Fall 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,11,NULL,NULL,NULL),(67,'org.sakaiproject.coursemanagement.impl.SectionCmImpl',1,'admin','2011-04-13','admin','2011-04-13','Discussion 6 SMPL202 Fall 2011','Discussion 6 SMPL202','Discussion 6 SMPL202 Fall 2011',NULL,NULL,NULL,NULL,NULL,NULL,'03.dsc',NULL,11,NULL,NULL,NULL);
/*!40000 ALTER TABLE `CM_MEMBER_CONTAINER_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CM_OFFICIAL_INSTRUCTORS_T`
--

DROP TABLE IF EXISTS `CM_OFFICIAL_INSTRUCTORS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CM_OFFICIAL_INSTRUCTORS_T` (
  `ENROLLMENT_SET_ID` bigint(20) NOT NULL,
  `INSTRUCTOR_ID` varchar(255) DEFAULT NULL,
  UNIQUE KEY `ENROLLMENT_SET_ID` (`ENROLLMENT_SET_ID`,`INSTRUCTOR_ID`),
  KEY `FK470F8ACCC28CC1AD` (`ENROLLMENT_SET_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CM_OFFICIAL_INSTRUCTORS_T`
--

LOCK TABLES `CM_OFFICIAL_INSTRUCTORS_T` WRITE;
/*!40000 ALTER TABLE `CM_OFFICIAL_INSTRUCTORS_T` DISABLE KEYS */;
INSERT INTO `CM_OFFICIAL_INSTRUCTORS_T` VALUES (1,'admin'),(1,'instructor'),(2,'admin'),(2,'instructor'),(3,'admin'),(3,'instructor'),(4,'admin'),(4,'instructor'),(5,'admin'),(5,'instructor'),(6,'admin'),(6,'instructor'),(7,'admin'),(7,'instructor'),(8,'admin'),(8,'instructor');
/*!40000 ALTER TABLE `CM_OFFICIAL_INSTRUCTORS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CM_SEC_CATEGORY_T`
--

DROP TABLE IF EXISTS `CM_SEC_CATEGORY_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CM_SEC_CATEGORY_T` (
  `CAT_CODE` varchar(255) NOT NULL,
  `CAT_DESCR` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`CAT_CODE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CM_SEC_CATEGORY_T`
--

LOCK TABLES `CM_SEC_CATEGORY_T` WRITE;
/*!40000 ALTER TABLE `CM_SEC_CATEGORY_T` DISABLE KEYS */;
INSERT INTO `CM_SEC_CATEGORY_T` VALUES ('01.lct','Lecture'),('03.dsc','Discussion'),('02.lab','Lab'),('04.rec','Recitation'),('05.sto','Studio');
/*!40000 ALTER TABLE `CM_SEC_CATEGORY_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTENT_COLLECTION`
--

DROP TABLE IF EXISTS `CONTENT_COLLECTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTENT_COLLECTION` (
  `COLLECTION_ID` varchar(255) NOT NULL,
  `IN_COLLECTION` varchar(255) DEFAULT NULL,
  `XML` longtext,
  `BINARY_ENTITY` blob,
  UNIQUE KEY `CONTENT_COLLECTION_INDEX` (`COLLECTION_ID`),
  KEY `CONTENT_IN_COLLECTION_INDEX` (`IN_COLLECTION`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTENT_COLLECTION`
--

LOCK TABLES `CONTENT_COLLECTION` WRITE;
/*!40000 ALTER TABLE `CONTENT_COLLECTION` DISABLE KEYS */;
INSERT INTO `CONTENT_COLLECTION` VALUES ('/','',NULL,'CHSBCE\0\0\0\0\0\0\n\0/\0%org.sakaiproject.content.types.folder\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getlastmodified\020020401000000000\0\0\0e\0DAV:displayname\0root\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020020401000000000\0\0\0e\0CHEF:is-collection\0true\0\0\0e\0CHEF:modifiedby\0admin\0\0\0'),('/group/','/',NULL,'CHSBCE\0\0\0\0\0\0\n\0/group/\0%org.sakaiproject.content.types.folder\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getlastmodified\020020401000000000\0\0\0e\0DAV:displayname\0group\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020020401000000000\0\0\0e\0CHEF:is-collection\0true\0\0\0e\0CHEF:modifiedby\0admin\0\0\0'),('/public/','/',NULL,'CHSBCE\0\0\0\0\0\0\n\0/public/\0%org.sakaiproject.content.types.folder\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getlastmodified\020020401000000000\0\0\0e\0DAV:displayname\0public\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020020401000000000\0\0\0e\0CHEF:is-collection\0true\0\0\0e\0CHEF:modifiedby\0admin\0\0\0'),('/attachment/','/',NULL,'CHSBCE\0\0\0\0\0\0\n\0/attachment/\0%org.sakaiproject.content.types.folder\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getlastmodified\020020401000000000\0\0\0e\0DAV:displayname\0\nattachment\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020020401000000000\0\0\0e\0CHEF:is-collection\0true\0\0\0e\0CHEF:modifiedby\0admin\0\0\0'),('/group/PortfolioAdmin/system/','/group/PortfolioAdmin/',NULL,'CHSBCE\0\0\0\0\0\0\n\0/group/PortfolioAdmin/system/\0%org.sakaiproject.content.types.folder\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getlastmodified\020110413185929493\0\0\0e\0DAV:displayname\0system\0\0\0e\0SAKAI:content_priority\02\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413185929487\0\0\0e\0CHEF:is-collection\0true\0\0\0e\0CHEF:modifiedby\0admin\0\0\0'),('/private/','/',NULL,'CHSBCE\0\0\0\0\0\0\n\0	/private/\0%org.sakaiproject.content.types.folder\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getlastmodified\020020401000000000\0\0\0e\0DAV:displayname\0private\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020020401000000000\0\0\0e\0CHEF:is-collection\0true\0\0\0e\0CHEF:modifiedby\0admin\0\0\0'),('/user/','/',NULL,'CHSBCE\0\0\0\0\0\0\n\0/user/\0%org.sakaiproject.content.types.folder\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getlastmodified\020020401000000000\0\0\0e\0DAV:displayname\0user\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020020401000000000\0\0\0e\0CHEF:is-collection\0true\0\0\0e\0CHEF:modifiedby\0admin\0\0\0'),('/group/PortfolioAdmin/','/group/',NULL,'CHSBCE\0\0\0\0\0\0\n\0/group/PortfolioAdmin/\0%org.sakaiproject.content.types.folder\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getlastmodified\020110413185929479\0\0\0e\0DAV:displayname\0Portfolio Admin\0\0\0e\0SAKAI:content_priority\02\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413185929477\0\0\0e\0CHEF:is-collection\0true\0\0\0e\0CHEF:modifiedby\0admin\0\0\0'),('/group-user/','/',NULL,'CHSBCE\0\0\0\0\0\0\n\0/group-user/\0%org.sakaiproject.content.types.folder\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getlastmodified\020020401000000000\0\0\0e\0DAV:displayname\0\ngroup-user\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020020401000000000\0\0\0e\0CHEF:is-collection\0true\0\0\0e\0CHEF:modifiedby\0admin\0\0\0'),('/user/admin/','/user/',NULL,'CHSBCE\0\0\0\0\0\0\n\0/user/admin/\0%org.sakaiproject.content.types.folder\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getlastmodified\020110413185940409\0\0\0e\0DAV:displayname\0admin\0\0\0e\0SAKAI:content_priority\02\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413185940407\0\0\0e\0CHEF:is-collection\0true\0\0\0e\0CHEF:modifiedby\0admin\0\0\0'),('/user/d1430d8d-af0b-49e6-8c39-2d253673319a/','/user/',NULL,'CHSBCE\0\0\0\0\0\0\n\0+/user/d1430d8d-af0b-49e6-8c39-2d253673319a/\0%org.sakaiproject.content.types.folder\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getlastmodified\020110413195541038\0\0\0e\0DAV:displayname\0My Workspace\0\0\0e\0SAKAI:content_priority\03\0\0\0e\0CHEF:creator\0$d1430d8d-af0b-49e6-8c39-2d253673319a\0\0\0e\0DAV:creationdate\020110413195541036\0\0\0e\0CHEF:is-collection\0true\0\0\0e\0CHEF:modifiedby\0$d1430d8d-af0b-49e6-8c39-2d253673319a\0\0\0');
/*!40000 ALTER TABLE `CONTENT_COLLECTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTENT_DROPBOX_CHANGES`
--

DROP TABLE IF EXISTS `CONTENT_DROPBOX_CHANGES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTENT_DROPBOX_CHANGES` (
  `DROPBOX_ID` varchar(255) NOT NULL,
  `IN_COLLECTION` varchar(255) DEFAULT NULL,
  `LAST_UPDATE` varchar(24) DEFAULT NULL,
  UNIQUE KEY `CONTENT_DROPBOX_CI` (`DROPBOX_ID`),
  KEY `CONTENT_DROPBOX_II` (`IN_COLLECTION`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTENT_DROPBOX_CHANGES`
--

LOCK TABLES `CONTENT_DROPBOX_CHANGES` WRITE;
/*!40000 ALTER TABLE `CONTENT_DROPBOX_CHANGES` DISABLE KEYS */;
/*!40000 ALTER TABLE `CONTENT_DROPBOX_CHANGES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTENT_RESOURCE`
--

DROP TABLE IF EXISTS `CONTENT_RESOURCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTENT_RESOURCE` (
  `RESOURCE_ID` varchar(255) NOT NULL,
  `RESOURCE_UUID` varchar(36) DEFAULT NULL,
  `IN_COLLECTION` varchar(255) DEFAULT NULL,
  `CONTEXT` varchar(99) DEFAULT NULL,
  `FILE_PATH` varchar(128) DEFAULT NULL,
  `FILE_SIZE` bigint(20) DEFAULT NULL,
  `RESOURCE_TYPE_ID` varchar(255) DEFAULT NULL,
  `XML` longtext,
  `BINARY_ENTITY` blob,
  UNIQUE KEY `CONTENT_RESOURCE_INDEX` (`RESOURCE_ID`),
  KEY `CONTENT_IN_RESOURCE_INDEX` (`IN_COLLECTION`),
  KEY `CONTENT_RESOURCE_CI` (`CONTEXT`),
  KEY `CONTENT_UUID_RESOURCE_INDEX` (`RESOURCE_UUID`),
  KEY `CONTENT_RESOURCE_RTI` (`RESOURCE_TYPE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTENT_RESOURCE`
--

LOCK TABLES `CONTENT_RESOURCE` WRITE;
/*!40000 ALTER TABLE `CONTENT_RESOURCE` DISABLE KEYS */;
INSERT INTO `CONTENT_RESOURCE` VALUES ('/group/PortfolioAdmin/system/formFieldTemplate.xslt',NULL,'/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,64703,'org.sakaiproject.content.types.fileUpload',NULL,'CHSBRE\0\0\0\0\0\0\n\03/group/PortfolioAdmin/system/formFieldTemplate.xslt\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0text/xml\0\0\0e\0DAV:getlastmodified\020110413190656653\0\0\0e\0DAV:displayname\0formFieldTemplate.xslt\0\0\0e\0SAKAI:content_priority\0	268435466\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413190656652\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\0)used for default rendering of form fields\0\0\0e\0DAV:getcontentlength\064703\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0text/xml\0\0\0\0\0\0¸ø\0\0\0\0\0'),('/group/PortfolioAdmin/system/freeFormRenderer.xml','2e48c42d-f61c-44fe-bdb8-870e84e0a5d7','/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,6499,'org.sakaiproject.content.types.fileUpload',NULL,'CHSBRE\0\0\0\0\0\0\n\01/group/PortfolioAdmin/system/freeFormRenderer.xml\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0text/xml\0\0\0e\0DAV:getlastmodified\020110413190702994\0\0\0e\0DAV:displayname\0freeFormRenderer.xml\0\0\0e\0SAKAI:content_priority\0	268435460\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413185939695\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\0)used for rendering the free form template\0\0\0e\0DAV:getcontentlength\06499\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0text/xml\0\0\0\0\0\0c\0\0\0\0\0'),('/group/PortfolioAdmin/system/contentOverText.jpg','a483d33b-6b6b-4c7c-b69e-901065cf247a','/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,2611,'org.sakaiproject.content.types.fileUpload',NULL,'CHSBRE\0\0\0\0\0\0\n\00/group/PortfolioAdmin/system/contentOverText.jpg\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0\nimage/jpeg\0\0\0e\0DAV:getlastmodified\020110413190703062\0\0\0e\0DAV:displayname\0contentOverText.jpg\0\0\0e\0SAKAI:content_priority\0	268435461\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413185939953\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\0contentOverText layout preview\0\0\0e\0DAV:getcontentlength\02611\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0\nimage/jpeg\0\0\0\0\0\0\n3\0\0\0\0\0'),('/group/PortfolioAdmin/system/contentOverText.xml','f25eb3d7-5900-4f03-9a4f-09d53ab65dd5','/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,1767,'org.sakaiproject.content.types.fileUpload',NULL,'CHSBRE\0\0\0\0\0\0\n\00/group/PortfolioAdmin/system/contentOverText.xml\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0text/xml\0\0\0e\0DAV:getlastmodified\020110413190703074\0\0\0e\0DAV:displayname\0contentOverText.xml\0\0\0e\0SAKAI:content_priority\0	268435462\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413185940006\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\0contentOverText layout file\0\0\0e\0DAV:getcontentlength\01767\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0text/xml\0\0\0\0\0\0Á\0\0\0\0\0'),('/group/PortfolioAdmin/system/2column.jpg','badfb3c8-f143-47fe-9192-7a53bb463495','/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,2450,'org.sakaiproject.content.types.fileUpload',NULL,'CHSBRE\0\0\0\0\0\0\n\0(/group/PortfolioAdmin/system/2column.jpg\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0\nimage/jpeg\0\0\0e\0DAV:getlastmodified\020110413190703092\0\0\0e\0DAV:displayname\02column.jpg\0\0\0e\0SAKAI:content_priority\0	268435463\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413185940046\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\0twoColumn layout preview\0\0\0e\0DAV:getcontentlength\02450\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0\nimage/jpeg\0\0\0\0\0\0	í\0\0\0\0\0'),('/group/PortfolioAdmin/system/twoColumn.xml','cc793aa9-71ea-4d45-be38-b414b4aa33ac','/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,1729,'org.sakaiproject.content.types.fileUpload',NULL,'CHSBRE\0\0\0\0\0\0\n\0*/group/PortfolioAdmin/system/twoColumn.xml\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0text/xml\0\0\0e\0DAV:getlastmodified\020110413190703104\0\0\0e\0DAV:displayname\0\rtwoColumn.xml\0\0\0e\0SAKAI:content_priority\0	268435464\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413185940068\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\0twoColumn layout file\0\0\0e\0DAV:getcontentlength\01729\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0text/xml\0\0\0\0\0\0¡\0\0\0\0\0'),('/group/PortfolioAdmin/system/Simplehtml.jpg','9f4061a9-ea10-4121-8bd2-ed9e8a465156','/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,4044,'org.sakaiproject.content.types.fileUpload',NULL,'CHSBRE\0\0\0\0\0\0\n\0+/group/PortfolioAdmin/system/Simplehtml.jpg\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0\nimage/jpeg\0\0\0e\0DAV:getlastmodified\020110413190703121\0\0\0e\0DAV:displayname\0Simplehtml.jpg\0\0\0e\0SAKAI:content_priority\0	268435465\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413185940094\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\0simpleRichText layout preview\0\0\0e\0DAV:getcontentlength\04044\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0\nimage/jpeg\0\0\0\0\0\0Ã\0\0\0\0\0'),('/group/PortfolioAdmin/system/simpleRichText.xml','b0436581-e11f-4c19-a004-cd8775baad17','/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,552,'org.sakaiproject.content.types.fileUpload',NULL,'CHSBRE\0\0\0\0\0\0\n\0//group/PortfolioAdmin/system/simpleRichText.xml\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0text/xml\0\0\0e\0DAV:getlastmodified\020110413190703132\0\0\0e\0DAV:displayname\0simpleRichText.xml\0\0\0e\0SAKAI:content_priority\0	268435466\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413185940116\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\0\ZsimpleRichText layout file\0\0\0e\0DAV:getcontentlength\0552\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0text/xml\0\0\0\0\0\0(\0\0\0\0\0'),('/group/PortfolioAdmin/system/formCreate.xslt',NULL,'/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,12573,'org.sakaiproject.content.types.fileUpload',NULL,'CHSBRE\0\0\0\0\0\0\n\0,/group/PortfolioAdmin/system/formCreate.xslt\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0text/xml\0\0\0e\0DAV:getlastmodified\020110413190656465\0\0\0e\0DAV:displayname\0formCreate.xslt\0\0\0e\0SAKAI:content_priority\0	268435466\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413190656464\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\01used for default rendering of form add and update\0\0\0e\0DAV:getcontentlength\012573\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0text/xml\0\0\0\0\0\01\0\0\0\0\0'),('/group/PortfolioAdmin/system/formView.xslt',NULL,'/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,19385,'org.sakaiproject.content.types.fileUpload',NULL,'CHSBRE\0\0\0\0\0\0\n\0*/group/PortfolioAdmin/system/formView.xslt\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0text/xml\0\0\0e\0DAV:getlastmodified\020110413190656709\0\0\0e\0DAV:displayname\0\rformView.xslt\0\0\0e\0SAKAI:content_priority\0	268435466\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413190656708\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\0*used for default rendering of form viewing\0\0\0e\0DAV:getcontentlength\019385\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0text/xml\0\0\0\0\0\0Kπ\0\0\0\0\0');
/*!40000 ALTER TABLE `CONTENT_RESOURCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTENT_RESOURCE_BODY_BINARY`
--

DROP TABLE IF EXISTS `CONTENT_RESOURCE_BODY_BINARY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTENT_RESOURCE_BODY_BINARY` (
  `RESOURCE_ID` varchar(255) NOT NULL,
  `BODY` longblob,
  UNIQUE KEY `CONTENT_RESOURCE_BB_INDEX` (`RESOURCE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTENT_RESOURCE_BODY_BINARY`
--

LOCK TABLES `CONTENT_RESOURCE_BODY_BINARY` WRITE;
/*!40000 ALTER TABLE `CONTENT_RESOURCE_BODY_BINARY` DISABLE KEYS */;
INSERT INTO `CONTENT_RESOURCE_BODY_BINARY` VALUES ('/group/PortfolioAdmin/system/formCreate.xslt','<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<xsl:stylesheet version=\"2.0\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:sakaifn=\"org.sakaiproject.metaobj.utils.xml.XsltFunctions\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns:osp=\"http://www.osportfolio.org/OspML\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n	<!--xsl:template match=\"formView\">\n      <formView>\n            <xsl:copy-of select=\"*\"></xsl:copy-of>\n      </formView>\n   </xsl:template-->\n	<xsl:param name=\"panelId\" />\n	<xsl:param name=\"subForm\" />\n	<xsl:param name=\"preview\" />\n	<xsl:param name=\"fromResources\" />\n	<xsl:param name=\"edit\" />\n	<xsl:output method=\"html\" version=\"4.0\" cdata-section-elements=\"\" encoding=\"UTF-8\" indent=\"yes\" />\n	<xsl:include href=\"/group/PortfolioAdmin/system/formFieldTemplate.xslt\" />\n	<xsl:template match=\"formView\">\n		<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">\n			<head>\n				<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n				<meta http-equiv=\"Content-Style-Type\" content=\"text/css\" />\n				<title>\n					<xsl:value-of select=\"formData/artifact/schema/element/xs:annotation/xs:documentation[@source=\'ospi.label\']\" />\n				</title>\n				<xsl:for-each select=\"css/uri\">\n               <xsl:sort select=\"@order\" data-type=\"number\" />\n					<link type=\"text/css\" rel=\"stylesheet\" media=\"all\">\n						<xsl:attribute name=\"href\">\n							<xsl:value-of select=\".\" />\n						</xsl:attribute>\n					</link>\n				</xsl:for-each>\n				<link type=\"text/css\" rel=\"stylesheet\" media=\"all\" href=\"/sakai-metaobj-tool/css/metaobj.css\" />\n				<script type=\"text/javascript\" language=\"JavaScript\" src=\"/library/js/jquery.js\"> // empty\n					block </script>\n				<script type=\"text/javascript\" language=\"JavaScript\" src=\"/library/js/headscripts.js\"> // empty\n					block </script>\n				<script type=\"text/javascript\" language=\"JavaScript\" src=\"/sakai-metaobj-tool/js/nicetitle.js\"> // empty\n					block </script>\n				<xsl:value-of select=\"sakaifn:getRichTextHead()\" disable-output-escaping=\"yes\"/>\n            <script type=\"text/javascript\" src=\"/library/calendar/js/calendar1.js\"> // empty block </script>\n            <script type=\"text/javascript\" src=\"/library/calendar/js/calendar2.js\"> // empty block </script>\n			</head>\n			<body>\n				<xsl:if test=\"$panelId\">\n					<xsl:attribute name=\"onLoad\">setMainFrameHeight(\'<xsl:value-of select=\"$panelId\" />\');setFocus(focus_path);</xsl:attribute>\n				</xsl:if>\n				<div class=\"portletBody\">\n					<input type=\"hidden\" id=\"remove_item_msg\">\n						<xsl:attribute name=\"value\">\n							<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'delete_form_element\')\" />\n						</xsl:attribute>\n					</input>\n					<p class=\"instruction\">\n						<xsl:value-of disable-output-escaping=\"yes\" select=\"formData/artifact/schema/instructions\" />\n					</p>\n					<xsl:for-each select=\"/formView/errors/error\">\n						<div class=\"alertMessage\">\n							<xsl:value-of select=\"message\" />\n						</div>\n					</xsl:for-each>\n					<xsl:for-each select=\"/formView/success\">\n						<div class=\"success\">\n							<xsl:value-of select=\"sakaifn:getMessage(\'messages\', @messageKey)\" />\n						</div>\n					</xsl:for-each>\n					<form method=\"post\" onsubmit=\"a=1;\">\n						<xsl:if test=\"formData/artifact/schema/element/xs:annotation/xs:documentation[@source=\'ospi.description\']\">\n							<h3>\n								<xsl:value-of select=\"formData/artifact/schema/element/xs:annotation/xs:documentation[@source=\'ospi.description\']\" />\n							</h3>\n						</xsl:if>\n						<xsl:if test=\"formData/artifact/schema/element/xs:annotation/xs:documentation[@source=\'ospi.inlinedescription\']\">\n							<p class=\"instruction highlightPanel\">\n								<xsl:value-of select=\"formData/artifact/schema/element/xs:annotation/xs:documentation[@source=\'ospi.inlinedescription\']\" />\n							</p>\n						</xsl:if>\n						<xsl:choose>\n							<!-- todo: if this is a subform, display a good title -->\n							<xsl:when test=\"$subForm = \'true\'\">\n								<h4>\n									<xsl:value-of select=\"formData/artifact/metaData/displayName\" />\n								</h4>\n							</xsl:when>\n							<xsl:when test=\"$fromResources = \'true\' and $edit = \'true\'\">\n								<h4>\n									<xsl:value-of select=\"formData/artifact/metaData/displayName\" />\n								</h4>\n								<input type=\"hidden\" id=\"displayName\" name=\"displayName\" maxlength=\"1024\">\n									<xsl:attribute name=\"value\">\n										<xsl:value-of select=\"formData/artifact/metaData/displayName\" />\n									</xsl:attribute>\n								</input>\n							</xsl:when>\n							<xsl:when test=\"$fromResources = \'true\'\">\n								<input type=\"hidden\" id=\"displayName\" name=\"displayName\" maxlength=\"1024\" value=\"new form\" />\n							</xsl:when>\n							<xsl:otherwise>\n								<!-- the name of this entry, always required -->\n								<div class=\"shorttext required\">\n									<span class=\"reqStar\">*</span>\n									<label for=\"displayName\">\n										<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'display.name.label\')\" />\n									</label>\n									<input type=\"text\" id=\"displayName\" name=\"displayName\" maxlength=\"1024\">\n										<xsl:attribute name=\"value\">\n											<xsl:value-of select=\"formData/artifact/metaData/displayName\" />\n										</xsl:attribute>\n									</input>\n								</div>\n							</xsl:otherwise>\n						</xsl:choose>\n						<xsl:apply-templates select=\"formData/artifact/schema/element\">\n							<xsl:with-param name=\"currentParent\" select=\"formData/artifact/structuredData\" />\n							<xsl:with-param name=\"rootNode\" select=\"\'true\'\" />\n						</xsl:apply-templates>\n                  <input type=\"hidden\" name=\"childPath\" value=\"\" />\n                  <input type=\"hidden\" name=\"childFieldLabel\" value=\"\" />\n						<input type=\"hidden\" name=\"childIndex\" value=\"\" />\n						<input type=\"hidden\" name=\"fileHelper\" value=\"\" />\n						<input type=\"hidden\" name=\"editButton\" value=\"\" />\n						<input type=\"hidden\" name=\"removeButton\" value=\"\" />\n						<div class=\"act\">\n							<xsl:choose>\n								<xsl:when test=\"$subForm = \'true\'\">\n									<input type=\"submit\" name=\"updateNestedButton\" class=\"active\" accesskey=\"s\">\n										<xsl:attribute name=\"value\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'button_update\')\" />\n										</xsl:attribute>\n									</input>\n									<input type=\"submit\" name=\"cancelNestedButton\" accesskey=\"x\">\n										<xsl:attribute name=\"value\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'button_cancel\')\" />\n										</xsl:attribute>\n									</input>\n								</xsl:when>\n								<xsl:when test=\"$preview = \'true\'\">\n									<input type=\"submit\" name=\"submitButton\" class=\"active\">\n										<xsl:attribute name=\"value\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'button_validate\')\" />\n										</xsl:attribute>\n									</input>\n									<input type=\"submit\" name=\"cancel\" accesskey=\"x\">\n										<xsl:attribute name=\"value\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'button_return\')\" />\n										</xsl:attribute>\n									</input>\n								</xsl:when>\n								<xsl:when test=\"$fromResources = \'true\' and $edit != \'true\'\">\n									<input type=\"submit\" name=\"submitButton\" class=\"active\" accesskey=\"s\">\n										<xsl:attribute name=\"value\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'button_saveEditContinue\')\" />\n										</xsl:attribute>\n									</input>\n									<input type=\"submit\" name=\"backButton\" accesskey=\"b\">\n										<xsl:attribute name=\"value\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'button_back\')\" />\n										</xsl:attribute>\n									</input>\n									<input type=\"submit\" name=\"cancel\" accesskey=\"x\">\n										<xsl:attribute name=\"value\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'button_cancel\')\" />\n										</xsl:attribute>\n									</input>\n								</xsl:when>\n								<xsl:otherwise>\n									<input type=\"submit\" name=\"submitButton\" class=\"active\" accesskey=\"s\">\n										<xsl:attribute name=\"value\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'button_saveEdit\')\" />\n										</xsl:attribute>\n									</input>\n									<input type=\"submit\" name=\"cancel\" accesskey=\"x\">\n										<xsl:attribute name=\"value\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'button_cancel\')\" />\n										</xsl:attribute>\n									</input>\n								</xsl:otherwise>\n							</xsl:choose>\n						</div>\n					</form>\n				</div>\n			</body>\n		</html>\n	</xsl:template>\n	<!--\n    sub form\n   -->\n	<xsl:template match=\"element[children]\">\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:variable name=\"name\" select=\"@name\" />\n		<xsl:variable name=\"currentNode\" select=\"$currentParent/node()[$name=name()]\" />\n		<xsl:if test=\"$rootNode = \'true\'\">\n			<xsl:call-template name=\"produce-fields\">\n				<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n				<xsl:with-param name=\"currentNode\" select=\"$currentNode\" />\n				<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n			</xsl:call-template>\n		</xsl:if>\n		<xsl:if test=\"$rootNode=\'false\'\">\n			<xsl:call-template name=\"complexElement-field\">\n				<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n				<xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n				<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n			</xsl:call-template>\n		</xsl:if>\n	</xsl:template>\n	<!--\n    date picker\n   -->\n	<xsl:template match=\"element[@type = \'xs:date\']\">\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n      <xsl:call-template name=\"date-field\">\n         <xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n         <xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n         <xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n      </xsl:call-template>\n	</xsl:template>\n	<!--\n    file picker\n   -->\n	<xsl:template match=\"element[@type = \'xs:anyURI\']\">\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:call-template name=\"fileHelper-field\">\n			<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n			<xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n			<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n		</xsl:call-template>\n	</xsl:template>\n	<!--\n    check box\n   -->\n	<xsl:template match=\"element[@type = \'xs:boolean\']\">\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:call-template name=\"checkBox-field\">\n			<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n			<xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n			<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n		</xsl:call-template>\n	</xsl:template>\n	<!--\n    long text\n   -->\n	<xsl:template match=\"element[xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength[@value>99]]\">\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n      <xsl:call-template name=\"longText-field\">\n         <xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n         <xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n         <xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n      </xsl:call-template>\n	</xsl:template>\n	<!--\n    rich text\n   -->\n	<xsl:template match=\"element[xs:annotation/xs:documentation[@source=\'ospi.isRichText\' or @source=\'sakai.isRichText\']]\">\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:call-template name=\"richText-field\">\n			<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n			<xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n			<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n		</xsl:call-template>\n	</xsl:template>\n	<!--\n    select one or more from many (radio and checkbox groups, single and multiple selects) \n   -->\n	<xsl:template match=\"element[xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:enumeration]\">\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:call-template name=\"select-field\">\n			<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n			<xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n			<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n		</xsl:call-template>\n	</xsl:template>\n	<!--\n    catch all\n   -->\n	<xsl:template match=\"element\">\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n      <xsl:call-template name=\"shortText-field\">\n         <xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n         <xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n         <xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n      </xsl:call-template>\n	</xsl:template>\n</xsl:stylesheet>\n'),('/group/PortfolioAdmin/system/formFieldTemplate.xslt','<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<xsl:stylesheet version=\"2.0\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:sakaifn=\"org.sakaiproject.metaobj.utils.xml.XsltFunctions\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns:osp=\"http://www.osportfolio.org/OspML\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n	<!-- todo: final i18n pass -->\n	<xsl:template name=\"complexElement-field\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:variable name=\"name\" select=\"$currentSchemaNode/@name\" />\n		<xsl:variable name=\"maxOccurs\" select=\"$currentSchemaNode/@maxOccurs\" />\n		<xsl:variable name=\"currentCount\" select=\"count($currentParent/node()[$name=name()])\" />\n		<xsl:comment>\n			<xsl:value-of select=\"$currentCount\" />\n		</xsl:comment>\n		<xsl:call-template name=\"produce-inline\">\n			<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n		</xsl:call-template>\n		<xsl:choose>\n			<xsl:when test=\"$currentParent/node()[$name=name()]\">\n				<table class=\"listHier lines nolines\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:50%\">\n					<thead>\n						<tr>\n							<th scope=\"col\">\n                        <xsl:if test=\"($currentSchemaNode/children/element/xs:annotation/xs:documentation[@source=\'ospi.key\']/text())\">\n                           <xsl:attribute name=\"colspan\">\n                              <xsl:value-of \n                                 select=\"count($currentSchemaNode/children/element/xs:annotation/xs:documentation[@source=\'ospi.key\']/text())\"/>\n                           </xsl:attribute>\n                        </xsl:if>\n								<xsl:call-template name=\"produce-label\">\n									<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n								</xsl:call-template>\n							</th>\n							<th scope=\"col\">\n								<div style=\"float:right\">\n									<input type=\"submit\" name=\"addButton\" id=\"{$name}\" alignment=\"center\" onClick=\"this.form.childPath.value=\'{$name}\';return true\">\n										<xsl:attribute name=\"value\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'button_addsubform\')\" />\n										</xsl:attribute>\n										<xsl:if test=\"$maxOccurs != -1 and $currentCount >= $maxOccurs\">\n											<xsl:attribute name=\"disabled\">true</xsl:attribute>\n										</xsl:if>\n									</input>\n								</div>\n							</th>\n						</tr>\n                  <xsl:if test=\"($currentSchemaNode/children/element/xs:annotation/xs:documentation[@source=\'ospi.key\']/text())\">\n                     <tr>\n                     <xsl:for-each select=\"$currentSchemaNode/children/element[xs:annotation/xs:documentation[@source=\'ospi.key\']/text()]\">\n                        <xsl:sort select=\"xs:annotation/xs:documentation[@source=\'ospi.key\']/text()\" data-type=\"number\"/>\n                        <th>\n                           <xsl:call-template name=\"produce-label\">\n                              <xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n                           </xsl:call-template>\n                        </th>\n                     </xsl:for-each>\n                        <th></th>\n                     </tr>\n                  </xsl:if>\n					</thead>\n					<tbody>\n						<xsl:for-each select=\"$currentParent/node()[$name=name()]\">\n							<xsl:call-template name=\"subListRow\">\n								<xsl:with-param name=\"index\" select=\"position() - 1\" />\n								<xsl:with-param name=\"fieldName\" select=\"$name\" />\n								<xsl:with-param name=\"dataNode\" select=\".\" />\n                        <xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\"/>\n                     </xsl:call-template>\n						</xsl:for-each>\n					</tbody>\n				</table>\n			</xsl:when>\n			<xsl:otherwise>\n				<table class=\"listHier lines nolines\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:50%\">\n					<thead>\n						<tr>\n							<th scope=\"col\">\n								<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n									<span class=\"reqStar\">*</span>\n								</xsl:if>\n								<xsl:call-template name=\"produce-label\">\n									<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n								</xsl:call-template>\n							</th>\n							<th scope=\"col\">\n								<div style=\"float:right\">\n									<input type=\"submit\" name=\"addButton\" alignment=\"center\" onClick=\"this.form.childPath.value=\'{$name}\';return true\">\n										<xsl:attribute name=\"value\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'button_addsubform\')\" />\n										</xsl:attribute>\n										<xsl:if test=\"$maxOccurs != -1 and $currentCount >= $maxOccurs\">\n											<xsl:attribute name=\"disabled\">true</xsl:attribute>\n										</xsl:if>\n									</input>\n								</div>\n							</th>\n						</tr>\n					</thead>\n					<tbody>\n						<tr>\n							<td />\n							<td />\n						</tr>\n					</tbody>\n				</table>\n			</xsl:otherwise>\n		</xsl:choose>\n		<xsl:if test=\"not(@maxOccurs=\'1\')\">\n			<div id=\"{$name}-hidden-fields\" class=\"skipthis\">\n				<input id=\"{$name}-count\" type=\"text\" value=\"1\" />\n				<input id=\"{$name}-max\" type=\"text\" value=\"{@maxOccurs}\" />\n			</div>\n		</xsl:if>\n	</xsl:template>\n	<!--produce an input type text element -->\n	<xsl:template name=\"shortText-field-empty-list\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:variable name=\"name\" select=\"$currentSchemaNode/@name\" />\n		<xsl:variable name=\"currentNode\" select=\"$currentParent/node()[$name=name()]\" />\n		<!-- render some explanatory text associated with this input if documentation/@source=ospi.inlinedescription has a text node-->\n		<xsl:call-template name=\"produce-inline\">\n			<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n		</xsl:call-template>\n		<div id=\"{$name}-node\">\n			<xsl:attribute name=\"class\">\n            <xsl:call-template\n               name=\"fieldClass\"><xsl:with-param\n               name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n               name=\"baseType\" select=\"\'shorttext\'\" /></xsl:call-template>\n			</xsl:attribute>\n			<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n				<span class=\"reqStar\">*</span>\n			</xsl:if>\n			<xsl:call-template name=\"produce-label\">\n				<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n			</xsl:call-template>\n			<input type=\"text\" id=\"{$name}\" name=\"{$name}\" value=\"{$currentNode}\">\n				<xsl:call-template name=\"calculateRestrictions\">\n					<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n				</xsl:call-template>\n			</input>\n			<!-- if @maxOccurs is not 1, then it is either a discreet number or unbounded, so add a link that will clone the node in the DOM\n                -->\n			<xsl:if test=\"not(@maxOccurs=\'1\')\">\n				<a href=\"javascript:addItem(\'{$name}-node\',\'{$name}\');\" class=\"addEl\" id=\"{$name}-addlink\">\n					<xsl:attribute name=\"title\">\n						<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'add_form_element\')\" />\n					</xsl:attribute>\n					<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n				</a>\n				<div class=\"instruction\" style=\"display:inline\" id=\"{$name}-disp\">\n					<xsl:text> </xsl:text>\n				</div>\n			</xsl:if>\n		</div>\n		<!-- render hidden fields to aid the cloning -->\n		<xsl:if test=\"not(@maxOccurs=\'1\')\">\n			<div id=\"{$name}-hidden-fields\" class=\"skipthis\">\n				<input id=\"{$name}-count\" type=\"text\" value=\"1\" />\n				<input id=\"{$name}-max\" type=\"text\" value=\"{@maxOccurs}\" />\n			</div>\n		</xsl:if>\n	</xsl:template>\n	<xsl:template name=\"shortText-field\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:variable name=\"name\" select=\"$currentSchemaNode/@name\" />\n		<xsl:variable name=\"currentNode\" select=\"$currentParent/node()[$name=name()]\" />\n		<xsl:variable name=\"count\" select=\"count($currentParent//node()[$name=name()])\" />\n		<xsl:choose>\n			<!-- if there are no values for this named element then the user did not fill them out while creating - so call the \"create\" template then -->\n			<xsl:when test=\"$count=\'0\'\">\n				<xsl:call-template name=\"shortText-field-empty-list\">\n					<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n					<xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n					<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n				</xsl:call-template>\n			</xsl:when>\n			<xsl:otherwise>\n				<!-- render some inline text if documentation/@source=ospi.inlinedescription -->\n				<xsl:call-template name=\"produce-inline\">\n					<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n				</xsl:call-template>\n			</xsl:otherwise>\n		</xsl:choose>\n		<!-- cycle through all the data nodes that have this name, rendering input groups that  can be required, cloned, deleted, have a label, or inlined description -->\n		<xsl:for-each select=\"$currentParent/node()[$name=name()]\">\n			<div>\n				<!-- the id attribute will be used by javascript -->\n				<xsl:attribute name=\"id\">\n					<xsl:choose>\n						<xsl:when test=\"position()=\'1\'\">\n							<xsl:value-of select=\"name()\" />-node</xsl:when>\n						<xsl:otherwise>\n							<xsl:value-of select=\"name()\" />-<xsl:value-of select=\"position()\" />-node</xsl:otherwise>\n					</xsl:choose>\n				</xsl:attribute>\n				<xsl:attribute name=\"class\">\n               <xsl:call-template\n                  name=\"fieldClass\"><xsl:with-param\n                  name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n                  name=\"baseType\" select=\"\'shorttext\'\" /></xsl:call-template>\n				</xsl:attribute>\n				<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n					<span class=\"reqStar\">*</span>\n				</xsl:if>\n				<!-- call template that will produce the label in edit mode -->\n				<xsl:call-template name=\"produce-label-edit\">\n					<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n					<xsl:with-param name=\"sep\">-</xsl:with-param>\n					<xsl:with-param name=\"num\" select=\"position()\" />\n				</xsl:call-template>\n				<input type=\"text\" name=\"{$name}\" value=\"{.}\">\n					<xsl:attribute name=\"id\">\n						<xsl:value-of select=\"name()\" />-<xsl:value-of select=\"position()\" />\n					</xsl:attribute>\n					<xsl:call-template name=\"calculateRestrictions\">\n						<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n					</xsl:call-template>\n				</input>\n				<xsl:if test=\"not($currentSchemaNode/@maxOccurs=\'1\')\">\n					<!-- calculate if this is an original node that can be cloned, an original node that has been cloned up to the max or a cloned node that can be deleted and render the appropriate links -->\n					<xsl:choose>\n						<xsl:when test=\"position() = \'1\'\">\n							<xsl:choose>\n								<xsl:when test=\"$currentSchemaNode/@maxOccurs=$count\">\n									<a id=\"{$name}-addlink\" class=\"addEl-inact\">\n										<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n									</a>\n									<div class=\"instruction\" style=\"display:inline\" id=\"{$name}-disp\">\n										<xsl:text> </xsl:text>\n									</div>\n								</xsl:when>\n								<xsl:otherwise>\n									<a href=\"javascript:addItem(\'{$name}-node\',\'{$name}\');\" class=\"addEl\" id=\"{$name}-addlink\">\n										<xsl:attribute name=\"title\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'add_form_element\')\" />\n										</xsl:attribute>\n										<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n									</a>\n									<div class=\"instruction\" style=\"display:inline\" id=\"{$name}-disp\">\n										<xsl:text> </xsl:text>\n									</div>\n								</xsl:otherwise>\n							</xsl:choose>\n						</xsl:when>\n						<xsl:otherwise>\n							<a href=\"javascript:removeItem(\'{$name}-{position()}-node\',\'{$name}\');\" class=\"deleteEl\" id=\"{$name}-addlink\">\n								<xsl:attribute name=\"title\">\n									<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'delete_form_element\')\" />\n								</xsl:attribute>\n								<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n							</a>\n						</xsl:otherwise>\n					</xsl:choose>\n				</xsl:if>\n			</div>\n		</xsl:for-each>\n		<xsl:if test=\"not($currentSchemaNode/@maxOccurs=\'1\')\">\n			<div id=\"{$name}-hidden-fields\" class=\"skipthis\">\n				<input id=\"{$name}-count\" type=\"text\" value=\"{$count}\" />\n				<input id=\"{$name}-max\" type=\"text\" value=\"{$currentSchemaNode/@maxOccurs}\" />\n			</div>\n		</xsl:if>\n	</xsl:template>\n	<!-- same in most respects as shorttext element except  1) cannot be cloned, 2) cannot be required (there is always a default) - so create and edit templates are one and the same.\n		todo: required work\n		-->\n	<xsl:template name=\"select-field\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:variable name=\"name\" select=\"$currentSchemaNode/@name\" />\n		<xsl:variable name=\"currentNode\" select=\"$currentParent/node()[$name=name()]\" />\n		<!-- this variable in com with maxOccurs\' value controls the xhtml expression of this element (radiogroup, checkboxgroup, single select, multiple select) -->\n		<xsl:variable name=\"htmldeterm\">4</xsl:variable>\n		<xsl:call-template name=\"produce-inline\">\n			<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n		</xsl:call-template>\n		<div id=\"{$name}-node\">\n			<xsl:choose>\n				<xsl:when test=\"@maxOccurs=\'1\' and count($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:enumeration) &lt;= $htmldeterm\">\n					<!-- this will resolve as a radio group control -->\n					<fieldset>\n						<xsl:attribute name=\"class\">\n                     <xsl:call-template\n                        name=\"fieldClass\"><xsl:with-param\n                        name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n                        name=\"baseType\" select=\"\'osp-radcheck\'\" /></xsl:call-template>\n						</xsl:attribute>\n						<legend>\n							<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n								<span class=\"reqStar\">*</span>\n							</xsl:if>\n							<xsl:call-template name=\"produce-label\">\n								<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n							</xsl:call-template>\n						</legend>\n						<xsl:for-each select=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:enumeration\">\n							<div class=\"checkbox\">\n								<input id=\"{$name}-{position()}\" name=\"{$name}\" value=\"{@value}\" type=\"radio\">\n									<xsl:if test=\"$currentNode = @value\">\n										<xsl:attribute name=\"checked\">checked</xsl:attribute>\n									</xsl:if>\n								</input>\n								<label for=\"{$name}-{position()}\">\n									<xsl:choose>\n                              <xsl:when test=\"./xs:annotation/xs:documentation[@source=\'sakai.label\']\">\n                                 <xsl:value-of select=\"./xs:annotation/xs:documentation[@source=\'sakai.label\']\" />\n                              </xsl:when>\n                              <xsl:when test=\"./xs:annotation/xs:documentation[@source=\'ospi.label\']\">\n                                 <xsl:value-of select=\"./xs:annotation/xs:documentation[@source=\'ospi.label\']\" />\n                              </xsl:when>\n                              <xsl:when test=\"./xs:annotation/xs:documentation\">\n                                 <xsl:value-of select=\"./xs:annotation/xs:documentation\" />\n                              </xsl:when>\n                              <xsl:otherwise>\n                                 <xsl:value-of select=\"@value\" />\n                              </xsl:otherwise>\n                           </xsl:choose>\n								</label>\n							</div>\n						</xsl:for-each>\n					</fieldset>\n				</xsl:when>\n				<xsl:when test=\"@maxOccurs=\'1\' and count($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:enumeration) &gt; $htmldeterm\">\n					<!-- this will resolve as a single select control-->\n					<xsl:attribute name=\"class\">\n                  <xsl:call-template\n                     name=\"fieldClass\"><xsl:with-param\n                     name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n                     name=\"baseType\" select=\"\'shorttext\'\" /></xsl:call-template>\n					</xsl:attribute>\n					<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n						<span class=\"reqStar\">*</span>\n					</xsl:if>\n					<xsl:call-template name=\"produce-label\">\n						<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n					</xsl:call-template>\n					<select id=\"{$name}\" name=\"{$name}\">\n						<xsl:for-each select=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:enumeration\">\n							<option value=\"{@value}\">\n								<xsl:if test=\"$currentNode = @value\">\n									<xsl:attribute name=\"selected\">selected</xsl:attribute>\n								</xsl:if>\n								<xsl:choose>\n                              <xsl:when test=\"./xs:annotation/xs:documentation[@source=\'sakai.label\']\">\n                                 <xsl:value-of select=\"./xs:annotation/xs:documentation[@source=\'sakai.label\']\" />\n                              </xsl:when>\n                              <xsl:when test=\"./xs:annotation/xs:documentation[@source=\'ospi.label\']\">\n                                 <xsl:value-of select=\"./xs:annotation/xs:documentation[@source=\'ospi.label\']\" />\n                              </xsl:when>\n                              <xsl:when test=\"./xs:annotation/xs:documentation\">\n                                 <xsl:value-of select=\"./xs:annotation/xs:documentation\" />\n                              </xsl:when>\n                              <xsl:otherwise>\n                                 <xsl:value-of select=\"@value\" />\n                              </xsl:otherwise>\n                           </xsl:choose>\n							</option>\n						</xsl:for-each>\n					</select>\n				</xsl:when>\n				<xsl:when test=\"@maxOccurs !=\'1\' and count($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:enumeration) &lt;= $htmldeterm\">\n					<!-- this will resolve as a checkbox group control -->\n					<fieldset>\n						<xsl:attribute name=\"class\">\n                     <xsl:call-template\n                        name=\"fieldClass\"><xsl:with-param\n                        name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n                        name=\"baseType\" select=\"\'osp-radcheck\'\" /></xsl:call-template>\n						</xsl:attribute>\n						<legend>\n							<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n								<span class=\"reqStar\">*</span>\n							</xsl:if>\n							<xsl:call-template name=\"produce-label\">\n								<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n							</xsl:call-template>\n						</legend>\n						<xsl:for-each select=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:enumeration\">\n							<div class=\"checkbox\">\n								<input id=\"{$name}-{position()}\" name=\"{$name}-{position()}\" type=\"checkbox\">\n									<xsl:attribute name=\"onChange\">\n										(this.checked==true) ? document.getElementById(\'<xsl:value-of select=\"$name\" />-<xsl:value-of select=\"position()\" />-w\').value=\'<xsl:value-of select=\"@value\" />\'\n										: document.getElementById(\'<xsl:value-of select=\"$name\" />-<xsl:value-of select=\"position()\" />-w\').value=\'\'</xsl:attribute>\n									<xsl:if test=\"$currentNode = @value\">\n										<xsl:attribute name=\"checked\">checked</xsl:attribute>\n									</xsl:if>\n								</input>\n								<label for=\"{$name}-{position()}\">\n									<xsl:value-of select=\"@value\" />\n								</label>\n								<input type=\"hidden\">\n									<xsl:attribute name=\"name\">\n										<xsl:value-of select=\"$name\" />\n									</xsl:attribute>\n									<xsl:attribute name=\"id\">\n										<xsl:value-of select=\"concat($name,\'-\',position(),\'-w\')\" />\n									</xsl:attribute>\n									<xsl:attribute name=\"value\">\n										<xsl:if test=\"$currentNode = @value\">\n											<xsl:value-of select=\"@value\" />\n										</xsl:if>\n									</xsl:attribute>\n								</input>\n							</div>\n						</xsl:for-each>\n					</fieldset>\n				</xsl:when>\n				<xsl:when test=\"@maxOccurs !=\'1\' and count($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:enumeration) &gt; $htmldeterm\">\n					<!-- this will resolve as a multiple select control -->\n					<xsl:attribute name=\"class\">\n                  <xsl:call-template\n                     name=\"fieldClass\"><xsl:with-param\n                     name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n                     name=\"baseType\" select=\"\'shorttext\'\" /></xsl:call-template>\n					</xsl:attribute>\n					<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n						<span class=\"reqStar\">*</span>\n					</xsl:if>\n					<xsl:call-template name=\"produce-label\">\n						<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n					</xsl:call-template>\n					<select id=\"{$name}\" name=\"{$name}\" multiple=\"multiple\">\n						<xsl:attribute name=\"size\">\n							<xsl:choose>\n								<!-- some crude calculations to determine the select visible row count -->\n								<xsl:when test=\"count($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:enumeration) &lt; 10\">5</xsl:when>\n								<xsl:when test=\"count($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:enumeration) &lt; 20\">10</xsl:when>\n								<xsl:otherwise>15</xsl:otherwise>\n							</xsl:choose>\n						</xsl:attribute>\n						<xsl:for-each select=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:enumeration\">\n							<option value=\"{@value}\">\n								<xsl:if test=\"$currentNode = @value\">\n									<xsl:attribute name=\"selected\">selected</xsl:attribute>\n								</xsl:if>\n								<xsl:choose>\n                              <xsl:when test=\"./xs:annotation/xs:documentation[@source=\'sakai.label\']\">\n                                 <xsl:value-of select=\"./xs:annotation/xs:documentation[@source=\'sakai.label\']\" />\n                              </xsl:when>\n                              <xsl:when test=\"./xs:annotation/xs:documentation[@source=\'ospi.label\']\">\n                                 <xsl:value-of select=\"./xs:annotation/xs:documentation[@source=\'ospi.label\']\" />\n                              </xsl:when>\n                              <xsl:when test=\"./xs:annotation/xs:documentation\">\n                                 <xsl:value-of select=\"./xs:annotation/xs:documentation\" />\n                              </xsl:when>\n                              <xsl:otherwise>\n                                 <xsl:value-of select=\"@value\" />\n                              </xsl:otherwise>\n                           </xsl:choose>\n							</option>\n						</xsl:for-each>\n					</select>\n				</xsl:when>\n			</xsl:choose>\n		</div>\n	</xsl:template>\n	<!-- same in most respects as shorttext element except  cannot be cloned -->\n	<xsl:template name=\"richText-field\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:variable name=\"name\" select=\"$currentSchemaNode/@name\" />\n		<xsl:variable name=\"currentNode\" select=\"$currentParent/node()[$name=name()]\" />\n		<xsl:call-template name=\"produce-inline\">\n			<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n		</xsl:call-template>\n		<div id=\"{$name}-div\">\n			<xsl:attribute name=\"class\">\n            <xsl:call-template\n               name=\"fieldClass\"><xsl:with-param\n               name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n               name=\"baseType\" select=\"\'longtext\'\" /></xsl:call-template>\n			</xsl:attribute>\n			<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n				<span class=\"reqStar\">*</span>\n			</xsl:if>\n			<xsl:call-template name=\"produce-label\">\n				<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n				<xsl:with-param name=\"nodeType\">longtext</xsl:with-param>\n			</xsl:call-template>\n			<table>\n				<tr>\n					<td>\n						<textarea rows=\"30\" cols=\"80\" id=\"{$name}\" name=\"{$name}\">\n							<xsl:choose>\n								<xsl:when test=\"string($currentNode) = \'\'\">\n									<xsl:text disable-output-escaping=\"yes\">&amp;nbsp;\n               </xsl:text>\n								</xsl:when>\n								<xsl:otherwise>\n									<xsl:value-of select=\"$currentNode\" disable-output-escaping=\"yes\" />\n								</xsl:otherwise>\n							</xsl:choose>\n						</textarea>\n						<xsl:if test=\"string($currentNode) = \'\'\">\n							<script language=\"JavaScript\" type=\"text/javascript\"> document.forms[0].<xsl:value-of select=\"$name\" />.value=\"\" </script>\n						</xsl:if>\n						<xsl:value-of select=\"sakaifn:getRichTextScript($name, $currentSchemaNode)\" disable-output-escaping=\"yes\" />\n						<xsl:if test=\"@maxOccurs=\'-1\'\">\n							<a href=\"javascript:addItem(\'{$name}parent\');\" class=\"addEl\">\n								<xsl:attribute name=\"title\">\n									<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'add_form_element\')\" />\n								</xsl:attribute>\n								<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n							</a>\n							<input type=\"hidden\" id=\"{$name}parenthid\" value=\"0\" />\n						</xsl:if>\n					</td>\n				</tr>\n			</table>\n		</div>\n	</xsl:template>\n	<!-- renders a textarea, similar in most respects to the shorttext element, except where noted below in comments -->\n	<xsl:template name=\"longText-field-empty-list\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"thisname\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:variable name=\"name\" select=\"$currentSchemaNode/@name\" />\n		<xsl:variable name=\"currentNode\" select=\"$currentParent/node()[$name=name()]\" />\n		<xsl:call-template name=\"produce-inline\">\n			<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n		</xsl:call-template>\n		<div id=\"{$name}-node\">\n			<xsl:attribute name=\"class\">\n            <xsl:call-template\n               name=\"fieldClass\"><xsl:with-param\n               name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n               name=\"baseType\" select=\"\'longtext\'\" /></xsl:call-template>\n			</xsl:attribute>\n			<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n				<span class=\"reqStar\">*</span>\n			</xsl:if>\n			<!-- passing a nodeType param to the label producing template creates a label with the css class \"block\" so that it renders label and textarea in 2 separate lines -->\n			<xsl:call-template name=\"produce-label\">\n				<xsl:with-param name=\"nodeType\">longtext</xsl:with-param>\n				<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n			</xsl:call-template>\n			<!-- maxlength expressed as a title attribute as in shorttext, no default maxlength, however, and some rough calculations for rendered sized of the textarea based on the maxLength value -->\n			<textarea id=\"{$name}\" name=\"{$name}\">\n				<xsl:call-template name=\"calculateRestrictions\">\n					<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n					<xsl:with-param name=\"nodeType\" select=\"longtext\" />\n				</xsl:call-template>\n				<xsl:choose>\n					<xsl:when test=\"string($currentNode) = \'\'\" />\n					<xsl:otherwise>\n						<xsl:value-of select=\"$currentNode\" disable-output-escaping=\"yes\" />\n					</xsl:otherwise>\n				</xsl:choose>\n			</textarea>\n			<xsl:if test=\"not(@maxOccurs=\'1\')\">\n				<a href=\"javascript:addItem(\'{$name}-node\',\'{$name}\');\" class=\"addEl\" id=\"{$name}-addlink\">\n					<xsl:attribute name=\"title\">\n						<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'add_form_element\')\" />\n					</xsl:attribute>\n					<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n				</a>\n				<div class=\"instruction\" style=\"display:inline\" id=\"{$name}-disp\">\n					<xsl:text> </xsl:text>\n				</div>\n			</xsl:if>\n		</div>\n		<xsl:if test=\"not(@maxOccurs=\'1\')\">\n			<div id=\"{$name}-hidden-fields\" class=\"skipthis\">\n				<input id=\"{$name}-count\" type=\"text\" value=\"1\" />\n				<input id=\"{$name}-max\" type=\"text\" value=\"{@maxOccurs}\" />\n			</div>\n		</xsl:if>\n	</xsl:template>\n	<!-- same considerations as in the shorttext edit template -->\n	<xsl:template name=\"longText-field\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:variable name=\"name\" select=\"$currentSchemaNode/@name\" />\n		<xsl:variable name=\"currentNode\" select=\"$currentParent/node()[$name=name()]\" />\n		<xsl:variable name=\"count\" select=\"count($currentParent//node()[$name=name()])\" />\n		<xsl:choose>\n			<xsl:when test=\"$count=\'0\'\">\n				<xsl:call-template name=\"longText-field-empty-list\">\n					<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n					<xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n					<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n				</xsl:call-template>\n			</xsl:when>\n			<xsl:otherwise>\n				<xsl:call-template name=\"produce-inline\">\n					<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n				</xsl:call-template>\n			</xsl:otherwise>\n		</xsl:choose>\n		<xsl:for-each select=\"$currentParent/node()[$name=name()]\">\n			<div>\n				<xsl:attribute name=\"id\">\n					<xsl:choose>\n						<xsl:when test=\"position()=\'1\'\">\n							<xsl:value-of select=\"name()\" />-node</xsl:when>\n						<xsl:otherwise>\n							<xsl:value-of select=\"name()\" />-<xsl:value-of select=\"position()\" />-node</xsl:otherwise>\n					</xsl:choose>\n				</xsl:attribute>\n				<xsl:attribute name=\"class\">\n               <xsl:call-template\n                  name=\"fieldClass\"><xsl:with-param\n                  name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n                  name=\"baseType\" select=\"\'longtext\'\" /></xsl:call-template>\n				</xsl:attribute>\n				<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n					<span class=\"reqStar\">*</span>\n				</xsl:if>\n				<xsl:call-template name=\"produce-label-edit\">\n					<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n					<xsl:with-param name=\"sep\">-</xsl:with-param>\n					<xsl:with-param name=\"nodeType\">longtext</xsl:with-param>\n					<xsl:with-param name=\"num\" select=\"position()\" />\n				</xsl:call-template>\n				<textarea name=\"{$name}\">\n					<xsl:attribute name=\"id\">\n						<xsl:value-of select=\"name()\" />-<xsl:value-of select=\"position()\" />\n					</xsl:attribute>\n					<xsl:call-template name=\"calculateRestrictions\">\n						<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n						<xsl:with-param name=\"nodeType\" select=\"longtext\" />\n					</xsl:call-template>\n					<xsl:choose>\n						<xsl:when test=\"string($currentNode) = \'\'\" />\n						<xsl:otherwise>\n							<xsl:value-of select=\".\" disable-output-escaping=\"yes\" />\n						</xsl:otherwise>\n					</xsl:choose>\n				</textarea>\n				<xsl:if test=\"not($currentSchemaNode/@maxOccurs=\'1\')\">\n					<xsl:choose>\n						<xsl:when test=\"position() = \'1\'\">\n							<xsl:choose>\n								<xsl:when test=\"$currentSchemaNode/@maxOccurs=$count\">\n									<a id=\"{$name}-addlink\" class=\"addEl-inact\">\n										<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n									</a>\n									<div class=\"instruction\" style=\"display:inline\" id=\"{$name}-disp\">\n										<xsl:text> </xsl:text>\n									</div>\n								</xsl:when>\n								<xsl:otherwise>\n									<a href=\"javascript:addItem(\'{$name}-node\',\'{$name}\');\" class=\"addEl\" id=\"{$name}-addlink\">\n										<xsl:attribute name=\"title\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'add_form_element\')\" />\n										</xsl:attribute>\n										<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n									</a>\n									<div class=\"instruction\" style=\"display:inline\" id=\"{$name}-disp\">\n										<xsl:text> </xsl:text>\n									</div>\n								</xsl:otherwise>\n							</xsl:choose>\n						</xsl:when>\n						<xsl:otherwise>\n							<a href=\"javascript:removeItem(\'{$name}-{position()}-node\',\'{$name}\');\" class=\"deleteEl\" id=\"{$name}-addlink\">\n								<xsl:attribute name=\"title\">\n									<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'delete_form_element\')\" />\n								</xsl:attribute>\n								<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n							</a>\n						</xsl:otherwise>\n					</xsl:choose>\n				</xsl:if>\n			</div>\n		</xsl:for-each>\n		<xsl:if test=\"not($currentSchemaNode/@maxOccurs=\'1\')\">\n			<div id=\"{$name}-hidden-fields\" class=\"skipthis\">\n				<input id=\"{$name}-count\" type=\"text\" value=\"1\" />\n				<input id=\"{$name}-max\" type=\"text\" value=\"{$currentSchemaNode/@maxOccurs}\" />\n			</div>\n		</xsl:if>\n	</xsl:template>\n	<xsl:template name=\"checkBox-field\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:variable name=\"name\" select=\"$currentSchemaNode/@name\" />\n		<xsl:variable name=\"currentNode\" select=\"$currentParent/node()[$name=name()]\" />\n		<xsl:call-template name=\"produce-inline\">\n			<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n		</xsl:call-template>\n		<div id=\"{$name}parent\">\n			<xsl:attribute name=\"class\">\n            <xsl:call-template\n               name=\"fieldClass\"><xsl:with-param\n               name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n               name=\"baseType\" select=\"\'checkbox indnt1\'\" /></xsl:call-template>\n			</xsl:attribute>\n			<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n				<span class=\"reqStar\">*</span>\n			</xsl:if>\n			<xsl:call-template name=\"checkbox-widget\">\n				<xsl:with-param name=\"name\" select=\"$name\" />\n				<xsl:with-param name=\"currentNode\" select=\"$currentNode\" />\n			</xsl:call-template>\n			<xsl:call-template name=\"produce-label\">\n				<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n				<xsl:with-param name=\"fieldName\" select=\"concat($name, \'_checkbox\')\" />\n			</xsl:call-template>\n			<xsl:if test=\"@maxOccurs=\'-1\'\">\n				<a href=\"javascript:addItem(\'{$name}parent\');\" class=\"addEl\">\n					<xsl:attribute name=\"title\">\n						<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'add_form_element\')\" />\n					</xsl:attribute>\n					<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n				</a>\n				<input type=\"hidden\" id=\"{$name}parenthid\" value=\"0\" />\n			</xsl:if>\n		</div>\n	</xsl:template>\n	<xsl:template name=\"checkbox-widget\">\n		<xsl:param name=\"name\" />\n		<xsl:param name=\"currentNode\" />\n		<input type=\"checkbox\" id=\"{$name}_checkbox\" name=\"{$name}_checkbox\">\n			<xsl:if test=\"$currentNode = \'true\'\">\n				<xsl:attribute name=\"checked\" />\n			</xsl:if>\n			<xsl:attribute name=\"onChange\">form[\'<xsl:value-of select=\"$name\" />\'].value=this.checked </xsl:attribute>\n		</input>\n		<input type=\"hidden\" name=\"{$name}\" value=\"{$currentNode}\" />\n	</xsl:template>\n	<!-- simple template, maxOccurs happens as a parameter used by the filepicker helper application -->\n	<xsl:template name=\"fileHelper-field\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:variable name=\"name\" select=\"$currentSchemaNode/@name\" />\n		<xsl:variable name=\"currentNode\" select=\"$currentParent/node()[$name=name()]\" />\n		<xsl:call-template name=\"produce-inline\">\n			<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n		</xsl:call-template>\n		<div>\n			<div id=\"{$name}parent\">\n				<xsl:attribute name=\"class\">\n               <xsl:call-template\n                  name=\"fieldClass\"><xsl:with-param\n                  name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n                  name=\"baseType\" select=\"\'shorttext\'\" /></xsl:call-template>\n				</xsl:attribute>\n				<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n					<span class=\"reqStar\">*</span>\n				</xsl:if>\n				<xsl:call-template name=\"produce-label\">\n					<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n				</xsl:call-template>\n            <xsl:variable name=\"fieldLabel\"><xsl:choose><xsl:when \n               test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'sakai.label\']\"><xsl:value-of \n               select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'sakai.label\']\" /></xsl:when><xsl:when \n               test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.label\']\"><xsl:value-of \n               select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.label\']\" \n               /></xsl:when><xsl:otherwise><xsl:value-of select=\"$name\" /></xsl:otherwise></xsl:choose></xsl:variable>\n            <input id=\"{$name}\" type=\"button\" onclick=\"javascript:document.forms[0].childPath.value=\'{$name}\';document.forms[0].childFieldLabel.value=\'{normalize-space($fieldLabel)}\';document.forms[0].fileHelper.value=\'true\';document.forms[0].onsubmit();document.forms[0].submit();\">\n					<xsl:choose>\n						<xsl:when test=\"$currentParent/node()[$name=name()]\">\n							<xsl:attribute name=\"value\">\n								<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'manage_attachments\')\" />\n							</xsl:attribute>\n							<xsl:attribute name=\"title\">\n								<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'manage_attachments_title\')\" />\n							</xsl:attribute>\n						</xsl:when>\n						<xsl:otherwise>\n							<xsl:attribute name=\"value\">\n								<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'add_attachments\')\" />\n							</xsl:attribute>\n							<xsl:attribute name=\"title\">\n								<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'add_attachments_title\')\" />\n							</xsl:attribute>\n						</xsl:otherwise>\n					</xsl:choose>\n				</input>\n				<!-- if there are attachments already, render these as a list -->\n				<!--todo: mime type lookup to resolve the icons  -->\n				<xsl:if test=\"$currentParent/node()[$name=name()]\">\n					<ul class=\"attachList labelindnt\" style=\"clear:both;padding-top:.5em\">\n						<xsl:for-each select=\"$currentParent/node()[$name=name()]\">\n							<li>\n								<img>\n									<xsl:attribute name=\"src\">\n										<xsl:value-of select=\"sakaifn:getImageUrl(.)\" />\n									</xsl:attribute>\n								</img>\n								<input type=\"hidden\" name=\"{$name}\" value=\"{.}\" />\n								<a target=\"_blank\">\n									<xsl:attribute name=\"href\">\n										<xsl:value-of select=\"sakaifn:getReferenceUrl(.)\" />\n									</xsl:attribute>\n									<xsl:value-of select=\"sakaifn:getReferenceName(.)\" />\n								</a>\n							</li>\n						</xsl:for-each>\n					</ul>\n				</xsl:if>\n			</div>\n		</div>\n		<!-- todo: remove this test if not needed -->\n		<xsl:if test=\"not(@maxOccurs=\'1\')\">\n			<div id=\"{$name}-hidden-fields\" class=\"skipthis\">\n				<input id=\"{$name}-count\" type=\"text\" value=\"1\" />\n				<input id=\"{$name}-max\" type=\"text\" value=\"{@maxOccurs}\" />\n			</div>\n		</xsl:if>\n	</xsl:template>\n	<!-- similar to shorttext except as noted -->\n	<xsl:template name=\"date-field-empty-list\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:variable name=\"name\" select=\"$currentSchemaNode/@name\" />\n		<xsl:variable name=\"currentNode\" select=\"$currentParent/node()[$name=name()]\" />\n		<xsl:call-template name=\"produce-inline\">\n			<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n		</xsl:call-template>\n		<div id=\"{$name}-node\">\n			<xsl:attribute name=\"class\">\n            <xsl:call-template\n               name=\"fieldClass\"><xsl:with-param\n               name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n               name=\"baseType\" select=\"\'shorttext\'\" /></xsl:call-template>\n			</xsl:attribute>\n			<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n				<span class=\"reqStar\">*</span>\n			</xsl:if>\n			<xsl:call-template name=\"produce-label\">\n				<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n			</xsl:call-template>\n			<!-- calls a template that will produce a link to the calendar popup -->\n			<xsl:call-template name=\"calendar-widget\">\n				<xsl:with-param name=\"schemaNode\" select=\"$currentSchemaNode\" />\n				<xsl:with-param name=\"dataNode\" select=\"$currentNode\" />\n			</xsl:call-template>\n			<!-- if it can be cloned, render a link to do so -->\n			<xsl:if test=\"not(@maxOccurs=\'1\')\">\n				<a href=\"javascript:addItem(\'{$name}-node\',\'{$name}\');\" class=\"addEl\" id=\"{$name}-addlink\">\n					<xsl:attribute name=\"title\">\n						<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'add_form_element\')\" />\n					</xsl:attribute>\n					<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n				</a>\n				<div class=\"instruction\" style=\"display:inline\" id=\"{$name}-disp\">\n					<xsl:text> </xsl:text>\n				</div>\n			</xsl:if>\n		</div>\n		<xsl:if test=\"not(@maxOccurs=\'1\')\">\n			<div id=\"{$name}-hidden-fields\" class=\"skipthis\">\n				<input id=\"{$name}-count\" type=\"text\" value=\"1\" />\n				<input id=\"{$name}-max\" type=\"text\" value=\"{@maxOccurs}\" />\n			</div>\n		</xsl:if>\n	</xsl:template>\n	<!-- as with the shorttext and longtext templates, the edit mode was different enough that it gets its own template -->\n	<xsl:template name=\"date-field\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:variable name=\"name\" select=\"$currentSchemaNode/@name\" />\n		<xsl:variable name=\"currentNode\" select=\"$currentParent/node()[$name=name()]\" />\n		<xsl:variable name=\"count\" select=\"count($currentParent//node()[$name=name()])\" />\n		<xsl:choose>\n			<!-- this element was not filled out on create, so does not exist in the data, so call original create template -->\n			<xsl:when test=\"$count=\'0\'\">\n				<xsl:call-template name=\"date-field-empty-list\">\n					<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n					<xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n					<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n				</xsl:call-template>\n			</xsl:when>\n			<xsl:otherwise>\n				<xsl:call-template name=\"produce-inline\">\n					<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n				</xsl:call-template>\n			</xsl:otherwise>\n		</xsl:choose>\n		<!--calendar popup needs a unique id to call it - create the unique id here and used it with increments for each element in this cloned collection -->\n		<xsl:variable name=\"fieldId\" select=\"generate-id()\" />\n		<xsl:for-each select=\"$currentParent/node()[$name=name()]\">\n			<div>\n				<xsl:attribute name=\"id\">\n					<xsl:choose>\n						<xsl:when test=\"position()=\'1\'\">\n							<xsl:value-of select=\"name()\" />-node</xsl:when>\n						<xsl:otherwise>\n							<xsl:value-of select=\"name()\" />-<xsl:value-of select=\"position()\" />-node</xsl:otherwise>\n					</xsl:choose>\n				</xsl:attribute>\n				<xsl:attribute name=\"class\">\n               <xsl:call-template\n                  name=\"fieldClass\"><xsl:with-param\n                  name=\"schemaNode\" select=\"$currentSchemaNode\" /><xsl:with-param\n                  name=\"baseType\" select=\"\'shorttext\'\" /></xsl:call-template>\n				</xsl:attribute>\n				<xsl:if test=\"$currentSchemaNode/@minOccurs=\'1\'\">\n					<span class=\"reqStar\">*</span>\n				</xsl:if>\n				<xsl:call-template name=\"produce-label-edit\">\n					<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n					<xsl:with-param name=\"sep\">-</xsl:with-param>\n					<xsl:with-param name=\"num\" select=\"position()\" />\n				</xsl:call-template>\n				<!-- as with the create version of this template - call a template that will render the click link for the calendar popup. Complications: need to pass the value, the number in the clone sequence, as well as the unique id -->\n				<xsl:call-template name=\"calendar-widget-edit\">\n					<xsl:with-param name=\"schemaNode\" select=\"$currentSchemaNode\" />\n					<xsl:with-param name=\"dataNode\" select=\"$currentNode\" />\n					<xsl:with-param name=\"val\" select=\".\" />\n					<xsl:with-param name=\"num\" select=\"position()\" />\n					<xsl:with-param name=\"fieldIdclone\" select=\"$fieldId\" />\n				</xsl:call-template>\n				<!-- same as in shorttext, should really templatize this -->\n				<xsl:if test=\"not($currentSchemaNode/@maxOccurs=\'1\')\">\n					<xsl:choose>\n						<xsl:when test=\"position() = \'1\'\">\n							<xsl:choose>\n								<xsl:when test=\"$currentSchemaNode/@maxOccurs=$count\">\n									<a id=\"{$name}-addlink\" class=\"addEl-inact\">\n										<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n									</a>\n									<div class=\"instruction\" style=\"display:inline\" id=\"{$name}-disp\">\n										<xsl:text> </xsl:text>\n									</div>\n								</xsl:when>\n								<xsl:otherwise>\n									<a href=\"javascript:addItem(\'{$name}-node\',\'{$name}\');\" class=\"addEl\" id=\"{$name}-addlink\">\n										<xsl:attribute name=\"title\">\n											<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'add_form_element\')\" />\n										</xsl:attribute>\n										<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n									</a>\n									<div class=\"instruction\" style=\"display:inline\" id=\"{$name}-disp\">\n										<xsl:text> </xsl:text>\n									</div>\n								</xsl:otherwise>\n							</xsl:choose>\n						</xsl:when>\n						<xsl:otherwise>\n							<a href=\"javascript:removeItem(\'{$name}-{position()}-node\',\'{$name}\');\" class=\"deleteEl\" id=\"{$name}-addlink\">\n								<xsl:attribute name=\"title\">\n									<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'delete_form_element\')\" />\n								</xsl:attribute>\n								<img src=\"/sakai-metaobj-tool/img/blank.gif\" alt=\"\" />\n							</a>\n						</xsl:otherwise>\n					</xsl:choose>\n				</xsl:if>\n			</div>\n		</xsl:for-each>\n		<xsl:if test=\"not($currentSchemaNode/@maxOccurs=\'1\')\">\n			<div id=\"{$name}-hidden-fields\" class=\"skipthis\">\n				<input id=\"{$name}-count\" type=\"text\" value=\"{$count}\" />\n				<input id=\"{$name}-max\" type=\"text\" value=\"{$currentSchemaNode/@maxOccurs}\" />\n			</div>\n		</xsl:if>\n	</xsl:template>\n	<xsl:template name=\"calendar-widget\">\n		<xsl:param name=\"schemaNode\" />\n		<xsl:param name=\"dataNode\" />\n		<xsl:variable name=\"year\" select=\"sakaifn:dateField($dataNode, 1, \'date\')\" />\n		<xsl:variable name=\"currentYear\" select=\"sakaifn:dateField(sakaifn:currentDate(), 1, \'date\')\" />\n		<xsl:variable name=\"month\" select=\"sakaifn:dateField($dataNode, 2, \'date\') + 1\" />\n		<xsl:variable name=\"day\" select=\"sakaifn:dateField($dataNode, 5, \'date\')\" />\n		<xsl:variable name=\"fieldId\" select=\"generate-id()\" />\n		<input type=\"text\" size=\"10\" name=\"{$schemaNode/@name}.fullDate\" id=\"{$schemaNode/@name}\">\n			<xsl:attribute name=\"value\">\n				<xsl:value-of select=\"sakaifn:formatDateWidget($dataNode)\" />\n			</xsl:attribute>\n			<xsl:attribute name=\"title\">\n				<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'date_format_hint\')\" />\n			</xsl:attribute>\n		</input>\n		<!-- hidden field to hold the value of the unique id the calendar needs, and use it by increment to call the calendar in the context of any cloned nodes -->\n		<input type=\"hidden\" value=\"{$fieldId}\" id=\"{$schemaNode/@name}-dateWId\" />\n		<!-- since there are two links to the right of the input  put some space between them to avoid confusion -->\n		<xsl:text>&#xa0;</xsl:text>\n		<img width=\"16\" height=\"16\" style=\"cursor:pointer;\" border=\"0\" src=\"/jsf-resource/inputDate/images/calendar.gif\">\n			<xsl:attribute name=\"alt\">\n				<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'date_pick_alt\')\" />\n			</xsl:attribute>\n			<xsl:attribute name=\"onclick\"><xsl:value-of select=\"sakaifn:getDateWidget($fieldId, $schemaNode/@name)\" /></xsl:attribute>\n		</img>\n		<!-- since there are two links to the right of the input  put some space between them to avoid confusion -->\n		<xsl:text>&#xa0;&#xa0;</xsl:text>\n	</xsl:template>\n	<xsl:template name=\"calendar-widget-edit\">\n		<xsl:param name=\"schemaNode\" />\n		<xsl:param name=\"dataNode\" />\n		<xsl:param name=\"num\" />\n		<xsl:param name=\"val\" />\n		<xsl:param name=\"fieldIdclone\" />\n		<xsl:variable name=\"year\" select=\"sakaifn:dateField($dataNode, 1, \'date\')\" />\n		<xsl:variable name=\"currentYear\" select=\"sakaifn:dateField(sakaifn:currentDate(), 1, \'date\')\" />\n		<xsl:variable name=\"month\" select=\"sakaifn:dateField($dataNode, 2, \'date\') + 1\" />\n		<xsl:variable name=\"day\" select=\"sakaifn:dateField($dataNode, 5, \'date\')\" />\n		<input type=\"text\" size=\"10\" name=\"{$schemaNode/@name}.fullDate\">\n			<xsl:attribute name=\"id\">\n				<xsl:value-of select=\"$schemaNode/@name\" />\n				<xsl:value-of select=\"position()\" />\n			</xsl:attribute>\n			<xsl:attribute name=\"value\">\n				<xsl:value-of select=\"sakaifn:formatDateWidget($val)\" />\n			</xsl:attribute>\n			<xsl:attribute name=\"title\">\n				<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'date_format_hint\')\" />\n			</xsl:attribute>\n		</input>\n		<input type=\"hidden\" value=\"\" id=\"{$schemaNode/@name}-dateWId\" />\n		<xsl:text>&#xa0;</xsl:text>\n		<img width=\"16\" height=\"16\" style=\"cursor:pointer;\" border=\"0\" src=\"/jsf-resource/inputDate/images/calendar.gif\">\n			<xsl:attribute name=\"alt\">\n				<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'date_pick_alt\')\" />\n			</xsl:attribute>\n			<xsl:attribute name=\"onclick\"><xsl:value-of select=\"sakaifn:getDateWidget(concat($fieldIdclone, $num),\n			   concat($schemaNode/@name, $num))\" /></xsl:attribute>\n		</img>\n		<xsl:text>&#xa0;&#xa0;</xsl:text>\n	</xsl:template>\n	<xsl:template name=\"month-option\">\n		<xsl:param name=\"selectedMonth\" />\n		<xsl:param name=\"month\" />\n		<xsl:param name=\"monthName\" />\n		<option value=\"{$month}\">\n			<xsl:if test=\"$month = $selectedMonth\">\n				<xsl:attribute name=\"selected\">true</xsl:attribute>\n			</xsl:if>\n			<xsl:value-of select=\"sakaifn:getMessage(\'messages\', $monthName)\" />\n		</option>\n	</xsl:template>\n	<xsl:template name=\"produce-label\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"nodeType\" />\n		<xsl:param name=\"sep\" />\n		<xsl:param name=\"num\" />\n		<xsl:param name=\"fieldName\" />\n		<label for=\"{@name}\">\n			<xsl:if test=\"$fieldName\">\n            <xsl:attribute name=\"for\"><xsl:value-of select=\"$fieldName\"/></xsl:attribute>\n			</xsl:if>\n			<xsl:if test=\"$nodeType=\'longtext\'\">\n				<xsl:attribute name=\"class\">block</xsl:attribute>\n			</xsl:if>\n			<!--output the ospi.descripion as a title in a link (using nicetitle)  -->\n			<xsl:choose>\n				<xsl:when test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.description\']/text()\">\n					<a class=\"nt\">\n						<xsl:attribute name=\"title\">\n							<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.description\']\" />\n						</xsl:attribute>\n						<xsl:choose>\n							<xsl:when test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'sakai.label\']\">\n								<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'sakai.label\']\" />\n							</xsl:when>\n							<xsl:when test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.label\']\">\n								<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.label\']\" />\n							</xsl:when>\n							<xsl:otherwise>\n								<xsl:for-each select=\"$currentSchemaNode\">\n									<xsl:value-of select=\"@name\" />\n								</xsl:for-each>\n							</xsl:otherwise>\n						</xsl:choose>\n					</a>\n				</xsl:when>\n				<xsl:otherwise>\n					<xsl:choose>\n						<xsl:when test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'sakai.label\']\">\n							<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'sakai.label\']\" />\n						</xsl:when>\n						<xsl:when test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.label\']\">\n							<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.label\']\" />\n						</xsl:when>\n						<xsl:otherwise>\n							<xsl:for-each select=\"$currentSchemaNode\">\n								<!-- todo: this is sort of a radical fallback -->\n								<xsl:value-of select=\"@name\" />\n							</xsl:for-each>\n						</xsl:otherwise>\n					</xsl:choose>\n				</xsl:otherwise>\n			</xsl:choose>\n		</label>\n	</xsl:template>\n	<xsl:template name=\"produce-label-edit\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"nodeType\" />\n		<xsl:param name=\"sep\" />\n		<xsl:param name=\"num\" />\n		<label for=\"{$currentSchemaNode/@name}{$sep}{$num}\">\n			<xsl:if test=\"$nodeType=\'longtext\'\">\n				<xsl:attribute name=\"class\">block</xsl:attribute>\n			</xsl:if>\n			<!--output the ospi.descripion as a title in a link (using nicetitle)  -->\n			<xsl:choose>\n				<xsl:when test=\"($currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.description\']/text() and $num=\'1\')\">\n					<a class=\"nt\">\n						<xsl:attribute name=\"title\">\n							<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.description\']\" />\n						</xsl:attribute>\n						<xsl:choose>\n							<xsl:when test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'sakai.label\']\">\n								<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'sakai.label\']\" />\n							</xsl:when>\n							<xsl:when test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.label\']\">\n								<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.label\']\" />\n							</xsl:when>\n							<xsl:otherwise>\n								<xsl:for-each select=\"$currentSchemaNode\">\n									<xsl:value-of select=\"@name\" />\n								</xsl:for-each>\n							</xsl:otherwise>\n						</xsl:choose>\n					</a>\n				</xsl:when>\n				<xsl:otherwise>\n					<xsl:choose>\n						<xsl:when test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'sakai.label\']\">\n							<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'sakai.label\']\" />\n						</xsl:when>\n						<xsl:when test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.label\']\">\n							<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.label\']\" />\n						</xsl:when>\n						<xsl:otherwise>\n							<xsl:for-each select=\"$currentSchemaNode\">\n								<xsl:value-of select=\"@name\" />\n							</xsl:for-each>\n						</xsl:otherwise>\n					</xsl:choose>\n				</xsl:otherwise>\n			</xsl:choose>\n		</label>\n	</xsl:template>\n	<!--output the documentation/@source=ospi.inlinedescripion as text block *above* the element -->\n	<xsl:template name=\"produce-inline\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:if test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.inlinedescription\']/text()\">\n			<p class=\"instruction clear\">\n				<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.inlinedescription\']\" />\n			</p>\n		</xsl:if>\n	</xsl:template>\n	<xsl:template name=\"produce-metadata\">\n		<xsl:if test=\"/formView/formData/artifact/metaData/repositoryNode/created\">\n			<table class=\"itemSummary\">\n				<tr>\n					<th> Created </th>\n					<td>\n						<xsl:value-of select=\"/formView/formData/artifact/metaData/repositoryNode/created\" />\n					</td>\n				</tr>\n				<xsl:if test=\"/formView/formData/artifact/metaData/repositoryNode/modified\">\n					<tr>\n						<th> Modified </th>\n						<td>\n							<xsl:value-of select=\"/formView/formData/artifact/metaData/repositoryNode/modified\" />\n						</td>\n					</tr>\n				</xsl:if>\n			</table>\n		</xsl:if>\n	</xsl:template>\n	<xsl:template name=\"produce-fields\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentNode\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:for-each select=\"$currentSchemaNode/children\">\n			<xsl:apply-templates select=\"@*|node()\">\n				<xsl:with-param name=\"currentParent\" select=\"$currentNode\" />\n				<xsl:with-param name=\"rootNode\" select=\"\'false\'\" />\n			</xsl:apply-templates>\n		</xsl:for-each>\n		<xsl:if test=\"$rootNode=\'true\' and /formView/formData/artifact/metaData\">\n			<xsl:call-template name=\"produce-metadata\" />\n		</xsl:if>\n	</xsl:template>\n	<!-- for each subform filled out render a row in the table opened in complexElementField -->\n	<xsl:template name=\"subListRow\">\n		<xsl:param name=\"index\" />\n		<xsl:param name=\"fieldName\" />\n		<xsl:param name=\"dataNode\" />\n		<xsl:param name=\"currentSchemaNode\" />\n		<tr>\n         <xsl:choose>\n         <xsl:when test=\"($currentSchemaNode/children/element/xs:annotation/xs:documentation[@source=\'ospi.key\']/text())\">\n            <xsl:for-each select=\"$currentSchemaNode/children/element[xs:annotation/xs:documentation[@source=\'ospi.key\']/text()]\">\n               <xsl:sort select=\"xs:annotation/xs:documentation[@source=\'ospi.key\']/text()\" data-type=\"number\"/>\n               <xsl:variable name=\"keyField\" select=\"@name\"/>\n               <td>\n                  <xsl:value-of select=\"$dataNode/node()[$keyField=name()]\"/>\n               </td>\n            </xsl:for-each>\n         </xsl:when>               \n         <xsl:otherwise>\n            <td>\n               <xsl:choose>\n               <xsl:when test=\"($currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.key\']/text())\">\n                  <xsl:variable name=\"keyField\" \n                                select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.key\']/text()\"/>\n                  <xsl:value-of select=\"$dataNode/node()[$keyField=name()]\"/>\n               </xsl:when>               \n               <xsl:otherwise>\n                  <xsl:value-of select=\"$dataNode/*[1]\" />\n               </xsl:otherwise>\n               </xsl:choose>\n            </td>\n         </xsl:otherwise>\n         </xsl:choose>\n			<td class=\"itemAction\">\n				<a>\n					<xsl:attribute name=\"href\">javascript:document.forms[0].childPath.value=\'<xsl:value-of select=\"$fieldName\" />\';document.forms[0].editButton.value=\'Edit\';document.forms[0].removeButton.value=\'\';document.forms[0].childIndex.value=\'<xsl:value-of select=\"$index\" />\';document.forms[0].onsubmit();document.forms[0].submit();</xsl:attribute> edit </a> | <a>\n					<xsl:attribute name=\"href\">javascript:document.forms[0].childPath.value=\'<xsl:value-of select=\"$fieldName\" />\';document.forms[0].removeButton.value=\'Remove\';document.forms[0].editButton.value=\'\';document.forms[0].childIndex.value=\'<xsl:value-of select=\"$index\" />\';document.forms[0].onsubmit();document.forms[0].submit();</xsl:attribute> remove </a>\n			</td>\n		</tr>\n	</xsl:template>\n	<!-- a crutch - since the date in datefields comes in on edit in a specific format, massage it here to render in any format. This template called with a \"format\" parameter which is not used but could be -->\n	<xsl:template name=\"dateformat\">\n		<xsl:param name=\"date\" />\n		<xsl:param name=\"format\" />\n		<xsl:variable name=\"year\" select=\"substring-before($date,\'-\')\" />\n		<xsl:variable name=\"rest\" select=\"substring-after($date,\'-\')\" />\n		<xsl:variable name=\"month\" select=\"substring-before($rest,\'-\')\" />\n		<xsl:variable name=\"day\" select=\"substring-after($rest,\'-\')\" />\n		<xsl:value-of select=\"$month\" />/<xsl:value-of select=\"$day\" />/<xsl:value-of select=\"$year\" />\n	</xsl:template>\n	<xsl:template name=\"fieldClass\">\n		<xsl:param name=\"schemaNode\" />\n		<xsl:param name=\"baseType\" />\n		<xsl:variable name=\"name\" select=\"$schemaNode/@name\" />\n      <xsl:value-of select=\"$baseType\"/> <xsl:if\n         test=\"$schemaNode/@minOccurs = \'1\'\"> required </xsl:if> <xsl:if\n         test=\"//formView/errors/error[@field=$name]\"> validFail </xsl:if>\n	</xsl:template>\n	<!-- all UI restriction driven hints here -->\n	<xsl:template name=\"calculateRestrictions\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:attribute name=\"size\">20</xsl:attribute>\n		<xsl:choose>\n			<!-- textarea restrictions and UI hints -->\n			<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value and $currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value &gt; 99\">\n				<xsl:attribute name=\"title\">\n					<xsl:value-of select=\"sakaifn:formatMessage(\'messages\',\'max_chars\',string($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value))\" />\n				</xsl:attribute>\n				<xsl:choose>\n					<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value &lt; 600\">\n						<xsl:attribute name=\"rows\">3</xsl:attribute>\n						<xsl:attribute name=\"cols\">45</xsl:attribute>\n					</xsl:when>\n					<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value &lt; 800\">\n						<xsl:attribute name=\"rows\">5</xsl:attribute>\n						<xsl:attribute name=\"cols\">45</xsl:attribute>\n					</xsl:when>\n					<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value &lt; 1000\">\n						<xsl:attribute name=\"rows\">7</xsl:attribute>\n						<xsl:attribute name=\"cols\">45</xsl:attribute>\n					</xsl:when>\n					<xsl:otherwise>\n						<xsl:attribute name=\"rows\">9</xsl:attribute>\n						<xsl:attribute name=\"cols\">45</xsl:attribute>\n					</xsl:otherwise>\n				</xsl:choose>\n			</xsl:when>\n			<!-- string restrictions and UI hints -->\n			<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value\">\n				<xsl:attribute name=\"maxLength\">\n					<xsl:value-of select=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value\" />\n				</xsl:attribute>\n				<xsl:attribute name=\"title\">\n					<xsl:value-of select=\"sakaifn:formatMessage(\'messages\',\'max_chars\',string($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value))\" />\n				</xsl:attribute>\n				<xsl:attribute name=\"size\">\n					<xsl:choose>\n						<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value &gt; 25\"> 25 </xsl:when>\n						<xsl:otherwise>\n							<xsl:value-of select=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value\" />\n						</xsl:otherwise>\n					</xsl:choose>\n				</xsl:attribute>\n			</xsl:when>\n			<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:length/@value\">\n				<xsl:attribute name=\"maxLength\">\n					<xsl:value-of select=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:length/@value\" />\n				</xsl:attribute>\n				<xsl:attribute name=\"title\">\n					<xsl:value-of select=\"sakaifn:formatMessage(\'messages\',\'exactly\',string($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:length/@value))\" />\n				</xsl:attribute>\n				<xsl:attribute name=\"size\">\n					<xsl:choose>\n						<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:length/@value &gt; 25\"> 25 </xsl:when>\n						<xsl:otherwise>\n							<xsl:value-of select=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:length/@value\" />\n						</xsl:otherwise>\n					</xsl:choose>\n				</xsl:attribute>\n			</xsl:when>\n			<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:minLength/@value\">\n				<xsl:attribute name=\"title\">\n					<xsl:value-of select=\"sakaifn:formatMessage(\'messages\',\'at_least\',string($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:minLength/@value))\" />\n				</xsl:attribute>\n			</xsl:when>\n			<!-- integer restrictions and UI hints -->\n			<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:totalDigits/@value\">\n				<xsl:attribute name=\"maxLength\">\n					<xsl:value-of select=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:totalDigits/@value\" />\n				</xsl:attribute>\n				<xsl:attribute name=\"title\">\n					<xsl:value-of select=\"sakaifn:formatMessage(\'messages\',\'max_digs\',string($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:totalDigits/@value))\" />\n				</xsl:attribute>\n				<xsl:attribute name=\"size\">\n					<xsl:choose>\n						<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:totalDigits/@value &gt; 20\"> 20 </xsl:when>\n						<xsl:otherwise>\n							<xsl:value-of select=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:totalDigits/@value\" />\n						</xsl:otherwise>\n					</xsl:choose>\n				</xsl:attribute>\n			</xsl:when>\n			<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:maxInclusive/@value\">\n				<xsl:attribute name=\"title\">\n					<xsl:value-of select=\"sakaifn:formatMessage(\'messages\',\'less_or_equal_to\',string($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:maxInclusive/@value))\" />\n				</xsl:attribute>\n			</xsl:when>\n			<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:minInclusive/@value\">\n				<xsl:attribute name=\"title\">\n					<xsl:value-of select=\"sakaifn:formatMessage(\'messages\',\'more_than_or_equal_to\',string($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:minInclusive/@value))\" />\n				</xsl:attribute>\n			</xsl:when>\n			<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:minExclusive/@value\">\n				<xsl:attribute name=\"title\">\n					<xsl:value-of select=\"sakaifn:formatMessage(\'messages\',\'less_than\',string($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:minExclusive/@value))\" />\n				</xsl:attribute>\n			</xsl:when>\n			<xsl:when test=\"$currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:maxExclusive/@value\">\n				<xsl:attribute name=\"title\">\n					<xsl:value-of select=\"sakaifn:formatMessage(\'messages\',\'more_than\',string($currentSchemaNode/xs:simpleType/xs:restriction[@base=\'xs:integer\']/xs:maxExclusive/@value))\" />\n				</xsl:attribute>\n			</xsl:when>\n			<xsl:otherwise>\n				<xsl:attribute name=\"size\">20</xsl:attribute>\n			</xsl:otherwise>\n		</xsl:choose>\n	</xsl:template>\n</xsl:stylesheet>\n'),('/group/PortfolioAdmin/system/formView.xslt','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<xsl:stylesheet version=\"1.0\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:sakaifn=\"org.sakaiproject.metaobj.utils.xml.XsltFunctions\" exclude-result-prefixes=\"xs sakaifn\">\n	<xsl:output method=\"xml\" omit-xml-declaration=\"yes\" encoding=\"UTF-8\" doctype-public=\"-//W3C//DTD XHTML 1.0 Transitional//EN\" />\n	<xsl:param name=\"urlDecoration\" />\n	<xsl:template match=\"formView\">\n		<!--  note: equivalent to / -->\n		<html lang=\"en\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n			<head>\n				<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n				<meta http-equiv=\"Content-Style-Type\" content=\"text/css\" />\n				<title>\n					<xsl:value-of select=\"formData/artifact/metaData/displayName\" />\n				</title>\n				<script type=\"text/javascript\" language=\"JavaScript\" src=\"/library/js/headscripts.js\">  // empty\n					block </script>\n				<script language=\"JavaScript\">\n					<![CDATA[\n						// in case the parent iframe id has whitespace, trim so that IE can use it.\n						function trim(s){\n						if((s==null)||(typeof(s)!=\'string\')||!s.length)return\'\';return s.replace(/^\\s+/,\'\').replace(/\\s+$/,\'\')}\n					]]>\n				</script>\n				<link type=\"text/css\" rel=\"stylesheet\" media=\"all\" href=\"/sakai-metaobj-tool/css/metaobj.css\" />\n				<xsl:apply-templates select=\"css\" />\n			</head>\n			<body onload=\"(window.frameElement) ? setMainFrameHeight(trim(window.frameElement.id)):\'\'\" class=\"formDisplay\">\n				<div class=\"portletBodyForm\" style=\"padding:1em 0\">\n					<xsl:apply-templates select=\"formData/artifact/schema/element\">\n						<xsl:with-param name=\"currentParent\" select=\"formData/artifact/structuredData\" />\n						<xsl:with-param name=\"rootNode\" select=\"\'true\'\" />\n					</xsl:apply-templates>\n				</div>\n			</body>\n		</html>\n	</xsl:template>\n	<xsl:template match=\"element\">\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"nodetype\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:param name=\"thisname\" />\n		<xsl:param name=\"subform\" />\n		<xsl:variable name=\"name\" select=\"@name\" />\n		<xsl:variable name=\"currentNode\" select=\"$currentParent/node()[$name=name()]\" />\n		<xsl:variable name=\"elementtype\">\n			<!-- find out element type -->\n			<xsl:choose>\n				<xsl:when test=\"xs:simpleType/xs:restriction/@base=\'xs:boolean\'\">boolean</xsl:when>\n				<xsl:when test=\"xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value &lt; 99\">shorttext</xsl:when>\n				<xsl:when test=\"xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:maxLength/@value &gt;= 99\">\n					<xsl:choose>\n						<xsl:when test=\"xs:annotation/xs:documentation[@source=\'ospi.isRichText\' or @source=\'sakai.isRichText\']=\'true\'\">richtext</xsl:when>\n						<xsl:otherwise>longtext</xsl:otherwise>\n					</xsl:choose>\n				</xsl:when>\n				<xsl:when test=\"xs:simpleType/xs:restriction/@base=\'xs:date\'\">date</xsl:when>\n				<xsl:when test=\"xs:simpleType/xs:restriction/@base=\'xs:anyURI\' or @type=\'xs:anyURI\'\">file</xsl:when>\n				<xsl:when test=\"xs:simpleType/xs:restriction[@base=\'xs:string\']/xs:enumeration\">select</xsl:when>\n				<!-- fallback element type -->\n				<xsl:otherwise>shorttext</xsl:otherwise>\n			</xsl:choose>\n		</xsl:variable>\n		<xsl:choose>\n			<xsl:when test=\"children\">\n				<xsl:if test=\"$rootNode = \'true\'\">\n					<xsl:call-template name=\"produce-label\">\n						<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n						<xsl:with-param name=\"elementtype\" select=\"$elementtype\" />\n						<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n						<xsl:with-param name=\"itemCount\" select=\"count($currentParent/*[name()=$name])\" />\n					</xsl:call-template>\n					<!-- has children and this is a top level node, not a subform element  -->\n					<table class=\"top\">\n						<xsl:attribute name=\"summary\"><xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'table_top_summary\')\" /></xsl:attribute>\n						<xsl:call-template name=\"produce-fields\">\n							<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n							<xsl:with-param name=\"currentNode\" select=\"$currentNode\" />\n							<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n							<xsl:with-param name=\"thisname\" select=\"name()\" />\n						</xsl:call-template>\n					</table>\n				</xsl:if>\n				<xsl:if test=\"$rootNode=\'false\'\">\n					<!-- a darn subform -->\n					<tr>\n						<td colspan=\"2\" class=\"subformlabel\">\n							<xsl:call-template name=\"produce-label\">\n								<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n								<xsl:with-param name=\"itemCount\" select=\"count($currentParent/*[name()=$name])\" />\n							</xsl:call-template>\n						</td>\n					</tr>\n					<tr>\n						<td colspan=\"2\">\n							<table class=\"subformtable\">\n								<xsl:attribute name=\"summary\"><xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'table_subform_summary\')\" /></xsl:attribute>\n								<xsl:call-template name=\"elem\">\n									<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n									<xsl:with-param name=\"elem\" select=\"element\" />\n									<xsl:with-param name=\"data\" select=\"$currentNode\" />\n									<xsl:with-param name=\"thisname\" select=\"$thisname\" />\n									<xsl:with-param name=\"name\" select=\"$name\" />\n									<xsl:with-param name=\"subform\" select=\"\'true\'\" />\n								</xsl:call-template>\n							</table>\n						</td>\n					</tr>\n				</xsl:if>\n			</xsl:when>\n			<xsl:otherwise>\n				<!-- a plain old element - of which there are 2 types, big things to lay horizontally, short ones to lay vertically  -->\n				<xsl:if test=\"($thisname=\'element\' or $thisname=$name) and $subform !=\'true\'\">\n					<tr>\n						<td colspan=\"2\">\n							<table>\n								<xsl:attribute name=\"summary\"><xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'table_item_summary\')\" /></xsl:attribute>\n								<xsl:choose>\n									<xsl:when test=\"$elementtype=\'longtext\' or $elementtype=\'richtext\'\">\n										<xsl:attribute name=\"class\">h</xsl:attribute>\n										<tr>\n											<td>\n												<xsl:call-template name=\"produce-label\">\n													<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n													<xsl:with-param name=\"itemCount\" select=\"count($currentParent/*[name()=$name])\" />\n													<xsl:with-param name=\"elementtype\" select=\"$elementtype\" />\n												</xsl:call-template>\n											</td>\n										</tr>\n										<tr>\n											<td>\n												<xsl:call-template name=\"h\">\n													<xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n													<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n													<xsl:with-param name=\"currentNode\" select=\"$currentNode\" />\n													<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n													<xsl:with-param name=\"thisname\" select=\"name()\" />\n													<xsl:with-param name=\"name\" select=\"$name\" />\n													<xsl:with-param name=\"elementtype\" select=\"$elementtype\" />\n												</xsl:call-template>\n											</td>\n										</tr>\n									</xsl:when>\n									<xsl:otherwise>\n										<xsl:attribute name=\"class\">v</xsl:attribute>\n										<tr>\n											<th>\n												<xsl:call-template name=\"produce-label\">\n													<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n													<xsl:with-param name=\"elementtype\" select=\"$elementtype\" />\n													<xsl:with-param name=\"itemCount\" select=\"count($currentParent/*[name()=$name])\" />\n												</xsl:call-template>\n											</th>\n											<td>\n												<xsl:call-template name=\"v\">\n													<xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n													<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n													<xsl:with-param name=\"currentNode\" select=\"$currentNode\" />\n													<xsl:with-param name=\"rootNode\" select=\"$rootNode\" />\n													<xsl:with-param name=\"thisname\" select=\"name()\" />\n													<xsl:with-param name=\"name\" select=\"$name\" />\n													<xsl:with-param name=\"elementtype\" select=\"$elementtype\" />\n												</xsl:call-template>\n											</td>\n										</tr>\n									</xsl:otherwise>\n								</xsl:choose>\n							</table>\n						</td>\n					</tr>\n					<!--need to see if this is a subform-->\n				</xsl:if>\n				<xsl:if test=\"$subform=\'true\'\">\n					<xsl:if test=\"@name = $thisname\">\n						<xsl:choose>\n							<xsl:when test=\"$elementtype=\'longtext\' or $elementtype=\'richtext\'\">\n								<tr>\n									<th>\n										<xsl:call-template name=\"produce-label\">\n											<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n											<xsl:with-param name=\"elementtype\" select=\"$elementtype\" />\n											<xsl:with-param name=\"itemCount\" select=\"count($currentParent/*[name()=$name])\" />\n										</xsl:call-template>\n									</th>\n								</tr>\n								<tr class=\"longtextsubform\">\n									<td colspan=\"2\">\n										<xsl:call-template name=\"h\">\n											<xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n										</xsl:call-template>\n									</td>\n								</tr>\n							</xsl:when>\n							<xsl:otherwise>\n								<tr>\n									<th>\n										<xsl:call-template name=\"produce-label\">\n											<xsl:with-param name=\"elementtype\" select=\"$elementtype\" />\n											<xsl:with-param name=\"currentSchemaNode\" select=\".\" />\n											<xsl:with-param name=\"itemCount\" select=\"count($currentParent/*[name()=$name])\" />\n										</xsl:call-template>\n									</th>\n									<td>\n										<xsl:call-template name=\"v\">\n											<xsl:with-param name=\"currentParent\" select=\"$currentParent\" />\n										</xsl:call-template>\n									</td>\n								</tr>\n							</xsl:otherwise>\n						</xsl:choose>\n					</xsl:if>\n				</xsl:if>\n			</xsl:otherwise>\n		</xsl:choose>\n	</xsl:template>\n	<!-- named templates  -->\n	<xsl:template name=\"produce-label\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"elementtype\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:param name=\"itemCount\" />\n		<h4>\n			<xsl:choose>\n				<xsl:when test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'sakai.label\']\">\n					<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'sakai.label\']\" />\n				</xsl:when>\n				<xsl:when test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.label\']\">\n					<xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.label\']\" />\n				</xsl:when>\n				<xsl:otherwise>\n					<xsl:for-each select=\"$currentSchemaNode\">\n						<!-- todo: this is sort of a radical fallback -->\n						<xsl:value-of select=\"@name\" />\n					</xsl:for-each>\n				</xsl:otherwise>\n			</xsl:choose>\n		</h4>\n		<xsl:if test=\"$rootNode=\'true\'\">\n			<xsl:call-template name=\"produce-metadata\" />\n		</xsl:if>\n		<xsl:if test=\"$itemCount &gt; 1\">\n			<div class=\"textPanelFooter\">\n				<xsl:value-of select=\"$itemCount\" /><xsl:text> </xsl:text>\n				<xsl:choose>\n					<xsl:when test=\"$elementtype=\'select\'\">\n						<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'itemsselected\')\" />\n					</xsl:when>\n					<xsl:otherwise>\n						<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'items\')\" />\n					</xsl:otherwise>\n				</xsl:choose>\n			</div>\n		</xsl:if>\n		<!-- these - where to put these, do they even make sense in a form display? As a nicetitle hover? in the response but visible only on printing? \n		<xsl:if test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.inlinedescription\']/text()\">\n			<p class=\"instruction clear\"> (i) <xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.inlinedescription\']\" />\n			</p>\n		</xsl:if>\n		<xsl:if test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.description\']/text()\">\n			<p class=\"instruction clear\"> (d) <xsl:value-of select=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.description\']\" />\n			</p>\n		</xsl:if>\n		-->\n	</xsl:template>\n	<xsl:template name=\"produce-fields\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentNode\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:param name=\"thisname\" />\n		<xsl:param name=\"subform\" />\n		<xsl:for-each select=\"$currentSchemaNode/children\">\n			<xsl:apply-templates select=\"@*|node()\">\n				<xsl:with-param name=\"currentParent\" select=\"$currentNode\" />\n				<xsl:with-param name=\"rootNode\" select=\"\'false\'\" />\n				<xsl:with-param name=\"currentNode\" select=\"$currentNode\" />\n				<xsl:with-param name=\"thisname\" select=\"$thisname\" />\n				<xsl:with-param name=\"subform\" select=\"$subform\" />\n			</xsl:apply-templates>\n		</xsl:for-each>\n	</xsl:template>\n	<xsl:template name=\"produce-metadata\">\n		<div class=\"textPanelFooter\">\n			<strong>\n				<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'created\')\" />\n			</strong>\n			<xsl:text> </xsl:text>\n			<xsl:value-of select=\"/formView/formData/artifact/metaData/repositoryNode/created\" />\n			<xsl:text> /  </xsl:text>\n			<strong>\n				<xsl:value-of select=\"sakaifn:getMessage(\'messages\', \'modified\')\" />\n			</strong>\n			<xsl:text> </xsl:text>\n			<xsl:value-of select=\"/formView/formData/artifact/metaData/repositoryNode/modified\" />\n		</div>\n	</xsl:template>\n	<xsl:template name=\"elem\">\n		<xsl:param name=\"elem\" />\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"data\" />\n		<xsl:param name=\"thisname\" />\n		<xsl:param name=\"name\" />\n		<xsl:param name=\"subform\" />\n		<xsl:for-each select=\"$data/*\">\n			<xsl:call-template name=\"produce-fields\">\n				<xsl:with-param name=\"currentSchemaNode\" select=\"$currentSchemaNode\" />\n				<xsl:with-param name=\"currentNode\" select=\".\" />\n				<xsl:with-param name=\"rootNode\" select=\"false\" />\n				<xsl:with-param name=\"thisname\" select=\"name()\" />\n				<xsl:with-param name=\"subform\" select=\"$subform\" />\n			</xsl:call-template>\n		</xsl:for-each>\n	</xsl:template>\n	<!-- a template for each element type group-->\n	<xsl:template name=\"v\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"currentNode\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:param name=\"elementtype\" />\n		<xsl:param name=\"thisname\" />\n		<xsl:param name=\"name\" />\n		<xsl:choose>\n			<!-- a root node (not a node in a subform) -->\n			<xsl:when test=\"$thisname=\'element\'\">\n				<xsl:for-each select=\"$currentParent/*[name()=$name]\">\n					<xsl:choose>\n						<xsl:when test=\"$elementtype=\'shorttext\'\">\n								<xsl:choose>\n									<xsl:when test=\"$currentSchemaNode/xs:annotation/xs:documentation[@source=\'ospi.isRichText\']=\'true\'\">\n										<xsl:value-of disable-output-escaping=\"yes\" select=\".\" />\n									</xsl:when>\n									<xsl:otherwise>\n										<div class=\"textPanel\">\n										<xsl:value-of select=\".\" />\n										</div>\n									</xsl:otherwise>\n								</xsl:choose>\n						</xsl:when>\n						<xsl:when test=\"$elementtype=\'date\'\">\n							<div class=\"textPanel\">\n								<xsl:call-template name=\"dateformat\">\n									<xsl:with-param name=\"date\" select=\".\" />\n									<xsl:with-param name=\"format\">mm/dd/yy</xsl:with-param>\n								</xsl:call-template>\n							</div>\n						</xsl:when>\n						<xsl:when test=\"$elementtype=\'file\'\">\n							<div class=\"textPanel\">\n								<img src=\"/library/image/sakai/attachments.gif\" alt=\"attachment\" />\n								<xsl:text> </xsl:text>\n								<a target=\"_blank\">\n									<xsl:choose>\n										<xsl:when test=\"$urlDecoration != \'\'\">\n											<xsl:attribute name=\"href\">\n												<xsl:value-of select=\"sakaifn:getReferenceUrl(., $urlDecoration)\" />\n											</xsl:attribute>\n											<xsl:value-of select=\"sakaifn:getReferenceName(., $urlDecoration)\" />\n										</xsl:when>\n										<xsl:otherwise>\n											<xsl:attribute name=\"href\">\n												<xsl:value-of select=\"sakaifn:getReferenceUrl(.)\" />\n											</xsl:attribute>\n											<xsl:value-of select=\"sakaifn:getReferenceName(.)\" />\n										</xsl:otherwise>\n									</xsl:choose>\n								</a>\n							</div>\n						</xsl:when>\n						<xsl:when test=\"$elementtype=\'boolean\'\">\n							<xsl:if test=\".=\'true\'\">\n								<div class=\"textPanel\">\n									<img src=\"/library/image/sakai/checkon.gif\" alt=\"checked \" />\n								</div>\n							</xsl:if>\n						</xsl:when>\n						<xsl:otherwise> </xsl:otherwise>\n					</xsl:choose>\n				</xsl:for-each>\n				<!-- selects need their own iteration -->\n				<xsl:if test=\"$elementtype=\'select\'\">\n					<ul class=\"selectList\">\n						<xsl:for-each select=\"$currentSchemaNode/xs:simpleType/xs:restriction/xs:enumeration\">\n                     <li>\n                        <xsl:choose>\n                           <xsl:when test=\"@value=$currentParent/node()[$name=name()]\">\n                              <img src=\"/library/image/sakai/checkon.gif\" alt=\"checked\" />\n                           </xsl:when>\n                           <xsl:otherwise>\n                              <img src=\"/library/image/sakai/checkoff.gif\" alt=\"unchecked\" />\n                           </xsl:otherwise>\n                        </xsl:choose>\n                        <xsl:choose>\n                           <xsl:when test=\"./xs:annotation/xs:documentation[@source=\'sakai.label\']\">\n                              <xsl:value-of\n                                 select=\"./xs:annotation/xs:documentation[@source=\'sakai.label\']\" />\n                           </xsl:when>\n                           <xsl:when test=\"./xs:annotation/xs:documentation[@source=\'ospi.label\']\">\n                              <xsl:value-of select=\"./xs:annotation/xs:documentation[@source=\'ospi.label\']\" />\n                           </xsl:when>\n                           <xsl:when test=\"./xs:annotation/xs:documentation\">\n                              <xsl:value-of select=\"./xs:annotation/xs:documentation\" />\n                           </xsl:when>\n                           <xsl:otherwise>\n                              <xsl:value-of select=\"@value\" />\n                           </xsl:otherwise>\n                        </xsl:choose>. </li>\n                  </xsl:for-each>\n					</ul>\n				</xsl:if>\n			</xsl:when>\n			<xsl:otherwise>\n				<!--- a subform node -->\n				<div class=\"textPanel\">\n					<xsl:value-of select=\"$currentParent\" />\n				</div>\n			</xsl:otherwise>\n		</xsl:choose>\n	</xsl:template>\n	<xsl:template name=\"h\">\n		<xsl:param name=\"currentSchemaNode\" />\n		<xsl:param name=\"currentParent\" />\n		<xsl:param name=\"currentNode\" />\n		<xsl:param name=\"rootNode\" />\n		<xsl:param name=\"thisname\" />\n		<xsl:param name=\"name\" />\n		<xsl:choose>\n			<!-- a root node (not a node in a subform) -->\n			<xsl:when test=\"$thisname=\'element\'\">\n				<xsl:for-each select=\"$currentParent/*[name()=$name]\">\n					<div class=\"textPanel\">\n						<xsl:value-of disable-output-escaping=\"yes\" select=\".\" />\n					</div>\n				</xsl:for-each>\n			</xsl:when>\n			<xsl:otherwise>\n				<!--- a subform node -->\n				<div class=\"textPanel\">\n					<xsl:value-of disable-output-escaping=\"yes\" select=\"$currentParent\" />\n				</div>\n			</xsl:otherwise>\n		</xsl:choose>\n	</xsl:template>\n	<xsl:template name=\"dateformat\">\n		<xsl:param name=\"date\" />\n		<xsl:param name=\"format\" />\n		<xsl:variable name=\"year\" select=\"substring-before($date,\'-\')\" />\n		<xsl:variable name=\"rest\" select=\"substring-after($date,\'-\')\" />\n		<xsl:variable name=\"month\" select=\"substring-before($rest,\'-\')\" />\n		<xsl:variable name=\"day\" select=\"substring-after($rest,\'-\')\" />\n		<xsl:value-of select=\"$month\" />/<xsl:value-of select=\"$day\" />/<xsl:value-of select=\"$year\" />\n	</xsl:template>\n	<xsl:template match=\"css\">\n		<xsl:apply-templates />\n	</xsl:template>\n	<xsl:template match=\"uri\">\n		<link href=\"{.}\" type=\"text/css\" rel=\"stylesheet\" media=\"all\" />\n	</xsl:template>\n</xsl:stylesheet>\n'),('/group/PortfolioAdmin/system/freeFormRenderer.xml','<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<xsl:stylesheet version=\"2.0\"\n   xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"\n   xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"\n   xmlns:osp=\"http://www.osportfolio.org/OspML\">\n\n<xsl:output method=\"html\"/>\n   \n<xsl:variable name=\"layoutFile\" select=\"/ospiPresentation/layout/artifact/fileArtifact/uri\" />\n<xsl:variable name=\"page\" select=\"document($layoutFile)\"/>\n<xsl:variable name=\"presContent\" select=\"/\"/>\n\n<xsl:template match=\"/\">\n    <xsl:apply-templates select=\"$page/xhtml:html\"/>\n</xsl:template>\n\n<xsl:template match=\"osp:sequence\">\n   <xsl:variable name=\"currentId\" select=\"@firstChild\" />\n   <xsl:variable name=\"sequence\" select=\".\" />\n   \n   <xsl:for-each select=\"$presContent/ospiPresentation/regions/region[@id=$currentId]\">\n      <xsl:variable name=\"currentSeqNo\" select=\"@sequence\" />\n      <xsl:for-each select=\"$sequence\">\n         <xsl:copy>\n            <xsl:apply-templates select=\"@*|node()\">\n               <xsl:with-param name=\"currentSeqNo\" select=\"$currentSeqNo\" />\n            </xsl:apply-templates>\n         </xsl:copy>\n      </xsl:for-each>\n   </xsl:for-each>\n</xsl:template>\n\n<xsl:template match=\"osp:region\">\n   <xsl:param name=\"currentSeqNo\" />\n   <xsl:variable name=\"currentId\" select=\"@id\" />\n   \n   <xsl:choose>\n      <xsl:when test=\"$presContent/ospiPresentation/regions/region[@id=$currentId]/@sequence\" >\n         <xsl:apply-templates select=\"$presContent/ospiPresentation/regions/region[@id=$currentId and @sequence=$currentSeqNo]\"/>\n      </xsl:when>\n      <xsl:otherwise>\n         <xsl:apply-templates select=\"$presContent/ospiPresentation/regions/region[@id=$currentId]\"/>\n      </xsl:otherwise>\n   </xsl:choose>\n   \n</xsl:template>\n\n\n<!-- render the various region types -->\n<!-- text -->\n<xsl:template match=\"region[@type=\'text\']\">\n   <xsl:value-of select=\"value\" disable-output-escaping=\"no\" />      \n</xsl:template>\n<!-- text -->\n\n<!-- rich text -->\n<xsl:template match=\"region[@type=\'richtext\']\">\n   <xsl:value-of select=\"value\" disable-output-escaping=\"yes\" />     \n</xsl:template>\n<!-- rich text -->\n\n<!-- link -->\n<xsl:template match=\"region[@type=\'link\']\">\n   <a>\n      <xsl:attribute name=\"href\">\n         <xsl:value-of select=\"artifact/fileArtifact/uri\" />\n      </xsl:attribute>\n      <xsl:attribute name=\"target\">\n         <xsl:value-of select=\"itemProperties/linkTarget\" />\n      </xsl:attribute>\n      <xsl:value-of select=\"artifact/metaData/displayName\" />     \n   </a>  \n</xsl:template>\n<!-- link -->\n\n<!-- form -->\n<xsl:template match=\"region[@type=\'form\']\">\n   <xsl:text>Form data here</xsl:text>\n</xsl:template>\n<!-- form -->\n\n<!-- inline file -->\n<xsl:template match=\"region[@type=\'inline\']\">\n   <xsl:choose>\n      <xsl:when test=\"artifact/metaData/repositoryNode/mimeType/primary = \'image\'\">\n         <xsl:call-template name=\"inline-image\" />\n      </xsl:when>    \n      <xsl:otherwise>\n         <xsl:call-template name=\"inline-error\" />\n      </xsl:otherwise>\n   </xsl:choose>\n</xsl:template>\n\n   <!-- various inline file type templates -->\n   <!-- image -->\n   <xsl:template name=\"inline-image\">\n      <img>\n         <xsl:attribute name=\"src\">\n            <xsl:value-of select=\"artifact/fileArtifact/uri\" />      \n         </xsl:attribute>\n         <xsl:attribute name=\"alt\">\n            <xsl:value-of select=\"artifact/metaData/displayName\" />     \n         </xsl:attribute>\n         <xsl:attribute name=\"height\">\n            <xsl:value-of select=\"itemProperties/itemHeight\" />\n         </xsl:attribute>\n         <xsl:attribute name=\"width\">\n            <xsl:value-of select=\"itemProperties/itemWidth\" />\n         </xsl:attribute>\n      </img>      \n   </xsl:template>\n   \n   <!-- error -->\n   <xsl:template name=\"inline-error\">\n      <xsl:text>Unable to display file: </xsl:text>\n      <a>\n         <xsl:attribute name=\"href\">\n            <xsl:value-of select=\"artifact/fileArtifact/uri\" />\n         </xsl:attribute>\n         <xsl:value-of select=\"artifact/metaData/displayName\" />     \n      </a>\n      <xsl:text> inline</xsl:text>\n   </xsl:template>\n   <!-- inline file -->\n\n   <xsl:template match=\"region\">\n      <xsl:text>Unknown region type</xsl:text>\n   </xsl:template>\n\n   <xsl:template name=\"apply-navigation\">\n      <xsl:if test=\"$presContent/ospiPresentation/navigation/nextPage |\n                    $presContent/ospiPresentation/navigation/previousPage\">\n<div class=\"navIntraTool\">\n   <xsl:if test=\"$presContent/ospiPresentation/navigation/previousPage\">\n<a title=\"Previous\">\n<xsl:attribute name=\"href\">\n   <xsl:value-of\n      select=\"$presContent/ospiPresentation/navigation/previousPage/artifact/fileArtifact/uri\"/>\n</xsl:attribute>\n<xsl:value-of\n   select=\"$presContent/ospiPresentation/navigation/previousPage/artifact/metaData/displayName\"/>\n</a>\n   </xsl:if>\n   <xsl:text>   </xsl:text>\n   <xsl:if test=\"$presContent/ospiPresentation/navigation/nextPage\">\n<a title=\"Next\">\n<xsl:attribute name=\"href\">\n   <xsl:value-of\n      select=\"$presContent/ospiPresentation/navigation/nextPage/artifact/fileArtifact/uri\"/>\n</xsl:attribute>\n<xsl:value-of\n   select=\"$presContent/ospiPresentation/navigation/nextPage/artifact/metaData/displayName\"/>\n</a>\n   </xsl:if>\n</div>\n      </xsl:if>\n   </xsl:template>\n   \n\n   <xsl:template name=\"apply-pageStyle\">\n      <xsl:if test=\"$presContent//ospiPresentation/pageStyle\">\n         <link rel=\"stylesheet\" type=\"text/css\" media=\"all\">\n            <xsl:attribute name=\"href\">\n               <xsl:value-of select=\"$presContent/ospiPresentation/pageStyle/artifact/fileArtifact/uri\"/>\n            </xsl:attribute>\n         </link>\n      </xsl:if>\n   </xsl:template>   \n   \n\n   <!-- head tag -->\n   <xsl:template match=\"xhtml:head\">\n      <xsl:copy>\n         <xsl:call-template name=\"apply-pageStyle\" />\n      </xsl:copy>\n   </xsl:template>   \n   \n   <!-- body tag -->\n   <xsl:template match=\"xhtml:body\">\n      <xsl:param name=\"currentSeqNo\" />\n      <xsl:copy>\n         <xsl:call-template name=\"apply-navigation\"/>\n         <xsl:apply-templates select=\"@*|node()\" >\n            <xsl:with-param name=\"currentSeqNo\" select=\"$currentSeqNo\" />\n         </xsl:apply-templates>\n      </xsl:copy>\n   </xsl:template>\n\n   <!-- Identity transformation -->\n   <xsl:template match=\"@*|*\">\n      <xsl:param name=\"currentSeqNo\" />\n      <xsl:copy>\n         <xsl:apply-templates select=\"@*|node()\" >\n            <xsl:with-param name=\"currentSeqNo\" select=\"$currentSeqNo\" />\n         </xsl:apply-templates>\n      </xsl:copy>\n   </xsl:template>\n\n</xsl:stylesheet>\n\n'),('/group/PortfolioAdmin/system/contentOverText.jpg','ˇÿˇ‡\0JFIF\0\0`\0`\0\0ˇ€\0C\0		\n\r\Z\Z $.\' \",#(7),01444\'9=82<.342ˇ€\0C			\r\r2!!22222222222222222222222222222222222222222222222222ˇ¿\0\0C\0T\"\0ˇƒ\0\0\0\0\0\0\0\0\0\0\0	\nˇƒ\0µ\0\0\0}\0!1AQa\"q2Åë°#B±¡R—$3brÇ	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyzÉÑÖÜáàâäíìîïñóòôö¢£§•¶ß®©™≤≥¥µ∂∑∏π∫¬√ƒ≈∆«»… “”‘’÷◊ÿŸ⁄·‚„‰ÂÊÁËÈÍÒÚÛÙıˆ˜¯˘˙ˇƒ\0\0\0\0\0\0\0\0	\nˇƒ\0µ\0\0w\0!1AQaq\"2ÅBë°±¡	#3Rbr—\n$4·%Ò\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyzÇÉÑÖÜáàâäíìîïñóòôö¢£§•¶ß®©™≤≥¥µ∂∑∏π∫¬√ƒ≈∆«»… “”‘’÷◊ÿŸ⁄‚„‰ÂÊÁËÈÍÚÛÙıˆ˜¯˘˙ˇ⁄\0\0\0?\0ˆ]Uº”_LÜ¬Œ´õ˚≥lã=√@ãà§îí ézEåmÔYrxõ[äFéK_£©!ïº@‡Ç=GŸ™Ôà‰7·O˚\n…ˇ\0§W5ÃÍ6ﬁ}RÌÓ¸[e\rÀLÊhõRÅJ>„∏yåPø¸%:«¸Ø˛/ˇ\0»‘¬S¨œ\nˇ\0·Bˇ\0¸ç\\ˇ\0Ÿ>vÒïëˇ\0∏ùΩd¯såˇ\0¬eeˇ\0É;z\0Ë?·)÷?ÁáÖ°˛F£˛ùc˛xxWˇ\0\nˇ\0‰j√ÉL¯}uqΩøã≠fûV	qÍP3;Ä\0$í@¨j>Vëp∂˙üà“∆vP‚+´ÿbbß<·Ä8‡ÛÏhS˛ùc˛xxWˇ\0\nˇ\0‰j?·)÷?ÁáÖ°˛FÆÏü?Ës±ˇ\0¡•Ωd¯qˇ\0Cùè˛\r-Ë†ˇ\0ÑßXˇ\0ûˇ\0¬Öˇ\0˘\ZÆi⁄Ê´6Ωkßj:~üwVí›C=û†˜ÑmêCDòœú ûï…õ?á?·3≤ˇ\0¡ùΩtñˇ\0f-ÿ≥òOj4K¡ ¡ÉßôgµÅF9÷QEœxá˛C~ˇ\0∞¨ü˙Eu\\Œès\n-‰w7¢◊EØ⁄\03®]·Hœ\0O@;◊M‚.uø\nÿVO˝\"∫ÆYµ)Ïº7$1]5∫À®jŒ][kñ[…0Ì–u¿Ìê@7m.m€Y¥K[ˇ\077$2.£$˘_%Œ6íA\0Á‘b¨x™}F”æÀ1ÇŒIY.§ånîßÀ\nùX‡ÅŒH„#H◊f‘¸Kldº…ﬁWÀå≤∆P¬O‹œ\'wÆñ/kS≈ykw&©myg¥ÖcíΩB·	;‘ …€‘Á\0ë«$\rByÆºßI;»ŒuK∫Lo _ƒ`:ùG~iö§…ènå∑ﬁ≥±1∏0Ç3|@.#êægŒ´k£Cilóçk.ß¶›	Ó◊&R˜V§m*dÇ=py˘±´©^ΩáäuÎò§Tu“Ù’ÛŒ–◊JXP	Î˘\Z\0ÜÚÓÕ4◊hµ@\'nÍ?µ‰fﬁ\0«∫Á<w‰WG‚9u|=}&íØR\"bﬁ·Gæ|π∆qû3◊ä‡ÊÒÀŸ˝îÍÖD~yçíVÏ‹·æaÉ«o©Œ•+‹^\r6KI˛«\")k∏Ÿ≤éXÌ\0=\nÇI8Á®»O\\œ<˜±Ω’ÕÃ(#d{ÑŸªp<™üò)¿‰ıÁ¨O¸«¿$ûOÖ•?è˙?@6±j_iÉS∏7m›Ó¥q|—ªdF02ûÙÕ3¬¬ˇ\0b¥£ˇ\0H®–(¢ä\0ÁºCˇ\0!ø\nÿVO˝\"πÆj;{+©ÆÂ”º7‚Ÿ 7∑*œi≠y4¢gïOµ¶–d~ËÎ“∫_s≠¯S˛¬≤È’\'Éá¸I.?Ï+©Èl‘Å˝ùˇ\0Røé?£ˇ\0ÓÍAß‰Á˛~\"ˇ\0ÓÍË4ò4ÙÒwà•∑“/mØd˚7⁄ÔfFﬁb2 %à; ÿìŒz◊>,¸<sè	ÎsÁßõü7?ﬁﬁ„ÔÔﬂè¯˘õ~3u¥\0\Zlfhdx¬o*TôV}u%MË¡îïk“ß‰uf˛‘µ}?ÉºTó-¬“ZÍ–€nE, Ev†‡ªc#<öØˆ?1ˇ\0ëG\\úûûdR3?ﬁﬁˇ\0≈∏g~3ˆô∑„7[O±¯yè¸ä:‰‰ÙÛ\"ê˘ô˛ˆ˜˛-√;Òü¥Õø∫⁄\0gc˛„˛∆/˛Ó§˛Œˇ\0©_∆ˇ\0¯Q˜m/ÿ¸<«˛ErrzyëH|Ãˇ\0{{ˇ\0·ù¯œ⁄fﬂå›m±am†Æ•jÒxs\\ÜQ*î∫í9æSëábX∑;îíF“f‹2nvÄW˛Œıøé?¢ˇ\0ÓÍπ£Ωå∫◊ÉÂ”aí=¸=r÷±»r…k=äNO :ûùk¥##≠yˇ\0Ñ>#øÖeˇ\0€*\0Ù\Z(¢Ä9Ôë˝∑·Cˇ\0QY?ÙäÊ≥4z€G≤ª≤Ω≥÷e‘Ø‰˝ﬁèu*ï{©]Hdå©XzËımœZäΩˇ\0£ÀÁD÷˜2@Ë˚Yr\Z6V˚Æ√Øzœˇ\0Ñ3L„˝+[ˇ\0¡ÂÔˇ\0†Î?¨\Z∆£usu≠‹Y\\ye¥ˇ\0ÑfÌ>Àµpˇ\08ã/∏ÛœNÇ®jı˛ˆªÆÆz˘~∫œ]πà„üŒ6√ù€dÛ∫¯C4ø˘˙◊?{{ˇ\0«hˇ\0Ñ3Kˇ\0ü≠sˇ\0∑ø¸vÄ9ˇ\0Ì´÷˚˙Ó∏3˜ºø	›zÌÃG‹¯Œq∂Ó€\'úm^∑ﬂ◊u¡üΩÂ¯NËc◊nb8∆Á∆sç∞ÁvŸ<ÓÉ˛Õ/˛~µœ¸ﬁˇ\0Ò⁄?·“ˇ\0ÁÎ\\ˇ\0¡ÌÔˇ\0†˚jıæ˛ªÆ˝Ô/¬wCªs∆7>3úmá;∂…ÁIoØ]«q≥j˙Ã»Æ¨Ò¬\'v™√9*?wê>gúç∞‰∂Ÿ<Ìœ¯C4ø˘˙◊?{{ˇ\0«hˇ\0Ñ3Kˇ\0ü≠sˇ\0∑ø¸vÄ˛-/˛}uœ¸^ˇ\0Ò™¬¥Z‹x\Zﬁ‚!ûMë»•Y AêA‚∑?·“ˇ\0ÁÎ\\ˇ\0¡ÌÔˇ\0´\ZÜ4›7Q[¯MÙó+BØw®\\\\ÌF*Ã\0ëÿî^ü›†\rö(¢Ä\n(¢Ä\n(¢Ä\n(¢Ä\n(¢Ä\n(¢Ä\n(¢Ä?ˇŸ'),('/group/PortfolioAdmin/system/contentOverText.xml','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\"\n      xmlns:osp=\"http://www.osportfolio.org/OspML\"\n      xml:lang=\"en\" lang=\"en\">\n   <head>\n      <title>OSP Presentation</title>\n   </head>\n   <body>\n      <table border=\"1\">\n         <tr>\n            <td colspan=\"2\" align=\"center\">\n               <osp:region id=\"region_1\">\n                  <osp:texttype helpText=\"Type your rich text title here\" isRichText=\"true\" />\n               </osp:region>\n            </td>\n         </tr>\n         <tr>\n            <td>\n               <osp:region id=\"region_2\">\n                  <osp:texttype helpText=\"Type your rich text content here\" isRichText=\"true\" />\n               </osp:region>\n            </td>\n            <td>\n               <osp:region id=\"region_3\">\n                  <osp:texttype helpText=\"Type your rich text content here\" isRichText=\"true\" />\n               </osp:region>\n            </td>\n         </tr>\n         <tr>\n            <td colspan=\"2\">\n               <ul>\n                  <osp:sequence maxOccurs=\"0\" firstChild=\"region_4\">\n                     <li>\n                        <osp:region id=\"region_4\">\n                           <osp:texttype minOccurs=\"0\" helpText=\"type new item here\" />\n                        </osp:region>\n                     </li>\n                  </osp:sequence>\n               </ul>\n            </td>\n         </tr>\n         <tr>\n            <td colspan=\"2\" align=\"center\">\n               <osp:region id=\"region_5\">\n                  <osp:texttype helpText=\"footer line\" />\n               </osp:region>\n            </td>\n         </tr>\n      </table>\n   </body>\n</html>\n'),('/group/PortfolioAdmin/system/2column.jpg','ˇÿˇ‡\0JFIF\0\0`\0`\0\0ˇ€\0C\0		\n\r\Z\Z $.\' \",#(7),01444\'9=82<.342ˇ€\0C			\r\r2!!22222222222222222222222222222222222222222222222222ˇ¿\0\0C\0S\"\0ˇƒ\0\0\0\0\0\0\0\0\0\0\0	\nˇƒ\0µ\0\0\0}\0!1AQa\"q2Åë°#B±¡R—$3brÇ	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyzÉÑÖÜáàâäíìîïñóòôö¢£§•¶ß®©™≤≥¥µ∂∑∏π∫¬√ƒ≈∆«»… “”‘’÷◊ÿŸ⁄·‚„‰ÂÊÁËÈÍÒÚÛÙıˆ˜¯˘˙ˇƒ\0\0\0\0\0\0\0\0	\nˇƒ\0µ\0\0w\0!1AQaq\"2ÅBë°±¡	#3Rbr—\n$4·%Ò\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyzÇÉÑÖÜáàâäíìîïñóòôö¢£§•¶ß®©™≤≥¥µ∂∑∏π∫¬√ƒ≈∆«»… “”‘’÷◊ÿŸ⁄‚„‰ÂÊÁËÈÍÚÛÙıˆ˜¯˘˙ˇ⁄\0\0\0?\0ˆ≠Sƒ\Z~èsomuˆ∂û·]„é÷ kÜ*õCD¨@◊Æ:’O¯Ltø˘ı◊?E{ˇ\0∆©/‰°hˇ\0ˆ\nøˇ\0—∂ïÄ</‚¸qØ‰‰ø¸M\0tòÈÛÎÆ‡ä˜ˇ\0çQˇ\0	éóˇ\0>∫Á˛Ø¯’sˇ\0ã¯ø˛Éﬂ˘77ˇ\0G¸\"˛/ˇ\0†˜˛MÕˇ\0ƒ–Aˇ\0	éóˇ\0>∫Á˛Ø¯’òÈÛÎÆ‡ä˜ˇ\0çW?ˇ\0øãˇ\0Ë=ˇ\0ìíˇ\0Ò4¬/‚Ûˇ\01Ô¸õõˇ\0â†É˛/˛}uœ¸^ˇ\0Ò™?·1“ˇ\0Á◊\\ˇ\0¡Ôˇ\0\ZÆ˛ˇ\0–{ˇ\0&Êˇ\0‚hˇ\0Ñ_≈ˇ\0Ùˇ\0…πø¯ö\0ﬂˇ\0Ñ«Kˇ\0ü]sˇ\0Wø¸j≠Èû!”µ{õã{_µ¨ˆÍè$w6S[∞W,Å*© înôÈ\\ß¸\"˛.ùtÔw.?UÆÇœ˛J±◊˛AVÎ≠›\0t#•Äœ^¯∏ZG¯ï_ˇ\0Ë€JƒoÉ|+u‡o‹‹¯kFöyt€i$ñ[ã;‘ñ$Ø$ìö‹ºˇ\0íÖ£ˇ\0ÿ&ˇ\0ˇ\0F⁄VwÑ?µ«Ö<ˆ/∞ˇ\0f*Ì;ù˛°<ø+/ﬁŒÌ›∫Pá¸ ﬁˇ\0°SCˇ\0¡t_¸M\'¸ ﬁˇ\0°SCˇ\0¡t?¸MXΩˇ\0Ñå›∏∞˛ Kn4˛c?Añ `ubvÁ˛YcwÔssˇ\0	i…Œá{ü5ˆgÚ›∑w∂Ô+¯|Ô‹Ädxì¬~”4˚;Õ?√˙U•ÃzÆü≤h,£ç◊7pÉÜë¡#Ëi|5·?\rÍz}Â›ˇ\0áÙ´ª©5]C|◊Q»Ôã…ÄÀI‡¯Vøå‰mˇ\0a];ˇ\0Ka¨˝˚c˚?Ïè∞ˇ\0»¡}ˆø∂oˇ\0è∂œøÀ€ˇ\0-:c<uÕ\0h¬\r‡ˇ\0˙4?¸Cˇ\0ƒ“¬\r‡ˇ\0˙4?¸Eˇ\0ƒ’ΩEµ—rüŸâßB~“Úf…8˘FaBÁüıõ±˚ΩíS«ãì˛$oå<‘›èœnv˚ÌÛøã…˝/å¸·k_xÇÊ€√z<3E¶‹IëÿD¨å#b!r\"∑,∆> Îˇ\0âUá˛çª®¸d%¸@.$éIÜèsÊ<hQYºñ…\nI €\'¶§¥ˇ\0íÖ¨ˇ\0ÿ*√ˇ\0G]–CEP?yˇ\0%Gˇ\0∞Mˇ\0˛ç¥Æ{√pÈÚiü%π“/Æoc“áŸ/°F0ŸÊŸ˘§0x¬ÆA‰qäËo?‰°hˇ\0ˆ	øˇ\0—∂ïù·-B\nx&Ê€S˚=î\ZT?k¥˚:∑⁄∑@Å>s m9n:ÁÄZ∂—ß’ßküÍ∫ç¡\n≠$q9çŒ–\rÃoÃ®G‹ƒÚÁÂ7h?7¬Z‰≈∏dR3?ﬁﬁ¸Óﬁ7oÎˆô∑„7[zãÌ7YπºíKmwÏê6≈¢1QåπâÀrÁ$c\".0Æ%Ø˝ç‚˜ºO∑?{À±Aåı€∏úcs„9∆ÿsªlûp„\0ÖlOÌ];ˇ\0KaÆ~ﬁ\r>mN˛–“/µ-û*∫{≤#7Ÿ¶∑fìkFº‰úéG∫»€˛¬∫o˛ñ√Y˙ñ£y¢G˝ü™\'ƒ“‹f›eÛ‚≥ÓãÊ˚ª∏˘á#7â “§‘ckÌ\'TΩïb_ﬁ⁄$¨±.I„kõÂi>\\ù–ƒ~¯Äè±¯yÊR÷‚Ÿ¡Ú‚ì˜x˛Ó«„n”∑gO≥C≥8µ›÷ÍV:µ‘Íˆ:ºvq™Âµ†î3dú±$d ¿« d‹»—“˛∆Ò\në∑ƒ˚∂˝ﬂ6¬3út›Ç3ù©úc;¶∆›—˘ x≠#ã·^πVücçtI¬€UÚGêÿL)*1”\nH„é*≈ß¸î-g˛¡V˙:Ó£ÒúM√OƒÛ<Óö= ¥≤‰BŸc¥ì◊Ä†%ß¸î-g˛¡V˙:ÓÄ:\Z(¢Ä9Î”ˇ\0GÔˇ\0´˛?Ì≠•/Å?‰û¯k˛¡Vø˙)i˙∆ç®›Î6:¶ô®Z⁄œko=π[õFù]dhõ#làA!Î÷πœ¯WÙ„‡¸%ø˚¢Ä=4fºˇ\0˛œ˝8¯ˇ\0	o˛Ë£˛œ˝8¯ˇ\0	o˛Ë†É∆_Ú∂ˇ\0∞Æùˇ\0•∞—‡”ˇ\0;ü˚\nÍ_˙[5a¡˛[[àÓ-ÌºÒ8x‰è√%YÇ∏» Ûö\'◊W\\\\€¯.iÂrÚK\'Ü3±9$ìqíIÔ@ÊhÕyˇ\0¸+ü˙q?˛ﬂ˝—G¸+ü˙q?˛ﬂ˝—@˛;#˛ÔâÏuˇ\0¢öãO˘(Zœ˝Ç¨?Ùm›`¬π Òe‡ÅÙøˇ\0tWE§h⁄ï¶±{©ÍZç•‘◊[™⁄Ÿ¥\nãJ√;§|íe>ù\0ov¢ê\0(†¿£ä(\0¿£ä(\0¿£ä(\0¿£ä(\0¿£ä(\0¢ä(ˇŸ'),('/group/PortfolioAdmin/system/twoColumn.xml','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\"\n      xmlns:osp=\"http://www.osportfolio.org/OspML\"\n      xml:lang=\"en\" lang=\"en\">\n   <head>\n      <title>OSP Presentation</title>\n   </head>\n   <body>\n      <table border=\"1\">\n         <tr>\n            <td colspan=\"2\" align=\"center\">\n               <osp:region id=\"region_1\">\n                  <osp:texttype helpText=\"Type your rich text title here\" isRichText=\"true\" />\n               </osp:region>\n            </td>\n         </tr>\n         <tr>\n            <td>\n               <ul>\n                  <osp:sequence maxOccurs=\"0\" firstChild=\"region_2\">\n                     <li>\n                        <osp:region id=\"region_2\">\n                           <osp:texttype minOccurs=\"0\" helpText=\"type new item here\" />\n                        </osp:region>\n                     </li>\n                  </osp:sequence>\n               </ul>\n            </td>\n            <td>\n               <ul>\n                  <osp:sequence maxOccurs=\"0\" firstChild=\"region_3\">\n                     <li>\n                        <osp:region id=\"region_3\">\n                           <osp:texttype minOccurs=\"0\" helpText=\"type new item here\" />\n                        </osp:region>\n                     </li>\n                  </osp:sequence>\n               </ul>\n            </td>\n         </tr>\n         <tr>\n            <td colspan=\"2\" align=\"center\">\n               <osp:region id=\"region_4\">\n                  <osp:texttype helpText=\"footer line\" />\n               </osp:region>\n            </td>\n         </tr>\n      </table>\n   </body>\n</html>\n'),('/group/PortfolioAdmin/system/Simplehtml.jpg','ˇÿˇ‡\0JFIF\0\0`\0`\0\0ˇ€\0C\0ˇ€\0Cˇ¿\0\09\0L\"\0ˇƒ\0\0\0\0\0\0\0\0\0\0\0	\nˇƒ\0µ\0\0\0}\0!1AQa\"q2Åë°#B±¡R—$3brÇ	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyzÉÑÖÜáàâäíìîïñóòôö¢£§•¶ß®©™≤≥¥µ∂∑∏π∫¬√ƒ≈∆«»… “”‘’÷◊ÿŸ⁄·‚„‰ÂÊÁËÈÍÒÚÛÙıˆ˜¯˘˙ˇƒ\0\0\0\0\0\0\0\0	\nˇƒ\0µ\0\0w\0!1AQaq\"2ÅBë°±¡	#3Rbr—\n$4·%Ò\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyzÇÉÑÖÜáàâäíìîïñóòôö¢£§•¶ß®©™≤≥¥µ∂∑∏π∫¬√ƒ≈∆«»… “”‘’÷◊ÿŸ⁄‚„‰ÂÊÁËÈÍÚÛÙıˆ˜¯˘˙ˇ⁄\0\0\0?\0˛éæ\'¯ˇ\0„EÁé‡§ø¸U˚u~◊ˇ\0~~ _¥ˇ\0¬?Çû¯O˚6|5˝ÅuÀ;O\n¯ªˆ;˝Ü˛(\\ÍÛˆé˝í>+x”[Ò•ÒSˆåÒ~©™]jøb”≠4/±i˙6ïjödV◊ ˇ\0√pËÙíO¯+˜˛ˇ\0¯#oˇ\0AE}}‚?ä®◊˛\nÂ‚æ|?˝†µˇ\0¡Wˇ\0aÇö¬oäæ(ˇ\0Ñ/·«à¸U˚F~ GŸ˚¬Záç<Rø˛2O£x¡˛&¯õ§¯€P∫≤¯c„-D≈·√ô•hKmuoË•j˙E¸ˇ\0#‚/ˇ\0IÍÄ>ˇ\0Ü·–ˇ\0È$üWÔ¸1ˇ\0Fﬂ˛Çä?·∏t?˙I\'¸˚ˇ\0¸∑ˇ\0†¢æ˛ˇ\0Ö+˚Sˇ\0“(‡ê?¯ô˙OT¬ï˝©ˇ\0È?H¸Lèàø˝\'™\0¯˛áCˇ\0§í¡_ø«ˇ\0¡˙\n(ˇ\0Ü·–ˇ\0È$üWÔ¸1ˇ\0Fﬂ˛Çä˚˚˛ØÌOˇ\0H°ˇ\0Ç@ˇ\0‚d|Eˇ\0È=Qˇ\0\nWˆßˇ\0§Pˇ\0¡ Ò2>\"ˇ\0Ùû®‡¯n˛íIˇ\0~ˇ\0√ˇ\0mˇ\0Ë(£˛áCˇ\0§í¡_ø«ˇ\0¡˙\n+ÔÔ¯Røµ?˝\"á˛	ˇ\0âëÒˇ\0§ıG¸)_⁄ü˛ëCˇ\0Åˇ\0ƒ»¯ãˇ\0“z†ò>¸wæ˝¢>,xS‡ÔÇø‡¶UÌ/ƒﬁ0˛›˛ÃæÒO¡Ø¯#ıñÖ¸#˛\Z÷<S{ˆÎ≠\'ˆ÷ıº›?DªÜ◊Ï˙]÷˚…-„ó…Å‰∏ãÙ˚˛	Â„üâû=˝õØÔ>.¸IÒ≈ˇ\0x/ˆü˝º>\nIÒ\'≈ö√ﬂx´≈û˝ûønü⁄7‡?√ΩCƒ⁄O¬è¸8¯wà\"¯w„¬∂:Õ◊Ö|·};T‘mnuC•As{p[Êk>˛—ü≤áæ+¡:`ÉzÌÒÉ∆ˇ\0<1Ògˆl˝£<GÒ#‚?ÄºU§˛Àø¥OÌ5·o¡<g(.<?Ø¯g‡Gâº™]iˇ\0¨u9|Se0“µ}<jµÔˇ\0M?˘7_àﬂˆˇ\0V/˝zoÌë@\0|dˇ\0úçˇ\0⁄ø‡äˇ\0¸\0∫˝æ¯≥ÒK¿üæ|K¯◊ÒK]ˇ\0Ñ_·ó¡ˇ\0á˛2¯•ÒƒﬂŸö∆∑ˇ\0ÔÅ>¯sRÒgãµﬂÏoÈ˙øà5ÏèÈ\ZÜ°˝ô°iZû±ˆ≤Èö}ÌÏ∞[K¯ÉÒì˛r7ˇ\0i˛ˇ\0Ç+ˇ\0Îˆ˚‚œ¸-?¯Uüˇ\0·Eˇ\0¬øˇ\0Ö€ˇ\0\nˇ\0∆_ß·lˇ\0¬Gˇ\0\n≥˛ü¸#öó¸+Ô¯Yáˇ\0≈[ˇ\0\nˇ\0˛ﬂÏè¯Lø·ˇ\0äè˛œÌ/ÏO¯ô˝ñÄ<ÉR˝¥?f\r‡¬O⁄á]¯ø·˝‡O«O¯≈ü<{‚/hPxü¬ø|\'ƒ›ƒÕ†Í⁄=üätË?\nlıˇ\0åµüËz>ùc‡◊Çæ#¸^¯øs‡oÜﬂ˛ ¯£√^?ˇ\0G˝Ñ‚ˇ\0êó«?¯Gºü˘\nˇ\0¬]À„É·˚7¸áˇ\0·6ˇ\0ÑØ·ˆèˇ\0\'¸ üÿˇ\0?·hˇ\0¬e˝Öˇ\0\nü˛_ˆÁˇ\0Öóˇ\0ß¸0\'Ì≠ˇ\0\nÿ5+ü€B€‡¬F—ÙoŸÉY˝®«á¸sÒ⁄Kƒﬂº5	ºU§¯.Ms‚~çí‚◊¬~,¯àæÒÔƒM./ÜÒ7åm_Q¯G‡øIÒ€ƒ>¯ŸÆ|+_Ÿœ„èˇ\0∆”løË¿<Iˆ˚8Øˇ\0¬W˝ìˇ\0á˛ˇ\0¸,¯Wˇ\0ıRˇ\0·P√[Õqˇ\0áwˇ\0∆–@¯z?Ï\'¸Ñæ9ˇ\0¬=‰ˇ\0»W˛ÔÜ_ºˇ\0«Ÿø‰?ˇ\0	∑¸%¥¯A?·˛«¯±ˇ\0G˛/Ï/¯Tˇ\0 ˇ\0∑?¸,ø¯E?·Å?mo¯Pá¸=ˆã˛B_ˇ\0·Ú‰+ˇ\0	w√/å^\rˇ\0ÑcÏﬂÚˇ\0Ñ€˛øá⁄?¸ üÇc¸Xˇ\0Ö£ˇ\0	óˆ¸*¯e€ü˛_¸\"ü¿ü∂∑¸(C˛6õeˇ\0F‚O∞Ÿ≈x\'˛øÏü¸8Øˇ\0·`¬øˇ\0™óˇ\0\nÉ˛\Z€˛kè¸;ø˛6Ç∆”løË¿<Iˆ˚8Øˇ\0¬W˝ìˇ\0á˛ˇ\0¸,¯Wˇ\0ıRˇ\0·P√[Õqˇ\0áwˇ\0∆–@¯z?Ï\'¸Ñæ9ˇ\0¬=‰ˇ\0»W˛ÔÜ_ºˇ\0«Ÿø‰?ˇ\0	∑¸%¥¯A?·˛«¯±ˇ\0G˛/Ï/¯Tˇ\0 ˇ\0∑?¸,ø¯E?·Å?mo¯Pü@|˝®˛˛“_î≈w«?ëÍ~˛ƒˇ\0Ñ√√öøÜ|c‡?¯g˚˚^ ◊˚o¡_º=·_ÈﬂŸﬁ-ØƒÖ>)˚Vã¸!æ¸t˝ü¸a˝ÖÒ∑‡G∆/\0xÁˇ\0¯⁄mó˝â>¡ˇ\0g‡ü¯Jˇ\0≤‡¬øˇ\0ÖÅˇ\0\nˇ\0˛™_¸*¯ko˘Æ?Óˇ\0¯⁄–ˇ\0·©·ˇ\0Ñ¶«ˆóˇ\0Ü‘æœ˝âˇ\0Wâæˇ\0¬≈—?µ~…˝Ø·Ô¬S‡oˇ\0¬Aˇ\0ˇ\0¸$èË?tOÏüàû&ˇ\0Ñ?˛ﬁØ˚:_ˇ\0¬qˇ\0Ôg˚Q˛‘\0?˛ŸÚq_Iﬂ˚?ˇ\0àˇ\0˙Î/¯)eM?˘7_àﬂˆˇ\0V/˝zoÌëGÌëˇ\0\'ˇ\0ùˇ\0≥ˇ\0¯èˇ\0Æ≤ˇ\0ÇñQˇ\0”ˇ\0ìu¯çˇ\0gˇ\0ˇ\0bˇ\0◊¶˛Ÿ∆O˘»ﬂ˝ß˚˛Øˇ\0¿Ø◊ˇ\0⁄«L&∑˚,~“⁄7≈/Öø>8¸2’ˇ\0gˇ\0åögƒ_Çü	Ù˝cW¯ßÒÉ¿óˇ\0ºGk‚ÔÖø\r4ØÎæÒßÒ‚áÂ‘<\'‡›?BÒ7áuãﬂÍ˙m∂ôÆÈ≤¡®[˛@|dˇ\0úçˇ\0⁄ø‡äˇ\0¸\0∫˝˛†Ã¯gˆgèˆ˝íº9´~Àüµ˛ù+C¯˛gÏ—‡ﬂ~–â˚B|ÁÖ~œ·?¯[:√ÕÒG¯Å˚&¸?“Ô¸¶ˇ\0ox◊≈_¸9˚L|(¯i{˚4¬}˚ziﬂ≤ˆìØ¸Åˇ\0\nÀˆ±ˇ\0_ˇ\0∏˝ø¸	˝üˇ\0?à¸>¯≈gˇ\0\nÁ˚˛?·	ˇ\0Ö1ÒœU˚¸)¯W>%ˇ\0Ö]ˇ\0\n˚c˚+˛Ø˚√%¬Kˇ\0\'¸˘ˇ\0£˜˙ä\0¸ˇ\0Öe˚	ÿˇ\0Øˇ\0Ç\\~ﬂ˛˛œˇ\0èü¯D~|b≥ˇ\0Ös˝èˇ\0øÑˇ\0¬ò¯Á™˝á˛ø¸+üˇ\0¬Æˇ\0Ö\r˝±˝ïˇ\0◊˝Ü?·íø·%ˇ\0Ñ˛\r¸ˇ\0Ö–¬≤˝ÑÏ◊ˇ\0¡.?oˇ\0gˇ\0«œ¸\"?æ1Yˇ\0¬π˛«ˇ\0èﬂ¯B·L|s’~√ˇ\0\n_˛œâ·W¬Ü˛ÿ˛ ˇ\0ák˛√…_íˇ\0¬	ˇ\0˛¬Ë˝˛¢Ä?\0·Y~¬v?Îˇ\0‡ó∑ˇ\0Å?≥ˇ\0„Á˛áﬂ¨ˇ\0·\\ˇ\0cˇ\0«Ô¸!?¶>9Íøaˇ\0Ö/ˇ\0\nÁƒø´ø·Cle√µˇ\0aè¯dØ¯I·ˇ\0É?·t}ˇ\0˚xg‡OÖ·hÿ|˝ó?hŸ6‚O¯B·*≈ﬂx«√û‘≤ˇ\0·.Êôˇ\0\nÓ ¯˚‡ìˇ\0¬®‘Ù?~Õ_¨ıh≥æ¸˝ù4Öˇ\0€ˇ\0Oç˛	È„_}ˇ\0E\0|˚d…≈¡\'Ïˇ\0˛#ˇ\0Î¨ø‡•î¡4ˇ\0‰›~#Ÿˇ\0ˇ\0¡XøıÈø∂E∂G¸úW¸w˛œˇ\0‚?˛∫À˛\nYG¸O˛M◊‚7˝üˇ\0¸ãˇ\0^õ˚dPÂÌÒÔ‡á<iˇ\0L¯/‚⁄Àˆ ¯%˚AÈøUˇ\0¯\'WÌA‡_ÜﬂµÁÌ[‡øŸØNÒßÖg/Ä_GOèâ{´_i>9Ò∂Ö·ˇ\0\Z€|ÒèÇt\Zhˇ\0ºeßA‚®eÇ[àÙÕPZ˚¸>£˛´\'¸ˇ\0Èˇ\0Ê‰ØﬂÍ(˛Qˇ\0Uì˛ˇ\0ãÙˇ\0ÛrQˇ\0®ˇ\0™…ˇ\0ˇ\0≈˙˘π+˜˙ä\0¸ˇ\0á‘’dˇ\0Ç\0ˇ\0‚˝?¸‹î√Í?Í≤¡\0Ò~ü˛nJ˝˛¢Ä?\0·ııY?‡Ä?¯øOˇ\07%˙è˙¨ü@¸_ßˇ\0õíø®†ÁSˇ\0Çë¸,¯„˚@~∆>.¯◊˚_¡~¸2˝ôˇ\0h|tÒ6±∑˛·œéæ;ÒˆﬂÏÅ˚S~Õ˙7Ö¥/x≥ˆS˝ü<?ô‚⁄JÒß≠Ím˛¡£¯sPÇ◊H’/om£ãÙ˛	O‚œ\n¯˜ˆKÒéº‚o¯”¡>4˝∑ˇ\0‡©>,wå|\'¨Èﬁ#Øã<+‚?¯)ÁÌÅ¨x{ƒﬁÒèsy§kﬁ◊¥ãÀ=SF÷tªÀ≠;T”Æ≠Ø¨ng∂û)[Ù~ä\0ˇŸ'),('/group/PortfolioAdmin/system/simpleRichText.xml','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\r\n   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n<html xmlns=\"http://www.w3.org/1999/xhtml\"\r\n      xmlns:osp=\"http://www.osportfolio.org/OspML\"\r\n      xml:lang=\"en\" lang=\"en\">\r\n   <head>\r\n      <title>OSP Presentation</title>\r\n   </head>\r\n   <body><osp:region id=\"region_1\">\r\n                  <osp:texttype helpText=\"Type your rich text content here\" isRichText=\"true\" width=\"800\" height=\"600\" />\r\n               </osp:region>\r\n </body>\r\n</html>');
/*!40000 ALTER TABLE `CONTENT_RESOURCE_BODY_BINARY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTENT_RESOURCE_DELETE`
--

DROP TABLE IF EXISTS `CONTENT_RESOURCE_DELETE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTENT_RESOURCE_DELETE` (
  `RESOURCE_ID` varchar(255) NOT NULL,
  `RESOURCE_UUID` varchar(36) DEFAULT NULL,
  `IN_COLLECTION` varchar(255) DEFAULT NULL,
  `CONTEXT` varchar(99) DEFAULT NULL,
  `FILE_PATH` varchar(128) DEFAULT NULL,
  `FILE_SIZE` bigint(20) DEFAULT NULL,
  `RESOURCE_TYPE_ID` varchar(255) DEFAULT NULL,
  `DELETE_DATE` datetime DEFAULT NULL,
  `DELETE_USERID` varchar(36) DEFAULT NULL,
  `XML` longtext,
  `BINARY_ENTITY` blob,
  UNIQUE KEY `CONTENT_RESOURCE_UUID_DELETE_I` (`RESOURCE_UUID`),
  KEY `CONTENT_RESOURCE_DELETE_INDEX` (`RESOURCE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTENT_RESOURCE_DELETE`
--

LOCK TABLES `CONTENT_RESOURCE_DELETE` WRITE;
/*!40000 ALTER TABLE `CONTENT_RESOURCE_DELETE` DISABLE KEYS */;
INSERT INTO `CONTENT_RESOURCE_DELETE` VALUES ('/group/PortfolioAdmin/system/formCreate.xslt','0a06a97a-6efa-45c1-a77b-8025e52a0ea0','/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,12573,'org.sakaiproject.content.types.fileUpload','2011-04-13 15:06:56','admin',NULL,'CHSBRE\0\0\0\0\0\0\n\0,/group/PortfolioAdmin/system/formCreate.xslt\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0text/xml\0\0\0e\0DAV:getlastmodified\020110413190656375\0\0\0e\0DAV:displayname\0formCreate.xslt\0\0\0e\0SAKAI:content_priority\0	268435457\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413190656375\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\01used for default rendering of form add and update\0\0\0e\0DAV:getcontentlength\012573\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0text/xml\0\0\0\0\0\01\0\0\0\0\0'),('/group/PortfolioAdmin/system/formFieldTemplate.xslt','a0f9dcab-dba4-4b4a-8dfe-3052f49135a2','/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,64703,'org.sakaiproject.content.types.fileUpload','2011-04-13 15:06:56','admin',NULL,'CHSBRE\0\0\0\0\0\0\n\03/group/PortfolioAdmin/system/formFieldTemplate.xslt\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0text/xml\0\0\0e\0DAV:getlastmodified\020110413190656639\0\0\0e\0DAV:displayname\0formFieldTemplate.xslt\0\0\0e\0SAKAI:content_priority\0	268435458\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413190656639\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\0)used for default rendering of form fields\0\0\0e\0DAV:getcontentlength\064703\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0text/xml\0\0\0\0\0\0¸ø\0\0\0\0\0'),('/group/PortfolioAdmin/system/formView.xslt','9f6e4b3b-6d38-4031-b391-697b8d761e1d','/group/PortfolioAdmin/system/','PortfolioAdmin',NULL,19385,'org.sakaiproject.content.types.fileUpload','2011-04-13 15:06:56','admin',NULL,'CHSBRE\0\0\0\0\0\0\n\0*/group/PortfolioAdmin/system/formView.xslt\0)org.sakaiproject.content.types.fileUpload\0	inherited\0\0\0\0ˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇˇ\0\0\0\0\0\0\0\0\0\0\r\0\0\0\0\0\0d\0\0\0\0\0\0e\0DAV:getcontenttype\0text/xml\0\0\0e\0DAV:getlastmodified\020110413190656696\0\0\0e\0DAV:displayname\0\rformView.xslt\0\0\0e\0SAKAI:content_priority\0	268435459\0\0\0e\0encoding\0UTF-8\0\0\0e\0CHEF:creator\0admin\0\0\0e\0DAV:creationdate\020110413190656696\0\0\0e\0CHEF:is-collection\0false\0\0\0e\0CHEF:description\0*used for default rendering of form viewing\0\0\0e\0DAV:getcontentlength\019385\0\0\0e\0CHEF:modifiedby\0admin\0\0\0\0text/xml\0\0\0\0\0\0Kπ\0\0\0\0\0');
/*!40000 ALTER TABLE `CONTENT_RESOURCE_DELETE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTENT_TYPE_REGISTRY`
--

DROP TABLE IF EXISTS `CONTENT_TYPE_REGISTRY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTENT_TYPE_REGISTRY` (
  `CONTEXT_ID` varchar(99) NOT NULL,
  `RESOURCE_TYPE_ID` varchar(255) DEFAULT NULL,
  `ENABLED` varchar(1) DEFAULT NULL,
  KEY `content_type_registry_idx` (`CONTEXT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTENT_TYPE_REGISTRY`
--

LOCK TABLES `CONTENT_TYPE_REGISTRY` WRITE;
/*!40000 ALTER TABLE `CONTENT_TYPE_REGISTRY` DISABLE KEYS */;
/*!40000 ALTER TABLE `CONTENT_TYPE_REGISTRY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EMAIL_TEMPLATE_ITEM`
--

DROP TABLE IF EXISTS `EMAIL_TEMPLATE_ITEM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EMAIL_TEMPLATE_ITEM` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LAST_MODIFIED` datetime NOT NULL,
  `OWNER` varchar(255) NOT NULL,
  `SUBJECT` longtext NOT NULL,
  `emailfrom` varchar(255) DEFAULT NULL,
  `MESSAGE` longtext NOT NULL,
  `HTMLMESSAGE` longtext,
  `TEMPLATE_KEY` varchar(255) NOT NULL,
  `TEMPLATE_LOCALE` varchar(255) DEFAULT NULL,
  `defaultType` varchar(255) DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EMAIL_TEMPLATE_ITEM`
--

LOCK TABLES `EMAIL_TEMPLATE_ITEM` WRITE;
/*!40000 ALTER TABLE `EMAIL_TEMPLATE_ITEM` DISABLE KEYS */;
INSERT INTO `EMAIL_TEMPLATE_ITEM` VALUES (1,'2011-04-13 14:59:24','admin',' Welcome To ${localSakaiName}!',NULL,'${addedBy} (${addedByEmail}) has invited you to join the ${memberSites} site on ${localSakaiName}.\n\nAccept this invitation ${url}\n\nWhat is ${localSakaiName}?\n${localSakaiName} is the ${institution}‚Äôs web-based content management system. It is home to many course sites, as well as a host of other sites including those used for administration, research and project groups, libraries and student societies. As a guest user, you have been invited to join a ${localSakaiName} site. On the site you will have rights to read content but you also may be able to create and/or edit content (depending on your assigned role).\n\nWhat if I already have a ${localSakaiName} account?\nIf you have used ${localSakaiName} in the past then you already have a ${localSakaiName} account, in\nwhich case we strongly suggest that you accept the above invitation and then\nindicate which existing ${localSakaiName} account you wish to use to access this site. This\nwill avoid you having multiple accounts, each with a different set of\nassociated sites. If you do not choose to associate this site with an existing\naccount, a new guest account will be created for you using this email\naddress. And in the future to access the site you must login to ${localSakaiName} using the\nguest account username and its associated password.','<p>${addedBy} (${addedByEmail}) has invited you to join the <strong>${memberSites}</strong> site on ${localSakaiName}.</p>\n\n<p><a href=${url}>Accept this invitation ${url}</a></p>\n\n<h2>What is ${localSakaiName}?</h2>\n<p>${localSakaiName} is the ${institution}‚Äôs web-based content management system. It is home to many course sites, as well as a host of other sites including those used for administration, research and project groups, libraries and student societies. As a guest user, you have been invited to join a ${localSakaiName} site. On the site you will have rights to read content but you also may be able to create and/or edit content (depending on your assigned role).</p>\n\n\n<h2>What if I already have a ${localSakaiName} account?</h2>\n<p>If you have used ${localSakaiName} in the past then you already have a ${localSakaiName} account, in\nwhich case we strongly suggest that you accept the above invitation and then\nindicate which existing ${localSakaiName} account you wish to use to access this site. This\nwill avoid you having multiple accounts, each with a different set of\nassociated sites. If you do not choose to associate this site with an existing\naccount, a new guest account will be created for you using this email\naddress. And in the future to access the site you must login to ${localSakaiName} using the\nguest account username and its associated password.</p>','validate.newUser','',NULL,1),(2,'2011-04-13 14:59:24','admin',' Please Validate your ${localSakaiName} Account',NULL,'Dear ${displayName}\n\nYou currently have a guest account on  with access to the following sites: \n\n${memberSites}\n\nPlease if you still require access to these sites using your current login details ${userEid}, or if you would like to access these sites through your UCT staff/student account or another guest, please follow this link to: ${url}\n\nIf you no longer require access to ${localSakaiName} using this login, simply ignore this email and your account will be deactivated. If you wish to reactivate your account, please contact help@vula.uct.ac.za \n\n*Please note*: Accounts that have been deactivated for longer than 90 days\n will be deleted.\n\n','<p>Dear ${displayName}<p>\n\n<p>You currently have a guest account on  with access to the following sites: <br/>\n\n${memberSites}\n</p>\n\n<p>Please if you still require access to these sites using your current login details ${userEid}, or if you would like to access these sites through your UCT staff/student account or another guest, please follow this <a href=\"${url}\">link</a></p>\n\n</p,If you no longer require access to  using this login, simply ignore this email and your account will be deactivated. If you wish to reactivate your account, please contact ${supportemail} </p>\n\n<p><strong>Please note</strong>: Accounts that have been deactivated for longer than 90 days will be deleted.</p>','validate.existinguser','',NULL,1),(3,'2011-04-13 14:59:24','admin',' Please verify your details',NULL,'\nDear ${displayName}		\n\n${addedBy} (${addedByEmail}) invited you to join ${localSakaiName}.\n\nPlease confirm your details on ${url}\n\nWhat is ${localSakaiName}?\n${localSakaiName} is the ${institution}‚Äôs web-based content management system. It is home to many course sites, as well as a host of other sites including those used for administration, research and project groups, libraries and student societies. As a guest user, you have been invited to join a ${localSakaiName} site. On the site you will have rights to read content but you also may be able to create and/or edit content (depending on your assigned role).\n\nWhat if I already have another ${localSakaiName} account?\nIf you have used${localSakaiName} with another ${localSakaiName} account, we strongly suggest that you accept the above invitation and then\nindicate which existing ${localSakaiName} account you wish to use to access this site. This will avoid you having multiple accounts, each with a different set of\nassociated sites. If you do not choose to associate this site with an existing account, a new guest account will be created for you using this email\naddress. And in the future to access the site you must login to ${localSakaiName} using the guest account username and its associated password.\n',NULL,'validate.legacyuser','',NULL,1),(4,'2011-04-13 14:59:24','admin',' New Password Request',NULL,'\n		Dear ${displayName}\n		\n		\n		PLease use the following link to reset your password on ${localSakaiName}:\n		\n		${url}\n		\n		Your username is: ${userEid} \n		\n		Regards\n		\n		The ${localSakaiName} Team\n		${institution}\n		',NULL,'validate.passwordreset','',NULL,1),(5,'2011-04-13 15:07:10','admin','[${localSakaiName}] A poll option you voted for has been deleted',NULL,'Dear ${recipientFirstName},\n\nThe poll option you voted for in the site \'${siteTitle}\' has been deleted by a poll maintainer. The poll question is:\n\n${pollQuestion}\n\nPlease log in to ${localSakaiName} and place a new vote for the poll.',NULL,'polls.notifyDeletedOption','',NULL,0),(6,'2011-04-13 14:59:49','admin','${senderDisplayName} sent you a message on ${localSakaiName}',NULL,'${senderDisplayName} sent you a message on ${localSakaiName}\n\n--------------------\n${messageSubject}\n\n${messageBody}\n--------------------\n\nTo view this message, follow the link below:\n${messageLink}\n\n---\nThis automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\nControl what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n	','${senderDisplayName} sent you a message on ${localSakaiName}\n<p>\n--------------------\n<br />${messageSubject}\n<br />\n<br />${messageBody}\n<br />--------------------\n</p>\n\n<p>To view this message, follow the link below:\n<br />${messageLink}\n</p>\n\n<p>\n---\n<br />This automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\n<br />Control what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n</p>\n	','profile2.messageNew','en',NULL,2),(7,'2011-04-13 14:59:49','admin','${senderDisplayName} replied to your message on ${localSakaiName}',NULL,'${senderDisplayName} replied to your message on ${localSakaiName}\n\n--------------------\n${messageSubject}\n\n${messageBody}\n--------------------\n\nTo view this message, follow the link below:\n${messageLink}\n\n---\nThis automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\nControl what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n	','${senderDisplayName} replied to your message on ${localSakaiName}\n<p>\n--------------------\n<br />${messageSubject}\n<br />\n<br />${messageBody}\n<br />--------------------\n</p>\n\n<p>To view this message, follow the link below:\n<br />${messageLink}\n</p>\n\n<p>\n---\n<br />This automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\n<br />Control what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n</p>\n	','profile2.messageReply','en',NULL,2),(8,'2011-04-13 14:59:49','admin','${senderDisplayName} added you as a connection on ${localSakaiName}',NULL,'${senderDisplayName} added you as a connection on ${localSakaiName}\n\nTo confirm ${senderDisplayName}\'s connection request, follow the url below:\n${connectionLink}\n\n---\nThis automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\nControl what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n	','${senderDisplayName} added you as a connection on ${localSakaiName}\n<p>\nTo confirm ${senderDisplayName}\'s connection request, follow the url below:\n<br />${connectionLink}\n</p>\n<p>\n---\n<br />This automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\n<br />Control what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n</p>\n	','profile2.connectionRequest','en',NULL,1),(9,'2011-04-13 14:59:49','admin','${senderDisplayName} confirmed your connection request on ${localSakaiName}',NULL,'${senderDisplayName} confirmed your connection request on ${localSakaiName}\n\nTo view {senderDisplayName}\'s profile, follow the url below:\n${connectionLink}\n\n---\nThis automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\nControl what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n	','${senderDisplayName} confirmed your connection request on ${localSakaiName}\n<p>\nTo view ${senderDisplayName}\'s profile, follow the url below:\n<br />${connectionLink}\n</p>\n<p>\n---\n<br />This automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\n<br />Control what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n</p>\n	','profile2.connectionConfirm','en',NULL,1),(10,'2011-04-13 14:59:49','admin','${senderDisplayName} added activity to their profile wall on ${localSakaiName}',NULL,'${displayName},\n	\n${senderDisplayName} added activity to their profile wall on ${localSakaiName}\n\nTo view their profile, follow the link below:\n${senderProfileLink}\n\n---\nThis automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\nControl what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n	','${displayName},\n<p>\n${senderDisplayName} added activity to their profile wall on ${localSakaiName}\n<p>\nTo view their profile, follow the link below:\n<br />${senderProfileLink}\n</p>\n<p>\n---\n<br />This automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\n<br />Control what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n</p>\n	','profile2.wallEventNew','en',NULL,1),(11,'2011-04-13 14:59:49','admin','${senderDisplayName} has just posted to your profile wall on ${localSakaiName}',NULL,'${displayName},\n	\n${senderDisplayName} has just posted to your profile wall on ${localSakaiName}\n\nTo view your profile, follow the link below:\n${profileLink}\n\n---\nThis automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\nControl what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n	','${displayName},\n<p>\n${senderDisplayName} has just posted to your profile wall on ${localSakaiName}\n<p>\nTo view your profile, follow the link below:\n<br />${profileLink}\n</p>\n<p>\n---\n<br />This automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\n<br />Control what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n</p>\n	','profile2.wallPostMyWallNew','en',NULL,1),(12,'2011-04-13 14:59:49','admin','${senderDisplayName} has just posted to his/her profile wall on ${localSakaiName}',NULL,'${displayName},\n	\n${senderDisplayName} has just posted to his/her profile wall on ${localSakaiName}\n\nTo view ${senderDisplayName}\'s profile, follow the link below:\n${senderProfileLink}\n\n---\nThis automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\nControl what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n	','${displayName},\n<p>\n${senderDisplayName} has just posted to his/her profile wall on ${localSakaiName}\n<p>\nTo view ${senderDisplayName}\'s profile, follow the link below:\n<br />${senderProfileLink}\n</p>\n<p>\n---\n<br />This automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\n<br />Control what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n</p>\n	','profile2.wallPostConnectionWallNew','en',NULL,1),(13,'2011-04-13 14:59:49','admin','${senderDisplayName}\'s new status was posted on their profile wall on ${localSakaiName}',NULL,'${displayName},\n	\n${senderDisplayName}\'s new status was posted on their profile wall on ${localSakaiName}\n\nTo view their profile, follow the link below:\n${senderProfileLink}\n\n---\nThis automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\nControl what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n	','${displayName},\n<p>\n${senderDisplayName}\'s new status was posted on their profile wall on ${localSakaiName}\n<p>\nTo view their profile, follow the link below:\n<br />${senderProfileLink}\n</p>\n<p>\n---\n<br />This automatic notification message was sent by ${localSakaiName} (${localSakaiUrl})\n<br />Control what email notifications you receive from connections at My Workspace > ${toolName} > Preferences\n</p>\n	','profile2.wallStatusNew','en',NULL,1),(14,'2011-04-13 15:00:00','admin','${localSakaiName} Site Notification',NULL,'\n			Dear ${userName},\n\n				You have been added to the following ${localSakaiName} site: \n					${siteName}\n				by ${currentUserName}.\n			\n			<#if newNonOfficialAccount == \"true\" >\n			<#if hasNonOfficialAccountUrl == \"true\" >	\n				To get a guest account, open the following site:\n					${nonOfficialAccountUrl}\n				and follow the steps listed.\n			\n			</#if>\n				Once you have your guest account, you can log in to ${localSakaiName}:\n				\n					1. Open ${localSakaiName}: ${localSakaiURL}\n					2. Click the Login button.\n					3. Type your guest account login and password, and click Login.\n					4. Click on the site tab to go to the site. (You will see two or more tabs in a row across the upper part of the screen.)\n			\n			<#else>		\n				To log in:\n\n					1. Open ${localSakaiName}: ${localSakaiURL}\n					2. Click the Login button.\n					3. Enter your username and password, and click Login.\n					4. Click on the site tab to go to the site. (You will see two or more tabs in a row across the upper part of the screen.)\n			</#if>\n		',NULL,'sitemange.notifyAddedParticipant','',NULL,1),(15,'2011-04-13 15:00:00','admin','${localSakaiName} Notificaci√≥ de lloc\n		',NULL,'${userName}: Se us ha\n			afegit al/s seg√ºent/s ${localSakaiName} lloc: per\n			${currentUserDisplayName}. <#if newNonOfficialAccount == \"true\" >Insert your institutions specific instructions for new guest users here \n\nQuan tingueu el vostre compte de convidat, ja podeu accedir-hi. ${localSakaiName} : \n1. Obriu ${localSakaiName} : ${localSakaiURL}\n2. Feu clic al bot√≥ Entrar.\n3. Escriviu el vostre compte d\'amic d\'inici de sessi√≥ i contrasenya, i feu clic a Entrar.\n<#else >Per entrar:\n1. Obriu ${localSakaiName} : ${localSakaiURL}\n2. Feu clic al bot√≥ Entrar.\n3. Escriviu el vostre nom d\'usuari i contrasenya i feu clic a Entrar.\n</#if>4. Aneu al lloc, feu clic a la pestanya del lloc (veureu dues o m√©s pestanyes en una fila a la part superior de la pantalla).',NULL,'sitemange.notifyAddedParticipant','ca',NULL,1),(16,'2011-04-13 15:00:00','admin','${localSakaiName} ',NULL,'${userName}:\n\n ${localSakaiName}\n ${userName}. \n\n<#if newNonOfficialAccount == \"true\" >Insert your institutions specific instructions for new guest users here \n\n ${localSakaiName} : \n ${localSakaiName} : ${localSakaiURL}\n\n\n<#else >\n ${localSakaiName} : ${localSakaiURL}\n\n\n</#if>',NULL,'sitemange.notifyAddedParticipant','en_GB',NULL,1),(17,'2011-04-13 15:00:00','admin','${localSakaiName} Site melding',NULL,'${userName}:\n\nU bent toegevoegd aan de volgende ${localSakaiName}  site:\ndoor ${currentUserDisplayName}. \n\n<#if newNonOfficialAccount == \"true\" >Insert your institutions specific instructions for new guest users here </#if>\n\nZodra u uw gastaccount heeft, kunt u inloggen voor ${localSakaiName} : \n1. Open ${localSakaiName} : ${localSakaiURL}\n2. Klik op de inlog knop.\n3. Vul uw inloggegevens als gast in en klik op Inloggen.\n<#else >Voor inlog:\n1. Open ${localSakaiName} : ${localSakaiURL}\n2. Klik op de inlog knop.\n3. Vul uw gebruikersnaam en wachtwoord in en klik op Inloggen.\n</#if>4. Ga naar de site en klik op de site tab. (U ziet twee of meer tabs naast elkaar over het bovenste deel van het scherm.)',NULL,'sitemange.notifyAddedParticipant','nl',NULL,1),(18,'2011-04-13 15:00:00','admin','${localSakaiName} New User Notification ',NULL,'Dear ${userName},\n\n An account on ${localSakaiName} (${localSakaiURL}) has been created for you by ${currentUserDisplayName}. To access your account:\n\n	1) Go to:\n		${localSakaiURL}\n	\n	2) Login using:\n		User Id: 	${userEid}\n		password: 	${newPassword} \n You can change your password after you have logged in, using the Account tool in your My Workspace.\n \n (This is an automated message from ${localSakaiName}.)\n',NULL,'sitemanage.notifyNewUserEmail','',NULL,1),(19,'2011-04-13 15:00:00','admin','${localSakaiName} Notificaci al nou usuari',NULL,'${userName}:\n\nUs hem afegit a ${localSakaiName} (${localSakaiURL})per ${currentUserDisplayName}. \n\nLa vostra contrasenya √©s \n ${newPassword} \n\nDespr√©s podeu anar a l\'eina de Comptes al vostre lloc El meu espai de treball per reajustar-la.\n\n',NULL,'sitemanage.notifyNewUserEmail','ca',NULL,1),(20,'2011-04-13 15:00:00','admin','${localSakaiName} Nieuwe gebruiker melding',NULL,'${userName}:\n\nJe bent toegevoegd aan ${localSakaiName} (${localSakaiURL})door ${currentUserDisplayName}. \n\nUw wachtwoord is \n ${newPassword} \n\nU kunt naar de \'Accountgegevens\' gaan in \'Mijn Werkruimte\' om het wachtwoord te wijzigen.\n\n',NULL,'sitemanage.notifyNewUserEmail','nl',NULL,1),(21,'2011-04-13 15:00:00','admin','${localSakaiName} ',NULL,'${userName}:\n\nHa sido a√±adido a ${localSakaiName} (${localSakaiURL})por ${currentUserDisplayName}. \n\nSu password es \n ${newPassword} \n\nPuede ir a la herramienta de Cuentas en \'Mi Sito de Trabajo\' para inicializarlo.\n\n',NULL,'sitemanage.notifyNewUserEmail','es',NULL,1);
/*!40000 ALTER TABLE `EMAIL_TEMPLATE_ITEM` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_CATEGORY_T`
--

DROP TABLE IF EXISTS `GB_CATEGORY_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_CATEGORY_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `GRADEBOOK_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `WEIGHT` double DEFAULT NULL,
  `DROP_LOWEST` int(11) DEFAULT NULL,
  `REMOVED` bit(1) DEFAULT NULL,
  `IS_EXTRA_CREDIT` bit(1) DEFAULT NULL,
  `IS_EQUAL_WEIGHT_ASSNS` bit(1) DEFAULT NULL,
  `IS_UNWEIGHTED` bit(1) DEFAULT NULL,
  `CATEGORY_ORDER` int(11) DEFAULT NULL,
  `ENFORCE_POINT_WEIGHTING` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKCD333737325D7986` (`GRADEBOOK_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_CATEGORY_T`
--

LOCK TABLES `GB_CATEGORY_T` WRITE;
/*!40000 ALTER TABLE `GB_CATEGORY_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `GB_CATEGORY_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_COMMENT_T`
--

DROP TABLE IF EXISTS `GB_COMMENT_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_COMMENT_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `GRADER_ID` varchar(255) NOT NULL,
  `STUDENT_ID` varchar(255) NOT NULL,
  `COMMENT_TEXT` text,
  `DATE_RECORDED` datetime NOT NULL,
  `GRADABLE_OBJECT_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `STUDENT_ID` (`STUDENT_ID`,`GRADABLE_OBJECT_ID`),
  KEY `FK7977DFF06F98CFF` (`GRADABLE_OBJECT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_COMMENT_T`
--

LOCK TABLES `GB_COMMENT_T` WRITE;
/*!40000 ALTER TABLE `GB_COMMENT_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `GB_COMMENT_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_GRADABLE_OBJECT_T`
--

DROP TABLE IF EXISTS `GB_GRADABLE_OBJECT_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_GRADABLE_OBJECT_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `OBJECT_TYPE_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `GRADEBOOK_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `REMOVED` bit(1) DEFAULT NULL,
  `SORT_ORDER` int(11) DEFAULT NULL,
  `POINTS_POSSIBLE` double DEFAULT NULL,
  `DUE_DATE` date DEFAULT NULL,
  `NOT_COUNTED` bit(1) DEFAULT NULL,
  `EXTERNALLY_MAINTAINED` bit(1) DEFAULT NULL,
  `EXTERNAL_STUDENT_LINK` varchar(255) DEFAULT NULL,
  `EXTERNAL_INSTRUCTOR_LINK` varchar(255) DEFAULT NULL,
  `EXTERNAL_ID` varchar(255) DEFAULT NULL,
  `EXTERNAL_APP_NAME` varchar(255) DEFAULT NULL,
  `IS_EXTRA_CREDIT` bit(1) DEFAULT NULL,
  `ASSIGNMENT_WEIGHTING` double DEFAULT NULL,
  `RELEASED` bit(1) DEFAULT NULL,
  `CATEGORY_ID` bigint(20) DEFAULT NULL,
  `UNGRADED` bit(1) DEFAULT NULL,
  `IS_NULL_ZERO` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK759996A7F09DEFAE` (`CATEGORY_ID`),
  KEY `FK759996A7325D7986` (`GRADEBOOK_ID`),
  KEY `GB_GRADABLE_OBJ_ASN_IDX` (`OBJECT_TYPE_ID`,`GRADEBOOK_ID`,`NAME`,`REMOVED`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_GRADABLE_OBJECT_T`
--

LOCK TABLES `GB_GRADABLE_OBJECT_T` WRITE;
/*!40000 ALTER TABLE `GB_GRADABLE_OBJECT_T` DISABLE KEYS */;
INSERT INTO `GB_GRADABLE_OBJECT_T` VALUES (1,2,0,1,'Course Grade','\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `GB_GRADABLE_OBJECT_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_GRADEBOOK_T`
--

DROP TABLE IF EXISTS `GB_GRADEBOOK_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_GRADEBOOK_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `GRADEBOOK_UID` varchar(255) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `SELECTED_GRADE_MAPPING_ID` bigint(20) DEFAULT NULL,
  `ASSIGNMENTS_DISPLAYED` bit(1) NOT NULL,
  `COURSE_GRADE_DISPLAYED` bit(1) NOT NULL,
  `ALL_ASSIGNMENTS_ENTERED` bit(1) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `GRADE_TYPE` int(11) NOT NULL,
  `CATEGORY_TYPE` int(11) NOT NULL,
  `IS_EQUAL_WEIGHT_CATS` bit(1) DEFAULT NULL,
  `IS_SCALED_EXTRA_CREDIT` bit(1) DEFAULT NULL,
  `DO_SHOW_MEAN` bit(1) DEFAULT NULL,
  `DO_SHOW_MEDIAN` bit(1) DEFAULT NULL,
  `DO_SHOW_MODE` bit(1) DEFAULT NULL,
  `DO_SHOW_RANK` bit(1) DEFAULT NULL,
  `DO_SHOW_ITEM_STATS` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `GRADEBOOK_UID` (`GRADEBOOK_UID`),
  KEY `FK7C870191552B7E63` (`SELECTED_GRADE_MAPPING_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_GRADEBOOK_T`
--

LOCK TABLES `GB_GRADEBOOK_T` WRITE;
/*!40000 ALTER TABLE `GB_GRADEBOOK_T` DISABLE KEYS */;
INSERT INTO `GB_GRADEBOOK_T` VALUES (1,1,'92baf195-be33-4a5b-b378-6d96e9665ffc','92baf195-be33-4a5b-b378-6d96e9665ffc',2,'','\0','\0','\0',1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `GB_GRADEBOOK_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_GRADE_MAP_T`
--

DROP TABLE IF EXISTS `GB_GRADE_MAP_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_GRADE_MAP_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `OBJECT_TYPE_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `GRADEBOOK_ID` bigint(20) NOT NULL,
  `GB_GRADING_SCALE_T` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKADE11225181E947A` (`GB_GRADING_SCALE_T`),
  KEY `FKADE11225325D7986` (`GRADEBOOK_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_GRADE_MAP_T`
--

LOCK TABLES `GB_GRADE_MAP_T` WRITE;
/*!40000 ALTER TABLE `GB_GRADE_MAP_T` DISABLE KEYS */;
INSERT INTO `GB_GRADE_MAP_T` VALUES (1,0,0,1,1),(2,0,0,1,2),(3,0,0,1,3);
/*!40000 ALTER TABLE `GB_GRADE_MAP_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_GRADE_RECORD_T`
--

DROP TABLE IF EXISTS `GB_GRADE_RECORD_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_GRADE_RECORD_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `OBJECT_TYPE_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `GRADABLE_OBJECT_ID` bigint(20) NOT NULL,
  `STUDENT_ID` varchar(255) NOT NULL,
  `GRADER_ID` varchar(255) NOT NULL,
  `DATE_RECORDED` datetime NOT NULL,
  `POINTS_EARNED` double DEFAULT NULL,
  `IS_EXCLUDED_FROM_GRADE` bit(1) DEFAULT NULL,
  `USER_ENTERED_GRADE` varchar(255) DEFAULT NULL,
  `ENTERED_GRADE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `GRADABLE_OBJECT_ID` (`GRADABLE_OBJECT_ID`,`STUDENT_ID`),
  KEY `FK46ACF7526F98CFF` (`GRADABLE_OBJECT_ID`),
  KEY `GB_GRADE_RECORD_O_T_IDX` (`OBJECT_TYPE_ID`),
  KEY `GB_GRADE_RECORD_STUDENT_ID_IDX` (`STUDENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_GRADE_RECORD_T`
--

LOCK TABLES `GB_GRADE_RECORD_T` WRITE;
/*!40000 ALTER TABLE `GB_GRADE_RECORD_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `GB_GRADE_RECORD_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_GRADE_TO_PERCENT_MAPPING_T`
--

DROP TABLE IF EXISTS `GB_GRADE_TO_PERCENT_MAPPING_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_GRADE_TO_PERCENT_MAPPING_T` (
  `GRADE_MAP_ID` bigint(20) NOT NULL,
  `PERCENT` double DEFAULT NULL,
  `LETTER_GRADE` varchar(255) NOT NULL,
  PRIMARY KEY (`GRADE_MAP_ID`,`LETTER_GRADE`),
  KEY `FKCDEA021162B659F1` (`GRADE_MAP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_GRADE_TO_PERCENT_MAPPING_T`
--

LOCK TABLES `GB_GRADE_TO_PERCENT_MAPPING_T` WRITE;
/*!40000 ALTER TABLE `GB_GRADE_TO_PERCENT_MAPPING_T` DISABLE KEYS */;
INSERT INTO `GB_GRADE_TO_PERCENT_MAPPING_T` VALUES (1,60,'D'),(1,0,'F'),(1,90,'A'),(1,80,'B'),(1,70,'C'),(2,63,'D'),(2,0,'F'),(2,95,'A'),(2,83,'B'),(2,73,'C'),(2,67,'D+'),(2,80,'B-'),(2,100,'A+'),(2,87,'B+'),(2,77,'C+'),(2,90,'A-'),(2,70,'C-'),(2,60,'D-'),(3,75,'P'),(3,0,'NP');
/*!40000 ALTER TABLE `GB_GRADE_TO_PERCENT_MAPPING_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_GRADING_EVENT_T`
--

DROP TABLE IF EXISTS `GB_GRADING_EVENT_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_GRADING_EVENT_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `GRADABLE_OBJECT_ID` bigint(20) NOT NULL,
  `GRADER_ID` varchar(255) NOT NULL,
  `STUDENT_ID` varchar(255) NOT NULL,
  `DATE_GRADED` datetime NOT NULL,
  `GRADE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK4C9D99E06F98CFF` (`GRADABLE_OBJECT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_GRADING_EVENT_T`
--

LOCK TABLES `GB_GRADING_EVENT_T` WRITE;
/*!40000 ALTER TABLE `GB_GRADING_EVENT_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `GB_GRADING_EVENT_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_GRADING_SCALE_GRADES_T`
--

DROP TABLE IF EXISTS `GB_GRADING_SCALE_GRADES_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_GRADING_SCALE_GRADES_T` (
  `GRADING_SCALE_ID` bigint(20) NOT NULL,
  `LETTER_GRADE` varchar(255) DEFAULT NULL,
  `GRADE_IDX` int(11) NOT NULL,
  PRIMARY KEY (`GRADING_SCALE_ID`,`GRADE_IDX`),
  KEY `FK5D3F0C95605CD0C5` (`GRADING_SCALE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_GRADING_SCALE_GRADES_T`
--

LOCK TABLES `GB_GRADING_SCALE_GRADES_T` WRITE;
/*!40000 ALTER TABLE `GB_GRADING_SCALE_GRADES_T` DISABLE KEYS */;
INSERT INTO `GB_GRADING_SCALE_GRADES_T` VALUES (1,'A',0),(1,'B',1),(1,'C',2),(1,'D',3),(1,'F',4),(2,'A+',0),(2,'A',1),(2,'A-',2),(2,'B+',3),(2,'B',4),(2,'B-',5),(2,'C+',6),(2,'C',7),(2,'C-',8),(2,'D+',9),(2,'D',10),(2,'D-',11),(2,'F',12),(3,'P',0),(3,'NP',1);
/*!40000 ALTER TABLE `GB_GRADING_SCALE_GRADES_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_GRADING_SCALE_PERCENTS_T`
--

DROP TABLE IF EXISTS `GB_GRADING_SCALE_PERCENTS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_GRADING_SCALE_PERCENTS_T` (
  `GRADING_SCALE_ID` bigint(20) NOT NULL,
  `PERCENT` double DEFAULT NULL,
  `LETTER_GRADE` varchar(255) NOT NULL,
  PRIMARY KEY (`GRADING_SCALE_ID`,`LETTER_GRADE`),
  KEY `FKC98BE467605CD0C5` (`GRADING_SCALE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_GRADING_SCALE_PERCENTS_T`
--

LOCK TABLES `GB_GRADING_SCALE_PERCENTS_T` WRITE;
/*!40000 ALTER TABLE `GB_GRADING_SCALE_PERCENTS_T` DISABLE KEYS */;
INSERT INTO `GB_GRADING_SCALE_PERCENTS_T` VALUES (1,60,'D'),(1,0,'F'),(1,90,'A'),(1,80,'B'),(1,70,'C'),(2,63,'D'),(2,0,'F'),(2,95,'A'),(2,83,'B'),(2,73,'C'),(2,67,'D+'),(2,80,'B-'),(2,100,'A+'),(2,87,'B+'),(2,90,'A-'),(2,77,'C+'),(2,70,'C-'),(2,60,'D-'),(3,75,'P'),(3,0,'NP');
/*!40000 ALTER TABLE `GB_GRADING_SCALE_PERCENTS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_GRADING_SCALE_T`
--

DROP TABLE IF EXISTS `GB_GRADING_SCALE_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_GRADING_SCALE_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `OBJECT_TYPE_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `SCALE_UID` varchar(255) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `UNAVAILABLE` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SCALE_UID` (`SCALE_UID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_GRADING_SCALE_T`
--

LOCK TABLES `GB_GRADING_SCALE_T` WRITE;
/*!40000 ALTER TABLE `GB_GRADING_SCALE_T` DISABLE KEYS */;
INSERT INTO `GB_GRADING_SCALE_T` VALUES (1,0,0,'LetterGradeMapping','Letter Grades','\0'),(2,0,0,'LetterGradePlusMinusMapping','Letter Grades with +/-','\0'),(3,0,0,'PassNotPassMapping','Pass / Not Pass','\0');
/*!40000 ALTER TABLE `GB_GRADING_SCALE_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_LETTERGRADE_MAPPING`
--

DROP TABLE IF EXISTS `GB_LETTERGRADE_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_LETTERGRADE_MAPPING` (
  `LG_MAPPING_ID` bigint(20) NOT NULL,
  `value` double DEFAULT NULL,
  `grade` varchar(255) NOT NULL,
  PRIMARY KEY (`LG_MAPPING_ID`,`grade`),
  KEY `FKC8CDDC5C626E35B6` (`LG_MAPPING_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_LETTERGRADE_MAPPING`
--

LOCK TABLES `GB_LETTERGRADE_MAPPING` WRITE;
/*!40000 ALTER TABLE `GB_LETTERGRADE_MAPPING` DISABLE KEYS */;
INSERT INTO `GB_LETTERGRADE_MAPPING` VALUES (1,63,'D'),(1,0,'F'),(1,95,'A'),(1,83,'B'),(1,73,'C'),(1,67,'D+'),(1,80,'B-'),(1,100,'A+'),(1,87,'B+'),(1,90,'A-'),(1,77,'C+'),(1,70,'C-'),(1,60,'D-');
/*!40000 ALTER TABLE `GB_LETTERGRADE_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_LETTERGRADE_PERCENT_MAPPING`
--

DROP TABLE IF EXISTS `GB_LETTERGRADE_PERCENT_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_LETTERGRADE_PERCENT_MAPPING` (
  `LGP_MAPPING_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `MAPPING_TYPE` int(11) NOT NULL,
  `GRADEBOOK_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`LGP_MAPPING_ID`),
  UNIQUE KEY `MAPPING_TYPE` (`MAPPING_TYPE`,`GRADEBOOK_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_LETTERGRADE_PERCENT_MAPPING`
--

LOCK TABLES `GB_LETTERGRADE_PERCENT_MAPPING` WRITE;
/*!40000 ALTER TABLE `GB_LETTERGRADE_PERCENT_MAPPING` DISABLE KEYS */;
INSERT INTO `GB_LETTERGRADE_PERCENT_MAPPING` VALUES (1,1,1,NULL);
/*!40000 ALTER TABLE `GB_LETTERGRADE_PERCENT_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_PERMISSION_T`
--

DROP TABLE IF EXISTS `GB_PERMISSION_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_PERMISSION_T` (
  `GB_PERMISSION_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `GRADEBOOK_ID` bigint(20) NOT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `FUNCTION_NAME` varchar(255) NOT NULL,
  `CATEGORY_ID` bigint(20) DEFAULT NULL,
  `GROUP_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`GB_PERMISSION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_PERMISSION_T`
--

LOCK TABLES `GB_PERMISSION_T` WRITE;
/*!40000 ALTER TABLE `GB_PERMISSION_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `GB_PERMISSION_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_PROPERTY_T`
--

DROP TABLE IF EXISTS `GB_PROPERTY_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_PROPERTY_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_PROPERTY_T`
--

LOCK TABLES `GB_PROPERTY_T` WRITE;
/*!40000 ALTER TABLE `GB_PROPERTY_T` DISABLE KEYS */;
INSERT INTO `GB_PROPERTY_T` VALUES (1,0,'uidOfDefaultGradingScale','LetterGradePlusMinusMapping');
/*!40000 ALTER TABLE `GB_PROPERTY_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GB_SPREADSHEET_T`
--

DROP TABLE IF EXISTS `GB_SPREADSHEET_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GB_SPREADSHEET_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `CREATOR` varchar(255) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `CONTENT` mediumtext NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `GRADEBOOK_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKB2FE801D325D7986` (`GRADEBOOK_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GB_SPREADSHEET_T`
--

LOCK TABLES `GB_SPREADSHEET_T` WRITE;
/*!40000 ALTER TABLE `GB_SPREADSHEET_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `GB_SPREADSHEET_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `JCRV_BINVAL`
--

DROP TABLE IF EXISTS `JCRV_BINVAL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JCRV_BINVAL` (
  `BINVAL_ID` varchar(64) NOT NULL,
  `BINVAL_DATA` longblob NOT NULL,
  UNIQUE KEY `JCRV_BINVAL_IDX` (`BINVAL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `JCRV_BINVAL`
--

LOCK TABLES `JCRV_BINVAL` WRITE;
/*!40000 ALTER TABLE `JCRV_BINVAL` DISABLE KEYS */;
/*!40000 ALTER TABLE `JCRV_BINVAL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `JCRV_BUNDLE`
--

DROP TABLE IF EXISTS `JCRV_BUNDLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JCRV_BUNDLE` (
  `NODE_ID` varbinary(16) NOT NULL,
  `BUNDLE_DATA` longblob NOT NULL,
  UNIQUE KEY `JCRV_BUNDLE_IDX` (`NODE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `JCRV_BUNDLE`
--

LOCK TABLES `JCRV_BUNDLE` WRITE;
/*!40000 ALTER TABLE `JCRV_BUNDLE` DISABLE KEYS */;
INSERT INTO `JCRV_BUNDLE` VALUES ('ﬁ≠æÔ˙Œ∫æ ˛∫æ ˛∫æ','\0\0\0\0\0ﬁ≠æÔ ˛∫æ ˛∫æ ˛∫æ\0\n1994195451ˇˇˇˇˇˇˇˇ\0\0\0\0');
/*!40000 ALTER TABLE `JCRV_BUNDLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `JCRV_NAMES`
--

DROP TABLE IF EXISTS `JCRV_NAMES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JCRV_NAMES` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `JCRV_NAMES`
--

LOCK TABLES `JCRV_NAMES` WRITE;
/*!40000 ALTER TABLE `JCRV_NAMES` DISABLE KEYS */;
INSERT INTO `JCRV_NAMES` VALUES (1,'versionStorage');
/*!40000 ALTER TABLE `JCRV_NAMES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `JCRV_REFS`
--

DROP TABLE IF EXISTS `JCRV_REFS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JCRV_REFS` (
  `NODE_ID` varbinary(16) NOT NULL,
  `REFS_DATA` longblob NOT NULL,
  UNIQUE KEY `JCRV_REFS_IDX` (`NODE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `JCRV_REFS`
--

LOCK TABLES `JCRV_REFS` WRITE;
/*!40000 ALTER TABLE `JCRV_REFS` DISABLE KEYS */;
/*!40000 ALTER TABLE `JCRV_REFS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `JCR_DEFAULT_BINVAL`
--

DROP TABLE IF EXISTS `JCR_DEFAULT_BINVAL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JCR_DEFAULT_BINVAL` (
  `BINVAL_ID` varchar(64) NOT NULL,
  `BINVAL_DATA` longblob NOT NULL,
  UNIQUE KEY `JCR_DEFAULT_BINVAL_IDX` (`BINVAL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `JCR_DEFAULT_BINVAL`
--

LOCK TABLES `JCR_DEFAULT_BINVAL` WRITE;
/*!40000 ALTER TABLE `JCR_DEFAULT_BINVAL` DISABLE KEYS */;
/*!40000 ALTER TABLE `JCR_DEFAULT_BINVAL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `JCR_DEFAULT_BUNDLE`
--

DROP TABLE IF EXISTS `JCR_DEFAULT_BUNDLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JCR_DEFAULT_BUNDLE` (
  `NODE_ID` varbinary(16) NOT NULL,
  `BUNDLE_DATA` longblob NOT NULL,
  UNIQUE KEY `JCR_DEFAULT_BUNDLE_IDX` (`NODE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `JCR_DEFAULT_BUNDLE`
--

LOCK TABLES `JCR_DEFAULT_BUNDLE` WRITE;
/*!40000 ALTER TABLE `JCR_DEFAULT_BUNDLE` DISABLE KEYS */;
INSERT INTO `JCR_DEFAULT_BUNDLE` VALUES (' ˛∫æ ˛∫æ ˛∫æ ˛∫æ','\0\0\0\0\0\0\0-1537436024ˇˇˇˇˇˇˇˇ\0ﬁ≠æÔ ˛∫æ ˛∫æ ˛∫æ\0\0\0\0systemtÔ˝J∑Ñ+¡\ZÚ‰Ä\0\0\0\0testdata\0\0'),('ﬁ≠æÔ ˛∫æ ˛∫æ ˛∫æ','\0\0\0\0\0 ˛∫æ ˛∫æ ˛∫æ ˛∫æ\0-1971945898ˇˇˇˇˇˇˇˇ\0ﬁ≠æÔ˙Œ∫æ ˛∫æ ˛∫æ\0\0\0\0versionStorageﬁ≠æÔ ˛ ˛ ˛∫æ ˛∫æ\0\0\0\0	nodeTypes\0\0\0'),('tÔ˝J∑Ñ+¡\ZÚ‰Ä','\0\0\0\0\0 ˛∫æ ˛∫æ ˛∫æ ˛∫æ\0-1603354723ˇˇˇˇˇˇˇˇ\0\0\0\0');
/*!40000 ALTER TABLE `JCR_DEFAULT_BUNDLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `JCR_DEFAULT_NAMES`
--

DROP TABLE IF EXISTS `JCR_DEFAULT_NAMES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JCR_DEFAULT_NAMES` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `JCR_DEFAULT_NAMES`
--

LOCK TABLES `JCR_DEFAULT_NAMES` WRITE;
/*!40000 ALTER TABLE `JCR_DEFAULT_NAMES` DISABLE KEYS */;
INSERT INTO `JCR_DEFAULT_NAMES` VALUES (1,'root'),(2,'system'),(3,'unstructured');
/*!40000 ALTER TABLE `JCR_DEFAULT_NAMES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `JCR_DEFAULT_REFS`
--

DROP TABLE IF EXISTS `JCR_DEFAULT_REFS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JCR_DEFAULT_REFS` (
  `NODE_ID` varbinary(16) NOT NULL,
  `REFS_DATA` longblob NOT NULL,
  UNIQUE KEY `JCR_DEFAULT_REFS_IDX` (`NODE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `JCR_DEFAULT_REFS`
--

LOCK TABLES `JCR_DEFAULT_REFS` WRITE;
/*!40000 ALTER TABLE `JCR_DEFAULT_REFS` DISABLE KEYS */;
/*!40000 ALTER TABLE `JCR_DEFAULT_REFS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MAILARCHIVE_CHANNEL`
--

DROP TABLE IF EXISTS `MAILARCHIVE_CHANNEL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MAILARCHIVE_CHANNEL` (
  `CHANNEL_ID` varchar(99) NOT NULL,
  `NEXT_ID` int(11) DEFAULT NULL,
  `XML` longtext,
  UNIQUE KEY `MAILARCHIVE_CHANNEL_INDEX` (`CHANNEL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MAILARCHIVE_CHANNEL`
--

LOCK TABLES `MAILARCHIVE_CHANNEL` WRITE;
/*!40000 ALTER TABLE `MAILARCHIVE_CHANNEL` DISABLE KEYS */;
INSERT INTO `MAILARCHIVE_CHANNEL` VALUES ('/mailarchive/channel/!site/postmaster',1,'<?xml version=\"1.0\" encoding=\"UTF-8\"?> <channel context=\"!site\" id=\"postmaster\" next-message-id=\"1\"> <properties/> </channel> '),('/mailarchive/channel/PortfolioAdmin/main',0,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<channel context=\"PortfolioAdmin\" id=\"main\"><properties><property enc=\"BASE64\" name=\"CHEF:channel-enabled\" value=\"dHJ1ZQ==\"/></properties></channel>');
/*!40000 ALTER TABLE `MAILARCHIVE_CHANNEL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MAILARCHIVE_MESSAGE`
--

DROP TABLE IF EXISTS `MAILARCHIVE_MESSAGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MAILARCHIVE_MESSAGE` (
  `CHANNEL_ID` varchar(99) NOT NULL,
  `MESSAGE_ID` varchar(36) NOT NULL,
  `DRAFT` char(1) DEFAULT NULL,
  `PUBVIEW` char(1) DEFAULT NULL,
  `OWNER` varchar(99) DEFAULT NULL,
  `MESSAGE_DATE` datetime NOT NULL,
  `XML` longtext,
  `SUBJECT` varchar(255) DEFAULT NULL,
  `BODY` longtext,
  PRIMARY KEY (`CHANNEL_ID`,`MESSAGE_ID`),
  KEY `IE_MAILARC_MSG_CHAN` (`CHANNEL_ID`),
  KEY `IE_MAILARC_MSG_ATTRIB` (`DRAFT`,`PUBVIEW`,`OWNER`),
  KEY `IE_MAILARC_MSG_DATE` (`MESSAGE_DATE`),
  KEY `IE_MAILARC_MSG_DATE_DESC` (`MESSAGE_DATE`),
  KEY `MAILARC_MSG_CDD` (`CHANNEL_ID`,`MESSAGE_DATE`,`DRAFT`),
  KEY `IE_MAILARC_SUBJECT` (`SUBJECT`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MAILARCHIVE_MESSAGE`
--

LOCK TABLES `MAILARCHIVE_MESSAGE` WRITE;
/*!40000 ALTER TABLE `MAILARCHIVE_MESSAGE` DISABLE KEYS */;
/*!40000 ALTER TABLE `MAILARCHIVE_MESSAGE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_AREA_T`
--

DROP TABLE IF EXISTS `MFR_AREA_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_AREA_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `UUID` varchar(36) NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(36) NOT NULL,
  `MODIFIED` datetime NOT NULL,
  `MODIFIED_BY` varchar(36) NOT NULL,
  `CONTEXT_ID` varchar(255) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `HIDDEN` bit(1) NOT NULL,
  `TYPE_UUID` varchar(36) NOT NULL,
  `ENABLED` bit(1) NOT NULL,
  `SENDEMAILOUT` bit(1) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `MODERATED` bit(1) NOT NULL,
  `POST_FIRST` bit(1) NOT NULL,
  `AUTO_MARK_THREADS_READ` bit(1) NOT NULL,
  `AVAILABILITY_RESTRICTED` bit(1) NOT NULL DEFAULT b'0',
  `AVAILABILITY` bit(1) NOT NULL DEFAULT b'1',
  `OPEN_DATE` datetime DEFAULT NULL,
  `CLOSE_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CONTEXT_ID` (`CONTEXT_ID`,`TYPE_UUID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_AREA_T`
--

LOCK TABLES `MFR_AREA_T` WRITE;
/*!40000 ALTER TABLE `MFR_AREA_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_AREA_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_ATTACHMENT_T`
--

DROP TABLE IF EXISTS `MFR_ATTACHMENT_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_ATTACHMENT_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `UUID` varchar(36) NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(255) NOT NULL,
  `MODIFIED` datetime NOT NULL,
  `MODIFIED_BY` varchar(255) NOT NULL,
  `ATTACHMENT_ID` varchar(255) NOT NULL,
  `ATTACHMENT_URL` varchar(255) NOT NULL,
  `ATTACHMENT_NAME` varchar(255) NOT NULL,
  `ATTACHMENT_SIZE` varchar(255) NOT NULL,
  `ATTACHMENT_TYPE` varchar(255) NOT NULL,
  `m_surrogateKey` bigint(20) DEFAULT NULL,
  `of_surrogateKey` bigint(20) DEFAULT NULL,
  `pf_surrogateKey` bigint(20) DEFAULT NULL,
  `t_surrogateKey` bigint(20) DEFAULT NULL,
  `of_urrogateKey` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK7B2D5CDE5B252FAE` (`of_urrogateKey`),
  KEY `FK7B2D5CDE7DEF8466` (`t_surrogateKey`),
  KEY `FK7B2D5CDE74C7E92B` (`of_surrogateKey`),
  KEY `FK7B2D5CDE82FAB29` (`pf_surrogateKey`),
  KEY `FK7B2D5CDE58F99AA5` (`m_surrogateKey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_ATTACHMENT_T`
--

LOCK TABLES `MFR_ATTACHMENT_T` WRITE;
/*!40000 ALTER TABLE `MFR_ATTACHMENT_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_ATTACHMENT_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_DATE_RESTRICTIONS_T`
--

DROP TABLE IF EXISTS `MFR_DATE_RESTRICTIONS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_DATE_RESTRICTIONS_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `VISIBLE` datetime NOT NULL,
  `VISIBLE_POST_ON_SCHEDULE` bit(1) NOT NULL,
  `POSTING_ALLOWED` datetime NOT NULL,
  `PSTNG_ALLWD_PST_ON_SCHD` bit(1) NOT NULL,
  `READ_ONLY` datetime NOT NULL,
  `READ_ONLY_POST_ON_SCHEDULE` bit(1) NOT NULL,
  `HIDDEN` datetime NOT NULL,
  `HIDDEN_POST_ON_SCHEDULE` bit(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_DATE_RESTRICTIONS_T`
--

LOCK TABLES `MFR_DATE_RESTRICTIONS_T` WRITE;
/*!40000 ALTER TABLE `MFR_DATE_RESTRICTIONS_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_DATE_RESTRICTIONS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_EMAIL_NOTIFICATION_T`
--

DROP TABLE IF EXISTS `MFR_EMAIL_NOTIFICATION_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_EMAIL_NOTIFICATION_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `CONTEXT_ID` varchar(255) NOT NULL,
  `NOTIFICATION_LEVEL` varchar(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_EMAIL_NOTIFICATION_T`
--

LOCK TABLES `MFR_EMAIL_NOTIFICATION_T` WRITE;
/*!40000 ALTER TABLE `MFR_EMAIL_NOTIFICATION_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_EMAIL_NOTIFICATION_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_LABEL_T`
--

DROP TABLE IF EXISTS `MFR_LABEL_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_LABEL_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `UUID` varchar(36) NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(36) NOT NULL,
  `MODIFIED` datetime NOT NULL,
  `MODIFIED_BY` varchar(36) NOT NULL,
  `KEY_C` varchar(255) NOT NULL,
  `VALUE_C` varchar(255) NOT NULL,
  `df_surrogateKey` bigint(20) DEFAULT NULL,
  `df_index_col` int(11) DEFAULT NULL,
  `dt_surrogateKey` bigint(20) DEFAULT NULL,
  `dt_index_col` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKC661154329B20882` (`df_surrogateKey`),
  KEY `FKC661154320BD9842` (`dt_surrogateKey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_LABEL_T`
--

LOCK TABLES `MFR_LABEL_T` WRITE;
/*!40000 ALTER TABLE `MFR_LABEL_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_LABEL_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_MEMBERSHIP_ITEM_T`
--

DROP TABLE IF EXISTS `MFR_MEMBERSHIP_ITEM_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_MEMBERSHIP_ITEM_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `UUID` varchar(36) NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(255) NOT NULL,
  `MODIFIED` datetime NOT NULL,
  `MODIFIED_BY` varchar(255) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `TYPE` int(11) NOT NULL,
  `PERMISSION_LEVEL_NAME` varchar(255) NOT NULL,
  `PERMISSION_LEVEL` bigint(20) DEFAULT NULL,
  `t_surrogateKey` bigint(20) DEFAULT NULL,
  `of_surrogateKey` bigint(20) DEFAULT NULL,
  `a_surrogateKey` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PERMISSION_LEVEL` (`PERMISSION_LEVEL`),
  KEY `FKE03761CBA306F94D` (`a_surrogateKey`),
  KEY `FKE03761CB7DEF8466` (`t_surrogateKey`),
  KEY `FKE03761CB74C7E92B` (`of_surrogateKey`),
  KEY `FKE03761CB88085F8E` (`PERMISSION_LEVEL`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_MEMBERSHIP_ITEM_T`
--

LOCK TABLES `MFR_MEMBERSHIP_ITEM_T` WRITE;
/*!40000 ALTER TABLE `MFR_MEMBERSHIP_ITEM_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_MEMBERSHIP_ITEM_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_MESSAGE_T`
--

DROP TABLE IF EXISTS `MFR_MESSAGE_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_MESSAGE_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MESSAGE_DTYPE` varchar(2) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `UUID` varchar(36) NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(36) NOT NULL,
  `MODIFIED` datetime NOT NULL,
  `MODIFIED_BY` varchar(36) NOT NULL,
  `TITLE` varchar(255) NOT NULL,
  `BODY` text,
  `AUTHOR` varchar(255) NOT NULL,
  `HAS_ATTACHMENTS` bit(1) NOT NULL,
  `GRADEASSIGNMENTNAME` varchar(255) DEFAULT NULL,
  `NUM_READERS` int(11) DEFAULT NULL,
  `LABEL` varchar(255) DEFAULT NULL,
  `IN_REPLY_TO` bigint(20) DEFAULT NULL,
  `THREADID` bigint(20) DEFAULT NULL,
  `LASTTHREADATE` datetime DEFAULT NULL,
  `LASTTHREAPOST` bigint(20) DEFAULT NULL,
  `TYPE_UUID` varchar(36) NOT NULL,
  `APPROVED` bit(1) DEFAULT NULL,
  `DRAFT` bit(1) NOT NULL,
  `DELETED` bit(1) NOT NULL,
  `surrogateKey` bigint(20) DEFAULT NULL,
  `EXTERNAL_EMAIL` bit(1) DEFAULT NULL,
  `EXTERNAL_EMAIL_ADDRESS` varchar(255) DEFAULT NULL,
  `RECIPIENTS_AS_TEXT` text,
  `RECIPIENTS_AS_TEXT_BCC` text,
  PRIMARY KEY (`ID`),
  KEY `FK80C1A316A2D0BE7B` (`surrogateKey`),
  KEY `FK80C1A31650339D56` (`IN_REPLY_TO`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_MESSAGE_T`
--

LOCK TABLES `MFR_MESSAGE_T` WRITE;
/*!40000 ALTER TABLE `MFR_MESSAGE_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_MESSAGE_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_OPEN_FORUM_T`
--

DROP TABLE IF EXISTS `MFR_OPEN_FORUM_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_OPEN_FORUM_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FORUM_DTYPE` varchar(2) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `UUID` varchar(36) NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(36) NOT NULL,
  `MODIFIED` datetime NOT NULL,
  `MODIFIED_BY` varchar(36) NOT NULL,
  `DEFAULTASSIGNNAME` varchar(255) DEFAULT NULL,
  `TITLE` varchar(255) NOT NULL,
  `SHORT_DESCRIPTION` varchar(255) DEFAULT NULL,
  `EXTENDED_DESCRIPTION` longtext,
  `TYPE_UUID` varchar(36) NOT NULL,
  `SORT_INDEX` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `DRAFT` bit(1) DEFAULT NULL,
  `AVAILABILITY_RESTRICTED` bit(1) NOT NULL DEFAULT b'0',
  `AVAILABILITY` bit(1) NOT NULL DEFAULT b'1',
  `OPEN_DATE` datetime DEFAULT NULL,
  `CLOSE_DATE` datetime DEFAULT NULL,
  `surrogateKey` bigint(20) DEFAULT NULL,
  `MODERATED` bit(1) NOT NULL,
  `AUTO_MARK_THREADS_READ` bit(1) NOT NULL,
  `POST_FIRST` bit(1) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKC17608478B5E2A2F` (`surrogateKey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_OPEN_FORUM_T`
--

LOCK TABLES `MFR_OPEN_FORUM_T` WRITE;
/*!40000 ALTER TABLE `MFR_OPEN_FORUM_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_OPEN_FORUM_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_PERMISSION_LEVEL_T`
--

DROP TABLE IF EXISTS `MFR_PERMISSION_LEVEL_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_PERMISSION_LEVEL_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `UUID` varchar(36) NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(255) NOT NULL,
  `MODIFIED` datetime NOT NULL,
  `MODIFIED_BY` varchar(255) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `TYPE_UUID` varchar(36) NOT NULL,
  `CHANGE_SETTINGS` bit(1) NOT NULL,
  `DELETE_ANY` bit(1) NOT NULL,
  `DELETE_OWN` bit(1) NOT NULL,
  `MARK_AS_READ` bit(1) NOT NULL,
  `MOVE_POSTING` bit(1) NOT NULL,
  `NEW_FORUM` bit(1) NOT NULL,
  `NEW_RESPONSE` bit(1) NOT NULL,
  `NEW_RESPONSE_TO_RESPONSE` bit(1) NOT NULL,
  `NEW_TOPIC` bit(1) NOT NULL,
  `POST_TO_GRADEBOOK` bit(1) NOT NULL,
  `X_READ` bit(1) NOT NULL,
  `REVISE_ANY` bit(1) NOT NULL,
  `REVISE_OWN` bit(1) NOT NULL,
  `MODERATE_POSTINGS` bit(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_PERMISSION_LEVEL_T`
--

LOCK TABLES `MFR_PERMISSION_LEVEL_T` WRITE;
/*!40000 ALTER TABLE `MFR_PERMISSION_LEVEL_T` DISABLE KEYS */;
INSERT INTO `MFR_PERMISSION_LEVEL_T` VALUES (1,0,'3336f7e0-112d-4443-9640-9ac5ee0cb246','2011-04-13 14:59:28','test-user','2011-04-13 14:59:28','test-user','Owner','6542f08e-68d3-429a-9dc8-1d32666f265b','','','\0','','','','','','','','','','\0',''),(2,0,'aca47bbc-4e3c-4cbb-8ab6-c0664e42bedf','2011-04-13 14:59:28','test-user','2011-04-13 14:59:28','test-user','Author','73201531-6fbc-446a-abc0-9f0bdf83c01f','','\0','','','','','','','','','','\0','','\0'),(3,0,'fa131148-4f00-47db-8dd6-1d2f8b54a60d','2011-04-13 14:59:28','test-user','2011-04-13 14:59:28','test-user','Contributor','1ca2d020-d95a-469d-bb50-40531fb6364b','\0','\0','\0','','\0','\0','','','\0','\0','','\0','\0','\0'),(4,0,'1d3d950a-ea3e-4f61-9be9-7f22c5fbb128','2011-04-13 14:59:28','test-user','2011-04-13 14:59:28','test-user','Reviewer','b9865f7f-c1c8-467f-94f5-9e5f80ed9913','\0','\0','\0','','\0','\0','\0','\0','\0','\0','','\0','\0','\0'),(5,0,'87d09896-5230-4b7e-8462-a380f8f16ac9','2011-04-13 14:59:28','test-user','2011-04-13 14:59:28','test-user','Nonediting Author','22e7796d-f9e1-43bd-9f41-b9fcebaf6894','','\0','\0','','\0','','','','','','','\0','','\0'),(6,0,'609e7847-6764-4440-a626-82f7dd507dfa','2011-04-13 14:59:28','test-user','2011-04-13 14:59:28','test-user','None','8b9402a8-0538-486e-bb2f-7e3f9ba051aa','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0');
/*!40000 ALTER TABLE `MFR_PERMISSION_LEVEL_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_PRIVATE_FORUM_T`
--

DROP TABLE IF EXISTS `MFR_PRIVATE_FORUM_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_PRIVATE_FORUM_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `UUID` varchar(36) NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(36) NOT NULL,
  `MODIFIED` datetime NOT NULL,
  `MODIFIED_BY` varchar(36) NOT NULL,
  `TITLE` varchar(255) NOT NULL,
  `SHORT_DESCRIPTION` varchar(255) DEFAULT NULL,
  `EXTENDED_DESCRIPTION` longtext,
  `TYPE_UUID` varchar(36) NOT NULL,
  `SORT_INDEX` int(11) NOT NULL,
  `OWNER` varchar(255) NOT NULL,
  `AUTO_FORWARD` bit(1) DEFAULT NULL,
  `AUTO_FORWARD_EMAIL` varchar(255) DEFAULT NULL,
  `PREVIEW_PANE_ENABLED` bit(1) DEFAULT NULL,
  `surrogateKey` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `OWNER` (`OWNER`,`surrogateKey`),
  KEY `FKA9EE57548B5E2A2F` (`surrogateKey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_PRIVATE_FORUM_T`
--

LOCK TABLES `MFR_PRIVATE_FORUM_T` WRITE;
/*!40000 ALTER TABLE `MFR_PRIVATE_FORUM_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_PRIVATE_FORUM_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_PVT_MSG_USR_T`
--

DROP TABLE IF EXISTS `MFR_PVT_MSG_USR_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_PVT_MSG_USR_T` (
  `messageSurrogateKey` bigint(20) NOT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `TYPE_UUID` varchar(255) NOT NULL,
  `CONTEXT_ID` varchar(255) NOT NULL,
  `READ_STATUS` bit(1) NOT NULL,
  `BCC` bit(1) NOT NULL DEFAULT b'0',
  `user_index_col` int(11) NOT NULL,
  PRIMARY KEY (`messageSurrogateKey`,`user_index_col`),
  KEY `FKC4DE0E1473D286ED` (`messageSurrogateKey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_PVT_MSG_USR_T`
--

LOCK TABLES `MFR_PVT_MSG_USR_T` WRITE;
/*!40000 ALTER TABLE `MFR_PVT_MSG_USR_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_PVT_MSG_USR_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_SYNOPTIC_ITEM`
--

DROP TABLE IF EXISTS `MFR_SYNOPTIC_ITEM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_SYNOPTIC_ITEM` (
  `SYNOPTIC_ITEM_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `USER_ID` varchar(36) NOT NULL,
  `SITE_ID` varchar(99) NOT NULL,
  `SITE_TITLE` varchar(255) DEFAULT NULL,
  `NEW_MESSAGES_COUNT` int(11) DEFAULT NULL,
  `MESSAGES_LAST_VISIT_DT` datetime DEFAULT NULL,
  `NEW_FORUM_COUNT` int(11) DEFAULT NULL,
  `FORUM_LAST_VISIT_DT` datetime DEFAULT NULL,
  `HIDE_ITEM` bit(1) DEFAULT NULL,
  PRIMARY KEY (`SYNOPTIC_ITEM_ID`),
  UNIQUE KEY `USER_ID` (`USER_ID`,`SITE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_SYNOPTIC_ITEM`
--

LOCK TABLES `MFR_SYNOPTIC_ITEM` WRITE;
/*!40000 ALTER TABLE `MFR_SYNOPTIC_ITEM` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_SYNOPTIC_ITEM` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_TOPIC_T`
--

DROP TABLE IF EXISTS `MFR_TOPIC_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_TOPIC_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TOPIC_DTYPE` varchar(2) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `UUID` varchar(36) NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(36) NOT NULL,
  `MODIFIED` datetime NOT NULL,
  `MODIFIED_BY` varchar(36) NOT NULL,
  `DEFAULTASSIGNNAME` varchar(255) DEFAULT NULL,
  `TITLE` varchar(255) NOT NULL,
  `SHORT_DESCRIPTION` varchar(255) DEFAULT NULL,
  `EXTENDED_DESCRIPTION` longtext,
  `MODERATED` bit(1) NOT NULL,
  `POST_FIRST` bit(1) NOT NULL,
  `AUTO_MARK_THREADS_READ` bit(1) NOT NULL,
  `MUTABLE` bit(1) NOT NULL,
  `SORT_INDEX` int(11) NOT NULL,
  `TYPE_UUID` varchar(36) NOT NULL,
  `AVAILABILITY_RESTRICTED` bit(1) NOT NULL DEFAULT b'0',
  `AVAILABILITY` bit(1) NOT NULL DEFAULT b'1',
  `OPEN_DATE` datetime DEFAULT NULL,
  `CLOSE_DATE` datetime DEFAULT NULL,
  `of_surrogateKey` bigint(20) DEFAULT NULL,
  `pf_surrogateKey` bigint(20) DEFAULT NULL,
  `USER_ID` varchar(255) DEFAULT NULL,
  `CONTEXT_ID` varchar(36) DEFAULT NULL,
  `pt_surrogateKey` bigint(20) DEFAULT NULL,
  `LOCKED` bit(1) DEFAULT NULL,
  `DRAFT` bit(1) DEFAULT NULL,
  `CONFIDENTIAL_RESPONSES` bit(1) DEFAULT NULL,
  `MUST_RESPOND_BEFORE_READING` bit(1) DEFAULT NULL,
  `HOUR_BEFORE_RESPONSES_VISIBLE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK863DC0BEFF3B3AE9` (`pt_surrogateKey`),
  KEY `FK863DC0BE74C7E92B` (`of_surrogateKey`),
  KEY `FK863DC0BE82FAB29` (`pf_surrogateKey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_TOPIC_T`
--

LOCK TABLES `MFR_TOPIC_T` WRITE;
/*!40000 ALTER TABLE `MFR_TOPIC_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_TOPIC_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MFR_UNREAD_STATUS_T`
--

DROP TABLE IF EXISTS `MFR_UNREAD_STATUS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MFR_UNREAD_STATUS_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) NOT NULL,
  `TOPIC_C` bigint(20) NOT NULL,
  `MESSAGE_C` bigint(20) NOT NULL,
  `USER_C` varchar(255) NOT NULL,
  `READ_C` bit(1) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `TOPIC_C` (`TOPIC_C`,`MESSAGE_C`,`USER_C`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MFR_UNREAD_STATUS_T`
--

LOCK TABLES `MFR_UNREAD_STATUS_T` WRITE;
/*!40000 ALTER TABLE `MFR_UNREAD_STATUS_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `MFR_UNREAD_STATUS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `POLL_OPTION`
--

DROP TABLE IF EXISTS `POLL_OPTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `POLL_OPTION` (
  `OPTION_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `OPTION_POLL_ID` bigint(20) DEFAULT NULL,
  `OPTION_TEXT` text,
  `OPTION_UUID` varchar(255) DEFAULT NULL,
  `DELETED` bit(1) DEFAULT NULL,
  PRIMARY KEY (`OPTION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `POLL_OPTION`
--

LOCK TABLES `POLL_OPTION` WRITE;
/*!40000 ALTER TABLE `POLL_OPTION` DISABLE KEYS */;
/*!40000 ALTER TABLE `POLL_OPTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `POLL_POLL`
--

DROP TABLE IF EXISTS `POLL_POLL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `POLL_POLL` (
  `POLL_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `POLL_OWNER` varchar(255) DEFAULT NULL,
  `POLL_SITE_ID` varchar(255) DEFAULT NULL,
  `POLL_DETAILS` text,
  `POLL_CREATION_DATE` datetime DEFAULT NULL,
  `POLL_TEXT` text,
  `POLL_VOTE_OPEN` datetime DEFAULT NULL,
  `POLL_VOTE_CLOSE` datetime DEFAULT NULL,
  `POLL_MIN_OPTIONS` int(11) DEFAULT NULL,
  `POLL_MAX_OPTIONS` int(11) DEFAULT NULL,
  `POLL_DISPLAY_RESULT` varchar(255) DEFAULT NULL,
  `POLL_LIMIT_VOTE` bit(1) DEFAULT NULL,
  `POLL_UUID` varchar(255) DEFAULT NULL,
  `POLL_IS_PUBLIC` bit(1) DEFAULT NULL,
  PRIMARY KEY (`POLL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `POLL_POLL`
--

LOCK TABLES `POLL_POLL` WRITE;
/*!40000 ALTER TABLE `POLL_POLL` DISABLE KEYS */;
/*!40000 ALTER TABLE `POLL_POLL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `POLL_VOTE`
--

DROP TABLE IF EXISTS `POLL_VOTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `POLL_VOTE` (
  `VOTE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(255) DEFAULT NULL,
  `VOTE_IP` varchar(255) DEFAULT NULL,
  `VOTE_DATE` datetime DEFAULT NULL,
  `VOTE_POLL_ID` bigint(20) DEFAULT NULL,
  `VOTE_OPTION` bigint(20) DEFAULT NULL,
  `VOTE_SUBMISSION_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`VOTE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `POLL_VOTE`
--

LOCK TABLES `POLL_VOTE` WRITE;
/*!40000 ALTER TABLE `POLL_VOTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `POLL_VOTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_COMPANY_PROFILES_T`
--

DROP TABLE IF EXISTS `PROFILE_COMPANY_PROFILES_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_COMPANY_PROFILES_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_UUID` varchar(99) NOT NULL,
  `COMPANY_NAME` varchar(255) DEFAULT NULL,
  `COMPANY_DESCRIPTION` text,
  `COMPANY_WEB_ADDRESS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_COMPANY_PROFILES_T`
--

LOCK TABLES `PROFILE_COMPANY_PROFILES_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_COMPANY_PROFILES_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_COMPANY_PROFILES_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_EXTERNAL_INTEGRATION_T`
--

DROP TABLE IF EXISTS `PROFILE_EXTERNAL_INTEGRATION_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_EXTERNAL_INTEGRATION_T` (
  `USER_UUID` varchar(99) NOT NULL,
  `TWITTER_TOKEN` varchar(255) DEFAULT NULL,
  `TWITTER_SECRET` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`USER_UUID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_EXTERNAL_INTEGRATION_T`
--

LOCK TABLES `PROFILE_EXTERNAL_INTEGRATION_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_EXTERNAL_INTEGRATION_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_EXTERNAL_INTEGRATION_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_FRIENDS_T`
--

DROP TABLE IF EXISTS `PROFILE_FRIENDS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_FRIENDS_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_UUID` varchar(99) NOT NULL,
  `FRIEND_UUID` varchar(99) NOT NULL,
  `RELATIONSHIP` int(11) NOT NULL,
  `REQUESTED_DATE` datetime NOT NULL,
  `CONFIRMED` bit(1) NOT NULL,
  `CONFIRMED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_FRIENDS_T`
--

LOCK TABLES `PROFILE_FRIENDS_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_FRIENDS_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_FRIENDS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_GALLERY_IMAGES_T`
--

DROP TABLE IF EXISTS `PROFILE_GALLERY_IMAGES_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_GALLERY_IMAGES_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_UUID` varchar(99) NOT NULL,
  `RESOURCE_MAIN` text NOT NULL,
  `RESOURCE_THUMB` text NOT NULL,
  `DISPLAY_NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_GALLERY_IMAGES_T`
--

LOCK TABLES `PROFILE_GALLERY_IMAGES_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_GALLERY_IMAGES_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_GALLERY_IMAGES_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_IMAGES_EXTERNAL_T`
--

DROP TABLE IF EXISTS `PROFILE_IMAGES_EXTERNAL_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_IMAGES_EXTERNAL_T` (
  `USER_UUID` varchar(99) NOT NULL,
  `URL_MAIN` text NOT NULL,
  `URL_THUMB` text,
  PRIMARY KEY (`USER_UUID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_IMAGES_EXTERNAL_T`
--

LOCK TABLES `PROFILE_IMAGES_EXTERNAL_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_IMAGES_EXTERNAL_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_IMAGES_EXTERNAL_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_IMAGES_OFFICIAL_T`
--

DROP TABLE IF EXISTS `PROFILE_IMAGES_OFFICIAL_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_IMAGES_OFFICIAL_T` (
  `USER_UUID` varchar(99) NOT NULL,
  `URL` text NOT NULL,
  PRIMARY KEY (`USER_UUID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_IMAGES_OFFICIAL_T`
--

LOCK TABLES `PROFILE_IMAGES_OFFICIAL_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_IMAGES_OFFICIAL_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_IMAGES_OFFICIAL_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_IMAGES_T`
--

DROP TABLE IF EXISTS `PROFILE_IMAGES_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_IMAGES_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_UUID` varchar(99) NOT NULL,
  `RESOURCE_MAIN` text NOT NULL,
  `RESOURCE_THUMB` text NOT NULL,
  `IS_CURRENT` bit(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_IMAGES_T`
--

LOCK TABLES `PROFILE_IMAGES_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_IMAGES_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_IMAGES_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_KUDOS_T`
--

DROP TABLE IF EXISTS `PROFILE_KUDOS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_KUDOS_T` (
  `USER_UUID` varchar(99) NOT NULL,
  `SCORE` int(11) NOT NULL,
  `PERCENTAGE` decimal(19,2) NOT NULL,
  `DATE_ADDED` datetime NOT NULL,
  PRIMARY KEY (`USER_UUID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_KUDOS_T`
--

LOCK TABLES `PROFILE_KUDOS_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_KUDOS_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_KUDOS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_MESSAGES_T`
--

DROP TABLE IF EXISTS `PROFILE_MESSAGES_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_MESSAGES_T` (
  `ID` varchar(36) NOT NULL,
  `FROM_UUID` varchar(99) NOT NULL,
  `MESSAGE_BODY` text NOT NULL,
  `MESSAGE_THREAD` varchar(36) NOT NULL,
  `DATE_POSTED` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_MESSAGES_T`
--

LOCK TABLES `PROFILE_MESSAGES_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_MESSAGES_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_MESSAGES_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_MESSAGE_PARTICIPANTS_T`
--

DROP TABLE IF EXISTS `PROFILE_MESSAGE_PARTICIPANTS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_MESSAGE_PARTICIPANTS_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MESSAGE_ID` varchar(36) NOT NULL,
  `PARTICIPANT_UUID` varchar(99) NOT NULL,
  `MESSAGE_READ` bit(1) NOT NULL,
  `MESSAGE_DELETED` bit(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_MESSAGE_PARTICIPANTS_T`
--

LOCK TABLES `PROFILE_MESSAGE_PARTICIPANTS_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_MESSAGE_PARTICIPANTS_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_MESSAGE_PARTICIPANTS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_MESSAGE_THREADS_T`
--

DROP TABLE IF EXISTS `PROFILE_MESSAGE_THREADS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_MESSAGE_THREADS_T` (
  `ID` varchar(36) NOT NULL,
  `SUBJECT` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_MESSAGE_THREADS_T`
--

LOCK TABLES `PROFILE_MESSAGE_THREADS_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_MESSAGE_THREADS_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_MESSAGE_THREADS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_PREFERENCES_T`
--

DROP TABLE IF EXISTS `PROFILE_PREFERENCES_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_PREFERENCES_T` (
  `USER_UUID` varchar(99) NOT NULL,
  `EMAIL_REQUEST` bit(1) NOT NULL,
  `EMAIL_CONFIRM` bit(1) NOT NULL,
  `EMAIL_MESSAGE_NEW` bit(1) NOT NULL,
  `EMAIL_MESSAGE_REPLY` bit(1) NOT NULL,
  `USE_OFFICIAL_IMAGE` bit(1) NOT NULL,
  `SHOW_KUDOS` bit(1) NOT NULL,
  `SHOW_GALLERY_FEED` bit(1) NOT NULL,
  `USE_GRAVATAR` bit(1) NOT NULL,
  `EMAIL_WALL_ITEM_NEW` bit(1) NOT NULL,
  PRIMARY KEY (`USER_UUID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_PREFERENCES_T`
--

LOCK TABLES `PROFILE_PREFERENCES_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_PREFERENCES_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_PREFERENCES_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_PRIVACY_T`
--

DROP TABLE IF EXISTS `PROFILE_PRIVACY_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_PRIVACY_T` (
  `USER_UUID` varchar(99) NOT NULL,
  `PROFILE_IMAGE` int(11) NOT NULL,
  `BASIC_INFO` int(11) NOT NULL,
  `CONTACT_INFO` int(11) NOT NULL,
  `BUSINESS_INFO` int(11) NOT NULL,
  `PERSONAL_INFO` int(11) NOT NULL,
  `BIRTH_YEAR` bit(1) NOT NULL,
  `MY_FRIENDS` int(11) NOT NULL,
  `MY_STATUS` int(11) NOT NULL,
  `MY_PICTURES` int(11) NOT NULL,
  `MESSAGES` int(11) NOT NULL,
  `STAFF_INFO` int(11) NOT NULL,
  `STUDENT_INFO` int(11) NOT NULL,
  `SOCIAL_NETWORKING_INFO` int(11) NOT NULL,
  `MY_KUDOS` int(11) NOT NULL,
  `MY_WALL` int(11) NOT NULL,
  PRIMARY KEY (`USER_UUID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_PRIVACY_T`
--

LOCK TABLES `PROFILE_PRIVACY_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_PRIVACY_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_PRIVACY_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_SOCIAL_INFO_T`
--

DROP TABLE IF EXISTS `PROFILE_SOCIAL_INFO_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_SOCIAL_INFO_T` (
  `USER_UUID` varchar(99) NOT NULL,
  `FACEBOOK_URL` varchar(255) DEFAULT NULL,
  `LINKEDIN_URL` varchar(255) DEFAULT NULL,
  `MYSPACE_URL` varchar(255) DEFAULT NULL,
  `SKYPE_USERNAME` varchar(255) DEFAULT NULL,
  `TWITTER_URL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`USER_UUID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_SOCIAL_INFO_T`
--

LOCK TABLES `PROFILE_SOCIAL_INFO_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_SOCIAL_INFO_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_SOCIAL_INFO_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_STATUS_T`
--

DROP TABLE IF EXISTS `PROFILE_STATUS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_STATUS_T` (
  `USER_UUID` varchar(99) NOT NULL,
  `MESSAGE` varchar(255) NOT NULL,
  `DATE_ADDED` datetime NOT NULL,
  PRIMARY KEY (`USER_UUID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_STATUS_T`
--

LOCK TABLES `PROFILE_STATUS_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_STATUS_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_STATUS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_WALL_ITEMS_T`
--

DROP TABLE IF EXISTS `PROFILE_WALL_ITEMS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_WALL_ITEMS_T` (
  `WALL_ITEM_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_UUID` varchar(99) NOT NULL,
  `CREATOR_UUID` varchar(99) NOT NULL,
  `WALL_ITEM_TYPE` int(11) NOT NULL,
  `WALL_ITEM_TEXT` text NOT NULL,
  `WALL_ITEM_DATE` datetime NOT NULL,
  PRIMARY KEY (`WALL_ITEM_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_WALL_ITEMS_T`
--

LOCK TABLES `PROFILE_WALL_ITEMS_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_WALL_ITEMS_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_WALL_ITEMS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFILE_WALL_ITEM_COMMENTS_T`
--

DROP TABLE IF EXISTS `PROFILE_WALL_ITEM_COMMENTS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFILE_WALL_ITEM_COMMENTS_T` (
  `WALL_ITEM_COMMENT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `WALL_ITEM_ID` bigint(20) NOT NULL,
  `CREATOR_UUID` varchar(99) NOT NULL,
  `WALL_ITEM_COMMENT_TEXT` text NOT NULL,
  `WALL_ITEM_COMMENT_DATE` datetime NOT NULL,
  PRIMARY KEY (`WALL_ITEM_COMMENT_ID`),
  KEY `FK32185F67BEE209` (`WALL_ITEM_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFILE_WALL_ITEM_COMMENTS_T`
--

LOCK TABLES `PROFILE_WALL_ITEM_COMMENTS_T` WRITE;
/*!40000 ALTER TABLE `PROFILE_WALL_ITEM_COMMENTS_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFILE_WALL_ITEM_COMMENTS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_BLOB_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_BLOB_TRIGGERS`
--

LOCK TABLES `QRTZ_BLOB_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_BLOB_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_BLOB_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_CALENDARS`
--

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_CALENDARS` (
  `CALENDAR_NAME` varchar(80) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`CALENDAR_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_CALENDARS`
--

LOCK TABLES `QRTZ_CALENDARS` WRITE;
/*!40000 ALTER TABLE `QRTZ_CALENDARS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_CALENDARS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_CRON_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `CRON_EXPRESSION` varchar(80) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_CRON_TRIGGERS`
--

LOCK TABLES `QRTZ_CRON_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_CRON_TRIGGERS` DISABLE KEYS */;
INSERT INTO `QRTZ_CRON_TRIGGERS` VALUES ('Nightly Log Purge Trigger','DEFAULT','0 0 0 * * ? *','America/Indiana/Indianapolis');
/*!40000 ALTER TABLE `QRTZ_CRON_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_FIRED_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `INSTANCE_NAME` varchar(80) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(80) DEFAULT NULL,
  `JOB_GROUP` varchar(80) DEFAULT NULL,
  `IS_STATEFUL` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  `PRIORITY` int(11) NOT NULL,
  PRIMARY KEY (`ENTRY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_FIRED_TRIGGERS`
--

LOCK TABLES `QRTZ_FIRED_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_FIRED_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_FIRED_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_JOB_DETAILS`
--

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `DESCRIPTION` varchar(120) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(128) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `IS_STATEFUL` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`JOB_NAME`,`JOB_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_JOB_DETAILS`
--

LOCK TABLES `QRTZ_JOB_DETAILS` WRITE;
/*!40000 ALTER TABLE `QRTZ_JOB_DETAILS` DISABLE KEYS */;
INSERT INTO `QRTZ_JOB_DETAILS` VALUES ('Event Log Purge','DEFAULT',NULL,'org.sakaiproject.component.app.scheduler.jobs.SpringConfigurableJobBeanWrapper','1','0','0','1','#\n#Wed Apr 13 14:59:25 EDT 2011\norg.sakaiproject.api.app.scheduler.JobBeanWrapper.bean=eventPurgeJob\norg.sakaiproject.api.app.scheduler.JobBeanWrapper.jobType=Event Log Purge\nnumber.days=7\n'),('org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl',NULL,'org.sakaiproject.component.app.scheduler.jobs.ScheduledInvocationRunner','0','0','1','0','#\n#Wed Apr 13 15:06:35 EDT 2011\n');
/*!40000 ALTER TABLE `QRTZ_JOB_DETAILS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_JOB_LISTENERS`
--

DROP TABLE IF EXISTS `QRTZ_JOB_LISTENERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_JOB_LISTENERS` (
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `JOB_LISTENER` varchar(80) NOT NULL,
  PRIMARY KEY (`JOB_NAME`,`JOB_GROUP`,`JOB_LISTENER`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_JOB_LISTENERS`
--

LOCK TABLES `QRTZ_JOB_LISTENERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_JOB_LISTENERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_JOB_LISTENERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_LOCKS`
--

DROP TABLE IF EXISTS `QRTZ_LOCKS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_LOCKS` (
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`LOCK_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_LOCKS`
--

LOCK TABLES `QRTZ_LOCKS` WRITE;
/*!40000 ALTER TABLE `QRTZ_LOCKS` DISABLE KEYS */;
INSERT INTO `QRTZ_LOCKS` VALUES ('CALENDAR_ACCESS'),('JOB_ACCESS'),('MISFIRE_ACCESS'),('STATE_ACCESS'),('TRIGGER_ACCESS');
/*!40000 ALTER TABLE `QRTZ_LOCKS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  PRIMARY KEY (`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

LOCK TABLES `QRTZ_PAUSED_TRIGGER_GRPS` WRITE;
/*!40000 ALTER TABLE `QRTZ_PAUSED_TRIGGER_GRPS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_PAUSED_TRIGGER_GRPS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SCHEDULER_STATE`
--

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `INSTANCE_NAME` varchar(80) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  `RECOVERER` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`INSTANCE_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SCHEDULER_STATE`
--

LOCK TABLES `QRTZ_SCHEDULER_STATE` WRITE;
/*!40000 ALTER TABLE `QRTZ_SCHEDULER_STATE` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_SCHEDULER_STATE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SIMPLE_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(7) NOT NULL,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SIMPLE_TRIGGERS`
--

LOCK TABLES `QRTZ_SIMPLE_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_SIMPLE_TRIGGERS` DISABLE KEYS */;
INSERT INTO `QRTZ_SIMPLE_TRIGGERS` VALUES ('org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl',-1,600000,5);
/*!40000 ALTER TABLE `QRTZ_SIMPLE_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_TRIGGERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `DESCRIPTION` varchar(120) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(80) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `JOB_NAME` (`JOB_NAME`,`JOB_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_TRIGGERS`
--

LOCK TABLES `QRTZ_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_TRIGGERS` DISABLE KEYS */;
INSERT INTO `QRTZ_TRIGGERS` VALUES ('Nightly Log Purge Trigger','DEFAULT','Event Log Purge','DEFAULT','0',NULL,1302753600000,-1,'WAITING','CRON',1302721165000,0,NULL,0,5,''),('org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl','0',NULL,1302725195635,1302724595635,'WAITING','SIMPLE',1302722195635,0,NULL,0,5,'');
/*!40000 ALTER TABLE `QRTZ_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_TRIGGER_LISTENERS`
--

DROP TABLE IF EXISTS `QRTZ_TRIGGER_LISTENERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRTZ_TRIGGER_LISTENERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `TRIGGER_LISTENER` varchar(80) NOT NULL,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_LISTENER`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_TRIGGER_LISTENERS`
--

LOCK TABLES `QRTZ_TRIGGER_LISTENERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_TRIGGER_LISTENERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_TRIGGER_LISTENERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_ALIAS`
--

DROP TABLE IF EXISTS `SAKAI_ALIAS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_ALIAS` (
  `ALIAS_ID` varchar(99) NOT NULL,
  `TARGET` varchar(255) DEFAULT NULL,
  `CREATEDBY` varchar(99) NOT NULL,
  `MODIFIEDBY` varchar(99) NOT NULL,
  `CREATEDON` datetime NOT NULL,
  `MODIFIEDON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ALIAS_ID`),
  KEY `IE_SAKAI_ALIAS_CREATED` (`CREATEDBY`,`CREATEDON`),
  KEY `IE_SAKAI_ALIAS_MODDED` (`MODIFIEDBY`,`MODIFIEDON`),
  KEY `IE_SAKAI_ALIAS_TARGET` (`TARGET`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_ALIAS`
--

LOCK TABLES `SAKAI_ALIAS` WRITE;
/*!40000 ALTER TABLE `SAKAI_ALIAS` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_ALIAS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_ALIAS_PROPERTY`
--

DROP TABLE IF EXISTS `SAKAI_ALIAS_PROPERTY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_ALIAS_PROPERTY` (
  `ALIAS_ID` varchar(99) NOT NULL,
  `NAME` varchar(99) NOT NULL,
  `VALUE` longtext,
  PRIMARY KEY (`ALIAS_ID`,`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_ALIAS_PROPERTY`
--

LOCK TABLES `SAKAI_ALIAS_PROPERTY` WRITE;
/*!40000 ALTER TABLE `SAKAI_ALIAS_PROPERTY` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_ALIAS_PROPERTY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_CLUSTER`
--

DROP TABLE IF EXISTS `SAKAI_CLUSTER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_CLUSTER` (
  `SERVER_ID` varchar(64) NOT NULL DEFAULT '',
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`SERVER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_CLUSTER`
--

LOCK TABLES `SAKAI_CLUSTER` WRITE;
/*!40000 ALTER TABLE `SAKAI_CLUSTER` DISABLE KEYS */;
INSERT INTO `SAKAI_CLUSTER` VALUES ('localhost-1302721559078','2011-04-13 16:00:20');
/*!40000 ALTER TABLE `SAKAI_CLUSTER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_DIGEST`
--

DROP TABLE IF EXISTS `SAKAI_DIGEST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_DIGEST` (
  `DIGEST_ID` varchar(99) NOT NULL,
  `XML` longtext,
  UNIQUE KEY `SAKAI_DIGEST_INDEX` (`DIGEST_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_DIGEST`
--

LOCK TABLES `SAKAI_DIGEST` WRITE;
/*!40000 ALTER TABLE `SAKAI_DIGEST` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_DIGEST` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_EVENT`
--

DROP TABLE IF EXISTS `SAKAI_EVENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_EVENT` (
  `EVENT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EVENT_DATE` datetime DEFAULT NULL,
  `EVENT` varchar(32) DEFAULT NULL,
  `REF` varchar(255) DEFAULT NULL,
  `CONTEXT` varchar(255) DEFAULT NULL,
  `SESSION_ID` varchar(163) DEFAULT NULL,
  `EVENT_CODE` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`EVENT_ID`),
  KEY `IE_SAKAI_EVENT_SESSION_ID` (`SESSION_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=111 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_EVENT`
--

LOCK TABLES `SAKAI_EVENT` WRITE;
/*!40000 ALTER TABLE `SAKAI_EVENT` DISABLE KEYS */;
INSERT INTO `SAKAI_EVENT` VALUES (1,'2011-04-13 14:59:22','server.start','2.9-SNAPSHOT/TRUNK',NULL,'~localhost~?','a'),(2,'2011-04-13 14:59:29','content.new','/content/group/PortfolioAdmin/','PortfolioAdmin','~localhost~admin','m'),(3,'2011-04-13 14:59:29','content.new','/content/group/PortfolioAdmin/system/','PortfolioAdmin','~localhost~admin','m'),(4,'2011-04-13 14:59:29','content.new','/content/group/PortfolioAdmin/system/formCreate.xslt','PortfolioAdmin','~localhost~admin','m'),(5,'2011-04-13 14:59:29','content.available','/content/group/PortfolioAdmin/system/formCreate.xslt','PortfolioAdmin','~localhost~admin','a'),(6,'2011-04-13 14:59:29','realm.add','/realm//content/group/PortfolioAdmin/system/formCreate.xslt',NULL,'~localhost~admin','m'),(7,'2011-04-13 14:59:29','realm.upd','/realm//content/group/PortfolioAdmin/system/formCreate.xslt',NULL,'~localhost~admin','m'),(8,'2011-04-13 14:59:29','content.new','/content/group/PortfolioAdmin/system/formFieldTemplate.xslt','PortfolioAdmin','~localhost~admin','m'),(9,'2011-04-13 14:59:29','content.available','/content/group/PortfolioAdmin/system/formFieldTemplate.xslt','PortfolioAdmin','~localhost~admin','a'),(10,'2011-04-13 14:59:29','realm.add','/realm//content/group/PortfolioAdmin/system/formFieldTemplate.xslt',NULL,'~localhost~admin','m'),(11,'2011-04-13 14:59:29','realm.upd','/realm//content/group/PortfolioAdmin/system/formFieldTemplate.xslt',NULL,'~localhost~admin','m'),(12,'2011-04-13 14:59:29','content.new','/content/group/PortfolioAdmin/system/formView.xslt','PortfolioAdmin','~localhost~admin','m'),(13,'2011-04-13 14:59:29','content.available','/content/group/PortfolioAdmin/system/formView.xslt','PortfolioAdmin','~localhost~admin','a'),(14,'2011-04-13 14:59:29','realm.add','/realm//content/group/PortfolioAdmin/system/formView.xslt',NULL,'~localhost~admin','m'),(15,'2011-04-13 14:59:29','realm.upd','/realm//content/group/PortfolioAdmin/system/formView.xslt',NULL,'~localhost~admin','m'),(16,'2011-04-13 14:59:39','realm.upd','/realm/!site.template.portfolio',NULL,'~localhost~admin','m'),(17,'2011-04-13 14:59:39','realm.upd','/realm/!group.template.portfolio',NULL,'~localhost~admin','m'),(18,'2011-04-13 14:59:39','realm.upd','/realm/!site.template.portfolioAdmin',NULL,'~localhost~admin','m'),(19,'2011-04-13 14:59:39','content.new','/content/group/PortfolioAdmin/system/freeFormRenderer.xml','PortfolioAdmin','~localhost~admin','m'),(20,'2011-04-13 14:59:39','content.available','/content/group/PortfolioAdmin/system/freeFormRenderer.xml','PortfolioAdmin','~localhost~admin','a'),(21,'2011-04-13 14:59:39','org.theospi.template.add','freeFormTemplate',NULL,'~localhost~admin','m'),(22,'2011-04-13 14:59:39','content.new','/content/group/PortfolioAdmin/system/contentOverText.jpg','PortfolioAdmin','~localhost~admin','m'),(23,'2011-04-13 14:59:39','content.available','/content/group/PortfolioAdmin/system/contentOverText.jpg','PortfolioAdmin','~localhost~admin','a'),(24,'2011-04-13 14:59:40','content.new','/content/group/PortfolioAdmin/system/contentOverText.xml','PortfolioAdmin','~localhost~admin','m'),(25,'2011-04-13 14:59:40','content.available','/content/group/PortfolioAdmin/system/contentOverText.xml','PortfolioAdmin','~localhost~admin','a'),(26,'2011-04-13 14:59:40','content.new','/content/group/PortfolioAdmin/system/2column.jpg','PortfolioAdmin','~localhost~admin','m'),(27,'2011-04-13 14:59:40','content.available','/content/group/PortfolioAdmin/system/2column.jpg','PortfolioAdmin','~localhost~admin','a'),(28,'2011-04-13 14:59:40','content.new','/content/group/PortfolioAdmin/system/twoColumn.xml','PortfolioAdmin','~localhost~admin','m'),(29,'2011-04-13 14:59:40','content.available','/content/group/PortfolioAdmin/system/twoColumn.xml','PortfolioAdmin','~localhost~admin','a'),(30,'2011-04-13 14:59:40','content.new','/content/group/PortfolioAdmin/system/Simplehtml.jpg','PortfolioAdmin','~localhost~admin','m'),(31,'2011-04-13 14:59:40','content.available','/content/group/PortfolioAdmin/system/Simplehtml.jpg','PortfolioAdmin','~localhost~admin','a'),(32,'2011-04-13 14:59:40','content.new','/content/group/PortfolioAdmin/system/simpleRichText.xml','PortfolioAdmin','~localhost~admin','m'),(33,'2011-04-13 14:59:40','content.available','/content/group/PortfolioAdmin/system/simpleRichText.xml','PortfolioAdmin','~localhost~admin','a'),(34,'2011-04-13 14:59:40','content.new','/content/user/admin/','~admin','~localhost~admin','m'),(35,'2011-04-13 14:59:40','realm.add','/realm//site/PortfolioAdmin',NULL,'~localhost~admin','m'),(36,'2011-04-13 14:59:40','realm.upd','/realm//site/PortfolioAdmin',NULL,'~localhost~admin','m'),(37,'2011-04-13 14:59:40','site.add','/site/PortfolioAdmin',NULL,'~localhost~admin','m'),(38,'2011-04-13 14:59:40','site.upd','/site/PortfolioAdmin',NULL,'~localhost~admin','m'),(39,'2011-04-13 14:59:40','site.upd','/site/PortfolioAdmin',NULL,'~localhost~admin','m'),(40,'2011-04-13 14:59:40','site.upd','/site/PortfolioAdmin',NULL,'~localhost~admin','m'),(41,'2011-04-13 14:59:40','mail.create','/mailarchive/channel/PortfolioAdmin/main','PortfolioAdmin','~localhost~admin','m'),(42,'2011-04-13 14:59:40','site.upd','/site/PortfolioAdmin',NULL,'~localhost~admin','m'),(43,'2011-04-13 14:59:40','site.upd','/site/PortfolioAdmin',NULL,'~localhost~admin','m'),(44,'2011-04-13 14:59:40','site.upd','/site/PortfolioAdmin',NULL,'~localhost~admin','m'),(45,'2011-04-13 14:59:40','site.upd','/site/PortfolioAdmin',NULL,'~localhost~admin','m'),(46,'2011-04-13 14:59:41','site.upd','/site/PortfolioAdmin',NULL,'~localhost~admin','m'),(47,'2011-04-13 14:59:41','site.upd','/site/PortfolioAdmin',NULL,'~localhost~admin','m'),(48,'2011-04-13 14:59:41','site.upd','/site/PortfolioAdmin',NULL,'~localhost~admin','m'),(49,'2011-04-13 14:59:41','site.upd','/site/PortfolioAdmin',NULL,'~localhost~admin','m'),(50,'2011-04-13 14:59:51','realm.add','/realm//site/citationsAdmin',NULL,'~localhost~admin','m'),(51,'2011-04-13 14:59:51','realm.upd','/realm//site/citationsAdmin',NULL,'~localhost~admin','m'),(52,'2011-04-13 14:59:51','site.add','/site/citationsAdmin',NULL,'~localhost~admin','m'),(53,'2011-04-13 14:59:51','site.upd','/site/citationsAdmin',NULL,'~localhost~admin','m'),(54,'2011-04-13 15:06:33','server.start','2.9-SNAPSHOT/TRUNK',NULL,'~localhost~?','a'),(55,'2011-04-13 15:06:36','user.login',NULL,NULL,'1b6e1a48-a679-4581-b43b-abb602db75a9','m'),(56,'2011-04-13 15:06:54','user.logout',NULL,NULL,'1b6e1a48-a679-4581-b43b-abb602db75a9','m'),(57,'2011-04-13 15:06:54','user.logout',NULL,NULL,'~localhost~?','m'),(58,'2011-04-13 15:06:56','realm.del','/realm//content/group/PortfolioAdmin/system/formCreate.xslt',NULL,'~localhost~admin','m'),(59,'2011-04-13 15:06:56','content.delete','/content/group/PortfolioAdmin/system/formCreate.xslt','PortfolioAdmin','~localhost~admin','m'),(60,'2011-04-13 15:06:56','content.new','/content/group/PortfolioAdmin/system/formCreate.xslt','PortfolioAdmin','~localhost~admin','m'),(61,'2011-04-13 15:06:56','content.available','/content/group/PortfolioAdmin/system/formCreate.xslt','PortfolioAdmin','~localhost~admin','a'),(62,'2011-04-13 15:06:56','realm.add','/realm//content/group/PortfolioAdmin/system/formCreate.xslt',NULL,'~localhost~admin','m'),(63,'2011-04-13 15:06:56','realm.upd','/realm//content/group/PortfolioAdmin/system/formCreate.xslt',NULL,'~localhost~admin','m'),(64,'2011-04-13 15:06:56','realm.del','/realm//content/group/PortfolioAdmin/system/formFieldTemplate.xslt',NULL,'~localhost~admin','m'),(65,'2011-04-13 15:06:56','content.delete','/content/group/PortfolioAdmin/system/formFieldTemplate.xslt','PortfolioAdmin','~localhost~admin','m'),(66,'2011-04-13 15:06:56','content.new','/content/group/PortfolioAdmin/system/formFieldTemplate.xslt','PortfolioAdmin','~localhost~admin','m'),(67,'2011-04-13 15:06:56','content.available','/content/group/PortfolioAdmin/system/formFieldTemplate.xslt','PortfolioAdmin','~localhost~admin','a'),(68,'2011-04-13 15:06:56','realm.add','/realm//content/group/PortfolioAdmin/system/formFieldTemplate.xslt',NULL,'~localhost~admin','m'),(69,'2011-04-13 15:06:56','realm.upd','/realm//content/group/PortfolioAdmin/system/formFieldTemplate.xslt',NULL,'~localhost~admin','m'),(70,'2011-04-13 15:06:56','realm.del','/realm//content/group/PortfolioAdmin/system/formView.xslt',NULL,'~localhost~admin','m'),(71,'2011-04-13 15:06:56','content.delete','/content/group/PortfolioAdmin/system/formView.xslt','PortfolioAdmin','~localhost~admin','m'),(72,'2011-04-13 15:06:56','content.new','/content/group/PortfolioAdmin/system/formView.xslt','PortfolioAdmin','~localhost~admin','m'),(73,'2011-04-13 15:06:56','content.available','/content/group/PortfolioAdmin/system/formView.xslt','PortfolioAdmin','~localhost~admin','a'),(74,'2011-04-13 15:06:56','realm.add','/realm//content/group/PortfolioAdmin/system/formView.xslt',NULL,'~localhost~admin','m'),(75,'2011-04-13 15:06:56','realm.upd','/realm//content/group/PortfolioAdmin/system/formView.xslt',NULL,'~localhost~admin','m'),(76,'2011-04-13 15:07:02','realm.upd','/realm/!site.template.portfolio',NULL,'~localhost~admin','m'),(77,'2011-04-13 15:07:02','realm.upd','/realm/!group.template.portfolio',NULL,'~localhost~admin','m'),(78,'2011-04-13 15:07:02','realm.upd','/realm/!site.template.portfolioAdmin',NULL,'~localhost~admin','m'),(79,'2011-04-13 15:07:02','content.revise','/content/group/PortfolioAdmin/system/freeFormRenderer.xml','PortfolioAdmin','~localhost~admin','m'),(80,'2011-04-13 15:07:03','org.theospi.template.revise','freeFormTemplate',NULL,'~localhost~admin','m'),(81,'2011-04-13 15:07:03','content.revise','/content/group/PortfolioAdmin/system/contentOverText.jpg','PortfolioAdmin','~localhost~admin','m'),(82,'2011-04-13 15:07:03','content.revise','/content/group/PortfolioAdmin/system/contentOverText.xml','PortfolioAdmin','~localhost~admin','m'),(83,'2011-04-13 15:07:03','content.revise','/content/group/PortfolioAdmin/system/2column.jpg','PortfolioAdmin','~localhost~admin','m'),(84,'2011-04-13 15:07:03','content.revise','/content/group/PortfolioAdmin/system/twoColumn.xml','PortfolioAdmin','~localhost~admin','m'),(85,'2011-04-13 15:07:03','content.revise','/content/group/PortfolioAdmin/system/Simplehtml.jpg','PortfolioAdmin','~localhost~admin','m'),(86,'2011-04-13 15:07:03','content.revise','/content/group/PortfolioAdmin/system/simpleRichText.xml','PortfolioAdmin','~localhost~admin','m'),(87,'2011-04-13 15:55:40','user.login',NULL,NULL,'8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(88,'2011-04-13 15:55:40','realm.add','/realm//site/~d1430d8d-af0b-49e6-8c39-2d253673319a',NULL,'8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(89,'2011-04-13 15:55:40','realm.upd','/realm//site/~d1430d8d-af0b-49e6-8c39-2d253673319a',NULL,'8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(90,'2011-04-13 15:55:40','calendar.create','/calendar/calendar/~d1430d8d-af0b-49e6-8c39-2d253673319a/main','~d1430d8d-af0b-49e6-8c39-2d253673319a','8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(91,'2011-04-13 15:55:41','annc.create','/announcement/channel/~d1430d8d-af0b-49e6-8c39-2d253673319a/main','~d1430d8d-af0b-49e6-8c39-2d253673319a','8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(92,'2011-04-13 15:55:41','content.new','/content/user/d1430d8d-af0b-49e6-8c39-2d253673319a/','~d1430d8d-af0b-49e6-8c39-2d253673319a','8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(93,'2011-04-13 15:55:41','site.add','/site/~d1430d8d-af0b-49e6-8c39-2d253673319a',NULL,'8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(94,'2011-04-13 15:55:41','pres.begin','/presence/~d1430d8d-af0b-49e6-8c39-2d253673319a-presence',NULL,'8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(95,'2011-04-13 15:56:50','pres.end','/presence/~d1430d8d-af0b-49e6-8c39-2d253673319a-presence',NULL,'8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(96,'2011-04-13 15:58:17','pres.begin','/presence/~instructor-presence',NULL,'8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(97,'2011-04-13 15:59:20','pres.end','/presence/~instructor-presence',NULL,'8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(98,'2011-04-13 15:59:36','realm.upd','/realm//site/92baf195-be33-4a5b-b378-6d96e9665ffc','~d1430d8d-af0b-49e6-8c39-2d253673319a','8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(99,'2011-04-13 15:59:36','site.add','/site/92baf195-be33-4a5b-b378-6d96e9665ffc','~d1430d8d-af0b-49e6-8c39-2d253673319a','8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(100,'2011-04-13 15:59:36','realm.upd','/realm//site/92baf195-be33-4a5b-b378-6d96e9665ffc','~d1430d8d-af0b-49e6-8c39-2d253673319a','8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(101,'2011-04-13 15:59:36','site.upd','/site/92baf195-be33-4a5b-b378-6d96e9665ffc','~d1430d8d-af0b-49e6-8c39-2d253673319a','8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(102,'2011-04-13 15:59:37','annc.create','/announcement/channel/92baf195-be33-4a5b-b378-6d96e9665ffc/main','92baf195-be33-4a5b-b378-6d96e9665ffc','8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(103,'2011-04-13 15:59:37','site.upd','/site/92baf195-be33-4a5b-b378-6d96e9665ffc','~d1430d8d-af0b-49e6-8c39-2d253673319a','8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(104,'2011-04-13 15:59:37','realm.upd','/realm//site/92baf195-be33-4a5b-b378-6d96e9665ffc','~d1430d8d-af0b-49e6-8c39-2d253673319a','8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(105,'2011-04-13 15:59:39','realm.upd','/realm//site/92baf195-be33-4a5b-b378-6d96e9665ffc/group/33d648ee-b6df-444a-ba72-ce93646802cc','~d1430d8d-af0b-49e6-8c39-2d253673319a','8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(106,'2011-04-13 15:59:39','site.upd','/site/92baf195-be33-4a5b-b378-6d96e9665ffc','~d1430d8d-af0b-49e6-8c39-2d253673319a','8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(107,'2011-04-13 15:59:39','pres.begin','/presence/~instructor-presence',NULL,'8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(108,'2011-04-13 15:59:48','pres.begin','/presence/92baf195-be33-4a5b-b378-6d96e9665ffc-presence',NULL,'8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(109,'2011-04-13 16:00:50','pres.end','/presence/92baf195-be33-4a5b-b378-6d96e9665ffc-presence',NULL,'8a024426-7f6f-4e86-b17f-dd5209bef218','m'),(110,'2011-04-13 16:00:50','pres.end','/presence/~instructor-presence',NULL,'8a024426-7f6f-4e86-b17f-dd5209bef218','m');
/*!40000 ALTER TABLE `SAKAI_EVENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_EVENT_DELAY`
--

DROP TABLE IF EXISTS `SAKAI_EVENT_DELAY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_EVENT_DELAY` (
  `EVENT_DELAY_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EVENT` varchar(32) DEFAULT NULL,
  `REF` varchar(255) DEFAULT NULL,
  `USER_ID` varchar(99) DEFAULT NULL,
  `EVENT_CODE` varchar(1) DEFAULT NULL,
  `PRIORITY` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`EVENT_DELAY_ID`),
  KEY `IE_SAKAI_EVENT_DELAY_RESOURCE` (`REF`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_EVENT_DELAY`
--

LOCK TABLES `SAKAI_EVENT_DELAY` WRITE;
/*!40000 ALTER TABLE `SAKAI_EVENT_DELAY` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_EVENT_DELAY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_LOCKS`
--

DROP TABLE IF EXISTS `SAKAI_LOCKS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_LOCKS` (
  `TABLE_NAME` varchar(64) DEFAULT NULL,
  `RECORD_ID` varchar(512) DEFAULT NULL,
  `LOCK_TIME` datetime DEFAULT NULL,
  `USAGE_SESSION_ID` varchar(36) DEFAULT NULL,
  UNIQUE KEY `SAKAI_LOCKS_INDEX` (`TABLE_NAME`,`RECORD_ID`(128))
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_LOCKS`
--

LOCK TABLES `SAKAI_LOCKS` WRITE;
/*!40000 ALTER TABLE `SAKAI_LOCKS` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_LOCKS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_MESSAGE_BUNDLE`
--

DROP TABLE IF EXISTS `SAKAI_MESSAGE_BUNDLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_MESSAGE_BUNDLE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MODULE_NAME` varchar(255) NOT NULL,
  `BASENAME` varchar(255) NOT NULL,
  `PROP_NAME` varchar(255) NOT NULL,
  `PROP_VALUE` text,
  `LOCALE` varchar(255) NOT NULL,
  `DEFAULT_VALUE` text NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_MESSAGE_BUNDLE`
--

LOCK TABLES `SAKAI_MESSAGE_BUNDLE` WRITE;
/*!40000 ALTER TABLE `SAKAI_MESSAGE_BUNDLE` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_MESSAGE_BUNDLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_NOTIFICATION`
--

DROP TABLE IF EXISTS `SAKAI_NOTIFICATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_NOTIFICATION` (
  `NOTIFICATION_ID` varchar(99) NOT NULL,
  `XML` longtext,
  UNIQUE KEY `SAKAI_NOTIFICATION_INDEX` (`NOTIFICATION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_NOTIFICATION`
--

LOCK TABLES `SAKAI_NOTIFICATION` WRITE;
/*!40000 ALTER TABLE `SAKAI_NOTIFICATION` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_NOTIFICATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_PERSON_META_T`
--

DROP TABLE IF EXISTS `SAKAI_PERSON_META_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_PERSON_META_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_UUID` varchar(99) NOT NULL,
  `PROPERTY` varchar(255) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_PERSON_META_T`
--

LOCK TABLES `SAKAI_PERSON_META_T` WRITE;
/*!40000 ALTER TABLE `SAKAI_PERSON_META_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_PERSON_META_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_PERSON_T`
--

DROP TABLE IF EXISTS `SAKAI_PERSON_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_PERSON_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PERSON_TYPE` varchar(3) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `UUID` varchar(36) NOT NULL,
  `LAST_MODIFIED_BY` varchar(36) NOT NULL,
  `LAST_MODIFIED_DATE` datetime NOT NULL,
  `CREATED_BY` varchar(36) NOT NULL,
  `CREATED_DATE` datetime NOT NULL,
  `AGENT_UUID` varchar(99) NOT NULL,
  `TYPE_UUID` varchar(36) NOT NULL,
  `COMMON_NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `SEE_ALSO` varchar(255) DEFAULT NULL,
  `STREET` varchar(255) DEFAULT NULL,
  `SURNAME` varchar(255) DEFAULT NULL,
  `TELEPHONE_NUMBER` varchar(255) DEFAULT NULL,
  `FAX_NUMBER` varchar(255) DEFAULT NULL,
  `LOCALITY_NAME` varchar(255) DEFAULT NULL,
  `OU` varchar(255) DEFAULT NULL,
  `PHYSICAL_DELIVERY_OFFICE_NAME` varchar(255) DEFAULT NULL,
  `POSTAL_ADDRESS` varchar(255) DEFAULT NULL,
  `POSTAL_CODE` varchar(255) DEFAULT NULL,
  `POST_OFFICE_BOX` varchar(255) DEFAULT NULL,
  `STATE_PROVINCE_NAME` varchar(255) DEFAULT NULL,
  `STREET_ADDRESS` varchar(255) DEFAULT NULL,
  `TITLE` varchar(255) DEFAULT NULL,
  `BUSINESS_CATEGORY` varchar(255) DEFAULT NULL,
  `CAR_LICENSE` varchar(255) DEFAULT NULL,
  `DEPARTMENT_NUMBER` varchar(255) DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `EMPLOYEE_NUMBER` varchar(255) DEFAULT NULL,
  `EMPLOYEE_TYPE` varchar(255) DEFAULT NULL,
  `GIVEN_NAME` varchar(255) DEFAULT NULL,
  `HOME_PHONE` varchar(255) DEFAULT NULL,
  `HOME_POSTAL_ADDRESS` varchar(255) DEFAULT NULL,
  `INITIALS` varchar(255) DEFAULT NULL,
  `JPEG_PHOTO` blob,
  `LABELED_URI` varchar(255) DEFAULT NULL,
  `MAIL` varchar(255) DEFAULT NULL,
  `MANAGER` varchar(255) DEFAULT NULL,
  `MOBILE` varchar(255) DEFAULT NULL,
  `ORGANIZATION` varchar(255) DEFAULT NULL,
  `PAGER` varchar(255) DEFAULT NULL,
  `PREFERRED_LANGUAGE` varchar(255) DEFAULT NULL,
  `ROOM_NUMBER` varchar(255) DEFAULT NULL,
  `SECRETARY` varchar(255) DEFAULT NULL,
  `UID_C` varchar(255) DEFAULT NULL,
  `USER_CERTIFICATE` tinyblob,
  `USER_PKCS12` tinyblob,
  `USER_SMIME_CERTIFICATE` tinyblob,
  `X500_UNIQUE_ID` varchar(255) DEFAULT NULL,
  `AFFILIATION` varchar(255) DEFAULT NULL,
  `ENTITLEMENT` varchar(255) DEFAULT NULL,
  `NICKNAME` varchar(255) DEFAULT NULL,
  `ORG_DN` varchar(255) DEFAULT NULL,
  `ORG_UNIT_DN` varchar(255) DEFAULT NULL,
  `PRIMARY_AFFILIATION` varchar(255) DEFAULT NULL,
  `PRIMARY_ORG_UNIT_DN` varchar(255) DEFAULT NULL,
  `PRINCIPAL_NAME` varchar(255) DEFAULT NULL,
  `CAMPUS` varchar(255) DEFAULT NULL,
  `HIDE_PRIVATE_INFO` bit(1) DEFAULT NULL,
  `HIDE_PUBLIC_INFO` bit(1) DEFAULT NULL,
  `NOTES` text,
  `PICTURE_URL` varchar(255) DEFAULT NULL,
  `SYSTEM_PICTURE_PREFERRED` bit(1) DEFAULT NULL,
  `ferpaEnabled` bit(1) DEFAULT NULL,
  `dateOfBirth` date DEFAULT NULL,
  `locked` bit(1) DEFAULT NULL,
  `FAVOURITE_BOOKS` text,
  `FAVOURITE_TV_SHOWS` text,
  `FAVOURITE_MOVIES` text,
  `FAVOURITE_QUOTES` text,
  `EDUCATION_COURSE` text,
  `EDUCATION_SUBJECTS` text,
  `NORMALIZEDMOBILE` varchar(255) DEFAULT NULL,
  `STAFF_PROFILE` text,
  `UNIVERSITY_PROFILE_URL` text,
  `ACADEMIC_PROFILE_URL` text,
  `PUBLICATIONS` text,
  `BUSINESS_BIOGRAPHY` text,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UUID` (`UUID`),
  UNIQUE KEY `AGENT_UUID` (`AGENT_UUID`,`TYPE_UUID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_PERSON_T`
--

LOCK TABLES `SAKAI_PERSON_T` WRITE;
/*!40000 ALTER TABLE `SAKAI_PERSON_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_PERSON_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_POSTEM_GRADEBOOK`
--

DROP TABLE IF EXISTS `SAKAI_POSTEM_GRADEBOOK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_POSTEM_GRADEBOOK` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lockId` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `context` varchar(36) NOT NULL,
  `creator` varchar(36) NOT NULL,
  `created` datetime NOT NULL,
  `last_updater` varchar(36) NOT NULL,
  `last_updated` datetime NOT NULL,
  `released` bit(1) NOT NULL,
  `stats` bit(1) NOT NULL,
  `template` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`,`context`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_POSTEM_GRADEBOOK`
--

LOCK TABLES `SAKAI_POSTEM_GRADEBOOK` WRITE;
/*!40000 ALTER TABLE `SAKAI_POSTEM_GRADEBOOK` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_POSTEM_GRADEBOOK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_POSTEM_HEADINGS`
--

DROP TABLE IF EXISTS `SAKAI_POSTEM_HEADINGS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_POSTEM_HEADINGS` (
  `gradebook_id` bigint(20) NOT NULL,
  `heading` text NOT NULL,
  `location` int(11) NOT NULL,
  PRIMARY KEY (`gradebook_id`,`location`),
  KEY `FKF54C1C2E8B6B03CE` (`gradebook_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_POSTEM_HEADINGS`
--

LOCK TABLES `SAKAI_POSTEM_HEADINGS` WRITE;
/*!40000 ALTER TABLE `SAKAI_POSTEM_HEADINGS` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_POSTEM_HEADINGS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_POSTEM_STUDENT`
--

DROP TABLE IF EXISTS `SAKAI_POSTEM_STUDENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_POSTEM_STUDENT` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lockId` int(11) NOT NULL,
  `username` varchar(36) NOT NULL,
  `last_checked` datetime DEFAULT NULL,
  `surrogate_key` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4FBA80FE56A169CC` (`surrogate_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_POSTEM_STUDENT`
--

LOCK TABLES `SAKAI_POSTEM_STUDENT` WRITE;
/*!40000 ALTER TABLE `SAKAI_POSTEM_STUDENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_POSTEM_STUDENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_POSTEM_STUDENT_GRADES`
--

DROP TABLE IF EXISTS `SAKAI_POSTEM_STUDENT_GRADES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_POSTEM_STUDENT_GRADES` (
  `student_id` bigint(20) NOT NULL,
  `grade` text,
  `location` int(11) NOT NULL,
  PRIMARY KEY (`student_id`,`location`),
  KEY `FK321A31DDD3321FCA` (`student_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_POSTEM_STUDENT_GRADES`
--

LOCK TABLES `SAKAI_POSTEM_STUDENT_GRADES` WRITE;
/*!40000 ALTER TABLE `SAKAI_POSTEM_STUDENT_GRADES` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_POSTEM_STUDENT_GRADES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_PREFERENCES`
--

DROP TABLE IF EXISTS `SAKAI_PREFERENCES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_PREFERENCES` (
  `PREFERENCES_ID` varchar(99) NOT NULL,
  `XML` longtext,
  UNIQUE KEY `SAKAI_PREFERENCES_INDEX` (`PREFERENCES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_PREFERENCES`
--

LOCK TABLES `SAKAI_PREFERENCES` WRITE;
/*!40000 ALTER TABLE `SAKAI_PREFERENCES` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_PREFERENCES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_PRESENCE`
--

DROP TABLE IF EXISTS `SAKAI_PRESENCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_PRESENCE` (
  `SESSION_ID` varchar(36) DEFAULT NULL,
  `LOCATION_ID` varchar(255) DEFAULT NULL,
  KEY `SAKAI_PRESENCE_SESSION_INDEX` (`SESSION_ID`),
  KEY `SAKAI_PRESENCE_LOCATION_INDEX` (`LOCATION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_PRESENCE`
--

LOCK TABLES `SAKAI_PRESENCE` WRITE;
/*!40000 ALTER TABLE `SAKAI_PRESENCE` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_PRESENCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_PRIVACY_RECORD`
--

DROP TABLE IF EXISTS `SAKAI_PRIVACY_RECORD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_PRIVACY_RECORD` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lockId` int(11) NOT NULL,
  `contextId` varchar(100) NOT NULL,
  `recordType` varchar(100) NOT NULL,
  `userId` varchar(100) NOT NULL,
  `viewable` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `contextId` (`contextId`,`recordType`,`userId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_PRIVACY_RECORD`
--

LOCK TABLES `SAKAI_PRIVACY_RECORD` WRITE;
/*!40000 ALTER TABLE `SAKAI_PRIVACY_RECORD` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_PRIVACY_RECORD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_REALM`
--

DROP TABLE IF EXISTS `SAKAI_REALM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_REALM` (
  `REALM_KEY` int(11) NOT NULL AUTO_INCREMENT,
  `REALM_ID` varchar(255) NOT NULL,
  `PROVIDER_ID` varchar(4000) DEFAULT NULL,
  `MAINTAIN_ROLE` int(11) DEFAULT NULL,
  `CREATEDBY` varchar(99) DEFAULT NULL,
  `MODIFIEDBY` varchar(99) DEFAULT NULL,
  `CREATEDON` datetime DEFAULT NULL,
  `MODIFIEDON` datetime DEFAULT NULL,
  PRIMARY KEY (`REALM_KEY`),
  UNIQUE KEY `AK_SAKAI_REALM_ID` (`REALM_ID`),
  KEY `IE_SAKAI_REALM_CREATED` (`CREATEDBY`,`CREATEDON`),
  KEY `IE_SAKAI_REALM_MODDED` (`MODIFIEDBY`,`MODIFIEDON`),
  KEY `MAINTAIN_ROLE` (`MAINTAIN_ROLE`)
) ENGINE=MyISAM AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_REALM`
--

LOCK TABLES `SAKAI_REALM` WRITE;
/*!40000 ALTER TABLE `SAKAI_REALM` DISABLE KEYS */;
INSERT INTO `SAKAI_REALM` VALUES (1,'!site.helper','',NULL,'admin','admin','2011-04-13 14:59:20','2011-04-13 14:59:20'),(2,'!site.user','',NULL,'admin','admin','2011-04-13 14:59:20','2011-04-13 14:59:20'),(3,'!user.template','',NULL,'admin','admin','2011-04-13 14:59:20','2011-04-13 14:59:20'),(4,'!user.template.guest','',NULL,'admin','admin','2011-04-13 14:59:20','2011-04-13 14:59:20'),(5,'!user.template.maintain','',NULL,'admin','admin','2011-04-13 14:59:20','2011-04-13 14:59:20'),(6,'!user.template.registered','',NULL,'admin','admin','2011-04-13 14:59:20','2011-04-13 14:59:20'),(7,'!user.template.sample','',NULL,'admin','admin','2011-04-13 14:59:20','2011-04-13 14:59:20'),(8,'!site.template','',9,'admin','admin','2011-04-13 14:59:20','2011-04-13 14:59:20'),(9,'!site.template.course','',8,'admin','admin','2011-04-13 14:59:20','2011-04-13 14:59:20'),(10,'!site.template.portfolio',NULL,5,'admin','admin','2011-04-13 14:59:20','2011-04-13 15:07:02'),(11,'!site.template.portfolioAdmin',NULL,10,'admin','admin','2011-04-13 14:59:21','2011-04-13 15:07:02'),(12,'!group.template','',NULL,'admin','admin','2011-04-13 14:59:21','2011-04-13 14:59:21'),(13,'!group.template.course','',8,'admin','admin','2011-04-13 14:59:21','2011-04-13 14:59:21'),(14,'!group.template.portfolio',NULL,5,'admin','admin','2011-04-13 14:59:21','2011-04-13 15:07:02'),(15,'/content/public/','',NULL,'admin','admin','2011-04-13 14:59:21','2011-04-13 14:59:21'),(16,'/content/attachment/','',NULL,'admin','admin','2011-04-13 14:59:21','2011-04-13 14:59:21'),(17,'/announcement/channel/!site/motd','',NULL,'admin','admin','2011-04-13 14:59:21','2011-04-13 14:59:21'),(18,'!pubview','',NULL,'admin','admin','2011-04-13 14:59:21','2011-04-13 14:59:21'),(19,'/site/!gateway','',NULL,'admin','admin','2011-04-13 14:59:21','2011-04-13 14:59:21'),(20,'/site/!error','',NULL,'admin','admin','2011-04-13 14:59:21','2011-04-13 14:59:21'),(21,'/site/!urlError','',NULL,'admin','admin','2011-04-13 14:59:21','2011-04-13 14:59:21'),(22,'/site/mercury','',NULL,'admin','admin','2011-04-13 14:59:21','2011-04-13 14:59:21'),(23,'/site/!admin','',4,'admin','admin','2011-04-13 14:59:22','2011-04-13 14:59:22'),(24,'!matrix.template.portfolio','',NULL,'admin','admin','2011-04-13 14:59:22','2011-04-13 14:59:22'),(25,'!matrix.template.course','',NULL,'admin','admin','2011-04-13 14:59:22','2011-04-13 14:59:22'),(26,'!matrix.template.project','',NULL,'admin','admin','2011-04-13 14:59:22','2011-04-13 14:59:22'),(32,'/content/group/PortfolioAdmin/system/formCreate.xslt',NULL,NULL,'admin','admin','2011-04-13 15:06:56','2011-04-13 15:06:56'),(33,'/content/group/PortfolioAdmin/system/formFieldTemplate.xslt',NULL,NULL,'admin','admin','2011-04-13 15:06:56','2011-04-13 15:06:56'),(34,'/content/group/PortfolioAdmin/system/formView.xslt',NULL,NULL,'admin','admin','2011-04-13 15:06:56','2011-04-13 15:06:56'),(30,'/site/PortfolioAdmin',NULL,10,'admin','admin','2011-04-13 14:59:40','2011-04-13 14:59:40'),(31,'/site/citationsAdmin',NULL,9,'admin','admin','2011-04-13 14:59:51','2011-04-13 14:59:51'),(35,'/site/~d1430d8d-af0b-49e6-8c39-2d253673319a',NULL,NULL,'d1430d8d-af0b-49e6-8c39-2d253673319a','d1430d8d-af0b-49e6-8c39-2d253673319a','2011-04-13 15:55:40','2011-04-13 15:55:40'),(36,'/site/92baf195-be33-4a5b-b378-6d96e9665ffc','SMPL101 Spring 2011',8,'admin','d1430d8d-af0b-49e6-8c39-2d253673319a','2011-04-13 14:59:20','2011-04-13 15:59:37'),(37,'/site/92baf195-be33-4a5b-b378-6d96e9665ffc/group/33d648ee-b6df-444a-ba72-ce93646802cc','SMPL101 Spring 2011',8,'admin','d1430d8d-af0b-49e6-8c39-2d253673319a','2011-04-13 14:59:21','2011-04-13 15:59:38');
/*!40000 ALTER TABLE `SAKAI_REALM` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_REALM_FUNCTION`
--

DROP TABLE IF EXISTS `SAKAI_REALM_FUNCTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_REALM_FUNCTION` (
  `FUNCTION_KEY` int(11) NOT NULL AUTO_INCREMENT,
  `FUNCTION_NAME` varchar(99) NOT NULL,
  PRIMARY KEY (`FUNCTION_KEY`),
  UNIQUE KEY `IE_SAKAI_REALM_FUNCTION_NAME` (`FUNCTION_NAME`),
  KEY `SAKAI_REALM_FUNCTION_KN` (`FUNCTION_KEY`,`FUNCTION_NAME`)
) ENGINE=MyISAM AUTO_INCREMENT=181 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_REALM_FUNCTION`
--

LOCK TABLES `SAKAI_REALM_FUNCTION` WRITE;
/*!40000 ALTER TABLE `SAKAI_REALM_FUNCTION` DISABLE KEYS */;
INSERT INTO `SAKAI_REALM_FUNCTION` VALUES (1,'annc.all.groups'),(2,'annc.delete.any'),(3,'annc.delete.own'),(4,'annc.new'),(5,'annc.read'),(6,'annc.read.drafts'),(7,'annc.revise.any'),(8,'annc.revise.own'),(9,'asn.delete'),(10,'asn.grade'),(11,'asn.new'),(12,'asn.read'),(13,'asn.revise'),(14,'asn.submit'),(15,'asn.all.groups'),(16,'assessment.createAssessment'),(17,'assessment.deleteAssessment.any'),(18,'assessment.deleteAssessment.own'),(19,'assessment.editAssessment.any'),(20,'assessment.editAssessment.own'),(21,'assessment.gradeAssessment.any'),(22,'assessment.gradeAssessment.own'),(23,'assessment.publishAssessment.any'),(24,'assessment.publishAssessment.own'),(25,'assessment.questionpool.copy.own'),(26,'assessment.questionpool.create'),(27,'assessment.questionpool.delete.own'),(28,'assessment.questionpool.edit.own'),(29,'assessment.submitAssessmentForGrade'),(30,'assessment.takeAssessment'),(31,'assessment.template.create'),(32,'assessment.template.delete.own'),(33,'assessment.template.edit.own'),(34,'calendar.delete.any'),(35,'calendar.delete.own'),(36,'calendar.new'),(37,'calendar.read'),(38,'calendar.revise.any'),(39,'calendar.revise.own'),(40,'calendar.all.groups'),(41,'chat.delete.any'),(42,'chat.delete.own'),(43,'chat.delete.channel'),(44,'chat.new'),(45,'chat.new.channel'),(46,'chat.read'),(47,'chat.revise.channel'),(48,'content.delete.any'),(49,'content.delete.own'),(50,'content.new'),(51,'content.read'),(52,'content.revise.any'),(53,'content.revise.own'),(54,'content.all.groups'),(55,'content.hidden'),(56,'disc.delete.any'),(57,'disc.delete.own'),(58,'disc.new'),(59,'disc.new.topic'),(60,'disc.read'),(61,'disc.revise.any'),(62,'disc.revise.own'),(63,'dropbox.own'),(64,'dropbox.maintain'),(65,'gradebook.editAssignments'),(66,'gradebook.gradeAll'),(67,'gradebook.gradeSection'),(68,'gradebook.viewOwnGrades'),(69,'mail.delete.any'),(70,'mail.new'),(71,'mail.read'),(72,'msg.emailout'),(73,'metaobj.create'),(74,'metaobj.edit'),(75,'metaobj.export'),(76,'metaobj.delete'),(77,'metaobj.publish'),(78,'metaobj.suggest.global.publish'),(79,'prefs.add'),(80,'prefs.del'),(81,'prefs.upd'),(82,'realm.add'),(83,'realm.del'),(84,'realm.upd'),(85,'reports.view'),(86,'reports.run'),(87,'reports.create'),(88,'reports.edit'),(89,'reports.delete'),(90,'reports.share'),(91,'roster.export'),(92,'roster.viewallmembers'),(93,'roster.viewhidden'),(94,'roster.viewofficialphoto'),(95,'roster.viewenrollmentstatus'),(96,'roster.viewprofile'),(97,'roster.viewgroup'),(98,'realm.upd.own'),(99,'section.role.instructor'),(100,'section.role.student'),(101,'section.role.ta'),(102,'site.add'),(103,'site.add.course'),(104,'site.add.usersite'),(105,'site.del'),(106,'site.upd'),(107,'site.upd.site.mbrshp'),(108,'site.upd.grp.mbrshp'),(109,'site.viewRoster'),(110,'site.visit'),(111,'site.visit.unp'),(112,'user.add'),(113,'user.upd.own'),(114,'rwiki.admin'),(115,'rwiki.create'),(116,'rwiki.delete'),(117,'rwiki.read'),(118,'rwiki.superadmin'),(119,'rwiki.update'),(120,'mailtool.admin'),(121,'mailtool.send'),(122,'poll.add'),(123,'poll.deleteAny'),(124,'poll.deleteOwn'),(125,'poll.editAny'),(126,'poll.editOwn'),(127,'poll.vote'),(128,'osp.style.globalPublish'),(129,'osp.style.publish'),(130,'osp.style.delete'),(131,'osp.style.create'),(132,'osp.style.edit'),(133,'osp.style.suggestGlobalPublish'),(134,'osp.help.glossary.delete'),(135,'osp.help.glossary.add'),(136,'osp.help.glossary.edit'),(137,'osp.help.glossary.export'),(138,'osp.matrix.scaffolding.create'),(139,'osp.matrix.scaffolding.revise.any'),(140,'osp.matrix.scaffolding.revise.own'),(141,'osp.matrix.scaffolding.delete.any'),(142,'osp.matrix.scaffolding.delete.own'),(143,'osp.matrix.scaffolding.publish.any'),(144,'osp.matrix.scaffolding.publish.own'),(145,'osp.matrix.scaffolding.export.any'),(146,'osp.matrix.scaffolding.export.own'),(147,'osp.matrix.scaffoldingSpecific.accessAll'),(148,'osp.matrix.scaffoldingSpecific.viewEvalOther'),(149,'osp.matrix.scaffoldingSpecific.viewFeedbackOther'),(150,'osp.matrix.scaffoldingSpecific.manageStatus'),(151,'osp.matrix.scaffoldingSpecific.accessUserList'),(152,'osp.matrix.scaffoldingSpecific.viewAllGroups'),(153,'osp.matrix.scaffoldingSpecific.use'),(154,'osp.matrix.viewOwner'),(155,'osp.portfolio.evaluation.use'),(156,'osp.presentation.create'),(157,'osp.presentation.edit'),(158,'osp.presentation.delete'),(159,'osp.presentation.copy'),(160,'osp.presentation.comment'),(161,'osp.presentation.review'),(162,'osp.presentation.template.copy'),(163,'osp.presentation.template.publish'),(164,'osp.presentation.template.delete'),(165,'osp.presentation.template.create'),(166,'osp.presentation.template.edit'),(167,'osp.presentation.template.export'),(168,'osp.presentation.layout.publish'),(169,'osp.presentation.layout.delete'),(170,'osp.presentation.layout.create'),(171,'osp.presentation.layout.edit'),(172,'osp.presentation.layout.suggestPublish'),(173,'osp.wizard.publish'),(174,'osp.wizard.delete'),(175,'osp.wizard.create'),(176,'osp.wizard.edit'),(177,'osp.wizard.review'),(178,'osp.wizard.export'),(179,'osp.wizard.view'),(180,'osp.wizard.evaluate');
/*!40000 ALTER TABLE `SAKAI_REALM_FUNCTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_REALM_PROPERTY`
--

DROP TABLE IF EXISTS `SAKAI_REALM_PROPERTY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_REALM_PROPERTY` (
  `REALM_KEY` int(11) NOT NULL,
  `NAME` varchar(99) NOT NULL,
  `VALUE` mediumtext,
  PRIMARY KEY (`REALM_KEY`,`NAME`),
  KEY `FK_SAKAI_REALM_PROPERTY` (`REALM_KEY`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_REALM_PROPERTY`
--

LOCK TABLES `SAKAI_REALM_PROPERTY` WRITE;
/*!40000 ALTER TABLE `SAKAI_REALM_PROPERTY` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_REALM_PROPERTY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_REALM_PROVIDER`
--

DROP TABLE IF EXISTS `SAKAI_REALM_PROVIDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_REALM_PROVIDER` (
  `REALM_KEY` int(11) NOT NULL,
  `PROVIDER_ID` varchar(200) NOT NULL,
  PRIMARY KEY (`REALM_KEY`,`PROVIDER_ID`),
  KEY `FK_SAKAI_REALM_PROVIDER` (`REALM_KEY`),
  KEY `IE_SAKAI_REALM_PROVIDER_ID` (`PROVIDER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_REALM_PROVIDER`
--

LOCK TABLES `SAKAI_REALM_PROVIDER` WRITE;
/*!40000 ALTER TABLE `SAKAI_REALM_PROVIDER` DISABLE KEYS */;
INSERT INTO `SAKAI_REALM_PROVIDER` VALUES (36,'SMPL101 Spring 2011'),(37,'SMPL101 Spring 2011');
/*!40000 ALTER TABLE `SAKAI_REALM_PROVIDER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_REALM_RL_FN`
--

DROP TABLE IF EXISTS `SAKAI_REALM_RL_FN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_REALM_RL_FN` (
  `REALM_KEY` int(11) NOT NULL,
  `ROLE_KEY` int(11) NOT NULL,
  `FUNCTION_KEY` int(11) NOT NULL,
  PRIMARY KEY (`REALM_KEY`,`ROLE_KEY`,`FUNCTION_KEY`),
  KEY `FK_SAKAI_REALM_RL_FN_REALM` (`REALM_KEY`),
  KEY `FK_SAKAI_REALM_RL_FN_FUNC` (`FUNCTION_KEY`),
  KEY `FJ_SAKAI_REALM_RL_FN_ROLE` (`ROLE_KEY`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_REALM_RL_FN`
--

LOCK TABLES `SAKAI_REALM_RL_FN` WRITE;
/*!40000 ALTER TABLE `SAKAI_REALM_RL_FN` DISABLE KEYS */;
INSERT INTO `SAKAI_REALM_RL_FN` VALUES (1,9,83),(1,9,84),(2,3,5),(2,3,37),(2,3,44),(2,3,46),(2,3,51),(2,3,58),(2,3,60),(2,3,62),(2,3,68),(2,3,71),(2,3,110),(2,3,115),(2,3,117),(2,3,119),(2,3,121),(2,9,2),(2,9,3),(2,9,4),(2,9,5),(2,9,6),(2,9,7),(2,9,8),(2,9,34),(2,9,35),(2,9,36),(2,9,37),(2,9,38),(2,9,39),(2,9,41),(2,9,42),(2,9,43),(2,9,44),(2,9,45),(2,9,46),(2,9,47),(2,9,48),(2,9,49),(2,9,50),(2,9,51),(2,9,52),(2,9,53),(2,9,55),(2,9,56),(2,9,57),(2,9,58),(2,9,59),(2,9,60),(2,9,61),(2,9,62),(2,9,64),(2,9,65),(2,9,66),(2,9,69),(2,9,70),(2,9,71),(2,9,83),(2,9,84),(2,9,106),(2,9,107),(2,9,108),(2,9,109),(2,9,110),(2,9,111),(2,9,114),(2,9,115),(2,9,117),(2,9,119),(2,9,120),(2,9,121),(3,1,112),(3,2,79),(3,2,80),(3,2,81),(3,2,82),(3,2,98),(3,2,104),(3,2,112),(3,2,113),(4,1,112),(4,2,79),(4,2,80),(4,2,81),(4,2,82),(4,2,98),(4,2,104),(4,2,112),(4,2,113),(5,1,112),(5,2,79),(5,2,80),(5,2,81),(5,2,82),(5,2,98),(5,2,102),(5,2,103),(5,2,104),(5,2,112),(5,2,113),(6,1,112),(6,2,79),(6,2,80),(6,2,81),(6,2,82),(6,2,98),(6,2,102),(6,2,103),(6,2,104),(6,2,112),(6,2,113),(7,1,112),(7,2,79),(7,2,80),(7,2,81),(7,2,82),(7,2,98),(7,2,102),(7,2,103),(7,2,104),(7,2,112),(7,2,113),(8,3,5),(8,3,12),(8,3,14),(8,3,29),(8,3,30),(8,3,37),(8,3,44),(8,3,46),(8,3,51),(8,3,58),(8,3,60),(8,3,62),(8,3,63),(8,3,68),(8,3,70),(8,3,71),(8,3,91),(8,3,92),(8,3,96),(8,3,100),(8,3,110),(8,3,115),(8,3,117),(8,3,119),(8,3,121),(8,3,127),(8,3,131),(8,3,156),(8,3,159),(8,3,160),(8,3,169),(8,3,170),(8,3,171),(8,3,179),(8,9,1),(8,9,2),(8,9,3),(8,9,4),(8,9,5),(8,9,6),(8,9,7),(8,9,8),(8,9,9),(8,9,10),(8,9,11),(8,9,12),(8,9,13),(8,9,14),(8,9,15),(8,9,16),(8,9,17),(8,9,18),(8,9,19),(8,9,20),(8,9,21),(8,9,22),(8,9,23),(8,9,24),(8,9,25),(8,9,26),(8,9,27),(8,9,28),(8,9,31),(8,9,32),(8,9,33),(8,9,34),(8,9,35),(8,9,36),(8,9,37),(8,9,38),(8,9,39),(8,9,40),(8,9,41),(8,9,42),(8,9,43),(8,9,44),(8,9,45),(8,9,46),(8,9,47),(8,9,48),(8,9,49),(8,9,50),(8,9,51),(8,9,52),(8,9,53),(8,9,54),(8,9,55),(8,9,56),(8,9,57),(8,9,58),(8,9,59),(8,9,60),(8,9,61),(8,9,62),(8,9,64),(8,9,65),(8,9,66),(8,9,69),(8,9,70),(8,9,71),(8,9,72),(8,9,73),(8,9,74),(8,9,76),(8,9,77),(8,9,78),(8,9,83),(8,9,84),(8,9,85),(8,9,86),(8,9,87),(8,9,88),(8,9,89),(8,9,90),(8,9,91),(8,9,92),(8,9,96),(8,9,97),(8,9,99),(8,9,105),(8,9,106),(8,9,107),(8,9,108),(8,9,109),(8,9,110),(8,9,111),(8,9,114),(8,9,115),(8,9,117),(8,9,119),(8,9,120),(8,9,121),(8,9,122),(8,9,123),(8,9,124),(8,9,125),(8,9,126),(8,9,127),(8,9,128),(8,9,129),(8,9,130),(8,9,131),(8,9,132),(8,9,133),(8,9,134),(8,9,135),(8,9,136),(8,9,137),(8,9,138),(8,9,139),(8,9,140),(8,9,141),(8,9,142),(8,9,143),(8,9,144),(8,9,145),(8,9,146),(8,9,154),(8,9,155),(8,9,156),(8,9,157),(8,9,158),(8,9,159),(8,9,160),(8,9,162),(8,9,163),(8,9,164),(8,9,165),(8,9,166),(8,9,167),(8,9,168),(8,9,169),(8,9,170),(8,9,171),(8,9,172),(8,9,173),(8,9,174),(8,9,175),(8,9,176),(8,9,177),(8,9,178),(9,8,1),(9,8,2),(9,8,3),(9,8,4),(9,8,5),(9,8,6),(9,8,7),(9,8,8),(9,8,9),(9,8,10),(9,8,11),(9,8,12),(9,8,13),(9,8,14),(9,8,15),(9,8,16),(9,8,17),(9,8,18),(9,8,19),(9,8,20),(9,8,21),(9,8,22),(9,8,23),(9,8,24),(9,8,25),(9,8,26),(9,8,27),(9,8,28),(9,8,31),(9,8,32),(9,8,33),(9,8,34),(9,8,35),(9,8,36),(9,8,37),(9,8,38),(9,8,39),(9,8,40),(9,8,41),(9,8,42),(9,8,43),(9,8,44),(9,8,45),(9,8,46),(9,8,47),(9,8,48),(9,8,49),(9,8,50),(9,8,51),(9,8,52),(9,8,53),(9,8,54),(9,8,55),(9,8,56),(9,8,57),(9,8,58),(9,8,59),(9,8,60),(9,8,61),(9,8,62),(9,8,64),(9,8,65),(9,8,66),(9,8,69),(9,8,70),(9,8,71),(9,8,72),(9,8,73),(9,8,74),(9,8,76),(9,8,77),(9,8,78),(9,8,83),(9,8,84),(9,8,85),(9,8,86),(9,8,87),(9,8,88),(9,8,89),(9,8,90),(9,8,91),(9,8,92),(9,8,93),(9,8,94),(9,8,95),(9,8,96),(9,8,97),(9,8,99),(9,8,105),(9,8,106),(9,8,107),(9,8,108),(9,8,109),(9,8,110),(9,8,111),(9,8,114),(9,8,115),(9,8,117),(9,8,119),(9,8,120),(9,8,121),(9,8,122),(9,8,123),(9,8,124),(9,8,125),(9,8,126),(9,8,127),(9,8,128),(9,8,129),(9,8,130),(9,8,131),(9,8,132),(9,8,133),(9,8,134),(9,8,135),(9,8,136),(9,8,137),(9,8,138),(9,8,139),(9,8,140),(9,8,141),(9,8,142),(9,8,143),(9,8,144),(9,8,145),(9,8,146),(9,8,154),(9,8,155),(9,8,156),(9,8,157),(9,8,158),(9,8,159),(9,8,160),(9,8,162),(9,8,163),(9,8,164),(9,8,165),(9,8,166),(9,8,167),(9,8,168),(9,8,169),(9,8,170),(9,8,171),(9,8,172),(9,8,173),(9,8,174),(9,8,175),(9,8,176),(9,8,177),(9,8,178),(9,14,5),(9,14,12),(9,14,14),(9,14,29),(9,14,30),(9,14,37),(9,14,44),(9,14,46),(9,14,51),(9,14,58),(9,14,60),(9,14,62),(9,14,63),(9,14,68),(9,14,71),(9,14,96),(9,14,100),(9,14,110),(9,14,117),(9,14,121),(9,14,127),(9,14,131),(9,14,156),(9,14,159),(9,14,160),(9,14,169),(9,14,170),(9,14,171),(9,14,179),(9,15,5),(9,15,12),(9,15,21),(9,15,22),(9,15,37),(9,15,44),(9,15,46),(9,15,51),(9,15,55),(9,15,58),(9,15,60),(9,15,62),(9,15,63),(9,15,67),(9,15,68),(9,15,71),(9,15,85),(9,15,86),(9,15,90),(9,15,91),(9,15,93),(9,15,94),(9,15,96),(9,15,101),(9,15,108),(9,15,110),(9,15,115),(9,15,117),(9,15,119),(9,15,120),(9,15,121),(9,15,127),(9,15,131),(9,15,156),(9,15,159),(9,15,160),(9,15,169),(9,15,170),(9,15,171),(9,15,179),(10,5,1),(10,5,2),(10,5,3),(10,5,4),(10,5,5),(10,5,6),(10,5,7),(10,5,8),(10,5,9),(10,5,10),(10,5,11),(10,5,12),(10,5,13),(10,5,14),(10,5,15),(10,5,16),(10,5,17),(10,5,18),(10,5,19),(10,5,20),(10,5,21),(10,5,22),(10,5,23),(10,5,24),(10,5,25),(10,5,26),(10,5,27),(10,5,28),(10,5,31),(10,5,32),(10,5,33),(10,5,34),(10,5,35),(10,5,36),(10,5,37),(10,5,38),(10,5,39),(10,5,41),(10,5,42),(10,5,44),(10,5,46),(10,5,48),(10,5,49),(10,5,50),(10,5,51),(10,5,52),(10,5,53),(10,5,54),(10,5,55),(10,5,56),(10,5,57),(10,5,58),(10,5,59),(10,5,60),(10,5,61),(10,5,62),(10,5,64),(10,5,69),(10,5,70),(10,5,71),(10,5,72),(10,5,73),(10,5,74),(10,5,75),(10,5,76),(10,5,77),(10,5,78),(10,5,83),(10,5,84),(10,5,85),(10,5,86),(10,5,87),(10,5,88),(10,5,89),(10,5,90),(10,5,91),(10,5,92),(10,5,93),(10,5,94),(10,5,95),(10,5,96),(10,5,97),(10,5,99),(10,5,105),(10,5,106),(10,5,107),(10,5,108),(10,5,109),(10,5,110),(10,5,111),(10,5,114),(10,5,115),(10,5,117),(10,5,119),(10,5,128),(10,5,129),(10,5,130),(10,5,131),(10,5,132),(10,5,133),(10,5,134),(10,5,135),(10,5,136),(10,5,137),(10,5,138),(10,5,139),(10,5,140),(10,5,141),(10,5,142),(10,5,143),(10,5,144),(10,5,145),(10,5,146),(10,5,154),(10,5,155),(10,5,156),(10,5,157),(10,5,158),(10,5,159),(10,5,160),(10,5,162),(10,5,163),(10,5,164),(10,5,165),(10,5,166),(10,5,167),(10,5,168),(10,5,169),(10,5,170),(10,5,171),(10,5,172),(10,5,173),(10,5,174),(10,5,175),(10,5,176),(10,5,177),(10,5,178),(10,6,5),(10,6,12),(10,6,14),(10,6,29),(10,6,30),(10,6,37),(10,6,44),(10,6,46),(10,6,51),(10,6,58),(10,6,60),(10,6,62),(10,6,63),(10,6,71),(10,6,96),(10,6,100),(10,6,110),(10,6,115),(10,6,117),(10,6,119),(10,6,131),(10,6,156),(10,6,159),(10,6,160),(10,6,169),(10,6,170),(10,6,171),(10,6,179),(10,7,5),(10,7,12),(10,7,14),(10,7,29),(10,7,30),(10,7,37),(10,7,44),(10,7,46),(10,7,51),(10,7,58),(10,7,60),(10,7,62),(10,7,63),(10,7,71),(10,7,85),(10,7,86),(10,7,91),(10,7,93),(10,7,94),(10,7,96),(10,7,100),(10,7,110),(10,7,115),(10,7,117),(10,7,119),(10,7,154),(10,7,155),(10,7,159),(10,7,160),(10,7,180),(10,13,5),(10,13,12),(10,13,14),(10,13,29),(10,13,30),(10,13,37),(10,13,44),(10,13,46),(10,13,51),(10,13,58),(10,13,60),(10,13,62),(10,13,63),(10,13,71),(10,13,85),(10,13,86),(10,13,91),(10,13,93),(10,13,94),(10,13,96),(10,13,100),(10,13,110),(10,13,115),(10,13,117),(10,13,119),(10,13,159),(10,13,160),(10,13,177),(11,10,1),(11,10,2),(11,10,3),(11,10,4),(11,10,5),(11,10,6),(11,10,7),(11,10,8),(11,10,9),(11,10,10),(11,10,11),(11,10,12),(11,10,13),(11,10,14),(11,10,15),(11,10,16),(11,10,17),(11,10,18),(11,10,19),(11,10,20),(11,10,21),(11,10,22),(11,10,23),(11,10,24),(11,10,25),(11,10,26),(11,10,27),(11,10,28),(11,10,31),(11,10,32),(11,10,33),(11,10,34),(11,10,35),(11,10,36),(11,10,37),(11,10,38),(11,10,39),(11,10,41),(11,10,42),(11,10,44),(11,10,46),(11,10,48),(11,10,49),(11,10,50),(11,10,51),(11,10,52),(11,10,53),(11,10,56),(11,10,57),(11,10,58),(11,10,59),(11,10,60),(11,10,61),(11,10,62),(11,10,64),(11,10,69),(11,10,70),(11,10,71),(11,10,72),(11,10,73),(11,10,74),(11,10,75),(11,10,76),(11,10,77),(11,10,78),(11,10,83),(11,10,84),(11,10,99),(11,10,105),(11,10,106),(11,10,107),(11,10,108),(11,10,109),(11,10,110),(11,10,111),(11,10,114),(11,10,115),(11,10,117),(11,10,119),(11,10,128),(11,10,129),(11,10,130),(11,10,131),(11,10,132),(11,10,133),(11,10,134),(11,10,135),(11,10,136),(11,10,137),(11,10,138),(11,10,139),(11,10,140),(11,10,141),(11,10,142),(11,10,143),(11,10,144),(11,10,145),(11,10,146),(11,10,154),(11,10,155),(11,10,156),(11,10,157),(11,10,158),(11,10,159),(11,10,160),(11,10,162),(11,10,163),(11,10,164),(11,10,165),(11,10,166),(11,10,167),(11,10,168),(11,10,169),(11,10,170),(11,10,171),(11,10,172),(11,10,173),(11,10,174),(11,10,175),(11,10,176),(11,10,177),(11,10,178),(11,11,1),(11,11,2),(11,11,3),(11,11,4),(11,11,5),(11,11,6),(11,11,7),(11,11,8),(11,11,9),(11,11,10),(11,11,11),(11,11,12),(11,11,13),(11,11,14),(11,11,15),(11,11,16),(11,11,17),(11,11,18),(11,11,19),(11,11,20),(11,11,21),(11,11,22),(11,11,23),(11,11,24),(11,11,25),(11,11,26),(11,11,27),(11,11,28),(11,11,31),(11,11,32),(11,11,33),(11,11,34),(11,11,35),(11,11,36),(11,11,37),(11,11,38),(11,11,39),(11,11,41),(11,11,42),(11,11,44),(11,11,46),(11,11,48),(11,11,49),(11,11,50),(11,11,51),(11,11,52),(11,11,53),(11,11,56),(11,11,57),(11,11,58),(11,11,59),(11,11,60),(11,11,61),(11,11,62),(11,11,64),(11,11,69),(11,11,70),(11,11,71),(11,11,72),(11,11,73),(11,11,74),(11,11,75),(11,11,76),(11,11,77),(11,11,78),(11,11,83),(11,11,84),(11,11,99),(11,11,105),(11,11,106),(11,11,107),(11,11,108),(11,11,109),(11,11,110),(11,11,111),(11,11,114),(11,11,115),(11,11,117),(11,11,119),(11,11,128),(11,11,129),(11,11,130),(11,11,131),(11,11,132),(11,11,133),(11,11,134),(11,11,135),(11,11,136),(11,11,137),(11,11,138),(11,11,139),(11,11,140),(11,11,141),(11,11,142),(11,11,143),(11,11,144),(11,11,145),(11,11,146),(11,11,154),(11,11,155),(11,11,156),(11,11,157),(11,11,158),(11,11,159),(11,11,160),(11,11,162),(11,11,163),(11,11,164),(11,11,165),(11,11,166),(11,11,167),(11,11,168),(11,11,169),(11,11,170),(11,11,171),(11,11,172),(11,11,173),(11,11,174),(11,11,175),(11,11,176),(11,11,177),(11,11,178),(12,3,5),(12,3,12),(12,3,14),(12,3,37),(12,3,51),(12,3,68),(12,3,97),(12,3,100),(12,3,110),(12,3,121),(12,3,127),(12,3,131),(12,3,156),(12,3,159),(12,3,160),(12,3,169),(12,3,170),(12,3,171),(12,3,179),(12,9,2),(12,9,3),(12,9,4),(12,9,5),(12,9,6),(12,9,7),(12,9,8),(12,9,9),(12,9,10),(12,9,11),(12,9,12),(12,9,13),(12,9,14),(12,9,34),(12,9,35),(12,9,36),(12,9,37),(12,9,38),(12,9,39),(12,9,48),(12,9,49),(12,9,50),(12,9,51),(12,9,52),(12,9,53),(12,9,55),(12,9,65),(12,9,66),(12,9,99),(12,9,109),(12,9,110),(12,9,111),(12,9,120),(12,9,121),(12,9,122),(12,9,123),(12,9,124),(12,9,125),(12,9,126),(12,9,127),(12,9,128),(12,9,129),(12,9,130),(12,9,131),(12,9,132),(12,9,133),(12,9,134),(12,9,135),(12,9,136),(12,9,137),(12,9,138),(12,9,139),(12,9,140),(12,9,141),(12,9,142),(12,9,143),(12,9,144),(12,9,145),(12,9,146),(12,9,154),(12,9,155),(12,9,156),(12,9,157),(12,9,158),(12,9,159),(12,9,160),(12,9,162),(12,9,163),(12,9,164),(12,9,165),(12,9,166),(12,9,167),(12,9,168),(12,9,169),(12,9,170),(12,9,171),(12,9,172),(12,9,173),(12,9,174),(12,9,175),(12,9,176),(12,9,177),(12,9,178),(13,8,2),(13,8,3),(13,8,4),(13,8,5),(13,8,6),(13,8,7),(13,8,8),(13,8,9),(13,8,10),(13,8,11),(13,8,12),(13,8,13),(13,8,14),(13,8,34),(13,8,35),(13,8,36),(13,8,37),(13,8,38),(13,8,39),(13,8,48),(13,8,49),(13,8,50),(13,8,51),(13,8,52),(13,8,53),(13,8,55),(13,8,65),(13,8,66),(13,8,85),(13,8,86),(13,8,87),(13,8,88),(13,8,89),(13,8,90),(13,8,93),(13,8,99),(13,8,109),(13,8,110),(13,8,111),(13,8,120),(13,8,121),(13,8,122),(13,8,123),(13,8,124),(13,8,125),(13,8,126),(13,8,127),(13,8,128),(13,8,129),(13,8,130),(13,8,131),(13,8,132),(13,8,133),(13,8,134),(13,8,135),(13,8,136),(13,8,137),(13,8,138),(13,8,139),(13,8,140),(13,8,141),(13,8,142),(13,8,143),(13,8,144),(13,8,145),(13,8,146),(13,8,154),(13,8,155),(13,8,156),(13,8,157),(13,8,158),(13,8,159),(13,8,160),(13,8,162),(13,8,163),(13,8,164),(13,8,165),(13,8,166),(13,8,167),(13,8,168),(13,8,169),(13,8,170),(13,8,171),(13,8,172),(13,8,173),(13,8,174),(13,8,175),(13,8,176),(13,8,177),(13,8,178),(13,14,5),(13,14,12),(13,14,14),(13,14,37),(13,14,51),(13,14,68),(13,14,92),(13,14,97),(13,14,100),(13,14,110),(13,14,121),(13,14,127),(13,14,131),(13,14,156),(13,14,159),(13,14,160),(13,14,169),(13,14,170),(13,14,171),(13,14,179),(13,15,2),(13,15,3),(13,15,4),(13,15,5),(13,15,6),(13,15,7),(13,15,8),(13,15,9),(13,15,10),(13,15,11),(13,15,12),(13,15,13),(13,15,34),(13,15,35),(13,15,36),(13,15,37),(13,15,38),(13,15,39),(13,15,48),(13,15,49),(13,15,50),(13,15,51),(13,15,52),(13,15,53),(13,15,55),(13,15,67),(13,15,85),(13,15,86),(13,15,90),(13,15,92),(13,15,93),(13,15,97),(13,15,101),(13,15,110),(13,15,120),(13,15,121),(13,15,127),(13,15,131),(13,15,156),(13,15,159),(13,15,160),(13,15,169),(13,15,170),(13,15,171),(13,15,179),(14,5,1),(14,5,2),(14,5,3),(14,5,4),(14,5,5),(14,5,6),(14,5,7),(14,5,8),(14,5,9),(14,5,10),(14,5,11),(14,5,12),(14,5,13),(14,5,14),(14,5,15),(14,5,16),(14,5,17),(14,5,18),(14,5,19),(14,5,20),(14,5,21),(14,5,22),(14,5,23),(14,5,24),(14,5,25),(14,5,26),(14,5,27),(14,5,28),(14,5,31),(14,5,32),(14,5,33),(14,5,34),(14,5,35),(14,5,36),(14,5,37),(14,5,38),(14,5,39),(14,5,41),(14,5,42),(14,5,44),(14,5,46),(14,5,48),(14,5,49),(14,5,50),(14,5,51),(14,5,52),(14,5,53),(14,5,54),(14,5,55),(14,5,56),(14,5,57),(14,5,58),(14,5,59),(14,5,60),(14,5,61),(14,5,62),(14,5,64),(14,5,69),(14,5,70),(14,5,71),(14,5,73),(14,5,74),(14,5,75),(14,5,76),(14,5,77),(14,5,78),(14,5,83),(14,5,84),(14,5,85),(14,5,86),(14,5,87),(14,5,88),(14,5,89),(14,5,90),(14,5,99),(14,5,105),(14,5,106),(14,5,107),(14,5,108),(14,5,109),(14,5,110),(14,5,111),(14,5,114),(14,5,115),(14,5,117),(14,5,119),(14,5,128),(14,5,129),(14,5,130),(14,5,131),(14,5,132),(14,5,133),(14,5,134),(14,5,135),(14,5,136),(14,5,137),(14,5,138),(14,5,139),(14,5,140),(14,5,141),(14,5,142),(14,5,143),(14,5,144),(14,5,145),(14,5,146),(14,5,154),(14,5,155),(14,5,156),(14,5,157),(14,5,158),(14,5,159),(14,5,160),(14,5,162),(14,5,163),(14,5,164),(14,5,165),(14,5,166),(14,5,167),(14,5,168),(14,5,169),(14,5,170),(14,5,171),(14,5,172),(14,5,173),(14,5,174),(14,5,175),(14,5,176),(14,5,177),(14,5,178),(14,6,5),(14,6,12),(14,6,14),(14,6,29),(14,6,30),(14,6,37),(14,6,44),(14,6,46),(14,6,51),(14,6,58),(14,6,60),(14,6,62),(14,6,63),(14,6,71),(14,6,100),(14,6,110),(14,6,115),(14,6,117),(14,6,119),(14,6,131),(14,6,156),(14,6,159),(14,6,160),(14,6,169),(14,6,170),(14,6,171),(14,6,179),(14,7,5),(14,7,12),(14,7,14),(14,7,29),(14,7,30),(14,7,37),(14,7,44),(14,7,46),(14,7,51),(14,7,58),(14,7,60),(14,7,62),(14,7,63),(14,7,71),(14,7,85),(14,7,86),(14,7,100),(14,7,110),(14,7,115),(14,7,117),(14,7,119),(14,7,154),(14,7,155),(14,7,159),(14,7,160),(14,7,180),(14,13,5),(14,13,12),(14,13,14),(14,13,29),(14,13,30),(14,13,37),(14,13,44),(14,13,46),(14,13,51),(14,13,58),(14,13,60),(14,13,62),(14,13,63),(14,13,71),(14,13,85),(14,13,86),(14,13,100),(14,13,110),(14,13,115),(14,13,117),(14,13,119),(14,13,159),(14,13,160),(14,13,177),(15,1,51),(15,2,51),(16,1,51),(16,2,48),(16,2,49),(16,2,50),(16,2,51),(16,2,52),(16,2,53),(17,1,5),(17,2,5),(18,12,5),(18,12,37),(18,12,46),(18,12,51),(18,12,60),(18,12,71),(18,12,110),(19,1,110),(19,2,110),(20,1,110),(20,2,110),(21,1,110),(21,2,110),(22,3,5),(22,3,12),(22,3,14),(22,3,37),(22,3,44),(22,3,46),(22,3,51),(22,3,58),(22,3,60),(22,3,62),(22,3,63),(22,3,68),(22,3,71),(22,3,97),(22,3,110),(22,3,115),(22,3,117),(22,3,119),(22,3,121),(22,3,127),(22,9,2),(22,9,3),(22,9,4),(22,9,5),(22,9,6),(22,9,7),(22,9,8),(22,9,9),(22,9,10),(22,9,11),(22,9,12),(22,9,13),(22,9,14),(22,9,34),(22,9,35),(22,9,36),(22,9,37),(22,9,38),(22,9,39),(22,9,41),(22,9,42),(22,9,43),(22,9,44),(22,9,45),(22,9,46),(22,9,47),(22,9,48),(22,9,49),(22,9,50),(22,9,51),(22,9,52),(22,9,53),(22,9,55),(22,9,56),(22,9,57),(22,9,58),(22,9,59),(22,9,60),(22,9,61),(22,9,62),(22,9,64),(22,9,65),(22,9,66),(22,9,69),(22,9,70),(22,9,71),(22,9,73),(22,9,74),(22,9,76),(22,9,77),(22,9,78),(22,9,83),(22,9,84),(22,9,91),(22,9,92),(22,9,105),(22,9,106),(22,9,107),(22,9,108),(22,9,109),(22,9,110),(22,9,111),(22,9,114),(22,9,115),(22,9,117),(22,9,119),(22,9,120),(22,9,121),(22,9,122),(22,9,123),(22,9,124),(22,9,125),(22,9,126),(22,9,127),(23,4,106),(24,5,147),(24,5,148),(24,5,149),(24,5,150),(24,5,151),(24,5,152),(24,6,153),(24,7,151),(24,13,151),(25,8,147),(25,8,148),(25,8,149),(25,8,150),(25,8,151),(25,8,152),(25,14,153),(25,15,147),(25,15,148),(25,15,149),(25,15,150),(25,15,151),(25,15,152),(26,3,153),(26,9,147),(26,9,148),(26,9,149),(26,9,150),(26,9,151),(26,9,152),(30,10,1),(30,10,2),(30,10,3),(30,10,4),(30,10,5),(30,10,6),(30,10,7),(30,10,8),(30,10,9),(30,10,10),(30,10,11),(30,10,12),(30,10,13),(30,10,14),(30,10,15),(30,10,16),(30,10,17),(30,10,18),(30,10,19),(30,10,20),(30,10,21),(30,10,22),(30,10,23),(30,10,24),(30,10,25),(30,10,26),(30,10,27),(30,10,28),(30,10,31),(30,10,32),(30,10,33),(30,10,34),(30,10,35),(30,10,36),(30,10,37),(30,10,38),(30,10,39),(30,10,41),(30,10,42),(30,10,44),(30,10,46),(30,10,48),(30,10,49),(30,10,50),(30,10,51),(30,10,52),(30,10,53),(30,10,56),(30,10,57),(30,10,58),(30,10,59),(30,10,60),(30,10,61),(30,10,62),(30,10,64),(30,10,69),(30,10,70),(30,10,71),(30,10,72),(30,10,73),(30,10,74),(30,10,75),(30,10,76),(30,10,77),(30,10,78),(30,10,83),(30,10,84),(30,10,99),(30,10,105),(30,10,106),(30,10,107),(30,10,108),(30,10,109),(30,10,110),(30,10,111),(30,10,114),(30,10,115),(30,10,117),(30,10,119),(30,10,128),(30,10,129),(30,10,130),(30,10,131),(30,10,132),(30,10,133),(30,10,134),(30,10,135),(30,10,136),(30,10,137),(30,10,138),(30,10,139),(30,10,140),(30,10,141),(30,10,142),(30,10,143),(30,10,144),(30,10,145),(30,10,146),(30,10,154),(30,10,155),(30,10,156),(30,10,157),(30,10,158),(30,10,159),(30,10,160),(30,10,162),(30,10,163),(30,10,164),(30,10,165),(30,10,166),(30,10,167),(30,10,168),(30,10,169),(30,10,170),(30,10,171),(30,10,172),(30,10,173),(30,10,174),(30,10,175),(30,10,176),(30,10,177),(30,10,178),(30,11,1),(30,11,2),(30,11,3),(30,11,4),(30,11,5),(30,11,6),(30,11,7),(30,11,8),(30,11,9),(30,11,10),(30,11,11),(30,11,12),(30,11,13),(30,11,14),(30,11,15),(30,11,16),(30,11,17),(30,11,18),(30,11,19),(30,11,20),(30,11,21),(30,11,22),(30,11,23),(30,11,24),(30,11,25),(30,11,26),(30,11,27),(30,11,28),(30,11,31),(30,11,32),(30,11,33),(30,11,34),(30,11,35),(30,11,36),(30,11,37),(30,11,38),(30,11,39),(30,11,41),(30,11,42),(30,11,44),(30,11,46),(30,11,48),(30,11,49),(30,11,50),(30,11,51),(30,11,52),(30,11,53),(30,11,56),(30,11,57),(30,11,58),(30,11,59),(30,11,60),(30,11,61),(30,11,62),(30,11,64),(30,11,69),(30,11,70),(30,11,71),(30,11,72),(30,11,73),(30,11,74),(30,11,75),(30,11,76),(30,11,77),(30,11,78),(30,11,83),(30,11,84),(30,11,99),(30,11,105),(30,11,106),(30,11,107),(30,11,108),(30,11,109),(30,11,110),(30,11,111),(30,11,114),(30,11,115),(30,11,117),(30,11,119),(30,11,128),(30,11,129),(30,11,130),(30,11,131),(30,11,132),(30,11,133),(30,11,134),(30,11,135),(30,11,136),(30,11,137),(30,11,138),(30,11,139),(30,11,140),(30,11,141),(30,11,142),(30,11,143),(30,11,144),(30,11,145),(30,11,146),(30,11,154),(30,11,155),(30,11,156),(30,11,157),(30,11,158),(30,11,159),(30,11,160),(30,11,162),(30,11,163),(30,11,164),(30,11,165),(30,11,166),(30,11,167),(30,11,168),(30,11,169),(30,11,170),(30,11,171),(30,11,172),(30,11,173),(30,11,174),(30,11,175),(30,11,176),(30,11,177),(30,11,178),(31,3,5),(31,3,12),(31,3,14),(31,3,29),(31,3,30),(31,3,37),(31,3,44),(31,3,46),(31,3,51),(31,3,58),(31,3,60),(31,3,62),(31,3,63),(31,3,68),(31,3,70),(31,3,71),(31,3,91),(31,3,92),(31,3,96),(31,3,100),(31,3,110),(31,3,115),(31,3,117),(31,3,119),(31,3,121),(31,3,127),(31,3,131),(31,3,156),(31,3,159),(31,3,160),(31,3,169),(31,3,170),(31,3,171),(31,3,179),(31,9,1),(31,9,2),(31,9,3),(31,9,4),(31,9,5),(31,9,6),(31,9,7),(31,9,8),(31,9,9),(31,9,10),(31,9,11),(31,9,12),(31,9,13),(31,9,14),(31,9,15),(31,9,16),(31,9,17),(31,9,18),(31,9,19),(31,9,20),(31,9,21),(31,9,22),(31,9,23),(31,9,24),(31,9,25),(31,9,26),(31,9,27),(31,9,28),(31,9,31),(31,9,32),(31,9,33),(31,9,34),(31,9,35),(31,9,36),(31,9,37),(31,9,38),(31,9,39),(31,9,40),(31,9,41),(31,9,42),(31,9,43),(31,9,44),(31,9,45),(31,9,46),(31,9,47),(31,9,48),(31,9,49),(31,9,50),(31,9,51),(31,9,52),(31,9,53),(31,9,54),(31,9,55),(31,9,56),(31,9,57),(31,9,58),(31,9,59),(31,9,60),(31,9,61),(31,9,62),(31,9,64),(31,9,65),(31,9,66),(31,9,69),(31,9,70),(31,9,71),(31,9,72),(31,9,73),(31,9,74),(31,9,76),(31,9,77),(31,9,78),(31,9,83),(31,9,84),(31,9,85),(31,9,86),(31,9,87),(31,9,88),(31,9,89),(31,9,90),(31,9,91),(31,9,92),(31,9,96),(31,9,97),(31,9,99),(31,9,105),(31,9,106),(31,9,107),(31,9,108),(31,9,109),(31,9,110),(31,9,111),(31,9,114),(31,9,115),(31,9,117),(31,9,119),(31,9,120),(31,9,121),(31,9,122),(31,9,123),(31,9,124),(31,9,125),(31,9,126),(31,9,127),(31,9,128),(31,9,129),(31,9,130),(31,9,131),(31,9,132),(31,9,133),(31,9,134),(31,9,135),(31,9,136),(31,9,137),(31,9,138),(31,9,139),(31,9,140),(31,9,141),(31,9,142),(31,9,143),(31,9,144),(31,9,145),(31,9,146),(31,9,154),(31,9,155),(31,9,156),(31,9,157),(31,9,158),(31,9,159),(31,9,160),(31,9,162),(31,9,163),(31,9,164),(31,9,165),(31,9,166),(31,9,167),(31,9,168),(31,9,169),(31,9,170),(31,9,171),(31,9,172),(31,9,173),(31,9,174),(31,9,175),(31,9,176),(31,9,177),(31,9,178),(32,1,51),(33,1,51),(34,1,51),(35,3,5),(35,3,37),(35,3,44),(35,3,46),(35,3,51),(35,3,58),(35,3,60),(35,3,62),(35,3,68),(35,3,71),(35,3,110),(35,3,115),(35,3,117),(35,3,119),(35,3,121),(35,9,2),(35,9,3),(35,9,4),(35,9,5),(35,9,6),(35,9,7),(35,9,8),(35,9,34),(35,9,35),(35,9,36),(35,9,37),(35,9,38),(35,9,39),(35,9,41),(35,9,42),(35,9,43),(35,9,44),(35,9,45),(35,9,46),(35,9,47),(35,9,48),(35,9,49),(35,9,50),(35,9,51),(35,9,52),(35,9,53),(35,9,55),(35,9,56),(35,9,57),(35,9,58),(35,9,59),(35,9,60),(35,9,61),(35,9,62),(35,9,64),(35,9,65),(35,9,66),(35,9,69),(35,9,70),(35,9,71),(35,9,83),(35,9,84),(35,9,106),(35,9,107),(35,9,108),(35,9,109),(35,9,110),(35,9,111),(35,9,114),(35,9,115),(35,9,117),(35,9,119),(35,9,120),(35,9,121),(36,8,1),(36,8,2),(36,8,3),(36,8,4),(36,8,5),(36,8,6),(36,8,7),(36,8,8),(36,8,9),(36,8,10),(36,8,11),(36,8,12),(36,8,13),(36,8,14),(36,8,15),(36,8,16),(36,8,17),(36,8,18),(36,8,19),(36,8,20),(36,8,21),(36,8,22),(36,8,23),(36,8,24),(36,8,25),(36,8,26),(36,8,27),(36,8,28),(36,8,31),(36,8,32),(36,8,33),(36,8,34),(36,8,35),(36,8,36),(36,8,37),(36,8,38),(36,8,39),(36,8,40),(36,8,41),(36,8,42),(36,8,43),(36,8,44),(36,8,45),(36,8,46),(36,8,47),(36,8,48),(36,8,49),(36,8,50),(36,8,51),(36,8,52),(36,8,53),(36,8,54),(36,8,55),(36,8,56),(36,8,57),(36,8,58),(36,8,59),(36,8,60),(36,8,61),(36,8,62),(36,8,64),(36,8,65),(36,8,66),(36,8,69),(36,8,70),(36,8,71),(36,8,72),(36,8,73),(36,8,74),(36,8,76),(36,8,77),(36,8,78),(36,8,83),(36,8,84),(36,8,85),(36,8,86),(36,8,87),(36,8,88),(36,8,89),(36,8,90),(36,8,91),(36,8,92),(36,8,93),(36,8,94),(36,8,95),(36,8,96),(36,8,97),(36,8,99),(36,8,105),(36,8,106),(36,8,107),(36,8,108),(36,8,109),(36,8,110),(36,8,111),(36,8,114),(36,8,115),(36,8,117),(36,8,119),(36,8,120),(36,8,121),(36,8,122),(36,8,123),(36,8,124),(36,8,125),(36,8,126),(36,8,127),(36,8,128),(36,8,129),(36,8,130),(36,8,131),(36,8,132),(36,8,133),(36,8,134),(36,8,135),(36,8,136),(36,8,137),(36,8,138),(36,8,139),(36,8,140),(36,8,141),(36,8,142),(36,8,143),(36,8,144),(36,8,145),(36,8,146),(36,8,154),(36,8,155),(36,8,156),(36,8,157),(36,8,158),(36,8,159),(36,8,160),(36,8,162),(36,8,163),(36,8,164),(36,8,165),(36,8,166),(36,8,167),(36,8,168),(36,8,169),(36,8,170),(36,8,171),(36,8,172),(36,8,173),(36,8,174),(36,8,175),(36,8,176),(36,8,177),(36,8,178),(36,14,5),(36,14,12),(36,14,14),(36,14,29),(36,14,30),(36,14,37),(36,14,44),(36,14,46),(36,14,51),(36,14,58),(36,14,60),(36,14,62),(36,14,63),(36,14,68),(36,14,71),(36,14,96),(36,14,100),(36,14,110),(36,14,117),(36,14,121),(36,14,127),(36,14,131),(36,14,156),(36,14,159),(36,14,160),(36,14,169),(36,14,170),(36,14,171),(36,14,179),(36,15,5),(36,15,12),(36,15,21),(36,15,22),(36,15,37),(36,15,44),(36,15,46),(36,15,51),(36,15,55),(36,15,58),(36,15,60),(36,15,62),(36,15,63),(36,15,67),(36,15,68),(36,15,71),(36,15,85),(36,15,86),(36,15,90),(36,15,91),(36,15,93),(36,15,94),(36,15,96),(36,15,101),(36,15,108),(36,15,110),(36,15,115),(36,15,117),(36,15,119),(36,15,120),(36,15,121),(36,15,127),(36,15,131),(36,15,156),(36,15,159),(36,15,160),(36,15,169),(36,15,170),(36,15,171),(36,15,179),(37,8,2),(37,8,3),(37,8,4),(37,8,5),(37,8,6),(37,8,7),(37,8,8),(37,8,9),(37,8,10),(37,8,11),(37,8,12),(37,8,13),(37,8,14),(37,8,34),(37,8,35),(37,8,36),(37,8,37),(37,8,38),(37,8,39),(37,8,48),(37,8,49),(37,8,50),(37,8,51),(37,8,52),(37,8,53),(37,8,55),(37,8,65),(37,8,66),(37,8,85),(37,8,86),(37,8,87),(37,8,88),(37,8,89),(37,8,90),(37,8,93),(37,8,99),(37,8,109),(37,8,110),(37,8,111),(37,8,120),(37,8,121),(37,8,122),(37,8,123),(37,8,124),(37,8,125),(37,8,126),(37,8,127),(37,8,128),(37,8,129),(37,8,130),(37,8,131),(37,8,132),(37,8,133),(37,8,134),(37,8,135),(37,8,136),(37,8,137),(37,8,138),(37,8,139),(37,8,140),(37,8,141),(37,8,142),(37,8,143),(37,8,144),(37,8,145),(37,8,146),(37,8,154),(37,8,155),(37,8,156),(37,8,157),(37,8,158),(37,8,159),(37,8,160),(37,8,162),(37,8,163),(37,8,164),(37,8,165),(37,8,166),(37,8,167),(37,8,168),(37,8,169),(37,8,170),(37,8,171),(37,8,172),(37,8,173),(37,8,174),(37,8,175),(37,8,176),(37,8,177),(37,8,178),(37,14,5),(37,14,12),(37,14,14),(37,14,37),(37,14,51),(37,14,68),(37,14,92),(37,14,97),(37,14,100),(37,14,110),(37,14,121),(37,14,127),(37,14,131),(37,14,156),(37,14,159),(37,14,160),(37,14,169),(37,14,170),(37,14,171),(37,14,179),(37,15,2),(37,15,3),(37,15,4),(37,15,5),(37,15,6),(37,15,7),(37,15,8),(37,15,9),(37,15,10),(37,15,11),(37,15,12),(37,15,13),(37,15,34),(37,15,35),(37,15,36),(37,15,37),(37,15,38),(37,15,39),(37,15,48),(37,15,49),(37,15,50),(37,15,51),(37,15,52),(37,15,53),(37,15,55),(37,15,67),(37,15,85),(37,15,86),(37,15,90),(37,15,92),(37,15,93),(37,15,97),(37,15,101),(37,15,110),(37,15,120),(37,15,121),(37,15,127),(37,15,131),(37,15,156),(37,15,159),(37,15,160),(37,15,169),(37,15,170),(37,15,171),(37,15,179);
/*!40000 ALTER TABLE `SAKAI_REALM_RL_FN` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_REALM_RL_GR`
--

DROP TABLE IF EXISTS `SAKAI_REALM_RL_GR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_REALM_RL_GR` (
  `REALM_KEY` int(11) NOT NULL,
  `USER_ID` varchar(99) NOT NULL,
  `ROLE_KEY` int(11) NOT NULL,
  `ACTIVE` char(1) DEFAULT NULL,
  `PROVIDED` char(1) DEFAULT NULL,
  PRIMARY KEY (`REALM_KEY`,`USER_ID`),
  KEY `FK_SAKAI_REALM_RL_GR_REALM` (`REALM_KEY`),
  KEY `FK_SAKAI_REALM_RL_GR_ROLE` (`ROLE_KEY`),
  KEY `IE_SAKAI_REALM_RL_GR_ACT` (`ACTIVE`),
  KEY `IE_SAKAI_REALM_RL_GR_USR` (`USER_ID`),
  KEY `IE_SAKAI_REALM_RL_GR_PRV` (`PROVIDED`),
  KEY `SAKAI_REALM_RL_GR_RAU` (`ROLE_KEY`,`ACTIVE`,`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_REALM_RL_GR`
--

LOCK TABLES `SAKAI_REALM_RL_GR` WRITE;
/*!40000 ALTER TABLE `SAKAI_REALM_RL_GR` DISABLE KEYS */;
INSERT INTO `SAKAI_REALM_RL_GR` VALUES (22,'admin',9,'1','0'),(23,'admin',4,'1','0'),(30,'admin',10,'1','0'),(31,'admin',9,'1','0'),(35,'d1430d8d-af0b-49e6-8c39-2d253673319a',9,'1','0'),(36,'ec2f730b-65d6-4b11-a4f1-813d989500b0',14,'1','1'),(36,'6d84cc4f-5e31-40a0-825f-0fcc8f9d48d3',14,'1','1'),(36,'62295668-bc06-48e7-9982-c9eae994b3a1',14,'1','1'),(36,'b40da10f-3530-4546-b8bd-41004aadf81e',14,'1','1'),(36,'6b9c318d-7347-4322-afbb-e9b3e734e4fd',14,'1','1'),(36,'bf09a537-3fcc-4032-ab5f-aced1839513a',14,'1','1'),(36,'4b6e2678-e97a-4882-afa8-4bee785e0d55',14,'1','1'),(36,'e1805a1d-4f4d-4769-bf6f-0ceef96a2716',14,'1','1'),(36,'19afe12a-e810-48b1-88f3-3583a9dc786e',14,'1','1'),(36,'874d49d3-d7ef-4ddd-a606-db4a024092d3',14,'1','1'),(36,'38d7bebf-0524-485d-9952-6870eaa22075',14,'1','1'),(36,'a82185d9-0c49-4fe2-a558-de87927740eb',14,'1','1'),(36,'c3ea4458-bb78-46fa-8c70-c953f638ee0c',14,'1','1'),(36,'ee86526c-0087-489e-b553-260e3d07e1fe',14,'1','1'),(36,'ebb7415d-ce79-4c2b-b621-e57076e7452c',14,'1','1'),(36,'af492ecd-a9cb-45dd-9e08-e4892ffce38c',14,'1','1'),(36,'07a7a71d-8feb-427f-99b1-1df351dfb290',14,'1','1'),(36,'7326d09e-0df2-493f-8709-e81cd960b141',14,'1','1'),(36,'b5bbaab7-5e41-4a2a-9497-512eb12adf8e',14,'1','1'),(36,'b356f123-ea78-40ac-9bae-dd18a164d5e5',14,'1','1'),(36,'da0885d7-0ff7-44a4-b68a-ed1397105208',14,'1','1'),(36,'4e3695ee-b1a2-4093-bc60-cc4813aac669',14,'1','1'),(36,'f54853a9-a0a3-42f1-8639-3ec5b09e5401',14,'1','1'),(36,'a01062c6-7eb3-4c29-aab5-1ee72716962e',14,'1','1'),(36,'ee638295-fd3a-4f20-84c8-74934f898159',14,'1','1'),(36,'4ecd482a-e961-4f5a-a927-5c4984e2b0d2',14,'1','1'),(36,'025e9195-08fd-4a76-a142-0dbece7b1984',14,'1','1'),(36,'88ecaf43-a585-4600-bb6b-9cd7517727cd',14,'1','1'),(36,'06f67e74-e7c8-4fe2-8625-7139b496edfc',14,'1','1'),(36,'bc184bd7-f864-4e88-8984-d11bde38e6d7',14,'1','1'),(36,'0c7b9b12-bc79-4da6-b8f6-b18057cbe882',14,'1','1'),(36,'a6c19c10-e4c1-4d9d-8838-a3ff29dbd4ce',14,'1','1'),(36,'69791ed2-7461-42bc-8699-d8be328e2ee1',14,'1','1'),(36,'c19bcc71-53a7-4712-95dd-d0012e9e7592',14,'1','1'),(36,'db1495af-bef1-431d-87fb-ce4ec0d650c3',14,'1','1'),(36,'ebc7f28f-8270-49e5-a86f-75e47fe1c480',14,'1','1'),(36,'35c66078-1217-439b-bb19-41f94e359964',14,'1','1'),(36,'9aecc130-9c34-49b9-80fc-5ffdbb657d73',14,'1','1'),(36,'bc0b9585-18d6-45e0-a132-13010a00350e',14,'1','1'),(36,'cdb9b220-a3ee-4655-93b3-df7e9ba8065f',14,'1','1'),(36,'546be8b2-1ed7-4608-aeaa-9343bee7630e',14,'1','1'),(36,'1db66cb6-37b8-4906-aabc-71d0eef18068',14,'1','1'),(36,'6174bb8a-07e2-4a35-ae7b-add5e474e50a',14,'1','1'),(36,'5386549e-c9e2-403b-bd68-11471ae2af6f',14,'1','1'),(36,'71145325-848d-44ff-8dbc-ff6d8ab04d75',14,'1','1'),(36,'38b0bb81-007f-401a-97ae-53a55cb30bae',14,'1','1'),(36,'3a97f645-fdfc-4544-b281-945333aaaf30',14,'1','1'),(36,'18e2e188-845e-4ce1-a036-19302dcc84cf',14,'1','1'),(36,'7f1a7baf-6f7d-4c99-8c56-ec2a0f2b6839',14,'1','1'),(36,'7d39489a-5fc5-4851-b0b0-99265b81368d',14,'1','1'),(36,'58eee43e-f6aa-40e5-b927-155ae5b30920',14,'1','1'),(36,'27ed9197-681c-4f9d-8b0c-c16b11d7f5e6',14,'1','1'),(36,'fdeb0b4c-7ccd-4864-802a-3e21e18001f8',14,'1','1'),(36,'9ef72519-430d-4ddf-a4c7-5be254ab1e9e',14,'1','1'),(36,'43c71a7f-e4f3-4eef-85a4-7217b03eb0e0',14,'1','1'),(36,'2e930f7d-c4bc-4f9d-bc47-08e10781cc8c',14,'1','1'),(36,'1c0fc61c-458b-4f1c-8e07-94f8ea068172',14,'1','1'),(36,'a352ddcc-c489-4f23-b16a-0243776f18c3',14,'1','1'),(36,'3a3e1a4b-1911-4896-add7-73ea93885d2e',14,'1','1'),(36,'b93d5ba4-c733-4c0c-b106-01b66c5c1244',14,'1','1'),(36,'9cba5865-394e-41a7-8be0-be2209868e09',14,'1','1'),(36,'fb2fe8bb-0924-4a29-a4f5-0ea1e0573247',14,'1','1'),(36,'783c8415-1a2e-4450-8385-7d4a9b690057',14,'1','1'),(36,'94a280a1-495c-409f-813b-78b78f218751',14,'1','1'),(36,'4ea3676d-3fbe-40ed-affe-f46e45bed47c',14,'1','1'),(36,'d1bad65a-5e59-4ef5-af4c-5be4e6c85064',14,'1','1'),(36,'c8996c6a-3368-4d73-95cb-e8be868f9541',14,'1','1'),(36,'ae415b58-6117-4807-9050-a8d630138142',14,'1','1'),(36,'e8276751-f824-4144-8b51-b1344ca9cd55',14,'1','1'),(36,'93b7ec01-39fa-4ac6-a0c7-4087a8960511',14,'1','1'),(36,'bf744747-98d8-4858-8877-5dc9f1dfcdc2',14,'1','1'),(36,'a7607355-0aa0-43b5-b9da-2bae135a42c9',14,'1','1'),(36,'a22b2a23-6495-4084-8442-bc32125fd2a6',14,'1','1'),(36,'36fba6f3-216d-49d7-bf91-add309e764f8',8,'1','1'),(36,'b50e4212-4bc4-4915-bffb-f793103c05ca',14,'1','1'),(36,'33d968c8-265d-4306-8ebc-23e68849a33b',8,'1','1'),(36,'6e680d07-601e-4adb-a581-6f4d301de96a',14,'1','1'),(36,'1f98e91a-6f4f-4f98-9fdc-73dcb64f4206',14,'1','1'),(36,'efd34a54-8d40-48dd-9b75-67e4b0d8cd47',14,'1','1'),(36,'d21c7e23-bda0-47b3-a7c0-495e0ce56ef4',14,'1','1'),(36,'ef69e0f1-1cbd-44d5-a9ec-33b721096a07',14,'1','1'),(36,'f51996fc-ef2d-4d08-bc73-5c369a1f0c7f',14,'1','1'),(36,'e1383061-fac9-496a-aa9b-b036615c03c4',14,'1','1'),(36,'ac239a61-c642-468f-a16a-f6976892b93c',14,'1','1'),(36,'7fbbbd9f-7900-4488-aec7-d9898e11ea2d',14,'1','1'),(36,'e603171a-e7b3-4d84-82f1-5d848351d32b',14,'1','1'),(36,'ff504578-c994-4a89-bb4e-a672ee817064',14,'1','1'),(36,'0648876a-3b02-419b-9b8b-6a232c6d5574',14,'1','1'),(36,'5c6a69e2-3100-4fb2-b6cf-38a9d5bc92e4',14,'1','1'),(36,'8a63abf2-777a-4a19-b1f7-f82763a32b72',14,'1','1'),(36,'b786fba6-2233-4061-9070-143d9fc9da9a',14,'1','1'),(36,'23a4562f-3ad2-4b42-8de7-d92754deb8fc',14,'1','1'),(36,'871b0b76-c679-4df1-9576-90d9f8bc2e7f',14,'1','1'),(36,'00645336-54be-4062-9cb5-f14ef811dabe',14,'1','1'),(36,'f9efede8-9ebe-403a-bf88-53d2a53f867a',14,'1','1'),(36,'admin',8,'1','1'),(36,'e6e892e4-e16e-47c1-aada-6e22e7cb8858',14,'1','1'),(36,'6716a043-275b-4d28-b235-b313445efbef',14,'1','1'),(36,'27377926-4baa-4edd-9455-c331d21d4f35',14,'1','1'),(36,'0689e163-b621-4be1-bd15-0573d81b15d8',14,'1','1'),(36,'b75ade34-fd28-43ae-9b3d-d4441b25621e',14,'1','1'),(36,'cec0bd35-eb6f-4fa7-8210-70ce84a70b43',14,'1','1'),(36,'2340fa22-539c-4da2-8206-827a177e2c43',8,'1','1'),(36,'6ade0933-a316-4e0d-9dbf-942559d45a44',14,'1','1'),(36,'8cbe7f4c-dfc7-4138-b8a2-5bbfe70bc72f',14,'1','1'),(36,'b50bf16e-552c-4d0a-aa9c-a1f5bee65dae',14,'1','1'),(36,'90d1b7fd-a8c4-4061-b9db-6bc1f6fbb830',14,'1','1'),(36,'b8874b5d-6022-4548-af9f-cf3e9ed2ea76',14,'1','1'),(36,'83458e5e-f20c-4213-bf71-54c003c35af1',14,'1','1'),(36,'dff0baa1-64bf-4f7f-a36e-b03d34dfa3fd',14,'1','1'),(36,'ba09c144-ddc6-4137-a675-0ecdd57e63fb',14,'1','1'),(36,'c638e427-6560-441c-93fa-a53eb2f0ea22',14,'1','1'),(36,'e8a68356-16ff-406f-8970-1b2f879c43d6',14,'1','1'),(36,'8a22ebd8-7768-443c-b317-ea981814628e',14,'1','1'),(36,'229b3e48-07e1-4e96-b1c1-4fdc424baa34',14,'1','1'),(36,'521c3ad4-c045-4007-bccc-9d392d73c58f',14,'1','1'),(36,'ba5c01ce-28d0-4544-a0ef-d99e1f96e8eb',14,'1','1'),(36,'184d9822-8448-4b0b-aef3-b2662d1f0e4f',14,'1','1'),(36,'ea215a2a-edb1-47e2-a6b8-5fccbc194b98',14,'1','1'),(36,'280c67f5-2ab2-44cf-9d98-f348688c6178',14,'1','1'),(36,'2fe9e739-658a-43f1-a5d7-4b209640b2d5',14,'1','1'),(36,'17b70709-f198-4395-8d92-e854d7d34958',14,'1','1'),(36,'eec9a2ba-040a-4766-942b-3f911c2743d9',14,'1','1'),(36,'135239e0-3182-459f-8fc6-c1e0b89e0da9',14,'1','1'),(36,'1faf011d-b5d9-450b-b651-5e679e8c77e6',14,'1','1'),(36,'a577d15f-97ff-4513-bfde-58c28728e390',14,'1','1'),(36,'6c15bf0f-09ea-4b73-a0fe-c7408e13ddfe',14,'1','1'),(36,'e343173a-5ea9-452a-b325-aa21ea24bcea',14,'1','1'),(36,'231939d6-201a-4170-8fce-f3f85de45975',14,'1','1'),(36,'9d9ba6b9-1b16-4dc9-bcf7-e17c13d26878',14,'1','1'),(36,'12011fae-64cc-4210-a513-d4f47fa2ab40',14,'1','1'),(36,'a8f04f79-8a2a-4a37-abce-683d076d5bb1',14,'1','1'),(36,'852dde27-c7e9-4bc5-982e-f927d80011e1',14,'1','1'),(36,'e67957d2-967e-4f2c-9e12-81991c5edbe7',14,'1','1'),(36,'c9dd9324-8fa5-4bd7-8f2f-bfbc81ec0aaf',14,'1','1'),(36,'e01bdf14-8bdd-424d-b8e6-970d62643857',14,'1','1'),(36,'268ec015-d248-4742-87cd-593092e7ef84',14,'1','1'),(36,'95af1aa1-e663-4d45-b273-1d779b7822fb',14,'1','1'),(36,'249ede3e-e1df-44e4-b441-fad88bc99e01',14,'1','1'),(36,'004e56fe-3d4d-4e03-bd49-21eedacab82c',14,'1','1'),(36,'e7f5fe3c-c24d-4a4d-9264-55a8762fcac5',14,'1','1'),(36,'a635da81-52fd-48bd-8b7f-d309e41c4f43',14,'1','1'),(36,'8977a479-f55a-4d17-8386-f07d0dfd50d6',14,'1','1'),(36,'b0eb288b-8d79-4ef2-b1b4-d78e70b7973b',14,'1','1'),(36,'d4ac4d20-c126-4b4b-bbf7-726e54a9d046',14,'1','1'),(36,'d229185c-7df3-4f5f-a305-4735c88a3a01',14,'1','1'),(36,'4dde7d46-3f04-43cb-a62a-42acc2d10ff1',14,'1','1'),(36,'9c3e321d-ffcf-4a2e-b525-4c98c5bb2348',14,'1','1'),(36,'f6e77be8-0c4e-4730-8c5f-3ab8e932fab6',14,'1','1'),(36,'e700872a-9fa0-446e-aff9-6111fbe76ac8',14,'1','1'),(36,'c28c6d0e-ed7e-44a6-a7a0-cf507c80e85a',14,'1','1'),(36,'0d0147df-c9f7-4e10-ac5c-2e8d97e42394',14,'1','1'),(36,'2441a09b-c7e5-4521-8177-6aa100fd9288',14,'1','1'),(36,'7c5a341c-cdae-4a86-a0bb-7c543e5ed87b',14,'1','1'),(36,'b428c076-476b-4cfd-a4c2-1cceb46ec194',14,'1','1'),(36,'1e700067-f42d-4b29-89cc-5bf6298c67a8',14,'1','1'),(36,'df0da763-1b07-4f84-b23c-ff19a69798bb',14,'1','1'),(36,'c5d6208c-d5a4-44eb-a9d6-bebc19531c5a',14,'1','1'),(36,'5cc7dd88-e841-44e0-82c2-1cd97b8b33ba',14,'1','1'),(36,'1114674c-46c0-40ca-8c27-906d15fbe4c8',14,'1','1'),(36,'a994288e-028c-46a1-8c06-91b6328f8cf7',14,'1','1'),(36,'5ee07051-daf2-43d7-9946-b962b2e26492',14,'1','1'),(36,'5991188b-9d84-4c98-9ce4-a92f385b5a0c',14,'1','1'),(36,'4ed4a6e0-cdf7-4f80-b445-2e2be39e5f16',14,'1','1'),(36,'9b118863-1af5-4289-9455-ad635e5eb2e6',14,'1','1'),(36,'e9827285-8ef5-4b5c-8419-e8ac540010c2',14,'1','1'),(36,'4369a381-7a8a-42ec-b2ee-e2aca63b48f8',14,'1','1'),(36,'29ec91ea-ea08-4874-969a-aa414c65efc3',14,'1','1'),(36,'4d58c188-b285-4322-9503-983620f1b0f8',14,'1','1'),(36,'8755dc9c-706b-4c48-9531-6694c12c4154',14,'1','1'),(36,'b64911c3-c018-47d4-b65c-7c9dea9a64b3',14,'1','1'),(36,'cdc567c7-a70d-428d-9717-70c54e615f5e',14,'1','1'),(36,'e30f7fbb-4abc-49d6-b521-c601e72284d2',14,'1','1'),(36,'d38a9a35-e8b1-46fb-bdb5-97eb3f1e4f13',14,'1','1'),(36,'ef9f40e9-7fd3-4241-805a-697ae4aeeaac',14,'1','1'),(36,'bb6954bd-8040-48e5-950e-9f85817b9f7d',14,'1','1'),(36,'7765a558-2d83-4af8-adc6-81b51e0a0b20',14,'1','1'),(36,'464ab2fd-1148-4920-8197-afb252d79f25',14,'1','1'),(36,'fe58e748-b84e-4b4e-be17-9871596ce3a2',14,'1','1'),(36,'2cad9454-b4e6-40b7-960b-a6955b821886',14,'1','1'),(36,'388740db-cc93-460a-962e-73638d15088d',14,'1','1'),(36,'32123e79-3966-4bfb-9cb8-32a4a2409966',14,'1','1'),(36,'869ba04f-28b9-469e-a01c-f18fec920a9f',14,'1','1'),(36,'90eab2b6-1cf0-48a7-93e8-417fa4d1f34a',14,'1','1'),(36,'d1430d8d-af0b-49e6-8c39-2d253673319a',8,'1','1'),(37,'ec2f730b-65d6-4b11-a4f1-813d989500b0',14,'1','1'),(37,'d1430d8d-af0b-49e6-8c39-2d253673319a',8,'1','1'),(37,'6d84cc4f-5e31-40a0-825f-0fcc8f9d48d3',14,'1','1'),(37,'62295668-bc06-48e7-9982-c9eae994b3a1',14,'1','1'),(37,'b40da10f-3530-4546-b8bd-41004aadf81e',14,'1','1'),(37,'6b9c318d-7347-4322-afbb-e9b3e734e4fd',14,'1','1'),(37,'bf09a537-3fcc-4032-ab5f-aced1839513a',14,'1','1'),(37,'4b6e2678-e97a-4882-afa8-4bee785e0d55',14,'1','1'),(37,'e1805a1d-4f4d-4769-bf6f-0ceef96a2716',14,'1','1'),(37,'19afe12a-e810-48b1-88f3-3583a9dc786e',14,'1','1'),(37,'874d49d3-d7ef-4ddd-a606-db4a024092d3',14,'1','1'),(37,'38d7bebf-0524-485d-9952-6870eaa22075',14,'1','1'),(37,'a82185d9-0c49-4fe2-a558-de87927740eb',14,'1','1'),(37,'c3ea4458-bb78-46fa-8c70-c953f638ee0c',14,'1','1'),(37,'ee86526c-0087-489e-b553-260e3d07e1fe',14,'1','1'),(37,'ebb7415d-ce79-4c2b-b621-e57076e7452c',14,'1','1'),(37,'af492ecd-a9cb-45dd-9e08-e4892ffce38c',14,'1','1'),(37,'07a7a71d-8feb-427f-99b1-1df351dfb290',14,'1','1'),(37,'7326d09e-0df2-493f-8709-e81cd960b141',14,'1','1'),(37,'b5bbaab7-5e41-4a2a-9497-512eb12adf8e',14,'1','1'),(37,'b356f123-ea78-40ac-9bae-dd18a164d5e5',14,'1','1'),(37,'da0885d7-0ff7-44a4-b68a-ed1397105208',14,'1','1'),(37,'f54853a9-a0a3-42f1-8639-3ec5b09e5401',14,'1','1'),(37,'4e3695ee-b1a2-4093-bc60-cc4813aac669',14,'1','1'),(37,'ee638295-fd3a-4f20-84c8-74934f898159',14,'1','1'),(37,'a01062c6-7eb3-4c29-aab5-1ee72716962e',14,'1','1'),(37,'4ecd482a-e961-4f5a-a927-5c4984e2b0d2',14,'1','1'),(37,'025e9195-08fd-4a76-a142-0dbece7b1984',14,'1','1'),(37,'06f67e74-e7c8-4fe2-8625-7139b496edfc',14,'1','1'),(37,'88ecaf43-a585-4600-bb6b-9cd7517727cd',14,'1','1'),(37,'bc184bd7-f864-4e88-8984-d11bde38e6d7',14,'1','1'),(37,'0c7b9b12-bc79-4da6-b8f6-b18057cbe882',14,'1','1'),(37,'a6c19c10-e4c1-4d9d-8838-a3ff29dbd4ce',14,'1','1'),(37,'69791ed2-7461-42bc-8699-d8be328e2ee1',14,'1','1'),(37,'c19bcc71-53a7-4712-95dd-d0012e9e7592',14,'1','1'),(37,'db1495af-bef1-431d-87fb-ce4ec0d650c3',14,'1','1'),(37,'ebc7f28f-8270-49e5-a86f-75e47fe1c480',14,'1','1'),(37,'35c66078-1217-439b-bb19-41f94e359964',14,'1','1'),(37,'9aecc130-9c34-49b9-80fc-5ffdbb657d73',14,'1','1'),(37,'bc0b9585-18d6-45e0-a132-13010a00350e',14,'1','1'),(37,'cdb9b220-a3ee-4655-93b3-df7e9ba8065f',14,'1','1'),(37,'546be8b2-1ed7-4608-aeaa-9343bee7630e',14,'1','1'),(37,'1db66cb6-37b8-4906-aabc-71d0eef18068',14,'1','1'),(37,'6174bb8a-07e2-4a35-ae7b-add5e474e50a',14,'1','1'),(37,'5386549e-c9e2-403b-bd68-11471ae2af6f',14,'1','1'),(37,'71145325-848d-44ff-8dbc-ff6d8ab04d75',14,'1','1'),(37,'38b0bb81-007f-401a-97ae-53a55cb30bae',14,'1','1'),(37,'3a97f645-fdfc-4544-b281-945333aaaf30',14,'1','1'),(37,'18e2e188-845e-4ce1-a036-19302dcc84cf',14,'1','1'),(37,'7f1a7baf-6f7d-4c99-8c56-ec2a0f2b6839',14,'1','1'),(37,'7d39489a-5fc5-4851-b0b0-99265b81368d',14,'1','1'),(37,'58eee43e-f6aa-40e5-b927-155ae5b30920',14,'1','1'),(37,'27ed9197-681c-4f9d-8b0c-c16b11d7f5e6',14,'1','1'),(37,'fdeb0b4c-7ccd-4864-802a-3e21e18001f8',14,'1','1'),(37,'9ef72519-430d-4ddf-a4c7-5be254ab1e9e',14,'1','1'),(37,'43c71a7f-e4f3-4eef-85a4-7217b03eb0e0',14,'1','1'),(37,'2e930f7d-c4bc-4f9d-bc47-08e10781cc8c',14,'1','1'),(37,'1c0fc61c-458b-4f1c-8e07-94f8ea068172',14,'1','1'),(37,'a352ddcc-c489-4f23-b16a-0243776f18c3',14,'1','1'),(37,'3a3e1a4b-1911-4896-add7-73ea93885d2e',14,'1','1'),(37,'b93d5ba4-c733-4c0c-b106-01b66c5c1244',14,'1','1'),(37,'9cba5865-394e-41a7-8be0-be2209868e09',14,'1','1'),(37,'fb2fe8bb-0924-4a29-a4f5-0ea1e0573247',14,'1','1'),(37,'783c8415-1a2e-4450-8385-7d4a9b690057',14,'1','1'),(37,'94a280a1-495c-409f-813b-78b78f218751',14,'1','1'),(37,'4ea3676d-3fbe-40ed-affe-f46e45bed47c',14,'1','1'),(37,'c8996c6a-3368-4d73-95cb-e8be868f9541',14,'1','1'),(37,'d1bad65a-5e59-4ef5-af4c-5be4e6c85064',14,'1','1'),(37,'ae415b58-6117-4807-9050-a8d630138142',14,'1','1'),(37,'e8276751-f824-4144-8b51-b1344ca9cd55',14,'1','1'),(37,'93b7ec01-39fa-4ac6-a0c7-4087a8960511',14,'1','1'),(37,'bf744747-98d8-4858-8877-5dc9f1dfcdc2',14,'1','1'),(37,'a7607355-0aa0-43b5-b9da-2bae135a42c9',14,'1','1'),(37,'a22b2a23-6495-4084-8442-bc32125fd2a6',14,'1','1'),(37,'36fba6f3-216d-49d7-bf91-add309e764f8',8,'1','1'),(37,'b50e4212-4bc4-4915-bffb-f793103c05ca',14,'1','1'),(37,'33d968c8-265d-4306-8ebc-23e68849a33b',8,'1','1'),(37,'6e680d07-601e-4adb-a581-6f4d301de96a',14,'1','1'),(37,'1f98e91a-6f4f-4f98-9fdc-73dcb64f4206',14,'1','1'),(37,'efd34a54-8d40-48dd-9b75-67e4b0d8cd47',14,'1','1'),(37,'d21c7e23-bda0-47b3-a7c0-495e0ce56ef4',14,'1','1'),(37,'ef69e0f1-1cbd-44d5-a9ec-33b721096a07',14,'1','1'),(37,'f51996fc-ef2d-4d08-bc73-5c369a1f0c7f',14,'1','1'),(37,'e1383061-fac9-496a-aa9b-b036615c03c4',14,'1','1'),(37,'ac239a61-c642-468f-a16a-f6976892b93c',14,'1','1'),(37,'7fbbbd9f-7900-4488-aec7-d9898e11ea2d',14,'1','1'),(37,'e603171a-e7b3-4d84-82f1-5d848351d32b',14,'1','1'),(37,'ff504578-c994-4a89-bb4e-a672ee817064',14,'1','1'),(37,'0648876a-3b02-419b-9b8b-6a232c6d5574',14,'1','1'),(37,'5c6a69e2-3100-4fb2-b6cf-38a9d5bc92e4',14,'1','1'),(37,'8a63abf2-777a-4a19-b1f7-f82763a32b72',14,'1','1'),(37,'b786fba6-2233-4061-9070-143d9fc9da9a',14,'1','1'),(37,'23a4562f-3ad2-4b42-8de7-d92754deb8fc',14,'1','1'),(37,'871b0b76-c679-4df1-9576-90d9f8bc2e7f',14,'1','1'),(37,'00645336-54be-4062-9cb5-f14ef811dabe',14,'1','1'),(37,'f9efede8-9ebe-403a-bf88-53d2a53f867a',14,'1','1'),(37,'admin',8,'1','1'),(37,'e6e892e4-e16e-47c1-aada-6e22e7cb8858',14,'1','1'),(37,'6716a043-275b-4d28-b235-b313445efbef',14,'1','1'),(37,'27377926-4baa-4edd-9455-c331d21d4f35',14,'1','1'),(37,'0689e163-b621-4be1-bd15-0573d81b15d8',14,'1','1'),(37,'b75ade34-fd28-43ae-9b3d-d4441b25621e',14,'1','1'),(37,'cec0bd35-eb6f-4fa7-8210-70ce84a70b43',14,'1','1'),(37,'2340fa22-539c-4da2-8206-827a177e2c43',8,'1','1'),(37,'6ade0933-a316-4e0d-9dbf-942559d45a44',14,'1','1'),(37,'8cbe7f4c-dfc7-4138-b8a2-5bbfe70bc72f',14,'1','1'),(37,'b50bf16e-552c-4d0a-aa9c-a1f5bee65dae',14,'1','1'),(37,'90d1b7fd-a8c4-4061-b9db-6bc1f6fbb830',14,'1','1'),(37,'b8874b5d-6022-4548-af9f-cf3e9ed2ea76',14,'1','1'),(37,'83458e5e-f20c-4213-bf71-54c003c35af1',14,'1','1'),(37,'dff0baa1-64bf-4f7f-a36e-b03d34dfa3fd',14,'1','1'),(37,'ba09c144-ddc6-4137-a675-0ecdd57e63fb',14,'1','1'),(37,'c638e427-6560-441c-93fa-a53eb2f0ea22',14,'1','1'),(37,'e8a68356-16ff-406f-8970-1b2f879c43d6',14,'1','1'),(37,'8a22ebd8-7768-443c-b317-ea981814628e',14,'1','1'),(37,'229b3e48-07e1-4e96-b1c1-4fdc424baa34',14,'1','1'),(37,'521c3ad4-c045-4007-bccc-9d392d73c58f',14,'1','1'),(37,'ba5c01ce-28d0-4544-a0ef-d99e1f96e8eb',14,'1','1'),(37,'184d9822-8448-4b0b-aef3-b2662d1f0e4f',14,'1','1'),(37,'ea215a2a-edb1-47e2-a6b8-5fccbc194b98',14,'1','1'),(37,'280c67f5-2ab2-44cf-9d98-f348688c6178',14,'1','1'),(37,'2fe9e739-658a-43f1-a5d7-4b209640b2d5',14,'1','1'),(37,'17b70709-f198-4395-8d92-e854d7d34958',14,'1','1'),(37,'eec9a2ba-040a-4766-942b-3f911c2743d9',14,'1','1'),(37,'135239e0-3182-459f-8fc6-c1e0b89e0da9',14,'1','1'),(37,'1faf011d-b5d9-450b-b651-5e679e8c77e6',14,'1','1'),(37,'a577d15f-97ff-4513-bfde-58c28728e390',14,'1','1'),(37,'6c15bf0f-09ea-4b73-a0fe-c7408e13ddfe',14,'1','1'),(37,'e343173a-5ea9-452a-b325-aa21ea24bcea',14,'1','1'),(37,'231939d6-201a-4170-8fce-f3f85de45975',14,'1','1'),(37,'9d9ba6b9-1b16-4dc9-bcf7-e17c13d26878',14,'1','1'),(37,'12011fae-64cc-4210-a513-d4f47fa2ab40',14,'1','1'),(37,'a8f04f79-8a2a-4a37-abce-683d076d5bb1',14,'1','1'),(37,'e67957d2-967e-4f2c-9e12-81991c5edbe7',14,'1','1'),(37,'852dde27-c7e9-4bc5-982e-f927d80011e1',14,'1','1'),(37,'e01bdf14-8bdd-424d-b8e6-970d62643857',14,'1','1'),(37,'c9dd9324-8fa5-4bd7-8f2f-bfbc81ec0aaf',14,'1','1'),(37,'268ec015-d248-4742-87cd-593092e7ef84',14,'1','1'),(37,'95af1aa1-e663-4d45-b273-1d779b7822fb',14,'1','1'),(37,'249ede3e-e1df-44e4-b441-fad88bc99e01',14,'1','1'),(37,'004e56fe-3d4d-4e03-bd49-21eedacab82c',14,'1','1'),(37,'a635da81-52fd-48bd-8b7f-d309e41c4f43',14,'1','1'),(37,'e7f5fe3c-c24d-4a4d-9264-55a8762fcac5',14,'1','1'),(37,'8977a479-f55a-4d17-8386-f07d0dfd50d6',14,'1','1'),(37,'b0eb288b-8d79-4ef2-b1b4-d78e70b7973b',14,'1','1'),(37,'d4ac4d20-c126-4b4b-bbf7-726e54a9d046',14,'1','1'),(37,'d229185c-7df3-4f5f-a305-4735c88a3a01',14,'1','1'),(37,'4dde7d46-3f04-43cb-a62a-42acc2d10ff1',14,'1','1'),(37,'9c3e321d-ffcf-4a2e-b525-4c98c5bb2348',14,'1','1'),(37,'f6e77be8-0c4e-4730-8c5f-3ab8e932fab6',14,'1','1'),(37,'e700872a-9fa0-446e-aff9-6111fbe76ac8',14,'1','1'),(37,'c28c6d0e-ed7e-44a6-a7a0-cf507c80e85a',14,'1','1'),(37,'0d0147df-c9f7-4e10-ac5c-2e8d97e42394',14,'1','1'),(37,'2441a09b-c7e5-4521-8177-6aa100fd9288',14,'1','1'),(37,'7c5a341c-cdae-4a86-a0bb-7c543e5ed87b',14,'1','1'),(37,'b428c076-476b-4cfd-a4c2-1cceb46ec194',14,'1','1'),(37,'1e700067-f42d-4b29-89cc-5bf6298c67a8',14,'1','1'),(37,'df0da763-1b07-4f84-b23c-ff19a69798bb',14,'1','1'),(37,'c5d6208c-d5a4-44eb-a9d6-bebc19531c5a',14,'1','1'),(37,'5cc7dd88-e841-44e0-82c2-1cd97b8b33ba',14,'1','1'),(37,'1114674c-46c0-40ca-8c27-906d15fbe4c8',14,'1','1'),(37,'a994288e-028c-46a1-8c06-91b6328f8cf7',14,'1','1'),(37,'5ee07051-daf2-43d7-9946-b962b2e26492',14,'1','1'),(37,'5991188b-9d84-4c98-9ce4-a92f385b5a0c',14,'1','1'),(37,'9b118863-1af5-4289-9455-ad635e5eb2e6',14,'1','1'),(37,'4ed4a6e0-cdf7-4f80-b445-2e2be39e5f16',14,'1','1'),(37,'e9827285-8ef5-4b5c-8419-e8ac540010c2',14,'1','1'),(37,'29ec91ea-ea08-4874-969a-aa414c65efc3',14,'1','1'),(37,'4369a381-7a8a-42ec-b2ee-e2aca63b48f8',14,'1','1'),(37,'4d58c188-b285-4322-9503-983620f1b0f8',14,'1','1'),(37,'8755dc9c-706b-4c48-9531-6694c12c4154',14,'1','1'),(37,'b64911c3-c018-47d4-b65c-7c9dea9a64b3',14,'1','1'),(37,'cdc567c7-a70d-428d-9717-70c54e615f5e',14,'1','1'),(37,'e30f7fbb-4abc-49d6-b521-c601e72284d2',14,'1','1'),(37,'d38a9a35-e8b1-46fb-bdb5-97eb3f1e4f13',14,'1','1'),(37,'ef9f40e9-7fd3-4241-805a-697ae4aeeaac',14,'1','1'),(37,'bb6954bd-8040-48e5-950e-9f85817b9f7d',14,'1','1'),(37,'7765a558-2d83-4af8-adc6-81b51e0a0b20',14,'1','1'),(37,'464ab2fd-1148-4920-8197-afb252d79f25',14,'1','1'),(37,'fe58e748-b84e-4b4e-be17-9871596ce3a2',14,'1','1'),(37,'388740db-cc93-460a-962e-73638d15088d',14,'1','1'),(37,'2cad9454-b4e6-40b7-960b-a6955b821886',14,'1','1'),(37,'32123e79-3966-4bfb-9cb8-32a4a2409966',14,'1','1'),(37,'869ba04f-28b9-469e-a01c-f18fec920a9f',14,'1','1'),(37,'90eab2b6-1cf0-48a7-93e8-417fa4d1f34a',14,'1','1');
/*!40000 ALTER TABLE `SAKAI_REALM_RL_GR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_REALM_ROLE`
--

DROP TABLE IF EXISTS `SAKAI_REALM_ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_REALM_ROLE` (
  `ROLE_KEY` int(11) NOT NULL AUTO_INCREMENT,
  `ROLE_NAME` varchar(99) NOT NULL,
  PRIMARY KEY (`ROLE_KEY`),
  UNIQUE KEY `IE_SAKAI_REALM_ROLE_NAME` (`ROLE_NAME`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_REALM_ROLE`
--

LOCK TABLES `SAKAI_REALM_ROLE` WRITE;
/*!40000 ALTER TABLE `SAKAI_REALM_ROLE` DISABLE KEYS */;
INSERT INTO `SAKAI_REALM_ROLE` VALUES (1,'.anon'),(2,'.auth'),(3,'access'),(4,'admin'),(5,'CIG Coordinator'),(6,'CIG Participant'),(7,'Evaluator'),(8,'Instructor'),(9,'maintain'),(10,'Program Admin'),(11,'Program Coordinator'),(12,'pubview'),(13,'Reviewer'),(14,'Student'),(15,'Teaching Assistant');
/*!40000 ALTER TABLE `SAKAI_REALM_ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_REALM_ROLE_DESC`
--

DROP TABLE IF EXISTS `SAKAI_REALM_ROLE_DESC`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_REALM_ROLE_DESC` (
  `REALM_KEY` int(11) NOT NULL,
  `ROLE_KEY` int(11) NOT NULL,
  `DESCRIPTION` mediumtext,
  `PROVIDER_ONLY` char(1) DEFAULT NULL,
  PRIMARY KEY (`REALM_KEY`,`ROLE_KEY`),
  KEY `FK_SAKAI_REALM_ROLE_DESC_REALM` (`REALM_KEY`),
  KEY `ROLE_KEY` (`ROLE_KEY`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_REALM_ROLE_DESC`
--

LOCK TABLES `SAKAI_REALM_ROLE_DESC` WRITE;
/*!40000 ALTER TABLE `SAKAI_REALM_ROLE_DESC` DISABLE KEYS */;
INSERT INTO `SAKAI_REALM_ROLE_DESC` VALUES (9,8,'Can read, revise, delete and add both content and participants to a site.','0'),(9,14,'Can read content, and add content to a site where appropriate.','0'),(9,15,'Can read, add, and revise most content in their sections.','0'),(13,8,'Can read, revise, delete and add both content and participants to a site.','0'),(13,14,'Can read content, and add content to a site where appropriate.','0'),(13,15,'Can read, add, and revise most content in their sections.','0'),(32,1,NULL,'0'),(33,1,NULL,'0'),(34,1,NULL,'0'),(10,6,NULL,'0'),(10,7,NULL,'0'),(10,5,NULL,'0'),(10,13,NULL,'0'),(14,6,NULL,'0'),(14,7,NULL,'0'),(14,5,NULL,'0'),(14,13,NULL,'0'),(11,11,NULL,'0'),(11,10,NULL,'0'),(30,11,NULL,'0'),(30,10,NULL,'0'),(31,3,NULL,'0'),(31,9,NULL,'0'),(35,3,NULL,'0'),(35,9,NULL,'0'),(36,8,'Can read, revise, delete and add both content and participants to a site.','0'),(36,14,'Can read content, and add content to a site where appropriate.','0'),(36,15,'Can read, add, and revise most content in their sections.','0'),(37,8,'Can read, revise, delete and add both content and participants to a site.','0'),(37,14,'Can read content, and add content to a site where appropriate.','0'),(37,15,'Can read, add, and revise most content in their sections.','0');
/*!40000 ALTER TABLE `SAKAI_REALM_ROLE_DESC` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SESSION`
--

DROP TABLE IF EXISTS `SAKAI_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SESSION` (
  `SESSION_ID` varchar(36) DEFAULT NULL,
  `SESSION_SERVER` varchar(64) DEFAULT NULL,
  `SESSION_USER` varchar(99) DEFAULT NULL,
  `SESSION_IP` varchar(128) DEFAULT NULL,
  `SESSION_HOSTNAME` varchar(255) DEFAULT NULL,
  `SESSION_USER_AGENT` varchar(255) DEFAULT NULL,
  `SESSION_START` datetime DEFAULT NULL,
  `SESSION_END` datetime DEFAULT NULL,
  `SESSION_ACTIVE` tinyint(1) DEFAULT NULL,
  UNIQUE KEY `SAKAI_SESSION_INDEX` (`SESSION_ID`),
  KEY `SAKAI_SESSION_SERVER_INDEX` (`SESSION_SERVER`),
  KEY `SAKAI_SESSION_START_END_IE` (`SESSION_START`,`SESSION_END`,`SESSION_ID`),
  KEY `SESSION_ACTIVE_IE` (`SESSION_ACTIVE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SESSION`
--

LOCK TABLES `SAKAI_SESSION` WRITE;
/*!40000 ALTER TABLE `SAKAI_SESSION` DISABLE KEYS */;
INSERT INTO `SAKAI_SESSION` VALUES ('1b6e1a48-a679-4581-b43b-abb602db75a9','localhost-1302721559078','admin','127.0.0.1',NULL,'CMSync','2011-04-13 15:06:36','2011-04-13 15:06:54',NULL),('8a024426-7f6f-4e86-b17f-dd5209bef218','localhost-1302721559078','d1430d8d-af0b-49e6-8c39-2d253673319a','0:0:0:0:0:0:0:1',NULL,'Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.16) Gecko/20110323 Ubuntu/10.10 (maverick) Firefox/3.6.16','2011-04-13 15:55:40','2011-04-13 15:55:40',1);
/*!40000 ALTER TABLE `SAKAI_SESSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SITE`
--

DROP TABLE IF EXISTS `SAKAI_SITE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SITE` (
  `SITE_ID` varchar(99) NOT NULL,
  `TITLE` varchar(99) DEFAULT NULL,
  `TYPE` varchar(99) DEFAULT NULL,
  `SHORT_DESC` longtext,
  `DESCRIPTION` longtext,
  `ICON_URL` varchar(255) DEFAULT NULL,
  `INFO_URL` varchar(255) DEFAULT NULL,
  `SKIN` varchar(255) DEFAULT NULL,
  `PUBLISHED` int(11) DEFAULT NULL,
  `JOINABLE` char(1) DEFAULT NULL,
  `PUBVIEW` char(1) DEFAULT NULL,
  `JOIN_ROLE` varchar(99) DEFAULT NULL,
  `CREATEDBY` varchar(99) DEFAULT NULL,
  `MODIFIEDBY` varchar(99) DEFAULT NULL,
  `CREATEDON` datetime DEFAULT NULL,
  `MODIFIEDON` datetime DEFAULT NULL,
  `IS_SPECIAL` char(1) DEFAULT NULL,
  `IS_USER` char(1) DEFAULT NULL,
  `CUSTOM_PAGE_ORDERED` char(1) DEFAULT NULL,
  `IS_SOFTLY_DELETED` char(1) DEFAULT NULL,
  `SOFTLY_DELETED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`SITE_ID`),
  KEY `IE_SAKAI_SITE_CREATED` (`CREATEDBY`,`CREATEDON`),
  KEY `IE_SAKAI_SITE_MODDED` (`MODIFIEDBY`,`MODIFIEDON`),
  KEY `IE_SAKAI_SITE_FLAGS` (`SITE_ID`,`IS_SPECIAL`,`IS_USER`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SITE`
--

LOCK TABLES `SAKAI_SITE` WRITE;
/*!40000 ALTER TABLE `SAKAI_SITE` DISABLE KEYS */;
INSERT INTO `SAKAI_SITE` VALUES ('~admin','Administration Workspace',NULL,NULL,'Administration Workspace',NULL,NULL,NULL,1,'0','0','','admin','admin','2011-04-13 14:59:17','2011-04-13 14:59:17','0','1','0','0',NULL),('!admin','Administration Workspace',NULL,NULL,'Administration Workspace',NULL,NULL,NULL,1,'0','0','','admin','admin','2011-04-13 14:59:17','2011-04-13 14:59:17','0','0','0','0',NULL),('!error','Site Unavailable',NULL,NULL,'The site you requested is not available.',NULL,NULL,NULL,1,'0','0','','admin','admin','2011-04-13 14:59:17','2011-04-13 14:59:17','1','0','0','0',NULL),('!urlError','Invalid URL',NULL,NULL,'The URL you entered is invalid.  SOLUTIONS: Please check for spelling errors or typos.  Make sure you are using the right URL.  Type a URL to try again.',NULL,NULL,NULL,1,'0','0','','admin','admin','2011-04-13 14:59:17','2011-04-13 14:59:17','1','0','0','0',NULL),('!gateway','Gateway',NULL,NULL,'The Gateway Site',NULL,NULL,NULL,1,'0','0','','admin','admin','2011-04-13 14:59:17','2011-04-13 14:59:17','1','0','0','0',NULL),('!user','My Workspace',NULL,NULL,'My Workspace Site',NULL,NULL,NULL,1,'0','0','','admin','admin','2011-04-13 14:59:17','2011-04-13 14:59:17','1','0','0','0',NULL),('!worksite','worksite',NULL,NULL,NULL,'','',NULL,0,'0','0','access','admin','admin','2011-04-13 14:59:17','2011-04-13 14:59:17','1','0','0','0',NULL),('mercury','mercury site',NULL,NULL,NULL,'','',NULL,1,'1','1','access','admin','admin','2011-04-13 14:59:17','2011-04-13 14:59:17','0','0','0','0',NULL),('PortfolioAdmin','Portfolio Admin','portfolioAdmin',NULL,'Site for portfolio administration',NULL,NULL,NULL,1,'0','0',NULL,'admin','admin','2011-04-13 14:59:40','2011-04-13 14:59:41','0','0','0','0',NULL),('citationsAdmin','Citations Admin','project',NULL,NULL,NULL,NULL,NULL,1,'0','0',NULL,'admin','admin','2011-04-13 14:59:51','2011-04-13 14:59:51','0','0','0','0',NULL),('~d1430d8d-af0b-49e6-8c39-2d253673319a','My Workspace',NULL,NULL,'My Workspace Site',NULL,NULL,NULL,1,'0','0',NULL,'d1430d8d-af0b-49e6-8c39-2d253673319a','d1430d8d-af0b-49e6-8c39-2d253673319a','2011-04-13 15:55:40','2011-04-13 15:55:40','0','1','0','0',NULL),('92baf195-be33-4a5b-b378-6d96e9665ffc','SMPL101 Spring 2011','course',NULL,'Test Course site for Assignments 2 Selenium Tests.',NULL,NULL,NULL,1,'1','1','Student','d1430d8d-af0b-49e6-8c39-2d253673319a','d1430d8d-af0b-49e6-8c39-2d253673319a','2011-04-13 15:59:36','2011-04-13 15:59:38','0','0','0','0',NULL);
/*!40000 ALTER TABLE `SAKAI_SITE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SITE_GROUP`
--

DROP TABLE IF EXISTS `SAKAI_SITE_GROUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SITE_GROUP` (
  `GROUP_ID` varchar(99) NOT NULL,
  `SITE_ID` varchar(99) NOT NULL,
  `TITLE` varchar(99) DEFAULT NULL,
  `DESCRIPTION` longtext,
  PRIMARY KEY (`GROUP_ID`),
  KEY `IE_SAKAI_SITE_GRP_SITE` (`SITE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SITE_GROUP`
--

LOCK TABLES `SAKAI_SITE_GROUP` WRITE;
/*!40000 ALTER TABLE `SAKAI_SITE_GROUP` DISABLE KEYS */;
INSERT INTO `SAKAI_SITE_GROUP` VALUES ('33d648ee-b6df-444a-ba72-ce93646802cc','92baf195-be33-4a5b-b378-6d96e9665ffc','SMPL101 Spring 2011','SMPL101 Spring 2011 Lecture');
/*!40000 ALTER TABLE `SAKAI_SITE_GROUP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SITE_GROUP_PROPERTY`
--

DROP TABLE IF EXISTS `SAKAI_SITE_GROUP_PROPERTY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SITE_GROUP_PROPERTY` (
  `SITE_ID` varchar(99) NOT NULL,
  `GROUP_ID` varchar(99) NOT NULL,
  `NAME` varchar(99) NOT NULL,
  `VALUE` longtext,
  PRIMARY KEY (`GROUP_ID`,`NAME`),
  KEY `IE_SAKAI_SITE_GRP_PROP_SITE` (`SITE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SITE_GROUP_PROPERTY`
--

LOCK TABLES `SAKAI_SITE_GROUP_PROPERTY` WRITE;
/*!40000 ALTER TABLE `SAKAI_SITE_GROUP_PROPERTY` DISABLE KEYS */;
INSERT INTO `SAKAI_SITE_GROUP_PROPERTY` VALUES ('92baf195-be33-4a5b-b378-6d96e9665ffc','33d648ee-b6df-444a-ba72-ce93646802cc','sections_sunday','false'),('92baf195-be33-4a5b-b378-6d96e9665ffc','33d648ee-b6df-444a-ba72-ce93646802cc','sections_location','A Building 11'),('92baf195-be33-4a5b-b378-6d96e9665ffc','33d648ee-b6df-444a-ba72-ce93646802cc','sections_category','01.lct'),('92baf195-be33-4a5b-b378-6d96e9665ffc','33d648ee-b6df-444a-ba72-ce93646802cc','sections_start_time','13/04/2011 10:30 Eastern Daylight Time'),('92baf195-be33-4a5b-b378-6d96e9665ffc','33d648ee-b6df-444a-ba72-ce93646802cc','sections_tuesday','false'),('92baf195-be33-4a5b-b378-6d96e9665ffc','33d648ee-b6df-444a-ba72-ce93646802cc','sections_end_time','13/04/2011 11:00 Eastern Daylight Time'),('92baf195-be33-4a5b-b378-6d96e9665ffc','33d648ee-b6df-444a-ba72-ce93646802cc','sections_saturday','false'),('92baf195-be33-4a5b-b378-6d96e9665ffc','33d648ee-b6df-444a-ba72-ce93646802cc','sections_thursday','false'),('92baf195-be33-4a5b-b378-6d96e9665ffc','33d648ee-b6df-444a-ba72-ce93646802cc','sections_friday','true'),('92baf195-be33-4a5b-b378-6d96e9665ffc','33d648ee-b6df-444a-ba72-ce93646802cc','sections_monday','true'),('92baf195-be33-4a5b-b378-6d96e9665ffc','33d648ee-b6df-444a-ba72-ce93646802cc','sections_wednesday','true');
/*!40000 ALTER TABLE `SAKAI_SITE_GROUP_PROPERTY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SITE_PAGE`
--

DROP TABLE IF EXISTS `SAKAI_SITE_PAGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SITE_PAGE` (
  `PAGE_ID` varchar(99) NOT NULL,
  `SITE_ID` varchar(99) NOT NULL,
  `TITLE` varchar(99) DEFAULT NULL,
  `LAYOUT` char(1) DEFAULT NULL,
  `SITE_ORDER` int(11) NOT NULL,
  `POPUP` char(1) DEFAULT NULL,
  PRIMARY KEY (`PAGE_ID`),
  KEY `SITE_ID` (`SITE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SITE_PAGE`
--

LOCK TABLES `SAKAI_SITE_PAGE` WRITE;
/*!40000 ALTER TABLE `SAKAI_SITE_PAGE` DISABLE KEYS */;
INSERT INTO `SAKAI_SITE_PAGE` VALUES ('~admin-100','~admin','Home','0',1,'0'),('~admin-200','~admin','Users','0',2,'0'),('~admin-250','~admin','Aliases','0',3,'0'),('~admin-300','~admin','Sites','0',4,'0'),('~admin-350','~admin','Realms','0',5,'0'),('~admin-360','~admin','Worksite Setup','0',6,'0'),('~admin-400','~admin','MOTD','0',7,'0'),('~admin-500','~admin','Resources','0',8,'0'),('~admin-600','~admin','On-Line','0',9,'0'),('~admin-700','~admin','Memory','0',10,'0'),('~admin-900','~admin','Site Archive','0',11,'0'),('~admin-1000','~admin','Job Scheduler','0',12,'0'),('~admin-1100','~admin','Become User','0',13,'0'),('~admin-1200','~admin','User Membership','0',14,'0'),('!admin-100','!admin','Home','0',1,'0'),('!admin-200','!admin','Users','0',2,'0'),('!admin-250','!admin','Aliases','0',3,'0'),('!admin-300','!admin','Sites','0',4,'0'),('!admin-350','!admin','Realms','0',5,'0'),('!admin-360','!admin','Worksite Setup','0',6,'0'),('!admin-400','!admin','MOTD','0',7,'0'),('!admin-500','!admin','Resources','0',8,'0'),('!admin-600','!admin','On-Line','0',9,'0'),('!admin-700','!admin','Memory','0',10,'0'),('!admin-900','!admin','Site Archive','0',11,'0'),('!admin-1000','!admin','Job Scheduler','0',12,'0'),('!admin-1100','!admin','Become User','0',13,'0'),('!admin-1200','!admin','User Membership','0',14,'0'),('!admin-1205','!admin','Email Templates','0',15,'0'),('!error-100','!error','Site Unavailable','1',1,'0'),('!urlError-100','!urlError','Invalid URL','1',1,'0'),('!gateway-100','!gateway','Welcome','0',1,'0'),('!gateway-200','!gateway','About','0',2,'0'),('!gateway-300','!gateway','Features','0',3,'0'),('!gateway-400','!gateway','Sites','0',4,'0'),('!gateway-500','!gateway','Training','0',5,'0'),('!gateway-600','!gateway','Acknowledgements','0',6,'0'),('!gateway-700','!gateway','New Account','0',7,'0'),('!user-100','!user','Home','1',1,'0'),('!user-150','!user','Profile','0',2,'0'),('!user-200','!user','Membership','0',3,'0'),('!user-300','!user','Schedule','0',4,'0'),('!user-400','!user','Resources','0',5,'0'),('!user-450','!user','Announcements','0',6,'0'),('!user-500','!user','Worksite Setup','0',7,'0'),('!user-600','!user','Preferences','0',8,'0'),('!user-700','!user','Account','0',9,'0'),('!worksite-100','!worksite','Home','1',1,'0'),('!worksite-200','!worksite','Schedule','0',2,'0'),('!worksite-300','!worksite','Announcements','0',3,'0'),('!worksite-400','!worksite','Resources','0',4,'0'),('!worksite-500','!worksite','Discussion','0',5,'0'),('!worksite-600','!worksite','Assignments','0',6,'0'),('!worksite-700','!worksite','Drop Box','0',7,'0'),('!worksite-800','!worksite','Chat','0',8,'0'),('!worksite-900','!worksite','Email Archive','0',9,'0'),('mercury-100','mercury','Home','1',1,'0'),('mercury-200','mercury','Schedule','0',2,'0'),('mercury-300','mercury','Announcements','0',3,'0'),('mercury-400','mercury','Resources','0',4,'0'),('mercury-500','mercury','Discussion','0',5,'0'),('mercury-600','mercury','Assignments','0',6,'0'),('mercury-700','mercury','Drop Box','0',7,'0'),('mercury-800','mercury','Chat','0',8,'0'),('mercury-900','mercury','Email Archive','0',9,'0'),('mercury-1000','mercury','Site Info','0',10,'0'),('dd9e7576-9075-4687-a2cf-8027eab3e357','PortfolioAdmin','Forms','0',9,'0'),('83483d88-c8aa-436c-b386-c4634107a314','PortfolioAdmin','Glossary','0',8,'0'),('62ed87bc-e7f4-49d1-9599-7cdc0dbaa311','PortfolioAdmin','Styles','0',7,'0'),('151cc959-2cbc-4664-a6e7-1d8a8a8257d8','PortfolioAdmin','Portfolio Layouts','0',6,'0'),('2dd82f79-f5a8-4039-99cc-85c5cd935f5f','PortfolioAdmin','Portfolio Templates','0',5,'0'),('a6f5a5fc-edea-42b2-ab20-a44b80653612','PortfolioAdmin','Portfolio','0',4,'0'),('6c58e1d1-df97-4dab-808e-1719ea5e86d7','PortfolioAdmin','Email Archive','0',3,'0'),('66f0ce04-657b-4798-9b3b-f337fd2dbb87','PortfolioAdmin','Resources','0',2,'0'),('1b5c3625-0248-48ee-805d-8c65aac227b2','PortfolioAdmin','Home','0',1,'0'),('11fbfedf-546f-40c1-bc81-88346e11346b','PortfolioAdmin','Site Info','0',10,'0'),('9adf007b-75ff-4976-88ce-ce0eaaca36a2','citationsAdmin','Resources','0',1,'0'),('de661c3f-5028-4613-92e4-69a69e277252','~d1430d8d-af0b-49e6-8c39-2d253673319a','Home','1',1,'0'),('e12e8b78-56e4-40bd-878b-c28b791a0f7a','~d1430d8d-af0b-49e6-8c39-2d253673319a','Profile','0',2,'0'),('9c519fbd-a4d0-4fc0-8c19-bfd1e199d51c','~d1430d8d-af0b-49e6-8c39-2d253673319a','Membership','0',3,'0'),('fdb0f735-75de-4bec-9446-6d77e9232a73','~d1430d8d-af0b-49e6-8c39-2d253673319a','Schedule','0',4,'0'),('0bf05b25-0622-4fe0-a845-7b8520c13475','~d1430d8d-af0b-49e6-8c39-2d253673319a','Resources','0',5,'0'),('8fc3ab38-9132-4c25-9ea2-8e7c713d8775','~d1430d8d-af0b-49e6-8c39-2d253673319a','Announcements','0',6,'0'),('81c8975a-826b-4cfc-9097-a97e804738f4','~d1430d8d-af0b-49e6-8c39-2d253673319a','Worksite Setup','0',7,'0'),('464f0bf2-53f0-42d2-b0db-c2cea45c30b7','~d1430d8d-af0b-49e6-8c39-2d253673319a','Preferences','0',8,'0'),('79613c13-3c02-4c37-a2e1-6c0a90ff116f','~d1430d8d-af0b-49e6-8c39-2d253673319a','Account','0',9,'0'),('2dac6354-f50b-4720-8208-fe824a0ae514','92baf195-be33-4a5b-b378-6d96e9665ffc','Site Info','0',5,'0'),('111b87d9-d13f-4b30-8be1-13434495c45e','92baf195-be33-4a5b-b378-6d96e9665ffc','Assignment2','0',4,'0'),('db27cce5-65d2-4f49-9142-6c2f73884212','92baf195-be33-4a5b-b378-6d96e9665ffc','Gradebook','0',3,'0'),('095c10cd-db2a-474d-8fa3-223a75e2f9b6','92baf195-be33-4a5b-b378-6d96e9665ffc','Announcements','0',2,'0'),('613ee5c7-e448-4823-8ddb-1bb8bf6ed8bf','92baf195-be33-4a5b-b378-6d96e9665ffc','Home','1',1,'0');
/*!40000 ALTER TABLE `SAKAI_SITE_PAGE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SITE_PAGE_PROPERTY`
--

DROP TABLE IF EXISTS `SAKAI_SITE_PAGE_PROPERTY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SITE_PAGE_PROPERTY` (
  `SITE_ID` varchar(99) NOT NULL,
  `PAGE_ID` varchar(99) NOT NULL,
  `NAME` varchar(99) NOT NULL,
  `VALUE` longtext,
  PRIMARY KEY (`PAGE_ID`,`NAME`),
  KEY `IE_SAKAI_SITE_PAGE_PROP_SITE` (`SITE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SITE_PAGE_PROPERTY`
--

LOCK TABLES `SAKAI_SITE_PAGE_PROPERTY` WRITE;
/*!40000 ALTER TABLE `SAKAI_SITE_PAGE_PROPERTY` DISABLE KEYS */;
INSERT INTO `SAKAI_SITE_PAGE_PROPERTY` VALUES ('~admin','~admin-100','is_home_page','true'),('~admin','~admin-400','sitePage.customTitle','true'),('!admin','!admin-100','is_home_page','true'),('!gateway','!gateway-200','sitePage.customTitle','true'),('!gateway','!gateway-300','sitePage.customTitle','true'),('!gateway','!gateway-500','sitePage.customTitle','true'),('!gateway','!gateway-600','sitePage.customTitle','true'),('!user','!user-100','is_home_page','true'),('!worksite','!worksite-100','is_home_page','true'),('mercury','mercury-100','is_home_page','true'),('~d1430d8d-af0b-49e6-8c39-2d253673319a','de661c3f-5028-4613-92e4-69a69e277252','is_home_page','true'),('92baf195-be33-4a5b-b378-6d96e9665ffc','613ee5c7-e448-4823-8ddb-1bb8bf6ed8bf','is_home_page','true');
/*!40000 ALTER TABLE `SAKAI_SITE_PAGE_PROPERTY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SITE_PROPERTY`
--

DROP TABLE IF EXISTS `SAKAI_SITE_PROPERTY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SITE_PROPERTY` (
  `SITE_ID` varchar(99) NOT NULL,
  `NAME` varchar(99) NOT NULL,
  `VALUE` longtext,
  PRIMARY KEY (`SITE_ID`,`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SITE_PROPERTY`
--

LOCK TABLES `SAKAI_SITE_PROPERTY` WRITE;
/*!40000 ALTER TABLE `SAKAI_SITE_PROPERTY` DISABLE KEYS */;
INSERT INTO `SAKAI_SITE_PROPERTY` VALUES ('92baf195-be33-4a5b-b378-6d96e9665ffc','contact-email','instructor@local.host'),('92baf195-be33-4a5b-b378-6d96e9665ffc','term_eid','Spring 2011'),('92baf195-be33-4a5b-b378-6d96e9665ffc','locale_string','en'),('92baf195-be33-4a5b-b378-6d96e9665ffc','term','Spring 2011'),('92baf195-be33-4a5b-b378-6d96e9665ffc','contact-name','The Instructor'),('92baf195-be33-4a5b-b378-6d96e9665ffc','sections_externally_maintained','true'),('92baf195-be33-4a5b-b378-6d96e9665ffc','sections_student_switching_allowed','false'),('92baf195-be33-4a5b-b378-6d96e9665ffc','sections_student_registration_allowed','false');
/*!40000 ALTER TABLE `SAKAI_SITE_PROPERTY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SITE_TOOL`
--

DROP TABLE IF EXISTS `SAKAI_SITE_TOOL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SITE_TOOL` (
  `TOOL_ID` varchar(99) NOT NULL,
  `PAGE_ID` varchar(99) NOT NULL,
  `SITE_ID` varchar(99) NOT NULL,
  `REGISTRATION` varchar(99) NOT NULL,
  `PAGE_ORDER` int(11) NOT NULL,
  `TITLE` varchar(99) DEFAULT NULL,
  `LAYOUT_HINTS` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`TOOL_ID`),
  KEY `PAGE_ID` (`PAGE_ID`),
  KEY `SITE_ID` (`SITE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SITE_TOOL`
--

LOCK TABLES `SAKAI_SITE_TOOL` WRITE;
/*!40000 ALTER TABLE `SAKAI_SITE_TOOL` DISABLE KEYS */;
INSERT INTO `SAKAI_SITE_TOOL` VALUES ('~admin-110','~admin-100','~admin','sakai.motd',1,'Message of the Day',NULL),('~admin-120','~admin-100','~admin','sakai.iframe.myworkspace',2,'My Workspace Information',NULL),('~admin-210','~admin-200','~admin','sakai.users',1,'Users',NULL),('~admin-260','~admin-250','~admin','sakai.aliases',1,'Aliases',NULL),('~admin-310','~admin-300','~admin','sakai.sites',1,'Sites',NULL),('~admin-355','~admin-350','~admin','sakai.realms',1,'Realms',NULL),('~admin-365','~admin-360','~admin','sakai.sitesetup',1,'Worksite Setup',NULL),('~admin-410','~admin-400','~admin','sakai.announcements',1,'MOTD',NULL),('~admin-510','~admin-500','~admin','sakai.resources',1,'Resources',NULL),('~admin-610','~admin-600','~admin','sakai.online',1,'On-Line',NULL),('~admin-710','~admin-700','~admin','sakai.memory',1,'Memory',NULL),('~admin-910','~admin-900','~admin','sakai.archive',1,'Site Archive / Import',NULL),('~admin-1010','~admin-1000','~admin','sakai.scheduler',1,'Job Scheduler',NULL),('~admin-1110','~admin-1100','~admin','sakai.su',1,'Become User',NULL),('~admin-1210','~admin-1200','~admin','sakai.usermembership',1,'User Membership',NULL),('!admin-110','!admin-100','!admin','sakai.motd',1,'Message of the Day',NULL),('!admin-120','!admin-100','!admin','sakai.iframe.myworkspace',2,'My Workspace Information',NULL),('!admin-210','!admin-200','!admin','sakai.users',1,'Users',NULL),('!admin-260','!admin-250','!admin','sakai.aliases',1,'Aliases',NULL),('!admin-310','!admin-300','!admin','sakai.sites',1,'Sites',NULL),('!admin-355','!admin-350','!admin','sakai.realms',1,'Realms',NULL),('!admin-365','!admin-360','!admin','sakai.sitesetup',1,'Worksite Setup',NULL),('!admin-410','!admin-400','!admin','sakai.announcements',1,'MOTD',NULL),('!admin-510','!admin-500','!admin','sakai.resources',1,'Resources',NULL),('!admin-610','!admin-600','!admin','sakai.online',1,'On-Line',NULL),('!admin-710','!admin-700','!admin','sakai.memory',1,'Memory',NULL),('!admin-910','!admin-900','!admin','sakai.archive',1,'Site Archive / Import',NULL),('!admin-1010','!admin-1000','!admin','sakai.scheduler',1,'Job Scheduler',NULL),('!admin-1110','!admin-1100','!admin','sakai.su',1,'Become User',NULL),('!admin-1210','!admin-1200','!admin','sakai.usermembership',1,'User Membership',NULL),('!admin-1211','!admin-1205','!admin','sakai.emailtemplateservice',1,'Email Templates',NULL),('!error-110','!error-100','!error','sakai.iframe.site',1,'Site Unavailable',NULL),('!urlError-110','!urlError-100','!urlError','sakai.iframe.site',1,'Invalid URL',NULL),('!gateway-110','!gateway-100','!gateway','sakai.motd',1,'Message of the day',NULL),('!gateway-120','!gateway-100','!gateway','sakai.iframe.service',2,'Welcome!',NULL),('!gateway-210','!gateway-200','!gateway','sakai.iframe',1,'About',NULL),('!gateway-310','!gateway-300','!gateway','sakai.iframe',1,'Features',NULL),('!gateway-410','!gateway-400','!gateway','sakai.sitebrowser',1,'Sites',NULL),('!gateway-510','!gateway-500','!gateway','sakai.iframe',1,'Training',NULL),('!gateway-610','!gateway-600','!gateway','sakai.iframe',1,'Acknowledgments',NULL),('!gateway-710','!gateway-700','!gateway','sakai.createuser',1,'New Account',NULL),('!user-110','!user-100','!user','sakai.motd',1,'Message of the Day','0,0'),('!user-120','!user-100','!user','sakai.iframe.myworkspace',2,'My Workspace Information','1,0'),('!user-130','!user-100','!user','sakai.summary.calendar',2,'Calendar','0,1'),('!user-140','!user-100','!user','sakai.synoptic.announcement',2,'Recent Announcements','1,1'),('!user-145','!user-100','!user','sakai.synoptic.messagecenter',2,'Unread Messages and Forums','1,1'),('!user-165','!user-150','!user','sakai.profile2',1,'Profile',NULL),('!user-210','!user-200','!user','sakai.membership',1,'Membership',NULL),('!user-310','!user-300','!user','sakai.schedule',1,'Schedule',NULL),('!user-410','!user-400','!user','sakai.resources',1,'Resources',NULL),('!user-455','!user-450','!user','sakai.announcements',1,'Announcements',NULL),('!user-510','!user-500','!user','sakai.sitesetup',1,'Worksite Setup',NULL),('!user-610','!user-600','!user','sakai.preferences',1,'Preferences',NULL),('!user-710','!user-700','!user','sakai.singleuser',1,'Account',NULL),('!worksite-110','!worksite-100','!worksite','sakai.iframe.site',1,'My Workspace Information',NULL),('!worksite-120','!worksite-100','!worksite','sakai.synoptic.announcement',2,'Recent Announcements',NULL),('b4e28c78-f3d0-48cd-a9e2-f70ae34884d1','dd9e7576-9075-4687-a2cf-8027eab3e357','PortfolioAdmin','sakai.metaobj',1,NULL,'0,0'),('!worksite-140','!worksite-100','!worksite','sakai.synoptic.chat',4,'Recent Chat Messages',NULL),('!worksite-210','!worksite-200','!worksite','sakai.schedule',1,'Schedule',NULL),('!worksite-310','!worksite-300','!worksite','sakai.announcements',1,'Announcements',NULL),('!worksite-410','!worksite-400','!worksite','sakai.resources',1,'Resources',NULL),('e99c47e7-6b7d-4409-a4a9-32871d8b1579','83483d88-c8aa-436c-b386-c4634107a314','PortfolioAdmin','osp.glossary',1,NULL,'0,0'),('!worksite-610','!worksite-600','!worksite','sakai.assignment.grades',1,'Assignments',NULL),('!worksite-710','!worksite-700','!worksite','sakai.dropbox',1,'Drop Box',NULL),('!worksite-810','!worksite-800','!worksite','sakai.chat',1,'Chat',NULL),('!worksite-910','!worksite-900','!worksite','sakai.mailbox',1,'Email Archive',NULL),('mercury-110','mercury-100','mercury','sakai.iframe.site',1,'My Workspace Information',NULL),('mercury-120','mercury-100','mercury','sakai.synoptic.announcement',2,'Recent Announcements',NULL),('mercury-140','mercury-100','mercury','sakai.synoptic.chat',4,'Recent Chat Messages',NULL),('mercury-210','mercury-200','mercury','sakai.schedule',1,'Schedule',NULL),('mercury-310','mercury-300','mercury','sakai.announcements',1,'Announcements',NULL),('mercury-410','mercury-400','mercury','sakai.resources',1,'Resources',NULL),('mercury-610','mercury-600','mercury','sakai.assignment.grades',1,'Assignments',NULL),('mercury-710','mercury-700','mercury','sakai.dropbox',1,'Drop Box',NULL),('mercury-810','mercury-800','mercury','sakai.chat',1,'Chat',NULL),('mercury-910','mercury-900','mercury','sakai.mailbox',1,'Email Archive',NULL),('mercury-1010','mercury-1000','mercury','sakai.siteinfo',1,'Site Info',NULL),('09bbec81-8322-4423-8b65-acc636ed2105','62ed87bc-e7f4-49d1-9599-7cdc0dbaa311','PortfolioAdmin','osp.style',1,NULL,'0,0'),('b0b38507-4604-44e0-b690-cae897aefa8b','151cc959-2cbc-4664-a6e7-1d8a8a8257d8','PortfolioAdmin','osp.presLayout',1,NULL,'0,0'),('12860648-d921-4bdf-91a5-9fae8e76334b','2dd82f79-f5a8-4039-99cc-85c5cd935f5f','PortfolioAdmin','osp.presTemplate',1,NULL,'0,0'),('05b43286-2d15-42a0-b495-23511cb84a11','a6f5a5fc-edea-42b2-ab20-a44b80653612','PortfolioAdmin','osp.presentation',1,NULL,'0,0'),('7c90fe2b-327e-44db-beca-96e51befeeb5','6c58e1d1-df97-4dab-808e-1719ea5e86d7','PortfolioAdmin','sakai.mailbox',1,NULL,'0,0'),('681145c4-5b36-49dc-8db6-f2de2c8e84ff','66f0ce04-657b-4798-9b3b-f337fd2dbb87','PortfolioAdmin','sakai.resources',1,NULL,'0,0'),('33fbcff5-abbd-47ee-97db-23c69210c318','1b5c3625-0248-48ee-805d-8c65aac227b2','PortfolioAdmin','sakai.iframe.site',1,'Worksite Information','0,0'),('28dfa9db-614a-44b2-84ad-8f61eda1f1a4','11fbfedf-546f-40c1-bc81-88346e11346b','PortfolioAdmin','sakai.siteinfo',1,NULL,'0,0'),('db636dbb-f3d5-432d-a753-c3b5b54a42a2','9adf007b-75ff-4976-88ce-ce0eaaca36a2','citationsAdmin','sakai.resources',1,NULL,NULL),('ddecfb3d-ed0d-49f0-bcdd-8cc2ea55fc39','de661c3f-5028-4613-92e4-69a69e277252','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.motd',1,'Message Of The Day','0,0'),('b72b6848-d6fe-4a23-b8e4-894f2ba60b1b','de661c3f-5028-4613-92e4-69a69e277252','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.iframe.myworkspace',2,'My Workspace Information Display','1,0'),('6e72f498-6363-4fe1-a211-21f337dc064b','de661c3f-5028-4613-92e4-69a69e277252','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.summary.calendar',3,'Calendar','0,1'),('ae827087-8ebb-456d-b90d-17b6c17942ff','de661c3f-5028-4613-92e4-69a69e277252','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.synoptic.announcement',4,'Recent Announcements','1,1'),('23a9bffa-0290-4e38-8011-422a7a3eb4ba','de661c3f-5028-4613-92e4-69a69e277252','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.synoptic.messagecenter',5,'Message Center Notifications','1,1'),('fe125356-767e-4f64-8385-9a6df04576c4','e12e8b78-56e4-40bd-878b-c28b791a0f7a','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.profile2',1,'Profile',NULL),('11813e43-82a8-4c28-a6d0-49bec324c42f','9c519fbd-a4d0-4fc0-8c19-bfd1e199d51c','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.membership',1,'Membership',NULL),('fbef4ae5-3873-4893-a715-6e871703aeb7','fdb0f735-75de-4bec-9446-6d77e9232a73','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.schedule',1,'Schedule',NULL),('058ad951-de34-42b1-95e6-8448daedda77','0bf05b25-0622-4fe0-a845-7b8520c13475','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.resources',1,'Resources',NULL),('c8c58085-b433-4f73-9a0b-d4127572d6a5','8fc3ab38-9132-4c25-9ea2-8e7c713d8775','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.announcements',1,'Announcements',NULL),('2608a5f4-ecf1-484c-a072-3a77324512d7','81c8975a-826b-4cfc-9097-a97e804738f4','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.sitesetup',1,'Worksite Setup',NULL),('19965fe2-7ce0-41b3-8a35-3492e53413e7','464f0bf2-53f0-42d2-b0db-c2cea45c30b7','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.preferences',1,'Preferences',NULL),('a521e0b2-45a0-4c75-b945-f1eb2cee5f14','79613c13-3c02-4c37-a2e1-6c0a90ff116f','~d1430d8d-af0b-49e6-8c39-2d253673319a','sakai.singleuser',1,'Account',NULL),('bf73ff60-582a-4e85-9d2d-c99a672725bf','111b87d9-d13f-4b30-8be1-13434495c45e','92baf195-be33-4a5b-b378-6d96e9665ffc','sakai.assignment2',1,'Assignment2',NULL),('7e8cd041-855f-4399-b1cf-768465b7b19a','db27cce5-65d2-4f49-9142-6c2f73884212','92baf195-be33-4a5b-b378-6d96e9665ffc','sakai.gradebook.tool',1,'Gradebook',NULL),('08c0b8d1-e088-4ad3-94ba-2a5020140b98','095c10cd-db2a-474d-8fa3-223a75e2f9b6','92baf195-be33-4a5b-b378-6d96e9665ffc','sakai.announcements',1,'Announcements',NULL),('f820bd4e-d308-4f6c-8b10-d5fb484b6d31','613ee5c7-e448-4823-8ddb-1bb8bf6ed8bf','92baf195-be33-4a5b-b378-6d96e9665ffc','sakai.synoptic.announcement',2,'Recent Announcements','0,1'),('73aaaf19-99f9-4bc8-8eee-87cd3382178c','613ee5c7-e448-4823-8ddb-1bb8bf6ed8bf','92baf195-be33-4a5b-b378-6d96e9665ffc','sakai.iframe.site',1,'Site Information Display','0,0'),('6f233af6-8636-4902-9bcf-d536cab4f65c','2dac6354-f50b-4720-8208-fe824a0ae514','92baf195-be33-4a5b-b378-6d96e9665ffc','sakai.siteinfo',1,'Site Info',NULL);
/*!40000 ALTER TABLE `SAKAI_SITE_TOOL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SITE_TOOL_PROPERTY`
--

DROP TABLE IF EXISTS `SAKAI_SITE_TOOL_PROPERTY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SITE_TOOL_PROPERTY` (
  `SITE_ID` varchar(99) NOT NULL,
  `TOOL_ID` varchar(99) NOT NULL,
  `NAME` varchar(99) NOT NULL,
  `VALUE` longtext,
  PRIMARY KEY (`TOOL_ID`,`NAME`),
  KEY `IE_SAKAI_SITE_TOOL_PROP_SITE` (`SITE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SITE_TOOL_PROPERTY`
--

LOCK TABLES `SAKAI_SITE_TOOL_PROPERTY` WRITE;
/*!40000 ALTER TABLE `SAKAI_SITE_TOOL_PROPERTY` DISABLE KEYS */;
INSERT INTO `SAKAI_SITE_TOOL_PROPERTY` VALUES ('~admin','~admin-410','channel','/announcement/channel/!site/motd'),('~admin','~admin-510','home','/'),('!admin','!admin-410','channel','/announcement/channel/!site/motd'),('!admin','!admin-510','home','/'),('!error','!error-110','height','400px'),('!urlError','!urlError-110','height','400px'),('!gateway','!gateway-210','height','500px'),('!gateway','!gateway-210','source','/library/content/gateway/about.html'),('!gateway','!gateway-310','height','500px'),('!gateway','!gateway-310','source','/library/content/gateway/features.html'),('!gateway','!gateway-510','height','500px'),('!gateway','!gateway-510','source','/library/content/gateway/training.html'),('!gateway','!gateway-610','height','500px'),('!gateway','!gateway-610','source','/library/content/gateway/acknowledgments.html'),('!user','!user-710','include-password','true'),('!worksite','!worksite-110','height','100px'),('!worksite','!worksite-710','resources_mode','dropbox'),('!worksite','!worksite-810','display-date','true'),('!worksite','!worksite-810','filter-param','3'),('!worksite','!worksite-810','display-time','true'),('!worksite','!worksite-810','sound-alert','true'),('!worksite','!worksite-810','filter-type','SelectMessagesByTime'),('!worksite','!worksite-810','display-user','true'),('mercury','mercury-710','resources_mode','dropbox'),('mercury','mercury-810','display-date','true'),('mercury','mercury-810','filter-param','3'),('mercury','mercury-810','display-time','true'),('mercury','mercury-810','sound-alert','true'),('mercury','mercury-810','filter-type','SelectMessagesByTime'),('mercury','mercury-810','display-user','true'),('PortfolioAdmin','e99c47e7-6b7d-4409-a4a9-32871d8b1579','theospi.toolListenerId','org.theospi.portfolio.security.mgt.ToolPermissionManager.glossaryGlobal'),('PortfolioAdmin','b0b38507-4604-44e0-b690-cae897aefa8b','theospi.toolListenerId','org.theospi.portfolio.security.mgt.ToolPermissionManager.presentationLayout'),('PortfolioAdmin','09bbec81-8322-4423-8b65-acc636ed2105','theospi.toolListenerId','org.theospi.portfolio.security.mgt.ToolPermissionManager.style'),('PortfolioAdmin','b0b38507-4604-44e0-b690-cae897aefa8b','theospi.resetUrl','/listLayout.osp'),('PortfolioAdmin','12860648-d921-4bdf-91a5-9fae8e76334b','theospi.toolListenerId','org.theospi.portfolio.security.mgt.ToolPermissionManager.presentationTemplate'),('PortfolioAdmin','05b43286-2d15-42a0-b495-23511cb84a11','theospi.resetUrl','/member/listPresentation.osp'),('PortfolioAdmin','05b43286-2d15-42a0-b495-23511cb84a11','theospi.toolListenerId','org.theospi.portfolio.security.mgt.ToolPermissionManager.presentation'),('PortfolioAdmin','12860648-d921-4bdf-91a5-9fae8e76334b','theospi.resetUrl','/member/listTemplate.osp'),('~d1430d8d-af0b-49e6-8c39-2d253673319a','a521e0b2-45a0-4c75-b945-f1eb2cee5f14','include-password','true');
/*!40000 ALTER TABLE `SAKAI_SITE_TOOL_PROPERTY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SITE_USER`
--

DROP TABLE IF EXISTS `SAKAI_SITE_USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SITE_USER` (
  `SITE_ID` varchar(99) NOT NULL,
  `USER_ID` varchar(99) NOT NULL,
  `PERMISSION` int(11) NOT NULL,
  PRIMARY KEY (`SITE_ID`,`USER_ID`),
  KEY `IE_SAKAI_SITE_USER_USER` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SITE_USER`
--

LOCK TABLES `SAKAI_SITE_USER` WRITE;
/*!40000 ALTER TABLE `SAKAI_SITE_USER` DISABLE KEYS */;
INSERT INTO `SAKAI_SITE_USER` VALUES ('!admin','admin',-1),('PortfolioAdmin','admin',-1),('citationsAdmin','admin',-1),('mercury','admin',-1),('~d1430d8d-af0b-49e6-8c39-2d253673319a','d1430d8d-af0b-49e6-8c39-2d253673319a',-1),('92baf195-be33-4a5b-b378-6d96e9665ffc','d1430d8d-af0b-49e6-8c39-2d253673319a',-1),('92baf195-be33-4a5b-b378-6d96e9665ffc','2340fa22-539c-4da2-8206-827a177e2c43',-1),('92baf195-be33-4a5b-b378-6d96e9665ffc','admin',-1),('92baf195-be33-4a5b-b378-6d96e9665ffc','36fba6f3-216d-49d7-bf91-add309e764f8',-1),('92baf195-be33-4a5b-b378-6d96e9665ffc','33d968c8-265d-4306-8ebc-23e68849a33b',-1),('92baf195-be33-4a5b-b378-6d96e9665ffc','27ed9197-681c-4f9d-8b0c-c16b11d7f5e6',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','184d9822-8448-4b0b-aef3-b2662d1f0e4f',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','bf744747-98d8-4858-8877-5dc9f1dfcdc2',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','7c5a341c-cdae-4a86-a0bb-7c543e5ed87b',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','5c6a69e2-3100-4fb2-b6cf-38a9d5bc92e4',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','b50bf16e-552c-4d0a-aa9c-a1f5bee65dae',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','7326d09e-0df2-493f-8709-e81cd960b141',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','4ea3676d-3fbe-40ed-affe-f46e45bed47c',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','464ab2fd-1148-4920-8197-afb252d79f25',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','dff0baa1-64bf-4f7f-a36e-b03d34dfa3fd',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','783c8415-1a2e-4450-8385-7d4a9b690057',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','6c15bf0f-09ea-4b73-a0fe-c7408e13ddfe',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','b40da10f-3530-4546-b8bd-41004aadf81e',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','d38a9a35-e8b1-46fb-bdb5-97eb3f1e4f13',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','12011fae-64cc-4210-a513-d4f47fa2ab40',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','c3ea4458-bb78-46fa-8c70-c953f638ee0c',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','9aecc130-9c34-49b9-80fc-5ffdbb657d73',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','a22b2a23-6495-4084-8442-bc32125fd2a6',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e8276751-f824-4144-8b51-b1344ca9cd55',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','869ba04f-28b9-469e-a01c-f18fec920a9f',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','f6e77be8-0c4e-4730-8c5f-3ab8e932fab6',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','a994288e-028c-46a1-8c06-91b6328f8cf7',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','8755dc9c-706b-4c48-9531-6694c12c4154',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','93b7ec01-39fa-4ac6-a0c7-4087a8960511',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e01bdf14-8bdd-424d-b8e6-970d62643857',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','b0eb288b-8d79-4ef2-b1b4-d78e70b7973b',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','b5bbaab7-5e41-4a2a-9497-512eb12adf8e',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ef9f40e9-7fd3-4241-805a-697ae4aeeaac',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','852dde27-c7e9-4bc5-982e-f927d80011e1',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','1114674c-46c0-40ca-8c27-906d15fbe4c8',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','025e9195-08fd-4a76-a142-0dbece7b1984',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','6e680d07-601e-4adb-a581-6f4d301de96a',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','004e56fe-3d4d-4e03-bd49-21eedacab82c',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','32123e79-3966-4bfb-9cb8-32a4a2409966',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','1db66cb6-37b8-4906-aabc-71d0eef18068',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','da0885d7-0ff7-44a4-b68a-ed1397105208',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','229b3e48-07e1-4e96-b1c1-4fdc424baa34',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','43c71a7f-e4f3-4eef-85a4-7217b03eb0e0',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','b356f123-ea78-40ac-9bae-dd18a164d5e5',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','1e700067-f42d-4b29-89cc-5bf6298c67a8',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','23a4562f-3ad2-4b42-8de7-d92754deb8fc',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','af492ecd-a9cb-45dd-9e08-e4892ffce38c',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','fb2fe8bb-0924-4a29-a4f5-0ea1e0573247',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','4dde7d46-3f04-43cb-a62a-42acc2d10ff1',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','a01062c6-7eb3-4c29-aab5-1ee72716962e',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e8a68356-16ff-406f-8970-1b2f879c43d6',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','a577d15f-97ff-4513-bfde-58c28728e390',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','9c3e321d-ffcf-4a2e-b525-4c98c5bb2348',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','bc0b9585-18d6-45e0-a132-13010a00350e',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','6b9c318d-7347-4322-afbb-e9b3e734e4fd',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','2cad9454-b4e6-40b7-960b-a6955b821886',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','c638e427-6560-441c-93fa-a53eb2f0ea22',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ec2f730b-65d6-4b11-a4f1-813d989500b0',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','2fe9e739-658a-43f1-a5d7-4b209640b2d5',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','4ecd482a-e961-4f5a-a927-5c4984e2b0d2',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ea215a2a-edb1-47e2-a6b8-5fccbc194b98',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','d4ac4d20-c126-4b4b-bbf7-726e54a9d046',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','cdc567c7-a70d-428d-9717-70c54e615f5e',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','6ade0933-a316-4e0d-9dbf-942559d45a44',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','280c67f5-2ab2-44cf-9d98-f348688c6178',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','4b6e2678-e97a-4882-afa8-4bee785e0d55',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','1f98e91a-6f4f-4f98-9fdc-73dcb64f4206',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','d229185c-7df3-4f5f-a305-4735c88a3a01',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','0689e163-b621-4be1-bd15-0573d81b15d8',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','7765a558-2d83-4af8-adc6-81b51e0a0b20',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','38d7bebf-0524-485d-9952-6870eaa22075',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','f51996fc-ef2d-4d08-bc73-5c369a1f0c7f',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','8cbe7f4c-dfc7-4138-b8a2-5bbfe70bc72f',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','8a63abf2-777a-4a19-b1f7-f82763a32b72',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','231939d6-201a-4170-8fce-f3f85de45975',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','a8f04f79-8a2a-4a37-abce-683d076d5bb1',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','a82185d9-0c49-4fe2-a558-de87927740eb',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','b786fba6-2233-4061-9070-143d9fc9da9a',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','35c66078-1217-439b-bb19-41f94e359964',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','71145325-848d-44ff-8dbc-ff6d8ab04d75',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','6716a043-275b-4d28-b235-b313445efbef',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','07a7a71d-8feb-427f-99b1-1df351dfb290',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','b8874b5d-6022-4548-af9f-cf3e9ed2ea76',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','6d84cc4f-5e31-40a0-825f-0fcc8f9d48d3',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','fdeb0b4c-7ccd-4864-802a-3e21e18001f8',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e9827285-8ef5-4b5c-8419-e8ac540010c2',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','bf09a537-3fcc-4032-ab5f-aced1839513a',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','00645336-54be-4062-9cb5-f14ef811dabe',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','8a22ebd8-7768-443c-b317-ea981814628e',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','18e2e188-845e-4ce1-a036-19302dcc84cf',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','5991188b-9d84-4c98-9ce4-a92f385b5a0c',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','4369a381-7a8a-42ec-b2ee-e2aca63b48f8',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','9ef72519-430d-4ddf-a4c7-5be254ab1e9e',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','a352ddcc-c489-4f23-b16a-0243776f18c3',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','83458e5e-f20c-4213-bf71-54c003c35af1',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e7f5fe3c-c24d-4a4d-9264-55a8762fcac5',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','c28c6d0e-ed7e-44a6-a7a0-cf507c80e85a',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','b428c076-476b-4cfd-a4c2-1cceb46ec194',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ba09c144-ddc6-4137-a675-0ecdd57e63fb',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','a635da81-52fd-48bd-8b7f-d309e41c4f43',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e603171a-e7b3-4d84-82f1-5d848351d32b',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','bc184bd7-f864-4e88-8984-d11bde38e6d7',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','874d49d3-d7ef-4ddd-a606-db4a024092d3',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','4e3695ee-b1a2-4093-bc60-cc4813aac669',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','d21c7e23-bda0-47b3-a7c0-495e0ce56ef4',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','90d1b7fd-a8c4-4061-b9db-6bc1f6fbb830',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','19afe12a-e810-48b1-88f3-3583a9dc786e',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','4ed4a6e0-cdf7-4f80-b445-2e2be39e5f16',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','fe58e748-b84e-4b4e-be17-9871596ce3a2',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','62295668-bc06-48e7-9982-c9eae994b3a1',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','871b0b76-c679-4df1-9576-90d9f8bc2e7f',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ef69e0f1-1cbd-44d5-a9ec-33b721096a07',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','8977a479-f55a-4d17-8386-f07d0dfd50d6',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e67957d2-967e-4f2c-9e12-81991c5edbe7',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','1c0fc61c-458b-4f1c-8e07-94f8ea068172',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','2441a09b-c7e5-4521-8177-6aa100fd9288',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','cdb9b220-a3ee-4655-93b3-df7e9ba8065f',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','5ee07051-daf2-43d7-9946-b962b2e26492',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','88ecaf43-a585-4600-bb6b-9cd7517727cd',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e1805a1d-4f4d-4769-bf6f-0ceef96a2716',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','df0da763-1b07-4f84-b23c-ff19a69798bb',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','249ede3e-e1df-44e4-b441-fad88bc99e01',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','efd34a54-8d40-48dd-9b75-67e4b0d8cd47',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','7d39489a-5fc5-4851-b0b0-99265b81368d',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','2e930f7d-c4bc-4f9d-bc47-08e10781cc8c',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','c9dd9324-8fa5-4bd7-8f2f-bfbc81ec0aaf',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','95af1aa1-e663-4d45-b273-1d779b7822fb',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','3a97f645-fdfc-4544-b281-945333aaaf30',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ee86526c-0087-489e-b553-260e3d07e1fe',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','546be8b2-1ed7-4608-aeaa-9343bee7630e',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','388740db-cc93-460a-962e-73638d15088d',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','b64911c3-c018-47d4-b65c-7c9dea9a64b3',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','c19bcc71-53a7-4712-95dd-d0012e9e7592',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ee638295-fd3a-4f20-84c8-74934f898159',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','5cc7dd88-e841-44e0-82c2-1cd97b8b33ba',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','268ec015-d248-4742-87cd-593092e7ef84',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e700872a-9fa0-446e-aff9-6111fbe76ac8',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','5386549e-c9e2-403b-bd68-11471ae2af6f',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','b75ade34-fd28-43ae-9b3d-d4441b25621e',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e1383061-fac9-496a-aa9b-b036615c03c4',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','c8996c6a-3368-4d73-95cb-e8be868f9541',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','6174bb8a-07e2-4a35-ae7b-add5e474e50a',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','cec0bd35-eb6f-4fa7-8210-70ce84a70b43',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e6e892e4-e16e-47c1-aada-6e22e7cb8858',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','06f67e74-e7c8-4fe2-8625-7139b496edfc',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','a7607355-0aa0-43b5-b9da-2bae135a42c9',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','f9efede8-9ebe-403a-bf88-53d2a53f867a',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ff504578-c994-4a89-bb4e-a672ee817064',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','7fbbbd9f-7900-4488-aec7-d9898e11ea2d',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','0648876a-3b02-419b-9b8b-6a232c6d5574',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','27377926-4baa-4edd-9455-c331d21d4f35',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','58eee43e-f6aa-40e5-b927-155ae5b30920',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','94a280a1-495c-409f-813b-78b78f218751',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e343173a-5ea9-452a-b325-aa21ea24bcea',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','0d0147df-c9f7-4e10-ac5c-2e8d97e42394',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ba5c01ce-28d0-4544-a0ef-d99e1f96e8eb',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','a6c19c10-e4c1-4d9d-8838-a3ff29dbd4ce',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','0c7b9b12-bc79-4da6-b8f6-b18057cbe882',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','9d9ba6b9-1b16-4dc9-bcf7-e17c13d26878',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','9b118863-1af5-4289-9455-ad635e5eb2e6',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','db1495af-bef1-431d-87fb-ce4ec0d650c3',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','90eab2b6-1cf0-48a7-93e8-417fa4d1f34a',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ac239a61-c642-468f-a16a-f6976892b93c',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','29ec91ea-ea08-4874-969a-aa414c65efc3',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ebc7f28f-8270-49e5-a86f-75e47fe1c480',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','38b0bb81-007f-401a-97ae-53a55cb30bae',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','d1bad65a-5e59-4ef5-af4c-5be4e6c85064',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ae415b58-6117-4807-9050-a8d630138142',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','b93d5ba4-c733-4c0c-b106-01b66c5c1244',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','ebb7415d-ce79-4c2b-b621-e57076e7452c',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','135239e0-3182-459f-8fc6-c1e0b89e0da9',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','eec9a2ba-040a-4766-942b-3f911c2743d9',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','7f1a7baf-6f7d-4c99-8c56-ec2a0f2b6839',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','f54853a9-a0a3-42f1-8639-3ec5b09e5401',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','9cba5865-394e-41a7-8be0-be2209868e09',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','bb6954bd-8040-48e5-950e-9f85817b9f7d',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','17b70709-f198-4395-8d92-e854d7d34958',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','1faf011d-b5d9-450b-b651-5e679e8c77e6',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','3a3e1a4b-1911-4896-add7-73ea93885d2e',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','69791ed2-7461-42bc-8699-d8be328e2ee1',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','e30f7fbb-4abc-49d6-b521-c601e72284d2',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','521c3ad4-c045-4007-bccc-9d392d73c58f',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','b50e4212-4bc4-4915-bffb-f793103c05ca',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','4d58c188-b285-4322-9503-983620f1b0f8',1),('92baf195-be33-4a5b-b378-6d96e9665ffc','c5d6208c-d5a4-44eb-a9d6-bebc19531c5a',1);
/*!40000 ALTER TABLE `SAKAI_SITE_USER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SYLLABUS_ATTACH`
--

DROP TABLE IF EXISTS `SAKAI_SYLLABUS_ATTACH`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SYLLABUS_ATTACH` (
  `syllabusAttachId` bigint(20) NOT NULL AUTO_INCREMENT,
  `lockId` int(11) NOT NULL,
  `attachmentId` text NOT NULL,
  `syllabusAttachName` text NOT NULL,
  `syllabusAttachSize` text,
  `syllabusAttachType` text,
  `createdBy` text,
  `syllabusAttachUrl` text NOT NULL,
  `lastModifiedBy` text,
  `syllabusId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`syllabusAttachId`),
  KEY `FK4BF41E45AE4A118A` (`syllabusId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SYLLABUS_ATTACH`
--

LOCK TABLES `SAKAI_SYLLABUS_ATTACH` WRITE;
/*!40000 ALTER TABLE `SAKAI_SYLLABUS_ATTACH` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_SYLLABUS_ATTACH` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SYLLABUS_DATA`
--

DROP TABLE IF EXISTS `SAKAI_SYLLABUS_DATA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SYLLABUS_DATA` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lockId` int(11) NOT NULL,
  `asset` mediumtext,
  `position_c` int(11) NOT NULL,
  `title` text,
  `xview` varchar(16) DEFAULT NULL,
  `status` varchar(64) DEFAULT NULL,
  `emailNotification` varchar(128) DEFAULT NULL,
  `surrogateKey` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3BC123AA391BE33A` (`surrogateKey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SYLLABUS_DATA`
--

LOCK TABLES `SAKAI_SYLLABUS_DATA` WRITE;
/*!40000 ALTER TABLE `SAKAI_SYLLABUS_DATA` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_SYLLABUS_DATA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_SYLLABUS_ITEM`
--

DROP TABLE IF EXISTS `SAKAI_SYLLABUS_ITEM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_SYLLABUS_ITEM` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lockId` int(11) NOT NULL,
  `userId` varchar(36) NOT NULL,
  `contextId` varchar(255) NOT NULL,
  `redirectURL` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`,`contextId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_SYLLABUS_ITEM`
--

LOCK TABLES `SAKAI_SYLLABUS_ITEM` WRITE;
/*!40000 ALTER TABLE `SAKAI_SYLLABUS_ITEM` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_SYLLABUS_ITEM` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_USER`
--

DROP TABLE IF EXISTS `SAKAI_USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_USER` (
  `USER_ID` varchar(99) NOT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `EMAIL_LC` varchar(255) DEFAULT NULL,
  `FIRST_NAME` varchar(255) DEFAULT NULL,
  `LAST_NAME` varchar(255) DEFAULT NULL,
  `TYPE` varchar(255) DEFAULT NULL,
  `PW` varchar(255) DEFAULT NULL,
  `CREATEDBY` varchar(99) NOT NULL,
  `MODIFIEDBY` varchar(99) NOT NULL,
  `CREATEDON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `MODIFIEDON` datetime NOT NULL,
  PRIMARY KEY (`USER_ID`),
  KEY `IE_SAKAI_USER_CREATED` (`CREATEDBY`,`CREATEDON`),
  KEY `IE_SAKAI_USER_MODDED` (`MODIFIEDBY`,`MODIFIEDON`),
  KEY `IE_SAKAI_USER_EMAIL` (`EMAIL_LC`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_USER`
--

LOCK TABLES `SAKAI_USER` WRITE;
/*!40000 ALTER TABLE `SAKAI_USER` DISABLE KEYS */;
INSERT INTO `SAKAI_USER` VALUES ('admin','','','Sakai','Administrator','','ISMvKXpXpadDiUoOSoAfww==','admin','admin','2011-04-13 18:59:24','2011-04-13 14:59:24'),('postmaster','','','Sakai','Postmaster','','','postmaster','postmaster','2011-04-13 18:59:24','2011-04-13 14:59:24');
/*!40000 ALTER TABLE `SAKAI_USER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_USER_ID_MAP`
--

DROP TABLE IF EXISTS `SAKAI_USER_ID_MAP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_USER_ID_MAP` (
  `USER_ID` varchar(99) NOT NULL,
  `EID` varchar(255) NOT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `AK_SAKAI_USER_ID_MAP_EID` (`EID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_USER_ID_MAP`
--

LOCK TABLES `SAKAI_USER_ID_MAP` WRITE;
/*!40000 ALTER TABLE `SAKAI_USER_ID_MAP` DISABLE KEYS */;
INSERT INTO `SAKAI_USER_ID_MAP` VALUES ('admin','admin'),('postmaster','postmaster'),('d1430d8d-af0b-49e6-8c39-2d253673319a','instructor'),('ec2f730b-65d6-4b11-a4f1-813d989500b0','student0011'),('6d84cc4f-5e31-40a0-825f-0fcc8f9d48d3','student0010'),('62295668-bc06-48e7-9982-c9eae994b3a1','student0013'),('b40da10f-3530-4546-b8bd-41004aadf81e','student0012'),('6b9c318d-7347-4322-afbb-e9b3e734e4fd','student0015'),('bf09a537-3fcc-4032-ab5f-aced1839513a','student0014'),('4b6e2678-e97a-4882-afa8-4bee785e0d55','student0112'),('e1805a1d-4f4d-4769-bf6f-0ceef96a2716','student0111'),('19afe12a-e810-48b1-88f3-3583a9dc786e','student0114'),('874d49d3-d7ef-4ddd-a606-db4a024092d3','student0113'),('38d7bebf-0524-485d-9952-6870eaa22075','student0110'),('a82185d9-0c49-4fe2-a558-de87927740eb','student0099'),('c3ea4458-bb78-46fa-8c70-c953f638ee0c','student0108'),('ee86526c-0087-489e-b553-260e3d07e1fe','student0098'),('ebb7415d-ce79-4c2b-b621-e57076e7452c','student0109'),('af492ecd-a9cb-45dd-9e08-e4892ffce38c','student0097'),('07a7a71d-8feb-427f-99b1-1df351dfb290','student0096'),('7326d09e-0df2-493f-8709-e81cd960b141','student0104'),('b5bbaab7-5e41-4a2a-9497-512eb12adf8e','student0105'),('b356f123-ea78-40ac-9bae-dd18a164d5e5','student0106'),('da0885d7-0ff7-44a4-b68a-ed1397105208','student0107'),('4e3695ee-b1a2-4093-bc60-cc4813aac669','student0091'),('f54853a9-a0a3-42f1-8639-3ec5b09e5401','student0005'),('a01062c6-7eb3-4c29-aab5-1ee72716962e','student0090'),('ee638295-fd3a-4f20-84c8-74934f898159','student0006'),('4ecd482a-e961-4f5a-a927-5c4984e2b0d2','student0007'),('025e9195-08fd-4a76-a142-0dbece7b1984','student0008'),('88ecaf43-a585-4600-bb6b-9cd7517727cd','student0095'),('06f67e74-e7c8-4fe2-8625-7139b496edfc','student0009'),('bc184bd7-f864-4e88-8984-d11bde38e6d7','student0094'),('0c7b9b12-bc79-4da6-b8f6-b18057cbe882','student0093'),('a6c19c10-e4c1-4d9d-8838-a3ff29dbd4ce','student0092'),('69791ed2-7461-42bc-8699-d8be328e2ee1','student0022'),('c19bcc71-53a7-4712-95dd-d0012e9e7592','student0021'),('db1495af-bef1-431d-87fb-ce4ec0d650c3','student0020'),('ebc7f28f-8270-49e5-a86f-75e47fe1c480','student0026'),('35c66078-1217-439b-bb19-41f94e359964','student0025'),('9aecc130-9c34-49b9-80fc-5ffdbb657d73','student0024'),('bc0b9585-18d6-45e0-a132-13010a00350e','student0023'),('cdb9b220-a3ee-4655-93b3-df7e9ba8065f','student0125'),('546be8b2-1ed7-4608-aeaa-9343bee7630e','student0124'),('1db66cb6-37b8-4906-aabc-71d0eef18068','student0123'),('6174bb8a-07e2-4a35-ae7b-add5e474e50a','student0122'),('5386549e-c9e2-403b-bd68-11471ae2af6f','student0121'),('71145325-848d-44ff-8dbc-ff6d8ab04d75','student0120'),('38b0bb81-007f-401a-97ae-53a55cb30bae','student0119'),('3a97f645-fdfc-4544-b281-945333aaaf30','student0117'),('18e2e188-845e-4ce1-a036-19302dcc84cf','student0118'),('7f1a7baf-6f7d-4c99-8c56-ec2a0f2b6839','student0115'),('7d39489a-5fc5-4851-b0b0-99265b81368d','student0116'),('58eee43e-f6aa-40e5-b927-155ae5b30920','student0018'),('27ed9197-681c-4f9d-8b0c-c16b11d7f5e6','student0019'),('fdeb0b4c-7ccd-4864-802a-3e21e18001f8','student0016'),('9ef72519-430d-4ddf-a4c7-5be254ab1e9e','student0017'),('43c71a7f-e4f3-4eef-85a4-7217b03eb0e0','student0130'),('2e930f7d-c4bc-4f9d-bc47-08e10781cc8c','student0132'),('1c0fc61c-458b-4f1c-8e07-94f8ea068172','student0131'),('a352ddcc-c489-4f23-b16a-0243776f18c3','student0134'),('3a3e1a4b-1911-4896-add7-73ea93885d2e','student0133'),('b93d5ba4-c733-4c0c-b106-01b66c5c1244','student0136'),('9cba5865-394e-41a7-8be0-be2209868e09','student0135'),('fb2fe8bb-0924-4a29-a4f5-0ea1e0573247','student0126'),('783c8415-1a2e-4450-8385-7d4a9b690057','student0127'),('94a280a1-495c-409f-813b-78b78f218751','student0079'),('4ea3676d-3fbe-40ed-affe-f46e45bed47c','student0128'),('d1bad65a-5e59-4ef5-af4c-5be4e6c85064','student0078'),('c8996c6a-3368-4d73-95cb-e8be868f9541','student0129'),('ae415b58-6117-4807-9050-a8d630138142','student0077'),('e8276751-f824-4144-8b51-b1344ca9cd55','student0076'),('93b7ec01-39fa-4ac6-a0c7-4087a8960511','student0075'),('bf744747-98d8-4858-8877-5dc9f1dfcdc2','student0074'),('a7607355-0aa0-43b5-b9da-2bae135a42c9','student0073'),('a22b2a23-6495-4084-8442-bc32125fd2a6','student0072'),('36fba6f3-216d-49d7-bf91-add309e764f8','instructor2'),('b50e4212-4bc4-4915-bffb-f793103c05ca','student0071'),('33d968c8-265d-4306-8ebc-23e68849a33b','instructor1'),('6e680d07-601e-4adb-a581-6f4d301de96a','student0070'),('1f98e91a-6f4f-4f98-9fdc-73dcb64f4206','student0004'),('efd34a54-8d40-48dd-9b75-67e4b0d8cd47','student0003'),('d21c7e23-bda0-47b3-a7c0-495e0ce56ef4','student0002'),('ef69e0f1-1cbd-44d5-a9ec-33b721096a07','student0001'),('f51996fc-ef2d-4d08-bc73-5c369a1f0c7f','student0143'),('e1383061-fac9-496a-aa9b-b036615c03c4','student0142'),('ac239a61-c642-468f-a16a-f6976892b93c','student0141'),('7fbbbd9f-7900-4488-aec7-d9898e11ea2d','student0140'),('e603171a-e7b3-4d84-82f1-5d848351d32b','student0147'),('ff504578-c994-4a89-bb4e-a672ee817064','student0146'),('0648876a-3b02-419b-9b8b-6a232c6d5574','student0145'),('5c6a69e2-3100-4fb2-b6cf-38a9d5bc92e4','student0144'),('8a63abf2-777a-4a19-b1f7-f82763a32b72','student0139'),('b786fba6-2233-4061-9070-143d9fc9da9a','student0089'),('23a4562f-3ad2-4b42-8de7-d92754deb8fc','student0137'),('871b0b76-c679-4df1-9576-90d9f8bc2e7f','student0138'),('00645336-54be-4062-9cb5-f14ef811dabe','student0086'),('f9efede8-9ebe-403a-bf88-53d2a53f867a','student0085'),('e6e892e4-e16e-47c1-aada-6e22e7cb8858','student0088'),('6716a043-275b-4d28-b235-b313445efbef','student0087'),('27377926-4baa-4edd-9455-c331d21d4f35','student0082'),('0689e163-b621-4be1-bd15-0573d81b15d8','student0081'),('b75ade34-fd28-43ae-9b3d-d4441b25621e','student0084'),('cec0bd35-eb6f-4fa7-8210-70ce84a70b43','student0083'),('2340fa22-539c-4da2-8206-827a177e2c43','da1'),('6ade0933-a316-4e0d-9dbf-942559d45a44','student0080'),('8cbe7f4c-dfc7-4138-b8a2-5bbfe70bc72f','student0049'),('b50bf16e-552c-4d0a-aa9c-a1f5bee65dae','student0149'),('90d1b7fd-a8c4-4061-b9db-6bc1f6fbb830','student0148'),('b8874b5d-6022-4548-af9f-cf3e9ed2ea76','student0157'),('83458e5e-f20c-4213-bf71-54c003c35af1','student0158'),('dff0baa1-64bf-4f7f-a36e-b03d34dfa3fd','student0155'),('ba09c144-ddc6-4137-a675-0ecdd57e63fb','student0156'),('c638e427-6560-441c-93fa-a53eb2f0ea22','student0050'),('e8a68356-16ff-406f-8970-1b2f879c43d6','student0153'),('8a22ebd8-7768-443c-b317-ea981814628e','student0051'),('229b3e48-07e1-4e96-b1c1-4fdc424baa34','student0154'),('521c3ad4-c045-4007-bccc-9d392d73c58f','student0151'),('ba5c01ce-28d0-4544-a0ef-d99e1f96e8eb','student0152'),('184d9822-8448-4b0b-aef3-b2662d1f0e4f','student0054'),('ea215a2a-edb1-47e2-a6b8-5fccbc194b98','student0150'),('280c67f5-2ab2-44cf-9d98-f348688c6178','student0055'),('2fe9e739-658a-43f1-a5d7-4b209640b2d5','student0052'),('17b70709-f198-4395-8d92-e854d7d34958','student0053'),('eec9a2ba-040a-4766-942b-3f911c2743d9','student0058'),('135239e0-3182-459f-8fc6-c1e0b89e0da9','student0059'),('1faf011d-b5d9-450b-b651-5e679e8c77e6','student0056'),('a577d15f-97ff-4513-bfde-58c28728e390','student0057'),('6c15bf0f-09ea-4b73-a0fe-c7408e13ddfe','student0159'),('e343173a-5ea9-452a-b325-aa21ea24bcea','student0166'),('231939d6-201a-4170-8fce-f3f85de45975','student0167'),('9d9ba6b9-1b16-4dc9-bcf7-e17c13d26878','student0168'),('12011fae-64cc-4210-a513-d4f47fa2ab40','student0169'),('a8f04f79-8a2a-4a37-abce-683d076d5bb1','student0162'),('852dde27-c7e9-4bc5-982e-f927d80011e1','student0163'),('e67957d2-967e-4f2c-9e12-81991c5edbe7','student0060'),('c9dd9324-8fa5-4bd7-8f2f-bfbc81ec0aaf','student0164'),('e01bdf14-8bdd-424d-b8e6-970d62643857','student0061'),('268ec015-d248-4742-87cd-593092e7ef84','student0062'),('95af1aa1-e663-4d45-b273-1d779b7822fb','student0165'),('249ede3e-e1df-44e4-b441-fad88bc99e01','student0063'),('004e56fe-3d4d-4e03-bd49-21eedacab82c','student0064'),('e7f5fe3c-c24d-4a4d-9264-55a8762fcac5','student0160'),('a635da81-52fd-48bd-8b7f-d309e41c4f43','student0065'),('8977a479-f55a-4d17-8386-f07d0dfd50d6','student0161'),('b0eb288b-8d79-4ef2-b1b4-d78e70b7973b','student0066'),('d4ac4d20-c126-4b4b-bbf7-726e54a9d046','student0067'),('d229185c-7df3-4f5f-a305-4735c88a3a01','student0068'),('4dde7d46-3f04-43cb-a62a-42acc2d10ff1','student0069'),('9c3e321d-ffcf-4a2e-b525-4c98c5bb2348','student0029'),('f6e77be8-0c4e-4730-8c5f-3ab8e932fab6','student0028'),('e700872a-9fa0-446e-aff9-6111fbe76ac8','student0027'),('c28c6d0e-ed7e-44a6-a7a0-cf507c80e85a','student0175'),('0d0147df-c9f7-4e10-ac5c-2e8d97e42394','student0176'),('2441a09b-c7e5-4521-8177-6aa100fd9288','student0173'),('7c5a341c-cdae-4a86-a0bb-7c543e5ed87b','student0174'),('b428c076-476b-4cfd-a4c2-1cceb46ec194','student0179'),('1e700067-f42d-4b29-89cc-5bf6298c67a8','student0177'),('df0da763-1b07-4f84-b23c-ff19a69798bb','student0178'),('c5d6208c-d5a4-44eb-a9d6-bebc19531c5a','student0036'),('5cc7dd88-e841-44e0-82c2-1cd97b8b33ba','student0037'),('1114674c-46c0-40ca-8c27-906d15fbe4c8','student0034'),('a994288e-028c-46a1-8c06-91b6328f8cf7','student0035'),('5ee07051-daf2-43d7-9946-b962b2e26492','student0171'),('5991188b-9d84-4c98-9ce4-a92f385b5a0c','student0032'),('4ed4a6e0-cdf7-4f80-b445-2e2be39e5f16','student0172'),('9b118863-1af5-4289-9455-ad635e5eb2e6','student0033'),('e9827285-8ef5-4b5c-8419-e8ac540010c2','student0030'),('4369a381-7a8a-42ec-b2ee-e2aca63b48f8','student0170'),('29ec91ea-ea08-4874-969a-aa414c65efc3','student0031'),('4d58c188-b285-4322-9503-983620f1b0f8','student0039'),('8755dc9c-706b-4c48-9531-6694c12c4154','student0102'),('b64911c3-c018-47d4-b65c-7c9dea9a64b3','student0038'),('cdc567c7-a70d-428d-9717-70c54e615f5e','student0103'),('e30f7fbb-4abc-49d6-b521-c601e72284d2','student0100'),('d38a9a35-e8b1-46fb-bdb5-97eb3f1e4f13','student0101'),('ef9f40e9-7fd3-4241-805a-697ae4aeeaac','student0040'),('bb6954bd-8040-48e5-950e-9f85817b9f7d','student0045'),('7765a558-2d83-4af8-adc6-81b51e0a0b20','student0046'),('464ab2fd-1148-4920-8197-afb252d79f25','student0047'),('fe58e748-b84e-4b4e-be17-9871596ce3a2','student0048'),('2cad9454-b4e6-40b7-960b-a6955b821886','student0180'),('388740db-cc93-460a-962e-73638d15088d','student0041'),('32123e79-3966-4bfb-9cb8-32a4a2409966','student0042'),('869ba04f-28b9-469e-a01c-f18fec920a9f','student0043'),('90eab2b6-1cf0-48a7-93e8-417fa4d1f34a','student0044');
/*!40000 ALTER TABLE `SAKAI_USER_ID_MAP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAKAI_USER_PROPERTY`
--

DROP TABLE IF EXISTS `SAKAI_USER_PROPERTY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAKAI_USER_PROPERTY` (
  `USER_ID` varchar(99) NOT NULL,
  `NAME` varchar(99) NOT NULL,
  `VALUE` longtext,
  PRIMARY KEY (`USER_ID`,`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAKAI_USER_PROPERTY`
--

LOCK TABLES `SAKAI_USER_PROPERTY` WRITE;
/*!40000 ALTER TABLE `SAKAI_USER_PROPERTY` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAKAI_USER_PROPERTY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ANSWERFEEDBACK_T`
--

DROP TABLE IF EXISTS `SAM_ANSWERFEEDBACK_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ANSWERFEEDBACK_T` (
  `ANSWERFEEDBACKID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ANSWERID` bigint(20) NOT NULL,
  `TYPEID` varchar(255) DEFAULT NULL,
  `TEXT` text,
  PRIMARY KEY (`ANSWERFEEDBACKID`),
  KEY `FK58CEF0D8DEC85889` (`ANSWERID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ANSWERFEEDBACK_T`
--

LOCK TABLES `SAM_ANSWERFEEDBACK_T` WRITE;
/*!40000 ALTER TABLE `SAM_ANSWERFEEDBACK_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_ANSWERFEEDBACK_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ANSWER_T`
--

DROP TABLE IF EXISTS `SAM_ANSWER_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ANSWER_T` (
  `ANSWERID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMTEXTID` bigint(20) NOT NULL,
  `ITEMID` bigint(20) NOT NULL,
  `TEXT` text,
  `SEQUENCE` bigint(20) NOT NULL,
  `LABEL` varchar(20) DEFAULT NULL,
  `ISCORRECT` bit(1) DEFAULT NULL,
  `GRADE` varchar(80) DEFAULT NULL,
  `SCORE` float DEFAULT NULL,
  `DISCOUNT` float DEFAULT NULL,
  `PARTIAL_CREDIT` float DEFAULT NULL,
  PRIMARY KEY (`ANSWERID`),
  KEY `FKDD0580933288DBBD` (`ITEMID`),
  KEY `FKDD058093278A7DAD` (`ITEMTEXTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ANSWER_T`
--

LOCK TABLES `SAM_ANSWER_T` WRITE;
/*!40000 ALTER TABLE `SAM_ANSWER_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_ANSWER_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ASSESSACCESSCONTROL_T`
--

DROP TABLE IF EXISTS `SAM_ASSESSACCESSCONTROL_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ASSESSACCESSCONTROL_T` (
  `ASSESSMENTID` bigint(20) NOT NULL,
  `SUBMISSIONSALLOWED` int(11) DEFAULT NULL,
  `UNLIMITEDSUBMISSIONS` bit(1) DEFAULT NULL,
  `SUBMISSIONSSAVED` int(11) DEFAULT NULL,
  `ASSESSMENTFORMAT` int(11) DEFAULT NULL,
  `BOOKMARKINGITEM` int(11) DEFAULT NULL,
  `TIMELIMIT` int(11) DEFAULT NULL,
  `TIMEDASSESSMENT` int(11) DEFAULT NULL,
  `RETRYALLOWED` int(11) DEFAULT NULL,
  `LATEHANDLING` int(11) DEFAULT NULL,
  `STARTDATE` datetime DEFAULT NULL,
  `DUEDATE` datetime DEFAULT NULL,
  `SCOREDATE` datetime DEFAULT NULL,
  `FEEDBACKDATE` datetime DEFAULT NULL,
  `RETRACTDATE` datetime DEFAULT NULL,
  `AUTOSUBMIT` int(11) DEFAULT NULL,
  `ITEMNAVIGATION` int(11) DEFAULT NULL,
  `ITEMNUMBERING` int(11) DEFAULT NULL,
  `SUBMISSIONMESSAGE` text,
  `RELEASETO` varchar(255) DEFAULT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `FINALPAGEURL` text,
  `MARKFORREVIEW` int(11) DEFAULT NULL,
  PRIMARY KEY (`ASSESSMENTID`),
  KEY `FKC945448A694216CC` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ASSESSACCESSCONTROL_T`
--

LOCK TABLES `SAM_ASSESSACCESSCONTROL_T` WRITE;
/*!40000 ALTER TABLE `SAM_ASSESSACCESSCONTROL_T` DISABLE KEYS */;
INSERT INTO `SAM_ASSESSACCESSCONTROL_T` VALUES (1,NULL,'',1,1,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,1,2,1,'','','','','',NULL),(2,NULL,'',1,1,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,1,2,1,'','','','','',1),(3,1,'\0',1,3,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,1,1,1,'','','','','',NULL),(4,NULL,'',1,2,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,1,2,1,'','','','','',NULL),(5,1,'\0',1,1,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,1,2,1,'','','','','',NULL),(6,1,'\0',1,1,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,1,1,1,'','','','','',NULL),(7,1,'\0',1,1,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,1,1,1,'','','','','',NULL);
/*!40000 ALTER TABLE `SAM_ASSESSACCESSCONTROL_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ASSESSEVALUATION_T`
--

DROP TABLE IF EXISTS `SAM_ASSESSEVALUATION_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ASSESSEVALUATION_T` (
  `ASSESSMENTID` bigint(20) NOT NULL,
  `EVALUATIONCOMPONENTS` varchar(255) DEFAULT NULL,
  `SCORINGTYPE` int(11) DEFAULT NULL,
  `NUMERICMODELID` varchar(255) DEFAULT NULL,
  `FIXEDTOTALSCORE` int(11) DEFAULT NULL,
  `GRADEAVAILABLE` int(11) DEFAULT NULL,
  `ISSTUDENTIDPUBLIC` int(11) DEFAULT NULL,
  `ANONYMOUSGRADING` int(11) DEFAULT NULL,
  `AUTOSCORING` int(11) DEFAULT NULL,
  `TOGRADEBOOK` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ASSESSMENTID`),
  KEY `FK6A6F29F5694216CC` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ASSESSEVALUATION_T`
--

LOCK TABLES `SAM_ASSESSEVALUATION_T` WRITE;
/*!40000 ALTER TABLE `SAM_ASSESSEVALUATION_T` DISABLE KEYS */;
INSERT INTO `SAM_ASSESSEVALUATION_T` VALUES (1,'',1,'',NULL,NULL,NULL,1,NULL,'2'),(2,'',1,'',NULL,NULL,NULL,1,NULL,'2'),(3,'',1,'',NULL,NULL,NULL,2,NULL,'2'),(4,'',1,'',NULL,NULL,NULL,2,NULL,'2'),(5,'',1,'',NULL,NULL,NULL,1,NULL,'2'),(6,'',1,'',NULL,NULL,NULL,1,NULL,'2'),(7,'',1,'',NULL,NULL,NULL,1,NULL,'2');
/*!40000 ALTER TABLE `SAM_ASSESSEVALUATION_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ASSESSFEEDBACK_T`
--

DROP TABLE IF EXISTS `SAM_ASSESSFEEDBACK_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ASSESSFEEDBACK_T` (
  `ASSESSMENTID` bigint(20) NOT NULL,
  `FEEDBACKDELIVERY` int(11) DEFAULT NULL,
  `FEEDBACKCOMPONENTOPTION` int(11) DEFAULT NULL,
  `FEEDBACKAUTHORING` int(11) DEFAULT NULL,
  `EDITCOMPONENTS` int(11) DEFAULT NULL,
  `SHOWQUESTIONTEXT` bit(1) DEFAULT NULL,
  `SHOWSTUDENTRESPONSE` bit(1) DEFAULT NULL,
  `SHOWCORRECTRESPONSE` bit(1) DEFAULT NULL,
  `SHOWSTUDENTSCORE` bit(1) DEFAULT NULL,
  `SHOWSTUDENTQUESTIONSCORE` bit(1) DEFAULT NULL,
  `SHOWQUESTIONLEVELFEEDBACK` bit(1) DEFAULT NULL,
  `SHOWSELECTIONLEVELFEEDBACK` bit(1) DEFAULT NULL,
  `SHOWGRADERCOMMENTS` bit(1) DEFAULT NULL,
  `SHOWSTATISTICS` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ASSESSMENTID`),
  KEY `FK557D4CFE694216CC` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ASSESSFEEDBACK_T`
--

LOCK TABLES `SAM_ASSESSFEEDBACK_T` WRITE;
/*!40000 ALTER TABLE `SAM_ASSESSFEEDBACK_T` DISABLE KEYS */;
INSERT INTO `SAM_ASSESSFEEDBACK_T` VALUES (1,3,2,1,1,'','\0','\0','\0','\0','\0','\0','\0','\0'),(2,3,2,3,1,'','\0','\0','\0','\0','\0','\0','\0','\0'),(3,3,2,1,1,'','\0','\0','\0','\0','\0','\0','\0','\0'),(4,3,2,1,1,'','\0','\0','\0','\0','\0','\0','\0','\0'),(5,1,2,3,1,'\0','','\0','\0','\0','\0','\0','\0',''),(6,3,2,1,1,'','\0','\0','\0','\0','\0','\0','\0','\0'),(7,3,2,1,1,'','\0','\0','\0','\0','\0','\0','\0','\0');
/*!40000 ALTER TABLE `SAM_ASSESSFEEDBACK_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ASSESSMENTBASE_T`
--

DROP TABLE IF EXISTS `SAM_ASSESSMENTBASE_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ASSESSMENTBASE_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `isTemplate` varchar(255) NOT NULL,
  `PARENTID` bigint(20) DEFAULT NULL,
  `TITLE` varchar(255) DEFAULT NULL,
  `DESCRIPTION` text,
  `COMMENTS` text,
  `TYPEID` bigint(20) DEFAULT NULL,
  `INSTRUCTORNOTIFICATION` int(11) DEFAULT NULL,
  `TESTEENOTIFICATION` int(11) DEFAULT NULL,
  `MULTIPARTALLOWED` int(11) DEFAULT NULL,
  `STATUS` int(11) NOT NULL,
  `CREATEDBY` varchar(255) NOT NULL,
  `CREATEDDATE` datetime NOT NULL,
  `LASTMODIFIEDBY` varchar(255) NOT NULL,
  `LASTMODIFIEDDATE` datetime NOT NULL,
  `ASSESSMENTTEMPLATEID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ASSESSMENTBASE_T`
--

LOCK TABLES `SAM_ASSESSMENTBASE_T` WRITE;
/*!40000 ALTER TABLE `SAM_ASSESSMENTBASE_T` DISABLE KEYS */;
INSERT INTO `SAM_ASSESSMENTBASE_T` VALUES (1,'1',0,'Default Assessment Type','System Defined Assessment Type','comments',142,1,1,1,1,'admin','2005-01-01 12:00:00','admin','2005-01-01 12:00:00',NULL),(2,'1',0,'Formative Assessment','System Defined Assessment Type','',142,1,1,1,1,'admin','2006-06-01 12:00:00','admin','2006-06-01 12:00:00',NULL),(3,'1',0,'Quiz','System Defined Assessment Type','',142,1,1,1,1,'admin','2006-06-01 12:00:00','admin','2006-06-01 12:00:00',NULL),(4,'1',0,'Problem Set','System Defined Assessment Type','',142,1,1,1,1,'admin','2006-06-01 12:00:00','admin','2006-06-01 12:00:00',NULL),(5,'1',0,'Survey','System Defined Assessment Type','',142,1,1,1,1,'admin','2006-06-01 12:00:00','admin','2006-06-01 12:00:00',NULL),(6,'1',0,'Test','System Defined Assessment Type','',142,1,1,1,1,'admin','2006-06-01 12:00:00','admin','2006-06-01 12:00:00',NULL),(7,'1',0,'Timed Test','System Defined Assessment Type','',142,1,1,1,1,'admin','2006-06-01 12:00:00','admin','2006-06-01 12:00:00',NULL);
/*!40000 ALTER TABLE `SAM_ASSESSMENTBASE_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ASSESSMENTGRADING_T`
--

DROP TABLE IF EXISTS `SAM_ASSESSMENTGRADING_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ASSESSMENTGRADING_T` (
  `ASSESSMENTGRADINGID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PUBLISHEDASSESSMENTID` bigint(20) NOT NULL,
  `AGENTID` varchar(255) NOT NULL,
  `SUBMITTEDDATE` datetime DEFAULT NULL,
  `ISLATE` bit(1) NOT NULL,
  `FORGRADE` bit(1) NOT NULL,
  `TOTALAUTOSCORE` float DEFAULT NULL,
  `TOTALOVERRIDESCORE` float DEFAULT NULL,
  `FINALSCORE` float DEFAULT NULL,
  `COMMENTS` text,
  `GRADEDBY` varchar(255) DEFAULT NULL,
  `GRADEDDATE` datetime DEFAULT NULL,
  `STATUS` int(11) NOT NULL,
  `ATTEMPTDATE` datetime DEFAULT NULL,
  `TIMEELAPSED` int(11) DEFAULT NULL,
  `ISAUTOSUBMITTED` bit(1) DEFAULT NULL,
  `LASTVISITEDPART` int(11) DEFAULT NULL,
  `LASTVISITEDQUESTION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ASSESSMENTGRADINGID`),
  KEY `SAM_PUBLISHEDASSESSMENT_I` (`PUBLISHEDASSESSMENTID`),
  KEY `SAM_ASSGRAD_AID_PUBASSEID_T` (`AGENTID`,`PUBLISHEDASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ASSESSMENTGRADING_T`
--

LOCK TABLES `SAM_ASSESSMENTGRADING_T` WRITE;
/*!40000 ALTER TABLE `SAM_ASSESSMENTGRADING_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_ASSESSMENTGRADING_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ASSESSMETADATA_T`
--

DROP TABLE IF EXISTS `SAM_ASSESSMETADATA_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ASSESSMETADATA_T` (
  `ASSESSMENTMETADATAID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ASSESSMENTID` bigint(20) NOT NULL,
  `LABEL` varchar(255) NOT NULL,
  `ENTRY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ASSESSMENTMETADATAID`),
  KEY `FK7E6F9A28694216CC` (`ASSESSMENTID`)
) ENGINE=MyISAM AUTO_INCREMENT=224 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ASSESSMETADATA_T`
--

LOCK TABLES `SAM_ASSESSMETADATA_T` WRITE;
/*!40000 ALTER TABLE `SAM_ASSESSMETADATA_T` DISABLE KEYS */;
INSERT INTO `SAM_ASSESSMETADATA_T` VALUES (1,1,'finalPageURL_isInstructorEditable','true'),(2,1,'anonymousRelease_isInstructorEditable','true'),(3,1,'dueDate_isInstructorEditable','true'),(4,1,'description_isInstructorEditable','true'),(5,1,'metadataQuestions_isInstructorEditable','true'),(6,1,'bgImage_isInstructorEditable','true'),(7,1,'feedbackComponents_isInstructorEditable','true'),(8,1,'retractDate_isInstructorEditable','true'),(9,1,'feedbackType_isInstructorEditable','true'),(10,1,'timedAssessmentAutoSubmit_isInstructorEditable','true'),(11,1,'toGradebook_isInstructorEditable','true'),(12,1,'displayChunking_isInstructorEditable','true'),(13,1,'recordedScore_isInstructorEditable','true'),(14,1,'authenticatedRelease_isInstructorEditable','true'),(15,1,'displayNumbering_isInstructorEditable','true'),(16,1,'submissionMessage_isInstructorEditable','true'),(17,1,'releaseDate_isInstructorEditable','true'),(18,1,'assessmentAuthor_isInstructorEditable','true'),(19,1,'passwordRequired_isInstructorEditable','true'),(20,1,'author',''),(21,1,'submissionModel_isInstructorEditable','true'),(22,1,'ipAccessType_isInstructorEditable','true'),(23,1,'timedAssessment_isInstructorEditable','true'),(24,1,'metadataAssess_isInstructorEditable','true'),(25,1,'bgColor_isInstructorEditable','true'),(26,1,'testeeIdentity_isInstructorEditable','true'),(27,1,'templateInfo_isInstructorEditable','true'),(28,1,'itemAccessType_isInstructorEditable','true'),(29,1,'lateHandling_isInstructorEditable','true'),(30,1,'feedbackAuthoring_isInstructorEditable','true'),(31,1,'releaseTo','SITE_MEMBERS'),(32,2,'finalPageURL_isInstructorEditable','true'),(33,2,'anonymousRelease_isInstructorEditable','true'),(34,2,'dueDate_isInstructorEditable','true'),(35,2,'description_isInstructorEditable','true'),(36,2,'metadataQuestions_isInstructorEditable','true'),(37,2,'bgImage_isInstructorEditable','true'),(38,2,'feedbackComponents_isInstructorEditable','true'),(39,2,'retractDate_isInstructorEditable','true'),(40,2,'feedbackType_isInstructorEditable','true'),(41,2,'timedAssessmentAutoSubmit_isInstructorEditable','true'),(42,2,'toGradebook_isInstructorEditable','false'),(43,2,'displayChunking_isInstructorEditable','true'),(44,2,'recordedScore_isInstructorEditable','false'),(45,2,'authenticatedRelease_isInstructorEditable','true'),(46,2,'displayNumbering_isInstructorEditable','true'),(47,2,'submissionMessage_isInstructorEditable','true'),(48,2,'releaseDate_isInstructorEditable','true'),(49,2,'assessmentAuthor_isInstructorEditable','true'),(50,2,'passwordRequired_isInstructorEditable','false'),(51,2,'author',''),(52,2,'submissionModel_isInstructorEditable','true'),(53,2,'ipAccessType_isInstructorEditable','false'),(54,2,'timedAssessment_isInstructorEditable','true'),(55,2,'metadataAssess_isInstructorEditable','true'),(56,2,'bgColor_isInstructorEditable','true'),(57,2,'testeeIdentity_isInstructorEditable','false'),(58,2,'templateInfo_isInstructorEditable','true'),(59,2,'itemAccessType_isInstructorEditable','true'),(60,2,'lateHandling_isInstructorEditable','false'),(61,2,'feedbackAuthoring_isInstructorEditable','true'),(62,2,'releaseTo','SITE_MEMBERS'),(63,3,'finalPageURL_isInstructorEditable','true'),(64,3,'anonymousRelease_isInstructorEditable','false'),(65,3,'dueDate_isInstructorEditable','true'),(66,3,'description_isInstructorEditable','true'),(67,3,'metadataQuestions_isInstructorEditable','true'),(68,3,'bgImage_isInstructorEditable','true'),(69,3,'feedbackComponents_isInstructorEditable','true'),(70,3,'retractDate_isInstructorEditable','true'),(71,3,'feedbackType_isInstructorEditable','true'),(72,3,'timedAssessmentAutoSubmit_isInstructorEditable','false'),(73,3,'toGradebook_isInstructorEditable','true'),(74,3,'displayChunking_isInstructorEditable','true'),(75,3,'recordedScore_isInstructorEditable','false'),(76,3,'authenticatedRelease_isInstructorEditable','true'),(77,3,'displayNumbering_isInstructorEditable','true'),(78,3,'submissionMessage_isInstructorEditable','true'),(79,3,'releaseDate_isInstructorEditable','true'),(80,3,'assessmentAuthor_isInstructorEditable','true'),(81,3,'passwordRequired_isInstructorEditable','false'),(82,3,'author',''),(83,3,'submissionModel_isInstructorEditable','true'),(84,3,'ipAccessType_isInstructorEditable','false'),(85,3,'timedAssessment_isInstructorEditable','false'),(86,3,'metadataAssess_isInstructorEditable','true'),(87,3,'bgColor_isInstructorEditable','true'),(88,3,'testeeIdentity_isInstructorEditable','true'),(89,3,'templateInfo_isInstructorEditable','true'),(90,3,'itemAccessType_isInstructorEditable','true'),(91,3,'lateHandling_isInstructorEditable','true'),(92,3,'feedbackAuthoring_isInstructorEditable','true'),(93,3,'releaseTo','SITE_MEMBERS'),(94,4,'finalPageURL_isInstructorEditable','true'),(95,4,'anonymousRelease_isInstructorEditable','false'),(96,4,'dueDate_isInstructorEditable','true'),(97,4,'description_isInstructorEditable','true'),(98,4,'metadataQuestions_isInstructorEditable','true'),(99,4,'bgImage_isInstructorEditable','true'),(100,4,'feedbackComponents_isInstructorEditable','true'),(101,4,'retractDate_isInstructorEditable','true'),(102,4,'feedbackType_isInstructorEditable','true'),(103,4,'timedAssessmentAutoSubmit_isInstructorEditable','false'),(104,4,'toGradebook_isInstructorEditable','true'),(105,4,'displayChunking_isInstructorEditable','true'),(106,4,'recordedScore_isInstructorEditable','true'),(107,4,'authenticatedRelease_isInstructorEditable','true'),(108,4,'displayNumbering_isInstructorEditable','true'),(109,4,'submissionMessage_isInstructorEditable','true'),(110,4,'releaseDate_isInstructorEditable','true'),(111,4,'assessmentAuthor_isInstructorEditable','true'),(112,4,'passwordRequired_isInstructorEditable','false'),(113,4,'author',''),(114,4,'submissionModel_isInstructorEditable','true'),(115,4,'ipAccessType_isInstructorEditable','false'),(116,4,'timedAssessment_isInstructorEditable','false'),(117,4,'metadataAssess_isInstructorEditable','true'),(118,4,'bgColor_isInstructorEditable','true'),(119,4,'testeeIdentity_isInstructorEditable','true'),(120,4,'templateInfo_isInstructorEditable','true'),(121,4,'itemAccessType_isInstructorEditable','true'),(122,4,'lateHandling_isInstructorEditable','true'),(123,4,'feedbackAuthoring_isInstructorEditable','true'),(124,4,'releaseTo','SITE_MEMBERS'),(125,5,'finalPageURL_isInstructorEditable','true'),(126,5,'anonymousRelease_isInstructorEditable','true'),(127,5,'dueDate_isInstructorEditable','true'),(128,5,'description_isInstructorEditable','true'),(129,5,'metadataQuestions_isInstructorEditable','true'),(130,5,'bgImage_isInstructorEditable','true'),(131,5,'feedbackComponents_isInstructorEditable','true'),(132,5,'retractDate_isInstructorEditable','true'),(133,5,'feedbackType_isInstructorEditable','true'),(134,5,'timedAssessmentAutoSubmit_isInstructorEditable','false'),(135,5,'toGradebook_isInstructorEditable','true'),(136,5,'displayChunking_isInstructorEditable','true'),(137,5,'recordedScore_isInstructorEditable','false'),(138,5,'authenticatedRelease_isInstructorEditable','false'),(139,5,'displayNumbering_isInstructorEditable','true'),(140,5,'submissionMessage_isInstructorEditable','true'),(141,5,'releaseDate_isInstructorEditable','true'),(142,5,'assessmentAuthor_isInstructorEditable','true'),(143,5,'passwordRequired_isInstructorEditable','false'),(144,5,'author',''),(145,5,'submissionModel_isInstructorEditable','true'),(146,5,'ipAccessType_isInstructorEditable','false'),(147,5,'timedAssessment_isInstructorEditable','false'),(148,5,'metadataAssess_isInstructorEditable','true'),(149,5,'bgColor_isInstructorEditable','true'),(150,5,'testeeIdentity_isInstructorEditable','true'),(151,5,'templateInfo_isInstructorEditable','true'),(152,5,'itemAccessType_isInstructorEditable','true'),(153,5,'lateHandling_isInstructorEditable','false'),(154,5,'feedbackAuthoring_isInstructorEditable','false'),(155,5,'releaseTo','SITE_MEMBERS'),(156,6,'finalPageURL_isInstructorEditable','true'),(157,6,'anonymousRelease_isInstructorEditable','false'),(158,6,'dueDate_isInstructorEditable','true'),(159,6,'description_isInstructorEditable','true'),(160,6,'metadataQuestions_isInstructorEditable','true'),(161,6,'bgImage_isInstructorEditable','true'),(162,6,'feedbackComponents_isInstructorEditable','true'),(163,6,'retractDate_isInstructorEditable','true'),(164,6,'feedbackType_isInstructorEditable','true'),(165,6,'timedAssessmentAutoSubmit_isInstructorEditable','false'),(166,6,'toGradebook_isInstructorEditable','true'),(167,6,'displayChunking_isInstructorEditable','true'),(168,6,'recordedScore_isInstructorEditable','false'),(169,6,'authenticatedRelease_isInstructorEditable','false'),(170,6,'displayNumbering_isInstructorEditable','true'),(171,6,'submissionMessage_isInstructorEditable','true'),(172,6,'releaseDate_isInstructorEditable','true'),(173,6,'assessmentAuthor_isInstructorEditable','true'),(174,6,'passwordRequired_isInstructorEditable','true'),(175,6,'author',''),(176,6,'submissionModel_isInstructorEditable','true'),(177,6,'ipAccessType_isInstructorEditable','false'),(178,6,'timedAssessment_isInstructorEditable','false'),(179,6,'metadataAssess_isInstructorEditable','true'),(180,6,'bgColor_isInstructorEditable','true'),(181,6,'testeeIdentity_isInstructorEditable','true'),(182,6,'templateInfo_isInstructorEditable','true'),(183,6,'itemAccessType_isInstructorEditable','true'),(184,6,'lateHandling_isInstructorEditable','true'),(185,6,'feedbackAuthoring_isInstructorEditable','true'),(186,6,'releaseTo','SITE_MEMBERS'),(187,7,'finalPageURL_isInstructorEditable','true'),(188,7,'anonymousRelease_isInstructorEditable','false'),(189,7,'dueDate_isInstructorEditable','true'),(190,7,'description_isInstructorEditable','true'),(191,7,'metadataQuestions_isInstructorEditable','true'),(192,7,'bgImage_isInstructorEditable','true'),(193,7,'feedbackComponents_isInstructorEditable','true'),(194,7,'retractDate_isInstructorEditable','true'),(195,7,'feedbackType_isInstructorEditable','true'),(196,7,'timedAssessmentAutoSubmit_isInstructorEditable','true'),(197,7,'toGradebook_isInstructorEditable','true'),(198,7,'displayChunking_isInstructorEditable','true'),(199,7,'recordedScore_isInstructorEditable','false'),(200,7,'authenticatedRelease_isInstructorEditable','false'),(201,7,'displayNumbering_isInstructorEditable','true'),(202,7,'submissionMessage_isInstructorEditable','true'),(203,7,'releaseDate_isInstructorEditable','true'),(204,7,'assessmentAuthor_isInstructorEditable','true'),(205,7,'passwordRequired_isInstructorEditable','true'),(206,7,'author',''),(207,7,'submissionModel_isInstructorEditable','true'),(208,7,'ipAccessType_isInstructorEditable','false'),(209,7,'timedAssessment_isInstructorEditable','true'),(210,7,'metadataAssess_isInstructorEditable','true'),(211,7,'bgColor_isInstructorEditable','true'),(212,7,'testeeIdentity_isInstructorEditable','true'),(213,7,'templateInfo_isInstructorEditable','true'),(214,7,'itemAccessType_isInstructorEditable','true'),(215,7,'lateHandling_isInstructorEditable','true'),(216,7,'feedbackAuthoring_isInstructorEditable','true'),(217,7,'releaseTo','SITE_MEMBERS'),(218,1,'markForReview_isInstructorEditable','true'),(219,2,'markForReview_isInstructorEditable','true'),(220,3,'markForReview_isInstructorEditable','true'),(221,4,'markForReview_isInstructorEditable','true'),(222,6,'markForReview_isInstructorEditable','true'),(223,7,'markForReview_isInstructorEditable','true');
/*!40000 ALTER TABLE `SAM_ASSESSMETADATA_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ATTACHMENT_T`
--

DROP TABLE IF EXISTS `SAM_ATTACHMENT_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ATTACHMENT_T` (
  `ATTACHMENTID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATTACHMENTTYPE` varchar(255) NOT NULL,
  `RESOURCEID` varchar(255) DEFAULT NULL,
  `FILENAME` varchar(255) DEFAULT NULL,
  `MIMETYPE` varchar(80) DEFAULT NULL,
  `FILESIZE` bigint(20) DEFAULT NULL,
  `DESCRIPTION` text,
  `LOCATION` text,
  `ISLINK` bit(1) DEFAULT NULL,
  `STATUS` int(11) NOT NULL,
  `CREATEDBY` varchar(255) NOT NULL,
  `CREATEDDATE` datetime NOT NULL,
  `LASTMODIFIEDBY` varchar(255) NOT NULL,
  `LASTMODIFIEDDATE` datetime NOT NULL,
  `ASSESSMENTID` bigint(20) DEFAULT NULL,
  `SECTIONID` bigint(20) DEFAULT NULL,
  `ITEMID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ATTACHMENTID`),
  KEY `FK99FA8CB83288DBBD` (`ITEMID`),
  KEY `FK99FA8CB870CE2BD` (`SECTIONID`),
  KEY `FK99FA8CB8CAC2365B` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ATTACHMENT_T`
--

LOCK TABLES `SAM_ATTACHMENT_T` WRITE;
/*!40000 ALTER TABLE `SAM_ATTACHMENT_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_ATTACHMENT_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_AUTHZDATA_T`
--

DROP TABLE IF EXISTS `SAM_AUTHZDATA_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_AUTHZDATA_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `lockId` int(11) NOT NULL,
  `AGENTID` varchar(255) NOT NULL,
  `FUNCTIONID` varchar(36) NOT NULL,
  `QUALIFIERID` varchar(36) NOT NULL,
  `EFFECTIVEDATE` date DEFAULT NULL,
  `EXPIRATIONDATE` date DEFAULT NULL,
  `LASTMODIFIEDBY` varchar(255) NOT NULL,
  `LASTMODIFIEDDATE` date NOT NULL,
  `ISEXPLICIT` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `AGENTID` (`AGENTID`,`FUNCTIONID`,`QUALIFIERID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_AUTHZDATA_T`
--

LOCK TABLES `SAM_AUTHZDATA_T` WRITE;
/*!40000 ALTER TABLE `SAM_AUTHZDATA_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_AUTHZDATA_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_FUNCTIONDATA_T`
--

DROP TABLE IF EXISTS `SAM_FUNCTIONDATA_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_FUNCTIONDATA_T` (
  `FUNCTIONID` bigint(20) NOT NULL AUTO_INCREMENT,
  `REFERENCENAME` varchar(255) NOT NULL,
  `DISPLAYNAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` text,
  `FUNCTIONTYPEID` text,
  PRIMARY KEY (`FUNCTIONID`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_FUNCTIONDATA_T`
--

LOCK TABLES `SAM_FUNCTIONDATA_T` WRITE;
/*!40000 ALTER TABLE `SAM_FUNCTIONDATA_T` DISABLE KEYS */;
INSERT INTO `SAM_FUNCTIONDATA_T` VALUES (1,'take.published.assessment','Take Assessment','Take Assessment','81'),(2,'view.published.assessment','View Assessment','View Assessment','81'),(3,'submit.assessment','Submit Assessment','Submit Assessment','81'),(4,'submit.assessment.forgrade','Submit Assessment For Grade','Submit Assessment For Grade','81');
/*!40000 ALTER TABLE `SAM_FUNCTIONDATA_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_GRADINGATTACHMENT_T`
--

DROP TABLE IF EXISTS `SAM_GRADINGATTACHMENT_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_GRADINGATTACHMENT_T` (
  `ATTACHMENTID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATTACHMENTTYPE` varchar(255) NOT NULL,
  `RESOURCEID` varchar(255) DEFAULT NULL,
  `FILENAME` varchar(255) DEFAULT NULL,
  `MIMETYPE` varchar(80) DEFAULT NULL,
  `FILESIZE` bigint(20) DEFAULT NULL,
  `DESCRIPTION` text,
  `LOCATION` text,
  `ISLINK` bit(1) DEFAULT NULL,
  `STATUS` int(11) NOT NULL,
  `CREATEDBY` varchar(255) NOT NULL,
  `CREATEDDATE` datetime NOT NULL,
  `LASTMODIFIEDBY` varchar(255) NOT NULL,
  `LASTMODIFIEDDATE` datetime NOT NULL,
  `ITEMGRADINGID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ATTACHMENTID`),
  KEY `FK28156C6C4D7EA7B3` (`ITEMGRADINGID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_GRADINGATTACHMENT_T`
--

LOCK TABLES `SAM_GRADINGATTACHMENT_T` WRITE;
/*!40000 ALTER TABLE `SAM_GRADINGATTACHMENT_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_GRADINGATTACHMENT_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_GRADINGSUMMARY_T`
--

DROP TABLE IF EXISTS `SAM_GRADINGSUMMARY_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_GRADINGSUMMARY_T` (
  `ASSESSMENTGRADINGSUMMARYID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PUBLISHEDASSESSMENTID` bigint(20) NOT NULL,
  `AGENTID` varchar(255) NOT NULL,
  `TOTALSUBMITTED` int(11) DEFAULT NULL,
  `TOTALSUBMITTEDFORGRADE` int(11) DEFAULT NULL,
  `LASTSUBMITTEDDATE` datetime DEFAULT NULL,
  `LASTSUBMITTEDASSESSMENTISLATE` bit(1) NOT NULL,
  `SUMOF_AUTOSCOREFORGRADE` float DEFAULT NULL,
  `AVERAGE_AUTOSCOREFORGRADE` float DEFAULT NULL,
  `HIGHEST_AUTOSCOREFORGRADE` float DEFAULT NULL,
  `LOWEST_AUTOSCOREFORGRADE` float DEFAULT NULL,
  `LAST_AUTOSCOREFORGRADE` float DEFAULT NULL,
  `SUMOF_OVERRIDESCOREFORGRADE` float DEFAULT NULL,
  `AVERAGE_OVERRIDESCOREFORGRADE` float DEFAULT NULL,
  `HIGHEST_OVERRIDESCOREFORGRADE` float DEFAULT NULL,
  `LOWEST_OVERRIDESCOREFORGRADE` float DEFAULT NULL,
  `LAST_OVERRIDESCOREFORGRADE` float DEFAULT NULL,
  `SCORINGTYPE` int(11) DEFAULT NULL,
  `ACCEPTEDASSESSMENTISLATE` bit(1) DEFAULT NULL,
  `FINALASSESSMENTSCORE` float DEFAULT NULL,
  `FEEDTOGRADEBOOK` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ASSESSMENTGRADINGSUMMARYID`),
  KEY `FKBC88AA27D02EF633` (`PUBLISHEDASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_GRADINGSUMMARY_T`
--

LOCK TABLES `SAM_GRADINGSUMMARY_T` WRITE;
/*!40000 ALTER TABLE `SAM_GRADINGSUMMARY_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_GRADINGSUMMARY_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ITEMFEEDBACK_T`
--

DROP TABLE IF EXISTS `SAM_ITEMFEEDBACK_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ITEMFEEDBACK_T` (
  `ITEMFEEDBACKID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMID` bigint(20) NOT NULL,
  `TYPEID` varchar(255) NOT NULL,
  `TEXT` text,
  PRIMARY KEY (`ITEMFEEDBACKID`),
  KEY `FK3254E9ED3288DBBD` (`ITEMID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ITEMFEEDBACK_T`
--

LOCK TABLES `SAM_ITEMFEEDBACK_T` WRITE;
/*!40000 ALTER TABLE `SAM_ITEMFEEDBACK_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_ITEMFEEDBACK_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ITEMGRADING_T`
--

DROP TABLE IF EXISTS `SAM_ITEMGRADING_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ITEMGRADING_T` (
  `ITEMGRADINGID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ASSESSMENTGRADINGID` bigint(20) NOT NULL,
  `PUBLISHEDITEMID` bigint(20) NOT NULL,
  `PUBLISHEDITEMTEXTID` bigint(20) NOT NULL,
  `AGENTID` varchar(255) NOT NULL,
  `SUBMITTEDDATE` datetime DEFAULT NULL,
  `PUBLISHEDANSWERID` bigint(20) DEFAULT NULL,
  `RATIONALE` text,
  `ANSWERTEXT` text,
  `AUTOSCORE` float DEFAULT NULL,
  `OVERRIDESCORE` float DEFAULT NULL,
  `COMMENTS` text,
  `GRADEDBY` varchar(255) DEFAULT NULL,
  `GRADEDDATE` datetime DEFAULT NULL,
  `REVIEW` bit(1) DEFAULT NULL,
  `ATTEMPTSREMAINING` int(11) DEFAULT NULL,
  `LASTDURATION` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ITEMGRADINGID`),
  UNIQUE KEY `ASSESSMENTGRADINGID` (`ASSESSMENTGRADINGID`,`PUBLISHEDITEMID`,`PUBLISHEDITEMTEXTID`,`AGENTID`,`PUBLISHEDANSWERID`),
  KEY `FKB68E675667B430D5` (`ASSESSMENTGRADINGID`),
  KEY `SAM_ITEMGRADING_ITEM_I` (`PUBLISHEDITEMID`),
  KEY `SAM_ITEMGRADING_ITEMTEXT_I` (`PUBLISHEDITEMTEXTID`),
  KEY `SAM_ITEMGRADING_PUBANS_I` (`PUBLISHEDANSWERID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ITEMGRADING_T`
--

LOCK TABLES `SAM_ITEMGRADING_T` WRITE;
/*!40000 ALTER TABLE `SAM_ITEMGRADING_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_ITEMGRADING_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ITEMMETADATA_T`
--

DROP TABLE IF EXISTS `SAM_ITEMMETADATA_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ITEMMETADATA_T` (
  `ITEMMETADATAID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMID` bigint(20) NOT NULL,
  `LABEL` varchar(255) NOT NULL,
  `ENTRY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ITEMMETADATAID`),
  KEY `FK5B4737173288DBBD` (`ITEMID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ITEMMETADATA_T`
--

LOCK TABLES `SAM_ITEMMETADATA_T` WRITE;
/*!40000 ALTER TABLE `SAM_ITEMMETADATA_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_ITEMMETADATA_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ITEMTEXT_T`
--

DROP TABLE IF EXISTS `SAM_ITEMTEXT_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ITEMTEXT_T` (
  `ITEMTEXTID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMID` bigint(20) NOT NULL,
  `SEQUENCE` bigint(20) NOT NULL,
  `TEXT` text,
  PRIMARY KEY (`ITEMTEXTID`),
  KEY `FK271D63153288DBBD` (`ITEMID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ITEMTEXT_T`
--

LOCK TABLES `SAM_ITEMTEXT_T` WRITE;
/*!40000 ALTER TABLE `SAM_ITEMTEXT_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_ITEMTEXT_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_ITEM_T`
--

DROP TABLE IF EXISTS `SAM_ITEM_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_ITEM_T` (
  `ITEMID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SECTIONID` bigint(20) DEFAULT NULL,
  `ITEMIDSTRING` varchar(255) DEFAULT NULL,
  `SEQUENCE` int(11) DEFAULT NULL,
  `DURATION` int(11) DEFAULT NULL,
  `TRIESALLOWED` int(11) DEFAULT NULL,
  `INSTRUCTION` text,
  `DESCRIPTION` text,
  `TYPEID` bigint(20) NOT NULL,
  `GRADE` varchar(80) DEFAULT NULL,
  `SCORE` float DEFAULT NULL,
  `PARTIAL_CREDIT_FLAG` bit(1) DEFAULT NULL,
  `DISCOUNT` float DEFAULT NULL,
  `HINT` text,
  `HASRATIONALE` bit(1) DEFAULT NULL,
  `STATUS` int(11) NOT NULL,
  `CREATEDBY` varchar(255) NOT NULL,
  `CREATEDDATE` datetime NOT NULL,
  `LASTMODIFIEDBY` varchar(255) NOT NULL,
  `LASTMODIFIEDDATE` datetime NOT NULL,
  PRIMARY KEY (`ITEMID`),
  KEY `FK3AAC5EA870CE2BD` (`SECTIONID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_ITEM_T`
--

LOCK TABLES `SAM_ITEM_T` WRITE;
/*!40000 ALTER TABLE `SAM_ITEM_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_ITEM_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_MEDIA_T`
--

DROP TABLE IF EXISTS `SAM_MEDIA_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_MEDIA_T` (
  `MEDIAID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMGRADINGID` bigint(20) DEFAULT NULL,
  `MEDIA` longblob,
  `FILESIZE` bigint(20) DEFAULT NULL,
  `MIMETYPE` varchar(80) DEFAULT NULL,
  `DESCRIPTION` text,
  `LOCATION` varchar(255) DEFAULT NULL,
  `FILENAME` varchar(255) DEFAULT NULL,
  `ISLINK` bit(1) DEFAULT NULL,
  `ISHTMLINLINE` bit(1) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `CREATEDBY` varchar(255) DEFAULT NULL,
  `CREATEDDATE` datetime DEFAULT NULL,
  `LASTMODIFIEDBY` varchar(255) DEFAULT NULL,
  `LASTMODIFIEDDATE` datetime DEFAULT NULL,
  `DURATION` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`MEDIAID`),
  KEY `FKD4CF5A194D7EA7B3` (`ITEMGRADINGID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_MEDIA_T`
--

LOCK TABLES `SAM_MEDIA_T` WRITE;
/*!40000 ALTER TABLE `SAM_MEDIA_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_MEDIA_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDACCESSCONTROL_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDACCESSCONTROL_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDACCESSCONTROL_T` (
  `ASSESSMENTID` bigint(20) NOT NULL,
  `UNLIMITEDSUBMISSIONS` bit(1) DEFAULT NULL,
  `SUBMISSIONSALLOWED` int(11) DEFAULT NULL,
  `SUBMISSIONSSAVED` int(11) DEFAULT NULL,
  `ASSESSMENTFORMAT` int(11) DEFAULT NULL,
  `BOOKMARKINGITEM` int(11) DEFAULT NULL,
  `TIMELIMIT` int(11) DEFAULT NULL,
  `TIMEDASSESSMENT` int(11) DEFAULT NULL,
  `RETRYALLOWED` int(11) DEFAULT NULL,
  `LATEHANDLING` int(11) DEFAULT NULL,
  `STARTDATE` datetime DEFAULT NULL,
  `DUEDATE` datetime DEFAULT NULL,
  `SCOREDATE` datetime DEFAULT NULL,
  `FEEDBACKDATE` datetime DEFAULT NULL,
  `RETRACTDATE` datetime DEFAULT NULL,
  `AUTOSUBMIT` int(11) DEFAULT NULL,
  `ITEMNAVIGATION` int(11) DEFAULT NULL,
  `ITEMNUMBERING` int(11) DEFAULT NULL,
  `SUBMISSIONMESSAGE` text,
  `RELEASETO` varchar(255) DEFAULT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `FINALPAGEURL` text,
  `MARKFORREVIEW` int(11) DEFAULT NULL,
  PRIMARY KEY (`ASSESSMENTID`),
  KEY `FK2EDF39E09482C945` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDACCESSCONTROL_T`
--

LOCK TABLES `SAM_PUBLISHEDACCESSCONTROL_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDACCESSCONTROL_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDACCESSCONTROL_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDANSWERFEEDBACK_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDANSWERFEEDBACK_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDANSWERFEEDBACK_T` (
  `ANSWERFEEDBACKID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ANSWERID` bigint(20) NOT NULL,
  `TYPEID` varchar(255) DEFAULT NULL,
  `TEXT` text,
  PRIMARY KEY (`ANSWERFEEDBACKID`),
  KEY `FK6CB765A624D77573` (`ANSWERID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDANSWERFEEDBACK_T`
--

LOCK TABLES `SAM_PUBLISHEDANSWERFEEDBACK_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDANSWERFEEDBACK_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDANSWERFEEDBACK_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDANSWER_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDANSWER_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDANSWER_T` (
  `ANSWERID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMTEXTID` bigint(20) NOT NULL,
  `ITEMID` bigint(20) NOT NULL,
  `TEXT` text,
  `SEQUENCE` bigint(20) NOT NULL,
  `LABEL` varchar(20) DEFAULT NULL,
  `ISCORRECT` bit(1) DEFAULT NULL,
  `GRADE` varchar(80) DEFAULT NULL,
  `SCORE` float DEFAULT NULL,
  `PARTIAL_CREDIT` float DEFAULT NULL,
  `DISCOUNT` float DEFAULT NULL,
  PRIMARY KEY (`ANSWERID`),
  KEY `FKB41EA36131446627` (`ITEMID`),
  KEY `FKB41EA36126460817` (`ITEMTEXTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDANSWER_T`
--

LOCK TABLES `SAM_PUBLISHEDANSWER_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDANSWER_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDANSWER_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDASSESSMENT_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDASSESSMENT_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDASSESSMENT_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(255) DEFAULT NULL,
  `ASSESSMENTID` bigint(20) DEFAULT NULL,
  `DESCRIPTION` text,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TYPEID` bigint(20) DEFAULT NULL,
  `INSTRUCTORNOTIFICATION` int(11) DEFAULT NULL,
  `TESTEENOTIFICATION` int(11) DEFAULT NULL,
  `MULTIPARTALLOWED` int(11) DEFAULT NULL,
  `STATUS` int(11) NOT NULL,
  `CREATEDBY` varchar(255) NOT NULL,
  `CREATEDDATE` datetime NOT NULL,
  `LASTMODIFIEDBY` varchar(255) NOT NULL,
  `LASTMODIFIEDDATE` datetime NOT NULL,
  `LASTNEEDRESUBMITDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `SAM_PUBA_ASSESSMENT_I` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDASSESSMENT_T`
--

LOCK TABLES `SAM_PUBLISHEDASSESSMENT_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDASSESSMENT_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDASSESSMENT_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDATTACHMENT_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDATTACHMENT_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDATTACHMENT_T` (
  `ATTACHMENTID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATTACHMENTTYPE` varchar(255) NOT NULL,
  `RESOURCEID` varchar(255) DEFAULT NULL,
  `FILENAME` varchar(255) DEFAULT NULL,
  `MIMETYPE` varchar(80) DEFAULT NULL,
  `FILESIZE` bigint(20) DEFAULT NULL,
  `DESCRIPTION` text,
  `LOCATION` text,
  `ISLINK` bit(1) DEFAULT NULL,
  `STATUS` int(11) NOT NULL,
  `CREATEDBY` varchar(255) NOT NULL,
  `CREATEDDATE` datetime NOT NULL,
  `LASTMODIFIEDBY` varchar(255) NOT NULL,
  `LASTMODIFIEDDATE` datetime NOT NULL,
  `ASSESSMENTID` bigint(20) DEFAULT NULL,
  `SECTIONID` bigint(20) DEFAULT NULL,
  `ITEMID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ATTACHMENTID`),
  KEY `FK2709988631446627` (`ITEMID`),
  KEY `FK27099886895D4813` (`SECTIONID`),
  KEY `FK270998869482C945` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDATTACHMENT_T`
--

LOCK TABLES `SAM_PUBLISHEDATTACHMENT_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDATTACHMENT_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDATTACHMENT_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDEVALUATION_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDEVALUATION_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDEVALUATION_T` (
  `ASSESSMENTID` bigint(20) NOT NULL,
  `EVALUATIONCOMPONENTS` varchar(255) DEFAULT NULL,
  `SCORINGTYPE` int(11) DEFAULT NULL,
  `NUMERICMODELID` varchar(255) DEFAULT NULL,
  `FIXEDTOTALSCORE` int(11) DEFAULT NULL,
  `GRADEAVAILABLE` int(11) DEFAULT NULL,
  `ISSTUDENTIDPUBLIC` int(11) DEFAULT NULL,
  `ANONYMOUSGRADING` int(11) DEFAULT NULL,
  `AUTOSCORING` int(11) DEFAULT NULL,
  `TOGRADEBOOK` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ASSESSMENTID`),
  KEY `FK94CB245F9482C945` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDEVALUATION_T`
--

LOCK TABLES `SAM_PUBLISHEDEVALUATION_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDEVALUATION_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDEVALUATION_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDFEEDBACK_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDFEEDBACK_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDFEEDBACK_T` (
  `ASSESSMENTID` bigint(20) NOT NULL,
  `FEEDBACKDELIVERY` int(11) DEFAULT NULL,
  `FEEDBACKCOMPONENTOPTION` int(11) DEFAULT NULL,
  `FEEDBACKAUTHORING` int(11) DEFAULT NULL,
  `EDITCOMPONENTS` int(11) DEFAULT NULL,
  `SHOWQUESTIONTEXT` bit(1) DEFAULT NULL,
  `SHOWSTUDENTRESPONSE` bit(1) DEFAULT NULL,
  `SHOWCORRECTRESPONSE` bit(1) DEFAULT NULL,
  `SHOWSTUDENTSCORE` bit(1) DEFAULT NULL,
  `SHOWSTUDENTQUESTIONSCORE` bit(1) DEFAULT NULL,
  `SHOWQUESTIONLEVELFEEDBACK` bit(1) DEFAULT NULL,
  `SHOWSELECTIONLEVELFEEDBACK` bit(1) DEFAULT NULL,
  `SHOWGRADERCOMMENTS` bit(1) DEFAULT NULL,
  `SHOWSTATISTICS` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ASSESSMENTID`),
  KEY `FK1488D9E89482C945` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDFEEDBACK_T`
--

LOCK TABLES `SAM_PUBLISHEDFEEDBACK_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDFEEDBACK_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDFEEDBACK_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDITEMFEEDBACK_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDITEMFEEDBACK_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDITEMFEEDBACK_T` (
  `ITEMFEEDBACKID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMID` bigint(20) NOT NULL,
  `TYPEID` varchar(255) NOT NULL,
  `TEXT` text,
  PRIMARY KEY (`ITEMFEEDBACKID`),
  KEY `FKB7D03A3B31446627` (`ITEMID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDITEMFEEDBACK_T`
--

LOCK TABLES `SAM_PUBLISHEDITEMFEEDBACK_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDITEMFEEDBACK_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDITEMFEEDBACK_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDITEMMETADATA_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDITEMMETADATA_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDITEMMETADATA_T` (
  `ITEMMETADATAID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMID` bigint(20) NOT NULL,
  `LABEL` varchar(255) NOT NULL,
  `ENTRY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ITEMMETADATAID`),
  KEY `FKE0C2876531446627` (`ITEMID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDITEMMETADATA_T`
--

LOCK TABLES `SAM_PUBLISHEDITEMMETADATA_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDITEMMETADATA_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDITEMMETADATA_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDITEMTEXT_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDITEMTEXT_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDITEMTEXT_T` (
  `ITEMTEXTID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMID` bigint(20) NOT NULL,
  `SEQUENCE` bigint(20) NOT NULL,
  `TEXT` text,
  PRIMARY KEY (`ITEMTEXTID`),
  KEY `FK9C790A6331446627` (`ITEMID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDITEMTEXT_T`
--

LOCK TABLES `SAM_PUBLISHEDITEMTEXT_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDITEMTEXT_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDITEMTEXT_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDITEM_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDITEM_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDITEM_T` (
  `ITEMID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SECTIONID` bigint(20) NOT NULL,
  `ITEMIDSTRING` varchar(255) DEFAULT NULL,
  `SEQUENCE` int(11) DEFAULT NULL,
  `DURATION` int(11) DEFAULT NULL,
  `TRIESALLOWED` int(11) DEFAULT NULL,
  `INSTRUCTION` text,
  `DESCRIPTION` text,
  `TYPEID` bigint(20) NOT NULL,
  `GRADE` varchar(80) DEFAULT NULL,
  `SCORE` float DEFAULT NULL,
  `DISCOUNT` float DEFAULT NULL,
  `HINT` text,
  `HASRATIONALE` bit(1) DEFAULT NULL,
  `PARTIAL_CREDIT_FLAG` bit(1) DEFAULT NULL,
  `STATUS` int(11) NOT NULL,
  `CREATEDBY` varchar(255) NOT NULL,
  `CREATEDDATE` datetime NOT NULL,
  `LASTMODIFIEDBY` varchar(255) NOT NULL,
  `LASTMODIFIEDDATE` datetime NOT NULL,
  PRIMARY KEY (`ITEMID`),
  KEY `FK53ABDCF6895D4813` (`SECTIONID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDITEM_T`
--

LOCK TABLES `SAM_PUBLISHEDITEM_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDITEM_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDITEM_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDMETADATA_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDMETADATA_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDMETADATA_T` (
  `ASSESSMENTMETADATAID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ASSESSMENTID` bigint(20) NOT NULL,
  `LABEL` varchar(255) NOT NULL,
  `ENTRY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ASSESSMENTMETADATAID`),
  KEY `FK3D7B27129482C945` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDMETADATA_T`
--

LOCK TABLES `SAM_PUBLISHEDMETADATA_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDMETADATA_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDMETADATA_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDSECTIONMETADATA_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDSECTIONMETADATA_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDSECTIONMETADATA_T` (
  `PUBLISHEDSECTIONMETADATAID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SECTIONID` bigint(20) NOT NULL,
  `LABEL` varchar(255) NOT NULL,
  `ENTRY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`PUBLISHEDSECTIONMETADATAID`),
  KEY `FKDF50FC3B895D4813` (`SECTIONID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDSECTIONMETADATA_T`
--

LOCK TABLES `SAM_PUBLISHEDSECTIONMETADATA_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDSECTIONMETADATA_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDSECTIONMETADATA_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDSECTION_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDSECTION_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDSECTION_T` (
  `SECTIONID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ASSESSMENTID` bigint(20) NOT NULL,
  `DURATION` int(11) DEFAULT NULL,
  `SEQUENCE` int(11) DEFAULT NULL,
  `TITLE` varchar(255) DEFAULT NULL,
  `DESCRIPTION` text,
  `TYPEID` bigint(20) NOT NULL,
  `STATUS` int(11) NOT NULL,
  `CREATEDBY` varchar(255) NOT NULL,
  `CREATEDDATE` datetime NOT NULL,
  `LASTMODIFIEDBY` varchar(255) NOT NULL,
  `LASTMODIFIEDDATE` datetime NOT NULL,
  PRIMARY KEY (`SECTIONID`),
  KEY `FK424F87CC9482C945` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDSECTION_T`
--

LOCK TABLES `SAM_PUBLISHEDSECTION_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDSECTION_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDSECTION_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_PUBLISHEDSECUREDIP_T`
--

DROP TABLE IF EXISTS `SAM_PUBLISHEDSECUREDIP_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_PUBLISHEDSECUREDIP_T` (
  `IPADDRESSID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ASSESSMENTID` bigint(20) NOT NULL,
  `HOSTNAME` varchar(255) DEFAULT NULL,
  `IPADDRESS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`IPADDRESSID`),
  KEY `FK1EDEA25B9482C945` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_PUBLISHEDSECUREDIP_T`
--

LOCK TABLES `SAM_PUBLISHEDSECUREDIP_T` WRITE;
/*!40000 ALTER TABLE `SAM_PUBLISHEDSECUREDIP_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_PUBLISHEDSECUREDIP_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_QUALIFIERDATA_T`
--

DROP TABLE IF EXISTS `SAM_QUALIFIERDATA_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_QUALIFIERDATA_T` (
  `QUALIFIERID` bigint(20) NOT NULL,
  `REFERENCENAME` varchar(255) NOT NULL,
  `DISPLAYNAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` text,
  `QUALIFIERTYPEID` text,
  PRIMARY KEY (`QUALIFIERID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_QUALIFIERDATA_T`
--

LOCK TABLES `SAM_QUALIFIERDATA_T` WRITE;
/*!40000 ALTER TABLE `SAM_QUALIFIERDATA_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_QUALIFIERDATA_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_QUESTIONPOOLACCESS_T`
--

DROP TABLE IF EXISTS `SAM_QUESTIONPOOLACCESS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_QUESTIONPOOLACCESS_T` (
  `QUESTIONPOOLID` bigint(20) NOT NULL,
  `AGENTID` varchar(255) NOT NULL,
  `ACCESSTYPEID` bigint(20) NOT NULL,
  PRIMARY KEY (`QUESTIONPOOLID`,`AGENTID`,`ACCESSTYPEID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_QUESTIONPOOLACCESS_T`
--

LOCK TABLES `SAM_QUESTIONPOOLACCESS_T` WRITE;
/*!40000 ALTER TABLE `SAM_QUESTIONPOOLACCESS_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_QUESTIONPOOLACCESS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_QUESTIONPOOLITEM_T`
--

DROP TABLE IF EXISTS `SAM_QUESTIONPOOLITEM_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_QUESTIONPOOLITEM_T` (
  `QUESTIONPOOLID` bigint(20) NOT NULL,
  `ITEMID` varchar(255) NOT NULL,
  PRIMARY KEY (`QUESTIONPOOLID`,`ITEMID`),
  KEY `FKF0FAAE2A39ED26BB` (`QUESTIONPOOLID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_QUESTIONPOOLITEM_T`
--

LOCK TABLES `SAM_QUESTIONPOOLITEM_T` WRITE;
/*!40000 ALTER TABLE `SAM_QUESTIONPOOLITEM_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_QUESTIONPOOLITEM_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_QUESTIONPOOL_T`
--

DROP TABLE IF EXISTS `SAM_QUESTIONPOOL_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_QUESTIONPOOL_T` (
  `QUESTIONPOOLID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `PARENTPOOLID` bigint(20) DEFAULT NULL,
  `OWNERID` varchar(255) DEFAULT NULL,
  `ORGANIZATIONNAME` varchar(255) DEFAULT NULL,
  `DATECREATED` datetime DEFAULT NULL,
  `LASTMODIFIEDDATE` datetime DEFAULT NULL,
  `LASTMODIFIEDBY` varchar(255) DEFAULT NULL,
  `DEFAULTACCESSTYPEID` bigint(20) DEFAULT NULL,
  `OBJECTIVE` varchar(255) DEFAULT NULL,
  `KEYWORDS` varchar(255) DEFAULT NULL,
  `RUBRIC` text,
  `TYPEID` bigint(20) DEFAULT NULL,
  `INTELLECTUALPROPERTYID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`QUESTIONPOOLID`),
  KEY `SAM_QPOOL_OWNER_I` (`OWNERID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_QUESTIONPOOL_T`
--

LOCK TABLES `SAM_QUESTIONPOOL_T` WRITE;
/*!40000 ALTER TABLE `SAM_QUESTIONPOOL_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_QUESTIONPOOL_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_SECTIONMETADATA_T`
--

DROP TABLE IF EXISTS `SAM_SECTIONMETADATA_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_SECTIONMETADATA_T` (
  `SECTIONMETADATAID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SECTIONID` bigint(20) NOT NULL,
  `LABEL` varchar(255) NOT NULL,
  `ENTRY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`SECTIONMETADATAID`),
  KEY `FK762AD74970CE2BD` (`SECTIONID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_SECTIONMETADATA_T`
--

LOCK TABLES `SAM_SECTIONMETADATA_T` WRITE;
/*!40000 ALTER TABLE `SAM_SECTIONMETADATA_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_SECTIONMETADATA_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_SECTION_T`
--

DROP TABLE IF EXISTS `SAM_SECTION_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_SECTION_T` (
  `SECTIONID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ASSESSMENTID` bigint(20) NOT NULL,
  `DURATION` int(11) DEFAULT NULL,
  `SEQUENCE` int(11) DEFAULT NULL,
  `TITLE` varchar(255) DEFAULT NULL,
  `DESCRIPTION` text,
  `TYPEID` bigint(20) DEFAULT NULL,
  `STATUS` int(11) NOT NULL,
  `CREATEDBY` varchar(255) NOT NULL,
  `CREATEDDATE` datetime NOT NULL,
  `LASTMODIFIEDBY` varchar(255) NOT NULL,
  `LASTMODIFIEDDATE` datetime NOT NULL,
  PRIMARY KEY (`SECTIONID`),
  KEY `FK364450DACAC2365B` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_SECTION_T`
--

LOCK TABLES `SAM_SECTION_T` WRITE;
/*!40000 ALTER TABLE `SAM_SECTION_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_SECTION_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_SECUREDIP_T`
--

DROP TABLE IF EXISTS `SAM_SECUREDIP_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_SECUREDIP_T` (
  `IPADDRESSID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ASSESSMENTID` bigint(20) NOT NULL,
  `HOSTNAME` varchar(255) DEFAULT NULL,
  `IPADDRESS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`IPADDRESSID`),
  KEY `FKE8C55FE9694216CC` (`ASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_SECUREDIP_T`
--

LOCK TABLES `SAM_SECUREDIP_T` WRITE;
/*!40000 ALTER TABLE `SAM_SECUREDIP_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_SECUREDIP_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_STUDENTGRADINGSUMMARY_T`
--

DROP TABLE IF EXISTS `SAM_STUDENTGRADINGSUMMARY_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_STUDENTGRADINGSUMMARY_T` (
  `STUDENTGRADINGSUMMARYID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PUBLISHEDASSESSMENTID` bigint(20) NOT NULL,
  `AGENTID` varchar(255) NOT NULL,
  `NUMBERRETAKE` int(11) DEFAULT NULL,
  `CREATEDBY` varchar(255) NOT NULL,
  `CREATEDDATE` datetime NOT NULL,
  `LASTMODIFIEDBY` varchar(255) NOT NULL,
  `LASTMODIFIEDDATE` datetime NOT NULL,
  PRIMARY KEY (`STUDENTGRADINGSUMMARYID`),
  KEY `SAM_PUBLISHEDASSESSMENT2_I` (`PUBLISHEDASSESSMENTID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_STUDENTGRADINGSUMMARY_T`
--

LOCK TABLES `SAM_STUDENTGRADINGSUMMARY_T` WRITE;
/*!40000 ALTER TABLE `SAM_STUDENTGRADINGSUMMARY_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `SAM_STUDENTGRADINGSUMMARY_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SAM_TYPE_T`
--

DROP TABLE IF EXISTS `SAM_TYPE_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SAM_TYPE_T` (
  `TYPEID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AUTHORITY` varchar(255) DEFAULT NULL,
  `DOMAIN` varchar(255) DEFAULT NULL,
  `KEYWORD` varchar(255) DEFAULT NULL,
  `DESCRIPTION` text,
  `STATUS` int(11) NOT NULL,
  `CREATEDBY` varchar(255) NOT NULL,
  `CREATEDDATE` datetime NOT NULL,
  `LASTMODIFIEDBY` varchar(255) NOT NULL,
  `LASTMODIFIEDDATE` datetime NOT NULL,
  PRIMARY KEY (`TYPEID`)
) ENGINE=MyISAM AUTO_INCREMENT=143 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SAM_TYPE_T`
--

LOCK TABLES `SAM_TYPE_T` WRITE;
/*!40000 ALTER TABLE `SAM_TYPE_T` DISABLE KEYS */;
INSERT INTO `SAM_TYPE_T` VALUES (1,'stanford.edu','assessment.item','Multiple Choice',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(2,'stanford.edu','assessment.item','Multiple Correct',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(3,'stanford.edu','assessment.item','Multiple Choice Survey',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(4,'stanford.edu','assessment.item','True - False',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(5,'stanford.edu','assessment.item','Short Answer/Essay',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(6,'stanford.edu','assessment.item','File Upload',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(7,'stanford.edu','assessment.item','Audio Recording',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(8,'stanford.edu','assessment.item','Fill in Blank',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(11,'stanford.edu','assessment.item','Numeric Response',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(9,'stanford.edu','assessment.item','Matching',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(12,'stanford.edu','assessment.item','Multiple Correct Single Selection',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(21,'stanford.edu','assessment.section','Default',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(22,'stanford.edu','assessment.section','Normal',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(41,'stanford.edu','assessment.template','Quiz',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(42,'stanford.edu','assessment.template','Homework',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(43,'stanford.edu','assessment.template','Mid Term',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(44,'stanford.edu','assessment.template','Final',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(61,'stanford.edu','assessment','Quiz',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(62,'stanford.edu','assessment','Homework',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(63,'stanford.edu','assessment','Mid Term',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(64,'stanford.edu','assessment','Final',NULL,1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(65,'stanford.edu','assessment.questionpool','Default','Stanford Question Pool',1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(30,'stanford.edu','assessment.questionpool.access','Access Denied','Access Denied',1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(31,'stanford.edu','assessment.questionpool.access','Read Only','Read Only',1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(32,'stanford.edu','assessment.questionpool.access','Read and Copy','Read and Copy',1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(33,'stanford.edu','assessment.questionpool.access','Read/Write','Read/Write',1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(34,'stanford.edu','assessment.questionpool.access','Administration','Adminstration',1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(81,'stanford.edu','assessment.taking','Taking Assessment','Taking Assessment',1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(101,'stanford.edu','assessment.published','A Published Assessment','A Published Assessment',1,'1','2005-01-01 12:00:00','1','2005-01-01 12:00:00'),(142,'stanford.edu','assessment.template.system','System Defined',NULL,1,'1','2006-01-01 12:00:00','1','2005-06-01 12:00:00');
/*!40000 ALTER TABLE `SAM_TYPE_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SCHEDULER_DELAYED_INVOCATION`
--

DROP TABLE IF EXISTS `SCHEDULER_DELAYED_INVOCATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SCHEDULER_DELAYED_INVOCATION` (
  `INVOCATION_ID` varchar(36) NOT NULL,
  `INVOCATION_TIME` datetime NOT NULL,
  `COMPONENT` varchar(2000) NOT NULL,
  `CONTEXT` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`INVOCATION_ID`),
  KEY `SCHEDULER_DI_TIME_INDEX` (`INVOCATION_TIME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SCHEDULER_DELAYED_INVOCATION`
--

LOCK TABLES `SCHEDULER_DELAYED_INVOCATION` WRITE;
/*!40000 ALTER TABLE `SCHEDULER_DELAYED_INVOCATION` DISABLE KEYS */;
/*!40000 ALTER TABLE `SCHEDULER_DELAYED_INVOCATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SITEASSOC_CONTEXT_ASSOCIATION`
--

DROP TABLE IF EXISTS `SITEASSOC_CONTEXT_ASSOCIATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SITEASSOC_CONTEXT_ASSOCIATION` (
  `FROM_CONTEXT` varchar(99) NOT NULL,
  `TO_CONTEXT` varchar(99) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`FROM_CONTEXT`,`TO_CONTEXT`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SITEASSOC_CONTEXT_ASSOCIATION`
--

LOCK TABLES `SITEASSOC_CONTEXT_ASSOCIATION` WRITE;
/*!40000 ALTER TABLE `SITEASSOC_CONTEXT_ASSOCIATION` DISABLE KEYS */;
/*!40000 ALTER TABLE `SITEASSOC_CONTEXT_ASSOCIATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SSQ_ANSWER`
--

DROP TABLE IF EXISTS `SSQ_ANSWER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SSQ_ANSWER` (
  `ID` varchar(99) NOT NULL,
  `ANSWER` varchar(255) DEFAULT NULL,
  `ANSWER_STRING` varchar(255) DEFAULT NULL,
  `FILL_IN_BLANK` bit(1) DEFAULT NULL,
  `ORDER_NUM` int(11) DEFAULT NULL,
  `QUESTION_ID` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK390C0DCC6B21AFB4` (`QUESTION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SSQ_ANSWER`
--

LOCK TABLES `SSQ_ANSWER` WRITE;
/*!40000 ALTER TABLE `SSQ_ANSWER` DISABLE KEYS */;
/*!40000 ALTER TABLE `SSQ_ANSWER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SSQ_QUESTION`
--

DROP TABLE IF EXISTS `SSQ_QUESTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SSQ_QUESTION` (
  `ID` varchar(99) NOT NULL,
  `QUESTION` varchar(255) DEFAULT NULL,
  `REQUIRED` bit(1) DEFAULT NULL,
  `MULTIPLE_ANSWERS` bit(1) DEFAULT NULL,
  `ORDER_NUM` int(11) DEFAULT NULL,
  `IS_CURRENT` varchar(255) DEFAULT NULL,
  `SITETYPE_ID` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKFE88BA7443AD4C69` (`SITETYPE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='This table stores site setup questions';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SSQ_QUESTION`
--

LOCK TABLES `SSQ_QUESTION` WRITE;
/*!40000 ALTER TABLE `SSQ_QUESTION` DISABLE KEYS */;
/*!40000 ALTER TABLE `SSQ_QUESTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SSQ_SITETYPE_QUESTIONS`
--

DROP TABLE IF EXISTS `SSQ_SITETYPE_QUESTIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SSQ_SITETYPE_QUESTIONS` (
  `ID` varchar(99) NOT NULL,
  `SITE_TYPE` varchar(255) DEFAULT NULL,
  `INSTRUCTION` varchar(255) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `URL_LABEL` varchar(255) DEFAULT NULL,
  `URL_Target` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SSQ_SITETYPE_QUESTIONS`
--

LOCK TABLES `SSQ_SITETYPE_QUESTIONS` WRITE;
/*!40000 ALTER TABLE `SSQ_SITETYPE_QUESTIONS` DISABLE KEYS */;
/*!40000 ALTER TABLE `SSQ_SITETYPE_QUESTIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SSQ_USER_ANSWER`
--

DROP TABLE IF EXISTS `SSQ_USER_ANSWER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SSQ_USER_ANSWER` (
  `ID` varchar(99) NOT NULL,
  `SITE_ID` varchar(255) DEFAULT NULL,
  `USER_ID` varchar(255) DEFAULT NULL,
  `ANSWER_STRING` varchar(255) DEFAULT NULL,
  `ANSWER_ID` varchar(255) DEFAULT NULL,
  `QUESTION_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SSQ_USER_ANSWER`
--

LOCK TABLES `SSQ_USER_ANSWER` WRITE;
/*!40000 ALTER TABLE `SSQ_USER_ANSWER` DISABLE KEYS */;
/*!40000 ALTER TABLE `SSQ_USER_ANSWER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SST_EVENTS`
--

DROP TABLE IF EXISTS `SST_EVENTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SST_EVENTS` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(99) NOT NULL,
  `SITE_ID` varchar(99) NOT NULL,
  `EVENT_ID` varchar(32) NOT NULL,
  `EVENT_DATE` date NOT NULL,
  `EVENT_COUNT` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SST_EVENTS`
--

LOCK TABLES `SST_EVENTS` WRITE;
/*!40000 ALTER TABLE `SST_EVENTS` DISABLE KEYS */;
/*!40000 ALTER TABLE `SST_EVENTS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SST_JOB_RUN`
--

DROP TABLE IF EXISTS `SST_JOB_RUN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SST_JOB_RUN` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `JOB_START_DATE` datetime DEFAULT NULL,
  `JOB_END_DATE` datetime DEFAULT NULL,
  `START_EVENT_ID` bigint(20) DEFAULT NULL,
  `END_EVENT_ID` bigint(20) DEFAULT NULL,
  `LAST_EVENT_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SST_JOB_RUN`
--

LOCK TABLES `SST_JOB_RUN` WRITE;
/*!40000 ALTER TABLE `SST_JOB_RUN` DISABLE KEYS */;
/*!40000 ALTER TABLE `SST_JOB_RUN` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SST_PREFERENCES`
--

DROP TABLE IF EXISTS `SST_PREFERENCES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SST_PREFERENCES` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SITE_ID` varchar(99) NOT NULL,
  `PREFS` text NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SST_PREFERENCES`
--

LOCK TABLES `SST_PREFERENCES` WRITE;
/*!40000 ALTER TABLE `SST_PREFERENCES` DISABLE KEYS */;
/*!40000 ALTER TABLE `SST_PREFERENCES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SST_PRESENCES`
--

DROP TABLE IF EXISTS `SST_PRESENCES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SST_PRESENCES` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SITE_ID` varchar(99) NOT NULL,
  `USER_ID` varchar(99) NOT NULL,
  `P_DATE` date NOT NULL,
  `DURATION` bigint(20) NOT NULL DEFAULT '0',
  `LAST_VISIT_START_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SST_PRESENCES`
--

LOCK TABLES `SST_PRESENCES` WRITE;
/*!40000 ALTER TABLE `SST_PRESENCES` DISABLE KEYS */;
/*!40000 ALTER TABLE `SST_PRESENCES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SST_REPORTS`
--

DROP TABLE IF EXISTS `SST_REPORTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SST_REPORTS` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SITE_ID` varchar(99) DEFAULT NULL,
  `TITLE` varchar(255) NOT NULL,
  `DESCRIPTION` longtext,
  `HIDDEN` bit(1) DEFAULT NULL,
  `REPORT_DEF` text NOT NULL,
  `CREATED_BY` varchar(99) NOT NULL,
  `CREATED_ON` datetime NOT NULL,
  `MODIFIED_BY` varchar(99) DEFAULT NULL,
  `MODIFIED_ON` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SST_REPORTS`
--

LOCK TABLES `SST_REPORTS` WRITE;
/*!40000 ALTER TABLE `SST_REPORTS` DISABLE KEYS */;
/*!40000 ALTER TABLE `SST_REPORTS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SST_RESOURCES`
--

DROP TABLE IF EXISTS `SST_RESOURCES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SST_RESOURCES` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(99) NOT NULL,
  `SITE_ID` varchar(99) NOT NULL,
  `RESOURCE_REF` varchar(255) NOT NULL,
  `RESOURCE_ACTION` varchar(12) NOT NULL,
  `RESOURCE_DATE` date NOT NULL,
  `RESOURCE_COUNT` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SST_RESOURCES`
--

LOCK TABLES `SST_RESOURCES` WRITE;
/*!40000 ALTER TABLE `SST_RESOURCES` DISABLE KEYS */;
/*!40000 ALTER TABLE `SST_RESOURCES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SST_SITEACTIVITY`
--

DROP TABLE IF EXISTS `SST_SITEACTIVITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SST_SITEACTIVITY` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SITE_ID` varchar(99) NOT NULL,
  `ACTIVITY_DATE` date NOT NULL,
  `EVENT_ID` varchar(32) NOT NULL,
  `ACTIVITY_COUNT` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SST_SITEACTIVITY`
--

LOCK TABLES `SST_SITEACTIVITY` WRITE;
/*!40000 ALTER TABLE `SST_SITEACTIVITY` DISABLE KEYS */;
/*!40000 ALTER TABLE `SST_SITEACTIVITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SST_SITEVISITS`
--

DROP TABLE IF EXISTS `SST_SITEVISITS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SST_SITEVISITS` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SITE_ID` varchar(99) NOT NULL,
  `VISITS_DATE` date NOT NULL,
  `TOTAL_VISITS` bigint(20) NOT NULL,
  `TOTAL_UNIQUE` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SST_SITEVISITS`
--

LOCK TABLES `SST_SITEVISITS` WRITE;
/*!40000 ALTER TABLE `SST_SITEVISITS` DISABLE KEYS */;
/*!40000 ALTER TABLE `SST_SITEVISITS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TAGGABLE_LINK`
--

DROP TABLE IF EXISTS `TAGGABLE_LINK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TAGGABLE_LINK` (
  `LINK_ID` varchar(36) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `ACTIVITY_REF` varchar(255) NOT NULL,
  `TAG_CRITERIA_REF` varchar(255) NOT NULL,
  `RUBRIC` text,
  `RATIONALE` text,
  `EXPORT_STRING` int(11) NOT NULL,
  `VISIBLE` bit(1) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  PRIMARY KEY (`LINK_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TAGGABLE_LINK`
--

LOCK TABLES `TAGGABLE_LINK` WRITE;
/*!40000 ALTER TABLE `TAGGABLE_LINK` DISABLE KEYS */;
/*!40000 ALTER TABLE `TAGGABLE_LINK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `URL_RANDOMISED_MAPPINGS_T`
--

DROP TABLE IF EXISTS `URL_RANDOMISED_MAPPINGS_T`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `URL_RANDOMISED_MAPPINGS_T` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TINY` varchar(255) NOT NULL,
  `URL` text NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `URL_RANDOMISED_MAPPINGS_T`
--

LOCK TABLES `URL_RANDOMISED_MAPPINGS_T` WRITE;
/*!40000 ALTER TABLE `URL_RANDOMISED_MAPPINGS_T` DISABLE KEYS */;
/*!40000 ALTER TABLE `URL_RANDOMISED_MAPPINGS_T` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VALIDATIONACCOUNT_ITEM`
--

DROP TABLE IF EXISTS `VALIDATIONACCOUNT_ITEM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VALIDATIONACCOUNT_ITEM` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(255) NOT NULL,
  `VALIDATION_TOKEN` varchar(255) NOT NULL,
  `VALIDATION_SENT` datetime DEFAULT NULL,
  `VALIDATION_RECEIVED` datetime DEFAULT NULL,
  `VALIDATIONS_SENT` int(11) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `FIRST_NAME` varchar(255) NOT NULL,
  `SURNAME` varchar(255) NOT NULL,
  `ACCOUNT_STATUS` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VALIDATIONACCOUNT_ITEM`
--

LOCK TABLES `VALIDATIONACCOUNT_ITEM` WRITE;
/*!40000 ALTER TABLE `VALIDATIONACCOUNT_ITEM` DISABLE KEYS */;
/*!40000 ALTER TABLE `VALIDATIONACCOUNT_ITEM` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `content_resource_lock`
--

DROP TABLE IF EXISTS `content_resource_lock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_resource_lock` (
  `id` varchar(36) NOT NULL,
  `asset_id` varchar(36) DEFAULT NULL,
  `qualifier_id` varchar(36) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `is_system` bit(1) DEFAULT NULL,
  `reason` varchar(36) DEFAULT NULL,
  `date_added` datetime DEFAULT NULL,
  `date_removed` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content_resource_lock`
--

LOCK TABLES `content_resource_lock` WRITE;
/*!40000 ALTER TABLE `content_resource_lock` DISABLE KEYS */;
INSERT INTO `content_resource_lock` VALUES ('f8682c82-d2b3-4426-a63b-ed55b4d98558','2e48c42d-f61c-44fe-bdb8-870e84e0a5d7','freeFormTemplate','','','saving a presentation template','2011-04-13 15:07:03',NULL),('1ed8a7a1-d890-4e7b-81b0-fa2626892289','b0436581-e11f-4c19-a004-cd8775baad17','simpleRichText','','','saving a presentation layout','2011-04-13 15:07:03',NULL),('356044da-6d31-47eb-bfca-044f5a77de33','badfb3c8-f143-47fe-9192-7a53bb463495','twoColumn','','','saving a presentation layout','2011-04-13 15:07:03',NULL),('a8d8b1ed-4cda-4e06-94c1-b4d1576c26c1','cc793aa9-71ea-4d45-be38-b414b4aa33ac','twoColumn','','','saving a presentation layout','2011-04-13 15:07:03',NULL),('1f3da784-9600-4c0a-8487-ca286d7bf3a5','a483d33b-6b6b-4c7c-b69e-901065cf247a','contentOverText','','','saving a presentation layout','2011-04-13 15:07:03',NULL),('d967212c-2e66-441d-adea-ec9168d96e36','f25eb3d7-5900-4f03-9a4f-09d53ab65dd5','contentOverText','','','saving a presentation layout','2011-04-13 15:07:03',NULL),('835739f5-c888-489f-9e01-8500501b233b','9f4061a9-ea10-4121-8bd2-ed9e8a465156','simpleRichText','','','saving a presentation layout','2011-04-13 15:07:03',NULL);
/*!40000 ALTER TABLE `content_resource_lock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_assignment_status`
--

DROP TABLE IF EXISTS `dw_assignment_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_assignment_status` (
  `USER_ID` varchar(99) NOT NULL,
  `ASSIGNMENT_ID` varchar(99) NOT NULL,
  `COURSE_TITLE` text,
  `ASSIGNMENT_TITLE` text,
  `ASSIGNMENT_STATUS` varchar(64) DEFAULT NULL,
  `ASSIGNMENT_GRADE` varchar(64) DEFAULT NULL,
  `STUDENT_FIRST_NAME` varchar(128) DEFAULT NULL,
  `STUDENT_LAST_NAME` varchar(128) DEFAULT NULL,
  `SCHOOL` varchar(128) DEFAULT NULL,
  `DISTRICT` varchar(128) DEFAULT NULL,
  `CLASS_YEAR` varchar(128) DEFAULT NULL,
  `ADVISOR` varchar(128) DEFAULT NULL,
  `COURSE_TERM` varchar(128) DEFAULT NULL,
  `COURSE_CODE` varchar(128) DEFAULT NULL,
  `COURSE_SECTION` varchar(128) DEFAULT NULL,
  `COURSE_START_DATE` varchar(128) DEFAULT NULL,
  `INSTRUCTOR` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`ASSIGNMENT_ID`,`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_assignment_status`
--

LOCK TABLES `dw_assignment_status` WRITE;
/*!40000 ALTER TABLE `dw_assignment_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_assignment_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_content_resource_lock`
--

DROP TABLE IF EXISTS `dw_content_resource_lock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_content_resource_lock` (
  `id` varchar(36) NOT NULL,
  `asset_id` varchar(36) DEFAULT NULL,
  `qualifier_id` varchar(36) DEFAULT NULL,
  `is_active` tinyint(4) DEFAULT NULL,
  `is_system` tinyint(4) DEFAULT NULL,
  `reason` varchar(36) DEFAULT NULL,
  `date_added` datetime DEFAULT NULL,
  `date_removed` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_content_resource_lock`
--

LOCK TABLES `dw_content_resource_lock` WRITE;
/*!40000 ALTER TABLE `dw_content_resource_lock` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_content_resource_lock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_guidance`
--

DROP TABLE IF EXISTS `dw_guidance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_guidance` (
  `id` varchar(36) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `site_id` varchar(36) NOT NULL,
  `securityQualifier` varchar(255) DEFAULT NULL,
  `securityViewFunction` varchar(255) NOT NULL,
  `securityEditFunction` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_guidance`
--

LOCK TABLES `dw_guidance` WRITE;
/*!40000 ALTER TABLE `dw_guidance` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_guidance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_guidance_item`
--

DROP TABLE IF EXISTS `dw_guidance_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_guidance_item` (
  `id` varchar(36) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `guidance_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK605DDBA7BE2FA762` (`guidance_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_guidance_item`
--

LOCK TABLES `dw_guidance_item` WRITE;
/*!40000 ALTER TABLE `dw_guidance_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_guidance_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_guidance_item_file`
--

DROP TABLE IF EXISTS `dw_guidance_item_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_guidance_item_file` (
  `id` varchar(36) NOT NULL,
  `baseReference` varchar(255) DEFAULT NULL,
  `fullReference` varchar(255) DEFAULT NULL,
  `item_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK297703147E22B9C7` (`item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_guidance_item_file`
--

LOCK TABLES `dw_guidance_item_file` WRITE;
/*!40000 ALTER TABLE `dw_guidance_item_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_guidance_item_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_matrix`
--

DROP TABLE IF EXISTS `dw_matrix`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_matrix` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `scaffolding_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5A17205459517621` (`scaffolding_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_matrix`
--

LOCK TABLES `dw_matrix` WRITE;
/*!40000 ALTER TABLE `dw_matrix` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_matrix` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_matrix_cell`
--

DROP TABLE IF EXISTS `dw_matrix_cell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_matrix_cell` (
  `id` varchar(36) NOT NULL,
  `matrix_id` varchar(36) NOT NULL,
  `wizard_page_id` varchar(36) NOT NULL,
  `scaffolding_cell_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8C1D366D9AAD0C05` (`scaffolding_cell_id`),
  KEY `FK8C1D366D3E503659` (`matrix_id`),
  KEY `FK8C1D366DFE6D91AF` (`wizard_page_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_matrix_cell`
--

LOCK TABLES `dw_matrix_cell` WRITE;
/*!40000 ALTER TABLE `dw_matrix_cell` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_matrix_cell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_metaobj_form_def`
--

DROP TABLE IF EXISTS `dw_metaobj_form_def`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_metaobj_form_def` (
  `id` varchar(36) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `documentRoot` varchar(255) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `systemOnly` tinyint(4) NOT NULL,
  `externalType` varchar(255) NOT NULL,
  `siteId` varchar(255) DEFAULT NULL,
  `siteState` int(11) NOT NULL,
  `globalState` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_metaobj_form_def`
--

LOCK TABLES `dw_metaobj_form_def` WRITE;
/*!40000 ALTER TABLE `dw_metaobj_form_def` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_metaobj_form_def` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_pres_itemdef_mimetype`
--

DROP TABLE IF EXISTS `dw_pres_itemdef_mimetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_pres_itemdef_mimetype` (
  `item_def_id` varchar(36) NOT NULL,
  `primaryMimeType` varchar(36) DEFAULT NULL,
  `secondaryMimeType` varchar(36) DEFAULT NULL,
  KEY `FK9EA59837CEC82A41` (`item_def_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_pres_itemdef_mimetype`
--

LOCK TABLES `dw_pres_itemdef_mimetype` WRITE;
/*!40000 ALTER TABLE `dw_pres_itemdef_mimetype` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_pres_itemdef_mimetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_presentation`
--

DROP TABLE IF EXISTS `dw_presentation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_presentation` (
  `id` varchar(36) NOT NULL,
  `owner_id` varchar(255) NOT NULL,
  `template_id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `isDefault` tinyint(4) DEFAULT NULL,
  `isPublic` tinyint(4) DEFAULT NULL,
  `expiresOn` datetime DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA9028D6D697A9B00` (`template_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_presentation`
--

LOCK TABLES `dw_presentation` WRITE;
/*!40000 ALTER TABLE `dw_presentation` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_presentation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_presentation_comment`
--

DROP TABLE IF EXISTS `dw_presentation_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_presentation_comment` (
  `id` varchar(36) NOT NULL,
  `title` varchar(255) NOT NULL,
  `creator_id` varchar(255) NOT NULL,
  `presentation_id` varchar(36) NOT NULL,
  `visibility` tinyint(4) NOT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1E7E658D662DE460` (`presentation_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_presentation_comment`
--

LOCK TABLES `dw_presentation_comment` WRITE;
/*!40000 ALTER TABLE `dw_presentation_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_presentation_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_presentation_item`
--

DROP TABLE IF EXISTS `dw_presentation_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_presentation_item` (
  `presentation_id` varchar(36) NOT NULL,
  `artifact_id` varchar(36) DEFAULT NULL,
  `item_definition_id` varchar(36) NOT NULL,
  KEY `FK2FA02A5FB2AC75B` (`item_definition_id`),
  KEY `FK2FA02A5662DE460` (`presentation_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_presentation_item`
--

LOCK TABLES `dw_presentation_item` WRITE;
/*!40000 ALTER TABLE `dw_presentation_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_presentation_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_presentation_item_def`
--

DROP TABLE IF EXISTS `dw_presentation_item_def`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_presentation_item_def` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `allowMultiple` tinyint(4) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `external_type` varchar(255) DEFAULT NULL,
  `sequence_no` int(11) DEFAULT NULL,
  `template_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1B6ADB6B697A9B00` (`template_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_presentation_item_def`
--

LOCK TABLES `dw_presentation_item_def` WRITE;
/*!40000 ALTER TABLE `dw_presentation_item_def` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_presentation_item_def` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_presentation_item_property`
--

DROP TABLE IF EXISTS `dw_presentation_item_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_presentation_item_property` (
  `id` varchar(36) NOT NULL,
  `presentation_page_item_id` varchar(36) NOT NULL,
  `property_key` varchar(255) NOT NULL,
  `property_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK86B136F2150091C` (`presentation_page_item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_presentation_item_property`
--

LOCK TABLES `dw_presentation_item_property` WRITE;
/*!40000 ALTER TABLE `dw_presentation_item_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_presentation_item_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_presentation_layout`
--

DROP TABLE IF EXISTS `dw_presentation_layout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_presentation_layout` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `globalState` tinyint(4) DEFAULT NULL,
  `owner_id` varchar(255) NOT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `xhtml_file_id` varchar(36) NOT NULL,
  `preview_image_id` varchar(36) DEFAULT NULL,
  `tool_id` varchar(36) DEFAULT NULL,
  `site_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_presentation_layout`
--

LOCK TABLES `dw_presentation_layout` WRITE;
/*!40000 ALTER TABLE `dw_presentation_layout` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_presentation_layout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_presentation_log`
--

DROP TABLE IF EXISTS `dw_presentation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_presentation_log` (
  `id` varchar(36) NOT NULL,
  `viewer_id` varchar(255) NOT NULL,
  `presentation_id` varchar(36) NOT NULL,
  `view_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2120E172662DE460` (`presentation_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_presentation_log`
--

LOCK TABLES `dw_presentation_log` WRITE;
/*!40000 ALTER TABLE `dw_presentation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_presentation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_presentation_page`
--

DROP TABLE IF EXISTS `dw_presentation_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_presentation_page` (
  `id` varchar(36) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `presentation_id` varchar(36) NOT NULL,
  `layout_id` varchar(36) NOT NULL,
  `style_id` varchar(255) DEFAULT NULL,
  `seq_num` varchar(255) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2FCEA21662DE460` (`presentation_id`),
  KEY `FK2FCEA21AE13AE50` (`layout_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_presentation_page`
--

LOCK TABLES `dw_presentation_page` WRITE;
/*!40000 ALTER TABLE `dw_presentation_page` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_presentation_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_presentation_page_item`
--

DROP TABLE IF EXISTS `dw_presentation_page_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_presentation_page_item` (
  `id` varchar(36) NOT NULL,
  `presentation_page_region_id` varchar(36) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `value` text,
  `seq_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK64176719185445B` (`presentation_page_region_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_presentation_page_item`
--

LOCK TABLES `dw_presentation_page_item` WRITE;
/*!40000 ALTER TABLE `dw_presentation_page_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_presentation_page_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_presentation_page_region`
--

DROP TABLE IF EXISTS `dw_presentation_page_region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_presentation_page_region` (
  `id` varchar(36) NOT NULL,
  `presentation_page_id` varchar(36) NOT NULL,
  `region_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8A46C2D295B9BCA6` (`presentation_page_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_presentation_page_region`
--

LOCK TABLES `dw_presentation_page_region` WRITE;
/*!40000 ALTER TABLE `dw_presentation_page_region` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_presentation_page_region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_presentation_template`
--

DROP TABLE IF EXISTS `dw_presentation_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_presentation_template` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `includeHeaderAndFooter` tinyint(4) DEFAULT NULL,
  `published` tinyint(4) DEFAULT NULL,
  `owner_id` varchar(255) NOT NULL,
  `renderer` varchar(36) DEFAULT NULL,
  `propertyPage` varchar(36) DEFAULT NULL,
  `documentRoot` varchar(255) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `site_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_presentation_template`
--

LOCK TABLES `dw_presentation_template` WRITE;
/*!40000 ALTER TABLE `dw_presentation_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_presentation_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_resource`
--

DROP TABLE IF EXISTS `dw_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_resource` (
  `ID` varchar(36) NOT NULL,
  `URI` varchar(255) NOT NULL,
  `RESOURCE_TYPE` varchar(255) NOT NULL,
  `SUB_TYPE` varchar(255) DEFAULT NULL,
  `PRIMARY_MIME_TYPE` varchar(255) DEFAULT NULL,
  `SUB_MIME_TYPE` varchar(255) DEFAULT NULL,
  `PARENT_COLLECTION` varchar(255) DEFAULT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `MODIFIED_BY` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COPYRIGHT_CHOICE` varchar(255) DEFAULT NULL,
  `COPYRIGHT` varchar(255) DEFAULT NULL,
  `CONTENT_LENGTH` int(11) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `MODIFIED` datetime DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_RESORUCE_PARENT` (`PARENT_COLLECTION`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_resource`
--

LOCK TABLES `dw_resource` WRITE;
/*!40000 ALTER TABLE `dw_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_resource_collection`
--

DROP TABLE IF EXISTS `dw_resource_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_resource_collection` (
  `ID` varchar(36) NOT NULL,
  `URI` varchar(255) DEFAULT NULL,
  `PARENT_COLLECTION` varchar(255) DEFAULT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `MODIFIED_BY` varchar(255) DEFAULT NULL,
  `CREATED` datetime DEFAULT NULL,
  `MODIFIED` datetime DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `SPACE_USED` int(11) DEFAULT NULL,
  `SPACE_AVAILABLE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_COLLECTION_PARENT` (`PARENT_COLLECTION`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_resource_collection`
--

LOCK TABLES `dw_resource_collection` WRITE;
/*!40000 ALTER TABLE `dw_resource_collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_resource_collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_review_items`
--

DROP TABLE IF EXISTS `dw_review_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_review_items` (
  `id` varchar(36) NOT NULL,
  `review_content_id` varchar(36) DEFAULT NULL,
  `site_id` varchar(36) DEFAULT NULL,
  `parent_id` varchar(36) NOT NULL,
  `review_device_id` varchar(36) DEFAULT NULL,
  `review_type` int(11) DEFAULT NULL,
  `securityQualifier` varchar(255) DEFAULT NULL,
  `securityViewFunction` varchar(255) DEFAULT NULL,
  `securityEditFunction` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4240ED2786D510` (`parent_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_review_items`
--

LOCK TABLES `dw_review_items` WRITE;
/*!40000 ALTER TABLE `dw_review_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_review_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_scaffolding`
--

DROP TABLE IF EXISTS `dw_scaffolding`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_scaffolding` (
  `id` varchar(36) NOT NULL,
  `ownerId` varchar(36) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `worksiteId` varchar(255) DEFAULT NULL,
  `published` tinyint(4) DEFAULT NULL,
  `publishedBy` varchar(255) DEFAULT NULL,
  `publishedDate` datetime DEFAULT NULL,
  `columnLabel` varchar(255) DEFAULT NULL,
  `rowLabel` varchar(255) DEFAULT NULL,
  `readyColor` varchar(7) DEFAULT NULL,
  `pendingColor` varchar(7) DEFAULT NULL,
  `completedColor` varchar(7) DEFAULT NULL,
  `lockedColor` varchar(7) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_scaffolding`
--

LOCK TABLES `dw_scaffolding` WRITE;
/*!40000 ALTER TABLE `dw_scaffolding` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_scaffolding` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_scaffolding_cell`
--

DROP TABLE IF EXISTS `dw_scaffolding_cell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_scaffolding_cell` (
  `id` varchar(36) NOT NULL,
  `rootcriterion_id` varchar(36) DEFAULT NULL,
  `level_id` varchar(36) DEFAULT NULL,
  `scaffolding_id` varchar(36) NOT NULL,
  `wizardPageDef_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK184EAE6880F1F7B6` (`level_id`),
  KEY `FK184EAE68C604760E` (`scaffolding_id`),
  KEY `FK184EAE685827115B` (`rootcriterion_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_scaffolding_cell`
--

LOCK TABLES `dw_scaffolding_cell` WRITE;
/*!40000 ALTER TABLE `dw_scaffolding_cell` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_scaffolding_cell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_scaffolding_cell_evaluators`
--

DROP TABLE IF EXISTS `dw_scaffolding_cell_evaluators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_scaffolding_cell_evaluators` (
  `scaffolding_cell_id` varchar(36) NOT NULL,
  `id` varchar(255) DEFAULT NULL,
  KEY `FK184EAE6880F1F7B4` (`scaffolding_cell_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_scaffolding_cell_evaluators`
--

LOCK TABLES `dw_scaffolding_cell_evaluators` WRITE;
/*!40000 ALTER TABLE `dw_scaffolding_cell_evaluators` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_scaffolding_cell_evaluators` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_scaffolding_criteria`
--

DROP TABLE IF EXISTS `dw_scaffolding_criteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_scaffolding_criteria` (
  `id` varchar(36) NOT NULL,
  `scaffolding_id` varchar(36) DEFAULT NULL,
  `sequenceNumber` varchar(36) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `color` varchar(36) DEFAULT NULL,
  `textColor` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_scaffolding_criteria`
--

LOCK TABLES `dw_scaffolding_criteria` WRITE;
/*!40000 ALTER TABLE `dw_scaffolding_criteria` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_scaffolding_criteria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_scaffolding_levels`
--

DROP TABLE IF EXISTS `dw_scaffolding_levels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_scaffolding_levels` (
  `id` varchar(36) NOT NULL,
  `scaffolding_id` varchar(36) DEFAULT NULL,
  `sequenceNumber` varchar(36) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `color` varchar(36) DEFAULT NULL,
  `textColor` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4EBCD0F5C604760E` (`scaffolding_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_scaffolding_levels`
--

LOCK TABLES `dw_scaffolding_levels` WRITE;
/*!40000 ALTER TABLE `dw_scaffolding_levels` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_scaffolding_levels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_session`
--

DROP TABLE IF EXISTS `dw_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_session` (
  `id` varchar(99) DEFAULT NULL,
  `server` varchar(99) DEFAULT NULL,
  `userId` varchar(99) DEFAULT NULL,
  `ip` varchar(99) DEFAULT NULL,
  `user_agent` varchar(99) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `durationSeconds` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_session`
--

LOCK TABLES `dw_session` WRITE;
/*!40000 ALTER TABLE `dw_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_site_users`
--

DROP TABLE IF EXISTS `dw_site_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_site_users` (
  `site_id` varchar(99) NOT NULL,
  `user_id` varchar(99) NOT NULL,
  `user_eid` varchar(99) DEFAULT NULL,
  `user_display_id` varchar(99) DEFAULT NULL,
  `role` varchar(99) DEFAULT NULL,
  `permission` int(11) DEFAULT NULL,
  PRIMARY KEY (`site_id`,`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_site_users`
--

LOCK TABLES `dw_site_users` WRITE;
/*!40000 ALTER TABLE `dw_site_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_site_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_sites`
--

DROP TABLE IF EXISTS `dw_sites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_sites` (
  `id` varchar(99) NOT NULL,
  `type` varchar(99) DEFAULT NULL,
  `title` varchar(99) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_sites`
--

LOCK TABLES `dw_sites` WRITE;
/*!40000 ALTER TABLE `dw_sites` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_sites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_template_file_ref`
--

DROP TABLE IF EXISTS `dw_template_file_ref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_template_file_ref` (
  `id` varchar(36) NOT NULL,
  `file_id` varchar(36) DEFAULT NULL,
  `file_type_id` varchar(36) DEFAULT NULL,
  `usage_desc` varchar(255) DEFAULT NULL,
  `template_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4B70FB02697A9B00` (`template_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_template_file_ref`
--

LOCK TABLES `dw_template_file_ref` WRITE;
/*!40000 ALTER TABLE `dw_template_file_ref` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_template_file_ref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_users`
--

DROP TABLE IF EXISTS `dw_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_users` (
  `user_id` varchar(99) NOT NULL,
  `user_eid` varchar(99) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `email_lc` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_users`
--

LOCK TABLES `dw_users` WRITE;
/*!40000 ALTER TABLE `dw_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard`
--

DROP TABLE IF EXISTS `dw_wizard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard` (
  `id` varchar(36) NOT NULL,
  `owner_id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `keywords` text,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `site_id` varchar(99) NOT NULL,
  `guidance_id` varchar(36) DEFAULT NULL,
  `published` tinyint(4) DEFAULT NULL,
  `wizard_type` varchar(255) DEFAULT NULL,
  `exposed_page_id` varchar(36) DEFAULT NULL,
  `root_category` varchar(36) DEFAULT NULL,
  `seq_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard`
--

LOCK TABLES `dw_wizard` WRITE;
/*!40000 ALTER TABLE `dw_wizard` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard_category`
--

DROP TABLE IF EXISTS `dw_wizard_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard_category` (
  `id` varchar(36) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `wizard_id` varchar(36) DEFAULT NULL,
  `parent_category_id` varchar(36) DEFAULT NULL,
  `seq_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard_category`
--

LOCK TABLES `dw_wizard_category` WRITE;
/*!40000 ALTER TABLE `dw_wizard_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard_completed`
--

DROP TABLE IF EXISTS `dw_wizard_completed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard_completed` (
  `id` varchar(36) NOT NULL,
  `owner_id` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `lastVisited` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `wizard_id` varchar(36) NOT NULL,
  `root_category` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard_completed`
--

LOCK TABLES `dw_wizard_completed` WRITE;
/*!40000 ALTER TABLE `dw_wizard_completed` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard_completed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard_completed_category`
--

DROP TABLE IF EXISTS `dw_wizard_completed_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard_completed_category` (
  `id` varchar(36) NOT NULL,
  `completed_wizard_id` varchar(36) DEFAULT NULL,
  `category_id` varchar(36) NOT NULL,
  `parent_category_id` varchar(36) DEFAULT NULL,
  `expanded` int(11) DEFAULT NULL,
  `seq_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard_completed_category`
--

LOCK TABLES `dw_wizard_completed_category` WRITE;
/*!40000 ALTER TABLE `dw_wizard_completed_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard_completed_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard_completed_page`
--

DROP TABLE IF EXISTS `dw_wizard_completed_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard_completed_page` (
  `id` varchar(36) NOT NULL,
  `completed_category_id` varchar(36) NOT NULL,
  `wizard_page_def_id` varchar(36) NOT NULL,
  `wizard_page_id` varchar(36) NOT NULL,
  `seq_num` int(11) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `lastVisited` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard_completed_page`
--

LOCK TABLES `dw_wizard_completed_page` WRITE;
/*!40000 ALTER TABLE `dw_wizard_completed_page` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard_completed_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard_page`
--

DROP TABLE IF EXISTS `dw_wizard_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard_page` (
  `id` varchar(36) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `wizard_page_def_id` varchar(36) NOT NULL,
  `modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard_page`
--

LOCK TABLES `dw_wizard_page` WRITE;
/*!40000 ALTER TABLE `dw_wizard_page` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard_page_attachments`
--

DROP TABLE IF EXISTS `dw_wizard_page_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard_page_attachments` (
  `id` varchar(36) NOT NULL,
  `artifact_id` varchar(36) DEFAULT NULL,
  `wizard_page_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard_page_attachments`
--

LOCK TABLES `dw_wizard_page_attachments` WRITE;
/*!40000 ALTER TABLE `dw_wizard_page_attachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard_page_attachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard_page_def`
--

DROP TABLE IF EXISTS `dw_wizard_page_def`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard_page_def` (
  `id` varchar(36) NOT NULL,
  `initialStatus` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `site_id` varchar(36) DEFAULT NULL,
  `guidance_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard_page_def`
--

LOCK TABLES `dw_wizard_page_def` WRITE;
/*!40000 ALTER TABLE `dw_wizard_page_def` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard_page_def` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard_page_def_add_forms`
--

DROP TABLE IF EXISTS `dw_wizard_page_def_add_forms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard_page_def_add_forms` (
  `wizard_page_def_id` varchar(36) NOT NULL,
  `form_def_id` varchar(36) NOT NULL,
  `seq_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`wizard_page_def_id`,`form_def_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard_page_def_add_forms`
--

LOCK TABLES `dw_wizard_page_def_add_forms` WRITE;
/*!40000 ALTER TABLE `dw_wizard_page_def_add_forms` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard_page_def_add_forms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard_page_forms`
--

DROP TABLE IF EXISTS `dw_wizard_page_forms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard_page_forms` (
  `id` varchar(36) NOT NULL,
  `artifact_id` varchar(36) DEFAULT NULL,
  `wizard_page_id` varchar(36) NOT NULL,
  `form_type` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard_page_forms`
--

LOCK TABLES `dw_wizard_page_forms` WRITE;
/*!40000 ALTER TABLE `dw_wizard_page_forms` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard_page_forms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard_page_sequence`
--

DROP TABLE IF EXISTS `dw_wizard_page_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard_page_sequence` (
  `id` varchar(36) NOT NULL,
  `category_id` varchar(36) DEFAULT NULL,
  `wiz_page_def_id` varchar(36) DEFAULT NULL,
  `seq_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard_page_sequence`
--

LOCK TABLES `dw_wizard_page_sequence` WRITE;
/*!40000 ALTER TABLE `dw_wizard_page_sequence` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard_page_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard_style`
--

DROP TABLE IF EXISTS `dw_wizard_style`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard_style` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `globalState` int(11) DEFAULT NULL,
  `owner` varchar(255) NOT NULL,
  `styleFile` varchar(36) DEFAULT NULL,
  `siteId` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard_style`
--

LOCK TABLES `dw_wizard_style` WRITE;
/*!40000 ALTER TABLE `dw_wizard_style` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard_style` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_wizard_support_item`
--

DROP TABLE IF EXISTS `dw_wizard_support_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_wizard_support_item` (
  `id` varchar(36) NOT NULL,
  `wizard_id` varchar(36) NOT NULL,
  `item_id` varchar(36) NOT NULL,
  `generic_type` varchar(255) NOT NULL,
  `content_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_wizard_support_item`
--

LOCK TABLES `dw_wizard_support_item` WRITE;
/*!40000 ALTER TABLE `dw_wizard_support_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_wizard_support_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dw_workflow_parent`
--

DROP TABLE IF EXISTS `dw_workflow_parent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dw_workflow_parent` (
  `id` varchar(36) NOT NULL,
  `reflection_device_id` varchar(36) DEFAULT NULL,
  `reflection_device_type` varchar(255) DEFAULT NULL,
  `evaluation_device_id` varchar(36) DEFAULT NULL,
  `evaluation_device_type` varchar(255) DEFAULT NULL,
  `review_device_id` varchar(36) DEFAULT NULL,
  `review_device_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dw_workflow_parent`
--

LOCK TABLES `dw_workflow_parent` WRITE;
/*!40000 ALTER TABLE `dw_workflow_parent` DISABLE KEYS */;
/*!40000 ALTER TABLE `dw_workflow_parent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metaobj_form_def`
--

DROP TABLE IF EXISTS `metaobj_form_def`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metaobj_form_def` (
  `id` varchar(36) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `documentRoot` varchar(255) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `systemOnly` bit(1) NOT NULL,
  `externalType` varchar(255) NOT NULL,
  `siteId` varchar(255) DEFAULT NULL,
  `siteState` int(11) NOT NULL,
  `globalState` int(11) NOT NULL,
  `schemaData` longblob NOT NULL,
  `instruction` text,
  `schema_hash` varchar(255) DEFAULT NULL,
  `alternateCreateXslt` varchar(36) DEFAULT NULL,
  `alternateViewXslt` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metaobj_form_def`
--

LOCK TABLES `metaobj_form_def` WRITE;
/*!40000 ALTER TABLE `metaobj_form_def` DISABLE KEYS */;
/*!40000 ALTER TABLE `metaobj_form_def` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_authz_simple`
--

DROP TABLE IF EXISTS `osp_authz_simple`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_authz_simple` (
  `id` varchar(36) NOT NULL,
  `qualifier_id` varchar(255) NOT NULL,
  `agent_id` varchar(255) NOT NULL,
  `function_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_authz_simple`
--

LOCK TABLES `osp_authz_simple` WRITE;
/*!40000 ALTER TABLE `osp_authz_simple` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_authz_simple` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_completed_wiz_category`
--

DROP TABLE IF EXISTS `osp_completed_wiz_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_completed_wiz_category` (
  `id` varchar(36) NOT NULL,
  `completed_wizard_id` varchar(36) DEFAULT NULL,
  `category_id` varchar(36) DEFAULT NULL,
  `expanded` bit(1) DEFAULT NULL,
  `seq_num` int(11) DEFAULT NULL,
  `parent_category_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4EC54F7C21B27839` (`completed_wizard_id`),
  KEY `FK4EC54F7C6EA23D5D` (`category_id`),
  KEY `FK4EC54F7CF992DFC3` (`parent_category_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_completed_wiz_category`
--

LOCK TABLES `osp_completed_wiz_category` WRITE;
/*!40000 ALTER TABLE `osp_completed_wiz_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_completed_wiz_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_completed_wizard`
--

DROP TABLE IF EXISTS `osp_completed_wizard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_completed_wizard` (
  `id` varchar(36) NOT NULL,
  `owner_id` varchar(255) NOT NULL,
  `created` datetime NOT NULL,
  `lastVisited` datetime NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `wizard_id` varchar(36) DEFAULT NULL,
  `root_category` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `root_category` (`root_category`),
  KEY `FKABC9DEB2D62513B2` (`wizard_id`),
  KEY `FKABC9DEB2D4C797` (`root_category`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_completed_wizard`
--

LOCK TABLES `osp_completed_wizard` WRITE;
/*!40000 ALTER TABLE `osp_completed_wizard` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_completed_wizard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_completed_wizard_page`
--

DROP TABLE IF EXISTS `osp_completed_wizard_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_completed_wizard_page` (
  `id` varchar(36) NOT NULL,
  `completed_category_id` varchar(36) DEFAULT NULL,
  `wizard_page_def_id` varchar(36) DEFAULT NULL,
  `wizard_page_id` varchar(36) DEFAULT NULL,
  `seq_num` int(11) DEFAULT NULL,
  `created` datetime NOT NULL,
  `lastVisited` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `wizard_page_id` (`wizard_page_id`),
  KEY `FK52DE9BFC473463E4` (`completed_category_id`),
  KEY `FK52DE9BFCE4E7E6D3` (`wizard_page_id`),
  KEY `FK52DE9BFC2E24C4` (`wizard_page_def_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_completed_wizard_page`
--

LOCK TABLES `osp_completed_wizard_page` WRITE;
/*!40000 ALTER TABLE `osp_completed_wizard_page` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_completed_wizard_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_guidance`
--

DROP TABLE IF EXISTS `osp_guidance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_guidance` (
  `id` varchar(36) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `site_id` varchar(99) NOT NULL,
  `securityQualifier` varchar(255) DEFAULT NULL,
  `securityViewFunction` varchar(255) NOT NULL,
  `securityEditFunction` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_guidance`
--

LOCK TABLES `osp_guidance` WRITE;
/*!40000 ALTER TABLE `osp_guidance` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_guidance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_guidance_item`
--

DROP TABLE IF EXISTS `osp_guidance_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_guidance_item` (
  `id` varchar(36) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `text` text,
  `guidance_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK605DDBA737209105` (`guidance_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_guidance_item`
--

LOCK TABLES `osp_guidance_item` WRITE;
/*!40000 ALTER TABLE `osp_guidance_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_guidance_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_guidance_item_file`
--

DROP TABLE IF EXISTS `osp_guidance_item_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_guidance_item_file` (
  `id` varchar(36) NOT NULL,
  `baseReference` varchar(255) DEFAULT NULL,
  `fullReference` varchar(255) DEFAULT NULL,
  `item_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK29770314DB93091D` (`item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_guidance_item_file`
--

LOCK TABLES `osp_guidance_item_file` WRITE;
/*!40000 ALTER TABLE `osp_guidance_item_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_guidance_item_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_help_glossary`
--

DROP TABLE IF EXISTS `osp_help_glossary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_help_glossary` (
  `id` varchar(36) NOT NULL,
  `worksite_id` varchar(255) DEFAULT NULL,
  `term` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_help_glossary`
--

LOCK TABLES `osp_help_glossary` WRITE;
/*!40000 ALTER TABLE `osp_help_glossary` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_help_glossary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_help_glossary_desc`
--

DROP TABLE IF EXISTS `osp_help_glossary_desc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_help_glossary_desc` (
  `id` varchar(36) NOT NULL,
  `entry_id` varchar(255) DEFAULT NULL,
  `long_description` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_help_glossary_desc`
--

LOCK TABLES `osp_help_glossary_desc` WRITE;
/*!40000 ALTER TABLE `osp_help_glossary_desc` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_help_glossary_desc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_list_config`
--

DROP TABLE IF EXISTS `osp_list_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_list_config` (
  `id` varchar(36) NOT NULL,
  `owner_id` varchar(255) NOT NULL,
  `tool_id` varchar(36) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `numRows` int(11) DEFAULT NULL,
  `selected_columns` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_list_config`
--

LOCK TABLES `osp_list_config` WRITE;
/*!40000 ALTER TABLE `osp_list_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_list_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_matrix`
--

DROP TABLE IF EXISTS `osp_matrix`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_matrix` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `scaffolding_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5A172054A6286438` (`scaffolding_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_matrix`
--

LOCK TABLES `osp_matrix` WRITE;
/*!40000 ALTER TABLE `osp_matrix` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_matrix` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_matrix_cell`
--

DROP TABLE IF EXISTS `osp_matrix_cell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_matrix_cell` (
  `id` varchar(36) NOT NULL,
  `matrix_id` varchar(36) NOT NULL,
  `wizard_page_id` varchar(36) DEFAULT NULL,
  `scaffolding_cell_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `wizard_page_id` (`wizard_page_id`),
  KEY `FK8C1D366DCD99D2B1` (`scaffolding_cell_id`),
  KEY `FK8C1D366DE4E7E6D3` (`wizard_page_id`),
  KEY `FK8C1D366D2D955C` (`matrix_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_matrix_cell`
--

LOCK TABLES `osp_matrix_cell` WRITE;
/*!40000 ALTER TABLE `osp_matrix_cell` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_matrix_cell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_matrix_label`
--

DROP TABLE IF EXISTS `osp_matrix_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_matrix_label` (
  `id` varchar(36) NOT NULL,
  `type` char(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `color` varchar(7) DEFAULT NULL,
  `textColor` varchar(7) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_matrix_label`
--

LOCK TABLES `osp_matrix_label` WRITE;
/*!40000 ALTER TABLE `osp_matrix_label` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_matrix_label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_portal_category_pages`
--

DROP TABLE IF EXISTS `osp_portal_category_pages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_portal_category_pages` (
  `category_id` varchar(36) NOT NULL,
  `page` longblob,
  `page_locale` varchar(255) NOT NULL,
  PRIMARY KEY (`category_id`,`page_locale`),
  KEY `FK5453883DA502DE9` (`category_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_portal_category_pages`
--

LOCK TABLES `osp_portal_category_pages` WRITE;
/*!40000 ALTER TABLE `osp_portal_category_pages` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_portal_category_pages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_portal_site_type`
--

DROP TABLE IF EXISTS `osp_portal_site_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_portal_site_type` (
  `id` varchar(36) NOT NULL,
  `type_key` varchar(255) NOT NULL,
  `type_name` varchar(255) DEFAULT NULL,
  `type_description` varchar(255) DEFAULT NULL,
  `skin` varchar(255) DEFAULT NULL,
  `firstCategory` int(11) NOT NULL,
  `lastCategory` int(11) NOT NULL,
  `type_order` int(11) NOT NULL,
  `hidden` bit(1) DEFAULT NULL,
  `display_tab` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_key` (`type_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_portal_site_type`
--

LOCK TABLES `osp_portal_site_type` WRITE;
/*!40000 ALTER TABLE `osp_portal_site_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_portal_site_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_portal_special_sites`
--

DROP TABLE IF EXISTS `osp_portal_special_sites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_portal_special_sites` (
  `site_type_id` varchar(36) NOT NULL,
  `site_type` varchar(255) DEFAULT NULL,
  `site_index` int(11) NOT NULL,
  PRIMARY KEY (`site_type_id`,`site_index`),
  KEY `FKCB5737C68CA05920` (`site_type_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_portal_special_sites`
--

LOCK TABLES `osp_portal_special_sites` WRITE;
/*!40000 ALTER TABLE `osp_portal_special_sites` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_portal_special_sites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_portal_tool_category`
--

DROP TABLE IF EXISTS `osp_portal_tool_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_portal_tool_category` (
  `id` varchar(36) NOT NULL,
  `type_key` varchar(255) NOT NULL,
  `type_description` varchar(255) DEFAULT NULL,
  `category_order` int(11) DEFAULT NULL,
  `home_page_path` varchar(255) DEFAULT NULL,
  `site_type_id` varchar(36) DEFAULT NULL,
  `category_index` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_key` (`type_key`),
  KEY `FKFBED88858CA05920` (`site_type_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_portal_tool_category`
--

LOCK TABLES `osp_portal_tool_category` WRITE;
/*!40000 ALTER TABLE `osp_portal_tool_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_portal_tool_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_portal_tool_functions`
--

DROP TABLE IF EXISTS `osp_portal_tool_functions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_portal_tool_functions` (
  `tool_type_id` varchar(36) NOT NULL,
  `function_name` varchar(255) DEFAULT NULL,
  `function_index` int(11) NOT NULL,
  PRIMARY KEY (`tool_type_id`,`function_index`),
  KEY `FK1C0DACF4F73EFC42` (`tool_type_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_portal_tool_functions`
--

LOCK TABLES `osp_portal_tool_functions` WRITE;
/*!40000 ALTER TABLE `osp_portal_tool_functions` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_portal_tool_functions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_portal_tool_type`
--

DROP TABLE IF EXISTS `osp_portal_tool_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_portal_tool_type` (
  `id` varchar(36) NOT NULL,
  `qualifier_type` varchar(255) DEFAULT NULL,
  `category_id` varchar(36) DEFAULT NULL,
  `tool_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK58EA3641DA502DE9` (`category_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_portal_tool_type`
--

LOCK TABLES `osp_portal_tool_type` WRITE;
/*!40000 ALTER TABLE `osp_portal_tool_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_portal_tool_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_pres_itemdef_mimetype`
--

DROP TABLE IF EXISTS `osp_pres_itemdef_mimetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_pres_itemdef_mimetype` (
  `item_def_id` varchar(36) NOT NULL,
  `primaryMimeType` varchar(36) DEFAULT NULL,
  `secondaryMimeType` varchar(36) DEFAULT NULL,
  KEY `FK9EA59837650346CA` (`item_def_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_pres_itemdef_mimetype`
--

LOCK TABLES `osp_pres_itemdef_mimetype` WRITE;
/*!40000 ALTER TABLE `osp_pres_itemdef_mimetype` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_pres_itemdef_mimetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_presentation`
--

DROP TABLE IF EXISTS `osp_presentation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_presentation` (
  `id` varchar(36) NOT NULL,
  `owner_id` varchar(255) NOT NULL,
  `template_id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `isDefault` bit(1) DEFAULT NULL,
  `isPublic` bit(1) DEFAULT NULL,
  `isCollab` bit(1) DEFAULT NULL,
  `presentationType` varchar(255) NOT NULL,
  `expiresOn` datetime DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `allowComments` bit(1) DEFAULT NULL,
  `site_id` varchar(99) NOT NULL,
  `properties` blob,
  `property_form` varchar(36) DEFAULT NULL,
  `layout_id` varchar(36) DEFAULT NULL,
  `style_id` varchar(36) DEFAULT NULL,
  `advanced_navigation` bit(1) DEFAULT NULL,
  `tool_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA9028D6DFAEA67E8` (`style_id`),
  KEY `FKA9028D6D533F283D` (`layout_id`),
  KEY `FKA9028D6D6FE1417D` (`template_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_presentation`
--

LOCK TABLES `osp_presentation` WRITE;
/*!40000 ALTER TABLE `osp_presentation` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_presentation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_presentation_comment`
--

DROP TABLE IF EXISTS `osp_presentation_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_presentation_comment` (
  `id` varchar(36) NOT NULL,
  `title` varchar(255) NOT NULL,
  `commentText` text,
  `creator_id` varchar(255) NOT NULL,
  `presentation_id` varchar(36) NOT NULL,
  `visibility` tinyint(4) NOT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1E7E658D7658ED43` (`presentation_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_presentation_comment`
--

LOCK TABLES `osp_presentation_comment` WRITE;
/*!40000 ALTER TABLE `osp_presentation_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_presentation_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_presentation_item`
--

DROP TABLE IF EXISTS `osp_presentation_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_presentation_item` (
  `presentation_id` varchar(36) NOT NULL,
  `artifact_id` varchar(36) NOT NULL,
  `item_definition_id` varchar(36) NOT NULL,
  PRIMARY KEY (`presentation_id`,`artifact_id`,`item_definition_id`),
  KEY `FK2FA02A59165E3E4` (`item_definition_id`),
  KEY `FK2FA02A57658ED43` (`presentation_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_presentation_item`
--

LOCK TABLES `osp_presentation_item` WRITE;
/*!40000 ALTER TABLE `osp_presentation_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_presentation_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_presentation_item_def`
--

DROP TABLE IF EXISTS `osp_presentation_item_def`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_presentation_item_def` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `allowMultiple` bit(1) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `external_type` varchar(255) DEFAULT NULL,
  `sequence_no` int(11) DEFAULT NULL,
  `template_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1B6ADB6B6FE1417D` (`template_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_presentation_item_def`
--

LOCK TABLES `osp_presentation_item_def` WRITE;
/*!40000 ALTER TABLE `osp_presentation_item_def` DISABLE KEYS */;
INSERT INTO `osp_presentation_item_def` VALUES ('C59A1B780019E596F6BA3971535F51EF','freeFormItem',NULL,NULL,'',NULL,NULL,0,'freeFormTemplate');
/*!40000 ALTER TABLE `osp_presentation_item_def` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_presentation_item_property`
--

DROP TABLE IF EXISTS `osp_presentation_item_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_presentation_item_property` (
  `id` varchar(36) NOT NULL,
  `presentation_page_item_id` varchar(36) NOT NULL,
  `property_key` varchar(255) NOT NULL,
  `property_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK86B1362FA9B15561` (`presentation_page_item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_presentation_item_property`
--

LOCK TABLES `osp_presentation_item_property` WRITE;
/*!40000 ALTER TABLE `osp_presentation_item_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_presentation_item_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_presentation_layout`
--

DROP TABLE IF EXISTS `osp_presentation_layout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_presentation_layout` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `globalState` int(11) NOT NULL,
  `owner_id` varchar(255) NOT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `xhtml_file_id` varchar(36) NOT NULL,
  `preview_image_id` varchar(36) DEFAULT NULL,
  `tool_id` varchar(36) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_presentation_layout`
--

LOCK TABLES `osp_presentation_layout` WRITE;
/*!40000 ALTER TABLE `osp_presentation_layout` DISABLE KEYS */;
INSERT INTO `osp_presentation_layout` VALUES ('contentOverText','Content Over Text','Content Over Text Layout File',2,'admin','2011-04-13 14:59:39','2011-04-13 15:07:03','f25eb3d7-5900-4f03-9a4f-09d53ab65dd5','a483d33b-6b6b-4c7c-b69e-901065cf247a',NULL,NULL),('twoColumn','Two Column','Two Column Layout File',2,'admin','2011-04-13 14:59:40','2011-04-13 15:07:03','cc793aa9-71ea-4d45-be38-b414b4aa33ac','badfb3c8-f143-47fe-9192-7a53bb463495',NULL,NULL),('simpleRichText','Simple HTML','Single HTML Layout File',2,'admin','2011-04-13 14:59:40','2011-04-13 15:07:03','b0436581-e11f-4c19-a004-cd8775baad17','9f4061a9-ea10-4121-8bd2-ed9e8a465156',NULL,NULL);
/*!40000 ALTER TABLE `osp_presentation_layout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_presentation_log`
--

DROP TABLE IF EXISTS `osp_presentation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_presentation_log` (
  `id` varchar(36) NOT NULL,
  `viewer_id` varchar(255) NOT NULL,
  `presentation_id` varchar(36) NOT NULL,
  `view_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2120E1727658ED43` (`presentation_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_presentation_log`
--

LOCK TABLES `osp_presentation_log` WRITE;
/*!40000 ALTER TABLE `osp_presentation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_presentation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_presentation_page`
--

DROP TABLE IF EXISTS `osp_presentation_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_presentation_page` (
  `id` varchar(36) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `presentation_id` varchar(36) NOT NULL,
  `layout_id` varchar(36) NOT NULL,
  `style_id` varchar(36) DEFAULT NULL,
  `seq_num` int(11) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2FCEA21FAEA67E8` (`style_id`),
  KEY `FK2FCEA21533F283D` (`layout_id`),
  KEY `FK2FCEA217658ED43` (`presentation_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_presentation_page`
--

LOCK TABLES `osp_presentation_page` WRITE;
/*!40000 ALTER TABLE `osp_presentation_page` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_presentation_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_presentation_page_item`
--

DROP TABLE IF EXISTS `osp_presentation_page_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_presentation_page_item` (
  `id` varchar(36) NOT NULL,
  `presentation_page_region_id` varchar(36) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `value` longtext,
  `seq_num` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6417671954DB801` (`presentation_page_region_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_presentation_page_item`
--

LOCK TABLES `osp_presentation_page_item` WRITE;
/*!40000 ALTER TABLE `osp_presentation_page_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_presentation_page_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_presentation_page_region`
--

DROP TABLE IF EXISTS `osp_presentation_page_region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_presentation_page_region` (
  `id` varchar(36) NOT NULL,
  `presentation_page_id` varchar(36) NOT NULL,
  `region_id` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `help_text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8A46C2D215C572B8` (`presentation_page_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_presentation_page_region`
--

LOCK TABLES `osp_presentation_page_region` WRITE;
/*!40000 ALTER TABLE `osp_presentation_page_region` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_presentation_page_region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_presentation_template`
--

DROP TABLE IF EXISTS `osp_presentation_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_presentation_template` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `includeHeaderAndFooter` bit(1) DEFAULT NULL,
  `published` bit(1) DEFAULT NULL,
  `owner_id` varchar(255) NOT NULL,
  `renderer` varchar(36) DEFAULT NULL,
  `markup` text,
  `propertyPage` varchar(36) DEFAULT NULL,
  `propertyFormType` varchar(36) DEFAULT NULL,
  `documentRoot` varchar(255) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `site_id` varchar(99) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_presentation_template`
--

LOCK TABLES `osp_presentation_template` WRITE;
/*!40000 ALTER TABLE `osp_presentation_template` DISABLE KEYS */;
INSERT INTO `osp_presentation_template` VALUES ('freeFormTemplate','Free Form Presentation',NULL,'\0','\0','anonymous','2e48c42d-f61c-44fe-bdb8-870e84e0a5d7',NULL,NULL,NULL,NULL,'2011-04-13 14:59:39','2011-04-13 15:07:02','5850C29A015B67C7F7253D1EF0433D73');
/*!40000 ALTER TABLE `osp_presentation_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_review`
--

DROP TABLE IF EXISTS `osp_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_review` (
  `id` varchar(36) NOT NULL,
  `review_content_id` varchar(36) DEFAULT NULL,
  `site_id` varchar(99) NOT NULL,
  `parent_id` varchar(36) DEFAULT NULL,
  `review_device_id` varchar(36) DEFAULT NULL,
  `review_item_id` varchar(36) DEFAULT NULL,
  `review_type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_review`
--

LOCK TABLES `osp_review` WRITE;
/*!40000 ALTER TABLE `osp_review` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_scaffolding`
--

DROP TABLE IF EXISTS `osp_scaffolding`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_scaffolding` (
  `id` varchar(36) NOT NULL,
  `ownerId` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` text,
  `worksiteId` varchar(255) DEFAULT NULL,
  `generalFeedbackOption` int(11) NOT NULL,
  `itemFeedbackOption` int(11) NOT NULL,
  `preview` bit(1) NOT NULL,
  `published` bit(1) DEFAULT NULL,
  `publishedBy` varchar(255) DEFAULT NULL,
  `publishedDate` datetime DEFAULT NULL,
  `modifiedDate` datetime DEFAULT NULL,
  `columnLabel` varchar(255) NOT NULL,
  `rowLabel` varchar(255) NOT NULL,
  `readyColor` varchar(7) DEFAULT NULL,
  `pendingColor` varchar(7) DEFAULT NULL,
  `completedColor` varchar(7) DEFAULT NULL,
  `lockedColor` varchar(7) DEFAULT NULL,
  `returnedColor` varchar(7) DEFAULT NULL,
  `workflowOption` int(11) NOT NULL,
  `allowRequestFeedback` bit(1) DEFAULT NULL,
  `hideEvaluations` bit(1) DEFAULT NULL,
  `defaultFormsMatrixVersion` bit(1) DEFAULT NULL,
  `exposed_page_id` varchar(36) DEFAULT NULL,
  `style_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK65135779FAEA67E8` (`style_id`),
  KEY `FK65135779C73F84BD` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_scaffolding`
--

LOCK TABLES `osp_scaffolding` WRITE;
/*!40000 ALTER TABLE `osp_scaffolding` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_scaffolding` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_scaffolding_attachments`
--

DROP TABLE IF EXISTS `osp_scaffolding_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_scaffolding_attachments` (
  `id` varchar(36) NOT NULL,
  `artifact_id` varchar(255) DEFAULT NULL,
  `seq_num` int(11) NOT NULL,
  PRIMARY KEY (`id`,`seq_num`),
  KEY `FK529713EAE023FB45` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_scaffolding_attachments`
--

LOCK TABLES `osp_scaffolding_attachments` WRITE;
/*!40000 ALTER TABLE `osp_scaffolding_attachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_scaffolding_attachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_scaffolding_cell`
--

DROP TABLE IF EXISTS `osp_scaffolding_cell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_scaffolding_cell` (
  `id` varchar(36) NOT NULL,
  `rootcriterion_id` varchar(36) DEFAULT NULL,
  `level_id` varchar(36) DEFAULT NULL,
  `scaffolding_id` varchar(36) NOT NULL,
  `wiz_page_def_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `wiz_page_def_id` (`wiz_page_def_id`),
  KEY `FK184EAE68754F20BD` (`wiz_page_def_id`),
  KEY `FK184EAE689FECDBB8` (`level_id`),
  KEY `FK184EAE68A6286438` (`scaffolding_id`),
  KEY `FK184EAE6870EDF97A` (`rootcriterion_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_scaffolding_cell`
--

LOCK TABLES `osp_scaffolding_cell` WRITE;
/*!40000 ALTER TABLE `osp_scaffolding_cell` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_scaffolding_cell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_scaffolding_cell_form_defs`
--

DROP TABLE IF EXISTS `osp_scaffolding_cell_form_defs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_scaffolding_cell_form_defs` (
  `wiz_page_def_id` varchar(36) NOT NULL,
  `form_def_id` varchar(255) DEFAULT NULL,
  `seq_num` int(11) NOT NULL,
  PRIMARY KEY (`wiz_page_def_id`,`seq_num`),
  KEY `FK904DCA92754F20BD` (`wiz_page_def_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_scaffolding_cell_form_defs`
--

LOCK TABLES `osp_scaffolding_cell_form_defs` WRITE;
/*!40000 ALTER TABLE `osp_scaffolding_cell_form_defs` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_scaffolding_cell_form_defs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_scaffolding_criteria`
--

DROP TABLE IF EXISTS `osp_scaffolding_criteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_scaffolding_criteria` (
  `scaffolding_id` varchar(36) NOT NULL,
  `elt` varchar(36) NOT NULL,
  `seq_num` int(11) NOT NULL,
  PRIMARY KEY (`scaffolding_id`,`seq_num`),
  KEY `FK8634116518C870CC` (`elt`),
  KEY `FK86341165A6286438` (`scaffolding_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_scaffolding_criteria`
--

LOCK TABLES `osp_scaffolding_criteria` WRITE;
/*!40000 ALTER TABLE `osp_scaffolding_criteria` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_scaffolding_criteria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_scaffolding_form_defs`
--

DROP TABLE IF EXISTS `osp_scaffolding_form_defs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_scaffolding_form_defs` (
  `id` varchar(36) NOT NULL,
  `form_def_id` varchar(255) DEFAULT NULL,
  `seq_num` int(11) NOT NULL,
  PRIMARY KEY (`id`,`seq_num`),
  KEY `FK95431263E023FB45` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_scaffolding_form_defs`
--

LOCK TABLES `osp_scaffolding_form_defs` WRITE;
/*!40000 ALTER TABLE `osp_scaffolding_form_defs` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_scaffolding_form_defs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_scaffolding_levels`
--

DROP TABLE IF EXISTS `osp_scaffolding_levels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_scaffolding_levels` (
  `scaffolding_id` varchar(36) NOT NULL,
  `elt` varchar(36) NOT NULL,
  `seq_num` int(11) NOT NULL,
  PRIMARY KEY (`scaffolding_id`,`seq_num`),
  KEY `FK4EBCD0F51EFC6CAF` (`elt`),
  KEY `FK4EBCD0F5A6286438` (`scaffolding_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_scaffolding_levels`
--

LOCK TABLES `osp_scaffolding_levels` WRITE;
/*!40000 ALTER TABLE `osp_scaffolding_levels` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_scaffolding_levels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_site_tool`
--

DROP TABLE IF EXISTS `osp_site_tool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_site_tool` (
  `id` varchar(40) NOT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  `tool_id` varchar(36) DEFAULT NULL,
  `listener_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_site_tool`
--

LOCK TABLES `osp_site_tool` WRITE;
/*!40000 ALTER TABLE `osp_site_tool` DISABLE KEYS */;
INSERT INTO `osp_site_tool` VALUES ('4AAEE6A8613F98F8B2F3261E893E677C','PortfolioAdmin','05b43286-2d15-42a0-b495-23511cb84a11','org.theospi.portfolio.security.mgt.ToolPermissionManager.presentation'),('3A6560707ED052F451E45D5A6C8D050E','PortfolioAdmin','12860648-d921-4bdf-91a5-9fae8e76334b','org.theospi.portfolio.security.mgt.ToolPermissionManager.presentationTemplate'),('1F8276602FFC201687968322169E20F6','PortfolioAdmin','b0b38507-4604-44e0-b690-cae897aefa8b','org.theospi.portfolio.security.mgt.ToolPermissionManager.presentationLayout'),('0C57E55614AFAA3807204ACB60EC6055','PortfolioAdmin','09bbec81-8322-4423-8b65-acc636ed2105','org.theospi.portfolio.security.mgt.ToolPermissionManager.style'),('8BBE736B47DABEA70A08254F7EF741BD','PortfolioAdmin','e99c47e7-6b7d-4409-a4a9-32871d8b1579','org.theospi.portfolio.security.mgt.ToolPermissionManager.glossaryGlobal');
/*!40000 ALTER TABLE `osp_site_tool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_style`
--

DROP TABLE IF EXISTS `osp_style`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_style` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `globalState` int(11) NOT NULL,
  `owner_id` varchar(255) NOT NULL,
  `style_file_id` varchar(36) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `style_hash` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_style`
--

LOCK TABLES `osp_style` WRITE;
/*!40000 ALTER TABLE `osp_style` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_style` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_template_file_ref`
--

DROP TABLE IF EXISTS `osp_template_file_ref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_template_file_ref` (
  `id` varchar(36) NOT NULL,
  `file_id` varchar(36) DEFAULT NULL,
  `file_type_id` varchar(36) DEFAULT NULL,
  `usage_desc` varchar(255) DEFAULT NULL,
  `template_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4B70FB026FE1417D` (`template_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_template_file_ref`
--

LOCK TABLES `osp_template_file_ref` WRITE;
/*!40000 ALTER TABLE `osp_template_file_ref` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_template_file_ref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_wiz_page_attachment`
--

DROP TABLE IF EXISTS `osp_wiz_page_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_wiz_page_attachment` (
  `id` varchar(36) NOT NULL,
  `artifactId` varchar(36) DEFAULT NULL,
  `page_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2257FCC9BDC195A7` (`page_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_wiz_page_attachment`
--

LOCK TABLES `osp_wiz_page_attachment` WRITE;
/*!40000 ALTER TABLE `osp_wiz_page_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_wiz_page_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_wiz_page_def_attachments`
--

DROP TABLE IF EXISTS `osp_wiz_page_def_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_wiz_page_def_attachments` (
  `wiz_page_def_id` varchar(36) NOT NULL,
  `artifact_id` varchar(255) DEFAULT NULL,
  `seq_num` int(11) NOT NULL,
  PRIMARY KEY (`wiz_page_def_id`,`seq_num`),
  KEY `FK331C8290754F20BD` (`wiz_page_def_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_wiz_page_def_attachments`
--

LOCK TABLES `osp_wiz_page_def_attachments` WRITE;
/*!40000 ALTER TABLE `osp_wiz_page_def_attachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_wiz_page_def_attachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_wiz_page_form`
--

DROP TABLE IF EXISTS `osp_wiz_page_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_wiz_page_form` (
  `id` varchar(36) NOT NULL,
  `artifactId` varchar(36) DEFAULT NULL,
  `page_id` varchar(36) NOT NULL,
  `formType` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4725E4EABDC195A7` (`page_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_wiz_page_form`
--

LOCK TABLES `osp_wiz_page_form` WRITE;
/*!40000 ALTER TABLE `osp_wiz_page_form` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_wiz_page_form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_wizard`
--

DROP TABLE IF EXISTS `osp_wizard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_wizard` (
  `id` varchar(36) NOT NULL,
  `owner_id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `keywords` text,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `site_id` varchar(99) NOT NULL,
  `guidance_id` varchar(36) DEFAULT NULL,
  `published` bit(1) DEFAULT NULL,
  `preview` bit(1) DEFAULT NULL,
  `wizard_type` varchar(255) DEFAULT NULL,
  `style_id` varchar(36) DEFAULT NULL,
  `exposed_page_id` varchar(36) DEFAULT NULL,
  `root_category` varchar(36) DEFAULT NULL,
  `seq_num` int(11) DEFAULT NULL,
  `generalFeedbackOption` int(11) NOT NULL,
  `itemFeedbackOption` int(11) NOT NULL,
  `reviewerGroupAccess` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `root_category` (`root_category`),
  KEY `FK6B9ACDFEFAEA67E8` (`style_id`),
  KEY `FK6B9ACDFEC73F84BD` (`id`),
  KEY `FK6B9ACDFEE831DD1C` (`root_category`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_wizard`
--

LOCK TABLES `osp_wizard` WRITE;
/*!40000 ALTER TABLE `osp_wizard` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_wizard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_wizard_category`
--

DROP TABLE IF EXISTS `osp_wizard_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_wizard_category` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `wizard_id` varchar(36) DEFAULT NULL,
  `parent_category_id` varchar(36) DEFAULT NULL,
  `seq_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3A81FE1FD62513B2` (`wizard_id`),
  KEY `FK3A81FE1FE0EFF548` (`parent_category_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_wizard_category`
--

LOCK TABLES `osp_wizard_category` WRITE;
/*!40000 ALTER TABLE `osp_wizard_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_wizard_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_wizard_page`
--

DROP TABLE IF EXISTS `osp_wizard_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_wizard_page` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `wiz_page_def_id` varchar(36) DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4CFB5C30754F20BD` (`wiz_page_def_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_wizard_page`
--

LOCK TABLES `osp_wizard_page` WRITE;
/*!40000 ALTER TABLE `osp_wizard_page` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_wizard_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_wizard_page_def`
--

DROP TABLE IF EXISTS `osp_wizard_page_def`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_wizard_page_def` (
  `id` varchar(36) NOT NULL,
  `initialStatus` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `SUPPRESS_ITEMS` bit(1) DEFAULT NULL,
  `site_id` varchar(255) DEFAULT NULL,
  `guidance_id` varchar(255) DEFAULT NULL,
  `style_id` varchar(36) DEFAULT NULL,
  `defaultCustomForm` bit(1) DEFAULT NULL,
  `defaultReflectionForm` bit(1) DEFAULT NULL,
  `defaultFeedbackForm` bit(1) DEFAULT NULL,
  `defaultReviewers` bit(1) DEFAULT NULL,
  `defaultEvaluationForm` bit(1) DEFAULT NULL,
  `defaultEvaluators` bit(1) DEFAULT NULL,
  `allowRequestFeedback` bit(1) DEFAULT NULL,
  `hideEvaluations` bit(1) DEFAULT NULL,
  `type` varchar(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6ABE7776FAEA67E8` (`style_id`),
  KEY `FK6ABE7776C73F84BD` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_wizard_page_def`
--

LOCK TABLES `osp_wizard_page_def` WRITE;
/*!40000 ALTER TABLE `osp_wizard_page_def` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_wizard_page_def` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_wizard_page_sequence`
--

DROP TABLE IF EXISTS `osp_wizard_page_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_wizard_page_sequence` (
  `id` varchar(36) NOT NULL,
  `seq_num` int(11) DEFAULT NULL,
  `category_id` varchar(36) NOT NULL,
  `wiz_page_def_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `wiz_page_def_id` (`wiz_page_def_id`),
  KEY `FKA5A702F0754F20BD` (`wiz_page_def_id`),
  KEY `FKA5A702F06EA23D5D` (`category_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_wizard_page_sequence`
--

LOCK TABLES `osp_wizard_page_sequence` WRITE;
/*!40000 ALTER TABLE `osp_wizard_page_sequence` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_wizard_page_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_workflow`
--

DROP TABLE IF EXISTS `osp_workflow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_workflow` (
  `id` varchar(36) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `parent_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2065879242A62872` (`parent_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_workflow`
--

LOCK TABLES `osp_workflow` WRITE;
/*!40000 ALTER TABLE `osp_workflow` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_workflow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_workflow_item`
--

DROP TABLE IF EXISTS `osp_workflow_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_workflow_item` (
  `id` varchar(36) NOT NULL,
  `actionType` int(11) NOT NULL,
  `action_object_id` varchar(255) NOT NULL,
  `action_value` varchar(255) NOT NULL,
  `workflow_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB38697A091A4BC5E` (`workflow_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_workflow_item`
--

LOCK TABLES `osp_workflow_item` WRITE;
/*!40000 ALTER TABLE `osp_workflow_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_workflow_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osp_workflow_parent`
--

DROP TABLE IF EXISTS `osp_workflow_parent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osp_workflow_parent` (
  `id` varchar(36) NOT NULL,
  `reflection_device_id` varchar(36) DEFAULT NULL,
  `reflection_device_type` varchar(255) DEFAULT NULL,
  `evaluation_device_id` varchar(36) DEFAULT NULL,
  `evaluation_device_type` varchar(255) DEFAULT NULL,
  `review_device_id` varchar(36) DEFAULT NULL,
  `review_device_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osp_workflow_parent`
--

LOCK TABLES `osp_workflow_parent` WRITE;
/*!40000 ALTER TABLE `osp_workflow_parent` DISABLE KEYS */;
/*!40000 ALTER TABLE `osp_workflow_parent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rwikicurrentcontent`
--

DROP TABLE IF EXISTS `rwikicurrentcontent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rwikicurrentcontent` (
  `id` varchar(36) NOT NULL,
  `rwikiid` varchar(36) NOT NULL,
  `content` mediumtext,
  PRIMARY KEY (`id`),
  KEY `irwikicurrentcontent_rwi` (`rwikiid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rwikicurrentcontent`
--

LOCK TABLES `rwikicurrentcontent` WRITE;
/*!40000 ALTER TABLE `rwikicurrentcontent` DISABLE KEYS */;
/*!40000 ALTER TABLE `rwikicurrentcontent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rwikihistory`
--

DROP TABLE IF EXISTS `rwikihistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rwikihistory` (
  `id` varchar(36) NOT NULL,
  `version` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `realm` varchar(255) DEFAULT NULL,
  `referenced` text,
  `userid` varchar(64) DEFAULT NULL,
  `owner` varchar(64) DEFAULT NULL,
  `ownerRead` bit(1) DEFAULT NULL,
  `ownerWrite` bit(1) DEFAULT NULL,
  `ownerAdmin` bit(1) DEFAULT NULL,
  `groupRead` bit(1) DEFAULT NULL,
  `groupWrite` bit(1) DEFAULT NULL,
  `groupAdmin` bit(1) DEFAULT NULL,
  `publicRead` bit(1) DEFAULT NULL,
  `publicWrite` bit(1) DEFAULT NULL,
  `sha1` varchar(64) DEFAULT NULL,
  `revision` int(11) DEFAULT NULL,
  `rwikiobjectid` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `name_idx` (`name`),
  KEY `rwikiobjectid_idx` (`rwikiobjectid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rwikihistory`
--

LOCK TABLES `rwikihistory` WRITE;
/*!40000 ALTER TABLE `rwikihistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `rwikihistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rwikihistorycontent`
--

DROP TABLE IF EXISTS `rwikihistorycontent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rwikihistorycontent` (
  `id` varchar(36) NOT NULL,
  `rwikiid` varchar(36) NOT NULL,
  `content` mediumtext,
  PRIMARY KEY (`id`),
  KEY `irwikihistorycontent_rwi` (`rwikiid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rwikihistorycontent`
--

LOCK TABLES `rwikihistorycontent` WRITE;
/*!40000 ALTER TABLE `rwikihistorycontent` DISABLE KEYS */;
/*!40000 ALTER TABLE `rwikihistorycontent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rwikiobject`
--

DROP TABLE IF EXISTS `rwikiobject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rwikiobject` (
  `id` varchar(36) NOT NULL,
  `version` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `realm` varchar(255) DEFAULT NULL,
  `referenced` text,
  `userid` varchar(64) DEFAULT NULL,
  `owner` varchar(64) DEFAULT NULL,
  `ownerRead` bit(1) DEFAULT NULL,
  `ownerWrite` bit(1) DEFAULT NULL,
  `ownerAdmin` bit(1) DEFAULT NULL,
  `groupRead` bit(1) DEFAULT NULL,
  `groupWrite` bit(1) DEFAULT NULL,
  `groupAdmin` bit(1) DEFAULT NULL,
  `publicRead` bit(1) DEFAULT NULL,
  `publicWrite` bit(1) DEFAULT NULL,
  `sha1` varchar(64) DEFAULT NULL,
  `revision` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `irwikiobject_realm` (`realm`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rwikiobject`
--

LOCK TABLES `rwikiobject` WRITE;
/*!40000 ALTER TABLE `rwikiobject` DISABLE KEYS */;
/*!40000 ALTER TABLE `rwikiobject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rwikipagemessage`
--

DROP TABLE IF EXISTS `rwikipagemessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rwikipagemessage` (
  `id` varchar(36) NOT NULL,
  `sessionid` varchar(255) DEFAULT NULL,
  `userid` varchar(64) NOT NULL,
  `pagespace` varchar(255) DEFAULT NULL,
  `pagename` varchar(255) DEFAULT NULL,
  `lastseen` datetime DEFAULT NULL,
  `message` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rwikipagemessage`
--

LOCK TABLES `rwikipagemessage` WRITE;
/*!40000 ALTER TABLE `rwikipagemessage` DISABLE KEYS */;
/*!40000 ALTER TABLE `rwikipagemessage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rwikipagepresence`
--

DROP TABLE IF EXISTS `rwikipagepresence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rwikipagepresence` (
  `id` varchar(36) NOT NULL,
  `sessionid` varchar(255) DEFAULT NULL,
  `userid` varchar(64) NOT NULL,
  `pagespace` varchar(255) DEFAULT NULL,
  `pagename` varchar(255) DEFAULT NULL,
  `lastseen` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `irwikipagepresence_sid` (`sessionid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rwikipagepresence`
--

LOCK TABLES `rwikipagepresence` WRITE;
/*!40000 ALTER TABLE `rwikipagepresence` DISABLE KEYS */;
/*!40000 ALTER TABLE `rwikipagepresence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rwikipagetrigger`
--

DROP TABLE IF EXISTS `rwikipagetrigger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rwikipagetrigger` (
  `id` varchar(36) NOT NULL,
  `userid` varchar(64) NOT NULL,
  `pagespace` varchar(255) DEFAULT NULL,
  `pagename` varchar(255) DEFAULT NULL,
  `lastseen` datetime DEFAULT NULL,
  `triggerspec` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rwikipagetrigger`
--

LOCK TABLES `rwikipagetrigger` WRITE;
/*!40000 ALTER TABLE `rwikipagetrigger` DISABLE KEYS */;
/*!40000 ALTER TABLE `rwikipagetrigger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rwikipreference`
--

DROP TABLE IF EXISTS `rwikipreference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rwikipreference` (
  `id` varchar(36) NOT NULL,
  `userid` varchar(64) NOT NULL,
  `lastseen` datetime DEFAULT NULL,
  `prefcontext` varchar(255) DEFAULT NULL,
  `preftype` varchar(64) DEFAULT NULL,
  `preference` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rwikipreference`
--

LOCK TABLES `rwikipreference` WRITE;
/*!40000 ALTER TABLE `rwikipreference` DISABLE KEYS */;
/*!40000 ALTER TABLE `rwikipreference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rwikiproperties`
--

DROP TABLE IF EXISTS `rwikiproperties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rwikiproperties` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rwikiproperties`
--

LOCK TABLES `rwikiproperties` WRITE;
/*!40000 ALTER TABLE `rwikiproperties` DISABLE KEYS */;
INSERT INTO `rwikiproperties` VALUES ('15260f6b2f503aab012f503cc1640003','schema-version','20051107');
/*!40000 ALTER TABLE `rwikiproperties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scheduler_trigger_events`
--

DROP TABLE IF EXISTS `scheduler_trigger_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_trigger_events` (
  `uuid` varchar(36) NOT NULL,
  `eventType` varchar(255) NOT NULL,
  `jobName` varchar(255) NOT NULL,
  `triggerName` varchar(255) DEFAULT NULL,
  `eventTime` datetime NOT NULL,
  `message` text,
  PRIMARY KEY (`uuid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scheduler_trigger_events`
--

LOCK TABLES `scheduler_trigger_events` WRITE;
/*!40000 ALTER TABLE `scheduler_trigger_events` DISABLE KEYS */;
INSERT INTO `scheduler_trigger_events` VALUES ('15260f6b2f504190012f504b31120003','FIRED','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','2011-04-13 15:16:35','Trigger fired'),('15260f6b2f504190012f504b316e0004','COMPLETE','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','2011-04-13 15:16:35','Trigger complete'),('15260f6b2f504190012f5054587c0005','FIRED','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','2011-04-13 15:26:35','Trigger fired'),('15260f6b2f504190012f505458800006','COMPLETE','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','2011-04-13 15:26:35','Trigger complete'),('15260f6b2f504190012f505d80390007','FIRED','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','2011-04-13 15:36:35','Trigger fired'),('15260f6b2f504190012f505d803c0008','COMPLETE','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','2011-04-13 15:36:35','Trigger complete'),('15260f6b2f504190012f5066a7f90009','FIRED','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','2011-04-13 15:46:35','Trigger fired'),('15260f6b2f504190012f5066a7fb000a','COMPLETE','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','2011-04-13 15:46:35','Trigger complete'),('15260f6b2f504190012f506fcfb8000b','FIRED','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','2011-04-13 15:56:35','Trigger fired'),('15260f6b2f504190012f506fcfba000c','COMPLETE','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','org.sakaiproject.component.app.scheduler.ScheduledInvocationManagerImpl.runner','2011-04-13 15:56:35','Trigger complete');
/*!40000 ALTER TABLE `scheduler_trigger_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `search_journal`
--

DROP TABLE IF EXISTS `search_journal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search_journal` (
  `txid` bigint(20) NOT NULL,
  `txts` bigint(20) NOT NULL,
  `indexwriter` varchar(255) NOT NULL,
  `status` varchar(32) NOT NULL,
  PRIMARY KEY (`txid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `search_journal`
--

LOCK TABLES `search_journal` WRITE;
/*!40000 ALTER TABLE `search_journal` DISABLE KEYS */;
/*!40000 ALTER TABLE `search_journal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `search_node_status`
--

DROP TABLE IF EXISTS `search_node_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search_node_status` (
  `jid` bigint(20) NOT NULL,
  `jidts` bigint(20) NOT NULL,
  `serverid` varchar(255) NOT NULL,
  PRIMARY KEY (`serverid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `search_node_status`
--

LOCK TABLES `search_node_status` WRITE;
/*!40000 ALTER TABLE `search_node_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `search_node_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `search_segments`
--

DROP TABLE IF EXISTS `search_segments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search_segments` (
  `name_` varchar(254) NOT NULL,
  `version_` bigint(20) NOT NULL,
  `size_` bigint(20) NOT NULL,
  `packet_` longblob,
  PRIMARY KEY (`name_`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `search_segments`
--

LOCK TABLES `search_segments` WRITE;
/*!40000 ALTER TABLE `search_segments` DISABLE KEYS */;
/*!40000 ALTER TABLE `search_segments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `search_transaction`
--

DROP TABLE IF EXISTS `search_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search_transaction` (
  `txname` varchar(64) NOT NULL,
  `txid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`txname`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `search_transaction`
--

LOCK TABLES `search_transaction` WRITE;
/*!40000 ALTER TABLE `search_transaction` DISABLE KEYS */;
INSERT INTO `search_transaction` VALUES ('optimizeSequence',0),('mergeSequence',0),('sharedOptimizeSequence',0),('indexerTransaction',0),('itemQueueLock',2002);
/*!40000 ALTER TABLE `search_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `searchbuilderitem`
--

DROP TABLE IF EXISTS `searchbuilderitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `searchbuilderitem` (
  `id` varchar(64) NOT NULL,
  `version` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `context` varchar(255) NOT NULL,
  `searchaction` int(11) DEFAULT NULL,
  `searchstate` int(11) DEFAULT NULL,
  `itemscope` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `isearchbuilderitem_name` (`name`),
  KEY `isearchbuilderitem_ctx` (`context`),
  KEY `isearchbuilderitem_act_sta` (`searchstate`,`searchaction`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `searchbuilderitem`
--

LOCK TABLES `searchbuilderitem` WRITE;
/*!40000 ALTER TABLE `searchbuilderitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `searchbuilderitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `searchwriterlock`
--

DROP TABLE IF EXISTS `searchwriterlock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `searchwriterlock` (
  `id` varchar(64) NOT NULL,
  `lockkey` varchar(64) NOT NULL,
  `nodename` varchar(64) DEFAULT NULL,
  `expires` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `lockkey` (`lockkey`),
  KEY `isearchwriterlock_lk` (`lockkey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `searchwriterlock`
--

LOCK TABLES `searchwriterlock` WRITE;
/*!40000 ALTER TABLE `searchwriterlock` DISABLE KEYS */;
/*!40000 ALTER TABLE `searchwriterlock` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-04-13 16:00:57
