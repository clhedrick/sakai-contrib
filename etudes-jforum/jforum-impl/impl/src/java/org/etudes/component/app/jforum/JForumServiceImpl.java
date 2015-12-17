/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumServiceImpl.java $ 
 * $Id: JForumServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.Evaluation;
import org.etudes.api.app.jforum.Forum;
import org.etudes.api.app.jforum.Grade;
import org.etudes.api.app.jforum.JForumCategoryService;
import org.etudes.api.app.jforum.JForumForumService;
import org.etudes.api.app.jforum.JForumService;
import org.etudes.api.app.jforum.JForumSpecialAccessService;
import org.etudes.api.app.jforum.LastPostInfo;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.SpecialAccess;
import org.etudes.api.app.jforum.Topic;
import org.etudes.api.app.jforum.User;
import org.etudes.util.api.AccessAdvisor;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;

public class JForumServiceImpl implements JForumService
{
	private static Log logger = LogFactory.getLog(JForumServiceImpl.class);
	
	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	/** Dependency: SiteService. */
	protected SiteService siteService = null;
	
	/** Dependency: JForumCategoryService */
	protected JForumCategoryService jforumCategoryService = null;
	
	/** Dependency: JForumForumService */	
	protected JForumForumService jforumForumService = null;
	
	/** Dependency: JForumSpecialAccessService */
	protected JForumSpecialAccessService jforumSpecialAccessService = null;
	
	/** Dependency (optional, self-injected): AccessAdvisor. */
	protected transient AccessAdvisor accessAdvisor = null;
	
	public void init()
	{
		if (logger.isInfoEnabled())
			logger.info("init....");
		
		// check if there is an access advisor - if not, that's ok.
		this.accessAdvisor = (AccessAdvisor) ComponentManager.get(AccessAdvisor.class);
	}

	public void destroy()
	{
		if (logger.isInfoEnabled())
			logger.info("destroy....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Category> getGradableItemsByContext(String context)
	{
		if (context == null) throw new IllegalArgumentException();
		
		/*get categories, forums and topics that are gradable and resuable topics*/
		return this.jforumCategoryService.getContextGradableCategoriesForumsTopics(context);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Category> getUserGradableItemsByContext(String context, String userId)
	{
		/*get categories, forums and topics that are gradable and resuable topics. Check the dates and user special access for forums and topics*/
		List<Category> categories = this.jforumCategoryService.getContextGradableCategoriesForumsTopics(context);
		
		getUserGradableItems(context, userId, categories, false);

		return categories;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Category> getUserFilteredGradableItemsByContext(String context, String userId)
	{
		/*get categories, forums and topics that are gradable and resuable topics but not deny access forums and it's topics. 
		 * Check the dates and user special access for forums and topics*/
		List<Category> categories = this.jforumCategoryService.getFilteredContextGradableCategoriesForumsTopics(context);
		
		getUserGradableItems(context, userId, categories, true);

		return categories;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getCategory(int categoryId)
	{
		return this.jforumCategoryService.getCategory(categoryId);
	}

	/**
	 * {@inheritDoc}
	 */
	public Category getCategoryForum(int forumId)
	{
		return this.jforumCategoryService.getCategoryForum(forumId);
	}

	/**
	 * {@inheritDoc}
	 */
	public Category getCategoryForumTopic(int topicId)
	{
		return this.jforumCategoryService.getCategoryForumTopic(topicId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateCategoryDates(Category category)
	{
		jforumCategoryService.updateDates(category, Type.CATEGORY);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateForumDates(Category category)
	{
		jforumCategoryService.updateDates(category, Type.FORUM);		
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateTopicDates(Category category)
	{
		jforumCategoryService.updateDates(category, Type.TOPIC);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean checkItemAccess(String context, String id, String userId)
	{
		if (this.accessAdvisor == null)
			return Boolean.TRUE;
		
		return !accessAdvisor.denyAccess("sakai.jforum", context, id, userId);
		//String blockedByTitle = accessAdvisor.message(SakaiSystemGlobals.getValue("etudes.jforum.sakai.jforum.tool.id"),  ToolManager.getCurrentPlacement().getContext(), String.valueOf(c.getId()), us.getSakaiUserId());

	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, Integer> getContextGradableItemsPostsCount(String context)
	{
		return fetchContextGradableItemsPostsCount(context);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, Integer> getContextUserPostsCount(String context)
	{
		return fetchContextPostsCount(context);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Category> getGradableItemsUserPostsCountByContext(String context)
	{

		return jforumCategoryService.getContextGradableCategoriesForumsTopicsUsersPostCount(context);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getUsersPostCountByCategory(String context, int categoryId)
	{
		return jforumCategoryService.getCategoryUsersPostCount(context, categoryId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getUsersPostCountByForum(String context, int forumId)
	{
		return jforumCategoryService.getForumUsersPostCount(context, forumId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getUsersPostCountByTopic(String context, int topicId)
	{
		return jforumCategoryService.getTopicUsersPostCount(context, topicId);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public String getItemAccessMessage(String context, String id, String userId)
	{
		if (this.accessAdvisor == null)
			return null;
		
		 return accessAdvisor.message("sakai.jforum", context, id, userId);

	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getItemAccessDetails(String context, String id, String userId)
	{
		if (this.accessAdvisor == null)
			return null;
		
		return accessAdvisor.details("sakai.jforum", context, id, userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getEvaluationsByCategory(String context, int categoryId)
	{
		if (context == null) throw new IllegalArgumentException();
		
		/*get category evaluations*/
		return this.jforumCategoryService.getCategoryEvaluations(context, categoryId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Category getEvaluationsByForum(String context, int forumId)
	{
		if (context == null) throw new IllegalArgumentException();
		
		/*get forum evaluations*/
		return this.jforumCategoryService.getForumEvaluations(context, forumId);
	}

	/**
	 * {@inheritDoc}
	 */
	public Category getEvaluationsByTopic(String context, int topicId)
	{
		/*get topic evaluations*/
		return this.jforumCategoryService.getTopicEvaluations(context, topicId);
	}
		

	/**
	 * Gets the user gradable items that are accessible
	 * 
	 * @param context	Context
	 * 
	 * @param userId	User id
	 * 
	 * @param user		User
	 * 
	 * @param	validateDates 	true	-	validate dates
	 * 							false	- ignore dates validation
	 * 
	 * @param categories
	 */
	protected void getUserGradableItems(String context, String userId, List<Category> categories, boolean validateDates)
	{
		User user = getJforumUser(userId);
		
		Date currentTime = Calendar.getInstance().getTime();
		
		// special access in the site
		List<SpecialAccess> siteSpecialAccessList = this.jforumSpecialAccessService.getBySite(context);
		
		Map<Integer, List<SpecialAccess>> forumSpecialAccessMap = new HashMap<Integer, List<SpecialAccess>>();
		Map<Integer, List<SpecialAccess>> topicSpecialAccessMap = new HashMap<Integer, List<SpecialAccess>>();
		
		for (SpecialAccess specialAccess : siteSpecialAccessList)
		{
			if (specialAccess.getForumId() > 0 && specialAccess.getTopicId() == 0)
			{
				if (forumSpecialAccessMap.get(specialAccess.getForumId()) == null)
				{
					List<SpecialAccess> forumSpecialAccessList = new ArrayList<SpecialAccess>();
					forumSpecialAccessList.add(specialAccess);
					forumSpecialAccessMap.put(specialAccess.getForumId(), forumSpecialAccessList);
				}
				else
				{
					List<SpecialAccess> forumSpecialAccessList = forumSpecialAccessMap.get(specialAccess.getForumId());
					forumSpecialAccessList.add(specialAccess);
					forumSpecialAccessMap.put(specialAccess.getForumId(), forumSpecialAccessList);
				}
			}
			else if (specialAccess.getForumId() > 0 && specialAccess.getTopicId() > 0)
			{
				if (topicSpecialAccessMap.get(specialAccess.getTopicId()) == null)
				{
					List<SpecialAccess> topicSpecialAccessList = new ArrayList<SpecialAccess>();
					topicSpecialAccessList.add(specialAccess);
					topicSpecialAccessMap.put(specialAccess.getTopicId(), topicSpecialAccessList);
				}
				else
				{
					List<SpecialAccess> topicSpecialAccessList = topicSpecialAccessMap.get(specialAccess.getTopicId());
					topicSpecialAccessList.add(specialAccess);
					topicSpecialAccessMap.put(specialAccess.getTopicId(), topicSpecialAccessList);
				}
			}
		}
		
		// site forum groups
		Map<Integer, List<String>> siteForumGroups = null;
		
		Collection<org.sakaiproject.site.api.Group> userGroups = null;
		
		// user evaluations
		List<Evaluation> userSiteEvaluations = null;
		Map<Integer, Evaluation> userSiteEvaluationsMap = null;
				
		/*user posts count*/
		Map<Integer, Integer> userSiteCatPostCountMap = null;
		Map<Integer, Integer> userSiteForumPostCountMap = null;
		Map<Integer, Integer> userSiteTopicPostCountMap = null;
		
		/*user recent posts*/
		Map<Integer, Date> userSiteCatRecentPostMap = null;
		Map<Integer, Date> userSiteForumRecentPostMap = null;
		Map<Integer, Date> userSiteTopicRecentPostMap = null;
		
		// gradable items
		for (Iterator<Category> catIter = categories.iterator(); catIter.hasNext();) 
		{
		    Category category = catIter.next();
		    
			// ignore deny access forums (if validateDates)
			if (validateDates)
			{
				List<Forum> catForums = category.getForums();
				for (Iterator<Forum> forumIter = catForums.iterator(); forumIter.hasNext();)
				{
					Forum forum = forumIter.next();
					if (forum.getAccessType() == Forum.ACCESS_DENY)
					{
						forumIter.remove();
					}
				}
			}

			if (category.isGradable() && (user != null))
			{
				//if ((validateDates) && (category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null) && (category.getAccessDates().getOpenDate().after(currentTime)))
				if ((validateDates) && (category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null) && (category.getAccessDates().getOpenDate().after(currentTime) && category.getAccessDates().isHideUntilOpen()))
				{
					catIter.remove();
					continue;
				}
				
				// check forum dates and user access. If the category contains only inaccessible forums remove the category from the list
				List<Forum> forums = category.getForums();
				
				for (Iterator<Forum> forumIter = forums.iterator(); forumIter.hasNext();) 
				{
					Forum forum = forumIter.next();

					// check forum user groups
					if (forum.getAccessType() == Forum.ACCESS_GROUPS)
					{
						boolean userInGroup = false;
						
						//forum.setGroups(getForumGroups(forum));
						if (siteForumGroups == null)
						{
							// fetch only ones
							siteForumGroups = getSiteForumGroups(context);
						}
						
						try
						{
							if (userGroups == null)
							{
								Site site = siteService.getSite(context);
								
								userGroups = site.getGroupsWithMember(userId);
							}
						} 
						catch (IdUnusedException e)
						{
							if (logger.isWarnEnabled())
							{
								logger.warn("getUserGradableItems: missing site: " + context);
							}
						}						
						
						List<String> forumGroups = siteForumGroups.get(new Integer(forum.getId()));
						
						forum.setGroups(forumGroups);
						
						/*try
						{*/
							if (forum.getGroups() != null)
							{
								
								
								for (org.sakaiproject.site.api.Group grp : userGroups) 
								{
									if (forum.getGroups().contains(grp.getId()))
									{
										userInGroup = true;
										break;
									}
								}
							}
						/*} 
						catch (IdUnusedException e)
						{
							if (logger.isWarnEnabled())
							{
								logger.warn("getUserGradableItems: missing site: " + context);
							}
						}*/
	
						// user not in any group
						if (!userInGroup)
						{
							forumIter.remove();
							continue;
						}
					}
					
					if ((validateDates) && (forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))
					{
						boolean specialAccessUser = false;
						boolean specialAccessUserAccess = false;
						
						// check special access if forum has dates
						if (forum.getAccessDates().getOpenDate() != null)
						{
							// forum not open
							if (forum.getAccessDates().getOpenDate().after(currentTime))
							{
								// if forum is not open check the special access for the user
								if (user != null)
								{
									//List<SpecialAccess> specialAccessList = this.jforumSpecialAccessService.getByForum(forum.getId());
									List<SpecialAccess> specialAccessList = forumSpecialAccessMap.get(forum.getId());
									
									if (specialAccessList != null)
									{
										for (SpecialAccess specialAccess : specialAccessList)
										{
											if (specialAccess.getUserIds().contains(new Integer(user.getId())))
											{
												specialAccessUser = true;
												
												if (!specialAccess.isOverrideStartDate())
												{
													specialAccess.getAccessDates().setOpenDate(forum.getAccessDates().getOpenDate());
												}
												if (!specialAccess.isOverrideEndDate())
												{
													specialAccess.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
												}
												if (!specialAccess.isOverrideAllowUntilDate())
												{
													specialAccess.getAccessDates().setAllowUntilDate(forum.getAccessDates().getAllowUntilDate());
												}
												/*if (!specialAccess.isOverrideLockEndDate())
												{
													specialAccess.getAccessDates().setLocked(forum.getAccessDates().isLocked());
												}*/
												if (!specialAccess.isOverrideHideUntilOpen())
												{
													specialAccess.getAccessDates().setHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
												}
												
												if (checkSpecialAccessOpenDate(specialAccess))
												{
													forum.getSpecialAccess().add(specialAccess);
													specialAccessUserAccess = true;
												}
												else
												{
													//forumIter.remove();
												}
												break;
											}
										}
									}
									
									/*if (!specialAccessUser || !specialAccessUserAccess)
									{
										forumIter.remove();
										continue;
									}*/
									if (specialAccessUser)
									{
										if (!specialAccessUserAccess)
										{
											forumIter.remove();
											continue;
										}
									}
									else
									{
										if (forum.getAccessDates().isHideUntilOpen())
										{
											forumIter.remove();
											continue;
										}
									}
								}
								else
								{
									forumIter.remove();
									continue;
								}
							}
						}
						
						// if forum is open check the special access dates for special access user
						if (user != null && !specialAccessUser)
						{
							//List<SpecialAccess> specialAccessList = this.jforumSpecialAccessService.getByForum(forum.getId());
							List<SpecialAccess> specialAccessList = forumSpecialAccessMap.get(forum.getId());
							specialAccessUser = false;
							specialAccessUserAccess = false;
							
							if (specialAccessList != null)
							{
								for (SpecialAccess specialAccess : specialAccessList)
								{
									if (specialAccess.getUserIds().contains(new Integer(user.getId())))
									{
										specialAccessUser = true;
										
										if (!specialAccess.isOverrideStartDate())
										{
											specialAccess.getAccessDates().setOpenDate(forum.getAccessDates().getOpenDate());
										}
										if (!specialAccess.isOverrideEndDate())
										{
											specialAccess.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
										}
										if (!specialAccess.isOverrideAllowUntilDate())
										{
											specialAccess.getAccessDates().setAllowUntilDate(forum.getAccessDates().getAllowUntilDate());
										}
										/*if (!specialAccess.isOverrideLockEndDate())
										{
											specialAccess.getAccessDates().setLocked(forum.getAccessDates().isLocked());
										}*/
										
										if (!specialAccess.isOverrideHideUntilOpen())
										{
											specialAccess.getAccessDates().setHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
										}
										
										if (checkSpecialAccessOpenDate(specialAccess))
										{
											forum.getSpecialAccess().add(specialAccess);
											specialAccessUserAccess = true;
										}
										else
										{
											// forumIter.remove();
										}
										break;
									}
								}
							}
							
							if (specialAccessUser && !specialAccessUserAccess)
							{
								forumIter.remove();
								continue;								
							}
						}
					}
				}
				
				if (forums.size() == 0)
				{
					catIter.remove();
					continue;
				}				
				
				// get post count and latest post time for the user for the category
				int userPostsCount = 0;
				// get user category post count only once
				if (userSiteCatPostCountMap == null)
				{
					userSiteCatPostCountMap = getUserSiteGradableCategoryPostCount(context, user.getId());
				}
				if (userSiteCatPostCountMap != null)
				{
					Integer postsCount = userSiteCatPostCountMap.get(new Integer(category.getId()));
					if (postsCount != null)
					{
						userPostsCount = postsCount;
					}
				}
				
				LastPostInfo userLastPostInfo = new LastPostInfoImpl();
				userLastPostInfo.setTopicReplies(userPostsCount);
				userLastPostInfo.setUserId(user.getId());
				
				if (userPostsCount > 0)
				{
					// user recent post
					if (userSiteCatRecentPostMap == null)
					{
						userSiteCatRecentPostMap = getUserSiteGradableCategoryRecentPosts(context, user.getId());
					}
					
					if (userSiteCatRecentPostMap != null)
					{
						Date userRecentPost = userSiteCatRecentPostMap.get(category.getId());
						userLastPostInfo.setPostDate(userRecentPost);	
					}
				}

				category.setLastPostInfo(userLastPostInfo);
				
				if (category.getGrade() != null)
				{				
					// user evaluation
					//Evaluation evaluation = getUserEvaluation(category.getGrade(), user.getSakaiUserId());
					
					// get user site evaluations once
					if (userSiteEvaluationsMap == null)
					{
						userSiteEvaluationsMap = new HashMap<Integer, Evaluation>();
						
						userSiteEvaluations = getUserSiteEvaluations(context, user.getId());
						
						for (Evaluation evaluation : userSiteEvaluations)
						{
							userSiteEvaluationsMap.put(new Integer(evaluation.getGradeId()), evaluation);
						}
					}
					if (userSiteEvaluationsMap != null)
					{
						Evaluation evaluation = userSiteEvaluationsMap.get(new Integer(category.getGrade().getId()));							
						if ((evaluation != null) && (evaluation.getUserId() == user.getId()))
						{
							category.getEvaluations().add(evaluation);
						}
					}
				}
			}
			else 
			{
				// gradable forums
				List<Forum> forums = category.getForums();
				for (Iterator<Forum> forumIter = forums.iterator(); forumIter.hasNext();) 
				{
				    Forum forum = forumIter.next();

				    if (forum.getGradeType() == Grade.GradeType.FORUM.getType())
					{
						// check forum user groups
						if (forum.getAccessType() == Forum.ForumAccess.GROUPS.getAccessType())
						{
							boolean userInGroup = false;
							
							//forum.setGroups(getForumGroups(forum));
							if (siteForumGroups == null)
							{
								// fetch only ones
								siteForumGroups = getSiteForumGroups(context);
							}
							
							try
							{
								if (userGroups == null)
								{
									Site site = siteService.getSite(context);
									
									userGroups = site.getGroupsWithMember(userId);
								}
							} 
							catch (IdUnusedException e)
							{
								if (logger.isWarnEnabled())
								{
									logger.warn("getUserGradableItems: missing site: " + context);
								}
							}
							
							List<String> forumGroups = siteForumGroups.get(new Integer(forum.getId()));
							
							forum.setGroups(forumGroups);
							
							/*try
							{*/
								if (forum.getGroups() != null)
								{
									/*Site site = siteService.getSite(context);
									
									Collection<org.sakaiproject.site.api.Group> userGroups = site.getGroupsWithMember(userId);*/
									
									for (org.sakaiproject.site.api.Group grp : userGroups) 
									{
										if (forum.getGroups().contains(grp.getId()))
										{
											userInGroup = true;
											break;
										}
									}
								}
							/*} 
							catch (IdUnusedException e)
							{
								if (logger.isWarnEnabled())
								{
									logger.warn("getUserGradableItems: missing site: " + context);
								}
							}*/

							// user not in any group
							if (!userInGroup)
							{
								forumIter.remove();
								continue;
							}
						}
						
						//if ((validateDates) && (forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null))
						if ((validateDates) && (forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))
						{
							boolean specialAccessUser = false;
							boolean specialAccessUserAccess = false;
							
							// check special access if forum has dates
							if (forum.getAccessDates().getOpenDate() != null)
							{
								// forum not open
								if (forum.getAccessDates().getOpenDate().after(currentTime))
								{
									// if forum is not open check the special access for the user
									if (user != null)
									{
										//List<SpecialAccess> specialAccessList = this.jforumSpecialAccessService.getByForum(forum.getId());
										List<SpecialAccess> specialAccessList = forumSpecialAccessMap.get(forum.getId());
										
										if (specialAccessList != null)
										{
											for (SpecialAccess specialAccess : specialAccessList)
											{
												if (specialAccess.getUserIds().contains(new Integer(user.getId())))
												{
													specialAccessUser = true;
													
													if (!specialAccess.isOverrideStartDate())
													{
														specialAccess.getAccessDates().setOpenDate(forum.getAccessDates().getOpenDate());
													}
													if (!specialAccess.isOverrideEndDate())
													{
														specialAccess.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
													}
													if (!specialAccess.isOverrideAllowUntilDate())
													{
														specialAccess.getAccessDates().setAllowUntilDate(forum.getAccessDates().getAllowUntilDate());
													}
													/*if (!specialAccess.isOverrideLockEndDate())
													{
														specialAccess.getAccessDates().setLocked(forum.getAccessDates().isLocked());
													}*/
													if (!specialAccess.isOverrideHideUntilOpen())
													{
														specialAccess.getAccessDates().setHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
													}
													
													if (checkSpecialAccessOpenDate(specialAccess))
													{
														forum.getSpecialAccess().add(specialAccess);
														specialAccessUserAccess = true;
													}
													else
													{
														//forumIter.remove();
													}
													break;
												}
											}
										}
										
										if (!specialAccessUser)
										{
											if (forum.getAccessDates().isHideUntilOpen())
											{
												forumIter.remove();
												continue;
											}
										}
										else if (!specialAccessUserAccess)
										{
											forumIter.remove();
											continue;
										}											
									}
									else
									{
										forumIter.remove();
										continue;
									}
								}
							}
							
							// if forum is open check the special access dates for special access user
							if (user != null && !specialAccessUser)
							{
								//List<SpecialAccess> specialAccessList = this.jforumSpecialAccessService.getByForum(forum.getId());
								List<SpecialAccess> specialAccessList = forumSpecialAccessMap.get(forum.getId());
								
								specialAccessUser = false;
								specialAccessUserAccess = false;
								if (specialAccessList != null)
								{
									for (SpecialAccess specialAccess : specialAccessList)
									{
										if (specialAccess.getUserIds().contains(new Integer(user.getId())))
										{
											specialAccessUser = true;
											
											if (!specialAccess.isOverrideStartDate())
											{
												specialAccess.getAccessDates().setOpenDate(forum.getAccessDates().getOpenDate());
											}
											if (!specialAccess.isOverrideEndDate())
											{
												specialAccess.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
											}
											if (!specialAccess.isOverrideAllowUntilDate())
											{
												specialAccess.getAccessDates().setAllowUntilDate(forum.getAccessDates().getAllowUntilDate());
											}
											/*if (!specialAccess.isOverrideLockEndDate())
											{
												specialAccess.getAccessDates().setLocked(forum.getAccessDates().isLocked());
											}*/
											if (!specialAccess.isOverrideHideUntilOpen())
											{
												specialAccess.getAccessDates().setHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
											}
											
											if (checkSpecialAccessOpenDate(specialAccess))
											{
												forum.getSpecialAccess().add(specialAccess);
												specialAccessUserAccess = true;
											}
											else
											{
												// forumIter.remove();
											}
											break;
										}
									}
								}
								
								if (specialAccessUser && !specialAccessUserAccess)
								{
									forumIter.remove();
									continue;
								}
							}
						}
						// check category open date
						//else if ((validateDates) && (category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null) && (category.getAccessDates().getOpenDate().after(currentTime)))
						else if ((validateDates) && (category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null) && (category.getAccessDates().getOpenDate().after(currentTime) && category.getAccessDates().isHideUntilOpen()))
						{
							forumIter.remove();
							continue;
						}
						
						if (user != null)
						{
							// get posts count and latest post time for the user for the forum
							int userPostsCount = 0;
							// get user site forum post count only once
							if (userSiteForumPostCountMap == null)
							{
								userSiteForumPostCountMap = getUserSiteGradableForumPostCount(context, user.getId());
							}
							if (userSiteForumPostCountMap != null)
							{
								Integer postsCount = userSiteForumPostCountMap.get(new Integer(forum.getId()));
								if (postsCount != null)
								{
									userPostsCount = postsCount;
								}
							}
							LastPostInfo userLastPostInfo = new LastPostInfoImpl();
							userLastPostInfo.setTopicReplies(userPostsCount);
							userLastPostInfo.setUserId(user.getId());
							
							if (userPostsCount > 0)
							{
								// user recent post
								if (userSiteForumRecentPostMap == null)
								{
									userSiteForumRecentPostMap = getUserSiteGradableForumRecentPosts(context, user.getId());
								}
								
								if (userSiteForumRecentPostMap != null)
								{
									Date userRecentPost = userSiteForumRecentPostMap.get(forum.getId());
									userLastPostInfo.setPostDate(userRecentPost);	
								}
							}

							((ForumImpl)forum).setLastPostInfo(userLastPostInfo);
							
							// user evaluation
							if (forum.getGrade() != null)
							{							
								//Evaluation evaluation = getUserEvaluation(forum.getGrade(), user.getSakaiUserId());
								// get user site evaluations once
								if (userSiteEvaluationsMap == null)
								{
									userSiteEvaluationsMap = new HashMap<Integer, Evaluation>();
									
									userSiteEvaluations = getUserSiteEvaluations(context, user.getId());
									
									for (Evaluation evaluation : userSiteEvaluations)
									{
										userSiteEvaluationsMap.put(new Integer(evaluation.getGradeId()), evaluation);
									}
								}
								if (userSiteEvaluationsMap != null)
								{
									Evaluation evaluation = userSiteEvaluationsMap.get(new Integer(forum.getGrade().getId()));							
									if ((evaluation != null) && (evaluation.getUserId() == user.getId()))
									{
										forum.getEvaluations().add(evaluation);
									}
								}	
							}
						}
					}
					else
					{
						// gradable topics
						List<Topic> topics = forum.getTopics();
						for (Iterator<Topic> topicIter = topics.iterator(); topicIter.hasNext();) 
						{
							Topic topic = topicIter.next();
							
							// check topic dates
							if (topic.isGradeTopic())
							{
								//if ((validateDates) && (topic.getAccessDates() != null) && (topic.getAccessDates().getOpenDate() != null || topic.getAccessDates().getDueDate() != null))
								if ((validateDates) && (topic.getAccessDates() != null) && (topic.getAccessDates().getOpenDate() != null || topic.getAccessDates().getDueDate() != null) || topic.getAccessDates().getAllowUntilDate() != null)
								{
									boolean specialAccessUser = false;
									boolean specialAccessUserAccess = false;
									
									if (topic.getAccessDates().getOpenDate() != null)
									{
										// topic not open
										if (topic.getAccessDates().getOpenDate().after(currentTime))
										{
											// if topic is not open check the special access for the user
											if (user != null)
											{
												//List<SpecialAccess> specialAccessList = this.jforumSpecialAccessService.getByTopic(forum.getId(), topic.getId());
												List<SpecialAccess> specialAccessList = topicSpecialAccessMap.get(topic.getId());
												
												if (specialAccessList != null)
												{
													for (SpecialAccess specialAccess : specialAccessList)
													{
														if (specialAccess.getUserIds().contains(new Integer(user.getId())))
														{
															specialAccessUser = true;
															
															if (!specialAccess.isOverrideStartDate())
															{
																specialAccess.getAccessDates().setOpenDate(topic.getAccessDates().getOpenDate());
															}
															if (!specialAccess.isOverrideEndDate())
															{
																specialAccess.getAccessDates().setDueDate(topic.getAccessDates().getDueDate());
															}
															if (!specialAccess.isOverrideAllowUntilDate())
															{
																specialAccess.getAccessDates().setAllowUntilDate(topic.getAccessDates().getAllowUntilDate());
															}
															/*if (!specialAccess.isOverrideLockEndDate())
															{
																specialAccess.getAccessDates().setLocked(topic.getAccessDates().isLocked());
															}*/
															if (!specialAccess.isOverrideHideUntilOpen())
															{
																specialAccess.getAccessDates().setHideUntilOpen(topic.getAccessDates().isHideUntilOpen());
															}
															
															if (checkSpecialAccessOpenDate(specialAccess))
															{
																topic.getSpecialAccess().add(specialAccess);
																specialAccessUserAccess = true;
															}
															else
															{
																//topicIter.remove();
															}
															break;
														}
													}
												}
												
												if (!specialAccessUser)
												{
													if (topic.getAccessDates().isHideUntilOpen())
													{
														topicIter.remove();
														continue;
													}
												}
												else if (!specialAccessUserAccess)
												{
													topicIter.remove();
													continue;
												}
											}
											else
											{
												topicIter.remove();
												continue;
											}
										}
									}
	
									// if topic is open check the special access dates for special access user
									if (user != null && !specialAccessUser)
									{
										//List<SpecialAccess> specialAccessList = this.jforumSpecialAccessService.getByTopic(forum.getId(), topic.getId());
										List<SpecialAccess> specialAccessList = topicSpecialAccessMap.get(topic.getId());
										
										specialAccessUser = false;
										specialAccessUserAccess = false;
										if (specialAccessList != null)
										{
											for (SpecialAccess specialAccess : specialAccessList)
											{
												if (specialAccess.getUserIds().contains(new Integer(user.getId())))
												{
													specialAccessUser = true;
													
													if (!specialAccess.isOverrideStartDate())
													{
														specialAccess.getAccessDates().setOpenDate(topic.getAccessDates().getOpenDate());
													}
													if (!specialAccess.isOverrideEndDate())
													{
														specialAccess.getAccessDates().setDueDate(topic.getAccessDates().getDueDate());
													}
													if (!specialAccess.isOverrideAllowUntilDate())
													{
														specialAccess.getAccessDates().setAllowUntilDate(topic.getAccessDates().getAllowUntilDate());
													}
													/*if (!specialAccess.isOverrideLockEndDate())
													{
														specialAccess.getAccessDates().setLocked(topic.getAccessDates().isLocked());
													}*/
													if (!specialAccess.isOverrideHideUntilOpen())
													{
														specialAccess.getAccessDates().setHideUntilOpen(topic.getAccessDates().isHideUntilOpen());
													}
													
													if (checkSpecialAccessOpenDate(specialAccess))
													{
														topic.getSpecialAccess().add(specialAccess);
														specialAccessUserAccess = true;
													}
													else
													{
														//topicIter.remove();
													}
													break;
												}
											}
										}
										
										if (specialAccessUser && !specialAccessUserAccess)
										{
											topicIter.remove();
											continue;
										}
									}
								}
								// check forum open date
								else if ((validateDates) && (forum.getAccessDates() != null) && (forum.getAccessDates().getOpenDate() != null || forum.getAccessDates().getDueDate() != null || forum.getAccessDates().getAllowUntilDate() != null))
								{
									boolean specialAccessUser = false;
									boolean specialAccessUserAccess = false;
									
									// check special access if forum has dates
									if (forum.getAccessDates().getOpenDate() != null)
									{
										// forum not open
										if (forum.getAccessDates().getOpenDate().after(currentTime))
										{
											// if forum is not open check the special access for the user
											if (user != null)
											{
												//List<SpecialAccess> specialAccessList = this.jforumSpecialAccessService.getByForum(forum.getId());
												List<SpecialAccess> specialAccessList = forumSpecialAccessMap.get(forum.getId());
												
												if (specialAccessList != null)
												{
													for (SpecialAccess specialAccess : specialAccessList)
													{
														if (specialAccess.getUserIds().contains(new Integer(user.getId())))
														{
															specialAccessUser = true;
															
															if (!specialAccess.isOverrideStartDate())
															{
																specialAccess.getAccessDates().setOpenDate(forum.getAccessDates().getOpenDate());
															}
															if (!specialAccess.isOverrideEndDate())
															{
																specialAccess.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
															}
															if (!specialAccess.isOverrideAllowUntilDate())
															{
																specialAccess.getAccessDates().setAllowUntilDate(forum.getAccessDates().getAllowUntilDate());
															}
															/*if (!specialAccess.isOverrideLockEndDate())
															{
																specialAccess.getAccessDates().setLocked(forum.getAccessDates().isLocked());
															}*/
															if (!specialAccess.isOverrideHideUntilOpen())
															{
																specialAccess.getAccessDates().setHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
															}
															
															if (checkSpecialAccessOpenDate(specialAccess))
															{
																if (forum.getSpecialAccess().size() == 0)
																{
																	forum.getSpecialAccess().add(specialAccess);
																}
																specialAccessUserAccess = true;
															}
															else
															{
																
															}
															break;
														}
													}
												}
												
												if (!specialAccessUser)
												{
													if (forum.getAccessDates().isHideUntilOpen())
													{
														topicIter.remove();
														continue;
													}
												}
												else if (!specialAccessUserAccess)
												{
													topicIter.remove();
													continue;
												}
											}
											else
											{
												topicIter.remove();
												continue;
											}
										}
									}
									
									// if forum is open check the special access dates for special access user
									if (user != null && !specialAccessUser)
									{
										//List<SpecialAccess> specialAccessList = this.jforumSpecialAccessService.getByForum(forum.getId());
										List<SpecialAccess> specialAccessList = forumSpecialAccessMap.get(forum.getId());
										
										specialAccessUser = false;
										specialAccessUserAccess = false;
										
										if (specialAccessList != null)
										{
											for (SpecialAccess specialAccess : specialAccessList)
											{
												if (specialAccess.getUserIds().contains(new Integer(user.getId())))
												{
													specialAccessUser = true;
													
													if (!specialAccess.isOverrideStartDate())
													{
														specialAccess.getAccessDates().setOpenDate(forum.getAccessDates().getOpenDate());
													}
													if (!specialAccess.isOverrideEndDate())
													{
														specialAccess.getAccessDates().setDueDate(forum.getAccessDates().getDueDate());
													}
													if (!specialAccess.isOverrideAllowUntilDate())
													{
														specialAccess.getAccessDates().setAllowUntilDate(forum.getAccessDates().getAllowUntilDate());
													}
													/*if (!specialAccess.isOverrideLockEndDate())
													{
														specialAccess.getAccessDates().setLocked(forum.getAccessDates().isLocked());
													}*/
													if (!specialAccess.isOverrideHideUntilOpen())
													{
														specialAccess.getAccessDates().setHideUntilOpen(forum.getAccessDates().isHideUntilOpen());
													}
													
													if (checkSpecialAccessOpenDate(specialAccess))
													{
														if (forum.getSpecialAccess().size() == 0)
														{
															forum.getSpecialAccess().add(specialAccess);
														}
														specialAccessUserAccess = true;
													}
													else
													{
														
													}
													break;
												}
											}
										}
										
										if (specialAccessUser && !specialAccessUserAccess)
										{
											topicIter.remove();
											continue;
										}
									}
								}
								// check category open date
								//else if ((validateDates) && (category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null) && (category.getAccessDates().getOpenDate().after(currentTime)))
								else if ((validateDates) && (category.getAccessDates() != null) && (category.getAccessDates().getOpenDate() != null) && (category.getAccessDates().getOpenDate().after(currentTime) && category.getAccessDates().isHideUntilOpen()))
								{
									topicIter.remove();
									continue;
								}
								
								// get user latest post time for user for the topic
								if (user !=null)
								{
									// get posts count and latest post time for the user for the forum
									int userPostsCount = 0;
									// get user site gradable topics post count only once
									if (userSiteTopicPostCountMap == null)
									{
										userSiteTopicPostCountMap = getUserSiteGradableTopicPostCount(context, user.getId());
									}
									if (userSiteTopicPostCountMap != null)
									{
										Integer postsCount = userSiteTopicPostCountMap.get(new Integer(topic.getId()));
										if (postsCount != null)
										{
											userPostsCount = postsCount;
										}
									}
									LastPostInfo userLastPostInfo = new LastPostInfoImpl();
									userLastPostInfo.setTopicReplies(userPostsCount);
									userLastPostInfo.setUserId(user.getId());									
									
									if (userPostsCount > 0)
									{
										// user recent post
										if (userSiteTopicRecentPostMap == null)
										{
											userSiteTopicRecentPostMap = getUserSiteGradableTopicRecentPosts(context, user.getId());
										}
										
										if (userSiteTopicRecentPostMap != null)
										{
											Date userRecentPost = userSiteTopicRecentPostMap.get(topic.getId());
											userLastPostInfo.setPostDate(userRecentPost);	
										}
									}
									
									if (topic.getGrade() != null)
									{							
										//Evaluation evaluation = getUserEvaluation(topic.getGrade(), user.getSakaiUserId());
										// get user site evaluations once
										if (userSiteEvaluationsMap == null)
										{
											userSiteEvaluationsMap = new HashMap<Integer, Evaluation>();
											
											userSiteEvaluations = getUserSiteEvaluations(context, user.getId());
											
											for (Evaluation evaluation : userSiteEvaluations)
											{
												userSiteEvaluationsMap.put(new Integer(evaluation.getGradeId()), evaluation);
											}
										}
										if (userSiteEvaluationsMap != null)
										{
											Evaluation evaluation = userSiteEvaluationsMap.get(new Integer(topic.getGrade().getId()));							
											if ((evaluation != null) && (evaluation.getUserId() == user.getId()))
											{
												topic.getEvaluations().add(evaluation);
											}
										}
									}
									
									topic.setLastPostInfo(userLastPostInfo);
								}
							}
						}
					}
				}
			}
			
			
		}
	}
	
	/**
	 * Gets the user evaluation
	 * 
	 * @param grade		The grade
	 * 
	 * @param userId	The user id
	 * 
	 * @return		Evaluation or null if there is no evaluation
	 */
	protected Evaluation getUserEvaluation(Grade grade, String userId)
	{
		if (grade == null) throw new IllegalArgumentException();
		if (userId == null) throw new IllegalArgumentException();
		
		// user evaluation
		Evaluation evaluation = null;
		
		StringBuilder sql;
		Object[] fields;
		int i;
			
		sql = new StringBuilder();
		sql.append("SELECT evaluation_id, grade_id, user_id, sakai_user_id, score, comments, evaluated_by, evaluated_date, released ");						
		sql.append(" FROM jforum_evaluations");
		sql.append(" WHERE grade_id = ? AND sakai_user_id = ?");
		
		fields = new Object[2];
		i = 0;
		fields[i++] = grade.getId();
		fields[i++] = userId;
					
		final List<Evaluation> evaluations = new ArrayList<Evaluation>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Evaluation evaluation = new EvaluationImpl();
					((EvaluationImpl)evaluation).setId(result.getInt("evaluation_id"));
					((EvaluationImpl)evaluation).setGradeId(result.getInt("grade_id"));
					((EvaluationImpl)evaluation).setSakaiUserId(result.getString("sakai_user_id"));
					evaluation.setScore(result.getFloat("score"));
					((EvaluationImpl)evaluation).setEvaluatedBy(result.getInt("evaluated_by"));
					
					if (result.getDate("evaluated_date") != null)
				    {
						Timestamp dueDate = result.getTimestamp("evaluated_date");
						((EvaluationImpl)evaluation).setEvaluatedDate(dueDate);
				    }
				    else
				    {
				    	((EvaluationImpl)evaluation).setEvaluatedDate(null);
				    }
					
					evaluation.setReleased(result.getInt("released") == 1);
					
					evaluations.add(evaluation);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getUserEvaluation: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (evaluations.size() == 1)
		{
			evaluation = evaluations.get(0);
		}

		return evaluation;
	}
	
	/**
	 * Gets the user site evaluations
	 * 
	 * @param context	The context/site id
	 * 
	 * @param userId	The jforum user id
	 * 
	 * @return	List of user site evaluations
	 */
	protected List<Evaluation> getUserSiteEvaluations(String context, int userId)
	{
		StringBuilder sql;
		Object[] fields;
		int i;
			
		sql = new StringBuilder();
		sql.append("SELECT e.evaluation_id, e.grade_id, e.user_id, e.sakai_user_id, e.score, e.comments, ");
		sql.append("e.evaluated_by, e.evaluated_date, e.released, e.reviewed_date ");
		sql.append("FROM jforum_evaluations e, jforum_grade g WHERE e.user_id = ? ");
		sql.append("AND e.grade_id = g.grade_id AND g.context = ?");
		
		fields = new Object[2];
		i = 0;
		fields[i++] = userId;
		fields[i++] = context;
					
		final List<Evaluation> evaluations = new ArrayList<Evaluation>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Evaluation evaluation = new EvaluationImpl();
					((EvaluationImpl)evaluation).setId(result.getInt("evaluation_id"));
					((EvaluationImpl)evaluation).setGradeId(result.getInt("grade_id"));
					((EvaluationImpl)evaluation).setUserId(result.getInt("user_id"));
					((EvaluationImpl)evaluation).setSakaiUserId(result.getString("sakai_user_id"));
					evaluation.setScore(result.getFloat("score"));
					((EvaluationImpl)evaluation).setEvaluatedBy(result.getInt("evaluated_by"));
					
					if (result.getDate("evaluated_date") != null)
				    {
						Timestamp dueDate = result.getTimestamp("evaluated_date");
						((EvaluationImpl)evaluation).setEvaluatedDate(dueDate);
				    }
				    else
				    {
				    	((EvaluationImpl)evaluation).setEvaluatedDate(null);
				    }
					
					evaluation.setReleased(result.getInt("released") == 1);
					
					evaluations.add(evaluation);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getUserSiteEvaluations: " + e, e);
					}
					return null;
				}
			}
		});

		return evaluations;
	}
	
	/**
	 * Gets user site gradable categories post count
	 * 
	 * @param context	The site id
	 * 
	 * @param userId	The jforum user id
	 * 
	 * @return	The map of category id as key and post count as value
	 */
	protected Map<Integer, Integer> getUserSiteGradableCategoryPostCount(String context, int userId)
	{
		final Map<Integer, Integer> userCatPostcount = new HashMap<Integer, Integer>();
		
		StringBuilder sql;
		Object[] fields;
		int i;
			
		sql = new StringBuilder();
		sql.append("SELECT c.categories_id, COUNT(p.post_id) AS post_count FROM jforum_posts p, jforum_topics t, jforum_forums f, jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE p.user_id = ? AND cc.course_id = ? ");
		sql.append("AND cc.categories_id = c.categories_id ");
		sql.append("AND p.topic_id = t.topic_id AND t.forum_id = f.forum_id ");
		sql.append("AND f.categories_id = c.categories_id AND c.gradable = 1 AND ");
		sql.append("f.forum_access_type <> "+ Forum.ACCESS_DENY +" ");
		sql.append("GROUP BY c.categories_id ");
		
		fields = new Object[2];
		i = 0;
		fields[i++] = userId;
		fields[i++] = context;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					userCatPostcount.put(new Integer(result.getInt("categories_id")), new Integer(result.getInt("post_count")));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getUserSiteEvaluations: " + e, e);
					}
					return null;
				}
			}
		});
		
		return userCatPostcount;
	}
	
	/**
	 * Gets user site gradable forums post count
	 * 
	 * @param context	The site id
	 * 
	 * @param userId	The jforum user id
	 * 
	 * @return	The map of category id as key and post count as value
	 */
	protected Map<Integer, Integer> getUserSiteGradableForumPostCount(String context, int userId)
	{
		final Map<Integer, Integer> userForumPostcount = new HashMap<Integer, Integer>();
		
		StringBuilder sql;
		Object[] fields;
		int i;
			
		sql = new StringBuilder();
		sql.append("SELECT f.forum_id, COUNT(p.post_id) AS post_count FROM jforum_posts p, jforum_topics t, jforum_forums f, jforum_categories c,jforum_sakai_course_categories cc ");
		sql.append("WHERE p.user_id = ? AND cc.course_id = ? ");
		sql.append("AND p.topic_id = t.topic_id AND t.forum_id = f.forum_id ");
		sql.append("AND f.categories_id = c.categories_id ");
		sql.append("AND cc.categories_id = c.categories_id ");
		sql.append("AND f.forum_grade_type = "+ Grade.GRADE_BY_FORUM +" ");
		sql.append("AND f.forum_access_type <> "+ Forum.ACCESS_DENY +" ");
		sql.append("GROUP BY f.forum_id	");
		
		fields = new Object[2];
		i = 0;
		fields[i++] = userId;
		fields[i++] = context;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					userForumPostcount.put(new Integer(result.getInt("forum_id")), new Integer(result.getInt("post_count")));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getUserSiteGradableForumPostCount: " + e, e);
					}
					return null;
				}
			}
		});
		
		return userForumPostcount;
	}
	
	/**
	 * Gets user site gradable topics post count
	 * 
	 * @param context	The site id
	 * 
	 * @param userId	The jforum user id
	 * 
	 * @return	The map of category id as key and post count as value
	 */
	protected Map<Integer, Integer> getUserSiteGradableTopicPostCount(String context, int userId)
	{
		final Map<Integer, Integer> userTopicPostcount = new HashMap<Integer, Integer>();
		
		StringBuilder sql;
		Object[] fields;
		int i;
			
		sql = new StringBuilder();
		sql.append("SELECT t.topic_id, COUNT(p.post_id) AS post_count FROM jforum_posts p, jforum_topics t, jforum_forums f, ");
		sql.append("jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE p.user_id = ? AND cc.course_id = ? ");
		sql.append("AND t.topic_grade = 1 ");
		sql.append("AND f.forum_grade_type = "+ Grade.GRADE_BY_TOPIC +" ");
		sql.append("AND f.forum_access_type <> "+ Forum.ACCESS_DENY +" ");
		sql.append("AND p.topic_id = t.topic_id AND t.forum_id = f.forum_id ");
		sql.append("AND f.categories_id = c.categories_id ");
		sql.append("AND cc.categories_id = c.categories_id ");
		sql.append("GROUP BY t.topic_id");
		
		fields = new Object[2];
		i = 0;
		fields[i++] = userId;
		fields[i++] = context;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					userTopicPostcount.put(new Integer(result.getInt("topic_id")), new Integer(result.getInt("post_count")));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getUserSiteGradableTopicPostCount: " + e, e);
					}
					return null;
				}
			}
		});
		
		return userTopicPostcount;
	}
	
	/**
	 * Gets user recent posts for gradable categories of the site
	 * 
	 * @param context	The context/site id
	 * 
	 * @param userId	Jfourm user id
	 * 
	 * @return The user recent posts for gradable categories of the site
	 */
	protected Map<Integer, Date> getUserSiteGradableCategoryRecentPosts(String context, int userId)
	{

		final Map<Integer, Date> userCatRecentPosts = new HashMap<Integer, Date>();
		
		StringBuilder sql;
		Object[] fields;
		int i;
			
		sql = new StringBuilder();
		sql.append("SELECT c.categories_id, max(p.post_time) as latest_post_time ");
		sql.append("FROM jforum_posts p, jforum_topics t, jforum_forums f, jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE p.user_id = ? AND cc.course_id = ? ");
		sql.append("AND f.categories_id = c.categories_id AND c.categories_id = cc.categories_id ");
		sql.append("AND p.topic_id = t.topic_id AND t.forum_id = f.forum_id ");
		sql.append("AND f.forum_access_type <> "+ Forum.ACCESS_DENY +" ");
		sql.append("AND c.gradable = 1 GROUP BY c.categories_id");
		
		fields = new Object[2];
		i = 0;
		fields[i++] = userId;
		fields[i++] = context;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					userCatRecentPosts.put(new Integer(result.getInt("categories_id")), result.getTimestamp("latest_post_time"));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getUserSiteGradableCategoryRecentPosts: " + e, e);
					}
					return null;
				}
			}
		});
		
		return userCatRecentPosts;		
	}
	
	/**
	 * Gets user recent posts for gradable forums of the site
	 * 
	 * @param context	The context/site id
	 * 
	 * @param userId	Jfourm user id
	 * 
	 * @return The user recent posts for gradable forums of the site
	 */
	protected Map<Integer, Date> getUserSiteGradableForumRecentPosts(String context, int userId)
	{

		final Map<Integer, Date> userForumRecentPosts = new HashMap<Integer, Date>();
		
		StringBuilder sql;
		Object[] fields;
		int i;
			
		sql = new StringBuilder();
		sql.append("SELECT p.forum_id, max(p.post_time) as latest_post_time ");
		sql.append("FROM jforum_posts p, jforum_topics t, jforum_forums f, jforum_categories c, jforum_sakai_course_categories cc "); 
		sql.append("WHERE p.user_id = ? ");
		sql.append("AND cc.course_id = ? ");
		sql.append("AND p.topic_id = t.topic_id AND t.forum_id = f.forum_id ");
		sql.append("AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = cc.categories_id ");
		sql.append("AND f.forum_access_type <> "+ Forum.ACCESS_DENY +" ");
		sql.append("AND f.forum_grade_type = "+ Grade.GRADE_BY_FORUM +" ");
		sql.append("GROUP BY f.forum_id");

		fields = new Object[2];
		i = 0;
		fields[i++] = userId;
		fields[i++] = context;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					userForumRecentPosts.put(new Integer(result.getInt("forum_id")), result.getTimestamp("latest_post_time"));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getUserSiteGradableForumRecentPosts: " + e, e);
					}
					return null;
				}
			}
		});
		
		return userForumRecentPosts;		
	}
	
	/**
	 * Gets user recent posts for gradable topics of the site
	 * 
	 * @param context	The context/site id
	 * 
	 * @param userId	Jfourm user id
	 * 
	 * @return The user recent posts for gradable topics of the site
	 */
	protected Map<Integer, Date> getUserSiteGradableTopicRecentPosts(String context, int userId)
	{

		final Map<Integer, Date> userTopicRecentPosts = new HashMap<Integer, Date>();
		
		StringBuilder sql;
		Object[] fields;
		int i;
			
		sql = new StringBuilder();
		sql.append("SELECT p.topic_id, max(p.post_time) as latest_post_time ");
		sql.append("FROM jforum_posts p, jforum_topics t, jforum_forums f, jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE p.user_id = ? ");
		sql.append("AND cc.course_id = ? ");
		sql.append("AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = cc.categories_id ");
		sql.append("AND p.topic_id = t.topic_id AND t.forum_id = f.forum_id ");
		sql.append("AND t.topic_grade = 1 ");
		sql.append("AND f.forum_access_type <> "+ Forum.ACCESS_DENY +" ");
		sql.append("AND f.forum_grade_type = "+ Grade.GRADE_BY_TOPIC +" ");
		sql.append("GROUP BY p.topic_id");

		fields = new Object[2];
		i = 0;
		fields[i++] = userId;
		fields[i++] = context;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					userTopicRecentPosts.put(new Integer(result.getInt("topic_id")), result.getTimestamp("latest_post_time"));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getUserSiteGradableTopicRecentPosts: " + e, e);
					}
					return null;
				}
			}
		});
		
		return userTopicRecentPosts;		
	}
	
	/**
	 * Checks the special access open date with current time
	 * 
	 * @param specialAccess	Special access
	 * 
	 * @return	true - if there is no open date or open date is before the current time
	 * 			false - if not accessible or open date is after the current time
	 */
	protected boolean checkSpecialAccessOpenDate(SpecialAccess specialAccess)
	{
		if (specialAccess == null) throw new IllegalArgumentException();
			
		Date currentTime = Calendar.getInstance().getTime();
		
		if ((specialAccess.getAccessDates().getOpenDate() == null) || (specialAccess.getAccessDates().getOpenDate().before(currentTime) || (specialAccess.getAccessDates().getOpenDate().after(currentTime) && !specialAccess.getAccessDates().isHideUntilOpen())))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Get jforum user
	 * 
	 * @param userId	User id
	 * 
	 * @return	Jforum user
	 */
	protected User getJforumUser(String userId)
	{
		User user = null;
		
		String sql;
		Object[] fields;
		int i = 0;
		
		sql = "SELECT user_id, user_fname, user_lname, sakai_user_id FROM jforum_users WHERE sakai_user_id = ?";						
				
		fields = new Object[1];
		fields[i++] = userId;
		

		final List<User> users = new ArrayList<User>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					UserImpl user = new UserImpl();
					user.setId(result.getInt("user_id"));
					user.setFirstName(result.getString("user_fname"));
					user.setLastName(result.getString("user_lname"));
					user.setSakaiUserId(result.getString("sakai_user_id"));
					
					users.add(user);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getJforumUser: " + e, e);
					}
					return null;
				}
			}
		});
		
		
		
		if (users.size() == 1)
		{
			user = users.get(0);
		}
	
		
		return user;
	}
	
	/**
	 * get userid list from the string
	 * 
	 * @param userIds	userids string
	 * 
	 * @return list of userid's
	 */
	protected List<Integer> getUserIdList(String userIds)
	{
		if ((userIds == null) || (userIds.trim().length() == 0)) 
		{
			return null;
		}
		List<Integer> userIdList = new ArrayList<Integer>();
		
		String[] userIdsArray = userIds.split(":");
		if ((userIdsArray != null) && (userIdsArray.length > 0))
		{
			for (String userId : userIdsArray)
			{
				userIdList.add(new Integer(userId));
			}
		}
		
		return userIdList;
	}
	
	/**
	 * get forum groups
	 * 
	 * @param forum	Forum
	 * 
	 * @return	List of forum groups
	 */
	protected List<String> getForumGroups(Forum forum)
	{
		String sql;
		Object[] fields;
		int i = 0;
		
		sql = "SELECT forum_id, sakai_group_id from jforum_forum_sakai_groups where forum_id = ?";						
				
		fields = new Object[1];
		fields[i++] = forum.getId();
		

		final List<String> groups = new ArrayList<String>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					groups.add(result.getString("sakai_group_id"));

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getForumGroups: " + e, e);
					}
					return null;
				}
			}
		});
		
		return groups;
	}
	
	/**
	 * Gets the froum site groups
	 * 
	 * @param siteId	The site id
	 * 
	 * @return The site forum groups
	 */
	protected Map<Integer, List<String>> getSiteForumGroups(String siteId)
	{
		final Map<Integer, List<String>> siteForumGroups = new HashMap<Integer, List<String>>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT g.forum_id, g.sakai_group_id FROM jforum_forum_sakai_groups g, jforum_forums f, jforum_sakai_course_categories cc ");
		sql.append("WHERE g.forum_id = f.forum_id AND f.categories_id = cc.categories_id AND cc.course_id = ?");						
		
		Object[] fields;
		int i = 0;
		fields = new Object[1];
		fields[i++] = siteId;
			
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Integer forumId = new Integer(result.getInt("forum_id"));
					String groupId = result.getString("sakai_group_id");
					
					List<String> forumGroups = siteForumGroups.get(forumId);
					
					if (forumGroups == null)
					{
						forumGroups = new ArrayList<String>();
						forumGroups.add(groupId);
						
						siteForumGroups.put(forumId, forumGroups);
					}
					else
					{
						forumGroups.add(groupId);
					}

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getSiteForumGroups: " + e, e);
					}
					return null;
				}
			}
		});
		
		return siteForumGroups;
	}
	
	/**
	 * get user post count
	 * 
	 * @param sql		The query
	 * 
	 * @param fields	Query params
	 * 
	 * @return		Posts count
	 */
	protected int getUserPostCount(String sql, Object[] fields)
	{
		int postsCount = 0;
		
		final List<Integer> count = new ArrayList<Integer>();
		
		this.sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					//result.getInt("categories_id"));
					count.add(result.getInt("post_count"));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getUserPostCount: " + e, e);
					}
					return null;
				}
			}
		});
		
		if (count.size() == 1)
		{
			postsCount = count.get(0);
		}
				
		return postsCount;		
	}
	
	/**
	 * gets user recent post time
	 * 
	 * @param sql	SQL query to execute
	 * 
	 * @param fields	Query params 
	 * 
	 * @return	Returns post time if user has posted else null
	 */
	protected Post getUserRecentPost(String sql, Object[] fields)
	{
		Post post = null;
		
		final List<Post> postList = new ArrayList<Post>(); 
		
		this.sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					PostImpl post = new PostImpl();
					post.setId(result.getInt("post_id"));
					post.setForumId(result.getInt("forum_id"));
					post.setTopicId(result.getInt("topic_id"));
					
					if ((result.getDate("latest_post_time") != null))
					{
						if (result.getDate("latest_post_time") != null)
					    {
					      Timestamp userLatestPostTime = result.getTimestamp("latest_post_time");
					      post.setUserLatestPostTime(userLatestPostTime);
					    }
					    else
					    {
					    	post.setUserLatestPostTime(null);
					    }
					}					
					
					postList.add(post);

					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("getUserRecentPost: " + e, e);
					}
					return null;
				}
			}
		});
				
		if (postList.size() == 1)
		{
			post = postList.get(0);
		}
		return post;		
	}
	
	/**
	 * Fetches the posts count of the user
	 * 
	 * @param context	The context
	 * 
	 * @return The map of userid as key with post count as value
	 */
	protected Map<String, Integer> fetchContextGradableItemsPostsCount(String context)
	{
		if (context == null) throw new IllegalArgumentException();
		
		// user gradable items posts count
		StringBuilder sql;
		Object[] fields;
		int i;
		sql = null;
		fields = null;
		i = 0;	
			
		sql = new StringBuilder();
		
		sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
		sql.append("FROM  jforum_site_users s, jforum_users u, jforum_posts p, jforum_topics t, ");
		sql.append("jforum_forums f,jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.sakai_site_id = ? ");
		sql.append("AND s.user_id = u.user_id AND s.user_id = p.user_id ");
		sql.append("AND p.topic_id = t.topic_id ");
		sql.append("AND p.forum_id = f.forum_id AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = cc.categories_id ");
		sql.append("AND cc.course_id = ? ");
		sql.append("AND (c.gradable = 1 OR f.forum_grade_type = "+ Grade.GradeType.FORUM.getType() +" OR t.topic_grade = "+ Topic.TopicGradableCode.YES.getTopicGradableCode() +" OR t.topic_export = "+ Topic.TopicGradableCode.YES.getTopicGradableCode() +") ");
		sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
				
		fields = new Object[2];
		i = 0;
		fields[i++] = context;
		fields[i++] = context;
					
		Map<String, Integer> userPostCount = readUserPostsCount(sql, fields);
		
		return userPostCount;
	}
	
	/**
	 * Fetches the site posts count of the user
	 * 
	 * @param context	The context
	 * 
	 * @return The map of userid as key with post count as value
	 */
	protected Map<String, Integer> fetchContextPostsCount(String context)
	{
		if (context == null) throw new IllegalArgumentException();
		
		// user site posts count
		StringBuilder sql;
		Object[] fields;
		int i;
		sql = null;
		fields = null;
		i = 0;	
			
		sql = new StringBuilder();
		
		sql.append("SELECT s.user_id, COUNT(p.post_id) AS user_posts_count, u.user_fname AS user_fname,u.user_lname AS user_lname, u.sakai_user_id, u.username ");
		sql.append("FROM  jforum_site_users s, jforum_users u, jforum_posts p, jforum_topics t, ");
		sql.append("jforum_forums f,jforum_categories c, jforum_sakai_course_categories cc ");
		sql.append("WHERE s.sakai_site_id = ? ");
		sql.append("AND s.user_id = u.user_id AND s.user_id = p.user_id ");
		sql.append("AND p.topic_id = t.topic_id ");
		sql.append("AND p.forum_id = f.forum_id AND f.categories_id = c.categories_id ");
		sql.append("AND c.categories_id = cc.categories_id ");
		sql.append("AND cc.course_id = ? ");
		sql.append("GROUP BY s.user_id, u.user_fname, u.user_lname, u.sakai_user_id, u.username ");
				
		fields = new Object[2];
		i = 0;
		fields[i++] = context;
		fields[i++] = context;
					
		Map<String, Integer> userPostCount = readUserPostsCount(sql, fields);
		
		return userPostCount;
	}

	/**
	 * Gets user posts count from the database
	 * 
	 * @param sql	The query
	 * 
	 * @param fields	Query parameters
	 * 
	 * @return The map of user posts count with userid as key and posts count as value
	 */
	protected Map<String, Integer> readUserPostsCount(StringBuilder sql, Object[] fields)
	{
		final Map<String, Integer> userPostCount = new HashMap<String, Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					String userSakId = result.getString("sakai_user_id");
					int userPostsCount = result.getInt("user_posts_count");
					userPostCount.put(userSakId, userPostsCount);
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("readUserPostsCount: " + e, e);
					}
					return null;
				}
			}
		});
		return userPostCount;
	}
	
	
	/**
	 * @param sqlService
	 *            the sqlService to set
	 */
	public void setSqlService(SqlService sqlService)
	{
		this.sqlService = sqlService;
	}
	
	/**
	 * Set the SiteService.
	 * 
	 * @param service
	 *       	The SiteService.
	 */
	public void setSiteService(SiteService service)
	{
		this.siteService = service;
	}

	/**
	 * @param jforumCategoryService 
	 * 			The jforumCategoryService to set
	 */
	public void setJforumCategoryService(JForumCategoryService jforumCategoryService)
	{
		this.jforumCategoryService = jforumCategoryService;
	}
	
	/**
	 * @param jforumForumService the jforumForumService to set
	 */
	public void setJforumForumService(JForumForumService jforumForumService)
	{
		this.jforumForumService = jforumForumService;
	}

	/**
	 * @param jforumSpecialAccessService the jforumSpecialAccessService to set
	 */
	public void setJforumSpecialAccessService(JForumSpecialAccessService jforumSpecialAccessService)
	{
		this.jforumSpecialAccessService = jforumSpecialAccessService;
	}
}
