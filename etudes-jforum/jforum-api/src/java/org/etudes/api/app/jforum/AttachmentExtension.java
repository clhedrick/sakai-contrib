/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/AttachmentExtension.java $ 
 * $Id: AttachmentExtension.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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

public interface AttachmentExtension
{
	/**
	 * @return the comment
	 */
	public String getComment();
	
	/**
	 * @return the extension
	 */
	public String getExtension();
	
	/**
	 * @return the extensionGroupId
	 */
	public int getExtensionGroupId();
	
	/**
	 * @return the id
	 */
	public int getId();
	
	/**
	 * @return the uploadIcon
	 */
	public String getUploadIcon();
	
	/**
	 * @return the allow
	 */
	public boolean isAllow();

	/**
	 * @return the physicalDownloadMode
	 */
	public boolean isPhysicalDownloadMode();
	
	/**
	 * @return the unknown
	 */
	public boolean isUnknown();
	
	/**
	 * @param allow the allow to set
	 */
	public void setAllow(boolean allow);
	
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment);
	
	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension);
	
	/**
	 * @param extensionGroupId the extensionGroupId to set
	 */
	public void setExtensionGroupId(int extensionGroupId);
	
	/**
	 * @param physicalDownloadMode the physicalDownloadMode to set
	 */
	public void setPhysicalDownloadMode(boolean physicalDownloadMode);
	
	/**
	 * @param unknown the unknown to set
	 */
	public void setUnknown(boolean unknown);

	/**
	 * @param uploadIcon the uploadIcon to set
	 */
	public void setUploadIcon(String uploadIcon);
}
