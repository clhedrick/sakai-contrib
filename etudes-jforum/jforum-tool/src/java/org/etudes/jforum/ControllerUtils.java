/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/ControllerUtils.java $ 
 * $Id: ControllerUtils.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.jforum;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.Cookie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.JForumAccessException;
import org.etudes.api.app.jforum.JForumCategoryService;
import org.etudes.api.app.jforum.JForumUserService;
import org.etudes.api.app.jforum.dao.UserAlreadyExistException;
import org.etudes.jforum.entities.UserSession;
import org.etudes.jforum.util.I18n;
import org.etudes.jforum.util.date.DateUtil;
import org.etudes.jforum.util.preferences.ConfigKeys;
import org.etudes.jforum.util.preferences.SakaiSystemGlobals;
import org.etudes.jforum.util.preferences.SystemGlobals;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.user.cover.UserDirectoryService;

import freemarker.template.SimpleHash;

/**
 * Common methods used by the controller.
 *
 * @author Rafael Steil
 */
public class ControllerUtils
{
	private static Log logger = LogFactory.getLog(ControllerUtils.class);
	/**
	 * Setup common variables used by almost all templates.
	 *
	 * @param context The context to use
	 */
	public void prepareTemplateContext(SimpleHash context)
	{
		//context.put("karmaEnabled", SecurityRepository.canAccess(SecurityConstants.PERM_KARMA_ENABLED));
		context.put("karmaEnabled", false);
		//context.put("dateTimeFormat", SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		context.put("dateTimeFormat", SakaiSystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		context.put("autoLoginEnabled", SystemGlobals.getBoolValue(ConfigKeys.AUTO_LOGIN_ENABLED));
		context.put("sso", ConfigKeys.TYPE_SSO.equals(SystemGlobals.getValue(ConfigKeys.AUTHENTICATION_TYPE)));
		context.put("contextPath", JForum.getRequest().getContextPath());
		context.put("serverName", JForum.getRequest().getServerName());
		context.put("templateName", SystemGlobals.getValue(ConfigKeys.TEMPLATE_DIR));
		context.put("extension", SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		context.put("serverPort", Integer.toString(JForum.getRequest().getServerPort()));
		context.put("I18n", I18n.getInstance());
		//context.put("imagesI18n", SystemGlobals.getValue(ConfigKeys.I18N_IMAGES_DIR));
		context.put("imagesI18n", SakaiSystemGlobals.getValue(ConfigKeys.I18N_IMAGES_DIR));
		context.put("version", SystemGlobals.getValue(ConfigKeys.VERSION));
		context.put("forumTitle", SystemGlobals.getValue(ConfigKeys.FORUM_PAGE_TITLE));
		context.put("pageTitle", SystemGlobals.getValue(ConfigKeys.FORUM_PAGE_TITLE));
		context.put("metaKeywords", SystemGlobals.getValue(ConfigKeys.FORUM_PAGE_METATAG_KEYWORDS));
		context.put("metaDescription", SystemGlobals.getValue(ConfigKeys.FORUM_PAGE_METATAG_DESCRIPTION));
		context.put("forumLink", SystemGlobals.getValue(ConfigKeys.FORUM_LINK));
		context.put("homepageLink", SystemGlobals.getValue(ConfigKeys.HOMEPAGE_LINK));
		// context.put("encoding", SystemGlobals.getValue(ConfigKeys.ENCODING));
		context.put("encoding", SakaiSystemGlobals.getValue(ConfigKeys.ENCODING));
		//context.put("bookmarksEnabled", SecurityRepository.canAccess(SecurityConstants.PERM_BOOKMARKS_ENABLED));
		context.put("bookmarksEnabled", true);
		context.put("JForumContext", new JForumContext(JForum.getRequest().getContextPath(),
				SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION), JForum.getRequest(), JForum.getResponse()));
		//<<<02/22/06 - Murthy - added for avatars
		// if (SystemGlobals.getBoolValue(ConfigKeys.AVATAR_CLUSTERED))
		if (SakaiSystemGlobals.getBoolValue(ConfigKeys.AVATAR_CLUSTERED))
			// context.put("imageContextPath", SystemGlobals.getValue(ConfigKeys.AVATAR_CONTEXT));
			context.put("imageContextPath", SakaiSystemGlobals.getValue(ConfigKeys.AVATAR_CONTEXT));
		else
			context.put("imageContextPath", JForum.getRequest().getContextPath());
		//>>>02/22/06 - Murthy
		
		// for timezone support
		context.put("DateUtil", new DateUtil());
		
		//get the editor path for etudes installations
		String editorPath = ServerConfigurationService.getString("etudes.editor.path");
		
		if (editorPath != null && editorPath.trim().length() > 0)
		{
			context.put("editorPath", editorPath);
		}
	}

	/**
	 * Checks user credentials / automatic login.
	 *
	 * @param userSession The UserSession instance associated to the user's session
	 * @return <code>true</code> if auto login was enabled and the user was sucessfuly
	 * logged in.
	 * @throws DatabaseException
	 */
	/*protected boolean checkAutoLogin(UserSession userSession)
	{
		String cookieName = SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_DATA);

		Cookie cookie = this.getCookieTemplate(cookieName);
		Cookie hashCookie = this.getCookieTemplate(SystemGlobals.getValue(ConfigKeys.COOKIE_USER_HASH));
		Cookie autoLoginCookie = this.getCookieTemplate(SystemGlobals.getValue(ConfigKeys.COOKIE_AUTO_LOGIN));

		if (hashCookie != null && cookie != null
				&& !cookie.getValue().equals(SystemGlobals.getValue(ConfigKeys.ANONYMOUS_USER_ID))
				&& autoLoginCookie != null && "1".equals(autoLoginCookie.getValue())) {
			String uid = cookie.getValue();
			String uidHash = hashCookie.getValue();

			String securityHash = SystemGlobals.getValue(ConfigKeys.USER_HASH_SEQUENCE);

			if ((uid != null && !uid.equals("")) && (securityHash != null && !securityHash.equals(""))
					&& (MD5.crypt(securityHash + uid).equals(uidHash))) {
				int userId = Integer.parseInt(uid);
				userSession.setUserId(userId);

				try {
					User user = DataAccessDriver.getInstance().newUserDAO().selectById(userId);

					if (user == null || user.getId() != userId) {
						userSession.makeAnonymous();
						return false;
					}

					this.configureUserSession(userSession, user);
				}
				catch (Exception e) {
					throw new DatabaseException(e);
				}

				return true;
			}

			userSession.makeAnonymous();
		}

		return false;
	}*/

	/**
	 * Setup optios and values for the user's session if authentication was ok.
	 *
	 * @param userSession The UserSession instance of the user
	 * @param user The User instance of the authenticated user
	 * @throws Exception
	 */
	/*protected void configureUserSession(UserSession userSession, User user) throws Exception
	{
		userSession.dataToUser(user);

		// As an user may come back to the forum before its
		// last visit's session expires, we should check for
		// existent user information and then, if found, store
		// it to the database before getting his information back.
		String sessionId = SessionFacade.isUserInSession(user.getId());

		UserSession tmpUs = new UserSession();
		if (sessionId != null) {
			System.out.println("RENDERING FROM CACHE");
			SessionFacade.storeSessionData(sessionId, JForum.getConnection());
			tmpUs = SessionFacade.getUserSession(sessionId);
			SessionFacade.remove(sessionId);
		}
		else {
			System.out.println("RENDERING FROM DB");
			UserSessionDAO sm = DataAccessDriver.getInstance().newUserSessionDAO();
			tmpUs = sm.selectById(userSession, JForum.getConnection());
		}

		if (tmpUs == null) {
			System.out.println("CONTROLLERUTILS 202");
			userSession.setLastVisit(new Date(System.currentTimeMillis()));
		}
		else {
			// Update last visit and session start time
			System.out.println("CONTROLLERUTILS 206 "+new Date(tmpUs.getStartTime().getTime() + tmpUs.getSessionTime()));
			userSession.setLastVisit(new Date(tmpUs.getStartTime().getTime() + tmpUs.getSessionTime()));
		}

		// If the execution point gets here, then the user
		// has chosen "autoLogin"
		userSession.setAutoLogin(true);
		SessionFacade.setAttribute("logged", "1");
		//rashmi added
		System.out.println("adding course_id to user session after else part");
		userSession.setCourse_id(ToolManager.getCurrentPlacement().getContext());
		// add end
		I18n.load(user.getLang());
	}*/

	/*protected void configureUserSession(UserSession userSession, User user) throws Exception
	{
		userSession.dataToUser(user);

		 As an user may come back to the forum before its last visit's session expires, we should check for
			existent user information and then, if found, store it to the database before getting his information back.
		
		String sessionId = SessionFacade.isUserInSession(user.getId());

		UserSession tmpUs = new UserSession();
		if (sessionId != null)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("RENDERING FROM CACHE");
			}
			SessionFacade.storeSessionData(sessionId, JForum.getConnection());
			tmpUs = SessionFacade.getUserSession(sessionId);
			SessionFacade.remove(sessionId);
		}
		else
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("RENDERING FROM DB");
			}
			UserSessionDAO sm = DataAccessDriver.getInstance().newUserSessionDAO();
			tmpUs = sm.selectById(userSession, JForum.getConnection());
		}

		if (tmpUs == null)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("CONTROLLERUTILS 202");
			}
		}
		else
		{
			// Update last visit and session start time
			if (logger.isDebugEnabled())
			{
				logger.debug("CONTROLLERUTILS 206 " + new Date(tmpUs.getStartTime().getTime() + tmpUs.getSessionTime()));
			}
		}

		CourseTimeDAO ctimeDao = DataAccessDriver.getInstance().newCourseTimeDAO();

		try
		{
			CourseTimeObj ctimeObj = ctimeDao.selectVisitTime(userSession.getUserId());
			 If this is user's first visit to course, set last visit time to
			 current time and insert a record into db table. If this user's repeat visit,pull the last visit time from db
			 The last visit time is "changed" only when user clicks on mark all as read link
			 
			if (ctimeObj == null)
			{
				userSession.setLastVisit(new Date(System.currentTimeMillis()));
				CourseTimeObj newCtimeObj = new CourseTimeObj();
				newCtimeObj.setCourseId(ToolManager.getCurrentPlacement().getContext());
				newCtimeObj.setUserId(userSession.getUserId());
				newCtimeObj.setVisitTime(new Date(System.currentTimeMillis()));
				ctimeDao.addNew(newCtimeObj);
			}
			else
			{

				userSession.setLastVisit(ctimeObj.getVisitTime());

				CourseTimeObj newCtimeObj = new CourseTimeObj();
				newCtimeObj.setCourseId(ToolManager.getCurrentPlacement().getContext());
				newCtimeObj.setUserId(userSession.getUserId());
				newCtimeObj.setVisitTime(new Date(System.currentTimeMillis()));
				ctimeDao.update(newCtimeObj);

			}
		}
		catch (Exception e)
		{
			logger.error(this.getClass().getName() + ".configureUserSession() : " + e.getMessage(), e);
		}
		try
		{
			CourseTimeObj ctimeObj = ctimeDao.selectMarkAllTime(userSession.getUserId());
			if (ctimeObj == null)
			{
				userSession.setMarkAllTime(null);
			}
			else
			{
				userSession.setMarkAllTime(ctimeObj.getMarkAllTime());
			}
		}
		catch (Exception e)
		{
			logger.error(this.getClass().getName() + ".setMarkAllTime() : " + e.getMessage(), e);
		}

		// If the execution point gets here, then the user has chosen "autoLogin"
		userSession.setAutoLogin(true);
		SessionFacade.setAttribute("logged", "1");
		
		userSession.setCourse_id(ToolManager.getCurrentPlacement().getContext());
		
		I18n.load(user.getLang());
	}*/
	
	protected void configureUserSessionAndVisitTime(UserSession userSession, org.etudes.api.app.jforum.User user) throws Exception
	{
		//userSession.dataToUser(user);

		/* As an user may come back to the forum before its last visit's session expires, we should check for
			existent user information and then, if found, store it to the database before getting his information back.
		*/
		//String sessionId = SessionFacade.isUserInSession(user.getId());

		//UserSession tmpUs = new UserSession();
		/*if (sessionId != null)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("RENDERING FROM CACHE");
			}
			SessionFacade.storeSessionData(sessionId, JForum.getConnection());
			tmpUs = SessionFacade.getUserSession(sessionId);
			SessionFacade.remove(sessionId);
		}
		else
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("RENDERING FROM DB");
			}
			UserSessionDAO sm = DataAccessDriver.getInstance().newUserSessionDAO();
			tmpUs = sm.selectById(userSession, JForum.getConnection());
		}

		if (tmpUs == null)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("CONTROLLERUTILS 202");
			}
		}
		else
		{
			// Update last visit and session start time
			if (logger.isDebugEnabled())
			{
				logger.debug("CONTROLLERUTILS 206 " + new Date(tmpUs.getStartTime().getTime() + tmpUs.getSessionTime()));
			}
		}*/

		/*CourseTimeDAO ctimeDao = DataAccessDriver.getInstance().newCourseTimeDAO();

		try
		{
			CourseTimeObj ctimeObj = ctimeDao.selectVisitTime(userSession.getUserId());
			 If this is user's first visit to course, set last visit time to
			 current time and insert a record into db table. If this user's repeat visit,pull the last visit time from db
			 The last visit time is "changed" only when user clicks on mark all as read link
			 
			if (ctimeObj == null)
			{
				userSession.setLastVisit(new Date(System.currentTimeMillis()));
				CourseTimeObj newCtimeObj = new CourseTimeObj();
				newCtimeObj.setCourseId(ToolManager.getCurrentPlacement().getContext());
				newCtimeObj.setUserId(userSession.getUserId());
				newCtimeObj.setVisitTime(new Date(System.currentTimeMillis()));
				ctimeDao.addNew(newCtimeObj);
			}
			else
			{

				userSession.setLastVisit(ctimeObj.getVisitTime());

				CourseTimeObj newCtimeObj = new CourseTimeObj();
				newCtimeObj.setCourseId(ToolManager.getCurrentPlacement().getContext());
				newCtimeObj.setUserId(userSession.getUserId());
				newCtimeObj.setVisitTime(new Date(System.currentTimeMillis()));
				ctimeDao.update(newCtimeObj);

			}
		}
		catch (Exception e)
		{
			logger.error(this.getClass().getName() + ".configureUserSession() : " + e.getMessage(), e);
		}
		try
		{
			CourseTimeObj ctimeObj = ctimeDao.selectMarkAllTime(userSession.getUserId());
			if (ctimeObj == null)
			{
				userSession.setMarkAllTime(null);
			}
			else
			{
				userSession.setMarkAllTime(ctimeObj.getMarkAllTime());
			}
		}
		catch (Exception e)
		{
			logger.error(this.getClass().getName() + ".setMarkAllTime() : " + e.getMessage(), e);
		}*/
		
		JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");

		/*last visit time - If this is user's first visit to course, set last visit time to current time and insert a record into db table. 
		 * If this user's repeat visit, pull the last visit time from db and update the last visit time to current time*/
		
		Date userSiteLastVisitTime = jforumUserService.getSiteVisitTime(ToolManager.getCurrentPlacement().getContext(), userSession.getSakaiUserId());
		if (userSiteLastVisitTime == null)
		{
			userSession.setLastVisit(new Date());
			
			// add last visit time in the database
			jforumUserService.addModifySiteVisitTime(ToolManager.getCurrentPlacement().getContext(), userSession.getSakaiUserId());
		}
		else
		{
			userSession.setLastVisit(userSiteLastVisitTime);
			
			// update last visit time in the database
			jforumUserService.addModifySiteVisitTime(ToolManager.getCurrentPlacement().getContext(), userSession.getSakaiUserId());

		}
		
		// mark all time
		Date userMarkAllTime = jforumUserService.getMarkAllReadTime(ToolManager.getCurrentPlacement().getContext(), userSession.getUserId());
		userSession.setMarkAllTime(userMarkAllTime);
		 
		// If the execution point gets here, then the user has chosen "autoLogin"
		userSession.setAutoLogin(true);
		SessionFacade.setAttribute("logged", "1");
		
		userSession.setCourse_id(ToolManager.getCurrentPlacement().getContext());
		
		I18n.load(user.getLang());
	}
	
	protected void checkAndIntializeCourse(String context, String sakaiUserId)
	{
		JForumCategoryService jforumCategoryService = (JForumCategoryService)ComponentManager.get("org.etudes.api.app.jforum.JForumCategoryService");
		try
		{
			jforumCategoryService.checkAndAddContextDefaultCategoriesForums(context, sakaiUserId);
		}
		catch (JForumAccessException e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn(e.toString(), e);
			}
		}
		
		/*SakaiUserDAO s = new SakaiUserDAO();
		try
		{

			// Load default categories and forums for admin or facilitator or participant
			if ((s.isUserAdmin() == true) || (s.isUserFacilitator() == true) || (s.isUserParticipant() == true))
			{

				if (logger.isDebugEnabled())
				{
					logger.debug("User is a facilitator or admin or participant");
				}

				if (!ForumRepository.isCourseInitialized())
				{
					ForumRepository.loadDefaultCategoriesAndForums();
					ForumRepository.loadCourseInit();
				}
			}
			
		}
		catch (Exception e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn(e.toString(), e);
			}
			
		}*/
	}

	/**
	 * Checks for user authentication using some SSO implementation
	 * 
	 * Renamed this to updateGroups, since that's what it does -- JMH
	 */
	/*protected void updateGroups(UserSession userSession, SSOUtils utils) {

		SakaiUserDAO s = new SakaiUserDAO();
		
		 * 11/08/05 Murthy - Updated to update user groups if user role changed
		 * in site info - This is to take care of updating user groups after
		 * updating user role in site info and member listing is not clicked
		 
		try {
		if (s.isUserAdmin() == false) {
			if (s.isUserFacilitator()) {
				
				 * if use role is faciliator - get the existing groups if user
				 * has only facilitator group no updates needed. If user belongs
				 * to groups other than facilitator remove them. Remove all
				 * exisitng groups if user belongs to other groups as
				 * Facilitator belongs to only one group(Facilitator group) and
				 * add the Facilitator group
				 
				User usr = utils.getUser();
				updateFacilitator(s, usr);
			} else if (s.isUserParticipant()) {
				
				 * If user belongs to jforum.member get the exisitng user groups
				 * and if don't belong to any group add to Paticipant else check
				 * the groups if the user belongs to Facilitator if belongs
				 * remove the group leave the other groups(realistically if user
				 * belongs to Facilitator he should not have any other groups)
				 
				// newusrdao.addToGroup(exisusr.getId(), new
				// int[]{s.findGroupId("Participant")});
				User usr = utils.getUser();
				updateParticipant(s, usr);
			}
		} else {
			User usr = utils.getUser();
			// 11/16/05 Murthy - For admin add or keep Facilitator group only
			updateFacilitator(s, usr);
		}
		// Mallika's new code end

		// Load default categories and forums for admins or facilitator or participant
		if ((s.isUserAdmin() == true) || (s.isUserFacilitator() == true)
				|| (s.isUserParticipant() == true)) 
		{

			if (logger.isDebugEnabled()) logger.debug("User is a facilitator or admin or participant");
			if ( ! ForumRepository.isCourseInitialized()) 
			{
				// System.out.println("Course is not initialized");
				ForumRepository.loadDefaultCategoriesAndForums();
				ForumRepository.loadCourseInit();
			}

		}
		this.configureUserSession(userSession, utils.getUser());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}*/


	/**
	 * If user belongs to jforum.member get the exisitng user groups and if
	 * don't belong to any group add to Paticipant else check the groups if the
	 * user belongs to Facilitator if belongs remove the group leave the other
	 * groups(realistically if user belongs to Facilitator he should not have
	 * any other groups)
	 * 
	 * @param s
	 * @param usr
	 * @throws Exception
	 */
	/*private void updateParticipant(SakaiUserDAO s, User usr) throws Exception {
		int participantGrpId = s.findGroupId("Participant");
		int facilitatorGrpId = s.findGroupId("Facilitator");
		UserDAO newusrdao = DataAccessDriver.getInstance().newUserDAO();
		SSOUtils utils = new SSOUtils();

		User existinguser = newusrdao.selectById(usr.getId());
		List usrExisGrps = existinguser.getGroupsList();
		Iterator usrExisGrpsIter = usrExisGrps.iterator();

		boolean participantGrp = false;
		while (usrExisGrpsIter.hasNext()) {
			Group usrGrp = (Group) usrExisGrpsIter.next();

			if (facilitatorGrpId == usrGrp.getId()) {
				//remove facilitator group
				newusrdao.removeFromGroup(usr.getId(), new int[] { usrGrp.getId() });
			} else if (participantGrpId == usrGrp.getId()) {
				//participant group existing
				participantGrp = true;
			}
		}

		User existingParuser = newusrdao.selectById(usr.getId());
		List usrParGrps = existingParuser.getGroupsList();
		//if already in Participant group
		if (participantGrp) {
			//do nothing and keep the exisiting groups
			// as is
		} else {
			//add Participant group
			if (usrParGrps.size() == 0)
				newusrdao.addToGroup(usr.getId(), new int[] { s.findGroupId("Participant") });
		}
	}
*/
	/**
	 * if use role is faciliator - get the existing
	 * groups if user has only facilitator group no
	 * updates needed. If user belongs to groups
	 * other than facilitator remove them. Remove
	 * all exisitng groups if user belongs to other
	 * groups as Facilitator belongs to only one
	 * group(Facilitator group) and add the
	 * Facilitator group
	 * @param s
	 * @param usr
	 * @throws Exception
	 */
	/*private void updateFacilitator(SakaiUserDAO s, User usr) throws Exception {
		UserDAO newusrdao = DataAccessDriver.getInstance().newUserDAO();
		User existinguser = newusrdao.selectById(usr.getId());
		List usrExisGrps = existinguser.getGroupsList();
		Iterator usrExisGrpsIter = usrExisGrps.iterator();
		int facilitatorGrpId = s.findGroupId("Facilitator");
		boolean facGrp = false;
		while (usrExisGrpsIter.hasNext()) {
			Group usrGrp = (Group) usrExisGrpsIter.next();

			if (facilitatorGrpId != usrGrp.getId()) {
				newusrdao.removeFromGroup(usr.getId(),
								new int[] { usrGrp.getId() });
			} else
				facGrp = true;
		}

		//if already in Facilitator group
		if (facGrp) {
			User existingfacuser = newusrdao.selectById(usr.getId());
			List usrFacGrps = existingfacuser.getGroupsList();

			if (usrFacGrps != null
					&& usrFacGrps.size() > 1) {
				//keep only one facilitator group
//				Iterator usrFacGrpsIter = usrFacGrps.iterator();
				for (int i = 0; i < usrFacGrps.size() - 1; i++) {
					//Group usrFacGrp =
					// (Group)usrFacGrpsIter.next();
					Group usrFacGrp = (Group) usrFacGrps.get(i);
					newusrdao.removeFromGroup(existingfacuser.getId(),
							new int[] { usrFacGrp.getId() });
				}
			}
		} else {
			//add Facilitator group
			newusrdao.addToGroup(usr.getId(),new int[] { s.findGroupId("Facilitator") });
		}
	}*/


	/**
	 * Do a refresh in the user's session. This method will update the last visit time for the
	 * current user, as well checking for authentication if the session is new or the SSO user has
	 * changed
	 *
	 * @throws Exception
	 */
	public void refreshSession() throws Exception
	{
		UserSession userSession = SessionFacade.getUserSession();

		boolean logged = "1".equals(SessionFacade.getAttribute("logged"));

		if (userSession == null || !logged)
		{
			userSession = new UserSession();
			userSession.setSessionId(JForum.getRequest().getSession().getId());

			// Get the current username from Sakai and populate the jforum user object -- JMH
			org.sakaiproject.user.api.User sakaiUser = UserDirectoryService.getCurrentUser();
			
			JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
			org.etudes.api.app.jforum.User user = jforumUserService.getBySakaiUserId(sakaiUser.getId());
			
			if (user == null)
			{
				try
				{
					// create user in jforum
					jforumUserService.createUser(sakaiUser.getId());
				}
				catch (UserAlreadyExistException e)
				{
				}
				
				user = jforumUserService.getBySakaiUserId(sakaiUser.getId());
			}
			else
			{
				// update user info
				jforumUserService.modifyUserSakaiInfo(user);
				
			}
			
			// copy the user info to the session
			userSession.dataToUser(user);
			
			// check and intialize course
			this.checkAndIntializeCourse(ToolManager.getCurrentPlacement().getContext(), userSession.getSakaiUserId());
			
			//
			this.configureUserSessionAndVisitTime(userSession, user);
			
			SessionFacade.add(userSession, userSession.getSessionId());
			SessionFacade.setAttribute(ConfigKeys.TOPICS_TRACKING, new HashMap<Object, Object>());
					
			/*SSOUtils utils = new SSOUtils();
			User user;

			if (!utils.sakaiUserExists(sakaiUser.getId()))
			{
				// add the user to jforum
				user = new User();
				JForumUtil.copyUser(sakaiUser, user);
				HttpSession session = JForum.getRequest().getSession();
				String password = (String) session.getAttribute(ConfigKeys.SSO_PASSWORD_ATTRIBUTE);
				if (password == null)
				{
					password = SystemGlobals.getValue(ConfigKeys.SSO_DEFAULT_PASSWORD);
				}

				user.setPassword(password);
				user.setActive(1);
				utils.register(user);
			}
			else
			{
				// this user is known in jforum. Update the jforum data with the
				// latest sakai data
				user = new GenericUserDAO().selectBySakaiUserId(sakaiUser.getId());

				JForumUserUtil.updateJFUser(user);
			}
			
			// copy the user info to the session
			userSession.dataToUser(user);

			if (logger.isDebugEnabled())
			{
				logger.debug("controllerUtils.refreshSession - calling update groups");
			}
			this.updateGroups(userSession, utils);
			
			if (logger.isDebugEnabled())
				logger.debug("controllerUtils.refreshSession - calling sessionFacade.add");
			SessionFacade.add(userSession, userSession.getSessionId());
			SessionFacade.setAttribute(ConfigKeys.TOPICS_TRACKING, new HashMap<Object, Object>());
			*/
		}
		else if (ConfigKeys.TYPE_SSO.equals(SystemGlobals.getValue(ConfigKeys.AUTHENTICATION_TYPE)))
		{
			/*if (logger.isDebugEnabled())
			{
				logger.debug("****" + this.getClass().getName() + ".refreshSession()- checking ConfigKeys.TYPE_SSO");
			}

			// We always use the RemoteUserSSO impl -- JMH
			SSO sso = new RemoteUserSSO();
			// SSO sso = (SSO) Class.forName(SystemGlobals.getValue(ConfigKeys.SSO_IMPLEMENTATION)).newInstance();

			// If SSO, then check if the session is valid
			if (!sso.isSessionValid(userSession, JForum.getRequest()))
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("****" + this.getClass().getName() + ".refreshSession()- isSessionValid is false");
				}
				JForum.getRequest().getSession().invalidate();
				SessionFacade.remove(userSession.getSessionId());
				refreshSession();
			}*/
		}
		else
		{
			SessionFacade.getUserSession().updateSessionTime();
		}
	}

	/**
	 * Gets a cookie by its name.
	 *
	 * @param name The cookie name to retrieve
	 * @return The <code>Cookie</code> object if found, or <code>null</code> oterwhise
	 */
	public static Cookie getCookie(String name)
	{
		Cookie[] cookies = JForumBaseServlet.getRequest().getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];

				if (c.getName().equals(name)) {
					return c;
				}
			}
		}

		return null;
	}

	/**
	 * Template method to get a cookie.
	 * Useful to situations when a subclass
	 * wants to have a different way to
	 * retrieve a cookie.
	 * @param name The cookie name to retrieve
	 * @return The Cookie object if found, or null otherwise
	 * @see #getCookie(String)
	 */
	protected Cookie getCookieTemplate(String name)
	{
		return ControllerUtils.getCookie(name);
	}

	/**
	 * Add or update a cookie. This method adds a cookie, serializing its value using XML.
	 *
	 * @param name The cookie name.
	 * @param value The cookie value
	 */
	public static void addCookie(String name, String value)
	{
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(3600 * 24 * 365);
		cookie.setPath("/");

		JForumBaseServlet.getResponse().addCookie(cookie);
	}

	/**
	 * Template method to add a cookie.
	 * Useful to suatins when a subclass wants to add
	 * a cookie in a fashion different than the normal
	 * behaviour
	 * @param name The cookie name
	 * @param value The cookie value
	 * @see #addCookie(String, String)
	 */
	protected void addCookieTemplate(String name, String value)
	{
		ControllerUtils.addCookie(name, value);
	}
}
