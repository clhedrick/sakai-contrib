/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/mysql/PostDaoMysql.java $ 
 * $Id: PostDaoMysql.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Post;
import org.etudes.component.app.jforum.dao.generic.PostDaoGeneric;

public class PostDaoMysql extends PostDaoGeneric
{
	private static Log logger = LogFactory.getLog(PostDaoMysql.class);
	
	public void init()
	{
		if (logger.isInfoEnabled())
			logger.info("init....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int insertPost(Post post)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO jforum_posts (topic_id, forum_id, user_id, post_time, poster_ip, enable_bbcode, enable_html, enable_smilies, enable_sig, post_edit_time, need_moderate) ");
		sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		Object[] fields = new Object[11];
		int i = 0;
		Date now = new Date();
		
		fields[i++] = post.getTopicId();
		fields[i++] = post.getForumId();
		fields[i++] = post.getPostedBy().getId();
		
		if (post.getTime() != null)
		{
			fields[i++] = new Timestamp(post.getTime().getTime());
		}
		else
		{
			fields[i++] = new Timestamp(now.getTime());
		}
		fields[i++] = post.getUserIp();
		fields[i++] = (post.isBbCodeEnabled() ? 1 : 0);
		fields[i++] = (post.isHtmlEnabled() ? 1 : 0);
		fields[i++] = (post.isSmiliesEnabled() ? 1 : 0);
		fields[i++] = (post.isSignatureEnabled() ? 1 : 0);
		if (post.getTime() != null)
		{
			fields[i++] = new Timestamp(post.getTime().getTime());
		}
		else
		{
			fields[i++] = new Timestamp(now.getTime());
		}
		
		fields[i++] = 0;
		
		Long id = null;
		try
		{
			id = this.sqlService.dbInsert(null, sql.toString(), fields, "post_id");
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
			throw new RuntimeException("insertPost: dbInsert failed");
		}
		
		return id.intValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void insertPostText(Post post)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO jforum_posts_text ( post_id, post_text, post_subject ) VALUES (?, ?, ?)");
		
		Object[] fields = new Object[3];
		int i = 0;
		
		fields[i++] = post.getId();
		fields[i++] = post.getText();
		fields[i++] = post.getSubject();
				
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("insertPostText: dbWrite failed");
		}
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
	protected void updatePostText(Post post)
	{
		String sql = "UPDATE jforum_posts_text SET post_text = ?, post_subject = ? WHERE post_id = ?";
		
		Object[] fields = new Object[3];
		int i = 0;
		
		fields[i++] = post.getText();
		fields[i++] = post.getSubject();
		fields[i++] = post.getId();
				
		if (!sqlService.dbWrite(sql, fields)) 
		{
			throw new RuntimeException("updatePostsText: dbWrite failed");
		}
		
	}
	
}
