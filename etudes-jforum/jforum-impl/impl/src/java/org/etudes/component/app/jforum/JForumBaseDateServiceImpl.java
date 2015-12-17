/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumBaseDateServiceImpl.java $ 
 * $Id: JForumBaseDateServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.AccessDates;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.JForumBaseDateService;
import org.etudes.api.app.jforum.JForumGBService;
import org.etudes.api.app.jforum.JForumGradeService;
import org.etudes.api.app.jforum.Topic;
import org.etudes.component.app.jforum.util.JForumUtil;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.tool.cover.ToolManager;

public class JForumBaseDateServiceImpl implements JForumBaseDateService
{
	private static Log logger = LogFactory.getLog(JForumBaseDateServiceImpl.class);

	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	/** Dependency: JForumGBService */
	protected JForumGBService jforumGBService = null;	
	
	/**
	 * @see org.etudes.api.app.jforum.JForumBaseDateService#applyBaseDateTx(String, int)
	 */
	public void applyBaseDateTx(String course_id, int days_diff)
	{
		if (course_id == null)
		{
			throw new IllegalArgumentException("applyBaseDateTx: course_id is null");
		}
		if (days_diff == 0)
		{
			return;
		}
		
		// forum dates
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE JFORUM_FORUMS JF, JFORUM_SAKAI_COURSE_CATEGORIES JSCC SET");
		sql.append(" JF.START_DATE=DATE_ADD(JF.START_DATE,INTERVAL ? DAY), JF.END_DATE=DATE_ADD(JF.END_DATE,INTERVAL ? DAY), JF.ALLOW_UNTIL_DATE=DATE_ADD(JF.ALLOW_UNTIL_DATE,INTERVAL ? DAY)");
		sql.append(" WHERE JF.CATEGORIES_ID=JSCC.CATEGORIES_ID AND JSCC.COURSE_ID =?");

		Object[] fields = new Object[4];
		int i = 0;
		fields[i++] = days_diff;
		fields[i++] = days_diff;
		fields[i++] = days_diff;
		fields[i++] = course_id;

		if (!sqlService.dbWrite(sql.toString(), fields)) {
			throw new RuntimeException("applyBaseDate: db write failed");
		}
		
		// category dates
		sql = new StringBuilder();
		sql.append("UPDATE JFORUM_CATEGORIES JC, JFORUM_SAKAI_COURSE_CATEGORIES JSCC SET");
		sql.append(" JC.START_DATE=DATE_ADD(JC.START_DATE,INTERVAL ? DAY), JC.END_DATE=DATE_ADD(JC.END_DATE,INTERVAL ? DAY), JC.ALLOW_UNTIL_DATE=DATE_ADD(JC.ALLOW_UNTIL_DATE,INTERVAL ? DAY)");
		sql.append(" WHERE JC.CATEGORIES_ID=JSCC.CATEGORIES_ID AND JSCC.COURSE_ID =?");

		fields = new Object[4];
		i = 0;
		fields[i++] = days_diff;
		fields[i++] = days_diff;
		fields[i++] = days_diff;
		fields[i++] = course_id;

		if (!sqlService.dbWrite(sql.toString(), fields)) {
			throw new RuntimeException("applyBaseDate: db write failed");
		}
		
		// topic dates
		sql = new StringBuilder();
		sql.append("UPDATE JFORUM_SAKAI_COURSE_CATEGORIES JSCC, JFORUM_FORUMS JF, JFORUM_TOPICS JT SET");
		sql.append(" JT.START_DATE=DATE_ADD(JT.START_DATE,INTERVAL ? DAY), JT.END_DATE=DATE_ADD(JT.END_DATE,INTERVAL ? DAY), JT.ALLOW_UNTIL_DATE=DATE_ADD(JT.ALLOW_UNTIL_DATE,INTERVAL ? DAY)");
		sql.append(" WHERE JSCC.COURSE_ID =? AND JF.CATEGORIES_ID=JSCC.CATEGORIES_ID AND JT.FORUM_ID = JF.FORUM_ID");

		fields = new Object[4];
		i = 0;
		fields[i++] = days_diff;
		fields[i++] = days_diff;
		fields[i++] = days_diff;
		fields[i++] = course_id;

		if (!sqlService.dbWrite(sql.toString(), fields)) {
			throw new RuntimeException("applyBaseDate: db write failed");
		}
		
		// get grades added to gradebook
		sql = new StringBuilder();
		sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id  ");
		sql.append(" FROM jforum_grade j WHERE context = ? AND add_to_gradebook = 1");
		
		fields = new Object[1];
		i = 0;
		fields[i++] = course_id;
		
		final List<Grade> siteGrades = new ArrayList<Grade>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Grade grade = new GradeImpl();
					((GradeImpl)grade).setId(result.getInt("grade_id"));
					grade.setContext(result.getString("context"));
					grade.setType(result.getInt("grade_type"));
					grade.setForumId(result.getInt("forum_id"));
					grade.setTopicId(result.getInt("topic_id"));
					grade.setPoints(result.getFloat("points"));
					grade.setAddToGradeBook(result.getInt("add_to_gradebook") == 1);
					grade.setCategoryId(result.getInt("categories_id"));
					
					siteGrades.add(grade);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("read site grades: " + e, e);
					}
					return null;
				}
			}
		});
		
		// grading is only at one level either category or forum or topic and same goes with dates
		for (Grade siteGrade : siteGrades)
		{
			if (siteGrade.getType() == Grade.GradeType.CATEGORY.getType())
			{
				updateGradebookCategory(siteGrade);
			}
			else if (siteGrade.getType() == Grade.GradeType.FORUM.getType())
			{
				updateGradebookForum(siteGrade);
			}
			else if (siteGrade.getType() == Grade.GradeType.TOPIC.getType())
			{
				updateGradebookTopic(siteGrade);
			}				
		}
	}

	public void destroy()
	{
		if (logger.isInfoEnabled())
			logger.info("destroy....");
	}

	/**
	 * @return the jforumGBService
	 */
	public JForumGBService getJforumGBService()
	{
		return jforumGBService;
	}
	
	/**
	 * @see org.etudes.api.app.jforum.JForumBaseDateService#getMaxStartDate(String)
	 */
	public Date getMaxStartDate(String courseId)
	{
		
		if (courseId == null)
		{
			return null;
		}

		PreparedStatement p = null;
		ResultSet rs = null;

		Connection conn = null;

		Date maxDate = null;
		
		try
		{
			// forum
			conn = sqlService.borrowConnection();
			String selectMinStartDate = "SELECT MAX(f.start_date) AS start_date, MAX(f.end_date) AS end_date, MAX(f.allow_until_date) AS allow_until_date "
					+ "FROM jforum_sakai_course_categories cc, jforum_categories c, jforum_forums f WHERE cc.course_id = ? "
					+ "AND cc.categories_id = c.categories_id AND c.categories_id = f.categories_id";

			p = conn.prepareStatement(selectMinStartDate);
			p.setString(1, courseId);

			rs = p.executeQuery();

			if (rs.next())
			{
				Date startDate = null, endDate = null, allowUntilDate = null;
				
				startDate = rs.getTimestamp("start_date");
				endDate = rs.getTimestamp("end_date");
				allowUntilDate = rs.getTimestamp("allow_until_date");
				
				maxDate = getMax(startDate, endDate, allowUntilDate);
			}
			
			rs.close();
			p.close();
			
			// category 
			p = null;
			rs = null;
			
			String selectCatMinStartDate = "SELECT MAX(c.start_date) AS start_date, MAX(c.end_date) AS end_date, MAX(c.allow_until_date) AS allow_until_date  "
					+ "FROM jforum_categories c, jforum_sakai_course_categories jscc "
					+ "WHERE c.categories_id = jscc.categories_id AND jscc.course_id = ?";
			
			p = conn.prepareStatement(selectCatMinStartDate);
			p.setString(1, courseId);

			rs = p.executeQuery();

			if (rs.next())
			{
				Date startDate = null, endDate = null, allowUntilDate = null;
				startDate = rs.getTimestamp("start_date");
				endDate = rs.getTimestamp("end_date");
				allowUntilDate = rs.getTimestamp("allow_until_date");
				
				if (maxDate == null)
				{
					//Get the maximum of cat start and end date
					maxDate = getMax(startDate, endDate, allowUntilDate);
				}
				else
				{
					//Get the max of cat start and end date
					Date maxCatDate = getMax(startDate, endDate, allowUntilDate);
					maxDate = getMax(maxDate, maxCatDate);
				}	
			}
			
			// topics
			p = null;
			rs = null;
			
			String selectTopicMinStartDate = "SELECT MAX(t.start_date) AS start_date,  MAX(t.end_date) AS end_date, MAX(t.allow_until_date) AS allow_until_date "
					+ "FROM jforum_sakai_course_categories cc, jforum_categories c, jforum_forums f, jforum_topics t "
					+ "WHERE cc.course_id = ? AND cc.categories_id = c.categories_id AND c.categories_id = f.categories_id AND f.forum_id = t.forum_id";
			
			p = conn.prepareStatement(selectTopicMinStartDate);
			p.setString(1, courseId);

			rs = p.executeQuery();
			
			if (rs.next())
			{
				Date startDate = null, endDate = null, allowUntilDate = null;
				startDate = rs.getTimestamp("start_date");
				endDate = rs.getTimestamp("end_date");
				allowUntilDate = rs.getTimestamp("allow_until_date");
				
				if (maxDate == null)
				{
					//Get the maximum of topic start and end date
					maxDate = getMax(startDate, endDate, allowUntilDate);
				}
				else
				{
					//Get the max of topic start and end date
					Date maxTopicDate = getMax(startDate, endDate, allowUntilDate);
					maxDate = getMax(maxDate, maxTopicDate);
				}
			}
		}
		catch (SQLException e)
		{
			if (logger.isWarnEnabled())
				logger.warn(this.getClass().getName() + ".getMaxStartDate() : " + e.getMessage(), e);
		}
		finally
		{
			if (rs != null)
			{
				try
				{
					rs.close();
				}
				catch (SQLException e)
				{
				}
			}

			if (p != null)
			{
				try
				{
					p.close();
				}
				catch (SQLException e)
				{
				}
			}

			if (conn != null)
			{
				sqlService.returnConnection(conn);
			}
		}
		
		return maxDate;
	}
	
	/**
	 * @see org.etudes.api.app.jforum.JForumBaseDateService#getMinStartDate(String)
	 */
	public Date getMinStartDate(String course_id)
	{
		if (course_id == null)
		{
			return null;
		}

		PreparedStatement p = null;
		ResultSet rs = null;

		Connection conn = null;

		Date minDate = null;

		try
		{
			conn = sqlService.borrowConnection();
			String selectMinStartDate = "SELECT MIN(f.start_date) AS start_date, MIN(f.end_date) AS end_date, MIN(f.allow_until_date) AS allow_until_date "
					+ "FROM jforum_sakai_course_categories cc, jforum_categories c, jforum_forums f WHERE cc.course_id = ? "
					+ "AND cc.categories_id = c.categories_id AND c.categories_id = f.categories_id";

			p = conn.prepareStatement(selectMinStartDate);
			p.setString(1, course_id);

			rs = p.executeQuery();

			if (rs.next())
			{
				Date startDate = null, endDate = null, allowUntilDate = null;
				
				startDate = rs.getTimestamp("start_date");
				endDate = rs.getTimestamp("end_date");
				allowUntilDate = rs.getTimestamp("allow_until_date");
				
				minDate = getMin(startDate, endDate, allowUntilDate);
			}
			
			rs.close();
			p.close();
			
			// category 
			p = null;
			rs = null;
			
			String selectCatMinStartDate = "SELECT MIN(c.start_date) AS start_date, MIN(c.end_date) AS end_date , MIN(c.allow_until_date) AS allow_until_date "
					+ "FROM jforum_categories c, jforum_sakai_course_categories jscc "
					+ "WHERE c.categories_id = jscc.categories_id AND jscc.course_id = ?";
			
			p = conn.prepareStatement(selectCatMinStartDate);
			p.setString(1, course_id);

			rs = p.executeQuery();

			if (rs.next())
			{
				Date startDate = null, endDate = null, allowUntilDate = null;;
				startDate = rs.getTimestamp("start_date");
				endDate = rs.getTimestamp("end_date");
				allowUntilDate = rs.getTimestamp("allow_until_date");
				
				if (minDate == null)
				{
					//Get the minimum of cat start and end date
					minDate = getMin(startDate, endDate, allowUntilDate);
				}
				else
				{
					//Get the min of cat start and end date and compare it with minDate
					Date minCatDate = getMin(startDate, endDate, allowUntilDate);
					minDate = getMin(minDate, minCatDate);
				}	
			}
			
			// topics
			p = null;
			rs = null;
			
			String selectTopicMinStartDate = "SELECT MIN(t.start_date) AS start_date,  MIN(t.end_date) AS end_date, MIN(t.allow_until_date) AS allow_until_date "
					+ "FROM jforum_sakai_course_categories cc, jforum_categories c, jforum_forums f, jforum_topics t "
					+ "WHERE cc.course_id = ? AND cc.categories_id = c.categories_id AND c.categories_id = f.categories_id AND f.forum_id = t.forum_id";
			
			p = conn.prepareStatement(selectTopicMinStartDate);
			p.setString(1, course_id);

			rs = p.executeQuery();
			
			if (rs.next())
			{
				Date startDate = null, endDate = null, allowUntilDate = null;;
				startDate = rs.getTimestamp("start_date");
				endDate = rs.getTimestamp("end_date");
				allowUntilDate = rs.getTimestamp("allow_until_date");
				
				if (minDate == null)
				{
					//Get the minimum of topic start and end date
					minDate = getMin(startDate, endDate, allowUntilDate);
				}
				else
				{
					//Get the min of topic start and end date and compare it with minDate
					Date minTopicDate = getMin(startDate, endDate, allowUntilDate);
					minDate = getMin(minDate, minTopicDate);
				}
			}
		}
		catch (SQLException e)
		{
			if (logger.isWarnEnabled())
				logger.warn(this.getClass().getName() + ".getMinStartDate() : " + e.getMessage(), e);
		}
		finally
		{
			if (rs != null)
			{
				try
				{
					rs.close();
				}
				catch (SQLException e)
				{
				}
			}

			if (p != null)
			{
				try
				{
					p.close();
				}
				catch (SQLException e)
				{
				}
			}

			if (conn != null)
			{
				sqlService.returnConnection(conn);
			}
		}

		return minDate;
	}
	
	/**
	 * @return the sqlService
	 */
	public SqlService getSqlService()
	{
		return sqlService;
	}
	
	public void init()
	{
		if (logger.isInfoEnabled())
			logger.info("init....");
	}
	
	/**
	 * @param jforumGBService the jforumGBService to set
	 */
	public void setJforumGBService(JForumGBService jforumGBService)
	{
		this.jforumGBService = jforumGBService;
	}	
	
	/**
	 * @param sqlService
	 *            the sqlService to set
	 */
	public void setSqlService(SqlService sqlService)
	{
		this.sqlService = sqlService;
	}
	
	/**
	 * Get the category 
	 * 
	 * @param categoryId	Category id
	 * 
	 * @return	The category
	 */
	protected Category getCategory(int categoryId)
	{
		StringBuilder sql;
		Object[] fields;
		int i;
		// get category
		sql = new StringBuilder();
		sql.append("SELECT categories_id, title, gradable, start_date, end_date, lock_end_date  ");
		sql.append(" FROM jforum_categories c WHERE categories_id = ?");
		
		fields = new Object[1];
		i = 0;
		fields[i++] = categoryId;
		
		final List<Category> siteCategory = new ArrayList<Category>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					CategoryImpl category = new CategoryImpl();
					category.setId(result.getInt("categories_id"));
					category.setTitle(result.getString("title"));							
					category.setGradable(result.getInt("gradable") == 1);
					
					if ((result.getDate("start_date") != null) || (result.getDate("end_date") != null))
					{
						AccessDates accessDates = new AccessDatesImpl();
						accessDates.setOpenDate(result.getTimestamp("start_date"));
						if (result.getDate("end_date") != null)
					    {
					      Timestamp endDate = result.getTimestamp("end_date");
					      accessDates.setDueDate(endDate);
					      // accessDates.setLocked(result.getInt("lock_end_date") > 0);
					    }
					    else
					    {
					    	accessDates.setDueDate(null);
					    }
						category.setAccessDates(accessDates);
					}
					
					siteCategory.add(category);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getCategory(categoryId): " + e, e);
					}
					return null;
				}
			}
		});
		
		Category category = null;
		if (siteCategory.size() == 1)
		{
			category = siteCategory.get(0);
		}
		
		return category;
	}

	/**
	 * Gets the forum
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return	The forum
	 */
	protected Forum getForum(int forumId)
	{
		StringBuilder sql;
		Object[] fields;
		int i;
		// get forum
		sql = new StringBuilder();
		sql.append("SELECT forum_id, categories_id, forum_name, forum_desc, start_date, end_date, forum_type, forum_access_type, forum_grade_type, lock_end_date  ");
		sql.append(" FROM jforum_forums WHERE forum_id = ?");
		
		fields = new Object[1];
		i = 0;
		fields[i++] = forumId;
		
		final List<Forum> siteForum = new ArrayList<Forum>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					ForumImpl forum = new ForumImpl();
					forum.setId(result.getInt("forum_id"));
					forum.setCategoryId(result.getInt("categories_id"));
					forum.setName(result.getString("forum_name"));
					forum.setDescription(result.getString("forum_desc"));
					forum.setType(result.getInt("forum_type"));
					forum.setGradeType(result.getInt("forum_grade_type"));
					
					if ((result.getDate("start_date") != null) || (result.getDate("end_date") != null))
					{
						AccessDates accessDates = new AccessDatesImpl();
						accessDates.setOpenDate(result.getTimestamp("start_date"));
						if (result.getDate("end_date") != null)
					    {
					      Timestamp endDate = result.getTimestamp("end_date");
					      accessDates.setDueDate(endDate);
					      // accessDates.setLocked(result.getInt("lock_end_date") > 0);
					    }
					    else
					    {
					    	accessDates.setDueDate(null);
					    }
						forum.setAccessDates(accessDates);
					}
					
					siteForum.add(forum);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("read site grades: " + e, e);
					}
					return null;
				}
			}
		});
		
		Forum forum = null;
		if (siteForum.size() == 1)
		{
			forum = siteForum.get(0);
		}
		return forum;
	}
	
	/**
	 * Compares the dates and gets the maximum date
	 * 
	 * @param dates	Dates
	 * 
	 * @return	The minimum date
	 */
	protected Date getMax(Date... dates)
	{
		Date minDate = null;
		
		//compare
		for (Date date : dates)
		{
			if (date == null)
			{
				continue;
			}
			
			if (minDate == null)
			{
				minDate = date;
			}
			else
			{
				if (date.after(minDate))
				{
					minDate = date;
				}
			}
		}
		
		return minDate;
	}
	
	/**
	 * Compares the dates and gets the minimum date
	 * 
	 * @param dates	Dates
	 * 
	 * @return	The maximum date
	 */
	protected Date getMin(Date... dates)
	{
		Date minDate = null;
		
		//compare
		for (Date date : dates)
		{
			if (date == null)
			{
				continue;
			}
			
			if (minDate == null)
			{
				minDate = date;
			}
			else
			{
				if (date.before(minDate))
				{
					minDate = date;
				}
			}
		}
		
		return minDate;
	}
	
	/**
	 * Gets the forum
	 * 
	 * @param topicId	Topic id
	 * 
	 * @return	The topic
	 */
	protected Topic getTopic(int topicId)
	{
		StringBuilder sql;
		Object[] fields;
		int i;
		// get topic
		sql = new StringBuilder();
		sql.append("SELECT topic_id, forum_id, topic_title, user_id, topic_time, topic_status, topic_type, topic_first_post_id, topic_last_post_id, topic_grade, topic_export, start_date, end_date, lock_end_date ");
		sql.append(" FROM jforum_topics WHERE topic_id = ?");
		
		fields = new Object[1];
		i = 0;
		fields[i++] = topicId;
		
		final List<Topic> siteTopic = new ArrayList<Topic>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					TopicImpl topic = new TopicImpl();
					topic.setId(result.getInt("topic_id"));
					topic.setForumId(result.getInt("forum_id"));
					topic.setTitle(result.getString("topic_title"));
					topic.setTime(result.getTimestamp("topic_time"));
					topic.setType(result.getInt("topic_type"));
					topic.setFirstPostId(result.getInt("topic_first_post_id"));
					topic.setLastPostId(result.getInt("topic_last_post_id"));
					topic.setGradeTopic(result.getInt("topic_grade") == 1);
					topic.setExportTopic(result.getInt("topic_export") == 1);
					
					if ((result.getDate("start_date") != null) || (result.getDate("end_date") != null))
					{
						AccessDates accessDates = new AccessDatesImpl();
						accessDates.setOpenDate(result.getTimestamp("start_date"));
						if (result.getDate("end_date") != null)
					    {
					      Timestamp endDate = result.getTimestamp("end_date");
					      accessDates.setDueDate(endDate);
					      // accessDates.setLocked(result.getInt("lock_end_date") > 0);
					    }
					    else
					    {
					    	accessDates.setDueDate(null);
					    }
						topic.setAccessDates(accessDates);
					}
					
					siteTopic.add(topic);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("read site grades: " + e, e);
					}
					return null;
				}
			}
		});
		
		Topic topic = null;
		if (siteTopic.size() == 1)
		{
			topic = siteTopic.get(0);
		}
		return topic;
	}

	/**
	 * Update gradebook for the due date
	 * 
	 * @param gradebookUid	Gradebook unique identifier
	 * @param externalId	External id
	 * @param externalUrl	External url
	 * @param title			Title
	 * @param points		Points
	 * @param dueDate		Due date
	 */
	protected void updateGradeBook(String gradebookUid, String url, Float points, String externalId, String title, Date dueDate)
	{
		if (jforumGBService.isGradebookDefined(gradebookUid))
		{				
			boolean existing = jforumGBService.isExternalAssignmentDefined(gradebookUid, externalId);
			
			if (existing)
			{
				jforumGBService.updateExternalAssessment(gradebookUid, externalId, url, title, JForumUtil.toDoubleScore(points), dueDate);
			}
		}
	}

	/**
	 * Update category due date in the gradebook
	 * 
	 * @param catGrade	Category Grade
	 */
	protected void updateGradebookCategory(Grade catGrade)
	{
		Category category = getCategory(catGrade.getCategoryId());
		
		if (category != null)
		{
			if ((category.isGradable()) && (category.getAccessDates() != null) && (category.getAccessDates().getDueDate() != null))
			{
				String gradebookUid = ToolManager.getInstance().getCurrentPlacement().getContext();
				String url = null;
				Float points = catGrade.getPoints();
				String externalId = JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT   + String.valueOf(catGrade.getId());
				String title = category.getTitle();
				Date dueDate = category.getAccessDates().getDueDate();
				
				updateGradeBook(gradebookUid, url, points, externalId, title, dueDate);
			}
		}
	}

	/**
	 * Update forum due date in the gradebook
	 * 
	 * @param forumGrade	Forum Grade
	 */
	protected void updateGradebookForum(Grade forumGrade)
	{
		Forum forum = getForum(forumGrade.getForumId());
		
		if (forum != null && forum.getGradeType() == Grade.GradeType.FORUM.getType())
		{
			String gradebookUid = ToolManager.getInstance().getCurrentPlacement().getContext();
			String url = null;
			Float points = forumGrade.getPoints();
			String externalId = JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT   + String.valueOf(forumGrade.getId());
			String title = forum.getName();
			
			// check forum dates
			if (forum.getAccessDates() != null)
			{
				if (forum.getAccessDates().getDueDate() != null)
				{
					Date dueDate = forum.getAccessDates().getDueDate();
					updateGradeBook(gradebookUid, url, points, externalId, title, dueDate);
				}
			}
			else
			{
				// check category dates
				Category category = getCategory(forum.getCategoryId());
				
				if ((category != null) && (category.getAccessDates() != null) && (category.getAccessDates().getDueDate() != null))
				{
					Date dueDate = category.getAccessDates().getDueDate();
					updateGradeBook(gradebookUid, url, points, externalId, title, dueDate);
				}
			}
		}
	}

	/**
	 * Update topic due date in the gradebook
	 * 
	 * @param topicGrade	Topic Grade
	 */
	protected void updateGradebookTopic(Grade topicGrade)
	{
		Topic topic = getTopic(topicGrade.getTopicId());
		
		if (topic != null && topic.isGradeTopic())
		{
			String gradebookUid = ToolManager.getInstance().getCurrentPlacement().getContext();
			String url = null;
			Float points = topicGrade.getPoints();
			String externalId = JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT   + String.valueOf(topicGrade.getId());
			String title = topic.getTitle();
			
			// check forum dates
			if ((topic.getAccessDates() != null) && (topic.getAccessDates().getDueDate() != null))
			{
				Date dueDate = topic.getAccessDates().getDueDate();
				updateGradeBook(gradebookUid, url, points, externalId, title, dueDate);
			}
			else
			{
				// check forum dates
				Forum forum = getForum(topic.getForumId());
				
				if (forum != null)
				{
					if (forum.getAccessDates() != null)
					{
						if (forum.getAccessDates().getDueDate() != null)
						{
							Date dueDate = forum.getAccessDates().getDueDate();
							updateGradeBook(gradebookUid, url, points, externalId, title, dueDate);
						}
					}
					else
					{
						// check category dates
						Category category = getCategory(forum.getCategoryId());
						
						if ((category != null) && (category.getAccessDates() != null) && (category.getAccessDates().getDueDate() != null))
						{
							Date dueDate = category.getAccessDates().getDueDate();
							updateGradeBook(gradebookUid, url, points, externalId, title, dueDate);
						}
					}
				}
			}
		}
	}
}