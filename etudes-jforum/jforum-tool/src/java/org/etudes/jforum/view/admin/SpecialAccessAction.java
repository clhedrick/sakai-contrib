/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/view/admin/SpecialAccessAction.java $ 
 * $Id: SpecialAccessAction.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.jforum.view.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.JForumForumService;
import org.etudes.api.app.jforum.JForumPostService;
import org.etudes.api.app.jforum.JForumSpecialAccessService;
import org.etudes.api.app.jforum.JForumUserService;
import org.etudes.jforum.JForum;
import org.etudes.jforum.dao.DataAccessDriver;
import org.etudes.jforum.dao.generic.UserOrderComparator;
import org.etudes.jforum.entities.Category;
import org.etudes.jforum.entities.Forum;
import org.etudes.jforum.entities.SpecialAccess;
import org.etudes.jforum.entities.User;
import org.etudes.jforum.util.I18n;
import org.etudes.jforum.util.date.DateUtil;
import org.etudes.jforum.util.preferences.ConfigKeys;
import org.etudes.jforum.util.preferences.SakaiSystemGlobals;
import org.etudes.jforum.util.preferences.SystemGlobals;
import org.etudes.jforum.util.preferences.TemplateKeys;
import org.etudes.jforum.util.user.JForumUserUtil;
import org.sakaiproject.authz.api.Member;
import org.sakaiproject.authz.cover.SecurityService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.cover.SiteService;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.user.cover.UserDirectoryService;

public class SpecialAccessAction extends AdminCommand
{

	private static Log logger = LogFactory.getLog(SpecialAccessAction.class);
	
	@Override
	public void list() throws Exception
	{
	}
	
	/**
	 * Show forum special access list
	 * @throws Exception
	 */
	public void showForumList() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		this.context.put("viewTitleManageForums", true);
		int forumId = this.request.getIntParameter("forum_id");
		
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(forumId);
		JForumForumService jforumForumService = (JForumForumService) ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
		org.etudes.api.app.jforum.Forum forum = jforumForumService.getForum(forumId);
		
		this.context.put("forum", forum);
		
		//Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		this.context.put("category", forum.getCategory());
		
		//List<SpecialAccess> forumSpecialAccessList = DataAccessDriver.getInstance().newSpecialAccessDAO().selectByForumId(forumId);
		JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
		List<org.etudes.api.app.jforum.SpecialAccess> forumSpecialAccessList = jforumSpecialAccessService.getByForum(forumId);
		
		// TODO: remove the special access and delete the user if user is inactive or removed from the site
					
		this.context.put("forumspecialAccessList", forumSpecialAccessList);
		
		this.context.put("action", "delete");
		this.setTemplateName(TemplateKeys.SPECIAL_ACCESS_FORUM_LIST);
	}
	
	/**
	 * Insert forum special access
	 * @throws Exception
	 */
	public void insertForum() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		int forumId = this.request.getIntParameter("forum_id");
		
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(forumId);
		JForumForumService jforumForumService = (JForumForumService) ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
		org.etudes.api.app.jforum.Forum forum = jforumForumService.getForum(forumId);
		
		this.context.put("forum", forum);
		
		//getForumUsers(forum);
		if (forum.getAccessType() == org.etudes.api.app.jforum.Forum.ForumAccess.GROUPS.getAccessType())
		{
			this.context.put("groupsExist", true);
		}
		List<org.etudes.api.app.jforum.User> users = jforumForumService.getForumParticipantUsers(forum.getId());
		this.context.put("users", users);
		
		Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		this.context.put("category", category);
		
		this.context.put("viewTitleManageForums", true);
		this.context.put("action", "insertForumSave");
		//context.put("calendarDateTimeFormat", SakaiSystemGlobals.getValue(ConfigKeys.CALENDAR_DATE_TIME_FORMAT));
		
		this.setTemplateName(TemplateKeys.SPECIAL_ACCESS_FORUM_FORM);
	}

	/**
	 * Edit forum special access
	 * @throws Exception
	 */
	public void editForum() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();

		if (!isfacilitator)
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}

		int specialAccessId = this.request.getIntParameter("special_access_id");
		//SpecialAccess specialAccess = DataAccessDriver.getInstance().newSpecialAccessDAO().selectById(specialAccessId);
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(specialAccess.getForumId());
		JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
		
		org.etudes.api.app.jforum.SpecialAccess specialAccess = jforumSpecialAccessService.getSpecialAccess(specialAccessId);
		
		if (specialAccess == null)
		{
			if (logger.isErrorEnabled())
			{
				logger.error("special access with id " + specialAccessId + " not found");
			}
			this.context.put("errorMessage", I18n.getMessage("SpecialAccess.NotFound"));
			this.setTemplateName(TemplateKeys.ITEM_NOT_FOUND_MESSAGE);
			return;
		}
		
		JForumForumService jforumForumService = (JForumForumService) ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
		org.etudes.api.app.jforum.Forum forum = jforumForumService.getForum(specialAccess.getForumId());
		this.context.put("forum", forum);

		if (specialAccess != null)
		{
			if (!specialAccess.isOverrideStartDate())
			{
				specialAccess.getAccessDates().setOpenDate(forum.getAccessDates().getOpenDate());
				
				if (!specialAccess.isOverrideHideUntilOpen())
				{
					specialAccess.getAccessDates().setHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
				}
			}
			
			if (!specialAccess.isOverrideHideUntilOpen())
			{
				specialAccess.setOverrideHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
				if (!specialAccess.isOverrideStartDate())
				{
					specialAccess.getAccessDates().setHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
				}
			}
			
			if (!specialAccess.isOverrideEndDate())
			{
				specialAccess.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
				/*if (!specialAccess.isOverrideLockEndDate())
				{
					specialAccess.getAccessDates().setLocked(forum.getAccessDates().isLocked());
				}*/
			}
			
			if (!specialAccess.isOverrideAllowUntilDate())
			{
				specialAccess.getAccessDates().setAllowUntilDate(forum.getAccessDates().getAllowUntilDate());
			}

			/*if (!specialAccess.isOverrideLockEndDate())
			{
				specialAccess.setOverrideLockEndDate(forum.getAccessDates().isLocked());
				if (!specialAccess.isOverrideEndDate())
				{
					specialAccess.getAccessDates().setLocked(forum.getAccessDates().isLocked());
				}
			}*/
		}

		this.context.put("specialAccess", specialAccess);

		// getForumSpecialAccess(specialAccess, forum);
		//getForumUsers(forum);
		List<org.etudes.api.app.jforum.User> users = jforumForumService.getForumParticipantUsers(forum.getId());
		this.context.put("users", users);

		this.context.put("viewTitleManageForums", true);
		this.context.put("action", "editForumSave");
		//context.put("calendarDateTimeFormat", SakaiSystemGlobals.getValue(ConfigKeys.CALENDAR_DATE_TIME_FORMAT));

		this.setTemplateName(TemplateKeys.SPECIAL_ACCESS_FORUM_FORM);
	}
	
	/**
	 * Save new forum special access
	 * @throws Exception
	 */
	public void insertForumSave() throws Exception
	{
		
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		String forumSpecialAccess = this.request.getParameter("forum_special_access");
		String forumGroupSpecialAccess = this.request.getParameter("forum_group_special_access");
		
		int forumId = this.request.getIntParameter("forum_id");
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(forumId);
		
		JForumForumService jforumForumService = (JForumForumService) ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
		org.etudes.api.app.jforum.Forum forum = jforumForumService.getForum(forumId);
		
		if (forumSpecialAccess != null)
		{
			addForumSpecialAccess(forum);
		}
		else if (forumGroupSpecialAccess != null)
		{
			addForumSpecialAccess(forum);
			//addForumGroupSpecialAccess(forum);
		}
		
		this.context.put("forum", forum);		
			
		this.showForumList();
	}
	
	/**
	 * Save existing forum special access
	 * @throws Exception
	 */
	public void editForumSave() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		String forumSpecialAccess = this.request.getParameter("forum_special_access");
		//String forumGroupSpecialAccess = this.request.getParameter("forum_group_special_access");
		
		int forumId = this.request.getIntParameter("forum_id");
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(forumId);
		JForumForumService jforumForumService = (JForumForumService) ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
		org.etudes.api.app.jforum.Forum forum = jforumForumService.getForum(forumId);
		
		if (forumSpecialAccess != null)
		{
			editForumSpecialAccess(forum);
		}
					
		this.showForumList();
	}
	
	/**
	 * Delete special access
	 * @throws Exception
	 */
	public void delete() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		String ids[] = this.request.getParameterValues("special_access_id");
		
		if (ids != null) 
		{				
			JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
			for (int i = 0; i < ids.length; i++)
			{
				int specialAccessId = Integer.parseInt(ids[i]);
				
				//DataAccessDriver.getInstance().newSpecialAccessDAO().delete(id);
				jforumSpecialAccessService.delete(specialAccessId);
			}
		}
		
		String mode = this.request.getParameter("mode");
		
		if (mode.equals("forum_sa"))
		{
			this.showForumList();
		}
	}
	
	/**
	 * Delete user special access for the forum
	 * 
	 * @throws Exception
	 */
	public void deleteForumUser() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		int specialAccessId = this.request.getIntParameter("special_access_id");
		int forumId = this.request.getIntParameter("forum_id");
		int userId = this.request.getIntParameter("user_id");
				
		// delete existing special access for the selected user
		//List<Integer> specialAccessUser = new ArrayList<Integer>();
		//specialAccessUser.add(new Integer(userId));
		
		/*SpecialAccess exisSpecialAccess = DataAccessDriver.getInstance().newSpecialAccessDAO().selectById(specialAccessId);
		List<Integer>  exisUserIds = exisSpecialAccess.getUserIds();
		if (exisUserIds.removeAll(specialAccessUser))
		{
			if (exisUserIds.size() > 0)
			{	
				exisSpecialAccess.setUserIds(exisUserIds);
				DataAccessDriver.getInstance().newSpecialAccessDAO().update(exisSpecialAccess);
			}
			else
			{
				DataAccessDriver.getInstance().newSpecialAccessDAO().delete(exisSpecialAccess.getId());
			}
		}*/
		
		JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
		jforumSpecialAccessService.removeUserSpecialAccess(specialAccessId, userId);
		
		JForum.setRedirect(this.request.getContextPath() +"/gradeForum/evalForumList/"+ forumId +"/name/a" + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
	}
	
	/**
	 * Delete user special access for the topic
	 * 
	 * @throws Exception
	 */
	public void deleteTopicUser() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		int specialAccessId = this.request.getIntParameter("special_access_id");
		int topicId = this.request.getIntParameter("topic_id");
		int userId = this.request.getIntParameter("user_id");
				
		// delete existing special access for the selected user
		/*List<Integer> specialAccessUser = new ArrayList<Integer>();
		specialAccessUser.add(new Integer(userId));
		
		SpecialAccess exisSpecialAccess = DataAccessDriver.getInstance().newSpecialAccessDAO().selectById(specialAccessId);
		List<Integer>  exisUserIds = exisSpecialAccess.getUserIds();
		if (exisUserIds.removeAll(specialAccessUser))
		{
			if (exisUserIds.size() > 0)
			{	
				exisSpecialAccess.setUserIds(exisUserIds);
				DataAccessDriver.getInstance().newSpecialAccessDAO().update(exisSpecialAccess);
			}
			else
			{
				DataAccessDriver.getInstance().newSpecialAccessDAO().delete(exisSpecialAccess.getId());
			}
		}*/
		
		JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
		jforumSpecialAccessService.removeUserSpecialAccess(specialAccessId, userId);
		
		JForum.setRedirect(this.request.getContextPath() +"/gradeForum/evalTopicList/"+ topicId +"/name/a" + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
	}
	
	/**
	 * Add special access to forum user
	 * @throws Exception
	 */
	public void addForumUser() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}

		int forumId = this.request.getIntParameter("forum_id");
		int userId = this.request.getIntParameter("user_id");
		
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(forumId);
		JForumForumService jforumForumService = (JForumForumService) ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
		org.etudes.api.app.jforum.Forum forum = jforumForumService.getForum(forumId);
		this.context.put("forum", forum);
		
		//Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		this.context.put("category", forum.getCategory());
		
		//User user = DataAccessDriver.getInstance().newUserDAO().selectById(userId);
		JForumUserService jforumUserService = (JForumUserService) ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		org.etudes.api.app.jforum.User user = jforumUserService.getByUserId(userId);
		this.context.put("user", user);
		
		// get forum user groups
		getForumUserGroups(forum, user);
			
		//context.put("calendarDateTimeFormat", SakaiSystemGlobals.getValue(ConfigKeys.CALENDAR_DATE_TIME_FORMAT));
		
		this.context.put("action", "addForumUserSave");
		
		this.setTemplateName(TemplateKeys.SPECIAL_ACCESS_FORUM_ADD_EDIT_USER);
	}
	
	/**
	 * Add special access to topic user
	 * @throws Exception
	 */
	public void addTopicUser() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}

		int topicId = this.request.getIntParameter("topic_id");
		int userId = this.request.getIntParameter("user_id");
		
		//Topic topic = DataAccessDriver.getInstance().newTopicDAO().selectById(topicId);
		JForumPostService jforumPostService = (JForumPostService) ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		org.etudes.api.app.jforum.Topic topic = jforumPostService.getTopic(topicId);
		this.context.put("topic", topic);
		
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(topic.getForumId());
		org.etudes.api.app.jforum.Forum forum = topic.getForum();
		this.context.put("forum", forum);
		
		//Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		org.etudes.api.app.jforum.Category category = forum.getCategory();
		this.context.put("category", category);
		
		//User user = DataAccessDriver.getInstance().newUserDAO().selectById(userId);
		JForumUserService jforumUserService = (JForumUserService) ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		org.etudes.api.app.jforum.User user = jforumUserService.getByUserId(userId);
		
		this.context.put("user", user);
		
		// get forum user groups
		//getForumUserGroups(forum, user);
		getForumUserGroups(forum, user);
			
		//context.put("calendarDateTimeFormat", SakaiSystemGlobals.getValue(ConfigKeys.CALENDAR_DATE_TIME_FORMAT));
		
		this.context.put("action", "addTopicUserSave");
		
		this.setTemplateName(TemplateKeys.SPECIAL_ACCESS_TOPIC_ADD_EDIT_USER);
	}
	
	/**
	 * Save the new special access to forum user
	 * @throws Exception
	 */
	public void addForumUserSave() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		int forumId = this.request.getIntParameter("forum_id");
		//int userId = this.request.getIntParameter("user_id");
		
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(forumId);
		JForumForumService jforumForumService = (JForumForumService) ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
		org.etudes.api.app.jforum.Forum forum = jforumForumService.getForum(forumId);
		
		//SpecialAccess specialAccess = new SpecialAccess();
		JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
		org.etudes.api.app.jforum.SpecialAccess specialAccess = jforumSpecialAccessService.newSpecialAccess(forum.getId(), 0);
		
		//specialAccess.setForumId(forumId);		
		
		this.context.put("forum", forum);
		
		//Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		
		// forum groups user_group
		if ((forum.getGroups() != null) && (forum.getGroups().size() > 0))
		{
			
		}
		
		/*String startDateParam = this.request.getParameter("start_date");
		if (startDateParam != null && startDateParam.trim().length() > 0)
		{
			Date startDate;
			try
			{
				startDate = DateUtil.getDateFromString(startDateParam.trim());
				specialAccess.getAccessDates().setOpenDate(startDate);
				
				// check with forum dates
				if (forum.getAccessDates().getOpenDate() == null)
				{
					specialAccess.setOverrideStartDate(true);					
				}
				else if (startDate.equals(forum.getAccessDates().getOpenDate()))
				{
					specialAccess.setOverrideStartDate(false);
					specialAccess.getAccessDates().setOpenDate(null);
				}
				else
				{
					specialAccess.setOverrideStartDate(true);
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.addForumUser();
				return;
			}
		}
		else
		{
			specialAccess.getAccessDates().setOpenDate(null);
			if (forum.getAccessDates().getOpenDate() != null)
			{
				specialAccess.setOverrideStartDate(true);					
			}
		}
		
		String endDateParam = this.request.getParameter("end_date");
		if (endDateParam != null && endDateParam.trim().length() > 0)
		{
			Date endDate;
			try
			{
				endDate = DateUtil.getDateFromString(endDateParam.trim());
				specialAccess.getAccessDates().setDueDate(endDate);
				
				// lock on due
				String lockOnDue = this.request.getParameter("lock_on_due");
				
				// check with forum dates
				if (forum.getAccessDates().getDueDate() == null)
				{
					specialAccess.setOverrideEndDate(true);
					specialAccess.setOverrideLockEndDate(true);
					if (lockOnDue != null && "1".equals(lockOnDue))
					{
						specialAccess.getAccessDates().setLocked(true);
					}
					else
					{
						specialAccess.getAccessDates().setLocked(false);
					}						
				}
				else
				{
					if (endDate.equals(forum.getAccessDates().getDueDate()))
					{
						specialAccess.getAccessDates().setDueDate(null);
						specialAccess.setOverrideEndDate(false);						
					}
					else
					{
						specialAccess.setOverrideEndDate(true);
					}
					
					// lock on due
					if (specialAccess.isOverrideEndDate())
					{
						ignore lock on due setting of forum
						specialAccess.setOverrideLockEndDate(true);
						
						if (lockOnDue != null && "1".equals(lockOnDue))
						{
							specialAccess.getAccessDates().setLocked(true);							
						}
						else
						{
							specialAccess.getAccessDates().setLocked(false);
						}
					}
					else
					{
						consider lock on due setting of forum and override if not selected for special access
						if (forum.getAccessDates().isLocked())
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(false);
							}
							else
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(true);
							}
						}
						else
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.getAccessDates().setLocked(true);
								specialAccess.setOverrideLockEndDate(true);
							}
							else
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(false);
							}								
						}
					}
				}
			} catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.addForumUser();
				return;
			}
		}
		else
		{
			no special access end date
			
			specialAccess.getAccessDates().setDueDate(null);
						
			if (forum.getAccessDates().getDueDate() != null)
			{
				specialAccess.setOverrideEndDate(true);
				specialAccess.setOverrideLockEndDate(true);
				specialAccess.getAccessDates().setLocked(false);
			}
			else
			{
				specialAccess.setOverrideEndDate(false);
				specialAccess.setOverrideLockEndDate(false);
				specialAccess.getAccessDates().setLocked(false);
			}
		}
		
		int userId = this.request.getIntParameter("user_id");
		List<Integer> specialAccessUser = new ArrayList<Integer>();
		specialAccessUser.add(new Integer(userId));
		
		specialAccess.setUserIds(specialAccessUser);
		*/
		
		/*if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideEndDate())) && (specialAccess.getUserIds().size() > 0))
		{
			int newSpecialAccessId  = DataAccessDriver.getInstance().newSpecialAccessDAO().addNew(specialAccess);
			specialAccess.setId(newSpecialAccessId);
		}*/
		
		try
		{
			addEditUserForumSpecialAccess(forum, specialAccess);
		}
		catch (ParseException e)
		{
			this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
			this.addForumUser();
			return;
		}
		
		jforumSpecialAccessService.createForumSpecialAccess(specialAccess);
		
		JForum.setRedirect(this.request.getContextPath() +"/gradeForum/evalForumList/"+ forumId +"/name/a" + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		
	}
	
	/**
	 * Save the new special access to topic user
	 * @throws Exception
	 */
	public void addTopicUserSave() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		int topicId = this.request.getIntParameter("topic_id");
		int userId = this.request.getIntParameter("user_id");
		
		//Topic topic = DataAccessDriver.getInstance().newTopicDAO().selectById(topicId);
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(topic.getForumId());
		
		JForumPostService jforumPostService = (JForumPostService) ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		org.etudes.api.app.jforum.Topic topic = jforumPostService.getTopic(topicId);
		org.etudes.api.app.jforum.Forum forum = topic.getForum();
		
		//SpecialAccess specialAccess = new SpecialAccess();
		//specialAccess.setForumId(topic.getForumId());
		//specialAccess.setTopicId(topic.getId());
		
		JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
		org.etudes.api.app.jforum.SpecialAccess specialAccess = jforumSpecialAccessService.newSpecialAccess(topic.getForumId(), topic.getId());
		
		this.context.put("topic", topic);
		this.context.put("forum", forum);
		
		//Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		
		/*// forum groups user_group
		if ((forum.getGroups() != null) && (forum.getGroups().size() > 0))
		{
			
		}*/
		
		/*String startDateParam = this.request.getParameter("start_date");
		if (startDateParam != null && startDateParam.trim().length() > 0)
		{
			Date startDate;
			try
			{
				startDate = DateUtil.getDateFromString(startDateParam.trim());
				specialAccess.getAccessDates().setOpenDate(startDate);
				
				//SimpleDateFormat df = new SimpleDateFormat(SakaiSystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				//specialAccess.setStartDateFormatted(df.format(startDate));
				
				// check with topic dates
				if ((topic.getStartDate() == null) || (!startDate.equals(topic.getStartDate())))
				{
					specialAccess.setOverrideStartDate(true);					
				}
				// check with topic dates
				if (topic.getAccessDates().getOpenDate() == null)
				{
					specialAccess.setOverrideStartDate(true);					
				}
				else if (startDate.equals(topic.getAccessDates().getOpenDate()))
				{
					specialAccess.setOverrideStartDate(false);
					specialAccess.getAccessDates().setOpenDate(null);
				}
				else
				{
					specialAccess.setOverrideStartDate(true);
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.addTopicUser();
				return;
			}
		}
		else
		{
			specialAccess.getAccessDates().setOpenDate(null);
			if (topic.getAccessDates().getOpenDate() != null)
			{
				specialAccess.setOverrideStartDate(true);					
			}
		}
		
		String endDateParam = this.request.getParameter("end_date");
		if (endDateParam != null && endDateParam.trim().length() > 0)
		{
			Date endDate;
			try
			{
				endDate = DateUtil.getDateFromString(endDateParam.trim());
				specialAccess.getAccessDates().setDueDate(endDate);
				
				//SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				//specialAccess.setEndDateFormatted(df.format(endDate));
				
				// check with topic dates
				// lock on due
				String lockOnDue = this.request.getParameter("lock_on_due");
				
				// check with topic dates
				if (topic.getAccessDates().getDueDate() == null)
				{
					specialAccess.setOverrideEndDate(true);
					specialAccess.setOverrideLockEndDate(true);
					if (lockOnDue != null && "1".equals(lockOnDue))
					{
						specialAccess.getAccessDates().setLocked(true);
					}
					else
					{
						specialAccess.getAccessDates().setLocked(false);
					}						
				}
				else
				{
					if (endDate.equals(topic.getAccessDates().getDueDate()))
					{
						specialAccess.getAccessDates().setDueDate(null);
						specialAccess.setOverrideEndDate(false);						
					}
					else
					{
						specialAccess.setOverrideEndDate(true);
					}
					
					// lock on due
					if (specialAccess.isOverrideEndDate())
					{
						ignore lock on due setting of forum
						specialAccess.setOverrideLockEndDate(true);
						
						if (lockOnDue != null && "1".equals(lockOnDue))
						{
							specialAccess.getAccessDates().setLocked(true);							
						}
						else
						{
							specialAccess.getAccessDates().setLocked(false);
						}
					}
					else
					{
						consider lock on due setting of topic and override if not selected for special access
						if (topic.getAccessDates().isLocked())
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(false);
							}
							else
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(true);
							}
						}
						else
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.getAccessDates().setLocked(true);
								specialAccess.setOverrideLockEndDate(true);
							}
							else
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(false);
							}								
						}
					}
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.addTopicUser();
				return;
			}
		}
		else
		{
			specialAccess.getAccessDates().setDueDate(null);
						
			if (topic.getAccessDates().getDueDate() != null)
			{
				specialAccess.setOverrideEndDate(true);
				specialAccess.setOverrideLockEndDate(true);
				specialAccess.getAccessDates().setLocked(false);
			}
			else
			{
				specialAccess.setOverrideEndDate(false);
				specialAccess.setOverrideLockEndDate(false);
				specialAccess.getAccessDates().setLocked(false);
			}
		}
		
		List<Integer> specialAccessUser = new ArrayList<Integer>();
		specialAccessUser.add(new Integer(userId));
		
		specialAccess.setUserIds(specialAccessUser);*/
		
		/*
		if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideLockEndDate())) && (specialAccess.getUserIds().size() > 0))
		{
			int newSpecialAccessId  = DataAccessDriver.getInstance().newSpecialAccessDAO().addNew(specialAccess);
			specialAccess.setId(newSpecialAccessId);
		}*/
		
		
		try
		{
			addEditUserTopicSpecialAccess(topic, specialAccess);
		}
		catch (ParseException e)
		{
			this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
			this.addTopicUser();
			return;
		}
		
		jforumSpecialAccessService.createTopicSpecialAccess(specialAccess);
		
		JForum.setRedirect(this.request.getContextPath() +"/gradeForum/evalTopicList/"+ topic.getId() +"/name/a" + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		
	}
	
	/**
	 * Edit the existing special access to Forum user
	 * @throws Exception
	 */
	public void editForumUser() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		int specialAccessId = this.request.getIntParameter("special_access_id");
		int forumId = this.request.getIntParameter("forum_id");
		int userId = this.request.getIntParameter("user_id");
		
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(forumId);
		JForumForumService jforumForumService = (JForumForumService) ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
		org.etudes.api.app.jforum.Forum forum = jforumForumService.getForum(forumId);
		this.context.put("forum", forum);
		
		//SpecialAccess specialAccess = DataAccessDriver.getInstance().newSpecialAccessDAO().selectById(specialAccessId);
		JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
		org.etudes.api.app.jforum.SpecialAccess specialAccess = jforumSpecialAccessService.getSpecialAccess(specialAccessId);
		if (specialAccess != null)
		{
			/*if (!specialAccess.isOverrideStartDate())
			{
				specialAccess.getAccessDates().setOpenDate(forum.getAccessDates().getOpenDate());
			}
			
			if (!specialAccess.isOverrideEndDate())
			{
				specialAccess.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
				
				if (!specialAccess.isOverrideLockEndDate())
				{
					specialAccess.getAccessDates().setLocked(forum.getAccessDates().isLocked());
				}
			}*/
			

			if (!specialAccess.isOverrideStartDate())
			{
				specialAccess.getAccessDates().setOpenDate(forum.getAccessDates().getOpenDate());
				
				if (!specialAccess.isOverrideHideUntilOpen())
				{
					specialAccess.getAccessDates().setHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
				}
			}
			
			if (!specialAccess.isOverrideHideUntilOpen())
			{
				specialAccess.setOverrideHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
				if (!specialAccess.isOverrideStartDate())
				{
					specialAccess.getAccessDates().setHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
				}
			}
			
			if (!specialAccess.isOverrideEndDate())
			{
				specialAccess.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
				/*if (!specialAccess.isOverrideLockEndDate())
				{
					specialAccess.getAccessDates().setLocked(forum.getAccessDates().isLocked());
				}*/
			}
			
			if (!specialAccess.isOverrideAllowUntilDate())
			{
				specialAccess.getAccessDates().setAllowUntilDate(forum.getAccessDates().getAllowUntilDate());
			}		
		}
		
		this.context.put("specialAccess", specialAccess);
				
		//User user = DataAccessDriver.getInstance().newUserDAO().selectById(userId);
		JForumUserService jforumUserService = (JForumUserService) ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		org.etudes.api.app.jforum.User user = jforumUserService.getByUserId(userId);
		this.context.put("user", user);
		
		// get forum user groups
		getForumUserGroups(forum, user);
		
		//context.put("calendarDateTimeFormat", SakaiSystemGlobals.getValue(ConfigKeys.CALENDAR_DATE_TIME_FORMAT));
		
		this.context.put("action", "editForumUserSave");
		
		this.setTemplateName(TemplateKeys.SPECIAL_ACCESS_FORUM_ADD_EDIT_USER);
	}
	
	/**
	 * Save the updated special access to forum user
	 * @throws Exception
	 */
	public void editForumUserSave() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		int specialAccessId = this.request.getIntParameter("special_access_id");
		int forumId = this.request.getIntParameter("forum_id");
		int userId = this.request.getIntParameter("user_id");
		
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(forumId);
		JForumForumService jforumForumService = (JForumForumService) ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
		org.etudes.api.app.jforum.Forum forum = jforumForumService.getForum(forumId);
		
		//SpecialAccess specialAccess = new SpecialAccess();
		JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
		org.etudes.api.app.jforum.SpecialAccess specialAccess = jforumSpecialAccessService.newSpecialAccess(forum.getId(), 0);
		//specialAccess.setForumId(forum.getId());
		
		this.context.put("forum", forum);
		
		// forum groups user_group
		if ((forum.getGroups() != null) && (forum.getGroups().size() > 0))
		{
		}
		
		//Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		
		/*String startDateParam = this.request.getParameter("start_date");
		if (startDateParam != null && startDateParam.trim().length() > 0)
		{
			Date startDate;
			try
			{
				startDate = DateUtil.getDateFromString(startDateParam.trim());
				specialAccess.getAccessDates().setOpenDate(startDate);
				
				//SimpleDateFormat df = new SimpleDateFormat(SakaiSystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				//specialAccess.setStartDateFormatted(df.format(startDate));

				// check with forum dates
				if (forum.getAccessDates().getOpenDate() == null)
				{
					specialAccess.setOverrideStartDate(true);					
				}
				else if (startDate.equals(forum.getAccessDates().getOpenDate()))
				{
					specialAccess.setOverrideStartDate(false);
					specialAccess.getAccessDates().setOpenDate(null);
				}
				else
				{
					specialAccess.setOverrideStartDate(true);
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.editForumUser();
				return;
			}
		}
		else
		{
			specialAccess.getAccessDates().setOpenDate(null);
			if (forum.getAccessDates().getOpenDate() != null)
			{
				specialAccess.setOverrideStartDate(true);					
			}
		}
		
		String endDateParam = this.request.getParameter("end_date");
		if (endDateParam != null && endDateParam.trim().length() > 0)
		{
			Date endDate;
			try
			{
				endDate = DateUtil.getDateFromString(endDateParam.trim());
				specialAccess.getAccessDates().setDueDate(endDate);
				
				//SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				//specialAccess.setEndDateFormatted(df.format(endDate));
				
				// lock on due
				String lockOnDue = this.request.getParameter("lock_on_due");
				
				// check with forum dates
				if (forum.getAccessDates().getDueDate() == null)
				{
					specialAccess.setOverrideEndDate(true);
					specialAccess.setOverrideLockEndDate(true);
					if (lockOnDue != null && "1".equals(lockOnDue))
					{
						specialAccess.getAccessDates().setLocked(true);
					}
					else
					{
						specialAccess.getAccessDates().setLocked(false);
					}						
				}
				else
				{
					if (endDate.equals(forum.getAccessDates().getDueDate()))
					{
						specialAccess.getAccessDates().setDueDate(null);
						specialAccess.setOverrideEndDate(false);						
					}
					else
					{
						specialAccess.setOverrideEndDate(true);
					}
					
					// lock on due
					if (specialAccess.isOverrideEndDate())
					{
						ignore lock on due setting of forum
						specialAccess.setOverrideLockEndDate(true);
						
						if (lockOnDue != null && "1".equals(lockOnDue))
						{
							specialAccess.getAccessDates().setLocked(true);							
						}
						else
						{
							specialAccess.getAccessDates().setLocked(false);
						}
					}
					else
					{
						consider lock on due setting of forum and override if not selected for special access
						if (forum.getAccessDates().isLocked())
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(false);
							}
							else
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(true);
							}
						}
						else
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.getAccessDates().setLocked(true);
								specialAccess.setOverrideLockEndDate(true);
							}
							else
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(false);
							}								
						}
					}
				}
			} catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.editForumUser();
				return;
			}
		}
		else
		{
			no special access end date
			
			specialAccess.getAccessDates().setDueDate(null);
						
			if (forum.getAccessDates().getDueDate() != null)
			{
				specialAccess.setOverrideEndDate(true);
				specialAccess.setOverrideLockEndDate(true);
				specialAccess.getAccessDates().setLocked(false);
			}
			else
			{
				specialAccess.setOverrideEndDate(false);
				specialAccess.setOverrideLockEndDate(false);
				specialAccess.getAccessDates().setLocked(false);
			}
		}
		
		List<Integer> userIds = new ArrayList<Integer>();
		userIds.add(new Integer(userId));
		specialAccess.setUserIds(userIds);*/
		
		try
		{
			addEditUserForumSpecialAccess(forum, specialAccess);
		}
		catch (ParseException e)
		{
			this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
			this.editForumUser();
			return;
		}
		
		jforumSpecialAccessService.createForumSpecialAccess(specialAccess);
		
		/*// delete existing special access for the selected user
		SpecialAccess exisSpecialAccess = DataAccessDriver.getInstance().newSpecialAccessDAO().selectById(specialAccessId);
		List<Integer>  exisUserIds = exisSpecialAccess.getUserIds();
		if (exisUserIds.removeAll(specialAccessUser))
		{
			if (exisUserIds.size() > 0)
			{	
				exisSpecialAccess.setUserIds(exisUserIds);
				DataAccessDriver.getInstance().newSpecialAccessDAO().update(exisSpecialAccess);
			}
			else
			{
				DataAccessDriver.getInstance().newSpecialAccessDAO().delete(exisSpecialAccess.getId());
			}
		}
		
		specialAccess.setUserIds(specialAccessUser);
		if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideEndDate())) && (specialAccess.getUserIds().size() > 0))
		{
			int newSpecialAccessId  = DataAccessDriver.getInstance().newSpecialAccessDAO().addNew(specialAccess);
			specialAccess.setId(newSpecialAccessId);
		}*/
		
		JForum.setRedirect(this.request.getContextPath() +"/gradeForum/evalForumList/"+ forumId +"/name/a" + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
	}
	
	/**
	 * Edit the existing special access to topic user
	 * @throws Exception
	 */
	public void editTopicUser() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		int specialAccessId = this.request.getIntParameter("special_access_id");
		int topicId = this.request.getIntParameter("topic_id");
		int userId = this.request.getIntParameter("user_id");
		
		//Topic topic = DataAccessDriver.getInstance().newTopicDAO().selectById(topicId);
		JForumPostService jforumPostService = (JForumPostService) ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		org.etudes.api.app.jforum.Topic topic = jforumPostService.getTopic(topicId);
		this.context.put("topic", topic);
		
		//SpecialAccess specialAccess = DataAccessDriver.getInstance().newSpecialAccessDAO().selectById(specialAccessId);
		JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
		org.etudes.api.app.jforum.SpecialAccess specialAccess = jforumSpecialAccessService.getSpecialAccess(specialAccessId);
		if (specialAccess != null)
		{
			/*if (!specialAccess.isOverrideStartDate())
			{
				specialAccess.getAccessDates().setDueDate(topic.getAccessDates().getOpenDate());
			}
			
			if (!specialAccess.isOverrideEndDate())
			{
				specialAccess.getAccessDates().setDueDate(topic.getAccessDates().getDueDate());
				
				if (!specialAccess.isOverrideLockEndDate())
				{
					specialAccess.getAccessDates().setLocked(topic.getAccessDates().isLocked());
				}
			}*/
			
			if (!specialAccess.isOverrideStartDate())
			{
				specialAccess.getAccessDates().setOpenDate(topic.getAccessDates().getOpenDate());
				
				if (!specialAccess.isOverrideHideUntilOpen())
				{
					specialAccess.getAccessDates().setHideUntilOpen(topic.getAccessDates().isHideUntilOpen());
				}
			}
			
			if (!specialAccess.isOverrideHideUntilOpen())
			{
				specialAccess.setOverrideHideUntilOpen(topic.getAccessDates().isHideUntilOpen());
				if (!specialAccess.isOverrideStartDate())
				{
					specialAccess.getAccessDates().setHideUntilOpen(topic.getAccessDates().isHideUntilOpen());
				}
			}
			
			if (!specialAccess.isOverrideEndDate())
			{
				specialAccess.getAccessDates().setDueDate(topic.getAccessDates().getDueDate());
			}
			
			if (!specialAccess.isOverrideAllowUntilDate())
			{
				specialAccess.getAccessDates().setAllowUntilDate(topic.getAccessDates().getAllowUntilDate());
			}
		}
		this.context.put("specialAccess", specialAccess);		
		
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(topic.getForumId());
		//JForumForumService jforumForumService = (JForumForumService) ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
		//org.etudes.api.app.jforum.Forum forum = jforumForumService.getForum(topic.getForumId());
		org.etudes.api.app.jforum.Forum forum = topic.getForum();
		this.context.put("forum", topic.getForum());
		
		//Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		org.etudes.api.app.jforum.Category category = forum.getCategory();
		this.context.put("category", category);
		
		//User user = DataAccessDriver.getInstance().newUserDAO().selectById(userId);
		JForumUserService jforumUserService = (JForumUserService) ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		org.etudes.api.app.jforum.User user = jforumUserService.getByUserId(userId);
		this.context.put("user", user);
		
		// get forum user groups
		getForumUserGroups(forum, user);
		
		//context.put("calendarDateTimeFormat", SakaiSystemGlobals.getValue(ConfigKeys.CALENDAR_DATE_TIME_FORMAT));
		
		this.context.put("action", "editTopicUserSave");
		
		this.setTemplateName(TemplateKeys.SPECIAL_ACCESS_TOPIC_ADD_EDIT_USER);
	}
	
	/**
	 * Save the updated special access to topic user
	 * @throws Exception
	 */
	public void editTopicUserSave() throws Exception
	{
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		if (!isfacilitator) 
		{
			this.context.put("message", I18n.getMessage("User.NotAuthorizedToManage"));
			this.setTemplateName(TemplateKeys.MANAGE_NOT_AUTHORIZED);
			return;
		}
		
		int specialAccessId = this.request.getIntParameter("special_access_id");
		int topicId = this.request.getIntParameter("topic_id");
		int userId = this.request.getIntParameter("user_id");
		
		//Topic topic = DataAccessDriver.getInstance().newTopicDAO().selectById(topicId);
		JForumPostService jforumPostService = (JForumPostService) ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		org.etudes.api.app.jforum.Topic topic = jforumPostService.getTopic(topicId);
		
		//Forum forum = DataAccessDriver.getInstance().newForumDAO().selectById(topic.getForumId());
		org.etudes.api.app.jforum.Forum forum = topic.getForum();
				
		//SpecialAccess specialAccess = new SpecialAccess();
		//specialAccess.setForumId(forum.getId());
		//specialAccess.setTopicId(topic.getId());
		
		this.context.put("forum", forum);
		this.context.put("topic", topic);
		
		// forum groups user_group
		if ((forum.getGroups() != null) && (forum.getGroups().size() > 0))
		{
		}
		
		//Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
		org.etudes.api.app.jforum.SpecialAccess specialAccess = jforumSpecialAccessService.newSpecialAccess(topic.getForumId(), topic.getId());
		
		/*String startDateParam = this.request.getParameter("start_date");
		if (startDateParam != null && startDateParam.trim().length() > 0)
		{
			Date startDate;
			try
			{
				startDate = DateUtil.getDateFromString(startDateParam.trim());
				specialAccess.getAccessDates().setOpenDate(startDate);
				
				//SimpleDateFormat df = new SimpleDateFormat(SakaiSystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				//specialAccess.setStartDateFormatted(df.format(startDate));
				
				// check with topic dates
				if (topic.getAccessDates().getOpenDate() == null)
				{
					specialAccess.setOverrideStartDate(true);					
				}
				else if (startDate.equals(topic.getAccessDates().getOpenDate()))
				{
					specialAccess.setOverrideStartDate(false);
					specialAccess.getAccessDates().setOpenDate(null);
				}
				else
				{
					specialAccess.setOverrideStartDate(true);
				}
			} catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.editTopicUser();
				return;
			}
		}
		else
		{
			specialAccess.getAccessDates().setOpenDate(null);
			if (topic.getAccessDates().getOpenDate() != null)
			{
				specialAccess.setOverrideStartDate(true);					
			}
		}
		
		String endDateParam = this.request.getParameter("end_date");
		if (endDateParam != null && endDateParam.trim().length() > 0)
		{
			Date endDate;
			try
			{
				endDate = DateUtil.getDateFromString(endDateParam.trim());
				specialAccess.getAccessDates().setDueDate(endDate);
				
				//SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				//specialAccess.setEndDateFormatted(df.format(endDate));
				
				// lock on due
				String lockOnDue = this.request.getParameter("lock_on_due");
				
				// check with topic dates
				if (topic.getAccessDates().getDueDate() == null)
				{
					specialAccess.setOverrideEndDate(true);
					specialAccess.setOverrideLockEndDate(true);
					if (lockOnDue != null && "1".equals(lockOnDue))
					{
						specialAccess.getAccessDates().setLocked(true);
					}
					else
					{
						specialAccess.getAccessDates().setLocked(false);
					}						
				}
				else
				{
					if (endDate.equals(topic.getAccessDates().getDueDate()))
					{
						specialAccess.getAccessDates().setDueDate(null);
						specialAccess.setOverrideEndDate(false);						
					}
					else
					{
						specialAccess.setOverrideEndDate(true);
					}
					
					// lock on due
					if (specialAccess.isOverrideEndDate())
					{
						ignore lock on due setting of forum
						specialAccess.setOverrideLockEndDate(true);
						
						if (lockOnDue != null && "1".equals(lockOnDue))
						{
							specialAccess.getAccessDates().setLocked(true);							
						}
						else
						{
							specialAccess.getAccessDates().setLocked(false);
						}
					}
					else
					{
						consider lock on due setting of topic and override if not selected for special access
						if (topic.getAccessDates().isLocked())
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(false);
							}
							else
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(true);
							}
						}
						else
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.getAccessDates().setLocked(true);
								specialAccess.setOverrideLockEndDate(true);
							}
							else
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(false);
							}								
						}
					}
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.editTopicUser();
				return;
			}
		}
		else
		{
			specialAccess.getAccessDates().setDueDate(null);
			
			if (topic.getAccessDates().getDueDate() != null)
			{
				specialAccess.setOverrideEndDate(true);
				specialAccess.setOverrideLockEndDate(true);
				specialAccess.getAccessDates().setLocked(false);
			}
			else
			{
				specialAccess.setOverrideEndDate(false);
				specialAccess.setOverrideLockEndDate(false);
				specialAccess.getAccessDates().setLocked(false);
			}
		}
		
		List<Integer> specialAccessUser = new ArrayList<Integer>();
		specialAccessUser.add(new Integer(userId));*/
		
		// delete existing special access for the selected user
		/*SpecialAccess exisSpecialAccess = DataAccessDriver.getInstance().newSpecialAccessDAO().selectById(specialAccessId);
		List<Integer>  exisUserIds = exisSpecialAccess.getUserIds();
		if (exisUserIds.removeAll(specialAccessUser))
		{
			if (exisUserIds.size() > 0)
			{	
				exisSpecialAccess.setUserIds(exisUserIds);
				DataAccessDriver.getInstance().newSpecialAccessDAO().update(exisSpecialAccess);
			}
			else
			{
				DataAccessDriver.getInstance().newSpecialAccessDAO().delete(exisSpecialAccess.getId());
			}
		}
		
		specialAccess.setUserIds(specialAccessUser);
		if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideEndDate())) && (specialAccess.getUserIds().size() > 0))
		{
			int newSpecialAccessId  = DataAccessDriver.getInstance().newSpecialAccessDAO().addNew(specialAccess);
			specialAccess.setId(newSpecialAccessId);
		}*/
		//specialAccess.setUserIds(specialAccessUser);
		
		try
		{
			addEditUserTopicSpecialAccess(topic, specialAccess);
		}
		catch (ParseException e)
		{
			this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
			this.editTopicUser();
			return;
		}
		
		jforumSpecialAccessService.createTopicSpecialAccess(specialAccess);
		
		JForum.setRedirect(this.request.getContextPath() +"/gradeForum/evalTopicList/"+ topicId +"/name/a" + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
	}
	
	/**
	 * Gets the forum users with groups or with out groups
	 * @param forum
	 * @throws IdUnusedException
	 */
	protected void getForumUsers(org.etudes.api.app.jforum.Forum forum) throws IdUnusedException
	{
		// check for groups
		if ((forum.getGroups() != null) && forum.getGroups().size() > 0)
		{
			List users = null;
			Map<String, User> usersMap = new HashMap<String, User>();
			try 
			{
				users = JForumUserUtil.updateMembersInfo();
				Iterator userIterator = users.iterator();
				while (userIterator.hasNext())
				{
					User user = (User) userIterator.next();
					if (JForumUserUtil.isJForumFacilitator(user.getSakaiUserId()))
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
					
			List forumGroupsIds = forum.getGroups();
			this.context.put("groupsExist", true);
			
			//show the selected groups for the forum and group users
			Site site = SiteService.getSite(ToolManager.getCurrentPlacement().getContext());
			Collection sakaiSiteGroups = site.getGroups();
			//this.context.put("sakaigroups", sakaiSiteGroups);
			
			List sakaiSiteGroupsUsed = new ArrayList();
			Map forumGroupsMap = new HashMap();
			List<User> forumGroupMembers = new ArrayList<User>();
			
			for (Iterator i = sakaiSiteGroups.iterator(); i.hasNext();)
			{
				Group group = (Group) i.next();
				
				// get forum groups
				/*if (forumGroupsIds.contains(group.getId()))
				{
					sakaiSiteGroupsUsed.add(group);
					
					List<User> groupMembers = new ArrayList<User>();
					
					Set members = group.getMembers();
					for (Iterator iter = members.iterator(); iter.hasNext();)
					{
						Member member = (Member) iter.next();
						
						if (usersMap.containsKey(member.getUserId()))
						{
							groupMembers.add(usersMap.get(member.getUserId()));
						}
					}
					forumGroupsMap.put(group.getId(), groupMembers);
				}*/
				
				
				//get froum group users
				if (forumGroupsIds.contains(group.getId()))
				{
					//sakaiSiteGroupsUsed.add(group);
					
					//List<User> groupMembers = new ArrayList<User>();
					
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
					//forumGroupsMap.put(group.getId(), groupMembers);
				}
			}
			
			//this.context.put("forumGroupsMap", forumGroupsMap);
			//this.context.put("sakaiSiteGroupsUsed", sakaiSiteGroupsUsed);
			
			// this.context.put("forumGroupMembers", forumGroupMembers);
			users.clear();
			users.addAll(forumGroupMembers);
			
			Collections.sort(users,new UserOrderComparator());
			
			this.context.put("users", users);
		}
		else
		{
			List users = null;
			try 
			{
				users = JForumUserUtil.updateMembersInfo();
				Iterator userIterator = users.iterator();
				while (userIterator.hasNext())
				{
					User user = (User) userIterator.next();
					if (JForumUserUtil.isJForumFacilitator(user.getSakaiUserId()))
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
			
			this.context.put("groupsExist", false);
			this.context.put("users", users);
		}
	}
	
	/**
	 * gets the user's sakai groups
	 * @param forum			Forum	
	 * @param user			User
	 * @throws IdUnusedException
	 */
	protected void getForumUserGroups(org.etudes.api.app.jforum.Forum forum, org.etudes.api.app.jforum.User user)throws IdUnusedException
	{
		// check for groups
		if ((forum.getGroups() != null) && forum.getGroups().size() > 0)
		{
			List forumGroupsIds = forum.getGroups();
			this.context.put("groupsExist", true);
			
			Site site = SiteService.getSite(ToolManager.getCurrentPlacement().getContext());
			
			List userSiteGroupsUsed = new ArrayList();
		
			Collection userGroups = site.getGroupsWithMember(user.getSakaiUserId());
			for (Iterator usrGrpIter = userGroups.iterator(); usrGrpIter.hasNext(); ) 
			{
				org.sakaiproject.site.api.Group group = (org.sakaiproject.site.api.Group)usrGrpIter.next();
			
				if (forum.getGroups().contains(group.getId()))
				{
					userSiteGroupsUsed.add(group);
				}
			}
			
			//this.context.put("userSiteGroupsUsed", userSiteGroupsUsed);
		}
		else
		{
			this.context.put("groupsExist", false);
		}
	
		
	}
	
	/**
	 * Edit forum with out group special access
	 * @param forum		Forum
	 * @throws Exception
	 *//*
	protected void editForumSpecialAccess(Forum forum) throws Exception
	{
		Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		
		int specialAccessId = this.request.getIntParameter("special_access_id");
		SpecialAccess specialAccess = DataAccessDriver.getInstance().newSpecialAccessDAO().selectById(specialAccessId);
		
		// visible date
		String startDateParam = this.request.getParameter("start_date");
		if (startDateParam != null && startDateParam.trim().length() > 0)
		{
			Date startDate;
			try
			{
				startDate = DateUtil.getDateFromString(startDateParam.trim());
				specialAccess.setStartDate(startDate);
				
				SimpleDateFormat df = new SimpleDateFormat(SakaiSystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				specialAccess.setStartDateFormatted(df.format(startDate));
				
				// check with forum dates
				if (forum.getStartDate() == null)
				{
					specialAccess.setOverrideStartDate(true);					
				}
				else if (startDate.equals(forum.getStartDate()))
				{
					specialAccess.setOverrideStartDate(false);
					specialAccess.setStartDate(null);
				}
				else
				{
					specialAccess.setOverrideStartDate(true);
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.editForum();
				return;
			}
			
		}
		else
		{			
			specialAccess.setStartDate(null);
			if (forum.getStartDate() != null)
			{
				specialAccess.setOverrideStartDate(true);					
			}
			else
			{
				specialAccess.setOverrideStartDate(false);
			}
		}
		
		// due date
		String endDateParam = this.request.getParameter("end_date");
		if (endDateParam != null && endDateParam.trim().length() > 0)
		{
			Date endDate;
			try
			{
				endDate = DateUtil.getDateFromString(endDateParam.trim());
				specialAccess.setEndDate(endDate);
				
				SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				specialAccess.setEndDateFormatted(df.format(endDate));
				
				// lock on due
				String lockOnDue = this.request.getParameter("lock_on_due");

				// check with forum dates
				if (forum.getEndDate() == null)
				{
					specialAccess.setOverrideEndDate(true);
					specialAccess.setOverrideLockEndDate(true);
					if (lockOnDue != null && "1".equals(lockOnDue))
					{
						specialAccess.setLockOnEndDate(true);
					}
					else
					{
						specialAccess.setLockOnEndDate(false);
					}						
				}
				else
				{
					if (endDate.equals(forum.getEndDate()))
					{
						specialAccess.setEndDate(null);
						specialAccess.setOverrideEndDate(false);						
					}
					else
					{
						specialAccess.setOverrideEndDate(true);
					}

					// lock on due
					if (specialAccess.isOverrideEndDate())
					{
						ignore lock on due setting of forum
						specialAccess.setOverrideLockEndDate(true);

						if (lockOnDue != null && "1".equals(lockOnDue))
						{
							specialAccess.setLockOnEndDate(true);							
						}
						else
						{
							specialAccess.setLockOnEndDate(false);
						}
					}
					else
					{
						consider lock on due setting of forum and override if not selected for special access
						if (forum.isLockForum())
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.setLockOnEndDate(false);
								specialAccess.setOverrideLockEndDate(false);
							}
							else
							{
								specialAccess.setLockOnEndDate(false);
								specialAccess.setOverrideLockEndDate(true);
							}
						}
						else
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.setLockOnEndDate(true);
								specialAccess.setOverrideLockEndDate(true);
							}
							else
							{
								specialAccess.setLockOnEndDate(false);
								specialAccess.setOverrideLockEndDate(false);
							}								
						}
					}
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.editForum();
				return;
			}
		}
		else
		{
			no special access end date
			
			specialAccess.setEndDate(null);

			if (forum.getEndDate() != null)
			{
				specialAccess.setOverrideEndDate(true);
				specialAccess.setOverrideLockEndDate(true);
				specialAccess.setLockOnEndDate(false);
			}
			else
			{
				specialAccess.setOverrideEndDate(false);
				specialAccess.setOverrideLockEndDate(false);
				specialAccess.setLockOnEndDate(false);
			}
		}
		
		List<Integer> users = new ArrayList<Integer>();
		String userIds[] = (String[])this.request.getObjectParameter("toUsername"+"ParamValues");
		if (userIds != null){
						
			for (int i = 0; i < userIds.length; i++) 
			{
				if (userIds[i] != null && userIds[i].trim().length() > 0) 
				{
					int userId;
					try
					{
						userId = Integer.parseInt(userIds[i]);
						users.add(new Integer(userId));
					} 
					catch (NumberFormatException e)
					{
						if (logger.isWarnEnabled()) logger.warn(this.getClass().getName()+".editForumSpecialAccess() : Error while parsing the userId.", e);
					}
				}
			}
		}
		else
		{
			try
			{
				int userId = Integer.parseInt(this.request.getParameter("toUsername"));
				users.add(new Integer(userId));
			} 
			catch (NumberFormatException e)
			{
				if (logger.isWarnEnabled()) logger.warn(this.getClass().getName()+".editForumSpecialAccess() : Error while parsing the userId.", e);
			}
		}
		
		specialAccess.setUserIds(users);
		
		// delete any existing special access for the selected users
		List<SpecialAccess> forumSpecialAccessList = DataAccessDriver.getInstance().newSpecialAccessDAO().selectByForumId(specialAccess.getForumId());
		for (SpecialAccess exiSpecialAccess : forumSpecialAccessList)
		{
			if (exiSpecialAccess.getId() == specialAccess.getId())
				continue;
			
			List<Integer>  exisUserIds = exiSpecialAccess.getUserIds();
			if (exisUserIds.removeAll(users))
			{
				if (exisUserIds.size() > 0)
				{	
					exiSpecialAccess.setUserIds(exisUserIds);
					DataAccessDriver.getInstance().newSpecialAccessDAO().update(exiSpecialAccess);
				}
				else
				{
					DataAccessDriver.getInstance().newSpecialAccessDAO().delete(exiSpecialAccess.getId());
				}
					
			}
		}
		
		if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideLockEndDate())) && (users.size() > 0))
		{
			DataAccessDriver.getInstance().newSpecialAccessDAO().update(specialAccess);
		}
		else
		{
			DataAccessDriver.getInstance().newSpecialAccessDAO().delete(specialAccess.getId());
		}
		
	}*/
	
	/**
	 * Edit forum with out group special access
	 * @param forum		Forum
	 * @throws Exception
	 */
	protected void editForumSpecialAccess(org.etudes.api.app.jforum.Forum forum) throws Exception
	{
		//Category category = DataAccessDriver.getInstance().newCategoryDAO().selectById(forum.getCategoryId());
		
		int specialAccessId = this.request.getIntParameter("special_access_id");
		//SpecialAccess specialAccess = DataAccessDriver.getInstance().newSpecialAccessDAO().selectById(specialAccessId);
		JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
		org.etudes.api.app.jforum.SpecialAccess specialAccess = jforumSpecialAccessService.getSpecialAccess(specialAccessId);
		
		/*// visible date
		String startDateParam = this.request.getParameter("start_date");
		if (startDateParam != null && startDateParam.trim().length() > 0)
		{
			Date startDate;
			try
			{
				startDate = DateUtil.getDateFromString(startDateParam.trim());
				specialAccess.getAccessDates().setOpenDate(startDate);
				
				//SimpleDateFormat df = new SimpleDateFormat(SakaiSystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				//specialAccess.setStartDateFormatted(df.format(startDate));
				
				// check with forum dates
				if (forum.getAccessDates().getOpenDate() == null)
				{
					specialAccess.setOverrideStartDate(true);					
				}
				else if (startDate.equals(forum.getAccessDates().getOpenDate()))
				{
					specialAccess.setOverrideStartDate(false);
					specialAccess.getAccessDates().setOpenDate(null);
				}
				else
				{
					specialAccess.setOverrideStartDate(true);
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.editForum();
				return;
			}
			
		}
		else
		{			
			specialAccess.getAccessDates().setOpenDate(null);
			if (forum.getAccessDates().getOpenDate() != null)
			{
				specialAccess.setOverrideStartDate(true);					
			}
			else
			{
				specialAccess.setOverrideStartDate(false);
			}
		}
		
		// due date
		String endDateParam = this.request.getParameter("end_date");
		if (endDateParam != null && endDateParam.trim().length() > 0)
		{
			Date endDate;
			try
			{
				endDate = DateUtil.getDateFromString(endDateParam.trim());
				specialAccess.getAccessDates().setDueDate(endDate);
				
				//SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				//specialAccess.setEndDateFormatted(df.format(endDate));
				
				// lock on due
				String lockOnDue = this.request.getParameter("lock_on_due");

				// check with forum dates
				if (forum.getAccessDates().getDueDate() == null)
				{
					specialAccess.setOverrideEndDate(true);
					specialAccess.setOverrideLockEndDate(true);
					if (lockOnDue != null && "1".equals(lockOnDue))
					{
						specialAccess.getAccessDates().setLocked(true);
					}
					else
					{
						specialAccess.getAccessDates().setLocked(false);
					}						
				}
				else
				{
					if (endDate.equals(forum.getAccessDates().getDueDate()))
					{
						specialAccess.getAccessDates().setDueDate(null);
						specialAccess.setOverrideEndDate(false);						
					}
					else
					{
						specialAccess.setOverrideEndDate(true);
					}

					// lock on due
					if (specialAccess.isOverrideEndDate())
					{
						ignore lock on due setting of forum
						specialAccess.setOverrideLockEndDate(true);

						if (lockOnDue != null && "1".equals(lockOnDue))
						{
							specialAccess.getAccessDates().setLocked(true);							
						}
						else
						{
							specialAccess.getAccessDates().setLocked(false);
						}
					}
					else
					{
						consider lock on due setting of forum and override if not selected for special access
						if (forum.getAccessDates().isLocked())
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(false);
							}
							else
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(true);
							}
						}
						else
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.getAccessDates().setLocked(true);
								specialAccess.setOverrideLockEndDate(true);
							}
							else
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(false);
							}								
						}
					}
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.editForum();
				return;
			}
		}
		else
		{
			no special access end date
			
			specialAccess.getAccessDates().setDueDate(null);

			if (forum.getAccessDates().getDueDate() != null)
			{
				specialAccess.setOverrideEndDate(true);
				specialAccess.setOverrideLockEndDate(true);
				specialAccess.getAccessDates().setLocked(false);
			}
			else
			{
				specialAccess.setOverrideEndDate(false);
				specialAccess.setOverrideLockEndDate(false);
				specialAccess.getAccessDates().setLocked(false);
			}
		}*/
		
		try
		{
			addEditSpecialAccess(forum, specialAccess);
		}
		catch (ParseException e)
		{
			this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
			this.editForum();
			return;
		}
		
		// delete any existing special access for the selected users
		/*List<SpecialAccess> forumSpecialAccessList = DataAccessDriver.getInstance().newSpecialAccessDAO().selectByForumId(specialAccess.getForumId());
		for (SpecialAccess exiSpecialAccess : forumSpecialAccessList)
		{
			if (exiSpecialAccess.getId() == specialAccess.getId())
				continue;
			
			List<Integer>  exisUserIds = exiSpecialAccess.getUserIds();
			if (exisUserIds.removeAll(users))
			{
				if (exisUserIds.size() > 0)
				{	
					exiSpecialAccess.setUserIds(exisUserIds);
					DataAccessDriver.getInstance().newSpecialAccessDAO().update(exiSpecialAccess);
				}
				else
				{
					DataAccessDriver.getInstance().newSpecialAccessDAO().delete(exiSpecialAccess.getId());
				}
					
			}
		}*/
		
		jforumSpecialAccessService.modifyForumSpecialAccess(specialAccess);
		/*if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideLockEndDate())) && (users.size() > 0))
		{
			DataAccessDriver.getInstance().newSpecialAccessDAO().update(specialAccess);
		}
		else
		{
			DataAccessDriver.getInstance().newSpecialAccessDAO().delete(specialAccess.getId());
		}*/
		
	}

	/**
	 * Adds or edits the forum special access
	 * 
	 * @param forum		Forum
	 * 
	 * @param specialAccess		Special access
	 * 
	 * @throws ParseException	Date parsing error
	 */
	protected void addEditSpecialAccess(org.etudes.api.app.jforum.Forum forum, org.etudes.api.app.jforum.SpecialAccess specialAccess) throws ParseException
	{
		// open date
		String startDateParam = this.request.getParameter("start_date");
		if (startDateParam != null && startDateParam.trim().length() > 0)
		{
			Date startDate;
			try
			{			
				startDate = DateUtil.getDateFromString(startDateParam.trim());
				
				specialAccess.getAccessDates().setOpenDate(startDate);
				
				// hide until open
				String hideUntilOpen = this.request.getParameter("hide_until_open");
				
				// check with forum start date
				if (forum.getAccessDates().getOpenDate() == null)
				{
					specialAccess.setOverrideStartDate(true);
					specialAccess.setOverrideHideUntilOpen(true);
					if (hideUntilOpen != null && "1".equals(hideUntilOpen))
					{
						specialAccess.getAccessDates().setHideUntilOpen(true);
					}
					else
					{
						specialAccess.getAccessDates().setHideUntilOpen(false);
					}						
				}
				else
				{
					if (startDate.equals(forum.getAccessDates().getOpenDate()))
					{
						specialAccess.getAccessDates().setOpenDate(null);
						specialAccess.setOverrideStartDate(false);						
					}
					else
					{
						specialAccess.setOverrideStartDate(true);
					}
					
					// hide until open
					if (specialAccess.isOverrideStartDate())
					{
						/*ignore hide until open setting of forum*/
						//specialAccess.setOverrideStartDate(true);
						specialAccess.setOverrideHideUntilOpen(true);
						
						if (hideUntilOpen != null && "1".equals(hideUntilOpen))
						{
							specialAccess.getAccessDates().setHideUntilOpen(true);							
						}
						else
						{
							specialAccess.getAccessDates().setHideUntilOpen(false);
						}
					}
					else
					{
						//consider hide until open setting of forum and override if not selected for special access
						if (forum.getAccessDates().isHideUntilOpen())
						{
							if (hideUntilOpen != null && "1".equals(hideUntilOpen))
							{
								specialAccess.getAccessDates().setHideUntilOpen(false);
								specialAccess.setOverrideHideUntilOpen(false);
							}
							else
							{
								specialAccess.getAccessDates().setHideUntilOpen(false);
								specialAccess.setOverrideHideUntilOpen(true);
							}
						}
						else
						{
							if (hideUntilOpen != null && "1".equals(hideUntilOpen))
							{
								specialAccess.getAccessDates().setHideUntilOpen(true);
								specialAccess.setOverrideHideUntilOpen(true);
							}
							else
							{
								specialAccess.getAccessDates().setHideUntilOpen(false);
								specialAccess.setOverrideHideUntilOpen(false);
							}								
						}
					}
				}
			
			} 
			catch (ParseException e)
			{
				throw e;
			}
		}
		else
		{
			specialAccess.getAccessDates().setOpenDate(null);
			if (forum.getAccessDates().getOpenDate() != null)
			{
				specialAccess.setOverrideStartDate(true);					
			}			
		}
		
		// due date
		String endDateParam = this.request.getParameter("end_date");
		if (endDateParam != null && endDateParam.trim().length() > 0)
		{
			Date endDate;
			try
			{
				endDate = DateUtil.getDateFromString(endDateParam.trim());
				
				specialAccess.getAccessDates().setDueDate(endDate);
				
				// check with forum dates
				if (forum.getAccessDates().getDueDate() == null)
				{
					specialAccess.setOverrideEndDate(true);
					specialAccess.setOverrideLockEndDate(true);
				}
				else
				{
					if (endDate.equals(forum.getAccessDates().getDueDate()))
					{
						specialAccess.getAccessDates().setDueDate(null);
						specialAccess.setOverrideEndDate(false);						
					}
					else
					{
						specialAccess.setOverrideEndDate(true);
					}
				}
			} 
			catch (ParseException e)
			{
				throw e;
			}
			
		}
		else
		{	/*no special access end date*/
			
			specialAccess.getAccessDates().setDueDate(null);
						
			if (forum.getAccessDates().getDueDate() != null)
			{
				specialAccess.setOverrideEndDate(true);
				specialAccess.setOverrideLockEndDate(true);
			}
			else
			{
				specialAccess.setOverrideEndDate(false);
				specialAccess.setOverrideLockEndDate(false);
			}
		}		
		
		// allow until date
		String allowUntilParam = this.request.getParameter("allow_until");
		if (allowUntilParam != null && allowUntilParam.trim().length() > 0)
		{
			Date allowUntilDate;
			try
			{
				allowUntilDate = DateUtil.getDateFromString(allowUntilParam.trim());
				
				specialAccess.getAccessDates().setAllowUntilDate(allowUntilDate);
				
				// check with forum dates
				if (forum.getAccessDates().getAllowUntilDate() == null)
				{
					specialAccess.setOverrideAllowUntilDate(true);					
				}
				else if (allowUntilDate.equals(forum.getAccessDates().getAllowUntilDate()))
				{
					specialAccess.setOverrideAllowUntilDate(false);
					specialAccess.getAccessDates().setAllowUntilDate(null);
				}
				else
				{
					specialAccess.setOverrideAllowUntilDate(true);
				}
			} 
			catch (ParseException e)
			{
				throw e;
			}
		}
		else
		{
			specialAccess.getAccessDates().setAllowUntilDate(null);
			if (forum.getAccessDates().getAllowUntilDate() != null)
			{
				specialAccess.setOverrideAllowUntilDate(true);					
			}			
		}
		
		List<Integer> users = new ArrayList<Integer>();
		String userIds[] = (String[])this.request.getObjectParameter("toUsername"+"ParamValues");
		if (userIds != null){
						
			for (int i = 0; i < userIds.length; i++) 
			{
				if (userIds[i] != null && userIds[i].trim().length() > 0) 
				{
					int userId;
					try
					{
						userId = Integer.parseInt(userIds[i]);
						users.add(new Integer(userId));
					} 
					catch (NumberFormatException e)
					{
						if (logger.isWarnEnabled()) logger.warn(this.getClass().getName()+".editForumSpecialAccess() : Error while parsing the userId.", e);
					}
				}
			}
		}
		else
		{
			try
			{
				int userId = Integer.parseInt(this.request.getParameter("toUsername"));
				users.add(new Integer(userId));
			} 
			catch (NumberFormatException e)
			{
				if (logger.isWarnEnabled()) logger.warn(this.getClass().getName()+".editForumSpecialAccess() : Error while parsing the userId.", e);
			}
		}
		
		specialAccess.setUserIds(users);
	}
	
	/**
	 * adds or edits the user forum special access
	 * 
	 * @param forum	Forum
	 * 
	 * @param specialAccess	Special access
	 * 
	 * @throws ParseException	Date parsing error
	 */
	protected void addEditUserForumSpecialAccess(org.etudes.api.app.jforum.Forum forum, org.etudes.api.app.jforum.SpecialAccess specialAccess) throws ParseException
	{
		// open date
		String startDateParam = this.request.getParameter("start_date");
		if (startDateParam != null && startDateParam.trim().length() > 0)
		{
			Date startDate;
			try
			{			
				startDate = DateUtil.getDateFromString(startDateParam.trim());
				
				specialAccess.getAccessDates().setOpenDate(startDate);
				
				// hide until open
				String hideUntilOpen = this.request.getParameter("hide_until_open");
				
				// check with forum start date
				if (forum.getAccessDates().getOpenDate() == null)
				{
					specialAccess.setOverrideStartDate(true);
					specialAccess.setOverrideHideUntilOpen(true);
					if (hideUntilOpen != null && "1".equals(hideUntilOpen))
					{
						specialAccess.getAccessDates().setHideUntilOpen(true);
					}
					else
					{
						specialAccess.getAccessDates().setHideUntilOpen(false);
					}						
				}
				else
				{
					if (startDate.equals(forum.getAccessDates().getOpenDate()))
					{
						specialAccess.getAccessDates().setOpenDate(null);
						specialAccess.setOverrideStartDate(false);						
					}
					else
					{
						specialAccess.setOverrideStartDate(true);
					}
					
					// hide until open
					if (specialAccess.isOverrideStartDate())
					{
						/*ignore hide until open setting of forum*/
						//specialAccess.setOverrideStartDate(true);
						specialAccess.setOverrideHideUntilOpen(true);
						
						if (hideUntilOpen != null && "1".equals(hideUntilOpen))
						{
							specialAccess.getAccessDates().setHideUntilOpen(true);							
						}
						else
						{
							specialAccess.getAccessDates().setHideUntilOpen(false);
						}
					}
					else
					{
						//consider hide until open setting of forum and override if not selected for special access
						if (forum.getAccessDates().isHideUntilOpen())
						{
							if (hideUntilOpen != null && "1".equals(hideUntilOpen))
							{
								specialAccess.getAccessDates().setHideUntilOpen(false);
								specialAccess.setOverrideHideUntilOpen(false);
							}
							else
							{
								specialAccess.getAccessDates().setHideUntilOpen(false);
								specialAccess.setOverrideHideUntilOpen(true);
							}
						}
						else
						{
							if (hideUntilOpen != null && "1".equals(hideUntilOpen))
							{
								specialAccess.getAccessDates().setHideUntilOpen(true);
								specialAccess.setOverrideHideUntilOpen(true);
							}
							else
							{
								specialAccess.getAccessDates().setHideUntilOpen(false);
								specialAccess.setOverrideHideUntilOpen(false);
							}								
						}
					}
				}
			
			} 
			catch (ParseException e)
			{
				throw e;
			}
		}
		else
		{
			specialAccess.getAccessDates().setOpenDate(null);
			if (forum.getAccessDates().getOpenDate() != null)
			{
				specialAccess.setOverrideStartDate(true);					
			}			
		}
		
		// due date
		String endDateParam = this.request.getParameter("end_date");
		if (endDateParam != null && endDateParam.trim().length() > 0)
		{
			Date endDate;
			try
			{
				endDate = DateUtil.getDateFromString(endDateParam.trim());
				
				specialAccess.getAccessDates().setDueDate(endDate);
				
				// check with forum dates
				if (forum.getAccessDates().getDueDate() == null)
				{
					specialAccess.setOverrideEndDate(true);
					specialAccess.setOverrideLockEndDate(true);
				}
				else
				{
					if (endDate.equals(forum.getAccessDates().getDueDate()))
					{
						specialAccess.getAccessDates().setDueDate(null);
						specialAccess.setOverrideEndDate(false);						
					}
					else
					{
						specialAccess.setOverrideEndDate(true);
					}
				}
			} 
			catch (ParseException e)
			{
				throw e;
			}
			
		}
		else
		{	/*no special access end date*/
			
			specialAccess.getAccessDates().setDueDate(null);
						
			if (forum.getAccessDates().getDueDate() != null)
			{
				specialAccess.setOverrideEndDate(true);
				specialAccess.setOverrideLockEndDate(true);
			}
			else
			{
				specialAccess.setOverrideEndDate(false);
				specialAccess.setOverrideLockEndDate(false);
			}
		}		
		
		// allow until date
		String allowUntilParam = this.request.getParameter("allow_until_date");
		if (allowUntilParam != null && allowUntilParam.trim().length() > 0)
		{
			Date allowUntilDate;
			try
			{
				allowUntilDate = DateUtil.getDateFromString(allowUntilParam.trim());
				
				specialAccess.getAccessDates().setAllowUntilDate(allowUntilDate);
				
				// check with forum dates
				if (forum.getAccessDates().getAllowUntilDate() == null)
				{
					specialAccess.setOverrideAllowUntilDate(true);					
				}
				else if (allowUntilDate.equals(forum.getAccessDates().getAllowUntilDate()))
				{
					specialAccess.setOverrideAllowUntilDate(false);
					specialAccess.getAccessDates().setAllowUntilDate(null);
				}
				else
				{
					specialAccess.setOverrideAllowUntilDate(true);
				}
			} 
			catch (ParseException e)
			{
				throw e;
			}
		}
		else
		{
			specialAccess.getAccessDates().setAllowUntilDate(null);
			if (forum.getAccessDates().getAllowUntilDate() != null)
			{
				specialAccess.setOverrideAllowUntilDate(true);					
			}			
		}

		int userId = this.request.getIntParameter("user_id");
		List<Integer> specialAccessUser = new ArrayList<Integer>();
		specialAccessUser.add(new Integer(userId));
		
		specialAccess.setUserIds(specialAccessUser);
	}
	
	protected void addEditUserTopicSpecialAccess(org.etudes.api.app.jforum.Topic topic, org.etudes.api.app.jforum.SpecialAccess specialAccess) throws ParseException
	{
		// open date
		String startDateParam = this.request.getParameter("start_date");
		if (startDateParam != null && startDateParam.trim().length() > 0)
		{
			Date startDate;
			try
			{			
				startDate = DateUtil.getDateFromString(startDateParam.trim());
				
				specialAccess.getAccessDates().setOpenDate(startDate);
				
				// hide until open
				String hideUntilOpen = this.request.getParameter("hide_until_open");
				
				// check with forum start date
				if (topic.getAccessDates().getOpenDate() == null)
				{
					specialAccess.setOverrideStartDate(true);
					specialAccess.setOverrideHideUntilOpen(true);
					if (hideUntilOpen != null && "1".equals(hideUntilOpen))
					{
						specialAccess.getAccessDates().setHideUntilOpen(true);
					}
					else
					{
						specialAccess.getAccessDates().setHideUntilOpen(false);
					}						
				}
				else
				{
					if (startDate.equals(topic.getAccessDates().getOpenDate()))
					{
						specialAccess.getAccessDates().setOpenDate(null);
						specialAccess.setOverrideStartDate(false);						
					}
					else
					{
						specialAccess.setOverrideStartDate(true);
					}
					
					// hide until open
					if (specialAccess.isOverrideStartDate())
					{
						/*ignore hide until open setting of topic*/
						//specialAccess.setOverrideStartDate(true);
						specialAccess.setOverrideHideUntilOpen(true);
						
						if (hideUntilOpen != null && "1".equals(hideUntilOpen))
						{
							specialAccess.getAccessDates().setHideUntilOpen(true);							
						}
						else
						{
							specialAccess.getAccessDates().setHideUntilOpen(false);
						}
					}
					else
					{
						//consider hide until open setting of topic and override if not selected for special access
						if (topic.getAccessDates().isHideUntilOpen())
						{
							if (hideUntilOpen != null && "1".equals(hideUntilOpen))
							{
								specialAccess.getAccessDates().setHideUntilOpen(false);
								specialAccess.setOverrideHideUntilOpen(false);
							}
							else
							{
								specialAccess.getAccessDates().setHideUntilOpen(false);
								specialAccess.setOverrideHideUntilOpen(true);
							}
						}
						else
						{
							if (hideUntilOpen != null && "1".equals(hideUntilOpen))
							{
								specialAccess.getAccessDates().setHideUntilOpen(true);
								specialAccess.setOverrideHideUntilOpen(true);
							}
							else
							{
								specialAccess.getAccessDates().setHideUntilOpen(false);
								specialAccess.setOverrideHideUntilOpen(false);
							}								
						}
					}
				}
			
			} 
			catch (ParseException e)
			{
				throw e;
			}
		}
		else
		{
			specialAccess.getAccessDates().setOpenDate(null);
			if (topic.getAccessDates().getOpenDate() != null)
			{
				specialAccess.setOverrideStartDate(true);					
			}			
		}
		
		// due date
		String endDateParam = this.request.getParameter("end_date");
		if (endDateParam != null && endDateParam.trim().length() > 0)
		{
			Date endDate;
			try
			{
				endDate = DateUtil.getDateFromString(endDateParam.trim());
				
				specialAccess.getAccessDates().setDueDate(endDate);
				
				// check with topic dates
				if (topic.getAccessDates().getDueDate() == null)
				{
					specialAccess.setOverrideEndDate(true);
					specialAccess.setOverrideLockEndDate(true);
				}
				else
				{
					if (endDate.equals(topic.getAccessDates().getDueDate()))
					{
						specialAccess.getAccessDates().setDueDate(null);
						specialAccess.setOverrideEndDate(false);						
					}
					else
					{
						specialAccess.setOverrideEndDate(true);
					}
				}
			} 
			catch (ParseException e)
			{
				throw e;
			}
			
		}
		else
		{	/*no special access end date*/
			
			specialAccess.getAccessDates().setDueDate(null);
						
			if (topic.getAccessDates().getDueDate() != null)
			{
				specialAccess.setOverrideEndDate(true);
				specialAccess.setOverrideLockEndDate(true);
			}
			else
			{
				specialAccess.setOverrideEndDate(false);
				specialAccess.setOverrideLockEndDate(false);
			}
		}		
		
		// allow until date
		String allowUntilParam = this.request.getParameter("allow_until_date");
		if (allowUntilParam != null && allowUntilParam.trim().length() > 0)
		{
			Date allowUntilDate;
			try
			{
				allowUntilDate = DateUtil.getDateFromString(allowUntilParam.trim());
				
				specialAccess.getAccessDates().setAllowUntilDate(allowUntilDate);
				
				// check with topic dates
				if (topic.getAccessDates().getAllowUntilDate() == null)
				{
					specialAccess.setOverrideAllowUntilDate(true);					
				}
				else if (allowUntilDate.equals(topic.getAccessDates().getAllowUntilDate()))
				{
					specialAccess.setOverrideAllowUntilDate(false);
					specialAccess.getAccessDates().setAllowUntilDate(null);
				}
				else
				{
					specialAccess.setOverrideAllowUntilDate(true);
				}
			} 
			catch (ParseException e)
			{
				throw e;
			}
		}
		else
		{
			specialAccess.getAccessDates().setAllowUntilDate(null);
			if (topic.getAccessDates().getAllowUntilDate() != null)
			{
				specialAccess.setOverrideAllowUntilDate(true);					
			}			
		}

		int userId = this.request.getIntParameter("user_id");
		List<Integer> specialAccessUser = new ArrayList<Integer>();
		specialAccessUser.add(new Integer(userId));
		
		specialAccess.setUserIds(specialAccessUser);
	}

	/**
	 * Add new special access to forum
	 * @param forum
	 * @throws Exception
	 */
	/*protected void addForumSpecialAccess(Forum forum) throws Exception
	{
		SpecialAccess specialAccess = new SpecialAccess();
		specialAccess.setForumId(forum.getId());
		
		// visible date
		String startDateParam = this.request.getParameter("start_date");
		if (startDateParam != null && startDateParam.trim().length() > 0)
		{
			Date startDate;
			try
			{
				startDate = DateUtil.getDateFromString(startDateParam.trim());
				
				specialAccess.setStartDate(startDate);
				
				SimpleDateFormat df = new SimpleDateFormat(SakaiSystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				specialAccess.setStartDateFormatted(df.format(startDate));
				
				// check with forum dates
				if (forum.getStartDate() == null)
				{
					specialAccess.setOverrideStartDate(true);					
				}
				else if (startDate.equals(forum.getStartDate()))
				{
					specialAccess.setOverrideStartDate(false);
					specialAccess.setStartDate(null);
				}
				else
				{
					specialAccess.setOverrideStartDate(true);
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.insertForum();
				return;
			}
		}
		else
		{
			specialAccess.setStartDate(null);
			if (forum.getStartDate() != null)
			{
				specialAccess.setOverrideStartDate(true);					
			}			
		}
		
		// due date
		String endDateParam = this.request.getParameter("end_date");
		if (endDateParam != null && endDateParam.trim().length() > 0)
		{
			Date endDate;
			try
			{
				endDate = DateUtil.getDateFromString(endDateParam.trim());
				
				specialAccess.setEndDate(endDate);
				
				SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				specialAccess.setEndDateFormatted(df.format(endDate));
				
				// lock on due
				String lockOnDue = this.request.getParameter("lock_on_due");
				
				// check with forum dates
				if (forum.getEndDate() == null)
				{
					specialAccess.setOverrideEndDate(true);
					specialAccess.setOverrideLockEndDate(true);
					if (lockOnDue != null && "1".equals(lockOnDue))
					{
						specialAccess.setLockOnEndDate(true);
					}
					else
					{
						specialAccess.setLockOnEndDate(false);
					}						
				}
				else
				{
					if (endDate.equals(forum.getEndDate()))
					{
						specialAccess.setEndDate(null);
						specialAccess.setOverrideEndDate(false);						
					}
					else
					{
						specialAccess.setOverrideEndDate(true);
					}
					
					// lock on due
					if (specialAccess.isOverrideEndDate())
					{
						ignore lock on due setting of forum
						specialAccess.setOverrideLockEndDate(true);
						
						if (lockOnDue != null && "1".equals(lockOnDue))
						{
							specialAccess.setLockOnEndDate(true);							
						}
						else
						{
							specialAccess.setLockOnEndDate(false);
						}
					}
					else
					{
						consider lock on due setting of forum and override if not selected for special access
						if (forum.isLockForum())
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.setLockOnEndDate(false);
								specialAccess.setOverrideLockEndDate(false);
							}
							else
							{
								specialAccess.setLockOnEndDate(false);
								specialAccess.setOverrideLockEndDate(true);
							}
						}
						else
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.setLockOnEndDate(true);
								specialAccess.setOverrideLockEndDate(true);
							}
							else
							{
								specialAccess.setLockOnEndDate(false);
								specialAccess.setOverrideLockEndDate(false);
							}								
						}
					}
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.insertForum();
				return;
			}
			
		}
		else
		{	no special access end date
			
			specialAccess.setEndDate(null);
						
			if (forum.getEndDate() != null)
			{
				specialAccess.setOverrideEndDate(true);
				specialAccess.setOverrideLockEndDate(true);
				specialAccess.setLockOnEndDate(false);
			}
			else
			{
				specialAccess.setOverrideEndDate(false);
				specialAccess.setOverrideLockEndDate(false);
				specialAccess.setLockOnEndDate(false);
			}
		}
		
		List<Integer> users = new ArrayList<Integer>();
		String userIds[] = (String[])this.request.getObjectParameter("toUsername"+"ParamValues");
		if (userIds != null){
						
			for (int i = 0; i < userIds.length; i++) 
			{
				if (userIds[i] != null && userIds[i].trim().length() > 0) 
				{
					int userId;
					try
					{
						userId = Integer.parseInt(userIds[i]);
						users.add(new Integer(userId));
					} 
					catch (NumberFormatException e)
					{
						if (logger.isWarnEnabled()) logger.warn(this.getClass().getName()+".addForumSpecialAccess() : Error while parsing the userId.", e);
					}
				}
			}
		}
		else
		{
			try
			{
				int userId = Integer.parseInt(this.request.getParameter("toUsername"));
				users.add(new Integer(userId));
			} 
			catch (NumberFormatException e)
			{
				if (logger.isWarnEnabled()) logger.warn(this.getClass().getName()+".addForumSpecialAccess() : Error while parsing the userId.", e);
			}
		}
		
		specialAccess.setUserIds(users);
		
		// delete any existing special access for the selected users
		List<SpecialAccess> categorySpecialAccessList = DataAccessDriver.getInstance().newSpecialAccessDAO().selectByForumId(forum.getId());
		for (SpecialAccess exiSpecialAccess : categorySpecialAccessList)
		{
			List<Integer>  exisUserIds = exiSpecialAccess.getUserIds();
			if (exisUserIds.removeAll(users))
			{
				if (exisUserIds.size() > 0)
				{	
					exiSpecialAccess.setUserIds(exisUserIds);
					DataAccessDriver.getInstance().newSpecialAccessDAO().update(exiSpecialAccess);
				}
				else
				{
					DataAccessDriver.getInstance().newSpecialAccessDAO().delete(exiSpecialAccess.getId());
				}
					
			}
		}
		if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideLockEndDate())) && (users.size() > 0))
		{
			DataAccessDriver.getInstance().newSpecialAccessDAO().addNew(specialAccess);
		}
	}*/
	
	protected void addForumSpecialAccess(org.etudes.api.app.jforum.Forum forum) throws Exception
	{
		JForumSpecialAccessService jforumSpecialAccessService = (JForumSpecialAccessService) ComponentManager.get("org.etudes.api.app.jforum.JForumSpecialAccessService");
		org.etudes.api.app.jforum.SpecialAccess specialAccess = jforumSpecialAccessService.newSpecialAccess(forum.getId(), 0);
		//specialAccess.setForumId(forum.getId());
		
		/*// open date
		String startDateParam = this.request.getParameter("start_date");
		if (startDateParam != null && startDateParam.trim().length() > 0)
		{
			Date startDate;
			try
			{			
				startDate = DateUtil.getDateFromString(startDateParam.trim());
				
				specialAccess.getAccessDates().setOpenDate(startDate);
				
				// hide until open
				String hideUntilOpen = this.request.getParameter("hide_until_open");
				
				// check with forum start date
				if (forum.getAccessDates().getOpenDate() == null)
				{
					specialAccess.setOverrideStartDate(true);
					specialAccess.setOverrideHideUntilOpen(true);
					if (hideUntilOpen != null && "1".equals(hideUntilOpen))
					{
						specialAccess.getAccessDates().setHideUntilOpen(true);
					}
					else
					{
						specialAccess.getAccessDates().setHideUntilOpen(false);
					}						
				}
				else
				{
					if (startDate.equals(forum.getAccessDates().getOpenDate()))
					{
						specialAccess.getAccessDates().setOpenDate(null);
						specialAccess.setOverrideStartDate(false);						
					}
					else
					{
						specialAccess.setOverrideStartDate(true);
					}
					
					// hide until open
					if (specialAccess.isOverrideStartDate())
					{
						ignore hide until open setting of forum
						specialAccess.setOverrideStartDate(true);
						
						if (hideUntilOpen != null && "1".equals(hideUntilOpen))
						{
							specialAccess.getAccessDates().setHideUntilOpen(true);							
						}
						else
						{
							specialAccess.getAccessDates().setHideUntilOpen(false);
						}
					}
					else
					{
						//consider hide until open setting of forum and override if not selected for special access
						if (forum.getAccessDates().isHideUntilOpen())
						{
							if (hideUntilOpen != null && "1".equals(hideUntilOpen))
							{
								specialAccess.getAccessDates().setHideUntilOpen(false);
								specialAccess.setOverrideHideUntilOpen(false);
							}
							else
							{
								specialAccess.getAccessDates().setHideUntilOpen(false);
								specialAccess.setOverrideHideUntilOpen(true);
							}
						}
						else
						{
							if (hideUntilOpen != null && "1".equals(hideUntilOpen))
							{
								specialAccess.getAccessDates().setHideUntilOpen(true);
								specialAccess.setOverrideHideUntilOpen(true);
							}
							else
							{
								specialAccess.getAccessDates().setHideUntilOpen(false);
								specialAccess.setOverrideHideUntilOpen(false);
							}								
						}
					}
				}
			
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.insertForum();
				return;
			}
		}
		else
		{
			specialAccess.getAccessDates().setOpenDate(null);
			if (forum.getAccessDates().getOpenDate() != null)
			{
				specialAccess.setOverrideStartDate(true);					
			}			
		}
		
		// due date
		String endDateParam = this.request.getParameter("end_date");
		if (endDateParam != null && endDateParam.trim().length() > 0)
		{
			Date endDate;
			try
			{
				endDate = DateUtil.getDateFromString(endDateParam.trim());
				
				specialAccess.getAccessDates().setDueDate(endDate);
				
				//SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				//specialAccess.setEndDateFormatted(df.format(endDate));
				
				// lock on due
				//String lockOnDue = this.request.getParameter("lock_on_due");
				
				// check with forum dates
				if (forum.getAccessDates().getDueDate() == null)
				{
					specialAccess.setOverrideEndDate(true);
					specialAccess.setOverrideLockEndDate(true);
					if (lockOnDue != null && "1".equals(lockOnDue))
					{
						specialAccess.getAccessDates().setLocked(true);
					}
					else
					{
						specialAccess.getAccessDates().setLocked(false);
					}						
				}
				else
				{
					if (endDate.equals(forum.getAccessDates().getDueDate()))
					{
						specialAccess.getAccessDates().setDueDate(null);
						specialAccess.setOverrideEndDate(false);						
					}
					else
					{
						specialAccess.setOverrideEndDate(true);
					}
					
					// lock on due
					if (specialAccess.isOverrideEndDate())
					{
						ignore lock on due setting of forum
						specialAccess.setOverrideLockEndDate(true);
						
						if (lockOnDue != null && "1".equals(lockOnDue))
						{
							specialAccess.getAccessDates().setLocked(true);							
						}
						else
						{
							specialAccess.getAccessDates().setLocked(false);
						}
					}
					else
					{
						consider lock on due setting of forum and override if not selected for special access
						if (forum.getAccessDates().isLocked())
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(false);
							}
							else
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(true);
							}
						}
						else
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.getAccessDates().setLocked(true);
								specialAccess.setOverrideLockEndDate(true);
							}
							else
							{
								specialAccess.getAccessDates().setLocked(false);
								specialAccess.setOverrideLockEndDate(false);
							}								
						}
					}
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.insertForum();
				return;
			}
			
		}
		else
		{	no special access end date
			
			specialAccess.getAccessDates().setDueDate(null);
						
			if (forum.getAccessDates().getDueDate() != null)
			{
				specialAccess.setOverrideEndDate(true);
				specialAccess.setOverrideLockEndDate(true);
				//specialAccess.getAccessDates().setLocked(false);
			}
			else
			{
				specialAccess.setOverrideEndDate(false);
				specialAccess.setOverrideLockEndDate(false);
				//specialAccess.getAccessDates().setLocked(false);
			}
		}		
		
		// allow until date
		String allowUntilParam = this.request.getParameter("allow_until");
		if (allowUntilParam != null && allowUntilParam.trim().length() > 0)
		{
			Date allowUntilDate;
			try
			{
				allowUntilDate = DateUtil.getDateFromString(allowUntilParam.trim());
				
				specialAccess.getAccessDates().setAllowUntilDate(allowUntilDate);
				
				// check with forum dates
				if (forum.getAccessDates().getAllowUntilDate() == null)
				{
					specialAccess.setOverrideAllowUntilDate(true);					
				}
				else if (allowUntilDate.equals(forum.getAccessDates().getAllowUntilDate()))
				{
					specialAccess.setOverrideAllowUntilDate(false);
					specialAccess.getAccessDates().setAllowUntilDate(null);
				}
				else
				{
					specialAccess.setOverrideAllowUntilDate(true);
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.insertForum();
				return;
			}
		}
		else
		{
			specialAccess.getAccessDates().setAllowUntilDate(null);
			if (forum.getAccessDates().getAllowUntilDate() != null)
			{
				specialAccess.setOverrideAllowUntilDate(true);					
			}			
		}		
		
		List<Integer> users = new ArrayList<Integer>();
		String userIds[] = (String[])this.request.getObjectParameter("toUsername"+"ParamValues");
		if (userIds != null){
						
			for (int i = 0; i < userIds.length; i++) 
			{
				if (userIds[i] != null && userIds[i].trim().length() > 0) 
				{
					int userId;
					try
					{
						userId = Integer.parseInt(userIds[i]);
						users.add(new Integer(userId));
					} 
					catch (NumberFormatException e)
					{
						if (logger.isWarnEnabled()) logger.warn(this.getClass().getName()+".addForumSpecialAccess() : Error while parsing the userId.", e);
					}
				}
			}
		}
		else
		{
			try
			{
				int userId = Integer.parseInt(this.request.getParameter("toUsername"));
				users.add(new Integer(userId));
			} 
			catch (NumberFormatException e)
			{
				if (logger.isWarnEnabled()) logger.warn(this.getClass().getName()+".addForumSpecialAccess() : Error while parsing the userId.", e);
			}
		}
		
		specialAccess.setUserIds(users);*/
		
		try
		{
			addEditSpecialAccess(forum, specialAccess);
		}
		catch (ParseException e)
		{
			this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
			this.insertForum();
			return;
		}
		
		jforumSpecialAccessService.createForumSpecialAccess(specialAccess);
		// delete any existing special access for the selected users
		/*List<SpecialAccess> forumSpecialAccessList = DataAccessDriver.getInstance().newSpecialAccessDAO().selectByForumId(forum.getId());
		for (SpecialAccess exiSpecialAccess : forumSpecialAccessList)
		{
			List<Integer>  exisUserIds = exiSpecialAccess.getUserIds();
			if (exisUserIds.removeAll(users))
			{
				if (exisUserIds.size() > 0)
				{	
					exiSpecialAccess.setUserIds(exisUserIds);
					DataAccessDriver.getInstance().newSpecialAccessDAO().update(exiSpecialAccess);
				}
				else
				{
					DataAccessDriver.getInstance().newSpecialAccessDAO().delete(exiSpecialAccess.getId());
				}
					
			}
		}
		if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideLockEndDate())) && (users.size() > 0))
		{
			DataAccessDriver.getInstance().newSpecialAccessDAO().addNew(specialAccess);
		}*/
	}
	
	/**
	 * Add new special access to forum with group
	 * @param forum
	 * @throws Exception
	 */
	protected void addForumGroupSpecialAccess(Forum forum) throws Exception
	{
		SpecialAccess specialAccess = new SpecialAccess();
		specialAccess.setForumId(forum.getId());
		
		String groupId = this.request.getParameter("group_id");
		
		if ((groupId == null) || (groupId.trim().length() == 0))
		{
			this.showForumList();
			return;
		}
		
		// start/visible date
		String startDateParam = this.request.getParameter("startdate_"+ groupId);
		if (startDateParam != null && startDateParam.trim().length() > 0)
		{
			Date startDate;
			try
			{
				startDate = DateUtil.getDateFromString(startDateParam.trim());

				specialAccess.setStartDate(startDate);

				SimpleDateFormat df = new SimpleDateFormat(SakaiSystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				specialAccess.setStartDateFormatted(df.format(startDate));

				// check with forum dates
				if (forum.getStartDate() == null)
				{
					specialAccess.setOverrideStartDate(true);					
				}
				else if (startDate.equals(forum.getStartDate()))
				{
					specialAccess.setOverrideStartDate(false);
					specialAccess.setStartDate(null);
				}
				else
				{
					specialAccess.setOverrideStartDate(true);
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.insertForum();
				return;
			}
		}
		else
		{
			specialAccess.setStartDate(null);
			if (forum.getStartDate() != null)
			{
				specialAccess.setOverrideStartDate(true);					
			}			
		}
		
		// due date
		String endDateParam = this.request.getParameter("enddate_"+ groupId);
		if (endDateParam != null && endDateParam.trim().length() > 0)
		{
			Date endDate;
			try
			{
				endDate = DateUtil.getDateFromString(endDateParam.trim());

				specialAccess.setEndDate(endDate);

				SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				specialAccess.setEndDateFormatted(df.format(endDate));

				// lock on due
				String lockOnDue = this.request.getParameter("lock_on_due");

				// check with forum dates
				if (forum.getEndDate() == null)
				{
					specialAccess.setOverrideEndDate(true);
					specialAccess.setOverrideLockEndDate(true);
					if (lockOnDue != null && "1".equals(lockOnDue))
					{
						specialAccess.setLockOnEndDate(true);
					}
					else
					{
						specialAccess.setLockOnEndDate(false);
					}						
				}
				else
				{
					if (endDate.equals(forum.getEndDate()))
					{
						specialAccess.setEndDate(null);
						specialAccess.setOverrideEndDate(false);						
					}
					else
					{
						specialAccess.setOverrideEndDate(true);
					}

					// lock on due
					if (specialAccess.isOverrideEndDate())
					{
						/*ignore lock on due setting of forum*/
						specialAccess.setOverrideLockEndDate(true);

						if (lockOnDue != null && "1".equals(lockOnDue))
						{
							specialAccess.setLockOnEndDate(true);							
						}
						else
						{
							specialAccess.setLockOnEndDate(false);
						}
					}
					else
					{
						/*consider lock on due setting of forum and override if not selected for special access*/
						if (forum.isLockForum())
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.setLockOnEndDate(false);
								specialAccess.setOverrideLockEndDate(false);
							}
							else
							{
								specialAccess.setLockOnEndDate(false);
								specialAccess.setOverrideLockEndDate(true);
							}
						}
						else
						{
							if (lockOnDue != null && "1".equals(lockOnDue))
							{
								specialAccess.setLockOnEndDate(true);
								specialAccess.setOverrideLockEndDate(true);
							}
							else
							{
								specialAccess.setLockOnEndDate(false);
								specialAccess.setOverrideLockEndDate(false);
							}								
						}
					}
				}
			} 
			catch (ParseException e)
			{
				this.context.put("errorMessage", I18n.getMessage("Forums.Forum.DateParseError"));
				this.insertForum();
				return;
			}

		}
		else
		{	/*no special access end date*/
			
			specialAccess.setEndDate(null);

			if (forum.getEndDate() != null)
			{
				specialAccess.setOverrideEndDate(true);
				specialAccess.setOverrideLockEndDate(true);
				specialAccess.setLockOnEndDate(false);
			}
			else
			{
				specialAccess.setOverrideEndDate(false);
				specialAccess.setOverrideLockEndDate(false);
				specialAccess.setLockOnEndDate(false);
			}
		}
		List<Integer> users = new ArrayList<Integer>();
		String userIds[] = (String[])this.request.getObjectParameter("toUsername"+"ParamValues");
		if (userIds != null){
						
			for (int i = 0; i < userIds.length; i++) 
			{
				if (userIds[i] != null && userIds[i].trim().length() > 0) 
				{
					int userId;
					try
					{
						userId = Integer.parseInt(userIds[i]);
						users.add(new Integer(userId));
					} 
					catch (NumberFormatException e)
					{
						if (logger.isWarnEnabled()) logger.warn(this.getClass().getName()+".addForumGroupSpecialAccess() : Error while parsing the userId.", e);
					}
				}
			}
		}
		else
		{
			try
			{
				int userId = Integer.parseInt(this.request.getParameter("toUsername"));
				users.add(new Integer(userId));
			} 
			catch (NumberFormatException e)
			{
				if (logger.isWarnEnabled()) logger.warn(this.getClass().getName()+".addForumGroupSpecialAccess() : Error while parsing the userId.", e);
			}
		}
		
		specialAccess.setUserIds(users);
		
		// delete any existing special access for the selected users
		List<SpecialAccess> categorySpecialAccessList = DataAccessDriver.getInstance().newSpecialAccessDAO().selectByForumId(forum.getId());
		for (SpecialAccess exiSpecialAccess : categorySpecialAccessList)
		{
			List<Integer>  exisUserIds = exiSpecialAccess.getUserIds();
			if (exisUserIds.removeAll(users))
			{
				if (exisUserIds.size() > 0)
				{	
					exiSpecialAccess.setUserIds(exisUserIds);
					DataAccessDriver.getInstance().newSpecialAccessDAO().update(exiSpecialAccess);
				}
				else
				{
					DataAccessDriver.getInstance().newSpecialAccessDAO().delete(exiSpecialAccess.getId());
				}
					
			}
		}
		if (((specialAccess.isOverrideStartDate()) || (specialAccess.isOverrideEndDate()) || (specialAccess.isOverrideLockEndDate())) && (users.size() > 0))
		{
			DataAccessDriver.getInstance().newSpecialAccessDAO().addNew(specialAccess);
		}
	}
	
}
