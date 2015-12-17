/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/view/forum/common/UserCommon.java $ 
 * $Id: UserCommon.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.jforum.view.forum.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.JForumUserService;
import org.etudes.jforum.JForum;
import org.etudes.jforum.SessionFacade;
import org.etudes.jforum.entities.User;
import org.etudes.jforum.util.MD5;
import org.etudes.jforum.util.image.ImageUtils;
import org.etudes.jforum.util.legacy.commons.fileupload.FileItem;
import org.etudes.jforum.util.legacy.commons.fileupload.disk.DiskFileItem;
import org.etudes.jforum.util.preferences.ConfigKeys;
import org.etudes.jforum.util.preferences.SakaiSystemGlobals;
import org.etudes.jforum.util.preferences.SystemGlobals;
import org.etudes.util.HtmlHelper;
import org.sakaiproject.component.cover.ComponentManager;

/**
 * @author Rafael Steil
 * 02/22/2006 - Murthy - updated to support avatars in clustered environment
 */
public class UserCommon 
{
	private static final Log logger = LogFactory.getLog(UserCommon.class);

	/**
	 * Updates the user information
	 * 
	 * @param userId The user id we are saving
	 * @throws Exception
	 */
	public static List<String> saveUser(int userId) throws Exception
	{
		JForumUserService jforumUserService = (JForumUserService)ComponentManager.get("org.etudes.api.app.jforum.JForumUserService");
		
		org.etudes.api.app.jforum.User user = jforumUserService.getByUserId(userId);
		
		if (user ==  null)
		{
			return new ArrayList<String>();
		}
		
		/*String username = JForum.getRequest().getParameter("username");
		if (username != null)
		{
			user.setUsername(username);
		}*/

		//user.setId(userId);
		String email = JForum.getRequest().getParameter("email");
		if (email != null)
		{
			user.setEmail(email);
		}
		//user.setIcq(SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("icq"))));
		user.setAim(JForum.getRequest().getParameter("aim"));
		user.setMsnm(JForum.getRequest().getParameter("msn"));
		user.setYim(JForum.getRequest().getParameter("yim"));
		user.setFaceBookAccount(JForum.getRequest().getParameter("fbook"));
		user.setTwitterAccount(JForum.getRequest().getParameter("tweet"));		
		user.setGooglePlus(JForum.getRequest().getParameter("googleplus"));
		user.setSkype(JForum.getRequest().getParameter("skype"));
		user.setLinkedIn(JForum.getRequest().getParameter("linkedIn"));		
		user.setFrom(JForum.getRequest().getParameter("location"));
		user.setOccupation(JForum.getRequest().getParameter("occupation"));
		user.setInterests(JForum.getRequest().getParameter("interests"));
		//user.setSignature(SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("signature"))));
		user.setSignature(JForum.getRequest().getParameter("signature"));
		user.setViewEmailEnabled(JForum.getRequest().getParameter("viewemail").equals("1"));
		//user.setViewOnlineEnabled(JForum.getRequest().getParameter("hideonline").equals("0"));
		user.setNotifyPrivateMessagesEnabled(JForum.getRequest().getParameter("notifypm").equals("1"));
		user.setNotifyOnMessagesEnabled(JForum.getRequest().getParameter("notifyreply").equals("1"));
		user.setAttachSignatureEnabled(JForum.getRequest().getParameter("attachsig").equals("1"));
		//12/12/2012 user.setHtmlEnabled(JForum.getRequest().getParameter("allowhtml").equals("1"));
		user.setLang(JForum.getRequest().getParameter("language"));

		String website = JForum.getRequest().getParameter("website");
		if (website != null && !"".equals(website.trim()) && !website.toLowerCase().startsWith("http://"))
		{
			website = "http://" + website;
		}

		user.setWebSite(website);

		/*if (JForum.getRequest().getParameter("new_password") != null && JForum.getRequest().getParameter("new_password").length() > 0)
		{
			user.setPassword(MD5.crypt(JForum.getRequest().getParameter("new_password")));
		}*/

		if (JForum.getRequest().getParameter("avatardel") != null)
		{
			/*File f = null;
			if (SakaiSystemGlobals.getBoolValue(ConfigKeys.AVATAR_CLUSTERED))
			{
				f = new File(SakaiSystemGlobals.getValue(ConfigKeys.AVATAR_PATH) + "/images/avatar/" + user.getAvatar());
			}
			else
			{
				f = new File(SystemGlobals.getApplicationPath() + "/images/avatar/" + user.getAvatar());
			}

			f.delete();*/

			user.setAvatar(null);
		}
		
		// avatar
		List<String> warns = new ArrayList<String>();

		attachAvatar(user);
		
		jforumUserService.modifyUser(user);
				
		/*
		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
		User u = um.selectById(userId);

		String username = JForum.getRequest().getParameter("username");
		if (username != null)
		{
			u.setUsername(username);
		}

		u.setId(userId);
		String email = SafeHtml.makeSafe(JForum.getRequest().getParameter("email"));
		if (email != null)
		{
			u.setEmail(email);
		}
		u.setIcq(SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("icq"))));
		u.setAim(SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("aim"))));
		u.setMsnm(SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("msn"))));
		u.setYim(SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("yim"))));
		u.setFaceBookAccount(SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("fbook"))));
		u.setTwitterAccount(SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("tweet"))));
		u.setFrom(SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("location"))));
		u.setOccupation(SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("occupation"))));
		u.setInterests(SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("interests"))));
		u.setSignature(SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("signature"))));
		u.setViewEmailEnabled(JForum.getRequest().getParameter("viewemail").equals("1"));
		u.setViewOnlineEnabled(JForum.getRequest().getParameter("hideonline").equals("0"));
		u.setNotifyPrivateMessagesEnabled(JForum.getRequest().getParameter("notifypm").equals("1"));
		u.setNotifyOnMessagesEnabled(JForum.getRequest().getParameter("notifyreply").equals("1"));
		u.setAttachSignatureEnabled(JForum.getRequest().getParameter("attachsig").equals("1"));
		u.setHtmlEnabled(JForum.getRequest().getParameter("allowhtml").equals("1"));
		u.setLang(JForum.getRequest().getParameter("language"));

		String website = SafeHtml.makeSafe(SafeHtml.escapeJavascript(JForum.getRequest().getParameter("website")));
		if (website != null && !"".equals(website.trim()) && !website.toLowerCase().startsWith("http://"))
		{
			website = "http://" + website;
		}

		u.setWebSite(website);

		if (JForum.getRequest().getParameter("new_password") != null && JForum.getRequest().getParameter("new_password").length() > 0)
		{
			u.setPassword(MD5.crypt(JForum.getRequest().getParameter("new_password")));
		}

		if (JForum.getRequest().getParameter("avatardel") != null)
		{
			// 02/22/2006 - Murthy - updated to support avatars in clustered environment
			File f = null;
			// if (SystemGlobals.getBoolValue(ConfigKeys.AVATAR_CLUSTERED))
			if (SakaiSystemGlobals.getBoolValue(ConfigKeys.AVATAR_CLUSTERED))
				// f = new File(SystemGlobals.getValue(ConfigKeys.AVATAR_PATH) + "/images/avatar/"+ u.getAvatar());
				f = new File(SakaiSystemGlobals.getValue(ConfigKeys.AVATAR_PATH) + "/images/avatar/" + u.getAvatar());
			else
				f = new File(SystemGlobals.getApplicationPath() + "/images/avatar/" + u.getAvatar());

			f.delete();

			u.setAvatar(null);
		}

		// avatar
		List<String> warns = new ArrayList<String>();
		if (JForum.getRequest().getObjectParameter("avatar") != null)
		{
			try
			{
				UserCommon.handleAvatar(u);
			}
			catch (Exception e)
			{
				UserCommon.logger.warn("Problems while uploading the avatar: " + e);
				warns.add(I18n.getMessage("User.avatarUploadError"));
			}
		}
		else
		{
			String avatarUrl = JForum.getRequest().getParameter("avatarUrl");
			if (avatarUrl != null && !"".equals(avatarUrl.trim()))
			{
				if (avatarUrl.toLowerCase().startsWith("http://"))
				{
					u.setAvatar(SafeHtml.escapeJavascript(avatarUrl));
				}
				else
				{
					warns.add(I18n.getMessage("User.avatarUrlShouldHaveHttp"));
				}
			}
		}

		um.update(u);

		SessionFacade.getUserSession().setLang(u.getLang());
		
		*/
		SessionFacade.getUserSession().setLang(user.getLang());

		return warns;
	}
	
	
	protected static void attachAvatar(org.etudes.api.app.jforum.User user)
	{
		DiskFileItem item = (DiskFileItem) JForum.getRequest().getObjectParameter("avatar");
		
		if (item == null)
		{
			return;
		}
		
		if (item.getName().indexOf('\000') > -1)
		{
			logger.warn("Possible bad avatar upload (null char): " + item.getName() + " - user_id: " + SessionFacade.getUserSession().getUserId());
			return;
		}
		
		String fileName = null;
		byte[] fileContent = null;
		
		fileName = item.getName();
		fileContent = item.get();
		
		user.attachAvatar(fileName, fileContent);
	}

	/**
	 * @param u
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void handleAvatar(User u) throws Exception 
	{
		String fileName = MD5.crypt(Integer.toString(u.getId()));
		FileItem item = (FileItem) JForum.getRequest().getObjectParameter("avatar");
		UploadUtils uploadUtils = new UploadUtils(item);

		// Gets file extension
		String extension = uploadUtils.getExtension().toLowerCase();
		int type = -1;

		if (extension.equals("jpg") || extension.equals("jpeg"))
		{
			type = ImageUtils.IMAGE_JPEG;
		}
		else if (extension.equals("gif") || extension.equals("png"))
		{
			type = ImageUtils.IMAGE_PNG;
		}

		if (type != -1)
		{
			// <<<02/22/2006 - Murthy - updated to support avatars in clustered
			// environment
			String avatarTmpFileName = null;
			// if (SystemGlobals.getBoolValue(ConfigKeys.AVATAR_CLUSTERED)){
			if (SakaiSystemGlobals.getBoolValue(ConfigKeys.AVATAR_CLUSTERED))
			{
				// String avatarDirPath = SystemGlobals.getValue(ConfigKeys.AVATAR_PATH) + "/images/avatar/";
				String avatarDirPath = SakaiSystemGlobals.getValue(ConfigKeys.AVATAR_PATH) + "/images/avatar/";
				File avatarDir = new File(avatarDirPath);
				if (!avatarDir.exists())
				{
					avatarDir.mkdirs();
				}

				// avatarTmpFileName =
				// SystemGlobals.getValue(ConfigKeys.AVATAR_PATH)
				avatarTmpFileName = SakaiSystemGlobals.getValue(ConfigKeys.AVATAR_PATH) + "/images/avatar/" + fileName + "_tmp." + extension;
			}
			else
				avatarTmpFileName = SystemGlobals.getApplicationPath() + "/images/avatar/" + fileName + "_tmp." + extension;
			// >>>Murthy

			// We cannot handle gifs
			if (extension.toLowerCase().equals("gif"))
			{
				extension = "png";
			}

			String avatarFinalFileName = null;
			// if (SystemGlobals.getBoolValue(ConfigKeys.AVATAR_CLUSTERED))
			if (SakaiSystemGlobals.getBoolValue(ConfigKeys.AVATAR_CLUSTERED))
				// avatarFinalFileName = SystemGlobals.getValue(ConfigKeys.AVATAR_PATH) +
				avatarFinalFileName = SakaiSystemGlobals.getValue(ConfigKeys.AVATAR_PATH) + "/images/avatar/" + fileName + "." + extension;
			else
				avatarFinalFileName = SystemGlobals.getApplicationPath() + "/images/avatar/" + fileName + "." + extension;

			uploadUtils.saveUploadedFile(avatarTmpFileName);

			// OK, time to check and process the avatar size
			// int maxWidth = SystemGlobals.getIntValue(ConfigKeys.AVATAR_MAX_WIDTH);
			int maxWidth = SakaiSystemGlobals.getIntValue(ConfigKeys.AVATAR_MAX_WIDTH);
			// int maxHeight = SystemGlobals.getIntValue(ConfigKeys.AVATAR_MAX_HEIGHT);
			int maxHeight = SakaiSystemGlobals.getIntValue(ConfigKeys.AVATAR_MAX_HEIGHT);

			BufferedImage image = ImageUtils.resizeImage(avatarTmpFileName, type, maxWidth, maxHeight);
			ImageUtils.saveImage(image, avatarFinalFileName, type);

			u.setAvatar(fileName + "." + extension);

			// Delete the temporary file
			new File(avatarTmpFileName).delete();
		}
	}

}
