/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/generic/AttachmentDaoGeneric.java $ 
 * $Id: AttachmentDaoGeneric.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum.dao.generic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Attachment;
import org.etudes.api.app.jforum.AttachmentExtension;
import org.etudes.api.app.jforum.dao.AttachmentDao;
import org.etudes.component.app.jforum.AttachmentExtensionImpl;
import org.etudes.component.app.jforum.AttachmentImpl;
import org.etudes.component.app.jforum.AttachmentInfoImpl;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;

public abstract class AttachmentDaoGeneric implements AttachmentDao
{

	private static Log logger = LogFactory.getLog(AttachmentDaoGeneric.class);
	
	/** Dependency: SqlService */
	protected SqlService sqlService = null;
	
	/**
	 * {@inheritDoc}
	 */
	public void addPostAttachment(final Attachment attachment)
	{
		if ((attachment.getPostId() ==  0) || (attachment.getUserId() == 0) || (attachment.getPrivmsgsId() > 0))
		{
			return;
		}
		
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					insertAttachment(attachment);
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while adding attachment.", e);
				}
			}
		}, "addAttachment: post id:" + attachment.getPostId() +" :Private message id:"+  attachment.getPrivmsgsId());
			
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addPrivateMessageAttachment(final Attachment attachment, final List<Integer> pmIds)
	{
		if ((attachment.getPostId() >  0) || (attachment.getUserId() == 0) || (attachment.getPrivmsgsId() > 0))
		{
			return;
		}
		
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				try
				{
					int attachId  = insertAttachment(attachment);
					
					/* if attachment is for private message insert data in jforum_privmsgs_attach(attach_id, privmsgs_id) and
				 	increase private message attachment count(table is jforum_privmsgs and column is privmsgs_attachments) */
					// insert data in to jforum_privmsgs_attach
					StringBuilder sql = new StringBuilder();
					sql.append("INSERT INTO jforum_privmsgs_attach ( attach_id, privmsgs_id ) VALUES (?, ?)");
					
					Object[] fields = null;
					int i = 0;
					for (Integer pmId : pmIds)
					{
						fields = new Object[2];
						i = 0;
						
						fields[i++] = attachId;
						fields[i++] = pmId;
						
						if (!sqlService.dbWrite(sql.toString(), fields)) 
						{
							throw new RuntimeException("insertAttachment: inserting private message attach failed");
						}
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while adding attachment.", e);
				}
				
				try
				{
					StringBuilder sql = new StringBuilder();
					
					sql.append("UPDATE jforum_privmsgs SET privmsgs_attachments = privmsgs_attachments + ? WHERE privmsgs_id = ?");
					
					Object[] fields = null;
					int i = 0;
					for (Integer pmId : pmIds)
					{
						fields = new Object[2];
						i = 0;
						
						fields[i++] = 1;
						fields[i++] = pmId;
						
						if (!sqlService.dbWrite(null, sql.toString(), fields))
						{
							throw new RuntimeException("increment privmsgs attachments count: db write failed");
						}
					}
				}
				catch (Exception e)
				{
					if (logger.isErrorEnabled())
					{
						logger.error(e.toString(), e);
					}
					
					throw new RuntimeException("Error while adding attachment.", e);
				}
			}
		}, "addAttachment: pmIds:" + pmIds.toString());
			
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deletePostAttachment(int postId, int attachmentId)
	{
		deletePostAttachmentTx(postId, attachmentId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deletePrivateMessageAttachment(int attachmentId)
	{
		deletePrivateMessageAttachmentTx(attachmentId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Attachment selectAttachmentById(int attachmentId)
	{

		if (attachmentId < 0)
		{
			return null;
		}
					
		if (attachmentId == 0) throw new IllegalArgumentException();
		
		final Map<Integer, AttachmentExtension> attachmentsExtensions = selectAttachmentExtensions();
		
		StringBuilder sql = new StringBuilder();
		Object[] fields;
		
		final List<Attachment> attachments = new ArrayList<Attachment>();
		
		sql.append("SELECT a.attach_id, a.user_id, a.post_id, a.privmsgs_id, d.mimetype, d.physical_filename, d.real_filename, ");
		sql.append("d.download_count, d.description, d.filesize, d.upload_time, d.extension_id ");
		sql.append("FROM jforum_attach a, jforum_attach_desc d ");
		sql.append("WHERE a.attach_id = ? ");
		sql.append("AND a.attach_id = d.attach_id");
		
		int i = 0;
		fields = new Object[1];
		fields[i++] = attachmentId;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					AttachmentImpl attachment = new AttachmentImpl();
					
					attachment.setId(result.getInt("attach_id"));
					attachment.setUserId(result.getInt("user_id"));
					attachment.setPostId(result.getInt("post_id"));
					attachment.setPrivmsgsId(result.getInt("privmsgs_id"));
					
					AttachmentInfoImpl attachmentInfoImpl = new AttachmentInfoImpl();
					attachmentInfoImpl.setComment(result.getString("description"));
					attachmentInfoImpl.setDownloadCount(result.getInt("download_count"));
					attachmentInfoImpl.setFilesize(result.getLong("filesize"));
					attachmentInfoImpl.setMimetype(result.getString("mimetype"));
					attachmentInfoImpl.setPhysicalFilename(result.getString("physical_filename"));
					attachmentInfoImpl.setRealFilename(result.getString("real_filename"));
					attachmentInfoImpl.setUploadTime(result.getTimestamp("upload_time"));
					
					AttachmentExtension attachmentExtension = attachmentsExtensions.get(result.getInt("extension_id"));
					
					if (attachmentExtension != null)
					{
						attachmentInfoImpl.setExtension(attachmentExtension);
					}
					else
					{
						attachmentInfoImpl.setExtension(null);
					}
					
					attachment.setInfo(attachmentInfoImpl);
					
					attachments.add(attachment);
									
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectAttachmentById: " + e, e);
					}
					return null;
				}
			}
		});
		
		Attachment attachment = null;
	
		if (attachments.size() == 1)
		{
			attachment = attachments.get(0);
		}
		
		return attachment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<Integer, AttachmentExtension> selectAttachmentExtensions()
	{
		final Map<Integer, AttachmentExtension>  attachmentExtensions = new HashMap<Integer, AttachmentExtension>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT e.extension_id, e.extension_group_id, e.extension, e.description, e.upload_icon, e.allow, g.allow AS group_allow, g.upload_icon AS group_icon, g.download_mode ");
		sql.append("FROM jforum_extensions e, jforum_extension_groups g ");
		sql.append("WHERE e.extension_group_id = g.extension_group_id");
		
		Object[] fields = null;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					AttachmentExtensionImpl attachmentExtensionImpl = new AttachmentExtensionImpl();
					
					attachmentExtensionImpl.setId(result.getInt("extension_id"));
					attachmentExtensionImpl.setExtensionGroupId(result.getInt("extension_group_id"));
					attachmentExtensionImpl.setExtension(result.getString("extension"));
					attachmentExtensionImpl.setComment(result.getString("description"));
					
					String icon = result.getString("upload_icon");
					if (icon == null || icon.equals("")) {
						icon = result.getString("group_icon");
					}
					
					attachmentExtensionImpl.setUploadIcon(icon);
					
					int allow = result.getInt("group_allow");
					if (allow == 1) {
						allow = result.getInt("allow");
					}
					attachmentExtensionImpl.setAllow(allow == 1);
					
					attachmentExtensionImpl.setPhysicalDownloadMode(result.getInt("download_mode") == 2);
										
					attachmentExtensions.put(attachmentExtensionImpl.getId(), attachmentExtensionImpl);
					
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectAttachmentExtensions: " + e, e);
					}
					return null;
				}
			}
		});
		
		return attachmentExtensions;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int selectAttachmentPrivateMessagesCount(int attachId)
	{
		String sql = "SELECT count(1) AS total FROM jforum_privmsgs_attach p WHERE p.attach_id = ?";
		
		int i = 0; 
		Object[] fields = new Object[1];
		fields[i++] = attachId;
		
		final List<Integer> attachmentCountList = new ArrayList<Integer>();
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					
					attachmentCountList.add(result.getInt("total"));
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectAttachmentPrivateMessagesCount: " + e, e);
					}					
				}
				return null;
			}
		});
		
		int attachmentCount = 0;
		if (attachmentCountList.size() == 1)
		{
			attachmentCount = attachmentCountList.get(0).intValue();
		}
		
		return attachmentCount;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public List<Attachment> selectPostAttachments(int postId)
	{
		//TODO: add attachment extensions to cache and get from the cache
		final Map<Integer, AttachmentExtension> attachmentsExtensions = selectAttachmentExtensions();
		
		final List<Attachment> postAttachments = new ArrayList<Attachment>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT a.attach_id, a.user_id, a.post_id, a.privmsgs_id, d.mimetype, d.physical_filename, d.real_filename, ");
		sql.append("d.download_count, d.description, d.filesize, d.upload_time, d.extension_id ");
		sql.append("FROM jforum_attach a, jforum_attach_desc d, jforum_posts p ");
		sql.append("WHERE p.post_id = ? ");
		sql.append("AND a.post_id = p.post_id ");
		sql.append("AND a.attach_id = d.attach_id");
				
		int i = 0;
		
		Object[] fields = new Object[1];
		fields[i++] = postId;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					AttachmentImpl attachment = new AttachmentImpl();
					
					attachment.setId(result.getInt("attach_id"));
					attachment.setUserId(result.getInt("user_id"));
					attachment.setPostId(result.getInt("post_id"));
					attachment.setPrivmsgsId(result.getInt("privmsgs_id"));
					
					AttachmentInfoImpl attachmentInfoImpl = new AttachmentInfoImpl();
					attachmentInfoImpl.setComment(result.getString("description"));
					attachmentInfoImpl.setDownloadCount(result.getInt("download_count"));
					attachmentInfoImpl.setFilesize(result.getLong("filesize"));
					attachmentInfoImpl.setMimetype(result.getString("mimetype"));
					attachmentInfoImpl.setPhysicalFilename(result.getString("physical_filename"));
					attachmentInfoImpl.setRealFilename(result.getString("real_filename"));
					attachmentInfoImpl.setUploadTime(result.getTimestamp("upload_time"));
					
					AttachmentExtension attachmentExtension = attachmentsExtensions.get(result.getInt("extension_id"));
					
					if (attachmentExtension != null)
					{
						attachmentInfoImpl.setExtension(attachmentExtension);
					}
					else
					{
						attachmentInfoImpl.setExtension(null);
					}
					
					attachment.setInfo(attachmentInfoImpl);
					
					postAttachments.add(attachment);
		
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectPostAttachments: " + e, e);
					}
					return null;
				}
			}
		});
		
		return postAttachments;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Attachment> selectPrivateMessageAttachments(int privateMessageId)
	{
		final Map<Integer, AttachmentExtension> attachmentsExtensions = selectAttachmentExtensions();
		
		final List<Attachment> attachments = new ArrayList<Attachment>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT a.attach_id, a.user_id, a.post_id, a.privmsgs_id, d.mimetype, d.physical_filename, d.real_filename, ");
		sql.append("d.download_count, d.description, d.filesize, d.upload_time, d.extension_id ");
		sql.append("FROM jforum_attach a, jforum_attach_desc d, jforum_privmsgs_attach p ");
		sql.append("WHERE a.privmsgs_id = 0 AND a.post_id = 0 ");
		sql.append("AND a.attach_id = d.attach_id ");
		sql.append("AND a.attach_id = p.attach_id ");
		sql.append("AND p.privmsgs_id = ?");
				
		int i = 0;
		
		Object[] fields = new Object[1];
		fields[i++] = privateMessageId;
		
		this.sqlService.dbRead(sql.toString(), fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					AttachmentImpl attachment = new AttachmentImpl();
					
					attachment.setId(result.getInt("attach_id"));
					attachment.setPostId(result.getInt("post_id"));
					attachment.setPrivmsgsId(result.getInt("privmsgs_id"));
					
					
					AttachmentInfoImpl attachmentInfoImpl = new AttachmentInfoImpl();
					attachmentInfoImpl.setComment(result.getString("description"));
					attachmentInfoImpl.setDownloadCount(result.getInt("download_count"));
					attachmentInfoImpl.setFilesize(result.getLong("filesize"));
					attachmentInfoImpl.setMimetype(result.getString("mimetype"));
					attachmentInfoImpl.setPhysicalFilename(result.getString("physical_filename"));
					attachmentInfoImpl.setRealFilename(result.getString("real_filename"));
					attachmentInfoImpl.setUploadTime(result.getTimestamp("upload_time"));
										
					AttachmentExtension attachmentExtension = attachmentsExtensions.get(result.getInt("extension_id"));
					
					if (attachmentExtension != null)
					{
						attachmentInfoImpl.setExtension(attachmentExtension);
					}
					else
					{
						attachmentInfoImpl.setExtension(null);
					}
					
					attachment.setInfo(attachmentInfoImpl);
					
					attachments.add(attachment);
									
					return null;
				}
				catch (SQLException e)
				{
					if (logger.isWarnEnabled())
					{
						logger.warn("selectPrivateMessageAttachments: " + e, e);
					}
					return null;
				}
			}
		});
		
		return attachments;
	}
	
	/**
	 * @param sqlService the sqlService to set
	 */
	public void setSqlService(SqlService sqlService)
	{
		this.sqlService = sqlService;
	}
	
	/**
	 * Deletes post attachments and updates post attachments count
	 * 
	 * @param postId	Post id
	 * 
	 * @param attachmentId	Attachment id
	 */
	protected void deletePostAttachmentTx(final int postId, final int attachmentId)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{
				//delete attachment description and attachment
				String sql = "DELETE FROM jforum_attach_desc WHERE attach_id = ?";
				Object[] fields = new Object[1];
				int i = 0;
				fields[i++] = attachmentId;
				
				if (!sqlService.dbWrite(sql, fields)) 
				{
					throw new RuntimeException("deletePostAttachmentTx:deleteAttachmentDescription: db write failed");
				}
				
				sql = "DELETE FROM jforum_attach WHERE attach_id = ?";
								
				if (!sqlService.dbWrite(sql, fields)) 
				{
					throw new RuntimeException("deletePostAttachmentTx:deleteAttachment: db write failed");
				}
				
				//update post attach count
				sql = "SELECT COUNT(1) FROM jforum_attach WHERE post_id = ?";
				final List<Integer> postAttachCountList = new ArrayList<Integer>();
				fields = new Object[1];
				i = 0;
				fields[i++] = postId;
				
				sqlService.dbRead(sql.toString(), fields, new SqlReader()
				{
					public Object readSqlResultRecord(ResultSet result)
					{
						try
						{
							postAttachCountList.add(result.getInt(1));
							return null;
						}
						catch (SQLException e)
						{
							if (logger.isWarnEnabled())
							{
								logger.warn("deletePostAttachmentTx:selectPostAttachCount: " + e, e);
							}
							return null;
						}
					}
				});
				
				int postAttachCount = 0;
				
				if (postAttachCountList.size() == 1)
				{
					postAttachCount = postAttachCountList.get(0);
				}
				
				sql = "UPDATE jforum_posts SET attach = ? WHERE post_id = ?";
				fields = new Object[2];
				i = 0;
				fields[i++] = postAttachCount;
				fields[i++] = postId;
				
				if (!sqlService.dbWrite(sql, fields)) 
				{
					throw new RuntimeException("deletePostAttachmentTx:updatePostAttachCount: db write failed");
				}
						
			}
		}, "deletePostAttachmentTx: " + attachmentId);
	}
	
	
	/**
	 * Deletes private message attachment
	 * 
	 * @param attachmentId	Attachment id
	 */
	protected void deletePrivateMessageAttachmentTx(final int attachmentId)
	{
		this.sqlService.transact(new Runnable()
		{
			public void run()
			{

				String sql = "DELETE FROM jforum_attach_desc WHERE attach_id = ?";
				Object[] fields = new Object[1];
				int i = 0;
				fields[i++] = attachmentId;
				
				if (!sqlService.dbWrite(sql, fields)) 
				{
					throw new RuntimeException("deleteTx:deleteAttachmentDescription: db write failed");
				}
				
				sql = "DELETE FROM jforum_attach WHERE attach_id = ?";
								
				if (!sqlService.dbWrite(sql, fields)) 
				{
					throw new RuntimeException("deleteTx:deleteAttachment: db write failed");
				}
						
			}
		}, "deletePrivateMessageAttachmentTx: " + attachmentId);
	}
	
	/**
	 * Inserts attachment and attachment info
	 * 
	 * @param attachment	Attachment wiht AttachmentInfo
	 * 
	 * @return	New created attachment id
	 */
	protected abstract int insertAttachment(Attachment attachment);
}
