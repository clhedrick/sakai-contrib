/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/dao/GradeDao.java $ 
 * $Id: GradeDao.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.api.app.jforum.dao;

import java.util.Date;
import java.util.List;

import org.etudes.api.app.jforum.Evaluation;
import org.etudes.api.app.jforum.Grade;

public interface GradeDao
{
	/**
	 * Creates grade 
	 * 
	 * @param grade	The grade
	 * 
	 * @return	The newly created grade id
	 */
	public int addNew(Grade grade);
	
	/**
	 * Schedules(adds record) the grade publishing to gradebook
	 *  
	 * @param gradeId	Grade id
	 * 
	 * @param openDate	Open date of the grade book item
	 */
	public void addScheduledGradeToGradebook(int gradeId, Date openDate);
	
	/**
	 * inserts the new evaluations or updates the existing evaluations. Also deletes the evaluations when there is no score and no comments
	 * 
	 * @param grade	Grade
	 * 
	 * @param evaluations
	 */
	public void addUpdateEvaluations(Grade grade, List<Evaluation> evaluations);
	
	/**
	 * Creates or modifies the user evaluation
	 * 
	 * @param evaluation	Evaluation
	 */
	public void addUpdateUserEvaluation(Evaluation evaluation);
	
	/**
	 * Checks if grade publishing to gradebook is scheduled(record existing)
	 * 
	 * @param gradeId	Grade id
	 * 
	 * @return	true - if grade is scheduled to publish to gradebook
	 * 			false - if grade is not scheduled to publish to gradebook 
	 */
	public boolean checkScheduledGradeToGradebook(int gradeId);
	
	/**
	 * Deletes grade
	 * 
	 * @param gradeId	Grade id
	 */
	public void delete(int gradeId);
	
	/**
	 * Deletes forum or topic grade
	 * 
	 * @param forumId	Forum id
	 * 
	 * @param topicId	For forum grade topic id is zero and for topic grade provide topic id
	 */
	public void delete(int forumId, int topicId);
	
	/**
	 * Deletes the category grade
	 * 
	 * @param categoryId	Category id
	 */
	public void deleteCategoryGrade(int categoryId);
	
	/**
	 * Deletes and evaluation
	 * 
	 * @param evaluationId	Evaluation id
	 */
	public void deleteEvaluation(int evaluationId);
	
	/**
	 * Deletes the forum grade
	 * 
	 * @param forumId	Forum id
	 */
	public void deleteForumGrade(int forumId);
	
	/**
	 * Deletes the scheduled grade publishing to gradebook
	 * 
	 * @param gradeId	Grade id
	 */
	public void deleteScheduledGradeToGradebook(int gradeId);	
	
	/**
	 * Gets the category grade
	 * 
	 * @param categoryId	The category id
	 * 
	 * @return 	The category grade if exists or null
	 */
	public Grade selectByCategory(int categoryId);
	
	/**
	 * Gets the forum grade
	 * 
	 * @param forumId	The forum id
	 * 
	 * @return	The forum grade if exists or null
	 */
	public Grade selectByForum(int forumId);
	
	/**
	 * Gets the topic grade
	 * 
	 * @param forumId	The forum id
	 * 
	 * @param topicId 	The topic id
	 * 
	 * @return	The topic grade if exists or null
	 */
	public Grade selectByForumTopic(int forumId, int topicId);
	
	/**
	 * Gets the grades of the site
	 * 
	 * @param siteId	Site id
	 * 
	 * @return	The grades in the site
	 */
	public List<Grade> selectBySite(String siteId);
	
	/**
	 * Gets the grades of the site by grade type
	 * 
	 * @param siteId	Site id
	 * 
	 * @param gradeType	Grade type
	 * 
	 * @return	The grades of the site by grade type
	 */
	public List<Grade> selectBySiteByGradeType(String siteId, int gradeType);
	
	/**
	 * Gets the category evaluations with posts count, late information, and last post time
	 * 
	 * @param categoryId	Category id
	 * 
	 * @param evalSort	Evaluation sort
	 * 
	 * @param all All evaluations or evaluations with posts
	 * 
	 * @return	The category evaluations with posts count, late information, and last post time
	 */
	public List<Evaluation> selectCategoryEvaluationsWithPosts(int categoryId, Evaluation.EvaluationsSort evalSort, boolean all);
	
	/**
	 * Gets the gradable forums count of the category
	 * 
	 * @param categoryId	The category id
	 * 
	 * @return	Count of gradable forums of the category
	 */
	public int selectCategoryGradableForumsCount(int categoryId);
	
	/**
	 * Gets the grade evaluations
	 * 
	 * @param grade	Grade
	 * 
	 * @return	The list of grade evaluations
	 */
	public List<Evaluation> selectEvaluations(Grade grade);
	
	/**
	 * selects the grade evaluations count
	 * 
	 * @param grade	Grade
	 * 
	 * @return	The count of the grade evaluations
	 */
	public int selectEvaluationsCount(Grade grade);
		
	/**
	 * Selects the forum evaluations with posts count
	 * 
	 * @param forumId	Forum id
	 * 
	 * @param evalSort	Evaluation sort
	 * 
	 * @param all All evaluations or evaluations with posts
	 * 
	 * @return	The forum evaluations with posts count
	 */
	public List<Evaluation> selectForumEvaluationsWithPosts(int forumId, Evaluation.EvaluationsSort evalSort, boolean all);
	
	/**
	 * Gets the gradable topic evaluations count of the forum
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return	Count of gradable topic evaluations count of the forum
	 */
	public int selectForumTopicEvaluationsCount(int forumId);
	
	/**
	 * Gets the list of grades associated with categories gradable forums and topics
	 * 
	 * @param categoryId 	The category id
	 * 
	 * @return List of grades associated with forums and topics of the category
	 */
	public List<Grade> selectForumTopicGradesByCategoryId(int categoryId);
	
	/**
	 * Gets the topic grades for a forum
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return	The topic grades for a forum
	 */
	public List<Grade> selectForumTopicGradesByForumId(int forumId);
	
	/**
	 * Gets the grade
	 * 
	 * @param gradeId	Grade id
	 * 
	 * @return	The grade if exits or null
	 */
	public Grade selectGradeById(int gradeId);
	
	/**
	 * Selects the topic evaluations with posts count
	 * 
	 * @param topicId	Topic id
	 * 
	 * @param evalSort	Evaluation sort
	 * 
	 * @param all All evaluations or evaluations with posts
	 * 
	 * @return	The topic evaluations with posts count
	 */
	public List<Evaluation> selectTopicEvaluationsWithPosts(int topicId, Evaluation.EvaluationsSort evalSort, boolean all);
	
	/**
	 * Gets the list of grades associated with forum's gradable topics
	 * 
	 * @param forumId	The forum id
	 * 
	 * @return	List of grades associated with topics of the forum
	 */
	public List<Grade> selectTopicGradesByForumId(int forumId);
	
	/**
	 * Gets the user category evaluation
	 * 
	 * @param categoryId	category id
	 * 
	 * @param userId	User id
	 * 
	 * @return	The user category evaluation or null
	 */
	public Evaluation selectUserCategoryEvaluation(int categoryId, int userId);
	
	/**
	 * Gets the user category evaluation with late posts information
	 * 
	 * @param categoryId	category id
	 * 
	 * @param userId	User id
	 * 
	 * @param checkLatePosts true - Checks for user late posts
	 * 						 false - don't check for user late posts
	 * 
	 * @return	The user category evaluation with late posts information if there are user posts or user evaluated. Or null if there are no posts and user is not evaluated
	 */
	public Evaluation selectUserCategoryEvaluation(int categoryId, int userId, boolean checkLatePosts);
	
	/**
	 * Gets the user forum evaluation
	 *  
	 * @param forumId	Forum id
	 * 
	 * @param userId	User id
	 * 
	 * @return	The user forum evaluation or null
	 */
	public Evaluation selectUserForumEvaluation(int forumId, int userId);
	
	/**
	 * Gets the user forum evaluation with late posts information
	 *  
	 * @param forumId	Forum id
	 * 
	 * @param userId	User id
	 * 
	 * @param checkLatePosts true - Checks for user late posts
	 * 						 false - don't check for user late posts 
	 * 
	 * @return	The user forum evaluation with late posts information if there are user posts or user evaluated. Or null if there are no posts and user is not evaluated
	 */
	public Evaluation selectUserForumEvaluation(int forumId, int userId, boolean checkLatePosts);
	
	/**
	 * Gets the user topic evaluation
	 * 
	 * @param forumId	Forum id
	 * 
	 * @param topicId	Topic id
	 * 
	 * @param userId	User id
	 * 
	 * @return	The user topic evaluation
	 */
	public Evaluation selectUserTopicEvaluation(int forumId, int topicId, int userId);
	
	/**
	 * Gets the user topic evaluation with late posts information
	 * 
	 * @param forumId	Forum id
	 * 
	 * @param topicId	Topic id
	 * 
	 * @param userId	User id
	 * 
	 * @param checkLatePosts true - Checks for user late posts
	 * 						 false - don't check for user late posts
	 * 
	 * @return	The user topic evaluation with late posts information if there are user posts or user evaluated. Or null if there are no posts and user is not evaluated
	 */
	public Evaluation selectUserTopicEvaluation(int forumId, int topicId, int userId, boolean checkLatePosts);
	
	/**
	 * Updates the add to gradebook status of the grade
	 * 
	 * @param gradeId	Grade id
	 * 
	 * @param addToGradeBook	true is grade is to be added to gradebook
	 */
	public void updateAddToGradeBookStatus(int gradeId, boolean addToGradeBook);
	
	/**
	 * Updates category grade
	 * 
	 * @param grade	Grade
	 */
	public void updateCategoryGrade(Grade grade);
	
	/**
	 * Updates forum grade
	 * 
	 * @param grade	Grade
	 */
	public void updateForumGrade(Grade grade);
	
	/**
	 * Updates topic grade
	 * 
	 * @param grade	Grade
	 */
	public void updateTopicGrade(Grade grade);
	
	/**
	 * Update user review date
	 * 
	 * @param evaluationId	Evaluation id
	 */
	public void updateUserReviewedDate(int evaluationId);
}
