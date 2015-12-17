/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/SpecialAccess.java $ 
 * $Id: SpecialAccess.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011 Etudes, Inc. 
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

public interface SpecialAccess
{

	/**
	 * Gets the override lock on end date
	 * 
	 * @return true - if the Lock on EndDate is overridden
	 * 			false - if the Lock on EndDate is not overridden
	 */
	public Boolean isOverrideLockEndDate();

	/**
	 * Sets the id
	 * 
	 * @param id 	The id to set
	 */
	//void setId(int id);

	/**
	 * Sets the override lock on end date
	 * 
	 * @param overrideLockEndDate The overrideLockEndDate to set
	 */
	public void setOverrideLockEndDate(Boolean overrideLockEndDate);

	/**
	 * sets the forum id
	 * 
	 * @param forumId	The forumId to set
	 */
	//void setForumId(int forumId);

	/**
	 * Get the category access dates
	 * 
	 * @return	The category access dates
	 */
	AccessDates getAccessDates();

	/**
	 * Sets the topic id
	 * 
	 * @param topicId 	The topicId to set
	 */
	//void setTopicId(int topicId);

	/**
	 * Sets the access dates
	 * 
	 * @param accessDates 	The accessDates to set
	 */
	//void setAccessDates(AccessDates accessDates);
	
	/**
	 * gets the forum id
	 * 
	 * @return 	The forumId
	 */
	int getForumId();

	/**
	 * Gets the id
	 * 
	 * @return The id
	 */
	int getId();

	/**
	 * Getst the topic id
	 * 
	 * @return 	The topicId
	 */
	int getTopicId();

	/**
	 * Gets the list of userid's
	 * 
	 * @return 	The userIds
	 */
	List<Integer> getUserIds();

	/**
	 * Gets the jforum users
	 * 
	 * @return 	The users
	 */
	List<User> getUsers();

	/**
	 * validates forum special access dates
	 * 
	 * @param forum	Forum of the special access
	 * 
	 * @return	true - if dates are valid
	 * 			false - if dates are invalid
	 */
	boolean isForumSpecialAccessDatesValid(Forum forum);
	
	/**
	 * validates topic special access dates
	 * 
	 * @param topic	Topic of the special access
	 * 
	 * @return	true - if dates are valid
	 * 			false - if dates are invalid
	 */
	boolean isTopicSpecialAccessDatesValid(Topic topic);

	/**
	 * Gets the is lock on enddate
	 * 
	 * @return the true - if the end date is locked
	 * 			false - if the end date is not locked
	 */
	@Deprecated
	Boolean isLockOnEndDate();
	
	/**
	 * @return the overrideAllowUntilDate
	 */
	Boolean isOverrideAllowUntilDate();

	/**
	 * gets the override due date
	 * 
	 * @return 	true - if the end date is overridden
	 * 			false - if the end date is not overridden
	 */
	Boolean isOverrideEndDate();

	/**
	 * @return the overrideHideUntilOpen
	 */
	Boolean isOverrideHideUntilOpen();

	/**
	 * Gets the override open date
	 * 
	 * @return 	true - if the start date is overridden
	 * 			false - if the start date is not overridden
	 */
	Boolean isOverrideStartDate();

	/**
	 * Sets the lock on end date
	 * 
	 * @param lockOnEndDate 	The lockOnEndDate to set
	 */
	@Deprecated
	void setLockOnEndDate(Boolean lockOnEndDate);

	/**
	 * @param overrideAllowUntilDate the overrideAllowUntilDate to set
	 */
	void setOverrideAllowUntilDate(Boolean overrideAllowUntilDate);
	
	
	/**
	 * Sets the override due date
	 * 
	 * @param overrideEndDate 	The overrideEndDate to set
	 */
	void setOverrideEndDate(Boolean overrideEndDate);
	
	/**
	 * @param overrideHideUntilOpen the overrideHideUntilOpen to set
	 */
	void setOverrideHideUntilOpen(Boolean overrideHideUntilOpen);

	/**
	 * Sets the override open date
	 * 
	 * @param overrideStartDate 	The overrideStartDate to set
	 */
	void setOverrideStartDate(Boolean overrideStartDate);

	/**
	 * Sets the userid's
	 * 
	 * @param userIds 	The userIds to set
	 */
	void setUserIds(List<Integer> userIds);
	
	/**
	 * Sets the users
	 * 
	 * @param users 	The users to set
	 */
	void setUsers(List<User> users);
}