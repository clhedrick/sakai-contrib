/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/generic/PrivateMessageDaoGeneric.java $ 
 * $Id: PrivateMessageDaoGeneric.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011, 2012, 2013 Etudes, Inc. 
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
package org.etudes.component.app.jforum.dao.generic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Post;
import org.etudes.api.app.jforum.PrivateMessage;
import org.etudes.api.app.jforum.User;
import org.etudes.api.app.jforum.dao.AttachmentDao;
import org.etudes.api.app.jforum.dao.PrivateMessageDao;
import org.etudes.api.app.jforum.dao.UserDao;
import org.etudes.component.app.jforum.PostImpl;
import org.etudes.component.app.jforum.PrivateMessageImpl;
import org.etudes.component.app.jforum.UserImpl;
import org.etudes.util.HtmlHelper;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;

public abstract class PrivateMessageDaoGeneric implements PrivateMessageDao
{
	private static Log logger = LogFactory.getLog(PrivateMessageDaoGeneric.class);
	
	/** Dependency: AttachmentDao */
	protected AttachmentDao attachmentDao = null;
	
	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	/** Dependency: UserDao */
	protected UserDao userDao = null;
	
	/**
	 * {@inheritDoc}
	 */
	public void delete(PrivateMessage privateMessage)
	{
		if (privateMessage == null || privateMessage.getId() == 0 || privateMessage.getFromUser() == null || privateMessage.getToUser() == null)
		{
			return;
		}
		deleteTx(privateMessage);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Integer> saveMessage(PrivateMessage pm)
	{
		if (pm == null)
		{
			throw new IllegalArgumentException();
		}
		
		if ((pm.getFromUser() == null) || (pm.getToUser() == null))
		{
			throw new IllegalArgumentException("From user and To user information is needed");
		}
		
		if ((pm.getPost() == null))
		{
			throw new IllegalArgumentException("Private message post information is needed");
		}
		
		if (pm.getPost().getSubject() != null && pm.getPost().getSubject().length() > 100)
		{
			pm.getPost().setSubject(pm.getPost().getSubject().substring(0, 99));
		}
		
		return saveMessageTx(pm);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public PrivateMessage selectById(int privateMessageId)
	{

		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT p.privmsgs_id, p.privmsgs_type, p.privmsgs_subject, p.privmsgs_from_userid, ");
		sql.append("p.privmsgs_to_userid, p.privmsgs_date, p.privmsgs_ip, p.privmsgs_enable_bbcode, ");
		sql.append("p.privmsgs_enable_html, p.privmsgs_enable_smilies, p.privmsgs_attach_sig, ");
		sql.append("p.privmsgs_attachments, p.privmsgs_flag_to_follow, p.privmsgs_replied, ");
		sql.append("p.privmsgs_priority, pt.privmsgs_text, pc.course_id, ");
		sql.append("u1.sakai_user_id AS from_user_sakai_id, u1.user_fname AS from_user_first_name, ");
		sql.append("u1.user_lname AS from_user_last_name, u1.user_avatar AS from_user_avatar, ");
		sql.append("u1.user_attachsig AS from_user_attachsig, u1.user_sig AS from_user_sig, ");
		sql.append("u2.sakai_user_id AS to_user_sakai_id, u2.user_fname AS to_user_first_name, ");
		sql.append("u2.user_lname AS to_user_last_name, u2.user_avatar AS to_user_avatar, ");
		sql.append("u2.user_attachsig AS to_user_attachsig, u2.user_sig AS to_user_sig ");
		sql.append("FROM jforum_privmsgs p, jforum_privmsgs_text pt, jforum_sakai_course_privmsgs pc, jforum_users u1, jforum_users u2 ");
		sql.append("WHERE p.privmsgs_id = pt.privmsgs_id ");
		sql.append("AND p.privmsgs_id = ? ");
		sql.append("AND p.privmsgs_id = pc.privmsgs_id ");
		sql.append("AND p.privmsgs_from_userid = u1.user_id ");
		sql.append("AND p.privmsgs_to_userid = u2.user_id ");
		
		int i = 0; 
		Object[] fields = new Object[1];
		fields[i++] = privateMessageId;
		
		final List<PrivateMessage> pmList = new ArrayList<PrivateMessage>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					PrivateMessage pm = getPm(result, true);
					pm.setContext(result.getString("course_id"));
					
					pmList.add(pm);
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectById: " + e, e);
					}					
				}
				return null;
			}
		});
		
		PrivateMessage privateMessage = null;
		
		if (pmList.size() == 1)
		{
			privateMessage = pmList.get(0);
			//PostUtil.preparePostForDisplay(privateMessage.getPost());
			
			// get PM attachments
			if (privateMessage.getPost().hasAttachments())
			{
				privateMessage.getPost().setAttachments(attachmentDao.selectPrivateMessageAttachments(privateMessage.getId()));
			}
		}
		return privateMessage;
	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int selectUnreadCount(String context, int userId)
	{
		if (context == null || context.trim().length() == 0 || userId <= 0)
		{
			return 0;
		}
		
		final List<Integer> count = new ArrayList<Integer>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT count(*) AS total FROM jforum_privmsgs p, jforum_sakai_course_privmsgs cp ");
		sql.append("WHERE p.privmsgs_to_userid = ? ");
		sql.append("AND p.privmsgs_type = ? ");
		sql.append("AND p.privmsgs_id = cp.privmsgs_id ");
		sql.append("AND cp.course_id = ?");						
				
		Object[] fields;
		int i = 0;
		
		fields = new Object[3];
		fields[i++] = userId;
		fields[i++] = PrivateMessage.PrivateMessageType.NEW.getType();
		fields[i++] = context;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					count.add(result.getInt("total"));
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectUnreadCount: " + e, e);
					}
					return null;
				}
			}
		});
		
		int unreadPrivateMessagesCount = 0;
		if (count.size() == 1)
		{
			unreadPrivateMessagesCount = count.get(0);
		}
		
		return unreadPrivateMessagesCount;	
		
	}
	
	/**
	 * @param attachmentDao the attachmentDao to set
	 */
	public void setAttachmentDao(AttachmentDao attachmentDao)
	{
		this.attachmentDao = attachmentDao;
	}
	
	/**
	 * @param sqlService
	 *          The sqlService to set
	 */
	public void setSqlService(SqlService sqlService)
	{
		this.sqlService = sqlService;
	}
	
	/**
	 * @param userDao the userDao to set
	 */
	public void setUserDao(UserDao userDao)
	{
		this.userDao = userDao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateFlagToFollowup(int messageId, boolean flag)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE jforum_privmsgs SET privmsgs_flag_to_follow = ? WHERE privmsgs_id = ?");
		
		int i = 0;
		Object[] fields = new Object[2];
		fields[i++] = flag ?  1 : 0;
		fields[i++] = messageId;
		
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("updateFlagToFollowup: db write failed");
		}
	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateMessageType(int messageId, int messageType)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE jforum_privmsgs SET privmsgs_type = ? WHERE privmsgs_id = ?");
		
		int i = 0;
		Object[] fields = new Object[2];
		fields[i++] = messageType;
		fields[i++] = messageId;
		
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("updateMessageType: db write failed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateRepliedStatus(PrivateMessage pm)
	{
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE jforum_privmsgs SET privmsgs_replied = ? WHERE privmsgs_id = ?");
		
		int i = 0;
		
		Object[] fields = new Object[2];
		fields[i++] = pm.isReplied() ?  1 : 0;
		fields[i++] = pm.getId();
		
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("updateRepliedStatus: db write failed");
		}
	}
	
	/**
	 * add private message to course
	 * 
	 * @param context	Course id
	 * 
	 * @param pmId	Private message id
	 */
	protected void addPrivateMessageToCourse(String context, int pmId)
	{
		// add to jforum_privmsgs_text table
		String sql = "INSERT INTO jforum_sakai_course_privmsgs (course_id, privmsgs_id) VALUES (?, ?)";
		
		Object[] fields = new Object[2];
		int i = 0;
		fields[i++] = context;
		fields[i++] = pmId;
		
		if (!sqlService.dbWrite(sql, fields)) 
		{
			throw new RuntimeException("addPrivateMessageToCourse: db write failed");
		}
	}
	
	protected void deletePrivateMessage(PrivateMessage privateMessage)
	{
		//READ(0), NEW(1), SENT(2), SAVED_IN(3), SAVED_OUT(4), UNREAD(5);
		
		// delete from jforum_privmsgs table
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM jforum_privmsgs WHERE privmsgs_id = ? AND ");
		sql.append("( (privmsgs_from_userid = ? AND privmsgs_type = " +	PrivateMessage.PrivateMessageType.SENT.getType() +") OR ");
		sql.append("(privmsgs_to_userid = ? AND privmsgs_type ");
		sql.append("IN(");
		sql.append(PrivateMessage.PrivateMessageType.READ.getType());
		sql.append(", ");
		sql.append(PrivateMessage.PrivateMessageType.NEW.getType());
		sql.append(", ");
		sql.append(PrivateMessage.PrivateMessageType.UNREAD.getType());
		sql.append(")) )");
		
		Object[] fields = new Object[3];
		int i = 0;
		fields[i++] = privateMessage.getId();
		fields[i++] = privateMessage.getFromUser().getId();
		fields[i++] = privateMessage.getToUser().getId();		
		
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("deletePrivateMessage: db write failed");
		}
		
	}
		
	protected void deletePrivateMessageAttachment(PrivateMessage privateMessage)
	{
		String sql = "DELETE FROM jforum_privmsgs_attach WHERE privmsgs_id = ?";
		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] = privateMessage.getId();
		
		if (!sqlService.dbWrite(sql, fields)) 
		{
			throw new RuntimeException("deletePrivateMessageAttachment: db write failed");
		}
	}
	
	protected void deletePrivateMessageFromCourse(PrivateMessage privateMessage)
	{
		String sql = "DELETE FROM jforum_sakai_course_privmsgs WHERE privmsgs_id = ?";
		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] = privateMessage.getId();
		
		if (!sqlService.dbWrite(sql, fields)) 
		{
			throw new RuntimeException("deletePrivateMessageFromCourse: db write failed");
		}
	}
	
	protected void deletePrivateMessageText(PrivateMessage privateMessage)
	{
		String sql = "DELETE FROM jforum_privmsgs_text WHERE privmsgs_id = ?";
		Object[] fields = new Object[1];
		int i = 0;
		fields[i++] = privateMessage.getId();
		
		if (!sqlService.dbWrite(sql.toString(), fields)) 
		{
			throw new RuntimeException("deletePrivateMessageText: db write failed");
		}
	}
	
	/**
	 * Deletes private message 
	 * 
	 * @param privateMessage	Private message
	 */
	protected void deleteTx(final PrivateMessage privateMessage)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				deletePrivateMessage(privateMessage);
				deletePrivateMessageText(privateMessage);
				deletePrivateMessageAttachment(privateMessage);
				deletePrivateMessageFromCourse(privateMessage);				
			}
		}, "deleteTx: " + privateMessage.getId());
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
		p.setHtmlEnabled(rs.getInt("privmsgs_enable_html") == 1);
		p.setSmiliesEnabled(rs.getInt("privmsgs_enable_smilies") == 1);
		
			
		if (full) 
		{
			/*UserImpl fromUser = new UserImpl();
			fromUser.setId(rs.getInt("privmsgs_from_userid"));
			fromUser.setSakaiUserId(rs.getString("from_user_sakai_id"));
			fromUser.setFirstName(rs.getString("from_user_first_name") == null ? "" : rs.getString("from_user_first_name"));
			fromUser.setLastName(rs.getString("from_user_last_name")==null ? "" : rs.getString("from_user_last_name"));
			fromUser.setAvatar(rs.getString("from_user_avatar"));
			fromUser.setAttachSignatureEnabled(rs.getInt("from_user_attachsig") == 1);
			fromUser.setSignature(rs.getString("from_user_sig"));*/
			
			User fromUser = userDao.selectByUserId(rs.getInt("privmsgs_from_userid"));
						
			pm.setFromUser(fromUser);
			
			/*UserImpl toUser = new UserImpl();
			toUser.setId(rs.getInt("privmsgs_to_userid"));
			toUser.setSakaiUserId(rs.getString("to_user_sakai_id"));
			toUser.setFirstName(rs.getString("to_user_first_name") == null ? "" : rs.getString("to_user_first_name"));
			toUser.setLastName(rs.getString("to_user_last_name")==null ? "" : rs.getString("to_user_last_name"));
			toUser.setAvatar(rs.getString("to_user_avatar"));
			toUser.setAttachSignatureEnabled(rs.getInt("to_user_attachsig") == 1);
			toUser.setSignature(rs.getString("to_user_sig"));*/
			
			User toUser = userDao.selectByUserId(rs.getInt("privmsgs_to_userid"));
					
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
	abstract protected String getPmText(ResultSet rs) throws SQLException;
	
	/**
	 * Insert the message
	 * 
	 * @param pm	Private message
	 * 
	 * @return	Newly created private message id
	 */
	protected abstract int insertPrivateMessage(PrivateMessage pm);
	
	/**
	 * Insert private message text
	 * 
	 * @param id	Private message id
	 * 
	 * @param messageText	Message text
	 */
	protected abstract void insertPrivateMessageText(int pmId, String messageText);
	
	/**
	 * Save the message in from user sent box
	 * 
	 * @param pm	Private message
	 * 
	 * @return newly created private message id
	 */
	protected int saveInFromUserSentbox(PrivateMessage pm)
	{
		pm.setType(PrivateMessage.PrivateMessageType.SENT.getType());
		
		// message
		int pmId = insertPrivateMessage(pm);
		
		// add private message to course
		addPrivateMessageToCourse(pm.getContext(), pmId);
		
		// message text
		insertPrivateMessageText(pmId, pm.getPost().getText());
		
		return pmId;
	}
	
	/**
	 * Save the private message in to user inbox
	 * 
	 * @param pm	Private message

	 * @return newly created private message id
	 */
	protected int saveInToUserInbox(PrivateMessage pm)
	{
		pm.setType(PrivateMessage.PrivateMessageType.NEW.getType());
		
		// message
		int pmId = insertPrivateMessage(pm);
		
		// add private message to course
		addPrivateMessageToCourse(pm.getContext(), pmId);
		
		// message text
		insertPrivateMessageText(pmId, pm.getPost().getText());
		
		return pmId;
	}
	
	/**
	 * Save private message in from user sent box and to user inbox and update replied status if replying to an existing private message
	 * 
	 * @param pm	Private Message
	 *
	 * @return newly created private message id's of "from user sent box" and "to user inbox"
	 */
	protected List<Integer> saveMessageTx(final PrivateMessage pm)
	{
		final List<Integer> pmIds = new ArrayList<Integer>();
		
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					int fromUserPMId = saveInFromUserSentbox(pm);
					pmIds.add(Integer.valueOf(fromUserPMId));
					
					int toUserPMId = saveInToUserInbox(pm);
					pmIds.add(Integer.valueOf(toUserPMId));
					
					// update replied status of the existing private message
					if ((pm.getId() > 0) && (pm.isReplied()))
					{
						updateRepliedStatus(pm);
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}

					throw new RuntimeException("Error while creating new private message.", e);
				}
			}
		}, "saveMessageTx: " + pm.getPost().getSubject());
		
		return pmIds;
	}
}
