/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/JForumService.java $ 
 * $Id: JForumService.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.api.app.jforum;

import java.util.List;
import java.util.Map;

public interface JForumService
{
	public static final String SERVLET_EXTENSION = ".page";
	public enum Type { CATEGORY, FORUM, TOPIC }
	
	public enum CMItemIdPrefix
	{
		CAT("CAT-"), FORUM("FORUM-"), TOPIC("TOPIC-");
		
		private final String itemIdPrefix;
		
		CMItemIdPrefix(String itemIdPrefix)
		{
			this.itemIdPrefix = itemIdPrefix;
		}
		
		final public String getCMItemIdPrefix()
		{
			return itemIdPrefix;
		}
	}

	/**
	 * gets the gradable items of the site for the user 
	 * 
	 * @param context	Context
	 * 	
	 * @param userId	User id
	 * 
	 * @return	List of jforum gradable items for the user 
	 */
	public List<Category> getUserGradableItemsByContext(String context, String userId);
	
	/**
	 * gets the gradable items of the site for the user but not deny access forums and it's topics
	 * 
	 * @param context	Context
	 * 	
	 * @param userId	User id
	 * 
	 * @return	List of jforum gradable items for the user but not deny access forums and it's topics
	 */
	public List<Category> getUserFilteredGradableItemsByContext(String context, String userId);
	
	/**
	 * gets the gradable items of the site
	 * 
	 * @param context	Context
	 * 
	 * @return	List of gradable items of the site
	 */
	public List<Category> getGradableItemsByContext(String context);
	
	/**
	 * Gets the category
	 * 
	 * @param categoryId	The category id
	 * 
	 * @return	The Category object or null if the category doesn't exist
	 */
	public Category getCategory(int categoryId);
	
	/**
	 * Gets the category with forum
	 * 
	 * @param forumId	The forum id
	 * 
	 * @return	The the category with forum or null if the forum doesn't exist
	 */
	public Category getCategoryForum(int forumId);

	/**
	 * Gets the category with forum and topic
	 * 
	 * @param topicId	The topic id
	 * 
	 * @return The category with forum and topic or null if the topic doesn't exist
	 */
	public Category getCategoryForumTopic(int topicId);
	
	/**
	 * Updates category dates
	 * 
	 * @param category	The category 
	 */
	public void updateCategoryDates(Category category);
	
	/**
	 * Updates forum dates
	 * 
	 * @param category	The category with forum whose dates need to be updated
	 */
	public void updateForumDates(Category category);
	
	/**
	 * Updates topic dates
	 * 
	 * @param category	The category with forum and topic whose dates need to be updated
	 */
	public void updateTopicDates(Category category);
	
	/**
	 * Checks the coursemap item access
	 * 
	 * @param context	The context
	 * 
	 * @param id		The item id
	 * 
	 * @param userId	The user id
	 * 
	 * @return		True - if user has access to the item
	 * 				False - if user has no access to the item
	 */
	public Boolean checkItemAccess(String context, String id, String userId);
	
	/**
	 * Gets the item no access message
	 * 
	 * @param context	The context
	 * 
	 * @param id		The item id
	 * 
	 * @param userId	The user id
	 * 
	 * @return		The message
	 */
	public String getItemAccessMessage(String context, String id, String userId);
	
	/**
	 * Gets the item no access details
	 * 
	  * @param context	The context
	 * 
	 * @param id		The item id
	 * 
	 * @param userId	The user id
	 * 
	 * @return		The details
	 */
	public String getItemAccessDetails(String context, String id, String userId);
	
	/**
	 * Gets the total posts for gradable items per user 
	 * 
	 * @param context	The context
	 * 
	 * @return	The map with userid as key and posts count as value
	 */
	public Map<String, Integer> getContextGradableItemsPostsCount(String context);
	
	/**
	 * Gets the total posts per user in the site 
	 * 
	 * @param context	The context
	 * 
	 * @return	The map with userid as key and posts count as value
	 */
	public Map<String, Integer> getContextUserPostsCount(String context);
	
	/**
	 * Gets the user gradable items with user posts count
	 * 
	 * @param context	The context
	 * 
	 * @return	List of gradable items with user posts count
	 */
	public List<Category> getGradableItemsUserPostsCountByContext(String context);
	
	/**
	 * Gets the post count per user for the category
	 * 
	 * @param context	The context
	 * 
	 * @param categoryId	The category id
	 * 
	 * @return Category with the post count per user
	 */
	public Category getUsersPostCountByCategory(String context, int categoryId);
	
	/**
	 * Gets the post count per user for the forum
	 * 
	 * @param context	The context
	 * 
	 * @param forumId	The forum id
	 * 
	 * @return Category with the post count per user for the forum
	 */
	public Category getUsersPostCountByForum(String context, int forumId);
	
	/**
	 * Gets the post count per user for the topic
	 * 
	 * @param context	The context
	 * 
	 * @param topicId	The topic id
	 * 
	 * @return Category with the post count per user for the topic
	 */
	public Category getUsersPostCountByTopic(String context, int topicId);
	
	/**
	 * Get gradable category evalautions
	 * 
	 * @param context	The context
	 * 
	 * @param categoryId	Category id
	 * 
	 * @return	List of gradable category evaluations
	 */
	public Category getEvaluationsByCategory(String context, int categoryId);
	
	/**
	 * Get gradable forum evalautions
	 * 
	 * @param context	The context
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return	List of gradable forum evaluations
	 */
	public Category getEvaluationsByForum(String context, int forumId);
	
	/**
	 * Get gradable topic evalautions
	 * 
	 * @param context	The context
	 * 
	 * @param topicId	Topic id
	 * 
	 * @return	List of gradable topic evaluations
	 */
	public Category getEvaluationsByTopic(String context, int topicId);
}
