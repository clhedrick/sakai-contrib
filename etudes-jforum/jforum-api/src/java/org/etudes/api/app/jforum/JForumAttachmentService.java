/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/JForumAttachmentService.java $ 
 * $Id: JForumAttachmentService.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.api.app.jforum;

import java.util.List;

public interface JForumAttachmentService
{
	/* Default total attachments quota limit in mega bytes */
	public static final int ATTACHMENTS_DEFAULT_QUOTA_LIMIT = 2;
	public static final String ATTACHMENTS_QUOTA_LIMIT = "etudes.jforum.attachments.quota.limit";
	public static final String ATTACHMENTS_STORE_DIR = "etudes.jforum.attachments.store.dir";
	
	public static final String AVATAR_CLUSTERED = "etudes.jforum.avatar.clustered";
	public static final String AVATAR_PATH = "etudes.jforum.avatar.path";
	public static final String AVATAR_CONTEXT = "etudes.jforum.avatar.context";
	public static final String AVATAR_MAX_WIDTH = "etudes.jforum.avatar.maxWidth";
	public static final String AVATAR_DEFAULT_MAX_WIDTH = "150";
	public static final String AVATAR_MAX_HEIGHT = "etudes.jforum.avatar.maxHeight";
	public static final String AVATAR_DEFAULT_MAX_HEIGHT = "150";	
	
	/**
	 * Deletes post attachments
	 * 
	 * @param post	Post
	 */
	public void deletePostAttachments(Post post);
	
	/**
	 * Deletes private message attachments if not referenced by private messages
	 * 
	 * @param privateMessage	Private message
	 */
	public void deletePrivateMessageAttachments(PrivateMessage privateMessage);
	
	/**
	 * Processes the post attachments in the post edit mode. Removes the attachments if removed from the post and adds new attachments
	 *  
	 * @param post	Post
	 * 
	 * @throws JForumAttachmentOverQuotaException	if attachments total size is more than allowed
	 * 
	 * @throws JForumAttachmentBadExtensionException	if the attachment has not allowed extension
	 */
	public void processEditPostAttachments(Post post) throws JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException;
	
	/**
	 * Saves post attachments
	 * 
	 * @param post
	 * 
	 * @throws JForumAttachmentOverQuotaException	if attachments total size is more than allowed
	 * 
	 * @throws JForumAttachmentBadExtensionException	if the attachment has not allowed extension
	 */
	public void processPostAttachments(Post post) throws JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException;
	
	/**
	 * Saves private message attachments
	 * 
	 * @param privateMessage	private message	
	 * 
	 * @param pmIds 	private message id's of from user sent box and to user inbox private
	 * 
	 * @throws JForumAttachmentOverQuotaException	if attachments total size is more than allowed
	 * 
	 * @throws JForumAttachmentBadExtensionException	if the attachment has not allowed extension
	 */
	public void processPrivateMessageAttachments(PrivateMessage privateMessage, List<Integer> pmIds) throws JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException;
	
	/**
	 * Validate edit post attachments
	 * 
	 * @param post	Post
	 * 
	 * @throws JForumAttachmentOverQuotaException	if attachments total size is more than allowed
	 * 
	 * @throws JForumAttachmentBadExtensionException	if the attachment has not allowed extension
	 */
	public void validateEditPostAttachments(Post post) throws JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException;
	
	/**
	 * checks the post attachments for not allowed extensions and attachments quota limit
	 * 
	 * @param post	Post
	 * 
	 * @throws JForumAttachmentOverQuotaException	if attachments total size is more than allowed
	 * 
	 * @throws JForumAttachmentBadExtensionException	if the attachment has not allowed extension
	 */
	public void validatePostAttachments(Post post) throws JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException;
}
