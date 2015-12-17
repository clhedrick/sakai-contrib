/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/dao/UserDao.java $ 
 * $Id: UserDao.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.etudes.api.app.jforum.User;

public interface UserDao
{
	/**
	 * Add user to site users
	 * 
	 * @param siteId	The site id
	 * 
	 * @param userId	The user id
	 * 
	 * @throws UserAlreadyInSiteUsersException
	 */
	public void addToSiteUsers(String siteId, int userId) throws UserAlreadyInSiteUsersException;
	
	/**
	 * If user site last visit time is existing it gets updated else new record will be created
	 *  
	 * @param siteId	Site id
	 * 
	 * @param userId	User id
	 */
	public void addUpdateSiteVisitTime(String siteId, int userId);
	
	/**
	 * adds jforum user
	 * 
	 * @param user	The user
	 * 
	 * @return The newly created user id
	 */
	public int addUser(User user) throws UserAlreadyExistException;
	
	/**
	 * Mark all read time
	 * 
	 * @param siteId	The site id
	 * 
	 * @param userId	Jforum user id
	 */
	public void markAllReadTime(String siteId, int userId);
	
	/**
	 * Gets the jforum user 
	 * 
	 * @param sakaiUserId	The sakai user id
	 * 
	 * @return	The jforum user or null
	 */
	public User selectBySakaiUserId(String sakaiUserId);
	
	/**
	 * Gets the jforum user
	 * 
	 * @param userId	The jforum user id
	 * 
	 * @return	The jforum user or null
	 */
	public User selectByUserId(int userId);
	
	/**
	 * Gets the mark all read time of the user
	 * 
	 * @param siteId	The site or course id
	 * 
	 * @param userId	Jforum user id
	 * 
	 * @return  The mark all read time of the user or null if not existing
	 */
	public Date selectMarkAllReadTime(String siteId, int userId);
	
	/**
	 * Gets the site user
	 * 
	 * @param siteId	The site id
	 * 
	 * @param userId	The user id
	 * 
	 * @return	The jforum user or null
	 */
	public User selectSiteUser(String siteId, int userId);
	
	/**
	 * Gets the site users
	 * 
	 * @param siteId	The site id
	 * 
	 * @return	The site users
	 */
	public List<User> selectSiteUsers(String siteId);
	
	/**
	 * Gets the site uses with posts count
	 * 
	 * @param siteId	The site id
	 * 
	 * @return	The site uses with posts count
	 */
	public Map<String, Integer> selectSiteUsersPostsCount(String siteId);
	
	/**
	 * Gets the user last site visit time
	 * 
	 * @param siteId	Site id
	 * 
	 * @param userId	User id
	 * 
	 * @return The user last site visit time
	 */
	public Date selectSiteVisitTime(String siteId, int userId);
	
	/**
	 * Gets the users posts count for a site
	 * 
	 * @param userId	The user id
	 * 
	 * @param siteId	The site id
	 * 
	 * @return The users posts count for a site
	 */
	public int selectUserSitePostsCount(int userId, String siteId);
	
	/**
	 * Update user details with latest sakai user information
	 * 
	 * @param user	User
	 */
	public void updateSakaiAccountDetails(User user);
	
	/**
	 * Udates user information
	 * 
	 * @param user	User
	 */
	public void updateUser(User user);
	
	/**
	 * Updates user avatar
	 * 
	 * @param user	User
	 */
	public void updateUserAvatar(User user);
}
