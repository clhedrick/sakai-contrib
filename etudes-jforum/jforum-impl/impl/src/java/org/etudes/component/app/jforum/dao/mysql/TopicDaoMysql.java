/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/mysql/TopicDaoMysql.java $ 
 * $Id: TopicDaoMysql.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011, 2012, 2013 Etudes, Inc. 
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.Topic;
import org.etudes.api.app.jforum.dao.TopicDao;
import org.etudes.component.app.jforum.dao.generic.TopicDaoGeneric;
import org.sakaiproject.db.api.SqlReader;

public class TopicDaoMysql extends TopicDaoGeneric implements TopicDao
{
	private static Log logger = LogFactory.getLog(TopicDaoMysql.class);
	
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
	protected List<Topic> getForumTopics(int forumId, int startFrom, int count)
	{
		if (startFrom < 0 || count < 0)
		{
			return new ArrayList<Topic>();
		}
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		
		if (startFrom == 0 && count == 0)
		{
			sql.append("SELECT t.topic_id, t.forum_id, t.topic_title, t.user_id, t.topic_time, t.topic_replies, t.topic_status, t.topic_vote, t.topic_type, t.topic_first_post_id, ");
			sql.append("t.topic_last_post_id, t.moderated, t.topic_grade, t.topic_export, t.start_date, t.hide_until_open, t.end_date, t.allow_until_date, t.lock_end_date, u.username AS posted_by_username, u.sakai_user_id AS posted_sakai_user_id, ");
			sql.append("u.user_id AS posted_by_id, u.user_fname AS posted_by_fname, u.user_lname AS posted_by_lname, u.user_avatar AS posted_by_avatar, u2.username AS last_post_by_username, u2.sakai_user_id AS last_post_sakai_user_id, ");
			sql.append("u2.user_id AS last_post_by_id, u2.user_fname AS last_post_by_fname, u2.user_lname AS last_post_by_lname, u2.user_avatar AS last_post_by_avatar, p2.post_time, p.attach ");
			sql.append("FROM jforum_topics t, jforum_users u, jforum_posts p, jforum_posts p2, jforum_users u2 ");
			sql.append("WHERE t.forum_id = ? ");
			sql.append("AND t.user_id = u.user_id ");
			sql.append("AND p.post_id = t.topic_first_post_id ");
			sql.append("AND p2.post_id = t.topic_last_post_id ");
			sql.append("AND u2.user_id = p2.user_id ");
			sql.append("ORDER BY t.topic_export DESC, t.topic_type DESC, p2.post_time DESC, t.topic_last_post_id DESC ");
		
			int i = 0; 
			fields = new Object[1];
			fields[i++] = forumId;
		}
		else
		{
			sql.append("SELECT t.topic_id, t.forum_id, t.topic_title, t.user_id, t.topic_time, t.topic_replies, t.topic_status, t.topic_vote, t.topic_type, t.topic_first_post_id, ");
			sql.append("t.topic_last_post_id, t.moderated, t.topic_grade, t.topic_export, t.start_date, t.hide_until_open, t.end_date, t.allow_until_date, t.lock_end_date, u.username AS posted_by_username, u.sakai_user_id AS posted_sakai_user_id, ");
			sql.append("u.user_id AS posted_by_id, u.user_fname AS posted_by_fname, u.user_lname AS posted_by_lname, u.user_avatar AS posted_by_avatar, u2.username AS last_post_by_username, u2.sakai_user_id AS last_post_sakai_user_id, ");
			sql.append("u2.user_id AS last_post_by_id, u2.user_fname AS last_post_by_fname, u2.user_lname AS last_post_by_lname, u2.user_avatar AS last_post_by_avatar, p2.post_time, p.attach ");
			sql.append("FROM jforum_topics t, jforum_users u, jforum_posts p, jforum_posts p2, jforum_users u2 ");
			sql.append("WHERE t.forum_id = ? ");
			sql.append("AND t.user_id = u.user_id ");
			sql.append("AND p.post_id = t.topic_first_post_id ");
			sql.append("AND p2.post_id = t.topic_last_post_id ");
			sql.append("AND u2.user_id = p2.user_id ");
			sql.append("ORDER BY t.topic_export DESC, t.topic_type DESC, p2.post_time DESC, t.topic_last_post_id DESC ");
			sql.append("LIMIT ?, ? ");
		
			int i = 0; 
			fields = new Object[3];
			fields[i++] = forumId;
			fields[i++] = startFrom;
			fields[i++] = count;
		}
		List<Topic> topics = readTopics(sql.toString(), fields);
		
		// topics special access
		for (Topic topic : topics)
		{
			if ((topic.getAccessDates() != null) && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null) || (topic.getAccessDates().getAllowUntilDate() != null)))
			{
				List<SpecialAccess> topicSpecialAccess = specialAccessDao.selectByTopic(topic.getForumId(), topic.getId());
				topic.getSpecialAccess().addAll(topicSpecialAccess);
			}
			
			// topic grade
			if (topic.isGradeTopic())
			{
				Grade grade = gradeDao.selectByForumTopic(forumId, topic.getId());
				topic.setGrade(grade);
			}
		}
		
		return topics;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected Post getPostById(int postId)
	{
		final List<Post> posts = new ArrayList<Post>();
		
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		int i = 0;
		fields = new Object[1];
		fields[i++] = postId;
		
		sql.append("SELECT p.post_id, p.topic_id, p.forum_id, p.user_id, p.post_time, p.poster_ip, p.enable_bbcode, p.attach, p.need_moderate, ");
		sql.append("p.enable_html, p.enable_smilies, p.enable_sig, p.post_edit_time, p.post_edit_count, p.status, pt.post_subject, pt.post_text, ");
		//sql.append(" u.username, u.sakai_user_id, u.user_fname, u.user_lname, u.user_avatar, u.user_attachsig, u.user_sig ");
		
		sql.append("u.user_id, u.username, u.user_regdate, u.user_new_privmsg, u.user_unread_privmsg, u.user_last_privmsg, u.user_viewemail, u.user_attachsig, ");
		sql.append("u.user_allowhtml, u.user_allow_pm, u.user_notify, u.user_notify_pm, u.user_avatar, u.user_avatar_type, u.user_email, u.user_icq, ");
		sql.append("u.user_website, u.user_from, u.user_sig, u.user_sig_bbcode_uid, u.user_aim, u.user_yim, u.user_msnm, u.user_occ, u.user_interests, u.gender, u.user_fname, ");
		sql.append("u.user_lname, u.sakai_user_id, u.user_facebook_account, u.user_twitter_account, u.user_lang ");
		
		sql.append("FROM jforum_posts p, jforum_posts_text pt, jforum_users u ");
		sql.append("WHERE p.post_id = pt.post_id ");
		sql.append("AND p.post_id = ? ");
		sql.append("AND p.user_id = u.user_id ");
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Post post = makePost(result);
					
					posts.add(post);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getPostById: " + e, e);
					}
					return null;
				}
			}
		});
		
		Post post = null;
		if (posts.size() == 1)
		{
			post = posts.get(0);
		}
		
		return post;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected List<Post> getPostsByCategoryByUser(int categoryId, int userId)
	{
		if (categoryId <= 0 || userId <= 0)
		{
			throw new IllegalArgumentException("category and user iformation is missing.");
		}
		
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		
		final List<Post> posts = new ArrayList<Post>();
		
		sql.append("SELECT p.post_id, topic_id, p.forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, ");
		sql.append("enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, p.need_moderate, ");
		//sql.append("u.username, u.sakai_user_id, u.user_fname, u.user_lname, u.user_avatar, u.user_attachsig, u.user_sig ");
		sql.append("u.user_id, u.username, u.user_regdate, u.user_new_privmsg, u.user_unread_privmsg, u.user_last_privmsg, u.user_viewemail, u.user_attachsig, ");
		sql.append("u.user_allowhtml, u.user_allow_pm, u.user_notify, u.user_notify_pm, u.user_avatar, u.user_avatar_type, u.user_email, u.user_icq, ");
		sql.append("u.user_website, u.user_from, u.user_sig, u.user_sig_bbcode_uid, u.user_aim, u.user_yim, u.user_msnm, u.user_occ, u.user_interests, u.gender, u.user_fname, ");
		sql.append("u.user_lname, u.sakai_user_id, u.user_facebook_account, u.user_twitter_account, u.user_lang ");
		sql.append("FROM jforum_posts p, jforum_posts_text pt, jforum_users u, jforum_forums f ");
		sql.append("WHERE p.post_id = pt.post_id ");
		sql.append("AND p.forum_id = f.forum_id ");
		sql.append("AND f.categories_id = ? ");
		sql.append("AND p.user_id = ? ");
		sql.append("AND p.user_id = u.user_id ");
		sql.append("AND p.need_moderate = 0 ");
		sql.append("ORDER BY p.forum_id ASC, p.topic_id ASC, p.post_time ASC ");
		
		int i = 0;
		fields = new Object[2];
		fields[i++] = categoryId;
		fields[i++] = userId;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Post post = makePost(result);
					
					posts.add(post);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getPostsByCategoryByUser: " + e, e);
					}
					return null;
				}
			}
		});
		
		return posts;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected List<Post> getPostsByForumByUser(int forumId, int userId)
	{
		if (forumId <= 0 || userId <= 0)
		{
			throw new IllegalArgumentException("category and user iformation is missing.");
		}
		
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		
		final List<Post> posts = new ArrayList<Post>();
		
		sql.append("SELECT p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, ");
		sql.append("enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username, p.need_moderate, ");
		//sql.append("u.username, u.sakai_user_id, u.user_fname, u.user_lname, u.user_avatar, u.user_attachsig, u.user_sig ");		
		sql.append("u.user_id, u.username, u.user_regdate, u.user_new_privmsg, u.user_unread_privmsg, u.user_last_privmsg, u.user_viewemail, u.user_attachsig, ");
		sql.append("u.user_allowhtml, u.user_allow_pm, u.user_notify, u.user_notify_pm, u.user_avatar, u.user_avatar_type, u.user_email, u.user_icq, ");
		sql.append("u.user_website, u.user_from, u.user_sig, u.user_sig_bbcode_uid, u.user_aim, u.user_yim, u.user_msnm, u.user_occ, u.user_interests, u.gender, u.user_fname, ");
		sql.append("u.user_lname, u.sakai_user_id, u.user_facebook_account, u.user_twitter_account, u.user_lang ");		
		sql.append("FROM jforum_posts p, jforum_posts_text pt, jforum_users u ");
		sql.append("WHERE p.post_id = pt.post_id ");
		sql.append("AND p.forum_id = ? ");
		sql.append("AND p.user_id = ? ");
		sql.append("AND p.user_id = u.user_id ");
		sql.append("AND p.need_moderate = 0 ");
		sql.append("ORDER BY p.post_time ASC");
		
		int i = 0;
		fields = new Object[2];
		fields[i++] = forumId;
		fields[i++] = userId;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Post post = makePost(result);
					
					posts.add(post);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getPostsByForumByUser: " + e, e);
					}
					return null;
				}
			}
		});
		
		return posts;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected List<Post> getPostsByTopicByUser(int topicId, int userId)
	{
		if (topicId <= 0 || userId <= 0)
		{
			throw new IllegalArgumentException("category and user iformation is missing.");
		}
		
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		
		final List<Post> posts = new ArrayList<Post>();
		
		sql.append("SELECT p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, ");
		sql.append("enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username, p.need_moderate, ");
		//sql.append("u.username, u.sakai_user_id, u.user_fname, u.user_lname, u.user_avatar, u.user_attachsig, u.user_sig ");		
		sql.append("u.user_id, u.username, u.user_regdate, u.user_new_privmsg, u.user_unread_privmsg, u.user_last_privmsg, u.user_viewemail, u.user_attachsig, ");
		sql.append("u.user_allowhtml, u.user_allow_pm, u.user_notify, u.user_notify_pm, u.user_avatar, u.user_avatar_type, u.user_email, u.user_icq, ");
		sql.append("u.user_website, u.user_from, u.user_sig, u.user_sig_bbcode_uid, u.user_aim, u.user_yim, u.user_msnm, u.user_occ, u.user_interests, u.gender, u.user_fname, ");
		sql.append("u.user_lname, u.sakai_user_id, u.user_facebook_account, u.user_twitter_account, u.user_lang ");		
		sql.append("FROM jforum_posts p, jforum_posts_text pt, jforum_users u  ");
		sql.append("WHERE p.post_id = pt.post_id  ");
		sql.append("AND p.topic_id = ?  ");
		sql.append("AND p.user_id = ?  ");
		sql.append("AND p.user_id = u.user_id  ");
		sql.append("AND p.need_moderate = 0  ");
		sql.append("ORDER BY p.topic_id ASC, p.post_time ASC");
		
		int i = 0;
		fields = new Object[2];
		fields[i++] = topicId;
		fields[i++] = userId;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Post post = makePost(result);
					
					posts.add(post);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getPostsByTopicByUser: " + e, e);
					}
					return null;
				}
			}
		});
		
		return posts;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected String getPostTextFromResultSet(ResultSet rs) throws SQLException
	{
		return rs.getString("post_text");
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected List<Topic> getRecentTopics(String context, int limit)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t.topic_id, t.forum_id, t.topic_title, t.user_id, t.topic_time, t.topic_replies, t.topic_status, t.topic_vote, t.topic_type, t.topic_first_post_id, ");
		sql.append("t.topic_last_post_id, t.moderated, t.topic_grade, t.topic_export, t.start_date, t.hide_until_open, t.end_date, t.allow_until_date, t.lock_end_date, u.username AS posted_by_username, u.sakai_user_id AS posted_sakai_user_id, "); 
		sql.append("u.user_id AS posted_by_id, u.user_fname AS posted_by_fname, u.user_lname AS posted_by_lname, u.user_avatar AS posted_by_avatar, u2.username AS last_post_by_username, u2.sakai_user_id AS last_post_sakai_user_id, "); 
		sql.append("u2.user_id AS last_post_by_id, u2.user_fname AS last_post_by_fname, u2.user_lname AS last_post_by_lname, u2.user_avatar AS last_post_by_avatar, p2.post_time, p.attach "); 
		sql.append("FROM jforum_topics t, jforum_users u, jforum_posts p, jforum_posts p2, jforum_users u2, ");
		sql.append("jforum_forums f, jforum_sakai_course_categories c, jforum_categories jc ");
		sql.append("WHERE t.user_id = u.user_id "); 
		sql.append("AND p.post_id = t.topic_first_post_id "); 
		sql.append("AND p2.post_id = t.topic_last_post_id ");
		sql.append("AND u2.user_id = p2.user_id "); 
		sql.append("AND t.forum_id = f.forum_id ");
		sql.append("AND c.course_id = ? ");
		sql.append("AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = jc.categories_id ");
		sql.append("AND jc.archived = 0 "); 
		sql.append("ORDER BY p2.post_time DESC, t.topic_last_post_id DESC ");
		if (limit > 0)
		{
			sql.append("LIMIT 0, ? ");
		}
		
		int i = 0; 
		Object[] fields = null;
		if (limit > 0)
		{
			fields = new Object[2];
			fields[i++] = context;
			fields[i++] = limit;
		}
		else
		{
			fields = new Object[1];
			fields[i++] = context;
		}
		
		
		List<Topic> topics = readTopics(sql.toString(), fields);
		
		// topic special access
		for (Topic topic : topics)
		{
			if ((topic.isGradeTopic()) && (topic.getAccessDates() != null) && (topic.getAccessDates().getOpenDate() != null || topic.getAccessDates().getDueDate() != null))
			{
				topic.getSpecialAccess().addAll(specialAccessDao.selectByTopic(topic.getForumId(), topic.getId()));
			}
		}
		
		return topics;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected List<Post> getTopicPostsByLimit(int topicId, int startFrom, int count)
	{
		if (startFrom < 0 || count < 0)
		{
			return new ArrayList<Post>();
		}
					
		if (topicId == 0) throw new IllegalArgumentException();
		
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		
		final List<Post> posts = new ArrayList<Post>();
		
		if (startFrom == 0 && count == 0)
		{
			sql.append("SELECT p.post_id, p.topic_id, p.forum_id, p.user_id, p.post_time, p.poster_ip, p.enable_bbcode, p.attach, p.need_moderate, ");
			sql.append("p.enable_html, p.enable_smilies, p.enable_sig, p.post_edit_time, p.post_edit_count, p.status, pt.post_subject, pt.post_text, ");
			//sql.append("u.username, u.sakai_user_id, u.user_fname, u.user_lname, u.user_avatar, u.user_attachsig, u.user_sig ");			
			sql.append("u.user_id, u.username, u.user_regdate, u.user_new_privmsg, u.user_unread_privmsg, u.user_last_privmsg, u.user_viewemail, u.user_attachsig, ");
			sql.append("u.user_allowhtml, u.user_allow_pm, u.user_notify, u.user_notify_pm, u.user_avatar, u.user_avatar_type, u.user_email, u.user_icq, ");
			sql.append("u.user_website, u.user_from, u.user_sig, u.user_sig_bbcode_uid, u.user_aim, u.user_yim, u.user_msnm, u.user_occ, u.user_interests, u.gender, u.user_fname, ");
			sql.append("u.user_lname, u.sakai_user_id, u.user_facebook_account, u.user_twitter_account, u.user_lang ");			
			sql.append("FROM jforum_posts p, jforum_posts_text pt, jforum_users u ");
			sql.append("WHERE p.post_id = pt.post_id ");
			sql.append("AND topic_id = ? ");
			sql.append("AND p.user_id = u.user_id ");
			sql.append("AND p.need_moderate = 0 ");
			sql.append("ORDER BY p.post_time ASC ");
			
			int i = 0;
			fields = new Object[1];
			fields[i++] = topicId;
		}
		else
		{
			sql.append("SELECT p.post_id, p.topic_id, p.forum_id, p.user_id, p.post_time, p.poster_ip, p.enable_bbcode, p.attach, p.need_moderate, ");
			sql.append("p.enable_html, p.enable_smilies, p.enable_sig, p.post_edit_time, p.post_edit_count, p.status, pt.post_subject, pt.post_text, ");
			//sql.append("u.username, u.sakai_user_id, u.user_fname, u.user_lname, u.user_avatar,  u.user_attachsig, u.user_sig ");			
			sql.append("u.user_id, u.username, u.user_regdate, u.user_new_privmsg, u.user_unread_privmsg, u.user_last_privmsg, u.user_viewemail, u.user_attachsig, ");
			sql.append("u.user_allowhtml, u.user_allow_pm, u.user_notify, u.user_notify_pm, u.user_avatar, u.user_avatar_type, u.user_email, u.user_icq, ");
			sql.append("u.user_website, u.user_from, u.user_sig, u.user_sig_bbcode_uid, u.user_aim, u.user_yim, u.user_msnm, u.user_occ, u.user_interests, u.gender, u.user_fname, ");
			sql.append("u.user_lname, u.sakai_user_id, u.user_facebook_account, u.user_twitter_account, u.user_lang ");			
			sql.append("FROM jforum_posts p, jforum_posts_text pt, jforum_users u ");
			sql.append("WHERE p.post_id = pt.post_id ");
			sql.append("AND topic_id = ? ");
			sql.append("AND p.user_id = u.user_id ");
			sql.append("AND p.need_moderate = 0 ");
			sql.append("ORDER BY p.post_time ASC ");
			sql.append("LIMIT ?, ?");
			
			int i = 0;
			fields = new Object[3];
			fields[i++] = topicId;
			fields[i++] = startFrom;
			fields[i++] = count;
		}
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Post post = makePost(result);
					
					posts.add(post);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getTopicPostsByLimit: " + e, e);
					}
					return null;
				}
			}
		});
		
		return posts;
	
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int insertTopic(Topic topic)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO jforum_topics (forum_id, topic_title, user_id, topic_time, topic_first_post_id, topic_last_post_id, topic_type, ");
		//sql.append("topic_status, topic_grade, topic_export, start_date, end_date, lock_end_date) ");
		sql.append("topic_status, topic_grade, topic_export, start_date, hide_until_open, end_date, allow_until_date) ");
		sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		Object[] fields = new Object[14];
		int i = 0;
		fields[i++] = topic.getForumId();
		fields[i++] = topic.getTitle();
		fields[i++] = topic.getPostedBy().getId();
		if (topic.getTime() != null)
		{
			fields[i++] = new Timestamp(topic.getTime().getTime());
		}
		else
		{
			Date now = new Date();
			fields[i++] = new Timestamp(now.getTime());
		}
		fields[i++] = topic.getFirstPostId();
		fields[i++] = topic.getLastPostId();
		fields[i++] = topic.getType();
		fields[i++] = topic.getStatus();
		fields[i++] = topic.isGradeTopic() ? 1 : 0;
		fields[i++] = topic.isExportTopic() ? 1 : 0;
		
		if (topic.getAccessDates() != null)
		{
			if (topic.getAccessDates().getOpenDate() == null)
			{
				fields[i++] = null;
				fields[i++] = 0;
			} 
			else
			{
				fields[i++] = new Timestamp(topic.getAccessDates().getOpenDate().getTime());
				fields[i++] = topic.getAccessDates().isHideUntilOpen() ? 1 : 0;
			}
	
			if (topic.getAccessDates().getDueDate() == null)
			{
				fields[i++] = null;
			} 
			else
			{
				fields[i++] = new Timestamp(topic.getAccessDates().getDueDate().getTime());
			}
			
			if (topic.getAccessDates().getAllowUntilDate() == null)
			{
				fields[i++] = null;
			} 
			else
			{
				fields[i++] = new Timestamp(topic.getAccessDates().getAllowUntilDate().getTime());
			}
		}
		else
		{
			fields[i++] = null;
			fields[i++] = 0;
			fields[i++] = null;
			fields[i++] = null;
		}
		
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
			throw new RuntimeException("insertTopic: dbInsert failed");
		}
		
		return id.intValue();
	}
}
