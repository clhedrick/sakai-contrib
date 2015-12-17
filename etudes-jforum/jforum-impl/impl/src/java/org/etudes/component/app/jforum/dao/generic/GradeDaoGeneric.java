/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/generic/GradeDaoGeneric.java $ 
 * $Id: GradeDaoGeneric.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Evaluation;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.Topic;
import org.etudes.api.app.jforum.User;
import org.etudes.api.app.jforum.dao.CategoryDao;
import org.etudes.api.app.jforum.dao.ForumDao;
import org.etudes.api.app.jforum.dao.GradeDao;
import org.etudes.api.app.jforum.dao.TopicDao;
import org.etudes.api.app.jforum.dao.UserDao;
import org.etudes.component.app.jforum.EvaluationImpl;
import org.etudes.component.app.jforum.GradeImpl;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;

public abstract class GradeDaoGeneric implements GradeDao
{
	private static Log logger = LogFactory.getLog(GradeDaoGeneric.class);
	
	/** Dependency: CategoryDao */
	protected CategoryDao categoryDao = null;
	
	/** Dependency: ForumDao */
	protected ForumDao forumDao = null;
	
	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	/** Dependency: TopicDao */
	protected TopicDao topicDao = null;
	
	protected UserDao userDao = null;

	/**
	 * {@inheritDoc}
	 */
	public int addNew(Grade grade)
	{
		if (grade == null)
		{
			throw new IllegalArgumentException("Grade is null.");
		}
		
		if (grade.getId() > 0)
		{
			throw new IllegalArgumentException("New Grade cannot be created as grade has id.");
		}
		
		if (grade.getContext() == null || grade.getContext().trim().length() == 0)
		{
			throw new IllegalArgumentException("New Grade cannot be created as there is no grade context.");
		}
		
		if ((grade.getType() == Grade.GradeType.CATEGORY.getType()) && (grade.getForumId() > 0 || grade.getTopicId() > 0 || grade.getCategoryId() == 0))
		{
			new IllegalArgumentException("addNew: category grade information is not correct");
		}
		else if ((grade.getType() == Grade.GradeType.FORUM.getType()) && (grade.getForumId() == 0 || grade.getTopicId() > 0 || grade.getCategoryId() > 0))
		{
			new IllegalArgumentException("addNew: forum grade information is not correct");
		}
		else if ((grade.getType() == Grade.GradeType.TOPIC.getType()) && (grade.getForumId() == 0 || grade.getTopicId() == 0 ||  grade.getCategoryId() > 0))
		{
			new IllegalArgumentException("addNew: topic grade information is not correct");
		}
		
		int gradeId = insertGrade(grade);
		((GradeImpl)grade).setId(gradeId);
		
		return gradeId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addScheduledGradeToGradebook(int gradeId, Date openDate)
	{
		if (gradeId <= 0 || openDate == null)
		{
			throw new IllegalArgumentException("Grade id or open date is missing.");
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO jforum_schedule_grades_gradebook(grade_id, open_date) VALUES (?, ?)");
				
		Object[] fields = new Object[2];
		int i = 0;
		fields[i++] =  gradeId;
		fields[i++] =  new Timestamp(openDate.getTime());
		try
		{
			this.sqlService.dbWrite(sql.toString(), fields);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error("addScheduledGradesToGradebook: Error while adding: "+ e, e);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addUpdateEvaluations(Grade grade, List<Evaluation> evaluations)
	{
		if (grade == null || grade.getContext() == null || grade.getContext().trim().length() == 0 || evaluations.isEmpty())
		{
			return;
		}
		
		if (grade.getType() != Grade.GradeType.CATEGORY.getType() && grade.getType() != Grade.GradeType.FORUM.getType() && grade.getType() != Grade.GradeType.TOPIC.getType())
		{
			return;
		}
		
		addUpdateEvaluationsTx(grade, evaluations);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addUpdateUserEvaluation(Evaluation evaluation)
	{
		if (evaluation == null || evaluation.getGradeId() <= 0 || evaluation.getEvaluatedBySakaiUserId() == null || evaluation.getEvaluatedBySakaiUserId().trim().length() == 0 
							|| evaluation.getSakaiUserId() == null || evaluation.getSakaiUserId().trim().length() == 0)
		{
			return;
		}
		
		addUpdateEvaluationTx(evaluation);
	}
	/**
	 * {@inheritDoc}
	 */
	public boolean checkScheduledGradeToGradebook(int gradeId)
	{
		
		if (gradeId <= 0)
		{
			throw new IllegalArgumentException("Grade id is missing.");
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT grade_id, open_date FROM jforum_schedule_grades_gradebook WHERE grade_id = ?");
		
		int i = 0;
		Object[] fields = new Object[1];
		fields[i++] = gradeId;
					
		final List<Integer> scheduledGrades = new ArrayList<Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					scheduledGrades.add(result.getInt("grade_id"));

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectScheduledGradeToGradebook: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (scheduledGrades.isEmpty())
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(int gradeId)
	{
		//check and delete any evaluations associated
		List<Evaluation> evaluations = selectEvaluations(gradeId);
		
		for (Evaluation evaluation : evaluations)
		{
			deleteEvaluation(evaluation.getId());
		}
		
		// delete existing grade schedule that needs to be published to gradebook
		deleteScheduledGradeToGradebook(gradeId);
		
		String sql = "DELETE FROM jforum_grade WHERE grade_id = ?";
		
		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] =  gradeId;
		
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
			
			throw new RuntimeException("delete(gradeId): dbWrite failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void delete(int forumId, int topicId)
	{
		if (forumId == 0 || topicId == 0) throw new IllegalArgumentException("Topic or forum id is missing.");
		
		// check and delete any evaluations associated
		Grade grade = selectByForumTopic(forumId, topicId);
		
		if (grade == null)
		{
			return;
		}
		
		List<Evaluation> evaluations = selectEvaluations(grade.getId());
			
		for (Evaluation evaluation : evaluations)
		{
			deleteEvaluation(evaluation.getId());
		}
		
		// check and delete the record of existing grade schedule that needs to be published to gradebook 
		if (grade.getItemOpenDate() != null)
		{
			// delete existing grade schedule that needs to be published to gradebook
			deleteScheduledGradeToGradebook(grade.getId());
		}
				
		String sql = "DELETE FROM jforum_grade WHERE forum_id = ? AND topic_id = ?";
		
		Object[] fields = new Object[2];
		int i = 0;
		fields[i++] =  forumId;
		fields[i++] =  topicId;
		
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
			
			throw new RuntimeException("delete(gradeId, topicId): dbWrite failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteCategoryGrade(int categoryId)
	{
		// check and delete any evaluations associated
		Grade grade = selectByCategory(categoryId);
		
		if (grade == null)
		{
			return;
		}
		
		
		List<Evaluation> evaluations = selectEvaluations(grade.getId());
		
		for (Evaluation evaluation : evaluations)
		{
			deleteEvaluation(evaluation.getId());
		}
		
		// check and delete the record of existing grade schedule that needs to be published to gradebook 
		if (grade.getItemOpenDate() != null)
		{
			// delete existing grade schedule that needs to be published to gradebook
			deleteScheduledGradeToGradebook(grade.getId());
		}
		
		String sql = "DELETE FROM jforum_grade WHERE categories_id = ? AND forum_id = 0 AND topic_id = 0";
		
		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] =  categoryId;
		
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
			
			throw new RuntimeException("deleteCategoryGrade(categoryId): dbWrite failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteEvaluation(int evaluationId)
	{

		String sql = "DELETE FROM jforum_evaluations WHERE evaluation_id = ?";
		
		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] =  evaluationId;
		
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
			
			throw new RuntimeException("deleteEvaluation(evaluationId): dbWrite failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteForumGrade(int forumId)
	{

		// check and delete any evaluations associated
		Grade grade = selectByForum(forumId);
		
		if (grade == null)
		{
			return;
		}
		
		List<Evaluation> evaluations = selectEvaluations(grade.getId());
		
		for (Evaluation evaluation : evaluations)
		{
			deleteEvaluation(evaluation.getId());
		}
	
	
		// check and delete the record of existing grade schedule that needs to be published to gradebook 
		if (grade.getItemOpenDate() != null)
		{
			// delete existing grade schedule that needs to be published to gradebook
			deleteScheduledGradeToGradebook(grade.getId());
		}
		
		String sql = "DELETE FROM jforum_grade WHERE categories_id = 0 AND forum_id = ? AND topic_id = 0";
		
		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] =  forumId;
		
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
			
			throw new RuntimeException("deleteForumGrade(forumId): dbWrite failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteScheduledGradeToGradebook(int gradeId)
	{
		String sql = "DELETE FROM jforum_schedule_grades_gradebook WHERE grade_id = ?";
		
		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] =  gradeId;
		
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
			
			throw new RuntimeException("deleteScheduledGradesToGradebook(grade_id): dbWrite failed");			
		}
	}
	/**
	 * {@inheritDoc}
	 */
	public Grade selectByCategory(int categoryId)
	{
		StringBuilder sql = null;
		Object[] fields = null;
		int i = 0;
		
		sql = new StringBuilder();
		//sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts  ");
		//sql.append(" FROM jforum_grade WHERE categories_id = ? AND forum_id = 0 AND topic_id = 0");
		
		sql.append("SELECT g.grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts , s.open_date ");
		sql.append("FROM jforum_grade g LEFT OUTER JOIN jforum_schedule_grades_gradebook s ON g.grade_id = s.grade_id WHERE categories_id = ? AND forum_id = 0 AND topic_id = 0");
		
		fields = new Object[1];
		fields[i++] = categoryId;
		
		return getGrade(sql, fields);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Grade selectByForum(int forumId)
	{
		if (forumId <=0 ) throw new IllegalArgumentException("Forum id is missing");
		
		StringBuilder sql = null;
		Object[] fields = null;
		int i = 0;
		
		sql = new StringBuilder();
		//sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts  ");
		//sql.append(" FROM jforum_grade WHERE categories_id = 0 AND forum_id = ? AND topic_id = 0");
		sql.append("SELECT g.grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts, s.open_date ");
		sql.append("FROM jforum_grade g LEFT OUTER JOIN jforum_schedule_grades_gradebook s ON g.grade_id = s.grade_id ");
		sql.append("WHERE categories_id = 0 AND forum_id = ? AND topic_id = 0");
		
		fields = new Object[1];
		fields[i++] = forumId;
		
		return getGrade(sql, fields);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Grade selectByForumTopic(int forumId, int topicId)
	{
		if (forumId == 0 || topicId == 0) throw new IllegalArgumentException("Topic or forum id is missing.");
		
		StringBuilder sql = null;
		Object[] fields = null;
		int i = 0;
		
		sql = new StringBuilder();
		//sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts  ");
		//sql.append(" FROM jforum_grade WHERE categories_id = 0 AND forum_id = ? AND topic_id = ?");
		sql.append("SELECT g.grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts, s.open_date ");   
		sql.append("FROM jforum_grade g LEFT OUTER JOIN jforum_schedule_grades_gradebook s ON g.grade_id = s.grade_id ");
		sql.append("WHERE categories_id = 0 AND forum_id = ? AND topic_id = ?");
		
		fields = new Object[2];
		fields[i++] = forumId;
		fields[i++] = topicId;
		
		return getGrade(sql, fields);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Grade> selectBySite(String siteId)
	{
		StringBuilder sql = new StringBuilder();
		//sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts, min_posts_required ");
		//sql.append("FROM jforum_grade WHere context = ?");
		sql.append("SELECT g.grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts, min_posts_required, s.open_date ");
		sql.append("FROM jforum_grade g LEFT OUTER JOIN jforum_schedule_grades_gradebook s ON g.grade_id = s.grade_id ");
		sql.append("WHERE context = ?");
		
		int i = 0;
		Object[] fields = new Object[1];
		fields[i++] = siteId;
		
		return getGrades(sql, fields);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Grade> selectBySiteByGradeType(String siteId, int gradeType)
	{
		StringBuilder sql = new StringBuilder();
		//sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts, min_posts_required ");
		//sql.append("FROM jforum_grade WHERE context = ? AND grade_type = ?");
		
		if (gradeType == Grade.GradeType.CATEGORY.getType())
		{
			sql.append("SELECT g.grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts, min_posts_required, s.open_date ");
			sql.append("FROM jforum_grade g LEFT OUTER JOIN jforum_schedule_grades_gradebook s ON g.grade_id = s.grade_id ");
			sql.append("WHERE context = ? AND grade_type = ? AND categories_id > 0 AND forum_id = 0 AND topic_id = 0");
		}
		else if (gradeType == Grade.GradeType.FORUM.getType())
		{
			sql.append("SELECT g.grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts, min_posts_required, s.open_date ");
			sql.append("FROM jforum_grade g LEFT OUTER JOIN jforum_schedule_grades_gradebook s ON g.grade_id = s.grade_id ");
			sql.append("WHERE context = ? AND grade_type = ? AND categories_id = 0 AND forum_id > 0 AND topic_id = 0");
		}
		else if (gradeType == Grade.GradeType.TOPIC.getType())
		{
			sql.append("SELECT g.grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts, min_posts_required, s.open_date ");
			sql.append("FROM jforum_grade g LEFT OUTER JOIN jforum_schedule_grades_gradebook s ON g.grade_id = s.grade_id ");
			sql.append("WHERE context = ? AND grade_type = ? AND categories_id = 0 AND forum_id > 0 AND topic_id > 0");
		}
		else
		{
			return new ArrayList<Grade>();
		}
		
		int i = 0;
		Object[] fields = new Object[2];
		fields[i++] = siteId;
		fields[i++] = gradeType;
		
		return getGrades(sql, fields);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Evaluation> selectCategoryEvaluationsWithPosts(int categoryId, Evaluation.EvaluationsSort evalSort, boolean all)
	{
		List<Evaluation> evaluations = selectCategoryEvaluations(categoryId, all, evalSort);
				
		return evaluations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int selectCategoryGradableForumsCount(int categoryId)
	{
		StringBuilder sql = null;
		Object[] fields = null;
		int i = 0;
		
		sql = new StringBuilder();
		sql.append("SELECT c.categories_id, f.forum_id FROM jforum_categories c, jforum_forums f ");
		sql.append("WHERE c.categories_id = f.categories_id and c.categories_id = ? AND f.forum_grade_type IN (?, ?)");
		
		fields = new Object[3];
		fields[i++] = categoryId;
		fields[i++] = Grade.GradeType.FORUM.getType();
		fields[i++] = Grade.GradeType.TOPIC.getType();
		
		final List<Integer> forums = new ArrayList<Integer>();
		
		if (sql != null && fields != null)
		{
			this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
			{
				public Object readSqlResultRecord(ResultSet result)
				{
					try
					{
						forums.add(Integer.valueOf(result.getInt("forum_id")));
						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("selectForumTopicGradesByCategoryId: " + e, e);
						}
						return null;
					}
				}
			});
		}
		
		return forums.size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Evaluation> selectEvaluations(Grade grade)
	{
		if (grade == null)
		{
			throw new IllegalArgumentException("Grade is null.");
		}
		
		StringBuilder sql;
		Object[] fields;
		int i;
		sql = null;
		fields = null;
		i = 0;	
			
		sql = new StringBuilder();
		sql.append("SELECT evaluation_id, grade_id, user_id, sakai_user_id, score, comments, evaluated_by, evaluated_date, released, reviewed_date ");						
		sql.append(" FROM jforum_evaluations WHERE grade_id = ?");
		
		fields = new Object[1];
		i = 0;
		fields[i++] = grade.getId();
					
		final List<Evaluation> evaluations = new ArrayList<Evaluation>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Evaluation evaluation = new EvaluationImpl();
					((EvaluationImpl)evaluation).setId(result.getInt("evaluation_id"));
					((EvaluationImpl)evaluation).setGradeId(result.getInt("grade_id"));
					((EvaluationImpl)evaluation).setSakaiUserId(result.getString("sakai_user_id"));
					//evaluation.setScore(result.getFloat("score"));
					String str = result.getString("score");
					if (str == null)
					{
						evaluation.setScore(null);
					}
					else
					{
						try
						{
							evaluation.setScore(Float.valueOf(str));
						} 
						catch (NumberFormatException e)
						{
							evaluation.setScore(null);
						}
					}
					((EvaluationImpl)evaluation).setEvaluatedBy(result.getInt("evaluated_by"));
					
					if (result.getDate("evaluated_date") != null)
				    {
						Timestamp dueDate = result.getTimestamp("evaluated_date");
						((EvaluationImpl)evaluation).setEvaluatedDate(dueDate);
				    }
				    else
				    {
				    	((EvaluationImpl)evaluation).setEvaluatedDate(null);
				    }
					
					evaluation.setReleased(result.getInt("released") == 1);
					
					if (result.getDate("reviewed_date") != null)
				    {
						Timestamp reviewedDate = result.getTimestamp("reviewed_date");
				      	evaluation.setReviewedDate(reviewedDate);
				    }
				    else
				    {
				    	((EvaluationImpl)evaluation).setEvaluatedDate(null);
				    }
					
					evaluations.add(evaluation);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getEvaluations: " + e, e);
					}
					return null;
				}
			}
		});
		
		return evaluations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int selectEvaluationsCount(Grade grade)
	{
		if (grade == null)
		{
			throw new IllegalArgumentException("Grade is null.");
		}
		
		int evaluationsCount = 0;
		
		final List<Integer> count = new ArrayList<Integer>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COUNT(1) AS eval_count FROM jforum_evaluations WHERE grade_id = ?");						
				
		Object[] fields;
		int i = 0;
		
		fields = new Object[1];
		fields[i++] = grade.getId();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					count.add(result.getInt("eval_count"));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectEvaluationsCount: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (count.size() == 1)
		{
			evaluationsCount = count.get(0);
		}
		
		return evaluationsCount;	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Evaluation> selectForumEvaluationsWithPosts(int forumId, Evaluation.EvaluationsSort evalSort, boolean all)
	{
		if (forumId <= 0)
		{
			return new ArrayList<Evaluation>();
		}
		
		return selectForumEvaluations(forumId, all, evalSort);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int selectForumTopicEvaluationsCount(int forumId)
	{
		StringBuilder sql = null;
		Object[] fields = null;
		int i = 0;
		
		sql = new StringBuilder();
		sql.append("SELECT COUNT(1) AS eval_count FROM jforum_evaluations WHERE grade_id IN (SELECT grade_id FROM jforum_grade WHERE forum_id = ? AND topic_id > 0)");
		
		fields = new Object[1];
		fields[i++] = forumId;
		
		final List<Integer> evaluationsCountList = new ArrayList<Integer>();
		
		if (sql != null && fields != null)
		{
			this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
			{
				public Object readSqlResultRecord(ResultSet result)
				{
					try
					{
						evaluationsCountList.add(Integer.valueOf(result.getInt("eval_count")));
						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("selectForumTopicGradesByCategoryId: " + e, e);
						}
						return null;
					}
				}
			});
		}
		
		int evaluationsCount = 0;
		
		if (evaluationsCountList.size() == 1)
		{
			evaluationsCount = evaluationsCountList.get(0).intValue();
		}
		
		return evaluationsCount;	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Grade> selectForumTopicGradesByCategoryId(int categoryId)
	{
		StringBuilder sql = null;
		Object[] fields = null;
		int i = 0;
		
		sql = new StringBuilder();
		//sql.append("SELECT g.grade_id, g.context, g.grade_type, g.forum_id, g.topic_id, g.points, g.add_to_gradebook, g.categories_id, g.min_posts, g.min_posts_required ");
		//sql.append("FROM jforum_grade g, jforum_forums f WHERE g.forum_id = f.forum_id AND  f.categories_id = ? AND (f.forum_grade_type = ? OR f.forum_grade_type = ?)");
		sql.append("SELECT g.grade_id, g.context, g.grade_type, g.forum_id, g.topic_id, g.points, g.add_to_gradebook, g.categories_id, g.min_posts, g.min_posts_required, s.open_date "); 
		sql.append("FROM jforum_forums f, jforum_grade g LEFT OUTER JOIN jforum_schedule_grades_gradebook s ON g.grade_id = s.grade_id ");
		sql.append("WHERE g.forum_id = f.forum_id AND  f.categories_id = ? AND (f.forum_grade_type = ? OR f.forum_grade_type = ?)");
		
		fields = new Object[3];
		fields[i++] = categoryId;
		fields[i++] = Grade.GradeType.FORUM.getType();
		fields[i++] = Grade.GradeType.TOPIC.getType();
		
		final List<Grade> grades = new ArrayList<Grade>();
		
		if (sql != null && fields != null)
		{
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
						grade.setMinimumPostsRequired(result.getInt("min_posts_required") == 1);
						grade.setMinimumPosts(result.getInt("min_posts"));
						((GradeImpl)grade).setItemOpenDate(result.getTimestamp("open_date"));
						grades.add(grade);

						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("selectForumTopicGradesByCategoryId: " + e, e);
						}
						return null;
					}
				}
			});
		}
		
		return grades;
	}
	
	public List<Grade> selectForumTopicGradesByForumId(int forumId)
	{
		if (forumId <=0 ) throw new IllegalArgumentException("Forum id is missing");
		
		StringBuilder sql = null;
		Object[] fields = null;
		int i = 0;
		
		sql = new StringBuilder();
		//sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts  ");
		//sql.append(" FROM jforum_grade WHERE forum_id = ? AND grade_type = ?");
		
		sql.append("SELECT g.grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts, s.open_date ");
		sql.append("FROM jforum_grade g LEFT OUTER JOIN jforum_schedule_grades_gradebook s ON g.grade_id = s.grade_id WHERE forum_id = ? AND grade_type = ?");
		
		fields = new Object[2];
		fields[i++] = forumId;
		fields[i++] = Grade.GradeType.TOPIC.getType();
		
		return getGrades(sql, fields);
	}
	
	public Grade selectGradeById(int gradeId)
	{
		if (gradeId <= 0) throw new IllegalArgumentException();
		
		StringBuilder sql = null;
		Object[] fields = null;
		int i = 0;
		
		sql = new StringBuilder();
		//sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts  ");
		//sql.append(" FROM jforum_grade WHERE grade_id = ?");
		sql.append("SELECT g.grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts, s.open_date "); 
		sql.append("FROM jforum_grade g LEFT OUTER JOIN jforum_schedule_grades_gradebook s ON g.grade_id = s.grade_id WHERE g.grade_id = ?");
		
		fields = new Object[1];
		fields[i++] = gradeId;
		
		return getGrade(sql, fields);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Evaluation> selectTopicEvaluationsWithPosts(int topicId, Evaluation.EvaluationsSort evalSort, boolean all)
	{
		if (topicId <= 0)
		{
			return new ArrayList<Evaluation>();
		}
		
		return selectTopicEvaluations(0, topicId, all, evalSort);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Grade> selectTopicGradesByForumId(int forumId)
	{
		StringBuilder sql = null;
		Object[] fields = null;
		int i = 0;
		
		sql = new StringBuilder();
		//sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts, min_posts_required ");
		//sql.append("FROM jforum_grade WHERE forum_id = ? and grade_type = ?");
		sql.append("SELECT g.grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts, min_posts_required, s.open_date ");
		sql.append("FROM jforum_grade g LEFT OUTER JOIN jforum_schedule_grades_gradebook s ON g.grade_id = s.grade_id ");
		sql.append("WHERE forum_id = ? and grade_type = ?");
		
		fields = new Object[2];
		fields[i++] = forumId;
		fields[i++] = Grade.GradeType.TOPIC.getType();
		
		final List<Grade> grades = new ArrayList<Grade>();
		
		if (sql != null && fields != null)
		{
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
						grade.setMinimumPostsRequired(result.getInt("min_posts_required") == 1);
						grade.setMinimumPosts(result.getInt("min_posts"));
						((GradeImpl)grade).setItemOpenDate(result.getTimestamp("open_date"));
						grades.add(grade);

						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("selectTopicGradesByForumId: " + e, e);
						}
						return null;
					}
				}
			});
		}
		
		return grades;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation selectUserCategoryEvaluation(int categoryId, int userId)
	{
		Grade grade = selectByCategory(categoryId);
		
		if (grade == null)
		{
			return null;
		}
		
		return selectUserEvaluation(grade.getId(), userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation selectUserCategoryEvaluation(int categoryId, int userId, boolean checkLatePosts)
	{
		Grade grade = selectByCategory(categoryId);
		
		if (grade == null)
		{
			return null;
		}
		
		Evaluation evaluation = selectUserEvaluation(grade.getId(), userId);
		
		if (!checkLatePosts)
		{
			return evaluation;
		}
		
		Category category = categoryDao.selectById(categoryId);
		if (category == null)
		{
			return evaluation; 
		}
		
		if (evaluation == null)
		{
			// if user have no posts return evaluation object with user late posts  information.
			evaluation = new EvaluationImpl();

			((EvaluationImpl)evaluation).setId(-1);
		}
		
		// user posts count and user last post time
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p.user_id, COUNT(p.post_id) AS user_posts_count, MAX(post_time) AS last_post_time, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username "); 
		sql.append("FROM jforum_posts p, jforum_users u, jforum_forums f ");
		sql.append("WHERE p.forum_id = f.forum_id AND f.categories_id = ? AND p.user_id = u.user_id AND u.user_id = ? ");
		sql.append("GROUP BY p.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username");
		
		int i = 0;
		Object[] fields = new Object[2];
		fields[i++] = categoryId;
		fields[i++] = userId;		
				
		// read user posts count and user last post time
		final List<Integer> userPostsCount = new ArrayList<Integer>();
		final List<Date> userLastPostTime = new ArrayList<Date>();
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					int userPostCount = result.getInt("user_posts_count");
					if (userPostCount > 0)
					{
						userPostsCount.add(userPostCount);
						userLastPostTime.add(result.getTimestamp("last_post_time"));
					}
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectUserCategoryEvaluation(int categoryId, int userId, boolean checkLatePosts) - get user posts count and user last post time: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (userPostsCount.size() == 0)
		{
			// user have no posts
			return evaluation;
		}
		else
		{
			int totalPosts = userPostsCount.get(0);
			Date lastPostTime = userLastPostTime.get(0);
			
			((EvaluationImpl)evaluation).setTotalPosts(totalPosts);
			((EvaluationImpl)evaluation).setLastPostTime(lastPostTime);
		}
		
		Date dueDate = null;
		if ((category != null) && (category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
		{
			dueDate = category.getAccessDates().getDueDate();
		}
		
		final List<Integer> isLateList = new ArrayList<Integer>();
		if (dueDate != null)
		{
			sql = new StringBuilder();
			sql.append("SELECT p.user_id, COUNT(p.post_id) AS user_late_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username "); 
			sql.append("FROM jforum_posts p, jforum_users u, jforum_forums f ");
			sql.append("WHERE p.forum_id = f.forum_id AND f.categories_id = ? AND p.user_id = u.user_id AND u.user_id = ? ");
			sql.append("AND p.post_time > ? ");
			sql.append("GROUP BY p.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username");
			
			i = 0;
			fields = new Object[3];
			fields[i++] = categoryId;
			fields[i++] = userId;
			fields[i++] = dueDate;		
			
			// user late posts			
			this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
			{
				public Object readSqlResultRecord(ResultSet result)
				{
					try
					{
						int latePostCount = result.getInt("user_late_posts_count");
						if (latePostCount > 0)
						{
							isLateList.add(new Integer(latePostCount));
						}
						
						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("selectUserCategoryEvaluation(int categoryId, int userId, boolean checkLatePosts) - get user late posts: " + e, e);
						}
						return null;
					}
				}
			});
		}
		
		if (isLateList.size() > 0)
		{
			((EvaluationImpl)evaluation).setLate(Boolean.TRUE);
		}
		else
		{
			if (evaluation.getId() == -1 && evaluation.getTotalPosts() == 0)
			{
				evaluation = null;
			}			
		}
		
		return evaluation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation selectUserForumEvaluation(int forumId, int userId)
	{
		Grade grade = selectByForum(forumId);
		
		if (grade == null)
		{
			return null;
		}
		
		return selectUserEvaluation(grade.getId(), userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation selectUserForumEvaluation(int forumId, int userId, boolean checkLatePosts)
	{
		Grade grade = selectByForum(forumId);
		
		if (grade == null)
		{
			return null;
		}
		
		Evaluation evaluation = selectUserEvaluation(grade.getId(), userId);
		
		if (!checkLatePosts)
		{
			return evaluation;
		}
		
		Forum forum = forumDao.selectById(forumId);
		if (forum == null)
		{
			return evaluation; 
		}
		
		Category category = categoryDao.selectById(forum.getCategoryId());
		if (category == null)
		{
			return evaluation; 
		}
		
		if (evaluation == null)
		{
			// if user have no posts return evaluation object with user late posts  information. For special access users the due date is special access due date.
			evaluation = new EvaluationImpl();

			((EvaluationImpl)evaluation).setId(-1);
		}
		
		// user posts count and user last post time
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_posts_count, MAX(post_time) AS last_post_time, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");		
		sql.append("FROM (SELECT user_id from jforum_site_users WHERE sakai_site_id = ? AND user_id = ?) s ");
		sql.append("LEFT OUTER JOIN jforum_users u ON s.user_id = u.user_id ");
		sql.append("LEFT OUTER JOIN jforum_posts p ON s.user_id = p.user_id ");
		sql.append("AND p.forum_id = ? ");
		sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
		
		int i = 0;
		Object[] fields = new Object[3];
		fields[i++] = category.getContext();
		fields[i++] = userId;
		fields[i++] = grade.getForumId();
				
		// read user posts count and user last post time
		final List<Integer> userPostsCount = new ArrayList<Integer>();
		final List<Date> userLastPostTime = new ArrayList<Date>();
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					int userPostCount = result.getInt("user_posts_count");
					if (userPostCount > 0)
					{
						userPostsCount.add(userPostCount);
						userLastPostTime.add(result.getTimestamp("last_post_time"));
					}
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectUserForumEvaluation(int forumId, int userId, boolean checkLatePosts) - get user posts count and user last post time: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (userPostsCount.size() == 0)
		{
			// user have no posts
			return evaluation;
		}
		else
		{
			int totalPosts = userPostsCount.get(0);
			Date lastPostTime = userLastPostTime.get(0);
			
			((EvaluationImpl)evaluation).setTotalPosts(totalPosts);
			((EvaluationImpl)evaluation).setLastPostTime(lastPostTime);
		}
		
		// user late posts information. For special access users the due date is special access due date.
		Date dueDate = null;
		List<SpecialAccess> specialAccessList = null;
		if ((forum != null) && (forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
		{
			dueDate = forum.getAccessDates().getDueDate();
			specialAccessList = forum.getSpecialAccess();
		}
		else if ((category != null) && (category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
		{
			dueDate = category.getAccessDates().getDueDate();
		}
		
		// special access users late posts
		boolean specialAccessUser = false;
		if (specialAccessList != null)
		{
			for (SpecialAccess specialAccess : specialAccessList)
			{
				List<Integer> userIds = specialAccess.getUserIds();
				
				if (userIds == null || userIds.isEmpty())
				{
					continue;
				}
				
				for (Integer saUserId : userIds)
				{
					if (saUserId.intValue() == userId)
					{
						specialAccessUser = true;
						break;
					}
				}
				
				if (specialAccessUser)
				{
					dueDate = specialAccess.getAccessDates().getDueDate();
					break;
				}
			}
		}
		
		final List<Integer> isLateList = new ArrayList<Integer>();
		if (dueDate != null)
		{
			sql = new StringBuilder();
			// get users late posts count in a forum.
			sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_late_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");		
			sql.append("FROM (SELECT user_id from jforum_site_users WHERE sakai_site_id = ? AND user_id = ?) s ");
			sql.append("LEFT OUTER JOIN jforum_users u ON s.user_id = u.user_id ");
			sql.append("LEFT OUTER JOIN jforum_posts p ON s.user_id = p.user_id ");
			sql.append("AND p.forum_id = ? ");
			sql.append("AND p.post_time > ? ");
			sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
			
			i = 0;
			fields = new Object[4];
			fields[i++] = category.getContext();
			fields[i++] = userId;
			fields[i++] = grade.getForumId();
			fields[i++] = dueDate;		
			
			// user late posts
			this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
			{
				public Object readSqlResultRecord(ResultSet result)
				{
					try
					{
						int latePostCount = result.getInt("user_late_posts_count");
						if (latePostCount > 0)
						{
							isLateList.add(new Integer(latePostCount));
						}
						
						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("selectUserForumEvaluation(int forumId, int userId, boolean checkLatePosts) - get user late posts: " + e, e);
						}
						return null;
					}
				}
			});
		}
		
		if (isLateList.size() > 0)
		{
			((EvaluationImpl)evaluation).setLate(Boolean.TRUE);
		}
		else
		{
			if (evaluation.getId() == -1 && evaluation.getTotalPosts() == 0)
			{
				evaluation = null;
			}			
		}
		
		return evaluation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation selectUserTopicEvaluation(int forumId, int topicId, int userId)
	{
		Grade grade = selectByForumTopic(forumId, topicId);
		
		if (grade == null)
		{
			return null;
		}
		
		return selectUserEvaluation(grade.getId(), userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation selectUserTopicEvaluation(int forumId, int topicId, int userId, boolean checkLatePosts)
	{
		Grade grade = selectByForumTopic(forumId, topicId);
		
		if (grade == null)
		{
			return null;
		}
		Evaluation evaluation = selectUserEvaluation(grade.getId(), userId);
		
		if (!checkLatePosts)
		{
			return evaluation;
		}
		
		Topic topic = topicDao.selectById(topicId);
		if (topic == null)
		{
			return evaluation; 
		}
		
		Forum forum = forumDao.selectById(topic.getForumId());
		if (forum == null)
		{
			return evaluation; 
		}
		
		Category category = categoryDao.selectById(forum.getCategoryId());
		if (category == null)
		{
			return evaluation; 
		}
		
		if (evaluation == null)
		{
			// if user have no posts return evaluation object with user late posts  information. For special access users the due date is special access due date.
			evaluation = new EvaluationImpl();

			((EvaluationImpl)evaluation).setId(-1);
		}
		
		// user posts count and user last post time
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_posts_count, MAX(post_time) AS last_post_time, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");		
		sql.append("FROM (SELECT user_id from jforum_site_users WHERE sakai_site_id = ? AND user_id = ?) s ");
		sql.append("LEFT OUTER JOIN jforum_users u ON s.user_id = u.user_id ");
		sql.append("LEFT OUTER JOIN jforum_posts p ON s.user_id = p.user_id ");
		sql.append("AND p.forum_id = ? AND p.topic_id = ? ");
		sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
		
		int i = 0;
		Object[] fields = new Object[4];
		fields[i++] = category.getContext();
		fields[i++] = userId;
		fields[i++] = grade.getForumId();
		fields[i++] = topicId;		
		
		// user posts count and user last post time
		final List<Integer> userPostsCount = new ArrayList<Integer>();
		final List<Date> userLastPostTime = new ArrayList<Date>();
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					int userPostCount = result.getInt("user_posts_count");
					if (userPostCount > 0)
					{
						userPostsCount.add(userPostCount);
						userLastPostTime.add(result.getTimestamp("last_post_time"));
					}
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectUserTopicEvaluation(int forumId, int topicId, int userId, boolean checkLatePosts) - get user posts count and user last post time: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (userPostsCount.size() == 0)
		{
			// user have no posts
			return evaluation;
		}
		else
		{
			int totalPosts = userPostsCount.get(0);
			Date lastPostTime = userLastPostTime.get(0);
			
			((EvaluationImpl)evaluation).setTotalPosts(totalPosts);
			((EvaluationImpl)evaluation).setLastPostTime(lastPostTime);
		}
		
		// user late posts information. For special access users the due date is special access due date.
		Date dueDate = null;
		List<SpecialAccess> specialAccessList = null;
		if ((topic.getAccessDates() != null) && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null) || (topic.getAccessDates().getAllowUntilDate() != null)))
		{
			dueDate = topic.getAccessDates().getDueDate();
			specialAccessList = topic.getSpecialAccess();
		}
		else if ((forum != null) && (forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
		{
			dueDate = forum.getAccessDates().getDueDate();
			specialAccessList = forum.getSpecialAccess();
		}
		else if ((category != null) && (category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
		{
			dueDate = category.getAccessDates().getDueDate();
		}
		
		// special access users late posts
		boolean specialAccessUser = false;
		if (specialAccessList != null)
		{
			for (SpecialAccess specialAccess : specialAccessList)
			{
				List<Integer> userIds = specialAccess.getUserIds();
				
				if (userIds == null || userIds.isEmpty())
				{
					continue;
				}
				
				for (Integer saUserId : userIds)
				{
					if (saUserId.intValue() == userId)
					{
						specialAccessUser = true;
						break;
					}
				}
				
				if (specialAccessUser)
				{
					dueDate = specialAccess.getAccessDates().getDueDate();
					break;
				}
			}
		}
		
		final List<Integer> isLateList = new ArrayList<Integer>();
		if (dueDate != null)
		{
			sql = new StringBuilder();
			// get users late posts count in a topic.
			sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_late_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");		
			sql.append("FROM (SELECT user_id from jforum_site_users WHERE sakai_site_id = ? AND user_id = ?) s ");
			sql.append("LEFT OUTER JOIN jforum_users u ON s.user_id = u.user_id ");
			sql.append("LEFT OUTER JOIN jforum_posts p ON s.user_id = p.user_id ");
			sql.append("AND p.forum_id = ? AND p.topic_id = ? ");
			sql.append("AND p.post_time > ? ");
			sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
			
			i = 0;
			fields = new Object[5];
			fields[i++] = category.getContext();
			fields[i++] = userId;
			fields[i++] = grade.getForumId();
			fields[i++] = topicId;
			fields[i++] = dueDate;		
			
			// user late posts
			this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
			{
				public Object readSqlResultRecord(ResultSet result)
				{
					try
					{
						int latePostCount = result.getInt("user_late_posts_count");
						if (latePostCount > 0)
						{
							isLateList.add(new Integer(latePostCount));
						}
						
						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("selectUserTopicEvaluation(int forumId, int topicId, int userId, boolean checkLatePosts) - get user late posts: " + e, e);
						}
						return null;
					}
				}
			});
		}
		
		if (isLateList.size() > 0)
		{
			((EvaluationImpl)evaluation).setLate(Boolean.TRUE);
		}
		else
		{
			if (evaluation.getId() == -1 && evaluation.getTotalPosts() == 0)
			{
				evaluation = null;
			}			
		}
		
		return evaluation;
	}
	
	/**
	 * @param categoryDao the categoryDao to set
	 */
	public void setCategoryDao(CategoryDao categoryDao)
	{
		this.categoryDao = categoryDao;
	}
	
	/**
	 * @param forumDao the forumDao to set
	 */
	public void setForumDao(ForumDao forumDao)
	{
		this.forumDao = forumDao;
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
	 * @param userDao the userDao to set
	 */
	public void setUserDao(UserDao userDao)
	{
		this.userDao = userDao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateAddToGradeBookStatus(int gradeId, boolean addToGradeBook)
	{
		if (gradeId <= 0)
		{
			return;
		}
		
		String sql = "UPDATE jforum_grade SET add_to_gradebook = ? WHERE grade_id = ?";
		
		Object[] fields = new Object[2];
		int i = 0;
		fields[i++] =   addToGradeBook ? 1 : 0;
		fields[i++] =  gradeId;
		
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
			
			throw new RuntimeException("updateAddToGradeBookStatus: dbWrite failed");
		}
	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateCategoryGrade(Grade grade)
	{
		if (grade == null)
		{
			throw new IllegalArgumentException("Grade is null.");
		}
		
		if (grade.getId() == 0 || grade.getForumId() > 0 || grade.getTopicId() > 0 || grade.getCategoryId() == 0)
		{
			new IllegalArgumentException("updateCategoryGrade: category grade information is not correct");
		}
		
		String sql = "UPDATE jforum_grade SET points = ?, add_to_gradebook = ?, min_posts_required = ?, min_posts = ? WHERE categories_id = ? AND forum_id = 0 AND topic_id = 0";
		
		Object[] fields = new Object[5];
		int i = 0;
		fields[i++] =  grade.getPoints();
						
		fields[i++] =  (grade.isAddToGradeBook() ? 1 : 0);
		
		if (grade.isMinimumPostsRequired())
		{
			fields[i++] =  1;
			fields[i++] =  grade.getMinimumPosts();			
		}
		else
		{
			fields[i++] =  0;
			fields[i++] =  0;
		}
		
		fields[i++] =  grade.getCategoryId();

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
			
			throw new RuntimeException("updateCategoryGrade: dbWrite failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateForumGrade(Grade grade)
	{
		if (grade == null)
		{
			throw new IllegalArgumentException("Grade is null.");
		}
		
		if (grade.getId() == 0 || grade.getForumId() == 0 || grade.getTopicId() > 0 || grade.getCategoryId() > 0)
		{
			new IllegalArgumentException("updateForumGrade: forum grade information is not correct");
		}
		
		String sql = "UPDATE jforum_grade SET points = ?,  add_to_gradebook = ?, min_posts_required = ?, min_posts = ? WHERE forum_id = ? AND topic_id = 0 AND categories_id = 0";
		
		Object[] fields = new Object[5];
		int i = 0;
		fields[i++] =  grade.getPoints();
				
		fields[i++] =  (grade.isAddToGradeBook() ? 1 : 0);
		
		if (grade.isMinimumPostsRequired())
		{
			fields[i++] =  1;
			fields[i++] =  grade.getMinimumPosts();			
		}
		else
		{
			fields[i++] =  0;
			fields[i++] =  0;
		}
		
		fields[i++] =  grade.getForumId();

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
			
			throw new RuntimeException("updateForumGrade: dbWrite failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateTopicGrade(Grade grade)
	{
		if (grade == null)
		{
			throw new IllegalArgumentException("Grade is null.");
		}
		
		if (grade.getId() == 0 || grade.getForumId() == 0 || grade.getTopicId() == 0 ||  grade.getCategoryId() > 0)
		{
			new IllegalArgumentException("updateTopicGrade: topic grade information is not correct");
		}
		
		String sql = "UPDATE jforum_grade SET points = ?,  add_to_gradebook = ?, min_posts_required = ?, min_posts = ? WHERE forum_id = ? AND topic_id = ? AND categories_id = 0";
		
		Object[] fields = new Object[6];
		int i = 0;
		fields[i++] =  grade.getPoints();
				
		fields[i++] =  (grade.isAddToGradeBook() ? 1 : 0);
		
		if (grade.isMinimumPostsRequired())
		{
			fields[i++] =  1;
			fields[i++] =  grade.getMinimumPosts();			
		}
		else
		{
			fields[i++] =  0;
			fields[i++] =  0;
		}
		
		fields[i++] =  grade.getForumId();
		fields[i++] =  grade.getTopicId();

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
			
			throw new RuntimeException("updateForumGrade: dbWrite failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateUserReviewedDate(int evaluationId)
	{

		if (evaluationId <= 0)
		{
			return;
		}
		
		String sql = "UPDATE jforum_evaluations SET reviewed_date = ? WHERE evaluation_id = ?";
		
		Object[] fields = new Object[2];
		int i = 0;
		fields[i++] = new Timestamp(System.currentTimeMillis());
		fields[i++] = evaluationId;

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
			
			throw new RuntimeException("updateUserReviewedDate: dbWrite failed");
		}
	
	}
	
	/**
	 * Adds the new evaluations, updates the existing evaluations and deletes the evaluations with no score and comments
	 * 
	 * @param grade		Grade
	 * 
	 * @param evaluations	Evaluations
	 */
	protected void addUpdateEvaluationsTx(final Grade grade, final List<Evaluation> evaluations)
	{
		if (grade == null || grade.getContext() == null || grade.getContext().trim().length() == 0 || evaluations.isEmpty())
		{
			return;
		}

		final String evaluatedBySakaiUserId;
		evaluatedBySakaiUserId = evaluations.get(0).getEvaluatedBySakaiUserId();

		if (evaluatedBySakaiUserId == null)
		{
			return;
		}

		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					User evaluatedByUser = userDao.selectBySakaiUserId(evaluatedBySakaiUserId);

					if (evaluatedByUser == null)
					{
						return;
					}

					/* each user must have only one evaluation */

					// get all site users for their user id's
					List<User> siteUsers = userDao.selectSiteUsers(grade.getContext());

					Map<String, User> siteUsersMap = new HashMap<String, User>();

					for (User user : siteUsers)
					{
						siteUsersMap.put(user.getSakaiUserId(), user);
					}

					// get existing evaluations
					List<Evaluation> exisEvals = selectEvaluations(grade.getId());

					Map<String, Evaluation> exisEvalMap = new HashMap<String, Evaluation>();

					for (Evaluation exisEval : exisEvals)
					{
						exisEvalMap.put(exisEval.getSakaiUserId(), exisEval);
					}

					int modEvalId;
					String modEvalSakaiUserId = null;
					// int modEvalJforumUserId;
					Float score = null;
					String comments = null;
					// boolean releaseScore = false;
					
					Date evaluatedDate = new Date();

					for (Evaluation modEval : evaluations)
					{
						modEvalId = modEval.getId();
						// modEvalJforumUserId = modEval.getUserId();
						modEvalSakaiUserId = modEval.getSakaiUserId();
						score = modEval.getScore();
						comments = modEval.getComments();
						// releaseScore = modEval.isReleased();
						
						if (grade.getId() != modEval.getGradeId())
						{
							continue;
						}

						if (modEvalSakaiUserId == null || modEvalSakaiUserId.trim().length() == 0)
						{
							continue;
						}

						// if no score and comments delete the existing evaluation or don't create the evaluation if not existing
						if (score == null && (comments == null || comments.trim().length() == 0))
						{
							if (modEvalId > 0)
							{
								deleteEvaluation(modEvalId);
							}

							continue;
						}

						User evaluationUser = siteUsersMap.get(modEvalSakaiUserId);

						if (evaluationUser == null)
						{
							continue;
						}

						((EvaluationImpl) modEval).setUserId(evaluationUser.getId());
						((EvaluationImpl) modEval).setEvaluatedBy(evaluatedByUser.getId());

						Evaluation exisEval = exisEvalMap.get(modEvalSakaiUserId);

						if (exisEval == null && modEvalId == -1)
						{
							int id = insertEvaluation(modEval);
							((EvaluationImpl) modEval).setId(id);
						}
						else if (exisEval != null)
						{
							if (exisEval.getId() == -1 && modEvalId == -1)
							{
								((EvaluationImpl)modEval).setEvaluatedDate(evaluatedDate);
								int id = insertEvaluation(modEval);
								((EvaluationImpl) modEval).setId(id);
							}
							// check for changes for existing evaluations, if changed then update
							else if (exisEval.getId() == modEvalId && checkEvaluationChanges(exisEval, modEval))
							{
								// update existing evaluation
								((EvaluationImpl)modEval).setEvaluatedDate(evaluatedDate);
								updateEvaluation(modEval);
							}
						}						
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}

					throw new RuntimeException("Error while adding or updating evaluations.", e);
				}
			}
		}, "addUpdateEvaluations: " + grade.getId());
	}
	
	/**
	 * Inserts new evaluation if not exists or updates the existing evaluation
	 * 
	 * @param evaluation	Evaluation
	 */
	protected void addUpdateEvaluationTx(final Evaluation evaluation)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					Grade grade = selectGradeById(evaluation.getGradeId());
					
					if (grade == null)
					{
						return;
					}
					
					String evaluatedBySakaiUserId = evaluation.getEvaluatedBySakaiUserId();
					String sakaiUserId = evaluation.getSakaiUserId();
					
					User evaluationUser = userDao.selectBySakaiUserId(sakaiUserId);
									
					User evaluatedByUser = userDao.selectBySakaiUserId(evaluatedBySakaiUserId);

					if (evaluationUser == null || evaluatedByUser == null)
					{
						return;
					}
					
					User evaluatedSiteUser = userDao.selectSiteUser(grade.getContext(), evaluationUser.getId());
					
					User evaluatedBySiteUser = userDao.selectSiteUser(grade.getContext(), evaluationUser.getId());
					
					if (evaluatedSiteUser == null || evaluatedBySiteUser == null)
					{
						return;
					}
					
					Evaluation exisUserEvaluation = selectUserEvaluation(grade.getId(), evaluatedSiteUser.getId());
					
					if (exisUserEvaluation == null)
					{
						((EvaluationImpl) evaluation).setUserId(evaluationUser.getId());
						((EvaluationImpl) evaluation).setEvaluatedBy(evaluatedByUser.getId());
						((EvaluationImpl) evaluation).setEvaluatedDate(new Date());
						
						// add evaluation
						int id = insertEvaluation(evaluation);
						((EvaluationImpl) evaluation).setId(id);
					}
					else 
					{
						if (evaluation.getId() == exisUserEvaluation.getId())
						{
							Float score = evaluation.getScore();
							String comments = evaluation.getComments();
							
							// if no score and comments delete the existing evaluation or don't create the evaluation if not existing
							if (score == null && (comments == null || comments.trim().length() == 0))
							{
								deleteEvaluation(exisUserEvaluation.getId());
							}
							else
							{
								// update evaluation
								((EvaluationImpl) evaluation).setEvaluatedDate(new Date());
								
								updateEvaluation(evaluation);
							}
						}
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}

					throw new RuntimeException("Error while adding or updating user evaluation.", e);
				}
			}
		}, "addUpdateuserEvaluation: " + evaluation.getSakaiUserId());
	}
	

	/**
	 * Sort string of the sql
	 * 
	 * @param evalSort	Evaluation sort
	 * 
	 * @return	The sort string
	 */
	protected String applySort(Evaluation.EvaluationsSort evalSort)
	{
		String sql = null;
		if (evalSort == null)
		{
			sql = " ORDER BY user_lname ASC";
			return sql;
		}
			
		switch (evalSort)
		{
			case last_name_a:
			{
				sql = " ORDER BY user_lname ASC";
				break;
			}
			case last_name_d:
			{
				sql = " ORDER BY user_lname DESC";
				break;
			}
			case total_posts_a:
			{
				sql = " ORDER BY user_posts_count ASC";
				break;
			}
			case total_posts_d:
			{
				sql = " ORDER BY user_posts_count DESC";
				break;
			}
			default:
			{
				sql = " ORDER BY user_lname ASC";
				break;
			}

		}
		return sql;
	}
	
	/**
	 * compare the scores, comments, released of evaluation objects
	 * 
	 * @param exisEval	The existing evaluation
	 * 
	 * @param modEval	The modified evaluation
	 * 
	 * @return		True - if scores or comments changed
	 * 				False - if scores and comments are not changed
	 */
	protected boolean checkEvaluationChanges(Evaluation exisEval, Evaluation modEval)
	{
		if ((exisEval == null) && (modEval == null))
		{
			return false;
		}
		
		if (((exisEval == null) && (modEval != null)) || ((exisEval != null) && (modEval == null)))
		{
			return true;
		}
		else
		{
			// check for score changes
			Float exisScore = exisEval.getScore();
			Float modScore = modEval.getScore();
			
			if ((exisScore == null) && (modScore == null))
			{
				// continue to check comments
			}
			else if (((exisScore == null) && (modScore != null)) || ((exisScore != null) && (modScore == null)))
			{
				return true;
			}
			else if (!exisScore.equals(modScore))
			{
				return true;
			}
			
			// check for comments changes
			String exisComments = exisEval.getComments();
			String modComments = modEval.getComments();
			
			if ((exisComments == null) && (modComments == null))
			{
			}
			else if (((exisComments == null) && (modComments != null)) || ((exisComments != null) && (modComments == null)))
			{
				return true;
			}
			else if (!exisComments.equalsIgnoreCase(modComments))
			{
				return true;
			}
			
			// check for released
			boolean exisReleased = exisEval.isReleased();
			boolean modReleased = modEval.isReleased();
			
			if (exisReleased != modReleased)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Fill the evaluation with user information
	 * 
	 * @param rs	Result set
	 * 
	 * @return	The evaluation with filled user information
	 */
	protected Evaluation fillEvaluationUserInfo(ResultSet rs) throws SQLException
	{
		Evaluation evaluation = new EvaluationImpl();

		((EvaluationImpl)evaluation).setId(-1);

		((EvaluationImpl)evaluation).setUserId(rs.getInt("user_id"));
		((EvaluationImpl)evaluation).setTotalPosts(rs.getInt("user_posts_count"));
		((EvaluationImpl)evaluation).setUserFirstName(rs.getString("user_fname"));
		((EvaluationImpl)evaluation).setUserLastName(rs.getString("user_lname"));
		((EvaluationImpl)evaluation).setSakaiUserId(rs.getString("sakai_user_id"));
		((EvaluationImpl)evaluation).setUsername(rs.getString("username"));		
		((EvaluationImpl)evaluation).setLastPostTime(rs.getTimestamp("last_post_time"));
		
		return evaluation;
	}
	
	/**
	 * Utility method to read the post text fromt the result set.
	 * This method may be useful when using some "non-standard" way
	 * to store text, like oracle does when using (c|b)lob
	 * 
	 * @param rs The resultset to fetch data from
	 * 
	 * @return The comments string
	 * 
	 * @throws SQLException
	 */
	abstract protected String getEvaluationCommentsFromResultSet(ResultSet rs) throws SQLException;
	
	/**
	 * Gets the grade
	 * 
	 * @param sql		SQL Query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return		The grade if existing else null
	 */
	protected Grade getGrade(StringBuilder sql, Object[] fields)
	{
		Grade grade = null;
		
		final List<Grade> grades = new ArrayList<Grade>();
		
		if (sql != null && fields != null)
		{
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
						grade.setMinimumPostsRequired(result.getInt("min_posts_required") == 1);
						grade.setMinimumPosts(result.getInt("min_posts"));
						((GradeImpl)grade).setItemOpenDate(result.getTimestamp("open_date"));
						grades.add(grade);

						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("getGrade: " + e, e);
						}
						return null;
					}
				}
			});
		}
			
		if (grades.size() == 1)
		{
			grade = grades.get(0);
		}
		return grade;
	}
	
	protected List<Grade> getGrades(StringBuilder sql, Object[] fields)
	{
		final List<Grade> grades = new ArrayList<Grade>();
		
		if (sql != null && fields != null)
		{
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
						grade.setMinimumPostsRequired(result.getInt("min_posts_required") == 1);
						grade.setMinimumPosts(result.getInt("min_posts"));
						((GradeImpl)grade).setItemOpenDate(result.getTimestamp("open_date"));
						grades.add(grade);

						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("getGrade: " + e, e);
						}
						return null;
					}
				}
			});
		}
			
		return grades;
	}
	
	abstract protected int insertEvaluation(Evaluation evaluation);
	
	/**
	 * Create new grade
	 * 
	 * @param grade	Grade 
	 * 
	 * @return	New grade id
	 */
	protected abstract int insertGrade(Grade grade);
	
	/**
	 * Gets the gradable category evaluations with posts count, late information, and last post time
	 * 
	 * @param categoryId	Categogy id
	 * 
	 * @param all		true - all users who have posts or no posts
	 * 					false - users who have posts
	 * 
	 * @param evalSort	Evaluations sort
	 * 
	 * @return	 The gradable category evaluations with posts count, late information, and last post time
	 */
	protected List<Evaluation> selectCategoryEvaluations(int categoryId, boolean all, Evaluation.EvaluationsSort evalSort)
	{
		final List<Evaluation> evaluations = new ArrayList<Evaluation>();
		final Map<Integer, Evaluation> evalMap = new HashMap<Integer, Evaluation>();
		
		Grade grade = selectByCategory(categoryId);
		
		if (grade == null)
		{
			return evaluations;
		}
		
		Category category = null;
		category = categoryDao.selectById(categoryId);
		
		if (category == null)
		{
			return evaluations;
		}
		
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		int i = 0;
		if (all)
		{
			sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_posts_count, MAX(post_time) AS last_post_time, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");				
			sql.append("FROM  (SELECT user_id from jforum_site_users WHERE sakai_site_id = ?) s ");
			sql.append("LEFT OUTER JOIN jforum_users u ON (s.user_id = u.user_id) ");
			sql.append("LEFT OUTER JOIN jforum_posts p ON (s.user_id = p.user_id) ");
			sql.append("AND p.forum_id IN (SELECT forum_id FROM jforum_forums f where f.categories_id = ?) ");
			sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
			sql.append(applySort(evalSort));
			
			fields = new Object[2];
			fields[i++] = category.getContext();
			fields[i++] = categoryId;
		}
		else
		{
			sql.append("SELECT p.user_id, COUNT(p.post_id) AS user_posts_count, MAX(post_time) AS last_post_time, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username "); 
			sql.append("FROM jforum_posts p, jforum_users u, jforum_forums f ");
			sql.append("WHERE p.forum_id = f.forum_id AND f.categories_id = ? AND p.user_id = u.user_id ");
			sql.append("GROUP BY p.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username");
			sql.append(applySort(evalSort));
			
			fields = new Object[1];
			fields[i++] = categoryId;			
		}
				
		// user details
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Evaluation evaluation = fillEvaluationUserInfo(result);
					evaluations.add(evaluation);
					evalMap.put(new Integer(evaluation.getUserId()), evaluation);
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectCategoryEvaluations - get user details: " + e, e);
					}
					return null;
				}
			}
		});	
		
		Date dueDate = null;
		if ((category != null) && (category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
		{
			dueDate = category.getAccessDates().getDueDate();
		}
		
		// category user late posts
		if (dueDate != null)
		{
			sql = new StringBuilder();
			if (all)
			{	
				sql = new StringBuilder();
				// get all site users late posts count in a category.
				sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_late_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");				
				sql.append("FROM  (SELECT user_id from jforum_site_users WHERE sakai_site_id = ?) s ");
				sql.append("LEFT OUTER JOIN jforum_users u ON (s.user_id = u.user_id) ");
				sql.append("LEFT OUTER JOIN jforum_posts p ON (s.user_id = p.user_id) ");
				sql.append("AND p.post_time > ? ");
				sql.append("AND p.forum_id IN (SELECT forum_id FROM jforum_forums f where f.categories_id = ?) ");
				sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
				sql.append(applySort(evalSort));
				
				i = 0;
				fields = new Object[3];
				fields[i++] = category.getContext();
				fields[i++] = dueDate;
				fields[i++] = categoryId;
			}
			else
			{
				sql = new StringBuilder();
				// get late posts counts of users who posted in a category.Due dates are different for special access users
				sql.append("SELECT p.user_id, COUNT(p.post_id) AS user_late_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username "); 
				sql.append("FROM jforum_posts p, jforum_users u, jforum_forums f ");
				sql.append("WHERE p.forum_id = f.forum_id AND f.categories_id = ? AND p.user_id = u.user_id ");
				sql.append("AND p.post_time > ? ");
				sql.append("GROUP BY p.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username");
				sql.append(applySort(evalSort));
				
				i = 0;
				fields = new Object[2];
				fields[i++] = categoryId;
				fields[i++] = dueDate;
			}
			
			// late posts
			this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
			{
				public Object readSqlResultRecord(ResultSet result)
				{
					try
					{
						int userId = result.getInt("user_id");
						if (evalMap.containsKey(new Integer(userId)))
						{
							int latePostCount = result.getInt("user_late_posts_count");
							if (latePostCount > 0)
							{
								Evaluation evaluation = evalMap.get(new Integer(userId));
								((EvaluationImpl)evaluation).setLate(Boolean.TRUE);
							}
						}
						
						return null;
					}
					catch (SQLException e)
					{
						if (logger.isWarnEnabled())
						{
							logger.warn("selectCategoryEvaluations - get category evaluations - get late posts: " + e, e);
						}
						return null;
					}
				}
			});
			
		}
		
		// evaluation details
		sql = new StringBuilder();
		sql.append("SELECT evaluation_id, grade_id, user_id, sakai_user_id, score, comments, evaluated_by, evaluated_date, released, reviewed_date ");						
		sql.append(" FROM jforum_evaluations WHERE grade_id = ?");
		
		fields = new Object[1];
		i = 0;
		fields[i++] = grade.getId();
					
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					int userId = result.getInt("user_id");
					if (evalMap.containsKey(new Integer(userId)))
					{
						Evaluation evaluation = evalMap.get(new Integer(userId));
						
						((EvaluationImpl)evaluation).setId(result.getInt("evaluation_id"));
						((EvaluationImpl)evaluation).setGradeId(result.getInt("grade_id"));
						((EvaluationImpl)evaluation).setSakaiUserId(result.getString("sakai_user_id"));
						
						//evaluation.setScore(result.getFloat("score"));
						String str = result.getString("score");
						if (str == null)
						{
							evaluation.setScore(null);
						}
						else
						{
							try
							{
								evaluation.setScore(Float.valueOf(str));
							} 
							catch (NumberFormatException e)
							{
								evaluation.setScore(null);
							}
						}
						
						((EvaluationImpl)evaluation).setEvaluatedBy(result.getInt("evaluated_by"));
						
						if (result.getDate("evaluated_date") != null)
					    {
							Timestamp dueDate = result.getTimestamp("evaluated_date");
							((EvaluationImpl)evaluation).setEvaluatedDate(dueDate);
					    }
					    else
					    {
					    	((EvaluationImpl)evaluation).setEvaluatedDate(null);
					    }
						
						evaluation.setReleased(result.getInt("released") == 1);
						
						if (result.getDate("reviewed_date") != null)
					    {
							Timestamp reviewedDate = result.getTimestamp("reviewed_date");
					      	evaluation.setReviewedDate(reviewedDate);
					    }
					    else
					    {
					    	((EvaluationImpl)evaluation).setEvaluatedDate(null);
					    }
						
						evaluation.setComments(getEvaluationCommentsFromResultSet(result));
					}

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectCategoryEvaluations - get Evaluation details: " + e, e);
					}
					return null;
				}
			}
		});
		
		return evaluations;
	}
	
	/**
	 * Gets the grade evaluations
	 * 
	 * @param gradeId	Grade id
	 * 
	 * @return	The grade evaluations
	 */
	protected List<Evaluation> selectEvaluations(int gradeId)
	{
		if (gradeId <= 0)
		{
			throw new IllegalArgumentException("Grade is missing.");
		}
		
		StringBuilder sql;
		Object[] fields;
		int i;
		sql = null;
		fields = null;
		i = 0;	
			
		sql = new StringBuilder();
		sql.append("SELECT evaluation_id, grade_id, user_id, sakai_user_id, score, comments, evaluated_by, evaluated_date, released, reviewed_date ");						
		sql.append(" FROM jforum_evaluations WHERE grade_id = ?");
		
		fields = new Object[1];
		i = 0;
		fields[i++] = gradeId;
					
		final List<Evaluation> evaluations = new ArrayList<Evaluation>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Evaluation evaluation = new EvaluationImpl();
					((EvaluationImpl)evaluation).setId(result.getInt("evaluation_id"));
					((EvaluationImpl)evaluation).setGradeId(result.getInt("grade_id"));
					((EvaluationImpl)evaluation).setSakaiUserId(result.getString("sakai_user_id"));
					//evaluation.setScore(result.getFloat("score"));
					String str = result.getString("score");
					if (str == null)
					{
						evaluation.setScore(null);
					}
					else
					{
						try
						{
							evaluation.setScore(Float.valueOf(str));
						} 
						catch (NumberFormatException e)
						{
							evaluation.setScore(null);
						}
					}
					((EvaluationImpl)evaluation).setEvaluatedBy(result.getInt("evaluated_by"));
					
					if (result.getDate("evaluated_date") != null)
				    {
						Timestamp dueDate = result.getTimestamp("evaluated_date");
						((EvaluationImpl)evaluation).setEvaluatedDate(dueDate);
				    }
				    else
				    {
				    	((EvaluationImpl)evaluation).setEvaluatedDate(null);
				    }
					
					evaluation.setReleased(result.getInt("released") == 1);
					
					if (result.getDate("reviewed_date") != null)
				    {
						Timestamp reviewedDate = result.getTimestamp("reviewed_date");
				      	evaluation.setReviewedDate(reviewedDate);
				    }
				    else
				    {
				    	((EvaluationImpl)evaluation).setEvaluatedDate(null);
				    }
					
					evaluations.add(evaluation);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getEvaluations: " + e, e);
					}
					return null;
				}
			}
		});
		
		return evaluations;
	}
	
	/**
	 * Gets the forum evaluations with posts count
	 * 
	 * @param forumId	Forum id
	 * 
	 * @param all	All users or users with posts
	 * 
	 * @param evalSort	Evaluation sort
	 * 
	 * @return	The forum evaluations with posts count
	 */
	protected List<Evaluation> selectForumEvaluations(int forumId, boolean all, Evaluation.EvaluationsSort evalSort)
	{
		return selectTopicEvaluations(forumId, 0, all, evalSort);
	}
	
	/**
	 * Gets the topic evaluations with posts count
	 * 
	 * @param forumId	Forum id
	 * 
	 * @param topicId	Topic id
	 * 
	 * @param all	All users or users with posts
	 * 
	 * @param evalSort	Evaluation sort
	 * 
	 * @return	The topic evaluations with posts count
	 */
	protected List<Evaluation> selectTopicEvaluations(int forumId, int topicId, boolean all, Evaluation.EvaluationsSort evalSort)
	{
		if (forumId == 0 && topicId == 0)
		{
			 return new ArrayList<Evaluation>();
		}
		
		final List<Evaluation> evaluations = new ArrayList<Evaluation>();
		final Map<Integer, Evaluation> evalMap = new HashMap<Integer, Evaluation>();
		
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		int i = 0;
		
		Grade grade = null;
		Topic topic = null;
		Forum forum = null;
		Category category = null;
		
		if (topicId == 0)
		{	
			/*forum evaluations*/
			
			grade = selectByForum(forumId);
			
			if (grade == null)
			{
				return evaluations;
			}
			
			if (all) 
			{
				forum = forumDao.selectById(forumId);
				if (forum == null)
				{
					return evaluations; 
				}
				
				category = categoryDao.selectById(forum.getCategoryId());
				if (category == null)
				{
					return evaluations; 
				}
				
				// get all site users posts count in a forum
				sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_posts_count, MAX(post_time) AS last_post_time, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
				sql.append("FROM (SELECT user_id from jforum_site_users WHERE sakai_site_id = ?) s LEFT OUTER JOIN jforum_users u ON s.user_id = u.user_id ");
				sql.append("LEFT OUTER JOIN jforum_posts p ON s.user_id = p.user_id ");
				sql.append("AND p.forum_id = ? ");
				sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
				sql.append(applySort(evalSort));
				
				fields = new Object[2];
				fields[i++] = category.getContext();
				fields[i++] = forumId;
			}
			else
			{
				// get posts counts of users who posted in a forum
				sql.append("SELECT p.user_id, COUNT(p.post_id) AS user_posts_count, MAX(post_time) AS last_post_time, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
				sql.append("FROM jforum_posts p, jforum_users u ");
				sql.append("WHERE p.forum_id = ? and p.user_id = u.user_id ");
				sql.append("GROUP BY p.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
				sql.append(applySort(evalSort));
				
				fields = new Object[1];
				fields[i++] = forumId;				
			}
			
		}
		else
		{
			/*topic evaluations*/
			topic = topicDao.selectById(topicId);
			if (topic == null)
			{
				return evaluations; 
			}
			
			forum = forumDao.selectById(topic.getForumId());
			if (forum == null)
			{
				return evaluations; 
			}
			
			category = categoryDao.selectById(forum.getCategoryId());
			if (category == null)
			{
				return evaluations; 
			}
			
			grade = selectByForumTopic(topic.getForumId(), topicId);
			
			if (grade == null)
			{
				return evaluations;
			}
			
			if (all) 
			{
				// get all site users posts count in a topic
				sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_posts_count, MAX(post_time) AS last_post_time, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
				sql.append("FROM (SELECT user_id from jforum_site_users WHERE sakai_site_id = ?) s LEFT OUTER JOIN jforum_users u ON s.user_id = u.user_id ");
				sql.append("LEFT OUTER JOIN jforum_posts p ON s.user_id = p.user_id ");
				sql.append("AND p.forum_id = ? AND p.topic_id = ? ");
				sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
				sql.append(applySort(evalSort));
				
				fields = new Object[3];
				fields[i++] = category.getContext();
				fields[i++] = topic.getForumId();
				fields[i++] = topicId;
			}
			else
			{
				// get posts counts of users who posted in a topic
				sql.append("SELECT p.user_id, COUNT(p.post_id) AS user_posts_count, MAX(post_time) AS last_post_time, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
				sql.append("FROM jforum_posts p, jforum_users u ");
				sql.append("WHERE p.forum_id = ? and p.topic_id = ? and p.user_id = u.user_id ");
				sql.append("GROUP BY p.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
				sql.append(applySort(evalSort));
				
				fields = new Object[2];
				fields[i++] = topic.getForumId();
				fields[i++] = topicId;
			}
		}
		
		// user details with posts count
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Evaluation evaluation = fillEvaluationUserInfo(result);
					evaluations.add(evaluation);
					evalMap.put(new Integer(evaluation.getUserId()), evaluation);
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectTopicEvaluations - get user details: " + e, e);
					}
					return null;
				}
			}
		});
		
		// user late posts
		if (topicId == 0)
		{	
			// forum evaluations - late posts
			Date dueDate = null;
			List<SpecialAccess> specialAccessList = null;
			
			if ((forum != null) && (forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
			{
				dueDate = forum.getAccessDates().getDueDate();
				specialAccessList = forum.getSpecialAccess();
			}
			else if ((category != null) && (category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
			{
				dueDate = category.getAccessDates().getDueDate();
			}
			
			// forum late posts
			if (dueDate != null)
			{
				sql = new StringBuilder();
				if (all)
				{				
					// get all site users late posts count in a forum. Due dates are different for special access users
					sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_late_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
					sql.append("FROM (SELECT user_id from jforum_site_users WHERE sakai_site_id = ? ) s ");
					sql.append("LEFT OUTER JOIN jforum_users u ON s.user_id = u.user_id ");
					sql.append("LEFT OUTER JOIN jforum_posts p ON s.user_id = p.user_id ");
					sql.append("AND p.forum_id = ? ");
					sql.append("AND p.post_time > ? ");
					sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
					sql.append(applySort(evalSort));
					
					i = 0;
					fields = new Object[3];
					fields[i++] = category.getContext();
					fields[i++] = forum.getId();
					fields[i++] = dueDate;
				}
				else
				{
					// get late posts counts of users who posted in a topic.Due dates are different for special access users
					sql.append("SELECT p.user_id, COUNT(p.post_id) AS user_late_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
					sql.append("FROM jforum_posts p, jforum_users u ");
					sql.append("WHERE p.forum_id = ? AND p.user_id = u.user_id ");
					sql.append("AND p.post_time > ? ");
					sql.append("GROUP BY p.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
					sql.append(applySort(evalSort));
					
					i = 0;
					fields = new Object[3];
					fields[i++] = forum.getId();
					fields[i++] = dueDate;
				}
				
				// late posts
				this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
				{
					public Object readSqlResultRecord(ResultSet result)
					{
						try
						{
							int userId = result.getInt("user_id");
							if (evalMap.containsKey(new Integer(userId)))
							{
								int latePostCount = result.getInt("user_late_posts_count");
								if (latePostCount > 0)
								{
									Evaluation evaluation = evalMap.get(new Integer(userId));
									((EvaluationImpl)evaluation).setLate(Boolean.TRUE);
								}
							}
							
							return null;
						}
						catch (SQLException e)
						{
							if (logger.isWarnEnabled())
							{
								logger.warn("selectTopicEvaluations - get forum evaluations - get late posts: " + e, e);
							}
							return null;
						}
					}
				});			
			}
			
			// special access users late posts
			if (specialAccessList != null && !specialAccessList.isEmpty())
			{
				for (SpecialAccess specialAccess : specialAccessList)
				{
					List<Integer> userIds = specialAccess.getUserIds();
					
					if (userIds == null || userIds.isEmpty())
					{
						continue;
					}
					
					StringBuilder userIdStr = new StringBuilder();
					
					for (Integer userId : userIds)
					{
						userIdStr.append(userId);
						userIdStr.append(",");
						
						if (evalMap.containsKey(userId))
						{
							Evaluation evaluation = evalMap.get(userId);
							((EvaluationImpl)evaluation).setLate(Boolean.FALSE);
						}
					}
					
					if (userIdStr.length() == 0)
					{
						continue;
					}
					else
					{
						userIdStr.deleteCharAt(userIdStr.length() - 1);
					}
					
					if (specialAccess.getAccessDates() != null && specialAccess.getAccessDates().getDueDate() != null)
					{
						Date saDueDate = specialAccess.getAccessDates().getDueDate();
						
						sql = new StringBuilder();
						// get special access users late posts count in a topic.
						sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_late_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
						sql.append("FROM (SELECT user_id from jforum_site_users WHERE sakai_site_id = ? AND user_id IN ("+ userIdStr.toString()+")) s ");
						sql.append("LEFT OUTER JOIN jforum_users u ON s.user_id = u.user_id ");
						sql.append("LEFT OUTER JOIN jforum_posts p ON s.user_id = p.user_id ");
						sql.append("AND p.forum_id = ? ");
						sql.append("AND p.post_time > ? ");
						sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
						sql.append(applySort(evalSort));
						
						i = 0;
						fields = new Object[3];
						fields[i++] = category.getContext();
						fields[i++] = forum.getId();
						fields[i++] = saDueDate;
						
						// late posts
						this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
						{
							public Object readSqlResultRecord(ResultSet result)
							{
								try
								{
									int userId = result.getInt("user_id");
									if (evalMap.containsKey(new Integer(userId)))
									{
										int latePostCount = result.getInt("user_late_posts_count");
										if (latePostCount > 0)
										{
											Evaluation evaluation = evalMap.get(new Integer(userId));
											((EvaluationImpl)evaluation).setLate(Boolean.TRUE);
										}
									}
									
									return null;
								}
								catch (SQLException e)
								{
									if (logger.isWarnEnabled())
									{
										logger.warn("selectTopicEvaluations - get forum evaluations - get special access late posts: " + e, e);
									}
									return null;
								}
							}
						});
					}
				}
			}
		}
		else
		{
			// topic evaluations - late posts			
			Date dueDate = null;
			List<SpecialAccess> specialAccessList = null;
			if ((topic.getAccessDates() != null) && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null) || (topic.getAccessDates().getAllowUntilDate() != null)))
			{
				dueDate = topic.getAccessDates().getDueDate();
				specialAccessList = topic.getSpecialAccess();
			}
			else if ((forum != null) && (forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
			{
				dueDate = forum.getAccessDates().getDueDate();
				specialAccessList = forum.getSpecialAccess();
			}
			else if ((category != null) && (category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
			{
				dueDate = category.getAccessDates().getDueDate();
			}
			
			// topic evaluations - late posts
			if (dueDate != null)
			{
				sql = new StringBuilder();
				if (all)
				{				
					// get all site users late posts count in a topic. Due dates are different for special access users
					sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_late_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
					sql.append("FROM (SELECT user_id from jforum_site_users WHERE sakai_site_id = ? ) s ");
					sql.append("LEFT OUTER JOIN jforum_users u ON s.user_id = u.user_id ");
					sql.append("LEFT OUTER JOIN jforum_posts p ON s.user_id = p.user_id ");
					sql.append("AND p.forum_id = ? AND p.topic_id = ? ");
					sql.append("AND p.post_time > ? ");
					sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
					sql.append(applySort(evalSort));
					
					i = 0;
					fields = new Object[4];
					fields[i++] = category.getContext();
					fields[i++] = topic.getForumId();
					fields[i++] = topicId;
					fields[i++] = dueDate;
				}
				else
				{
					// get late posts counts of users who posted in a topic.Due dates are different for special access users
					sql.append("SELECT p.user_id, COUNT(p.post_id) AS user_late_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
					sql.append("FROM jforum_posts p, jforum_users u ");
					sql.append("WHERE p.forum_id = ? AND p.topic_id = ? AND p.user_id = u.user_id ");
					sql.append("AND p.post_time > ? ");
					sql.append("GROUP BY p.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
					sql.append(applySort(evalSort));
					
					i = 0;
					fields = new Object[3];
					fields[i++] = topic.getForumId();
					fields[i++] = topicId;
					fields[i++] = dueDate;
				}
				
				// late posts
				this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
				{
					public Object readSqlResultRecord(ResultSet result)
					{
						try
						{
							int userId = result.getInt("user_id");
							if (evalMap.containsKey(new Integer(userId)))
							{
								int latePostCount = result.getInt("user_late_posts_count");
								if (latePostCount > 0)
								{
									Evaluation evaluation = evalMap.get(new Integer(userId));
									((EvaluationImpl)evaluation).setLate(Boolean.TRUE);
								}
							}
							
							return null;
						}
						catch (SQLException e)
						{
							if (logger.isWarnEnabled())
							{
								logger.warn("selectTopicEvaluations - get topic evaluations - topic late posts: " + e, e);
							}
							return null;
						}
					}
				});
			}
			
			// special access users late posts
			if (specialAccessList != null && !specialAccessList.isEmpty())
			{
				for (SpecialAccess specialAccess : specialAccessList)
				{
					List<Integer> userIds = specialAccess.getUserIds();
					
					if (userIds == null || userIds.isEmpty())
					{
						continue;
					}
					
					StringBuilder userIdStr = new StringBuilder();
					
					for (Integer userId : userIds)
					{
						userIdStr.append(userId);
						userIdStr.append(",");
						
						if (evalMap.containsKey(userId))
						{
							Evaluation evaluation = evalMap.get(userId);
							((EvaluationImpl)evaluation).setLate(Boolean.FALSE);
						}
					}
					
					if (userIdStr.length() == 0)
					{
						continue;
					}
					else
					{
						userIdStr.deleteCharAt(userIdStr.length() - 1);
					}
					
					if (specialAccess.getAccessDates() != null && specialAccess.getAccessDates().getDueDate() != null)
					{
						Date saDueDate = specialAccess.getAccessDates().getDueDate();
						
						sql = new StringBuilder();
						// get special access users late posts count in a topic.
						sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_late_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
						sql.append("FROM (SELECT user_id from jforum_site_users WHERE sakai_site_id = ? AND user_id IN ("+ userIdStr.toString()+")) s ");
						sql.append("LEFT OUTER JOIN jforum_users u ON s.user_id = u.user_id ");
						sql.append("LEFT OUTER JOIN jforum_posts p ON s.user_id = p.user_id ");
						sql.append("AND p.forum_id = ? AND p.topic_id = ? ");
						sql.append("AND p.post_time > ? ");
						sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
						sql.append(applySort(evalSort));
						
						i = 0;
						fields = new Object[4];
						fields[i++] = category.getContext();
						fields[i++] = topic.getForumId();
						fields[i++] = topicId;
						fields[i++] = saDueDate;
						
						// late posts
						this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
						{
							public Object readSqlResultRecord(ResultSet result)
							{
								try
								{
									int userId = result.getInt("user_id");
									if (evalMap.containsKey(new Integer(userId)))
									{
										int latePostCount = result.getInt("user_late_posts_count");
										if (latePostCount > 0)
										{
											Evaluation evaluation = evalMap.get(new Integer(userId));
											((EvaluationImpl)evaluation).setLate(Boolean.TRUE);
										}
									}
									
									return null;
								}
								catch (SQLException e)
								{
									if (logger.isWarnEnabled())
									{
										logger.warn("selectTopicEvaluations - get topic evaluations - special access late posts: " + e, e);
									}
									return null;
								}
							}
						});
					}
				}
			}
		}
		
		// evaluation details
		sql = new StringBuilder();
		sql.append("SELECT evaluation_id, grade_id, user_id, sakai_user_id, score, comments, evaluated_by, evaluated_date, released, reviewed_date ");						
		sql.append(" FROM jforum_evaluations WHERE grade_id = ?");
		
		fields = new Object[1];
		i = 0;
		fields[i++] = grade.getId();
					
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					int userId = result.getInt("user_id");
					if (evalMap.containsKey(new Integer(userId)))
					{
						Evaluation evaluation = evalMap.get(new Integer(userId));
						
						((EvaluationImpl)evaluation).setId(result.getInt("evaluation_id"));
						((EvaluationImpl)evaluation).setGradeId(result.getInt("grade_id"));
						((EvaluationImpl)evaluation).setSakaiUserId(result.getString("sakai_user_id"));
						
						//evaluation.setScore(result.getFloat("score"));
						String str = result.getString("score");
						if (str == null)
						{
							evaluation.setScore(null);
						}
						else
						{
							try
							{
								evaluation.setScore(Float.valueOf(str));
							} 
							catch (NumberFormatException e)
							{
								evaluation.setScore(null);
							}
						}
						
						((EvaluationImpl)evaluation).setEvaluatedBy(result.getInt("evaluated_by"));
						
						if (result.getDate("evaluated_date") != null)
					    {
							Timestamp dueDate = result.getTimestamp("evaluated_date");
							((EvaluationImpl)evaluation).setEvaluatedDate(dueDate);
					    }
					    else
					    {
					    	((EvaluationImpl)evaluation).setEvaluatedDate(null);
					    }
						
						evaluation.setReleased(result.getInt("released") == 1);
						
						if (result.getDate("reviewed_date") != null)
					    {
							Timestamp reviewedDate = result.getTimestamp("reviewed_date");
					      	evaluation.setReviewedDate(reviewedDate);
					    }
					    else
					    {
					    	((EvaluationImpl)evaluation).setEvaluatedDate(null);
					    }
						
						evaluation.setComments(getEvaluationCommentsFromResultSet(result));
					}

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectTopicEvaluations - get Evaluation details: " + e, e);
					}
					return null;
				}
			}
		});
		
		return evaluations;
	}
	
	/**
	 * Gets the user evaluation 
	 * 
	 * @param gradeId	Grade id
	 * 
	 * @param userId	User id
	 * 
	 * @return	The user evaluation if exists or null
	 */
	protected Evaluation selectUserEvaluation(int gradeId, int userId)
	{
		String  sql = "SELECT evaluation_id, grade_id, user_id, sakai_user_id, score, comments,  evaluated_by, evaluated_date, released, reviewed_date  FROM jforum_evaluations WHERE grade_id = ? AND user_id = ?";
		
		Object[] fields = new Object[2];
		int i = 0;
		fields[i++] = gradeId;
		fields[i++] = userId;
					
		final List<Evaluation> evaluations = new ArrayList<Evaluation>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Evaluation evaluation = new EvaluationImpl();
					((EvaluationImpl)evaluation).setId(result.getInt("evaluation_id"));
					((EvaluationImpl)evaluation).setGradeId(result.getInt("grade_id"));
					((EvaluationImpl)evaluation).setSakaiUserId(result.getString("sakai_user_id"));
					//evaluation.setScore(result.getFloat("score"));
					String str = result.getString("score");
					if (str == null)
					{
						evaluation.setScore(null);
					}
					else
					{
						try
						{
							evaluation.setScore(Float.valueOf(str));
						} 
						catch (NumberFormatException e)
						{
							evaluation.setScore(null);
						}
					}
					((EvaluationImpl)evaluation).setEvaluatedBy(result.getInt("evaluated_by"));
					
					if (result.getDate("evaluated_date") != null)
				    {
						Timestamp dueDate = result.getTimestamp("evaluated_date");
						((EvaluationImpl)evaluation).setEvaluatedDate(dueDate);
				    }
				    else
				    {
				    	((EvaluationImpl)evaluation).setEvaluatedDate(null);
				    }
					
					evaluation.setReleased(result.getInt("released") == 1);
					
					if (result.getDate("reviewed_date") != null)
				    {
						Timestamp reviewedDate = result.getTimestamp("reviewed_date");
				      	evaluation.setReviewedDate(reviewedDate);
				    }
				    else
				    {
				    	((EvaluationImpl)evaluation).setEvaluatedDate(null);
				    }
					
					// comments
					evaluation.setComments(getEvaluationCommentsFromResultSet(result));
					
					evaluations.add(evaluation);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectUserEvaluation: " + e, e);
					}
					return null;
				}
			}
		});
		
		Evaluation evaluation = null;
		
		if (evaluations.size() == 1)
		{
			evaluation = evaluations.get(0);
		}
		
		return evaluation;
	}
	
	/**
	 * Updated existing evaluations
	 * 
	 * @param evaluation	Evaluation 
	 */
	protected void updateEvaluation(Evaluation evaluation)
	{
		if (evaluation == null)
		{
			return;
		}
		
		String sql = "UPDATE jforum_evaluations SET score = ?, comments = ?, evaluated_by = ?, evaluated_date = ?, released = ? WHERE evaluation_id = ?";
		
		Object[] fields = new Object[6];
		int i = 0;
		fields[i++] = evaluation.getScore();
		fields[i++] = evaluation.getComments();
		fields[i++] = evaluation.getEvaluatedBy();
		if (evaluation.getEvaluatedDate() == null)
		{
			fields[i++] = new Timestamp(System.currentTimeMillis());
		}
		else
		{
			fields[i++] = new Timestamp(evaluation.getEvaluatedDate().getTime());
			
		}
		fields[i++] = evaluation.isReleased() ? 1: 0;
		fields[i++] = evaluation.getId();

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
			
			throw new RuntimeException("updateEvaluation: dbWrite failed");
		}
	}
}
