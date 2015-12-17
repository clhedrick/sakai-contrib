/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/view/forum/SearchAction.java $ 
 * $Id: SearchAction.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.etudes.api.app.jforum.JForumCategoryService;
import org.etudes.jforum.ActionServletRequest;
import org.etudes.jforum.Command;
import org.etudes.jforum.JForum;
import org.etudes.jforum.dao.DataAccessDriver;
import org.etudes.jforum.dao.SearchDAO;
import org.etudes.jforum.dao.SearchData;
import org.etudes.jforum.entities.Category;
import org.etudes.jforum.entities.Forum;
import org.etudes.jforum.entities.Topic;
import org.etudes.jforum.entities.User;
import org.etudes.jforum.repository.ForumRepository;
import org.etudes.jforum.util.preferences.ConfigKeys;
import org.etudes.jforum.util.preferences.SakaiSystemGlobals;
import org.etudes.jforum.util.preferences.SystemGlobals;
import org.etudes.jforum.util.preferences.TemplateKeys;
import org.etudes.jforum.util.user.JForumUserUtil;
import org.etudes.jforum.view.forum.common.ForumCommon;
import org.etudes.jforum.view.forum.common.TopicsCommon;
import org.etudes.jforum.view.forum.common.ViewCommon;
import org.sakaiproject.authz.cover.SecurityService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.user.cover.UserDirectoryService;

import freemarker.template.SimpleHash;

/**
 * @author Rafael Steil
 */
public class SearchAction extends Command 
{
	private String searchTerms;
	private String forum;
	private String category;
	private String sortBy;
	private String sortDir;
	private String kw;
	private String author;
	private String postTime;
	
	private static Map fieldsMap = new HashMap();
	private static Map sortByMap = new HashMap();
	
	static {
		fieldsMap.put("search_terms", "searchTerms");
		fieldsMap.put("search_forum", "forum");
		fieldsMap.put("search_cat", "catgory");
		fieldsMap.put("sort_by", "sortBy");
		fieldsMap.put("sort_dir", "sortDir");
		fieldsMap.put("search_keywords", "kw");
		fieldsMap.put("search_author", "author");
		fieldsMap.put("post_time", "postTime");
		
		sortByMap.put("time", "p.post_time");
		sortByMap.put("title", "t.topic_title");
		sortByMap.put("username", "u.username");
		sortByMap.put("forum", "t.forum_id");
	}
	
	public SearchAction() {}
	
	public SearchAction(ActionServletRequest request, HttpServletResponse response,
			SimpleHash context) {
		this.request = request;
		this.response = response;
		this.context = context;
	}
	
	public void filters() throws Exception
	{
		this.setTemplateName(TemplateKeys.SEARCH_FILTERS);
		//this.context.put("categories", ForumRepository.getAllCategories());
		//this.context.put("categories", ForumRepository.getUserAllCourseCategories());
		//this.context.put("categories", ForumCommon.getAllCategoriesAndForums(false));
		JForumCategoryService jforumCategoryService = (JForumCategoryService) ComponentManager.get("org.etudes.api.app.jforum.JForumCategoryService");
		List<org.etudes.api.app.jforum.Category> categories = jforumCategoryService.getUserContextCategories(ToolManager.getCurrentPlacement().getContext(), UserDirectoryService.getCurrentUser().getId());
		this.context.put("categories", categories);
	}
	
	private void getSearchFields()
	{
		this.searchTerms = this.addSlashes(this.request.getParameter("search_terms"));
		this.forum = this.addSlashes(this.request.getParameter("search_forum"));
		this.category = this.addSlashes(this.request.getParameter("search_cat"));
		
		this.sortBy = (String)sortByMap.get(this.addSlashes(this.request.getParameter("sort_by")));
		
		if (this.sortBy == null) {
			this.sortBy = (String)sortByMap.get("time");
		}
		
		this.sortDir = this.addSlashes(this.request.getParameter("sort_dir"));
		
		if (!"ASC".equals(this.sortDir) && !"DESC".equals(this.sortDir)) {
			this.sortDir = "DESC";
		}
		
		this.kw = this.addSlashes(this.request.getParameter("search_keywords"));
		this.author = this.addSlashes(this.request.getParameter("search_author"));
		this.postTime = this.addSlashes(this.request.getParameter("post_time"));
	}
	
	public void search() throws Exception
	{
		this.getSearchFields();
		
		SearchData sd = new SearchData();
		sd.setKeywords(kw);
		sd.setAuthor(author);
		sd.setOrderByField(sortBy);
		sd.setOrderBy(sortDir);
		
		if (postTime != null) {
			sd.setTime(new Date(Long.parseLong(postTime)));		    
		}
		
		if (searchTerms != null) {
			sd.setUseAllWords(searchTerms.equals("any") ? false : true);
		}
		
		if (forum != null && !forum.equals("")) {
			sd.setForumId(Integer.parseInt(forum));
		}
		
		if (category != null && !category.equals("")) {
			sd.setCategoryId(Integer.parseInt(category));
		}
		
		int start = ViewCommon.getStartPage();
		// int recordsPerPage = SystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE);
		int recordsPerPage = SakaiSystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE);
		
		SearchDAO sm = DataAccessDriver.getInstance().newSearchDAO();

		// Clean the search
		if (this.request.getParameter("clean") != null) {
			sm.cleanSearch();
		}
		else {
			sd.setSearchStarted(true);
		}
		
		List allTopics = this.onlyAllowedData(sm.search(sd));
		int totalTopics = allTopics.size();
		int sublistLimit = (recordsPerPage + start) > totalTopics ? totalTopics : recordsPerPage + start;
		
		this.setTemplateName(TemplateKeys.SEARCH_SEARCH);
		
		this.context.put("fr", new ForumRepository());
		
		this.context.put("topics", TopicsCommon.prepareTopics(allTopics.subList(start, sublistLimit)));
		//this.context.put("categories", ForumRepository.getAllCategories());
		//this.context.put("categories", ForumRepository.getUserAllCourseCategories());
		//this.context.put("categories", ForumCommon.getAllCategoriesAndForums(false));
		JForumCategoryService jforumCategoryService = (JForumCategoryService) ComponentManager.get("org.etudes.api.app.jforum.JForumCategoryService");
		List<org.etudes.api.app.jforum.Category> categories = jforumCategoryService.getUserContextCategories(ToolManager.getCurrentPlacement().getContext(), UserDirectoryService.getCurrentUser().getId());
		
		Date nowDate = new Date();
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		boolean participant = false;
		
		if (!isfacilitator)
		{
			participant = JForumUserUtil.isJForumParticipant(UserDirectoryService.getCurrentUser().getId());
		}
		
		// filter user categories and forums in the drop down
		if (!isfacilitator)
		{
			org.etudes.api.app.jforum.Category userCategory = null;
			for (Iterator<org.etudes.api.app.jforum.Category> catIter = categories.iterator(); catIter.hasNext(); ) 
			{
				userCategory = catIter.next();
				
				if (userCategory.getAccessDates() != null && userCategory.getAccessDates().getOpenDate() != null)
				{
					if (userCategory.getAccessDates().getOpenDate().before(nowDate))
					{
						catIter.remove();
						continue;
					}
				}
				
				org.etudes.api.app.jforum.Forum userForum = null;
				for (Iterator<org.etudes.api.app.jforum.Forum> forumIter = userCategory.getForums().iterator(); forumIter.hasNext(); ) 
				{
					userForum = forumIter.next();
					List<org.etudes.api.app.jforum.SpecialAccess> specialAccessList = userForum.getSpecialAccess();
					
					if (!specialAccessList.isEmpty() && specialAccessList.size() == 1)
					{
						org.etudes.api.app.jforum.SpecialAccess specialAccess = specialAccessList.get(0);
						
						if ((specialAccess.getAccessDates().getOpenDate() != null) && (specialAccess.getAccessDates().getOpenDate().after(nowDate)))
						{
							forumIter.remove();
						}
					}
					else
					{
						if ((userForum.getAccessDates().getOpenDate() != null) && (userForum.getAccessDates().getOpenDate().after(nowDate)))
						{
							forumIter.remove();
							continue;
						}
					}
				}
			}
		}
		this.context.put("categories", categories);
		
		this.context.put("kw", kw);
		this.context.put("terms", searchTerms);
		this.context.put("forum", forum);
		this.context.put("category", category);
		this.context.put("orderField", sortBy);
		this.context.put("orderBy", sortDir);
		this.context.put("author", author);
		this.context.put("postTime", postTime);
		//this.context.put("openModeration", "1".equals(this.request.getParameter("openModeration")));
		this.context.put("openModeration", true);
		// this.context.put("postsPerPage", new Integer(SystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE)));
		this.context.put("postsPerPage", new Integer(SakaiSystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE)));
		GregorianCalendar gc = new GregorianCalendar();
		this.context.put("nowDate", gc.getTime());
		
		ViewCommon.contextToPagination(start, totalTopics, recordsPerPage);
		
		TopicsCommon.topicListingBase();
	}
	
	private List onlyAllowedData(List topics) throws Exception
	{
		List l = new ArrayList();
		HashMap<Integer, Forum> forums = new HashMap<Integer, Forum>();
		HashMap<Integer, Category> categories = new HashMap<Integer, Category>();
		
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		this.context.put("facilitator", isfacilitator);
		
		for (Iterator iter = topics.iterator(); iter.hasNext(); ) {
			Topic t = (Topic)iter.next();
			
			// forums in search
			Forum forum = null;
			if (!forums.containsKey(new Integer(t.getForumId())))
			{
				forum = ForumRepository.getForum(t.getForumId());
				forums.put(new Integer(t.getForumId()), forum);
			}
			else
			{
				forum = forums.get(new Integer(t.getForumId()));
			}
			
			// categories in search
			Category category = null;
			if (!categories.containsKey(new Integer(forum.getCategoryId())))
			{
				category = ForumRepository.getCategory(forum.getCategoryId());
				categories.put(new Integer(forum.getCategoryId()), category);
			}
			else
			{
				category = categories.get(new Integer(forum.getCategoryId()));
			}			
			
			String sakaiUserId = UserDirectoryService.getCurrentUser().getId();
			User user = DataAccessDriver.getInstance().newUserDAO().selectBySakaiUserId(sakaiUserId);
			
			// check user access permissions to the category, forum, and topics
			if (forum != null && (ForumRepository.isCategoryAccessible(category, user.getId()) 
						&& ForumRepository.isForumAccessibleToUser(forum)))
			{
				if (t != null && ForumRepository.isTopicAccessible(t))
				{
					l.add(t);
				}
			}
		}
		
		return l;
	}
	
	public void doModeration() throws Exception
	{
		int status = new ModerationHelper().doModeration(this.makeRedirect());
		
		String actionMode = JForum.getRequest().getParameter("actionMode");
		//if (JForum.getRequest().getParameter("topicMove") != null) {
		if ((actionMode != null) && (actionMode.trim().equalsIgnoreCase("topicMove")))
		{
			this.setTemplateName(TemplateKeys.MODERATION_MOVE_TOPICS);
		}
		
		if (status == ModerationHelper.GRADED) 
		{
			this.search();
		}
	}
	
	public void moveTopic() throws Exception
	{
		new ModerationHelper().moveTopicsSave(this.makeRedirect());
	}
	
	public void moderationDone() throws Exception
	{
		this.setTemplateName(new ModerationHelper().moderationDone(this.makeRedirect()));
	}
	
	private String makeRedirect() throws Exception
	{
		String persistData = this.request.getParameter("persistData");
		if (persistData == null) {
			this.getSearchFields();
		}
		else {
			String[] p = persistData.split("&");
			
			for (int i = 0; i < p.length; i++) {
				String[] v = p[i].split("=");
				
				String name = (String)fieldsMap.get(v[0]);
				if (name != null) {
					Field field = this.getClass().getDeclaredField(name);
					if (field != null && v[1] != null && !v[1].equals("")) {
						field.set(this, v[1]);
					}
				}
			}
		}

		StringBuffer path = new StringBuffer(512);
		path.append(this.request.getContextPath()).append("/jforum" 
				+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION)
				+ "?module=search&action=search&clean=1");
		
		if (this.forum != null) { 
			path.append("&search_forum=").append(this.forum); 
		}
		
		if (this.searchTerms != null) { 
			path.append("&search_terms=").append(this.searchTerms); 
		}
		
		if (this.category != null) {
			path.append("&search_cat=").append(this.category);
		}
		
		if (this.sortDir != null) {
			path.append("&sort_dir=").append(this.sortDir);
		}
		
		if (this.sortBy != null) {
			path.append("&sort_by=").append(this.sortBy);
		}
		
		if (this.kw != null) {
			path.append("&search_keywords=").append(this.kw);
		}
		
		if (this.postTime != null) {
			path.append("&post_time=").append(this.postTime);
		}
		
		if (ViewCommon.getStartPage() > 0)
			path.append("&start=").append(ViewCommon.getStartPage() - 1);
		else
			path.append("&start=").append(ViewCommon.getStartPage());
		
		return path.toString();
	}
	
	private String addSlashes(String s)
	{
		if (s != null) {
			s = s.replaceAll("'", "\\'");
			s = s.replaceAll("\"", "\\\"");
		}
		
		return s;
	}
	
	/** 
	 * @see org.etudes.jforum.Command#list()
	 */
	public void list() throws Exception 
	{
		this.filters();
	}
}
