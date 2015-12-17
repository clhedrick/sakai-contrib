/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/dao/CategoryDao.java $ 
 * $Id: CategoryDao.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.api.app.jforum.dao;

import java.util.List;

import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.JForumCategoryForumsExistingException;

public interface CategoryDao
{
	/**
	 * Creates new category
	 * 
	 * @param category	Category with dates(optional) and grade information(optional)
	 * 
	 * @return	The new category id
	 */
	public int addNew(Category category);
	
	/**
	 * Checks and adds default categories and forums to the site
	 * 
	 * @param courseId	The course or site id
	 */
	public void checkAndAddDefaultCategoriesForums(String courseId);
	
	/**
	 * Deletes the category
	 * 
	 * @param categoryId	The category id
	 * 
	 * @throws JForumCategoryForumsExistingException	If category forums are existing delete forums before deleting category
	 */
	public void delete(int categoryId) throws JForumCategoryForumsExistingException;
	
	/**
	 * Gets the course or site categories and it's forums. Adds default categories and forums if site is not initialized
	 * 
	 * @param courseId	The site or course id
	 * 
	 * @return	The course categories and it's forums
	 */
	public List<Category> selectAllByCourse(String courseId);
	
	/**
	 * Get the category by id
	 * 
	 * @param categoryId	The category id
	 * 
	 * @return	The category if existing else null
	 */
	public Category selectById(int categoryId);
	
	/**
	 * Gets the category forums count
	 * 
	 * @param categoryId	The category id
	 * 
	 * @return	Count of category forums
	 */
	public int selectCategoryForumsCount(int categoryId);
	
	/**
	 * Updates the category
	 * 
	 * @param category	Category
	 */
	public void update(Category category);
	
	/**
	 * Change the order of the category
	 * 
	 * @param category	The category to change the order
	 * 
	 * @param other		The category in the current position
	 * 
	 * @return	The changed category, with the new order set
	 */
	public Category updateOrder(Category category, Category other);
}
