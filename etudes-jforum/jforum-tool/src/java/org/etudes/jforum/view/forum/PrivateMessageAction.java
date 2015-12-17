/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/view/forum/PrivateMessageAction.java $ 
 * $Id: PrivateMessageAction.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.jforum.view.forum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.JForumAccessException;
import org.etudes.api.app.jforum.JForumAttachmentBadExtensionException;
import org.etudes.api.app.jforum.JForumAttachmentOverQuotaException;
import org.etudes.api.app.jforum.JForumPrivateMessageService;
import org.etudes.api.app.jforum.JForumSecurityService;
import org.etudes.api.app.jforum.JForumUserService;
import org.etudes.jforum.Command;
import org.etudes.jforum.JForum;
import org.etudes.jforum.SessionFacade;
import org.etudes.jforum.dao.DataAccessDriver;
import org.etudes.jforum.dao.PrivateMessageDAO.PrivateMessageSort;
import org.etudes.jforum.entities.PrivateMessage;
import org.etudes.jforum.entities.PrivateMessageType;
import org.etudes.jforum.entities.User;
import org.etudes.jforum.entities.UserSession;
import org.etudes.jforum.repository.SmiliesRepository;
import org.etudes.jforum.util.I18n;
import org.etudes.jforum.util.concurrent.executor.QueuedExecutor;
import org.etudes.jforum.util.legacy.commons.fileupload.disk.DiskFileItem;
import org.etudes.jforum.util.mail.EmailSenderTask;
import org.etudes.jforum.util.mail.PrivateMessageSpammer;
import org.etudes.jforum.util.preferences.ConfigKeys;
import org.etudes.jforum.util.preferences.SakaiSystemGlobals;
import org.etudes.jforum.util.preferences.SystemGlobals;
import org.etudes.jforum.util.preferences.TemplateKeys;
import org.etudes.jforum.util.user.JForumUserUtil;
import org.etudes.jforum.view.forum.common.AttachmentCommon;
import org.etudes.jforum.view.forum.common.PostCommon;
import org.etudes.jforum.view.forum.common.ViewCommon;
import org.etudes.util.api.RosterAdvisor;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.site.cover.SiteService;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.user.cover.UserDirectoryService;

/**
 * @author Rafael Steil
 */
public class PrivateMessageAction extends Command
{
	private String templateName;

	private static Log logger = LogFactory.getLog(PrivateMessageAction.class);

	public void inbox() throws Exception
	{
		if (!SessionFacade.isLogged()) 
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		User user = new User();
		user.setId(SessionFacade.getUserSession().getUserId());

		JForumPrivateMessageService jforumPrivateMessageService = (JForumPrivateMessageService)ComponentManager.get("org.etudes.api.app.jforum.JForumPrivateMessageService");
		
		List<org.etudes.api.app.jforum.PrivateMessage> pmList = jforumPrivateMessageService.inbox(ToolManager.getCurrentPlacement().getContext(), UserDirectoryService.getCurrentUser().getId());
		
		sortPrivateMessageList(pmList, true);
		
		this.context.put("inbox", true);
		this.context.put("pmList", pmList);
		
		this.setTemplateName(TemplateKeys.PM_INBOX);
		this.putTypes();
	}

	public void sentbox() throws Exception
	{
		if (!SessionFacade.isLogged()) {
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		User user = new User();
		user.setId(SessionFacade.getUserSession().getUserId());

		JForumPrivateMessageService jforumPrivateMessageService = (JForumPrivateMessageService)ComponentManager.get("org.etudes.api.app.jforum.JForumPrivateMessageService");
		
		List<org.etudes.api.app.jforum.PrivateMessage> pmList = jforumPrivateMessageService.sentbox(ToolManager.getCurrentPlacement().getContext(), UserDirectoryService.getCurrentUser().getId());
		
		sortPrivateMessageList(pmList, false);

		this.context.put("sentbox", true);
		this.context.put("pmList", pmList);
		this.setTemplateName(TemplateKeys.PM_SENTBOX);
		this.putTypes();
	}

	private void putTypes()
	{
		this.context.put("NEW", new Integer(PrivateMessageType.NEW));
		this.context.put("READ", new Integer(PrivateMessageType.READ));
		this.context.put("UNREAD", new Integer(PrivateMessageType.UNREAD));
		this.context.put("PRIORITY_HIGH", new Integer(PrivateMessage.PRIORITY_HIGH));
	}

	public void send() throws Exception
	{
		if (!SessionFacade.isLogged())
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}
		
		//JForumPrivateMessageService jforumPrivateMessageService = (JForumPrivateMessageService)ComponentManager.get("org.etudes.api.app.jforum.JForumPrivateMessageService");

		/*User user = DataAccessDriver.getInstance().newUserDAO().selectById(SessionFacade.getUserSession().getUserId());
		user.setSignature(PostCommon.processText(user.getSignature()));
		user.setSignature(PostCommon.processSmilies(user.getSignature(), SmiliesRepository.getSmilies()));

		this.sendFormCommon(user);*/
		JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		
		org.etudes.api.app.jforum.User user = jforumUserService.getBySakaiUserId(UserDirectoryService.getCurrentUser().getId());
		this.sendFormCommon(user);
		
		List<org.etudes.api.app.jforum.User> users = null;
		try
		{
			users = jforumUserService.getSiteUsers(ToolManager.getCurrentPlacement().getContext(), 0, 0);
			
			//users = JForumUserUtil.updateMembersInfo();
			this.context.put("users", users);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e.toString(), e);
			}
			this.context.put("users", new ArrayList());
		}
		// 12/04/06 - Murthy - Maximum number of users allowed to copy in Private Messages
		// this.context.put("maxPMToUsers", new
		// Integer(SystemGlobals.getIntValue(ConfigKeys.MAX_PM_TOUSERS)));
		// this.context.put("maxPMToUsers", new Integer(SakaiSystemGlobals.getIntValue(ConfigKeys.MAX_PM_TOUSERS)));
		boolean facilitator = JForumUserUtil.isJForumFacilitator(user.getSakaiUserId());
		this.context.put("facilitator", facilitator);
	}
	
	
	public void sendTo() throws Exception
	{
		if (!SessionFacade.isLogged()) 
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		// User user = DataAccessDriver.getInstance().newUserDAO().selectById(SessionFacade.getUserSession().getUserId());
		JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		
		org.etudes.api.app.jforum.User user = jforumUserService.getBySakaiUserId(UserDirectoryService.getCurrentUser().getId());

		int userId = this.request.getIntParameter("user_id");
		
		if (userId > 0)
		{
			//User user1 = DataAccessDriver.getInstance().newUserDAO().selectById(userId);
			org.etudes.api.app.jforum.User user1 = jforumUserService.getByUserId(userId);
			JForumSecurityService jforumSecurityService = (JForumSecurityService)ComponentManager.get("org.etudes.api.app.jforum.JForumSecurityService");
			
			if (!jforumSecurityService.isUserActive(ToolManager.getCurrentPlacement().getContext(), user1.getSakaiUserId()) || jforumSecurityService.isUserBlocked(ToolManager.getCurrentPlacement().getContext(), user1.getSakaiUserId()))
			{
				this.setTemplateName(TemplateKeys.PM_SENDSAVE_USER_NOTFOUND);
				this.context.put("message", I18n.getMessage("PrivateMessage.userInactive"));
				return;
			}
			else
			{
				boolean facilitator = JForumUserUtil.isJForumFacilitator(user1.getSakaiUserId());
				boolean participant = JForumUserUtil.isJForumParticipant(user1.getSakaiUserId());
				if (!facilitator && !participant)
				{
					this.setTemplateName(TemplateKeys.PM_SENDSAVE_USER_NOTFOUND);
					this.context.put("message", I18n.getMessage("PrivateMessage.userInactive"));
					return;
				}
			}

			
			this.context.put("pmRecipient", user1);
			this.context.put("toUserId", String.valueOf(user1.getId()));

            this.context.put("toUsername", user1.getFirstName()+" "+user1.getLastName());
			
			this.context.put("toUserEmail", user1.getEmail());
			
			this.context.put("pageTitle", SystemGlobals.getValue(ConfigKeys.FORUM_NAME) + " - " + I18n.getMessage("PrivateMessage.title"));
		}
		else
		{
			this.setTemplateName(TemplateKeys.PM_SENDSAVE_USER_NOTFOUND);
			this.context.put("message", I18n.getMessage("PrivateMessage.PrivateMessage.userNotFound"));
			return;
		}

		this.sendFormCommon(user);
		
		this.context.put("action", "sendPMSave");
		this.setTemplateName(TemplateKeys.PM_SENDTO);
	}

	private void sendFormCommon(User user)
	{
		this.context.put("user", user);
		this.context.put("moduleName", "pm");
		this.context.put("action", "sendSave");
		this.setTemplateName(TemplateKeys.PM_SENDFORM);
		//this.context.put("htmlAllowed", true);
		// this.context.put("maxAttachments", SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_MAX_POST));
		this.context.put("maxAttachments", SakaiSystemGlobals.getValue(ConfigKeys.ATTACHMENTS_MAX_POST));
		//12/11/2007 Murthy - attachments enabled
		this.context.put("attachmentsEnabled", true);
		this.context.put("maxAttachmentsSize", new Integer(0));
		this.context.put("pmPost", true);
	}
	
	private void sendFormCommon(org.etudes.api.app.jforum.User user)
	{
		this.context.put("user", user);
		this.context.put("moduleName", "pm");
		this.context.put("action", "sendSave");
		this.setTemplateName(TemplateKeys.PM_SENDFORM);
		//this.context.put("htmlAllowed", true);
		// this.context.put("maxAttachments", SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_MAX_POST));
		this.context.put("maxAttachments", SakaiSystemGlobals.getValue(ConfigKeys.ATTACHMENTS_MAX_POST));
		//12/11/2007 Murthy - attachments enabled
		this.context.put("attachmentsEnabled", true);
		this.context.put("maxAttachmentsSize", new Integer(0));
		this.context.put("pmPost", true);
	}

	public void sendSave() throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("PrivateMessageAction - sendSave");
		}
			
		if (!SessionFacade.isLogged())
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}
		
		try
		{
			sendPrivateMessage();
		}
		catch (JForumAccessException e)
		{
			this.send();
			return;
		}
		catch (JForumAttachmentOverQuotaException e)
		{
			this.send();
			return;
		}
		catch (JForumAttachmentBadExtensionException e)
		{
			this.send();
			return;
		}

		/*String sId = this.request.getParameter("toUserId");
		String toUsername = this.request.getParameter("toUsername");
		String userEmail = this.request.getParameter("toUserEmail");

		boolean pmEmailEnabled = false;

		PrivateMessage pm = new PrivateMessage();
		pm.setPost(PostCommon.fillPostFromRequest());

		if ((this.request.getParameter("high_priority_pm") != null) && (this.request.getIntParameter("high_priority_pm") == 1))
		{
			pm.setPriority(PrivateMessage.PRIORITY_HIGH);
		}
		else
		{
			pm.setPriority(PrivateMessage.PRIORITY_GENERAL);
		}

		User fromUser = DataAccessDriver.getInstance().newUserDAO().selectById(SessionFacade.getUserSession().getUserId());
		pm.setFromUser(fromUser);

		boolean preview = (this.request.getParameter("preview") != null && this.request.getParameter("preview").trim().length() > 0);

		if (preview)
		{
			if (sId == null || sId.trim().equals(""))
			{

				String toUserIds[] = (String[]) this.request.getObjectParameter("toUsername" + "ParamValues");
				if (toUserIds != null)
				{
					List toUsers = new ArrayList();

					for (int i = 0; i < toUserIds.length; i++)
					{
						if (toUserIds[i] != null && toUserIds[i].trim().length() > 0)
						{

							int toUserId = Integer.parseInt(toUserIds[i]);
							User toUser = DataAccessDriver.getInstance().newUserDAO().selectById(toUserId);
							// pm.setToUser(toUser);
							toUsers.add(toUser);
						}
					}
					this.context.put("toUsers", toUsers);
				}
				else
				{
					List toUsers = new ArrayList();

					int toUserId = Integer.parseInt(toUsername);
					User toUser = DataAccessDriver.getInstance().newUserDAO().selectById(toUserId);
					// pm.setToUser(toUser);
					toUsers.add(toUser);
					this.context.put("toUsers", toUsers);
				}
			}
			else
			{
				// PM quote reply
				int toUserId = Integer.parseInt(sId);
				User usr = DataAccessDriver.getInstance().newUserDAO().selectById(toUserId);
				toUsername = usr.getFirstName() + " " + usr.getLastName();
				pmEmailEnabled = usr.isNotifyPrivateMessagesEnabled();

				String exisPmId = this.request.getParameter("exisPmId");

				if (exisPmId != null && exisPmId.trim().length() > 0)
				{
					PrivateMessage exisPm = new PrivateMessage();
					exisPm.setId(Integer.parseInt(exisPmId));

					exisPm = DataAccessDriver.getInstance().newPrivateMessageDAO().selectById(exisPm);

					pm.setFromUser(exisPm.getFromUser());
					pm.setToUser(exisPm.getToUser());
					pm.setId(exisPm.getId());
				}
			}
			this.context.put("preview", true);
			this.context.put("post", pm.getPost());

			Post postPreview = new Post(pm.getPost());
			this.context.put("postPreview", PostCommon.preparePostForDisplay(postPreview));
			this.context.put("pm", pm);
			// this.context.put("maxPMToUsers", new
			// Integer(SakaiSystemGlobals.getIntValue(ConfigKeys.MAX_PM_TOUSERS)));

			List users = null;
			try
			{
				users = JForumUserUtil.updateMembersInfo();
				this.context.put("users", users);
			}
			catch (Exception e)
			{
				if (logger.isErrorEnabled())
					logger.error(e.toString());
			}

			this.send();
			return;
		}

		// save PM attachments only once
		int attachmentIds[] = null;
		if (!preview)
		{
			try
			{
				attachmentIds = addPMAttachments();
			}
			catch (Exception e)
			{
				this.context.put("post", pm.getPost());
				this.context.put("pm", pm);
				this.send();
				return;
			}
		}

		int toUserId = -1;
		boolean messageSent = false;
		if (sId == null || sId.trim().equals(""))
		{

			String toUserIds[] = (String[]) this.request.getObjectParameter("toUsername" + "ParamValues");
			if (toUserIds != null)
			{

				for (int i = 0; i < toUserIds.length; i++)
				{
					if (toUserIds[i] != null && toUserIds[i].trim().length() > 0)
					{
						toUserId = Integer.parseInt(toUserIds[i]);
						User usr = DataAccessDriver.getInstance().newUserDAO().selectById(toUserId);
						toUserId = usr.getId();
						userEmail = usr.getEmail();
						toUsername = usr.getFirstName() + " " + usr.getLastName();
						pmEmailEnabled = usr.isNotifyPrivateMessagesEnabled();
						sendPrivateMessage(pm, toUsername, userEmail, pmEmailEnabled, toUserId, attachmentIds);
						messageSent = true;
					}
				}
			}
			else
			{
				toUserId = Integer.parseInt(toUsername);
				if (toUserId != -1)
				{
					User usr = DataAccessDriver.getInstance().newUserDAO().selectById(toUserId);
					toUserId = usr.getId();
					userEmail = usr.getEmail();
					toUsername = usr.getFirstName() + " " + usr.getLastName();
					pmEmailEnabled = usr.isNotifyPrivateMessagesEnabled();
					sendPrivateMessage(pm, toUsername, userEmail, pmEmailEnabled, toUserId, attachmentIds);
					messageSent = true;
				}
			}
		}
		else
		{
			toUserId = Integer.parseInt(sId);
			if (toUserId != -1)
			{
				// Mallika - new code beg
				// Needed to do this because otherwise emailEnabled is not used
				User usr = DataAccessDriver.getInstance().newUserDAO().selectById(toUserId);
				toUsername = usr.getFirstName() + " " + usr.getLastName();
				pmEmailEnabled = usr.isNotifyPrivateMessagesEnabled();
				// Mallika - new code end
	
				sendPrivateMessage(pm, toUsername, userEmail, pmEmailEnabled, toUserId, attachmentIds);
				messageSent = true;
			}
		}

		// update private message status to replied
		if (!preview)
		{
			String exisPmId = this.request.getParameter("exisPmId");

			if (exisPmId != null && exisPmId.trim().length() > 0 && messageSent)
			{
				PrivateMessage exisPm = new PrivateMessage();
				exisPm.setId(Integer.parseInt(exisPmId));

				exisPm = DataAccessDriver.getInstance().newPrivateMessageDAO().selectById(exisPm);
				exisPm.setReplied(true);
				DataAccessDriver.getInstance().newPrivateMessageDAO().updateRepliedStatus(exisPm);
			}
		}

		// boolean preview = (this.request.getParameter("preview") != null);
		// if (logger.isDebugEnabled()) logger.debug("Preview is "+preview);
		if (preview)
		{
			this.context.put("preview", true);
			this.context.put("post", pm.getPost());

			Post postPreview = new Post(pm.getPost());
			this.context.put("postPreview", PostCommon.preparePostForDisplay(postPreview));
			this.context.put("pm", pm);

			this.send();
		}
		else
		{
			this.setTemplateName(TemplateKeys.PM_SENDSAVE);

			this.context.put("message", I18n.getMessage("PrivateMessage.messageSent", new String[] { this.request.getContextPath()
					+ "/pm/inbox/date/d" + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) }));
		}*/
	}
	
	
	/**
	 * save private message sent to individual user from pop up window
	 * @throws Exception
	 */
	public void sendPMSave() throws Exception
	{
		if (!SessionFacade.isLogged()) 
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		String sId = this.request.getParameter("toUserId");
		String toUsername = this.request.getParameter("toUsername");
		String userEmail = this.request.getParameter("toUserEmail");
		
		boolean preview = (this.request.getParameter("preview") != null && this.request.getParameter("preview").trim().length() > 0);
		
		JForumPrivateMessageService jforumPrivateMessageService = null;
		jforumPrivateMessageService = (JForumPrivateMessageService)ComponentManager.get("org.etudes.api.app.jforum.JForumPrivateMessageService");
		
		if (jforumPrivateMessageService == null) return;
		
		JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		
		// to user
		org.etudes.api.app.jforum.User toUser = jforumUserService.getByUserId(Integer.parseInt(sId));		
		
		org.etudes.api.app.jforum.PrivateMessage privateMessage = null;
		
		List<String> toSakaiUserIds =  new ArrayList<String>();
		
		privateMessage = jforumPrivateMessageService.newPrivateMessage(UserDirectoryService.getCurrentUser().getId(), toUser.getSakaiUserId());
		
		toSakaiUserIds.add(toUser.getSakaiUserId());
		
		privateMessage.setContext(ToolManager.getCurrentPlacement().getContext());
		
		if ((this.request.getParameter("high_priority_pm") != null) && (this.request.getIntParameter("high_priority_pm") == 1))
		{
			privateMessage.setPriority(org.etudes.api.app.jforum.PrivateMessage.PRIORITY_HIGH);
		}
		else
		{
			privateMessage.setPriority(org.etudes.api.app.jforum.PrivateMessage.PRIORITY_GENERAL);
		}
		
		org.etudes.api.app.jforum.Post post = privateMessage.getPost();
		
		post.setTime(new Date());
		
		post.setSubject(this.request.getParameter("subject"));
		
		post.setBbCodeEnabled(this.request.getParameter("disable_bbcode") != null ? false : true);
		post.setSmiliesEnabled(this.request.getParameter("disable_smilies") != null ? false : true);
		post.setSignatureEnabled(this.request.getParameter("attach_sig") != null ? true : false);
		
		String message = this.request.getParameter("message");
		post.setText(message);
		
		/*String modMessage = cleanMessage(message);
		post.setText(modMessage);		
		
		post.setHtmlEnabled(this.request.getParameter("disable_html") != null);
		
		if (post.isHtmlEnabled())
		{
			post.setText(SafeHtml.makeSafe(message));
		}
		else
		{
			post.setText(message);
		}		*/
		
		//process post attachments
		processPostAttachments(jforumPrivateMessageService, privateMessage);
		
		if (toSakaiUserIds.size() == 1)
		{
			//jforumPrivateMessageService.sendPrivateMessage(privateMessage);
			try
			{
				jforumPrivateMessageService.sendPrivateMessageWithAttachments(privateMessage);
			}
			catch (JForumAccessException e)
			{
				JForum.enableCancelCommit();
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("postPreview", privateMessage);
				this.request.addParameter("user_id", sId);
				this.sendTo();
				this.context.put("savesucess", false);
				return;
			}
			catch (JForumAttachmentOverQuotaException e)
			{
				JForum.enableCancelCommit();
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("postPreview", privateMessage);
				this.request.addParameter("user_id", sId);
				this.sendTo();
				this.context.put("savesucess", false);
				return;
			}
			catch (JForumAttachmentBadExtensionException e)
			{
				JForum.enableCancelCommit();
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("postPreview", privateMessage);
				this.request.addParameter("user_id", sId);
				this.sendTo();
				this.context.put("savesucess", false);
				return;
			}
		}
		
		/*boolean pmEmailEnabled = false;
		
		PrivateMessage pm = new PrivateMessage();
		pm.setPost(PostCommon.fillPostFromRequest());

		if ((this.request.getParameter("high_priority_pm") != null) && (this.request.getIntParameter("high_priority_pm") == 1))
		{
			pm.setPriority(PrivateMessage.PRIORITY_HIGH);
		}
		else
		{
			pm.setPriority(PrivateMessage.PRIORITY_GENERAL);
		}
		
		if (preview) 
		{
			this.context.put("postPreview", pm);
			this.context.put("preview", true);
			this.request.addParameter("user_id", sId);
			this.sendTo();
			return;
		}
		
		int attachmentIds[] = null;
		try
		{
			attachmentIds = addPMAttachments();
		}
		catch (Exception e)
		{
			this.request.addParameter("user_id", sId);
			this.sendTo();
			return;
		}			
		int toUserId = Integer.parseInt(sId);
       	User usr = DataAccessDriver.getInstance().newUserDAO().selectById(toUserId);
		toUsername = usr.getFirstName()+ " "+usr.getLastName();
		pmEmailEnabled = usr.isNotifyPrivateMessagesEnabled();
		
		sendPrivateMessage(pm, toUsername, userEmail, pmEmailEnabled, toUserId, attachmentIds);*/

		this.context.put("savesucess", true);
		this.setTemplateName(TemplateKeys.PM_SENDTOSAVE);
	}
	
	/**
	 * sorts the PM list
	 * 
	 * @param pmList	THe PM List
	 */
	protected void sortPrivateMessageList(List<org.etudes.api.app.jforum.PrivateMessage> pmList, final boolean fromUser)
	{
		String sortColumn = this.request.getParameter("sort_column");
		String sortDirection = this.request.getParameter("sort_direction");
		
		PrivateMessageSort pmSort = PrivateMessageSort.last_name_a;
		
		if (sortColumn != null && sortDirection != null)
		{
			if (sortColumn.equals("name") && sortDirection.equals("a"))
			{
				pmSort = PrivateMessageSort.last_name_a;
			}
			else if (sortColumn.equals("name") && sortDirection.equals("d"))
			{
				pmSort = PrivateMessageSort.last_name_d;
			}
			else if (sortColumn.equals("date") && sortDirection.equals("a"))
			{
				pmSort = PrivateMessageSort.date_a;
			}
			else if (sortColumn.equals("date") && sortDirection.equals("d"))
			{
				pmSort = PrivateMessageSort.date_d;
			}			
		} 
		else 
		{
			sortColumn = "name";
			sortDirection = "a";
		}
		
		applySort(pmList, pmSort, fromUser);
		
		this.context.put("sort_column", sortColumn);
		this.context.put("sort_direction", sortDirection);
	}
	
	/**
	 * save attachments
	 * @return attachment Id's
	 * @throws Exception
	 */
	private int[] addPMAttachments() throws Exception
	{
		AttachmentCommon attachments = new AttachmentCommon(this.request);

		try
		{
			attachments.preProcess();
		}
		catch (Exception e)
		{
			JForum.enableCancelCommit();
			this.context.put("errorMessage", e.getMessage());
			throw e;
		}

		return attachments.insertPMAttachments();
	}

	/**
	 * send private message
	 * @param pm - PrivateMessage
	 * @param toUsername - to user name
	 * @param userEmail - user email
	 * @param pmEmailEnabled - is pm enabled
	 * @param toUserId - to user id
	 * @param attachmentIds - attachment Id's
	 * @throws Exception
	 */
	private void sendPrivateMessage(PrivateMessage pm, String toUsername, String userEmail, boolean pmEmailEnabled, int toUserId, int attachmentIds[]) throws Exception {
		if (toUserId == -1) {
			this.setTemplateName(TemplateKeys.PM_SENDSAVE_USER_NOTFOUND);
			this.context.put("message", I18n.getMessage("PrivateMessage.userIdNotFound"));
			return;
		}

		User fromUser = DataAccessDriver.getInstance().newUserDAO().selectById(SessionFacade.getUserSession().getUserId());
		pm.setFromUser(fromUser);

		User toUser = new User();
		toUser.setId(toUserId);
		toUser.setUsername(toUsername);
		toUser.setEmail(userEmail);
		pm.setToUser(toUser);

		boolean preview = (this.request.getParameter("preview") != null);

		if (!preview) {
			//DataAccessDriver.getInstance().newPrivateMessageDAO().send(pm);
			DataAccessDriver.getInstance().newPrivateMessageDAO().saveMessage(pm, attachmentIds);

			/*this.setTemplateName(TemplateKeys.PM_SENDSAVE);
			this.context.put("message", I18n.getMessage("PrivateMessage.messageSent",
							new String[] { this.request.getContextPath() +"/pm/inbox"
											+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION)}));*/

			// If the target user if in the forum, then increments its
			// private messate count
			String sid = SessionFacade.isUserInSession(toUserId);
			if (sid != null) {
				UserSession us = SessionFacade.getUserSession(sid);
				us.setPrivateMessages(us.getPrivateMessages() + 1);
			}
			
			if (logger.isDebugEnabled()) logger.debug("Before userEmail");
			if (userEmail != null && userEmail.trim().length() > 0) {
				if (logger.isDebugEnabled()) logger.debug("Useremail is not null "+pmEmailEnabled);
				//Mallika-commenting line below and going based off of settings instead
				/*if (SystemGlobals.getBoolValue(ConfigKeys.MAIL_NOTIFY_ANSWERS)) {*/
				if ((pmEmailEnabled == true) || (pm.getPriority() == PrivateMessage.PRIORITY_HIGH)) {
					if (logger.isDebugEnabled()) logger.debug("Sending email");
					
					try	{
						new InternetAddress(toUser.getEmail());
					} catch (AddressException e) {
						if (logger.isWarnEnabled()) logger.warn("sendPrivateMessage(...) : "+ toUser.getEmail() + " is invalid. And exception is : "+ e);
						return;
					}
					
					//get attachments
					List attachments = DataAccessDriver.getInstance().newAttachmentDAO().selectPMAttachments(pm.getId());
					try {
						if (attachments != null && attachments.size() > 0)
						{
							QueuedExecutor.getInstance().execute(new EmailSenderTask(new PrivateMessageSpammer(toUser, pm, attachments)));
						}
						else
						{
							QueuedExecutor.getInstance().execute(new EmailSenderTask(new PrivateMessageSpammer(toUser, pm)));
						}
					}
					catch (Exception e) {
						logger.error(this.getClass().getName() +
								".sendSave() : " + e.getMessage(), e);
					}
				}
			}
		}
	}
	
	public void findUser() throws Exception
	{
		boolean showResult = false;
		String username = this.request.getParameter("username");

		if (username != null && !username.equals("")) {
			List namesList = DataAccessDriver.getInstance().newUserDAO().findByName(username, false);
			this.context.put("namesList", namesList);
			showResult = true;
		}

		this.setTemplateName(TemplateKeys.PM_FIND_USER);

		this.context.put("username", username);
		this.context.put("showResult", showResult);
	}

	//Mallika's new code beg
	public void findFluser() throws Exception
	{
		boolean showResult = false;
		String username = this.request.getParameter("username");

		if (username != null && !username.equals("")) {
			List namesList = DataAccessDriver.getInstance().newUserDAO().findByFlName(username, false);
			this.context.put("namesList", namesList);
			showResult = true;
		}

		this.setTemplateName(TemplateKeys.PM_FIND_USER);

		this.context.put("username", username);
		this.context.put("showResult", showResult);
	}
	//Mallika's new code end

	
	public void read() throws Exception
	{
		if (!SessionFacade.isLogged()) {
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		int id = this.request.getIntParameter("id");
		
		// Don't allow the read of messages that don't belongs to the current user
		UserSession us = SessionFacade.getUserSession();
		int userId = us.getUserId();
		
		JForumPrivateMessageService jforumPrivateMessageService = (JForumPrivateMessageService)ComponentManager.get("org.etudes.api.app.jforum.JForumPrivateMessageService");
				
		org.etudes.api.app.jforum.PrivateMessage pm = jforumPrivateMessageService.getPrivateMessage(id);
		
		if (pm.getToUser().getId() == userId || pm.getFromUser().getId() == userId) 
		{
			// Update the message status, if needed
			if (pm.getType() == PrivateMessageType.NEW)
			{
				pm.setType(PrivateMessageType.READ);
				jforumPrivateMessageService.markMessageRead(pm.getId(), us.getSakaiUserId(), null, true);
				us.setPrivateMessages(us.getPrivateMessages() - 1);
			}

			org.etudes.api.app.jforum.User u = pm.getFromUser();
			//u.setSignature(PostCommon.processText(u.getSignature()));
			u.setSignature(PostCommon.processSmilies(u.getSignature(), SmiliesRepository.getSmilies()));

			this.context.put("pm", pm);
			this.setTemplateName(TemplateKeys.PM_READ);
		}
		else
		{
			this.setTemplateName(TemplateKeys.PM_READ_DENIED);
			this.context.put("message", I18n.getMessage("PrivateMessage.readDenied"));
		}		
	}

	public void review() throws Exception
	{
		this.read();
		this.setTemplateName(TemplateKeys.PM_READ_REVIEW);
	}

	public void delete() throws Exception
	{
		if (!SessionFacade.isLogged()) 
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		String ids[] = this.request.getParameterValues("id");

		if (ids != null && ids.length > 0)
		{
			JForumPrivateMessageService jforumPrivateMessageService = (JForumPrivateMessageService)ComponentManager.get("org.etudes.api.app.jforum.JForumPrivateMessageService");
			List<Integer> privateMessageIds = new ArrayList<Integer>();
			
			for (int i = 0; i < ids.length; i++)
			{
				privateMessageIds.add(Integer.parseInt(ids[i]));
			}
			
			try
			{
				jforumPrivateMessageService.deleteMessage(ToolManager.getCurrentPlacement().getContext(), UserDirectoryService.getCurrentUser().getId(), privateMessageIds);
			}
			catch (JForumAccessException e)
			{
				
			}
			
			/*PrivateMessage[] pm = new PrivateMessage[ids.length];
			User u = new User();
			u.setId(SessionFacade.getUserSession().getUserId());

			for (int i = 0; i < ids.length; i++)
			{
				pm[i] = new PrivateMessage();
				pm[i].setFromUser(u);
				pm[i].setId(Integer.parseInt(ids[i]));
			}

			// delete attachments if any
			if (pm != null)
			{
				for (int i = 0; i < pm.length; i++)
				{
					new AttachmentCommon(this.request).deletePMAttachments(pm[i].getId());
				}
			}

			DataAccessDriver.getInstance().newPrivateMessageDAO().delete(pm);*/
		}

		this.setTemplateName(TemplateKeys.PM_DELETE);
		this.context.put("message", I18n.getMessage("PrivateMessage.deleteDone",
						new String[] { this.request.getContextPath() + "/pm/inbox/date/d"
										+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) }));
	}
	
	public void reply() throws Exception
	{
		if (!SessionFacade.isLogged()) {
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		int id = this.request.getIntParameter("id");

		PrivateMessage pm = new PrivateMessage();
		pm.setId(id);
		pm = DataAccessDriver.getInstance().newPrivateMessageDAO().selectById(pm);

		int userId = SessionFacade.getUserSession().getUserId();

		if (pm.getToUser().getId() != userId && pm.getFromUser().getId() != userId) {
			this.setTemplateName(TemplateKeys.PM_READ_DENIED);
			this.context.put("message", I18n.getMessage("PrivateMessage.readDenied"));
			return;
		}
		//Mallika - adding this if to strip out additional Res
		//Only add Re: at beginning if this is the first reply
		if (pm.getPost().getSubject().length() >= 3)
		{
		  if (!(pm.getPost().getSubject().substring(0,3).equals("Re:")))
		  {
		    pm.getPost().setSubject(I18n.getMessage("PrivateMessage.replyPrefix") + pm.getPost().getSubject());
		  }
	    }
		else
		{
			 pm.getPost().setSubject(I18n.getMessage("PrivateMessage.replyPrefix") + pm.getPost().getSubject());
			 
		}


		this.context.put("pm", pm);
		this.context.put("pmReply", true);

		this.sendFormCommon(DataAccessDriver.getInstance().newUserDAO().selectById(
				SessionFacade.getUserSession().getUserId()));
	}

	public void quote() throws Exception
	{
		if (!SessionFacade.isLogged()) {
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		int id = this.request.getIntParameter("id");

		PrivateMessage pm = new PrivateMessage();
		pm.setId(id);
		pm = DataAccessDriver.getInstance().newPrivateMessageDAO().selectById(pm);

		int userId = SessionFacade.getUserSession().getUserId();

		if (pm.getToUser().getId() != userId && pm.getFromUser().getId() != userId) {
			this.setTemplateName(TemplateKeys.PM_READ_DENIED);
			this.context.put("message", I18n.getMessage("PrivateMessage.readDenied"));
			return;
		}
       
		//Mallika - adding this if to strip out additional Res
		//Only add Re: at beginning if this is the first reply
		if (pm.getPost().getSubject().length() >= 3)
		{
		  if (!(pm.getPost().getSubject().substring(0,3).equals("Re:")))
		  {
		    pm.getPost().setSubject(I18n.getMessage("PrivateMessage.replyPrefix") + pm.getPost().getSubject());
		  }
	    }
		else
		{
			 pm.getPost().setSubject(I18n.getMessage("PrivateMessage.replyPrefix") + pm.getPost().getSubject());
			 
		}
		
		this.sendFormCommon(DataAccessDriver.getInstance().newUserDAO().selectById(userId));

		this.context.put("quote", "true");
		this.context.put("quoteUser", pm.getFromUser());
		this.context.put("post", pm.getPost());
		this.context.put("pm", pm);
	}
	
	/**
	 * flag or clear the flag of the PM to follow up
	 * @throws Exception
	 */
	public void flag() throws Exception
	{
		if (!SessionFacade.isLogged())
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		int id = this.request.getIntParameter("id");

		JForumPrivateMessageService jforumPrivateMessageService = (JForumPrivateMessageService) ComponentManager.get("org.etudes.api.app.jforum.JForumPrivateMessageService");
		org.etudes.api.app.jforum.PrivateMessage pm = jforumPrivateMessageService.getPrivateMessage(id);

		// Don't allow the flag the message that don't belongs to the current user
		UserSession us = SessionFacade.getUserSession();
		int userId = us.getUserId();
		if (pm.getToUser().getId() == userId || pm.getFromUser().getId() == userId)
		{

			// Update the flag
			pm.setFlagToFollowup(!pm.isFlagToFollowup());
			jforumPrivateMessageService.flagToFollowup(pm);
		}
		else
		{
			this.setTemplateName(TemplateKeys.PM_FLAG_FOLLOWUP_DENIED);
			this.context.put("message", I18n.getMessage("PrivateMessage.flagToFollowUpDenied"));
		}
		this.read();
	}
	
	/**
	 * add the flag of the PM to follow up
	 * @throws Exception
	 */
	public void addFlag() throws Exception
	{
		if (!SessionFacade.isLogged())
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		String ids[] = this.request.getParameterValues("id");
		UserSession us = SessionFacade.getUserSession();
		int userId = us.getUserId();
		
		JForumPrivateMessageService jforumPrivateMessageService = (JForumPrivateMessageService)ComponentManager.get("org.etudes.api.app.jforum.JForumPrivateMessageService");
		
		if (ids != null && ids.length > 0)
		{
			for (int i = 0; i < ids.length; i++)
			{

				org.etudes.api.app.jforum.PrivateMessage pm = jforumPrivateMessageService.getPrivateMessage(Integer.parseInt(ids[i]));

				if (pm == null)
					continue;

				if (pm.getToUser().getId() == userId || pm.getFromUser().getId() == userId)
				{

					// Update the flag
					pm.setFlagToFollowup(true);

					jforumPrivateMessageService.flagToFollowup(pm);
				}
			}
		}

		this.setTemplateName(TemplateKeys.PM_FLAGGED);
		this.context.put("message", I18n.getMessage("PrivateMessage.flaggingDone",
						new String[] { this.request.getContextPath() + "/pm/inbox/date/d"
										+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) }));
	}
	
	/**
	 * clear the flag of the PM to follow up
	 * @throws Exception
	 */
	public void clearFlag() throws Exception
	{
		if (!SessionFacade.isLogged())
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		String ids[] = this.request.getParameterValues("id");
		UserSession us = SessionFacade.getUserSession();
		int userId = us.getUserId();

		JForumPrivateMessageService jforumPrivateMessageService = (JForumPrivateMessageService) ComponentManager.get("org.etudes.api.app.jforum.JForumPrivateMessageService");

		if (ids != null && ids.length > 0)
		{
			for (int i = 0; i < ids.length; i++)
			{

				org.etudes.api.app.jforum.PrivateMessage pm = jforumPrivateMessageService.getPrivateMessage(Integer.parseInt(ids[i]));

				if (pm == null)
					continue;

				if (pm.getToUser().getId() == userId || pm.getFromUser().getId() == userId)
				{

					// Update the flag
					pm.setFlagToFollowup(false);
					jforumPrivateMessageService.flagToFollowup(pm);
				}
			}
		}

		this.setTemplateName(TemplateKeys.PM_FLAGGED);
		this.context.put( "message", I18n.getMessage("PrivateMessage.flaggingDone",
						new String[] { this.request.getContextPath() + "/pm/inbox/date/d" + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) }));
	}
	

	/**
	 * @see org.etudes.jforum.Command#list()
	 */
	public void list() throws Exception
	{
		this.inbox();
	}
	
	/**
	 * Apply sort to the PM list
	 * 
	 * @param pmList	PM list
	 * 
	 * @param pmSort	Sort
	 * 
	 * @param fromUser	true - sort on From user last name
	 * 					false - sort on To user last name
	 */
	protected void applySort(List pmList, PrivateMessageSort pmSort, final boolean fromUser)
	{
		if (pmSort == PrivateMessageSort.last_name_a)
		{
			Collections.sort(pmList, new Comparator<org.etudes.api.app.jforum.PrivateMessage>()
			{
				public int compare(org.etudes.api.app.jforum.PrivateMessage p1, org.etudes.api.app.jforum.PrivateMessage p2) 
				{
					if (fromUser)
					{
						return p1.getFromUser().getLastName().toUpperCase().compareTo(p2.getFromUser().getLastName().toUpperCase());
					}
					else
					{
						return p1.getToUser().getLastName().toUpperCase().compareTo(p2.getToUser().getLastName().toUpperCase());
					}
					
				}
			});
		}
		else if (pmSort == PrivateMessageSort.last_name_d)
		{
			Collections.sort(pmList, new Comparator<org.etudes.api.app.jforum.PrivateMessage>()
			{
				public int compare(org.etudes.api.app.jforum.PrivateMessage p1, org.etudes.api.app.jforum.PrivateMessage p2) 
				{
					if (fromUser)
					{
						return -1 * (p1.getFromUser().getLastName().toUpperCase().compareTo(p2.getFromUser().getLastName().toUpperCase()));
					}
					else
					{
						return -1 * (p1.getToUser().getLastName().toUpperCase().compareTo(p2.getToUser().getLastName().toUpperCase()));
					}
					
				}
			});
		}
		else if (pmSort == PrivateMessageSort.date_a)
		{
			Collections.sort(pmList, new Comparator<org.etudes.api.app.jforum.PrivateMessage>()
			{
				public int compare(org.etudes.api.app.jforum.PrivateMessage p1, org.etudes.api.app.jforum.PrivateMessage p2) 
				{
					return p1.getPost().getTime().compareTo(p2.getPost().getTime());
					
				}
			});
		}
		else if (pmSort == PrivateMessageSort.date_d)
		{
			Collections.sort(pmList, new Comparator<org.etudes.api.app.jforum.PrivateMessage>()
			{
				public int compare(org.etudes.api.app.jforum.PrivateMessage p1, org.etudes.api.app.jforum.PrivateMessage p2) 
				{
					return -1 * (p1.getPost().getTime().compareTo(p2.getPost().getTime()));
					
				}
			});
		}
	}
	
	/**
	 * Sends the private message from activity meter.
	 * 
	 * @throws Exception
	 */
	public void amSend() throws Exception
	{
		if (!SessionFacade.isLogged())
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}
		
		String returnTo = this.request.getParameter("return_to");
		String returnPath[] = returnTo.split("~");
		
		if (returnPath.length > 1)
		{
			returnTo = returnTo.replace("~", "/");
		}
				
		User user = DataAccessDriver.getInstance().newUserDAO().selectById(SessionFacade.getUserSession().getUserId());
		user.setSignature(PostCommon.processText(user.getSignature()));
		user.setSignature(PostCommon.processSmilies(user.getSignature(), SmiliesRepository.getSmilies()));

		this.sendFormCommon(user);
		
		List users = null;
		try
		{
			users = JForumUserUtil.updateMembersInfo();
			this.context.put("users", users);
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled()) logger.error(e.toString());
		}
		
		// overwrite action
		this.context.put("action", "amSendSave");
		this.context.put("returnTo", returnTo);
		this.setTemplateName(TemplateKeys.PM_AM_SENDFORM);
		
		Site currentSite = null;
		try
		{
		  currentSite = SiteService.getSite(ToolManager.getCurrentPlacement().getContext());
				  
		}
		catch (Exception e)
		{
			if (logger.isWarnEnabled()) logger.error(this.getClass().getName() + ".amSendSave() : " + e.getMessage(), e);
		}
		
		if (currentSite != null)
		{
			StringBuilder siteNavUrl = new StringBuilder();
			
			String portalUrl = ServerConfigurationService.getPortalUrl();
			siteNavUrl.append(portalUrl);
			siteNavUrl.append("/directtool/");
			
			ToolConfiguration jforumToolConfiguration = currentSite.getToolForCommonId("sakai.activitymeter");
			
			if (jforumToolConfiguration != null)
			{
				siteNavUrl.append(jforumToolConfiguration.getId());	
				if (returnTo != null)
				{
					siteNavUrl.append("/");
					siteNavUrl.append(returnTo);
				}
			}
			
			this.context.put("returnUrl", siteNavUrl);
		}
		
		//this.context.put("maxPMToUsers", new Integer(SakaiSystemGlobals.getIntValue(ConfigKeys.MAX_PM_TOUSERS)));
	}
	
	/** 
	 * Saves the private message that was invoked from activity meter and displays message with link
	 * back to activity meter
	 * 
	 * @throws Exception
	 */
	public void amSendSave() throws Exception
	{
		if (logger.isDebugEnabled()) logger.debug("PrivateMessageAction - amsendSave");
		if (!SessionFacade.isLogged())
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		String sId = this.request.getParameter("toUserId");
		String toUsername = this.request.getParameter("toUsername");
		String userEmail = this.request.getParameter("toUserEmail");		
		String returnTo = this.request.getParameter("return_to");
		
		String returnPath[] = returnTo.split("~");
		
		if (returnPath.length > 1)
		{
			returnTo = returnTo.replace("~", "/");
		}

		boolean pmEmailEnabled = false;

		PrivateMessage pm = new PrivateMessage();
		pm.setPost(PostCommon.fillPostFromRequest());

		if ((this.request.getParameter("high_priority_pm") != null) && (this.request.getIntParameter("high_priority_pm") == 1))
		{
			pm.setPriority(PrivateMessage.PRIORITY_HIGH);
		}
		else
		{
			pm.setPriority(PrivateMessage.PRIORITY_GENERAL);
		}

		User fromUser = DataAccessDriver.getInstance().newUserDAO().selectById(SessionFacade.getUserSession().getUserId());
		pm.setFromUser(fromUser);

		int attachmentIds[] = null;
		try
		{
			attachmentIds = addPMAttachments();
		}
		catch (Exception e)
		{
			this.context.put("post", pm.getPost());
			this.context.put("pm", pm);
			this.request.addParameter("return_to", returnTo);
			this.amSend();
			return;
		}

		int toUserId = -1;
		if (sId == null || sId.trim().equals(""))
		{

			String toUserIds[] = (String[]) this.request.getObjectParameter("toUsername" + "ParamValues");
			if (toUserIds != null)
			{

				for (int i = 0; i < toUserIds.length; i++)
				{
					if (toUserIds[i] != null && toUserIds[i].trim().length() > 0)
					{
						toUserId = Integer.parseInt(toUserIds[i]);
						User usr = DataAccessDriver.getInstance().newUserDAO().selectById(toUserId);
						toUserId = usr.getId();
						userEmail = usr.getEmail();
						toUsername = usr.getFirstName() + " " + usr.getLastName();
						pmEmailEnabled = usr.isNotifyPrivateMessagesEnabled();
						sendPrivateMessage(pm, toUsername, userEmail, pmEmailEnabled, toUserId, attachmentIds);
					}
				}
			}
			else
			{
				toUserId = Integer.parseInt(toUsername);
				User usr = DataAccessDriver.getInstance().newUserDAO().selectById(toUserId);
				toUserId = usr.getId();
				userEmail = usr.getEmail();
				toUsername = usr.getFirstName() + " " + usr.getLastName();
				pmEmailEnabled = usr.isNotifyPrivateMessagesEnabled();
				sendPrivateMessage(pm, toUsername, userEmail, pmEmailEnabled, toUserId, attachmentIds);
			}
		}
		else
		{
			toUserId = Integer.parseInt(sId);
			User usr = DataAccessDriver.getInstance().newUserDAO().selectById(toUserId);
			toUsername = usr.getFirstName() + " " + usr.getLastName();
			pmEmailEnabled = usr.isNotifyPrivateMessagesEnabled();
			sendPrivateMessage(pm, toUsername, userEmail, pmEmailEnabled, toUserId, attachmentIds);
		}

		this.setTemplateName(TemplateKeys.PM_AM_SENDSAVE);

		Site currentSite = null;
		try
		{
		  currentSite = SiteService.getSite(ToolManager.getCurrentPlacement().getContext());
				  
		}
		catch (Exception e)
		{
			if (logger.isWarnEnabled()) logger.error(this.getClass().getName() + ".amSendSave() : " + e.getMessage(), e);
		}
		
		if (currentSite != null)
		{
			StringBuilder siteNavUrl = new StringBuilder();
			
			String portalUrl = ServerConfigurationService.getPortalUrl();
			siteNavUrl.append(portalUrl);
			siteNavUrl.append("/directtool/");
			
			ToolConfiguration jforumToolConfiguration = currentSite.getToolForCommonId("sakai.activitymeter");
			
			if (jforumToolConfiguration != null)
			{
				siteNavUrl.append(jforumToolConfiguration.getId());
				if (returnTo != null)
				{
					siteNavUrl.append("/");
					siteNavUrl.append(returnTo);
				}
			}
			
			this.context.put("returnUrl", siteNavUrl.toString());
			
			this.context.put("message", I18n.getMessage("PrivateMessage.messageSent.returnToActivityMeter",	new String[] { siteNavUrl.toString()}));
		}
	}
	
	/**
	 * Sends the private message from activity meter.
	 * 
	 * @throws Exception
	 */
	public void amSendTo() throws Exception
	{
		if (!SessionFacade.isLogged())
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}
		
		String sakUserIdStr = this.request.getParameter("sakai_user_id");
		//String sakUserIdsArr[] = sakUserIdStr.split("~");
		
		//RosterAdvisor rosterAdvisor = (RosterAdvisor)ComponentManager.get("org.etudes.util.api.RosterAdvisor");
		RosterAdvisor rosterAdvisor = (RosterAdvisor)ComponentManager.get(RosterAdvisor.class);
		JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		
		if (rosterAdvisor != null)
		{
			List<String> sakUserIds = rosterAdvisor.getUsers(sakUserIdStr);
			
			Set<String> sakaiUserIds = new HashSet<String>();
			
			String highPriority = this.request.getParameter("high_priority");
			this.context.put("highPriority", highPriority);
			
			if (sakUserIds.isEmpty())
			{
				String sakai_user_id_save_exp = this.request.getParameter("sakai_user_id_save_exp");
				
				if (sakai_user_id_save_exp != null)
				{
					String[] sakUserIdsSaveExp = sakai_user_id_save_exp.split(",");
					
					for (String sakUserId : sakUserIdsSaveExp)
					{
						sakaiUserIds.add(sakUserId);
					}
				}
			}
			else
			{
				for (String sakUserId : sakUserIds)
				{
					sakaiUserIds.add(sakUserId);
				}
			}
			
			List<org.etudes.api.app.jforum.User> users = null;
			try
			{
				users = jforumUserService.getSiteUsers(ToolManager.getCurrentPlacement().getContext(), 0, 0);
				
				Iterator<org.etudes.api.app.jforum.User> userIter = users.iterator();
			    while (userIter.hasNext())
			    {
			    	org.etudes.api.app.jforum.User siteUser = userIter.next();
			    	if (!sakaiUserIds.contains(siteUser.getSakaiUserId()))
			    	{
			    		userIter.remove();
			    	}
			    }
				this.context.put("users", users);
			}
			catch (Exception e)
			{
				if (logger.isErrorEnabled())
				{
					logger.error(e.toString(), e);
				}
				this.context.put("users", new ArrayList());
			}
			
			/*List users = null;
			try
			{
				users = JForumUserUtil.updateMembersInfo();
				Iterator userIter = users.iterator();
			    while (userIter.hasNext())
			    {
			    	User siteUser = (User) userIter.next();
			    	if (!sakaiUserIds.contains(siteUser.getSakaiUserId()))
			    	{
			    		userIter.remove();
			    	}
			    }
			    this.context.put("users", users);
			}
			catch (Exception e)
			{
				if (logger.isErrorEnabled()) logger.error(e.toString());
			}*/
						
			
		}
		else
		{
			this.context.put("users", new ArrayList());
		}
		
		/*User user = DataAccessDriver.getInstance().newUserDAO().selectById(SessionFacade.getUserSession().getUserId());
		user.setSignature(PostCommon.processText(user.getSignature()));
		user.setSignature(PostCommon.processSmilies(user.getSignature(), SmiliesRepository.getSmilies()));

		this.sendFormCommon(user);*/
		org.etudes.api.app.jforum.User user = jforumUserService.getBySakaiUserId(UserDirectoryService.getCurrentUser().getId());
		user.setSignature(PostCommon.processText(user.getSignature()));
		user.setSignature(PostCommon.processSmilies(user.getSignature(), SmiliesRepository.getSmilies()));
		
		this.sendFormCommon(user);
			
		// overwrite action
		this.context.put("action", "sendAMPMSave");
		this.context.put("sakaiToUserIds", sakUserIdStr);
		this.setTemplateName(TemplateKeys.PM_AM_SENDFORM);
		this.context.put("pageTitle", I18n.getMessage("PrivateMessage.pageTitle"));				
		//this.context.put("maxPMToUsers", new Integer(SakaiSystemGlobals.getIntValue(ConfigKeys.MAX_PM_TOUSERS)));
	}
	
	/** 
	 * Saves the private message that was invoked from activity meter and displays message with link
	 * back to activity meter
	 * 
	 * @throws Exception
	 */
	public void sendAMPMSave() throws Exception
	{
		if (logger.isDebugEnabled()) logger.debug("PrivateMessageAction - amsendSave");
		if (!SessionFacade.isLogged())
		{
			this.setTemplateName(ViewCommon.contextToLogin());
			return;
		}

		String sakUserIdStr = this.request.getParameter("sakai_user_id");
		this.context.put("sakaiToUserIds", sakUserIdStr);
		
		String highPriority = this.request.getParameter("high_priority");
		this.context.put("highPriority", highPriority);
		
		JForumPrivateMessageService jforumPrivateMessageService = (JForumPrivateMessageService)ComponentManager.get("org.etudes.api.app.jforum.JForumPrivateMessageService");
		
		String toUsername = this.request.getParameter("toUsername");
		
		String toUserIds[] = (String[]) this.request.getObjectParameter("toUsername" + "ParamValues");
		
		int[] toJFUserIds = null;
		
		if (toUserIds != null)
		{
			toJFUserIds = new int[toUserIds.length];
			
			for (int i = 0; i < toUserIds.length; i++)
			{
				if (toUserIds[i] != null && toUserIds[i].trim().length() > 0)
				{
					toJFUserIds[i] = Integer.parseInt(toUserIds[i]);
				}
			}
		}
		else
		{
			toJFUserIds = new int[1];
			
			toJFUserIds[0] = Integer.parseInt(toUsername);
		}
		
		JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		org.etudes.api.app.jforum.User fromUser = jforumUserService.getBySakaiUserId(UserDirectoryService.getCurrentUser().getId());
		
		org.etudes.api.app.jforum.User[] toUsers = null;
		try
		{
			toUsers =  new org.etudes.api.app.jforum.User[toJFUserIds.length];
			
			for (int i = 0; i < toJFUserIds.length; i++)
			{
				toUsers[i] = jforumUserService.getByUserId(toJFUserIds[i]);
			}
			
		}
		catch (Exception e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn(e.toString(), e);
			}
		}
		String fromSakaiUserId = fromUser.getSakaiUserId(); 
		
		org.etudes.api.app.jforum.PrivateMessage privateMessage = null;
		
		List<String> toSakaiUserIds =  new ArrayList<String>();
		if (toUsers.length == 1)
		{
			privateMessage = jforumPrivateMessageService.newPrivateMessage(fromSakaiUserId, toUsers[0].getSakaiUserId());
			toSakaiUserIds.add(toUsers[0].getSakaiUserId());
		}
		else
		{
			privateMessage = jforumPrivateMessageService.newPrivateMessage(fromSakaiUserId);
			
			for (int i = 0; i < toUsers.length; i++)
			{
				toSakaiUserIds.add(toUsers[i].getSakaiUserId());
			}
		}
		
		privateMessage.setContext(ToolManager.getCurrentPlacement().getContext());
		
		if ((this.request.getParameter("high_priority_pm") != null) && (this.request.getIntParameter("high_priority_pm") == 1))
		{
			privateMessage.setPriority(org.etudes.api.app.jforum.PrivateMessage.PRIORITY_HIGH);
		}
		else
		{
			privateMessage.setPriority(org.etudes.api.app.jforum.PrivateMessage.PRIORITY_GENERAL);
		}
		
		org.etudes.api.app.jforum.Post post = privateMessage.getPost();
		
		post.setTime(new Date());
		
		post.setSubject(JForum.getRequest().getParameter("subject"));
				
		post.setBbCodeEnabled(this.request.getParameter("disable_bbcode") != null ? false : true);
		post.setSmiliesEnabled(this.request.getParameter("disable_smilies") != null ? false : true);
		post.setSignatureEnabled(this.request.getParameter("attach_sig") != null ? true : false);
		
		String message = this.request.getParameter("message");
		post.setText(message);
		
		/*String modMessage = cleanMessage(message);
		post.setText(modMessage);		
		
		post.setHtmlEnabled(this.request.getParameter("disable_html") != null);
		
		if (post.isHtmlEnabled())
		{
			post.setText(SafeHtml.makeSafe(message));
		}
		else
		{
			post.setText(message);
		}*/
				
		//process post attachments
		processPostAttachments(jforumPrivateMessageService, privateMessage);
		
		if (toSakaiUserIds.size() == 1)
		{
			//jforumPrivateMessageService.sendPrivateMessage(privateMessage);
			try
			{
				jforumPrivateMessageService.sendPrivateMessageWithAttachments(privateMessage);
			}
			catch (JForumAccessException e)
			{
				JForum.enableCancelCommit();
				StringBuffer toSakUserIdSb = new StringBuffer();
				for (String sakUserId : toSakaiUserIds)
				{
					toSakUserIdSb.append(sakUserId);
					toSakUserIdSb.append(",");
				}
				
				String toSakUserIdStr = toSakUserIdSb.toString();
				if (toSakUserIdStr.endsWith(","))
				{
					toSakUserIdStr = toSakUserIdStr.substring(0, toSakUserIdStr.length() - 1);
				}
				this.request.addParameter("sakai_user_id_save_exp", toSakUserIdStr.toString());
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				this.amSendTo();
				return;
			}
			catch (JForumAttachmentOverQuotaException e)
			{
				JForum.enableCancelCommit();
				
				StringBuffer toSakUserIdSb = new StringBuffer();
				for (String sakUserId : toSakaiUserIds)
				{
					toSakUserIdSb.append(sakUserId);
					toSakUserIdSb.append(",");
				}
				
				String toSakUserIdStr = toSakUserIdSb.toString();
				if (toSakUserIdStr.endsWith(","))
				{
					toSakUserIdStr = toSakUserIdStr.substring(0, toSakUserIdStr.length() - 1);
				}
				this.request.addParameter("sakai_user_id_save_exp", toSakUserIdStr.toString());
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				this.amSendTo();
				return;
			}
			catch (JForumAttachmentBadExtensionException e)
			{
				JForum.enableCancelCommit();
				StringBuffer toSakUserIdSb = new StringBuffer();
				for (String sakUserId : toSakaiUserIds)
				{
					toSakUserIdSb.append(sakUserId);
					toSakUserIdSb.append(",");
				}
				
				String toSakUserIdStr = toSakUserIdSb.toString();
				if (toSakUserIdStr.endsWith(","))
				{
					toSakUserIdStr = toSakUserIdStr.substring(0, toSakUserIdStr.length() - 1);
				}
				this.request.addParameter("sakai_user_id_save_exp", toSakUserIdStr.toString());
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				this.amSendTo();
				return;
			}
		}
		else
		{
			try
			{
				jforumPrivateMessageService.sendPrivateMessageWithAttachments(privateMessage, toSakaiUserIds);
			}
			catch (JForumAccessException e)
			{
				JForum.enableCancelCommit();
				StringBuffer toSakUserIdSb = new StringBuffer();
				for (String sakUserId : toSakaiUserIds)
				{
					toSakUserIdSb.append(sakUserId);
					toSakUserIdSb.append(",");
				}
				
				String toSakUserIdStr = toSakUserIdSb.toString();
				if (toSakUserIdStr.endsWith(","))
				{
					toSakUserIdStr = toSakUserIdStr.substring(0, toSakUserIdStr.length() - 1);
				}
				this.request.addParameter("sakai_user_id_save_exp", toSakUserIdStr.toString());
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				this.amSendTo();
				return;
			}
			catch (JForumAttachmentOverQuotaException e)
			{
				JForum.enableCancelCommit();
				StringBuffer toSakUserIdSb = new StringBuffer();
				for (String sakUserId : toSakaiUserIds)
				{
					toSakUserIdSb.append(sakUserId);
					toSakUserIdSb.append(",");
				}
				
				String toSakUserIdStr = toSakUserIdSb.toString();
				if (toSakUserIdStr.endsWith(","))
				{
					toSakUserIdStr = toSakUserIdStr.substring(0, toSakUserIdStr.length() - 1);
				}
				this.request.addParameter("sakai_user_id_save_exp", toSakUserIdStr);
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				this.amSendTo();
				return;
			}
			catch (JForumAttachmentBadExtensionException e)
			{
				JForum.enableCancelCommit();
				StringBuffer toSakUserIdSb = new StringBuffer();
				for (String sakUserId : toSakaiUserIds)
				{
					toSakUserIdSb.append(sakUserId);
					toSakUserIdSb.append(",");
				}
				
				String toSakUserIdStr = toSakUserIdSb.toString();
				if (toSakUserIdStr.endsWith(","))
				{
					toSakUserIdStr = toSakUserIdStr.substring(0, toSakUserIdStr.length() - 1);
				}
				this.request.addParameter("sakai_user_id_save_exp", toSakUserIdStr.toString());
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				this.amSendTo();
				return;
			}
		}
		
		/*boolean pmEmailEnabled = false;

		PrivateMessage pm = new PrivateMessage();
		pm.setPost(PostCommon.fillPostFromRequest());

		if ((this.request.getParameter("high_priority_pm") != null) && (this.request.getIntParameter("high_priority_pm") == 1))
		{
			pm.setPriority(PrivateMessage.PRIORITY_HIGH);
		}
		else
		{
			pm.setPriority(PrivateMessage.PRIORITY_GENERAL);
		}

		User fromUser = DataAccessDriver.getInstance().newUserDAO().selectById(SessionFacade.getUserSession().getUserId());
		pm.setFromUser(fromUser);

		int attachmentIds[] = null;
		try
		{
			attachmentIds = addPMAttachments();
		}
		catch (Exception e)
		{
			this.context.put("post", pm.getPost());
			this.context.put("pm", pm);
			this.request.addParameter("sakai_user_id", sakUserIdStr);
			this.request.addParameter("highPriority", highPriority);
			this.amSendTo();
			return;
		}

		String toUsername = this.request.getParameter("toUsername");
		
		String userEmail = null;
		int toUserId = -1;
		String toUserIds[] = (String[]) this.request.getObjectParameter("toUsername" + "ParamValues");
		if (toUserIds != null)
		{

			for (int i = 0; i < toUserIds.length; i++)
			{
				if (toUserIds[i] != null && toUserIds[i].trim().length() > 0)
				{
					toUserId = Integer.parseInt(toUserIds[i]);
					User usr = DataAccessDriver.getInstance().newUserDAO().selectById(toUserId);
					toUserId = usr.getId();
					userEmail = usr.getEmail();
					toUsername = usr.getFirstName() + " " + usr.getLastName();
					pmEmailEnabled = usr.isNotifyPrivateMessagesEnabled();
					sendPrivateMessage(pm, toUsername, userEmail, pmEmailEnabled, toUserId, attachmentIds);
				}
			}
		}
		else
		{
			toUserId = Integer.parseInt(toUsername);
			User usr = DataAccessDriver.getInstance().newUserDAO().selectById(toUserId);
			toUserId = usr.getId();
			userEmail = usr.getEmail();
			toUsername = usr.getFirstName() + " " + usr.getLastName();
			pmEmailEnabled = usr.isNotifyPrivateMessagesEnabled();
			sendPrivateMessage(pm, toUsername, userEmail, pmEmailEnabled, toUserId, attachmentIds);
		}*/
		
		this.setTemplateName(TemplateKeys.PM_AM_SENDSAVE);
		this.context.put("saveSuccess", Boolean.TRUE);
		this.context.put("pageTitle", I18n.getMessage("PrivateMessage.pageTitle"));
	}
	
	/**
	 * Sends private message
	 *
	 */
	protected void sendPrivateMessage() throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		String sId = this.request.getParameter("toUserId");
		String toUsername = this.request.getParameter("toUsername");
		
		
		if (sId == null || sId.trim().equals(""))
		{

			String toUserIds[] = (String[]) this.request.getObjectParameter("toUsername" + "ParamValues");
			if (toUserIds != null)
			{
				
				sendPrivateMessageWithAttachments();
			}
			else
			{
				int toUserId = Integer.parseInt(toUsername);
				if (toUserId != -1)
				{
					sendPrivateMessageWithAttachments();
				}
			}
		}
		else
		{
			//quote
			int toUserId = Integer.parseInt(sId);
			if (toUserId != -1)
			{
				replyToPrivateMessage();
			}
		}
		
		
		this.setTemplateName(TemplateKeys.PM_SENDSAVE);

		this.context.put("message", I18n.getMessage("PrivateMessage.messageSent", new String[] { this.request.getContextPath()
				+ "/pm/inbox/date/d" + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) }));
		
		
	}
	
	/**
	 * Send private message with attachments
	 * 
	 * @throws JForumAccessException
	 * @throws JForumAttachmentOverQuotaException
	 * @throws JForumAttachmentBadExtensionException
	 */
	protected void sendPrivateMessageWithAttachments() throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		String sId = this.request.getParameter("toUserId");
		String toUsername = this.request.getParameter("toUsername");
		
		int[] toJFUserIds = null;
		
		if (sId == null || sId.trim().equals(""))
		{
			String toUserIds[] = (String[]) this.request.getObjectParameter("toUsername" + "ParamValues");
			if (toUserIds != null)
			{
				toJFUserIds = new int[toUserIds.length];
				
				for (int i = 0; i < toUserIds.length; i++)
				{
					if (toUserIds[i] != null && toUserIds[i].trim().length() > 0)
					{
						toJFUserIds[i] = Integer.parseInt(toUserIds[i]);
					}
				}
			}
			else
			{
				toJFUserIds = new int[1];
				
				toJFUserIds[0] = Integer.parseInt(toUsername);
				/*try
				{
					User usr = DataAccessDriver.getInstance().newUserDAO().selectById(toJFUserIds[0]);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
				
				//org.etudes.api.app.jforum.User user = jforumUserService.getBySakaiUserId(UserDirectoryService.getCurrentUser().getId());
				
			}
		}
		JForumPrivateMessageService jforumPrivateMessageService = (JForumPrivateMessageService)ComponentManager.get("org.etudes.api.app.jforum.JForumPrivateMessageService");
		
		if (jforumPrivateMessageService == null) return;
		
		//User fromUser = null;
		JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		org.etudes.api.app.jforum.User fromUser = jforumUserService.getBySakaiUserId(UserDirectoryService.getCurrentUser().getId());
		//User[] toUsers = null;
		org.etudes.api.app.jforum.User[] toUsers = null;
		try
		{
			toUsers =  new org.etudes.api.app.jforum.User[toJFUserIds.length];
			
			//fromUser = DataAccessDriver.getInstance().newUserDAO().selectById(SessionFacade.getUserSession().getUserId());
			
			for (int i = 0; i < toJFUserIds.length; i++)
			{
				//toUsers[i] = DataAccessDriver.getInstance().newUserDAO().selectById(toJFUserIds[i]);
				toUsers[i] = jforumUserService.getByUserId(toJFUserIds[i]);
			}
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fromSakaiUserId = fromUser.getSakaiUserId(); 
		
		org.etudes.api.app.jforum.PrivateMessage privateMessage = null;
		
		List<String> toSakaiUserIds =  new ArrayList<String>();
		if (toUsers.length == 1)
		{
			privateMessage = jforumPrivateMessageService.newPrivateMessage(fromSakaiUserId, toUsers[0].getSakaiUserId());
			toSakaiUserIds.add(toUsers[0].getSakaiUserId());
		}
		else
		{
			privateMessage = jforumPrivateMessageService.newPrivateMessage(fromSakaiUserId);
			
			for (int i = 0; i < toUsers.length; i++)
			{
				toSakaiUserIds.add(toUsers[i].getSakaiUserId());
			}
		}
		
		privateMessage.setContext(ToolManager.getCurrentPlacement().getContext());
		
		if ((this.request.getParameter("high_priority_pm") != null) && (this.request.getIntParameter("high_priority_pm") == 1))
		{
			privateMessage.setPriority(org.etudes.api.app.jforum.PrivateMessage.PRIORITY_HIGH);
		}
		else
		{
			privateMessage.setPriority(org.etudes.api.app.jforum.PrivateMessage.PRIORITY_GENERAL);
		}
		
		org.etudes.api.app.jforum.Post post = privateMessage.getPost();
		
		post.setTime(new Date());
		
		post.setSubject(JForum.getRequest().getParameter("subject"));
				
		post.setBbCodeEnabled(this.request.getParameter("disable_bbcode") != null ? false : true);
		post.setSmiliesEnabled(this.request.getParameter("disable_smilies") != null ? false : true);
		post.setSignatureEnabled(this.request.getParameter("attach_sig") != null ? true : false);
		
		String message = this.request.getParameter("message");
		post.setText(message);
		/*String modMessage = cleanMessage(message);
		post.setText(modMessage);		
		
		post.setHtmlEnabled(this.request.getParameter("disable_html") != null);
		
		if (post.isHtmlEnabled())
		{
			post.setText(SafeHtml.makeSafe(message));
		}
		else
		{
			post.setText(message);
		}*/
				
		//process post attachments
		processPostAttachments(jforumPrivateMessageService, privateMessage);
		
		if (toSakaiUserIds.size() == 1)
		{
			//jforumPrivateMessageService.sendPrivateMessage(privateMessage);
			try
			{
				jforumPrivateMessageService.sendPrivateMessageWithAttachments(privateMessage);
			}
			catch (JForumAccessException e)
			{
				JForum.enableCancelCommit();
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				//this.send();
				throw e;
			}
			catch (JForumAttachmentOverQuotaException e)
			{
				JForum.enableCancelCommit();
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				//this.send();
				throw e;
			}
			catch (JForumAttachmentBadExtensionException e)
			{
				JForum.enableCancelCommit();
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				//this.send();
				throw e;
			}
		}
		else
		{
			try
			{
				jforumPrivateMessageService.sendPrivateMessageWithAttachments(privateMessage, toSakaiUserIds);
			}
			catch (JForumAccessException e)
			{
				JForum.enableCancelCommit();
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				//this.send();
				throw e;
			}
			catch (JForumAttachmentOverQuotaException e)
			{
				JForum.enableCancelCommit();
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				//this.send();
				throw e;
			}
			catch (JForumAttachmentBadExtensionException e)
			{
				JForum.enableCancelCommit();
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				//this.send();
				throw e;
			}
		}		
	}
	
	/**
	 * Reply to private message
	 * 
	 * @throws JForumAccessException
	 * @throws JForumAttachmentOverQuotaException
	 * @throws JForumAttachmentBadExtensionException
	 */
	protected void replyToPrivateMessage() throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		//String sId = this.request.getParameter("toUserId");
		//String toUsername = this.request.getParameter("toUsername");
		
		//int toUserId = Integer.parseInt(sId);
		
		JForumPrivateMessageService jforumPrivateMessageService = null;
		jforumPrivateMessageService = (JForumPrivateMessageService)ComponentManager.get("org.etudes.api.app.jforum.JForumPrivateMessageService");
		
		if (jforumPrivateMessageService == null) return;
		
		String exisPmId = this.request.getParameter("exisPmId");
		if (exisPmId != null && exisPmId.trim().length() > 0)
		{
			org.etudes.api.app.jforum.PrivateMessage privateMessage = null;
			
			privateMessage = jforumPrivateMessageService.newPrivateMessage(Integer.parseInt(exisPmId));
			
			//privateMessage.setContext(ToolManager.getCurrentPlacement().getContext());
			JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
			org.etudes.api.app.jforum.User currentUser = jforumUserService.getBySakaiUserId(UserDirectoryService.getCurrentUser().getId());
			if ((privateMessage == null || currentUser == null) || (privateMessage.getFromUser().getId() != currentUser.getId()))
			{
				this.context.put("errorMessage", "Error while replying to existing message.");
			}
			
			if ((this.request.getParameter("high_priority_pm") != null) && (this.request.getIntParameter("high_priority_pm") == 1))
			{
				privateMessage.setPriority(org.etudes.api.app.jforum.PrivateMessage.PRIORITY_HIGH);
			}
			else
			{
				privateMessage.setPriority(org.etudes.api.app.jforum.PrivateMessage.PRIORITY_GENERAL);
			}
			
			org.etudes.api.app.jforum.Post post = privateMessage.getPost();
			
			post.setTime(new Date());
			
			post.setSubject(JForum.getRequest().getParameter("subject"));
			
			post.setBbCodeEnabled(this.request.getParameter("disable_bbcode") != null ? false : true);
			post.setSmiliesEnabled(this.request.getParameter("disable_smilies") != null ? false : true);
			post.setSignatureEnabled(this.request.getParameter("attach_sig") != null ? true : false);
			
			String message = this.request.getParameter("message");
			post.setText(message);
			
			/*String modMessage = cleanMessage(message);
			post.setText(modMessage);		
			
			post.setHtmlEnabled(this.request.getParameter("disable_html") != null);
			
			if (post.isHtmlEnabled())
			{
				post.setText(SafeHtml.makeSafe(message));
			}
			else
			{
				post.setText(message);
			}*/			
			
			//process post attachments
			processPostAttachments(jforumPrivateMessageService, privateMessage);
			
			try
			{
				jforumPrivateMessageService.replyPrivateMessage(privateMessage);
			}
			catch (JForumAccessException e)
			{
				JForum.enableCancelCommit();
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				//this.send();
				throw e;
			}
			catch (JForumAttachmentOverQuotaException e)
			{
				JForum.enableCancelCommit();
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				//this.send();
				throw e;
			}
			catch (JForumAttachmentBadExtensionException e)
			{
				JForum.enableCancelCommit();
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", privateMessage.getPost());
				this.context.put("pm", privateMessage);
				//this.send();
				throw e;
			}
		}
		
	}
	
	/**
	 * process post attachments
	 * 
	 * @param jforumPrivateMessageService	jforumPrivateMessageService
	 * 
	 * @param privateMessage	Private message with attachments
	 */
	protected void processPostAttachments(JForumPrivateMessageService jforumPrivateMessageService, org.etudes.api.app.jforum.PrivateMessage privateMessage)
	{
		org.etudes.api.app.jforum.Post post = privateMessage.getPost();
		
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
			
			org.etudes.api.app.jforum.Attachment  attachment = jforumPrivateMessageService.newAttachment(fileName, contentType, comments, fileContent);
			if (attachment != null)
			{
				post.getAttachments().add(attachment);
			}
		}

	}
	
	/** 
	 * Removes html comments and extra spaces, escapes javascript tags, adds anchor tags
	 * 
	 * @param message	Message
	 * 
	 * @return	Cleaned message with removed html comments and extra spaces, escapes javascript tags, adds anchor tags
	 */
	/*protected String cleanMessage(String message)
	{
		if (message == null)
		{
			return null;
		}
		
		String formatMessage = message;
		
		formatMessage = SafeHtml.escapeJavascript(formatMessage);
		
		// strip html comments
		//formatMessage = SafeHtml.stripHTMLComments(formatMessage);
		
		// strip excess spaces
		//formatMessage = SafeHtml.removeExcessSpaces(formatMessage);
		
		// add target to anchor tag
		//formatMessage = SafeHtml.addAnchorTarget(formatMessage);
		
		return formatMessage;
	}*/
}
