/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/generic/ForumDaoGeneric.java $ 
 * $Id: ForumDaoGeneric.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.AccessDates;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.JForumCategoryService;
import org.etudes.api.app.jforum.JForumForumTopicsExistingException;
import org.etudes.api.app.jforum.JForumPostService;
import org.etudes.api.app.jforum.JForumSecurityService;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.Topic;
import org.etudes.api.app.jforum.dao.CategoryDao;
import org.etudes.api.app.jforum.dao.ForumDao;
import org.etudes.api.app.jforum.dao.GradeDao;
import org.etudes.api.app.jforum.dao.SpecialAccessDao;
import org.etudes.api.app.jforum.dao.TopicDao;
import org.etudes.component.app.jforum.AccessDatesImpl;
import org.etudes.component.app.jforum.ForumImpl;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;

public abstract class ForumDaoGeneric implements ForumDao
{
	private static Log logger = LogFactory.getLog(ForumDaoGeneric.class);
	
	protected CategoryDao categoryDao = null;

	/** Dependency: GradeDao */
	protected GradeDao gradeDao = null;

	/** Dependency: JForumCategoryService. */
	protected JForumCategoryService jforumCategoryService = null;
	
	/** Dependency: JForumPostService. */
	protected JForumPostService jforumPostService = null;
	
	/** Dependency: JForumSecurityService. */
	protected JForumSecurityService jforumSecurityService = null;
	
	/** Dependency: SqlService */
	protected SpecialAccessDao specialAccessDao = null;

	/** Dependency: SqlService */
	protected SqlService sqlService = null;

	protected TopicDao topicDao = null;
	
	/**
	 * {@inheritDoc}
	 */
	public int addNew(Forum forum)
	{
		if ((forum == null) || (forum.getName() == null) || (forum.getName().trim().length() == 0) ||  (forum.getCategoryId() == 0))
		{
			throw new IllegalArgumentException("forum information is missing");
		}
		
		if (forum.getId() > 0)
		{
			throw new IllegalArgumentException("New forum cannot be created as forum has id.");
		}
		
		if (forum.getName() != null && forum.getName().length() > 150)
		{
			forum.setName(forum.getName().substring(0, 149));
		}
		
		/*if (forum.getDescription() != null && forum.getDescription().length() > 255)
		{
			forum.setDescription(forum.getDescription().substring(0, 254));
		}*/
		
		return creatNewForumTx(forum);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void decrementTotalTopics(int forumId, int count)
	{
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE jforum_forums SET forum_topics = forum_topics - ? WHERE forum_id = ?");
		
		Object[] fields = new Object[2];
		int i = 0;
		
		fields[i++] = count;
		fields[i++] = forumId;
		
		if (!sqlService.dbWrite(null, sql.toString(), fields))
		{
			throw new RuntimeException("decrementTotalTopics: db write failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void delete(int forumId) throws JForumForumTopicsExistingException
	{
		Forum forum = selectById(forumId);
		
		if (forum == null)
		{
			return;
		}
		
		// if forum have topics don't allow to delete forum
		if (getTotalTopics(forumId) > 0)
		{
			throw new JForumForumTopicsExistingException(forum.getName());
		}
		
		deleteTx(forum);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxPostId(int forumId)
	{
		int maxPostId = -1;
		
		final List<Integer> postIds = new ArrayList<Integer>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT MAX(post_id) AS post_id FROM jforum_posts WHERE forum_id = ?");						
				
		Object[] fields;
		int i = 0;
		
		fields = new Object[1];
		fields[i++] = forumId;
		
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
	 * {@inheritDoc}
	 */
	public int getTotalTopics(int forumId)
	{
		int topicsCount = 0;
		
		final List<Integer> count = new ArrayList<Integer>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COUNT(topic_id) AS topics_count FROM jforum_topics WHERE forum_id = ?");						
				
		Object[] fields;
		int i = 0;
		
		fields = new Object[1];
		fields[i++] = forumId;
		
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
						logger.warn("getTotalTopics: " + e, e);
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
	public void incrementTotalTopics(int forumId, int count)
	{
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE jforum_forums SET forum_topics = forum_topics + ? WHERE forum_id = ?");
		
		Object[] fields = new Object[2];
		int i = 0;
		
		fields[i++] = count;
		fields[i++] = forumId;
		
		if (!sqlService.dbWrite(null, sql.toString(), fields))
		{
			throw new RuntimeException("incrementTotalTopics: db write failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Forum> selectAllByCourse(String courseId)
	{
		if (logger.isDebugEnabled()) 
		{
			logger.debug("selectAllByCourse - courseId:"+ courseId);
		}
		
		// all site forums
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT f.forum_id, f.categories_id, f.forum_name, f.forum_desc, f.forum_order, f.forum_topics, f.forum_last_post_id, f.moderated, f.start_date, f.hide_until_open, f.end_date, f.allow_until_date, f.forum_type, f.forum_access_type, f.forum_grade_type, f.lock_end_date, COUNT(p.post_id) AS total_posts "); 
		sql.append("FROM jforum_sakai_course_categories c, jforum_forums f ");
		sql.append("LEFT JOIN jforum_topics t ON f.forum_id = t.forum_id ");
		sql.append("LEFT JOIN jforum_posts p ON p.topic_id = t.topic_id ");
		sql.append("WHERE f.categories_id = c.categories_id ");
		sql.append("AND c.course_id = ? ");
		sql.append("GROUP BY f.categories_id, f.forum_order");

		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] = courseId;
		
		List<Forum> forums = readForums(sql.toString(), fields);		
		
		for (Forum forum : forums)
		{
			// groups
			if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
			{
				forum.setGroups(getForumGroups(forum.getId()));
			}
			
			// special access
			if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))
			{
				List<SpecialAccess> specialAccess = specialAccessDao.selectByForum(forum.getId());
				forum.getSpecialAccess().addAll(specialAccess);
			}
			
			// grades
			if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
			{
				Grade grade = gradeDao.selectByForum(forum.getId());
				forum.setGrade(grade);
			}
		}
		
		return forums;		
	}
	/**
	 * {@inheritDoc}
	 */
	public List<Forum> selectByCategoryId(int categoryId)
	{
		if (logger.isDebugEnabled()) 
		{
			logger.debug("selectByCategoryId - categoryId:"+ categoryId);
		}
		
		// all category forums
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT f.forum_id, f.categories_id, f.forum_name, f.forum_desc, f.forum_order, f.forum_topics, f.forum_last_post_id, f.moderated, f.start_date, f.hide_until_open, f.end_date, f.allow_until_date, f.forum_type, f.forum_access_type, f.forum_grade_type, f.lock_end_date, COUNT(p.post_id) AS total_posts "); 
		sql.append("FROM jforum_sakai_course_categories c, jforum_forums f ");
		sql.append("LEFT JOIN jforum_topics t ON f.forum_id = t.forum_id ");
		sql.append("LEFT JOIN jforum_posts p ON p.topic_id = t.topic_id ");
		sql.append("WHERE f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = ? ");
		sql.append("GROUP BY f.categories_id, f.forum_order");

		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] = categoryId;
		
		List<Forum> forums = readForums(sql.toString(), fields);		
		
		for (Forum forum : forums)
		{
			// groups
			if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
			{
				forum.setGroups(getForumGroups(forum.getId()));
			}
			
			// special access
			if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))
			{
				List<SpecialAccess> specialAccess = specialAccessDao.selectByForum(forum.getId());
				forum.getSpecialAccess().addAll(specialAccess);
			}
			
			// grades
			if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
			{
				Grade grade = gradeDao.selectByForum(forum.getId());
				forum.setGrade(grade);
			}
		}
		
		return forums;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Forum selectById(int forumId)
	{
		// all site forums
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT forum_id, categories_id, forum_name, forum_desc, forum_order, forum_topics, ");
		sql.append("forum_last_post_id, moderated, start_date, hide_until_open, end_date, allow_until_date, forum_type, forum_access_type, ");
		sql.append("forum_grade_type, lock_end_date FROM jforum_forums WHERE forum_id = ?");

		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] = forumId;
		
		final List<Forum> forums =  new ArrayList<Forum>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Forum forum = fillForum(result);
					forums.add(forum);
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectById: " + e, e);
					}
					return null;
				}
			}

			
		});	
		
		Forum forum = null;
		
		if (forums.size() == 1)
		{
			forum = forums.get(0);			

			// groups
			if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
			{
				forum.setGroups(getForumGroups(forum.getId()));
			}
			
			// special access
			if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))
			{
				List<SpecialAccess> specialAccess = specialAccessDao.selectByForum(forum.getId());
				forum.getSpecialAccess().addAll(specialAccess);
			}
			
			// grades
			if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
			{
				Grade grade = gradeDao.selectByForum(forum.getId());
				forum.setGrade(grade);
			}
		
		}
		
		return forum;
	}
	
	/**
	 * @param categoryDao the categoryDao to set
	 */
	public void setCategoryDao(CategoryDao categoryDao)
	{
		this.categoryDao = categoryDao;
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
	 * @param jforumCategoryService the jforumCategoryService to set
	 */
	public void setJforumCategoryService(JForumCategoryService jforumCategoryService)
	{
		this.jforumCategoryService = jforumCategoryService;
	}
	
	/**
	 * @param jforumPostService the jforumPostService to set
	 */
	public void setJforumPostService(JForumPostService jforumPostService)
	{
		this.jforumPostService = jforumPostService;
	}
	
	public void setJforumSecurityService(JForumSecurityService jforumSecurityService)
	{
		this.jforumSecurityService = jforumSecurityService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setLastPost(int forumId, int postId)
	{
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE jforum_forums SET forum_last_post_id = ? WHERE forum_id = ?");
		
		Object[] fields = new Object[2];
		int i = 0;
		
		fields[i++] = postId;
		fields[i++] = forumId;
		
		if (!sqlService.dbWrite(null, sql.toString(), fields))
		{
			throw new RuntimeException("setLastPost: db write failed");
		}
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
	 * @param topicDao the topicDao to set
	 */
	public void setTopicDao(TopicDao topicDao)
	{
		this.topicDao = topicDao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void update(Forum forum)
	{
		if ((forum == null) || (forum.getName() == null) || (forum.getName().trim().length() == 0) ||  (forum.getCategoryId() == 0))
		{
			throw new IllegalArgumentException("forum information is missing");
		}
		
		if (forum.getId() == 0)
		{
			throw new IllegalArgumentException("Forum cannot be edited as forum information is missing.");
		}
		
		if (forum.getName() != null && forum.getName().length() > 150)
		{
			forum.setName(forum.getName().substring(0, 149));
		}
		
		/*if (forum.getDescription() != null && forum.getDescription().length() > 255)
		{
			forum.setDescription(forum.getDescription().substring(0, 254));
		}*/
		
		updateTx(forum);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Forum updateOrder(Forum forum, Forum other)
	{
		if (forum.getCategoryId() != other.getCategoryId())
		{
			return null;
		}
		
		if (forum.getOrder() <= 0 || other.getOrder() <= 0)
		{
			return null;
		}
		
		return updateOrderTx(forum, other);
	}
	
	/**
	 * Adds forums groups
	 * 
	 * @param forum	Forum with groups
	 */
	protected void addForumGroups(Forum forum)
	{
		if (forum.getAccessType() != Forum.ForumAccess.GROUPS.getAccessType())
		{
			return;
		}
		
		String sql = "INSERT INTO jforum_forum_sakai_groups (forum_id, sakai_group_id) VALUES (?, ?)";
		
		List<String> groups = forum.getGroups();
			
		if (groups != null && groups.size() > 0)
		{
			for (String groupId : groups)
			{
				Object[] fields = new Object[2];
				int i = 0;
				fields[i++] = forum.getId();
				fields[i++] = groupId;
				
				try
				{
					this.sqlService.dbWrite(sql, fields);
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e, e);
					}
					
					throw new RuntimeException("addForumGroups: dbInsert failed");
				}
			}
		}
		
		
	}
	
	/**
	 * Creates new forum and grade if gradable and the forum groups if forum has groups
	 * 
	 * @param forum
	 * @return
	 */
	protected int creatNewForumTx(final Forum forum)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					/*create new forum*/
					int forumId = insertForum(forum);
					((ForumImpl)forum).setId(forumId);
					
					// forum groups
					addForumGroups(forum);
					
					// create grade if forum is gradable.
					if ((forum.getGradeType() == Grade.GradeType.FORUM.getType()) && (forum.getGrade() != null))
					{
						//set the grade context in the impl
						forum.getGrade().setType(Grade.GradeType.FORUM.getType());
						forum.getGrade().setCategoryId(0);
						forum.getGrade().setForumId(forum.getId());
						forum.getGrade().setTopicId(0);
						
						gradeDao.addNew(forum.getGrade());
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					// remove any external entries like entry in grade book if forum is gradable if created
					throw new RuntimeException("Error while creating new forum.", e);
				}
			}
		}, "creatNewForum: " + forum.getName());
		
		
		return forum.getId();
		
	}
	
	/**
	 * Deletes forum groups
	 * 
	 * @param forumId	The forum id
	 */
	protected void deleteForumGroups(int forumId)
	{
		String sql = "DELETE FROM jforum_forum_sakai_groups WHERE forum_id = ?";
		
		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] = forumId;
		
		try
		{
			this.sqlService.dbWrite(sql, fields);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e, e);
			}
			
			throw new RuntimeException("deleteForumGroups: dbWrite failed");
		}
	}
	
	/**
	 * deletes the forum and it's special access, groups, grade
	 * @param forum
	 */
	protected void deleteTx(final Forum forum)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					// forum with dates - delete forum special access
					//List<SpecialAccess> forumSpecialAccessList = specialAccessDao.selectByForum(forum.getId());
					List<SpecialAccess> forumSpecialAccessList = forum.getSpecialAccess();
					
					for (SpecialAccess specialAccess : forumSpecialAccessList)
					{
						specialAccessDao.delete(specialAccess.getId());
					}
					
					// gradable forum - delete forum grade
					if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
					{
						Grade grade = forum.getGrade();
						
						if (grade != null)
						{
							//delete grade
							gradeDao.delete(grade.getId());
						}
					}
					
					// delete Forum Groups
					if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
					{
						deleteForumGroups(forum.getId());
					}
					
					//delete forum
					String sql = "DELETE FROM jforum_forums WHERE forum_id = ?";
					
					Object[] fields = new Object[1];
					int i = 0;
					fields[i++] =  forum.getId();
					
					sqlService.dbWrite(sql, fields);
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}

					throw new RuntimeException("Error while deleting forum.", e);
				}
			}
		}, "delete: " + forum.getName());
	}

	/**
	 * edits forum
	 * 
	 * @param forum		The forum object
	 */
	protected void editForum(Forum forum)
	{
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE jforum_forums SET categories_id = ?, forum_name = ?, forum_desc = ?, start_date = ?, hide_until_open = ?, end_date = ?, allow_until_date = ?, ");
		sql.append("moderated = ?, forum_type = ?, forum_access_type = ?, forum_grade_type = ? WHERE forum_id = ?");
		
		Object[] fields = new Object[12];
		int i = 0;
		fields[i++] = forum.getCategoryId();
		fields[i++] = forum.getName();
		fields[i++] = forum.getDescription();

		if (forum.getAccessDates() != null)
		{
			if (forum.getAccessDates().getOpenDate() == null)
			{
				fields[i++] = null;
				fields[i++] = 0;
			} 
			else
			{
				fields[i++] = new Timestamp(forum.getAccessDates().getOpenDate().getTime());
				fields[i++] = forum.getAccessDates().isHideUntilOpen() ? 1 : 0;
			}
	
			if (forum.getAccessDates().getDueDate() == null)
			{
				fields[i++] = null;
				//fields[i++] = 0;
			} 
			else
			{
				// 08/20/2012 - lock on due is not supported anymore
				fields[i++] = new Timestamp(forum.getAccessDates().getDueDate().getTime());
				//fields[i++] = forum.getAccessDates().isLocked() ? 1 : 0;
			}
			
			if (forum.getAccessDates().getAllowUntilDate() == null)
			{
				fields[i++] = null;
			} 
			else
			{
				fields[i++] = new Timestamp(forum.getAccessDates().getAllowUntilDate().getTime());
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

		fields[i++] = 0;
		fields[i++] = forum.getType();
		fields[i++] = forum.getAccessType();
		fields[i++] = forum.getGradeType();
		fields[i++] = forum.getId();
				
		try
		{
			this.sqlService.dbWrite(sql.toString(), fields);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e, e);
			}
			
			throw new RuntimeException("editForum: dbWrite failed");
		}
		
	}
	
	/**
	 * Deletes and adds groups to the forum
	 * 
	 * @param forum
	 */
	protected void editForumGroups(Forum forum)
	{
		// delete forum groups
		deleteForumGroups(forum.getId());
		
		if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
		{		
			// add groups
			addForumGroups(forum);
		}
	}
	
	/**
	 * Fills the forum object from the result set
	 * 
	 * @param rs	Result set
	 * 
	 * @return	Forum
	 * 
	 * @throws SQLException
	 */
	protected Forum fillForum(ResultSet rs) throws SQLException
	{
		ForumImpl forum = new ForumImpl(this.jforumPostService, this.jforumCategoryService, this.jforumSecurityService);

		forum.setId(rs.getInt("forum_id"));
		forum.setCategoryId(rs.getInt("categories_id"));
		forum.setName(rs.getString("forum_name"));
		forum.setDescription(rs.getString("forum_desc"));
		forum.setOrder(rs.getInt("forum_order"));
		forum.setTotalTopics(rs.getInt("forum_topics"));
		
		// TODO: last post(er) info
		//forum.setLastPostInfo(lastPostInfo);
		forum.setLastPostId(rs.getInt("forum_last_post_id"));
		forum.setType(rs.getInt("forum_type"));
		forum.setAccessType(rs.getInt("forum_access_type"));
		forum.setGradeType(rs.getInt("forum_grade_type"));
		
		if ((rs.getDate("start_date") != null) || (rs.getDate("end_date") != null) || (rs.getDate("allow_until_date") != null))
		{
			AccessDates accessDates = new AccessDatesImpl();
			accessDates.setOpenDate(rs.getTimestamp("start_date"));
			accessDates.setHideUntilOpen(rs.getInt("hide_until_open") > 0);
			
			if (rs.getDate("end_date") != null)
		    {
		      Timestamp endDate = rs.getTimestamp("end_date");
		      accessDates.setDueDate(endDate);
		      // 08/20/2012 - lock on due is not supported anymore
		      // accessDates.setLocked(rs.getInt("lock_end_date") > 0);
		    }
		    else
		    {
		    	accessDates.setDueDate(null);
		    }
			accessDates.setAllowUntilDate(rs.getTimestamp("allow_until_date"));
			
			forum.setAccessDates(accessDates);
		}
		else
		{
			AccessDates accessDates = new AccessDatesImpl();
			forum.setAccessDates(accessDates);
		}
		
		return forum;
	}

	/**
	 * get forum groups
	 * 
	 * @param forum	Forum
	 * 
	 * @return	List of forum groups
	 */
	protected List<String> getForumGroups(int forumId)
	{
		String sql;
		Object[] fields;
		int i = 0;
		
		sql = "SELECT forum_id, sakai_group_id from jforum_forum_sakai_groups where forum_id = ?";						
				
		fields = new Object[1];
		fields[i++] = forumId;
		

		final List<String> groups = new ArrayList<String>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					groups.add(result.getString("sakai_group_id"));

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getForumGroups: " + e, e);
					}
					return null;
				}
			}
		});
		
		return groups;
	}

	/**
	 * Gets the maximum display order of the current forums in the database
	 * 
	 * @return	The maximum display order of the forums
	 */
	protected int getMaxDisplayOrder()
	{
		final List<Integer> displayOrderList =  new ArrayList<Integer>();
		
		String sql = "SELECT MAX(forum_order) FROM jforum_forums";

		Object[] fields = null;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					displayOrderList.add(new Integer(result.getInt(1)));
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getMaxDisplayOrder: " + e, e);
					}					
				}
				return null;
			}
		});
		
		int maxDisplayOrder = -1;
		if (displayOrderList.size() == 1)
		{
			maxDisplayOrder = displayOrderList.get(0).intValue();
		}
		return maxDisplayOrder;
	}

	protected abstract int insertForum(Forum forum);
	
	/**
	 * Get forums
	 * 
	 * @param sql	The query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return List of forums belong to the course
	 */
	protected List<Forum> readForums(String sql, Object[] fields)
	{
		final List<Forum> forums =  new ArrayList<Forum>();
						
		this.sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Forum forum = fillForum(result);
					
					forum.setTotalPosts(result.getInt("total_posts"));
					
					forums.add(forum);
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readForums: " + e, e);
					}
					return null;
				}
			}

			
		});		
		
		return forums;
	}
	
	/**
	 * Updates the forum order and adjusts the order for the other forums in the category
	 * 
	 * @param forum			Forum the order to be updated
	 * 
	 * @param related		Related forum in the current position
	 * 
	 * @return		The updated forum
	 */
	protected Forum updateOrderTx(final Forum forum, final Forum related)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					int curOrder = forum.getOrder();
					int newOrder = related.getOrder();					

					forum.setOrder(newOrder);
							
					List<Forum> forums = selectByCategoryId(forum.getCategoryId());
					
					if (curOrder < newOrder)
					{
						// move forums above and including existing forum in new position one level up
						for (Forum f : forums)
						{
							if ((f.getOrder() > curOrder) && (f.getOrder() <= newOrder))
							{
								
								String sql = "UPDATE jforum_forums SET forum_order = ? WHERE forum_id = ?";
								
								Object[] fields = new Object[2];
								int i = 0;
								fields[i++] = f.getOrder() - 1;
								fields[i++] = f.getId();
								
								try
								{
									sqlService.dbWrite(sql, fields);
								}
								catch (Exception e)
								{
									if (logger.isErrorEnabled())
									{
										logger.error(e, e);
									}
									
									throw new RuntimeException("updateOrder: Other forums order update failed");
								}
							}
						}
						
						// actual forum that need to reset it's order
						String sql = "UPDATE jforum_forums SET forum_order = ? WHERE forum_id = ?";
						
						Object[] fields = new Object[2];
						int i = 0;
						fields[i++] = forum.getOrder();
						fields[i++] = forum.getId();
						
						try
						{
							sqlService.dbWrite(sql, fields);
						}
						catch (Exception e)
						{
							if (logger.isErrorEnabled())
							{
								logger.error(e, e);
							}
							
							throw new RuntimeException("updateOrder: forum order update failed");
						}
					}
					else if (curOrder > newOrder)
					{
						// move forums above and including existing forum in new position one level down
						for (Forum f : forums)
						{
							if ((f.getOrder() >= newOrder) && (f.getOrder() < curOrder))
							{
								String sql = "UPDATE jforum_forums SET forum_order = ? WHERE forum_id = ?";
								
								Object[] fields = new Object[2];
								int i = 0;
								fields[i++] = f.getOrder() + 1;
								fields[i++] = f.getId();
								
								try
								{
									sqlService.dbWrite(sql, fields);
								}
								catch (Exception e)
								{
									if (logger.isErrorEnabled())
									{
										logger.error(e, e);
									}
									
									throw new RuntimeException("updateOrder: Other forums order update failed");
								}
							}
						}
						
						// actual forum that need to reset it's order
						String sql = "UPDATE jforum_forums SET forum_order = ? WHERE forum_id = ?";
						
						Object[] fields = new Object[2];
						int i = 0;
						fields[i++] = forum.getOrder();
						fields[i++] = forum.getId();
						
						try
						{
							sqlService.dbWrite(sql, fields);
						}
						catch (Exception e)
						{
							if (logger.isErrorEnabled())
							{
								logger.error(e, e);
							}
							
							throw new RuntimeException("updateOrder: forum order update failed");
						}
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}

					throw new RuntimeException("Error while updating forum.", e);
				}
			}
		}, "updateOrder: " + forum.getName());
		
		return selectById(forum.getId());
	}
	
	/**
	 * Edits forum and it's groups, grade, special access
	 * 
	 * @param forum		Forum object
	 */
	protected void updateTx(final Forum forum)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					// check for existing forum
					Forum exisForum = selectById(forum.getId());
					
					if (exisForum == null)
					{
						return;
					}
					
					Category category = categoryDao.selectById(forum.getCategoryId());
					
					if (category == null)
					{
						return;
					}
					
					// check for exiting forum category and modified forum category and check the dates, grades etc
					if (exisForum.getCategoryId() != forum.getCategoryId())
					{
						if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
						{
							forum.setAccessDates(null);
							
							// if category has dates forum topics cannot have dates
							int topicDatesCount = topicDao.selectTopicDatesCountByForum(forum.getId());
							
							if (topicDatesCount > 0)
							{
								List<Topic> topicsWithDates = topicDao.selectTopicsWithDatesByForum(forum.getId());
								
								for (Topic topic: topicsWithDates)
								{
									topic.getAccessDates().setOpenDate(null);
									topic.getAccessDates().setDueDate(null);
									topic.getAccessDates().setAllowUntilDate(null);
									
									topicDao.updateTopic(topic);
								}
							}
						}							
						
						if (category.isGradable())
						{
							forum.setGradeType(Grade.GradeType.DISABLED.getType());
						}
					}
					
					// update forum
					editForum(forum);
					
					// forum groups
					editForumGroups(forum);					
					
					
					//TODO: check evaluations
					
					// forum grade
					if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
					{
						if (forum.getGrade() != null)
						{
							// existing grade
							Grade exisGrade = gradeDao.selectByForum(forum.getId());
							
							if (exisGrade == null)
							{
								//set the grade context in the impl
								forum.getGrade().setType(Grade.GradeType.FORUM.getType());
								forum.getGrade().setCategoryId(0);
								forum.getGrade().setForumId(forum.getId());
								forum.getGrade().setTopicId(0);
								
								gradeDao.addNew(forum.getGrade());
							}
							else
							{
								//set the grade context in the impl
								exisGrade.setType(Grade.GradeType.FORUM.getType());
								exisGrade.setPoints(forum.getGrade().getPoints());
								exisGrade.setContext(forum.getGrade().getContext());
								exisGrade.setAddToGradeBook(forum.getGrade().isAddToGradeBook());
								exisGrade.setMinimumPostsRequired(forum.getGrade().isMinimumPostsRequired());
								exisGrade.setMinimumPosts(forum.getGrade().getMinimumPosts());
								exisGrade.setCategoryId(0);
								exisGrade.setTopicId(0);
								
								gradeDao.updateForumGrade(exisGrade);
								
							}
						}
						else
						{
							// remove forum grade and update forum grade type
							gradeDao.delete(forum.getId(), 0);
							
							forum.setGradeType(Grade.GradeType.DISABLED.getType()); 
							editForum(forum);
						}
					}
					else if (forum.getGradeType() == Grade.GradeType.TOPIC.getType())
					{
						// remove forum grade
						gradeDao.deleteForumGrade(forum.getId());
					}
					else
					{
						// remove forum grade
						gradeDao.deleteForumGrade(forum.getId());
					}
					
					// delete topic grades if category is gradable or forum is gradable
					if (category.isGradable() || (forum.getGradeType() == Grade.GradeType.FORUM.getType()) || forum.getGradeType() == Grade.GradeType.DISABLED.getType())
					{
						List<Grade> topicGrades = gradeDao.selectTopicGradesByForumId(forum.getId());
						
						for (Grade grade : topicGrades)
						{
							gradeDao.delete(grade.getId());
							// update topic for grade type
							Topic topic = topicDao.selectById(grade.getTopicId());
							if (topic != null)
							{
								topic.setGradeTopic(Boolean.FALSE);
								topicDao.updateTopic(topic);
							}
						}					
					}
					
					// remove existing special access if no dates
					if (forum.getAccessDates() == null || ((forum.getAccessDates().getOpenDate() == null) && (forum.getAccessDates().getDueDate() == null) && (forum.getAccessDates().getAllowUntilDate() == null)))
					{
						List<SpecialAccess> forumSpecialAccess = specialAccessDao.selectByForum(forum.getId());
						for (SpecialAccess specialAccess : forumSpecialAccess)
						{
							specialAccessDao.delete(specialAccess.getId());
						}
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					// remove any external entries like entry in grade book if forum is gradable if created
					throw new RuntimeException("Error while updating forum.", e);
				}
			}
		}, "editForum: " + forum.getName());
				
	
	}
}
