---------------------------------------------------------------------------------- 
-- $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/webapp/WEB-INF/config/database/conversion/jforum_2_9m18-2_9_mysql_conversion.sql $ 
-- $Id: jforum_2_9m18-2_9_mysql_conversion.sql 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
----------------------------------------------------------------------------------- 
-- 
-- Copyright (c) 2008, 2009, 2010, 2011, 2012 Etudes, Inc. 
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
-- This is for MySQL, JForum 2.9m18 to JForum 2.9
--------------------------------------------------------------------------
-- Note : Before running this script back up the updated tables
--
--update jforum_posts poster_ip length
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