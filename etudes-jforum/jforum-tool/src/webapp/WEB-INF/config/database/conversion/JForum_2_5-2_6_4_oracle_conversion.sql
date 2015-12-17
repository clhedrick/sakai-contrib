---------------------------------------------------------------------------------- 
-- $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/webapp/WEB-INF/config/database/conversion/JForum_2_5-2_6_4_oracle_conversion.sql $ 
-- $Id: JForum_2_5-2_6_4_oracle_conversion.sql 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
----------------------------------------------------------------------------------- 
-- 
-- Copyright (c) 2009 Etudes, Inc. 
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
-- This is the Oracle JForum 2.5 to JForum 2.6.4 conversion script
-----------------------------------------------------------------------------------------------------
--add privmsgs_flag_to_follow to jforum_privmsgs
ALTER TABLE jforum_privmsgs ADD privmsgs_flag_to_follow NUMBER(10) DEFAULT 0 NOT NULL;

--add privmsgs_replied to jforum_privmsgs
ALTER TABLE jforum_privmsgs ADD privmsgs_replied NUMBER(10) DEFAULT 0 NOT NULL;