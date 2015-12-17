/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/view/forum/PostAction.java $ 
 * $Id: PostAction.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
 * Portions completed before July 1, 2004 Copyright (c) 2003, 2004 Rafael Steil, All rights reserved, licensed under the BSD license. 
 * http://www.opensource.org/licenses/bsd-license.php 
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met: 
 * 
 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following disclaimer. 
 * 2) Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution. 
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE 
 ***********************************************************************************/
package org.etudes.jforum.view.forum;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.JForumAccessException;
import org.etudes.api.app.jforum.JForumAttachmentBadExtensionException;
import org.etudes.api.app.jforum.JForumAttachmentOverQuotaException;
import org.etudes.api.app.jforum.JForumCategoryService;
import org.etudes.api.app.jforum.JForumForumService;
import org.etudes.api.app.jforum.JForumGBService;
import org.etudes.api.app.jforum.JForumGradesModificationException;
import org.etudes.api.app.jforum.JForumItemNotFoundException;
import org.etudes.api.app.jforum.JForumPostService;
import org.etudes.api.app.jforum.JForumSecurityService;
import org.etudes.api.app.jforum.JForumUserService;
import org.etudes.jforum.Command;
import org.etudes.jforum.JForum;
import org.etudes.jforum.SessionFacade;
import org.etudes.jforum.dao.DataAccessDriver;
import org.etudes.jforum.dao.EvaluationDAO;
import org.etudes.jforum.dao.ForumDAO;
import org.etudes.jforum.dao.GradeDAO;
import org.etudes.jforum.dao.PostDAO;
import org.etudes.jforum.dao.TopicDAO;
import org.etudes.jforum.entities.Category;
import org.etudes.jforum.entities.Evaluation;
import org.etudes.jforum.entities.Forum;
import org.etudes.jforum.entities.Grade;
import org.etudes.jforum.entities.Post;
import org.etudes.jforum.entities.SpecialAccess;
import org.etudes.jforum.entities.Topic;
import org.etudes.jforum.entities.UserSession;
import org.etudes.jforum.repository.ForumRepository;
import org.etudes.jforum.repository.PostRepository;
import org.etudes.jforum.repository.SmiliesRepository;
import org.etudes.jforum.repository.TopicRepository;
import org.etudes.jforum.util.I18n;
import org.etudes.jforum.util.JForumUtil;
import org.etudes.jforum.util.date.DateUtil;
import org.etudes.jforum.util.legacy.commons.fileupload.disk.DiskFileItem;
import org.etudes.jforum.util.preferences.ConfigKeys;
import org.etudes.jforum.util.preferences.SakaiSystemGlobals;
import org.etudes.jforum.util.preferences.SystemGlobals;
import org.etudes.jforum.util.preferences.TemplateKeys;
import org.etudes.jforum.util.user.JForumUserUtil;
import org.etudes.jforum.view.forum.common.AttachmentCommon;
import org.etudes.jforum.view.forum.common.TopicsCommon;
import org.etudes.jforum.view.forum.common.ViewCommon;
import org.sakaiproject.authz.cover.SecurityService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.cover.SiteService;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.sakaiproject.util.Validator;


/**
 * @author Rafael Steil
 * 01/12/2006 Murthy - updated downloadAttach() method writing the response
 * 1/31/06 - Mallika - making changes for quotalimit
 * 1/25/06 - Mallika - adding nowDate variable
 * 08/08/06 - Murthy - updated for task topic
 * Mallika - 9/27/06 - eliminating views column
 * Mallika - 10/2/06 - Adding markAllTime from session to page
 * 11/09/06 - Murthy - commented the updateAttachment code in downloadAttach method
* Mallika - 11/9/06 - Adding updateTopicReadFlags method
 * Mallika - 11/13/06  - Changing compareDate to markAllDate
 * Mallika - 11/15/06 - Adding code for markTime
 * 11/20/06 - Murthy - code added to notify users whose preference under my profile, 
 *                 "Notify me when new topic is posted" is selected
 */
/**
 * @author Murthy Tanniru
 * @version $Id: PostAction.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $
 */
public class PostAction extends Command 
{
	private static final Log logger = LogFactory.getLog(PostAction.class);

	public void list() throws Exception
	{
		int topicId = this.request.getIntParameter("topic_id");	
		
		try
		{
		
			if (logger.isDebugEnabled())
			{
				logger.debug("Entering list()...");
				logger.debug("Listing posts for topic id = " + topicId);
			}
			
			if (topicId <= 0) 
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("topic " + topicId + " has id <= 0");
				}
				this.topicNotFound();
				return;
			}
			
			int count = SakaiSystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE);
			int startFrom = ViewCommon.getStartPage();
			
			JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
			
			List<org.etudes.api.app.jforum.Post> posts = null;
			try
			{
				posts = jforumPostService.getTopicPosts(topicId, startFrom, count, UserDirectoryService.getCurrentUser().getId());
			}
			catch (JForumAccessException e)
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}
			
			org.etudes.api.app.jforum.Topic topic = null;
			
			if (posts.isEmpty())
			{
				try
				{
					topic = jforumPostService.getTopic(topicId, UserDirectoryService.getCurrentUser().getId());
				}
				catch (JForumAccessException e)
				{
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}
			else
			{
				topic = posts.get(0).getTopic();
			}
			
			if (topic == null)
			{
				if (logger.isErrorEnabled())
				{
					logger.error("topic with id " + topicId + " not found");
				}
				
				this.topicNotFound();
				return;
			}
			
			org.etudes.api.app.jforum.Forum forum = null;
			forum = topic.getForum();	
			
			boolean gradeForum = (forum.getGradeType() == Forum.GRADE_BY_FORUM);
			this.context.put("gradeForum", gradeForum);
			
			boolean gradeTopic = (forum.getGradeType() == Forum.GRADE_BY_TOPIC) && topic.isGradeTopic();
			this.context.put("gradeTopic", gradeTopic);
			
			org.etudes.api.app.jforum.Category category = forum.getCategory();
			boolean gradeCategory = category.isGradable();
			this.context.put("gradeCategory", gradeCategory);
			
			this.context.put("topic", topic);
			this.context.put("forum", forum) ;
			this.context.put("category", category);
			
			boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
			
			if (!isfacilitator && gradeTopic && topic.getBlocked())
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}
			
			List<Integer> facilitatorsList = new ArrayList<Integer>();
			if (gradeForum || gradeTopic || gradeCategory) 
			{
				this.context.put("facilitators", facilitatorsList);
			}
			
			if (posts.size() > 0)
			{
				JForumSecurityService jforumSecurityService = (JForumSecurityService)ComponentManager.get("org.etudes.api.app.jforum.JForumSecurityService");
				boolean facilitator = false;
				for (org.etudes.api.app.jforum.Post post : posts)
				{
					facilitator = false;
					facilitator = jforumSecurityService.isJForumFacilitator(ToolManager.getCurrentPlacement().getContext(), post.getPostedBy().getSakaiUserId());
					if (facilitator)
					{
						facilitatorsList.add(post.getPostedBy().getId());
					}
				}
			}
			
			this.context.put("posts", posts);
			
			this.context.put("attachmentsEnabled", true);
			this.context.put("canDownloadAttachments", true);
			this.context.put("canRemove", isfacilitator);
			this.context.put("canEdit", isfacilitator);
			this.context.put("isAdmin", isfacilitator);
			
			this.context.put("watching", jforumPostService.isUserSubscribedToTopic(topicId, UserDirectoryService.getCurrentUser().getId()));
				
			// Topic Status
			this.context.put("STATUS_LOCKED", new Integer(Topic.STATUS_LOCKED));
			this.context.put("STATUS_UNLOCKED", new Integer(Topic.STATUS_UNLOCKED));
	
			// Pagination
			int totalPosts = jforumPostService.getTotalPosts(topic.getId());
			ViewCommon.contextToPagination(startFrom, totalPosts, count);
			
			GregorianCalendar gc = new GregorianCalendar();
			this.context.put("nowDate", gc.getTime());
			Date lastVisitDate = SessionFacade.getUserSession().getLastVisit();
		    this.context.put("lastVisit", lastVisitDate);
		    
		    JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		    org.etudes.api.app.jforum.User user = jforumUserService.getBySakaiUserId(UserDirectoryService.getCurrentUser().getId());
		    
		    Date markAllReadTime = jforumUserService.getMarkAllReadTime(category.getContext(), user.getId());
	
	        Date compareDate = null;
			
			//Date UserTopicMarkTime = jforumPostService.getUserTopicMarkTime(topic.getId(), UserDirectoryService.getCurrentUser().getId());
	        org.etudes.api.app.jforum.Topic topicUserMarkTime = jforumPostService.getUserTopicMarkTime(topic.getId(), UserDirectoryService.getCurrentUser().getId());
	        
	        Date userTopicMarkTime = null;
	        
	        if (topicUserMarkTime != null)
	        {
	        	userTopicMarkTime =  topicUserMarkTime.getTime();
	        }
	
			if ((markAllReadTime == null) && (userTopicMarkTime == null))
			{
				compareDate = null;
			}
			else
			{
				/*if (markAllReadTime == null)
				{
					compareDate = UserTopicMarkTime;
				}
				
				if (UserTopicMarkTime == null)
				{
					compareDate = markAllReadTime;
				}*/
				
				if ((markAllReadTime != null) && (userTopicMarkTime != null))
				{
					if (markAllReadTime.getTime() > userTopicMarkTime.getTime())
					{
						compareDate = markAllReadTime;
					}
					else
					{
						compareDate = userTopicMarkTime;
					}
				}
				else if ((markAllReadTime == null) && (userTopicMarkTime != null))
				{
					compareDate = userTopicMarkTime;
				}
				else if ((markAllReadTime != null) && (userTopicMarkTime == null))
				{
					compareDate = markAllReadTime;
				}
	
			}
	
			if (compareDate != null)
			{
				this.context.put("compareDate", compareDate);
			}
			
			this.context.put("sju", new JForumUserUtil());
			
			JForumCategoryService jforumCategoryService = (JForumCategoryService) ComponentManager.get("org.etudes.api.app.jforum.JForumCategoryService");
			List<org.etudes.api.app.jforum.Category> categories = jforumCategoryService.getUserContextCategories(category.getContext(), UserDirectoryService.getCurrentUser().getId());
			this.context.put("allCategories", categories);
		    
		    // Set the topic status as read
		    jforumPostService.markTopicRead(topic.getId(), UserDirectoryService.getCurrentUser().getId(), new Date(), true);
		    
		    // 12/17/2012 - To fix chrome loading of youtube video's immediately after posting(chrome issue - Refused to execute a JavaScript script. Source code of script found within request.)
		    this.response.setIntHeader("X-XSS-Protection", 0);
		    
		    this.setTemplateName(TemplateKeys.POSTS_LIST);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e.toString(), e);
			}
			
			throw e;
			
			/*if (topicId > 0)
			{
				org.etudes.api.app.jforum.Topic topic = null;
				try
				{
					JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
					topic = jforumPostService.getTopic(topicId, UserDirectoryService.getCurrentUser().getId());
				}
				catch (JForumAccessException e1)
				{
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
				
				if (topic != null)
				{
					org.etudes.api.app.jforum.Forum forum = topic.getForum();
					
					if (forum != null)
					{
						String path = this.request.getContextPath() +"/forums/show/"+ forum.getId() + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION);
						
						JForum.setRedirect(path);
					}
				}
			}*/
		}
	}

	/**
	 * review
	 * 
	 * @throws Exception
	 */
	public void review() throws Exception 
	{
		
		/*PostDAO pm = DataAccessDriver.getInstance().newPostDAO();
		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
		TopicDAO tm = DataAccessDriver.getInstance().newTopicDAO();*/

		//int userId = SessionFacade.getUserSession().getUserId();
		int topicId = this.request.getIntParameter("topic_id");
		/*Topic topic = tm.selectById(topicId);

		if (!TopicsCommon.isTopicAccessible(topic.getForumId())) {
			return;
		}*/

		int count = SakaiSystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE);
		int start = ViewCommon.getStartPage();

		/*Map usersMap = new HashMap();
		List helperList = PostCommon.topicPosts(pm, um, usersMap, false, userId, topic.getId(), start, count);
		Collections.reverse(helperList);*/
				
		JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		
		List<org.etudes.api.app.jforum.Post> posts = null;
		try
		{
			posts = jforumPostService.getTopicPosts(topicId, start, count, UserDirectoryService.getCurrentUser().getId());
		}
		catch (JForumAccessException e)
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		
		Collections.reverse(posts);

		this.setTemplateName(SystemGlobals.getValue(ConfigKeys.TEMPLATE_DIR) + "/empty.htm");

		this.setTemplateName(TemplateKeys.POSTS_REVIEW);
		this.context.put("posts", posts);
		//this.context.put("users", usersMap);
	}

	/**
	 * topic not found message 
	 */
	private void topicNotFound() {
		this.setTemplateName(TemplateKeys.POSTS_TOPIC_NOT_FOUND);
		this.context.put("message", I18n.getMessage("PostShow.TopicNotFound"));
	}

	/**
	 * posts not found
	 */
	private void postNotFound() {
		this.setTemplateName(TemplateKeys.POSTS_POST_NOT_FOUND);
		this.context.put("message", I18n.getMessage("PostShow.PostNotFound"));
	}

	/**
	 * reply only
	 */
	private void replyOnly()
	{
		this.setTemplateName(TemplateKeys.POSTS_REPLY_ONLY);
		this.context.put("message", I18n.getMessage("PostShow.replyOnly"));
	}

	/**
	 * insert
	 * 
	 * @throws Exception
	 */
	public void insert() throws Exception
	{
		if(logger.isDebugEnabled()) logger.debug("starting insert()");
		int forumId = this.request.getIntParameter("forum_id");
		
		boolean facilitator = false;
		
		//facilitator can add all type of topics 
		if (JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser())
		{
			facilitator = true;
		}
		
		boolean participant = JForumUserUtil.isJForumParticipant(UserDirectoryService.getCurrentUser().getId());	
		
		if (!(facilitator || participant))
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		
		JForumForumService jforumForumService = (JForumForumService)ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
		
		org.etudes.api.app.jforum.Forum forum = null;
		try
		{
			forum = jforumForumService.getForum(forumId, UserDirectoryService.getCurrentUser().getId());
		}
		catch (JForumAccessException e)
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		
		if (!facilitator)
		{
			if (forum.getType() == org.etudes.api.app.jforum.Forum.ForumType.READ_ONLY.getType())
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}
		}
		
		if (this.request.getParameter("topic_id") != null) 
		{
			int topicId = this.request.getIntParameter("topic_id");
			
			JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
			org.etudes.api.app.jforum.Topic topic;
			try
			{
				topic = jforumPostService.getTopic(topicId, UserDirectoryService.getCurrentUser().getId());
			}
			catch (JForumAccessException e)
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}
			
			if (!topic.mayPost())
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}			
					
			this.context.put("topic", topic);
		}
		else
		{
			if (!jforumForumService.isUserHasCreateTopicAccess(forumId, UserDirectoryService.getCurrentUser().getId()))
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}
			
			this.context.put("firstpost", true);
		}
		
		org.etudes.api.app.jforum.Category category = forum.getCategory();
		
		// topic dates. If category and forum have no dates then dates are allowed for the topics
		if (facilitator)
		{
			if (this.request.getParameter("topic_id") == null) 
			{
				if (((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() == null) && (category.getAccessDates().getDueDate() == null) && (category.getAccessDates().getAllowUntilDate() == null)) 
						&& ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() == null) && (forum.getAccessDates().getDueDate() == null) && (forum.getAccessDates().getAllowUntilDate() == null)))
				{
					this.context.put("setDates", true);
				}
				else if ((category.getAccessDates() == null) && (category.getAccessDates() == null))
				{
					this.context.put("setDates", true);
				}
				else 
				{
					this.context.put("setDates", false);
				}
				
				this.context.put("setType", true);
				
				if (forum.getGradeType() == org.etudes.api.app.jforum.Grade.GradeType.TOPIC.getType())
				{
					this.context.put("canAddEditGrade", true);
					Site site = SiteService.getSite(ToolManager.getCurrentPlacement().getContext());
					if (site.getToolForCommonId(SakaiSystemGlobals.getValue(ConfigKeys.GRADEBOOK_TOOL_ID)) != null)
					{
						this.context.put("enableGrading", Boolean.TRUE);
					}
				}
				
				addGradeTypesToContext();
			}
			else
			{
				this.context.put("setType", false);
			}
		}
		

		/*if (!TopicsCommon.isTopicAccessible(forumId)) {
			return;
		}

		Forum forum = ForumRepository.getForum(forumId);
		
		if (this.isForumTypeReadonly(forum))
		{
				return;
		}
		Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		
		SpecialAccess specialAccess = getUserSpecialAccess(forum);
		boolean specialAccessUser = ((specialAccess != null)? true : false);
				
		if ((!specialAccessUser) && (this.isForumLocked(forum) || !this.isForumOpen(forum) || this.isCategoryLocked(category) || !this.isCategoryOpen(category)))
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		
		// forum special access user - verify access
		if (specialAccessUser)
		{
			boolean specialAccessUserAccess = false;
		
			Date currentTime = Calendar.getInstance().getTime();
			if ((specialAccess.getStartDate() != null) && (specialAccess.getStartDate().after(currentTime)))
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}
			
			if (((specialAccess.getEndDate() != null) && (specialAccess.getEndDate().before(currentTime))) && specialAccess.isLockOnEndDate())
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}
		}
				
		boolean facilitator = false;
		//facilitator can add all type of topics 
		if (JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser())
			facilitator = true;
		
		JForumService jforumService = (JForumService)ComponentManager.get("org.etudes.api.app.jforum.JForumService");
		
		if (!facilitator)
		{	
			// ignore forums with category invalid dates
			if ((category.getStartDate() != null) && (category.getEndDate() != null))
			{
				if (category.getStartDate().after(category.getEndDate()))
				{
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}
			
			// ignore forums with invalid dates
			if ((forum.getStartDate() != null) && (forum.getEndDate() != null))
			{
				if (forum.getStartDate().after(forum.getEndDate()))
				{
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}
			
			// check course map category and forum access 
			if (category.isGradeCategory() && jforumService != null)
			{ 
				UserSession currentUser = SessionFacade.getUserSession();
				Boolean checkAccess = jforumService.checkItemAccess(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_CATEGORY +"-"+ String.valueOf(category.getId()), currentUser.getSakaiUserId());
				
				if (!checkAccess)
				{
					category.setBlocked(true);
					category.setBlockedByTitle(jforumService.getItemAccessMessage(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_CATEGORY +"-"+ String.valueOf(category.getId()), currentUser.getSakaiUserId()));
					category.setBlockedByDetails(jforumService.getItemAccessDetails(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_CATEGORY +"-"+ String.valueOf(category.getId()), currentUser.getSakaiUserId()));
					
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized") +"</br>"+ I18n.getMessage("Prerequisite.Alert") +" "+ category.getBlockedByTitle());
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}
			if (forum.getGradeType() == Forum.GRADE_BY_FORUM && jforumService != null)
			{ 
				UserSession currentUser = SessionFacade.getUserSession();
				Boolean checkAccess = jforumService.checkItemAccess(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_FORUM +"-"+ String.valueOf(forum.getId()), currentUser.getSakaiUserId());
				
				if (!checkAccess)
				{
					forum.setBlocked(true);
					forum.setBlockedByTitle(jforumService.getItemAccessMessage(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_FORUM +"-"+ String.valueOf(forum.getId()), currentUser.getSakaiUserId()));
					forum.setBlockedByDetails(jforumService.getItemAccessDetails(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_FORUM +"-"+ String.valueOf(forum.getId()), currentUser.getSakaiUserId()));
					
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized") +"</br>"+ I18n.getMessage("Prerequisite.Alert") +" "+ forum.getBlockedByTitle());
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}
		}
		
		addGradeTypesToContext();
		
		if (this.request.getParameter("topic_id") != null) 
		{
			int topicId = this.request.getIntParameter("topic_id");
			Topic t = DataAccessDriver.getInstance().newTopicDAO().selectRaw(topicId);
			
			if (!facilitator)
			{
				// check course map topic access 
				if (forum.getGradeType() == Forum.GRADE_BY_TOPIC && jforumService != null)
				{ 
					UserSession currentUser = SessionFacade.getUserSession();
					Boolean checkAccess = jforumService.checkItemAccess(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_TOPIC +"-"+ String.valueOf(t.getId()), currentUser.getSakaiUserId());
					
					if (!checkAccess)
					{
						t.setBlocked(true);
						t.setBlockedByTitle(jforumService.getItemAccessMessage(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_TOPIC +"-"+ String.valueOf(t.getId()), currentUser.getSakaiUserId()));
						t.setBlockedByDetails(jforumService.getItemAccessDetails(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_TOPIC +"-"+ String.valueOf(t.getId()), currentUser.getSakaiUserId()));
						
						this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized") +"</br>"+ I18n.getMessage("Prerequisite.Alert") +" "+ t.getBlockedByTitle());
						this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
						return;
					}
				}
				
				if (t.getStatus() == Topic.STATUS_LOCKED) 
				{
					this.topicLocked();
					return;
				}
				
				GregorianCalendar gc = new GregorianCalendar();
				
				// topic special access
				boolean topicSpecialAccessUser = false;
				if (!specialAccessUser)
				{
					specialAccess = getUserTopicSpecialAccess(t);
					topicSpecialAccessUser = ((specialAccess != null)? true : false);
				}
				
				Date currentTime = Calendar.getInstance().getTime();
				if (topicSpecialAccessUser)
				{
					boolean specialAccessUserAccess = false;
					
					if (!specialAccess.isOverrideStartDate())
					{
						specialAccess.setStartDate(t.getStartDate());
					}
					if (!specialAccess.isOverrideEndDate())
					{
						specialAccess.setEndDate(t.getEndDate());
					}
					if (!specialAccess.isOverrideLockEndDate())
					{
						specialAccess.setLockOnEndDate(t.isLockTopic());
					}
					
					if ((specialAccess.getStartDate() != null) && (specialAccess.getStartDate().after(currentTime)))
					{
						this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
						this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
						return;
					}
					
					if (((specialAccess.getEndDate() != null) && (specialAccess.getEndDate().before(currentTime))) && specialAccess.isLockOnEndDate())
					{
						this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
						this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
						return;
					}
				}
				else
				{
					if ((t.getStartDate() != null) && (t.getStartDate().after(currentTime)))
					{
						this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
						this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
						return;
					}
					
				}
			
				if ((!topicSpecialAccessUser) && (t.getEndDate() != null) && (t.getEndDate().before(gc.getTime()) && t.isLockTopic()))
				{
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}

			this.context.put("topic", t);
			this.context.put("setType", false);
		}
		else 
		{

			this.context.put("setType", true);
			if (this.isForumGradeByTopic(forum)) 
			{
				this.context.put("canAddEditGrade", true);
				Site site = SiteService.getSite(ToolManager.getCurrentPlacement().getContext());
				if (site.getToolForCommonId(SakaiSystemGlobals.getValue(ConfigKeys.GRADEBOOK_TOOL_ID)) != null)
				{
					this.context.put("enableGrading", Boolean.TRUE);
				}
			}
			
			// topic dates. If category and forum have no dates then dates are allowed for the topics
			if (facilitator)
			{
				if (((category.getStartDate() == null) && (category.getEndDate() ==null)) && ((forum.getStartDate() == null) && (forum.getEndDate() ==null)))
				{
					this.context.put("setDates", true);
				}
				else
				{
					this.context.put("setDates", false);
				}
			}
		}

		int userId = SessionFacade.getUserSession().getUserId();

		this.setTemplateName(TemplateKeys.POSTS_INSERT);

		boolean attachmentsEnabled = true;
		this.context.put("attachmentsEnabled", attachmentsEnabled);

		if (attachmentsEnabled) {
			
			this.context.put("maxAttachmentsSize", new Long(SakaiSystemGlobals.getIntValue(ConfigKeys.ATTACHMENTS_QUOTA_LIMIT) * 1024 * 1024));
			
			this.context.put("maxAttachments", SakaiSystemGlobals.getValue(ConfigKeys.ATTACHMENTS_MAX_POST));
		}

		boolean needCaptcha = SystemGlobals.getBoolValue(ConfigKeys.CAPTCHA_POSTS);

		if (needCaptcha) {
			SessionFacade.getUserSession().createNewCaptcha();
		}*/
		
		boolean attachmentsEnabled = true;
		this.context.put("attachmentsEnabled", attachmentsEnabled);

		if (attachmentsEnabled) 
		{
			
			this.context.put("maxAttachmentsSize", new Long(SakaiSystemGlobals.getIntValue(ConfigKeys.ATTACHMENTS_QUOTA_LIMIT) * 1024 * 1024));
			
			this.context.put("maxAttachments", SakaiSystemGlobals.getValue(ConfigKeys.ATTACHMENTS_MAX_POST));
		}

		this.context.put("forum", forum);
		this.context.put("category", forum.getCategory());
		this.context.put("action", "insertSave");
		this.context.put("start", this.request.getParameter("start"));
		this.context.put("isNewPost", true);
		//this.context.put("needCaptcha", needCaptcha);
		this.context.put("htmlAllowed", true);
		this.context.put("canCreateStickyOrAnnouncementTopics", facilitator);
		this.context.put("canCreateTaskTopics", facilitator);
		
		/*User user = DataAccessDriver.getInstance().newUserDAO().selectById(userId);
		user.setSignature(PostCommon.processText(user.getSignature()));
		user.setSignature(PostCommon.processSmilies(user.getSignature(), SmiliesRepository.getSmilies()));

		if (this.request.getParameter("preview") != null) 
		{
			user.setNotifyOnMessagesEnabled(this.request.getParameter("notify") != null);
		}*/
		
		JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		org.etudes.api.app.jforum.User user = jforumUserService.getBySakaiUserId(UserDirectoryService.getCurrentUser().getId());

		this.context.put("user", user);
		
		//TODO: editor - FCKEditor or CKEditor from sakai.properties (wysiwyg.editor=FCKeditor or wysiwyg.editor=CKeditor)
		
		this.setTemplateName(TemplateKeys.POSTS_INSERT);
	}

	/**
	 * edit the post
	 * @throws Exception
	 */
	/*public void edit() throws Exception {
		this.edit(false, null);
	}*/
	
	public void edit() throws Exception 
	{
		this.edit(null);
	}

	/**
	 * edit the post
	 * @param preview
	 * @param p
	 * @throws Exception
	 */
	/*private void edit(boolean preview, Post p) throws Exception
	{*/
	public void edit(org.etudes.api.app.jforum.Post post) throws Exception
	{
		boolean facilitator = false;
		
		//facilitator can add all type of topics 
		if (JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser())
		{
			facilitator = true;
		}
		
		boolean participant = JForumUserUtil.isJForumParticipant(UserDirectoryService.getCurrentUser().getId());	
		
		if (!(facilitator || participant))
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		
		int postId = this.request.getIntParameter("post_id");
		
		JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		
		//org.etudes.api.app.jforum.Post post = null;
		if (post == null)
		{
			post = jforumPostService.getPost(postId);
		}
		
		if (post == null)
		{
			this.postNotFound();
			return;
		}
		
		org.etudes.api.app.jforum.Topic topic = null;
			
		try
		{
			topic = jforumPostService.getTopic(post.getTopicId(), UserDirectoryService.getCurrentUser().getId());
		}
		catch (JForumAccessException e)
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		
		if (topic == null)
		{
			this.topicNotFound();
			return;
		}
		
		int userId = SessionFacade.getUserSession().getUserId();
		boolean canAccess = (facilitator || post.getUserId() == userId);
		
		if (!canAccess)
		{
			this.setTemplateName(TemplateKeys.POSTS_EDIT_CANNOTEDIT);
			this.context.put("message", I18n.getMessage("CannotEditPost"));
			return;
		}
		
		if (participant && !topic.mayPost())
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;		
		}
		
		if (post.hasAttachments())
		{
			this.context.put("attachments", post.getAttachments());
		}
		this.context.put("attachmentsEnabled", true);
		
		// 11/14/2011 Murthy - To render html code properly in the edit mode
		String postText = post.getText();
		postText = Validator.escapeHtmlFormattedTextarea(postText);
		post.setText(postText);
		
		// To render html code in post attachments description to display properly
		for (org.etudes.api.app.jforum.Attachment attachment : post.getAttachments())
		{
			if (attachment.getInfo() != null && attachment.getInfo().getComment() != null)
			{
				String attachmentComment = attachment.getInfo().getComment();
				attachmentComment = Validator.escapeHtmlFormattedTextarea(attachmentComment);
				attachment.getInfo().setComment(attachmentComment);
			}
		}
		
		org.etudes.api.app.jforum.Forum forum = topic.getForum();
		org.etudes.api.app.jforum.Category category = forum.getCategory();

		this.context.put("maxAttachmentsSize", new Long(SakaiSystemGlobals.getIntValue(ConfigKeys.ATTACHMENTS_QUOTA_LIMIT) * 1024 * 1024));
		this.context.put("maxAttachments", SakaiSystemGlobals.getValue(ConfigKeys.ATTACHMENTS_MAX_POST));
		this.context.put("forum", forum);
		this.context.put("category", category);
		this.context.put("action", "editSave");
		this.context.put("post", post);
		this.context.put("setType", post.getId() == topic.getFirstPostId());
		this.context.put("editPost", true);

		if (post.getId() == topic.getFirstPostId())
		{

			if (((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() == null) && (category.getAccessDates().getDueDate() == null) && (category.getAccessDates().getAllowUntilDate() == null)) 
					&& ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() == null) && (forum.getAccessDates().getDueDate() == null) && (forum.getAccessDates().getAllowUntilDate() == null)))
			{
				if (facilitator)
				{
					this.context.put("setDates", true);
				}
			}
			else
			{
				this.context.put("setDates", false);
			}
		}

		addGradeTypesToContext();
		
		if ((post.getId() == topic.getFirstPostId()) && forum.getGradeType() == org.etudes.api.app.jforum.Grade.GradeType.TOPIC.getType())
		{
			this.context.put("canAddEditGrade", true);

			if (topic.isGradeTopic())
			{
				this.context.put("grade", topic.getGrade());
			}
			Site site = SiteService.getSite(ToolManager.getCurrentPlacement().getContext());
			if (site.getToolForCommonId(SakaiSystemGlobals.getValue(ConfigKeys.GRADEBOOK_TOOL_ID)) != null)
			{
				this.context.put("enableGrading", Boolean.TRUE);
			}
		}

		this.context.put("topic", topic);
		this.setTemplateName(TemplateKeys.POSTS_EDIT);
		this.context.put("start", this.request.getParameter("start"));
		this.context.put("htmlAllowed", true);

		this.context.put("canCreateStickyOrAnnouncementTopics", facilitator);

		this.context.put("canCreateTaskTopics", facilitator);
		
		JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");

		org.etudes.api.app.jforum.User u = jforumUserService.getBySakaiUserId(post.getPostedBy().getSakaiUserId());
		this.context.put("user", u);
	}

	/**
	 * quote
	 * 
	 * @throws Exception
	 */
	public void quote() throws Exception 
	{
		boolean facilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		boolean participant = JForumUserUtil.isJForumParticipant(UserDirectoryService.getCurrentUser().getId());	
		
		if (!(facilitator || participant))
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		
		JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		
		org.etudes.api.app.jforum.Post post = null;
		
		post = jforumPostService.getPost(this.request.getIntParameter("post_id"));
		
		if (post == null)
		{
			this.postNotFound();
			return;
		}
		
		org.etudes.api.app.jforum.Topic topic = null;
			
		try
		{
			topic = jforumPostService.getTopic(post.getTopicId(), UserDirectoryService.getCurrentUser().getId());
		}
		catch (JForumAccessException e)
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		
		if (topic == null)
		{
			this.topicNotFound();
			return;
		}
		
		if (!topic.mayPost())
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;		
		}
		
		String postText = post.getText();
		postText = Validator.escapeHtmlFormattedTextarea(postText);
		post.setText(postText);

		JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");

		org.etudes.api.app.jforum.User u = jforumUserService.getBySakaiUserId(post.getPostedBy().getSakaiUserId());
		
		this.context.put("forum", topic.getForum());
		this.context.put("action", "insertSave");
		this.context.put("post", post);
		this.context.put("attachmentsEnabled", true);
		this.context.put("maxAttachmentsSize", new Long(SakaiSystemGlobals.getIntValue(ConfigKeys.ATTACHMENTS_QUOTA_LIMIT) * 1024 * 1024));
		this.context.put("maxAttachments", SakaiSystemGlobals.getValue(ConfigKeys.ATTACHMENTS_MAX_POST));
		this.context.put("isNewPost", true);
		this.context.put("topic", topic);
		this.context.put("quote", "true");
		this.context.put("quoteUser", u);
		this.setTemplateName(TemplateKeys.POSTS_QUOTE);
		this.context.put("setType", false);
		this.context.put("htmlAllowed", true);
		this.context.put("start", this.request.getParameter("start"));
		this.context.put("user", jforumUserService.getBySakaiUserId(UserDirectoryService.getCurrentUser().getId()));
		
		/*PostDAO pm = DataAccessDriver.getInstance().newPostDAO();
		Post p = pm.selectById(this.request.getIntParameter("post_id"));

		boolean facilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();

		if (!this.anonymousPost(p.getForumId()))
		{
			if (!facilitator)
				return;
		}

		Topic t = DataAccessDriver.getInstance().newTopicDAO().selectRaw(p.getTopicId());

		if (!TopicsCommon.isTopicAccessible(t.getForumId()))
		{
			if (!facilitator)
				return;
		}

		if (t.getStatus() == Topic.STATUS_LOCKED)
		{
			if (!facilitator)
			{
				this.topicLocked();
				return;
			}
		}

		if (p.getId() == 0)
		{
			this.postNotFound();
			return;
		}

		if (p.isModerationNeeded())
		{
			if (!facilitator)
			{
				this.notModeratedYet();
				return;
			}
		}

		Forum forum = ForumRepository.getForum(t.getForumId());
		Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		SpecialAccess specialAccess = getUserSpecialAccess(forum);
		boolean specialAccessUser = ((specialAccess != null) ? true : false);

		if ((!specialAccessUser)
				&& (this.isForumLocked(forum) || !this.isForumOpen(forum) || this.isCategoryLocked(category) || !this.isCategoryOpen(category)))
		{
			this.context.put("errorMessage", I18n.getMessage("Forum.Locked"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}

		// special access user - verify access
		if (specialAccessUser)
		{
			boolean specialAccessUserAccess = false;

			Date currentTime = Calendar.getInstance().getTime();
			if ((specialAccess.getStartDate() != null) && (specialAccess.getStartDate().after(currentTime)))
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}

			if (((specialAccess.getEndDate() != null) && (specialAccess.getEndDate().before(currentTime))) && specialAccess.isLockOnEndDate())
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}
		}

		// 11/14/2011 Murthy - To render html code properly in the edit mode
		String postText = p.getText();
		postText = Validator.escapeHtmlFormattedTextarea(postText);
		p.setText(postText);

		this.context.put("forum", ForumRepository.getForum(p.getForumId()));
		this.context.put("action", "insertSave");
		this.context.put("post", p);

		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
		User u = um.selectById(p.getUserId());

		Topic topic = DataAccessDriver.getInstance().newTopicDAO().selectById(p.getTopicId());
		int userId = SessionFacade.getUserSession().getUserId();
		this.context.put("attachmentsEnabled", true);
		this.context.put("maxAttachmentsSize", new Long(SakaiSystemGlobals.getIntValue(ConfigKeys.ATTACHMENTS_QUOTA_LIMIT) * 1024 * 1024));
		this.context.put("maxAttachments", SakaiSystemGlobals.getValue(ConfigKeys.ATTACHMENTS_MAX_POST));
		this.context.put("isNewPost", true);
		this.context.put("topic", topic);
		this.context.put("quote", "true");
		this.context.put("quoteUser", u);
		this.setTemplateName(TemplateKeys.POSTS_QUOTE);
		this.context.put("setType", false);
		this.context.put("htmlAllowed", true);
		this.context.put("start", this.request.getParameter("start"));
		this.context.put("user", DataAccessDriver.getInstance().newUserDAO().selectById(userId));*/
	}
	
	/**
	 * save the edited post
	 * @throws Exception
	 */
	public void editSave() throws Exception 
	{
		boolean facilitator = false;
		
		//facilitator can add all type of topics 
		if (JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser())
		{
			facilitator = true;
		}
		
		boolean participant = JForumUserUtil.isJForumParticipant(UserDirectoryService.getCurrentUser().getId());	
		
		if (!(facilitator || participant))
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		
		int postId = this.request.getIntParameter("post_id");
		
		JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		
		org.etudes.api.app.jforum.Post post = jforumPostService.getPost(postId);
	
		if (post == null)
		{
			this.postNotFound();
			return;
		}
		
		org.etudes.api.app.jforum.Topic topic = null;
		
		try
		{
			topic = jforumPostService.getTopic(post.getTopicId(), UserDirectoryService.getCurrentUser().getId());
		}
		catch (JForumAccessException e)
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		
		if (topic == null)
		{
			this.topicNotFound();
			return;
		}
		
		int userId = SessionFacade.getUserSession().getUserId();
		boolean canAccess = (facilitator || post.getUserId() == userId);
		
		if (!canAccess)
		{
			this.setTemplateName(TemplateKeys.POSTS_EDIT_CANNOTEDIT);
			this.context.put("message", I18n.getMessage("CannotEditPost"));
			return;
		}
		
		if (participant && !topic.mayPost())
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;		
		}
		
		if (facilitator)
		{
			org.etudes.api.app.jforum.Topic apiTopic = null;
			apiTopic = post.getTopic();
			
			if (apiTopic.getFirstPostId() == post.getId())
			{
				apiTopic.setType(this.request.getIntParameter("topic_type"));

				// topic - mark for export
				if (this.request.getParameter("topic_export") != null)
				{
					apiTopic.setExportTopic(true);
				}
				else
				{
					apiTopic.setExportTopic(false);
				}
				
				// star and end dates
				/*String startDateParam = this.request.getParameter("start_date");
				if (startDateParam != null && startDateParam.trim().length() > 0)
				{
					Date startDate = null;
					try
					{
						startDate = DateUtil.getDateFromString(startDateParam.trim());
					} catch (ParseException e)
					{
						
					}
					apiTopic.getAccessDates().setOpenDate(startDate);
				}
				else
				{
					apiTopic.getAccessDates().setOpenDate(null);
				}
				
				String endDateParam = this.request.getParameter("end_date");
				if (endDateParam != null && endDateParam.trim().length() > 0)
				{
					Date endDate = null;
					try
					{
						endDate = DateUtil.getDateFromString(endDateParam.trim());
					} catch (ParseException e)
					{
						logger.error(e);				
					}
									
					apiTopic.getAccessDates().setDueDate(endDate);
					
					String lockTopic = this.request.getParameter("lock_topic");
					if (lockTopic != null && "1".equals(lockTopic)){
						apiTopic.getAccessDates().setLocked(true);
					}
					else
					{
						apiTopic.getAccessDates().setLocked(false);
					}
				}
				else
				{
					apiTopic.getAccessDates().setDueDate(null);
					apiTopic.getAccessDates().setLocked(false);
				}*/
				
				String startDateParam = this.request.getParameter("start_date");
				
				// dates
				if (startDateParam != null && startDateParam.trim().length() > 0)
				{
					Date startDate = null;
					try
					{
						startDate = DateUtil.getDateFromString(startDateParam.trim());
					}
					catch (ParseException e)
					{
						if (logger.isErrorEnabled())
						{
							logger.error(e, e);
						}
					}
					apiTopic.getAccessDates().setOpenDate(startDate);
					
					String hideUntilOpen = this.request.getParameter("hide_until_open");
					if (hideUntilOpen != null && "1".equals(hideUntilOpen))
					{
						apiTopic.getAccessDates().setHideUntilOpen(Boolean.TRUE);
					}
					else
					{
						apiTopic.getAccessDates().setHideUntilOpen(Boolean.FALSE);
					}
				}
				else
				{
					apiTopic.getAccessDates().setOpenDate(null);
					apiTopic.getAccessDates().setHideUntilOpen(Boolean.FALSE);
				}

				// due date
				String endDateParam = this.request.getParameter("end_date");
				if (endDateParam != null && endDateParam.trim().length() > 0)
				{
					Date endDate = null;
					try
					{
						endDate = DateUtil.getDateFromString(endDateParam.trim());
					}
					catch (ParseException e)
					{
						if (logger.isErrorEnabled())
						{
							logger.error(e, e);
						}
					}
					apiTopic.getAccessDates().setDueDate(endDate);			
				}
				else
				{
					apiTopic.getAccessDates().setDueDate(null);
				}	
						
				// allow until
				String allowUntilDateParam = this.request.getParameter("allow_until_date");
				if (allowUntilDateParam != null && allowUntilDateParam.trim().length() > 0)
				{
					Date allowUntilDate = null;
					try
					{
						allowUntilDate = DateUtil.getDateFromString(allowUntilDateParam.trim());
					}
					catch (ParseException e)
					{
						if (logger.isErrorEnabled())
						{
							logger.error(e, e);
						}
					}
					apiTopic.getAccessDates().setAllowUntilDate(allowUntilDate);			
				}
				else
				{
					apiTopic.getAccessDates().setAllowUntilDate(null);
				}
				
				if (this.request.getParameter("grade_topic") != null)
				{
					if (this.request.getIntParameter("grade_topic") == Topic.GRADE_NO)
					{
						apiTopic.setGradeTopic(false);
					}
					else if (this.request.getIntParameter("grade_topic") == Topic.GRADE_YES)
					{
						apiTopic.setGradeTopic(true);
					}
					
					if (apiTopic.isGradeTopic())
					{
						if (apiTopic.getGrade() != null)
						{
							
						}
						else
						{
							 jforumPostService.newTopicGrade(apiTopic);							
						}
						
						//org.etudes.api.app.jforum.Grade grade = topic.getGrade();
						try 
						{
							Float points = Float.parseFloat(this.request.getParameter("point_value"));
							
							if (points.floatValue() < 0) points = Float.valueOf(0.0f);
							if (points.floatValue() > 1000) points = Float.valueOf(1000.0f);
							points = Float.valueOf(((float) Math.round(points.floatValue() * 100.0f)) / 100.0f);
							apiTopic.getGrade().setPoints(points);
						} 
						catch (NumberFormatException ne) 
						{
							apiTopic.getGrade().setPoints(0f);
						}
						
						String minPostsRequired = this.request.getParameter("min_posts_required");
						
						if ((minPostsRequired != null) && ("1".equals(minPostsRequired)))
						{
							apiTopic.getGrade().setMinimumPostsRequired(true);
							
							try
							{
								int minimumPosts = this.request.getIntParameter("min_posts");
								apiTopic.getGrade().setMinimumPosts(minimumPosts);
							}
							catch (NumberFormatException e)
							{
								if (logger.isWarnEnabled())
								{
									logger.warn(e.toString(), e);
								}
							}
						}
						else
						{
							apiTopic.getGrade().setMinimumPostsRequired(false);
							apiTopic.getGrade().setMinimumPosts(0);
						}
						
						String sendToGradebook = this.request.getParameter("send_to_grade_book");
						boolean addToGradeBook = false;
						if ((sendToGradebook != null) && (Integer.parseInt(sendToGradebook) == 1))
						{
							addToGradeBook = true;
						}
						apiTopic.getGrade().setAddToGradeBook(addToGradeBook);
					}
														
				}
			}
		}
		
		fillPost(post, true);
		
		processPostAttachments(jforumPostService, post);
		editPostAttachments(jforumPostService, post);
		
		try
		{
			jforumPostService.modifyTopicPost(post);
		}
		catch (JForumAccessException e)
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		catch (JForumAttachmentOverQuotaException e)
		{
			JForum.enableCancelCommit();
			this.context.put("errorMessage", e.getMessage());
			this.context.put("post", post);
			this.edit(post);
			return;				
		}
		catch (JForumAttachmentBadExtensionException e)
		{
			JForum.enableCancelCommit();
			this.context.put("errorMessage", e.getMessage());
			this.context.put("post", post);
			this.edit(post);
			return;
		}
		catch (JForumGradesModificationException e)
		{
			JForum.enableCancelCommit();
			this.context.put("errorMessage", I18n.getMessage("PostShow.CannotEditTopic"));
			this.context.put("post", post);
			this.edit(post);
			return;
		}
		
		String path = this.request.getContextPath() + "/posts/list/";
		String start = this.request.getParameter("start");
		if (start != null && !start.equals("0"))
		{
			path += start + "/";
		}
		
		if (facilitator)
		{
			org.etudes.api.app.jforum.Post modPost = jforumPostService.getPost(postId);
			org.etudes.api.app.jforum.Topic modTopic = null;
			modTopic = modPost.getTopic();
			
			if (modTopic.isGradeTopic() && modTopic.getFirstPostId() == post.getId())
			{
				String sendToGradebook = this.request.getParameter("send_to_grade_book");
				boolean addToGradeBook = false;
				if ((sendToGradebook != null) && (Integer.parseInt(sendToGradebook) == 1))
				{
					addToGradeBook = true;
				}
				
				if (addToGradeBook)
				{
					if (modTopic.isGradeTopic() && !modTopic.getGrade().isAddToGradeBook())
					{
						if (modTopic.getGrade().getPoints() <= 0)
						{
							this.context.put("errorMessage", I18n.getMessage("Grade.AddEditTopicGradeBookAssignmentHasIllegalPointsException"));
						}
						else
						{
							this.context.put("errorMessage", I18n.getMessage("Grade.AddEditTopicGradeBookConflictingAssignmentNameException"));
						}
						
						this.request.addParameter("post_id", String.valueOf(postId));
						this.edit();
						return;
					}
					
				}
			}
		}

		path += post.getTopicId() + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) + "#" + post.getId();
		JForum.setRedirect(path);		
	}

	/**
	 * waiting moderation
	 */
	public void waitingModeration()
	{
		this.setTemplateName(TemplateKeys.POSTS_WAITING);

		int topicId = this.request.getIntParameter("topic_id");
		String path = this.request.getContextPath();

		if (topicId == 0) {
			path += "/forums/show/" + this.request.getParameter("forum_id");
		}
		else {
			path += "/posts/list/" + topicId;
		}

		this.context.put("message", I18n.getMessage("PostShow.waitingModeration",
				new String[] { path + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) }));
	}

	/**
	 * not moderated yet
	 */
	private void notModeratedYet()
	{
		this.setTemplateName(TemplateKeys.POSTS_NOT_MODERATED);
		this.context.put("message", I18n.getMessage("PostShow.notModeratedYet"));
	}

	/**
	 * save the post
	 * 
	 * @throws Exception
	 */
	public void insertSave() throws Exception
	{
		if(logger.isDebugEnabled()) logger.debug("starting insertSave()");
		
		int forumId = this.request.getIntParameter("forum_id");
		
		try
		{
		
			boolean facilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId());
			
			boolean participant = JForumUserUtil.isJForumParticipant(UserDirectoryService.getCurrentUser().getId());	
			
			if (!(facilitator || participant))
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}	
			
			JForumForumService jforumForumService = (JForumForumService)ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
			
			org.etudes.api.app.jforum.Forum forum = null;
			try
			{
				forum = jforumForumService.getForum(forumId, UserDirectoryService.getCurrentUser().getId());
			}
			catch (JForumAccessException e)
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}
			
			JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
			
			if (this.request.getParameter("topic_id") != null) 
			{
				int topicId = Integer.parseInt(this.request.getParameter("topic_id"));
				// posting reply
				org.etudes.api.app.jforum.Topic topic = jforumPostService.getTopic(topicId);
				
				JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
	
				org.etudes.api.app.jforum.User postedBy = jforumUserService.getBySakaiUserId(SessionFacade.getUserSession().getSakaiUserId());
				topic.setPostedBy(postedBy);
				
				org.etudes.api.app.jforum.Post post = jforumPostService.newPost();			
				
				
				fillPost(post, false);
				
				if (post.getSubject() == null || post.getSubject().trim() == "") 
				{
					post.setSubject(topic.getTitle());
				}
				
				processPostAttachments(jforumPostService, post);
				
				topic.getPosts().clear();
				topic.getPosts().add(post);
				
				try
				{
					jforumPostService.createPost(topic);
				}
				catch (JForumAccessException e)
				{
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
				catch (JForumAttachmentOverQuotaException e)
				{
					JForum.enableCancelCommit();
					this.request.addParameter("topic_id", this.request.getParameter("topic_id"));
					this.context.put("errorMessage", e.getMessage());
					this.context.put("post", post);
					this.insert();
					return;
				}
				catch (JForumAttachmentBadExtensionException e)
				{
					JForum.enableCancelCommit();
					this.request.addParameter("topic_id", this.request.getParameter("topic_id"));
					this.context.put("errorMessage", e.getMessage());
					this.context.put("post", post);
					this.insert();
					return;
				}
				
				// Topic watch
				if (this.request.getParameter("notify") != null)
				{
					jforumPostService.subscribeUserToTopic(topic.getId(), post.getPostedBy().getSakaiUserId());
				}
							
				String path = this.request.getContextPath() + "/posts/list/";
				int start = ViewCommon.getStartPage();
	
				path += this.startPage(topic, start) + "/";
				path += topic.getId() + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) + "#" + post.getId();
				
				JForum.setRedirect(path);
				//this.response.setIntHeader("X-XSS-Protection", 0);
			}
			else
			{
				// creating new topic
				//topic type, forum id, title(subject), dates, postedBy if topic is gradable grade information.
				org.etudes.api.app.jforum.Topic topic = jforumPostService.newTopic();
				
				if (this.request.getParameter("topic_type") != null) 
				{
					topic.setType(this.request.getIntParameter("topic_type"));
				}
	
				topic.setForumId(forumId);
				
				topic.setTitle(this.request.getParameter("subject"));
	
				// topic - mark for export
				if (this.request.getParameter("topic_export") != null)
				{
					topic.setExportTopic(true);
				}
				else
				{
					topic.setExportTopic(false);
				}
				
				String startDateParam = this.request.getParameter("start_date");
				
				// dates
				if (startDateParam != null && startDateParam.trim().length() > 0)
				{
					Date startDate = null;
					try
					{
						startDate = DateUtil.getDateFromString(startDateParam.trim());
					}
					catch (ParseException e)
					{
						if (logger.isErrorEnabled())
						{
							logger.error(e, e);
						}
					}
					topic.getAccessDates().setOpenDate(startDate);
					
					String hideUntilOpen = this.request.getParameter("hide_until_open");
					if (hideUntilOpen != null && "1".equals(hideUntilOpen))
					{
						topic.getAccessDates().setHideUntilOpen(Boolean.TRUE);
					}
					else
					{
						topic.getAccessDates().setHideUntilOpen(Boolean.FALSE);
					}
				}
				else
				{
					topic.getAccessDates().setOpenDate(null);
					topic.getAccessDates().setHideUntilOpen(Boolean.FALSE);
				}
	
				// due date
				String endDateParam = this.request.getParameter("end_date");
				if (endDateParam != null && endDateParam.trim().length() > 0)
				{
					Date endDate = null;
					try
					{
						endDate = DateUtil.getDateFromString(endDateParam.trim());
					}
					catch (ParseException e)
					{
						if (logger.isErrorEnabled())
						{
							logger.error(e, e);
						}
					}
					topic.getAccessDates().setDueDate(endDate);			
				}
				else
				{
					topic.getAccessDates().setDueDate(null);
				}	
						
				// allow until
				String allowUntilDateParam = this.request.getParameter("allow_until_date");
				if (allowUntilDateParam != null && allowUntilDateParam.trim().length() > 0)
				{
					Date allowUntilDate = null;
					try
					{
						allowUntilDate = DateUtil.getDateFromString(allowUntilDateParam.trim());
					}
					catch (ParseException e)
					{
						if (logger.isErrorEnabled())
						{
							logger.error(e, e);
						}
					}
					topic.getAccessDates().setAllowUntilDate(allowUntilDate);			
				}
				else
				{
					topic.getAccessDates().setAllowUntilDate(null);
				}
							
				//grade topic
				if (facilitator && this.request.getParameter("grade_topic") != null)
				{
					org.etudes.api.app.jforum.Grade grade = topic.getGrade();
					if (this.request.getIntParameter("grade_topic") == Topic.GRADE_NO)
					{
						topic.setGradeTopic(false);
					}
					else if (this.request.getIntParameter("grade_topic") == Topic.GRADE_YES)
					{
						topic.setGradeTopic(true);
					}
					
					try 
					{
						Float points = Float.parseFloat(this.request.getParameter("point_value"));
						
						if (points.floatValue() < 0) points = Float.valueOf(0.0f);
						if (points.floatValue() > 1000) points = Float.valueOf(1000.0f);
						points = Float.valueOf(((float) Math.round(points.floatValue() * 100.0f)) / 100.0f);
						grade.setPoints(points);
					} 
					catch (NumberFormatException ne) 
					{
						grade.setPoints(0f);
						if (logger.isWarnEnabled())
						{
							logger.warn(ne.toString(), ne);
						}
					}
					
					String minPostsRequired = this.request.getParameter("min_posts_required");
					
					if ((minPostsRequired != null) && ("1".equals(minPostsRequired)))
					{
						grade.setMinimumPostsRequired(true);
						try
						{
							int minimumPosts = this.request.getIntParameter("min_posts");
							grade.setMinimumPosts(minimumPosts);
						}
						catch (NumberFormatException e)
						{
							if (logger.isWarnEnabled())
							{
								logger.warn(e.toString(), e);
							}
						}
					}
					
					String sendToGradebook = this.request.getParameter("send_to_grade_book");
					boolean addToGradeBook = false;
					if ((sendToGradebook != null) && (Integer.parseInt(sendToGradebook) == 1))
					{
						addToGradeBook = true;
					}
					grade.setAddToGradeBook(addToGradeBook);
				}
				
				JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
	
				org.etudes.api.app.jforum.User postedBy = jforumUserService.getBySakaiUserId(SessionFacade.getUserSession().getSakaiUserId());
				topic.setPostedBy(postedBy);
				
				org.etudes.api.app.jforum.Post post = jforumPostService.newPost();			
				
				
				fillPost(post, false);
				
				processPostAttachments(jforumPostService, post);
				
				topic.getPosts().add(post);
				
				try
				{
					jforumPostService.createTopicWithAttachments(topic);
				}
				catch (JForumAccessException e)
				{
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
				catch (JForumAttachmentOverQuotaException e)
				{
					JForum.enableCancelCommit();
					this.context.put("errorMessage", e.getMessage());
					this.context.put("post", post);
					this.insert();
					return;				
				}
				catch (JForumAttachmentBadExtensionException e)
				{
					JForum.enableCancelCommit();
					this.context.put("errorMessage", e.getMessage());
					this.context.put("post", post);
					this.insert();
					return;
				}
				
				// Topic watch
				if (this.request.getParameter("notify") != null)
				{
					jforumPostService.subscribeUserToTopic(topic.getId(), post.getPostedBy().getSakaiUserId());
				}
				
				if (facilitator)
				{
					org.etudes.api.app.jforum.Post newPost = jforumPostService.getPost(post.getId());
					org.etudes.api.app.jforum.Topic newTopic = null;
					newTopic = newPost.getTopic();
					
					if (newTopic.isGradeTopic() && newTopic.getFirstPostId() == post.getId())
					{
						String sendToGradebook = this.request.getParameter("send_to_grade_book");
						boolean addToGradeBook = false;
						if ((sendToGradebook != null) && (Integer.parseInt(sendToGradebook) == 1))
						{
							addToGradeBook = true;
						}
						
						if (addToGradeBook)
						{
							if (newTopic.isGradeTopic() && !newTopic.getGrade().isAddToGradeBook())
							{
								if (newTopic.getGrade().getPoints() <= 0)
								{
									this.context.put("errorMessage", I18n.getMessage("Grade.AddEditTopicGradeBookAssignmentHasIllegalPointsException"));
								}
								else
								{
									this.context.put("errorMessage", I18n.getMessage("Grade.AddEditTopicGradeBookConflictingAssignmentNameException"));
								}
								
								this.request.addParameter("post_id", String.valueOf(post.getId()));
								this.request.addParameter("start", "0");
								this.edit();
								return;
							}
							
						}
					}
				}
				
				String path = this.request.getContextPath() + "/posts/list/";
				path += topic.getId() + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION);
	
				JForum.setRedirect(path);
			}
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e.toString(), e);
			}
			
			throw e;

			/*String path = this.request.getContextPath() +"/forums/show/"+ forumId + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION);
			
			JForum.setRedirect(path);*/
		}
	}

	/**
	 * start page
	 * 
	 * @param t
	 * @param currentStart
	 * @return
	 */
	/*private int startPage(Topic t, int currentStart) {
		int postsPerPage = SakaiSystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE);

		int newStart = (t.getTotalReplies() + 1) / postsPerPage * postsPerPage;
		if (newStart > currentStart) {
			return newStart;
		}

		return currentStart;
	}*/	
	
	/**
	 * Fill the topic post with request data
	 * 
	 * @param p	Post
	 * 
	 * @param isEdit	is post edit
	 */
	protected void fillPost(org.etudes.api.app.jforum.Post p, boolean isEdit)
	{
		p.setSubject(JForum.getRequest().getParameter("subject"));
		p.setBbCodeEnabled(JForum.getRequest().getParameter("disable_bbcode") != null ? false : true);
		p.setSmiliesEnabled(JForum.getRequest().getParameter("disable_smilies") != null ? false : true);
		p.setSignatureEnabled(JForum.getRequest().getParameter("attach_sig") != null ? true : false);

		if (!isEdit)
		{
			p.setUserIp(JForum.getRequest().getRemoteAddr());
			//p.setUserId(SessionFacade.getUserSession().getUserId());
		}
		
		JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");

		org.etudes.api.app.jforum.User postedBy = jforumUserService.getBySakaiUserId(SessionFacade.getUserSession().getSakaiUserId());
		p.setPostedBy(postedBy);
		
		//boolean htmlEnabled = true;
		
		//p.setHtmlEnabled(htmlEnabled && JForum.getRequest().getParameter("disable_html") != null);

		String message = "";
		if(JForum.getRequest().getParameter("message") != null) 
		{
			message = JForum.getRequest().getParameter("message");
			//message = SafeHtml.escapeJavascript(JForum.getRequest().getParameter("message"));
			
			// strip html comments - moved to service uses HtmlHelper.clean
			//message = SafeHtml.stripHTMLComments(message);
			
			// strip excess spaces - moved to service uses HtmlHelper.clean
			//message = SafeHtml.removeExcessSpaces(message);
			
			// add target to anchor tag - moved to service uses HtmlHelper.clean
			//message = SafeHtml.addAnchorTarget(message);
			
			// clean the post text
			//message = HtmlHelper.clean(message);
			
		} 
		else if (JForum.getRequest().getParameter("quickmessage") !=null)
		{
			message = JForum.getRequest().getParameter("quickmessage");
			
			// strip html comments - moved to service uses HtmlHelper.clean
			// message = SafeHtml.stripHTMLComments(message);
			
			// strip excess spaces
			//message = SafeHtml.removeExcessSpaces(message);
			
			// add target to anchor tag - moved to service uses HtmlHelper.clean
			//message = SafeHtml.addAnchorTarget(message);
			
			//scrub the raw user content of invalid HTML...
			//message = SafeHtml.makeSafe( JForum.getRequest().getParameter("quickmessage"));
							
			//...then augment the plain-text to safe HTML
			message = message.replaceAll("\n<p>", "<p>");
			message = message.replaceAll("\n", "<br>");
									
			//p.setHtmlEnabled(false);
		}
		
		/*if (p.isHtmlEnabled())
		{
			p.setText(SafeHtml.makeSafe(message));
		}
		else
		{
			p.setText(message);
		}*/
		p.setText(message);
	}
	
	/**
	 * Process post attachments
	 * 
	 * @param jforumPostService	jforum post service
	 * 
	 * @param post	Post with attachments
	 */
	protected void processPostAttachments(JForumPostService jforumPostService, org.etudes.api.app.jforum.Post post)
	{
		String t = this.request.getParameter("total_files");

		if (t == null || "".equals(t))
		{
			return;
		}

		int total = Integer.parseInt(t);

		if (total < 1)
		{
			return;
		}
		post.setHasAttachments(true);
		

		for (int i = 0; i < total; i++)
		{
			DiskFileItem item = (DiskFileItem) this.request.getObjectParameter("file_" + i);
			if (item == null)
			{
				continue;
			}

			if (item.getName().indexOf('\000') > -1)
			{
				logger.warn("Possible bad attachment (null char): " + item.getName() + " - user_id: " + SessionFacade.getUserSession().getUserId());
				continue;
			}
			
			String fileName = null;
			String contentType = null;
			String comments = null;
			byte[] fileContent = null;
			
			fileName = item.getName();
			contentType = item.getContentType();
			comments = this.request.getParameter("comment_" + i);
			fileContent = item.get();
			
			org.etudes.api.app.jforum.Attachment  attachment = jforumPostService.newAttachment(fileName, contentType, comments, fileContent);
			if (attachment != null)
			{
				post.getAttachments().add(attachment);
			}
		}

	}
	
	protected void editPostAttachments(JForumPostService jforumPostService, org.etudes.api.app.jforum.Post post)
	{
		String[] delete = null;
		String s = this.request.getParameter("delete_attach");

		if (s != null)
		{
			delete = s.split(",");
		}
		
		List<org.etudes.api.app.jforum.Attachment> attachments = post.getAttachments();

		if (delete != null)
		{
			for (int i = 0; i < delete.length; i++)
			{
				if (delete[i] != null && !delete[i].equals(""))
				{
					int id = Integer.parseInt(delete[i]);
					
					for (Iterator<org.etudes.api.app.jforum.Attachment> iter = attachments.iterator(); iter.hasNext();)
					{
						org.etudes.api.app.jforum.Attachment attachment = iter.next();
						
						if (attachment.getId() == id)
						{
							iter.remove();
							break;
						}
					}
				}
			}
		}
	}
	
	private int startPage(org.etudes.api.app.jforum.Topic t, int currentStart) 
	{
		int postsPerPage = SakaiSystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE);

		int newStart = (t.getTotalReplies() + 1) / postsPerPage * postsPerPage;
		if (newStart > currentStart) {
			return newStart;
		}

		return currentStart;
	}
	
	public void delete() throws Exception 
	{
		if (!(JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser()))
		{
			this.setTemplateName(TemplateKeys.POSTS_CANNOT_DELETE);
			this.context.put("message", I18n.getMessage("CannotRemovePost"));

			return;
		}
		
		int postId = this.request.getIntParameter("post_id");
		
		JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		
		org.etudes.api.app.jforum.Post post = jforumPostService.getPost(postId);
	
		if (post == null)
		{
			this.postNotFound();
			return;
		}
		
		org.etudes.api.app.jforum.Topic topic = null;
		
		try
		{
			topic = jforumPostService.getTopic(post.getTopicId(), UserDirectoryService.getCurrentUser().getId());
		}
		catch (JForumAccessException e)
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		
		try
		{
			jforumPostService.removePost(postId, UserDirectoryService.getCurrentUser().getId());
		}
		catch (JForumItemNotFoundException e)
		{
			this.postNotFound();
			return;
		}
		catch (JForumAccessException e)
		{
			// already verified the access
		}
		catch (JForumGradesModificationException e)
		{
			this.context.put("errorMessage", I18n.getMessage("PostShow.CannotDeleteGradedForumTopic", new Object[]{topic.getTitle()}));
			this.request.addParameter("topic_id", String.valueOf(topic.getId()));
			this.list();
			return;
		}
		
		// It was the last remaining post in the topic?
		int totalPosts = jforumPostService.getTotalPosts(post.getTopicId());
		if (totalPosts > 0)
		{
			String page = this.request.getParameter("start");
			String returnPath = this.request.getContextPath() + "/posts/list/";

			if (page != null && !page.equals("") && !page.equals("0"))
			{

				int postsPerPage = SakaiSystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE);
				int newPage = Integer.parseInt(page);

				if (totalPosts % postsPerPage == 0)
				{
					newPage -= postsPerPage;
				}

				returnPath += newPage + "/";
			}

			JForum.setRedirect(returnPath + post.getTopicId() + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		}
		else
		{
			JForum.setRedirect(this.request.getContextPath() + "/forums/show/" + post.getForumId() + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		}		
	}

	/**
	 * delete the post
	 * 
	 * @throws Exception
	 *//*
	public void delete() throws Exception {

		if (!(JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) 
				|| SecurityService.isSuperUser())){
			this.setTemplateName(TemplateKeys.POSTS_CANNOT_DELETE);
			this.context.put("message", I18n.getMessage("CannotRemovePost"));

			return;
		}

		// Post
		PostDAO pm = DataAccessDriver.getInstance().newPostDAO();
		Post p = pm.selectById(this.request.getIntParameter("post_id"));

		TopicDAO tm = DataAccessDriver.getInstance().newTopicDAO();
		Topic t = tm.selectById(p.getTopicId());

		if (!TopicsCommon.isTopicAccessible(t.getForumId())) {
			return;
		}

		if (p.getId() == 0) {
			this.postNotFound();
			return;
		}
		
		
		 * If the post is first post check the grade type of the topic. If the topic's grade type
		 * is yes check for existing evaluations. If the topic's grade has evaluations, the topic cannot
		 * be deleted
		 * If the forum has grades the top level topics cannot be deleted
		 
		ForumDAO fm = DataAccessDriver.getInstance().newForumDAO();
		Forum forum = fm.selectById(t.getForumId());
		if ((t.getFirstPostId() == p.getId()) && (forum.getGradeType() == Forum.GRADE_BY_TOPIC) && (t.isGradeTopic())) {
			EvaluationDAO evaldao = DataAccessDriver.getInstance().newEvaluationDAO();
			int evalCount = evaldao.selectForumTopicEvaluationsCountById(t.getId());
			
			if (evalCount > 0) {
				this.context.put("errorMessage", I18n.getMessage("PostShow.CannotDeleteTopic", new Object[]{t.getTitle()}));
				this.request.addParameter("topic_id", String.valueOf(t.getId()));
				this.list();
				return;
			}
			
			//remove entry from gradebook
			GradeDAO gradedao = DataAccessDriver.getInstance().newGradeDAO();
			Grade grade = gradedao.selectByForumTopicId(t.getForumId(), t.getId());
			if (grade != null && grade.isAddToGradeBook()) {
				
				removeEntryFromGradeBook(grade);
			}
			
			
		} else if ((t.getFirstPostId() == p.getId()) && (forum.getGradeType() == Forum.GRADE_BY_FORUM)) {
			EvaluationDAO evaldao = DataAccessDriver.getInstance().newEvaluationDAO();
			int evalCount = evaldao.selectForumEvaluationsCount(forum.getId());
			
			if (evalCount > 0) {
				this.context.put("errorMessage", I18n.getMessage("PostShow.CannotDeleteForumTopic", new Object[]{t.getTitle()}));
				this.request.addParameter("topic_id", String.valueOf(t.getId()));
				this.list();
				return;
			}
		}
		
		if (t.getFirstPostId() == p.getId())
		{
			deleteSpecialAccess(t);	
		}
		
		pm.delete(p);
		DataAccessDriver.getInstance().newUserDAO().decrementPosts(p.getUserId());

		// Attachments
		new AttachmentCommon(this.request, p.getForumId()).deleteAttachments(p.getId(), p.getForumId());

		// Topic
		tm.decrementTotalReplies(p.getTopicId());

		int maxPostId = tm.getMaxPostId(p.getTopicId());
		if (maxPostId > -1) {
			tm.setLastPostId(p.getTopicId(), maxPostId);
		}

		int minPostId = tm.getMinPostId(p.getTopicId());
		if (minPostId > -1) {
		  tm.setFirstPostId(p.getTopicId(), minPostId);
		}

		// Forum

		maxPostId = fm.getMaxPostId(p.getForumId());
		if (maxPostId > -1) {
			fm.setLastPost(p.getForumId(), maxPostId);
		}

		// It was the last remaining post in the topic?
		int totalPosts = tm.getTotalPosts(p.getTopicId());
		if (totalPosts > 0) {
			String page = this.request.getParameter("start");
			String returnPath = this.request.getContextPath() + "/posts/list/";

			if (page != null && !page.equals("") && !page.equals("0")) {

				int postsPerPage = SakaiSystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE);
				int newPage = Integer.parseInt(page);

				if (totalPosts % postsPerPage == 0) {
					newPage -= postsPerPage;
				}

				returnPath += newPage + "/";
			}

			JForum.setRedirect(returnPath + p.getTopicId() + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		}
		else {
			// Ok, all posts were removed. Time to say goodbye
			TopicsCommon.deleteTopic(p.getTopicId(), p.getForumId(), false);

			JForum.setRedirect(this.request.getContextPath() + "/forums/show/" + p.getForumId()
					+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		}

		ForumRepository.reloadForum(p.getForumId());
		TopicRepository.clearCache(p.getForumId());
		PostRepository.clearCache(p.getTopicId());
	}*/

	/**
	 * update topic read flags
	 * @throws Exception
	 */
	public void updateTopicReadFlags() throws Exception
	{
		//TopicMarkTimeDAO tm = DataAccessDriver.getInstance().newTopicMarkTimeDAO();
		int topicId = this.request.getIntParameter("topic_id");
		
		JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		
		org.etudes.api.app.jforum.Topic topic = jforumPostService.getTopic(topicId);
		
		if (topic == null)
		{
			return;
		}
		
		jforumPostService.markTopicRead(topicId, UserDirectoryService.getCurrentUser().getId(), null, false);
		
		//UserSession us = SessionFacade.getUserSession();
		
		/*Topic topic = DataAccessDriver.getInstance().newTopicDAO().selectById(topicId);

		Date markTime = null;
		try
		{
		  markTime = tm.selectMarkTime(topicId, us.getUserId());

		}
		catch (Exception e)
		{
			logger.error(this.getClass().getName() +
					".updateTopicReadFlags() : " + e.getMessage(), e);
		}
		if (markTime == null)
		{
		  tm.addMarkTime(topicId, us.getUserId(), new Date(System.currentTimeMillis()), false);
		}
		else
		{
		  tm.updateMarkTime(topicId, us.getUserId(), new Date(System.currentTimeMillis()), false);
		}*/

		JForum.setRedirect(this.request.getContextPath() + "/forums/show/"+ topic.getForumId() + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
	}

	/**
	 * watch the topic
	 * @param tm
	 * @param topicId
	 * @param userId
	 * @throws Exception
	 */
	private void watch(TopicDAO tm, int topicId, int userId) throws Exception {
		if (!tm.isUserSubscribed(topicId, userId)) {
			tm.subscribeUser(topicId, userId);
		}
	}

	/**
	 * wat the topic
	 * @throws Exception
	 */
	public void watch() throws Exception 
	{
		int topicId = this.request.getIntParameter("topic_id");
		//int userId = SessionFacade.getUserSession().getUserId();

		// this.watch(DataAccessDriver.getInstance().newTopicDAO(), topicId, userId);
		JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		jforumPostService.subscribeUserToTopic(topicId, UserDirectoryService.getCurrentUser().getId());
		
		this.list();
	}

	/**
	 * unwatch the topic
	 * 
	 * @throws Exception
	 */
	public void unwatch() throws Exception 
	{
		if (SessionFacade.isLogged())
		{
			int topicId = this.request.getIntParameter("topic_id");
			int userId = SessionFacade.getUserSession().getUserId();
			String start = this.request.getParameter("start");

			//DataAccessDriver.getInstance().newTopicDAO().removeSubscription(topicId, userId);
			JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
			jforumPostService.removeUserTopicSubscription(topicId, UserDirectoryService.getCurrentUser().getId());
			

			String returnPath = this.request.getContextPath() + "/posts/list/";
			if (start != null && !start.equals(""))
			{
				returnPath += start + "/";
			}

			returnPath += topicId + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION);

			this.setTemplateName(TemplateKeys.POSTS_UNWATCH);
			this.context.put("message", I18n.getMessage("ForumBase.unwatched", new String[] { returnPath }));
		}
		else
		{
			this.setTemplateName(ViewCommon.contextToLogin());
		}
	}

	/**
	 * download attachment
	 * 
	 * @throws Exception
	 */
	public void downloadAttach() throws Exception
	{

		int id = this.request.getIntParameter("attach_id");

		JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		org.etudes.api.app.jforum.Attachment attachment = jforumPostService.getPostAttachment(id);

		String filename = SakaiSystemGlobals.getValue(ConfigKeys.ATTACHMENTS_STORE_DIR)
			+ "/"
			+ attachment.getInfo().getPhysicalFilename();

		if (!new File(filename).exists()) {
			this.setTemplateName(TemplateKeys.POSTS_ATTACH_NOTFOUND);
			this.context.put("message", I18n.getMessage("Attachments.notFound"));
			return;
		}

		FileInputStream fis = new FileInputStream(filename);
		OutputStream os = response.getOutputStream();

		if(attachment.getInfo().getExtension() != null)
		{
			if(attachment.getInfo().getExtension().isPhysicalDownloadMode())
			{
				this.response.setContentType("application/octet-stream");
			}
			else
			{
				this.response.setContentType(attachment.getInfo().getMimetype());
			}
		}
		else
		{
			this.response.setContentType("application/octet-stream");
		}

		this.response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getInfo().getRealFilename() + "\";");
		this.response.setContentLength((int)attachment.getInfo().getFilesize());

		byte[] b = new byte[4096];
		int c = 0;
		while ((c = fis.read(b)) != -1) {
			os.write(b, 0, c);
		}
		os.flush();

		fis.close();
		os.close();

		JForum.enableBinaryContent(true);
	}

	/**
	 * 
	 */
	private void topicLocked() {
		this.setTemplateName(TemplateKeys.POSTS_TOPIC_LOCKED);
		this.context.put("message", I18n.getMessage("PostShow.topicLocked"));
	}

	/**
	 * 
	 */
	public void listSmilies()
	{
		this.setTemplateName(SystemGlobals.getValue(ConfigKeys.TEMPLATE_DIR) + "/empty.htm");
		this.setTemplateName(TemplateKeys.POSTS_LIST_SMILIES);
		this.context.put("smilies", SmiliesRepository.getSmilies());
	}


	/**
	 * 
	 * @param forumId
	 * @return
	 * @throws Exception
	 */
	private boolean anonymousPost(int forumId) throws Exception {
		// Check if anonymous posts are allowed
		
		if (!SessionFacade.isLogged()) {
			this.setTemplateName(ViewCommon.contextToLogin());

			return false;
		}

		return true;
	}
	
	/**
	 * check to see if forum is read only
	 * 
	 * @param forum		forum
	 * @return
	 * @throws Exception
	 */
	private boolean isForumTypeReadonly(Forum forum) throws Exception {
		if (JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser())
			return false;
		
		if (forum.getType() == Forum.TYPE_READ_ONLY){
			return true;
		}

		return false;
	}
	
	/**
	 * Check to see of forum type is reply only
	 * 
	 * @param forum
	 * @return
	 * @throws Exception
	 */
	private boolean isForumTypeReplyOnly(Forum forum) throws Exception {
		
		if (JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser())
			return false;
				
		if (forum.getType() == Forum.TYPE_REPLY_ONLY) {
			return true;
		}

		return false;
	}
	
	/**
	 * check to see if forum is grade by topid
	 * @param forum
	 * @return
	 * @throws Exception
	 */
	private boolean isForumGradeByTopic(Forum forum) throws Exception
	{
		if (JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) 
				|| SecurityService.isSuperUser()) {

			if (forum.getGradeType() == Forum.GRADE_BY_TOPIC) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Checks to see if the forum is locked after due date
	 * 
	 * @param forumId	forum
	 * @return	true 	if forum is not locked after the due date or the user is admin or facilitator
	 * 			false 	if forum is locked after the due date and the user is not admin or facilitator
	 * @throws Exception
	 */
	private boolean isForumLocked(Forum forum) throws Exception 
	{
		if (JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser())
			return false;
		
		if ((forum.getEndDate() == null) || (!forum.isLockForum())) 
			return false;
		
		GregorianCalendar gc = new GregorianCalendar();
		Date nowDate = gc.getTime();
		
		if (nowDate.getTime() > forum.getEndDate().getTime())
		{
				return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Check to see if forum is open
	 * 
	 * @param forum		forum
	 * @return	true 	if forum start date is before the current date/time or the user is admin or facilitator
	 * 			false 	if forum start date is after the current date/time or and the user is not admin or facilitator
	 * @throws Exception
	 */
	private boolean isForumOpen(Forum forum) throws Exception 
	{
		if (JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser())
			return true;
		
		if (forum.getStartDate() == null) 
			return true;
		
		GregorianCalendar gc = new GregorianCalendar();
		Date nowDate = gc.getTime();
		
		if (nowDate.getTime() > forum.getStartDate().getTime())
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks to see if the category is locked after due date
	 * 
	 * @param category		category
	 * @return	true 	if category is not locked after the due date or the user is admin or facilitator
	 * 			false 	if category is locked after the due date and the user is not admin or facilitator
	 * @throws Exception
	 */
	private boolean isCategoryLocked(Category category) throws Exception 
	{
		if (JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser())
			return false;
				
		if ((category.getEndDate() == null) || (!category.isLockCategory())) 
			return false;
		
		GregorianCalendar gc = new GregorianCalendar();
		Date nowDate = gc.getTime();
		
		if (nowDate.getTime() > category.getEndDate().getTime())
		{
				return true;
		}
		
		return false;
	}
	
	/**
	 * Check to see if category is open
	 * 
	 * @param category		category
	 * @return	true 	if category start date is before the current date/time or the user is admin or facilitator
	 * 			false 	if start date is after the current date/time or and the user is not admin or facilitator
	 * @throws Exception
	 */
	private boolean isCategoryOpen(Category category) throws Exception 
	{
		if (JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser())
			return true;
		
		if (category.getStartDate() == null) 
			return true;
		
		GregorianCalendar gc = new GregorianCalendar();
		Date nowDate = gc.getTime();
						
		if (nowDate.getTime() > category.getStartDate().getTime())
		{
				return true;
		}
		
		return false;
	}
	
	private boolean isCategoryAccessibleToSpecialAccessUser (SpecialAccess specialAccess) throws Exception 
	{
		if (JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser())
			return true;
		
		GregorianCalendar gc = new GregorianCalendar();
		Date nowDate = gc.getTime();
		
		/* 
		 	check for category special access. If category special access is available
		   	verify the special access start date with current time. If special access is not
		   	available verify forum start dates with current time.
	    */
		boolean specialAccessUser = false, specialAccessUserAccess = false;
				
		if (specialAccess.getStartDate() != null)
		{
			if (specialAccess.getStartDate().getTime() > nowDate.getTime())
			{
				specialAccessUserAccess = false;
			}
			if (specialAccess.getEndDate() != null)
			{
				if ((specialAccess.getEndDate().getTime() > nowDate.getTime()))
				{
					specialAccessUserAccess = false;
				}
			}
			specialAccessUserAccess = true;
		}
		else
		{
			specialAccessUserAccess = true;
		}
		
		
		this.context.put("specialAccessUserAccess", specialAccessUserAccess);
				
		return specialAccessUserAccess;
	}
	
	private SpecialAccess getUserSpecialAccess (Forum forum) throws Exception 
	{
		// no special access for forums with out dates
		if (forum.getStartDate() == null && forum.getEndDate() == null)
		{
			return null;
		}
		
		// check forum special access
		List<SpecialAccess> forumSpecialAccessList = forum.getSpecialAccessList();
		
		List<SpecialAccess> specialAccessList = null;
		
		if ((forumSpecialAccessList != null) && (forumSpecialAccessList.size() > 0))
		{
			specialAccessList = forumSpecialAccessList;
		}
				
		SpecialAccess userSpecialAccess = null;
		boolean specialAccessUser = false;
		
		if (specialAccessList != null)
		{
			for (SpecialAccess specialAccess : specialAccessList)
			{
				UserSession currentUser = SessionFacade.getUserSession();
				if (specialAccess.getUserIds().contains(currentUser.getUserId()))
				{
					if (!ForumRepository.isForumAccessibleToUser(forum))
					{
						break;
					}
					
					specialAccessUser = true;
					userSpecialAccess = specialAccess;
					
					break;
				}
			}
		}	
			
		this.context.put("specialAccessUser", specialAccessUser);
		this.context.put("specialAccess", userSpecialAccess);
		
		return userSpecialAccess;
	}
	
	/**
	 * check user topic special access
	 * 
	 * @param topic		Topic
	 * @return			user special access 
	 */
	protected SpecialAccess getUserTopicSpecialAccess(Topic topic) throws Exception
	{
		SpecialAccess userSpecialAccess = null;
		
		// no special access for topics with out dates
		if (topic.getStartDate() == null && topic.getEndDate() == null)
		{
			return userSpecialAccess;
		}

		List<SpecialAccess> topicSpecialAccessList = DataAccessDriver.getInstance().newSpecialAccessDAO().selectByTopic(topic.getForumId(), topic.getId());
		
		if ((topicSpecialAccessList == null) || (topicSpecialAccessList.size() == 0))
		{
			return userSpecialAccess;
		}
		
		boolean specialAccessUser = false;
		UserSession currentUser = SessionFacade.getUserSession();
		
		for (SpecialAccess specialAccess : topicSpecialAccessList)
		{			
			if (specialAccess.getUserIds().contains(currentUser.getUserId()))
			{
				specialAccessUser = true;
				userSpecialAccess = specialAccess;
				
				break;
			}
		}
		
		this.context.put("specialAccessUser", specialAccessUser);
		this.context.put("specialAccess", userSpecialAccess);
		
		return userSpecialAccess;
	}
	
	/**
	 * add forum grade type to context
	 */
	protected void addGradeTypesToContext()
	{
		this.context.put("gradeDisabled", Forum.GRADE_DISABLED);
		this.context.put("gradeForum", Forum.GRADE_BY_FORUM);
		this.context.put("gradeTopic", Forum.GRADE_BY_TOPIC);
		this.context.put("gradeCategory", Forum.GRADE_BY_CATEGORY);		
	}
	
		
	/**
	 * update grade book
	 * @param grade		grade
	 * @param gradebookUid	gradebookUid
	 * @param endDate 	end date
	 * @return true - if updated in gradebook
	 * 		   false - if not added or updated in gradebook 
	 * @throws Exception
	 */
	protected boolean updateGradebook(Topic topic, Grade grade, Date endDate) throws Exception 
	{
		String gradebookUid = ToolManager.getInstance().getCurrentPlacement().getContext();
		
		JForumGBService jForumGBService = null;
		jForumGBService = (JForumGBService)ComponentManager.get("org.etudes.api.app.jforum.JForumGBService");
		
		if (jForumGBService == null)
			return false;
				
		if (grade.getType() != Forum.GRADE_BY_TOPIC)
		{
			return false;
		}
		
		if (!jForumGBService.isGradebookDefined(gradebookUid))
		{
			return false;
		}
		
		boolean entryExisInGradebook = false;
		
		if (jForumGBService.isExternalAssignmentDefined(gradebookUid, "discussions-" + String.valueOf(grade.getId())))
		{
			entryExisInGradebook = true;
		}
		
		//add or update to gradebook
		String url = null;
		
		if (entryExisInGradebook)
		{
			/*remove entry in the gradebook and add again if there is no entry with the same name in the gradebook.
			Then publish the scores from grading page.
			Oct 01 2012 - Publish scores to gradebook to fix JFSI-1083 - Changing any of the settings of topics removes grades from GB*/
			jForumGBService.removeExternalAssessment(gradebookUid, "discussions-" + String.valueOf(grade.getId()));
			
			Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(topic.getForumId());
			
			if (jForumGBService.isAssignmentDefined(gradebookUid, topic.getTitle()))
			{
				this.context.put("errorMessage", I18n.getMessage("Grade.AddEditTopicGradeBookConflictingAssignmentNameException"));
				return false;
			}
			
			if (forum.getAccessType() == Forum.ACCESS_DENY)
			{
				return false;
			}
						
			/*if (jForumGBService.addExternalAssessment(gradebookUid, "discussions-" + String.valueOf(grade.getId()), url,  topic.getTitle(), 
					JForumUtil.toDoubleScore(grade.getPoints()), endDate, I18n.getMessage("Grade.sendToGradebook.description")))
			{
				// push scores
				List<Evaluation> evaluations = null;
				EvaluationDAO.EvaluationsSort evalSort = EvaluationDAO.EvaluationsSort.last_name_a;
				
				evaluations = DataAccessDriver.getInstance().newEvaluationDAO().selectTopicEvaluations(topic.getForumId(), topic.getId(), evalSort);
				
				//points double datatype for gradebook
				Map<String, Double> scores = new HashMap<String, Double>();
				for(Evaluation eval: evaluations) 
				{
					if (eval.isReleased())
					{
						String key = eval.getSakaiUserId();
						Float userScore = eval.getScore();
						//scores.put(key, (userScore == null) ? null : Double.valueOf(userScore.doubleValue()));
						scores.put(key, (userScore == null) ? null :JForumUtil.toDoubleScore(userScore));
					}
				}

				//remove and update scores
				jForumGBService.updateExternalAssessmentScores(gradebookUid, "discussions-"+ String.valueOf(grade.getId()), scores);
				
				return true;
			}
			
			return false;*/
		}
		else
		{
			/*Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(topic.getForumId());
			
			if (forum.getAccessType() == Forum.ACCESS_DENY)
			{
				return false;
			}
			
			if (jForumGBService.isAssignmentDefined(gradebookUid, topic.getTitle()))
			{
				this.context.put("errorMessage", I18n.getMessage("Grade.AddEditTopicGradeBookConflictingAssignmentNameException"));
				
				return false;
			}
			
			if (jForumGBService.addExternalAssessment(gradebookUid, "discussions-" + String.valueOf(grade.getId()), url, topic.getTitle(), 
					JForumUtil.toDoubleScore(grade.getPoints()), endDate, I18n.getMessage("Grade.sendToGradebook.description")))
			{
				return true;
			}
			
			return false;*/			
		}
		
		if (jForumGBService.addExternalAssessment(gradebookUid, "discussions-" + String.valueOf(grade.getId()), url,  topic.getTitle(), 
				JForumUtil.toDoubleScore(grade.getPoints()), endDate, I18n.getMessage("Grade.sendToGradebook.description")))
		{
			// push scores
			List<Evaluation> evaluations = null;
			EvaluationDAO.EvaluationsSort evalSort = EvaluationDAO.EvaluationsSort.last_name_a;
			
			evaluations = DataAccessDriver.getInstance().newEvaluationDAO().selectTopicEvaluations(topic.getForumId(), topic.getId(), evalSort);
			
			//points double datatype for gradebook
			Map<String, Double> scores = new HashMap<String, Double>();
			for(Evaluation eval: evaluations) 
			{
				if (eval.isReleased())
				{
					String key = eval.getSakaiUserId();
					Float userScore = eval.getScore();
					//scores.put(key, (userScore == null) ? null : Double.valueOf(userScore.doubleValue()));
					scores.put(key, (userScore == null) ? null :JForumUtil.toDoubleScore(userScore));
				}
			}

			//remove and update scores
			jForumGBService.updateExternalAssessmentScores(gradebookUid, "discussions-"+ String.valueOf(grade.getId()), scores);
			
			return true;
		}
		
		return false;

	}
	
	/**
	 * remove entry from gradebook
	 * @param grade
	 */
	protected void removeEntryFromGradeBook(Grade grade) throws Exception
	{
		//remove entry from gradebook
		if (grade != null) 
		{
			
			JForumGBService jForumGBService = null;
			jForumGBService = (JForumGBService)ComponentManager.get("org.etudes.api.app.jforum.JForumGBService");
			
			if (jForumGBService == null)
				return;
			
			String gradebookUid = ToolManager.getInstance().getCurrentPlacement().getContext();
			if (jForumGBService.isExternalAssignmentDefined(gradebookUid, "discussions-" + String.valueOf(grade.getId())))
			{
				jForumGBService.removeExternalAssessment(gradebookUid, "discussions-" + String.valueOf(grade.getId()));
			}
		}
	}
	
	/**
	 * delete topic special access
	 * @param t		Topic
	 * @throws Exception
	 */
	protected void deleteSpecialAccess(Topic t) throws Exception
	{
		List<SpecialAccess> topicSpecialAccessList = DataAccessDriver.getInstance().newSpecialAccessDAO().selectByTopic(t.getForumId(), t.getId());
		
		for (SpecialAccess exiSpecialAccess : topicSpecialAccessList)
		{
			DataAccessDriver.getInstance().newSpecialAccessDAO().delete(exiSpecialAccess.getId());
		}
	}
}