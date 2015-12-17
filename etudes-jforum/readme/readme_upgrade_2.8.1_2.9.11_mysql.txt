---------------------------------------------------------------------------------- 
-- $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/readme/readme_upgrade_2.8.1_2.9.11_mysql.txt $ 
-- $Id: readme_upgrade_2.8.1_2.9.11_mysql.txt 83562 2013-04-30 19:32:53Z murthy@etudes.org $ 
----------------------------------------------------------------------------------- 
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
----------------------------------------------------------------------------------
------------------------------------------------------------------
UPGRADE INSTRUCTIONS from Etudes Jforum 2.8.1 to Etudes Jforum 2.9.11
These instructions are for existing Etudes Jforum MySQL database users.
-----------------------------------------------------------------

1. Database conversion

 	1.1 This is for database conversion for existing JForum users
 		
 		
	1.2  Run the conversion script located at 
			jforum-tool/src/webapp/WEB-INF/config/database/conversion/jforum_2_8_1-2_9_11_sakai_mysql_conversion.sql	
	    
2.  Configure, Build and Deploy JForum-Sakai
    Follow step # 2, 3, 4 from readme.txt to build and deploy

