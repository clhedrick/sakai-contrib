/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/dao/UserAlreadyInSiteUsersException.java $ 
 * $Id: UserAlreadyInSiteUsersException.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.api.app.jforum.dao;

public class UserAlreadyInSiteUsersException extends Exception
{
	private String sakaiSiteId = null;
	private int jforumUserId;

	public UserAlreadyInSiteUsersException(String siteId, int userId)
	{
		sakaiSiteId = siteId;
		jforumUserId = userId;
	}

	public String toString()
	{
		return super.toString() + " User is in site users with sakaiSiteId = "+ sakaiSiteId +" and userId =  " + jforumUserId;
	}
}
