/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/generic/TopicDaoGeneric.java $ 
 * $Id: TopicDaoGeneric.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.AccessDates;
import org.etudes.api.app.jforum.Attachment;
import org.etudes.api.app.jforum.AttachmentExtension;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.JForumForumService;
import org.etudes.api.app.jforum.JForumPostService;
import org.etudes.api.app.jforum.LastPostInfo;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.Topic;
import org.etudes.api.app.jforum.User;
import org.etudes.api.app.jforum.dao.AttachmentDao;
import org.etudes.api.app.jforum.dao.ForumDao;
import org.etudes.api.app.jforum.dao.GradeDao;
import org.etudes.api.app.jforum.dao.PostDao;
import org.etudes.api.app.jforum.dao.SpecialAccessDao;
import org.etudes.api.app.jforum.dao.TopicDao;
import org.etudes.component.app.jforum.AccessDatesImpl;
import org.etudes.component.app.jforum.AttachmentExtensionImpl;
import org.etudes.component.app.jforum.AttachmentImpl;
import org.etudes.component.app.jforum.AttachmentInfoImpl;
import org.etudes.component.app.jforum.LastPostInfoImpl;
import org.etudes.component.app.jforum.PostImpl;
import org.etudes.component.app.jforum.TopicImpl;
import org.etudes.component.app.jforum.UserImpl;
import org.etudes.util.HtmlHelper;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;

public abstract class TopicDaoGeneric implements TopicDao
{
	private static Log logger = LogFactory.getLog(TopicDaoGeneric.class);
	
	/** Dependency:AttachmentDao */
	protected AttachmentDao attachmentDao = null;
	
	/** Dependency: ForumDao */
	protected ForumDao forumDao = null;
	
	/** Dependency: GradeDao */
	protected GradeDao gradeDao = null;
	
	/** Dependency: JForumForumService. */
	protected JForumForumService jforumForumService = null;
	
	/** Dependency: JForumPostService. */
	protected JForumPostService jforumPostService = null;
	
	/** Dependency: PostDao */
	protected PostDao postDao = null;
	
	/** Dependency: SqlService */
	protected SpecialAccessDao specialAccessDao = null;
	
	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	/**
	 * {@inheritDoc}
	 */
	public int addNew(Topic topic)
	{
		if (topic == null)
		{
			throw new IllegalArgumentException();
		}
		
		if (topic.getId() > 0 || topic.getForumId() <= 0)
		{
			throw new IllegalArgumentException("New topic cannot be created as topic may be having id or has no forum id");
		}
		
		if (topic.getPostedBy() == null || topic.getPostedBy().getId() <= 0)
		{
			throw new IllegalArgumentException("Topic data has no proper postedby information");
		}
		
		if (topic.getPosts().size() != 1)
		{
			throw new IllegalArgumentException("Post data in needed to create the topic");
		}
		
		Post post = topic.getPosts().get(0);
		if (post.getPostedBy() == null || post.getPostedBy().getId() <= 0)
		{
			throw new IllegalArgumentException("Post data has no proper postedby information");
		}
		
		int topicId = creatNewTopicTx(topic);
		
		return topicId;
	}

	/**
	 * {@inheritDoc}
	 */
	public int addNewTopicPost(Topic topic)
	{
		if (topic == null)
		{
			throw new IllegalArgumentException();
		}
		
		if (topic.getFirstPostId() <= 0 || topic.getLastPostId() <= 0)
		{
			throw new IllegalArgumentException("Topic first and last post information is needed.");
		}
		
		if (topic.getPosts().size() != 1)
		{
			throw new IllegalArgumentException("Post data in needed to create the topic");
		}
		
		Post post = topic.getPosts().get(0);
		if (post.getPostedBy() == null || post.getPostedBy().getId() <= 0)
		{
			throw new IllegalArgumentException("Post data has no proper postedby information");
		}
		
		if (post.getTopicId() <= 0 || post.getForumId() <= 0)
		{
			throw new IllegalArgumentException("Post data has no proper topic and forum information");
		}
		
		int postId = addNewTopicPostTx(topic);

		return postId;
	}

	public void decrementTotalReplies(int topicId)
	{
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE jforum_topics SET topic_replies = topic_replies - 1 WHERE topic_id = ?");
		
		Object[] fields = new Object[1];
		int i = 0;

		fields[i++] = topicId;
		
		if (!sqlService.dbWrite(null, sql.toString(), fields))
		{
			throw new RuntimeException("decrementTotalReplies: db write failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteTopic(int topicId)
	{
		Topic topic = selectById(topicId);
		
		deleteTopicTx(topic);
	}
	
	/**
	 * Deletes topic post and it's associations
	 * 
	 * @param postId Post id
	 */
	public void deleteTopicPost(int postId)
	{
		deletePostTx(postId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void incrementTotalReplies(int topicId)
	{
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE jforum_topics SET topic_replies = topic_replies + 1 WHERE topic_id = ?");
		
		Object[] fields = new Object[1];
		int i = 0;

		fields[i++] = topicId;
		
		if (!sqlService.dbWrite(null, sql.toString(), fields))
		{
			throw new RuntimeException("incrementTotalReplies: db write failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isUserSubscribed(int topicId, int userId)
	{
		String sql = "SELECT user_id FROM jforum_topics_watch WHERE topic_id = ? AND user_id = ?";
				
		Object[] fields;
		int i = 0;
		
		fields = new Object[2];
		fields[i++] = topicId;
		fields[i++] = userId;
		
		final List<Integer> subscribedList = new ArrayList<Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					subscribedList.add(result.getInt("user_id"));

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("isUserSubscribed: " + e, e);
					}
					return null;
				}
			}
		});
		
		boolean subscribed = false;
		if (subscribedList.size() > 0)
		{
			subscribed = true;
		}
		return subscribed;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void lockUnlock(int topicId, Topic.TopicStatus status)
	{
		String sql = "UPDATE jforum_topics SET topic_status = ? WHERE topic_id = ?";
		
		Object[] fields = new Object[2];
		int i = 0;	
		
		fields[i++] = status.getStatus();
		fields[i++] = topicId;		
			
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("lockUnlock: db write failed");
		}	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void moveTopic(int topicId, int toForumId)
	{
		if (topicId <= 0 || toForumId <= 0)
		{
			return;
		}
		
		Topic topic = selectById(topicId);
		
		if (topic == null)
		{
			return;
		}
		
		moveTopicTx(topic, toForumId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<User> notifyUsers(int topicId, int posterId)
	{
		final List<User> users = new ArrayList<User>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT u.user_id AS user_id, u.username AS username, u.user_lang AS user_lang, u.user_email AS user_email, u.sakai_user_id ");
		sql.append("FROM jforum_topics_watch tw ");
		sql.append("INNER JOIN jforum_users u ON (tw.user_id = u.user_id) ");
		sql.append("WHERE tw.topic_id = ? ");
		sql.append("AND tw.is_read = 1 ");
		sql.append("AND u.user_id <> ?");
		//sql.append("AND u.user_id NOT IN ( ?, ? )");
	
		int i = 0;
		Object[] fields = new Object[2];
		fields[i++] = topicId;
		fields[i++] = posterId;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					UserImpl user = new UserImpl();
					
					user.setId(result.getInt("user_id"));
					user.setUsername(result.getString("username"));
					user.setLang(result.getString("user_lang"));
					user.setEmail(result.getString("user_email")==null ? "" : result.getString("user_email"));
					user.setSakaiUserId(result.getString("sakai_user_id"));
					
					users.add(user);
	
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("notifyUsers() - select TopicWatchUsers: " + e, e);
					}
					return null;
				}
			}
		});
		
		// Set read status to false
		//String sqlStatus = "UPDATE jforum_topics_watch SET is_read = '0' WHERE topic_id = ? AND user_id NOT IN (?, ?)";
		String sqlStatus = "UPDATE jforum_topics_watch SET is_read = '0' WHERE topic_id = ? AND user_id <> ?";
	
		i = 0;
		fields = new Object[2];
		fields[i++] = topicId;
		fields[i++] = posterId;
		
		if (!sqlService.dbWrite(sqlStatus, fields)) 
		{
			throw new RuntimeException("notifyUsers() - set read status to false: db write failed");
		}
		
		return users;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeSubscription(int topicId)
	{
		String sql = "DELETE FROM jforum_topics_watch WHERE topic_id = ? AND user_id = ?";
		
		Object[] fields;
		int i = 0;
		
		fields = new Object[1];
		fields[i++] = topicId;
		
		if (!sqlService.dbWrite(sql, fields)) 
		{
			throw new RuntimeException("removeSubscription() - db write failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeUserSubscription(int topicId,  int userId)
	{
		String sql = "DELETE FROM jforum_topics_watch WHERE topic_id = ? AND user_id = ?";
		
		Object[] fields;
		int i = 0;
		
		fields = new Object[2];
		fields[i++] = topicId;
		fields[i++] = userId;
		
		if (!sqlService.dbWrite(sql, fields)) 
		{
			throw new RuntimeException("removeUserSubscription() - db write failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Topic selectById(int topicId)
	{
		
		Topic topic = null;
		
		StringBuilder sql = new StringBuilder();
				
		sql.append("SELECT t.topic_id, t.forum_id, t.topic_title, t.user_id, t.topic_time, t.topic_replies, t.topic_status, t.topic_type, t.topic_first_post_id, t.topic_last_post_id, t.topic_grade, t.topic_export, t.start_date, t.hide_until_open, t.end_date, t.allow_until_date, t.lock_end_date, ");
		sql.append("u.username AS posted_by_username, u.user_id AS posted_by_id, u.sakai_user_id AS posted_sakai_user_id, u.user_fname AS posted_by_fname, u.user_lname AS posted_by_lname, u.user_avatar AS posted_by_avatar, u2.username AS last_post_by_username, u2.user_id AS last_post_by_id, u2.sakai_user_id AS last_post_sakai_user_id, u2.user_fname AS last_post_by_fname,u2.user_lname AS last_post_by_lname, u2.user_avatar AS last_post_by_avatar, p2.post_time, p.attach ");
		sql.append("FROM jforum_topics t, jforum_users u, jforum_posts p, jforum_posts p2, jforum_users u2 ");
		sql.append("WHERE t.topic_id = ? ");
		sql.append("AND t.user_id = u.user_id ");
		sql.append("AND p.post_id = t.topic_first_post_id ");
		sql.append("AND p2.post_id = t.topic_last_post_id ");
		sql.append("AND u2.user_id = p2.user_id");
				
		Object[] fields;
		int i = 0;
		
		fields = new Object[1];
		fields[i++] = topicId;

		List<Topic> topics = readTopics(sql.toString(), fields);
		
		if (topics.size() == 1)
		{
			topic = topics.get(0);
		}
		
		if (topic != null)
		{
			// special access
			if ((topic.getAccessDates() != null) && (topic.getAccessDates().getOpenDate() != null || topic.getAccessDates().getDueDate() != null || topic.getAccessDates().getAllowUntilDate() != null))
			{
				List<SpecialAccess> specialAccess = specialAccessDao.selectByTopic(topic.getForumId(), topic.getId());
				topic.getSpecialAccess().addAll(specialAccess);
			}
			
			// grades
			if (topic.isGradeTopic())
			{
				Grade grade = gradeDao.selectByForumTopic(topic.getForumId(), topic.getId());
				topic.setGrade(grade);
			}
		}
		
		return topic;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> selectForumExportTopics(int forumId)
	{
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		
		sql.append("SELECT t.topic_id, t.forum_id, t.topic_title, t.user_id, t.topic_time, t.topic_replies, t.topic_status, t.topic_vote, t.topic_type, t.topic_first_post_id, ");
		sql.append("t.topic_last_post_id, t.moderated, t.topic_grade, t.topic_export, t.start_date, t.hide_until_open, t.end_date, t.allow_until_date, t.lock_end_date, u.username AS posted_by_username, u.sakai_user_id AS posted_sakai_user_id, ");
		sql.append("u.user_id AS posted_by_id, u.user_fname AS posted_by_fname, u.user_lname AS posted_by_lname, u.user_avatar AS posted_by_avatar, u2.username AS last_post_by_username, u2.sakai_user_id AS last_post_sakai_user_id, ");
		sql.append("u2.user_id AS last_post_by_id, u2.user_fname AS last_post_by_fname, u2.user_lname AS last_post_by_lname, u2.user_avatar AS last_post_by_avatar, p2.post_time, p.attach ");
		sql.append("FROM jforum_topics t, jforum_users u, jforum_posts p, jforum_posts p2, jforum_users u2 ");
		sql.append("WHERE t.forum_id = ? AND topic_export = ? ");
		sql.append("AND t.user_id = u.user_id ");
		sql.append("AND p.post_id = t.topic_first_post_id ");
		sql.append("AND p2.post_id = t.topic_last_post_id ");
		sql.append("AND u2.user_id = p2.user_id ");
		sql.append("ORDER BY t.topic_export DESC, t.topic_type DESC, p2.post_time DESC, t.topic_last_post_id DESC ");
	
		int i = 0; 
		fields = new Object[2];
		fields[i++] = forumId;
		fields[i++] = Topic.TopicExportImportCode.YES.getTopicExportImportCode();
		
		List<Topic> topics = readTopics(sql.toString(), fields);
		
		// topics special access
		for (Topic topic : topics)
		{
			// topic grade
			if (topic.isGradeTopic())
			{
				Grade grade = gradeDao.selectByForumTopic(topic.getForumId(), topic.getId());
				topic.setGrade(grade);
			}
			
			// special access
			if ((topic.getAccessDates() != null) && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null || topic.getAccessDates().getAllowUntilDate() != null)))
			{
				List<SpecialAccess> topicSpecialAccess = specialAccessDao.selectByTopic(topic.getForumId(), topic.getId());
				topic.getSpecialAccess().addAll(topicSpecialAccess);
			}
		}
				
		return topics;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public LastPostInfo selectForumLastPostInfo(int forumId, boolean invisbleTopics)
	{
		LastPostInfo lastPostInfo = null;
		
		final List<LastPostInfo> lastPostInfoList = new ArrayList<LastPostInfo>();
		
		StringBuilder sql = new StringBuilder();
		Object[] fields = null;
		int i = 0;
		
		if (invisbleTopics)
		{
			sql.append("SELECT post_time, p.topic_id, t.topic_replies, post_id, u.user_id, u.username, u.user_fname, u.user_lname ");
			sql.append("FROM jforum_posts p, jforum_users u, jforum_topics t , jforum_forums f WHERE t.forum_id = f.forum_id ");
			sql.append("AND t.topic_id = p.topic_id AND f.forum_last_post_id = t.topic_last_post_id AND ");
			sql.append("t.topic_last_post_id = p.post_id AND p.forum_id = ? AND p.user_id = u.user_id");
			
			fields = new Object[1];
			fields[i++] = forumId;
		}
		else
		{
			sql.append("SELECT post_time, p.topic_id, t.topic_replies, post_id, u.user_id, u.username, u.user_fname, u.user_lname ");
			sql.append("FROM jforum_posts p, jforum_users u, jforum_topics t WHERE t.topic_id = p.topic_id AND t.topic_last_post_id = p.post_id ");
			sql.append("AND t.forum_id = ? AND (t.start_date < ? OR t.start_date IS NULL) AND p.user_id = u.user_id ORDER BY p.post_time DESC");
			
			fields = new Object[2];
			fields[i++] = forumId;
			fields[i++] = new Timestamp(System.currentTimeMillis());
		}		
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					LastPostInfoImpl lpi = new LastPostInfoImpl();
					
					lpi.setUserId(result.getInt("user_id"));
					lpi.setFirstName(result.getString("user_fname")==null ? "" : result.getString("user_fname"));
					lpi.setLastName(result.getString("user_lname")==null ? "" : result.getString("user_lname"));
					lpi.setPostDate(result.getTimestamp("post_time"));
					lpi.setPostId(result.getInt("post_id"));
					lpi.setTopicId(result.getInt("topic_id"));
					lpi.setPostTimeMillis(result.getTimestamp("post_time").getTime());
					lpi.setTopicReplies(result.getInt("topic_replies"));
					
					lastPostInfoList.add(lpi);
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectForumLastPostInfo: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (lastPostInfoList.size() > 0)
		{
			lastPostInfo = lastPostInfoList.get(0);
		}
		
		return lastPostInfo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> selectForumTopics(int forumId)
	{
		return getForumTopics(forumId, 0, 0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> selectForumTopics(int forumId, int startFrom, int count)
	{
		return getForumTopics(forumId, startFrom, count);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int selectMarkedTopicsCountByForum(int forumId, int userId)
	{
		int topicsCount = 0;
		
		final List<Integer> count = new ArrayList<Integer>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COUNT(j.topic_id) AS topics_count FROM jforum_topics j, jforum_topics_mark m WHERE j.forum_id = ? AND j.topic_id = m.topic_id AND m.user_id = ? AND m.is_read = 1");						
				
		Object[] fields;
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
					count.add(result.getInt("topics_count"));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectMarkedTopicsCountByForum: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (count.size() == 1)
		{
			topicsCount = count.get(0);
		}
		
		return topicsCount;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Topic selectMarkTime(int topicId, int userId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT topic_id, user_id, mark_time, is_read FROM jforum_topics_mark WHERE topic_id = ? AND user_id = ?");
		
		int i = 0;
		Object[] fields = new Object[2];
		
		fields[i++] = topicId;
		fields[i++] = userId;
		
		//final List<Date> markTimeList = new ArrayList<Date>();
		final List<Topic> topics = new ArrayList<Topic>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					//markTimeList.add(result.getTimestamp("mark_time"));
					TopicImpl topic = new TopicImpl();
					topic.setId(result.getInt("topic_id"));
					topic.setTime(result.getTimestamp("mark_time"));
					
					int isRead = result.getInt("is_read");
					topic.setRead((isRead == 0) ? Boolean.TRUE : Boolean.FALSE);

					topics.add(topic);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectMarkTime: " + e, e);
					}
					return null;
				}
			}
		});
		
		/*Date markTime = null;
		if (markTimeList.size() == 1)
		{
			markTime = markTimeList.get(0);
		}
		
		return markTime;*/
		
		Topic topic = null;
		if (topics.size() == 1)
		{
			topic = topics.get(0);
		}
		
		return topic;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Post selectPostById(int postId)
	{
		Post post = getPostById(postId);
		
		// attachments
		if ((post != null) && (post.hasAttachments()))
		{
			//post.getAttachments().addAll(selectPostAttachments(postId));
			post.getAttachments().addAll(attachmentDao.selectPostAttachments(postId));
		}
		return post;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Post> selectPostsByCategoryByUser(int categoryId, int userId)
	{
		List<Post> posts = getPostsByCategoryByUser(categoryId, userId);
		
		// attachment info
		for (Post post : posts)
		{
			if (post.hasAttachments())
			{
				List<Attachment> postAttachments = selectPostAttachments(post.getId());
				
				if (postAttachments != null)
				{
					post.getAttachments().addAll(postAttachments);
				}
			}
		}
		
		return posts; 
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Post> selectPostsByForumByUser(int forumId, int userId)
	{
		List<Post> posts = getPostsByForumByUser(forumId, userId);
		
		// attachment info
		for (Post post : posts)
		{
			if (post.hasAttachments())
			{
				List<Attachment> postAttachments = selectPostAttachments(post.getId());
				
				if (postAttachments != null)
				{
					post.getAttachments().addAll(postAttachments);
				}
			}
		}
		
		return posts; 
	}
	
	public List<Post> selectPostsByTopicByUser(int topicId, int userId)
	{
		List<Post> posts = getPostsByTopicByUser(topicId, userId);
		
		// attachment info
		for (Post post : posts)
		{
			if (post.hasAttachments())
			{
				List<Attachment> postAttachments = selectPostAttachments(post.getId());
				
				if (postAttachments != null)
				{
					post.getAttachments().addAll(postAttachments);
				}
			}
		}
		
		return posts;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int selectPostsCountByTopic(int topicId)
	{
		int topicsCount = 0;
		
		final List<Integer> count = new ArrayList<Integer>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COUNT(1) AS topics_count FROM jforum_posts WHERE topic_id = ?");						
				
		Object[] fields;
		int i = 0;
		
		fields = new Object[1];
		fields[i++] = topicId;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					count.add(result.getInt("topics_count"));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectPostsCountByTopic: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (count.size() == 1)
		{
			topicsCount = count.get(0);
		}
		
		return topicsCount;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> selectRecentTopics(String context, int limit)
	{
		return getRecentTopics(context, limit);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int selectTopicDatesCountByCategory(int categoryId)
	{
		String sql = "SELECT count(t.topic_id) As topics_with_dates FROM jforum_topics t, jforum_forums f, jforum_categories c WHERE c.categories_id = ? AND f.categories_id = c.categories_id AND f.forum_id = t.forum_id AND (t.start_date IS NOT NULL OR t.end_date IS NOT NULL OR t.allow_until_date IS NOT NULL)";
		
		int i = 0; 
		Object[] fields = new Object[1];
		fields[i++] = categoryId;
				
		return getTopicsCount(sql, fields);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int selectTopicDatesCountByForum(int forumId)
	{
		String sql;
		Object[] fields;
		int i = 0;
		
		sql = "SELECT count(t.topic_id) As topics_with_dates FROM jforum_topics t, jforum_forums f WHERE f.forum_id = ? AND f.forum_id = t.forum_id AND (t.start_date IS NOT NULL OR t.end_date IS NOT NULL OR t.allow_until_date IS NOT NULL)";						
						
		fields = new Object[1];
		fields[i++] = forumId;		

		return getTopicsCount(sql, fields);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public LastPostInfo selectTopicLastPostInfo(int topicId)
	{
		LastPostInfo lastPostInfo = null;
		
		final List<LastPostInfo> lastPostInfoList = new ArrayList<LastPostInfo>();
		
		StringBuilder sql = new StringBuilder();
				
		sql.append("SELECT post_time, p.topic_id, t.topic_replies, post_id, u.user_id, username, u.user_fname, u.user_lname ");
		sql.append("FROM jforum_posts p, jforum_users u, jforum_topics t WHERE t.topic_id = p.topic_id AND ");
		sql.append(" t.topic_last_post_id = p.post_id AND t.topic_id = ? AND p.user_id = u.user_id");
						
		Object[] fields;
		int i = 0;
		
		fields = new Object[1];
		fields[i++] = topicId;
	
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					LastPostInfoImpl lpi = new LastPostInfoImpl();
					
					lpi.setUserId(result.getInt("user_id"));
					lpi.setFirstName(result.getString("user_fname")==null ? "" : result.getString("user_fname"));
					lpi.setLastName(result.getString("user_lname")==null ? "" : result.getString("user_lname"));
					lpi.setPostDate(result.getTimestamp("post_time"));
					lpi.setPostId(result.getInt("post_id"));
					lpi.setTopicId(result.getInt("topic_id"));
					lpi.setPostTimeMillis(result.getTimestamp("post_time").getTime());
					lpi.setTopicReplies(result.getInt("topic_replies"));
					
					lastPostInfoList.add(lpi);
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectTopicLastPostInfo: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (lastPostInfoList.size() > 0)
		{
			lastPostInfo = lastPostInfoList.get(0);
		}
		
		return lastPostInfo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Topic selectTopicLatestPosttime(int topicId, int userId, Date after, boolean topicDatesNeeded)
	{
		final List<Topic> topics = new ArrayList<Topic>();
		
		StringBuilder sql = new StringBuilder();
		Object[] fields = null;
		int i = 0;
		
		if (after == null)
		{
			if (topicDatesNeeded)
			{
				sql.append("SELECT MAX(p.post_time) As post_time, p.topic_id, t.start_date, t.end_date FROM jforum_posts p, jforum_topics t WHERE p.topic_id = ? AND p.topic_id = t.topic_id AND p.user_id <> ?");					
			}
			else
			{
				sql.append("SELECT MAX(post_time) As post_time, topic_id FROM jforum_posts WHERE topic_id = ? AND user_id <> ? GROUP BY topic_id");
			}
			fields = new Object[2];
			fields[i++] = topicId;
			fields[i++] = userId;
		}
		else
		{
			if (topicDatesNeeded)
			{
				sql.append("SELECT MAX(p.post_time) As post_time, p.topic_id, t.start_date, t.end_date FROM jforum_posts p, jforum_topics t WHERE p.topic_id = ? AND p.topic_id = t.topic_id AND p.user_id <> ? AND p.post_time > ?");						
			}
			else
			{
				sql.append("SELECT MAX(post_time) As post_time, topic_id FROM jforum_posts WHERE topic_id = ? AND user_id <> ? AND post_time > ?");
			}
			fields = new Object[3];
			fields[i++] = topicId;
			fields[i++] = userId;
			fields[i++] = after;
		}
		
		if (topicDatesNeeded)
		{
			this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
			{
				public Object readSqlResultRecord(ResultSet result)
				{
					try
					{
						TopicImpl topic = new TopicImpl();
						topic.setId(result.getInt("topic_id"));
						topic.setTime(result.getTimestamp("post_time"));

						AccessDates accessDates = new AccessDatesImpl();
						accessDates.setOpenDate(result.getTimestamp("start_date"));
						if (result.getDate("end_date") != null)
					    {
					      Timestamp endDate = result.getTimestamp("end_date");
					      accessDates.setDueDate(endDate);
					    }
					    else
					    {
					    	accessDates.setDueDate(null);
					    }
						
						topic.setAccessDates(accessDates);
						
						topics.add(topic);
	
						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("selectTopicLatestPosttime: " + e, e);
						}
						return null;
					}
				}
			});
		}
		else
		{
			this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
			{
				public Object readSqlResultRecord(ResultSet result)
				{
					try
					{
						TopicImpl topic = new TopicImpl();
						topic.setId(result.getInt("topic_id"));
						topic.setTime(result.getTimestamp("post_time"));
						
						topics.add(topic);
	
						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("selectTopicLatestPosttime: " + e, e);
						}
						return null;
					}
				}
			});
		}
		
		Topic topic = null;
		
		if (topics.size() == 1)
		{
			topic = topics.get(0);
		}
		return topic;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> selectTopicLatestPosttimesByForum(int forumId, int userId, Date after, boolean topicDatesNeeded)
	{
		final List<Topic> topics = new ArrayList<Topic>();
		
		StringBuilder sql = new StringBuilder();
		Object[] fields = null;
		int i = 0;
		
		if (after == null)
		{
			if (topicDatesNeeded)
			{
				sql.append("SELECT MAX(p.post_time) As post_time, p.topic_id, t.start_date, t.end_date FROM jforum_posts p, jforum_topics t WHERE p.forum_id = ? AND p.topic_id = t.topic_id AND p.user_id <> ? GROUP BY topic_id");					
			}
			else
			{
				sql.append("SELECT MAX(post_time) As post_time, topic_id FROM jforum_posts WHERE forum_id = ? AND user_id <> ? GROUP BY topic_id");
			}
			fields = new Object[2];
			fields[i++] = forumId;
			fields[i++] = userId;
		}
		else
		{
			if (topicDatesNeeded)
			{
				sql.append("SELECT MAX(p.post_time) As post_time, p.topic_id, t.start_date, t.end_date FROM jforum_posts p, jforum_topics t WHERE p.forum_id = ? AND p.topic_id = t.topic_id AND p.user_id <> ? AND p.post_time > ? GROUP BY topic_id");						
			}
			else
			{
				sql.append("SELECT MAX(post_time) As post_time, topic_id FROM jforum_posts WHERE forum_id = ? AND user_id <> ? AND post_time > ? GROUP BY topic_id");
			}
			fields = new Object[3];
			fields[i++] = forumId;
			fields[i++] = userId;
			fields[i++] = after;
		}
		
		if (topicDatesNeeded)
		{
			this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
			{
				public Object readSqlResultRecord(ResultSet result)
				{
					try
					{
						TopicImpl topic = new TopicImpl();
						topic.setId(result.getInt("topic_id"));
						topic.setTime(result.getTimestamp("post_time"));

						AccessDates accessDates = new AccessDatesImpl();
						accessDates.setOpenDate(result.getTimestamp("start_date"));
						if (result.getDate("end_date") != null)
					    {
					      Timestamp endDate = result.getTimestamp("end_date");
					      accessDates.setDueDate(endDate);
					    }
					    else
					    {
					    	accessDates.setDueDate(null);
					    }
						
						topic.setAccessDates(accessDates);
						
						topics.add(topic);
	
						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("selectTopicLatestPosttimesByForum: " + e, e);
						}
						return null;
					}
				}
			});
		}
		else
		{
			this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
			{
				public Object readSqlResultRecord(ResultSet result)
				{
					try
					{
						TopicImpl topic = new TopicImpl();
						topic.setId(result.getInt("topic_id"));
						topic.setTime(result.getTimestamp("post_time"));
						
						topics.add(topic);
	
						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("selectTopicLatestPosttimesByForum: " + e, e);
						}
						return null;
					}
				}
			});
		}
		
		return topics;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Post> selectTopicPosts(int topicId)
	{
		return selectTopicPostsByLimit(topicId, 0, 0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Post> selectTopicPostsByLimit(int topicId, int startFrom, int count)
	{
		List<Post> posts = getTopicPostsByLimit(topicId, startFrom, count);
		
		// attachment info
		Map<Integer, List<Attachment>> topicPostAttachments = selectTopicPostAttachments(topicId);
		
		for (Post post: posts)
		{
			if (post.hasAttachments())
			{
				List<Attachment> postAttachments = topicPostAttachments.get(post.getId());
				
				if (postAttachments != null)
				{
					post.getAttachments().addAll(postAttachments);
				}
			}
		}
		
		return posts;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int selectTopicsCountByForum(int forumId)
	{
		String sql = "SELECT COUNT(1) AS total FROM jforum_topics WHERE forum_id = ?";
		
		Object[] fields;
		int i = 0;
		
		fields = new Object[1];
		fields[i++] = forumId;		

		int forumTopicsCount = 0;
		
		final List<Integer> count = new ArrayList<Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					count.add(result.getInt("total"));

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectTopicsCountByForum: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (count.size() == 1)
		{
			forumTopicsCount = count.get(0);
		}
		return forumTopicsCount;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> selectTopicsWithDatesByForum(int forumId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t.topic_id, t.forum_id, t.topic_title, t.user_id, t.topic_time, t.topic_replies, t.topic_status, t.topic_vote, t.topic_type, t.topic_first_post_id, ");
		sql.append("t.topic_last_post_id, t.moderated, t.topic_grade, t.topic_export, t.start_date, t.hide_until_open, t.end_date, t.allow_until_date, t.lock_end_date, u.username AS posted_by_username, u.sakai_user_id AS posted_sakai_user_id, ");
		sql.append("u.user_id AS posted_by_id, u.user_fname AS posted_by_fname, u.user_lname AS posted_by_lname, u.user_avatar AS posted_by_avatar, u2.username AS last_post_by_username, u2.sakai_user_id AS last_post_sakai_user_id, ");
		sql.append("u2.user_id AS last_post_by_id, u2.user_fname AS last_post_by_fname, u2.user_lname AS last_post_by_lname, u2.user_avatar AS last_post_by_avatar, p2.post_time, p.attach ");
		sql.append("FROM jforum_topics t, jforum_users u, jforum_posts p, jforum_posts p2, jforum_users u2 ");
		sql.append("WHERE t.forum_id = ? ");
		sql.append("AND (t.start_date IS NOT NULL OR t.end_date IS NOT NULL OR t.allow_until_date IS NOT NULL) ");
		sql.append("AND t.user_id = u.user_id ");
		sql.append("AND p.post_id = t.topic_first_post_id ");
		sql.append("AND p2.post_id = t.topic_last_post_id ");
		sql.append("AND u2.user_id = p2.user_id ");
		sql.append("ORDER BY t.topic_export DESC, t.topic_type DESC, p2.post_time DESC, t.topic_last_post_id DESC ");
						
		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] = forumId;
		
		List<Topic> topics = readTopics(sql.toString(), fields);
		
		// topics special access
		for (Topic topic : topics)
		{
			if ((topic.getAccessDates() != null) && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null) || (topic.getAccessDates().getAllowUntilDate() != null)))
			{
				List<SpecialAccess> topicSpecialAccess = specialAccessDao.selectByTopic(topic.getForumId(), topic.getId());
				topic.getSpecialAccess().addAll(topicSpecialAccess);
			}
		}
		
		return topics;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> selectUserTopicVisitTimesByForum(int forumId, int userId)
	{
		final List<Topic> topics = new ArrayList<Topic>();
		
		StringBuilder sql = new StringBuilder();
				
		// sql.append("SELECT m.topic_id, m.user_id, m.mark_time, m.is_read FROM jforum_topics_mark m, jforum_topics t WHERE m.topic_id = t.topic_id AND t.forum_id = ? AND m.user_id = ? AND m.is_read = 0");
		sql.append("SELECT m.topic_id, m.user_id, m.mark_time, m.is_read FROM jforum_topics_mark m, jforum_topics t WHERE m.topic_id = t.topic_id AND t.forum_id = ? AND m.user_id = ?");
						
		Object[] fields;
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
					TopicImpl topic = new TopicImpl();
					topic.setId(result.getInt("topic_id"));
					topic.setTime(result.getTimestamp("mark_time"));
					
					int isRead = result.getInt("is_read");
					topic.setRead((isRead == 0) ? Boolean.TRUE : Boolean.FALSE);

					topics.add(topic);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectTopicLatestPosttimesByForum: " + e, e);
					}
					return null;
				}
			}
		});
		
		return topics;		
	}
	
	/**
	 * @param attachmentDao the attachmentDao to set
	 */
	public void setAttachmentDao(AttachmentDao attachmentDao)
	{
		this.attachmentDao = attachmentDao;
	}
	
	/**
	 * @param forumDao 
	 * 				The forumDao to set
	 */
	public void setForumDao(ForumDao forumDao)
	{
		this.forumDao = forumDao;
	}
	
	/**
	 * @param gradeDao 
	 * 			The gradeDao to set
	 */
	public void setGradeDao(GradeDao gradeDao)
	{
		this.gradeDao = gradeDao;
	}
	
	/**
	 * @param jforumForumService the jforumForumService to set
	 */
	public void setJforumForumService(JForumForumService jforumForumService)
	{
		this.jforumForumService = jforumForumService;
	}
	
	/**
	 * @param jforumPostService the jforumPostService to set
	 */
	public void setJforumPostService(JForumPostService jforumPostService)
	{
		this.jforumPostService = jforumPostService;
	}
	
	/**
	 * @param postDao the postDao to set
	 */
	public void setPostDao(PostDao postDao)
	{
		this.postDao = postDao;
	}
	
	/**
	 * @param specialAccessDao 
	 * 			The specialAccessDao to set
	 */
	public void setSpecialAccessDao(SpecialAccessDao specialAccessDao)
	{
		this.specialAccessDao = specialAccessDao;
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
	public void subscribeUser(final int topicId, final int userId)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					if (!isUserSubscribed(topicId, userId))
					{
								
						String sql = "INSERT INTO jforum_topics_watch(topic_id, user_id, is_read) VALUES (?, ?, '1')";
								
						Object[] fields;
						int i = 0;
						
						fields = new Object[2];
						fields[i++] = topicId;
						fields[i++] = userId;
						
						if (!sqlService.dbWrite(sql, fields)) 
						{
							throw new RuntimeException("subscribeUser() - db write failed");
						}
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while subscribing user to watch the topic.", e);
				}
			}
		}, "subscribeUser: topic id: " + topicId +" user id: "+ userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateTopic(Topic topic)
	{
		if (topic == null)
		{
			throw new IllegalArgumentException();
		}
		
		if (topic.getId() <= 0 || topic.getForumId() <= 0)
		{
			throw new IllegalArgumentException("Topic cannot be updated as topic has no id or forum id");
		}
		
		updateTopicTx(topic);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateTopicMarkTime(int topicId, int userId, Date markTime, boolean isRead)
	{
		if (markTime == null)
		{
			markTime = new Date();
		}
		
		if (selectMarkTime(topicId, userId) == null)
		{
			addTopicMarkTime(topicId, userId, markTime, isRead);
			
			return;
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE jforum_topics_mark SET mark_time = ?, is_read = ? WHERE topic_id = ? AND user_id = ?");
		
		int i = 0;
		Object[] fields = new Object[4];
		fields[i++] = new Timestamp(markTime.getTime());
		fields[i++] = isRead ? 0 : 1;
		fields[i++] = topicId;
		fields[i++] = userId;
		
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("updateTopicMarkTime: db write failed");
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateTopicPost(Post post)
	{
		if (post == null)
		{
			throw new IllegalArgumentException("Post information is required.");
		}
		
		if (post.getId() <= 0 || post.getTopicId() <= 0 || post.getForumId() <= 0)
		{
			throw new IllegalArgumentException("Post cannot be updated as post has no post or topic or forum id");
		}
		
		updateTopicPostTx(post);
	}
	
	/**
	 * creates new post for the topic and updated related information
	 * 
	 * @param topic		Topic with post information
	 * 
	 * @return	Newly created post id
	 */
	protected int addNewTopicPostTx(final Topic topic)
	{
		final Post post = topic.getPosts().get(0);
		
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					// create post and post text 
					int postId = postDao.addNew(post);

					topic.setLastPostId(postId);
					
					// update topic for the last post id 
					updateTopicFirstLastPostIds(topic);
					
					// update forum last post id 
					forumDao.setLastPost(topic.getForumId(), topic.getLastPostId());
					
					//increase topic replies count
					incrementTotalReplies(topic.getId());
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while creating new post for the topic.", e);
				}
			}
		}, "savetopic: " + topic.getId());
		
		
		return post.getId();
		
	}
	
	/**
	 * Adds the mark or visit time of the topic
	 * 
	 * @param topicId	The topic id
	 * 
	 * @param userId	Jforum user id
	 * 
	 * @param markTime	Mark time
	 * 
	 * @param isRead	true for read and false for unread
	 */
	protected void addTopicMarkTime(int topicId, int userId, Date markTime, boolean isRead)
	{
		if (markTime == null)
		{
			markTime = new Date();
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO jforum_topics_mark(topic_id, user_id, mark_time, is_read) VALUES (?, ?, ?, ?)");
		
		int i = 0;
		Object[] fields = new Object[4];
		fields[i++] = topicId;
		fields[i++] = userId;
		fields[i++] = new Timestamp(markTime.getTime());
		fields[i++] = isRead ? 0 : 1;
		
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("addTopicMarkTime: db write failed");
		}
	}
	
	
	/**
	 * Creates new topic along with post, post text, grade if gradable topic etc
	 * 
	 * @param topic		Topic
	 * 
	 * @return	The new topic's id
	 */
	protected int creatNewTopicTx(final Topic topic)
	{	
		final Post post = topic.getPosts().get(0);
		
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					Forum forum = forumDao.selectById(topic.getForumId());
					
					if (forum != null)
					{
						if (forum.getGradeType() != Grade.GradeType.TOPIC.getType())
						{
							topic.setGradeTopic(false);
						}
					}
					
					// check grade topic
					if (topic.isGradeTopic())
					{
						if ((topic.getGrade() == null) || (topic.getGrade().getPoints() == null))
						{
							topic.setGradeTopic(false);
						}
					}
					
					/*create new topic*/
					int topicId = insertTopic(topic);
					((TopicImpl)topic).setId(topicId);
					
					// create grade if topic is gradable. 
					if ((topic.isGradeTopic()) && (topic.getGrade() != null))
					{
						topic.getGrade().setType(Grade.GradeType.TOPIC.getType());
						topic.getGrade().setForumId(topic.getForumId());
						topic.getGrade().setTopicId(topic.getId());
						topic.getGrade().setCategoryId(0);
												
						gradeDao.addNew(topic.getGrade());
					}
					
					((PostImpl)post).setTopicId(topic.getId());
					((PostImpl)post).setForumId(topic.getForumId());
					
					// create post and post text 
					int postId = postDao.addNew(post);
					
					topic.setFirstPostId(postId);
					topic.setLastPostId(postId);
					
					// update topic for last and first post id 
					updateTopicFirstLastPostIds(topic);
										
					// update forum last post id 
					forumDao.setLastPost(topic.getForumId(), topic.getLastPostId());
					
					// if first topic, increase forum total topics count else increase topic replies count 
					forumDao.incrementTotalTopics(topic.getForumId(), 1);
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					// remove any external entries like entry in grade book if topic is gradable if created
					throw new RuntimeException("Error while creating new topic.", e);
				}
			}
		}, "savetopic: " + topic.getTitle());
		
		
		return topic.getId();
	}
	
	/**
	 * Deletes topic posts and it's associations
	 * 
	 * @param postId	Post id
	 */
	protected void deletePostTx(final int postId)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					
					Post post = getPostById(postId);
					if (post == null)
					{
						return;
					}
					
					Topic topic = selectById(post.getTopicId());
										
					// delete post text and post 
					postDao.delete(postId);
					
					// delete post attachments
					if (post.hasAttachments() && post.getAttachments().size() > 0)
					{
						for (Attachment attachment : post.getAttachments())
						{
							attachmentDao.deletePostAttachment(post.getId(), attachment.getId());
						}
					}
					
					//TODO decrement users posts count - may not be needed as it is per site
					
					// decrease topic replies
					decrementTotalReplies(post.getTopicId());

					// reset topic first post id and last post id
					int maxPostId = getMaxPostId(post.getTopicId());
					if (maxPostId > -1)
					{
						setTopicLastPostId(post.getTopicId(), maxPostId);
					}

					int minPostId = getMinPostId(post.getTopicId());
					if (minPostId > -1)
					{
						setTopicFirstPostId(post.getTopicId(), minPostId);
					}

					// reset forum last post id
					maxPostId = forumDao.getMaxPostId(post.getForumId());
					if (maxPostId > -1)
					{
						forumDao.setLastPost(post.getForumId(), maxPostId);
					}

					// if there are no posts delete the topic
					if (selectPostsCountByTopic(post.getTopicId()) == 0)
					{
						deleteTopic(topic);
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while deleting post.", e);
				}
			}
		}, "deletePost: " + postId);		
	}
	
	/**
	 * Deletes topic and it's associations
	 * 
	 * @param topic	Topic
	 */
	protected void deleteTopic(Topic topic)
	{
		List<Post> posts = selectTopicPostsByLimit(topic.getId(), 0, 0);
		
		// remove all posts
		for (Post post: posts)
		{						
			if (post == null)
			{
				continue;
			}
			
			// delete post text and post 
			postDao.delete(post.getId());
			
			// delete post attachments
			if (post.hasAttachments() && post.getAttachments().size() > 0)
			{
				for (Attachment attachment : post.getAttachments())
				{
					attachmentDao.deletePostAttachment(post.getId(), attachment.getId());
				}
			}
		}
		
		// delete subscriptions/watches
		deleteTopicSubscriptions(topic.getId());
		
		// delete topic read status or mark times
		deleteTopicMarkTimes(topic.getId());
		
		// delete grades and evaluations
		if (topic.isGradeTopic())
		{
			gradeDao.delete(topic.getForumId(), topic.getId());
		}
		
		//if topic is gradable and has dates delete existing special access
		if (topic.getAccessDates() != null && (topic.getAccessDates().getOpenDate() != null || topic.getAccessDates().getDueDate() != null || topic.getAccessDates().getAllowUntilDate() != null))
		{
			List<SpecialAccess> topicSpecialAccess = topic.getSpecialAccess();
			for (SpecialAccess specialAccess : topicSpecialAccess)
			{
				specialAccessDao.delete(specialAccess.getId());
			}
		}
		
		// delete topic
		removeTopic(topic.getId());
		
		// update forum topics count
		forumDao.decrementTotalTopics(topic.getForumId(), 1);
		
		// if there are no topics under forum update forum last post to zero
		// reset forum last post id
		int maxPostId = forumDao.getMaxPostId(topic.getForumId());
		if (maxPostId > -1)
		{
			forumDao.setLastPost(topic.getForumId(), maxPostId);
		}
		else
		{
			forumDao.setLastPost(topic.getForumId(), 0);
		}
	}
	
	/**
	 * Deletes topic mark times or read statuses
	 * 
	 * @param topicId	Topic id
	 */
	protected void deleteTopicMarkTimes(int topicId)
	{

		String sql = "DELETE FROM jforum_topics_mark WHERE topic_id = ?";
		
		Object[] fields = new Object[1];
		
		int i = 0;
		
		fields[i++] = topicId;
				
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("deleteTopicMarkTimes: db write failed");
		}		
	}
	
	/**
	 * Deletes topic subscriptions to watch the topic
	 * 
	 * @param topicId	Topic id
	 */
	protected void deleteTopicSubscriptions(int topicId)
	{

		String sql = "DELETE FROM jforum_topics_watch WHERE topic_id = ?";
		
		Object[] fields = new Object[1];
		
		int i = 0;
		
		fields[i++] = topicId;
				
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("deleteTopicSubscriptions: db write failed");
		}		
	}
	
	/**
	 * deletes topic and it's associations
	 * 
	 * @param topic	Topic
	 */
	protected void deleteTopicTx(final Topic topic)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{	
					
					deleteTopic(topic);
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while updating post.", e);
				}
			}
		}, "deleteTopic: " + topic.getId());		
	}
	
	abstract protected List<Topic> getForumTopics(int forumId, int startFrom, int count);
	
	/**
	 * Gets the topic's maximum post id 
	 * 
	 * @param topicId	Topic id
	 * 
	 * @return	The topic's maximum post id
	 */
	protected int getMaxPostId(int topicId)
	{
		int maxPostId = -1;
		
		final List<Integer> postIds = new ArrayList<Integer>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT MAX(post_id) AS post_id FROM jforum_posts WHERE topic_id = ?");						
				
		Object[] fields;
		int i = 0;
		
		fields = new Object[1];
		fields[i++] = topicId;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					postIds.add(result.getInt("post_id"));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getMaxPostId: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (postIds.size() == 1)
		{
			maxPostId = postIds.get(0);
		}
		
		return maxPostId;
	}
	
	/**
	 * Gets the topic's minimum post id
	 * 
	 * @param topicId	Topic id
	 * 
	 * @return	The topic's minimum post id
	 */
	protected int getMinPostId(int topicId)
	{

		int minPostId = -1;
		
		final List<Integer> postIds = new ArrayList<Integer>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT MIN(post_id) AS post_id FROM jforum_posts WHERE topic_id = ?");						
				
		Object[] fields;
		int i = 0;
		
		fields = new Object[1];
		fields[i++] = topicId;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					postIds.add(result.getInt("post_id"));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getMaxPostId: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (postIds.size() == 1)
		{
			minPostId = postIds.get(0);
		}
		
		return minPostId;	
	}
	
	/**
	 * Gets the post
	 * 
	 * @param postId	Post id
	 * 
	 * @return	The post or null
	 */
	abstract protected Post getPostById(int postId);
	
	/**
	 * Gets the user category posts
	 * 
	 * @param categoryId	Category id
	 * 
	 * @param userId	User id
	 * 
	 * @return	The user category posts
	 */
	abstract protected List<Post> getPostsByCategoryByUser(int categoryId, int userId);
	
	/**
	 * Gets the user forum posts
	 * 
	 * @param forumId	Forum id
	 * 
	 * @param userId	User id
	 * 
	 * @return	The user forum posts
	 */
	abstract protected List<Post> getPostsByForumByUser(int forumId, int userId);
	
	/**
	 * Gets the user topic posts
	 * 
	 * @param topicId	Topic id
	 * 
	 * @param userId	User id
	 * 
	 * @return	The user topic posts
	 */
	abstract protected List<Post> getPostsByTopicByUser(int topicId, int userId);
	
	/**
	 * Reads the post text
	 * 
	 * @param rs	The Resultset
	 * 
	 * @return	Post text
	 * 
	 * @throws SQLException
	 */
	abstract protected String getPostTextFromResultSet(ResultSet rs) throws SQLException;
	
	/**
	 * Gets the site recent topics
	 * 
	 * @param context	The context or site id
	 * 
	 * @param limit		Maximum records to fetch
	 * 
	 * @return	List of recent topics
	 */
	abstract protected List<Topic> getRecentTopics(String context, int limit);
	
	/**
	 * gets the topic base data
	 * 
	 * @param result	Resultset
	 * 
	 * @return		The topic
	 * 
	 * @throws SQLException
	 */
	protected Topic getTopicBaseData(ResultSet result) throws SQLException
	{
		TopicImpl topic = new TopicImpl(this.jforumForumService);
		topic.setId(result.getInt("topic_id"));
		topic.setForumId(result.getInt("forum_id"));
		topic.setTitle(result.getString("topic_title"));
		topic.setGradeTopic(result.getInt("topic_grade") == Topic.TopicGradableCode.YES.getTopicGradableCode());
		topic.setExportTopic(result.getInt("topic_export") == Topic.TopicExportImportCode.YES.getTopicExportImportCode());
		
		if ((result.getDate("start_date") != null) || (result.getDate("end_date") != null) || (result.getDate("allow_until_date") != null))
		{
			AccessDates accessDates = new AccessDatesImpl();
			accessDates.setOpenDate(result.getTimestamp("start_date"));
			accessDates.setHideUntilOpen(result.getInt("hide_until_open") > 0);
			
			if (result.getDate("end_date") != null)
		    {
		      Timestamp endDate = result.getTimestamp("end_date");
		      accessDates.setDueDate(endDate);
		      // 08/20/2012 - lock on due is not supported anymore
		      // accessDates.setLocked(rs.getInt("lock_end_date") > 0);
		    }
		    else
		    {
		    	accessDates.setDueDate(null);
		    }
			accessDates.setAllowUntilDate(result.getTimestamp("allow_until_date"));
			
			topic.setAccessDates(accessDates);
		}
		else
		{
			AccessDates accessDates = new AccessDatesImpl();
			topic.setAccessDates(accessDates);
		}
		
		topic.setType(result.getInt("topic_type"));
		topic.setTotalReplies(result.getInt("topic_replies"));
		topic.setStatus(result.getInt("topic_status"));
		
		topic.setFirstPostId(result.getInt("topic_first_post_id"));
		topic.setLastPostId(result.getInt("topic_last_post_id"));
		
		return topic;
	}
	
	abstract protected List<Post> getTopicPostsByLimit(int topicId, int startFrom, int count);
	
	/**
	 * Gets the topics count
	 * 
	 * @param topicsCount	Topics count
	 * 
	 * @param sql			SQL Query
	 *  
	 * @param fields		Query parameters
	 * 		
	 * @return	The topics count
	 */
	protected int getTopicsCount(String sql, Object[] fields)
	{
		int topicsCount = 0;
		
		final List<Integer> count = new ArrayList<Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					count.add(result.getInt("topics_with_dates"));

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getTopicsCount: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (count.size() == 1)
		{
			topicsCount = count.get(0);
		}
		return topicsCount;
	}
	
	/**
	 * Create new topic
	 * 
	 * @param topic	Topic 
	 * 
	 * @return	New topic id
	 */
	protected abstract int insertTopic(Topic topic);
	
	/**
	 * prepares the post object
	 * 
	 * @param rs	ResultSet
	 * 
	 * @return	The post
	 * 
	 * @throws SQLException
	 */
	protected Post makePost(ResultSet rs) throws SQLException
	{
		PostImpl post = new PostImpl(this.jforumPostService);
		post.setId(rs.getInt("post_id"));
		post.setTopicId(rs.getInt("topic_id"));
		post.setForumId(rs.getInt("forum_id"));
		post.setUserId(rs.getInt("user_id"));
		post.setTime(rs.getTimestamp("post_time"));
		post.setHtmlEnabled(rs.getInt("enable_html") > 0);
		post.setSubject(rs.getString("post_subject"));
		post.setText(HtmlHelper.clean(this.getPostTextFromResultSet(rs), true));
		post.setRawText(this.getPostTextFromResultSet(rs));
		post.setHasAttachments(rs.getInt("attach") > 0);
		post.setEditTime(rs.getTimestamp("post_edit_time"));
		post.setEditCount(rs.getInt("post_edit_count"));
		post.setSignatureEnabled(rs.getInt("enable_sig") == 1);
		
		UserImpl u = new UserImpl();
		u.setId(rs.getInt("user_id"));
		u.setSakaiUserId(rs.getString("sakai_user_id"));
		u.setFirstName(rs.getString("user_fname"));
		u.setLastName(rs.getString("user_lname"));
		u.setAvatar(rs.getString("user_avatar"));
		u.setAttachSignatureEnabled(rs.getInt("user_attachsig") == 1);
		u.setSignature(rs.getString("user_sig"));
		u.setUsername(rs.getString("username"));
		u.setNotifyOnMessagesEnabled(rs.getInt("user_notify") == 1);
		u.setNotifyPrivateMessagesEnabled(rs.getInt("user_notify_pm") == 1);
		u.setEmail(rs.getString("user_email")==null ? "" : rs.getString("user_email"));
		u.setIcq(rs.getString("user_icq"));
		u.setWebSite(rs.getString("user_website"));
		u.setFrom(rs.getString("user_from"));
		u.setAim(rs.getString("user_aim"));
		u.setYim(rs.getString("user_yim"));
		u.setMsnm(rs.getString("user_msnm"));
		u.setFaceBookAccount(rs.getString("user_facebook_account"));
		u.setTwitterAccount(rs.getString("user_twitter_account"));
		u.setOccupation(rs.getString("user_occ"));
		u.setInterests(rs.getString("user_interests"));
		u.setViewEmailEnabled(rs.getInt("user_viewemail") == 1);
		u.setLang(rs.getString("user_lang"));
		//u.setHtmlEnabled(rs.getInt("user_allowhtml") == 1);
		
		post.setPostedBy(u);
						
		return post;
	}
	
	/**
	 * Updates topic
	 * 
	 * @param topic	Topic
	 */
	protected void modifyTopic(Topic topic)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE jforum_topics SET topic_title = ?, topic_last_post_id = ?, topic_first_post_id = ?, topic_type = ?, ");
		sql.append("topic_grade = ?, topic_export = ?, start_date = ?, hide_until_open = ?, end_date = ?, allow_until_date = ? WHERE topic_id = ?");
		
		Object[] fields = new Object[11];
		int i = 0;

		fields[i++] = topic.getTitle();
		fields[i++] = topic.getLastPostId();
		fields[i++] = topic.getFirstPostId();
		fields[i++] = topic.getType();
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
				//fields[i++] = 0;
			} 
			else
			{
				// 08/20/2012 - lock on due is not supported anymore
				fields[i++] = new Timestamp(topic.getAccessDates().getDueDate().getTime());
				//fields[i++] = topic.getAccessDates().isLocked() ? 1 : 0;
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
			//fields[i++] = 0;
			fields[i++] = null;
		}
		
		fields[i++] = topic.getId();
			
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("updateTopic: db write failed");
		}
	}
	
	/**
	 * Moves topic to another forum and updates forum topics count
	 * 
	 * @param topic		Topic
	 * 
	 * @param toForumId		Forum id
	 */
	protected void moveTopicTx(final Topic topic, final int toForumId)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					Forum toForum = forumDao.selectById(toForumId);
					
					if (toForum == null)
					{
						return;
					}
					
					// check topic dates and to forum dates and to forum's category dates.
					if (topic.getAccessDates() != null && (topic.getAccessDates().getOpenDate() != null || topic.getAccessDates().getDueDate() != null || topic.getAccessDates().getAllowUntilDate() != null))
					{
						if (toForum.getAccessDates() != null && (toForum.getAccessDates().getOpenDate() != null || toForum.getAccessDates().getDueDate() != null || toForum.getAccessDates().getAllowUntilDate() != null))
						{
							topic.setAccessDates(null);
							modifyTopic(topic);
						}
						else
						{
							Category category = toForum.getCategory();
							
							if ((category != null) && ((category.getAccessDates() != null && (category.getAccessDates().getOpenDate() != null || category.getAccessDates().getDueDate() != null) || category.getAccessDates().getAllowUntilDate() != null)))
							{
								topic.setAccessDates(null);
								modifyTopic(topic);
							}
						}
					}
					
					// check topic grade type and to forum grade type and to forum's category grade type.
					if (topic.isGradeTopic())
					{
						if (toForum.getGradeType() == Grade.GradeType.TOPIC.getType())
						{
							// keep the topic grade
						}
						else if (toForum.getGradeType() == Grade.GradeType.FORUM.getType())
						{
							// remove topic grade and evaluations
							gradeDao.delete(topic.getForumId(), topic.getId());
							
							topic.setGradeTopic(Boolean.FALSE);
							modifyTopic(topic);
						}
						else
						{
							Category category = toForum.getCategory();
							
							if (category.isGradable())
							{
								// remove topic grade and evaluations
								gradeDao.delete(topic.getForumId(), topic.getId());
								
								topic.setGradeTopic(Boolean.FALSE);
								modifyTopic(topic);
							}
						}
						
					}
					
					
					int fromForumId = topic.getForumId();
					
					// remove topic special access list
					List<SpecialAccess> specialAccessList = specialAccessDao.selectByTopic(topic.getForumId(), topic.getId());
					
					for (SpecialAccess specialAccess : specialAccessList)
					{
						specialAccessDao.delete(specialAccess.getId());
					}
					
					// update topic forum id
					updateTopicForumId(topic.getId(), toForumId);
					
					// update topic posts forum id
					updateTopicPostsForumId(topic.getId(), toForumId);
					
					// updating moderation status not needed
					
					// reduce from forum total topics
					forumDao.decrementTotalTopics(fromForumId, 1);
					
					// increase from forum total topics
					forumDao.incrementTotalTopics(toForumId, 1);
					
					// update last post of from forum id
					int fromForumMaxPostId = forumDao.getMaxPostId(fromForumId);
					if (fromForumMaxPostId > -1)
					{
						forumDao.setLastPost(fromForumId, fromForumMaxPostId);
					}
					else
					{
						forumDao.setLastPost(fromForumId, 0);
					}
					
					// update last post of to forum id
					int toForumMaxPostId = forumDao.getMaxPostId(fromForumId);
					if (toForumMaxPostId > -1)
					{
						forumDao.setLastPost(fromForumId, toForumMaxPostId);
					}
					else
					{
						forumDao.setLastPost(fromForumId, 0);
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while moving topic.", e);
				}
			}
		}, "moveTopic: " + topic.getId());
		
	}
	
	/**
	 * Gets the topics from the database
	 * 
	 * @param sql	The query
	 * 
	 * @param fields Query parameters
	 * 
	 * @return List of topics
	 */
	protected List<Topic> readTopics(String sql, Object[] fields)
	{
		final List<Topic> topics = new ArrayList<Topic>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Topic topic = getTopicBaseData(result);
					
					topic.setHasAttach(result.getInt("attach") > 0);
					
					// First Post Time
					topic.setFirstPostTime(result.getTimestamp("topic_time"));
					// Last Post Time
					topic.setLastPostTime(result.getTimestamp("post_time"));
   					// Created by
					UserImpl u = new UserImpl();
					u.setId(result.getInt("posted_by_id"));
					u.setSakaiUserId(result.getString("posted_sakai_user_id"));
					u.setFirstName(result.getString("posted_by_fname"));
					u.setLastName(result.getString("posted_by_lname"));
					u.setAvatar(result.getString("posted_by_avatar"));
					topic.setPostedBy(u);					
					// Last post by
					u = new UserImpl();
					u.setId(result.getInt("last_post_by_id"));
					u.setSakaiUserId(result.getString("last_post_sakai_user_id"));
					u.setFirstName(result.getString("last_post_by_fname"));
					u.setLastName(result.getString("last_post_by_lname"));
					u.setAvatar(result.getString("last_post_by_avatar"));
					topic.setLastPostBy(u);
					
					topics.add(topic);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readTopics: " + e, e);
					}
					return null;
				}
			}
		});
		return topics;
	}
	
	/**
	 * Gets the topics from the database
	 * 
	 * @param sql	The query
	 * 
	 * @param fields Query parameters
	 * 
	 * @return List of topics
	 */
	protected List<Topic> readTopicsBaseData(String sql, Object[] fields)
	{
		final List<Topic> topics = new ArrayList<Topic>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Topic topic = getTopicBaseData(result);
					
					topics.add(topic);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readTopicsBaseData: " + e, e);
					}
					return null;
				}
			}
		});
		return topics;
	}	
	
	
	/**
	 * deletes topic
	 * 
	 * @param topicId	Topic id
	 */
	protected void removeTopic(int topicId)
	{

		String sql = "DELETE FROM jforum_topics WHERE topic_id = ?";
		
		Object[] fields = new Object[1];
		
		int i = 0;
		
		fields[i++] = topicId;
				
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("removeTopic: db write failed");
		}		
	}
	
	/**
	 * Gets the attachment extensions that are available in the jforum
	 * 
	 * @return	The attachment extensions that are in jforum with extension id as key and attachment extension as value
	 */
	protected Map<Integer, AttachmentExtension> selectAttachmentExtensions()
	{
		final Map<Integer, AttachmentExtension>  attachmentExtensions = new HashMap<Integer, AttachmentExtension>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT e.extension_id, e.extension_group_id, e.extension, e.description, e.upload_icon, e.allow, g.upload_icon AS group_icon, g.download_mode ");
		sql.append("FROM jforum_extensions e, jforum_extension_groups g ");
		sql.append("WHERE e.extension_group_id = g.extension_group_id");
		
		Object[] fields = null;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					AttachmentExtensionImpl attachmentExtensionImpl = new AttachmentExtensionImpl();
					
					attachmentExtensionImpl.setId(result.getInt("extension_id"));
					attachmentExtensionImpl.setExtensionGroupId(result.getInt("extension_group_id"));
					attachmentExtensionImpl.setExtension(result.getString("extension"));
					attachmentExtensionImpl.setComment(result.getString("description"));
					
					String icon = result.getString("upload_icon");
					if (icon == null || icon.equals("")) {
						icon = result.getString("group_icon");
					}
					
					attachmentExtensionImpl.setUploadIcon(icon);
					attachmentExtensionImpl.setAllow(result.getInt("allow") == 1);
					
					attachmentExtensionImpl.setPhysicalDownloadMode(result.getInt("download_mode") == 2);
										
					attachmentExtensions.put(attachmentExtensionImpl.getId(), attachmentExtensionImpl);
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectAttachmentExtensions: " + e, e);
					}
					return null;
				}
			}
		});
		
		return attachmentExtensions;
	}
	
	/**
	 * Selects the post attachments
	 * 
	 * @param postId	Post id
	 * 
	 * @return The post attachments
	 */
	protected List<Attachment> selectPostAttachments(int postId)
	{
		final Map<Integer, AttachmentExtension> attachmentsExtensions = selectAttachmentExtensions();
		
		final List<Attachment> postAttachments = new ArrayList<Attachment>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT a.attach_id, a.user_id, a.post_id, a.privmsgs_id, d.mimetype, d.physical_filename, d.real_filename, ");
		sql.append("d.download_count, d.description, d.filesize, d.upload_time, d.extension_id ");
		sql.append("FROM jforum_attach a, jforum_attach_desc d, jforum_posts p ");
		sql.append("WHERE p.post_id = ? ");
		sql.append("AND a.post_id = p.post_id ");
		sql.append("AND a.attach_id = d.attach_id");
				
		int i = 0;
		
		Object[] fields = new Object[1];
		fields[i++] = postId;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					AttachmentImpl attachment = new AttachmentImpl();
					
					attachment.setId(result.getInt("attach_id"));
					attachment.setUserId(result.getInt("user_id"));
					attachment.setPostId(result.getInt("post_id"));
					attachment.setPrivmsgsId(result.getInt("privmsgs_id"));
					
					AttachmentInfoImpl attachmentInfoImpl = new AttachmentInfoImpl();
					attachmentInfoImpl.setComment(result.getString("description"));
					attachmentInfoImpl.setDownloadCount(result.getInt("download_count"));
					attachmentInfoImpl.setFilesize(result.getLong("filesize"));
					attachmentInfoImpl.setMimetype(result.getString("mimetype"));
					attachmentInfoImpl.setPhysicalFilename(result.getString("physical_filename"));
					attachmentInfoImpl.setRealFilename(result.getString("real_filename"));
					attachmentInfoImpl.setUploadTime(result.getTimestamp("upload_time"));
					
					AttachmentExtension attachmentExtension = attachmentsExtensions.get(result.getInt("extension_id"));
					
					if (attachmentExtension != null)
					{
						attachmentInfoImpl.setExtension(attachmentExtension);
					}
					else
					{
						attachmentInfoImpl.setExtension(null);
					}
					
					attachment.setInfo(attachmentInfoImpl);
					
					postAttachments.add(attachment);
									
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectPostAttachments: " + e, e);
					}
					return null;
				}
			}
		});
		
		return postAttachments;
	}
	
	/**
	 * Gets topic post attachments
	 * 
	 * @param topicId	Topic id
	 * 
	 * @return	The post attachments with post id as key and attachments as value
	 */
	protected Map<Integer, List<Attachment>> selectTopicPostAttachments(int topicId)
	{
		final Map<Integer, AttachmentExtension> attachmentsExtensions = selectAttachmentExtensions();
		
		final Map<Integer, List<Attachment>> attachments = new HashMap<Integer, List<Attachment>>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT a.attach_id, a.user_id, a.post_id, a.privmsgs_id, d.mimetype, d.physical_filename, d.real_filename, ");
		sql.append("d.download_count, d.description, d.filesize, d.upload_time, d.extension_id ");
		sql.append("FROM jforum_attach a, jforum_attach_desc d, jforum_posts p ");
		sql.append("WHERE p.topic_id = ? ");
		sql.append("AND a.post_id = p.post_id ");
		sql.append("AND a.attach_id = d.attach_id");
				
		int i = 0;
		
		Object[] fields = new Object[1];
		fields[i++] = topicId;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					AttachmentImpl attachment = new AttachmentImpl();
					
					attachment.setId(result.getInt("attach_id"));
					attachment.setUserId(result.getInt("user_id"));
					attachment.setPostId(result.getInt("post_id"));
					attachment.setPrivmsgsId(result.getInt("privmsgs_id"));
					
					AttachmentInfoImpl attachmentInfoImpl = new AttachmentInfoImpl();
					attachmentInfoImpl.setComment(result.getString("description"));
					attachmentInfoImpl.setDownloadCount(result.getInt("download_count"));
					attachmentInfoImpl.setFilesize(result.getLong("filesize"));
					attachmentInfoImpl.setMimetype(result.getString("mimetype"));
					attachmentInfoImpl.setPhysicalFilename(result.getString("physical_filename"));
					attachmentInfoImpl.setRealFilename(result.getString("real_filename"));
					attachmentInfoImpl.setUploadTime(result.getTimestamp("upload_time"));
					
					AttachmentExtension attachmentExtension = attachmentsExtensions.get(result.getInt("extension_id"));
					
					if (attachmentExtension != null)
					{
						attachmentInfoImpl.setExtension(attachmentExtension);
					}
					else
					{
						attachmentInfoImpl.setExtension(null);
					}
					
					attachment.setInfo(attachmentInfoImpl);
					
					List<Attachment> postAttachments = attachments.get(attachment.getPostId());
					
					if (postAttachments == null)
					{
						postAttachments = new ArrayList<Attachment>();
						postAttachments.add(attachment);						
						attachments.put(attachment.getPostId(), postAttachments);						
					}
					else
					{
						postAttachments.add(attachment);
					}
									
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectTopicPostAttachments: " + e, e);
					}
					return null;
				}
			}
		});
		
		return attachments;
	}
	
	/**
	 * sets the topic first post id
	 * 
	 * @param topicId	Topic id
	 * 
	 * @param postId	Post id
	 */
	protected void setTopicFirstPostId(int topicId, int postId)
	{
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE jforum_topics SET topic_first_post_id = ? WHERE topic_id = ?");
		
		Object[] fields = new Object[2];
		int i = 0;
		fields[i++] = postId;
		fields[i++] = topicId;
		
		if (!sqlService.dbWrite(null, sql.toString(), fields))
		{
			throw new RuntimeException("setTopicFirstPostId: db write failed");
		}
	}
	
	/**
	 * Sets the topic last post id
	 * 
	 * @param topicId	Topic id
	 * 
	 * @param postId	Post id
	 */
	protected void setTopicLastPostId(int topicId, int postId)
	{
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE jforum_topics SET topic_last_post_id = ? WHERE topic_id = ?");
		
		Object[] fields = new Object[2];
		int i = 0;
		fields[i++] = postId;
		fields[i++] = topicId;
		
		if (!sqlService.dbWrite(null, sql.toString(), fields))
		{
			throw new RuntimeException("setTopicLastPostId: db write failed");
		}
	}

	/**
	 * Update topic first and last post id's
	 * 
	 * @param topic	Topic
	 */
	protected void updateTopicFirstLastPostIds(Topic topic)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE jforum_topics SET topic_last_post_id = ?, topic_first_post_id = ? WHERE topic_id = ?");
		
		Object[] fields = new Object[3];
		int i = 0;
		
		fields[i++] = topic.getLastPostId();
		fields[i++] = topic.getFirstPostId();
		fields[i++] = topic.getId();
			
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("updateTopicFirstLastPostIds: db write failed");
		}
	}
	
	
	
	/**
	 * Update topic's forum id
	 * 
	 * @param topicId	Topic id
	 * 
	 * @param forumId	Forum id
	 */
	protected void updateTopicForumId(int topicId, int forumId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE jforum_topics SET forum_id = ? WHERE topic_id = ?");
		
		Object[] fields = new Object[2];
		int i = 0;
		
		fields[i++] = forumId;
		fields[i++] = topicId;
			
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("updateTopicForumId: db write failed");
		}
	}
	
	/**
	 * Updatest topic post's forum id
	 * 
	 * @param topicId	Topic id
	 * 
	 * @param forumId	Forum id
	 */
	protected void updateTopicPostsForumId(int topicId, int forumId)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE jforum_posts SET forum_id = ? WHERE topic_id = ?");
		
		Object[] fields = new Object[2];
		int i = 0;
		
		fields[i++] = forumId;
		fields[i++] = topicId;
			
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("updateTopicPostsForumId: db write failed");
		}
	}
	
	/**
	 * Updates topic post and text
	 * 
	 * @param post	Post
	 */
	protected void updateTopicPostTx(final Post post)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					// update post and post text 
					postDao.update(post);
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while updating post.", e);
				}
			}
		}, "modifypost: " + post.getId());		
	}
	
	/**
	 * Update topic and topic grades
	 * 
	 * @param topic	Topic
	 */
	protected void updateTopicTx(final Topic topic)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					Topic exisTopic = selectById(topic.getId());
					
					if (exisTopic == null)
					{
						return;
					}
					
					if (exisTopic.getForumId() != topic.getForumId())
					{
						return;
					}
					
					Forum forum = forumDao.selectById(topic.getForumId());
					
					if (forum != null)
					{
						if (forum.getGradeType() != Grade.GradeType.TOPIC.getType())
						{
							topic.setGradeTopic(false);
						}
					}
					
					// check grade topic
					if (topic.isGradeTopic())
					{
						if ((topic.getGrade() == null) || (topic.getGrade().getPoints() == null))
						{
							topic.setGradeTopic(false);
						}
					}

					
					if (topic.isGradeTopic())
					{
						if (exisTopic.isGradeTopic())
						{
							topic.getGrade().setType(Grade.GradeType.TOPIC.getType());
							topic.getGrade().setForumId(topic.getForumId());
							topic.getGrade().setTopicId(topic.getId());
							topic.getGrade().setCategoryId(0);
							
							gradeDao.updateTopicGrade(topic.getGrade());
						}
						else
						{
							topic.getGrade().setType(Grade.GradeType.TOPIC.getType());
							topic.getGrade().setForumId(topic.getForumId());
							topic.getGrade().setTopicId(topic.getId());
							topic.getGrade().setCategoryId(0);
							
							gradeDao.addNew(topic.getGrade());
						}
					}
					else
					{
						// remove existing topic grade
						if (exisTopic.isGradeTopic())
						{
							gradeDao.delete(exisTopic.getForumId(), exisTopic.getId());
						}
						
					}
					
					//delete topic special access if topic has no dates or if topic is not gradable. Topic can have special access if topic is gradable and have dates
					if ((!topic.isGradeTopic()) || (topic.getAccessDates() == null || (topic.getAccessDates().getOpenDate() == null && topic.getAccessDates().getDueDate() == null && topic.getAccessDates().getAllowUntilDate() == null)))
					{
						List<SpecialAccess> topicSpecialAccess = topic.getSpecialAccess();
						for (SpecialAccess specialAccess : topicSpecialAccess)
						{
							specialAccessDao.delete(specialAccess.getId());
						}
					}
					
					modifyTopic(topic);
					
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while updating the topic.", e);
				}
			}
		}, "updateTopic: " + topic.getTitle());		
	}
}
