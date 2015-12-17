/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/Grade.java $ 
 * $Id: Grade.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.util.Date;

/**
 * Represents Grade.
 */
public interface Grade
{
	public enum GradeType
	{
		CATEGORY(3), DISABLED(0), FORUM(2), TOPIC(1);
		
		private final int type;
		
		GradeType(int type)
		{
			this.type = type;
		}
		
		final public int getType()
		{
			return type;
		}
	}
	public static final int GRADE_BY_CATEGORY = 3;
	public static final int GRADE_BY_FORUM = 2;
	public static final int GRADE_BY_TOPIC = 1;
	
	public static final int GRADE_DISABLED = 0;
	
	public static final String GradeSendToGradebookDescription = "Discussions";

	/**
	 * Gets the category id
	 * 
	 * @return the categoryId
	 */
	int getCategoryId();

	/**
	 * Gets the grade context
	 * 
	 * @return the context
	 */
	String getContext();

	/**
	 * Gets the forum id
	 * 
	 * @return the forumId
	 */
	int getForumId();

	/**
	 * Gets the grade id
	 * 
	 * @return the id
	 */
	int getId();

	/**
	 * Gets the minimum posts
	 * 
	 * @return The minimumPosts
	 */
	int getMinimumPosts();

	/**
	 * Gets the grade points
	 * 
	 * @return the points
	 */
	Float getPoints();

	/**
	 * Gets the topic id
	 * 
	 * @return the topicId
	 */
	int getTopicId();

	/**
	 * Sets the grade id
	 * 
	 * @param id 	The id to set
	 */
	//void setId(int id);

	/**
	 * Gets the grade type
	 * 
	 * @return the type
	 */
	int getType();

	/**
	 * Gets the "add to gradebook" status
	 * 
	 * @return	true - if added to gradebook
	 * 			false - if not added to gradebook
	 */
	Boolean isAddToGradeBook();

	/**
	 * @return the minimumPostsRequired
	 */
	Boolean isMinimumPostsRequired();

	/**
	 * Sets the "add to gradebook" status
	 * 
	 * @param addToGradeBook 	The addToGradeBook to set
	 */
	void setAddToGradeBook(Boolean addToGradeBook);

	/**
	 * Sets the category id
	 * 
	 * @param categoryId 	The categoryId to set
	 */
	void setCategoryId(int categoryId);

	/**
	 * Sets the context
	 * 
	 * @param context 	The context to set
	 */
	void setContext(String context);

	/**
	 * Sets the forum id
	 * 
	 * @param forumId 	The forumId to set
	 */
	void setForumId(int forumId);

	/**
	 * @param minimumPosts The minimumPosts to set
	 */
	void setMinimumPosts(int minimumPosts);
	
	/**
	 * @param minimumPostsRequired the minimumPostsRequired to set
	 */
	void setMinimumPostsRequired(Boolean minimumPostsRequired);

	/**
	 * Sets the grade points
	 * 
	 * @param points 	The points to set
	 */
	void setPoints(Float points);
	
	/**
	 * Sets the topic id
	 * 
	 * @param topicId 	The topicId to set
	 */
	void setTopicId(int topicId);

	/**
	 * Sets the grade type
	 * 
	 * @param gradeType 	The gradeType to set
	 */
	void setType(int type);
	
	/**
	 * @return the itemOpenDate
	 */
	Date getItemOpenDate();
}