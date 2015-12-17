/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/mysql/SpecialAccessDaoMysql.java $ 
 * $Id: SpecialAccessDaoMysql.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum.dao.mysql;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.dao.SpecialAccessDao;
import org.etudes.component.app.jforum.dao.generic.SpecialAccessGeneric;

public class SpecialAccessDaoMysql extends SpecialAccessGeneric implements SpecialAccessDao
{
	private static Log logger = LogFactory.getLog(SpecialAccessDaoMysql.class);
	
	public void destroy()
	{
		if (logger.isInfoEnabled())
			logger.info("destroy....");
	}

	public void init()
	{
		if (logger.isInfoEnabled())
			logger.info("init....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected int insertSpecialAccess(SpecialAccess specialAccess)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO jforum_special_access(forum_id, topic_id, start_date, hide_until_open, end_date, allow_until_date, override_start_date, override_hide_until_open, override_end_date, ");
		sql.append("override_allow_until_date, users) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		Object[] fields = new Object[11];
		int i = 0;
		
		fields[i++] = specialAccess.getForumId();
		fields[i++] = specialAccess.getTopicId();
		
		if (specialAccess.getAccessDates().getOpenDate() == null)
		{
			fields[i++] = null;
		}
		else
		{
			fields[i++] = new Timestamp(specialAccess.getAccessDates().getOpenDate().getTime());
		}
		
		fields[i++] = specialAccess.getAccessDates().isHideUntilOpen() ? 1 : 0;
		
		if (specialAccess.getAccessDates().getDueDate() == null)
		{
			fields[i++] = null;
		}
		else
		{
			fields[i++] = new Timestamp(specialAccess.getAccessDates().getDueDate().getTime());		  
		}
		
		if (specialAccess.getAccessDates().getAllowUntilDate() == null)
		{
			fields[i++] = null;
		}
		else
		{
			fields[i++] = new Timestamp(specialAccess.getAccessDates().getAllowUntilDate().getTime());		  
		}
		
		fields[i++] = specialAccess.isOverrideStartDate() ? 1 : 0;
		fields[i++] = specialAccess.isOverrideHideUntilOpen() ? 1 : 0;
		fields[i++] = specialAccess.isOverrideEndDate() ? 1 : 0;
		fields[i++] = specialAccess.isOverrideAllowUntilDate() ? 1 : 0;
		fields[i++] = getUserIdString(specialAccess.getUserIds());
		
		Long id = null;
		try
		{
			id = this.sqlService.dbInsert(null, sql.toString(), fields, "topic_id");
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
			throw new RuntimeException("insertSpecialAccess: dbInsert failed");
		}
		
		return id.intValue();
	}

}
