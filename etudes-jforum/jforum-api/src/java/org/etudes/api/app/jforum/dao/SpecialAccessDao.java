/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/dao/SpecialAccessDao.java $ 
 * $Id: SpecialAccessDao.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011, 2012 Etudes, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 ***********************************************************************************/
package org.etudes.api.app.jforum.dao;

import java.util.List;

import org.etudes.api.app.jforum.SpecialAccess;

public interface SpecialAccessDao
{
	/**
	 * Adds new forum special access and also removes if there is existing special access for the users
	 * 
	 * @param specialAccess	Special access
	 * 
	 * @return 	The special access id
	 */
	public int addForumSpecialAccess(SpecialAccess specialAccess);
	
	/**
	 * Adds new topic special access and also removes if there is existing special access for the users
	 * 
	 * @param specialAccess	Special access
	 * 
	 * @return	Special access
	 */
	public int addTopicSpecialAccess(SpecialAccess specialAccess);
	
	/**
	 * Deletes the special access
	 * 
	 * @param specialAccessId	The special access id
	 */
	public void delete(int specialAccessId);
	
	/**
	 * Deletes users special access
	 * 
	 * @param specialAccessId	The special access id
	 * 
	 * @param userId	The user id
	 */
	public void deleteUserSpecialAccess(int specialAccessId, int userId);
	
	/**
	 * Gets the forum special access
	 * 
	 * @param forumId	The forum id
	 * 
	 * @return	The list of forum special access
	 */
	public List<SpecialAccess> selectByForum(int forumId);
	
	/**
	 * Gets the special access 
	 * 
	 * @param specialAccessId	Special access id
	 * 
	 * @return	The special access if existing or null
	 */
	public SpecialAccess selectById(int specialAccessId);
	
	/**
	 * Gets the list of site special access
	 * 
	 * @param siteId	The site id
	 * 
	 * @return	The list of site special access
	 */
	public List<SpecialAccess> selectBySite(String siteId);
	
	/**
	 * Gets the list of all forum's special access of the site
	 * 
	 * @param siteId	The site id
	 * 
	 * @return	The list of all forum's special access of the site
	 */
	public List<SpecialAccess> selectBySiteAllForums(String siteId);
	
	/**
	 * Gets the list of all topic's special access of the site
	 * 
	 * @param siteId	The site id
	 * 
	 * @return	The list of all topic's special access of the site
	 */
	public List<SpecialAccess> selectBySiteAllTopics(String siteId);
	
	/**
	 * Gets the topic special access
	 * 
	 * @param forumId	The forum id
	 * 
	 * @param topicId	The topic id
	 * 
	 * @return	The list of topic special access
	 */
	public List<SpecialAccess> selectByTopic(int forumId, int topicId);
	
	/**
	 * Gets special access of the forum of all topics with dates
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return The special access of the forum of all topics with dates
	 */
	public List<SpecialAccess> selectTopicsByForumId(int forumId);
	
	/**
	 * Updates the exising special access.
	 * 
	 * @param specialAccess	Special access
	 */
	public void updateForumSpecialAccess(SpecialAccess specialAccess);
	
	/**
	 * Updates the topic special access
	 * 
	 * @param specialAccess Special access
	 */
	public void updateTopicSpecialAccess(SpecialAccess specialAccess);
}
