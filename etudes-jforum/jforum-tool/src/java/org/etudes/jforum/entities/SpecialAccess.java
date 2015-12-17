/*********************************************************************************** 
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/entities/SpecialAccess.java $ 
 * $Id: SpecialAccess.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
 * 
 **********************************************************************************/ 
package org.etudes.jforum.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpecialAccess implements Serializable
{
	private int id;
	private int forumId;
	private int topicId;
	private Date startDate;
	private Date endDate;
	private Date allowUntilDate;
	private String startDateFormatted;
	private String endDateFormatted;
	private Boolean overrideStartDate = false;
	private Boolean overrideEndDate = false;
	private Boolean overrideLockEndDate = false;
	private Boolean overrideAllowUntilDate = false;
	private boolean lockOnEndDate = false;
	protected List<Integer> userIds = new ArrayList<Integer>();
	protected List<User> users = new ArrayList<User>();
 
	public SpecialAccess() {}
	
	public SpecialAccess(SpecialAccess specialAccess) 
	{
		this.id = specialAccess.id;
		this.forumId = specialAccess.getForumId();
		this.topicId = specialAccess.getTopicId();
		this.startDate = specialAccess.getStartDate();
		this.endDate = specialAccess.getEndDate();
		this.startDateFormatted = specialAccess.getStartDateFormatted();
		this.endDateFormatted = specialAccess.getEndDateFormatted();
		this.lockOnEndDate = specialAccess.isLockOnEndDate();
		this.overrideStartDate = specialAccess.isOverrideStartDate();
		this.overrideEndDate = specialAccess.isOverrideEndDate();
		this.overrideLockEndDate = specialAccess.isOverrideLockEndDate();
		this.userIds = specialAccess.getUserIds();
		this.users = specialAccess.getUsers();
		this.allowUntilDate = specialAccess.getAllowUntilDate();
		this.overrideAllowUntilDate = specialAccess.isOverrideAllowUntilDate();
	}
		
	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * @return the forumId
	 */
	public int getForumId()
	{
		return forumId;
	}

	/**
	 * @param forumId the forumId to set
	 */
	public void setForumId(int forumId)
	{
		this.forumId = forumId;
	}

	/**
	 * @param topicId the topicId to set
	 */
	public void setTopicId(int topicId)
	{
		this.topicId = topicId;
	}

	/**
	 * @return the topicId
	 */
	public int getTopicId()
	{
		return topicId;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return the startDateFormatted
	 */
	public String getStartDateFormatted()
	{
		return startDateFormatted;
	}

	/**
	 * @param startDateFormatted the startDateFormatted to set
	 */
	public void setStartDateFormatted(String startDateFormatted)
	{
		this.startDateFormatted = startDateFormatted;
	}

	/**
	 * @return the endDateFormatted
	 */
	public String getEndDateFormatted()
	{
		return endDateFormatted;
	}

	/**
	 * @param endDateFormatted the endDateFormatted to set
	 */
	public void setEndDateFormatted(String endDateFormatted)
	{
		this.endDateFormatted = endDateFormatted;
	}

	/**
	 * @return the overrideStartDate
	 */
	public Boolean isOverrideStartDate()
	{
		return overrideStartDate;
	}

	/**
	 * @param overrideStartDate the overrideStartDate to set
	 */
	public void setOverrideStartDate(Boolean overrideStartDate)
	{
		this.overrideStartDate = overrideStartDate;
	}

	/**
	 * @return the overrideEndDate
	 */
	public Boolean isOverrideEndDate()
	{
		return overrideEndDate;
	}

	/**
	 * @param overrideEndDate the overrideEndDate to set
	 */
	public void setOverrideEndDate(Boolean overrideEndDate)
	{
		this.overrideEndDate = overrideEndDate;
	}

	/**
	 * @return the overrideLockEndDate
	 */
	@Deprecated
	public Boolean isOverrideLockEndDate()
	{
		return overrideLockEndDate;
	}

	/**
	 * @param overrideLockEndDate the overrideLockEndDate to set
	 */
	@Deprecated
	public void setOverrideLockEndDate(Boolean overrideLockEndDate)
	{
		this.overrideLockEndDate = overrideLockEndDate;
	}

	/**
	 * @return the userIds
	 */
	public List<Integer> getUserIds()
	{
		return userIds;
	}

	/**
	 * @param userIds the userIds to set
	 */
	public void setUserIds(List<Integer> userIds)
	{
		this.userIds = userIds;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers()
	{
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users)
	{
		this.users = users;
	}

	/**
	 * @return the lockOnEndDate
	 */
	@Deprecated
	public boolean isLockOnEndDate()
	{
		return lockOnEndDate;
	}

	/**
	 * @param lockOnEndDate the lockOnEndDate to set
	 */
	public void setLockOnEndDate(boolean lockOnEndDate)
	{
		this.lockOnEndDate = lockOnEndDate;
	}

	/**
	 * @return the allowUntilDate
	 */
	public Date getAllowUntilDate()
	{
		return allowUntilDate;
	}

	/**
	 * @param allowUntilDate the allowUntilDate to set
	 */
	public void setAllowUntilDate(Date allowUntilDate)
	{
		this.allowUntilDate = allowUntilDate;
	}

	/**
	 * @return the overrideAllowUntilDate
	 */
	public Boolean isOverrideAllowUntilDate()
	{
		return overrideAllowUntilDate;
	}

	/**
	 * @param overrideAllowUntilDate the overrideAllowUntilDate to set
	 */
	public void setOverrideAllowUntilDate(Boolean overrideAllowUntilDate)
	{
		this.overrideAllowUntilDate = overrideAllowUntilDate;
	}	
}
