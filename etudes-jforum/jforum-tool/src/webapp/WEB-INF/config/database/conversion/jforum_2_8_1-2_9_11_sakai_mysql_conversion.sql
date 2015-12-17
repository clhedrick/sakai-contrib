-- -------------------------------------------------------------------------------- 
-- $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/webapp/WEB-INF/config/database/conversion/jforum_2_8_1-2_9_11_sakai_mysql_conversion.sql $ 
-- $Id: jforum_2_8_1-2_9_11_sakai_mysql_conversion.sql 83562 2013-04-30 19:32:53Z murthy@etudes.org $ 
-- --------------------------------------------------------------------------------- 
-- 
-- Copyright (c) 2013 Etudes, Inc. 
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
-- --------------------------------------------------------------------------------
-- ------------------------------------------------------------------------
-- This is for MySQL, JForum 2.8.1 (JForum public release) to JForum 2.9.10 (JForum public release) for Sakai 2.9.1 installations
-- ------------------------------------------------------------------------
-- Note : Before running this script back up the updated tables



-- ------------------------------------------------------------------------
-- MySQL, JForum 2.8 to JForum 2.9m1
-- ------------------------------------------------------------------------
-- add start date, end date and lock end date to jforum_topics
ALTER TABLE jforum_topics ADD COLUMN start_date DATETIME AFTER topic_export, ADD COLUMN end_date DATETIME AFTER start_date, ADD COLUMN lock_end_date TINYINT(1) UNSIGNED DEFAULT 0 AFTER end_date;

-- add start date, end date and lock end date to jforum_search_topics
ALTER TABLE jforum_search_topics ADD COLUMN start_date DATETIME AFTER topic_export, ADD COLUMN end_date DATETIME AFTER start_date, ADD COLUMN lock_end_date TINYINT(1) UNSIGNED DEFAULT 0 AFTER end_date;

-- add topic_id to jforum_special_access
ALTER TABLE jforum_special_access ADD COLUMN topic_id MEDIUMINT(8) UNSIGNED NOT NULL DEFAULT 0 AFTER forum_id;

-- add min_posts to jforum_grade
ALTER TABLE jforum_grade ADD COLUMN min_posts MEDIUMINT UNSIGNED NOT NULL DEFAULT 1 AFTER categories_id;

-- add override_lock_end_date to jforum_special_access
ALTER TABLE jforum_special_access ADD COLUMN override_lock_end_date TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 AFTER override_end_date;

-- ------------------------------------------------------------------------
-- MySQL, JForum 2.9m1 to JForum 2.9m2
-- ------------------------------------------------------------------------
-- add min_posts_required to jforum_grade
ALTER TABLE jforum_grade ADD COLUMN min_posts_required TINYINT(1) NOT NULL DEFAULT 0 AFTER min_posts;


-- ------------------------------------------------------------------------
-- MySQL, JForum 2.9m3 to JForum 2.9m4
-- ------------------------------------------------------------------------
-- add reviewed_date to jforum_evaluations
ALTER TABLE jforum_evaluations ADD COLUMN reviewed_date DATETIME AFTER released;


-- -----------------------------------------------------------------------
-- MySQL, JForum 2.9m7 to JForum 2.9m8
-- -----------------------------------------------------------------------
-- add index on jforum_grade for context and grade_type
CREATE INDEX idx_jforum_grade_context_grade_type ON jforum_grade(context, grade_type);


-- -----------------------------------------------------------------------
-- MySQL, JForum 2.9m14 to JForum 2.9m15
-- -----------------------------------------------------------------------
-- change the user_lang from NOT NULL to NULL
ALTER TABLE jforum_users MODIFY user_lang VARCHAR(255) NULL DEFAULT NULL;


-- -----------------------------------------------------------------------
-- MySQL, JForum 2.9m17 to JForum 2.9m18
-- -----------------------------------------------------------------------
-- update forum description data type
ALTER TABLE jforum_forums MODIFY forum_desc LONGTEXT NULL DEFAULT NULL;

-- -----------------------------------------------------------------------
-- MySQL, JForum 2.9m18 to JForum 2.9
-- -----------------------------------------------------------------------
-- update jforum_posts poster_ip length
ALTER TABLE jforum_posts MODIFY COLUMN poster_ip VARCHAR(50) DEFAULT NULL;
--
-- change the user_lang from NOT NULL to NULL
ALTER TABLE jforum_users MODIFY user_lang VARCHAR(255) NULL DEFAULT NULL;
--
-- jforum_categories add columns allow_until_date, hide_until_open
ALTER TABLE jforum_categories ADD COLUMN allow_until_date DATETIME NULL DEFAULT NULL  AFTER lock_end_date , ADD COLUMN hide_until_open TINYINT(1) NULL DEFAULT 0  AFTER allow_until_date ;
--
-- jforum_forums add columns allow_until_date, hide_until_open
ALTER TABLE jforum_forums ADD COLUMN allow_until_date DATETIME NULL DEFAULT NULL  AFTER lock_end_date , ADD COLUMN hide_until_open TINYINT(1) NULL DEFAULT 0  AFTER allow_until_date ;
--
-- jforum_topics add columns allow_until_date, hide_until_open
ALTER TABLE jforum_topics ADD COLUMN allow_until_date DATETIME NULL DEFAULT NULL  AFTER lock_end_date , ADD COLUMN hide_until_open TINYINT(1) NULL DEFAULT 0  AFTER allow_until_date ;
--
-- jforum_special_access add columns allow_until_date, hide_until_open, override_allow_until_date, override_hide_until_open
ALTER TABLE jforum_special_access ADD COLUMN allow_until_date DATETIME NULL  AFTER users , ADD COLUMN hide_until_open TINYINT(1) NULL DEFAULT 0  AFTER allow_until_date , ADD COLUMN override_allow_until_date TINYINT(1) NULL DEFAULT 0  AFTER hide_until_open , ADD COLUMN override_hide_until_open TINYINT(1) NULL DEFAULT 0  AFTER override_allow_until_date ;

-- added user accounts for google plus, skype, linkedIn
ALTER TABLE jforum_users ADD COLUMN user_google_plus VARCHAR(255) NULL DEFAULT NULL  AFTER user_twitter_account , ADD COLUMN user_skype VARCHAR(255) NULL DEFAULT NULL  AFTER user_google_plus , ADD COLUMN user_linkedIn VARCHAR(255) NULL DEFAULT NULL  AFTER user_skype;

-- ----------------------------------------------------------------------
-- MySQL, JForum 2.9 to JForum 2.9.1
-- -----------------------------------------------------------------------
-- add allow_until_date to jforum_search_topics
ALTER TABLE jforum_search_topics ADD COLUMN allow_until_date DATETIME NULL DEFAULT NULL  AFTER search_time, ADD COLUMN hide_until_open TINYINT(1) NULL DEFAULT 0  AFTER allow_until_date ;

-- ------------------------------------------------------------------------
-- JForum 2.9.1 to JForum 2.9.2
-- ------------------------------------------------------------------------
-- -- add primary column to tables to address MySQL replication issues
-- add id primary column to jforum_role_values 
ALTER TABLE jforum_role_values ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- add id primary column to jforum_sakai_course_categories 
ALTER TABLE jforum_sakai_course_categories ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- add id primary column to jforum_sakai_course_groups 
ALTER TABLE jforum_sakai_course_groups ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;
  
-- add id primary column to jforum_sakai_course_initvalues 
ALTER TABLE jforum_sakai_course_initvalues ADD PRIMARY KEY (course_id) ;

-- if there are duplicate roles then add id as primary key
-- ALTER TABLE jforum_sakai_course_initvalues ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- add id primary column to jforum_sakai_course_privmsgs 
ALTER TABLE jforum_sakai_course_privmsgs ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- add id primary column to jforum_sakai_sessions 
ALTER TABLE jforum_sakai_sessions ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- add id primary column to jforum_search_results 
ALTER TABLE jforum_search_results ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- add id primary column to jforum_search_topics 
ALTER TABLE jforum_search_topics ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- add id primary column to jforum_search_wordmatch 
ALTER TABLE jforum_search_wordmatch ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- add id primary column to jforum_sessions 
ALTER TABLE jforum_sessions ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- add id primary column to jforum_topics_watch 
ALTER TABLE jforum_topics_watch ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- add id primary column to jforum_user_groups 
ALTER TABLE jforum_user_groups ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- add id primary column to jforum_vote_results 
ALTER TABLE jforum_vote_results ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- add id primary column to jforum_vote_voters
ALTER TABLE jforum_vote_voters ADD id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY;

-- ------------------------------------------------------------------------
-- MySQL, JForum 2.9.7 to JForum 2.9.8
-- ------------------------------------------------------------------------
-- create table jforum_schedule_grades_gradebook
CREATE  TABLE jforum_schedule_grades_gradebook (
  grade_id MEDIUMINT UNSIGNED NOT NULL ,  
  open_date DATETIME NOT NULL ,
  PRIMARY KEY (grade_id)
);
