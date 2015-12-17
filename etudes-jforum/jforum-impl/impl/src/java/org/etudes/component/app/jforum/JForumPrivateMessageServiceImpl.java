/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumPrivateMessageServiceImpl.java $ 
 * $Id: JForumPrivateMessageServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Attachment;
import org.etudes.api.app.jforum.JForumAccessException;
import org.etudes.api.app.jforum.JForumAttachmentBadExtensionException;
import org.etudes.api.app.jforum.JForumAttachmentOverQuotaException;
import org.etudes.api.app.jforum.JForumAttachmentService;
import org.etudes.api.app.jforum.JForumEmailExecutorService;
import org.etudes.api.app.jforum.JForumPrivateMessageService;
import org.etudes.api.app.jforum.JForumSecurityService;
import org.etudes.api.app.jforum.JForumUserService;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.PrivateMessage;
import org.etudes.api.app.jforum.User;
import org.etudes.api.app.jforum.dao.AttachmentDao;
import org.etudes.api.app.jforum.dao.PrivateMessageDao;
import org.etudes.component.app.jforum.util.post.EmailUtil;
import org.etudes.component.app.jforum.util.post.PostUtil;
import org.etudes.util.HtmlHelper;
import org.etudes.util.XrefHelper;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.util.Web;

public class JForumPrivateMessageServiceImpl implements JForumPrivateMessageService
{
	private static Log logger = LogFactory.getLog(JForumPrivateMessageServiceImpl.class);
	
	/** Dependency: AttachmentDao */
	protected AttachmentDao attachmentDao = null;
	
	/** Dependency: JForumAttachmentService */
	protected JForumAttachmentService jforumAttachmentService = null;
	
	/** Dependency: JForumUserService. */
	protected JForumEmailExecutorService jforumEmailExecutorService = null;
	
	/** Dependency: JForumSecurityService. */
	protected JForumSecurityService jforumSecurityService = null;
	
	/** Dependency: JForumUserService. */
	protected JForumUserService jforumUserService = null;
	
	/** Dependency: PrivateMessageDao */
	protected PrivateMessageDao privateMessageDao = null;
	
	/** Dependency: SiteService. */
	protected SiteService siteService = null;
	
	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteMessage(String context, String sakaiUserId, List<Integer> privateMessageIds)throws JForumAccessException
	{
		if (context == null || context.trim().length() == 0)
		{
			throw new IllegalArgumentException("context is needed");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakai user id is needed");
		}
		
		if (privateMessageIds == null || privateMessageIds.isEmpty())
		{
			throw new IllegalArgumentException("Privatemessage id's needed");
		}
		//access check for user
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
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		if (user == null)
		{
			return;
		}
		
		for (Integer privateMessageId : privateMessageIds)
		{
			PrivateMessage privateMessage = new PrivateMessageImpl(privateMessageId, context, user, user);
			
			PrivateMessage exisPrivateMessage = privateMessageDao.selectById(privateMessageId);
			
			//delete private message
			privateMessageDao.delete(privateMessage);
			
			//delete attachments on the file system if not referenced by other private messages
			if (exisPrivateMessage.getPost().hasAttachments())
			{
				privateMessage.getPost().setHasAttachments(true);
				privateMessage.getPost().getAttachments().addAll(exisPrivateMessage.getPost().getAttachments());
				jforumAttachmentService.deletePrivateMessageAttachments(privateMessage);
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
	public void flagToFollowup(PrivateMessage privateMessage)
	{
		if (privateMessage == null)
		{
			new IllegalArgumentException();
		}
		
		privateMessageDao.updateFlagToFollowup(privateMessage.getId(), privateMessage.isFlagToFollowup());
	}

	/**
	 * {@inheritDoc}
	 */
	public PrivateMessage getPrivateMessage(int privateMessageId)
	{
		if (privateMessageId <= 0)
		{
			throw new IllegalArgumentException();
		}
		
		PrivateMessage privateMessage = privateMessageDao.selectById(privateMessageId);
		
		if (privateMessage != null)
		{
			if (privateMessage.getPost() != null)
			{
				PostUtil.preparePostForDisplay(privateMessage.getPost());
			}
			
			// user signature
			if (privateMessage.getFromUser() != null)
			{
				User fromUser = privateMessage.getFromUser();
				if (fromUser.getAttachSignatureEnabled() && ((fromUser.getSignature() != null) && (fromUser.getSignature().trim().length() > 0)))
				{
					fromUser.setSignature(PostUtil.processText(fromUser.getSignature()));
				}
				
				//ignore smilies in the signature to UI
			}
		}
				
		return privateMessage;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Attachment getPrivateMessageAttachment(int attachmentId)
	{
		if (attachmentId <= 0) throw new IllegalArgumentException("Attachment id should be greater than 0");
		
		return attachmentDao.selectAttachmentById(attachmentId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getUserUnreadCount(String context, String sakaiUserId)
	{
		if (context == null || context.trim().length() == 0)
		{
			throw new IllegalArgumentException("context is needed");
		}
		
		if (sakaiUserId == null || sakaiUserId.trim().length() == 0)
		{
			throw new IllegalArgumentException("Sakai user id is needed");
		}
		
		User user = jforumUserService.getBySakaiUserId(sakaiUserId);
		
		if (user == null)
		{
			return 0;
		}
		
		return privateMessageDao.selectUnreadCount(context, user.getId());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<PrivateMessage> inbox(String context, String sakaiUserId)
	{
		StringBuilder sql = new StringBuilder();
		

		sql.append("SELECT pm.privmsgs_id, pm.privmsgs_type, pm.privmsgs_subject, pm.privmsgs_from_userid, pm.privmsgs_to_userid, pm.privmsgs_date, pm.privmsgs_ip, ");
		sql.append("pm.privmsgs_enable_bbcode, pm.privmsgs_enable_html, pm.privmsgs_enable_smilies, pm.privmsgs_attach_sig, pm.privmsgs_attachments, pm.privmsgs_flag_to_follow, ");
		sql.append("pm.privmsgs_replied, pm.privmsgs_priority, u2.user_id, u2.sakai_user_id, u2.username, u2.user_fname, u2.user_lname, u2.user_avatar ");
		sql.append("FROM jforum_privmsgs pm, jforum_users u1, jforum_users u2, jforum_sakai_course_privmsgs cp ");
		sql.append("WHERE u1.sakai_user_id = ? ");
		sql.append("AND u1.user_id = pm.privmsgs_to_userid ");
		sql.append("AND u2.user_id = pm.privmsgs_from_userid ");
		sql.append("AND ( pm.privmsgs_type = "+ PrivateMessage.TYPE_NEW +" ");
		sql.append("OR pm.privmsgs_type = "+ PrivateMessage.TYPE_READ +" ");
		sql.append("OR privmsgs_type = "+ PrivateMessage.TYPE_UNREAD +") ");
		sql.append("AND pm.privmsgs_id = cp.privmsgs_id AND cp.course_id = ? ");
		sql.append("ORDER BY pm.privmsgs_date DESC ");		
						
		int i = 0; 
		Object[] fields = new Object[2];
		fields[i++] = sakaiUserId;
		fields[i++] = context;
		
		final List<PrivateMessage> pmList = new ArrayList<PrivateMessage>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					PrivateMessage pm = getPm(result, false);
					
					UserImpl fromUser = new UserImpl();
					fromUser.setId(result.getInt("user_id"));
					fromUser.setSakaiUserId(result.getString("sakai_user_id"));
					fromUser.setFirstName(result.getString("user_fname")==null ? "" : result.getString("user_fname"));
					fromUser.setLastName(result.getString("user_lname")==null ? "" : result.getString("user_lname"));
					fromUser.setAvatar(result.getString("user_avatar"));
					fromUser.setUsername(result.getString("username"));
					pm.setFromUser(fromUser);
					
					pmList.add(pm);
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("inbox: " + e, e);
					}					
				}
				return null;
			}
		});
		
		for (PrivateMessage pm : pmList)
		{
			PostUtil.preparePostForDisplay(pm.getPost());
		}
		return pmList;
	}
	
	public void init()
	{
		if (logger.isInfoEnabled())
			logger.info("init....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void markMessageRead(int messageId, String sakaiUserId, Date readTime, boolean isRead)
	{
		if (messageId == 0)
		{
			new IllegalArgumentException();
		}
		
		int messageType = isRead ? PrivateMessage.PrivateMessageType.READ.getType() : PrivateMessage.PrivateMessageType.NEW.getType();
		
		privateMessageDao.updateMessageType(messageId, messageType);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Attachment newAttachment(String fileName, String contentType, String comments, byte[] fileContent)
	{
		if ((fileName == null || fileName.trim().length() == 0) ||(fileContent == null || fileContent.length == 0))
		{
			return null;
		}
		
		// Get only the filename, without the path
		String separator = "/";
		int index = fileName.indexOf(separator);

		if (index == -1)
		{
			separator = "\\";
			index = fileName.indexOf(separator);
		}

		if (index > -1)
		{
			if (separator.equals("\\"))
			{
				separator = "\\\\";
			}

			String[] p = fileName.split(separator);
			fileName = p[p.length - 1];
		}
		
		Attachment attachment = new AttachmentImpl();
		
		AttachmentInfoImpl attachmentInfo = new AttachmentInfoImpl();
		attachmentInfo.setMimetype(contentType);
		attachmentInfo.setRealFilename(fileName);
		attachmentInfo.setComment(comments);
		attachmentInfo.setFileContent(fileContent);
		
		attachment.setInfo(attachmentInfo);
		
		return attachment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public PrivateMessage newPrivateMessage(int existingPrivateMessageId)
	{
		PrivateMessage existingPrivateMessage = privateMessageDao.selectById(existingPrivateMessageId);
		
		if (existingPrivateMessage == null)
		{
			return null;
		}
		
		int id = existingPrivateMessage.getId();
		String context = existingPrivateMessage.getContext();
		
		User toUser = null;
		User fromUser = null;
		if (existingPrivateMessage.getType() == PrivateMessage.PrivateMessageType.SENT.getType())
		{
			// reply to message sent
			toUser = existingPrivateMessage.getToUser();
			fromUser = existingPrivateMessage.getFromUser();
		}
		else
		{
			// reply to message received
			fromUser = existingPrivateMessage.getToUser();
			toUser = existingPrivateMessage.getFromUser();
		}
				
		PrivateMessage privateMessage = new PrivateMessageImpl(id, context, fromUser, toUser);
				
		return privateMessage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public PrivateMessage newPrivateMessage(String fromSakaiUserId)
	{
		PrivateMessage privateMessage = new PrivateMessageImpl();
		
		privateMessage.setPost(new PostImpl());
		
		User fromUser = jforumUserService.getBySakaiUserId(fromSakaiUserId);
		privateMessage.setFromUser(fromUser);
		
		return privateMessage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public PrivateMessage newPrivateMessage(String fromSakaiUserId, String toSakaiUserId)
	{
		PrivateMessage privateMessage = new PrivateMessageImpl();
		
		privateMessage.setPost(new PostImpl());
		
		User fromUser = jforumUserService.getBySakaiUserId(fromSakaiUserId);
		privateMessage.setFromUser(fromUser);
		
		User toUser = jforumUserService.getBySakaiUserId(toSakaiUserId);
		privateMessage.setToUser(toUser);
		
		return privateMessage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void replyPrivateMessage(PrivateMessage privateMessage) throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		if (privateMessage == null)
		{
			throw new IllegalArgumentException();
		}
		
		if (privateMessage.getId() <= 0)
		{
			throw new IllegalArgumentException("Existing message id is needed.");
		}
		
		if ((privateMessage.getFromUser() == null) || (privateMessage.getToUser() == null))
		{
			throw new IllegalArgumentException("From user and To user information is needed");
		}
		
		if ((privateMessage.getPost() == null))
		{
			throw new IllegalArgumentException("Private message post information is needed");
		}
		
		if (privateMessage.getContext() == null)
		{
			throw new IllegalArgumentException("Private message context is needed");
		}
		
		//access check from user
		boolean facilitator = jforumSecurityService.isJForumFacilitator(privateMessage.getContext(), privateMessage.getFromUser().getSakaiUserId());
		
		boolean participant = false;
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(privateMessage.getContext(), privateMessage.getFromUser().getSakaiUserId());
		}
		
		if (!(facilitator || participant))
		{
			throw new JForumAccessException(privateMessage.getFromUser().getSakaiUserId());
		}
		
		//access check to user
		facilitator = false;
		participant = false;
		
		facilitator = jforumSecurityService.isJForumFacilitator(privateMessage.getContext(), privateMessage.getToUser().getSakaiUserId());
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(privateMessage.getContext(), privateMessage.getToUser().getSakaiUserId());
		}
		
		if (!(facilitator || participant))
		{
			throw new JForumAccessException(privateMessage.getToUser().getSakaiUserId());
		}
		
		Post post = privateMessage.getPost();
		
		//attachments
		if (post.hasAttachments())
		{
			jforumAttachmentService.validatePostAttachments(post);
		}
		
		replyToPrivateMessage(privateMessage);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendPrivateMessage(PrivateMessage privateMessage)
	{
		if (privateMessage == null)
		{
			throw new IllegalArgumentException();
		}
		
		if (privateMessage.getId() > 0)
		{
			throw new IllegalArgumentException("New private message cannot be created as private message having id");
		}
		
		if ((privateMessage.getFromUser() == null) || (privateMessage.getToUser() == null))
		{
			throw new IllegalArgumentException("From user and To user information is needed");
		}
		
		if ((privateMessage.getPost() == null))
		{
			throw new IllegalArgumentException("Private message post information is needed");
		}
		
		if (privateMessage.getContext() == null)
		{
			throw new IllegalArgumentException("Private message context is needed");
		}
		
		// clean html
		String cleanedHtml = HtmlHelper.clean(privateMessage.getPost().getText(), true);
		privateMessage.getPost().setText(cleanedHtml);
		
		// save the message
		List<Integer> pmIds = privateMessageDao.saveMessage(privateMessage);
		
		// process attachments
		Post post = privateMessage.getPost();
		if (post.hasAttachments())
		{
			try
			{
				jforumAttachmentService.processPrivateMessageAttachments(privateMessage, pmIds);
			}
			catch (JForumAttachmentOverQuotaException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(" Private Message - '" + privateMessage.getPost().getSubject()  + "' and with id's "+ pmIds.toString() +" attachment(s) are not saved. " + e.toString());
				}
				post.setHasAttachments(false);
			}
			catch(JForumAttachmentBadExtensionException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(" Private Message - '" + privateMessage.getPost().getSubject()  + "' and with id's "+ pmIds.toString() +" attachment(s) are not saved. " + e.toString());
				}
				post.setHasAttachments(false);
			}
		}
		
		// send email if user opted to receive private message email's
		sendPrivateMessageEmail(privateMessage);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendPrivateMessageWithAttachments(PrivateMessage privateMessage) throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		if (privateMessage == null)
		{
			throw new IllegalArgumentException();
		}
		
		if (privateMessage.getId() > 0)
		{
			throw new IllegalArgumentException("New private message cannot be created as private message having id");
		}
		
		if ((privateMessage.getFromUser() == null) || (privateMessage.getToUser() == null))
		{
			throw new IllegalArgumentException("From user and To user information is needed");
		}
		
		if ((privateMessage.getPost() == null))
		{
			throw new IllegalArgumentException("Private message post information is needed");
		}
		
		if (privateMessage.getContext() == null)
		{
			throw new IllegalArgumentException("Private message context is needed");
		}
		
		//access check from user
		boolean facilitator = jforumSecurityService.isJForumFacilitator(privateMessage.getContext(), privateMessage.getFromUser().getSakaiUserId());
		
		boolean participant = false;
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(privateMessage.getContext(), privateMessage.getFromUser().getSakaiUserId());
		}
		
		if (!(facilitator || participant))
		{
			throw new JForumAccessException(privateMessage.getFromUser().getSakaiUserId());
		}
		
		//access check to user
		facilitator = false;
		participant = false;
		
		facilitator = jforumSecurityService.isJForumFacilitator(privateMessage.getContext(), privateMessage.getToUser().getSakaiUserId());
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(privateMessage.getContext(), privateMessage.getToUser().getSakaiUserId());
		}
		
		if (!(facilitator || participant))
		{
			throw new JForumAccessException(privateMessage.getToUser().getSakaiUserId());
		}
		
		Post post = privateMessage.getPost();
		
		String cleanedPostText = HtmlHelper.clean(post.getText(), true);
		post.setText(cleanedPostText);
		
		//attachments
		if (post.hasAttachments())
		{
			jforumAttachmentService.validatePostAttachments(post);
		}
		
		sendPrivateMessage(privateMessage);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendPrivateMessageWithAttachments(PrivateMessage privateMessage, List<String> toSakaiUserIds) throws JForumAccessException, JForumAttachmentOverQuotaException, JForumAttachmentBadExtensionException
	{
		if (privateMessage == null)
		{
			throw new IllegalArgumentException();
		}
		
		if (privateMessage.getId() > 0)
		{
			throw new IllegalArgumentException("New private message cannot be created as private message having id");
		}
		
		if ((privateMessage.getFromUser() == null) || (toSakaiUserIds == null) || (toSakaiUserIds.size() == 0))
		{
			throw new IllegalArgumentException("From user and To users information is needed");
		}
		
		if ((privateMessage.getPost() == null))
		{
			throw new IllegalArgumentException("Private message post information is needed");
		}
		
		if (privateMessage.getContext() == null)
		{
			throw new IllegalArgumentException("Private message context is needed");
		}
		
		List<User> toUsers = new ArrayList<User>();
		
		List<Integer> pmIds = new ArrayList<Integer>();
		
		User toJforumUser = null;
		
		boolean facilitator = false;
		boolean participant = false;
		for (String toSakaiUserId : toSakaiUserIds)
		{
			if (toSakaiUserId == null)
			{
				continue;
			}
			toJforumUser = jforumUserService.getBySakaiUserId(toSakaiUserId);
			if (toJforumUser != null)
			{
				//access check To user
				facilitator = false;
				participant = false;
				
				facilitator = jforumSecurityService.isJForumFacilitator(privateMessage.getContext(), toSakaiUserId);
				if (!facilitator)
				{
					participant = jforumSecurityService.isJForumParticipant(privateMessage.getContext(), toSakaiUserId);
				}
				
				if (!(facilitator || participant))
				{
					// throw new JForumAccessException(toSakaiUserId);
					continue;
				}
				
				toUsers.add(toJforumUser);
			}
		}
		
		if (toUsers.size() == 0)
		{
			return;
		}
		
		//access check from user
		facilitator = jforumSecurityService.isJForumFacilitator(privateMessage.getContext(), privateMessage.getFromUser().getSakaiUserId());
		
		participant = false;
		if (!facilitator)
		{
			participant = jforumSecurityService.isJForumParticipant(privateMessage.getContext(), privateMessage.getFromUser().getSakaiUserId());
		}
		
		if (!(facilitator || participant))
		{
			throw new JForumAccessException(privateMessage.getFromUser().getSakaiUserId());
		}
		
		Post post = privateMessage.getPost();
		
		String cleanedPostText = HtmlHelper.clean(post.getText(), true);
		post.setText(cleanedPostText);
		
		//attachments
		if (post.hasAttachments())
		{
			jforumAttachmentService.validatePostAttachments(post);
		}
		
		for (User toUser : toUsers)
		{
			privateMessage.setToUser(toUser);
			
			// save the message
			List<Integer> createdPmIds = privateMessageDao.saveMessage(privateMessage);
			
			pmIds.addAll(createdPmIds);
		}
		
		// process attachments
		if (post.hasAttachments())
		{
			try
			{
				jforumAttachmentService.processPrivateMessageAttachments(privateMessage, pmIds);
			}
			catch (JForumAttachmentOverQuotaException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(" Private Message - Post '" + post.getSubject() + "' and with id "+ post.getId() +" attachment(s) are not saved. " + e.toString());
				}
			}
			catch(JForumAttachmentBadExtensionException e1)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(" Private Message - Post '" + post.getSubject() + "' and with id "+ post.getId() +" attachment(s) are not saved. " + e1.toString());
				}
			}
		}
		
		//send email if user opted to receive private message email's
		for (User toUser : toUsers)
		{
			privateMessage.setToUser(toUser);
			sendPrivateMessageEmail(privateMessage);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<PrivateMessage> sentbox(String context, String sakaiUserId)
	{
		StringBuilder sql = new StringBuilder();
		

		sql.append("SELECT pm.privmsgs_id, pm.privmsgs_type, pm.privmsgs_subject, pm.privmsgs_from_userid, pm.privmsgs_to_userid, pm.privmsgs_date, pm.privmsgs_ip, ");
		sql.append("pm.privmsgs_enable_bbcode, pm.privmsgs_enable_html, pm.privmsgs_enable_smilies, pm.privmsgs_attach_sig, pm.privmsgs_attachments, pm.privmsgs_flag_to_follow, ");
		sql.append("pm.privmsgs_replied, pm.privmsgs_priority, u2.user_id, u2.sakai_user_id, u2.username, u2.user_fname, u2.user_lname, u2.user_avatar ");
		sql.append("FROM jforum_privmsgs pm, jforum_users u1, jforum_users u2, jforum_sakai_course_privmsgs cp ");
		sql.append("WHERE u1.sakai_user_id = ? ");
		sql.append("AND u2.user_id = pm.privmsgs_to_userid ");
		sql.append("AND u1.user_id = pm.privmsgs_from_userid ");
		sql.append("AND pm.privmsgs_type = "+ PrivateMessage.TYPE_SENT +" ");
		sql.append("AND pm.privmsgs_id = cp.privmsgs_id AND cp.course_id = ? ");
		sql.append("ORDER BY pm.privmsgs_date DESC ");		
						
		int i = 0; 
		Object[] fields = new Object[2];
		fields[i++] = sakaiUserId;
		fields[i++] = context;
		
		final List<PrivateMessage> pmList = new ArrayList<PrivateMessage>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					PrivateMessage pm = getPm(result, false);
					
					UserImpl toUser = new UserImpl();
					toUser.setId(result.getInt("user_id"));
					toUser.setSakaiUserId(result.getString("sakai_user_id"));
					toUser.setFirstName(result.getString("user_fname")==null ? "" : result.getString("user_fname"));
					toUser.setLastName(result.getString("user_lname")==null ? "" : result.getString("user_lname"));
					toUser.setAvatar(result.getString("user_avatar"));
					pm.setToUser(toUser);
					
					pmList.add(pm);
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("sentbox: " + e, e);
					}					
				}
				return null;
			}
		});
		
		for (PrivateMessage pm : pmList)
		{
			PostUtil.preparePostForDisplay(pm.getPost());
		}
		
		return pmList;
	}
	
	/**
	 * @param attachmentDao the attachmentDao to set
	 */
	public void setAttachmentDao(AttachmentDao attachmentDao)
	{
		this.attachmentDao = attachmentDao;
	}
	
	/**
	 * @param jforumAttachmentService the jforumAttachmentService to set
	 */
	public void setJforumAttachmentService(JForumAttachmentService jforumAttachmentService)
	{
		this.jforumAttachmentService = jforumAttachmentService;
	}

	/**
	 * @param jforumEmailExecutorService the jforumEmailExecutorService to set
	 */
	public void setJforumEmailExecutorService(JForumEmailExecutorService jforumEmailExecutorService)
	{
		this.jforumEmailExecutorService = jforumEmailExecutorService;
	}
	
	/**
	 * @param jforumSecurityService the jforumSecurityService to set
	 */
	public void setJforumSecurityService(JForumSecurityService jforumSecurityService)
	{
		this.jforumSecurityService = jforumSecurityService;
	}

	/**
	 * @param jforumUserService the jforumUserService to set
	 */
	public void setJforumUserService(JForumUserService jforumUserService)
	{
		this.jforumUserService = jforumUserService;
	}
	
	/**
	 * @param privateMessageDao the privateMessageDao to set
	 */
	public void setPrivateMessageDao(PrivateMessageDao privateMessageDao)
	{
		this.privateMessageDao = privateMessageDao;
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
	 * 			The sqlService to set
	 */
	public void setSqlService(SqlService sqlService)
	{
		this.sqlService = sqlService;
	}
	
	/**
	 * Gets the private message from the result set
	 * 
	 * @param rs	The result set
	 * 
	 * @param full	With private message text or with out
	 * 
	 * @return	The private message from the result set
	 * 
	 * @throws SQLException
	 */
	protected PrivateMessage getPm(ResultSet rs, boolean full) throws SQLException
	{
		PrivateMessageImpl pm = new PrivateMessageImpl();
		PostImpl p = new PostImpl();

		pm.setId(rs.getInt("privmsgs_id"));
		pm.setType(rs.getInt("privmsgs_type"));
		p.setTime(rs.getTimestamp("privmsgs_date"));
		p.setSubject(rs.getString("privmsgs_subject"));
		p.setHasAttachments(Boolean.valueOf(rs.getInt("privmsgs_attachments") > 0));
		p.setBbCodeEnabled(rs.getInt("privmsgs_enable_bbcode") == 1);
		p.setSignatureEnabled(rs.getInt("privmsgs_attach_sig") == 1);
		//p.setHtmlEnabled(rs.getInt("privmsgs_enable_html") == 1);
		p.setSmiliesEnabled(rs.getInt("privmsgs_enable_smilies") == 1);
		
			
		if (full) 
		{
			UserImpl fromUser = new UserImpl();
			fromUser.setId(rs.getInt("privmsgs_from_userid"));
			fromUser.setSakaiUserId(rs.getString("from_user_sakai_id"));
			fromUser.setFirstName(rs.getString("from_user_first_name") == null ? "" : rs.getString("from_user_first_name"));
			fromUser.setLastName(rs.getString("from_user_last_name")==null ? "" : rs.getString("from_user_last_name"));
			fromUser.setAvatar(rs.getString("from_user_avatar"));
			
			pm.setFromUser(fromUser);
			
			UserImpl toUser = new UserImpl();
			toUser.setId(rs.getInt("privmsgs_to_userid"));
			toUser.setSakaiUserId(rs.getString("to_user_sakai_id"));
			toUser.setFirstName(rs.getString("to_user_first_name") == null ? "" : rs.getString("to_user_first_name"));
			toUser.setLastName(rs.getString("to_user_last_name")==null ? "" : rs.getString("to_user_last_name"));
			toUser.setAvatar(rs.getString("to_user_avatar"));
			
			pm.setToUser(toUser);
			p.setText(HtmlHelper.clean(this.getPmText(rs), true));
			p.setRawText(this.getPmText(rs));
		}
		
		pm.setFlagToFollowup(rs.getInt("privmsgs_flag_to_follow") > 0);
		pm.setReplied(rs.getInt("privmsgs_replied") > 0);
		pm.setPriority(rs.getInt("privmsgs_priority"));		
		
		pm.setPost((Post)p);

		return pm;
	}
	
	/**
	 * Gets the private message text
	 * 
	 * @param rs	The result set
	 * 
	 * @return	The private message text
	 * 
	 * @throws SQLException
	 */
	protected String getPmText(ResultSet rs) throws SQLException
	{
		return rs.getString("privmsgs_text");
	}
	
	/**
	 * Sends reply to existing private message
	 * 
	 * @param privateMessage	Private message
	 */
	protected void replyToPrivateMessage(PrivateMessage privateMessage)
	{
		if (privateMessage == null)
		{
			throw new IllegalArgumentException("Private message is null.");
		}
		
		if (privateMessage.getId() <= 0)
		{
			throw new IllegalArgumentException("Existing message id is needed.");
		}
		
		if ((privateMessage.getFromUser() == null) || (privateMessage.getToUser() == null))
		{
			throw new IllegalArgumentException("From user and To user information is needed");
		}
		
		if ((privateMessage.getPost() == null))
		{
			throw new IllegalArgumentException("Private message post information is needed");
		}
		
		if (privateMessage.getContext() == null)
		{
			throw new IllegalArgumentException("Private message context is needed");
		}
		
		privateMessage.setReplied(true);
		
		// save the message
		List<Integer> pmIds = privateMessageDao.saveMessage(privateMessage);
		
		// process attachments
		Post post = privateMessage.getPost();
		if (post.hasAttachments())
		{
			try
			{
				jforumAttachmentService.processPrivateMessageAttachments(privateMessage, pmIds);
			}
			catch (JForumAttachmentOverQuotaException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(" Private Message - '" + privateMessage.getPost().getSubject()  + "' and with id's "+ pmIds.toString() +" attachment(s) are not saved. " + e.toString());
				}
				post.setHasAttachments(false);
			}
			catch(JForumAttachmentBadExtensionException e)
			{
				if (logger.isWarnEnabled())
				{
					logger.warn(" Private Message - '" + privateMessage.getPost().getSubject()  + "' and with id's "+ pmIds.toString() +" attachment(s) are not saved. " + e.toString());
				}
				post.setHasAttachments(false);
			}
		}
		
		// send email if user opted to receive private message email's
		sendPrivateMessageEmail(privateMessage);
	}
	
	/**
	 * Sends private message
	 * 
	 * @param privateMessage	Private message
	 * 
	 * @param toUser	To user
	 */
	protected void sendPrivateMessageEmail(PrivateMessage privateMessage)
	{
		if ((privateMessage == null) || (privateMessage.getToUser() == null))
		{
			return;
		}
		
		// send email if to users opted to receive private message email's or high priority private messages
		if (!privateMessage.getToUser().isNotifyPrivateMessagesEnabled())
		{
			if ((privateMessage.getPriority() != PrivateMessage.PRIORITY_HIGH))
			{
				return;
			}
		}
		
		if ((privateMessage.getToUser().getEmail() == null) || (privateMessage.getToUser().getEmail().trim().length() == 0))
		{
			return;
		}
		
		InternetAddress from = null;
		InternetAddress[] to = null;
		
		try
		{
			InternetAddress toUserEmail = new InternetAddress(privateMessage.getToUser().getEmail());
			
			to = new InternetAddress[1];
			to[0] = toUserEmail;
			
			String fromUserEmail = "\"" + ServerConfigurationService.getString("ui.service", "Sakai") + "\"<no-reply@" + ServerConfigurationService.getServerName() + ">";
			from = new InternetAddress(fromUserEmail);
		} 
		catch (AddressException e)
		{
			if (logger.isWarnEnabled()) logger.warn("sendPrivateMessageEmail(): Private message email notification error : "+ e);
			return;
		}
		String subject = privateMessage.getPost().getSubject();
		String postText = privateMessage.getPost().getText();
		
		// full URL's for smilies etc
		if (postText != null && postText.trim().length() > 0)
		{
			postText = XrefHelper.fullUrls(postText);
		}
		
		// format message text
		Map<String, String> params = new HashMap<String, String>();
		
		StringBuilder toUserName = new StringBuilder();
		toUserName.append(privateMessage.getToUser().getFirstName() != null ? privateMessage.getToUser().getFirstName() : "");
		toUserName.append(" ");
		toUserName.append(privateMessage.getToUser().getLastName() != null ? privateMessage.getToUser().getLastName() : "");
		params.put("pm.to", toUserName.toString());
		
		StringBuilder fromUserName = new StringBuilder();
		fromUserName.append(privateMessage.getFromUser().getFirstName() != null ? privateMessage.getFromUser().getFirstName() : "");
		fromUserName.append(" ");
		fromUserName.append(privateMessage.getFromUser().getLastName() != null ? privateMessage.getFromUser().getLastName() : "");
		params.put("pm.from", fromUserName.toString());
		
		params.put("pm.subject", subject);
		params.put("pm.text", postText);
		
		StringBuilder emailSubject = new StringBuilder();
		try
		{
			Site site = siteService.getSite(privateMessage.getContext());
			
			if (!site.isPublished())
			{
				return;
			}
			
			params.put("site.title", site.getTitle());
			
			emailSubject.append("[");
			emailSubject.append(site.getTitle());
			emailSubject.append(" - ");
			emailSubject.append(PrivateMessage.MAIL_NEW_PM_SUBJECT);
			emailSubject.append("] ");
			emailSubject.append(subject);
			
			
			String portalUrl = ServerConfigurationService.getPortalUrl();
			params.put("portal.url", portalUrl);
			
			//String currToolId = ToolManager.getCurrentPlacement().getId();
			//ToolConfiguration toolConfiguration = site.getTool(currToolId);
			
			ToolConfiguration toolConfiguration = site.getToolForCommonId("sakai.jforum.tool");
			
			String siteNavUrl = portalUrl + "/"+ "site" + "/"+ Web.escapeUrl(site.getId());
			
			if (toolConfiguration != null)
				siteNavUrl = siteNavUrl + "/" + "page" + "/"+ toolConfiguration.getPageId();
			
			params.put("site.url", siteNavUrl);
		}
		catch (IdUnusedException e)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn(e.toString(), e);
			}
		}
		
		String messageText = EmailUtil.getPrivateMessageText(params);
		
		if (messageText != null)
		{
			Post post = privateMessage.getPost();
			if ((post !=null) && (post.hasAttachments()))
			{
				//email with attachments
				jforumEmailExecutorService.notifyUsers(from, to, emailSubject.toString(), messageText, post.getAttachments());
			}
			else
			{
				jforumEmailExecutorService.notifyUsers(from, to, emailSubject.toString(), messageText);
			}
		}
		else
		{
			Post post = privateMessage.getPost();
			if ((post !=null) && (post.hasAttachments()))
			{
				//email with attachments
				jforumEmailExecutorService.notifyUsers(from, to, emailSubject.toString(), postText, post.getAttachments());
			}
			else
			{
				jforumEmailExecutorService.notifyUsers(from, to, emailSubject.toString(), postText);
			}
		}
	}
}
