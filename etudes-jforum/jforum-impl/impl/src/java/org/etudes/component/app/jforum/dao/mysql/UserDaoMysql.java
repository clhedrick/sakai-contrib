/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/mysql/UserDaoMysql.java $ 
 * $Id: UserDaoMysql.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum.dao.mysql;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.User;
import org.etudes.api.app.jforum.dao.UserAlreadyExistException;
import org.etudes.api.app.jforum.dao.UserAlreadyInSiteUsersException;
import org.etudes.api.app.jforum.dao.UserDao;
import org.etudes.component.app.jforum.dao.generic.UserDaoGeneric;

public class UserDaoMysql extends UserDaoGeneric implements UserDao
{
	private static Log logger = LogFactory.getLog(UserDaoMysql.class);

	public void init()
	{
		if (logger.isInfoEnabled()) logger.info("init....");
	}
	
	public void destroy()
	{
		if (logger.isInfoEnabled()) logger.info("destroy....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int insertUser(User user) throws UserAlreadyExistException
	{
		// check to see if user is in jforum
		User jforumUser = selectBySakaiUserId(user.getSakaiUserId());
		if (jforumUser != null)
		{
			throw new UserAlreadyExistException(user.getSakaiUserId());
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO jforum_users (username, user_password, user_email, user_regdate, user_fname, user_lname, sakai_user_id) ");
		sql.append("VALUES (?, ?, ?, ?, ?, ?, ?)");
		
		Object[] fields = new Object[7];
		int i = 0;
		fields[i++] = user.getUsername();
		fields[i++] = "password";
		if (user.getEmail() == null || user.getEmail().trim().length() == 0)
		{
			fields[i++] = " ";
		}
		else
		{
			fields[i++] = user.getEmail();
		}
		fields[i++] = new Timestamp(System.currentTimeMillis());
		fields[i++] = user.getFirstName();
		fields[i++] = user.getLastName();
		fields[i++] = user.getSakaiUserId();	
		
		Long id = null;
		try
		{
			id = this.sqlService.dbInsert(null, sql.toString(), fields, "user_id");
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e, e);
			}
		}
		
		if (id == null)
		{
			throw new RuntimeException("insertUser: dbInsert failed");
		}
		
		return id.intValue();
	}	
}
