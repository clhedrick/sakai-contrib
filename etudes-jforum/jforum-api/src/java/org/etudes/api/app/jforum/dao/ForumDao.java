/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/dao/ForumDao.java $ 
 * $Id: ForumDao.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.JForumForumTopicsExistingException;

public interface ForumDao
{
	/**
	 * Adds a new forum
	 * 
	 * @param forum	Forum with groups and grade information
	 * 
	 * @return	Newly created forum id
	 */
	public int addNew(Forum forum);
	
	/**
	 * Decrements the total number of topics of a forum
	 * 
	 * @param forumId The forum ID to update
	 * 
	 * @param count Decrement a total of <code>count</code> elements 
	 */
	public void decrementTotalTopics(int forumId, int count);
	
	/**
	 * Deletes the forum
	 * 
	 * @param forumId	Forum id
	 * 
	 * @throws JForumForumTopicsExistingException	If topics are existing for the forum the forum cannot be deleted. Delete the topics first.
	 */
	public void delete(int forumId) throws JForumForumTopicsExistingException;
	
	/**
	 * Gets maximum post id of the forum
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return	Maximum post id of the forum
	 */
	public int getMaxPostId(int forumId);
	
	/**
	 * Gets the forum total topics count
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return	The forum total topics count
	 */
	public int getTotalTopics(int forumId);
	
	/**
	 * Increments the total number of topics of a forum
	 * 
	 * @param forumId The forum ID to update
	 * 
	 * @param count Increment a total of <code>count</code> elements
	 */
	public void incrementTotalTopics(int forumId, int count);
	
	/**
	 * Gets all the forums of the site or course
	 * 
	 * @param courseId	The course or site id
	 * 
	 * @return	The forums of the site
	 */
	public List<Forum> selectAllByCourse(String courseId);
	
	/**
	 * Gets the category forums
	 * 
	 * @param categoryId	Category id
	 * 
	 * @return	List of category forums
	 */
	public List<Forum> selectByCategoryId(int categoryId);
	
	/**
	 * Get the forum by id
	 * 
	 * @param forumId	The forum id
	 * 
	 * @return	The forum if exists else null
	 */
	public Forum selectById(int forumId);
	
	/**
	 * Sets the last topic of a forum
	 * 
	 * @param forumId	Forum id to update
	 * 
	 * @param postId	Last post id
	 */
	public void setLastPost(int forumId, int postId);
	
	/**
	 * Updates forum
	 * 
	 * @param forum	Forum
	 */
	public void update(Forum forum);
	
	/**
	 * Change the order of the forum
	 * 
	 * @param forum		The forum to change its order
	 * 
	 * @param other		The forum in the current position
	 * 
	 * @return	The changed forum, with the new order set
	 */
	public Forum updateOrder(Forum forum, Forum other);
}
