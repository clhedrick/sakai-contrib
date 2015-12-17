package org.etudes.component.app.jforum.dao.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.PrivateMessage;
import org.etudes.component.app.jforum.dao.generic.PrivateMessageDaoGeneric;

/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/mysql/PrivateMessageDaoMysql.java $ 
 * $Id: PrivateMessageDaoMysql.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
public class PrivateMessageDaoMysql extends PrivateMessageDaoGeneric
{
	private static Log logger = LogFactory.getLog(PrivateMessageDaoMysql.class);
	
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
	protected String getPmText(ResultSet rs) throws SQLException
	{
		return rs.getString("privmsgs_text");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int insertPrivateMessage(PrivateMessage pm)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO jforum_privmsgs ( privmsgs_type, privmsgs_subject, privmsgs_from_userid, privmsgs_to_userid, privmsgs_date, privmsgs_enable_bbcode, ");
		sql.append("privmsgs_enable_html, privmsgs_enable_smilies, privmsgs_attach_sig, privmsgs_priority ) ");
		sql.append("VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
		
		Object[] fields = new Object[10];
		int i = 0;
		fields[i++] = pm.getType();
		fields[i++] = pm.getPost().getSubject();
		fields[i++] = pm.getFromUser().getId();
		fields[i++] = pm.getToUser().getId();
		fields[i++] = new Timestamp(pm.getPost().getTime().getTime());
		fields[i++] = pm.getPost().isBbCodeEnabled() ? 1 : 0;
		fields[i++] = pm.getPost().isHtmlEnabled() ? 1 : 0;
		fields[i++] = pm.getPost().isSmiliesEnabled() ? 1 : 0;
		fields[i++] = pm.getPost().isSignatureEnabled() ? 1 : 0;
		fields[i++] = pm.getPriority();
		
		Long id = null;
		try
		{
			id = this.sqlService.dbInsert(null, sql.toString(), fields, "privmsgs_id");
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
			throw new RuntimeException("insertPrivateMessage: dbInsert failed");
		}
		
		return id.intValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void insertPrivateMessageText(int pmId, String messageText)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO jforum_privmsgs_text ( privmsgs_id, privmsgs_text ) VALUES (?, ?)");
		
		Object[] fields = new Object[2];
		int i = 0;
		fields[i++] = pmId;
		fields[i++] = messageText;
		
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("insertPrivateMessageText: db write failed");
		}
	}

}
