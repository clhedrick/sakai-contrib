/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/dao/mysql/AttachmentDaoMysql.java $ 
 * $Id: AttachmentDaoMysql.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum.dao.mysql;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Attachment;
import org.etudes.component.app.jforum.AttachmentImpl;
import org.etudes.component.app.jforum.dao.generic.AttachmentDaoGeneric;

public class AttachmentDaoMysql extends AttachmentDaoGeneric
{
	private static Log logger = LogFactory.getLog(AttachmentDaoMysql.class);
	
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
	protected int insertAttachment(Attachment attachment)
	{
		
		// attachment
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO jforum_attach (post_id, privmsgs_id, user_id) VALUES (?, ?, ?)");
		
		Object[] fields = new Object[3];
		int i = 0;
		
		fields[i++] =  attachment.getPostId();
		fields[i++] =  attachment.getPrivmsgsId();
		fields[i++] =  attachment.getUserId();
		
		Long id = null;
		try
		{
			id = this.sqlService.dbInsert(null, sql.toString(), fields, "attach_id");
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e, e);
			}
		}
		
		if (id == null)
		{
			throw new RuntimeException("insertAttachment: dbInsert failed");
		}
		int attachId  = id.intValue();
		
		// attachment info
		sql = new StringBuilder();
		sql.append("INSERT INTO jforum_attach_desc (attach_id, physical_filename, real_filename, description, ");
		sql.append("mimetype, filesize, upload_time, thumb, extension_id ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		fields = new Object[9];
		i = 0;
		id = null;
		
		fields[i++] =  attachId;
		fields[i++] =  attachment.getInfo().getPhysicalFilename();
		fields[i++] =  attachment.getInfo().getRealFilename();
		fields[i++] =  attachment.getInfo().getComment();
		fields[i++] =  attachment.getInfo().getMimetype();
		fields[i++] =  attachment.getInfo().getFilesize();
		fields[i++] =  new Timestamp(attachment.getInfo().getUploadTime().getTime());
		fields[i++] =  0;
		fields[i++] =  attachment.getInfo().getExtension().getId();
		
		try
		{
			id = this.sqlService.dbInsert(null, sql.toString(), fields, "attach_id");
		}
		catch (Exception e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e, e);
			}
		}
		
		if (id == null)
		{
			throw new RuntimeException("insertAttachment: Attachment description dbInsert failed");
		}
			
		// if attachment is for post update post for attach column
		if (attachment.getPostId() > 0)
		{
			sql = new StringBuilder();
			sql.append("UPDATE jforum_posts SET attach = ? WHERE post_id = ?");
			
			fields = new Object[2];
			i = 0;
			
			fields[i++] =  1;
			fields[i++] = attachment.getPostId();
			
			if (!sqlService.dbWrite(sql.toString(), fields)) 
			{
				throw new RuntimeException("insertAttachment: updating jforum posts for attach failed");
			}
		}
		((AttachmentImpl)attachment).setId(attachId);
		
		return attachId;		
	}

}
