/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/mysql/GradeDaoMysql.java $ 
 * $Id: GradeDaoMysql.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Evaluation;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.dao.GradeDao;
import org.etudes.component.app.jforum.dao.generic.GradeDaoGeneric;

public class GradeDaoMysql extends GradeDaoGeneric implements GradeDao
{
	private static Log logger = LogFactory.getLog(GradeDaoMysql.class);
	
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
	protected String getEvaluationCommentsFromResultSet(ResultSet rs) throws SQLException
	{
		return rs.getString("comments");
	}

	/**
	 * {@inheritDoc}
	 */
	protected int insertEvaluation(Evaluation evaluation)
	{
		if (evaluation.getGradeId() <= 0 || evaluation.getUserId() <= 0 || evaluation.getSakaiUserId() == null || evaluation.getSakaiUserId().trim().length() == 0 || evaluation.getEvaluatedBy() <= 0)
		{
			throw new IllegalArgumentException("Evaluation information is missing.");
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO jforum_evaluations(grade_id, user_id, sakai_user_id, score, comments, evaluated_by, evaluated_date, released) ");
		sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		
		Object[] fields = new Object[8];
		int i = 0;
		fields[i++] = evaluation.getGradeId();
		fields[i++] = evaluation.getUserId();
		fields[i++] = evaluation.getSakaiUserId();
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
		
		Long id = null;
		try
		{
			id = this.sqlService.dbInsert(null, sql.toString(), fields, "evaluation_id");
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
			throw new RuntimeException("insertEvaluation: dbInsert failed");
		}
		
		return id.intValue();
	}	

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int insertGrade(Grade grade)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO jforum_grade(context, grade_type, forum_id, topic_id, categories_id, points, add_to_gradebook, min_posts, min_posts_required)");
		sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		Object[] fields = new Object[9];
		int i = 0;
		fields[i++] =  grade.getContext();
		fields[i++] =  grade.getType();
		fields[i++] =  grade.getForumId();
		fields[i++] =  grade.getTopicId();
		fields[i++] =  grade.getCategoryId();
		fields[i++] =  grade.getPoints();
		fields[i++] =  (grade.isAddToGradeBook() ? 1 : 0);
		if (grade.isMinimumPostsRequired())
		{
			fields[i++] =  grade.getMinimumPosts();
			fields[i++] =  1;
		}
		else
		{
			fields[i++] =  0;
			fields[i++] =  0;
		}
		
		Long id = null;
		try
		{
			id = this.sqlService.dbInsert(null, sql.toString(), fields, "grade_id");
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
			throw new RuntimeException("insertGrade: dbInsert failed");
		}
		
		return id.intValue();
	}
}
