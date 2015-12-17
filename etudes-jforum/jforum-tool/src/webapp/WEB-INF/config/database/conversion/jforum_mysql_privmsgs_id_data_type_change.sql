---------------------------------------------------------------------------------- 
-- $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/webapp/WEB-INF/config/database/conversion/jforum_mysql_privmsgs_id_data_type_change.sql $ 
-- $Id: jforum_mysql_privmsgs_id_data_type_change.sql 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
----------------------------------------------------------------------------------- 
-- 
-- Copyright (c) 2008, 2009 , 2010, 2011, 2012, 2013 Etudes, Inc. 
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
-- This is for MySQL privmsgs_id data type change to bigint 
--------------------------------------------------------------------------
--Note : Before running this script back up the updated tables

-- privmsgs_id data type changed to bigint
ALTER TABLE jforum_privmsgs MODIFY COLUMN privmsgs_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT;

ALTER TABLE jforum_privmsgs_text MODIFY COLUMN privmsgs_id BIGINT UNSIGNED NOT NULL;

ALTER TABLE jforum_attach MODIFY COLUMN privmsgs_id BIGINT UNSIGNED;

ALTER TABLE jforum_sakai_course_privmsgs MODIFY COLUMN privmsgs_id BIGINT UNSIGNED NOT NULL;

ALTER TABLE jforum_privmsgs_attach MODIFY COLUMN privmsgs_id BIGINT UNSIGNED NOT NULL DEFAULT 0;