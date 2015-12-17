/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/AttachmentInfo.java $ 
 * $Id: AttachmentInfo.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

public interface AttachmentInfo
{
	
	/**
	 * @return the attachId
	 */
	public int getAttachId();
	

	/**
	 * @return the comment
	 */
	public String getComment();
	

	/**
	 * @return the downloadCount
	 */
	public int getDownloadCount();

	/**
	 * @return the extension
	 */
	public AttachmentExtension getExtension();

	/**
	 * @return the fileContent while saving the file
	 */
	public byte[] getFileContent();

	/**
	 * @return the filesize
	 */
	public long getFilesize();

	/**
	 * @return the id
	 */
	public int getId();

	/**
	 * @return the mimetype
	 */
	public String getMimetype();

	/**
	 * @return the physicalFilename
	 */
	public String getPhysicalFilename();

	/**
	 * @return the realFilename
	 */
	public String getRealFilename();

	/**
	 * @return the uploadTime
	 */
	public Date getUploadTime();

	/**
	 * @return the hasThumb
	 */
	public boolean isHasThumb();

	/**
	 * @param attachId the attachId to set
	 */
	public void setAttachId(int attachId);

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment);

	/**
	 * @param downloadCount the downloadCount to set
	 */
	public void setDownloadCount(int downloadCount);

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(AttachmentExtension extension);

	/**
	 * @param fileContent the fileContent to set while saving the file
	 */
	public void setFileContent(byte[] fileContent);

	/**
	 * @param filesize the filesize to set
	 */
	public void setFilesize(long filesize);

	/**
	 * @param hasThumb the hasThumb to set
	 */
	public void setHasThumb(boolean hasThumb);

	/**
	 * @param mimetype the mimetype to set
	 */
	public void setMimetype(String mimetype);

	/**
	 * @param physicalFilename the physicalFilename to set
	 */
	public void setPhysicalFilename(String physicalFilename);

	/**
	 * @param realFilename the realFilename to set
	 */
	public void setRealFilename(String realFilename);

	/**
	 * @param uploadTime the uploadTime to set
	 */
	public void setUploadTime(Date uploadTime);

}
