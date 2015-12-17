/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/JForumPrivateMessageService.java $ 
 * $Id: JForumPrivateMessageService.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.util.Date;
import java.util.List;

public interface JForumPrivateMessageService
{
	/**
	 * Deletes the private message and it's attachments
	 * 
	 * @param context		The context or site id
	 * 
	 * @param sakaiUserId		User sakai id
	 * 
	 * @param privateMessageIds		Private message id's that are to be deleted
	 * 
	 * @throws JForumAccessException	If user has has no access to the private message site or context 
	 */
	public void deleteMessage(String context, String sakaiUserId, List<Integer> privateMessageIds)throws JForumAccessException;
	
	/**
	 * 	Flags the private message to follow up or not
	 * 
	 * @param privateMessage	Private message
	 */
	public void flagToFollowup(PrivateMessage privateMessage);
	
	/**
	 * Get the private message
	 * 
	 * @param privateMessageId	The private message id
	 * 
	 * @return	The private message
	 */
	public PrivateMessage getPrivateMessage(int privateMessageId);
	
	/**
	 * Gets the private message attachment
	 * 
	 * @param attachmentId	Attachment id
	 * 
	 * @return	Attachment
	 */
	public Attachment getPrivateMessageAttachment(int attachmentId);
	
	/**
	 * Gets the count of private messages that are new
	 * 
	 * @param context	The context
	 * 
	 * @param sakaiUserId	User sakai id
	 * 
	 * @return	The count of private messages that are new
	 */
	public int getUserUnreadCount(String context, String sakaiUserId);
	
	/**
	 * Gets the private messages received by the user in the site
	 * 
	 * @param context		The context
	 * 
	 * @param sakaiUserId	User sakai id
	 * 
	 * @return	The list of private messages received by the user in the site
	 */
	public List<PrivateMessage> inbox(String context, String sakaiUserId);
	
	/**
	 * Marks user private message read or unread
	 * 
	 * @param messageId		Privare message id
	 * 
	 * @param sakaiUserId	Sakai user id
	 * 
	 * @param readTime		Read time or unread marked time
	 * 
	 * @param isRead		Read or unread
	 */
	public void markMessageRead(int messageId, String sakaiUserId, Date readTime, boolean isRead);
	
	/**
	 * Creates new instance of Attachment object with AttachmentInfo
	 * 
	 * @param fileName		Attachment info file name
	 * 
	 * @param contentType	Content type
	 * 
	 * @param comments		Comments or description
	 * 
	 * @param fileContent	File content or body
	 * 
	 * @return	The attachment object or null
	 */
	public Attachment newAttachment(String fileName, String contentType, String comments, byte[] fileContent);
	
	/**
	 * Creates new instance of PrivateMessage object from existing private message with id, context, from user, and to user 
	 * 
	 * @param existingPrivateMessageId	Existing private message id
	 * 
	 * @return	The private message object with id, context, from user, and to user if existing or null
	 */
	public PrivateMessage newPrivateMessage(int existingPrivateMessageId);
	
	/**
	 * Creates new private message object
	 * 
	 * @param fromSakaiUserId	From sakai user id
	 * 
	 * @return	The private message object with from user
	 */
	public PrivateMessage newPrivateMessage(String fromSakaiUserId);
	
	/**
	 * Creates new instance of PrivateMessage object
	 * 
	 * @param	fromSakaiUserId		From sakai user id
	 * 
	 * @param	toSakaiUserId		To sakai user id
	 * 
	 * @return	The private message object with from user and to user
	 */
	public PrivateMessage newPrivateMessage(String fromSakaiUserId, String toSakaiUserId);
	
	/**
	 * Reply to an existing private message
	 * 
	 * @param privateMessage	Private message with id, context, from user, and to user from exiting private message and post information
	 * 
	  * @throws JForumAccessException	if "from user" or "to user' has no access to the private message site or context
	 * 
	 * @throws JForumAttachmentOverQuotaException	if attachments total size is more than allowed
	 * 
	 * @throws JForumAttachmentBadExtensionException	if the attachment has not allowed extension
	 */
	public void replyPrivateMessage(PrivateMessage privateMessage) throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException;
	
	/**
	 * Sends the private message to one user and supports attachments with no validation for max attachments size and extensions. 
	 * If attachments exceeds max quota allowed they are not uploaded and if attachments have extensions that are not allowed they are not uploaded
	 * 
	 * @param privateMessage	Private message
	 */
	public void sendPrivateMessage(PrivateMessage privateMessage);
	
	/**
	 * Sends the private message to one user and supports attachments with validation for max attachments size and extensions
	 * 
	 * @param privateMessage	Private message
	 * 
	 * @throws JForumAccessException	if "from user" or "to user' has no access to the private message site or context
	 * 
	 * @throws JForumAttachmentOverQuotaException	if attachments total size is more than allowed
	 * 
	 * @throws JForumAttachmentBadExtensionException	if the attachment has not allowed extension
	 */
	public void sendPrivateMessageWithAttachments(PrivateMessage privateMessage) throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException;
	
	/**
	 * Sends the private message to multiple users and supports attachments with validation for max attachments size and extensions
	 * 
	 * @param privateMessage	Privare message with private message info
	 * 
	 * @param toSakaiUserIds	Sakai user id's to whom the private message is sent
	 * 
	 * @throws JForumAccessException	if "from user" or "To sakai users" has no access to the private message site or context
	 * 
	 * @throws JForumAttachmentOverQuotaException	if attachments total size is more than allowed
	 * 
	 * @throws JForumAttachmentBadExtensionException	if the attachment has not allowed extension
	 */
	public void sendPrivateMessageWithAttachments(PrivateMessage privateMessage, List<String> toSakaiUserIds) throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException;
	
	/**
	 * Gets the private messages sent by the user in the site
	 * 
	 * @param context		The context
	 * 
	 * @param sakaiUserId	User sakai id
	 * 
	 * @return	The list of private messages sent by the user in the site
	 */
	public List<PrivateMessage> sentbox(String context, String sakaiUserId);
}
