/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/JForumSecurityService.java $ 
 * $Id: JForumSecurityService.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

public interface JForumSecurityService
{
	/** For the users who can administrate - This is not used */
	public static final String ROLE_ADMIN="jforum.admin";
	
	/** For the users who can manage */
	public static final String ROLE_FACILITATOR="jforum.manage";
	
	/** For the users who can participate */
	public static final String ROLE_PARTICIPANT="jforum.member";
		

	/**
	 * Check to see if the current user is jforum facilitator in the current site
	 * 
	 * @return	True - if the user is facilitator
	 * 			False - if the user is not facilitator
	 * 				
	 */
	public Boolean isJForumFacilitator();
	
	/**
	 * Check to see if the current user is jforum facilitator
	 * 
	 * @param context	Context
	 * 
	 * @return	True - if the user is facilitator
	 * 			False - if the user is not facilitator
	 * 				
	 */
	public Boolean isJForumFacilitator(String context);
	
	/**
	 *  Check to see if the user is jforum facilitator
	 *  
	 * @param context	Context
	 * 
	 * @param userId	User id
	 * 	
	 * @return	True - if the user is facilitator
	 * 			False - if the user is not facilitator
	 */
	public Boolean isJForumFacilitator(String context, String userId);
	
	/**
	 * Check to see if the current user is jforum participant in the current site
	 * 
	 * @param context	Context
	 * 
	 * @return	True - if the user is participant
	 * 			False - if the user is not participant
	 * 				
	 */
	public Boolean isJForumParticipant();
	
	/**
	 * Check to see if the current user is jforum participant
	 * 
	 * @param context	Context
	 * 
	 * @return	True - if the user is participant
	 * 			False - if the user is not participant
	 * 				
	 */
	public Boolean isJForumParticipant(String context);
	
	/**
	 *  Check to see if the user is jforum participant
	 *  
	 * @param context	Context
	 * 
	 * @param userId	User id
	 * 	
	 * @return	True - if the user is participant
	 * 			False - if the user is not participant
	 */
	public Boolean isJForumParticipant(String context, String userId);
	
	/**
	 * Check user active or inactive in the site
	 * 
	 * @param context		Context
	 * 
	 * @param sakaiUserId	user sakai id
	 * 
	 * @return	true - if user is active in site
	 * 			false - if user is inactive in site
	 */
	public Boolean isUserActive(String context, String sakaiUserId);
	
	/**
	 * Check to see if user is blocked in the site
	 * 
	 * @param context	Context
	 * 
	 * @param sakaiUserId	user sakai id
	 * 
	 * @return	true - if user is blocked in site
	 * 			false - if user is not blocked in site
	 */
	public Boolean isUserBlocked(String context, String sakaiUserId);
	
}
