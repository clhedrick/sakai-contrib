/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumGradeServiceImpl.java $ 
 * $Id: JForumGradeServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Evaluation;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.JForumAccessException;
import org.etudes.api.app.jforum.JForumCategoryService;
import org.etudes.api.app.jforum.JForumForumService;
import org.etudes.api.app.jforum.JForumGBService;
import org.etudes.api.app.jforum.JForumGradeService;
import org.etudes.api.app.jforum.JForumPostService;
import org.etudes.api.app.jforum.JForumSecurityService;
import org.etudes.api.app.jforum.JForumUserService;
import org.etudes.api.app.jforum.Topic;
import org.etudes.api.app.jforum.User;
import org.etudes.api.app.jforum.dao.GradeDao;
import org.etudes.component.app.jforum.util.JForumUtil;
import org.sakaiproject.authz.api.Member;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;

public class JForumGradeServiceImpl implements JForumGradeService
{
	private static Log logger = LogFactory.getLog(JForumGradeServiceImpl.class);
	
	/** Dependency: gradeDao */
	protected GradeDao gradeDao = null;
	
	/** Dependency: JForumCategoryService. */
	protected JForumCategoryService jforumCategoryService = null;
	
	/** Dependency: JForumForumService. */
	protected JForumForumService jforumForumService = null;
	
	/** Dependency: SqlService */
	protected JForumGBService jforumGBService = null;

	/** Dependency: JForumPostService. */
	protected JForumPostService jforumPostService = null;

	/** Dependency: JForumSecurityService. */
	protected JForumSecurityService jforumSecurityService = null;
	
	/** Dependency: JForumUserService. */
	protected JForumUserService jforumUserService = null;
	
	/** Dependency: SiteService. */
	protected SiteService siteService = null;
	
	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	/**
	 * {@inheritDoc}
	 */
	public void addModifyCategoryEvaluations(Grade grade, List<Evaluation> evaluations) throws JForumAccessException
	{
		Grade exisCatGrade = getByCategoryId(grade.getCategoryId());
		
		if (exisCatGrade == null)
		{
			return;
		}
		
		if (grade == null || grade.getContext() == null || grade.getContext().trim().length() == 0)
		{
			return;
		}
		
		if (exisCatGrade.getId() != grade.getId())
		{
			return;
		}
		
		// check grade type - may be not needed
		if (grade.getType() != Grade.GradeType.CATEGORY.getType())
		{
			return;
		}
		
		Category category = jforumCategoryService.getCategory(grade.getCategoryId());
		
		if (category == null)
		{
			return;
		}
		
		if (evaluations.isEmpty())
		{
			/* "Send to Gradebook" may be changed*/
			
			//update gradebook
			updateGradebook(grade, category);
			
			// update grade for "add to grade book" status and update gradebook.
			gradeDao.updateAddToGradeBookStatus(grade.getId(), grade.isAddToGradeBook());
			
			return;
		}
				
		// check evaluated by has access to update or add evaluations.
		String evaluatedBySakaiUserId = null;
		evaluatedBySakaiUserId = evaluations.get(0).getEvaluatedBySakaiUserId();
		
		if (evaluatedBySakaiUserId == null)
		{
			return;
		}
		
		if (!jforumSecurityService.isJForumFacilitator(grade.getContext(), evaluatedBySakaiUserId))
		{
			throw new JForumAccessException(evaluatedBySakaiUserId);
		}
		
		// check grade id with evaluation grade id, evaluated by etc... Each user must have only one evaluation
		Map<String, Evaluation> userEvaluations = new HashMap<String, Evaluation>();
				
		Iterator<Evaluation> iter = evaluations.iterator();
		Evaluation evaluation = null;
		
		while(iter.hasNext()) 
		{
			evaluation = iter.next();
			
			// evaluation should belong same grade	
			if (grade.getId() != evaluation.getGradeId())
			{
				iter.remove();
				continue;
			}
			
			// one evaluation per participant user
			if (!jforumSecurityService.isJForumParticipant(grade.getContext(), evaluation.getSakaiUserId()))
			{
				iter.remove();
				continue;
			}
			
			// evaluated by should be same
			if (evaluation.getEvaluatedBySakaiUserId() == null || evaluation.getEvaluatedBySakaiUserId().trim().length() == 0
					|| !evaluation.getEvaluatedBySakaiUserId().trim().equalsIgnoreCase(evaluatedBySakaiUserId))
			{
				iter.remove();
				continue;
			}
			
			if (!userEvaluations.containsKey(evaluation.getSakaiUserId()))
			{
				userEvaluations.put(evaluation.getSakaiUserId(), evaluation);
			}
		} 
		
		List<Evaluation> userEvals = new ArrayList<Evaluation>();
		userEvals.addAll(userEvaluations.values());
		
		gradeDao.addUpdateEvaluations(grade, userEvals);
		 
		// remove and add to gradebook
		/*if (exisCatGrade.isAddToGradeBook())
		{
			// remove entry in the gradebook
			removeGradebookEntry(exisCatGrade);
		}*/
		
		//update gradebook
		updateGradebook(grade, category);
		
		// update grade for "add to grade book" status and update gradebook.
		if (grade.isAddToGradeBook())
		{
			gradeDao.updateAddToGradeBookStatus(grade.getId(), grade.isAddToGradeBook());
		}
		else
		{
			gradeDao.updateAddToGradeBookStatus(grade.getId(), grade.isAddToGradeBook());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addModifyForumEvaluations(Grade grade, List<Evaluation> evaluations) throws JForumAccessException
	{
		Grade exisForumGrade = getByForumId(grade.getForumId());
		
		if (exisForumGrade == null)
		{
			return;
		}
		
		if (grade == null || grade.getContext() == null || grade.getContext().trim().length() == 0)
		{
			return;
		}
		
		// may be not needed
		if (exisForumGrade.getId() != grade.getId())
		{
			return;
		}
		
		// check grade type - may be not needed
		if (grade.getType() != Grade.GradeType.FORUM.getType())
		{
			return;
		}
		
		Forum forum = jforumForumService.getForum(grade.getForumId());
		
		if (forum == null)
		{
			return;
		}
		if (evaluations.isEmpty())
		{
			/* "Send to Gradebook" may be changed*/
			
			updateGradebook(grade, forum);
			
			// update grade for "add to grade book" status and update gradebook.
			gradeDao.updateAddToGradeBookStatus(grade.getId(), grade.isAddToGradeBook());
				
			return;
		}
		
		// forum - groups
		filterForumGroupUsers(forum, evaluations);
		
		// check evaluated by has access to update or add evaluations.
		String evaluatedBySakaiUserId = null;
		evaluatedBySakaiUserId = evaluations.get(0).getEvaluatedBySakaiUserId();
		
		if (evaluatedBySakaiUserId == null)
		{
			return;
		}
		
		if (!jforumSecurityService.isJForumFacilitator(grade.getContext(), evaluatedBySakaiUserId))
		{
			throw new JForumAccessException(evaluatedBySakaiUserId);
		}
		
		// check grade id with evaluation grade id, evaluated by etc... Each user must have only one evaluation
		Map<String, Evaluation> userEvaluations = new HashMap<String, Evaluation>();
				
		Iterator<Evaluation> iter = evaluations.iterator();
		Evaluation evaluation = null;
		
		while(iter.hasNext()) 
		{
			evaluation = iter.next();
			
			// evaluation should belong same grade	
			if (grade.getId() != evaluation.getGradeId())
			{
				iter.remove();
				continue;
			}
			
			// one evaluation per participant user
			if (!jforumSecurityService.isJForumParticipant(grade.getContext(), evaluation.getSakaiUserId()))
			{
				iter.remove();
				continue;
			}
			
			// evaluated by should be same
			if (evaluation.getEvaluatedBySakaiUserId() == null || evaluation.getEvaluatedBySakaiUserId().trim().length() == 0
					|| !evaluation.getEvaluatedBySakaiUserId().trim().equalsIgnoreCase(evaluatedBySakaiUserId))
			{
				iter.remove();
				continue;
			}
			
			if (!userEvaluations.containsKey(evaluation.getSakaiUserId()))
			{
				userEvaluations.put(evaluation.getSakaiUserId(), evaluation);
			}
		} 
		
		List<Evaluation> userEvals = new ArrayList<Evaluation>();
		userEvals.addAll(userEvaluations.values());
		
		gradeDao.addUpdateEvaluations(grade, userEvals);
		 
		// remove and add to gradebook
		/*if (exisForumGrade.isAddToGradeBook())
		{
			// remove entry in the gradebook
			removeGradebookEntry(exisForumGrade);
		}*/
		
		// update grade book
		updateGradebook(grade, forum);
		
		// update grade for "add to grade book" status and update gradebook.
		if (grade.isAddToGradeBook())
		{
			gradeDao.updateAddToGradeBookStatus(grade.getId(), grade.isAddToGradeBook());
		}
		else
		{
			gradeDao.updateAddToGradeBookStatus(grade.getId(), grade.isAddToGradeBook());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addModifyTopicEvaluations(Grade grade, List<Evaluation> evaluations) throws JForumAccessException
	{
		if (grade == null || grade.getContext() == null || grade.getContext().trim().length() == 0)
		{
			return;
		}
		
		Grade exisTopicGrade = getByForumTopicId(grade.getForumId(), grade.getTopicId());
		
		if (exisTopicGrade == null)
		{
			return;
		}
		
		// may be not needed
		if (exisTopicGrade.getId() != grade.getId())
		{
			return;
		}
		
		// check grade type - may be not needed
		if (grade.getType() != Grade.GradeType.TOPIC.getType())
		{
			return;
		}
		
		Topic topic = jforumPostService.getTopic(grade.getTopicId());
		
		if (topic == null)
		{
			return;
		}
		
		if (evaluations.isEmpty())
		{
			/* "Send to Gradebook" may be changed*/
			
			// update gradebook
			updateGradebook(grade, topic);
			
			gradeDao.updateAddToGradeBookStatus(grade.getId(), grade.isAddToGradeBook());
			
			return;
		}
		
		Forum forum = topic.getForum();
		
		if (forum == null)
		{
			return;
		}
		
		// forum - groups
		filterForumGroupUsers(forum, evaluations);
		
		// check evaluated by has access to update or add evaluations.
		String evaluatedBySakaiUserId = null;
		evaluatedBySakaiUserId = evaluations.get(0).getEvaluatedBySakaiUserId();
		
		if (evaluatedBySakaiUserId == null)
		{
			return;
		}
		
		if (!jforumSecurityService.isJForumFacilitator(grade.getContext(), evaluatedBySakaiUserId))
		{
			throw new JForumAccessException(evaluatedBySakaiUserId);
		}
		
		// check grade id with evaluation grade id, evaluated by etc... Each user must have only one evaluation
		Map<String, Evaluation> userEvaluations = new HashMap<String, Evaluation>();
				
		Iterator<Evaluation> iter = evaluations.iterator();
		Evaluation evaluation = null;
		
		while(iter.hasNext()) 
		{
			evaluation = iter.next();
			
			// evaluation should belong same grade	
			if (grade.getId() != evaluation.getGradeId())
			{
				iter.remove();
				continue;
			}
			
			// one evaluation per participant user
			if (!jforumSecurityService.isJForumParticipant(grade.getContext(), evaluation.getSakaiUserId()))
			{
				iter.remove();
				continue;
			}
			
			// evaluated by should be same
			if (evaluation.getEvaluatedBySakaiUserId() == null || evaluation.getEvaluatedBySakaiUserId().trim().length() == 0
					|| !evaluation.getEvaluatedBySakaiUserId().trim().equalsIgnoreCase(evaluatedBySakaiUserId))
			{
				iter.remove();
				continue;
			}
			
			if (!userEvaluations.containsKey(evaluation.getSakaiUserId()))
			{
				userEvaluations.put(evaluation.getSakaiUserId(), evaluation);
			}
		} 
		
		List<Evaluation> userEvals = new ArrayList<Evaluation>();
		userEvals.addAll(userEvaluations.values());
		
		gradeDao.addUpdateEvaluations(grade, userEvals);
		 
		// remove and add to gradebook
		/*if (exisTopicGrade.isAddToGradeBook())
		{
			// remove entry in the gradebook
			removeGradebookEntry(exisTopicGrade);
		}*/
		
		// update gradebook
		updateGradebook(grade, topic);
		
		// update grade for "add to grade book" status and update gradebook.
		if (grade.isAddToGradeBook())
		{
			gradeDao.updateAddToGradeBookStatus(grade.getId(), grade.isAddToGradeBook());			
		}
		else
		{
			gradeDao.updateAddToGradeBookStatus(grade.getId(), grade.isAddToGradeBook());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addModifyUserEvaluation(Evaluation evaluation) throws JForumAccessException
	{
		if (evaluation == null)
		{
			return;
		}
		
		// check evaluated by has access to update or add evaluations.
		String evaluatedBySakaiUserId = evaluation.getEvaluatedBySakaiUserId();
		
		if (evaluatedBySakaiUserId == null || evaluatedBySakaiUserId.trim().length() == 0)
		{
			return;
		}
		
		String evaluationSakaiUserId = evaluation.getSakaiUserId();
		
		if (evaluationSakaiUserId == null || evaluationSakaiUserId.trim().length() == 0)
		{
			return;
		}
		
		Grade grade = gradeDao.selectGradeById(evaluation.getGradeId());
		
		if (grade == null)
		{
			return;
		}
		
		if (!jforumSecurityService.isJForumFacilitator(grade.getContext(), evaluatedBySakaiUserId))
		{
			throw new JForumAccessException(evaluatedBySakaiUserId);
		}
	
		Evaluation exisEval = null;
		
		if (grade.getType() == Grade.GradeType.CATEGORY.getType())
		{
			exisEval = getUserCategoryEvaluation(grade.getCategoryId(), evaluationSakaiUserId);
		}
		
		if (checkEvaluationChanges(exisEval, evaluation))
		{
			gradeDao.addUpdateUserEvaluation(evaluation);
			
			//update gradebook
			updateGradebookForUser(grade, evaluation);
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
	public Grade getByCategoryId(int categoryId)
	{
		if (categoryId <= 0) throw new IllegalArgumentException();
		
		/*StringBuilder sql = null;
		Object[] fields = null;
		int i = 0;
		
		sql = new StringBuilder();
		sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts  ");
		sql.append(" FROM jforum_grade WHERE categories_id = ? AND forum_id = 0 AND topic_id = 0");
		
		fields = new Object[1];
		fields[i++] = categoryId;
		
		return getGrade(sql.toString(), fields);*/
		return gradeDao.selectByCategory(categoryId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Grade getByForumId(int forumId)
	{
		if (forumId <= 0) throw new IllegalArgumentException();
		
		/*StringBuilder sql = null;
		Object[] fields = null;
		int i = 0;
		
		sql = new StringBuilder();
		sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts  ");
		sql.append(" FROM jforum_grade WHERE categories_id = 0 AND forum_id = ? AND topic_id = 0");
		
		fields = new Object[1];
		fields[i++] = forumId;
		
		return getGrade(sql.toString(), fields);*/
		return gradeDao.selectByForum(forumId);
	}

	/**
	 * {@inheritDoc}
	 */
	public Grade getByForumTopicId(int forumId, int topicId)
	{
		if (forumId <= 0 || topicId <= 0) throw new IllegalArgumentException();
		
		/*StringBuilder sql = null;
		Object[] fields = null;
		int i = 0;
		
		sql = new StringBuilder();
		sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts_required, min_posts  ");
		sql.append(" FROM jforum_grade WHERE categories_id = 0 AND forum_id = ? AND topic_id = ?");
		
		fields = new Object[2];
		fields[i++] = forumId;
		fields[i++] = topicId;
		
		return getGrade(sql.toString(), fields);*/
		return gradeDao.selectByForumTopic(forumId, topicId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Grade> getBySite(String siteId)
	{
		if (siteId == null || siteId.trim().length() == 0) throw new IllegalArgumentException();
		
		/*StringBuilder sql = new StringBuilder();
		sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts, min_posts_required ");
		sql.append("FROM jforum_grade WHere context = ?");
		
		int i = 0;
		Object[] fields = new Object[1];
		fields[i++] = siteId;
		
		return getGrades(sql.toString(), fields);*/
		return gradeDao.selectBySite(siteId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Grade> getBySiteByGradeType(String siteId, int gradeType)
	{
		if (siteId == null || siteId.trim().length() == 0) throw new IllegalArgumentException();
		if (!(gradeType == Grade.GradeType.CATEGORY.getType() || gradeType == Grade.GradeType.FORUM.getType() || gradeType == Grade.GradeType.TOPIC.getType()))
		{
			throw new IllegalArgumentException();
		}
		/*StringBuilder sql = new StringBuilder();
		sql.append("SELECT grade_id, context, grade_type, forum_id, topic_id, points, add_to_gradebook, categories_id, min_posts, min_posts_required ");
		sql.append("FROM jforum_grade WHERE context = ? AND grade_type = ?");
		
		int i = 0;
		Object[] fields = new Object[2];
		fields[i++] = siteId;
		fields[i++] = gradeType;
		
		return getGrades(sql.toString(), fields);*/
		return gradeDao.selectBySiteByGradeType(siteId, gradeType);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Evaluation> getCategoryEvaluations(int categoryId)
	{
		if (categoryId <= 0)
		{
			throw new IllegalArgumentException("Category information is missing.");
		}
		
		List<Evaluation> evaluations = new ArrayList<Evaluation>();
		
		Grade grade = getByCategoryId(categoryId);
		
		if (grade != null)
		{
			evaluations.addAll(gradeDao.selectEvaluations(grade));
		}
		
		return evaluations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getCategoryEvaluationsCount(int categoryId)
	{
		if (categoryId <= 0) throw new IllegalArgumentException("Not a valid category id.");
		
		Grade grade = gradeDao.selectByCategory(categoryId);
		
		if (grade != null)
		{
			return gradeDao.selectEvaluationsCount(grade);
		}
		
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Evaluation> getCategoryEvaluationsWithPosts(int categoryId, Evaluation.EvaluationsSort evalSort, String sakaiUserId, boolean all) throws JForumAccessException
	{
		if (categoryId <= 0)
		{
			throw new IllegalArgumentException("category information is missing.");
		}
		
		List<Evaluation> evaluations = new ArrayList<Evaluation>();
		
		Grade grade = getByCategoryId(categoryId);
				
		if (grade == null)
		{
			return evaluations;
		}
		
		Category category = jforumCategoryService.getCategory(categoryId);
		
		// publish to gradebook if the open date of the grade is in the past
		Date now = new Date();
		if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))		
		{
			if (category.getAccessDates().getOpenDate().before(now))
			{
				if (grade.getItemOpenDate() != null)
				{
					updateGradebook(grade, category);
				}
			}
		}
		
		if (!jforumSecurityService.isJForumFacilitator(category.getContext(), sakaiUserId))
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		// sync sakai users
		jforumUserService.getSiteUsers(category.getContext(), 0, 0);
		
		evaluations.addAll(gradeDao.selectCategoryEvaluationsWithPosts(categoryId, evalSort, all));
		
		Iterator<Evaluation> iterEvaluation = evaluations.iterator();
		
		Evaluation evaluation = null;
		String evalSakaiUserId = null;
		while (iterEvaluation.hasNext()) 
		{
			evaluation = iterEvaluation.next();
			
			evalSakaiUserId = evaluation.getSakaiUserId();
			
			// remove facilitators and admin users
			if (jforumSecurityService.isJForumFacilitator(category.getContext(), evalSakaiUserId))
			{
				iterEvaluation.remove();
				continue;
			}
			
			// retain only participants
			if (!jforumSecurityService.isJForumParticipant(category.getContext(), evalSakaiUserId))
			{
				iterEvaluation.remove();
				continue;
			}
			
			// remove inactive users
			if (!jforumSecurityService.isUserActive(category.getContext(), evalSakaiUserId))
			{
				iterEvaluation.remove();
				continue;
			}
			
			// remove the users with blocked role
			if (jforumSecurityService.isUserBlocked(category.getContext(), evalSakaiUserId))
			{
				iterEvaluation.remove();
				continue;
			}
			
			// set sakai display id
			try
			{
				org.sakaiproject.user.api.User sakUser = UserDirectoryService.getUser(evalSakaiUserId);
				((EvaluationImpl)evaluation).setSakaiDisplayId(sakUser.getDisplayId());
			}
			catch (UserNotDefinedException e)
			{
				iterEvaluation.remove();
			}
		}
		//sort evaluations
		sortEvaluations(evaluations, evalSort);
		
		return evaluations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getCategoryGradableForumsCount(int categoryId)
	{
		if (categoryId <= 0) throw new IllegalArgumentException();
		
		return gradeDao.selectCategoryGradableForumsCount(categoryId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Evaluation> getForumEvaluations(int forumId)
	{
		List<Evaluation> evaluations = new ArrayList<Evaluation>();
		
		Grade grade = getByForumId(forumId);
		
		if (grade != null)
		{
			evaluations.addAll(gradeDao.selectEvaluations(grade));
		}
		
		return evaluations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getForumEvaluationsCount(int forumId)
	{
		if (forumId <= 0) throw new IllegalArgumentException("Not a valid forum id.");
		
		Grade grade = gradeDao.selectByForum(forumId);
		
		if (grade != null)
		{
			return gradeDao.selectEvaluationsCount(grade);
		}
		
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Evaluation> getForumEvaluationsWithPosts(int forumId, Evaluation.EvaluationsSort evalSort, String sakaiUserId, boolean all) throws JForumAccessException
	{
		if (forumId <= 0)
		{
			throw new IllegalArgumentException("forum information is missing.");
		}
		
		List<Evaluation> evaluations = new ArrayList<Evaluation>();
		
		Grade grade = getByForumId(forumId);
		
		if (grade == null)
		{
			return evaluations;
		}
		
		Forum forum = jforumForumService.getForum(forumId);
		
		Category category = forum.getCategory();
		
		if (!jforumSecurityService.isJForumFacilitator(category.getContext(), sakaiUserId))
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		// publish to gradebook if the open date of the grade is in the past
		Date now = new Date();
		if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))		
		{
			if (forum.getAccessDates().getOpenDate() != null && forum.getAccessDates().getOpenDate().before(now))
			{
				if (forum.getAccessDates().isHideUntilOpen())
				{
					updateGradebook(grade, forum);
				}
			}
		}
		else 		
		{
			if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))
			{
				if (category.getAccessDates().getOpenDate().before(now))
				{
					if (category.getAccessDates().isHideUntilOpen())
					{
						updateGradebook(grade, forum);
					}
				}
			}
		}
		
		// sync sakai users
		jforumUserService.getSiteUsers(category.getContext(), 0, 0);
		
		evaluations.addAll(gradeDao.selectForumEvaluationsWithPosts(forumId, evalSort, all));
		
		Iterator<Evaluation> iterEvaluation = evaluations.iterator();
		
		Evaluation evaluation = null;
		String evalSakaiUserId = null;
		while (iterEvaluation.hasNext()) 
		{
			evaluation = iterEvaluation.next();
			
			evalSakaiUserId = evaluation.getSakaiUserId();
			
			// remove facilitators and admin users
			if (jforumSecurityService.isJForumFacilitator(category.getContext(), evalSakaiUserId))
			{
				iterEvaluation.remove();
				continue;
			}
			
			// retain only participants
			if (!jforumSecurityService.isJForumParticipant(category.getContext(), evalSakaiUserId))
			{
				iterEvaluation.remove();
				continue;
			}
			
			// remove inactive users
			if (!jforumSecurityService.isUserActive(category.getContext(), evalSakaiUserId))
			{
				iterEvaluation.remove();
				continue;
			}
			
			// remove the users with blocked role
			if (jforumSecurityService.isUserBlocked(category.getContext(), evalSakaiUserId))
			{
				iterEvaluation.remove();
				continue;
			}
			
			// set sakai display id
			try
			{
				org.sakaiproject.user.api.User sakUser = UserDirectoryService.getUser(evalSakaiUserId);
				((EvaluationImpl)evaluation).setSakaiDisplayId(sakUser.getDisplayId());
			}
			catch (UserNotDefinedException e)
			{
				iterEvaluation.remove();
			}
		}
		
		//sort evaluations
		sortEvaluations(evaluations, evalSort);
		
		//
		if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
		{
			filterForumGroupUsers(forum, evaluations);

			sortEvaluationGroups(evalSort, evaluations);
		}
		
		return evaluations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getForumTopicEvaluationsCount(int forumId)
	{
		if (forumId <= 0) throw new IllegalArgumentException("Not a valid forum id.");
		
		return gradeDao.selectForumTopicEvaluationsCount(forumId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Grade> getForumTopicGradesByCategory(int categoryId)
	{
		if (categoryId <= 0) throw new IllegalArgumentException();
		
		return gradeDao.selectForumTopicGradesByCategoryId(categoryId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Grade> getForumTopicGradesByForum(int forumId)
	{
		return gradeDao.selectForumTopicGradesByForumId(forumId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Evaluation> getTopicEvaluations(int forumId, int topicId)
	{
		List<Evaluation> evaluations = new ArrayList<Evaluation>();
		
		Grade grade = getByForumTopicId(forumId, topicId);
		
		if (grade != null)
		{
			evaluations.addAll(gradeDao.selectEvaluations(grade));
		}
		
		return evaluations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTopicEvaluationsCount(int forumId, int topicId)
	{
		if (forumId <= 0 || topicId <= 0) throw new IllegalArgumentException("Not a valid forum or topic id.");
		
		Grade grade = gradeDao.selectByForumTopic(forumId, topicId);
		
		if (grade != null)
		{
			return gradeDao.selectEvaluationsCount(grade);
		}
		
		return 0;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Evaluation> getTopicEvaluationsWithPosts(int topicId, Evaluation.EvaluationsSort evalSort, String sakaiUserId, boolean all) throws JForumAccessException
	{
		if (topicId <= 0)
		{
			throw new IllegalArgumentException("topic information is missing.");
		}
		
		List<Evaluation> evaluations = new ArrayList<Evaluation>();
		
		Topic topic = jforumPostService.getTopic(topicId);
		
		if (topic == null)
		{
			return evaluations;
		}
		
		Grade grade = getByForumTopicId(topic.getForumId(), topicId);
		
		if (grade == null)
		{
			return evaluations;
		}
		
		Forum forum = topic.getForum();
		
		Category category = forum.getCategory();
		
		if (!jforumSecurityService.isJForumFacilitator(category.getContext(), sakaiUserId))
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		// publish to gradebook if the open date of the grade is in the past
		Date now = new Date();
		if ((topic.getAccessDates() != null) && (topic.getAccessDates().getOpenDate() != null || topic.getAccessDates().getDueDate() != null || topic.getAccessDates().getAllowUntilDate() != null))		
		{
			if (topic.getAccessDates().getOpenDate() != null && topic.getAccessDates().getOpenDate().before(now))
			{
				if (topic.getAccessDates().isHideUntilOpen())
				{
					updateGradebook(grade, topic);
				}
			}
		}
		else if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))		
		{
			if (forum.getAccessDates().getOpenDate() != null && forum.getAccessDates().getOpenDate().before(now))
			{
				if (forum.getAccessDates().isHideUntilOpen())
				{
					updateGradebook(grade, topic);
				}
			}
		}
		else 		
		{
			if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))
			{
				if (category.getAccessDates().getOpenDate().before(now))
				{
					if (category.getAccessDates().isHideUntilOpen())
					{
						updateGradebook(grade, topic);
					}
				}
			}
		}
		
		// sync sakai users
		jforumUserService.getSiteUsers(category.getContext(), 0, 0);
		
		evaluations.addAll(gradeDao.selectTopicEvaluationsWithPosts(topicId, evalSort, all));
		
		Iterator<Evaluation> iterEvaluation = evaluations.iterator();
		
		Evaluation evaluation = null;
		String evalSakaiUserId = null;
		while (iterEvaluation.hasNext()) 
		{
			evaluation = iterEvaluation.next();
			
			evalSakaiUserId = evaluation.getSakaiUserId();
			
			// remove facilitators and admin users
			if (jforumSecurityService.isJForumFacilitator(category.getContext(), evalSakaiUserId))
			{
				iterEvaluation.remove();
				continue;
			}
			
			// retain only participants
			if (!jforumSecurityService.isJForumParticipant(category.getContext(), evalSakaiUserId))
			{
				iterEvaluation.remove();
				continue;
			}
			
			// remove inactive users
			if (!jforumSecurityService.isUserActive(category.getContext(), evalSakaiUserId))
			{
				iterEvaluation.remove();
				continue;
			}
			
			// remove the users with blocked role
			if (jforumSecurityService.isUserBlocked(category.getContext(), evalSakaiUserId))
			{
				iterEvaluation.remove();
				continue;
			}
			
			// set sakai display id
			try
			{
				org.sakaiproject.user.api.User sakUser = UserDirectoryService.getUser(evalSakaiUserId);
				((EvaluationImpl)evaluation).setSakaiDisplayId(sakUser.getDisplayId());
			}
			catch (UserNotDefinedException e)
			{
				iterEvaluation.remove();
			}
		}
		
		//sort evaluations
		sortEvaluations(evaluations, evalSort);
		
		//
		if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
		{
			filterForumGroupUsers(forum, evaluations);

			sortEvaluationGroups(evalSort, evaluations);
		}
		
		return evaluations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Grade> getTopicGradesByForum(int forumId)
	{
		if (forumId <= 0) throw new IllegalArgumentException();
		
		return gradeDao.selectTopicGradesByForumId(forumId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation getUserCategoryEvaluation(int categoryId, String sakaiUserId)
	{
		if (categoryId <= 0)
		{
			throw new IllegalArgumentException("Category information is missing.");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("User information is missing.");
		}
		
		Grade grade = getByCategoryId(categoryId);
		
		if (grade != null)
		{
			// publish to gradebook if the open date of the grade is in the past
			if (grade.getItemOpenDate() != null)
			{
				Date now = new Date();
				Category category = jforumCategoryService.getCategory(categoryId);
				
				if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))		
				{
					if (category.getAccessDates().getOpenDate().before(now))
					{
						if (category.getAccessDates().isHideUntilOpen())
						{
							updateGradebook(grade, category);
						}
					}
				}
			}
						
			User user = jforumUserService.getBySakaiUserId(sakaiUserId);
			
			if (user != null)
			{
				return gradeDao.selectUserCategoryEvaluation(categoryId, user.getId());
			}			
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation getUserCategoryEvaluation(int categoryId, String sakaiUserId, boolean checkLatePosts)
	{
		if (categoryId <= 0)
		{
			throw new IllegalArgumentException("Category information is missing.");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("User information is missing.");
		}
		
		Grade grade = getByCategoryId(categoryId);
		
		if (grade != null)
		{
			// publish to gradebook if the open date of the grade is in the past
			if (grade.getItemOpenDate() != null)
			{
				Date now = new Date();
				Category category = jforumCategoryService.getCategory(categoryId);
				
				if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))		
				{
					if (category.getAccessDates().getOpenDate().before(now))
					{
						if (category.getAccessDates().isHideUntilOpen())
						{
							updateGradebook(grade, category);
						}
					}
				}
			}
			
			User user = jforumUserService.getBySakaiUserId(sakaiUserId);
			
			if (user != null)
			{
				return gradeDao.selectUserCategoryEvaluation(categoryId, user.getId(), true);
			}			
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation getUserForumEvaluation(int forumId, String sakaiUserId)
	{
		if (forumId <= 0)
		{
			throw new IllegalArgumentException("Forum information is missing.");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("User information is missing.");
		}
		
		Grade grade = getByForumId(forumId);
		
		if (grade != null)
		{
			// publish to gradebook if the open date of the grade is in the past
			if (grade.getItemOpenDate() != null)
			{
				Date now = new Date();
				Forum forum = jforumForumService.getForum(forumId);
				if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))		
				{
					if (forum.getAccessDates().getOpenDate() != null && forum.getAccessDates().getOpenDate().before(now))
					{
						if (forum.getAccessDates().isHideUntilOpen())
						{
							updateGradebook(grade, forum);
						}
					}
				}
				else 		
				{
					Category category = forum.getCategory();
					
					if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))
					{
						if (category.getAccessDates().getOpenDate().before(now))
						{
							if (category.getAccessDates().isHideUntilOpen())
							{
								updateGradebook(grade, forum);
							}
						}
					}
				}
			}
						
			User user = jforumUserService.getBySakaiUserId(sakaiUserId);
			
			if (user != null)
			{
				return gradeDao.selectUserForumEvaluation(forumId, user.getId());
			}			
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation getUserForumEvaluation(int forumId, String sakaiUserId, boolean checkLatePosts)
	{
		if (forumId <= 0)
		{
			throw new IllegalArgumentException("Forum information is missing.");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("User information is missing.");
		}
		
		Grade grade = getByForumId(forumId);
		
		if (grade != null)
		{
			// publish to gradebook if the open date of the grade is in the past
			if (grade.getItemOpenDate() != null)
			{
				Date now = new Date();
				Forum forum = jforumForumService.getForum(forumId);
				if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))		
				{
					if (forum.getAccessDates().getOpenDate() != null && forum.getAccessDates().getOpenDate().before(now))
					{
						if (forum.getAccessDates().isHideUntilOpen())
						{
							updateGradebook(grade, forum);
						}
					}
				}
				else 		
				{
					Category category = forum.getCategory();
					
					if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))
					{
						if (category.getAccessDates().getOpenDate().before(now))
						{
							if (category.getAccessDates().isHideUntilOpen())
							{
								updateGradebook(grade, forum);
							}
						}
					}
				}
			}
						
			User user = jforumUserService.getBySakaiUserId(sakaiUserId);
			
			if (user != null)
			{
				return gradeDao.selectUserForumEvaluation(forumId, user.getId(), true);
			}			
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation getUserTopicEvaluation(int topicId, String sakaiUserId)
	{
		if (topicId <= 0)
		{
			throw new IllegalArgumentException("Topic information is missing.");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("User information is missing.");
		}
		
		Topic topic = jforumPostService.getTopic(topicId);
				
		Grade grade = getByForumTopicId(topic.getForumId(), topic.getId());
		
		if (grade != null)
		{
			// publish to gradebook if the open date of the grade is in the past
			if (grade.getItemOpenDate() != null)
			{
				Date now = new Date();
								
				if ((topic.getAccessDates() != null) && (topic.getAccessDates().getOpenDate() != null || topic.getAccessDates().getDueDate() != null || topic.getAccessDates().getAllowUntilDate() != null))		
				{
					if (topic.getAccessDates().getOpenDate() != null && topic.getAccessDates().getOpenDate().before(now))
					{
						if (topic.getAccessDates().isHideUntilOpen())
						{
							updateGradebook(grade, topic);
						}
					}
				}
				else		
				{
					Forum forum = jforumForumService.getForum(topic.getForumId());
					
					if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))
					{						
						if (forum.getAccessDates().getOpenDate() != null && forum.getAccessDates().getOpenDate().before(now))
						{
							if (forum.getAccessDates().isHideUntilOpen())
							{
								updateGradebook(grade, topic);
							}
						}
					}
					else 		
					{
						Category category = forum.getCategory();
						
						if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))
						{
							if (category.getAccessDates().getOpenDate().before(now))
							{
								if (category.getAccessDates().isHideUntilOpen())
								{
									updateGradebook(grade, topic);
								}
							}
						}
					}
				}				
			}
			
			User user = jforumUserService.getBySakaiUserId(sakaiUserId);
			
			if (user != null)
			{
				return gradeDao.selectUserTopicEvaluation(topic.getForumId(), topicId, user.getId());
			}			
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation getUserTopicEvaluation(int topicId, String sakaiUserId, boolean checkLatePosts)
	{
		if (topicId <= 0)
		{
			throw new IllegalArgumentException("Topic information is missing.");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("User information is missing.");
		}
		
		Topic topic = jforumPostService.getTopic(topicId);
				
		Grade grade = getByForumTopicId(topic.getForumId(), topic.getId());
		
		if (grade != null)
		{
			// publish to gradebook if the open date of the grade is in the past
			if (grade.getItemOpenDate() != null)
			{
				Date now = new Date();
								
				if ((topic.getAccessDates() != null) && (topic.getAccessDates().getOpenDate() != null || topic.getAccessDates().getDueDate() != null || topic.getAccessDates().getAllowUntilDate() != null))		
				{
					if (topic.getAccessDates().getOpenDate() != null && topic.getAccessDates().getOpenDate().before(now))
					{
						if (topic.getAccessDates().isHideUntilOpen())
						{
							updateGradebook(grade, topic);
						}
					}
				}
				else		
				{
					Forum forum = jforumForumService.getForum(topic.getForumId());
					
					if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))
					{						
						if (forum.getAccessDates().getOpenDate() != null && forum.getAccessDates().getOpenDate().before(now))
						{
							if (forum.getAccessDates().isHideUntilOpen())
							{
								updateGradebook(grade, topic);
							}
						}
					}
					else 		
					{
						Category category = forum.getCategory();
						
						if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))
						{
							if (category.getAccessDates().getOpenDate().before(now))
							{
								if (category.getAccessDates().isHideUntilOpen())
								{
									updateGradebook(grade, topic);
								}
							}
						}
					}
				}				
			}
						
			User user = jforumUserService.getBySakaiUserId(sakaiUserId);
			
			if (user != null)
			{
				return gradeDao.selectUserTopicEvaluation(topic.getForumId(), topicId, user.getId(), true);
			}			
		}
		
		return null;
	}
	
	public void init()
	{
		if (logger.isInfoEnabled())
			logger.info("init....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void markUserReviewedDate(Evaluation evaluation) throws JForumAccessException
	{
		if (evaluation == null || evaluation.getId() <= 0 || evaluation.getGradeId() <= 0 || evaluation.getSakaiUserId() == null || evaluation.getSakaiUserId().trim().length() == 0)
		{
			return;
		}
		
		Grade grade = gradeDao.selectGradeById(evaluation.getGradeId());
		
		if (grade == null)
		{
			return;
		}
		
		boolean isParticipant = jforumSecurityService.isJForumParticipant(grade.getContext(), evaluation.getSakaiUserId());
		
		if (!isParticipant)
		{
			throw new JForumAccessException(evaluation.getSakaiUserId());
		}
		
		gradeDao.updateUserReviewedDate(evaluation.getId());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void modifyAddToGradeBookStatus(int gradeId, boolean addToGradeBook)
	{
		if (gradeId <= 0)
		{
			throw new IllegalArgumentException("Grade information is missing.");
		}
		
		gradeDao.updateAddToGradeBookStatus(gradeId, addToGradeBook);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void modifyForumGrade(Grade grade)
	{
		if (grade == null)
		{
			new IllegalArgumentException("grade is missing");
		}
		
		if (grade.getId() == 0 || grade.getForumId() == 0 || grade.getTopicId() > 0 || grade.getCategoryId() > 0)
		{
			new IllegalArgumentException("grade information is missing");
		}
		
		gradeDao.updateForumGrade(grade);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void modifyTopicGrade(Grade grade)
	{
		if (grade == null)
		{
			new IllegalArgumentException("grade is missing");
		}
		
		if (grade.getId() == 0 || grade.getForumId() == 0 || grade.getTopicId() == 0 ||  grade.getCategoryId() > 0)
		{
			new IllegalArgumentException("grade information is missing");
		}
		
		gradeDao.updateTopicGrade(grade);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Evaluation newEvaluation(int gradeId, String evaluatedBySakaiUserId, String evaluationSakaiUserId)
	{
		if (gradeId <= 0 || evaluationSakaiUserId == null || evaluationSakaiUserId.trim().length() == 0)
		{
			return null;
		}
		
		Evaluation evaluation = new EvaluationImpl();
		((EvaluationImpl)evaluation).setId(-1);
		
		((EvaluationImpl)evaluation).setGradeId(gradeId);
		((EvaluationImpl)evaluation).setSakaiUserId(evaluationSakaiUserId);
		((EvaluationImpl)evaluation).setEvaluatedBySakaiUserId(evaluatedBySakaiUserId);
		
		return evaluation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean removeGradebookEntry(Grade grade)
	{
		if (grade == null || grade.getContext() == null)
		{
			throw new IllegalArgumentException("grade is null.");
		}
		
		/*if (grade.isAddToGradeBook())
		{
			return false;
		}*/
		
		String gradebookUid = grade.getContext();
		
		if (jforumGBService.isExternalAssignmentDefined(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId())))
		{			
			return jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
		}
		
		// check and delete the record of existing grade schedule that needs to be published to gradebook 
		if (grade.getItemOpenDate() != null)
		{
			// delete existing grade schedule that needs to be published to gradebook
			gradeDao.deleteScheduledGradeToGradebook(grade.getId());
		}
		
		return false;
	}
	
	/**
	 * @param gradeDao the gradeDao to set
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
	 * @param jforumForumService the jforumForumService to set
	 */
	public void setJforumForumService(JForumForumService jforumForumService)
	{
		this.jforumForumService = jforumForumService;
	}

	/**
	 * @param jforumGBService 
	 * 				The jforumGBService to set
	 */
	public void setJforumGBService(JForumGBService jforumGBService)
	{
		this.jforumGBService = jforumGBService;
	}
	
	/**
	 * @param jforumPostService the jforumPostService to set
	 */
	public void setJforumPostService(JForumPostService jforumPostService)
	{
		this.jforumPostService = jforumPostService;
	}
	
	/**
	 * @param jforumSecurityService the jforumSecurityService to set
	 */
	public void setJforumSecurityService(JForumSecurityService jforumSecurityService)
	{
		this.jforumSecurityService = jforumSecurityService;
	}
	
	/**
	 * @param jforumUserService the jforumUserService to set
	 */
	public void setJforumUserService(JForumUserService jforumUserService)
	{
		this.jforumUserService = jforumUserService;
	}
	
	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService)
	{
		this.siteService = siteService;
	}

	/**
	 * @param sqlService 
	 * 				The sqlService to set
	 */
	public void setSqlService(SqlService sqlService)
	{
		this.sqlService = sqlService;
	}
	
	/**
	 * gets the evaluations
	 * 
	 * @param grade		Grade
	 * 
	 * @return	List of evaluations
	 */
	/*protected List<Evaluation> getEvaluations(Grade grade)
	{
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
					evaluation.setId(result.getInt("evaluation_id"));
					evaluation.setGradeId(result.getInt("grade_id"));
					evaluation.setSakaiUserId(result.getString("sakai_user_id"));
					evaluation.setScore(result.getFloat("score"));
					evaluation.setEvaluatedBy(result.getInt("evaluated_by"));
					
					if (result.getDate("evaluated_date") != null)
				    {
						Timestamp dueDate = result.getTimestamp("evaluated_date");
				      	evaluation.setEvaluatedDate(dueDate);
				    }
				    else
				    {
				    	evaluation.setEvaluatedDate(null);
				    }
					
					evaluation.setReleased(result.getInt("released") == 1);
					
					if (result.getDate("reviewed_date") != null)
				    {
						Timestamp reviewedDate = result.getTimestamp("reviewed_date");
				      	evaluation.setReviewedDate(reviewedDate);
				    }
				    else
				    {
				    	evaluation.setEvaluatedDate(null);
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
	}*/
	
	/**
	 * Gets the grade
	 * 
	 * @param sql		SQL Query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return		The grade if existing else null
	 *//*
	protected Grade getGrade(String sql, Object[] fields)
	{
		Grade grade = null;
		
		List<Grade> grades = new ArrayList<Grade>();
		
		if (sql != null && fields != null)
		{
			grades.addAll(readGrades(sql, fields));
		}
			
		if (grades.size() == 1)
		{
			grade = grades.get(0);
		}
		return grade;
	}*/

	/**
	 * {@inheritDoc}
	 */
	public boolean updateGradebook(Grade grade, Category category)
	{
		if (grade == null || category == null)
		{
			throw new IllegalArgumentException("grade or category is null.");
		}
		
		String gradebookUid = grade.getContext();
		
		if (!jforumGBService.isGradebookDefined(gradebookUid))
		{
			return false;
		}
		
		if (!grade.isAddToGradeBook())
		{
			if (jforumGBService.isExternalAssignmentDefined(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId())))
			{
				jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
			}
			
			if (grade.getItemOpenDate() != null)
			{
				// delete existing grade schedule that needs to be published to gradebook
				gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
			}
			
			return false;
		}
		
		if (grade.getType() != Grade.GradeType.CATEGORY.getType())
		{
			return false;
		}
				
		// update gradebook
		String url = null;
		Date dueDate = null;
		if (category.getAccessDates() != null)
		{
			dueDate = category.getAccessDates().getDueDate();
		}
		
		Date now = new Date();
		
		if (jforumGBService.isExternalAssignmentDefined(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId())))
		{
			// remove and add again
			jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
			
		}
			
		// add to gradebook
		if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))		
		{
			if (category.getAccessDates().getOpenDate().after(now))
			{
				if (category.getAccessDates().isHideUntilOpen())
				{
					if (jforumGBService.isExternalAssignmentDefined(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId())))
					{
						jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
					}
					// gradeDao.updateAddToGradeBookStatus(grade.getId(), false);
					
					// existing schedule
					if (grade.getItemOpenDate() != null)
					{
						// delete existing grade schedule that needs to be published to gradebook
						gradeDao.deleteScheduledGradeToGradebook(grade.getId());
					}
					
					// add the grade to gradebook when it opened
					gradeDao.addScheduledGradeToGradebook(grade.getId(), category.getAccessDates().getOpenDate());
					return false;
				}
				else
				{
					if (grade.getItemOpenDate() != null)
					{
						// delete existing grade schedule that needs to be published to gradebook
						gradeDao.deleteScheduledGradeToGradebook(grade.getId());
					}
					
				}
			}
			else
			{
				if (grade.getItemOpenDate() != null)
				{
					// delete existing grade schedule that needs to be published to gradebook
					gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
				}
			}
		}
		else
		{
			if (grade.getItemOpenDate() != null)
			{
				// delete existing grade schedule that needs to be published to gradebook
				gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
			}				
		}
		
		// add to gradebook
		if (jforumGBService.addExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()), url, category.getTitle(), JForumUtil.toDoubleScore(grade.getPoints()), dueDate, JForumGradeService.GRADE_SENDTOGRADEBOOK_DESCRIPTION))
		{
			sendGradesToGradebook(grade, gradebookUid);	
		}
		else
		{
			// update isAddToGradeBook of grade
			grade.setAddToGradeBook(false);
			modifyAddToGradeBookStatus(grade.getId(), false);
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean updateGradebook(Grade grade, Forum forum)
	{
		if (grade == null || forum == null)
		{
			throw new IllegalArgumentException("grade or forum is null.");
		}
		
		String gradebookUid = grade.getContext();
		
		if (!jforumGBService.isGradebookDefined(gradebookUid))
		{
			return false;
		}
		
		if (!grade.isAddToGradeBook())
		{
			if (jforumGBService.isExternalAssignmentDefined(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId())))
			{
				jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
			}
			
			if (grade.getItemOpenDate() != null)
			{
				// delete existing grade scheduled that needs to be published to gradebook
				gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
			}
			
			return false;
		}
		
		if (grade.getType() != Grade.GradeType.FORUM.getType())
		{
			return false;
		}
		
		// update gradebook
		String url = null;
		Date dueDate = null;
		Date now = new Date();
		if (forum.getAccessDates() != null && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
		{
			//if (forum.getAccessDates() != null && forum.getAccessDates().getOpenDate() != null && forum.getAccessDates().getOpenDate().after(now) && forum.getAccessDates().isHideUntilOpen())
			if (forum.getAccessDates().getOpenDate() != null)
			{	
				if (forum.getAccessDates().getOpenDate().after(now))
				{
					if (forum.getAccessDates().isHideUntilOpen())
					{
						if (jforumGBService.isExternalAssignmentDefined(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId())))
						{
							jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
						}
						// gradeDao.updateAddToGradeBookStatus(grade.getId(), false);
						
						// existing schedule
						if (grade.getItemOpenDate() != null)
						{
							// delete existing grade schedule that needs to be published to gradebook
							gradeDao.deleteScheduledGradeToGradebook(grade.getId());
						}
						
						// add the grade to gradebook when it opened
						gradeDao.addScheduledGradeToGradebook(grade.getId(), forum.getAccessDates().getOpenDate());
						return false;
					}
					else
					{
						if (grade.getItemOpenDate() != null)
						{
							// delete existing grade schedule that needs to be published to gradebook
							gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
						}
					}
				}
				else
				{
					if (grade.getItemOpenDate() != null)
					{
						// delete existing grade schedule that needs to be published to gradebook
						gradeDao.deleteScheduledGradeToGradebook(grade.getId());					
					}
				}
			}
			else
			{
				if (grade.getItemOpenDate() != null)
				{
					// delete existing grade schedule that needs to be published to gradebook
					gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
				}
			}
			dueDate = forum.getAccessDates().getDueDate();
		}
		else
		{
		
			Category category = forum.getCategory();
			
			//if (category.getAccessDates() != null && category.getAccessDates().getOpenDate() != null && category.getAccessDates().getOpenDate().after(now) && category.getAccessDates().isHideUntilOpen())
			if (category.getAccessDates() != null && category.getAccessDates().getOpenDate() != null)
			{
				if (category.getAccessDates().getOpenDate().after(now))
				{
					if (category.getAccessDates().isHideUntilOpen())
					{
						if (jforumGBService.isExternalAssignmentDefined(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId())))
						{
							jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
						}
						// gradeDao.updateAddToGradeBookStatus(grade.getId(), false);
						
						// existing schedule
						if (grade.getItemOpenDate() != null)
						{
							// delete existing grade schedule that needs to be published to gradebook
							gradeDao.deleteScheduledGradeToGradebook(grade.getId());
						}
						
						// add the grade to gradebook when it opened
						gradeDao.addScheduledGradeToGradebook(grade.getId(), category.getAccessDates().getOpenDate());
						return false;
					}
					else
					{
						if (grade.getItemOpenDate() != null)
						{
							// delete existing grade schedule that needs to be published to gradebook
							gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
						}
					}
				}
				else
				{
					if (grade.getItemOpenDate() != null)
					{
						// delete existing grade schedule that needs to be published to gradebook
						gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
					}
				}
			}
			else
			{
				if (grade.getItemOpenDate() != null)
				{
					// delete existing grade schedule that needs to be published to gradebook
					gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
				}
			}
			
			if (category.getAccessDates() != null && category.getAccessDates().getDueDate() != null || category.getAccessDates().getOpenDate() != null || category.getAccessDates().getAllowUntilDate() != null)
			{
				dueDate = category.getAccessDates().getDueDate();
			}
		}
		
		if (jforumGBService.isExternalAssignmentDefined(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId())))
		{			
			// delete and add again
			jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
		}
		
		// add to gradebook
		if (jforumGBService.addExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()), url, forum.getName(), JForumUtil.toDoubleScore(grade.getPoints()), dueDate, JForumGradeService.GRADE_SENDTOGRADEBOOK_DESCRIPTION))
		{
			sendGradesToGradebook(grade, gradebookUid);	
		}
		else
		{
			// update isAddToGradeBook of grade
			grade.setAddToGradeBook(false);
			modifyAddToGradeBookStatus(grade.getId(), false);
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean updateGradebook(Grade grade, Topic topic)
	{
		if (grade == null || topic == null)
		{
			throw new IllegalArgumentException("grade or topic is null.");
		}
		
		String gradebookUid = grade.getContext();
		if (!jforumGBService.isGradebookDefined(gradebookUid))
		{
			return false;
		}
		
		if (!grade.isAddToGradeBook())
		{
			if (jforumGBService.isExternalAssignmentDefined(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId())))
			{
				jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
			}
			
			if (grade.getItemOpenDate() != null)
			{
				// delete existing grade scheduled that needs to be published to gradebook
				gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
			}
			return false;
		}
		
		if (grade.getType() != Grade.GradeType.TOPIC.getType())
		{
			return false;
		}
		
		Date now = new Date();
		
		// update gradebook
		String url = null;
		Date dueDate = null;
		if (topic.getAccessDates() != null && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null) || (topic.getAccessDates().getAllowUntilDate() != null)))
		{
			if (topic.getAccessDates().getOpenDate() != null)
			{	
				if (topic.getAccessDates().getOpenDate().after(now))
				{
					if (topic.getAccessDates().isHideUntilOpen())
					{
						if (jforumGBService.isExternalAssignmentDefined(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId())))
						{
							jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
						}
						
						// existing schedule
						if (grade.getItemOpenDate() != null)
						{
							// delete existing grade schedule that needs to be published to gradebook
							gradeDao.deleteScheduledGradeToGradebook(grade.getId());
						}
						
						// add the grade to gradebook when it opened
						gradeDao.addScheduledGradeToGradebook(grade.getId(), topic.getAccessDates().getOpenDate());
						return false;
					}
					else
					{
						if (grade.getItemOpenDate() != null)
						{
							// delete existing grade schedule that needs to be published to gradebook
							gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
						}
					}
				}
				else
				{
					if (grade.getItemOpenDate() != null)
					{
						// delete existing grade schedule that needs to be published to gradebook
						gradeDao.deleteScheduledGradeToGradebook(grade.getId());					
					}
				}
			}
			else
			{
				if (grade.getItemOpenDate() != null)
				{
					// delete existing grade schedule that needs to be published to gradebook
					gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
				}
			}
			dueDate = topic.getAccessDates().getDueDate();
		
		}
		else
		{
			Forum forum = topic.getForum();
			
			if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
			{/*
				if (forum.getAccessDates() != null && forum.getAccessDates().getOpenDate() != null && forum.getAccessDates().getOpenDate().after(now) && forum.getAccessDates().isHideUntilOpen())
				{
					jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
					//gradeDao.updateAddToGradeBookStatus(grade.getId(), false);
					
					// existing schedule
					if (grade.getItemOpenDate() != null)
					{
						// delete existing grade schedule that needs to be published to gradebook
						gradeDao.deleteScheduledGradeToGradebook(grade.getId());
					}
					
					// add the grade to gradebook when it opened
					gradeDao.addScheduledGradeToGradebook(grade.getId(), forum.getAccessDates().getOpenDate());
					return false;
				}
				
				dueDate = forum.getAccessDates().getDueDate();
			*/
				if (forum.getAccessDates().getOpenDate() != null)
				{	
					if (forum.getAccessDates().getOpenDate().after(now))
					{
						if (forum.getAccessDates().isHideUntilOpen())
						{
							if (jforumGBService.isExternalAssignmentDefined(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId())))
							{
								jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
							}
							
							// existing schedule
							if (grade.getItemOpenDate() != null)
							{
								// delete existing grade schedule that needs to be published to gradebook
								gradeDao.deleteScheduledGradeToGradebook(grade.getId());
							}
							
							// add the grade to gradebook when it opened
							gradeDao.addScheduledGradeToGradebook(grade.getId(), forum.getAccessDates().getOpenDate());
							return false;
						}
						else
						{
							if (grade.getItemOpenDate() != null)
							{
								// delete existing grade schedule that needs to be published to gradebook
								gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
							}
						}
					}
					else
					{
						if (grade.getItemOpenDate() != null)
						{
							// delete existing grade schedule that needs to be published to gradebook
							gradeDao.deleteScheduledGradeToGradebook(grade.getId());					
						}
					}
				}
				else
				{
					if (grade.getItemOpenDate() != null)
					{
						// delete existing grade schedule that needs to be published to gradebook
						gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
					}
				}
				dueDate = forum.getAccessDates().getDueDate();			
			}
			else
			{
				Category category = forum.getCategory();
				
				/*if (category.getAccessDates() != null && category.getAccessDates().getOpenDate() != null && category.getAccessDates().getOpenDate().after(now) && category.getAccessDates().isHideUntilOpen())
				{
					jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
					//gradeDao.updateAddToGradeBookStatus(grade.getId(), false);
					
					// existing schedule
					if (grade.getItemOpenDate() != null)
					{
						// delete existing grade schedule that needs to be published to gradebook
						gradeDao.deleteScheduledGradeToGradebook(grade.getId());
					}
					
					// add the grade to gradebook when it opened
					gradeDao.addScheduledGradeToGradebook(grade.getId(), category.getAccessDates().getOpenDate());
					return false;
				}
				
				if (category.getAccessDates() != null && category.getAccessDates().getDueDate() != null || (category.getAccessDates().getAllowUntilDate() != null))
				{
					dueDate = category.getAccessDates().getDueDate();
				}*/
				if (category.getAccessDates() != null && category.getAccessDates().getOpenDate() != null)
				{
					if (category.getAccessDates().getOpenDate().after(now))
					{
						if (category.getAccessDates().isHideUntilOpen())
						{
							if (jforumGBService.isExternalAssignmentDefined(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId())))
							{
								jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
							}
							
							// existing schedule
							if (grade.getItemOpenDate() != null)
							{
								// delete existing grade schedule that needs to be published to gradebook
								gradeDao.deleteScheduledGradeToGradebook(grade.getId());
							}
							
							// add the grade to gradebook when it opened
							gradeDao.addScheduledGradeToGradebook(grade.getId(), category.getAccessDates().getOpenDate());
							return false;
						}
						else
						{
							if (grade.getItemOpenDate() != null)
							{
								// delete existing grade schedule that needs to be published to gradebook
								gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
							}
						}
					}
					else
					{
						if (grade.getItemOpenDate() != null)
						{
							// delete existing grade schedule that needs to be published to gradebook
							gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
						}
					}
				}
				else
				{
					if (grade.getItemOpenDate() != null)
					{
						// delete existing grade schedule that needs to be published to gradebook
						gradeDao.deleteScheduledGradeToGradebook(grade.getId());						
					}
				}
				
				if (category.getAccessDates() != null && category.getAccessDates().getDueDate() != null || category.getAccessDates().getOpenDate() != null || category.getAccessDates().getAllowUntilDate() != null)
				{
					dueDate = category.getAccessDates().getDueDate();
				}
			}
		}
		
		
		if (jforumGBService.isExternalAssignmentDefined(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId())))
		{			
			// remove and add again
			jforumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()));
		}
		
		// add to gradebook
		if (jforumGBService.addExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT  + String.valueOf(grade.getId()), url, topic.getTitle(), JForumUtil.toDoubleScore(grade.getPoints()), dueDate, JForumGradeService.GRADE_SENDTOGRADEBOOK_DESCRIPTION))
		{
			sendGradesToGradebook(grade, gradebookUid);	
		}
		else
		{
			// update isAddToGradeBook of grade
			grade.setAddToGradeBook(false);
			modifyAddToGradeBookStatus(grade.getId(), false);
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks for evaluation changes
	 * 
	 * @param exisEval	Existing evaluation
	 * 
	 * @param modEval	Modified evaluation
	 * 
	 * @return	true - if evaluation is modified
	 * 			false - if evaluation is not modified
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
	 * filters the forum group users
	 * 
	 * @param forum	Forum
	 * 
	 * @param evaluations	Evaluations
	 */
	protected void filterForumGroupUsers(Forum forum, List<Evaluation> evaluations)
	{
		if (forum.getAccessType() == Forum.ACCESS_GROUPS)
		{
			if ((forum.getGroups() != null) && (forum.getGroups().size() > 0))
			{
				Site site;
				try
				{
					Category category = forum.getCategory();
					site = siteService.getSite(category.getContext());
				}
				catch (IdUnusedException e)
				{
					return;
				}
				
				// get forum group users
				Collection sakaiSiteGroups = site.getGroups();
				List forumGroupsIds = forum.getGroups();
				List<String> sakaiSiteGroupUserIds = new ArrayList<String>();
				Map<String, Group> sakaiUserGroups = new HashMap<String, Group>();
				for (Iterator i = sakaiSiteGroups.iterator(); i.hasNext();)
				{
					Group group = (Group) i.next();
					
					if (forumGroupsIds.contains(group.getId()))
					{
						Set members = group.getMembers();
						for (Iterator iter = members.iterator(); iter.hasNext();)
						{
							Member member = (Member) iter.next();						
							sakaiSiteGroupUserIds.add(member.getUserId());
							
							sakaiUserGroups.put(member.getUserId(), group);
						}
					}
				}
				
				// show users belong to the forum group
				for (Iterator<Evaluation> i = evaluations.iterator(); i.hasNext();)
				{
					Evaluation evaluation = i.next();
					if (!sakaiSiteGroupUserIds.contains(evaluation.getSakaiUserId()))
					{
						if (evaluation.getId() > 0)
						{
							gradeDao.deleteEvaluation(evaluation.getId());
						}
						i.remove();
					}
					else
					{
						Group group = sakaiUserGroups.get(evaluation.getSakaiUserId());
						((EvaluationImpl)evaluation).setUserSakaiGroupName(group.getTitle());
					}
				}
			}
			else
			{
				for (Iterator<Evaluation> i = evaluations.iterator(); i.hasNext();)
				{
					Evaluation evaluation = i.next();
					if (evaluation.getId() > 0)
					{
						gradeDao.deleteEvaluation(evaluation.getId());
					}
					i.remove();
				}
			}
		}
	}
	
	/**
	 * Gets the grades
	 * 
	 * @param sql	The SQL query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return	The list of grades
	 */
	protected List<Grade> getGrades(String sql, Object[] fields)
	{
		List<Grade> grades = new ArrayList<Grade>();
		
		if (sql != null && fields != null)
		{
			grades.addAll(readGrades(sql.toString(), fields));
		}
		
		return grades;
	}
	
	/**
	 * 
	 * @param sql
	 * @param fields
	 * @return
	 */
	protected List<Grade> readGrades(String sql, Object[] fields)
	{
		final List<Grade> grades = new ArrayList<Grade>();
		
		this.sqlService.dbRead(sql, fields, new SqlReader()
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
					grades.add(grade);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readGrades: " + e, e);
					}
					return null;
				}
			}
		});
		
		return grades;
	}
	
	/**
	 * Send grades to grade book
	 * 
	 * @param grade		Grade
	 * 
	 * @param gradebookUid	Gradebook id
	 */
	protected void sendGradesToGradebook(Grade grade, String gradebookUid)
	{
		if (grade.isAddToGradeBook())
		{
			// send grades to gradebook
			List<Evaluation> evaluations = null;
			evaluations = gradeDao.selectEvaluations(grade);
			
			Map<String, Double> scores = new HashMap<String, Double>();
			for(Evaluation eval: evaluations) 
			{
				if (eval.isReleased())
				{
					String key = eval.getSakaiUserId();
					Float userScore = eval.getScore();
					scores.put(key, (userScore == null) ? null : JForumUtil.toDoubleScore(userScore));
				}
			}
			jforumGBService.updateExternalAssessmentScores(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId()), scores);
		}
	}
	
	/**
	 * sort evaluations for group titles
	 * 
	 * @param evalSort		evalution sort
	 * 
	 * @param evaluations	evalutions
	 */
	protected void sortEvaluationGroups(Evaluation.EvaluationsSort evalSort, List<Evaluation> evaluations)
	{
		if (evalSort == Evaluation.EvaluationsSort.group_title_a)
		{
			Collections.sort(evaluations, new Comparator<Evaluation>()
			{
				public int compare(Evaluation eval1, Evaluation eval2)
				{
					return eval1.getUserSakaiGroupName().compareTo(eval2.getUserSakaiGroupName());
				}
			});
		} 
		else if (evalSort == Evaluation.EvaluationsSort.group_title_d)
		{
			Collections.sort(evaluations, new Comparator<Evaluation>()
			{
				public int compare(Evaluation eval1, Evaluation eval2)
				{
					return -1 * eval1.getUserSakaiGroupName().compareTo(eval2.getUserSakaiGroupName());
				}
			});
		}
	}
	
	/**
	 * Sorts evaluations
	 * 
	 * @param evaluations	Evalutions
	 * 
	 * @param evalSort	Evaluation sort
	 */
	protected void sortEvaluations(List<Evaluation> evaluations, Evaluation.EvaluationsSort evalSort)
	{
		if (evalSort == Evaluation.EvaluationsSort.scores_a)
		{
			Collections.sort(evaluations, new Comparator<Evaluation>()
			{
				/*used 1000.0f for null scores to appear negative values below 1000
				in proper order*/
				public int compare(Evaluation eval1, Evaluation eval2)
				{
					Float f1 = eval1.getScore();
					if (f1 == null) f1 = Float.valueOf(1000.0f);
					Float f2 = eval2.getScore();
					if (f2 == null) f2 = Float.valueOf(1000.0f);
					
					int result = 0;					
					result = f1.compareTo(f2);

					return result;
				}
			});
		} 
		else if (evalSort == Evaluation.EvaluationsSort.scores_d)
		{
			/*used 1000.0f for null scores to appear negative values below 1000
			in proper order*/
			Collections.sort(evaluations, new Comparator<Evaluation>()
			{
				public int compare(Evaluation eval1, Evaluation eval2)
				{
					Float f1 = eval1.getScore();
					if (f1 == null) f1 = Float.valueOf(1000.0f);
					Float f2 = eval2.getScore();
					if (f2 == null) f2 = Float.valueOf(1000.0f);
					
					int result = 0;
					result = -1 * f1.compareTo(f2);

					return result;
				}
			});
		}
	}
	
	/**
	 * Updates the user evaluation entry in the gradebook
	 * 
	 * @param grade	Grade
	 * 
	 * @param evaluation	Evaluation
	 */
	protected void updateGradebookForUser(Grade grade, Evaluation evaluation)
	{
		if (grade == null || !grade.isAddToGradeBook() || evaluation == null) 
		{
			return;		
		}
		
		String gradebookUid = grade.getContext();
		
		if (!jforumGBService.isExternalAssignmentDefined(gradebookUid, "discussions-" + String.valueOf(grade.getId())))
		{
			return;
		}
		
		if ((evaluation.isReleased()) && evaluation.getScore() != null)
		{
			jforumGBService.updateExternalAssessmentScore(gradebookUid, "discussions-"+ String.valueOf(grade.getId()), evaluation.getSakaiUserId(), JForumUtil.toDoubleScore(evaluation.getScore()));
		}
		else
		{
			jforumGBService.updateExternalAssessmentScore(gradebookUid, "discussions-"+ String.valueOf(grade.getId()), evaluation.getSakaiUserId(), null);
		}
	}
}

