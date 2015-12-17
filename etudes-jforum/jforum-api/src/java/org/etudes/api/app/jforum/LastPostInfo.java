/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/LastPostInfo.java $ 
 * $Id: LastPostInfo.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

public interface LastPostInfo
{

	/**
	 * @return the topicId
	 */
	int getTopicId();

	/**
	 * @param topicId the topicId to set
	 */
	void setTopicId(int topicId);

	/**
	 * @return the postId
	 */
	int getPostId();

	/**
	 * @param postId the postId to set
	 */
	void setPostId(int postId);

	/**
	 * @return the userId
	 */
	int getUserId();

	/**
	 * @param userId the userId to set
	 */
	void setUserId(int userId);
	
	/**
	 * @return the sakaiUserId
	 */
	public String getSakaiUserId();

	/**
	 * @return the topicReplies
	 */
	int getTopicReplies();

	/**
	 * @param topicReplies the topicReplies to set
	 */
	void setTopicReplies(int topicReplies);

	/**
	 * @return the postDate
	 */
	Date getPostDate();

	/**
	 * @param postDate the postDate to set
	 */
	void setPostDate(Date postDate);
	
	/**
	 * @return the firstName
	 */
	public String getFirstName();
	
	/**
	 * @return the lastName
	 */
	public String getLastName();
	
	/**
	 * @return the postTimeMillis
	 */
	public long getPostTimeMillis();
	
}