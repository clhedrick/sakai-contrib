---------------------------------------------------------------------------------- 
-- $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/webapp/WEB-INF/config/database/conversion/jforum_2_9_1_primary_key_addition_mysql_conversion.sql $ 
-- $Id: jforum_2_9_1_primary_key_addition_mysql_conversion.sql 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
----------------------------------------------------------------------------------- 
-- 
-- Copyright (c) 2008, 2009, 2010, 2011, 2012, 2013 Etudes, Inc. 
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
-- This is for MySQL, JForum 2.9.1 to JForum 2.9.2
--------------------------------------------------------------------------
-- Note : Before running this script back up the updated tables
--
---- add primary column to tables to address MySQL replication issues
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