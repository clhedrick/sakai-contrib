/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/JForumForumService.java $ 
 * $Id: JForumForumService.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

public interface JForumForumService
{
	/**
	 * Creates new forum and grade if gradable and the forum groups if forum has groups
	 * 
	 * @param forum		Forum
	 * 
	 * @return	The newly created forum id
	 * 
	 * @throws JForumAccessException
	 */
	public int createForum(Forum forum) throws JForumAccessException;
	
	/**
	 * Deletes the forum
	 * 
	 * @param forumId		Forum id
	 * 
	 * @param sakaiUserId	Sakai user id 
	 * 
	 * @throws JForumAccessException	If the user has no access to delete the forum
	 * 
	 * @throws JForumForumTopicsExistingException	If forum has topics the forum cannot be deleted. Topics need to be deleted first.
	 * 
	 * @throws JForumItemEvaluatedException	If the forum is gradable and evaluated the forum cannot be deleted.
	 */
	public void deleteForum(int forumId, String sakaiUserId) throws JForumAccessException, JForumForumTopicsExistingException, JForumItemEvaluatedException;
	
	/**
	 * Evaluates forum
	 * 
	 * @param forum	Forum
	 */
	public void evaluateForum(Forum forum) throws JForumAccessException;

	/**
	 * Gets the category forums
	 * 
	 * @param categoryId	Category id
	 * 
	 * @return	The list of category forums
	 */
	public List<Forum> getCategoryForums(int categoryId);
	
	/**
	 * Gets the forum
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return The forum object or null if the forum doesn't exist
	 */
	public Forum getForum(int forumId);
	
	/**
	 * Get the forum for the user including read status
	 * 
	 * @param forumId	Forum id
	 * 
	 * @param sakaiUserId	Sakai user id
	 * 
	 * @return	The forum if existing else null
	 * 
	 * @throws JForumAccessException
	 */
	public Forum getForum(int forumId, String sakaiUserId) throws JForumAccessException;
	
	/**
	 * gets the category forum count that have dates
	 * 
	 * @param categoryId	Category id
	 * 
	 * @return	Count of the forums that have dates
	 */
	public int getForumDatesCount(int categoryId);
	
	/**
	 * Gets the forum users with participant role
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return	The forum users with participant role
	 */
	public List<User> getForumParticipantUsers(int forumId);
	
	/**
	 * Gets the topics count of the forum
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return	The topics count of the forum
	 */
	public int getTotalTopics(int forumId);
	
	/**
	 * Verifies user access to create new topic in the forum
	 * 
	 * @param forumId	Forum id
	 * 
	 * @param sakaiUserId	Sakai user id
	 * 
	 * @return	true - if user has access to create new topic
	 * 			false - if user has no access to create new topic
	 */
	public boolean isUserHasCreateTopicAccess(int forumId, String sakaiUserId);
	
	/**
	 * Modifies the existing forum
	 * 
	 * @param forum	Forum
	 * 
	 * @throws JForumAccessException
	 * 
	 * @throws JForumGradesModificationException
	 */
	public void modifyForum(Forum forum) throws JForumAccessException, JForumGradesModificationException;
	
	/**
	 * Updates forum order
	 * 
	 * @param forumId		Forum id that is to be reordered
	 * 
	 * @param positionToMove	new position to move
	 * 
	 * @param sakaiUserId		User who is updating the forum order
	 * 
	 * @throws JForumAccessException	If user has no access to update the forum order
	 */
	public void modifyOrder(int forumId, int positionToMove, String sakaiUserId) throws JForumAccessException;
	
	/**
	 * Creates new instance of AccessDates object
	 * 
	 * @param forum	The Forum object
	 * 
	 * @return	The AccessDates object
	 */
	public AccessDates newAccessDates(Forum forum);
	
	/**
	 * Creates new instance of forum object
	 * 
	 * @return	The Forum object
	 */
	public Forum newForum();
	
	/**
	 * Create new instance of grade object
	 * 
	 * @param forum		The Forum object
	 * 
	 * @return	The grade object
	 */
	public Grade newGrade(Forum forum);
	
	/**
	 * Sets the forum may access create topic. This method is for participant users
	 * 
	 * @param forum		Forum
	 * 
	 * @param userSa	user special access
	 */
	public void setForumMayAccessCreateTopic(Forum forum, SpecialAccess userSa);
	
	/**
	 * Updates the dates of the forum if changed
	 * 
	 * @param forum	Forum
	 */
	public void updateDates(Forum forum);
}