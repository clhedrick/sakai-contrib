/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumUserServiceImpl.java $ 
 * $Id: JForumUserServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Attachment;
import org.etudes.api.app.jforum.JForumAttachmentService;
import org.etudes.api.app.jforum.JForumSecurityService;
import org.etudes.api.app.jforum.JForumUserService;
import org.etudes.api.app.jforum.User;
import org.etudes.api.app.jforum.dao.UserAlreadyExistException;
import org.etudes.api.app.jforum.dao.UserAlreadyInSiteUsersException;
import org.etudes.api.app.jforum.dao.UserDao;
import org.etudes.component.app.jforum.util.image.ImageUtils;
import org.etudes.util.HtmlHelper;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.thread_local.api.ThreadLocalManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;

public class JForumUserServiceImpl implements JForumUserService
{
	private static Log logger = LogFactory.getLog(JForumUserServiceImpl.class);
	
	protected static final int MAX_USERS_CREATE = 100;
	
	protected static Object obj = new Object();
	
	/** Dependency: JForumSecurityService. */
	protected JForumSecurityService jforumSecurityService = null;
	
	/** Dependency: SiteService. */
	protected SiteService siteService = null;
	
	/** Dependency: ThreadLocalManager. */
	protected ThreadLocalManager threadLocalManager = null;
	
	/** Dependency: TopicDao */
	protected UserDao userDao = null;
	
	/** Dependency: UserDirectoryService. */
	protected UserDirectoryService userDirectoryService = null;
	
	/**
	 * {@inheritDoc}
	 */
	public void addModifyMarkAllTime(String siteId, String sakaiUserId)
	{
		if (siteId == null || siteId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Site information is missing.");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("User information is missing");
		}
		
		User user = getBySakaiUserId(sakaiUserId);
		
		if (user == null)
		{
			return;
		}
		
		userDao.markAllReadTime(siteId, user.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	public void addModifySiteVisitTime(String siteId, String sakaiUserId)
	{
		if (siteId == null || siteId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Site information is missing.");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("User information is missing");
		}
		
		User user = getBySakaiUserId(sakaiUserId);
		
		if (user == null)
		{
			return;
		}
		
		userDao.addUpdateSiteVisitTime(siteId, user.getId());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addUserToSiteUsers(String siteId, int userId) throws UserAlreadyInSiteUsersException
	{
		if (siteId == null)
		{
			throw new IllegalArgumentException();
		}
		
		//TODO access check
		
		userDao.addToSiteUsers(siteId, userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public User createUser(String sakaiUserId) throws UserAlreadyExistException
	{
		UserImpl jforumUserImpl = null;
		
		org.sakaiproject.user.api.User sakaiUser;
		try
		{
			sakaiUser = userDirectoryService.getUser(sakaiUserId);
		}
		catch (UserNotDefinedException e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e.toString(), e);
			}
			
			throw new IllegalArgumentException("user not defined in sakai");
		}
		
		if (sakaiUser != null)
		{
			jforumUserImpl = new UserImpl();
			jforumUserImpl.setUsername(sakaiUser.getEid());
			jforumUserImpl.setSakaiUserId(sakaiUser.getId());
			jforumUserImpl.setEmail(sakaiUser.getEmail());
			jforumUserImpl.setFirstName(sakaiUser.getFirstName());
			jforumUserImpl.setLastName(sakaiUser.getLastName());
			
			int jforumUserId = createUser(jforumUserImpl);
			jforumUserImpl.setId(jforumUserId);
		}
			
		return (User)jforumUserImpl;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int createUser(User user) throws UserAlreadyExistException
	{
		if (user == null || user.getSakaiUserId() == null || user.getSakaiUserId().trim().length() == 0)
		{
			new IllegalArgumentException("user information is missing");
		}
		
		if (user.getId() != 0)
		{
			return user.getId();
		}
		
		//TODO access check
		
		return userDao.addUser(user);
	}

	public void destroy()
	{
		if (logger.isInfoEnabled())
			logger.info("destroy....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public User getBySakaiUserId(String sakaiUserId)
	{
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			return null;
		}
		// for thread-local caching
		String key = cacheKey(sakaiUserId);
		UserImpl cachedUser = (UserImpl) this.threadLocalManager.get(key);
		if (cachedUser != null)
		{
			return this.clone(cachedUser);
		}
				
		User user = userDao.selectBySakaiUserId(sakaiUserId);
		
		// thread-local cache (copy)
		if (user != null)
		{
			this.threadLocalManager.set(key, this.clone((UserImpl)user));
		}
		else
		{
			try
			{
				user = createUser(sakaiUserId);
				this.threadLocalManager.set(key, this.clone((UserImpl)user));
			}
			catch (UserAlreadyExistException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(e.toString(), e);
				}
			}
		}
		
		return user;
				
	}
	
	/**
	 * {@inheritDoc}
	 */
	public User getByUserId(int userId)
	{
		if (userId <= 0)
		{
			throw new IllegalArgumentException();
		}
		
		// for thread-local caching
		String key = "jforum:user:"+ String.valueOf(userId);
		UserImpl cachedUser = (UserImpl) this.threadLocalManager.get(key);
		if (cachedUser != null)
		{
			return this.clone(cachedUser);
		}
		
		User user = userDao.selectByUserId(userId);
		
		// thread-local cache (copy)
		if (user != null) this.threadLocalManager.set("jforum:user:"+ String.valueOf(userId), this.clone((UserImpl)user));
		
		return user;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getMarkAllReadTime(String siteId, int userId)
	{
		if (siteId == null || siteId.trim().length() == 0)
		{
			throw new IllegalArgumentException("site information is needed");
		}
		
		if (userId <= 0)
		{
			throw new IllegalArgumentException("used information is missing");
		}
		
		return userDao.selectMarkAllReadTime(siteId, userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public User getSiteUser(String siteId, int userId)
	{
		return userDao.selectSiteUser(siteId, userId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<User> getSiteUsers(String siteId)
	{
		if (siteId == null || siteId.trim().length() == 0)
		{
			return null;
		}		
		// get jforum users		
		//List<User> users = userDao.selectSiteUsers(siteId);		
		
		// sync with sakai site users list
		List<User> users = syncSakaiUsers(siteId, 0, 0);
		
		sortUsers(users);
		
		return users;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<User> getSiteUsers(String siteId, int startFrom, int count)
	{
		if (siteId == null || siteId.trim().length() == 0)
		{
			return null;
		}
		
		// sync with sakai site users list
		return syncSakaiUsers(siteId, startFrom, count);
		
		// get jforum users		
		//return userDao.selectSiteUsers(siteId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<User> getSiteUsersAndPostsCount(String siteId, int startFrom, int count)
	{
		if (siteId == null || siteId.trim().length() == 0)
		{
			return null;
		}
		
		// sync with sakai site users list
		List<User> siteUsers = syncSakaiUsers(siteId, startFrom, count);
		
		Map<String, Integer> usersPostCount = userDao.selectSiteUsersPostsCount(siteId);
		
		int userSitePostsCount  = 0;
		for (User siteUser : siteUsers)
		{
			userSitePostsCount  = 0;
			if (usersPostCount.containsKey(siteUser.getSakaiUserId()))
			{
				userSitePostsCount  = usersPostCount.get(siteUser.getSakaiUserId());
				((UserImpl)siteUser).setTotalSitePosts(userSitePostsCount);
			}
		}
		
		return siteUsers;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getSiteVisitTime(String siteId, String sakaiUserId)
	{
		if (siteId == null || siteId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Site information is missing.");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("User information is missing");
		}
		
		User user = getBySakaiUserId(sakaiUserId);
		
		if (user == null)
		{
			return null;
		}
		
		return userDao.selectSiteVisitTime(siteId, user.getId());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getUserSitePostsCount(int userId, String siteId)
	{
		if (userId <= 0 || siteId == null || siteId.trim().length() == 0)
		{
			throw new IllegalArgumentException("user Id or site information is missing.");
		}
		
		String key = "jforum:userSitePostsCount:"+ String.valueOf(userId);
		
		Integer userPostsCount =  (Integer) this.threadLocalManager.get(key);
		if (userPostsCount != null)
		{
			return userPostsCount.intValue();
		}
		
		userPostsCount = userDao.selectUserSitePostsCount(userId, siteId);
		
		this.threadLocalManager.set(key, userPostsCount);
		
		return userPostsCount.intValue();
	}
	
	public void init()
	{
		if (logger.isInfoEnabled())
			logger.info("init....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void modifyUser(User user)
	{
		if (user == null || user.getId() <= 0)
		{
			throw new IllegalArgumentException("user information is missing.");
		}
		
		User exisUser = userDao.selectByUserId(user.getId());
		
		if (exisUser == null)
		{
			return;
		}
		
		// clean html
		String cleanedUserSignature = HtmlHelper.clean(user.getSignature(), true);
		user.setSignature(cleanedUserSignature);
		
		// save user
		userDao.updateUser(user);
		
		Attachment avatarAttachmment = ((UserImpl)user).getAvatarAttach();
		
		// handle avatar
		if (avatarAttachmment != null)
		{
			String avatarStoreDir = ServerConfigurationService.getString(JForumAttachmentService.AVATAR_PATH);
			
			if (avatarStoreDir == null || avatarStoreDir.trim().length() == 0)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("JForum attachments directory ("+ JForumAttachmentService.AVATAR_PATH +") property is not set in sakai.properties ");
				}
				return;
			}
			
			// save avatar if there is no existing avatar
			if (exisUser.getAvatar() == null)
			{
				// save avatar				
				saveAvatar(user, avatarAttachmment, avatarStoreDir);
			}
			else
			{
				// delete existing avatar file
				deleteFile(exisUser.getAvatar());
					
				saveAvatar(user, avatarAttachmment, avatarStoreDir);
			}
		}
		else
		{
			if ((user.getAvatar() ==  null || user.getAvatar().trim().length() == 0) && exisUser.getAvatar() != null && !exisUser.isExternalAvatar())
			{
				// delete exising avatar physical file
				deleteFile(exisUser.getAvatar());
			}
		}
		
		// clear cache
		if (user != null) 
		{
			clearCache(user.getSakaiUserId());
			this.threadLocalManager.set("jforum:user:"+ String.valueOf(user.getId()), null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void modifyUserSakaiInfo(User user)
	{
		if (user == null || user.getSakaiUserId() == null)
		{
			return;
		}

		updateJFUser(user);
	}
	
	/**
	 * @param jforumSecurityService the jforumSecurityService to set
	 */
	public void setJforumSecurityService(JForumSecurityService jforumSecurityService)
	{
		this.jforumSecurityService = jforumSecurityService;
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
	 * @param userDao the userDao to set
	 */
	public void setUserDao(UserDao userDao)
	{
		this.userDao = userDao;
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
	public void setUserSitePostsCount(User user, String siteId)
	{
		if ( user == null || user.getId() <= 0 || siteId == null || siteId.trim().length() == 0)
		{
			throw new IllegalArgumentException("user or site information is missing.");
		}
		
		int userPostsCount = userDao.selectUserSitePostsCount(user.getId(), siteId);
		
		((UserImpl)user).setTotalSitePosts(userPostsCount);
	}
	
	/**
	 * Key for caching a user.
	 * 
	 * @param userId	sakai User Id
	 * 
	 * @return	Cache key
	 */
	protected String cacheKey(String sakaiUserId)
	{
		return "jforum:user:" + sakaiUserId;
	}
	
	/**
	 * Clear the user from thread local cache
	 * 
	 * @param sakaiUserId 	Sakai user id
	 */
	protected void clearCache(String sakaiUserId)
	{
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			return;
		}
		// clear the cache
		this.threadLocalManager.set(cacheKey(sakaiUserId), null);
	}

	/**
	 * Create copy of user
	 * 
	 * @param user		user
	 * 
	 * @return	The copy of user
	 */
	protected UserImpl clone(UserImpl user)
	{
		return new UserImpl(user);
	}
	
	/**
	 * create new users
	 * 
	 * @param siteId	Site or course id
	 * 
	 * @param allSiteUsers	
	 * 
	 * @return
	 */
	protected List<User> createNewUsers(String siteId, List<String> allSiteUsers)
	{
		List<User> users = new ArrayList<User>();

		/* Step 3 : create new users in jforum and add to users list */
		if (logger.isDebugEnabled())
		{
			logger.debug("createNewUsers() : left out users" + allSiteUsers.toString());
		}

		Iterator<String> iter = allSiteUsers.iterator();
		synchronized (obj)
		{
			while (iter.hasNext())
			{
				String newSakaiUserId = (String) iter.next();
				if (logger.isDebugEnabled())
				{
					logger.debug("createNewUsers() : creating jforum user for " + newSakaiUserId);
				}
				
				try
				{
					User jfusr = userDao.selectBySakaiUserId(newSakaiUserId);
					if (jfusr != null)
					{
						try
						{
							// add as site user
							userDao.addToSiteUsers(siteId, jfusr.getId());
						}
						catch (UserAlreadyInSiteUsersException e)
						{
							if (logger.isDebugEnabled())
							{
								logger.debug("createNewUsers() " + e.toString(), e);
							}
						}
						
						users.add(jfusr);
					}
					else
					{
						/* else create new jforum user get sakai user attribs */
						try
						{
							User newUser = createUser(newSakaiUserId);
							userDao.addToSiteUsers(siteId, newUser.getId());
							
							users.add(newUser);
						}
						catch (UserAlreadyExistException e)
						{
							if (logger.isDebugEnabled())
							{
								logger.debug("createNewUsers() " + e.toString(), e);
							}
						}
						catch (UserAlreadyInSiteUsersException e)
						{
							if (logger.isDebugEnabled())
							{
								logger.debug("createNewUsers() " + e.toString(), e);
							}
						}
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error("createNewUsers() " + e.toString(), e);
					}
				}
			}
		}
		return users;
	}
	
	/**
	 * Deletes the attachment file from the file system
	 * 
	 * @param attachment	Attachment
	 */
	protected void deleteFile(String avatarPhysicalFileName)
	{
		if (avatarPhysicalFileName == null || avatarPhysicalFileName.trim().length() == 0)
		{
			return;
		}
		
		String avatarStoreDir = ServerConfigurationService.getString(JForumAttachmentService.AVATAR_PATH);
		
		if (avatarStoreDir == null || avatarStoreDir.trim().length() == 0)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("JForum attachments directory ("+ JForumAttachmentService.ATTACHMENTS_STORE_DIR +") property is not set in sakai.properties ");
			}
			return;
		}
		
		String avatarDirPath = avatarStoreDir + "/images/avatar/";
		
		File f = new File(avatarDirPath + "/" + avatarPhysicalFileName);
		if (f.exists())
		{
			f.delete();
		}
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
	 * Saves user avatar
	 * 
	 * @param user
	 * @param avatarAttachmment
	 * @param avatarStoreDir
	 */
	protected void saveAvatar(User user, Attachment avatarAttachmment, String avatarStoreDir)
	{
		String fileName = null;
		try
		{
			fileName = org.etudes.component.app.jforum.util.MD5.crypt(Integer.toString(user.getId())+System.currentTimeMillis());
		}
		catch (Exception e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("Error while creating the file name for JForum user avatar.", e);
			}
			return;
		}
		
		String realFileName = avatarAttachmment.getInfo().getRealFilename();
		
		if (realFileName == null)
		{
			return;
		}
		
		String extension = realFileName.substring(realFileName.lastIndexOf('.') + 1).toLowerCase();
		
		int type = -1;

		if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg"))
		{
			type = ImageUtils.IMAGE_JPEG;
		}
		else if (extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("png"))
		{
			type = ImageUtils.IMAGE_PNG;
		}

		if (type != -1)
		{
			String avatarTmpFileName = null;
			String avatarDirPath = avatarStoreDir + "/images/avatar/";
			File avatarDir = new File(avatarDirPath);
			if (!avatarDir.exists())
			{
				avatarDir.mkdirs();
			}

			avatarTmpFileName = avatarStoreDir + "/images/avatar/" + fileName + "_tmp." + extension;			
			
			// resize and store
			String avatarFinalFileName = null;
			
			avatarFinalFileName = avatarStoreDir + "/images/avatar/" + fileName + "." + extension;

			saveUploadedFile(avatarAttachmment.getInfo().getFileContent(), avatarTmpFileName);

			// time to check and process the avatar size
			int maxWidth = 150;
			int maxHeight = 150;
			
			try
			{
				String avatarMaxWidth = ServerConfigurationService.getString(JForumAttachmentService.AVATAR_MAX_WIDTH);
				if (avatarMaxWidth == null || avatarMaxWidth.trim().length() == 0)
				{
					maxWidth = Integer.parseInt(JForumAttachmentService.AVATAR_DEFAULT_MAX_WIDTH);
				}
				else
				{
					maxWidth = Integer.parseInt(avatarMaxWidth);
				}
				
				String avatarMaxHeight = ServerConfigurationService.getString(JForumAttachmentService.AVATAR_MAX_HEIGHT);
				
				if (avatarMaxHeight == null || avatarMaxHeight.trim().length() == 0)
				{
					maxHeight = Integer.parseInt(JForumAttachmentService.AVATAR_DEFAULT_MAX_HEIGHT);
				}
				else
				{
					maxHeight = Integer.parseInt(ServerConfigurationService.getString(JForumAttachmentService.AVATAR_MAX_HEIGHT));
				}
			}
			catch (NumberFormatException e1)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("Error in fetching avatar size.", e1);
				}
			}
			

			BufferedImage image;
			try
			{
				image = ImageUtils.resizeImage(avatarTmpFileName, type, maxWidth, maxHeight);
			
				ImageUtils.saveImage(image, avatarFinalFileName, type);
				
				user.setAvatar(fileName + "." + extension);
				
				// update user avatar in the database
				userDao.updateUserAvatar(user);
			}
			catch (IOException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("Error while saving avatar.", e);
				}
			}

			// Delete the temporary file
			new File(avatarTmpFileName).delete();			
		}
	}
	
	/**
	 * Saves file to the disk
	 * 
	 * @param fileContent	File content or body
	 * 
	 * @param filename	File name
	 */
	protected void saveUploadedFile(byte[] fileContent, String filename)
	{
		if ((fileContent == null) || (fileContent.length == 0))
		{
			return;
		}

		File file = new File(filename);

		if (file.exists())
		{
			file.delete();
		}

		FileOutputStream outputStream = null;
		// add the new
		try
		{
			// check for directory
			File parentDirectory = file.getParentFile();
			if (parentDirectory != null)
			{
				parentDirectory.mkdirs();
			}

			// write the file
			outputStream = new FileOutputStream(file);
			outputStream.write(fileContent);
		}
		catch (Throwable t)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("Failed to write the file to disk", t);
			}
		}
		finally
		{
			if (outputStream != null)
			{
				try
				{
					outputStream.flush();
					outputStream.close();
				}
				catch (IOException e)
				{
					
				}
			}
		}
	}

	/**
	 * Sort users alphabatecally
	 * 
	 * @param users	Users
	 */
	protected void sortUsers(List<User> users)
	{
		if (users == null)
		{
			return;
		}
		
		Collections.sort(users, new Comparator<User>()
		{
			public int compare(User user1, User user2) 
			{
				if ((user1.getLastName() == null || user1.getLastName().trim().length() == 0)  || (user2.getLastName() == null || user2.getLastName().trim().length() == 0))
				{
					return 0;
				}
				else
				{
					return user1.getLastName().toUpperCase().compareTo(user2.getLastName().toUpperCase());
				}
			}
		});	
	}
	
	/**
	 * Updates the sakai site users in jforum or creates if not in jforum
	 * 
	 * @param siteId		Site id
	 * 
	 * @param startFrom		Rows from
	 * 
	 * @param count			Number of records
	 * 
	 * @return	Updated users list
	 */
	protected List<User> syncSakaiUsers(String siteId, int startFrom, int count)
	{
		List<User> users = userDao.selectSiteUsers(siteId);
		
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
		
		//remove users that are deleted in the sakai and blocked in the site
		Iterator<String> sakUserIterator = siteUsers.iterator();
		while (sakUserIterator.hasNext())
		{
			org.sakaiproject.user.api.User sakUser = null;
			String userId = (String) sakUserIterator.next();
			try
			{
				sakUser = userDirectoryService.getUser(userId);
			}
			catch (UserNotDefinedException e)
			{
				sakUserIterator.remove();
				continue;
			}
			
			// remove blocked users from the list
			if (jforumSecurityService.isUserBlocked(siteId, sakUser.getId()))
			{
				sakUserIterator.remove();
				continue;
			}
		}
		
		List<String> allSiteUsers = new ArrayList<String>();
		
		allSiteUsers.addAll(siteUsers);
		
		List<String> copyAllSiteUsers = new ArrayList<String>();
		
		Iterator<String> copyiter = allSiteUsers.iterator();
		while (copyiter.hasNext())
		{
			String copyName = (copyiter.next()).toLowerCase();
			copyAllSiteUsers.add(copyName);
		}
		
		/* for existing site users */
		List<User> exisSiteUsers = new ArrayList<User>();
		
		/*
		 * add new members Step 2: find new users
		 */
		Iterator<User> iter = users.iterator();
		while (iter.hasNext())
		{
			User checkusr = (User) iter.next();

			if (allSiteUsers.contains(checkusr.getSakaiUserId().toLowerCase()))
			{
				/* 2a. remove from allsiteusers as user is existing */
				allSiteUsers.remove(checkusr.getSakaiUserId().toLowerCase());

				/* add existing site users */
				exisSiteUsers.add(checkusr);
			}
		}

		/* create new users - create 100 users maximum at a time*/
		if (allSiteUsers.size() > MAX_USERS_CREATE)
		{
			allSiteUsers = allSiteUsers.subList(0, MAX_USERS_CREATE);
		}
		
		List<User> newUsers = createNewUsers(siteId, allSiteUsers);

		users.addAll(newUsers);
		
		// update existing users info
		updateExistingUsersInfo(exisSiteUsers);		
		
		/*dropped students remove from users */
		User adminuser = dropUsers(users, copyAllSiteUsers);

		if (adminuser != null) users.remove(adminuser);
       
		sortUsers(users);
		
		return users;
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
	 * Update user info
	 * 
	 * @param jfuser	Jforum user
	 */
	protected void updateJFUser(User jfuser)
	{
		try
		{
			org.sakaiproject.user.api.User sakUser = userDirectoryService.getUser(jfuser.getSakaiUserId());

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
					userDao.updateSakaiAccountDetails(jfuser);
					
					// clear user in cache
					clearCache(jfuser.getSakaiUserId());
					this.threadLocalManager.set("jforum:user:"+ String.valueOf(jfuser.getId()), null);
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
				userDirectoryService.getUser(userId);
			}
			catch (UserNotDefinedException e)
			{
				sakUserIterator.remove();
			}
		}
		
		List<String> allSiteUsers = new ArrayList<String>();
		
		allSiteUsers.addAll(siteUsers);
		List<String> copyallsiteusers = new ArrayList<String>();
		Iterator<String> copyiter = allSiteUsers.iterator();
		while (copyiter.hasNext())
		{
			String copyname = (copyiter.next()).toLowerCase();
			copyallsiteusers.add(copyname);
		}
		
		/* for existing site users */
		List<User> exisSiteUsers = new ArrayList<User>();
		
		List<User> users = getSiteUsers(siteId);

		//find new users
		Iterator<User> iter = users.iterator();
		while (iter.hasNext())
		{
			User checkusr = iter.next();

			if (allSiteUsers.contains(checkusr.getSakaiUserId().toLowerCase()))
			{
				/* remove from allSiteUsers as user is existing */
				allSiteUsers.remove(checkusr.getSakaiUserId().toLowerCase());

				/* add existing site users */
				exisSiteUsers.add(checkusr);
			}
		}
		
		updateExistingUsersInfo(exisSiteUsers);		
		
		/*dropped students remove from users */
		User adminuser = dropUsers(users, copyallsiteusers);

		if (adminuser != null) users.remove(adminuser);
       
		sortUsers(users);
		
		return users;
	}
}
