/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/mysql/CategoryDaoMysql.java $ 
 * $Id: CategoryDaoMysql.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.dao.CategoryDao;
import org.etudes.component.app.jforum.dao.generic.CategoryDaoGeneric;

public class CategoryDaoMysql extends CategoryDaoGeneric implements CategoryDao
{
	private static Log logger = LogFactory.getLog(CategoryDaoMysql.class);
	
	public void init()
	{
		if (logger.isInfoEnabled())
			logger.info("init....");
	}

	public void destroy()
	{
		if (logger.isInfoEnabled())
			logger.info("destroy....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int insertCategory(Category category)
	{
		int maxDisplayOrder = getMaxDisplayOrder();
		
		StringBuilder sql = new StringBuilder();
		//sql.append("INSERT INTO jforum_categories (title, display_order, moderated, archived, gradable, start_date, end_date, lock_end_date) ");
		//sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		
		sql.append("INSERT INTO jforum_categories (title, display_order, moderated, archived, gradable, start_date, hide_until_open, end_date, allow_until_date) ");
		sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		Object[] fields = new Object[9];
		int i = 0;
		fields[i++] = category.getTitle();
		fields[i++] = maxDisplayOrder + 1;
		fields[i++] = 0;
		fields[i++] = 0;
		fields[i++] = category.isGradable() ? 1 : 0;
		
		if (category.getAccessDates() != null)
		{
			if (category.getAccessDates().getOpenDate() == null)
			{
				fields[i++] = null;
				fields[i++] = 0;
			} 
			else
			{
				fields[i++] = new Timestamp(category.getAccessDates().getOpenDate().getTime());
				fields[i++] = category.getAccessDates().isHideUntilOpen() ? 1 : 0;
			}
	
			if (category.getAccessDates().getDueDate() == null)
			{
				fields[i++] = null;
				//fields[i++] = 0;
			} 
			else
			{
				fields[i++] = new Timestamp(category.getAccessDates().getDueDate().getTime());
				//fields[i++] = category.getAccessDates().isLocked() ? 1 : 0;
			}
			
			if (category.getAccessDates().getAllowUntilDate() == null)
			{
				fields[i++] = null;
			} 
			else
			{
				fields[i++] = new Timestamp(category.getAccessDates().getAllowUntilDate().getTime());
			}
		}
		else
		{
			fields[i++] = null;
			fields[i++] = 0;
			fields[i++] = null;
			//fields[i++] = 0;
			fields[i++] = null;
		}

		Long id = null;
		try
		{
			id = this.sqlService.dbInsert(null, sql.toString(), fields, "categories_id");
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
			throw new RuntimeException("insertCategory: dbInsert failed");
		}
		
		return id.intValue();
	}
}
