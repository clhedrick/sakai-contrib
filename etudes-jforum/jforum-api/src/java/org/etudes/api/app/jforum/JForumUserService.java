/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/JForumUserService.java $ 
 * $Id: JForumUserService.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.util.Date;
import java.util.List;

import org.etudes.api.app.jforum.dao.UserAlreadyExistException;
import org.etudes.api.app.jforum.dao.UserAlreadyInSiteUsersException;

public interface JForumUserService
{
	/**
	 * Adds or updates user mark all time
	 * 
	 * @param siteId	Site id
	 * 
	 * @param sakaiUserId	User sakai id
	 */
	public void addModifyMarkAllTime(String siteId, String sakaiUserId);
	
	/**
	 * Creates user visit time to the site if not visited or modifies the existing last visit time
	 * 
	 * @param siteId	The site id
	 * 
	 * @param sakaiUserId	User sakai id
	 */
	public void addModifySiteVisitTime(String siteId, String sakaiUserId);
	
	/**
	 * Add user to the jforum site users
	 * 
	 * @param siteId	The site id
	 * 
	 * @param userId	The user id
	 */
	public void addUserToSiteUsers(String siteId, int userId) throws UserAlreadyInSiteUsersException;
	
	/**
	 * Creates jforum user for the sakai user id
	 * 
	 * @param sakaiUserId	sakai user id
	 * 
	 * @return	Created jforum user
	 * 
	 * @throws UserAlreadyExistException
	 */
	public User createUser(String sakaiUserId) throws UserAlreadyExistException;
	
	/**
	 * Creates jforum user
	 * 
	 * @param user	The jforum user
	 * 
	 * @return	Created user id
	 */
	public int createUser(User user) throws UserAlreadyExistException;
	
	/**
	 * Gets jforum user. If user is in sakai and user is not JForum the user will be added to JForum.
	 * 
	 * @param sakaiUserId	The sakai user id
	 * 
	 * @return	The jforum user. If user is in sakai and user is not JForum the user will be added to JForum. Returns null if not sakai user.
	 */
	public User getBySakaiUserId(String sakaiUserId);
	
	/**
	 * Gets jforum user
	 * 
	 * @param userId	Jforum user id
	 * 
	 * @return	The jforum user is exists else null	
	 */
	public User getByUserId(int userId);
	
	/**
	 * Get user mark all read time
	 * 
	 * @param siteId	The site id
	 * 
	 * @param userId	The jforum user id
	 * 
	 * @return	The mark all time is exists else null
	 */
	public Date getMarkAllReadTime(String siteId, int userId);
	
	/**
	 * Gets the site user
	 * 
	 * @param siteId	The site id
	 * 
	 * @param userId	Jforum user id
	 * 
	 * @return	The site user
	 */
	public User getSiteUser(String siteId, int userId);
	
	/**
	 * Gets the site users in the jforum
	 * 
	 * @param siteId	Site id
	 * 
	 * @return	The list of site users in the jforum
	 */
	public List<User> getSiteUsers(String siteId);
	
	/**
	 * Gets the site users in the jforum
	 * 
	 * @param siteId	Site id
	 * 
	 * @param startFrom		Rows from
	 * 
	 * @param count			Number of records
	 * 
	 * @return	Gets the site users in the jforum	
	 */
	public List<User> getSiteUsers(String siteId, int startFrom, int count);
	
	/**
	 * Gets the site users in the jforum with posts count
	 * 
	 * @param siteId		Site id
	 * 
	 * @param startFrom		Rows from
	 * 
	 * @param count			Number of records
	 * 
	 * @return	Gets the site users in the jforum with posts count
	 */
	public List<User> getSiteUsersAndPostsCount(String siteId, int startFrom, int count);
	
	/**
	 * Gets the user site last visit time
	 * 
	 * @param siteId	The site id
	 * 
	 * @param sakaiUserId	User sakai id
	 * 
	 * @return	The user site last visit time
	 */
	public Date getSiteVisitTime(String siteId, String sakaiUserId);
	
	/**
	 * Gets the user posts count in the site
	 * 
	 * @param user	User
	 * 
	 * @param siteId	The site id
	 * 
	 * @return user posts count in the site
	 */
	public int getUserSitePostsCount(int userId, String siteId);
	
	/**
	 * Modified user
	 * 
	 * @param user	user
	 */
	public void modifyUser(User user);
	
	/**
	 * Modifies user first name, last name , email, and user lang
	 * 
	 * @param user	user
	 */
	public void modifyUserSakaiInfo(User user);
	
	/**
	 * Gets and sets the user posts count in the site
	 * 
	 * @param user	User
	 * 
	 * @param siteId	The site id
	 */
	public void setUserSitePostsCount(User user, String siteId);
}
