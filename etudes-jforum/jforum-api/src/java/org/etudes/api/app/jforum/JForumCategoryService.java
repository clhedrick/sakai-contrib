/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/JForumCategoryService.java $ 
 * $Id: JForumCategoryService.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.api.app.jforum;

import java.util.List;

import org.etudes.api.app.jforum.JForumService.Type;

public interface JForumCategoryService
{
	/**
	 * Checks and adds context default category forums
	 * 
	 * @param context	Context
	 * 
	 * @param sakaiUserId	Sakai user id
	 * 
	 * @throws JForumAccessException	If user has no access to site
	 */
	public void checkAndAddContextDefaultCategoriesForums(String context, String sakaiUserId) throws JForumAccessException;
	
	/**
	 * Creates a new category
	 * 
	 * @param category	Category with dates(optional) and grade information(optional)
	 * 
	 * @return	New category id

	 * @throws JForumAccessException If created by user has no permission to create category
	 */
	public int createCategory(Category category) throws JForumAccessException;
	
	/**
	 * Deletes the category
	 * 
	 * @param categoryId	Category id
	 * 
	 * @param sakaiUserId	Sakai user id who is deleting the category
	 * 
	 * @throws JForumAccessException	If user has no access to delete the category
	 * 
	 * @throws JForumCategoryForumsExistingException	If forums are existing then category cannot be deleted
	 * 
	 * @throws JForumItemEvaluatedException	If category is evaluated the category cannot be deleted
	 */
	public void deleteCategory(int categoryId, String sakaiUserId) throws JForumAccessException, JForumCategoryForumsExistingException, JForumItemEvaluatedException;
	
	/**
	 * Evaluates category
	 * 
	 * @param category	Category with grade and evaluations
	 */
	public void evaluateCategory(Category category) throws JForumAccessException;;

	/**
	 * Gets the category
	 * 
	 * @param categoryId	The category id
	 * 
	 * @return	The Category object or null if the category doesn't exist
	 */
	public Category getCategory(int categoryId);
	
	/**
	 * Gets the user category
	 * 
	 * @param categoryId	The category id
	 * 
	 * @param sakaiUserId	The sakai user id
	 * 
	 * @return	The Category object or null if the category doesn't exist
	 * 
	 * @throws JForumAccessException	If user has no access privileges to the category
	 */
	public Category getCategory(int categoryId, String sakaiUserId) throws JForumAccessException;
	
	/**
	 * Gets the category evaluations
	 * 
	 * @param context		The context
	 * 
	 * @param categoryId	The category id
	 * 
	 * @return	The category with evaluations
	 */
	public Category getCategoryEvaluations(String context, int categoryId);
	
	/**
	 * Gets the forum and it's category
	 * 
	 * @param forumId	The forum id
	 * 
	 * @return The forum and it's category
	 */
	public Category getCategoryForum(int forumId);
	
	/**
	 * Gets the context categories
	 * 
	 * @param context	The context
	 * 
	 * @return List of the context categories
	 */
	//public List<Category> getContextCategories(String context);
	
	/**
	 * Gets the topic and it's category and forum
	 * 
	 * @param topicId	The topic id
	 * 
	 * @return	The topic and it's category and forum
	 */
	public Category getCategoryForumTopic(int topicId);
	
	/**
	 * Gets the category post count per user
	 * 
	 * @param context	The context
	 * 
	 * @param categoryId	The category id
	 * 
	 * @return The category with user post count
	 */
	public Category getCategoryUsersPostCount(String context, int categoryId);

	/**
	 * Gets the list of gradable categories, forums, topics
	 * 
	 * @param context	The context
	 * 
	 * @return List of gradable categories, forums, topics
	 */
	public List<Category> getContextGradableCategoriesForumsTopics(String context);
	
	/**
	 * Get the post count per user for gradable categories, forums and topics 
	 * 
	 * @param context	The context
	 * 
	 * @return	List of gradable categories, forums and topics with users post count
	 */
	public List<Category> getContextGradableCategoriesForumsTopicsUsersPostCount(String context);
	
	/**
	 * Gets the list of gradable categories, forums, topics but not denied access forums and its topics
	 * 
	 * @param context	The context
	 * 
	 * @return List of gradable categories, forums, topics but not denied access forums and its topics
	 */
	public List<Category> getFilteredContextGradableCategoriesForumsTopics(String context);
	
	/**
	 * Gets the posts of a topic with it's category, forum and topic
	 * 
	 * @param topicId	The topic id
	 * 
	 * @return List of posts of a topic with it's category, forum and topic
	 */
	//public Category getCategoryForumTopicPosts(int topicId);
	
	/**
	 * Gets the forum evaluations
	 * 
	 * @param context	The context
	 * 
	 * @param forumId	The forum id
	 * 
	 * @return	The category with forum evaluations
	 */
	public Category getForumEvaluations(String context, int forumId);
	
	/**
	 * Gets the category post count per user for the forum
	 * 
	 * @param context	The context
	 * 
	 * @param forumId	The forum id
	 * 
	 * @return The category with forum user post count
	 */
	public Category getForumUsersPostCount(String context, int forumId);

	/**
	 * Gets all the categories and forums of the site. Only instructors can access all the categories and forums
	 * 
	 * @param context	Contest or site id
	 * 
	 * @param userId	Sakai user id
	 * 
	 * @return	The list of the categories and forums of the site
	 */
	public List<Category> getManageCategories(String context, String userId);
	
	/**
	 * Gets the topic evaluations
	 * 
	 * @param context	The context
	 * 
	 * @param topicId	The topic id
	 * 
	 * @return	The topic with it's category and forum with evaluations
	 */
	public Category getTopicEvaluations(String context, int topicId);
	
	/**
	 * Gets the category post count per user for the topic
	 * 
	 * @param context	The context
	 * 
	 * @param topicId	The topic id
	 * 
	 * @return The category with topic user post count
	 */
	public Category getTopicUsersPostCount(String context, int topicId);
	
	/**
	 * Gets the user category forums
	 * 
	 * @param categoryId	The category id
	 * 
	 * @return Gets category and forums that user have access to
	 */
	public Category getUserCategoryForums(int categoryId, String userId);
	
	/**
	 * Gets the posts of a topic with it's category, forum and topic for the user limiting the number of records
	 * 
	 * @param topicId		The topic id
	 * 
	 * @param userId		Sakai user id
	 * 
	 * @param startFrom		Starting row
	 * 
	 * @param count			Number of records
	 * 
	 * @return	category with forum, topic and posts for the user limiting the number of records
	 */
	public Category getUserCategoryForumTopicPosts(int topicId, String userId, int startFrom, int count);
	
	/**
	 * Gets the topics of a forum with it's category and forum for the user
	 * 
	 * @param forumId	The forum id
	 * 	
	 * @param userId	Sakai user id
	 * 
	 * @return List of topics of a forum with it's category
	 */
	public Category getUserCategoryForumTopics(int forumId, String userId);
	
	/**
	 * Gets the topics of a forum with it's category and forum for the user with limited records
	 * 
	 * @param forumId	The forum id
	 * 
	 * @param userId	Jforum user id
	 * 
	 * @param startFrom	Starting row
	 * 
	 * @param count		Number of records
	 * 
	 * @return	The topics of a forum with it's category and forum for the user with limited records
	 */
	public Category getUserCategoryForumTopicsByLimit(int forumId, String userId, int startFrom, int count);
	
	
	/***
	 * Gets the user context categories and it's forums
	 * 
	 * @param context	The context or course id
	 * 
	 * @param userId	The sakai user id
	 * 
	 * @return	List of the context categories user has access to
	 */
	public List<Category> getUserContextCategories(String context, String userId);
	
	/**
	 * Checks for the unread posts
	 * 
	 * @param context	Site id
	 * 
	 * @param sakaiUserId	Sakai user id
	 * 
	 * @return true - if user has unread posts
	 * 		   false - if user has no unread posts
	 */
	public boolean isUserHasUnreadTopicsAndReplies(String context, String sakaiUserId);
	
	/**
	 * Modifies the category
	 * 
	 * @param category	Category
	 * 
	 * @throws JForumAccessException	If the modifying user has no access to update the category
	 * 
	 * @throws JForumGradesModificationException	If the category grade is modified and gradable category is evaluated or category forums are gradable
	 */
	public void modifyCategory(Category category) throws JForumAccessException, JForumGradesModificationException;
	
	/**
	 * Updates the category order
	 * 
	 * @param categoryId		Category id that is to be reordered
	 * 
	 * @param positionToMove	new position to move
	 * 
	 * @param sakaiUserId		User who is updating the forum order
	 * 
	 * @throws JForumAccessException	If user has no access to update the category order
	 */
	public void modifyOrder(int categoryId, int positionToMove, String sakaiUserId) throws JForumAccessException;
	
	/**
	 * Creates new instance of category object
	 * 
	 * @return	The Category object
	 */
	public Category newCategory();
	
	/**
	 * Create new instance of grade object
	 * 
	 * @param category		The category object
	 * 
	 * @return	The grade object
	 */
	public Grade newGrade(Category category);
	
	/**
	 * Updates the dates of the category if changed
	 * 
	 * @param category	Category
	 */
	public void updateDates(Category category, Type type);
}
