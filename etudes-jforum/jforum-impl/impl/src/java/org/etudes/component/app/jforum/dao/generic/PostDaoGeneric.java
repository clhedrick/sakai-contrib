/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/generic/PostDaoGeneric.java $ 
 * $Id: PostDaoGeneric.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum.dao.generic;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.dao.PostDao;
import org.etudes.component.app.jforum.PostImpl;
import org.sakaiproject.db.api.SqlService;

public abstract class PostDaoGeneric implements PostDao
{
	private static Log logger = LogFactory.getLog(PostDaoGeneric.class);
	
	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	/**
	 * {@inheritDoc}
	 */
	public int addNew(Post post)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Start : addNew");
		}
		if (post == null)
		{
			throw new IllegalArgumentException("Post information is required.");
		}
		
		if (post.getId() > 0 || post.getTopicId() <= 0 || post.getForumId() <= 0)
		{
			throw new IllegalArgumentException("New post cannot be created as post may be having id or has no topic or forum id");
		}
		
		// save post
		int postId = insertPost(post);
		((PostImpl)post).setId(postId);
		
		if (post.getSubject() != null && post.getSubject().length() > 100)
		{
			post.setSubject(post.getSubject().substring(0, 99));
		}
		
		//save post text
		insertPostText(post);
		
		if (logger.isDebugEnabled())
		{
			logger.debug("End : addNew");
		}
		
		return postId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void delete(int postId)
	{
		deletePostText(postId);
		deletePost(postId);
	}
	
	/**
	 * @param sqlService
	 *          The sqlService to set
	 */
	public void setSqlService(SqlService sqlService)
	{
		this.sqlService = sqlService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void update(Post post)
	{
		if (post == null)
		{
			throw new IllegalArgumentException("Post information is required.");
		}
		
		if (post.getId() <= 0 || post.getTopicId() <= 0 || post.getForumId() <= 0)
		{
			throw new IllegalArgumentException("Post cannot be updated as post has no post or topic or forum id");
		}
		
		// update post
		updatePost(post);
		
		// update post text
		updatePostText(post);
	}
	
	/**
	 * Deletes post
	 * 
	 * @param postId	Post id
	 */
	protected void deletePost(int postId)
	{
		String sql = "DELETE FROM jforum_posts WHERE post_id = ?";
		
		Object[] fields = new Object[1];
		
		int i = 0;
		
		fields[i++] = postId;
				
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("deletePostText: db write failed");
		}		
	}
	
	/**
	 * Deletes post text
	 * 
	 * @param postId	post id
	 */
	protected void deletePostText(int postId)
	{
		String sql = "DELETE FROM jforum_posts_text WHERE post_id = ?";
		
		Object[] fields = new Object[1];
		
		int i = 0;
		
		fields[i++] = postId;
				
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("deletePostText: db write failed");
		}		
	}
	
	/**
	 * save new post
	 * 
	 * @param post	post 
	 * 
	 * @return	New post id
	 */
	protected abstract int insertPost(Post post);
	
	/**
	 * saves post text
	 *
	 * @param post	post
	 */
	protected abstract void insertPostText(Post post);
	
	/**
	 * updates post
	 * 
	 * @param post	Post
	 */
	protected void updatePost(Post post)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE jforum_posts SET topic_id = ?, forum_id = ?, enable_bbcode = ?, enable_html = ?, enable_smilies = ?, enable_sig = ?, ");
		sql.append("post_edit_time = ?, post_edit_count = post_edit_count + 1, poster_ip = ? WHERE post_id = ?");
		
		Object[] fields = new Object[9];
		
		int i = 0;
		Date now = new Date();
		
		fields[i++] = post.getTopicId();
		fields[i++] = post.getForumId();
		fields[i++] = (post.isBbCodeEnabled() ? 1 : 0);
		fields[i++] = (post.isHtmlEnabled() ? 1 : 0);
		fields[i++] = (post.isSmiliesEnabled() ? 1 : 0);
		fields[i++] = (post.isSignatureEnabled() ? 1 : 0);
		fields[i++] = new Timestamp(now.getTime());
		fields[i++] = post.getUserIp();
		fields[i++] = post.getId();
				
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("updatePost: db write failed");
		}		
	}
	
	/**
	 * updates post text
	 * 
	 * @param post	Post object
	 */
	abstract protected void updatePostText(Post post);
}
