---------------------------------------------------------------------------------- 
-- $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/webapp/WEB-INF/config/database/conversion/jforum_2_8-2_9m1_mysql_conversion.sql $ 
-- $Id: jforum_2_8-2_9m1_mysql_conversion.sql 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
----------------------------------------------------------------------------------- 
-- 
-- Copyright (c) 2010, 2011 Etudes, Inc. 
-- 
-- Licensed under the Apache License, Version 2.0 (the "License"); 
-- you may not use this file except in compliance with the License. 
-- You may obtain a copy of the License at 
-- 
-- http://www.apache.org/licenses/LICENSE-2.0 
-- 
-- Unless required by applicable law or agreed to in writing, software 
-- distributed under the License is distributed on an "AS IS" BASIS, 
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
-- See the License for the specific language governing permissions and 
-- limitations under the License. 
----------------------------------------------------------------------------------
--------------------------------------------------------------------------
-- This is for MySQL, JForum 2.8 to JForum 2.9m1
--------------------------------------------------------------------------
--Note : Before running this script back up the updated tables
--add start date, end date and lock end date to jforum_topics
ALTER TABLE jforum_topics ADD COLUMN start_date DATETIME AFTER topic_export, ADD COLUMN end_date DATETIME AFTER start_date, ADD COLUMN lock_end_date TINYINT(1) UNSIGNED DEFAULT 0 AFTER end_date;

--add start date, end date and lock end date to jforum_search_topics
ALTER TABLE jforum_search_topics ADD COLUMN start_date DATETIME AFTER topic_export, ADD COLUMN end_date DATETIME AFTER start_date, ADD COLUMN lock_end_date TINYINT(1) UNSIGNED DEFAULT 0 AFTER end_date;

--add topic_id to jforum_special_access
ALTER TABLE jforum_special_access ADD COLUMN topic_id MEDIUMINT(8) UNSIGNED NOT NULL DEFAULT 0 AFTER forum_id;

--add min_posts to jforum_grade
ALTER TABLE jforum_grade ADD COLUMN min_posts MEDIUMINT UNSIGNED NOT NULL DEFAULT 1 AFTER categories_id;

--add override_lock_end_date to jforum_special_access
ALTER TABLE jforum_special_access ADD COLUMN override_lock_end_date TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 AFTER override_end_date;