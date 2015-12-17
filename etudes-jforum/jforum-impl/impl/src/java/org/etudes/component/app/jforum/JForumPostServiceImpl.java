/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumPostServiceImpl.java $ 
 * $Id: JForumPostServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
 ***********************************************************************************/
package org.etudes.component.app.jforum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Attachment;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Evaluation;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.JForumAccessException;
import org.etudes.api.app.jforum.JForumAttachmentBadExtensionException;
import org.etudes.api.app.jforum.JForumAttachmentOverQuotaException;
import org.etudes.api.app.jforum.JForumAttachmentService;
import org.etudes.api.app.jforum.JForumCategoryService;
import org.etudes.api.app.jforum.JForumEmailExecutorService;
import org.etudes.api.app.jforum.JForumForumService;
import org.etudes.api.app.jforum.JForumGBService;
import org.etudes.api.app.jforum.JForumGradeService;
import org.etudes.api.app.jforum.JForumGradesModificationException;
import org.etudes.api.app.jforum.JForumItemEvaluatedException;
import org.etudes.api.app.jforum.JForumItemNotFoundException;
import org.etudes.api.app.jforum.JForumPostService;
import org.etudes.api.app.jforum.JForumSearchIndexingExecutorService;
import org.etudes.api.app.jforum.JForumSecurityService;
import org.etudes.api.app.jforum.JForumService;
import org.etudes.api.app.jforum.JForumSpecialAccessService;
import org.etudes.api.app.jforum.JForumUserService;
import org.etudes.api.app.jforum.LastPostInfo;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.Topic;
import org.etudes.api.app.jforum.User;
import org.etudes.api.app.jforum.dao.AttachmentDao;
import org.etudes.api.app.jforum.dao.CategoryDao;
import org.etudes.api.app.jforum.dao.ForumDao;
import org.etudes.api.app.jforum.dao.TopicDao;
import org.etudes.component.app.jforum.util.post.EmailUtil;
import org.etudes.component.app.jforum.util.post.PostUtil;
import org.etudes.util.HtmlHelper;
import org.etudes.util.XrefHelper;
import org.etudes.util.api.AccessAdvisor;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.thread_local.api.ThreadLocalManager;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.sakaiproject.util.Web;

public class JForumPostServiceImpl implements JForumPostService
{
	private static Log logger = LogFactory.getLog(JForumPostServiceImpl.class);
	
	/** Dependency (optional, self-injected): AccessAdvisor. */
	protected transient AccessAdvisor accessAdvisor = null;
	
	/** Dependency: AttachmentDao */
	protected AttachmentDao attachmentDao = null;
	
	/** Dependency: CategoryDao. */
	protected CategoryDao categoryDao = null;
	
	/** Dependency: ForumDao. */
	protected ForumDao forumDao = null;
	
	/** Dependency: JForumAttachmentService */
	protected JForumAttachmentService jforumAttachmentService = null;
	
	/** Dependency: JForumCategoryService */
	protected JForumCategoryService jforumCategoryService = null;

	/** Dependency: JForumEmailExecutorService. */
	protected JForumEmailExecutorService jforumEmailExecutorService = null;
	
	/** Dependency: JForumForumService */
	protected JForumForumService jforumForumService = null;
	
	/** Dependency:JForumGBService */
	protected JForumGBService jforumGBService = null;
	
	/** Dependency: JForumGradeService */
	protected JForumGradeService jforumGradeService = null;
	
	/** Dependency: JForumSearchIndexingExecutorService. */
	protected JForumSearchIndexingExecutorService jforumSearchIndexingExecutorService = null;
	
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
	
	/** Dependency: TopicDao */
	protected TopicDao topicDao = null;
	
	/**
	 * {@inheritDoc}
	 */
	public int createPost(Topic topic) throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		if (topic == null || topic.getId() <= 0)
		{
			throw new IllegalArgumentException("Topic is null or invalid");			
		}
		
		if (topic.getPosts().size() != 1)
		{
			throw new IllegalArgumentException("Post data is required to create topic post");
		}
		
		Post post = topic.getPosts().get(0);
		if (post.getPostedBy() == null || post.getPostedBy().getId() <= 0)
		{
			throw new IllegalArgumentException("Post data has no proper postedby information");
		}
		
		// validate attachments
		if (post.hasAttachments())
		{
			jforumAttachmentService.validatePostAttachments(post);
		}

		return createTopicPost(topic);
	}

	/**
	 * {@inheritDoc}
	 */
	public int createTopic(Topic topic) throws JForumAccessException
	{
		if (topic == null)
		{
			throw new IllegalArgumentException();			
		}
		
		if (topic.getPosts().size() != 1)
		{
			throw new IllegalArgumentException("Post data is required to create the topic");
		}
		
		if (topic.getForumId() == 0)
		{
			throw new IllegalArgumentException("Topic's forum information is required");
		}
		
		Forum forum = forumDao.selectById(topic.getForumId());
		
		if (forum == null)
		{
			throw new IllegalArgumentException("Topic forum is not a valid forum or not existing");
		}
		
		Category category = categoryDao.selectById(forum.getCategoryId());
		
		if (category == null)
		{
			throw new IllegalArgumentException("Topic forum category is not a valid category or not existing");
		}
		
		if (topic.getPostedBy() == null || topic.getPostedBy().getSakaiUserId() == null)
		{
			throw new IllegalArgumentException("Topic data has no proper postedby information");
		}
		
		String sakaiUserId = topic.getPostedBy().getSakaiUserId();
		
		// access check
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), topic.getPostedBy().getSakaiUserId());
		
		boolean participant = false;
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(category.getContext(), topic.getPostedBy().getSakaiUserId());
		}
		
		if (!(facilitator || participant))
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		Site site = null;
		if (participant)
		{
			// participants cannot create new topics if the forum has deny access etc
			if (forum.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
			{
				throw new JForumAccessException(sakaiUserId);
			}
			
			// participants cannot create new topics if the forum type is reply only or readonly
			if ((forum.getType() == Forum.ForumType.READ_ONLY.getType()) || (forum.getAccessType() == Forum.ForumType.REPLY_ONLY.getType()))
			{
				throw new JForumAccessException(sakaiUserId);
			}
			
			String userSakaiId = topic.getPostedBy().getSakaiUserId();
						
			try
			{
				site = siteService.getSite(category.getContext());
			}
			catch (IdUnusedException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("createTopic: missing site: " + category.getContext());
				}
			}
			
			// if forum access type is GROUPS. Check the user groups.
			if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
			{
				boolean userInGroup = false;
				Collection<org.sakaiproject.site.api.Group> userGroups = null;
				
				// fetch user groups only once
				if (userGroups == null && site != null)
				{
					userGroups = site.getGroupsWithMember(userSakaiId);
				}
				
				if (userGroups != null)
				{
					for (org.sakaiproject.site.api.Group grp : userGroups) 
					{
						if ((forum.getGroups() != null) && (forum.getGroups().contains(grp.getId())))
						{
							userInGroup = true;
							break;
						}
					}
				}
				
				// user not in any group
				if (!userInGroup)
				{
					throw new JForumAccessException(sakaiUserId);
				}
			}
			
			Date now = new Date();
			
			// participants cannot create new topics if the category or forum is not open.
			if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null)))
			{
				if (((category.getAccessDates().getOpenDate() != null)) && (category.getAccessDates().getOpenDate().after(now)))
				{
					throw new JForumAccessException(sakaiUserId);
				}
				
				//if ((category.getAccessDates().getDueDate() != null) && (category.getAccessDates().getDueDate().before(now)) && (category.getAccessDates().isLocked()))
				if (category.getAccessDates().getAllowUntilDate() != null)
				{
					if (category.getAccessDates().getAllowUntilDate().before(now))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
				else if (category.getAccessDates().getDueDate() != null)
				{
					if (category.getAccessDates().getDueDate().before(now))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
			}
			else if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
			{
				// forum special access
				List<SpecialAccess> specialAccessList = forum.getSpecialAccess();
				boolean specialAccessUser = false;
				boolean specialAccessUserAccess = false;

				for (SpecialAccess sa : specialAccessList)
				{
					if (sa.getUserIds().contains(new Integer(topic.getPostedBy().getId())))
					{
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

				if (specialAccessUser)
				{
					if (specialAccessUserAccess)
					{
						
					}
					else
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
				
				// forum dates
				if (!specialAccessUserAccess)
				{
					if (forum.getAccessDates().getOpenDate() != null && now.before(forum.getAccessDates().getOpenDate()))
					{
						throw new JForumAccessException(sakaiUserId);
					}
					else if (forum.getAccessDates().getAllowUntilDate() != null)
					{
						if (now.after(forum.getAccessDates().getAllowUntilDate()))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
					else if (forum.getAccessDates().getDueDate() != null)
					{
						if (now.after(forum.getAccessDates().getDueDate()))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
				}
			}
			
			// CourseMap blocker check for gradable category/forum/topic
			if (topic.isGradeTopic())
			{
				Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), sakaiUserId);
				
				if (!checkAccess)
				{
					throw new JForumAccessException(sakaiUserId);
				}
			}
			else if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
			{
				Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(forum.getId()), sakaiUserId);
				
				if (!checkAccess)
				{
					throw new JForumAccessException(sakaiUserId);
				}
			}
			else if (category.isGradable())
			{
				Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), sakaiUserId);
				
				if (!checkAccess)
				{
					throw new JForumAccessException(sakaiUserId);
				}
			}
		}
		
		// if forum/category have dates then topic should not have dates
		if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
		{
			topic.setAccessDates(null);
		}
		else if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
		{
			topic.setAccessDates(null);
		}
		
		// participants cannot add dates and grades
		if (participant)
		{
			topic.setAccessDates(null);
			topic.setGradeTopic(Boolean.FALSE);
			topic.setType(Topic.TopicType.NORMAL.getType());
			topic.setExportTopic(Boolean.FALSE);
		}
		
		//if forum/category have grades then topic is not gradable.
		if (forum.getGradeType() == Grade.GradeType.FORUM.getType() || category.isGradable())
		{
			if (topic.isGradeTopic())
			{
				topic.setGradeTopic(Boolean.FALSE);
			}
		}
		else if (forum.getGradeType() != Grade.GradeType.TOPIC.getType())
		{
			topic.setGradeTopic(Boolean.FALSE);
		}
		
		
		if (topic.isGradeTopic())
		{
			if ((topic.getGrade() != null) && (topic.getGrade().getPoints() != null))
			{
				topic.getGrade().setContext(category.getContext());
			}
			else
			{
				topic.setGradeTopic(Boolean.FALSE);
			}
		}
		
		Post post = topic.getPosts().get(0);
		
		// clean html
		String cleanedPostText = HtmlHelper.clean(post.getText(), true);
		post.setText(cleanedPostText);
		
		int topicId = topicDao.addNew(topic);
		
		// post attachments
		Post topicPost = topic.getPosts().get(0);
		
		if (topicPost.hasAttachments())
		{
			try
			{
				jforumAttachmentService.processPostAttachments(topicPost);
			}
			catch (JForumAttachmentOverQuotaException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(" Post '" + topicPost.getSubject() + "' and with id "+ topicPost.getId() +" attachment(s) are not saved. " + e.toString());
				}
			}
			catch(JForumAttachmentBadExtensionException e1)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(" Post '" + topicPost.getSubject() + "' and with id "+ topicPost.getId() +" attachment(s) are not saved. " + e1.toString());
				}
			}
		}
		
		// gradable topic add to gradebook
		if (facilitator)
		{
			((ForumImpl)forum).setCategory(category);
			((TopicImpl)topic).setForum(forum);
			
			if (topic.isGradeTopic())
			{
				updateGradeBook(topic);
			}
		}
		
		// new topic email notification
		/* if site is published and category and forum are open notify the users who opted to receive new topic notifications*/
		boolean notifyTopicToUsers = true;
		if (forum.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
		{
			notifyTopicToUsers = false;
		}
		
		// check for open and due dates
		if (notifyTopicToUsers)
		{
			Date startDate = null;
			Date endDate = null;
			
			Date nowDate = Calendar.getInstance().getTime();
			
			if ((topic.getAccessDates() != null) && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null)))
			{
				startDate = topic.getAccessDates().getOpenDate();
				endDate = topic.getAccessDates().getDueDate();
			}
			else if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null)))
			{
				startDate = forum.getAccessDates().getOpenDate();
				endDate = forum.getAccessDates().getDueDate();
			}
			else if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null)))
			{
				startDate = category.getAccessDates().getOpenDate();
				endDate = category.getAccessDates().getDueDate();
			}
			
			if (startDate != null)
			{
				if (nowDate.getTime() < startDate.getTime())
				{
					notifyTopicToUsers = false;
				}
			}
			
			if (endDate != null && notifyTopicToUsers)
			{
				if (nowDate.getTime() > endDate.getTime())
				{
					notifyTopicToUsers = false;
				}
			}
		}
		
		try
		{
			if (site == null)
			{
				site = siteService.getSite(category.getContext());
			}
			
			if (site != null && site.isPublished() && notifyTopicToUsers)
			{
				forum.getTopics().add(topic);
				category.getForums().add(forum);
				
				// notify new topic to users
				notifyNewTopicToUsers(category);
			}
		}
		catch (IdUnusedException e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("createTopic: missing site: " + category.getContext());
			}
		}
		
		// Index topic post for search
		post = topic.getPosts().get(0);
		jforumSearchIndexingExecutorService.indexPost(post);
		
		return topicId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int createTopicPost(Topic topic) throws JForumAccessException
	{
		if (topic == null || topic.getId() <= 0)
		{
			throw new IllegalArgumentException("Topic is null or invalid");			
		}
		
		if (topic.getForumId() <= 0)
		{
			throw new IllegalArgumentException("Topic's forum information is required");
		}
		
		Forum forum = forumDao.selectById(topic.getForumId());
		
		if (forum == null)
		{
			throw new IllegalArgumentException("Topic forum is not a valid forum or not existing");
		}
		
		Category category = categoryDao.selectById(forum.getCategoryId());
		
		if (category == null)
		{
			throw new IllegalArgumentException("Topic forum category is not a valid category or not existing");
		}
				
		if (topic.getPosts().size() != 1)
		{
			throw new IllegalArgumentException("Post data is required to create topic post");
		}
		
		Post post = topic.getPosts().get(0);
		if (post.getPostedBy() == null || post.getPostedBy().getId() <= 0)
		{
			throw new IllegalArgumentException("Post data has no proper postedby information");
		}
		
		((PostImpl)post).setTopicId(topic.getId());
		((PostImpl)post).setForumId(topic.getForumId());
		
		String sakaiUserId = post.getPostedBy().getSakaiUserId();
		
		// access check
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), post.getPostedBy().getSakaiUserId());
		
		boolean participant = false;
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(category.getContext(), post.getPostedBy().getSakaiUserId());
		}
		
		if (!(facilitator || participant))
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		// participants cannot reply to topic if the category/ forum/ topic is not open. check the user special access.
		Site site = null;
		if (participant)
		{
			// participants cannot reply to topic if the forum has deny access
			if (forum.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
			{
				throw new JForumAccessException(sakaiUserId);
			}
			
			String userSakaiId = post.getPostedBy().getSakaiUserId();
						
			try
			{
				site = siteService.getSite(category.getContext());
			}
			catch (IdUnusedException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("createTopicPost: missing site: " + category.getContext());
				}
			}
			
			// if forum access type is GROUPS. Check the user groups.
			if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
			{
				boolean userInGroup = false;
				Collection<org.sakaiproject.site.api.Group> userGroups = null;
				
				// fetch user groups only once
				if (userGroups == null && site != null)
				{
					userGroups = site.getGroupsWithMember(userSakaiId);
				}
				
				if (userGroups != null)
				{
					for (org.sakaiproject.site.api.Group grp : userGroups) 
					{
						if ((forum.getGroups() != null) && (forum.getGroups().contains(grp.getId())))
						{
							userInGroup = true;
							break;
						}
					}
				}
				
				// user not in any group
				if (!userInGroup)
				{
					throw new JForumAccessException(sakaiUserId);
				}
			}
			
			Date now = Calendar.getInstance().getTime();
			
			// participants cannot create new topics if the category or forum or topic is not open.
			if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
			{
				if (((category.getAccessDates().getOpenDate() != null)) && (category.getAccessDates().getOpenDate().after(now)))
				{
					throw new JForumAccessException(sakaiUserId);
				}
				
				//if ((category.getAccessDates().getDueDate() != null) && (category.getAccessDates().getDueDate().before(now)) && (category.getAccessDates().isLocked()))
				if (category.getAccessDates().getAllowUntilDate() != null)
				{
					if (category.getAccessDates().getAllowUntilDate().before(now))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
				else if (category.getAccessDates().getDueDate() != null)
				{
					if (category.getAccessDates().getDueDate().before(now))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
			}
			else if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
			{
				// forum special access
				List<SpecialAccess> specialAccessList = forum.getSpecialAccess();
				boolean specialAccessUser = false;
				boolean specialAccessUserAccess = false;

				for (SpecialAccess sa : specialAccessList)
				{
					if (sa.getUserIds().contains(new Integer(topic.getPostedBy().getId())))
					{
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

				if (specialAccessUser)
				{
					if (specialAccessUserAccess)
					{
						
					}
					else
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
				
				// forum dates
				if (!specialAccessUserAccess)
				{
					if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null))
					{
						if (forum.getAccessDates().getOpenDate().after(now))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
					
					//if ((forum.getAccessDates().getDueDate() != null) && (forum.getAccessDates().getDueDate().before(now)) && (forum.getAccessDates().isLocked()))
					if (forum.getAccessDates().getAllowUntilDate() != null)
					{
						if (forum.getAccessDates().getAllowUntilDate().before(now))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
					else if (forum.getAccessDates().getDueDate() != null)
					{
						if (forum.getAccessDates().getDueDate().before(now))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
				}
			}
			else if ((topic.getAccessDates() != null) && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null) || (topic.getAccessDates().getAllowUntilDate() != null)))
			{
				// topic special access
				List<SpecialAccess> specialAccessList = topic.getSpecialAccess();
				boolean specialAccessUser = false;
				boolean specialAccessUserAccess = false;

				for (SpecialAccess sa : specialAccessList)
				{
					if (sa.getUserIds().contains(new Integer(topic.getPostedBy().getId())))
					{
						specialAccessUser = true;
						
						if (!sa.isOverrideStartDate())
						{
							sa.getAccessDates().setOpenDate(topic.getAccessDates().getOpenDate());
						}
						
						if (!sa.isOverrideHideUntilOpen())
						{
							sa.getAccessDates().setHideUntilOpen(topic.getAccessDates().isHideUntilOpen());
						}
						
						if (!sa.isOverrideEndDate())
						{
							sa.getAccessDates().setDueDate(topic.getAccessDates().getDueDate());
						}
						
						if (!sa.isOverrideAllowUntilDate())
						{
						
							sa.getAccessDates().setAllowUntilDate(topic.getAccessDates().getAllowUntilDate());
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

				if (specialAccessUser)
				{
					if (specialAccessUserAccess)
					{
						
					}
					else
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
				
				// topic dates
				if (!specialAccessUserAccess)
				{
					if ((topic.getAccessDates() != null) && (topic.getAccessDates().getOpenDate() != null))
					{
						if (topic.getAccessDates().getOpenDate().after(now))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
					
					//if ((topic.getAccessDates().getDueDate() != null) && (topic.getAccessDates().getDueDate().before(now)) && (topic.getAccessDates().isLocked()))
					if (topic.getAccessDates().getAllowUntilDate() != null)
					{
						if (topic.getAccessDates().getAllowUntilDate().before(now))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
					else if (topic.getAccessDates().getDueDate() != null)
					{
						if (topic.getAccessDates().getDueDate().before(now))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
				}
			}
			
			// CourseMap blocker check for gradable category/forum/topic
			if (topic.isGradeTopic())
			{
				Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), sakaiUserId);
				
				if (!checkAccess)
				{
					throw new JForumAccessException(sakaiUserId);
				}
			}
			else if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
			{
				Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(forum.getId()), sakaiUserId);
				
				if (!checkAccess)
				{
					throw new JForumAccessException(sakaiUserId);
				}
			}
			else if (category.isGradable())
			{
				Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), sakaiUserId);
				
				if (!checkAccess)
				{
					throw new JForumAccessException(sakaiUserId);
				}
			}
		}
		
		// clean html
		String cleanedPostText = HtmlHelper.clean(post.getText(), true);
		post.setText(cleanedPostText);	
		
		int postId = topicDao.addNewTopicPost(topic);
		
		// post attachments
		if (post.hasAttachments())
		{
			try
			{
				jforumAttachmentService.processPostAttachments(post);
			}
			catch (JForumAttachmentOverQuotaException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(" Post '" + post.getSubject() + "' and with id "+ post.getId() +" attachment(s) are not saved. " + e.toString());
				}
			}
			catch(JForumAttachmentBadExtensionException e1)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(" Post '" + post.getSubject() + "' and with id "+ post.getId() +" attachment(s) are not saved. " + e1.toString());
				}
			}
		}
		
		//topic reply email notification for the user who are watching the topic
		/* if site is published and category, forum and topic are open notify the users who opted to watch the topic*/
		boolean notifyTopicToUsers = true;
		if (forum.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
		{
			notifyTopicToUsers = false;
		}
		
		// check for open and due dates
		if (notifyTopicToUsers)
		{
			Date startDate = null;
			Date endDate = null;
			
			Date nowDate = Calendar.getInstance().getTime();
			
			if ((topic.getAccessDates() != null) && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null)))
			{
				startDate = topic.getAccessDates().getOpenDate();
				endDate = topic.getAccessDates().getDueDate();
			}
			else if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null)))
			{
				startDate = forum.getAccessDates().getOpenDate();
				endDate = forum.getAccessDates().getDueDate();
			}
			else if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null)))
			{
				startDate = category.getAccessDates().getOpenDate();
				endDate = category.getAccessDates().getDueDate();
			}
			
			if (startDate != null)
			{
				if (nowDate.getTime() < startDate.getTime())
				{
					notifyTopicToUsers = false;
				}
			}
			
			if (endDate != null && notifyTopicToUsers)
			{
				if (nowDate.getTime() > endDate.getTime())
				{
					notifyTopicToUsers = false;
				}
			}
		}
		
		try
		{
			if (site == null)
			{
				site = siteService.getSite(category.getContext());
			}
			
			if (site != null && site.isPublished() && notifyTopicToUsers)
			{
				forum.getTopics().add(topic);
				category.getForums().add(forum);
				
				// notify post reply
				notifyTopicReplyToUsers(category);
				
			}
		}
		catch (IdUnusedException e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("createTopic: missing site: " + category.getContext());
			}
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e.toString(), e);
			}
		}
		
		try
		{
			// index post for search
			jforumSearchIndexingExecutorService.indexPost(post);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e.toString(), e);
			}
		}
		
		return postId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int createTopicWithAttachments(Topic topic) throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		if (topic == null)
		{
			throw new IllegalArgumentException();			
		}
		
		if (topic.getPosts().size() != 1)
		{
			throw new IllegalArgumentException("Post data in required to create the topic");
		}
		
		if (topic.getForumId() == 0)
		{
			throw new IllegalArgumentException("Topic's forum information is required");
		}
		
		// validate attachments
		Post topicPost = topic.getPosts().get(0); 
		if (topicPost.hasAttachments())
		{
			try
			{
				jforumAttachmentService.validatePostAttachments(topicPost);
			}
			catch (JForumAttachmentOverQuotaException e)
			{
				throw e;
			}
			catch (JForumAttachmentBadExtensionException e)
			{
				throw e;
			}
		}
	
		return createTopic(topic);
	}
	
	public void destroy()
	{
		if (logger.isInfoEnabled())
			logger.info("destroy....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void evaluateTopic(Topic topic) throws JForumAccessException
	{

		if (topic == null || !topic.isGradeTopic())
		{
			return;
		}

		Grade grade = topic.getGrade();
		
		if (grade == null)
		{
			return;
		}
		
		jforumGradeService.addModifyTopicEvaluations(grade, topic.getEvaluations());	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> getForumExportTopics(int forumId)
	{
		if (forumId <= 0) throw new IllegalArgumentException();
		
		return topicDao.selectForumExportTopics(forumId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public LastPostInfo getForumLatestPostInfo(String siteId, int forumId, String sakaiUserId)
	{
		boolean facilitator = jforumSecurityService.isJForumFacilitator(siteId, sakaiUserId);
		boolean participant = jforumSecurityService.isJForumParticipant(siteId, sakaiUserId);
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		LastPostInfo lastPostInfo = null;
		if (facilitator)
		{
			lastPostInfo = topicDao.selectForumLastPostInfo(forumId, true);
		}
		else if (participant)
		{
			lastPostInfo = topicDao.selectForumLastPostInfo(forumId, false);
			
			if (user != null && (lastPostInfo != null))
			{
				// check user special access for topics with dates
				List<SpecialAccess> topicsSpecialAccessList = jforumSpecialAccessService.getTopicsByForumId(forumId);
				
				Date curDate = new Date(System.currentTimeMillis());
				
				if (topicsSpecialAccessList.size() > 0)
				{
					for (SpecialAccess sa : topicsSpecialAccessList)
					{
						if (sa.getUserIds().contains(user.getId()))
						{
							if (sa.getAccessDates().getOpenDate() == null || curDate.after(sa.getAccessDates().getOpenDate()))
							{
								LastPostInfo topicLpi = topicDao.selectTopicLastPostInfo(sa.getTopicId());
								
								if (topicLpi.getPostTimeMillis() > lastPostInfo.getPostTimeMillis())
								{
									lastPostInfo = new LastPostInfoImpl((LastPostInfoImpl)topicLpi);
								}
							}
						}
					}
					
				}
			}
		}
		
		return lastPostInfo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> getForumTopicLatestPosttimes(int forumId, int userId, Date after, boolean topicDatesNeeded)
	{
		return topicDao.selectTopicLatestPosttimesByForum(forumId, userId, after, topicDatesNeeded);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> getForumTopics(int forumId)
	{
		if (forumId <= 0) throw new IllegalArgumentException();
				
		return topicDao.selectForumTopics(forumId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> getForumTopics(int forumId, int startFrom, int count)
	{
		if (forumId <= 0) throw new IllegalArgumentException();
		
		return topicDao.selectForumTopics(forumId, startFrom, count);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getForumTopicsAccessibleCount(int forumId)
	{
		
		return getAccessibleTopicsCountByForum(forumId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getForumTopicsAccessibleMessagesCount(int forumId)
	{
		return getAccessibleTopicsMessagesCountByForum(forumId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getForumTopicsCount(int forumId)
	{
		if (forumId <= 0) throw new IllegalArgumentException();
		
		return topicDao.selectTopicsCountByForum(forumId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMarkedTopicsCountByForum(int forumId, int userId)
	{
		return topicDao.selectMarkedTopicsCountByForum(forumId, userId);
	}

	/**
	 * {@inheritDoc}
	 */
	public Post getPost(int postId)
	{
		Post post = topicDao.selectPostById(postId);
				
		if (post != null)
		{
			PostUtil.preparePostForDisplay(post);
		}
		
		return post;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Attachment getPostAttachment(int attachmentId)
	{
		if (attachmentId <= 0) throw new IllegalArgumentException("Attachment id should be greater than 0");
		
		return attachmentDao.selectAttachmentById(attachmentId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> getRecentTopics(String context, int limit)
	{
		if ((context == null) || (limit < 0))throw new IllegalArgumentException("Limit can not be less than 0");
		
		if (limit > RECENT_TOPICS_LIMIT)
		{
			limit = RECENT_TOPICS_LIMIT;
		}
		
		return topicDao.selectRecentTopics(context, limit);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> getRecentTopics(String context, int limit, String sakaiUserId) throws JForumAccessException
	{
		if (context == null || context.trim().length() == 0)
		{
			throw new IllegalArgumentException("Not a valid site id.");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakai user Id is required.");
		}
		
		// user access check
		boolean facilitator = jforumSecurityService.isJForumFacilitator(context, sakaiUserId);
		
		boolean participant = false;
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(context, sakaiUserId);
		}
		
		if (!(facilitator || participant))
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		if (limit < 0)throw new IllegalArgumentException("Limit can not be less than 0");
		
		if (limit > RECENT_TOPICS_LIMIT)
		{
			limit = RECENT_TOPICS_LIMIT;
		}
		
		List<Topic> topics = topicDao.selectRecentTopics(context, limit);
		
		if (participant)
		{			
			// filter topics based on user topic, forum, and categories accessibility
			filterUserRecentTopics(topics, sakaiUserId);
		}
		
		// mark read or unread
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		checkUnreadTopics(context, topics, user);
		
		return topics;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Topic getTopic(int topicId)
	{
		if (topicId == 0) throw new IllegalArgumentException(); 
		
		// for thread-local caching
		String key = cacheKey(String.valueOf(topicId));
		TopicImpl cachedTopic = (TopicImpl) this.threadLocalManager.get(key);
		if (cachedTopic != null)
		{
			return this.clone(cachedTopic);
		}
		Topic topic = topicDao.selectById(topicId);
		
		// thread-local cache (copy)
		if (topic != null) this.threadLocalManager.set(key, this.clone((TopicImpl)topic));
		
		return topic;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Topic getTopic(int topicId, String sakaiUserId) throws JForumAccessException
	{
		Topic topic = getTopic(topicId);
		
		if (topic == null)
		{
			return null;
		}
		
		Forum forum = topic.getForum();
		
		if (forum == null)
		{
			return null;
		}
		
		Category category = forum.getCategory();
		
		if (category == null)
		{
			return null;
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
		
		if (facilitator)
		{
			((TopicImpl)topic).setMayPost(Boolean.TRUE);
		}
		else 
		{
			checkUserTopicAccess(topic, sakaiUserId);
		}
		
		return topic;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTopicDatesCountByCategory(int categoryId)
	{
		return topicDao.selectTopicDatesCountByCategory(categoryId);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTopicDatesCountByForum(int forumId)
	{
		return topicDao.selectTopicDatesCountByForum(forumId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Topic getTopicLatestPosttime(int topicId, int userId, Date after, boolean topicDatesNeeded)
	{
		return topicDao.selectTopicLatestPosttime(topicId, userId, after, topicDatesNeeded);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Post> getTopicPosts(int topicId, int startFrom, int count)
	{
		List<Post> posts = topicDao.selectTopicPostsByLimit(topicId, startFrom, count);
		
		for (Post post : posts)
		{
			PostUtil.preparePostForDisplay(post);
		}
		
		return posts;
		
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Post> getTopicPosts(int topicId, int startFrom, int count, String sakaiUserId) throws JForumAccessException
	{
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakai user Id is required.");
		}
		
		if (topicId <= 0) throw new IllegalArgumentException("Not a valid topic id.");
		
		Topic topic = getTopic(topicId, sakaiUserId);
		
		if (topic == null)
		{
			return new ArrayList<Post>();
		}
		
		Forum forum = jforumForumService.getForum(topic.getForumId());
		if (forum == null)
		{
			return new ArrayList<Post>();
		}
		((TopicImpl)topic).setForum(forum);
		
		Category category = jforumCategoryService.getCategory(forum.getCategoryId());
		if (category == null)
		{
			return new ArrayList<Post>();
		}
		((ForumImpl)forum).setCategory(category);
		
		// access check
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
		
		List<Post> posts = topicDao.selectTopicPostsByLimit(topicId, startFrom, count);
		
		for (Post post : posts)
		{
			((PostImpl)post).setTopic(topic);
			
			PostUtil.preparePostForDisplay(post);
			
			// edit permission
			if (facilitator)
			{
				((PostImpl)post).setCanEdit(true);
			}
			
			// posted by user posts count
			int userSitePostsCount = jforumUserService.getUserSitePostsCount(post.getPostedBy().getId(), category.getContext());
			
			((UserImpl)post.getPostedBy()).setTotalSitePosts(userSitePostsCount);
		}
		
		topic.getPosts().clear();
		topic.getPosts().addAll(posts);
		
		/*add blcokedBy details to topic, forum, category if they are gradable based on sakai user accessing posts
		check category, forum, topic dates and forum, topic special access*/
		if (participant && !posts.isEmpty())
		{
			filterUserTopicPosts(topic, sakaiUserId);
			
		}		
		
		if (!posts.isEmpty())
		{
			//update user topic read/unread
			markTopicForumReadStatus(topic, sakaiUserId);
			
			if (!forum.isUnread())
			{
				checkForumUnreadTopics(forum, sakaiUserId);
			}			
		}
		
		return posts;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> getTopics(int forumId, int startFrom, int count, String sakaiUserId) throws JForumAccessException
	{
		if (forumId <= 0) throw new IllegalArgumentException("Not a valid forum id.");
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakai user Id is required.");
		}
		
		// get forum including user forum unread status
		Forum forum = jforumForumService.getForum(forumId, sakaiUserId);
		
		if (forum == null)
		{
			return new ArrayList<Topic>();
		}
		
		Category category = forum.getCategory();
				
		if (category == null)
		{
			return new ArrayList<Topic>();
		}
		
		((CategoryImpl)category).setCurrentSakaiUserId(sakaiUserId);
		
		// access check
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
				
		List<Topic> topics = topicDao.selectForumTopics(forumId, startFrom, count);
		
		Date now = new Date();
		for (Topic topic : topics)
		{
			((TopicImpl)topic).setForum(forum);
			
			// publish topic with future open date and hidden until open grade to gradebook after the open date
			if (topic.isGradeTopic())
			{
				Grade grade = topic.getGrade();
		    	if (grade != null && grade.getItemOpenDate() != null && grade.getItemOpenDate().before(now))
		    	{
		    		jforumGradeService.updateGradebook(grade, topic);
		    	}
			}
		}
		
		forum.getTopics().clear();
		forum.getTopics().addAll(topics);
		
		if (facilitator)
		{
			User user = jforumUserService.getBySakaiUserId(sakaiUserId);
			
			// topics read status
			checkUnreadTopics(forum, user, category.getContext());
		}
		
		if (participant)
		{
			// filter user topics
			filterUserForumTopics(forum, sakaiUserId);
		}
		
		return forum.getTopics();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTotalPosts(int topicId)
	{
		if (topicId <= 0)
		{
			throw new IllegalArgumentException("Topic id is needed.");
		}
		
		return topicDao.selectPostsCountByTopic(topicId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Post> getUserCategoryPosts(int categoryId, String sakaiUserId) throws JForumAccessException
	{
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakai user Information is missing or not valid.");
		}
		
		if (categoryId <= 0) throw new IllegalArgumentException("Category information is missing or not valid.");
		
		Category category = jforumCategoryService.getCategory(categoryId);
		if (category == null)
		{
			return new ArrayList<Post>();
		}
		
		// access check
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
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		List<Post> posts = topicDao.selectPostsByCategoryByUser(categoryId, user.getId());
		
		// posted by user posts count
		int userSitePostsCount = jforumUserService.getUserSitePostsCount(user.getId(), category.getContext());
		
		for (Post post : posts)
		{
			PostUtil.preparePostForDisplay(post);
			((UserImpl)post.getPostedBy()).setTotalSitePosts(userSitePostsCount);
		}
		
		return posts;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Post> getUserForumPosts(int forumId, String sakaiUserId) throws JForumAccessException
	{
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakai user Information is missing or not valid.");
		}
		
		if (forumId <= 0) throw new IllegalArgumentException("Forum information is missing or not valid.");
		
		Forum forum = jforumForumService.getForum(forumId);
		if (forum == null)
		{
			return new ArrayList<Post>();
		}
		
		Category category = forum.getCategory();
		
		// access check
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
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		List<Post> posts = topicDao.selectPostsByForumByUser(forumId, user.getId());
		
		// posted by user posts count
		int userSitePostsCount = jforumUserService.getUserSitePostsCount(user.getId(), category.getContext());
		
		for (Post post : posts)
		{
			PostUtil.preparePostForDisplay(post);
			((UserImpl)post.getPostedBy()).setTotalSitePosts(userSitePostsCount);
		}
		
		return posts;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Topic> getUserForumTopicVisittimes(int forumId, int userId)
	{
		return topicDao.selectUserTopicVisitTimesByForum(forumId, userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Topic getUserTopicMarkTime(int topicId, String sakaiUserId)
	{
		if ((topicId == 0) || (sakaiUserId == null))
		{
			new IllegalArgumentException();
		}
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		if (user == null)
		{
			return null;
		}
		
		return topicDao.selectMarkTime(topicId, user.getId());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Post>  getUserTopicPosts(int topicId, String sakaiUserId) throws JForumAccessException
	{
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakai user Information is missing or not valid.");
		}
		
		if (topicId <= 0) throw new IllegalArgumentException("Topic information is missing or not valid.");
		
		Topic topic = getTopic(topicId);
		
		Forum forum = topic.getForum();
		if (forum == null)
		{
			return new ArrayList<Post>();
		}
		
		Category category = forum.getCategory();
		
		// access check
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
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		List<Post> posts = topicDao.selectPostsByTopicByUser(topicId, user.getId());
		
		// posted by user posts count
		int userSitePostsCount = jforumUserService.getUserSitePostsCount(user.getId(), category.getContext());
		
		for (Post post : posts)
		{
			PostUtil.preparePostForDisplay(post);
			((UserImpl)post.getPostedBy()).setTotalSitePosts(userSitePostsCount);
		}
		
		return posts;
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
	public boolean isUserSubscribedToTopic(int topicId, String sakaiUserId)
	{
		if ((topicId <= 0) || (sakaiUserId == null || sakaiUserId.trim().length() == 0))
		{
			new IllegalArgumentException("Not a valid topic id or user id.");
		}
				
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		if (user != null)
		{
			return topicDao.isUserSubscribed(topicId, user.getId());
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void lockUnlock(int[] topicId, Topic.TopicStatus status)
	{
		if (topicId.length == 0)
		{
			return;
		}
		
		for (int count = 0; count < topicId.length; count++) 
		{
			topicDao.lockUnlock(topicId[count], status);
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void markTopicRead(int topicId, String sakaiUserId, Date markTime, boolean isRead)
	{
		if ((topicId == 0) || (sakaiUserId == null) || (markTime == null))
		{
			new IllegalArgumentException();
		}
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		if (user == null)
		{
			return;
		}
		
		topicDao.updateTopicMarkTime(topicId, user.getId(), markTime, isRead);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void modifyPost(Post post) throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		if (post == null)
		{
			throw new IllegalArgumentException("Post information is missing");
		}
		
		if (post.getId() <= 0)
		{
			throw new IllegalArgumentException("Post id is missing");
		}
		
		if (post.getTopicId() <= 0)
		{
			throw new IllegalArgumentException("Post's topic is required");			
		}
		
		if (post.getForumId() <= 0)
		{
			throw new IllegalArgumentException("Topic's forum id is required");
		}
		
		Post exisPost = topicDao.selectPostById(post.getId());
		
		if (exisPost == null)
		{
			throw new IllegalArgumentException("Post not existing");
		}
		
		Topic exisTopic = topicDao.selectById(post.getTopicId());
		
		if (exisTopic == null)
		{
			throw new IllegalArgumentException("Topic is not existing");
		}
		
		if (exisTopic.getForumId() != post.getForumId())
		{
			throw new IllegalArgumentException("Post forum information cannot be verified");
		}
		
		Forum forum = forumDao.selectById(exisTopic.getForumId());
		
		if (forum == null)
		{
			throw new IllegalArgumentException("Forum is not existing");
		}
		
		Category category = categoryDao.selectById(forum.getCategoryId());
		
		if (category == null)
		{
			throw new IllegalArgumentException("Category is not existing");
		}
		
		if (post.getPostedBy() == null || post.getPostedBy().getId() <= 0)
		{
			throw new IllegalArgumentException("Post data has no proper postedby information");
		}
		
		Post existingPost = topicDao.selectPostById(post.getId());
		
		//if ((existingPost.getTopicId() != post.getTopicId()) || (existingPost.getForumId() != post.getForumId()) || (!post.getPostedBy().getSakaiUserId().trim().equalsIgnoreCase(existingPost.getPostedBy().getSakaiUserId().trim())))
		if ((existingPost.getTopicId() != post.getTopicId()) || (existingPost.getForumId() != post.getForumId()))
		{
			throw new IllegalArgumentException("Post's information is not matched with the existing post");
		}
		
		String sakaiUserId = post.getPostedBy().getSakaiUserId();
		
		// access check
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), post.getPostedBy().getSakaiUserId());
		
		boolean participant = false;
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(category.getContext(), post.getPostedBy().getSakaiUserId());
		}
		
		if (!(facilitator || participant))
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		// participant post edit permission. participants cannot edit the post if topic is locked or forum is read only or category/forum/topic has dates and locked after the end date
		if (participant)
		{
			if (!post.getPostedBy().getSakaiUserId().trim().equalsIgnoreCase(existingPost.getPostedBy().getSakaiUserId().trim()))
			{
				throw new JForumAccessException(sakaiUserId);
			}
			
			// participants cannot update post if forum has deny access
			if (forum.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
			{
				throw new JForumAccessException(sakaiUserId);
			}
			
			if (forum.getType() == Forum.ForumType.READ_ONLY.getType())
			{
				throw new JForumAccessException(sakaiUserId);
			}
			
			if (exisTopic.getStatus() == Topic.TopicStatus.LOCKED.getStatus())
			{
				throw new JForumAccessException(sakaiUserId);
			}
			
			Date currentTime = Calendar.getInstance().getTime();
			
			// category due date passed and category locked
			if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
			{
				if (category.getAccessDates().getAllowUntilDate() != null)
				{
					if (category.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
				else if (category.getAccessDates().getDueDate() != null)
				{
					//if (category.getAccessDates().getDueDate().before(currentTime) && category.getAccessDates().isLocked())
					if (category.getAccessDates().getDueDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
			}
						
			//forum dates and special access
			List<SpecialAccess> forumSpecialAccessList = forum.getSpecialAccess();
			boolean forumSpecialAccessUser = false;
			boolean forumSpecialAccessUserAccess = false;
			SpecialAccess userSa = null;
			
			User user = jforumUserService.getBySakaiUserId(sakaiUserId);
			if (user != null && ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null))))
			{
				for (SpecialAccess sa : forumSpecialAccessList)
				{
					if (sa.getUserIds().contains(new Integer(user.getId())))
					{
						userSa = sa;
						
						forumSpecialAccessUser = true;
						
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
						
						if (!sa.isOverrideAllowUntilDate())
						{
						
							sa.getAccessDates().setAllowUntilDate(forum.getAccessDates().getAllowUntilDate());
						}
						
						if (sa.getAccessDates().getOpenDate() == null)
						{
							forumSpecialAccessUserAccess = true;
						}
						else
						{
							if (sa.getAccessDates().getOpenDate().after(currentTime))
							{
								if (!sa.getAccessDates().isHideUntilOpen())
								{
									forumSpecialAccessUserAccess = true;
								}
							}
							else
							{
								forumSpecialAccessUserAccess = true;
							}
						}
						break;
					}
				}
			}
			
			forum.getSpecialAccess().clear();
			if (forumSpecialAccessUser)
			{
				forum.getSpecialAccess().add(userSa);
			}
			// forum dates
			else if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null))
			{
				if (forum.getAccessDates().getOpenDate().after(currentTime))
				{
					throw new JForumAccessException(sakaiUserId);
				}
			}
			
			// forum special access user
			if (forumSpecialAccessUser)
			{
				if (forumSpecialAccessUserAccess && userSa != null)
				{
					//if (userSa.getAccessDates().getDueDate() != null && userSa.getAccessDates().getDueDate().before(currentTime) && userSa.getAccessDates().isLocked())
					if (userSa.getAccessDates().getAllowUntilDate() != null)
					{
						if (userSa.getAccessDates().getAllowUntilDate().before(currentTime))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
					else if (userSa.getAccessDates().getDueDate() != null)
					{
						if (userSa.getAccessDates().getDueDate().before(currentTime))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
				}
			}
			// forum due date passed and forum locked
			else if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
			{
				//if (forum.getAccessDates().getDueDate().before(currentTime) && forum.getAccessDates().isLocked())
				if (forum.getAccessDates().getAllowUntilDate() != null)
				{
					if (forum.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
				else if (forum.getAccessDates().getDueDate() != null)
				{
					if (forum.getAccessDates().getDueDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
			}
						
			//topic dates and special access
			List<SpecialAccess> topicSpecialAccessList = exisTopic.getSpecialAccess();
			boolean topicSpecialAccessUser = false;
			boolean topicSpecialAccessUserAccess = false;
			SpecialAccess userTopicSa = null;
			
			if ((user != null) && ((exisTopic.getAccessDates() != null) && ((exisTopic.getAccessDates().getOpenDate() != null) || (exisTopic.getAccessDates().getDueDate() != null))))
			{
				for (SpecialAccess sa : topicSpecialAccessList)
				{
					if (sa.getUserIds().contains(new Integer(user.getId())))
					{
						userTopicSa = sa;
						
						topicSpecialAccessUser = true;
						
						if (!sa.isOverrideStartDate())
						{
							sa.getAccessDates().setOpenDate(exisTopic.getAccessDates().getOpenDate());
						}
						
						if (!sa.isOverrideHideUntilOpen())
						{
							sa.getAccessDates().setHideUntilOpen(exisTopic.getAccessDates().isHideUntilOpen());
						}
						
						if (!sa.isOverrideEndDate())
						{
							sa.getAccessDates().setDueDate(exisTopic.getAccessDates().getDueDate());
						}
						
						if (!sa.isOverrideAllowUntilDate())
						{
						
							sa.getAccessDates().setAllowUntilDate(exisTopic.getAccessDates().getAllowUntilDate());
						}
						
						if (sa.getAccessDates().getOpenDate() == null)
						{
							topicSpecialAccessUser = true;
						}
						else
						{
							if (sa.getAccessDates().getOpenDate().after(currentTime))
							{
								if (!sa.getAccessDates().isHideUntilOpen())
								{
									topicSpecialAccessUser = true;
								}
							}
							else
							{
								topicSpecialAccessUser = true;
							}
						}
						break;
					}
				}
			}
			
			exisTopic.getSpecialAccess().clear();
			if (topicSpecialAccessUser)
			{
				exisTopic.getSpecialAccess().add(userTopicSa);
			}
			// topic dates
			else if ((exisTopic.getAccessDates() != null) && (exisTopic.getAccessDates().getOpenDate() != null))
			{
				if (exisTopic.getAccessDates().getOpenDate().after(currentTime))
				{
					throw new JForumAccessException(user.getSakaiUserId());
				}
			}
			
			if (topicSpecialAccessUser)
			{
				if (topicSpecialAccessUserAccess && userTopicSa != null)
				{
					//if (userTopicSa.getAccessDates().getDueDate() != null && userTopicSa.getAccessDates().getDueDate().before(currentTime) && userTopicSa.getAccessDates().isLocked())
					if (userTopicSa.getAccessDates().getAllowUntilDate() != null)
					{
						if (userTopicSa.getAccessDates().getAllowUntilDate().before(currentTime))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
					else if (userTopicSa.getAccessDates().getDueDate() != null)
					{
						if (userTopicSa.getAccessDates().getDueDate().before(currentTime))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
				}
			}
			// topic due date passed and topic locked
			else if ((exisTopic.getAccessDates() != null) && ((exisTopic.getAccessDates().getDueDate() != null) || (exisTopic.getAccessDates().getAllowUntilDate() != null)))
			{
				//if (exisTopic.getAccessDates().getDueDate().before(currentTime) && exisTopic.getAccessDates().isLocked())
				if (exisTopic.getAccessDates().getAllowUntilDate() != null)
				{
					if (exisTopic.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
				else if (exisTopic.getAccessDates().getDueDate() != null)
				{
					if (exisTopic.getAccessDates().getDueDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
			}			
		}
		
		// validate attachments
		if (post.hasAttachments())
		{
			jforumAttachmentService.validateEditPostAttachments(post);
		}
		
		// clean html
		String cleanedPostText = HtmlHelper.clean(post.getText(), true);
		post.setText(cleanedPostText);
		
		//save modified post
		topicDao.updateTopicPost(post);
		
		//if it's first post update topic title. if facilitator update topic type,  export topic, dates, grades as needed.
		if (exisTopic.getFirstPostId() == post.getId())
		{
			Topic topic = post.getTopic();
			exisTopic.setTitle(post.getSubject());
			Grade exisGrade = null;
			
			if (facilitator)
			{
				if (exisTopic.isGradeTopic())
				{
					if (exisTopic.getGrade() != null)						
					{
						exisGrade = exisTopic.getGrade();
					}
				}
				
				/*update topic type, export topic, dates, grade etc*/				
				// topic type
				exisTopic.setType(topic.getType());

				// topic - mark for export
				exisTopic.setExportTopic(topic.isExportTopic());
				
				//topic dates - check forum and category dates
				boolean catForumDates = false;
				if ((forum.getAccessDates() != null) &&  (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))
				{
					catForumDates = true;
				}
				else if ((category.getAccessDates() != null) &&  (category.getAccessDates().getOpenDate() != null || category.getAccessDates().getDueDate() != null || category.getAccessDates().getAllowUntilDate() != null))
				{
					catForumDates = true;
				}
				
				if (catForumDates)
				{
					exisTopic.setAccessDates(null);
				}
				else
				{
					exisTopic.setAccessDates(topic.getAccessDates());
				}
				
				//topic grades - check forum and category grades
				if (topic.isGradeTopic())
				{
					if (forum.getGradeType() == Grade.GradeType.FORUM.getType() || category.isGradable())
					{
						if (exisTopic.isGradeTopic())
						{
							exisTopic.setGradeTopic(Boolean.FALSE);
						}
					}
					else if (forum.getGradeType() != Grade.GradeType.TOPIC.getType())
					{
						exisTopic.setGradeTopic(Boolean.FALSE);
					}
										
					if (exisTopic.isGradeTopic())
					{
						exisTopic.getGrade().setPoints(topic.getGrade().getPoints());
						exisTopic.getGrade().setMinimumPosts(topic.getGrade().getMinimumPosts());
						exisTopic.getGrade().setMinimumPostsRequired(topic.getGrade().isMinimumPostsRequired());
						exisTopic.getGrade().setAddToGradeBook(topic.getGrade().isAddToGradeBook());
					}
					else
					{
						if (forum.getGradeType() == Grade.GradeType.TOPIC.getType())
						{
							Grade grade = new GradeImpl();
							grade.setPoints(topic.getGrade().getPoints());
							grade.setMinimumPosts(topic.getGrade().getMinimumPosts());
							grade.setMinimumPostsRequired(topic.getGrade().isMinimumPostsRequired());
							grade.setContext(category.getContext());
							grade.setAddToGradeBook(topic.getGrade().isAddToGradeBook());
							
							exisTopic.setGradeTopic(Boolean.TRUE);
							exisTopic.setGrade(grade);
						}
					}
				}
				else
				{
					exisTopic.setGradeTopic(Boolean.FALSE);
				}
			}
			
			// save topic
			topicDao.updateTopic(exisTopic);
			
			if (facilitator)
			{
				//send or remove from grade book if gradable topic
				((ForumImpl)forum).setCategory(category);
				((TopicImpl)exisTopic).setForum(forum);
				
				if (exisTopic.isGradeTopic() && exisTopic.getGrade().isAddToGradeBook())
				{
					updateGradeBook(exisTopic);
				}
				else					
				{
					if (exisGrade != null)
					{
						removeEntryFromGradeBook(exisGrade);
					}
				}
			}
			
		}
		
		// post attachments
		jforumAttachmentService.processEditPostAttachments(post);
		
		// index post for search
		jforumSearchIndexingExecutorService.indexPost(post);
		
		// clear the cache
		clearCache(exisTopic);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void modifyTopicPost(Post post) throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException, JForumGradesModificationException
	{
		if (post.getId() <= 0)
		{
			throw new IllegalArgumentException("Post id is missing");
		}
		
		if (post.getTopicId() <= 0)
		{
			throw new IllegalArgumentException("Post's topic is required");			
		}
		
		if (post.getForumId() <= 0)
		{
			throw new IllegalArgumentException("Topic's forum id is required");
		}
		
		Post exisPost = topicDao.selectPostById(post.getId());
		
		if (exisPost == null)
		{
			throw new IllegalArgumentException("Post not existing");
		}
		
		Topic exisTopic = topicDao.selectById(post.getTopicId());
		
		if (exisTopic == null)
		{
			throw new IllegalArgumentException("Topic is not existing");
		}
		
		if (exisTopic.getForumId() != post.getForumId())
		{
			throw new IllegalArgumentException("Post forum information cannot be verified");
		}
		
		Forum forum = forumDao.selectById(exisTopic.getForumId());
		
		if (forum == null)
		{
			throw new IllegalArgumentException("Forum is not existing");
		}
		
		Category category = categoryDao.selectById(forum.getCategoryId());
		
		if (category == null)
		{
			throw new IllegalArgumentException("Category is not existing");
		}
		
		if (post.getPostedBy() == null || post.getPostedBy().getId() <= 0)
		{
			throw new IllegalArgumentException("Post data has no proper postedby information");
		}
		
		Post existingPost = topicDao.selectPostById(post.getId());
		
		//if ((existingPost.getTopicId() != post.getTopicId()) || (existingPost.getForumId() != post.getForumId()) || (!post.getPostedBy().getSakaiUserId().trim().equalsIgnoreCase(existingPost.getPostedBy().getSakaiUserId().trim())))
		if ((existingPost.getTopicId() != post.getTopicId()) || (existingPost.getForumId() != post.getForumId()))
		{
			throw new IllegalArgumentException("Post's information is not matched with the existing post");
		}
		
		String sakaiUserId = post.getPostedBy().getSakaiUserId();
		
		// access check
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), post.getPostedBy().getSakaiUserId());
		
		boolean participant = false;
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(category.getContext(), post.getPostedBy().getSakaiUserId());
		}
		
		if (!(facilitator || participant))
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		// participant post edit permission. participants cannot edit the post if topic is locked or forum is read only or category/forum/topic has dates and locked after the end date
		if (participant)
		{
			if (!post.getPostedBy().getSakaiUserId().trim().equalsIgnoreCase(existingPost.getPostedBy().getSakaiUserId().trim()))
			{
				throw new JForumAccessException(sakaiUserId);
			}
			
			// participants cannot update post if forum has deny access
			if (forum.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
			{
				throw new JForumAccessException(sakaiUserId);
			}
			
			if (forum.getType() == Forum.ForumType.READ_ONLY.getType())
			{
				throw new JForumAccessException(sakaiUserId);
			}
			
			if (exisTopic.getStatus() == Topic.TopicStatus.LOCKED.getStatus())
			{
				throw new JForumAccessException(sakaiUserId);
			}
			
			Date currentTime = Calendar.getInstance().getTime();
			
			// category due date passed and category locked
			if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
			{
				if (category.getAccessDates().getAllowUntilDate() != null)
				{
					if (category.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
				else if (category.getAccessDates().getDueDate() != null)
				{
					if (category.getAccessDates().getDueDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
			}
						
			//forum dates and special access
			List<SpecialAccess> forumSpecialAccessList = forum.getSpecialAccess();
			boolean forumSpecialAccessUser = false;
			boolean forumSpecialAccessUserAccess = false;
			SpecialAccess userSa = null;
			
			User user = jforumUserService.getBySakaiUserId(sakaiUserId);
			if (user != null && ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null))))
			{
				for (SpecialAccess sa : forumSpecialAccessList)
				{
					if (sa.getUserIds().contains(new Integer(user.getId())))
					{
						userSa = sa;
						
						forumSpecialAccessUser = true;
						
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
						
						if (!sa.isOverrideAllowUntilDate())
						{
						
							sa.getAccessDates().setAllowUntilDate(forum.getAccessDates().getAllowUntilDate());
						}
						
						if (sa.getAccessDates().getOpenDate() == null)
						{
							forumSpecialAccessUserAccess = true;
						}
						else
						{
							if (sa.getAccessDates().getOpenDate().after(currentTime))
							{
								if (!sa.getAccessDates().isHideUntilOpen())
								{
									forumSpecialAccessUserAccess = true;
								}
							}
							else
							{
								forumSpecialAccessUserAccess = true;
							}
						}
						break;
					}
				}
			}
			
			forum.getSpecialAccess().clear();
			if (forumSpecialAccessUser)
			{
				forum.getSpecialAccess().add(userSa);
			}
			// forum dates
			else if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
			{
				if (forum.getAccessDates().getAllowUntilDate() != null)
				{
					if (forum.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
				else if (forum.getAccessDates().getDueDate() != null)
				{
					if (forum.getAccessDates().getDueDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
			}
			
			// forum special access user
			if (forumSpecialAccessUser)
			{
				if (forumSpecialAccessUserAccess && userSa != null)
				{
					//if (userSa.getAccessDates().getDueDate() != null && userSa.getAccessDates().getDueDate().before(currentTime) && userSa.getAccessDates().isLocked())
					if (userSa.getAccessDates().getAllowUntilDate() != null)
					{
						if (userSa.getAccessDates().getAllowUntilDate().before(currentTime))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
					else if (userSa.getAccessDates().getDueDate() != null)
					{
						if (userSa.getAccessDates().getDueDate().before(currentTime))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
				}
			}
			// forum due date passed and forum locked
			else if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
			{
				//if (forum.getAccessDates().getDueDate().before(currentTime) && forum.getAccessDates().isLocked())
				if (forum.getAccessDates().getAllowUntilDate() != null) 
				{
					if (forum.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
				else if (forum.getAccessDates().getDueDate() != null) 
				{
					if (forum.getAccessDates().getDueDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
			}
						
			//topic dates and special access
			List<SpecialAccess> topicSpecialAccessList = exisTopic.getSpecialAccess();
			boolean topicSpecialAccessUser = false;
			boolean topicSpecialAccessUserAccess = false;
			SpecialAccess userTopicSa = null;
			
			if ((user != null) && ((exisTopic.getAccessDates() != null) && ((exisTopic.getAccessDates().getOpenDate() != null) || (exisTopic.getAccessDates().getDueDate() != null) || (exisTopic.getAccessDates().getAllowUntilDate() != null))))
			{
				for (SpecialAccess sa : topicSpecialAccessList)
				{
					if (sa.getUserIds().contains(new Integer(user.getId())))
					{
						userTopicSa = sa;
						
						topicSpecialAccessUser = true;
						
						if (!sa.isOverrideStartDate())
						{
							sa.getAccessDates().setOpenDate(exisTopic.getAccessDates().getOpenDate());
						}
						
						if (!sa.isOverrideHideUntilOpen())
						{
							sa.getAccessDates().setHideUntilOpen(exisTopic.getAccessDates().isHideUntilOpen());
						}
						
						if (!sa.isOverrideEndDate())
						{
							sa.getAccessDates().setDueDate(exisTopic.getAccessDates().getDueDate());
						}
						
						if (!sa.isOverrideAllowUntilDate())
						{
						
							sa.getAccessDates().setAllowUntilDate(exisTopic.getAccessDates().getAllowUntilDate());
						}
						
						if (sa.getAccessDates().getOpenDate() == null)
						{
							topicSpecialAccessUserAccess = true;
						}
						else
						{
							if (sa.getAccessDates().getOpenDate().after(currentTime))
							{
								if (!sa.getAccessDates().isHideUntilOpen())
								{
									topicSpecialAccessUserAccess = true;
								}
							}
							else
							{
								topicSpecialAccessUserAccess = true;
							}
						}
						break;
					}
				}
			}
			
			exisTopic.getSpecialAccess().clear();
			if (topicSpecialAccessUser)
			{
				exisTopic.getSpecialAccess().add(userTopicSa);
			}
			// topic dates
			else if ((exisTopic.getAccessDates() != null) && (exisTopic.getAccessDates().getOpenDate() != null))
			{
				if (exisTopic.getAccessDates().getOpenDate().after(currentTime))
				{
					throw new JForumAccessException(user.getSakaiUserId());
				}
			}
			
			if (topicSpecialAccessUser)
			{
				if (topicSpecialAccessUserAccess && userTopicSa != null)
				{
					//if (userTopicSa.getAccessDates().getDueDate() != null && userTopicSa.getAccessDates().getDueDate().before(currentTime) && userTopicSa.getAccessDates().isLocked())
					if (userTopicSa.getAccessDates().getAllowUntilDate() != null)
					{
						if (userTopicSa.getAccessDates().getAllowUntilDate().before(currentTime))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
					else if (userTopicSa.getAccessDates().getDueDate() != null)
					{
						if (userTopicSa.getAccessDates().getDueDate().before(currentTime))
						{
							throw new JForumAccessException(sakaiUserId);
						}
					}
				}
			}
			// topic due date passed and topic locked
			else if ((exisTopic.getAccessDates() != null) && (exisTopic.getAccessDates().getDueDate() != null || exisTopic.getAccessDates().getAllowUntilDate() != null))
			{
				if (exisTopic.getAccessDates().getAllowUntilDate() != null)
				{
					if (exisTopic.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
				else if (exisTopic.getAccessDates().getDueDate() != null)
				{
					if (exisTopic.getAccessDates().getDueDate().before(currentTime))
					{
						throw new JForumAccessException(sakaiUserId);
					}
				}
			}			
		}
		
		// validate attachments
		if (post.hasAttachments())
		{
			jforumAttachmentService.validateEditPostAttachments(post);
		}
		
		//if it's first post update topic title. if facilitator update topic type,  export topic, dates, grades as needed.
		if (exisTopic.getFirstPostId() == post.getId())
		{
			Topic topic = post.getTopic();
			exisTopic.setTitle(post.getSubject());
			Grade exisGrade = null;
			
			if (facilitator)
			{
				if (exisTopic.isGradeTopic())
				{
					if (exisTopic.getGrade() != null)						
					{
						exisGrade = exisTopic.getGrade();
					}
				}
				
				/*update topic type, export topic, dates, grade etc*/				
				// topic type
				exisTopic.setType(topic.getType());

				// topic - mark for export
				exisTopic.setExportTopic(topic.isExportTopic());
				
				//topic dates - check forum and category dates
				boolean catForumDates = false;
				if ((forum.getAccessDates() != null) &&  (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))
				{
					catForumDates = true;
				}
				else if ((category.getAccessDates() != null) &&  (category.getAccessDates().getOpenDate() != null || category.getAccessDates().getDueDate() != null || category.getAccessDates().getAllowUntilDate() != null))
				{
					catForumDates = true;
				}
				
				if (catForumDates)
				{
					exisTopic.setAccessDates(null);
				}
				else
				{
					exisTopic.setAccessDates(topic.getAccessDates());
				}
				
				//topic grades - check forum and category grades
				if (topic.isGradeTopic())
				{
					if (forum.getGradeType() == Grade.GradeType.FORUM.getType() || category.isGradable())
					{
						if (exisTopic.isGradeTopic())
						{
							exisTopic.setGradeTopic(Boolean.FALSE);
						}
					}
					else if (forum.getGradeType() != Grade.GradeType.TOPIC.getType())
					{
						exisTopic.setGradeTopic(Boolean.FALSE);
					}
										
					if (exisTopic.isGradeTopic())
					{
						exisTopic.getGrade().setPoints(topic.getGrade().getPoints());
						exisTopic.getGrade().setMinimumPosts(topic.getGrade().getMinimumPosts());
						exisTopic.getGrade().setMinimumPostsRequired(topic.getGrade().isMinimumPostsRequired());
						exisTopic.getGrade().setAddToGradeBook(topic.getGrade().isAddToGradeBook());
					}
					else
					{
						if (forum.getGradeType() == Grade.GradeType.TOPIC.getType())
						{
							Grade grade = new GradeImpl();
							grade.setPoints(topic.getGrade().getPoints());
							grade.setMinimumPosts(topic.getGrade().getMinimumPosts());
							grade.setMinimumPostsRequired(topic.getGrade().isMinimumPostsRequired());
							grade.setContext(category.getContext());
							grade.setAddToGradeBook(topic.getGrade().isAddToGradeBook());
							
							exisTopic.setGradeTopic(Boolean.TRUE);
							exisTopic.setGrade(grade);
						}
					}
				}
				else
				{
					if (exisTopic.isGradeTopic())
					{
						// if exiting gradable topic is graded don't allow to change the grade type
						List<Evaluation> topicEvaluations = jforumGradeService.getTopicEvaluations(topic.getForumId(), topic.getId());
						
						if (!topicEvaluations.isEmpty())
						{
							throw new JForumGradesModificationException("Item with title: "+ topic.getTitle() +" has been graded");
						}
					}
					exisTopic.setGradeTopic(Boolean.FALSE);
				}
			}
			
			// clean html
			String cleanedPostText = HtmlHelper.clean(post.getText(), true);
			post.setText(cleanedPostText);
			
			//save modified post
			topicDao.updateTopicPost(post);
			
			// save topic
			topicDao.updateTopic(exisTopic);
			
			if (facilitator)
			{
				//send or remove from grade book if gradable topic
				((ForumImpl)forum).setCategory(category);
				((TopicImpl)exisTopic).setForum(forum);
				
				if (exisTopic.isGradeTopic() && exisTopic.getGrade().isAddToGradeBook())
				{
					updateGradeBook(exisTopic);
				}
				else					
				{
					if (exisGrade != null)
					{
						removeEntryFromGradeBook(exisGrade);
					}
				}
			}
			
		}
		else
		{
			//save modified post
			topicDao.updateTopicPost(post);
		}
		
		// post attachments
		jforumAttachmentService.processEditPostAttachments(post);
		
		// index post for search
		jforumSearchIndexingExecutorService.indexPost(post);
		
		// clear the cache
		clearCache(exisTopic);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void moveTopic(int topicId, int toForumId, String movedBySakaiUserId) throws JForumItemNotFoundException, JForumAccessException, JForumGradesModificationException
	{
		Topic topic = getTopic(topicId);
		
		if (topic == null)
		{
			throw new JForumItemNotFoundException(topicId);
		}
		
		Forum curforum = forumDao.selectById(topic.getForumId());
		
		if (curforum == null)
		{
			throw new JForumItemNotFoundException(topic.getForumId());
		}
		
		Category category = categoryDao.selectById(curforum.getCategoryId());
		
		if (category == null)
		{
			throw new JForumItemNotFoundException(curforum.getCategoryId());
		}

		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), movedBySakaiUserId);
		
		if (!facilitator)
		{
			throw new JForumAccessException(topic.getPostedBy().getSakaiUserId());
		}
		
		/*
		 * gradable topics and gradable forum topics cannot be moved
		 */
		if (curforum.getGradeType() == Grade.GradeType.TOPIC.getType() && topic.isGradeTopic())
		{
			throw new JForumGradesModificationException("Item with title: "+ topic.getTitle() +" is gradable.");
		}
		else if (curforum.getGradeType() == Grade.GradeType.FORUM.getType())
		{
			throw new JForumGradesModificationException("Item with title: "+ topic.getTitle() +" is gradable.");
		}
		
		topicDao.moveTopic(topicId, toForumId);
		
		// clear the cache
		clearCache(topic);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Attachment newAttachment(String fileName, String contentType, String comments, byte[] fileContent)
	{
		if ((fileName == null || fileName.trim().length() == 0) ||(fileContent == null || fileContent.length == 0))
		{
			return null;
		}
		
		// Get only the filename, without the path
		String separator = "/";
		int index = fileName.indexOf(separator);

		if (index == -1)
		{
			separator = "\\";
			index = fileName.indexOf(separator);
		}

		if (index > -1)
		{
			if (separator.equals("\\"))
			{
				separator = "\\\\";
			}

			String[] p = fileName.split(separator);
			fileName = p[p.length - 1];
		}
		
		Attachment attachment = new AttachmentImpl();
		
		AttachmentInfoImpl attachmentInfo = new AttachmentInfoImpl();
		attachmentInfo.setMimetype(contentType);
		attachmentInfo.setRealFilename(fileName);
		attachmentInfo.setComment(comments);
		attachmentInfo.setFileContent(fileContent);
		
		attachment.setInfo(attachmentInfo);
		
		return attachment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Post newPost()
	{
		// TODO: add access check
		
		Post post = new PostImpl();
		post.setPostedBy(new UserImpl());
		
		return post;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Topic newTopic()
	{
		// TODO: add access check
		
		Topic topic = new TopicImpl();
		topic.setAccessDates(new AccessDatesImpl());
		topic.setGrade(new GradeImpl());
		
		return topic;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Grade newTopicGrade(Topic topic)
	{
		// TODO: add access check
		
		if (topic == null)
		{
			return null;
		}
		
		Grade grade = new GradeImpl();
		topic.setGrade(grade);
		
		return grade;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void previewPostForDisplay(Post post)
	{
		if (post == null)
		{
			return;
		}
		
		// clean html
		String cleanedPostText = HtmlHelper.clean(post.getText(), true);
		post.setText(cleanedPostText);
		
		PostUtil.preparePostForDisplay(post);
		
		/*
		 * Escapes the characters in a String using JavaScript String rules.
		 * Example adds escape character to quotes - First name: <input name=\"firstname\" size=\"20\" type=\"text\" />
		 * Else the display may not be correct
		*/
		post.setSubject(StringEscapeUtils.escapeJavaScript(post.getSubject()));
		post.setText(StringEscapeUtils.escapeJavaScript(post.getText()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removePost(int postId, String deletedBySakaiUserId) throws JForumItemNotFoundException, JForumAccessException, JForumGradesModificationException
	{
		if (postId <= 0)
		{
			throw new IllegalArgumentException("Post id is missing");
		}
		
		Post post = topicDao.selectPostById(postId);
		
		if (post == null)
		{
			throw new JForumItemNotFoundException(postId);
		}
		
		Topic topic = topicDao.selectById(post.getTopicId());
		
		if (topic == null)
		{
			throw new JForumItemNotFoundException(post.getTopicId());
		}
		
		Forum forum = forumDao.selectById(topic.getForumId());
		
		if (forum == null)
		{
			throw new JForumItemNotFoundException(topic.getForumId());
		}
		
		Category category = categoryDao.selectById(forum.getCategoryId());
		
		if (category == null)
		{
			throw new JForumItemNotFoundException(forum.getCategoryId());
		}
		
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), deletedBySakaiUserId);
		
		if (!facilitator)
		{
			throw new JForumAccessException(deletedBySakaiUserId);
		}
		
		/*
		 * If the post is first post check the grade type of the topic. If the topic's grade type
		 * is yes check for existing evaluations. If the topic's grade has evaluations, the topic cannot be deleted
		 * If the gradable forum or category has evaluations the first post cannot be deleted
		 */
		if (topic.getFirstPostId() == post.getId())
		{
			if (forum.getGradeType() == Grade.GradeType.TOPIC.getType() && topic.isGradeTopic())
			{
				/*List<Evaluation> topicEvaluations = jforumGradeService.getTopicEvaluations(topic.getForumId(), topic.getId());
				
				if (!topicEvaluations.isEmpty())
				{
					throw new JForumGradesModificationException("Item with title: "+ topic.getTitle() +" has been graded");
				}*/
				int topicEvaluationsCount = jforumGradeService.getTopicEvaluationsCount(topic.getForumId(), topic.getId());
				
				if (topicEvaluationsCount > 0)
				{
					throw new JForumGradesModificationException("Item with title: "+ topic.getTitle() +" has been graded");
				}
			}
			else if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
			{
				/*List<Evaluation> forumEvaluations = jforumGradeService.getForumEvaluations(forum.getId());
				
				if (!forumEvaluations.isEmpty())
				{
					throw new JForumGradesModificationException("Item with title: "+ forum.getName() +" has been graded");
				}*/
				int forumEvaluationsCount = jforumGradeService.getForumEvaluationsCount(forum.getId());
				
				if (forumEvaluationsCount > 0)
				{
					throw new JForumGradesModificationException("Item with title: "+ forum.getName() +" has been graded");
				}
			}
			/*else if (category.isGradable())
			{
				List<Evaluation> catEvaluations = jforumGradeService.getCategoryEvaluations(category.getId());
				
				if (!catEvaluations.isEmpty())
				{
					throw new JForumGradesModificationException("Item with title: "+ category.getTitle() +" has been graded");
				}
			}*/			
		}
		
		//delete post
		topicDao.deleteTopicPost(postId);
		
		//delete post attachments
		if (post.hasAttachments())
		{
			jforumAttachmentService.deletePostAttachments(post);			
		}
		
		//remove entry from gradebook if topic is also removed
		if (topic.getFirstPostId() == post.getId() && topic.isGradeTopic())
		{
			Grade grade = topic.getGrade();

			//jforumGBService.removeExternalAssessment(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId()));
			jforumGradeService.removeGradebookEntry(grade);
		}
		
		//clean search index related to post
		jforumSearchIndexingExecutorService.cleanIndex(post);
		
		// clear the cache
		clearCache(topic);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeTopic(int topicId, String deletedBySakaiUserId) throws JForumItemNotFoundException, JForumAccessException, JForumItemEvaluatedException
	{
		Topic topic = topicDao.selectById(topicId);
		
		if (topic == null)
		{
			throw new JForumItemNotFoundException(topicId);
		}
		
		Forum forum = forumDao.selectById(topic.getForumId());
		
		if (forum == null)
		{
			throw new JForumItemNotFoundException(topic.getForumId());
		}
		
		Category category = categoryDao.selectById(forum.getCategoryId());
		
		if (category == null)
		{
			throw new JForumItemNotFoundException(forum.getCategoryId());
		}
		
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), deletedBySakaiUserId);
		
		if (!facilitator)
		{
			throw new JForumAccessException(deletedBySakaiUserId);
		}
		
		/*
		 * For gradable topic check for existing evaluations. If the topic's grade has evaluations, the topic cannot be deleted
		 * If the gradable forum has evaluations the topic cannot be deleted
		 */
		if (forum.getGradeType() == Grade.GradeType.TOPIC.getType() && topic.isGradeTopic())
		{
			int topicEvaluationsCount = jforumGradeService.getTopicEvaluationsCount(topic.getForumId(), topic.getId());
			
			if (topicEvaluationsCount > 0)
			{
				throw new JForumItemEvaluatedException(topic.getTitle());
			}
		}
		else if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
		{
			int forumEvaluationsCount = jforumGradeService.getForumEvaluationsCount(forum.getId());
			
			if (forumEvaluationsCount > 0)
			{
				throw new JForumItemEvaluatedException(forum.getName());
			}
		}
		
		List<Post> topicPosts = getTopicPosts(topicId, 0, 0);
		
		// delete topic
		topicDao.deleteTopic(topicId);
		
		// delete topic post attachments
		for (Post post : topicPosts)
		{
			if (post.hasAttachments())
			{
				jforumAttachmentService.deletePostAttachments(post);			
			}
		}
		
		// remove entry from gradebook if topic is also removed
		if (topic.isGradeTopic())
		{
			Grade grade = topic.getGrade();

			jforumGBService.removeExternalAssessment(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId()));
		}
		
		// clear the cache
		clearCache(topic);
		
		// clean search index related to post
		for (Post post : topicPosts)
		{
			jforumSearchIndexingExecutorService.cleanIndex(post);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeTopicSubscription(int topicId)
	{
		if (topicId <= 0)
		{
			new IllegalArgumentException("Not a valid topic id.");
		}
		
		topicDao.removeSubscription(topicId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeUserTopicSubscription(int topicId, String sakaiUserId)
	{
		if ((topicId <= 0) || (sakaiUserId == null || sakaiUserId.trim().length() == 0))
		{
			new IllegalArgumentException("Not a valid topic id or user id.");
		}
				
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		if (user != null)
		{
			topicDao.removeUserSubscription(topicId, user.getId());		
		}			
	}
	
	/**
	 * @param attachmentDao the attachmentDao to set
	 */
	public void setAttachmentDao(AttachmentDao attachmentDao)
	{
		this.attachmentDao = attachmentDao;
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

	/**
	 * @param jforumAttachmentService the jforumAttachmentService to set
	 */
	public void setJforumAttachmentService(JForumAttachmentService jforumAttachmentService)
	{
		this.jforumAttachmentService = jforumAttachmentService;
	}
	
	/**
	 * @param jforumCategoryService the jforumCategoryService to set
	 */
	public void setJforumCategoryService(JForumCategoryService jforumCategoryService)
	{
		this.jforumCategoryService = jforumCategoryService;
	}
	
	/**
	 * @param jforumEmailExecutorService the jforumEmailExecutorService to set
	 */
	public void setJforumEmailExecutorService(JForumEmailExecutorService jforumEmailExecutorService)
	{
		this.jforumEmailExecutorService = jforumEmailExecutorService;
	}
	
	/**
	 * @param jforumForumService the jforumForumService to set
	 */
	public void setJforumForumService(JForumForumService jforumForumService)
	{
		this.jforumForumService = jforumForumService;
	}
	
	/**
	 * @param jforumGBService the jforumGBService to set
	 */
	public void setJforumGBService(JForumGBService jforumGBService)
	{
		this.jforumGBService = jforumGBService;
	}
	
	/**
	 * @param jforumGradeService 
	 * 			The jforumGradeService to set
	 */
	public void setJforumGradeService(JForumGradeService jforumGradeService)
	{
		this.jforumGradeService = jforumGradeService;
	}
		
	/**
	 * @param jforumSearchIndexingExecutorService the jforumSearchIndexingExecutorService to set
	 */
	public void setJforumSearchIndexingExecutorService(JForumSearchIndexingExecutorService jforumSearchIndexingExecutorService)
	{
		this.jforumSearchIndexingExecutorService = jforumSearchIndexingExecutorService;
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
	 * 			The jforumSpecialAccessService to set
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
	 * 			The sqlService to set
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
	 * @param topicDao the topicDao to set
	 */
	public void setTopicDao(TopicDao topicDao)
	{
		this.topicDao = topicDao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void subscribeUserToTopic(int topicId, String sakaiUserId)
	{
		if ((topicId <= 0) || (sakaiUserId == null || sakaiUserId.trim().length() == 0))
		{
			new IllegalArgumentException("Not a valid topic id or user id.");
		}
				
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		if (user != null)
		{
			topicDao.subscribeUser(topicId, user.getId());			
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateDates(final Topic topic)
	{
		if (topic == null)
		{
			throw new IllegalArgumentException("updateDates: forum is null");
		}
		
		Topic existingTopic = getTopic(topic.getId());
		
		if (existingTopic == null)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("updateDates: There is no existing topic with id "+ topic.getId());
			}
			return;
		}
		
		// update if the date changes
		boolean datesChanged = false;
		
		datesChanged = checkDatesChanged(topic, existingTopic);
		
		if (datesChanged)
		{
			topic.setForumId(existingTopic.getForumId());
			
			boolean forumDates = false, categoryDates = false;
			
			Forum existingForum = existingTopic.getForum();
			if ((existingForum != null) && (existingForum.getAccessDates() != null))
			{
				if ((existingForum.getAccessDates().getOpenDate() != null) || (existingForum.getAccessDates().getDueDate() != null) || (existingForum.getAccessDates().getAllowUntilDate() != null))
				{
					forumDates = true;
				}						
			}
			
			Category existingcategory = existingForum.getCategory();
			
			if ((existingcategory != null) && (existingcategory.getAccessDates() != null))
			{
				if ((existingcategory.getAccessDates().getOpenDate() != null) || (existingcategory.getAccessDates().getDueDate() != null) || (existingcategory.getAccessDates().getAllowUntilDate() != null))
				{
					categoryDates = true;
				}						
			}
			
			if ((forumDates) || (categoryDates))
			{
				topic.getAccessDates().setOpenDate(null);
				topic.getAccessDates().setHideUntilOpen(null);
				topic.getAccessDates().setDueDate(null);
				topic.getAccessDates().setAllowUntilDate(null);
			}
			
			// topic can have dates if category and forum have no dates			
			if ((topic.getAccessDates() != null) && (existingTopic.getAccessDates() != null))
			{
				//topic.getAccessDates().setLocked(existingTopic.getAccessDates().isLocked());
			}
			
			// if no dates remove any existing special access. Gradable topics with dates can have special access.
			if ((topic.getAccessDates() == null) || ((topic.getAccessDates().getOpenDate() == null) && (topic.getAccessDates().getDueDate() == null) && (topic.getAccessDates().getAllowUntilDate() == null)))
			{
				List<SpecialAccess> forumSpecialAccessList = this.jforumSpecialAccessService.getByTopic(topic.getForumId(), topic.getId());
				
				for (SpecialAccess specialAccess : forumSpecialAccessList)
				{
					this.jforumSpecialAccessService.delete(specialAccess.getId());
				}
			}
			
			this.sqlService.transact(new Runnable()
			{
				public void run()
				{
					updateDatesTXN(topic);
				}
			}, "updateTopicDates: " + topic.getId());
			
			
			//Date exisDueDate = null, modDueDate = null, modOpenDate = null, modAllowUntilDate = null;
			//boolean dueDateChanged = false;
			Date modDueDate = null, modOpenDate = null, modAllowUntilDate = null;
			
			// check the due date changes
			if (topic.getAccessDates() != null)
			{
				modDueDate = topic.getAccessDates().getDueDate();
				modOpenDate = topic.getAccessDates().getOpenDate();
				modAllowUntilDate = topic.getAccessDates().getAllowUntilDate();
				
				if ((modOpenDate != null) || (modDueDate != null) || (modAllowUntilDate != null))
				{
					if (!topic.getAccessDates().isDatesValid())
					{
						Grade grade = jforumGradeService.getByForumTopicId(topic.getForumId(), topic.getId());
						
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
						return;
					}
				}
				
			}
			
			/*if (existingTopic.getAccessDates() != null)
			{
				exisDueDate = existingTopic.getAccessDates().getDueDate();
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
			}*/
			
			//if (dueDateChanged)
			//{
			topic.setTitle(existingTopic.getTitle());
			topic.setGradeTopic(existingTopic.isGradeTopic());
		
			Grade topicGrade = this.jforumGradeService.getByForumTopicId(topic.getForumId(), topic.getId());
			
			if (topicGrade != null && topicGrade.isAddToGradeBook())
			{
				this.jforumGradeService.updateGradebook(topicGrade, topic);
			}				
			//}

		}
		
		// clear the cache
		clearCache(topic);
	}
	
	/**
	 * Key for caching a topic.
	 * 
	 * @param topicId	Topic id
	 * 
	 * @return	Cache key
	 */
	protected String cacheKey(String topicId)
	{
		return "jforum:topic:" + topicId;
	}

	/**
	 * Check for date changes
	 * 
	 * @param topic			Topic
	 * 
	 * @param existingTopic	Existing topic to compare
	 * 
	 * @return	true - if dates are changed
	 * 			false - if dates are not changed
	 */
	protected boolean checkDatesChanged(Topic topic, Topic existingTopic)
	{
		boolean datesChanged = false;
		
		/*Date exisOpenDate = null, exisDueDate = null, modOpenDate = null, modDueDate = null;
		
		if (topic.getAccessDates() != null)
		{
			modOpenDate = topic.getAccessDates().getOpenDate();
			modDueDate = topic.getAccessDates().getDueDate();
		}
		
		if (existingTopic.getAccessDates() != null)
		{
			exisOpenDate = existingTopic.getAccessDates().getOpenDate();
			exisDueDate = existingTopic.getAccessDates().getDueDate();
		}
		
		if (exisOpenDate == null)
		{
			if (modOpenDate != null)
			{
				datesChanged = true;
			}
		}
		else
		{
			if (modOpenDate == null)
			{
				datesChanged = true;
			}
			else if (!modOpenDate.equals(exisOpenDate))
			{
				datesChanged = true;
			}
		}
		
		if (!datesChanged)
		{
			if (exisDueDate == null)
			{
				if (modDueDate != null)
				{
					datesChanged = true;
				}
			}
			else
			{
				if (modDueDate == null)
				{
					datesChanged = true;
				}
				else if (!modDueDate.equals(exisDueDate))
				{
					datesChanged = true;
				}
			}
		}*/
		
		Date exisOpenDate = null, exisDueDate = null, exisAllowUntilDate = null, modOpenDate = null, modDueDate = null, modAllowUntilDate = null;
		Boolean exisHideUntilOpen = null, modHideUntilOpen = null;
		
		if (topic.getAccessDates() != null)
		{
			modOpenDate = topic.getAccessDates().getOpenDate();
			modHideUntilOpen = topic.getAccessDates().isHideUntilOpen();
			modDueDate = topic.getAccessDates().getDueDate();
			modAllowUntilDate = topic.getAccessDates().getAllowUntilDate();
		}
		
		if (existingTopic.getAccessDates() != null)
		{
			exisOpenDate = existingTopic.getAccessDates().getOpenDate();
			exisHideUntilOpen = existingTopic.getAccessDates().isHideUntilOpen();
			exisDueDate = existingTopic.getAccessDates().getDueDate();
			exisAllowUntilDate = existingTopic.getAccessDates().getAllowUntilDate();
		}
		
		// open date
		if (exisOpenDate == null)
		{
			if (modOpenDate != null)
			{
				datesChanged = true;
			}
		}
		else
		{
			if (modOpenDate == null)
			{
				datesChanged = true;
			}
			else if (!modOpenDate.equals(exisOpenDate))
			{
				datesChanged = true;
			}
			else if (modOpenDate.equals(exisOpenDate))
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
			if (exisDueDate == null)
			{
				if (modDueDate != null)
				{
					datesChanged = true;
				}
			}
			else
			{
				if (modDueDate == null)
				{
					datesChanged = true;
				}
				else if (!modDueDate.equals(exisDueDate))
				{
					datesChanged = true;
				}
			}
		}
		
		// allow until date
		if (!datesChanged)
		{
			if (exisAllowUntilDate == null)
			{
				if (modAllowUntilDate != null)
				{
					datesChanged = true;
				}
			}
			else
			{
				if (modAllowUntilDate == null)
				{
					datesChanged = true;
				}
				else if (!modAllowUntilDate.equals(exisAllowUntilDate))
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
		int markedUnreadTopicCount  = getMarkedTopicsCountByForum(forum.getId(), user.getId());
		
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
				topicsLastPosttimes = getForumTopicLatestPosttimes(forum.getId(), user.getId(), null, false);
			}
			else
			{
				topicsLastPosttimes = getForumTopicLatestPosttimes(forum.getId(), user.getId(), null, true);
			}
		}
		else
		{
			/*check with topics whose latest post time is after mark all read time and not posted by the current user*/
			if (facilitator)
			{
				topicsLastPosttimes = getForumTopicLatestPosttimes(forum.getId(), user.getId(), markAllReadTime, false);
			}
			else
			{
				topicsLastPosttimes = getForumTopicLatestPosttimes(forum.getId(), user.getId(), markAllReadTime, true);
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
			visitedTopics = getUserForumTopicVisittimes(forum.getId(), user.getId());
		}
		else
		{
			visitedTopics = getUserForumTopicVisittimes(forum.getId(), user.getId());
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
	 * check topic dates for the user. If the topics are accessible to the user they are removed from the list
	 * 
	 * @param topics	The topics
	 * 
	 * @param user	Jforum user
	 */
	protected void checkTopicDates(List<Topic> topics, User user)
	{
		Date currentTime = Calendar.getInstance().getTime();
		
		for (Iterator<Topic> topicIter = topics.iterator(); topicIter.hasNext();) 
		{
		    Topic topic = topicIter.next();
		    
		    if (topic.getStatus() == Topic.TopicStatus.LOCKED.getStatus())
			{
				((TopicImpl)topic).setMayPost(Boolean.FALSE);
			}
		    
			// topic special access
			List<SpecialAccess> topicSpecialAccessList = topic.getSpecialAccess();
			boolean topicSpecialAccessUser = false;
			boolean topicSpecialAccessUserAccess = false;
			SpecialAccess userSa = null;
			
			if ((user != null) && ((topic.getAccessDates() != null) && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null) || (topic.getAccessDates().getAllowUntilDate() != null))))
			{
				for (SpecialAccess sa : topicSpecialAccessList)
				{
					if (sa.getUserIds().contains(new Integer(user.getId())))
					{
						userSa = sa;
						
						topicSpecialAccessUser = true;
						
						if (!sa.isOverrideStartDate())
						{
							sa.getAccessDates().setOpenDate(topic.getAccessDates().getOpenDate());
						}
						
						if (!sa.isOverrideHideUntilOpen())
						{
							sa.getAccessDates().setHideUntilOpen(topic.getAccessDates().isHideUntilOpen());
						}
						
						if (!sa.isOverrideEndDate())
						{
							sa.getAccessDates().setDueDate(topic.getAccessDates().getDueDate());
						}
						
						/*if (!sa.isOverrideLockEndDate())
						{
							sa.getAccessDates().setLocked(topic.getAccessDates().isLocked());
						}*/
						
						if (!sa.isOverrideAllowUntilDate())
						{
						
							sa.getAccessDates().setAllowUntilDate(topic.getAccessDates().getAllowUntilDate());
						}
						
						if (sa.getAccessDates().getOpenDate() == null)
						{
							topicSpecialAccessUserAccess = true;
						}
						else
						{
							if (sa.getAccessDates().getOpenDate().after(currentTime))
							{
								if (!sa.getAccessDates().isHideUntilOpen())
								{
									topicSpecialAccessUserAccess = true;
								}
							}
							else
							{
								topicSpecialAccessUserAccess = true;
							}
						}
						
						break;
					}
				}
			}
			
			topic.getSpecialAccess().clear();
			if (topicSpecialAccessUser)
			{
				topic.getSpecialAccess().add(userSa);
				
				if (topicSpecialAccessUserAccess)
				{
					//if (topic.mayPost() && userSa.getAccessDates().getDueDate() != null && userSa.getAccessDates().isLocked())
					if (topic.mayPost())
					{
						if (userSa.getAccessDates().getAllowUntilDate() != null)
						{
							if (userSa.getAccessDates().getAllowUntilDate().after(currentTime))
							{
								((TopicImpl)topic).setMayPost(Boolean.FALSE);
							}
						}
						else if (userSa.getAccessDates().getDueDate() != null)
						{
							if (userSa.getAccessDates().getDueDate().after(currentTime))
							{
								((TopicImpl)topic).setMayPost(Boolean.FALSE);
							}
						}
					}
					continue;
				}
				else
				{
					topicIter.remove();
					continue;
				}
			}
			// topic dates
			else if ((topic.getAccessDates() != null) && (topic.getAccessDates().getOpenDate() != null))
			{
				if (topic.getAccessDates().getOpenDate().after(currentTime) && topic.getAccessDates().isHideUntilOpen())
				{
					topicIter.remove();
					continue;
				}
			}
			
			//if (topic.mayPost() && (!topicSpecialAccessUserAccess) && ((topic.getAccessDates().getDueDate()) != null && (topic.getAccessDates().isLocked())))
			if (topic.mayPost() && topic.getAccessDates() != null && (!topicSpecialAccessUserAccess))
			{
				if (topic.getAccessDates().getAllowUntilDate() != null)
				{
					if (topic.getAccessDates().getAllowUntilDate().after(currentTime))
					{
						((TopicImpl)topic).setMayPost(Boolean.FALSE);
					}
				}
				else if (topic.getAccessDates().getDueDate() != null)
				{
					if (topic.getAccessDates().getDueDate().after(currentTime))
					{
						((TopicImpl)topic).setMayPost(Boolean.FALSE);
					}
				}
			}			
		}
	}

	/**
	 * check topic dates for the use
	 * 
	 * @param topic	The topic
	 * 
	 * @param user	Jforum user
	 * 
	 * @throws JForumAccessException	if user is not allowed to access topic
	 */
	protected void checkTopicDates(Topic topic, User user) throws JForumAccessException
	{
		Date currentTime = Calendar.getInstance().getTime();
		
		if (topic.getStatus() == Topic.TopicStatus.LOCKED.getStatus())
		{
			((TopicImpl)topic).setMayPost(Boolean.FALSE);
		}
		
		// topic special access
		List<SpecialAccess> topicSpecialAccessList = topic.getSpecialAccess();
		boolean topicSpecialAccessUser = false;
		boolean topicSpecialAccessUserAccess = false;
		SpecialAccess userSa = null;
		
		if ((user != null) && ((topic.getAccessDates() != null) && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null) || (topic.getAccessDates().getAllowUntilDate() != null))))
		{
			for (SpecialAccess sa : topicSpecialAccessList)
			{
				if (sa.getUserIds().contains(new Integer(user.getId())))
				{
					userSa = sa;
					
					topicSpecialAccessUser = true;
					
					if (!sa.isOverrideStartDate())
					{
						sa.getAccessDates().setOpenDate(topic.getAccessDates().getOpenDate());
					}
					
					if (!sa.isOverrideHideUntilOpen())
					{
						sa.getAccessDates().setHideUntilOpen(topic.getAccessDates().isHideUntilOpen());
					}
					
					if (!sa.isOverrideEndDate())
					{
						sa.getAccessDates().setDueDate(topic.getAccessDates().getDueDate());
					}
					
					/*if (!sa.isOverrideLockEndDate())
					{
						sa.getAccessDates().setLocked(topic.getAccessDates().isLocked());
					}*/
					
					if (!sa.isOverrideAllowUntilDate())
					{
					
						sa.getAccessDates().setAllowUntilDate(topic.getAccessDates().getAllowUntilDate());
					}
					
					if (sa.getAccessDates().getOpenDate() == null)
					{
						topicSpecialAccessUserAccess = true;
					}
					else
					{
						if (sa.getAccessDates().getOpenDate().after(currentTime))
						{
							if (!sa.getAccessDates().isHideUntilOpen())
							{
								topicSpecialAccessUserAccess = true;
							}
						}
						else
						{
							topicSpecialAccessUserAccess = true;
						}
					}
					break;
				}
			}
		}
		
		topic.getSpecialAccess().clear();
		if (topicSpecialAccessUser)
		{
			topic.getSpecialAccess().add(userSa);
			
			if (topicSpecialAccessUserAccess)
			{
				//if (topic.mayPost() && userSa.getAccessDates().getDueDate() != null && userSa.getAccessDates().isLocked())
				if (topic.mayPost())
				{
					if (userSa.getAccessDates().getAllowUntilDate() != null)
					{
						if (userSa.getAccessDates().getAllowUntilDate().before(currentTime))
						{
							((TopicImpl)topic).setMayPost(Boolean.FALSE);
						}
					}
					else if (userSa.getAccessDates().getDueDate() != null)
					{
						if (userSa.getAccessDates().getDueDate().before(currentTime))
						{
							((TopicImpl)topic).setMayPost(Boolean.FALSE);
						}
					}
				}
				return;
			}
			else
			{
				throw new JForumAccessException(user.getSakaiUserId());
			}
		}
		// topic dates
		else if ((topic.getAccessDates() != null) && (topic.getAccessDates().getOpenDate() != null))
		{
			if (topic.getAccessDates().getOpenDate().after(currentTime))
			{
				throw new JForumAccessException(user.getSakaiUserId());
			}
		}
		
		//if (topic.mayPost() && (!topicSpecialAccessUserAccess) && ((topic.getAccessDates().getDueDate()) != null && (topic.getAccessDates().isLocked())))
		if (topic.mayPost() && topic.getAccessDates() != null && (!topicSpecialAccessUserAccess))
		{
			if (topic.getAccessDates().getAllowUntilDate() != null)
			{
				if (topic.getAccessDates().getAllowUntilDate().before(currentTime))
				{
					((TopicImpl)topic).setMayPost(Boolean.FALSE);
				}
			}
			else if (topic.getAccessDates().getDueDate() != null)
			{
				if (topic.getAccessDates().getDueDate().before(currentTime))
				{
					((TopicImpl)topic).setMayPost(Boolean.FALSE);
				}
			}
		}
	}

	/**
	 * Check topic and mark the read status
	 * 
	 * @param forum		Forum
	 * 
	 * @param user		Jforum user
	 * 
	 * @param siteId	Course/site id
	 */
	protected void checkUnreadTopics(Forum forum, User user, String siteId)
	{
		// mark and posts pagination
		String postsPerPageStr = ServerConfigurationService.getString(POSTS_PER_PAGE);
		//String hotTopicBeginStr = ServerConfigurationService.getString(HOT_TOPIC_BEGIN);
		
		//int hotBegin = -1;
		int postsPerPage = -1;
		
		/*if ((hotTopicBeginStr != null) && (hotTopicBeginStr.trim().length() > 0))
		{
			try
			{
				hotBegin = Integer.parseInt(hotTopicBeginStr);
			}
			catch (NumberFormatException e)
			{
				//ignore error
			}
		}*/
		
		if ((postsPerPageStr != null) && (postsPerPageStr.trim().length() > 0))
		{
			try
			{
				postsPerPage = Integer.parseInt(postsPerPageStr);
			}
			catch (NumberFormatException e)
			{
				//ignore error
			}
		}		
		
		if (user != null)
		{
			// marked all read time
			Date markAllReadTime = jforumUserService.getMarkAllReadTime(siteId, user.getId());
			boolean facilitator = jforumSecurityService.isJForumFacilitator(siteId, user.getSakaiUserId());
			
			/*get all topics with latest post time and check with topic read time*/
			List<Topic> topicsLastPosttimes = null;
			
			if (markAllReadTime == null)
			{
				// check all topics
				if (facilitator)
				{
					topicsLastPosttimes = getForumTopicLatestPosttimes(forum.getId(), user.getId(), null, false);
				}
				else
				{
					topicsLastPosttimes = getForumTopicLatestPosttimes(forum.getId(), user.getId(), null, true);
				}
			}
			else
			{
				/*check topics whose latest post time is after mark all read time and not posted by the current user*/
				if (facilitator)
				{
					topicsLastPosttimes = getForumTopicLatestPosttimes(forum.getId(), user.getId(), markAllReadTime, false);
				}
				else
				{
					topicsLastPosttimes = getForumTopicLatestPosttimes(forum.getId(), user.getId(), markAllReadTime, true);
				}
			}
			
			Map<Integer, Topic> lastPosttimeTopicsMap = new HashMap<Integer, Topic>();
			for (Topic topicPostTimes : topicsLastPosttimes)
			{
				lastPosttimeTopicsMap.put(new Integer(topicPostTimes.getId()), topicPostTimes);
			}
			
			// compare with visited topics time
			List<Topic> visitedTopics = getUserForumTopicVisittimes(forum.getId(), user.getId());
			
			Map<Integer, Topic> visitedTopicsMap = new HashMap<Integer, Topic>();
			for (Topic visitedTopic : visitedTopics)
			{
				visitedTopicsMap.put(new Integer(visitedTopic.getId()), visitedTopic);
			}
			
			Topic visitedTopic = null;
			Topic lastPostTimeTopic = null;
			for (Topic topic : forum.getTopics())
			{
				/*if (user == null)
				{
					topic.setRead(false);
					continue;
				}*/
								
				visitedTopic = visitedTopicsMap.get(new Integer(topic.getId()));
				lastPostTimeTopic = lastPosttimeTopicsMap.get(new Integer(topic.getId()));
				
				if (visitedTopic == null)
				{
					Date lastPostTime = null;
					
					if (lastPostTimeTopic != null)
					{
						lastPostTime = lastPostTimeTopic.getTime();
					}
					
					if (lastPostTime != null)
					{
						topic.setRead(false);
					}
					else
					{
						topic.setRead(true);
					}
				}
				else
				{
					// if topic is marked as unread mark the topic as unread
					if (!visitedTopic.getRead())
					{
						topic.setRead(Boolean.FALSE);
						continue;
					}
					
					Date lastPostTime = null;
					
					if (lastPostTimeTopic != null)
					{
						lastPostTime = lastPostTimeTopic.getTime();
					}
					
					if (lastPostTime != null)
					{
						if  (lastPostTime.before(visitedTopic.getTime()))
						{
							topic.setRead(true);
						}
						else
						{
							topic.setRead(false);
						}
					}
					else
					{
						topic.setRead(true);
					}
					
				}
				
				// Check if this is a hot topic
				/*if (hotBegin != -1)
				{
					topic.setHot(topic.getTotalReplies() >= hotBegin);
				}*/
								
				// posts paginate
				if (postsPerPage != -1)
				{
					if (topic.getTotalReplies() + 1 > postsPerPage) 
					{
						topic.setPaginate(true);
					}
					else 
					{
						topic.setPaginate(false);
					}
				}
			}
		}
		else
		{
			for (Topic topic : forum.getTopics())
			{
				topic.setRead(false);
				
				// Check if this is a hot topic
				/*if (hotBegin != -1)
				{
					topic.setHot(topic.getTotalReplies() >= hotBegin);
				}*/
				
				// posts paginate
				if (postsPerPage != -1)
				{
					if (topic.getTotalReplies() + 1 > postsPerPage) 
					{
						topic.setPaginate(true);
					}
					else 
					{
						topic.setPaginate(false);
					}
				}
			}
		}		
	}

	/**
	 * Checks user unread topics
	 * 
	 * @param context	Context or site id
	 * 
	 * @param topics	Topics
	 * 
	 * @param user		Jforum user
	 */
	protected void checkUnreadTopics(String context, List<Topic> topics, User user)
	{
		if ((topics == null) || (topics.isEmpty()))
		{
			return;
		}
		
				
		if (user != null)
		{
			// marked all read time
			Date markAllReadTime = jforumUserService.getMarkAllReadTime(context, user.getId());
			
			/*// topic mark or visit time
			Date topicMarkTime = null;
			Date topicLatestPostTime = null;
			Topic lastPostimeTopic = null;*/
					
			/*get topics with latest post time and check with topic read time(or visited time) and mark all read time*/		
			for (Topic topic : topics) 
			{
				markUserTopicReadStatus(user, markAllReadTime, topic);
			}
		}
		else
		{
			/*
			String value = ServerConfigurationService.getString(HOT_TOPIC_BEGIN);
			
			int hotBegin = -1;
			
			if ((value != null) && (value.trim().length() > 0))
			{
				try
				{
					hotBegin = Integer.parseInt(value);
				}
				catch (NumberFormatException e)
				{
					//ignore error
				}
			}
			
			for (Topic topic : topics) 
			{
				if (hotBegin != -1)
				{
					topic.setHot(topic.getTotalReplies() >= hotBegin);
				}
				topic.setRead(false);
			}*/
			String postsPerPageStr = ServerConfigurationService.getString(POSTS_PER_PAGE);
			//String hotTopicBeginStr = ServerConfigurationService.getString(HOT_TOPIC_BEGIN);
			
			//int hotBegin = -1;
			int postsPerPage = -1;
			
			/*if ((hotTopicBeginStr != null) && (hotTopicBeginStr.trim().length() > 0))
			{
				try
				{
					hotBegin = Integer.parseInt(hotTopicBeginStr);
				}
				catch (NumberFormatException e)
				{
					//ignore error
				}
			}*/
			
			if ((postsPerPageStr != null) && (postsPerPageStr.trim().length() > 0))
			{
				try
				{
					postsPerPage = Integer.parseInt(postsPerPageStr);
				}
				catch (NumberFormatException e)
				{
					//ignore error
				}
			}
			
			// Check if this is a hot topic
			for (Topic topic : topics) 
			{
				/*if (hotBegin != -1)
				{
					topic.setHot(topic.getTotalReplies() >= hotBegin);
				}*/
				topic.setRead(false);
				
				// posts paginate
				if (postsPerPage != -1)
				{
					if (topic.getTotalReplies() + 1 > postsPerPage) 
					{
						topic.setPaginate(true);
					}
					else 
					{
						topic.setPaginate(false);
					}
				}
			}
		}
	}

	/**
	 * Verifies user topic access
	 * 
	 * @param topic		Topic
	 * 
	 * @param sakaiUserId	Sakai user id
	 * 
	 * @throws JForumAccessException	If user is not allowed to access this item or perform this action
	 */
	protected void checkUserTopicAccess(Topic topic, String sakaiUserId) throws JForumAccessException
	{
		if (topic == null)
		{
			return;
		}
		
		Category category = null;
		Forum forum = null;
		
		forum = topic.getForum();
		
		if (forum == null)
		{
			return;
		}
		
		category = forum.getCategory();
		if (category == null)
		{
			return;
		}
		
		boolean mayPost = true;
		if (forum.getType() == Forum.ForumType.READ_ONLY.getType())
		{
			mayPost = false;			
			((TopicImpl)topic).setMayPost(Boolean.FALSE);
		}
		
		if (mayPost && topic.getStatus() == Topic.TopicStatus.LOCKED.getStatus())
		{
			mayPost = false;
			((TopicImpl)topic).setMayPost(Boolean.FALSE);
		}
		
		boolean categoryForumDates = false;
		
		Date currentTime = new Date();
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		// ignore forums with category invalid dates
		/*if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null) && (category.getAccessDates().getDueDate() != null))
		{
			if (category.getAccessDates().getOpenDate().after(category.getAccessDates().getDueDate()))
			{	
				throw new JForumAccessException(sakaiUserId);
			}
		}*/
		// ignore forums with category invalid dates
		if (category.getAccessDates() != null)
		{
			if (!category.getAccessDates().isDatesValid())
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		
		// ignore forums with invalid dates
		/*if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null) && (forum.getAccessDates().getDueDate() != null))
		{
			if (forum.getAccessDates().getOpenDate().after(forum.getAccessDates().getDueDate()))
			{	
				throw new JForumAccessException(sakaiUserId);
			}
		}*/
		if (forum.getAccessDates() != null)
		{
			if (!forum.getAccessDates().isDatesValid())
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		
		// forum access type
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
					logger.warn("filterUserTopicPosts: missing site: " + category.getContext());
				}
			}

			// user not in any group
			if (!userInGroup)
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		
		// forum special access
		List<SpecialAccess> forumSpecialAccessList = forum.getSpecialAccess();
		boolean forumSpecialAccessUser = false;
		boolean forumSpecialAccessUserAccess = false;
		SpecialAccess userSa = null;
		
		if (user != null && ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null))))
		{
			for (SpecialAccess sa : forumSpecialAccessList)
			{
				if (sa.getUserIds().contains(new Integer(user.getId())))
				{
					userSa = sa;
					
					forumSpecialAccessUser = true;
					
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
						forumSpecialAccessUserAccess = true;
					}
					else
					{
						if (sa.getAccessDates().getOpenDate().before(currentTime))
						{
							forumSpecialAccessUserAccess = true;
						}
					}
					break;
				}
			}
		}
		
		forum.getSpecialAccess().clear();
		if (forumSpecialAccessUser)
		{
			forum.getSpecialAccess().add(userSa);
			
			if (forumSpecialAccessUserAccess)
			{
				//if (mayPost && userSa.getAccessDates().getDueDate() != null && userSa.getAccessDates().isLocked())
				if (mayPost)
				{
					if (userSa.getAccessDates().getAllowUntilDate() != null)
					{
						if (userSa.getAccessDates().getAllowUntilDate().before(currentTime))
						{
							mayPost = false;
							((TopicImpl)topic).setMayPost(Boolean.FALSE);
						}
					}
					else if (userSa.getAccessDates().getDueDate() != null)
					{
						if (userSa.getAccessDates().getDueDate().before(currentTime))
						{
							mayPost = false;
							((TopicImpl)topic).setMayPost(Boolean.FALSE);
						}
					}
				}
			}
			else
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		// forum dates
		else if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null))
		{
			if (forum.getAccessDates().getOpenDate().after(currentTime))
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		
		if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
		{
			categoryForumDates = true;
			
			//if (mayPost && (!forumSpecialAccessUserAccess) && ((forum.getAccessDates().getDueDate()) != null && (forum.getAccessDates().isLocked())))
			if (mayPost && !forumSpecialAccessUserAccess)
			{
				if (forum.getAccessDates().getAllowUntilDate() != null)
				{
					if (forum.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						mayPost = false;
						((TopicImpl)topic).setMayPost(Boolean.FALSE);
					}
				}
				else if (forum.getAccessDates().getDueDate() != null)
				{
					if (forum.getAccessDates().getDueDate().before(currentTime))
					{
						mayPost = false;
						((TopicImpl)topic).setMayPost(Boolean.FALSE);
					}
				}
			}
		}
		else if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
		{
			categoryForumDates = true;
			
			// category dates
			if ((!forumSpecialAccessUser) && (category.getAccessDates().getOpenDate() != null))
			{
				if (category.getAccessDates().getOpenDate().after(currentTime))
				{
					throw new JForumAccessException(sakaiUserId);
				}
			}
			
			//if (mayPost && (!forumSpecialAccessUserAccess) && ((category.getAccessDates().getDueDate()) != null && (category.getAccessDates().isLocked())))
			if (mayPost && !forumSpecialAccessUserAccess)
			{
				if (category.getAccessDates().getAllowUntilDate() != null)
				{
					if (category.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						mayPost = false;
						((TopicImpl)topic).setMayPost(Boolean.FALSE);
					}
				}
				else if ((category.getAccessDates().getDueDate() != null) && category.getAccessDates().getDueDate().before(currentTime))
				{
					mayPost = false;
					((TopicImpl)topic).setMayPost(Boolean.FALSE);
				}
			}			
		}
		
		// topics dates if no category or forum dates
		if (!categoryForumDates)
		{
			checkTopicDates(topic, user);
		}
		
		// for gradable topic verify with course map access advisor
		if (topic.isGradeTopic())
		{
			Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), sakaiUserId);
			
			if (!checkAccess)
			{
				topic.setBlocked(true);
				topic.setBlockedByTitle(getItemAccessMessage(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), sakaiUserId));
				topic.setBlockedByDetails(getItemAccessDetails(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), sakaiUserId));
			}
		}
		else if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
		{
			Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(forum.getId()), sakaiUserId);
			
			if (!checkAccess)
			{
				forum.setBlocked(true);
				forum.setBlockedByTitle(getItemAccessMessage(category.getContext(), JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(forum.getId()), sakaiUserId));
				forum.setBlockedByDetails(getItemAccessDetails(category.getContext(), JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(forum.getId()), sakaiUserId));
			}
		}
		else if (category.isGradable())
		{
			Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), sakaiUserId);
			
			if (!checkAccess)
			{
				category.setBlocked(true);
				category.setBlockedByTitle(getItemAccessMessage(category.getContext(), JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), sakaiUserId));
				category.setBlockedByDetails(getItemAccessDetails(category.getContext(), JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), sakaiUserId));
			}
		}
	}
	
	/**
	 * Clear the topic from thread local cache
	 * 
	 * @param topic
	 *        The topic.
	 */
	protected void clearCache(Topic topic)
	{
		// clear the cache
		this.threadLocalManager.set(cacheKey(String.valueOf(topic.getId())), null);
	}
	
	/**
	 * Create copy of topic
	 * 
	 * @param topic		topic
	 * 
	 * @return	The copy of topic
	 */
	protected TopicImpl clone(TopicImpl topic)
	{
		return new TopicImpl(topic);
	}
	
	/**
	 * Remove users who are not in site
	 * 
	 * @param users	Jforum users
	 * 
	 * @param copyallsiteusers	All site users
	 * @return
	 */
	protected User dropUsers(List<User> users, List<String> copyAllSiteUsers)  
	{
		Iterator<User> dropiter = users.iterator();
		User adminuser = null;
		while (dropiter.hasNext())
		{
			User dropusr = dropiter.next();

			String dropSakUsrId = dropusr.getSakaiUserId();
			if (dropSakUsrId.equalsIgnoreCase("admin")) adminuser = dropusr;
			if (!copyAllSiteUsers.contains(dropSakUsrId.toLowerCase()))
			{
				dropiter.remove();
			}
		}
		return adminuser;
	}
	
	/**
	 * Filters user topics for the user that the user is accessible to
	 * 
	 * @param forum		Forum with topics list to be filtered
	 * 
	 * @param sakaiUserId	Sakai user id
	 * 
	 * @throws JForumAccessException	If user is not accessible to category or fourm
	 */
	protected void filterUserForumTopics(Forum forum, String sakaiUserId) throws JForumAccessException
	{
		if (forum == null)
		{
			return;
		}
		
		boolean categoryForumDates = false;
		
		Date currentTime = Calendar.getInstance().getTime();
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		Category category = forum.getCategory();
		if (category == null)
		{
			return;
		}
		
		// ignore forums with category invalid dates
		/*if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null) && (category.getAccessDates().getDueDate() != null))
		{
			if (category.getAccessDates().getOpenDate().after(category.getAccessDates().getDueDate()))
			{	
				throw new JForumAccessException(sakaiUserId);
			}
		}*/
		// ignore forums with category invalid dates
		if (category.getAccessDates() != null)
		{
			if (!category.getAccessDates().isDatesValid())
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
	
		
		// ignore forums with invalid dates
		/*if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null) && (forum.getAccessDates().getDueDate() != null))
		{
			if (forum.getAccessDates().getOpenDate().after(forum.getAccessDates().getDueDate()))
			{	
				throw new JForumAccessException(sakaiUserId);
			}
		}*/
		if ((forum.getAccessDates() != null) && !forum.getAccessDates().isDatesValid())
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		boolean mayPost = true;
		if (forum.getType() == Forum.ForumType.READ_ONLY.getType())
		{
			mayPost = false;
			for (Topic topic : forum.getTopics())
			{
				((TopicImpl)topic).setMayPost(Boolean.FALSE);
			}
		}		
		
		/*if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null))
		{
			if (forum.getAccessDates().getOpenDate().after(currentTime))
			{	
				category.getForums().clear();
				return;
			}
		}*/
		
		// forum access type
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
					logger.warn("getUserCourseMapItemsByContext: missing site: " + category.getContext());
				}
			}

			// user not in any group
			if (!userInGroup)
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		
		// forum special access
		List<SpecialAccess> forumSpecialAccessList = forum.getSpecialAccess();
		boolean forumSpecialAccessUser = false;
		boolean forumSpecialAccessUserAccess = false;
		SpecialAccess userSa = null;
		
		if (user != null && ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null))))
		{
			for (SpecialAccess sa : forumSpecialAccessList)
			{
				if (sa.getUserIds().contains(new Integer(user.getId())))
				{
					userSa = sa;
					
					forumSpecialAccessUser = true;
					
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
						forumSpecialAccessUserAccess = true;
					}
					else
					{
						if (sa.getAccessDates().getOpenDate().after(currentTime))
						{
							if (!sa.getAccessDates().isHideUntilOpen())
							{
								forumSpecialAccessUserAccess = true;
							}
						}
						else
						{
							forumSpecialAccessUserAccess = true;
						}
					}
					break;
				}
			}
		}
		
		forum.getSpecialAccess().clear();
		if (forumSpecialAccessUser)
		{
			forum.getSpecialAccess().add(userSa);
			
			if (forumSpecialAccessUserAccess)
			{
				if (mayPost)
				{
					for (Topic topic : forum.getTopics())
					{
						if (userSa.getAccessDates().getAllowUntilDate() != null)
						{
							if (userSa.getAccessDates().getAllowUntilDate().before(currentTime))
							{
								mayPost = false;
								((TopicImpl)topic).setMayPost(Boolean.FALSE);
							}
						}
						else if (userSa.getAccessDates().getDueDate() != null)
						{
							if (userSa.getAccessDates().getDueDate().before(currentTime))
							{
								mayPost = false;
								((TopicImpl)topic).setMayPost(Boolean.FALSE);
							}
						}
					}
				}
			}
			else
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		// forum dates
		else if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null))
		{
			if (forum.getAccessDates().getOpenDate().after(currentTime))
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		
		if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
		{
			categoryForumDates = true;
			
			//if (mayPost && (!forumSpecialAccessUserAccess) && ((forum.getAccessDates().getDueDate()) != null && (forum.getAccessDates().isLocked())))
			if (mayPost && !forumSpecialAccessUserAccess)
			{
				if ((forum.getAccessDates().getAllowUntilDate() != null) && (forum.getAccessDates().getAllowUntilDate().before(currentTime)))
				{
					for (Topic topic : forum.getTopics())
					{
						((TopicImpl)topic).setMayPost(Boolean.FALSE);
					}
				}
				else if ((forum.getAccessDates().getDueDate() != null) && (forum.getAccessDates().getDueDate().before(currentTime)))
				{
					for (Topic topic : forum.getTopics())
					{
						((TopicImpl)topic).setMayPost(Boolean.FALSE);
					}
				}
			}
		}
		else if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
		{
			categoryForumDates = true;
			
			// category dates
			if ((!forumSpecialAccessUser) && (category.getAccessDates().getOpenDate() != null))
			{
				if (category.getAccessDates().getOpenDate().after(currentTime))
				{
					throw new JForumAccessException(sakaiUserId);
				}
			}
						
			//if (mayPost && (!forumSpecialAccessUserAccess) && ((category.getAccessDates().getDueDate()) != null && (category.getAccessDates().isLocked())))
			if (mayPost && !forumSpecialAccessUserAccess)
			{
				if (category.getAccessDates().getAllowUntilDate() != null)
				{
					if (category.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						for (Topic topic : forum.getTopics())
						{
							((TopicImpl)topic).setMayPost(Boolean.FALSE);
						}
					}
				}
				else if ((category.getAccessDates().getDueDate() != null) && category.getAccessDates().getDueDate().before(currentTime))
				{
					for (Topic topic : forum.getTopics())
					{
						((TopicImpl)topic).setMayPost(Boolean.FALSE);
					}
				}
			}
		}
		
		// topics dates if no category or forum dates
		if (!categoryForumDates)
		{
			checkTopicDates(forum.getTopics(), user);
		}		

		for (Topic topic : forum.getTopics())
		{
			// for gradable topic verify with course map access advisor
			if (topic.isGradeTopic())
			{
				Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), sakaiUserId);
				
				if (!checkAccess)
				{
					topic.setBlocked(true);
					topic.setBlockedByTitle(getItemAccessMessage(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), sakaiUserId));
					topic.setBlockedByDetails(getItemAccessDetails(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), sakaiUserId));
				}
			}
		}
		
		//add blocked details for category or forum is they are gradable
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
		else if (category.isGradable())
		{
			Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), sakaiUserId);
			
			if (!checkAccess)
			{
				category.setBlocked(true);
				category.setBlockedByTitle(getItemAccessMessage(category.getContext(), JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), sakaiUserId));
				category.setBlockedByDetails(getItemAccessDetails(category.getContext(), JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), sakaiUserId));
			}
		}
		
		// topics read status
		checkUnreadTopics(forum, user, category.getContext());
	}
	
	/**
	 * Filters user topics
	 * 
	 * @param topics	Topics
	 * 
	 * @param sakaiUserId	sakai user id
	 */
	protected void filterUserRecentTopics(List<Topic> topics, String sakaiUserId)
	{
		if (topics == null || topics.size() == 0)
		{
			return;
		}
		
		boolean categoryForumDates = false;
		
		Date currentTime = Calendar.getInstance().getTime();
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		//for (Topic topic : topics)
		Topic topic = null;
		Forum forum = null;
		Category category = null;
		boolean mayPost = true;
		for (Iterator<Topic> iter = topics.iterator(); iter.hasNext();) 
		{
			topic = null;
			forum = null;
			categoryForumDates = false;
			
			topic = iter.next();
		
			forum = topic.getForum();
			if (forum == null)
			{
				iter.remove();
				continue;
			}
			
			category = forum.getCategory();
			if (category == null)
			{
				iter.remove();
				continue;
			}
			
			mayPost = true;
			if (forum.getType() == Forum.ForumType.READ_ONLY.getType())
			{
				mayPost = false;
				
				((TopicImpl)topic).setMayPost(Boolean.FALSE);
			}
			
			if (mayPost && topic.getStatus() == Topic.TopicStatus.LOCKED.getStatus())
			{
				mayPost = false;
				((TopicImpl)topic).setMayPost(Boolean.FALSE);
			}
			
			// ignore forums with category invalid dates
			/*if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null) && (category.getAccessDates().getDueDate() != null))
			{
				if (category.getAccessDates().getOpenDate().after(category.getAccessDates().getDueDate()))
				{	
					iter.remove();
					continue;
				}
			}*/
			if ((category.getAccessDates() != null) && (!category.getAccessDates().isDatesValid()))
			{
				iter.remove();
				continue;
			}
		
			
			// ignore forums with invalid dates
			/*if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null) && (forum.getAccessDates().getDueDate() != null))
			{
				if (forum.getAccessDates().getOpenDate().after(forum.getAccessDates().getDueDate()))
				{	
					iter.remove();
					continue;
				}
			}*/
			if ((forum.getAccessDates() != null) && (!forum.getAccessDates().isDatesValid()))
			{
				iter.remove();
				continue;
			}
			
			// forum access type
			if (forum.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
			{
				iter.remove();
				continue;
			}
			
			// forum groups
			if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
			{
				boolean userInGroup = false;
				
				try
				{
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
						logger.warn("getUserCourseMapItemsByContext: missing site: " + category.getContext());
					}
				}
	
				// user not in any group
				if (!userInGroup)
				{
					iter.remove();
					continue;
				}
			}
			
			// forum special access
			List<SpecialAccess> forumSpecialAccessList = forum.getSpecialAccess();
			boolean forumSpecialAccessUser = false;
			boolean forumSpecialAccessUserAccess = false;
			SpecialAccess userSa = null;
			
			if (user != null && ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null))))
			{
				for (SpecialAccess sa : forumSpecialAccessList)
				{
					if (sa.getUserIds().contains(new Integer(user.getId())))
					{
						userSa = sa;
						
						forumSpecialAccessUser = true;
						
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
							forumSpecialAccessUserAccess = true;
						}
						else
						{
							if (sa.getAccessDates().getOpenDate().after(currentTime))
							{
								if (!sa.getAccessDates().isHideUntilOpen())
								{
									forumSpecialAccessUserAccess = true;
								}
							}
							else
							{
								forumSpecialAccessUserAccess = true;
							}
						}
						break;
					}
				}
			}
			
			forum.getSpecialAccess().clear();
			if (forumSpecialAccessUser)
			{
				forum.getSpecialAccess().add(userSa);
				
				if (forumSpecialAccessUserAccess)
				{
					//if (mayPost && userSa.getAccessDates().getDueDate() != null && userSa.getAccessDates().isLocked())
					if (mayPost)
					{
						if (userSa.getAccessDates().getAllowUntilDate() != null)
						{
							if (userSa.getAccessDates().getAllowUntilDate().before(currentTime))
							{
								mayPost = false;
								((TopicImpl) topic).setMayPost(Boolean.FALSE);
							}
						}
						else if (userSa.getAccessDates().getDueDate() != null)
						{
							if (userSa.getAccessDates().getDueDate().before(currentTime))
							{
								mayPost = false;
								((TopicImpl) topic).setMayPost(Boolean.FALSE);
							}
						}
					}					
				}
				else
				{
					iter.remove();
					continue;
				}
			}
			// forum dates
			else if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null))
			{
				if (forum.getAccessDates().getOpenDate().after(currentTime))
				{
					iter.remove();
					continue;
				}
			}
			
			if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
			{
				categoryForumDates = true;

				//if (mayPost && (!forumSpecialAccessUserAccess) && ((forum.getAccessDates().getDueDate()) != null && (forum.getAccessDates().isLocked())))
				if (mayPost && (!forumSpecialAccessUserAccess))
				{
					if (forum.getAccessDates().getAllowUntilDate() != null)
					{
						if (forum.getAccessDates().getAllowUntilDate().before(currentTime))
						{
							mayPost = false;
							((TopicImpl) topic).setMayPost(Boolean.FALSE);
						}
					}
					else if (forum.getAccessDates().getDueDate() != null)
					{
						if (forum.getAccessDates().getDueDate().before(currentTime))
						{
							mayPost = false;
							((TopicImpl) topic).setMayPost(Boolean.FALSE);
						}
					}
				}
			}
			else if ((category.getAccessDates() != null)
					&& ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
			{
				categoryForumDates = true;

				// category dates
				if ((!forumSpecialAccessUser) && (category.getAccessDates().getOpenDate() != null))
				{
					if (category.getAccessDates().getOpenDate().after(currentTime))
					{
						iter.remove();
						continue;
					}
				}

				//if (mayPost && (!forumSpecialAccessUserAccess) && ((category.getAccessDates().getDueDate()) != null && (category.getAccessDates().isLocked())))
				if (mayPost && !forumSpecialAccessUserAccess)
				{
					if (category.getAccessDates().getAllowUntilDate() != null)
					{
						if (category.getAccessDates().getAllowUntilDate().before(currentTime))
						{
							mayPost = false;
							((TopicImpl)topic).setMayPost(Boolean.FALSE);
						}
					}
					else if ((category.getAccessDates().getDueDate() != null) && category.getAccessDates().getDueDate().before(currentTime))
					{
						mayPost = false;
						((TopicImpl)topic).setMayPost(Boolean.FALSE);
					}
				}
			}
			
			// topics dates if no category or forum dates
			if (!categoryForumDates)
			{
				// topic special access
				List<SpecialAccess> topicSpecialAccessList = topic.getSpecialAccess();
				boolean topicSpecialAccessUser = false;
				boolean topicSpecialAccessUserAccess = false;
				SpecialAccess userTopicSa = null;
				
				if ((user != null) && ((topic.getAccessDates() != null) && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null))))
				{
					for (SpecialAccess sa : topicSpecialAccessList)
					{
						if (sa.getUserIds().contains(new Integer(user.getId())))
						{
							userTopicSa = sa;
							
							topicSpecialAccessUser = true;
							
							if (!sa.isOverrideStartDate())
							{
								sa.getAccessDates().setOpenDate(topic.getAccessDates().getOpenDate());
							}
							
							if (!sa.isOverrideHideUntilOpen())
							{
								sa.getAccessDates().setHideUntilOpen(topic.getAccessDates().isHideUntilOpen());
							}
							
							if (!sa.isOverrideEndDate())
							{
								sa.getAccessDates().setDueDate(topic.getAccessDates().getDueDate());
							}
							
							/*if (!sa.isOverrideLockEndDate())
							{
								sa.getAccessDates().setLocked(topic.getAccessDates().isLocked());
							}*/
							
							if (!sa.isOverrideAllowUntilDate())
							{
							
								sa.getAccessDates().setAllowUntilDate(topic.getAccessDates().getAllowUntilDate());
							}
							
							if (sa.getAccessDates().getOpenDate() == null)
							{
								topicSpecialAccessUserAccess = true;
							}
							else
							{
								if (sa.getAccessDates().getOpenDate().after(currentTime))
								{
									if (!sa.getAccessDates().isHideUntilOpen())
									{
										topicSpecialAccessUserAccess = true;
									}
								}
								else
								{
									topicSpecialAccessUserAccess = true;
								}
							}
							
							break;
						}
					}
				}
				
				topic.getSpecialAccess().clear();
				if (topicSpecialAccessUser)
				{
					topic.getSpecialAccess().add(userTopicSa);
					
					if (topicSpecialAccessUserAccess)
					{
						//if (mayPost && userTopicSa.getAccessDates().getDueDate() != null && userTopicSa.getAccessDates().isLocked())
						if (userTopicSa.getAccessDates().getAllowUntilDate() != null)
						{
							if (userTopicSa.getAccessDates().getAllowUntilDate().before(currentTime))
							{
								mayPost = false;
								((TopicImpl)topic).setMayPost(Boolean.FALSE);
							}
						}
						else if (userTopicSa.getAccessDates().getDueDate() != null)
						{
							if (userTopicSa.getAccessDates().getDueDate().before(currentTime))
							{
								mayPost = false;
								((TopicImpl)topic).setMayPost(Boolean.FALSE);
							}
						}
					}
					else
					{
						iter.remove();
						continue;
					}
				}
				// topic dates
				else if (topic.getAccessDates() != null)
				{
					if ((topic.getAccessDates().getOpenDate() != null)  && (topic.getAccessDates().getOpenDate().after(currentTime)))
					{
						iter.remove();
						continue;
					}
					
					//if (mayPost && topic.getAccessDates().getDueDate() != null && topic.getAccessDates().isLocked())
					if (mayPost)
					{
						if (topic.getAccessDates().getAllowUntilDate() != null)
						{
							if (topic.getAccessDates().getAllowUntilDate().before(currentTime))
							{
								mayPost = false;
								((TopicImpl)topic).setMayPost(Boolean.FALSE);
							}
						}
						else if (topic.getAccessDates().getDueDate() != null)
						{
							if (topic.getAccessDates().getDueDate().before(currentTime))
							{
								mayPost = false;
								((TopicImpl)topic).setMayPost(Boolean.FALSE);
							}
						}
					}
				}
			}
			
	
			//for (Topic topic : forum.getTopics())
			//{
			// for gradable topic verify with course map access advisor
			if (topic.isGradeTopic())
			{
				Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), sakaiUserId);
				
				if (!checkAccess)
				{
					topic.setBlocked(true);
					topic.setBlockedByTitle(getItemAccessMessage(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), sakaiUserId));
					topic.setBlockedByDetails(getItemAccessDetails(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), sakaiUserId));
				}
			}
			//}
			
			//add blocked details for category or forum is they are gradable
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
			else if (category.isGradable())
			{
				Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), sakaiUserId);
				
				if (!checkAccess)
				{
					category.setBlocked(true);
					category.setBlockedByTitle(getItemAccessMessage(category.getContext(), JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), sakaiUserId));
					category.setBlockedByDetails(getItemAccessDetails(category.getContext(), JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), sakaiUserId));
				}
			}
		}
	}

	/**
	 * Checks the user access to topic
	 * 
	 * @param Topic	Topic with posts
	 * 
	 * @param userId	Sakai user id
	 */
	protected void filterUserTopicPosts(Topic topic, String sakaiUserId) throws JForumAccessException
	{

		if (topic == null)
		{
			return;
		}

		Category category = null;
		Forum forum = null;

		forum = topic.getForum();

		if (forum == null)
		{
			return;
		}

		category = forum.getCategory();
		if (category == null)
		{
			return;
		}

		boolean mayPost = true;
		if (forum.getType() == Forum.ForumType.READ_ONLY.getType())
		{
			mayPost = false;
			((TopicImpl) topic).setMayPost(Boolean.FALSE);
		}

		if (mayPost && topic.getStatus() == Topic.TopicStatus.LOCKED.getStatus())
		{
			mayPost = false;
			((TopicImpl) topic).setMayPost(Boolean.FALSE);
		}

		boolean categoryForumDates = false;

		Date currentTime = Calendar.getInstance().getTime();

		User user = jforumUserService.getBySakaiUserId(sakaiUserId);

		// ignore forums with category invalid dates
		/*if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null)
				&& (category.getAccessDates().getDueDate() != null))
		{
			if (category.getAccessDates().getOpenDate().after(category.getAccessDates().getDueDate()))
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}*/
		// ignore forums with category invalid dates
		if (category.getAccessDates() != null)
		{
			if (!category.getAccessDates().isDatesValid())
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}

		// ignore forums with invalid dates
		if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null) && (forum.getAccessDates().getDueDate() != null))
		{
			if (forum.getAccessDates().getOpenDate().after(forum.getAccessDates().getDueDate()))
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}

		// forum access type
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
					logger.warn("filterUserTopicPosts: missing site: " + category.getContext());
				}
			}

			// user not in any group
			if (!userInGroup)
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}

		// forum special access
		List<SpecialAccess> forumSpecialAccessList = forum.getSpecialAccess();
		boolean forumSpecialAccessUser = false;
		boolean forumSpecialAccessUserAccess = false;
		SpecialAccess userSa = null;

		if (user != null
				&& ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null))))
		{
			for (SpecialAccess sa : forumSpecialAccessList)
			{
				if (sa.getUserIds().contains(new Integer(user.getId())))
				{
					userSa = sa;

					forumSpecialAccessUser = true;

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
						forumSpecialAccessUserAccess = true;
					}
					else
					{
						if (sa.getAccessDates().getOpenDate().after(currentTime))
						{
							if (!sa.getAccessDates().isHideUntilOpen())
							{
								forumSpecialAccessUserAccess = true;
							}
						}
						else
						{
							forumSpecialAccessUserAccess = true;
						}
					}
					break;
				}
			}
		}

		forum.getSpecialAccess().clear();
		if (forumSpecialAccessUser)
		{
			forum.getSpecialAccess().add(userSa);

			if (forumSpecialAccessUserAccess)
			{
				//if (mayPost && userSa.getAccessDates().getDueDate() != null && userSa.getAccessDates().isLocked())
				if (mayPost)
				{
					if (userSa.getAccessDates().getAllowUntilDate() != null)
					{
						if (userSa.getAccessDates().getAllowUntilDate().before(currentTime))
						{
							mayPost = false;
							((TopicImpl) topic).setMayPost(Boolean.FALSE);
						}
					}
					else if (userSa.getAccessDates().getDueDate() != null)
					{
						if (userSa.getAccessDates().getDueDate().before(currentTime))
						{
							mayPost = false;
							((TopicImpl) topic).setMayPost(Boolean.FALSE);
						}
					}
				}
			}
			else
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		// forum dates
		else if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null))
		{
			if (forum.getAccessDates().getOpenDate().after(currentTime))
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}

		if ((forum.getAccessDates() != null) && ((forum.getAccessDates().getOpenDate() != null) || (forum.getAccessDates().getDueDate() != null) || (forum.getAccessDates().getAllowUntilDate() != null)))
		{
			categoryForumDates = true;

			//if (mayPost && (!forumSpecialAccessUserAccess) && ((forum.getAccessDates().getDueDate()) != null && (forum.getAccessDates().isLocked())))
			if (mayPost && (!forumSpecialAccessUserAccess))
			{
				if (forum.getAccessDates().getAllowUntilDate() != null)
				{
					if (forum.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						mayPost = false;
						((TopicImpl) topic).setMayPost(Boolean.FALSE);
					}
				}
				else if (forum.getAccessDates().getDueDate() != null)
				{
					if (forum.getAccessDates().getDueDate().before(currentTime))
					{
						mayPost = false;
						((TopicImpl) topic).setMayPost(Boolean.FALSE);
					}
				}
			}
		}
		else if ((category.getAccessDates() != null)
				&& ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
		{
			categoryForumDates = true;

			// category dates
			if ((!forumSpecialAccessUser) && (category.getAccessDates().getOpenDate() != null))
			{
				if (category.getAccessDates().getOpenDate().after(currentTime))
				{
					throw new JForumAccessException(sakaiUserId);
				}
			}

			//if (mayPost && (!forumSpecialAccessUserAccess) && ((category.getAccessDates().getDueDate()) != null && (category.getAccessDates().isLocked())))
			if (mayPost && !forumSpecialAccessUserAccess)
			{
				if (category.getAccessDates().getAllowUntilDate() != null)
				{
					if (category.getAccessDates().getAllowUntilDate().before(currentTime))
					{
						mayPost = false;
						((TopicImpl)topic).setMayPost(Boolean.FALSE);
					}
				}
				else if ((category.getAccessDates().getDueDate() != null) && category.getAccessDates().getDueDate().before(currentTime))
				{
					mayPost = false;
					((TopicImpl)topic).setMayPost(Boolean.FALSE);
				}
			}
		}

		// topics dates if no category or forum dates
		if (!categoryForumDates)
		{
			checkTopicDates(topic, user);
		}

		// for gradable topic verify with course map access advisor
		if (topic.isGradeTopic())
		{
			Boolean checkAccess = checkItemAccess(category.getContext(),
					JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix() + String.valueOf(topic.getId()), sakaiUserId);

			if (!checkAccess)
			{
				topic.setBlocked(true);
				topic.setBlockedByTitle(getItemAccessMessage(category.getContext(),
						JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix() + String.valueOf(topic.getId()), sakaiUserId));
				topic.setBlockedByDetails(getItemAccessDetails(category.getContext(),
						JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix() + String.valueOf(topic.getId()), sakaiUserId));
			}
		}
		else if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
		{
			Boolean checkAccess = checkItemAccess(category.getContext(),
					JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix() + String.valueOf(forum.getId()), sakaiUserId);

			if (!checkAccess)
			{
				forum.setBlocked(true);
				forum.setBlockedByTitle(getItemAccessMessage(category.getContext(),
						JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix() + String.valueOf(forum.getId()), sakaiUserId));
				forum.setBlockedByDetails(getItemAccessDetails(category.getContext(),
						JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix() + String.valueOf(forum.getId()), sakaiUserId));
			}
		}
		else if (category.isGradable())
		{
			Boolean checkAccess = checkItemAccess(category.getContext(),
					JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix() + String.valueOf(category.getId()), sakaiUserId);

			if (!checkAccess)
			{
				category.setBlocked(true);
				category.setBlockedByTitle(getItemAccessMessage(category.getContext(),
						JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix() + String.valueOf(category.getId()), sakaiUserId));
				category.setBlockedByDetails(getItemAccessDetails(category.getContext(), JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()
						+ String.valueOf(category.getId()), sakaiUserId));
			}
		}

		// participant post edit permission. participants cannot edit the post
		// if topic is locked or forum is read only or category/forum/topic has
		// dates and locked after the end date
		for (Post post : topic.getPosts())
		{
			if (post.getUserId() == user.getId())
			{
				// topic locked
				if (topic.getStatus() == Topic.TopicStatus.LOCKED.getStatus())
				{
					continue;
				}

				// read only forum
				if (forum.getType() == Forum.ForumType.READ_ONLY.getType())
				{
					continue;
				}

				// category due date passed and category locked
				if ((category.getAccessDates() != null)
						&& ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null) || (category.getAccessDates().getAllowUntilDate() != null)))
				{
					if (category.getAccessDates().getAllowUntilDate() != null)
					{
						if (category.getAccessDates().getAllowUntilDate().before(currentTime))
						{
							continue;
						}
					}
					else if (category.getAccessDates().getDueDate() != null)
					{
						//if (category.getAccessDates().getDueDate().before(currentTime) && category.getAccessDates().isLocked())
						if (category.getAccessDates().getDueDate().before(currentTime))
						{
							continue;
						}
					}
				}

				// forum special access user
				if (forumSpecialAccessUser)
				{
					if (forumSpecialAccessUserAccess && userSa != null)
					{
						//if (userSa.getAccessDates().getDueDate() != null && userSa.getAccessDates().getDueDate().before(currentTime) && userSa.getAccessDates().isLocked())
						if (userSa.getAccessDates().getAllowUntilDate() != null)
						{
							if (userSa.getAccessDates().getAllowUntilDate().before(currentTime))
							{
								continue;
							}
						}
						else if (userSa.getAccessDates().getDueDate() != null)
						{
							if (userSa.getAccessDates().getDueDate().before(currentTime))
							{
								continue;
							}
						}							 
					}
					else
					{
						continue;
					}
				}
				// forum due date passed and forum locked
				else if ((forum.getAccessDates() != null) && (forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))
				{
					//if (forum.getAccessDates().getDueDate().before(currentTime) && forum.getAccessDates().isLocked())
					if (forum.getAccessDates().getAllowUntilDate() != null)
					{
						if (forum.getAccessDates().getAllowUntilDate().before(currentTime))
						{
							continue;
						}
					}
					else if (forum.getAccessDates().getDueDate() != null)
					{
						if (forum.getAccessDates().getDueDate().before(currentTime))
						{
							continue;
						}
					}
				}

				if ((topic.getAccessDates() != null)
						&& ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null) || (topic.getAccessDates().getAllowUntilDate() != null)))
				{
					boolean topicSpecialAccessUser = false;
					// topic special access user
					if (topic.getSpecialAccess().size() == 1)
					{
						SpecialAccess userTopicSa = topic.getSpecialAccess().get(0);

						if (userTopicSa != null)
						{
							topicSpecialAccessUser = true;

							//if (userTopicSa.getAccessDates().getDueDate() != null && userTopicSa.getAccessDates().getDueDate().before(currentTime) && userTopicSa.getAccessDates().isLocked())
							if (userTopicSa.getAccessDates().getAllowUntilDate() != null)
							{
								if (userTopicSa.getAccessDates().getAllowUntilDate().before(currentTime))
								{
									continue;
								}
							}
							else if (userTopicSa.getAccessDates().getDueDate() != null)
							{
								if (userTopicSa.getAccessDates().getDueDate().before(currentTime))
								{
									continue;
								}
							}
						}
					}

					// topic due date passed and topic locked
					if (!topicSpecialAccessUser && ((topic.getAccessDates().getDueDate() != null) || (topic.getAccessDates().getAllowUntilDate() != null)))
					{
						//if (topic.getAccessDates().getDueDate().before(currentTime) && topic.getAccessDates().isLocked())
						if (topic.getAccessDates().getAllowUntilDate() != null)
						{
							if (topic.getAccessDates().getAllowUntilDate().before(currentTime))
							{
								continue;
							}
						}
						else if (topic.getAccessDates().getDueDate() != null)
						{
							if (topic.getAccessDates().getDueDate().before(currentTime))
							{
								continue;
							}
						}
					}
				}

				((PostImpl) post).setCanEdit(true);
			}
		}
	}
	
	/**
	 * Gets accessible topics and not hidden(visible but not accessible) count of the forum
	 * 
	 * @param forumId	Forum id
	 * 
	 * @return	The accessible topics and not hidden(visible but not accessible) count of the forum
	 */
	protected int getAccessibleTopicsCountByForum(int forumId)
	{
		StringBuilder sql = new StringBuilder();
		
		// though query can be used with just checking hide_until_open = 0 for sanity check time is considered
		sql.append("SELECT count(1) AS accessible_topics_count FROM jforum_topics WHERE forum_id = ? AND (start_date < ? OR start_date IS NULL OR (start_date > ? AND hide_until_open = 0)) ");
		
		Object[] fields;
		int i = 0;
		
		fields = new Object[3];
		fields[i++] = forumId;
		fields[i++] = new Timestamp(System.currentTimeMillis());
		fields[i++] = new Timestamp(System.currentTimeMillis());
		

		int topicMessagesCount = 0;
		
		final List<Integer> count = new ArrayList<Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					count.add(result.getInt("accessible_topics_count"));

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getAccessibleTopicsCountByForum: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (count.size() == 1)
		{
			topicMessagesCount = count.get(0);
		}
		return topicMessagesCount;
	}
	
	/**
	 * get the accessible and not hidden(visible but not accessible) topics messages count
	 * 
	 * @param forumId	The forum id
	 * 
	 * @return		The count of the accessible and not hidden(visible but not accessible) topics messages count
	 */
	protected int getAccessibleTopicsMessagesCountByForum(int forumId)
	{
		StringBuilder sql = new StringBuilder();
		
		// though query can be used with just checking hide_until_open = 0 for sanity check time is considered
		sql.append("SELECT COUNT(p.post_id) AS total_posts FROM jforum_topics t LEFT JOIN jforum_posts p ON p.topic_id = t.topic_id ");
		sql.append("WHERE t.forum_id = ? AND  (start_date < ? OR start_date IS NULL OR (start_date > ? AND hide_until_open = 0 )) GROUP BY t.forum_id");
		
		Object[] fields;
		int i = 0;
		
		fields = new Object[3];
		fields[i++] = forumId;
		fields[i++] = new Timestamp(System.currentTimeMillis());
		fields[i++] = new Timestamp(System.currentTimeMillis());

		int topicMessagesCount = 0;
		
		final List<Integer> count = new ArrayList<Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					count.add(result.getInt("total_posts"));

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getAccessibleTopicsMessagesCountByForum: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (count.size() == 1)
		{
			topicMessagesCount = count.get(0);
		}
		return topicMessagesCount;
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
	 * Gets the user topic mark or visit time
	 *  
	 * @param topicId	Topic id
	 * 
	 * @param user	JForum user
	 * 
	 * @return	The user topic with mark time or null
	 */
	protected Topic getUserTopicMarkTime(int topicId, User user)
	{
		if ((topicId == 0) || (user == null))
		{
			new IllegalArgumentException();
		}
		
		return topicDao.selectMarkTime(topicId, user.getId());
	}
	
	/**
	 * Mark topic and forum read status of the user
	 * 
	 * @param topic			Topic
	 * 
	 * @param sakaiUserId	Sakai user id
	 */
	protected void markTopicForumReadStatus(Topic topic, String sakaiUserId)
	{
		if ((topic == null) || (sakaiUserId == null || sakaiUserId.trim().length() == 0))
		{
			return;
		}
				
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		Forum forum = topic.getForum();
		Category category = forum.getCategory();
		
		if (user == null)
		{
			topic.setRead(Boolean.FALSE);
			forum.setUnread(true);
			
			return;
		}
		
		// marked all read time
		Date markAllReadTime = jforumUserService.getMarkAllReadTime(category.getContext(), user.getId());
		
		//mark user topic read status. This may not be needed if this method is called when user visits the topic posts as topic is marked as visited.
		markUserTopicReadStatus(user, markAllReadTime, topic);
		
		if (!topic.getRead().booleanValue())
		{
			forum.setUnread(true);
			return;
		}
		
		//This may not be needed - if topic is read check forum read status with other topics - get all accessible topics with latest post time and check with topic read time
	}
	
	/**
	 * mark user topic read status
	 * 
	 * @param user		JForum user
	 * 
	 * @param markAllReadTime	user mark all read time
	 * 
	 * @param topic		Topic
	 */
	protected void markUserTopicReadStatus(User user, Date markAllReadTime, Topic topic)
	{
		String postsPerPageStr = ServerConfigurationService.getString(POSTS_PER_PAGE);
		//String hotTopicBeginStr = ServerConfigurationService.getString(HOT_TOPIC_BEGIN);
		
		//int hotBegin = -1;
		int postsPerPage = -1;
		
		/*if ((hotTopicBeginStr != null) && (hotTopicBeginStr.trim().length() > 0))
		{
			try
			{
				hotBegin = Integer.parseInt(hotTopicBeginStr);
			}
			catch (NumberFormatException e)
			{
				//ignore error
			}
		}*/
		
		if ((postsPerPageStr != null) && (postsPerPageStr.trim().length() > 0))
		{
			try
			{
				postsPerPage = Integer.parseInt(postsPerPageStr);
			}
			catch (NumberFormatException e)
			{
				//ignore error
			}
		}
		
		// Check if this is a hot topic
		/*if (hotBegin != -1)
		{
			topic.setHot(topic.getTotalReplies() >= hotBegin);
		}*/
		
		// posts paginate
		if (postsPerPage != -1)
		{
			if (topic.getTotalReplies() + 1 > postsPerPage) 
			{
				topic.setPaginate(true);
			}
			else 
			{
				topic.setPaginate(false);
			}
		}
		
		//topicMarkTime = getUserTopicMarkTime(topic.getId(), user);
		
		//Date topicMarkTime = null;
		Date topicLatestPostTime = null;
		Topic lastPostimeTopic = null;
		Topic topicUserMarkTime = getUserTopicMarkTime(topic.getId(), user);
				
		lastPostimeTopic = getTopicLatestPosttime(topic.getId(), user.getId(), markAllReadTime, false);
		if (lastPostimeTopic != null)
		{
			topicLatestPostTime = lastPostimeTopic.getTime();
		}
		
		if (topicUserMarkTime == null)
		{
			if (topicLatestPostTime != null)
			{
				topic.setRead(false);
			}
			else
			{
				topic.setRead(true);
			}
		}
		else
		{
			/*Date topicMarkTime = topicUserMarkTime.getTime();
			
			if (markAllReadTime != null)
			{
				if (topicMarkTime.before(markAllReadTime))
				{
					topic.setRead(true);
				}
			}
			else if (!topicUserMarkTime.getRead())
			{
				topic.setRead(false);
			}				
			else if (topicLatestPostTime != null)
			{
				if  (topicLatestPostTime.before(topicMarkTime))
				{
					topic.setRead(true);
				}
				else
				{
					topic.setRead(false);
				}
			}
			else
			{
				topic.setRead(true);
			}*/
			// if topic is marked as unread mark the forum as unread
			if (!topicUserMarkTime.getRead())
			{
				topic.setRead(false);
				return;
			}
			
			Date topicMarkTime = topicUserMarkTime.getTime();
			
			if (topicLatestPostTime != null)
			{
				if  (topicLatestPostTime.before(topicMarkTime))
				{
					topic.setRead(true);
				}
				else
				{
					topic.setRead(false);
				}
			}
			else
			{
				topic.setRead(true);
			}
			
		}
	}
	
	/**
	 * Notify new topic to users who opted to received new topic email notification
	 * 
	 * @param category	Category
	 */
	protected void notifyNewTopicToUsers(Category category)
	{
		List<User> usersToNotify = updateUsersInfo(category.getContext());
		
		Forum topicForum = category.getForums().get(0);
		Topic topic = category.getForums().get(0).getTopics().get(0);
		Post post = category.getForums().get(0).getTopics().get(0).getPosts().get(0);
		
		// if forum has groups send to the members of the groups
		boolean sendToGroups = false;
		Site site = null;
		if ((topicForum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType()) && (topicForum.getGroups() != null && topicForum.getGroups().size() > 0)) 
		{
			sendToGroups = true;
			
			try
			{
				site = siteService.getSite(category.getContext());
			}
			catch (IdUnusedException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("notifyNewTopicToUsers: missing site: " + category.getContext());
				}
			}
		}
		
		// remove the user who posted the message
		int posterId =  category.getForums().get(0).getTopics().get(0).getPostedBy().getId();
		
		Iterator<User> usersIterator = usersToNotify.iterator();
		while (usersIterator.hasNext())
		{
			User usr = (User) usersIterator.next();
			
			if (usr == null)
			{
				usersIterator.remove();
				continue;
			}
			
			if (usr.getId() == posterId)
			{
				usersIterator.remove();
				continue;
			}
						
			// notify only to the users who belong to the group(s) associated with forum
			if (sendToGroups && !jforumSecurityService.isJForumFacilitator(category.getContext(), usr.getSakaiUserId())) 
			{
				boolean userInGroup = false;

				Collection sakaiSiteGroups = site.getGroupsWithMember(usr.getSakaiUserId());

				for (Iterator<Group> usrGrpIter = sakaiSiteGroups.iterator(); usrGrpIter.hasNext();)
				{
					org.sakaiproject.site.api.Group grp = (org.sakaiproject.site.api.Group) usrGrpIter.next();
					if (topicForum.getGroups().contains(grp.getId()))
					{
						userInGroup = true;
						break;
					}
				}

				if (!userInGroup)
				{
					usersIterator.remove();
					continue;
				}
			}
			
			
			if (!usr.isNotifyOnMessagesEnabled() || (usr.getEmail() == null || usr.getEmail().trim().length() == 0) || (!jforumSecurityService.isUserActive(category.getContext(), usr.getSakaiUserId())))
			{
				usersIterator.remove();
				continue;
			}
			
			try
			{
				new InternetAddress(usr.getEmail());
			} 
			catch (AddressException e)
			{
				if (logger.isWarnEnabled()) logger.warn("notifyNewTopicToUsers(...) : "+ usr.getEmail() + " is invalid. And exception is : "+ e);
				usersIterator.remove();
				continue;
			}
		}
		
		if (usersToNotify != null && usersToNotify.size() > 0) 
		{
			// format message text
			Map<String, String> params = new HashMap<String, String>();
			
			// send email
			InternetAddress from = null;
			InternetAddress[] to = null;
			
			String topicFrom = "";
			if (topic.getPostedBy() != null)
			{
				topicFrom = topic.getPostedBy().getFirstName() +" "+ topic.getPostedBy().getLastName();		
			}
			
			to = new InternetAddress[usersToNotify.size()];
			int i = 0;
			for (User user: usersToNotify)
			{
				try
				{
					InternetAddress toUserEmail = new InternetAddress(user.getEmail());
					to[i] = toUserEmail;
					i++;
				} 
				catch (AddressException e)
				{
					if (logger.isWarnEnabled()) logger.warn("notifyNewTopicToUsers(): New topic email notification 'toUserEmail' error : "+ e);
				}
			}
			
			String fromUserEmail = "\"" + ServerConfigurationService.getString("ui.service", "Sakai") + "\"<no-reply@" + ServerConfigurationService.getServerName() + ">";
			try
			{
				from = new InternetAddress(fromUserEmail);
			}
			catch (AddressException e1)
			{
				if (logger.isWarnEnabled()) logger.warn("notifyNewTopicToUsers(): New topic email notification 'fromUserEmail' error : "+ e1);
			}
			
			try
			{
				if (site == null)
				{
					site = siteService.getSite(category.getContext());
				}
				
				params.put("site.title", site.getTitle());
				
				String portalUrl = ServerConfigurationService.getPortalUrl();
				params.put("portal.url", portalUrl);
				
				//String currToolId = ToolManager.getCurrentPlacement().getId();
				//ToolConfiguration toolConfiguration = site.getTool(currToolId);
				
				ToolConfiguration toolConfiguration = site.getToolForCommonId("sakai.jforum.tool");
				
				String siteNavUrl = portalUrl + "/"+ "site" + "/"+ Web.escapeUrl(site.getId());
				
				if (toolConfiguration != null)
				{
					siteNavUrl = siteNavUrl + "/" + "page" + "/"+ toolConfiguration.getPageId();
				}
				
				params.put("site.url", siteNavUrl);
			}
			catch (IdUnusedException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(e.toString(), e);
				}
			}
			
			params.put("topic.title", topic.getTitle());
			
			params.put("topic.from", topicFrom);
			params.put("post.subject", post.getSubject());
			
			String postText = post.getText();
			
			// full URL's for smilies etc
			if (postText != null && postText.trim().length() > 0)
			{
				postText = XrefHelper.fullUrls(postText);
			}
			params.put("post.text", postText);
			
			String messageText = EmailUtil.getNewTopicMessageText(params);
			
			String subject = "["+ site.getTitle() +" - New Post] "+ topic.getTitle();
			
			if (messageText != null)
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("notifyNewTopicToUsers() - Email notification - to.length :"+ to.length);
				}
				
				if ((post != null) && (post.hasAttachments()))
				{
					//email with attachments
					jforumEmailExecutorService.notifyUsers(from, to, subject, messageText, post.getAttachments());
				}
				else
				{
					jforumEmailExecutorService.notifyUsers(from, to, subject, messageText);
				}
			}
			else
			{
				if ((post != null) && (post.hasAttachments()))
				{
					//email with attachments
					jforumEmailExecutorService.notifyUsers(from, to, subject, postText, post.getAttachments());
				}
				else
				{
					jforumEmailExecutorService.notifyUsers(from, to, subject, postText);
				}
			}
		}	
	}
	
	/**
	 * Sends a "new post" notification message to all users watching the topic.
	 * 
	 * @param category
	 */
	protected void notifyTopicReplyToUsers(Category category)
	{
		List<User> siteUsers = updateUsersInfo(category.getContext());
		
		Forum topicForum = category.getForums().get(0);
		Topic topic = category.getForums().get(0).getTopics().get(0);
		Post post = category.getForums().get(0).getTopics().get(0).getPosts().get(0);
		
		// remove the user who posted the message
		int posterId =  post.getPostedBy().getId();
		
		List<User> usersToNotify = topicDao.notifyUsers(topic.getId(), posterId);
		
		// if forum has groups send to the members of the groups
		boolean sendToGroups = false;
		Site site = null;
		if ((topicForum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType()) && (topicForum.getGroups() != null && topicForum.getGroups().size() > 0)) 
		{
			sendToGroups = true;
			
			try
			{
				site = siteService.getSite(category.getContext());
			}
			catch (IdUnusedException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("notifyNewTopicToUsers: missing site: " + category.getContext());
				}
			}
		}
		
		Iterator<User> usersIterator = usersToNotify.iterator();
		while (usersIterator.hasNext())
		{
			User usr = (User) usersIterator.next();

			if (usr == null)
			{
				usersIterator.remove();
				continue;
			}
			
			if (usr.getId() == posterId)
			{
				usersIterator.remove();
				continue;
			}
			
			// notify only to the users who belong to the group(s) associated with forum
			if (sendToGroups && !jforumSecurityService.isJForumFacilitator(category.getContext(), usr.getSakaiUserId())) 
			{
				boolean userInGroup = false;

				Collection sakaiSiteGroups = site.getGroupsWithMember(usr.getSakaiUserId());

				for (Iterator usrGrpIter = sakaiSiteGroups.iterator(); usrGrpIter.hasNext();)
				{
					org.sakaiproject.site.api.Group grp = (org.sakaiproject.site.api.Group) usrGrpIter.next();
					if (topicForum.getGroups().contains(grp.getId()))
					{
						userInGroup = true;
						break;
					}
				}

				if (!userInGroup)
				{
					usersIterator.remove();
					continue;
				}
			}
			
			if ((usr.getEmail() == null || usr.getEmail().trim().length() == 0) || (!jforumSecurityService.isUserActive(category.getContext(), usr.getSakaiUserId())))
			{
				usersIterator.remove();
				continue;
			}
			
			try
			{
				new InternetAddress(usr.getEmail());
			} 
			catch (AddressException e)
			{
				if (logger.isWarnEnabled()) logger.warn("notifyTopicReplyToUsers(...) : "+ usr.getEmail() + " is invalid. And exception is : "+ e);
				usersIterator.remove();
				continue;
			}
		}
		
		if (usersToNotify != null && usersToNotify.size() > 0) 
		{
			// format message text
			Map<String, String> params = new HashMap<String, String>();
			
			// send email
			InternetAddress from = null;
			InternetAddress[] to = null;
			
			String postFrom = "";
			if (post.getPostedBy() != null)
			{
				postFrom = post.getPostedBy().getFirstName() +" "+ topic.getPostedBy().getLastName();		
			}
			
			to = new InternetAddress[usersToNotify.size()];
			int i = 0;
			for (User user: usersToNotify)
			{
				try
				{
					InternetAddress toUserEmail = new InternetAddress(user.getEmail());
					to[i] = toUserEmail;
					i++;
				} 
				catch (AddressException e)
				{
					if (logger.isWarnEnabled()) logger.warn("notifyTopicReplyToUsers(): Topic reply email notification 'toUserEmail' error : "+ e);
				}
			}
			
			String fromUserEmail = "\"" + ServerConfigurationService.getString("ui.service", "Sakai") + "\"<no-reply@" + ServerConfigurationService.getServerName() + ">";
			try
			{
				from = new InternetAddress(fromUserEmail);
			}
			catch (AddressException e1)
			{
				if (logger.isWarnEnabled()) logger.warn("notifyTopicReplyToUsers(): Topic reply email notification 'fromUserEmail' error : "+ e1);
			}
			
			try
			{
				if (site == null)
				{
					site = siteService.getSite(category.getContext());
				}

				params.put("site.title", site.getTitle());
				
				String portalUrl = ServerConfigurationService.getPortalUrl();
				
				//String currToolId = ToolManager.getCurrentPlacement().getId();
				//ToolConfiguration toolConfiguration = site.getTool(currToolId);
				
				ToolConfiguration toolConfiguration = site.getToolForCommonId("sakai.jforum.tool");
				
				String siteNavUrl = portalUrl + "/"+ "site" + "/"+ Web.escapeUrl(site.getId());
				
				if (toolConfiguration != null)
				{
					siteNavUrl = siteNavUrl + "/" + "page" + "/"+ toolConfiguration.getPageId();
				}
				
				params.put("site.url", siteNavUrl);
			}
			catch (IdUnusedException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(e.toString(), e);
				}
			}
			
			params.put("topic.title", topic.getTitle());
			
			params.put("post.from", postFrom);
			params.put("post.subject", post.getSubject());
			
			String postText = post.getText();
			
			// full URL's for smilies etc
			if (postText != null && postText.trim().length() > 0)
			{
				postText = XrefHelper.fullUrls(postText);
			}
			params.put("post.text", postText);
			
			String messageText = EmailUtil.getNewReplyMessageText(params);
			
			String subject = "["+ site.getTitle() +" - New Post] "+ topic.getTitle();
			
			if (messageText != null)
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("notifyTopicReplyToUsers(): Email notification - to.length :"+ to.length);
				}
				
				if ((post != null) && (post.hasAttachments()))
				{
					//email with attachments
					jforumEmailExecutorService.notifyUsers(from, to, subject, messageText, post.getAttachments());
				}
				else
				{
					jforumEmailExecutorService.notifyUsers(from, to, subject, messageText);
				}
			}
			else
			{
				
				if ((post != null) && (post.hasAttachments()))
				{
					//email with attachments
					jforumEmailExecutorService.notifyUsers(from, to, subject, postText, post.getAttachments());
				}
				else
				{
					jforumEmailExecutorService.notifyUsers(from, to, subject, postText);
				}
			}
		}		
	}
	
	/**
	 * remove entry from gradebook
	 * 
	 * @param grade
	 */
	protected void removeEntryFromGradeBook(Grade grade)
	{
		//remove entry from gradebook
		if (grade != null) 
		{
			
			JForumGBService jForumGBService = null;
			jForumGBService = (JForumGBService)ComponentManager.get("org.etudes.api.app.jforum.JForumGBService");
			
			if (jForumGBService == null)
				return;
			
			String gradebookUid = grade.getContext();
			if (jForumGBService.isExternalAssignmentDefined(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId())))
			{
				jForumGBService.removeExternalAssessment(gradebookUid, JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId()));
			}
		}
	}
	
	/**
	 * Update topic dates
	 * 
	 * @param topic		Topic
	 */
	protected void updateDatesTXN(Topic topic)
	{
		String sql = "UPDATE jforum_topics SET start_date = ?, hide_until_open = ?, end_date = ?, allow_until_date = ? WHERE topic_id = ?";
		
		Object[] fields = new Object[5];
		int i = 0;
		
		//fields[i++] = ((topic.getAccessDates() == null) || (topic.getAccessDates().getOpenDate() == null)) ? null : new Timestamp(topic.getAccessDates().getOpenDate().getTime());
		//fields[i++] = ((topic.getAccessDates() == null) || (topic.getAccessDates().getDueDate() == null)) ? null : new Timestamp(topic.getAccessDates().getDueDate().getTime());
		//fields[i++] = ((topic.getAccessDates() != null) && (topic.getAccessDates().isLocked()))? 1 : 0;
		
		if (topic.getAccessDates() != null)
		{
			if (topic.getAccessDates().getOpenDate() == null)
			{
				fields[i++] = null;
				fields[i++] = 0;
			} 
			else
			{
				fields[i++] = new Timestamp(topic.getAccessDates().getOpenDate().getTime());
				fields[i++] = topic.getAccessDates().isHideUntilOpen() ? 1 : 0;
			}
	
			if (topic.getAccessDates().getDueDate() == null)
			{
				fields[i++] = null;
			} 
			else
			{
				fields[i++] = new Timestamp(topic.getAccessDates().getDueDate().getTime());
			}
			
			if (topic.getAccessDates().getAllowUntilDate() == null)
			{
				fields[i++] = null;
			} 
			else
			{
				fields[i++] = new Timestamp(topic.getAccessDates().getAllowUntilDate().getTime());
			}
		}
		else
		{
			fields[i++] = null;
			fields[i++] = 0;
			fields[i++] = null;
			fields[i++] = null;
		}
		
		fields[i++] = topic.getId();

		if (!this.sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("updateDatesTXN: db write failed");
		}
	}
	
	/**
	 * Update existing users info in jforum if changed
	 * 
	 * @param currentSiteUsers	List of current site users in jforum
	 */
	protected void updateExistingUsersInfo(List<User> currentSiteUsers)
	{
		for (User user: currentSiteUsers)
		{
			updateJFUser(user);
		}
	}
	
	/**
	 * Sends topic grades to gradebook
	 * 
	 * @param topic	Topic
	 */
	protected void updateGradeBook(Topic topic)
	{
		Site site = null;
		
		Forum forum = topic.getForum();
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
		
		Grade grade = topic.getGrade();
		
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
		{/*
			Date now = new Date();
						
			Date endDate = null;
			if (topic.getAccessDates() != null)
			{
				endDate = topic.getAccessDates().getDueDate();
			}
			
			if (endDate == null)
			{
				if (forum.getAccessDates() != null)
				{
					endDate = forum.getAccessDates().getDueDate();
				}
				
				if (endDate == null)
				{
					if (category.getAccessDates() != null)
					{
						endDate = category.getAccessDates().getDueDate();
					}
				}
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
				// open date and hide until
				if (topic.getAccessDates() != null && topic.getAccessDates().getOpenDate() != null && topic.getAccessDates().getOpenDate().after(now) && topic.getAccessDates().isHideUntilOpen())
				{
					jforumGBService.removeExternalAssessment(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId()));
					//return;
				}
				
				this.jforumGradeService.updateGradebook(grade, topic);
				
				remove entry in the gradebook and add again if there is no entry with the same name in the gradebook.
				Then publish the scores from grading page.
				
				jforumGBService.removeExternalAssessment(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId()));
				
				
				if (!jforumGBService.isAssignmentDefined(grade.getContext(), topic.getTitle()))
				{
					if (!jforumGBService.addExternalAssessment(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId()), url,  topic.getTitle(), 
							JForumUtil.toDoubleScore(grade.getPoints()), endDate, JForumGradeService.GRADE_SENDTOGRADEBOOK_DESCRIPTION))
					{
						// update isAddToGradeBook of grade
						grade.setAddToGradeBook(false);
						jforumGradeService.modifyTopicGrade(grade);
					}
					else
					{
						this.jforumGradeService.updateGradebook(grade, topic);
					}
				}
				else
				{
					// update isAddToGradeBook of grade
					grade.setAddToGradeBook(false);
					jforumGradeService.modifyTopicGrade(grade);
				}
			}
			else
			{
				// open date and hide until 
				if (topic.getAccessDates() != null && topic.getAccessDates().getOpenDate() != null && topic.getAccessDates().getOpenDate().after(now) && topic.getAccessDates().isHideUntilOpen())
				{
					return;
				}
				
				if (!jforumGBService.isAssignmentDefined(grade.getContext(), topic.getTitle()))
				{
					if (!jforumGBService.addExternalAssessment(grade.getContext(), JForumGradeService.JFORUM_GRADEBOOK_EXTERNAL_ID_CONCAT + String.valueOf(grade.getId()), url, topic.getTitle(), 
							JForumUtil.toDoubleScore(grade.getPoints()), endDate, JForumGradeService.GRADE_SENDTOGRADEBOOK_DESCRIPTION))
					{
						// update isAddToGradeBook of grade
						grade.setAddToGradeBook(false);
						jforumGradeService.modifyTopicGrade(grade);
					}
				}
				else
				{
					// update isAddToGradeBook of grade
					grade.setAddToGradeBook(false);
					jforumGradeService.modifyTopicGrade(grade);
				}		
			}
		*/
			jforumGradeService.updateGradebook(grade, topic);
		}
		else
		{
			if (grade != null)
			{
				// update isAddToGradeBook of grade
				grade.setAddToGradeBook(false);
				jforumGradeService.modifyTopicGrade(grade);
			}
		}
	}
	
	/**
	 * Update user info
	 * 
	 * @param jfuser	Jforum user
	 */
	protected void updateJFUser(User jfuser)
	{
		try
		{
			org.sakaiproject.user.api.User sakUser = UserDirectoryService.getUser(jfuser.getSakaiUserId());

			if (sakUser != null)
			{
				String sakUsrEmail = sakUser.getEmail();
				String sakUsrFname = sakUser.getFirstName();
				String sakUsrLname = sakUser.getLastName();

				String jforumUsrEmail = jfuser.getEmail();
				String jforumUsrFname = jfuser.getFirstName();
				String jforumUsrLname = jfuser.getLastName();

				boolean changed = false, fnameblank = false;
				// if sakai Eid is changed
				/*if (!sakUser.getEid().equalsIgnoreCase(jfuser.getUsername()))
				{
					jfuser.setUsername(sakUser.getEid());
					changed = true;
				}*/

				/*
				 * if sakai user first name and last name are blank then change the jforum user last name as Guest
				 */
				// first name
				if (sakUsrFname != null && sakUsrFname.trim().length() > 0)
				{
					if (jforumUsrFname != null)
					{
						// compare first names
						if (!jforumUsrFname.equals(sakUsrFname))
						{
							jfuser.setFirstName(sakUsrFname);
							changed = true;
						}
					}
					else
					{
						jfuser.setFirstName(sakUsrFname);
						changed = true;
					}
				}
				else
				{
					fnameblank = true;
					jfuser.setFirstName("");
					changed = true;
				}

				// last name
				if (sakUsrLname != null && sakUsrLname.trim().length() > 0)
				{
					if (jforumUsrLname != null)
					{
						// compare last names
						if (!jforumUsrLname.equals(sakUsrLname))
						{
							jfuser.setLastName(sakUsrLname);
							changed = true;
						}
					}
					else
					{
						jfuser.setLastName(sakUsrLname);
						changed = true;
					}
				}
				else
				{
					if (fnameblank)
					{
						jfuser.setLastName("Guest User");
					}
					else
					{
						jfuser.setLastName("");
					}
					changed = true;
				}

				// email
				if (sakUsrEmail != null && sakUsrEmail.trim().length() > 0)
				{
					if (jforumUsrEmail != null)
					{
						if (!jforumUsrEmail.equals(sakUsrEmail))
						{
							jfuser.setEmail(sakUsrEmail);
							changed = true;
						}
					}
					else
					{
						jfuser.setEmail(sakUsrEmail);
						changed = true;
					}
				}
				else
				{
					if (jforumUsrEmail != null && jforumUsrEmail.trim().length() != 0)
					{
						jfuser.setEmail("");
						changed = true;
					}
				}

				if (changed)
				{
					jforumUserService.modifyUserSakaiInfo(jfuser);
				}
			}
		}
		catch (UserNotDefinedException e)
		{
			// if (logger.isWarnEnabled()) logger.warn(e, e);
		}
	}
	
	/**
	 * Update users info in jforum if sakai user information is modified
	 * 
	 * @param siteId	Site id
	 * 
	 * @return	List of users
	 */
	protected List<User> updateUsersInfo (String siteId)
	{
		Site site = null;
		
		try
		{
			site = siteService.getSite(siteId);
		}
		catch (IdUnusedException e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("updateUsersInfo: missing site: " + siteId);
			}
		}
		
		if (site == null)
		{
			return null;
		}
		// update users info in the jforum
		Set<String> siteUsers = site.getUsers();
		
		//remove users that are deleted in sakai
		Iterator<String> sakUserIterator = siteUsers.iterator();
		while (sakUserIterator.hasNext())
		{
			String userId = (String) sakUserIterator.next();
			try
			{
				UserDirectoryService.getUser(userId);
			}
			catch (UserNotDefinedException e)
			{
				sakUserIterator.remove();
			}
		}
		
		List<String> allsiteusers = new ArrayList<String>();
		
		allsiteusers.addAll(siteUsers);
		List<String> copyallsiteusers = new ArrayList<String>();
		Iterator<String> copyiter = allsiteusers.iterator();
		while (copyiter.hasNext())
		{
			String copyname = (copyiter.next()).toLowerCase();
			copyallsiteusers.add(copyname);
		}
		
		/* for existing site users */
		List<User> exissiteusers = new ArrayList<User>();
		
		List<User> users = jforumUserService.getSiteUsers(siteId);

		//find new users
		Iterator<User> iter = users.iterator();
		while (iter.hasNext())
		{
			User checkusr = iter.next();

			if (allsiteusers.contains(checkusr.getSakaiUserId().toLowerCase()))
			{
				/* remove from allsiteusers as user is existing */
				allsiteusers.remove(checkusr.getSakaiUserId().toLowerCase());

				/* add existing site users */
				exissiteusers.add(checkusr);
			}
		}
		
		//Don't create new users here
		//List newUsers = createNewUsers(allsiteusers);
		
		updateExistingUsersInfo(exissiteusers);		
		
		//users.addAll(newUsers);
		
		/*dropped students remove from users */
		User adminuser = dropUsers(users, copyallsiteusers);

		if (adminuser != null) users.remove(adminuser);
       
		//Collections.sort(users,new UserOrderComparator());
		
		return users;
	}
}
