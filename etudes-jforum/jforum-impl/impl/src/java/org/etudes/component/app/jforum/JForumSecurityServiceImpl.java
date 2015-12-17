/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumSecurityServiceImpl.java $ 
 * $Id: JForumSecurityServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.JForumSecurityService;
import org.sakaiproject.authz.api.AuthzGroup;
import org.sakaiproject.authz.api.GroupNotDefinedException;
import org.sakaiproject.authz.api.Member;
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.authz.cover.AuthzGroupService;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.thread_local.api.ThreadLocalManager;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;

public class JForumSecurityServiceImpl implements JForumSecurityService
{
	private static Log logger = LogFactory.getLog(JForumSecurityServiceImpl.class);
	
	/** Dependency: SecurityService */
	protected SecurityService securityService = null;
	
	/** Dependency: SessionManager */
	protected SessionManager sessionManager = null;
	
	/** Dependency: SiteService */
	protected SiteService siteService = null;
	
	/** Dependency: ThreadLocalManager. */
	protected ThreadLocalManager threadLocalManager = null;
	
	/** Dependency: SiteService */
	protected ToolManager toolManager = null;
	
	public void destroy()
	{
		if (logger.isInfoEnabled())
			logger.info("destroy....");
	}
	
	public void init()
	{
		if (logger.isInfoEnabled())
			logger.info("init....");
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isJForumFacilitator()
	{
		if (this.securityService.isSuperUser()) return true;
		
		return this.securityService.unlock(JForumSecurityService.ROLE_FACILITATOR, "/site/" + this.toolManager.getCurrentPlacement().getContext());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isJForumFacilitator(String context)
	{
		if (this.securityService.isSuperUser()) return true;
		
		return this.securityService.unlock(JForumSecurityService.ROLE_FACILITATOR, "/site/" + context);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isJForumFacilitator(String context, String userId)
	{
		if (this.securityService.isSuperUser(userId)) return true;
		
		return this.securityService.unlock(userId, JForumSecurityService.ROLE_FACILITATOR, "/site/" + context);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isJForumParticipant()
	{
		if (this.securityService.isSuperUser()) return true;
		
		return this.securityService.unlock(JForumSecurityService.ROLE_PARTICIPANT, "/site/" + this.toolManager.getCurrentPlacement().getContext());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isJForumParticipant(String context)
	{
		if (this.securityService.isSuperUser()) return true;
		
		return this.securityService.unlock(JForumSecurityService.ROLE_PARTICIPANT, "/site/" + context);
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isJForumParticipant(String context, String userId)
	{
		if (this.securityService.isSuperUser(userId)) return true;
		
		return this.securityService.unlock(userId, JForumSecurityService.ROLE_PARTICIPANT, "/site/" + context);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isUserActive(String context, String sakaiUserId)
	{
		if (context != null && context.trim().length() > 0 && sakaiUserId != null && sakaiUserId.trim().length() > 0)
		{
			if (isUserInSakai(sakaiUserId))
			{
				try
				{
					AuthzGroup authzGroup = (AuthzGroup) this.threadLocalManager.get(cacheKey("authGroup:"+ context));
					
					if (authzGroup == null)
					{
						String authzId = siteService.siteReference(context);
						authzGroup = AuthzGroupService.getAuthzGroup(authzId);
						
						this.threadLocalManager.set(cacheKey("authGroup:"+ context), authzGroup);
					}
					
					if (authzGroup == null)
					{
						return Boolean.FALSE;
					}
					
					Member member = authzGroup.getMember(sakaiUserId);
					if (member != null) return member.isActive();
				}
				catch (GroupNotDefinedException e)
				{
					if (logger.isErrorEnabled()) logger.error(".isUserActive() " + e.toString());
				}
			}

		}
		return Boolean.FALSE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isUserBlocked(String context, String sakaiUserId)
	{
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakaiuser id is null or blank");
		}
		// for thread-local caching
		Boolean cachedSiteBlockedRole = (Boolean) this.threadLocalManager.get(cacheKey("blocked:"+ context));
		AuthzGroup authzGroup = (AuthzGroup) this.threadLocalManager.get(cacheKey("authGroup:"+ context));
		if (cachedSiteBlockedRole != null && !cachedSiteBlockedRole)
		{
			return Boolean.FALSE;
		}
		
		if (isUserInSakai(sakaiUserId))
		{
			try
			{
				if (cachedSiteBlockedRole == null || authzGroup == null)
				{
					String authzId = siteService.siteReference(context);
					authzGroup = AuthzGroupService.getAuthzGroup(authzId);
					
					this.threadLocalManager.set(cacheKey("authGroup:"+ context), authzGroup);
						
					if (authzGroup.getRole("Blocked") != null)
					{
						this.threadLocalManager.set(cacheKey("blocked:"+ context), Boolean.TRUE);
						cachedSiteBlockedRole = Boolean.TRUE;
					}
					else
					{
						this.threadLocalManager.set(cacheKey("blocked:"+ context), Boolean.FALSE);
						cachedSiteBlockedRole = Boolean.FALSE;
					}
				}
				
				if (cachedSiteBlockedRole.booleanValue())
				{
					if (authzGroup != null)
					{
						Member member = authzGroup.getMember(sakaiUserId);
						
						if (member != null)
						{
							Role role = member.getRole();
							if (role != null)
							{
								return (Boolean.valueOf(role.getId().equalsIgnoreCase("Blocked")));
							}
						}
					}
					
					return Boolean.FALSE;					
				}
				else
				{
					return Boolean.FALSE;
				}
			}
			catch (GroupNotDefinedException e)
			{
				if (logger.isErrorEnabled()) logger.error(".isUserBlocked() " + e.toString());
			}
		}
		return Boolean.TRUE;
	}
	
	/**
	 * @param securityService the securityService to set
	 */
	public void setSecurityService(SecurityService securityService)
	{
		this.securityService = securityService;
	}
	
	/**
	 * @param sessionManager the sessionManager to set
	 */
	public void setSessionManager(SessionManager sessionManager)
	{
		this.sessionManager = sessionManager;
	}
	
	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService)
	{
		this.siteService = siteService;
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
	 * @param toolManager the toolManager to set
	 */
	public void setToolManager(ToolManager toolManager)
	{
		this.toolManager = toolManager;
	}

	/**
	 * Key for role blocked and auth group
	 * 
	 * @param key	context or auth group (blocked:context or authGroup:context)
	 * 
	 * @return	Cache key
	 */
	protected String cacheKey(String key)
	{
		return "jforum:site:role:" + key;
	}
	
	/**
	 * Clear the site blocked role from thread local cache
	 * 
	 * @param context 	Context
	 */
	protected void clearCache(String context)
	{
		if (context == null || context.trim().length() == 0)
		{
			return;
		}
		// clear the cache
		this.threadLocalManager.set(cacheKey("blocked:"+ context), null);
		this.threadLocalManager.set(cacheKey("authGroup:"+ context), null);
	}
	
	/**
	 * checks to see if user is in sakai
	 * 
	 * @param sakaiUserId	user sakai id
	 * 
	 * @return	true - if user is in site
	 * 			false - if user is not in site
	 */
	protected boolean isUserInSakai(String sakaiUserId)
	{
		try
		{
			UserDirectoryService.getUser(sakaiUserId);

			return true;
		}
		catch (UserNotDefinedException e)
		{
			return false;
		}
	}
}
