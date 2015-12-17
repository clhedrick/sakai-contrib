/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumAttachmentServiceImpl.java $ 
 * $Id: JForumAttachmentServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Attachment;
import org.etudes.api.app.jforum.AttachmentExtension;
import org.etudes.api.app.jforum.AttachmentInfo;
import org.etudes.api.app.jforum.JForumAttachmentBadExtensionException;
import org.etudes.api.app.jforum.JForumAttachmentOverQuotaException;
import org.etudes.api.app.jforum.JForumAttachmentService;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.PrivateMessage;
import org.etudes.api.app.jforum.dao.AttachmentDao;
import org.etudes.component.app.jforum.util.MD5;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.thread_local.api.ThreadLocalManager;

public class JForumAttachmentServiceImpl implements JForumAttachmentService
{
	private static Log logger = LogFactory.getLog(JForumPostServiceImpl.class);
	
	/** Dependency: AttachmentDao */
	protected AttachmentDao attachmentDao = null;
	
	/** Dependency: ThreadLocalManager. */
	protected ThreadLocalManager threadLocalManager = null;
	
	/**
	 * {@inheritDoc}
	 */
	public void deletePostAttachments(Post post)
	{
		List<Attachment> attachments = post.getAttachments();
		if (attachments.size() > 0)
		{
			for(Attachment attachment : attachments)
			{
				//delete attachment in the database
				attachmentDao.deletePostAttachment(post.getId(), attachment.getId());
				
				//delete attachment on the file system
				deleteFile(attachment);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deletePrivateMessageAttachments(PrivateMessage privateMessage)
	{
		List<Attachment> attachments = privateMessage.getPost().getAttachments();
		if (attachments.size() > 0)
		{
			for(Attachment attachment : attachments)
			{
				int attachmentPMCount = attachmentDao.selectAttachmentPrivateMessagesCount(attachment.getId());
				
				if (attachmentPMCount == 0)
				{
					//delete attachment in the database
					attachmentDao.deletePrivateMessageAttachment(attachment.getId());
					
					//delete attachment on the file system
					deleteFile(attachment);
				}
			}
		}
	}
	
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
	public void processEditPostAttachments(Post post) throws JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		if (post ==  null || !post.hasAttachments() || post.getId() == 0)
		{
			return;
		}
		
		List<Attachment> attachments = post.getAttachments();
		
		if ((attachments == null) || (attachments.size() == 0))
		{
			// remove existing attachments if any
			List<Attachment> exisPostAttachments = attachmentDao.selectPostAttachments(post.getId());
			
			deletePostAttachments(post.getId(), exisPostAttachments);
			
			return;
		}
		
		List<Attachment> exisPostAttachments = attachmentDao.selectPostAttachments(post.getId());
		
		List<Integer> editedExisAttachmentList = new ArrayList<Integer>();
		for (Attachment attachment : attachments)
		{
			if (attachment.getId() > 0)
			{
				editedExisAttachmentList.add(new Integer(attachment.getId()));
			}
		}
		
		List<Attachment> deleteExisAttachmentList = new ArrayList<Attachment>();
		//remove existing attachments that are not in the edit list
		for (Attachment attachment : exisPostAttachments)
		{
			if (!editedExisAttachmentList.contains(new Integer(attachment.getId())))
			{
				deleteExisAttachmentList.add(attachment);
			}
		}
		
		//delete removed attachments
		deletePostAttachments(post.getId(), deleteExisAttachmentList);
				
		// check max upload size allowed. attachments size in bytes
		long totalAttachmentsSize = 0;
		
		for ( Iterator<Attachment> attachmentsIter = attachments.iterator(); attachmentsIter.hasNext(); )
		{
			Attachment attachment = attachmentsIter.next();
			
			AttachmentInfo info = attachment.getInfo();
			
			/*existing files have no info.getFileContent() and they have the attachment id*/
			
			// existing files
			//if ((attachment.getId() > 0) && (info.getFileContent() == null || info.getFileContent().length == 0))
			if (attachment.getId() > 0)
			{
				totalAttachmentsSize += info.getFilesize();
				continue;
			}
			
			// new files
			if ((info.getRealFilename() == null) || (info.getRealFilename().trim().length() == 0))
			{
				attachmentsIter.remove();
				continue;
			}
			
			totalAttachmentsSize += info.getFileContent().length;
		}
		
		String attachmentQuotaLimit = ServerConfigurationService.getString(ATTACHMENTS_QUOTA_LIMIT);
		
		// allowed attachment size in bytes
		long allowedUploadSize = 0;
		if (attachmentQuotaLimit == null || attachmentQuotaLimit.trim().length() == 0)
		{
			
			allowedUploadSize = ATTACHMENTS_DEFAULT_QUOTA_LIMIT * 1024 * 1024;
		}
		else
		{
			long allowedAttachmentsQuotaLimit = 0;
			try
			{
				allowedAttachmentsQuotaLimit = Long.parseLong(attachmentQuotaLimit.trim());
			}
			catch (Exception e)
			{
			}
			allowedUploadSize = allowedAttachmentsQuotaLimit * 1024 * 1024;
		}
		
		if (totalAttachmentsSize > allowedUploadSize)
		{
			throw new JForumAttachmentOverQuotaException((int)(allowedUploadSize/1024), (int)(totalAttachmentsSize/1024));
		}
		
		// attachments extensions
		String extensionsCacheKey = extensionsCacheKey();
		Map<String, AttachmentExtension> attachmentExtensionsMap = (Map<String, AttachmentExtension>) this.threadLocalManager.get(extensionsCacheKey);
		if (attachmentExtensionsMap == null)
		{
			// attachments extension
			Map<Integer, AttachmentExtension> attachmentExtensions = attachmentDao.selectAttachmentExtensions();
			
			attachmentExtensionsMap = new HashMap<String, AttachmentExtension>();
			
			for (AttachmentExtension attachmentExtention : attachmentExtensions.values())
			{
				attachmentExtensionsMap.put(attachmentExtention.getExtension().toLowerCase(), attachmentExtention);
			}
			
			this.threadLocalManager.set(extensionsCacheKey, attachmentExtensionsMap);
		}
		
		for ( Iterator<Attachment> attachmentsIter = attachments.iterator(); attachmentsIter.hasNext(); )
		{
			Attachment attachment = attachmentsIter.next();
			
			// existing attachment no need to save
			if (attachment.getId() > 0)
			{
				attachmentsIter.remove();
				continue;
			}
			AttachmentInfo info = attachment.getInfo();
			
			info.setFilesize(info.getFileContent().length);
			info.setUploadTime(new Date());
			
			String fileExtension = info.getRealFilename().trim().substring(info.getRealFilename().trim().lastIndexOf('.') + 1).toLowerCase();
			
			AttachmentExtension attachmentExtension = attachmentExtensionsMap.get(fileExtension);
			
			// Check if the extension is allowed
			if (attachmentExtensionsMap.containsKey(fileExtension))
			{
				if (!attachmentExtension.isAllow())
				{
					throw new JForumAttachmentBadExtensionException(fileExtension);
				}
			}
			else
			{
				// though it's in the list of allowed or not allowed, allow to upload
				attachmentExtension = new AttachmentExtensionImpl();
				attachmentExtension.setExtension(fileExtension);				
			}
			
			info.setExtension(attachmentExtension);
			
			String savePath = makeStoreFilename(post.getPostedBy().getId(), info);
			
			if (savePath == null)
			{
				attachmentsIter.remove();
				continue;
			}
			info.setPhysicalFilename(savePath);
			
			attachment.setPostId(post.getId());
			attachment.setPrivmsgsId(0);
			attachment.setUserId(post.getPostedBy().getId());
		}
		
		//save attachments
		savePostAttachments(attachments);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void processPostAttachments(Post post) throws JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		if (post ==  null || !post.hasAttachments() || post.getId() == 0)
		{
			return;
		}
		
		List<Attachment> attachments = post.getAttachments();
		
		if (attachments.size() == 0)
		{
			return;
		}
		
		// check max upload size allowed. attachments size in bytes
		long totalAttachmentsSize = 0;
		
		for ( Iterator<Attachment> attachmentsIter = attachments.iterator(); attachmentsIter.hasNext(); )
		{
			Attachment attachment = attachmentsIter.next();
			
			AttachmentInfo info = attachment.getInfo();
			
			if (info.getFileContent() == null || info.getFileContent().length == 0)
			{
				attachmentsIter.remove();
				continue;
			}
			
			if ((info.getRealFilename() == null) || (info.getRealFilename().trim().length() == 0))
			{
				attachmentsIter.remove();
				continue;
			}
			
			totalAttachmentsSize += info.getFileContent().length;
		}
		
		String attachmentQuotaLimit = ServerConfigurationService.getString(ATTACHMENTS_QUOTA_LIMIT);
		
		// allowed attachment size in bytes
		long allowedUploadSize = 0;
		if (attachmentQuotaLimit == null || attachmentQuotaLimit.trim().length() == 0)
		{
			
			allowedUploadSize = ATTACHMENTS_DEFAULT_QUOTA_LIMIT * 1024 * 1024;
		}
		else
		{
			long allowedAttachmentsQuotaLimit = 0;
			try
			{
				allowedAttachmentsQuotaLimit = Long.parseLong(attachmentQuotaLimit.trim());
			}
			catch (Exception e)
			{
			}
			allowedUploadSize = allowedAttachmentsQuotaLimit * 1024 * 1024;
		}
		
		if (totalAttachmentsSize > allowedUploadSize)
		{
			throw new JForumAttachmentOverQuotaException((int)(allowedUploadSize/1024), (int)(totalAttachmentsSize/1024));
		}
		
		// attachments extensions
		String extensionsCacheKey = extensionsCacheKey();
		Map<String, AttachmentExtension> attachmentExtensionsMap = (Map<String, AttachmentExtension>) this.threadLocalManager.get(extensionsCacheKey);
		if (attachmentExtensionsMap == null)
		{
			// attachments extension
			Map<Integer, AttachmentExtension> attachmentExtensions = attachmentDao.selectAttachmentExtensions();
			
			attachmentExtensionsMap = new HashMap<String, AttachmentExtension>();
			
			for (AttachmentExtension attachmentExtention : attachmentExtensions.values())
			{
				attachmentExtensionsMap.put(attachmentExtention.getExtension().toLowerCase(), attachmentExtention);
			}
			
			this.threadLocalManager.set(extensionsCacheKey, attachmentExtensionsMap);
		}
		
		for ( Iterator<Attachment> attachmentsIter = attachments.iterator(); attachmentsIter.hasNext(); )
		{
			Attachment attachment = attachmentsIter.next();
			
			AttachmentInfo info = attachment.getInfo();
			
			info.setFilesize(info.getFileContent().length);
			info.setUploadTime(new Date());
			
			String fileExtension = info.getRealFilename().trim().substring(info.getRealFilename().trim().lastIndexOf('.') + 1).toLowerCase();
			
			AttachmentExtension attachmentExtension = attachmentExtensionsMap.get(fileExtension);
			
			// Check if the extension is allowed
			if (attachmentExtensionsMap.containsKey(fileExtension))
			{
				if (!attachmentExtension.isAllow())
				{
					throw new JForumAttachmentBadExtensionException(fileExtension);
				}
			}
			else
			{
				// though it's in the list of allowed or not allowed, allow to upload
				attachmentExtension = new AttachmentExtensionImpl();
				attachmentExtension.setExtension(fileExtension);				
			}
			
			info.setExtension(attachmentExtension);
			
			String savePath = makeStoreFilename(post.getPostedBy().getId(), info);
			
			if (savePath == null)
			{
				attachmentsIter.remove();
				continue;
			}
			info.setPhysicalFilename(savePath);
			
			attachment.setPostId(post.getId());
			attachment.setPrivmsgsId(0);
			attachment.setUserId(post.getPostedBy().getId());
		}
		
		//save attachments
		savePostAttachments(attachments);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void processPrivateMessageAttachments(PrivateMessage privateMessage, List<Integer> pmIds) throws JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		// for each message there is one message goes to from user sent box and another goes to to user inbox
		if ((privateMessage == null) || (pmIds == null) || (pmIds.size() < 2 || ((pmIds.size() % 2) != 0) ))
		{
			return;
		}
		
		Post post = privateMessage.getPost();
		
		if (post ==  null || !post.hasAttachments())
		{
			return;
		}
		
		List<Attachment> attachments = post.getAttachments();
		
		if (attachments.size() == 0)
		{
			return;
		}
		
		// check max upload size allowed. attachments size in bytes
		long totalAttachmentsSize = 0;
		
		for ( Iterator<Attachment> attachmentsIter = attachments.iterator(); attachmentsIter.hasNext(); )
		{
			Attachment attachment = attachmentsIter.next();
			
			AttachmentInfo info = attachment.getInfo();
			
			if (info.getFileContent() == null || info.getFileContent().length == 0)
			{
				attachmentsIter.remove();
				continue;
			}
			
			if ((info.getRealFilename() == null) || (info.getRealFilename().trim().length() == 0))
			{
				attachmentsIter.remove();
				continue;
			}
			
			totalAttachmentsSize += info.getFileContent().length;
		}
		
		String attachmentQuotaLimit = ServerConfigurationService.getString(ATTACHMENTS_QUOTA_LIMIT);
		
		// allowed attachment size in bytes
		long allowedUploadSize = 0;
		if (attachmentQuotaLimit == null || attachmentQuotaLimit.trim().length() == 0)
		{
			
			allowedUploadSize = ATTACHMENTS_DEFAULT_QUOTA_LIMIT * 1024 * 1024;
		}
		else
		{
			long allowedAttachmentsQuotaLimit = 0;
			try
			{
				allowedAttachmentsQuotaLimit = Long.parseLong(attachmentQuotaLimit.trim());
			}
			catch (Exception e)
			{
			}
			allowedUploadSize = allowedAttachmentsQuotaLimit * 1024 * 1024;
		}
		
		if (totalAttachmentsSize > allowedUploadSize)
		{
			throw new JForumAttachmentOverQuotaException((int)(allowedUploadSize/1024), (int)(totalAttachmentsSize/1024));
		}
		
		// attachments extensions
		String extensionsCacheKey = extensionsCacheKey();
		Map<String, AttachmentExtension> attachmentExtensionsMap = (Map<String, AttachmentExtension>) this.threadLocalManager.get(extensionsCacheKey);
		if (attachmentExtensionsMap == null)
		{
			// attachments extension
			Map<Integer, AttachmentExtension> attachmentExtensions = attachmentDao.selectAttachmentExtensions();
			
			attachmentExtensionsMap = new HashMap<String, AttachmentExtension>();
			
			for (AttachmentExtension attachmentExtention : attachmentExtensions.values())
			{
				attachmentExtensionsMap.put(attachmentExtention.getExtension().toLowerCase(), attachmentExtention);
			}
			
			this.threadLocalManager.set(extensionsCacheKey, attachmentExtensionsMap);
		}
		
		for ( Iterator<Attachment> attachmentsIter = attachments.iterator(); attachmentsIter.hasNext(); )
		{
			Attachment attachment = attachmentsIter.next();
			
			AttachmentInfo info = attachment.getInfo();
			
			info.setFilesize(info.getFileContent().length);
			info.setUploadTime(new Date());
			
			String fileExtension = info.getRealFilename().trim().substring(info.getRealFilename().trim().lastIndexOf('.') + 1).toLowerCase();
			
			AttachmentExtension attachmentExtension = attachmentExtensionsMap.get(fileExtension);
			
			// Check if the extension is allowed
			if (attachmentExtensionsMap.containsKey(fileExtension))
			{
				if (!attachmentExtension.isAllow())
				{
					throw new JForumAttachmentBadExtensionException(fileExtension);
				}
			}
			else
			{
				// though it's in the list of allowed or not allowed, allow to upload
				attachmentExtension = new AttachmentExtensionImpl();
				attachmentExtension.setExtension(fileExtension);				
			}
			
			info.setExtension(attachmentExtension);
			
			String savePath = makeStoreFilename(privateMessage.getFromUser().getId(), info);
			
			if (savePath == null)
			{
				attachmentsIter.remove();
				continue;
			}
			info.setPhysicalFilename(savePath);
			
			attachment.setPostId(0);
			attachment.setPrivmsgsId(0);
			attachment.setUserId(privateMessage.getFromUser().getId());
		}
		
		//save attachments
		savePrivateMessageAttachments(attachments, pmIds);
	}
	
	/**
	 * @param attachmentDao the attachmentDao to set
	 */
	public void setAttachmentDao(AttachmentDao attachmentDao)
	{
		this.attachmentDao = attachmentDao;
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
	 * {@inheritDoc}
	 */
	public void validateEditPostAttachments(Post post) throws JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		if (post ==  null || !post.hasAttachments() || post.getId() == 0)
		{
			return;
		}
		
		List<Attachment> attachments = post.getAttachments();
		
		if ((attachments == null) || (attachments.size() == 0))
		{
			return;
		}
		
		// check max upload size allowed. attachments size in bytes
		long totalAttachmentsSize = 0;
		
		for ( Iterator<Attachment> attachmentsIter = attachments.iterator(); attachmentsIter.hasNext(); )
		{
			Attachment attachment = attachmentsIter.next();
			
			AttachmentInfo info = attachment.getInfo();
			
			/*existing files have no info.getFileContent() and they have the attachment id*/
			
			// existing files
			//if ((attachment.getId() > 0) && (info.getFileContent() == null || info.getFileContent().length == 0))
			if (attachment.getId() > 0)
			{
				totalAttachmentsSize += info.getFilesize();
				continue;
			}
			
			// new files
			if ((info.getRealFilename() == null) || (info.getRealFilename().trim().length() == 0))
			{
				attachmentsIter.remove();
				continue;
			}
			
			totalAttachmentsSize += info.getFileContent().length;
		}
		
		String attachmentQuotaLimit = ServerConfigurationService.getString(ATTACHMENTS_QUOTA_LIMIT);
		
		// allowed attachment size in bytes
		long allowedUploadSize = 0;
		if (attachmentQuotaLimit == null || attachmentQuotaLimit.trim().length() == 0)
		{
			
			allowedUploadSize = ATTACHMENTS_DEFAULT_QUOTA_LIMIT * 1024 * 1024;
		}
		else
		{
			long allowedAttachmentsQuotaLimit = 0;
			try
			{
				allowedAttachmentsQuotaLimit = Long.parseLong(attachmentQuotaLimit.trim());
			}
			catch (Exception e)
			{
			}
			allowedUploadSize = allowedAttachmentsQuotaLimit * 1024 * 1024;
		}
		
		if (totalAttachmentsSize > allowedUploadSize)
		{
			throw new JForumAttachmentOverQuotaException((int)(allowedUploadSize/1024), (int)(totalAttachmentsSize/1024));
		}
		
		// attachments extensions
		String extensionsCacheKey = extensionsCacheKey();
		Map<String, AttachmentExtension> attachmentExtensionsMap = (Map<String, AttachmentExtension>) this.threadLocalManager.get(extensionsCacheKey);
		if (attachmentExtensionsMap == null)
		{
			// attachments extension
			Map<Integer, AttachmentExtension> attachmentExtensions = attachmentDao.selectAttachmentExtensions();
			
			attachmentExtensionsMap = new HashMap<String, AttachmentExtension>();
			
			for (AttachmentExtension attachmentExtention : attachmentExtensions.values())
			{
				attachmentExtensionsMap.put(attachmentExtention.getExtension().toLowerCase(), attachmentExtention);
			}
			
			this.threadLocalManager.set(extensionsCacheKey, attachmentExtensionsMap);
		}
		
		// check attachment extensions
		for ( Iterator<Attachment> attachmentsIter = attachments.iterator(); attachmentsIter.hasNext(); )
		{
			Attachment attachment = attachmentsIter.next();
			
			// existing attachment no need to check the extenstion
			if (attachment.getId() > 0)
			{
				continue;
			}
			AttachmentInfo info = attachment.getInfo();
			
			info.setFilesize(info.getFileContent().length);
			info.setUploadTime(new Date());
			
			String fileExtension = info.getRealFilename().trim().substring(info.getRealFilename().trim().lastIndexOf('.') + 1).toLowerCase();
			
			AttachmentExtension attachmentExtension = attachmentExtensionsMap.get(fileExtension);
			
			// Check if the extension is allowed
			if (attachmentExtensionsMap.containsKey(fileExtension))
			{
				if (!attachmentExtension.isAllow())
				{
					throw new JForumAttachmentBadExtensionException(fileExtension);
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void validatePostAttachments(Post post) throws JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		if (post ==  null || !post.hasAttachments())
		{
			return;
		}
		
		List<Attachment> attachments = post.getAttachments();
		
		if (attachments.size() == 0)
		{
			return;
		}
		/*check attachment quota limit*/
		// check max upload size allowed. attachments size in bytes
		long totalAttachmentsSize = 0;
		
		for ( Iterator<Attachment> attachmentsIter = attachments.iterator(); attachmentsIter.hasNext(); )
		{
			Attachment attachment = attachmentsIter.next();
			
			AttachmentInfo info = attachment.getInfo();
			
			if (info.getFileContent() == null || info.getFileContent().length == 0)
			{
				attachmentsIter.remove();
				continue;
			}
			
			if ((info.getRealFilename() == null) || (info.getRealFilename().trim().length() == 0))
			{
				attachmentsIter.remove();
				continue;
			}
			
			totalAttachmentsSize += info.getFileContent().length;
		}
		
		String attachmentQuotaLimit = ServerConfigurationService.getString(ATTACHMENTS_QUOTA_LIMIT);
		
		// allowed attachment size in bytes
		long allowedUploadSize = 0;
		if (attachmentQuotaLimit == null || attachmentQuotaLimit.trim().length() == 0)
		{
			
			allowedUploadSize = ATTACHMENTS_DEFAULT_QUOTA_LIMIT * 1024 * 1024;
		}
		else
		{
			long allowedAttachmentsQuotaLimit = 0;
			try
			{
				allowedAttachmentsQuotaLimit = Long.parseLong(attachmentQuotaLimit.trim());
			}
			catch (Exception e)
			{
			}
			allowedUploadSize = allowedAttachmentsQuotaLimit * 1024 * 1024;
		}
		
		if (totalAttachmentsSize > allowedUploadSize)
		{
			throw new JForumAttachmentOverQuotaException((int)(allowedUploadSize/1024), (int)(totalAttachmentsSize/1024));
		}
		
		/*check attachment extensions*/
		// attachments extensions
		String extensionsCacheKey = extensionsCacheKey();
		Map<String, AttachmentExtension> attachmentExtensionsMap = (Map<String, AttachmentExtension>) this.threadLocalManager.get(extensionsCacheKey);
		if (attachmentExtensionsMap == null)
		{
			// attachments extension
			Map<Integer, AttachmentExtension> attachmentExtensions = attachmentDao.selectAttachmentExtensions();
			
			attachmentExtensionsMap = new HashMap<String, AttachmentExtension>();
			
			for (AttachmentExtension attachmentExtention : attachmentExtensions.values())
			{
				attachmentExtensionsMap.put(attachmentExtention.getExtension().toLowerCase(), attachmentExtention);
			}
			
			this.threadLocalManager.set(extensionsCacheKey, attachmentExtensionsMap);
		}
		
		for ( Iterator<Attachment> attachmentsIter = attachments.iterator(); attachmentsIter.hasNext(); )
		{
			Attachment attachment = attachmentsIter.next();
			
			AttachmentInfo info = attachment.getInfo();
			
			String fileExtension = info.getRealFilename().trim().substring(info.getRealFilename().trim().lastIndexOf('.') + 1).toLowerCase();
			
			AttachmentExtension attachmentExtension = attachmentExtensionsMap.get(fileExtension);
			
			// Check if the extension is allowed
			if (attachmentExtensionsMap.containsKey(fileExtension))
			{
				if (!attachmentExtension.isAllow())
				{
					throw new JForumAttachmentBadExtensionException(fileExtension);
				}
			}
		}
	}

	/**
	 * Clear the attachments extensions from thread local cache
	 */
	protected void clearExtensionsCache()
	{
		// clear the cache
		this.threadLocalManager.set(extensionsCacheKey(), null);
	}

	/**
	 * Deletes the attachment file from the file system
	 * 
	 * @param attachment	Attachment
	 */
	protected void deleteFile(Attachment attachment)
	{
		String value = ServerConfigurationService.getString(ATTACHMENTS_STORE_DIR);
		
		if (value == null || value.trim().length() == 0)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("JForum attachments directory ("+ ATTACHMENTS_STORE_DIR +") property is not set in sakai.properties ");
			}
			return;
		}
		
		File f = new File(value + "/" + attachment.getInfo().getPhysicalFilename());
		if (f.exists())
		{
			f.delete();
		}
	}
	
	/**
	 * Deletes post attachments
	 * 
	 * @param postId	Post id
	 * 
	 * @param attachments	Attachments
	 */
	protected void deletePostAttachments(int postId, List<Attachment> attachments)
	{
		if (postId <= 0)
		{
			return;
		}
		
		if (attachments != null && attachments.size() > 0)
		{
			for(Attachment attachment : attachments)
			{
				//delete post attachment in the database
				attachmentDao.deletePostAttachment(postId, attachment.getId());
				
				//delete attachment on the file system
				deleteFile(attachment);
			}
		}
	}
	
	/**
	 * Key for caching attachments extensions.
	 * 
	 * @return	Cache key
	 */
	protected String extensionsCacheKey()
	{
		return "jforum:attachmentExtensionsMap";
	}
	
	/**
	 * Makes file name with the path where it is stored
	 * 
	 * @param postedBy	posted by user id
	 * 
	 * @param AttachmentInfo	Attachment info
	 * @return
	 */
	protected String makeStoreFilename(int postedBy, AttachmentInfo attachmentInfo)
	{
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(System.currentTimeMillis());
		c.get(Calendar.YEAR);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		String dir = "" + year + "/" + month + "/" + day + "/";
		
		String value = ServerConfigurationService.getString(ATTACHMENTS_STORE_DIR);
		
		if (value == null || value.trim().length() == 0)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("JForum attachments directory ("+ ATTACHMENTS_STORE_DIR +") property is not set in sakai.properties ");
			}
			return null;
		}
		
		try
		{
			return dir
			+ MD5.crypt(attachmentInfo.getRealFilename() + attachmentInfo.getUploadTime().getTime()) 
			+ "_" + postedBy
			+ "." + attachmentInfo.getExtension().getExtension();
		}
		catch (Exception e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("Error while creating the file name for JForum attachment.", e);
			}
			return null;
		}
	}
	
	/**
	 * Saves the post attachments
	 * 
	 * @param attachments	Attachments
	 * 
	 * @return		true - if saved successfully
	 * 				false - if not saved
	 */
	protected boolean savePostAttachments(List<Attachment> attachments)
	{
		String attachmentStoreDir = ServerConfigurationService.getString(ATTACHMENTS_STORE_DIR);
		
		if (attachmentStoreDir == null || attachmentStoreDir.trim().length() == 0)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("JForum attachments directory ("+ ATTACHMENTS_STORE_DIR +") property is not set in sakai.properties ");
			}
			return false;
		}
		
		for (Attachment attachment : attachments)
		{
			String path =  attachmentStoreDir + "/" + attachment.getInfo().getPhysicalFilename();
			
			if (attachment.getId() > 0)
			{
				continue;
			}
			
			try
			{
				//save attachment
				attachmentDao.addPostAttachment(attachment);
	
				// save file to disk
				saveUploadedFile(attachment.getInfo().getFileContent(),  path);
			}
			catch (Exception e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("Error while saving attachments. "+ e.toString(), e);
				}
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Saves the post attachments
	 * 
	 * @param attachments	Attachments
	 * 
	 * @return		true - if saved successfully
	 * 				false - if not saved
	 */
	protected boolean savePrivateMessageAttachments(List<Attachment> attachments, List<Integer> pmIds)
	{
		String attachmentStoreDir = ServerConfigurationService.getString(ATTACHMENTS_STORE_DIR);
		
		if (attachmentStoreDir == null || attachmentStoreDir.trim().length() == 0)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("JForum attachments directory ("+ ATTACHMENTS_STORE_DIR +") property is not set in sakai.properties ");
			}
			return false;
		}
		
		for (Attachment attachment : attachments)
		{
			String path =  attachmentStoreDir + "/" + attachment.getInfo().getPhysicalFilename();
			
			try
			{
				//save attachment
				attachmentDao.addPrivateMessageAttachment(attachment, pmIds);
	
				// save file to disk
				saveUploadedFile(attachment.getInfo().getFileContent(),  path);
			}
			catch (Exception e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn("Error while saving attachments. "+ e.toString(), e);
				}
				return false;
			}
		}
		return true;
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

}
