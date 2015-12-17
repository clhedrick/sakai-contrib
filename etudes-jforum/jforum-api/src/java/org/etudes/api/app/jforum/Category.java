/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/Category.java $ 
 * $Id: Category.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
import java.util.Map;


/**
 * Represents Category
 */
public interface Category
{	
	/**
	 * Get the category access dates
	 * 
	 * @return	The category access dates
	 */
	AccessDates getAccessDates();
	
	/**
	 * @return the blocked
	 */
	Boolean getBlocked();
		
	/**
	 * @return the blockedByDetails
	 */
	String getBlockedByDetails();
	
	/**
	 * @return the blockedByTitle
	 */
	String getBlockedByTitle();
	
	/**
	 * Gets the category context
	 * 
	 * @return	The category's context
	 */
	String getContext();
	
	/**
	 * @return the createdBy
	 */
	String getCreatedBySakaiUserId();
	
	/**
	 * Gets the evaluations
	 * 
	 * @return The evaluations
	 */
	List<Evaluation> getEvaluations();
	
	/**
	 * Get the category forums
	 * 
	 * @return
	 * 		The category forums
	 */
	List<Forum> getForums();
	
	/**
	 * @return The grade
	 */
	Grade getGrade();
	
	/**
	 * Gets the category id
	 * 
	 * @return	The category id
	 */
	int getId();
	
	/**
	 * Gets the lastPostInfo
	 * 
	 * @return The lastPostInfo
	 */
	LastPostInfo getLastPostInfo();
	
	/**
	 * @return the modifiedBySakaiUserId
	 */
	String getModifiedBySakaiUserId();
	
	/**
	 * Gets the display order
	 * 
	 * @return the order
	 */
	int getOrder();
	
	/**
	 * Gets the category title
	 * 
	 * @return	The category title
	 */
	String getTitle();
	

	/**
	 * Gets the users post count
	 * 
	 * @return The map of userid as key with post count as value
	 */
	Map<String, Integer> getUserPostCount();
	
	/**
	 * Check if the category is gradable
	 * 
	 * @return	true - if the category is gradable
	 * 			false - if the category is not gradable
	 */
	Boolean isGradable();

	/**
	 * Sets the access dates
	 * 
	 * @param accessDates 	The accessDates to set
	 */
	void setAccessDates(AccessDates accessDates);
	
	/**
	 * @param blocked the blocked to set
	 */
	void setBlocked(Boolean blocked);
	
	/**
	 * @param blockedByDetails the blockedByDetails to set
	 */
	void setBlockedByDetails(String blockedByDetails);
	
	/**
	 * @param blockedByTitle the blockedByTitle to set
	 */
	void setBlockedByTitle(String blockedByTitle);

	/**
	 * Sets the context
	 * 
	 * @param context	The context to set
	 */
	void setContext(String context);

	/**
	 * @param createdBySakaiUserId the createdBySakaiUserId to set
	 */
	void setCreatedBySakaiUserId(String createdBySakaiUserId);
	
	/**
	 * Sets the gradable
	 * 
	 * @param gradable 	The gradable to set
	 */
	void setGradable(Boolean gradable);
	
	/**
	 * @param grade 
	 * 		The grade to set
	 */
	void setGrade(Grade grade);
	
	/**
	 * Sets the lastPostInfo
	 * 
	 * @param lastPostInfo 
	 * 		The lastPostInfo to set
	 */
	void setLastPostInfo(LastPostInfo lastPostInfo);

	/**
	 * @param modifiedBySakaiUserId the modifiedBySakaiUserId to set
	 */
	void setModifiedBySakaiUserId(String modifiedBySakaiUserId);
	
	/**
	 * Sets the display order
	 * 
	 * @param order the order to set
	 */
	void setOrder(int order);

	/**
	 * Sets the title
	 * 
	 * @param title 	The title to set
	 */
	void setTitle(String title);
}