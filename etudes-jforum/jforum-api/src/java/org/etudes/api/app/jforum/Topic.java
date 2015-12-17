/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/Topic.java $ 
 * $Id: Topic.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Represents a topic
 */
public interface Topic
{
	public enum TopicDisplayStatus
	{
		ANNOUNCE, ANNOUNCE_NEW, HOT, HOT_NEW, LOCKED, LOCKED_NEW, NORMAL, NORMAL_NEW, NOT_YET_OPEN, NOT_YET_OPEN_NEW, REUSE, REUSE_NEW, STICKY,	STICKY_NEW;
	}
	
	public enum TopicExportImportCode
	{
		NO(0), YES(1);
		
		private final int exportImportCode;
		
		TopicExportImportCode(int exportImportCode)
		{
			this.exportImportCode = exportImportCode;
		}
		
		final public int getTopicExportImportCode()
		{
			return exportImportCode;
		}
	}
	
	public enum TopicGradableCode
	{
		NO(0), YES(1);
		
		private final int gradable;
		
		TopicGradableCode(int gradable)
		{
			this.gradable = gradable;
		}
		
		final public int getTopicGradableCode()
		{
			return gradable;
		}
	}
	
	public enum TopicStatus
	{
		LOCKED(1), UNLOCKED(0);
		
		private final int status;
		
		TopicStatus(int status)
		{
			this.status = status;
		}
		
		final public int getStatus()
		{
			return status;
		}
	}
	
	public enum TopicType
	{
		ANNOUNCE(3), NORMAL(0), STICKY(2);
		
		private final int type;
		
		TopicType(int type)
		{
			this.type = type;
		}
		
		final public int getType()
		{
			return type;
		}
	}
	
	public static final int STATUS_LOCKED = 1;
	
	// Activitymeter uses this code. After updating activity meter remove below code
	public static final int STATUS_UNLOCKED = 0;
	
	/*public static final int TYPE_NORMAL = 0;
	//public static final int TYPE_TASK = 1;
	public static final int TYPE_STICKY = 2;
	public static final int TYPE_ANNOUNCE = 3;*/
	
	/*public static final int STATUS_UNLOCKED = 0;
	public static final int STATUS_LOCKED = 1;*/
	
	/*public static final int GRADE_NO = 0;
	public static final int GRADE_YES = 1;*/
	
	/*public static final int EXPORT_No = 0;
	public static final int EXPORT_YES = 1;*/
	
	/**
	 * @return the paginate
	 */
	public Boolean getPaginate();

	/**
	 * @return the hasAttach
	 */
	public Boolean hasAttach();

	/**
	 * @return the hot
	 */
	public Boolean isHot();

	/**
	 * @param hasAttach the hasAttach to set
	 */
	public void setHasAttach(Boolean hasAttach);

	/**
	 * @param hot the hot to set
	 */
	public void setHot(Boolean hot);

	/**
	 * @param paginate the paginate to set
	 */
	public void setPaginate(Boolean paginate);

	/**
	 * Gets the access dates
	 * 
	 * @return The accessDates
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
	 * @return the evaluations
	 */
	List<Evaluation> getEvaluations();

	/**
	 * Gets the first post id
	 * 
	 * @return 	The firstPostId
	 */
	int getFirstPostId();

	/**
	 * @return the firstPostTime
	 */
	Date getFirstPostTime();

	/**
	 * @return the forum
	 */
	Forum getForum();

	/**
	 * Gets the forum id
	 * 
	 * @return 	The forumId
	 */
	int getForumId();
	
	/**
	 * @return The grade
	 */
	Grade getGrade();
	
	/**
	 * Gets the topic id
	 * 
	 * @return 	The id
	 */
	int getId();

	/**
	 * @return the lastPostBy
	 */
	User getLastPostBy();

	/**
	 * Gets the last post id
	 * 
	 * @return 	The lastPostId
	 */
	int getLastPostId();

	/**
	 * Gets the lastPostInfo
	 * 
	 * @return The lastPostInfo
	 */
	LastPostInfo getLastPostInfo();

	/**
	 * @return the lastPostTime
	 */
	Date getLastPostTime();
	
	/**
	 * @return the postedBy
	 */
	User getPostedBy();
	
	/**
	 * @return the posts
	 */
	List<Post> getPosts();
	
	/**
	 * @return the read
	 */
	Boolean getRead();

	/**
	 * @return the special access list
	 */
	List<SpecialAccess> getSpecialAccess();
	
	/**
	 * @return the status
	 */
	int getStatus();
	
	/**
	 * Get the time
	 * 
	 * @return	The time
	 */
	Date getTime();
	
	/**
	 * Gets the title
	 * 
	 * @return 	The title
	 */
	String getTitle();
	

	/**
	 * @return the totalReplies
	 */
	int getTotalReplies();
	
	/**
	 * Gets the type
	 * 
	 * @return 	The type
	 */
	int getType();
	
	/**
	 * Get topic display status for icons for the user
	
	 * @return The topic display status
	 */
	TopicDisplayStatus getUserDiplayStatus();
	
	/**
	 * Gets the user post count
	 * 
	 * @return The userPostCount
	 */
	Map<String, Integer> getUserPostCount();
	
	/**
	 * Check if the topic is to export or reuse
	 * 
	 * @return	true - if the topic is to export or reuse
	 * 			false - if the topic is not to export or reuse
	 */
	Boolean isExportTopic();
	
	/**
	 * Check if the topic is gradable
	 * 
	 * @return	true - if the topic is gradable
	 * 			false - if the topic is not gradable
	 */
	Boolean isGradeTopic();
	
	/**
	 * Gets user access to post or reply to the topic
	 * 
	 * @return
	 */
	Boolean mayPost();	

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
	 * Sets the export or reuse status
	 * 
	 * @param exportTopic 	The export or reuse status to set
	 */
	void setExportTopic(Boolean exportTopic);
	
	/**
	 * Sets the first post id
	 * 
	 * @param firstPostId 	The firstPostId to set
	 */
	void setFirstPostId(int firstPostId);

	/**
	 * @param firstPostTime the firstPostTime to set
	 */
	void setFirstPostTime(Date firstPostTime);
	
	/**
	 * Sets the forum id 
	 * 
	 * @param forumId 	The forumId to set
	 */
	void setForumId(int forumId);
	
	/**
	 * @param grade 
	 * 		The grade to set
	 */
	void setGrade(Grade grade);
	
	/**
	 * Sets the topic to gradable 
	 * 
	 * @param gradeTopic 	The gradable status to set
	 */
	void setGradeTopic(Boolean gradeTopic);

	/**
	 * @param lastPostBy the lastPostBy to set
	 */
	void setLastPostBy(User lastPostBy);

	/**
	 * Sets the last post id
	 * 
	 * @param lastPostId 	The lastPostId to set
	 */
	void setLastPostId(int lastPostId);

	/**
	 * Sets the lastPostInfo
	 * 
	 * @param lastPostInfo 
	 * 		The lastPostInfo to set
	 */
	void setLastPostInfo(LastPostInfo lastPostInfo);

	/**
	 * @param lastPostTime the lastPostTime to set
	 */
	void setLastPostTime(Date lastPostTime);

	/**
	 * @param postedBy the postedBy to set
	 */
	void setPostedBy(User postedBy);
	
	/**
	 * @param posts the posts to set
	 */
	void setPosts(List<Post> posts);

	/**
	 * @param read the read to set
	 */
	void setRead(Boolean read);
	
	/**
	 * @param status the status to set
	 */
	void setStatus(int status);

	/**
	 * Sets the time
	 * 
	 * @param time 	The time to set
	 */
	void setTime(Date time);
		
	/**
	 * Sets the title
	 * 
	 * @param title 	The title to set
	 */
	void setTitle(String title);
	
	/**
	 * @param totalReplies the totalReplies to set
	 */
	void setTotalReplies(int totalReplies);
	
	/**
	 * Sets the type
	 * 
	 * @param type 	The type to set
	 */
	void setType(int type);
}
