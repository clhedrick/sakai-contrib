/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/ForumImpl.java $ 
 * $Id: ForumImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etudes.api.app.jforum.AccessDates;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Evaluation;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.JForumAccessException;
import org.etudes.api.app.jforum.JForumCategoryService;
import org.etudes.api.app.jforum.JForumPostService;
import org.etudes.api.app.jforum.JForumSecurityService;
import org.etudes.api.app.jforum.LastPostInfo;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.Topic;

/**
 * Represents forum
 */
public class ForumImpl implements Forum
{
	protected AccessDates accessDates = null;
	
	protected int accessType;
	
	protected Boolean blocked = Boolean.FALSE;
	
	protected String blockedByDetails;
	
	protected String blockedByTitle;
	
	protected Category category = null;
	
	protected int categoryId;
	
	protected String createdBySakaiUserId;
	
	protected String description = null;
	
	protected List<Evaluation> evaluations = new ArrayList<Evaluation>();
	
	protected Grade grade = null;
	
	protected int gradeType;
	
	protected List<String> groups = null;
	
	protected int id;
	
	protected int lastPostId;
	
	protected LastPostInfo lastPostInfo = null;
	
	protected String modifiedBySakaiUserId;
	
	protected String name = null;

	protected int order;
	
	protected List<SpecialAccess> specialAccess = new ArrayList<SpecialAccess>();
	
	protected List<Topic> topics = new ArrayList<Topic>();
	
	protected int totalPosts;
	
	protected int totalTopics;
	
	protected int type;
	
	protected boolean unread;
	
	protected Map<String, Integer> userPostCount = new HashMap<String, Integer>();
	
	transient String currentSakaiUserId = null;
	
	transient JForumCategoryService jforumCategoryService = null;

	transient JForumPostService jforumPostService = null;
	
	transient JForumSecurityService jforumSecurityService = null;
	
	protected Boolean mayCreateTopic = Boolean.TRUE;
	
	public ForumImpl() {}
	
	public ForumImpl(JForumPostService jforumPostService, JForumCategoryService jforumCategoryService, JForumSecurityService jforumSecurityService) 
	{
		this.jforumPostService = jforumPostService;
		this.jforumCategoryService = jforumCategoryService;
		this.jforumSecurityService = jforumSecurityService;
	}

	protected ForumImpl(ForumImpl other)
	{
		this.id = other.id;
		this.categoryId = other.categoryId;
		this.name = other.name;
		this.description = other.description;
		this.type = other.type;
		this.accessType = other.accessType;
		this.gradeType = other.gradeType;
		this.accessDates = new AccessDatesImpl((AccessDatesImpl)other.accessDates);
		if (other.groups != null)
		{
			this.groups = new ArrayList<String>(other.groups);
		}
		
		if (other.lastPostInfo != null)
		{
			this.lastPostInfo = new LastPostInfoImpl((LastPostInfoImpl)other.lastPostInfo);
		}
		this.order = other.order;
		this.totalTopics = other.totalTopics;
		this.totalPosts = other.totalPosts;
		this.unread = other.unread;
		this.lastPostId = other.lastPostId;
		
		for (SpecialAccess specialAccess : other.getSpecialAccess())
		{
			
			this.specialAccess.add(new SpecialAccessImpl((SpecialAccessImpl)specialAccess));
		}
		
		if (other.grade != null)
		{
			this.grade = new GradeImpl((GradeImpl)other.grade);
		}
		
		this.currentSakaiUserId = other.currentSakaiUserId;
		
		this.blocked = other.blocked;
		this.blockedByDetails = other.blockedByDetails;
		this.blockedByTitle = other.blockedByTitle;		
		
		// evaluations - retrieve if needed
		
		this.jforumCategoryService = other.jforumCategoryService;
		this.jforumPostService = other.jforumPostService;
	}

	/** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) 
	{
		// two forums are equals if they have the same id
		return ((o instanceof Forum) && (((Forum)o).getId() == this.id));
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
	public int getAccessType()
	{
		return accessType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean getBlocked()
	{
		return blocked;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBlockedByDetails()
	{
		return blockedByDetails;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getBlockedByTitle()
	{
		return blockedByTitle;
	}

	/**
	 * {@inheritDoc}
	 */
	public Category getCategory()
	{
		if (this.category != null)
		{
			return this.category;
		}
		
		if (this.jforumCategoryService != null && getCategoryId() > 0)
		{
			Category category = null;
			if (getCreatedBySakaiUserId() != null)
			{
				try
				{
					this.jforumCategoryService.getCategory(categoryId, getCreatedBySakaiUserId());
				}
				catch (JForumAccessException e)
				{
				}
			}
			else
			{
				category = this.jforumCategoryService.getCategory(getCategoryId());
			}
			
			this.setCategory(category);
			
			return category;
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getCategoryId()
	{
		return categoryId;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getCreatedBySakaiUserId()
	{
		return createdBySakaiUserId;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Evaluation> getEvaluations()
	{
		return evaluations;
	}

	/**
	 * @return The grade
	 */
	public Grade getGrade()
	{
		return grade;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getGradeType()
	{
		return gradeType;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getGroups()
	{
		return groups;
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
	public int getLastPostId()
	{
		return lastPostId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public LastPostInfo getLastPostInfo()
	{
		return lastPostInfo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public LastPostInfo getLastPostInfo(String context, String sakaiUserId)
	{
		if (lastPostInfo != null)
		{
			return lastPostInfo;
		}
		
		LastPostInfo lastPostInfo = null;
		
		if ((context != null && context.trim().length() > 0) && (sakaiUserId != null && sakaiUserId.trim().length() > 0))
		{
			if (this.jforumPostService != null)
			{
				lastPostInfo = this.jforumPostService.getForumLatestPostInfo(context, this.id, sakaiUserId);
			}
		}
		
		this.lastPostInfo = lastPostInfo;
		
		return lastPostInfo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getModifiedBySakaiUserId()
	{
		return modifiedBySakaiUserId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getOrder()
	{
		return order;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<SpecialAccess> getSpecialAccess()
	{
		return specialAccess;
	}

	/**
	 * @return the topics
	 */
	public List<Topic> getTopics()
	{
		return topics;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTotalPosts()
	{
		return totalPosts;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTotalTopics()
	{
		return totalTopics;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getType()
	{
		return type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ForumDisplayStatus getUserDiplayStatus()
	{
		/*
		 	Rules:
			(1) If access is "Site Participants," then the forum type icon 
				wins (Normal/blank, read-only, or reply-only). 
			(2) If access is "Deny Access," no matter what type of forum 
				is selected (Normal, Reply-only, or Read-only), the "Deny Access" icon ALWAYS wins! 
			(3) If access is "selected group," and forum type is Normal OR Reply-only, 
				then group/people icon wins. 
			(4) if access is "selected group" and forum type is Read-only (locked! - 
				no posting allowed anymore), let's use an additional icon that is a 
				combo of groups with a lock (like the read-only). I'll attach it next. 
			
				Access Type: 0 - Site Access, 1 - Deny Access, 2 - Groups Access
				Forum Type: 0 - Normal, 1 - Reply Only, 2 - Read only
			
			* invisible forums have the deny access icon when they are NOT open -- ALONG WITH THE DATE 
				("Opens: date, time), just like we display the locked at x date, time, so that teachers 
				see when it will become visible. 
			* when an invisible forum opens, the deny access icon is replaced with the forum type 
				(normal, read-only, etc.) 
			* with regard to export, invisible forums are exported, even if they are not open yet, 
				but with NO dates (I don't think we want dates). 
			
			--forum visible date(start date)
			-If the visible date is after now i.e. form is not accessible, show deny icon
			-If the visible date is before now i.e. form is accessible, apply access and type rules
			
			--forum lock date(end date)
			-If the lock date is before now i.e. form is not accessible, show read only icon
			-If the visible date is after now i.e. form is accessible, apply access and type rules
		*/
		boolean unread = false;
		
		if (this.isUnread())
		{
			unread = true;
		}
		
		// facilitator or participant
		boolean specialAccessUser = false;
		SpecialAccess userSpecialAccess = null;
		
		String currentSakaiUserId = getCurrentSakaiUserId();
		if (currentSakaiUserId != null)
		{
			Category category = getCategory();
			
			if (category != null)
			{
			
				boolean facilitator = this.jforumSecurityService.isJForumFacilitator(category.getContext(), currentSakaiUserId);
				
				boolean participant = false;
				if (!facilitator)
				{
					participant = this.jforumSecurityService.isJForumParticipant(category.getContext(), currentSakaiUserId);
				}			
				if (participant)
				{
					// check for user special access
					if (this.accessDates != null && (this.accessDates.getOpenDate() != null || this.accessDates.getDueDate() != null || this.accessDates.getAllowUntilDate() != null))
					{
						if (this.getSpecialAccess().size() == 1)
						{
							specialAccessUser = true;
							userSpecialAccess = this.getSpecialAccess().get(0);
						}
					}
				}
			}
		}
		
		ForumDisplayStatus userStatusDisplay = ForumDisplayStatus.FOLDER;
		
		Date currentTime = Calendar.getInstance().getTime();
		
		boolean accessTypeRules = true;
		
		if (specialAccessUser)
		{
			// special access user			
			if ((userSpecialAccess.getAccessDates() != null) && (userSpecialAccess.getAccessDates().getOpenDate() != null) && (userSpecialAccess.getAccessDates().getOpenDate().after(currentTime)))
			{
				if (userSpecialAccess.getAccessDates().isHideUntilOpen())
				{
					userStatusDisplay = ForumDisplayStatus.NOT_YET_OPEN_INVISIBLE;
				}
				else
				{
					userStatusDisplay = (unread ? ForumDisplayStatus.NOT_YET_OPEN_NEW : ForumDisplayStatus.NOT_YET_OPEN);
				}
				
				accessTypeRules = false;
			}
			else if ((this.accessType != ForumAccess.GROUPS.getAccessType()) && (userSpecialAccess.getAccessDates() != null && (userSpecialAccess.getAccessDates().getAllowUntilDate() != null || userSpecialAccess.getAccessDates().getDueDate() != null)))
			{
				if (this.accessType == ForumAccess.DENY.getAccessType())
				{
					userStatusDisplay = (unread ? ForumDisplayStatus.DENY_ACCESS_NEW : ForumDisplayStatus.DENY_ACCESS);
					accessTypeRules = false;
				}
				else if (userSpecialAccess.getAccessDates().getAllowUntilDate() != null)
				{				
					if (userSpecialAccess.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						userStatusDisplay = (unread ? ForumDisplayStatus.READ_ONLY_NEW : ForumDisplayStatus.READ_ONLY);
						accessTypeRules = false;
					}
				}
				else if ((userSpecialAccess.getAccessDates().getDueDate() != null) && (userSpecialAccess.getAccessDates().getDueDate().before(currentTime)))
				{
					userStatusDisplay = (unread ? ForumDisplayStatus.READ_ONLY_NEW : ForumDisplayStatus.READ_ONLY);
					accessTypeRules = false;
				}			
			}
			
			if (accessTypeRules)
			{
				if (this.accessType == ForumAccess.SITE.getAccessType())
				{
					if (this.type ==ForumType.NORMAL.getType())
					{
						userStatusDisplay = (unread ? ForumDisplayStatus.FOLDER_NEW : ForumDisplayStatus.FOLDER);
					}
					else if (this.type ==ForumType.REPLY_ONLY.getType())
					{
						userStatusDisplay = (unread ? ForumDisplayStatus.REPLY_ONLY_NEW : ForumDisplayStatus.REPLY_ONLY);
					}
					else if (this.type ==ForumType.READ_ONLY.getType())
					{
						userStatusDisplay = (unread ? ForumDisplayStatus.READ_ONLY_NEW : ForumDisplayStatus.READ_ONLY);
					}
				}
				else if (this.accessType == ForumAccess.DENY.getAccessType())
				{
					userStatusDisplay = (unread ? ForumDisplayStatus.DENY_ACCESS_NEW : ForumDisplayStatus.DENY_ACCESS);
				}
				else if (this.accessType == ForumAccess.GROUPS.getAccessType())
				{	
					boolean datesChecked = false;
					if ((userSpecialAccess.getAccessDates() != null) && (userSpecialAccess.getAccessDates().getAllowUntilDate() != null))
					{
						if (userSpecialAccess.getAccessDates().getAllowUntilDate().before(currentTime))
						{
							userStatusDisplay = (unread ? ForumDisplayStatus.READ_ONLY_NEW : ForumDisplayStatus.READ_ONLY);
							datesChecked = true;
						}
					}
					else if ((userSpecialAccess.getAccessDates() != null) && (userSpecialAccess.getAccessDates().getDueDate() != null) && (userSpecialAccess.getAccessDates().getDueDate().before(currentTime)))
					{
						userStatusDisplay = (unread ? ForumDisplayStatus.READ_ONLY_NEW : ForumDisplayStatus.READ_ONLY);
						datesChecked = true;
					}
					
					if (!datesChecked)
					{
						if (this.type == ForumType.NORMAL.getType())
						{
							userStatusDisplay = (unread ? ForumDisplayStatus.GROUP_NEW : ForumDisplayStatus.GROUP);
						}
						else if (this.type == ForumType.REPLY_ONLY.getType())
						{
							userStatusDisplay = (unread ? ForumDisplayStatus.REPLY_ONLY_NEW : ForumDisplayStatus.REPLY_ONLY);
						}
						else if (this.type == ForumType.READ_ONLY.getType())
						{
							userStatusDisplay = (unread ? ForumDisplayStatus.READ_ONLY_NEW : ForumDisplayStatus.READ_ONLY);
						}
					}
				}
			}
		}
		else
		{

			if ((this.accessDates != null) && (this.accessDates.getOpenDate() != null) && (this.accessDates.getOpenDate().after(currentTime)))
			{
				if (this.accessDates.isHideUntilOpen())
				{
					userStatusDisplay = ForumDisplayStatus.NOT_YET_OPEN_INVISIBLE;
				}
				else
				{
					userStatusDisplay = (unread ? ForumDisplayStatus.NOT_YET_OPEN_NEW : ForumDisplayStatus.NOT_YET_OPEN);
				}

				accessTypeRules = false;
			}
			else if ((this.accessType != ForumAccess.GROUPS.getAccessType()) && (this.accessDates != null && (this.accessDates.getAllowUntilDate() != null || this.accessDates.getDueDate() != null)))
			{
				if (this.accessType == ForumAccess.DENY.getAccessType())
				{
					userStatusDisplay = (unread ? ForumDisplayStatus.DENY_ACCESS_NEW : ForumDisplayStatus.DENY_ACCESS);
					accessTypeRules = false;
				}
				// else if ((this.accessDates.isLocked()) && (this.type == ForumType.NORMAL.getType()))
				else if (this.accessDates.getAllowUntilDate() != null)
				{
					if (this.accessDates.getAllowUntilDate().before(currentTime))
					{
						userStatusDisplay = (unread ? ForumDisplayStatus.READ_ONLY_NEW : ForumDisplayStatus.READ_ONLY);
						accessTypeRules = false;
					}				
				}
				else if ((this.accessDates.getDueDate() != null) && (this.accessDates.getDueDate().before(currentTime)))
				{
					userStatusDisplay = (unread ? ForumDisplayStatus.READ_ONLY_NEW : ForumDisplayStatus.READ_ONLY);
					accessTypeRules = false;
				}
				else
				{
					userStatusDisplay = (unread ? ForumDisplayStatus.FOLDER_NEW : ForumDisplayStatus.FOLDER);
					accessTypeRules = false;					
				}
			}
			
			if (accessTypeRules)
			{
				if (this.accessType == ForumAccess.SITE.getAccessType())
				{
					if (this.type == ForumType.NORMAL.getType())
					{
						userStatusDisplay = (unread ? ForumDisplayStatus.FOLDER_NEW : ForumDisplayStatus.FOLDER);
					}
					else if (this.type == ForumType.REPLY_ONLY.getType())
					{
						userStatusDisplay = (unread ? ForumDisplayStatus.REPLY_ONLY_NEW : ForumDisplayStatus.REPLY_ONLY);
					}
					else if (this.type == ForumType.READ_ONLY.getType())
					{
						userStatusDisplay = (unread ? ForumDisplayStatus.READ_ONLY_NEW : ForumDisplayStatus.READ_ONLY);
					}
				}
				else if (this.accessType == ForumAccess.DENY.getAccessType())
				{
					userStatusDisplay = (unread ? ForumDisplayStatus.DENY_ACCESS_NEW : ForumDisplayStatus.DENY_ACCESS);
				}
				else if (this.accessType == ForumAccess.GROUPS.getAccessType())
				{
					boolean datesChecked = false;
					
					if ((this.accessDates != null) && (this.accessDates.getAllowUntilDate() != null))
					{
						if (this.accessDates.getAllowUntilDate().before(currentTime))
						{
							userStatusDisplay = (unread ? ForumDisplayStatus.GROUP_READ_ONLY_NEW : ForumDisplayStatus.GROUP_READ_ONLY);
							datesChecked = true;
						}
					}
					else if ((this.accessDates != null) && (this.accessDates.getDueDate() != null) && (this.accessDates.getDueDate().before(currentTime)))
					{
						userStatusDisplay = (unread ? ForumDisplayStatus.GROUP_READ_ONLY_NEW : ForumDisplayStatus.GROUP_READ_ONLY);
						datesChecked = true;
					}
					
					if (!datesChecked)
					{
						if (this.type == ForumType.NORMAL.getType())
						{
							userStatusDisplay = (unread ? ForumDisplayStatus.GROUP_NEW : ForumDisplayStatus.GROUP);
						}
						else if (this.type == ForumType.REPLY_ONLY.getType())
						{
							userStatusDisplay = (unread ? ForumDisplayStatus.GROUP_REPLY_ONLY_NEW : ForumDisplayStatus.GROUP_REPLY_ONLY);
						}
						else if (this.type == ForumType.READ_ONLY.getType())
						{
							userStatusDisplay = (unread ? ForumDisplayStatus.GROUP_READ_ONLY_NEW : ForumDisplayStatus.GROUP_READ_ONLY);
						}
					}
				}
			}
		}	
				
		return userStatusDisplay;
	}
	
	public LastPostInfo getUserLastPostInfo()
	{
		if (lastPostInfo != null)
		{
			return lastPostInfo;
		}
		
		LastPostInfo lastPostInfo = null;
		
		Category category = getCategory();
		
		if (category != null)
		{
			String context = category.getContext();
			String sakaiUserId = this.currentSakaiUserId;
			
			if (sakaiUserId == null)
			{
				sakaiUserId = ((CategoryImpl)category).getCurrentSakaiUserId();
			}
			
			if ((context != null && context.trim().length() > 0) && (sakaiUserId != null && sakaiUserId.trim().length() > 0))
			{
				if (this.jforumPostService != null)
				{
					lastPostInfo = this.jforumPostService.getForumLatestPostInfo(context, this.id, sakaiUserId);
				}
			}
		}
		
		this.lastPostInfo = lastPostInfo;
		
		return lastPostInfo;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, Integer> getUserPostCount()
	{
		return userPostCount;
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
	public boolean isUnread()
	{
		return unread;
	}

	/**
	 * Gets user access to create the topic
	 */
	public Boolean mayCreateTopic()
	{
		return mayCreateTopic;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAccessDates(AccessDates accessDates)
	{
		this.accessDates = accessDates;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAccessType(int accessType)
	{
		this.accessType = accessType;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBlocked(Boolean blocked)
	{
		this.blocked = blocked;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBlockedByDetails(String blockedByDetails)
	{
		this.blockedByDetails = blockedByDetails;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBlockedByTitle(String blockedByTitle)
	{
		this.blockedByTitle = blockedByTitle;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category)
	{
		this.category = category;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCategoryId(int categoryId)
	{
		this.categoryId = categoryId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCreatedBySakaiUserId(String createdBySakaiUserId)
	{
		this.createdBySakaiUserId = createdBySakaiUserId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @param grade 
	 * 		The grade to set
	 */
	public void setGrade(Grade grade)
	{
		this.grade = grade;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGradeType(int gradeType)
	{
		this.gradeType = gradeType;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGroups(List<String> groups)
	{
		this.groups = groups;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * @param lastPostId the lastPostId to set
	 */
	public void setLastPostId(int lastPostId)
	{
		this.lastPostId = lastPostId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setLastPostInfo(LastPostInfo lastPostInfo)
	{
		this.lastPostInfo = lastPostInfo;
	}

	/**
	 * Sets user may create topic access
	 * 
	 * @param mayCreateTopic
	 */
	public void setMayCreateTopic(Boolean mayCreateTopic)
	{
		this.mayCreateTopic = mayCreateTopic;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setModifiedBySakaiUserId(String modifiedBySakaiUserId)
	{
		this.modifiedBySakaiUserId = modifiedBySakaiUserId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setOrder(int order)
	{
		this.order = order;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setTotalPosts(int totalPosts)
	{
		this.totalPosts = totalPosts;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTotalTopics(int totalTopics)
	{
		this.totalTopics = totalTopics;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setType(int type)
	{
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setUnread(boolean unread)
	{
		this.unread = unread;
	}
	
	/**
	 * @return the currentSakaiUserId
	 */
	protected String getCurrentSakaiUserId()
	{
		return currentSakaiUserId;
	}
	
	/**
	 * @param currentSakaiUserId the currentSakaiUserId to set
	 */
	protected void setCurrentSakaiUserId(String currentSakaiUserId)
	{
		this.currentSakaiUserId = currentSakaiUserId;
	}
}
