/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/AttachmentInfoImpl.java $ 
 * $Id: AttachmentInfoImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

import java.util.Date;

import org.etudes.api.app.jforum.AttachmentExtension;
import org.etudes.api.app.jforum.AttachmentInfo;


public class AttachmentInfoImpl implements AttachmentInfo
{
	protected int attachId;
	
	protected String comment;

	protected int downloadCount;

	protected AttachmentExtension extension;

	protected byte[] fileContent;

	protected long filesize;

	protected boolean hasThumb;

	protected int id;

	protected String mimetype;

	protected String physicalFilename;

	protected String realFilename;
	
	protected Date uploadTime;
	
	protected AttachmentInfoImpl(AttachmentInfoImpl other)
	{
		this.attachId = other.attachId;
		this.comment = other.comment;
		this.downloadCount = other.downloadCount;
		this.extension = other.extension;
		//this.fileContent = other.fileContent;
		this.filesize = other.filesize;
		this.hasThumb = other.hasThumb;
		this.id = other.id;
		this.mimetype = other.mimetype;
		this.physicalFilename =other.physicalFilename;
		this.realFilename = other.realFilename;
		this.uploadTime = other.uploadTime;
	}
	
	public AttachmentInfoImpl(){}

	/**
	 * {@inheritDoc}
	 */
	public int getAttachId()
	{
		return attachId;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getDownloadCount()
	{
		return downloadCount;
	}

	/**
	 * {@inheritDoc}
	 */
	public AttachmentExtension getExtension()
	{
		return extension;
	}

	/**
	 * @return the fileContent
	 */
	public byte[] getFileContent()
	{
		return fileContent;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getFilesize()
	{
		return filesize;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getMimetype()
	{
		return mimetype;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPhysicalFilename()
	{
		return physicalFilename;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getRealFilename()
	{
		return realFilename;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getUploadTime()
	{
		return uploadTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isHasThumb()
	{
		return hasThumb;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAttachId(int attachId)
	{
		this.attachId = attachId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDownloadCount(int downloadCount)
	{
		this.downloadCount = downloadCount;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setExtension(AttachmentExtension extension)
	{
		this.extension = extension;
	}
	
	/**
	 * @param fileContent the fileContent to set
	 */
	public void setFileContent(byte[] fileContent)
	{
		this.fileContent = fileContent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setFilesize(long filesize)
	{
		this.filesize = filesize;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setHasThumb(boolean hasThumb)
	{
		this.hasThumb = hasThumb;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMimetype(String mimetype)
	{
		this.mimetype = mimetype;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setPhysicalFilename(String physicalFilename)
	{
		this.physicalFilename = physicalFilename;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setRealFilename(String realFilename)
	{
		this.realFilename = realFilename;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setUploadTime(Date uploadTime)
	{
		this.uploadTime = uploadTime;
	}
}
