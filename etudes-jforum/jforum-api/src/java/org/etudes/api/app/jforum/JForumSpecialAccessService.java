/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/JForumSpecialAccessService.java $ 
 * $Id: JForumSpecialAccessService.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.api.app.jforum;

import java.util.List;


public interface JForumSpecialAccessService
{

	/**
	 * Creates forum special access
	 * 
	 * @param specialAccess	Special access
	 */
	public void createForumSpecialAccess(SpecialAccess specialAccess);
	
	/**
	 * Creates topic special access
	 * 
	 * @param specialAccess	Special access
	 */
	public void createTopicSpecialAccess(SpecialAccess specialAccess);
	
	/**
	 * Deletes the special access
	 * 
	 * @param specialAccessId	Special access id
	 */
	public void delete(int specialAccessId);
	
	/**
	 * Gets the special access list for a forum
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return	The list of special access for a forum
	 */
	public List<SpecialAccess> getByForum(int forumId);

	/**
	 * Gets the special access list for the site
	 * 
	 * @param siteId	The site id
	 * 
	 * @return	The list of special access
	 */
	public List<SpecialAccess> getBySite(String siteId);
	
	/**
	 * Gets the special access list for a topic
	 * 
	 * @param forumId	Forum id
	 * 
	 * @param topicId	Topic id
	 * 
	 * @return	The list of special access for a topic
	 */
	public List<SpecialAccess> getByTopic(int forumId, int topicId);
	
	/**
	 * Gets the special access
	 * 
	 * @param specialAccessId	Special access id
	 * 
	 * @return	The special access if existing or null
	 */
	public SpecialAccess getSpecialAccess(int specialAccessId);

	/**
	 * Gets the special access list for all of the forum topics
	 * 
	 * @param forumId	The forum id
	 * 
	 * @return	The special access list for all of the forum topics
	 */
	public List<SpecialAccess> getTopicsByForumId(int forumId);
	
	/**
	 * check for forum special access valid dates
	 * 
	 * @return	true - if dates are valid
	 * 			false - if dates are invalid
	 */
	public boolean isForumSpecialAccessDatesValid(Forum forum, SpecialAccess specialAccess);
	
	/**
	 * Modifies forum special access
	 * 
	 * @param specialAccess	Special access
	 */
	public void modifyForumSpecialAccess(SpecialAccess specialAccess);
	
	/**
	 * Modifies topic special access
	 * 
	 * @param specialAccess	Special access
	 */
	public void modifyTopicSpecialAccess(SpecialAccess specialAccess);
	
	/**
	 * Creates new instance of special access
	 * 
	 * @param forumId	Forum id
	 * 
	 * @param topicId	Topic id
	 * 
	 * @return	The new instance of special access
	 */
	public SpecialAccess newSpecialAccess(int forumId, int topicId);
	

	
	/**
	 * Removes the user special access
	 * 
	 * @param specialAccessId	Special access id
	 * 
	 * @param userId	User id
	 */
	public void removeUserSpecialAccess(int specialAccessId, int userId);
}