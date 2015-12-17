/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumForumServiceImpl.java $ 
 * $Id: JForumForumServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.AccessDates;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.JForumAccessException;
import org.etudes.api.app.jforum.JForumForumService;
import org.etudes.api.app.jforum.JForumForumTopicsExistingException;
import org.etudes.api.app.jforum.JForumGBService;
import org.etudes.api.app.jforum.JForumGradeService;
import org.etudes.api.app.jforum.JForumGradesModificationException;
import org.etudes.api.app.jforum.JForumItemEvaluatedException;
import org.etudes.api.app.jforum.JForumPostService;
import org.etudes.api.app.jforum.JForumSecurityService;
import org.etudes.api.app.jforum.JForumService;
import org.etudes.api.app.jforum.JForumSpecialAccessService;
import org.etudes.api.app.jforum.JForumUserService;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.Topic;
import org.etudes.api.app.jforum.User;
import org.etudes.api.app.jforum.dao.CategoryDao;
import org.etudes.api.app.jforum.dao.ForumDao;
import org.etudes.util.api.AccessAdvisor;
import org.sakaiproject.authz.api.Member;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.thread_local.api.ThreadLocalManager;

public class JForumForumServiceImpl implements JForumForumService
{
	
	private static Log logger = LogFactory.getLog(JForumForumServiceImpl.class);
		
	/** Dependency (optional, self-injected): AccessAdvisor. */
	protected transient AccessAdvisor accessAdvisor = null;
	
	/** Dependency: CategoryDao. */
	protected CategoryDao categoryDao = null;

	/** Dependency: ForumDao. */
	protected ForumDao forumDao = null;
	
	/** Dependency:JForumGBService */
	protected JForumGBService jforumGBService = null;
	
	/** Dependency: JForumGradeService */
	protected JForumGradeService jforumGradeService = null;
	
	/** Dependency: JForumPostService */
	protected JForumPostService jforumPostService = null;
	
	/** Dependency: JForumSecurityService. */
	protected JForumSecurityService jforumSecurityService = null;
	
	/** Dependency: JForumSpecialAccessService */
	protected JForumSpecialAccessService jforumSpecialAccessService = null;
	
	/** Dependency: JForumUserService */
	protected JForumUserService jforumUserService = null;
	
	/** Dependency: SiteService. */
	protected SiteService siteService = null;
	
	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	/** Dependency: ThreadLocalManager. */
	protected ThreadLocalManager threadLocalManager = null;
	
	/**
	 * {@inheritDoc}
	 */
	public int createForum(Forum forum) throws JForumAccessException
	{
		if ((forum == null) || (forum.getId() > 0))
		{
			throw new IllegalArgumentException();			
		}
		
		if (forum.getCategoryId() == 0)
		{
			throw new IllegalArgumentException("Forum category information is missing");
		}
		
		if (forum.getCreatedBySakaiUserId() == null)
		{
			throw new IllegalArgumentException("Created by user information is missing");
		}
		
		Category category = categoryDao.selectById(forum.getCategoryId());
		
		((ForumImpl)forum).setCategory(category);
		
		if (category == null)
		{
			throw new IllegalArgumentException("Topic forum category is not a valid category or not existing");
		}
		
		if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
		{
			if ((forum.getGroups() == null) || forum.getGroups().size() == 0)
			{
				forum.setAccessType(Forum.ForumAccess.SITE.getAccessType());
			}
		}
		
		// access check
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), forum.getCreatedBySakaiUserId());
		
		if (!facilitator)
		{
			throw new JForumAccessException(forum.getCreatedBySakaiUserId());
		}
		
		if (category.isGradable())
		{
			forum.setGradeType(Grade.GradeType.DISABLED.getType());
			forum.setGrade(null);
		}
		
		if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
		{
			if ((forum.getGrade() == null) || (forum.getGrade().getPoints() == null))
			{
				forum.setGradeType(Grade.GradeType.DISABLED.getType());
				forum.setGrade(null);
			}
			else
			{
				forum.getGrade().setContext(category.getContext());
			}
		}
		else
		{
			forum.setGrade(null);
		}
		
		// if category has dates forum cannot have dates
		if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
		{
			forum.setAccessDates(null);
		}
		
		int forumId = forumDao.addNew(forum);
		
		// gradable forum add to grade book
		if ((forum.getGradeType() == Grade.GradeType.FORUM.getType()) && ((forum.getGrade() != null) && (forum.getGrade().isAddToGradeBook())))
		{
			this.jforumGradeService.updateGradebook(forum.getGrade(), forum);
		}
		
		return forumId;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteForum(int forumId, String sakaiUserId) throws JForumAccessException, JForumForumTopicsExistingException, JForumItemEvaluatedException
	{
		// is forum existing
		Forum forum = getForum(forumId);
		
		if (forum == null)
		{
			return;
		}
		
		Category category = forum.getCategory();
		// check user access
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), sakaiUserId);
		
		if (!facilitator)
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		// check topics
		int forumTopicsCount = getTotalTopics(forumId);
		
		if (forumTopicsCount > 0)
		{
			throw new JForumForumTopicsExistingException(forum.getName());
		}
		
		// if gradable forum check for evaluations
		if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
		{
			int evalCount = jforumGradeService.getForumEvaluationsCount(forumId);
			
			if (evalCount > 0)
			{
				throw new JForumItemEvaluatedException(forum.getName());
			}		
		}
		
		forumDao.delete(forumId);
		
		// gradable forum remove entry in the gradebook
		if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
		{
			Grade grade = forum.getGrade();
			
			if (grade != null && grade.isAddToGradeBook())
			{
				jforumGradeService.removeGradebookEntry(grade);
			}
		}
	}
	
	public void destroy()
	{
		if (logger.isInfoEnabled())
			logger.info("destroy....");
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void evaluateForum(Forum forum) throws JForumAccessException
	{

		if (forum == null || forum.getGradeType() != Grade.GradeType.FORUM.getType())
		{
			return;
		}

		Grade grade = forum.getGrade();
		
		if (grade == null)
		{
			return;
		}
		
		jforumGradeService.addModifyForumEvaluations(grade, forum.getEvaluations());	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Forum> getCategoryForums(int categoryId)
	{
		return forumDao.selectByCategoryId(categoryId);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Forum getForum(int forumId)
	{
		if (forumId <= 0) throw new IllegalArgumentException("Forum id is needed.");
		
		// for thread-local caching
		String key = cacheKey(String.valueOf(forumId));
		ForumImpl cachedForum = (ForumImpl) this.threadLocalManager.get(key);
		if (cachedForum != null)
		{
			return this.clone(cachedForum);
		}
		Forum forum = forumDao.selectById(forumId);
		
		// thread-local cache (a copy)
		if (forum != null) this.threadLocalManager.set(key, this.clone((ForumImpl)forum));
				
		return forum;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Forum getForum(int forumId, String sakaiUserId) throws JForumAccessException
	{
		if (forumId <= 0)
		{
			throw new IllegalArgumentException("Forum id is needed.");
		}
		
		if (sakaiUserId ==  null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakai user id is needed.");
		}
		
		Forum forum = forumDao.selectById(forumId);
		
		if (forum == null)
		{
			return null;
		}
		
		Category category = forum.getCategory();
		
		if (category == null)
		{
			throw new IllegalArgumentException("Forum category is not a valid category or not existing");
		}
		
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), sakaiUserId);
		
		boolean participant = false;
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(category.getContext(), sakaiUserId);
		}
		
		if (!(facilitator || participant))
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		((ForumImpl)forum).setCurrentSakaiUserId(sakaiUserId);
		((CategoryImpl)category).setCurrentSakaiUserId(sakaiUserId);
		
		//check user forum access and topics count
		if (participant)
		{
			checkUserForumAccess(category, forum, sakaiUserId);
			getUserForumTopicsCount(forum, sakaiUserId);
		}
		else
		{
			((ForumImpl)forum).setMayCreateTopic(Boolean.TRUE);
		}
						
		//forum read status
		checkForumUnreadTopics(forum, sakaiUserId);		
		
		return forum;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getForumDatesCount(int categoryId)
	{
		int forumCount = 0;
		
		String sql;
		Object[] fields;
		int i = 0;
		
		sql = "SELECT count(forum_id) As forums_with_dates FROM jforum_forums WHERE categories_id = ? AND (start_date IS NOT NULL OR end_date IS NOT NULL OR allow_until_date IS NOT NULL)";						
				
		fields = new Object[1];
		fields[i++] = categoryId;
		

		final List<Integer> count = new ArrayList<Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					count.add(result.getInt("forums_with_dates"));

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getForumDatesCount: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (count.size() == 1)
		{
			forumCount = count.get(0);
		}
		
		return forumCount;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<User> getForumParticipantUsers(int forumId)
	{
		Forum forum = getForum(forumId);
		Category category = forum.getCategory();
		
		// check for groups
		if ((forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType()) && (forum.getGroups() != null) && forum.getGroups().size() > 0)
		{
			List<User> users = null;
			Map<String, User> usersMap = new HashMap<String, User>();
			try 
			{
				
				users = jforumUserService.getSiteUsers(category.getContext());
				
				Iterator<User> userIterator = users.iterator();
				while (userIterator.hasNext())
				{
					User user = userIterator.next();
					if (jforumSecurityService.isJForumFacilitator(category.getContext(), user.getSakaiUserId()))
					{
						userIterator.remove();
					}
					else
					{
						usersMap.put(user.getSakaiUserId(), user);
					}
				}
			}
			catch (Exception e) 
			{
				if (logger.isErrorEnabled()) 
				{
					logger.error(e.toString(), e);
				}
			}
					
			List<String> forumGroupsIds = forum.getGroups();
			//this.context.put("groupsExist", true);
			
			//show the selected groups for the forum and group users
			Site site = null;
			try
			{
				site = siteService.getSite(category.getContext());
			}
			catch (IdUnusedException e)
			{
				return new ArrayList<User>();
			}
			
			Collection sakaiSiteGroups = site.getGroups();
			
			Map forumGroupsMap = new HashMap();
			List<User> forumGroupMembers = new ArrayList<User>();
			
			for (Iterator i = sakaiSiteGroups.iterator(); i.hasNext();)
			{
				Group group = (Group) i.next();
				
				//get froum group users
				if (forumGroupsIds.contains(group.getId()))
				{
					Set members = group.getMembers();
					for (Iterator iter = members.iterator(); iter.hasNext();)
					{
						Member member = (Member) iter.next();
						
						if (usersMap.containsKey(member.getUserId()))
						{
							if (!forumGroupMembers.contains(usersMap.get(member.getUserId())))
							{
								forumGroupMembers.add(usersMap.get(member.getUserId()));
							}
						}
					}
				}
			}
						
			users.clear();
			users.addAll(forumGroupMembers);
			
			Collections.sort(users, new Comparator<User>()
			{
				public int compare(User user1, User user2) 
				{
					if ((user1.getLastName() == null || user1.getLastName().trim().length() == 0)  || (user2.getLastName() == null || user2.getLastName().trim().length() == 0))
					{
						return 0;
					}
					else
					{
						return user1.getLastName().toUpperCase().compareTo(user2.getLastName().toUpperCase());
					}
				}
			});
			
			return users;
		}
		else
		{
			// all site participants
			List<User> users = null;
			try 
			{
				users = jforumUserService.getSiteUsers(category.getContext());
				Iterator<User> userIterator = users.iterator();
				while (userIterator.hasNext())
				{
					User user = (User) userIterator.next();
					if (jforumSecurityService.isJForumFacilitator(category.getContext(), user.getSakaiUserId()))
					{
						userIterator.remove();
					}
				}
			}
			catch (Exception e) 
			{
				if (logger.isErrorEnabled()) 
				{
					logger.error(e.toString(), e);
				}
			}
			
			return users;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTotalTopics(int forumId)
	{
		if (forumId <= 0)
		{
			throw new IllegalArgumentException("Forum id is needed.");
		}
		
		return forumDao.getTotalTopics(forumId);
	}
	
	public void init()
	{
		if (logger.isInfoEnabled())
			logger.info("init....");
		
		// check if there is an access advisor - if not, that's ok.
		this.accessAdvisor = (AccessAdvisor) ComponentManager.get(AccessAdvisor.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isUserHasCreateTopicAccess(int forumId, String sakaiUserId)
	{
		if (forumId <= 0)
		{
			throw new IllegalArgumentException("Forum id is needed.");
		}
		
		if (sakaiUserId ==  null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakai user id is needed.");
		}
		
		Forum forum = forumDao.selectById(forumId);
		
		if (forum == null)
		{
			return false;
		}
		
		Category category = forum.getCategory();
		
		if (category == null)
		{
			throw new IllegalArgumentException("Forum category is not a valid category or not existing");
		}
		
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), sakaiUserId);
		
		boolean participant = false;
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(category.getContext(), sakaiUserId);
		}
		
		if (!(facilitator || participant))
		{
			return false;
		}
		
		if (facilitator)
		{
			return true;
		}
		
		// ignore forums with category invalid dates
		if (category.getAccessDates() != null)
		{
			if (!category.getAccessDates().isDatesValid())
			{
				return false;
			}
		}	
		
		// ignore forums with invalid dates
		if (forum.getAccessDates() != null)
		{
			if (!forum.getAccessDates().isDatesValid())
			{
				return false;
			}
		}
		
		// forum type 
		if (forum.getType() == Forum.ForumType.READ_ONLY.getType())
		{
			return false;
		}
		else if (forum.getType() == Forum.ForumType.REPLY_ONLY.getType())
		{
			return false;
		}
		else if (forum.getType() == Forum.ForumType.NORMAL.getType())
		{
			// user has access
		}
		
		// access type
		if (forum.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
		{
			return false;
		}
		else if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
		{
			// verify user in forum groups
			if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
			{
				boolean userInGroup = false;
				
				try
				{
					// fetch user groups only once
					Site site = siteService.getSite(category.getContext());
					Collection<org.sakaiproject.site.api.Group> userGroups = site.getGroupsWithMember(sakaiUserId);
					
					for (org.sakaiproject.site.api.Group grp : userGroups) 
					{
						if ((forum.getGroups() != null) && (forum.getGroups().contains(grp.getId())))
						{
							userInGroup = true;
							break;
						}
					}
				} 
				catch (IdUnusedException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("isUserHasCreateTopicAccess: missing site: " + category.getContext());
					}
				}

				// user not in any group
				if (!userInGroup)
				{
					return false;
				}
			}
		}
		
		Date now = new Date();
		
		// forum special access
		List<SpecialAccess> specialAccessList = forum.getSpecialAccess();
		boolean specialAccessUser = false, specialAccessUserAccess = false;
		
		SpecialAccess userSa = null;
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		if (user != null)
		{
			for (SpecialAccess sa : specialAccessList)
			{
				if (sa.getUserIds().contains(new Integer(user.getId())))
				{
					userSa = sa;
					
					specialAccessUser = true;
					
					if (!sa.isOverrideStartDate())
					{
						sa.getAccessDates().setOpenDate(forum.getAccessDates().getOpenDate());
					}
					
					if (!sa.isOverrideHideUntilOpen())
					{
						sa.getAccessDates().setHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
					}
					
					if (!sa.isOverrideEndDate())
					{
						sa.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
					}
					
					/*if (!sa.isOverrideLockEndDate())
					{
						sa.getAccessDates().setLocked(forum.getAccessDates().isLocked());
					}*/
					
					if (!sa.isOverrideAllowUntilDate())
					{
					
						sa.getAccessDates().setAllowUntilDate(forum.getAccessDates().getAllowUntilDate());
					}
					
					if (sa.getAccessDates().getOpenDate() == null)
					{
						specialAccessUserAccess = true;
					}
					else
					{
						if (sa.getAccessDates().getOpenDate().after(now))
						{
							if (!sa.getAccessDates().isHideUntilOpen())
							{
								specialAccessUserAccess = true;
							}
						}
						else
						{
							specialAccessUserAccess = true;
						}
					}
					break;
				}
			}
		}
		
		forum.getSpecialAccess().clear();
		if (specialAccessUser)
		{
			forum.getSpecialAccess().add(userSa);
			
			if (specialAccessUserAccess)
			{
				
			}
			else
			{
				return false;
			}
		}
		
		// forum dates
		if (forum.getAccessDates() != null)
		{
			if (!specialAccessUser)
			{
				if (forum.getAccessDates().getOpenDate() != null && now.before(forum.getAccessDates().getOpenDate()))
				{
					return false;
				}
				else if (forum.getAccessDates().getAllowUntilDate() != null)
				{
					if (now.after(forum.getAccessDates().getAllowUntilDate()))
					{
						return false;
					}
				}
				else if (forum.getAccessDates().getDueDate() != null)
				{
					if (now.after(forum.getAccessDates().getDueDate()))
					{
						return false;
					}
				}
			}
		}
		
		// category dates
		if (category.getAccessDates() != null)
		{
			if (category.getAccessDates().getOpenDate() != null && now.before(category.getAccessDates().getOpenDate()))
			{
				return false;
			}
			else if (category.getAccessDates().getAllowUntilDate() != null)
			{
				if (now.after(category.getAccessDates().getAllowUntilDate()))
				{
					return false;
				}
			}
			else if (category.getAccessDates().getDueDate() != null)
			{
				if (now.after(category.getAccessDates().getDueDate()))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void modifyForum(Forum forum) throws JForumAccessException, JForumGradesModificationException
	{
		if (forum == null || forum.getId() <= 0 || forum.getName().trim().length() == 0)
		{
			throw new IllegalArgumentException("Forum information is missing");			
		}
		
		if (forum.getCategoryId() <= 0)
		{
			throw new IllegalArgumentException("Forum category information is missing");
		}
		
		if (forum.getModifiedBySakaiUserId() == null)
		{
			throw new IllegalArgumentException("modified by user information is missing");
		}
		
		Category category = categoryDao.selectById(forum.getCategoryId());
		
		if (category == null)
		{
			throw new IllegalArgumentException("Topic forum category is not a valid category or not existing");
		}
		
		if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
		{
			if ((forum.getGroups() == null) || forum.getGroups().size() == 0)
			{
				forum.setAccessType(Forum.ForumAccess.SITE.getAccessType());
			}
		}
		
		// access check
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), forum.getModifiedBySakaiUserId());
		
		if (!facilitator)
		{
			throw new JForumAccessException(forum.getModifiedBySakaiUserId());
		}
		
		// if existing forum grade has evaluations grade type cannot be changed
		Forum existingForum = forumDao.selectById(forum.getId());
		
		if (existingForum == null)
		{
			return;
		}
		
		int evalCount = 0;
		boolean editForum = true;
		
		if (existingForum.getGradeType() == Grade.GradeType.TOPIC.getType() && forum.getGradeType() != Grade.GradeType.TOPIC.getType())
		{
			evalCount = jforumGradeService.getForumTopicEvaluationsCount(existingForum.getId());
		}
		else if (existingForum.getGradeType() == Grade.GradeType.FORUM.getType() && forum.getGradeType() != Grade.GradeType.FORUM.getType())
		{
			evalCount = jforumGradeService.getForumEvaluationsCount(existingForum.getId());
		}

		if (forum.getGradeType() == Grade.GradeType.DISABLED.getType())
		{
			if (evalCount > 0)
			{
				editForum = false;
			}
		}
		else
		{
			if (evalCount > 0)
			{
				if ((existingForum.getGradeType() == Grade.GradeType.TOPIC.getType() && (forum.getGradeType() == Grade.GradeType.FORUM.getType()))
						|| (existingForum.getGradeType() == Grade.GradeType.FORUM.getType() && (forum.getGradeType() == Grade.GradeType.TOPIC.getType()))
						|| ((existingForum.getGradeType() == Grade.GradeType.FORUM.getType() || existingForum.getGradeType() == Grade.GradeType.TOPIC.getType()) && (forum.getGradeType() == Grade.GradeType.DISABLED.getType())))
				{
					editForum = false;
				}
			}
		}
		

		/*if (!editForum)
		{
			throw new JForumGradesModificationException("Cannot change the forum 'Grading' as this forum or the topics associated with the forum have been graded.");
		}*/
		
		boolean forumCategoryChangedToGradeCategory = false;	
		// if forum category is changed check for other category grades etc...
		if (existingForum.getCategoryId() != forum.getCategoryId() && category.isGradable())
		{
			forumCategoryChangedToGradeCategory = true;
		}
		
		
		if (!editForum)
		{
			if (forumCategoryChangedToGradeCategory)
			{
				throw new JForumGradesModificationException("Cannot change the forum Category to gradable Category as this forum or the topics associated with the forum have been graded.");
			}
			else
			{
				throw new JForumGradesModificationException("Cannot change the forum 'Grading' as this forum or the topics associated with the forum have been graded.");
			}
		}
		
		// if forum category is gradable and forum topics are gradable the forum cannot be changed to gradable
		if (category.isGradable())
		{
			forum.setGradeType(Grade.GradeType.DISABLED.getType());
			forum.setGrade(null);
		}
		else if ((existingForum.getGradeType() == Grade.GradeType.TOPIC.getType()) && (forum.getGradeType() != Grade.GradeType.TOPIC.getType()))
		{
			// check for forum gradable topics
			List<Grade> forumTopicGrades = jforumGradeService.getTopicGradesByForum(forum.getId());
			
			if (forumTopicGrades.size() > 0)
			{
				evalCount = jforumGradeService.getForumTopicEvaluationsCount(existingForum.getId());
				
				if (evalCount > 0)
				{
					forum.setGradeType(Grade.GradeType.TOPIC.getType());
					forum.setGrade(null);
				}
			}
		}
		
		if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
		{
			if ((forum.getGrade() == null) || (forum.getGrade().getPoints() == null))
			{
				forum.setGradeType(Grade.GradeType.DISABLED.getType());
				forum.setGrade(null);
			}
			else
			{
				forum.getGrade().setContext(category.getContext());
			}
		}
		else
		{
			forum.setGrade(null);
		}
		
		// if category has dates forum cannot have dates
		if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
		{
			forum.setAccessDates(null);
		}
		else
		{
			int topicDatesCount = this.jforumPostService.getTopicDatesCountByForum(forum.getId());
			
			if (topicDatesCount > 0)
			{
				forum.setAccessDates(null);
			}
		}
		
		List<Grade> forumTopicGrades = null;
		if (forum.getGradeType() == Grade.GradeType.FORUM.getType() || forum.getGradeType() == Grade.GradeType.DISABLED.getType())
		{
			// remove gradable topic entries in gradebook
			forumTopicGrades = jforumGradeService.getForumTopicGradesByForum(forum.getId());
		}
		
		Grade forumGrade = null;
		if (forum.getGradeType() == Grade.GradeType.TOPIC.getType() || forum.getGradeType() == Grade.GradeType.DISABLED.getType())
		{
			// remove gradable forum entries in gradebook
			forumGrade = jforumGradeService.getByForumId(forum.getId());
		}
		
		forumDao.update(forum);
		
		// clear forum from thread local cache
		clearCache(forum);
		
		// update gradebook
		if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
		{
			Grade grade = jforumGradeService.getByForumId(forum.getId());
			if (grade != null && !grade.isAddToGradeBook()) 
			{
				jforumGradeService.removeGradebookEntry(grade);
			}
		}

		if (forum.getGradeType() == Grade.GradeType.FORUM.getType() || forum.getGradeType() == Grade.GradeType.DISABLED.getType())
		{
			//Remove topic grades from gradebook
			for (Grade forumTopicGrade : forumTopicGrades)	
			{
				if (forumTopicGrade.isAddToGradeBook()) 
				{
					jforumGradeService.removeGradebookEntry(forumTopicGrade);
				}
			}
		}
		
		if (forum.getGradeType() == Grade.GradeType.TOPIC.getType() || forum.getGradeType() == Grade.GradeType.DISABLED.getType())
		{
			if (forumGrade != null)
			{
				if (forumGrade.isAddToGradeBook()) 
				{
					// remove gradable forum entry in gradebook
					jforumGradeService.removeGradebookEntry(forumGrade);
				}
			}
		}
		
		if (forum.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
		{
			// forum grade
			forumGrade = jforumGradeService.getByForumId(forum.getId());
			
			if (forumGrade != null)
			{
				if (forumGrade.isAddToGradeBook()) 
				{
					// remove gradable forum entry in gradebook
					jforumGradeService.removeGradebookEntry(forumGrade);
					jforumGradeService.modifyAddToGradeBookStatus(forumGrade.getId(), false);
				}
			}
			
			// topic grades
			List<Grade> topicGrades = jforumGradeService.getTopicGradesByForum(forum.getId());
			
			for (Grade topicGrade : topicGrades)	
			{
				if (topicGrade.isAddToGradeBook()) 
				{
					jforumGradeService.removeGradebookEntry(topicGrade);
					jforumGradeService.modifyAddToGradeBookStatus(topicGrade.getId(), false);
				}
			}
			
			return;
		}
		// update changed due date to gradable forum to topics. If invalid dates remove entry in the gradebook
		boolean datesChanged = false;
		
		datesChanged = checkDatesChanged(forum, existingForum);
		
		if (datesChanged)
		{
			if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
			{
					// check the due date changes
				if (forum.getAccessDates() != null)
				{
					if (!forum.getAccessDates().isDatesValid())
					{
						Grade grade = jforumGradeService.getByForumId(forum.getId());
						
						if (grade != null && grade.isAddToGradeBook())
						{
							boolean result = jforumGradeService.removeGradebookEntry(grade);
							
						}
						
						// clear forum from thread local cache
						clearCache(existingForum);
						return;
					}
				}
				
								
				//forum.setName(existingForum.getName());
				forum.setGradeType(existingForum.getGradeType());
			
				forumGrade = this.jforumGradeService.getByForumId(forum.getId());
				
				if (forumGrade != null && forumGrade.isAddToGradeBook())
				{
					this.jforumGradeService.updateGradebook(forumGrade, forum);
				}
			}
			else
			{
				// gradable topics of the forum in the gradebook
				if (forum.getAccessDates() != null)
				{
					// check for invalid dates
					if (forum.getAccessDates().isDatesValid())
					{
						List<Grade> topicGrades = jforumGradeService.getTopicGradesByForum(forum.getId());
						
						for (Grade topicGrade : topicGrades)	
						{
							if (topicGrade.isAddToGradeBook()) 
							{
								jforumGradeService.removeGradebookEntry(topicGrade);
							}
						}							
						return;
					}
					
					//update gradebook due dates for gradable forums and topics of the category
					forumTopicGrades = jforumGradeService.getTopicGradesByForum(forum.getId());
					
					for (Grade forumTopicGrade : forumTopicGrades)	
					{
						if (forumTopicGrade.isAddToGradeBook()) 
						{							
							if (forumTopicGrade.getType() == Grade.GradeType.TOPIC.getType())
							{
								Topic topic = jforumPostService.getTopic(forumTopicGrade.getTopicId());
								
								// update due date in the gradebook
								jforumGradeService.updateGradebook(forumTopicGrade, topic);
							}
						}
					}
				}
			}
			
		}
		else
		{
			if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
			{
				forumGrade = this.jforumGradeService.getByForumId(forum.getId());
				
				if (forumGrade != null && forumGrade.isAddToGradeBook())
				{
					this.jforumGradeService.updateGradebook(forumGrade, forum);
				}
				
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void modifyOrder(int forumId, int positionToMove, String sakaiUserId) throws JForumAccessException
	{
		if (forumId <= 0)
		{
			throw new IllegalArgumentException("To change forum information is missing");
		}
		
		if (positionToMove <= 0)
		{
			throw new IllegalArgumentException("position to move is incorrect");
		}
		
		Forum toChangeforum = getForum(forumId);
		
		if (toChangeforum == null)
		{
			throw new IllegalArgumentException("Forum is missing");
		}
		
		List<org.etudes.api.app.jforum.Forum> forums = getCategoryForums(toChangeforum.getCategoryId());
		
		if (positionToMove > forums.size())
		{
			throw new IllegalArgumentException("Postion to move is not valid");
		}		
		
		Forum otherForum = forums.get(positionToMove - 1);
		
		if (otherForum == null)
		{
			throw new IllegalArgumentException("Forum in the postion to move is not existing");
		}
		
		// access check
		boolean facilitator = jforumSecurityService.isJForumFacilitator(toChangeforum.getCategory().getContext(), sakaiUserId);
		
		if (!facilitator)
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		if (toChangeforum.getCategoryId() != otherForum.getCategoryId())
		{
			throw new IllegalArgumentException("Both forums should belong to same category");
		}
		
		forumDao.updateOrder(toChangeforum, otherForum);
		
	}
	
	public AccessDates newAccessDates(Forum forum)
	{
		// TODO: add access check
		
		if (forum == null)
		{
			return null;
		}
		
		AccessDates accessDates = new AccessDatesImpl();
		forum.setAccessDates(accessDates);
		
		return accessDates;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Forum newForum()
	{
		// TODO: add access check
		
		Forum forum = new ForumImpl();
		forum.setAccessDates(new AccessDatesImpl());
		forum.setGrade(new GradeImpl());
				
		return forum;
	}
	
	public Grade newGrade(Forum forum)
	{
		// TODO: add access check
		
		if (forum == null)
		{
			return null;
		}
		
		Grade grade = new GradeImpl();
		forum.setGrade(grade);
		
		return grade;
	}
	
	/**
	 * @param categoryDao the categoryDao to set
	 */
	public void setCategoryDao(CategoryDao categoryDao)
	{
		this.categoryDao = categoryDao;
	}
	
	/**
	 * @param forumDao the forumDao to set
	 */
	public void setForumDao(ForumDao forumDao)
	{
		this.forumDao = forumDao;
	}
	
	public void setForumMayAccessCreateTopic(Forum forum, SpecialAccess userSa)
	{
		if (forum == null)
		{
			return;
		}
		
		Date currentTime = new Date();
		
		/*set mayCreateTopicAccess. check forum and category dates, and forum type*/
		
		// forum type and forum access type
		if (forum.getType() == Forum.ForumType.READ_ONLY.getType())
		{
			((ForumImpl)forum).setMayCreateTopic(Boolean.FALSE);
			return;
		}
		else if (forum.getType() == Forum.ForumType.REPLY_ONLY.getType())
		{
			((ForumImpl)forum).setMayCreateTopic(Boolean.FALSE);
			return;
		}
		else if (forum.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
		{
			((ForumImpl)forum).setMayCreateTopic(Boolean.FALSE);
			return;
		}
		
		if (userSa != null)
		{
			// forum dates
			if (userSa.getAccessDates().getAllowUntilDate() != null)
			{
				if (userSa.getAccessDates().getAllowUntilDate().before(currentTime))
				{
					((ForumImpl)forum).setMayCreateTopic(Boolean.FALSE);
				}
				
				return;
			}
			else if (userSa.getAccessDates().getDueDate() != null)
			{
				if (userSa.getAccessDates().getDueDate().before(currentTime))
				{
					((ForumImpl)forum).setMayCreateTopic(Boolean.FALSE);
				}
				return;
			}			
		}
		else if (forum.getAccessDates() != null)
		{
			// forum dates
			if (forum.getAccessDates().getAllowUntilDate() != null)
			{
				if (forum.getAccessDates().getAllowUntilDate().before(currentTime))
				{
					((ForumImpl)forum).setMayCreateTopic(Boolean.FALSE);
				}
				
				return;
			}
			else if (forum.getAccessDates().getDueDate() != null)
			{
				if (forum.getAccessDates().getDueDate().before(currentTime))
				{
					((ForumImpl)forum).setMayCreateTopic(Boolean.FALSE);
				}
				return;
			}
		}
			
		Category category = forum.getCategory();
			
		if (category != null)
		{
			// category dates
			if (category.getAccessDates().getAllowUntilDate() != null)
			{
				if (category.getAccessDates().getAllowUntilDate().before(currentTime))
				{
					((ForumImpl)forum).setMayCreateTopic(Boolean.FALSE);
				}
			}
			else if (category.getAccessDates().getDueDate() != null)
			{
				if (category.getAccessDates().getDueDate().before(currentTime))
				{
					((ForumImpl)forum).setMayCreateTopic(Boolean.FALSE);
				}
			}
		}
	}
	
	/**
	 * @param jforumGBService the jforumGBService to set
	 */
	public void setJforumGBService(JForumGBService jforumGBService)
	{
		this.jforumGBService = jforumGBService;
	}
	
	/**
	 * @param jforumGradeService the jforumGradeService to set
	 */
	public void setJforumGradeService(JForumGradeService jforumGradeService)
	{
		this.jforumGradeService = jforumGradeService;
	}
	
	/**
	 * @param jforumPostService 
	 * 			The jforumPostService to set
	 */
	public void setJforumPostService(JForumPostService jforumPostService)
	{
		this.jforumPostService = jforumPostService;
	}
	
	/**
	 * @param jforumSecurityService the jforumSecurityService to set
	 */
	public void setJforumSecurityService(JForumSecurityService jforumSecurityService)
	{
		this.jforumSecurityService = jforumSecurityService;
	}
	
	/**
	 * @param jforumSpecialAccessService 
	 * 				The jforumSpecialAccessService to set
	 */
	public void setJforumSpecialAccessService(JForumSpecialAccessService jforumSpecialAccessService)
	{
		this.jforumSpecialAccessService = jforumSpecialAccessService;
	}
	
	/**
	 * @param jforumUserService the jforumUserService to set
	 */
	public void setJforumUserService(JForumUserService jforumUserService)
	{
		this.jforumUserService = jforumUserService;
	}
	
	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService)
	{
		this.siteService = siteService;
	}
	
	/**
	 * @param sqlService 
	 * 				The sqlService to set
	 */
	public void setSqlService(SqlService sqlService)
	{
		this.sqlService = sqlService;
	}
	
	/**
	 * @param threadLocalManager 
	 * 			The threadLocalManager to set
	 */
	public void setThreadLocalManager(ThreadLocalManager threadLocalManager)
	{
		this.threadLocalManager = threadLocalManager;
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateDates(final Forum forum)
	{
		if (forum == null)
		{
			throw new IllegalArgumentException("updateDates: forum is null");
		}
		
		Forum existingForum = getForum(forum.getId());
		
		if (existingForum == null)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("updateDates: There is no existing forum with id "+ forum.getId());
			}
			return;
		}
		
		// update if the date changes
		boolean datesChanged = false;
		
		datesChanged = checkDatesChanged(forum, existingForum);
		
		if (datesChanged)
		{
			/*forum can have dates if category or topics of the forum have no dates*/
			/*if ((forum.getAccessDates() != null) && (existingForum.getAccessDates() != null))
			{
				forum.getAccessDates().setLocked(existingForum.getAccessDates().isLocked());
			}*/
			
			int topicDatesCount = this.jforumPostService.getTopicDatesCountByForum(forum.getId());
			boolean categoryDates = false;
			
			Category category = forum.getCategory();
			if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
			{
				categoryDates = true;
			}
			
			if ((topicDatesCount > 0) || (categoryDates))
			{
				forum.getAccessDates().setOpenDate(null);
				forum.getAccessDates().setHideUntilOpen(null);
				forum.getAccessDates().setDueDate(null);
				forum.getAccessDates().setAllowUntilDate(null);
			}
			
			// if no dates remove any existing special access
			if ((forum.getAccessDates() == null) || ((forum.getAccessDates().getOpenDate() == null) && (forum.getAccessDates().getOpenDate() == null) && (forum.getAccessDates().getAllowUntilDate() == null)))
			{
				List<SpecialAccess> forumSpecialAccessList = this.jforumSpecialAccessService.getByForum(forum.getId());
				
				for (SpecialAccess specialAccess : forumSpecialAccessList)
				{
					this.jforumSpecialAccessService.delete(specialAccess.getId());
				}
			}
			
			this.sqlService.transact(new Runnable()
			{
				public void run()
				{
					updateDatesTXN(forum);
				}
			}, "updateForumDates: " + forum.getId());
			
			// clear category from thread local cache
			clearCache(existingForum);
			
			// update the due date changes in the gradebook
			if (existingForum.getGradeType() == Grade.GradeType.FORUM.getType())
			{
				Date exisDueDate = null, modDueDate = null, modOpenDate = null;
				boolean dueDateChanged = false;
				
				// check the due date changes
				if (forum.getAccessDates() != null)
				{
					modDueDate = forum.getAccessDates().getDueDate();
					modOpenDate = forum.getAccessDates().getOpenDate();
					if ((modOpenDate != null) && (modDueDate != null))
					{
						if (modOpenDate.after(modDueDate))
						{
							Grade grade = jforumGradeService.getByForumId(forum.getId());
							
							if (grade != null && grade.isAddToGradeBook())
							{
								boolean result = jforumGradeService.removeGradebookEntry(grade);
								
								/*if (result)
								{
									//update the grade for addToGradeBook
									String sql = "UPDATE jforum_grade SET add_to_gradebook = ? WHERE grade_id = ?";
									
									Object[] fields = new Object[2];
									int i = 0;
									
									fields[i++] = 0;
									fields[i++] = grade.getId();
						
									if (!sqlService.dbWrite(sql.toString(), fields)) 
									{
										throw new RuntimeException("updateGrade - add_to_gradebook : db write failed");
									}
								}*/
							}
							
							// clear forum from thread local cache
							clearCache(existingForum);
							return;
						}
					}
					
				}
				
				if (existingForum.getAccessDates() != null)
				{
					exisDueDate = existingForum.getAccessDates().getDueDate();
				}
				
				if (modDueDate == null && exisDueDate != null)
				{
					dueDateChanged = true;
				}
				else if (modDueDate != null && exisDueDate == null)
				{
					dueDateChanged = true;
				}
				else if (modDueDate == null && exisDueDate == null)
				{
					dueDateChanged = false;
				}
				else
				{
					if (!exisDueDate.equals(modDueDate))
					{
						dueDateChanged = true;
					}
				}
				
				//if (dueDateChanged) - removed to push due dates if invalid dates are corrected from course map 
				//{
				forum.setName(existingForum.getName());
				forum.setGradeType(existingForum.getGradeType());
			
				Grade forumGrade = this.jforumGradeService.getByForumId(forum.getId());
				
				if (forumGrade != null && forumGrade.isAddToGradeBook())
				{
					this.jforumGradeService.updateGradebook(forumGrade, forum);
				}				
				//}
			}
			else
			{

				Date modForumDueDate = null, modForumOpenDate = null;
				
				// gradable topics of the forum in the gradebook
				if (forum.getAccessDates() != null)
				{
					// check for invalid dates
					modForumDueDate = forum.getAccessDates().getDueDate();
					modForumOpenDate = forum.getAccessDates().getOpenDate();
					if ((modForumOpenDate != null) && (modForumDueDate != null))
					{
						if (modForumOpenDate.after(modForumDueDate))
						{
							List<Grade> topicGrades = jforumGradeService.getTopicGradesByForum(forum.getId());
							
							for (Grade topicGrade : topicGrades)	
							{
								if (topicGrade.isAddToGradeBook()) 
								{
									jforumGradeService.removeGradebookEntry(topicGrade);
								}
							}							
							return;
						}
					}
					
					//update gradebook due dates for gradable forums and topics of the category
					List<Grade> forumTopicGrades = jforumGradeService.getTopicGradesByForum(forum.getId());
					
					for (Grade forumTopicGrade : forumTopicGrades)	
					{
						if (forumTopicGrade.isAddToGradeBook()) 
						{							
							if (forumTopicGrade.getType() == Grade.GradeType.TOPIC.getType())
							{
								Topic topic = jforumPostService.getTopic(forumTopicGrade.getTopicId());
								
								// update due date in the gradebook
								jforumGradeService.updateGradebook(forumTopicGrade, topic);
							}
						}
					}
				}
			}
		}		
	}

	/**
	 * Key for caching a forum.
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return	Cache key
	 */
	protected String cacheKey(String forumId)
	{
		return "jforum:forum:" + forumId;
	}
	
	/**
	 * Check for date changes
	 * 
	 * @param forum			Forum with user input dates
	 * 
	 * @param existingForum	Existing forum 
	 * 
	 * @return	true - if dates are changed
	 * 			false - if dates are not changed
	 */
	protected boolean checkDatesChanged(Forum forum, Forum existingForum)
	{
		boolean datesChanged = false;
		
		Date exisCatOpenDate = null, exisCatDueDate = null, exisCatAllowUntilDate = null, modCatOpenDate = null, modCatDueDate = null, modCatAllowUntilDate = null;
		Boolean exisHideUntilOpen = null, modHideUntilOpen = null;
		
		if (forum.getAccessDates() != null)
		{
			modCatOpenDate = forum.getAccessDates().getOpenDate();
			modHideUntilOpen = forum.getAccessDates().isHideUntilOpen();
			modCatDueDate = forum.getAccessDates().getDueDate();
			modCatAllowUntilDate = forum.getAccessDates().getAllowUntilDate();
		}
		
		if (existingForum.getAccessDates() != null)
		{
			exisCatOpenDate = existingForum.getAccessDates().getOpenDate();
			exisHideUntilOpen = existingForum.getAccessDates().isHideUntilOpen();
			exisCatDueDate = existingForum.getAccessDates().getDueDate();
			exisCatAllowUntilDate = existingForum.getAccessDates().getAllowUntilDate();
		}
		
		/*if (exisCatOpenDate == null)
		{
			if (modCatOpenDate != null)
			{
				datesChanged = true;
			}
		}
		else
		{
			if (modCatOpenDate == null)
			{
				datesChanged = true;
			}
			else if (!modCatOpenDate.equals(exisCatOpenDate))
			{
				datesChanged = true;
			}
		}
		
		if (!datesChanged)
		{
			if (exisCatDueDate == null)
			{
				if (modCatDueDate != null)
				{
					datesChanged = true;
				}
			}
			else
			{
				if (modCatDueDate == null)
				{
					datesChanged = true;
				}
				else if (!modCatDueDate.equals(exisCatDueDate))
				{
					datesChanged = true;
				}
			}
		}*/
		
		// open date
		if (exisCatOpenDate == null)
		{
			if (modCatOpenDate != null)
			{
				datesChanged = true;
			}
		}
		else
		{
			if (modCatOpenDate == null)
			{
				datesChanged = true;
			}
			else if (!modCatOpenDate.equals(exisCatOpenDate))
			{
				datesChanged = true;
			}
			else if (modCatOpenDate.equals(exisCatOpenDate))
			{
				if (!exisHideUntilOpen.equals(modHideUntilOpen))
				{
					datesChanged = true;
				}
			}
		}
		
		// due date
		if (!datesChanged)
		{
			if (exisCatDueDate == null)
			{
				if (modCatDueDate != null)
				{
					datesChanged = true;
				}
			}
			else
			{
				if (modCatDueDate == null)
				{
					datesChanged = true;
				}
				else if (!modCatDueDate.equals(exisCatDueDate))
				{
					datesChanged = true;
				}
			}
		}
		
		// allow until date
		if (!datesChanged)
		{
			if (exisCatAllowUntilDate == null)
			{
				if (modCatAllowUntilDate != null)
				{
					datesChanged = true;
				}
			}
			else
			{
				if (modCatAllowUntilDate == null)
				{
					datesChanged = true;
				}
				else if (!modCatAllowUntilDate.equals(exisCatAllowUntilDate))
				{
					datesChanged = true;
				}
			}
		}
		return datesChanged;
	}
	
	/**
	 * Checks and marks user read status of the forum
	 * 
	 * @param forum		Forum
	 * 
	 * @param sakaiUserId	Sakai user id
	 */
	protected void checkForumUnreadTopics(Forum forum, String sakaiUserId)
	{
		Date currentTime = new Date(System.currentTimeMillis());
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
	    if (user == null)
		{
			forum.setUnread(true);
			return;
		}
	    
	    Category category = forum.getCategory();
	    
		// check marked unread topics
		int markedUnreadTopicCount  = jforumPostService.getMarkedTopicsCountByForum(forum.getId(), user.getId());
		
		if (markedUnreadTopicCount > 0)
		{
			forum.setUnread(true);
			return;
		}
		
		// marked all read time
		Date markAllReadTime = jforumUserService.getMarkAllReadTime(category.getContext(), user.getId());
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), user.getSakaiUserId());
		
		List<SpecialAccess> forumTopicsSpecialAccess = jforumSpecialAccessService.getTopicsByForumId(forum.getId());
		
		/*get all topics with latest post time and check with topic read time*/
		List<Topic> topicsLastPosttimes = null;
		
		if (markAllReadTime == null)
		{
			// check with all topics
			if (facilitator)
			{
				topicsLastPosttimes = jforumPostService.getForumTopicLatestPosttimes(forum.getId(), user.getId(), null, false);
			}
			else
			{
				topicsLastPosttimes = jforumPostService.getForumTopicLatestPosttimes(forum.getId(), user.getId(), null, true);
			}
		}
		else
		{
			/*check with topics whose latest post time is after mark all read time and not posted by the current user*/
			if (facilitator)
			{
				topicsLastPosttimes = jforumPostService.getForumTopicLatestPosttimes(forum.getId(), user.getId(), markAllReadTime, false);
			}
			else
			{
				topicsLastPosttimes = jforumPostService.getForumTopicLatestPosttimes(forum.getId(), user.getId(), markAllReadTime, true);
			}
		}

		List<Topic> lastPosttimeTopicsList = new ArrayList<Topic>();
		
		for (Topic t : topicsLastPosttimes)
		{
			if (!facilitator)
			{
				/* no special access topics and special access topics*/
				List<SpecialAccess> topicsSpecialAccessList = jforumSpecialAccessService.getTopicsByForumId(forum.getId());
				
				HashMap<Integer, SpecialAccess> userSpecialAccessAccessMap = new HashMap<Integer, SpecialAccess>();
				HashMap<Integer, SpecialAccess> userSpecialAccessNoAccessMap = new HashMap<Integer, SpecialAccess>();
				
				if (topicsSpecialAccessList.size() > 0)
				{
					for (SpecialAccess sa : topicsSpecialAccessList)
					{
						if (sa.getUserIds().contains(new Integer(user.getId())))
						{
							if (sa.getAccessDates().getOpenDate() == null)
							{
								userSpecialAccessAccessMap.put(new Integer(sa.getTopicId()), sa);
							}
							else if (currentTime.after(sa.getAccessDates().getOpenDate()))
							{
								userSpecialAccessAccessMap.put(new Integer(sa.getTopicId()), sa);
							}
							else
							{
								userSpecialAccessNoAccessMap.put(new Integer(sa.getTopicId()), sa);
							}
						}
					}
				}
				
				if (t.getAccessDates() == null)
				{
					lastPosttimeTopicsList.add(t);
				}
				else
				{
					if ((t.getAccessDates().getOpenDate() != null) || (t.getAccessDates().getDueDate() != null))
					{
						if (userSpecialAccessNoAccessMap.containsKey(t.getId()))
						{
							
						}
						else if (userSpecialAccessAccessMap.containsKey(t.getId()))
						{
							lastPosttimeTopicsList.add(t);
						}
						else if ((t.getAccessDates().getOpenDate() != null) && (t.getAccessDates().getOpenDate().after(currentTime)))
						{
							
						}
						else
						{
							lastPosttimeTopicsList.add(t);
						}
					}
					else
					{
						lastPosttimeTopicsList.add(t);
					}
				}
				
			}
			else
			{
				lastPosttimeTopicsList.add(t);
			}
			
		}
		
		// compare with visited topics time
		List<Topic> visitedTopics = null;
		
		if ((!facilitator) && (forumTopicsSpecialAccess != null) && (forumTopicsSpecialAccess.size() > 0))
		{
			visitedTopics = jforumPostService.getUserForumTopicVisittimes(forum.getId(), user.getId());
		}
		else
		{
			visitedTopics = jforumPostService.getUserForumTopicVisittimes(forum.getId(), user.getId());
		}
		
		if (lastPosttimeTopicsList.size() > visitedTopics.size())
		{
			forum.setUnread(true);
			return;
		}
		
		Map<Integer, Topic> visitedTopicsMap = new HashMap<Integer, Topic>();
		for (Topic t : visitedTopics)
		{
			visitedTopicsMap.put(Integer.valueOf(t.getId()), t);
		}
		
		for (Topic lastPosttimeTopic : lastPosttimeTopicsList)
		{
			Topic lastVistiedTopic = visitedTopicsMap.get(Integer.valueOf(lastPosttimeTopic.getId()));
			
			if (lastVistiedTopic != null)
			{
				// if topic is marked as unread mark the forum as unread
				if (!lastVistiedTopic.getRead())
				{
					forum.setUnread(true);
					return;
				}
				
				if (lastVistiedTopic.getTime().before(lastPosttimeTopic.getTime()))
				{
					forum.setUnread(true);
					return;
				}
			}
			else
			{
				forum.setUnread(true);
				return;
			}
		}
	}

	/**
	 * Check if item is accessible to the user (may be blocked item from coursemap)
	 * 
	 * @param context	The context
	 * 
	 * @param id		The item id
	 * 
	 * @param userId	The user id
	 * 
	 * @return	true - if the item is accessible to the user
	 * 			false - if the item is not accessible to the user
	 */
	protected Boolean checkItemAccess(String context, String id, String userId)
	{
		if (this.accessAdvisor == null)
			return Boolean.TRUE;
		
		return !accessAdvisor.denyAccess("sakai.jforum", context, id, userId);

	}

	/**
	 * Filter forum for the user
	 * 
	 * @param context	Context
	 * 
	 * @param forum		Forum
	 * 
	 * @param user		JForum user
	 * 
	 * @param userId	Sakai user id
	 */
	protected void checkUserForumAccess(Category category, Forum forum, String sakaiUserId) throws JForumAccessException
	{
		if (category == null || forum == null || sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			return;
		}
		
		// ignore forums with category invalid dates
		if (category.getAccessDates() != null)
		{
			if (!category.getAccessDates().isDatesValid())
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}	
		
		// ignore forums with invalid dates
		if (forum.getAccessDates() != null)
		{
			if (!forum.getAccessDates().isDatesValid())
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}				
		
		Date currentTime = new Date();
		
		Collection<org.sakaiproject.site.api.Group> userGroups = null;
		
		/* Ignore forum start date for forum special access user. 
			Show forums that have special access to the user.*/
		
		boolean specialAccessUser = false, specialAccessUserAccess = false;
		
		if (forum.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		// forum groups
		if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
		{
			boolean userInGroup = false;
			
			try
			{
				// fetch user groups only once
				if (userGroups == null)
				{
					Site site = siteService.getSite(category.getContext());
					userGroups = site.getGroupsWithMember(sakaiUserId);
				}
				
				for (org.sakaiproject.site.api.Group grp : userGroups) 
				{
					if ((forum.getGroups() != null) && (forum.getGroups().contains(grp.getId())))
					{
						userInGroup = true;
						break;
					}
				}
			} 
			catch (IdUnusedException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("checkUserForumAccess: missing site: " + category.getContext());
				}
			}

			// user not in any group
			if (!userInGroup)
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		
		// forum special access
		List<SpecialAccess> specialAccessList = forum.getSpecialAccess();
		specialAccessUser = false;
		specialAccessUserAccess = false;
		
		SpecialAccess userSa = null;
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		if (user != null)
		{
			for (SpecialAccess sa : specialAccessList)
			{
				if (sa.getUserIds().contains(new Integer(user.getId())))
				{
					userSa = sa;
					
					specialAccessUser = true;
					
					if (!sa.isOverrideStartDate())
					{
						sa.getAccessDates().setOpenDate(forum.getAccessDates().getOpenDate());
					}
					
					if (!sa.isOverrideHideUntilOpen())
					{
						sa.getAccessDates().setHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
					}
					
					if (!sa.isOverrideEndDate())
					{
						sa.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
					}
					
					/*if (!sa.isOverrideLockEndDate())
					{
						sa.getAccessDates().setLocked(forum.getAccessDates().isLocked());
					}*/
					
					if (!sa.isOverrideAllowUntilDate())
					{
					
						sa.getAccessDates().setAllowUntilDate(forum.getAccessDates().getAllowUntilDate());
					}
					
					if (sa.getAccessDates().getOpenDate() == null)
					{
						specialAccessUserAccess = true;
					}
					else
					{
						if (sa.getAccessDates().getOpenDate().after(currentTime))
						{
							if (!sa.getAccessDates().isHideUntilOpen())
							{
								specialAccessUserAccess = true;
							}
						}
						else
						{
							specialAccessUserAccess = true;
						}
					}
					break;				
				}
			}
		}
		
		forum.getSpecialAccess().clear();
		if (specialAccessUser)
		{
			forum.getSpecialAccess().add(userSa);
			
			if (specialAccessUserAccess)
			{
				
			}
			else
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		
		// forum dates
		if ((!specialAccessUser) && (forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null))
		{
			if (forum.getAccessDates().getOpenDate().after(currentTime))
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		
		// category dates
		if ((!specialAccessUser) && (category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))
		{
			if (category.getAccessDates().getOpenDate().after(currentTime))
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		
		// for gradable forum verify with course map access advisor
		if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
		{
			Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(forum.getId()), sakaiUserId);
			
			if (!checkAccess)
			{
				forum.setBlocked(true);
				forum.setBlockedByTitle(getItemAccessMessage(category.getContext(), JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(forum.getId()), sakaiUserId));
				forum.setBlockedByDetails(getItemAccessDetails(category.getContext(), JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(forum.getId()), sakaiUserId));
			}
		}
		
		setForumMayAccessCreateTopic(forum, userSa);			
		
	}
	
	/**
	 * Clear the forum from thread local cache
	 * 
	 * @param forum
	 *        The forum.
	 */
	protected void clearCache(Forum forum)
	{
		// clear the cache
		this.threadLocalManager.set(cacheKey(String.valueOf(forum.getId())), null);
	}
	
	/**
	 * Create copy of forum
	 * 
	 * @param forum		Forum
	 * 
	 * @return	The copy of forum
	 */
	protected ForumImpl clone(ForumImpl forum)
	{
		return new ForumImpl(forum);
	}
	
	
	/**
	 * Gets the item access details if the item is blocked in the coursemap
	 * 
	 * @param context	The context
	 * 
	 * @param id		The item id
	 * 
	 * @param userId	The user id
	 * 
	 * @return		The item access details
	 */
	protected String getItemAccessDetails(String context, String id, String userId)
	{
		if (this.accessAdvisor == null)
			return null;
		
		return accessAdvisor.details("sakai.jforum", context, id, userId);
	}
	
	/**
	 * Gets the message if the item is blocked in the coursemap
	 * 
	 * @param context	The context
	 * 
	 * @param id		The item id
	 * 
	 * @param userId	The user id
	 * 
	 * @return	The message if the item is blocked
	 */
	protected String getItemAccessMessage(String context, String id, String userId)
	{
		if (this.accessAdvisor == null)
			return null;
		
		 return accessAdvisor.message("sakai.jforum", context, id, userId);

	}
	
	/**
	 * Gets the user topic count
	 * 
	 * @param forum		Forum
	 * 
	 * @param sakaiUserId	Sakai user id
	 */
	protected void getUserForumTopicsCount(Forum forum, String sakaiUserId)
	{
		Category category = forum.getCategory();
		
		// for gradable forum verify with course map access advisor
		if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
		{
			Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(forum.getId()), sakaiUserId);
			
			if (!checkAccess)
			{
				forum.setBlocked(true);
				forum.setBlockedByTitle(getItemAccessMessage(category.getContext(), JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(forum.getId()), sakaiUserId));
				forum.setBlockedByDetails(getItemAccessDetails(category.getContext(), JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(forum.getId()), sakaiUserId));
			}
		}
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		// get accessible topics count
		int accessibleTopicsCount = jforumPostService.getForumTopicsAccessibleCount(forum.getId());
		
		// update messages count(messages count is topic + topic posts counts)
		int accessibleTopicsMessagesCount = jforumPostService.getForumTopicsAccessibleMessagesCount(forum.getId());
		
		/* for topic special access user add the special access topics count if the topics are not open and have access as a special access user*/						
		Date curDate = new Date(System.currentTimeMillis());
		
		if ((user == null) || ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null))))
		{
			forum.setTotalTopics(accessibleTopicsCount);
			
			forum.setTotalPosts(accessibleTopicsMessagesCount);
		}
		else
		{
			List<SpecialAccess> topicsSpecialAccessList = jforumSpecialAccessService.getTopicsByForumId(forum.getId());
			
			for (SpecialAccess sa : topicsSpecialAccessList)
			{
				if (sa.getUserIds().contains(new Integer(user.getId())))
				{
					// deduct this topic from the topics count if the start date of the topic is later than current time
					Topic topic = jforumPostService.getTopic(sa.getTopicId());
					
					// message count
					int topicPosts = topic.getTotalReplies() + 1; 	// plus one for original topic message
					
					if (topic.getAccessDates().getOpenDate() == null)
					{
						accessibleTopicsCount--;
						accessibleTopicsMessagesCount -= topicPosts;
					}
					else if (topic.getAccessDates().getOpenDate().before(curDate))
					{
						accessibleTopicsCount--;
						accessibleTopicsMessagesCount -= topicPosts;
					}
					
					// increase the topic count if the start date of special access is later than current time
					if (sa.getAccessDates().getOpenDate() == null || curDate.after(sa.getAccessDates().getOpenDate()))
					{
						accessibleTopicsCount++;
						
						accessibleTopicsMessagesCount += topicPosts;
					}
				}
			}
			forum.setTotalTopics(accessibleTopicsCount);
			forum.setTotalPosts(accessibleTopicsMessagesCount);
		}
	}
	
	/**
	 * Update forum dates
	 * 
	 * @param forum		Forum
	 */
	protected void updateDatesTXN(Forum forum)
	{
		String sql = "UPDATE jforum_forums SET start_date = ?, hide_until_open = ?, end_date = ?, allow_until_date = ? WHERE forum_id = ?";
		
		Object[] fields = new Object[5];
		int i = 0;
		
		fields[i++] = ((forum.getAccessDates() == null) || (forum.getAccessDates().getOpenDate() == null)) ? null : new Timestamp(forum.getAccessDates().getOpenDate().getTime());
		fields[i++] = (((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null && forum.getAccessDates().isHideUntilOpen() != null)) && (forum.getAccessDates().isHideUntilOpen()))? 1 : 0;
		fields[i++] = ((forum.getAccessDates() == null) || (forum.getAccessDates().getDueDate() == null)) ? null : new Timestamp(forum.getAccessDates().getDueDate().getTime());
		//fields[i++] = ((forum.getAccessDates() != null) && (forum.getAccessDates().isLocked()))? 1 : 0;
		fields[i++] = ((forum.getAccessDates() == null) || (forum.getAccessDates().getAllowUntilDate() == null)) ? null : new Timestamp(forum.getAccessDates().getAllowUntilDate().getTime());
		fields[i++] = forum.getId();

		if (!this.sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("updateDatesTXN: db write failed");
		}
	}
	
	/**
	 * Adds forum grade to the grade book
	 * 
	 * @param forum	Forum with grades
	 */
	/*protected void addToGradeBook(Forum forum)
	{
		if (forum == null || forum.getGrade() == null)
		{
			return;
		}
		
		Site site = null;
		
		Category category = forum.getCategory();
		
		try
		{
			site = siteService.getSite(category.getContext());
		}
		catch (IdUnusedException e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("updateGradeBook: missing site: " + category.getContext());
			}
		}
		
		Grade grade = forum.getGrade();
		
		//check for gradebook tool in the site
		boolean gradebookAvailable = false;
		
		try
		{
			if (site == null)
			{
				site = siteService.getSite(category.getContext());
			}
			
			String gradebookToolId = ServerConfigurationService.getString(JForumGradeService.GRADEBOOK_TOOL_ID);
			
			if ((gradebookToolId == null) || (gradebookToolId.trim().length() == 0))
			{
				gradebookToolId = "sakai.gradebook.tool";
			}
			
            if (site.getToolForCommonId(gradebookToolId) != null)
            {
            	gradebookAvailable = true;
            }
		}
		catch (IdUnusedException e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("updateGradeBook: missing site: " + category.getContext());
			}
		}
		
		if (jforumGBService.isGradebookDefined(grade.getContext()) && gradebookAvailable)
		{
			Date now = new Date();
			
			if (forum.getAccessDates() != null && forum.getAccessDates().getOpenDate() != null && forum.getAccessDates().getOpenDate().after(now) && forum.getAccessDates().isHideUntilOpen())
			{
				return;
			}
			
			Date endDate = null;
			if (forum.getAccessDates() != null)
			{
				endDate = forum.getAccessDates().getDueDate();
			}
			else if (category.getAccessDates() != null)
			{
				endDate = category.getAccessDates().getDueDate();
			}
			
			// new grade should not be in gradebook, sanity check
			boolean entryExisInGradebook = false;
			
			if (jforumGBService.isExternalAssignmentDefined(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId())))
			{
				entryExisInGradebook = true;
			}
			
			//add or update to gradebook
			String url = null;
			
			if (entryExisInGradebook)
			{
				remove entry in the gradebook and add again if there is no entry with the same name in the gradebook.
				Then publish the scores from grading page.
				jforumGBService.removeExternalAssessment(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId()));
				
			}
						
			if (!jforumGBService.isAssignmentDefined(grade.getContext(), forum.getName()))
			{
				if (!jforumGBService.addExternalAssessment(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId()), url,  forum.getName(), 
						JForumUtil.toDoubleScore(grade.getPoints()), endDate, JForumGradeService.GRADE_SENDTOGRADEBOOK_DESCRIPTION))
				{
					// update isAddToGradeBook of grade
					grade.setAddToGradeBook(false);
					jforumGradeService.modifyForumGrade(grade);
				}
			}
			else
			{
				// update isAddToGradeBook of grade
				grade.setAddToGradeBook(false);
				jforumGradeService.modifyForumGrade(grade);
			}
		}
		else
		{
			if (grade != null)
			{
				// update isAddToGradeBook of grade
				grade.setAddToGradeBook(false);
				jforumGradeService.modifyForumGrade(grade);
			}
		}
	}*/
}
