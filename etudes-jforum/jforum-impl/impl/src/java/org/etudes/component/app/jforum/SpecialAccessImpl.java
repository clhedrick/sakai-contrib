/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/SpecialAccessImpl.java $ 
 * $Id: SpecialAccessImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.etudes.api.app.jforum.AccessDates;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.Topic;
import org.etudes.api.app.jforum.User;

public class SpecialAccessImpl implements SpecialAccess
{
	protected AccessDates accessDates = null;
	
	protected int forumId;
	
	protected int id;
	
	@Deprecated
	protected Boolean lockOnEndDate = Boolean.FALSE;
	
	protected Boolean overrideAllowUntilDate = Boolean.FALSE;
	
	protected Boolean overrideEndDate = Boolean.FALSE;
	
	protected Boolean overrideHideUntilOpen = Boolean.FALSE;
	
	@Deprecated
	protected Boolean overrideLockEndDate = Boolean.FALSE;
	
	protected Boolean overrideStartDate = Boolean.FALSE;
	
	protected int topicId;
	
	protected List<Integer> userIds = new ArrayList<Integer>();
	
	protected List<User> users = new ArrayList<User>();

	public SpecialAccessImpl() {}

	protected SpecialAccessImpl(SpecialAccessImpl other)
	{
		this.id = other.id;
		this.forumId = other.forumId;
		this.topicId = other.topicId;
		this.accessDates = new AccessDatesImpl((AccessDatesImpl)other.accessDates);
		this.overrideStartDate = other.overrideStartDate;
		this.overrideEndDate = other.overrideEndDate;
		this.overrideLockEndDate = other.overrideLockEndDate;
		this.overrideHideUntilOpen = other.overrideHideUntilOpen;
		this.overrideAllowUntilDate = other.overrideAllowUntilDate;
		this.userIds.addAll(other.userIds);
		
		for(User user : other.users)
		{
			this.users.add(new UserImpl((UserImpl)user));
		}
	}

	/** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		// two SpecialAccess are equals if they have the same id
		return ((obj instanceof SpecialAccess) && (((SpecialAccess)obj).getId() == this.id));
	}

	/**
	 * {@inheritDoc}
	 */
	public AccessDates getAccessDates()
	{
		return this.accessDates;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getForumId()
	{
		return forumId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTopicId()
	{
		return topicId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Integer> getUserIds()
	{
		return userIds;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<User> getUsers()
	{
		return users;
	}
	
	/** 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isForumSpecialAccessDatesValid(Forum forum)
	{
		
		if (forum == null || forum.getAccessDates() == null || this == null || this.getAccessDates() == null)
		{
			throw new IllegalArgumentException("Special access or forum data is missing.");
		}
		
		// check the special access belongs to the forum
		if (forum.getId() != this.getForumId() && (this.getTopicId() > 0))
		{
			throw new IllegalArgumentException("Forum is not related to this special access.");
		}
		
		Date openDate = null, dueDate = null, allowUntilDate = null;
		
		if (this.isOverrideStartDate())
		{
			openDate = this.getAccessDates().getOpenDate();
		}
		else
		{
			openDate = forum.getAccessDates().getOpenDate();
		}
	
		if (this.isOverrideEndDate())
		{
			dueDate = this.getAccessDates().getDueDate();
		}
		else
		{
			dueDate = forum.getAccessDates().getDueDate();
		}
		
		if (this.isOverrideAllowUntilDate())
		{
			allowUntilDate = this.getAccessDates().getAllowUntilDate();
		}
		else
		{
			allowUntilDate = forum.getAccessDates().getAllowUntilDate();
		}
		
				
		boolean blnOpenDate = false, blnDueDate = false, blnAllowUntilDate = false;
		
		if (openDate != null) 
	 	{
			blnOpenDate = true;
	 	}
		
	 	if (dueDate != null) 
	 	{
	 		blnDueDate = true;
	 	}
	 	
	 	if (allowUntilDate != null) 
	 	{
	 		blnAllowUntilDate = true;
	 	}
	 	
	 	if (blnOpenDate && blnDueDate && blnAllowUntilDate)
	 	{
		 	if ((openDate.after(dueDate)) || (openDate.after(allowUntilDate)) || (dueDate.after(allowUntilDate))) 
		 	{
		   		return false;
		 	}
	 	}
	 	else if (blnOpenDate && blnDueDate && !blnAllowUntilDate)
		{
	 		if (openDate.after(dueDate)) 
		 	{
		   		
		   		return false;
		 	}
		}
	 	else if (!blnOpenDate && blnDueDate && blnAllowUntilDate)
		{
	 		if (dueDate.after(allowUntilDate)) 
		 	{
		   		return false;
		 	}
		}
	 	else if (blnOpenDate && !blnDueDate && blnAllowUntilDate)
	 	{
		 	if (openDate.after(allowUntilDate)) 
		 	{
		   		return false;
		 	}
	 	}

	 	return true;
	}
	
	public boolean isTopicSpecialAccessDatesValid(Topic topic)
	{
		
		if (topic == null || topic.getAccessDates() == null || this == null || this.getAccessDates() == null)
		{
			throw new IllegalArgumentException("Special access or topic data is missing.");
		}
		
		// check the special access belongs to the topic
		if (topic.getId() != this.getTopicId() || topic.getForumId() != this.getForumId())
		{
			throw new IllegalArgumentException("Forum is not related to this special access.");
		}
		
		Date openDate = null, dueDate = null, allowUntilDate = null;
		
		if (this.isOverrideStartDate())
		{
			openDate = this.getAccessDates().getOpenDate();
		}
		else
		{
			openDate = topic.getAccessDates().getOpenDate();
		}
	
		if (this.isOverrideEndDate())
		{
			dueDate = this.getAccessDates().getDueDate();
		}
		else
		{
			dueDate = topic.getAccessDates().getDueDate();
		}
		
		if (this.isOverrideAllowUntilDate())
		{
			allowUntilDate = this.getAccessDates().getAllowUntilDate();
		}
		else
		{
			allowUntilDate = topic.getAccessDates().getAllowUntilDate();
		}
		
				
		boolean blnOpenDate = false, blnDueDate = false, blnAllowUntilDate = false;
		
		if (openDate != null) 
	 	{
			blnOpenDate = true;
	 	}
		
	 	if (dueDate != null) 
	 	{
	 		blnDueDate = true;
	 	}
	 	
	 	if (allowUntilDate != null) 
	 	{
	 		blnAllowUntilDate = true;
	 	}
	 	
	 	if (blnOpenDate && blnDueDate && blnAllowUntilDate)
	 	{
		 	if ((openDate.after(dueDate)) || (openDate.after(allowUntilDate)) || (dueDate.after(allowUntilDate))) 
		 	{
		   		return false;
		 	}
	 	}
	 	else if (blnOpenDate && blnDueDate && !blnAllowUntilDate)
		{
	 		if (openDate.after(dueDate)) 
		 	{
		   		
		   		return false;
		 	}
		}
	 	else if (!blnOpenDate && blnDueDate && blnAllowUntilDate)
		{
	 		if (dueDate.after(allowUntilDate)) 
		 	{
		   		return false;
		 	}
		}
	 	else if (blnOpenDate && !blnDueDate && blnAllowUntilDate)
	 	{
		 	if (openDate.after(allowUntilDate)) 
		 	{
		   		return false;
		 	}
	 	}

	 	return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public Boolean isLockOnEndDate()
	{
		return lockOnEndDate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isOverrideAllowUntilDate()
	{
		return overrideAllowUntilDate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isOverrideEndDate()
	{
		return overrideEndDate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isOverrideHideUntilOpen()
	{
		return overrideHideUntilOpen;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isOverrideLockEndDate()
	{
		return overrideLockEndDate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isOverrideStartDate()
	{
		return overrideStartDate;
	}
	
	/**
	 * Sets the access dates
	 * 
	 * @param accessDates 	The accessDates to set
	 */
	public void setAccessDates(AccessDates accessDates)
	{
		this.accessDates = accessDates;
	}
	
	/**
	 * sets the forum id
	 * 
	 * @param forumId	The forumId to set
	 */
	public void setForumId(int forumId)
	{
		this.forumId = forumId;
	}
	
	/**
	 * Sets the id
	 * 
	 * @param id 	The id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public void setLockOnEndDate(Boolean lockOnEndDate)
	{
		this.lockOnEndDate = lockOnEndDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setOverrideAllowUntilDate(Boolean overrideAllowUntilDate)
	{
		this.overrideAllowUntilDate = overrideAllowUntilDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setOverrideEndDate(Boolean overrideEndDate)
	{
		this.overrideEndDate = overrideEndDate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setOverrideHideUntilOpen(Boolean overrideHideUntilOpen)
	{
		this.overrideHideUntilOpen = overrideHideUntilOpen;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setOverrideLockEndDate(Boolean overrideLockEndDate)
	{
		this.overrideLockEndDate = overrideLockEndDate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setOverrideStartDate(Boolean overrideStartDate)
	{
		this.overrideStartDate = overrideStartDate;
	}
	
	/**
	 * Sets the id
	 * 
	 * @param id 	The id to set
	 */
	public void setTopicId(int topicId)
	{
		this.topicId = topicId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setUserIds(List<Integer> userIds)
	{
		this.userIds = userIds;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setUsers(List<User> users)
	{
		this.users = users;
	}
}
