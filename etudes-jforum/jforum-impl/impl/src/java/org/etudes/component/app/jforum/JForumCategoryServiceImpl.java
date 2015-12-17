/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumCategoryServiceImpl.java $ 
 * $Id: JForumCategoryServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.AccessDates;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Evaluation;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.JForumAccessException;
import org.etudes.api.app.jforum.JForumCategoryForumsExistingException;
import org.etudes.api.app.jforum.JForumCategoryService;
import org.etudes.api.app.jforum.JForumForumService;
import org.etudes.api.app.jforum.JForumGradeService;
import org.etudes.api.app.jforum.JForumGradesModificationException;
import org.etudes.api.app.jforum.JForumItemEvaluatedException;
import org.etudes.api.app.jforum.JForumPostService;
import org.etudes.api.app.jforum.JForumSecurityService;
import org.etudes.api.app.jforum.JForumService;
import org.etudes.api.app.jforum.JForumService.Type;
import org.etudes.api.app.jforum.JForumSpecialAccessService;
import org.etudes.api.app.jforum.JForumUserService;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.Topic;
import org.etudes.api.app.jforum.User;
import org.etudes.api.app.jforum.dao.CategoryDao;
import org.etudes.api.app.jforum.dao.UserAlreadyExistException;
import org.etudes.api.app.jforum.dao.UserAlreadyInSiteUsersException;
import org.etudes.util.api.AccessAdvisor;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.thread_local.api.ThreadLocalManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;

public class JForumCategoryServiceImpl implements JForumCategoryService
{
	private static Log logger = LogFactory.getLog(JForumCategoryServiceImpl.class);
	
	/** Dependency (optional, self-injected): AccessAdvisor. */
	protected transient AccessAdvisor accessAdvisor = null;
	
	/** Dependency: CategoryDao. */
	protected CategoryDao categoryDao = null;
	
	/** Dependency: JForumForumService */
	protected JForumForumService jforumForumService = null;
	
	/** Dependency: JForumGradeService */
	protected JForumGradeService jforumGradeService = null;
	
	/** Dependency: JForumPostService */
	protected JForumPostService jforumPostService = null;
	
	/** Dependency: JForumSecurityService. */
	protected JForumSecurityService jforumSecurityService = null;
	
	/** Dependency: JForumSpecialAccessService */
	protected JForumSpecialAccessService jforumSpecialAccessService = null;
	
	/** Dependency: JForumUserService. */
	protected JForumUserService jforumUserService = null;
	
	/** Dependency: SiteService. */
	protected SiteService siteService = null;
	
	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	/** Dependency: ThreadLocalManager. */
	protected ThreadLocalManager threadLocalManager = null;
	
	/** Dependency: UserDirectoryService. */
	protected UserDirectoryService userDirectoryService = null;
	
	/**
	 * {@inheritDoc}
	 */
	public void checkAndAddContextDefaultCategoriesForums(String context, String sakaiUserId) throws JForumAccessException
	{
		if (context == null || context.trim().length() == 0) 
		{
			throw new IllegalArgumentException("context iformation is missing.");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0) 
		{
			throw new IllegalArgumentException("user iformation is missing.");
		}
		
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
		
		// create default categories and forums if site in not initialized
		categoryDao.checkAndAddDefaultCategoriesForums(context);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int createCategory(Category category) throws JForumAccessException
	{
		if ((category == null))
		{
			throw new IllegalArgumentException();
		}
		
		if ((category.getTitle() == null) || (category.getTitle().trim().length() == 0))
		{
			throw new IllegalArgumentException("Category title is missing");
		}
		
		if (category.getContext() == null)
		{
			throw new IllegalArgumentException("Category context is missing");
		}
		
		if (category.getCreatedBySakaiUserId() == null)
		{
			throw new IllegalArgumentException("Created by user information is missing");
		}
	
		// access check
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), category.getCreatedBySakaiUserId());
		
		if (!facilitator)
		{
			throw new JForumAccessException(category.getCreatedBySakaiUserId());
		}
		
		if (category.isGradable())
		{
			if ((category.getGrade() == null) || (category.getGrade().getPoints() == null))
			{
				category.setGradable(Boolean.FALSE);
			}
		}
		
		int categoryId = categoryDao.addNew(category);
		
		Grade grade = category.getGrade();
		
		// add to gradebook
		if (grade != null && grade.isAddToGradeBook())
		{
			jforumGradeService.updateGradebook(grade, category);
		}
		
		return categoryId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteCategory(int categoryId, String sakaiUserId) throws JForumAccessException, JForumCategoryForumsExistingException, JForumItemEvaluatedException
	{
		Category category = getCategory(categoryId);
		
		if (category == null)
		{
			return;
		}
		
		// check user access
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), sakaiUserId);
		
		if (!facilitator)
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		// category with forums cannot be deleted
		int forumsCount = categoryDao.selectCategoryForumsCount(categoryId);
		
		if (forumsCount > 0)
		{
			throw new JForumCategoryForumsExistingException(category.getTitle());
		}
		
		// gradable category if have evaluations cannot be deleted
		if (category.isGradable())
		{
			int evalCount = jforumGradeService.getCategoryEvaluationsCount(categoryId);
			
			if (evalCount > 0)
			{
				throw new JForumItemEvaluatedException(category.getTitle());
			}		
		}
		
		// delete category
		categoryDao.delete(category.getId());
		
		// gradable category remove entry in the gradebook
		if (category.isGradable())
		{
			Grade grade = category.getGrade();
			
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
	public void evaluateCategory(Category category) throws JForumAccessException
	{
		if (category == null || !category.isGradable())
		{
			return;
		}
		//Grade grade = jforumGradeService.getByCategoryId(category.getId());
		Grade grade = category.getGrade();
		
		if (grade == null)
		{
			return;
		}
		
		jforumGradeService.addModifyCategoryEvaluations(grade, category.getEvaluations());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getCategory(int categoryId)
	{
		if (categoryId == 0) throw new IllegalArgumentException();
		
		// for thread-local caching
		String key = cacheKey(String.valueOf(categoryId));
		CategoryImpl cachedCategory = (CategoryImpl) this.threadLocalManager.get(key);
		if (cachedCategory != null)
		{
			return this.clone(cachedCategory);
		}
		
		Category category = categoryDao.selectById(categoryId);
		
		// thread-local cache (a copy)
		if (category != null) this.threadLocalManager.set(key, this.clone((CategoryImpl)category));
		
		return category;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getCategory(int categoryId, String sakaiUserId) throws JForumAccessException
	{
		if (categoryId == 0) 
		{
			throw new IllegalArgumentException();
		}
		
		if (sakaiUserId ==  null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakai user id is needed.");
		}
		
		Category category = categoryDao.selectById(categoryId);
		
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
		
		((CategoryImpl)category).setCurrentSakaiUserId(sakaiUserId);
		
		if (participant)
		{
			checkUserCategoryAccess(category, sakaiUserId);
		}
		
		return category;
	}

	/**
	 * {@inheritDoc}
	 */
	public Category getCategoryEvaluations(String context, int categoryId)
	{
		if (context == null) throw new IllegalArgumentException();
		
		Category category = getCategory(categoryId);
		
		if (category != null)
		{
			List<Evaluation> evaluations = this.jforumGradeService.getCategoryEvaluations(categoryId);
			category.getEvaluations().addAll(evaluations);
		}
		
		return category;
	}

	/**
	 * {@inheritDoc}
	 */
	public Category getCategoryForum(int forumId)
	{
		Forum forum = this.jforumForumService.getForum(forumId);
		Category category = getCategory(forum.getCategoryId());
		category.getForums().add(forum);
		
		return category;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getCategoryForumTopic(int topicId)
	{
		Topic topic = this.jforumPostService.getTopic(topicId);
		Forum forum = this.jforumForumService.getForum(topic.getForumId());
		forum.getTopics().add(topic);
		Category category = getCategory(forum.getCategoryId());
		category.getForums().add(forum);
		
		return category;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	/*public List<Category> getContextCategories(String context)
	{
		if (context == null) throw new IllegalArgumentException();
		
		if (logger.isDebugEnabled()) 
		{
			logger.debug("getContextCategories - context:"+ context);
		}
		
		List<Category> categories = categoryDao.selectAllByCourse(context);
		
		return categories;
	}*/
	
	/**
	 * {@inheritDoc}
	 */
	public Category getCategoryUsersPostCount(String context, int categoryId)
	{
		StringBuilder sql;
		Object[] fields;
		int i;
			
		// category and users post count
		sql = new StringBuilder();
		
		//sql.append("SELECT cc.course_id, c.categories_id, c.title AS cat_title, c.gradable AS cat_gradable, c.start_date AS cat_start_date, c.end_date AS cat_end_date, c.lock_end_date AS cat_lock_end_date, s.user_id, COUNT(p.post_id) AS user_posts_count, c.display_order, ");
		sql.append("SELECT cc.course_id, c.categories_id, c.title AS cat_title, c.gradable AS cat_gradable, c.start_date AS cat_start_date, c.hide_until_open AS cat_hide_until_open, c.end_date AS cat_end_date, c.allow_until_date AS cat_allow_until_date, s.user_id, COUNT(p.post_id) AS user_posts_count, c.display_order, ");
		sql.append("u.user_fname AS user_fname,u.user_lname, u.sakai_user_id, u.username ");
		sql.append("FROM  jforum_site_users s, jforum_users u, jforum_posts p, jforum_topics t, ");
		sql.append("jforum_forums f, jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.sakai_site_id = ? AND s.user_id = u.user_id AND s.user_id = p.user_id ");
		sql.append("AND p.topic_id = t.topic_id ");
		sql.append("AND p.forum_id = f.forum_id AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = cc.categories_id ");
		//sql.append("AND cc.course_id = ? AND c.gradable = 1 ");
		sql.append("AND cc.course_id = ? ");
		sql.append("AND c.categories_id = ? ");
		//sql.append("GROUP BY cc.course_id, c.categories_id, c.title, c.gradable, c.start_date, c.end_date, c.lock_end_date, s.user_id, c.display_order, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
		sql.append("GROUP BY cc.course_id, c.categories_id, c.title, c.gradable, c.start_date, c.hide_until_open, c.end_date, c.allow_until_date, s.user_id, c.display_order, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
		sql.append("ORDER BY c.display_order");
		
		fields = new Object[3];
		i = 0;
		fields[i++] = context;
		fields[i++] = context;
		fields[i++] = categoryId;
		
		List<Category> categories = readCategoriesUserPostCount(sql, fields);
		
		Category category = null;		
		
		if (categories.size() == 1)
		{
			category = categories.get(0);
		}
		
		if (category == null)
		{
			category = getCategory(categoryId);
		}
		
		return category;	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Category> getContextGradableCategoriesForumsTopics(String context)
	{
		if (context == null) throw new IllegalArgumentException();
		
		if (logger.isDebugEnabled()) 
		{
			logger.debug("getContextGradableCategoriesForumsTopics - context:"+ context);
		}
		
		List<Category> categories = getContextGradableCategories(context);
		
		categories.addAll(getContextGradableForums(context));
		categories.addAll(getContextGradableTopics(context));
		
		// sort
		applySort(categories);
		
		return categories;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Category> getContextGradableCategoriesForumsTopicsUsersPostCount(String context)
	{
		if (context == null) throw new IllegalArgumentException();
		
		if (logger.isDebugEnabled()) 
		{
			logger.debug("getContextGradableCategoriesForumsTopicsUsersPostCount - context:"+ context);
		}
		
		List<Category> categories = new ArrayList<Category>();
		
		// categories
		List<Category> gradableUserPostCategories = getContextGradableCategoriesUsersPostCount(context);
		if (gradableUserPostCategories.size() == 0)
		{
			categories.addAll(getContextGradableCategories(context));
		}
		else
		{
			List<Category> gradableContextCategories = getContextGradableCategories(context);
			
			Map<Integer, Category> catMap =  new HashMap<Integer, Category>();
			for (Category category : gradableUserPostCategories)
			{
				catMap.put(category.getId(), category);
			}
			
			categories.addAll(gradableUserPostCategories);
			
			for (Category category : gradableContextCategories)
			{
				if (!catMap.containsKey(category.getId()))
				{
					categories.add(category);
				}
			}
		}
		
		// forums
		List<Category> gradableUserPostCategoryForums = getContextGradableForumUsersPostCount(context);
		if (gradableUserPostCategoryForums.size() == 0)
		{
			categories.addAll(getContextGradableForums(context));
		}
		else
		{
			List<Category> gradableContextCategoryForums = getContextGradableForums(context);
			
			Map<Integer, Category> catMap =  new HashMap<Integer, Category>();
			for (Category category : gradableUserPostCategoryForums)
			{
				catMap.put(category.getId(), category);
			}
			
			categories.addAll(gradableUserPostCategoryForums);
			
			for (Category category : gradableContextCategoryForums)
			{
				if (!catMap.containsKey(category.getId()))
				{
					categories.add(category);
				}
				else
				{
					Category exisCategory = catMap.get(category.getId());
					Map<Integer, Forum> forumMap =  new HashMap<Integer, Forum>();
					
					for(Forum forum : exisCategory.getForums())
					{
						forumMap.put(forum.getId(), forum);
					}
					
					for(Forum forum : category.getForums())
					{
						if (!forumMap.containsKey(forum.getId()))
						{
							exisCategory.getForums().add(forum);
						}
					}
				}
			}
		}		
		
		// topics
		List<Category> gradableUserPostCategoryForumTopics = getContextGradableTopicsUsersPostCount(context);
		Map<Integer, Category> catMap =  new HashMap<Integer, Category>();
		if (gradableUserPostCategoryForumTopics.size() != 0)
		{
			for (Category category : categories)
			{
				catMap.put(category.getId(), category);
			}
			
			for (Category category : gradableUserPostCategoryForumTopics)
			{
				if (catMap.containsKey(category.getId()))
				{
					Category exisCategory = catMap.get(category.getId());
					
					exisCategory.getForums().addAll(category.getForums());
				}
				else
				{
					categories.add(category);
				}
			}
		}
		
		// sort
		applySort(categories);
		
		return categories;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Category> getFilteredContextGradableCategoriesForumsTopics(String context)
	{
		if (context == null) throw new IllegalArgumentException();
		
		if (logger.isDebugEnabled()) 
		{
			logger.debug("getFilteredContextGradableCategoriesForumsTopics - context:"+ context);
		}
		
		List<Category> categories = getContextGradableCategories(context);
		
		categories.addAll(getContextAccessibleGradableForums(context));
		categories.addAll(getContextAccessibleGradableTopics(context));
		
		// sort
		applySort(categories);
		
		return categories;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getForumEvaluations(String context, int forumId)
	{
		if (context == null) throw new IllegalArgumentException();
		Category category = null;
		
		Forum forum = this.jforumForumService.getForum(forumId);
		
		if (forum != null)
		{
			category = getCategory(forum.getCategoryId());
			
			if (category != null)
			{
				List<Evaluation> evaluations = this.jforumGradeService.getForumEvaluations(forumId);
						
				forum.getEvaluations().addAll(evaluations);
				
				category.getForums().add(forum);
			}
		}
		return category;
	}


	/**
	 * {@inheritDoc}
	 */
	public Category getForumUsersPostCount(String context, int forumId)
	{
		// site forum and users post count
		StringBuilder sql = new StringBuilder();;
		Object[] fields;
		int i;
		
		sql.append("SELECT cc.course_id, c.categories_id AS categories_id, c.title AS cat_title, c.gradable AS cat_gradable, ");
		//sql.append("c.start_date AS cat_start_date, c.end_date AS cat_end_date, c.lock_end_date AS cat_lock_end_date, c.display_order, ");
		sql.append("c.start_date AS cat_start_date, c.hide_until_open AS cat_hide_until_open, c.end_date AS cat_end_date, c.allow_until_date AS cat_allow_until_date, c.display_order, ");
		sql.append("f.forum_id, f.forum_name, f.forum_desc, f.forum_type, f.forum_access_type, f.forum_grade_type, f.start_date AS forum_start_date, f.hide_until_open AS forum_hide_until_open, f.end_date AS forum_end_date, f.allow_until_date AS forum_allow_until_date, f.forum_order, ");
		sql.append("s.user_id, COUNT(p.post_id) AS user_posts_count, u.user_fname AS user_fname,u.user_lname, u.sakai_user_id, u.username ");
		sql.append("FROM  jforum_site_users s, jforum_users u, jforum_posts p, jforum_topics t, ");
		sql.append("jforum_forums f,jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.sakai_site_id = ? ");
		sql.append("AND s.user_id = u.user_id AND s.user_id = p.user_id ");
		sql.append("AND p.topic_id = t.topic_id ");
		sql.append("AND p.forum_id = f.forum_id AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = cc.categories_id ");
		sql.append("AND cc.course_id = ? ");
		//sql.append("AND (f.forum_grade_type = 2 ) ");
		sql.append("AND f.forum_id = ? ");
		//sql.append("GROUP BY cc.course_id, c.categories_id, c.title, c.gradable, c.start_date, c.end_date, c.lock_end_date, c.display_order, ");
		sql.append("GROUP BY cc.course_id, c.categories_id, c.title, c.gradable, c.start_date, c.hide_until_open, c.end_date, c.allow_until_date, c.display_order, ");
		sql.append("f.forum_id, f.forum_name, f.forum_desc, f.forum_order,f.forum_type, f.forum_access_type, f.forum_grade_type, f.start_date, f.hide_until_open, f.end_date, f.allow_until_date, f.lock_end_date, f.forum_order, ");
		sql.append("s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
		sql.append("ORDER BY c.display_order, f.forum_order ");
		
		fields = new Object[3];
		i = 0;
		fields[i++] = context;
		fields[i++] = context;
		fields[i++] = forumId;
		
		List<Category> categories = readForumsUserPostsCount(sql, fields);
		
		Category category = null;
		if (categories.size() == 1)
		{
			category = categories.get(0);
		}
		
		if (category == null)
		{
			category = getCategoryForum(forumId);
		}
		return category;	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Category> getManageCategories(String context, String userId)
	{
		if ((context == null) || (userId == null)) throw new IllegalArgumentException();
		
		if (logger.isDebugEnabled()) 
		{
			logger.debug("getManageCategories - context:"+ context +" userId: "+ userId);
		}
		
		// check user role in the site
		if (!jforumSecurityService.isJForumFacilitator(context, userId))
		{
			return new ArrayList<Category>();
		}
		
		// check user in site
		checkSiteUser(userId, context);
		
		/*check for default categories and forums not done. create default categories and forums when user categories are fetched*/
		List<Category> categories = categoryDao.selectAllByCourse(context);
		
		// sort
		applySort(categories);
		
		return categories;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getTopicEvaluations(String context, int topicId)
	{
		if (context == null) throw new IllegalArgumentException();
		Category category = null;
		
		Topic topic = this.jforumPostService.getTopic(topicId);
		
		if (topic != null)
		{
			Forum forum = this.jforumForumService.getForum(topic.getForumId());
			
			if (forum != null)
			{
				category = getCategory(forum.getCategoryId());
				
				if (category != null)
				{
					List<Evaluation> evaluations = this.jforumGradeService.getTopicEvaluations(topic.getForumId(), topicId);
					topic.getEvaluations().addAll(evaluations);
					
					forum.getTopics().add(topic);					
					category.getForums().add(forum);
				}
			}
		}
		return category;
	}
	
	/**
	 * {@inheritDoc}
	 *//*
	public Category getCategoryForumTopicPosts(int topicId)
	{
		//TODO add access check of the topic for user
		
		Topic topic = this.jforumPostService.getTopic(topicId);
		if (topic == null)
		{
			return null;
		}
		
		List<Post> posts = this.jforumPostService.getTopicPosts(topicId);
		
		Forum forum = this.jforumForumService.getForum(topic.getForumId());
		topic.getPosts().addAll(posts);
		forum.getTopics().add(topic);
		Category category = getCategory(forum.getCategoryId());
		category.getForums().add(forum);
		
		return category;
	}*/
	
	/**
	 * {@inheritDoc}
	 */
	public Category getTopicUsersPostCount(String context, int topicId)
	{
		// topic and users post count
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		int i;
		
		sql.append("SELECT cc.course_id, c.categories_id AS categories_id, c.title AS cat_title, c.gradable AS cat_gradable, ");
		//sql.append("c.start_date AS cat_start_date, c.end_date AS cat_end_date, c.lock_end_date AS cat_lock_end_date, c.display_order, ");
		sql.append("c.start_date AS cat_start_date, c.hide_until_open AS cat_hide_until_open, c.end_date AS cat_end_date, c.allow_until_date AS cat_allow_until_date, c.display_order, ");
		sql.append("f.forum_id, f.forum_name, f.forum_desc, f.forum_type, f.forum_access_type, f.forum_grade_type, f.start_date AS forum_start_date, f.hide_until_open AS forum_hide_until_open, f.end_date AS forum_end_date, f.allow_until_date AS forum_allow_until_date, f.forum_order, "); 
		sql.append("t.topic_id, t.topic_title, topic_grade, topic_export, t.start_date AS topic_start_date, t.hide_until_open AS topic_hide_until_open, t.end_date AS topic_end_date, t.allow_until_date AS topic_allow_until_date, t.topic_status, ");
		sql.append("s.user_id, COUNT(p.post_id) AS user_posts_count, u.user_fname AS user_fname,u.user_lname, u.sakai_user_id, u.username ");
		sql.append("FROM  jforum_site_users s, jforum_users u, jforum_posts p, jforum_topics t, ");
		sql.append("jforum_forums f,jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.sakai_site_id = ? ");
		sql.append("AND s.user_id = u.user_id AND s.user_id = p.user_id ");
		sql.append("AND p.topic_id = t.topic_id ");
		sql.append("AND p.forum_id = f.forum_id AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = cc.categories_id ");
		sql.append("AND cc.course_id = ? ");
//		sql.append("AND (t.topic_grade = "+ Topic.GRADE_YES +" OR t.topic_export = "+ Topic.EXPORT_YES +") ");
		sql.append("AND t.topic_id = ? ");
		//sql.append("GROUP BY cc.course_id, c.categories_id, c.title, c.gradable, c.start_date, c.end_date, c.lock_end_date, c.display_order, ");
		sql.append("GROUP BY cc.course_id, c.categories_id, c.title, c.gradable, c.start_date, c.hide_until_open, c.end_date, c.allow_until_date, c.display_order, ");
		sql.append("f.forum_id, f.forum_name, f.forum_desc, f.forum_order,f.forum_type, f.forum_access_type, f.forum_grade_type, f.start_date, f.hide_until_open, f.end_date, f.allow_until_date, f.lock_end_date, f.forum_order, ");
		sql.append("t.topic_id, t.topic_title, topic_grade, topic_export, t.start_date, t.hide_until_open, t.end_date, t.allow_until_date, t.topic_status, ");
		sql.append("s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
		sql.append("ORDER BY c.display_order, f.forum_order ");
		
		fields = new Object[3];
		i = 0;
		fields[i++] = context;
		fields[i++] = context;
		fields[i++] = topicId;
		
		List<Category> categories = readTopicsUserPostCount(sql, fields);
		
		Category category = null;
		if (categories.size() == 1)
		{
			category = categories.get(0);
		}
		
		return category;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getUserCategoryForums(int categoryId, String userId)
	{
		Category category = categoryDao.selectById(categoryId);
		
		if (category ==  null)
		{
			return null;
		}
		
		String context = category.getContext();
		
		// check user role in the site
		if (!(jforumSecurityService.isJForumParticipant(context, userId) || jforumSecurityService.isJForumFacilitator(context, userId)))
		{
			return null;
		}
		
		List<Forum> forums = jforumForumService.getCategoryForums(categoryId); 
		
		category.getForums().addAll(forums);
		
		if (jforumSecurityService.isJForumFacilitator(context, userId))
		{
			 return category;
		}
		
		// check user in site
		User user = checkSiteUser(userId, context);
		
		// filter for user - forum groups, dates/special access
		filterUserForums(context, forums, user, userId);
		
		return category;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getUserCategoryForumTopicPosts(int topicId, String userId, int startFrom, int count)
	{
		Topic topic = this.jforumPostService.getTopic(topicId);
		if (topic == null)
		{
			return null;
		}
		List<Post> posts = this.jforumPostService.getTopicPosts(topicId, startFrom, count);
		
		Forum forum = this.jforumForumService.getForum(topic.getForumId());
		topic.getPosts().addAll(posts);
		forum.getTopics().add(topic);
		Category category = getCategory(forum.getCategoryId());
		category.getForums().add(forum);
		
		if (jforumSecurityService.isJForumFacilitator(category.getContext(), userId) || jforumSecurityService.isJForumParticipant(category.getContext(), userId))
		{
			
			if (jforumSecurityService.isJForumFacilitator(category.getContext(), userId))
			{
				return category;
			}
			
			if (jforumSecurityService.isJForumParticipant(category.getContext(), userId))
			{
				// filter user topic posts
				filterUserForumTopicPosts(category, userId);
				
				return category;
			}
			
			return null;
		}
		else
		{
			return null;
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getUserCategoryForumTopics(int forumId, String userId)
	{
		if (userId == null)
		{
			throw new IllegalArgumentException("userId is null.");
		}
		
		Forum forum = this.jforumForumService.getForum(forumId);
		
		if (forum == null)
		{
			return null;
		}
		
		List<Topic> topics = this.jforumPostService.getForumTopics(forumId);
		
		forum.getTopics().addAll(topics);
		Category category = getCategory(forum.getCategoryId());
		
		if (category == null)
		{
			return null;
		}
		
		category.getForums().add(forum);
		
		
		if (jforumSecurityService.isJForumFacilitator(category.getContext(), userId))
		{
			User user = jforumUserService.getBySakaiUserId(userId);
			
			// topics read status
			checkUnreadTopics(forum, user, category.getContext());
			return category;
		}
		
		if (jforumSecurityService.isJForumParticipant(category.getContext(), userId))
		{
			// filter user topics
			filterUserForumTopics(category, userId);
		}
		else
		{
			return null;
		}
		
		return category;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getUserCategoryForumTopicsByLimit(int forumId, String userId, int startFrom, int count)
	{
		if (userId == null)
		{
			throw new IllegalArgumentException("userId is null.");
		}
		
		Forum forum = this.jforumForumService.getForum(forumId);
		
		if (forum == null)
		{
			return null;
		}
		
		List<Topic> topics = this.jforumPostService.getForumTopics(forumId, startFrom, count);
		
		forum.getTopics().addAll(topics);
		Category category = getCategory(forum.getCategoryId());
		
		if (category == null)
		{
			return null;
		}
		
		category.getForums().add(forum);
		
		
		if (jforumSecurityService.isJForumFacilitator(category.getContext(), userId))
		{
			User user = jforumUserService.getBySakaiUserId(userId);
			
			// topics read status
			checkUnreadTopics(forum, user, category.getContext());
			return category;
		}
		
		if (jforumSecurityService.isJForumParticipant(category.getContext(), userId))
		{
			// filter user topics
			filterUserForumTopics(category, userId);
		}
		else
		{
			return null;
		}
		
		return category;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Category> getUserContextCategories(String context, String userId)
	{
		if ((context == null) || (userId == null)) throw new IllegalArgumentException();
		
		if (logger.isDebugEnabled()) 
		{
			logger.debug("getUserContextCategories - context:"+ context +" userId: "+ userId);
		}
		
		// check user role in the site
		if (!(jforumSecurityService.isJForumParticipant(context, userId) || jforumSecurityService.isJForumFacilitator(context, userId)))
		{
			return new ArrayList<Category>();
		}
		
		long entryTime = System.currentTimeMillis();
		if (logger.isDebugEnabled())
		{
			logger.debug("Entering getUserContextCategories(String context, String userId) : Enter Time in milliseconds is: "+ System.currentTimeMillis());
		}
		
		try
		{
			// check for default category and forums course initialization and created default categories and forums
			checkAndAddContextDefaultCategoriesForums(context, userId);
		}
		catch (JForumAccessException e1)
		{
			// ignore - check for role is already done
		}
		
		List<Category> categories = categoryDao.selectAllByCourse(context);
		
		// check user in site
		User user = checkSiteUser(userId, context);
				
		// filter the categories for user
		Date currentTime = Calendar.getInstance().getTime();
		
		for (Iterator<Category> catIter = categories.iterator(); catIter.hasNext();) 
		{
		    Category category = catIter.next();
		    
		   ((CategoryImpl)category).setCurrentSakaiUserId(userId);
		    
		    // Don't show the category if start date is a later date
		    if (!(jforumSecurityService.isJForumFacilitator(context, userId)))
			{
		    	long participantCatEntry = System.currentTimeMillis();
		    	if (logger.isDebugEnabled())
				{
					logger.debug("Entering participantCatEntry : Entry time in milliseconds is: "+ participantCatEntry);					
				}
		    	
		    	// ignore categories with invalid dates
		    	if ((category.getAccessDates() != null) && (!category.getAccessDates().isDatesValid()))
		    	{
		    		catIter.remove();
					continue;
		    	}
				
				// category dates
				if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))
				{
					if (category.getAccessDates().getOpenDate().after(currentTime) && category.getAccessDates().isHideUntilOpen())
					{
						catIter.remove();
						continue;
					}
				}
				
				if (category.getForums().isEmpty())
				{
					catIter.remove();
					continue;
				}
				
				Collection<org.sakaiproject.site.api.Group> userGroups = null;
				
				/* Ignore forum start date for forum special access user. 
					Show forums that have special access to the user.*/
				for (Iterator<Forum> forumIter = category.getForums().iterator(); forumIter.hasNext(); ) 
				{
					boolean specialAccessUser = false, specialAccessUserAccess = false;
					Forum f = forumIter.next();
					
					((ForumImpl)f).setCurrentSakaiUserId(userId);
					
					if (f.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
					{
						forumIter.remove();
						continue;
					}
					
					// forum groups
					if (f.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
					{
						boolean userInGroup = false;
						
						try
						{
							// fetch user groups only once
							if (userGroups == null)
							{
								Site site = siteService.getSite(context);
								userGroups = site.getGroupsWithMember(userId);
							}
							
							for (org.sakaiproject.site.api.Group grp : userGroups) 
							{
								if ((f.getGroups() != null) && (f.getGroups().contains(grp.getId())))
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
								logger.warn("getUserCourseMapItemsByContext: missing site: " + context);
							}
						}

						// user not in any group
						if (!userInGroup)
						{
							forumIter.remove();
							continue;
						}
					}
					
					// forum special access
					List<SpecialAccess> specialAccessList = f.getSpecialAccess();
					specialAccessUser = false;
					specialAccessUserAccess = false;
					
					SpecialAccess userSa = null;
					
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
									sa.getAccessDates().setOpenDate(f.getAccessDates().getOpenDate());
								}
								
								if (!sa.isOverrideHideUntilOpen())
								{
									sa.getAccessDates().setHideUntilOpen(f.getAccessDates().isHideUntilOpen());
								}
								
								if (!sa.isOverrideEndDate())
								{
									sa.getAccessDates().setDueDate(f.getAccessDates().getDueDate());
								}
								
								/*if (!sa.isOverrideLockEndDate())
								{
									sa.getAccessDates().setLocked(f.getAccessDates().isLocked());
								}*/
								
								if (!sa.isOverrideAllowUntilDate())
								{
								
									sa.getAccessDates().setAllowUntilDate(f.getAccessDates().getAllowUntilDate());
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
					
					f.getSpecialAccess().clear();
					if (specialAccessUser)
					{
						f.getSpecialAccess().add(userSa);
						
						if (specialAccessUserAccess)
						{
							continue;
						}
						else
						{
							forumIter.remove();
							continue;
						}
					}
					
					// ignore forum with invalid dates
					if ((f.getAccessDates() != null) && (!f.getAccessDates().isDatesValid()))
					{
						forumIter.remove();
						continue;
					}
					
					// forum dates
					if ((f.getAccessDates() != null) && (f.getAccessDates().getOpenDate() != null))
					{
						if (f.getAccessDates().getOpenDate().after(currentTime) && f.getAccessDates().isHideUntilOpen())
						{
							forumIter.remove();
							continue;
						}
					}
					
					
					// set may create topic
					jforumForumService.setForumMayAccessCreateTopic(f, userSa);
				}
				
				// for gradable category verify with course map access advisor
				if (category.isGradable())
				{
					Boolean checkAccess = checkItemAccess(context, JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), userId);
					
					if (!checkAccess)
					{
						category.setBlocked(true);
						category.setBlockedByTitle(getItemAccessMessage(context, JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), userId));
						category.setBlockedByDetails(getItemAccessDetails(context, JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), userId));
					}
				}
				
				// access advisor check, topics count, read/unread
				List<Forum> catForumList = category.getForums();
				
				if (catForumList.isEmpty())
				{
					catIter.remove();
					continue;
				}
				
				//for (Forum f : category.getForums())
				for(Forum f : catForumList)
				{
					// for gradable forum verify with course map access advisor
					if (f.getGradeType() == Grade.GradeType.FORUM.getType())
					{
						Boolean checkAccess = checkItemAccess(context, JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(f.getId()), userId);
						
						if (!checkAccess)
						{
							f.setBlocked(true);
							f.setBlockedByTitle(getItemAccessMessage(context, JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(f.getId()), userId));
							f.setBlockedByDetails(getItemAccessDetails(context, JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(f.getId()), userId));
						}
					}
					
					// get accessible topics count
					int accessibleTopicsCount = jforumPostService.getForumTopicsAccessibleCount(f.getId());
					
					// update messages count(messages count is topic + topic posts counts)
					int accessibleTopicsMessagesCount = jforumPostService.getForumTopicsAccessibleMessagesCount(f.getId());
					
					/* for topic special access user add the special access topics count if the topics are not open and have access as a special access user*/						
					Date curDate = new Date(System.currentTimeMillis());
					
					if ((user == null) || ((f.getAccessDates() != null) && ((f.getAccessDates().getOpenDate() != null) || (f.getAccessDates().getDueDate() != null) || (f.getAccessDates().getAllowUntilDate() != null))))
					{
						f.setTotalTopics(accessibleTopicsCount);
						
						f.setTotalPosts(accessibleTopicsMessagesCount);
					}
					else
					{
						List<SpecialAccess> topicsSpecialAccessList = jforumSpecialAccessService.getTopicsByForumId(f.getId());
						
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
						f.setTotalTopics(accessibleTopicsCount);
						f.setTotalPosts(accessibleTopicsMessagesCount);
					}
					
					 // publish forum future open date and hidden until open grade to gradebook after the open date
					if (f.getGradeType() == Grade.GradeType.FORUM.getType())
					{
						Grade grade = f.getGrade();
				    	if (grade != null && grade.getItemOpenDate() != null && grade.getItemOpenDate().before(currentTime))
				    	{
				    		jforumGradeService.updateGradebook(grade, f);
				    	}
					}
				}
				
				// check unread topics
				checkForumUnreadTopics(user, category, context);
				
				long participantCatExit = System.currentTimeMillis();
		    	if (logger.isDebugEnabled())
				{
					logger.debug("Entering participantCatEntry : Exit time in milliseconds is: "+ participantCatExit);
					logger.debug("Time took to process participantCatEntry and Exit :  "+ (participantCatExit - participantCatEntry));
				}
			}
		    else
		    {
		    	checkForumUnreadTopics(user, category, context);
		    	
		    	// publish forum future open date and hidden until open grade to gradebook after the open date
				List<Forum> catForumList = category.getForums();

				for(Forum forum : catForumList)
				{
					if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
					{
						Grade grade = forum.getGrade();
				    	if (grade != null && grade.getItemOpenDate() != null && grade.getItemOpenDate().before(currentTime))
				    	{
				    		jforumGradeService.updateGradebook(grade, forum);
				    	}
					}
				}
		    }
		    
		    // publish category future open date and hidden until open grade to gradebook after the open date
		    if (category.isGradable())
		    {
		    	Grade grade = category.getGrade();
		    	if (grade != null && grade.getItemOpenDate() != null && grade.getItemOpenDate().before(currentTime))
		    	{
		    		jforumGradeService.updateGradebook(grade, category);
		    	}
		    }
		}
		
		if (logger.isDebugEnabled())
		{
			long exitTime = System.currentTimeMillis();
			logger.debug("Exiting getUserContextCategories(String context, String userId) : Exit time in milliseconds is: "+ exitTime);
			
			logger.debug("Time took to process getUserContextCategories(String context, String userId) : Entry time minus Exit time in milliseconds is: "+ (exitTime - entryTime));
		}
		
		
		
		return categories;
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
	public boolean isUserHasUnreadTopicsAndReplies(String context, String sakaiUserId)
	{
		if (context == null || context.trim().length() == 0)
		{
			throw new IllegalArgumentException("Context is needed.");			
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakai user id is needed.");			
		}
		// access check
		boolean facilitator = jforumSecurityService.isJForumFacilitator(context, sakaiUserId);
		
		boolean participant = false;
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(context, sakaiUserId);
		}
		
		if (!(facilitator || participant))
		{
			return false;
		}
		
		boolean unReadTopicsReplies = false;
		
		/*List<Category> categories = getUserContextCategories(context, sakaiUserId);
		
		if (categories.size() == 0)
		{
			return false;
		}
		
		for (Category category : categories)
		{
			for (Forum forum : category.getForums())
			{
				if (forum.isUnread())
				{
					unReadTopicsReplies = true;
					break;
				}
			}
			
			if (unReadTopicsReplies)
			{
				break;
			}
		}*/
		
		
		List<Category> categories = categoryDao.selectAllByCourse(context);
		
		// check user in site
		User user = checkSiteUser(sakaiUserId, context);
				
		// filter the categories for user
		Date currentTime = Calendar.getInstance().getTime();
		
		Collection<org.sakaiproject.site.api.Group> userGroups = null;
		
		for (Iterator<Category> catIter = categories.iterator(); catIter.hasNext();) 
		{
		    Category category = catIter.next();
		    
		    // Don't show the category if start date is a later date
		    if (!(jforumSecurityService.isJForumFacilitator(context, sakaiUserId)))
			{
				// ignore categories with invalid dates
				if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null) && (category.getAccessDates().getDueDate() != null))
				{
					if (category.getAccessDates().getDueDate().before(category.getAccessDates().getOpenDate()))
					{
						catIter.remove();
						continue;
					}
				}
				
				// category dates
				if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))
				{
					if (category.getAccessDates().getOpenDate().after(currentTime))
					{
						catIter.remove();
						continue;
					}
				}
				
				/* Ignore forum start date for forum special access user. 
					Show forums that have special access to the user.*/
				for (Iterator<Forum> forumIter = category.getForums().iterator(); forumIter.hasNext(); ) 
				{
					boolean specialAccessUser = false, specialAccessUserAccess = false;
					Forum f = forumIter.next();
					
					if (f.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
					{
						forumIter.remove();
						continue;
					}
					
					// forum groups
					if (f.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
					{
						boolean userInGroup = false;
						
						try
						{
							// fetch user groups only once
							if (userGroups == null)
							{
								Site site = siteService.getSite(context);
								userGroups = site.getGroupsWithMember(sakaiUserId);
							}
							
							for (org.sakaiproject.site.api.Group grp : userGroups) 
							{
								if ((f.getGroups() != null) && (f.getGroups().contains(grp.getId())))
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
								logger.warn("getUserCourseMapItemsByContext: missing site: " + context);
							}
						}

						// user not in any group
						if (!userInGroup)
						{
							forumIter.remove();
							continue;
						}
					}
					
					// forum special access
					List<SpecialAccess> specialAccessList = f.getSpecialAccess();
					specialAccessUser = false;
					specialAccessUserAccess = false;
					
					SpecialAccess userSa = null;
					
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
									sa.getAccessDates().setOpenDate(f.getAccessDates().getOpenDate());
								}
								if (!sa.isOverrideEndDate())
								{
									sa.getAccessDates().setDueDate(f.getAccessDates().getDueDate());
								}
								if (!sa.isOverrideLockEndDate())
								{
									sa.getAccessDates().setLocked(f.getAccessDates().isLocked());
								}
								
								if (sa.getAccessDates().getOpenDate() == null)
								{
									specialAccessUserAccess = true;
								}
								else if (currentTime.after(sa.getAccessDates().getOpenDate()))
								{
									specialAccessUserAccess = true;
								}
								break;
							}
						}
					}
					
					f.getSpecialAccess().clear();
					if (specialAccessUser)
					{
						f.getSpecialAccess().add(userSa);
						
						if (specialAccessUserAccess)
						{
							continue;
						}
						else
						{
							forumIter.remove();
							continue;
						}
					}
					// forum dates
					if ((f.getAccessDates() != null) && (f.getAccessDates().getOpenDate() != null))
					{
						if (f.getAccessDates().getOpenDate().after(currentTime))
						{
							forumIter.remove();
							continue;
						}
					}
				}
				
				// for gradable category verify with course map access advisor
				/*if (category.isGradable())
				{
					Boolean checkAccess = checkItemAccess(context, JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), userId);
					
					if (!checkAccess)
					{
						category.setBlocked(true);
						category.setBlockedByTitle(getItemAccessMessage(context, JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), userId));
						category.setBlockedByDetails(getItemAccessDetails(context, JForumService.CMItemIdPrefix.CAT.getCMItemIdPrefix()+ String.valueOf(category.getId()), userId));
					}
				}*/
				
				// read/unread
				for (Forum forum : category.getForums())
				{
					// check unread topics
					if (isUserHasUnreadTopics(context, forum, user))
					{
						unReadTopicsReplies = true;
						break;
					}
				}
			}
		    else
		    {
		    	// read/unread
				for (Forum forum : category.getForums())
				{
					// check unread topics
					if (isUserHasUnreadTopics(context, forum, user))
					{
						unReadTopicsReplies = true;
						break;
					}
				}
		    }			
		}
		
		
	
		return unReadTopicsReplies;
	}

	/**
	 * {@inheritDoc}
	 */
	public void modifyCategory(Category category) throws JForumAccessException, JForumGradesModificationException
	{
		if (category == null || category.getId() <= 0  || category.getTitle().trim().length() == 0)
		{
			throw new IllegalArgumentException("Category information is missing");			
		}
				
		if (category.getModifiedBySakaiUserId() == null)
		{
			throw new IllegalArgumentException("modified by user information is missing");
		}
		
		// access check
		boolean facilitator = jforumSecurityService.isJForumFacilitator(category.getContext(), category.getModifiedBySakaiUserId());
		
		if (!facilitator)
		{
			throw new JForumAccessException(category.getModifiedBySakaiUserId());
		}
		
		// if existing category is gradable and has evaluations grade type cannot be changed
		Category exisCategory = categoryDao.selectById(category.getId());
		
		if (exisCategory == null)
		{
			return;
		}
		
		// if category has dates check forum and topic dates count
		if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null || category.getAccessDates().getDueDate() != null))
		{
			if ((exisCategory.getAccessDates() == null) || (exisCategory.getAccessDates().getOpenDate() == null && exisCategory.getAccessDates().getDueDate() == null))
			{
				// category can have dates if forum or topics of the category have no dates
				int topicDatesCount = this.jforumPostService.getTopicDatesCountByCategory(category.getId());
				int forumDatesCount = this.jforumForumService.getForumDatesCount(category.getId());
				
				if (topicDatesCount > 0 || forumDatesCount > 0)
				{
					category.getAccessDates().setOpenDate(null);
					category.getAccessDates().setDueDate(null);
					category.getAccessDates().setAllowUntilDate(null);
				}
			}
		}
		
		// cannot change the grade type if existing category is gradable and evaluated
		if (exisCategory.isGradable())
		{
			int evaluationsCount = jforumGradeService.getCategoryEvaluationsCount(exisCategory.getId());
			
			if (evaluationsCount > 0 && (!category.isGradable()))
			{
				throw new JForumGradesModificationException("Category is already evaluated. Category cannot be changed to not gradable.");
			}
		}
		
		// if category forums or topics are gradable category cannot be changed to gradable from not gradable
		if (!exisCategory.isGradable() && category.isGradable())
		{
			if (jforumGradeService.getCategoryGradableForumsCount(exisCategory.getId()) > 0)
			{
				throw new JForumGradesModificationException("Category Forums or topics are gradable. Category cannot be changed to gradable");
			}
		}
		
		// update category
		categoryDao.update(category);
		
		// clear cache
		if (category != null) clearCache(category);
		
		// update grade book
		if (exisCategory.isGradable() || category.isGradable())
		{
			if (exisCategory.isGradable() && !category.isGradable())
			{
				// remove entry in the gradebook
				Grade catGrade = exisCategory.getGrade();
				
				if (catGrade != null && catGrade.isAddToGradeBook())
				{
					jforumGradeService.removeGradebookEntry(catGrade);
				}			
			}
			else if (category.isGradable())
			{
				Grade grade = category.getGrade();
				
				if (grade != null && !grade.isAddToGradeBook())
				{
					jforumGradeService.removeGradebookEntry(grade);
				}
				else
				{
					boolean incorrectDates = false;
					
					if (category.getAccessDates() != null && !category.getAccessDates().isDatesValid())
					{
						incorrectDates = true;
						
						Grade catGrade = category.getGrade();
						
						if (catGrade != null && catGrade.isAddToGradeBook())
						{
							jforumGradeService.removeGradebookEntry(catGrade);								
						}
					}
	
					if (!incorrectDates)
					{
						// add or update entry in the gradebook
						Grade catGrade = category.getGrade();
						
						if (catGrade != null && catGrade.isAddToGradeBook())
						{
							jforumGradeService.updateGradebook(catGrade, category);
						}
					}
				}
			}
		}
		else
		{
			boolean datesChanged = checkDatesChanged(category, exisCategory);
			
			if (datesChanged)
			{
				/// gradable forum and topics of the category in the gradebook
				// check for invalid dates
				if (category.getAccessDates() != null && !category.getAccessDates().isDatesValid())
				{
					List<Grade> forumTopicGrades = jforumGradeService.getForumTopicGradesByCategory(category.getId());
					
					for (Grade forumTopicGrade : forumTopicGrades)	
					{
						if (forumTopicGrade.isAddToGradeBook()) 
						{
							jforumGradeService.removeGradebookEntry(forumTopicGrade);
						}
					}								
					return;
				}
				
				//update gradebook due dates for gradable forums and topics of the category
				List<Grade> forumTopicGrades = jforumGradeService.getForumTopicGradesByCategory(category.getId());
				
				for (Grade forumTopicGrade : forumTopicGrades)	
				{
					if (forumTopicGrade.isAddToGradeBook()) 
					{							
						if (forumTopicGrade.getType() == Grade.GradeType.FORUM.getType())
						{	
							Forum forum = jforumForumService.getForum(forumTopicGrade.getForumId());
							
							// update due date in the gradebook
							jforumGradeService.updateGradebook(forumTopicGrade, forum);
							
						}
						else if (forumTopicGrade.getType() == Grade.GradeType.TOPIC.getType())
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
	
	/**
	 * {@inheritDoc}
	 */
	public void modifyOrder(int categoryId, int positionToMove, String sakaiUserId) throws JForumAccessException
	{
		if (categoryId <= 0)
		{
			throw new IllegalArgumentException("To change order category information is missing");
		}
		
		if (positionToMove <= 0)
		{
			throw new IllegalArgumentException("position to move is incorrect");
		}
		
		Category toChangeCategory = getCategory(categoryId);
		
		if (toChangeCategory == null)
		{
			throw new IllegalArgumentException("Category is missing");
		}
		
		
		List<org.etudes.api.app.jforum.Category> categories = getManageCategories(toChangeCategory.getContext(), sakaiUserId);
		
		if (positionToMove > categories.size())
		{
			throw new IllegalArgumentException("Postion to move is not valid");
		}		
		
		Category otherCatgory = categories.get(positionToMove - 1);
				
		if (otherCatgory == null)
		{
			throw new IllegalArgumentException("To change forum or Other forum information is not existing");
		}
		
		// access check
		boolean facilitator = jforumSecurityService.isJForumFacilitator(toChangeCategory.getContext(), sakaiUserId);
		
		if (!facilitator)
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		if (!toChangeCategory.getContext().equalsIgnoreCase(otherCatgory.getContext()))
		{
			throw new IllegalArgumentException("Both categories should belong to the same site");
		}
		
		categoryDao.updateOrder(toChangeCategory, otherCatgory);
		
		// clear cache
		if (toChangeCategory != null) clearCache(toChangeCategory);
		if (otherCatgory != null) clearCache(otherCatgory);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category newCategory()
	{
		// TODO: add access check
		
		Category category = new CategoryImpl();
		category.setAccessDates(new AccessDatesImpl());
		category.setGrade(new GradeImpl());
		
		return category;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Grade newGrade(Category category)
	{
		// TODO: add access check
		
		if (category == null)
		{
			return null;
		}
		
		Grade grade = new GradeImpl();
		category.setGrade(grade);
		
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
	 * @param jforumForumService the jforumForumService to set
	 */
	public void setJforumForumService(JForumForumService jforumForumService)
	{
		this.jforumForumService = jforumForumService;
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
	 * @param jforumSpecialAccessService the jforumSpecialAccessService to set
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
	 *          The sqlService to set
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
	 * @param userDirectoryService the userDirectoryService to set
	 */
	public void setUserDirectoryService(UserDirectoryService userDirectoryService)
	{
		this.userDirectoryService = userDirectoryService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateDates(Category category, Type type)
	{
		if (category == null)
		{
			throw new IllegalArgumentException("updateDates: category is null");
		}
		
		if (type == Type.CATEGORY)
		{
				
			Category existingCategory = getCategory(category.getId());
			
			if (existingCategory == null)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("updateDates: There is no existing category with category id "+ category.getId());
				}
				return;
			}
			
			// update if the date changes
			boolean datesChanged = false;
			
			datesChanged = checkDatesChanged(category, existingCategory);
			
			if (datesChanged)
			{
				// category can have dates if forum or topics of the category have no dates
				int topicDatesCount = this.jforumPostService.getTopicDatesCountByCategory(category.getId());
				int forumDatesCount = this.jforumForumService.getForumDatesCount(category.getId());
				
				String sql = "UPDATE jforum_categories SET start_date = ?, hide_until_open = ?, end_date = ?, allow_until_date = ? WHERE categories_id = ?";
		
				Object[] fields = new Object[5];
				int i = 0;
				if ((topicDatesCount > 0) || (forumDatesCount > 0))
				{
					category.getAccessDates().setOpenDate(null);
					category.getAccessDates().setHideUntilOpen(null);
					category.getAccessDates().setDueDate(null);
					category.getAccessDates().setAllowUntilDate(null);
				}
				
				/*int locked = 0;
				if ((category.getAccessDates() != null) && (existingCategory.getAccessDates() != null))
				{
					locked = existingCategory.getAccessDates().isLocked() ? 1 : 0;
				}*/
				
				int hideUntilOpen = 0;
				if ((category.getAccessDates() != null) && (existingCategory.getAccessDates() != null))
				{
					if (category.getAccessDates().isHideUntilOpen() != null)
					{
						hideUntilOpen = category.getAccessDates().isHideUntilOpen() ? 1 : 0;
					}
				}
					
				fields[i++] = ((category.getAccessDates() == null) || (category.getAccessDates().getOpenDate() == null)) ? null : new Timestamp(category.getAccessDates().getOpenDate().getTime());
				if (((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null)))
				{
					fields[i++] = hideUntilOpen;
				}
				else
				{
					fields[i++] = 0;
				}
				fields[i++] = ((category.getAccessDates() == null) || (category.getAccessDates().getDueDate() == null)) ? null : new Timestamp(category.getAccessDates().getDueDate().getTime());
				fields[i++] = ((category.getAccessDates() == null) || (category.getAccessDates().getAllowUntilDate() == null)) ? null : new Timestamp(category.getAccessDates().getAllowUntilDate().getTime());
				//fields[i++] = locked;
				fields[i++] = category.getId();
	
				if (!sqlService.dbWrite(sql.toString(), fields)) 
				{
					throw new RuntimeException("updateDates: db write failed");
				}
				
				// clear category from thread local cache
				clearCache(existingCategory);
				
				// update the due date changes in the gradebook
				if (existingCategory.isGradable())
				{
					Grade catGrade = category.getGrade();
					
					// check the due date changes
					if (category.getAccessDates() != null && !category.getAccessDates().isDatesValid())
					{
						if (catGrade != null && catGrade.isAddToGradeBook())
						{
							jforumGradeService.removeGradebookEntry(catGrade);
						}
							
						// clear category from thread local cache
						clearCache(existingCategory);
						return;
				
					}
					
					//push due dates if invalid dates are corrected from course map 
					category.setTitle(existingCategory.getTitle());
					category.setGradable(existingCategory.isGradable());
				
					if (catGrade != null && catGrade.isAddToGradeBook())
					{
						jforumGradeService.updateGradebook(catGrade, category);
					}
				}
				else
				{
					// gradable forum and topics of the category in the gradebook
					if (category.getAccessDates() != null)
					{
						// check for invalid dates
						if (category.getAccessDates() != null && !category.getAccessDates().isDatesValid())
						{
							List<Grade> forumTopicGrades = jforumGradeService.getForumTopicGradesByCategory(category.getId());
							
							for (Grade forumTopicGrade : forumTopicGrades)	
							{
								if (forumTopicGrade.isAddToGradeBook()) 
								{
									jforumGradeService.removeGradebookEntry(forumTopicGrade);
								}
							}								
							return;							
						}
												
						//update gradebook due dates for gradable forums and topics of the category
						List<Grade> forumTopicGrades = jforumGradeService.getForumTopicGradesByCategory(category.getId());
						
						for (Grade forumTopicGrade : forumTopicGrades)	
						{
							if (forumTopicGrade.isAddToGradeBook()) 
							{							
								if (forumTopicGrade.getType() == Grade.GradeType.FORUM.getType())
								{	
									Forum forum = jforumForumService.getForum(forumTopicGrade.getForumId());
									
									// update due date in the gradebook
									jforumGradeService.updateGradebook(forumTopicGrade, forum);
									
								}
								else if (forumTopicGrade.getType() == Grade.GradeType.TOPIC.getType())
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
		else if (type == Type.FORUM)
		{
			if (category.getForums().size() != 1)
			{
				return;
			}
			
			Forum forum = category.getForums().get(0);
			Forum existingForum = this.jforumForumService.getForum(forum.getId());
			// forum can have dates if category or topics of the forum have no dates
			int topicDatesCount = this.jforumPostService.getTopicDatesCountByForum(forum.getId());
			boolean categoryDates = false;
			
			Category exisCategory = this.getCategory(existingForum.getCategoryId());
			
			if ((exisCategory.getAccessDates() != null))
			{
				if ((exisCategory.getAccessDates().getOpenDate() != null) || (exisCategory.getAccessDates().getDueDate() != null) || (exisCategory.getAccessDates().getAllowUntilDate() != null))
				{
					categoryDates = true;
				}						
			}									
			
			if ((topicDatesCount > 0) || (categoryDates))
			{
				forum.getAccessDates().setOpenDate(null);
				forum.getAccessDates().setHideUntilOpen(null);
				forum.getAccessDates().setDueDate(null);
				forum.getAccessDates().setAllowUntilDate(null);
			} 
			this.jforumForumService.updateDates(forum);
		}
		else if (type == Type.TOPIC)
		{
			if ((category.getForums().size() != 1) && (category.getForums().get(0).getTopics().size() != 1))
			{
				return;
			}
			// topic can have dates if category and forum have no dates
			Forum forum = category.getForums().get(0);
			Topic topic = forum.getTopics().get(0);
			
			Forum existingForum = this.jforumForumService.getForum(forum.getId());
			boolean forumDates = false, categoryDates = false;
			
			if ((existingForum.getAccessDates() != null))
			{
				if ((existingForum.getAccessDates().getOpenDate() != null) || (existingForum.getAccessDates().getDueDate() != null) || (existingForum.getAccessDates().getAllowUntilDate() != null))
				{
					forumDates = true;
				}						
			}
			
			Category existingcategory = getCategory(forum.getCategoryId());
			
			if ((existingcategory.getAccessDates() != null))
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
			
			this.jforumPostService.updateDates(topic);
			
		}
	}
	
	/**
	 * Sort the categories, forums according to their display order
	 * 
	 * @param categories	Categories
	 */
	protected void applySort(List<Category> categories)
	{
		Collections.sort(categories, new Comparator<Category>()
		{
			public int compare(Category c1, Category c2)
			{
				if (c1.getOrder() > c2.getOrder()) 
				{
					return 1;
				}
				else if (c1.getOrder() < c2.getOrder() ) 
				{
					return -1;
				}
				else if (c1.getOrder() == c2.getOrder() ) 
				{
					return 0;
				}
				else 
				{
					return c1.getTitle().compareTo(c2.getTitle());
				}
			}
		});		
		
		for (Category category : categories) 
		{
			Collections.sort(category.getForums(), new Comparator<Forum>()
			{
				public int compare(Forum f1, Forum f2)
				{
					if (f1.getOrder() > f2.getOrder()) 
					{
						return 1;
					}
					else if (f1.getOrder() < f2.getOrder() ) 
					{
						return -1;
					}
					else if (f1.getOrder() == f2.getOrder() ) 
					{
						return 0;
					}
					else 
					{
						return f1.getName().compareTo(f2.getName());
					}
				}
			});
		}
		
		for (Category category : categories) 
		{
			for (Forum forum : category.getForums()) 
			{
				Collections.sort(forum.getTopics(), new Comparator<Topic>()
				{
					public int compare(Topic t1, Topic t2)
					{
						if (t1.getId() < t2.getId()) 
						{
							return 1;
						}
						else if (t1.getId() > t2.getId()) 
						{
							return -1;
						}
						return 0;
					}
				});
			}
		}
	}

	/**
	 *Key for caching a category.
	 * 
	 * @param categoryId	Category id
	 * 
	 * @return	Cache key
	 */
	protected String cacheKey(String categoryId)
	{
		return "jforum:category:" + categoryId;
	}
	
	/**
	 * Check for date changes
	 * 
	 * @param category			Category with user input dates
	 * 
	 * @param existingCategory	Existing category 
	 * 
	 * @return	true - if dates are changed
	 * 			false - if dates are not changed
	 */
	protected boolean checkDatesChanged(Category category, Category existingCategory)
	{
		boolean datesChanged = false;
		
		Date exisCatOpenDate = null, exisCatDueDate = null, exisCatAllowUntilDate = null, modCatOpenDate = null, modCatDueDate = null, modCatAllowUntilDate = null;
		Boolean exisHideUntilOpen = null, modHideUntilOpen = null;
		
		if (category.getAccessDates() != null)
		{
			modCatOpenDate = category.getAccessDates().getOpenDate();
			modHideUntilOpen = category.getAccessDates().isHideUntilOpen();
			modCatDueDate = category.getAccessDates().getDueDate();
			modCatAllowUntilDate = category.getAccessDates().getAllowUntilDate();
		}
		
		if (existingCategory.getAccessDates() != null)
		{
			exisCatOpenDate = existingCategory.getAccessDates().getOpenDate();
			exisHideUntilOpen = existingCategory.getAccessDates().isHideUntilOpen();
			exisCatDueDate = existingCategory.getAccessDates().getDueDate();
			exisCatAllowUntilDate = existingCategory.getAccessDates().getAllowUntilDate();
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
	 * Check forunm unread topics for the user
	 * 
	 * @param user		Jforum user
	 * 
	 * @param category	The category
	 * 
	 * @param siteId	Course id or site id
	 */
	protected void checkForumUnreadTopics(User user, Category category, String siteId)
	{
		Date currentTime = new Date(System.currentTimeMillis());
		
		// read/unread
		for (Forum f : category.getForums())
		{
		    if (user == null)
			{
				f.setUnread(true);
				continue;
			}
		    
			// check marked unread topics
			int markedUnreadTopicCount  = jforumPostService.getMarkedTopicsCountByForum(f.getId(), user.getId());
			
			if (markedUnreadTopicCount > 0)
			{
				f.setUnread(true);
				continue;
			}
			
			// marked all read time
			Date markAllReadTime = jforumUserService.getMarkAllReadTime(siteId, user.getId());
			boolean facilitator = jforumSecurityService.isJForumFacilitator(siteId, user.getSakaiUserId());
			
			List<SpecialAccess> forumTopicsSpecialAccess = jforumSpecialAccessService.getTopicsByForumId(f.getId());
			
			/*get all topics with latest post time and check with topic read time*/
			List<Topic> topicsLastPosttimes = null;
			
			if (markAllReadTime == null)
			{
				// check with all topics
				if (facilitator)
				{
					topicsLastPosttimes = jforumPostService.getForumTopicLatestPosttimes(f.getId(), user.getId(), null, false);
				}
				else
				{
					topicsLastPosttimes = jforumPostService.getForumTopicLatestPosttimes(f.getId(), user.getId(), null, true);
				}
			}
			else
			{
				/*check with topics whose latest post time is after mark all read time and not posted by the current user*/
				if (facilitator)
				{
					topicsLastPosttimes = jforumPostService.getForumTopicLatestPosttimes(f.getId(), user.getId(), markAllReadTime, false);
				}
				else
				{
					topicsLastPosttimes = jforumPostService.getForumTopicLatestPosttimes(f.getId(), user.getId(), markAllReadTime, true);
				}
			}

			List<Topic> lastPosttimeTopicsList = new ArrayList<Topic>();
			
			for (Topic t : topicsLastPosttimes)
			{
				if (!facilitator)
				{
					/* no special access topics and special access topics*/
					List<SpecialAccess> topicsSpecialAccessList = jforumSpecialAccessService.getTopicsByForumId(f.getId());
					
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
				visitedTopics = jforumPostService.getUserForumTopicVisittimes(f.getId(), user.getId());
			}
			else
			{
				visitedTopics = jforumPostService.getUserForumTopicVisittimes(f.getId(), user.getId());
			}
			
			if (lastPosttimeTopicsList.size() > visitedTopics.size())
			{
				f.setUnread(true);
				continue;
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
						f.setUnread(true);
						break;
					}
					
					if (lastVistiedTopic.getTime().before(lastPosttimeTopic.getTime()))
					{
						f.setUnread(true);
						break;
					}
				}
				else
				{
					f.setUnread(true);
					break;
				}
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
	 * checks to see if user is jforum user and adds if not
	 * 
	 * @param userId	Sakai user id
	 * 
	 * @param context	The site id
	 * 
	 * @return	Jforum user if user jforum user
	 */
	protected User checkSiteUser(String userId, String context)
	{
		User user = jforumUserService.getBySakaiUserId(userId);
		
		if (user == null)
		{
			//user is not in the jforum tables. Check if user is in site and add to jforum.
	    	if (jforumSecurityService.isJForumFacilitator(userId) || jforumSecurityService.isJForumParticipant(userId))
	    	{
	    		try
				{
	    			org.sakaiproject.user.api.User sakaiUser = userDirectoryService.getUser(userId);
	    			
	    			if (sakaiUser != null)
	    			{
	    				UserImpl jforumUser = new UserImpl();
	    				jforumUser.setUsername(sakaiUser.getEid());
	    				jforumUser.setSakaiUserId(sakaiUser.getId());
	    				jforumUser.setEmail(sakaiUser.getEmail());
	    				jforumUser.setFirstName(sakaiUser.getFirstName());
	    				jforumUser.setLastName(sakaiUser.getLastName());
	    				
	    				try
						{
							int jforurmUserId = jforumUserService.createUser(jforumUser);
							jforumUser.setId(jforurmUserId);
							
							//add user to jforum site users
							if (jforurmUserId > 0)
							{
								jforumUserService.addUserToSiteUsers(context, jforurmUserId);
							}
							
						}
						catch (UserAlreadyExistException e)
						{
							if (logger.isErrorEnabled())
							{
								logger.error(e.toString(), e);
							}
						}
						catch (UserAlreadyInSiteUsersException e)
						{
							if (logger.isErrorEnabled())
							{
								logger.error(e.toString(), e);
							}
						}
	    			}
				}
				catch (UserNotDefinedException e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
				}
	    	}
		}
		else
		{
			// add if user in not in site users  
			try
			{
				if (jforumUserService.getSiteUser(context, user.getId()) == null)
				{
					jforumUserService.addUserToSiteUsers(context, user.getId());
				}
			}
			catch (UserAlreadyInSiteUsersException e)
			{
				if (logger.isErrorEnabled())
				{
					logger.error(e.toString(), e);
				}
			}
		}
		return user;
	}
	
	/**
	 * check topic dates for the use
	 * 
	 * @param forum	The forum with topics
	 * 
	 * @param user	Jforum user
	 */
	protected void checkTopicDates(Forum forum, User user)
	{
		Date currentTime = Calendar.getInstance().getTime();
		
		for (Iterator<Topic> topicIter = forum.getTopics().iterator(); topicIter.hasNext();) 
		{
		    Topic topic = topicIter.next();
		    
			// topic special access
			List<SpecialAccess> topicSpecialAccessList = topic.getSpecialAccess();
			boolean topicSpecialAccessUser = false;
			boolean topicSpecialAccessUserAccess = false;
			SpecialAccess userSa = null;
			
			if ((user != null) && ((topic.getAccessDates() != null) && ((topic.getAccessDates().getOpenDate() != null) || (topic.getAccessDates().getDueDate() != null))))
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
						if (!sa.isOverrideEndDate())
						{
							sa.getAccessDates().setDueDate(topic.getAccessDates().getDueDate());
						}
						if (!sa.isOverrideLockEndDate())
						{
							sa.getAccessDates().setLocked(topic.getAccessDates().isLocked());
						}
						
						if (sa.getAccessDates().getOpenDate() == null)
						{
							topicSpecialAccessUserAccess = true;
						}
						else if (currentTime.after(sa.getAccessDates().getOpenDate()))
						{
							topicSpecialAccessUserAccess = true;
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
				if (topic.getAccessDates().getOpenDate().after(currentTime))
				{
					topicIter.remove();
					continue;
				}
			}
		}
	}

	/**
	 * Check topic mark the read status
	 * 
	 * @param forum		Forum
	 * 
	 * @param user		Jforum user
	 * 
	 * @param siteId	Course/site id
	 */
	protected void checkUnreadTopics(Forum forum, User user, String siteId)
	{
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
					topicsLastPosttimes = jforumPostService.getForumTopicLatestPosttimes(forum.getId(), user.getId(), null, false);
				}
				else
				{
					topicsLastPosttimes = jforumPostService.getForumTopicLatestPosttimes(forum.getId(), user.getId(), null, true);
				}
			}
			else
			{
				/*check topics whose latest post time is after mark all read time and not posted by the current user*/
				if (facilitator)
				{
					topicsLastPosttimes = jforumPostService.getForumTopicLatestPosttimes(forum.getId(), user.getId(), markAllReadTime, false);
				}
				else
				{
					topicsLastPosttimes = jforumPostService.getForumTopicLatestPosttimes(forum.getId(), user.getId(), markAllReadTime, true);
				}
			}
			
			Map<Integer, Topic> lastPosttimeTopicsMap = new HashMap<Integer, Topic>();
			for (Topic topicPostTimes : topicsLastPosttimes)
			{
				lastPosttimeTopicsMap.put(new Integer(topicPostTimes.getId()), topicPostTimes);
			}
			
			// compare with visited topics time
			List<Topic> visitedTopics = jforumPostService.getUserForumTopicVisittimes(forum.getId(), user.getId());
			
			Map<Integer, Topic> visitedTopicsMap = new HashMap<Integer, Topic>();
			for (Topic visitedTopic : visitedTopics)
			{
				visitedTopicsMap.put(new Integer(visitedTopic.getId()), visitedTopic);
			}
			
			Topic visitedTopic = null;
			Topic lastPostTimeTopic = null;
			for (Topic topic : forum.getTopics())
			{
				if (user == null)
				{
					topic.setRead(false);
					continue;
				}
				
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
			}
		}
		else
		{
			for (Topic topic : forum.getTopics())
			{
				topic.setRead(false);				
			}
		}

		
	}
	
	protected void checkUserCategoryAccess(Category category, String sakaiUserId) throws JForumAccessException
	{
		if (category == null || sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			return;
		}
		
		// invalid dates
		if ((category.getAccessDates() != null) && (!category.getAccessDates().isDatesValid()))
		{
			throw new JForumAccessException(sakaiUserId);
		}
		
		Date currentTime = new Date();
		
		// category dates
		if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))
		{
			if (category.getAccessDates().getOpenDate().after(currentTime))
			{
				throw new JForumAccessException(sakaiUserId);
			}
		}
		
		// for gradable category verify with course map access advisor
		if (category != null && category.isGradable())
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
	 * Clear the category from thread local cache
	 * 
	 * @param category
	 *        The category.
	 */
	protected void clearCache(Category category)
	{
		// clear the cache
		this.threadLocalManager.set(cacheKey(String.valueOf(category.getId())), null);
	}
	
	/**
	 * Create a copy of category
	 * 
	 * @param category	Category
	 * 
	 * @return	The copy of category
	 */
	protected Category clone(CategoryImpl category)
	{
		Category clonedCategory = new CategoryImpl(category);
		
		// always get the category grade
		if (category.isGradable())
		{
			Grade grade = jforumGradeService.getByCategoryId(category.getId());
			category.setGrade(grade);
		}
		
		return clonedCategory;
	}
	
	/**
	 * Creates the category object from the result set
	 * 
	 * @param result	The result set
	 * 
	 * @return			The category object
	 * 
	 * @throws SQLException
	 */
	protected Category fillCategory(ResultSet result) throws SQLException
	{
		CategoryImpl category = new CategoryImpl();
		category.setId(result.getInt("categories_id"));
		category.setTitle(result.getString("cat_title"));							
		category.setGradable(result.getInt("cat_gradable") == 1);
		
		if ((result.getDate("cat_start_date") != null) || (result.getDate("cat_end_date") != null) || (result.getDate("cat_allow_until_date") != null))
		{
			AccessDates accessDates = new AccessDatesImpl();
			//accessDates.setOpenDate(result.getTimestamp("cat_start_date"));
			if (result.getDate("cat_start_date") != null)
		    {
		      Timestamp startDate = result.getTimestamp("cat_start_date");
		      accessDates.setOpenDate(startDate);
		      accessDates.setHideUntilOpen(result.getInt("cat_hide_until_open") > 0);
		    }
		    else
		    {
		    	accessDates.setOpenDate(null);
		    }
			
			accessDates.setDueDate(result.getTimestamp("cat_end_date"));
			/*if (result.getDate("cat_end_date") != null)
		    {
		      Timestamp endDate = result.getTimestamp("cat_end_date");
		      accessDates.setDueDate(endDate);
		      //accessDates.setLocked(result.getInt("cat_lock_end_date") > 0);
		    }
		    else
		    {
		    	accessDates.setDueDate(null);
		    }*/
			
			accessDates.setAllowUntilDate(result.getTimestamp("cat_allow_until_date"));
			category.setAccessDates(accessDates);
		}
		else
		{
			AccessDates accessDates = new AccessDatesImpl();
			category.setAccessDates(accessDates);
		}
		category.setOrder(result.getInt("display_order"));
		category.setContext(result.getString("course_id"));
				
		return category;
	}
	
	/**
	 * Fills the category forum from the resultset
	 * 
	 * @param result	The resultset
	 * 
	 * @return	The forum object
	 * 
	 * @throws SQLException
	 */
	protected Forum fillCategoryForum(ResultSet result) throws SQLException
	{
		ForumImpl forum = new ForumImpl();
		forum.setId(result.getInt("forum_id"));
		forum.setDescription(result.getString("forum_desc"));
		forum.setCategoryId(result.getInt("categories_id"));
		forum.setName(result.getString("forum_name"));
		forum.setType(result.getInt("forum_type"));
		forum.setAccessType(result.getInt("forum_access_type"));
		forum.setGradeType(result.getInt("forum_grade_type"));
		
		if ((result.getDate("forum_start_date") != null) || (result.getDate("forum_end_date") != null) || result.getDate("forum_allow_until_date") != null)
		{
			AccessDates accessDates = new AccessDatesImpl();
			//accessDates.setOpenDate(result.getTimestamp("forum_start_date"));
			if (result.getDate("forum_start_date") != null)
		    {
		      Timestamp startDate = result.getTimestamp("forum_start_date");
		      accessDates.setOpenDate(startDate);
		      accessDates.setHideUntilOpen(result.getInt("forum_hide_until_open") > 0);
		    }
		    else
		    {
		    	accessDates.setOpenDate(null);
		    }
			
			accessDates.setDueDate(result.getTimestamp("forum_end_date"));
			/*if (result.getDate("forum_end_date") != null)
		    {
		      Timestamp endDate = result.getTimestamp("forum_end_date");
		      accessDates.setDueDate(endDate);
		      accessDates.setLocked(result.getInt("forum_lock_end_date") > 0);
		    }
		    else
		    {
		    	accessDates.setDueDate(null);
		    }*/
			accessDates.setAllowUntilDate(result.getTimestamp("forum_allow_until_date"));
			forum.setAccessDates(accessDates);
		}
		else
		{
			AccessDates accessDates = new AccessDatesImpl();
			forum.setAccessDates(accessDates);
		}
		
		forum.setOrder(result.getInt("forum_order"));
		
		return forum;
	}
	
	
	/**
	 * Fills the category forum topic from the resultset
	 * 
	 * @param result	The resultset
	 * 
	 * @return	The topic object
	 * 
	 * @throws SQLException
	 */
	protected Topic fillCategoryForumTopic(ResultSet result) throws SQLException
	{
		TopicImpl topic = new TopicImpl(this.jforumForumService);
		topic.setId(result.getInt("topic_id"));
		topic.setForumId(result.getInt("topic_forum_id"));
		topic.setTitle(result.getString("topic_title"));
		topic.setGradeTopic(result.getInt("topic_grade") == Topic.TopicGradableCode.YES.getTopicGradableCode());
		topic.setExportTopic(result.getInt("topic_export") == Topic.TopicExportImportCode.YES.getTopicExportImportCode());
		topic.setStatus(result.getInt("topic_status"));

		if ((result.getDate("topic_start_date") != null) || (result.getDate("topic_end_date") != null) || (result.getDate("topic_allow_until_date") != null))
		{
			AccessDates accessDates = new AccessDatesImpl();
			//accessDates.setOpenDate(result.getTimestamp("topic_start_date"));
			if (result.getDate("topic_start_date") != null)
		    {
		      Timestamp startDate = result.getTimestamp("topic_start_date");
		      accessDates.setOpenDate(startDate);
		      accessDates.setHideUntilOpen(result.getInt("topic_hide_until_open") > 0);
		    }
		    else
		    {
		    	accessDates.setOpenDate(null);
		    }
			accessDates.setDueDate(result.getTimestamp("topic_end_date"));
			/*if (result.getDate("topic_end_date") != null)
		    {
		      Timestamp endDate = result.getTimestamp("topic_end_date");
		      accessDates.setDueDate(endDate);
		      accessDates.setLocked(result.getInt("topic_lock_end_date") > 0);
		    }
		    else
		    {
		    	accessDates.setDueDate(null);
		    }*/
			accessDates.setAllowUntilDate(result.getTimestamp("topic_allow_until_date"));
			topic.setAccessDates(accessDates);
		}
		else
		{
			AccessDates accessDates = new AccessDatesImpl();
			topic.setAccessDates(accessDates);
		}
		return topic;
	}
	
	/**
	 * Creates the forum object from the result set
	 * 
	 * @param result	The result set
	 * 
	 * @return			The forum object
	 * 
	 * @throws SQLException
	 */
	protected Forum fillForum(ResultSet result) throws SQLException
	{
		ForumImpl forum = new ForumImpl();
		forum.setId(result.getInt("forum_id"));
		forum.setDescription(result.getString("forum_desc"));
		forum.setCategoryId(result.getInt("categories_id"));
		forum.setName(result.getString("forum_name"));
		forum.setType(result.getInt("forum_type"));
		forum.setAccessType(result.getInt("forum_access_type"));
		forum.setGradeType(result.getInt("forum_grade_type"));
		
		if ((result.getDate("forum_start_date") != null) || (result.getDate("forum_end_date") != null) || result.getDate("forum_allow_until_date") != null)
		{
			AccessDates accessDates = new AccessDatesImpl();
			//accessDates.setOpenDate(result.getTimestamp("forum_start_date"));
			if (result.getDate("forum_start_date") != null)
		    {
		      Timestamp startDate = result.getTimestamp("forum_start_date");
		      accessDates.setOpenDate(startDate);
		      accessDates.setHideUntilOpen(result.getInt("forum_hide_until_open") > 0);
		    }
		    else
		    {
		    	accessDates.setOpenDate(null);
		    }
			
			accessDates.setDueDate(result.getTimestamp("forum_end_date"));
			/*if (result.getDate("forum_end_date") != null)
		    {
		      Timestamp endDate = result.getTimestamp("forum_end_date");
		      accessDates.setDueDate(endDate);
		     // accessDates.setLocked(result.getInt("forum_lock_end_date") > 0);
		    }
		    else
		    {
		    	accessDates.setDueDate(null);
		    }*/
			accessDates.setAllowUntilDate(result.getTimestamp("forum_allow_until_date"));
			
			forum.setAccessDates(accessDates);
		}
		else
		{
			AccessDates accessDates = new AccessDatesImpl();
			forum.setAccessDates(accessDates);
		}
		return forum;
	}
	
	/**
	 * Creates the topic object from the result set
	 * 
	 * @param result	The result set
	 * 
	 * @return			The topic object
	 * 
	 * @throws SQLException
	 */
	protected Topic fillTopic(ResultSet result) throws SQLException
	{
		TopicImpl topic = new TopicImpl(this.jforumForumService);
		topic.setId(result.getInt("topic_id"));
		topic.setForumId(result.getInt("forum_id"));
		topic.setTitle(result.getString("topic_title"));
		topic.setGradeTopic(result.getInt("topic_grade") == Topic.TopicGradableCode.YES.getTopicGradableCode());
		topic.setExportTopic(result.getInt("topic_export") == Topic.TopicExportImportCode.YES.getTopicExportImportCode());
		topic.setStatus(result.getInt("topic_status"));

		if ((result.getDate("topic_start_date") != null) || (result.getDate("topic_end_date") != null) || (result.getDate("topic_allow_until_date") != null))
		{
			AccessDates accessDates = new AccessDatesImpl();
			//accessDates.setOpenDate(result.getTimestamp("topic_start_date"));
			if (result.getDate("topic_start_date") != null)
		    {
		      Timestamp startDate = result.getTimestamp("topic_start_date");
		      accessDates.setOpenDate(startDate);
		      accessDates.setHideUntilOpen(result.getInt("topic_hide_until_open") > 0);
		    }
		    else
		    {
		    	accessDates.setOpenDate(null);
		    }
			accessDates.setDueDate(result.getTimestamp("topic_end_date"));
			/*if (result.getDate("topic_end_date") != null)
		    {
		      Timestamp endDate = result.getTimestamp("topic_end_date");
		      accessDates.setDueDate(endDate);
		      accessDates.setLocked(result.getInt("topic_lock_end_date") > 0);
		    }
		    else
		    {
		    	accessDates.setDueDate(null);
		    }*/
			accessDates.setAllowUntilDate(result.getTimestamp("topic_allow_until_date"));
			topic.setAccessDates(accessDates);
		}
		else
		{
			AccessDates accessDates = new AccessDatesImpl();
			topic.setAccessDates(accessDates);
		}
		return topic;
	}
	
	/**
	 * Filter forum for the user
	 * 
	 * @param context	Context
	 * 
	 * @param forums	Forums list
	 * 
	 * @param user		Jforum user
	 * 
	 * @param userId	Sakai user id
	 */
	protected void filterUserForums(String context, List<Forum> forums, User user, String userId)
	{
		if (forums == null)
		{
			return;
		}
		
		Date currentTime = Calendar.getInstance().getTime();
		
		Collection<org.sakaiproject.site.api.Group> userGroups = null;
		
		/* Ignore forum start date for forum special access user. 
			Show forums that have special access to the user.*/
		for (Iterator<Forum> forumIter = forums.iterator(); forumIter.hasNext();) 
		{
			boolean specialAccessUser = false, specialAccessUserAccess = false;
			Forum f = forumIter.next();
			
			if (f.getAccessType() == Forum.ForumAccess.DENY.getAccessType())
			{
				forumIter.remove();
				continue;
			}
			
			// forum groups
			if (f.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
			{
				boolean userInGroup = false;
				
				try
				{
					// fetch user groups only once
					if (userGroups == null)
					{
						Site site = siteService.getSite(context);
						userGroups = site.getGroupsWithMember(userId);
					}
					
					for (org.sakaiproject.site.api.Group grp : userGroups) 
					{
						if ((f.getGroups() != null) && (f.getGroups().contains(grp.getId())))
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
						logger.warn("getUserCourseMapItemsByContext: missing site: " + context);
					}
				}
	
				// user not in any group
				if (!userInGroup)
				{
					forumIter.remove();
					continue;
				}
			}
			
			// forum special access
			List<SpecialAccess> specialAccessList = f.getSpecialAccess();
			specialAccessUser = false;
			specialAccessUserAccess = false;
			
			SpecialAccess userSa = null;
			
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
							sa.getAccessDates().setOpenDate(f.getAccessDates().getOpenDate());
						}
						if (!sa.isOverrideEndDate())
						{
							sa.getAccessDates().setDueDate(f.getAccessDates().getDueDate());
						}
						if (!sa.isOverrideLockEndDate())
						{
							sa.getAccessDates().setLocked(f.getAccessDates().isLocked());
						}
						
						if (sa.getAccessDates().getOpenDate() == null)
						{
							specialAccessUserAccess = true;
						}
						else if (currentTime.after(sa.getAccessDates().getOpenDate()))
						{
							specialAccessUserAccess = true;
						}
						break;
					}
				}
			}
			
			f.getSpecialAccess().clear();
			if (specialAccessUser)
			{
				f.getSpecialAccess().add(userSa);
				
				if (specialAccessUserAccess)
				{
					continue;
				}
				else
				{
					forumIter.remove();
					continue;
				}
			}
			
			// forum dates
			if ((f.getAccessDates() != null) && (f.getAccessDates().getOpenDate() != null))
			{
				if (f.getAccessDates().getOpenDate().after(currentTime))
				{
					forumIter.remove();
					continue;
				}
			}
			
			// for gradable forum verify with course map access advisor
			if (f.getGradeType() == Grade.GradeType.FORUM.getType())
			{
				Boolean checkAccess = checkItemAccess(context, JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(f.getId()), userId);
				
				if (!checkAccess)
				{
					f.setBlocked(true);
					f.setBlockedByTitle(getItemAccessMessage(context, JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(f.getId()), userId));
					f.setBlockedByDetails(getItemAccessDetails(context, JForumService.CMItemIdPrefix.FORUM.getCMItemIdPrefix()+ String.valueOf(f.getId()), userId));
				}
			}
		}
			
	}
	
	/**
	 * Checks the user access to topic
	 * 
	 * @param category	Category with forum and topic and its posts
	 * 
	 * @param userId	Sakai user id
	 */
	protected void filterUserForumTopicPosts(Category category, String userId)
	{
		if (category == null)
		{
			return;
		}
		
		boolean categoryForumDates = false;
		
		Date currentTime = Calendar.getInstance().getTime();
		
		User user = jforumUserService.getBySakaiUserId(userId);
		
		// ignore forums with category invalid dates
		if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null) && (category.getAccessDates().getDueDate() != null))
		{
			if (category.getAccessDates().getOpenDate().after(category.getAccessDates().getDueDate()))
			{	
				category.getForums().clear();
				return;
			}
		}
		
		if (category.getForums().size() != 1)
		{
			category.getForums().clear();
			return;
		}
		
		// ignore forums with invalid dates
		Forum forum = category.getForums().get(0);
		if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null) && (forum.getAccessDates().getDueDate() != null))
		{
			if (forum.getAccessDates().getOpenDate().after(forum.getAccessDates().getDueDate()))
			{	
				category.getForums().clear();
				return;
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
			category.getForums().clear();
			return;
		}
		
		// forum groups
		if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
		{
			boolean userInGroup = false;
			
			try
			{
				Site site = siteService.getSite(category.getContext());
				Collection<org.sakaiproject.site.api.Group> userGroups = site.getGroupsWithMember(userId);

				
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
					logger.warn("filterUserForumTopicPosts: missing site: " + category.getContext());
				}
			}

			// user not in any group
			if (!userInGroup)
			{
				category.getForums().clear();
				return;
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
					if (!sa.isOverrideEndDate())
					{
						sa.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
					}
					if (!sa.isOverrideLockEndDate())
					{
						sa.getAccessDates().setLocked(forum.getAccessDates().isLocked());
					}
					
					if (sa.getAccessDates().getOpenDate() == null)
					{
						forumSpecialAccessUserAccess = true;
					}
					else if (currentTime.after(sa.getAccessDates().getOpenDate()))
					{
						forumSpecialAccessUserAccess = true;
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
				
			}
			else
			{
				category.getForums().clear();
				return;
			}
		}
		// forum dates
		else if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null))
		{
			if (forum.getAccessDates().getOpenDate().after(currentTime))
			{
				category.getForums().clear();
				return;
			}
		}
		
		if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null)))
		{
			categoryForumDates = true;
		}
		else if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null)))
		{
			categoryForumDates = true;
		}
		
		if (forum.getTopics().size() != 1)
		{
			forum.getTopics().clear();
			return;
		}
		
		// topics dates if no category or forum dates
		if (!categoryForumDates)
		{
			checkTopicDates(forum, user);
		}
		

		for (Topic topic : forum.getTopics())
		{
			// for gradable topic verify with course map access advisor
			if (topic.isGradeTopic())
			{
				Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), userId);
				
				if (!checkAccess)
				{
					topic.setBlocked(true);
					topic.setBlockedByTitle(getItemAccessMessage(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), userId));
					topic.setBlockedByDetails(getItemAccessDetails(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), userId));
				}
			}
		}
	}
	
	/**
	 * Filters user topics for the user
	 * 
	 * @param forum	Forum
	 */
	protected void filterUserForumTopics(Category category, String userId)
	{
		if (category == null)
		{
			return;
		}
		
		boolean categoryForumDates = false;
		
		Date currentTime = Calendar.getInstance().getTime();
		
		User user = jforumUserService.getBySakaiUserId(userId);
		
		// ignore forums with category invalid dates
		if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null) && (category.getAccessDates().getDueDate() != null))
		{
			if (category.getAccessDates().getOpenDate().after(category.getAccessDates().getDueDate()))
			{	
				category.getForums().clear();
				return;
			}
		}
		
		if (category.getForums().size() != 1)
		{
			return;
		}
		
		// ignore forums with invalid dates
		Forum forum = category.getForums().get(0);
		if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null) && (forum.getAccessDates().getDueDate() != null))
		{
			if (forum.getAccessDates().getOpenDate().after(forum.getAccessDates().getDueDate()))
			{	
				category.getForums().clear();
				return;
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
			category.getForums().clear();
			return;
		}
		
		// forum groups
		if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
		{
			boolean userInGroup = false;
			
			try
			{
				Site site = siteService.getSite(category.getContext());
				Collection<org.sakaiproject.site.api.Group> userGroups = site.getGroupsWithMember(userId);

				
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
				category.getForums().clear();
				return;
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
					if (!sa.isOverrideEndDate())
					{
						sa.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
					}
					if (!sa.isOverrideLockEndDate())
					{
						sa.getAccessDates().setLocked(forum.getAccessDates().isLocked());
					}
					
					if (sa.getAccessDates().getOpenDate() == null)
					{
						forumSpecialAccessUserAccess = true;
					}
					else if (currentTime.after(sa.getAccessDates().getOpenDate()))
					{
						forumSpecialAccessUserAccess = true;
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
				
			}
			else
			{
				category.getForums().clear();
				return;
			}
		}
		// forum dates
		else if ((forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null))
		{
			if (forum.getAccessDates().getOpenDate().after(currentTime))
			{
				category.getForums().clear();
				return;
			}
		}
		
		if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null)))
		{
			categoryForumDates = true;
		}
		else if ((category.getAccessDates() != null) && ((category.getAccessDates().getOpenDate() != null) || (category.getAccessDates().getDueDate() != null)))
		{
			categoryForumDates = true;
		}
		
		// topics dates if no category or forum dates
		if (!categoryForumDates)
		{
			checkTopicDates(forum, user);
		}
		

		for (Topic topic : forum.getTopics())
		{
			// for gradable topic verify with course map access advisor
			if (topic.isGradeTopic())
			{
				Boolean checkAccess = checkItemAccess(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), userId);
				
				if (!checkAccess)
				{
					topic.setBlocked(true);
					topic.setBlockedByTitle(getItemAccessMessage(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), userId));
					topic.setBlockedByDetails(getItemAccessDetails(category.getContext(), JForumService.CMItemIdPrefix.TOPIC.getCMItemIdPrefix()+ String.valueOf(topic.getId()), userId));
				}
			}
		}
		
		// topics read status
		checkUnreadTopics(forum, user, category.getContext());
	}
	
	/**
	 * Gets the gradable forums that are accessible to the participants
	 * 
	 * @param context	Context
	 * 
	 * @return List of gradable forums
	 */
	protected List<Category> getContextAccessibleGradableForums(String context)
	{
		StringBuilder sql;
		sql = new StringBuilder();
		sql.append("SELECT cc.course_id, c.categories_id, c.title AS cat_title, c.gradable AS cat_gradable, c.start_date AS cat_start_date, ");
		// 08/16/2012 - lock on due is not supported anymore
		//sql.append("c.end_date AS cat_end_date, c.lock_end_date AS cat_lock_end_date, c.display_order, f.forum_id, f.forum_name, f.forum_desc, f.forum_type, f.forum_access_type, f.forum_grade_type, ");
		sql.append("c.hide_until_open AS cat_hide_until_open, c.end_date AS cat_end_date, c.allow_until_date as cat_allow_until_date, c.display_order, f.forum_id, f.forum_name, f.forum_desc, f.forum_type, f.forum_access_type, f.forum_grade_type, ");
		sql.append("f.start_date AS forum_start_date, f.hide_until_open AS forum_hide_until_open, f.end_date AS forum_end_date, f.allow_until_date AS forum_allow_until_date, f.forum_order ");
		sql.append(" FROM jforum_sakai_course_categories cc, jforum_categories c, jforum_forums f ");
		sql.append(" WHERE cc.course_id = ? AND cc.categories_id = c.categories_id AND c.categories_id = f.categories_id AND ");
		sql.append(" f.forum_grade_type = "+ Grade.GradeType.FORUM.getType());
		sql.append(" AND f.forum_access_type <> "+ Forum.ForumAccess.DENY.getAccessType());
		
		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] = context;
		
		List<Category> categories = readCategoryForums(sql.toString(), fields);
		
		// forum grades
		List<Grade> forumGrades = this.jforumGradeService.getBySiteByGradeType(context, Grade.GRADE_BY_FORUM);
		Map<Integer, Grade> forumGradesMap = new HashMap<Integer, Grade>();
		
		for(Grade grade : forumGrades)
		{
			forumGradesMap.put(new Integer(grade.getForumId()), grade);
		}
		
		// add grade
		for(Category category : categories)
		{
			if (!category.isGradable())
			{
				for(Forum forum : category.getForums())
				{
					if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
					{
						/*Grade grade = this.jforumGradeService.getByForumId(forum.getId());
						forum.setGrade(grade);*/
						Grade grade = forumGradesMap.get(new Integer(forum.getId()));
						forum.setGrade(grade);
						
						// publish forum future open date and hidden until open grade to gradebook after the open date
						Date now = new Date();
						if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
						{
					    	if (grade != null && grade.getItemOpenDate() != null && grade.getItemOpenDate().before(now))
					    	{
					    		Forum gradableForum = jforumForumService.getForum(forum.getId());
					    		jforumGradeService.updateGradebook(gradableForum.getGrade(), gradableForum);
					    	}
						}
					}
				}
			}
		}
		
		return categories;
	}
	
	/**
	 * Gets the gradable topics for the context and are accessible to the participant
	 * 
	 * @param context	The context
	 * 
	 * @return	List of topics and it's category and forum and are accessible to the participant
	 */
	protected List<Category> getContextAccessibleGradableTopics(String context)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cc.course_id, c.categories_id, c.title AS cat_title, c.gradable AS cat_gradable, c.start_date AS cat_start_date, ");
		// 08/16/2012 - lock on due is not supported anymore
		//sql.append("c.end_date AS cat_end_date, c.lock_end_date AS cat_lock_end_date, c.display_order, f.forum_id, f.forum_name, f.forum_desc, f.forum_type, f.forum_access_type, f.forum_grade_type, ");
		sql.append("c.hide_until_open AS cat_hide_until_open, c.end_date AS cat_end_date, c.allow_until_date as cat_allow_until_date, c.display_order, f.forum_id, f.forum_name, f.forum_desc, f.forum_type, f.forum_access_type, f.forum_grade_type, ");
		sql.append("f.start_date AS forum_start_date, f.hide_until_open AS forum_hide_until_open, f.end_date AS forum_end_date, f.allow_until_date AS forum_allow_until_date, ");
		sql.append("t.topic_id, t.topic_title, t.topic_grade, t.start_date AS topic_start_date, t.hide_until_open AS topic_hide_until_open, t.end_date AS topic_end_date, t.allow_until_date AS topic_allow_until_date, t.topic_status, t.topic_export ");
		sql.append(" FROM jforum_sakai_course_categories cc, jforum_categories c, jforum_forums f, jforum_topics t ");
		sql.append(" WHERE cc.course_id = ? AND cc.categories_id = c.categories_id AND c.categories_id = f.categories_id AND ");
		sql.append(" f.forum_id = t.forum_id AND t.topic_grade = "+ Topic.TopicGradableCode.YES.getTopicGradableCode() +" ");
		sql.append(" AND c.gradable <> 1 ");
		sql.append(" AND f.forum_grade_type <> "+ Grade.GradeType.FORUM.getType());
		sql.append(" AND f.forum_access_type <> "+ Forum.ForumAccess.DENY.getAccessType());
		
		final List<Category> categories = getGradableTopics(context, sql);
		
		return categories;
	}
	
	/**
	 * Gets the gradable categories belong to the context along with forum and topics with dates
	 * 
	 * @param context	Context
	 * 
	 * @return List of gradable categories belong to the context
	 */
	protected List<Category> getContextGradableCategories(String context)
	{
		StringBuilder sql;
		Object[] fields;
		int i;
			
		// site gradable categories
		sql = new StringBuilder();
		/*sql.append("SELECT cc.course_id, c.categories_id, c.title AS cat_title, c.gradable AS cat_gradable, c.start_date AS cat_start_date, ");
		sql.append("c.end_date AS cat_end_date, c.lock_end_date AS cat_lock_end_date, c.display_order ");
		sql.append(" FROM jforum_sakai_course_categories cc, jforum_categories c");
		sql.append(" WHERE cc.course_id = ? AND cc.categories_id = c.categories_id AND c.gradable = 1");*/
		
		// site gradable categories along with forum and topics with dates
		sql.append("SELECT cc.course_id, c.categories_id, c.title AS cat_title, c.gradable AS cat_gradable, ");
		sql.append("c.start_date AS cat_start_date, c.hide_until_open AS cat_hide_until_open, c.end_date AS cat_end_date, c.allow_until_date AS cat_allow_until_date, ");
		sql.append("c.display_order AS display_order, ");
		sql.append("f.forum_id, f.forum_name, f.forum_desc, f.forum_order, f.forum_topics, ");
		sql.append("f.forum_last_post_id, f.moderated, f.start_date AS forum_start_date, f.hide_until_open AS forum_hide_until_open, f.end_date AS forum_end_date, f.allow_until_date as forum_allow_until_date, f.forum_type, ");
		sql.append("f.forum_access_type, f.forum_grade_type, f.lock_end_date AS forum_lock_end_date, ");
		sql.append("t.topic_id, t.forum_id AS topic_forum_id, t.topic_title, t.user_id AS topic_user_id, t.topic_time, t.topic_replies, ");
		sql.append("t.topic_status, t.topic_vote, t.topic_type, t.topic_first_post_id, ");
		sql.append("t.topic_last_post_id, t.moderated AS topic_moderated, t.topic_grade, t.topic_export, t.start_date AS topic_start_date, t.hide_until_open AS topic_hide_until_open, t.end_date AS topic_end_date, t.allow_until_date as topic_allow_until_date, ");
		sql.append("t.lock_end_date AS topic_lock_end_date ");
		sql.append("FROM jforum_sakai_course_categories cc, jforum_categories c ");
		sql.append("LEFT OUTER JOIN jforum_forums f ON f.categories_id = c.categories_id ");
		sql.append("LEFT OUTER JOIN jforum_topics t ON t.forum_id = f.forum_id AND (t.start_date IS NOT NULL OR t.end_date IS NOT NULL OR t.allow_until_date IS NOT NULL) ");
		sql.append("WHERE cc.course_id = ? AND cc.categories_id = c.categories_id AND c.gradable = 1 ");
		sql.append("ORDER BY c.display_order, f.forum_order");
		
		fields = new Object[1];
		i = 0;
		fields[i++] = context;		
		
		// List<Category> categories = readCategories(sql.toString(), fields);
		List<Category> categories = readCategoriesForumsTopics(sql.toString(), fields);
		
		// category grades
		List<Grade> catGrades = this.jforumGradeService.getBySiteByGradeType(context, Grade.GRADE_BY_CATEGORY);
		Map<Integer, Grade> catGradesMap = new HashMap<Integer, Grade>();
		
		for(Grade grade : catGrades)
		{
			catGradesMap.put(new Integer(grade.getCategoryId()), grade);
		}
		
		for(Category category : categories)
		{
			if (category.isGradable())
			{
				Grade grade = catGradesMap.get(new Integer(category.getId()));
				category.setGrade(grade);
				
				// publish to gradebook if the open date of the grade is in the past
				Date now = new Date();
				if ((category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null))		
				{
					if (category.getAccessDates().getOpenDate().before(now))
					{
						if (grade != null && grade.getItemOpenDate() != null)
						{
							this.jforumGradeService.updateGradebook(grade, category);
						}
					}
				}
			}
		}

		return categories;
	}

	/**
	 * Gets the post count per user for the gradable categories
	 * 
	 * @param context	The context
	 * 
	 * @return	The list of categories with post count per user
	 */
	protected List<Category> getContextGradableCategoriesUsersPostCount(String context)
	{
		StringBuilder sql;
		Object[] fields;
		int i;
			
		// site gradable categories with users post count
		sql = new StringBuilder();
		// 08/16/2012 - lock on due is not supported anymore
		//sql.append("SELECT cc.course_id, c.categories_id, c.title AS cat_title, c.gradable AS cat_gradable, c.start_date AS cat_start_date, c.end_date AS cat_end_date, c.lock_end_date AS cat_lock_end_date, s.user_id, COUNT(p.post_id) AS user_posts_count, c.display_order, ");
		sql.append("SELECT cc.course_id, c.categories_id, c.title AS cat_title, c.gradable AS cat_gradable, c.start_date AS cat_start_date, c.hide_until_open AS cat_hide_until_open, c.end_date AS cat_end_date, c.allow_until_date AS cat_allow_until_date, s.user_id, COUNT(p.post_id) AS user_posts_count, c.display_order, ");
		sql.append("u.user_fname AS user_fname,u.user_lname, u.sakai_user_id, u.username ");
		sql.append("FROM  jforum_site_users s, jforum_users u, jforum_posts p, jforum_topics t, ");
		sql.append("jforum_forums f, jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.sakai_site_id = ? AND s.user_id = u.user_id AND s.user_id = p.user_id ");
		sql.append("AND p.topic_id = t.topic_id ");
		sql.append("AND p.forum_id = f.forum_id AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = cc.categories_id ");
		sql.append("AND cc.course_id = ? AND c.gradable = 1 ");
		//sql.append("GROUP BY cc.course_id, c.categories_id, c.title, c.gradable, c.start_date, c.end_date, c.lock_end_date, s.user_id, c.display_order, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
		sql.append("GROUP BY cc.course_id, c.categories_id, c.title, c.gradable, c.start_date, c.hide_until_open, c.end_date, c.allow_until_date, s.user_id, c.display_order, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
		sql.append("ORDER BY c.display_order");
		
		fields = new Object[2];
		i = 0;
		fields[i++] = context;
		fields[i++] = context;
	
		return readCategoriesUserPostCount(sql, fields);
	}
	
	/**
	 * Gets the gradable forums
	 * 
	 * @param context	Context
	 * 
	 * @return List of gradable forums
	 */
	protected List<Category> getContextGradableForums(String context)
	{
		StringBuilder sql = new StringBuilder();
		/*sql.append("SELECT cc.course_id, c.categories_id, c.title AS cat_title, c.gradable AS cat_gradable, c.start_date AS cat_start_date, ");
		sql.append("c.hide_until_open AS cat_hide_until_open, c.end_date AS cat_end_date, c.allow_until_date AS cat_allow_until_date, c.display_order, f.forum_id, f.forum_name, f.forum_desc, f.forum_type, f.forum_access_type, f.forum_grade_type, ");
		sql.append("f.start_date AS forum_start_date, f.hide_until_open AS forum_hide_until_open, f.end_date AS forum_end_date, f.allow_until_date AS forum_allow_until_date, f.forum_order ");
		sql.append(" FROM jforum_sakai_course_categories cc, jforum_categories c, jforum_forums f ");
		sql.append(" WHERE cc.course_id = ? AND cc.categories_id = c.categories_id AND c.categories_id = f.categories_id AND ");
		sql.append(" f.forum_grade_type = "+ Grade.GradeType.FORUM.getType());*/
		//sql.append(" AND f.forum_access_type <> "+ Forum.ACCESS_DENY);
		
		// site gradable forums along with topics with dates
		sql.append("SELECT cc.course_id, c.categories_id, c.title AS cat_title, c.gradable AS cat_gradable, ");
		sql.append("c.start_date AS cat_start_date, c.hide_until_open AS cat_hide_until_open, c.end_date AS cat_end_date, c.allow_until_date AS cat_allow_until_date, ");
		sql.append("c.display_order AS display_order, ");
		sql.append("f.forum_id, f.forum_name, f.forum_desc, f.forum_order, f.forum_topics, ");
		sql.append("f.forum_last_post_id, f.moderated, f.start_date AS forum_start_date, f.hide_until_open AS forum_hide_until_open, f.end_date AS forum_end_date, f.allow_until_date as forum_allow_until_date, f.forum_type, ");
		sql.append("f.forum_access_type, f.forum_grade_type, f.lock_end_date AS forum_lock_end_date, ");
		sql.append("t.topic_id, t.forum_id AS topic_forum_id, t.topic_title, t.user_id AS topic_user_id, t.topic_time, t.topic_replies, ");
		sql.append("t.topic_status, t.topic_vote, t.topic_type, t.topic_first_post_id, ");
		sql.append("t.topic_last_post_id, t.moderated AS topic_moderated, t.topic_grade, t.topic_export, t.start_date AS topic_start_date, t.hide_until_open AS topic_hide_until_open, t.end_date AS topic_end_date, t.allow_until_date as topic_allow_until_date, ");
		sql.append("t.lock_end_date AS topic_lock_end_date ");
		sql.append("FROM jforum_sakai_course_categories cc, jforum_categories c ");
		sql.append("LEFT OUTER JOIN jforum_forums f ON f.categories_id = c.categories_id ");
		sql.append("LEFT OUTER JOIN jforum_topics t ON t.forum_id = f.forum_id AND (t.start_date IS NOT NULL OR t.end_date IS NOT NULL OR t.allow_until_date IS NOT NULL) ");
		sql.append("WHERE cc.course_id = ? AND cc.categories_id = c.categories_id ");
		sql.append("AND f.forum_grade_type = "+ Grade.GradeType.FORUM.getType());
		sql.append(" ORDER BY c.display_order, f.forum_order");
		
		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] = context;
		
		//List<Category> categories = readCategoryForums(sql.toString(), fields);
		List<Category> categories = readCategoriesForumsTopics(sql.toString(), fields);
		
		// forum grades
		List<Grade> forumGrades = this.jforumGradeService.getBySiteByGradeType(context, Grade.GRADE_BY_FORUM);
		Map<Integer, Grade> forumGradesMap = new HashMap<Integer, Grade>();
		
		for(Grade grade : forumGrades)
		{
			forumGradesMap.put(new Integer(grade.getForumId()), grade);
		}
		
		// add grade
		for(Category category : categories)
		{
			if (!category.isGradable())
			{
				for(Forum forum : category.getForums())
				{
					if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
					{
						/*
						Grade grade = this.jforumGradeService.getByForumId(forum.getId());
						forum.setGrade(grade);
						*/
						Grade grade = forumGradesMap.get(new Integer(forum.getId()));
						forum.setGrade(grade);
						
						// publish forum future open date and hidden until open grade to gradebook after the open date
						Date now = new Date();
						if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
						{
					    	if (grade != null && grade.getItemOpenDate() != null && grade.getItemOpenDate().before(now))
					    	{
					    		Forum gradableForum = jforumForumService.getForum(forum.getId());
					    		jforumGradeService.updateGradebook(gradableForum.getGrade(), gradableForum);
					    	}
						}
					}
				}
			}
		}
		
		return categories;
	}

	/**
	 * Gets the users post count for the forums
	 * 
	 * @param context	The context
	 * 
	 * @return The list of categories with post count per user for the forum
	 */
	protected List<Category> getContextGradableForumUsersPostCount(String context)
	{
		StringBuilder sql = new StringBuilder();;
		Object[] fields;
		int i;
		
		sql.append("SELECT cc.course_id, c.categories_id AS categories_id, c.title AS cat_title, c.gradable AS cat_gradable, ");
		//sql.append("c.start_date AS cat_start_date, c.end_date AS cat_end_date, c.lock_end_date AS cat_lock_end_date, c.display_order, ");
		sql.append("c.start_date AS cat_start_date, c.hide_until_open AS cat_hide_until_open, c.end_date AS cat_end_date, c.allow_until_date as cat_allow_until_date, c.display_order, ");
		sql.append("f.forum_id, f.forum_name,f.forum_desc, f.forum_type, f.forum_access_type, f.forum_grade_type, f.start_date AS forum_start_date, f.hide_until_open AS forum_hide_until_open, f.end_date AS forum_end_date, f.allow_until_date AS forum_allow_until_date, f.forum_order, ");
		sql.append("s.user_id, COUNT(p.post_id) AS user_posts_count, u.user_fname AS user_fname,u.user_lname, u.sakai_user_id, u.username ");
		sql.append("FROM  jforum_site_users s, jforum_users u, jforum_posts p, jforum_topics t, ");
		sql.append("jforum_forums f,jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.sakai_site_id = ? ");
		sql.append("AND s.user_id = u.user_id AND s.user_id = p.user_id ");
		sql.append("AND p.topic_id = t.topic_id ");
		sql.append("AND p.forum_id = f.forum_id AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = cc.categories_id ");
		sql.append("AND cc.course_id = ? ");
		sql.append("AND (f.forum_grade_type = 2 ) ");
		//sql.append("GROUP BY cc.course_id, c.categories_id, c.title, c.gradable, c.start_date, c.end_date, c.lock_end_date, c.display_order, ");
		sql.append("GROUP BY cc.course_id, c.categories_id, c.title, c.gradable, c.start_date, c.hide_until_open, c.end_date, c.allow_until_date, c.display_order, ");
		sql.append("f.forum_id, f.forum_name, f.forum_desc, f.forum_order,f.forum_type, f.forum_access_type, f.forum_grade_type, f.start_date, f.hide_until_open, f.end_date, f.allow_until_date, f.forum_order, ");
		sql.append("s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
		sql.append("ORDER BY c.display_order, f.forum_order ");
		
		fields = new Object[2];
		i = 0;
		fields[i++] = context;
		fields[i++] = context;
		
		List<Category> categories = readForumsUserPostsCount(sql, fields);
		
		return categories;
	}

	/**
	 * Gets the gradable topics for the context
	 * 
	 * @param context	The context
	 * 
	 * @return	List of toipics and it's category and forum
	 */
	protected List<Category> getContextGradableTopics(String context)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cc.course_id, c.categories_id, c.title AS cat_title, c.gradable AS cat_gradable, c.start_date AS cat_start_date, ");
		//sql.append("c.end_date AS cat_end_date, c.lock_end_date AS cat_lock_end_date, c.display_order, f.forum_id, f.forum_name, f.forum_desc, f.forum_type, f.forum_access_type, f.forum_grade_type, ");
		sql.append("c.hide_until_open AS cat_hide_until_open, c.end_date AS cat_end_date, c.allow_until_date AS cat_allow_until_date, c.display_order, f.forum_id, f.forum_name, f.forum_desc, f.forum_type, f.forum_access_type, f.forum_grade_type, ");
		sql.append("f.start_date AS forum_start_date, f.hide_until_open AS forum_hide_until_open, f.end_date AS forum_end_date, f.allow_until_date AS forum_allow_until_date, ");
		sql.append("t.topic_id, t.topic_title, t.topic_grade, t.start_date AS topic_start_date, t.hide_until_open AS topic_hide_until_open, t.end_date AS topic_end_date, t.allow_until_date AS topic_allow_until_date, t.topic_status, t.topic_export ");
		sql.append(" FROM jforum_sakai_course_categories cc, jforum_categories c, jforum_forums f, jforum_topics t ");
		sql.append(" WHERE cc.course_id = ? AND cc.categories_id = c.categories_id AND c.categories_id = f.categories_id AND ");
		sql.append(" f.forum_id = t.forum_id AND t.topic_grade = "+ Topic.TopicGradableCode.YES.getTopicGradableCode() +" ");
		sql.append(" AND c.gradable <> 1 ");
		sql.append(" AND f.forum_grade_type <> "+ Grade.GradeType.FORUM.getType());
		//sql.append(" AND f.forum_access_type <> "+ Forum.ACCESS_DENY);
		
		final List<Category> categories = getGradableTopics(context, sql);
		
		return categories;
	}

	/**
	 * Gets the post count per user for the topics
	 * 
	 * @param context	The context
	 * 
	 * @return The list of categories with user post count for the topics
	 */
	protected List<Category> getContextGradableTopicsUsersPostCount(String context)
	{
		StringBuilder sql = new StringBuilder();;
		Object[] fields;
		int i;
		
		sql.append("SELECT cc.course_id, c.categories_id AS categories_id, c.title AS cat_title, c.gradable AS cat_gradable, ");
		//sql.append("c.start_date AS cat_start_date, c.end_date AS cat_end_date, c.lock_end_date AS cat_lock_end_date, c.display_order, ");
		sql.append("c.start_date AS cat_start_date, c.hide_until_open AS cat_hide_until_open, c.end_date AS cat_end_date, c.allow_until_date AS cat_allow_until_date, c.display_order, ");
		sql.append("f.forum_id, f.forum_name, f.forum_desc, f.forum_type, f.forum_access_type, f.forum_grade_type, f.start_date AS forum_start_date, f.hide_until_open AS forum_hide_until_open, f.end_date AS forum_end_date, f.allow_until_date AS forum_allow_until_date, f.forum_order, "); 
		sql.append("t.topic_id, t.topic_title, topic_grade, topic_export, t.start_date AS topic_start_date, t.hide_until_open AS topic_hide_until_open, t.end_date AS topic_end_date, t.allow_until_date AS topic_allow_until_date, t.topic_status, ");
		sql.append("s.user_id, COUNT(p.post_id) AS user_posts_count, u.user_fname AS user_fname,u.user_lname, u.sakai_user_id, u.username ");
		sql.append("FROM  jforum_site_users s, jforum_users u, jforum_posts p, jforum_topics t, ");
		sql.append("jforum_forums f,jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.sakai_site_id = ? ");
		sql.append("AND s.user_id = u.user_id AND s.user_id = p.user_id ");
		sql.append("AND p.topic_id = t.topic_id ");
		sql.append("AND p.forum_id = f.forum_id AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = cc.categories_id ");
		sql.append("AND cc.course_id = ? ");
		sql.append("AND t.topic_grade = "+ Topic.TopicGradableCode.YES.getTopicGradableCode() +" ");
		//sql.append("GROUP BY cc.course_id, c.categories_id, c.title, c.gradable, c.start_date, c.end_date, c.lock_end_date, c.display_order, ");
		sql.append("GROUP BY cc.course_id, c.categories_id, c.title, c.gradable, c.start_date, c.hide_until_open, c.end_date, c.allow_until_date, c.display_order, ");
		sql.append("f.forum_id, f.forum_name, f.forum_desc, f.forum_order,f.forum_type, f.forum_access_type, f.forum_grade_type, f.start_date, f.hide_until_open, f.end_date, f.allow_until_date, f.forum_order, ");
		sql.append("t.topic_id, t.topic_title, topic_grade, topic_export, t.start_date, t.hide_until_open, t.end_date, t.allow_until_date, t.topic_status, ");
		sql.append("s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
		sql.append("ORDER BY c.display_order, f.forum_order ");
		
		fields = new Object[2];
		i = 0;
		fields[i++] = context;
		fields[i++] = context;
		
		List<Category> categories = readTopicsUserPostCount(sql, fields);
		
		return categories;
	}
	
	/**
	 * Gets the categories 
	 * 
	 * @param sql	Query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return	List of Categories
	 */
	/*protected List<Category> readCategories(String sql, Object[] fields)
	{
		if (sql == null) throw new IllegalArgumentException();
		if (fields == null) throw new IllegalArgumentException();
		
		final List<Category> categories =  new ArrayList<Category>();
		
		this.sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Category category = fillCategory(result);
					
					categories.add(category);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readCategories: " + e, e);
					}
					return null;
				}
			}
		});
		
		return categories;
	}*/

	/**
	 * gets the gradable topics
	 * 
	 * @param context	Context
	 * 
	 * @param sql	Query
	 * 
	 * @return	The list of gradable topics belong to the context
	 */
	protected List<Category> getGradableTopics(String context, StringBuilder sql)
	{
		Object[] fields;
		int i;
		final List<Category> categories =  new ArrayList<Category>();
		final Map<Integer, Category> categoriesMap =  new HashMap<Integer, Category>();
		final Map<Integer, Forum> forumMap =  new HashMap<Integer, Forum>();
		
		fields = new Object[1];
		i = 0;
		fields[i++] = context;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Category category;
					boolean categoryExisting = false;
					boolean forumExisting = false;
					
					int categoryId = result.getInt("categories_id");
					if (categoriesMap.containsKey(categoryId))
					{
						category = categoriesMap.get(categoryId);
						categoryExisting = true;
					}
					else
					{
						category = fillCategory(result);
						categoriesMap.put(categoryId, category);						
					}
					
					if (result.getObject("forum_id") != null)
					{
						Forum forum;
						int forumId = result.getInt("forum_id");
					
						if (forumMap.containsKey(forumId))
						{
							forum = forumMap.get(forumId);
							forumExisting = true;
						}
						else
						{
							forum = fillForum(result);
							forumMap.put(forumId, forum);
						}				
						
						if (result.getObject("topic_id") != null)
						{
							Topic topic = fillTopic(result);
							forum.getTopics().add(topic);
						}
						
						if (!forumExisting)
						{
							category.getForums().add(forum);
						}
					}			
							
					if (!categoryExisting)
					{
						categories.add(category);
					}

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getGradableTopics: " + e, e);
					}
					return null;
				}
			}
		});
		
		// topic grades
		List<Grade> topicGrades = this.jforumGradeService.getBySiteByGradeType(context, Grade.GRADE_BY_TOPIC);
		Map<Integer, Grade> topicGradesMap = new HashMap<Integer, Grade>();
		
		for(Grade grade : topicGrades)
		{
			topicGradesMap.put(new Integer(grade.getTopicId()), grade);
		}
		
		// add grade
		for(Category category : categories)
		{
			if (!category.isGradable())
			{
				for(Forum forum : category.getForums())
				{
					if (forum.getGradeType() == Grade.GradeType.TOPIC.getType())
					{
						for (Topic topic : forum.getTopics())
						{
							if (topic.isGradeTopic())
							{
								/*Grade grade = this.jforumGradeService.getByForumTopicId(topic.getForumId(), topic.getId());
								topic.setGrade(grade);*/
								Grade grade = topicGradesMap.get(new Integer(topic.getId()));
								topic.setGrade(grade);
								
								// publish forum future open date and hidden until open grade to gradebook after the open date
								Date now = new Date();
								if (grade != null && grade.getItemOpenDate() != null && grade.getItemOpenDate().before(now))
						    	{
						    		jforumGradeService.updateGradebook(grade, topic);
						    	}
							}
						}						
					}
				}
			}
		}
		return categories;
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
	 * Checks user unread topics for a forum
	 *  
	 * @param siteId	Site id
	 * 
	 * @param forum		Forum
	 * 
	 * @param user		JForum user
	 * 
	 * @return	true - if there are unread topics
	 * 			false - if there are no unread topics
	 */
	protected boolean isUserHasUnreadTopics(String siteId, Forum forum, User user)
	{
	    if (user == null)
		{
			forum.setUnread(true);
			return true;
		}
	    
		// check marked unread topics
		int markedUnreadTopicCount  = jforumPostService.getMarkedTopicsCountByForum(forum.getId(), user.getId());
		
		if (markedUnreadTopicCount > 0)
		{
			forum.setUnread(true);
			return true;
		}
		
		Date currentTime = Calendar.getInstance().getTime();
		
		// marked all read time
		Date markAllReadTime = jforumUserService.getMarkAllReadTime(siteId, user.getId());
		boolean facilitator = jforumSecurityService.isJForumFacilitator(siteId, user.getSakaiUserId());
		
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
			return true;
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
					break;
				}
				
				if (lastVistiedTopic.getTime().before(lastPosttimeTopic.getTime()))
				{
					forum.setUnread(true);
					break;
				}
			}
			else
			{
				forum.setUnread(true);
				break;
			}
		}
		
		if (forum.isUnread())
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Reads the categories and it's forums and topics
	 * 
	 * @param sql		The query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return	The categories and it's forums and topics
	 */
	protected List<Category> readCategoriesForumsTopics(String sql, Object[] fields)
	{
		if (sql == null) throw new IllegalArgumentException();
		if (fields == null) throw new IllegalArgumentException();
		
		final Map<Integer, Category> categoriesMap =  new HashMap<Integer, Category>();
		final Map<Integer, Forum> forumsMap =  new HashMap<Integer, Forum>();
		
		final List<Category> categories =  new ArrayList<Category>();
		
		this.sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Category category;
					Forum forum;
					
					// category
					int categoryId = result.getInt("categories_id");
					if (categoriesMap.containsKey(categoryId))
					{
						category = categoriesMap.get(categoryId);						
					}
					else
					{
						category = fillCategory(result);
						//category.setOrder(result.getInt("cat_display_order"));
						
						categoriesMap.put(categoryId, category);
						categories.add(category);
					}
					
					// forum
					if (result.getObject("forum_id") != null)
					{	
						int forumId = result.getInt("forum_id");
						if (forumsMap.containsKey(forumId))
						{
							forum = forumsMap.get(forumId);						
						}
						else
						{
							forum = fillCategoryForum(result);						
							category.getForums().add(forum);
							forumsMap.put(forum.getId(), forum);
						}
						
						// topic
						if (result.getObject("topic_id") != null)
						{
							Topic topic = fillCategoryForumTopic(result);
							forum.getTopics().add(topic);
						}
					}
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readCategoriesForumsTopics: " + e, e);
					}
					return null;
				}
			}
		});
		
		return categories;
	}
	
	/**
	 * Reads the categories post count per user from the database
	 * 
	 * @param sql		The query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return	The list of categories with post count per user
	 */
	protected List<Category> readCategoriesUserPostCount(StringBuilder sql, Object[] fields)
	{
		final Map<Integer, Category> categoriesMap =  new HashMap<Integer, Category>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{	
					int currCatId = result.getInt("categories_id");
					if (categoriesMap.size() == 0)
					{
						Category category = fillCategory(result);
						category.setOrder(result.getInt("display_order"));
						category.getUserPostCount().put(result.getString("sakai_user_id"), Integer.valueOf(result.getInt("user_posts_count")));
						categoriesMap.put(Integer.valueOf(category.getId()), category);
					}
					else
					{
						if (categoriesMap.containsKey(Integer.valueOf(currCatId)))
						{
							Category category = categoriesMap.get(Integer.valueOf(currCatId));
							category.getUserPostCount().put(result.getString("sakai_user_id"), Integer.valueOf(result.getInt("user_posts_count")));
						}
						else
						{
							Category category = fillCategory(result);
							category.setOrder(result.getInt("display_order"));
							category.getUserPostCount().put(result.getString("sakai_user_id"), Integer.valueOf(result.getInt("user_posts_count")));
							categoriesMap.put(Integer.valueOf(category.getId()), category);
						}
					}
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readCategoriesUserPostCount: " + e, e);
					}
					return null;
				}
			}
		});
		
		List<Category> categories =  new ArrayList<Category>();
		for (Category category : categoriesMap.values()) 
		{
			categories.add(category);
		}
		
		return categories;
	}
	
	/**
	 * Get forums
	 * 
	 * @param sql	The query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return List of gradable forums belong to the context
	 */
	protected List<Category> readCategoryForums(String sql, Object[] fields)
	{
		final List<Category> categories =  new ArrayList<Category>();
		final Map<Integer, Category> categoriesMap =  new HashMap<Integer, Category>();
				
		this.sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Category category;
					boolean categoryExisting = false;
					int categoryId = result.getInt("categories_id");
					if (categoriesMap.containsKey(categoryId))
					{
						categoryExisting = true;
						category = categoriesMap.get(categoryId);						
					}
					else
					{
						category = fillCategory(result);
						category.setOrder(result.getInt("display_order"));
						categoriesMap.put(categoryId, category);
					}
										
					if (result.getObject("forum_id") != null)
					{
					
						Forum forum = fillForum(result);
						forum.setOrder(result.getInt("forum_order"));
						category.getForums().add(forum);
					}
					
					if (!categoryExisting)
					{
						categories.add(category);
					}

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readCategoryForums: " + e, e);
					}
					return null;
				}
			}

			
		});		
		
		return categories;
	}
	
	/**
	 * Gets the forums 
	 * 
	 * @param sql	Query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return	List of forums
	 */
	protected List<Forum> readForums(String sql, Object[] fields)
	{
		if (sql == null) throw new IllegalArgumentException();
		if (fields == null) throw new IllegalArgumentException();
		
		final List<Forum> forums =  new ArrayList<Forum>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					ForumImpl forum = new ForumImpl();
					forum.setId(result.getInt("forum_id"));
					forum.setCategoryId(result.getInt("categories_id"));
					forum.setName(result.getString("forum_name"));
					forum.setType(result.getInt("forum_type"));
					forum.setAccessType(result.getInt("forum_access_type"));
					forum.setGradeType(result.getInt("forum_grade_type"));
					
					if ((result.getDate("forum_start_date") != null) || (result.getDate("forum_end_date") != null))
					{
						AccessDates accessDates = new AccessDatesImpl();
						accessDates.setOpenDate(result.getTimestamp("forum_start_date"));
						if (result.getDate("forum_end_date") != null)
					    {
					      Timestamp endDate = result.getTimestamp("forum_end_date");
					      accessDates.setDueDate(endDate);
					      accessDates.setLocked(result.getInt("forum_lock_end_date") > 0);
					    }
					    else
					    {
					    	accessDates.setDueDate(null);
					    }
						forum.setAccessDates(accessDates);
					}
					forums.add(forum);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readForums: " + e, e);
					}
					return null;
				}
			}
		});
		
		return forums;
	}
	
	/**
	 * Reads the forums post count per user from the database
	 * 
	 * @param sql		The query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return	The list of categories with forums post count per user
	 */
	protected List<Category> readForumsUserPostsCount(StringBuilder sql, Object[] fields)
	{
		final Map<Integer, Category> categoriesMap =  new HashMap<Integer, Category>();
		final Map<Integer, Forum> forumMap =  new HashMap<Integer, Forum>();
				
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Category category;
					boolean forumExisting = false;
					
					int categoryId = result.getInt("categories_id");
					if (categoriesMap.containsKey(categoryId))
					{
						category = categoriesMap.get(categoryId);						
					}
					else
					{
						category = fillCategory(result);
						category.setOrder(result.getInt("display_order"));
						categoriesMap.put(categoryId, category);
					}
										
					if (result.getObject("forum_id") != null)
					{
						Forum forum = null;
						int forumId = result.getInt("forum_id");
						
						if (forumMap.containsKey(forumId))
						{
							forumExisting = true;
							forum = forumMap.get(forumId);
						}
						else
						{
							forum = fillForum(result);
							forum.setOrder(result.getInt("forum_order"));
							forumMap.put(forumId, forum);
						}
						
						forum.getUserPostCount().put(result.getString("sakai_user_id"), Integer.valueOf(result.getInt("user_posts_count")));
						if (!forumExisting)
						{
							category.getForums().add(forum);
						}
					}

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readForumsUserPostsCount: " + e, e);
					}
					return null;
				}
			}			
		});
		
		List<Category> categories =  new ArrayList<Category>();
		for (Category category : categoriesMap.values()) 
		{
			categories.add(category);
		}
		return categories;
	}
	
	/**
	 * Reads the topics post count per user from the database
	 * 
	 * @param sql		The query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return	The list of categories with topics post count per user
	 */
	protected List<Category> readTopicsUserPostCount(StringBuilder sql, Object[] fields)
	{
		final Map<Integer, Category> categoriesMap =  new HashMap<Integer, Category>();
		final Map<Integer, Forum> forumMap =  new HashMap<Integer, Forum>();
		final Map<Integer, Topic> topicMap =  new HashMap<Integer, Topic>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Category category;
					boolean forumExisting = false;
					boolean topicExisting = false;
					
					int categoryId = result.getInt("categories_id");
					if (categoriesMap.containsKey(categoryId))
					{
						category = categoriesMap.get(categoryId);						
					}
					else
					{
						category = fillCategory(result);
						category.setOrder(result.getInt("display_order"));
						categoriesMap.put(categoryId, category);
					}
										
					if (result.getObject("forum_id") != null)
					{
						Forum forum = null;
						int forumId = result.getInt("forum_id");
						
						if (forumMap.containsKey(forumId))
						{
							forumExisting = true;
							forum = forumMap.get(forumId);
						}
						else
						{
							forum = fillForum(result);
							forum.setOrder(result.getInt("forum_order"));
							forumMap.put(forumId, forum);
						}
						
						forum.getUserPostCount().put(result.getString("sakai_user_id"), Integer.valueOf(result.getInt("user_posts_count")));
						if (!forumExisting)
						{
							category.getForums().add(forum);
						}
						
						if (result.getObject("topic_id") != null)
						{
							int topicId = result.getInt("topic_id");
							Topic topic = null;
							
							if (topicMap.containsKey(topicId))
							{
								topicExisting = true;
								topic = topicMap.get(topicId);
							}
							else
							{
								topic = fillTopic(result);
								topicMap.put(topicId, topic);
							}
							
							topic.getUserPostCount().put(result.getString("sakai_user_id"), Integer.valueOf(result.getInt("user_posts_count")));
							if (!topicExisting)
							{
								forum.getTopics().add(topic);
							}
						}
					}

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readTopicsUserPostCount: " + e, e);
					}
					return null;
				}
			}
		});
		
		List<Category> categories =  new ArrayList<Category>();
		for (Category category : categoriesMap.values()) 
		{
			categories.add(category);
		}
		return categories;
	}
}
